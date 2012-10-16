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
