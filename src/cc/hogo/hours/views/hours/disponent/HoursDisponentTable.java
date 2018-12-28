package cc.hogo.hours.views.hours.disponent;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;

import cc.hogo.hours.core.Months;

public class HoursDisponentTable extends TreeViewer {

	public HoursDisponentTable(Tree table, HoursViewDisponentModel model) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumn("Firma", 300, SWT.LEFT);
		addColumn("Total", 90, SWT.RIGHT);
		addColumn("Gesch.", 60, SWT.CENTER);

		for( int i = 0; i<Months.NAMES.length; i++ )
			addColumn(Months.NAMES[i], 90, SWT.RIGHT);
		
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
