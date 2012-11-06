package ar.com.easytech.faces.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
*
* @author FMQ
*/
public class ChartManager implements Serializable {

	private static final long serialVersionUID = -3143333628078155040L;
	
	private List<String> series = new ArrayList<String>();
	
	public void addSeries(ChartTypes type) {
		
	}
	
	public static String serialize(Object object) {
	    String result = "";
	    if (object.getClass().isArray()) {
	      Object[][] data = (Object[][]) object;
	      for (int i = 0; i < data[0].length; i++) {
	        if (!"".equals(result))
	          result = result.concat(",");
	        result = result.concat("[" + data[0][i] + "," + data[1][i]
	            + "]");
	      }
	      result = "[" + result.concat("]");
	      return result;
	    } else if (object instanceof String) {
	      return object.toString();
	    }
	    return result;
	  }

	public static String serializeForPie(Map<String, Object> data) {

		if (data == null)
			return "[{}]";

		StringBuilder result = new StringBuilder();
		result.append("[");

		for (String key: data.keySet()) {
			result.append("{ label: '")
					.append(key)
					.append("', data: ")
					.append(data.get(key))
					.append("},");
		}
		String res = result.toString().substring(0, result.toString().length() -1);
		return res + "]";

	}
}