package cc.hogo.hours.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import org.daro.common.ui.UIError;

public class Row2Hour implements Function<ResultSet, HourEntry> {

	@Override
	public HourEntry apply(ResultSet rs) {
		HourEntry e = new HourEntry();
		try {
			e.setId(rs.getLong("id"));
			e.setYear(rs.getInt("year"));
			e.setMonth(rs.getInt("month"));
			e.setGeschaeftStelle(rs.getInt("geschaeftStelle"));
			e.setKundenNummer(rs.getInt("kundenNummer"));
			e.setGeschaeftStelle1(rs.getInt("geschaeftStelle1"));
			e.setPersonalNummer(rs.getInt("personalNummer"));
			e.setFremdLohnNummer(rs.getInt("fremdLohnNummer"));
			e.setFakturStunden(rs.getFloat("fakturStunden"));
			e.setLohnStunden(rs.getFloat("lohnStunden"));
			e.setKurzBezeichnung(rs.getString("kurzBezeichnung"));
			e.setDisponentId(rs.getString("disponentId"));
			e.setNachname(rs.getString("nachname"));
			e.setVorname(rs.getString("vorname"));
			e.setKundenName(rs.getString("kundenName"));
			e.setInfo(rs.getString("info"));
			return e;
		} catch (SQLException e1) {
			UIError.showError("DB Fehler", e1);
		}
		return null;
	}

}
