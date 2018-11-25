package cc.hogo.hours.views.hours.year;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.db.DB;
import cc.hogo.hours.views.hours.year.HoursYearTableEntry.Office;

public class HoursViewYearModel implements AutoCloseable {
	private PreparedStatement yearHours;
	private PreparedStatement officeHours;

	
	public static HoursViewYearModel open() throws SQLException {
		HoursViewYearModel set = new HoursViewYearModel();
		
		StringBuilder joiner = new StringBuilder("select sid, name, \n");
		class Counter { 
			private int i = 0;
			public int next() { return i++; }
		}
		
		final Counter c = new Counter();
		joiner.append(Arrays.asList(Months.NAMES).stream().map( mname -> (String)String.format(" (select sum(lohnstunden) from hours where disponentid = sid and \"month\" = %d and \"year\" = ?) as %s", c.next(), mname)  ).collect(Collectors.joining(",\n")));
		joiner.append("\n from disponent");

		set.yearHours = DB.instance().getConnection().prepareStatement(joiner.toString());
		set.officeHours = DB.instance().getConnection().prepareStatement("select month, geschaeftstelle, kurzbezeichnung, sum(lohnstunden) as total\r\n" + 
				"from hours where \"year\" = ? and disponentid = ? " + 
				"group by month, geschaeftstelle, kurzbezeichnung order by \"month\"");
		return set;
	}

	public SumHoursOverviewRecord[] getOverviewRecods(Collection<HoursYearTableEntry> data) throws SQLException { 
		
		final Map<String, SumHoursOverviewRecord> map = new LinkedHashMap<>();
		SumHoursOverviewRecord hogo = new SumHoursOverviewRecord("Hogo");
		map.put(hogo.getName(), hogo);
		
		for( HoursYearTableEntry e : data ) { 
			hogo.add(e.getValues());
			if( e.getOfficeEntries() != null ) { 
				for( Office o : e.getOfficeEntries()) {
					SumHoursOverviewRecord office = map.get(o.getName());
					if( office == null )
					{
						office = new SumHoursOverviewRecord(o.getName());
						map.put(o.getName(), office);
					}
					office.add(o.getSum());
				}
			}
		}
		
		SumHoursOverviewRecord[] result = new SumHoursOverviewRecord[map.size()];
		return map.values().toArray(result);
	}
	
	HoursYearTableEntry addOffices(HoursYearTableEntry e, int year, String disponentId) throws SQLException { 
		officeHours.setInt(1, year);
		officeHours.setString(2, disponentId);
		
		SumBuilder sb = new SumBuilder(e);
		ResultSet rs = officeHours.executeQuery(); 
		while(rs.next()) { 
			int month 	= rs.getInt("month");
			int officeId= rs.getInt("geschaeftstelle");
			String name = rs.getString("kurzbezeichnung");
			float sum 	= rs.getFloat("total");
			sb.addOfficeSum(officeId, name, month, sum);
		}
		return sb.build();
	}
	
	
	public Collection<HoursYearTableEntry> load(int year) throws SQLException {

		LinkedList<HoursYearTableEntry> result = new LinkedList<>();

		for (int i = 1; i < 12+1; i++) {
			yearHours.setInt(i, year);
		}

		final ResultSet rs = yearHours.executeQuery();
		while (rs.next()) {
			HoursYearTableEntry e = new HoursYearTableEntry();
			e.setSid(rs.getString(1));
			e.setName(rs.getString(2));
			
			float yearValue = 0f;
			
			for (int i = 1, max = Months.NAMES.length+1; i < max; i++) {
				float tmp = rs.getFloat(i+2);
				yearValue += tmp;
				e.setMonthTotal(i, tmp);
			}
			e.setMonthTotal(0, yearValue);
			
			result.add(addOffices(e, year, e.getSid()));
		}
		rs.close();
		
		return result;
	}


	@Override
	public void close() throws Exception {
		yearHours.close();
	}
}
