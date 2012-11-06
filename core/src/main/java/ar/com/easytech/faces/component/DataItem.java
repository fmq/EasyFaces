package ar.com.easytech.faces.component;

import javax.faces.component.UIData;

public abstract class DataItem extends UIData {

	
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Data";
	
	public DataItem() {
		
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	// Attributes
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
	
	public String getItemValue() {
		return (String) getStateHelper().eval(PropertyKeys.itemValue);
	}

	public void setItemValue(String itemValue) {
		getStateHelper().put(PropertyKeys.itemValue, itemValue);
	}
	
	protected enum PropertyKeys {
		style, styleClass, itemStyleClass, hoverClass, itemValue;
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
