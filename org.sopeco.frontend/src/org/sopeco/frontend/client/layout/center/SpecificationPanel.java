package org.sopeco.frontend.client.layout.center;

/**
 * 
 * @author Marius Oehler
 *
 */
public class SpecificationPanel extends CenterPanel {
	private String activeSpecification;
	
	public SpecificationPanel() {
	}

	public String getActiveSpecification() {
		return activeSpecification;
	}

	public void setActiveSpecification(String specification) {
		this.activeSpecification = specification;
	}
	
	
}
