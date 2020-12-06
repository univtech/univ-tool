package org.univ.tech.tool.docs.utils;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class LineUtils {

	public static void writeLines(List<String> lines, String path, boolean append) {
		try {
			if (StringUtils.isEmpty(path)) {
				printLines(lines);
			} else {
				FileUtils.writeLines(new File(path), lines, append);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printLines(List<String> lines) {
		for (String line : lines) {
			System.out.println(line);
		}
	}

}
