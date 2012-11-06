package ar.com.easytech.faces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

public class DataEvent extends AjaxBehaviorEvent {

	private static final long serialVersionUID = 5970173012951136750L;

	private String sourceId;
	private String position;
    private Object data;
	
	public DataEvent(UIComponent component, Behavior behavior, String sourceId, String position) {
		super(component, behavior);
		this.sourceId = sourceId;
		this.position = position;
	}

    public DataEvent(UIComponent component, Behavior behavior, String sourceId, String position, Object data) {
		super(component, behavior);
		this.sourceId = sourceId;
		this.position = position;
        this.data = data;
	}

	@Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
        return (faceslistener instanceof AjaxBehaviorListener);
	}

	@Override
	public void processListener(FacesListener faceslistener) {
		((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
	}
	
	public String getSourceId() {
		return sourceId;
	}

	public String getPosition() {
		return position;
	}

    public Object getData() {
        return data;
    }

}
