package org.sopeco.webui.shared.entities.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "getAllAccounts", query = "SELECT a FROM Account a"),
		@NamedQuery(name = "getAccountByName", query = "SELECT a FROM Account a WHERE a.name = :accountName") })
public class Account implements Serializable {

	/** */
	private static final long serialVersionUID = 202241918407921508L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "paswordHash")
	private String paswordHash;

	@Column(name = "dbHost")
	private String dbHost;

	@Column(name = "dbPort")
	private int dbPort;

	@Column(name = "lastInteraction")
	private long lastInteraction;

	@Column(name = "dbName")
	private String dbName;

	@Column(name = "dbPassword")
	private byte[][] dbPassword;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPaswordHash() {
		return paswordHash;
	}

	public void setPaswordHash(String paswordHash) {
		this.paswordHash = paswordHash;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public int getDbPort() {
		return dbPort;
	}

	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}

	public long getLastInteraction() {
		return lastInteraction;
	}

	public void setLastInteraction(long lastInteraction) {
		this.lastInteraction = lastInteraction;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public byte[][] getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(byte[][] dbPassword) {
		this.dbPassword = dbPassword;
	}

	@Override
	public String toString() {
		return "## Account ## \nId: " + id + " \nName: " + name + " \nPasswordHash: " + paswordHash
				+ " \nDatabaseHost: " + dbHost + " \nDatabasePort: " + dbPort + " \nLastInteraction: "
				+ lastInteraction + " \n#############";
	}
}
