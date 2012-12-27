package ar.com.easytech.faces.dashboard.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ar.com.easytech.faces.dashboard.service.DashboardBuilderBean;

public class Producers {
	
	public static DashboardBuilderBean getDashboardBuilder() {
		DashboardBuilderBean builder = null;
		try {
			InitialContext ic = new InitialContext();
			builder = (DashboardBuilderBean) ic.lookup("java:module/DashboardBuilderBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return builder;
	}
}
