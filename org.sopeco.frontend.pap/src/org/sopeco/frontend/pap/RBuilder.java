package org.sopeco.frontend.pap;

public class RBuilder {

	public static void createLegend(StringBuffer buffer, String[] names){
		buffer.append("\nlegend(\"topleft\", c(");
		for (int i = 1; i < names.length; i++){
			buffer.append("\""+names[i]+i+ "\"");
			if (i < names.length -1){
				buffer.append(",");
			}
		}
		buffer.append("), col=rainbow("+(names.length-1)+"),lwd=2,bty=\"n\")");
	}
}
