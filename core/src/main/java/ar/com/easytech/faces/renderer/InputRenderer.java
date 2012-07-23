package ar.com.easytech.faces.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.utils.StringUtils;

import com.sun.faces.renderkit.html_basic.TextRenderer;

/**
 * @author FMQ
 *
 */
@FacesRenderer(componentFamily = "javax.faces.Input",
			   rendererType = "javax.faces.Text")
public class InputRenderer extends TextRenderer {

	@Override
	protected void getEndTextToRender(FacesContext context, UIComponent component,
			String currentValue) throws IOException {
		
		final ResponseWriter originalResponseWriter = context.getResponseWriter();
	    context.setResponseWriter(new ResponseWriterWrapper() {

	        @Override
	        public ResponseWriter getWrapped() {
	            return originalResponseWriter;
	        }   

	        @Override
	        public void startElement(String name, UIComponent component)
	                throws IOException {
	            super.startElement(name, component);
	            if(name!=null && name.equals("input")){
	            	// Placeholder
	                String attribute = (String)component.getAttributes().get("placeholder");
	                if (!StringUtils.isEmpty(attribute)) {
	                    super.writeAttribute("placeholder", attribute, "placeholder");
	                } 
	                // Data-*
	                for (String key : component.getAttributes().keySet()) {
	                	if (key.startsWith("data-"))
	                		super.writeAttribute(key, component.getAttributes().get(key), key);
	                }

	            }
	        }   
	    });
	    super.getEndTextToRender(context, component, currentValue);
	    context.setResponseWriter(originalResponseWriter);
	}
	

}
