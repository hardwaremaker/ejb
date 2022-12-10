package com.lp.server.util.bean;

import com.lp.server.partner.ejbfac.PartnerFacBean;
import com.lp.server.partner.service.PartnerFac;

public class PartnerBean extends HvBean<PartnerFac> {

	public PartnerBean() {
		super(PartnerFacBean.class, PartnerFac.class);
	}
}
