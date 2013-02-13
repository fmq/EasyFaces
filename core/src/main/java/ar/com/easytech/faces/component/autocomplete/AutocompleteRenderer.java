package ar.com.easytech.faces.component.autocomplete;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.event.PhaseId;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.event.AutocompleteSearchEvent;
import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.Chain;
import ar.com.easytech.faces.utils.ComponentUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Input", rendererType = "ar.com.easyfaces.AutocompleteRenderer")
public class AutocompleteRenderer extends BaseRenderer {

	private static final Logger logger = Logger.getLogger(AutocompleteRenderer.class.getSimpleName());

	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null) {
			throw new NullPointerException();
		}
		// Decode behaviors for std ajax tag
		decodeClientBehaviors(context, component);
		// Process update behaviour
		Map<String, String> params = context.getExternalContext()
				.getRequestParameterMap();
		Autocomplete autocomplete = (Autocomplete) component;
		// Get the source of the call to validate it's a valid call
		String source = params.get("javax.faces.source");
		String searchStr = params.get(autocomplete.getClientId() + "_input");

		autocomplete.setSubmittedValue(params.get(autocomplete.getClientId() + "_input_hidden"));

		if (source != null && source.equals(autocomplete.getClientId())
				&& searchStr != null) {
			int x = 0;
			int y = 0;
			int width = 0;

			if (params.get("x") != null)
				x = new Integer(params.get("x"));
			if (params.get("y") != null)
				y = new Integer(params.get("y"));
			if (params.get("width") != null)
				width = new Integer(params.get("width"));
			// We have to enque the event to get the data
			AutocompleteSearchEvent autoCompleteEvent = new AutocompleteSearchEvent(
					autocomplete, searchStr, x, y, width);
			autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
			autocomplete.queueEvent(autoCompleteEvent);
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {

		Autocomplete autocomplete = (Autocomplete) component;
		Map<String, String> params = context.getExternalContext()
				.getRequestParameterMap();
		String searchStr = params.get(autocomplete.getClientId() + "_input");
		logger.info("SearchStr: " + searchStr);
		if (searchStr == null || searchStr.equals("")) {
			encodeMarkup(context, autocomplete);
			
			//encodeClientBehaviors(context, component);
		} else {
			encodeResultAsList(context, autocomplete);
		}
	}

	private void encodeResultAsList(FacesContext context,
			Autocomplete autocomplete) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		// Data is set we need to re-render
		writer.startElement("ul", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId(), null);
		String style = "z-index: 1; display: block; top: "
				+ autocomplete.getY() + "px; left: " + autocomplete.getX()
				+ "px; width: " + autocomplete.getWidth() + "px;";
		writer.writeAttribute("style", style, "style");
		writer.writeAttribute("class", "autocomplete-list", "class");
		Map<String, Object> requestMap = context.getExternalContext()
				.getRequestMap();

		if (autocomplete.getSourceData().isEmpty()) {
			writer.startElement("li", autocomplete);
			writer.writeAttribute("data-value", "-1", null);
			writer.startElement("a", autocomplete);
			writer.write((autocomplete.getNoDataLabel() != null) ? autocomplete
					.getNoDataLabel() : "No Data");
			writer.endElement("a");
			writer.endElement("li");
		} else {
			for (Object obj : autocomplete.getSourceData()) {
				writer.startElement("li", autocomplete);
				writer.writeAttribute("class", "autocomplete-list-item",
						"class");

				if (autocomplete.getVar() != null) {
					requestMap.put(autocomplete.getVar(), obj);

					if (autocomplete.getItemValue() != null) {
						String value = autocomplete.getItemValue();
						writer.writeAttribute("data-value", value, null);
					}
				} else {
					writer.writeAttribute("data-value", obj.toString(), null);
				}

				writer.startElement("a", autocomplete);
				if (autocomplete.getVar() != null) {

					if (autocomplete.getLabel() != null) {
						String label = autocomplete.getLabel();
						writer.write(label);
					}
				} else {
					writer.write(obj.toString());
				}
				writer.endElement("a");
				writer.endElement("li");
			}
		}
		writer.endElement("ul");
	}

	private void encodeMarkup(FacesContext context, Autocomplete autocomplete)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		// Outer Div
		writer.startElement("div", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_wrapper",
				null);
		writer.writeAttribute("class", "ui-widget autocmplete", null);
		// Input
		writer.startElement("input", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_input", null);
		writer.writeAttribute("name", autocomplete.getClientId() + "_input",null);
		if (autocomplete.getConverter() != null) {
			Converter converter = autocomplete.getConverter();
			writer.writeAttribute("value", converter.getAsString(context, autocomplete, autocomplete.getValue()), null);
		} else
			writer.writeAttribute("value", autocomplete.getValue(), null);
		writer.writeAttribute("autocomplete", "off", null);
		writer.writeAttribute("type", "text", null);
		String sClass = "ui-autocomplete-input ";
		if (autocomplete.getStyleClass() != null)
			sClass += autocomplete.getStyleClass();
		writer.writeAttribute("class", sClass, null);
		// Render JS actions
		encodeJsActions(context, autocomplete, writer);
		
		// End element
		writer.endElement("input");
		// Add hidden input to store the value
		writer.startElement("input", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_input_hidden", null);
		writer.writeAttribute("name", autocomplete.getClientId() + "_input_hidden", null);
		writer.writeAttribute("value", autocomplete.getValue(), null);
		writer.writeAttribute("autocomplete", "off", null);
		writer.writeAttribute("type", "hidden", null);
		writer.endElement("input");
		// Div for partial results
		writer.startElement("div", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId(), null);
		writer.endElement("div");
		// close outer div
		writer.endElement("div");
	}
	
	private void encodeJsActions(FacesContext context, Autocomplete autocomplete, ResponseWriter writer) throws IOException {
		Map<String, List<ClientBehavior>> clientBehaviors = autocomplete.getClientBehaviors();
		
		String script = ComponentUtils.getScript(context, autocomplete, clientBehaviors, "keyup");
		String att = (String) autocomplete.getAttributes().get("onkeyup");
	    if (script != null) {
			Chain chain = new Chain();
			chain.addScript("EasyFaces.autocomplete.updateItems(this, event,'"
						+ autocomplete.getClientId() + "',"
						+ autocomplete.getDelay() + " );");
			if (att != null)
				chain.addScript(att);
	        chain.addScript(script);
	        writer.writeAttribute( "onkeyup", chain.toString(), null);
		} else  if (att != null) {
				Chain chain = new Chain();
				chain.addScript("EasyFaces.autocomplete.updateItems(this, event,'"
						+ autocomplete.getClientId() + "',"
						+ autocomplete.getDelay() + " );");
				chain.addScript(att);
				writer.writeAttribute( "onkeyup", chain.toString(), null);
		} else {
			writer.writeAttribute(
					"onkeyup",
					"EasyFaces.autocomplete.updateItems(this, event,'"
							+ autocomplete.getClientId() + "',"
							+ autocomplete.getDelay() + " );", null);
		}
		    
	    // Focus
	    script = ComponentUtils.getScript(context, autocomplete, clientBehaviors, "focus");
	    att = (String) autocomplete.getAttributes().get("onfocus");
	    if (script != null) {
			Chain chain = new Chain();
	        chain.addScript("EasyFaces.autocomplete.inputOnFocus('"+ autocomplete.getClientId() + "');");
	        if (att != null) 
	        	chain.addScript(att);
	        
	        chain.addScript(script);
	        writer.writeAttribute( "onfocus", chain.toString(), null);
		} else if (att != null)  {
			Chain chain = new Chain();
	        chain.addScript("EasyFaces.autocomplete.inputOnFocus('"+ autocomplete.getClientId() + "');");
	        if (att != null) 
	        	chain.addScript(att);
	        writer.writeAttribute( "onfocus", chain.toString(), null);
		} else
			writer.writeAttribute(
				"onfocus",
				"EasyFaces.autocomplete.inputOnFocus('"
						+ autocomplete.getClientId() + "');", null);
	    
	    // Blur
	    script = ComponentUtils.getScript(context, autocomplete, clientBehaviors, "blur");
	    att = (String) autocomplete.getAttributes().get("onblur");
	    if (script != null) {
			Chain chain = new Chain();
	        chain.addScript("EasyFaces.autocomplete.inputLostFocus('" + autocomplete.getClientId() + "');");
	        chain.addScript(script);
	        writer.writeAttribute( "onfocus", chain.toString(), null);
		} else if (att != null)  {
			Chain chain = new Chain();
			chain.addScript("EasyFaces.autocomplete.inputLostFocus('" + autocomplete.getClientId() + "');");
	        if (att != null) 
	        	chain.addScript(att);
	        writer.writeAttribute( "onblur", chain.toString(), null);
		} else
			writer.writeAttribute(
				"onblur",
				"EasyFaces.autocomplete.inputLostFocus('" + autocomplete.getClientId() + "');", null);
		
		// Render common JS actions
		ComponentUtils.encodeJsActions(context, autocomplete, writer, autocomplete.JS_ACTIONS, autocomplete.getClientBehaviors());
	}
	

	@Override
	public boolean getRendersChildren() {
		return true;
    }
	

}
