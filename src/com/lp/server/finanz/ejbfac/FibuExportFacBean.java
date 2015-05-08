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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.eingangsrechnung.fastlanereader.generated.FLREingangsrechnung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.finanz.bl.FibuExportFormatterAbacus;
import com.lp.server.finanz.bl.FibuExportManager;
import com.lp.server.finanz.bl.FibuExportManagerFactory;
import com.lp.server.finanz.bl.datevexport.BuchungsjournalDatevExportHeaderFormatter;
import com.lp.server.finanz.bl.datevexport.BuchungsjournalExportDatevBuchung;
import com.lp.server.finanz.bl.datevexport.BuchungsjournalExportDatevFormatter;
import com.lp.server.finanz.bl.hvraw.BuchungsjournalExportHVRawFormatter;
import com.lp.server.finanz.bl.rzlexport.BuchungsjournalExportRzlFormatter;
import com.lp.server.finanz.ejb.Exportdaten;
import com.lp.server.finanz.ejb.Exportlauf;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzExportdaten;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzExportlauf;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.ExportdatenDto;
import com.lp.server.finanz.service.ExportdatenDtoAssembler;
import com.lp.server.finanz.service.ExportlaufDto;
import com.lp.server.finanz.service.ExportlaufDtoAssembler;
import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.IBuchungsjournalExportFormatter;
import com.lp.server.finanz.service.IntrastatDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
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
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FibuExportFacBean extends Facade implements FibuExportFac {
	@PersistenceContext
	private EntityManager entityManager;

	@TransactionTimeout(20000)
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
	public String exportierePersonenkonten(String kontotypCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (kontotypCNr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_EXPORT_STICHTAG_NICHT_DEFINIERT,
					new Exception("kontotypCNr == null"));
		}
		FibuExportManager em = FibuExportManagerFactory.getFibuExportManager(
				getExportVariante(theClientDto), getExportFormat(theClientDto),
				null, theClientDto);
		return exportierePersonenkonten(em, kontotypCNr, theClientDto);
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
			FLREingangsrechnung[] er = getVerbuchbareEingangsrechnungen(
					dStichtag, theClientDto);
			// keine daten?
			if (er == null || er.length == 0) {
				return null;
			}
			// jetzt alle sperren
			sperreEingangsrechnungen(er, true, theClientDto);
			sExport.append(em.exportiereUeberschriftBelege());
			for (int i = 0; i < er.length; i++) {
				// wenn ausserhalb gueltigem Exportzeitraum und
				// diese auch zu exportieren sind und
				// diese aber nur als exportiert zu markieren sind
				// dann also nicht wirklich exportieren
				if (!(!em
						.liegtBelegdatumInnerhalbGueltigemExportZeitraum(er[i])
						&& fibuExportKriterienDto
								.isBAuchBelegeAusserhalbGueltigkeitszeitraum() && fibuExportKriterienDto
						.isBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren())) {
					// anhaengen
					sExport.append(em.exportiereEingangsrechnung(er[i].getI_id(),
							fibuExportKriterienDto.getDStichtag()));
				} else {
					myLogger.warn("ER " + er[i].getC_nr()
							+ " wurde als exportiert markiert");
				}
				// Exportprotokoll
				ExportdatenDto exportdatenDto = new ExportdatenDto();
				exportdatenDto
						.setBelegartCNr(LocaleFac.BELEGART_EINGANGSRECHNUNG);
				exportdatenDto.setExportlaufIId(exportlaufIId);
				exportdatenDto.setIBelegiid(er[i].getI_id());
				createExportdaten(exportdatenDto);
				// ER-Status setzen
				getEingangsrechnungFac().setzeEingangsrechnungFibuUebernahme(
						er[i].getI_id(), theClientDto);
			}
			// die locks aufheben
			sperreEingangsrechnungen(er, false, theClientDto);
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
			for (int i = 0; i < re.length; i++) {
				// wenn ausserhalb gueltigem Exportzeitraum und
				// diese auch zu exportieren sind und
				// diese aber nur als exportiert zu markieren sind
				// dann also nicht wirklich exportieren
				if (!(!em
						.liegtBelegdatumInnerhalbGueltigemExportZeitraum(re[i])
						&& fibuExportKriterienDto
								.isBAuchBelegeAusserhalbGueltigkeitszeitraum() && fibuExportKriterienDto
						.isBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren())) {
					// anhaengen
					sExport.append(em.exportiereRechnung(re[i].getI_id(),
							dStichtag));
				} else {
					myLogger.warn("RE " + re[i].getC_nr()
							+ " wurde als exportiert markiert");
				}
				// Exportprotokoll
				ExportdatenDto exportdatenDto = new ExportdatenDto();
				exportdatenDto.setBelegartCNr(LocaleFac.BELEGART_RECHNUNG);
				exportdatenDto.setExportlaufIId(exportlaufIId);
				exportdatenDto.setIBelegiid(re[i].getI_id());
				createExportdaten(exportdatenDto);
				// AER-Status setzen
				getRechnungFac().setzeRechnungFibuUebernahme(re[i].getI_id(),
						theClientDto);
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
	public List<String> exportiereBuchungsjournal(String format, Date von, Date bis,
			boolean mitAutoEB, boolean mitManEB, boolean mitAutoB, boolean mitStornierte, String bezeichnung,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP { 
		
		
		IBuchungsjournalExportFormatter formatter = null;
		if(DATEV.equals(format))
			formatter = getBuchungsjournalExportFormatterDatev(von, bis, mitAutoEB, mitManEB, mitAutoB, bezeichnung, theClientDto);
		else if(HV_RAW.equals(format))
			formatter = getBuchungsjournalExportFormatterHVRaw(von, bis, mitAutoEB, mitManEB, mitAutoB, mitStornierte, bezeichnung, theClientDto);
		else if(RZL_CSV.equals(format))
			formatter = getBuchungsjournalExportFormatterRzl(von, bis, mitAutoEB, mitManEB, mitAutoB, bezeichnung, theClientDto);
		
		if(formatter == null)
			return null;
		return formatter.getExportLines();
	}
	
	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterHVRaw(
			Date von, Date bis, boolean mitAutoEB, boolean mitManEB,
			boolean mitAutoB, boolean mitStornierte, String bezeichnung, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		List<Integer> mitlaufendeKonten = getIIdsMitlaufendeKonten(theClientDto);
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRFinanzBuchung.class, "b");
		c.createAlias("b.flrkostenstelle", "ks");
		if (!mitAutoB)
			c.add(Restrictions.like("b.b_autombuchung", 0));
		if (!mitAutoEB)
			c.add(Restrictions.like("b.b_autombuchungeb", 0));
		if (!mitManEB)
			c.add(Restrictions.not(Restrictions.like("b.buchungsart_c_nr",
					FinanzFac.BUCHUNGSART_EROEFFNUNG)));
		if(!mitStornierte)
			c.add(Restrictions.isNull("b.t_storniert"));
		c.add(Restrictions.ge("b.d_buchungsdatum", von))
				.add(Restrictions.le("b.d_buchungsdatum", bis))
				.add(Restrictions.like("ks.mandant_c_nr", theClientDto.getMandant()))
				.addOrder(Order.asc("b.d_buchungsdatum"))
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
		return new BuchungsjournalExportHVRawFormatter(details, mitStornierte);
	}
	
	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterDatev(
			Date von, Date bis, boolean mitAutoEB, boolean mitManEB,
			boolean mitAutoB, String bezeichnung, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ParametermandantDto pBerater = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_BERATER);
		ParametermandantDto pMandant = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MANDANT);
		ParametermandantDto pKontostellen = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN);
		String berater = pBerater.getCWert();
		String mandant = pMandant.getCWert();
		Integer kontostellen = new Integer(pKontostellen.getCWert());
		
		Integer gf = getBuchenFac().findGeschaeftsjahrFuerDatum(von, theClientDto.getMandant());
		GeschaeftsjahrMandantDto gfDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(gf, theClientDto.getMandant());
		
		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		List<BuchungsjournalExportDatevBuchung> buchungen = getBuchungen(von, bis, mitAutoEB, mitManEB, mitAutoB, theClientDto);
		BuchungsjournalDatevExportHeaderFormatter head = new BuchungsjournalDatevExportHeaderFormatter(
				theClientDto, berater, mandant, gfDto.getDBeginndatum()
						.getTime(), kontostellen, von.getTime(), bis.getTime(),
				bezeichnung, mDto.getWaehrungCNr());
		return new BuchungsjournalExportDatevFormatter(head, buchungen);
	}

	private IBuchungsjournalExportFormatter getBuchungsjournalExportFormatterRzl(
			Date von, Date bis, boolean mitAutoEB, boolean mitManEB,
			boolean mitAutoB, String bezeichnung, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ParametermandantDto pBerater = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_BERATER);
		ParametermandantDto pMandant = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MANDANT);
		ParametermandantDto pKontostellen = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_KONTONUMMER_STELLENANZAHL_SACHKONTEN);
		String berater = pBerater.getCWert();
		String mandant = pMandant.getCWert();
		Integer kontostellen = new Integer(pKontostellen.getCWert());
		
		Integer gf = getBuchenFac().findGeschaeftsjahrFuerDatum(von, theClientDto.getMandant());
		GeschaeftsjahrMandantDto gfDto = getSystemFac().geschaeftsjahrFindByPrimaryKey(gf, theClientDto.getMandant());
		
		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		List<BuchungsjournalExportDatevBuchung> buchungen = getBuchungen(von, bis, mitAutoEB, mitManEB, mitAutoB, theClientDto);
		BuchungsjournalDatevExportHeaderFormatter head = new BuchungsjournalDatevExportHeaderFormatter(
				theClientDto, berater, mandant, gfDto.getDBeginndatum()
						.getTime(), kontostellen, von.getTime(), bis.getTime(),
				bezeichnung, mDto.getWaehrungCNr());
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
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	private List<BuchungsjournalExportDatevBuchung> getBuchungen(Date von,
			Date bis, boolean mitAutoEB, boolean mitManEB, boolean mitAutoB,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		String mandant = theClientDto.getMandant();
		ParametermandantDto pMitlaufendesKonto = getParameterFac().getMandantparameter(
				mandant, ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_MITLAUFENDES_KONTO);
		String durchlaufKonto = pMitlaufendesKonto.getCWert();
		ParametermandantDto pKontoklassenOhneUst = getParameterFac().getMandantparameter(
				mandant, ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_EXPORT_DATEV_KONTOKLASSEN_OHNE_BU_SCHLUESSEL);
		List<String> kontoklassenOhneUst = Arrays.asList(pKontoklassenOhneUst.getCWert().split(","));
		
		FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(theClientDto);
		
		Map<Integer, Integer> mwstKonten = new HashMap<Integer, Integer>();
		Set<Integer> mitlaufendeKonten = new HashSet<Integer>();
		for(FinanzamtDto amt : finanzaemter) {
			mwstKonten.putAll(getFinanzServiceFac().getAllIIdsSteuerkontoMitIIdMwstBez(amt.getPartnerIId(), theClientDto));
			mitlaufendeKonten.addAll(getFinanzServiceFac().getAllMitlaufendeKonten(amt.getPartnerIId(), theClientDto));
		}
		List<BuchungsjournalExportDatevBuchung> buchungen = new ArrayList<BuchungsjournalExportDatevBuchung>();
		Session session = FLRSessionFactory.getFactory().openSession();
		Criteria c = session.createCriteria(FLRFinanzBuchung.class);
		c.createAlias("flrkostenstelle", "ks");
		if (!mitAutoB)
			c.add(Restrictions.like("b_autombuchung", Helper.getShortFalse()));
		if (!mitAutoEB)
			c.add(Restrictions.like("b_autombuchungeb", Helper.getShortFalse()));
		if (!mitManEB)
			c.add(Restrictions.not(Restrictions.like("buchungsart_c_nr",
					FinanzFac.BUCHUNGSART_EROEFFNUNG)));
		c.add(Restrictions.ge("d_buchungsdatum", von))
				.add(Restrictions.le("d_buchungsdatum", bis))
				.add(Restrictions.like("ks.mandant_c_nr", mandant))
				.addOrder(Order.asc("d_buchungsdatum"))
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
				gegenkontoCNr = haben.get(0).getFlrkonto().getC_nr();
				flrGegenkonto = soll.get(0).getFlrkonto();
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KONTIERUNG_ZUGEORDNET, "Fehler! keine Mwst-Buchung erwartet!",
							(hvBuchung.getFlrfbbelegart() == null ? hvBuchung.getC_text() : hvBuchung.getFlrfbbelegart().getC_nr()) + " " + hvBuchung.getC_belegnummer());
//					System.out.println("Fehler! keine Mwst-Buchung erwartet! " + hvBuchung.getC_belegnummer() + ", id = " + hvBuchung.getI_id());
//					break;
				} else {
					if(zuBuchen.size() > i+1) { 
						FLRFinanzBuchungDetail mwstBuchung = zuBuchen.get(i+1);
						if(mwstKonten.containsKey(mwstBuchung.getKonto_i_id())) {
							Integer mwstIId = mwstKonten.get(mwstBuchung.getKonto_i_id());
							MwstsatzDto mwstDto;
							if(mwstIId != null) {
								mwstDto = getMandantFac().mwstsatzFindZuDatum(mwstIId, new Timestamp(hvBuchung.getD_buchungsdatum().getTime()));
							} else {
								mwstDto = getMandantFac().getMwstSatzVonBruttoBetragUndUst(mandant, new Timestamp(hvBuchung.getD_buchungsdatum().getTime()),
									b.getN_betrag(), mwstBuchung.getN_betrag());								
							}
							fibuCode = mwstDto.getIFibumwstcode();
							if(fibuCode == null) {
								MwstsatzbezDto mwstsatzbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(mwstDto.getIIMwstsatzbezId(), theClientDto);
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_MWSTCODE, "", mwstsatzbezDto.getCBezeichnung());
							}
							umsatz = b.getN_betrag().add(mwstBuchung.getN_betrag()).abs();
							i++;
						}
					}
				}
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
				buchungen.add(datevBuchung);
			}
		}
		
		return buchungen;
	}
	
	private List<FLRFinanzBuchungDetail> getBuchungenKorrekteUst(List<FLRFinanzBuchungDetail> buchungen, Map<Integer, Integer> mwstKonten) {
		List<FLRFinanzBuchungDetail> buchungenNeu = new ArrayList<FLRFinanzBuchungDetail>();
		
		for(int i = 0; i < buchungen.size(); i++) {
			
		}
		return buchungenNeu;
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
			for (int i = 0; i < gs.length; i++) {
				// wenn ausserhalb gueltigem Exportzeitraum und
				// diese auch zu exportieren sind und
				// diese aber nur als exportiert zu markieren sind
				// dann also nicht wirklich exportieren
				if (!(!em
						.liegtBelegdatumInnerhalbGueltigemExportZeitraum(gs[i])
						&& fibuExportKriterienDto
								.isBAuchBelegeAusserhalbGueltigkeitszeitraum() && fibuExportKriterienDto
						.isBBelegeAusserhalbGueltigkeitszeitraumAlsExportiertMarkieren())) {
					// anhaengen
					sExport.append(em.exportiereGutschrift(gs[i].getI_id(),
							dStichtag));
				} else {
					myLogger.warn("GS " + gs[i].getC_nr()
							+ " wurde als exportiert markiert");
				}
				// Exportprotokoll
				ExportdatenDto exportdatenDto = new ExportdatenDto();
				exportdatenDto.setBelegartCNr(LocaleFac.BELEGART_GUTSCHRIFT);
				exportdatenDto.setExportlaufIId(exportlaufIId);
				exportdatenDto.setIBelegiid(gs[i].getI_id());
				createExportdaten(exportdatenDto);
				// GS-Status setzen
				getRechnungFac().setzeRechnungFibuUebernahme(gs[i].getI_id(),
						theClientDto);
			}
			// die locks aufheben
			sperreGutschriften(gs, false, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return sExport.toString();
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
		// try {
		Exportdaten exportdaten = entityManager.find(Exportdaten.class, iId);
		if (exportdaten == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleExportdatenDto(exportdaten);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
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
		// try {
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
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
		// try {
		Exportlauf exportlauf = entityManager.find(Exportlauf.class, iId);
		if (exportlauf == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleExportlaufDto(exportlauf);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
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

	private String exportierePersonenkonten(FibuExportManager em,
			String kontotypCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuffer sExport = new StringBuffer();
		FibuKontoExportDto[] konten = getZuExportierendeKonten(kontotypCNr,
				theClientDto);
		if (konten == null || konten.length == 0) {
			return null;
		}
		// Ueberschriften werden exportiert, wenn Mandantenparameter
		// PARAMETER_FINANZ_EXPORT_UEBERSCHRIFT gesetzt ist.
		sExport.append(em.exportiereUeberschriftPersonenkonten(theClientDto));
		sExport.append(em.exportierePersonenkonten(konten, theClientDto));
		return sExport.toString();
	}

	private FibuKontoExportDto[] getZuExportierendeKonten(String kontotypCNr,
			TheClientDto theClientDto) {
		FibuKontoExportDto[] konten;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzKonto.class);
			// Filter nach Kontotyp
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_KONTOTYP_C_NR,
					kontotypCNr));
			// Filter nach Mandant
			c.add(Restrictions.eq(FinanzFac.FLR_KONTO_MANDANT_C_NR,
					theClientDto.getMandant()));
			// Sortierung aufsteigend nach Kontonummer
			c.addOrder(Order.asc(FinanzFac.FLR_KONTO_C_NR));
			List<?> list = c.list();
			// Array
			konten = new FibuKontoExportDto[list.size()];
			for (int i = 0; i < konten.length; i++) {
				konten[i] = new FibuKontoExportDto();
			}
			int i = 0;
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzKonto konto = (FLRFinanzKonto) iter.next();
				konten[i].setKontoDto(getFinanzFac().kontoFindByPrimaryKey(
						konto.getI_id()));
				// Fuer ein Debitorenkonto schaun, ob es einen Kunden gibt
				if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_DEBITOR)) {
					Criteria cKunde = session.createCriteria(FLRKunde.class);
					cKunde.add(Restrictions.eq(
							KundeFac.FLR_KUNDE_KONTO_I_ID_DEBITORENKONTO, konto
									.getI_id()));
					List<?> listKunde = cKunde.list();
					if (!listKunde.isEmpty()) {
						FLRKunde kunde = (FLRKunde) listKunde.get(0);
						konten[i].setPartnerDto(getKundeFac()
								.kundeFindByPrimaryKeyOhneExc(kunde.getI_id(),
										theClientDto).getPartnerDto());
					}
				}
				// Fuer ein Kreditorenkonto schaun, ob es einen Lieferanten gibt
				else if (kontotypCNr.equals(FinanzServiceFac.KONTOTYP_KREDITOR)) {
					Criteria cLieferant = session
							.createCriteria(FLRLieferant.class);
					cLieferant.add(Restrictions.eq(
							LieferantFac.FLR_KONTO_I_ID_KREDITORENKONTO, konto
									.getI_id()));
					List<?> listLieferant = cLieferant.list();
					if (!listLieferant.isEmpty()) {
						FLRLieferant lieferant = (FLRLieferant) listLieferant
								.get(0);
						konten[i].setPartnerDto(getLieferantFac()
								.lieferantFindByPrimaryKey(lieferant.getI_id(),
										theClientDto).getPartnerDto());
					}
				}
				i++;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return konten;
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

	
	
	@TransactionTimeout(20000)
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
			// pruefen, ob alle Artikel eine gueltige Warenverkehrsnummer haben
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
		Collections.sort(list, new ComparatorIntrastat());
		// hier kommen die verdichteten daten rein.
		ArrayList<IntrastatDto> aVerdichtet = new ArrayList<IntrastatDto>();
		IntrastatDto iDtoLetztesZiel = null;
		for (IntrastatDto iDto : list) {
			iDto.setBeistell("1");
			if (iDtoLetztesZiel == null) {
				iDtoLetztesZiel = iDto;
				aVerdichtet.add(iDtoLetztesZiel);
			} else {
				if (new ComparatorIntrastat().compare(iDtoLetztesZiel, iDto) == 0) {
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
	@TransactionTimeout(20000)
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
			cPartner.add(Restrictions.isNotNull(PartnerFac.FLR_PARTNER_C_UID));
			
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

			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRWareneingangspositionen wePos = (FLRWareneingangspositionen) iter
						.next();
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac()
						.artikelFindByPrimaryKey(
								wePos.getFlrbestellposition().getFlrartikel()
										.getI_id(), theClientDto));
				iDto.setBelegart("BS");
				iDto.setBelegnummer(wePos.getFlrbestellposition()
						.getFlrbestellung().getC_nr());
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
				iDto.setEinzelpreis(bdEinstandspreis);
				iDto.setMenge(wePos.getN_geliefertemenge() != null ? wePos
						.getN_geliefertemenge() : new BigDecimal(0));
				// Wert = Menge * Preis
				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
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
				daten.add(iDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
		return daten;
	}
	@TransactionTimeout(20000)
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
			cLSPartner.add(Restrictions.isNotNull(PartnerFac.FLR_PARTNER_C_UID));
			
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

			List<?> listLSPos = cLS.list();
			for (Iterator<?> iter = listLSPos.iterator(); iter.hasNext();) {
				FLRLieferscheinposition lsPos = (FLRLieferscheinposition) iter
						.next();
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
						lsPos.getFlrartikel().getI_id(), theClientDto));
				iDto.setBelegart("LS");
				iDto.setBelegnummer(lsPos.getFlrlieferschein().getC_nr());
				/**
				 * @todo MB ist das der richtige Preis?
				 * @todo MB Wechselkurs beruecksichtigen
				 */
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
				/**
				 * @todo das mit den Transportkosten noch besser loesen
				 */
				BigDecimal bdGesamtwertDesWareneingangs = new BigDecimal(0);
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

				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
				/*
				 * iDto .setStatistischerWert(iDto.getWert().add(
				 * bdTransportkosten));
				 */
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
				daten.add(iDto);
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
				// Die Rechnung muss aktiviert sein
				if (rePos.getFlrrechnung().getStatus_c_nr().equals(
						RechnungFac.STATUS_ANGELEGT)) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_EXPORT_BELEG_IST_NOCH_NICHT_AKTIVIERT,
							new Exception("Rechnung "
									+ rePos.getFlrrechnung().getC_nr()));
				}
				IntrastatDto iDto = new IntrastatDto();
				iDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
						rePos.getFlrartikel().getI_id(), theClientDto));
				iDto.setBelegart("RE");
				iDto.setBelegnummer(rePos.getFlrrechnung().getC_nr());
				/**
				 * @todo MB ist das der richtige Preis?
				 * @todo MB Wechselkurs beruecksichtigen
				 */
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iDto.setWert(iDto.getMenge().multiply(iDto.getEinzelpreis()));
				/*
				 * iDto .setStatistischerWert(iDto.getWert().add(
				 * bdTransportkosten));
				 */
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
				daten.add(iDto);
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
		
		public OpBelegnummer(String prefixAR, String prefixGS, int belegnrLaenge, int jahrLaenge, String belegTrennzeichen) {
			this.prefixAR = prefixAR ;
			this.prefixGS = prefixGS ;
			this.belegnrLaenge = belegnrLaenge ;
			this.jahrLaenge = jahrLaenge ;
			this.belegTrennzeichen = belegTrennzeichen ;
		}
		
		public boolean isValid(String anyNumber) {
			String candidate = anyNumber ;
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR)) {
				candidate = anyNumber.substring(prefixAR.length()) ;
			}			
			
			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS)) {
				candidate = anyNumber.substring(prefixGS.length()) ;
			}
			
			if(candidate.length() != jahrLaenge + belegTrennzeichen.length() + belegnrLaenge) {
				return false ;
			}

			return candidate.substring(
				jahrLaenge, jahrLaenge + belegTrennzeichen.length()).equals(belegTrennzeichen) ;
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
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR) && isValid(anyNumber)) {
				return buildKeyedBelegnummer("Rechnung", anyNumber.substring(prefixAR.length())) ;
			}

			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS) && isValid(anyNumber)) {
				return buildKeyedBelegnummer("Gutschrift", anyNumber.substring(prefixGS.length())) ;
			}
			
			return anyNumber ;
		}
		
		public String getExternalBelegnummer(String anyNumber) {
			if(prefixAR.length() > 0 && anyNumber.startsWith(prefixAR) && isValid(anyNumber)) {
				return anyNumber.substring(prefixAR.length()) ;
			}

			if(prefixGS.length() > 0 && anyNumber.startsWith(prefixGS) && isValid(anyNumber)) {
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
	@TransactionTimeout(10000)
	public String importiereOffenePosten(ArrayList<String[]> daten,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		final int MAX_ERROR = 30;
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

		Integer belegNrlaenge = new Integer(7);
		Integer belegNrlaengeJahr = new Integer(2);
		String belegNrTrennzeichen = "/";
		
		try {
			p = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_BELEGNUMMER);
			belegNrlaenge = new Integer(p.getCWert());
		} catch (Exception e) {
			//
		}
		try {
			p = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_STELLEN_GESCHAEFTSJAHR);
			belegNrlaengeJahr = new Integer(p.getCWert());
		} catch (Exception e) {
			//
		}
		try {
			p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN,
					ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);
			belegNrTrennzeichen = p.getCWert();
		} catch (Exception e) {
			//
		}

		OpBelegnummer bn = new OpBelegnummer(opPrefixAR, opPrefixGS, belegNrlaenge, belegNrlaengeJahr, belegNrTrennzeichen) ;
		
		String mandantCNr = theClientDto.getMandant();
		MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(
				theClientDto.getMandant(), theClientDto);
		Integer kontoIId = mDto.getIBankverbindung();
		HashMap<String, String> hmNummern = new HashMap<String, String>();
		for (int i = 1; i < daten.size(); i++) { // bei 1 beginnen, da erste
			// Zeile Header
			String[] as = daten.get(i);
			if (as.length < CSV_LEN) {
				bn.error(i, "Falsche Feldanzahl:" + as.length + " erwartet:" + CSV_LEN ) ;
			} else {
				if(bn.isValid(as[FI_RECHNUNGSNR])) {
					String rechnungsNummer = bn.getKeyedBelegnummer(as[FI_RECHNUNGSNR]) ;					
					String found = hmNummern.get(rechnungsNummer) ;
					if(found != null) {
//						bn.warn(as[FI_RECHNUNGSNR] + "(Raffung?" + i + ")");
						continue ;
					}
					
					hmNummern.put(rechnungsNummer, as[FI_RECHNUNGSNR]) ;
					String sErr = importARGS(bn.isAR(as[FI_RECHNUNGSNR]), bn.getExternalBelegnummer(as[FI_RECHNUNGSNR]),
							as[FI_SALDO], as[FI_SH_SALDO], "", IMPORT_TEXT,
							kontoIId, mandantCNr, theClientDto);
					if (sErr.length() > 0) {
						bn.error(i,  sErr);
					}
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
			String sQuery = "SELECT r.c_nr, r.flrrechnungart.c_nr "
					+ "FROM FLRRechnung AS r "
					+ "WHERE r.t_fibuuebernahme != NULL AND "
					+ "(r.status_c_nr = 'Offen' OR r.status_c_nr = 'Teilbezahlt') AND "
					+ "r.mandant_c_nr='"
					+ mandantCNr
					+ "' AND "
//					+ "(r.flrrechnungart.c_nr IN ('Rechnung', 'Anzahlungsrechnung'))";
					+ "(r.flrrechnungart.c_nr IN ('Rechnung', 'Anzahlungsrechnung', 'Gutschrift'))";
			org.hibernate.Query query = session.createQuery(sQuery);
			List<Object[]> baseList = new ArrayList<Object[]>() ;
//			baseList.add(new Object[]{"12/0000788", "Rechnung"}) ;
//			baseList.add(new Object[]{"12/0000809", "Rechnung"});
			baseList.addAll(query.list()) ;
			Iterator<?> it = baseList.iterator() ;
			
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next() ;
				String cNr = (String) o[0] ;
				String artCnr = (String) o[1] ;
	
//				try {
					String externalBelegNr = hmNummern.get(bn.buildKeyedBelegnummer(artCnr, cNr)) ;
					if (externalBelegNr == null) {
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

	private String importARGS(boolean bARGS, String sBelegnummer,
			String sSaldo, String sSHSaldo, String sKurs, Integer sBuchungsText,
			Integer kontoIId, String sMandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Query query;
		if (bARGS) {
			// AR
			query = entityManager
					.createNamedQuery("RechnungfindByRechnungartRechnungCNrMandant");
			query.setParameter(1, sBelegnummer);
			query.setParameter(2, sMandantCNr);
		} else {
			// GS
			query = entityManager
					.createNamedQuery("RechnungfindByCNrRechnungartCNrMandant");
			query.setParameter(1, sBelegnummer);
			query.setParameter(2, RechnungFac.RECHNUNGART_GUTSCHRIFT);
			query.setParameter(3, sMandantCNr);
		}
		Rechnung rechnung = null;

		try {
			rechnung = (Rechnung) query.getSingleResult();
		} catch (NoResultException e) {
			return (bARGS ? "Rechnung " : "Gutschrift ") + sBelegnummer + " nicht vorhanden";
		}

		if (rechnung.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
			return "Rechnung " + sBelegnummer + " wurde storniert";
		}
		if (rechnung.getNWert().signum() == 0) {
			return "Rechnung " + sBelegnummer + " hat Wert = 0";
		}
		BigDecimal bezahlt = getRechnungFac().getBereitsBezahltWertVonRechnung(
				rechnung.getIId(), null);
		BigDecimal saldo = new BigDecimal(sSaldo);
		BigDecimal saldodiff = (rechnung.getNWert().add(rechnung.getNWertust())
				.subtract(bezahlt)).subtract(saldo);
		// gemischter Steuersatz der Rechnung
		BigDecimal ustproz = rechnung.getNWertust().divide(rechnung.getNWert(),
				BigDecimal.ROUND_HALF_EVEN).movePointRight(2);
		if (saldodiff.signum() < 0) {
			return "Ung\u00FCltiger offener Saldo f\u00FCr " + sBelegnummer + 
					". Negativer Betrag. [" + saldodiff.toPlainString() + 
					", bezahlt=" + bezahlt.toPlainString() + 
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
//			if (bARGS)
//				rzDto.setRechnungIId(rechnung.getIId());
//			else
//				rzDto.setRechnungIIdGutschrift(rechnung.getIId());
//			// TODO: Zahlungsart
//			rzDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);

			if (bARGS) {
				rzDto.setRechnungIId(rechnung.getIId());
				rzDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_BANK);
			} else {
//				rzDto.setRechnungIIdGutschrift(rechnung.getIId());
				rzDto.setRechnungIId(rechnung.getIId());
				rzDto.setZahlungsartCNr(RechnungFac.ZAHLUNGSART_GUTSCHRIFT);
			}

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

}
