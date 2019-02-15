package cc.hogo.hours.views.hours.year;

import cc.hogo.hours.views.HoursEntry;

public class OfficeYearEntry implements HoursEntry {
	final int id;
	float sum[] = new float[13];
	final String name;
	final HoursYearTableEntry parent;
	
	public OfficeYearEntry(HoursYearTableEntry parent, int id, String name ) {
		this.parent = parent;
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public float[] getSum() {
		return sum;
	}

	public float getYearSum() {
		return sum[0];
	}

	public void setSum(int month, float sum) {
		this.sum[month] = sum;
	}
	
	public float getValue(int i ) {
		return sum[i];
	}
	
}