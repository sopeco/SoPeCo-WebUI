package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MainNavigation extends FlowPanel {

	/**
	 * Width of this panel.
	 */
	public static final String PANEL_WIDTH = "15";
	private MainLayoutPanel parent;

	/**
	 * 
	 *
	 */
	public enum Navigation {
		Environment, Specification, Execute, Result, Other, NoScenario
	}

	private TLEntry acitveEntry;

	public MainNavigation(MainLayoutPanel parentPanel) {
		parent = parentPanel;

		initialize();
	}

	/**
	 * Initialize the widgets.
	 */
	private void initialize() {
		setWidth(PANEL_WIDTH + "em");
		setHeight("100%");

		TLEntry environmentEntry = new TLEntry(Navigation.Environment, R.get("environment"), true);
		TLEntry specificationEntry = new TLEntry(Navigation.Specification, R.get("specification"));
		TLEntry executeEntry = new TLEntry(Navigation.Execute, R.get("execute"));
		TLEntry resultEntry = new TLEntry(Navigation.Result, R.get("result"));

		add(environmentEntry);
		add(specificationEntry);

		initExperiments();

		add(executeEntry);
		add(resultEntry);

		acitveEntry = environmentEntry;
	}

	private void initExperiments() {
		add(new SLEntry("Exp 1"));
		add(new SLEntry("Exp 2"));
		add(new SLEntry("Exp 3"));
	}

	/*
	 * ####################################################
	 */

	private class TLEntry extends FocusPanel {

		private FlowPanel contentPanel;
		private boolean isActive;
		private TLEntry myself;
		private Navigation type;

		protected FlowPanel getContentPanel() {
			return contentPanel;
		}

		public TLEntry(Navigation type, String name) {
			this(type, name, false);
		}

		public TLEntry(Navigation navType, String name, boolean isMarked) {
			myself = this;
			type = navType;

			addStyleName("tlEntry");

			contentPanel = new FlowPanel();

			HTML label = new HTML(name);
			contentPanel.add(label);

			HTML marked = new HTML();
			marked.addStyleName("marker");
			contentPanel.add(marked);

			if (isMarked) {
				setMarked(isMarked);
			}

			add(contentPanel);

			addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (isActive()) {
						return;
					}

					setMarked(true);

					acitveEntry.setMarked(false);
					acitveEntry = myself;

					switch (type) {
					case Environment:
					case Specification:
					case Execute:
					case Result:
						parent.updateCenterPanel(type);
						break;
					case Other:
					default:
						GWT.log("Other");
					}
				}
			});

			addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					setFocus(false);
				}
			});
		}

		public void setMarked(boolean isMarked) {
			if (isMarked) {
				addStyleName("marked");
				isActive = true;
			} else {
				removeStyleName("marked");
				isActive = false;
			}
		}

		public boolean isActive() {
			return isActive;
		}
	}

	private class SLEntry extends TLEntry {

		public SLEntry(String name) {
			super(Navigation.Other, name);

			removeStyleName("tlEntry");
			addStyleName("slEntry");

			HTML marked = new HTML();
			marked.addStyleName("marker");
			marked.addStyleName("marker second");
			getContentPanel().add(marked);
		}
	}
}
