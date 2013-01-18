package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

import com.google.gwt.safehtml.shared.SafeHtml;

public class Visualization implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2591904277445541753L;
	private SafeHtml chart;
	private String name;
	private String link;

	public Visualization(){
		
	}

	public SafeHtml getChart() {
		return chart;
	}

	public void setChart(SafeHtml chart) {
		this.chart = chart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	
}
