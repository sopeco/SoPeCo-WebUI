package org.sopeco.frontend.pap.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sopeco.frontend.pap.PAPChartConnection;
import org.sopeco.frontend.pap.PAPChartConnectionExtension;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartOptions.ChartType;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;

import static junit.framework.Assert.*;

public class TestPapConnection {
	
	private PAPChartConnection papConnector = (PAPChartConnection) new PAPChartConnectionExtension().createExtensionArtifact();
	
	@Test
	public void testConnection(){
		List<ChartParameter> chartParameter = new ArrayList<ChartParameter>();
		for (int i = 0; i < 3; i++){
			ChartParameter param = new ChartParameter();
			param.setParameterName("x" + i);
			chartParameter.add(param);
		}
		ChartOptions options = new ChartOptions();
		options.setType(ChartType.LINECHART);
		papConnector.createVisualization("test_experiment", new Double[][]{{1.0,1.0},{2.0,1.5},{3.0,3.0}}, chartParameter, options);
		List<Visualization> vis = papConnector.getVisualizations(0,Integer.MAX_VALUE);
		boolean b = false;
		for (Visualization v : vis){
			if (v.getName().equals("test_experiment")){
				b = true;
			}
		}
		assertTrue("Succesfully created chart",b);
	}

}
