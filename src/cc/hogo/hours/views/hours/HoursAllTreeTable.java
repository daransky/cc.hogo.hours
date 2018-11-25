package cc.hogo.hours.views.hours;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import cc.hogo.hours.core.Months;

public class HoursAllTreeTable extends TreeViewer {

	public HoursAllTreeTable(Tree table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumn("Jahr", 90, SWT.RIGHT);
		addColumn("Total", 100, SWT.RIGHT);

		for (int i = 0; i < Months.NAMES.length; i++)
			addColumn(Months.NAMES[i], 90, SWT.RIGHT);

		setContentProvider(new HoursAllTreeeTableContentProvider());
		setLabelProvider(new HoursAllTableLabelProvider());
	}

	TreeColumn addColumn(String text, int width, int swt) {
		Tree tree = getTree();
		final TreeColumn col = new TreeColumn(tree, swt);

		col.setText(text);
		col.setToolTipText(text);
		col.setWidth(width);

		return col;
	}

	public void removeAll() {
		getTree().removeAll();
	}

}
