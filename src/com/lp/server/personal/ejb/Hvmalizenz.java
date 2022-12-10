package com.lp.server.personal.ejb;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries({
	@NamedQuery(name = HvmalizenzQuery.ByCnr, query = "SELECT OBJECT(l) FROM Hvmalizenz l WHERE l.cNr = :licence"),
})
@Entity
@Table(name = "PERS_HVMALIZENZ")
public class Hvmalizenz implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "I_MAX_USER")
	private Integer iMaxUser;
	

	private static final long serialVersionUID = 1L;

	public Hvmalizenz() {
		super();
	}

	public Hvmalizenz(Integer id, String nr, Integer iMaxUser) {
		setCNr(nr);
		setIId(id);
		setIMaxUser(iMaxUser);

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

	public Integer getIMaxUser() {
		return iMaxUser;
	}

	public void setIMaxUser(Integer iMaxUser) {
		this.iMaxUser = iMaxUser;
	}

	

}
