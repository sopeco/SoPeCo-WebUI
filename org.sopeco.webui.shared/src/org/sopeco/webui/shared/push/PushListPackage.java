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
package org.sopeco.webui.shared.push;

import java.util.ArrayList;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushListPackage extends PushPackage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PushSerializable> attachment = new ArrayList<PushSerializable>();

	/**
	 * Returns the attachment.
	 * 
	 * @return the attachment
	 */
	public ArrayList<PushSerializable> getAttachment() {
		return attachment;
	}

	/**
	 * Returns the attached list. The elements are cast to the given class.
	 * 
	 * @return the attachment
	 */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAttachment(Class<T> clazz) {
		return (ArrayList<T>) attachment;
	}

	/**
	 * Sets the given ArrayList as the attached list.
	 * 
	 * @param pAttachment
	 *            the attachment to set
	 */
	public void setAttachment(ArrayList<PushSerializable> pAttachment) {
		attachment = pAttachment;
	}
}
