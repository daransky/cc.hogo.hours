package cc.hogo.hours.views.hours.year;

import java.sql.SQLException;
import java.util.Collection;

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
import org.swtchart.IAxis;

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
