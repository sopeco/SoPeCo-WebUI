package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Panel of the top. It displays the current database and scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class NorthPanel extends FlowPanel {

	private ListBox listboxScenarios;
	private HTML connectedToText;

	private MainLayoutPanel parentPanel;
	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "2.5";

	public NorthPanel(MainLayoutPanel parent) {
		parentPanel = parent;

		initialize();
	}

	/**
	 * initialize the user interface.
	 */
	private void initialize() {
		setSize("100%", PANEL_HEIGHT + "em");

		HorizontalPanel mainHoPanel = new HorizontalPanel();
		mainHoPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		add(mainHoPanel);
		mainHoPanel.addStyleName("north_hPanel");

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

		HTML htmlSelectScenario = new HTML(R.get("scenario_select"));
		secondHoPanel.add(htmlSelectScenario);

		listboxScenarios = new ListBox();
		secondHoPanel.add(listboxScenarios);
		listboxScenarios.setSize("120px", "2em");
		listboxScenarios.setVisibleItemCount(1);
		listboxScenarios.addItem(" ---");

		// OptGroupElement group = Document.get().createOptGroupElement();
		// group.setLabel("scenarios");
		// OptionElement element = Document.get().createOptionElement();
		// element.setInnerText("label");
		// element.setValue("val");
		// group.appendChild(element);
		//
		// SelectElement sel = listboxScenarios.getElement().cast();
		// sel.appendChild(group);

		// Button btnAddScenario = new Button("<img src=\"images/add.png\" />");
		// btnAddScenario.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// AddScenarioDialog addScenario = new AddScenarioDialog();
		//
		// addScenario.center();
		// }
		// });
		// btnAddScenario.setStyleName("sopeco-imageButton", true);
		// // horizontalPanel_1.add(btnAddScenario);
		// btnAddScenario.setHeight("2em");
		//
		// Button btnRemoveScenario = new
		// Button("<img src=\"images/remove.png\" />");
		// btnRemoveScenario.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		//
		// }
		// });
		// btnRemoveScenario.setStyleName("sopeco-imageButton", true);
		// // horizontalPanel_1.add(btnRemoveScenario);
		// btnRemoveScenario.setHeight("2em");

		Anchor addScenario = new Anchor(R.get("scenario_add"));
		secondHoPanel.add(addScenario);

		addScenario.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AddScenarioDialog addScenario = new AddScenarioDialog();
				addScenario.center();
			}
		});

		Anchor removeScenario = new Anchor(R.get("scenario_remove"));

		secondHoPanel.add(removeScenario);

		for (Widget w : new Widget[] { htmlSelectScenario, anchorChangeAccount, listboxScenarios, addScenario,
				removeScenario, secondHoPanel }) {
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
		connectedToText.setHTML(R.get("connected_to") + ": <b>" + name + "</b>");
	}

	/**
	 * Starts the update process of the scenario list.
	 */
	private void updateScenarioList() {
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
		} else {
			listboxScenarios.addItem(R.get("select"));
			listboxScenarios.setEnabled(true);

			for (String name : names) {
				listboxScenarios.addItem(name);
			}
		}
	}
}
