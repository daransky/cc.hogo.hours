package cc.hogo.hours.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Id;
import javax.persistence.Table;

public class EntityStructure<E> {
	private static final Map<Class<?>, EntityStructure<?>> cache = new HashMap<>();
	
	final List<String> fields;
	final String id;
	final String tableName;

	public EntityStructure(Class<E> type) {
		Table table = type.getAnnotation(Table.class);
		tableName = (table != null) ? table.name() : type.getName();

		Stream<Field> stream = Arrays.asList(type.getDeclaredFields()).stream().filter(f -> ( ( f.getModifiers() & Modifier.FINAL) == 0 && (f.getModifiers() & Modifier.STATIC) == 0 ));
		fields = stream.map(f -> f.getName()).collect(Collectors.toList());
		Collection<Field> all = Arrays.asList(type.getDeclaredFields()).stream().collect(Collectors.toList());
		
		id = all.stream().filter(f -> f.getAnnotation(Id.class) != null).findFirst().get().getName();
	}

	public int getFieldCount() {
		return fields.size();
	}

	public String getId() {
		return id;
	}

	public String getFieldsAsString() {
		return fields.stream().collect(Collectors.joining(", "));
	}

	public String getFieldsAsStringWithoutId() {
		return getFieldsWithoutId().stream().collect(Collectors.joining(", "));
	}

	public String getTableName() {
		return tableName;
	}
	
	public List<String> getFields() {
		return fields;
	}
	public List<String> getFieldsWithoutId() {
		final String id = getId();
		return fields.stream().filter( name -> name.compareToIgnoreCase(id) != 0 ).collect(Collectors.toList());
	}

	public static EntityStructure<?> get(Class<?> type) {
		EntityStructure<?> e = (EntityStructure<?>) cache.get(type);
		if (e == null) {
			e = new EntityStructure<>(type);
			cache.put(type, e);
		}
		return e;
	}

}
