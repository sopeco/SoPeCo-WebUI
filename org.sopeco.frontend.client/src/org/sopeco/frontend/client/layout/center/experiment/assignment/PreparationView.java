package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.widget.grid.EditGrid;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreparationView extends FlowPanel {

	private static final String NO_ASSIGNMENTS_CSS_CLASS = "noAssignments";
	
	private Headline headline;
	private EditGrid grid;
	private HTML htmlNoAssignments;
	
	public PreparationView() {
		init();
	}

	private void init() {
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setPadding(1, Unit.EM);

		headline = new Headline(R.get("prepAssignments"));
		headline.getElement().getStyle().setMarginBottom(1, Unit.EM);

		grid = new EditGrid(1, 4);
		
		htmlNoAssignments = new HTML(R.get("noprepAssignments"));
		htmlNoAssignments.setVisible(false);
		htmlNoAssignments.addStyleName(NO_ASSIGNMENTS_CSS_CLASS);

		add(headline);
		add(grid);
		add(htmlNoAssignments);
	}

	public EditGrid getGrid() {
		return grid;
	}

	public HTML getHtmlNoAssignments() {
		return htmlNoAssignments;
	}
}
