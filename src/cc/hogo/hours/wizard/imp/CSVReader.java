package cc.hogo.hours.wizard.imp;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import cc.hogo.hours.db.HourEntry;

public class CSVReader implements AutoCloseable {
	private static final String HEADER0 = "\"GESCHST\";\"KUNDENNR\";\"KUNDNAME\";\"GESCHST_1\";\"KURZBEZ\";\"PERSONALNR\";\"FREMDLOHNNR\";\"NACHNAME\";\"VORNAME\";\"DISPONENT\";\"FAKTUR_STUNDEN\";\"LOHN_STUNDEN\"";
	private static final String HEADER1 = "GESCHST;KUNDENNR;KUNDNAME;GESCHST_1;KURZBEZ;PERSONALNR\";FREMDLOHNNR;NACHNAME;VORNAME;DISPONENT;FAKTUR_STUNDEN;LOHN_STUNDEN";
	private final LineNumberReader reader;
	private static final String[] HEADER = HEADER1.split(";");

	private CSVReader(LineNumberReader reader) {
		this.reader = reader;
	}

	public static CSVReader open(String path) throws IOException {

		LineNumberReader reader = new LineNumberReader(new FileReader(path));
		String line = reader.readLine();

		if (line.compareToIgnoreCase(HEADER0) == 0 || line.compareToIgnoreCase(HEADER1) == 0) {
			reader.close();
			throw new IOException("Invalid format");
		}
		return new CSVReader(reader);
	}

	public HourEntry readNext() throws IOException {
		String line = reader.readLine();

		if (line != null) {
			String[] e = line.split(";");
			try {
				return new HourEntry(e);
			} catch (Exception ex) {
				throw new IOException("Inhalt auf Zeile " + reader.getLineNumber() + " ist ungültig.", ex);
			}
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
