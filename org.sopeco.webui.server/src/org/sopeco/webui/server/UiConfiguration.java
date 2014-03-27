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
package org.sopeco.webui.server;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class UiConfiguration {
	

	// settings for JPA database
	public static final String PERSISTENCE_HOST 	= "localhost";
	public static final int PERSISTENCE_PORT 		= 1527;
	public static final String PERSISTENCE_NAME 	= "sopeco-webui";
	public static final String PERSISTENCE_USER 	= "sopeco_webui";
	public static final String PERSISTENCE_PASSWORD = "sopeco_webui";
	
	
	public static final String USER_TIMEOUT = "sopeco.ui.userTimeout";
	public static final String TIMEOUT_CHECK_INTERVAL = "sopeco.ui.timeoutCheckInterval";

	public static final String META_DATA_HOST = "sopeco.config.persistence.metaServer.host";
	public static final String META_DATA_PORT = "sopeco.config.persistence.metaServer.port";

	public static final String SOPECO_UI_DATABASE_NAME = "sopeco.ui.persistence.name";
	public static final String SOPECO_UI_DATABASE_USER = "sopeco.ui.persistence.user";
	public static final String SOPECO_UI_DATABASE_PASSWORD = "sopeco.ui.persistence.password";

	public static final String SOPECO_UI_USERTIMEOUT = "sopeco.ui.userTimeout";

	public static final String SOPECO_CONFIG_MEC_LISTENER_PORT = "sopeco.config.mec.listener.port";
}
