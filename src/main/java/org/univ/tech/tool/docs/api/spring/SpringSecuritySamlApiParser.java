package org.univ.tech.tool.docs.api.spring;

public class SpringSecuritySamlApiParser extends SpringApiParser {

	public static void main(String[] args) {
		new SpringSecuritySamlApiParser().writeAll();
	}

	@Override
	protected String getProjectPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-security-saml-1.0.10";
	}

	protected String getProjectUrl() {
		return "https://spring.io/projects/spring-security-saml";
	}

	@Override
	protected String getApiName() {
		return "Spring Security SAML 1.0.10 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-security-saml/docs/1.0.10.RELEASE/api";
	}

}
