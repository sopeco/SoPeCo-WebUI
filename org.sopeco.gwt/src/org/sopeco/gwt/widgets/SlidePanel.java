package org.sopeco.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SlidePanel extends FlowPanel {

	private static final int ANIMATION_DURATION = 400;
	private int width, height;

	private int slidePosition;
	private List<FlowPanel> wrapperList;
	private FlowPanel slidingPanel, footerPanel;
	private VerticalPanel sliderLayout;
	private Animation activeAnimation;

	/**
	 * Constructor.
	 * 
	 * @param pWidth
	 *            width of the panel in PX
	 * @param pHeight
	 *            height of the panel in PX
	 */
	public SlidePanel(int pWidth, int pHeight) {
		width = pWidth;
		height = pHeight;

		slidePosition = 0;
		wrapperList = new ArrayList<FlowPanel>();

		init();
	}

	/**
	 * Initialize
	 */
	private void init() {
		getElement().getStyle().setOverflow(Overflow.HIDDEN);

		slidingPanel = new FlowPanel();
		slidingPanel.setHeight(height + "px");
		slidingPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		slidingPanel.getElement().getStyle().setLeft(0, Unit.PX);

		footerPanel = new FlowPanel();

		sliderLayout = new VerticalPanel();
		sliderLayout.add(slidingPanel);
		sliderLayout.add(footerPanel);

		sliderLayout.setCellHeight(slidingPanel, height + "px");
		sliderLayout.setCellWidth(slidingPanel, width + "px");

		add(sliderLayout);
	}

	/**
	 * Adds a widget to the sliderPanel.
	 * 
	 * @param widget
	 */
	public void addWidget(Widget widget) {
		FlowPanel wrapper = createWrapperPanel(widget);
		wrapperList.add(wrapper);
		slidingPanel.add(wrapper);

		slidingPanel.setWidth((width * wrapperList.size()) + "px");
	}

	/**
	 * 
	 * @param widget
	 * @return
	 */
	private FlowPanel createWrapperPanel(Widget widget) {
		FlowPanel wrapper = new FlowPanel();
		wrapper.setWidth(width + "px");
		wrapper.setHeight(height + "px");
		wrapper.getElement().getStyle().setPosition(Position.ABSOLUTE);
		wrapper.getElement().getStyle().setTop(0, Unit.PX);
		wrapper.getElement().getStyle().setLeft(width * wrapperList.size(), Unit.PX);
		wrapper.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		wrapper.add(widget);
		return wrapper;
	}

	/**
	 * Slides the panel in the left direction, that the next element appears.
	 * Exp: |A| B C - next() - A |B| C
	 */
	public void next() {
		if (hasNext()) {

			anim(-(slidePosition * width), -width);

			slidePosition++;
		}
	}

	/**
	 * Slides the panel in the left direction, that the next element appears.
	 * Exp: A |B| C - previous() - |A| B C
	 */
	public void previous() {
		if (hasPrevious()) {

			anim(-(slidePosition * width), width);

			slidePosition--;
		}
	}

	/**
	 * Returns whether a widget exists, after the current shown.
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return slidePosition + 1 < wrapperList.size();
	}

	/**
	 * Returns whether a widget exists, before the current shown.
	 * 
	 * @return
	 */
	public boolean hasPrevious() {
		return slidePosition - 1 >= 0;
	}

	/**
	 * Adds the given Widget to the footerPanel.
	 * 
	 * @param w
	 */
	public void addFooterWidget(Widget w) {
		footerPanel.add(w);
	}

	/**
	 * Animates the slidingPanel.
	 * 
	 * @param start
	 *            the start 'left' value.
	 * @param offset
	 *            the amount that the panel moves in PX.
	 */
	private void anim(final int start, final int offset) {
		if (activeAnimation != null) {
			activeAnimation.cancel();
		}

		activeAnimation = new Animation() {
			@Override
			protected void onUpdate(double progress) {
				int newPosition = start + (int) (offset * progress);
				SlidePanel.this.slidingPanel.getElement().getStyle().setLeft(newPosition, Unit.PX);
			}
		};
		activeAnimation.run(ANIMATION_DURATION);
	}

	/**
	 * @return the slidePosition
	 */
	public int getSlidePosition() {
		return slidePosition;
	}

	/**
	 * Returns the footerPanel of this widget.
	 * 
	 * @return
	 */
	public FlowPanel getFooterPanel() {
		return footerPanel;
	}
}
