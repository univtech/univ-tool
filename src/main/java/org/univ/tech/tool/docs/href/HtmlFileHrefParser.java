package org.univ.tech.tool.docs.href;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.univ.tech.tool.docs.utils.DocsFileUtils;
import org.univ.tech.tool.docs.utils.LineUtils;

public class HtmlFileHrefParser {

	public static void main(String[] args) {
		// 从body中解析href，忽略header标签内容
		String tag = "body";
		List<String> ignoreTags = Arrays.asList("header");
		new HtmlFileHrefParser().writeHrefs(tag, ignoreTags);
	}

	private void writeHrefs(String tag, List<String> ignoreTags) {
		List<String> hrefs = parseHrefs(tag, ignoreTags);
		LineUtils.printLines(hrefs);
	}

	private List<String> parseHrefs(String tag, List<String> ignoreTags) {
		try {
			String filePath = DocsFileUtils.getDocsFilePath();
			Document htmlDocument = Jsoup.parse(new File(filePath), "UTF-8");
			return new HtmlHrefParser().parseHrefs(htmlDocument, tag, ignoreTags);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

}
