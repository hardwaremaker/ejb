/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.eingangsrechnung.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungAuftragszuordnung;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungKontierung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDtoAssembler;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDtoAssembler;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungKontierungDtoAssembler;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDtoAssembler;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class EingangsrechnungFacBean extends Facade implements
		EingangsrechnungFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Wirft eine EJBException, wenn das Feld Reverse Charge nicht meht den, auf
	 * der hinterlegen Bestellung, vorhandenen Anzahlungen/Schlussrechnungen
	 * uebereinstimmt.
	 * 
	 * @param erDtoI
	 */
	private void pruefeReverseChargeWieBeiVorhandenAnzahlungen(
			EingangsrechnungDto erDtoI) {
		String art = erDtoI.getEingangsrechnungartCNr();
		if (art.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
				|| art.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
			for (EingangsrechnungDto er : eingangsrechnungFindByBestellungIId(erDtoI
					.getBestellungIId())) {
				if (er.getIId().equals(erDtoI.getIId()))
					continue;
				if (er.getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
						|| er.getEingangsrechnungartCNr()
								.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG)) {
					if (Helper.short2boolean(er.getBReversecharge()) != Helper
							.short2boolean(erDtoI.getBReversecharge())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND,
								"FEHLER_ER_ANZAHLUNGEN_REVERSE_CHARGE_ABWEICHEND",
								er.getCNr());
					}

				}
			}
		}
	}

	public EingangsrechnungDto createEingangsrechnung(
			EingangsrechnungDto erDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(erDtoI);
		// begin
		// Pruefen auf doppelte Lieferantenrechnungsnummer
		// if (erDtoI.getCLieferantenrechnungsnummer() != null) {
		// EingangsrechnungDto[] erDto =
		// eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
		// erDtoI.
		// getLieferantIId(), erDtoI.getCLieferantenrechnungsnummer());
		// if (erDto != null) {
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT
		// , null);
		// }
		// }

		pruefeReverseChargeWieBeiVorhandenAnzahlungen(erDtoI);

		LpBelegnummerFormat f = getBelegnummerGeneratorObj()
				.getBelegnummernFormat(erDtoI.getMandantCNr());
		try {
			// Geschaeftsjahr berechnen
			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					erDtoI.getMandantCNr(), erDtoI.getDBelegdatum());
			erDtoI.setIGeschaeftsjahr(iGeschaeftsjahr);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		LpBelegnummer bnr = null;
		if (erDtoI.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
			bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					erDtoI.getIGeschaeftsjahr(), PKConst.PK_EINGANGSRECHNUNG,
					"zusatzkosten", erDtoI.getMandantCNr(), theClientDto);

		} else {
			bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					erDtoI.getIGeschaeftsjahr(), PKConst.PK_EINGANGSRECHNUNG,
					erDtoI.getMandantCNr(), theClientDto);
		}
		erDtoI.setIId(bnr.getPrimaryKey());
		erDtoI.setIGeschaeftsjahr(bnr.getGeschaeftsJahr());
		String belegNummer = f.format(bnr);
		erDtoI.setCNr(belegNummer);
		erDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
		erDtoI.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		if (erDtoI.getBReversecharge() == null) {
			erDtoI.setBReversecharge(Helper.boolean2Short(false));
		}
		if (erDtoI.getBIgErwerb() == null) {
			erDtoI.setBIgErwerb(Helper.boolean2Short(false));
		}

		// Auf angelegt setzen
		erDtoI.setStatusCNr(EingangsrechnungFac.STATUS_ANGELEGT);
		// Werte pruefen
		pruefeBetraege(erDtoI);
		try {
			Eingangsrechnung eingangsrechnung = new Eingangsrechnung(
					erDtoI.getIId(), erDtoI.getCNr(),
					erDtoI.getIGeschaeftsjahr(), erDtoI.getMandantCNr(),
					erDtoI.getEingangsrechnungartCNr(),
					erDtoI.getDBelegdatum(), erDtoI.getDFreigabedatum(),
					erDtoI.getLieferantIId(), erDtoI.getNBetrag(),
					erDtoI.getNBetragfw(), erDtoI.getNUstBetrag(),
					erDtoI.getNUstBetragfw(), erDtoI.getMwstsatzIId(),
					erDtoI.getNKurs(), erDtoI.getWaehrungCNr(),
					erDtoI.getStatusCNr(), erDtoI.getPersonalIIdAnlegen(),
					erDtoI.getPersonalIIdAendern(), erDtoI.getBReversecharge(),
					erDtoI.getBIgErwerb());
			em.persist(eingangsrechnung);
			em.flush();
			erDtoI.setTAendern(eingangsrechnung.getTAendern());
			erDtoI.setTAnlegen(eingangsrechnung.getTAnlegen());
			setEingangsrechnungFromEingangsrechnungDto(eingangsrechnung, erDtoI);
			pruefeZugeordneteKontenAufGleichesFinanzamt(erDtoI.getIId(),
					theClientDto);

			HvDtoLogger<EingangsrechnungDto> logger = new HvDtoLogger<EingangsrechnungDto>(
					em, erDtoI.getIId(), theClientDto);
			logger.logInsert(erDtoI);

			// PJ 16969
			try {
				ParametermandantDto pmDto = getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_LIEFERANT,
								ParameterFac.PARAMETER_AUTOMATISCHE_KREDITORENNUMMER);
				if ((Boolean) pmDto.getCWertAsObject() == true) {
					if (erDtoI.getLieferantIId() != null) {
						LieferantDto lieferantDto = getLieferantFac()
								.lieferantFindByPrimaryKey(
										erDtoI.getLieferantIId(), theClientDto);
						if (lieferantDto.getKontoIIdKreditorenkonto() == null) {
							KontoDto ktoDto = getLieferantFac()
									.createKreditorenkontoZuLieferantenAutomatisch(
											erDtoI.getLieferantIId(), false,
											null, theClientDto);
							lieferantDto
									.setIKreditorenkontoAsIntegerNotiId(new Integer(
											ktoDto.getCNr()));
							getLieferantFac().updateLieferant(lieferantDto,
									theClientDto);
						}
					}
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			// AD wieso ??? Verbuchung passiert im Update -> geaendert
			try {
				verbucheEingangsrechnung(erDtoI.getIId(), erDtoI.getKontoIId(),
						theClientDto);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return erDtoI;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

	}

	/**
	 * ER stronieren.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void storniereEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData("I_ID=" + eingangsrechnungIId);
		// begin
		/**
		 * @todo fibu: zahlungen weg PJ 4189
		 * @todo zahlungen loeschen PJ 4189
		 */
		if (eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId).length > 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN,
					"FEHLER_STORNIEREN_ZAHLUNGEN_VORHANDEN");
		}
		updateEingangsrechnungStatus(theClientDto, eingangsrechnungIId,
				EingangsrechnungFac.STATUS_STORNIERT);
		// fibu: Verbuchen rueckgaengig
		getBelegbuchungFac(theClientDto.getMandant())
				.verbucheEingangsrechnungRueckgaengig(eingangsrechnungIId,
						theClientDto);
	}

	/**
	 * ER stronieren.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void storniereEingangsrechnungRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData("I_ID=" + eingangsrechnungIId);
		// begin
		/**
		 * @todo fibu: Verbuchen PJ 4190
		 */
		updateEingangsrechnungStatus(theClientDto, eingangsrechnungIId,
				EingangsrechnungFac.STATUS_ANGELEGT);
	}

	public int wiederholendeZusatzkostenAnlegen(TheClientDto theClientDto) {

		int iAnzahlAngelegt = 0;
		Session session = FLRSessionFactory.getFactory().openSession();

		Criteria crit = session.createCriteria(FLREingangsrechnung.class);

		crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
		crit.add(Restrictions.eq("eingangsrechnungart_c_nr",
				EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN));

		crit.add(Restrictions.not(Restrictions.eq("status_c_nr",
				EingangsrechnungFac.STATUS_STORNIERT)));

		crit.add(Restrictions.isNull("t_wiederholenderledigt"));

		crit.add(Restrictions.isNull("eingangsrechnung_i_id_nachfolger"));
		crit.add(Restrictions.isNotNull("auftragwiederholungsintervall_c_nr"));

		crit.add(Restrictions.lt(
				"t_belegdatum",
				Helper.cutTimestamp(new Timestamp(System.currentTimeMillis()
						+ (24 * 3600000)))));

		List<?> resultList = crit.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			FLREingangsrechnung flrEingangsrechnung = (FLREingangsrechnung) resultListIterator
					.next();

			// Naechster faelliger Termin nach Heute
			Calendar cBeginn = Calendar.getInstance();
			cBeginn.setTimeInMillis(flrEingangsrechnung.getT_belegdatum()
					.getTime());

			String intervall = flrEingangsrechnung
					.getAuftragwiederholungsintervall_c_nr();

			Timestamp tHeute = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis() + (24 * 3600000)));

			if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2WOECHENTLICH)) {
				cBeginn.set(Calendar.DAY_OF_MONTH,
						cBeginn.get(Calendar.DAY_OF_MONTH) + 14);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_WOECHENTLICH)) {
				cBeginn.set(Calendar.DAY_OF_MONTH,
						cBeginn.get(Calendar.DAY_OF_MONTH) + 7);
			}

			if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_JAHR)) {
				cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 1);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_2JAHR)) {
				cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 2);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_3JAHR)) {
				cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 3);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_4JAHR)) {
				cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 4);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_5JAHR)) {
				cBeginn.set(Calendar.YEAR, cBeginn.get(Calendar.YEAR) + 5);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_MONATLICH)) {
				cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 1);
			} else if (intervall
					.equals(AuftragServiceFac.AUFTRAGWIEDERHOLUNGSINTERVALL_QUARTAL)) {
				cBeginn.set(Calendar.MONTH, cBeginn.get(Calendar.MONTH) + 3);
			}

			Date tBelegdatumNeu = new Date(cBeginn.getTimeInMillis());

			if (cBeginn.getTimeInMillis() < tHeute.getTime()) {

				Eingangsrechnung er = em.find(Eingangsrechnung.class,
						flrEingangsrechnung.getI_id());

				EingangsrechnungDto erDtoNeu = assembleEingangsrechnungDto(er);
				erDtoNeu.setIId(null);
				erDtoNeu.setDBelegdatum(tBelegdatumNeu);
				erDtoNeu.setStatusCNr(EingangsrechnungFac.STATUS_ANGELEGT);
				Integer idNeu = createEingangsrechnung(erDtoNeu, theClientDto)
						.getIId();

				// Nachfolger setzten
				er.setEingangsrechnungIIdNachfolger(idNeu);
				iAnzahlAngelegt++;
			}

		}

		return iAnzahlAngelegt;
	}

	public EingangsrechnungDto updateEingangsrechnung(
			EingangsrechnungDto erDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		/**
		 * @todo umdatieren PJ 4198
		 */
		// log
		myLogger.logData(erDtoI);
		// begin
		// erlaubt ??
		pruefeUpdateAufEingangsrechnungErlaubt(erDtoI.getIId(), theClientDto);
		pruefeReverseChargeWieBeiVorhandenAnzahlungen(erDtoI);
		try {
			// Pruefen auf doppelte Lieferantenrechnungsnummer
			// if (erDtoI.getCLieferantenrechnungsnummer() != null) {
			// EingangsrechnungDto erDto =
			// eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc
			// (erDtoI.
			// getLieferantIId(), erDtoI.getCLieferantenrechnungsnummer());
			// // nur wenns nicht die selbe ist
			// if (erDto != null && !erDtoI.getIId().equals(erDto.getIId())) {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_LIEFERANTENRECHNUNGSNUMMER_DOPPELT, null);
			// }
			// }
			// pruefen , ob nicht ein hoeherer Wert auf Auftraege zugeordnet ist
			// aber nicht fuer ER-Gutschriften
			if (!erDtoI.getEingangsrechnungartCNr().equals(
					EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
				EingangsrechnungAuftragszuordnungDto[] azDtos = eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(erDtoI
						.getIId());
				if (azDtos != null && azDtos.length > 0) {
					BigDecimal bdZugeordnet = getSummeZuAuftraegenZugeordnet(erDtoI
							.getIId());
					if (bdZugeordnet.compareTo(erDtoI.getNBetragfw()) > 0) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_WERT_UNTER_AUFTRAGSZUORDNUNG,
								"");
					}
				}
			}
			// wenn gesamtkontierung, dann mehrfachkontierungen loeschen
			if (erDtoI.getKostenstelleIId() != null) {
				EingangsrechnungKontierungDto[] erKontoDto = eingangsrechnungKontierungFindByEingangsrechnungIId(erDtoI
						.getIId());
				for (int i = 0; i < erKontoDto.length; i++) {
					removeEingangsrechnungKontierung(erKontoDto[i],
							theClientDto);
				}
			}
			// das Belegdatum darf nur innerhalb des GJ der ER geaendert werden
			Integer iGJNeu = getParameterFac().getGeschaeftsjahr(
					theClientDto.getMandant(), erDtoI.getDBelegdatum());
			if (!iGJNeu.equals(erDtoI.getIGeschaeftsjahr())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BELEG_DARF_NICHT_IN_EIN_ANDERES_GJ_UMDATIERT_WERDEN,
						new Exception("Es wurde versucht, die RE "
								+ erDtoI.getCNr() + " auf "
								+ erDtoI.getDBelegdatum() + " (GJ " + iGJNeu
								+ ") umzudatieren"));
			}
			erDtoI.setPersonalIIdAendern(theClientDto.getIDPersonal());
			erDtoI.setTAendern(getTimestamp());
			pruefeBetraege(erDtoI);
			Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
					erDtoI.getIId());
			if (eingangsrechnung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}

			EingangsrechnungDto erDto_vorher = eingangsrechnungFindByPrimaryKey(erDtoI
					.getIId());
			HvDtoLogger<EingangsrechnungDto> erLogger = new HvDtoLogger<EingangsrechnungDto>(
					em, erDtoI.getIId(), theClientDto);
			erLogger.log(erDto_vorher, erDtoI);

			setEingangsrechnungFromEingangsrechnungDto(eingangsrechnung, erDtoI);
			pruefeZugeordneteKontenAufGleichesFinanzamt(erDtoI.getIId(),
					theClientDto);
			// Verbuchung wenn nicht storniert
			if (!erDtoI.getStatusCNr().equals(
					EingangsrechnungFac.STATUS_STORNIERT))
				verbucheEingangsrechnung(erDtoI.getIId(), erDtoI.getKontoIId(),
						theClientDto);

			return erDtoI;
		} catch (RemoteException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	private void verbucheEingangsrechnung(Integer eingangsrechnungIId,
			Integer kontoIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		// Verbuchung
		if (kontoIId != null) {
			// Einfachkontierung
			// alte storno falls vorhanden
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheEingangsrechnungRueckgaengig(eingangsrechnungIId,
							theClientDto);
			// und verbuchen
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheEingangsrechnung(eingangsrechnungIId, theClientDto);
		} else {
			// Mehrfachkontierung
			BigDecimal bdOffen = getWertNochNichtKontiert(eingangsrechnungIId);
			if (bdOffen.doubleValue() == 0) {
				// vollstaendig dann verbuchen
				// alte storno falls vorhanden
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheEingangsrechnungRueckgaengig(
								eingangsrechnungIId, theClientDto);
				// und verbuchen
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheEingangsrechnung(eingangsrechnungIId,
								theClientDto);
			} else {
				// alte storno falls vorhanden
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheEingangsrechnungRueckgaengig(
								eingangsrechnungIId, theClientDto);
			}
		}
	}

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// kein log
		// try {
		Eingangsrechnung eingangsrechnung = em
				.find(Eingangsrechnung.class, iId);
		if (eingangsrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleEingangsrechnungDto(eingangsrechnung);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKeyOhneExc(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		// kein log
		// try {
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		if (eingangsrechnung == null) {
			myLogger.warn("eingangsrechnungIId=" + eingangsrechnungIId);
			return null;
		}
		return assembleEingangsrechnungDto(eingangsrechnung);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("eingangsrechnungIId=" + eingangsrechnungIId, ex);
		// return null;
		// }
	}

	public EingangsrechnungDto eingangsrechnungFindByPrimaryKeyWithNull(
			Integer eingangsrechnungIId) {
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		return eingangsrechnung == null ? null
				: assembleEingangsrechnungDto(eingangsrechnung);
	}

	public EingangsrechnungDto[] eingangsrechnungFindByMandantCNr(
			String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery("EingangsrechnungfindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
	}

	public EingangsrechnungDto[] eingangsrechnungFindByMandantCNrDatumVonBis(
			String mandantCNr, Date dVon, Date dBis) throws EJBExceptionLP,
			RemoteException {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByMandantBelegdatumVonBis");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, dVon);
		query.setParameter(3, dBis);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
	}

	public EingangsrechnungDto[] eingangsrechnungFindByBestellungIId(
			Integer bestellungIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByBestellungIId");
		query.setParameter(1, bestellungIId);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		// if(eingangsrechnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public EingangsrechnungDto[] eingangsrechnungFindByMandantLieferantIId(
			String mandantCNr, Integer lieferantIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByMandantLieferantIId");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, lieferantIId);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		// if(eingangsrechnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public EingangsrechnungDto eingangsrechnungFindByCNrMandantCNr(String cNr,
			String mandantCNr) {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Eingangsrechnung eingangsrechnung = (Eingangsrechnung) query
					.getSingleResult();
			return assembleEingangsrechnungDto(eingangsrechnung);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public EingangsrechnungDto[] eingangsrechnungFindByMandantLieferantIIdOhneExc(
			String mandantCNr, Integer lieferantIId) {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByMandantLieferantIId");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, lieferantIId);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		// if(eingangsrechnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
		// }
		// catch (Throwable th) {
		// return null;
		// }
	}

	public ArrayList<EingangsrechnungDto> eingangsrechnungFindOffeneByLieferantIId(
			Integer lieferantIId) {

		ArrayList<EingangsrechnungDto> offene = new ArrayList<EingangsrechnungDto>();
		Query query = em
				.createNamedQuery("EingangsrechnungfindByLieferantIIdStatusCNrEingangsrechnungartCNr");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, EingangsrechnungFac.STATUS_ANGELEGT);
		query.setParameter(3,
				EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT);
		Collection<?> eingangsrechnungDtos = query.getResultList();

		Iterator it = eingangsrechnungDtos.iterator();
		while (it.hasNext()) {
			offene.add(assembleEingangsrechnungDto((Eingangsrechnung) it.next()));
		}

		query = em
				.createNamedQuery("EingangsrechnungfindByLieferantIIdStatusCNrEingangsrechnungartCNr");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, EingangsrechnungFac.STATUS_TEILBEZAHLT);
		query.setParameter(3,
				EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT);
		eingangsrechnungDtos = query.getResultList();

		it = eingangsrechnungDtos.iterator();
		while (it.hasNext()) {
			offene.add(assembleEingangsrechnungDto((Eingangsrechnung) it.next()));
		}

		return offene;

	}

	public EingangsrechnungDto[] eingangsrechnungFindByLieferantIIdLieferantenrechnungsnummerOhneExc(
			Integer lieferantIId, String sLieferantenrechnungsnummer)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByLieferantIIdCLieferantenrechnungsnummer");
		query.setParameter(1, lieferantIId);
		query.setParameter(2, sLieferantenrechnungsnummer);
		Collection<?> eingangsrechnungDtos = query.getResultList();

		return assembleEingangsrechnungDtos(eingangsrechnungDtos);

	}

	private EingangsrechnungDto[] eingangsrechnungFindByBelegdatumVonBis(
			String mandantCNr, Date von, Date bis) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByMandantBelegdatumVonBis");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, von);
		query.setParameter(3, bis);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		// if(eingangsrechnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private EingangsrechnungDto[] eingangsrechnungFindByFreigabedatumVonBis(
			String mandantCNr, Date von, Date bis) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungfindByMandantFreigabedatumVonBis");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, von);
		query.setParameter(3, bis);
		Collection<?> eingangsrechnungDtos = query.getResultList();
		// if(eingangsrechnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		return assembleEingangsrechnungDtos(eingangsrechnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setEingangsrechnungFromEingangsrechnungDto(
			Eingangsrechnung eingangsrechnung,
			EingangsrechnungDto eingangsrechnungDto) {
		eingangsrechnung.setCNr(eingangsrechnungDto.getCNr());
		eingangsrechnung.setIGeschaeftsjahr(eingangsrechnungDto
				.getIGeschaeftsjahr());
		eingangsrechnung.setMandantCNr(eingangsrechnungDto.getMandantCNr());
		eingangsrechnung.setEingangsrechnungartCNr(eingangsrechnungDto
				.getEingangsrechnungartCNr());
		eingangsrechnung.setTBelegdatum(eingangsrechnungDto.getDBelegdatum());
		eingangsrechnung.setTFreigabedatum(eingangsrechnungDto
				.getDFreigabedatum());
		eingangsrechnung.setLieferantIId(eingangsrechnungDto.getLieferantIId());
		eingangsrechnung.setCText(eingangsrechnungDto.getCText());
		eingangsrechnung.setKostenstelleIId(eingangsrechnungDto
				.getKostenstelleIId());
		eingangsrechnung.setZahlungszielIId(eingangsrechnungDto
				.getZahlungszielIId());
		eingangsrechnung.setBestellungIId(eingangsrechnungDto
				.getBestellungIId());
		eingangsrechnung.setKontoIId(eingangsrechnungDto.getKontoIId());
		eingangsrechnung.setNBetrag(eingangsrechnungDto.getNBetrag());
		eingangsrechnung.setNBetragfw(eingangsrechnungDto.getNBetragfw());
		eingangsrechnung.setNUstbetrag(eingangsrechnungDto.getNUstBetrag());
		eingangsrechnung.setNUstbetragfw(eingangsrechnungDto.getNUstBetragfw());
		eingangsrechnung.setMwstsatzIId(eingangsrechnungDto.getMwstsatzIId());
		eingangsrechnung.setNKurs(eingangsrechnungDto.getNKurs());
		eingangsrechnung.setWaehrungCNr(eingangsrechnungDto.getWaehrungCNr());
		eingangsrechnung.setStatusCNr(eingangsrechnungDto.getStatusCNr());
		eingangsrechnung.setTBezahltdatum(eingangsrechnungDto
				.getDBezahltdatum());
		eingangsrechnung.setPersonalIIdAnlegen(eingangsrechnungDto
				.getPersonalIIdAnlegen());
		eingangsrechnung.setTAnlegen(eingangsrechnungDto.getTAnlegen());
		eingangsrechnung.setPersonalIIdAendern(eingangsrechnungDto
				.getPersonalIIdAendern());
		eingangsrechnung.setTAendern(eingangsrechnungDto.getTAendern());
		eingangsrechnung.setPersonalIIdManuellerledigt(eingangsrechnungDto
				.getPersonalIIdManuellerledigt());
		eingangsrechnung.setTManuellerledigt(eingangsrechnungDto
				.getTManuellerledigt());
		eingangsrechnung.setCLieferantenrechnungsnummer(eingangsrechnungDto
				.getCLieferantenrechnungsnummer());
		eingangsrechnung.setTFibuuebernahme(eingangsrechnungDto
				.getTFibuuebernahme());
		eingangsrechnung.setCKundendaten(eingangsrechnungDto.getCKundendaten());
		eingangsrechnung
				.setAuftragwiederholungsintervallCNr(eingangsrechnungDto
						.getWiederholungsintervallCNr());
		eingangsrechnung.setEingangsrechnungIIdNachfolger(eingangsrechnungDto
				.getEingangsrechnungIIdNachfolger());
		eingangsrechnung.setBReversecharge(eingangsrechnungDto
				.getBReversecharge());
		eingangsrechnung.setBIgErwerb(eingangsrechnungDto.getBIgErwerb());
		eingangsrechnung.setMahnstufeIId(eingangsrechnungDto.getMahnstufeIId());
		eingangsrechnung.setTMahndatum(eingangsrechnungDto.getTMahndatum());
		eingangsrechnung.setTWiederholenderledigt(eingangsrechnungDto
				.getTWiederholenderledigt());
		eingangsrechnung.setPersonalIIdWiederholenderledigt(eingangsrechnungDto
				.getPersonalIIdWiederholenderledigt());
		eingangsrechnung.setTZollimportpapier(eingangsrechnungDto
				.getTZollimportpapier());
		eingangsrechnung.setPersonalIIdZollimportpapier(eingangsrechnungDto
				.getPersonalIIdZollimportpapier());
		eingangsrechnung.setCZollimportpapier(eingangsrechnungDto
				.getCZollimportpapier());
		eingangsrechnung.setCWeartikel(eingangsrechnungDto.getCWeartikel());
		eingangsrechnung.setEingangsrechnungIdZollimport(eingangsrechnungDto
				.getEingangsrechnungIdZollimport());
		em.merge(eingangsrechnung);
		em.flush();
	}

	private EingangsrechnungDto assembleEingangsrechnungDto(
			Eingangsrechnung eingangsrechnung) {
		return EingangsrechnungDtoAssembler.createDto(eingangsrechnung);
	}

	private EingangsrechnungDto[] assembleEingangsrechnungDtos(
			Collection<?> eingangsrechnungs) {
		List<EingangsrechnungDto> list = new ArrayList<EingangsrechnungDto>();
		if (eingangsrechnungs != null) {
			Iterator<?> iterator = eingangsrechnungs.iterator();
			while (iterator.hasNext()) {
				Eingangsrechnung eingangsrechnung = (Eingangsrechnung) iterator
						.next();
				list.add(assembleEingangsrechnungDto(eingangsrechnung));
			}
		}
		EingangsrechnungDto[] returnArray = new EingangsrechnungDto[list.size()];
		return (EingangsrechnungDto[]) list.toArray(returnArray);
	}

	public EingangsrechnungAuftragszuordnungDto createEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_EINGANGSRECHNUNGAUFTRAGSZUORDNUNG);
		eingangsrechnungAuftragszuordnungDto.setIId(iId);
		eingangsrechnungAuftragszuordnungDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		eingangsrechnungAuftragszuordnungDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());
		try {
			EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung = new EingangsrechnungAuftragszuordnung(
					eingangsrechnungAuftragszuordnungDto.getIId(),
					eingangsrechnungAuftragszuordnungDto
							.getEingangsrechnungIId(),
					eingangsrechnungAuftragszuordnungDto.getAuftragIId(),
					eingangsrechnungAuftragszuordnungDto.getNBetrag(),
					eingangsrechnungAuftragszuordnungDto
							.getPersonalIIdAnlegen(),
					eingangsrechnungAuftragszuordnungDto
							.getPersonalIIdAendern());
			em.persist(eingangsrechnungAuftragszuordnung);
			em.flush();

			eingangsrechnungAuftragszuordnungDto
					.setTAendern(eingangsrechnungAuftragszuordnung
							.getTAendern());
			eingangsrechnungAuftragszuordnungDto
					.setTAnlegen(eingangsrechnungAuftragszuordnung
							.getTAnlegen());
			setEingangsrechnungAuftragszuordnungFromEingangsrechnungAuftragszuordnungDto(
					eingangsrechnungAuftragszuordnung,
					eingangsrechnungAuftragszuordnungDto);
			return eingangsrechnungAuftragszuordnungDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungAuftragszuordnungDto != null) {
			Integer iId = eingangsrechnungAuftragszuordnungDto.getIId();
			// try {
			EingangsrechnungAuftragszuordnung toRemove = em.find(
					EingangsrechnungAuftragszuordnung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			// }
			// catch (RemoveException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public EingangsrechnungAuftragszuordnungDto updateEingangsrechnungAuftragszuordnung(
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungAuftragszuordnungDto != null) {
			eingangsrechnungAuftragszuordnungDto
					.setPersonalIIdAendern(theClientDto.getIDPersonal());
			eingangsrechnungAuftragszuordnungDto.setTAendern(getTimestamp());
			Integer iId = eingangsrechnungAuftragszuordnungDto.getIId();
			try {
				EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung = em
						.find(EingangsrechnungAuftragszuordnung.class, iId);
				setEingangsrechnungAuftragszuordnungFromEingangsrechnungAuftragszuordnungDto(
						eingangsrechnungAuftragszuordnung,
						eingangsrechnungAuftragszuordnungDto);
				return eingangsrechnungAuftragszuordnungDto;
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}
	}

	public EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		try {
			EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung = em
					.find(EingangsrechnungAuftragszuordnung.class, iId);
			if (eingangsrechnungAuftragszuordnung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleEingangsrechnungAuftragszuordnungDto(eingangsrechnungAuftragszuordnung);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungAuftragszuordnungfindByEingangsrechnungIId");
		query.setParameter(1, eingangsrechnungIId);
		Collection<?> eingangsrechnungAuftragszuordnungDtos = query
				.getResultList();
		// if(eingangsrechnungAuftragszuordnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleEingangsrechnungAuftragszuordnungDtos(eingangsrechnungAuftragszuordnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public EingangsrechnungAuftragszuordnungDto[] eingangsrechnungAuftragszuordnungFindByAuftragIId(
			Integer auftragIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungAuftragszuordnungfindByAuftragIId");
		query.setParameter(1, auftragIId);
		Collection<?> eingangsrechnungAuftragszuordnungDtos = query
				.getResultList();
		// if(eingangsrechnungAuftragszuordnungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleEingangsrechnungAuftragszuordnungDtos(eingangsrechnungAuftragszuordnungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public EingangsrechnungKontierungDto[] eingangsrechnungKontierungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungKontierungfindByEingangsrechnungIId");
		query.setParameter(1, eingangsrechnungIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> eingangsrechnungKontierungDtos = query.getResultList();
		// if(eingangsrechnungKontierungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleEingangsrechnungKontierungDtos(eingangsrechnungKontierungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public EingangsrechnungKontierungDto[] eingangsrechnungKontierungFindAll()
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("EingangsrechnungKontierungfindAll");
		Collection<?> eingangsrechnungKontierungDtos = query.getResultList();
		// if(eingangsrechnungKontierungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleEingangsrechnungKontierungDtos(eingangsrechnungKontierungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	private void setEingangsrechnungAuftragszuordnungFromEingangsrechnungAuftragszuordnungDto(
			EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung,
			EingangsrechnungAuftragszuordnungDto eingangsrechnungAuftragszuordnungDto) {
		eingangsrechnungAuftragszuordnung
				.setEingangsrechnungIId(eingangsrechnungAuftragszuordnungDto
						.getEingangsrechnungIId());
		eingangsrechnungAuftragszuordnung
				.setAuftragIId(eingangsrechnungAuftragszuordnungDto
						.getAuftragIId());
		eingangsrechnungAuftragszuordnung
				.setNBetrag(eingangsrechnungAuftragszuordnungDto.getNBetrag());
		eingangsrechnungAuftragszuordnung
				.setCText(eingangsrechnungAuftragszuordnungDto.getCText());
		eingangsrechnungAuftragszuordnung
				.setPersonalIIdAnlegen(eingangsrechnungAuftragszuordnungDto
						.getPersonalIIdAnlegen());
		eingangsrechnungAuftragszuordnung
				.setTAnlegen(eingangsrechnungAuftragszuordnungDto.getTAnlegen());
		eingangsrechnungAuftragszuordnung
				.setPersonalIIdAendern(eingangsrechnungAuftragszuordnungDto
						.getPersonalIIdAendern());
		eingangsrechnungAuftragszuordnung
				.setTAendern(eingangsrechnungAuftragszuordnungDto.getTAendern());
		em.merge(eingangsrechnungAuftragszuordnung);
		em.flush();
	}

	private EingangsrechnungAuftragszuordnungDto assembleEingangsrechnungAuftragszuordnungDto(
			EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung) {
		return EingangsrechnungAuftragszuordnungDtoAssembler
				.createDto(eingangsrechnungAuftragszuordnung);
	}

	private EingangsrechnungAuftragszuordnungDto[] assembleEingangsrechnungAuftragszuordnungDtos(
			Collection<?> eingangsrechnungAuftragszuordnungs) {
		List<EingangsrechnungAuftragszuordnungDto> list = new ArrayList<EingangsrechnungAuftragszuordnungDto>();
		if (eingangsrechnungAuftragszuordnungs != null) {
			Iterator<?> iterator = eingangsrechnungAuftragszuordnungs
					.iterator();
			while (iterator.hasNext()) {
				EingangsrechnungAuftragszuordnung eingangsrechnungAuftragszuordnung = (EingangsrechnungAuftragszuordnung) iterator
						.next();
				list.add(assembleEingangsrechnungAuftragszuordnungDto(eingangsrechnungAuftragszuordnung));
			}
		}
		EingangsrechnungAuftragszuordnungDto[] returnArray = new EingangsrechnungAuftragszuordnungDto[list
				.size()];
		return (EingangsrechnungAuftragszuordnungDto[]) list
				.toArray(returnArray);
	}

	/**
	 * Den noch nicht auf Auftraege zugeordneten Wert einer ER bestimmen.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getWertNochNichtZuAuftraegenZugeordnet(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		EingangsrechnungDto erDto = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		// Nettobetrachtung
		BigDecimal restWert = null;

		if (Helper.short2boolean(erDto.getBIgErwerb())
				|| Helper.short2boolean(erDto.getBReversecharge())) {
			// SP723
			restWert = erDto.getNBetragfw();
		} else {
			restWert = erDto.getNBetragfw().subtract(erDto.getNUstBetragfw());
		}

		// Bereits zugeordnete Werte subtrahieren
		EingangsrechnungAuftragszuordnungDto[] azDtos = eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < azDtos.length; i++) {
			restWert = restWert.subtract(azDtos[i].getNBetrag());
		}

		// PJ17873 Inserate ebenfalls abziehen
		InseraterDto[] izDtos = getInseratFac()
				.inseraterFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < izDtos.length; i++) {
			restWert = restWert.subtract(izDtos[i].getNBetrag());
		}

		return restWert;
	}

	/**
	 * Den noch nicht kontierten Wert einer ER bestimmen.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getWertNochNichtKontiert(Integer eingangsrechnungIId)
			throws EJBExceptionLP {
		EingangsrechnungDto erDto = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		BigDecimal restWert = erDto.getNBetragfw();
		EingangsrechnungKontierungDto[] azDtos = eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < azDtos.length; i++) {
			restWert = restWert.subtract(azDtos[i].getNBetrag());
		}
		return restWert;
	}

	/**
	 * Den Anteil des ER-Wertes bestimmen, der auf Auftraege zugeordnet ist.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @return BigDecimal
	 * @throws EJBExceptionLP
	 */
	public BigDecimal getSummeZuAuftraegenZugeordnet(Integer eingangsrechnungIId)
			throws EJBExceptionLP {
		BigDecimal restWert = new BigDecimal(0);
		EingangsrechnungAuftragszuordnungDto[] azDtos = eingangsrechnungAuftragszuordnungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < azDtos.length; i++) {
			restWert = restWert.add(azDtos[i].getNBetrag());
		}
		return restWert;
	}

	/**
	 * Den Default-wert fuer das Freigabedatum anhand Mandantenparametern
	 * bestimmen.
	 * 
	 * @return Date
	 */
	public java.sql.Date getDefaultFreigabeDatum() {
		/**
		 * @todo parametrierbar PJ 4199
		 */
		return getDate();
	}

	public BigDecimal berechneSummeOffenBruttoInMandantenwaehrung(
			TheClientDto theClientDto, String sKriteriumDatum,
			String sKriteriumJahr, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten) {
		String s = "SELECT er, (SELECT SUM(erz.n_betrag) FROM FLREingangsrechnungzahlung erz WHERE erz.eingangsrechnung_i_id=er.i_id GROUP BY erz.eingangsrechnung_i_id ) FROM FLREingangsrechnung er WHERE er.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND er.status_c_nr NOT IN ('"
				+ EingangsrechnungFac.STATUS_STORNIERT
				+ "','"
				+ EingangsrechnungFac.STATUS_ERLEDIGT + "') AND ";

		if (bZusatzkosten) {
			s += " er.eingangsrechnungart_c_nr ='"
					+ EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND ";
		} else {
			s += " er.eingangsrechnungart_c_nr <>'"
					+ EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND ";
		}

		if (sKriteriumDatum.equals(EingangsrechnungFac.KRIT_DATUM_BELEGDATUM)) {

			s += " er.t_belegdatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcVon
							.getTimeInMillis()))
					+ "' AND er.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcBis
							.getTimeInMillis())) + "'";

		} else {
			s += " er.t_freigabedatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcVon
							.getTimeInMillis()))
					+ "' AND er.t_freigabedatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcBis
							.getTimeInMillis())) + "'";

		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query queryErz = session.createQuery(s);

		List<?> resultList = queryErz.list();

		Iterator resultListIterator = resultList.iterator();
		BigDecimal bdSumme = new BigDecimal(0);
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLREingangsrechnung er = (FLREingangsrechnung) o[0];

			BigDecimal bdBezahlt = new BigDecimal(0);
			if (o[1] != null) {
				bdBezahlt = (BigDecimal) o[1];
			}

			// rechnungsbetrag
			bdSumme = bdSumme.add(er.getN_betrag());
			// minus bereits bezahlt
			bdSumme = bdSumme.subtract(bdBezahlt);
		}

		return bdSumme;
	}

	public BigDecimal berechneSummeOffenNettoInMandantenwaehrung(
			TheClientDto theClientDto, String sKriteriumDatum,
			String sKriteriumJahr, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten) {

		String s = "SELECT er, (SELECT SUM(erz.n_betrag) FROM FLREingangsrechnungzahlung erz WHERE erz.eingangsrechnung_i_id=er.i_id GROUP BY erz.eingangsrechnung_i_id ),(SELECT SUM(erz.n_betrag_ust) FROM FLREingangsrechnungzahlung erz WHERE erz.eingangsrechnung_i_id=er.i_id GROUP BY erz.eingangsrechnung_i_id ) FROM FLREingangsrechnung er WHERE er.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND er.status_c_nr NOT IN ('"
				+ EingangsrechnungFac.STATUS_STORNIERT
				+ "','"
				+ EingangsrechnungFac.STATUS_ERLEDIGT + "') AND ";

		if (bZusatzkosten) {
			s += " er.eingangsrechnungart_c_nr ='"
					+ EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND ";
		} else {
			s += " er.eingangsrechnungart_c_nr <>'"
					+ EINGANGSRECHNUNGART_ZUSATZKOSTEN + "' AND ";
		}

		if (sKriteriumDatum.equals(EingangsrechnungFac.KRIT_DATUM_BELEGDATUM)) {

			s += " er.t_belegdatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcVon
							.getTimeInMillis()))
					+ "' AND er.t_belegdatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcBis
							.getTimeInMillis())) + "'";

		} else {
			s += " er.t_freigabedatum>='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcVon
							.getTimeInMillis()))
					+ "' AND er.t_freigabedatum<'"
					+ Helper.formatDateWithSlashes(new java.sql.Date(gcBis
							.getTimeInMillis())) + "'";

		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		org.hibernate.Query queryErz = session.createQuery(s);

		List<?> resultList = queryErz.list();

		Iterator resultListIterator = resultList.iterator();
		BigDecimal bdSumme = new BigDecimal(0);
		while (resultListIterator.hasNext()) {

			Object[] o = (Object[]) resultListIterator.next();
			FLREingangsrechnung er = (FLREingangsrechnung) o[0];

			BigDecimal bdBezahlt = new BigDecimal(0);
			if (o[1] != null) {
				bdBezahlt = (BigDecimal) o[1];
			}
			BigDecimal bdBezahltUst = new BigDecimal(0);
			if (o[2] != null) {
				bdBezahltUst = (BigDecimal) o[2];
			}
			// rechnungsbetrag netto
			bdSumme = bdSumme.add(er.getN_betrag()
					.subtract(er.getN_ustbetrag()));
			// bereits bezahlt netto
			BigDecimal bdBezahltNetto = bdBezahlt.subtract(bdBezahltUst);
			bdSumme = bdSumme.subtract(bdBezahltNetto);

		}

		return bdSumme;
	}

	public BigDecimal berechneSummeUmsatzBrutto(TheClientDto theClientDto,
			String sKriteriumDatum, String sKriteriumJahr,
			String sKriteriumWaehrung, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten)
			throws EJBExceptionLP {
		EingangsrechnungDto[] erDtos = getEingangsrechnungen(theClientDto,
				sKriteriumDatum, sKriteriumJahr, gcVon, gcBis);
		BigDecimal bdSumme = new BigDecimal(0);
		for (int i = 0; i < erDtos.length; i++) {
			if (erDtos[i].getStatusCNr().equals(
					EingangsrechnungFac.STATUS_ANGELEGT)
					||
					// erDtos[i].getStatusCNr().equals(EingangsrechnungFac.
					// STATUS_VERBUCHT) ||
					erDtos[i].getStatusCNr().equals(
							EingangsrechnungFac.STATUS_TEILBEZAHLT)
					|| erDtos[i].getStatusCNr().equals(
							EingangsrechnungFac.STATUS_ERLEDIGT)) {

				if (bZusatzkosten) {
					if (erDtos[i]
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
						bdSumme = bdSumme.add(erDtos[i].getNBetrag());
					}
				} else {
					if (!erDtos[i]
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
						bdSumme = bdSumme.add(erDtos[i].getNBetrag());
					}
				}

			}
		}
		try {
			String sMandantWaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getWaehrungCNr();
			// noetigenfalls in die gewuenschte Waehrung umrechnen
			if (!sMandantWaehrung.equals(sKriteriumWaehrung)) {
				// TODO: AD woher kommt Datum
				bdSumme = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						bdSumme, sMandantWaehrung, sKriteriumWaehrung,
						new Date(gcBis.getTimeInMillis()), theClientDto);
			}
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		}
		return bdSumme;
	}

	public BigDecimal berechneSummeUmsatzNetto(TheClientDto theClientDto,
			String sKriteriumDatum, String sKriteriumJahr,
			String sKriteriumWaehrung, GregorianCalendar gcVon,
			GregorianCalendar gcBis, boolean bZusatzkosten)
			throws EJBExceptionLP {
		EingangsrechnungDto[] erDtos = getEingangsrechnungen(theClientDto,
				sKriteriumDatum, sKriteriumJahr, gcVon, gcBis);
		BigDecimal bdSumme = new BigDecimal(0);
		for (int i = 0; i < erDtos.length; i++) {
			if (erDtos[i].getStatusCNr().equals(
					EingangsrechnungFac.STATUS_ANGELEGT)
					||
					// erDtos[i].getStatusCNr().equals(EingangsrechnungFac.
					// STATUS_VERBUCHT) ||
					erDtos[i].getStatusCNr().equals(
							EingangsrechnungFac.STATUS_TEILBEZAHLT)
					|| erDtos[i].getStatusCNr().equals(
							EingangsrechnungFac.STATUS_ERLEDIGT)) {

				if (bZusatzkosten) {
					if (erDtos[i]
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
						bdSumme = bdSumme.add(erDtos[i].getNBetrag().subtract(
								erDtos[i].getNUstBetrag()));
					}
				} else {
					if (!erDtos[i]
							.getEingangsrechnungartCNr()
							.equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
						bdSumme = bdSumme.add(erDtos[i].getNBetrag().subtract(
								erDtos[i].getNUstBetrag()));
					}
				}

			}
		}
		try {
			String sMandantWaehrung = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto).getWaehrungCNr();
			// noetigenfalls in die gewuenschte Waehrung umrechnen
			if (!sMandantWaehrung.equals(sKriteriumWaehrung)) {
				// TODO: AD woher kommt Datum
				bdSumme = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						bdSumme, sMandantWaehrung, sKriteriumWaehrung,
						new Date(gcBis.getTimeInMillis()), theClientDto);
			}
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		}
		return bdSumme;
	}

	private EingangsrechnungDto[] getEingangsrechnungen(
			TheClientDto theClientDto, String sKriteriumDatum,
			String sKriteriumJahr, GregorianCalendar gcVon,
			GregorianCalendar gcBis) {

		EingangsrechnungDto[] erDtos = null;
		// nach belegdatum
		if (sKriteriumDatum.equals(EingangsrechnungFac.KRIT_DATUM_BELEGDATUM)) {
			erDtos = eingangsrechnungFindByBelegdatumVonBis(
					theClientDto.getMandant(),
					new java.sql.Date(gcVon.getTimeInMillis()),
					new java.sql.Date(gcBis.getTimeInMillis()));
		}
		// nach freigabedatum
		else {
			erDtos = eingangsrechnungFindByFreigabedatumVonBis(
					theClientDto.getMandant(),
					new java.sql.Date(gcVon.getTimeInMillis()),
					new java.sql.Date(gcBis.getTimeInMillis()));
		}
		return erDtos;
	}

	public void toggleWiederholendErledigt(Integer eingangsrechnungIId,
			TheClientDto theClientDto) {
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);

		if (eingangsrechnung.getAuftragwiederholungsintervallCNr() == null
				|| (eingangsrechnung.getAuftragwiederholungsintervallCNr() != null && eingangsrechnung
						.getEingangsrechnungIIdNachfolger() != null)) {
			// Wiederholend erledigt kann nur gesetzt werden, wenn noch kein
			// Nachfolger
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT,
					new Exception(
							"FEHLER_ZUSATZKOSTEN_FEHLER_WIEDERHOLUNGERLEDIGT"));

		}

		if (eingangsrechnung.getTWiederholenderledigt() == null) {
			eingangsrechnung.setTWiederholenderledigt(new Timestamp(System
					.currentTimeMillis()));
			eingangsrechnung.setPersonalIIdWiederholenderledigt(theClientDto
					.getIDPersonal());
		} else {
			eingangsrechnung.setTWiederholenderledigt(null);
			eingangsrechnung.setPersonalIIdWiederholenderledigt(null);
		}
	}

	public void toggleZollimportpapiereErhalten(Integer eingangsrechnungIId,
			String cZollimportpapier, Integer eingangsrechnungIId_Zollimport,
			TheClientDto theClientDto) {
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);

		EingangsrechnungDto erDto_vorher = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		EingangsrechnungDto erDto_nachher = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);

		if (eingangsrechnung.getTZollimportpapier() == null) {
			eingangsrechnung.setTZollimportpapier(new Timestamp(System
					.currentTimeMillis()));
			eingangsrechnung.setPersonalIIdZollimportpapier(theClientDto
					.getIDPersonal());
			eingangsrechnung.setCZollimportpapier(cZollimportpapier);
			eingangsrechnung
					.setEingangsrechnungIdZollimport(eingangsrechnungIId_Zollimport);
			// Logging
			erDto_nachher.setTZollimportpapier(new Timestamp(System
					.currentTimeMillis()));
			erDto_nachher.setPersonalIIdZollimportpapier(theClientDto
					.getIDPersonal());
			erDto_nachher.setCZollimportpapier(cZollimportpapier);
			erDto_nachher
					.setEingangsrechnungIdZollimport(eingangsrechnungIId_Zollimport);

		} else {
			eingangsrechnung.setTZollimportpapier(null);
			eingangsrechnung.setPersonalIIdZollimportpapier(null);
			eingangsrechnung.setCZollimportpapier(null);
			eingangsrechnung.setEingangsrechnungIdZollimport(null);

			erDto_nachher.setTZollimportpapier(null);
			erDto_nachher.setPersonalIIdZollimportpapier(null);
			erDto_nachher.setCZollimportpapier(null);
			erDto_nachher.setEingangsrechnungIdZollimport(null);

		}

		HvDtoLogger<EingangsrechnungDto> erLogger = new HvDtoLogger<EingangsrechnungDto>(
				em, erDto_vorher.getIId(), theClientDto);
		erLogger.log(erDto_vorher, erDto_nachher);

	}

	/**
	 * Ueberpruefen der Vorzeichen der Betraege. Zum Auseinanderhalten von
	 * Gutschriften und anderen ER.
	 * 
	 * @param erDto
	 *            EingangsrechnungDto
	 */
	private void pruefeBetraege(EingangsrechnungDto erDto) {
		BigDecimal bdZero = new BigDecimal(0);
		if (erDto.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			if (erDto.getNBetrag().compareTo(bdZero) >= 0) {
				erDto.setNBetrag(erDto.getNBetrag().negate());
			}
			if (erDto.getNBetragfw().compareTo(bdZero) >= 0) {
				erDto.setNBetragfw(erDto.getNBetragfw().negate());
			}
			if (erDto.getNUstBetrag().compareTo(bdZero) >= 0) {
				erDto.setNUstBetrag(erDto.getNUstBetrag().negate());
			}
			if (erDto.getNUstBetragfw().compareTo(bdZero) >= 0) {
				erDto.setNUstBetragfw(erDto.getNUstBetragfw().negate());
			}
		}
	}

	public void updateEingangsrechnungStatus(TheClientDto theClientDto,
			Integer iId, String statusCNr) throws EJBExceptionLP {
		myLogger.logData(iId + " " + statusCNr);
		// nochmal aus der db holen
		EingangsrechnungDto erDto2Update = eingangsrechnungFindByPrimaryKey(iId);
		erDto2Update.setStatusCNr(statusCNr);
		updateEingangsrechnung(erDto2Update, theClientDto);
	}

	public void updateEingangsrechnungGedruckt(TheClientDto theClientDto,
			Integer iId, Timestamp tGedruckt) {
		Eingangsrechnung eingangsrechnung = em
				.find(Eingangsrechnung.class, iId);
		eingangsrechnung.setTGedruckt(tGedruckt);
		em.merge(eingangsrechnung);
		em.flush();
	}

	public EingangsrechnungKontierungDto createEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// erlaubt ??
		pruefeUpdateAufEingangsrechnungErlaubt(
				eingangsrechnungKontierungDto.getEingangsrechnungIId(),
				theClientDto);

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_EINGANGSRECHNUNGKONTIERUNG);
		eingangsrechnungKontierungDto.setIId(iId);
		eingangsrechnungKontierungDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		eingangsrechnungKontierungDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());
		try {
			if (eingangsrechnungKontierungDto.getKostenstelleIId() == null) {
				KostenstelleDto[] kst = getSystemFac()
						.kostenstelleFindByMandant(theClientDto.getMandant());
				if (kst.length > 0) {
					eingangsrechnungKontierungDto.setKostenstelleIId(kst[0]
							.getIId());
				}
			}
			EingangsrechnungKontierung eingangsrechnungKontierung = new EingangsrechnungKontierung(
					eingangsrechnungKontierungDto.getIId(),
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					eingangsrechnungKontierungDto.getNBetrag(),
					eingangsrechnungKontierungDto.getNBetragUst(),
					eingangsrechnungKontierungDto.getMwstsatzIId(),
					eingangsrechnungKontierungDto.getKostenstelleIId(),
					eingangsrechnungKontierungDto.getKontoIId(),
					eingangsrechnungKontierungDto.getPersonalIIdAnlegen(),
					eingangsrechnungKontierungDto.getPersonalIIdAendern());
			em.persist(eingangsrechnungKontierung);
			em.flush();
			eingangsrechnungKontierungDto
					.setTAendern(eingangsrechnungKontierung.getTAendern());
			eingangsrechnungKontierungDto
					.setTAnlegen(eingangsrechnungKontierung.getTAnlegen());
			setEingangsrechnungKontierungFromEingangsrechnungKontierungDto(
					eingangsrechnungKontierung, eingangsrechnungKontierungDto);
			updateKontierung(eingangsrechnungKontierungDto
					.getEingangsrechnungIId());
			pruefeZugeordneteKontenAufGleichesFinanzamt(
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					theClientDto);
			// verbuchen versuchen (dort wird auf vollstaendig gepueft)
			verbucheEingangsrechnung(
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					null, theClientDto);
			return eingangsrechnungKontierungDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (eingangsrechnungKontierungDto != null) {
			// erlaubt ??
			pruefeUpdateAufEingangsrechnungErlaubt(
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					theClientDto);
			Integer iId = eingangsrechnungKontierungDto.getIId();
			EingangsrechnungKontierung toRemove = em.find(
					EingangsrechnungKontierung.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
			updateKontierung(eingangsrechnungKontierungDto
					.getEingangsrechnungIId());
			verbucheEingangsrechnung(
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					null, theClientDto);
		}
	}

	public EingangsrechnungKontierungDto updateEingangsrechnungKontierung(
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungKontierungDto != null) {
			// erlaubt ??
			pruefeUpdateAufEingangsrechnungErlaubt(
					eingangsrechnungKontierungDto.getEingangsrechnungIId(),
					theClientDto);
			eingangsrechnungKontierungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			eingangsrechnungKontierungDto.setTAendern(getTimestamp());
			Integer iId = eingangsrechnungKontierungDto.getIId();
			try {
				EingangsrechnungKontierung eingangsrechnungKontierung = em
						.find(EingangsrechnungKontierung.class, iId);
				setEingangsrechnungKontierungFromEingangsrechnungKontierungDto(
						eingangsrechnungKontierung,
						eingangsrechnungKontierungDto);
				updateKontierung(eingangsrechnungKontierungDto
						.getEingangsrechnungIId());
				pruefeZugeordneteKontenAufGleichesFinanzamt(
						eingangsrechnungKontierungDto.getEingangsrechnungIId(),
						theClientDto);
				verbucheEingangsrechnung(
						eingangsrechnungKontierung.getEingangsrechnungIId(),
						null, theClientDto);
				return eingangsrechnungKontierungDto;
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		} else {
			return null;
		}
	}

	public EingangsrechnungKontierungDto eingangsrechnungKontierungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		try {
			EingangsrechnungKontierung eingangsrechnungKontierung = em.find(
					EingangsrechnungKontierung.class, iId);
			if (eingangsrechnungKontierung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleEingangsrechnungKontierungDto(eingangsrechnungKontierung);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setEingangsrechnungKontierungFromEingangsrechnungKontierungDto(
			EingangsrechnungKontierung eingangsrechnungKontierung,
			EingangsrechnungKontierungDto eingangsrechnungKontierungDto) {
		eingangsrechnungKontierung
				.setEingangsrechnungIId(eingangsrechnungKontierungDto
						.getEingangsrechnungIId());
		eingangsrechnungKontierung.setNBetrag(eingangsrechnungKontierungDto
				.getNBetrag());
		eingangsrechnungKontierung.setNBetragust(eingangsrechnungKontierungDto
				.getNBetragUst());
		eingangsrechnungKontierung.setMwstsatzIId(eingangsrechnungKontierungDto
				.getMwstsatzIId());
		eingangsrechnungKontierung
				.setKostenstelleIId(eingangsrechnungKontierungDto
						.getKostenstelleIId());
		eingangsrechnungKontierung.setKontoIId(eingangsrechnungKontierungDto
				.getKontoIId());
		eingangsrechnungKontierung
				.setPersonalIIdAnlegen(eingangsrechnungKontierungDto
						.getPersonalIIdAnlegen());
		eingangsrechnungKontierung.setTAnlegen(eingangsrechnungKontierungDto
				.getTAnlegen());
		eingangsrechnungKontierung
				.setPersonalIIdAendern(eingangsrechnungKontierungDto
						.getPersonalIIdAendern());
		eingangsrechnungKontierung.setTAendern(eingangsrechnungKontierungDto
				.getTAendern());
		em.merge(eingangsrechnungKontierung);
		em.flush();
	}

	private EingangsrechnungKontierungDto assembleEingangsrechnungKontierungDto(
			EingangsrechnungKontierung eingangsrechnungKontierung) {
		return EingangsrechnungKontierungDtoAssembler
				.createDto(eingangsrechnungKontierung);
	}

	private EingangsrechnungKontierungDto[] assembleEingangsrechnungKontierungDtos(
			Collection<?> eingangsrechnungKontierungs) {
		List<EingangsrechnungKontierungDto> list = new ArrayList<EingangsrechnungKontierungDto>();
		if (eingangsrechnungKontierungs != null) {
			Iterator<?> iterator = eingangsrechnungKontierungs.iterator();
			while (iterator.hasNext()) {
				EingangsrechnungKontierung eingangsrechnungKontierung = (EingangsrechnungKontierung) iterator
						.next();
				list.add(assembleEingangsrechnungKontierungDto(eingangsrechnungKontierung));
			}
		}
		EingangsrechnungKontierungDto[] returnArray = new EingangsrechnungKontierungDto[list
				.size()];
		return (EingangsrechnungKontierungDto[]) list.toArray(returnArray);
	}

	public EingangsrechnungzahlungDto createEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			Boolean bErledigt, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_EINGANGSRECHNUNGKONTIERUNG);
			eingangsrechnungzahlungDto.setIId(iId);
			eingangsrechnungzahlungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			eingangsrechnungzahlungDto.setPersonalIIdAnlegen(theClientDto
					.getIDPersonal());

			// PJ 16326 updateEingangsrechnung(erDto, theClientDto);
			// zahlung erstellen
			Eingangsrechnungzahlung eingangsrechnungzahlung = new Eingangsrechnungzahlung(
					eingangsrechnungzahlungDto.getIId(),
					eingangsrechnungzahlungDto.getEingangsrechnungIId(),
					eingangsrechnungzahlungDto.getTZahldatum(),
					eingangsrechnungzahlungDto.getZahlungsartCNr(),
					eingangsrechnungzahlungDto.getNKurs(),
					eingangsrechnungzahlungDto.getNBetrag(),
					eingangsrechnungzahlungDto.getNBetragfw(),
					eingangsrechnungzahlungDto.getNBetragust(),
					eingangsrechnungzahlungDto.getNBetragustfw(),
					eingangsrechnungzahlungDto.getPersonalIIdAnlegen(),
					eingangsrechnungzahlungDto.getPersonalIIdAendern(),
					eingangsrechnungzahlungDto.getBKursuebersteuert());
			em.persist(eingangsrechnungzahlung);
			em.flush();
			eingangsrechnungzahlungDto.setTAendern(eingangsrechnungzahlung
					.getTAendern());
			eingangsrechnungzahlungDto.setTAnlegen(eingangsrechnungzahlung
					.getTAnlegen());

			// er updaten
			// PJ 16326
			Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
					eingangsrechnungzahlungDto.getEingangsrechnungIId());

			if (bErledigt == null) {

				BigDecimal bereitsBezahlt = getBezahltBetragFw(
						eingangsrechnungzahlungDto.getEingangsrechnungIId(),
						null);

				if (bereitsBezahlt.doubleValue() >= eingangsrechnung
						.getNBetragfw().doubleValue()) {
					eingangsrechnung
							.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
					eingangsrechnung
							.setTBezahltdatum(eingangsrechnungzahlungDto
									.getTZahldatum());
				} else {
					eingangsrechnung
							.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				}
			} else {

				if (bErledigt == true) {
					eingangsrechnung
							.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
					eingangsrechnung
							.setTBezahltdatum(eingangsrechnungzahlungDto
									.getTZahldatum());
				} else {
					eingangsrechnung
							.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
				}
			}

			em.merge(eingangsrechnung);
			em.flush();

			setEingangsrechnungzahlungFromEingangsrechnungzahlungDto(
					eingangsrechnungzahlung, eingangsrechnungzahlungDto);

			if (!eingangsrechnungzahlungDto.getZahlungsartCNr().equals(
					RechnungFac.ZAHLUNGSART_GEGENVERRECHNUNG)) {
				// SP 1676 nur wenn keine Gegenverrechung, da hat das Anlegen
				// der Rechnungszahlung schon gebucht
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheZahlungEr(eingangsrechnungzahlungDto.getIId(),
								theClientDto);
			}

			Integer kontoIId = getLieferantFac().lieferantFindByPrimaryKey(
					eingangsrechnung.getLieferantIId(), theClientDto)
					.getKontoIIdKreditorenkonto();

			List<BelegbuchungDto> belegbuchungDtos = getBelegbuchungFac(
					theClientDto.getMandant())
					.getAlleBelegbuchungenInklZahlungenER(
							eingangsrechnung.getIId());
			getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungenAusziffernWennNoetig(kontoIId,
							belegbuchungDtos);

			return eingangsrechnungzahlungDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = null;
		if (eingangsrechnungzahlungDto != null) {
			iId = eingangsrechnungzahlungDto.getIId();
			if (iId != null)
				pruefeUpdateAufEingangsrechnungZahlungErlaubt(iId, theClientDto);
			try {
				Eingangsrechnungzahlung toRemove = em.find(
						Eingangsrechnungzahlung.class, iId);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
							"Zahlung zum L\u00F6schen nicht gefunden. ID="
									+ iId);
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						ex);
			}
		}

		// vorher buchungen loeschen
		getBelegbuchungFac(theClientDto.getMandant())
				.verbucheZahlungErRueckgaengig(eingangsrechnungzahlungDto,
						theClientDto);

		// er updaten
		EingangsrechnungzahlungDto[] zahlungen = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungzahlungDto
				.getEingangsrechnungIId());

		// PJ 16326
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungzahlungDto.getEingangsrechnungIId());

		if (zahlungen == null || zahlungen.length == 0) {
			eingangsrechnung.setStatusCNr(EingangsrechnungFac.STATUS_ANGELEGT);
			eingangsrechnung.setTBezahltdatum(null);
		} else {
			eingangsrechnung
					.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
			eingangsrechnung.setTBezahltdatum(null);
		}
		em.merge(eingangsrechnung);
		em.flush();
	}

	public EingangsrechnungzahlungDto updateEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto,
			Boolean bErledigt, TheClientDto theClientDto) throws EJBExceptionLP {
		if (eingangsrechnungzahlungDto != null) {

			pruefeUpdateAufEingangsrechnungZahlungErlaubt(
					eingangsrechnungzahlungDto.getIId(), theClientDto);

			eingangsrechnungzahlungDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			eingangsrechnungzahlungDto.setTAendern(getTimestamp());
			Integer iId = eingangsrechnungzahlungDto.getIId();
			try {

				// zahlung erstellen
				Eingangsrechnungzahlung eingangsrechnungzahlung = em.find(
						Eingangsrechnungzahlung.class, iId);
				setEingangsrechnungzahlungFromEingangsrechnungzahlungDto(
						eingangsrechnungzahlung, eingangsrechnungzahlungDto);

				BigDecimal bereitsBezahlt = getBezahltBetragFw(
						eingangsrechnungzahlungDto.getEingangsrechnungIId(),
						null);

				// er updaten
				// PJ 16326
				Eingangsrechnung eingangsrechnung = em.find(
						Eingangsrechnung.class,
						eingangsrechnungzahlungDto.getEingangsrechnungIId());

				if (bErledigt == null) {

					if (bereitsBezahlt.doubleValue() >= eingangsrechnung
							.getNBetragfw().doubleValue()) {
						eingangsrechnung
								.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
						eingangsrechnung
								.setTBezahltdatum(eingangsrechnungzahlungDto
										.getTZahldatum());
					} else {
						eingangsrechnung
								.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
					}
				} else {
					if (bErledigt == true) {
						eingangsrechnung
								.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
						eingangsrechnung
								.setTBezahltdatum(eingangsrechnungzahlungDto
										.getTZahldatum());
					} else {
						eingangsrechnung
								.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
					}
				}

				em.merge(eingangsrechnung);
				em.flush();

				// Nach Ruecksprache mit AD wird die Belegbuchung automatisch
				// entfernt, 10.04.2012 (ghp)
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheZahlungErRueckgaengig(
								eingangsrechnungzahlungDto, theClientDto);

				// Zahlung verbuchen
				getBelegbuchungFac(theClientDto.getMandant())
						.verbucheZahlungEr(eingangsrechnungzahlungDto.getIId(),
								theClientDto);

				return eingangsrechnungzahlungDto;
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
		// null
		return eingangsrechnungzahlungDto;
	}

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		try {
			Eingangsrechnungzahlung eingangsrechnungzahlung = em.find(
					Eingangsrechnungzahlung.class, iId);
			if (eingangsrechnungzahlung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleEingangsrechnungzahlungDto(eingangsrechnungzahlung);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public void updateEingangsrechnungMahndaten(
			Integer eingangsrechnungrechnungIId, Integer mahnstufeIId,
			Timestamp tMahndatum) {

		try {
			Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
					eingangsrechnungrechnungIId);
			eingangsrechnung.setMahnstufeIId(mahnstufeIId);
			eingangsrechnung.setTMahndatum(tMahndatum);
			em.merge(eingangsrechnung);
			em.flush();

		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}

	}

	public EingangsrechnungzahlungDto eingangsrechnungzahlungFindByRechnungzahlungIId(
			Integer rechnungzahlungIId) {
		Query query = em
				.createNamedQuery("EingangsrechnungzahlungfindByRechnungzahlungIId");
		query.setParameter(1, rechnungzahlungIId);
		Eingangsrechnungzahlung eingangsrechnungzahlungDto = (Eingangsrechnungzahlung) query
				.getSingleResult();

		return assembleEingangsrechnungzahlungDto(eingangsrechnungzahlungDto);
	}

	private void setEingangsrechnungzahlungFromEingangsrechnungzahlungDto(
			Eingangsrechnungzahlung eingangsrechnungzahlung,
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto) {
		eingangsrechnungzahlung
				.setEingangsrechnungIId(eingangsrechnungzahlungDto
						.getEingangsrechnungIId());
		eingangsrechnungzahlung.setTZahldatum(eingangsrechnungzahlungDto
				.getTZahldatum());
		eingangsrechnungzahlung.setZahlungsartCNr(eingangsrechnungzahlungDto
				.getZahlungsartCNr());
		eingangsrechnungzahlung.setBankverbindungIId(eingangsrechnungzahlungDto
				.getBankverbindungIId());
		eingangsrechnungzahlung.setKassenbuchIId(eingangsrechnungzahlungDto
				.getKassenbuchIId());
		eingangsrechnungzahlung.setIAuszug(eingangsrechnungzahlungDto
				.getIAuszug());
		eingangsrechnungzahlung.setNKurs(eingangsrechnungzahlungDto.getNKurs());
		eingangsrechnungzahlung.setBkursuebersteuert(eingangsrechnungzahlungDto
				.getBKursuebersteuert());
		eingangsrechnungzahlung.setNBetrag(eingangsrechnungzahlungDto
				.getNBetrag());
		eingangsrechnungzahlung.setNBetragfw(eingangsrechnungzahlungDto
				.getNBetragfw());
		eingangsrechnungzahlung.setNBetragust(eingangsrechnungzahlungDto
				.getNBetragust());
		eingangsrechnungzahlung.setNBetragustfw(eingangsrechnungzahlungDto
				.getNBetragustfw());
		eingangsrechnungzahlung.setTAnlegen(eingangsrechnungzahlungDto
				.getTAnlegen());
		eingangsrechnungzahlung
				.setPersonalIIdAnlegen(eingangsrechnungzahlungDto
						.getPersonalIIdAnlegen());
		eingangsrechnungzahlung.setTAendern(eingangsrechnungzahlungDto
				.getTAendern());
		eingangsrechnungzahlung
				.setPersonalIIdAendern(eingangsrechnungzahlungDto
						.getPersonalIIdAendern());
		eingangsrechnungzahlung
				.setEingangsrechnungzahlungIIdGutschrift(eingangsrechnungzahlungDto
						.getEingangsrechnungIIdGutschriftZahlung());
		eingangsrechnungzahlung
				.setEingangsrechnungIIdGutschrift(eingangsrechnungzahlungDto
						.getEingangsrechnungIIdGutschrift());
		eingangsrechnungzahlung
				.setRechnungzahlungIId(eingangsrechnungzahlungDto
						.getRechnungzahlungIId());
		eingangsrechnungzahlung.setBuchungdetailIId(eingangsrechnungzahlungDto
				.getBuchungdetailIId());
		em.merge(eingangsrechnungzahlung);
		em.flush();
	}

	private EingangsrechnungzahlungDto assembleEingangsrechnungzahlungDto(
			Eingangsrechnungzahlung eingangsrechnungzahlung) {
		return EingangsrechnungzahlungDtoAssembler
				.createDto(eingangsrechnungzahlung);
	}

	private EingangsrechnungzahlungDto[] assembleEingangsrechnungzahlungDtos(
			Collection<?> eingangsrechnungzahlungs) {
		List<EingangsrechnungzahlungDto> list = new ArrayList<EingangsrechnungzahlungDto>();
		if (eingangsrechnungzahlungs != null) {
			Iterator<?> iterator = eingangsrechnungzahlungs.iterator();
			while (iterator.hasNext()) {
				Eingangsrechnungzahlung eingangsrechnungzahlung = (Eingangsrechnungzahlung) iterator
						.next();
				list.add(assembleEingangsrechnungzahlungDto(eingangsrechnungzahlung));
			}
		}
		EingangsrechnungzahlungDto[] returnArray = new EingangsrechnungzahlungDto[list
				.size()];
		return (EingangsrechnungzahlungDto[]) list.toArray(returnArray);
	}

	public EingangsrechnungzahlungDto[] eingangsrechnungzahlungFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("EingangsrechnungzahlungfindByEingangsrechnungIId");
		query.setParameter(1, eingangsrechnungIId);
		Collection<?> eingangsrechnungzahlungDtos = query.getResultList();
		// if(eingangsrechnungzahlungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleEingangsrechnungzahlungDtos(eingangsrechnungzahlungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public EingangsrechnungzahlungDto[] eingangsrechnungzahlungFindAll()
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("EingangsrechnungzahlungfindAll");
		Collection<?> eingangsrechnungzahlungDtos = query.getResultList();
		// if(eingangsrechnungzahlungDtos.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleEingangsrechnungzahlungDtos(eingangsrechnungzahlungDtos);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	public BigDecimal getBezahltBetrag(Integer eingangsrechnungIId,
			Integer kontierungIId) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String s = "from FLREingangsrechnungzahlung zahlung WHERE zahlung.eingangsrechnung_i_id="
				+ eingangsrechnungIId + "";

		org.hibernate.Query queryErz = session.createQuery(s);

		List<?> resultList = queryErz.list();

		Iterator resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnungzahlung zahlung = (FLREingangsrechnungzahlung) resultListIterator
					.next();
			wert = wert.add(zahlung.getN_betrag());
		}
		session.close();

		// PJ 16330
		if (kontierungIId != null) {

			BigDecimal bdGesmtwert = eingangsrechnungFindByPrimaryKey(
					eingangsrechnungIId).getNBetrag();
			Query query = em
					.createNamedQuery("EingangsrechnungKontierungfindByEingangsrechnungIIdOrderByNBetrag");
			query.setParameter(1, eingangsrechnungIId);
			Collection<?> eingangsrechnungKontierungDtos = query
					.getResultList();
			EingangsrechnungKontierungDto[] erkDtos = assembleEingangsrechnungKontierungDtos(eingangsrechnungKontierungDtos);

			if (erkDtos.length > 1 && bdGesmtwert.doubleValue() != 0) {
				BigDecimal werteBisher = new BigDecimal(0);
				for (int i = 0; i < erkDtos.length; i++) {
					BigDecimal wertKontierung = wert.divide(bdGesmtwert, 4,
							BigDecimal.ROUND_HALF_EVEN).multiply(
							erkDtos[i].getNBetrag());

					if (kontierungIId.equals(erkDtos[i].getIId())) {
						if (i == erkDtos.length - 1) {
							return wert.subtract(werteBisher);
						} else {
							return wertKontierung;
						}
					}

					werteBisher = werteBisher.add(wertKontierung);
				}

			}
		}

		return wert;
	}

	public BigDecimal getBezahltBetragUst(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();

		String s = "from FLREingangsrechnungzahlung zahlung WHERE zahlung.eingangsrechnung_i_id="
				+ eingangsrechnungIId + "";

		org.hibernate.Query queryErz = session.createQuery(s);

		List<?> resultList = queryErz.list();

		Iterator resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLREingangsrechnungzahlung zahlung = (FLREingangsrechnungzahlung) resultListIterator
					.next();

			if (zahlungIIdAusgenommen != null
					&& zahlungIIdAusgenommen.equals(zahlung.getI_id())) {
				break;
			}

			if (zahlungIIdAusgenommen == null
					|| !zahlungIIdAusgenommen.equals(zahlung.getI_id())) {

				wert = wert.add(zahlung.getN_betrag_ust());

			}
		}
		session.close();
		return wert;
	}

	public BigDecimal getBezahltBetragFwKontierung(Integer eingangsrechnungIId,
			Integer kontierungIId) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		EingangsrechnungzahlungDto[] erzDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < erzDtos.length; i++) {

			wert = wert.add(erzDtos[i].getNBetragfw());

		}

		// PJ 16330
		if (kontierungIId != null) {

			BigDecimal bdGesmtwert = eingangsrechnungFindByPrimaryKey(
					eingangsrechnungIId).getNBetragfw();
			Query query = em
					.createNamedQuery("EingangsrechnungKontierungfindByEingangsrechnungIIdOrderByNBetrag");
			query.setParameter(1, eingangsrechnungIId);
			Collection<?> eingangsrechnungKontierungDtos = query
					.getResultList();
			EingangsrechnungKontierungDto[] erkDtos = assembleEingangsrechnungKontierungDtos(eingangsrechnungKontierungDtos);

			if (erkDtos.length > 1 && bdGesmtwert.doubleValue() != 0) {
				BigDecimal werteBisher = new BigDecimal(0);
				for (int i = 0; i < erkDtos.length; i++) {
					BigDecimal wertKontierung = wert.divide(bdGesmtwert, 4,
							BigDecimal.ROUND_HALF_EVEN).multiply(
							erkDtos[i].getNBetrag());

					if (kontierungIId.equals(erkDtos[i].getIId())) {
						if (i == erkDtos.length - 1) {
							return wert.subtract(werteBisher);
						} else {
							return wertKontierung;
						}
					}

					werteBisher = werteBisher.add(wertKontierung);
				}

			}
		}

		return wert;
	}

	public BigDecimal getBezahltBetragUstFw(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		EingangsrechnungzahlungDto[] erzDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < erzDtos.length; i++) {

			if (zahlungIIdAusgenommen != null
					&& zahlungIIdAusgenommen.equals(erzDtos[i].getIId())) {
				break;
			}

			if (zahlungIIdAusgenommen == null
					|| !zahlungIIdAusgenommen.equals(erzDtos[i].getIId())) {

				wert = wert.add(erzDtos[i].getNBetragustfw());

			}
		}
		return wert;
	}

	/**
	 * Hole die Bankverbindung, die zuletzt zur bezahlung einer bestimmten
	 * eingangsrechnung verwendet wurde.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @return BankverbindungDto
	 * @throws EJBExceptionLP
	 */
	public BankverbindungDto getZuletztVerwendeteBankverbindung(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		try {
			EingangsrechnungzahlungDto[] zahlungen = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
			Integer bankverbindungIId = null;
			Date dLetztesZahldatum = null;
			for (int i = 0; i < zahlungen.length; i++) {
				if ((dLetztesZahldatum == null || dLetztesZahldatum
						.before(zahlungen[i].getTZahldatum()))
						&& zahlungen[i].getBankverbindungIId() != null) {
					dLetztesZahldatum = zahlungen[i].getTZahldatum();
					bankverbindungIId = zahlungen[i].getBankverbindungIId();
				}
			}
			BankverbindungDto bvDto = null;
			if (bankverbindungIId != null) {
				bvDto = getFinanzFac().bankverbindungFindByPrimaryKey(
						bankverbindungIId);
			}
			return bvDto;
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	/**
	 * Die letzte Zahlung (nach Zahldatum) zu einer ER finden.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @return BankverbindungDto
	 * @throws EJBExceptionLP
	 */
	public EingangsrechnungzahlungDto getLetzteZahlung(
			Integer eingangsrechnungIId) throws EJBExceptionLP {
		try {
			EingangsrechnungzahlungDto[] zahlungen = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
			EingangsrechnungzahlungDto letzteZahlung = null;
			Date dLetztesZahldatum = null;
			for (int i = 0; i < zahlungen.length; i++) {
				if ((dLetztesZahldatum == null || dLetztesZahldatum
						.before(zahlungen[i].getTZahldatum()))) {
					dLetztesZahldatum = zahlungen[i].getTZahldatum();
					letzteZahlung = zahlungen[i];
				}
			}
			return letzteZahlung;
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	public Integer getAuszugDerLetztenZahlung(Integer eingangsrechnungIId)
			throws EJBExceptionLP {
		try {
			EingangsrechnungzahlungDto[] zahlungen = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
			Date dLetztesZahldatum = null;
			Integer sAuszug = null;
			for (int i = 0; i < zahlungen.length; i++) {
				if ((dLetztesZahldatum == null || dLetztesZahldatum
						.before(zahlungen[i].getTZahldatum()))
						&& zahlungen[i].getBankverbindungIId() != null) {
					dLetztesZahldatum = zahlungen[i].getTZahldatum();
					sAuszug = zahlungen[i].getIAuszug();
				}
			}
			return sAuszug;
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}

	public BigDecimal berechneAnzahlungswertVonBestellungInMandantWaehrung(
			Integer bestellungIId) {
		BigDecimal bdWert = new BigDecimal(0);
		EingangsrechnungDto[] erDtos = eingangsrechnungFindByBestellungIId(bestellungIId);
		for (int i = 0; i < erDtos.length; i++) {
			bdWert = bdWert.add(erDtos[i].getNBetrag());
		}
		return bdWert;
	}

	/**
	 * Den Wechselkurs zwischen Eingangsrechnungswaehrung und Mandantwaehrung
	 * fuer eine bestimmte Eigangsrechnung bestimmen.
	 * 
	 * @param iIdEingangsrechnungI
	 *            PK der Eingangsrechnung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return der Wechselkurs
	 * @throws EJBExceptionLP
	 */
	public Double getWechselkursEingangsrechnungswaehrungZuMandantwaehrung(
			Integer iIdEingangsrechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Eingangsrechnung eingangsrechnung = null;

		eingangsrechnung = em
				.find(Eingangsrechnung.class, iIdEingangsrechnungI);
		if (eingangsrechnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		Double ddWechselkurs = new Double(eingangsrechnung.getNKurs()
				.doubleValue());

		return ddWechselkurs;
	}

	public void manuellErledigen(Integer iIdEingangsrechnungI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(iIdEingangsrechnungI);
		// try {
		Eingangsrechnung rechnung = em.find(Eingangsrechnung.class,
				iIdEingangsrechnungI);
		if (rechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (rechnung.getStatusCNr().equals(EingangsrechnungFac.STATUS_ANGELEGT)
				||
				// rechnung.getStatusCNr().equals(EingangsrechnungFac.
				// STATUS_VERBUCHT) ||
				rechnung.getStatusCNr().equals(
						EingangsrechnungFac.STATUS_TEILBEZAHLT)) {
			rechnung.setStatusCNr(EingangsrechnungFac.STATUS_ERLEDIGT);
			rechnung.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			rechnung.setTManuellerledigt(getTimestamp());
			rechnung.setTBezahltdatum(getDate());
		} else if (rechnung.getStatusCNr().equals(
				EingangsrechnungFac.STATUS_ERLEDIGT)) {
			// in diesem Fall die Erledigung zuruecknehmen
			rechnung.setPersonalIIdManuellerledigt(null);
			rechnung.setTManuellerledigt(null);
			rechnung.setTBezahltdatum(null);
			Query query = em
					.createNamedQuery("EingangsrechnungzahlungfindByEingangsrechnungIId");
			query.setParameter(1, iIdEingangsrechnungI);
			if (query.getResultList().isEmpty()) {
				// noch keine zahlungen
				rechnung.setStatusCNr(EingangsrechnungFac.STATUS_ANGELEGT);
			} else {
				// es gibt schon zahlungen
				rechnung.setStatusCNr(EingangsrechnungFac.STATUS_TEILBEZAHLT);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Eingangsrechnung kann nicht manuell erledigt werden, Status : "
									+ rechnung.getStatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Die Summe der in der Kontierung eingegebenen MWST-Betraege in die
	 * Kopfdaten zurueckschreiben.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	private void updateKontierung(Integer eingangsrechnungIId)
			throws EJBExceptionLP {
		EingangsrechnungKontierungDto[] dtos = eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungIId);
		BigDecimal bdMwstSumme = new BigDecimal(0);
		BigDecimal bdMwstSummeUST = new BigDecimal(0);
		for (int i = 0; i < dtos.length; i++) {
			bdMwstSumme = bdMwstSumme.add(dtos[i].getNBetragUst());
		}
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		if (eingangsrechnung == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		eingangsrechnung.setNUstbetragfw(bdMwstSumme);
		bdMwstSummeUST = Helper.rundeKaufmaennisch(
				bdMwstSumme.multiply(eingangsrechnung.getNKurs()), 2);
		eingangsrechnung.setNUstbetrag(bdMwstSummeUST);
	}

	/**
	 * Den Status einer ER von 'Erledigt' auf 'Teilerledigt' setzen. <br>
	 * Diese Aktion ist nur moeglich, wenn der 'Erledigt' Status manuell gesetzt
	 * wurde.
	 * 
	 * @param eingangsrechnungIId
	 *            PK der ER
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void erledigungAufheben(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Eingangsrechnung er = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		if (er == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (er.getStatusCNr().equals(EingangsrechnungFac.STATUS_ERLEDIGT)) {

			String status = EingangsrechnungFac.STATUS_TEILBEZAHLT;
			EingangsrechnungzahlungDto[] erzDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(er
					.getIId());
			if (erzDtos == null || erzDtos.length == 0) {
				status = EingangsrechnungFac.STATUS_ANGELEGT;
			}

			if (er.getPersonalIIdManuellerledigt() != null
					&& er.getTManuellerledigt() != null) {
				er.setStatusCNr(status);
				er.setPersonalIIdManuellerledigt(null);
				er.setTManuellerledigt(null);
			} else {
				// throw new EJBExceptionLP(
				// EJBExceptionLP.FEHLER_BELEG_WURDE_NICHT_MANUELL_ERLEDIGT,
				// new
				// Exception("Dieser Auftrag wurde nicht manuell erledigt"));
				er.setStatusCNr(status);
				myLogger.logKritisch("Status Erledigt wurde aufgehoben, obwohl die Eingangsrechnung nicht manuell erledigt wurde, ERIId: "
						+ eingangsrechnungIId);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STATUS,
					new Exception(
							"Die Erledigung der Eingangsrechnung kann nicht aufgehoben werden, Status: "
									+ er.getStatusCNr()));
		}
	}

	public void setzeEingangsrechnungFibuUebernahme(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData("eingangsrechnungIId=" + eingangsrechnungIId,
				theClientDto.getIDUser());
		// try {
		Eingangsrechnung er = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		if (er == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		er.setTFibuuebernahme(getTimestamp());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void setzeEingangsrechnungFibuUebernahmeRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData("eingangsrechnungIId=" + eingangsrechnungIId,
				theClientDto.getIDUser());
		// try {
		Eingangsrechnung er = em.find(Eingangsrechnung.class,
				eingangsrechnungIId);
		if (er == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		er.setTFibuuebernahme(null);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private EingangsrechnungDto pruefeUpdateAufEingangsrechnungErlaubt(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		EingangsrechnungDto erDto = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
		if (erDto.getTFibuuebernahme() != null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
					new Exception("ER i_id=" + eingangsrechnungIId
							+ " ist bereits verbucht"));
		}
		if (erDto.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG))
			getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
					LocaleFac.BELEGART_EINGANGSRECHNUNG, eingangsrechnungIId,
					theClientDto);
		else if (erDto.getEingangsrechnungartCNr().equals(
				EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT))
			// ER-Gutschrift wird als ER gebucht da gleicher Nummernkreis ->
			// daher eindeutig
			getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
					LocaleFac.BELEGART_EINGANGSRECHNUNG, eingangsrechnungIId,
					theClientDto);

		return erDto;
	}

	private EingangsrechnungzahlungDto pruefeUpdateAufEingangsrechnungZahlungErlaubt(
			Integer eingangsrechnungzahlungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		EingangsrechnungzahlungDto erzahlungDto = eingangsrechnungzahlungFindByPrimaryKey(eingangsrechnungzahlungIId);
		// TODO: Erzahlung wenn exportiert?
		/*
		 * if (erzahlungDto.getTFibuuebernahme() != null) { throw new
		 * EJBExceptionLP( EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
		 * new Exception("ER i_id=" + eingangsrechnungzahlungIId +
		 * " ist bereits verbucht")); }
		 */
		getBelegbuchungFac(theClientDto.getMandant()).pruefeUvaVerprobung(
				LocaleFac.BELEGART_ERZAHLUNG, eingangsrechnungzahlungIId,
				theClientDto);
		return erzahlungDto;
	}

	/**
	 * pruefen, ob die (in Kopfdaten und Kontierung) zugeordneten Konten nicht
	 * an mehrere Finanzaemter gebunden sind.
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param cNrUserI
	 *            String
	 * @throws EJBExceptionLP
	 */
	private void pruefeZugeordneteKontenAufGleichesFinanzamt(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// in dieser Map werden die IDs der Finanzaemter gesammelt.
			HashMap<Integer, Integer> hmGefundeneFinanzaemter = new HashMap<Integer, Integer>();
			EingangsrechnungDto erDto = eingangsrechnungFindByPrimaryKey(eingangsrechnungIId);
			LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(
					erDto.getLieferantIId(), theClientDto);
			// Konto der ER
			if (erDto.getKontoIId() != null) {
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
						erDto.getKontoIId());
				if (kontoDto.getFinanzamtIId() != null) {
					hmGefundeneFinanzaemter.put(kontoDto.getFinanzamtIId(),
							kontoDto.getFinanzamtIId());
				}
			}

			/*
			 * // PJ 162387 entfernt // Konto der zugeordneten Kostenstelle if
			 * (erDto.getKostenstelleIId() != null) { KostenstelleDto kstDto =
			 * getSystemFac() .kostenstelleFindByPrimaryKey(
			 * erDto.getKostenstelleIId()); if (kstDto.getKontoIId() != null) {
			 * KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
			 * kstDto.getKontoIId()); if (kontoDto.getFinanzamtIId() != null) {
			 * hmGefundeneFinanzaemter.put(kontoDto.getFinanzamtIId(),
			 * kontoDto.getFinanzamtIId()); } } }
			 */
			// Debitorenkonto des Lieferanten
			if (lfDto.getKontoIIdKreditorenkonto() != null) {
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
						lfDto.getKontoIIdKreditorenkonto());
				if (kontoDto.getFinanzamtIId() != null) {
					hmGefundeneFinanzaemter.put(kontoDto.getFinanzamtIId(),
							kontoDto.getFinanzamtIId());
				}
			}
			// Warenkonto des Lieferanten
			// PJ 162387 entfernt
			/*
			 * if (lfDto.getKontoIIdWarenkonto() != null) { KontoDto kontoDto =
			 * getFinanzFac().kontoFindByPrimaryKey(
			 * lfDto.getKontoIIdWarenkonto()); if (kontoDto.getFinanzamtIId() !=
			 * null) { hmGefundeneFinanzaemter.put(kontoDto.getFinanzamtIId(),
			 * kontoDto.getFinanzamtIId()); } }
			 */
			// nun die zugeordneten Mehrfachkontierungen
			EingangsrechnungKontierungDto[] erKonto = eingangsrechnungKontierungFindByEingangsrechnungIId(eingangsrechnungIId);
			for (int i = 0; i < erKonto.length; i++) {
				// Konto der ER
				if (erKonto[i].getKontoIId() != null) {
					KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(
							erKonto[i].getKontoIId());
					if (kontoDto.getFinanzamtIId() != null) {
						hmGefundeneFinanzaemter.put(kontoDto.getFinanzamtIId(),
								kontoDto.getFinanzamtIId());
					}
				}
				// Konto der zugeordneten Kostenstelle
				if (erKonto[i].getKostenstelleIId() != null) {
					KostenstelleDto kstDto = getSystemFac()
							.kostenstelleFindByPrimaryKey(
									erKonto[i].getKostenstelleIId());
					if (kstDto.getKontoIId() != null) {
						KontoDto kontoDto = getFinanzFac()
								.kontoFindByPrimaryKey(kstDto.getKontoIId());
						if (kontoDto.getFinanzamtIId() != null) {
							hmGefundeneFinanzaemter.put(
									kontoDto.getFinanzamtIId(),
									kontoDto.getFinanzamtIId());
						}
					}
				}
			}
			// Wurde mehr als ein FA gefunden? -> das darf nicht sein.
			if (hmGefundeneFinanzaemter.size() > 1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER,
						"ER" + erDto.getCNr());
			}
			// genau eines gefunden?
			else if (hmGefundeneFinanzaemter.size() == 1) {
				// Wenn es mehrere Finanzaemter gibt, muss auch geprueft werden,
				// ob das
				// Partner-UST-LKZ gleich dem LKZ des FA's ist. (9769)
				int iAnzahlFA = getFinanzFac().getAnzahlDerFinanzaemter(
						theClientDto);
				if (iAnzahlFA > 1) {
					Integer faIId = (Integer) hmGefundeneFinanzaemter.keySet()
							.toArray()[0];
					FinanzamtDto faDto = getFinanzFac()
							.finanzamtFindByPrimaryKey(faIId,
									theClientDto.getMandant(), theClientDto);
					if (getMandantFac().darfAnwenderAufModulZugreifen(
							LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
						// Kreditorenkonto muss gleiches Finanzamt haben, da nur
						// 1 FA beteiligt -> ist OK
					} else {
						// alte Methode ueber abweichendes UST-Land
						if (faDto.getPartnerDto().getLandplzortDto() != null) {
							// Vergleich des Lieferanten (UST) LKZ mit dem
							// Finanzamt
							// LKZ
							String sFinanzamtLKZ = faDto.getPartnerDto()
									.getLandplzortDto().getLandDto().getCLkz();
							String sLieferantLKZFinanzamt = null;
							if (lfDto.getPartnerDto()
									.getLandIIdAbweichendesustland() != null) {
								sLieferantLKZFinanzamt = getSystemFac()
										.landFindByPrimaryKey(
												lfDto.getPartnerDto()
														.getLandIIdAbweichendesustland())
										.getCLkz();
							} else {
								// Wenn kein Abweichendes UST-Land,dann ist das
								// LKZ
								// des Finanzamtes des Lieferanten gleich dem
								// des
								// Mandanten (=Lkz des Mandanten)

								MandantDto mandantDto = getMandantFac()
										.mandantFindByPrimaryKey(
												theClientDto.getMandant(),
												theClientDto);
								if (mandantDto.getPartnerDto() != null
										&& mandantDto.getPartnerDto()
												.getLandplzortDto() != null) {
									sLieferantLKZFinanzamt = mandantDto
											.getPartnerDto().getLandplzortDto()
											.getLandDto().getCLkz();
								}

							}
							// Vergleich
							if (sLieferantLKZFinanzamt != null) {
								if (!sLieferantLKZFinanzamt
										.equals(sFinanzamtLKZ)) {
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_FINANZ_EXPORT_PARTNER_UST_LKZ_UNGLEICH_FINANZAMT_LKZ,
											"ER" + erDto.getCNr());
								}
							}
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeEingangsrechnungzahlung(Integer iId)
			throws EJBExceptionLP {
		Eingangsrechnungzahlung toRemove = em.find(
				Eingangsrechnungzahlung.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto)
			throws EJBExceptionLP {
		if (eingangsrechnungzahlungDto != null) {
			Integer iId = eingangsrechnungzahlungDto.getIId();
			removeEingangsrechnungzahlung(iId);
		}
	}

	public void updateEingangsrechnungzahlung(
			EingangsrechnungzahlungDto eingangsrechnungzahlungDto)
			throws EJBExceptionLP {
		if (eingangsrechnungzahlungDto != null) {
			Integer iId = eingangsrechnungzahlungDto.getIId();
			Eingangsrechnungzahlung eingangsrechnungzahlung = em.find(
					Eingangsrechnungzahlung.class, iId);
			if (eingangsrechnungzahlung == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setEingangsrechnungzahlungFromEingangsrechnungzahlungDto(
					eingangsrechnungzahlung, eingangsrechnungzahlungDto);
		}
	}

	public void updateEingangsrechnungzahlungs(
			EingangsrechnungzahlungDto[] eingangsrechnungzahlungDtos)
			throws EJBExceptionLP {
		if (eingangsrechnungzahlungDtos != null) {
			for (int i = 0; i < eingangsrechnungzahlungDtos.length; i++) {
				updateEingangsrechnungzahlung(eingangsrechnungzahlungDtos[i]);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void pruefePreise(TheClientDto theClientDto) throws RemoteException,
			EJBExceptionLP {
		// SK 14441
		EingangsrechnungDto[] erDto = eingangsrechnungFindByMandantCNr(theClientDto
				.getMandant());
		for (int i = 0; i < erDto.length; i++) {
			if (!erDto[i].getWaehrungCNr().equals(
					theClientDto.getSMandantenwaehrung())) {
				try {
					WechselkursDto wechselDto = getLocaleFac().getKursZuDatum(
							erDto[i].getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(),
							erDto[i].getDBelegdatum(), theClientDto);
					// jetzt den Wert neu berechnen
					BigDecimal bdWertVorher = erDto[i].getNBetrag();
					BigDecimal bdKurs = wechselDto.getNKurs()/*
															 * .setScale(6,
															 * BigDecimal
															 * .ROUND_HALF_EVEN)
															 */;
					BigDecimal bdWertKorrekt = erDto[i]
							.getNBetragfw()
							.multiply(bdKurs)
							.setScale(FinanzFac.NACHKOMMASTELLEN,
									BigDecimal.ROUND_HALF_EVEN);
					BigDecimal bdWertUstKorrekt = erDto[i]
							.getNUstBetragfw()
							.multiply(bdKurs)
							.setScale(FinanzFac.NACHKOMMASTELLEN,
									BigDecimal.ROUND_HALF_EVEN);
					if (!bdWertKorrekt.equals(bdWertVorher)) {
						erDto[i].setNBetrag(bdWertKorrekt);
						erDto[i].setNUstBetrag(bdWertUstKorrekt);
						erDto[i].setNKurs(bdKurs);
						getEingangsrechnungFac().updateEingangsrechnung(
								erDto[i], theClientDto);
						myLogger.warn("ER: " + erDto[i].getCNr().trim()
								+ " ID: " + erDto[i].getIId()
								+ " wurde von Wert: " + bdWertVorher
								+ " auf Wert: " + bdWertKorrekt + " gesetzt!");
						myLogger.warn("Verwendeter Kurs von "
								+ erDto[i].getWaehrungCNr() + " nach "
								+ theClientDto.getSMandantenwaehrung() + ": "
								+ bdKurs.toString());
					}
				} catch (Exception e) {
					myLogger.error("Fehler beim pr\u00FCfen von "
							+ erDto[i].getCNr()
							+ " bitte h\u00E4ndisch nachpr\u00FCfen!");
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public List<Integer> eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
			String mandantCNr, Date dVon, Date dBis) {
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria crit = session
				.createCriteria(FLREingangsrechnungzahlung.class);
		crit.createAlias("flreingangsrechnung", "r");
		crit.add(Restrictions.eq("r.mandant_c_nr", mandantCNr));
		crit.add(Restrictions.between("t_zahldatum", dVon, dBis));
		List<FLREingangsrechnungzahlung> list = crit.list();
		List<Integer> ids = new ArrayList<Integer>();
		Iterator<FLREingangsrechnungzahlung> it = list.iterator();
		while (it.hasNext())
			ids.add(it.next().getI_id());
		session.close();
		return ids;
	}

	public void verbucheEingangsrechnungNeu(Integer iRechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		pruefeUpdateAufEingangsrechnungErlaubt(iRechnungIId, theClientDto);

		// Verbuchung
		Eingangsrechnung eingangsrechnung = em.find(Eingangsrechnung.class,
				iRechnungIId);
		if (eingangsrechnung != null) {
			if (eingangsrechnung.getStatusCNr().equals(
					EingangsrechnungFac.STATUS_STORNIERT)) {
				verbucheNeuRueckgaengig(iRechnungIId, theClientDto);
			} else {
				boolean verbuchen = false;
				if (eingangsrechnung.getKontoIId() != null) {
					// Einfachkontierung
					verbuchen = true;
				} else {
					// Mehrfachkontierung
					BigDecimal bdOffen = getWertNochNichtKontiert(iRechnungIId);
					if (bdOffen.doubleValue() == 0)
						verbuchen = true;
				}
				if (verbuchen) {
					EingangsrechnungzahlungDto[] zahlungenDtos = verbucheNeuRueckgaengig(
							iRechnungIId, theClientDto);
					// und verbuchen
					BuchungDto buchungDto = getBelegbuchungFac(
							theClientDto.getMandant())
							.verbucheEingangsrechnung(iRechnungIId,
									theClientDto);
					if (buchungDto != null) {
						// eventuell ist der Beleg aus einem anderen GJ, daher
						// hier
						// das GJ der Buchung ev. updaten
						if (!buchungDto.getIGeschaeftsjahr().equals(
								eingangsrechnung.getIGeschaeftsjahr())) {
							buchungDto.setIGeschaeftsjahr(eingangsrechnung
									.getIGeschaeftsjahr());
							getBuchenFac().updateBuchung(buchungDto,
									theClientDto);
						}
					}
					// und Zahlungen verbuchen
					for (int i = 0; i < zahlungenDtos.length; i++) {
						// Zahlung verbuchen
						buchungDto = getBelegbuchungFac(
								theClientDto.getMandant()).verbucheZahlungEr(
								zahlungenDtos[i].getIId(), theClientDto);
						if (buchungDto != null) {
							// eventuell ist der Beleg aus einem anderen GJ,
							// daher
							// hier das GJ der Buchung ev. updaten
							Integer iGeschaeftsjahr = getBuchenFac()
									.findGeschaeftsjahrFuerDatum(
											zahlungenDtos[i].getTZahldatum(),
											theClientDto.getMandant());
							if (iGeschaeftsjahr == null)
								iGeschaeftsjahr = getParameterFac()
										.getGeschaeftsjahr(
												theClientDto.getMandant(),
												zahlungenDtos[i]
														.getTZahldatum());
							if (!buchungDto.getIGeschaeftsjahr().equals(
									iGeschaeftsjahr)) {
								buchungDto.setIGeschaeftsjahr(iGeschaeftsjahr);
								getBuchenFac().updateBuchung(buchungDto,
										theClientDto);
							}
						}
					}
				}
			}
		}
	}

	private EingangsrechnungzahlungDto[] verbucheNeuRueckgaengig(
			Integer iRechnungIId, TheClientDto theClientDto) {
		EingangsrechnungzahlungDto[] zahlungenDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(iRechnungIId);
		for (int i = 0; i < zahlungenDtos.length; i++) {
			// Zahlung rueckbuchen
			getBelegbuchungFac(theClientDto.getMandant())
					.verbucheZahlungErRueckgaengig(zahlungenDtos[i],
							theClientDto);
		}
		// alte storno falls vorhanden
		getBelegbuchungFac(theClientDto.getMandant())
				.verbucheEingangsrechnungRueckgaengig(iRechnungIId,
						theClientDto);
		return zahlungenDtos;
	}

	public BigDecimal getBezahltBetragFw(Integer eingangsrechnungIId,
			Integer zahlungIIdAusgenommen) throws EJBExceptionLP,
			RemoteException {
		BigDecimal wert = new BigDecimal(0);
		EingangsrechnungzahlungDto[] erzDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < erzDtos.length; i++) {

			if (zahlungIIdAusgenommen != null
					&& zahlungIIdAusgenommen.equals(erzDtos[i].getIId())) {
				break;
			}

			if (zahlungIIdAusgenommen == null
					|| !zahlungIIdAusgenommen.equals(erzDtos[i].getIId())) {

				wert = wert.add(erzDtos[i].getNBetragfw());

			}
		}
		return wert;
	}

	public BigDecimal getBezahltKursdifferenzBetrag(
			Integer eingangsrechnungIId, BigDecimal kursRechnung)
			throws EJBExceptionLP, RemoteException {
		BigDecimal wert = new BigDecimal(0);
		// bei der ER ist der Kurs verkehrt gespeichert!
		// BigDecimal kursRechnungInv =
		// Helper.rundeKaufmaennisch(Helper.getKehrwert(kursRechnung),
		// LocaleFac.ANZAHL_NACHKOMMASTELLEN_WECHSELKURS);
		EingangsrechnungzahlungDto[] erzDtos = eingangsrechnungzahlungFindByEingangsrechnungIId(eingangsrechnungIId);
		for (int i = 0; i < erzDtos.length; i++) {
			BigDecimal betragRechnungkurs = Helper.rundeKaufmaennisch(
					erzDtos[i].getNBetragfw().multiply(kursRechnung),
					FinanzFac.NACHKOMMASTELLEN);
			wert = wert
					.add(betragRechnungkurs.subtract(erzDtos[i].getNBetrag()));
		}
		return wert;
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungFw(Integer erIId) {
		// berechnet die Summe der gestellten Anzahlungen! nicht der bezahlten
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			EingangsrechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(erIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNBetragfw() != null) {
					anzahlungen = anzahlungen.add(anzRechnungen[i]
							.getNBetragfw());
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	public BigDecimal getAnzahlungenZuSchlussrechnung(Integer erIId) {
		// berechnet die Summe der gestellten Anzahlungen! nicht der bezahlten
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			EingangsrechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(erIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNBetrag() != null) {
					anzahlungen = anzahlungen
							.add(anzRechnungen[i].getNBetrag());
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	public BigDecimal getAnzahlungenZuSchlussrechnungUstFw(Integer erIId) {
		BigDecimal anzahlungen = new BigDecimal(0);
		try {
			EingangsrechnungDto[] anzRechnungen = getAnzahlungsrechnungenZuSchlussrechnung(erIId);
			for (int i = 0; i < anzRechnungen.length; i++) {
				if (anzRechnungen[i].getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)
						&& anzRechnungen[i].getNUstBetragfw() != null) {
					anzahlungen = anzahlungen.add(anzRechnungen[i]
							.getNUstBetragfw());
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return anzahlungen;
	}

	private EingangsrechnungDto[] getAnzahlungsrechnungenZuSchlussrechnung(
			Integer schlussrechnungIId) throws EJBExceptionLP, RemoteException {
		EingangsrechnungDto rDto = getEingangsrechnungFac()
				.eingangsrechnungFindByPrimaryKey(schlussrechnungIId);
		EingangsrechnungDto[] anzRechnungen = new EingangsrechnungDto[0];
		if (rDto.getBestellungIId() != null
				&& rDto.getEingangsrechnungartCNr().equals(
						EingangsrechnungFac.EINGANGSRECHNUNGART_SCHLUSSZAHLUNG))
			anzRechnungen = getEingangsrechnungFac()
					.eingangsrechnungFindByBestellungIId(
							rDto.getBestellungIId());
		return anzRechnungen;
	}

}
