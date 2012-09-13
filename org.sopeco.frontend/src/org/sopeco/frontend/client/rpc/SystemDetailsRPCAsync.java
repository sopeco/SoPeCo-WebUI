package org.sopeco.frontend.client.rpc;

import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SystemDetailsRPCAsync {

	void getMetaDatabaseDetails(AsyncCallback<HashMap<String, String>> callback);

}
