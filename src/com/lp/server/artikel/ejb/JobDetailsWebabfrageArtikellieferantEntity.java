package com.lp.server.artikel.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.ITablenames;
import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoArtikellieferantWebabfragefindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsWebabfrageArtikellieferantEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = ITablenames.AUTO_ARTIKELLIEFERANT_WEBABFRAGE)
public class JobDetailsWebabfrageArtikellieferantEntity extends JobDetailsEntity {
	private static final long serialVersionUID = 2961937875233828374L;

	@Column(name = "C_EMAIL_FEHLER")
	private String cEmailFehler;
	
	@Column(name = "C_EMAIL_ERFOLGREICH")
	private String cEmailErfolgreich;
	
	public JobDetailsWebabfrageArtikellieferantEntity() {
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
