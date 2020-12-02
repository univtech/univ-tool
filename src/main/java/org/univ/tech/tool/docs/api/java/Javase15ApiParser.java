package org.univ.tech.tool.docs.api.java;

import java.text.MessageFormat;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.api.ApiParser;
import org.univ.tech.tool.docs.utils.JsoupUtils;

public class Javase15ApiParser extends ApiParser {

	public static void main(String[] args) {
		new Javase15ApiParser().writeAll();
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
	protected boolean writeModule() {
		return true;
	}

	@Override
	protected List<String> parseAllClass(String allClassUrl) {
		return parseTable(allClassUrl, "summary-table", 1, true);
	}

	@Override
	protected List<String> parseOverview(String overviewUrl) {
		return parseTable(overviewUrl, "summary-table", 1, false);
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
