package org.sopeco.frontend.pap;

import java.util.List;

public class RBuilder {

	public static void createLegend(StringBuffer buffer, List<String> names){
		buffer.append("\nlegend(range(x)[2],range(ca)[2], c(");
		for (int i = 0; i < names.size(); i++){
			buffer.append("\""+names.get(i)+"\"");
			if (i < names.size() -1){
				buffer.append(",");
			}
		}
		buffer.append("), col=rainbow("+names.size()+"),lwd=2,bty=\"n\")");
	}
}
