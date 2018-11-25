package cc.hogo.hours.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DISPONENT")
public class Disponent {

	@Id
	@Column(unique = true, length = 8)
	String sid;

	@Column(length = 128)
	String name;
	@Column(length = 128)
	String firma;

	public static final int Year = 1, Disponent = 2, All = 3;

	public Disponent() {
	}
	
	public Disponent(String login, String name) {
		this(login, name, null);
	}

	public Disponent(String login, String name, String company) {
		super();
		this.sid = login;
		this.name = name;
		this.firma = company;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}
	
	@Override
	public String toString() {
		return name != null ? name : sid;
	}

}
