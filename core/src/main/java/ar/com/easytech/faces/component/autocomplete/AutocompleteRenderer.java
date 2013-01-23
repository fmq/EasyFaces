package ar.com.easytech.faces.component.autocomplete;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.event.AutocompleteSearchEvent;
import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.ComponentUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Input", rendererType = "ar.com.easyfaces.AutocompleteRenderer")
public class AutocompleteRenderer extends BaseRenderer {

	private static final Logger logger = Logger
			.getLogger(AutocompleteRenderer.class.getSimpleName());

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
			AutocompleteSearchEvent autoCompleteEvent = new AutocompleteSearchEvent(autocomplete, searchStr, x, y, width);
			autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
			autocomplete.queueEvent(autoCompleteEvent);
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {

		Autocomplete autocomplete = (Autocomplete) component;
		Object value = autocomplete.getValue();
		if (autocomplete.getSourceData().size() == 0) {
			encodeMarkup(context, autocomplete);
			encodeClientBehaviors(context, component);
			// encodeScript(context, autocomplete);
		} else {
			encodeResultAsList(context, autocomplete);
		}
	}

	private void encodeResultAsList(FacesContext context, Autocomplete autocomplete) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		// Data is set we need to re-render
		logger.info("Size: " + autocomplete.getSourceData().size());
		// writer.write(new Gson().toJson(autocomplete.getSourceData()));
		writer.startElement("ul", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId(), null);
		String style = "z-index: 1; display: block; top: "
				+ autocomplete.getY() + "px; left: " + autocomplete.getX()
				+ "px; width: " + autocomplete.getWidth() + "px;";
		writer.writeAttribute("style", style, "style");
		writer.writeAttribute("class", "autocomplete-list", "class");
		for (Object obj : autocomplete.getSourceData()) {
			writer.startElement("li", autocomplete);
			writer.writeAttribute("class", "autocomplete-list-item", "class");
			writer.startElement("a", autocomplete);
			if (autocomplete.getLabel() != null) {
				try {
					Field field = obj.getClass().getDeclaredField(autocomplete.getLabel());
					field.setAccessible(true);
					writer.write((field.get(obj)).toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				writer.write(obj.toString());
			
			if (autocomplete.getItemValue() != null) {
				try {
					Field field = obj.getClass().getDeclaredField(autocomplete.getItemValue());
					field.setAccessible(true);
					writer.writeAttribute("data-value", (field.get(obj)).toString(), null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				writer.writeAttribute("data-value", obj.toString(), null);
			writer.endElement("a");
			writer.endElement("li");
		}
		writer.endElement("ul");
	}

	private void encodeMarkup(FacesContext context, Autocomplete autocomplete)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		// Outer Div
		writer.startElement("div", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_wrapper",null);
		writer.writeAttribute("class", "ui-widget autocmplete", null);
		// Input
		writer.startElement("input", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_input", null);
		writer.writeAttribute("name", autocomplete.getClientId() + "_input", null);
		//writer.writeAttribute("value", autocomplete.getValue() , null);
		writer.writeAttribute("autocomplete", "off", null);
		writer.writeAttribute("type", "text", null);
		String sClass = "ui-autocomplete-input ";
		if (autocomplete.getStyleClass() != null)
			sClass += autocomplete.getStyleClass();
		writer.writeAttribute("class", sClass, null);
		// Render JS actions
		writer.writeAttribute( "onkeyup", "EasyFaces.autocomplete.updateItems(this, event,'"
						+ autocomplete.getClientId() + "',"
						+ autocomplete.getDelay() + " );", null);
		writer.writeAttribute( "onfocus", "EasyFaces.autocomplete.inputOnFocus('"+ autocomplete.getClientId() +"');", null);
		writer.writeAttribute( "onblur", "EasyFaces.autocomplete.inputLostFocus('"+ autocomplete.getClientId() +"');", null);
		// Render common JS actions
		ComponentUtils.encodeJsActions(autocomplete, writer, autocomplete.JS_ACTIONS);
		// End element
		writer.endElement("input");
		// Add hidden input to store the value
		writer.startElement("input", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId() + "_input_hidden", null);
		writer.writeAttribute("name", autocomplete.getClientId() + "_input_hidden", null);
		writer.writeAttribute("value", autocomplete.getValue() , null);
		writer.writeAttribute("autocomplete", "off", null);
		writer.writeAttribute("type", "hidden", null);
		writer.endElement("input");
		//Div for partial results
		writer.startElement("div", autocomplete);
		writer.writeAttribute("id", autocomplete.getClientId(), null);
		writer.endElement("div");
		// close outer div
		writer.endElement("div");
	}

}
