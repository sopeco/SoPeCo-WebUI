package org.sopeco.frontend.client.widget;

import org.sopeco.frontend.client.layout.popups.Notification;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentTreeItem extends FrontendTreeItem {

	private FlowPanel actionPanel;
	private Image removeNamespace, addNamespace, addParameter;

	public EnvironmentTreeItem(String html) {
		super(html);
	}

	@Override
	protected void initialize() {
		super.initialize();

		actionPanel = new FlowPanel();
		actionPanel.addStyleName("actionPanel");

		removeNamespace = new Image("images/trash_white.png");
		addNamespace = new Image("images/add_white.png");
		addParameter = new Image("images/list_white.png");

		removeNamespace.addClickHandler(getClickHandlerRemove());
		addNamespace.addClickHandler(getClickHandlerAdd());
//		add.addClickHandler(getClickHandlerAdd());
		
		removeNamespace.setTitle(R.get("removeNamespace"));
		addNamespace.setTitle(R.get("addNamespace"));
		addParameter.setTitle(R.get("addParameter"));
	}

	@Override
	protected void refreshLinePanel() {
		linePanel.clear();
		if (children.size() > 0) {
			linePanel.add(img);
		}
		linePanel.add(htmlText);

		addActionPanel();

		linePanel.getElement().appendChild(clearLine);
	}

	protected void addActionPanel() {
		actionPanel.clear();

		actionPanel.add(addParameter);
		actionPanel.add(addNamespace);

		if (parentItem != null) {
			actionPanel.add(removeNamespace);
		}

		linePanel.add(actionPanel);
	}

	private ClickHandler getClickHandlerRemove() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove();
			}
		};
	}

	private ClickHandler getClickHandlerAdd() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				EnvironmentTreeItem newItem = new EnvironmentTreeItem("" + System.currentTimeMillis());
				addItem(newItem);
			}
		};
	}
}
