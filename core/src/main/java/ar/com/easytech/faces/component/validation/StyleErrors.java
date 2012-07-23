package ar.com.easytech.faces.component.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import ar.com.easytech.faces.component.JavaScriptComponentBase;
import ar.com.easytech.utils.StringUtils;

@FacesComponent(StyleErrors.COMPONENT_TYPE)
@ResourceDependencies({
        @ResourceDependency(library="javax.faces", name="jsf.js", target="head"), 
        @ResourceDependency(library="easyfaces", name="easyfaces.js", target="head")
        //ResourceDependency(library="jquery", name="jquery.js", target="head"),
        //ResourceDependency(library="jquery", name="jqueryui.js", target="head")
})
public class StyleErrors extends JavaScriptComponentBase {

	public static final String COMPONENT_TYPE  = "ar.easyfaces.js.StyleErrors";
	
	public StyleErrors() {
		getChildren().add(new UIOutput());
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		
		//I just need to add &ltscript&gt EasyFaces.styleErrors(ids,style); &ltscript&gt
		// So I create my component (it's actually only the script)
		// Get the form
		UIViewRoot viewRoot = context.getViewRoot();
		UIForm form = null;
		if (context.isPostback() && context.isValidationFailed()) {
			for (String key : context.getExternalContext().getRequestParameterMap().keySet()) {
				if (!key.startsWith("faces")) {
					UIComponent component = viewRoot.findComponent(key);
					if (component instanceof UIForm) {
						form = (UIForm) component;
						break;
					}
				}
			}
			
			if (form == null)
				return;
			
			List<String> invalidIds = new ArrayList<String>();
			// now just loop the form and check for invalid uiInputs
			for (UIComponent component : form.getChildren()) {
				if (component instanceof UIInput 
						&& !((UIInput)component).isValid()) 
					invalidIds.add(((UIInput)component).getId());
			}
			// now generate the list as array and add to the call
			StringBuilder theCall = new StringBuilder();
			theCall.append("EasyFaces.styleErrors('");
			theCall.append(StringUtils.listAsString(invalidIds, ",")).append("','");
			
			String errorClass = getStyleClass() == null ? "error" : getStyleClass();
			theCall.append(errorClass).append("');");
			//Set the compoent that was created in instantiation
			UIOutput theComponent = (UIOutput) getChildren().get(0);
			theComponent.setValue(theCall.toString());
		}
		
		// the actual encoding is done in the parent
		super.encodeChildren(context);
	
	}
	
	public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass) {
	        getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}

	public String getSetFocus() {
		return (String) getStateHelper().eval(PropertyKeys.setFocus);
	}

	public void setSetFocus(String setFocus) {
		getStateHelper().put(PropertyKeys.setFocus, setFocus);
	}
	
	protected enum PropertyKeys {
		styleClass, setFocus;
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
}
