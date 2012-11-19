package org.sopeco.frontend.client.layout.environment;

import org.sopeco.frontend.client.layout.environment.EnvironmentTree.ECheckBox;
import org.sopeco.gwt.widgets.tree.TreeItem;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvTreeItem extends TreeItem implements ValueChangeHandler<Boolean> {

	private static final String CSS_LEAF_CLASS = "envTreeItemLeaf";
	private Element textElement;
	private CheckBox firstCheckBox, secondCheckBox;
	private boolean hasSecondCheckbox;
	private EnvironmentTree environmentTree;
	private ParameterDefinition parameter;

	public EnvTreeItem(EnvironmentTree pEnvironmentTree, ParameterDefinition pPparameter, boolean pHasSecondCheckbox) {
		super(pPparameter.getName());
		hasSecondCheckbox = pHasSecondCheckbox;
		environmentTree = pEnvironmentTree;

		parameter = pPparameter;

		addCheckBoxes();
	}

	public void setFirstCheckboxValue(boolean value) {
		firstCheckBox.setValue(value);
	}

	public void setSecondCheckboxValue(boolean value) {
		secondCheckBox.setValue(value);
	}

	@Override
	protected void initialize(boolean noContent) {
		addStyleName(CSS_LEAF_CLASS);
		setHeight("26px");

		textElement = DOM.createSpan();
		textElement.setInnerHTML(getText());
		textElement.setTitle(getText());
		textElement.addClassName("text");

		getElement().appendChild(textElement);
	}

	private void addCheckBoxes() {
		firstCheckBox = new CheckBox();
		secondCheckBox = new CheckBox();

		firstCheckBox.addValueChangeHandler(this);
		secondCheckBox.addValueChangeHandler(this);

		add(firstCheckBox);
		if (hasSecondCheckbox) {
			add(secondCheckBox);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		ECheckBox checkbox;
		if (event.getSource() == firstCheckBox) {
			checkbox = ECheckBox.FIRST;
		} else {
			checkbox = ECheckBox.SECOND;
		}
		environmentTree.onClick(checkbox, this, event.getValue());

	}

	/**
	 * @return the parameter
	 */
	public ParameterDefinition getParameter() {
		return parameter;
	}

}
