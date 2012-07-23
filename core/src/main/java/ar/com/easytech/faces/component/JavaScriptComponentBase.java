package ar.com.easytech.faces.component;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

@FacesComponent(JavaScriptComponentBase.COMPONENT_TYPE)
@ResourceDependencies({
        @ResourceDependency(library="javax.faces", name="jsf.js", target="head"), 
        @ResourceDependency(library="easyfaces", name="easyfaces.js", target="head")
        //ResourceDependency(library="jquery", name="jquery.js", target="head"),
        //ResourceDependency(library="jquery", name="jqueryui.js", target="head")
})
public class JavaScriptComponentBase extends UIComponentBase {

	public static final String COMPONENT_FAMILY = "ar.easyfaces.component.js";
	public static final String COMPONENT_TYPE = "ar.easyfaces.js.script";
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {

		ResponseWriter writer = context.getResponseWriter();

		String clientId = getClientId(context);
		writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_js", null);
		writer.writeAttribute("type", "text/javascript", null);
		for (UIComponent child : getChildren()) {
			child.encodeAll(context);
		}
		writer.endElement("script");

	}
}
