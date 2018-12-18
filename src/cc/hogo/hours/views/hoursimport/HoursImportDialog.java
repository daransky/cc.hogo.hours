package cc.hogo.hours.views.hoursimport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.daro.common.ui.TableContentProvider;
import org.daro.common.ui.UIError;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import cc.hogo.hours.core.AbstractTableLabelProvider;
import cc.hogo.hours.db.DB;
import cc.hogo.hours.views.log.Logger;

public class HoursImportDialog extends DefaultSizeDialog {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy,hh:mm:ss");

	public static class ImportEntry {
		public final LocalDateTime time;
		public final int firstRecord;
		public final int lastRecord;

		public ImportEntry(LocalDateTime date, int first, int last) {
			this.time = date;
			this.firstRecord = first;
			this.lastRecord = last;
		}

		public int getRecordCount() {
			return lastRecord - firstRecord;
		}

		public String getTime() {
			return time != null ? FORMATTER.format(time) : "";
		}

		@Override
		public String toString() {
			return getTime() + " (" + firstRecord + " - " + lastRecord + ")";
		}
	}

	AbstractTableLabelProvider labelProvider = new AbstractTableLabelProvider() {
		@Override
		public String getColumnText(Object element, int columnIndex) {
			ImportEntry e = (ImportEntry) element;
			return columnIndex == 0 ? e.getTime() : Integer.toString(e.getRecordCount());
		}
	};

	TableViewer viewer = null;
	ImportEntry selected;

	public HoursImportDialog(ViewPart parent) {
		super(parent.getSite().getShell());
		setDefaultImage(parent.getTitleImage());
	}

	@Override
	public Control createDialogArea(Composite parent) {

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());

		Table table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		viewer = new TableViewer(table);

		addColumn("Datum", 250, SWT.LEFT);
		addColumn("Sätze", 150, SWT.RIGHT);

		viewer.setContentProvider(new TableContentProvider<>(viewer));
		viewer.setLabelProvider(labelProvider);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selected = (ImportEntry) table.getItem(table.getSelectionIndex()).getData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//
			}
		});

		try {
			load();
		} catch (SQLException e1) {
			UIError.showError("Fehler beim lesen aus Log", e1);
		}

		return container;
	}

	TableViewerColumn addColumn(String text, int width, int swt) {
		final TableViewerColumn col = new TableViewerColumn(viewer, swt);

		TableColumn c = col.getColumn();

		c.setText(text);
		c.setToolTipText(text);
		c.setWidth(width);

		return col;
	}

	void load() throws SQLException {
		try (Statement stmt = DB.instance().getConnection().createStatement()) {
			try (ResultSet rs = stmt.executeQuery("select logmsg, logdate from log where logid = "
					+ Logger.IMPORT_COMPLETED + " order by logdate desc")) {
				while (rs.next()) {

					String msg = rs.getString(1);
					Timestamp date = rs.getTimestamp(2);

					int end1 = msg.lastIndexOf('\'');
					int end2 = msg.lastIndexOf('\'', end1 - 1);
					try {
						String[] ins = msg.substring(end2 + 1, end1).split("-");

						viewer.add(new ImportEntry(date.toLocalDateTime(), Integer.parseInt(ins[0]),
								Integer.parseInt(ins[1])));
					} catch (Exception e) {

					}
				}
			}
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Import auswählen");
	}

	@Override
	protected void okPressed() {
		if (selected != null) {
			if (MessageDialog.openQuestion(getShell(), "Import Löschen",
					String.format("Willst du den gesamten Import '%s' wirklich löschen?", selected.getTime()))) {
				try (Statement stmt = DB.instance().getConnection().createStatement()) {
					stmt.executeUpdate(String.format("delete from hours where id >= %d AND id <= %d",
							selected.firstRecord, selected.lastRecord));
					Logger.instance().write(Logger.newImportDeleted(selected.toString()));
				} catch (SQLException e) {
					UIError.showError("Fehler beim löschen", e);
				}
				super.okPressed();
			}
		}
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
}
