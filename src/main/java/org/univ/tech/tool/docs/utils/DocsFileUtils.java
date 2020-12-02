package org.univ.tech.tool.docs.utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class DocsFileUtils {

	public static List<String> readLines() {
		try {
			return FileUtils.readLines(new File(getDocsFilePath()), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public static String getDocsFilePath() {
		return DocsFileUtils.class.getResource("/docs.txt").getPath();
	}

}
