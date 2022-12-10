/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2022 HELIUM V IT-Solutions GmbH
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellung;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.EingangsrechnungQuery;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.assembler.ExportdatenDtoAssembler;
import com.lp.server.finanz.assembler.ExportlaufDtoAssembler;
import com.lp.server.finanz.bl.FibuExportFormatterAbacus;
import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.bl.datevexport.BuchungsjournalDatevExportHeaderFormatter;
import com.lp.server.finanz.bl.datevexport.BuchungsjournalExportDatevBuchung;
import com.lp.server.finanz.bl.datevexport.DatevExportBuchungFormatter;
import com.lp.server.finanz.bl.hvraw.BuchungsjournalExportHVRawFormatter;
import com.lp.server.finanz.bl.rzlexport.BuchungsjournalExportRzlFormatter;
import com.lp.server.finanz.ejb.Exportdaten;
import com.lp.server.finanz.ejb.Exportlauf;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzExportdaten;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzExportlauf;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungsjournalExportDatumsart;
import com.lp.server.finanz.service.BuchungsjournalExportProperties;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.IBuchungsjournalExportFormatter;
import com.lp.server.finanz.service.IntrastatDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferschein;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.RechnungQuery;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.ejbfac.SteuercodeInfo;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.HvBelegnummernformatHistorisch;
import com.lp.server.system.service.IHvBelegnummernformat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FibuExportFacBean extends Facade implements FibuExportFac {
	@PersistenceContext
	private EntityManager entityManager;

	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public String exportiereBelege(
			FibuExportKriterienDto fibuExportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (fibuExportKriterienDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT,
					new Exception("fibuExportKriterienDto == null"));
		}
		FibuExportManager em = FibuExportManagerFactory.getFibuExportManager(
				getExportVariante(theClientDto), getExportFormat(theClientDto),
				fibuExportKriterienDto, theClientDto);
		// zuerst einen neuen Exportlauf generieren
		ExportlaufDto exportlaufDto = new ExportlaufDto();
		exportlaufDto.setMandantCNr(theClientDto.getMandant());
		exportlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		exportlaufDto.setTStichtag(fibuExportKriterienDto.getDStichtag());
		exportlaufDto = createExportlauf(exportlaufDto);
		// Die 3 Exportfiles werden als StringArray zurueckgegeben
		String exportResult;
		if (fibuExportKriterienDto.getSBelegartCNr().equals(
				LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
			exportResult = exportiereEingangsrechnungen(fibuExportKriterienDto,
					em, exportlaufDto.getIId(), fibuExportKriterienDto
							.getDStichtag(), theClientDto);
		} else if (fibuExportKriterienDto.getSBelegartCNr().equals(
				LocaleFac.BELEGART_RECHNUNG)) {
			exportResult = exportiereRechnungen(fibuExportKriterienDto, em,
					exportlaufDto.getIId(), fibuExportKriterienDto
							.getDStichtag(), theClientDto);
		} else if (fibuExportKriterienDto.getSBelegartCNr().equals(
				LocaleFac.BELEGART_GUTSCHRIFT)) {
			exportResult = exportiereGutschriften(fibuExportKriterienDto, em,
					exportlaufDto.getIId(), fibuExportKriterienDto
							.getDStichtag(), theClientDto);
		} else {
			exportResult = null;
		}
		// wenn keine Belege exportiert wurden, loesch ich den lauf gleich
		// wieder
		if (exportResult == null) {
			removeExportlauf(exportlaufDto.getIId());
		}

		/**
		 * @todo die Belege muessen ausserdem noch verbucht werden! PJ 4248
		 */
		return exportResult;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String exportierePersonenkonten(String kontotypCNr, boolean nurVerwendete, TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontotypCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT,
					new Exception("kontotypCNr == null"));
		}
		FibuExportManager em = FibuExportManagerFactory.getFibuExportManager(getExportVariante(theClientDto),
				getExportFormat(theClientDto), kontotypCNr, null, theClientDto);
		return exportierePersonenkonten(em, kontotypCNr, nurVerwendete, theClientDto);
	}

	private String getExportVariante(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_VARIANTE);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private String getExportFormat(TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FINANZ,
							ParameterFac.PARAMETER_FINANZ_EXPORT_ZIELPROGRAMM);
			return parameter.getCWert();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private String exportiereEingangsrechnungen(
			FibuExportKriterienDto fibuExportKriterienDto,
			FibuExportManager em, Integer exportlaufIId,
			java.sql.Date dStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		try {
			FLREingangsrechnung[] eingangsrechnungen = getVerbuchbareEingangsrechnungen(
					dStichtag, theClientDto);
			// keine daten?
			if (eingangsrechnungen == null || eingangsrechnungen.length == 0) {
				return null;
			}
			// jetzt alle sperren
			sperreEingangsrechnungen(eingangsrechnungen, true, theClientDto);
			sExport.append(em.exportiereUeberschriftBelege());
			
			ExportEingangsrechnungValidator exportValidator = new ExportEingangsrechnungValidator(em, fibuExportKriterienDto);
			for (FLREingangsrechnung er : eingangsrechnungen) {
				exportValidator.validate(er);
				if (exportValidator.shouldExport()) {
					sExport.append(em.exportiereEingangsrechnung(er.getI_id(),
							fibuExportKriterienDto.getDStichtag()));
				}
				
				if (exportValidator.shouldExport()
						|| exportValidator.shouldMark()) {
					// Exportprotokoll
					ExportdatenDto exportdatenDto = new ExportdatenDto();
					exportdatenDto
							.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
					exportdatenDto.setExportlaufIId(exportlaufIId);
					exportdatenDto.setIBelegiid(er.getI_id());
					createExportdaten(exportdatenDto);
					
					if (!exportValidator.shouldExport()) {
						myLogger.warn("ER " + er.getC_nr()
								+ " wurde als exportiert markiert");
					}
					// ER-Status setzen
					getEingangsrechnungFac().setzeEingangsrechnungFibuUebernahme(
							er.getI_id(), theClientDto);
					
				}
				
			}
			// die locks aufheben
			sperreEingangsrechnungen(eingangsrechnungen, false, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExport.toString();
	}

	private String exportiereRechnungen(
			FibuExportKriterienDto fibuExportKriterienDto,
			FibuExportManager em, Integer exportlaufIId,
			java.sql.Date dStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		try {
			FLRRechnung[] re = getVerbuchbareRechnungen(dStichtag, theClientDto);
			// keine daten?
			if (re == null || re.length == 0) {
				return null;
			}
			// jetzt alle sperren
			sperreRechnungen(re, true, theClientDto);
			sExport.append(em.exportiereUeberschriftBelege());
			
			ExportRechnungValidator exportValidator = new ExportRechnungValidator(em, fibuExportKriterienDto);
			for (FLRRechnung rechnung : re) {
				exportValidator.validate(rechnung);
				if (exportValidator.shouldExport()) {
					sExport.append(em.exportiereRechnung(
							rechnung.getI_id(), dStichtag));
				}
				
				if (exportValidator.shouldExport()
						|| exportValidator.shouldMark()) {
					// Exportprotokoll
					ExportdatenDto exportdatenDto = new ExportdatenDto();
					exportdatenDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
					exportdatenDto.setExportlaufIId(exportlaufIId);
					exportdatenDto.setIBelegiid(rechnung.getI_id());
					createExportdaten(exportdatenDto);
					
					if (!exportValidator.shouldExport()) {
						myLogger.warn("RE " + rechnung.getC_nr()
								+ " wurde als exportiert markiert");
					}
					// AER-Status setzen
					getRechnungFac().setzeRechnungFibuUebernahme(rechnung.getI_id(),
							theClientDto);
					
				}
			}
			if (em.getExportFormatter() instanceof FibuExportFormatterAbacus) {
				FibuExportFormatterAbacus temp = (FibuExportFormatterAbacus) em
						.getExportFormatter();
				sExport.append(temp.exportiereTA995());
			}
			// die locks aufheben
			sperreRechnungen(re, false, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExport.toString();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public List<String> exportiereBuchungsjournal(BuchungsjournalExportProperties exportProperties,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP { 
		
		
		IBuchungsjournalExportFormatter formatter = null;
		if(DATEV.equals(exportProperties.getFormat()))
			formatter = getBuchungsjournalExportFormatterDatev(exportProperties, theClientDto);
		else if(HV_RAW.equals(exportProperties.getFormat()))
			formatter = getBuchungsjournalExportFormatterHVRaw(exportProperties, theClientDto);
		else if(RZL_CSV.equals(exportProperties.getFormat()))
			formatter = getBuchungsjournalExportFormatterRzl(exportProperties, theClientDto);
		
		if(formatter == null)
			return null;
		return formatter.getExportLines();
	}
	
	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterHVRaw(
			BuchungsjournalExportProperties exportProperties, TheClientDto theClientDto) 
					throws EJBExceptionLP, RemoteException {

		List<Integer> mitlaufendeKonten = getIIdsMitlaufendeKonten(theClientDto);
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRFinanzBuchung.class, "b");
		c.createAlias("b.flrkostenstelle", "ks");
		if (!exportProperties.isMitAutoBuchungen())
			c.add(Restrictions.eq("b.b_autombuchung", Helper.getShortFalse()));
		if (!exportProperties.isMitAutoEroeffnungsbuchungen())
			c.add(Restrictions.eq("b.b_autombuchungeb", Helper.getShortFalse()));
		if (!exportProperties.isMitManEroeffnungsbuchungen())
			c.add(Restrictions.not(Restrictions.like("b.buchungsart_c_nr",
					FinanzFac.BUCHUNGSART_EROEFFNUNG)));
		if(!exportProperties.isMitStornierte())
			c.add(Restrictions.isNull("b.t_storniert"));
		String datumsartFilter = "b." + getDatumsartFilterForFLRFinanzBuchung(exportProperties.getDatumsart());
		
		Timestamp von = new Timestamp(exportProperties.getVon().getTime());
		Timestamp bis = new Timestamp(exportProperties.getBis().getTime());
		bis = Helper.addiereTageZuTimestamp(bis, 1);
		c.add(Restrictions.ge(datumsartFilter, von))
				.add(Restrictions.lt(datumsartFilter, bis))
				.add(Restrictions.like("ks.mandant_c_nr", theClientDto.getMandant()))
				.addOrder(Order.asc(datumsartFilter))
				.addOrder(Order.asc("b.i_id"));
		Iterator<?> iter = c.list().iterator();

		List<FLRFinanzBuchungDetail> details = new ArrayList<FLRFinanzBuchungDetail>();
		while (iter.hasNext()) {
			FLRFinanzBuchung buchung = (FLRFinanzBuchung) iter.next();
			List<?> detailList = session.createCriteria(FLRFinanzBuchungDetail.class)
					.add(Restrictions.eq("buchung_i_id", buchung.getI_id()))
					.addOrder(Order.asc("i_id"))
					.list();
			List<FLRFinanzBuchungDetail> nichtSachkonto = new ArrayList<FLRFinanzBuchungDetail>();
			for(Object o : detailList) {
				FLRFinanzBuchungDetail detail = (FLRFinanzBuchungDetail) o;
				if(!detail.getFlrkonto().getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) nichtSachkonto.add(detail);
				//alle Debit-/Kreditorbuchungen merken
			}
			for(Object o : detailList) {
				FLRFinanzBuchungDetail detail = (FLRFinanzBuchungDetail) o;
				
//				if(detailList.size() == 2) { // wenn nur 2 Buchungsdetails, dann muessen beide Exportiert werden
//					details.add(detail);	//da die Buchung sonst nicht ausgeglichen ist.
//					if(mitlaufendeKonten.contains(detail.getKonto_i_id()))
//						myLogger.warn("Direkte Buchung auf Sammelkonto: " +
//								"buchung_i_id = " + buchung.getI_id() + "; " +
//										"konto: " + detail.getFlrkonto().getC_nr() + " " +
//												"" + detail.getFlrkonto().getC_bez());
//				} else
				if(!mitlaufendeKonten.contains(detail.getKonto_i_id())) { 
					details.add(detail); // nicht mitlaufende Konten muessen immer dabei sein.
				} else {
					boolean mitlaufend = false;
					for(FLRFinanzBuchungDetail nichtSKDetail : nichtSachkonto) {
						if(detail.getN_betrag().equals(nichtSKDetail.getN_betrag())
							&& detail.getBuchungdetailart_c_nr().equals(nichtSKDetail.getBuchungdetailart_c_nr())) {
							mitlaufend = true;
							break;
						}
						//anhand der vorher gemerkten Debi-/Kreditorbuchungen erkennen ob mitlaufende Buchung
					}
					if(!mitlaufend) {
						details.add(detail);

						myLogger.warn("Direkte Buchung auf mitlaufendes Konto: " +
								"buchung_i_id = " + buchung.getI_id() + "; " +
								"belegnr: " + buchung.getC_belegnummer() + "; " +
								"konto: " + detail.getFlrkonto().getC_nr() + " " +
								detail.getFlrkonto().getC_bez() + (buchung.getT_storniert() == null?"" : " ,storniert"));
					}
				}
			}
			
		}		
		return new BuchungsjournalExportHVRawFormatter(details, exportProperties.isMitStornierte());
	}
	
	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterDatev(
			BuchungsjournalExportProperties exportProperties, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BuchungsjournalDatevExporter datevExporter = new BuchungsjournalDatevExporter(exportProperties, theClientDto);
		exportBuchungsjournalImpl(exportProperties, datevExporter, theClientDto);
		return datevExporter;
	}


	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterRzl(
			BuchungsjournalExportProperties exportProperties, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ParametermandantDto pBerater = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_BERATER);
		ParametermandantDto pMandant = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MANDANT);
		ParametermandantDto pKontostellen = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN);
		String berater = pBerater.getCWert();
		String mandant = pMandant.getCWert();
		Integer kontostellen = new Integer(pKontostellen.getCWert());
		
		Integer gf = getBuchenFac().findGeschaeftsjahrFuerDatum(exportProperties.getVon(), theClientDto.getMandant());
		GeschaeftsjahrMandantDto gfDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(gf, theClientDto.getMandant());
		
		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		List<BuchungsjournalExportDatevBuchung> buchungen = getBuchungen0(exportProperties, theClientDto);
		BuchungsjournalDatevExportHeaderFormatter head = new BuchungsjournalDatevExportHeaderFormatter(
				theClientDto, berater, mandant, gfDto.getDBeginndatum()
						.getTime(), kontostellen, exportProperties.getVon().getTime(), exportProperties.getBis().getTime(),
				exportProperties.getBezeichnung(), mDto.getWaehrungCNr());
		return new BuchungsjournalExportRzlFormatter(head, buchungen);
	}

	private List<Integer> getIIdsMitlaufendeKonten(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(theClientDto);
		List<Integer> mitlaufendeKonten = new ArrayList<Integer>();
		for(FinanzamtDto amt : finanzaemter) {
			SteuerkategorieDto[] stkDtos = getFinanzServiceFac().steuerkategorieFindByFinanzamtIId(amt.getPartnerIId(), theClientDto);
			for(SteuerkategorieDto stkat : stkDtos) {
				if(stkat.getKontoIIdForderungen() != null)
					mitlaufendeKonten.add(stkat.getKontoIIdForderungen());
				if(stkat.getKontoIIdVerbindlichkeiten() != null)
					mitlaufendeKonten.add(stkat.getKontoIIdVerbindlichkeiten());
			}
		}
		return mitlaufendeKonten;
				
	}
	
	private String getDatumsartFilterForFLRFinanzBuchung(BuchungsjournalExportDatumsart datumsart) {
		if (BuchungsjournalExportDatumsart.BUCHUNGSDATUM.equals(datumsart)) {
			return "d_buchungsdatum";
		} else if (BuchungsjournalExportDatumsart.GEBUCHTAM.equals(datumsart)) {
			return "t_anlegen";
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, 
					new IllegalArgumentException("Datumsart " + datumsart + " hat noch kein FLR-Mapping fuer Buchungsjournalexport")) ;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private void exportBuchungsjournalImpl(
			BuchungsjournalExportProperties exportProperties, BuchungsjournalExportVisitor exportVisitor, 
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(theClientDto);
		
		Map<Integer, SteuerkontoInfo> mwstKonten = new HashMap<Integer, SteuerkontoInfo>();
		Set<Integer> mitlaufendeKonten = new HashSet<Integer>();
		for(FinanzamtDto amt : finanzaemter) {
			mwstKonten.putAll(getFinanzServiceFac().getAllIIdsSteuerkontoMitIIdMwstBez(amt.getPartnerIId(), theClientDto));
			mitlaufendeKonten.addAll(getFinanzServiceFac().getAllMitlaufendeKonten(amt.getPartnerIId(), theClientDto));
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRFinanzBuchung.class);
		c.createAlias("flrkostenstelle", "ks");
		if (!exportProperties.isMitAutoBuchungen())
			c.add(Restrictions.eq("b_autombuchung", Helper.getShortFalse()));
		if (!exportProperties.isMitAutoEroeffnungsbuchungen())
			c.add(Restrictions.eq("b_autombuchungeb", Helper.getShortFalse()));
		if(!exportProperties.isMitStornierte())
			c.add(Restrictions.isNull("t_storniert"));
		if (!exportProperties.isMitManEroeffnungsbuchungen())
			c.add(Restrictions.not(Restrictions.like("buchungsart_c_nr",
					FinanzFac.BUCHUNGSART_EROEFFNUNG)));
		String datumsartFilter = getDatumsartFilterForFLRFinanzBuchung(exportProperties.getDatumsart());
		
		Timestamp von = new Timestamp(exportProperties.getVon().getTime());
		Timestamp bis = new Timestamp(exportProperties.getBis().getTime());
		bis = Helper.addiereTageZuTimestamp(bis, 1);
		c.add(Restrictions.ge(datumsartFilter, von))
				.add(Restrictions.lt(datumsartFilter, bis))
				.add(Restrictions.like("ks.mandant_c_nr", theClientDto.getMandant()))
				.addOrder(Order.asc(datumsartFilter))
				.addOrder(Order.asc("c_belegnummer"));
		Iterator<?> iter = c.list().iterator();
		while(iter.hasNext()) {
			FLRFinanzBuchung hvBuchung = (FLRFinanzBuchung)iter.next();
			@SuppressWarnings("unchecked")
			List<FLRFinanzBuchungDetail> haben = session.createCriteria(FLRFinanzBuchungDetail.class)
					.createAlias("flrbuchung", "b")
					.add(Restrictions.eq("buchung_i_id", hvBuchung.getI_id()))
					.add(Restrictions.or(
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.HabenBuchung),
								Restrictions.gt("n_betrag", BigDecimal.ZERO)
								),
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.SollBuchung),
								Restrictions.lt("n_betrag", BigDecimal.ZERO)
							)))
					.add(Restrictions.or(
							Restrictions.eq("b.buchungsart_c_nr", FinanzFac.BUCHUNGSART_EROEFFNUNG),
							Restrictions.not(Restrictions.in("konto_i_id", mitlaufendeKonten))
							))
					.addOrder(Order.asc("i_id"))
					.list();
			@SuppressWarnings("unchecked")
			List<FLRFinanzBuchungDetail> soll = session.createCriteria(FLRFinanzBuchungDetail.class)
					.createAlias("flrbuchung", "b")
					.add(Restrictions.eq("buchung_i_id", hvBuchung.getI_id()))
					.add(Restrictions.or(
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.SollBuchung),
								Restrictions.gt("n_betrag", BigDecimal.ZERO)
								),
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.HabenBuchung),
								Restrictions.lt("n_betrag", BigDecimal.ZERO)
							)))
					.add(Restrictions.or(
							Restrictions.eq("b.buchungsart_c_nr", FinanzFac.BUCHUNGSART_EROEFFNUNG),
							Restrictions.not(Restrictions.in("konto_i_id", mitlaufendeKonten))
							))
					.addOrder(Order.asc("i_id"))
					.list();
			
			FLRFinanzKonto flrGegenkonto = null;
			List<FLRFinanzBuchungDetail> zuBuchen;
			if(soll.size() == 1) {
				flrGegenkonto = soll.get(0).getFlrkonto();
				zuBuchen = haben;
			} else if(haben.size() == 1) {
				flrGegenkonto = haben.get(0).getFlrkonto();
				zuBuchen = soll;
			} else {
				zuBuchen = soll;
				zuBuchen.addAll(haben);
			}
			
			exportVisitor.visitBuchung(hvBuchung, flrGegenkonto);
			
			for(int i = 0; i < zuBuchen.size(); i++) {
				FLRFinanzBuchungDetail buchungDetail = zuBuchen.get(i);
				validateSteuerkontobuchungStornierung(hvBuchung, buchungDetail, mwstKonten);

				FLRFinanzBuchungDetail mwstBuchung = zuBuchen.size() > i+1 ? zuBuchen.get(i+1) : null;
				if (mwstBuchung != null
						&& mwstKonten.containsKey(mwstBuchung.getKonto_i_id())) {
					exportVisitor.visitSteuer(buchungDetail, mwstBuchung);
					i++;
				} else {
					exportVisitor.visit(buchungDetail);
				}
			}
			exportVisitor.visitBuchungDone();
		}
	}
	
	private void validateSteuerkontobuchungStornierung(FLRFinanzBuchung hvBuchung, FLRFinanzBuchungDetail hvDetail,
			Map<Integer, SteuerkontoInfo> mwstKonten) {
		if(!mwstKonten.containsKey(hvDetail.getKonto_i_id())) {
			return;
		}

		myLogger.warn("Buchungsdetail " + hvDetail.getI_id() +
				" ist eine unerwartete Mwst-Buchung. Storniert: " + 
				(hvBuchung.getT_storniert() != null));
		// Bei nicht stornierter Buchung handelt es sich um einen Fehler, sonst durchlassen
		if(hvBuchung.getT_storniert() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KONTIERUNG_ZUGEORDNET, "Fehler! keine Mwst-Buchung erwartet!",
			(hvBuchung.getFlrfbbelegart() == null ? hvBuchung.getC_text() : hvBuchung.getFlrfbbelegart().getC_nr()) + " " + hvBuchung.getC_belegnummer());					
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	private List<BuchungsjournalExportDatevBuchung> getBuchungen0(
			BuchungsjournalExportProperties exportProperties, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		String mandant = theClientDto.getMandant();
		ParametermandantDto pMitlaufendesKonto = getParameterFac().getMandantparameter(
				mandant, ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MITLAUFENDES_KONTO);
		String durchlaufKonto = pMitlaufendesKonto.getCWert();
		ParametermandantDto pKontoklassenOhneUst = getParameterFac().getMandantparameter(
				mandant, ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_KONTOKLASSEN_OHNE_BU_SCHLUESSEL);
		List<String> kontoklassenOhneUst = Arrays.asList(pKontoklassenOhneUst.getCWert().split(","));
		
		FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(theClientDto);
		
		Map<Integer, SteuerkontoInfo> mwstKonten = new HashMap<Integer, SteuerkontoInfo>();
		Set<Integer> mitlaufendeKonten = new HashSet<Integer>();
		for(FinanzamtDto amt : finanzaemter) {
			mwstKonten.putAll(getFinanzServiceFac().getAllIIdsSteuerkontoMitIIdMwstBez(amt.getPartnerIId(), theClientDto));
			mitlaufendeKonten.addAll(getFinanzServiceFac().getAllMitlaufendeKonten(amt.getPartnerIId(), theClientDto));
		}
		List<BuchungsjournalExportDatevBuchung> buchungen = new ArrayList<BuchungsjournalExportDatevBuchung>();
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRFinanzBuchung.class);
		c.createAlias("flrkostenstelle", "ks");
		if (!exportProperties.isMitAutoBuchungen())
			c.add(Restrictions.eq("b_autombuchung", Helper.getShortFalse()));
		if (!exportProperties.isMitAutoEroeffnungsbuchungen())
			c.add(Restrictions.eq("b_autombuchungeb", Helper.getShortFalse()));
		if(!exportProperties.isMitStornierte())
			c.add(Restrictions.isNull("t_storniert"));
		if (!exportProperties.isMitManEroeffnungsbuchungen())
			c.add(Restrictions.not(Restrictions.like("buchungsart_c_nr",
					FinanzFac.BUCHUNGSART_EROEFFNUNG)));
		String datumsartFilter = getDatumsartFilterForFLRFinanzBuchung(exportProperties.getDatumsart());
		
		Timestamp von = new Timestamp(exportProperties.getVon().getTime());
		Timestamp bis = new Timestamp(exportProperties.getBis().getTime());
		bis = Helper.addiereTageZuTimestamp(bis, 1);
		c.add(Restrictions.ge(datumsartFilter, von))
				.add(Restrictions.lt(datumsartFilter, bis))
				.add(Restrictions.like("ks.mandant_c_nr", mandant))
				.addOrder(Order.asc(datumsartFilter))
				.addOrder(Order.asc("c_belegnummer"));
		Iterator<?> iter = c.list().iterator();
		while(iter.hasNext()) {
			FLRFinanzBuchung hvBuchung = (FLRFinanzBuchung)iter.next();
			@SuppressWarnings("unchecked")
			List<FLRFinanzBuchungDetail> haben = session.createCriteria(FLRFinanzBuchungDetail.class)
					.createAlias("flrbuchung", "b")
					.add(Restrictions.eq("buchung_i_id", hvBuchung.getI_id()))
					.add(Restrictions.or(
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.HabenBuchung),
								Restrictions.gt("n_betrag", BigDecimal.ZERO)
								),
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.SollBuchung),
								Restrictions.lt("n_betrag", BigDecimal.ZERO)
							)))
					.add(Restrictions.or(
							Restrictions.eq("b.buchungsart_c_nr", FinanzFac.BUCHUNGSART_EROEFFNUNG),
							Restrictions.not(Restrictions.in("konto_i_id", mitlaufendeKonten))
							))
					.addOrder(Order.asc("i_id"))
					.list();
			@SuppressWarnings("unchecked")
			List<FLRFinanzBuchungDetail> soll = session.createCriteria(FLRFinanzBuchungDetail.class)
					.createAlias("flrbuchung", "b")
					.add(Restrictions.eq("buchung_i_id", hvBuchung.getI_id()))
					.add(Restrictions.or(
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.SollBuchung),
								Restrictions.gt("n_betrag", BigDecimal.ZERO)
								),
						Restrictions.and(
								Restrictions.like("buchungdetailart_c_nr", BuchenFac.HabenBuchung),
								Restrictions.lt("n_betrag", BigDecimal.ZERO)
							)))
					.add(Restrictions.or(
							Restrictions.eq("b.buchungsart_c_nr", FinanzFac.BUCHUNGSART_EROEFFNUNG),
							Restrictions.not(Restrictions.in("konto_i_id", mitlaufendeKonten))
							))
					.addOrder(Order.asc("i_id"))
					.list();
			
			String gegenkontoCNr;
			FLRFinanzKonto flrGegenkonto = null;
			List<FLRFinanzBuchungDetail> zuBuchen;
			String uid = null;
			boolean buSchluesselErlaubt = true;
			boolean buSchluesselGanzeBuchung = true;
			if(soll.size() == 1) {
				flrGegenkonto = soll.get(0).getFlrkonto();
				gegenkontoCNr = flrGegenkonto.getC_nr();
				uid = getUIDZuKonto(flrGegenkonto, theClientDto);
				zuBuchen = haben;
			} else if(haben.size() == 1) {
				flrGegenkonto = haben.get(0).getFlrkonto();
				gegenkontoCNr = flrGegenkonto.getC_nr();
				uid = getUIDZuKonto(flrGegenkonto, theClientDto);
				zuBuchen = soll;
			} else {
				zuBuchen = soll;
				zuBuchen.addAll(haben);
				gegenkontoCNr = durchlaufKonto;
			}
			if(flrGegenkonto != null && flrGegenkonto.getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				for(String klassen : kontoklassenOhneUst) {
					if(!klassen.isEmpty() && flrGegenkonto.getC_nr().startsWith(klassen)) {
						buSchluesselGanzeBuchung = false;
						break;
					}
				}
			}
			
//			zuBuchen = getBuchungenKorrekteUst(zuBuchen, mwstKonten);

			for(int i = 0; i < zuBuchen.size(); i++) {
				FLRFinanzBuchungDetail b = zuBuchen.get(i);
				FLRFinanzKonto konto = b.getFlrkonto();
				buSchluesselErlaubt = true;
				if(konto.getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO)) {
					for(String klassen : kontoklassenOhneUst) {
						if(!klassen.isEmpty() && konto.getC_nr().startsWith(klassen)) {
							buSchluesselErlaubt = false;
							break;
						}
					}
				}

				Integer fibuCode = 0;
				BigDecimal umsatz = null;
				if(mwstKonten.containsKey(b.getKonto_i_id())) {
					myLogger.warn("Buchungsdetail " + b.getI_id() +
							" ist eine unerwartete Mwst-Buchung. Storniert: " + 
							(b.getFlrbuchung().getT_storniert() != null));
					// Bei nicht stornierter Buchung handelt es sich um einen Fehler, sonst durchlassen
					if(b.getFlrbuchung().getT_storniert() == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KONTIERUNG_ZUGEORDNET, "Fehler! keine Mwst-Buchung erwartet!",
						(hvBuchung.getFlrfbbelegart() == null ? hvBuchung.getC_text() : hvBuchung.getFlrfbbelegart().getC_nr()) + " " + hvBuchung.getC_belegnummer());					
					}
				}
				if(zuBuchen.size() > i+1) { 
					FLRFinanzBuchungDetail mwstBuchung = zuBuchen.get(i+1);
					if(mwstKonten.containsKey(mwstBuchung.getKonto_i_id())) {
						Integer mwstIId = mwstKonten.get(mwstBuchung.getKonto_i_id()).getMwstsatzbezId() ;
						MwstsatzDto mwstDto;
						if(mwstIId != null) {
							mwstDto = getMandantFac().mwstsatzFindZuDatum(mwstIId, new Timestamp(hvBuchung.getD_buchungsdatum().getTime()));
						} else {
							mwstDto = getMandantFac().getMwstSatzVonBruttoBetragUndUst(mandant, new Timestamp(hvBuchung.getD_buchungsdatum().getTime()),
								b.getN_betrag(), mwstBuchung.getN_betrag());								
						}
						fibuCode = getFibuSteuercode(mwstDto, theClientDto);
						if(fibuCode == null) {
							MwstsatzbezDto mwstsatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(mwstDto.getIIMwstsatzbezId(), theClientDto);
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE, "", mwstsatzbezDto.getCBezeichnung());
						}
						umsatz = b.getN_betrag().add(mwstBuchung.getN_betrag()).abs();
						i++;
					}
				}
//				}
				BuchungsjournalExportDatevBuchung datevBuchung = new BuchungsjournalExportDatevBuchung();
				datevBuchung.setUmsatz(umsatz == null ? b.getN_betrag().abs() : umsatz);
				boolean negativ = b.getN_betrag().signum() < 0;
				datevBuchung.setSoll(negativ != b.getBuchungdetailart_c_nr().equals(BuchenFac.SollBuchung)); //XOR
				datevBuchung.setKonto(b.getFlrkonto().getC_nr());
				datevBuchung.setGegenkonto(gegenkontoCNr);
				if(hvBuchung.getT_storniert() != null) {
					fibuCode += 20;
				}
				if(buSchluesselErlaubt && buSchluesselGanzeBuchung)
					datevBuchung.setBuSchluessel(fibuCode == 0 ? "" : fibuCode.toString());
				datevBuchung.setBelegdatum(hvBuchung.getD_buchungsdatum());
				datevBuchung.setBeleg(hvBuchung.getC_belegnummer());
				datevBuchung.setBuchungstext(hvBuchung.getC_text());
				datevBuchung.setUid(uid == null ? getUIDZuKonto(b.getFlrkonto(), theClientDto) : uid);
				setBelegInfo(datevBuchung, hvBuchung, theClientDto);
				buchungen.add(datevBuchung);
			}
		}
		
		return buchungen;
	}
	
	private String getUIDZuKonto(FLRFinanzKonto konto, TheClientDto theClientDto) {
		if(konto.getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
			KundeDto[] kunden = getKundeFac().kundefindByKontoIIdDebitorenkonto(konto.getI_id());
			String uid = null;
			for (KundeDto kundeDto : kunden) {
				String newUid = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto).getCUid();
				newUid = (newUid == null ? "" : newUid.replaceAll(" ", ""));
				if(uid == null) uid = newUid;
				else if(!uid.equals(newUid)) {
					return null;
				}
			}
			return uid;
		}
		if(konto.getKontotyp_c_nr().equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
			LieferantDto[] lieferanten = getLieferantFac().lieferantfindByKontoIIdKreditorenkonto(konto.getI_id());
			String uid = null;
			for (LieferantDto lieferantDto : lieferanten) {
				String newUid = getPartnerFac().partnerFindByPrimaryKey(lieferantDto.getPartnerIId(), theClientDto).getCUid();
				newUid = (newUid == null ? "" : newUid.replaceAll(" ", ""));
				if(uid == null) uid = newUid;
				else if(!uid.equals(newUid)) {
					return null;
				}
			}
			return uid; 
		}
		return null;
	}

	private String exportiereGutschriften(
			FibuExportKriterienDto fibuExportKriterienDto,
			FibuExportManager em, Integer exportlaufIId,
			java.sql.Date dStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		try {
			FLRRechnung[] gs = getVerbuchbareGutschriften(dStichtag,
					theClientDto);
			// keine daten?
			if (gs == null || gs.length == 0) {
				return null;
			}
			// jetzt alle sperren
			sperreGutschriften(gs, true, theClientDto);
			sExport.append(em.exportiereUeberschriftBelege());
			
			ExportRechnungValidator exportValidator = new ExportRechnungValidator(em, fibuExportKriterienDto);
			for (FLRRechnung gutschrift : gs) {
				exportValidator.validate(gutschrift);
				if (exportValidator.shouldExport()) {
					sExport.append(em.exportiereGutschrift(
							gutschrift.getI_id(), dStichtag));
				}

				if (exportValidator.shouldExport()
						|| exportValidator.shouldMark()) {
					// Exportprotokoll
					ExportdatenDto exportdatenDto = new ExportdatenDto();
					exportdatenDto.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
					exportdatenDto.setExportlaufIId(exportlaufIId);
					exportdatenDto.setIBelegiid(gutschrift.getI_id());
					createExportdaten(exportdatenDto);
					
					if (!exportValidator.shouldExport()) {
						myLogger.warn("GS " + gutschrift.getC_nr()
								+ " wurde als exportiert markiert");
					}
					// GS-Status setzen
					getRechnungFac().setzeRechnungFibuUebernahme(gutschrift.getI_id(),
							theClientDto);
				}
			}
			// die locks aufheben
			sperreGutschriften(gs, false, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExport.toString();
	}
	
	
	protected abstract class ExportBelegValidator {
		private FibuExportKriterienDto kritDto;
		private FibuExportManager fibuEm;
		private boolean shouldExport;
		private boolean shouldMark;
		
		public ExportBelegValidator(FibuExportManager fibuEm, FibuExportKriterienDto kritDto) {
			this.kritDto = kritDto;
			this.fibuEm = fibuEm;
		}
		
		protected void validate() {
			validateImpl();
			shouldExport = shouldExport 
					&& customExportCondition();
		}
		
		private void validateImpl() {
			if (liegtBelegdatumInnerhalbGueltigemExportZeitraum()) {
				shouldExport = true;
				shouldMark = true;
				return;
			}
			
			if (!kritDto.isBAuchBelegeAusserhalbGueltigkeitszeitraum()) {
				shouldExport = false;
				shouldMark = false;
				return;
			}
			
			shouldExport = !kritDto.isBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren();
			shouldMark = true;
		}
		
		public boolean shouldExport() {
			return shouldExport;
		}
		
		public boolean shouldMark() {
			return shouldMark;
		}
		
		protected boolean customExportCondition() {
			return true;
		}
		
		protected abstract boolean liegtBelegdatumInnerhalbGueltigemExportZeitraum();
		
		protected FibuExportManager fibuEm() {
			return fibuEm;
		}
	}
	
	public class ExportRechnungValidator extends ExportBelegValidator {
		private FLRRechnung flrRechnung;

		public ExportRechnungValidator(FibuExportManager fibuEm, FibuExportKriterienDto kritDto) {
			super(fibuEm, kritDto);
		}
		
		public void validate(FLRRechnung flrRechnung) {
			this.flrRechnung = flrRechnung;
			super.validate();
		}

		@Override
		protected boolean customExportCondition() {
			return flrRechnung.getN_wert() != null
					&& flrRechnung.getN_wert().signum() != 0;
		}
		
		@Override
		protected boolean liegtBelegdatumInnerhalbGueltigemExportZeitraum() {
			return fibuEm().liegtBelegdatumInnerhalbGueltigemExportZeitraum(flrRechnung);
		}
	}
	
	public class ExportEingangsrechnungValidator extends ExportBelegValidator {
		private FLREingangsrechnung flrEingangsrechnung;
		
		public ExportEingangsrechnungValidator(FibuExportManager fibuEm, FibuExportKriterienDto kritDto) {
			super(fibuEm, kritDto);
		}
		
		public void validate(FLREingangsrechnung flrEingangsrechnung) {
			this.flrEingangsrechnung = flrEingangsrechnung;
			super.validate();
		}
		
		@Override
		protected boolean liegtBelegdatumInnerhalbGueltigemExportZeitraum() {
			return fibuEm().liegtBelegdatumInnerhalbGueltigemExportZeitraum(flrEingangsrechnung);
		}
	}

	private FLREingangsrechnung[] getVerbuchbareEingangsrechnungen(
			java.sql.Date dStichtag, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLREingangsrechnung.class);
			// Filter nach Mandant
			c.add(Restrictions.eq(EingangsrechnungFac.FLR_ER_MANDANT_C_NR,
					theClientDto.getMandant()));
			if (!anzahlungenExportieren(theClientDto)) {
				// keine Anzahlungen wenn Parameter = 0
				c.add(Restrictions.not(Restrictions.eq(
						EingangsrechnungFac.FLR_ER_EINGANGSRECHNUNGART_C_NR,
						EingangsrechnungFac.EINGANGSRECHNUNGART_ANZAHLUNG)));
			}
			// Noch nicht verbuchte
			c.add(Restrictions
					.isNull(EingangsrechnungFac.FLR_ER_T_FIBUUEBERNAHME));
			// Keine stornierten
			c.add(Restrictions.not(Restrictions.eq(
					EingangsrechnungFac.FLR_ER_STATUS_C_NR,
					EingangsrechnungFac.STATUS_STORNIERT)));
			// PJ 15286 Ohne Eingangsrechnungen mit Betrag = 0
			c.add(Restrictions.not(Restrictions.eq(
					EingangsrechnungFac.FLR_ER_N_BETRAG, new BigDecimal(0))));
			// Stichtag
			c.add(Restrictions.le(EingangsrechnungFac.FLR_ER_D_BELEGDATUM,
					dStichtag));
			c.addOrder(Order.asc(EingangsrechnungFac.FLR_ER_C_NR));
			List<?> list = c.list();
			FLREingangsrechnung[] erArray = new FLREingangsrechnung[list.size()];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLREingangsrechnung er = (FLREingangsrechnung) iter.next();
				erArray[i] = er;
			}
			return erArray;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	private boolean anzahlungenExportieren(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ParametermandantDto parameterAnzahlungen =
				getParameterFac().parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_FINANZ_EXPORT_ANZAHLUNGSRECHNUNG,
						ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant());
		return parameterAnzahlungen.asBoolean();
	}

	private FLRRechnung[] getVerbuchbareRechnungen(java.sql.Date dStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Session session = null;
		try {
			pruefeAufAngelegte(dStichtag, theClientDto,
					RechnungFac.RECHNUNGTYP_RECHNUNG);
			
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnung.class);
			// nur Rechnungen
			Criteria rechnungart = c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.eq(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							RechnungFac.RECHNUNGTYP_RECHNUNG));
			if(!anzahlungenExportieren(theClientDto)) {
				// keine Anzahlung wenn Parameter = 0
				rechnungart.add(Restrictions.not(Restrictions.eq(RechnungFac.FLR_RECHNUNGART_C_NR, RechnungFac.RECHNUNGART_ANZAHLUNG)));
			}
			// Filter nach Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Noch nicht verbuchte
			c.add(Restrictions
					.isNull(RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME));
			// Stichtag
			c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
					dStichtag));
			// Und nach status
			Collection<String> coll = new LinkedList<String>();
			coll.add(RechnungFac.STATUS_BEZAHLT);
			coll.add(RechnungFac.STATUS_OFFEN);
			coll.add(RechnungFac.STATUS_TEILBEZAHLT);
			coll.add(RechnungFac.STATUS_VERBUCHT);
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, coll));
			c.addOrder(Property.forName("c_nr").asc());
			// zu exportierende Belege holen
			List<?> list = c.list();
			FLRRechnung[] reArray = new FLRRechnung[list.size()];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRRechnung re = (FLRRechnung) iter.next();
				reArray[i] = re;
			}
			return reArray;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void pruefeAufAngelegte(Date dStichtag, TheClientDto theClientDto,
			String sRechnungstyp) throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnung.class);
			// nur Rechnungen
			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.eq(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							sRechnungstyp));
			// Filter nach Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Noch nicht verbuchte
			c.add(Restrictions
					.isNull(RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME));
			// Stichtag
			c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
					dStichtag));
			// Und nach status
			Collection<String> coll = new LinkedList<String>();
			// Zuerst schauen, ob es noch angelegte gibt, in diesem fall muss
			// der export abgebrochen werden
			coll.add(RechnungFac.STATUS_ANGELEGT);
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, coll));
			List<?> listAngelegte = c.list();
			if (!listAngelegte.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (Iterator<?> iter = listAngelegte.iterator(); iter
						.hasNext();) {
					FLRRechnung re = (FLRRechnung) iter.next();
					sb.append(re.getC_nr());
					sb.append(" an ").append(
							re.getFlrkunde().getFlrpartner()
									.getC_name1nachnamefirmazeile1());
					sb.append("\n");
				}
				EJBExceptionLP ex = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
						new Exception("noch nicht aktivierte Belege"));
				// Kunden holen
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(sb.toString());
				ex.setAlInfoForTheClient(a);
				throw ex;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private FLRRechnung[] getVerbuchbareGutschriften(java.sql.Date dStichtag,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			pruefeAufAngelegte(dStichtag, theClientDto,
					RechnungFac.RECHNUNGTYP_GUTSCHRIFT);
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnung.class);
			// nur Rechnungen
			c.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART).add(
					Restrictions.eq(
							RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
							RechnungFac.RECHNUNGTYP_GUTSCHRIFT));
			// Filter nach Mandant
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Noch nicht verbuchte
			c.add(Restrictions
					.isNull(RechnungFac.FLR_RECHNUNG_T_FIBUUEBERNAHME));
			// Stichtag
			c.add(Restrictions.le(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM,
					dStichtag));
			// Und nach status
			Collection<String> coll = new LinkedList<String>();
			coll.add(RechnungFac.STATUS_BEZAHLT);
			coll.add(RechnungFac.STATUS_OFFEN);
			coll.add(RechnungFac.STATUS_TEILBEZAHLT);
			coll.add(RechnungFac.STATUS_VERBUCHT);
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, coll));
			// zu exportierende Belege holen
			List<?> list = c.list();
			FLRRechnung[] reArray = new FLRRechnung[list.size()];
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRRechnung re = (FLRRechnung) iter.next();
				reArray[i] = re;
			}
			return reArray;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public ExportdatenDto createExportdaten(ExportdatenDto exportdatenDto)
			throws EJBExceptionLP {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_EXPORTDATEN);
		exportdatenDto.setIId(iId);
		try {
			Exportdaten exportdaten = new Exportdaten(exportdatenDto.getIId(),
					exportdatenDto.getExportlaufIId(), exportdatenDto
							.getBelegartCNr(), exportdatenDto.getIBelegiid());
			entityManager.persist(exportdaten);
			entityManager.flush();
			setExportdatenFromExportdatenDto(exportdaten, exportdatenDto);
			return exportdatenDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private void removeExportdaten(Integer exportdatenIId)
			throws EJBExceptionLP {
		if (exportdatenIId != null) {
			Exportdaten toRemove = entityManager.find(Exportdaten.class,
					exportdatenIId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				entityManager.remove(toRemove);
				entityManager.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
	}

	private void updateExportdaten(ExportdatenDto exportdatenDto)
			throws EJBExceptionLP {
		if (exportdatenDto != null) {
			Integer iId = exportdatenDto.getIId();
			// try {
			Exportdaten exportdaten = entityManager
					.find(Exportdaten.class, iId);
			if (exportdaten == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setExportdatenFromExportdatenDto(exportdaten, exportdatenDto);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
			// }
		}
	}

	public ExportdatenDto exportdatenFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Exportdaten exportdaten = entityManager.find(Exportdaten.class, iId);
		if (exportdaten == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleExportdatenDto(exportdaten);
	}

	public ExportdatenDto exportdatenFindByBelegartCNrBelegiid(
			String belegartCNr, Integer iBelegiid) throws EJBExceptionLP {
		try {
			Query query = entityManager
					.createNamedQuery("ExportdatenfindByBelegartCNrBelegiid");
			query.setParameter(1, belegartCNr);
			query.setParameter(2, iBelegiid);
			Exportdaten exportdaten = (Exportdaten) query.getSingleResult();
			return assembleExportdatenDto(exportdaten);

		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	private void setExportdatenFromExportdatenDto(Exportdaten exportdaten,
			ExportdatenDto exportdatenDto) {
		exportdaten.setExportlaufIId(exportdatenDto.getExportlaufIId());
		exportdaten.setBelegartCNr(exportdatenDto.getBelegartCNr());
		exportdaten.setIBelegiid(exportdatenDto.getIBelegiid());
		entityManager.merge(exportdaten);
		entityManager.flush();
	}

	private ExportdatenDto assembleExportdatenDto(Exportdaten exportdaten) {
		return ExportdatenDtoAssembler.createDto(exportdaten);
	}

	private ExportdatenDto[] assembleExportdatenDtos(Collection<?> exportdatens) {
		List<ExportdatenDto> list = new ArrayList<ExportdatenDto>();
		if (exportdatens != null) {
			Iterator<?> iterator = exportdatens.iterator();
			while (iterator.hasNext()) {
				Exportdaten exportdaten = (Exportdaten) iterator.next();
				list.add(assembleExportdatenDto(exportdaten));
			}
		}
		ExportdatenDto[] returnArray = new ExportdatenDto[list.size()];
		return (ExportdatenDto[]) list.toArray(returnArray);
	}

	public ExportlaufDto createExportlauf(ExportlaufDto exportlaufDto)
			throws EJBExceptionLP {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_EXPORTLAUF);
		exportlaufDto.setIId(iId);
		try {
			Exportlauf exportlauf = new Exportlauf(exportlaufDto.getIId(),
					exportlaufDto.getMandantCNr(), exportlaufDto
							.getPersonalIIdAendern(), exportlaufDto
							.getTStichtag());
			entityManager.persist(exportlauf);
			entityManager.flush();
			exportlaufDto.setTAendern(exportlauf.getTAendern());
			setExportlaufFromExportlaufDto(exportlauf, exportlaufDto);
			return exportlaufDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private void removeExportlauf(Integer exportlaufIId) throws EJBExceptionLP {
		if (exportlaufIId != null) {
			Exportlauf toRemove = entityManager.find(Exportlauf.class,
					exportlaufIId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				entityManager.remove(toRemove);
				entityManager.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
	}

	private void updateExportlauf(ExportlaufDto exportlaufDto)
			throws EJBExceptionLP {
		if (exportlaufDto != null) {
			Integer iId = exportlaufDto.getIId();
			// try {
			Exportlauf exportlauf = entityManager.find(Exportlauf.class, iId);
			if (exportlauf == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setExportlaufFromExportlaufDto(exportlauf, exportlaufDto);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
			// }
		}
	}

	public ExportlaufDto exportlaufFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		Exportlauf exportlauf = entityManager.find(Exportlauf.class, iId);
		if (exportlauf == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleExportlaufDto(exportlauf);
	}

	public Integer exportlaufFindLetztenExportlauf(String mandantCNr)
			throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzExportlauf.class);
			c.add(Restrictions.eq(FinanzFac.FLR_EXPORTLAUF_MANDANT_C_NR,
					mandantCNr));
			// absteigend nach datum -> der letzte kommt als erster
			c.addOrder(Order.desc(FinanzFac.FLR_EXPORTLAUF_T_AENDERN));
			List<?> list = c.list();
			if (list.size() > 0) {
				return ((FLRFinanzExportlauf) list.get(0)).getI_id();
			} else {
				return null;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private void setExportlaufFromExportlaufDto(Exportlauf exportlauf,
			ExportlaufDto exportlaufDto) {
		exportlauf.setMandantCNr(exportlaufDto.getMandantCNr());
		exportlauf.setPersonalIIdAendern(exportlaufDto.getPersonalIIdAendern());
		exportlauf.setTAendern(exportlaufDto.getTAendern());
		entityManager.merge(exportlauf);
		entityManager.flush();
	}

	private ExportlaufDto assembleExportlaufDto(Exportlauf exportlauf) {
		return ExportlaufDtoAssembler.createDto(exportlauf);
	}

	private ExportlaufDto[] assembleExportlaufDtos(Collection<?> exportlaufs) {
		List<ExportlaufDto> list = new ArrayList<ExportlaufDto>();
		if (exportlaufs != null) {
			Iterator<?> iterator = exportlaufs.iterator();
			while (iterator.hasNext()) {
				Exportlauf exportlauf = (Exportlauf) iterator.next();
				list.add(assembleExportlaufDto(exportlauf));
			}
		}
		ExportlaufDto[] returnArray = new ExportlaufDto[list.size()];
		return (ExportlaufDto[]) list.toArray(returnArray);
	}

	private void sperreEingangsrechnungen(FLREingangsrechnung[] er,
			boolean bAddOrRemoveLock, TheClientDto theClientDto)
			throws EJBExceptionLP {
		/**
		 * @todo ER sperren PJ 4260
		 */
	}

	private void sperreRechnungen(FLRRechnung[] er, boolean bAddOrRemoveLock,
			TheClientDto theClientDto) throws EJBExceptionLP {
		/**
		 * @todo AR sperren PJ 4268
		 */
	}

	private void sperreGutschriften(FLRRechnung[] er, boolean bAddOrRemoveLock,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// das laeuft identisch wie bei den Rechnungen
		sperreRechnungen(er, bAddOrRemoveLock, theClientDto);
	}

	public void nehmeExportlaufZurueckUndLoescheIhn(Integer exportlaufIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		// es darf nur der letzte geloescht werden
		Integer iIdLetzter = exportlaufFindLetztenExportlauf(theClientDto
				.getMandant());
		if (!iIdLetzter.equals(exportlaufIId)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_ES_DARF_NUR_DER_LETZTE_GELOESCHT_WERDEN,
					new Exception("Exportlauf " + exportlaufIId
							+ " darf nicht gel\u00F6scht werden, " + iIdLetzter
							+ " ist der letzte"));
		}
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzExportdaten.class);
			c.createCriteria(FinanzFac.FLR_EXPORTDATEN_FLREXPORTLAUF).add(
					Restrictions.eq(FinanzFac.FLR_EXPORTLAUF_I_ID,
							exportlaufIId));
			List<?> list = c.list();
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext(); i++) {
				FLRFinanzExportdaten ex = (FLRFinanzExportdaten) iter.next();
				removeExportdaten(ex.getI_id(), theClientDto);
			}
			// nun den exportlauf loeschen
			removeExportlauf(exportlaufIId);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private String exportierePersonenkonten(FibuExportManager em, String kontotypCNr, boolean nurVerwendete, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		FibuKontoExportDto[] konten = getZuExportierendeKonten(kontotypCNr, nurVerwendete, theClientDto);
		if (konten == null || konten.length == 0) {
			return null;
		}
		// Ueberschriften werden exportiert, wenn Mandantenparameter
		// PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT gesetzt ist.
		sExport.append(em.exportiereUeberschriftPersonenkonten(theClientDto));
		sExport.append(em.exportierePersonenkonten(konten, theClientDto));
		return sExport.toString();
	}

	private FibuKontoExportDto[] getZuExportierendeKonten(String kontotypCNr, boolean nurVerwendete, TheClientDto theClientDto) {
		List<FibuKontoExportDto> konten;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzKonto.class);
			// Filter nach Kontotyp
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR, kontotypCNr));
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR, theClientDto.getMandant()));
			// Sortierung aufsteigend nach Kontonummer
			c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));
			List<?> list = c.list();
			// Array
			konten = new ArrayList<FibuKontoExportDto>(list.size());
//			for (int i = 0; i < konten.length; i++) {
//				konten[i] = new FibuKontoExportDto();
//			}
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzKonto konto = (FLRFinanzKonto) iter.next();
				FibuKontoExportDto exportDto = new FibuKontoExportDto();
				exportDto.setKontoDto(getFinanzFac().kontoFindByPrimaryKey(konto.getI_id()));
				// Fuer ein Debitorenkonto schaun, ob es einen Kunden gibt
				if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
					Criteria cKunde = session.createCriteria(FLRKunde.class);
					cKunde.add(Restrictions.eq(KundeFac.FLR_KUNDE_KONTO_I_ID_DEBITORENKONTO, konto.getI_id()));
					List<?> listKunde = cKunde.list();
					if (!listKunde.isEmpty()) {
						FLRKunde kunde = (FLRKunde) listKunde.get(0);
						exportDto.setPartnerDto(getKundeFac()
								.kundeFindByPrimaryKeyOhneExc(kunde.getI_id(), theClientDto).getPartnerDto());
					}
					else if(nurVerwendete) {
						continue;
					}
				}
				// Fuer ein Kreditorenkonto schaun, ob es einen Lieferanten gibt
				else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
					Criteria cLieferant = session.createCriteria(FLRLieferant.class);
					cLieferant.add(Restrictions.eq(LieferantFac.FLR_KONTO_I_ID_KREDITORENKONTO, konto.getI_id()));
					List<?> listLieferant = cLieferant.list();
					if (!listLieferant.isEmpty()) {
						FLRLieferant lieferant = (FLRLieferant) listLieferant.get(0);
						exportDto.setPartnerDto(getLieferantFac()
								.lieferantFindByPrimaryKey(lieferant.getI_id(), theClientDto).getPartnerDto());
					}
					else if(nurVerwendete) {
						continue;
					}
				}
				konten.add(exportDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return konten.toArray(new FibuKontoExportDto[konten.size()]);
	}

	public void removeExportdaten(Integer exportdatenIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (getTheJudgeFac().hatRecht(RechteFac.RECHT_FB_CHEFBUCHHALTER,
					theClientDto)) {
				ExportdatenDto ex = exportdatenFindByPrimaryKey(exportdatenIId);
				String sBelegnummer = "";
				if (ex.getBelegartCNr().equals(
						LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
					getEingangsrechnungFac()
							.setzeEingangsrechnungFibuUebernahmeRueckgaengig(
									ex.getIBelegiid(), theClientDto);
					EingangsrechnungDto erDto = getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(ex.getIBelegiid());
					sBelegnummer = erDto.getCNr();
				} else if (ex.getBelegartCNr().equals(
						LocaleFac.BELEGART_RECHNUNG)
						|| ex.getBelegartCNr().equals(
								LocaleFac.BELEGART_GUTSCHRIFT)) {
					getRechnungFac().setzeRechnungFibuUebernahmeRueckgaengig(
							ex.getIBelegiid(), theClientDto);
					RechnungDto erDto = getRechnungFac()
							.rechnungFindByPrimaryKey(ex.getIBelegiid());
					sBelegnummer = erDto.getCNr();
				} else {
					// andere belegarten darfs hier nicht geben
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
							new Exception("belegart nicht zulaessig: "
									+ ex.getBelegartCNr()));
				}
				removeExportdaten(ex.getIId());
				myLogger.logData("Export zurueckgenommen: "
						+ ex.getBelegartCNr() + " " + sBelegnummer,
						theClientDto.getIDUser());
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_UNZUREICHENDE_RECHTE,
						new Exception("FB_CHEFBUCHHALTER"));
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public ArrayList<IntrastatDto> exportiereIntrastatmeldung(
			String sVerfahren, java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP {

		dBis = Helper.addiereTageZuDatum(Helper.cutDate(dBis), 1); // auf 24 Uhr
		// setzen,
		// damit
		// alle
		// Daten
		// dieses
		// Tages
		// dabei
		// sind

		ArrayList<IntrastatDto> daten = null;
		if (sVerfahren.equals(FinanzReportFac.INTRASTAT_VERFAHREN_VERSAND)) {
			daten = getIntrastatDatenVersand(dVon, dBis, bdTransportkosten,
					theClientDto);
		} else if (sVerfahren
				.equals(FinanzReportFac.INTRASTAT_VERFAHREN_WARENEINGANG)) {
			daten = getIntrastatDatenWareneingang(dVon, dBis,
					bdTransportkosten, theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"Verfahren = " + sVerfahren));
		}
		
		ArrayList<IntrastatDto> alToRemove = new ArrayList<IntrastatDto>();
		for (IntrastatDto iDto : daten) {
			//PJ19680 alle 0000 00 00 entfernen
			
			if (iDto.getArtikelDto() != null && iDto.getArtikelDto().getCWarenverkehrsnummer() != null && iDto.getArtikelDto().getCWarenverkehrsnummer().equals("0000 00 00")) {
				alToRemove.add(iDto);
			} else {
			
				// pruefen, ob alle Artikel eine gueltige Warenverkehrsnummer haben
				//PJ19680 alle 0000 00 00 entfernen
				if (iDto.getWarenverkehrsnummerDto() == null) {
					if (!(iDto.getUid() == null || iDto.getUid().equals(""))) {
						EJBExceptionLP ex = new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_INTRASTAT_KEINE_WVK_NR,
								new Exception(
										"Beim Artikel ist keine WVK-Nummer eingetragen:"
												+ iDto.getArtikelDto()
														.formatArtikelbezeichnung()));
						ArrayList<Object> aList = new ArrayList<Object>();
						aList.add(iDto.getArtikelDto().formatArtikelbezeichnung());
						ex.setAlInfoForTheClient(aList);
						throw ex;
					} else {
						alToRemove.add(iDto);
					}
				}			
			}

			// Verfahren setzen
			iDto.setVerfahren(sVerfahren);
		}
		for (int i = 0; i < alToRemove.size(); i++) {
			daten.remove(alToRemove.get(i));
		}
		
		return verdichteIntrastatDaten(daten);
	}

	private ArrayList<IntrastatDto> verdichteIntrastatDaten(
			ArrayList<IntrastatDto> list) throws EJBExceptionLP {
		// zuerst sortieren.
		ComparatorIntrastat comparator = new ComparatorIntrastat();
		Collections.sort(list, comparator);
		
		// hier kommen die verdichteten daten rein.
		// Ab 2022 muss die Empfaenger-UID (beim Versand) angegeben werden, 
		// deshalb kann nur noch deutlich weniger verdichtet werden.
		// Ab 2022 muss ein Gewicht > 0 vorhanden sein
		ArrayList<IntrastatDto> aVerdichtet = new ArrayList<IntrastatDto>();
		IntrastatDto iDtoLetztesZiel = null;
		for (IntrastatDto iDto : list) {
			if (iDtoLetztesZiel == null) {
				iDtoLetztesZiel = iDto;
				aVerdichtet.add(iDtoLetztesZiel);
			} else {
				if (comparator.compare(iDtoLetztesZiel, iDto) == 0) {
					myLogger.info("verdichte: " + iDto.getArtikelDto().getCNr() + ", " + iDto.getGewichtInKg() + ".");
					if (iDto.getGewichtInKg().signum() != 1) {
						throw EJBExcFactory.artikelBenoetigtGewicht(iDto.getArtikelDto());
					}
					
					// hab 2 gleiche gefunden. -> Werte addieren
					iDtoLetztesZiel.setMenge(iDtoLetztesZiel.getMenge().add(
							iDto.getMenge()));
					iDtoLetztesZiel.setWert(iDtoLetztesZiel.getWert().add(
							iDto.getWert()));
					iDtoLetztesZiel.setStatistischerWert(iDtoLetztesZiel
							.getStatistischerWert().add(
									iDto.getStatistischerWert()));
					iDtoLetztesZiel.setGewichtInKg(iDtoLetztesZiel
							.getGewichtInKg().add(iDto.getGewichtInKg()));
				} else {
					iDtoLetztesZiel = iDto;
					aVerdichtet.add(iDtoLetztesZiel);
				}
			}
		}
		
		return aVerdichtet;
	}
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public ArrayList<IntrastatDto> getIntrastatDatenWareneingang(
			java.sql.Date dVon, java.sql.Date dBis,
			BigDecimal bdTransportkosten, TheClientDto theClientDto)
			throws EJBExceptionLP {
		ArrayList<IntrastatDto> daten = new ArrayList<IntrastatDto>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session
					.createCriteria(FLRWareneingangspositionen.class);
			Criteria cWE = c
					.createCriteria(WareneingangFac.FLR_WEPOS_FLRWARENEINGANG);
			Criteria cBesPos = c
					.createCriteria(WareneingangFac.FLR_WEPOS_FLRBESTELLPOSITION);
			Criteria cBes = cBesPos
					.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
			Criteria cLieferant = cBes
					.createCriteria(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT);
			Criteria cPartner = cLieferant
					.createCriteria(LieferantFac.FLR_PARTNER);
			Criteria cLandPLZOrt = cPartner
					.createCriteria(PartnerFac.FLR_PARTNER_FLRLANDPLZORT);
			Criteria cLand = cLandPLZOrt
					.createCriteria(SystemFac.FLR_LP_FLRLAND);
			// Nur auslaendische
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			//und die die eine UID-Nummer haben
			// gilt ab Berichtsjahr 2022 nicht mehr
//			cPartner.add(Restrictions.isNotNull(PartnerFac.FLR_PARTNER_C_UID));
			
			cLand
					.add(Restrictions.ne(SystemFac.FLR_LP_LANDLKZ, mandantDto
							.getPartnerDto().getLandplzortDto().getLandDto()
							.getCLkz()));
			// Filter nach Mandant
			cBes.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Filter: Nur Ident-Positionen
			cBesPos
					.add(Restrictions
							.eq(
									BestellpositionFac.FLR_BESTELLPOSITION_BESTELLPOSITIONART_C_NR,
									BestellpositionFac.BESTELLPOSITIONART_IDENT));
			// Wareneingangsdatum von - bis
			cWE.add(Restrictions.ge(
					WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM, dVon)); // von
			// 00
			// :
			// 00
			// :
			// 00
			cWE.add(Restrictions.lt(
					WareneingangFac.FLR_WE_T_WARENEINGANGSDATUM, dBis)); // bis
			// 23
			// :
			// 59
			// :
			// 59

			IntrastatWareneingangBuilder b = new IntrastatWareneingangBuilder(theClientDto);
			
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRWareneingangspositionen wePos = (FLRWareneingangspositionen) iter
						.next();
/*				
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac()
						.artikelFindByPrimaryKey(
								wePos.getFlrbestellposition().getFlrartikel()
										.getI_id(), theClientDto));
*/	
				FLRBestellposition flrBestellposition = wePos.getFlrbestellposition();
				FLRBestellung flrBestellung = wePos.getFlrbestellposition().getFlrbestellung();
				
				b.artikel(flrBestellposition.getFlrartikel().getI_id(),
					wePos.getN_geliefertemenge());
				b.bsBeleg(flrBestellung.getC_nr())
					.lieferant(flrBestellung.getFlrlieferant());
				if (!b.isEU(
						Helper.asTimestamp(flrBestellung.getT_belegdatum()))) {
					myLogger.info("Ignored BS " + flrBestellung.getC_nr() + ", not EU");
					continue;
				}
				b.verkehrszweig(flrBestellung.getFlrspediteur().getC_verkehrszweig());

/*				
				iDto.setBelegart("BS");
				iDto.setBelegnummer(wePos.getFlrbestellposition()
						.getFlrbestellung().getC_nr());
*/						
				// Einstandspreis in Mandantenwaehrung
				BigDecimal bdEinstandspreis;
				if (wePos.getN_einstandspreis() != null) {
					bdEinstandspreis = getLocaleFac()
							.rechneUmInMandantenWaehrung(
									wePos.getN_einstandspreis(),
									wePos.getFlrwareneingang()
											.getN_wechselkurs());
				} else {
					bdEinstandspreis = new BigDecimal(0);
				}
				b.einzelpreis(bdEinstandspreis);
/*				
				iDto.setEinzelpreis(bdEinstandspreis);
				iDto.setMenge(wePos.getN_geliefertemenge() != null ? wePos
						.getN_geliefertemenge() : new BigDecimal(0));
				// Wert = Menge * Preis
				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
*/				
				// Zur Aufteilung der Transportkosten den Gesamtwert des
				// Wareneingangs berechnen
				BigDecimal bdGesamtwertDesWareneingangs = new BigDecimal(0);
				WareneingangspositionDto[] wePositionen = getWareneingangFac()
						.wareneingangspositionFindByWareneingangIId(
								wePos.getWareneingang_i_id());
				for (int i = 0; i < wePositionen.length; i++) {
					if (wePositionen[i].getNGeliefertemenge() != null
							&& wePositionen[i].getNEinstandspreis() != null) {
						bdGesamtwertDesWareneingangs = bdGesamtwertDesWareneingangs
								.add(getLocaleFac()
										.rechneUmInMandantenWaehrung(
												wePositionen[i]
														.getNGeliefertemenge()
														.multiply(
																wePositionen[i]
																		.getNEinstandspreis()),
												wePos.getFlrwareneingang()
														.getN_wechselkurs()));
					}
				}
/*				
				BigDecimal bdAnteiligeTransportkosten;
				if (bdGesamtwertDesWareneingangs.compareTo(new BigDecimal(0)) > 0) {
					bdAnteiligeTransportkosten = bdGesamtwertDesWareneingangs
							.divide(
									bdGesamtwertDesWareneingangs
											.add(bdTransportkosten),
									FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
									BigDecimal.ROUND_HALF_EVEN);
					if (bdAnteiligeTransportkosten.compareTo(new BigDecimal(0)) == 0) {
						bdAnteiligeTransportkosten = new BigDecimal(1);
					}
					iDto.setStatistischerWert(iDto.getWert().divide(
							bdAnteiligeTransportkosten,
							FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
							BigDecimal.ROUND_HALF_EVEN));
				} else {
					// kann nicht aufgeteilt werden
					bdAnteiligeTransportkosten = bdTransportkosten;
					iDto.setStatistischerWert(iDto.getWert().add(
							bdAnteiligeTransportkosten));
				}
*/
				b.versandWert(bdGesamtwertDesWareneingangs, bdTransportkosten);
/*				
				iDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
						wePos.getFlrbestellposition().getFlrbestellung()
								.getFlrlieferant().getFlrpartner().getI_id(),
						theClientDto));
				WarenverkehrsnummerDto wvk = null;
				if (iDto.getArtikelDto().getCWarenverkehrsnummer() != null) {
					wvk = getFinanzServiceFac()
							.warenverkehrsnummerFindByPrimaryKeyOhneExc(
									iDto.getArtikelDto()
											.getCWarenverkehrsnummer());
				}
				BigDecimal bdGewicht = null;
				iDto.setWarenverkehrsnummerDto(wvk);
				if (iDto.getArtikelDto().getFGewichtkg() != null) {
					bdGewicht = iDto.getMenge()
							.multiply(
									new BigDecimal(iDto.getArtikelDto()
											.getFGewichtkg()));
				} else {
					bdGewicht = new BigDecimal(0);
				}
				iDto.setGewichtInKg(bdGewicht);
*/				
				daten.add(b.build());
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return daten;
	}
	
	protected  class IntrastatBuilder {
		private ArtikelDto artikelDto;
		private WarenverkehrsnummerDto wvkDto;
		private TheClientDto theClientDto;
		private BigDecimal amount;
		private String kennungCnr;
		private String belegCnr;
		private BigDecimal einzelpreis;
		// private PartnerDto partnerDto;
		private BigDecimal lsWert;
		private BigDecimal transportKosten;
		private Integer partnerId;
		private PartnerCache partnerCache;
		private MandantDto mandantDto;
		private String verkehrszweig;
		
		public IntrastatBuilder(TheClientDto theClientDto) {
			this.theClientDto = theClientDto;
			this.partnerCache = new PartnerCache(this.theClientDto);
			try {
				this.mandantDto = getMandantFac().mandantFindByPrimaryKey(
					this.theClientDto.getMandant(), this.theClientDto);
			} catch(RemoteException e) {
				throw EJBExcFactory.respectOld(e);
			}
		}
		
		public IntrastatBuilder artikel(Integer artikelId,
				BigDecimal amount) throws RemoteException {
			Validator.notNull(artikelId, "artikelId");

			wvkDto = null;
			einzelpreis = null;
			lsWert = null;
			transportKosten = null;
			partnerId = null;
			kennungCnr = null;
			
			artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					artikelId, this.theClientDto);
			
			if (artikelDto.getCWarenverkehrsnummer() != null) {
				wvkDto = getFinanzServiceFac()
						.warenverkehrsnummerFindByPrimaryKeyOhneExc(
								artikelDto.getCWarenverkehrsnummer());
			}

			this.amount = asDecimal(amount);
			return this;
		}

		/**
		 * Der Einzelpreis der Position
		 * 
		 * @param preis
		 * @return
		 */
		public IntrastatBuilder einzelpreis(BigDecimal preis) {
			this.einzelpreis = asDecimal(preis);
			return this;
		}
		
		/**
		 * Der Warenempfaenger
		 * 
		 * @param partnerId
		 * @return
		 */
		public IntrastatBuilder partner(Integer partnerId) {
			Validator.notNull(partnerId, "partnerId");
			this.partnerId = partnerId;
			return this;
		}
		
		public IntrastatBuilder versandWert(
				BigDecimal lsWert, BigDecimal transportkosten) {
			this.lsWert = lsWert;
			this.transportKosten = transportkosten;
			return this;
		}

		public IntrastatBuilder verkehrszweig(String verkehrszweig) {
			this.verkehrszweig = verkehrszweig;
			return this;
		}
		
		public boolean isEU(Timestamp belegDatum) throws RemoteException {
			Validator.notNullUserMessage(partnerId, "empfaenger() nicht verwendet");			
			PartnerDto partnerDto = partnerCache.getValueOfKey(partnerId);
			
			String laenderart = getFinanzServiceFac().getLaenderartZuPartner(
					mandantDto, partnerDto, belegDatum, theClientDto);
			boolean isIntrastatEU = Helper.isOneOf(laenderart, 
					FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID, 
					FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID);
			if (!isIntrastatEU) {
				// Nordirland (LKZ XI) ist kein EU-Mitglied, aber trotzdem Intrastat (Brexit)
				isIntrastatEU = "XI".equals(partnerDto.getLandplzortDto().getLandDto().getCLkz());
			}

			return isIntrastatEU;
		}

		protected void beleg(String kennungCnr, String belegCnr) {
			Validator.notEmpty(belegCnr, "belegCnr");
			this.belegCnr = belegCnr;
			this.kennungCnr = kennungCnr;		
		}

		private BigDecimal gewicht() {
			if (artikelDto.getFGewichtkg() == null) return BigDecimal.ZERO;

			return this.amount.multiply(BigDecimal.valueOf(artikelDto.getFGewichtkg()));
		}
		
		private BigDecimal asDecimal(BigDecimal value) {
			return value == null ? BigDecimal.ZERO : value;
		}
		
		public IntrastatDto build() {
			IntrastatDto dto = new IntrastatDto();
			dto.setArtikelDto(artikelDto);
			dto.setWarenverkehrsnummerDto(wvkDto);
			dto.setMenge(amount);

//			if(wvkDto != null && "73269098".equals(wvkDto.getCNr().replace(" ", ""))) {
//				System.out.println("halt");
//			}
			Validator.notNullUserMessage(this.kennungCnr, "reBeleg()/lsBeleg() wurde nicht verwendet");
			dto.setBelegart(this.kennungCnr);
			dto.setBelegnummer(this.belegCnr);
			dto.setEinzelpreis(asDecimal(this.einzelpreis));
			
			Validator.notNullUserMessage(this.partnerId, "empfaenger() nicht verwendet");
			dto.setPartnerDto(partnerCache.getValueOfKey(partnerId));
			
			dto.setGewichtInKg(gewicht());
			dto.setWert(dto.getMenge().multiply(asDecimal(this.einzelpreis)));
			
			/*
			BigDecimal bdAnteiligeTransportkosten;
			if (bdGesamtwertDesWareneingangs.compareTo(new BigDecimal(0)) > 0) {
				bdAnteiligeTransportkosten = bdGesamtwertDesWareneingangs
						.divide(
								bdGesamtwertDesWareneingangs
										.add(bdTransportkosten),
								FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
								BigDecimal.ROUND_HALF_EVEN);
				if (bdAnteiligeTransportkosten.compareTo(new BigDecimal(0)) == 0) {
					bdAnteiligeTransportkosten = new BigDecimal(1);
				}
				iDto.setStatistischerWert(iDto.getWert().divide(
						bdAnteiligeTransportkosten,
						FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
						BigDecimal.ROUND_HALF_EVEN));
			} else {
				// kann nicht aufgeteilt werden
				bdAnteiligeTransportkosten = bdTransportkosten;
				iDto.setStatistischerWert(iDto.getWert().add(
						bdAnteiligeTransportkosten));
			}
			
*/
			if (lsWert.signum() > 0) {
				BigDecimal anteilig = lsWert.divide(lsWert.add(transportKosten), 
						FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE, BigDecimal.ROUND_HALF_EVEN);
				if (anteilig.signum() == 0) {
					anteilig = BigDecimal.ONE;
				}
				dto.setStatistischerWert(dto.getWert().divide(anteilig, 
						FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE, BigDecimal.ROUND_HALF_EVEN));
			} else {
				dto.setStatistischerWert(dto.getWert().add(transportKosten));
			}

			// Wir haben/unterstuetzen keine Lohnveredelung
//			dto.setBeistell("1");
			dto.setBeistell(Helper.isStringEmpty(dto.getUid()) ? "12" : "11");
			
			dto.setVerkehrszweig(Helper.isStringEmpty(verkehrszweig) 
					? FinanzReportFac.INTRASTAT_VERKEHRSZWEIG : verkehrszweig);
			return dto;
		}

		private class PartnerCache extends HvCreatingCachingProvider<Integer, PartnerDto> {
			private TheClientDto theClientDto;
			
			public PartnerCache(TheClientDto theClientDto) {
				this.theClientDto = theClientDto;
			}
			
			@Override
			protected PartnerDto provideValue(Integer key, Integer transformedKey) {
				return getPartnerFac().partnerFindByPrimaryKey(transformedKey, theClientDto);
			}			
		}
	}

	public class IntrastatVersandBuilder extends IntrastatBuilder {
		public IntrastatVersandBuilder(TheClientDto theClientDto) {
			super(theClientDto);
		}

		/**
		 * Es handelt sich um einen Lieferschein
		 * 
		 * @param belegCnr die Lieferscheinnummer
		 * @return
		 */
		public IntrastatVersandBuilder lsBeleg(String belegCnr) {
			beleg("LS", belegCnr);
			return this;
		}
		
		/**
		 * Es handelt sich um eine Rechnung
		 * 
		 * @param belegCnr die Rechnungsnummer
		 * @return
		 */
		public IntrastatVersandBuilder reBeleg(String belegCnr) {
			beleg("RE", belegCnr);
			return this;			
		}
		
		public IntrastatVersandBuilder empfaenger(FLRKunde flrKunde) {
			Validator.notNull(flrKunde, "flrKunde");
			partner(flrKunde.getFlrpartner().getI_id());
			return this;
		}

		public IntrastatVersandBuilder empfaenger(Integer partnerId) {
			Validator.notNull(partnerId, "partnerId");
			partner(partnerId);
			return this;
		}
	}
	

	public class IntrastatWareneingangBuilder extends IntrastatBuilder {
		public IntrastatWareneingangBuilder(TheClientDto theClientDto) {
			super(theClientDto);
		}

		/**
		 * Es handelt sich um eine Bestellung 
		 * 
		 * @param belegCnr die Bestellnummer
		 * @return
		 */
		public IntrastatWareneingangBuilder bsBeleg(String belegCnr) {
			beleg("BS", belegCnr);
			return this;
		}

		public IntrastatWareneingangBuilder lieferant(FLRLieferant flrLieferant) {
			Validator.notNull(flrLieferant, "flrLieferant");
			partner(flrLieferant.getFlrpartner().getI_id());
			return this;
		}

		public IntrastatBuilder lieferant(Integer partnerId) {
			Validator.notNull(partnerId, "partnerId");
			partner(partnerId);
			return this;
		}
	}
	
	
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public ArrayList<IntrastatDto> getIntrastatDatenVersand(java.sql.Date dVon,
			java.sql.Date dBis, BigDecimal bdTransportkosten,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<IntrastatDto> daten = new ArrayList<IntrastatDto>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Lieferscheinpositionen
			Criteria cLSPos = session
					.createCriteria(FLRLieferscheinposition.class);
			Criteria cLS = cLSPos
					.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN);
			Criteria cLSKunde = cLS
					.createCriteria(LieferscheinFac.FLR_LIEFERSCHEIN_FLRKUNDE);
			Criteria cLSPartner = cLSKunde
					.createCriteria(LieferantFac.FLR_PARTNER);
			Criteria cLSLandPLZOrt = cLSPartner
					.createCriteria(PartnerFac.FLR_PARTNER_FLRLANDPLZORT);
			Criteria cLSLand = cLSLandPLZOrt
					.createCriteria(SystemFac.FLR_LP_FLRLAND);
			// Nur auslaendische
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			
			//und die die eine UID-Nummer haben
// Gilt ab Berichtsjahr 2022 nicht mehr			
//			cLSPartner.add(Restrictions.isNotNull(PartnerFac.FLR_PARTNER_C_UID));
			
			cLSLand
					.add(Restrictions.ne(SystemFac.FLR_LP_LANDLKZ, mandantDto
							.getPartnerDto().getLandplzortDto().getLandDto()
							.getCLkz()));
			// Filter nach Mandant
			cLS.add(Restrictions.eq(
					LieferscheinFac.FLR_LIEFERSCHEIN_MANDANT_C_NR, theClientDto
							.getMandant()));
			// Filter: Nur Ident-Positionen
			cLSPos
					.add(Restrictions
							.eq(
									LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_LIEFERSCHEINPOSITIONART_C_NR,
									LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT));
			// Belegdatum von - bis
			cLS.add(Restrictions.ge(
					LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM, dVon)); // von
			// 00
			// :
			// 00
			// :
			// 00
			cLS.add(Restrictions.lt(
					LieferscheinFac.FLR_LIEFERSCHEIN_D_BELEGDATUM, dBis)); // bis
			// 23
			// :
			// 59
			// :
			// 59

			IntrastatVersandBuilder b = new IntrastatVersandBuilder(theClientDto);
			
			List<?> listLSPos = cLS.list();
			for (Iterator<?> iter = listLSPos.iterator(); iter.hasNext();) {
				FLRLieferscheinposition lsPos = (FLRLieferscheinposition) iter
						.next();
				FLRLieferschein flrLs = lsPos.getFlrlieferschein();
				
				b.artikel(lsPos.getFlrartikel().getI_id(), lsPos.getN_menge());
				b.lsBeleg(flrLs.getC_nr())
					.empfaenger(flrLs.getFlrkunde())
					.einzelpreis(lsPos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt());
				b.verkehrszweig(flrLs.getFlrspediteur().getC_verkehrszweig());
				
				if (!b.isEU(Helper.asTimestamp(flrLs.getD_belegdatum()))) {
					myLogger.info("Ignored LS " + flrLs.getC_nr() + ", not EU");
					continue;
				}
				
/*				
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
						lsPos.getFlrartikel().getI_id(), theClientDto));
				iDto.setBelegart("LS");
				iDto.setBelegnummer(lsPos.getFlrlieferschein().getC_nr());
*/				
				/**
				 * @todo MB ist das der richtige Preis?
				 * @todo MB Wechselkurs beruecksichtigen
				 */
/*				
				iDto
						.setEinzelpreis(lsPos
								.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt() != null ? lsPos
								.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()
								: new BigDecimal(0));
				iDto.setMenge(lsPos.getN_menge() != null ? lsPos.getN_menge()
						: new BigDecimal(0));
				iDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
						lsPos.getFlrlieferschein().getFlrkunde()
								.getFlrpartner().getI_id(), theClientDto));
*/								
				/**
				 * @todo das mit den Transportkosten noch besser loesen
				 */
				BigDecimal bdGesamtwertDesWareneingangs = BigDecimal.ZERO;
				LieferscheinpositionDto[] lsPositionen = getLieferscheinpositionFac()
						.lieferscheinpositionFindByLieferscheinIId(
								lsPos.getFlrlieferschein().getI_id());
				for (int i = 0; i < lsPositionen.length; i++) {
					if (lsPositionen[i].getNMenge() != null
							&& lsPositionen[i]
									.getNEinzelpreisplusversteckteraufschlag() != null) {
						bdGesamtwertDesWareneingangs = bdGesamtwertDesWareneingangs
								.add(getLocaleFac()
										.rechneUmInMandantenWaehrung(
												lsPositionen[i]
														.getNMenge()
														.multiply(
																lsPositionen[i]
																		.getNEinzelpreisplusversteckteraufschlag()),
												new BigDecimal(
														lsPos
																.getFlrlieferschein()
																.getF_wechselkursmandantwaehrungzulieferscheinwaehrung())));
					}
				}
/*
				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
*/				
				/*
				 * iDto .setStatistischerWert(iDto.getWert().add(
				 * bdTransportkosten));
				 */
				b.versandWert(bdGesamtwertDesWareneingangs, bdTransportkosten);
	
/*				
				BigDecimal bdAnteiligeTransportkosten;
				if (bdGesamtwertDesWareneingangs.compareTo(new BigDecimal(0)) > 0) {
					bdAnteiligeTransportkosten = bdGesamtwertDesWareneingangs
							.divide(
									bdGesamtwertDesWareneingangs
											.add(bdTransportkosten),
									FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
									BigDecimal.ROUND_HALF_EVEN);
					if (bdAnteiligeTransportkosten.compareTo(new BigDecimal(0)) == 0) {
						bdAnteiligeTransportkosten = new BigDecimal(1);
					}
					iDto.setStatistischerWert(iDto.getWert().divide(
							bdAnteiligeTransportkosten,
							FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
							BigDecimal.ROUND_HALF_EVEN));
				} else {
					// kann nicht aufgeteilt werden
					bdAnteiligeTransportkosten = bdTransportkosten;
					iDto.setStatistischerWert(iDto.getWert().add(
							bdAnteiligeTransportkosten));
				}
*/

/*				
				WarenverkehrsnummerDto wvk = null;
				if (iDto.getArtikelDto().getCWarenverkehrsnummer() != null) {
					wvk = getFinanzServiceFac()
							.warenverkehrsnummerFindByPrimaryKeyOhneExc(
									iDto.getArtikelDto()
											.getCWarenverkehrsnummer());
				}
				iDto.setWarenverkehrsnummerDto(wvk);
*/				
/*				
				BigDecimal bdGewicht = null;
				if (iDto.getArtikelDto().getFGewichtkg() != null) {
					bdGewicht = iDto.getMenge()
							.multiply(
									new BigDecimal(iDto.getArtikelDto()
											.getFGewichtkg()));
				} else {
					bdGewicht = new BigDecimal(0);
				}
				iDto.setGewichtInKg(bdGewicht);
				daten.add(iDto);
*/
				daten.add(b.build());
			}

			// Rechnungspositionen
			Criteria cREPos = session.createCriteria(FLRRechnungPosition.class);
			Criteria cRE = cREPos
					.createCriteria(RechnungFac.FLR_RECHNUNGPOSITION_FLRRECHNUNG);
			Criteria cREArt = cRE
					.createCriteria(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART);
			Criteria cREKunde = cRE
					.createCriteria(RechnungFac.FLR_RECHNUNG_FLRKUNDE);
			Criteria cREPartner = cREKunde
					.createCriteria(LieferantFac.FLR_PARTNER);
			Criteria cRELandPLZOrt = cREPartner
					.createCriteria(PartnerFac.FLR_PARTNER_FLRLANDPLZORT);
			Criteria cRELand = cRELandPLZOrt
					.createCriteria(SystemFac.FLR_LP_FLRLAND);
			// Nur auslaendische
			cRELand
					.add(Restrictions.ne(SystemFac.FLR_LP_LANDLKZ, mandantDto
							.getPartnerDto().getLandplzortDto().getLandDto()
							.getCLkz()));
			// Filter nach Mandant
			cRE.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Filter: Nur Ident-Positionen
			cREPos.add(Restrictions.eq(
					RechnungFac.FLR_RECHNUNGPOSITION_POSITIONSART_C_NR,
					RechnungFac.POSITIONSART_RECHNUNG_IDENT));
			// keine stornierten
			cRE.add(Restrictions.ne(RechnungFac.FLR_RECHNUNG_STATUS_C_NR,
					RechnungFac.STATUS_STORNIERT));
			// Belegdatum von - bis
			cRE.add(Restrictions
					.ge(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, dVon)); // von
			// 00:
			// 00:00
			cRE.add(Restrictions
					.lt(RechnungFac.FLR_RECHNUNG_D_BELEGDATUM, dBis)); // bis
			// 23:
			// 59:59
			// nur Rechnungen
			cREArt.add(Restrictions.eq(
					RechnungFac.FLR_RECHNUNGART_RECHNUNGTYP_C_NR,
					RechnungFac.RECHNUNGTYP_RECHNUNG));
			// Query
			List<?> listREPos = cREPos.list();
			for (Iterator<?> iter = listREPos.iterator(); iter.hasNext();) {
				FLRRechnungPosition rePos = (FLRRechnungPosition) iter.next();
				FLRRechnung flrRe = rePos.getFlrrechnung();
				
				// Die Rechnung muss aktiviert sein
				if (flrRe.getStatus_c_nr().equals(
						RechnungFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
							new Exception("Rechnung "
									+ rePos.getFlrrechnung().getC_nr()));
				}
/*				
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
						rePos.getFlrartikel().getI_id(), theClientDto));
				iDto.setBelegart("RE");
				iDto.setBelegnummer(rePos.getFlrrechnung().getC_nr());
*/				
				b.artikel(rePos.getFlrartikel().getI_id(), rePos.getN_menge());
				b.empfaenger(flrRe.getFlrkunde())
					.reBeleg(flrRe.getC_nr())
					.einzelpreis(rePos.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt());

				b.verkehrszweig(flrRe.getFlrspediteur().getC_verkehrszweig());
				
				if (!b.isEU(Helper.asTimestamp(
						flrRe.getD_belegdatum()))) {
					myLogger.info("Ignored AR " + flrRe.getC_nr() + ", not EU");
					continue;
				}
				
				/**
				 * @todo MB ist das der richtige Preis?
				 * @todo MB Wechselkurs beruecksichtigen
				 */
/*				
				iDto
						.setEinzelpreis(rePos
								.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt() != null ? rePos
								.getN_nettoeinzelpreis_plus_aufschlag_minus_rabatt()
								: new BigDecimal(0));
				iDto.setMenge(rePos.getN_menge() != null ? rePos.getN_menge()
						: new BigDecimal(0));
				iDto.setPartnerDto(getPartnerFac().partnerFindByPrimaryKey(
						rePos.getFlrrechnung().getFlrkunde().getFlrpartner()
								.getI_id(), theClientDto));
*/								
				/**
				 * @todo das mit den Transportkosten noch besser loesen
				 */
				BigDecimal bdGesamtwertDesWareneingangs = new BigDecimal(0);
				RechnungPositionDto[] rsPositionen = getRechnungFac()
						.rechnungPositionFindByRechnungIId(
								rePos.getFlrrechnung().getI_id());
				try {
					for (int i = 0; i < rsPositionen.length; i++) {
						if (rsPositionen[i].getNMenge() != null
								&& rsPositionen[i]
										.getNEinzelpreisplusversteckteraufschlag() != null) {
							bdGesamtwertDesWareneingangs = bdGesamtwertDesWareneingangs
									.add(getLocaleFac()
											.rechneUmInMandantenWaehrung(
													rsPositionen[i]
															.getNMenge()
															.multiply(
																	rsPositionen[i]
																			.getNEinzelpreisplusversteckteraufschlag()),

													rePos.getFlrrechnung()
															.getN_kurs()));
						}
					}
				} catch (Exception e) {
					myLogger.error("Exc", e);
				}
/*				
				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
*/				

				b.versandWert(bdGesamtwertDesWareneingangs, bdTransportkosten);
/*				
				BigDecimal bdAnteiligeTransportkosten;
				if (bdGesamtwertDesWareneingangs.compareTo(new BigDecimal(0)) > 0) {
					bdAnteiligeTransportkosten = bdGesamtwertDesWareneingangs
							.divide(
									bdGesamtwertDesWareneingangs
											.add(bdTransportkosten),
									FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
									BigDecimal.ROUND_HALF_EVEN);
					if (bdAnteiligeTransportkosten.compareTo(new BigDecimal(0)) == 0) {
						bdAnteiligeTransportkosten = new BigDecimal(1);
					}
					iDto.setStatistischerWert(iDto.getWert().divide(
							bdAnteiligeTransportkosten,
							FinanzReportFac.INTRASTAT_NACHKOMMASTELLEN_PREISE,
							BigDecimal.ROUND_HALF_EVEN));
				} else {
					// kann nicht aufgeteilt werden
					bdAnteiligeTransportkosten = bdTransportkosten;
					iDto.setStatistischerWert(iDto.getWert().add(
							bdAnteiligeTransportkosten));
				}
*/
/*				
				WarenverkehrsnummerDto wvk = null;
				if (iDto.getArtikelDto().getCWarenverkehrsnummer() != null) {
					wvk = getFinanzServiceFac()
							.warenverkehrsnummerFindByPrimaryKeyOhneExc(
									iDto.getArtikelDto()
											.getCWarenverkehrsnummer());
				}
				iDto.setWarenverkehrsnummerDto(wvk);
				BigDecimal bdGewicht = null;
				iDto.setWarenverkehrsnummerDto(wvk);
				if (iDto.getArtikelDto().getFGewichtkg() != null) {
					bdGewicht = iDto.getMenge()
							.multiply(
									new BigDecimal(iDto.getArtikelDto()
											.getFGewichtkg()));
				} else {
					bdGewicht = new BigDecimal(0);
				}
				iDto.setGewichtInKg(bdGewicht);
*/				
//				daten.add(iDto);
				daten.add(b.build());
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return daten;
	}

	
	private class OpBelegnummer {
		private String prefixAR ;
		private String prefixGS ;
		int belegnrLaenge ;
		int jahrLaenge ;
		String belegTrennzeichen ;
		StringBuffer err = new StringBuffer();
		StringBuffer warn = new StringBuffer();
		int warnCount = 0 ;
		int errCount = 0 ;
		private Collection<IHvBelegnummernformat> bnFormate;
		private Integer idxFormate = 0;
		
//		public OpBelegnummer(String prefixAR, String prefixGS, int belegnrLaenge, int jahrLaenge, String belegTrennzeichen) {
//			this.prefixAR = prefixAR ;
//			this.prefixGS = prefixGS ;
//			this.belegnrLaenge = belegnrLaenge ;
//			this.jahrLaenge = jahrLaenge ;
//			this.belegTrennzeichen = belegTrennzeichen ;
//		}
		
		public OpBelegnummer(String prefixAR, String prefixGS, Collection<IHvBelegnummernformat> bnFormate) {
			this.prefixAR = prefixAR ;
			this.prefixGS = prefixGS ;
			this.bnFormate = bnFormate;
		}
		
		private boolean isValid(IHvBelegnummernformat bnFormat, String anyNumber) {
			String candidate = anyNumber ;
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR)) {
				candidate = anyNumber.substring(prefixAR.length()) ;
			}			
			
			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS)) {
				candidate = anyNumber.substring(prefixGS.length()) ;
			}
			
			if(candidate.length() != bnFormat.getLaengeGesamt()) {
				return false ;
			}

			String trennzeichen = bnFormat.getTrennzeichen() 
					+ (bnFormat.getMandantkennung() != null ? bnFormat.getMandantkennung() : "");
			return candidate.substring(
					bnFormat.getLaengeGj(), bnFormat.getLaengeGj() + trennzeichen.length()).equals(trennzeichen) ;
		}
		
		public boolean isValid(String anyNumber) {
			for (IHvBelegnummernformat format : bnFormate) {
				if (isValid(format, anyNumber))
					return true;
			}
			return false;
//			String candidate = anyNumber ;
//			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR)) {
//				candidate = anyNumber.substring(prefixAR.length()) ;
//			}			
//			
//			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS)) {
//				candidate = anyNumber.substring(prefixGS.length()) ;
//			}
//			
//			if(candidate.length() != jahrLaenge + belegTrennzeichen.length() + belegnrLaenge) {
//				return false ;
//			}
//
//			return candidate.substring(
//				jahrLaenge, jahrLaenge + belegTrennzeichen.length()).equals(belegTrennzeichen) ;
		}
		
		public String formatBelegnummer(IHvBelegnummernformat format, String anyNumber) {
			anyNumber = getExternalBelegnummer(anyNumber);
			String trennzeichen = format.getTrennzeichen() 
					+ (format.getMandantkennung() != null ? format.getMandantkennung() : "");
			int idxTrennzeichen = anyNumber.lastIndexOf(trennzeichen);
			if (idxTrennzeichen < 0)
				return null;
			
			String cLfdNr = anyNumber.substring(idxTrennzeichen + trennzeichen.length());
			if (!StringUtils.isNumeric(cLfdNr))
				return null;
			
			Integer lfdNr = new Integer(cLfdNr);
			if (String.valueOf(lfdNr).length() > format.getLaengeBelegnr())
				return null;
			
			String transformed = anyNumber.substring(0, idxTrennzeichen + 1)
					+ String.format("%0" + format.getLaengeBelegnr() + "d", lfdNr);
			return transformed;
		}
		
		public Collection<IHvBelegnummernformat> getBnFormate() {
			return bnFormate;
		}
		
		/**
		 * Ist prefix AR definiert und die Nummer beginnt mit Prefix und ist gueltig dann AR.
		 * Ist prefix GS definiert und die Nummer beginnt
		 * @param anyNumber
		 * @return
		 */
		public boolean isAR(String anyNumber) {
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR) && isValid(anyNumber)) return true ;
			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS) && isValid(anyNumber)) return false ;
			
			return isValid(anyNumber) ;
		}
		
		public String buildKeyedBelegnummer(String belegTyp, String belegNummer) {
			return belegTyp + "_" + belegNummer ;
		}
		
		public String getKeyedBelegnummer(String anyNumber) {
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR)) {
				return buildKeyedBelegnummer("Rechnung", anyNumber.substring(prefixAR.length())) ;
			}

			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS)) {
				return buildKeyedBelegnummer("Gutschrift", anyNumber.substring(prefixGS.length())) ;
			}
			
			return anyNumber ;
		}
		
		public String getKeyedBelegnummer(boolean bAR, String anyNumber) {
			if(bAR) {
				return buildKeyedBelegnummer("Rechnung", anyNumber) ;
			} else {
				return buildKeyedBelegnummer("Gutschrift", anyNumber) ;
			}
		}
		
		public String getExternalBelegnummer(String anyNumber) {
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR)) {
				return anyNumber.substring(prefixAR.length()) ;
			}

			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS)) {
				return anyNumber.substring(prefixGS.length()) ;
			}
			
			return anyNumber ;			
		}
		
		public void warn(String message) {
			if(warnCount % 10 >= 1) {
				warn.append(",") ; 
			}
						
			warn.append(message) ;
			if(++warnCount % 10 == 0) {
				warn.append("\n") ;
			}
		}
		
		public boolean hasWarnings() {
			return warn.length() > 0 ;
		}
		
		public void error(String message, boolean countAsError) {
			err.append(message + (message.endsWith("\\") ? "" : "\n")) ;
			if(countAsError) {
				++errCount ;
			}
		}
		
		public void error(int linenr, String message) {
			error("Zeile " + linenr + ": " + message, true) ;
		}
		
		public boolean hasErrors() {
			return err.length() > 0 ;
		}
		
		public int getErrorCount() {
			return errCount ;
		}
		
		public String getWarnings() {
			return warn.toString() ;
		}
		
		public String getErrors() {
			return err.toString() ;
		}
	}
	
	private String getStringParameter(String parameterName, TheClientDto theClientDto) {
		String result = "" ;
		
		try {
			ParametermandantDto p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_FINANZ, parameterName);
			result = p.getCWert();
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					parameterName + " fehlt");
		}
		if (result == null || result.length() == 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					parameterName + " fehlt");
		}

		return result ;
	}
	
//	@TransactionTimeout(1000)
	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public String importiereOffenePosten(List<String[]> daten,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		final int MAX_ERROR = 100;
		final int FI_KONTONUMMER = 0;
		final int FI_RECHNUNGSNR = 1;
		final int FI_RECHNUNGSDATUM = 2;
		final int FI_FAELLIGKEIT = 3;
		final int FI_GEGENKONTO = 4;
		final int FI_BETRAGSOLL = 5;
		final int FI_BETRAGHABEN = 6;
		final int FI_SALDO = 7;
		final int FI_SH_SALDO = 8;
		final Integer IMPORT_TEXT = 99999;

		final int CSV_LEN = 9;

		ParametermandantDto p;
		String opPrefixAR = getStringParameter(ParameterFac.PARAMETER_FINANZ_OP_PREFIX_RECHNUNG, theClientDto);
		String opPrefixGS = getStringParameter(ParameterFac.PARAMETER_FINANZ_OP_PREFIX_GUTSCHRIFT, theClientDto);

//		Integer belegNrlaenge = new Integer(7);
//		Integer belegNrlaengeJahr = new Integer(2);
//		String belegNrTrennzeichen = "/";
//		
//		try {
//			p = getParameterFac()
//					.getMandantparameter(
//							theClientDto.getMandant(),
//							ParameterFac.KATEGORIE_ALLGEMEIN,
//							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
//			belegNrlaenge = new Integer(p.getCWert());
//		} catch (Exception e) {
//			//
//		}
//		try {
//			p = getParameterFac()
//					.getMandantparameter(
//							theClientDto.getMandant(),
//							ParameterFac.KATEGORIE_ALLGEMEIN,
//							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);
//			belegNrlaengeJahr = new Integer(p.getCWert());
//		} catch (Exception e) {
//			//
//		}
//		try {
//			p = getParameterFac().getMandantparameter(
//					theClientDto.getMandant(),
//					ParameterFac.KATEGORIE_ALLGEMEIN,
//					ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
//			belegNrTrennzeichen = p.getCWert();
//		} catch (Exception e) {
//			//
//		}
//		OpBelegnummer bn = new OpBelegnummer(opPrefixAR, opPrefixGS, belegNrlaenge, belegNrlaengeJahr, belegNrTrennzeichen) ;

		Collection<IHvBelegnummernformat> validBnFormate = getBelegnummernformate(theClientDto.getMandant());
		OpBelegnummer bn = new OpBelegnummer(opPrefixAR, opPrefixGS, validBnFormate);
		
		String mandantCNr = theClientDto.getMandant();
		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		Integer kontoIId = mDto.getIBankverbindung();
		HashMap<String, String> hmNummern = new HashMap<String, String>();
		for (int i = 1; i < daten.size(); i++) { // bei 1 beginnen, da erste
			myLogger.warn("Importiere Zeile " + i + "...");
			// Zeile Header
			String[] as = daten.get(i);
			if (as.length < CSV_LEN) {
				bn.error(i, "Falsche Feldanzahl:" + as.length + " erwartet:" + CSV_LEN ) ;
			} else {
				if(bn.isValid(as[FI_RECHNUNGSNR])) {
					HvOptional<Rechnung> hvRechnung = HvOptional.empty();
					//OP-Rechnungsnr mit jedem moeglichen Belegnrformat pruefen
					for (IHvBelegnummernformat format : bn.getBnFormate()) {
						HvOptional<String> reNr = HvOptional.ofNullable(bn.formatBelegnummer(format, as[FI_RECHNUNGSNR]));
						if (reNr.isEmpty()) {
							continue;
						}
						
						hvRechnung = findARGS(bn.isAR(as[FI_RECHNUNGSNR]), reNr.get(), mandantCNr);
						if (hvRechnung.isPresent()) {
							break;
						}
					}
					
					if (hvRechnung.isEmpty()) {
						bn.error(i, bn.getKeyedBelegnummer(bn.getExternalBelegnummer(as[FI_RECHNUNGSNR]))
								+ " nicht vorhanden");
						continue;
					}
					
					String reNummer = hvRechnung.get().getCNr();
					String keyedReNummer = bn.getKeyedBelegnummer(bn.isAR(as[FI_RECHNUNGSNR]), hvRechnung.get().getCNr());
					String found = hmNummern.get(keyedReNummer);
					myLogger.warn("> Verarbeite Rechnung " + keyedReNummer + "(" + found + ")");
					if(found != null) {
//						bn.warn(as[FI_RECHNUNGSNR] + "(Raffung?" + i + ")");
						continue ;
					}
					
					hmNummern.put(keyedReNummer, as[FI_RECHNUNGSNR]) ;
					String sErr = importARGS(bn.isAR(as[FI_RECHNUNGSNR]), reNummer, hvRechnung,
							as[FI_SALDO], as[FI_SH_SALDO], "", IMPORT_TEXT,
							kontoIId, mandantCNr, theClientDto);
					myLogger.warn(">> " + sErr);
					if (sErr.length() > 0) {
						bn.error(i,  sErr);
					}
					
//					String rechnungsNummer = bn.getKeyedBelegnummer(as[FI_RECHNUNGSNR]) ;					
//					String found = hmNummern.get(rechnungsNummer) ;
//					myLogger.warn("> Verarbeite Rechnung " + rechnungsNummer + "(" + found + ")");
//					if(found != null) {
////						bn.warn(as[FI_RECHNUNGSNR] + "(Raffung?" + i + ")");
//						continue ;
//					}
//					
//					hmNummern.put(rechnungsNummer, as[FI_RECHNUNGSNR]) ;
//					String sErr = importARGS(bn.isAR(as[FI_RECHNUNGSNR]), bn.getExternalBelegnummer(as[FI_RECHNUNGSNR]),
//							as[FI_SALDO], as[FI_SH_SALDO], "", IMPORT_TEXT,
//							kontoIId, mandantCNr, theClientDto);
//					myLogger.warn(">> " + sErr);
//					if (sErr.length() > 0) {
//						bn.error(i,  sErr);
//					}
				} else {
					bn.warn(as[FI_RECHNUNGSNR] + "(" + i + ")") ;
				}
			}
			
			if (bn.getErrorCount() > MAX_ERROR) {
				bn.error("ACHTUNG: zu viele Fehler (>" + MAX_ERROR, false);
				break;
			}
		}
		
		if (bn.getErrorCount() <= MAX_ERROR) {
			// restliche Rechnungen erledigen
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT r.c_nr, r.flrrechnungart.rechnungtyp_c_nr "
					+ "FROM FLRRechnung AS r "
					+ "WHERE r.t_fibuuebernahme != NULL AND "
					+ "(r.status_c_nr = 'Offen' OR r.status_c_nr = 'Teilbezahlt') AND "
					+ "r.mandant_c_nr='"
					+ mandantCNr
					+ "' AND "
//					+ "(r.flrrechnungart.c_nr IN ('Rechnung', 'Anzahlungsrechnung'))";
					+ "(r.flrrechnungart.rechnungtyp_c_nr IN ('Rechnung', 'Gutschrift'))";
			org.hibernate.Query query = session.createQuery(sQuery);
			List<Object[]> baseList = new ArrayList<Object[]>() ;
			baseList.addAll(query.list()) ;
			Iterator<?> it = baseList.iterator() ;
			
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next() ;
				String cNr = (String) o[0] ;
				String artCnr = (String) o[1] ;
	
//				try {
					String externalBelegNr = hmNummern.get(bn.buildKeyedBelegnummer(artCnr, cNr)) ;
					if (externalBelegNr == null) {
						myLogger.warn("erledigen von " + artCnr + "/" + cNr + "...");
						String sErr = importARGS(!"Gutschrift".equals(artCnr), cNr, "0", "S", "",
								IMPORT_TEXT, kontoIId, mandantCNr, theClientDto);
						if (sErr.length() > 0) {
							bn.error(sErr, true) ;
						}
					}
//				} catch(EJBExceptionLP e) {
//					System.out.println("uups") ;
//				} catch(Exception e) {
//					System.out.println("uups " + e.getMessage()) ;					
//				}
			}
		}
		
		String result = bn.getErrors()  ;
		if (bn.hasWarnings()) {
			result += "\n\nWarnung: Zeilen mit Fremdnummer!\n" ;
			result += bn.getWarnings() ;
		}

		return result ;
	}

	private Collection<IHvBelegnummernformat> getBelegnummernformate(String mandantCnr) {
		Set<IHvBelegnummernformat> validBnFormate = new HashSet<IHvBelegnummernformat>();
		List<HvBelegnummernformatHistorisch> bnFormateAll = getParameterFac().getHvBelegnummernformatHistorischAll(mandantCnr);
		bnFormateAll.forEach(format -> {
			if (format.isValidPattern())
				validBnFormate.add(format);
		});
		IHvBelegnummernformat currentBnFormat = getParameterFac().getHvBelegnummernformat(mandantCnr);
		validBnFormate.add(currentBnFormat);
		return validBnFormate;
	}
	
	private boolean checkRechnungsnummer(String nummer, String prefix,
			int belegNrlaenge, int belegNrlaengeJahr, String belegNrTrennzeichen) {
		String s = null;
		if (prefix.length() > 0) {
			if (nummer.startsWith(prefix)) {
				s = nummer.substring(prefix.length());
			} else {
				return false;
			}
		} else {
			s = nummer;
		}
		if (s.length() != (belegNrlaenge + belegNrlaengeJahr + belegNrTrennzeichen
				.length())) {
			return false;
		}
		if (s.substring(belegNrlaengeJahr,
				belegNrlaengeJahr + belegNrTrennzeichen.length()).equals(
				belegNrTrennzeichen)) {
			return true;
		}
		return false;
	}

	private String importER(String sBelegnummer, String sSaldo,
			String sSHSaldo, String sKurs, String sBuchungsText,
			Integer kontoIId, String sMandantCNr, TheClientDto theClientDto) {
		return "";
	}
	
	private HvOptional<Rechnung> findARGS(boolean bAR, String belegnummer, String mandantCnr) {
		List<String> rechnungarten = bAR
				? getRechnungarten()
				: getGutschriftarten();
				
		return HvOptional.ofNullable(
				RechnungQuery.resultByCNrMehrereRechnungartCNrMandant(
						entityManager, belegnummer, rechnungarten, mandantCnr));
	}

	private String importARGS(boolean bARGS, String sBelegnummer,
			String sSaldo, String sSHSaldo, String sKurs, Integer sBuchungsText,
			Integer kontoIId, String sMandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		HvOptional<Rechnung> rechnung = findARGS(bARGS, sBelegnummer, sMandantCNr);
		return importARGS(bARGS, sBelegnummer, rechnung, sSaldo, sSHSaldo, sKurs, 
				sBuchungsText, kontoIId, sMandantCNr, theClientDto);
	}
	
	private String importARGS(boolean bARGS, String sBelegnummer, HvOptional<Rechnung> rechnungOpt,
			String sSaldo, String sSHSaldo, String sKurs, Integer sBuchungsText,
			Integer kontoIId, String sMandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		String belegBez = (bARGS ? "Rechnung " : "Gutschrift ") + sBelegnummer;

		if (!rechnungOpt.isPresent())
			return belegBez + " nicht vorhanden";
		
		Rechnung rechnung = rechnungOpt.get();

		if (RechnungFac.STATUS_STORNIERT.equals(rechnung.getStatusCNr())) {
			return belegBez + " wurde storniert";
		}
		if(RechnungFac.STATUS_BEZAHLT.equals(rechnung.getStatusCNr())) {
			return belegBez + " ist bereits bezahlt";
		}
			
		if (rechnung.getNWert().signum() == 0) {
			return belegBez + " hat Wert = 0";
		}

		BigDecimal bezahlt = getRechnungFac().getBereitsBezahltWertVonRechnung(
				rechnung.getIId(), null);
		BigDecimal bezahltUst = getRechnungFac()
				.getBereitsBezahltWertVonRechnungUst(rechnung.getIId(), null);

		BigDecimal saldo = new BigDecimal(sSaldo);
		if (bARGS && "H".equals(sSHSaldo)
				|| !bARGS && "S".equals(sSHSaldo)) {
			// Fall der Ueberzahlung bei RE und GS
			saldo = saldo.negate();
		}
		
		BigDecimal saldodiff = rechnung.getNWert().add(rechnung.getNWertust())
				.subtract(bezahlt).subtract(bezahltUst).subtract(saldo);
		// gemischter Steuersatz der Rechnung
		BigDecimal ustproz = rechnung.getNWertust().divide(rechnung.getNWert(),
				BigDecimal.ROUND_HALF_EVEN).movePointRight(2);
		if (saldodiff.signum() < 0) {
			return "Ung\u00FCltiger offener Saldo f\u00FCr " + belegBez + 
					". Negativer Betrag. [" + saldodiff.toPlainString() + 
					", bezahlt=" + bezahlt.add(bezahltUst).toPlainString() + 
					", RE-Wert=" + rechnung.getNWert().toPlainString() + 
					", RE-Ust=" + rechnung.getNWertust() + "]";
		} else if (saldodiff.signum() > 0) {
			RechnungzahlungDto rzDto = new RechnungzahlungDto();

			// Der Zahlbetrag ist immer "saechlich" also mit Steuer
			BigDecimal zahlungsUst = Helper.rundeKaufmaennisch(Helper
					.getMehrwertsteuerBetrag(saldodiff, ustproz.doubleValue()), 2) ;
			BigDecimal zahlungsBetrag = saldodiff.subtract(zahlungsUst) ;

			rzDto.setDZahldatum(new Date(System.currentTimeMillis()));
			rzDto.setNBetrag(zahlungsBetrag);
			rzDto.setNBetragUst(zahlungsUst);

			// TODOD: Kurs und FW Betraege
			rzDto.setNKurs(BigDecimal.ONE);
			rzDto.setNBetragfw(rzDto.getNBetrag());
			rzDto.setNBetragUstfw(rzDto.getNBetragUst());
			rzDto.setRechnungIId(rechnung.getIId());
			rzDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);
			rzDto.setBankkontoIId(kontoIId);
			rzDto.setIAuszug(sBuchungsText);

			getRechnungFac().createZahlung(rzDto, saldo.signum() == 0,
					theClientDto);
			return "";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public ExportdatenDto[] exportdatenFindByExportlaufIIdBelegartCNr(
			Integer exportlaufIId, String belegartCNr) throws EJBExceptionLP {
		Query query = entityManager.createNamedQuery("ExportdatenfindByExportlaufiidBelegartCNr");
		query.setParameter(1, exportlaufIId);
		query.setParameter(2, belegartCNr);
		List<Exportdaten> list = query.getResultList();
		return assembleExportdatenDtos(list);
	}

	private List<String> rechnungarten;
	public List<String> getRechnungarten() {
		if (rechnungarten == null) {
			rechnungarten = Arrays.asList(RechnungFac.RECHNUNGART_RECHNUNG, 
					RechnungFac.RECHNUNGART_ANZAHLUNG, RechnungFac.RECHNUNGART_SCHLUSSZAHLUNG);
		}
		return rechnungarten;
	}
	
	private List<String> gutschriftarten;
	public List<String> getGutschriftarten() {
		if (gutschriftarten == null) {
			gutschriftarten = Arrays.asList(RechnungFac.RECHNUNGART_GUTSCHRIFT, 
					RechnungFac.RECHNUNGART_WERTGUTSCHRIFT);
		}
		return gutschriftarten;
	}

	public interface BuchungsjournalExportVisitor {
		void visitBuchung(FLRFinanzBuchung flrBuchung, FLRFinanzKonto flrGegenkonto);
		void visit(FLRFinanzBuchungDetail flrDetail);
		void visitSteuer(FLRFinanzBuchungDetail flrDetail1, FLRFinanzBuchungDetail flrDetail2);
		void visitBuchungDone();
	}
	
	private void setBelegInfo(BuchungsjournalExportDatevBuchung datevBuchung, FLRFinanzBuchung hvBuchung, TheClientDto theClientDto) {
		String belegart = hvBuchung.getFlrfbbelegart() != null ? hvBuchung.getFlrfbbelegart().getC_nr() : "";
		String belegnummer = hvBuchung.getC_belegnummer() != null ? hvBuchung.getC_belegnummer().trim() : "";
		if (Helper.isStringEmpty(belegart) || Helper.isStringEmpty(belegnummer))
			return;
		
		if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegart)) {
			Eingangsrechnung eingangsrechnung = EingangsrechnungQuery.resultByCNrMandantCNrOhneZusatzkosten(
					entityManager, belegnummer, theClientDto.getMandant());
			if (eingangsrechnung == null) return;
			
			datevBuchung.setBelegInfo1(eingangsrechnung.getCLieferantenrechnungsnummer());
			
		} else if (LocaleFac.BELEGART_RECHNUNG.equals(belegart) 
				|| LocaleFac.BELEGART_GUTSCHRIFT.equals(belegart)) {
			List<String> rechnungarten = LocaleFac.BELEGART_RECHNUNG.equals(belegart) ? 
					getRechnungarten() : getGutschriftarten();
					
			Rechnung rechnung = RechnungQuery.resultByCNrMehrereRechnungartCNrMandant(
					entityManager, belegnummer, rechnungarten, theClientDto.getMandant());
			if (rechnung == null) return;
			
			datevBuchung.setBelegInfo1(rechnung.getCBestellnummer());
			datevBuchung.setBelegInfo2(rechnung.getCBez());
		}
	}

	public class HvBuchungExport {
		private FLRFinanzBuchung flrBuchung;
		private FLRFinanzKonto flrGegenkonto;
		
		private EingangsrechnungDto erDto;
		private RechnungDto rechnungDto;
		
		public HvBuchungExport() {
		}
		
		public FLRFinanzBuchung getFlrBuchung() {
			return flrBuchung;
		}

		public FLRFinanzKonto getFlrGegenkonto() {
			return flrGegenkonto;
		}
		
		public void setErDto(EingangsrechnungDto erDto) {
			this.erDto = erDto;
		}
		
		public EingangsrechnungDto getErDto() {
			return erDto;
		}
		
		public boolean isEingangsrechnung() {
			return getErDto() != null;
		}
		
		public void setRechnungDto(RechnungDto rechnungDto) {
			this.rechnungDto = rechnungDto;
		}
		
		public RechnungDto getRechnungDto() {
			return rechnungDto;
		}
		
		public boolean isRechnung() {
			return getRechnungDto() != null;
		}

		public void init(FLRFinanzBuchung flrBuchung, FLRFinanzKonto flrGegenkonto, TheClientDto theClientDto) {
			this.flrBuchung = flrBuchung;
			this.flrGegenkonto = flrGegenkonto;
			
			try {
				String belegart = flrBuchung.getFlrfbbelegart() != null ? flrBuchung.getFlrfbbelegart().getC_nr() : "";
			
				BelegbuchungDto belegBuchungDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBuchungIIdOhneExc(flrBuchung.getI_id());
				if (belegBuchungDto == null) {
					// Handelt sich um Umbuchung und dergleichen
					return;
				}
				
				Integer belegId = belegBuchungDto.getIBelegiid();
				
				if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegart)) {
					if (LocaleFac.BELEGART_ERZAHLUNG.equals(belegBuchungDto.getBelegartCNr())) {
						EingangsrechnungzahlungDto erzDto = getEingangsrechnungFac()
								.eingangsrechnungzahlungFindByPrimaryKey(belegId);
						belegId = erzDto.getEingangsrechnungIId();
					} 
					setErDto(getEingangsrechnungFac()
							.eingangsrechnungFindByPrimaryKey(belegId));			
				} else if (LocaleFac.BELEGART_RECHNUNG.equals(belegart) || 
						LocaleFac.BELEGART_GUTSCHRIFT.equals(belegart)) {
					if (LocaleFac.BELEGART_REZAHLUNG.equals(belegBuchungDto.getBelegartCNr())) {
						RechnungzahlungDto rezDto = getRechnungFac()
								.rechnungzahlungFindByPrimaryKey(belegId);
						belegId = rezDto.getRechnungIId();
					}
					
					setRechnungDto(getRechnungFac()
							.rechnungFindByPrimaryKey(belegId));						
				}
/*				
				String belegnummer = flrBuchung.getC_belegnummer() != null ? flrBuchung.getC_belegnummer().trim() : "";
				if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegart)) {
					Eingangsrechnung eingangsrechnung = EingangsrechnungQuery.resultByCNrMandantCNrOhneZusatzkosten(
							entityManager, belegnummer, theClientDto.getMandant());
					if (eingangsrechnung != null) {
						setErDto(getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(eingangsrechnung.getIId()));
					}
				} else if (LocaleFac.BELEGART_RECHNUNG.equals(belegart) 
						|| LocaleFac.BELEGART_GUTSCHRIFT.equals(belegart)) {
					List<String> rechnungarten = LocaleFac.BELEGART_RECHNUNG.equals(belegart) ? 
							getRechnungarten() : getGutschriftarten();
					Rechnung rechnung = RechnungQuery.resultByCNrMehrereRechnungartCNrMandant(
							entityManager, belegnummer, rechnungarten, theClientDto.getMandant());
					if (rechnung != null) { 
						setRechnungDto(getRechnungFac().rechnungFindByPrimaryKey(rechnung.getIId()));
					}
				}
*/				
			} catch (RemoteException re) {
				throwEJBExceptionLPRespectOld(re);
			}
		}
	}
	
	public class BuchungsjournalDatevExporter implements BuchungsjournalExportVisitor, IBuchungsjournalExportFormatter {
		private String durchlaufKonto;
		private List<String> kontoklassenOhneBU;
		private Map<Integer, SteuerkontoInfo> mwstKonten;
		private HvCreatingCachingProvider<Integer, MwstsatzDto> mwstsatzCache;
		private ReversechargeartDto rcOhneDto;
		private TheClientDto theClientDto;
		private BuchungsjournalExportProperties exportProperties;
		
		private HvBuchungExport hvBuchung;
		private boolean buSchluesselBuchungForbidden;
		
		private List<BuchungsjournalExportDatevBuchung> resultBuchungen;
		
		private BuchungsfallVisitor buchungsfallVisitor;

		public BuchungsjournalDatevExporter(BuchungsjournalExportProperties exportProperties, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			this.theClientDto = theClientDto;
			this.exportProperties = exportProperties;
			initParams();
			initFormatter();
		}
		
		public void setBuSchluesselBuchungForbidden(boolean buSchluesselBuchungForbidden) {
			this.buSchluesselBuchungForbidden = buSchluesselBuchungForbidden;
		}
		
		public boolean isBuSchluesselBuchungForbidden() {
			return buSchluesselBuchungForbidden;
		}
		
		private void initParams() throws EJBExceptionLP, RemoteException {
			durchlaufKonto = getParameterFac().getExportDatevMitlaufendesKonto(theClientDto.getMandant());
			kontoklassenOhneBU = getParameterFac().getExportDatevKontoklassenOhneBuSchluessel(theClientDto.getMandant());

			mwstKonten = new HashMap<Integer, SteuerkontoInfo>();
			FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(theClientDto);
			for(FinanzamtDto amt : finanzaemter) {
				mwstKonten.putAll(getFinanzServiceFac().getAllIIdsSteuerkontoMitIIdMwstBez(amt.getPartnerIId(), theClientDto));
			}
			
			mwstsatzCache = new HvCreatingCachingProvider<Integer, MwstsatzDto>() {
				protected MwstsatzDto provideValue(Integer kontoId, Integer transformedKey) {
					Integer mwstIId = mwstKonten.get(kontoId).getMwstsatzbezId();
					if (mwstIId == null) 
						return null;
					
					MwstsatzDto mwstDto = getMandantFac().mwstsatzFindZuDatum(
							mwstIId, new Timestamp(getHvBuchung().getFlrBuchung().getD_buchungsdatum().getTime()));
					return mwstDto;
				}
			};
			rcOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);
			resultBuchungen = new ArrayList<BuchungsjournalExportDatevBuchung>();
		}

		private void resetHvBuchung() {
			hvBuchung = null;
		}
		
		private HvBuchungExport getHvBuchung() {
			if (hvBuchung == null) {
				hvBuchung = new HvBuchungExport();
			}
			return hvBuchung;
		}

		private boolean isBuSchluesselRequiredForBuchungDetail(FLRFinanzBuchungDetail flrDetail) {
			if (isBuSchluesselBuchungForbidden())
				return false;
			
			return validateBuSchluesselRequired(flrDetail.getFlrkonto());
		}

		private boolean validateBuSchluesselRequired(FLRFinanzKonto flrKonto) {
			if(flrKonto == null 
					|| !FinanzServiceFac.KONTOTYP_SACHKONTO.equals(flrKonto.getKontotyp_c_nr())) {
				return true;
			}

			for(String klassen : kontoklassenOhneBU) {
				if(!klassen.isEmpty() && flrKonto.getC_nr().startsWith(klassen)) {
					return false;
				}
			}

			return true;
		}
		
		private void initHvBuchung(FLRFinanzBuchung flrBuchung, FLRFinanzKonto flrGegenkonto) {
			getHvBuchung().init(flrBuchung, flrGegenkonto, theClientDto);
			// TODO Exc wenn Durchlaufkonto == null
			setBuSchluesselBuchungForbidden(!validateBuSchluesselRequired(flrGegenkonto));
		}
		
		private BuchungsjournalExportDatevBuchung setupBuchungsjournalExportDatevBuchung(FLRFinanzBuchung flrBuchung, FLRFinanzKonto flrGegenkonto) {
			BuchungsjournalExportDatevBuchung datevBuchung = new BuchungsjournalExportDatevBuchung();
			datevBuchung.setGegenkonto(flrGegenkonto != null ? flrGegenkonto.getC_nr() : durchlaufKonto);
			datevBuchung.setBelegdatum(flrBuchung.getD_buchungsdatum());
			datevBuchung.setBeleg(flrBuchung.getC_belegnummer());
			datevBuchung.setBuchungstext(flrBuchung.getC_text());
			datevBuchung.setUid(flrGegenkonto != null ? getUIDZuKonto(flrGegenkonto, theClientDto) : null);
			
			if (getHvBuchung().isEingangsrechnung()) {
				datevBuchung.setBelegInfo1(getHvBuchung().getErDto().getCLieferantenrechnungsnummer());
			} else if (getHvBuchung().isRechnung()) {
				datevBuchung.setBelegInfo1(getHvBuchung().getRechnungDto().getCBestellnummer());
				datevBuchung.setBelegInfo2(getHvBuchung().getRechnungDto().getCBez());
			}
			
			return datevBuchung;
		}
		
		public boolean isEingangsrechnungIgErwerb() {
			return getHvBuchung().isEingangsrechnung()
					&& Helper.short2boolean(getHvBuchung().getErDto().getBIgErwerb())
					&& FinanzFac.BUCHUNGSART_BUCHUNG.equals(getHvBuchung().getFlrBuchung().getBuchungsart_c_nr());
		}
		
		public boolean isEingangsrechnungReverseCharge() {
			return getHvBuchung().isEingangsrechnung()
					&& !rcOhneDto.getIId().equals(getHvBuchung().getErDto().getReversechargeartId())
					&& FinanzFac.BUCHUNGSART_BUCHUNG.equals(getHvBuchung().getFlrBuchung().getBuchungsart_c_nr());
		}

		private void initBuchungsfallVisitor() {
			if (getHvBuchung().getFlrBuchung().getT_storniert() != null) {
				buchungsfallVisitor = new DatevBuchungDefault();
			} else if (isEingangsrechnungIgErwerb()) {
				buchungsfallVisitor = new DatevBuchungErIgErwerb();
			} else if (isEingangsrechnungReverseCharge()) {
				buchungsfallVisitor = new DatevBuchungErReverseCharge();
			} else {
				buchungsfallVisitor = new DatevBuchungDefault();
			}
		}
		
		public BuchungsfallVisitor getBuchungsfallVisitor() {
			return buchungsfallVisitor;
		}

		private Integer getFibuCode(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail) {
			if(!mwstKonten.containsKey(mwstDetail.getKonto_i_id())) {
				return 0;
			}
			
			MwstsatzDto mwstDto = mwstsatzCache.getValueOfKey(mwstDetail.getKonto_i_id());
			if (mwstDto == null) {
				mwstDto = getMandantFac().getMwstSatzVonBruttoBetragUndUst(
						theClientDto.getMandant(), new Timestamp(getHvBuchung().getFlrBuchung().getD_buchungsdatum().getTime()),
						nettoDetail.getN_betrag(), mwstDetail.getN_betrag());
			}
			
			return getFibuSteuercode(mwstDto, theClientDto);
		}

		@Override
		public void visitBuchung(FLRFinanzBuchung flrBuchung, FLRFinanzKonto flrGegenkonto) {
			resetHvBuchung();
			initHvBuchung(flrBuchung, flrGegenkonto);
			
			initBuchungsfallVisitor();
		}

		@Override
		public void visit(FLRFinanzBuchungDetail flrDetail) {
			getBuchungsfallVisitor().visit(flrDetail);
		}

		@Override
		public void visitSteuer(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail) {
			getBuchungsfallVisitor().visitSteuer(nettoDetail, mwstDetail);
		}

		@Override
		public void visitBuchungDone() {
			resultBuchungen.addAll(getBuchungsfallVisitor().getBuchungen());
		}
		
		public List<BuchungsjournalExportDatevBuchung> getBuchungen() {
			return resultBuchungen;
		}
		
		private BuchungsjournalExportDatevBuchung createDefaultBuchung(FLRFinanzBuchungDetail flrDetail) {
			BuchungsjournalExportDatevBuchung datevDetail = setupBuchungsjournalExportDatevBuchung(getHvBuchung().getFlrBuchung(), getHvBuchung().getFlrGegenkonto());
			datevDetail.setUmsatz(flrDetail.getN_betrag().abs());
			boolean negativ = flrDetail.getN_betrag().signum() < 0;
			datevDetail.setSoll(negativ != BuchenFac.SollBuchung.equals(flrDetail.getBuchungdetailart_c_nr()));
			datevDetail.setKonto(flrDetail.getFlrkonto().getC_nr());
			datevDetail.setUid(datevDetail.getUid() != null ? datevDetail.getUid() : getUIDZuKonto(flrDetail.getFlrkonto(), theClientDto));
			if (isBuSchluesselRequiredForBuchungDetail(flrDetail)) {
				datevDetail.setBuSchluessel(getHvBuchung().getFlrBuchung().getT_storniert() != null ? "20" : "");
			}
			
			return datevDetail;
		}
		
		private BuchungsjournalExportDatevBuchung createDefaultSteuerBuchung(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail) {
			BuchungsjournalExportDatevBuchung datevDetail = setupBuchungsjournalExportDatevBuchung(
					getHvBuchung().getFlrBuchung(), getHvBuchung().getFlrGegenkonto());
			datevDetail.setUmsatz(nettoDetail.getN_betrag().add(mwstDetail.getN_betrag()).abs());
			
			boolean negativ = nettoDetail.getN_betrag().signum() < 0;
			datevDetail.setSoll(negativ != BuchenFac.SollBuchung.equals(nettoDetail.getBuchungdetailart_c_nr()));
			datevDetail.setKonto(nettoDetail.getFlrkonto().getC_nr());
			datevDetail.setUid(datevDetail.getUid() != null ? datevDetail.getUid() : getUIDZuKonto(nettoDetail.getFlrkonto(), theClientDto));
			if (isBuSchluesselRequiredForBuchungDetail(nettoDetail)) {
				Integer fibuCode = getFibuCode(nettoDetail, mwstDetail);
				if (getHvBuchung().getFlrBuchung().getT_storniert() != null) {
					fibuCode += 20;
				}
				datevDetail.setBuSchluessel(fibuCode.equals(0) ? "" : fibuCode.toString());
			}
			return datevDetail;
		}

		public class DatevBuchungDefault implements BuchungsfallVisitor {
			private List<BuchungsjournalExportDatevBuchung> datevBuchungen = new ArrayList<BuchungsjournalExportDatevBuchung>();
			
			@Override
			public void visit(FLRFinanzBuchungDetail flrDetail) {
				datevBuchungen.add(createDefaultBuchung(flrDetail));
			}

			@Override
			public void visitSteuer(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail) {
				datevBuchungen.add(createDefaultSteuerBuchung(nettoDetail, mwstDetail));
			}

			@Override
			public List<BuchungsjournalExportDatevBuchung> getBuchungen() {
				return datevBuchungen;
			}
		}
		
		public class DatevBuchungErIgErwerb implements BuchungsfallVisitor {
			private List<FLRFinanzBuchungDetail> flrDetails = new ArrayList<FLRFinanzBuchungDetail>();
			
			@Override
			public void visit(FLRFinanzBuchungDetail flrDetail) {
				flrDetails.add(flrDetail);
			}

			@Override
			public void visitSteuer(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail) {
				flrDetails.add(nettoDetail);
				flrDetails.add(mwstDetail);
			}

			@Override
			public List<BuchungsjournalExportDatevBuchung> getBuchungen() {
				if (flrDetails.size() == 1) {
					return Arrays.asList(createDefaultBuchung(flrDetails.get(0)));
				}
				if (flrDetails.size() == 4) {
					FLRFinanzBuchungDetail nettoDetail = flrDetails.get(0);
					FLRFinanzBuchungDetail mwstDetail = flrDetails.get(1);
					BuchungsjournalExportDatevBuchung igErwerbBuchung = createDefaultSteuerBuchung(nettoDetail, mwstDetail);
					igErwerbBuchung.setUmsatz(nettoDetail.getN_betrag().abs()); // Nettobetrag
					
					FLRFinanzBuchungDetail kreditorDetail = flrDetails.get(2);
					igErwerbBuchung.setGegenkonto(kreditorDetail.getFlrkonto().getC_nr());
					igErwerbBuchung.setUid(igErwerbBuchung.getUid() != null ? igErwerbBuchung.getUid() : getUIDZuKonto(kreditorDetail.getFlrkonto(), theClientDto));
					return Arrays.asList(igErwerbBuchung);
				}
				
				if (flrDetails.size() == 6) {
					FLRFinanzBuchungDetail nettoDetail = flrDetails.get(0);
					FLRFinanzBuchungDetail mwstDetail = flrDetails.get(1);
					BuchungsjournalExportDatevBuchung igErwerbBuchung = createDefaultSteuerBuchung(nettoDetail, mwstDetail);
					igErwerbBuchung.setUmsatz(nettoDetail.getN_betrag().abs()); // Nettobetrag
					
					FLRFinanzBuchungDetail kreditorDetail = flrDetails.get(3);
					igErwerbBuchung.setGegenkonto(kreditorDetail.getFlrkonto().getC_nr());
					igErwerbBuchung.setUid(igErwerbBuchung.getUid() != null ? igErwerbBuchung.getUid() : getUIDZuKonto(kreditorDetail.getFlrkonto(), theClientDto));
					BuchungsjournalExportDatevBuchung anzahlungBuchung = createDefaultBuchung(flrDetails.get(2));
					anzahlungBuchung.setGegenkonto(flrDetails.get(2).getFlrgegenkonto().getC_nr());
					return Arrays.asList(igErwerbBuchung, anzahlungBuchung);
				}

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, 
						"Unerwartete Anzahl an Buchungsdetails (" + flrDetails.size() 
						+ ") fuer ER mit IG-Erwerb oder ReverseCharge [Buchung-Id=" + getHvBuchung().getFlrBuchung().getI_id() + "]");
			}
		}
		
		public class DatevBuchungErReverseCharge extends DatevBuchungErIgErwerb {
		}
		
		private BuchungsjournalDatevExportHeaderFormatter headerFormatter;
		
		private void initFormatter() throws RemoteException, EJBExceptionLP {
			initHeaderFormatter();
		}
		
		private void initHeaderFormatter() throws RemoteException, EJBExceptionLP {
			ParametermandantDto pBerater = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_BERATER);
			ParametermandantDto pMandant = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MANDANT);
			ParametermandantDto pKontostellen = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN);
			String berater = pBerater.getCWert();
			String mandant = pMandant.getCWert();
			Integer kontostellen = new Integer(pKontostellen.getCWert());
			
			Integer gf = getBuchenFac().findGeschaeftsjahrFuerDatum(exportProperties.getVon(), theClientDto.getMandant());
			GeschaeftsjahrMandantDto gfDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(gf, theClientDto.getMandant());
			
			MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			headerFormatter = new BuchungsjournalDatevExportHeaderFormatter(
					theClientDto, berater, mandant, gfDto.getDBeginndatum().getTime(), 
					kontostellen, exportProperties.getVon().getTime(), exportProperties.getBis().getTime(),
					exportProperties.getBezeichnung(), mDto.getWaehrungCNr());
		}

		@Override
		public List<String> getExportLines() {
			List<String> lines = new ArrayList<String>();
			lines.add(headerFormatter.format());
			
			boolean printTitles = true;
			for (BuchungsjournalExportDatevBuchung b : getBuchungen()) {
				if(printTitles) {
					lines.add(new DatevExportBuchungFormatter(b).formatFieldNames(false));
					printTitles = false;
				}
				lines.add(new DatevExportBuchungFormatter(b).format());
			}		
			return lines;
		}
	}

	public interface BuchungsfallVisitor {
		void visit(FLRFinanzBuchungDetail flrDetail);
		void visitSteuer(FLRFinanzBuchungDetail nettoDetail, FLRFinanzBuchungDetail mwstDetail);
		List<BuchungsjournalExportDatevBuchung> getBuchungen();
	}
	
	private Integer getFibuSteuercode(MwstsatzDto mwstsatzDto, TheClientDto theClientDto) {
		try {
			// TODO: Noch auf den jeweiligen Buchungsfall umstellen (ana)
			HvOptional<SteuercodeInfo> codeOpt = getMandantFac().getSteuercodeArDefault(new MwstsatzId(mwstsatzDto.getIId()));
			if (codeOpt.isEmpty()) {
					MwstsatzbezDto mwstsatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(mwstsatzDto.getIIMwstsatzbezId(), theClientDto);
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE, "", mwstsatzbezDto.getCBezeichnung());
			}
			
			try {
				Integer codeNumber = Integer.parseInt(codeOpt.get().getCode());
				return codeNumber;
			} catch (NumberFormatException ex) {
				MwstsatzbezDto mwstsatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(mwstsatzDto.getIIMwstsatzbezId(), theClientDto);
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE, 
						"FIBU Mwst-Code ist keine Zahl", mwstsatzbezDto.getCBezeichnung());
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}
}
