package ar.com.easytech.faces.dashboard.model;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.inject.Typed;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import ar.com.easytech.faces.dashboard.enumerations.ColumnType;

@Entity
@Table(name = "ef_dashboard_column_layout")
public class DashboardColumnLayout implements Serializable {

	private static final long serialVersionUID = -4277191914176680877L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlTransient
	private long id;

	@NotNull
	private int position;

	@Typed
	private ColumnType type;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dashboard_layout_id")
	private List<DashboardWidgetInstance> dashboardWidgets;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ColumnType getType() {
		return type;
	}

	public void setType(ColumnType type) {
		this.type = type;
	}

	public List<DashboardWidgetInstance> getDashboardWidgets() {
		return dashboardWidgets;
	}

	public void setDashboardWidgets(List<DashboardWidgetInstance> dashboardWidgets) {
		this.dashboardWidgets = dashboardWidgets;
	}

}
