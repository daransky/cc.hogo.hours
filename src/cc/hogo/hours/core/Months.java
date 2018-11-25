package cc.hogo.hours.core;

public class Months {
	public static final String NAMES[] = new String[] { 
		"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"
	};
	
	public static final int name2Index(String name ) { 
		switch( name ) {
		
		case "Januar" : return 0;
		case "Februar": return 1;
		case "März"	: return 2;
		case "April" : return 3;
		case "Mai" : return 4;
		case "Juni": return 5;
		case "Juli" : return 6;
		case "August" : return 7;
		case "September": return 8;
		case "Oktober" : return 9;
		case "November" : return 10;
		case "Dezember" : return 11;
		}
		
		return -1;
	}
	
}
