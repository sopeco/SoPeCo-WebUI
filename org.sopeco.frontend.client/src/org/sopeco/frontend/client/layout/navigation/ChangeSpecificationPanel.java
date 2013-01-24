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

import java.util.HashMap;

import org.sopeco.frontend.client.resources.R;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The panel, where you can change the current specification.
 * 
 * @author Marius Oehler
 * 
 */
public class ChangeSpecificationPanel extends VerticalPanel {

	private static final String CHANGE_SPECIFICATION_PANEL_ID = "changeSpecPanel";
	private static final String ADD_SPECIFICATION_IMAGE = "<img src=\"images/add_blue.png\" />";
	private static final String ADD_SPECIFIACTION_ID = "addSpecification";

	private HashMap<String, HTML> itemMap;
	private HTML addSpecificationHTML;

	public ChangeSpecificationPanel() {
		getElement().setId(CHANGE_SPECIFICATION_PANEL_ID);

		itemMap = new HashMap<String, HTML>();

		addSpecificationHTML = new HTML(ADD_SPECIFICATION_IMAGE + R.get("addSpecification"));
		addSpecificationHTML.getElement().setId(ADD_SPECIFIACTION_ID);
		add(addSpecificationHTML);

		setVisible(false);
	}

	/**
	 * The map, where all existing items are stored.
	 * 
	 * @return the HashMap
	 */
	public HashMap<String, HTML> getItemMap() {
		return itemMap;
	}

	/**
	 * Returns the "add Spec" HTML Element.
	 * 
	 * @return HTML
	 */
	public HTML getAddSpecificationHTML() {
		return addSpecificationHTML;
	}

	/**
	 * Adds a new item to this panel and to the HashMap. The created Element
	 * will be returned.
	 * 
	 * @param text
	 *            the text of the item
	 * @return the created element
	 */
	public HTML addItem(String text) {
		remove(addSpecificationHTML);

		HTML newItem = new HTML(text);
		add(newItem);
		add(addSpecificationHTML);

		itemMap.put(text, newItem);
		return newItem;
	}
}
