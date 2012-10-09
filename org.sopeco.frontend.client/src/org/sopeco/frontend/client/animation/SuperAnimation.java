package org.sopeco.frontend.client.animation;

import com.google.gwt.animation.client.Animation;

/**
 * 
 * Wrapper class of the animations.
 * 
 * @author Marius Oehler
 * 
 */
public abstract class SuperAnimation extends Animation {

	/**
	 * Longest duration: 1200ms.
	 */
	public static final int SLOW = 1200;
	/**
	 * Default duration: 600ms.
	 */
	public static final int DEFAULT = 600;
	/**
	 * Shortest duration: 300ms.
	 */
	public static final int FAST = 300;

	private ICompleteHandler completeHandler;

	@Override
	protected abstract void onUpdate(double progress);

	@Override
	protected abstract void onComplete();

	protected void setCompleteHandler(ICompleteHandler handler) {
		completeHandler = handler;
	}

	/**
	 * Handler which will be executed when the animation ends.
	 * 
	 * @return completeHandler
	 */
	protected void executeCompleteHandler() {
		if (completeHandler != null) {
			completeHandler.onComplete();
		}
	}
}
