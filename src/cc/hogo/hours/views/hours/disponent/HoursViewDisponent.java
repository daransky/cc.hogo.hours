package cc.hogo.hours.views.hours.disponent;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

import org.daro.common.ui.TreeNodeData;
import org.daro.common.ui.UIError;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.swtchart.LineStyle;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.core.export.HtmlTable;
import cc.hogo.hours.core.export.HtmlTable.HtmlRecord;
import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.views.HoursAbstractView;
import cc.hogo.hours.views.HoursChart;
import cc.hogo.hours.views.hours.SumHoursOverview;

public class HoursViewDisponent extends HoursAbstractView {

	public static final String ID = "cc.hogo.hours.views.hours.HoursViewDisponent"; //$NON-NLS-1$
	private HoursDisponentTable table;
	private HoursChart chart;
	private TreeNodeData node;
	private HoursViewDisponentModel model;
	private SumHoursOverview sum;
	private String ori;
	private int year;

	@Override
	public void setInput(Object node) {
		this.node = (TreeNodeData) node;
		refresh();
	}

	@Override
	protected Composite createChart(Composite parent) {
		chart = new HoursChart(parent, SWT.NONE);
		return chart;
	}

	@Override
	public Composite createTable(Composite parent) {
		ori = getTitle();

		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout());
		top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		sum = new SumHoursOverview(top);
		new Label(top, SWT.NONE);

		Tree t = new Tree(top, SWT.BORDER | SWT.FULL_SELECTION);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));

		addExpandCollapseMenu(() -> {
			TreeItem[] items = table.getTree().getItems();
			Arrays.asList(items).forEach(item -> table.expandToLevel(item.getData(), TreeViewer.ALL_LEVELS));
		}, () -> {
			TreeItem[] items = table.getTree().getItems();
			Arrays.asList(items).forEach(item -> table.collapseToLevel(item.getData(), TreeViewer.ALL_LEVELS));
		});
		
		try {
			model = HoursViewDisponentModel.open();
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}

		table = new HoursDisponentTable(t, model);
		refresh();
		return top;
	}

	@Override
	public void setFocus() { //
	}

	@Override
	public void createPartControl(Composite parent) {
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
				HoursDisponentTableEntry id = (HoursDisponentTableEntry) items[i].getData();
				rec = htmlTable.addRecord().addValue(id.getKundenname(), 500).addValue(Float.toString(id.getTotal()),
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
					HoursDisponentTableEntry id = (HoursDisponentTableEntry) items[i].getData();
					out.write(id.getKundenname());
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
	}

	public void refresh() {
		setContentDescription(ori);

		if (node != null) {
			year = Year.now().getValue();

			TreeNodeData parent = node.getParent();
			if (parent.getType() == Disponent.Year) {
				year = Integer.parseInt(parent.getName());
			}

			Disponent disponent = (Disponent) node.getData();
			try {
				Collection<HoursDisponentTableEntry> lastResult = model.load(disponent.getSid(), year);

				float[] sumRecord = model.getSum(disponent.getSid(), year);
				float[] sumPrev = model.getSum(disponent.getSid(), year - 1);
				refreshData(lastResult, sumRecord);
				refreshChart(year, sumRecord, sumPrev);
				setContentDescription(ori + " " + disponent.getName());
			} catch (SQLException e) {
				UIError.showError("DB Fehler", e);
			}
		}
	}

	void refreshData(Collection<HoursDisponentTableEntry> result, float[] sumRecord) {
		table.getTree().removeAll();

		if (result != null)
			table.setInput(result);

		sum.setRecord(sumRecord);
	}

	void refreshChart(int year, float[] sum1, float[] sum2) {
		if (chart == null)
			return;

		chart.removeSeries();

		int yearNow = year;
		int yearPrv = yearNow - 1;

		Function<float[], double[]> rec2chart = (values) -> {
			double[] tmp = new double[12];
			for (int i = 0; i < 12; i++)
				tmp[i] = values[i];
			return tmp;
		};

		chart.addSeries(Integer.toString(yearNow) + " - " + sum1[0], rec2chart.apply(sum1),
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), LineStyle.SOLID);
		chart.addSeries(Integer.toString(yearPrv) + " - " + sum2[0], rec2chart.apply(sum2),
				Display.getCurrent().getSystemColor(SWT.COLOR_RED), LineStyle.DASHDOT);

		chart.getChart().getAxisSet().adjustRange();
		chart.setTitle(node.getName());
		chart.getChart().redraw();
	}

	@Override
	public String getViewID() {
		return ID;
	}

}
