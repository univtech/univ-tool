package org.univ.tech.tool.docs.api.spring;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.univ.tech.tool.docs.api.ApiParser;

public abstract class SpringApiParser extends ApiParser {

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

	protected void writeHrefs(String url) {
		writeHrefs(url, "article", Arrays.asList("header", "footer"), "README.md", true);
	}

}
