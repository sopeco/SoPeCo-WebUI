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
	 * 
	 */
	public static void loadTopNavigationCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.topNavigationCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadScenarioAddCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.scenarioAddCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadSopecoTabPanelCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.sopecoTabPanelCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadControllerViewCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.controllerViewCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadLoginBoxCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.loginBoxCss().ensureInjected();
			}
		});
	}

	/**
	 * 
	 */
	public static void loadExecuteViewCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.executeViewCss().ensureInjected();
			}
		});
	}
	
	/**
	 * 
	 */
	public static void loadVisualizationViewCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.visualizationViewCss().ensureInjected();
			}
		});
	}
	
	/**
	 * 
	 */
	public static void loadVisualizationWizardCSS() {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert(R.get("errorCssLoading"));
			}

			@Override
			public void onSuccess() {
				ResourceBundle rsc = GWT.create(ResourceBundle.class);
				rsc.visualizationWizardCss().ensureInjected();
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

		@Source("topNavigation.css")
		@CssResource.NotStrict
		CssResource topNavigationCss();

		@Source("scenarioAddView.css")
		@CssResource.NotStrict
		CssResource scenarioAddCss();

		@Source("sopecoTabPanel.css")
		@CssResource.NotStrict
		CssResource sopecoTabPanelCss();

		@Source("controllerView.css")
		@CssResource.NotStrict
		CssResource controllerViewCss();

		@Source("loginBox.css")
		@CssResource.NotStrict
		CssResource loginBoxCss();

		@Source("executeView.css")
		@CssResource.NotStrict
		CssResource executeViewCss();
		
		@Source("visualizationView.css")
		@CssResource.NotStrict
		CssResource visualizationViewCss();
		
		@Source("visualizationWizard.css")
		@CssResource.NotStrict
		CssResource visualizationWizardCss();
	}

}