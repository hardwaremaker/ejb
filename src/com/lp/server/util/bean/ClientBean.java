package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.TheClientFacBean;
import com.lp.server.system.service.TheClientFac;

public class ClientBean extends HvBean<TheClientFac> {

	public ClientBean() {
		super(TheClientFacBean.class, TheClientFac.class);
	}
}
