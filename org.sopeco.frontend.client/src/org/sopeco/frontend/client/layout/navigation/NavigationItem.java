package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.helper.UIObjectHelper;
import org.sopeco.frontend.client.layout.center.CenterType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * The class of the items for the navigation.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationItem extends FocusPanel {

	private static final String CSS_SUB_TEXT_CLASS = "subText";
	private static final String CHANGE_SPECIFICATION_ICON_ID = "changeSpecImage";
	private static final String CHANGE_SPECIFICATION_ICON = "images/change_2.png";

	private FlowPanel contentPanel;
	private boolean isActive;
	private CenterType type;
	private Element subTextElement;
	private HTML label;

	/**
	 * The panel, which contains all other widgets.
	 * 
	 * @return
	 */
	protected FlowPanel getContentPanel() {
		return contentPanel;
	}

	public NavigationItem(CenterType centerType, String name) {
		type = centerType;

		addStyleName("tlEntry");

		contentPanel = new FlowPanel();

		FlowPanel textWrapper = new FlowPanel();
		label = new HTML(name);

		subTextElement = DOM.createSpan();
		subTextElement.addClassName(CSS_SUB_TEXT_CLASS);

		textWrapper.add(label);
		textWrapper.getElement().appendChild(subTextElement);

		contentPanel.add(textWrapper);

		HTML marked = new HTML();
		marked.addStyleName("marker");
		contentPanel.add(marked);

		add(contentPanel);

		preventFocus();
	}

	/**
	 * Sets the second textline of the navi items.
	 * 
	 * @param text
	 */
	public void setSubText(String text) {
		subTextElement.setInnerHTML(text);
	}

	/**
	 * Adds a FocusHandler, which prevents this item is focused.
	 */
	private void preventFocus() {
		addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setFocus(false);
			}
		});
	}

	/**
	 * Returns the HTML Element 'label'.
	 */
	public HTML getLabel() {
		return label;
	}

	/**
	 * Returns the CenterType of this navigation item.
	 * 
	 * @return CenterType of this item
	 */
	public CenterType getType() {
		return type;
	}

	/**
	 * Adds the item/icon to show the panel, where you can change the current
	 * specification.
	 */
	public ChangeSpecificationPanel addChangeSpecificationIcon() {
		Image changeSpecification = new Image(CHANGE_SPECIFICATION_ICON);
		changeSpecification.getElement().setId(CHANGE_SPECIFICATION_ICON_ID);

		final ChangeSpecificationPanel specificationPanel = new ChangeSpecificationPanel();

		changeSpecification.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (specificationPanel.isVisible()) {
					specificationPanel.setVisible(false);
				} else {
					specificationPanel.setVisible(true);
				}
			}
		});

		contentPanel.add(changeSpecification);

		contentPanel.add(specificationPanel);

		return specificationPanel;
	}

	/**
	 * Set whether the item is highlighted.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		if (active) {
			addStyleName("marked");
		} else {
			removeStyleName("marked");
		}

		isActive = active;
	}

	/**
	 * Return whether the item is highlighted.
	 * 
	 * @return
	 */
	public boolean isActive() {
		return UIObjectHelper.hasCssClass(this, "marked");

		// return isActive;
	}

}
