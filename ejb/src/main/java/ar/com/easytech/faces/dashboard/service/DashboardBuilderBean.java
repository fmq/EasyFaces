package ar.com.easytech.faces.dashboard.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import ar.com.easytech.faces.dashboard.model.ChartSeries;
import ar.com.easytech.faces.dashboard.model.DashboardColumnLayout;
import ar.com.easytech.faces.dashboard.model.DashboardDefinition;
import ar.com.easytech.faces.dashboard.model.DashboardWidgetInstance;
import ar.com.easytech.faces.dashboard.model.Widget;
import ar.com.easytech.faces.dashboard.model.WidgetInstance;
import ar.com.easytech.faces.dashboard.utils.ChartManager;

@Stateless
@LocalBean
@Named
public class DashboardBuilderBean implements Serializable {
	
	private static final long serialVersionUID = -3625071554648966507L;

	@PersistenceContext (name="DashboardPU")
	EntityManager em;

	private static final String SERVICE_URL = "http://localhost:8080/crm/ws/dashboard/ejecutar/";

	public DashboardDefinition getDashboardForUser(String userId) {

		// Cargo el dashboard para el usuario
		DashboardDefinition dashboard = (DashboardDefinition) em
				.createNamedQuery("ef_dashboard_for_user")
				.setParameter("userId", userId).getSingleResult();

		if (dashboard == null)
			return new DashboardDefinition();

		return dashboard;
	}

	public List<WidgetInstance> getWidgetsForColumn(DashboardColumnLayout layout) {

		List<WidgetInstance> widgetinstances = new ArrayList<WidgetInstance>();
		for (DashboardWidgetInstance widgetInstance : layout
				.getDashboardWidgets()) {
			widgetinstances.add(new WidgetInstance(widgetInstance.getId(),
					layout.getId(), widgetInstance.getWidget()));
		}

		return widgetinstances;
	}

	public String getWidgetData(Widget widget) {

		// POr ahora recibe un sql y devuelve un Map con los datos que puede ser
		// si es un PIE Map<Label, Value> por lo que el query tiene 2 campos
		// Si es otro tipo de grafico Map<String,String[]> (representa [x,y])
		// por lo que el query tiene
		// que tener 3 camppos

		// IF Pie
		Logger.getAnonymousLogger().info(widget.getType().toString());
		switch (widget.getType()) {
		case PIE:
			String response = executePost(widget.getSql());
			return ChartManager.serializeForPie(parseResponseToMap(response));
		case CHART:
			// If we use a chart we can have more than one series
			if (widget.getSeries().size() > 0) {
				StringBuilder data = new StringBuilder();
				// We need to process all the series and add the data

				// var data = [{ data: d1, label: "Pressure", color: "#333" }];

				boolean first = true;
				for (ChartSeries series : widget.getSeries()) {

					if (!first)
						data.append(",");
					data.append("{");
					data.append("data: ");
					data.append(ChartManager.serialize(executePost(series.getSql())));
					data.append(", label: '" + series.getName() + "'");
					data.append("}");

					first = false;
				}
				return data.toString();
			} else
				return ChartManager.serialize(executePost(widget.getSql()));
		default:
			break;
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getWidgetDataAsList(Widget widget) {
		switch (widget.getType()) {
		case TABLE:
			String response = executePost(widget.getSql());
			return parseResponseToList(response);

		default:
			break;
		}

		return null;
	}
	
	public void updateModel(long dashboardId, String sortData) {
		// First we get the data from the jSon object..
		// Map of columns and each column has a map with
		Map<Long, Map<Long, Integer>> dataMap = new HashMap<Long, Map<Long, Integer>>();
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<HashMap<Long, HashMap<Long, Integer>>> typeRef = new TypeReference<HashMap<Long, HashMap<Long, Integer>>>() {
		};

		try {
			dataMap = mapper.readValue(sortData, typeRef);
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

		// Get the layouts for the dashboard
		DashboardDefinition dash = em.find(DashboardDefinition.class,
				dashboardId);

		Map<Long, DashboardColumnLayout> layouts = new HashMap<Long, DashboardColumnLayout>(
				dash.getColumnLayout().size());
		for (DashboardColumnLayout layout : dash.getColumnLayout()) {
			layouts.put(layout.getId(), layout);
		}

		// Now we update the database
		for (long layoutId : dataMap.keySet()) {
			boolean isLayoutDirty = false;
			// Get the layout
			DashboardColumnLayout currentLayout = em.find(
					DashboardColumnLayout.class, layoutId);
			// Each layout has now widgets
			// we have to check if the widget is in the same layout
			// and what position and update de layout accordingly
			Map<Long, Integer> map = dataMap.get(layoutId);
			for (long id : map.keySet()) {
				// Find the widget instance and
				DashboardWidgetInstance wi = em.find(
						DashboardWidgetInstance.class, id);

				// Loop through the layout to find the current instance of the
				// widget
				DashboardColumnLayout currentWidgetLayout = new DashboardColumnLayout();

				for (DashboardColumnLayout layout : dash.getColumnLayout()) {
					if (layout.getDashboardWidgets().contains(wi))
						currentWidgetLayout = layout;
				}
				// If the current layout is not the same as the one passsed from
				// the page then we need to move the instance.
				if (currentWidgetLayout.getId() != layoutId) {
					// Remove the widget from the layout
					currentWidgetLayout.getDashboardWidgets().remove(wi);
					// Persist the layout
					em.persist(currentWidgetLayout);
					// Add the widget to the corrent layout
					currentLayout.getDashboardWidgets().add(wi);
					isLayoutDirty = true;
				}
				// Set the position
				if (wi.getPosition() != map.get(id)) {
					wi.setPosition(map.get(id));
					em.persist(wi);
				}
			}
			if (isLayoutDirty)
				em.persist(currentLayout);
		}
	}

	private String executePost(String sql) {
		Logger.getLogger("DashboardBuilder").info(sql);
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(SERVICE_URL);
		String response = "";
		method.addParameter("query", sql);
		try {
			int returnCode = client.executeMethod(method);
			if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
				System.err.println("The Post method is not implemented by this URI");
				// still consume the response body
			}
			response = method.getResponseBodyAsString();
		} catch (Exception e) {
			Logger.getAnonymousLogger().warning("Error al ejecutar SQL");
		} finally{
			method.releaseConnection();
		}
		Logger.getAnonymousLogger().info(response);
		response.replaceAll(String.valueOf('"'), "'");
		return response;
	}
	
	private Map<Object, Object> parseResponseToMap(String response){
		// First we get the data from the jSon object..
		// Map of columns and each column has a map with
		Map<Object, Object> responseMap = new HashMap<Object, Object>();
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<HashMap<Object, Object>> typeRef = new TypeReference<HashMap<Object, Object>>() {
		};
	
		try {
			responseMap = mapper.readValue(response, typeRef);
		}catch (Exception e){
		}
		return responseMap;
	}
	
	private List<Object[]> parseResponseToList(String response){
		List<Object[]> responseList = new LinkedList<Object[]>();
		JsonFactory factory = new JsonFactory();
		ObjectMapper mapper = new ObjectMapper(factory);
		TypeReference<LinkedList<Object>> typeRef = new TypeReference<LinkedList<Object>>() {
		};
	
		try {
			responseList = mapper.readValue(response, typeRef);
		}catch (Exception e){
		}
		return responseList;
	}
	
}
