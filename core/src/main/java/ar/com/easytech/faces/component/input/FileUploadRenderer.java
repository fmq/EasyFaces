package ar.com.easytech.faces.component.input;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;

@FacesRenderer(componentFamily = "javax.faces.Input", rendererType = "ar.com.easyfaces.uploadRenderer")
public class FileUploadRenderer extends Renderer {

	private static final Attribute[] INPUT_ATTRIBUTES = AttributeManager.getAttributes(AttributeManager.Key.INPUTTEXT);
	private static final String EMPTY_STRING = "";

	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		if (!component.isRendered())
			return;
		ResponseWriter writer = context.getResponseWriter();

		String clientId = component.getClientId(context);

		writer.startElement("input", component);
		writer.writeAttribute("type", "file", "type");
		writer.writeAttribute("name", clientId, "clientId");
		// Std styleclass
		String styleClass = (String) component.getAttributes()
				.get("styleClass");
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}
		// Render other attributes
		RenderKitUtils.renderPassThruAttributes(context, writer, component, INPUT_ATTRIBUTES, getPassThruBehaviors(component, "change", "valueChange"));
		RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);
		RenderKitUtils.renderOnchange(context, component, false);
		// End the input
		writer.endElement("input");
		writer.flush();
	}

	public void decode(FacesContext context, UIComponent component) {
		// Validate null params
		ExternalContext external = context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) external.getRequest();
		String clientId = component.getClientId(context);
		FileItem item = (FileItem) request.getAttribute(clientId);

		String target = (String) component.getAttributes().get("target");
		String path = (String)request.getParameter("path");
		

		if (path != null) {
			File file;
			// Construct the filename
			StringBuilder fullFileName = new StringBuilder();
			String fileName = request.getParameter("fileName");
			// Path always ends with /
			path = path.endsWith("/") ? path : path + "/";
			// Path
			fullFileName.append(path);
			// Any specific target
			if (target != null) {
				// Target does not start with slash
				target = target.startsWith("/") ? target.substring(1) : target;
				// and always ends with a slash
				target = target.endsWith("/") ? target : target + "/";
				fullFileName.append(target);
			}
			// Finaly the file nam
			fullFileName.append(fileName);
			file = new File(fullFileName.toString());

			try { 
				item.write(file);
			} catch (Exception ex) {
				throw new FacesException(ex);
			}
			
			((UIInput) component).setSubmittedValue((file != null) ? file : EMPTY_STRING);
			
		} else
			throw new FacesException("path required");
	}

	private Map<String, List<ClientBehavior>> getPassThruBehaviors(
	        UIComponent component,
	        String domEventName,
	        String componentEventName) {

	        if (!(component instanceof ClientBehaviorHolder)) {
	            return null;
	        }

	        Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder)component).getClientBehaviors();

	        int size = behaviors.size();

	        if ((size == 1) || (size == 2)) {
	            boolean hasDomBehavior = behaviors.containsKey(domEventName);
	            boolean hasComponentBehavior = behaviors.containsKey(componentEventName);

	            // If the behavior map only contains behaviors for non-pass
	            // thru attributes, return null.
	            if (((size == 1) && (hasDomBehavior || hasComponentBehavior)) ||
	                ((size == 2) && hasDomBehavior && hasComponentBehavior)) {
	                return null;
	            }
	        }

	        return behaviors;
	    }
}
