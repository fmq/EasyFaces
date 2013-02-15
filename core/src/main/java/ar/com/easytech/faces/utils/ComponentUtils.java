package ar.com.easytech.faces.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class ComponentUtils {

	private static Set<VisitHint> ROOT_TREE_HINTS =EnumSet.of(VisitHint.SKIP_UNRENDERED);
	
    public String[] COMMON_JS_ACTIONS = {"onclick", "ondblclick", "onmousedown", "onmouseup", "onmouseover", "onmousemove", 
    									"onmouseout", "onkeypress", "onkeydown", "onkeyup", "onfocus", "onblur"};
	
    private static final Map<String, String> eventAttributeMap = new HashMap<String, String>();
    
    static {
    	eventAttributeMap.put("blur","onblur"); 
    	eventAttributeMap.put("change","onchange"); 
    	eventAttributeMap.put("click","onclick"); 
    	eventAttributeMap.put("dblclick","ondblclick"); 
    	eventAttributeMap.put("focus","onfocus"); 
    	eventAttributeMap.put("keydown","onkeydown"); 
    	eventAttributeMap.put("keypress","onkeypress"); 
    	eventAttributeMap.put("keyup","onkeyup"); 
    	eventAttributeMap.put("mousedown","onmousedown"); 
    	eventAttributeMap.put("mousemove","onmousemove"); 
    	eventAttributeMap.put("mouseout","onmouseout"); 
    	eventAttributeMap.put("mouseover","onmouseover"); 
    	eventAttributeMap.put("mouseup","onmouseup"); 
    	eventAttributeMap.put("select","onselect"); 
    }
    
	@SuppressWarnings("unchecked")
	public static <T extends UIComponent> T getComponentById(String id) {
		return (T)FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
	}
	
	public static UIForm getComponentForm(UIComponent component) {
		
		if (component == null)
			return null;
		
		UIForm form = null;
		// IF the component is a form.. just return that
		if (component instanceof UIForm)
			return (UIForm)component;
		// IF the parent is the form.. return that...
		if (component.getParent() instanceof UIForm)
			return (UIForm)component.getParent();
		
		//else.. Cycle throw the component parents to search for the form..
		UIComponent parent = component.getParent();
		
		while (parent.getParent() != null)
			if (parent.getParent() instanceof UIForm) {
				form = (UIForm) parent.getParent();
				break;
			} else
				parent = parent.getParent();
		
		return form;
		
	}
	
	public static List<UIForm> getAllFormsForView() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		final List<UIForm> forms = new ArrayList<UIForm>();
		
		root.visitTree(VisitContext.createVisitContext(context, null, ROOT_TREE_HINTS), new VisitCallback() {
			public VisitResult visit(VisitContext context, UIComponent component) {
				if (component instanceof UIForm) {
					forms.add((UIForm) component);
		        }
				
				return VisitResult.ACCEPT;
			}
		});
		
		return forms;
	}
	
	
	// Used to encode attributes and 
	// actions
	public static void encodeJsActions(FacesContext context, UIComponent component, ResponseWriter writer,String[] attributes, Map<String, List<ClientBehavior>> clientBehaviors) throws IOException {
		
		List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
		
		// Render the attribute if it has a value
		for (int i =0 ; i< attributes.length; i++) {
			String attribute = attributes[i];
			String event = attribute.startsWith("on") ? attribute.substring(2) : attribute;
			if (component.getAttributes().get(attribute) != null) {
				// Check if it's a client behaviour
				if (clientBehaviors.containsKey(event))  {
					// Handle as chain
					StringBuilder chain = new StringBuilder(100);
					chain.append("jsf.util.chain(this,event,");
					Logger.getLogger(ComponentUtils.class.getCanonicalName()).log(Level.INFO,"Encoding JSAction {0} ", attribute);
	                String script = getScript(context, component, clientBehaviors, event);
	                if (script != null)
	                	chain.append(script);
					
					// remove from behaviours
					clientBehaviors.remove(event);
					// Add the attribute to the chain
					 if (chain.charAt(chain.length() - 1) != ',')
				            chain.append(',');
					 chain.append(component.getAttributes().get(attribute));
					 chain.append(")");
					 writer.writeAttribute(attribute, chain.toString(), attribute);
				} else {
					// Just render the attribute
					writer.writeAttribute(attribute, component.getAttributes().get(attribute), attribute);
				}
				
			}
		}
		// Now we render the leftover clientBehaviours
		for(Iterator<String> eventIterator = clientBehaviors.keySet().iterator(); eventIterator.hasNext();) {
            String event = eventIterator.next();
		    for(Iterator<ClientBehavior> behaviorIter = clientBehaviors.get(event).iterator(); behaviorIter.hasNext();) {
	            ClientBehavior behavior = behaviorIter.next();
	            ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, event, component.getClientId(), params);
	            String script = behavior.getScript(cbc);
	
	            if(script != null) {
	            	writer.writeAttribute(eventAttributeMap.get(event), script, eventAttributeMap.get(event));
	            }
		    }
		}
    }
	
	public static String getScript(FacesContext context, UIComponent component, Map<String, List<ClientBehavior>> clientBehaviors, String attribute ) {
		
		List<ClientBehaviorContext.Parameter> params = Collections.emptyList();
		
		if (clientBehaviors.get(attribute) != null) {
			for(Iterator<ClientBehavior> behaviorIter = clientBehaviors.get(attribute).iterator(); behaviorIter.hasNext();) {
	            ClientBehavior behavior = behaviorIter.next();
	            ClientBehaviorContext cbc = ClientBehaviorContext.createClientBehaviorContext(context, (UIComponent) component, attribute, component.getClientId(), params);
	            return behavior.getScript(cbc);
			}
        }
		
		return null;
	}

	public static void renderPtAttributes(UIComponent component, ResponseWriter writer, String[] attributes) throws IOException {
		// Loop the attributes and if defined render them
		for (int i=0 ; i < attributes.length ; i++) {
			String att = attributes[i];
			String value = (String) component.getAttributes().get(att);
			
			if (value != null && !value.equals("") )
				writer.writeAttribute(att, value, null);
		}
	}
    
}
