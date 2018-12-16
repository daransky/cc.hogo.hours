package cc.hogo.hours.views.disponent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
			
			insert = c.prepareStatement("insert into disponent (sid, firma, name) values(?,?,?)");
			
			this.row2e = result -> {
				try {
					return new Disponent(result.getString("disponentId"), result.getString("name"));
				} catch (SQLException e1) {
					UIError.showError("DB Fehler", e1);
				}
				return null;
			};

		} catch (SQLException e) {
			if (all != null)
				all.close();
			if (selectYears != null)
				selectYears.close();
			UIError.showError("DB Fehler", e);
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
	
	@Override
	public List<Disponent> select() throws SQLException {
		List<Disponent> list = new LinkedList<>();
		try(Statement any = all.getConnection().createStatement()) { 
			try(ResultSet rs = any.executeQuery("select sid, firma, name from disponent")) {
				while(rs.next()) { 
					list.add(new Disponent(rs.getString("sid"), rs.getString("firma"), rs.getString("name")));
				}
			}
		}
		return list;
	}
	
	@Override
	public void add(Disponent e) throws SQLException {
		insert.setString(1, e.getSid());
		insert.setString(2, e.getFirma());
		insert.setString(3, e.getName());
		insert.executeUpdate();
	}
}
