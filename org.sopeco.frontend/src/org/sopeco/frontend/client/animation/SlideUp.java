package org.sopeco.frontend.client.animation;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 *
 */
public final class SlideUp extends SuperAnimation {

	private Widget slidingWidget;
	private int startheight;

	private SlideUp(Widget w) {
		slidingWidget = w;
		startheight = w.getOffsetHeight();
	}

	@Override
	protected void onUpdate(double progress) {
		slidingWidget.setHeight((int) ((1D - interpolate(progress)) * startheight) + "px");
	}

	public static void start(Widget w) {
		start(w, null, DEFAULT);
	}
	
	public static void start(Widget w, ICompleteHandler cHandler) {
		start(w, cHandler, DEFAULT);
	}

	public static void start(Widget w, ICompleteHandler cHandler, int duration) {
		SlideUp ani = new SlideUp(w);
		ani.setCompleteHandler(cHandler);
		ani.run(duration);
	}
	
	@Override
	protected void onComplete() {
		onUpdate(interpolate(1.0));
		
		executeCompleteHandler();
	}
}
