package org.sopeco.frontend.client.helper;

/**
 * 
 * @author Marius Oehler
 * 
 * @param <T>
 */
public interface INotifyHandler<T> {
	void call(Result<T> result);

	/**
	 * The class, that contains all information about the result of the call.
	 */
	public class Result<T> {
		private boolean successfull;
		private T resultValue;
		private String key;

		public Result(boolean pSuccessful, T pResultValue) {
			this(pSuccessful, pResultValue, null);
		}

		public Result(boolean pSuccessful, T pResultValue, String pKey) {
			successfull = pSuccessful;
			resultValue = pResultValue;
			key = pKey;
		}

		/**
		 * Returns whether the call was successful.
		 * 
		 * @return
		 */
		public boolean wasSuccessful() {
			return successfull;
		}

		/**
		 * @return the resultValue
		 */
		public T getValue() {
			return resultValue;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}
	}
}