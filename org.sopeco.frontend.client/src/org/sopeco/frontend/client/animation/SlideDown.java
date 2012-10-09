package org.sopeco.frontend.client.animation;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class SlideDown extends SuperAnimation {

	private Widget slidingWidget;
	private int targetHeight;

	private SlideDown(Widget w, int target) {
		slidingWidget = w;
		targetHeight = target;
	}

	@Override
	protected void onUpdate(double progress) {
		slidingWidget.setHeight((int) (targetHeight * interpolate(progress)) + "px");
	}

	public static void start(Widget w, int targetHeight) {
		start(w, targetHeight, null, DEFAULT);
	}

	public static void start(Widget w, int targetHeight, ICompleteHandler cHandler) {
		start(w, targetHeight, cHandler, DEFAULT);
	}

	public static void start(Widget w, int targetHeight, ICompleteHandler cHandler, int duration) {
		SlideDown ani = new SlideDown(w, targetHeight);

		if (cHandler != null) {
			ani.setCompleteHandler(cHandler);
		}

		ani.run(DEFAULT);
	}

	@Override
	protected void onComplete() {
		onUpdate(interpolate(1.0));

		executeCompleteHandler();
	}

}
