package com.lp.server.util.bean;

import com.lp.server.system.jcr.ejbfac.JCRDocFacBean;
import com.lp.server.system.jcr.service.JCRDocFac;

public class JcrDocBean extends HvBean<JCRDocFac> {
	public JcrDocBean() {
		super(JCRDocFacBean.class, JCRDocFac.class);
	}
}
