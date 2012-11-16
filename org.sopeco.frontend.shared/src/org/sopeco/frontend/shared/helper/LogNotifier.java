package org.sopeco.frontend.shared.helper;

import org.sopeco.frontend.shared.helper.UiLog.LogMessage;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface LogNotifier {
	void onNewLog(LogMessage log);
}
