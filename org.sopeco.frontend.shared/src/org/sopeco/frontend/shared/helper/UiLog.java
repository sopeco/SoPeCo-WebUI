package org.sopeco.frontend.shared.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UiLog {

	/** */
	public enum Level {
		/** */
		DEBUG, INFO, WARNING, ERROR;
	}

	private static Level currentLevel = Level.DEBUG;
	private static List<LogNotifier> logNotifier = new ArrayList<LogNotifier>();

	/**
	 * Stores all log messages.
	 */
	private static List<LogMessage> logs = new LinkedList<LogMessage>();

	private UiLog() {
	}

	public static void addLogNotifier(LogNotifier notifier) {
		logNotifier.add(notifier);
	}

	/**
	 * @param level
	 *            the currentLevel to set
	 */
	public static void setLevel(Level level) {
		UiLog.currentLevel = level;
	}

	/**
	 * Returns a copy of the list, that contains all log messages.
	 * 
	 * @return
	 */
	public static List<LogMessage> getLogList() {
		return new ArrayList<LogMessage>(logs);
	}

	/**
	 * 
	 * @param level
	 * @param text
	 */
	private static void log(Level level, String text) {
		if (currentLevel.ordinal() <= level.ordinal()) {

			Exception ex = new Exception();
			StackTraceElement stackTop = ex.getStackTrace()[2];

			LogMessage log = LogMessage.createLog(level, text, stackTop.getClassName(), stackTop.getLineNumber());
			logs.add(log);

			if (level.ordinal() >= Level.WARNING.ordinal()) {
				System.err.println(log.getMessage());
			} else {
				System.out.println(log.getMessage());
			}

			for (LogNotifier n : logNotifier) {
				n.onNewLog(log);
			}
		}
	}

	/**
	 * 
	 * @param text
	 */
	public static void debug(String text) {
		log(Level.DEBUG, text);
	}

	public static void info(String text) {
		log(Level.INFO, text);
	}

	public static void warn(String text) {
		log(Level.WARNING, text);
	}

	public static void error(String text) {
		log(Level.ERROR, text);
	}

	/**
	 * 
	 *
	 */
	public static final class LogMessage {
		private long time;
		private String text;
		private Level level;
		private String className;
		private int line;

		static LogMessage createLog(Level lvl, String txt, String classNme, int lne) {
			return new LogMessage(lvl, txt, classNme, lne);
		}

		private LogMessage(Level lvl, String txt, String classNme, int lne) {
			text = txt;
			level = lvl;
			time = System.currentTimeMillis();
			className = classNme;
			line = lne;
		}

		/**
		 * 
		 * @return
		 */
		public String getMessage() {
			return getMessage("\n");
		}

		public String getMessage(String lineSeparator) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(level.toString());
			buffer.append(" ");
			buffer.append(new Date(time));
			buffer.append(" - ");
			buffer.append(className);
			buffer.append(":" + line);
			buffer.append(lineSeparator);
			buffer.append("\t");
			buffer.append(text);

			return buffer.toString();
		}

		/**
		 * @return the time
		 */
		public long getTime() {
			return time;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @return the level
		 */
		public Level getLevel() {
			return level;
		}

		/**
		 * @return the className
		 */
		public String getClassName() {
			return className;
		}

		/**
		 * @return the line
		 */
		public int getLine() {
			return line;
		}

	}
}
