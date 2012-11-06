package ar.com.easytech.faces.dashboard.widgets.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.dashboard.component.widgets.Chart;
import ar.com.easytech.faces.dashboard.component.widgets.PieChart;
import ar.com.easytech.faces.utils.AjaxUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Widget", rendererType = "ar.com.easyfaces.ChartRenderer")
public class ChartRenderer extends BaseWidgetRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {

		Chart chart = (Chart)component;

		ResponseWriter writer = context.getResponseWriter();
		String clientId = generateId(context, component, "pie");
		//Javascript call
	    writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		AjaxUtils.newLine(writer);
		writer.write("$(document).ready(function() {");
		AjaxUtils.newLine(writer);
		String data = (String)chart.getData() ;
		writer.write("		var data = [" + data + "];");
		//AjaxUtils.newLine(writer);
		writer.write("		var options = { lines: { show: true } ,legend: {position: 'ne' , margin: [5,5]} };");
		AjaxUtils.newLine(writer);
		String tmpId = clientId + "_graph";
		writer.write("		jQuery.plot($('' + Dashboard.escapeId('"+ tmpId + "')), data, options);");
		AjaxUtils.newLine(writer);
		writer.write("});");
		AjaxUtils.newLine(writer);
		writer.endElement("script");
		
		// Start elements
		startWidget(context, component, chart.getInstanceId() );
		//Wrapper
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_wrapper", "id");
		
		writer.startElement("div", null);
		
		String style = null;
		
		if (chart.getHeight() != null)
			style = "height: " + chart.getHeight() + "px;";
		
		if (chart.getWidth() != null)
			if (style != null)
				style +="width: " + chart.getWidth() + "px;";
			else
				style = "width: " + chart.getWidth() + "px;"; 
		writer.writeAttribute("style", style, "style");
		//Graph
		writer.writeAttribute("id", clientId + "_graph", "id");
		writer.writeAttribute("class", "graph", "class");
		writer.endElement("div");
        writer.endElement("div");		
		
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		endWidget(context, component);
	}

	
}
