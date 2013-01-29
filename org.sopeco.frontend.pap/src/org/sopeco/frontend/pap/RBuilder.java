package org.sopeco.frontend.pap;

import java.util.List;

public class RBuilder {

	public static void createLegend(StringBuffer buffer, List<String> names){
		buffer.append("\nlegend(\"topleft\", c(");
		for (int i = 1; i < names.size(); i++){
			buffer.append("\""+names.get(i)+i+ "\"");
			if (i < names.size() -1){
				buffer.append(",");
			}
		}
		buffer.append("), col=rainbow("+(names.size()-1)+"),lwd=2,bty=\"n\")");
	}
}
