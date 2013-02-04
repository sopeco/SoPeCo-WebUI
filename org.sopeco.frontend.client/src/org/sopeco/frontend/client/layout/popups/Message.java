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
package org.sopeco.frontend.client.layout.popups;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.SoPeCoUI;
import org.sopeco.frontend.client.widget.ExceptionDialog;
import org.sopeco.frontend.client.widget.SoPeCoDialog;

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

	/**
	 * @deprecated Give the throwable element to
	 *             {@link ExceptionDialog#show(Throwable)} for detailed error
	 *             messages.
	 * @param message
	 */
	@Deprecated
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
