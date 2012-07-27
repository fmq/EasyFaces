package ar.com.easytech.utils;

import javax.faces.context.FacesContext;

public class AjaxUtils {

	public static boolean isAjaxRequest() {
		return FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
	}
	
	public static String onCompleteStart() {
		return "jsf.ajax.addOnEvent( function(data) { EasyFaces.oncomplete(data, ";
	}
	
	public static String onCompleteEnd() {
		return  "); });";
	}
	
}
