package org.univ.tech.tool.docs.href;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.univ.tech.tool.docs.utils.DocsFileUtils;

public class HtmlFileHrefParser extends HrefParser {

	public static void main(String[] args) {
		List<String> ignoreTags = Arrays.asList("header");
		new HtmlFileHrefParser().writeHrefs(ignoreTags);
	}

	private void writeHrefs(List<String> ignoreTags) {
		Set<String> hrefs = parseHrefs(ignoreTags);
		List<String> hrefUrls = convertHrefs(hrefs);
		printHrefs(hrefUrls);
	}

	private Set<String> parseHrefs(List<String> ignoreTags) {
		try {
			String filePath = DocsFileUtils.getDocsFilePath();
			Document htmlDocument = Jsoup.parse(new File(filePath), "UTF-8");
			return parseHrefs(htmlDocument, ignoreTags);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

}
