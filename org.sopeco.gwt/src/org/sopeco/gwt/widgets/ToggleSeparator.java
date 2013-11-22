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

import java.util.ArrayList;
import java.util.List;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ToggleSeparator extends HorizontalPanel implements ClickHandler {

	private static final String CSS_CLASS = "spc-ToggleSeparator";

	private HTML htmlLabel;
	private Image imgState;

	private String firstLabel;
	private String secondLabel;

	private boolean state;

	private List<ToggleHandler> handlerList;

	public ToggleSeparator(String pFirstLabel, String pSecondLabel) {
		WidgetResources.resc.toggleSeparatorCss().ensureInjected();

		handlerList = new ArrayList<ToggleSeparator.ToggleHandler>();

		state = false;

		firstLabel = pFirstLabel;
		secondLabel = pSecondLabel;

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		htmlLabel = new HTML(firstLabel);
		htmlLabel.addClickHandler(this);

		imgState = new Image(WidgetResources.resc.imgArrowStateRight());
		imgState.getElement().getStyle().setVerticalAlign(VerticalAlign.BOTTOM);
		imgState.addClickHandler(this);

		add(imgState);
		add(htmlLabel);
		add(new HorizontalLine());

		setCellWidth(imgState, "1px");
		setCellWidth(htmlLabel, "1px");
	}

	@Override
	public void onClick(ClickEvent event) {
		state = !state;
		if (!state) {
			htmlLabel.setHTML(firstLabel);
			imgState.setResource(WidgetResources.resc.imgArrowStateRight());
		} else {
			htmlLabel.setHTML(secondLabel);
			imgState.setResource(WidgetResources.resc.imgArrowStateDown());
		}
		for (ToggleHandler handler : new ArrayList<ToggleHandler>(handlerList)) {
			handler.onToggle(state);
		}
	}

	public void addToggleHandler(ToggleHandler handler) {
		handlerList.add(handler);
	}

	public void removeToggleHandler(ToggleHandler handler) {
		handlerList.remove(handler);
	}

	public boolean getState() {
		return state;
	}

	/**
	 * 
	 * @author Marius Oehler
	 * 
	 */
	public interface ToggleHandler {
		void onToggle(boolean state);
	}
}
