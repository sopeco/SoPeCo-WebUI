package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.widget.ComboBox;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentExtensionView extends FlowPanel {

	private static final String EXTENSION_VIEW_CSS_CLASS = "experimentExtensionView";
	private static final int DEFAULT_MARGIN = 20;

	private ComboBox combobox;
	private Element headline;
	private FlexTable configTable;

	private int width;

	public ExperimentExtensionView(int pxWidth) {
		width = pxWidth;

		initialize();
	}

	/**
	 * Inits all necessary objects.
	 */
	private void initialize() {
		addStyleName(EXTENSION_VIEW_CSS_CLASS);

		headline = DOM.createElement("h3");
		combobox = new ComboBox();
		configTable = new FlexTable();

		combobox.setWidth(width - 2 * DEFAULT_MARGIN);
		combobox.getElement().getStyle().setMarginLeft(DEFAULT_MARGIN, Unit.PX);
		combobox.setEditable(false);

		getElement().appendChild(headline);
		add(combobox);
		add(configTable);
	}

	/**
	 * Returns the headline H3-Element.
	 * 
	 * @return headline
	 */
	public Element getHeadline() {
		return headline;
	}

	/**
	 * @return the combobox
	 */
	public ComboBox getCombobox() {
		return combobox;
	}

	/**
	 * @return the configTable
	 */
	public FlexTable getConfigTable() {
		return configTable;
	}

	public void addConfigRow(String labelText, String value) {
		HTML htmlLabel = new HTML(labelText);
		TextBox textboxValue = new TextBox();

		textboxValue.setText(value);

		int row = configTable.getRowCount();

		configTable.setWidget(row, 0, htmlLabel);
		configTable.setWidget(row, 1, textboxValue);
	}
}
