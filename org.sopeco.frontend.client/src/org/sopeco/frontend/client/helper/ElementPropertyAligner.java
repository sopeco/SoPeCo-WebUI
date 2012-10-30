package org.sopeco.frontend.client.helper;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.shared.helper.Metering;

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
