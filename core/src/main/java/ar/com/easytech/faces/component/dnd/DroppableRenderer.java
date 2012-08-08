package ar.com.easytech.faces.component.dnd;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import ar.com.easytech.faces.event.DropEvent;
import ar.com.easytech.utils.AjaxRequest;
import ar.com.easytech.utils.StringUtils;

@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "ar.com.easyfaces.DroppableRenderer")
public class DroppableRenderer extends Renderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		Droppable droppable = (Droppable)component;

	 	Map<String, List<ClientBehavior>> behaviors = droppable.getClientBehaviors();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if(null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if(behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
               String behaviorSource = params.get("javax.faces.source");
               String clientId = droppable.getClientId();

               if(behaviorSource != null && clientId.startsWith(behaviorSource)) {
                   for(ClientBehavior behavior: behaviorsForEvent) {
                	   behavior.decode(context, droppable);
                   }
               }
            }
        }
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		Droppable droppable = (Droppable)component;
		
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId(context);
		UIComponent targetComponent = droppable.findComponent(droppable.getFor());
		
        if(targetComponent == null)
            throw new FacesException("Cannot find component \"" + droppable.getFor());
        
        String target = targetComponent.getClientId();

        writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("$(function() {");
		writer.write("$( '#" + target.replace(":", "\\\\:") + "').droppable({");

		if (droppable.getActiveClass() != null) writer.write(" activeClass: '" + droppable.getActiveClass() + "',");
		if (droppable.getHoverClass() != null) writer.write(" hoverClass: '" + droppable.getHoverClass() + "',");
		if (droppable.getAccept() != null) writer.write(" accept: '" + droppable.getAccept() + "',");
		if (droppable.getTolerance() != null) writer.write(" tolerance: '" + droppable.getTolerance() + "',");
			
		//Ajax call on drop...This is allways created.
		writer.write(" drop: function( event, ui ) { ");
		writer.write(" $( this ).find( '.placeholder' ).remove(); ");
		if (droppable.getTargetType() != null &&  droppable.getTargetType().equalsIgnoreCase("list")) 
			writer.write("$( '<li></li>' ).text( ui.draggable.text() ).appendTo( this );");
		
		if (droppable.getOnDrop() != null) writer.write(droppable.getOnDrop());
		writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("drop"))
									  .addExecute(StringUtils.addSingleQuotes(droppable.getClientId()))
									  .addSource(StringUtils.addSingleQuotes(droppable.getClientId()))
									  .addOther("sourceId", "EasyFaces.getDragSource(ui.draggable)")
									  .addOther("targetId", StringUtils.addSingleQuotes(target)).getAjaxCall());
		
		writer.write(" } ");
		// If dropOut event is defined
		if (droppable.getOnDropOut() != null) {
			writer.write(" dropout: function( event, ui ) { ");
			writer.write(droppable.getOnDropOut());
			writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("dropout"))
					  .addExecute(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addSource(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addOther("sourceId", "ui.draggable.attr('id')")
					  .addOther("targetId", StringUtils.addSingleQuotes(target)).getAjaxCall());
			writer.write(" } ");
		}
		
		encodeClientBehaviors(context, droppable);

		writer.write("});");
		writer.write("});");
		writer.endElement("script");
	}

	// Private
	
	private void encodeClientBehaviors(FacesContext context, Droppable component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        //ClientBehaviors
        Map<String,List<ClientBehavior>> behaviorEvents = component.getClientBehaviors();

        if(!behaviorEvents.isEmpty()) {
            String clientId = ((UIComponent) component).getClientId(context);
            List<ClientBehaviorContext.Parameter> params = Collections.emptyList();

            writer.write(",behaviors:{");

            for(Iterator<String> eventIterator = behaviorEvents.keySet().iterator(); eventIterator.hasNext();) {
                String event = eventIterator.next();
                String domEvent = event;
                
                writer.write(domEvent + ":");
                writer.write("function(event) {");
                for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);

                    if(script != null) {
                        writer.write(script);
                    }
                }
                writer.write("}");

                if(eventIterator.hasNext()) {
                    writer.write(",");
                }
            }

            writer.write("}");
        }
    }
	
}
