package ar.com.easytech.faces.renderer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;


/**
 * @author FMQ
 *
 */
@FacesRenderer(componentFamily = "javax.faces.Command",
				rendererType = "javax.faces.Link")
public class CommandLinkRenderer extends com.sun.faces.renderkit.html_basic.CommandLinkRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.COMMANDLINK);

    
	/*
     * Render the necessary Javascript for the link.
     * Note that much of this code is shared with CommandButtonRenderer.renderOnClick
     * RELEASE_PENDING: Consolidate this code into a utility method, if possible.
     */
    protected void renderAsActive(FacesContext context, UIComponent command)
          throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        assert(writer != null);
        String formClientId = RenderKitUtils.getFormClientId(command, context);
        if (formClientId == null) {
            return;
        }

        //make link act as if it's a button using javascript        
        writer.startElement("a", command);
        writeIdAttributeIfNecessary(context, writer, command);
        writer.writeAttribute("href", "#", "href");
        RenderKitUtils.renderPassThruAttributes(context,
                                                writer,
                                                command,
                                                ATTRIBUTES,
                                                getNonOnClickBehaviors(command));

        RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, command);

        String target = (String) command.getAttributes().get("target");
        if (target != null) {
            target = target.trim();
        } else {
            target = "";
        }

        // EasyTech
        // Data-*
        for (String key : command.getAttributes().keySet()) {
        	if (key.startsWith("data-"))
        		writer.writeAttribute(key, command.getAttributes().get(key), key);
        }
        // EasyTech
        // REL
        String rel = (String)command.getAttributes().get("rel");
        if (rel != null && !rel.equals(""))
        	writer.writeAttribute("rel", rel, "rel");
        // End ET 
        Collection<ClientBehaviorContext.Parameter> params = getBehaviorParameters(command);
        RenderKitUtils.renderOnclick(context, 
                                     command,
                                     params,
                                     target,
                                     true);

        writeCommonLinkAttributes(writer, command);

        // render the current value as link text.
        writeValue(command, writer);
        writer.flush();

    }
    
    // Returns the Behaviors map, but only if it contains some entry other
    // than those handled by renderOnclick().  This helps us optimize
    // renderPassThruAttributes() in the very common case where the
    // link only contains an "action" (or "click") Behavior.  In that
    // we pass a null Behaviors map into renderPassThruAttributes(),
    // which allows us to take a more optimized code path.
    private static Map<String, List<ClientBehavior>> getNonOnClickBehaviors(UIComponent component) {

        return getPassThruBehaviors(component, "click", "action");
    }	

}
