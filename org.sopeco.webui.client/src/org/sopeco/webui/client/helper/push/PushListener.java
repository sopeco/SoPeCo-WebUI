package org.sopeco.webui.client.helper.push;

import org.sopeco.webui.shared.push.PushPackage;

public interface PushListener {

	void receive(PushPackage pushPackage);

}
