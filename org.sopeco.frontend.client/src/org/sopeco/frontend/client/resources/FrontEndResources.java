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