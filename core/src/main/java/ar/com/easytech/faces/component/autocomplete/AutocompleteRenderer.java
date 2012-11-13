package ar.com.easytech.faces.component.autocomplete;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.event.AutocompleteSearchEvent;
import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.AjaxUtils;
import ar.com.easytech.faces.utils.ScriptUtils;
import ar.com.easytech.faces.utils.StringUtils;

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
		String searchStr = params.get(autocomplete.getClientId());
		if (source != null && source.equals(autocomplete.getClientId()) && searchStr != null) {
			// We have enque the event to get the data
			AutocompleteSearchEvent autoCompleteEvent = new AutocompleteSearchEvent(autocomplete, searchStr);
			autoCompleteEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
			autocomplete.queueEvent(autoCompleteEvent);
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {

		String clientId = generateId(context, component, "autocomplete");
		ResponseWriter writer = context.getResponseWriter();
		Autocomplete autocomplete = (Autocomplete) component;

		if (autocomplete.getSourceData().size() == 0) {

			writer.startElement("input", component);
			writer.writeAttribute("id", clientId, null);
			String sClass = "autocomplete ";
			if (autocomplete.getStyleClass() != null)
				sClass += autocomplete.getStyleClass();
			writer.writeAttribute("class", sClass, null);
			writer.endElement("input");
			// Javascript
			ScriptUtils.startScript(writer, autocomplete.getClientId());
			writer.write("var tableNames = ['XXA_TAB1','XXA_TAB2','XXA_TAB3'];");
			writer.write("$(function() {");
			AjaxUtils.newLine(writer);
			writer.write("		$(EasyFaces.escapeId('"
					+ autocomplete.getClientId() + "')).autocomplete({");
			AjaxUtils.newLine(writer);
			writer.write("			source: tableNames,");
			AjaxUtils.newLine(writer);
			if (autocomplete.getMinLength() != null) {
				writer.write("			minLength: " + autocomplete.getMinLength()
						+ ",");
				AjaxUtils.newLine(writer);
			}
			if (autocomplete.getDelay() != null) {
				AjaxUtils.newLine(writer);
				writer.write("			delay: " + autocomplete.getDelay() + ",");
			}
			writer.write("			search: function (event, ui) { ");
			AjaxUtils.newLine(writer);
			writer.write(new AjaxRequest()
					.addEvent(StringUtils.addSingleQuotes("valueChange"))
					.addSource(
							StringUtils.addSingleQuotes(autocomplete
									.getClientId()))
					// We send the whole form to monitor changes
					.addExecute(
							StringUtils.addSingleQuotes(autocomplete
									.getClientId()))
					// We do not re-render anything since
					.addRender(
							StringUtils.addSingleQuotes(autocomplete
									.getClientId())).getAjaxCall());
			writer.write(" 			} ");
			AjaxUtils.newLine(writer);
			writer.write("		});");
			AjaxUtils.newLine(writer);
			writer.write("});");
			AjaxUtils.newLine(writer);
			ScriptUtils.endScript(writer);
		} else {
			// Data is set we need to re-render
			logger.info("Size: " + autocomplete.getSourceData().size());
			writer.startElement("ul", component);
			writer.writeAttribute("class", "ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all", null);
			writer.writeAttribute("tabindex", 0, null);
			writer.writeAttribute("style", "z-index: 1; display: none; width: 135px; top: 29px; left: 48px;", "style");
			for (Object obj : autocomplete.getSourceData()) {
				writer.startElement("li", component);
				writer.writeAttribute("class", "ui-menu-item", "class");
				writer.writeAttribute("role", "presentation", "role");
				writer.write(obj.toString());
				writer.endElement("li");
			}
			writer.endElement("ul");
		}
		

	}

}
