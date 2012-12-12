package org.sopeco.frontend.shared.push;

import java.io.Serializable;

import org.sopeco.frontend.client.rpc.PushRPC.Type;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushPackage implements Serializable {

	private static final long serialVersionUID = 1L;
	private Type type;

	public PushPackage() {
		type = Type.IDLE;
	}

	public PushPackage(Type pType) {
		type = pType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type pType) {
		type = pType;
	}
}
