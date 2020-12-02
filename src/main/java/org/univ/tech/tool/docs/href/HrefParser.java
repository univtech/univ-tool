package org.univ.tech.tool.docs.href;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HrefParser {

	protected Set<String> parseHrefs(Document htmlDocument, List<String> ignoreTags) {
		Element bodyElement = htmlDocument.getElementsByTag("body").get(0);
		if (CollectionUtils.isNotEmpty(ignoreTags)) {
			for (String ignoreTag : ignoreTags) {
				bodyElement.getElementsByTag(ignoreTag).remove();
			}
		}
		Elements anchorElements = bodyElement.getElementsByTag("a");

		Set<String> hrefs = new LinkedHashSet<>();
		for (Element anchorElement : anchorElements) {
			String href = anchorElement.attr("href").trim();
			if (StringUtils.isNotEmpty(href)) {
				hrefs.add(href);
			}
		}
		return hrefs;
	}

	protected List<String> convertHrefs(Set<String> hrefs) {
		List<String> hrefUrls = new ArrayList<>(hrefs);
		Collections.sort(hrefUrls);
		return hrefUrls;
	}

	protected void printHrefs(List<String> hrefs) {
		for (String href : hrefs) {
			System.out.println(href);
		}
	}

}
