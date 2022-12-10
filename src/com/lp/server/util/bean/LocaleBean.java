package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.LocaleFacBean;
import com.lp.server.system.service.LocaleFac;

public class LocaleBean extends HvBean<LocaleFac> {
	public LocaleBean() {
		super(LocaleFacBean.class, LocaleFac.class);
	}
}
