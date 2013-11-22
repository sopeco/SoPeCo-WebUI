package org.sopeco.frontend.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sopeco.webui.server.helper.ScheduleExpression;

public class ScheduleExpressionTest {

	@Test
	public void testExpand() {
		// 0,1,2,..,58,59
		assertEquals(ScheduleExpression.expandMinutes("*").size(), 60);
		assertEquals(ScheduleExpression.expandHour("*").size(), 24);

		assertEquals(ScheduleExpression.expandHour("*"), ScheduleExpression.expandHour("0-23"));

		// 0,1,5
		assertEquals(ScheduleExpression.expandHour("0,1,5,80").size(), 3);

		// 0,1,10,11,12,13,14,15
		assertEquals(ScheduleExpression.setToString(ScheduleExpression.expandHour("0,1,10-15")),
				"0,1,10,11,12,13,14,15");

	}

	@Test
	public void testNextDate() {
		long nextDate = ScheduleExpression.nextValidDate("0,1,2,3,4,5,6", "*", "*");
		long current = System.currentTimeMillis();

		// Max. 60seconds
		assertTrue(Math.abs(nextDate - current) < 1000 * 60);

		current += 1000 * 3600;
		nextDate = ScheduleExpression.nextValidDate(current, "0,1,2,3,4,5,6", "*", "*");

		assertEquals(nextDate, current);

		// Max. 60seconds
		assertTrue(Math.abs(nextDate - System.currentTimeMillis() - 1000 * 3600) < 1000 * 60);
	}
}
