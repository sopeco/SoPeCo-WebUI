package org.sopeco.frontend.client.helper.serverstatus;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.popups.Warning;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Serverstatus {

	private static List<Deactivatable> deactivatableList = new ArrayList<Deactivatable>();

	private Serverstatus() {
	}

	public static void setOffline() {
		Warning.showWarning(R.get("noDbConnection"));

		for (Deactivatable d : deactivatableList) {
			d.goOffline();
		}
	}

	public static void setOnline() {
		Warning.hideWarning();

		for (Deactivatable d : deactivatableList) {
			d.goOnline();
		}
	}

	public static void register(Deactivatable deactivatable) {
		deactivatableList.add(deactivatable);
	}

	public static void remove(Deactivatable deactivatable) {
		deactivatableList.remove(deactivatable);
	}
}
