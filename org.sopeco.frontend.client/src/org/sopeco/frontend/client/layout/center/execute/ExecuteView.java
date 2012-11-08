package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.CenterPanel;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteView extends CenterPanel {

	private HTML htmlStatus;
	private Button btnStartExperiment;

	public ExecuteView() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		btnStartExperiment = new Button(R.get("startExperiment"));
		btnStartExperiment.getElement().getStyle().setMargin(1, Unit.EM);

		htmlStatus = new HTML("Status: -");
		htmlStatus.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		htmlStatus.getElement().getStyle().setMarginLeft(1, Unit.EM);

		add(htmlStatus);
		add(btnStartExperiment);
	}

	/**
	 * @return the btnStartExperiment
	 */
	public Button getBtnStartExperiment() {
		return btnStartExperiment;
	}

	/**
	 * @return the htmlStatus
	 */
	public HTML getHtmlStatus() {
		return htmlStatus;
	}

}
