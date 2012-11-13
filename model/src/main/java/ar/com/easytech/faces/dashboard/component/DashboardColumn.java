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

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@FacesComponent(DashboardColumn.COMPONENT_TYPE)
public class DashboardColumn extends UIComponentBase {
	
	public static final String COMPONENT_TYPE = "ar.com.easyfaces.DashboardColumn";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.DashboardColumnRenderer";
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Dashboard";

	public DashboardColumn() {
		setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass);
	}

	public void setStyleClass(String styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, styleClass);
	}
	
	public Integer getColumnPosition() {
		return (Integer) getStateHelper().eval(PropertyKeys.columnPosition);
	}

	public void setColumnPosition(Integer columnPosition) {
		getStateHelper().put(PropertyKeys.columnPosition, columnPosition);
	}

	public Long getLayoutId() {
		return (Long) getStateHelper().eval(PropertyKeys.layoutId);
	}

	public void setLayoutId(Long layoutId) {
		getStateHelper().put(PropertyKeys.layoutId, layoutId);
	}
	
	protected enum PropertyKeys {
		styleClass, columnPosition, layoutId;
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

}