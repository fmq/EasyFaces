package ar.com.easytech.faces.dashboard.widgets.renderer;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.dashboard.component.widgets.DataTable;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Widget", rendererType = "ar.com.easyfaces.DataTableRenderer")
public class DataTableRenderer extends BaseWidgetRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		DataTable table = (DataTable)component;

		ResponseWriter writer = context.getResponseWriter();
		String clientId = generateId(context, component, "table");
		
		// Start elements
		startWidget(context, component, table.getInstanceId() );
		//Wrapper
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId + "_wrapper", "id");
		
		String baseStyleClass = "stdtable ";
		if (table.getStyleClass() != null)
			baseStyleClass += table.getStyleClass();
		
		writer.startElement("table", null);
		writer.writeAttribute("id", clientId + "_tab", "id");
		writer.writeAttribute("class", baseStyleClass, "class");
		
		// Render headers
		renderHeader(context, component);
		// Render column group
		renderColGroup(context, component);
		//Render body
		renderData(context, component);
		// End table render
		writer.endElement("table");
		writer.endElement("div");
		endWidget(context, component);
		
	}

	private void renderHeader(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		DataTable table = (DataTable)component;
		
		writer.startElement("thead", component);
		writer.startElement("tr", component);
		
		String baseStyleClass = "";
		if (table.getColumnStyleClass() != null)
			baseStyleClass = table.getColumnStyleClass();
		
		int col = 0;
		for (String header : table.getHeaders()) {
			writer.startElement("th", component);
			if (col % 2 == 0)
				writer.writeAttribute("class", "head0 " + baseStyleClass, "class");
			else
				writer.writeAttribute("class", "head1 " + baseStyleClass, "class");
			writer.append(header);
			writer.endElement("th");
		}
		writer.endElement("tr");
		writer.endElement("thead");
	}
	
	
	private void renderColGroup(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		DataTable table = (DataTable)component;
		
		writer.startElement("colgroup", component);
		
		String baseStyleClass = "";
		if (table.getColumnStyleClass() != null)
			baseStyleClass = table.getColumnStyleClass();
		
		int col = 0;
		for (String header : table.getHeaders()) {
			writer.startElement("col", component);
			if (col++ % 2 == 0)
				writer.writeAttribute("class", "con0 " + baseStyleClass, "class");
			else
				writer.writeAttribute("class", "con1 " + baseStyleClass, "class");
			
			writer.endElement("col");
		}
		writer.endElement("colgroup");
	}
	
	private void renderData(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		DataTable table = (DataTable)component;
		writer.startElement("tbody", component);
		List<Object[]> data = (List<Object[]>)table.getData();
		for (Object[] row : data) {
			writer.startElement("tr", component);
			for (int i = 0; i < table.getHeaders().size(); i++) {
				writer.startElement("td", component);
				writer.write((String)((Object)row[i].toString()));
				writer.endElement("td");
			}
			writer.endElement("tr");
		}
		writer.endElement("tbody");
	}
}
