package org.sopeco.frontend.client.helper;

/**
 * 
 * @author Marius Oehler
 *
 * @param <T>
 */
public interface INotifyHandler<T> {
	void call(boolean success, T result);
}
