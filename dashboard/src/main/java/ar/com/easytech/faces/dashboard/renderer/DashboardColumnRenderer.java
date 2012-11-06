package ar.com.easytech.faces.dashboard.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.persistence.Column;

import ar.com.easytech.faces.dashboard.component.DashboardColumn;
import ar.com.easytech.faces.renderer.BaseRenderer;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Dashboard", rendererType = "ar.com.easyfaces.DashboardColumnRenderer")
public class DashboardColumnRenderer extends BaseRenderer {
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();
		DashboardColumn column = (DashboardColumn)component;
		// Start elements
		writer.startElement("div", component);
		String clientId = generateId(context, component, "column");
		writer.writeAttribute("id", clientId, null);
	    writer.writeAttribute("data-layout-id", column.getLayoutId(), null);
		String sClass = "column ";
		if (component.getAttributes().get("styleClass") != null)
			sClass += (String)component.getAttributes().get("styleClass");
		
		writer.writeAttribute("class", sClass, "class");
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
    	writer.endElement("div");
    	
	}
	
	@Override
    public boolean getRendersChildren() {
        return true;
    }


}
