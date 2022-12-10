package com.lp.server.util.bean;

import com.lp.server.auftrag.ejbfac.AuftragpositionFacBean;
import com.lp.server.auftrag.service.AuftragpositionFac;

public class AuftragpositionBean extends HvBean<AuftragpositionFac> {
	public AuftragpositionBean() {
		super(AuftragpositionFacBean.class, AuftragpositionFac.class);
	}
}
