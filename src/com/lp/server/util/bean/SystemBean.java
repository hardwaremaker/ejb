package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.SystemFacBean;
import com.lp.server.system.service.SystemFac;

public class SystemBean extends HvBean<SystemFac> {
	public SystemBean() {
		super(SystemFacBean.class, SystemFac.class);
	}
}
