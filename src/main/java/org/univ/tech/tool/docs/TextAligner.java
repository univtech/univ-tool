package org.univ.tech.tool.docs;

import java.text.MessageFormat;
import java.util.List;

import org.univ.tech.tool.docs.utils.DocsFileUtils;

public class TextAligner {

	public static void main(String[] args) {
		new TextAligner().alignTexts();
	}

	private void alignTexts() {
		List<String> lines = DocsFileUtils.readLines();
		int maxLength = getMaxLength(lines);
		formatTexts(lines, maxLength);
	}

	private int getMaxLength(List<String> lines) {
		int maxLength = 0;
		for (String line : lines) {
			maxLength = Math.max(maxLength, line.length());
		}
		return maxLength;
	}

	private void formatTexts(List<String> lines, int maxLength) {
		String format = MessageFormat.format("%-{0}s", String.valueOf(maxLength));
		for (String line : lines) {
			System.out.println(String.format(format, line));
		}
	}

}
