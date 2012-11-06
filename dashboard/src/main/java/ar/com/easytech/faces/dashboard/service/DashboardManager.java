package ar.com.easytech.faces.dashboard.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@Named
@ViewScoped
public class DashboardManager implements Serializable {

	private static final long serialVersionUID = 4949225441948805291L;
	private final static Logger logger = Logger.getLogger(DashboardManager.class.toString()); 

	@EJB DashboardBuilderBean builder; 
	
	
	
}
