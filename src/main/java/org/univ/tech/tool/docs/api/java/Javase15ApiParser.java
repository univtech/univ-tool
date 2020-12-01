package org.univ.tech.tool.docs.api.java;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.api.ApiParser;
import org.univ.tech.tool.utils.JsoupUtils;

public class Javase15ApiParser extends ApiParser {

	public static void main(String[] args) {
		new Javase15ApiParser().writeAllClass();
	}

	@Override
	protected String getApiName() {
		return "JavaSE 15 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.oracle.com/en/java/javase/15/docs/api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/java/javase15/api";
	}

	@Override
	protected String getAllClassUrl() {
		return MessageFormat.format("{0}/allclasses-index.html", getApiUrl());
	}

	@Override
	protected String getOverviewUrl() {
		return MessageFormat.format("{0}/index.html", getApiUrl());
	}

	@Override
	protected String getPackageUrl(String moduleName) {
		return MessageFormat.format("{0}/{1}/module-summary.html", getApiUrl(), moduleName);
	}

	@Override
	protected boolean hasModules() {
		return true;
	}

	@Override
	protected List<String> parseAllClass(String allClassUrl) {
		try {
			Document htmlDocument = Jsoup.connect(allClassUrl).get();
			Elements tableElements = JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "summary-table");
			if (CollectionUtils.isEmpty(tableElements)) {
				return Collections.emptyList();
			}

			Element tableElement = tableElements.get(0);
			List<String> fullClassNames = parseColumnFirst(tableElement, 1, true);
			Collections.sort(fullClassNames);
			return fullClassNames;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	protected Elements parseOverview(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "summary-table");
	}

	@Override
	protected Elements parseModule(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "summary-table");
	}

	@Override
	protected Elements parsePackage(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "table", "summary-table");
	}

	@Override
	protected Elements parseColumnFirst(Element tableElement) {
		return tableElement.getElementsByClass("col-first");
	}

	@Override
	protected boolean printGenericType() {
		return true;
	}

}
