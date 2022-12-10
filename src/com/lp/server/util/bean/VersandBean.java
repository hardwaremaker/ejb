package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.VersandFacBean;
import com.lp.server.system.service.VersandFac;

public class VersandBean extends HvBean<VersandFac> {
	public VersandBean() {
		super(VersandFacBean.class, VersandFac.class);
	}
}
