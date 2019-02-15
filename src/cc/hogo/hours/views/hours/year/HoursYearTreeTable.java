package cc.hogo.hours.views.hours.year;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.views.ColumnValueComparator;
import cc.hogo.hours.views.HoursEntry;

public class HoursYearTreeTable extends TreeViewer {

	public HoursYearTreeTable(Tree table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TreeViewerColumn col = addColumn("Vertreter", 190, SWT.RIGHT);
		new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> e1.getName().compareTo(e2.getName()) );
				
		col = addColumn("Total", 100, SWT.RIGHT);
		new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> Float.compare(e1.getYearSum(), e2.getYearSum()));
		
		for (int i = 0; i < Months.NAMES.length; i++) {
			col = addColumn(Months.NAMES[i], 90, SWT.RIGHT);
			final int month = i;
			new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> Double.compare(e1.getValue(month), e2.getValue(month)));
		}

		setContentProvider(new HoursYearTreeeTableContentProvider());
		setLabelProvider(new HoursYearTableLabelProvider());
		
	}

	TreeViewerColumn addColumn(String text, int width, int swt) {
		final TreeViewerColumn vc = new TreeViewerColumn(this, swt);
		final TreeColumn col = vc.getColumn();

		col.setText(text);
		col.setToolTipText(text);
		col.setWidth(width);

		return vc;
	}

	public void removeAll() {
		getTree().removeAll();
	}

}
