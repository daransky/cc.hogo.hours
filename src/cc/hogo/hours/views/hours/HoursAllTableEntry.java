	package cc.hogo.hours.views.hours;

import cc.hogo.hours.views.HoursEntry;

class HoursAllTableEntry implements HoursEntry {
	
	int 	yaer;
	float 	sum[] = new float[13];
	HoursOfficeEntry	office[];
	
	public HoursAllTableEntry(int year) {
		this.yaer  = year;
	}

	public float getTotal() {
		return sum[0];
	}
	

	public float getHours(int month) {
		return this.sum[month];
	}

	public void setHours(int month, float value) {
		this.sum[month] = value;
	}

	public String getName() {
		return Integer.toString(yaer);
	}

	public float[] getValues() {
		return sum;
	}
	
	public double[] getMonthValues() { 
		double[] tmp = new double[12];
		for(int i = 1; i < 13; i++)
			tmp[i-1] = sum[i];
		return tmp;
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
