package cc.hogo.hours.views;

import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.LineStyle;

import cc.hogo.hours.core.Months;

public class HoursChart extends ChartComposite {

	static class Line {
		final Color color;
		final LineStyle style;
		
		Line(Color color, LineStyle style) { 
			this.color = color;
			this.style = style;
		}
	}

	private LinkedList<Line> LINES = new LinkedList<>();

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
		if (LINES.isEmpty()) {
			LineStyle[] styles = LineStyle.values();
			for (LineStyle style : styles) {
				if (style == LineStyle.NONE)
					continue;
				for (int i = 2; i < 16; i++) {
					LINES.push(new Line(Display.getCurrent().getSystemColor(i), style));
				}
			}
		}
		
		Line line = LINES.pop();
		return addSeries(title, values, line.color, line.style);
	}
}
