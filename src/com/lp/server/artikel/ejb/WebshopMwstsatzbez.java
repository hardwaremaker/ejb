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
	@NamedQuery(name = WebshopMwstsatzbezQuery.ByShopIdMwstsatzbezId,
		query = "SELECT OBJECT(o) FROM WebshopMwstsatzbez o WHERE o.webshopId=:shopId AND o.mwstsatzbezId=:mwstsatzbezId")
})

@Entity
@Table(name = "WW_WEBSHOPMWSTSATZBEZ")
public class WebshopMwstsatzbez implements Serializable, IIId {
	private static final long serialVersionUID = 2375912335144831641L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopId;

	@Column(name = "MWSTSATZBEZ_I_ID")
	private Integer mwstsatzbezId;
	
	@Column(name = "C_EXTERNE_ID")
	private String externalId;
	
	@Column(name = "T_AENDERN") 
	private Timestamp tAendern;
	
	@Column(name = "T_ANLEGEN") 
	private Timestamp tAnlegen;

	public WebshopMwstsatzbez() {
	}
	
	public WebshopMwstsatzbez(Integer id, Integer shopId, Integer mwstsatzbezId, String externalId) {
		setIId(id);
		setWebshopId(shopId);
		setMwstsatzbezIId(mwstsatzbezId);
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
	
	public Integer getMwstsatzbezIId() {
		return mwstsatzbezId;
	}
	
	public void setMwstsatzbezIId(Integer mwstsatzbezId) {
		this.mwstsatzbezId = mwstsatzbezId;
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
		result = prime * result + ((mwstsatzbezId == null) ? 0 : mwstsatzbezId.hashCode());
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
		WebshopMwstsatzbez other = (WebshopMwstsatzbez) obj;
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
		if (mwstsatzbezId == null) {
			if (other.mwstsatzbezId != null)
				return false;
		} else if (!mwstsatzbezId.equals(other.mwstsatzbezId))
			return false;
		if (webshopId == null) {
			if (other.webshopId != null)
				return false;
		} else if (!webshopId.equals(other.webshopId))
			return false;
		return true;
	}
}
