package cc.hogo.hours.views.hours.disponent;

import java.util.Locale;

import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.views.hours.disponent.HoursDisponentTableEntry.SubEntry;

public class HoursDisponentTableLabelProvider extends AbstractTableLabelProvider
//		implements ITableColorProvider, ITableFontProvider
{

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof HoursDisponentTableEntry) {
			HoursDisponentTableEntry e = (HoursDisponentTableEntry) element;
			switch (columnIndex) {
			case 0:
				return e.getKundenname();
			case 1:
				return String.format(Locale.GERMAN, "%,.2f", e.getTotal());
			case 2:
				return e.getKurzbezeichnung();
			default:
				return String.format(Locale.GERMAN, "%,.2f", e.getHours(columnIndex - 3));
			}
		}
		if( element instanceof SubEntry ) { 
			SubEntry e = (SubEntry)element;
			if( columnIndex > 2 && e.value[columnIndex-3] > 0f) 
				return String.format(Locale.GERMAN, "%,.2f", e.value[columnIndex-3] );
		}
		return "";
	}

	/*
	 * @Override public Color getForeground(Object element, int columnIndex) {
	 * return null; }
	 * 
	 * @Override public Color getBackground(Object element, int columnIndex) { final
	 * HoursDisponentTableEntry e = (HoursDisponentTableEntry) element; if
	 * (columnIndex < 3) return null;
	 * 
	 * float value = e.getHours(columnIndex - 3); return (value > 0 && value < 10) ?
	 * Display.getCurrent().getSystemColor(SWT.COLOR_RED) : null; }
	 * 
	 * @Override public Font getFont(Object element, int columnIndex) { final
	 * HoursDisponentTableEntry e = (HoursDisponentTableEntry) element; if
	 * (columnIndex > 0 || columnIndex < 3) return null; float value = (columnIndex
	 * == 0) ? e.getTotal() : e.getHours(columnIndex - 3);
	 * 
	 * FontRegistry registry = JFaceResources.getFontRegistry(); return (value >
	 * 1000) ? registry.getBold(JFaceResources.DEFAULT_FONT) :
	 * registry.getItalic(JFaceResources.DEFAULT_FONT); }
	 */
}
