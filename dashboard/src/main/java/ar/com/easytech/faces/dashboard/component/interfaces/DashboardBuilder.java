package ar.com.easytech.faces.dashboard.component.interfaces;

import java.util.List;

import ar.com.easytech.faces.dashboard.model.DashboardDefinition;
import ar.com.easytech.faces.dashboard.model.Widget;
import ar.com.easytech.faces.dashboard.model.WidgetType;

public interface DashboardBuilder {

	public abstract DashboardDefinition getDashboardForUser(String userId);

	public abstract List<Widget> getWidgetsForColumn(DashboardDefinition dashboard, int column);

	//Execute SQL
	public abstract String getWidgetData(String sql, WidgetType type);

}