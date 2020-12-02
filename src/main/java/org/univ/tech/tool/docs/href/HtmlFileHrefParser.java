package org.univ.tech.tool.docs.href;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.univ.tech.tool.docs.utils.DocsFileUtils;

public class HtmlFileHrefParser {

	public static void main(String[] args) {
		new HtmlFileHrefParser().writeHrefs();
	}

	private void writeHrefs() {
		List<String> hrefs = parseHrefs();
		printHrefs(hrefs);
	}

	private List<String> parseHrefs() {
		try {
			String filePath = DocsFileUtils.getDocsFilePath();
			Document htmlDocument = Jsoup.parse(new File(filePath), "UTF-8");
			Elements anchorElements = htmlDocument.getElementsByTag("a");

			Set<String> hrefs = new LinkedHashSet<>();
			for (Element anchorElement : anchorElements) {
				hrefs.add(anchorElement.attr("href"));
			}

			List<String> hrefUrls = new ArrayList<>(hrefs);
			Collections.sort(hrefUrls);
			return hrefUrls;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	private void printHrefs(List<String> hrefs) {
		for (String href : hrefs) {
			System.out.println(href);
		}
	}

}
