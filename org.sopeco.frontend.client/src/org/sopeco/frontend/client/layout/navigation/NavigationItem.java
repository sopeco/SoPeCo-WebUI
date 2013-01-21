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
package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.helper.UIObjectHelper;
import org.sopeco.frontend.client.layout.center.CenterType;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * The class of the items for the navigation.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationItem extends FocusPanel {

	private static final String CSS_SUB_TEXT_CLASS = "subText";
	private static final String CHANGE_SPECIFICATION_ICON_ID = "changeSpecImage";
	private static final String CHANGE_SPECIFICATION_ICON = "images/switch.png";

	private FlowPanel contentPanel;
	private boolean isActive;
	private CenterType type;
	private Element subTextElement;
	private HTML label;

	/**
	 * The panel, which contains all other widgets.
	 * 
	 * @return
	 */
	protected FlowPanel getContentPanel() {
		return contentPanel;
	}

	public NavigationItem(CenterType centerType, String name) {
		type = centerType;

		addStyleName("tlEntry");

		contentPanel = new FlowPanel();

		FlowPanel textWrapper = new FlowPanel();
		label = new HTML(name);

		subTextElement = DOM.createSpan();
		subTextElement.addClassName(CSS_SUB_TEXT_CLASS);

		textWrapper.add(label);
		textWrapper.getElement().appendChild(subTextElement);

		contentPanel.add(textWrapper);

		HTML marked = new HTML();
		marked.addStyleName("marker");
		contentPanel.add(marked);

		add(contentPanel);

		preventFocus();
	}

	/**
	 * Sets the second textline of the navi items.
	 * 
	 * @param text
	 */
	public void setSubText(String text) {
		subTextElement.setInnerHTML(text);
	}

	/**
	 * Adds a FocusHandler, which prevents this item is focused.
	 */
	private void preventFocus() {
		addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setFocus(false);
			}
		});
	}

	/**
	 * Returns the HTML Element 'label'.
	 */
	public HTML getLabel() {
		return label;
	}

	/**
	 * Returns the CenterType of this navigation item.
	 * 
	 * @return CenterType of this item
	 */
	public CenterType getType() {
		return type;
	}

	/**
	 * Adds the item/icon to show the panel, where you can change the current
	 * specification.
	 */
	public ChangeSpecificationPanel addChangeSpecificationIcon() {
		Image changeSpecification = new Image(CHANGE_SPECIFICATION_ICON);
		changeSpecification.getElement().setId(CHANGE_SPECIFICATION_ICON_ID);

		final ChangeSpecificationPanel specificationPanel = new ChangeSpecificationPanel();

		changeSpecification.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (specificationPanel.isVisible()) {
					specificationPanel.setVisible(false);
				} else {
					specificationPanel.setVisible(true);
				}
			}
		});

		contentPanel.add(changeSpecification);

		contentPanel.add(specificationPanel);

		return specificationPanel;
	}

	/**
	 * Set whether the item is highlighted.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		if (active) {
			addStyleName("marked");
		} else {
			removeStyleName("marked");
		}

		isActive = active;
	}

	/**
	 * Return whether the item is highlighted.
	 * 
	 * @return
	 */
	public boolean isActive() {
		return UIObjectHelper.hasCssClass(this, "marked");

		// return isActive;
	}

}
