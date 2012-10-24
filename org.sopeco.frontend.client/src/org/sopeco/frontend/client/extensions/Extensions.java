package org.sopeco.frontend.client.extensions;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.frontend.client.helper.callback.ParallelCallback;
import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Extensions {

	private static Extensions instance;

	private ExtensionContainer extensionContainer;

	private Extensions() {
	}

	/**
	 * Returns the singleton isntance of this class.
	 * 
	 * @return Extensions
	 */
	public static Extensions get() {
		if (instance == null) {
			instance = new Extensions();
		}
		return instance;
	}

	/**
	 * Loads all Extensions from the server.
	 */
	public static ParallelCallback<ExtensionContainer> getLoadingCallback() {
		return new ParallelCallback<ExtensionContainer>() {

			@Override
			public void onSuccess(ExtensionContainer result) {
				get().extensionContainer = result;
				super.onSuccess(result);
			}
		};
	}

	/**
	 * Return all extensions of the given type/class.
	 * 
	 * @param type
	 * @return
	 */
	public Map<String, Map<String, String>> getExtensions(ExtensionTypes type) {
		if (!extensionContainer.getMap().containsKey(type)) {
			return new HashMap<String, Map<String, String>>();
		}

		return extensionContainer.getExtensions(type);
	}
}
