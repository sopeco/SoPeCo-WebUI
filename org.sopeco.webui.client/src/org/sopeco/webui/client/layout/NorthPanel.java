/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout;

import java.util.logging.Logger;

import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.ImageHover;
import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.helper.SimpleCallback;
import org.sopeco.webui.client.layout.dialog.AddScenario;
import org.sopeco.webui.client.layout.dialog.LogDialog;
import org.sopeco.webui.client.layout.popups.Confirmation;
import org.sopeco.webui.client.layout.popups.InputDialog;
import org.sopeco.webui.client.layout.popups.InputDialogHandler;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.mec.MECSettings;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SoPeCoDialog;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.UIObject;

/**
 * The Panel of the top. It displays the current database and scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class NorthPanel extends FlowPanel implements ClickHandler, InputDialogHandler, ValueChangeHandler<String> {

	private static final Logger LOGGER = Logger.getLogger(NorthPanel.class.getName());

	/**
	 * The height of this panel in EM.
	 */
	public static final String PANEL_HEIGHT = "3";

	private static final String DISABLED_CSS_CLASS = "disabled";
	private static final String GRADIENT_CSS = "gradient-blue";
	private static final String IMG_BUTTON_CSS_CLASS = "imgButton";
	private static final String NAVI_PANEL_HEIGHT = "2.8em";

	private static final String SEPARATOR_CSS_CLASS = "separator";
	private HTML connectedToText, htmlSelectScenario;
	private Image imageSatellite, imageExport, imageScenarioAdd, imageScenarioRemove, imageChangeAccount, imageLog,
			imageScenarioClone;
	private InputDialog inputClone;

	private ComboBox cbScenarios;

	private HorizontalPanel navigationPanel;

	public NorthPanel(MainLayoutPanel parent) {
		R.css.cssTopNavigation().ensureInjected();
		initialize();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == cbScenarios) {
			switchScenario(event.getValue());
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == imageSatellite && isEnabled(imageSatellite)) {
			showMECSettings();
		} else if (event.getSource() == imageExport && isEnabled(imageExport)) {
			ExportBox.showExportBox();
		} else if (event.getSource() == imageScenarioAdd && isEnabled(imageScenarioAdd)) {
			addScenario();
		} else if (event.getSource() == imageScenarioClone && isEnabled(imageScenarioClone)) {
			cloneScenario();
		} else if (event.getSource() == imageScenarioRemove && isEnabled(imageScenarioRemove)) {
			removeScenario();
		} else if (event.getSource() == imageChangeAccount) {
			SoPeCoUI.get().logout();
		} else if (event.getSource() == imageLog) {
			LogDialog.show();
		}
	}
	
	private void showMECSettings () {
		final MECSettings settings = new MECSettings();
		settings.getElement().getStyle().setMarginBottom(1, Unit.EM);
		settings.removeHeadline();

		final SoPeCoDialog dialog = new SoPeCoDialog(false);
		dialog.setContentWidget(settings);
		dialog.setWidth("360px");
		dialog.setHeadline(R.lang.mecontrollerSettings());
		
		dialog.addButton(R.lang.cancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		
		final Button createButton = dialog.addButton(R.lang.ok(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				settings.saveControllerSettings();
				dialog.hide();
			}
		});		
		createButton.setEnabled(false);
		
		settings.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				createButton.setEnabled(event.getValue());
			}
		});
		
		settings.loadCurrentControllerSettings();
		
		dialog.center();
	}

	private void addScenario() {
		final SoPeCoDialog dialog = new SoPeCoDialog(false);
		final AddScenario adder = new AddScenario();
		dialog.setWidth("360px");
		dialog.setDraggable(true);
		dialog.setHeadline(R.lang.CreateScenario());
		dialog.setContentWidget(adder);
		dialog.setGlassEnabled(true);
		adder.setText(R.lang.enterScenarioDetails());

		dialog.addButton(R.lang.cancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialog.hide();
			}
		});
		final Button createButton = dialog.addButton(R.lang.create(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				adder.createScenario();
				dialog.hide();
			}
		});
		createButton.setEnabled(false);
		adder.setStateHandler(new SimpleCallback<Boolean>() {
			@Override
			public void callback(Boolean object) {
				createButton.setEnabled(object);
			}
		});

		dialog.center();
	}

	@Override
	public void onInput(InputDialog source, String value) {
		if (source == inputClone) {
			ScenarioManager.get().cloneCurrentScenario(value);
		}
	}

	/**
	 * Disables or enables the buttons....
	 * 
	 * @param enabled
	 */
	public void setButtonsEnabled(boolean enabled) {
		if (enabled) {
			cbScenarios.setEnabled(true);
			imageScenarioAdd.removeStyleName(DISABLED_CSS_CLASS);
			imageScenarioClone.removeStyleName(DISABLED_CSS_CLASS);
			imageScenarioRemove.removeStyleName(DISABLED_CSS_CLASS);
			imageExport.removeStyleName(DISABLED_CSS_CLASS);
			imageSatellite.removeStyleName(DISABLED_CSS_CLASS);
		} else {
			cbScenarios.setEnabled(false);
			cbScenarios.addItem("No scenarios");
			imageScenarioAdd.addStyleName(DISABLED_CSS_CLASS);
			imageScenarioClone.addStyleName(DISABLED_CSS_CLASS);
			imageScenarioRemove.addStyleName(DISABLED_CSS_CLASS);
			imageExport.addStyleName(DISABLED_CSS_CLASS);
			imageSatellite.addStyleName(DISABLED_CSS_CLASS);
			Manager.get().getAccountDetails().setSelectedScenario(null);
		}
	}

	/**
	 * Refresh the content of the listbox, which contains all names of available
	 * scenarios and where the user can switch the scenario.
	 * 
	 * @param names
	 *            the scenario names
	 */
	public void updateScenarioList() {
		LOGGER.fine("Update the scenario list");
		cbScenarios.clear();
		String[] names = Manager.get().getAccountDetails().getScenarioNames();
		if (names == null || names.length == 0) {
			setButtonsEnabled(false);
		} else {
			setButtonsEnabled(true);
			for (String name : names) {
				cbScenarios.addItem(name);
			}
			selectListboxItem(Manager.get().getAccountDetails().getSelectedScenario());
		}
	}

	/**
	 *
	 */
	public void updateScenarioListAndSwitch(String scenarioName) {
		updateScenarioList();
		switchScenario(scenarioName);
	}

	private void cloneScenario() {
		if (inputClone == null) {
			inputClone = new InputDialog(R.lang.scenario_clone(), R.lang.cloneScenarioName() + ":");
			inputClone.addHandler(this);
		}
		inputClone.setText("");
		inputClone.center();
	}

	/**
	 * Creates a HTML-DIV-Element. The added classes cause a appearance of a
	 * small bar-separator.
	 * 
	 * @return
	 */
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

		imageChangeAccount = new ImageHover(R.img.navLogout(), R.img.navLogoutHover());
		imageChangeAccount.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageChangeAccount.setTitle(R.lang.Logout());
		imageChangeAccount.addClickHandler(this);
		navigationPanel.add(imageChangeAccount);

		navigationPanel.add(createSeparator());

		htmlSelectScenario = new HTML(R.lang.scenario_select() + ":");
		navigationPanel.add(htmlSelectScenario);

		cbScenarios = new ComboBox();
		cbScenarios.setEditable(false);
		cbScenarios.addValueChangeHandler(this);
		navigationPanel.add(cbScenarios);

		imageScenarioAdd = new ImageHover(R.img.navFileAdd(), R.img.navFileAddHover());
		imageScenarioAdd.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageScenarioAdd.setTitle(R.lang.scenario_add());
		imageScenarioAdd.addClickHandler(this);
		navigationPanel.add(imageScenarioAdd);

		imageScenarioClone = new ImageHover(R.img.navFileDuplicate(), R.img.navFileDuplicateHover());
		imageScenarioClone.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageScenarioClone.setTitle(R.lang.scenario_clone());
		imageScenarioClone.addClickHandler(this);
		navigationPanel.add(imageScenarioClone);

		imageScenarioRemove = new ImageHover(R.img.navFileDelete(), R.img.navFileDeleteHover());
		imageScenarioRemove.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageScenarioRemove.setTitle(R.lang.scenario_remove());
		imageScenarioRemove.addClickHandler(this);
		navigationPanel.add(imageScenarioRemove);

		navigationPanel.add(createSeparator());

		imageExport = new ImageHover(R.img.navExport(), R.img.navExportHover());
		imageExport.addClickHandler(this);
		imageExport.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageExport.setTitle(R.lang.exportModel());
		navigationPanel.add(imageExport);

		navigationPanel.add(createSeparator());

		imageSatellite = new ImageHover(R.img.navSatellite(), R.img.navSatelliteHover());
		imageSatellite.addClickHandler(this);
		imageSatellite.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageSatellite.setTitle(R.lang.mecSettings());
		navigationPanel.add(imageSatellite);

		navigationPanel.add(createSeparator());

		imageLog = new ImageHover(R.img.navLog(), R.img.navLogHover());
		imageLog.addClickHandler(this);
		imageLog.addStyleName(IMG_BUTTON_CSS_CLASS);
		imageLog.setTitle(R.lang.showLog());
		navigationPanel.add(imageLog);

		navigationPanel.add(createSeparator());

		add(navigationPanel);

		connectedToText.setHTML(R.lang.connected_to() + ": <b>" + Manager.get().getAccountDetails().getAccountName()
				+ "</b>");

		updateScenarioList();
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
	 * Confirmation box whether the current scenario should be deleted. If OK is
	 * clicked, the action is passed to the ScenarioManager to remove it.
	 */
	private void removeScenario() {
		final String selectedScenario = Manager.get().getAccountDetails().getSelectedScenario();
		String msg = R.lang.confRemoveScenario() + " <b>'" + selectedScenario + "'</b>?";
		Confirmation.confirm(msg, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ScenarioManager.get().removeScenario(selectedScenario);
			}
		});
	}

	/**
	 * Select the item with the given String in the listbox, if it exists.
	 * 
	 * @param itemToSelect
	 */
	private void selectListboxItem(String itemToSelect) {
		if (itemToSelect == null) {
			return;
		}
		cbScenarios.setSelectedText(itemToSelect);
	}

	/**
	 * Changes the scenario in the one, that has the specified name.
	 */
	private void switchScenario(String scenarioName) {
		ScenarioManager.get().switchScenario(scenarioName);
	}
}
