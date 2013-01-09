package org.sopeco.frontend.shared.push;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushObjectPackage extends PushPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PushSerializable attachment;

	/**
	 * Returns the attachment.
	 * 
	 * @return the attachment
	 */
	public PushSerializable getAttachment() {
		return attachment;
	}

	/**
	 * Returns the attachment. The attachment is cast to the given class.
	 * 
	 * @return the attachment
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttachment(Class<T> clazz) {
		return (T) attachment;
	}

	/**
	 * Sets the given object as the attached object.
	 * 
	 * @param pAttachment
	 *            the attachment to set
	 */
	public void setAttachment(PushSerializable pAttachment) {
		attachment = pAttachment;
	}

}
