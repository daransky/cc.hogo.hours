package cc.hogo.hours.views.hours.disponent;

import java.util.Locale;

import cc.hogo.hours.core.AbstractTableLabelProvider;

public class HoursDisponentTableLabelProvider extends AbstractTableLabelProvider
//		implements ITableColorProvider, ITableFontProvider
		{

	@Override
	public String getColumnText(Object element, int columnIndex) {
		HoursDisponentTableEntry e = (HoursDisponentTableEntry) element;
		switch (columnIndex) {
		case 0:
			return String.format(Locale.GERMAN, "%,.2f", e.getTotal());
		case 1:
			return e.getKundenname();
		case 2:
			return e.getKurzbezeichnung();
		default:
			return String.format(Locale.GERMAN, "%,.2f", e.getHours(columnIndex - 3));
		}
	}

	/*
	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		final HoursDisponentTableEntry e = (HoursDisponentTableEntry) element;
		if (columnIndex < 3)
			return null;

		float value = e.getHours(columnIndex - 3);
		return (value > 0 && value < 10) ? Display.getCurrent().getSystemColor(SWT.COLOR_RED) : null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		final HoursDisponentTableEntry e = (HoursDisponentTableEntry) element;
		if (columnIndex > 0 || columnIndex < 3)
			return null;
		float value = (columnIndex == 0) ? e.getTotal() : e.getHours(columnIndex - 3);

		FontRegistry registry = JFaceResources.getFontRegistry();
		return (value > 1000) ? registry.getBold(JFaceResources.DEFAULT_FONT)
				: registry.getItalic(JFaceResources.DEFAULT_FONT);
	}
	*/
}
