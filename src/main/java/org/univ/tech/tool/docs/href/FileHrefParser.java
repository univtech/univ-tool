package org.univ.tech.tool.docs.href;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.univ.tech.tool.docs.utils.DocsFileUtils;
import org.univ.tech.tool.docs.utils.LineUtils;

public class FileHrefParser {

	public static void main(String[] args) {
		new FileHrefParser().writeHrefs();
	}

	private void writeHrefs() {
		List<String> hrefs = parseHrefs();
		LineUtils.printLines(hrefs);
	}

	private List<String> parseHrefs() {
		try {
			String hrefBegin = "href=\"";
			String hrefEnd = "\"";
			List<String> lines = DocsFileUtils.readLines();

			Set<String> hrefs = new LinkedHashSet<>();
			for (String line : lines) {
				while (line.contains(hrefBegin)) { // 可能存在多个href
					line = line.substring(line.indexOf(hrefBegin) + hrefBegin.length());
					int endIndex = line.indexOf(hrefEnd);
					String href = line.substring(0, endIndex).trim();
					if (StringUtils.isNotEmpty(href)) {
						hrefs.add(href);
					}
					line = line.substring(endIndex);
				}
			}

			List<String> hrefUrls = new ArrayList<>(hrefs);
			Collections.sort(hrefUrls);
			return hrefUrls;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
