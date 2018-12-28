package cc.hogo.hours.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.daro.common.ui.UIError;

public class GenericDbModel<T> implements AutoCloseable {

	public static class GenericDBIterator<T> implements Iterator<T>, AutoCloseable {
		final ResultSet rs;
		final Function<ResultSet, T> row2e;

		public GenericDBIterator(ResultSet rs, Function<ResultSet, T> row2e) {
			this.rs = rs;
			this.row2e = row2e;
		}

		@Override
		public boolean hasNext() {
			try {
				return rs.next();
			} catch (SQLException e) {
				UIError.showError("DB Fehler", e);
				return false;
			}
		}

		@Override
		public T next() {
			return row2e.apply(rs);
		}

		@Override
		public void close() throws Exception {
			if (!rs.isClosed())
				rs.close();
		}
	}

	protected Function<ResultSet, T> row2e;
	protected BiConsumer<T, PreparedStatement> e2row;

	protected PreparedStatement all;
	protected PreparedStatement insert;
	protected PreparedStatement update;
	protected PreparedStatement updateRead;
	protected PreparedStatement delete;

	public static String getStatementParameters(int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append('?');
			if (i + 1 < count)
				sb.append(',');
		}
		return sb.toString();
	}

	protected GenericDbModel() {
	}

	protected GenericDbModel(PreparedStatement all, PreparedStatement insert, PreparedStatement update,
			PreparedStatement delete, PreparedStatement updateRead, Function<ResultSet, T> row2entity,
			BiConsumer<T, PreparedStatement> entity2row) {
		this.all = all;
		this.update = update;
		this.insert = insert;
		this.delete = delete;
		this.updateRead = updateRead;
		this.row2e = row2entity;
		this.e2row = entity2row;
	}

	public GenericDbModel(Class<T> type, Function<ResultSet, T> row2entity, BiConsumer<T, PreparedStatement> entity2row)
			throws SQLException {

		EntityStructure<?> struct = EntityStructure.get(type);
		Connection c = DB.instance().getConnection();

		try {
			final String stmt = String.format("select %s from %s", struct.getFieldsAsString(), struct.getTableName());
			all = c.prepareStatement(stmt + " order by " + struct.getId());
			updateRead = c.prepareStatement(stmt + " where " + struct.getId() + "=? for update");
			insert = c.prepareStatement(String.format("insert into %s (%s) values(%s)", struct.getTableName(),
					struct.getFieldsAsStringWithoutId(), getStatementParameters(struct.getFieldCount() - 1)));
			update = c.prepareStatement(String.format("update %s set %s where %s = ?", struct.getTableName(),
					struct.getFieldsWithoutId().stream().map(f -> f + "=?").collect(Collectors.joining(",")),
					struct.getId()));
			delete = c.prepareStatement(
					String.format("delete from %s where %s = ?", struct.getTableName(), struct.getId()));
			this.row2e = row2entity;
			this.e2row = entity2row;
		} catch (SQLException e) {
			if (all != null)
				all.close();
			if (insert != null)
				insert.close();
			if (update != null)
				update.close();
		}
	}

	public List<T> select() throws SQLException {
		List<T> list = new LinkedList<>();
		try (ResultSet rs = all.executeQuery()) {
			while (rs.next()) {
				T entry = row2e.apply(rs);
				list.add(entry);
			}
		}
		return list;

	}

	public Iterator<T> read() throws SQLException {
		final ResultSet rs = all.executeQuery();
		return new GenericDBIterator<>(rs, row2e);
	}

	public void add(T e) throws SQLException {
		if (insert == null)
			return;
		e2row.accept(e, insert);
		insert.executeUpdate();
		insert.getConnection().commit();
	}

	public T read(long id) throws SQLException {
		if (updateRead == null)
			return null;
		updateRead.setLong(1, id);
		try (ResultSet rs = updateRead.executeQuery()) {
			return rs.next() ? row2e.apply(rs) : null;
		}
	}

	public void update(T e, BiConsumer<T, PreparedStatement> e2r) throws SQLException {
		if (update == null)
			return;
		if (e2r == null)
			e2row.accept(e, update);
		else
			e2r.accept(e, update);

		update.executeUpdate();
		update.getConnection().commit();
	}

	public void update(T e) throws SQLException {
		if (update == null)
			return;
		e2row.accept(e, update);
		update.executeUpdate();
		update.getConnection().commit();
	}

	public void delete(Object id) throws SQLException {
		if (delete == null)
			return;
		delete.setObject(1, id);
		delete.executeUpdate();
		delete.getConnection().commit();
	}

	@Override
	public void close() throws Exception {
		if (all != null)
			all.close();
		if (insert != null)
			insert.close();
		if (update != null)
			update.close();
		if (delete != null)
			delete.close();
	}

}