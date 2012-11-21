package org.sopeco.frontend.client.layout.navigation;

import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioLoadedEvent;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.ScenarioLoadedEventHandler;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInput.Icon;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;

/**
 * The controller class of the navigation.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationController implements ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());

	private NavigationView view;
	private CenterType currentCenterType;
	private NavigationItem currentActiveNavigationItem;

	public NavigationController() {
		view = new NavigationView();

		loadExperiments();
		addCreateSpecificationClickHandler();

		for (NavigationItem item : view.getNaviItemsMap().values()) {
			item.addClickHandler(this);
		}

		// Events
		EventControl.get().addHandler(ScenarioLoadedEvent.TYPE, new ScenarioLoadedEventHandler() {
			@Override
			public void onScenarioLoadedEvent(ScenarioLoadedEvent scenarioLoadedEvent) {
				updateSpecifications();
			}
		});

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				setActiveSpecification(event.getSelectedSpecification());

				view.getNaviItemsMap().get(CenterType.Specification).setSubText(event.getSelectedSpecification());
				loadExperiments();
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		NavigationItem source = (NavigationItem) event.getSource();

		if (source.isActive()) {
			LOGGER.info("navi item is already selected -> skipped");
			return;
		}

		if (source.getType() == CenterType.Experiment) {
			String experimentName = ((NavigationSubItem) source).getExperimentName();
			MainLayoutPanel.get().getViewSwitch().switchToExperiment(experimentName);
			Manager.get().setSelectedExperiment(experimentName);
		} else {
			MainLayoutPanel.get().getViewSwitch().switchTo(source.getType());
		}
	}

	/**
	 * Udpates the "specification-select-panel", where you can select an other
	 * specification.
	 */
	public void updateSpecifications() {
		removeAllSpecifications();
		for (MeasurementSpecification ms : ScenarioManager.get().getCurrentScenarioDefinition()
				.getMeasurementSpecifications()) {
			addSpecifications(ms.getName());
		}
	}

	/**
	 * Add a clickhandler to the "add specification" html-element, which handels
	 * the adding of new specifications.
	 */
	private void addCreateSpecificationClickHandler() {
		view.getChangeSpecificationPanel().getAddSpecificationHTML().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.getChangeSpecificationPanel().setVisible(false);

				TextInput.doInput(Icon.Add, "Add specification", R.get("addExpText") + ":", new TextInputOkHandler() {
					@Override
					public void onInput(ClickEvent event, String input) {
						ScenarioManager.get().createNewSpecification(input);
					}
				});
			}
		});
	}

	/**
	 * Hides the panel, where the user can change the current specification.
	 */
	public void hideChangeSpecpanel() {
		view.getChangeSpecificationPanel().setVisible(false);
	}

	/**
	 * Add a new specification to the panel, where you can change the current
	 * specification.
	 * 
	 * @param text
	 *            the name of the specification
	 */
	public void addSpecifications(String text) {
		view.getChangeSpecificationPanel().addItem(text).addClickHandler(getChangeSpecificationClickHandler());
	}

	/**
	 * Removes all entries of the panel, where you can change the current
	 * specification.
	 */
	public void removeAllSpecifications() {
		view.getChangeSpecificationPanel().clear();
		view.getChangeSpecificationPanel().getItemMap().clear();
	}

	/**
	 * The clickhandler of the entries, where you can change the current
	 * specification. This handler changes the current specification.
	 * 
	 * @return the clickhandler
	 */
	private ClickHandler getChangeSpecificationClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				HTML item = (HTML) event.getSource();
				String specificationName = item.getText();

				if (ScenarioManager.get().specification().getWorkingSpecificationName().equals(specificationName)) {
					view.getChangeSpecificationPanel().setVisible(false);

					return;
				}

				EventControl.get().fireEvent(new SpecificationChangedEvent(specificationName));
			}
		};
	}

	/**
	 * The specification (in the ChangeSpecificationPanel) with the given name
	 * will be highlighted.
	 */
	private void setActiveSpecification(String name) {
		for (HTML html : view.getChangeSpecificationPanel().getItemMap().values()) {
			html.removeStyleName("marked");
		}

		view.getChangeSpecificationPanel().getItemMap().get(name).addStyleName("marked");
		view.getChangeSpecificationPanel().setVisible(false);
	}

	/**
	 * Adds the existing experiments to the navigation.
	 */
	public void loadExperiments() {
		view.clearExperiments();

		for (ExperimentSeriesDefinition experiment : ScenarioManager.get().experiment()
				.getExperimentsOfCurrentSpecififcation()) {
			NavigationSubItem expItem = view.addExperimentItem(experiment.getName());

			// expItem.addClickHandler(getNavigationSubItemClickHandler());
			expItem.addClickHandler(this);
		}

		NavigationSubItem addExperiment = view.addExperimentItem(R.get("addExperiment"));
		addExperiment.addStyleName("addExperimentNaviItem");
		addExperiment.addAddImage();
		addExperiment.addClickHandler(getAddExperimentClickHandler());
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getAddExperimentClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TextInput.doInput(Icon.Add, R.get("addExperiment"), R.get("addExpText") + ":",
						new TextInputOkHandler() {
							@Override
							public void onInput(ClickEvent event, String input) {
								ScenarioManager.get().experiment().createExperimentSeries(input);
							}
						});
			}
		};
	}

	/**
	 * Highlights the navigation entry, which having the given type.
	 * 
	 * @param newType
	 *            the new CenterType.
	 */
	public void setCurrentCenterType(CenterType newType) {
		if (view.getNaviItemsMap().containsKey(newType)) {
			setActiveNavigationItem(view.getNaviItemsMap().get(newType));
		}

		currentCenterType = newType;
	}

	/**
	 * Highlights the navigation entry, which having the given type.
	 * 
	 * @param item
	 */
	private void setActiveNavigationItem(NavigationItem item) {
		if (item == null) {
			return;
		}

		if (currentActiveNavigationItem != null) {
			currentActiveNavigationItem.setActive(false);
		}

		currentActiveNavigationItem = item;
		item.setActive(true);
	}

	/**
	 * Highlights the NaviItem, which is related to the experiment with the
	 * given name.
	 * 
	 * @param experimentName
	 */
	public void highlightExperiment(String experimentName) {
		setActiveNavigationItem(view.getExperimentItems().get(experimentName));
	}

	/**
	 * Returns the current CenterType, which is highlighted.
	 * 
	 * @return
	 */
	public CenterType getCurrentCenterType() {
		return currentCenterType;
	}

	/**
	 * Returns the NavigationView of this Controller.
	 * 
	 * @return the nvaigationView
	 */
	public NavigationView getView() {
		return view;
	}
}
