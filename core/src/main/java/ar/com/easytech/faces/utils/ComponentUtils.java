package ar.com.easytech.faces.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

public class ComponentUtils {

	private static Set<VisitHint> ROOT_TREE_HINTS =EnumSet.of(VisitHint.SKIP_UNRENDERED);
	
	@SuppressWarnings("unchecked")
	public static <T extends UIComponent> T getComponentById(String id) {
		return (T)FacesContext.getCurrentInstance().getViewRoot().findComponent(id);
	}
	
	public static UIForm getComponentForm(UIComponent component) {
		
		if (component == null)
			return null;
		
		UIForm form = null;
		// IF the component is a form.. just return that
		if (component instanceof UIForm)
			return (UIForm)component;
		// IF the parent is the form.. return that...
		if (component.getParent() instanceof UIForm)
			return (UIForm)component.getParent();
		
		//else.. Cycle throw the component parents to search for the form..
		UIComponent parent = component.getParent();
		
		while (parent.getParent() != null)
			if (parent.getParent() instanceof UIForm) {
				form = (UIForm) parent.getParent();
				break;
			} else
				parent = parent.getParent();
		
		return form;
		
	}
	
	public static List<UIForm> getAllFormsForView() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		final List<UIForm> forms = new ArrayList<UIForm>();
		
		root.visitTree(VisitContext.createVisitContext(context, null, ROOT_TREE_HINTS), new VisitCallback() {
			public VisitResult visit(VisitContext context, UIComponent component) {
				if (component instanceof UIForm) {
					forms.add((UIForm) component);
		        }
				
				return VisitResult.ACCEPT;
			}
		});
		
		return forms;
	}
}
