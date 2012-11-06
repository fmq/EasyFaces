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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import com.sun.faces.facelets.component.UIRepeat;

import ar.com.easytech.faces.event.DropEvent;

@FacesComponent(Droppable.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="javax.faces", name="jsf.js"),
	@ResourceDependency(library="easyfaces", name="easyfaces.js")
})
public class Droppable extends UIComponentBase implements ClientBehaviorHolder {
	
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Droppable";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.DroppableRenderer";
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Output";

	public final static String DEFAULT_EVENT = "drop";
	
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT,"dropout","dropover","update"));

	public Droppable() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getFor() {
		return (String) getStateHelper().eval(PropertyKeys.forVal);
	}

	public void setFor(String forParam) {
		getStateHelper().put(PropertyKeys.forVal, forParam);
	}

	public String getDroppableSelector() {
		return (String) getStateHelper().eval(PropertyKeys.droppableSelector);
	}

	public void setDroppableSelector(String droppableSelector) {
		getStateHelper().put(PropertyKeys.droppableSelector, droppableSelector);
	}

	public String getActiveClass() {
		return (String) getStateHelper().eval(PropertyKeys.activeClass);
	}

	public void setActiveClass(String activeClass) {
		getStateHelper().put(PropertyKeys.activeClass, activeClass);
	}

	public String getHoverClass() {
		return (String) getStateHelper().eval(PropertyKeys.hoverClass);
	}

	public void setHoverClass(String hoverClass) {
		getStateHelper().put(PropertyKeys.hoverClass, hoverClass);
	}

	public String getAccept() {
		return (String) getStateHelper().eval(PropertyKeys.accept);
	}

	public void setAccept(String accept) {
		getStateHelper().put(PropertyKeys.accept, accept);
	}

	public String getTolerance() {
		return (String) getStateHelper().eval(PropertyKeys.tolerance);
	}

	public void setTolerance(String tolerance) {
		getStateHelper().put(PropertyKeys.tolerance, tolerance);
	}

	public String getDataSource() {
		return (String) getStateHelper().eval(PropertyKeys.dataSource);
	}

	public void setDataSource(String dataSource) {
		getStateHelper().put(PropertyKeys.dataSource, dataSource);
	}
	
	public String getTargetType() {
		return (String) getStateHelper().eval(PropertyKeys.targetType);
	}

	public void setTargetType(String targetType) {
		getStateHelper().put(PropertyKeys.targetType, targetType);
	}

	public String getOnDrop() {
		return (String) getStateHelper().eval(PropertyKeys.onDrop);
	}

	public void setOnDrop(String onDrop) {
		getStateHelper().put(PropertyKeys.onDrop, onDrop);
	}

	public String getOnDropOut() {
		return (String) getStateHelper().eval(PropertyKeys.onDropOut);
	}

	public void setOnDropOut(String onDropOut) {
		getStateHelper().put(PropertyKeys.onDropOut, onDropOut);
	}
	
	public String getOnDropOver() {
		return (String) getStateHelper().eval(PropertyKeys.onDropOver);
	}

	public void setOnDropOver(String onDropOver) {
		getStateHelper().put(PropertyKeys.onDropOver, onDropOver);
	}

	public String getSortable() {
		return (String) getStateHelper().eval(PropertyKeys.sortable);
	}

	public void setSortable(String sortable) {
		getStateHelper().put(PropertyKeys.sortable, sortable);
	}
	
	protected enum PropertyKeys {
		forVal("for"), droppableSelector, activeClass, hoverClass, accept, tolerance, dataSource, targetType
			,onDrop, onDropOut, onDropOver, sortable;
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

			Map<String, String> params = context.getExternalContext().getRequestParameterMap();
			String eventName = params.get("javax.faces.behavior.event");

			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if (eventName.equals("drop")) {
				String sourceId = params.get("sourceId");
				String targetId = params.get("targetId");
				DropEvent dndEvent = null;
				String dataSource = getDataSource();

				if (dataSource != null) {
					// If dataSource is not null we asume it either from an UIData component or a repeater
					// So we first get the rowId of the sourceId (it should be something like :n:rowId:id)
					String[] tokens = sourceId.split(String.valueOf(UINamingContainer.getSeparatorChar(context)));
					int idx = Integer.parseInt(tokens[tokens.length - 2]);
					UIComponent component = findComponent(dataSource);
					Object data = null;
					// Source should be a datasource
					if (component instanceof UIData) {
						UIData ds = (UIData) component;
						ds.setRowIndex(idx);
						data = ds.getRowData();
						ds.setRowIndex(-1);
					} else 
						throw new FacesException("DataSource should be an instance of UIData");
					
					// Create the event with the data
					dndEvent = new DropEvent(this,behaviorEvent.getBehavior(), sourceId, targetId, data);
				} else {
					dndEvent = new DropEvent(this,behaviorEvent.getBehavior(), sourceId, targetId);
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

}