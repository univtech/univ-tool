package org.univ.tech.tool.docs.href;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.univ.tech.tool.docs.utils.DocsFileUtils;

public class FileHrefParser {

	public static void main(String[] args) {
		new FileHrefParser().writeHrefs();
	}

	private void writeHrefs() {
		Set<String> hrefs = parseHrefs();
		printHrefs(hrefs);
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
					hrefs.add(line.substring(0, line.indexOf(hrefEnd)));
					line = line.substring(line.indexOf(hrefEnd));
				}
			}
			return hrefs;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

	private void printHrefs(Set<String> hrefs) {
		for (String href : hrefs) {
			System.out.println(href);
		}
	}

}
