/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.layout.center.experiment.assignment.items.AssignmentItem;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentView extends FlowPanel {
	private static final String EXP_PREPERATION_PANEL_ID = "expPreperationPanel";
	private static final String ASSIGNMENT_ITEM_TABLE = "assignmentItemTable";
	private static final String ASSIGNMENT_ITEM_TABLE_HEADER_CSS = "headerRow";

	private Headline headline;
	private FlexTable grid;

	public AssignmentView(String headlineText) {
		initialize(headlineText);
	}

	/**
	 * Inits all objects.
	 */
	private void initialize(String headlineText) {
		getElement().setId(EXP_PREPERATION_PANEL_ID);

		headline = new Headline(headlineText);
		headline.getElement().getStyle().setMarginBottom(1, Unit.EM);

		initGrid();

		add(headline);
		add(grid);
	}

	private void initGrid() {
		grid = new FlexTable();

		grid.addStyleName(ASSIGNMENT_ITEM_TABLE);
		addTableHeader();
	}

	public FlexTable getGrid() {
		return grid;
	}

	public void addTableHeader() {
		grid.getColumnFormatter().setWidth(0, "1px");
		grid.getColumnFormatter().setWidth(1, "1px");
		grid.getColumnFormatter().setWidth(2, "1px");

		grid.setText(0, 0, R.get("Namespace"));
		grid.setText(0, 1, R.get("Parameter"));
		grid.setText(0, 2, R.get("Type"));
		grid.setText(0, 3, R.get("Variation"));

		grid.getRowFormatter().addStyleName(0, ASSIGNMENT_ITEM_TABLE_HEADER_CSS);
	}

	public void addItem(int row, AssignmentItem item) {
		grid.setText(row, 0, item.getAssignment().getParameter().getNamespace().getFullName());
		grid.setText(row, 1, item.getAssignment().getParameter().getName());
		grid.setText(row, 2, item.getAssignment().getParameter().getType());
		grid.setWidget(row, 3, item.getCombobox());

		grid.setWidget(row + 1, 0, item.getParameterWrapper());

		grid.getCellFormatter().addStyleName(row, 0, "namespace");
		grid.getCellFormatter().addStyleName(row, 1, "name");
		grid.getCellFormatter().addStyleName(row, 2, "type");
		grid.getCellFormatter().addStyleName(row + 1, 0, "value");

		grid.getFlexCellFormatter().setColSpan(row + 1, 0, 4);
		grid.getRowFormatter().addStyleName(row + 1, "valueRow");
	}
}
