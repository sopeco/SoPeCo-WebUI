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
package org.sopeco.frontend.client.layout.center.visualization.wizard;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.layout.center.visualization.wizard.VisualizationSelectionTreeModel.ChartType;
import org.sopeco.frontend.client.layout.center.visualization.wizard.VisualizationSelectionTreeModel.Extra;
import org.sopeco.frontend.client.layout.center.visualization.wizard.VisualizationSelectionTreeModel.Interpolation;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class VisualizationSelectionCellBrowser extends CellBrowser {
	private String[] captions = new String[50];

	public VisualizationSelectionCellBrowser(SingleSelectionModel<ChartType> chartTypeSelection){
		super(new VisualizationSelectionTreeModel(), null);
		VisualizationSelectionTreeModel vstm = (VisualizationSelectionTreeModel) this.getTreeViewModel();
		vstm.setChartSelection(chartTypeSelection);
		TreeNode treeNode = getRootTreeNode().setChildOpen(0, true);
		treeNode.setChildOpen(0, true);
//		SingleSelectionModel<Interpolation> ssm = new SingleSelectionModel<Interpolation>();
//		ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			
//			@Override
//			public void onSelectionChange(SelectionChangeEvent event) {
//				displayCaption(1);
//				System.out.println("Selected Interpolation");
//			}
//		});
//		vstm.setInterpolationSelection(ssm);
//		MultiSelectionModel<Extra> msm = new MultiSelectionModel<Extra>();
//		msm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
//			
//			@Override
//			public void onSelectionChange(SelectionChangeEvent event) {
//				displayCaption(2);
//				System.out.println("Selected Extra");
//			}
//		});
//		vstm.setExtrasSelection(msm);
		this.addHandler(new OpenHandler<Object>() {

			@Override
			public void onOpen(OpenEvent<Object> event) {
				TreeNode tn = (TreeNode) event.getTarget();
				System.out.println(tn.getValue());
				if (tn.getValue() instanceof Interpolation){
					tn.setChildOpen(0, true, true);
					displayCaption(1);
				}
				else {
					displayCaption(2);
				}
			}
		}, OpenEvent.getType());
		getWidget().setWidgetSize(getWidget().getWidget(0), 300);
//		//ssm.setSelected(((VisualizationSelectionTreeModel) getTreeViewModel()).getDefaultInterpolation(), true);
	}

	@Override
	public SplitLayoutPanel getWidget() {
		return (SplitLayoutPanel) super.getWidget();
	}

	public void setCaption(int index, String caption){
		captions[index] = caption;
		displayCaption(index);
	}
	
	private void displayCaption(int index){
		int currentFlowPanelIndex = -1;
		int j = 0;
		Widget widget;
		FlowPanel fPanel = null;
		while (currentFlowPanelIndex < index){
			Widget w1 = getWidget().getWidget(j);
//			System.out.println("Widget: " + w1.getClass() + " j:" + j + " index: " + index);
			if (w1 instanceof ScrollPanel){
				getWidget().setWidgetSize(w1, 300);
				widget = ((ScrollPanel) w1).getWidget();
				if (widget instanceof FlowPanel){
					currentFlowPanelIndex ++;
					fPanel = (FlowPanel) widget;
				}
			}
			j++;
		}
//		System.out.println("displaying caption (" + captions[index] + ") in " + fPanel.getClass());
		List<Widget> widgets = new ArrayList<Widget>();
		for (int i = 0; i < fPanel.getWidgetCount(); i++){
			if (! (fPanel.getWidget(i) instanceof CaptionLabel)){
				widgets.add(fPanel.getWidget(i));
			}
		}
		fPanel.clear();
		CaptionLabel captionLabel = new CaptionLabel(captions[index]);
		captionLabel.setWidth(getDefaultColumnWidth() + "px");
		captionLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		captionLabel.getElement().getStyle().setFontSize(20, Unit.PX);
		fPanel.add(captionLabel);
		for (Widget w : widgets){
			fPanel.add(w);
		}
	}
	
	private static class CaptionLabel extends Label{
		
		public CaptionLabel(String caption){
			super(caption);
		}
	}
}
