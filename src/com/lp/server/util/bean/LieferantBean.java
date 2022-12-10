package com.lp.server.util.bean;

import com.lp.server.partner.ejbfac.LieferantFacBean;
import com.lp.server.partner.service.LieferantFac;

public class LieferantBean extends HvBean<LieferantFac> {

	public LieferantBean() {
		super(LieferantFacBean.class, LieferantFac.class);
	}
}
