package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class KVParameterPanel extends ParameterPanel implements ValueChangeHandler<String> {

	private Map<String, EditableText> editableTextMap;

	public KVParameterPanel(AssignmentItem item) {
		super(item);
		initialize();
	}

	/**
	 * Initializes the UI and all necessary elements.
	 */
	private void initialize() {
		editableTextMap = new HashMap<String, EditableText>();

		updateMap(getDVAConfiguration());

		Grid grid = new Grid(editableTextMap.size(), 2);
		grid.setWidth("100%");
		grid.getColumnFormatter().setWidth(0, "1px");
		int row = 0;
		for (String key : editableTextMap.keySet()) {
			grid.setWidget(row, 0, new HTML(key + ":"));
			grid.setWidget(row, 1, editableTextMap.get(key));
			row++;
		}

		add(grid);
	}

	/**
	 * Creates the editable textfield and stores them in the editableTextMap.
	 * The key is the same as that of the configuration map.
	 * 
	 * @param config
	 */
	private void updateMap(Map<String, String> config) {
		for (String key : config.keySet()) {
			EditableText editText = new EditableText(config.get(key));
			editText.addValueChangeHandler(this);
			editableTextMap.put(key, editText);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		getAssignmentItem().storeAssignment();
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		DynamicValueAssignment dva = createDynamicValueAssignment();
		dva.getConfiguration().clear();
		for (String key : editableTextMap.keySet()) {
			dva.getConfiguration().put(key, editableTextMap.get(key).getValue());
		}
		return dva;
	}

}
