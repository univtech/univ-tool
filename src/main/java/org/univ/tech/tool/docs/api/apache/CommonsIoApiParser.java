package org.univ.tech.tool.docs.api.apache;

import org.apache.commons.lang3.StringUtils;
import org.univ.tech.tool.docs.api.ApiParser;

public class CommonsIoApiParser extends ApiParser {

	public static void main(String[] args) {
		new CommonsIoApiParser().writeAll();
	}

	@Override
	protected String getProjectPath() {
		return "D:/Workspace/univtech/univ-tech/apache/commons/commons-io-2.8.0";
	}

	@Override
	protected String getApiName() {
		return "Apache Commons IO 2.8.0 API";
	}

	@Override
	protected String getApiUrl() {
		return "http://commons.apache.org/proper/commons-io/apidocs";
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
