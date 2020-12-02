package org.univ.tech.tool.docs.api.spring;

import org.univ.tech.tool.docs.api.ApiParser;

public class SpringBootApiParser extends ApiParser {

	public static void main(String[] args) {
		new SpringBootApiParser().writeAll();
	}

	@Override
	protected String getApiName() {
		return "Spring Boot 2.4.0 API";
	}

	@Override
	protected String getApiUrl() {
		return "https://docs.spring.io/spring-boot/docs/current/api";
	}

	@Override
	protected String getApiPath() {
		return "D:/Workspace/univtech/univ-tech/spring/spring-boot-2.4.0/api";
	}

}
