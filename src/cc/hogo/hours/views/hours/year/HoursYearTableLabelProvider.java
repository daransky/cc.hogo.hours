package cc.hogo.hours.views.hours.year;

import java.util.Locale;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.swt.graphics.Font;

import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.views.hours.year.HoursYearTableEntry.Office;

public class HoursYearTableLabelProvider extends AbstractTableLabelProvider implements ITableFontProvider {

	@Override
	public String getColumnText(Object element, int columnIndex) {
		float values[] = null;
		String name = null;
		
		if (element instanceof HoursYearTableEntry) {
			HoursYearTableEntry e = (HoursYearTableEntry) element;
			name = e.getName();
			values = e.getValues();
		} else if (element instanceof Office) {
			Office e = (Office) element;
			name = e.getName();
			values = e.getSum();
		}
			
		switch (columnIndex) {
		case 0:
			return name;
		case 1:
			return String.format(Locale.GERMAN, "%,.2f", values[0]);
		default:
			return String.format(Locale.GERMAN, "%,.2f", values[columnIndex-1]);
		}
	} 

	@Override
	public Font getFont(Object element, int columnIndex) {
		if(columnIndex == 0) {
			FontRegistry registry = JFaceResources.getFontRegistry();
			if (element instanceof HoursYearTableEntry) {
				return 	registry.getBold(JFaceResources.DEFAULT_FONT);
			}
			return registry.getItalic(JFaceResources.DEFAULT_FONT);
		}
		return null;
	}
}
