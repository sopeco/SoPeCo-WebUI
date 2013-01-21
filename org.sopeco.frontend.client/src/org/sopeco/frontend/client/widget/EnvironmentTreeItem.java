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

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.helper.handler.NoSpecialCharsHandler;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentTreeItem extends FrontendTreeItem {

	private static final Logger LOGGER = Logger.getLogger(EnvironmentTreeItem.class.getName());

	protected FlowPanel actionPanel;
	protected Image removeNamespace, addNamespace, addParameter;
	protected TextBox textboxEdit;
	private boolean preventBlur = false;
	private boolean hasActionPanel = true;

	private static final ParameterRole DEFAULT_PARAMETER_ROLE = ParameterRole.INPUT;
	private static final ParameterType DEFAULT_PARAMETER_TYPE = ParameterType.BOOLEAN;

	public EnvironmentTreeItem(String html) {
		super(html);
	}

	@Override
	protected void initialize() {
		super.initialize();

		htmlText.addClickHandler(getOnHTMLClickHandler());
		htmlText.setTitle(R.get("clickEdit"));

		actionPanel = new FlowPanel();
		actionPanel.addStyleName("actionPanel");

		removeNamespace = new Image("images/trash_white.png");
		addNamespace = new Image("images/add_white.png");
		addParameter = new Image("images/list_white.png");

		removeNamespace.addClickHandler(getClickHandlerRemove());
		addNamespace.addClickHandler(getClickHandlerAdd());
		addParameter.addClickHandler(getClickHandlerAddParameter());

		removeNamespace.setTitle(R.get("removeNamespace"));
		addNamespace.setTitle(R.get("addNamespace"));
		addParameter.setTitle(R.get("addParameter"));

		NoSpecialCharsHandler nscHandler = new NoSpecialCharsHandler();

		textboxEdit = new TextBox();
		textboxEdit.getElement().getStyle().setHeight(22, Unit.PX);
		textboxEdit.addBlurHandler(getOnTextboxBlurHandler());
		textboxEdit.addKeyDownHandler(getTextboxKeyDownHandler());
		textboxEdit.addKeyPressHandler(nscHandler);
		textboxEdit.addChangeHandler(nscHandler);
	}

	/**
	 * 
	 */
	public void removeActionPanel() {
		actionPanel.clear();
		actionPanel.removeFromParent();
		hasActionPanel = false;
	}

	@Override
	protected void refreshLinePanel() {
		linePanel.clear();

		addImage();

		linePanel.add(htmlText);
		linePanel.add(textboxEdit);

		if (hasActionPanel) {
			addActionPanel();
		}

		linePanel.getElement().appendChild(clearLine);
	}

	/**
	 * 
	 */
	protected void addImage() {
		if (children.size() > 0) {
			linePanel.add(img);
		}
	}

	/**
	 * 
	 */
	protected void addActionPanel() {
		actionPanel.clear();

		actionPanel.add(addParameter);
		actionPanel.add(addNamespace);

		if (parentItem != null) {
			actionPanel.add(removeNamespace);
		}

		linePanel.add(actionPanel);
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getOnHTMLClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				textboxEdit.setText(currentText);

				htmlText.getElement().getStyle().setDisplay(Display.NONE);
				textboxEdit.getElement().getStyle().setDisplay(Display.BLOCK);
				textboxEdit.setFocus(true);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private BlurHandler getOnTextboxBlurHandler() {
		return new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {

				rename();

			}
		};
	}

	/**
	 * 
	 */
	protected void applyChanges() {
		if (!preventBlur && !textboxEdit.getText().isEmpty()) {
			currentText = textboxEdit.getText();
			htmlText.setHTML(currentText);
		} else {
			preventBlur = false;
		}

		textboxEdit.getElement().getStyle().setDisplay(Display.NONE);
		htmlText.getElement().getStyle().setDisplay(Display.BLOCK);
	}

	/**
	 * 
	 */
	protected void rename() {
		String oldPath = getPath();

		applyChanges();

		RPC.getMEControllerRPC().renameNamespace(oldPath, currentText, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
			}

			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	private KeyDownHandler getTextboxKeyDownHandler() {
		return new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
					preventBlur = true;
					textboxEdit.setFocus(false);
				} else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					textboxEdit.setFocus(false);
				}
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getClickHandlerRemove() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				removeItem();
			}
		};
	}

	/**
	 * 
	 */
	protected void removeItem() {
		String remNsPath = getPath();
		GWT.log("new ns: " + remNsPath);

		Loader.showIcon();
		RPC.getMEControllerRPC().removeNamespace(remNsPath, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				remove();

				Loader.hideIcon();
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());

				Loader.hideIcon();
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getClickHandlerAdd() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String name = R.get("newNamespace");

				boolean loop = true;
				int counter = 0;
				while (loop) {
					loop = false;
					String tempName = name;
					if (counter++ > 0) {
						tempName += "_(" + (counter) + ")";
					}

					for (FrontendTreeItem fti : children) {
						if (fti.getText().equals(tempName)) {
							loop = true;
							break;
						}
					}

					if (!loop) {
						name = tempName;
					}
				}

				EnvironmentTreeItem newItem = new EnvironmentTreeItem(name);
				addItem(newItem);

				String newNsPath = getPath() + "/" + name;
				GWT.log("new ns: " + newNsPath);

				Loader.showIcon();
				RPC.getMEControllerRPC().addNamespace(newNsPath, new AsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						// TODO

						Loader.hideIcon();
					}

					@Override
					public void onFailure(Throwable caught) {
						Message.error(caught.getMessage());

						Loader.hideIcon();
					}
				});
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getClickHandlerAddParameter() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String name = R.get("newParameter");

				boolean loop = true;
				int counter = 0;
				while (loop) {
					loop = false;
					String tempName = name;
					if (counter++ > 0) {
						tempName += "_(" + (counter) + ")";
					}

					for (FrontendTreeItem fti : children) {
						if (fti.getText().equals(tempName)) {
							loop = true;
							break;
						}
					}

					if (!loop) {
						name = tempName;
					}
				}

				final String finalName = name;

				Loader.showIcon();
				RPC.getMEControllerRPC().addParameter(getPath(), finalName, DEFAULT_PARAMETER_TYPE.name(),
						DEFAULT_PARAMETER_ROLE, new AsyncCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								EParameterTreeItem newItem = new EParameterTreeItem(finalName, DEFAULT_PARAMETER_TYPE
										.name(), DEFAULT_PARAMETER_ROLE);

								addItem(newItem);

								Loader.hideIcon();
							}

							@Override
							public void onFailure(Throwable caught) {
								Message.error(caught.getMessage());

								Loader.hideIcon();
							}
						});

			}
		};
	}
}
