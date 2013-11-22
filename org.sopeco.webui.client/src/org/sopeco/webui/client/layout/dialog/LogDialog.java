/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout.dialog;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.sopeco.gwt.widgets.Preformatted;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SoPeCoDialog;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class LogDialog {

	private static SoPeCoDialog dialog;
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
		scrollPanel.getElement().getStyle().setBorderColor("#CCC");
		scrollPanel.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		scrollPanel.getElement().getStyle().setBorderWidth(1, Unit.PX);
		scrollPanel.getElement().getStyle().setMarginTop(0.5, Unit.EM);
		scrollPanel.getElement().getStyle().setMarginBottom(0.5, Unit.EM);
		preLog.getElement().getStyle().setFontSize(11, Unit.PX);

		dialog = new SoPeCoDialog(false);
		dialog.setHeadline(R.lang.logDialog());
		dialog.setContentWidget(scrollPanel);
		dialog.setDraggable(true);
		dialog.setWindowMargin(200);
		dialog.setWidth("600px");
		dialog.addButton("Close", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
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
