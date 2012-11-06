package ar.com.easytech.faces.dashboard.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "ef_dashboard_definition")
@NamedQueries(value = { @NamedQuery(name = "ef_dashboard_for_user", query = "select d from DashboardDefinition d where d.userId = :userId") })
public class DashboardDefinition implements Serializable {

	private static final long serialVersionUID = -4277191914176680877L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlTransient
	private long id;

	@NotNull
	private int columns;

	private String userId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinColumn(name = "dashboard_id")
	private List<DashboardColumnLayout> columnLayout;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getColumns() {
		return columns != 0 ? columns : 1;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<DashboardColumnLayout> getColumnLayout() {
		return columnLayout;
	}

	public void setColumnLayout(List<DashboardColumnLayout> columnLayout) {
		this.columnLayout = columnLayout;
	}

}
