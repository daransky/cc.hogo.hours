package cc.hogo.hours.views.disponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.DB;
import cc.hogo.hours.db.Disponent;
import cc.hogo.hours.db.GenericDbModel;

public class DisponentModel extends GenericDbModel<Disponent> {

	PreparedStatement selectYears;

	public DisponentModel() throws SQLException {

		Connection c = DB.instance().getConnection();

		try {
			selectYears = c.prepareStatement("select year from hours group by year order by year desc");

			all = c.prepareStatement(
					"select disponentid, (select name from disponent where sid = hours.disponentid) as Name from hours where \"year\" = ? group by disponentid, \"year\" order by disponentid");

			this.row2e = (result) -> {
				try {
					return new Disponent(result.getString("disponentId"), result.getString("name"));
				} catch (SQLException e1) {
					UIError.showError("DB Fehler", e1);
				}
				return null;
			};

//			this.e2row = (e, statement) -> {
//				try {
//					statement.setString(1, e.getSid());
//					statement.setString(2, e.getName());
//				} catch (Exception ee) {
//					UIError.showError("DB Fehler", ee);
//				}
//			};

		} catch (SQLException e) {
			if (all != null)
				all.close();
			if (selectYears != null)
				selectYears.close();
		}
	}

	public Collection<Integer> getYears() throws SQLException {
		Collection<Integer> result = new LinkedList<>();
		try (ResultSet rs = selectYears.executeQuery()) {
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
		}
		return result;
	}
	
	public Iterator<Disponent> select(int year) throws SQLException { 
		all.setInt(1, year);
		final ResultSet rs = all.executeQuery();
		return new GenericDBIterator<>(rs, this.row2e);
	}
}
