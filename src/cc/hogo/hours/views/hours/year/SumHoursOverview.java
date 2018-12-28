package cc.hogo.hours.views.hours.year;

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
				SumHoursOverviewRecord rec = (SumHoursOverviewRecord)element;
				if( rec == null)
					return null;
				
				if(columnIndex == 0 )
					return rec.getName();
				
				return rec.getSumAsString(columnIndex-1);
			}
		});
		
		TableColumn name = new TableColumn(table, SWT.RIGHT);
		name.setText("Name");
		name.setWidth(90);
		
		TableColumn total = new TableColumn(table, SWT.RIGHT);
		total.setText("Total");
		total.setWidth(90);
		
		for( int i = 0; i < Months.NAMES.length; i++) {
			TableColumn c = new TableColumn(table, SWT.RIGHT);
			c.setText(Months.NAMES[i]);
			c.setMoveable(false);
			c.setWidth(90);
		}
	}
	
	public void add(SumHoursOverviewRecord[] records) {
		viewer.getTable().removeAll();
		viewer.add(records);
	}

}
