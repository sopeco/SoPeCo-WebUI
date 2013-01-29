/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.frontend.shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * 
 * @author Benjamin Ebling
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "getAllVisualizations", query = "SELECT u FROM Visualization u"),
	@NamedQuery(name = "getVisualizationsByAccount", query = "SELECT s FROM Visualization s WHERE s.accountId = :accountId") })
public class Visualization implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2591904277445541753L;
	
	@Id
	@Column(name = "id")
	private long id;
	
	@Column(name = "accountId")
	private String accountId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "link")
	private String link;
	
	@Column(name = "type")
	private Type type = Type.LINK;
	
	@Transient
	private ChartData data;
	
	@Lob
	@Column(name = "chartOptions")
	private ChartOptions options;
	
	@Lob
	@Column(name = "chartParameters")
	private List<ChartParameter> chartParameters;
	
	@Column(name = "scenarioName")
	private String scenarioName;
	
	@Column(name = "measurementEnvironmentUrl")
	private String measurementEnvironmentUrl;
	
	@Column(name = "timestamp")
	private Long timestamp;
	
	@Column(name = "experimentName")
	private String experimentName;

	public Visualization(){
		
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
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ChartData getData() {
		return data;
	}

	public void setData(ChartData data) {
		this.data = data;
	}

	public static enum Type {
		LINK, GCHART;
	}

	public ChartOptions getOptions() {
		return options;
	}

	public void setOptions(ChartOptions options) {
		this.options = options;
	}

	public List<ChartParameter> getChartParameters() {
		return chartParameters;
	}

	public void setChartParameters(List<ChartParameter> chartParameters) {
		this.chartParameters = chartParameters;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getMeasurementEnvironmentUrl() {
		return measurementEnvironmentUrl;
	}

	public void setMeasurementEnvironmentUrl(String measurementEnvironmentUrl) {
		this.measurementEnvironmentUrl = measurementEnvironmentUrl;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != Visualization.class){
			return false;
		}
		return this.getId() == ((Visualization) obj).getId();
	}
	
}
