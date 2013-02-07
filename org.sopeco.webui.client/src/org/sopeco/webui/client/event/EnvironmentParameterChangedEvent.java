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
package org.sopeco.webui.client.event;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.event.handler.EnvironmentParameterChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentParameterChangedEvent extends GwtEvent<EnvironmentParameterChangedEventHandler> {

	/** */
	public static final Type<EnvironmentParameterChangedEventHandler> TYPE = new Type<EnvironmentParameterChangedEventHandler>();
	private ParameterDefinition oldParameter, newParameter;

	public EnvironmentParameterChangedEvent(ParameterDefinition pOldparameter, ParameterDefinition pNewParameter) {
		oldParameter = pOldparameter;
		newParameter = pNewParameter;
	}

	/**
	 * @return the oldParameter
	 */
	public ParameterDefinition getOldParameter() {
		return oldParameter;
	}

	/**
	 * @return the newParameter
	 */
	public ParameterDefinition getNewParameter() {
		return newParameter;
	}

	@Override
	public Type<EnvironmentParameterChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EnvironmentParameterChangedEventHandler handler) {
		handler.onEnvironmentParameterChangedEvent(this);
	}

}
