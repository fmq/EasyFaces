package ar.com.easytech.faces.component.data;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;

@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "ar.com.easyfaces.DataPanelRenderer")
public class DataPanelRenderer extends HtmlBasicRenderer {

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
	      throws IOException {
	
		rendererParamsNotNull(context, component);
        if (!shouldEncode(component)) {
            return;
        }

	    String style = (String) component.getAttributes().get("style");
	    String styleClass = (String) component.getAttributes().get("styleClass");
	    String currentValue = (String) component.getAttributes().get("value");
	    
	    ResponseWriter writer = context.getResponseWriter();
	
	    writer.startElement("div", component);
	    
	    writeIdAttributeIfNecessary(context, writer, component);
        if (styleClass != null)  writer.writeAttribute("class", styleClass, "styleClass");
        if (style != null) writer.writeAttribute("style", style, "style");
        if (currentValue != null)  writer.writeAttribute("value", currentValue, "value");
        
	}

	    
    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        // Render our children recursively
        Iterator<UIComponent> kids = getChildren(component);
        while (kids.hasNext()) {
            encodeRecursive(context, kids.next());
        }

    }


    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

        rendererParamsNotNull(context, component);
        if (!shouldEncode(component)) {
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
        
    }


    @Override
    public boolean getRendersChildren() {

        return true;

    }
	
}
