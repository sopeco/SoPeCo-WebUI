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

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class for the loading dialog.
 * 
 * @author Marius Oehler
 * 
 */
public final class Loader extends DialogBox {

	private static int count = 0, counterIcon = 0;
	private static Loader loader;

	private Loader(String text) {
		super(false, true);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(15);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		Label lblLoading = new Label(text);
		verticalPanel.add(lblLoading);

		Image image = new Image("images/loading.gif");
		verticalPanel.add(image);

		setGlassEnabled(true);
	}

	public static void showLoader() {
		showLoader("loading");
	}

	public static void showLoader(final String txt) {
		if (loader == null) {
			loader = new Loader(txt);
		}

		count++;

		if (!loader.isShowing()) {
			loader.center();
		}
	}

	public static void hideLoader() {
		if (loader == null || count <= 0) {
			return;
		}

		count--;

		if (count <= 0) {
			loader.hide();
			loader = null;
		}
	}

	public static void showIcon() {
		counterIcon++;

		DOM.getElementById("loadingIndicator").getStyle().setDisplay(Display.BLOCK);
	}

	public static void hideIcon() {
		if (counterIcon <= 0) {
			return;
		}

		counterIcon--;

		if (counterIcon <= 0) {
			DOM.getElementById("loadingIndicator").getStyle().setDisplay(Display.NONE);
		}
	}
}
