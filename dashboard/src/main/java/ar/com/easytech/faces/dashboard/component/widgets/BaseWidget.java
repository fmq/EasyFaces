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

import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;

public class BaseWidget extends UIComponentBase implements ClientBehaviorHolder {
	
	public static final String COMPONENT_FAMILY = "ar.com.easyfaces.Widget";

	public BaseWidget() {
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
	
	public String getWidgetStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.widgetStyleClass);
	}

	public void setWidgetStyleClass(String widgetStyleClass) {
		getStateHelper().put(PropertyKeys.widgetStyleClass, widgetStyleClass);
	}
	
	public String getHeaderStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.headerStyleClass);
	}

	public void setHeaderStyleClass(String headerStyleClass) {
		getStateHelper().put(PropertyKeys.headerStyleClass, headerStyleClass);
	}

	public String getBodyStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.bodyStyleClass);
	}

	public void setBodyStyleClass(String bodyStyleClass) {
		getStateHelper().put(PropertyKeys.bodyStyleClass, bodyStyleClass);
	}
	
	public Integer getWidth() {
		return (Integer)getStateHelper().eval(PropertyKeys.width);
	}
	
	public void setWidth(Integer width) {
		getStateHelper().put(PropertyKeys.width, width);
	}
	
	public Integer getHeight() {
		return (Integer)getStateHelper().eval(PropertyKeys.height);
	}
	
	public void setHeight(Integer height) {
		getStateHelper().put(PropertyKeys.height, height);
	}
	
	public Object getData() {
		return (Object)getStateHelper().eval(PropertyKeys.data);
	}
	
	public void setData(Object data) {
		getStateHelper().put(PropertyKeys.data, data);
	}
	
	public String getTitle() {
		return (String)getStateHelper().eval(PropertyKeys.title);
	}
	
	public void setTitle(String title) {
		getStateHelper().put(PropertyKeys.title, title);
	}
	
	public Long getInstanceId() {
		return (Long)getStateHelper().eval(PropertyKeys.instanceId);
	}
	
	public void setInstanceId(Long instanceId) {
		getStateHelper().put(PropertyKeys.instanceId, instanceId);
	}
	
	protected enum PropertyKeys {
		widgetStyleClass, styleClass, headerStyleClass, bodyStyleClass, width, height, data, title, instanceId;
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