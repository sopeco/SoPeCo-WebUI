package org.sopeco.frontend.client.helper;

public interface INotifyHandler<T> {
	void call(boolean success, T result);
}
