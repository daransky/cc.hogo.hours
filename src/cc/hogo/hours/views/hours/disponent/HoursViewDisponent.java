package cc.hogo.hours.views.hours.disponent;

import java.sql.SQLException;
import java.time.Year;
import java.util.Collection;
import java.util.function.Function;

import org.daro.common.ui.TreeNodeData;
import org.daro.common.ui.UIError;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.swtchart.LineStyle;

import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.views.HoursAbstractView;
import cc.hogo.hours.views.HoursChart;
import cc.hogo.hours.views.hours.SumHoursOverview;

public class HoursViewDisponent extends HoursAbstractView {

	public static final String ID = "cc.hogo.hours.views.hours.HoursViewDisponent"; //$NON-NLS-1$
	private HoursDisponentTable 	table;
	private HoursChart				chart;
	private TreeNodeData 			node;
	private HoursViewDisponentModel model;
	private	SumHoursOverview 		sum;
	private String 					ori;
	
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
		top.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE) );
		
		sum = new SumHoursOverview(top);
		new Label(top, SWT.NONE);
		
		Table t = new Table(top, SWT.BORDER | SWT.FULL_SELECTION);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
		table = new HoursDisponentTable(t);

		try {
			model = HoursViewDisponentModel.open();
			refresh();
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}
		return top;
	}

	@Override
	public void setFocus() { //
	}

	public void refresh() {
		setContentDescription(ori);
		
		if (node != null) {
			int year = Year.now().getValue();
			
			TreeNodeData parent = node.getParent();
			if (parent.getType() == Disponent.Year) {
				year = Integer.parseInt(parent.getName());
			}
			
			Disponent disponent = (Disponent) node.getData();
			try {
				Collection<HoursDisponentTableEntry> lastResult = model.load(disponent.getSid(), year);
				
				float[] sumRecord = model.getSum(disponent.getSid(), year);
				float[] sumPrev = model.getSum(disponent.getSid(), year-1);
				refreshData(lastResult, sumRecord);
				refreshChart(year, sumRecord, sumPrev);
				
				setContentDescription(ori + " " + disponent.getName());
			} catch (SQLException e) {
				UIError.showError("DB Fehler", e);
			}
		}
	}
	
	void refreshData(Collection<HoursDisponentTableEntry> result, float[] sumRecord) {
		table.getTable().removeAll();
		
		if( result != null )
			table.add(result.toArray());
		
		sum.setRecord(sumRecord);
	}

	void refreshChart(int year, float[] sum1, float[] sum2) {
		if( chart == null )
			return;
		
		chart.removeSeries();
		
		int yearNow = year;
		int yearPrv = yearNow -1;
		
		Function<float[], double[]> rec2chart = (values) -> {
			double[] tmp = new double[12];
			for(int i = 0; i < 12; i++)
				tmp[i] = values[i];
			return tmp;
		};
		
		chart.addSeries(Integer.toString(yearNow) + " - " + sum1[0], rec2chart.apply(sum1), Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), LineStyle.SOLID);
		chart.addSeries(Integer.toString(yearPrv) + " - " + sum2[0], rec2chart.apply(sum2), Display.getCurrent().getSystemColor(SWT.COLOR_RED), LineStyle.DASHDOT);

		chart.getChart().getAxisSet().adjustRange();
		chart.setTitle(node.getName());
		chart.getChart().redraw();
	}

	@Override
	public String getViewID() {
		return ID;
	}

}
