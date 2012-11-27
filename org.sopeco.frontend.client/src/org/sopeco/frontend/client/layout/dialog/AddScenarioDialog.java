package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.helper.SimpleNotify;
import org.sopeco.frontend.client.layout.CreateScenarioWizzard;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AddScenarioDialog extends DialogBox implements ClickHandler, SimpleNotify {

	private Button closeButton;

	public AddScenarioDialog() {
		super(false, false);
		setGlassEnabled(true);

		// ScenarioAddController sca = new ScenarioAddController(false, true,
		// true);
		// sca.addHideHandler(this);
		// add(sca.getView());
		CreateScenarioWizzard wizzard = new CreateScenarioWizzard(410, 250);
		wizzard.addSimpleNotifier(this);

		closeButton = new Button(R.get("Close"));
		closeButton.addClickHandler(this);
		closeButton.getElement().getStyle().setFloat(Float.RIGHT);
		closeButton.getElement().getStyle().setMarginRight(10, Unit.PX);
		closeButton.getElement().getStyle().setWidth(164, Unit.PX);

		wizzard.getFooter().add(closeButton);

		add(wizzard);
	}

	@Override
	public void onClick(ClickEvent event) {
		hide();
	}

	@Override
	public void call() {
		hide();
	}
}
