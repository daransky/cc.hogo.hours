package cc.hogo.hours.views.hours;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.views.ColumnValueComparator;
import cc.hogo.hours.views.HoursEntry;

public class HoursAllTreeTable extends TreeViewer {

	public HoursAllTreeTable(Tree table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TreeViewerColumn col = addColumn("Jahr", 90, SWT.RIGHT);
		new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> e1.getName().compareTo(e2.getName()) );
		
		col = addColumn("Total", 100, SWT.RIGHT);
		new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> Float.compare(e1.getYearSum(), e2.getYearSum()));

		for (int i = 0; i < Months.NAMES.length; i++) {
			col = addColumn(Months.NAMES[i], 90, SWT.RIGHT);
			final int month = i;
			new ColumnValueComparator<HoursEntry>(this, col, (e1,e2) -> Double.compare(e1.getValue(month), e2.getValue(month)));
		}

		setContentProvider(new HoursAllTreeeTableContentProvider());
		setLabelProvider(new HoursAllTableLabelProvider());
	}

	TreeViewerColumn addColumn(String text, int width, int swt) {
		Tree tree = getTree();
		final TreeColumn col = new TreeColumn(tree, swt);

		col.setText(text);
		col.setToolTipText(text);
		col.setWidth(width);

		return new TreeViewerColumn(this, col);
	}

	public void removeAll() {
		getTree().removeAll();
	}

}
