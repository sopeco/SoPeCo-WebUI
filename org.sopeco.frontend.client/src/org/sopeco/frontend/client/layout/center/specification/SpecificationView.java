package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.CenterPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
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
	private HorizontalPanel bottomPanel;
	private FlowPanel assignmentListPanel, environmentTreePanel;

	private static final String TOP_PANEL_ID = "specificationTopPanel";
	private static final String ENVIRONMENT_PANEL_ID = "environmentTreePanel";

	public SpecificationView(AssignmentView assignmentView) {
		assignmentListPanel = assignmentView;

		initialize();
	}

	/**
	 * Initializing the widgets.
	 */
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

		bottomPanel = new HorizontalPanel();
		bottomPanel.getElement().getStyle().setWidth(100, Unit.PCT);

		environmentTreePanel = new FlowPanel();
		environmentTreePanel.getElement().setId(ENVIRONMENT_PANEL_ID);
		environmentTreePanel.getElement().getStyle().setWidth(0, Unit.PX);

		// ADDING
		bottomPanel.add(assignmentListPanel);
		bottomPanel.add(environmentTreePanel);

		add(topPanel);
		add(bottomPanel);
	}

	/**
	 * Setting the given string to the specificationName-Textbox.
	 * 
	 * @param name
	 */
	public void setSpecificationName(String name) {
		textboxName.setText(name);
	}

	/**
	 * Returns the textbox of the specification name.
	 * 
	 * @return
	 */
	public TextBox getSpecificationNameTextbox() {
		return textboxName;
	}
}
