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
package org.sopeco.webui.client.helper;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.webui.shared.helper.Metering;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Aligns a specified css property at a amount of objects.
 * 
 * @author Marius Oehler
 * 
 */
public class ElementPropertyAligner {

	private List<Element> elementList;
	private List<Element[]> relatedElementsList;

	int waiting = 0;
	
	public ElementPropertyAligner() {
		elementList = new ArrayList<Element>();
		relatedElementsList = new ArrayList<Element[]>();
	}

	/**
	 * Adds an element to the elementList.
	 * 
	 * @param element
	 */
	public void addElement(Element element) {
		elementList.add(element);
		relatedElementsList.add(new Element[0]);
	}

	/**
	 * Adds an element to the elementList.
	 * 
	 * @param element
	 */
	public void addElement(Element element, Element... relatedElements) {
		elementList.add(element);
		relatedElementsList.add(relatedElements);
	}

	/**
	 * Sets the width of the objects in the elementList to the same value
	 * (largest width in the list).
	 */
	public void alignWith() {
		double metering = Metering.start();

		int widestHTML = 0;
		for (Element element : elementList) {
			element.getStyle().clearWidth();

			if (element.getClientWidth() > widestHTML) {
				widestHTML = element.getClientWidth();
			}
		}

		for (Element element : elementList) {
			element.getStyle().setWidth(widestHTML, Unit.PX);
		}

		Metering.stop(metering);
	}

	/**
	 * The elements fit their width in a way that all "element-groups" have the
	 * same overall width.
	 */
	public void offsetWidth() {
		double metering = Metering.start();

		int widestHTML = 0;
		for (int i = 0; i < elementList.size(); i++) {
			elementList.get(i).getStyle().clearWidth();

			int tempWidth = elementList.get(i).getOffsetWidth();

			for (Element element : relatedElementsList.get(i)) {
				tempWidth += element.getClientWidth();
			}

			if (tempWidth > widestHTML) {
				widestHTML = tempWidth;
			}
		}

		for (int i = 0; i < elementList.size(); i++) {
			int widthToSet = widestHTML;
			for (Element element : relatedElementsList.get(i)) {
				widthToSet -= element.getClientWidth();
			}
			elementList.get(i).getStyle().setWidth(widthToSet, Unit.PX);
		}

		Metering.stop(metering);
	}
}
