package org.univ.tech.tool.docs.api.spring;

public class SpringSecurityKerberosApiParser extends SpringApiParser {

	public static void main(String[] args) {
		new SpringSecurityKerberosApiParser().writeAll();
	}

	@Override
	protected String getProjectPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-security-kerberos-1.0.1";
	}

	protected String getProjectUrl() {
		return "https://spring.io/projects/spring-security-kerberos";
	}

	@Override
	protected String getApiName() {
		return "Spring Security Kerberos 1.0.1 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-security-kerberos/docs/1.0.1.RELEASE/api";
	}

}
