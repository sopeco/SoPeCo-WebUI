package org.sopeco.frontend.shared.helper;

import java.io.Serializable;
import java.util.Comparator;

public class ChartXLabelComparator implements Comparator<String>, Serializable {

	@Override
	public int compare(String o1, String o2) {
		String[] s1 = o1.split("\\.");
		String[] s2 = o2.split("\\.");
		for (int i = 0; i< s1.length; i++){
			Integer i1 = Integer.parseInt(s1[i]);
			Integer i2 = Integer.parseInt(s2[i]);
			if (i1 != i2){
				return i1.compareTo(i2);
			}
		}
		return 0;
	}

}
