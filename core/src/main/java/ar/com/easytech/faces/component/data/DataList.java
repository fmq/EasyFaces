package ar.com.easytech.faces.component.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import ar.com.easytech.faces.component.DataItem;
import ar.com.easytech.faces.event.DataEvent;

@FacesComponent(DataList.COMPONENT_TYPE)
@ResourceDependencies({
		@ResourceDependency(library = "js", name = "jquery.js", target = "head"),
		@ResourceDependency(library = "js", name = "jquery-ui.js", target = "head"),
		@ResourceDependency(library = "javax.faces", name = "jsf.js"),
		@ResourceDependency(library = "easyfaces", name = "easyfaces.js") })
public class DataList extends DataItem implements ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "ar.com.easyfaces.DataList";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.DataListRenderer";
	public static final String DEFAULT_EVENT = "update";

	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT));

	public DataList() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String eventName = params.get("javax.faces.behavior.event");
		AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

		if (eventName.equals("update")) {
			String sourceId = params.get("sourceId");
			String position = params.get("dropedPosition");
			Object data = params.get("dataValue");
			DataEvent dataEvent = null;

			if (data != null) {
				dataEvent = new DataEvent(this, behaviorEvent.getBehavior(),
						sourceId, position, data);
			} else {
				dataEvent = new DataEvent(this, behaviorEvent.getBehavior(),
						sourceId, position);
			}

			super.queueEvent(dataEvent);
		} else
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

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	// Attributes
	public String getItemStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.itemStyleClass);
	}

	public void setItemStyleClass(String itemStyleClass) {
		getStateHelper().put(PropertyKeys.itemStyleClass, itemStyleClass);
	}

	public String getHoverClass() {
		return (String) getStateHelper().eval(PropertyKeys.hoverClass);
	}

	public void setHoverClass(String hoverClass) {
		getStateHelper().put(PropertyKeys.hoverClass, hoverClass);
	}

	public String getSortable() {
		return (String) getStateHelper().eval(PropertyKeys.sortable);
	}

	public void setSortable(String sortable) {
		getStateHelper().put(PropertyKeys.sortable, sortable);
	}

	public String getType() {
		return (String) getStateHelper().eval(PropertyKeys.type);
	}

	public void setType(String type) {
		getStateHelper().put(PropertyKeys.type, type);
	}

	protected enum PropertyKeys {
		itemStyleClass, hoverClass, sortable, type;
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
