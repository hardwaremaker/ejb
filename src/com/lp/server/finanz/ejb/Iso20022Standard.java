package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = Iso20022Query.StandardAll, query = "SELECT OBJECT (o) FROM Iso20022Standard o"),
	@NamedQuery(name = Iso20022Query.StandardFindByCNr, query = "SELECT OBJECT (o) FROM Iso20022Standard o WHERE cNr = ?1")
})

@Entity
@Table(name = ITablenames.FB_ISO20022STANDARD)
public class Iso20022Standard implements Serializable {
	private static final long serialVersionUID = -6201337287095656862L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name="iso20022standard_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue="iso20022standard", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="iso20022standard_id")
	private Integer iId;
	
	@Column(name="C_NR")
	private String cNr;
	
	@Column(name="C_BEZ")
	private String cBez;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result
				+ ((cNr == null) ? 0 : cNr.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Iso20022Standard other = (Iso20022Standard) obj;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		
		if (cNr == null) {
			if (other.cNr != null)
				return false;
		} else if (!cNr.equals(other.cNr))
			return false;

		if (cBez == null) {
			if (other.cBez != null)
				return false;
		} else if (!cBez.equals(other.cBez))
			return false;
		
		return true;
	}
	
}

