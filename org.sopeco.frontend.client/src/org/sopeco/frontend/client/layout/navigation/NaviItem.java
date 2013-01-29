package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.resources.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NaviItem extends FlowPanel {

	private static final String CSS_CLASS = "naviItem";
	private static final String CSS_CLASS_SELECTED = "selected";
	private static final String CSS_CLASS_SUBITEM = "subItem";
	private static final String CSS_CLASS_EXPERIMENT = "experiment";
	private static final String CSS_CLASS_ADDITEM = "addItem";

	private static final String CSS_CLASS_CHANGE_SPECIFICATION = "changeSpecIcon";

	private String text;
	private String subText;

	private HTML htmlText;
	private HTML htmlSubText;

	public NaviItem(String pText) {
		this(pText, null);
	}

	public NaviItem(String pText, String pSubText) {
		R.resc.cssNavigation().ensureInjected();

		text = pText;
		subText = pSubText;

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);
		sinkEvents(Event.ONCLICK);

		htmlText = new HTML(text);
		add(htmlText);

		if (subText != null) {
			htmlSubText = new HTML(subText);
			add(htmlSubText);
		}
	}

	public void setAsExperiment() {
		setAsSubItem();
		addStyleName(CSS_CLASS_EXPERIMENT);
		setTitle(text);
	}

	public void setAsSubItem() {
		addStyleName(CSS_CLASS_SUBITEM);
	}

	public void setAsAddItem() {
		addStyleName(CSS_CLASS_ADDITEM);
	}

	public void setText(String pText) {
		text = pText;
		htmlText.setHTML(text);
	}

	public void setSubText(String pSubText) {
		subText = pSubText;

		if (htmlSubText == null) {
			htmlSubText = new HTML(subText);
			add(htmlSubText);
		} else {
			htmlSubText.setHTML(subText);
		}
	}

	public String getText() {
		return text;
	}

	public void setSelected(boolean isSelected) {
		if (isSelected) {
			addStyleName(CSS_CLASS_SELECTED);
		} else {
			removeStyleName(CSS_CLASS_SELECTED);
		}
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	public void addChangeSpecificationIcon(final SpecificationPopup specificationPopup) {
		Image changeSpecification = new Image(R.resc.imgSwitch());
		changeSpecification.addStyleName(CSS_CLASS_CHANGE_SPECIFICATION);

		changeSpecification.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (specificationPopup.isAttached()) {
					specificationPopup.removeFromParent();
				} else {
					NaviItem.this.add(specificationPopup);
				}
			}
		});

		add(changeSpecification);
	}
}
