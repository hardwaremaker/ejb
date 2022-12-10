package com.lp.server.fertigung.ejbfac;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;

public class VerbrauchsartikelImporterBeanHolder {

	private FertigungFac fertigungFac;
	private ArtikelFac artikelFac;
	private StuecklisteFac stuecklisteFac;
	private LagerFac lagerFac;
	private MandantFac mandantFac;
	private SystemFac systemFac;
	private ParameterFac parameterFac;

	public VerbrauchsartikelImporterBeanHolder(MandantFac mandantFac, FertigungFac fertigungFac, 
			ArtikelFac artikelFac, StuecklisteFac stuecklisteFac, 
			LagerFac lagerFac, SystemFac systemFac, ParameterFac parameterFac) {
		super();
		this.fertigungFac = fertigungFac;
		this.artikelFac = artikelFac;
		this.stuecklisteFac = stuecklisteFac;
		this.lagerFac = lagerFac;
		this.mandantFac = mandantFac;
		this.systemFac = systemFac;
		this.parameterFac = parameterFac;
	}
	
	public FertigungFac getFertigungFac() {
		return fertigungFac;
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
	
	public ParameterFac getParameterFac() {
		return parameterFac;
	}
}
