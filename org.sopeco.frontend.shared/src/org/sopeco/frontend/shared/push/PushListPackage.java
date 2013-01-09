package org.sopeco.frontend.shared.push;

import java.util.ArrayList;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushListPackage extends PushPackage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<PushSerializable> attachment = new ArrayList<PushSerializable>();

	/**
	 * Returns the attachment.
	 * 
	 * @return the attachment
	 */
	public ArrayList<PushSerializable> getAttachment() {
		return attachment;
	}

	/**
	 * Returns the attached list. The elements are cast to the given class.
	 * 
	 * @return the attachment
	 */
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> getAttachment(Class<T> clazz) {
		return (ArrayList<T>) attachment;
	}

	/**
	 * Sets the given ArrayList as the attached list.
	 * 
	 * @param pAttachment
	 *            the attachment to set
	 */
	public void setAttachment(ArrayList<PushSerializable> pAttachment) {
		attachment = pAttachment;
	}
}
