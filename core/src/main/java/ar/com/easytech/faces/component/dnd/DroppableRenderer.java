package ar.com.easytech.faces.component.dnd;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import ar.com.easytech.faces.renderer.BaseRenderer;
import ar.com.easytech.faces.utils.AjaxRequest;
import ar.com.easytech.faces.utils.StringUtils;

@FacesRenderer(componentFamily = "ar.com.easyfaces.Output", rendererType = "ar.com.easyfaces.DroppableRenderer")
public class DroppableRenderer extends BaseRenderer {
	
	@Override
	public void decode(FacesContext context, UIComponent component) {

		if (context == null ) {
			throw new NullPointerException();
		}
		 
		decodeClientBehaviors(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

		Droppable droppable = (Droppable)component;

		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId(context);
		UIComponent targetComponent = droppable.findComponent(droppable.getFor());
		
        if(targetComponent == null)
            throw new FacesException("Cannot find component \"" + droppable.getFor());
        
        String target = targetComponent.getClientId();

        writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("$(function() {");
		writer.write("$( '#" + target.replace(":", "\\\\:") + "').droppable({");

		if (droppable.getActiveClass() != null) writer.write(" activeClass: '" + droppable.getActiveClass() + "',");
		if (droppable.getHoverClass() != null) writer.write(" hoverClass: '" + droppable.getHoverClass() + "',");
		if (droppable.getTolerance() != null) writer.write(" tolerance: '" + droppable.getTolerance() + "',");
		
		// If we are sorting we need to exclude sortable from droppable so we don't
		// duplicate items.
		if (droppable.getSortable() != null && droppable.getSortable().equals("true")) {
			if (droppable.getAccept() != null) 
				writer.write(" accept: ':not(.ui-sortable-helper), " + droppable.getAccept() + "',");
			else
				writer.write("accept: ':not(.ui-sortable-helper)',");
		} else
			if (droppable.getAccept() != null) writer.write(" accept: '" + droppable.getAccept() + "',");
		
		//Ajax call on drop...This is allways created.
		writer.write(" drop: function( event, ui ) { ");
		writer.write(" $( this ).find( '.placeholder' ).remove(); ");
		if (droppable.getTargetType() != null &&  droppable.getTargetType().equalsIgnoreCase("list")) 
			writer.write("$( '<li></li>' ).text( ui.draggable.text() ).attr('id',ui.draggable.attr('id')).appendTo( this );");
		if (droppable.getOnDrop() != null) writer.write(droppable.getOnDrop());
		
		writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("drop"))
									  .addExecute(StringUtils.addSingleQuotes(droppable.getClientId()))
									  .addSource(StringUtils.addSingleQuotes(droppable.getClientId()))
									  .addOther("sourceId", "EasyFaces.getDragSource(ui.draggable)")
									  .addOther("targetId", StringUtils.addSingleQuotes(target)).getAjaxCall());
		
		writer.write(" } ");
		// If dropOut event is defined
		if (droppable.getOnDropOut() != null) {
			writer.write(" dropout: function( event, ui ) { ");
			writer.write(droppable.getOnDropOut());
			writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("dropout"))
					  .addExecute(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addSource(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addOther("sourceId", "ui.draggable.attr('id')")
					  .addOther("targetId", StringUtils.addSingleQuotes(target)).getAjaxCall());
			writer.write(" } ");
		}
		
		encodeClientBehaviors(context, droppable);
		
		// If sortable
		if (droppable.getSortable() != null && droppable.getSortable().equals("true")) {
			writer.write("}).sortable({");
			//Add an update event to notify of sorting update
			writer.write(" update: function( event, ui ) { ");
			writer.write(new AjaxRequest().addEvent(StringUtils.addSingleQuotes("update"))
					  .addExecute(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addSource(StringUtils.addSingleQuotes(droppable.getClientId()))
					  .addOther("sourceId", "ui.item.attr('id')")
					  .addOther("dropedPosition", "ui.item.index()").getAjaxCall());
			writer.write(" } ");
			writer.write("});");
			
		} else
			writer.write("});");
		
		writer.write("});");
		writer.endElement("script");
	}

	
}
