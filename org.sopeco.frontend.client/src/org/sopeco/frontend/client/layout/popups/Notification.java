package org.sopeco.frontend.client.layout.popups;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

public class Notification extends PopupPanel {

	private static Notification currentNotification;
	private static List<Notification> queue = new ArrayList<Notification>();

	private Notification(String html) {
		super(false, false);
		setHeight("72px");
		setAnimationEnabled(true);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setWidget(horizontalPanel);
		horizontalPanel.setSize("", "60px");

		Image image = new Image("images/tipp.png");
		horizontalPanel.add(image);

		HTML htmlNewHtml = new HTML(html, true);
		horizontalPanel.add(htmlNewHtml);
	}

	public static void show(String message) {
		Notification not = new Notification(message);
		not.setAnimationEnabled(true);
		not.addStyleName("sopeco-notification-bottom");

		if (currentNotification == null) {
			currentNotification = not;
			not.show();

			startTimer();
		} else {
			queue.add(not);
		}
	}

	public void hide() {
		if (queue.isEmpty()) {
			super.hide();
			currentNotification = null;
		} else {
			super.hide();
			currentNotification = queue.get(0);
			queue.remove(0);
			currentNotification.show();

			startTimer();
		}
	}

	private static void startTimer() {
		Timer t = new Timer() {
			@Override
			public void run() {
				currentNotification.hide();
			}
		};

		t.schedule(5000);
	}
}
