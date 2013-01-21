package org.sopeco.frontend.client.layout.center.execute.tabOne;

import org.sopeco.gwt.widgets.tree.TreeItem;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectionTreeItem extends TreeItem implements HasValueChangeHandlers<Boolean>, ValueChangeHandler<Boolean> {

	private CheckBox checkBox;

	public SelectionTreeItem(String pText) {
		super(pText, true);
		init(pText);
	}

	private void init(String pText) {
		Element textElement = DOM.createSpan();
		textElement.setInnerHTML(pText);

		checkBox = new CheckBox();
		checkBox.addValueChangeHandler(this);
		checkBox.setValue(true);

		getContentWrapper().add(checkBox);
		getContentWrapper().getElement().appendChild(textElement);
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		ValueChangeEvent.fire(this, event.getValue());
	}
}
