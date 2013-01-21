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

import org.sopeco.frontend.client.layout.center.CenterType;

import com.google.gwt.user.client.ui.HTML;

/**
 * The class of the navigation items for the experiments.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationSubItem extends NavigationItem {

	private static final String ADD_IMAGE = "<img src=\"images/add_blue.png\">";
	private String experimentName;

	public NavigationSubItem(String name, CenterType type) {
		super(type, name);
		experimentName = name;

		removeStyleName("tlEntry");
		addStyleName("slEntry");

		HTML marked = new HTML();
		marked.addStyleName("marker");
		marked.addStyleName("second");
		getContentPanel().add(marked);
	}

	/**
	 * Returns the name of this item.
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * Adds an add iamge to the label on the navi item.
	 */
	public void addAddImage() {
		getLabel().setHTML(ADD_IMAGE + getLabel().getText());
	}
}
