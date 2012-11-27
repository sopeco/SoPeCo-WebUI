package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.experiment.assignment.CSVEditor;
import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CSVParameterPanel extends ParameterPanel implements ValueChangeHandler<String>, ClickHandler {

	private static final String IMAGE_EDIT = "images/list_white.png";
	private Image editImage;
	private HTML label;
	private EditableText editText;
	private String csvKey;
	private CSVEditor csvEditor;

	public CSVParameterPanel(AssignmentItem item) {
		super(item);

		if (getDVAConfiguration().size() != 1) {
			throw new IllegalStateException();
		}
		csvKey = getDVAConfiguration().keySet().iterator().next();

		initialize();
	}

	/**
	 * Initialize the UI and all necessary elements.
	 * 
	 * @param value
	 */
	private void initialize() {
		getElement().getStyle().setDisplay(Display.BLOCK);

		label = new HTML(R.get("values") + ":");
		label.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getElement().getStyle().setProperty("whiteSpace", "nowrap");

		editText = new EditableText(getDVAConfiguration().get(csvKey));
		editText.addValueChangeHandler(this);

		editImage = new Image(IMAGE_EDIT);
		editImage.getElement().getStyle().setPaddingBottom(1, Unit.PX);
		editImage.getElement().getStyle().setCursor(Cursor.POINTER);
		editImage.addClickHandler(this);
		editImage.setTitle(R.get("showCsvEditor"));

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.setWidth("100%");

		hPanel.add(label);
		hPanel.add(editText);
		hPanel.add(editImage);

		hPanel.setCellWidth(label, "1px");
		hPanel.setCellWidth(editImage, "1px");

		add(hPanel);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == editImage) {
			if (csvEditor == null) {
				csvEditor = new CSVEditor();
				csvEditor.addValueChangeHandler(this);
			}
			csvEditor.setValue(editText.getValue());

			csvEditor.center();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == editText) {
			getAssignmentItem().storeAssignment();
		} else if (event.getSource() == csvEditor) {
			if (event.getValue().equals(editText.getValue())) {
				return;
			}
			editText.setValue(event.getValue(), true);
		}
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		DynamicValueAssignment dva = createDynamicValueAssignment();
		dva.getConfiguration().put(csvKey, editText.getValue());
		return dva;
	}

}
