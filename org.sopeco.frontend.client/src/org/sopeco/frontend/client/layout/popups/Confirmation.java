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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Confirmation extends DialogBox implements ClickHandler {

	private Button btnConfirm, btnCancel;
	private HTML lblText;
	private static Confirmation currentConfirmDialog = null;

	private static List<Confirmation> queue = new ArrayList<Confirmation>();

	/*
	 * show a confirm dialog with the committed handler (here: cancelhandler is
	 * default handler)
	 */
	public static void confirm(String message, ClickHandler onConfirm) {
		confirm(message, onConfirm, null);
	}

	/*
	 * show a confirm dialog with the committed handler
	 */
	public static void confirm(String message, ClickHandler onConfirm, ClickHandler onCancel) {
		Confirmation conf = new Confirmation(message, onConfirm, onCancel);

		if (currentConfirmDialog == null) {
			currentConfirmDialog = conf;
			conf.center();
		} else {
			queue.add(conf);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		GWT.log("hide confirmation dialog");

		currentConfirmDialog.hide();

		if (queue.isEmpty()) {
			currentConfirmDialog = null;
		} else {
			currentConfirmDialog = queue.get(0);
			queue.remove(0);
			currentConfirmDialog.center();
		}
	}

	/*
	 * create a confirm dialog
	 */
	private Confirmation(String message, ClickHandler onConfirm, ClickHandler onCancel) {
		super(false, true);

		initialize();

		lblText.setHTML(message);

		btnConfirm.addClickHandler(onConfirm);
		btnConfirm.addClickHandler(this);
		if (onCancel == null) {
			btnCancel.addClickHandler(this);
		} else {
			btnCancel.addClickHandler(onCancel);
		}
	}

	/*
	 * init the gui
	 */
	private void initialize() {
		setGlassEnabled(true);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(8);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);

		Image image = new Image("images/question.png");
		horizontalPanel.add(image);

		lblText = new HTML("Are you sure?");
		horizontalPanel.add(lblText);
		lblText.setWidth("");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(8);
		verticalPanel.add(horizontalPanel_1);

		btnConfirm = new Button("Yes");
		horizontalPanel_1.add(btnConfirm);
		btnConfirm.setWidth("60px");

		btnCancel = new Button("Cancel");
		horizontalPanel_1.add(btnCancel);
		btnCancel.setWidth("60px");
	}

}
