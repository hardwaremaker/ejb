
package com.lp.server.personal.ejb;



import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = "HvmabenutzerfindByBenutzerIIdHvmalizenzIId", query = "SELECT OBJECT(C) FROM Hvmabenutzer c WHERE c.benutzerIId = ?1 AND c.hvmalizenzIId = ?2"),
	@NamedQuery(name = "HvmabenutzerfindByHvmalizenzIId", query = "SELECT count(c.iId) FROM Hvmabenutzer c WHERE c.hvmalizenzIId = ?1")})


@Entity
@Table(name = "PERS_HVMABENUTZER")
public class Hvmabenutzer implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "BENUTZER_I_ID")
	private Integer benutzerIId;

	@Column(name = "HVMALIZENZ_I_ID")
	private Integer hvmalizenzIId;
	
	@Column(name = "C_TOKEN")
	private String cToken;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;
	

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	private static final long serialVersionUID = 1L;

	public Hvmabenutzer() {
		super();
	}

	public Hvmabenutzer(Integer id, Integer benutzerIId, Integer hvmalizenzIId) {
		setBenutzerIId(benutzerIId);
		setIId(id);
		setHvmalizenzIId(hvmalizenzIId);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}


	public Integer getBenutzerIId() {
		return benutzerIId;
	}

	public void setBenutzerIId(Integer benutzerIId) {
		this.benutzerIId = benutzerIId;
	}

	public String getcToken() {
		return cToken;
	}

	public void setcToken(String cToken) {
		this.cToken = cToken;
	}

	
	public Integer getHvmalizenzIId() {
		return hvmalizenzIId;
	}

	public void setHvmalizenzIId(Integer hvmalizenzIId) {
		this.hvmalizenzIId = hvmalizenzIId;
	}


	

}
