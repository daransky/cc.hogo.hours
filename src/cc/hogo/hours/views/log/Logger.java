package cc.hogo.hours.views.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.DB;
import cc.hogo.hours.views.log.LogEntry.Type;

public class Logger {

	private static Logger instance;

	private final PreparedStatement insert;
	private final PreparedStatement getImport;

	public static final int GENERAL_ERROR = 10;
	public static final int IMPORT_COMPLETED = 100;
	public static final int RECORD_UPDATED = 110;
	public static final int RECORD_ADDED = 120;
	public static final int IMPORT_DELETED = 130;

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

	Logger(PreparedStatement stmt, PreparedStatement lastImp) {
		insert = stmt;
		getImport = lastImp;
	}

	public static LogEntry newRecordUpdated(long record) {
		return new LogEntry(RECORD_UPDATED, Type.WARNING,
				String.format("Benutzer '%S' hat den Satz '%d' geändert.", System.getProperty("user.name"), record));
	}

	public static LogEntry newRecordAdded(long record) {
		return new LogEntry(RECORD_ADDED, Type.WARNING,
				String.format("Benutzer '%S' hat den Satz '%d' hinzugefügt.", System.getProperty("user.name"), record));
	}

	public static LogEntry newImportDeleted(String record) {
		return new LogEntry(IMPORT_DELETED, Type.WARNING,
				String.format("Benutzer '%S' hat den Import '%s' gelöscht.", System.getProperty("user.name"), record));
	}

	public static LogEntry newImportCompleted(int count, int year, int month, long first, long last) {
		final LocalDate date = LocalDate.of(year, month+1, 1);
		return new LogEntry(IMPORT_COMPLETED, Type.INFO,
				String.format("Es wurden '%d' Sätze für '%s' importiert '%d-%d'.", count, FORMATTER.format(date), first, last));
	}

	public static LogEntry newHistoryImportCompleted(int count, long first, long last) {
		return new LogEntry(IMPORT_COMPLETED, Type.INFO,
				String.format("Benutzer '%s' hat %d Historiesätze importiert '%d-%d'.", System.getProperty("user.name"), count, first, last));
	}

	public static LogEntry newGeneralError(String errMessage) {
		return new LogEntry(GENERAL_ERROR, Type.ERROR, errMessage);
	}

	public void write(LogEntry e) {
		try {
			insert.setInt(1, e.getMsgId());
			insert.setShort(2, (short) e.getType().value);
			insert.setString(3, e.getMessage());
			insert.executeUpdate();
			insert.getConnection().commit();
		} catch (SQLException e1) {
			UIError.showError("DB Fehler", e1);
		}
	}

	public static Logger instance() {
		if (instance == null) {
			try {
				Connection c = DB.instance().getConnection();
				instance = new Logger(c.prepareStatement("INSERT INTO LOG (logId, logType, logMsg) values(?,?,?)"),
						c.prepareStatement(String.format(
								"select id, logid, logdate, logtype, logmsg from LOG log where logid = %d and logmsg like ? limit 1",
								IMPORT_COMPLETED)));
			} catch (SQLException e) {
				UIError.showError("DB Fehler", e);
			}
		}
		return instance;
	}

	public LogEntry getImportEntry(int year, int month) {
		String date = String.format("%d/%d", year, month);
		try {
			getImport.setString(1, date);
			
			final ResultSet result = getImport.executeQuery();
			if (result.next()) {
				return new LogEntry(result.getInt("id"), 
						result.getInt("logid"), 
						result.getShort("logtype"),
						result.getTimestamp("logdate"), 
						result.getString("logmsg"));
			}
		} catch (SQLException e) {
			UIError.showError("DB Fehler", e);
		}
		return null;
	}
}
