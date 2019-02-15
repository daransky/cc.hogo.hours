package cc.hogo.hours.views.hours;

import java.util.Locale;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.swt.graphics.Font;

import cc.hogo.hours.core.AbstractTableLabelProvider;

public class HoursAllTableLabelProvider extends AbstractTableLabelProvider implements ITableFontProvider 
//implements ITableColorProvider, 
{

	@Override
	public String getColumnText(Object element, int columnIndex) {
		float values[] = null;
		String name = null;
		
		if (element instanceof HoursAllTableEntry) {
			HoursAllTableEntry e = (HoursAllTableEntry) element;
			name = e.getName();
			values = e.getValues();
		} else if (element instanceof HoursOfficeEntry) {
			HoursOfficeEntry e = (HoursOfficeEntry) element;
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
/*
	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		final HoursYearTableEntry e = (HoursYearTableEntry) element;
		if(columnIndex <= 1) 
			return null;
		
		float value = e.getHours(columnIndex - 2, true);
		return ( value > 0 && value < 10 ) ? Display.getCurrent().getSystemColor(SWT.COLOR_RED) : null;
	}
 */

	@Override
	public Font getFont(Object element, int columnIndex) {
		if(columnIndex == 0) {
			FontRegistry registry = JFaceResources.getFontRegistry();
			if (element instanceof HoursAllTableEntry) {
				return 	registry.getBold(JFaceResources.DEFAULT_FONT);
			}
			return registry.getItalic(JFaceResources.DEFAULT_FONT);
		}
		return null;
	}
}
