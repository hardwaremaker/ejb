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
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.bl.FinanzValidator;
import com.lp.server.finanz.ejb.Belegbuchung;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BelegbuchungIstversteurerFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegbuchungIstversteurerFacBean extends BelegbuchungMischversteurerFacBean implements BelegbuchungIstversteurerFac {
	@PersistenceContext
	private EntityManager em;

/*	*//**
	 * Verbuchen einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei Istversteurer wird nichts getan!
	 *//*
	@Override
	public BuchungDto verbucheRechnung(Integer rechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// bei Istversteurer wird die Rechnung nicht gebucht!
		return null;
	}

	*//**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei Istversteurer wird nichts getan!
	 *//*
	@Override
	public void verbucheRechnungRueckgaengig(Integer rechnungIId, TheClientDto theClientDto) 
		throws EJBExceptionLP {

		}

	*//**
	 * Rueckgaengigmachen des Verbuchens einer Gutschrift und Wertgutschrift
	 * Anmerkung: Wertgutschrift ist in der Fibu die fb_belegart Gutschrift
	 * 
	 * @param rechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung: bei Istversteurer wird nichts getan!
	 *//*
	@Override
	public void verbucheGutschriftRueckgaengig(Integer rechnungIId, TheClientDto theClientDto) 
		throws EJBExceptionLP {
		
	}
*/
	/**
	 * Automatisches Uebernehmen einer ER in die FiBu
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
 	 * Anmerkung: bei Istversteurer wird nichts getan!
	 */
	@Override
	public BuchungDto verbucheEingangsrechnung(Integer eingangsrechnungIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return null;
	}
	
	/**
	 * Rueckgaengigmachen des Verbuchens einer Rechnung
	 * 
	 * @param eingangsrechnungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * 
 	 * Anmerkung: bei Istversteurer wird nichts getan!
	 */
	@Override
	public void verbucheEingangsrechnungRueckgaengig(
			Integer eingangsrechnungIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
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

	/**
	 * Verbuchen der Zahlung einer Eingangsrechnung
	 * 
	 * @param zahlungIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @throws RemoteException 
	 * 
	 * Anmerkung:	ISTVERSTEUERER: der "Rechnungsanteil" wird ebenfalls gebucht
	 * 								es wird kein Skonto gebucht!
	 * 
	 */
	@Override
	public BuchungDto verbucheZahlungEr(Integer zahlungIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BuchungDto buchungDto = null;
		if (getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			// Zahlung schon verbucht?
			BelegbuchungDto bbDto = null;
			bbDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, zahlungIId);
			if (bbDto != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_BELEG_BEREITS_VERBUCHT,
						"Zahlung bereits verbucht. ID=" + zahlungIId);
			}
			EingangsrechnungzahlungDto zahlungDto = getEingangsrechnungzahlung(zahlungIId);
			EingangsrechnungDto rechnungDto = getEingangsrechnungDto(zahlungDto.getEingangsrechnungIId());

			// nur diese Zahlung verbuchen
			buchungDto = verbucheZahlungErSkonto(false, zahlungDto, rechnungDto, theClientDto);
			
			// anteilige Rechnung (Aufwand/Erloes) jetzt buchen mit Zahldatum 
			bucheEingangsrechnungIst(rechnungDto, zahlungDto, buchungDto.getIId(), theClientDto);
		}
		return buchungDto;
	}

	private void bucheEingangsrechnungIst(EingangsrechnungDto eingangsrechnungDto,
			EingangsrechnungzahlungDto zahlungDto, Integer buchungIIdZahlung,
			TheClientDto theClientDto) {
		BuchungDto buchungDto = null;
		FibuexportDto[] exportDaten = getExportDatenRechnung(eingangsrechnungDto, theClientDto);
			
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
				eingangsrechnungDto.getLieferantIId(), theClientDto);
		FinanzValidator.kreditorkontoDefinition(lieferantDto);
		Konto kreditorKonto = em.find(Konto.class, lieferantDto.getKontoIIdKreditorenkonto());
		FinanzValidator.steuerkategorieDefinition(kreditorKonto, eingangsrechnungDto);
		PartnerDto  partnerDto = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto);
//			if (Helper.isStringEmpty(kreditorKonto.getSteuerkategorieCNr())) {
//				KontoDto kreditorDto = KontoDtoAssembler.createDto(kreditorKonto) ;
//				throw EJBExcFactory.steuerkategorieDefinitionFehlt(eingangsrechnungDto, kreditorDto) ;
////				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
////						"Keine Steuerkategorie definiert bei Kreditorenkonto " + kreditorKonto.getCNr());				
//			}
		
		buchungDto = new BuchungDto();
		buchungDto.setCBelegnummer(eingangsrechnungDto.getCNr());
		buchungDto.setCText(eingangsrechnungDto.getCText());
		if (buchungDto.getCText() == null)
			buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
		buchungDto.setDBuchungsdatum(zahlungDto.getTZahldatum());
		buchungDto.setKostenstelleIId(eingangsrechnungDto.getKostenstelleIId());
		buchungDto.setBelegartCNr(LocaleFac.BELEGART_FIBU_EINGANGSRECHNUNG);
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBuchungIIdZahlung(buchungIIdZahlung);

//		Integer steuerkategorieIId = kreditorKonto.getSteuerkategorieIId();
		SteuerkategorieDto stkDto = 
				getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
						kreditorKonto.getSteuerkategorieCNr(), 
						eingangsrechnungDto.getReversechargeartId(), 
						kreditorKonto.getFinanzamtIId(), theClientDto) ;
		Integer steuerkategorieIId = stkDto.getIId() ;		
		boolean reversecharge = isEingangsrechnungDtoReversecharge(eingangsrechnungDto) ;
		List<BuchungdetailDto> details = getBuchungdetailsVonExportDtos(
				exportDaten, steuerkategorieIId, null, null, false, Helper.short2boolean(eingangsrechnungDto.getBIgErwerb()), reversecharge, theClientDto);
		details = skaliereDetails(details, eingangsrechnungDto, zahlungDto);
		if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_EINGANGSRECHNUNG)
					|| eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN)) {
			belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		} else if (eingangsrechnungDto.getEingangsrechnungartCNr().equals(EingangsrechnungFac.EINGANGSRECHNUNGART_GUTSCHRIFT)) {
			belegbuchungDto.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
		} else {
			myLogger.error("Ungueltiger Eingangsrechnungstyp, keine Verbuchung moeglich");
		}
		// nur buchen, wenn die Eingangsrechnung auch verbucht werden kann
		if (details != null) {
			try {
				// Das Verbuchen einer Eingangsrechnung muss immer den
				// Buchungsregeln entsprechen
				//
				// Kostenstelle aus erster Buchung wenn Splittbuchung
				if (buchungDto.getKostenstelleIId() == null) {
					for (int i = 0; i < exportDaten.length; i++) {
						if (exportDaten[i].getKostDto() != null) {
							if (exportDaten[i].getKostDto().getIId() != null) {
								buchungDto.setKostenstelleIId(exportDaten[i].getKostDto().getIId());
								break;
							}
						}
					}
				}
				BuchungdetailDto[] detailDtos = details.toArray(new BuchungdetailDto[0]) ;					
				buchungDto = getBuchenFac().buchen(buchungDto, detailDtos, 
						eingangsrechnungDto.getReversechargeartId(), true, theClientDto);
				// in Tabelle buchungRechnung speichern
				belegbuchungDto.setBuchungIId(buchungDto.getIId());
				belegbuchungDto.setIBelegiid(eingangsrechnungDto.getIId());
				createBelegbuchung(belegbuchungDto, theClientDto);
				// Status der Rechnung auf verbucht setzen
				// AD: nicht auf verbucht setzen, macht erst die UVA
				// rechnungDto.setStatusCNr(RechnungFac.STATUS_VERBUCHT);
				// getEingangsrechnungFac().updateEingangsrechnung(eingangsrechnungDto,
				// theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
	}

	private List<BuchungdetailDto> skaliereDetails(
			List<BuchungdetailDto> details,
			EingangsrechnungDto eingangsrechnungDto,
			EingangsrechnungzahlungDto zahlungDto) {
		BigDecimal faktor = zahlungDto.getNBetrag().divide(eingangsrechnungDto.getNBetrag(), 100, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal saldo = BigDecimal.ZERO ;
		BigDecimal betragMax = BigDecimal.ZERO ;
		int betragMaxIndex = -1;
		
		for (int i=0; i<details.size(); i++) {
			BuchungdetailDto detail = details.get(i) ;
			detail.setNBetrag(Helper.rundeKaufmaennisch(detail.getNBetrag().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
			if (detail.getNUst().signum() != 0)
				detail.setNUst(Helper.rundeKaufmaennisch(detail.getNUst().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
			if (detail.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung))
				saldo = saldo.add(detail.getNBetrag());
			else
				saldo = saldo.subtract(detail.getNBetrag());
			if (i > 0)
				if (detail.getNBetrag().compareTo(betragMax)==1) {
					betragMax = detail.getNBetrag();
					betragMaxIndex = i;
				}
		}
		if (saldo.signum() != 0) {
			// hoechsten Betrag anpassen
			BuchungdetailDto detail = details.get(betragMaxIndex) ;
			if (detail.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung)) {
				detail.setNBetrag(detail.getNBetrag().subtract(saldo));
			} else {
				detail.setNBetrag(detail.getNBetrag().add(saldo));
			}
		}
		
		return details;
	}

	/**
	 * Rueckgaengigmachen des Verbuchens einer Eingangsrechnung Zahlung
	 * 
	 * @param zahlungDto die rueckg&auml;ngigzumachende Zahlung
	 * @param theClientDto der aktuelle Benutzer 
	 * @throws EJBExceptionLP
	 * 
	 * Anmerkung:	ISTVERSTEUERER: der "Rechnungsanteil" wird ebenfalls storniert
	 */

	@Override
	public void verbucheZahlungErRueckgaengig(EingangsrechnungzahlungDto zahlungDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			myLogger.logData(zahlungDto.getIId(), theClientDto.getIDUser());
			BelegbuchungDto buchungZahlungDto = belegbuchungFindByBelegartCNrBelegiidOhneExc(
					LocaleFac.BELEGART_ERZAHLUNG, zahlungDto.getIId());
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
	
}
