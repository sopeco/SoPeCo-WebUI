package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.Extension;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentExtensionController {

	private ExperimentExtensionView view;
	private ExtensionTypes extensionType;
	private Extension extension;

	public ExperimentExtensionController(int width) {
		view = new ExperimentExtensionView(width);
	}

	/**
	 * @return the view
	 */
	public ExperimentExtensionView getView() {
		return view;
	}

	/**
	 * Setting the headline text.
	 */
	public void setHeadline(String text) {
		view.getHeadline().setInnerHTML(text);
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtensionType(ExtensionTypes newExtensionType) {
		extensionType = newExtensionType;

		retreiveExtension();
	}

	/**
	 * 
	 * @return
	 */
	private void retreiveExtension() {
		RPC.getExtensionRPC().getExtension(extensionType, new AsyncCallback<Extension>() {
			@Override
			public void onSuccess(Extension result) {
				extension = result;

				updateView();
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}
		});
	}

	/**
	 * 
	 */
	private void updateView() {
		view.getCombobox().clear();

		for (String name : extension.getExtensionMap().keySet()) {
			view.getCombobox().addItem(name);

			for (String key : extension.getExtensionMap().get(name).keySet()) {
				view.addConfigRow(key, extension.getExtensionMap().get(name).get(key));
			}
		}
	}
}
