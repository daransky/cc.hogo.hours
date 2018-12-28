package cc.hogo.hours.views.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.daro.common.ui.UIError;

import cc.hogo.hours.db.GenericDbModel;

public class LogModel extends GenericDbModel<LogEntry> {
	
	
	protected LogModel(Function<ResultSet, LogEntry> row2entity, BiConsumer<LogEntry, PreparedStatement> entity2row)
			throws SQLException {
		super(LogEntry.class, row2entity, entity2row);
		Connection c = all.getConnection();
		
		all = c.prepareStatement("select id, logId, logDate, logType, logMsg from LOG log order by logdate desc");
	}

	public static LogModel open() throws Exception {
				
		Function<ResultSet, LogEntry> row2e = (result) -> {
			try {
				return new LogEntry(result.getInt("id"), 
									result.getInt("logid"), 
									result.getShort("logtype"), 
									result.getTimestamp("logdate"), 
									result.getString("logmsg"));
			} catch (SQLException e1) {
				UIError.showError("DB Fehler", e1);
			}
			return null;
		};

		BiConsumer<LogEntry, PreparedStatement> entry2stmt = (e, stmt) -> {
			try {
				stmt.setLong(1, e.getId());
				stmt.setInt(2, e.getMsgId());
				stmt.setShort(3, (short)e.getType().value);
				stmt.setTimestamp(4, Timestamp.valueOf(e.getTime()));
				stmt.setString(5, e.getMessage());
			} catch (Exception ee) {
				UIError.showError("DB Fehler", ee);
			}
		};
		
		return new LogModel(row2e, entry2stmt);
	}
}
