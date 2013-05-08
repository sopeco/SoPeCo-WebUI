package org.sopeco.webui.shared.entities.account;

import java.io.Serializable;
import java.util.Date;

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
@NamedQueries({
		@NamedQuery(name = "getAllTokens", query = "SELECT t FROM RememberMeToken t"),
		@NamedQuery(name = "getTokenByAccountId", query = "SELECT t FROM RememberMeToken t WHERE t.accountId = :accountId"),
		@NamedQuery(name = "deleteExipredTokens", query = "DELETE FROM RememberMeToken t WHERE t.expireTimestamp < :expireDate") })
public class RememberMeToken implements Serializable {

	/** */
	private static final long serialVersionUID = -7871537028329850115L;

	@Id
	private String tokenHash;

	@Column(name = "accountId")
	private long accountId;

	@Column(name = "expireTimestamp")
	private long expireTimestamp;

	@Column(name = "encrypted")
	private byte[][] encrypted;

	public String getTokenHash() {
		return tokenHash;
	}

	public void setTokenHash(String tokenHash) {
		this.tokenHash = tokenHash;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getExpireTimestamp() {
		return expireTimestamp;
	}

	public void setExpireTimestamp(long expireTimestamp) {
		this.expireTimestamp = expireTimestamp;
	}

	public byte[][] getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(byte[][] encrypted) {
		this.encrypted = encrypted;
	}

}
