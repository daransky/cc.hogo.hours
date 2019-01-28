package cc.hogo.hours.views.hours.year;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Locale;

import org.daro.common.ui.TreeNodeData;
import org.daro.common.ui.UIError;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.swtchart.IAxis;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.core.export.HtmlTable;
import cc.hogo.hours.core.export.HtmlTable.HtmlRecord;
import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.views.HoursAbstractView;
import cc.hogo.hours.views.HoursChart;

public class HoursViewYear extends HoursAbstractView {

	public static final String ID = "cc.hogo.hours.views.hours.year.HoursViewYear";
	private HoursYearTreeTable table;
	private TreeNodeData node;
	private HoursViewYearModel model;
	private SumHoursOverview sum;
	private HoursChart chart, officeChart;

	public void setInput(Object node) {
		this.node = (TreeNodeData) node;
		refresh();
	}

	Composite createOfficeChart(Composite parent) {
		officeChart = new HoursChart(parent, SWT.NONE);

		return officeChart;
	}

	protected Composite createChart(Composite parent) {
		chart = new HoursChart(parent, SWT.NONE);

		IAxis yAxis = chart.getYAxis();
		yAxis.getTitle().setText("Vertreter");

		return chart;
	}

	protected Composite createTable(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout());
		top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		sum = new SumHoursOverview(top);
		new Label(top, SWT.NONE);

		Tree t = new Tree(top, SWT.BORDER | SWT.FULL_SELECTION);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
		table = new HoursYearTreeTable(t);

		return top;
	}

	@Override
	public void createPartControl(Composite parent) {
		try {
			model = HoursViewYearModel.open();
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}

		super.createPartControl(parent);
		TabItem item = newTab("Geschäftstellen");
		item.setControl(createOfficeChart(folder));
		
		addExportToClipboardMenu(() -> {
			final int FIRMA = 500;
			final int TOTAL = 50;
			final int MONTH = 30;

			final TreeItem[] items = table.getTree().getItems();
			final HtmlTable htmlTable = new HtmlTable();

			HtmlRecord rec = htmlTable.addRecord().addValue("Vertreter", FIRMA).addValue("Total", TOTAL).setHeader(true);
			for (String s : Months.NAMES)
				rec.addValue(s, MONTH);

			for (int i = 0, max = items.length; i < max; i++) {
				HoursYearTableEntry id = (HoursYearTableEntry) items[i].getData();
				rec = htmlTable.addRecord().addValue(id.getName(), 500).addValue(Float.toString(id.getYearSum()),
						50);
				for (int m = 0; m < 12; m++)
					rec.addValue(Float.toString(id.getValue(m)), 30);
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
					HoursYearTableEntry id = (HoursYearTableEntry) items[i].getData();
					out.write(id.getName());
					out.write(';');
					out.printf(Locale.GERMAN, "%,.2f", id.getYearSum());
					out.write(';');
					for (int m = 0; m < 12; m++) {
						out.printf(Locale.GERMAN, "%,.2f", id.getValue(m));
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

	void refreshTable(Collection<HoursYearTableEntry> data, SumHoursOverviewRecord[] data2) {
		table.removeAll();
		table.setInput(data);
		table.expandAll();

		sum.add(data2);
	}

	@Override
	public void refresh() {

		if (node != null) {
			if(node.getType() == Disponent.Year) {
				int year = Integer.parseInt(node.getName());
				try {
					final Collection<HoursYearTableEntry> result = model.load(year);
					final SumHoursOverviewRecord[] result2 = model.getOverviewRecods(result);

					refreshTable(result, result2);
					refreshChart(result);
					refreshOfficeChart(result2);

				} catch (SQLException e) {
					UIError.showError("DB Fehler", e);
				}
			}
		}
	}

	private void refreshOfficeChart(SumHoursOverviewRecord[] result) {
		officeChart.removeSeries();

		for (SumHoursOverviewRecord e : result)
			officeChart.addSeries(e.getName() + ' ' + e.getSum(0), e.getMonthValues()).setDescription(e.getName());

		officeChart.getChart().getAxisSet().adjustRange();
		officeChart.setTitle(node.getName());
	}

	private void refreshChart(Collection<HoursYearTableEntry> result) {
		chart.removeSeries();

		for (HoursYearTableEntry e : result)
			chart.addSeries(e.getName() + ' ' + e.getYearSum(), e.getMonthValues());

		chart.getChart().getAxisSet().adjustRange();
		chart.setTitle(node.getName());
	}

	@Override
	public String getViewID() {
		return ID;
	}

}
