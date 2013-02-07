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
package org.sopeco.gwt.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class WidgetResources {
	private WidgetResources() {
	}

	public static final ResourceBundle resc = GWT.create(ResourceBundle.class);

	/**
	 * ClientBundle to load Resources.
	 */
	public static interface ResourceBundle extends ClientBundle {
		@Source("comboBox.css")
		@CssResource.NotStrict
		CssResource comboBoxCss();

		@Source("editableText.css")
		@CssResource.NotStrict
		CssResource editableTextCss();

		@Source("progressBar.css")
		@CssResource.NotStrict
		CssResource progressBarCss();

		@Source("toggleSeparator.css")
		@CssResource.NotStrict
		CssResource toggleSeparatorCss();

		/** IMAGES. */

		@Source("images/arrow_state_right.png")
		ImageResource imgArrowStateRight();

		@Source("images/arrow_state_down.png")
		ImageResource imgArrowStateDown();

		@Source("images/triangle_20x20.png")
		DataResource imgTriangle20x20();

	}
}
