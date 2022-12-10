package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.util.Helper;

public class LieferantenpreisvergleichDto {

	LinkedHashMap<Integer, LinkedHashMap<BigDecimal, LieferantenpreisvergleichDtoZeile>> lhmReportZeile = new LinkedHashMap<Integer, LinkedHashMap<BigDecimal, LieferantenpreisvergleichDtoZeile>>();

	HashMap<BigDecimal, BigDecimal> guenstigstePreise = new HashMap<BigDecimal, BigDecimal>();
	HashMap<BigDecimal, Integer> schnellsteWbz = new HashMap<BigDecimal, Integer>();

	public void addReportZeile(Integer lieferantIId, BigDecimal bdMengenstaffel, ArtikellieferantDto alDto) {

		LieferantenpreisvergleichDtoZeile zeile = new LieferantenpreisvergleichDtoZeile();

		LinkedHashMap<BigDecimal, LieferantenpreisvergleichDtoZeile> lhmMengenstaffeln = null;

		if (lhmReportZeile.containsKey(lieferantIId)) {
			lhmMengenstaffeln = lhmReportZeile.get(lieferantIId);
		} else {
			lhmMengenstaffeln = new LinkedHashMap<BigDecimal, LieferantenpreisvergleichDtoZeile>();

		}

		if (alDto != null) {
			zeile.setPreis(alDto.getNNettopreis());
			zeile.setWbz(alDto.getIWiederbeschaffungszeit());
			zeile.setbNichtLieferbar(Helper.short2boolean(alDto.getBNichtLieferbar()));

			if (alDto.getNNettopreis() != null) {
				if (guenstigstePreise.containsKey(bdMengenstaffel)) {
					BigDecimal bdPreisVorhanden = guenstigstePreise.get(bdMengenstaffel);

					if (alDto.getNNettopreis().doubleValue() < bdPreisVorhanden.doubleValue()) {
						guenstigstePreise.put(bdMengenstaffel, alDto.getNNettopreis());
					}
				} else {
					guenstigstePreise.put(bdMengenstaffel, alDto.getNNettopreis());
				}
			}

			if (alDto.getIWiederbeschaffungszeit() != null) {
				if (schnellsteWbz.containsKey(bdMengenstaffel)) {
					Integer wbzVorhanden = schnellsteWbz.get(bdMengenstaffel);

					if (alDto.getIWiederbeschaffungszeit() < wbzVorhanden.doubleValue()) {
						schnellsteWbz.put(bdMengenstaffel, alDto.getIWiederbeschaffungszeit());
					}
				} else {
					schnellsteWbz.put(bdMengenstaffel, alDto.getIWiederbeschaffungszeit());
				}
			}
		}

		lhmMengenstaffeln.put(bdMengenstaffel, zeile);
		lhmReportZeile.put(lieferantIId, lhmMengenstaffeln);
	}

	public boolean istAmGuenstigsten(BigDecimal bdMengenstaffel, BigDecimal bdPreis) {
		if (bdPreis != null) {
			if (guenstigstePreise.get(bdMengenstaffel).doubleValue() >= bdPreis.doubleValue()) {
				return true;
			}
		}

		return false;
	}

	public boolean istAmSchnellsten(BigDecimal bdMengenstaffel, Integer iWbz) {
		if (iWbz != null) {
			if (schnellsteWbz.get(bdMengenstaffel) >= iWbz) {
				return true;
			}
		}

		return false;
	}

}
