package com.lp.server.angebotstkl.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.IEkpositionWandlerBeanServices;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.util.Validator;
import com.lp.util.Helper;

public class EkpositionHandartikelWandler {
	
	private IEkpositionWandlerBeanServices beanService;
	
	public EkpositionHandartikelWandler(IEkpositionWandlerBeanServices beanService) {
		this.beanService = beanService;
	}

	public ArtikelDto findeArtikelZuEinkaufsangebotpositionHandeingabe(Integer positionIId) 
			throws RemoteException {
		List<EinkaufsangebotpositionDto> matchingPositionen = getAllEinkaufspositionenMatchingOne(positionIId);
		if (matchingPositionen == null) return null;
		
		Integer artikelIId = getUniqueArtikelIIdFromEinkaufsangebotpositionen(matchingPositionen);
		if (artikelIId == null) return null;
		
		return beanService.artikelFindByPrimaryKey(artikelIId);
	}
	
	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(List<Integer> positionIIds, 
			String artikelCNr) throws RemoteException {
		Map<String, List<EinkaufsangebotpositionDto>> gruppen = bildeGruppenEkPositionen(positionIIds);
		
		if (gruppen.size() > 1) {
			//abbrechen, da nicht alle "gleich"
			return null;
		}
		
		List<List<EinkaufsangebotpositionDto>> gruppe = new ArrayList<List<EinkaufsangebotpositionDto>>(gruppen.values());
		
		return fasseEkpositionenZusammenInArtikel(gruppe.get(0), artikelCNr);
	}
	
	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(Integer positionIId, 
			String artikelCNr) throws RemoteException {
		ArtikelDto artikelDto = beanService.artikelFindByCNr(artikelCNr);
		
		List<EinkaufsangebotpositionDto> matchingPositionen;
		if (artikelDto == null) {
			matchingPositionen = getAllHandeingabeEinkaufspositionenMatchingOne(positionIId);
			if (matchingPositionen.isEmpty()) return null;

			return fasseEkpositionenZusammenInArtikel(matchingPositionen, artikelCNr);
		} else {
			matchingPositionen = getAllEinkaufspositionenMatchingOne(positionIId);
			List<EinkaufsangebotpositionDto> deleteList = new ArrayList<EinkaufsangebotpositionDto>();
			for (EinkaufsangebotpositionDto posDto : matchingPositionen) {
				if (AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT.equals(posDto.getPositionsartCNr()) 
						&& !artikelDto.getIId().equals(posDto.getArtikelIId())) {
					deleteList.add(posDto);
				}
			}
			
			matchingPositionen.removeAll(deleteList);
			if (matchingPositionen.isEmpty()) return null;
			
			return fasseEkpositionenZusammenInArtikel(matchingPositionen, artikelDto.getIId());
		}
		
	}

	/**
	 * Liefert alle Einkaufsangebotpositionen, die nach den Kriterien gleich jener 
	 * mit der &uuml;bergebenen IId ist. Es werden nur jene Positionen &uuml;berpr&uuml;ft,
	 * des selben Einkaufsangebots, wie der &uuml;bergebenen Position.
	 * 
	 * @param positionIId
	 * @return Liste der - nach den Kriterien - gleichen Positionen
	 * @throws RemoteException 
	 */
	private List<EinkaufsangebotpositionDto> getAllEinkaufspositionenMatchingOne(Integer positionIId) throws RemoteException {
		EinkaufsangebotpositionDto positionDto = beanService.einkaufsangebotpositionFindByPrimaryKey(positionIId);
		
		List<Integer> positionIIds = beanService.einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(positionDto.getBelegIId());
		Map<String, List<EinkaufsangebotpositionDto>> gruppen = bildeGruppenEkPositionen(positionIIds);
		
		List<EinkaufsangebotpositionDto> list = gruppen.get(getComparableStringEinkaufsangebotPosition(positionDto));
		Integer index = null;
		for (EinkaufsangebotpositionDto posDto : list) {
			if (posDto.getIId().equals(positionIId)) {
				index = list.indexOf(posDto);
				break;
			}
		}
		if (index == null) return new ArrayList<EinkaufsangebotpositionDto>();
		
		EinkaufsangebotpositionDto tempPosDto = list.get(index);
		list.remove(tempPosDto);
		list.add(0, tempPosDto);
		
		return list;
	}
	
	private List<EinkaufsangebotpositionDto> getAllHandeingabeEinkaufspositionenMatchingOne(Integer positionIId) throws RemoteException {
		List<EinkaufsangebotpositionDto> list = getAllEinkaufspositionenMatchingOne(positionIId);
		List<EinkaufsangebotpositionDto> deleteList = new ArrayList<EinkaufsangebotpositionDto>();
		for (EinkaufsangebotpositionDto posDto : list) {
			if (AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT.equals(posDto.getPositionsartCNr())) {
				deleteList.add(posDto);
			}
		}
		
		list.removeAll(deleteList);
		return list;
	}

	private Integer fasseEkpositionenZusammenInArtikel(List<EinkaufsangebotpositionDto> gruppe, Integer artikelIId) throws RemoteException {
		BigDecimal menge = BigDecimal.ZERO;
		for (EinkaufsangebotpositionDto ekposition : gruppe) {
			menge = menge.add(ekposition.getNMenge());
		}
		Iterator<EinkaufsangebotpositionDto> iter = gruppe.iterator();
		EinkaufsangebotpositionDto newPosition = iter.next();
		newPosition.setNMenge(menge);
		newPosition.setArtikelIId(artikelIId);
		newPosition.setPositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);
		beanService.updateEinkaufsangebotposition(newPosition);
		
		while (iter.hasNext()) {
			beanService.removeEinkaufsangebotposition(iter.next());
		}
		
		return newPosition.getIId();
	}
	
	private Integer fasseEkpositionenZusammenInArtikel(List<EinkaufsangebotpositionDto> gruppe, String artikelCNr) 
			throws RemoteException {
		Validator.notNull(gruppe, "Einkaufsangebotpositionen List");
		Validator.notNull(artikelCNr, "artikelCNr");
		Integer artikelIId = erzeugeArtikelFuerGruppe(gruppe, artikelCNr);
		
		return fasseEkpositionenZusammenInArtikel(gruppe, artikelIId);
	}
	
	private Integer getUniqueArtikelIIdFromEinkaufsangebotpositionen(List<EinkaufsangebotpositionDto> list) {
		Integer artikelIId = null;
		for (EinkaufsangebotpositionDto ekposition : list) {
			if (AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT.equals(ekposition.getPositionsartCNr())) {
				if (artikelIId != null && !artikelIId.equals(ekposition.getArtikelIId())) return null;
				artikelIId = ekposition.getArtikelIId();
			}
		}

		return artikelIId;
	}

	private Integer erzeugeArtikelFuerGruppe(List<EinkaufsangebotpositionDto> list, String artikelCNr) 
			throws RemoteException {
		Integer artikelIId = getUniqueArtikelIIdFromEinkaufsangebotpositionen(list);

		if (artikelIId != null) {
			return artikelIId;
		}
		
		ArtikelDto artikelDto = beanService.artikelFindByCNr(artikelCNr);
		if (artikelDto != null) {
			return artikelDto.getIId();
		}

		EinkaufsangebotpositionDto ekposition = list.get(0);
		artikelDto = new ArtikelDto();
		artikelDto.setCNr(artikelCNr);
		artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		ArtikelsprDto artikelsprDto = new ArtikelsprDto();
		artikelsprDto.setCBez(ekposition.getCBez());
		artikelsprDto.setCZbez(ekposition.getCZusatzbez());
		
		artikelDto.setArtikelsprDto(artikelsprDto);
		artikelDto.setEinheitCNr(ekposition.getEinheitCNr());
		artikelDto.setBVersteckt(Helper.boolean2Short(false));
		artikelDto.setHerstellerIId(ekposition.getHerstellerIId());
		artikelDto.setCArtikelnrhersteller(ekposition.getCArtikelnrhersteller());
		artikelDto.setCArtikelbezhersteller(ekposition.getCArtikelbezhersteller());
		
		artikelIId = beanService.createArtikel(artikelDto);
		
		if (ekposition.getLieferantIId() != null) {
			createArtikellieferantUndStaffelAusEkposition(ekposition.getIId(), artikelIId);
		}

		return artikelIId;
	}
	
	private void createArtikellieferantUndStaffelAusEkposition(Integer ekpositionIId, 
			Integer artikelIId) throws RemoteException {
		EinkaufsangebotpositionDto positionDto = beanService.einkaufsangebotpositionFindByPrimaryKey(ekpositionIId);
		EinkaufsangebotDto einkaufsangebotDto = beanService.einkaufsangebotFindByPrimaryKey(positionDto.getBelegIId());
		
		Map<BigDecimal, BigDecimal> mengenPreise = new HashMap<BigDecimal, BigDecimal>();
		mengenPreise.put(einkaufsangebotDto.getNMenge1(), positionDto.getNPreis1());
		mengenPreise.put(einkaufsangebotDto.getNMenge2(), positionDto.getNPreis2());
		mengenPreise.put(einkaufsangebotDto.getNMenge3(), positionDto.getNPreis3());
		mengenPreise.put(einkaufsangebotDto.getNMenge4(), positionDto.getNPreis4());
		mengenPreise.put(einkaufsangebotDto.getNMenge5(), positionDto.getNPreis5());
		
		List<BigDecimal> sortedMengen = new ArrayList<BigDecimal>(mengenPreise.keySet());
		
		Collections.sort(sortedMengen);
		List<BigDecimal> zeros = new ArrayList<BigDecimal>();
		for (BigDecimal menge : sortedMengen) {
			if (BigDecimal.ZERO.compareTo(menge) == 0 || mengenPreise.get(menge) == null 
					|| BigDecimal.ZERO.compareTo(mengenPreise.get(menge)) == 0) {
				zeros.add(menge);
			}
		}
		sortedMengen.removeAll(zeros);
		if (sortedMengen.isEmpty()) return;
		
		ArtikellieferantDto artikellieferantDto = new ArtikellieferantDto();
		artikellieferantDto.setArtikelIId(artikelIId);
		artikellieferantDto.setLieferantIId(positionDto.getLieferantIId());
		artikellieferantDto.setTPreisgueltigab(new Timestamp(System.currentTimeMillis()));
		artikellieferantDto.setBRabattbehalten(Helper.getShortFalse());
		artikellieferantDto.setNEinzelpreis(mengenPreise.get(sortedMengen.get(0)));
		
		Integer artikellieferantIId = beanService.createArtikellieferant(artikellieferantDto);

		for (BigDecimal menge : sortedMengen) {
			BigDecimal staffelpreis = mengenPreise.get(menge);
			
			ArtikellieferantstaffelDto artLiefStaffelDto = new ArtikellieferantstaffelDto();
			artLiefStaffelDto.setArtikellieferantIId(artikellieferantIId);
			artLiefStaffelDto.setNMenge(menge);
			artLiefStaffelDto.setNNettopreis(staffelpreis);
			BigDecimal bRabatt = BigDecimal.ONE.subtract(staffelpreis.divide(artikellieferantDto.getNEinzelpreis(), 4, RoundingMode.HALF_UP)); 
			artLiefStaffelDto.setFRabatt(bRabatt.multiply(new BigDecimal(100)).doubleValue());
			artLiefStaffelDto.setTPreisgueltigab(new Timestamp(System.currentTimeMillis()));
			artLiefStaffelDto.setBRabattbehalten(Helper.getShortFalse());
			
			beanService.createArtikellieferantstaffel(artLiefStaffelDto);
		}
		
	}

	/**
	 * Suche die Positionen in der DB und gruppiere sie nach den 
	 * Regeln &uuml;ber einen Vergleichstring als Key einer HashMap
	 * 
	 * @param positionIIds
	 * @return Map der Einkaufsangebotpositionen
	 */
	private Map<String, List<EinkaufsangebotpositionDto>> bildeGruppenEkPositionen(List<Integer> positionIIds) {
		List<EinkaufsangebotpositionDto> ekpositionen = beanService.einkaufsangebotpositionenFindByPrimaryKeys(
				positionIIds.toArray(new Integer[positionIIds.size()]));
		Map<String, List<EinkaufsangebotpositionDto>> map = new HashMap<String, List<EinkaufsangebotpositionDto>>();
		
		for (EinkaufsangebotpositionDto dto : ekpositionen) {
			String comparableString = getComparableStringEinkaufsangebotPosition(dto);
			List<EinkaufsangebotpositionDto> group = map.get(comparableString);
			if (group == null) {
				group = new ArrayList<EinkaufsangebotpositionDto>();
			}
			group.add(dto);
			map.put(comparableString, group);
		}
		
		return map;
	}

	/**
	 * Erstellt den String der f&uuml;r den Vergleich zwischen den einzelnen Handartikeln
	 * vwerwendet wird
	 * @param dto Einkaufsangebotposition
	 * @return der zu vergleichende String
	 */
	private String getComparableStringEinkaufsangebotPosition(EinkaufsangebotpositionDto dto) {
		StringBuilder builder = new StringBuilder();
		builder.append(dto.getCBez() != null ? dto.getCBez().trim() : null);
		builder.append(",");
//		builder.append(dto.getHerstellerIId());
//		builder.append(",");
		builder.append(dto.getCArtikelnrhersteller());
		builder.append(",");
		builder.append(dto.getLieferantIId());

		return builder.toString();
	}

}
