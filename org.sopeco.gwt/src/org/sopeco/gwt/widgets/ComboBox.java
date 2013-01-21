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
package org.sopeco.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Provides a ComboBox.
 * 
 * @author Marius Oehler
 * 
 */
public class ComboBox extends FlowPanel implements
		HasValueChangeHandlers<String> {

	private static final String CCS_CLASS_NAME = "spc-ComboBox";
	private static final String CCS_DROPDOWN_VIEW_NAME = "spc-ComboBox-DropDownView";
	private static final String CSS_FOCUSPANEL_NAME = "spc-ComboBox-FocusPanel";
	private static final String DROPDOWN_ICON_URL = "images/triangle.png";

	private static final int DD_ICON_WIDTH = 54;
	private static final int DEFAULT_WIDTH = 200;
	private static final int OUT_OF_SCREEN = -1000;

	private ComboBoxItemHandler comboBoxItemHandler;
	private InputFieldHandler inputfieldHandler;

	private TextBox inputField;
	private Image dropdownIcon;
	private VerticalPanel dropdownView;

	/**
	 * This element is located outside the visible screen, and serves only to
	 * catch the "blur" event of the combobox (if nothing is selected), because
	 * there is no real blur event.
	 */
	private FocusPanel dummyPanel;

	private List<FocusPanel> itemList;
	private int selectedIndex = -1;
	private boolean editable;

	private boolean userEditedText;

	public ComboBox() {
		WidgetResources.loadComboBoxCSS();
		initialize();
	}

	/**
	 * Adding a new item to the combobox.
	 * 
	 * @param itemText
	 *            text of the new item
	 */
	public void addItem(String itemText) {
		FocusPanel focusPanel = new FocusPanel();
		focusPanel.addStyleName(CSS_FOCUSPANEL_NAME);
		focusPanel.addClickHandler(getComboBoxItemHandler());
		focusPanel.addBlurHandler(getComboBoxItemHandler());
		focusPanel.addMouseOverHandler(getComboBoxItemHandler());
		focusPanel.addMouseOutHandler(getComboBoxItemHandler());

		Label newItem = new Label(itemText);

		focusPanel.add(newItem);

		dropdownView.add(focusPanel);
		itemList.add(focusPanel);

		if (selectedIndex == -1) {
			setSelectedIndex(Math.max(0, itemList.size() - 1));
		}
	}

	/**
	 * Checks if the given String is already in the comboboxlist.
	 * 
	 * @param testing
	 *            string
	 * @return
	 */
	public boolean containsString(String testing) {
		for (FocusPanel panel : itemList) {
			if (((Label) panel.getWidget()).getText().equals(testing)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes all children from the combobox.
	 */
	public void clear() {
		selectedIndex = -1;
		itemList.clear();
		dropdownView.clear();
		userEditedText = false;
		setText("");
	}

	/**
	 * Set the given text to the combobox.
	 */
	public void setText(String text) {
		inputField.setText(text);
	}

	/**
	 * Returns the selcted index.
	 * 
	 * @return index of the selected element, or -1 if nothing is selected.
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * Returns the current text of the inputfield.
	 * 
	 * @return current text
	 */
	public String getText() {
		return inputField.getText();
	}

	/**
	 * Returns whether the user has edited the text in the textfield.
	 * 
	 * @return
	 */
	public boolean hasUserEditedText() {
		return userEditedText;
	}

	/**
	 * Selects the item, that equals the given text. If no item equals the given
	 * text, nothing happens.
	 * 
	 * @param text
	 */
	public void setSelectedText(String text) {
		int count = 0;
		for (FocusPanel panel : itemList) {
			if (((Label) panel.getWidget()).getText().equals(text)) {
				setSelectedIndex(count);
				return;
			}
			count++;
		}
	}

	/**
	 * Set the combobox to the selected element.
	 * 
	 * @param i
	 *            index
	 */
	public void setSelectedIndex(int i) {
		setSelectedIndex(i, false);
	}

	/**
	 * Set the combobox to the selected element.
	 * 
	 * @param i
	 *            index
	 * @param fireEvent
	 *            whether an event will be fired
	 */
	public void setSelectedIndex(int i, boolean fireEvent) {
		if (i == -1) {
			if (selectedIndex != -1) {
				itemList.get(selectedIndex).setFocus(false);
			}
			selectedIndex = -1;
			ValueChangeEvent.fire(ComboBox.this, getText());
			return;
		} else if (i < 0 || i >= itemList.size()) {
			throw new IndexOutOfBoundsException("Index " + i
					+ " is out of the list size of " + itemList.size());
		}

		selectedIndex = i;
		Label label = (Label) itemList.get(i).getWidget();
		inputField.setText(label.getText());
		userEditedText = false;

		if (fireEvent) {
			ValueChangeEvent.fire(ComboBox.this, getText());
		}
	}

	/**
	 * Set the width of the combobox and all related elements.
	 * 
	 * @param pixel
	 */
	public void setWidth(int pixel) {
		setWidth(pixel + "px");
		dropdownView.setWidth(pixel + "px");
		inputField.setWidth((pixel - DD_ICON_WIDTH) + "px");
	}

	/**
	 * Return the handler (click, mouseover, mouseout, blur) of the items in the
	 * dropdown list.
	 * 
	 * @return handler
	 */
	private ComboBoxItemHandler getComboBoxItemHandler() {
		if (comboBoxItemHandler == null) {
			comboBoxItemHandler = new ComboBoxItemHandler();
		}
		return comboBoxItemHandler;
	}

	/**
	 * Returns the handler of the inputfield.
	 * 
	 * @return
	 */
	private InputFieldHandler getInputFieldHandler() {
		if (inputfieldHandler == null) {
			inputfieldHandler = new InputFieldHandler();
		}
		return inputfieldHandler;
	}

	/**
	 * Hides the dropdown list.
	 */
	private void hideDropdownList() {
		dropdownView.removeFromParent();
	}

	/**
	 * Initialize the necessary objects.
	 */
	private void initialize() {
		inputField = new TextBox();
		dropdownIcon = new Image(DROPDOWN_ICON_URL);
		dropdownView = new VerticalPanel();
		itemList = new ArrayList<FocusPanel>();
		userEditedText = false;
		editable = true;
		dummyPanel = new FocusPanel();

		addStyleName(CCS_CLASS_NAME);
		dropdownView.addStyleName(CCS_DROPDOWN_VIEW_NAME);

		dummyPanel.getElement().getStyle().setPosition(Position.FIXED);
		dummyPanel.getElement().getStyle().setTop(OUT_OF_SCREEN, Unit.PX);

		Element clearDiv = DOM.createDiv();
		clearDiv.setAttribute("style", "clear: both;");

		dropdownIcon.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (dropdownView.isAttached()) {
					hideDropdownList();
				} else {
					showDropdownList();
				}
			}
		});

		inputField.addValueChangeHandler(getInputFieldHandler());
		inputField.addFocusHandler(getInputFieldHandler());
		dummyPanel.addBlurHandler(getComboBoxItemHandler());

		add(inputField);
		add(dropdownIcon);
		getElement().appendChild(clearDiv);
		add(dummyPanel);

		setWidth(DEFAULT_WIDTH);
	}

	/**
	 * Sets whether the text of the combobox is editable.
	 */
	public void setEditable(boolean pEditable) {
		// inputField.setEnabled(editable);
		editable = pEditable;
	}

	/**
	 * Set the flag, that the user edited the current text in the inputfield.
	 */
	private void setUserEdited() {
		userEditedText = true;
		setSelectedIndex(-1);
	}

	/**
	 * Shows the dropdown list.
	 */
	private void showDropdownList() {
		RootPanel.get().add(dropdownView);

		int left = getElement().getAbsoluteLeft();
		int top = getElement().getAbsoluteTop() + getOffsetHeight();
		dropdownView.getElement().getStyle().setLeft(left, Unit.PX);
		dropdownView.getElement().getStyle().setTop(top, Unit.PX);

		if (selectedIndex != -1) {
			itemList.get(selectedIndex).setFocus(true);
		} else {
			dummyPanel.setFocus(true);
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * ClickHandler of the dropdown-list items.
	 * 
	 * @author Marius Oehler
	 * 
	 */
	private class ComboBoxItemHandler implements ClickHandler, BlurHandler,
			MouseOverHandler, MouseOutHandler {
		private boolean isOverElement = false;

		@Override
		public void onBlur(BlurEvent event) {
			if (!isOverElement) {
				hideDropdownList();
			}
		}

		@Override
		public void onClick(ClickEvent event) {
			int index = itemList.indexOf(event.getSource());
			setSelectedIndex(index, true);
			hideDropdownList();
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			isOverElement = false;
		}

		@Override
		public void onMouseOver(MouseOverEvent event) {
			isOverElement = true;
		}
	}

	/**
	 * The handler of the input textfield.
	 * 
	 * @author Marius Oehler
	 * 
	 */
	private class InputFieldHandler implements ValueChangeHandler<String>,
			FocusHandler {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			setUserEdited();
		}

		@Override
		public void onFocus(FocusEvent event) {
			if (!editable) {
				inputField.setFocus(false);
				showDropdownList();
			}
		}
	}
}
