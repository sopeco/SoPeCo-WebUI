package org.sopeco.gwt.widgets.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class WidgetResources {
	private WidgetResources() {
	}

	/**
	 * 
	 */
	public static void loadComboBoxCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Can't load comboBox.css");
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.comboBoxCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadEditableTextCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Can't load comboBox.css");
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.editableTextCss().ensureInjected();
			}
		});
	}

	/**
	 * ClientBundle to load Resources.
	 */
	interface ResourceBundle extends ClientBundle {
		@Source("comboBox.css")
		@CssResource.NotStrict
		CssResource comboBoxCss();

		@Source("editableText.css")
		@CssResource.NotStrict
		CssResource editableTextCss();
	}
}
