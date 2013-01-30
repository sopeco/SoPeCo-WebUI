package org.sopeco.frontend.client.widget;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Preformatted;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExceptionDialog extends SoPeCoDialog implements ClickHandler {

	private static final String CSS_CLASS = "sopeco-ExceptionDialog-Exception";
	private static final double CONTENT_MARGIN_EM = 0.5;
	private static final int WINDOW_SPACE = 200;

	public static void show(Throwable throwable) {
		new ExceptionDialog(throwable).center();
	}

	private Button closeButton;
	private Throwable throwable;
	private FlowPanel content;
	private Anchor showStack;
	private Preformatted error;

	private ExceptionDialog(Throwable pThrowable) {
		super(false);

		throwable = pThrowable;
		setHeadline("Exception was thrown");
		closeButton = addButton("Close", this);

		init();
	}

	private void init() {
		content = new FlowPanel();
		content.getElement().getStyle().setMarginTop(CONTENT_MARGIN_EM, Unit.EM);
		content.getElement().getStyle().setMarginBottom(CONTENT_MARGIN_EM, Unit.EM);

		HTML html = new HTML("The following exception was thrown:");
		html.getElement().getStyle().setFloat(Float.LEFT);
		showStack = new Anchor("show stacktrace");
		showStack.getElement().getStyle().setFloat(Float.RIGHT);
		showStack.addClickHandler(this);

		error = new Preformatted(throwable.getClass().getName() + ": " + throwable.getMessage());
		error.addStyleName(CSS_CLASS);

		content.add(html);
		content.add(showStack);
		content.add(new ClearDiv());
		content.add(error);

		setContentWidget(content);

		setDraggable(true);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == closeButton) {
			hide();
		} else if (event.getSource() == showStack) {
			String st = throwable.getClass().getName() + ": " + throwable.getMessage();
			for (StackTraceElement ste : throwable.getStackTrace()) {
				st += "\n" + ste.toString();
			}
			error.setHTML(st);
			setWidth("600px");
			error.getElement().getStyle().setOverflowX(Overflow.SCROLL);
			showStack.removeFromParent();
			center();
		}
	}

	@Override
	public void center() {
		error.getElement().getStyle().setProperty("maxHeight", (Window.getClientHeight() - WINDOW_SPACE) + "px");
		super.center();
	}
}
