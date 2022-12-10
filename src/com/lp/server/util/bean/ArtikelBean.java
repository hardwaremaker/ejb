package com.lp.server.util.bean;

import com.lp.server.artikel.ejbfac.ArtikelFacBean;
import com.lp.server.artikel.service.ArtikelFac;

public class ArtikelBean extends HvBean<ArtikelFac> {
	public ArtikelBean() {
		super(ArtikelFacBean.class, ArtikelFac.class);
	}
}
