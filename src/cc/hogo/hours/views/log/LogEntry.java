package cc.hogo.hours.views.log;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Id;

public class LogEntry {

	public static enum Type {
		INFO('I'), WARNING('W'), ERROR('E');

		Type(char ch) {
			value = (short)ch;
		}

		public static Type parse(short ch) {
			switch (Character.toUpperCase(ch)) {
			case 'I':
				return INFO;
			case 'W':
				return WARNING;
			case 'E':
				return ERROR;
			default:
				return INFO;
			}
		}

		public short getValue() {
			return value;
		}

		final short value;
	}

	@Id
	long			id;
	int				logId;
	LocalDateTime	logDate;
	Type			logType;
	String 			logMsg;

	public LogEntry() {
	}
	
	public LogEntry(Type type, String message) {
		super();
		this.logType = type;
		this.logMsg = message;
	}

	public LogEntry(int msgId, Type type, String message) {
		super();
		this.logDate = LocalDateTime.now();
		this.logId = msgId;
		this.logType = type;
		this.logMsg = message;
	}

	public LogEntry(int id, int msgId, short type, Timestamp mod, String message) {
		this.id = id;
		this.logDate = mod.toLocalDateTime();
		this.logId = msgId;
		this.logType = Type.parse(type);
		this.logMsg = message;
	}
	
	public long getId() {
		return id;
	}

	public String getMessage() {
		return logMsg;
	}

	public void setMessage(String message) {
		this.logMsg = message;
	}

	public LocalDateTime getTime() {
		return logDate;
	}

	public Type	getType() {
		return logType;
	}

	public void setType(Type type) {
		this.logType = type;
	}

	public void setType(short type) {
		this.logType = Type.parse(type);
	}

	public int getMsgId() {
		return logId;
	}

	public void setMsgId(int msgId) {
		this.logId = msgId;
	}
	
}
