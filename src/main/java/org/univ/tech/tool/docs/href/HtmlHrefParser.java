package org.univ.tech.tool.docs.href;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlHrefParser extends HrefParser {

	public static void main(String[] args) {
//		String url = "https://spring.io/";
//		String url = "https://spring.io/guides";
//		String tag = "body"; 

		String url = "https://spring.io/projects/spring-boot";
		String tag = "article";
		List<String> ignoreTags = Arrays.asList("header", "footer");
		new HtmlHrefParser().writeHrefs(url, tag, ignoreTags);
	}

	private void writeHrefs(String url, String tag, List<String> ignoreTags) {
		Set<String> hrefs = parseHrefs(url, tag, ignoreTags);
		List<String> hrefUrls = convertHrefs(hrefs);
		printHrefs(hrefUrls);
	}

	private Set<String> parseHrefs(String url, String tag, List<String> ignoreTags) {
		try {
			Document htmlDocument = Jsoup.connect(url).get();
			return parseHrefs(htmlDocument, tag, ignoreTags);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}

}
