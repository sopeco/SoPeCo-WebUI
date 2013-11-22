package org.sopeco.webui.client.layout.center.execute.tabFour;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Image;

public class MyImageCell extends AbstractCell<Image> {

	interface MyTemplate extends SafeHtmlTemplates {
		@Template("<img src=\"{0}\" style=\"{1}\"/>")
		SafeHtml img(String url, SafeStyles style);
	}

	private static MyTemplate template;

	public MyImageCell() {
		if (template == null) {
			template = GWT.create(MyTemplate.class);
		}
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, Image value, SafeHtmlBuilder sb) {
		if (value != null) {
			SafeStyles style = SafeStylesUtils.fromTrustedString(value.getElement().getAttribute("style"));
			sb.append(template.img(value.getElement().getAttribute("src"), style));
		}
	}

}
