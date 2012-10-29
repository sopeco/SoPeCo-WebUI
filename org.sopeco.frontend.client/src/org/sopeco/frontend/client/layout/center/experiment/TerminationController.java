package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.layout.center.experiment.TerminationView.Condition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TerminationController implements ValueChangeHandler<Boolean> {

	private TerminationView view;

	public TerminationController() {
		view = new TerminationView();

		ExperimentTerminationCondition t = new ExperimentTerminationCondition("test 1", "desc 1");
		ExperimentTerminationCondition t2 = new ExperimentTerminationCondition("test 2", "desc 2");

		t.addParameter("para1", "1");
		t.addParameter("para2", "2");
		t.addParameter("para3", "3");

		t2.addParameter("para1", "1");
		t2.addParameter("para2", "2");

		view.addCondition(t);
		view.addCondition(t2);

		addCheckboxHandler();
	}

	public TerminationView getView() {
		return view;
	}

	/**
	 * 
	 */
	private void addCheckboxHandler() {
		for (Condition condition : view.getConditionMap().values()) {
			condition.addValueChangeHandler(this);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		Condition source = (Condition) event.getSource();
		source.setConditionVisibility(event.getValue());

		if (event.getValue()) {
			GWT.log("user con: " + source.getCondition().getName());
			GWT.log("    conf: " + source.getConfig());
		} else {
			GWT.log("remove con: " + source.getCondition().getName());
		}
	}
}
