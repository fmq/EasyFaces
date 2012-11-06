package ar.com.easytech.faces.dashboard.listeners;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

public class DashboardChangeListener implements AjaxBehaviorListener {

	private MethodExpression listener;
	
	public DashboardChangeListener (MethodExpression listener) {
        this.listener = listener;
    }
	
	public void processAjaxBehavior(AjaxBehaviorEvent event)
			throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
        final ELContext elContext = context.getELContext();
        
        try{
            listener.invoke(elContext, new Object[]{});
        } catch (MethodNotFoundException mnfe) {
            MethodExpression argListener = context.getApplication().getExpressionFactory().
                        createMethodExpression(elContext, listener.getExpressionString(), null, new Class[]{event.getClass()});
            
            argListener.invoke(elContext, new Object[]{event});
        }
		
	}

}
