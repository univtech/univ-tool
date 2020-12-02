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
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.utils.JsoupUtils;

public abstract class ApiParser {

	protected abstract String getApiName();

	protected abstract String getApiUrl();

	protected abstract String getApiPath();

	protected String getAllClassUrl() {
		return MessageFormat.format("{0}/allclasses-noframe.html", getApiUrl());
	}

	protected String getOverviewUrl() {
		return MessageFormat.format("{0}/overview-summary.html", getApiUrl());
	}

	protected String getPackageUrl(String packageName) {
		return MessageFormat.format("{0}/{1}/package-summary.html", getApiUrl(), packageName.replaceAll("\\.", "\\/"));
	}

	protected String getPackageUrl(String moduleName, String packageName) {
		return MessageFormat.format("{0}/{1}/{2}/package-summary.html", getApiUrl(), moduleName, packageName.replaceAll("\\.", "\\/"));
	}

	protected String getAllClassPath() {
		return MessageFormat.format("{0}/所有类.md", getApiPath());
	}

	protected String getOverviewPath() {
		return MessageFormat.format("{0}/所有包.md", getApiPath());
	}

	protected String getPackagePath(String packageName) {
		return MessageFormat.format("{0}/{1}.md", getApiPath(), packageName);
	}

	public void writeAll() {
		writeAllClass();
		if (writeModule()) {
			writeOverviewOfModule();
		} else {
			writeOverviewOfPackage();
		}
		printAllClass();
	}

	public void writeAllClass() {
		List<String> fullClassNames = parseAllClass(getAllClassUrl());
		List<String> lines = buildLines(getApiName(), fullClassNames);
		writeLines(lines, getAllClassPath());
	}

	public void writeOverviewOfModule() {
		Map<String, List<String>> packageNameMap = new LinkedHashMap<>();
		List<String> moduleNames = parseOverview(getOverviewUrl());
		for (String moduleName : moduleNames) {
			List<String> packageNames = parseModule(moduleName, getPackageUrl(moduleName));
			for (String packageName : packageNames) {
				writePackage(packageName, getPackageUrl(moduleName, packageName), getPackagePath(packageName));
			}
			packageNameMap.put(moduleName, packageNames);
		}
		List<String> lines = buildLines(getApiName(), packageNameMap, false);
		writeLines(lines, getOverviewPath());
	}

	public void writeOverviewOfPackage() {
		List<String> packageNames = parseOverview(getOverviewUrl());
		for (String packageName : packageNames) {
			writePackage(packageName, getPackageUrl(packageName), getPackagePath(packageName));
		}
		List<String> lines = buildLines(getApiName(), packageNames);
		writeLines(lines, getOverviewPath());
	}

	protected void writePackage(String packageName, String packageUrl, String packagePath) {
		Map<String, List<String>> fullClassNameMap = parsePackage(packageName, packageUrl);
		if (MapUtils.isEmpty(fullClassNameMap)) {
			return;
		}
		List<String> lines = buildLines(packageName, fullClassNameMap, true);
		writeLines(lines, packagePath);
	}

	protected boolean writeModule() {
		return false;
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
			File[] files = new File(getApiPath()).listFiles(file -> !ignoreFiles.contains(file.getName()));

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
			printLines(fullClassNames);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean printGenericType() {
		return false;
	}

	protected void printLines(List<String> lines) {
		for (String line : lines) {
			System.out.println(line);
		}
	}

	protected void writeLines(List<String> lines, String path) {
		try {
			if (StringUtils.isEmpty(path)) {
				printLines(lines);
			} else {
				FileUtils.writeLines(new File(path), lines);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
