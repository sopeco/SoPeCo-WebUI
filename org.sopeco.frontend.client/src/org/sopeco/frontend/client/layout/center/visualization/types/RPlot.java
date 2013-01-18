package org.sopeco.frontend.client.layout.center.visualization.types;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class RPlot extends FlowPanel implements VisualizationType {
	
	private Frame frame;
	private Image loadingIndicator;
	private boolean loading = true;
	
	private static final String LOADING_INDICATOR = "images/loading_indicator.gif";
	private String papLinkCache = null;

	public RPlot(final String name, String processScript, String outputScript){
		frame = new Frame();
		frame.addStyleName("chartView");
//		frame.setPixelSize(1024, 650);
		loadingIndicator = new Image(LOADING_INDICATOR);
//		this.setPixelSize(1024, 650);
		this.add(loadingIndicator);
		if (papLinkCache == null){
//			RPC.getVisualizationRPC().getChartUrl(name,processScript, outputScript, new AsyncCallback<String>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							
//						}
//
//						@Override
//						public void onSuccess(String result) {
//							papLinkCache = result;
//							RPlot.this.setLink(result);
//							loading = false;
//						}
//					});
		}
		else {
			loading = false;
			this.setLink(papLinkCache);
		}
	}
	
	public RPlot(String chartUrl){
		frame = new Frame();
		frame.addStyleName("chartView");
		papLinkCache = chartUrl;
//		frame.setPixelSize(1024, 650);
		loadingIndicator = new Image(LOADING_INDICATOR);
//		this.setPixelSize(1024, 650);
		this.setLink(chartUrl);
		loading = false;
	}
	
	public void setLink(String link){
		this.clear();
		this.add(frame);
		frame.setUrl(link);
	}
	
	public String getLink(){
		return papLinkCache;
	}

	@Override
	public Widget getWidget() {
		return this;
	}

	@Override
	public String getName() {
		return R.get("RPlot");
	}

	@Override
	public boolean isLoading() {
		return loading;
	}
}
