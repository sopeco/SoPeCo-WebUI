package org.sopeco.frontend.pap.test;

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
		ChartParameter[] chartParameter = new ChartParameter[3];
		chartParameter[0] = new ChartParameter();
		chartParameter[0].setParameterName("x");
		chartParameter[1] = new ChartParameter();
		chartParameter[1].setParameterName("y");
		chartParameter[2] = new ChartParameter();
		chartParameter[2].setParameterName("z");
		ChartOptions options = new ChartOptions();
		options.setType(ChartType.LINECHART);
		papConnector.createVisualization("test_experiment", new Double[][]{{1.0,1.0},{2.0,1.5},{3.0,3.0}}, chartParameter, options);
		Visualization[] vis = papConnector.getAllVisualizations();
		boolean b = false;
		for (Visualization v : vis){
			if (v.getName().equals("test_experiment")){
				b = true;
			}
		}
		assertTrue("Succesfully created chart",b);
	}

}
