package org.sopeco.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ToggleSeparator extends HorizontalPanel implements ClickHandler {

	private static final String CSS_CLASS = "spc-ToggleSeparator";

	private HTML htmlLabel;
	private Image imgState;

	private String firstLabel;
	private String secondLabel;

	private boolean state;

	private List<ToggleHandler> handlerList;

	public ToggleSeparator(String pFirstLabel, String pSecondLabel) {
		WidgetResources.resc.toggleSeparatorCss().ensureInjected();

		handlerList = new ArrayList<ToggleSeparator.ToggleHandler>();

		state = false;

		firstLabel = pFirstLabel;
		secondLabel = pSecondLabel;

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		htmlLabel = new HTML(firstLabel);
		htmlLabel.addClickHandler(this);

		imgState = new Image(WidgetResources.resc.imgArrowStateRight());
		imgState.getElement().getStyle().setVerticalAlign(VerticalAlign.BOTTOM);
		imgState.addClickHandler(this);

		add(imgState);
		add(htmlLabel);
		add(new HorizontalLine());

		setCellWidth(imgState, "1px");
		setCellWidth(htmlLabel, "1px");
	}

	@Override
	public void onClick(ClickEvent event) {
		state = !state;
		if (!state) {
			htmlLabel.setHTML(firstLabel);
			imgState.setResource(WidgetResources.resc.imgArrowStateRight());
		} else {
			htmlLabel.setHTML(secondLabel);
			imgState.setResource(WidgetResources.resc.imgArrowStateDown());
		}
		for (ToggleHandler handler : new ArrayList<ToggleHandler>(handlerList)) {
			handler.onToggle(state);
		}
	}

	public void addToggleHandler(ToggleHandler handler) {
		handlerList.add(handler);
	}

	public void removeToggleHandler(ToggleHandler handler) {
		handlerList.remove(handler);
	}

	public boolean getState() {
		return state;
	}

	/**
	 * 
	 * @author Marius Oehler
	 * 
	 */
	public interface ToggleHandler {
		void onToggle(boolean state);
	}
}
