package org.univ.tech.tool.docs.api.java;

import java.text.MessageFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.api.ApiParser;
import org.univ.tech.tool.docs.utils.JsoupUtils;

public class Javase11ApiParser extends ApiParser {

	public static void main(String[] args) {
		new Javase11ApiParser().writeAll();
	}

	@Override
	protected String getApiName() {
		return "JavaSE 11 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.oracle.com/en/java/javase/11/docs/api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/java/javase11/api";
	}

	@Override
	protected String getAllClassUrl() {
		return MessageFormat.format("{0}/allclasses.html", getApiUrl());
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
	protected Elements parseAllClass(Document htmlDocument) {
		return JsoupUtils.getElementsByTagAndClass(htmlDocument, "main", "indexContainer");
	}

}
