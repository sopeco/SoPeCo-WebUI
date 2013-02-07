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
package org.sopeco.webui.client.layout.environment;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.tree.Tree;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TreeView extends FlowPanel {

	private static final String ELEMENT_CSS_CLASS = "envTreeView";
	private static final String HEADLINE_CSS_CLASS = "envTreeHeadline";
	private static final String IMG_OBSERVATION = "images/eye.png";
	private static final String IMG_INIT_ASSIGNED = "images/list_white.png";

	private static final int RIGHT_SECOND = 35;

	private FlowPanel headline;
	private Tree tree;
	private ToggleButton tbtnObservation, tbtnInitialAssignments;
	private Image imgInfoFirst, imgInfoSecond;

	TreeView(boolean hasTwoCheckboxes) {
		init(hasTwoCheckboxes);
	}

	private void init(boolean hasTwoCheckboxes) {
		addStyleName(ELEMENT_CSS_CLASS);

		headline = new FlowPanel();
		HTML headlineText = new HTML(R.get("envParameter"));

		Image imgObserve = new Image(IMG_OBSERVATION);
		tbtnObservation = new ToggleButton(imgObserve);
		tbtnObservation.setTitle(R.get("showObservation"));

		Image imgInitAssignment = new Image(IMG_INIT_ASSIGNED);
		tbtnInitialAssignments = new ToggleButton(imgInitAssignment);
		tbtnInitialAssignments.setDown(true);
		tbtnInitialAssignments.setTitle(R.get("showInitAssignments"));

		headline.addStyleName(HEADLINE_CSS_CLASS);
		headline.add(headlineText);
		headline.add(tbtnObservation);
		if (hasTwoCheckboxes) {
			headline.add(tbtnInitialAssignments);
		}
		headline.add(new ClearDiv());

		tree = new Tree();

		add(createMarkerBox(true));
		if (hasTwoCheckboxes) {
			add(createMarkerBox(false));
		}

		add(headline);
		add(tree);
	}

	private FlowPanel createMarkerBox(boolean first) {
		FlowPanel testMarker = new FlowPanel();
		testMarker.addStyleName("rMarker");
		Image infoImg = new Image("images/information.png");
		infoImg.setTitle("test");
		testMarker.add(infoImg);
		if (first) {
			imgInfoFirst = infoImg;
		} else {
			testMarker.getElement().getStyle().setRight(RIGHT_SECOND, Unit.PX);
			imgInfoSecond = infoImg;
		}
		return testMarker;
	}

	public void setFirstInfoText(String text) {
		imgInfoFirst.setTitle(text);
	}

	public void setSecondInfoText(String text) {
		imgInfoSecond.setTitle(text);
	}

	public ToggleButton getTbtnObservationButton() {
		return tbtnObservation;
	}

	/**
	 * @return the tbtnInitialAssignments
	 */
	public ToggleButton getTbtnInitialAssignments() {
		return tbtnInitialAssignments;
	}

	public Tree getTree() {
		return tree;
	}
}