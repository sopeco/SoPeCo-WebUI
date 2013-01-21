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
package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.ClearDiv;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentSettingsView extends FlowPanel {

	private static final String EXP_SETTINGS_PANEL_ID = "expSettingsPanel";
	private static final String IMAGE_DUPLICATE = "images/duplicate.png";
	private static final String IMAGE_RENAME = "images/rename.png";
	private static final String IMAGE_REMOVE = "images/trash.png";

	private HTML htmlName;
	private FlowPanel topWrapper;

	private Image imgDuplicate, imgRemove, imgRename;

	public ExperimentSettingsView() {
		initialize();
	}

	/**
	 * Inits the necessary objects.
	 */
	private void initialize() {
		getElement().setId(EXP_SETTINGS_PANEL_ID);

		htmlName = new HTML("1234567890");
		htmlName.addStyleName("name");

		imgRename = new Image(IMAGE_RENAME);
		imgDuplicate = new Image(IMAGE_DUPLICATE);
		imgRemove = new Image(IMAGE_REMOVE);

		imgRename.setTitle(R.get("Rename"));
		imgDuplicate.setTitle(R.get("Duplicate"));
		imgRemove.setTitle(R.get("Remove"));

		topWrapper = new FlowPanel();
		topWrapper.add(new HTML("Name:"));
		topWrapper.add(htmlName);
		topWrapper.add(imgRename);
		topWrapper.add(imgDuplicate);
		topWrapper.add(imgRemove);
		topWrapper.add(new ClearDiv());
		topWrapper.addStyleName("expTopWrapper");

		add(topWrapper);
	}

	public void addExtensionView(ExperimentExtensionView extView) {
		add(extView);
	}

	/**
	 * Sets all important values to a default value.
	 */
	public void reset() {
	}

	public void setExperimentName(String text) {
		htmlName.setText(text);
	}

	/**
	 * @return the removeExperimentImage
	 */
	public Image getImgRemove() {
		return imgRemove;
	}

	/**
	 * @return the imgDuplicate
	 */
	public Image getImgDuplicate() {
		return imgDuplicate;
	}

	/**
	 * @return the imgRename
	 */
	public Image getImgRename() {
		return imgRename;
	}

}
