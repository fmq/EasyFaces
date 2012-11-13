package ar.com.easytech.faces.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Object> convertToList(Object dataObject) {
		
		List<Object> data = new ArrayList<Object>();
		// For now we support only lists, sets and maps..
		if (dataObject instanceof List ) { 
			return (List<Object>) dataObject;
		} else if (dataObject instanceof Set) {
			Set dataSet = (Set)dataObject;
			Iterator iter =  dataSet.iterator();
			while (iter.hasNext()) {
				data.add(iter.next());
			}
		} else if (dataObject instanceof Map) {
			Map dataMap = (Map)dataObject;
			for (Object key : dataMap.keySet()) {
				data.add(dataMap.get(key));
			}
		} else if (dataObject instanceof Object[])  {
			
			Object[] dataObj = (Object[]) dataObject; 
			for (int i=0; i< dataObj.length ; i++)
				data.add(dataObj[i]);
		}
		
		return data;
		
	}
}
