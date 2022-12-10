package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.IIId;

@NamedQueries( {
	@NamedQuery(name = WebshopKundeQuery.ByShopIdKundeId,
		query = "SELECT OBJECT(o) FROM WebshopKunde o WHERE o.webshopId=:shopId AND o.kundeId=:kundeId"),
	@NamedQuery(name = WebshopKundeQuery.ByShopIdExternalId,
	query = "SELECT OBJECT(o) FROM WebshopKunde o WHERE o.webshopId=:shopId AND o.externalId=:externalId")	
})

@Entity
@Table(name = "WW_WEBSHOPKUNDE")
public class WebshopKunde implements Serializable, IIId {
	private static final long serialVersionUID = 2375912335144831641L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopId;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeId;
	
	@Column(name = "C_EXTERNE_ID")
	private String externalId;
	
	@Column(name = "T_AENDERN") 
	private Timestamp tAendern;
	
	@Column(name = "T_ANLEGEN") 
	private Timestamp tAnlegen;

	public WebshopKunde() {
	}
	
	public WebshopKunde(Integer id, Integer shopId, Integer kundeId, String externalId) {
		setIId(id);
		setWebshopId(shopId);
		setKundeIId(kundeId);
		setExternalId(externalId);
	}
	
	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public Integer getWebshopId() {
		return webshopId;
	}
	
	public void setWebshopId(Integer webshopId) {
		this.webshopId = webshopId;
	}
	
	public Integer getKundeIId() {
		return kundeId;
	}
	
	public void setKundeIId(Integer kundeId) {
		this.kundeId = kundeId;
	}
	
	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalId == null) ? 0 : externalId.hashCode());
		result = prime * result + ((iId == null) ? 0 : iId.hashCode());
		result = prime * result + ((kundeId == null) ? 0 : kundeId.hashCode());
		result = prime * result + ((webshopId == null) ? 0 : webshopId.hashCode());
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
		WebshopKunde other = (WebshopKunde) obj;
		if (externalId == null) {
			if (other.externalId != null)
				return false;
		} else if (!externalId.equals(other.externalId))
			return false;
		if (iId == null) {
			if (other.iId != null)
				return false;
		} else if (!iId.equals(other.iId))
			return false;
		if (kundeId == null) {
			if (other.kundeId != null)
				return false;
		} else if (!kundeId.equals(other.kundeId))
			return false;
		if (webshopId == null) {
			if (other.webshopId != null)
				return false;
		} else if (!webshopId.equals(other.webshopId))
			return false;
		return true;
	}
}
