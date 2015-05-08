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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.ejb.Belegbuchung;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegbuchungMischversteurerFacBean extends BelegbuchungFacBean {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Verbuchen einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei Misch- und Istversteurer wird nichts getan!
	 */
	@Override
	public BuchungDto verbucheRechnung(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// bei Istversteurer wird die Rechnung nicht gebucht!
		return null;
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei bei Misch- und Istversteurer wird nichts getan!
	 */
	@Override
	public void verbucheRechnungRueckgaengig(Integer rechnungIId, TheClientDto theClientDto) 
		throws EJBExceptionLP {

		}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Gutschrift und Wertgutschrift
	 * Anmerkung: Wertgutschrift ist in der Fibu die fb_belegart Gutschrift
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei bei Misch- und Istversteurer wird nichts getan!
	 */
	@Override
	public void verbucheGutschriftRueckgaengig(Integer rechnungIId, TheClientDto theClientDto) 
		throws EJBExceptionLP {
		
	}

	/**
	 * Verbuchen einer Zahlung einer Rechnung bzw. Gutschrift
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException 
	 * 
	 * Anmerkung:	Belegbuchung ist in beiden F&auml;llen REZAHLUNG!
	 * 	 			ISTVERSTEUERER: der "Rechnungsanteil" wird ebenfalls gebucht
	 * 								es wird kein Skonto gebucht!
	 */
	@Override
	public BuchungDto verbucheZahlung(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {

			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_REZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}

			RechnungzahlungDto zahlungDto = getRechnungzahlung(zahlungIId);
			RechnungDto rechnungDto = getRechnungDto(zahlungDto.getRechnungIId());
			
			// nur diese Zahlung verbuchen, da es kein Skonto gibt
			buchungDto = verbucheZahlungSkonto(false, zahlungDto, rechnungDto, theClientDto);
			
			// anteilige Rechnung (Aufwand/Erloes) jetzt buchen mit Zahldatum 
			if (buchungDto != null)
				bucheRechnungIst(rechnungDto, zahlungDto, buchungDto.getIId(), theClientDto);
		}
		return buchungDto;
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Zahlung
	 * 
	 * @param zahlungDto
	 *            RechnungzahlungDto
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung:	ISTVERSTEUERER: der "Rechnungsanteil" wird ebenfalls storniert
	 */
	@Override
	public void verbucheZahlungRueckgaengig(RechnungzahlungDto zahlungDto,
				TheClientDto theClientDto) throws EJBExceptionLP {

		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(zahlungDto.getIId(), theClientDto.getIDUser());
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_REZAHLUNG, zahlungDto.getIId());
			if (buchungZahlungDto != null) {
				getSystemFac().pruefeGeschaeftsjahrSperre(buchungZahlungDto, theClientDto.getMandant());
				
				Integer buchungIId = buchungZahlungDto.getBuchungIId();
				removeBelegbuchung(buchungZahlungDto, theClientDto);
				getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				
				BelegbuchungDto buchungRechnungDto = belegbuchungFindByBuchungIIdZahlungOhneEx(buchungIId);
				if (buchungRechnungDto != null) {
					buchungIId = buchungRechnungDto.getBuchungIId();
					removeBelegbuchung(buchungRechnungDto, theClientDto);
					getBuchenFac().storniereBuchung(buchungIId, theClientDto);
				}
				if (zahlungDto.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_VORAUSZAHLUNG)) {
					Integer bdIId = zahlungDto.getBuchungdetailIId();
					try {
						bucheVorauszahlungBetrag(bdIId, zahlungDto.getNBetrag(), theClientDto);
					} catch (RemoteException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNGAKTIVIEREN_NICHT_MOEGLICH, "Vorauszahlung kann nicht aktiviert werden. " + e.getMessage());
					}
				}
			}
		}
	}
	
	private BelegbuchungDto belegbuchungFindByBuchungIIdZahlungOhneEx(
			Integer buchungIIdZahlung) {
		try {
			Query query = em.createNamedQuery(Belegbuchung.QUERY_BelegbuchungfindByBuchungIIdZahlung);
			query.setParameter(1, buchungIIdZahlung);
			Belegbuchung belegbuchung = (Belegbuchung) query.getSingleResult();
			return assembleBelegbuchungDto(belegbuchung);
		} catch (NoResultException ex) {
			myLogger.warn("buchungIId=" + buchungIIdZahlung, ex);
			return null;
		}
	}

	private void bucheRechnungIst(RechnungDto rechnungDto, RechnungzahlungDto zahlungDto, Integer buchungIIdZahlung, TheClientDto theClientDto) {
		BuchungDto buchungDto = null;
		String rechnungtyp = null;
		try {
			RechnungartDto rechnungartDto = getRechnungServiceFac().rechnungartFindByPrimaryKey(rechnungDto.getRechnungartCNr(), theClientDto);
			rechnungtyp = rechnungartDto.getRechnungtypCNr();
		} catch (Exception ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

		FibuExportKriterienDto fibuExportKriterienDto = new FibuExportKriterienDto();
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG))
			fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		else
			fibuExportKriterienDto.setSBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
		fibuExportKriterienDto.setDStichtag(new Date(rechnungDto.getTBelegdatum().getTime()));

		FibuExportManager manager = FibuExportManagerFactory.getFibuExportManager(
				getExportVariante(theClientDto),
				getExportFormat(theClientDto),
				fibuExportKriterienDto, theClientDto);
		FibuexportDto[] exportDaten;
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG))
			exportDaten = manager.getExportdatenRechnung(rechnungDto.getIId(), null);
		else if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT))
			exportDaten = manager.getExportdatenGutschrift(rechnungDto.getIId(), null);
		else
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Rechnungstyp f\u00FCr Fibu nicht definiert: Typ=" + rechnungtyp);
			
		PartnerDto partnerDto = null;
		Konto debitorKonto = null;
		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
			if (kundeDto.getIidDebitorenkonto() == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEIN_DEBITORENKONTO_DEFINIERT,
						"Kein Debitorenkonto: Kunde ID="
								+ kundeDto.getIId());
			}
			debitorKonto = em.find(Konto.class,	kundeDto.getIidDebitorenkonto());
			if (debitorKonto.getSteuerkategorieIId() == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
						"Keine Steuerkategorie bei Debitorenkonto "
								+ debitorKonto.getCNr());

		} catch (Exception ex3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex3);
		}
		buchungDto = new BuchungDto();
		buchungDto.setCBelegnummer(rechnungDto.getCNr());
		buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
		buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum().getTime()));
		buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBuchungIIdZahlung(buchungIIdZahlung);
		BuchungdetailDto[] details = null;
		Integer steuerkategorieIId = null;
		if (Helper.short2boolean(rechnungDto.getBReversecharge()))
			if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null)
				steuerkategorieIId = debitorKonto.getSteuerkategorieIIdReverse();
			else {
				Konto debKontouebersteuert = em.find(Konto.class, exportDaten[1].getDebitorenKontoIIdUebersteuert());
				steuerkategorieIId = debKontouebersteuert.getSteuerkategorieIIdReverse();
			}
		else
			if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null)
				steuerkategorieIId = debitorKonto.getSteuerkategorieIId();
			else {
				Konto debKontouebersteuert = em.find(Konto.class, exportDaten[1].getDebitorenKontoIIdUebersteuert());
				steuerkategorieIId = debKontouebersteuert.getSteuerkategorieIId();
			}
		details = getBuchungdetailsVonExportDtos(exportDaten, steuerkategorieIId, true, false, false, theClientDto);
		details = skaliereDetails(details, rechnungDto, zahlungDto);
		if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_RECHNUNG);
			belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
		} else if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
			buchungDto.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
			belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
		} else {
			myLogger.error("Ungueltiger Rechnungstyp, keine Verbuchung moeglich");
		}
		// nur buchen, wenn die Rechnung auch verbucht werden kann
		if (details != null) {
			try {
				// Das Verbuchen einer Rechnung muss immer den Buchungsregeln entsprechen
				buchungDto = getBuchenFac().buchen(buchungDto, details,	true, theClientDto);

				// in Tabelle buchungRechnung speichern
				belegbuchungDto.setBuchungIId(buchungDto.getIId());
				belegbuchungDto.setIBelegiid(rechnungDto.getIId());
				createBelegbuchung(belegbuchungDto, theClientDto);
				
				// Status der Rechnung auf verbucht setzen
				// AD: nicht auf verbucht setzen, macht erst die UVA
				/* rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT);
				getRechnungFac().updateRechnung(rechnungDto, theClientDto); */
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
	}

	private BuchungdetailDto[] skaliereDetails(BuchungdetailDto[] details,
			RechnungDto rechnungDto, RechnungzahlungDto zahlungDto) {
		if (zahlungDto.getNBetrag().compareTo(rechnungDto.getNWert()) == 0)
			return details;
		else {
			BigDecimal faktor = zahlungDto.getNBetrag().divide(rechnungDto.getNWert(), 100, BigDecimal.ROUND_HALF_EVEN);
			for (int i=0; i<details.length; i++) {
				details[i].setNBetrag(Helper.rundeKaufmaennisch(details[i].getNBetrag().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
				if (details[i].getNUst().signum() != 0)
					details[i].setNUst(Helper.rundeKaufmaennisch(details[i].getNUst().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
			}		
			return details;
		}
	}

}
