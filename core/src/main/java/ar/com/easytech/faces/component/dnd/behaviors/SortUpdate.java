package ar.com.easytech.faces.component.dnd.behaviors;

import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;

public class SortUpdate extends ClientBehaviorBase {

	@Override
	public String getScript(ClientBehaviorContext context) { 
		return "mojarra.ab('frm:j_idt26',event,'update','@form','@form')";
	}
}
