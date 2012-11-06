package ar.com.easytech.faces.component.dnd;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;

@FacesComponent(DragPanel.COMPONENT_TYPE)
public class DragPanel extends UIComponentBase implements ValueHolder {

	public static final String COMPONENT_TYPE = "ar.com.easyfaces.DragPanel";
	public static final String DEFAULT_RENDERER_TYPE = "ar.com.easyfaces.DragPanelRenderer";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";

	private Converter converter;
	 
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public Object getLocalValue() {
		 return getStateHelper().get(PropertyKeys.value);
	}

	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value);
	}

	public void setValue(Object value) {
		getStateHelper().put(PropertyKeys.value, value);
		
	}

	public Converter getConverter() {
		if (this.converter != null) 
			  return (this.converter);
			 
		return (Converter) getStateHelper().eval(PropertyKeys.converter);
	}

	public void setConverter(Converter converter) {
		super.clearInitialState();
		this.converter = converter;
	}
	
	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style);
	}

	public void setStyle(String style) {
		getStateHelper().put(PropertyKeys.style, style);
	}
	
	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
	
	protected enum PropertyKeys {
		value, converter, style, styleClass;
		
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
