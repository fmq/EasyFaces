package ar.com.easytech.faces.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean
public class EasyfacesBean {

	private final static Logger logger = Logger.getLogger(EasyfacesBean.class.toString());

	private List<String> data = new ArrayList<String>();
	private String selectedValue;
	
	@PostConstruct
	public void init() {

		data.add("Value 1");
		data.add("Value 2");
	}
	
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}
	
	public void listChanged(AjaxBehaviorEvent event) {
		logger.info("Event: " + event.getBehavior());
		Object obj = event;
	}


	public List<String> autcompleteFromSource(String value) {
		
		List<String> tmpData = new ArrayList<String>();
		tmpData.add("XXA_TABLE_A");
		tmpData.add("XXA_TABLE_B");
		tmpData.add("XXA_TABLE_C");
		
		return tmpData;
	
	}
	
	public String getSelectedValue() {
		return selectedValue;
	}


	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	
}
