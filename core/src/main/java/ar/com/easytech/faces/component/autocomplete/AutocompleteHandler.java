package ar.com.easytech.faces.component.autocomplete;

import java.util.List;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

import com.sun.faces.facelets.tag.MethodRule;

public class AutocompleteHandler extends ComponentHandler {

	public AutocompleteHandler(ComponentConfig config) {
		super(config);
	}

	@SuppressWarnings("rawtypes")
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(new MethodRule("dataSource", List.class, new Class[] { String.class }));
		return metaRuleset;
	}

}
