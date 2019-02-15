package cc.hogo.hours.views;

public interface HoursEntry {

	String	getName();
	
	float getValue(int month);
	
	float getYearSum();
}
