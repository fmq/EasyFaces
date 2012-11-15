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
package ar.com.easytech.faces.dashboard.component.widgets;

import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;

@FacesComponent(DataTable.COMPONENT_TYPE)
@ResourceDependencies({
		@ResourceDependency(library = "js", name = "jquery.flot.js"),
		@ResourceDependency(library = "js", name = "jquery.flot.pie.js"),
		@ResourceDependency(library = "js", name = "excanvas.compiled.js"),
		@ResourceDependency(library = "css", name = "widgets.css") })
public class DataTable extends BaseWidget implements ClientBehaviorHolder {

	public static final String COMPONENT_TYPE = "ar.com.easyfaces.DataTableWidget";
	public static final String RENDERER_TYPE = "ar.com.easyfaces.DataTableRenderer";

	public DataTable() {
		setRendererType(RENDERER_TYPE);
	}

	public List<String> getHeaders() {
		return (List<String>) getStateHelper().eval(PropertyKeys.headers);
	}

	public void setHeaders(List<String> headers) {
		getStateHelper().put(PropertyKeys.headers, headers);
	}
	
	public String getColumnStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.columnStyleClass);
	}

	public void setColumnHeaderStyleClass(String columnHeaderStyleClass) {
		getStateHelper().put(PropertyKeys.columnHeaderStyleClass, columnHeaderStyleClass);
	}

	protected enum PropertyKeys {
		headers, columnStyleClass, columnHeaderStyleClass;
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