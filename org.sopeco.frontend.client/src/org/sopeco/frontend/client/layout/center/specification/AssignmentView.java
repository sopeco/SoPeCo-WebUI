package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentView extends FlowPanel {

	private static final String ASSIGNMENT_PANEL_ID = "assignmentListPanel";
	private static final String ASSIGNMENT_PANEL_WRAPPER_ID = "assignmentListPanelWrapper";

	private static final String ASSIGNMENT_ITEM_TABLE = "assignmentItemTable";
	private static final String ASSIGNMENT_ITEM_TABLE_COL_1 = "columnFirst";
	private static final String ASSIGNMENT_ITEM_TABLE_COL_2 = "columnSecond";
	private static final String ASSIGNMENT_ITEM_TABLE_COL_3 = "columnThird";
	private static final String ASSIGNMENT_ITEM_TABLE_COL_4 = "columnFourth";

	private FlowPanel assignmentItemTable;
	//private FlowPanel columnFirst, columnSecond, columnThird, columnFourth;

	private Image editImage;
	
	public AssignmentView() {
		initialize();
	}

	/**
	 * Creates the necessary items.
	 */
	private void initialize() {
		getElement().setId(ASSIGNMENT_PANEL_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("initAssignments"));

		getElement().appendChild(headline);

		assignmentItemTable = new FlowPanel();
		assignmentItemTable.getElement().setId(ASSIGNMENT_ITEM_TABLE);

//		columnFirst = new FlowPanel();
//		columnSecond = new FlowPanel();
//		columnThird = new FlowPanel();
//		columnFourth = new FlowPanel();
//
//		columnFirst.addStyleName(ASSIGNMENT_ITEM_TABLE_COL_1);
//		columnSecond.addStyleName(ASSIGNMENT_ITEM_TABLE_COL_2);
//		columnThird.addStyleName(ASSIGNMENT_ITEM_TABLE_COL_3);
//		columnFourth.addStyleName(ASSIGNMENT_ITEM_TABLE_COL_4);
//
//		assignmentItemTable.add(columnFirst);
//		assignmentItemTable.add(columnSecond);
//		assignmentItemTable.add(columnThird);
//		assignmentItemTable.add(columnFourth);

		editImage = new Image("images/pencil.png");
		editImage.setHeight("18px");
		editImage.setWidth("18px");
		editImage.getElement().getStyle().setPosition(Position.ABSOLUTE);
		editImage.getElement().getStyle().setLeft(170, Unit.PX);
		editImage.getElement().getStyle().setTop(16, Unit.PX);
		editImage.getElement().getStyle().setCursor(Cursor.POINTER);
		
		add(assignmentItemTable);
		add(editImage);
	}

	public ScrollPanel getInScrollPanel() {
		ScrollPanel panel = new ScrollPanel(this);
		panel.getElement().setId(ASSIGNMENT_PANEL_WRAPPER_ID);
		return panel;
	}

	public void addAssignmentitem(AssignmentItem item) {
//		columnFirst.add(item.getHtmlNamespace());
//		columnSecond.add(item.getHtmlName());
//		columnThird.add(item.getHtmlType());
//		columnFourth.add(item.getNestedValueTextBox());
		add(item);
	}

	public void removeAssignmentitem(AssignmentItem item) {
//		item.getHtmlNamespace().removeFromParent();
//		item.getHtmlName().removeFromParent();
//		item.getHtmlType().removeFromParent();
//		item.getNestedValueTextBox().removeFromParent();
		remove(item);
	}
	
	/**
	 * @return the editImage
	 */
	public Image getEditImage() {
		return editImage;
	}
}
