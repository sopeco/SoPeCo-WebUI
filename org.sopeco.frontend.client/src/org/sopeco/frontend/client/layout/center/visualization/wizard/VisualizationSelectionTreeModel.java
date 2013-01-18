package org.sopeco.frontend.client.layout.center.visualization.wizard;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

public class VisualizationSelectionTreeModel implements TreeViewModel {
	ListDataProvider<Interpolation> interpolationDataProvider;
	ListDataProvider<Extra> extrasDataProvider;
	ListDataProvider<ChartType> chartTypesDataProvider;
	private SingleSelectionModel<Interpolation> interpolationSelection;
	private MultiSelectionModel<Extra> extrasSelection;
	private SingleSelectionModel<ChartType> chartSelection;
	
	 private final DefaultSelectionEventManager<Interpolation> interpolationSelectionManager =
		      DefaultSelectionEventManager.createDefaultManager();
	 private final DefaultSelectionEventManager<Extra> extraSelectionManager =
		      DefaultSelectionEventManager.createDefaultManager();
	 private final DefaultSelectionEventManager<ChartType> chartTypeSelectionManager =
		      DefaultSelectionEventManager.createDefaultManager();
	 private Interpolation defaultInterpolation = new Interpolation("none");
	 private Extra defaultExtra = new Extra("none");

	public VisualizationSelectionTreeModel(){
		interpolationDataProvider = new ListDataProvider<Interpolation>();
		interpolationDataProvider.getList().add(defaultInterpolation);
		interpolationDataProvider.getList().add(new Interpolation("Test-Interpolation"));
		extrasDataProvider = new ListDataProvider<Extra>();
		extrasDataProvider.getList().add(defaultExtra);
		extrasDataProvider.getList().add(new Extra("Test-Extra"));
		chartTypesDataProvider = new ListDataProvider<ChartType>();
		for (ChartType chart : ChartType.values()){
			chartTypesDataProvider.getList().add(chart);
		}
	}
	
	
	
	public SingleSelectionModel<Interpolation> getInterpolationSelection() {
		return interpolationSelection;
	}



	public void setInterpolationSelection(
			SingleSelectionModel<Interpolation> interpolationSelection) {
		this.interpolationSelection = interpolationSelection;
	}



	public MultiSelectionModel<Extra> getExtrasSelection() {
		return extrasSelection;
	}



	public void setExtrasSelection(MultiSelectionModel<Extra> extrasSelection) {
		this.extrasSelection = extrasSelection;
	}



	public SingleSelectionModel<ChartType> getChartSelection() {
		return chartSelection;
	}



	public void setChartSelection(SingleSelectionModel<ChartType> chartSelection) {
		this.chartSelection = chartSelection;
	}

	

	public Interpolation getDefaultInterpolation() {
		return defaultInterpolation;
	}



	public Extra getDefaultExtra() {
		return defaultExtra;
	}



	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
	    if (value == null) {
	      return new DefaultNodeInfo<Interpolation>(interpolationDataProvider,
	          new InterpolationTypeCell(), interpolationSelection, interpolationSelectionManager, null);
	    } else if (value instanceof Interpolation){
	    	return new DefaultNodeInfo<Extra>(extrasDataProvider, new ExtrasCell(), extrasSelection, extraSelectionManager, null);
	    } else if (value instanceof Extra){
	    	return new DefaultNodeInfo<ChartType>(chartTypesDataProvider, new ChartTypeCell(), chartSelection, chartTypeSelectionManager, null);
	    }

	    // Unhandled type.
	    String type = value.getClass().getName();
	    throw new IllegalArgumentException("Unsupported object type: " + type);
	  }

	@Override
	public boolean isLeaf(Object value) {
		return value instanceof ChartType;
	}

	public static class Interpolation {
		private String name;
		
		public Interpolation(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	public static class Extra {
		private String name;
		
		public Extra(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	public enum ChartType {
		RPLOT("R-plot"), G_LINECHART("Line Chart");
		
		private String name;
		
		ChartType(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	private static class InterpolationTypeCell extends AbstractCell<Interpolation> {

	    public InterpolationTypeCell() {
	    }

	    @Override
	    public void render(Context context, Interpolation value, SafeHtmlBuilder sb) {
	      if (value != null) {
	        sb.appendEscaped(value.getName());
	      }
	      else {
	    	  sb.appendEscaped("-");
	      }
	    }
	  }
	
	private static class ExtrasCell extends AbstractCell<Extra> {

	    public ExtrasCell() {
	    }

	    @Override
	    public void render(Context context, Extra value, SafeHtmlBuilder sb) {
	      if (value != null) {
	        sb.appendEscaped(value.getName());
	      }
	      else {
	    	  sb.appendEscaped("-");
	      }
	    }
	  }
	
	private static class ChartTypeCell extends AbstractCell<ChartType> {

	    public ChartTypeCell() {
	    }

	    @Override
	    public void render(Context context, ChartType value, SafeHtmlBuilder sb) {
	      if (value != null) {
	        sb.appendEscaped(value.getName());
	      }
	      else {
	    	  sb.appendEscaped("-");
	      }
	    }
	  }
}
