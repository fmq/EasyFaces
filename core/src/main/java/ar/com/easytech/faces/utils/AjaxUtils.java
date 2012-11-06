package ar.com.easytech.faces.utils;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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

	public static void newLine(ResponseWriter writer) {
		try {
			writer.write( "\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
