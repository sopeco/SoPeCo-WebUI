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
package org.sopeco.webui.client.animation;

import com.google.gwt.animation.client.Animation;

/**
 * 
 * Wrapper class of the animations.
 * 
 * @author Marius Oehler
 * 
 */
public abstract class SuperAnimation extends Animation {

	/**
	 * Longest duration: 1200ms.
	 */
	public static final int SLOW = 1200;
	/**
	 * Default duration: 600ms.
	 */
	public static final int DEFAULT = 600;
	/**
	 * Shortest duration: 300ms.
	 */
	public static final int FAST = 300;

	private ICompleteHandler completeHandler;

	@Override
	protected abstract void onUpdate(double progress);

	@Override
	protected abstract void onComplete();

	protected void setCompleteHandler(ICompleteHandler handler) {
		completeHandler = handler;
	}

	/**
	 * Handler which will be executed when the animation ends.
	 * 
	 * @return completeHandler
	 */
	protected void executeCompleteHandler() {
		if (completeHandler != null) {
			completeHandler.onComplete();
		}
	}
}
