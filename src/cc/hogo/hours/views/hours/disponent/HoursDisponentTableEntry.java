package cc.hogo.hours.views.hours.disponent;

public class HoursDisponentTableEntry {
	
	int kundennummer;
	String kundenname;
	String kurzbezeichnung;
	
	float current[] = new float[12];
	
	public HoursDisponentTableEntry(int kundennummer, String kundenname, String kurzbezeichnung) {
		super();
		this.kundennummer = kundennummer;
		this.kundenname = kundenname;
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public float getTotal() {
		float sum = 0.0f;
		for( int i = 0; i < 12; i++)
			sum += current[i];
		return sum;
	}
	
	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}
	
	
	public int getKundennummer() {
		return kundennummer;
	}

	public String getKundenname() {
		return kundenname;
	}

	public float getHours(int month) {
		return this.current[month];
	}

	public void setHours(int month, float value) {
		this.current[month] = value;
	}
	
	
}
