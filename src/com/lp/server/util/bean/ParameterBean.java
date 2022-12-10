package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.ParameterFacBean;
import com.lp.server.system.service.ParameterFac;

public class ParameterBean extends HvBean<ParameterFac> {
	public ParameterBean() {
		super(ParameterFacBean.class, ParameterFac.class);
	}
}
