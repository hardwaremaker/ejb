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
package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragRahmenAbrufFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Facade beinhaltet die Logik, die fuer die Behandlung von
 * Rahmenauftraegen und den zugehoerigen Abrufen zustaenig ist.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 10.11.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.19 $
 */
@Stateless
public class AuftragRahmenAbrufFacBean extends Facade implements
		AuftragRahmenAbrufFac {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Wenn eine neue Abrufposition angelegt wird, muss die entsprechende
	 * Rahmenposition angepasst werden.
	 * 
	 * @param abrufpositionDtoI
	 *            die aktuelle Abrufposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Abrufposition
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAbrufpositionZuRahmenposition(
			AuftragpositionDto abrufpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAbrufpositionDto(abrufpositionDtoI);
		Integer abrufpositionIId = null;
		AuftragDto abrufauftragDto = null;
		AuftragDto rahmenauftragDto = null;

		try {
			abrufauftragDto = getAuftragFac().auftragFindByPrimaryKey(
					abrufpositionDtoI.getBelegIId());
			rahmenauftragDto = getAuftragFac().auftragFindByPrimaryKey(
					abrufauftragDto.getAuftragIIdRahmenauftrag());
			if (abrufauftragDto.getDLiefertermin().before(
					rahmenauftragDto.getDLiefertermin())) {
				abrufpositionDtoI
						.setTUebersteuerbarerLiefertermin(abrufauftragDto
								.getDLiefertermin());
			}
			// Schritt 1: die Abrufposition mit Reservierung anlegen
			abrufpositionIId = getAuftragpositionFac().createAuftragposition(
					abrufpositionDtoI, theClientDto);

			// Schritt 2: die zugehoerige Rahmenposition korrigieren
			AuftragpositionDto rahmenpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(
							abrufpositionDtoI
									.getAuftragpositionIIdRahmenposition());

			rahmenpositionDto.setNOffeneRahmenMenge(rahmenpositionDto
					.getNOffeneRahmenMenge().subtract(
							abrufpositionDtoI.getNMenge()));

			setzeStatusRahmenposition(rahmenpositionDto);

			getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
					rahmenpositionDto, theClientDto);

			// Schritt 3: den Status des zugehoerigen Rahmenauftrags
			// aktualisieren
			pruefeUndSetzeRahmenstatus(rahmenpositionDto.getBelegIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}

		return abrufpositionIId;
	}

	/**
	 * Der Status einer Rahmenposition wird bestimmt durch die aktuelle offene
	 * Menge.
	 * 
	 * @param rahmenpositionDto
	 *            die Rahmenposition
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void setzeStatusRahmenposition(AuftragpositionDto rahmenpositionDto) {
		if (rahmenpositionDto.getNOffeneRahmenMenge().doubleValue() == 0) {
			rahmenpositionDto
					.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
		} else if (rahmenpositionDto.getNOffeneRahmenMenge().doubleValue() == rahmenpositionDto
				.getNMenge().doubleValue()) {
			rahmenpositionDto
					.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
		} else {
			rahmenpositionDto
					.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_TEILERLEDIGT);
		}
	}

	/**
	 * Wenn eine Abrufposition geloescht wird, muss die entsprechende
	 * Rahmenposition angepasst werden.
	 * 
	 * @param abrufpositionDtoI
	 *            die aktuelle Abrufbestellposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAbrufpositionZuRahmenposition(
			AuftragpositionDto abrufpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAbrufpositionDto(abrufpositionDtoI);
		try {
			// Schritt 1: die Abrufposition mit Reservierung loeschen
			getAuftragpositionFac().removeAuftragposition(abrufpositionDtoI,
					theClientDto);

			// Schritt 2: die zugehoerige Rahmenposition korrigieren
			AuftragpositionDto rahmenpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(
							abrufpositionDtoI
									.getAuftragpositionIIdRahmenposition());

			rahmenpositionDto
					.setNOffeneRahmenMenge(rahmenpositionDto
							.getNOffeneRahmenMenge().add(
									abrufpositionDtoI.getNMenge()));

			setzeStatusRahmenposition(rahmenpositionDto);

			getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
					rahmenpositionDto, theClientDto);

			// Schritt 3: den Status des zugehoerigen Rahmenauftrags
			// aktualisieren
			pruefeUndSetzeRahmenstatus(rahmenpositionDto.getBelegIId(),
					theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Abrufbestellposition in Sicht Rahmen wird korrigiert. Damit mussen
	 * auch die Mengen in der Rahmenbestellung angepasst werden.
	 * 
	 * @param abrufpositionDtoI
	 *            die Abrufbestellposition mit den aktuellen Werten
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAbrufpositionZuRahmenpositionSichtRahmen(
			AuftragpositionDto abrufpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAbrufpositionDto(abrufpositionDtoI);
		try {
			AuftragpositionDto bestehendeAbrufpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(abrufpositionDtoI.getIId());

			AuftragpositionDto bestehendeRahmenpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(
							bestehendeAbrufpositionDto
									.getAuftragpositionIIdRahmenposition());

			BigDecimal nZusaetzlicheMengeAbrufposition = abrufpositionDtoI
					.getNMenge();

			// Schritt 1: Die Abrufposition mit Reservierung aktualisieren
			bestehendeAbrufpositionDto.setNMenge(bestehendeAbrufpositionDto
					.getNMenge().add(nZusaetzlicheMengeAbrufposition));

			getAuftragpositionFac().updateAuftragposition(
					bestehendeAbrufpositionDto, theClientDto);

			// Schritt 2: Die zugehoerige Rahmenposition aktualisieren
			bestehendeRahmenpositionDto
					.setNOffeneRahmenMenge(bestehendeRahmenpositionDto
							.getNOffeneRahmenMenge().subtract(
									nZusaetzlicheMengeAbrufposition));

			setzeStatusRahmenposition(bestehendeRahmenpositionDto);

			getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
					bestehendeRahmenpositionDto, theClientDto);

			// Schritt 3: Den Status des Rahmenauftrags pruefen
			pruefeUndSetzeRahmenstatus(
					bestehendeRahmenpositionDto.getBelegIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} 
	}

	/**
	 * Eine Abrufbestellposition wird korrigiert. Damit mussen auch die Mengen
	 * in der Rahmenbestellung angepasst werden.
	 * 
	 * @param abrufpositionDtoI
	 *            die Abrufbestellposition mit den aktuellen Werten
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAbrufpositionZuRahmenposition(
			AuftragpositionDto abrufpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAbrufpositionDto(abrufpositionDtoI);
		AuftragpositionDto bestehendeRahmenpositionDto = null;
		try {
			if (abrufpositionDtoI.getAuftragpositionIIdRahmenposition() != null) {
				// Schritt 1: Die Rahmenposition anpassen, wenn ein Bezug auf
				// einen Rahmenauftrag gibt
				AuftragpositionDto bestehendeAbrufpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								abrufpositionDtoI.getIId());

				bestehendeRahmenpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(
								bestehendeAbrufpositionDto
										.getAuftragpositionIIdRahmenposition());

				BigDecimal nDeltaMengeAbrufposition = abrufpositionDtoI
						.getNMenge().subtract(
								bestehendeAbrufpositionDto.getNMenge());

				if (nDeltaMengeAbrufposition.doubleValue() > 0) {
					// die erfasste Menge wurde erhoeht -> Rahmenposition
					// verringern
					bestehendeRahmenpositionDto
							.setNOffeneRahmenMenge(bestehendeRahmenpositionDto
									.getNOffeneRahmenMenge().subtract(
											nDeltaMengeAbrufposition));
				} else if (nDeltaMengeAbrufposition.doubleValue() < 0) {
					// die erfasste Menge wurde verringert -> Rahmenposition
					// erhoehen
					bestehendeRahmenpositionDto
							.setNOffeneRahmenMenge(bestehendeRahmenpositionDto
									.getNOffeneRahmenMenge().add(
											nDeltaMengeAbrufposition.negate()));
				}

				setzeStatusRahmenposition(bestehendeRahmenpositionDto);

				getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(
						bestehendeRahmenpositionDto, theClientDto);
			}

			// Schritt 2: Die Abrufposition aktualisieren
			getAuftragpositionFac().updateAuftragposition(abrufpositionDtoI,
					theClientDto);

			// Schritt 3: Den Status des Rahmenauftrags pruefen, wenn ein Bezug
			// auf einen Rahmenauftrag gibt
			if (abrufpositionDtoI.getAuftragpositionIIdRahmenposition() != null) {
				pruefeUndSetzeRahmenstatus(
						bestehendeRahmenpositionDto.getBelegIId(),
						theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}
	}

	private void checkAbrufpositionDto(AuftragpositionDto abrufpositionDtoI)
			throws EJBExceptionLP {
		if (abrufpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("abrufpositionDtoI == null"));
		}

		myLogger.logData(abrufpositionDtoI);
	}

	/**
	 * Den Status eines bestimmten Rahmenauftrags aufgrund seiner
	 * Rahmenpositionen pruefen und setzen.
	 * 
	 * @param iIdRahmenauftragI
	 *            Integer
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void pruefeUndSetzeRahmenstatus(Integer iIdRahmenauftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdRahmenauftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdRahmenauftragI == null"));
		}

		myLogger.logData(iIdRahmenauftragI);

		try {
			AuftragDto rahmenauftragDto = getAuftragFac()
					.auftragFindByPrimaryKey(iIdRahmenauftragI);

			boolean bAlleAbrufeErledigt = true;
			AuftragDto[] abrufAuftrag = getAuftragFac()
					.abrufauftragFindByAuftragIIdRahmenauftrag(
							iIdRahmenauftragI, theClientDto);
			for (int i = 0; i < abrufAuftrag.length; i++) {
				if (!(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT
						.equals(abrufAuftrag[i].getStatusCNr()) || AuftragServiceFac.AUFTRAGSTATUS_STORNIERT
						.equals(abrufAuftrag[i].getStatusCNr()))) {
					bAlleAbrufeErledigt = false;
				}
			}

			AuftragpositionDto[] aRahmenpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(iIdRahmenauftragI);

			double dOffeneMenge = 0;
			double dGesamtmenge = 0;

			for (int i = 0; i < aRahmenpositionDto.length; i++) {
				if (aRahmenpositionDto[i].getNOffeneRahmenMenge() != null
						&& aRahmenpositionDto[i].getNOffeneRahmenMenge()
								.doubleValue() > 0) { // Ueberlieferungen
														// duerfen das Ergebnis
														// nicht aendern
					dOffeneMenge += aRahmenpositionDto[i]
							.getNOffeneRahmenMenge().doubleValue();

					dGesamtmenge += aRahmenpositionDto[i].getNMenge()
							.doubleValue();
				}
			}

			
			if(rahmenauftragDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT)){
				//ungueltiger status -> Siehe PJ 1063
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT,
						new Exception("FEHLER_RAHMENAUFTRAG_IST_IM_STATUS_ANGELEGT"));
			}
			
			if (dOffeneMenge == 0) {
				if (bAlleAbrufeErledigt) {
					rahmenauftragDto
							.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
				} else {
					rahmenauftragDto
							.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
				}
			} else if (dOffeneMenge == dGesamtmenge) {
				rahmenauftragDto
						.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			} else {
				rahmenauftragDto
						.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
			}

			getAuftragFac().aendereAuftragstatus(rahmenauftragDto.getIId(),
					rahmenauftragDto.getStatusCNr(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Aufgrund des Divisors die Abrufpositionen fuer einen Abrufauftrag
	 * erzeugen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Abrufauftrags
	 * @param iDivisorI
	 *            der Divisor
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void erzeugeAbrufpositionen(Integer iIdAuftragI, int iDivisorI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		if (iDivisorI <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
					new Exception("iDivisorI <= 0"));
		}

		AuftragDto abrufauftragDto = null;
		AuftragDto rahmenauftragDto = null;

		abrufauftragDto = getAuftragFac().auftragFindByPrimaryKey(iIdAuftragI);

		rahmenauftragDto = getAuftragFac().auftragFindByPrimaryKey(
				abrufauftragDto.getAuftragIIdRahmenauftrag());

		try {
			// alle offenen oder teilerledigten Positionen des Rahmenauftrags
			// holen
			AuftragpositionDto[] aRahmenpositionDto = getAuftragpositionFac()
					.auftragpositionFindByAuftrag(rahmenauftragDto.getIId());

			for (int i = 0; i < aRahmenpositionDto.length; i++) {
				AuftragpositionDto rahmenpositionDto = aRahmenpositionDto[i];

				if (rahmenpositionDto.getNOffeneMenge() != null
						&& rahmenpositionDto.getNOffeneMenge().doubleValue() > 0) {
					AuftragpositionDto abrufpositionDto = (AuftragpositionDto) rahmenpositionDto
							.clone();
					abrufpositionDto
							.setTUebersteuerbarerLiefertermin(abrufauftragDto
									.getDLiefertermin());
					abrufpositionDto.setBelegIId(abrufauftragDto.getIId());
					abrufpositionDto
							.setAuftragpositionIIdRahmenposition(rahmenpositionDto
									.getIId());
					abrufpositionDto
							.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
					/*
					 * if
					 * (abrufauftragDto.getDLiefertermin().before(rahmenauftragDto
					 * .getDLiefertermin())){
					 * abrufpositionDto.setTUebersteuerbarerLiefertermin
					 * (abrufauftragDto.getDLiefertermin()); }
					 */

					double dMengeAbrufposition = 0;

					// die Menge aufgrund des Divisors bestimmen
					if ((iDivisorI != 1)
							&& (new Double(iDivisorI).doubleValue() <= rahmenpositionDto
									.getNMenge().doubleValue())) {
						// zuerst den Rest bestimmen
						double dRest = rahmenpositionDto.getNMenge()
								.doubleValue()
								% new Double(iDivisorI).doubleValue();

						// jetzt ergibt die Division einen ganzzahligen Wert
						dMengeAbrufposition = (rahmenpositionDto.getNMenge()
								.doubleValue() - dRest)
								/ new Double(iDivisorI).doubleValue();
					} else {
						dMengeAbrufposition = rahmenpositionDto
								.getNOffeneRahmenMenge().doubleValue();
					}

					abrufpositionDto.setNMenge(Helper.rundeKaufmaennisch(
							new BigDecimal(dMengeAbrufposition), 4));
					abrufpositionDto.setNOffeneMenge(abrufpositionDto
							.getNMenge());

					getAuftragpositionFac().createAuftragposition(
							abrufpositionDto, theClientDto);

					// Schritt 2: Die zugehoerige Rahmenposition aktualisieren
					rahmenpositionDto.setNOffeneRahmenMenge(rahmenpositionDto
							.getNOffeneRahmenMenge().subtract(
									new BigDecimal(dMengeAbrufposition)));

					setzeStatusRahmenposition(rahmenpositionDto);

					getAuftragpositionFac()
							.updateAuftragpositionOhneWeitereAktion(
									rahmenpositionDto, theClientDto);
				} else {
					AuftragpositionDto abrufpositionDto = (AuftragpositionDto) rahmenpositionDto
							.clone();
					abrufpositionDto.setBelegIId(abrufauftragDto.getIId());
					getAuftragpositionFac().createAuftragposition(
							abrufpositionDto, theClientDto);
				}
			}

			// Schritt 3: Den Status des Rahmenauftrags pruefen
			pruefeUndSetzeRahmenstatus(rahmenauftragDto.getIId(), theClientDto);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}
	}
}
