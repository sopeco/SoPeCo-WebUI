package org.sopeco.frontend.shared.helper;

import java.util.HashMap;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class Metering {
	private static HashMap<Double, Long> map = new HashMap<Double, Long>();

	private Metering() {
	}

	public static double start() {
		double rand = Math.random();

		map.put(rand, System.currentTimeMillis());
		
		return rand;
	}

	public static void stop(double key) {
		Exception ex = new Exception();
		StackTraceElement stackTop = ex.getStackTrace()[1];

		long duration = System.currentTimeMillis() - map.get(key);

		System.err.println("Metering: " + stackTop.getClassName() + " "
				+ stackTop.getMethodName() + "() : " + duration + "ms");
	}
}
