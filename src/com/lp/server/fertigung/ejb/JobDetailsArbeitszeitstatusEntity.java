package com.lp.server.fertigung.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoArbeitszeitstatusfindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsArbeitszeitstatusEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = "AUTO_ARBEITSZEITSTATUS")
public class JobDetailsArbeitszeitstatusEntity extends JobDetailsEntity {
	private static final long serialVersionUID = -8096804936551495361L;

	@Column(name = "C_PFADPATTERN")
	private String cPfadPattern;
	
	@Column(name = "C_EMAILEMPFAENGER")
	private String cEmailEmpfaenger;
	
	@Column(name = "I_TAGEBISSTICHTAG")
	private Integer iTageBisStichtag;
	
	@Column(name = "I_ARCHIVIERUNGSTAGE")
	private Integer iArchivierungstage;

	
	public String getCPfadPattern() {
		return cPfadPattern;
	}
	
	public void setCPfadPattern(String cPfadPattern) {
		this.cPfadPattern = cPfadPattern;
	}
	
	public String getCEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}
	
	public void setCEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}
	
	public Integer getITageBisStichtag() {
		return iTageBisStichtag;
	}
	
	public void setITageBisStichtag(Integer iTageBisStichtag) {
		this.iTageBisStichtag = iTageBisStichtag;
	}
	
	public Integer getIArchivierungstage() {
		return iArchivierungstage;
	}
	
	public void setIArchivierungstage(Integer iArchivierungstage) {
		this.iArchivierungstage = iArchivierungstage;
	}
	
}
