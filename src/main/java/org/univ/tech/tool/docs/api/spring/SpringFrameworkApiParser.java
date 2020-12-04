package org.univ.tech.tool.docs.api.spring;

public class SpringFrameworkApiParser extends SpringApiParser {

	public static void main(String[] args) {
		new SpringFrameworkApiParser().writeAll();
	}

	@Override
	protected String getProjectUrl() {
		return "https://spring.io/projects/spring-framework";
	}

	@Override
	protected String getApiName() {
		return "Spring Framework 5.3.1 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-framework/docs/current/javadoc-api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-framework-5.3.1/api";
	}

}
