package org.sopeco.frontend.client.layout.center.visualization.wizard;

import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.entities.ChartOptions.ChartType;

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

	public ChartSelectionPanel() {
		FrontEndResources.loadVisualizationWizardCSS();
		this.setWidth("750px");
		ChartType[] types = ChartType.values();
		for (int i = 0; i < types.length; i++){
			final ChartType type = types[i];
			ChartIconPanel chartIconPanel = new ChartIconPanel(type.name(), new Image("images/" + type.name().toLowerCase() + ".png"));
			chartIconPanel.addStyleName("chartIconBox");
			chartIconPanel.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					System.out.println("selected type: " + type);
					selectedType = type;
				}
			});
			this.add(chartIconPanel);
		}
	}

	public ChartType getSelectedType() {
		return selectedType;
	}

	private static class ChartIconPanel extends FocusPanel {
		private VerticalPanel rootWidget;

		public ChartIconPanel(String labelString, Image image) {
			rootWidget = new VerticalPanel();
			this.setPixelSize(250, 250);
			rootWidget.setPixelSize(250, 250);
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
		}
	}
}
