package com.lp.server.angebot.ejbfac;

import java.math.BigDecimal;

import com.lp.server.angebot.ejb.Angebotposition;
import com.lp.server.util.IArtikelsetPreisUpdate;
import com.lp.server.util.IBelegVerkaufEntity;

public class AngebotpositionArtikelsetPreisUpdate implements IArtikelsetPreisUpdate {
	@Override
	public void initializePreis(IBelegVerkaufEntity positionEntity) {
		Angebotposition apos = (Angebotposition) positionEntity;
		apos.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
		apos.setNBruttogesamtpreis(BigDecimal.ZERO);
	}

	@Override
	public void setPreis(IBelegVerkaufEntity positionEntity, BigDecimal nettoPrice) {
		Angebotposition apos = (Angebotposition) positionEntity;
		apos.setNNettoeinzelpreisplusversteckteraufschlag(nettoPrice);
		apos.setNBruttogesamtpreis(nettoPrice);
	}

	@Override
	public void addPreis(IBelegVerkaufEntity positionEntity, BigDecimal addNettoPrice) {
		Angebotposition apos = (Angebotposition) positionEntity;
		apos.setNNettoeinzelpreisplusversteckteraufschlag(
				apos.getNNettoeinzelpreisplusversteckteraufschlag()
					.add(addNettoPrice));
		apos.setNBruttogesamtpreis(
				apos.getNBruttogesamtpreis().add(addNettoPrice));
	}
}
