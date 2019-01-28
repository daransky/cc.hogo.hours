package cc.hogo.hours.core.export;

import java.util.function.Function;

public class CSVConverter<T> {

	Function<T, String[]> valueConverter;
	
	public CSVConverter(Function<T,String[]> typeToStringValues) {
	}
}
