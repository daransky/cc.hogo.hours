	package cc.hogo.hours.views.hours;

public class HoursAllTableEntry {

	public static class Office {
		final int id;
		float sum[] = new float[13];
		final String name;
		final HoursAllTableEntry parent;
		
		public Office(HoursAllTableEntry parent, int id, String name ) {
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
	}

	
	int 	yaer;
	float 	sum[] = new float[13];
	Office	office[];
	
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
}
