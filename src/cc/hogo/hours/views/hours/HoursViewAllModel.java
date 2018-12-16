package cc.hogo.hours.views.hours;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import cc.hogo.hours.core.Months;
import cc.hogo.hours.db.DB;

public class HoursViewAllModel implements AutoCloseable {
	private PreparedStatement all;
	private PreparedStatement officeHours;

	public static HoursViewAllModel open() throws SQLException {
		HoursViewAllModel set = new HoursViewAllModel();

		Connection connection = DB.instance().getConnection();
		set.all = connection.prepareStatement("select year,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  AND month = 0 group by month) as Januar,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 1 group by month) as Februar,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 2 group by month) as März,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 3 group by month) as April,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 4 group by month) as Mai,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 5 group by month) as Juni,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 6 group by month) as Juli,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 7 group by month) as August,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 8 group by month) as September,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 9 group by month) as Oktober,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 10 group by month) as November,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.year  and month = 11 group by month) as Dezember\n"
				+ " from hours as h group by year order by year desc");
		set.officeHours = DB.instance().getConnection()
				.prepareStatement("select month, geschaeftstelle, kurzbezeichnung, sum(lohnstunden) as total\r\n"
						+ "from hours where year = ? "
						+ "group by month, geschaeftstelle, kurzbezeichnung order by \"month\"");
		return set;
	}

	HoursAllTableEntry addOffices(HoursAllTableEntry e, int year) throws SQLException {
		officeHours.setInt(1, year);

		SumBuilder sb = new SumBuilder(e);
		try (ResultSet rs = officeHours.executeQuery()) {
			while (rs.next()) {
				int month = rs.getInt("month");
				int officeId = rs.getInt("geschaeftstelle");
				String name = rs.getString("kurzbezeichnung");
				float sum = rs.getFloat("total");
				sb.addOfficeSum(officeId, name, month, sum);
			}
			return sb.build();
		}
	}

	public Collection<HoursAllTableEntry> load() throws SQLException {

		LinkedList<HoursAllTableEntry> result = new LinkedList<>();

		try (final ResultSet rs = all.executeQuery()) {
			while (rs.next()) {
				int year = rs.getInt(1);
				HoursAllTableEntry e = new HoursAllTableEntry(year);

				float allValue = 0f;

				for (int i = 1, max = Months.NAMES.length + 1; i < max; i++) {
					float tmp = rs.getFloat(i + 1);
					allValue += tmp;
					e.setHours(i, tmp);
				}
				e.setHours(0, allValue);

				result.add(addOffices(e, year));
			}
		}

		return result;
	}

	@Override
	public void close() throws Exception {
		all.close();
		officeHours.close();
	}
}
