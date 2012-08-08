package ar.com.easytech.faces.component.dnd;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

import com.sun.faces.facelets.tag.MethodRule;

import ar.com.easytech.faces.event.DropEvent;

public class DroppableHandler extends ComponentHandler {

	public DroppableHandler(ComponentConfig config) {
		super(config);
	}

	@SuppressWarnings("unchecked")
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);
		Class[] eventClasses = new Class[] { DropEvent.class };

		metaRuleset.addRule(new MethodRule("dropListener", null, eventClasses));

		return metaRuleset;
	}

}
