package cc.hogo.hours.views.hours.disponent;

public class HoursDisponentSumEntry {

	int 	year;
	float 	sum;
	float 	total[] = new float[12];
	
	public HoursDisponentSumEntry(int year, float sum, float[] total) {
		super();
		this.year = year;
		this.sum = sum;
		this.total = total;
	}
	public int getYear() {
		return year;
	}
	public float getSum() {
		return sum;
	}
	public float[] getTotal() {
		return total;
	}
	
	
}
