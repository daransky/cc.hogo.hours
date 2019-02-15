package cc.hogo.hours.views.hours;

import cc.hogo.hours.views.HoursEntry;

class HoursOfficeEntry implements HoursEntry {
	final int id;
	float sum[] = new float[13];
	final String name;
	final HoursAllTableEntry parent;
	
	public HoursOfficeEntry(HoursAllTableEntry parent, int id, String name ) {
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

	public void setSum(int month, float sum) {
		this.sum[month] = sum;
	}

	@Override
	public float getValue(int month) {
		return sum[month];
	}

	@Override
	public float getYearSum() {
		return sum[0];
	}
}
