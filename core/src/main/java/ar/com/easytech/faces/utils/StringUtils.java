package ar.com.easytech.faces.utils;

import java.util.List;

/**
 * @author FMQ
 *
 */
public class StringUtils {

	public static boolean isEmpty(String value) {
		return (value == null || value.equals(""));
	}
	
	public static String listAsString(List data, String delimiter) {
		
		StringBuilder string = new StringBuilder();
		for (Object obj : data) {
			if (string.length() > 0)
				string.append(delimiter);
			string.append(obj.toString());
		}
		
		return string.toString().replace(delimiter+"$", "");
		
	}
	
	public static String addSingleQuotes(String value) {
		return "'" + value + "'";
	}
}
