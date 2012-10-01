package org.sopeco.frontend.client.widget;

import org.sopeco.persistence.dataset.util.ParameterType;
import org.sopeco.persistence.entities.definition.ParameterRole;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EParameterTreeItem extends EnvironmentTreeItem {

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

		linePanel.getElement().appendChild(clearLine);
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
			}
		};
	}
}
