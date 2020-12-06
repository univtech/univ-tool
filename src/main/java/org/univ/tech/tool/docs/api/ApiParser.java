package org.univ.tech.tool.docs.api;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.href.HtmlHrefParser;
import org.univ.tech.tool.docs.utils.JsoupUtils;
import org.univ.tech.tool.docs.utils.LineUtils;

public abstract class ApiParser {

	protected abstract String getProjectPath();

	protected abstract String getApiName();

	protected abstract String getApiUrl();

	protected abstract String getAllClassUri();

	protected abstract String getOverviewUri();

	protected abstract String getModuleUri();

	protected abstract String getPackageUri();

	public void writeAll() {
		writeAllClass();
		if (writeModule()) {
			writeModuleOverview();
		} else {
			writePackageOverview();
		}
		printAllClass();
	}

	protected abstract boolean writeModule();

	public void writeAllClass() {
		String allClassUrl = MessageFormat.format("{0}/{1}", getApiUrl(), getAllClassUri());
		List<String> fullClassNames = parseAllClass(allClassUrl);

		List<String> lines = buildLines(getApiName(), fullClassNames);
		String allClassPath = MessageFormat.format("{0}/api/所有类.md", getProjectPath());
		LineUtils.writeLines(lines, allClassPath, false);
	}

	public void writeModuleOverview() {
		String overviewUrl = MessageFormat.format("{0}/{1}", getApiUrl(), getOverviewUri());
		List<String> moduleNames = parseOverview(overviewUrl);

		Map<String, List<String>> packageNameMap = new LinkedHashMap<>();
		for (String moduleName : moduleNames) {
			String moduleUrl = MessageFormat.format("{0}/{1}/{2}", getApiUrl(), moduleName, getModuleUri());
			List<String> packageNames = parseModule(moduleName, moduleUrl);
			packageNameMap.put(moduleName, packageNames);

			for (String packageName : packageNames) {
				String packageUrl = MessageFormat.format("{0}/{1}/{2}/{3}", getApiUrl(), moduleName, packageName.replaceAll("\\.", "\\/"), getPackageUri());
				writePackage(packageName, packageUrl);
			}
		}

		List<String> lines = buildLines(getApiName(), packageNameMap, false);
		String overviewPath = MessageFormat.format("{0}/api/所有包.md", getProjectPath());
		LineUtils.writeLines(lines, overviewPath, false);
	}

	public void writePackageOverview() {
		String overviewUrl = MessageFormat.format("{0}/{1}", getApiUrl(), getOverviewUri());
		List<String> packageNames = parseOverview(overviewUrl);

		for (String packageName : packageNames) {
			String packageUrl = MessageFormat.format("{0}/{1}/{2}", getApiUrl(), packageName.replaceAll("\\.", "\\/"), getPackageUri());
			writePackage(packageName, packageUrl);
		}

		List<String> lines = buildLines(getApiName(), packageNames);
		String overviewPath = MessageFormat.format("{0}/api/所有包.md", getProjectPath());
		LineUtils.writeLines(lines, overviewPath, false);
	}

	protected void writePackage(String packageName, String packageUrl) {
		Map<String, List<String>> fullClassNameMap = parsePackage(packageName, packageUrl);
		if (MapUtils.isEmpty(fullClassNameMap)) {
			return;
		}

		List<String> lines = buildLines(packageName, fullClassNameMap, true);
		String packagePath = MessageFormat.format("{0}/api/{1}.md", getProjectPath(), packageName);
		LineUtils.writeLines(lines, packagePath, false);
	}

	protected void writeHrefs(String url, String tag, List<String> ignoreTags, String path, boolean append) {
		List<String> hrefs = new HtmlHrefParser().parseHrefs(url, tag, ignoreTags);
		String hrefPath = MessageFormat.format("{0}/{1}", getProjectPath(), path);
		LineUtils.writeLines(hrefs, hrefPath, append);
	}

	protected List<String> parseAllClass(String allClassUrl) {
		try {
			Document htmlDocument = Jsoup.connect(allClassUrl).get();
			Elements containerElements = parseAllClass(htmlDocument);
			if (CollectionUtils.isEmpty(containerElements)) {
				return Collections.emptyList();
			}

			Element containerElement = containerElements.get(0);
			Elements anchorElements = containerElement.getElementsByTag("a");

			List<String> fullClassNames = new ArrayList<>();
			for (Element anchorElement : anchorElements) {
				String title = anchorElement.attr("title");
				String packageName = title.substring(title.indexOf("in ") + "in ".length());
				fullClassNames.add(MessageFormat.format("{0}.{1}", packageName, anchorElement.text()));
			}
			Collections.sort(fullClassNames);
			return fullClassNames;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	protected Elements parseAllClass(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "div", "indexContainer");
	}

	protected List<String> parseOverview(String overviewUrl) {
		return parseTable(overviewUrl, "overviewSummary", 1, false);
	}

	protected List<String> parseModule(String moduleName, String moduleUrl) {
		try {
			Document htmlDocument = Jsoup.connect(moduleUrl).get();
			Elements tableElements = parseModule(htmlDocument);
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyList();
			}

			for (Element tableElement : tableElements) {
				List<String> packageNames = parseColumnFirst(tableElement, 0, false);
				if (CollectionUtils.isNotEmpty(packageNames) && "Package".equals(packageNames.remove(0))) {
					Collections.sort(packageNames);
					return packageNames;
				}
			}
			return Collections.emptyList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	protected Elements parseModule(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "packagesSummary");
	}

	protected Map<String, List<String>> parsePackage(String packageName, String packageUrl) {
		try {
			Document htmlDocument = Jsoup.connect(packageUrl).get();
			Elements tableElements = parsePackage(htmlDocument);
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyMap();
			}

			Map<String, List<String>> fullClassNameMap = ClassType.getClassTypeMap();
			for (Element tableElement : tableElements) {
				List<String> fullClassNames = parseColumnFirst(tableElement, 0, true);
				if (CollectionUtils.isNotEmpty(fullClassNames)) {
					String classTypeName = ClassType.getZhName(fullClassNames.remove(0));
					Collections.sort(fullClassNames);
					fullClassNameMap.get(classTypeName).addAll(fullClassNames);
				}
			}
			return fullClassNameMap;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	protected Elements parsePackage(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "typeSummary");
	}

	protected List<String> parseTable(String url, String className, int beginIndex, boolean parseFullText) {
		try {
			Document htmlDocument = Jsoup.connect(url).get();
			Elements tableElements = JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", className);
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyList();
			}

			Element tableElement = tableElements.get(0);
			List<String> columnFirstTexts = parseColumnFirst(tableElement, beginIndex, parseFullText);
			Collections.sort(columnFirstTexts);
			return columnFirstTexts;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	protected List<String> parseColumnFirst(Element tableElement, int beginIndex, boolean parseFullText) {
		List<Integer> beginIndexs = Arrays.asList(0, 1);
		if (!beginIndexs.contains(beginIndex)) {
			return Collections.emptyList();
		}

		Elements columnFirstElements = parseColumnFirst(tableElement);
		if (CollectionUtils.isEmpty(columnFirstElements) || columnFirstElements.size() < beginIndexs.size()) {
			return Collections.emptyList();
		}

		List<String> columnFirstTexts = new ArrayList<>();
		for (int index = beginIndex; index < columnFirstElements.size(); index++) {
			Element columnFirstElement = columnFirstElements.get(index);
			Elements anchorElements = columnFirstElement.getElementsByTag("a");
			String columnFirstText = columnFirstElement.text();

			if (parseFullText && CollectionUtils.isNotEmpty(anchorElements)) {
				String title = anchorElements.get(0).attr("title");
				String packageName = title.substring(title.indexOf("in ") + "in ".length());
				columnFirstText = MessageFormat.format("{0}.{1}", packageName, columnFirstText);
			}
			columnFirstTexts.add(columnFirstText);
		}
		return columnFirstTexts;
	}

	protected Elements parseColumnFirst(Element tableElement) {
		return tableElement.getElementsByClass("colFirst");
	}

	protected List<String> buildLines(String title, List<String> contents) {
		List<String> lines = new ArrayList<>();
		lines.add(MessageFormat.format("# {0}\r\n", title));
		lines.addAll(contents);
		lines.add("\r\n\r\n\r\n");
		return lines;
	}

	protected List<String> buildLines(String title, Map<String, List<String>> contentMap, boolean ignoreEmptyContent) {
		List<String> lines = new ArrayList<>();
		lines.add(MessageFormat.format("# {0}", title));
		for (Entry<String, List<String>> contentEntry : contentMap.entrySet()) {
			if (ignoreEmptyContent && CollectionUtils.isEmpty(contentEntry.getValue())) {
				continue;
			}
			lines.add(MessageFormat.format("\r\n## {0}\r\n", contentEntry.getKey()));
			lines.addAll(contentEntry.getValue());
		}
		lines.add("\r\n\r\n\r\n");
		return lines;
	}

	public void printAllClass() {
		try {
			List<String> ignoreFiles = Arrays.asList("README.md", "所有类.md", "所有包.md");
			String path = MessageFormat.format("{0}/api", getProjectPath());
			File[] files = new File(path).listFiles(file -> !ignoreFiles.contains(file.getName()));

			List<String> fullClassNames = new ArrayList<>();
			for (File file : files) {
				String fileName = file.getName();
				String packageName = fileName.substring(0, fileName.lastIndexOf(".md"));
				List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

				for (String line : lines) {
					if (line.startsWith(packageName)) {
						if (!printGenericType() && line.contains("<")) {
							line = line.substring(0, line.indexOf("<"));
						}
						fullClassNames.add(line);
					}
				}
			}

			Collections.sort(fullClassNames);
			LineUtils.printLines(fullClassNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean printGenericType() {
		return false;
	}

}
