package org.sopeco.frontend.pap.test;

import org.junit.Test;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.sopeco.frontend.pap.PAPChartConnection;
import org.sopeco.frontend.pap.PAPChartConnectionExtension;
import org.sopeco.frontend.server.chartconnector.IChartConnection.ChartTypes;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import static junit.framework.Assert.*;

public class TestPapConnection {
	
	private PAPChartConnection papConnector = (PAPChartConnection) new PAPChartConnectionExtension().createExtensionArtifact();
	
	@Test
	public void testConnection(){
		papConnector.getChartHTML("test_experiment", new Double[][]{{1.0,1.0},{2.0,1.5},{3.0,3.0}}, new String[]{"x","y","z"}, ChartTypes.LINE_CHART);
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
