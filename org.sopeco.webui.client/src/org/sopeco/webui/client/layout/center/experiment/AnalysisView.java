package org.sopeco.webui.client.layout.center.experiment;

import org.sopeco.gwt.widgets.ComboBox;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;

public class AnalysisView extends ExtensionView {
	private static final String TREE_CSS_CLASS = "specificationTreeView";
	
	private ComboBox cbDependentParameter;
	private FlexTable ftIndependentParameters;
	private AnalysisDefinitionTree parameterTree;

	public AnalysisView(int pxWidth) {
		super(pxWidth);
		initAnalysisView();
		
	}

	private void initAnalysisView() {
		cbDependentParameter = new ComboBox();
		getCbDependentParameter().setWidth(width - 2 * DEFAULT_MARGIN);
		getCbDependentParameter().getElement().getStyle().setMarginLeft(DEFAULT_MARGIN, Unit.PX);
		getCbDependentParameter().getElement().getStyle().setMarginRight(DEFAULT_MARGIN, Unit.PX);

		ftIndependentParameters = new FlexTable();
		
		parameterTree=new AnalysisDefinitionTree();
		parameterTree.getView().addStyleName(TREE_CSS_CLASS);
		
		add(getCbDependentParameter());
		add(getFtIndependentParameters());
		add(parameterTree.getView());

	}

	/**
	 * @return the cbDependentParameter
	 */
	public ComboBox getCbDependentParameter() {
		return cbDependentParameter;
	}

	/**
	 * @return the ftIndependentParameters
	 */
	public FlexTable getFtIndependentParameters() {
		return ftIndependentParameters;
	}

	/**
	 * @return the parameterTree
	 */
	public AnalysisDefinitionTree getParameterTree() {
		return parameterTree;
	}

	/**
	 * @param parameterTree
	 *            the parameterTree to set
	 */
	public void setParameterTree(AnalysisDefinitionTree parameterTree) {
		this.parameterTree = parameterTree;
	}

	// /**
	// *
	// * @param labelText
	// * @param key
	// * @param value
	// * @return
	// */
	// public EditableText addConfigRow(String labelText, String key, String
	// value) {
	// //TODO
	// }

}
