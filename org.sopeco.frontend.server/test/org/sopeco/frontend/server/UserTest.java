package org.sopeco.frontend.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class UserTest {

	@Test
	public void addAndRemoveUser() {
		String id = "testId";

		User user = new User(id);

		UserManager.setUser(user);
		assertEquals(UserManager.getUser(id), user);
		assertTrue(UserManager.existSession(id));
		UserManager.removeUser(user);
		assertTrue(UserManager.getAllUsers().isEmpty());
	}
}
