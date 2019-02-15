package cc.hogo.hours.views.hours.year;

import java.util.HashMap;

public class SumBuilder {

	private final HoursYearTableEntry	entry;
	HashMap<Integer, OfficeYearEntry>	officeMap = new HashMap<>();

	public SumBuilder(HoursYearTableEntry e) {
		entry = e;
	}
	
	public SumBuilder	addOfficeSum(int id, String name, int month, float value ) { 
		OfficeYearEntry o = officeMap.get(id);
		if( o == null ) { 
			o = new OfficeYearEntry(entry, id, name);
			officeMap.put(id, o);
		}
		o.sum[0] += value;
		o.sum[month+1] = value;
		return this;
	}
	
	public HoursYearTableEntry build() { 
		if( !officeMap.isEmpty() ) { 
			OfficeYearEntry[] sum = new OfficeYearEntry[officeMap.size()];
			officeMap.values().toArray(sum);
			entry.office = sum;
		} else
			entry.office = new OfficeYearEntry[0];
		return entry;
	}
}
