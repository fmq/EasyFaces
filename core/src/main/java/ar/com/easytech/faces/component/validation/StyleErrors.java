package ar.com.easytech.faces.component.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

import ar.com.easytech.faces.component.JavaScriptComponentBase;
import ar.com.easytech.utils.ComponentUtils;
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
	private Set<VisitHint> FORM_TREE_HINTS = EnumSet.of(VisitHint.SKIP_UNRENDERED);
	
	public StyleErrors() {
		getChildren().add(new UIOutput());
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		
		UIOutput theComponent = (UIOutput) getChildren().get(0);
		theComponent.setValue(null);
		//I just need to add &ltscript&gt EasyFaces.styleErrors(ids,style); &ltscript&gt
		// So I create my component (it's actually only the script)
		// Get the form that encloses the component
		if (context.isPostback() && context.isValidationFailed()) {
			
			List<UIForm> forms = new ArrayList<UIForm>();
			
			// If component that needs validation is specified get it's
			// form.. (it should always be a form anyway..
			if (getFor() != null) {
				// Search for the form of the component..
				forms.add(ComponentUtils.getComponentForm(ComponentUtils.getComponentById(getFor())));
			} else {
				// if for is null.. I have to assume that all forms must be scanned.. 
				forms.addAll(ComponentUtils.getAllFormsForView());
			}
			
			for (UIForm form : forms) {
			
				final List<String> invalidIds = new ArrayList<String>();
				// Tried to loop through children but it was almost impossible..
				// Fortunately.. visitTree does the trick
				form.visitTree(VisitContext.createVisitContext(context, null, FORM_TREE_HINTS), new VisitCallback() {
					public VisitResult visit(VisitContext context, UIComponent component) {
						if (component instanceof UIInput) {
				            UIInput input = (UIInput) component;
				            if (!input.isValid()) {
				            	invalidIds.add(((UIInput)component).getClientId());
				            }
				        }
						
						return VisitResult.ACCEPT;
					}
				});
				if (invalidIds.size() > 0) {
					// now generate the list as array and add to the call
					StringBuilder theCall = new StringBuilder();
					theCall.append("EasyFaces.styleErrors('");
					theCall.append(StringUtils.listAsString(invalidIds, ",")).append("','");
					String errorClass = getStyleClass() == null ? "error" : getStyleClass();
					theCall.append(errorClass).append("')");
					//Set the component that was created in instantiation
					theComponent.setValue(theCall.toString());
				}
			}
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

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forVal);
	}

	public void setFor(String forParam) {
		getStateHelper().put(PropertyKeys.forVal, forParam);
	}
	
	protected enum PropertyKeys {
		forVal("for"), styleClass, setFocus;
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
