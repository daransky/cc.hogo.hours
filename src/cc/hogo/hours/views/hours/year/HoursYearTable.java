package cc.hogo.hours.views.hours.year;

import org.daro.common.ui.TableContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cc.hogo.hours.core.Months;

public class HoursYearTable extends TableViewer {

	public HoursYearTable(Table table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumn("Vertreter", 190, SWT.RIGHT);
		addColumn("Total", 100, SWT.RIGHT);

		for( int i = 0; i<Months.NAMES.length; i++ )
			addColumn(Months.NAMES[i], 90, SWT.RIGHT);
		
		setContentProvider(new TableContentProvider<>(this));
		setLabelProvider(new HoursYearTableLabelProvider());
	}
	
	TableViewerColumn addColumn(String text, int width, int swt) { 
		final TableViewerColumn col = new TableViewerColumn(this, swt);
		
		TableColumn c = col.getColumn(); 
		
		c.setText(text);
		c.setToolTipText(text);
		c.setWidth(width);
		
		TableViewerColumn hts = new TableViewerColumn(this, swt);
		c = hts.getColumn(); 
		c.setText("HTS");
		c.setWidth(90);
		
		TableViewerColumn hbs = new TableViewerColumn(this, swt);
		c = hbs.getColumn(); 
		c.setText("HBS");
		c.setWidth(90);
		
		return col;
	}

	public void removeAll() { 
		getTable().removeAll();
	}
}
