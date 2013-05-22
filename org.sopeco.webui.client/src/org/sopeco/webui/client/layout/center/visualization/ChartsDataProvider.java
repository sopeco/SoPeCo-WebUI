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
package org.sopeco.webui.client.layout.center.visualization;

import org.sopeco.webui.client.layout.center.visualization.VisualizationController.Status;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.entities.VisualizationBundle;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ChartsDataProvider extends AsyncDataProvider<Visualization> {
	private final VisualizationController visualizationController;

	public ChartsDataProvider(VisualizationController visualizationController) {
		this.visualizationController = visualizationController;
	}

	@Override
	protected void onRangeChanged(HasData<Visualization> display) {
		final Range range = display.getVisibleRange();
		if (visualizationController.getStatus() != Status.BUSY) {
			visualizationController.setStatus(Status.LOADING);
		}
		RPC.getVisualizationRPC().getVisualizations(range.getStart(), range.getLength(),
				new AsyncCallback<VisualizationBundle>() {

					@Override
					public void onSuccess(VisualizationBundle result) {
						if (result.getVisualizations() != null) {
							updateRowData(range.getStart(), result.getVisualizations());
							updateRowCount(result.getTotalNumberOfVisualizations(), true);
						}
						if (visualizationController.getStatus() != Status.BUSY) {
							visualizationController.setStatus(Status.READY);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
	}

	public void addDataDisplay(CellList<Visualization> visualizationList) {
		super.addDataDisplay(visualizationList);
	}

}
