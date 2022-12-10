package com.lp.server.fertigung.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.ITablenames;
import com.lp.server.system.service.JobDetailsFac;

@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoBedarfsuebernahmeOffenefindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsBedarfsuebernahmeOffeneEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = ITablenames.AUTO_BEDARFSUEBERNAHMEOFFENJOURNAL)
public class JobDetailsBedarfsuebernahmeOffeneEntity extends JobDetailsEntity {
	private static final long serialVersionUID = 7935079851966449513L;
	
	@Column(name = "C_EMAILEMPFAENGER")
	private String cEmailEmpfaenger;

	public JobDetailsBedarfsuebernahmeOffeneEntity() {
	}

	public void setCEmailEmpfaenger(String cEmailEmpfaenger) {
		this.cEmailEmpfaenger = cEmailEmpfaenger;
	}
	public String getCEmailEmpfaenger() {
		return cEmailEmpfaenger;
	}
}
