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
package org.sopeco.webui.client.layout.center;

import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EmptyCenterPanel extends CenterPanel {
	private static final int IMG_SIZE_X = 640, IMG_SIZE_Y = 175;
	private static final int FIFTY = 50;
	private static final double OPACITY = 0.1;
	private static Image logo;

	public EmptyCenterPanel() {
		if (logo == null) {
			logo = new Image(R.img.sopecoLogo().getSafeUri());
			logo.setSize(IMG_SIZE_X + "px", IMG_SIZE_Y + "px");
			logo.getElement().getStyle().setPosition(Position.ABSOLUTE);
			logo.getElement().getStyle().setTop(FIFTY, Unit.PCT);
			logo.getElement().getStyle().setLeft(FIFTY, Unit.PCT);
			logo.getElement().getStyle().setMarginTop(-IMG_SIZE_Y / 2, Unit.PX);
			logo.getElement().getStyle().setMarginLeft(-IMG_SIZE_X / 2, Unit.PX);
			logo.getElement().getStyle().setOpacity(OPACITY);
		}
		add(logo);
	}
}
