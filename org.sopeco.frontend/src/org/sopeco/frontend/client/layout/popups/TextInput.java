package org.sopeco.frontend.client.layout.popups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creates an modal text input dialog.
 * 
 * @author Marius Oehler
 * 
 */
public final class TextInput extends DialogBox implements KeyPressHandler {

	/**
	 * Standard icon: notepad.
	 */
	public static final int ICO_DEFAULT = 0;
	/**
	 * Password icon: lock.
	 */
	public static final int ICO_PASSWORD = 1;

	private TextBox textboxText;
	private TextInputOkHandler okHandler;
	private Button btnNewButton;

	private TextInput(int icon, String title, String message, TextInputOkHandler action) {
		super(false, true);

		okHandler = action;

		initialize(icon, title, message);
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
	private void initialize(int icon, String title, String message) {
		setGlassEnabled(true);

		setText(title);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(8);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);

		Image image = null;
		switch (icon) {
		case ICO_PASSWORD:
			image = new Image("images/lock.png");
			break;
		case ICO_DEFAULT:
		default:
			image = new Image("images/text.png");
		}

		horizontalPanel.add(image);

		HorizontalPanel horizontalPanel2 = new HorizontalPanel();
		horizontalPanel2.setSpacing(5);
		horizontalPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.add(horizontalPanel2);

		HTML htmlNewHtml = new HTML(message, true);
		horizontalPanel2.add(htmlNewHtml);

		textboxText = new TextBox();
		horizontalPanel2.add(textboxText);
		textboxText.addKeyPressHandler(this);

		HorizontalPanel horizontalPanel1 = new HorizontalPanel();
		horizontalPanel1.setSpacing(5);
		verticalPanel.add(horizontalPanel1);

		btnNewButton = new Button("ok");
		btnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				okHandler.onInput(event, textboxText.getText());
			}
		});
		btnNewButton.addClickHandler(getDefaultCloserClick());
		horizontalPanel1.add(btnNewButton);
		btnNewButton.setWidth("80px");

		Button btnNewButton1 = new Button("cancel");
		btnNewButton1.addClickHandler(getDefaultCloserClick());
		horizontalPanel1.add(btnNewButton1);
		btnNewButton1.setWidth("80px");
	}

	/**
	 * Returns a clickhandler which hides the dialog.
	 * 
	 * @return clickhandler
	 */
	private ClickHandler getDefaultCloserClick() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		};
	}

	/**
	 * Set the focus on the textbox.
	 */
	public void setFocus() {
		textboxText.setFocus(true);
	}

	/**
	 * Creates and displays a input dialog.
	 * 
	 * @param title
	 *            the title of the dialog
	 * @param message
	 *            the message in the dialog
	 * @param action
	 *            the handler of the ok button
	 */
	public static void doInput(String title, String message, TextInputOkHandler action) {
		doInput(ICO_DEFAULT, title, message, action);
	}

	/**
	 * Creates and displays a input dialog.
	 * 
	 * @param icon
	 *            the displayed icon
	 * @param title
	 *            the title of the dialog
	 * @param message
	 *            the message in the dialog
	 * @param action
	 *            the handler of the ok button
	 */
	public static void doInput(int icon, String title, String message, TextInputOkHandler action) {
		TextInput input = new TextInput(icon, title, message, action);

		input.center();
		input.setFocus();
	}

	/**
	 * Handler to confirm with the enter key.
	 */
	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER) {
			btnNewButton.fireEvent(new ClickEvent() {
			});
		}
	}
}
