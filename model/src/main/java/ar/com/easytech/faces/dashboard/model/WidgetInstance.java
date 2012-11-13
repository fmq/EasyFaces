package ar.com.easytech.faces.dashboard.model;


public class WidgetInstance {

	public WidgetInstance(long instanceId, long layoutId, Widget widget) {
		super();
		this.instanceId = instanceId;
		this.layoutId = layoutId;
		this.widget = widget;
	}

	private long layoutId;
	private long instanceId;
	private Widget widget;
	
	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(long layoutId) {
		this.layoutId = layoutId;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
}
