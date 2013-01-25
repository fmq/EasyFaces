/*
 * net/balusc/http/multipart/MultipartRequest.java
 * 
 * Copyright (C) 2009 BalusC
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package ar.com.easytech.faces.filters;

import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class MultipartRequest extends HttpServletRequestWrapper {

	final Map<String, Object> parameterMap = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public MultipartRequest(HttpServletRequest request, String path)
			throws Exception {
		super(request);
		DiskFileItemFactory factory = new DiskFileItemFactory();

		if (path != null)
			factory.setRepository(new File(path));

		ServletFileUpload upload = new ServletFileUpload(factory);
		parameterMap.put("path", path);
		
		try {
			List<FileItem> items = (List<FileItem>) upload.parseRequest(request);

			for (FileItem item : items) {
				
				String str = item.getString();
				if (item.isFormField())
					parameterMap.put(item.getFieldName(), str);
				else {
					if (item.getName() != null) {
						parameterMap.put("fileName", item.getName() );
					}
					request.setAttribute(item.getFieldName(), item);
				}
			}

		} catch (FileUploadException ex) {
			ServletException servletEx = new ServletException();
			servletEx.initCause(ex);
			throw new Exception(ex.getLocalizedMessage());
		}
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		
		for (String key : parameterMap.keySet()) {
			Object param = parameterMap.get(key);
			if (param instanceof String[])
				map.put(key,(String[])param);
			else 
				map.put(key, new String[] {param.toString()});
				
		}
		return map;
	}

	@Override
	public String[] getParameterValues(String name) {
		Object param = parameterMap.get(name);
		if (param instanceof String[])
			return (String[])param;
		return parameterMap.get(name) != null ? new String[] {parameterMap.get(name).toString()} : null;
		
	}

	@Override
	public String getParameter(String name) {
		Object param = getParameterValues(name);
		
		if (param != null && param instanceof String[]) {
			String[] values = (String[])param;
			return values[0];
		}
		
        return param != null ? param.toString(): null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(getParameterMap().keySet());
	}
	
}