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
package org.sopeco.webui.client.widget;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Preformatted;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExceptionDialog extends SoPeCoDialog implements ClickHandler {

	private static final String CSS_CLASS = "sopeco-ExceptionDialog-Exception";
	private static final double CONTENT_MARGIN_EM = 0.5;
	private static final int WINDOW_SPACE = 200;

	public static void show(Throwable throwable) {
		new ExceptionDialog(throwable).center();
	}

	private Button closeButton;
	private Throwable throwable;
	private FlowPanel content;
	private Anchor showStack;
	private Preformatted error;

	private ExceptionDialog(Throwable pThrowable) {
		super(false);

		throwable = pThrowable;
		setHeadline("Exception was thrown");
		closeButton = addButton("Close", this);

		init();
	}

	private void init() {
		content = new FlowPanel();
		content.getElement().getStyle().setMarginTop(CONTENT_MARGIN_EM, Unit.EM);
		content.getElement().getStyle().setMarginBottom(CONTENT_MARGIN_EM, Unit.EM);

		HTML html = new HTML("The following exception was thrown:");
		html.getElement().getStyle().setFloat(Float.LEFT);
		showStack = new Anchor("show stacktrace");
		showStack.getElement().getStyle().setFloat(Float.RIGHT);
		showStack.addClickHandler(this);

		error = new Preformatted(throwable.getClass().getName() + ": " + throwable.getMessage());
		error.addStyleName(CSS_CLASS);

		content.add(html);
		content.add(showStack);
		content.add(new ClearDiv());
		content.add(error);

		setContentWidget(content);

		getElement().getStyle().setProperty("minWidth", "400px");
		setWidth("");

		setDraggable(true);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == closeButton) {
			hide();
		} else if (event.getSource() == showStack) {
			String st = throwable.getClass().getName() + ": " + throwable.getMessage();
			for (StackTraceElement ste : throwable.getStackTrace()) {
				st += "\n" + ste.toString();
			}
			error.setHTML(st);
			setWidth("600px");
			error.getElement().getStyle().setOverflowX(Overflow.SCROLL);
			showStack.removeFromParent();
			center();
		}
	}

	@Override
	public void center() {
		error.getElement().getStyle().setProperty("maxHeight", (Window.getClientHeight() - WINDOW_SPACE) + "px");
		super.center();
	}
}
