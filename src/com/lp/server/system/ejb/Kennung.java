package com.lp.server.system.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries( {
	@NamedQuery(name = KennungQuery.ByCnr, 
			query = "SELECT OBJECT(o) FROM Kennung o WHERE o.cNr = :cnr")
	
})
@Entity
@Table(name = ITablenames.LP_KENNUNG)
public class Kennung implements Serializable {
	private static final long serialVersionUID = -5109473076360442614L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BEZ")
	private String cBez;

	public Kennung() {
	}
	
	public Kennung(Integer id, String cnr, String cbez) {
		setIId(id);
		setCNr(cnr);
		setCBez(cbez);
		
	}
	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}
}
