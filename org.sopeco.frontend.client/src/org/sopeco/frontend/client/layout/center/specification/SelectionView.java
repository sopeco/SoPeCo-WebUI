package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.model.Manager.ControllerStatus;
import org.sopeco.frontend.client.widget.ClickPanel;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.tree.Tree;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectionView extends ScrollPanel {

	private static final String SELECTION_PANEL_WRAPPER_ID = "selectionViewPanelWrapper";
	private static final String SELECTION_PANEL_ID = "selectionViewPanel";
	private static final String MECONTROLLER_URL_ID = "meControllerUrl";

	private FlowPanel wrapper;
	private Tree tree;
	private HTML htmlControllerUrl;
	private Image imgControllerStatus;

	private long lastCheck;

	public SelectionView() {
		initialize();
	}

	/**
	 * initialize the view.
	 */
	private void initialize() {
		wrapper = new FlowPanel();

		wrapper.getElement().setId(SELECTION_PANEL_ID);
		getElement().setId(SELECTION_PANEL_WRAPPER_ID);

		// ************************
		// ************************
		// ************************
		// TODO cleanup
		Headline headlineW = new Headline(R.get("meController"));
		wrapper.add(headlineW);

		lastCheck = System.currentTimeMillis();

		ClickPanel cpa = new ClickPanel();
		FlowPanel par = new FlowPanel();
		par.getElement().setId(MECONTROLLER_URL_ID);

		imgControllerStatus = new Image("images/status-green.png");
		imgControllerStatus.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		imgControllerStatus.getElement().getStyle().setPaddingBottom(1, Unit.PX);
		imgControllerStatus.getElement().getStyle().setMarginRight(6, Unit.PX);
		imgControllerStatus.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				long dif = (System.currentTimeMillis() - lastCheck) / 1000;
				String text = "Last check: ";
				if (dif >= 60) {
					text += (dif / 60) + " minutes";
				} else {
					text += dif + " seconds";
				}

				imgControllerStatus.setTitle(text);
			}
		});
		cpa.add(par);
		cpa.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MEControllerBox.showBox();
			}
		});

		par.add(imgControllerStatus);

		htmlControllerUrl = new HTML("");
		htmlControllerUrl.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		par.add(htmlControllerUrl);

		wrapper.add(cpa);

		// ************************
		// ************************
		// ************************

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("envParameter"));

		wrapper.getElement().appendChild(headline);

		wrapper.add(getTree());

		add(wrapper);
	}

	public void setControllerUrl(String controllerUrl) {
		htmlControllerUrl.setText(controllerUrl);
	}

	public void setControllerStatus(ControllerStatus status, long lastUpdate) {
		String imgUrl = "images/status-";
		switch (status) {
		case ONLINE:
			imgUrl += "green.png";
			break;
		case OFFLINE:
			imgUrl += "red.png";
			break;
		default:
		case UNKNOWN:
			imgUrl += "gray.png";
			break;
		}

		imgControllerStatus.setUrl(imgUrl);
		lastCheck = lastUpdate;
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		if (tree == null) {
			tree = new Tree();
		}

		return tree;
	}

	/**
	 * @param newTree
	 *            the tree to set
	 */
	public void setTree(Tree newTree) {
		tree = newTree;
		wrapper.add(tree);
	}

}
