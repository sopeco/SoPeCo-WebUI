/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout.popups;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Timer;
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
