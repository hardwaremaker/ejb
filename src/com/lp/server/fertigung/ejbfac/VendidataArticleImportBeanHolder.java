package com.lp.server.fertigung.ejbfac;

import java.io.Serializable;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.FehlmengeFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.FertigungImportFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;

public class VendidataArticleImportBeanHolder implements Serializable {
	
	private static final long serialVersionUID = 6398739748992612752L;

	private FertigungFac fertigungFac;
	private FertigungImportFac fertigungImportFac;
	private ArtikelFac artikelFac;
	private StuecklisteFac stuecklisteFac;
	private LagerFac lagerFac;
	private MandantFac mandantFac;
	private SystemFac systemFac;
	private FehlmengeFac fehlmengeFac;
	
	public VendidataArticleImportBeanHolder(MandantFac mandantFac, FertigungFac fertigungFac, 
			FertigungImportFac fertigungImportFac,ArtikelFac artikelFac, 
			StuecklisteFac stuecklisteFac, LagerFac lagerFac, SystemFac systemFac, FehlmengeFac fehlmengeFac) {
		super();
		this.fertigungFac = fertigungFac;
		this.fertigungImportFac = fertigungImportFac;
		this.artikelFac = artikelFac;
		this.stuecklisteFac = stuecklisteFac;
		this.lagerFac = lagerFac;
		this.mandantFac = mandantFac;
		this.systemFac = systemFac;
		this.fehlmengeFac = fehlmengeFac;
	}

	public FertigungFac getFertigungFac() {
		return fertigungFac;
	}

	public FertigungImportFac getFertigungImportFac() {
		return fertigungImportFac;
	}

	public ArtikelFac getArtikelFac() {
		return artikelFac;
	}

	public StuecklisteFac getStuecklisteFac() {
		return stuecklisteFac;
	}

	public LagerFac getLagerFac() {
		return lagerFac;
	}

	public MandantFac getMandantFac() {
		return mandantFac;
	}

	public SystemFac getSystemFac() {
		return systemFac;
	}

	public FehlmengeFac getFehlmengeFac() {
		return fehlmengeFac;
	}
}
