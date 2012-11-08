package ar.com.easytech.faces.input.autocomplete.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.DataModel;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.dashboard.component.Dashboard;
import ar.com.easytech.faces.dashboard.component.DashboardColumn;
import ar.com.easytech.faces.dashboard.component.widgets.Chart;
import ar.com.easytech.faces.dashboard.component.widgets.DataTable;
import ar.com.easytech.faces.dashboard.component.widgets.PieChart;
import ar.com.easytech.faces.dashboard.model.DashboardColumnLayout;
import ar.com.easytech.faces.dashboard.model.DashboardDefinition;
import ar.com.easytech.faces.dashboard.model.Widget;
import ar.com.easytech.faces.dashboard.model.WidgetInstance;
import ar.com.easytech.faces.dashboard.model.WidgetType;
import ar.com.easytech.faces.dashboard.service.DashboardBuilderBean;
import ar.com.easytech.faces.dashboard.utils.Producers;
import ar.com.easytech.faces.input.autocomplete.component.InputAutocomplete;
import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.AjaxUtils;
import ar.com.easytech.faces.utils.ScriptUtils;
import ar.com.easytech.faces.utils.StringUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Dashboard", rendererType = "ar.com.easyfaces.DashboardRenderer")
public class InputAutocompleteRenderer extends BaseRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null) {
			throw new NullPointerException();
		}
		// Decode behaviors for std ajax tag
		decodeClientBehaviors(context, component);
		// Process update behaviour 
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		Dashboard dashboard = (Dashboard) component;
		// Get the source of the call to validate it's a valid call
		String source = params.get("javax.faces.source");
		String data = params.get("data");
		if (source != null && source.equals(dashboard.getClientId()) && data != null) {
			// We have to update the database with new positions.
			// First we parse the columns
			DashboardBuilderBean builder = Producers.getDashboardBuilder();
			builder.updateModel(dashboard.getInstanceId(), data);
				
		}
		
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		String clientId = generateId(context, component, "autocomplete");

		InputAutocomplete autocomplete = (InputAutocomplete) component;
		ResponseWriter writer = context.getResponseWriter();
		
		// Start elements
		// Script with autocomplete source
		writer.startElement("script", component);
		writer.writeAttribute("id", clientId.concat("script"),null);
		//TODO
		/*<script>
		var tableNames = [""<ui:repeat var="tableName" value="#{createReportBacking.tableNameList}">, "#{tableName}"</ui:repeat>];
		jQuery(".rpt-table-name").autocomplete('option','source',tableNames);
		</script>*/
		writer.endElement("script");
		
		writer.startElement("input", component);
		writer.writeAttribute("id", clientId,null);
		String sClass = "autocomplete ";
		
		if (autocomplete.getStyleClass() != null)
			sClass += autocomplete.getStyleClass();
		
		writer.writeAttribute("class", sClass,  null);
		
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	
		InputAutocomplete autocomplete = (InputAutocomplete) component;
	
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("input");
		// Javascript call
		ScriptUtils.startScript(writer, autocomplete.getClientId());
    	writer.write("$(function() {");
    	AjaxUtils.newLine(writer);
    	writer.write("		$(#" + autocomplete.getClientId() + ").autocomplete({");
    	AjaxUtils.newLine(writer);
    	writer.write("			minLength: " + autocomplete.getMinLength() + ",");
    	AjaxUtils.newLine(writer);
    	writer.write("			search: function (event, ui) { ");
    	AjaxUtils.newLine(writer);
    	writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("update"))
    			.addSource(StringUtils.addSingleQuotes(autocomplete.getClientId())) 
    			// We send the whole dashboard to monitor changes
    			.addExecute(StringUtils.addSingleQuotes(autocomplete.getClientId()))
				// We do not re-render anything since 
				.addRender(StringUtils.addSingleQuotes("@none"))
				.addOther("data", "Dashboard.getWidgets('" + autocomplete.getClientId() + "')")
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

