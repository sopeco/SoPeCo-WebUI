package org.sopeco.frontend.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("startupService")
public interface StartupService extends RemoteService  {
	boolean start();
}
