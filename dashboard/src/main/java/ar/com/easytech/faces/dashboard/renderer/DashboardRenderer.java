package ar.com.easytech.faces.dashboard.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
import ar.com.easytech.faces.dashboard.service.DashboardBuilderTemplate;
import ar.com.easytech.faces.dashboard.utils.Producers;
import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.AjaxUtils;
import ar.com.easytech.faces.utils.ScriptUtils;
import ar.com.easytech.faces.utils.StringUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Dashboard", rendererType = "ar.com.easyfaces.DashboardRenderer")
public class DashboardRenderer extends BaseRenderer {

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
		String clientId =  component.getClientId(context);
		// Get the source of the call to validate it's a valid call
		String source = params.get("javax.faces.source");
		String data = params.get("data");
		if (source != null && source.equals(dashboard.getClientId()) && data != null) {
			// We have to update the database with new positions.
			// First we parse the columns
			DashboardBuilderTemplate builder = Producers.getDashboardBuilder();
			builder.updateModel(dashboard.getInstanceId(), data);
				
		}
		
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		String clientId = generateId(context, component, "dashboard");

		Dashboard dashboard = (Dashboard) component;
		ResponseWriter writer = context.getResponseWriter();
		
		// Start elements
		writer.startElement("div", component);
		writer.writeAttribute("id", clientId,null);
		String sClass = "dashboard ";
		
		if (dashboard.getStyleClass() != null)
			sClass += dashboard.getStyleClass();
		
		writer.writeAttribute("class", sClass,  null);
		
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	
		DashboardBuilderTemplate builder = Producers.getDashboardBuilder();
		Dashboard dashboard = (Dashboard) component;
		
		// Get dashboard for user
		DashboardDefinition dashboardDefinition = builder.getDashboardForUser(dashboard.getUserId());
		//dashboard.setColumns(dashboardDefinition.getColumns());
		dashboard.setInstanceId(dashboardDefinition.getId());
		// Add columns
		for (DashboardColumnLayout layout :  dashboardDefinition.getColumnLayout()) {
			DashboardColumn col = new DashboardColumn();
			col.setId("dash_" + layout.getId() + "_col");
			col.setLayoutId(layout.getId());
			col.setParent(dashboard);
			switch (layout.getType()) {
			case HORIZONAL:
				col.setStyleClass("cols-1");
				break;
			case VERTICAL:
				col.setStyleClass("cols-2");
				break;
			}
			// Get widgets for each column
			List<WidgetInstance> widgetsInstances = builder.getWidgetsForColumn(layout);

			// Generate the widgets
			for (WidgetInstance widgetInstance : widgetsInstances) {
				Widget widget = widgetInstance.getWidget();
				switch (widget.getType()) {
				case PIE:
					// Add content
					PieChart pieChart = new PieChart();
					String data = builder.getWidgetData(widget);
					pieChart.setData(data);
					pieChart.setInstanceId(widgetInstance.getInstanceId());
					pieChart.setId("widg_" + widgetInstance.getLayoutId() + "_" + widgetInstance.getInstanceId() +  "_pie");
					pieChart.setTitle(widget.getTitle());
					pieChart.setWidth(widget.getWidth());
					pieChart.setHeight(widget.getHeight());
					if (widget.getStyleClass() != null)
						pieChart.setStyleClass(widget.getStyleClass());
					// Encode
					col.getChildren().add(pieChart);
					break;
				case CHART:
					Chart chart = new Chart();
					String chartData = builder.getWidgetData(widget);
					chart.setData(chartData);
					chart.setInstanceId(widgetInstance.getInstanceId());
					chart.setId("widg_" + widgetInstance.getLayoutId() + "_" + widgetInstance.getInstanceId() +  "_chart");
					chart.setTitle(widget.getTitle());
					chart.setWidth(widget.getWidth());
					chart.setHeight(widget.getHeight());
					if (widget.getStyleClass() != null)
						chart.setStyleClass(widget.getStyleClass());
					// Encode
					col.getChildren().add(chart);
					break;
				case TABLE:
					DataTable table = new DataTable();
					table.setHeaders(new ArrayList<String>(Arrays.asList(widget.getAdditionalData().split(","))));
					table.setData(builder.getWidgetDataAsList(widget));
					table.setInstanceId(widgetInstance.getInstanceId());
					table.setId("widg_" + widgetInstance.getLayoutId() + "_" + widgetInstance.getInstanceId() +  "_table");
					table.setTitle(widget.getTitle());
					table.setWidth(widget.getWidth());
					table.setHeight(widget.getHeight());
					if (widget.getStyleClass() != null)
						table.setStyleClass(widget.getStyleClass());
					// Encode
					col.getChildren().add(table);
					break;
				default:
					break;
				}
			}
			
			col.encodeBegin(context);
			col.encodeChildren(context);
			col.encodeEnd(context);
		}
	
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
		// Javascript call
		ScriptUtils.startScript(writer, dashboard.getClientId());
    	writer.write("$(function() {");
    	AjaxUtils.newLine(writer);
    	writer.write("		$('.column').sortable({");
    	AjaxUtils.newLine(writer);
    	writer.write("			connectWith : '.column',");
    	AjaxUtils.newLine(writer);
    	writer.write("			accept : function(dragabble) { alert(dragabble); },");
    	AjaxUtils.newLine(writer);
    	writer.write("			handle : '.widget-header',");
    	AjaxUtils.newLine(writer);
    	writer.write("			opacity : 0.6,");
    	AjaxUtils.newLine(writer);
    	writer.write("			update: function (event, ui) { ");
    	AjaxUtils.newLine(writer);
    	writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("update"))
    			.addSource(StringUtils.addSingleQuotes(dashboard.getClientId())) 
    			// We send the whole dashboard to monitor changes
    			.addExecute(StringUtils.addSingleQuotes(dashboard.getClientId()))
				// We do not re-render anything since 
				.addRender(StringUtils.addSingleQuotes("@none"))
				.addOther("data", "Dashboard.getWidgets('" + dashboard.getClientId() + "')")
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
