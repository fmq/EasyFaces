package ar.com.easytech.faces.renderer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.utils.StringUtils;

import com.sun.faces.renderkit.html_basic.SecretRenderer;;

/**
 * @author FMQ
 *
 */
@FacesRenderer(componentFamily = "javax.faces.Input",
			   rendererType = "javax.faces.Secret")
public class InputSecretRenderer extends SecretRenderer {

	private final String[] attrs = {"autocomplete", "autofocus", "list", "pattern", "placeholder"};
	
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
	            	// Attributes
	            	for (String attr : attrs ) {
		                String attribute = (String)component.getAttributes().get(attr);
		                if (!StringUtils.isEmpty(attribute)) {
		                    super.writeAttribute(attr, attribute, attr);
		                }
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
