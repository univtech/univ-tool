package org.univ.tech.tool.docs.api.spring;

public class SpringSecurityApiParser extends SpringApiParser {

	public static void main(String[] args) {
		new SpringSecurityApiParser().writeAll();
	}

	@Override
	protected String getProjectUrl() {
		return "https://spring.io/projects/spring-security";
	}

	@Override
	protected String getApiName() {
		return "Spring Security 5.4.1 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-security/site/docs/5.4.1/api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-security-5.4.1/api";
	}

}
