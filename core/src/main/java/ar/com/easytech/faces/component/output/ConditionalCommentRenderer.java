package ar.com.easytech.faces.component.output;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.renderer.BaseRenderer;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Output", 
					rendererType = "ar.com.easyfaces.ConditionalCommentRenderer")
public class ConditionalCommentRenderer extends BaseRenderer {

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException { 
	
		ConditionalComment comment = (ConditionalComment)component;
		String test = comment.getTest();
		
		if ( test == null)
			throw new IllegalArgumentException("clause is required");
		
		ResponseWriter writer = context.getResponseWriter();
		writer.write("<!--[if ");
        writer.writeText(test, comment, "test");
        writer.write("]>");
        super.encodeChildren(context, comment);
        writer.write("<![endif]-->");
		
	}

}
