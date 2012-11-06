package ar.com.easytech.faces.dashboard.widgets.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.dashboard.component.widgets.BaseWidget;
import ar.com.easytech.faces.dashboard.component.widgets.PieChart;
import ar.com.easytech.faces.dashboard.model.Widget;
import ar.com.easytech.faces.renderer.BaseRenderer;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Widget", rendererType = "ar.com.easyfaces.PieChartRenderer")
public class BaseWidgetRenderer extends BaseRenderer {
	
	public void preDecode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}

	
	public void startWidget(FacesContext context, UIComponent component,long instanceId) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId(context);
		
		// Start elements
		writer.startElement("div", component);
		writer.writeAttribute("id", clientId, "id");
		
		String sClass = "widgetbox ";
		if (component.getAttributes().get("widgetStyleClass") != null)
			sClass += (String)component.getAttributes().get("widgetStyleClass");
		
		writer.writeAttribute("class", sClass, "class");
		// Add instance id of widget
		writer.writeAttribute("data-instance-id", instanceId, null);
		//Header
		writer.startElement("div", component);
		writer.writeAttribute("id", clientId+ "Header", "id");
		
		sClass = "widget-header ";
		if (component.getAttributes().get("headerStyleClass") != null)
			sClass += component.getAttributes().get("headerStyleClass");
		
		writer.writeAttribute("class", sClass, "class");
		writer.write((String) component.getAttributes().get("title"));
		writer.endElement("div");
		
		//Body
		writer.startElement("div", component);
		writer.writeAttribute("id", clientId+ "Body", "id");
		
		sClass = "widget-body ";
		if (component.getAttributes().get("bodyStyleClass") != null)
			sClass += component.getAttributes().get("bodyStyleClass");
		writer.writeAttribute("class", sClass, "class");
		if (component.getAttributes().get("height") != null) {
	        // Add height
			int height = (Integer)component.getAttributes().get("height") + 50;
			writer.writeAttribute("style", "height: " + height + "px;", null);
		}
	
		
	}
	
	public void endWidget(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
		writer.endElement("div");
	}

	
}
