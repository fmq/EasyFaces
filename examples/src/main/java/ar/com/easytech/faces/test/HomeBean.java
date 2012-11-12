package ar.com.easytech.faces.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import ar.com.easytech.faces.dashboard.utils.ChartManager;
import ar.com.easytech.faces.event.DropEvent;

@ManagedBean
public class HomeBean {

	private final static Logger logger = Logger.getLogger(HomeBean.class.toString());

	private List<String> data = new ArrayList<String>();
	private List<String> selectedRows = new ArrayList<String>();

	Map<String,Map<String, String>> dataToUpdate = new HashMap<String,Map<String,String>>();
	
	String chartData;
	String sortData;
	
	@PostConstruct
	public void init() {

		data.add("Value 1");
		data.add("Value 2");
		generateChartData();

	}
	private void generateChartData() {

		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("Uno", 200);
		data.put("Dos", 430);
		data.put("tres", 100);
		chartData = ChartManager.serializeForPie(data);
		//chartData = "\"[ { label: 'Foo', data: [ [10, 1], [17, -14], [30, 5] ] }, { label: 'Bar', data: [ [11, 13], [19, 11], [30, -7] ] } ]\"";
	}
	
	public void updateData() {
		
		JsonFactory factory = new JsonFactory(); 
	    ObjectMapper mapper = new ObjectMapper(factory); 
	    TypeReference<HashMap<String,HashMap<String, Object>>> typeRef= new TypeReference<HashMap<String,HashMap<String,Object>>>() {}; 
	    
		try {
			dataToUpdate = mapper.readValue(sortData, typeRef);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
		//
		
	}

	public void objectDropped(DropEvent event) {
		selectedRows.add(event.getSourceId());
	}

	public void objectDropped(AjaxBehaviorEvent event) {
		logger.info("Event: " + event.getBehavior());
		Object obj = event;
	}

	public void objectMoved(AjaxBehaviorEvent event) {
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

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getSortData() {
		return sortData;
	}

	public void setSortData(String sortData) {
		this.sortData = sortData;
	}
	
}
