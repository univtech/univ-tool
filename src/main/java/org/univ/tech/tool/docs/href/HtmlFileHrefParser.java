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
		// 从body中解析href，忽略header标签内容
		String tag = "body";
		List<String> ignoreTags = Arrays.asList("header");
		new HtmlFileHrefParser().writeHrefs(tag, ignoreTags);
	}

	private void writeHrefs(String tag, List<String> ignoreTags) {
		Set<String> hrefs = parseHrefs(tag, ignoreTags);
		List<String> hrefUrls = convertHrefs(hrefs);
		printHrefs(hrefUrls);
	}

	private Set<String> parseHrefs(String tag, List<String> ignoreTags) {
		try {
			String filePath = DocsFileUtils.getDocsFilePath();
			Document htmlDocument = Jsoup.parse(new File(filePath), "UTF-8");
			return parseHrefs(htmlDocument, tag, ignoreTags);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

}
