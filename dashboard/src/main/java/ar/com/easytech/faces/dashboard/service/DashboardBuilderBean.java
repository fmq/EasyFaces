package ar.com.easytech.faces.dashboard.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@Named
public class DashboardBuilderBean extends DashboardBuilderTemplate{
	@PersistenceContext (name="dashboard")
	EntityManager localEM;

	@Override
	protected EntityManager getLocalEM() {
		return this.localEM;
	}

	@Override
	protected EntityManager getRemoteEM() {
		return this.localEM;
	}
	
}
