package org.sopeco.frontend.client.layout.center;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NoScenario extends CenterPanel {

	public NoScenario() {
		Element wrapper = DOM.createDiv();
		wrapper.getStyle().setPaddingLeft(1, Unit.EM);
		wrapper.getStyle().setPaddingRight(1, Unit.EM);

		Element h3 = DOM.createElement("h3");
		h3.setInnerHTML(R.get("note"));

		Element p = DOM.createElement("p");
		p.setInnerHTML(R.get("createScenario"));

		wrapper.appendChild(h3);
		wrapper.appendChild(p);

		getElement().appendChild(wrapper);
	}
}
