package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.JobKpiReportFac;

@NamedQueries( { 
	@NamedQuery(name = JobKpiReportFac.KpiReportfindByMandantCnr, query = "SELECT OBJECT (o) FROM JobDetailsKpiReportEntity o WHERE o.mandantCNr=?1") 
	})

@Entity
@Table(name = "AUTO_KPIREPORT")
public class JobDetailsKpiReportEntity extends JobDetailsEntity {
	private static final long serialVersionUID = 8777791769624130565L;

	@Column(name = "I_TAGE")
	private Integer iTage;

	@Column(name = "C_PFADPATTERN")
	private String cPfadPattern;
	
	@Column(name = "I_ARCHIVIERUNGSTAGE")
	private Integer iArchivierungstage;

	@Column(name = "C_EMAILEMPFAENGER")
	private String cEmailEmpfaenger;

	public String getcEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}

	public void setcEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}

	public Integer getiTage() {
		return iTage;
	}

	public void setiTage(Integer iTage) {
		this.iTage = iTage;
	}

	public String getcPfadPattern() {
		return cPfadPattern;
	}

	public void setcPfadPattern(String cPfadPattern) {
		this.cPfadPattern = cPfadPattern;
	}

	public Integer getiArchivierungstage() {
		return iArchivierungstage;
	}

	public void setiArchivierungstage(Integer iArchivierungstage) {
		this.iArchivierungstage = iArchivierungstage;
	}
}
