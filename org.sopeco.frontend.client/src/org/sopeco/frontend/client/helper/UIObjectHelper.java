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
