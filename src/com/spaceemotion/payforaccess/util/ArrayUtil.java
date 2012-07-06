package com.spaceemotion.payforaccess.util;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtil {
	public static ArrayList<String> stringToArrayList(String list, String separator) {
		String[] pieces = list.split(separator);

		for (int i = pieces.length - 1; i >= 0; i--)
			pieces[i] = pieces[i].trim();

		return new ArrayList<String>(Arrays.asList(pieces));
	}
}
