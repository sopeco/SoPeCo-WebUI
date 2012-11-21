package org.sopeco.frontend.client.layout.dialog;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.sopeco.frontend.client.R;
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

	private static List<LogRecord> logList;
	private static StringBuffer logText = new StringBuffer();

	private LogDialog() {
	}

	/**
	 * 
	 */
	private static void initDialog() {
		logList = new LinkedList<LogRecord>();
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
	}

	public static void pushLogRecord(LogRecord log) {
		if (dialog == null) {
			initDialog();
		}
		logList.add(log);

		if (log.getLevel().equals(Level.WARNING) || log.getLevel().equals(Level.SEVERE)) {
			logText.append("<span style=\"color:red;\">");
		}

		logText.append(log.getLevel());
		logText.append(" ");
		logText.append(new Date(log.getMillis()).toString());
		logText.append(" - ");
		logText.append(log.getLoggerName());
		logText.append("\n\t");
		logText.append(log.getMessage());
		logText.append("\n");

		if (log.getLevel().equals(Level.INFO) || log.getLevel().equals(Level.SEVERE)) {
			logText.append("</span>");
		}

		preLog.setHTML(logText.toString());
		scrollPanel.scrollToBottom();
	}

	/**
	 * 
	 */
	public static void show() {
		if (dialog == null) {
			initDialog();
		}

		// updateLogArea();
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
