package ar.com.easytech.faces.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.GroupRenderer;

@FacesRenderer(componentFamily = "javax.faces.Panel",
				rendererType = "javax.faces.Group")
public class PanelGroupRenderer extends GroupRenderer {
	
	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		// TODO Auto-generated method stub
		super.encodeBegin(context, component);
			
		if (!shouldEncode(component)) {
            return;
        }
		
		ResponseWriter writer = context.getResponseWriter();
		
        for (String key : component.getAttributes().keySet()) {
        	
        	if (key.startsWith("data-"))
        		writer.writeAttribute(key, component.getAttributes().get(key), key);
        }
    }
}
