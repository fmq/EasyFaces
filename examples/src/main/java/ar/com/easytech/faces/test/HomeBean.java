package ar.com.easytech.faces.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import ar.com.easytech.faces.event.DragDropEvent;

@ManagedBean
public class HomeBean {


	private List<String> data = new ArrayList<String>();
	private List<String> selectedRows = new ArrayList<String>();
	
	@PostConstruct
	public void init() {
		
		data.add("Value 1");
		data.add("Value 2");
	}

	public void objectDropped(DragDropEvent event) {
		selectedRows.add(event.getSourceId());
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
