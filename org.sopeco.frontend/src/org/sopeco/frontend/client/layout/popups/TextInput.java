package org.sopeco.frontend.client.layout.popups;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TextInput extends DialogBox {

	public final static int ICO_DEFAULT = 0;
	public final static int ICO_PASSWORD = 1;

	private TextBox textboxText;
	private TextInputOkHandler okHandler;

	private TextInput(int icon, String title, String message, TextInputOkHandler action) {
		super(false, true);

		okHandler = action;

		initialize(icon, title, message);
	}

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

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSpacing(5);
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.add(horizontalPanel_2);

		HTML htmlNewHtml = new HTML(message, true);
		horizontalPanel_2.add(htmlNewHtml);

		textboxText = new TextBox();
		horizontalPanel_2.add(textboxText);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		verticalPanel.add(horizontalPanel_1);

		Button btnNewButton = new Button("ok");
		btnNewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				okHandler.onClick(event, textboxText.getText());
			}
		});
		btnNewButton.addClickHandler(getDefaultCloserClick());
		horizontalPanel_1.add(btnNewButton);
		btnNewButton.setWidth("80px");

		Button btnNewButton_1 = new Button("cancel");
		btnNewButton_1.addClickHandler(getDefaultCloserClick());
		horizontalPanel_1.add(btnNewButton_1);
		btnNewButton_1.setWidth("80px");
	}

	private ClickHandler getDefaultCloserClick() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		};
	}

	public void setFocus() {
		textboxText.setFocus(true);

	}

	public static void doInput(String title, String message, TextInputOkHandler action) {
		doInput(ICO_DEFAULT, title, message, action);
	}

	public static void doInput(int icon, String title, String message, TextInputOkHandler action) {
		TextInput input = new TextInput(icon, title, message, action);

		input.center();
		input.setFocus();
	}
}
