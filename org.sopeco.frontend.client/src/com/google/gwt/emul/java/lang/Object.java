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
package java.lang;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.impl.Impl;

/**
 * The superclass of all other types. The GWT emulation library supports a
 * limited subset of methods on <code>Object</code> due to browser limitations.
 * The methods documented here are the only ones available.
 */
public class Object {

	/**
	 * Used by {@link com.google.gwt.core.client.impl.WeakMapping} in web mode
	 * to store an expando containing a String -> Object mapping.
	 * 
	 * @skip
	 */
	@SuppressWarnings("unused")
	private transient JavaScriptObject expando;

	/**
	 * A JavaScript Json map for looking up castability between types.
	 * 
	 * @skip
	 */
	@SuppressWarnings("unused")
	private transient JavaScriptObject castableTypeMap;

	/**
	 * A special marker field used internally to the GWT compiler. For example,
	 * it is used for distinguishing whether an object is a Java object or a
	 * JavaScriptObject. It is also used to differentiate our own Java objects
	 * from foreign objects in a different module on the same page.
	 * 
	 * @see com.google.gwt.lang.Cast
	 * 
	 * @skip
	 */
	@SuppressWarnings("unused")
	private transient JavaScriptObject typeMarker;

	public boolean equals(Object other) {
		return this == other;
	}

	/*
	 * Note: Unlike the real JRE, we don't spec this method as final because the
	 * compiler generates a polymorphic override on every other class which will
	 * return the correct class object.
	 * 
	 * TODO(scottb, compiler magician): declare this final, but have the
	 * compiler fix it up.
	 */
	public Class<? extends Object> getClass() {
		return Object.class;
	}

	public int hashCode() {
		return Impl.getHashCode(this);
	}

	public String toString() {
		return getClass().getName() + '@' + Integer.toHexString(hashCode());
	}

	protected void finalize() throws Throwable {
	}

	protected native Object clone() throws CloneNotSupportedException /*-{
		var cloned = {};

		@com.google.gwt.core.client.impl.Impl::getHashCode(Ljava/lang/Object;)(cloned);

		var obj = this;
		for ( var i in obj) {
			if (!(i in cloned)) {
				cloned[i] = obj[i];
			}
		}
		return cloned;
	}-*/;
}
