package com.lp.server.util.bean;

import com.lp.server.auftrag.ejbfac.AuftragFacBean;
import com.lp.server.auftrag.service.AuftragFac;

public class AuftragBean extends HvBean<AuftragFac> {
	public AuftragBean() {
		super(AuftragFacBean.class, AuftragFac.class);
	}
}
