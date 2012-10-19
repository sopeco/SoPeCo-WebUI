package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.helper.Extension;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("extensionRPC")
public interface ExtensionRPC extends RemoteService {
	Extension getExtension();
}
