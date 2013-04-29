package org.sopeco.webui.client.ui;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class Dialog extends Composite implements HasWidgets {

	private static DialogUiBinder uiBinder = GWT.create(DialogUiBinder.class);

	interface DialogUiBinder extends UiBinder<Widget, Dialog> {
	}

	public Dialog() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	FlowPanel contentPanel;

	@UiField
	Heading heading;

	public void setTitle(String title) {
		heading.setVisible(!title.isEmpty());
		heading.setTitle(title);
	}

	public void setWidthPX(int width) {
		contentPanel.setWidth(width + "px");
	}

	@UiChild
	public void addChild(Widget w) {
		GWT.log("add");
	}

	public void setHasTitle(boolean hasTitle) {
		if (!hasTitle) {
			heading.removeFromParent();
		}
	}

	@Override
	public void add(Widget w) {
		contentPanel.add(w);
	}

	@Override
	public void clear() {
		contentPanel.clear();
	}

	@Override
	public Iterator<Widget> iterator() {
		return contentPanel.iterator();
	}

	@Override
	public boolean remove(Widget w) {
		return contentPanel.remove(w);
	}

}
