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
package org.sopeco.gwt.widgets;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class WrappedTextBox extends SimplePanel {

	private static final String CSS_CLASS = "sopeco-WrappedTextBox";
	private TextBox textbox;

	public WrappedTextBox() {
		WidgetResources.resc.wrappedTextBoxCss().ensureInjected();
		
		addStyleName(CSS_CLASS);
		addStyleName("gwt-TextBox");
		textbox = new TextBox();
		textbox.getElement().getStyle().setMargin(0, Unit.PX);
		textbox.getElement().getStyle().setPadding(0, Unit.PX);
		textbox.getElement().getStyle().setBorderWidth(0, Unit.PX);
		textbox.getElement().getStyle().setProperty("outline", "0");
		textbox.getElement().getStyle().setProperty("textOverflow", "ellipsis");
		textbox.setWidth("100%");
		getElement().getStyle().setBackgroundColor("white");
		add(textbox);
	}

	public void setAsPasswordTextbox() {
		textbox.getElement().setAttribute("type", "password");
	}

	public TextBox getTextbox() {
		return textbox;
	}
}
