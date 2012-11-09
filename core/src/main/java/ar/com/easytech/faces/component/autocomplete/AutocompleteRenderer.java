package ar.com.easytech.faces.component.autocomplete;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.AjaxUtils;
import ar.com.easytech.faces.utils.ScriptUtils;
import ar.com.easytech.faces.utils.StringUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Input", rendererType = "ar.com.easyfaces.AutocompleteRenderer")
public class AutocompleteRenderer extends BaseRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null) {
			throw new NullPointerException();
		}
		// Decode behaviors for std ajax tag
		decodeClientBehaviors(context, component);
		// Process update behaviour 
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		Autocomplete autocomplete = (Autocomplete) component;
		// Get the source of the call to validate it's a valid call
		String source = params.get("javax.faces.source");
		String data = params.get(autocomplete.getClientId());
		if (source != null && source.equals(autocomplete.getClientId()) && data != null) {
			// We have to set the data to the component
			autocomplete.setDataSource(data);
			
		}
		
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		String clientId = generateId(context, component, "autocomplete");

		Autocomplete autocomplete = (Autocomplete) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("input", component);
		writer.writeAttribute("id", clientId,null);
		String sClass = "autocomplete ";
		if (autocomplete.getStyleClass() != null)
			sClass += autocomplete.getStyleClass();
		writer.writeAttribute("class", sClass,  null);
		writer.endElement("input");
		
		writer.startElement("input", component);
		writer.writeAttribute("id", autocomplete.getClientId() + "_data",null);
		writer.writeAttribute("value", "['XXA_TAB1','XXA_TAB2','XXA_TAB3']", null);
		writer.endElement("input");
		
		// Javascript 
		ScriptUtils.startScript(writer, autocomplete.getClientId());
		writer.write("var tableNames = ['XXA_TAB1','XXA_TAB2','XXA_TAB3'];");
    	writer.write("$(function() {");
    	AjaxUtils.newLine(writer);
    	writer.write("		$(EasyFaces.escapeId('" + autocomplete.getClientId() + "')).autocomplete({");
    	AjaxUtils.newLine(writer);
    	writer.write("			source: function( request, response ) { var data = jQuery.parseJSON(document.getElementById('" +  autocomplete.getClientId() + "_data').value); response(data) },");
    	AjaxUtils.newLine(writer);
    	if (autocomplete.getMinLength() != null) {
	    	writer.write("			minLength: " + autocomplete.getMinLength() + ",");
	    	AjaxUtils.newLine(writer);
    	}
    	writer.write("			minLength: " + autocomplete.getMinLength() + ",");
    	if (autocomplete.getDelay() != null) {
	    	AjaxUtils.newLine(writer);
	    	writer.write("			delay: " + autocomplete.getDelay() + ",");
    	}
    	AjaxUtils.newLine(writer);
    	writer.write("			search: function (event, ui) { ");
    	AjaxUtils.newLine(writer);
    	writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("valueChange"))
    			.addSource(StringUtils.addSingleQuotes(autocomplete.getClientId())) 
    			// We send the whole form to monitor changes
    			.addExecute(StringUtils.addSingleQuotes(autocomplete.getClientId()))
				// We do not re-render anything since 
				.addRender(StringUtils.addSingleQuotes("@none"))
				.getAjaxCall());
    	writer.write(" 			} ");
    	AjaxUtils.newLine(writer);
    	writer.write("		});");
    	AjaxUtils.newLine(writer);
    	writer.write("});");
    	AjaxUtils.newLine(writer);
    	ScriptUtils.endScript(writer);
		
	}

}

