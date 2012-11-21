package org.sopeco.frontend.client.log;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.sopeco.frontend.client.layout.dialog.LogDialog;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class LogHandler extends Handler {

	private static LogHandler handler;

	private LogHandler() {
	}

	@Override
	public void close() {
		// No action needed
	}

	@Override
	public void flush() {
		// No action needed
	}

	@Override
	public void publish(LogRecord log) {
		LogDialog.pushLogRecord(log);
	}

	public static LogHandler get() {
		if (handler == null) {
			handler = new LogHandler();
		}
		return handler;
	}
}
