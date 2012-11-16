package org.sopeco.frontend.client.layout.dialog;

import java.util.ListIterator;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.shared.helper.LogNotifier;
import org.sopeco.frontend.shared.helper.UiLog;
import org.sopeco.frontend.shared.helper.UiLog.Level;
import org.sopeco.frontend.shared.helper.UiLog.LogMessage;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Preformatted;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class LogDialog {

	private static DialogBox dialog;
	private static Preformatted preLog;
	private static final int WIDTH = 500, HEIGHT = 300;
	private static ScrollPanel scrollPanel;
	private static LogNotifier notifier;

	private LogDialog() {
	}

	/**
	 *  
	 */
	private static void updateLogArea() {
		// ListIterator<LogMessage> iter =
		// UiLog.getLogList().listIterator(UiLog.getLogList().size());
		ListIterator<LogMessage> iter = UiLog.getLogList().listIterator(0);

		StringBuffer buffer = new StringBuffer();
		while (iter.hasNext()) {
			LogMessage log = iter.next();
			if (log.getLevel().ordinal() >= Level.WARNING.ordinal()) {
				buffer.append("<span style=\"color:red;\">");
			}
			buffer.append(log.getMessage());
			if (log.getLevel().ordinal() >= Level.WARNING.ordinal()) {
				buffer.append("</span>");
			}
			buffer.append("\n");
		}

		preLog.setHTML(buffer.toString());
		scrollPanel.scrollToBottom();
	}

	/**
	 * 
	 */
	private static void initDialog() {
		preLog = new Preformatted();

		scrollPanel = new ScrollPanel();
		scrollPanel.add(preLog);
		scrollPanel.setSize(WIDTH + "px", HEIGHT + "px");
		scrollPanel.getElement().getStyle().setBorderColor("#CCC");
		scrollPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		scrollPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);

		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(0.5, Unit.EM);

		Headline headline = new Headline(R.get("logDialog"));
		headline.getElement().getStyle().setMargin(0, Unit.PX);
		headline.getElement().getStyle().setMarginBottom(10, Unit.PX);

		Button btnClose = new Button(R.get("Close"));
		btnClose.getElement().getStyle().setMarginTop(0.5, Unit.EM);
		btnClose.getElement().getStyle().setFloat(Float.RIGHT);
		btnClose.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LogDialog.hide();
			}
		});

		panel.add(headline);
		panel.add(scrollPanel);
		panel.add(btnClose);

		dialog = new DialogBox(false, false);
		dialog.setGlassEnabled(false);
		dialog.add(panel);

		notifier = new LogNotifier() {
			@Override
			public void onNewLog(LogMessage log) {
				if (dialog.isShowing()) {
					updateLogArea();
				}
			}
		};

		UiLog.addLogNotifier(notifier);
	}

	/**
	 * 
	 */
	public static void show() {
		if (dialog == null) {
			initDialog();
		}

		updateLogArea();
		dialog.center();
		scrollPanel.scrollToBottom();
	}

	/**
	 * 
	 */
	public static void hide() {
		if (dialog == null) {
			return;
		}

		dialog.hide();
	}
}
