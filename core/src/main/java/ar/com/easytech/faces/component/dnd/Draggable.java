package ar.com.easytech.faces.component.dnd;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

@FacesComponent(Draggable.COMPONENT_TYPE)
public class Draggable extends UIComponentBase  {
	
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Draggable";
	public static final String DEFAULT_RENDERER_TYPE = "ar.com.easyfaces.DraggableRenderer";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	
	// property: for
	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forVal);
	}

	public void setFor(String forParam) {
		getStateHelper().put(PropertyKeys.forVal, forParam);
	}

	public String getDraggableSelector() {
		return (String) getStateHelper().eval(PropertyKeys.draggableSelector);
	}

	public void setDraggableSelector(String draggableSelector) {
		getStateHelper().put(PropertyKeys.draggableSelector, draggableSelector);
	}

	public String getRevert() {
		return (String) getStateHelper().eval(PropertyKeys.revert);
	}

	public void setRevert(String revert) {
		getStateHelper().put(PropertyKeys.revert, revert);
	}

	public String getContainTo() {
		return (String) getStateHelper().eval(PropertyKeys.containTo);
	}

	public void setContainTo(String containTo) {
		getStateHelper().put(PropertyKeys.containTo, containTo);
	}
	
	protected enum PropertyKeys {
		forVal("for"), draggableSelector, revert, containTo;
		String c;

		PropertyKeys() {
		}

		PropertyKeys(String c) {
			this.c = c;
		}

		public String toString() {
			return ((this.c != null) ? this.c : super.toString());
		}
	}
	
	@Override
	public void encodeEnd(FacesContext facesContext) throws IOException {
		ResponseWriter writer = facesContext.getResponseWriter();
		
		String clientId = getClientId(facesContext);
		UIComponent targetComponent = findComponent(getFor());
		
        if(targetComponent == null)
            throw new FacesException("Cannot find component \"" + getFor());
		
        String target = targetComponent.getClientId();
		
		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("$(function() {");
		writer.write("$( '#" + target.replace(":", "\\\\:") + "').draggable({");
		if (getRevert() != null) writer.write(" revert: '" + getRevert() + "', ");
		if (getContainTo() != null) writer.write(" containment: '" + getContainTo() + "',");
		writer.write("});");

		writer.write("});");
		writer.endElement("script");
	}

}
