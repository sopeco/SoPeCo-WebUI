package org.sopeco.frontend.client.resources;

import org.sopeco.frontend.client.R;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.Window;

/**
 * Loading Resources. The resources are not loaded until they are requested. =>
 * Code-Split-Point.
 * 
 * @author Marius Oehler
 * 
 */
public final class FrontEndResources {

	private FrontEndResources() {
	}

	/**
	 * 
	 */
	public static void loadExperimentCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.experimentCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadCSVEditorCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.csvEditorCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadSpecificationCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.specificationCss().ensureInjected();
			}
		});
	}

	/**
	 * ClientBundle to load Resources.
	 */
	interface ResourceBundle extends ClientBundle {
		@Source("experimentView.css")
		@CssResource.NotStrict
		CssResource experimentCss();

		@Source("csvEditor.css")
		@CssResource.NotStrict
		CssResource csvEditorCss();

		@Source("specificationView.css")
		@CssResource.NotStrict
		CssResource specificationCss();
	}

}