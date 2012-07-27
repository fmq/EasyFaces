package ar.com.easytech.utils;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

public class ScriptUtils {

	public static void startScript(ResponseWriter writer, String clientId) throws IOException {
		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_js", null);
		writer.writeAttribute("type", "text/javascript", null);	
	}
	
	public static void endScript(ResponseWriter writer) throws IOException {
		writer.endElement("script");
	}
	
}
