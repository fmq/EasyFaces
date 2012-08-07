package ar.com.easytech.faces.component.dnd;

import java.io.IOException;
import java.util.Collections;
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
                	   AjaxBehaviorEvent event = new AjaxBehaviorEvent(droppable, behavior);
                	   droppable.queueEvent(event);
                   }
               }
            }
        }
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		Droppable droppable = (Droppable)component;
		
		ClientBehaviorContext behaviorContext =
				  ClientBehaviorContext.createClientBehaviorContext(context,component, Droppable.DEFAULT_EVENT, droppable.getClientId(), null);

		Application application = context.getApplication();
        AjaxBehavior behavior = (AjaxBehavior)application.createBehavior(AjaxBehavior.BEHAVIOR_ID);
        droppable.addClientBehavior("drop", behavior);

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

		//Ajax call on drop...
		writer.write(" drop: function( event, ui ) { ");
		writer.write(" $( this ).find( '.placeholder' ).remove(); ");
		writer.write(" jsf.ajax.request(this,event,{execute: '" + droppable.getClientId() +"', 'javax.faces.behavior.event': 'drop', sourceId: ui.draggable.attr('id') , targetId: $(this).attr('id')}); ");
		writer.write(" } ");

//		Map<String,List<ClientBehavior>> behaviors = getClientBehaviors();
//		if (behaviors.containsKey(DEFAULT_EVENT) ) {
//			String drop = behaviors.get(DEFAULT_EVENT).get(0).getScript(behaviorContext);
//			writer.writeAttribute("drop:", drop, null);
//		}

		encodeClientBehaviors(context, droppable);

		writer.write("});");
		writer.write("});");
		writer.endElement("script");
	}

	// Private

	private boolean isRequestSource(FacesContext context, Droppable component) {
		return component.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get("javax.faces.source"));
	}

	protected void encodeClientBehaviors(FacesContext context, Droppable component) throws IOException {
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

                if(event.equalsIgnoreCase("valueChange"))       //editable value holders
                    domEvent = "change";
                else if(event.equalsIgnoreCase("action"))       //commands
                    domEvent = "click";

                writer.write(domEvent + ":");

                writer.write("function(event) {");
                for(Iterator<ClientBehavior> behaviorIter = behaviorEvents.get(event).iterator(); behaviorIter.hasNext();) {
                    ClientBehavior behavior = behaviorIter.next();
                    ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, clientId, params);
                    String script = behavior.getScript(cbc);    //could be null if disabled

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
