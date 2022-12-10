package com.lp.server.shop.ejb;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.JobDetailsFac;


@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoSyncArtikelfindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsSyncItemEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = "AUTO_SHOPSYNC_ARTIKEL")
public class JobDetailsSyncItemEntity extends JobDetailsEntity {
	private static final long serialVersionUID = -1221803928244087697L;

	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopIId;

	@Column(name = "T_VOLLSTAENDIG")
	private Timestamp tVollstaendig;

	@Column(name = "T_GEAENDERT")
	private Timestamp tGeaendert;
	
	@Column(name = "C_EMAIL_FEHLER")
	private String cEmailFehler;

	@Column(name = "C_EMAIL_ERFOLGREICH")
	private String cEmailErfolgreich;


	public Integer getWebshopIId() {
		return webshopIId;
	}
	
	public void setWebshopIId(Integer webshopIId) {
		this.webshopIId = webshopIId;
	}
	
	public Timestamp getTVollstaendig() {
		return tVollstaendig;
	}
	
	public void setTVollstaendig(Timestamp tVollstaendig) {
		this.tVollstaendig = tVollstaendig;
	}
	
	public Timestamp getTGeaendert() {
		return tGeaendert;
	}
	
	public void setTGeaendert(Timestamp tGeaendert) {
		this.tGeaendert = tGeaendert;
	}
	
	public String getCEmailFehler() {
		return cEmailFehler;
	}

	public void setCEmailFehler(String cEmailFehler) {
		this.cEmailFehler = cEmailFehler;
	}

	public String getCEmailErfolgreich() {
		return cEmailErfolgreich;
	}

	public void setCEmailErfolgreich(String cEmailErfolgreich) {
		this.cEmailErfolgreich = cEmailErfolgreich;
	}
}
