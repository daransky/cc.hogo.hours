package cc.hogo.hours.views.hours;

import java.util.HashMap;

public class SumBuilder {

	private final HoursAllTableEntry	entry;
	HashMap<Integer, HoursOfficeEntry>	officeMap = new HashMap<>();

	public SumBuilder(HoursAllTableEntry e) {
		entry = e;
	}
	
	public SumBuilder	addOfficeSum(int id, String name, int month, float value ) { 
		HoursOfficeEntry o = officeMap.get(id);
		if( o == null ) { 
			o = new HoursOfficeEntry(entry, id, name);
			officeMap.put(id, o);
		}
		o.sum[0] += value;
		o.sum[month+1] = value;
		return this;
	}
	
	public HoursAllTableEntry build() { 
		if( !officeMap.isEmpty() ) { 
			HoursOfficeEntry[] sum = new HoursOfficeEntry[officeMap.size()];
			officeMap.values().toArray(sum);
			entry.office = sum;
		} else
			entry.office = new HoursOfficeEntry[0];
		return entry;
	}
}
