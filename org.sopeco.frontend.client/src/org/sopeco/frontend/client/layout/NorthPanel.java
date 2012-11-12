package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioChangedEvent;
import org.sopeco.frontend.client.layout.center.specification.MEControllerBox;
import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.UIObject;

/**
 * The Panel of the top. It displays the current database and scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class NorthPanel extends FlowPanel implements ClickHandler, ChangeHandler {

	private static final String GRADIENT_CSS = "gradient-blue";
	private static final String IMG_BUTTON_CSS_CLASS = "imgButton";
	private static final String DISABLED_CSS_CLASS = "disabled";

	private static final String IMAGE_SATELLITE = "images/satellite_invert.png";
	private static final String IMAGE_EXPORT = "images/download_invert.png";
	private static final String SAP_RESEARCH_LOGO = "images/sap_research.png";
	private static final String SAP_RESEARCH_LOGO_ID = "sapResearchLogo";

	private static final String NAVI_PANEL_HEIGHT = "2.8em";

	private static final String SEPARATOR_CSS_CLASS = "separator";

	private ListBox listboxScenarios;
	private HTML connectedToText, htmlSelectScenario;
	private boolean scenariosAvailable = false;
	private Anchor anchorAddScenario, anchorRemoveScenario;
	private Image imageSatellite, imageExport, researchLogo;

	private Anchor anchorChangeAccount;

	private HorizontalPanel navigationPanel;

	private MainLayoutPanel parentPanel;
	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "3";

	public NorthPanel(MainLayoutPanel parent) {
		parentPanel = parent;

		FrontEndResources.loadTopNavigationCSS();

		initialize();
	}

	private HTML createSeparator() {
		HTML ret = new HTML();
		ret.addStyleName(SEPARATOR_CSS_CLASS);
		ret.addStyleName(GRADIENT_CSS);
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
		navigationPanel.setHeight(NAVI_PANEL_HEIGHT);

		connectedToText = new HTML();
		navigationPanel.add(connectedToText);

		anchorChangeAccount = new Anchor(R.get("change_account"));
		anchorChangeAccount.addClickHandler(this);
		navigationPanel.add(anchorChangeAccount);

		navigationPanel.add(createSeparator());

		htmlSelectScenario = new HTML(R.get("scenario_select") + ":");
		navigationPanel.add(htmlSelectScenario);

		listboxScenarios = new ListBox();
		listboxScenarios.setSize("120px", "1.8em");
		listboxScenarios.setVisibleItemCount(1);
		listboxScenarios.addChangeHandler(this);
		navigationPanel.add(listboxScenarios);

		anchorAddScenario = new Anchor(R.get("scenario_add"));
		anchorAddScenario.addClickHandler(this);
		navigationPanel.add(anchorAddScenario);

		anchorRemoveScenario = new Anchor(R.get("scenario_remove"));
		anchorRemoveScenario.addClickHandler(this);
		navigationPanel.add(anchorRemoveScenario);

		navigationPanel.add(createSeparator());

		imageExport = new Image(IMAGE_EXPORT);
		imageExport.addClickHandler(this);
		imageExport.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageExport.setTitle(R.get("exportModel")); // TODO: text
		navigationPanel.add(imageExport);

		navigationPanel.add(createSeparator());

		imageSatellite = new Image(IMAGE_SATELLITE);
		imageSatellite.addClickHandler(this);
		imageSatellite.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageSatellite.setTitle(R.get("mecSettings")); // TODO: text
		navigationPanel.add(imageSatellite);

		navigationPanel.add(createSeparator());

		researchLogo = new Image(SAP_RESEARCH_LOGO);
		researchLogo.getElement().setId(SAP_RESEARCH_LOGO_ID);
		getElement().appendChild(researchLogo.getElement());

		add(navigationPanel);

		setConnectedAccountName(parentPanel.getParentModule().getConnectedDatabase().getDbName());

		updateScenarioList();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == imageSatellite && isEnabled(imageSatellite)) {
			MEControllerBox.showBox();
		} else if (event.getSource() == imageExport && isEnabled(imageExport)) {
			ExportBox.showExportBox();
		} else if (event.getSource() == anchorAddScenario && isEnabled(anchorAddScenario)) {
			AddScenarioDialog addScenarioDialog = new AddScenarioDialog();
			addScenarioDialog.center();
		} else if (event.getSource() == anchorRemoveScenario && isEnabled(anchorRemoveScenario)) {
			removeScenario();
		} else if (event.getSource() == anchorChangeAccount) {
			FrontendEntryPoint.get().changeDatabase();
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (event.getSource() == listboxScenarios) {
			switchScenario();
		}
	}

	/**
	 * 
	 */
	private void removeScenario() {
		String msg = R.get("confRemoveScenario") + " <b>'"
				+ listboxScenarios.getItemText(listboxScenarios.getSelectedIndex()) + "'</b>?";

		Confirmation.confirm(msg, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String name = listboxScenarios.getItemText(listboxScenarios.getSelectedIndex());

				ScenarioManager.get().removeScenario(name);
			}
		});
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
	 * Disables or enables the buttons....
	 * 
	 * @param enabled
	 */
	public void setButtonsEnabled(boolean enabled) {
		if (enabled) {
			listboxScenarios.setEnabled(true);
			scenariosAvailable = true;
			anchorAddScenario.setEnabled(true);
			anchorAddScenario.removeStyleName(DISABLED_CSS_CLASS);
			anchorRemoveScenario.setEnabled(true);
			anchorRemoveScenario.removeStyleName(DISABLED_CSS_CLASS);
			imageExport.removeStyleName(DISABLED_CSS_CLASS);
			imageSatellite.removeStyleName(DISABLED_CSS_CLASS);
		} else {
			listboxScenarios.addItem(R.get("no_scenarios"));
			listboxScenarios.setEnabled(false);
			scenariosAvailable = false;
			anchorRemoveScenario.setEnabled(false);
			anchorRemoveScenario.addStyleName(DISABLED_CSS_CLASS);
			anchorAddScenario.setEnabled(false);
			anchorAddScenario.addStyleName(DISABLED_CSS_CLASS);
			imageExport.addStyleName(DISABLED_CSS_CLASS);
			imageSatellite.addStyleName(DISABLED_CSS_CLASS);
			Manager.get().getAccountDetails().setSelectedScenario(null);
		}
	}

	/**
	 * Returns whether the UIObject is enabled or not. It only checks, if the
	 * class attribute contains the "disabled" class.
	 * 
	 * @param object
	 * @return
	 */
	private boolean isEnabled(UIObject object) {
		for (String c : object.getStyleName().split(" ")) {
			if (c.equals(DISABLED_CSS_CLASS)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Updates the scenario listbox with the given names.
	 * 
	 * @param names
	 *            the scenario names
	 */
	private void updateScenarioList(String[] names) {
		listboxScenarios.clear();

		Manager.get().setAvailableScenarios(names);

		if (names == null || names.length == 0) {
			setButtonsEnabled(false);
		} else {
			setButtonsEnabled(true);

			String foundSelectedScenario = null;
			int count = 0;
			for (String name : names) {
				listboxScenarios.addItem(name);

				if (Manager.get().getAccountDetails().getSelectedScenario() != null
						&& name.equals(Manager.get().getAccountDetails().getSelectedScenario())) {
					listboxScenarios.setSelectedIndex(count);
					foundSelectedScenario = name;
				}
				count++;
			}

			if (foundSelectedScenario != null) {
				Manager.get().getAccountDetails().setSelectedScenario(foundSelectedScenario);
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

		// if (name.isEmpty()) {
		// return;
		// }

		EventControl.get().fireEvent(new ScenarioChangedEvent(name));
	}
}
