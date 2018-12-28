package cc.hogo.hours.views.hours;

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
