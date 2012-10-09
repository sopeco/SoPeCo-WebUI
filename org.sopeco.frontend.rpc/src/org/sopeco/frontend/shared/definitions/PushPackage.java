package org.sopeco.frontend.shared.definitions;

import java.io.Serializable;

import org.sopeco.frontend.client.rpc.PushRPC.Type;

public class PushPackage implements Serializable {

	private static final long serialVersionUID = 1L;
	private Type type;
	private String piggyback;

	public PushPackage() {
	}

	public PushPackage(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public String getPiggyback() {
		return piggyback;
	}

	public void setPiggyback(String piggyback) {
		this.piggyback = piggyback;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
