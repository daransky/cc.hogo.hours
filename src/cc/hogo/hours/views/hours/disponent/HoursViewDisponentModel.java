package cc.hogo.hours.views.hours.disponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import cc.hogo.hours.db.DB;
import cc.hogo.hours.views.hours.disponent.HoursDisponentTableEntry.SubEntry;

public class HoursViewDisponentModel implements AutoCloseable {
	private PreparedStatement monthHours;
	private PreparedStatement sumHours;
	private PreparedStatement children;
	private String disponentId;

	public static HoursViewDisponentModel open() throws SQLException {
		HoursViewDisponentModel set = new HoursViewDisponentModel();

		Connection connection = DB.instance().getConnection();
		set.monthHours = connection.prepareStatement(
				"select kundennummer, kundenname, sum(lohnstunden) as lohnstunden, kurzbezeichnung from hours where disponentid = ? and month = ? and year = ? group by kundenname, kundennummer, kurzbezeichnung;");
		set.sumHours = connection.prepareStatement("select year, disponentid,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  AND \"month\" = 0 and disponentid=h.disponentid group by \"month\") as Januar,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 1 and disponentid=h.disponentid group by month) as Februar,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 2 and disponentid=h.disponentid group by month) as März,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 3 and disponentid=h.disponentid group by month) as April,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 4 and disponentid=h.disponentid group by month) as Mai,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 5 and disponentid=h.disponentid group by month) as Juni,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 6 and disponentid=h.disponentid group by month) as Juli,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 7 and disponentid=h.disponentid group by month) as August,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 8 and disponentid=h.disponentid group by month) as September,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 9 and disponentid=h.disponentid group by month) as Oktober,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 10 and disponentid=h.disponentid group by month) as November,\n"
				+ " (select sum(lohnstunden) from hours where \"year\" = h.\"year\"  and \"month\" = 11 and disponentid=h.disponentid group by month) as Dezember\n"
				+ " from hours as h where year = ? and disponentid = ? group by \"year\", disponentid order by \"year\" desc");
		set.children = connection.prepareStatement(
				"select lohnstunden, month from hours where disponentid = ? and kundennummer = ? and year = ?");
		return set;
	}

	public Collection<HoursDisponentTableEntry> load(String disponentId, int year) throws SQLException {
		this.disponentId = disponentId;
		Map<Integer, HoursDisponentTableEntry> set = new LinkedHashMap<>();

		for (int month = 0; month < 12; month++) {
			monthHours.setString(1, disponentId);
			monthHours.setInt(2, month);
			monthHours.setInt(3, year);

			try (ResultSet rs = monthHours.executeQuery()) {
				while (rs.next()) {
					HoursDisponentTableEntry e = set.get(rs.getInt("kundennummer"));
					if (e == null) {
						e = new HoursDisponentTableEntry(rs.getInt("kundennummer"), rs.getString("kundenname"),
								rs.getString("kurzbezeichnung"));
						set.put(e.kundennummer, e);
					}
					e.setHours(month, rs.getFloat("lohnstunden"));
				}
			}
		}
		return set.values();
	}

	public float[] getSum(String disponentId, int year) throws SQLException {
		sumHours.setInt(1, year);
		sumHours.setString(2, disponentId);

		try (ResultSet rs = sumHours.executeQuery()) {
			float[] sum = new float[13];
			if (rs.next())
				for (int i = 0; i < 12; i++)
					sum[0] += sum[i] = rs.getFloat(i + 3);
			return sum;
		}
	}

	@Override
	public void close() throws Exception {
		monthHours.close();
	}

	void addToSubTable(Collection<SubEntry> table, int month, float value) { 
		for(SubEntry e : table) { 
			if( e.value[month] == 0) {
				e.value[month] = value;
				return;
			}
		}
		SubEntry newe = new SubEntry(null);
		newe.value[month] = value;
		table.add(newe);
	}
	
	public void loadSubEntries(HoursDisponentTableEntry entry, int year) throws SQLException {
		children.setString(1, disponentId);
		children.setInt(2, entry.getKundennummer());
		children.setInt(3, year);
		ArrayList<SubEntry> entries = new ArrayList<>();
		
		try (ResultSet rs = children.executeQuery()) {
			while (rs.next()) {
				addToSubTable(entries, rs.getInt(2), rs.getFloat(1));
			}
		}

		entry.childs = new SubEntry[entries.size()];
		entries.toArray(entry.childs);
	}
}
