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
package org.sopeco.frontend.client.helper.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 * @param <T>
 */
public class ParallelCallback<T> implements AsyncCallback<T> {

	private T data;
	private int index;
	private Throwable failure;

	private CallbackBatch batch;

	@Override
	public void onSuccess(T result) {
		data = result;
		batch.ready(-1);
	}

	@Override
	public void onFailure(Throwable caught) {
		failure = caught;
		batch.ready(index);
	}

	/**
	 * Returns the data of this callback.
	 * 
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * Returns the thrown Throwable object, if a failrue occured.
	 * 
	 * @return Throwable
	 */
	public Throwable getFailure() {
		return failure;
	}

	/**
	 * Sets the callbackBatch, which handles the success and failure functions.
	 * 
	 * @param callbackBatch
	 * @param indexPosition
	 */
	public void setBatch(CallbackBatch callbackBatch, int indexPosition) {
		batch = callbackBatch;
		index = indexPosition;
	}
}
