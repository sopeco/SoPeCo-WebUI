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
package org.sopeco.frontend.client.layout.center.visualization.types;

import org.sopeco.frontend.client.resources.R;
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
