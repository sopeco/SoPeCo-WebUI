package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;

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

				setActiveSpecification(specificationName);

				((SpecificationController) parentLayout.getCenterController(CenterType.Specification))
						.setCurrentSpecificationName(specificationName);
			}
		};
	}

	/**
	 * The specification (in the ChangeSpecificationPanel) with the given name
	 * will be highlighted.
	 */
	public void setActiveSpecification(String name) {
		String currentSelectedSpecification = ((SpecificationController) parentLayout
				.getCenterController(CenterType.Specification)).getCurrentSpecificationName();

		if (view.getChangeSpecificationPanel().getItemMap().containsKey(currentSelectedSpecification)) {
			view.getChangeSpecificationPanel().getItemMap().get(currentSelectedSpecification).removeStyleName("marked");
		}

		view.getChangeSpecificationPanel().getItemMap().get(name).addStyleName("marked");

		view.getChangeSpecificationPanel().setVisible(false);
	}

	/**
	 * Adds the existing experiments to the navigation.
	 */
	private void loadExperiments() {
		view.addExperimentItem("Exp. 1").addClickHandler(getNavigationSubItemClickHandler());
		view.addExperimentItem("Exp. 2").addClickHandler(getNavigationSubItemClickHandler());
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
				view.getNaviItemsMap().get(currentCenterType).setActive(false);

				NavigationSubItem item = (NavigationSubItem) event.getSource();
				setActiveNavigationItem(item);

				GWT.log("display experiment: " + item.getExperimentName());
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
