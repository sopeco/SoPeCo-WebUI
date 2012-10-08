package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.SpecificationPanel;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class Navigation extends FlowPanel {

	/**
	 * Width of this panel.
	 */
	public static final String PANEL_WIDTH = "15";
	private MainLayoutPanel parent;
	private TLEntry acitveEntry;

	public Navigation(MainLayoutPanel parentPanel) {
		parent = parentPanel;

		initialize();
	}

	/**
	 * Initialize the widgets.
	 */
	private void initialize() {
		setWidth(PANEL_WIDTH + "em");
		setHeight("100%");

		TLEntry environmentEntry = new TLEntry(CenterType.Environment, R.get("environment"), true);
		TLEntry specificationEntry = new TLEntry(CenterType.Specification, R.get("specification"));
		TLEntry executeEntry = new TLEntry(CenterType.Execute, R.get("execute"));
		TLEntry resultEntry = new TLEntry(CenterType.Result, R.get("result"));

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
		private CenterType type;
		private HTML activeSpecHTML;

		protected FlowPanel getContentPanel() {
			return contentPanel;
		}

		public TLEntry(CenterType type, String name) {
			this(type, name, false);
		}

		public TLEntry(CenterType navType, String name, boolean isMarked) {
			myself = this;
			type = navType;

			addStyleName("tlEntry");

			contentPanel = new FlowPanel();

			HTML label = new HTML(name);
			contentPanel.add(label);

			HTML marked = new HTML();
			marked.addStyleName("marker");
			contentPanel.add(marked);

			if (navType == CenterType.Specification) {
				extraSpecification();
			}

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

					DOM.getElementById("changeSpecPanel").getStyle().setDisplay(Display.NONE);

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

		private void extraSpecification() {
			Image changeSpecification = new Image("images/change_2.png");
			changeSpecification.getElement().setId("changeSpecImage");

			changeSpecification.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (DOM.getElementById("changeSpecPanel").getStyle().getDisplay().toLowerCase().equals("none")) {
						DOM.getElementById("changeSpecPanel").getStyle().setDisplay(Display.BLOCK);
					} else {
						DOM.getElementById("changeSpecPanel").getStyle().setDisplay(Display.NONE);
					}
				}
			});

			contentPanel.add(changeSpecification);

			VerticalPanel changeSpecPanel = new VerticalPanel();
			changeSpecPanel.getElement().setId("changeSpecPanel");

			HTML marked = new HTML("specification #1");
			marked.addStyleName("marked");
			activeSpecHTML = marked;
			marked.addClickHandler(new SpecClickhandler(marked));

			HTML test2 = new HTML("test 2");
			test2.addClickHandler(new SpecClickhandler(test2));
			HTML test3 = new HTML("test 3");
			test3.addClickHandler(new SpecClickhandler(test3));

			changeSpecPanel.add(marked);
			changeSpecPanel.add(test2);
			changeSpecPanel.add(test3);

			contentPanel.add(changeSpecPanel);
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

		private class SpecClickhandler implements ClickHandler {
			private HTML myself;

			public SpecClickhandler(HTML my) {
				myself = my;
			}

			@Override
			public void onClick(ClickEvent event) {
				activeSpecHTML.removeStyleName("marked");

				activeSpecHTML = myself;

				activeSpecHTML.addStyleName("marked");

				SpecificationPanel sPanel = (SpecificationPanel) parent.getCenterPanels().get(CenterType.Specification);
				sPanel.setActiveSpecification(activeSpecHTML.getText());

				GWT.log(sPanel.getActiveSpecification());
				DOM.getElementById("changeSpecPanel").getStyle().setDisplay(Display.NONE);
			}
		}
	}

	private class SLEntry extends TLEntry {

		public SLEntry(String name) {
			super(CenterType.Other, name);

			removeStyleName("tlEntry");
			addStyleName("slEntry");

			HTML marked = new HTML();
			marked.addStyleName("marker");
			marked.addStyleName("marker second");
			getContentPanel().add(marked);
		}
	}
}
