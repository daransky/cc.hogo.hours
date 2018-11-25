package cc.hogo.hours.views.log;

import org.daro.common.ui.TableContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

public class LogTable extends TableViewer {

	public LogTable(Table table) {
		super(table);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		addColumn("", 30, SWT.CENTER);
		addColumn("Datum", 180, SWT.NONE);
		addColumn("Text", 600, SWT.NONE);

		setContentProvider(new TableContentProvider<>(this));
		setLabelProvider(new LogTableLabelProvider());
	}
	
	TableViewerColumn addColumn(String text, int width, int swt) { 
		TableViewerColumn col = new TableViewerColumn(this, swt);
		col.getColumn().setText(text);
		col.getColumn().setWidth(width);
		return col;
	}

}
