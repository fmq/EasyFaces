package ar.com.easytech.faces.component.autocomplete;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.component.html.HtmlInputText;

@FacesComponent(Autocomplete.COMPONENT_TYPE)
@ResourceDependencies({
		@ResourceDependency(library = "js", name = "jquery.js", target = "head"),
		@ResourceDependency(library = "js", name = "jquery-ui.js", target = "head"),
		@ResourceDependency(library = "javax.faces", name = "jsf.js"),
		@ResourceDependency(library = "easyfaces", name = "easyfaces.js") })
public class Autocomplete extends HtmlInputText implements ClientBehaviorHolder {
	
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Input";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.AutocompleteRenderer";
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Autocomplete";
	
	public Autocomplete() {
		setRendererType(RENDERER_TYPE);
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

	public Integer getMinLength() {
		return (Integer) getStateHelper().eval(PropertyKeys.minLenght);
	}
	
	public void setMinLength(Integer minLenght) {
		getStateHelper().put(PropertyKeys.minLenght, minLenght);
	}

	public Integer getDelay() {
		return (Integer) getStateHelper().eval(PropertyKeys.delay);
	}

	public void setDelay(Integer delay) {
		getStateHelper().put(PropertyKeys.delay, delay);
	}
	
	public String getPosition() {
		return (String) getStateHelper().eval(PropertyKeys.position);
	}

	public void setPosition(String position) {
		getStateHelper().put(PropertyKeys.position, position);
	}

	public MethodExpression getDataSource() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.dataSource);
	}

	public void setDataSource(MethodExpression dataSource) {
		getStateHelper().put(PropertyKeys.dataSource, dataSource);
	}
	
	protected enum PropertyKeys {

		minLenght, delay, position, dataSource, style, styleClass;

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
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
}
