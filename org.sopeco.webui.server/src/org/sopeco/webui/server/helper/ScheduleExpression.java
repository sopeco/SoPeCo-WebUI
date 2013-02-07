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
package org.sopeco.webui.server.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 */
public final class ScheduleExpression {

	/**
	 * Allowed values are 0-23.<br>
	 * The '*' character is used to specify all values.<br>
	 * The '-' character is used to specify ranges. For example "10-12" means
	 * 10,11,12<br>
	 * The ',' character is used to separate values. For example "1,2,3"<br>
	 * The '/' character is used to specify increments. For example "0/15" means
	 * 0,15
	 * 
	 * @param src
	 * @param limit
	 * @return
	 */
	public static Set<Integer> expandHour(String expr) {
		return expand(expr, 23);
	}

	/**
	 * Allowed values are 0-59.<br>
	 * The '*' character is used to specify all values.<br>
	 * The '-' character is used to specify ranges. For example "10-12" means
	 * 10,11,12<br>
	 * The ',' character is used to separate values. For example "1,2,3"<br>
	 * The '/' character is used to specify increments. For example "0/15" means
	 * 0,15,30,45
	 * 
	 * @param src
	 * @param limit
	 * @return
	 */
	public static Set<Integer> expandMinutes(String expr) {
		return expand(expr, 59);
	}

	public static long nextValidDate(long startTime, String weekdays, String hours, String minutes) {
		if (weekdays == null || hours == null || minutes == null) {
			return -1;
		}
		if (startTime < System.currentTimeMillis()) {
			startTime = System.currentTimeMillis();
		}
		while (!runNow(startTime, weekdays, hours, minutes)) {
			startTime += 60 * 1000;
		}
		return startTime;
	}

	public static long nextValidDate(String weekdays, String hours, String minutes) {
		if (weekdays == null || hours == null || minutes == null) {
			return -1;
		}

		long time = System.currentTimeMillis();
		MyDate date = new MyDate(time);
		time -= date.getMiliSeconds();
		time -= date.getSeconds() * 1000;
		time += 60 * 1000;
		return nextValidDate(time, weekdays, hours, minutes);
	}

	public static boolean runNow(String weekdays, String hours, String minutes) {
		return runNow(System.currentTimeMillis(), weekdays, hours, minutes);
	}

	/**
	 * 
	 */
	public static String setToString(Set<Integer> set) {
		String result = "";
		for (Iterator<Integer> i = set.iterator(); i.hasNext();) {
			result += i.next();
			if (i.hasNext()) {
				result += ",";
			}
		}
		return result;
	}

	private static Set<Integer> expand(String src, int limit) {
		Set<Integer> set = new TreeSet<Integer>();

		if (src != null) {
			String[] split = src.split(",");
			for (String s : split) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}

				if (s.matches("\\d+")) {
					int i = Integer.parseInt(s);
					set.add(i);
				} else if (s.matches("\\d+-\\d+")) {
					int start = Integer.parseInt(s.substring(0, s.indexOf("-")));
					int end = Integer.parseInt(s.substring(s.indexOf("-") + 1));
					for (int x = start; x <= end && x <= limit; x++) {
						set.add(x);
					}
				} else if (s.matches("\\d+/\\d+")) {
					int start = Integer.parseInt(s.substring(0, s.indexOf("/")));
					int inter = Integer.parseInt(s.substring(s.indexOf("/") + 1));
					for (int x = start; x <= limit; x += inter) {
						set.add(x);
					}
				} else if (s.equals("*")) {
					for (int x = 0; x <= limit; x++) {
						set.add(x);
					}
				}
			}
		}

		return (set);
	}

	private static boolean runNow(long timestamp, String weekdays, String hours, String minutes) {
		MyDate date = new MyDate(timestamp);

		int nowHours = date.getHours();
		int nowMinutes = date.getMinutes();
		int nowDay = date.getDayOfWeek();

		Set<Integer> setDays = splitString(weekdays);
		Set<Integer> setHours = expandHour(hours);
		Set<Integer> setMinutes = expandMinutes(minutes);

		if (setDays.contains(nowDay) && setHours.contains(nowHours) && setMinutes.contains(nowMinutes)) {
			return true;
		}
		return false;
	}

	private static Set<Integer> splitString(String src) {
		Set<Integer> set = new TreeSet<Integer>();
		if (src != null) {
			String[] split = src.split(",");
			for (String s : split) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}
				set.add(Integer.parseInt(s));
			}
		}
		return set;
	}

	private ScheduleExpression() {
	}

	private static class MyDate {
		private Date date;

		public MyDate(long timestamp) {
			date = new Date(timestamp);
		}

		public int getDayOfWeek() {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.DAY_OF_WEEK);
		}

		public int getHours() {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.HOUR_OF_DAY);
		}

		public int getMiliSeconds() {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.MILLISECOND);
		}

		public int getMinutes() {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.MINUTE);
		}

		public int getSeconds() {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.SECOND);
		}
	}
}
