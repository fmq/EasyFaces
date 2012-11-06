package ar.com.easytech.faces.component;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.ListenersFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostRestoreStateEvent;

import ar.com.easytech.faces.utils.AjaxUtils;
import ar.com.easytech.faces.utils.ScriptUtils;

@FacesComponent(JavaScriptComponentBase.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="js", name="jquery.js", target="head"),
	@ResourceDependency(library = "js", name = "jquery-ui.js", target = "head"),
    @ResourceDependency(library="javax.faces", name="jsf.js", target="head"), 
    @ResourceDependency(library="easyfaces", name="easyfaces.js", target="head")
})
@ListenersFor ({
	@ListenerFor(systemEventClass=PostAddToViewEvent.class),
    @ListenerFor(systemEventClass=PostRestoreStateEvent.class)
})
public class JavaScriptComponentBase extends UIComponentBase {

	private static Logger logger = Logger.getLogger(JavaScriptComponentBase.class.toString());
	
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
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        
		// This should place the component at the end of the target
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = event.getComponent();
        
        // This was reported by mmartin that when placed in non ajax request 
        // the call to the JS was made but before the DOM rendered.
        /*if (event instanceof PostAddToViewEvent) {
        	if (AjaxUtils.isAjaxRequest()) {
	        	String target = (String) component.getAttributes().get("target");
	        
		        // In most cases we should rendered ajax calls at the end of the BODY
		        // so all DOM is present...so default to that unless specified 
		        if (target == null )
		        	target = "body";
	            context.getViewRoot().addComponentResource(context, component, target);
        	}
        } else if (event instanceof PostRestoreStateEvent) {
            
            if (AjaxUtils.isAjaxRequest()) {
                Collection<String> renderIds = context.getPartialViewContext().getRenderIds();
                String clientId = getClientId(context);

                if (!renderIds.contains(clientId)) {
                        renderIds.add(clientId);
                }
            }
        }*/
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException {

		if (isRendered() && getChildCount() > 0 ) {
			
			ResponseWriter writer = context.getResponseWriter();
			String clientId = getClientId(context);
			ScriptUtils.startScript(writer, clientId);
			
			// This will make the script run at the end of 
			// the java execution chain (if it's an Ajax Request)
			// We are using our own implementation of oncomplete that is
			// also used in &l;tef:ajax&gt;
			if (AjaxUtils.isAjaxRequest())
				writer.write(AjaxUtils.onCompleteStart());
			logger.info(AjaxUtils.onCompleteStart());
			//Render the actual JS...
			for (UIComponent child : getChildren()) {
				child.encodeAll(context);
			}
			logger.info(AjaxUtils.onCompleteEnd());
			//Close the on complete..
			if (AjaxUtils.isAjaxRequest())
				writer.write(AjaxUtils.onCompleteEnd());
			
			ScriptUtils.endScript(writer);
		}
	}
}
