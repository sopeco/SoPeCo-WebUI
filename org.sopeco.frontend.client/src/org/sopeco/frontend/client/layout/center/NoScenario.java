package org.sopeco.frontend.client.layout.center;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.MECController;
import org.sopeco.frontend.client.layout.ScenarioAddController;
import org.sopeco.gwt.widgets.SlidePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NoScenario extends CenterPanel implements ClickHandler, BlurHandler, KeyUpHandler {

	private static final String ADD_SCENARIO_BOX = "noScenarioBox";
	private static final int SLIDER_HEIGHT = 240, SLIDER_WIDTH = 410;

	private static final String FOOTER_CSS_CLASS = "noscFooterPanel";

	private SlidePanel slidePanel;
	private Button btnNext, btnPrevious;
	private ScenarioAddController sac;
	private MECController mecController;

	public NoScenario() {
		slidePanel = new SlidePanel(SLIDER_WIDTH, SLIDER_HEIGHT);
		slidePanel.addStyleName(ADD_SCENARIO_BOX);

		slidePanel.getFooterPanel().addStyleName(FOOTER_CSS_CLASS);

		btnNext = new Button(R.get("Next"));
		btnNext.addClickHandler(this);
		btnNext.getElement().getStyle().setFloat(Float.RIGHT);

		btnPrevious = new Button(R.get("Previous"));
		btnPrevious.addClickHandler(this);

		slidePanel.addFooterWidget(btnPrevious);
		slidePanel.addFooterWidget(btnNext);

		sac = new ScenarioAddController(true, false, false);
		sac.addBlurHandlerSName(this);
		sac.addKeyUpHandlerSName(this);

		mecController = new MECController();

		slidePanel.addWidget(sac.getView());
		slidePanel.addWidget(mecController.getView());

		add(slidePanel);

		updateButtons();
	}

	@Override
	public void onBlur(BlurEvent event) {
		btnNext.setEnabled(sac.checkForm());
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		btnNext.setEnabled(sac.checkForm());
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnNext) {
			if (slidePanel.getSlidePosition() == 0) {
				slidePanel.next();
			} else {
				GWT.log("add");
			}
		} else if (event.getSource() == btnPrevious) {
			slidePanel.previous();
		}
		updateButtons();
	}

	/**
	 * 
	 */
	private void updateButtons() {
		btnPrevious.setEnabled(slidePanel.hasPrevious());

		if (!slidePanel.hasNext()) {
			btnNext.setText(R.get("AddScenario"));
		} else {
			btnNext.setText(R.get("Next"));
		}
	}
}
