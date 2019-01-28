package cc.hogo.hours.views.hours;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import org.daro.common.ui.UIError;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.core.export.HtmlTable;
import cc.hogo.hours.core.export.HtmlTable.HtmlRecord;
import cc.hogo.hours.views.HoursAbstractView;
import cc.hogo.hours.views.HoursChart;

public class HoursViewAll extends HoursAbstractView {

	public static final String ID = "cc.hogo.hours.views.hours.HoursViewAll";
	private HoursAllTreeTable table;
	private HoursViewAllModel model;
	private HoursChart chart;

	@Override
	public void setInput(Object node) {
		refresh();
	}

	protected Composite createChart(Composite parent) {
		chart = new HoursChart(parent, SWT.NONE);
		chart.setTitle("Gesamtübersicht");
		return chart;
	}

	protected Composite createTable(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout());
		top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		Tree t = new Tree(top, SWT.BORDER | SWT.FULL_SELECTION);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
		table = new HoursAllTreeTable(t);

		return top;
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			model = HoursViewAllModel.open();
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}

		super.createPartControl(parent);

		addExportToClipboardMenu(() -> {
			final int FIRMA = 500;
			final int TOTAL = 50;
			final int MONTH = 30;
			final TreeItem[] items = table.getTree().getItems();
			final HtmlTable htmlTable = new HtmlTable();

			HtmlRecord rec = htmlTable.addRecord().addValue("Firma", FIRMA).addValue("Total", TOTAL).setHeader(true);
			for (String s : Months.NAMES)
				rec.addValue(s, MONTH);

			for (int i = 0, max = items.length; i < max; i++) {
				HoursAllTableEntry id = (HoursAllTableEntry) items[i].getData();
				rec = htmlTable.addRecord().addValue(id.getName(), 500).addValue(Float.toString(id.getTotal()),
						50);
				for (int m = 0; m < 12; m++)
					rec.addValue(Float.toString(id.getHours(m)), 30);
			}
			return htmlTable.toHtml();
		});

		addExportToFileMenu((path) -> {
			final TreeItem[] items = table.getTree().getItems();
			try (PrintWriter out = new PrintWriter(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE))) {

				out.write("Firma");
				out.write(';');
				out.write("Total");
				out.write(';');
				for (String s : Months.NAMES) {
					out.write(s);
					out.write(';');
				}
				out.println();
				
				for (int i = 0, max = items.length; i < max; i++) {
					HoursAllTableEntry id = (HoursAllTableEntry) items[i].getData();
					out.write(id.getName());
					out.write(';');
					out.printf(Locale.GERMAN, "%,.2f", id.getTotal());
					out.write(';');	
					for (int m = 0; m < 12; m++) {
						out.printf(Locale.GERMAN, "%,.2f", id.getHours(m));
						out.write(';');
					}
					out.println();
				}
			} catch( IOException e ) {
				UIError.showError(getSite().getShell(), "Fehler beim export", "Daten konnten nicht gespeichert werden, Ursache:\n"+e.getMessage() , e);
			}
		});
		refresh();
	}

	@Override
	public void setFocus() { //
	}

	void refreshTable(Collection<HoursAllTableEntry> data) {
		table.removeAll();
		table.setInput(data);
		table.expandAll();
	}

	@Override
	public void refresh() {

		try {
			final Collection<HoursAllTableEntry> result = model.load();

			refreshTable(result);
			refreshChart(result);

		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}
	}

	private void refreshChart(Collection<HoursAllTableEntry> result) {
		chart.removeSeries();

		for (HoursAllTableEntry e : result) {
			chart.addSeries(e.getName() + ' ' + String.format(Locale.GERMAN, "%,.2f", e.getTotal()), e.getMonthValues());
		}

		chart.getChart().getAxisSet().adjustRange();
	}

	@Override
	public String getViewID() {
		return ID;
	}

}
