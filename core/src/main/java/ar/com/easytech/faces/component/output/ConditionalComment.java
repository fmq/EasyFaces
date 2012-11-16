package ar.com.easytech.faces.component.output;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/*
 * Inspired by @balusc
 * Simplified post omnifaces. (can't use omnifaces since it clashes with parts of base project.)
 */
@FacesComponent(ConditionalComment.COMPONENT_TYPE)
public class ConditionalComment extends UIComponentBase {

	public static final String COMPONENT_TYPE = "ar.com.easyfaces.ConditionalComment";
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Output";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.ConditionalCommentRenderer";

	public ConditionalComment() {
		setRendererType(RENDERER_TYPE);
	}
	public String getTest() {
		return (String) getStateHelper().eval(PropertyKeys.test);
	}

	public void setTest(String test) {
		getStateHelper().put(PropertyKeys.test, test);
	}
	
	protected enum PropertyKeys {

		test;
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
	public boolean getRendersChildren() {
		return true;
	}

}
