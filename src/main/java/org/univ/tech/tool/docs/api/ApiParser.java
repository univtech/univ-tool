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
import org.univ.tech.tool.utils.JsoupUtils;

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

	private String getAllClassPath() {
		return MessageFormat.format("{0}/所有类.md", getApiPath());
	}

	private String getOverviewPath() {
		return MessageFormat.format("{0}/所有包.md", getApiPath());
	}

	private String getPackagePath(String packageName) {
		return MessageFormat.format("{0}/{1}.md", getApiPath(), packageName);
	}

	protected boolean hasModules() {
		return false;
	}

	protected void writeAllClass() {
		writeAllClass(getApiName(), getAllClassUrl(), getAllClassPath());
		if (hasModules()) {
			writeOverviewOfModule(getApiName(), getOverviewUrl(), getOverviewPath());
		} else {
			writeOverviewOfPackage(getApiName(), getOverviewUrl(), getOverviewPath());
		}
		printAllClass(getApiPath());
	}

	private void writeAllClass(String apiName, String allClassUrl, String allClassPath) {
		List<String> fullClassNames = parseAllClass(allClassUrl);
		List<String> lines = buildLines(apiName, fullClassNames);
		writeLines(lines, allClassPath);
	}

	private void writeOverviewOfModule(String apiName, String overviewUrl, String overviewPath) {
		Map<String, List<String>> packageNameMap = new LinkedHashMap<>();
		List<String> moduleNames = parseOverview(overviewUrl);
		for (String moduleName : moduleNames) {
			List<String> packageNames = parseModule(moduleName, getPackageUrl(moduleName));
			for (String packageName : packageNames) {
				writePackage(packageName, getPackageUrl(moduleName, packageName), getPackagePath(packageName));
			}
			packageNameMap.put(moduleName, packageNames);
		}
		List<String> lines = buildLines(apiName, packageNameMap, false);
		writeLines(lines, overviewPath);
	}

	private void writeOverviewOfPackage(String apiName, String overviewUrl, String overviewPath) {
		List<String> packageNames = parseOverview(overviewUrl);
		for (String packageName : packageNames) {
			writePackage(packageName, getPackageUrl(packageName), getPackagePath(packageName));
		}
		List<String> lines = buildLines(apiName, packageNames);
		writeLines(lines, overviewPath);
	}

	private void writePackage(String packageName, String packageUrl, String packagePath) {
		Map<String, List<String>> fullClassNameMap = parsePackage(packageName, packageUrl);
		if (MapUtils.isEmpty(fullClassNameMap)) {
			return;
		}
		List<String> lines = buildLines(packageName, fullClassNameMap, true);
		writeLines(lines, packagePath);
	}

	private List<String> parseAllClass(String allClassUrl) {
		try {
			Document htmlDocument = Jsoup.connect(allClassUrl).get();
			Elements containerElements = parseAllClassContainer(htmlDocument);
			if (CollectionUtils.isEmpty(containerElements)) {
				return Collections.emptyList();
			}

			Element containerElement = containerElements.get(0);
			Elements aElements = containerElement.getElementsByTag("a");

			String in = "in ";
			List<String> fullClassNames = new ArrayList<>();
			for (Element aElement : aElements) {
				String title = aElement.attr("title");
				String packageName = title.substring(title.indexOf(in) + in.length());
				fullClassNames.add(MessageFormat.format("{0}.{1}", packageName, aElement.text()));
			}
			Collections.sort(fullClassNames);
			return fullClassNames;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	protected Elements parseAllClassContainer(Element element) {
		return JsoupUtils.getElementsByTagAndClass(element, "div", "indexContainer");
	}

	private List<String> parseOverview(String overviewUrl) {
		try {
			Document htmlDocument = Jsoup.connect(overviewUrl).get();
			Elements tableElements = JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "overviewSummary");
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyList();
			}

			Element tableElement = tableElements.get(0);
			List<String> columnFirstTexts = parseColumnFirst(tableElement, 1);
			Collections.sort(columnFirstTexts);
			return columnFirstTexts;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private List<String> parseModule(String moduleName, String moduleUrl) {
		try {
			Document htmlDocument = Jsoup.connect(moduleUrl).get();
			Elements tableElements = JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "packagesSummary");
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyList();
			}

			for (Element tableElement : tableElements) {
				List<String> packageNames = parseColumnFirst(tableElement, 0);
				if ("Package".equals(packageNames.remove(0))) {
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

	private Map<String, List<String>> parsePackage(String packageName, String packageUrl) {
		try {
			Document htmlDocument = Jsoup.connect(packageUrl).get();
			Elements tableElements = JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "typeSummary");
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyMap();
			}

			Map<String, List<String>> fullClassNameMap = ClassType.getClassTypeMap();
			for (Element tableElement : tableElements) {
				List<String> classNames = parseColumnFirst(tableElement, 0);
				if (CollectionUtils.isEmpty(classNames)) {
					continue;
				}

				String classTypeName = ClassType.getZhName(classNames.remove(0));
				List<String> fullClassNames = fullClassNameMap.get(classTypeName);

				for (String className : classNames) {
					fullClassNames.add(MessageFormat.format("{0}.{1}", packageName, className));
				}
				Collections.sort(fullClassNames);
			}
			return fullClassNameMap;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	private List<String> parseColumnFirst(Element tableElement, int beginIndex) {
		List<Integer> beginIndexs = Arrays.asList(0, 1);
		if (!beginIndexs.contains(beginIndex)) {
			return Collections.emptyList();
		}

		Elements columnFirstElements = tableElement.getElementsByClass("colFirst");
		if (CollectionUtils.isEmpty(columnFirstElements) || columnFirstElements.size() < beginIndexs.size()) {
			return Collections.emptyList();
		}

		List<String> columnFirstTexts = new ArrayList<>();
		for (int index = beginIndex; index < columnFirstElements.size(); index++) {
			columnFirstTexts.add(columnFirstElements.get(index).text());
		}
		return columnFirstTexts;
	}

	private List<String> buildLines(String title, List<String> contents) {
		List<String> lines = new ArrayList<>();
		lines.add(MessageFormat.format("# {0}\r\n", title));
		lines.addAll(contents);
		lines.add("\r\n\r\n\r\n");
		return lines;
	}

	private List<String> buildLines(String title, Map<String, List<String>> contentMap, boolean ignoreEmptyContent) {
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

	private void printAllClass(String apiPath) {
		try {
			List<String> ignoreFiles = Arrays.asList("README.md", "所有类.md", "所有包.md");
			File[] files = new File(apiPath).listFiles((file) -> {
				return !ignoreFiles.contains(file.getName());
			});

			List<String> fullClassNames = new ArrayList<>();
			for (File file : files) {
				String fileName = file.getName();
				String packageName = fileName.substring(0, fileName.lastIndexOf(".md"));
				List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

				for (String line : lines) {
					if (line.startsWith(packageName)) {
						if (line.contains("<")) {
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

	private void printLines(List<String> lines) {
		for (String line : lines) {
			System.out.println(line);
		}
	}

	private void writeLines(List<String> lines, String path) {
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
