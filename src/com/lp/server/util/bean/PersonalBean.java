package com.lp.server.util.bean;

import com.lp.server.personal.ejbfac.PersonalFacBean;
import com.lp.server.personal.service.PersonalFac;

public class PersonalBean extends HvBean<PersonalFac> {
	public PersonalBean() {
		super(PersonalFacBean.class, PersonalFac.class);
	}
}
