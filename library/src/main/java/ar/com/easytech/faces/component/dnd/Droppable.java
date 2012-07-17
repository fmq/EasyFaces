/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ar.com.easytech.faces.component.dnd;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import ar.com.easytech.faces.event.DragDropEvent;

@FacesComponent(Droppable.COMPONENT_TYPE)
public class Droppable extends UIComponentBase implements ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "ar.com.easytech.Droppable";
	public static final String DEFAULT_RENDERER_TYPE = "ar.com.easytech.DroppableRenderer";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";

	private final static String DEFAULT_EVENT = "drop";
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT));

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	// Property: for
	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forVal);
	}

	public void setFor(String forParam) {
		getStateHelper().put(PropertyKeys.forVal, forParam);
	}

	// Property: droppableSelector
	public String getDroppableSelector() {
		return (String) getStateHelper().eval(PropertyKeys.droppableSelector);
	}

	public void setDroppableSelector(String droppableSelector) {
		getStateHelper().put(PropertyKeys.droppableSelector, droppableSelector);
	}

	// Property: activeClass
	public String getActiveClass() {
		return (String) getStateHelper().eval(PropertyKeys.activeClass);
	}

	public void setActiveClass(String activeClass) {
		getStateHelper().put(PropertyKeys.activeClass, activeClass);
	}

	// Property: hoverClass
	public String getHoverClass() {
		return (String) getStateHelper().eval(PropertyKeys.hoverClass);
	}

	public void setHoverClass(String hoverClass) {
		getStateHelper().put(PropertyKeys.hoverClass, hoverClass);
	}

	// Property: accept
	public String getAccept() {
		return (String) getStateHelper().eval(PropertyKeys.accept);
	}

	public void setAccept(String accept) {
		getStateHelper().put(PropertyKeys.accept, accept);
	}

	// Property: tolerance
	public String getTolerance() {
		return (String) getStateHelper().eval(PropertyKeys.tolerance);
	}

	public void setTolerance(String tolerance) {
		getStateHelper().put(PropertyKeys.tolerance, tolerance);
	}

	// Property: source
	public String getSource() {
		return (String) getStateHelper().eval(PropertyKeys.source);
	}

	public void setSource(String source) {
		getStateHelper().put(PropertyKeys.source, source);
	}
	
	protected enum PropertyKeys {
		forVal("for"), droppableSelector, activeClass, hoverClass, accept, tolerance, source;
		String c;

		PropertyKeys() {
		}

		// Constructor needed by "for" property
		PropertyKeys(String c) {
			this.c = c;
		}

		public String toString() {
			return ((this.c != null) ? this.c : super.toString());
		}
	}

	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		
			Map<String, String> params = context.getExternalContext()
					.getRequestParameterMap();
			String eventName = params.get("javax.faces.behavior.event");
			String clientId = getClientId(context);

			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if (eventName.equals("drop")) {
				String dragId = params.get("sourceId");
				String dropId = params.get("targetId");
				DragDropEvent dndEvent = null;
				String datasourceId = getSource();

				if (datasourceId != null) {
					UIData datasource = (UIData) findComponent(datasourceId);
					String[] idTokens = dragId.split(String
							.valueOf(UINamingContainer
									.getSeparatorChar(context)));
					int rowIndex = Integer
							.parseInt(idTokens[idTokens.length - 2]);
					datasource.setRowIndex(rowIndex);
					Object data = datasource.getRowData();
					datasource.setRowIndex(-1);

					dndEvent = new DragDropEvent(this,
							behaviorEvent.getBehavior(), dragId, dropId, data);
				} else {
					dndEvent = new DragDropEvent(this,
							behaviorEvent.getBehavior(), dragId, dropId);
				}

				super.queueEvent(dndEvent);
			}

		
	}

	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}

	@Override
	public String getDefaultEventName() {
		return DEFAULT_EVENT;
	}

	@Override
	public void decode(FacesContext context) {
		
		 if (context == null ) {
		      throw new NullPointerException();
		    }
		    
		 	Map<String, List<ClientBehavior>> behaviors = getClientBehaviors();
	        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
	        String behaviorEvent = params.get("javax.faces.behavior.event");

	        if(null != behaviorEvent) {
	            List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);

	            if(behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
	               String behaviorSource = params.get("javax.faces.source");
	               String clientId = getClientId();

	               if(behaviorSource != null && clientId.startsWith(behaviorSource)) {
	                   for(ClientBehavior behavior: behaviorsForEvent) {
	                	   AjaxBehaviorEvent event = new AjaxBehaviorEvent(this, behavior);
	           		    queueEvent(event);
	                   }
	               }
	            }
	        }
		 	
		 	
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException {
		
		ClientBehaviorContext behaviorContext =
				  ClientBehaviorContext.createClientBehaviorContext(context,this, DEFAULT_EVENT, getClientId(context), null);
		
		ResponseWriter writer = context.getResponseWriter();
		String clientId = getClientId(context);
		UIComponent targetComponent = findComponent(getFor());
        if(targetComponent == null)
            throw new FacesException("Cannot find component \"" + getFor());
        String target = targetComponent.getClientId();

        writer.startElement("script", null);
		writer.writeAttribute("id", clientId + "_s", null);
		writer.writeAttribute("type", "text/javascript", null);
		writer.write("$(function() {");
		writer.write("$( '#" + target.replace(":", "\\\\:") + "').droppable({");
		
		if (getActiveClass() != null) writer.write(" activeClass: '" + getActiveClass() + "',");
		if (getHoverClass() != null) writer.write(" hoverClass: '" + getHoverClass() + "',");
		if (getAccept() != null) writer.write(" accept: '" + getAccept() + "',");
		if (getTolerance() != null) writer.write(" tolerance: '" + getTolerance() + "',");
		
		//Ajax call on drop...
		writer.write(" drop: function( event, ui ) { ");
		writer.write(" $( this ).find( '.placeholder' ).remove(); ");
		writer.write(" jsf.ajax.request(this,event,{execute: '" + getClientId() +"', 'javax.faces.behavior.event': 'drop', sourceId: ui.draggable.attr('id') , targetId: $(this).attr('id')}); ");
		writer.write(" } ");
		
		Map<String,List<ClientBehavior>> behaviors = getClientBehaviors();
		if (behaviors.containsKey(DEFAULT_EVENT) ) {
			String drop = behaviors.get(DEFAULT_EVENT).get(0).getScript(behaviorContext);
			writer.writeAttribute("drop:", "alert('Hello!');", null);
		}
		
		writer.write("});");
		writer.write("});");
		writer.endElement("script");
	}

	// Private
	
	private boolean isRequestSource(FacesContext context) {
		return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get("javax.faces.source"));
	}
}
