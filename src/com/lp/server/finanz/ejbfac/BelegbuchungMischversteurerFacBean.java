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
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.bl.FinanzValidator;
import com.lp.server.finanz.ejb.Belegbuchung;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BelegbuchungMischversteuererFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungInfoDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HvOptional;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BelegbuchungMischversteurerFacBean extends BelegbuchungFacBean  implements BelegbuchungMischversteuererFac{
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
	public BuchungInfoDto verbucheRechnung(Integer rechnungIId,
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
//			HvOptional<BelegbuchungDto> azZahlungBuchung =
//					existsAnzahlungZahlungGegenbuchung(rechnungDto.getIId());
			
			HvOptional<RechnungDto> schlussrechnung = HvOptional.empty();
			if (rechnungDto.isAnzahlungsRechnung()) {
				schlussrechnung = findSchlussRechnung(rechnungDto.getAuftragIId());
			}
			
			buchungDto = verbucheZahlungSkonto(false, zahlungDto, rechnungDto,
					schlussrechnung, theClientDto);
			
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
			
		KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), theClientDto);
		FinanzValidator.debitorkontoDefinition(kundeDto);
		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		Konto debitorKonto = em.find(Konto.class,	kundeDto.getIidDebitorenkonto());
		FinanzValidator.steuerkategorieDefinition(debitorKonto, rechnungDto);
//			if (Helper.isStringEmpty(debitorKonto.getSteuerkategorieCNr())) {
//				KontoDto debitorDto = KontoDtoAssembler.createDto(debitorKonto) ;
//				throw EJBExcFactory.steuerkategorieDefinitionFehlt(rechnungDto, debitorDto) ;
////				throw new EJBExceptionLP(
////						EJBExceptionLP.FEHLER_FINANZ_KEINE_STEUERKATEGORIE_DEFINIERT,
////						"Keine Steuerkategorie definiert bei Debitorenkonto "
////								+ debitorKonto.getCNr());
//			}
		buchungDto = new BuchungDto();
		buchungDto.setCBelegnummer(rechnungDto.getCNr());
		buchungDto.setCText(partnerDto.getCName1nachnamefirmazeile1());
		buchungDto.setDBuchungsdatum(new Date(zahlungDto.getDZahldatum().getTime()));
		buchungDto.setKostenstelleIId(rechnungDto.getKostenstelleIId());
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_BUCHUNG);

		BelegbuchungDto belegbuchungDto = new BelegbuchungDto();
		belegbuchungDto.setBuchungIIdZahlung(buchungIIdZahlung);
		Integer debitorenKontoId = exportDaten[0].getKontoDto().getIId();
		Integer debitorenKontoUebersteuertId = null ;
		String steuerkategorieCnr = debitorKonto.getSteuerkategorieCNr() ;
		Integer finanzamtIId = debitorKonto.getFinanzamtIId() ;
		if(exportDaten[1].getDebitorenKontoIIdUebersteuert() != null) {
			debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
			Konto debKontouebersteuert = em.find(Konto.class, debitorenKontoUebersteuertId);			
			steuerkategorieCnr = debKontouebersteuert.getSteuerkategorieCNr() ;
			finanzamtIId = debKontouebersteuert.getFinanzamtIId() ;
		}
		
//		if (isRechnungDtoReversecharge(rechnungDto)) {
//			if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
//				steuerkategorieCnr = debitorKonto.getSteuerkategorieCNr() ;
////				steuerkategorieIId = debitorKonto.getSteuerkategorieIIdReverse();
//			} else {
//				debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
//				Konto debKontouebersteuert = em.find(Konto.class, debitorenKontoUebersteuertId);
//				steuerkategorieCnr = debKontouebersteuert.getSteuerkategorieCNr() ;
////				steuerkategorieIId = debKontouebersteuert.getSteuerkategorieIIdReverse();
//			}
//		} else {
//			if (exportDaten[1].getDebitorenKontoIIdUebersteuert() == null) {
//				steuerkategorieCnr = debitorKonto.getSteuerkategorieCNr() ;
////				steuerkategorieIId = debitorKonto.getSteuerkategorieIId();
//			} else {
//				debitorenKontoUebersteuertId = exportDaten[1].getDebitorenKontoIIdUebersteuert() ;
//				Konto debKontouebersteuert = em.find(Konto.class, debitorenKontoUebersteuertId);
//				steuerkategorieCnr = debKontouebersteuert.getSteuerkategorieCNr() ;
////				steuerkategorieIId = debKontouebersteuert.getSteuerkategorieIId();
//			}
//		}
		
		SteuerkategorieDto stkDto = 
				getFinanzServiceFac().steuerkategorieFindByCNrFinanzamtIId(
						steuerkategorieCnr, 
						rechnungDto.getReversechargeartId(), 
						finanzamtIId, theClientDto) ;
		Integer steuerkategorieIId = stkDto.getIId() ;
		
		List<BuchungdetailDto> details = getBuchungdetailsVonExportDtos(exportDaten,
				steuerkategorieIId, debitorenKontoId, debitorenKontoUebersteuertId, true, false, false, theClientDto);
		details = skaliereDetails(details, rechnungDto, zahlungDto);
		korrigiereRundungsUngenauigkeit(details, debitorenKontoId);

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
				BuchungdetailDto[] detailDtos = details.toArray(new BuchungdetailDto[0]) ;					
				buchungDto = getBuchenFac().buchen(buchungDto, 
						detailDtos, rechnungDto.getReversechargeartId(), true, theClientDto);

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

	private List<BuchungdetailDto> skaliereDetails(List<BuchungdetailDto> details,
			RechnungDto rechnungDto, RechnungzahlungDto zahlungDto) {
		if (zahlungDto.getNBetrag().compareTo(rechnungDto.getNWert()) == 0) {
			return details;
		}
		
		BigDecimal faktor = zahlungDto.getNBetrag().divide(rechnungDto.getNWert(), 100, BigDecimal.ROUND_HALF_EVEN);
		for (int i=0; i<details.size() ; i++) {
			BuchungdetailDto detail = details.get(i) ;
			detail.setNBetrag(Helper.rundeKaufmaennisch(detail.getNBetrag().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
			if (detail.getNUst().signum() != 0)
				detail.setNUst(Helper.rundeKaufmaennisch(detail.getNUst().multiply(faktor), FinanzFac.NACHKOMMASTELLEN));
		}		
		return details;
	}

}
