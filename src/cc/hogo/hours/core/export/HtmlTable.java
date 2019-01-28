package cc.hogo.hours.core.export;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HtmlTable {
	final String TEMPLATEBegin = "<html><head>\r\n"
			+ "<meta http-equiv=Content-Type content=\"text/html; charset=windows-1252\">\r\n"
			+ "<meta name=Generator content=\"Microsoft Word 15 (filtered)\">\r\n" + "<style>\r\n" + "<!--\r\n"
			+ " /* Font Definitions */\r\n" + " @font-face\r\n" + "	{font-family:\"Cambria Math\";\r\n"
			+ "	panose-1:2 4 5 3 5 4 6 3 2 4;}\r\n" + "@font-face\r\n" + "	{font-family:Calibri;\r\n"
			+ "	panose-1:2 15 5 2 2 2 4 3 2 4;}\r\n" + " /* Style Definitions */\r\n"
			+ " p.MsoNormal, li.MsoNormal, div.MsoNormal\r\n" + "	{margin-top:0cm;\r\n"
			+ "	margin-right:0cm;\r\n" + "	margin-bottom:8.0pt;\r\n" + "	margin-left:0cm;\r\n"
			+ "	line-height:107%;\r\n" + "	font-size:11.0pt;\r\n" + "	font-family:\"Calibri\",sans-serif;}\r\n"
			+ ".MsoChpDefault\r\n" + "	{font-family:\"Calibri\",sans-serif;}\r\n" + ".MsoPapDefault\r\n"
			+ "	{margin-bottom:8.0pt;\r\n" + "	line-height:107%;}\r\n" + "@page WordSection1\r\n"
			+ "	{size:612.0pt 792.0pt;\r\n" + "	margin:70.85pt 70.85pt 2.0cm 70.85pt;}\r\n" + "div.WordSection1\r\n"
			+ "	{page:WordSection1;}\r\n" + "-->\r\n" + "</style>\r\n" + "</head>\r\n" + "<body lang=EN-US>\r\n"
			+ "<div class=WordSection1>\r\n"
			+ "<table class=MsoTableGrid border=1 cellspacing=0 cellpadding=0 style='border-collapse:collapse;border:none'>\r\n";
	
	final String TEMPLATEEnd = "</table><p class=MsoNormal><span lang=DE-AT>&nbsp;</span></p></div></body></html>";
	
	public static class HtmlRecord {
		final List<Integer> widths = new LinkedList<>();
		final List<String> values = new LinkedList<>();
		boolean header;

		public HtmlRecord setHeader(boolean header) {
			this.header = header;
			return this;
		}

		public HtmlRecord addValue(String value, int width) {
			widths.add(width);
			values.add(value);
			return this;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(1024);
			sb.append("<tr>\r\n");

			Iterator<Integer> w = widths.iterator();
			Iterator<String> v = values.iterator();

			for (int i = 0, max = values.size(); i < max; i++) {
				int width = w.next();
				String value = v.next();
				
				sb.append(String.format(
						"<td width=%d valign=top style='width:4.0cm;border:solid windowtext 1.0pt; padding:0cm 5.4pt 0cm 5.4pt'><p class=MsoNormal style='margin-bottom:0cm;margin-bottom:.0001pt;line-height:normal'>",
						width));
				if (header)
					sb.append("<b>");

				sb.append(String.format("<span lang=DE-AT>%s</span>", value));

				if (header)
					sb.append("</b>");
				sb.append("</p></td>\r\n");
			}
			sb.append("</tr>\r\n");
			return sb.toString();
		}
	}

	final List<HtmlRecord> records = new LinkedList<>();
	
	public HtmlRecord addRecord() { 
		HtmlRecord rec = new HtmlRecord();
		records.add(rec);
		return rec;
	}
	
	public String toHtml() { 
		StringBuilder sb = new StringBuilder(4*1024);
		sb.append(TEMPLATEBegin);
		records.forEach( rec -> sb.append(rec.toString()));
		sb.append(TEMPLATEEnd);
		return sb.toString();
	}
}