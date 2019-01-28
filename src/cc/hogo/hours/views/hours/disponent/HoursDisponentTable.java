package cc.hogo.hours.views.hours.disponent;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.views.ColumnValueComparator;

public class HoursDisponentTable extends TreeViewer {

	public HoursDisponentTable(Tree table, HoursViewDisponentModel model) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TreeViewerColumn col;
		col = addColumn("Firma", 300, SWT.LEFT);
		new ColumnValueComparator<HoursDisponentTableEntry>(this, col, (e1,e2) -> e1.getKundenname().compareTo(e2.getKundenname()) );
		
		col = addColumn("Total", 90, SWT.RIGHT);
		new ColumnValueComparator<HoursDisponentTableEntry>(this, col, (e1,e2) -> Float.compare(e1.getTotal(),  e2.getTotal()));

		addColumn("Gesch.", 60, SWT.CENTER);

		for( int i = 0; i<Months.NAMES.length; i++ ) {
			col = addColumn(Months.NAMES[i], 90, SWT.RIGHT);
			final int month = i;
			new ColumnValueComparator<HoursDisponentTableEntry>(this, col, (e1,e2) -> {
				float f1 = e1.current[month];
				float f2 = e2.current[month];
				return Float.compare(f1, f2);
			});
		}
		
		setContentProvider(new HoursDisponentTableContentProvider(model));
		setLabelProvider(new HoursDisponentTableLabelProvider());

	}
	
	TreeViewerColumn addColumn(String text, int width, int swt) { 
		TreeViewerColumn col = new TreeViewerColumn(this, swt);
		col.getColumn().setText(text);
		col.getColumn().setWidth(width);
		
		return col;
	}
}
