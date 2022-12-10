package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.MandantFacBean;
import com.lp.server.system.service.MandantFac;

public class MandantBean extends HvBean<MandantFac> {

	public MandantBean() {
		super(MandantFacBean.class, MandantFac.class);
	}
}
