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
package org.sopeco.webui.client.layout.center.experiment.assignment;

import org.sopeco.gwt.widgets.Headline;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.grid.EditGrid;

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
