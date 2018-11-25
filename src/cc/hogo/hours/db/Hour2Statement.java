package cc.hogo.hours.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.BiConsumer;

import org.daro.common.ui.UIError;

public class Hour2Statement implements BiConsumer<HourEntry, PreparedStatement> {

	private final boolean update;
	public Hour2Statement(boolean update) {
		this.update = update;
	}
	
	@Override
	public void accept(HourEntry e, PreparedStatement s) {
		try {
			s.setInt(1, e.getYear());
			s.setInt(2, e.getMonth());
			s.setInt(3, e.getGeschaeftStelle());
			s.setInt(4, e.getKundenNummer());
			s.setInt(5, e.getGeschaeftStelle1());
			s.setInt(6, e.getPersonalNummer());
			s.setInt(7, e.getFremdLohnNummer());
			s.setFloat(8, e.getFakturStunden());
			s.setFloat(9, e.getLohnStunden());
			s.setString(10, e.getKurzBezeichnung());
			s.setString(11, e.getDisponentId());
			s.setString(12, e.getNachname());
			s.setString(13, e.getVorname());
			s.setString(14, e.getKundenName());
			s.setString(15, e.getInfo());
			if( update )
			s.setLong(16, e.getId());
		} catch (SQLException e1) {
			UIError.showError("DB Fehler", e1);
		}
	}

}
