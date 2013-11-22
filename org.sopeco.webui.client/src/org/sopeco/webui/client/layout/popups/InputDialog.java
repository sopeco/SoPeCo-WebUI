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
package org.sopeco.webui.client.layout.popups;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates an modal text input dialog.
 * 
 * @author Marius Oehler
 * 
 */
public final class InputDialog extends DialogBox implements ClickHandler, KeyUpHandler, BlurHandler {

	private static final String[] ICON_URLS = new String[] { "images/text.png", "images/lock.png", "images/add_big.png" };

	private Button btnOk, btnCancel;

	private HTML errorText;
	private List<InputDialogHandler> handlerList;
	private TextBox textboxText;
	private InputDialogValidator validator;

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the dialog
	 * @param message
	 *            The message that is displayed.
	 */
	public InputDialog(String title, String message) {
		this(title, message, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the dialog
	 * @param message
	 *            The message that is displayed.
	 * @param passwordText
	 *            Is the textbox a password textbox
	 */
	public InputDialog(String title, String message, boolean passwordText) {
		this(title, message, passwordText, Icon.Default);
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the dialog
	 * @param message
	 *            The message that is displayed.
	 * @param passwordText
	 *            Is the textbox a password textbox
	 * @param icon
	 *            Icon of the dialog.
	 */
	public InputDialog(String title, String message, boolean passwordText, Icon icon) {
		this(title, message, passwordText, ICON_URLS[Icon.Default.ordinal()]);
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the dialog
	 * @param message
	 *            The message that is displayed.
	 * @param passwordText
	 *            Is the textbox a password textbox
	 * @param icon
	 *            URL of the Icon of the dialog.
	 */
	public InputDialog(String title, String message, boolean passwordText, String icon) {
		initialize(icon, title, message, passwordText);
	}

	/**
	 * Adds an InputDialogHandler to this dialog, which will be called, if the
	 * user press the OK Button.
	 * 
	 * @param handler
	 */
	public void addHandler(InputDialogHandler handler) {
		handlerList.add(handler);
	}

	@Override
	public void center() {
		checkInput();
		super.center();
	}

	public void hideWarning() {
		errorText.setVisible(false);
	}

	@Override
	public void onBlur(BlurEvent event) {
		checkInput();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnOk && btnOk.isEnabled()) {
			for (InputDialogHandler handler : handlerList) {
				handler.onInput(this, textboxText.getText());
			}
			hide();
		} else if (event.getSource() == btnCancel) {
			hide();
		}
	}

	/**
	 * Handler to confirm with the enter key.
	 */
	@Override
	public void onKeyUp(KeyUpEvent event) {
		checkInput();
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			btnOk.fireEvent(new ClickEvent() {
			});
		}
	}

	/**
	 * Sets the validator to check the entered string.
	 * 
	 * @param pValidator
	 */
	public void setValidator(InputDialogValidator pValidator) {
		this.validator = pValidator;
	}

	/**
	 * Sets the value of the textbox.
	 * 
	 * @param text
	 */
	public void setValue(String text) {
		textboxText.setText(text);
	}

	public void showWarning(String text) {
		errorText.setText(text);
		errorText.setVisible(true);
	}

	/**
	 * Checks whether the text in the textbox is valid.
	 */
	private void checkInput() {
		if (validator == null || validator.validate(this, textboxText.getText())) {
			btnOk.setEnabled(true);
		} else {
			btnOk.setEnabled(false);
		}
	}

	/**
	 * Initialize the dialog box.
	 * 
	 * @param icon
	 *            displayed icon
	 * @param title
	 *            the title of the dialog box
	 * @param message
	 *            the message in the dialog box
	 */
	private void initialize(String iconUrl, String title, String message, boolean isPassword) {
		setGlassEnabled(true);

		handlerList = new ArrayList<InputDialogHandler>();

		setText(title);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		mainPanel.setSize("100%", "100%");

		HorizontalPanel panelInput = new HorizontalPanel();
		panelInput.setSpacing(8);
		panelInput.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Image image = new Image(iconUrl);
		image.setWidth("48px");
		image.setHeight("48px");

		HTML htmlLabel = new HTML(message);

		errorText = new HTML("Info");
		errorText.getElement().getStyle().setColor("red");
		errorText.getElement().getStyle().setMarginBottom(1, Unit.EM);
		errorText.setVisible(false);

		if (isPassword) {
			textboxText = new PasswordTextBox();
		} else {
			textboxText = new TextBox();
		}
		textboxText.addKeyUpHandler(this);
		textboxText.addBlurHandler(this);

		HorizontalPanel panelButtons = new HorizontalPanel();
		panelButtons.setSpacing(5);

		btnOk = new Button(R.lang.Ok());
		btnOk.setWidth("80px");
		btnOk.addClickHandler(this);

		btnCancel = new Button(R.lang.Cancel());
		btnCancel.setWidth("80px");
		btnCancel.addClickHandler(this);

		panelInput.add(image);
		panelInput.add(htmlLabel);
		panelInput.add(textboxText);
		panelButtons.add(btnOk);
		panelButtons.add(btnCancel);
		mainPanel.add(panelInput);
		mainPanel.add(errorText);
		mainPanel.add(panelButtons);
		setWidget(mainPanel);
	}

	/**
	 * Icons for the textinput.
	 */
	public enum Icon {
		/** */
		Add, Default, Password;
	}
}
