package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZeileFuerAbrechnungsvorschlagUeberleitungDto implements Serializable {

	public Map<Integer, Map<Integer, DauerUndZeitraumDto>> getGetrenntNachArtikelnAusVerrechnungsmodell() {
		return mGetrenntNachArtikelnAusVerrechnungsmodell;
	}

	private AbrechnungsvorschlagDto avDto = null;

	public AbrechnungsvorschlagDto getAvDto() {
		return avDto;
	}

	
	Map<Integer, Map<Integer, DauerUndZeitraumDto>> mGetrenntNachArtikelnAusVerrechnungsmodell = new LinkedHashMap<Integer, Map<Integer, DauerUndZeitraumDto>>();

	public ZeileFuerAbrechnungsvorschlagUeberleitungDto(AbrechnungsvorschlagDto avDto) {

		this.avDto = avDto;
	}

	public void add2Verrechnen(Integer artikelIId, BigDecimal bdZuVerrechnen, Integer auftragspositionIId,
			Timestamp tVon, Timestamp tBis, Integer zeitdatenIId, boolean bVerdichtetNachArtikel) {

		
		
		
		Map<Integer, DauerUndZeitraumDto> m = null;
		if (mGetrenntNachArtikelnAusVerrechnungsmodell.containsKey(artikelIId)) {
			m = mGetrenntNachArtikelnAusVerrechnungsmodell.get(artikelIId);
		} else {
			m = new LinkedHashMap<Integer, DauerUndZeitraumDto>();
		}

		DauerUndZeitraumDto dauerUndZeitraumDto=null;
		
		if (m.containsKey(auftragspositionIId)) {
			
			dauerUndZeitraumDto=m.get(auftragspositionIId);
		}else {
			dauerUndZeitraumDto=new DauerUndZeitraumDto();
		}
		
		dauerUndZeitraumDto.bVerdichtetNachArtikel=bVerdichtetNachArtikel;
		
		dauerUndZeitraumDto.add2Dauer(bdZuVerrechnen);
		if(tVon!=null && tBis!=null && zeitdatenIId!=null) {
		dauerUndZeitraumDto.add2Zeitraeume(zeitdatenIId,tVon, tBis, bdZuVerrechnen);
		}
		m.put(auftragspositionIId, dauerUndZeitraumDto);
		mGetrenntNachArtikelnAusVerrechnungsmodell.put(artikelIId, m);

		
		

	}

	public BigDecimal getGesamtsumme() {

		BigDecimal bdSumme = BigDecimal.ZERO;

		Map<Integer, Map<Integer, DauerUndZeitraumDto>> mGetrenntNachArtikel = getGetrenntNachArtikelnAusVerrechnungsmodell();

		Iterator itArtikel = mGetrenntNachArtikel.keySet().iterator();

		while (itArtikel.hasNext()) {

			Integer artikelIId = (Integer) itArtikel.next();
			Map<Integer, DauerUndZeitraumDto> mAufpos = mGetrenntNachArtikel.get(artikelIId);

			Iterator itAuftragspositionen = mAufpos.keySet().iterator();

			while (itAuftragspositionen.hasNext()) {

				Integer aufposIId = (Integer) itAuftragspositionen.next();

				
				DauerUndZeitraumDto dz=mAufpos.get(aufposIId);
				
				BigDecimal bdZeile = dz.getBdDauer();

				bdSumme = bdSumme.add(bdZeile);
			}

		}

		return bdSumme;
	}

}
