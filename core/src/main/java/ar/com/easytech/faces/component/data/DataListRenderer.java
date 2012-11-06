package ar.com.easytech.faces.component.data;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.ScriptUtils;
import ar.com.easytech.faces.utils.StringUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Data", 
			   rendererType = "ar.com.easyfaces.DataListRenderer")
public class DataListRenderer extends BaseRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		
		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}
	
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException{
		DataList datalist = (DataList)component;
		ResponseWriter writer = context.getResponseWriter();
		
		String clientId = datalist.getClientId(context);
        String style = datalist.getStyle();
        String styleClass = datalist.getStyleClass();
        String itemStyleClass =  datalist.getItemStyleClass();
        String sortable = datalist.getSortable();
        String type = "ul";
        
        if (datalist.getType() != null && datalist.getType().equals("ordered"))
        		type = "ol";
        
        if (style != null)  writer.writeAttribute("style", style, null);
        if (styleClass != null)  writer.writeAttribute("class", styleClass, null);
        
        writer.startElement(type, null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("class", styleClass, null);
        writer.writeAttribute("style", style, null);
        encodeList(context, datalist, itemStyleClass);
        writer.endElement(type);
        
        // If sortable then we add the sortable script.
        if (sortable != null && sortable.equalsIgnoreCase("true")) {
        	ScriptUtils.startScript(writer, clientId);
        	writer.write("$(function() {");
        	
        	writer.write("$(EasyFaces.escapeId('" + clientId + "')).sortable({");
        	
        	// Record the start position of the item
        	writer.write("start: function(event, ui) { ");
        	writer.write("    var startPosition = ui.item.index(); ");
        	writer.write("    ui.item.data('startPosition', startPosition);");
        	writer.write("},");
        	// Update event that will be triggered with dropped data
        	writer.write("update: function (event, ui) { ");
        	
        	writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("update"))
					  .addExecute(StringUtils.addSingleQuotes(datalist.getClientId()))
					  .addSource(StringUtils.addSingleQuotes(datalist.getClientId()))
					  .addOther("sourceId", "ui.item.attr('id')")
					  .addOther("dataValue", "ui.item.attr('data-value')")
					  .addOther("startPosition", "ui.item.data('startPosition')")
					  .addOther("dropedPosition", "ui.item.index()")
					  .getAjaxCall());
        	writer.write(" } ");
        	writer.write("});");
        	writer.write("});return false;");
        	ScriptUtils.endScript(writer);
        	
        }
        
	}
	
	@SuppressWarnings("rawtypes")
	private void encodeList(FacesContext context, DataList dataList, String itemStyleClass) throws IOException {
		
		List data = convertDataToList(dataList);
		for (Object item : data) {
				processItem(context, dataList, itemStyleClass, item);
		}
	} 
		
	protected void processItem(FacesContext context, DataList datalist, String itemStyleClass, Object item) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		//Put the item in the 'var' for looping in the list
		context.getExternalContext().getRequestMap().put(datalist.getVar(), item);
		// Add the li item
		writer.startElement("li", null);
		if (itemStyleClass != null) writer.writeAttribute("class", itemStyleClass, null);
		
		if (datalist.getItemValue() != null)
			writer.writeAttribute("data-value", datalist.getItemValue(), null);
		
		doRenderChildren(context, datalist);
		writer.endElement("li");
	}
	
	@Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

	
}
