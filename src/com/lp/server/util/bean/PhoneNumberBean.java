package com.lp.server.util.bean;

import com.lp.server.system.ejbfac.PhoneNumberFacBean;
import com.lp.server.system.ejbfac.PhoneNumberFacLocal;

public class PhoneNumberBean extends HvLocalBean<PhoneNumberFacLocal> {

	public PhoneNumberBean() {
		super(PhoneNumberFacBean.class, PhoneNumberFacLocal.class);
	}
}
