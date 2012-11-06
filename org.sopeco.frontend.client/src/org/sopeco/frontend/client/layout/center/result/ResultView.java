package org.sopeco.frontend.client.layout.center.result;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.gwt.widgets.tree.Tree;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ResultView extends CenterPanel {

	private Button btnRefresh;
	private Tree tree;

	public ResultView() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);

		btnRefresh = new Button(R.get("refresh"));
		tree = new Tree();

		btnRefresh.getElement().getStyle().setMargin(1, Unit.EM);
		tree.getElement().getStyle().setMarginLeft(1, Unit.EM);

		add(btnRefresh);
		add(tree);
	}

	/**
	 * @return the btnTest
	 */
	public Button getBtnTest() {
		return btnRefresh;
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

}
