package ar.com.easytech.faces.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.DragDropEvent;

import ar.com.easytech.faces.event.DropEvent;

@ManagedBean
public class HomeBean {


	private final static Logger logger = Logger.getLogger(HomeBean.class.toString());
	
	private List<String> data = new ArrayList<String>();
	private List<String> selectedRows = new ArrayList<String>();
	
	@PostConstruct
	public void init() {
		
		data.add("Value 1");
		data.add("Value 2");
	}

	 public void onCarDrop(DragDropEvent ddEvent) {  
	        Object obj = ddEvent.getData();  
	    }  
	
	public void objectDropped(DropEvent event) {
		selectedRows.add(event.getSourceId());
	}
	
	 public void objectDropped(AjaxBehaviorEvent event) {
		logger.info("Event: " + event.getBehavior());
		Object obj = event;
	 }
		
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public List<String> getSelectedRows() {
		return selectedRows;
	}

	public void setSelectedRows(List<String> selectedRows) {
		this.selectedRows = selectedRows;
	}
	
	
}
