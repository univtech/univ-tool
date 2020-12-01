package org.univ.tech.tool.docs.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum ClassType {

	Annotation(0, "Annotation Type", "注解"),

	Enum(1, "Enum", "枚举"),

	Interface(2, "Interface", "接口"),

	Class(3, "Class", "类"),

	Exception(4, "Exception", "异常"),

	Error(5, "Error", "错误");

	private int order;

	private String enName;

	private String zhName;

	private ClassType(int order, String enName, String zhName) {
		this.order = order;
		this.enName = enName;
		this.zhName = zhName;
	}

	public int getOrder() {
		return order;
	}

	public String getEnName() {
		return enName;
	}

	public String getZhName() {
		return zhName;
	}

	public static int getOrder(String enName) {
		ClassType classType = getClassType(enName);
		return classType == null ? values().length : classType.getOrder();
	}

	public static String getZhName(String enName) {
		ClassType classType = getClassType(enName);
		return classType == null ? enName : classType.getZhName();
	}

	public static ClassType getClassType(String enName) {
		for (ClassType classType : values()) {
			if (classType.getEnName().equals(enName)) {
				return classType;
			}
		}
		return null;
	}

	public static Map<String, List<String>> getClassTypeMap() {
		Map<String, List<String>> classTypeMap = new LinkedHashMap<>();
		for (ClassType classType : values()) {
			classTypeMap.put(classType.getZhName(), new ArrayList<>());
		}
		return classTypeMap;
	}

}
