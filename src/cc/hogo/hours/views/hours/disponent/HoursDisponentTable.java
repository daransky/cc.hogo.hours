package cc.hogo.hours.views.hours.disponent;

import org.daro.common.ui.TableContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

import cc.hogo.hours.core.Months;

public class HoursDisponentTable extends TableViewer {

	public HoursDisponentTable(Table table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumn("Total", 90, SWT.RIGHT);
		addColumn("Firma", 300, SWT.LEFT);
		addColumn("Gesch.", 40, SWT.CENTER);

		for( int i = 0; i<Months.NAMES.length; i++ )
			addColumn(Months.NAMES[i], 90, SWT.RIGHT);
		
		setContentProvider(new TableContentProvider<>(this));
		setLabelProvider(new HoursDisponentTableLabelProvider());
	}
	
	TableViewerColumn addColumn(String text, int width, int swt) { 
		TableViewerColumn col = new TableViewerColumn(this, swt);
		col.getColumn().setText(text);
		col.getColumn().setWidth(width);
		return col;
	}

}
