package cc.hogo.hours.wizard.imp;

import java.time.LocalDate;

public class LastImport {
	int recordCount, firstRecord, lastRecord;
	LocalDate lastDate;

	public static LastImport fromString(String arg) {

		LastImport li = new LastImport();

		int begin = 0, end = 0;
		int count = 0, x = 0;

		while (true) {
			if ((begin = arg.indexOf('\'', begin)) != -1) {
				if ((end = arg.indexOf('\'', begin + 1)) == -1) {
					break;
				}
			} else
				break;

			String value = arg.substring(begin + 1, end).trim();
			begin = end + 1;
			switch (++count) {
			case 1:
				li.recordCount = Integer.parseInt(value);
				break;
			case 2:
				x = value.indexOf('/');
				if (x != -1) {
					li.lastDate = LocalDate.of(Integer.parseInt(value.substring(x + 1)),
							Integer.parseInt(value.substring(0, x)), 1);
				}
				break;
			case 3:
				x = value.indexOf('-');
				li.firstRecord = Integer.parseInt(value.substring(0, x));
				li.lastRecord = Integer.parseInt(value.substring(x + 1));
				break;
			}
		}
		return li;
	}

	public boolean isDateSame(int year, int month) {
		return lastDate.compareTo(LocalDate.of(year, month, 1)) == 0;
	}

}
