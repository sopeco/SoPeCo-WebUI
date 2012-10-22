package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.ScenarioLoadedEvent;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.ScenarioLoadedEventHandler;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInput.Icon;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
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
public class NavigationController {

	private NavigationView view;
	private CenterType currentCenterType;
	private NavigationItem currentActiveNavigationItem;
	private MainLayoutPanel parentLayout;

	public NavigationController(MainLayoutPanel parent) {
		parentLayout = parent;
		view = new NavigationView();

		loadExperiments();
		attachNaviItemClickHandlers();
		addCreateSpecificationClickHandler();

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
	 * Adds a clickhandler to each item in the navigation.
	 */
	private void attachNaviItemClickHandlers() {
		for (final NavigationItem item : view.getNaviItemsMap().values()) {
			item.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (item.isActive()) {
						return;
					}

					parentLayout.updateCenterPanel(item.getType());
					view.getChangeSpecificationPanel().setVisible(false);
				}
			});
		}
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
			view.addExperimentItem(experiment.getName()).addClickHandler(getNavigationSubItemClickHandler());
		}

		NavigationSubItem addExperiment = view.addExperimentItem("Add Experiment");
		addExperiment.addStyleName("addExperimentNaviItem");
		addExperiment.addAddImage();
		addExperiment.addClickHandler(getAddExperimentClickHandler());
	}

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
	 * Returns the clickhandler of the experiment-entries.
	 * 
	 * @return the clickhandler
	 */
	private ClickHandler getNavigationSubItemClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				NavigationSubItem item = (NavigationSubItem) event.getSource();
				setActiveNavigationItem(item);

				if (MainLayoutPanel.get().getCenterType() != item.getType()) {
					GWT.log("display experiment: " + item.getExperimentName());
					parentLayout.updateCenterPanel(item.getType());
				}

				ExperimentChangedEvent expChangedEvent = new ExperimentChangedEvent(item.getExperimentName());
				EventControl.get().fireEvent(expChangedEvent);
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
		if (currentActiveNavigationItem != null) {
			currentActiveNavigationItem.setActive(false);
		}

		currentActiveNavigationItem = item;
		item.setActive(true);
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
