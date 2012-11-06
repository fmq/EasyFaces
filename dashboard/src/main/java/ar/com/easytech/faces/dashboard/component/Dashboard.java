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
package ar.com.easytech.faces.dashboard.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import ar.com.easytech.faces.dashboard.component.DashboardColumn.PropertyKeys;



@FacesComponent(Dashboard.COMPONENT_TYPE)
@ResourceDependencies({
	@ResourceDependency(library="javax.faces", name="jsf.js"),
	@ResourceDependency(library="easyfaces", name="easyfaces.js"),
	@ResourceDependency(library="easyfaces", name="dashboard.js"),
	@ResourceDependency(library="js", name="jquery-1.8.2.js"),
	@ResourceDependency(library="js", name="jquery-ui-1.9.0.custom.js"),
	@ResourceDependency(library="js", name="jquery.flot.js"),
	@ResourceDependency(library="js", name="jquery.flot.resize.js"),
	@ResourceDependency(library="js", name="jquery.flot.pie.js"),
	@ResourceDependency(library="js", name="excanvas.compiled.js"),
	@ResourceDependency(library="css", name="smoothness/jquery-ui-1.9.0.custom.css"),
	@ResourceDependency(library="css", name="widgets.css")
})
public class Dashboard extends UIComponentBase implements ClientBehaviorHolder {
	
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.Dashboard";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.DashboardRenderer";
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Dashboard";

	public final static String DEFAULT_EVENT = "update";
	
	private int columns = 2;
	
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT));

	public Dashboard() {
		setRendererType(RENDERER_TYPE);
	}
	
	 

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getUserId() {
		return (String) getStateHelper().eval(PropertyKeys.userId);
	}

	public void setUserId(String userId) {
		getStateHelper().put(PropertyKeys.userId, userId);
	}
	
	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
	
	public Object getValue() {
		return (Object) getStateHelper().eval(PropertyKeys.value);
	}

	public void setValue(Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}	
	
	public Integer getColumns() {
		Integer cols = (Integer) getStateHelper().eval(PropertyKeys.columns);
		return cols != null ? cols : columns;
	}

	public void setColumns(Integer value) {
		getStateHelper().put(PropertyKeys.value, value);
	}	
	
	public Long getInstanceId() {
		return (Long) getStateHelper().eval(PropertyKeys.instanceId);
	}

	public void setInstanceId(Long instanceId) {
		getStateHelper().put(PropertyKeys.instanceId, instanceId);
	}
	
	protected enum PropertyKeys {
		userId, styleClass, value, columns, instanceId;
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
			super.queueEvent(behaviorEvent);
			
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