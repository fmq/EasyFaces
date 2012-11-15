package ar.com.easytech.faces.dashboard.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ar.com.easytech.faces.dashboard.service.DashboardBuilderTemplate;

public class Producers {
	
	public static DashboardBuilderTemplate getDashboardBuilder() {
		DashboardBuilderTemplate builder = null;
		try {
			InitialContext ic = new InitialContext();
			builder = (DashboardBuilderTemplate) ic
					.lookup("java:global/easyfaces/DashboardBuilderBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return builder;
	}
}
