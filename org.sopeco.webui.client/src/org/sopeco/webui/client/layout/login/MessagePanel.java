package org.sopeco.webui.client.layout.login;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

public class MessagePanel extends FlowPanel {

	private Image image;
	private HTML message;

	private Button btnBack;

	public MessagePanel() {
		this(false, "");
	}

	public MessagePanel(String message) {
		this(false, message);
	}

	public MessagePanel(boolean loading, String strMessage) {
		addStyleName("content");
		addStyleName("dialogBox");
		addStyleName("dialogBox-padding");
		
		getElement().getStyle().setTextAlign(TextAlign.CENTER);

		setWidth("250px");

		image = new Image(R.resc.imgLoadingIndicatorCircle().getSafeUri());
		image.getElement().getStyle().setFloat(Float.LEFT);
		image.getElement().getStyle().setMarginRight(2, Unit.EM);

		add(image);

		image.setVisible(loading);

		message = new HTML(strMessage);

		add(message);

		add(new ClearDiv());

		btnBack = new Button("Back");
		btnBack.getElement().getStyle().setMarginTop(1, Unit.EM);
		btnBack.getElement().getStyle().setPaddingLeft(15, Unit.PX);
		btnBack.getElement().getStyle().setPaddingRight(15, Unit.PX);

		add(btnBack);

		add(new ClearDiv());
	}

	public Button getBtnBack() {
		return btnBack;
	}

	public void setMessage(String strMessage) {
		message.setText(strMessage);
		image.setVisible(true);
	}

	public void loginFailed() {
		btnBack.setVisible(true);
		image.setVisible(false);
		message.setText("The user name or password is incorrect.");
	}
}
