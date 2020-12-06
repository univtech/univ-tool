package org.univ.tech.tool.docs.api.spring;

public class SpringBootApiParser extends SpringApiParser {

	public static void main(String[] args) {
		new SpringBootApiParser().writeAll();
	}

	@Override
	protected String getProjectPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-boot-2.4.0";
	}

	protected String getProjectUrl() {
		return "https://spring.io/projects/spring-boot";
	}

	@Override
	protected String getApiName() {
		return "Spring Boot 2.4.0 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-boot/docs/current/api";
	}

}
