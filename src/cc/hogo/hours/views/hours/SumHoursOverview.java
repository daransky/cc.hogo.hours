package cc.hogo.hours.views.hours;

import java.util.Locale;

import org.daro.common.ui.TableContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.core.Months;

public class SumHoursOverview {
	private final TableViewer viewer;
	
	public SumHoursOverview(Composite section) {
		section.setLayout(new GridLayout());

		Table table = new Table(section, SWT.BORDER);
		table.setLinesVisible(false);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		viewer = new TableViewer(table);
		viewer.setContentProvider(new TableContentProvider<>(viewer));
		viewer.setLabelProvider(new AbstractTableLabelProvider() {
			
			@Override
			public String getColumnText(Object element, int columnIndex) {
				final float[] sum = (float[])element;
				return String.format(Locale.GERMAN, "%,.2f", sum[columnIndex]);
			}
		});
		
		TableColumn total = new TableColumn(table, SWT.RIGHT);
		total.setText("Total");
		total.setWidth(120);
		
		for( int i = 0; i < Months.NAMES.length; i++) {
			TableColumn c = new TableColumn(table, SWT.RIGHT);
			c.setText(Months.NAMES[i]);
			c.setMoveable(false);
			c.setWidth(100);
		}
	}
	
	public void setRecord(float[] record) {
		viewer.getTable().removeAll();
		viewer.add(record);
		
	}

}
