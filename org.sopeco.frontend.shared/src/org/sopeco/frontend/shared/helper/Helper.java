package org.sopeco.frontend.shared.helper;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Helper {
	private Helper() {
	}

	/**
	 * Prints the method which is called the method where this method is called.
	 * So prints the third method in the stack trace..
	 */
	public static void whoCalledMe() {
		Exception x = new Exception();

		GWT.log(x.getStackTrace()[1].getMethodName() + " CALLED FROM: " + x.getStackTrace()[2].getClassName() + " " + x.getStackTrace()[2].getMethodName() + ":"
				+ x.getStackTrace()[2].getLineNumber());
	}
}
