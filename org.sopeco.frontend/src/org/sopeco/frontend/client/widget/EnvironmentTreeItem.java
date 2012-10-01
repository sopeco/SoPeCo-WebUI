package org.sopeco.frontend.client.widget;

import org.sopeco.frontend.client.helper.handler.NoSpecialCharsHandler;
import org.sopeco.frontend.shared.rsc.R;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentTreeItem extends FrontendTreeItem {

	protected FlowPanel actionPanel;
	private Image removeNamespace, addNamespace, addParameter;
	protected TextBox textboxEdit;
	private boolean preventBlur = false;

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
		addParameter.addClickHandler(getClickHandlerAddparameter());

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

	@Override
	protected void refreshLinePanel() {
		linePanel.clear();

		addImage();

		linePanel.add(htmlText);
		linePanel.add(textboxEdit);

		addActionPanel();

		linePanel.getElement().appendChild(clearLine);
	}

	protected void addImage() {
		if (children.size() > 0) {
			linePanel.add(img);
		}
	}

	protected void addActionPanel() {
		actionPanel.clear();

		actionPanel.add(addParameter);
		actionPanel.add(addNamespace);

		if (parentItem != null) {
			actionPanel.add(removeNamespace);
		}

		linePanel.add(actionPanel);
	}

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

	private BlurHandler getOnTextboxBlurHandler() {
		return new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (!preventBlur && !textboxEdit.getText().isEmpty()) {
					currentText = textboxEdit.getText();
					htmlText.setHTML(currentText);
				} else {
					preventBlur = false;
				}
				textboxEdit.getElement().getStyle().setDisplay(Display.NONE);
				htmlText.getElement().getStyle().setDisplay(Display.BLOCK);
			}
		};
	}

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

	private ClickHandler getClickHandlerRemove() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove();
			}
		};
	}

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
						tempName += " (" + (counter) + ")";
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
			}
		};
	}

	private ClickHandler getClickHandlerAddparameter() {
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
						tempName += " (" + (counter) + ")";
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

				EParameterTreeItem newItem = new EParameterTreeItem(name, DEFAULT_PARAMETER_TYPE.name(),
						DEFAULT_PARAMETER_ROLE);
				addItem(newItem);
			}
		};
	}
}
