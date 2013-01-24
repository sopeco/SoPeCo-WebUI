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
package org.sopeco.frontend.client.layout.center.result;

import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.center.visualization.wizard.VisualizationWizard;
import org.sopeco.frontend.client.layout.dialog.ExportCsvDialog;
import org.sopeco.frontend.client.layout.dialog.ExportToRDialog;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.gwt.widgets.tree.TreeItem;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TreeLeaf extends TreeItem implements /* HasClickHandlers, */ClickHandler, AsyncCallback<String> {

	private static final Logger LOGGER = Logger.getLogger(TreeLeaf.class.getName());

	private static final String ITEM_CSS_CLASS = "resultTreeItem";
	private static final String DOWNLOAD_IMAGE = "images/download.png";
	private static final String R_IMAGE = "images/r_logo.png";
	private static final String CHART_IMAGE = "images/line_chart.png";
	
	private Image downloadImage, rImage, chartImage;
	private PopupPanel chartPopup;

	private SharedExperimentRuns experimentRun;

	private TreeLeaf(String pText) {
		super(pText);
	}

	public TreeLeaf(SharedExperimentRuns run) {
		this(run.getLabel());

		experimentRun = run;
		
		chartPopup = new VisualizationWizard(experimentRun);
	}

	@Override
	protected void initialize(boolean noContent) {
		super.initialize(noContent);
		addStyleName(ITEM_CSS_CLASS);

		removeIcon();
		getContentWrapper().getElement().getStyle().setMarginLeft(1, Unit.EM);

		downloadImage = new Image(DOWNLOAD_IMAGE);
		downloadImage.setTitle(R.get("download"));
		downloadImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		downloadImage.getElement().getStyle().setCursor(Cursor.POINTER);
		downloadImage.addClickHandler(this);

		rImage = new Image(R_IMAGE);
		rImage.setTitle(R.get("Get R-Command"));
		rImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		rImage.getElement().getStyle().setCursor(Cursor.POINTER);
		rImage.addClickHandler(this);
		
		chartImage = new Image(CHART_IMAGE);
		chartImage.setTitle(R.get("Show chart"));
		chartImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		chartImage.getElement().getStyle().setCursor(Cursor.POINTER);
		chartImage.addClickHandler(this);

		getContentWrapper().add(downloadImage);
		getContentWrapper().add(rImage);
		getContentWrapper().add(chartImage);
	}

	/**
	 * @return the experimentRun
	 */
	public SharedExperimentRuns getExperimentRun() {
		return experimentRun;
	}

	// @Override
	// public HandlerRegistration addClickHandler(ClickHandler handler) {
	// return addHandler(handler, ClickEvent.getType());
	// }

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == downloadImage) {

			StringBuffer param = new StringBuffer();

			param.append(experimentRun.getTimestamp());
			param.append("|");
			param.append(experimentRun.getParentSeries().getExperimentName());
			param.append("|");
			param.append(experimentRun.getParentSeries().getParentInstance().getControllerUrl());
			param.append("|");
			param.append(experimentRun.getParentSeries().getParentInstance().getScenarioName());

			// downloadUrl += "?param=" + Base64.encodeString(param.toString());

			// Window.open(downloadUrl, "_blank", "");
			ExportCsvDialog.show(param.toString());
		} else if(event.getSource() == chartImage){
			chartPopup.center();
		} else {
			RPC.getResultRPC().getResultAsR(experimentRun.getParentSeries().getParentInstance().getScenarioName(),
					experimentRun.getParentSeries().getExperimentName(),
					experimentRun.getParentSeries().getParentInstance().getControllerUrl(),
					experimentRun.getTimestamp(), this);
		}
	}

	@Override
	public void onFailure(Throwable caught) {
		LOGGER.severe(caught.getMessage());
		Message.error(caught.getMessage());
	}

	@Override
	public void onSuccess(String result) {
		ExportToRDialog.show(result);
	}
}
