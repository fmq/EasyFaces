package ar.com.easytech.faces.test;

import java.io.Serializable;
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
	
	private List<SelectItem> tmpData = new ArrayList<SelectItem>();

	
	@PostConstruct
	public void init() {

		data.add("Value 1");
		data.add("Value 2");
		
		
		tmpData.add(new SelectItem(1,"XXA_TABLE_A"));
		tmpData.add(new SelectItem(2,"XXA_TABLE_B"));
		tmpData.add(new SelectItem(3,"XXA_TABLE_C"));
		tmpData.add(new SelectItem(4,"BBC_TABLE_A"));
		tmpData.add(new SelectItem(5,"BBC_TABLE_B"));
		tmpData.add(new SelectItem(6,"BBC_TABLE_C"));

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


	public List autcompleteFromSource(String value) {
		
		return tmpData;
	
	}
	
	public void sendData() {
		System.out.println("Selected:" + getSelectedValue());
	}
	
	public String getSelectedValue() {
		return selectedValue;
	}


	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public class SelectItem implements Serializable {
		
		private static final long serialVersionUID = 6664143860508117091L;
		
		public SelectItem(long id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		
		long id;
		String name;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}
	
}
