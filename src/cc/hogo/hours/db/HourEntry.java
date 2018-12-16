package cc.hogo.hours.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOURS")
public class HourEntry {

	@Id
	private long id;

	int year;
	int month;
	int geschaeftStelle;
	int kundenNummer;
	int geschaeftStelle1;
	int personalNummer;
	int fremdLohnNummer;
	float fakturStunden;
	float lohnStunden;

	@Column(length = 8)
	String kurzBezeichnung;
	@Column(length = 8)
	String disponentId;
	@Column(length = 128)
	String nachname;
	@Column(length = 32)
	String vorname;
	@Column(length = 128)
	String kundenName;
	String info;

	public HourEntry() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getGeschaeftStelle() {
		return geschaeftStelle;
	}

	public void setGeschaeftStelle(int geschaeftStelle) {
		this.geschaeftStelle = geschaeftStelle;
	}

	public int getKundenNummer() {
		return kundenNummer;
	}

	public void setKundenNummer(int kundenNummer) {
		this.kundenNummer = kundenNummer;
	}

	public int getGeschaeftStelle1() {
		return geschaeftStelle1;
	}

	public void setGeschaeftStelle1(int geschaeftStelle1) {
		this.geschaeftStelle1 = geschaeftStelle1;
	}

	public int getPersonalNummer() {
		return personalNummer;
	}

	public void setPersonalNummer(int personalNummer) {
		this.personalNummer = personalNummer;
	}

	public int getFremdLohnNummer() {
		return fremdLohnNummer;
	}

	public void setFremdLohnNummer(int fremdLohnNummer) {
		this.fremdLohnNummer = fremdLohnNummer;
	}

	public float getFakturStunden() {
		return fakturStunden;
	}

	public void setFakturStunden(float fakturStunden) {
		this.fakturStunden = fakturStunden;
	}

	public float getLohnStunden() {
		return lohnStunden;
	}

	public void setLohnStunden(float lohnStunden) {
		this.lohnStunden = lohnStunden;
	}

	public String getDisponentId() {
		return disponentId;
	}

	public void setDisponentId(String disponentId) {
		this.disponentId = disponentId;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getKundenName() {
		return ( kundenName == null || kundenName.isEmpty()) ? Integer.toString(kundenNummer):kundenName;
	}

	public void setKundenName(String kundenName) {
		this.kundenName = kundenName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public HourEntry(String[] e) throws Exception {
		geschaeftStelle = toInt(getUnquoted(e[0]));
		kundenNummer = toInt(getUnquoted(e[1]));
		String tmp = getUnquoted(e[2]);
		if( tmp.startsWith("Monat ") && tmp.contains("- Jahr")) {
			String[] values = tmp.split("-");
			values[0] = values[0].trim();
			values[1] = values[1].trim();
			month = toInt(values[0].substring(values[0].length()-2));
			if( month > 0)
				month--;
			year = toInt(values[1].substring(values[1].length() -4));
		} else {
			kundenName = getUnquoted(e[2]);
		}
		geschaeftStelle1 = toInt(getUnquoted(e[3]));
		kurzBezeichnung = getUnquoted(e[4]);
		personalNummer = toInt(getUnquoted(e[5]));
		fremdLohnNummer = toInt(getUnquoted(e[6]));
		nachname = getUnquoted(e[7]);
		vorname = getUnquoted(e[8]);
		disponentId = getUnquoted(e[9]);
		fakturStunden = toFloat(getUnquoted(e[10]));
		lohnStunden = (e.length <= 11) ? fakturStunden : toFloat(getUnquoted(e[11]));
	}

	public String getKurzBezeichnung() {
		return kurzBezeichnung;
	}

	public void setKurzBezeichnung(String kurzBezeichnung) {
		this.kurzBezeichnung = kurzBezeichnung;
	}

	static String getUnquoted(String arg) {
		int i = arg.indexOf('\"');
		int e = arg.lastIndexOf('\"');

		return (i != -1 && e != -1) ? arg.substring(i + 1, e).trim() : arg;
	}

	static int toInt(String arg) {
		return Integer.parseInt(arg);
	}

	static float toFloat(String arg) {
		if (arg != null) {
			if (arg.indexOf('.') != -1)
				arg = arg.replace(".", "");

			arg = arg.replace(',', '.');

		}
		return Float.parseFloat(arg);
	}

	@Override
	public int hashCode() {
		if (id == 0) {
			id = 7;
			id = id * 11 + year;
			id = id * 11 + month;
			id = id * 31 + geschaeftStelle;
			id = id * 31 + kundenNummer;
			id = id * 31 + geschaeftStelle1;
			id = id * 31 + personalNummer;
			id = id * 31 + fremdLohnNummer;
			id = id * 31 + (int) fakturStunden;
			id = id * 31 + (int) lohnStunden;

			if (kurzBezeichnung != null)
				id = id * 31 + kurzBezeichnung.hashCode();
			if (disponentId != null)
				id = id * 31 + disponentId.hashCode();
			if (nachname != null)
				id = id * 31 + nachname.hashCode();
			if (vorname != null)
				id = id * 31 + vorname.hashCode();
			if (kundenName != null)
				id = id * 31 + kundenName.hashCode();
		}
		return (int) id;
	}
}
