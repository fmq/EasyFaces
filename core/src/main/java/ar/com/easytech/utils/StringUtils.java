package ar.com.easytech.utils;

import java.util.List;

/**
 * @author FMQ
 *
 */
public class StringUtils {

	public static boolean isEmpty(String value) {
		return (value == null || value.equals(""));
	}
	
	public static String listAsString(List<String> invalidIds, String delimiter) {
		
		StringBuilder string = new StringBuilder();
		for (String obj : invalidIds) {
			if (string.length() > 0)
				string.append(delimiter);
			string.append(obj);
		}
		
		return string.toString().replace(delimiter+"$", "");
		
	}
	
	public static String addSingleQuotes(String value) {
		return "'" + value + "'";
	}
}
