package org.univ.tech.tool.docs.href;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.univ.tech.tool.docs.utils.DocsFileUtils;

public class FileHrefParser extends HrefParser {

	public static void main(String[] args) {
		new FileHrefParser().writeHrefs();
	}

	private void writeHrefs() {
		Set<String> hrefs = parseHrefs();
		List<String> hrefUrls = convertHrefs(hrefs);
		printHrefs(hrefUrls);
	}

	private Set<String> parseHrefs() {
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
			return hrefs;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

}
