package com.lp.server.eingangsrechnung.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoErImportfindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsErImportEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = "AUTO_ER_IMPORT")
public class JobDetailsErImportEntity extends JobDetailsEntity {
	private static final long serialVersionUID = -2003223964260576275L;

	@Column(name = "C_IMPORTPFAD")
	private String cImportPfad;
	
	@Column(name = "C_EMAIL_FEHLER")
	private String cEmailFehler;
	
	@Column(name = "C_EMAIL_ERFOLGREICH")
	private String cEmailErfolgreich;
	
	public JobDetailsErImportEntity() {
	}

	public String getCImportPfad() {
		return cImportPfad;
	}
	
	public void setCImportPfad(String cImportPfad) {
		this.cImportPfad = cImportPfad;
	}
	
	public String getCEmailFehler() {
		return cEmailFehler;
	}
	
	public void setcCEmailFehler(String cEmailFehler) {
		this.cEmailFehler = cEmailFehler;
	}
	
	public String getCEmailErfolgreich() {
		return cEmailErfolgreich;
	}
	
	public void setCEmailErfolgreich(String cEmailErfolgreich) {
		this.cEmailErfolgreich = cEmailErfolgreich;
	}
}
