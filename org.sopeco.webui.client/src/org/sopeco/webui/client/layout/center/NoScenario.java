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

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.webui.client.helper.SimpleCallback;
import org.sopeco.webui.client.layout.dialog.AddScenario;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.ui.Dialog;
import org.sopeco.webui.client.ui.HorizontalRuler;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NoScenario extends CenterPanel implements SimpleCallback<Boolean>, ClickHandler {

	private AddScenario adder;
	private Button createButton;
	
	public NoScenario() {
		adder = new AddScenario();
		adder.setStateHandler(this);

		createButton = new Button(R.lang.create());
		createButton.getElement().getStyle().setFloat(Float.RIGHT);
		createButton.setEnabled(false);
		createButton.addClickHandler(this);

		Dialog dialog = new Dialog();
		dialog.setWidthPX(360);
		dialog.getElement().getStyle().setMarginTop(5, Unit.EM);

		dialog.add(adder);
		dialog.add(new HorizontalRuler());
		dialog.add(createButton);
		dialog.add(new ClearDiv());

		add(dialog);
	}

	@Override
	public void callback(Boolean object) {
		createButton.setEnabled(object);
	}

	@Override
	public void onClick(ClickEvent event) {
		adder.setEnabled(false);
		createButton.setEnabled(false);
		createButton.setText(R.lang.creatingScenario());
		
		adder.createScenario();
	}
}
