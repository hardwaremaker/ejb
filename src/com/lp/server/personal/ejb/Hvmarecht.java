
package com.lp.server.personal.ejb;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries({ @NamedQuery(name = "HvmarechtfindByLizenzIId", query = "SELECT OBJECT(o) FROM Hvmarecht o WHERE o.hvmalizenzIId=?1 AND o.bAktiv=1 ORDER BY o.cNr"),
	@NamedQuery(name = "HvmarechtfindAll", query = "SELECT OBJECT(o) FROM Hvmarecht o WHERE o.bAktiv=1 ORDER BY o.hvmalizenzIId, o.cNr")})

@Entity
@Table(name = "PERS_HVMARECHT")
public class Hvmarecht implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "HVMALIZENZ_I_ID")
	private Integer hvmalizenzIId;
	
	@Column(name="B_AKTIV")
	private Short bAktiv;

	private static final long serialVersionUID = 1L;

	public Hvmarecht() {
		super();
	}

	public Hvmarecht(Integer id, String nr, Integer hvmalizenzIId) {
		setCNr(nr);
		setIId(id);
		setHvmalizenzIId(hvmalizenzIId);

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getHvmalizenzIId() {
		return hvmalizenzIId;
	}

	public void setHvmalizenzIId(Integer hvmalizenzIId) {
		this.hvmalizenzIId = hvmalizenzIId;
	}

	public void setBAktiv(Short aktiv) {
		this.bAktiv = aktiv;
	}

	public Short getBAktiv() {
		return this.bAktiv;
	}
}
