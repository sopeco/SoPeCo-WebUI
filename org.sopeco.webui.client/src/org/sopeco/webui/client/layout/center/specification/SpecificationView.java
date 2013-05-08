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
package org.sopeco.webui.client.layout.center.specification;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.ImageHover;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationView extends FlowPanel {

	private static final String IMAGE_DUPLICATE = "images/duplicate.png";
	private static final String IMAGE_RENAME = "images/rename.png";
	private static final String IMAGE_REMOVE = "images/trash.png";


	private Widget selectionView, assignmentListPanel;
	private int selectionPanelPosition;
	private boolean selectionPanelIsVisible;

	private HTML htmlName;
	private FlowPanel topWrapper;
	private Image imgDuplicate, imgRemove, imgRename;

	private static final String TOP_PANEL_HEIGHT = "39";
	private static final int SELECTION_PANEL_WIDTH = 400;

	public SpecificationView(Widget assignmentView, Widget sView) {
		assignmentListPanel = assignmentView;
		selectionView = sView;

		initialize();
	}

	/**
	 * Initializing the widgets.
	 */
	private void initialize() {
		setWidth("100%");
		setHeight("100%");

		htmlName = new HTML("1234567890");
		htmlName.addStyleName("name");

		imgRename = new ImageHover(R.img.iconSet().getSafeUri(), 30, 60, 24, 18, R.img.iconSet().getSafeUri(),
				30, 90, 24, 18);
		imgDuplicate = new ImageHover(R.img.iconSet().getSafeUri(), 60, 60, 13, 18, R.img.iconSet().getSafeUri(),
				60, 90, 13, 18);
		imgRemove = new ImageHover(R.img.iconSet().getSafeUri(), 0, 60, 16, 18, R.img.iconSet().getSafeUri(),
				0, 90, 16, 18);

		imgRename.setTitle(R.get("Rename"));
		imgDuplicate.setTitle(R.get("Duplicate"));
		imgRemove.setTitle(R.get("Remove"));

		topWrapper = new FlowPanel();
		topWrapper.add(new HTML("Name:"));
		topWrapper.add(htmlName);
		topWrapper.add(imgRename);
		//topWrapper.add(imgDuplicate);
		topWrapper.add(imgRemove);
		topWrapper.add(new ClearDiv());
		topWrapper.addStyleName("expTopWrapper");


		Label nameLabel = new Label(R.get("name") + ":");
		nameLabel.addStyleName("spc-Label");


		selectionView.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);
		selectionView.getElement().getStyle().setPosition(Position.ABSOLUTE);
		selectionView.getElement().getStyle().setWidth(SELECTION_PANEL_WIDTH, Unit.PX);

		assignmentListPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		assignmentListPanel.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);

		setSelectionPanelPosition(1);
		selectionPanelIsVisible = false;

		// ADDING
		add(topWrapper);
		add(assignmentListPanel);
		add(selectionView);
	}

	/**
	 * Set the position of the selection panel and of all related elements like
	 * the toggleSelectionElement and the assignmentListPanel. parameter is a
	 * percent value between 0 (0%) and 1 (100%).
	 * 
	 * @param x
	 */
	private void setSelectionPanelPosition(float x) {
		x = Math.max(0F, Math.min(1F, x));
		selectionPanelPosition = (int) (SELECTION_PANEL_WIDTH * x);

		selectionView.getElement().getStyle().setLeft(selectionPanelPosition - SELECTION_PANEL_WIDTH, Unit.PX);
		assignmentListPanel.getElement().getStyle().setLeft(selectionPanelPosition, Unit.PX);
	}

	/**
	 * Set whether the selectionPanel is visible.
	 * 
	 * @param visible
	 */
	public void setSelectionPanelVisible(boolean visible) {
		selectionPanelIsVisible = visible;
		if (visible) {
			setSelectionPanelPosition(1F);
		} else {
			setSelectionPanelPosition(0);
		}
	}

	/**
	 * @return the selectionPanelVisible
	 */
	public boolean isSelectionPanelVisible() {
		return selectionPanelIsVisible;
	}

	/**
	 * Setting the given string to the specificationName-Textbox.
	 * 
	 * @param name
	 */
	public void setSpecificationName(String name) {
		htmlName.setText(name);
	}

	public Image getImgDuplicate() {
		return imgDuplicate;
	}

	public Image getImgRemove() {
		return imgRemove;
	}

	public Image getImgRename() {
		return imgRename;
	}

	/**
	 * Returns the textbox of the specification name.
	 * 
	 * @return
	 */
	// public TextBox getSpecificationNameTextbox() {
	// return textboxName;
	// }

}
