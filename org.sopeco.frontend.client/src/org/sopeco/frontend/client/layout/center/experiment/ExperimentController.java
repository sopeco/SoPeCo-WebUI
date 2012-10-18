package org.sopeco.frontend.client.layout.center.experiment;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.resources.FrontEndResources;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());

	private ExperimentView view;

	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

		view = new ExperimentView();

		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				experimentChange(event.getExperimentName());
			}
		});
	}

	private void experimentChange(final String experimentName) {
		LOGGER.info("Change experiment to '" + experimentName + "'");

//		Timer t = new Timer() {
//		      public void run() {
		    	  view.getSettingsView().getTextboxName().setText(experimentName);	
//		      }
//		    };
//
//		    // Schedule the timer to run once in 5 seconds.
//		    t.schedule(100);
//		
		
		
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void reset() {
		view.reset();
	}

}
