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

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import ar.com.easytech.faces.event.DropEvent;

@FacesComponent(Droppable.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="javax.faces", name="jsf.js"),
})
public class Droppable extends UIComponentBase implements ClientBehaviorHolder {
	
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Droppable";
	public static final String DEFAULT_RENDERER_TYPE = "ar.com.easyfaces.DroppableRenderer";
	public static final String COMPONENT_FAMILY = "javax.faces.Output";

	public final static String DEFAULT_EVENT = "drop";
	
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

			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

			if (eventName.equals("drop")) {
				String dragId = params.get("sourceId");
				String dropId = params.get("targetId");
				DropEvent dndEvent = null;
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

					dndEvent = new DropEvent(this,
							behaviorEvent.getBehavior(), dragId, dropId, data);
				} else {
					dndEvent = new DropEvent(this,
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

	


}