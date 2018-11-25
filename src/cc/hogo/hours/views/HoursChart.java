package cc.hogo.hours.views;

import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ISeriesSet;
import org.swtchart.LineStyle;
import org.swtchart.ISeries.SeriesType;

import cc.hogo.hours.core.Months;

public class HoursChart extends ChartComposite {

	void fillMonths() {
		IAxis xAxis = getXAxis();
		xAxis.enableCategory(true);
		xAxis.setCategorySeries(Months.NAMES);
		xAxis.getTitle().setText("Monat");
		xAxis.adjustRange();
	}

	public HoursChart(Composite parent, int swt) {
		super(parent, swt);

		getYAxis().getTitle().setText("Stunden");
		fillMonths();
	}

	public ILineSeries addSeries(String title, double[] values, Color color, LineStyle style) {
		ISeriesSet iset = getSeriesSet();
		ILineSeries series = (ILineSeries) iset.createSeries(SeriesType.LINE, title);
		series.setYSeries(values);
		series.setLineColor(color);
		series.setSymbolColor(color);
		series.setLineStyle(style);
		return series;
	}
	
	public ILineSeries addSeries(String title, double[] values) { 
		Color color = Display.getCurrent().getSystemColor(ThreadLocalRandom.current().nextInt(2, 16));
		return addSeries(title, values, color, LineStyle.SOLID);
	}
}
