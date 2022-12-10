package com.lp.server.util.bean;

import com.lp.server.benutzer.ejbfac.LogonFacBean;
import com.lp.server.benutzer.service.LogonFac;

public class LogonBean extends HvBean<LogonFac> {

	public LogonBean() {
		super(LogonFacBean.class, LogonFac.class);
	}
}
