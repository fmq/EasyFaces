package ar.com.easytech.faces.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public abstract class BaseRenderer extends Renderer {

	protected void doRenderChildren(FacesContext context, UIComponent component) throws IOException {
		for (Iterator<UIComponent> iterator = component.getChildren().iterator(); iterator.hasNext();) {
			UIComponent child = (UIComponent) iterator.next();
			renderChild(context, child);
		}
	}
	
	protected void renderChild(FacesContext context, UIComponent child) throws IOException {
		if (!child.isRendered()) {
			return;
		}
		
		child.encodeBegin(context);
		
		if (child.getRendersChildren()) {
			child.encodeChildren(context);
		} else {
			doRenderChildren(context, child);
		}
		child.encodeEnd(context);
	}
	
	protected void encodeClientBehaviors(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        
        //ClientBehaviors
        Map<String,List<ClientBehavior>> behaviorEvents = ((ClientBehaviorHolder) component).getClientBehaviors();

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
	
	protected void decodeClientBehaviors(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String behaviorEvent = params.get("javax.faces.behavior.event");

        if(null != behaviorEvent) {
            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

            if(behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
               String behaviorSource = params.get("javax.faces.source");
               String clientId = component.getClientId();

               if(behaviorSource != null && clientId.startsWith(behaviorSource)) {
                   for(ClientBehavior behavior: behaviorsForEvent) {
                	   behavior.decode(context, component);
                   }
               }
            }
        }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Object> convertDataToList(UIData dataObject) {
		List<Object> data = new ArrayList<Object>();
		// For now we support only lists, sets and maps..
		if (dataObject.getValue() instanceof List ) { 
			return (List<Object>) dataObject.getValue();
		} else if (dataObject.getValue() instanceof Set) {
			Set dataSet = (Set)dataObject.getValue();
			Iterator iter =  dataSet.iterator();
			while (iter.hasNext()) {
				data.add(iter.next());
			}
		} else if (dataObject.getValue() instanceof Map) {
			Map dataMap = (Map)dataObject.getValue();
			for (Object key : dataMap.keySet()) {
				data.add(dataMap.get(key));
			}
		} else if (dataObject.getValue() instanceof Object[])  {
			
			Object[] dataObj = (Object[]) dataObject.getValue(); 
			for (int i=0; i< dataObj.length ; i++)
				data.add(dataObj[i]);
		}
		
		return data;
		
	}
	
	protected String generateId(FacesContext context, UIComponent component, String prefix) throws IOException {
		
		Random random = new Random();
		
		String clientId = null;
	    if (component.getId( ) != null && 
	        !component.getId( ).startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
	        clientId = component.getClientId(context);
	    }

	    if (clientId == null) {
	    	if (component.getParent() != null) {
		    	String parentId = component.getParent().getId();
		    	if (parentId != null)
		    		clientId = parentId + ":" + prefix + "_" + random;
		    	else {
		    		
		    		clientId = prefix + "_" + random.nextInt(99);
		    	}
	    	} else {
	    		clientId = prefix + "_" + random.nextInt(99);
	    	}
	    }
	    
	   return clientId;
		
	}
	
}
