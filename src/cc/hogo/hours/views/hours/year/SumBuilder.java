package cc.hogo.hours.views.hours.year;

import java.util.HashMap;

import cc.hogo.hours.views.hours.year.HoursYearTableEntry.Office;

public class SumBuilder {

	private final HoursYearTableEntry	entry;
	HashMap<Integer, Office>	officeMap = new HashMap<>();

	public SumBuilder(HoursYearTableEntry e) {
		entry = e;
	}
	
	public SumBuilder	addOfficeSum(int id, String name, int month, float value ) { 
		Office o = officeMap.get(id);
		if( o == null ) { 
			o = new Office(entry, id, name);
			officeMap.put(id, o);
		}
		o.sum[0] += value;
		o.sum[month+1] = value;
		return this;
	}
	
	public HoursYearTableEntry build() { 
		if( !officeMap.isEmpty() ) { 
			Office[] sum = new Office[officeMap.size()];
			officeMap.values().toArray(sum);
			entry.office = sum;
		} else
			entry.office = new Office[0];
		return entry;
	}
}
