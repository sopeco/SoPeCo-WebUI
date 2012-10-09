package org.sopeco.frontend.client.layout.popups;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class Message extends DialogBox implements CloseHandler {

	private static Message current;
	private static List<Message> queue = new ArrayList<Message>();

	private static final int ERROR = 1;
	private static final int WARNING = 2;

	private Message(String text, int type) {
		super(true, true);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setWidget(horizontalPanel);
		horizontalPanel.setSize("", "");

		Image image = null;
		switch (type) {
		case ERROR:
			image = new Image("images/stop.png");
			break;
		default:
		case WARNING:
			image = new Image("images/alert.png");
			break;
		}
		horizontalPanel.add(image);

		Label lblMessage = new Label(text);
		lblMessage.setStyleName("warning-label", true);
		horizontalPanel.add(lblMessage);

		setGlassEnabled(true);
		setTitle("Warning");

		addCloseHandler(this);

		if (current == null) {
			current = this;
			current.center();
		} else {
			queue.add(this);
		}
	}

	public static void warning(String message) {
		new Message("Warning: " + message, WARNING);
	}

	public static void error(String message) {
		new Message("Error: " + message, ERROR);
	}

	@Override
	public void onClose(CloseEvent event) {
		current = null;

		if (!queue.isEmpty()) {
			current = queue.get(0);
			current.center();
			queue.remove(0);
		}
	}
}
