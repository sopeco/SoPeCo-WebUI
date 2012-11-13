package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ParameterTreeView extends FlowPanel {

	private static final String PARAMETER_TREE_ID = "expParameterTree";

	public ParameterTreeView() {
		initialize();
	}

	/**
	 * Inits all objects.
	 */
	private void initialize() {
		getElement().setId(PARAMETER_TREE_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("envParameter"));

		getElement().appendChild(headline);
	}
}
