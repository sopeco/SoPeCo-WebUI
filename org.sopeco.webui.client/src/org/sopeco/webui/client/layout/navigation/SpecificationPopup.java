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
package org.sopeco.webui.client.layout.navigation;

import java.util.HashMap;

import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The panel, where you can change the current specification.
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationPopup extends VerticalPanel implements ClickHandler {

	private static final String CSS_CLASS = "changeSpecPanel";
	private static final String CSS_CLASS_ADD = "addItem";
	private static final String CSS_CLASS_SELECTED = "selected";

	private HashMap<String, HTML> itemMap;
	private HTML addSpecificationHTML;
	private FlowPanel addPanel;

	private HTML selectedItem;

	public SpecificationPopup() {
		addStyleName(CSS_CLASS);

		itemMap = new HashMap<String, HTML>();

		addSpecificationHTML = new HTML(R.lang.AddSpecification());

		addPanel = new FlowPanel();
		addPanel.addStyleName(CSS_CLASS_ADD);
		addPanel.add(addSpecificationHTML);
		addPanel.add(new Image(R.img.iconSet().getSafeUri(), 0, 120, 13, 13));
		add(addPanel);
	}

	public HashMap<String, HTML> getItemMap() {
		return itemMap;
	}

	public HTML getAddItem() {
		return addSpecificationHTML;
	}

	public HTML addItem(String text) {
		HTML newItem = new HTML(text);
		add(newItem);

		newItem.addClickHandler(this);

		itemMap.put(text, newItem);
		return newItem;
	}

	public void addAddItem() {
		if (addPanel.isAttached()) {
			addPanel.removeFromParent();
		}
		add(addPanel);
	}

	@Override
	public void clear() {
		super.clear();
		itemMap.clear();
		selectedItem = null;
	}

	public void setSelectedItem(String specificationName) {
		if (!itemMap.containsKey(specificationName)) {
			return;
		}
		if (selectedItem != null) {
			selectedItem.removeStyleName(CSS_CLASS_SELECTED);
		}
		selectedItem = itemMap.get(specificationName);
		selectedItem.addStyleName(CSS_CLASS_SELECTED);
	}

	@Override
	public void onClick(ClickEvent event) {
		removeFromParent();
		if (event.getSource() == selectedItem) {
			return;
		}
		String specificationName = ((HTML) event.getSource()).getText();
		setSelectedItem(specificationName);
		ScenarioManager.get().specification().changeSpecification(specificationName);
	}
}
