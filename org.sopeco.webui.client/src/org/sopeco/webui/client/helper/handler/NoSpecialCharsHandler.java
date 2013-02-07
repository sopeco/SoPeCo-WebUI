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
package org.sopeco.webui.client.helper.handler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Restricts input to numbers and letters.
 * 
 * @author Marius Oehler
 * 
 */
public class NoSpecialCharsHandler implements KeyPressHandler, ChangeHandler {

	@Override
	public void onKeyPress(KeyPressEvent event) {
		/*
		 * KeyCodes: 46: ENTF, 37-40: LEFT UP DOWN RIGHT, 8: BACKSPACE
		 */
		if (!Character.isLetterOrDigit(event.getCharCode()) && event.getCharCode() != '_'
				&& event.getNativeEvent().getKeyCode() != 8 && event.getNativeEvent().getKeyCode() != 46
				&& (event.getNativeEvent().getKeyCode() < 37 || event.getNativeEvent().getKeyCode() > 40)) {
			((TextBox) event.getSource()).cancelKey();
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		String text = ((TextBox) event.getSource()).getText();

		text = text.replaceAll("[^\\w]", "");

		((TextBox) event.getSource()).setText(text);
	}

}