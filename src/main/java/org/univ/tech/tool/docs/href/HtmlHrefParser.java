package org.univ.tech.tool.docs.href;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlHrefParser {

	public static void main(String[] args) {
		String url = "";
		new HtmlHrefParser().writeHrefs(url);
	}

	private void writeHrefs(String url) {
		Set<String> hrefs = parseHrefs(url);
		printHrefs(hrefs);
	}

	private Set<String> parseHrefs(String url) {
		try {
			Document htmlDocument = Jsoup.connect(url).get();
			Elements anchorElements = htmlDocument.getElementsByTag("a");

			Set<String> hrefs = new LinkedHashSet<>();
			for (Element anchorElement : anchorElements) {
				hrefs.add(anchorElement.attr("href"));
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
