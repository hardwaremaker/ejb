package com.lp.server.util.bean;

import com.lp.server.stueckliste.ejbfac.StuecklisteFacBean;
import com.lp.server.stueckliste.service.StuecklisteFac;

public class StuecklisteBean extends HvBean<StuecklisteFac> {

	public StuecklisteBean() {
		super(StuecklisteFacBean.class, StuecklisteFac.class);
	}
}
