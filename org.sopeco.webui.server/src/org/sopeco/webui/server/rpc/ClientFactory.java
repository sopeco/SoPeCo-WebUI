package org.sopeco.webui.server.rpc;

import org.sopeco.service.rest.json.CustomObjectWrapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * This class is used to create {@link WebResource} with the Jersey {@link Client}.
 * As we don't want always to create a new {@link Client}, it's once created and
 * with a given URL the corresponding {@link WebResource} returned.
 * 
 * @author Peter Merkert
 */
public class ClientFactory {

	private static ClientFactory clientFactory 	= null;
	private static Client client 				= null;
	private static String URLprefix				= "http://localhost:8080";
	private static String URLsplitter			= "/";
	
	/**
	 * Private constructor for singleton.
	 */
	private ClientFactory() {
    	client = Client.create(createClientConfig());
	}
	
	/**
	 * Singleton constructor to create a {@link ClientFactory} build 
	 * {@link WebResource}s.
	 * 
	 * @return a {@link ClientFactory} to create a {@link WebResource}
	 */
	public static ClientFactory getInstance() {
		
		if (clientFactory == null) {
			clientFactory = new ClientFactory();
		}
		
		return clientFactory;
		
	}
	
	/**
	 * Returns the {@link WebResource} to do requests. Already sticks the prefix 
	 * <code>http://localhost:8080/</code> to the URL.
	 * 
	 * @param URL 	the URL where the service is
	 * @return		the {@link WebResource} to do requests
	 */
	public WebResource getClient(String... url) {
		String myurl = "";
		
		for (String suburl : url) {
			myurl += URLsplitter + suburl;
		}
		
		return client.resource(URLprefix + myurl);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////// HELPER ///////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sets the client config for the client. The method adds a special {@link CustomObjectWrapper}
	 * to the normal Jackson wrapper for JSON.<br />
	 * This method is called by the constructor.
	 * 
	 * @return ClientConfig to work with JSON
	 */
	private ClientConfig createClientConfig() {
		ClientConfig config = new DefaultClientConfig();
	    // the CustomObjectWrapper from SPC SL has configuration
		// which allows to (de)serialize all the interface objects
		// from and to JSON
	    config.getClasses().add(CustomObjectWrapper.class);
	    return config;
	}
}
