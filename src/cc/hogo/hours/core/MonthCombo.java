package cc.hogo.hours.core;

import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class MonthCombo {

	public static Combo	create(Composite parent) {
		Combo combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		combo.setItems(Months.NAMES);
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH);
		combo.select(month);
		
		return combo;
	}
}
