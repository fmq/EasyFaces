package ar.com.easytech.faces.input.autocomplete.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import ar.com.easytech.faces.dashboard.component.Dashboard;

@FacesComponent(InputAutocomplete.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="javax.faces", name="jsf.js"),
	@ResourceDependency(library="easyfaces", name="easyfaces.js"),
	@ResourceDependency(library="js", name="jquery-1.8.2.js"),
	@ResourceDependency(library="js", name="jquery-ui-1.9.0.custom.js"),
	@ResourceDependency(library="css", name="smoothness/jquery-ui-1.9.0.custom.css"),
	@ResourceDependency(library="css", name="widgets.css")
})
public class InputAutocomplete extends UIInput implements ClientBehaviorHolder{
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.InputAutocomplete";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.InputAutocompleteRenderer";
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.InputAutocomplete";

	public final static String DEFAULT_EVENT = "update";
	
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("search","select"));
	
	public InputAutocomplete() {
		setRendererType(RENDERER_TYPE);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getUserId() {
		return (String) getStateHelper().eval(PropertyKeys.userId);
	}

	public void setUserId(String userId) {
		getStateHelper().put(PropertyKeys.userId, userId);
	}
	
	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
	
	public Object getValue() {
		return (Object) getStateHelper().eval(PropertyKeys.value);
	}

	public void setValue(Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}	
	
	public Long getInstanceId() {
		return (Long) getStateHelper().eval(PropertyKeys.instanceId);
	}

	public void setInstanceId(Long instanceId) {
		getStateHelper().put(PropertyKeys.instanceId, instanceId);
	}
	
	public Long getMinLength(){
		return (Long) getStateHelper().eval(PropertyKeys.minLength);
	}

	public void setMinLength(Long minLength) {
		getStateHelper().put(PropertyKeys.minLength, minLength);
	}

	protected enum PropertyKeys {
		userId, styleClass, value, instanceId, minLength;
		String c;

		PropertyKeys() {
		}

		// Constructor needed by "for" property
		PropertyKeys(String c) {
			this.c = c;
		}

		public String toString() {
			return ((this.c != null) ? this.c : super.toString());
		}
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

			Map<String, String> params = context.getExternalContext().getRequestParameterMap();
			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
			super.queueEvent(behaviorEvent);
	}
	
	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public String getDefaultEventName() {
		return DEFAULT_EVENT;
	}
}
