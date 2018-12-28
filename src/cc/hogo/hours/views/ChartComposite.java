package cc.hogo.hours.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesSet;
import org.swtchart.ISeries.SeriesType;

public class ChartComposite extends Composite {

	private final Chart	chart;
	
	public ChartComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
		setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		chart = new Chart(this, SWT.NONE);
	}

	public Chart getChart() {
		return chart;
	}
	
	public String getYAxisTitle() { 
		IAxis yAxis = chart.getAxisSet().getYAxis(0);
		return yAxis.getTitle().getText();
	}

	public String getXAxisTitle() { 
		IAxis xAxis = chart.getAxisSet().getXAxis(0);
		return xAxis.getTitle().getText();
	}

	
	public void removeSeries() { 
		ISeriesSet iset = chart.getSeriesSet();
		for(ISeries s : iset.getSeries())
			iset.deleteSeries(s.getId());
	}
	
	public ILineSeries createLineSeries(String title, double [] values) {
		return createLineSeries(title, values, null);
	}
	
	public ILineSeries createLineSeries(String title, double [] values, Color color) { 
		ISeriesSet iset = chart.getSeriesSet();
		
		ILineSeries series = (ILineSeries)iset.createSeries(SeriesType.LINE, title);
		if( values != null )
			series.setYSeries(values);
		
		if( color != null ) { 
			series.setLineColor(color);
			series.setSymbolColor(color);
		}
		return series;
	}

	public void setTitle(String name) {
		chart.getTitle().setText(name);		
	}

	public IAxis getXAxis() {
		return chart.getAxisSet().getXAxis(0);
	}
	public IAxis getYAxis() {
		return chart.getAxisSet().getYAxis(0);
	}

	public ISeriesSet getSeriesSet() {
		return chart.getSeriesSet();
	}
	
}
