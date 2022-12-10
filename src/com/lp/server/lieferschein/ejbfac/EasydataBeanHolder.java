package com.lp.server.lieferschein.ejbfac;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;

public class EasydataBeanHolder {

	private ArtikelFac artikelFac;
	private LagerFac lagerFac;
	private PersonalFac personalFac;
	private LieferscheinFac lieferscheinFac;
	private LieferscheinpositionFac lieferscheinPosFac;
	private AnsprechpartnerFac ansprechpartnerFac;
	private MandantFac mandantFac;
	private KundeFac kundeFac;
	private PartnerFac partnerFac;
	private ParameterFac parameterFac;
	
	public EasydataBeanHolder(ArtikelFac artikelFac, LagerFac lagerFac, PersonalFac personalFac,
			LieferscheinFac lieferscheinFac, LieferscheinpositionFac lieferscheinPosFac,
			AnsprechpartnerFac ansprechpartnerFac, MandantFac mandantFac,
			KundeFac kundeFac, PartnerFac partnerFac, ParameterFac parameterFac) {
		super();
		this.artikelFac = artikelFac;
		this.lagerFac = lagerFac;
		this.personalFac = personalFac;
		this.lieferscheinFac = lieferscheinFac;
		this.lieferscheinPosFac = lieferscheinPosFac;
		this.ansprechpartnerFac = ansprechpartnerFac;
		this.mandantFac = mandantFac;
		this.kundeFac = kundeFac;
		this.partnerFac = partnerFac;
		this.parameterFac = parameterFac;
	}

	public ArtikelFac getArtikelFac() {
		return artikelFac;
	}

	public LagerFac getLagerFac() {
		return lagerFac;
	}

	public PersonalFac getPersonalFac() {
		return personalFac;
	}

	public LieferscheinFac getLieferscheinFac() {
		return lieferscheinFac;
	}

	public LieferscheinpositionFac getLieferscheinPosFac() {
		return lieferscheinPosFac;
	}

	public AnsprechpartnerFac getAnsprechpartnerFac() {
		return ansprechpartnerFac;
	}
	
	public MandantFac getMandantFac() {
		return mandantFac;
	}
	
	public KundeFac getKundeFac() {
		return kundeFac;
	}
	
	public PartnerFac getPartnerFac() {
		return partnerFac;
	}
	
	public ParameterFac getParameterFac() {
		return parameterFac;
	}
}
