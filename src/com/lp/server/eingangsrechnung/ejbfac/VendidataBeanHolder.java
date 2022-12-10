package com.lp.server.eingangsrechnung.ejbfac;

import com.lp.server.benutzer.ejbfac.BenutzerServicesFacLocal;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;

public class VendidataBeanHolder {

	private FinanzFac finanzFac;
	private KundeFac kundeFac;
	private LieferantFac lieferantFac;
	private EingangsrechnungFac eingangsrechnungFac;
	private MandantFac mandantFac;
	private BenutzerServicesFacLocal benutzerServicesFac;
	private RechnungFac rechnungFac;
	private BuchenFac buchenFac;
	private ParameterFac parameterFac;

	public VendidataBeanHolder() {
	}

	public VendidataBeanHolder(FinanzFac finanzFac, KundeFac kundeFac,
			LieferantFac lieferantFac, EingangsrechnungFac eingangsrechnungFac,
			MandantFac mandantFac, BenutzerServicesFacLocal benutzerServicesFac,
			RechnungFac rechnungFac, BuchenFac buchenFac, ParameterFac parameterFac) {
		super();
		this.finanzFac = finanzFac;
		this.kundeFac = kundeFac;
		this.lieferantFac = lieferantFac;
		this.eingangsrechnungFac = eingangsrechnungFac;
		this.mandantFac = mandantFac;
		this.benutzerServicesFac = benutzerServicesFac;
		this.rechnungFac = rechnungFac;
		this.buchenFac = buchenFac;
		this.parameterFac = parameterFac;
	}

	public FinanzFac getFinanzFac() {
		return finanzFac;
	}

	public void setFinanzFac(FinanzFac finanzFac) {
		this.finanzFac = finanzFac;
	}

	public KundeFac getKundeFac() {
		return kundeFac;
	}

	public void setKundeFac(KundeFac kundeFac) {
		this.kundeFac = kundeFac;
	}

	public LieferantFac getLieferantFac() {
		return lieferantFac;
	}

	public void setLieferantFac(LieferantFac lieferantFac) {
		this.lieferantFac = lieferantFac;
	}

	public EingangsrechnungFac getEingangsrechnungFac() {
		return eingangsrechnungFac;
	}

	public void setEingangsrechnungFac(EingangsrechnungFac eingangsrechnungFac) {
		this.eingangsrechnungFac = eingangsrechnungFac;
	}

	public MandantFac getMandantFac() {
		return mandantFac;
	}

	public void setMandantFac(MandantFac mandantFac) {
		this.mandantFac = mandantFac;
	}

	public BenutzerServicesFacLocal getBenutzerServicesFac() {
		return benutzerServicesFac;
	}

	public void setBenutzerServicesFac(BenutzerServicesFacLocal benutzerServicesFac) {
		this.benutzerServicesFac = benutzerServicesFac;
	}

	public RechnungFac getRechnungFac() {
		return rechnungFac;
	}
	
	public void setRechnungFac(RechnungFac rechnungFac) {
		this.rechnungFac = rechnungFac;
	}
	
	public BuchenFac getBuchenFac() {
		return buchenFac;
	}
	
	public void setBuchenFac(BuchenFac buchenFac) {
		this.buchenFac = buchenFac;
	}
	
	public ParameterFac getParameterFac() {
		return parameterFac;
	}
	
	public void setParameterFac(ParameterFac parameterFac) {
		this.parameterFac = parameterFac;
	}
}
