package ar.com.easytech.utils;

import java.util.HashMap;
import java.util.Map;

public class AjaxRequest {
	
	private StringBuilder dCall;
	private Map<String, String> params;
	
	public AjaxRequest() {
		
		params = new HashMap<String, String>();
	}
	
	public AjaxRequest addExecute(String value) {
		if (value != null)
			params.put("execute",value);
		return this;
	}
	
	public AjaxRequest addRender(String value) {
		if (value != null)
			params.put("render",value);
		return this;
	}
	
	public AjaxRequest addEvent(String value) {
		if (value != null)
			params.put("event",value);
		return this;
	}
	
	public AjaxRequest addSource(String value) {
		if (value != null)
			params.put("source",value);
		return this;
	}
	
	public AjaxRequest addOther(String key, String value) {
		if (value != null)
			params.put(key,value);
		return this;
	}
	
	public String getAjaxCall() {
		dCall = new StringBuilder();
		dCall.append("EasyFaces.ajax(this, event, {");
		boolean first = true;
		for (String key : params.keySet()) {
			if (!first) {
				dCall.append(",");
			}
			first = false;
			dCall.append(key)
			     .append(":").append(params.get(key));
		}
		dCall.append("});");
		
		return dCall.toString();
	}
}
