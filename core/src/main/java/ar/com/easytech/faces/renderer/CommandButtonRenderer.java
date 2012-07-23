package ar.com.easytech.faces.renderer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;


import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;

/**
 * @author FMQ
 * 
 * Agrega la funcionalidad de data-* a los botones
 */
@FacesRenderer(componentFamily = "javax.faces.Command", rendererType = "javax.faces.Button")
public class CommandButtonRenderer extends ButtonRenderer {

	private static final Attribute[] ATTRIBUTES =
	          AttributeManager.getAttributes(AttributeManager.Key.COMMANDBUTTON);

	
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
	// Hoy por hoy se tiene que hacer de esta manera
		rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        // Which button type (SUBMIT, RESET, or BUTTON) should we generate?
        String type = getButtonType(component);

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);

        String label = "";
        Object value = ((UICommand) component).getValue();
        if (value != null) {
            label = value.toString();
        }

        /*
         * If we have any parameters and the button type is submit or button, 
         * then render Javascript to use later.
         * RELEASE_PENDING this logic is slightly wrong - we should buffer the user onclick, and use it later.
         * Leaving it for when we decide how to do script injection.
         */

        Collection<ClientBehaviorContext.Parameter> params = getBehaviorParameters(component);
        if ( !params.isEmpty() && (type.equals("submit") || type.equals("button"))) {
           RenderKitUtils.renderJsfJs(context);
        }

        String imageSrc = (String) component.getAttributes().get("image");
        writer.startElement("input", component);
        writeIdAttributeIfNecessary(context, writer, component);
        String clientId = component.getClientId(context);
        if (imageSrc != null) {
            writer.writeAttribute("type", "image", "type");
            writer.writeURIAttribute("src", RenderKitUtils.getImageSource(context, component, "image"), "image");
            writer.writeAttribute("name", clientId, "clientId");
        } else {
            writer.writeAttribute("type", type, "type");
            writer.writeAttribute("name", clientId, "clientId");
            writer.writeAttribute("value", label, "value");
        }

        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                component,
                                                ATTRIBUTES,
                                                getNonOnClickBehaviors(component));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

        String styleClass = (String)
              component.getAttributes().get("styleClass");
        if (styleClass != null && styleClass.length() > 0) {
            writer.writeAttribute("class", styleClass, "styleClass");
        }
        
        // EasyTech
        // Data-*
        for (String key : component.getAttributes().keySet()) {
        	if (key.startsWith("data-"))
        		writer.writeAttribute(key, component.getAttributes().get(key), key);
        }

        RenderKitUtils.renderOnclick(context, 
                                     component, 
                                     params,
                                     null,
                                     false);

        writer.endElement("input");
	}
	
	/**
     * <p>If the component's type attribute is null or not equal
     * to <code>reset</code>, <code>submit</code> or <code>button</code>,
     * default to <code>submit</code>.
     * @param component the component of interest
     * @return the type for this button
     */
    private static String getButtonType(UIComponent component) {

        String type = (String) component.getAttributes().get("type");
        if (type == null || (!"reset".equals(type) &&
                !"submit".equals(type) && !"button".equals(type))) {
            type = "submit";
            // This is needed in the decode method
            component.getAttributes().put("type", type);
        }
        return type;

    }
    
 // Returns the Behaviors map, but only if it contains some entry other
    // than those handled by renderOnclick().  This helps us optimize
    // renderPassThruAttributes() in the very common case where the
    // button only contains an "action" (or "click") Behavior.  In that
    // we pass a null Behaviors map into renderPassThruAttributes(),
    // which allows us to take a more optimized code path.
    private static Map<String, List<ClientBehavior>> getNonOnClickBehaviors(UIComponent component) {

        return getPassThruBehaviors(component, "click", "action");
    }
}