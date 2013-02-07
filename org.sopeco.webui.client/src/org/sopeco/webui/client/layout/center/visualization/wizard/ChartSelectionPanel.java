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
package org.sopeco.webui.client.layout.center.visualization.wizard;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.webui.client.resources.FrontEndResources;
import org.sopeco.webui.shared.entities.ChartOptions.ChartType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChartSelectionPanel extends FlowPanel{
	
	private ChartType selectedType = ChartType.LINECHART;
	private List<ClickHandler> clickHandler;
	private List<ChartIconPanel> chartIconPanels;

	public ChartSelectionPanel() {
		FrontEndResources.loadVisualizationWizardCSS();
		clickHandler = new ArrayList<ClickHandler>();
		chartIconPanels = new ArrayList<ChartSelectionPanel.ChartIconPanel>();
		ChartType[] types = ChartType.values();
		for (int i = 0; i < types.length; i++){
			final ChartType type = types[i];
			final ChartIconPanel chartIconPanel = new ChartIconPanel(type.name(), new Image("images/" + type.name().toLowerCase() + ".png"));
			chartIconPanels.add(chartIconPanel);
			chartIconPanel.addStyleName("chartIconBox");
			chartIconPanel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					selectedType = type;
					for (ChartIconPanel cip : chartIconPanels){
						cip.setSelected(false);
					}
					chartIconPanel.setSelected(true);
					for (ClickHandler handler : clickHandler){
						handler.onClick(event);
					}
				}
			});
			this.add(chartIconPanel);
		}
	}
	
	public void addClickHandler(ClickHandler clickHandler){
		this.clickHandler.add(clickHandler);
	}
	
	public void removeClickHandler(ClickHandler clickHandler){
		this.clickHandler.remove(clickHandler);
	}

	public ChartType getSelectedType() {
		return selectedType;
	}

	private static class ChartIconPanel extends FocusPanel {
		private VerticalPanel rootWidget;

		public ChartIconPanel(String labelString, Image image) {
			rootWidget = new VerticalPanel();
			this.setPixelSize(100, 100);
			rootWidget.setPixelSize(100, 100);
			rootWidget.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			if(image != null){
				rootWidget.add(image);
				rootWidget.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
				rootWidget.setCellVerticalAlignment(image, HasVerticalAlignment.ALIGN_MIDDLE);
			}
			Label label = new Label(labelString);
			rootWidget.add(label);
			rootWidget.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER);
			rootWidget.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
			
			this.add(rootWidget);
			this.getElement().getStyle().setMargin(1, Unit.EM);
		}
		
		public void setSelected(boolean selected){
			if (selected){
				this.getElement().getStyle().setBorderColor("red");
			} else {
				this.getElement().getStyle().setBorderColor("grey");
			}
		}
	}
}
