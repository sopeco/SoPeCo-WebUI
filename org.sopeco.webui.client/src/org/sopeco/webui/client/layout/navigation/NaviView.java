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

import org.sopeco.webui.client.helper.BrandingChecker;
import org.sopeco.webui.client.helper.SimpleCallback;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NaviView extends FlowPanel implements SimpleCallback {
	private static final String CSS_CLASS = "navigation";
	private static final String CSS_CLASS_LOGO_PANEL = "logoPanel";

	private Image logo;
	private FlowPanel logoPanel;

	public NaviView() {
		R.css.cssNavigation().ensureInjected();

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		logoPanel = new FlowPanel();
		add(logoPanel);
		
		BrandingChecker.checkBranding(this);
	}

	@Override
	public void callback(Object object) {
		if ((Boolean) object) {
			logo = new Image("/branding.png");
			logoPanel.addStyleName(CSS_CLASS_LOGO_PANEL);
			logoPanel.add(new HTML("powered by"));
			logoPanel.add(logo);
		} else {
			logoPanel.removeFromParent();
		}
	}

	@Override
	public void clear() {
		super.clear();
		if (logoPanel != null) {
			add(logoPanel);
		}
	}
}
