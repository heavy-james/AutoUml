package com.heavy.autouml.util;

public class TextUtil {

	public static final String STD_INDENT = "    ";

	public static boolean isEmpty(String content) {
		return content == null || content.equals("");
	}

	public static boolean equals(String str1, String str2) {
		return null != str1 && str1.equals(str2);
	}

	public static String endLine(String content) {
		return content + "\n";
	}

	public static String newLine(String content) {
		return "\n" + content;
	}
}
