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
package org.sopeco.webui.client.resources;

import com.google.gwt.i18n.client.Constants;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface LanguageConstants extends Constants {

	String executeExperiment();

	String scheduleExperiment();

	String loginSelectAccount();

	String connect();

	String noAccountsAvailable();

	String codeDownloadFailed();

	String addNewAccount();

	String addAccount();

	String cancel();

	String accountname();

	String password();

	String confirmPassword();

	String optional();

	String database();

	String host();

	String port();

	String show();

	String hide();

	String settings();

	String delete();

	String account();

	String wrongPassword();

	String continueString();

	String addExperimentSeries();

	String selectLanguage();
	
	String ok();
	
	String measurementEnvironmentController();
	
	String controller();
	
	String protocol();
	
	String mecApplication();
	
	String ipAddress();
	
	String mecSettingDescription();
	
	String mecAppToken();
	
	String status();
	
	String noMecStarted();
	
	String noMecOnline();
	
	String create();
	
	String newChart();
	
	String dataProcessing();
	
	String numberOfPoints();
}
