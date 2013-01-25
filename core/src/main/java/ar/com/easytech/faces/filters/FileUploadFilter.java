package ar.com.easytech.faces.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FileUploadFilter implements Filter {

	private static final String MULTIPART = "multipart/";
	
	private String uploadBase;
	
	public void init(FilterConfig config) throws ServletException {

		uploadBase = config.getInitParameter("uploadBase");

	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// If we are uploading a file just use BALUSC multipart request
		// as the request in order to parse the file.
		if (isMultipartContent(httpRequest)) {
			try {
				request = new MultipartRequest(httpRequest, uploadBase);
			} catch (Exception _ex) {
				throw new ServletException(_ex.getLocalizedMessage());
			}
		}

		chain.doFilter(request, response);
		return;
	}

	private boolean isMultipartContent(HttpServletRequest request) {
		if (!"post".equals(request.getMethod().toLowerCase())) {
			return false;
		}
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		if (contentType.toLowerCase().startsWith(MULTIPART)) {
			return true;
		}
		return false;
	}
}
