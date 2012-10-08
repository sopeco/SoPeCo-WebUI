package org.sopeco.frontend.client.layout.center.specification;

import java.util.List;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationController implements ICenterController {

	private SpecificationView view;
	private String currentSpecificationName;
	private List<String> specificationNames;

	public SpecificationController() {
		reset();
	}

	@Override
	public void reset() {
		view = new SpecificationView();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	/**
	 * 
	 * @param name
	 */
	public void setCurrentSpecificationName(String name) {
		GWT.log("Set new currentSpecificationName: " + name);
		MainLayoutPanel.get().getNavigationController().setActiveSpecification(name);
		currentSpecificationName = name;

		view.setSpecificationName(name);
	}

	/**
	 * 
	 * @return
	 */
	public String getCurrentSpecificationName() {
		return currentSpecificationName;
	}

	/**
	 * 
	 */
	public void loadSpecificationNames() {
		Loader.showLoader();
		RPC.getMSpecificationRPC().getAllSpecificationNames(getLoadSpecificationNamesCallback());
	}

	/**
	 * 
	 * @return
	 */
	private AsyncCallback<List<String>> getLoadSpecificationNamesCallback() {
		return new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> result) {
				Loader.hideLoader();
				specificationNames = result;

				MainLayoutPanel.get().getNavigationController().removeAllSpecifications();

				if (!result.isEmpty()) {
					for (String sName : result) {
						MainLayoutPanel.get().getNavigationController().addSpecifications(sName);
					}

					setCurrentSpecificationName(result.get(0));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();
				Message.error(caught.getMessage());
			}
		};
	}
}
