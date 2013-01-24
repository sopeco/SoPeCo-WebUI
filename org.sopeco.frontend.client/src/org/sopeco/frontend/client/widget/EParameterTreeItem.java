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
package org.sopeco.frontend.client.widget;

import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EParameterTreeItem extends EnvironmentTreeItem {

	private static final Logger LOGGER = Logger.getLogger(EParameterTreeItem.class.getName());

	private Image parameterImage;
	private ParameterRole role;
	private String type;
	private HTML roleHTML, typeSeperator, typeHTML;
	private ListBox listboxType;

	public EParameterTreeItem(String name, String pType, ParameterRole pRole) {
		super(name);

		type = pType;
		role = pRole;

		extendedInitialize();
	}

	@Override
	protected void initialize() {
		super.initialize();

		parameterImage = new Image("images/list_white.png");
		parameterImage.addStyleName("parameterImg");

		roleHTML = new HTML("");
		roleHTML.addStyleName("leftDiv");
		roleHTML.addStyleName("editableParameter");
		roleHTML.addClickHandler(getRoleTextChanger());

		typeSeperator = new HTML(":");
		typeSeperator.addStyleName("leftDiv");
		typeSeperator.addStyleName("typeSeperator");

		typeHTML = new HTML();
		typeHTML.addStyleName("leftDiv");
		typeHTML.addStyleName("editableParameter");
		typeHTML.addClickHandler(getTypeTextChanger());

		listboxType = new ListBox();
		listboxType.addStyleName("listboxType");
		listboxType.addBlurHandler(getListboxTypeBlurHandler());
		for (ParameterType t : ParameterType.values()) {
			listboxType.addItem(t.name());
		}

		removeNamespace.setTitle(R.get("removeParameter"));
	}

	private void extendedInitialize() {
		roleHTML.setHTML(role.name());
		typeHTML.setHTML(type);
	}

	@Override
	protected void refreshLinePanel() {
		linePanel.clear();

		linePanel.add(parameterImage);

		linePanel.add(htmlText);
		linePanel.add(textboxEdit);

		linePanel.add(typeSeperator);
		linePanel.add(typeHTML);
		linePanel.add(listboxType);

		linePanel.add(roleHTML);

		addActionPanel();

		linePanel.getElement().appendChild(clearLine);
	}

	protected void addActionPanel() {
		actionPanel.clear();

		if (parentItem != null) {
			actionPanel.add(removeNamespace);
		}

		linePanel.add(actionPanel);
	}

	private ClickHandler getRoleTextChanger() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (role == ParameterRole.INPUT) {
					role = ParameterRole.OBSERVATION;
				} else {
					role = ParameterRole.INPUT;
				}

				roleHTML.setHTML(role.name());

				updateParameter();

				event.preventDefault();
				event.stopPropagation();
			}
		};
	}

	private ClickHandler getTypeTextChanger() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for (int i = 0; i < listboxType.getItemCount(); i++) {
					if (type.toLowerCase().equals(listboxType.getItemText(i).toLowerCase())) {
						listboxType.setSelectedIndex(i);
						break;
					}
				}

				typeHTML.getElement().getStyle().setDisplay(Display.NONE);
				listboxType.getElement().getStyle().setDisplay(Display.BLOCK);

				listboxType.setFocus(true);
			}
		};
	}

	private BlurHandler getListboxTypeBlurHandler() {
		return new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				type = listboxType.getItemText(listboxType.getSelectedIndex());
				typeHTML.setHTML(type);

				listboxType.getElement().getStyle().setDisplay(Display.NONE);
				typeHTML.getElement().getStyle().setDisplay(Display.BLOCK);

				updateParameter();
			}
		};
	}

	@Override
	protected void removeItem() {
		RPC.getMEControllerRPC().removeParameter(parentItem.getPath(), currentText, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				remove();
			}
		});
	}

	@Override
	protected void rename() {
		GWT.log("rename");

		String oldName = currentText;

		applyChanges();

		updateParameter(oldName, currentText);
	}

	private void updateParameter() {
		updateParameter(currentText, currentText);
	}

	private void updateParameter(String oldName, String newName) {
		String path = parentItem.getPath();

		// Loader.showIcon();
		ScenarioManager.get().updateParameter(path, oldName, newName, type, role);
		// RPC.getMEControllerRPC().updateParameter(path, oldName, newName,
		// type, role, new AsyncCallback<Boolean>() {
		// @Override
		// public void onSuccess(Boolean result) {
		// Loader.hideIcon();
		// }
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// Message.error(caught.getMessage());
		//
		// Loader.hideIcon();
		// }
		// });
	}
}
