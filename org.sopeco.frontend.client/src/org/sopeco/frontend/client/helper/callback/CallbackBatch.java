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

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of callbacks. Executes a method when all callbacks have been
 * finished.
 * 
 * @author Marius Oehler
 * 
 */
public abstract class CallbackBatch {

	/** Indexes of failed callbacks. */
	private List<Integer> failedCallbacks;
	/** Count of finished callbacks. */
	private int readyCallbacks = 0;

	/** All callbacks in this batch */
	@SuppressWarnings("rawtypes")
	private ParallelCallback[] callbackArray;

	@SuppressWarnings("rawtypes")
	public CallbackBatch(ParallelCallback... callbacks) {
		callbackArray = callbacks;
		failedCallbacks = new ArrayList<Integer>();

		for (int i = 0; i < callbacks.length; i++) {
			callbacks[i].setBatch(this, i);
		}
	}

	/**
	 * Returns the callback on the given index from the callbackArray.
	 * 
	 * @param index
	 * @return ParallelCallback
	 */
	@SuppressWarnings("rawtypes")
	public ParallelCallback getCallback(int index) {
		if (index < 0 || index >= callbackArray.length) {
			throw new RuntimeException("Index out of range.");
		}

		return callbackArray[index];
	}

	/**
	 * Notification of a finished callback.
	 * 
	 * @param failedIndex
	 */
	protected synchronized void ready(int failedIndex) {
		readyCallbacks++;

		if (failedIndex != -1) {
			failedCallbacks.add(failedIndex);
		}

		if (readyCallbacks >= callbackArray.length) {
			if (failedCallbacks.isEmpty()) {
				onSuccess();
			} else {
				List<Throwable> exceptionList = new ArrayList<Throwable>();
				for (int i : failedCallbacks) {
					exceptionList.add(callbackArray[i].getFailure());
				}
				onFailure(null);
			}
		}
	}

	/**
	 * Returns the data of the callback which is on index 'index'.
	 * 
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <D extends Object> D getCallbackData(int index) {
		return (D) getCallback(index).getData();
	}

	/**
	 * Called when all callbacks were successful.
	 */
	protected abstract void onSuccess();

	/**
	 * Called when at least one callback has failed.
	 */
	protected abstract void onFailure(List<Throwable> exceptionList);

	/**
	 * Returns whether the callback on the given index was successful.
	 * 
	 * @param index
	 * @return
	 */
	public boolean callbackSuccessful(int index) {
		for (int i : failedCallbacks) {
			if (i == index) {
				return false;
			}
		}
		return true;
	}
}
