package org.sopeco.frontend.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.touch.client.Point;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class to easily and quickly create a dialog that has the same layout as
 * all other dialogs. <br>
 * <br>
 * <strong>Example</strong>
 * 
 * <pre>
 * SoPeCoDialog dialog = new SoPeCoDialog(false);
 * 
 * dialog.setHeadline(&quot;My dialog&quot;);
 * dialog.setContentWidget(somePanelWithContent);
 * 
 * addButton(&quot;Close&quot;, new ClickHandler() {
 * 	&#064;Override
 * 	public void onClick(ClickEvent event) {
 * 		hide();
 * 	}
 * });
 * 
 * dialog.center();
 * </pre>
 * 
 * @author Marius Oehler
 * 
 */
public class SoPeCoDialog extends PopupPanel implements ResizeHandler, MouseDownHandler, MouseUpHandler,
		MouseMoveHandler {

	private static final String CSS_CLASS = "sopeco-Dialog";
	private static final String CSS_CLASS_BUTTON_PANEL = "sopeco-Dialog-ButtonPanel";
	private static final String CSS_CLASS_DRAG_PANEL = "dragPanel";
	private static final String CSS_CLASS_GLASS = "sopeco-Dialog-Glass";

	private static final int DEFAULT_WIDTH_PX = 400;

	private List<Button> buttonList;
	private FlowPanel buttonPanel;
	private Widget contentWidget;

	private SimplePanel dragPanel;
	private Headline headline;

	private boolean isDraggable = false;

	private boolean isDragging = false;

	private Point mouseOffset;
	private HandlerRegistration moveGlassRegistration;
	private HandlerRegistration moveRegistration;
	private HandlerRegistration resizeRegistration;
	private double windowMargin = -1;
	private FlowPanel wrapper;

	/**
	 * Constructor. Option autoHide is <code>true</code>. That means the popup
	 * will be automatically hidden when the user clicks outside of it.
	 */
	public SoPeCoDialog() {
		this(true);
	}

	/**
	 * Constructor.
	 * 
	 * @param autoHide
	 *            <code>true</code> if the popup should be automatically hidden
	 *            when the user clicks outside of it or the history token
	 *            changes.
	 */
	public SoPeCoDialog(boolean autoHide) {
		super(autoHide, false);

		addStyleName(CSS_CLASS);
		setGlassStyleName(CSS_CLASS_GLASS);
		setGlassEnabled(true);

		setWidth(DEFAULT_WIDTH_PX + "px");

		resizeRegistration = Window.addResizeHandler(this);

		wrapper = new FlowPanel();
		add(wrapper);
	}

	/**
	 * Adds a new button to the dialog.
	 * 
	 * @param buttonText
	 *            text on the button
	 * @param clickHandler
	 *            of the new button
	 * @return the created button
	 */
	public Button addButton(String buttonText, ClickHandler clickHandler) {
		Button button = new Button(buttonText);
		if (clickHandler != null) {
			button.addClickHandler(clickHandler);
		}
		if (buttonList == null) {
			buttonList = new ArrayList<Button>();
		}

		buttonList.add(button);

		rebuildButtonPanel();

		return button;
	}

	@Override
	public void center() {
		if (windowMargin != -1 && contentWidget != null) {
			String maxHeight = Window.getClientHeight() - windowMargin + "px";
			contentWidget.getElement().getStyle().setProperty("maxHeight", maxHeight);
		}
		super.center();
	}

	/**
	 * Returns the {@link Widget} that is set as the content widget.
	 * 
	 * @return
	 */
	public Widget getContentWidget() {
		return contentWidget;
	}

	@Override
	public synchronized void hide() {
		if (resizeRegistration != null) {
			resizeRegistration.removeHandler();
			resizeRegistration = null;
		}
		super.hide();
	}

	/**
	 * Returns whether the dialog is draggable.
	 * 
	 * @return Returns <code>true</code> if it is draggable else
	 *         <code>false</code> will be returned
	 */
	public boolean isDraggable() {
		return isDraggable;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (!isDraggable) {
			return;
		}
		isDragging = true;

		int x = event.getRelativeX(getElement());
		int y = event.getRelativeY(getElement());
		mouseOffset = new Point(x, y);

		sinkEvents(Event.ONMOUSEMOVE);
		moveRegistration = addHandler(this, MouseMoveEvent.getType());

		Label glass = Label.wrap(getGlassElement());
		moveGlassRegistration = glass.addMouseMoveHandler(this);

		event.stopPropagation();
		event.preventDefault();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (!isDragging) {
			return;
		}
		int xPos = Math.max(0, (int) (event.getClientX() - mouseOffset.getX()));
		int yPos = Math.max(0, (int) (event.getClientY() - mouseOffset.getY()));

		if (xPos + getOffsetWidth() >= Window.getClientWidth()) {
			xPos = Window.getClientWidth() - getOffsetWidth();
		}
		if (yPos + getOffsetHeight() >= Window.getClientHeight()) {
			yPos = Window.getClientHeight() - getOffsetHeight();
		}

		setPopupPosition(xPos, yPos);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (!isDraggable) {
			return;
		}
		isDragging = false;
		if (moveRegistration != null) {
			moveRegistration.removeHandler();
			moveRegistration = null;
		}
		if (moveGlassRegistration != null) {
			moveGlassRegistration.removeHandler();
			moveGlassRegistration = null;
		}
	}

	@Override
	public void onResize(ResizeEvent event) {
		center();
	}

	/**
	 * The given {@link Widget} will be set as the content widget and appears in
	 * the center of the dialog.
	 * 
	 * @param content
	 *            widget
	 */
	public void setContentWidget(Widget content) {
		contentWidget = content;
		rebuild();
	}

	/**
	 * Sets the dialog draggable. It can moved by dragging it with the mouse.
	 * 
	 * @param draggable
	 *            <code>true</code> if the dialog should be draggable
	 */
	public void setDraggable(boolean draggable) {
		isDraggable = draggable;
		if (draggable) {
			if (dragPanel == null) {
				dragPanel = new SimplePanel();
				dragPanel.addStyleName(CSS_CLASS_DRAG_PANEL);

				dragPanel.sinkEvents(Event.ONMOUSEDOWN);
				sinkEvents(Event.ONMOUSEUP);

				dragPanel.addHandler(this, MouseDownEvent.getType());
				addHandler(this, MouseUpEvent.getType());
			}
			wrapper.add(dragPanel);
		} else {
			if (dragPanel.isAttached()) {
				dragPanel.removeFromParent();
			}
		}
	}

	/**
	 * Sets the headline of this dialog.
	 * 
	 * @param pHeadline
	 *            text of the dialog
	 */
	public void setHeadline(String pHeadline) {
		if (headline == null) {
			headline = new Headline();
		}
		headline.setText(pHeadline);
		rebuild();
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);
		if (isShowing()) {
			center();
		}
	}

	/**
	 * If this value is not set to <code>-1</code> the content widget will be
	 * resized respectively the maxHeight will be set to
	 * <code>windowHeight - VALUE</code> on the window resize event. Thereby,
	 * the distance between the window frame and dialog is about
	 * <code>VALUE / 2</code>.
	 * 
	 * @param pMargin
	 *            value of the distance to the window frame
	 */
	public void setWindowMargin(double pMargin) {
		windowMargin = pMargin;
	}

	/**
	 * Refreshes the wrapper panel, which contains all elements of the popup.
	 * This method is called, if elements are added or removed.
	 */
	private void rebuild() {
		wrapper.clear();
		if (headline != null) {
			wrapper.add(headline);
		}
		if (contentWidget != null) {
			wrapper.add(contentWidget);
		}
		if (buttonPanel != null && !buttonList.isEmpty()) {
			wrapper.add(buttonPanel);
		}
		if (isDraggable) {
			wrapper.add(dragPanel);
		}
		if (isShowing()) {
			center();
		}
	}

	/**
	 * Refreshes the panel where the buttons are located. This emthod is called,
	 * if buttons are added or removed.
	 */
	private void rebuildButtonPanel() {
		if (buttonList != null && !buttonList.isEmpty()) {
			if (buttonPanel == null) {
				buttonPanel = new FlowPanel();
				buttonPanel.addStyleName(CSS_CLASS_BUTTON_PANEL);
			} else {
				buttonPanel.clear();
			}
			for (Button btn : buttonList) {
				buttonPanel.add(btn);
			}
			buttonPanel.add(new ClearDiv());
		}
		rebuild();
	}
}
