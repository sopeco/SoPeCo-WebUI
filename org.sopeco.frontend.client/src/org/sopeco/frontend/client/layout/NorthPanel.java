package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioChangedEvent;
import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Panel of the top. It displays the current database and scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class NorthPanel extends FlowPanel {

	private static final String SAP_RESEARCH_LOGO = "images/sap_research.png";
	private static final String SAP_RESEARCH_LOGO_ID = "sapResearchLogo";

	private static final int EXPORT_MARGIN = 4;

	private ListBox listboxScenarios;
	private HTML connectedToText;
	private boolean scenariosAvailable = false;
	private Anchor addScenario, removeScenario;

	private MainLayoutPanel parentPanel;
	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "3";

	public NorthPanel(MainLayoutPanel parent) {
		parentPanel = parent;

		initialize();
	}

	/**
	 * initialize the user interface.
	 */
	private void initialize() {
		setSize("100%", "2.8em"); // .nPanel in CSS Style
		addStyleName("nPanel");

		HorizontalPanel mainHoPanel = new HorizontalPanel();
		mainHoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainHoPanel.addStyleName("north_hPanel");
		add(mainHoPanel);

		// Adding Logo to the Top
		Image researchLogo = new Image(SAP_RESEARCH_LOGO);
		researchLogo.getElement().setId(SAP_RESEARCH_LOGO_ID);
		getElement().appendChild(researchLogo.getElement());

		HorizontalPanel firstHoPanel = new HorizontalPanel();
		firstHoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainHoPanel.add(firstHoPanel);
		connectedToText = new HTML();
		firstHoPanel.add(connectedToText);
		firstHoPanel.addStyleName("tabStyle");

		Anchor anchorChangeAccount = new Anchor(R.get("change_account"));
		firstHoPanel.add(anchorChangeAccount);
		anchorChangeAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parentPanel.getParentModule().changeDatabase();
			}
		});

		HorizontalPanel secondHoPanel = new HorizontalPanel();
		mainHoPanel.add(secondHoPanel);
		secondHoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		secondHoPanel.addStyleName("tabStyle");

		HTML htmlSelectScenario = new HTML(R.get("scenario_select") + ":");
		secondHoPanel.add(htmlSelectScenario);

		listboxScenarios = new ListBox();
		secondHoPanel.add(listboxScenarios);
		listboxScenarios.setSize("120px", "1.8em");
		listboxScenarios.setVisibleItemCount(1);
		listboxScenarios.addItem(" ---");
		listboxScenarios.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				switchScenario();
			}
		});

		addScenario = new Anchor(R.get("scenario_add"));
		secondHoPanel.add(addScenario);

		final NorthPanel mySelf = this;

		addScenario.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AddScenarioDialog addScenarioDialog = new AddScenarioDialog(mySelf);
				addScenarioDialog.center();
			}
		});

		removeScenario = new Anchor(R.get("scenario_remove"));
		secondHoPanel.add(removeScenario);
		removeScenario.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String msg = R.get("confRemoveScenario") + " <b>'"
						+ listboxScenarios.getItemText(listboxScenarios.getSelectedIndex()) + "'</b>?";

				Confirmation.confirm(msg, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						String name = listboxScenarios.getItemText(listboxScenarios.getSelectedIndex());
						RPC.getScenarioManager().removeScenario(name, new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught) {
								Message.error(caught.getMessage());
							}

							@Override
							public void onSuccess(Boolean result) {
								updateScenarioList();
							}
						});
					}
				});
			}
		});

		for (Widget w : new Widget[] { htmlSelectScenario, anchorChangeAccount, listboxScenarios, addScenario,
				removeScenario, secondHoPanel }) {
			w.addStyleName("hDefaultMargin");
		}

		Anchor export = new Anchor("export");
		export.getElement().getStyle().setMarginLeft(EXPORT_MARGIN, Unit.EM);
		export.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ExportBox.showExportBox();
			}
		});
		secondHoPanel.add(export);

		setConnectedAccountName(parentPanel.getParentModule().getConnectedDatabase().getDbName());

		updateScenarioList();
	}

	/**
	 * Set the database name in the left corner on the top.
	 * 
	 * @param name
	 *            name of the current database
	 */
	public void setConnectedAccountName(String name) {
		connectedToText.setHTML(R.get("connected_to") + ": <b>" + name + "</b>");
	}

	/**
	 * Starts the update process of the scenario list.
	 */
	public void updateScenarioList() {
		GWT.log("update scenariolist");
		Loader.showLoader();

		RPC.getScenarioManager().getScenarioNames(new AsyncCallback<String[]>() {
			@Override
			public void onSuccess(String[] result) {
				updateScenarioList(result);
				Loader.hideLoader();
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
				Loader.hideLoader();
			}
		});
	}

	/**
	 * Updates the scenario listbox with the given names.
	 * 
	 * @param names
	 *            the scenario names
	 */
	private void updateScenarioList(String[] names) {
		listboxScenarios.clear();

		if (names == null || names.length == 0) {
			listboxScenarios.addItem(R.get("no_scenarios"));
			listboxScenarios.setEnabled(false);
			scenariosAvailable = false;
			removeScenario.setEnabled(false);
			removeScenario.addStyleName("disabled");
		} else {
			listboxScenarios.setEnabled(true);
			scenariosAvailable = true;
			removeScenario.setEnabled(true);
			removeScenario.removeStyleName("disabled");

			for (String name : names) {
				listboxScenarios.addItem(name);
			}

		}

		switchScenario();
	}

	/**
	 * Retuns the selected item.
	 * 
	 * @return selected scenario name
	 */
	public String getSelectedScenario() {
		if (!scenariosAvailable) {
			return "";
		}

		return listboxScenarios.getItemText(listboxScenarios.getSelectedIndex());
	}

	/**
	 * 
	 */
	private void switchScenario() {
		String name = getSelectedScenario();

		if (name.isEmpty()) {
			return;
		}

		EventControl.get().fireEvent(new ScenarioChangedEvent(name));
		// ScenarioManager.get().switchScenario(name);

		// Loader.showLoader();
		// RPC.getScenarioManager().switchScenario(name, new
		// AsyncCallback<Boolean>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// Loader.hideLoader();
		// Message.error(caught.getMessage());
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// parentPanel.createNewCenterPanels();
		//
		// ((SpecificationController)
		// parentPanel.getCenterController(CenterType.Specification))
		// .loadSpecificationNames();
		//
		// Loader.hideLoader();
		// }
		// });
	}
}
