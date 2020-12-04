package org.univ.tech.tool.docs.api.java;

import org.apache.commons.lang3.StringUtils;
import org.univ.tech.tool.docs.api.ApiParser;

public class Javase8ApiParser extends ApiParser {

	public static void main(String[] args) {
		new Javase8ApiParser().writeAll();
	}

	@Override
	protected String getApiName() {
		return "JavaSE 8 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.oracle.com/javase/8/docs/api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/java/javase8/api";
	}

	@Override
	protected String getAllClassUri() {
		return "allclasses-noframe.html";
	}

	@Override
	protected String getOverviewUri() {
		return "overview-summary.html";
	}

	@Override
	protected String getModuleUri() {
		return StringUtils.EMPTY;
	}

	@Override
	protected String getPackageUri() {
		return "package-summary.html";
	}

	@Override
	protected boolean writeModule() {
		return false;
	}

}
