/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejbfac.AngebotstklpositionLocalFac;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.instandhaltung.service.WartungsschritteDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.personal.ejb.Personalgruppe;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.BelegpositionkonvertierungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.KundeId;
import com.lp.server.util.Validator;
import com.lp.service.BelegpositionDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um die Konvertierung von Belegpositionen
 * </p>
 * <p>
 * Hier wird "echtes" Klonen unterstuetzt, um Side-effects zu verhindern
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Christian Kollmann; 07.02.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/10/03 09:58:28 $
 */
@Stateless
public class BelegpositionkonvertierungFacBean extends Facade implements BelegpositionkonvertierungFac {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private AngebotstklpositionLocalFac angebotstklPositionLocalFac;

	public AgstklpositionDto[] konvertiereNachAgstklpositionDto(BelegpositionDto[] belegpositionDto, Integer agstklIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<AgstklpositionDto> a = new java.util.ArrayList<AgstklpositionDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {

			if (belegpositionDto[i] instanceof StuecklistearbeitsplanDto) {
				continue;
			}
			if (belegpositionDto[i] instanceof AgstklarbeitsplanDto) {
				continue;
			}

			AgstklpositionDto zielDto = new AgstklpositionDto();

			if (belegpositionDto[i].getPositionsartCNr()
					.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_HANDEINGABE)
					|| belegpositionDto[i].getPositionsartCNr()
							.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_IDENT)) {

				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				// Defaultwerte setzen
				if (belegpositionDto[i].getPositionsartCNr()
						.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_IDENT)) {
					if (zielDto.getNGestehungspreis() == null) {
						BigDecimal gestPreis = ermittleGestehungsPreis(zielDto.getArtikelIId(), theClientDto);
						zielDto.setNGestehungspreis(gestPreis);
						zielDto.setFRabattsatz(0.0);
						zielDto.setNNettoeinzelpreis(gestPreis);
						zielDto.setNNettogesamtpreis(gestPreis);
					}
				} else if (belegpositionDto[i].getPositionsartCNr()
						.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_HANDEINGABE)) {
					zielDto.setNGestehungspreis(BigDecimal.ZERO);
					zielDto.setFRabattsatz(0.0);
					zielDto.setNNettoeinzelpreis(BigDecimal.ZERO);
					zielDto.setNNettogesamtpreis(BigDecimal.ZERO);
				}

				if (belegpositionDto[i].getPositionsartCNr()
						.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_HANDEINGABE)) {

				}

				zielDto.setBDrucken(com.lp.util.Helper.boolean2Short(false));
				zielDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));

				if (belegpositionDto instanceof AgstklpositionDto[]) {
					AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
					zielDto.setFRabattsatz(Helper.cloneDouble(quellDto.getFRabattsatz()));
					zielDto.setFZusatzrabattsatz(Helper.cloneDouble(quellDto.getFZusatzrabattsatz()));
					zielDto.setNGestehungspreis(Helper.cloneBigDecimal(quellDto.getNGestehungspreis()));
					zielDto.setNNettoeinzelpreis(Helper.cloneBigDecimal(quellDto.getNNettoeinzelpreis()));
					zielDto.setNNettogesamtpreis(Helper.cloneBigDecimal(quellDto.getNNettogesamtpreis()));
					zielDto.setBDrucken(quellDto.getBDrucken());
					zielDto.setCPosition(quellDto.getCPosition());
				} else if (belegpositionDto instanceof AnfragepositionDto[]) {
					AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
					zielDto.setNNettogesamtpreis(quellDto.getNRichtpreis());
					zielDto.setNNettoeinzelpreis(new BigDecimal(0));
					zielDto.setNNettogesamtpreis(new BigDecimal(0));
					zielDto.setFRabattsatz(0.0);

					try {
						LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

						BigDecimal preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
								belegpositionDto[i].getArtikelIId(), lagerDto.getIId(), theClientDto);
						zielDto.setNGestehungspreis(preis);

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
					StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
					zielDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
					if (quellDto.getArtikelDto() != null) {
						if (quellDto.getArtikelDto().getArtikelsprDto() != null) {
							zielDto.setCBez(quellDto.getArtikelDto().getArtikelsprDto().getCBez());
						}
						zielDto.setEinheitCNr(quellDto.getArtikelDto().getEinheitCNr());
					}

					zielDto.setBDrucken(quellDto.getBMitdrucken());
					zielDto.setCPosition(quellDto.getCPosition());
					zielDto.setFRabattsatz(0.0);
					zielDto.setNNettoeinzelpreis(new BigDecimal(0));
					zielDto.setNNettogesamtpreis(new BigDecimal(0));
					try {
						LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

						BigDecimal preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
								belegpositionDto[i].getArtikelIId(), lagerDto.getIId(), theClientDto);
						zielDto.setNGestehungspreis(preis);

						// SP2065

						zielDto.setNMenge(Helper.rundeKaufmaennisch(
								getStuecklisteFac().berechneZielmenge(quellDto.getIId(), theClientDto), 4));

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				} else if (belegpositionDto instanceof BestellpositionDto[]) {
					BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
					zielDto.setBDrucken(quellDto.getBDrucken());

					zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
					zielDto.setNNettogesamtpreis(quellDto.getNNettogesamtpreis());
					zielDto.setFRabattsatz(quellDto.getDRabattsatz());

					try {
						LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

						BigDecimal preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
								belegpositionDto[i].getArtikelIId(), lagerDto.getIId(), theClientDto);
						zielDto.setNGestehungspreis(preis);

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				} else if (belegpositionDto instanceof EinkaufsangebotpositionDto[]) {
					EinkaufsangebotpositionDto quellDto = (EinkaufsangebotpositionDto) belegpositionDto[i];

					zielDto.setNNettoeinzelpreis(new BigDecimal(0));
					zielDto.setNNettogesamtpreis(new BigDecimal(0));
					zielDto.setFRabattsatz(new Double(0));

				}

				try {
					zielDto.setAgstklIId(agstklIId);
					zielDto = angebotstklPositionLocalFac.befuelleMitPreisenNachKalkulationsart(zielDto, theClientDto);
					// SP2064
					// ParametermandantDto parameterMand = getParameterFac()
					// .getMandantparameter(theClientDto.getMandant(),
					// ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					// ParameterFac.PARAMETER_KALKULATIONSART);
					// int iKalkulationsart = (Integer) parameterMand
					// .getCWertAsObject();
					// // Aufschlag
					// if (iKalkulationsart == 3
					// && zielDto.getArtikelIId() != null) {
					//
					// // Lief1Preis
					// zielDto = angebotstklPositionLocalFac
					// .befuellePositionMitPreisenKalkulationsart2(
					// theClientDto,
					// agstklDto.getWaehrungCNr(),
					// zielDto.getArtikelIId(),
					// zielDto.getNMenge(), zielDto);
					//
					// parameterMand = getParameterFac().getMandantparameter(
					// theClientDto.getMandant(),
					// ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
					// ParameterFac.PARAMETER_DEFAULT_AUFSCHLAG);
					// Double aufschlag = (Double) parameterMand
					// .getCWertAsObject();
					// zielDto.setFAufschlag(aufschlag);
					//
					// zielDto.setBAufschlaggesamtFixiert(Helper
					// .boolean2Short(false));
					//
					// BigDecimal bdAufschlag = Helper.getProzentWert(zielDto
					// .getNNettogesamtpreis(), new BigDecimal(
					// aufschlag), 4);
					//
					// zielDto.setNAufschlag(bdAufschlag);
					//
					// zielDto.setNNettogesamtmitaufschlag(zielDto
					// .getNNettogesamtpreis().add(bdAufschlag));
					//
					// }
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				a.add(zielDto);
			}
		}

		AgstklpositionDto[] returnArray = new AgstklpositionDto[a.size()];
		return (AgstklpositionDto[]) a.toArray(returnArray);
	}

	public StuecklistepositionDto[] konvertiereNachStklpositionDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<StuecklistepositionDto> a = new java.util.ArrayList<StuecklistepositionDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			StuecklistepositionDto zielDto = new StuecklistepositionDto();
			
			
			if (belegpositionDto instanceof StuecklistearbeitsplanDto[]) {
				StuecklistearbeitsplanDto quellDto = (StuecklistearbeitsplanDto) belegpositionDto[i];
				belegpositionDto[i].setPositionsartCNr(com.lp.server.system.service.LocaleFac.POSITIONSART_IDENT);
				belegpositionDto[i].setNMenge(BigDecimal.ONE);
				
				ArtikelDto aDto=getArtikelFac().artikelFindByPrimaryKeySmall(belegpositionDto[i].getArtikelIId(), theClientDto);
				belegpositionDto[i].setEinheitCNr(aDto.getEinheitCNr());
			}
			
			// ALLGMEINE FELDER
			if (belegpositionDto[i].getPositionsartCNr()
					.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_HANDEINGABE)
					|| belegpositionDto[i].getPositionsartCNr()
							.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_IDENT)) {

				zielDto = new StuecklistepositionDto();
				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				zielDto.setBMitdrucken(Helper.boolean2Short(false));
				zielDto.setSHandeingabe(belegpositionDto[i].getCBez());

				MontageartDto[] dtos = null;
				try {
					dtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

				if (dtos != null && dtos.length > 0) {
					zielDto.setMontageartIId(dtos[0].getIId());
				} else {
					return null;
				}

				if (belegpositionDto instanceof StuecklistepositionDto[]) {
					StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
					/**
					 * ? ArtikelDto kopieren wenn handeingabe?
					 */
					zielDto.setArtikelDto(quellDto.getArtikelDto());

					zielDto.setCKommentar(quellDto.getCKommentar());
					zielDto.setCPosition(quellDto.getCPosition());
					zielDto.setFDimension1(quellDto.getFDimension1());
					zielDto.setFDimension2(quellDto.getFDimension2());
					zielDto.setFDimension3(quellDto.getFDimension3());
					zielDto.setILfdnummer(quellDto.getILfdnummer());
					zielDto.setNKalkpreis(quellDto.getNKalkpreis());
					zielDto.setNZielmenge(quellDto.getNZielmenge());
					zielDto.setSHandeingabe(quellDto.getSHandeingabe());
					zielDto.setStuecklisteIId(quellDto.getStuecklisteIId());
					zielDto.setBRuestmenge(quellDto.getBRuestmenge());
					zielDto.setXFormel(quellDto.getXFormel());
				}

				if (belegpositionDto instanceof AgstklpositionDto[]) {
					AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
					zielDto.setBMitdrucken(quellDto.getBDrucken());
					zielDto.setCPosition(quellDto.getCPosition());
				} else if (belegpositionDto instanceof AnfragepositionDto[]) {
					AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				} else if (belegpositionDto instanceof BestellpositionDto[]) {
					BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
					zielDto.setBMitdrucken(quellDto.getBDrucken());
				} else if (belegpositionDto instanceof EinkaufsangebotpositionDto[]) {
					EinkaufsangebotpositionDto quellDto = (EinkaufsangebotpositionDto) belegpositionDto[i];
					zielDto.setCKommentar(quellDto.getCBemerkung());
					zielDto.setCPosition(quellDto.getCPosition());
					zielDto.setILfdnummer(quellDto.getILfdnummer());
				} else if (belegpositionDto instanceof LossollmaterialDto[]) {
					LossollmaterialDto quellDto = (LossollmaterialDto) belegpositionDto[i];
					int iLosIId = quellDto.getLosIId();
					LosDto losDto = null;
					switch (quellDto.getiCopyPasteModus()) {
					case BelegpositionDto.COPY_PASTE_MODUS_IST_PREIS_AUS_LOS: {
						// IstPreis u IstMenge setzen
						try {
							BigDecimal bdIstmenge = getFertigungFac().getAusgegebeneMenge(quellDto.getIId(), null,
									theClientDto);
							BigDecimal bdIstPreis = getFertigungFac().getAusgegebeneMengePreis(quellDto.getIId(), null,
									theClientDto);
							zielDto.setNMenge(bdIstmenge);
							zielDto.setNKalkpreis(bdIstPreis);
						} catch (RemoteException ex) {
							throwEJBExceptionLPRespectOld(ex);
						}
					}
						break;
					case BelegpositionDto.COPY_PASTE_MODUS_NORMAL: {
						// SollPreis setzen
						zielDto.setNKalkpreis(quellDto.getNSollpreis());
					}
						break;

					}
				}

				a.add(zielDto);
			}

		}

		StuecklistepositionDto[] returnArray = new StuecklistepositionDto[a.size()];
		return (StuecklistepositionDto[]) a.toArray(returnArray);

	}

	public StuecklistearbeitsplanDto[] konvertiereNachStklarbeitsplanDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<StuecklistearbeitsplanDto> a = new java.util.ArrayList<StuecklistearbeitsplanDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			StuecklistearbeitsplanDto zielDto = new StuecklistearbeitsplanDto();

			if (belegpositionDto instanceof StuecklistepositionDto[]) {

				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				zielDto.setStuecklisteIId(null);
				zielDto.setStuecklistepositionIId(null);
				zielDto.setArtikelIId(quellDto.getArtikelIId());
				zielDto.setLStueckzeit(0L);
				zielDto.setLRuestzeit(0L);
				zielDto.setIArbeitsgang(0);
				zielDto.setBNurmaschinenzeit(Helper.boolean2Short(false));
				

			}

			if (belegpositionDto instanceof StuecklistearbeitsplanDto[]) {
				zielDto = (StuecklistearbeitsplanDto) belegpositionDto[i];
				zielDto.setStuecklisteIId(null);
				zielDto.setStuecklistepositionIId(null);
			}

			if (belegpositionDto instanceof LossollarbeitsplanDto[]) {
				LossollarbeitsplanDto quellDto = (LossollarbeitsplanDto) belegpositionDto[i];
				zielDto.setIArbeitsgang(quellDto.getIArbeitsgangnummer());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setNMenge(quellDto.getNMenge());
				zielDto.setArtikelIId(quellDto.getArtikelIIdTaetigkeit());
				zielDto.setEinheitCNr(quellDto.getEinheitCNr());
				zielDto.setIAufspannung(quellDto.getIAufspannung());
				zielDto.setIMaschinenversatztage(quellDto.getIMaschinenversatztage());
				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setAgartCNr(quellDto.getAgartCNr());
				zielDto.setCKommentar(quellDto.getCKomentar());
				zielDto.setXLangtext(quellDto.getXText());
			}

			if (belegpositionDto instanceof AgstklarbeitsplanDto[]) {
				AgstklarbeitsplanDto quellDto = (AgstklarbeitsplanDto) belegpositionDto[i];
				zielDto.setIArbeitsgang(quellDto.getIArbeitsgang());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setBInitial(quellDto.getBInitial());
				zielDto.setNMenge(quellDto.getNMenge());
				zielDto.setArtikelIId(quellDto.getArtikelIId());
				zielDto.setEinheitCNr(quellDto.getEinheitCNr());
				zielDto.setIAufspannung(quellDto.getIAufspannung());

				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setAgartCNr(quellDto.getAgartCNr());
				zielDto.setCKommentar(quellDto.getCKommentar());
				zielDto.setXLangtext(quellDto.getXLangtext());
			}

			a.add(zielDto);

		}

		StuecklistearbeitsplanDto[] returnArray = new StuecklistearbeitsplanDto[a.size()];
		return (StuecklistearbeitsplanDto[]) a.toArray(returnArray);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lp.server.system.service.BelegpositionkonvertierungFac#
	 * konvertiereNachAgstklarbeitsplanDto(com.lp.service.BelegpositionDto[],
	 * com.lp.server.system.service.TheClientDto)
	 */
	public AgstklarbeitsplanDto[] konvertiereNachAgstklarbeitsplanDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<AgstklarbeitsplanDto> a = new java.util.ArrayList<AgstklarbeitsplanDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			AgstklarbeitsplanDto zielDto = new AgstklarbeitsplanDto();

			if (belegpositionDto instanceof AgstklarbeitsplanDto[]) {
				zielDto = (AgstklarbeitsplanDto) belegpositionDto[i];
				zielDto.setAgstklIId(null);
			}

			if (belegpositionDto instanceof LossollarbeitsplanDto[]) {
				LossollarbeitsplanDto quellDto = (LossollarbeitsplanDto) belegpositionDto[i];
				zielDto.setIArbeitsgang(quellDto.getIArbeitsgangnummer());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setNMenge(quellDto.getNMenge());
				zielDto.setArtikelIId(quellDto.getArtikelIIdTaetigkeit());
				zielDto.setEinheitCNr(quellDto.getEinheitCNr());
				zielDto.setIAufspannung(quellDto.getIAufspannung());

				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setAgartCNr(quellDto.getAgartCNr());
				zielDto.setCKommentar(quellDto.getCKomentar());
				zielDto.setXLangtext(quellDto.getXText());
			}
			if (belegpositionDto instanceof StuecklistearbeitsplanDto[]) {
				StuecklistearbeitsplanDto quellDto = (StuecklistearbeitsplanDto) belegpositionDto[i];
				zielDto.setIArbeitsgang(quellDto.getIArbeitsgang());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setBInitial(quellDto.getBInitial());
				zielDto.setNMenge(quellDto.getNMenge());
				zielDto.setArtikelIId(quellDto.getArtikelIId());
				zielDto.setEinheitCNr(quellDto.getEinheitCNr());
				zielDto.setIAufspannung(quellDto.getIAufspannung());

				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setAgartCNr(quellDto.getAgartCNr());
				zielDto.setCKommentar(quellDto.getCKommentar());
				zielDto.setXLangtext(quellDto.getXLangtext());
			}

			a.add(zielDto);

		}

		AgstklarbeitsplanDto[] returnArray = new AgstklarbeitsplanDto[a.size()];
		return (AgstklarbeitsplanDto[]) a.toArray(returnArray);

	}

	public EinkaufsangebotpositionDto[] konvertiereNachEinkaufsangebotpositionDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<EinkaufsangebotpositionDto> a = new java.util.ArrayList<EinkaufsangebotpositionDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			EinkaufsangebotpositionDto zielDto = new EinkaufsangebotpositionDto();
			// ALLGMEINE FELDER
			if (belegpositionDto[i].getPositionsartCNr()
					.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_HANDEINGABE)
					|| belegpositionDto[i].getPositionsartCNr()
							.equals(com.lp.server.system.service.LocaleFac.POSITIONSART_IDENT)) {

				if (belegpositionDto instanceof EinkaufsangebotpositionDto[]) {
					zielDto = (EinkaufsangebotpositionDto) belegpositionDto[i];
				} else {
					zielDto = new EinkaufsangebotpositionDto();
					cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

					if (belegpositionDto instanceof StuecklistepositionDto[]) {

						StuecklistepositionDto dto = (StuecklistepositionDto) belegpositionDto[i];
						if (dto.getArtikelDto() != null && dto.getArtikelDto().getArtikelsprDto() != null) {
							zielDto.setCBez(dto.getArtikelDto().getArtikelsprDto().getCBez());
							zielDto.setCZusatzbez(dto.getArtikelDto().getArtikelsprDto().getCZbez());
						}
						zielDto.setCPosition(dto.getCPosition());
						zielDto.setCBemerkung(dto.getCKommentar());
						zielDto.setILfdnummer(dto.getILfdnummer());

					} else {
						zielDto.setCBez(belegpositionDto[i].getCBez());
						zielDto.setCZusatzbez(belegpositionDto[i].getCZusatzbez());

					}
					zielDto.setBMitdrucken(Helper.boolean2Short(false));
					zielDto.setNMenge(belegpositionDto[i].getNMenge());
				}

				a.add(zielDto);
			}

		}

		EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[a.size()];
		return (EinkaufsangebotpositionDto[]) a.toArray(returnArray);

	}

	public BestellvorschlagDto[] konvertiereNachBestellvorschlagDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ArrayList<BestellvorschlagDto> al = new ArrayList<BestellvorschlagDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			BestellvorschlagDto zielDto = new BestellvorschlagDto();
			zielDto.setTLiefertermin(new Timestamp(System.currentTimeMillis()));
			zielDto.setCMandantCNr(theClientDto.getMandant());
			zielDto.setIArtikelId(belegpositionDto[i].getArtikelIId());
			zielDto.setNZubestellendeMenge(belegpositionDto[i].getNMenge());
			zielDto.setXTextinhalt(belegpositionDto[i].getXTextinhalt());
			if (belegpositionDto[i] instanceof AuftragpositionDto) {
				zielDto.setILieferantId(((AuftragpositionDto) belegpositionDto[i]).getLieferantIId());
				BigDecimal ekPreis = ((AuftragpositionDto) belegpositionDto[i]).getBdEinkaufpreis();
				if (ekPreis != null) {
					zielDto.setDRabattsatz(0D);
					zielDto.setNNettoeinzelpreis(ekPreis);
					zielDto.setNNettogesamtpreis(ekPreis);
					zielDto.setNRabattbetrag(BigDecimal.ZERO);
				}

			}
			if (belegpositionDto[i] instanceof AngebotpositionDto) {
				zielDto.setILieferantId(((AngebotpositionDto) belegpositionDto[i]).getLieferantIId());
				BigDecimal ekPreis = ((AngebotpositionDto) belegpositionDto[i]).getNEinkaufpreis();
				if (ekPreis != null) {
					zielDto.setDRabattsatz(0D);
					zielDto.setNNettoeinzelpreis(ekPreis);
					zielDto.setNNettogesamtpreis(ekPreis);
					zielDto.setNRabattbetrag(BigDecimal.ZERO);
				}
			}

			if (belegpositionDto[i] instanceof BestellpositionDto) {
				BestellpositionDto besPosDto = (BestellpositionDto) belegpositionDto[i];
				zielDto.setCBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
				zielDto.setDRabattsatz(besPosDto.getDRabattsatz());
				zielDto.setNNettoeinzelpreis(besPosDto.getNNettoeinzelpreis());
				zielDto.setBNettopreisuebersteuert(besPosDto.getBNettopreisuebersteuert());
				zielDto.setGebindeIId(besPosDto.getGebindeIId());
				zielDto.setNAnzahlgebinde(besPosDto.getNAnzahlgebinde());

				// PJ18006
				BigDecimal nMaterialzuschlag = besPosDto.getNMaterialzuschlag();

				if (nMaterialzuschlag != null && besPosDto.getNNettogesamtpreis() != null) {
					zielDto.setNNettogesamtpreis(besPosDto.getNNettogesamtpreis().subtract(nMaterialzuschlag));
				} else {
					zielDto.setNNettogesamtpreis(besPosDto.getNNettogesamtpreis());
				}

				if (nMaterialzuschlag != null && besPosDto.getNNettogesamtPreisminusRabatte() != null) {
					zielDto.setNNettoGesamtPreisMinusRabatte(
							besPosDto.getNNettogesamtPreisminusRabatte().subtract(nMaterialzuschlag));
				} else {
					zielDto.setNNettoGesamtPreisMinusRabatte(besPosDto.getNNettogesamtPreisminusRabatte());
				}

				zielDto.setNRabattbetrag(besPosDto.getNRabattbetrag());

				if (besPosDto.getLieferantIIdWennCopyInBestellvorschlag() != null) {
					zielDto.setILieferantId(besPosDto.getLieferantIIdWennCopyInBestellvorschlag());
				} else {
					if (besPosDto.getBelegIId() != null) {
						BestellungDto bestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(besPosDto.getBelegIId());
						zielDto.setILieferantId(bestellungDto.getLieferantIIdBestelladresse());
					}
				}

			}
			al.add(zielDto);

		}
		BestellvorschlagDto[] toReturn = new BestellvorschlagDto[al.size()];
		return al.toArray(toReturn);
	}

	// copypaste: 7 Hier wird die Verknuepfung der einzelnen Positionen
	// festgelegt
	public BestellpositionDto[] konvertiereNachBestellpositionDto(BestellungDto bestellungDto,
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<BestellpositionDto> a = new java.util.ArrayList<BestellpositionDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			// ALLGMEINE FELDER

			BigDecimal ekPreis = null;

			BestellpositionDto zielDto = new BestellpositionDto();
			cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);
			zielDto.setBDrucken(Helper.boolean2Short(true));

			zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

			zielDto.setNNettoeinzelpreis(new BigDecimal(0));
			zielDto.setNNettogesamtpreis(new BigDecimal(0));
			zielDto.setNMaterialzuschlag(new BigDecimal(0));
			zielDto.setNRabattbetrag(new BigDecimal(0));
			zielDto.setNNettogesamtPreisminusRabatte(new BigDecimal(0));
			zielDto.setDRabattsatz(new Double(0));

			// Status einer eingefuegten Position immer auf offen zuruecksetzen
			zielDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);

			// NOT NULL Felder pruefen und noetigenfalls mit default-werten
			// belegen
			if (zielDto.getBArtikelbezeichnunguebersteuert() == null) {
				zielDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
			}
			if (zielDto.getBMwstsatzUebersteuert() == null) {
				zielDto.setBMwstsatzUebersteuert(Helper.boolean2Short(false));
			}

			if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
				zielDto.setNMaterialzuschlag(quellDto.getNMaterialzuschlag());
				zielDto.setNNettogesamtpreis(quellDto.getNNettogesamtpreis());
				zielDto.setNRabattbetrag(quellDto.getNRabattbetrag());
				zielDto.setNNettogesamtPreisminusRabatte(quellDto.getNNettogesamtPreisminusRabatte());
				zielDto.setBNettopreisuebersteuert(quellDto.getBNettopreisuebersteuert());
				zielDto.setDRabattsatz(quellDto.getDRabattsatz());
				zielDto.setPositionsartCNr(quellDto.getPositionsartCNr());
				zielDto.setBestellungIId(quellDto.getBestellungIId());
				zielDto.setCABKommentar(quellDto.getCABKommentar());
				zielDto.setCABNummer(quellDto.getCABNummer());
				zielDto.setNFixkosten(quellDto.getNFixkosten());
				zielDto.setGebindeIId(quellDto.getGebindeIId());
				zielDto.setNAnzahlgebinde(quellDto.getNAnzahlgebinde());

				// Status einer eingefuegten Position nicht kopieren
				// TManuellvollstaendiggeliefert nicht setzen
				// NFixkostengeliefert nicht setzen, da diese Pos ja noch nicht
				// geliefert wurde
				// iBestellpositionIIdRahmenposition nicht setzen
				// tAuftragsbestaetigungstermin nicht setzen
			}

			if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(quellDto.getBDrucken());

				zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
				zielDto.setNNettogesamtpreis(quellDto.getNNettogesamtpreis());
				zielDto.setNNettogesamtPreisminusRabatte(quellDto.getNNettoeinzelpreis());
				zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
				if (quellDto.getNNettoeinzelpreis() != null && quellDto.getNNettogesamtpreis() != null) {

					BigDecimal nRabattbetrag = quellDto.getNNettoeinzelpreis()
							.subtract(quellDto.getNNettogesamtpreis());
					BigDecimal nRabatt = new BigDecimal(0);
					if (nRabattbetrag.doubleValue() != 0) {
						nRabatt = nRabattbetrag.divide((quellDto.getNNettoeinzelpreis().divide(new BigDecimal(100),
								BigDecimal.ROUND_HALF_EVEN)), BigDecimal.ROUND_HALF_EVEN);
						zielDto.setDRabattsatz(nRabatt.doubleValue());
					}
					zielDto.setNRabattbetrag(nRabattbetrag);

				}
			} else if (belegpositionDto instanceof AnfragepositionDto[]) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				zielDto.setLossollmaterialIId(quellDto.getLossollmaterialIId());
			} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				zielDto.setBDrucken(quellDto.getBMitdrucken());
			} else if (belegpositionDto[i] instanceof AuftragpositionDto) {

				ekPreis = ((AuftragpositionDto) belegpositionDto[i]).getBdEinkaufpreis();

			} else if (belegpositionDto[i] instanceof AngebotpositionDto) {

				ekPreis = ((AngebotpositionDto) belegpositionDto[i]).getNEinkaufpreis();

			}

			// SP2825 Materialsuchlag muss immer neu berechnet werden

			if (ekPreis == null) {
				if (belegpositionDto[i].getArtikelIId() != null) {

					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
							belegpositionDto[i].getArtikelIId(), bestellungDto.getLieferantIIdBestelladresse(),
							belegpositionDto[i].getNMenge(), bestellungDto.getWaehrungCNr(),
							bestellungDto.getDBelegdatum(), theClientDto);

					if (alDto != null && alDto.getNEinzelpreis() != null) {
						zielDto.setNNettoeinzelpreis(alDto.getNEinzelpreis());
						zielDto.setNRabattbetrag(alDto.getNEinzelpreis().subtract(alDto.getNNettopreis()));
						zielDto.setNMaterialzuschlag(alDto.getNMaterialzuschlag());

						zielDto.setNNettogesamtpreis(alDto.getNNettopreis());
						zielDto.setNNettogesamtPreisminusRabatte(alDto.getNNettopreis());

						if (alDto.getNMaterialzuschlag() != null) {
							zielDto.setNNettogesamtpreis(alDto.getNNettopreis().add(alDto.getNMaterialzuschlag()));
							zielDto.setNNettogesamtPreisminusRabatte(
									alDto.getNNettopreis().add(alDto.getNMaterialzuschlag()));
						}

						zielDto.setDRabattsatz(alDto.getFRabatt());
						zielDto.setCAngebotnummer(alDto.getCAngebotnummer());
					}

				}
			} else {
				// PJ18666
				if (ekPreis != null) {
					zielDto.setDRabattsatz(0D);
					zielDto.setNNettoeinzelpreis(ekPreis);
					zielDto.setNNettogesamtpreis(ekPreis);
					zielDto.setNRabattbetrag(BigDecimal.ZERO);
				}
			}

			a.add(zielDto);
		}

		BestellpositionDto[] returnArray = new BestellpositionDto[a.size()];
		return (BestellpositionDto[]) a.toArray(returnArray);

	}

	public AnfragepositionDto[] konvertiereNachAnfragepositionDto(AnfrageDto anfrageDto,
			BelegpositionDto[] belegpositionDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<AnfragepositionDto> a = new java.util.ArrayList<AnfragepositionDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			// ALLGMEINE FELDER
			AnfragepositionDto zielDto = new AnfragepositionDto();
			cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

			if (belegpositionDto instanceof AnfragepositionDto[]) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				zielDto.setNRichtpreis(quellDto.getNRichtpreis());

			} else {
				if (anfrageDto.getLieferantIIdAnfrageadresse() != null && belegpositionDto[i].getPositionsartCNr()
						.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)) {

					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
							belegpositionDto[i].getArtikelIId(), anfrageDto.getLieferantIIdAnfrageadresse(),
							belegpositionDto[i].getNMenge(), anfrageDto.getWaehrungCNr(),
							new java.sql.Date(anfrageDto.getTBelegdatum().getTime()), theClientDto);

					if (alDto != null && alDto.getNEinzelpreis() != null) {
						zielDto.setNRichtpreis(alDto.getNNettopreis());
					}

				}

			}

			// Defaultwerte setzen
			if (zielDto.getNRichtpreis() == null) {
				zielDto.setNRichtpreis(new BigDecimal(0));
			}

			if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				zielDto.setNRichtpreis(quellDto.getNNettoeinzelpreis());
			} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];

				if (quellDto.getArtikelDto() != null) {
					if (quellDto.getArtikelDto().getArtikelsprDto() != null) {
						zielDto.setCBez(quellDto.getArtikelDto().getArtikelsprDto().getCBez());

					}
				}

				zielDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
				try {
					LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

					BigDecimal preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
							belegpositionDto[i].getArtikelIId(), lagerDto.getIId(), theClientDto);
					zielDto.setNRichtpreis(preis);

				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			} else if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				zielDto.setNRichtpreis(quellDto.getNNettoeinzelpreis());
				zielDto.setLossollmaterialIId(quellDto.getLossollmaterialIId());
			}
			a.add(zielDto);
		}

		AnfragepositionDto[] returnArray = new AnfragepositionDto[a.size()];
		return (AnfragepositionDto[]) a.toArray(returnArray);
	}

	public AngebotpositionDto[] konvertiereNachAngebotpositionDto(BelegpositionDto[] belegpositionDto, Integer kundeIId,
			Timestamp tBelegdatum, TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<AngebotpositionDto> a = new java.util.ArrayList<AngebotpositionDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			AngebotpositionDto zielDto = new AngebotpositionDto();
			MwstsatzDto mwstsatzDto = null;

			// Schritt 2a: Positionen aus Verkaufsmodulen
			if (belegpositionDto[i] instanceof BelegpositionVerkaufDto) {
				cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(zielDto,
						(BelegpositionVerkaufDto) belegpositionDto[i], theClientDto);
			}
			// Schritt 2b: Positionen aus anderen Modulen
			else {
				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				// Defaultwerte setzen
				if (zielDto.getNMenge() != null) {
					setzeDefaultVerkaufspreise(zielDto);
					zielDto.setNRabattbetrag(new BigDecimal(0));
					zielDto.setNMwstbetrag(new BigDecimal(0));
					zielDto.setNGestehungspreis(new BigDecimal(0));
				}
				zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

				if (zielDto.getBRabattsatzuebersteuert() == null) {
					zielDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
				}
				if (zielDto.getBMwstsatzuebersteuert() == null) {
					zielDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
				}

				if (zielDto.getMwstsatzIId() == null) {
					mwstsatzDto = ermittleMwstSatz(belegpositionDto[i], tBelegdatum, theClientDto);
					if (mwstsatzDto != null) {
						zielDto.setMwstsatzIId(mwstsatzDto.getIId());
						zielDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
					}
				}
			}

			if (belegpositionDto instanceof AngebotpositionDto[]) {
				AngebotpositionDto quellDto = (AngebotpositionDto) belegpositionDto[i];
				zielDto.setNRabattbetrag(quellDto.getNRabattbetrag());
				zielDto.setNMwstbetrag(quellDto.getNMwstbetrag());
				zielDto.setNGestehungspreis(quellDto.getNGestehungspreis());
				zielDto.setNEinkaufpreis(quellDto.getNEinkaufpreis());
				zielDto.setAgstklIId(quellDto.getAgstklIId());
				zielDto.setBAlternative(quellDto.getBAlternative());
				// setNGesamtwertagstklinangebotswaehrung nicht setzen
			}

			// weitere Default-Eigenschaften setzen
			// NOT NULL Felder pruefen und noetigenfalls mit default-werten
			// belegen
			if (zielDto.getBAlternative() == null) {
				zielDto.setBAlternative((short) 0);
			}

			// sonstiges
			if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				zielDto.setNGestehungspreis(quellDto.getNGestehungspreis());
				zielDto.setFRabattsatz(quellDto.getFRabattsatz());
				zielDto.setBRabattsatzuebersteuert(quellDto.getBRabattsatzuebersteuert());
				zielDto.setFZusatzrabattsatz(quellDto.getFZusatzrabattsatz());
				zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
				zielDto.setNBruttoeinzelpreis(quellDto.getNNettoeinzelpreis());

				zielDto.setAgstklIId(quellDto.getAgstklIId());
			} else if (belegpositionDto instanceof AuftragpositionDto[]) {
				AuftragpositionDto quellDto = (AuftragpositionDto) belegpositionDto[i];
				zielDto.setNEinkaufpreis(quellDto.getBdEinkaufpreis());
			} else if (belegpositionDto instanceof AnfragepositionDto[]) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				// alles richtpreis, keine rabatte
				zielDto.setNEinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNNettoeinzelpreis(quellDto.getNRichtpreis());

				BigDecimal nBruttoeinzelpreis = null;
				if (mwstsatzDto != null && mwstsatzDto.getFMwstsatz() != 0.0) {
					zielDto.setNMwstbetrag(zielDto.getNNettoeinzelpreis()
							.divide(new BigDecimal(mwstsatzDto.getFMwstsatz()), BigDecimal.ROUND_HALF_EVEN));
					nBruttoeinzelpreis = zielDto.getNNettoeinzelpreis().add(zielDto.getNMwstbetrag());
				} else {
					nBruttoeinzelpreis = quellDto.getNRichtpreis();
				}
				zielDto.setNBruttoeinzelpreis(nBruttoeinzelpreis);

				BigDecimal nGestPreis = ermittleGestehungsPreis(quellDto.getArtikelIId(), theClientDto);
				zielDto.setNGestehungspreis(nGestPreis);

			}

			else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				// ??
				zielDto.setNEinzelpreis(quellDto.getNKalkpreis());
				if (zielDto.getNEinzelpreis() == null) {
					zielDto.setNEinzelpreis(BigDecimal.ZERO);
				}

				if (quellDto.getArtikelDto() != null) {
					if (quellDto.getArtikelDto().getArtikelsprDto() != null) {
						zielDto.setCBez(quellDto.getArtikelDto().getArtikelsprDto().getCBez());

					}
				}

				zielDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
				try {
					LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

					BigDecimal preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
							belegpositionDto[i].getArtikelIId(), lagerDto.getIId(), theClientDto);
					zielDto.setNGestehungspreis(preis);

				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}
			} else if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				if (quellDto.getNRabattbetrag() != null) {
					zielDto.setNRabattbetrag(quellDto.getNRabattbetrag());
				} else {
					zielDto.setNRabattbetrag(new BigDecimal(0));
				}
				if (quellDto.getDRabattsatz() != null) {
					zielDto.setFRabattsatz(quellDto.getDRabattsatz());
				} else {
					zielDto.setFRabattsatz(new Double(0));
				}
				zielDto.setBRabattsatzuebersteuert(quellDto.getBRabattsatzUebersteuert());
				if (quellDto.getNNettoeinzelpreis() != null) {
					zielDto.setNNettoeinzelpreis(quellDto.getNNettoeinzelpreis());
				} else {
					zielDto.setNNettoeinzelpreis(new BigDecimal(0));
				}
				if (quellDto.getNNettogesamtPreisminusRabatte() != null) {
					zielDto.setNEinzelpreis(quellDto.getNNettogesamtPreisminusRabatte());
				} else {
					zielDto.setNEinzelpreis(new BigDecimal(0));
				}

				/**
				 * @todo: MR Preise richtig setzen
				 */
				// zielDto.setNMwstbetrag(new BigDecimal(0));
				// zielDto.setNNettoeinzelpreisplusversteckteraufschlag(new
				// BigDecimal(0));
				// zielDto.setNBruttoeinzelpreis(new BigDecimal(0));
				// zielDto.setNEinzelpreisplusversteckteraufschlag(new
				// BigDecimal(0));
				BigDecimal nGestPreis = ermittleGestehungsPreis(quellDto.getArtikelIId(), theClientDto);
				zielDto.setNGestehungspreis(nGestPreis);
			}

			// SP6339
			zielDto = (AngebotpositionDto) mwstsatzBestimmenUndNeuBerechnen(zielDto, kundeIId, tBelegdatum,
					theClientDto);

			a.add(zielDto);
		}

		AngebotpositionDto[] returnArray = new AngebotpositionDto[a.size()];
		return (AngebotpositionDto[]) a.toArray(returnArray);
	}

	public AuftragpositionDto[] konvertiereNachAuftragpositionDto(BelegpositionDto[] belegpositionDto, Integer kundeIId,
			Timestamp tBelegdatum, TheClientDto theClientDto) throws EJBExceptionLP {

		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		java.util.ArrayList<AuftragpositionDto> a = new java.util.ArrayList<AuftragpositionDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			// neues Dto erstellen
			AuftragpositionDto zielDto = new AuftragpositionDto();
			MwstsatzDto mwstsatzDto = null;
			// Schritt 2a: Positionen aus Verkaufsmodulen
			if (belegpositionDto[i] instanceof BelegpositionVerkaufDto) {
				cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(zielDto,
						(BelegpositionVerkaufDto) belegpositionDto[i], theClientDto);
			}
			// Schritt 2b: Positionen aus anderen Modulen
			else {
				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				// Defaultwerte setzen
				if (zielDto.getNMenge() != null) {
					setzeDefaultVerkaufspreise(zielDto);
					zielDto.setNRabattbetrag(new BigDecimal(0));
					zielDto.setNMwstbetrag(new BigDecimal(0));
				}
				zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
				if (zielDto.getMwstsatzIId() == null) {
					mwstsatzDto = ermittleMwstSatz(belegpositionDto[i], tBelegdatum, theClientDto);
					if (mwstsatzDto != null) {
						zielDto.setMwstsatzIId(mwstsatzDto.getIId());
						zielDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
					}
				}

			}
			// Offene Menge immer auf Menge setzen
			zielDto.setNOffeneMenge(Helper.cloneBigDecimal(zielDto.getNMenge()));

			// Schritt 2c: weitere Default-Eigenschaften setzen
			zielDto.setBDrucken(Helper.boolean2Short(false));
			zielDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

			// zielDto.setTUebersteuerbarerLiefertermin() wird in
			// TabbedPaneAuftrag.einfugenHV richtig gesetzt

			// Schritt 2d: NOT NULL Felder pruefen und noetigenfalls mit
			// default-werten belegen
			if (zielDto.getBRabattsatzuebersteuert() == null) {
				zielDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
			}
			// Schritt 2e: Klonen
			if (belegpositionDto[i] instanceof AuftragpositionDto) {
				AuftragpositionDto quellDto = (AuftragpositionDto) belegpositionDto[i];
				// echtes Klonen
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				zielDto.setNOffeneMenge(Helper.cloneBigDecimal(quellDto.getNOffeneMenge()));

				/*
				 * PJ 16633 zielDto.setTUebersteuerbarerLiefertermin(new Timestamp(quellDto
				 * .getTUebersteuerbarerLiefertermin().getTime()));
				 */

				// SP1059
				zielDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				zielDto.setAuftragpositionIIdRahmenposition(
						Helper.cloneInteger(quellDto.getAuftragpositionIIdRahmenposition()));
				zielDto.setNRabattbetrag(Helper.cloneBigDecimal(quellDto.getNRabattbetrag()));
				zielDto.setNMwstbetrag(Helper.cloneBigDecimal(quellDto.getNMwstbetrag()));

				// SP5588
				zielDto.setBdEinkaufpreis(Helper.cloneBigDecimal(quellDto.getBdEinkaufpreis()));

			}
			// Schritt 2f: sonstiges
			else if (belegpositionDto[i] instanceof AngebotpositionDto) {
				AngebotpositionDto quellDto = (AngebotpositionDto) belegpositionDto[i];
				zielDto.setBdEinkaufpreis(Helper.cloneBigDecimal(quellDto.getNEinkaufpreis()));
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof RechnungPositionDto) {
				RechnungPositionDto quellDto = (RechnungPositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
			} else if (belegpositionDto[i] instanceof LieferscheinpositionDto) {
				LieferscheinpositionDto quellDto = (LieferscheinpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof AnfragepositionDto) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				// Richtpreis wird Einzelpreis
				zielDto.setNEinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNBruttoeinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNNettoeinzelpreis(quellDto.getNRichtpreis());
			} else if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				// im moment nichts besonderes
			} else {
				myLogger.warn("Belegpositionkonvertierung: Details nicht definiert f\u00FCr "
						+ belegpositionDto.getClass().getName());
			}

			// SP6339
			zielDto = (AuftragpositionDto) mwstsatzBestimmenUndNeuBerechnen(zielDto, kundeIId, tBelegdatum,
					theClientDto);

			a.add(zielDto);
		}

		AuftragpositionDto[] returnArray = new AuftragpositionDto[a.size()];
		return (AuftragpositionDto[]) a.toArray(returnArray);
	}

	public LossollarbeitsplanDto[] konvertiereNachLossollarbeitsplanDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		ArrayList<LossollarbeitsplanDto> a = new ArrayList<LossollarbeitsplanDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			// neues Dto erstellen
			LossollarbeitsplanDto zielDto = new LossollarbeitsplanDto();

			// Es koennen nur Lossollarbeitsplaene kopiert werden, keine
			// Konvertierung aus
			// anderen Modulen moeglich.
			if (belegpositionDto[i] instanceof LossollarbeitsplanDto) {
				LossollarbeitsplanDto quellDto = (LossollarbeitsplanDto) belegpositionDto[i];
				zielDto.setArtikelIIdTaetigkeit(quellDto.getArtikelIIdTaetigkeit());
				zielDto.setBNachtraeglich(quellDto.getBNachtraeglich());
				zielDto.setCKomentar(quellDto.getCKomentar());
				zielDto.setIArbeitsgangnummer(quellDto.getIArbeitsgangnummer());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setNGesamtzeit(quellDto.getNGesamtzeit());
				zielDto.setXText(quellDto.getXText());
				zielDto.setLosIId(quellDto.getLosIId());
				// PJ 17223
				zielDto.setBFertig(Helper.boolean2Short(false));
				zielDto.setBAutoendebeigeht(quellDto.getBAutoendebeigeht());
				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setAgartCNr(quellDto.getAgartCNr());
			} else if (belegpositionDto[i] instanceof StuecklistearbeitsplanDto) {
				StuecklistearbeitsplanDto quellDto = (StuecklistearbeitsplanDto) belegpositionDto[i];

				zielDto.setArtikelIIdTaetigkeit(quellDto.getArtikelIId());
				zielDto.setCKomentar(quellDto.getCKommentar());
				zielDto.setIArbeitsgangnummer(quellDto.getIArbeitsgang());
				zielDto.setLRuestzeit(quellDto.getLRuestzeit());
				zielDto.setLStueckzeit(quellDto.getLStueckzeit());
				zielDto.setMaschineIId(quellDto.getMaschineIId());
				zielDto.setXText(quellDto.getXLangtext());
				zielDto.setBFertig(Helper.boolean2Short(false));
				zielDto.setBAutoendebeigeht(Helper.boolean2Short(false));

				if (zielDto.getMaschineIId() != null) {
					MaschineDto mDto = getZeiterfassungFac().maschineFindByPrimaryKey(zielDto.getMaschineIId());
					zielDto.setBAutoendebeigeht(mDto.getBAutoendebeigeht());
				}

				zielDto.setBNurmaschinenzeit(quellDto.getBNurmaschinenzeit());
				zielDto.setIUnterarbeitsgang(quellDto.getIUnterarbeitsgang());
				zielDto.setNMenge(quellDto.getNMenge());
				zielDto.setEinheitCNr(quellDto.getEinheitCNr());
				zielDto.setIAufspannung(quellDto.getIAufspannung());
				zielDto.setIMaschinenversatztage(quellDto.getIMaschinenversatztage());
				zielDto.setAgartCNr(quellDto.getAgartCNr());

			}
			a.add(zielDto);
		}
		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[a.size()];
		return (LossollarbeitsplanDto[]) a.toArray(returnArray);
	}

	@Override
	public RechnungPositionDto[] konvertiereNachRechnungpositionDto(BelegpositionDto[] belegpositionDto,
			Integer kundeId, Timestamp tBelegDatum, TheClientDto theClientDto) {
		Validator.dtoNotNull(belegpositionDto, "belegpositionDto");

		ArrayList<RechnungPositionDto> a = new ArrayList<RechnungPositionDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			// neues Dto erstellen
			RechnungPositionDto zielDto = new RechnungPositionDto();
			zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			MwstsatzDto mwstsatzDto = null;
			// Schritt 2a: Positionen aus Verkaufsmodulen
			if (belegpositionDto[i] instanceof BelegpositionVerkaufDto) {
				cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(zielDto,
						(BelegpositionVerkaufDto) belegpositionDto[i], theClientDto);
			} else {
				// Schritt 2b: Positionen aus anderen Modulen
				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				// Defaultwerte setzen
				if (zielDto.getNMenge() != null) {
					setzeDefaultVerkaufspreise(zielDto);
					zielDto.setNEinzelpreisplusversteckteraufschlag(new BigDecimal(0));
					zielDto.setFKupferzuschlag(0.0);
				}
				zielDto.setAuftragpositionIId(null);

				if (zielDto.getMwstsatzIId() == null) {
					mwstsatzDto = ermittleMwstSatz(belegpositionDto[i], tBelegDatum, theClientDto);
					if (mwstsatzDto != null) {
						zielDto.setMwstsatzIId(mwstsatzDto.getIId());
					}
				}
			}

			// SP3406 Wenn Menge <0, dann 0
			if (zielDto.getNMenge() != null && zielDto.getNMenge().signum() < 0) {
				zielDto.setNMenge(BigDecimal.ZERO);
			}

			// Schritt 2c: weitere Default-Eigenschaften setzen
			zielDto.setBDrucken(Helper.boolean2Short(true));
			// Schritt 2d: NOT NULL Felder pruefen und noetigenfalls mit
			// default-werten belegen
			if (zielDto.getBRabattsatzuebersteuert() == null) {
				zielDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
			}
			if (zielDto.getBMwstsatzuebersteuert() == null) {
				zielDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
			}

			// Schritt 2e: Klonen
			if (belegpositionDto[i] instanceof RechnungPositionDto) {
				RechnungPositionDto quellDto = (RechnungPositionDto) belegpositionDto[i];
				// echtes Klonen
				// SP4104 Auftragpos nicht uebernehmen
				zielDto.setAuftragpositionIId(null);
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				zielDto.setSeriennrChargennrMitMenge(Helper.cloneList(quellDto.getSeriennrChargennrMitMenge()));
				zielDto.setFKupferzuschlag(Helper.cloneDouble(quellDto.getFKupferzuschlag()));
				zielDto.setLieferscheinIId(Helper.cloneInteger(quellDto.getLieferscheinIId()));
				zielDto.setRechnungIId(Helper.cloneInteger(quellDto.getRechnungIId()));
				zielDto.setRechnungIIdGutschrift(Helper.cloneInteger(quellDto.getRechnungIIdGutschrift()));
				zielDto.setRechnungpositionartCNr(Helper.cloneString(quellDto.getRechnungpositionartCNr()));
				zielDto.setRechnungpositionIId(Helper.cloneInteger(quellDto.getRechnungpositionIId()));
			}
			// Schritt 2f: sonstiges
			else if (belegpositionDto[i] instanceof AngebotpositionDto) {
				AngebotpositionDto quellDto = (AngebotpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof AuftragpositionDto) {
				AuftragpositionDto quellDto = (AuftragpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				// echtes Klonen
				zielDto.setAuftragpositionIId(Helper.cloneInteger(quellDto.getIId()));
			} else if (belegpositionDto[i] instanceof LieferscheinpositionDto) {
				LieferscheinpositionDto quellDto = (LieferscheinpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBMitdrucken()));
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof AnfragepositionDto) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				zielDto.setBDrucken(Helper.cloneShort(quellDto.getBDrucken()));
				// im moment nichts besonderes
			} else {
				myLogger.warn("Belegpositionkonvertierung: Details nicht definiert f\u00FCr "
						+ belegpositionDto.getClass().getName());
			}
			// SP6339
			zielDto = (RechnungPositionDto) mwstsatzBestimmenUndNeuBerechnen(zielDto, kundeId, tBelegDatum,
					theClientDto);
			a.add(zielDto);
		}

		RechnungPositionDto[] returnArray = new RechnungPositionDto[a.size()];
		return (RechnungPositionDto[]) a.toArray(returnArray);
	}

	public LieferscheinpositionDto[] konvertiereNachLieferscheinpositionDto(BelegpositionDto[] belegpositionDto,
			Integer kundeIId, Timestamp tBelegdatum, boolean bMwstSatzNeuBestimmen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (belegpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("belegpositionDto == null"));
		}
		ArrayList<LieferscheinpositionDto> a = new ArrayList<LieferscheinpositionDto>();
		for (int i = 0; i < belegpositionDto.length; i++) {
			// neues Dto erstellen
			LieferscheinpositionDto zielDto = new LieferscheinpositionDto();
			zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

			// Schritt 2a: Positionen aus Verkaufsmodulen
			if (belegpositionDto[i] instanceof BelegpositionVerkaufDto) {
				cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(zielDto,
						(BelegpositionVerkaufDto) belegpositionDto[i], theClientDto);

			}
			// Schritt 2b: Positionen aus anderen Modulen
			else {
				cloneBelegpositionDtoFromBelegpositionDto(zielDto, belegpositionDto[i], theClientDto);

				// Defaultwerte setzen
				if (zielDto.getNMenge() != null) {
					setzeDefaultVerkaufspreise(zielDto);
					zielDto.setNRabattbetrag(new BigDecimal(0));
					zielDto.setNMwstbetrag(new BigDecimal(0));
					zielDto.setFKupferzuschlag(0.0);
				}

				zielDto.setAuftragpositionIId(null);

			}

			// Schritt 2c: weitere Default-Eigenschaften setzen
			// Schritt 2d: NOT NULL Felder pruefen und noetigenfalls mit
			// default-werten belegen
			if (zielDto.getBMwstsatzuebersteuert() == null) {
				zielDto.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
			}
			if (zielDto.getBRabattsatzuebersteuert() == null) {
				zielDto.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
			}

			// Schritt 2e: Klonen
			if (belegpositionDto[i] instanceof LieferscheinpositionDto) {
				LieferscheinpositionDto quellDto = (LieferscheinpositionDto) belegpositionDto[i];

				if (quellDto.getPositioniIdArtikelset() != null) {
					continue;
				}

				zielDto.setFKupferzuschlag(Helper.cloneDouble(quellDto.getFKupferzuschlag()));
				zielDto.setLieferscheinIId(Helper.cloneInteger(quellDto.getLieferscheinIId()));
				zielDto.setLieferscheinpositionartCNr(Helper.cloneString(quellDto.getLieferscheinpositionartCNr()));

				zielDto.setSeriennrChargennrMitMenge(Helper.cloneList(quellDto.getSeriennrChargennrMitMenge()));

				// Auftragpos nicht uebernehmen
				zielDto.setAuftragpositionIId(null);
			}

			// Schritt 2f: sonstiges
			else if (belegpositionDto[i] instanceof AngebotpositionDto) {
				AngebotpositionDto quellDto = (AngebotpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto instanceof AgstklpositionDto[]) {
				AgstklpositionDto quellDto = (AgstklpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof AuftragpositionDto) {
				AuftragpositionDto quellDto = (AuftragpositionDto) belegpositionDto[i];
				// echtes Klonen
				// zielDto.setAuftragpositionIId(Helper.cloneInteger(quellDto.
				// getIId()));
			} else if (belegpositionDto[i] instanceof RechnungPositionDto) {
				// im moment nichts besonderes
				RechnungPositionDto quellDto = (RechnungPositionDto) belegpositionDto[i];
			} else if (belegpositionDto instanceof StuecklistepositionDto[]) {
				StuecklistepositionDto quellDto = (StuecklistepositionDto) belegpositionDto[i];
				zielDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
				// im moment nichts besonderes
			} else if (belegpositionDto[i] instanceof AnfragepositionDto) {
				AnfragepositionDto quellDto = (AnfragepositionDto) belegpositionDto[i];
				// Richtpreis wird Einzelpreis
				zielDto.setNEinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNBruttoeinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNNettoeinzelpreis(quellDto.getNRichtpreis());
				zielDto.setNNettoeinzelpreisplusversteckteraufschlag(quellDto.getNRichtpreis());
				zielDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(quellDto.getNRichtpreis());
			} else if (belegpositionDto instanceof BestellpositionDto[]) {
				BestellpositionDto quellDto = (BestellpositionDto) belegpositionDto[i];
				// im moment nichts besonderes
			} else {
				myLogger.warn("Belegpositionkonvertierung: Details nicht definiert f\u00FCr "
						+ belegpositionDto.getClass().getName());
			}

			// SP7266
			if (bMwstSatzNeuBestimmen == true) {
				// SP5720
				zielDto = (LieferscheinpositionDto) mwstsatzBestimmenUndNeuBerechnen(zielDto, kundeIId, tBelegdatum,
						theClientDto);
			}

			a.add(zielDto);
		}

		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[a.size()];
		return (LieferscheinpositionDto[]) a.toArray(returnArray);
	}

	public BelegpositionDto cloneBelegpositionDtoFromBelegpositionDto(BelegpositionDto target, BelegpositionDto source,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// alle Eigenschaften uebertragen (Echtes Klonen!!!)
		target.setPositionsartCNr(Helper.cloneString(source.getPositionsartCNr()));

		target.setUsecaseIIdQuelle(Helper.cloneInteger(source.getUsecaseIIdQuelle()));
		target.setIKeyQuelle(Helper.cloneInteger(source.getIId()));

		target.setBInitial(Helper.cloneShort(source.getBInitial()));

		// Bei Handartikel die IID nicht kopieren und ArtikelBez auf
		// uebersteuert setzen
		if (target.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			setzeHandartikelUebersteuert(target, Boolean.TRUE);

		} else {
			target.setArtikelIId(Helper.cloneInteger(source.getArtikelIId()));
			// Artikelbezeichnung uebersteuert darf nicht null sein.
			if (source.getBArtikelbezeichnunguebersteuert() != null) {
				target.setBArtikelbezeichnunguebersteuert(
						Helper.cloneShort(source.getBArtikelbezeichnunguebersteuert()));
			} else {
				target.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(Boolean.FALSE));
			}
		}

		// beleg_i_id kopieren macht nur sinn, wenn beide aus dem gleichen Modul
		// kommen
		if (target.getClass().equals(source.getClass())) {
			target.setBelegIId(Helper.cloneInteger(source.getBelegIId()));
		}
		target.setCBez(Helper.cloneString(source.getCBez()));
		target.setCZusatzbez(Helper.cloneString(source.getCZusatzbez()));

		// SP4578
		if (target.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {

			if (source instanceof StuecklistepositionDto) {
				ArtikelDto aDto = ((StuecklistepositionDto) source).getArtikelDto();
				if (aDto.getArtikelsprDto() != null && aDto.getArtikelsprDto().getCBez() != null) {
					target.setCBez(aDto.getArtikelsprDto().getCBez());
				}

			}

		}

		target.setEinheitCNr(Helper.cloneString(source.getEinheitCNr()));
		// i_id kopieren macht nur sinn, wenn beide aus dem gleichen Modul
		// kommen
		if (target.getClass().equals(source.getClass())) {
			target.setIId(Helper.cloneInteger(source.getIId()));
		}
		target.setISort(Helper.cloneInteger(source.getISort()));
		target.setMediastandardIId(Helper.cloneInteger(source.getMediastandardIId()));
		target.setNMenge(Helper.cloneBigDecimal(source.getNMenge()));

		target.setXTextinhalt(Helper.cloneString(source.getXTextinhalt()));
		// Das Kopieren kann auch nach bestimmten Kriterien erfolgen. z.B.
		// Preise sollen
		// aus ... kommmen.
		// Das wird hier behandelt:
		switch (source.getiCopyPasteModus()) {
		case BelegpositionDto.COPY_PASTE_MODUS_NORMAL: {
			// passt, nix besonderes.
		}
			break;
		case BelegpositionDto.COPY_PASTE_MODUS_IST_PREIS_AUS_LOS: {
			if (source instanceof LossollmaterialDto) {
				// Ist Menge setzen
				// Istpreis wird in konvertiereNach Methode gesetzt
				LossollmaterialDto quellDto = (LossollmaterialDto) source;
				/**
				 * @todo MR: WH Fragen ob Menge oder Ausgegebene Menge genommen werden soll.
				 */
				// try {
				/*
				 * BigDecimal bdAusgegebeneMenge = null; bdAusgegebeneMenge =
				 * getFertigungFac().getAusgegebeneMenge(quellDto.getIId(), theClientDto);
				 */
				target.setNMenge(quellDto.getNMenge());
				/*
				 * } catch (RemoteException ex) { throwEJBExceptionLPRespectOld(ex); }
				 */
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_COPY_PASTE,
						new Exception("Fehler bei C&P: keine Instanz von LossollmaterialDto"));
			}
		}
			break;
		default: {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_COPY_PASTE,
					new Exception("Fehler bei C&P: unbekannter iCopyPasteModus"));
		}
		}
		return target;
	}

	public BelegpositionVerkaufDto cloneBelegpositionVerkaufDtoFromBelegpositionVerkaufDto(
			BelegpositionVerkaufDto target, BelegpositionVerkaufDto source, TheClientDto theClientDto) {
		// Basiseigenschaften uebertragen
		cloneBelegpositionDtoFromBelegpositionDto(target, source, theClientDto);
		// alle weiteren Eigenschaften uebertragen
		target.setBMwstsatzuebersteuert(Helper.cloneShort(source.getBMwstsatzuebersteuert()));
		target.setBNettopreisuebersteuert(Helper.cloneShort(source.getBNettopreisuebersteuert()));
		target.setBRabattsatzuebersteuert(Helper.cloneShort(source.getBRabattsatzuebersteuert()));
		target.setFRabattsatz(Helper.cloneDouble(source.getFRabattsatz()));
		target.setFZusatzrabattsatz(Helper.cloneDouble(source.getFZusatzrabattsatz()));
		target.setMwstsatzIId(Helper.cloneInteger(source.getMwstsatzIId()));
		target.setVerleihIId(Helper.cloneInteger(source.getVerleihIId()));
		// Preise
		target.setNBruttoeinzelpreis(Helper.cloneBigDecimal(source.getNBruttoeinzelpreis()));
		target.setCLvposition(source.getCLvposition());
		target.setKostentraegerIId(source.getKostentraegerIId());
		target.setNEinzelpreis(Helper.cloneBigDecimal(source.getNEinzelpreis()));
		target.setNMaterialzuschlag(Helper.cloneBigDecimal(source.getNMaterialzuschlag()));
		target.setTMaterialzuschlagDatum(source.getTMaterialzuschlagDatum());
		target.setNMaterialzuschlagKurs(source.getNMaterialzuschlagKurs());
		target.setNMaterialzuschlag(Helper.cloneBigDecimal(source.getNMaterialzuschlag()));
		target.setNEinzelpreisplusversteckteraufschlag(
				Helper.cloneBigDecimal(source.getNEinzelpreisplusversteckteraufschlag()));
		target.setNNettoeinzelpreis(Helper.cloneBigDecimal(source.getNNettoeinzelpreis()));
		target.setNNettoeinzelpreisplusversteckteraufschlag(
				Helper.cloneBigDecimal(source.getNNettoeinzelpreisplusversteckteraufschlag()));
		target.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
				Helper.cloneBigDecimal(source.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
		target.setTypCNr(source.getTypCNr());

		target.setLieferantIId(source.getLieferantIId());
		target.setNNettoeinzelpreisplusversteckteraufschlag(
				Helper.cloneBigDecimal(source.getNNettoeinzelpreisplusversteckteraufschlag()));

		target.setNDimMenge(source.getNDimMenge());
		target.setNDimHoehe(source.getNDimHoehe());
		target.setNDimBreite(source.getNDimBreite());
		target.setNDimTiefe(source.getNDimTiefe());

		if (source instanceof AuftragpositionDto) {
			if (target instanceof AngebotpositionDto) {
				((AngebotpositionDto) target).setNMwstbetrag(((AuftragpositionDto) source).getNMwstbetrag());
				((AngebotpositionDto) target).setNRabattbetrag(((AuftragpositionDto) source).getNRabattbetrag());
			}
			if (target instanceof LieferscheinpositionDto) {
				((LieferscheinpositionDto) target).setNMwstbetrag(((AuftragpositionDto) source).getNMwstbetrag());
				((LieferscheinpositionDto) target).setNRabattbetrag(((AuftragpositionDto) source).getNRabattbetrag());
			}
		}
		if (source instanceof AngebotpositionDto) {
			if (target instanceof AuftragpositionDto) {
				((AuftragpositionDto) target).setNMwstbetrag(((AngebotpositionDto) source).getNMwstbetrag());
				((AuftragpositionDto) target).setNRabattbetrag(((AngebotpositionDto) source).getNRabattbetrag());
			}
			if (target instanceof LieferscheinpositionDto) {
				((LieferscheinpositionDto) target).setNMwstbetrag(((AngebotpositionDto) source).getNMwstbetrag());
				((LieferscheinpositionDto) target).setNRabattbetrag(((AngebotpositionDto) source).getNRabattbetrag());
			}

		}
		if (source instanceof LieferscheinpositionDto) {
			if (target instanceof AuftragpositionDto) {
				((AuftragpositionDto) target).setNMwstbetrag(((LieferscheinpositionDto) source).getNMwstbetrag());
				((AuftragpositionDto) target).setNRabattbetrag(((LieferscheinpositionDto) source).getNRabattbetrag());
			}
			if (target instanceof AngebotpositionDto) {
				((AngebotpositionDto) target).setNMwstbetrag(((LieferscheinpositionDto) source).getNMwstbetrag());
				((AngebotpositionDto) target).setNRabattbetrag(((LieferscheinpositionDto) source).getNRabattbetrag());
			}

		}

		if (source instanceof RechnungPositionDto) {
			// SP4394
			if (source.getArtikelIId() != null
					&& source.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null
					&& source.getNBruttoeinzelpreis() != null) {

				target.setNMwstbetrag(source.getNBruttoeinzelpreis()
						.subtract(source.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()));
			}
			if (source.getArtikelIId() != null && source.getFRabattsatz() != null
					&& source.getNNettoeinzelpreis() != null) {

				BigDecimal bdRabattbetrag = source.getNEinzelpreis()
						.multiply(new BigDecimal(source.getFRabattsatz().doubleValue())).movePointLeft(2);
				target.setNRabattbetrag(bdRabattbetrag);

			}

		}

		return target;

	}

	private BigDecimal ermittleGestehungsPreis(Integer artikelIId, TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal preis = new BigDecimal(0);
		try {
			LagerDto lagerDto = getLagerFac().getHauptlagerDesMandanten(theClientDto);

			preis = getLagerFac().getGemittelterGestehungspreisEinesLagers(artikelIId, lagerDto.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return preis;
	}

	/**
	 * Wenn von Einkauf in Verkauf kopiert wird muessen die Preisfelder
	 * zurueckgesetzt werden
	 * 
	 * @param zielDto RechnungPositionDto
	 */
	private void setzeDefaultVerkaufspreise(BelegpositionVerkaufDto zielDto) {
		zielDto.setNNettoeinzelpreis(new BigDecimal(0));
		zielDto.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(0));
		zielDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(0));
		zielDto.setNBruttoeinzelpreis(new BigDecimal(0));
		zielDto.setNEinzelpreis(new BigDecimal(0));
		zielDto.setFRabattsatz(0.0);
		zielDto.setFZusatzrabattsatz(0.0);
	}

	private void setzeHandartikelUebersteuert(BelegpositionDto positionDto, boolean bUebersteuert) {
		// Wenn Handeingabe, ArtikelID nicht mitkopieren,Artikelbezeichnung auf
		// uebersteuert setzen
		if (positionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			positionDto.setArtikelIId(null);
			positionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(bUebersteuert));

		}
	}

	public WartungsschritteDto[] konvertiereNachWartungsschritteDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) {

		ArrayList<WartungsschritteDto> al = new ArrayList<WartungsschritteDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {

			if (belegpositionDto[i].getArtikelIId() != null) {

				WartungsschritteDto zielDto = new WartungsschritteDto();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(belegpositionDto[i].getArtikelIId(),
						theClientDto);

				if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
					zielDto.setArtikelIId(belegpositionDto[i].getArtikelIId());
				} else {
					continue;
				}

				zielDto.setLDauer(new Long(0));

				zielDto.setTAbdurchfuehren(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
				zielDto.setAuftragwiederholungsintervallCNr(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH);

				if (belegpositionDto instanceof WartungsschritteDto[]) {
					WartungsschritteDto quellDto = (WartungsschritteDto) belegpositionDto[i];
					zielDto.setPersonalgruppeIId(quellDto.getPersonalgruppeIId());
					zielDto.setTAbdurchfuehren(quellDto.getTAbdurchfuehren());
					zielDto.setAuftragwiederholungsintervallCNr(quellDto.getAuftragwiederholungsintervallCNr());
					zielDto.setLDauer(quellDto.getLDauer());
					zielDto.setTagesartIId(quellDto.getTagesartIId());

					al.add(zielDto);
				} else {
					Query query = em.createNamedQuery("PersonalgruppefindAll");
					Collection<?> cl = query.getResultList();

					if (cl.size() > 0) {
						Personalgruppe p = (Personalgruppe) cl.iterator().next();
						zielDto.setPersonalgruppeIId(p.getIId());
						al.add(zielDto);
					}

				}

			}

		}

		WartungsschritteDto[] returnArray = new WartungsschritteDto[al.size()];
		return (WartungsschritteDto[]) al.toArray(returnArray);
	}

	public WartungslisteDto[] konvertiereNachWartungslisteDto(BelegpositionDto[] belegpositionDto,
			TheClientDto theClientDto) {

		ArrayList<WartungslisteDto> al = new ArrayList<WartungslisteDto>();

		for (int i = 0; i < belegpositionDto.length; i++) {
			WartungslisteDto zielDto = new WartungslisteDto();

			if (belegpositionDto[i].getArtikelIId() != null) {
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(belegpositionDto[i].getArtikelIId(),
						theClientDto);

				if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					zielDto.setCBez(belegpositionDto[i].getCBez());
				} else {
					zielDto.setArtikelIId(belegpositionDto[i].getArtikelIId());
				}

			} else {
				zielDto.setCBez(belegpositionDto[i].getCBez());
			}

			zielDto.setNMenge(belegpositionDto[i].getNMenge());

			zielDto.setBVerrechenbar(Helper.boolean2Short(false));
			zielDto.setBWartungsmaterial(Helper.boolean2Short(false));

			if (belegpositionDto instanceof WartungslisteDto[]) {
				WartungslisteDto quellDto = (WartungslisteDto) belegpositionDto[i];
				zielDto.setBVerrechenbar(quellDto.getBVerrechenbar());
				zielDto.setBWartungsmaterial(quellDto.getBWartungsmaterial());
				zielDto.setTPersonalVeraltet(quellDto.getTPersonalVeraltet());
				zielDto.settVeraltet(quellDto.getTVeraltet());
				zielDto.setXBemerkung(quellDto.getXBemerkung());
				zielDto.setPersonalIIdVeraltet(quellDto.getPersonalIIdVeraltet());

			}

			al.add(zielDto);

		}

		WartungslisteDto[] returnArray = new WartungslisteDto[al.size()];
		return (WartungslisteDto[]) al.toArray(returnArray);
	}
}
