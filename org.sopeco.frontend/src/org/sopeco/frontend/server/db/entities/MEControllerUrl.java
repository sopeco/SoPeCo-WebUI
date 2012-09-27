package org.sopeco.frontend.server.db.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "findAllColtrollerUrls", query = "SELECT u FROM MEControllerUrl u") })
public class MEControllerUrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "url")
	private String url;

	@Column(name = "timeAdded")
	private long timeAdded;

	public String getUrl() {
		return url;
	}

	public void setUrl(String newUrl) {
		this.url = newUrl;
	}

	public long getAdded() {
		return timeAdded;
	}

	public void setAdded(long time) {
		this.timeAdded = time;
	}

}
