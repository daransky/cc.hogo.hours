package cc.hogo.hours.views.hoursimport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.HourEntry;

public class TableRow2Hour implements Function<ResultSet, HourEntry> {

	
	@Override
	public HourEntry apply(ResultSet rs) {
		HourEntry e = new HourEntry();
		try {
			e.setId(rs.getLong("id"));
			e.setYear(rs.getInt("year"));
			e.setMonth(rs.getInt("month"));
			e.setFakturStunden(rs.getFloat("fakturStunden"));
			e.setDisponentId(rs.getString("disponentId"));
			e.setKundenName(rs.getString("kundenName"));
			e.setInfo(rs.getString("info"));
			return e;
		} catch (SQLException e1) {
			UIError.showError("DB Fehler", e1);
		}
		return null;
	}

}
