package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioChangedEvent;
import org.sopeco.frontend.client.layout.center.specification.MEControllerBox;
import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
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
public class NorthPanel extends FlowPanel implements ClickHandler {

	private static final String IMAGE_SATELLITE = "images/satellite.png";
	private static final String SAP_RESEARCH_LOGO = "images/sap_research.png";
	private static final String SAP_RESEARCH_LOGO_ID = "sapResearchLogo";

	private static final String NAVI_PANEL_HEIGHT = "2.8em";
	
	private static final String SEPARATOR_CSS_CLASS = "topBarSeparator";
	
	private static final int EXPORT_MARGIN = 4;

	private ListBox listboxScenarios;
	private HTML connectedToText;
	private boolean scenariosAvailable = false;
	private Anchor addScenario, removeScenario;
	private Image imageSatellite;

	private Anchor anchorChangeAccount;
	
	private HorizontalPanel navigationPanel;
	
	private MainLayoutPanel parentPanel;
	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "3";

	public NorthPanel(MainLayoutPanel parent) {
		parentPanel = parent;

		FrontEndResources.loadNavigationCSS();
		
		initialize();
	}

	private HTML createSeparator(){
		HTML ret = new HTML();
		ret.addStyleName(SEPARATOR_CSS_CLASS);
		return ret;
	}
	
	/**
	 * initialize the user interface.
	 */
	private void initialize() {
		setSize("100%", NAVI_PANEL_HEIGHT); // .nPanel in CSS Style
		addStyleName("nPanel");

		
		navigationPanel = new HorizontalPanel();
		navigationPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		navigationPanel.addStyleName("north_hPanel");
//		navigationPanel.setHeight(NAVI_PANEL_HEIGHT);
//		
//		connectedToText = new HTML();
//		navigationPanel.add(connectedToText);
//		
//		anchorChangeAccount = new Anchor(R.get("change_account"));
//		navigationPanel.add(anchorChangeAccount);
//		
//		navigationPanel.add(createSeparator());
//		
		add(navigationPanel);

		/** ######################################### */
		
		// Adding Logo to the Top
		Image researchLogo = new Image(SAP_RESEARCH_LOGO);
		researchLogo.getElement().setId(SAP_RESEARCH_LOGO_ID);
		getElement().appendChild(researchLogo.getElement());

		HorizontalPanel firstHoPanel = new HorizontalPanel();
		firstHoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		navigationPanel.add(firstHoPanel); // ####
		
		connectedToText = new HTML(); // ###
		firstHoPanel.add(connectedToText); // ####
		firstHoPanel.addStyleName("tabStyle");

		anchorChangeAccount = new Anchor(R.get("change_account")); // ####
		firstHoPanel.add(anchorChangeAccount); // ####
		anchorChangeAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parentPanel.getParentModule().changeDatabase();
			}
		});

		// Test
		HTML sep = new HTML();
		sep.addStyleName("topBarSeparator");
		firstHoPanel.add(sep);

		HorizontalPanel secondHoPanel = new HorizontalPanel();
		navigationPanel.add(secondHoPanel); // ####
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

		imageSatellite = new Image(IMAGE_SATELLITE);
		imageSatellite.addClickHandler(this);
		imageSatellite.getElement().getStyle().setMarginLeft(EXPORT_MARGIN, Unit.EM);
		imageSatellite.getElement().getStyle().setCursor(Cursor.POINTER);
		imageSatellite.setHeight("24px");
		imageSatellite.setWidth("24px");
		secondHoPanel.add(imageSatellite);

		setConnectedAccountName(parentPanel.getParentModule().getConnectedDatabase().getDbName());

		updateScenarioList();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == imageSatellite) {
			MEControllerBox.showBox();
		}
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

			int count = 0;
			for (String name : names) {
				listboxScenarios.addItem(name);

				if (Manager.get().getAccountDetails().getSelectedScenario() != null
						&& name.equals(Manager.get().getAccountDetails().getSelectedScenario())) {
					listboxScenarios.setSelectedIndex(count);
				}
				count++;
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
	}
}
