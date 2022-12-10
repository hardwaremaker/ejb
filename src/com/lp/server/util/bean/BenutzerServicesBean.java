package com.lp.server.util.bean;

import com.lp.server.benutzer.ejbfac.BenutzerServicesFacBean;
import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;

public class BenutzerServicesBean extends HvBean<BenutzerServicesFacLocal> {
	public BenutzerServicesBean() {
		super(BenutzerServicesFacBean.class, BenutzerServicesFacLocal.class);
	}
}
