package org.sopeco.frontend.shared.helper;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.i18n.client.DateTimeFormat;

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
		while (!runNow(startTime, weekdays, hours, minutes)) {
			startTime += 60 * 1000;
		}
		return startTime;
	}

	public static long nextValidDate(String weekdays, String hours, String minutes) {
		long time = System.currentTimeMillis();
		MyDate date = new MyDate(time);
		time -= date.getMiliSeconds();
		time -= date.getSeconds() * 1000;
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

		return (set);
	}

	private static boolean runNow(long timestamp, String weekdays, String hours, String minutes) {
		MyDate date = new MyDate(timestamp);

		int nowHours = date.getHours();
		int nowMinutes = date.getMinutes();
		int nowDay = date.getDay();

		Set<Integer> setDays = splitString(weekdays);
		Set<Integer> setHours = expandHour(hours);
		Set<Integer> setMinutes = expandHour(minutes);

		if (setDays.contains(nowDay) && setHours.contains(nowHours) && setMinutes.contains(nowMinutes)) {
			return true;
		}
		return false;
	}

	private static Set<Integer> splitString(String src) {
		Set<Integer> set = new TreeSet<Integer>();
		String[] split = src.split(",");
		for (String s : split) {
			s = s.trim();
			if (s.isEmpty()) {
				continue;
			}
			set.add(Integer.parseInt(s));
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

		public int getDay() {
			String day = DateTimeFormat.getFormat("EEEE").format(date);
			if (day.equals("Sunday")) {
				return 1;
			} else if (day.equals("Monday")) {
				return 2;
			} else if (day.equals("Tuesday")) {
				return 3;
			} else if (day.equals("Wednesday")) {
				return 4;
			} else if (day.equals("Thursday")) {
				return 5;
			} else if (day.equals("Friday")) {
				return 6;
			} else if (day.equals("Saturday")) {
				return 7;
			}
			return -1;
		}

		public int getHours() {
			return Integer.parseInt(DateTimeFormat.getFormat("H").format(date));
		}

		public int getMiliSeconds() {
			return Integer.parseInt(DateTimeFormat.getFormat("S").format(date));
		}

		public int getMinutes() {
			return Integer.parseInt(DateTimeFormat.getFormat("m").format(date));
		}

		public int getSeconds() {
			return Integer.parseInt(DateTimeFormat.getFormat("s").format(date));
		}
	}
}
