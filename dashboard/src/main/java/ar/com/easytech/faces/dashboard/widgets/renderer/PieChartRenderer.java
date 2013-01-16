package ar.com.easytech.faces.dashboard.widgets.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.dashboard.component.widgets.PieChart;
import ar.com.easytech.faces.utils.AjaxUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Widget", rendererType = "ar.com.easyfaces.PieChartRenderer")
public class PieChartRenderer extends BaseWidgetRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		PieChart pieChart = (PieChart)component;

		ResponseWriter writer = context.getResponseWriter();
		String clientId = generateId(context, component, "pie");
		//Javascript call
	    writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		AjaxUtils.newLine(writer);
		writer.write("jQuery(document).ready(function() {");
		AjaxUtils.newLine(writer);
		String data = (String)pieChart.getData() ;
		writer.write("		var data = " + data + ";");
		// Options
		AjaxUtils.newLine(writer);
		writer.write("		var options = { series: { pie: { show: true } }");
		writer.write(",legend: {position: 'ne' , margin: [5,5]}");
		writer.write(", grid: { hoverable: true, clickable: true } };");
		//
		AjaxUtils.newLine(writer);
		String tmpId = clientId + "_graph";
		writer.write("		jQuery.plot(jQuery('' + Dashboard.escapeId('"+ tmpId + "')), data, options);");
		AjaxUtils.newLine(writer);
		writer.write("});");
		AjaxUtils.newLine(writer);
		writer.endElement("script");
		
		// Start elements
		startWidget(context, component, pieChart.getInstanceId() );
		//Wrapper
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_wrapper", "id");
		
		String style = null;
		
		if (pieChart.getHeight() != null)
			style = "height: " + pieChart.getHeight() + 25 + "px;";
		
		/* Es necesario? idem chartRenderer -Matías
		 * if (pieChart.getWidth() != null) 
			if (style != null)
				style +="width: " + pieChart.getWidth() + "px;";
			else
				style = "width: " + pieChart.getWidth() + "px;"; */
		writer.writeAttribute("style", style, "style");
		//Graph
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_graph", "id");
		
		style = null;
		
		if (pieChart.getHeight() != null)
			style = "height: " + pieChart.getHeight() + "px;";
		
		if (pieChart.getWidth() != null)
			if (style != null)
				style +="width: " + pieChart.getWidth() + "px;";
			else
				style = "width: " + pieChart.getWidth() + "px;"; 
		writer.writeAttribute("style", style, "style");
		writer.writeAttribute("class", "graph", "class");
		writer.endElement("div");
        writer.endElement("div");		
	
		endWidget(context, component);
	}

	
}
