package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.gwt.widgets.ExtendedTextBox;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioAddController implements ClickHandler, BlurHandler {

	private static final int VIEW_WIDTH_PX = 400;
	private static final String VIEW_CSS_CLASS = "scenarioAddView";
	private static final String INVALID_CSS_CLASS = "invalid";
	private static final String ERROR_TEXT_CSS_CLASS = "errorText";
	private static final String DEFAULT_SPECIFICATION_NAME = "MySpecification";
	private static final String DEFAULT_EXPERIMENT_NAME = "MyExperiment";

	private ScenarioAddView view;
	private boolean hasInfoText, hasCancelButton;
	private ClickHandler hideHandler;

	public ScenarioAddController(boolean pHasInfoText, boolean pHasCancelButton) {
		FrontEndResources.loadScenarioAddCSS();

		hasInfoText = pHasInfoText;
		hasCancelButton = pHasCancelButton;

		view = new ScenarioAddView();
	}

	/**
	 * Returns the view which is managed by this controller.
	 * 
	 * @return ScenarioAddView
	 */
	public Widget getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getBtnOk() && checkForm()) {
			createAndAddScenario();
		} else {
			event.stopPropagation();
			event.preventDefault();
		}
	}

	/**
	 * Checks all input fields, whether the entered values are valid!
	 */
	private boolean checkForm() {
		if (view.getScenarioName().getText().isEmpty()) {
			view.getScenarioName().addStyleName(INVALID_CSS_CLASS);
			//setErrorText(R.get("scenarioNameNotEmpty"));
			return false;
		} else if (ScenarioManager.get().existScenario(view.getScenarioName().getText())) {
			view.getScenarioName().addStyleName(INVALID_CSS_CLASS);
			setErrorText(R.get("scenarioNameExists"));
			return false;
		} else {
			view.getScenarioName().removeStyleName(INVALID_CSS_CLASS);
		}

		return true;
	}

	@Override
	public void onBlur(BlurEvent event) {
		checkForm();
	}

	/**
	 * Sets the text of the error html text.
	 * 
	 * @param errorText
	 */
	private void setErrorText(String errorText) {
		view.getErrorText().setText(errorText);
	}

	/**
	 * 
	 */
	private void createAndAddScenario() {
		ExperimentSeriesDefinition experiment = ScenarioManager.get().experiment()
				.getNewExperimentSeries(view.getExperimentName().getText());

		ScenarioManager.get().createScenario(view.getScenarioName().getText(), view.getSpecificationName().getText(),
				experiment);

		hideHandler.onClick(null);
	}

	/**
	 * Adds a given clickHandler to the buttons of the view.
	 * 
	 * @param handler
	 */
	public void addHideHandler(ClickHandler handler) {
		hideHandler = handler;
		view.getBtnCancel().addClickHandler(handler);
	}

	/**
	 * ########################################################################
	 * The inner class that represents the view of this controller.
	 * 
	 */
	private class ScenarioAddView extends FlowPanel {
		private FlexTable table;
		private TextBox scenarioName, specificationName, experimentName;
		private Headline headline;
		private HTML htmlScenario, htmlSpecification, htmlExperiment, errorText;
		private Button btnOk, btnCancel;
		private Paragraph infoText;

		public ScenarioAddView() {
			init();
		}

		/**
		 * Initialize objects.
		 */
		private void init() {
			addStyleName(VIEW_CSS_CLASS);
			setWidth(VIEW_WIDTH_PX + "PX");

			headline = new Headline(R.get("addScenario"));

			infoText = new Paragraph(R.get("addScenarioInfoText"));

			table = new FlexTable();

			scenarioName = new TextBox();
			specificationName = new ExtendedTextBox(DEFAULT_SPECIFICATION_NAME, false);
			experimentName = new ExtendedTextBox(DEFAULT_EXPERIMENT_NAME, false);

			scenarioName.addBlurHandler(ScenarioAddController.this);

			htmlScenario = new HTML(R.get("scenarioName") + ":");
			htmlSpecification = new HTML(R.get("specificationName") + ":");
			htmlExperiment = new HTML(R.get("experimentName") + ":");
			errorText = new HTML();

			errorText.addStyleName(ERROR_TEXT_CSS_CLASS);

			btnOk = new Button(R.get("AddScenario"));
			btnCancel = new Button(R.get("Cancel"));

			btnOk.addClickHandler(ScenarioAddController.this);
			btnCancel.addClickHandler(ScenarioAddController.this);

			FlowPanel panelBtnWrapper = new FlowPanel();
			panelBtnWrapper.add(errorText);
			panelBtnWrapper.add(btnOk);
			if (hasCancelButton) {
				panelBtnWrapper.add(btnCancel);
			}

			table.setWidget(0, 0, htmlScenario);
			table.setWidget(1, 0, htmlSpecification);
			table.setWidget(2, 0, htmlExperiment);
			table.setWidget(0, 1, scenarioName);
			table.setWidget(1, 1, specificationName);
			table.setWidget(2, 1, experimentName);

			add(headline);
			if (hasInfoText) {
				add(infoText);
			}
			add(table);
			add(panelBtnWrapper);
		}

		/**
		 * @return the btnOk
		 */
		public Button getBtnOk() {
			return btnOk;
		}

		/**
		 * @return the btnCancel
		 */
		public Button getBtnCancel() {
			return btnCancel;
		}

		/**
		 * @return the scenarioName
		 */
		public TextBox getScenarioName() {
			return scenarioName;
		}

		/**
		 * @return the specificationName
		 */
		public TextBox getSpecificationName() {
			return specificationName;
		}

		/**
		 * @return the experimentName
		 */
		public TextBox getExperimentName() {
			return experimentName;
		}

		/**
		 * @return the errorText
		 */
		public HTML getErrorText() {
			return errorText;
		}
	}
}
