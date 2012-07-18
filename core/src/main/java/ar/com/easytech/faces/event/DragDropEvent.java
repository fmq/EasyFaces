package ar.com.easytech.faces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

public class DragDropEvent extends AjaxBehaviorEvent {

	private static final long serialVersionUID = 5970173012951136750L;

	private String sourceId;
	private String targetId;
    private Object data;
	
	public DragDropEvent(UIComponent component, Behavior behavior, String sourceId, String targetId) {
		super(component, behavior);
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

    public DragDropEvent(UIComponent component, Behavior behavior, String sourceId, String targetId, Object data) {
		super(component, behavior);
		this.sourceId = sourceId;
		this.targetId = targetId;
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

	public String getTargetId() {
		return targetId;
	}

    public Object getData() {
        return data;
    }
}
