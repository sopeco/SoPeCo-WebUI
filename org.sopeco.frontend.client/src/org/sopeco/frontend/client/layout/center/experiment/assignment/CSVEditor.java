package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.widget.Headline;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CSVEditor extends DialogBox implements HasValueChangeHandlers<String>, ValueChangeHandler<String>,
		KeyPressHandler, ClickHandler {

	private static final String EDITOR_CSS_CLASS = "csvEditor";
	private static final String LIST_CSS_CLASS = "csvList";
	private static final String LIST_ELEMENT_CSS_CLASS = "listElement";
	private static final String SEPERATOR = ",";
	private String values;

	private FlexTable boxLayout;
	private FlowPanel valueListPanel;
	private Headline headline;
	private Button btnSave, btnCancel, btnClear;

	private List<ListElement> listElements;
	private ListElement newRowElement;

	public CSVEditor() {
		FrontEndResources.loadCSVEditorCSS();

		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		addStyleName(EDITOR_CSS_CLASS);

		setGlassEnabled(true);
		setModal(true);
		setAutoHideEnabled(false);

		listElements = new ArrayList<ListElement>();

		boxLayout = new FlexTable();

		headline = new Headline(R.get("values"));
		headline.getElement().getStyle().setMarginTop(0, Unit.PX);
		headline.getElement().getStyle().setMarginBottom(0, Unit.PX);

		valueListPanel = new FlowPanel();
		valueListPanel.addStyleName(LIST_CSS_CLASS);

		btnSave = new Button(R.get("Ok"));
		btnCancel = new Button(R.get("Cancel"));
		btnClear = new Button(R.get("Clear"));
		btnCancel.addClickHandler(this);
		btnSave.addClickHandler(this);
		btnClear.addClickHandler(this);
		btnSave.setWidth("100%");
		btnCancel.setWidth("100%");
		btnClear.setWidth("100%");

		newRowElement = new ListElement("", false);
		newRowElement.getElement().getStyle().setBackgroundColor("#F3F5F7");
		newRowElement.getTextbox().addValueChangeHandler(this);
		newRowElement.getTextbox().addKeyPressHandler(this);
		newRowElement.setDefaultValue("Add values..");

		boxLayout.setWidget(0, 0, headline);
		boxLayout.setWidget(1, 0, newRowElement);
		boxLayout.setWidget(2, 0, valueListPanel);
		boxLayout.setWidget(3, 0, btnSave);
		boxLayout.setWidget(3, 1, btnCancel);
		boxLayout.setWidget(3, 2, btnClear);

		boxLayout.getFlexCellFormatter().setColSpan(0, 0, 3);
		boxLayout.getFlexCellFormatter().setColSpan(1, 0, 3);
		boxLayout.getFlexCellFormatter().setColSpan(2, 0, 3);

		add(boxLayout);
	}

	/**
	 * @param values
	 *            the value to set
	 */
	public void setValue(String csvValue) {
		if (csvValue == null) {
			values = "";
		} else {
			values = csvValue;
		}
		fillList();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		newValueInserted();
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER) {
			newValueInserted();
		}
	}

	private void newValueInserted() {
		String newElementText = newRowElement.getTextbox().getText();

		for (String sepVal : newElementText.split(SEPERATOR)) {
			if (sepVal.isEmpty()) {
				return;
			}

			newRowElement.getTextbox().setText("");

			ListElement element = new ListElement(sepVal);
			addValueRow(element);
		}
	}

	private void fillList() {
		valueListPanel.clear();
		listElements.clear();

		String[] splittedArray = values.split(SEPERATOR);
		for (String val : splittedArray) {
			if (val.isEmpty()) {
				continue;
			}
			addValueRow(val);
		}
	}

	private void addValueRow(String value) {
		addValueRow(new ListElement(value));
	}

	private void addValueRow(ListElement lElement) {
		listElements.add(lElement);
		valueListPanel.add(lElement);

		center();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnCancel) {
			hide();
		} else if (event.getSource() == btnSave) {
			ValueChangeEvent.fire(this, getCSValue());
			hide();
		} else if (event.getSource() == btnClear) {
			for (ListElement el : listElements) {
				el.removeFromParent();
			}
			listElements.clear();
			center();
		}
	}

	private String getCSValue() {
		StringBuffer buffer = new StringBuffer();

		boolean firstValue = true;
		for (ListElement el : listElements) {
			if (firstValue) {
				firstValue = false;
			} else {
				buffer.append(SEPERATOR);
			}

			buffer.append(el.getTextbox().getText());
		}

		return buffer.toString();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * 
	 * @author Marius Oehler
	 * 
	 */
	private class ListElement extends FlowPanel implements ClickHandler, FocusHandler, BlurHandler {
		private Image removeImage;
		private TextBox textbox;

		private String defaultValue;

		public ListElement(String value) {
			this(value, true);
		}

		public ListElement(String value, boolean hasRemoveImage) {
			addStyleName(LIST_ELEMENT_CSS_CLASS);

			defaultValue = "";

			textbox = new TextBox();
			textbox.setText(value);
			textbox.addBlurHandler(this);
			textbox.addFocusHandler(this);
			add(textbox);

			if (hasRemoveImage) {
				removeImage = new Image("images/trash.png");
				removeImage.getElement().getStyle().setCursor(Cursor.POINTER);
				removeImage.addClickHandler(this);
				add(removeImage);
			}
		}

		/**
		 * @param defValue
		 *            the defaultValue to set
		 */
		public void setDefaultValue(String defValue) {
			defaultValue = defValue;

			if (textbox.getText().isEmpty()) {
				textbox.setText(defaultValue);
				textbox.getElement().getStyle().setColor("gray");
			}
		}

		/**
		 * @return the textbox
		 */
		public TextBox getTextbox() {
			return textbox;
		}

		@Override
		public void onClick(ClickEvent event) {
			listElements.remove(this);
			removeFromParent();
			center();
		}

		@Override
		public void onBlur(BlurEvent event) {
			if (textbox.getText().isEmpty()) {
				textbox.setText(defaultValue);
				textbox.getElement().getStyle().setColor("gray");
			}
		}

		@Override
		public void onFocus(FocusEvent event) {
			if (textbox.getText().equals(defaultValue)) {
				textbox.setText("");
				textbox.getElement().getStyle().setColor("black");
			}
		}
	}
}
