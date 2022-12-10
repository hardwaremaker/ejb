package com.lp.server.util.bean;

import com.lp.server.partner.ejbfac.KundeFacBean;
import com.lp.server.partner.service.KundeFac;

public class KundeBean extends HvBean<KundeFac> {

	public KundeBean() {
		super(KundeFacBean.class, KundeFac.class);
	}
}
