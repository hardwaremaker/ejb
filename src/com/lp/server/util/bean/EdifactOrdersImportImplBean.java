package com.lp.server.util.bean;

import com.lp.server.auftrag.ejbfac.EdifactOrdersImportImplFacBean;
import com.lp.server.auftrag.service.EdifactOrdersImportImplFac;

public class EdifactOrdersImportImplBean extends HvBean<EdifactOrdersImportImplFac> {
	public EdifactOrdersImportImplBean() {
		super(EdifactOrdersImportImplFacBean.class, EdifactOrdersImportImplFac.class);
	}
}
