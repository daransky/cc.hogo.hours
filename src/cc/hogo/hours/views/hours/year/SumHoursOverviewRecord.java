package cc.hogo.hours.views.hours.year;

import java.util.Locale;

public class SumHoursOverviewRecord {

	final String name;
	final float[] sum;

	public SumHoursOverviewRecord() {
		name = "";
		sum = new float[13];
	}

	public SumHoursOverviewRecord(String name) {
		this.name = name;
		sum = new float[13];
	}

	public String getName() {
		return name;
	}

	public void setSum(int idx, float value) {
		sum[idx] = value;
	}

	public float getSum(int idx) {
		return sum[idx];
	}

	public String getSumAsString(int idx) {
		return String.format(Locale.GERMAN, "%,.2f", sum[idx]);
	}

	public void add(float[] values) {
		for( int i = 0; i < 13; i++)
			sum[i] += values[i];
	}

	public double[] getMonthValues() {
		double [] tmp = new double[12];
		for( int i = 1; i < 13; i++) {
			tmp[i-1] = sum[i];
		}
		return tmp;
	}

}
