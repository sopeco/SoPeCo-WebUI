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
package org.sopeco.frontend.client.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class EventControl {

	private static EventBus eventBus;
	private static List<HandlerRegistration> handlerRegistrationList;

	private EventControl() {
	}

	/**
	 * Returns an object (singleton) of the EventBus class.
	 * 
	 * @return
	 */
	public static EventBus get() {
		if (eventBus == null) {
			eventBus = GWT.create(MyEventBus.class);
		}
		return eventBus;
	}

	private static class MyEventBus extends SimpleEventBus {
		@Override
		public void fireEvent(GwtEvent<?> event) {
			super.fireEvent(event);

			Exception x = new Exception();
			GWT.log("Event fired from: " + x.getStackTrace()[1].getFileName() + " "
					+ x.getStackTrace()[1].getMethodName() + ":" + x.getStackTrace()[1].getLineNumber());
		}

		@Override
		public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
			HandlerRegistration reg = super.addHandler(type, handler);
			EventControl.addRegistration(reg);
			return reg;
		}
	}

	/**
	 * When a handler is added to the EventBus (addHandler), it will be added to
	 * the HandlerRegistrationList too.
	 * 
	 * @param reg
	 */
	private static void addRegistration(HandlerRegistration reg) {
		if (handlerRegistrationList == null) {
			handlerRegistrationList = new ArrayList<HandlerRegistration>();
		}
		handlerRegistrationList.add(reg);
	}

	/**
	 * Removes all registered handlers from the EventBus.
	 */
	public static void removeAllHandler() {
		if (handlerRegistrationList == null) {
			return;
		}
		List<HandlerRegistration> tempList = new ArrayList<HandlerRegistration>(handlerRegistrationList);
		for (HandlerRegistration reg : tempList) {
			reg.removeHandler();
			handlerRegistrationList.remove(reg);
		}
	}
}
