package com.spaceemotion.payforaccess.util;

public class MessageUtil {
	public static String parseColors(String msg) {
		if (msg == null) return null;

		return msg.replaceAll("&([0-9a-f])", "\u00A7$1");
	}

	public static String parseMessage(String msg, String... args) {
		return parseMessage(msg, true, args);
	}

	public static String parseMessage(String msg, boolean parseColors, String... args) {
		String s = LanguageUtil.getString(msg);
		for (int i = 0; i < args.length; i++)
			s = s.replaceAll("%" + i, args[i]);

		return parseColors(s);
	}

}
