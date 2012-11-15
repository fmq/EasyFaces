package ar.com.easytech.faces.dashboard.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ar.com.easytech.faces.dashboard.utils.ChartTypes;

/**
 * 
 * @author FMQ
 */
public class ChartManager implements Serializable {

	private static final long serialVersionUID = -3143333628078155040L;

	public void addSeries(ChartTypes type) {

	}

	public static String serialize(Object object) {

		String result = "";
		if (object == null)
			return "";
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
		} else if (object instanceof Map) {
			Map<Object, Object> data = (HashMap<Object, Object>) object;
			StringBuilder str = new StringBuilder();
			str.append("[");

			for (Object key : data.keySet()) {
				str.append("[")
					.append(key)
					.append(",")
					.append(data.get(key))
					.append("],");
			}
			
			result = str.toString().substring(0,str.toString().length() - 1);
			result += "]";

		}
		return result;
	}

	public static String serializeForPie(Map<Object, Object> data) {
		if (data == null)
			return "[{}]";

		StringBuilder result = new StringBuilder();
		result.append("[");

		for (Object key : data.keySet()) {
			result.append("{ label: '").append(key).append("', data: ")
					.append(data.get(key)).append("},");
		}
		String res = result.toString().substring(0,
				result.toString().length() - 1);
		Logger.getLogger("serialize").info(res);
		return res + "]";

	}
}