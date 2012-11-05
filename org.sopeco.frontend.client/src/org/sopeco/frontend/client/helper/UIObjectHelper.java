package org.sopeco.frontend.client.helper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UIObjectHelper {
	private UIObjectHelper() {
	}

	/**
	 * Checks whether the given UIObject contains the given css-class in the
	 * class tag.
	 * 
	 * @param object
	 *            uiObject which will be checked
	 * @param name
	 *            name of the css-class
	 * @return true if the object has the class
	 */
	public static boolean hasCssClass(UIObject object, String name) {
		return hasCssClass(object.getElement(), name);
	}

	/**
	 * Checks whether the given element contains the given css-class in the
	 * class tag.
	 * 
	 * @param element
	 *            element which will be checked
	 * @param name
	 *            name of the css-class
	 * @return true if the element has the class
	 */
	public static boolean hasCssClass(Element element, String name) {
		String[] splitted = element.getClassName().split(" ");

		for (String s : splitted) {
			if (s.equals(name)) {
				return true;
			}
		}

		return false;
	}
}
