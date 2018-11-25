package cc.hogo.hours.views.hoursimport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.DB;
import cc.hogo.hours.db.GenericDbModel;
import cc.hogo.hours.db.Hour2Statement;
import cc.hogo.hours.db.HourEntry;
import cc.hogo.hours.db.Row2Hour;
import cc.hogo.hours.views.log.Logger;

public class HoursImportModel extends GenericDbModel<HourEntry> {
	PreparedStatement select;

	HashMap<String, String> disp = new HashMap<>();

	HoursImportModel() throws SQLException {
		super(HourEntry.class, new Row2Hour(), new Hour2Statement(false));
	}

	HoursImportModel(PreparedStatement all, PreparedStatement insert, PreparedStatement update,
			PreparedStatement delete, PreparedStatement updateRead, Function<ResultSet, HourEntry> row2entity,
			BiConsumer<HourEntry, PreparedStatement> entity2row) throws SQLException {
		super(all, insert, update, delete, updateRead, row2entity, entity2row);
		insert = insert.getConnection().prepareStatement(insert.toString(), Statement.RETURN_GENERATED_KEYS);
	}

	public static HoursImportModel open() throws SQLException {
		HoursImportModel m = new HoursImportModel();

		Connection c = DB.instance().getConnection();

		ResultSet rs = c.createStatement().executeQuery("select sid, name from disponent");
		while (rs.next()) {
			String key = rs.getString("sid");
			String val = rs.getString("name");
			m.disp.put(key, val);
		}

		m.select = c.prepareStatement(
				"select id,disponentid,fakturStunden,info,kundenname,month,year from hours where month=? and year=? order by id");

		return m;
	}

	public List<HourEntry> read(int month, int year) throws SQLException {
		select.setInt(1, month);
		select.setInt(2, year);
		TableRow2Hour t2r = new TableRow2Hour();

		List<HourEntry> list = new LinkedList<>();
		try (ResultSet rs = select.executeQuery()) {
			while (rs.next()) {
				HourEntry e = t2r.apply(rs);
				list.add(e);
			}
		}
		return list;
	}

	public String getDisponentName(String dispId) {
		String value = disp.get(dispId);
		return value != null ? value : dispId;
	}

	@Override
	public void update(HourEntry e) {
		try {
			super.update(e, new Hour2Statement(true));
			Logger.instance().write(Logger.newRecordUpdated(e.getId()));
		} catch (Exception err) {
			UIError.showError("DB Fehler", err);
		}
	}

	@Override
	public void add(HourEntry e) {
		try {
			add(e);
			try (ResultSet rs = insert.getGeneratedKeys()) {
				if (rs.next()) {
					long id = rs.getLong(1);
					Logger.instance().write(Logger.newRecordAdded(id));
				}
			}
		} catch (Exception err) {
			UIError.showError("DB Fehler", err);
		}
	}
}
