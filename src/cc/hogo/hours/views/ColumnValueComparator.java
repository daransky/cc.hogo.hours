package cc.hogo.hours.views;

import java.util.Comparator;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;

public class ColumnValueComparator<T> extends ViewerComparator {

	public static final int ASC = 1;
	public static final int NONE = 0;
	public static final int DESC = -1;

	private int direction = 0;
	private final TreeViewerColumn column;
	private final ColumnViewer viewer;
	private final Comparator<T> comparator;

	public ColumnValueComparator(ColumnViewer viewer, TreeViewerColumn column, Comparator<T> comparator) {
		this.column = column;
		this.viewer = viewer;
		SelectionAdapter selectionAdapter = createSelectionAdapter();
		this.column.getColumn().addSelectionListener(selectionAdapter);
		this.comparator = comparator;
	}

	private SelectionAdapter createSelectionAdapter() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (ColumnValueComparator.this.viewer.getComparator() != null) {
					if (ColumnValueComparator.this.viewer.getComparator() == ColumnValueComparator.this) {
						int tdirection = ColumnValueComparator.this.direction;
						if (tdirection == ASC) {
							setSorter(ColumnValueComparator.this, DESC);
						} else if (tdirection == DESC) {
							setSorter(ColumnValueComparator.this, NONE);
						}
					} else {
						setSorter(ColumnValueComparator.this, ASC);
					}
				} else {
					setSorter(ColumnValueComparator.this, ASC);
				}
			}
		};
	}

	public void setSorter(ColumnValueComparator<T> sorter, int direction) {
		Tree columnParent = column.getColumn().getParent();
		if (direction == NONE) {
			columnParent.setSortColumn(null);
			columnParent.setSortDirection(SWT.NONE);
			viewer.setComparator(null);

		} else {
			columnParent.setSortColumn(column.getColumn());
			sorter.direction = direction;
			columnParent.setSortDirection(direction == ASC ? SWT.DOWN : SWT.UP);

			if (viewer.getComparator() == sorter) {
				viewer.refresh();
			} else {
				viewer.setComparator(sorter);
			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return ( e1.getClass().isInstance(e2)) ? 
				direction * comparator.compare((T)e1, (T)e2) : 
					0;
	}

}