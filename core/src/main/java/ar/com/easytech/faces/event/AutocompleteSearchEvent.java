package ar.com.easytech.faces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class AutocompleteSearchEvent extends FacesEvent {

	private static final long serialVersionUID = 5970173012951136750L;

	private String searchStr;
	
	public AutocompleteSearchEvent(UIComponent component, String searchStr) {
		super(component);
		this.searchStr = searchStr;
	}

    @Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
        return (faceslistener instanceof AjaxBehaviorListener);
	}

	@Override
	public void processListener(FacesListener faceslistener) {
		throw new UnsupportedOperationException();
	}

	public String getSearchStr() {
		return searchStr;
	}

}
