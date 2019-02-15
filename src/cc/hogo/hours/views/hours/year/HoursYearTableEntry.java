package cc.hogo.hours.views.hours.year;

import cc.hogo.hours.views.HoursEntry;

class HoursYearTableEntry implements HoursEntry {
	
	String 		sid;
	String 		name;
	float[] 	sum = new float[13];
	OfficeYearEntry[] 	office;


	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return (name != null && !name.isEmpty()) ? name : sid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getValues() {
		return sum;
	}

	public float getYearSum() {
		return sum[0];
	}

	public OfficeYearEntry[] getOfficeEntries() {
		return office;
	}

	public void setMonthTotal(int i, float value) {
		sum[i] = value;		
	}

	public float getValue(int i) {
		return sum[i];
	}
	
	public double[] getMonthValues() {
		double[] tmp = new double[12];
		for( int i = 1; i < 13; i++)
			tmp[i-1] = sum[i];
		return tmp;
	}

	public float[] getSum() {
		return sum;
	}
}
