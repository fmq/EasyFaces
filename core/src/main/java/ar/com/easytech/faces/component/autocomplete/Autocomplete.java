package ar.com.easytech.faces.component.autocomplete;

import static com.sun.faces.renderkit.Attribute.attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

import ar.com.easytech.faces.event.AutocompleteSearchEvent;
import ar.com.easytech.faces.utils.DataUtils;

@FacesComponent(Autocomplete.COMPONENT_TYPE)
@ResourceDependencies({
		@ResourceDependency(library = "javax.faces", name = "jsf.js"),
		@ResourceDependency(library = "easyfaces", name = "easyfaces.js"),
		@ResourceDependency(library = "easyfaces", name = "autocomplete.js"),
		@ResourceDependency(library = "css", name = "core.css"),})
public class Autocomplete extends UIInput implements ClientBehaviorHolder {

	private static final Logger logger = Logger.getLogger(Autocomplete.class.getSimpleName());

	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Input";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.AutocompleteRenderer";
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Autocomplete";
	//Add custom events
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(
			Arrays.asList("blur","change","valueChange","click","dblclick","focus","keydown","keypress","keyup","mousedown","mousemove","mouseout","mouseover","mouseup","select"));
	
	String[] JS_ACTIONS = {"onclick", "ondblclick", "onmousedown", "onmouseup", "onmouseover", "onmousemove", "onmouseout", "onkeypress", "onkeydown"};
	String[] ptAttributes = {"accesskey","alt","dir","lang","maxlength","size","tabindex","title"};
	// List with data returned from method from BB to use as source
	private List<Object> sourceData = new ArrayList<Object>();
	private int x, y, width;
	
	public Autocomplete() {
		super();
		setRendererType(RENDERER_TYPE);
	}
	
	@Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }
	
	@Override
	public String getDefaultEventName() {
		return "change";
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

	public String getData() {
		return (String) getStateHelper().eval(PropertyKeys.data);
	}

	public void setData(String data) {
		getStateHelper().put(PropertyKeys.data, data);
	}

	public String getLabel() {
		return (String) getStateHelper().eval(PropertyKeys.label);
	}

	public void setLabel(String label) {
		getStateHelper().put(PropertyKeys.label, label);
	}

	public String getVar() {
		return (String) getStateHelper().eval(PropertyKeys.var);
	}

	public void setVar(String var) {
		getStateHelper().put(PropertyKeys.var, var);
	}
	
	public String getItemValue() {
		return (String) getStateHelper().eval(PropertyKeys.itemValue);
	}

	public void setItemValue(String itemValue) {
		getStateHelper().put(PropertyKeys.itemValue, itemValue);
	}

	public String getNoDataLabel() {
		return (String) getStateHelper().eval(PropertyKeys.noDataLabel);
	}

	public void setNoDataLabel(String noDataLabel) {
		getStateHelper().put(PropertyKeys.noDataLabel, noDataLabel);
	}
	protected enum PropertyKeys {

		minLenght, delay, position, dataSource, style, styleClass, data, var,itemValue, label, noDataLabel;

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

	@Override
	public void broadcast(javax.faces.event.FacesEvent event) throws javax.faces.event.AbortProcessingException {
		
		super.broadcast(event);

		if (event instanceof AutocompleteSearchEvent) {
			Object dataObject = null;
			AutocompleteSearchEvent evt = (AutocompleteSearchEvent)event;
			this.x = evt.getX();
			this.y = evt.getY();
			this.width = evt.getWidth();
			
			MethodExpression dataSource = getDataSource();

			if (dataSource != null) {
				try {
					dataObject = dataSource.invoke(FacesContext.getCurrentInstance().getELContext(), new Object[] { evt.getSearchStr() });
					sourceData = DataUtils.convertToList(dataObject);
				} catch (MethodNotFoundException e) {
					logger.log(Level.INFO, "Method not found: {0}", dataSource.getExpressionString());
				}

				FacesContext.getCurrentInstance().renderResponse();
			}
		}

	}
	
	public List<Object> getSourceData() {
		return sourceData;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}

	public String[] getJS_ACTIONS() {
		return JS_ACTIONS;
	}

	public void setJS_ACTIONS(String[] jS_ACTIONS) {
		JS_ACTIONS = jS_ACTIONS;
	}

	public String[] getPtAttributes() {
		return ptAttributes;
	}

	public void setPtAttributes(String[] ptAttributes) {
		this.ptAttributes = ptAttributes;
	}
	
	
}
