package org.sopeco.frontend.client.layout.center.result;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.gwt.widgets.tree.TreeItem;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TreeLeaf extends TreeItem implements HasClickHandlers, ClickHandler {

	private static final String ITEM_CSS_CLASS = "resultTreeItem";
	private static final String DOWNLOAD_IMAGE = "images/download.png";
	private Image downloadImage;

	private SharedExperimentRuns experimentRun;

	private TreeLeaf(String pText) {
		super(pText);
	}

	public TreeLeaf(SharedExperimentRuns run) {
		this(run.getLabel());

		experimentRun = run;
	}

	@Override
	protected void initialize(boolean noContent) {
		super.initialize(noContent);
		addStyleName(ITEM_CSS_CLASS);

		removeIcon();
		getContentWrapper().getElement().getStyle().setMarginLeft(1, Unit.EM);

		downloadImage = new Image(DOWNLOAD_IMAGE);
		downloadImage.setTitle(R.get("download"));
		downloadImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		downloadImage.getElement().getStyle().setCursor(Cursor.POINTER);
		downloadImage.addClickHandler(this);

		getContentWrapper().add(downloadImage);
	}

	/**
	 * @return the experimentRun
	 */
	public SharedExperimentRuns getExperimentRun() {
		return experimentRun;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	@Override
	public void onClick(ClickEvent event) {
		fireEvent(new ClickEvent() {
		});
	}
}
