package org.sopeco.frontend.client.layout.popups;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Class for the loading dialog.
 * 
 * @author Marius Oehler
 * 
 */
public final class Loader extends DialogBox {

	private static int count = 0;
	private static Loader loader;

	private Loader(String text) {
		super(false, true);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(15);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		Label lblLoading = new Label(text);
		verticalPanel.add(lblLoading);

		Image image = new Image("images/loading.gif");
		verticalPanel.add(image);

		setGlassEnabled(true);
	}

	public static void showLoader() {
		showLoader("loading");
	}

	public static void showLoader(final String txt) {
		if (loader == null) {
			loader = new Loader(txt);
		}

		count++;

		if (!loader.isShowing()) {
			loader.center();
		}
	}

	public static void hideLoader() {
		if (loader == null || count <= 0) {
			return;
		}

		count--;

		if (count <= 0) {
			loader.hide();
			loader = null;
		}
	}

}
