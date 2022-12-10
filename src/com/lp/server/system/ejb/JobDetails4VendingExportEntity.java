package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.Auto4VendingExportfindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetails4VendingExportEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = "AUTO_4VENDING_EXPORT")
public class JobDetails4VendingExportEntity extends JobDetailsEntity {

	private static final long serialVersionUID = 5290930428161466178L;

	@Column(name = "B_ARTIKEL")
	private Short bArtikel;
	
	@Column(name = "B_KUNDEN")
	private Short bKunden;
	
	@Column(name = "B_LIEFERANTEN")
	private Short bLieferanten;
	
	@Column(name = "C_PFADPATTERN_ARTIKEL")
	private String cPfadPatternArtikel;
	
	@Column(name = "C_PFADPATTERN_KUNDEN")
	private String cPfadPatternKunden;
	
	@Column(name = "C_PFADPATTERN_LIEFERANTEN")
	private String cPfadPatternLieferanten;
	
	@Column(name = "C_EMAIL_FEHLER")
	private String cEmailFehler;

	@Column(name = "C_EMAIL_ERFOLGREICH")
	private String cEmailErfolgreich;

	public JobDetails4VendingExportEntity() {
	}

	public Short getBArtikel() {
		return bArtikel;
	}

	public void setBArtikel(Short bArtikel) {
		this.bArtikel = bArtikel;
	}

	public Short getBKunden() {
		return bKunden;
	}

	public void setBKunden(Short bKunden) {
		this.bKunden = bKunden;
	}

	public Short getBLieferanten() {
		return bLieferanten;
	}

	public void setBLieferanten(Short bLieferanten) {
		this.bLieferanten = bLieferanten;
	}

	public String getCPfadPatternKunden() {
		return cPfadPatternKunden;
	}

	public void setCPfadPatternKunden(String cPfadPatternKunden) {
		this.cPfadPatternKunden = cPfadPatternKunden;
	}

	public String getCPfadPatternLieferanten() {
		return cPfadPatternLieferanten;
	}

	public void setCPfadPatternLieferanten(String cPfadPatternLieferanten) {
		this.cPfadPatternLieferanten = cPfadPatternLieferanten;
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

	public String getCPfadPatternArtikel() {
		return cPfadPatternArtikel;
	}

	public void setCPfadPatternArtikel(String cPfadPatternArtikel) {
		this.cPfadPatternArtikel = cPfadPatternArtikel;
	}

}
