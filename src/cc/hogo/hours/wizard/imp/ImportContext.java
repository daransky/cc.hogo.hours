package cc.hogo.hours.wizard.imp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.DB;
import cc.hogo.hours.db.EntityStructure;
import cc.hogo.hours.db.GenericDbModel;
import cc.hogo.hours.db.Hour2Statement;
import cc.hogo.hours.db.HourEntry;
import cc.hogo.hours.views.log.Logger;

public class ImportContext {

	int month, year;
	String path;
	List<HourEntry> hours = new LinkedList<>();
	CSVReader reader;

	public int load() throws IOException {
		hours.clear();
		if (path != null)
			try (CSVReader reader = CSVReader.open(path)) {
				HourEntry e = null;
				while ((e = reader.readNext()) != null) {
					hours.add(e);
				}
			}
		return hours.size();
	}
	

	long getLastRecordId(Connection c) {
		ResultSet rs;
		try {
			rs = c.createStatement().executeQuery("select id from hours order by id desc");
			if( rs.next() )
				return rs.getLong(1);
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}
		return -1;
	}
	

	public int importRecords() throws Exception {
		int count = 0;

		Connection c = DB.instance().getConnection();
		long first = getLastRecordId(c);

		EntityStructure<?> struct = EntityStructure.get(HourEntry.class);
		String fields = struct.getFields().stream().filter(n -> (n.equals("id") == false) ).collect(Collectors.joining(","));
		try(PreparedStatement insert = c.prepareStatement(String.format("insert into %s (%s) values(%s)", 
															struct.getTableName(),
															fields, 
															GenericDbModel.getStatementParameters(struct.getFieldCount()-1)))) {

			Hour2Statement h = new Hour2Statement(false);
			for( HourEntry e : hours ) { 
				e.setMonth(month);
				e.setYear(year);
								
				h.accept(e, insert);
				insert.addBatch();
				count++;
			}
			insert.executeBatch();
			
			long last = getLastRecordId(c);
			c.commit();
			Logger.instance().write(Logger.newImportCompleted(count, year, month, first, last));
			insert.close();
		} catch( Exception e )
		{
			c.rollback();
			throw e;
		}
		return count;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<HourEntry> getHours() {
		return hours;
	}
}
