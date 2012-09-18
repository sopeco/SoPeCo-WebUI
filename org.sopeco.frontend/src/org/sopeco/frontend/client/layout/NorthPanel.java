package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.rpc.ScenarioManagerRPC;
import org.sopeco.frontend.client.rpc.ScenarioManagerRPCAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Panel of the top. It displays the current database and scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class NorthPanel extends FlowPanel {

	private ScenarioManagerRPCAsync scenarioManager;

	private ListBox listboxScenarios;
	private HTML connectedToText;

	private MainLayoutPanel parentPanel;
	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "2.5";

	public NorthPanel(MainLayoutPanel parent) {
		parentPanel = parent;

		scenarioManager = GWT.create(ScenarioManagerRPC.class);

		initialize();
	}

	/**
	 * initialize the user interface.
	 */
	private void initialize() {
		setSize("100%", PANEL_HEIGHT + "em");

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		add(horizontalPanel_2);
		horizontalPanel_2.addStyleName("north_hPanel");
		horizontalPanel_2.setHeight(PANEL_HEIGHT + "em");

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_2.add(horizontalPanel_3);
		// floatlPanel.add(horizontalPanel);
		connectedToText = new HTML("connected to account XXX");
		horizontalPanel_3.add(connectedToText);

		Anchor anchorChangeAccount = new Anchor("change account");
		horizontalPanel_3.add(anchorChangeAccount);
		anchorChangeAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parentPanel.getParentModule().changeDatabase();
			}
		});

		HTML htmlNewHtml = new HTML("", true);
		horizontalPanel_2.add(htmlNewHtml);
		htmlNewHtml.setStyleName("spc-seperator");
		htmlNewHtml.setHeight("2em");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_2.add(horizontalPanel_1);
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_1.setHeight("");

		Label lblSelectScenario = new Label("select scenario");
		horizontalPanel_1.add(lblSelectScenario);

		listboxScenarios = new ListBox();
		listboxScenarios.setEnabled(false);
		horizontalPanel_1.add(listboxScenarios);
		listboxScenarios.setSize("120px", "2em");
		listboxScenarios.setVisibleItemCount(1);

		Button btnAddScenario = new Button("<img src=\"images/add.png\" />");
		btnAddScenario.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AddScenarioDialog addScenario = new AddScenarioDialog();

				addScenario.center();
			}
		});
		btnAddScenario.setStyleName("sopeco-imageButton", true);
		horizontalPanel_1.add(btnAddScenario);
		btnAddScenario.setHeight("2em");

		Button btnRemoveScenario = new Button("<img src=\"images/remove.png\" />");
		btnRemoveScenario.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		btnRemoveScenario.setStyleName("sopeco-imageButton", true);
		horizontalPanel_1.add(btnRemoveScenario);
		btnRemoveScenario.setHeight("2em");

		for (Widget w : new Widget[] { lblSelectScenario, btnRemoveScenario, btnAddScenario, anchorChangeAccount }) {
			w.addStyleName("hDefaultMargin");
		}

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
		connectedToText.setHTML("connected to: <b>" + name + "</b>");
	}

	/**
	 * updating the scenario list
	 */
	private void updateScenarioList() {
		GWT.log("update scenariolist");
		Loader.showLoader();

		Loader.hideLoader();
	}
}
