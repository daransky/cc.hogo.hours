package cc.hogo.hours.wizard.imp;

import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.db.HourEntry;

public class ImportLabelProvider extends AbstractTableLabelProvider {

	private int id = 1;
	
	@Override
	public String getColumnText(Object element, int columnIndex) {
		HourEntry e = (HourEntry)element;
		switch( columnIndex ) { 
			case 0:	return Integer.toString(id++);
			case 1:	return e.getKundenName();
			case 2:	return e.getDisponentId();
			case 3:	return e.getVorname() + ' ' + e.getNachname();
			case 4: return Float.toString(e.getFakturStunden());
			case 5:	return Float.toString(e.getLohnStunden());
			default: return "???";
		}
		
	}

	public void reset() { 
		id = 1;
	}
}
