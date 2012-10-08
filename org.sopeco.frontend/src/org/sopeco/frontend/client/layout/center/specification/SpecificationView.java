package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationView extends CenterPanel {

	private HorizontalPanel topPanel;
	private TextBox textboxName;

	private static final String TOP_PANEL_ID = "specificationTopPanel";

	public SpecificationView() {
		initialize();
	}

	private void initialize() {
		topPanel = new HorizontalPanel();
		topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		topPanel.getElement().setId(TOP_PANEL_ID);

		Label nameLabel = new Label(R.get("name") + ":");
		nameLabel.addStyleName("spc-Label");

		textboxName = new TextBox();
		textboxName.addStyleName("spc-TextBox");

		topPanel.add(nameLabel);
		topPanel.add(textboxName);

		topPanel.setCellWidth(nameLabel, "100");

		add(topPanel);
	}

	public void setSpecificationName(String name) {
		textboxName.setText(name);
	}
}
