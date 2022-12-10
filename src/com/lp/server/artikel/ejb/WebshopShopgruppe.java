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
	@NamedQuery(name = WebshopShopgruppeQuery.ByShopIdShopgruppeId,
		query = "SELECT OBJECT(o) FROM WebshopShopgruppe o WHERE o.webshopId=:shopId AND o.shopgruppeId=:shopgruppeId"),
	@NamedQuery(name = WebshopShopgruppeQuery.ByShopIdExternalId,
		query = "SELECT OBJECT(o) FROM WebshopShopgruppe o WHERE o.webshopId=:shopId AND o.externalId=:externalId"),
	@NamedQuery(name = WebshopShopgruppeQuery.ByShopIdWithDate, 
		query = "SELECT OBJECT(o) FROM WebshopShopgruppe o WHERE o.webshopId=:shopId AND o.tAendern >= :tChanged")
})

@Entity
@Table(name = "WW_WEBSHOPSHOPGRUPPE")
public class WebshopShopgruppe implements Serializable, IIId {
	private static final long serialVersionUID = 2375912335144831641L;

	@Id
	@Column(name = "I_ID")
//	@TableGenerator(name="ww_webshopshopgruppe_id", table="LP_PRIMARYKEY",
//		pkColumnName = "C_NAME", pkColumnValue="ww_webshopshopgruppe", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 10)
//	@GeneratedValue(strategy = GenerationType.TABLE, generator="ww_webshopshopgruppe_id")
	private Integer iId;
	
	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopId;

	@Column(name = "SHOPGRUPPE_I_ID")
	private Integer shopgruppeId;
	
	@Column(name = "C_EXTERNE_ID")
	private String externalId;
	
	@Column(name = "C_PFAD")
	private String pfad;

	@Column(name = "T_AENDERN") 
	private Timestamp tAendern;
	
	@Column(name = "T_ANLEGEN") 
	private Timestamp tAnlegen;

	public WebshopShopgruppe() {
	}
	
	public WebshopShopgruppe(Integer id, Integer shopId, Integer shopgruppeId, String externalId, String pfad) {
		setIId(id);
		setWebshopId(shopId);
		setShopgruppeIId(shopgruppeId);
		setExternalId(externalId);
		setPfad(pfad);
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
	
	public Integer getShopgruppeIId() {
		return shopgruppeId;
	}
	
	public void setShopgruppeIId(Integer shopgruppeId) {
		this.shopgruppeId = shopgruppeId;
	}
	
	public String getPfad() {
		return pfad;
	}
	
	public void setPfad(String pfad) {
		this.pfad = pfad;
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
		result = prime * result + ((pfad == null) ? 0 : pfad.hashCode());
		result = prime * result + ((shopgruppeId == null) ? 0 : shopgruppeId.hashCode());
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
		WebshopShopgruppe other = (WebshopShopgruppe) obj;
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
		if (pfad == null) {
			if (other.pfad != null)
				return false;
		} else if (!pfad.equals(other.pfad))
			return false;
		if (shopgruppeId == null) {
			if (other.shopgruppeId != null)
				return false;
		} else if (!shopgruppeId.equals(other.shopgruppeId))
			return false;
		if (webshopId == null) {
			if (other.webshopId != null)
				return false;
		} else if (!webshopId.equals(other.webshopId))
			return false;
		return true;
	}
}
