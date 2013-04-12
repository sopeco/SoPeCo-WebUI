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
package org.sopeco.webui.client.rpc;

import java.util.List;

import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.shared.entities.AccountDetails;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * RPCs for accessing the database.
 * 
 * @author D059149
 * 
 */
@RemoteServiceRelativePath("databaseManagerRPC")
public interface DatabaseManagerRPC extends RemoteService {

	/**
	 * Get a list of all available databases.
	 * 
	 * @return List of all databases
	 */
	DatabaseInstance getDatabase();

	/**
	 * Add a new database in the metadatabase.
	 * 
	 * @param databaseInstance
	 *            of the new database
	 * @return true if succeeded
	 */
	boolean addDatabase(DatabaseInstance databaseInstance, String passwd);

	/**
	 * Removes the committed database.
	 * 
	 * @param database
	 *            which will be removed
	 * @return true if succeeded
	 */
	boolean removeDatabase(DatabaseInstance databaseInstance);

	/**
	 * Select a database.
	 * 
	 * @param database
	 *            which will be selected
	 * @return true if succeeded
	 */
	boolean login(String dbName, String passwd);

	boolean checkPassword(String dbName, String passwd);
	
	boolean accountExists(String accountName);

	AccountDetails getAccountDetails();
	
	void storeAccountDetails(AccountDetails details);
}
