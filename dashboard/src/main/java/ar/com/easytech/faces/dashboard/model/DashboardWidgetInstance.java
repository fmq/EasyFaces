package ar.com.easytech.faces.dashboard.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="ef_widget_instance")
public class DashboardWidgetInstance implements Serializable, Comparable<DashboardWidgetInstance>{

	private static final long serialVersionUID = 2652359464077410552L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlTransient
	private long id;
	
	@OneToOne(cascade= CascadeType.MERGE, fetch= FetchType.EAGER)
	@JoinColumn(name = "widget_id")
	private Widget widget;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "pos")
	private int position;
	
	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public int compareTo(DashboardWidgetInstance o) {
		
		return this.position - o.position;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
}
