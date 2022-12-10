package com.lp.server.shop.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.ejb.JobDetailsEntity;
import com.lp.server.system.service.JobDetailsFac;


@NamedQueries( { 
	@NamedQuery(name = JobDetailsFac.AutoSyncOrderfindByMandantCNr, query = "SELECT OBJECT (o) FROM JobDetailsSyncOrderEntity o WHERE o.mandantCNr=?1") 
})

@Entity
@Table(name = "AUTO_SHOPSYNC_ORDER")
public class JobDetailsSyncOrderEntity extends JobDetailsEntity {
	private static final long serialVersionUID = -1221803928244087697L;

	@Column(name = "WEBSHOP_I_ID")
	private Integer webshopIId;

	
	public Integer getWebshopIId() {
		return webshopIId;
	}
	
	public void setWebshopIId(Integer webshopIId) {
		this.webshopIId = webshopIId;
	}
}
