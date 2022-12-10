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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.service.NachrichtenFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.ejb.MailProperty;
import com.lp.server.system.ejb.MailPropertyQuery;
import com.lp.server.system.ejb.Versandanhang;
import com.lp.server.system.ejb.Versandauftrag;
import com.lp.server.system.ejb.Versandstatus;
import com.lp.server.system.fastlanereader.generated.FLRVersandauftrag;
import com.lp.server.system.jcr.service.FehlerVersandauftraegeDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.mail.service.LPMail;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailProcessorFac;
import com.lp.server.system.mail.service.MailTestMessage;
import com.lp.server.system.mail.service.MailTestMessageResult;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailServiceParameterSource;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandanhangDtoAssembler;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.system.service.VersandauftragDtoAssembler;
import com.lp.server.system.service.VersandstatusDto;
import com.lp.server.system.service.VersandstatusDtoAssembler;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.LpHtmlMailText;
import com.lp.server.util.report.LpMailText;
import com.lp.server.util.report.LpMailTextMitHtmlSignatur;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.PersonRpt;

@Stateless
//@Interceptors(MethodLogger.class)
public class VersandFacBean extends Facade implements VersandFac {
	@PersistenceContext
	private EntityManager em;

	@Resource(name = "Mail", mappedName = "java:/Mail")
	javax.mail.Session mailSession;

	// private TheClientDto theClientDto;
	// private String lastMandantCnr;
	// private final static String TIMER_TYP_SMTP = "SMTP";
	// private final static String TIMER_TYP_IMAP = "IMAP";

	public VersandauftragDto createVersandauftrag(VersandauftragDto versandauftragDto, boolean bDokumenteMitanhaengen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return createVersandauftrag(versandauftragDto, new ArrayList<VersandanhangDto>(), bDokumenteMitanhaengen,
				theClientDto);
	}

	public VersandauftragDto createVersandauftrag0(VersandauftragDto versandauftragDto, boolean bDokumenteMitanhaengen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragDto, theClientDto.getIDUser());
		// begin
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_VERSANDAUFTRAG);
		versandauftragDto.setIId(iId);
		// wenn kein sendezeitpunkt angegeben, dann jetzt
		try {
			if (versandauftragDto.getTSendezeitpunktwunsch() == null) {
				GregorianCalendar gc = new GregorianCalendar();
				ParametermandantDto pm = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				int iDelay = Integer.parseInt(pm.getCWert());
				gc.set(Calendar.MINUTE, gc.get(Calendar.MINUTE) + iDelay);
				versandauftragDto.setTSendezeitpunktwunsch(new Timestamp(gc.getTimeInMillis()));
			}
			// Default keine Empfangsbestaetigung
			if (versandauftragDto.getBEmpfangsbestaetigung() == null) {
				versandauftragDto.setBEmpfangsbestaetigung(Helper.boolean2Short(false));
			}
			// ein neuer ist immer auf angelegt
			versandauftragDto.setPersonalIId(theClientDto.getIDPersonal());
			Versandauftrag versandauftrag = new Versandauftrag(versandauftragDto.getIId(),
					versandauftragDto.getCEmpfaenger(), versandauftragDto.getTSendezeitpunktwunsch(),
					versandauftragDto.getPersonalIId(), versandauftragDto.getPartnerIIdEmpfaenger(),
					versandauftragDto.getPartnerIIdSender(), versandauftragDto.getOInhalt(),
					versandauftragDto.getBEmpfangsbestaetigung());

			versandauftragDto.setTAnlegen(versandauftrag.getTAnlegen());
			setVersandauftragFromVersandauftragDto(versandauftrag, versandauftragDto);
			em.persist(versandauftrag);
			em.flush();

			// Anlegen des Versandauftrags in JCR
//			getJCRDocFac().saveVersandauftragAsDocument(versandauftragDto, false, theClientDto);

			// PJ14774

			if (bDokumenteMitanhaengen == true && versandauftragDto.getBelegartCNr() != null
					&& versandauftragDto.getIIdBeleg() != null) {

				if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
					LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
							.getLieferscheinPositionenByLieferschein(versandauftragDto.getIIdBeleg(), theClientDto);

					for (int i = 0; i < lsPos.length; i++) {

						if (lsPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

							ArrayList<WarenzugangsreferenzDto> dtos = getLagerFac().getWareneingangsreferenz(
									LocaleFac.BELEGART_LIEFERSCHEIN, lsPos[i].getIId(),
									lsPos[i].getSeriennrChargennrMitMenge(), true, theClientDto);

							for (int z = 0; z < dtos.size(); z++) {
								if (dtos.get(z).getJcrdocs() != null) {
									ArrayList<JCRDocDto> anhaenge = dtos.get(z).getJcrdocs();

									// lt Hr. Brennecke immer nur den letzten
									// verwenden
									if (anhaenge.size() > 0) {
										JCRDocDto jcrDoc = anhaenge.get(0);
										VersandanhangDto versandanhangDto = new VersandanhangDto();
										versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
										versandanhangDto.setCDateiname(jcrDoc.getsFilename());

										versandanhangDto.setOInhalt(jcrDoc.getbData());
										createVersandanhangImpl(versandanhangDto, theClientDto);
									}
								}
							}
						}
					}
				}
			}

			// PJ 17044 Artikel-Anhaenge

			HashMap<Integer, ArtikelkommentarDto[]> hmArtikelAnhaenge = new HashMap<Integer, ArtikelkommentarDto[]>();

			if (versandauftragDto.getBelegartCNr() != null) {
				if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(versandauftragDto.getIIdBeleg(), theClientDto);

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);

					LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
							.getLieferscheinPositionenByLieferschein(versandauftragDto.getIIdBeleg(), theClientDto);

					for (int i = 0; i < lsPos.length; i++) {

						if (lsPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

							if (!hmArtikelAnhaenge.containsKey(lsPos[i].getArtikelIId())) {
								hmArtikelAnhaenge.put(lsPos[i].getArtikelIId(), getArtikelkommentarFac()
										.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
												lsPos[i].getArtikelIId(), LocaleFac.BELEGART_LIEFERSCHEIN,
												kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
							}

						}
					}

				} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)) {
					AngebotDto belegDto = getAngebotFac().angebotFindByPrimaryKey(versandauftragDto.getIIdBeleg(),
							theClientDto);

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIIdAngebotsadresse(),
							theClientDto);

					AngebotpositionDto[] belegposPos = getAngebotpositionFac()
							.angebotpositionFindByAngebotIId(belegDto.getIId(), theClientDto);

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
								hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(), getArtikelkommentarFac()
										.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
												belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_ANGEBOT,
												kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto belegDto = getAuftragFac().auftragFindByPrimaryKey(versandauftragDto.getIIdBeleg());

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIIdAuftragsadresse(),
							theClientDto);

					AuftragpositionDto[] belegposPos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(belegDto.getIId());

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
								hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(), getArtikelkommentarFac()
										.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
												belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_AUFTRAG,
												kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
						|| versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
					RechnungDto belegDto = getRechnungFac().rechnungFindByPrimaryKey(versandauftragDto.getIIdBeleg());

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIId(), theClientDto);

					RechnungPositionDto[] belegposPos = getRechnungFac()
							.rechnungPositionFindByRechnungIId(belegDto.getIId());

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
								hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(), getArtikelkommentarFac()
										.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
												belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_RECHNUNG,
												kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANFRAGE)) {
					AnfrageDto belegDto = getAnfrageFac().anfrageFindByPrimaryKey(versandauftragDto.getIIdBeleg(),
							theClientDto);

					if (belegDto.getLieferantIIdAnfrageadresse() != null) {
						LieferantDto lDto = getLieferantFac()
								.lieferantFindByPrimaryKey(belegDto.getLieferantIIdAnfrageadresse(), theClientDto);

						AnfragepositionDto[] belegposPos = getAnfragepositionFac()
								.anfragepositionFindByAnfrage(belegDto.getIId(), theClientDto);

						for (int i = 0; i < belegposPos.length; i++) {

							if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
								if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
									hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(), getArtikelkommentarFac()
											.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
													belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_ANFRAGE,
													lDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
								}
							}

						}
					}
				} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
					BestellungDto belegDto = getBestellungFac()
							.bestellungFindByPrimaryKey(versandauftragDto.getIIdBeleg());

					LieferantDto lDto = getLieferantFac()
							.lieferantFindByPrimaryKey(belegDto.getLieferantIIdBestelladresse(), theClientDto);

					BestellpositionDto[] belegposPos = getBestellpositionFac()
							.bestellpositionFindByBestellung(belegDto.getIId(), theClientDto);

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
								hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(), getArtikelkommentarFac()
										.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
												belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_BESTELLUNG,
												lDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
							}
						}

					}
				}

				Iterator<Integer> it = hmArtikelAnhaenge.keySet().iterator();
				while (it.hasNext()) {
					Integer artikelId = it.next();
					ArtikelkommentarDto[] anhangeArtikel = hmArtikelAnhaenge.get(artikelId);

//					List<VersandanhangDto> versandAnhangs = new ArrayList<VersandanhangDto>();
					for (int i = 0; i < anhangeArtikel.length; i++) {

						String dateiname = anhangeArtikel[i].getArtikelkommentarsprDto().getCDateiname();

						if (dateiname == null && anhangeArtikel[i].getDatenformatCNr()
								.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
							dateiname = i + ".pdf";
						}

						if (anhangeArtikel[i].getArtikelkommentarsprDto() != null && dateiname != null) {
							VersandanhangDto versandanhangDto = new VersandanhangDto();
							versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
							versandanhangDto.setCDateiname(dateiname);

							versandanhangDto.setOInhalt(anhangeArtikel[i].getArtikelkommentarsprDto().getOMedia());
							createVersandanhangImpl(versandanhangDto, theClientDto);
//							versandAnhangs.add(versandanhangDto);
						}
					}

//					createVersandanhaenge(versandAnhangs, theClientDto);
				}
			}

			// Anlegen des Versandauftrags in JCR
			getJCRDocFac().saveVersandauftragAsDocument(versandauftragDto, false, theClientDto);
			doSendMailOrFax(versandauftragDto, theClientDto);
			return versandauftragDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public FehlerVersandauftraegeDto getOffeneUndFehlgeschlageneAuftraege(TheClientDto theClientDto) {

		FehlerVersandauftraegeDto dto = new FehlerVersandauftraegeDto();

		// 5 Minuten spaeter
		Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis() - (5 * 60 * 1000));

		// Offene gesamt
		Query query = em.createQuery(
				"SELECT count(o) FROM Versandauftrag o WHERE o.statusCNr IS NULL AND o.cJobid IS NULL AND o.tSendezeitpunktwunsch<'"
						+ Helper.formatTimestampWithSlashes(ts) + "'");
		try {

			Long lAnzahl = (Long) query.getSingleResult();
			if (lAnzahl != null) {
				dto.setIOffenGesamt(lAnzahl);
			}
		} catch (Exception e) {
			myLogger.warn("sql", e);
		}

		Long lAnzahl;
		query = em.createQuery(
				"SELECT count(o) FROM Versandauftrag o WHERE o.statusCNr IS NULL AND o.cJobid IS NULL AND o.tSendezeitpunktwunsch<'"
						+ Helper.formatTimestampWithSlashes(ts) + "' AND o.personalIId="
						+ theClientDto.getIDPersonal());
		lAnzahl = (Long) query.getSingleResult();
		if (lAnzahl != null) {
			dto.setIOffenMeine(lAnzahl);
		}

		query = em.createQuery(
				"SELECT count(o) FROM Versandauftrag o WHERE o.statusCNr='" + VersandFac.STATUS_FEHLGESCHLAGEN + "'");
		lAnzahl = (Long) query.getSingleResult();
		if (lAnzahl != null) {
			dto.setIFehlgeschlagenGesamt(lAnzahl);
		}
		query = em.createQuery("SELECT count(o) FROM Versandauftrag o WHERE o.statusCNr='"
				+ VersandFac.STATUS_FEHLGESCHLAGEN + "' AND o.personalIId=" + theClientDto.getIDPersonal());
		lAnzahl = (Long) query.getSingleResult();
		if (lAnzahl != null) {
			dto.setIFehlgeschlagenMeine(lAnzahl);
		}

		return dto;
	}

	@Override
	public List<VersandauftragDto> createVersandauftrags(List<VersandauftragDto> versandauftragDtos,
			boolean dokumenteAnhaengen, TheClientDto theClientDto) {

		List<VersandauftragDto> result = new ArrayList<VersandauftragDto>();
		for (VersandauftragDto versandauftragDto : versandauftragDtos) {
			result.add(createVersandauftragWithoutJcr(versandauftragDto, new ArrayList<VersandanhangDto>(),
					dokumenteAnhaengen, theClientDto));
		}

		try {
			getJCRDocFac().saveVersandauftragsAsDocument(result, false, theClientDto);
		} catch (RemoteException e) {
			throw EJBExcFactory.respectOld(e);
		}
		return result;
	}

	private VersandauftragDto createVersandauftragWithoutJcr(VersandauftragDto versandauftragDto,
			List<VersandanhangDto> anhaenge, boolean dokumenteAnhaengen, TheClientDto theClientDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_VERSANDAUFTRAG);
		versandauftragDto.setIId(iId);

		try {
			if (versandauftragDto.getTSendezeitpunktwunsch() == null) {
				GregorianCalendar gc = new GregorianCalendar();
				ParametermandantDto pm = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				int iDelay = Integer.parseInt(pm.getCWert());
				gc.set(Calendar.MINUTE, gc.get(Calendar.MINUTE) + iDelay);
				versandauftragDto.setTSendezeitpunktwunsch(new Timestamp(gc.getTimeInMillis()));
			}

			if (versandauftragDto.getBEmpfangsbestaetigung() == null) {
				versandauftragDto.setBEmpfangsbestaetigung(Helper.getShortFalse());
			}

			versandauftragDto.setPersonalIId(theClientDto.getIDPersonal());
			Versandauftrag versandauftrag = new Versandauftrag(versandauftragDto.getIId(),
					versandauftragDto.getCEmpfaenger(), versandauftragDto.getTSendezeitpunktwunsch(),
					versandauftragDto.getPersonalIId(), versandauftragDto.getPartnerIIdEmpfaenger(),
					versandauftragDto.getPartnerIIdSender(), versandauftragDto.getOInhalt(),
					versandauftragDto.getBEmpfangsbestaetigung());

			versandauftragDto.setTAnlegen(versandauftrag.getTAnlegen());
			setVersandauftragFromVersandauftragDto(versandauftrag, versandauftragDto);
			em.persist(versandauftrag);
			em.flush();

			if (dokumenteAnhaengen) {
				createVersandanhaengeBelegspezifisch(versandauftragDto, theClientDto);
			}

			createVersandanhaengArtikelkommentarUndPartnerKommentar(versandauftragDto, theClientDto);

			if (anhaenge != null) {
				for (VersandanhangDto versandanhangDto : anhaenge) {
					versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
					createVersandanhangImpl(versandanhangDto, theClientDto);
				}
			}

			doSendMailOrFax(versandauftragDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

		return versandauftragDto;
	}

	@Override
	public VersandauftragDto createVersandauftrag(VersandauftragDto versandauftragDto, List<VersandanhangDto> anhaenge,
			boolean dokumenteAnhaengen, TheClientDto theClientDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_VERSANDAUFTRAG);
		versandauftragDto.setIId(iId);

		try {
			if (versandauftragDto.getTSendezeitpunktwunsch() == null) {
				GregorianCalendar gc = new GregorianCalendar();
				ParametermandantDto pm = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				int iDelay = Integer.parseInt(pm.getCWert());
				gc.set(Calendar.MINUTE, gc.get(Calendar.MINUTE) + iDelay);
				versandauftragDto.setTSendezeitpunktwunsch(new Timestamp(gc.getTimeInMillis()));
			}

			if (versandauftragDto.getBEmpfangsbestaetigung() == null) {
				versandauftragDto.setBEmpfangsbestaetigung(Helper.getShortFalse());
			}

			versandauftragDto.setPersonalIId(theClientDto.getIDPersonal());
			Versandauftrag versandauftrag = new Versandauftrag(versandauftragDto.getIId(),
					versandauftragDto.getCEmpfaenger(), versandauftragDto.getTSendezeitpunktwunsch(),
					versandauftragDto.getPersonalIId(), versandauftragDto.getPartnerIIdEmpfaenger(),
					versandauftragDto.getPartnerIIdSender(), versandauftragDto.getOInhalt(),
					versandauftragDto.getBEmpfangsbestaetigung());

			versandauftragDto.setTAnlegen(versandauftrag.getTAnlegen());
			setVersandauftragFromVersandauftragDto(versandauftrag, versandauftragDto);
			em.persist(versandauftrag);
			em.flush();

			if (dokumenteAnhaengen) {
				createVersandanhaengeBelegspezifisch(versandauftragDto, theClientDto);
			}

			createVersandanhaengArtikelkommentarUndPartnerKommentar(versandauftragDto, theClientDto);

			if (anhaenge != null) {
				for (VersandanhangDto versandanhangDto : anhaenge) {
					versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
					createVersandanhangImpl(versandanhangDto, theClientDto);
				}
			}

			// Anlegen des Versandauftrags in JCR
			getJCRDocFac().saveVersandauftragAsDocument(versandauftragDto, false, theClientDto);
			doSendMailOrFax(versandauftragDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

		return versandauftragDto;
	}

	private void createVersandanhaengeBelegspezifisch(VersandauftragDto versandauftragDto, TheClientDto theClientDto)
			throws RemoteException {
		if (versandauftragDto.getBelegartCNr() == null)
			return;
		if (versandauftragDto.getIIdBeleg() == null)
			return;

		if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
			LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
					.getLieferscheinPositionenByLieferschein(versandauftragDto.getIIdBeleg(), theClientDto);

			for (int i = 0; i < lsPos.length; i++) {

				if (lsPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

					ArrayList<WarenzugangsreferenzDto> dtos = getLagerFac().getWareneingangsreferenz(
							LocaleFac.BELEGART_LIEFERSCHEIN, lsPos[i].getIId(), lsPos[i].getSeriennrChargennrMitMenge(),
							true, theClientDto);

					for (int z = 0; z < dtos.size(); z++) {
						if (dtos.get(z).getJcrdocs() != null) {
							ArrayList<JCRDocDto> anhaenge = dtos.get(z).getJcrdocs();

							// lt Hr. Brennecke immer nur den letzten
							// verwenden
							if (anhaenge.size() > 0) {
								JCRDocDto jcrDoc = anhaenge.get(0);
								VersandanhangDto versandanhangDto = new VersandanhangDto();
								versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
								versandanhangDto.setCDateiname(jcrDoc.getsFilename());

								versandanhangDto.setOInhalt(jcrDoc.getbData());
								createVersandanhangImpl(versandanhangDto, theClientDto);
							}
						}
					}
				}
			}
		}
	}

	private void createVersandanhaengArtikelkommentarUndPartnerKommentar(VersandauftragDto versandauftragDto,
			TheClientDto theClientDto) throws RemoteException {
		if (versandauftragDto.getBelegartCNr() == null)
			return;

		// PJ 17044 Artikel-Anhaenge

		Map<Integer, ArtikelkommentarDto[]> hmArtikelAnhaenge = new HashMap<Integer, ArtikelkommentarDto[]>();

		ArrayList<PartnerkommentarDto> alPartnerAnhaenge = new ArrayList<PartnerkommentarDto>();

		if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
			LieferscheinDto lsDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(versandauftragDto.getIIdBeleg(),
					theClientDto);

			KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(lsDto.getKundeIIdLieferadresse(), theClientDto);

			alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(kDto.getPartnerIId(), true,
					versandauftragDto.getBelegartCNr(), theClientDto);

			LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
					.getLieferscheinPositionenByLieferschein(versandauftragDto.getIIdBeleg(), theClientDto);

			for (int i = 0; i < lsPos.length; i++) {

				if (lsPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

					if (!hmArtikelAnhaenge.containsKey(lsPos[i].getArtikelIId())) {
						hmArtikelAnhaenge.put(lsPos[i].getArtikelIId(),
								getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
										lsPos[i].getArtikelIId(), LocaleFac.BELEGART_LIEFERSCHEIN,
										kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
					}
				}
			}

		} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANGEBOT)) {
			AngebotDto belegDto = getAngebotFac().angebotFindByPrimaryKey(versandauftragDto.getIIdBeleg(),
					theClientDto);

			KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIIdAngebotsadresse(), theClientDto);

			alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(kDto.getPartnerIId(), true,
					versandauftragDto.getBelegartCNr(), theClientDto);

			AngebotpositionDto[] belegposPos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIId(belegDto.getIId(), theClientDto);

			for (int i = 0; i < belegposPos.length; i++) {

				if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
						hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(),
								getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
										belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_ANGEBOT,
										kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
					}
				}
			}
		} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
			AuftragDto belegDto = getAuftragFac().auftragFindByPrimaryKey(versandauftragDto.getIIdBeleg());

			KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIIdAuftragsadresse(), theClientDto);

			alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(kDto.getPartnerIId(), true,
					versandauftragDto.getBelegartCNr(), theClientDto);

			AuftragpositionDto[] belegposPos = getAuftragpositionFac().auftragpositionFindByAuftrag(belegDto.getIId());

			for (int i = 0; i < belegposPos.length; i++) {

				if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
						hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(),
								getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
										belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_AUFTRAG,
										kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
					}
				}
			}
		} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_RECHNUNG)
				|| versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_GUTSCHRIFT)) {
			RechnungDto belegDto = getRechnungFac().rechnungFindByPrimaryKey(versandauftragDto.getIIdBeleg());

			KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(belegDto.getKundeIId(), theClientDto);

			alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(kDto.getPartnerIId(), true,
					versandauftragDto.getBelegartCNr(), theClientDto);

			RechnungPositionDto[] belegposPos = getRechnungFac().rechnungPositionFindByRechnungIId(belegDto.getIId());

			for (int i = 0; i < belegposPos.length; i++) {

				if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
						hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(),
								getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
										belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_RECHNUNG,
										kDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
					}
				}
			}
		} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_ANFRAGE)) {
			AnfrageDto belegDto = getAnfrageFac().anfrageFindByPrimaryKey(versandauftragDto.getIIdBeleg(),
					theClientDto);

			if (belegDto.getLieferantIIdAnfrageadresse() != null) {
				LieferantDto lDto = getLieferantFac()
						.lieferantFindByPrimaryKey(belegDto.getLieferantIIdAnfrageadresse(), theClientDto);

				alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(lDto.getPartnerIId(), false,
						versandauftragDto.getBelegartCNr(), theClientDto);

				AnfragepositionDto[] belegposPos = getAnfragepositionFac()
						.anfragepositionFindByAnfrage(belegDto.getIId(), theClientDto);

				for (int i = 0; i < belegposPos.length; i++) {

					if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
						if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
							hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(),
									getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
											belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_ANFRAGE,
											lDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
						}
					}
				}
			}
		} else if (versandauftragDto.getBelegartCNr().equals(LocaleFac.BELEGART_BESTELLUNG)) {
			BestellungDto belegDto = getBestellungFac().bestellungFindByPrimaryKey(versandauftragDto.getIIdBeleg());

			LieferantDto lDto = getLieferantFac().lieferantFindByPrimaryKey(belegDto.getLieferantIIdBestelladresse(),
					theClientDto);

			alPartnerAnhaenge = getPartnerServicesFac().getPartnerkommentarAnhaenge(lDto.getPartnerIId(), false,
					versandauftragDto.getBelegartCNr(), theClientDto);

			BestellpositionDto[] belegposPos = getBestellpositionFac()
					.bestellpositionFindByBestellung(belegDto.getIId(), theClientDto);

			for (int i = 0; i < belegposPos.length; i++) {

				if (belegposPos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					if (!hmArtikelAnhaenge.containsKey(belegposPos[i].getArtikelIId())) {
						hmArtikelAnhaenge.put(belegposPos[i].getArtikelIId(),
								getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
										belegposPos[i].getArtikelIId(), LocaleFac.BELEGART_BESTELLUNG,
										lDto.getPartnerDto().getLocaleCNrKommunikation(), theClientDto));
					}
				}
			}
		}

		Iterator<Integer> it = hmArtikelAnhaenge.keySet().iterator();
		while (it.hasNext()) {
			Integer artikelId = it.next();
			ArtikelkommentarDto[] anhangeArtikel = hmArtikelAnhaenge.get(artikelId);

			for (int i = 0; i < anhangeArtikel.length; i++) {

				String dateiname = anhangeArtikel[i].getArtikelkommentarsprDto().getCDateiname();

				if (dateiname == null
						&& anhangeArtikel[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
					dateiname = i + ".pdf";
				}

				if (anhangeArtikel[i].getArtikelkommentarsprDto() != null && dateiname != null) {
					VersandanhangDto versandanhangDto = new VersandanhangDto();
					versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
					versandanhangDto.setCDateiname(dateiname);

					versandanhangDto.setOInhalt(anhangeArtikel[i].getArtikelkommentarsprDto().getOMedia());
					createVersandanhangImpl(versandanhangDto, theClientDto);
				}
			}
		}

		for (PartnerkommentarDto pkDto : alPartnerAnhaenge) {
			String dateiname = pkDto.getCDateiname();
			if (pkDto.getOMedia() != null && dateiname != null) {
				VersandanhangDto versandanhangDto = new VersandanhangDto();
				versandanhangDto.setVersandauftragIId(versandauftragDto.getIId());
				versandanhangDto.setCDateiname(dateiname);

				versandanhangDto.setOInhalt(pkDto.getOMedia());
				createVersandanhangImpl(versandanhangDto, theClientDto);
			}
		}

	}

	private void doSendMailOrFax(VersandauftragDto versandauftragDto, TheClientDto theClientDto) {
		if (versandauftragDto.getCEmpfaenger().contains("@")) {
			/*
			 * //sofort senden versandauftragDto = sendMailSofort(versandauftragDto,
			 * theClientDto); updateVersandauftrag(versandauftragDto, theClientDto);
			 */

			// ueber Timer senden
			sendMail(versandauftragDto, false, theClientDto);
		} else {
			// ist ein Fax
			String faxDomain = null;
			String mailAdmin = null;
			try {
				faxDomain = getParameterFac()
						.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_FAXDOMAIN,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
						.getCWert();
				mailAdmin = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_MAILADRESSE_ADMIN,
						ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant()).getCWert();
			} catch (EJBExceptionLP e) {
				myLogger.warn(e.getMessage());
			} catch (RemoteException e) {
				myLogger.warn(e.getMessage());
			}
			if (faxDomain != null)
				if (faxDomain.length() > 0)
					sendFaxAsMail(versandauftragDto, theClientDto, faxDomain, mailAdmin);
		}
	}

	private void sendFaxAsMail(VersandauftragDto versandauftragDto, TheClientDto theClientDto, String faxDomain,
			String mailAdmin) {
		versandauftragDto
				.setCEmpfaenger(HelperServer.formatFaxnummerFuerMail(versandauftragDto.getCEmpfaenger(), faxDomain));

		if (versandauftragDto.getCAbsenderadresse() == null)
			if (mailAdmin == null)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						"MailAdmin nicht angelegt");
			else
				versandauftragDto.setCAbsenderadresse(mailAdmin);

		sendMail(versandauftragDto, true, theClientDto);
	}

	@EJB
	private MailProcessorFac mailProcessor;

	private void sendMail(VersandauftragDto versandauftragDto, boolean isFax, TheClientDto theClientDto) {
		LPMailDto lpMailDto = new LPMailDto();
		lpMailDto.setFax(isFax);
		lpMailDto.setTheClientDto(theClientDto);

		String smtpServer = null;
		String smtpBenutzer = null;
		String smtpKennwort = null;
		try {
			smtpServer = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant()).getCWert();
			smtpBenutzer = getParameterFac()
					.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_BENUTZER,
							ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
					.getCWert();
			if (smtpBenutzer.length() > 0)
				smtpKennwort = getParameterFac()
						.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_KENNWORT,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
						.getCWert();
		} catch (EJBExceptionLP e1) {
			myLogger.error(e1.getMessage());
		} catch (RemoteException e1) {
			myLogger.error(e1.getMessage());
		}
		if (smtpServer != null) {
			if (smtpServer.length() > 0) {
				lpMailDto.setVersandauftragIId(versandauftragDto.getIId());
				lpMailDto.setSmtpServer(smtpServer);
				lpMailDto.setSmtpBenutzer(smtpBenutzer);
				lpMailDto.setSmtpKennwort(smtpKennwort);
				String imapServer = "";
				try {
					imapServer = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER,
							ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant()).getCWert();
				} catch (EJBExceptionLP e) {
					myLogger.warn(e.getMessage());
				} catch (RemoteException e) {
					myLogger.warn(e.getMessage());
				}
				lpMailDto.setImapServer(imapServer);
				if (imapServer.length() > 0) {
					Personal personal = em.find(Personal.class, versandauftragDto.getPersonalIId());
					if (personal != null) {
						lpMailDto.setImapBenutzer(personal.getCImapbenutzer());
						lpMailDto.setImapBenutzerKennwort(personal.getCImapkennwort());
					}
					String imapAdminKennwort = null;
					String imapAdmin = "";
					try {
						imapAdmin = getParameterFac()
								.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER_ADMIN,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
								.getCWert();
					} catch (EJBExceptionLP e) {
						myLogger.warn(e.getMessage());
					} catch (RemoteException e) {
						myLogger.warn(e.getMessage());
					}
					lpMailDto.setImapAdmin(imapAdmin);
					if (imapAdmin.length() > 0) {
						try {
							imapAdminKennwort = getParameterFac()
									.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT,
											ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
									.getCWert();
						} catch (EJBExceptionLP e) {
							myLogger.warn(e.getMessage());
						} catch (RemoteException e) {
							myLogger.warn(e.getMessage());
						}
						lpMailDto.setImapAdminKennwort(imapAdminKennwort);
					}
					try {
						String imapSentFolder = getParameterFac()
								.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER_SENTFOLDER,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
								.getCWert();
						if (imapSentFolder != null)
							if (imapSentFolder.length() > 0)
								lpMailDto.setSentFolder(imapSentFolder);
					} catch (EJBExceptionLP e) {
						myLogger.warn(e.getMessage());
					} catch (RemoteException e) {
						myLogger.warn(e.getMessage());
					}
				}
				if (isFax) {
					try {
						lpMailDto
								.setFaxDomain(getParameterFac()
										.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_FAXDOMAIN,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
										.getCWert());
						lpMailDto
								.setMailAdmin(getParameterFac()
										.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_MAILADRESSE_ADMIN,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
										.getCWert());
						lpMailDto.setFaxAnbindung(((Integer) getParameterFac()
								.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_FAXANBINDUNG,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
								.getCWertAsObject()).intValue());
						if (lpMailDto.getFaxAnbindung() == LPMail.FAXANBINDUNG_XPIRIO) {
							lpMailDto.setXpirioKennwort(getParameterFac()
									.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_XPIRIO_KENNWORT,
											ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
									.getCWert());
						}
					} catch (EJBExceptionLP e) {
						myLogger.warn(e.getMessage());
					} catch (RemoteException e) {
						myLogger.warn(e.getMessage());
					}
				}
				lpMailDto.setTSendezeitpunktwunsch(versandauftragDto.getTSendezeitpunktwunsch());
				byte[] versandinfo = HelperServer.getByteArrayFromObject(lpMailDto);
				Versandauftrag va = em.find(Versandauftrag.class, versandauftragDto.getIId());
				va.setOVersandinfo(versandinfo);
				em.merge(va);
//				@SuppressWarnings("unused")
//				javax.ejb.TimerHandle handle = mailProcessor.addMail(lpMailDto);
			}
		}

	}

	// TODO: Anhanege bei sendMailSofort
	private VersandauftragDto sendMailSofort(VersandauftragDto versandauftragDto, TheClientDto theClientDto) {
		String smtpServer = null;
		String smtpBenutzer = null;
		String smtpKennwort = null;
		try {
			smtpServer = getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant()).getCWert();
			smtpBenutzer = getParameterFac()
					.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_BENUTZER,
							ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
					.getCWert();
			if (smtpBenutzer.length() > 0)
				smtpKennwort = getParameterFac()
						.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_SMTPSERVER_KENNWORT,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
						.getCWert();
		} catch (EJBExceptionLP e1) {
			myLogger.error(e1.getMessage());
		} catch (RemoteException e1) {
			myLogger.error(e1.getMessage());
		}
		if (smtpServer != null) {
			if (smtpServer.length() > 0) {
				LPMail mail = new LPMail(smtpServer);
				Message message;
				try {
					File f = null;
					try {
						if (versandauftragDto.getOInhalt().length > 0) {
							String datei = null;
							if (versandauftragDto.getCDateiname() == null)
								datei = "anhang.pdf.";
							else
								datei = versandauftragDto.getCDateiname() + ".";
							f = File.createTempFile(datei, "");
						}
						FileOutputStream out = new FileOutputStream(f);
						out.write(versandauftragDto.getOInhalt()); // bytearray
																	// in die
																	// Datei
																	// schreiben
						out.flush();
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // neue temp-Datei mit Namen des Anhangs als Praefix
					List<String> anhaenge = new ArrayList<String>();
					if (f != null)
						anhaenge.add(f.getPath());
					message = mail.createMail(versandauftragDto.getCBetreff(), versandauftragDto.getCText(), null,
							versandauftragDto.getCEmpfaenger(), versandauftragDto.getCAbsenderadresse(),
							versandauftragDto.getCCcempfaenger(), null, anhaenge);
					mail.send(smtpServer, smtpBenutzer, smtpKennwort, message);
					myLogger.entry("[Mailversand] "
							+ Helper.formatTime(new Timestamp(System.currentTimeMillis()), new Locale("deAT"))
							+ versandauftragDto.toLogString());
					versandauftragDto.setStatusCNr(VersandFac.STATUS_ERLEDIGT);
					versandauftragDto.setCStatustext("Nachricht versandt.");
					try {
						String imapServer = getParameterFac()
								.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
								.getCWert();
						String imapAdmin = null;
						String imapAdminKennwort = null;
						if (imapServer.length() > 0) {
							imapAdmin = getParameterFac()
									.parametermandantFindByPrimaryKey(ParameterFac.PARAMETER_IMAPSERVER_ADMIN,
											ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
									.getCWert();
							if (imapAdmin.length() > 0)
								imapAdminKennwort = getParameterFac()
										.parametermandantFindByPrimaryKey(
												ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG, theClientDto.getMandant())
										.getCWert();
						}
						if (imapServer.length() > 0)
							mail.store(imapServer, imapAdmin, imapAdminKennwort, new LPMailDto().getSentFolder(),
									message);
					} catch (Exception e) {
						myLogger.warn(e.getMessage());
					}
				} catch (MessagingException e) {
					myLogger.error(e.getMessage());
					versandauftragDto.setStatusCNr(VersandFac.STATUS_FEHLGESCHLAGEN);
					versandauftragDto.setCStatustext(e.getMessage());
				}
			}
		}
		return versandauftragDto;
	}

	public void removeVersandauftrag(Integer versandauftragIId, TheClientDto theClientDto) throws EJBExceptionLP {

		// Zuerst Timer loeschen
		mailProcessor.killTimer(versandauftragIId);

		// Zuerst Anhaenge loeschen
		Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
		query.setParameter(1, versandauftragIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Versandanhang versandanhang = (Versandanhang) iterator.next();
			em.remove(versandanhang);
			em.flush();
		}

		// Dann den Versandauftrag
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(versandauftrag);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void storniereVersandauftrag(Integer versandauftragIId, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		// Zuerst Timer loeschen
		mailProcessor.killTimer(versandauftragIId);

		if (versandauftrag.getStatusCNr() == null) {
			versandauftrag.setStatusCNr(VersandFac.STATUS_STORNIERT);
		} else if (versandauftrag.getStatusCNr().equals(VersandFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT,
					new Exception("Versandauftrag iId=" + versandauftragIId + " wurde bereits versandt."));
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_KANN_NICHT_STORNIERT_WERDEN,
					new Exception("Versandauftrag iId=" + versandauftragIId + " kann nicht storniert werden. Status="
							+ versandauftrag.getStatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public VersandauftragDto updateVersandauftrag(VersandauftragDto versandauftragDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragDto);
		// begin
		Integer iId = versandauftragDto.getIId();
		try {
			Versandauftrag versandauftrag = em.find(Versandauftrag.class, iId);
			// boolean needTimeAdjust = false;
			// if (versandauftrag != null
			// && versandauftrag.getTSendezeitpunktwunsch() !=
			// versandauftragDto.getTSendezeitpunktwunsch()) {
			// needTimeAdjust = true;
			// }
			setVersandauftragFromVersandauftragDto(versandauftrag, versandauftragDto);
			// if (needTimeAdjust) {
			// sendeVersandauftragZeitpunktWunsch(iId, theClientDto);
			// }
			return versandauftragDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	public VersandanhangDto updateVersandanhang(VersandanhangDto versandanhangDto) {
		Versandanhang versandanhang = em.find(Versandanhang.class, versandanhangDto.getIId());
		setVersandanhangFromVersandanhangDto(versandanhang, versandanhangDto);
		return versandanhangDto;
	}

	public VersandauftragDto versandauftragFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		VersandauftragDto versandauftrag = versandauftragFindByPrimaryKeyOhneExc(iId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return versandauftrag;
	}

	public VersandauftragDto versandauftragFindByPrimaryKeyOhneExc(Integer iId) {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, iId);
		return versandauftrag == null ? null : assembleVersandauftragDto(versandauftrag);
	}

	public VersandanhangDto versandanhangFindByPrimaryKeyOhneExc(Integer iId) {
		Versandanhang versandanhang = em.find(Versandanhang.class, iId);
		return versandanhang == null ? null : assembleVersandanhangDto(versandanhang);
	}

	private void setVersandauftragFromVersandauftragDto(Versandauftrag versandauftrag,
			VersandauftragDto versandauftragDto) {
		versandauftrag.setCEmpfaenger(versandauftragDto.getCEmpfaenger());
		versandauftrag.setCCcempfaenger(versandauftragDto.getCCcempfaenger());
		versandauftrag.setCBetreff(versandauftragDto.getCBetreff());
		versandauftrag.setCText(versandauftragDto.getCText());
		versandauftrag.setCAbsenderadresse(versandauftragDto.getCAbsenderadresse());
		versandauftrag.setCDateiname(versandauftragDto.getCDateiname());
		versandauftrag.setTSendezeitpunktwunsch(versandauftragDto.getTSendezeitpunktwunsch());
		versandauftrag.setTSendezeitpunkt(versandauftragDto.getTSendezeitpunkt());
		versandauftrag.setPersonalIId(versandauftragDto.getPersonalIId());
		versandauftrag.setTAnlegen(versandauftragDto.getTAnlegen());
		versandauftrag.setPartnerIIdEmpfaenger(versandauftragDto.getPartnerIIdEmpfaenger());
		versandauftrag.setPartnerIIdSender(versandauftragDto.getPartnerIIdSender());
		versandauftrag.setBelegartCNr(versandauftragDto.getBelegartCNr());
		versandauftrag.setIBelegIId(versandauftragDto.getIIdBeleg());
		versandauftrag.setBEmpfangsbestaetigung(versandauftragDto.getBEmpfangsbestaetigung());
		// READ ONLY
		// versandauftrag.setStatusCNr(versandauftragDto.getStatusCNr());
		versandauftrag.setCStatustext(versandauftragDto.getCStatustext());
		versandauftrag.setOInhalt(versandauftragDto.getOInhalt());
		versandauftrag.setCBccempfaenger(versandauftragDto.getCBccempfaenger());
		// em.merge(versandauftrag);
	}

	private VersandauftragDto assembleVersandauftragDto(Versandauftrag versandauftrag) {
		return VersandauftragDtoAssembler.createDto(versandauftrag);
	}

	private VersandanhangDto assembleVersandanhangDto(Versandanhang versandanhang) {
		return VersandanhangDtoAssembler.createDto(versandanhang);
	}

	private VersandauftragDto[] assembleVersandauftragDtos(Collection<?> versandauftrags) {
		List<VersandauftragDto> list = new ArrayList<VersandauftragDto>();
		if (versandauftrags != null) {
			Iterator<?> iterator = versandauftrags.iterator();
			while (iterator.hasNext()) {
				Versandauftrag versandauftrag = (Versandauftrag) iterator.next();
				list.add(assembleVersandauftragDto(versandauftrag));
			}
		}
		VersandauftragDto[] returnArray = new VersandauftragDto[list.size()];
		return list.toArray(returnArray);
	}

	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto, String belegartCNr, Integer iIdBeleg,
			Locale locSprache, TheClientDto theClientDto) throws EJBExceptionLP {
		LpMailText mt = createLpMailText(new LpMailText(), mailtextDto, theClientDto);
		if (mt != null) {
			String betreff = mt.transformBetreff(mailtextDto, theClientDto);
			if (betreff != null)
				return betreff;
		}
		if (belegartCNr != null && iIdBeleg != null) {
			return getDefaultDateinameForBelegEmail(belegartCNr, iIdBeleg, locSprache, theClientDto);
		}

		return "kein Beleg.pdf";
	}

	public String getUebersteuertenAbsenderFuerBelegEmail(MailtextDto mailtextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LpMailText mt = createLpMailText(new LpMailText(), mailtextDto, theClientDto);
		if (mt != null) {
			return mt.transformAbsender(mailtextDto, theClientDto);
		}
		return null;
	}

	private LpMailText createLpMailText(LpMailText mt, MailtextDto mailtextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(mailtextDto, "mailtextDto");
		try {
			String sAnrede;
			Locale locDruck;
			if (mailtextDto.getParamLocale() != null) {
				locDruck = mailtextDto.getParamLocale();
			} else {
				locDruck = theClientDto.getLocUi();
			}
			if (mailtextDto.getMailAnprechpartnerIId() != null && mailtextDto.getMailPartnerIId() != null) {
				sAnrede = getPartnerServicesFac().getBriefanredeFuerBeleg(mailtextDto.getMailAnprechpartnerIId(),
						mailtextDto.getMailPartnerIId(), locDruck, theClientDto);
			} else {
				// neutrale Anrede
				if (mailtextDto.getMailPartnerIId() != null) {
					sAnrede = getBriefanredeNeutralOderPrivatperson(mailtextDto.getMailPartnerIId(), locDruck,
							theClientDto);
				} else {
					sAnrede = getTextRespectUISpr("lp.anrede.sehrgeehrtedamenundherren", theClientDto.getMandant(),
							locDruck);
				}

			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_ANREDE_ANSPRECHPARTNER, sAnrede);
			mt.addParameter(VersandFac.MAIL_PARAMETER_BELEGNUMMER, mailtextDto.getMailBelegnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEZEICHNUNG, mailtextDto.getMailBezeichnung());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BELEGDATUM,
					Helper.formatDatum(mailtextDto.getMailBelegdatum(), mailtextDto.getParamLocale()));

			if (mailtextDto.getMailBelegdatum() != null) {
				DateFormat df = new SimpleDateFormat("yyMMdd");
				String s = df.format(mailtextDto.getMailBelegdatum());
				mt.addParameter(VersandFac.MAIL_PARAMETER_BELEGDATUM_YYMMDD, s);
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_PROJEKT, mailtextDto.getMailProjekt());

			if (mailtextDto.getMailProjekt() != null) {

				String[] teile = mailtextDto.getMailProjekt().split(" ");

				mt.addParameter(VersandFac.MAIL_PARAMETER_PROJEKT1, teile[0]);
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER, mailtextDto.getMailVertreterAlsString());

			mt.addParameter(VersandFac.MAIL_PARAMETER_FUSSTEXT, Helper.removeStyles(mailtextDto.getMailFusstext()));
			mt.addParameter(VersandFac.MAIL_PARAMETER_TEXT, mailtextDto.getMailText());
			mt.addParameter(VersandFac.MAIL_PARAMETER_KUNDENBESTELLNUMMER, mailtextDto.getKundenbestellnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_ABNUMMER, mailtextDto.getAbnummer());

			PersonRpt personRpt = null;

			if (mailtextDto.getMailVertreter() != null) {
				personRpt = getPersonalFac().getPersonRpt(mailtextDto.getMailVertreter().getIId(), theClientDto);
			} else {
				personRpt = getPersonalFac().getPersonRpt(theClientDto.getIDPersonal(), theClientDto);
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_EMAIL, personRpt.getSEmail());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_MOBIL, personRpt.getSMobil());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_NACHNAME, personRpt.getSNachname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONDWFIRMA, personRpt.getSTelefonDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONFIRMA, personRpt.getSTelefonFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONFIRMAMITDW,
					personRpt.getSTelefonFirmaMitDurchwahlBearbeiter());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TITEL, personRpt.getSTitel());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_VORNAME, personRpt.getSVorname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_NTITEL, personRpt.getSNtitel());

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_DIREKTFAX, personRpt.getSDirektfax());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_FAXDWFIRMA, personRpt.getSFaxDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_FAXFIRMAMITDW,
					personRpt.getSFaxFirmaMitDurchwahlBearbeiter());

			PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKeySmall(personRpt.getPersonalIId());

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSFUNKTION,
					personalDto.getCUnterschriftsfunktion());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSTEXT, personalDto.getCUnterschriftstext());

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_SIGNATUR,
					getPersonalFac().getSignatur(personalDto.getIId(), theClientDto.getLocUiAsString()));

			personRpt = getPersonalFac().getPersonRpt(theClientDto.getIDPersonal(), theClientDto);

			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_EMAIL, personRpt.getSEmail());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_MOBIL, personRpt.getSMobil());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_NACHNAME, personRpt.getSNachname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONDWFIRMA, personRpt.getSTelefonDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONFIRMA, personRpt.getSTelefonFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONFIRMAMITDW,
					personRpt.getSTelefonFirmaMitDurchwahlBearbeiter());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TITEL, personRpt.getSTitel());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_VORNAME, personRpt.getSVorname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_NTITEL, personRpt.getSNtitel());

			// PJ14540
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_DIREKTFAX, personRpt.getSDirektfax());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_FAXDWFIRMA, personRpt.getSFaxDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_FAXFIRMAMITDW,
					personRpt.getSFaxFirmaMitDurchwahlBearbeiter());

			personalDto = getPersonalFac().personalFindByPrimaryKeySmall(theClientDto.getIDPersonal());

			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSFUNKTION,
					personalDto.getCUnterschriftsfunktion());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSTEXT,
					personalDto.getCUnterschriftstext());

			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_SIGNATUR,
					getPersonalFac().getSignatur(personalDto.getIId(), theClientDto.getLocUiAsString()));

			// PJ21286
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_ANDREDE, mailtextDto.getPerson_anrede());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_PERSONALNUMMER, mailtextDto.getPerson_personalnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_TITEL, mailtextDto.getPerson_titel());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_VORNAME1, mailtextDto.getPerson_vorname1());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_VORNAME2, mailtextDto.getPerson_vorname2());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_NACHNAME, mailtextDto.getPerson_nachname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_PERSON_NTITEL, mailtextDto.getPerson_ntitel());

			// PJ21315
			mt.addParameter(VersandFac.MAIL_PARAMETER_XLS_MAILVERSAND_ABGABETERMIN,
					Helper.formatDatum(mailtextDto.getXlsmailversand_abgabetermin(), theClientDto.getLocUi()));
			mt.addParameter(VersandFac.MAIL_PARAMETER_XLS_MAILVERSAND_ENDKUNDE,
					mailtextDto.getXlsmailversand_endkunde());
			mt.addParameter(VersandFac.MAIL_PARAMETER_XLS_MAILVERSAND_GEPLANTERFERTIGUNGSTERMIN, Helper
					.formatDatum(mailtextDto.getXlsmailversand_geplanterfertigungstermin(), theClientDto.getLocUi()));
			mt.addParameter(VersandFac.MAIL_PARAMETER_XLS_MAILVERSAND_PROJEKT, mailtextDto.getXlsmailversand_projekt());

			// PJ 17506
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_LIEFERSCHEIN, mailtextDto.getRekla_lieferschein());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_RECHNUNG, mailtextDto.getRekla_rechnung());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_KNDREKLANR, mailtextDto.getRekla_kndreklanr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_KNDLSNR, mailtextDto.getRekla_kndlsnr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_LSNR, mailtextDto.getRekla_we_lsnr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_WEDATUM, mailtextDto.getRekla_we_datum());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_LSDATUM, mailtextDto.getRekla_we_lsdatum());

			if (personalDto.getPartnerIIdFirma() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(personalDto.getPartnerIIdFirma(),
						theClientDto);

				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME1, partnerDto.getCName1nachnamefirmazeile1());
				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME2, partnerDto.getCName2vornamefirmazeile2());
				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME3, partnerDto.getCName3vorname2abteilung());

				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_STRASSE, partnerDto.getCStrasse());

				if (partnerDto.getLandplzortDto() != null) {
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_LKZ,
							partnerDto.getLandplzortDto().getLandDto().getCLkz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_PLZ, partnerDto.getLandplzortDto().getCPlz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_ORT,
							partnerDto.getLandplzortDto().getOrtDto().getCName());
				}
			}

			if (mailtextDto.getProjektIId() != null) {
				ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(mailtextDto.getProjektIId());
				mt.addParameter(VersandFac.MAIL_PARAMETER_PROJEKTNUMMER, projektDto.getCNr());
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_ANGEBOT_ANFRAGENUMMER, mailtextDto.getAngebotAnfragenummer());
			if (mailtextDto.getAnwesenheitsLfdnr() != null) {
				mt.addParameter(VersandFac.MAIL_PARAMETER_ANWESENHEIT_LFDNR,
						mailtextDto.getAnwesenheitsLfdnr().toString());
			}

			if (mailtextDto.getMailPartnerIId() != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(mailtextDto.getMailPartnerIId(),
						theClientDto);

				mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_KBEZ, partnerDto.getCKbez());

				mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_NAME1, partnerDto.getCName1nachnamefirmazeile1());
				mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_NAME2, partnerDto.getCName2vornamefirmazeile2());
				mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_NAME3, partnerDto.getCName3vorname2abteilung());
				mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_STRASSE, partnerDto.getCStrasse());

				if (partnerDto.getLandplzortDto() != null) {
					mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_LKZ,
							partnerDto.getLandplzortDto().getLandDto().getCLkz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_PLZ, partnerDto.getLandplzortDto().getCPlz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_PARTNER_ORT,
							partnerDto.getLandplzortDto().getOrtDto().getCName());
				}
			}

			if (mailtextDto.getBelegVersion() != null) {
				mt.addParameter(VersandFac.MAIL_PARAMETER_BELEG_VERSION, mailtextDto.getBelegVersion().toString());
			}

			if (mailtextDto.getBisDatum() != null) {
				mt.addParameter(VersandFac.MAIL_PARAMETER_BIS_DATUM,
						Helper.formatDatum(mailtextDto.getBisDatum(), mailtextDto.getParamLocale()));
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_RECHNUNGSART, mailtextDto.getRechnungsart());
			mt.addParameter(VersandFac.MAIL_PARAMETER_KOPFTEXT, mailtextDto.getMailKopftext());

			// PJ22158
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_HOMEPAGE, mandantDto.getPartnerDto().getCHomepage());
			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_FIRMENBUCHNR,
					mandantDto.getPartnerDto().getCFirmenbuchnr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_GERICHTSSTAND,
					mandantDto.getPartnerDto().getCGerichtsstand());
			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_UID, mandantDto.getPartnerDto().getCUid());
			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_FAX, mandantDto.getPartnerDto().getCFax());

			// PJ22452
			mt.addParameter(VersandFac.MAIL_PARAMETER_LS_VERSANDNUMMER, mailtextDto.getLs_versandnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_LS_VERSANDNUMMER2, mailtextDto.getLs_versandnummer2());

			// PJ22517
			mt.addParameter(VersandFac.MAIL_PARAMETER_LS_SPEDITEUR_NAME, mailtextDto.getLs_spediteur_name());
			mt.addParameter(VersandFac.MAIL_PARAMETER_LS_SPEDITEUR_WEBSITE, mailtextDto.getLs_spediteur_website());

			// PJ22208

			PersonalDto[] aGeschaeftsfDto = getPersonalFac().personalFindByMandantCNrPersonalfunktionCNr(
					theClientDto.getMandant(), PersonalFac.PERSONALFUNKTION_GESCHAEFTSFUEHRER);
			String sGeschaeftsfuehrer = null;
			for (int i = 0; i < aGeschaeftsfDto.length; i++) {

				boolean bAusgetreten = getPersonalFac().istPersonalAusgetreten(aGeschaeftsfDto[i].getIId(),
						getTimestamp(), theClientDto);
				if (bAusgetreten == false) {
					PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(aGeschaeftsfDto[i].getPartnerIId(),
							theClientDto);

					if (sGeschaeftsfuehrer == null) {
						sGeschaeftsfuehrer = partnerDto.formatFixTitelVornameNachnameNTitel();
					} else {
						sGeschaeftsfuehrer += ", " + partnerDto.formatFixTitelVornameNachnameNTitel();
					}

				}

			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_MANDANT_GESCHAEFSTFUEHRER, sGeschaeftsfuehrer);

			return mt;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public String getDefaultTextForBelegEmail(MailtextDto mailtextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

//		LpMailText mt = createLpMailText(new LpMailText(), mailtextDto,
//				theClientDto);
		LpMailText mt = createLpMailText(new LpMailTextMitHtmlSignatur(getParameterFac()), mailtextDto, theClientDto);
		return mt != null ? mt.transformText(mailtextDto, theClientDto) : null;
	}

	public String getDefaultTextForBelegHtmlEmail(MailtextDto mailtextDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LpMailText mt = createLpMailText(new LpHtmlMailText(), mailtextDto, theClientDto);
		return mt != null ? mt.transformText(mailtextDto, theClientDto) : null;
	}

	public String getVersandstatus(String belegartCNr, Integer i_belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		Session session = null;
		String status = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cStatus = session.createCriteria(FLRVersandauftrag.class);
			cStatus.add(Restrictions.eq("belegart_c_nr", belegartCNr));
			cStatus.add(Restrictions.eq("i_belegiid", i_belegIId));
			cStatus.addOrder(Order.desc("t_sendezeitpunktwunsch"));
			List<?> lStatus = cStatus.list();
			Iterator<?> iter = lStatus.iterator();
			if (iter.hasNext()) {
				FLRVersandauftrag flr = (FLRVersandauftrag) iter.next();

				if (flr.getStatus_c_nr() != null) {
					status = flr.getStatus_c_nr().trim();
				} else {
					status = getTextRespectUISpr("lp.system.postausgang", mandantCNr, locUI);

				}

				if (flr.getC_empfaenger().contains("@")) {
					status += "/" + getTextRespectUISpr("lp.email", mandantCNr, locUI);
				} else {
					status += "/" + getTextRespectUISpr("lp.fax", mandantCNr, locUI);
				}

				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKeySmall(flr.getPersonal_i_id());
				status += "/" + personalDto.getCKurzzeichen() + "/"
						+ Helper.formatDatumZeit(flr.getT_sendezeitpunktwunsch(), locUI);

				return status;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	public String getFormattedVersandstatus(String belegartCNr, Integer i_belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		Session session = null;
		String status = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria cStatus = session.createCriteria(FLRVersandauftrag.class);
			cStatus.add(Restrictions.eq("belegart_c_nr", belegartCNr));
			cStatus.add(Restrictions.eq("i_belegiid", i_belegIId));
			cStatus.addOrder(Order.desc("t_sendezeitpunktwunsch"));
			List<?> lStatus = cStatus.list();
			Iterator<?> iter = lStatus.iterator();
			if (iter.hasNext()) {
				FLRVersandauftrag flr = (FLRVersandauftrag) iter.next();
				status = flr.getStatus_c_nr();
				if (status == null) {
					java.util.Date dSend = flr.getT_sendezeitpunktwunsch();
					Calendar cal = Calendar.getInstance();
					java.util.Date dNow = cal.getTime();
					long lfiveminutes = 5 * 60 * 1000; // 5 minutes
					java.util.Date dSendAndFive = new java.util.Date(dSend.getTime() + lfiveminutes);
					int iComp = dNow.compareTo(dSendAndFive);
					if (iComp > 0) {
						// red
						status = "~r";
					} else {
						// black
						status = "~b";
					}
					String sDateFull = Helper.formatDatumZeit(flr.getT_sendezeitpunktwunsch(), theClientDto.getLocUi());
					String[] sTempstring = sDateFull.split(":");
					String sDate = sTempstring[0] + ":" + sTempstring[1];
					if (flr.getC_empfaenger().contains("@")) {
						status = status + getTextRespectUISpr("kund.sent", mandantCNr, locUI) + " " + sDate;
					} else {
						status = status + getTextRespectUISpr("kund.sent.fax", mandantCNr, locUI) + " " + sDate;
					}
					return status;
				} else {
					if (flr.getC_empfaenger().contains("@")) {
						return getTextRespectUISpr("lp.versandstatus.mail", mandantCNr, locUI) + " " + status;
					} else {
						return getTextRespectUISpr("lp.versandstatus.fax", mandantCNr, locUI) + " " + status;
					}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public String getDefaultAnhangForBelegEmail(MailtextDto mailtextDto, String belegartCNr, Integer belegIId,
			Locale locSprache, TheClientDto theClientDto) {
		if (mailtextDto != null) {
			LpMailText mt = createLpMailText(new LpMailText(), mailtextDto, theClientDto);
			if (mt != null) {
				String betreff = mt.transformAnhang(mailtextDto, theClientDto);
				if (betreff != null)
					return betreff + ".pdf";
			}
		}

		if (belegartCNr != null && belegIId != null) {
			return getDefaultDateinameForBelegImpl(belegartCNr, belegIId, locSprache, theClientDto);
		}

		return getTextRespectUISpr("lp.versand.anhang", theClientDto.getMandant(), locSprache) + ".pdf";
	}

	@Override
	public String getUebersteuertenDateinamenForBeleg(MailtextDto mailtextDto, String belegartCNr, Integer belegIId,
			Locale locSprache, TheClientDto theClientDto) {
		return getDefaultAnhangForBelegEmail(mailtextDto, belegartCNr, belegIId, locSprache, theClientDto);
	}

	public String getDefaultDateinameForBelegEmail(String belegartCNr, Integer belegIId, Locale locSprache,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (belegartCNr != null && belegIId != null) {
			return getDefaultDateinameForBelegImpl(belegartCNr, belegIId, locSprache, theClientDto);
		}

		return getTextRespectUISpr("lp.versand.beleg.dateiname.default", theClientDto.getMandant(), locSprache)
				+ ".pdf";
	}

	private String getDefaultDateinameForBelegImpl(String belegartCNr, Integer iIdBeleg, Locale locSprache,
			TheClientDto theClientDto) throws EJBExceptionLP {
		String sBelegart = null;
		String sBelegnummer = null;
		try {
			sBelegart = getLocaleFac().uebersetzeBelegartOptimal(belegartCNr, locSprache, locSprache);
		} catch (RemoteException ex) {
			// unuebersetzt ist besser als nix
			sBelegart = belegartCNr;
		}
		try {
			if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
				sBelegnummer = getAuftragFac().auftragFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
				sBelegnummer = getBestellungFac().bestellungFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)
					|| belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)
					|| belegartCNr.equals(LocaleFac.BELEGART_PROFORMARECHNUNG)) {
				sBelegnummer = getRechnungFac().rechnungFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				sBelegnummer = getLieferscheinFac().lieferscheinFindByPrimaryKey(iIdBeleg, theClientDto).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
				sBelegnummer = getAngebotFac().angebotFindByPrimaryKey(iIdBeleg, theClientDto).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANFRAGE)) {
				sBelegnummer = getAnfrageFac().anfrageFindByPrimaryKey(iIdBeleg, theClientDto).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_PROJEKT)) {
				sBelegnummer = getProjektFac().projektFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_INSERAT)) {
				sBelegnummer = getInseratFac().inseratFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_REKLAMATION)) {
				sBelegnummer = getReklamationFac().reklamationFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (LocaleFac.BELEGART_EINGANGSRECHNUNG.equals(belegartCNr)) {
				sBelegnummer = getEingangsrechnungFac().eingangsrechnungFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (LocaleFac.BELEGART_EINKAUFSANGEBOT.equals(belegartCNr)) {
				sBelegnummer = getAngebotstklFac().einkaufsangebotFindByPrimaryKey(iIdBeleg).getCNr();
			} else {
				myLogger.warn("Fuer Belegart '" + belegartCNr
						+ "' gibt es noch keine Implementierung fuer den Default-Dateinamen");
				return "";
//				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
//						"belegart " + belegartCNr + " nicht implementiert"));
			}
		} catch (Exception ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}
		String sDateiname = sBelegart.trim() + " " + sBelegnummer.trim() + ".pdf";
		// keine "/"
		sDateiname = sDateiname.replaceAll("/", "-");
		return sDateiname;
	}

	public void createVersandstatus(VersandstatusDto versandstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Versandstatus versandstatus = new Versandstatus(versandstatusDto.getStatusCNr(),
					versandstatusDto.getISort());
			em.persist(versandstatus);
			em.flush();
			setVersandstatusFromVersandstatusDto(versandstatus, versandstatusDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeVersandstatus(VersandstatusDto versandstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (versandstatusDto != null) {
			String statusCNr = versandstatusDto.getStatusCNr();
			try {
				Versandstatus toRemove = em.find(Versandstatus.class, statusCNr);
				if (toRemove == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			}
		}
	}

	public void updateVersandstatus(VersandstatusDto versandstatusDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (versandstatusDto != null) {
			String statusCNr = versandstatusDto.getStatusCNr();
			try {
				Versandstatus versandstatus = em.find(Versandstatus.class, statusCNr);
				setVersandstatusFromVersandstatusDto(versandstatus, versandstatusDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public VersandstatusDto versandstatusFindByPrimaryKey(String statusCNr) throws EJBExceptionLP {
		try {
			Versandstatus versandstatus = em.find(Versandstatus.class, statusCNr);
			if (versandstatus == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleVersandstatusDto(versandstatus);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIId(Integer partnerIId)
			throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em.createNamedQuery("VersandauftragfindByEmpfaengerPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		versandauftragDtos = assembleVersandauftragDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return versandauftragDtos;
	}

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIIdOhneExc(Integer partnerIId)
			throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em.createNamedQuery("VersandauftragfindByEmpfaengerPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		versandauftragDtos = assembleVersandauftragDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + partnerIId, ex);
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return versandauftragDtos;
	}

	public VersandauftragDto[] versandauftragFindBySenderPartnerIId(Integer partnerIId)
			throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em.createNamedQuery("VersandauftragfindBySenderPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		versandauftragDtos = assembleVersandauftragDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return versandauftragDtos;
	}

	public VersandauftragDto[] versandauftragFindBySenderPartnerIIdOhneExc(Integer partnerIId)
			throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em.createNamedQuery("VersandauftragfindBySenderPartnerIId");
		query.setParameter(1, partnerIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		versandauftragDtos = assembleVersandauftragDtos(cl);

		// }
		// catch (ObjectNotFoundException ex) {
		// myLogger.warn("partnerIId=" + partnerIId, ex);
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return versandauftragDtos;
	}

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(Integer versandauftragIID)
			throws EJBExceptionLP, RemoteException {
		// try {
		Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
		query.setParameter(1, versandauftragIID);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		return assembleVersandanhangDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	private VersandanhangDto[] assembleVersandanhangDtos(Collection<?> versandanhaenge) {
		List<VersandanhangDto> list = new ArrayList<VersandanhangDto>();
		if (versandanhaenge != null) {
			Iterator<?> iterator = versandanhaenge.iterator();
			while (iterator.hasNext()) {
				Versandanhang versandanhang = (Versandanhang) iterator.next();
				list.add(assembleVersandanhangDto(versandanhang));
			}
		}
		VersandanhangDto[] returnArray = new VersandanhangDto[list.size()];
		return list.toArray(returnArray);
	}

	private void setVersandstatusFromVersandstatusDto(Versandstatus versandstatus, VersandstatusDto versandstatusDto) {
		versandstatus.setISort(versandstatusDto.getISort());
		em.merge(versandstatus);
		em.flush();
	}

	private void setVersandanhangFromVersandanhangDto(Versandanhang versandanhang, VersandanhangDto versandanhangDto) {
		versandanhang.setCDateiname(versandanhangDto.getCDateiname());
		versandanhang.setIId(versandanhangDto.getIId());
		versandanhang.setOInhalt(versandanhangDto.getOInhalt());
		versandanhang.setVersandauftragIId(versandanhangDto.getVersandauftragIId());
		em.merge(versandanhang);
		em.flush();
	}

	private VersandstatusDto assembleVersandstatusDto(Versandstatus versandstatus) {
		return VersandstatusDtoAssembler.createDto(versandstatus);
	}

	private VersandstatusDto[] assembleVersandstatusDtos(Collection<?> versandstatuss) {
		List<VersandstatusDto> list = new ArrayList<VersandstatusDto>();
		if (versandstatuss != null) {
			Iterator<?> iterator = versandstatuss.iterator();
			while (iterator.hasNext()) {
				Versandstatus versandstatus = (Versandstatus) iterator.next();
				list.add(assembleVersandstatusDto(versandstatus));
			}
		}
		VersandstatusDto[] returnArray = new VersandstatusDto[list.size()];
		return list.toArray(returnArray);
	}

	public Integer versandauftragFindFehlgeschlagenen(TheClientDto theClientDto) throws EJBExceptionLP {
		// begin
		Integer iId = null;
		// try {
		Query query = em.createNamedQuery("VersandauftragfindByPersonalIIdStatus");
		query.setParameter(1, theClientDto.getIDPersonal());
		query.setParameter(2, VersandFac.STATUS_FEHLGESCHLAGEN);
		Versandauftrag versandauftrag = (Versandauftrag) query.getSingleResult();
		if (versandauftrag == null) {
			return null;
		}
		iId = versandauftrag.getIId();
		// }
		// catch (FinderException ex) {
		// }
		return iId;
	}

	public VersandauftragDto[] versandauftragFindOffen(Integer anzahl) throws EJBExceptionLP {
		Query query = em.createNamedQuery("VersandauftragfindOffen");
		query.setMaxResults(anzahl);
		query.setParameter(1, new Date(System.currentTimeMillis()));
		@SuppressWarnings("unchecked")
		List<Versandauftrag> list = query.getResultList();
		return assembleVersandauftragDtos(list);
	}

	public VersandauftragDto[] versandauftragFindAblage(Integer anzahl) throws EJBExceptionLP {
		Query query = em.createNamedQuery("VersandauftragfindStatusCNr");
		query.setMaxResults(anzahl);
		query.setParameter(1, VersandFac.STATUS_TEILERLEDIGT);
		@SuppressWarnings("unchecked")
		List<Versandauftrag> list = query.getResultList();
		return assembleVersandauftragDtos(list);
	}

	public VersandauftragDto versandauftragFindNextNotInDoc() {
		Query query = em.createNamedQuery("VersandauftragfindAllOhneDaten");
		query.setMaxResults(1);
		try {
			Versandauftrag versandauftrag = (Versandauftrag) query.getSingleResult();
			VersandauftragDto versandauftragDto = assembleVersandauftragDto(versandauftrag);
			return versandauftragDto;
		} catch (javax.persistence.NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN, e);
		}

	}

	public void sendeVersandauftragErneut(Integer versandauftragIId, Timestamp tSendezeitpunktWunsch,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		if (tSendezeitpunktWunsch == null) {
			// Dann zieht der Mandantparameter als Vorlauf
			GregorianCalendar gc = new GregorianCalendar();
			int delay;
			try {
				ParametermandantDto pmBeginnMonat = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				delay = Integer.parseInt(pmBeginnMonat.getCWert());
			} catch (RemoteException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, new Exception(
						"Parameter " + ParameterFac.PARAMETER_DELAY_E_MAILVERSAND + " konnte nicht gefunden werden"));
			}
			gc.set(GregorianCalendar.MINUTE, gc.get(GregorianCalendar.MINUTE) + delay);
			tSendezeitpunktWunsch = new Timestamp(gc.getTimeInMillis());
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (versandauftrag.getStatusCNr() == null
				|| versandauftrag.getStatusCNr().equals(VersandFac.STATUS_DATEN_UNGUELTIG)
				|| versandauftrag.getStatusCNr().equals(VersandFac.STATUS_STORNIERT)
				|| versandauftrag.getStatusCNr().equals(VersandFac.STATUS_FEHLGESCHLAGEN)) {
			versandauftrag.setStatusCNr(null);
			versandauftrag.setTSendezeitpunktwunsch(tSendezeitpunktWunsch);
		} else if (versandauftrag.getStatusCNr().equals(VersandFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT,
					new Exception("Versandauftrag iId=" + versandauftragIId + " wurde bereits versandt."));
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("Versandauftrag iId=" + versandauftragIId
					+ " hat ungueltigen Status. Status=" + versandauftrag.getStatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void sendeVersandauftragSofort(Integer versandauftragIId, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		// Wenn er noch nicht versandt wurde und nicht in Bearbeitung
		if (versandauftrag.getStatusCNr() == null && versandauftrag.getCJobid() == null) {
			versandauftrag.setTSendezeitpunktwunsch(getTimestamp());
			em.merge(versandauftrag);
			em.flush();
		}
		/*
		 * String smtpServer = null; try { smtpServer =
		 * getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.
		 * PARAMETER_SMTPSERVER, ParameterFac.KATEGORIE_VERSANDAUFTRAG,
		 * theClientDto.getMandant()).getCWert(); } catch (EJBExceptionLP e1) {
		 * myLogger.error(e1.getMessage()); } catch (RemoteException e1) {
		 * myLogger.error(e1.getMessage()); } if (smtpServer != null) {
		 * javax.ejb.TimerHandle handle = mailProcessor.updateTimer(versandauftragIId,
		 * versandauftrag.getTSendezeitpunktwunsch()); if (handle == null) { // kein
		 * Timer fuer den Auftrag vorhanden
		 * myLogger.warn("Kein Timer gefunden fuer Versandauftrag: " +
		 * versandauftragIId); VersandauftragDto versandauftragDto =
		 * assembleVersandauftragDto(versandauftrag); doSendMailOrFax(versandauftragDto,
		 * theClientDto); myLogger.warn("Versandauftrag erneut versandt: " +
		 * versandauftragIId); } } }
		 */
		// }
		// catch (ObjectNotFoundException ex) {
		// Konnte nicht gefunden werden
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	public void sendeVersandauftragZeitpunktWunsch(Integer versandauftragIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		// Wenn er noch nicht versandt wurde und nicht in Bearbeitung
		if (versandauftrag.getStatusCNr() == null && versandauftrag.getCJobid() == null) {

			/*
			 * String smtpServer = null; try { smtpServer =
			 * getParameterFac().parametermandantFindByPrimaryKey(ParameterFac.
			 * PARAMETER_SMTPSERVER, ParameterFac.KATEGORIE_VERSANDAUFTRAG,
			 * theClientDto.getMandant()).getCWert(); } catch (EJBExceptionLP e1) {
			 * myLogger.error(e1.getMessage()); } catch (RemoteException e1) {
			 * myLogger.error(e1.getMessage()); } if (smtpServer != null) {
			 * javax.ejb.TimerHandle handle = mailProcessor.updateTimer(versandauftragIId,
			 * versandauftrag.getTSendezeitpunktwunsch()); if (handle == null) { // kein
			 * Timer fuer den Auftrag vorhanden
			 * myLogger.warn("Kein Timer gefunden fuer Versandauftrag: " +
			 * versandauftragIId); VersandauftragDto versandauftragDto =
			 * assembleVersandauftragDto(versandauftrag); doSendMailOrFax(versandauftragDto,
			 * theClientDto); myLogger.warn("Versandauftrag erneut versandt: " +
			 * versandauftragIId); } }
			 */
		}
		// }
		// catch (ObjectNotFoundException ex) {
		// Konnte nicht gefunden werden
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	public VersandanhangDto createVersandanhang(VersandanhangDto versandanhangDto, TheClientDto theClientDto) {
		myLogger.logData(versandanhangDto, theClientDto.getIDUser());
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_VERSANDANHANG);
		versandanhangDto.setIId(iId);
		try {
			Versandanhang versandanhang = new Versandanhang(versandanhangDto.getIId());
			versandanhang.setCDateiname(versandanhangDto.getCDateiname());
			versandanhang.setOInhalt(versandanhangDto.getOInhalt());
			versandanhang.setVersandauftragIId(versandanhangDto.getVersandauftragIId());
			em.persist(versandanhang);
			em.flush();
			// Auch in JCR speichern da spaeter wieder geloescht wird
			// TODO UNCOMMENT AFTER VERSANDDIENST VERSION X.X.X IS INSTALLED
			getJCRDocFac().saveVersandanhangAsDocument(versandanhangDto, false, theClientDto);
			return versandanhangDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (Throwable t) {
			myLogger.error("saving anhang", t);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception());
		}
	}

	private VersandanhangDto createVersandanhangImpl(VersandanhangDto versandanhangDto, TheClientDto theClientDto) {
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_VERSANDANHANG);
		versandanhangDto.setIId(iId);
		try {
			Versandanhang versandanhang = new Versandanhang(versandanhangDto.getIId());
			versandanhang.setCDateiname(versandanhangDto.getCDateiname());
			versandanhang.setOInhalt(versandanhangDto.getOInhalt());
			versandanhang.setVersandauftragIId(versandanhangDto.getVersandauftragIId());
			em.persist(versandanhang);
			em.flush();
			return versandanhangDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception());
		}
	}

	public void createVersandanhaenge(List<VersandanhangDto> alAnhaenge, TheClientDto theClientDto) {
		for (int i = 0; i < alAnhaenge.size(); i++) {
			createVersandanhangImpl(alAnhaenge.get(i), theClientDto);
		}
		try {
			getJCRDocFac().saveVersandanhangsAsDocument(alAnhaenge, false, theClientDto);
		} catch (Throwable t) {
			myLogger.error("JCR Throwable", t);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception());
		}
	}

	public void createVersandanhaenge0(List<VersandanhangDto> alAnhaenge, TheClientDto theClientDto) {

		for (int i = 0; i < alAnhaenge.size(); i++) {
			createVersandanhang(alAnhaenge.get(i), theClientDto);
		}
	}

	@Override
	public MailTestMessageResult testMailConfiguration(MailTestMessage testMessage, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		String mandantCnr = theClientDto.getMandant();
		LPMailDto mailDto = new LPMailDto();
		mailDto.setFax(false);
		mailDto.setSmtpServer(paramVersand(ParameterFac.PARAMETER_SMTPSERVER, mandantCnr));
		String smtpBenutzer = paramVersand(ParameterFac.PARAMETER_SMTPSERVER_BENUTZER, mandantCnr);
		if (!Helper.isStringEmpty(smtpBenutzer)) {
			mailDto.setSmtpBenutzer(smtpBenutzer);
			mailDto.setSmtpKennwort(paramVersand(ParameterFac.PARAMETER_SMTPSERVER_KENNWORT, mandantCnr));
		}

		String imapServer = paramVersand(ParameterFac.PARAMETER_IMAPSERVER, mandantCnr);
		if (!Helper.isStringEmpty(imapServer)) {
			mailDto.setImapServer(imapServer);
			mailDto.setImapAdmin(paramVersand(ParameterFac.PARAMETER_IMAPSERVER_ADMIN, mandantCnr));
			mailDto.setImapAdminKennwort(paramVersand(ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT, mandantCnr));
			mailDto.setSentFolder(paramVersand(ParameterFac.PARAMETER_IMAPSERVER_SENTFOLDER, mandantCnr));
			if (testMessage.getPersonalIIdFrom() != null) {
				Personal personal = em.find(Personal.class, testMessage.getPersonalIIdFrom());
				if (personal != null && !Helper.isStringEmpty(personal.getCImapbenutzer())) {
					mailDto.setImapAdmin(personal.getCImapbenutzer());
					mailDto.setImapAdminKennwort(personal.getCImapkennwort());
				}
			}
		}

		testMessage.setMailDto(mailDto);

		return mailProcessor.testMailConfiguration(testMessage, theClientDto);
	}

	private String paramVersand(String param, String mandantCnr) throws EJBExceptionLP, RemoteException {
		return getParameterFac()
				.parametermandantFindByPrimaryKey(param, ParameterFac.KATEGORIE_VERSANDAUFTRAG, mandantCnr).getCWert();
	}

	public Versandauftrag getVersandauftrag(Integer IId) {
		return em.find(Versandauftrag.class, IId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean setJobid(Integer versandauftragIId, String jobId) {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null)
			return false;

		versandauftrag.setCJobid(jobId);
		versandauftrag = em.merge(versandauftrag);
		em.flush();
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean setStatus(Integer versandauftragIId, String statusCNr, String cStatustext, Timestamp tSendezeitpunkt,
			byte[] oMessage, boolean clearInhalt) {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, versandauftragIId);
		if (versandauftrag == null)
			return false;

		if (statusCNr != null)
			versandauftrag.setStatusCNr(statusCNr);

		if (cStatustext != null)
			versandauftrag.setCStatustext(cStatustext);

		if (tSendezeitpunkt != null)
			versandauftrag.setTSendezeitpunkt(tSendezeitpunkt);

		if (oMessage != null)
			versandauftrag.setOMessage(oMessage);

		if (clearInhalt)
			versandauftrag.setOInhalt(null);

		versandauftrag = em.merge(versandauftrag);
		em.flush();
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean sendMail(VersandauftragDto versandauftragDto, LPMailDto lpMailDto) {
		String smtpServer = lpMailDto.getSmtpServer();
		String smtpBenutzer = lpMailDto.getSmtpBenutzer();
		String smtpKennwort = lpMailDto.getSmtpKennwort();
		boolean success = false;
		if (smtpServer != null && smtpServer.length() > 0) {
			// LPMail mail = new LPMail(smtpServer);
			LPMail mail = new LPMail(getMailSession(lpMailDto.getTheClientDto()));
			Message message;
			List<String> anhaenge = new ArrayList<String>();
			try {
				File f = null;
				if (versandauftragDto.getOInhalt() != null && versandauftragDto.getOInhalt().length > 0) {
					try {
						f = HelperServer.bytesToFile(versandauftragDto.getOInhalt(), versandauftragDto.getCDateiname());
					} catch (IOException e) {
						myLogger.warn(e.getMessage());
					}
					if (f != null)
						anhaenge.add(f.getPath());
				}

				String cEmpfaenger = null;
				String cAbsender;
				String ccEmpfaenger = null;
				String cBetreff = versandauftragDto.getCBetreff();
				String bccEmpfaenger = null;
				if (lpMailDto.isFax()) {
					switch (lpMailDto.getFaxAnbindung()) {
					case LPMail.FAXANBINDUNG_DOMAIN:
						cEmpfaenger = HelperServer.formatFaxnummerFuerMail(versandauftragDto.getCEmpfaenger(),
								lpMailDto.getFaxDomain());
						break;
					case LPMail.FAXANBINDUNG_XPIRIO:
						cEmpfaenger = HelperServer.formatFaxnummerFuerMail("send", lpMailDto.getFaxDomain());
						cBetreff = lpMailDto.getXpirioKennwort() + versandauftragDto.getCEmpfaenger().replace(" ", "")
								+ ";" + cBetreff;
						break;
					}
					if (versandauftragDto.getCAbsenderadresse() == null)
						cAbsender = lpMailDto.getMailAdmin();
					else
						cAbsender = versandauftragDto.getCAbsenderadresse();
				} else {
					cEmpfaenger = versandauftragDto.getCEmpfaenger();
					cAbsender = versandauftragDto.getCAbsenderadresse();
					ccEmpfaenger = versandauftragDto.getCCcempfaenger();
					bccEmpfaenger = versandauftragDto.getCBccempfaenger();
					// zusaetzliche Anhaenge
					Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
					query.setParameter(1, versandauftragDto.getIId());
					@SuppressWarnings("unchecked")
					List<Versandanhang> versandanhaenge = (List<Versandanhang>) query.getResultList();
					Iterator<Versandanhang> it = versandanhaenge.iterator();
					while (it.hasNext()) {
						Versandanhang anhang = it.next();
						if (anhang.getOInhalt() != null && anhang.getOInhalt().length > 0) {
							f = null;
							try {
								f = HelperServer.bytesToFile(anhang.getOInhalt(), anhang.getCDateiname());
							} catch (IOException e) {
								myLogger.warn(e.getMessage(), e);
							}
							if (f != null)
								anhaenge.add(f.getPath());
						} else {
							// TODO: ghp, warum soll man so einen Versandanhang ueberhaupt abspeichern
							// koennen?
							myLogger.warn("Der Versandanhang (id=" + anhang.getIId() + ") fuer den Versandauftrag (id="
									+ versandauftragDto.getIId()
									+ ") hat keinen Inhalt (null bzw. 0 Laenge)! Ignoriert.");
						}
					}
				}

				message = mail.createMail(cBetreff, versandauftragDto.getCText(), null, cEmpfaenger, cAbsender,
						ccEmpfaenger, bccEmpfaenger, anhaenge);
				if (Helper.short2boolean(versandauftragDto.getBEmpfangsbestaetigung()) == true) {
					if (versandauftragDto.getCAbsenderadresse() != null) {
						message.addHeader("Disposition-Notification-To", versandauftragDto.getCAbsenderadresse());
						message.addHeader("Return-Receipt-To", versandauftragDto.getCAbsenderadresse());
					}
				}
				mail.send(smtpServer, smtpBenutzer, smtpKennwort, message);
				myLogger.info("OK " + versandauftragDto.toLogString());
				getVersandFac().anhaengeLoeschen(versandauftragDto.getIId());

				Timestamp tSendezeitpunkt = new Timestamp(System.currentTimeMillis());
				String statusCNr = null;
				String cStatustext = "Nachricht versandt.";
				byte[] oMessage = null;
				if (lpMailDto.getImapServer() != null && lpMailDto.getImapServer().length() > 0) {
					statusCNr = VersandFac.STATUS_TEILERLEDIGT;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						message.writeTo(baos);
						baos.close();
						oMessage = baos.toByteArray();
					} catch (IOException e) {
						e.printStackTrace();
						myLogger.error("Create message for imap " + e.getMessage());
						cStatustext = cStatustext + " " + e.getMessage();
						if (cStatustext.length() > 1000)
							cStatustext = cStatustext.substring(0, 1000);
					}
				} else {
					statusCNr = VersandFac.STATUS_ERLEDIGT;
				}
				success = getVersandFac().setStatus(versandauftragDto.getIId(), statusCNr, cStatustext, tSendezeitpunkt,
						oMessage, true);

			} catch (MessagingException e) {
				myLogger.error("FAIL " + versandauftragDto.toLogString());
				myLogger.error(e.getMessage(), e);
				getVersandFac().setStatus(versandauftragDto.getIId(), VersandFac.STATUS_FEHLGESCHLAGEN,
						(e.getMessage().length() > 1000 ? e.getMessage().substring(0, 1000) : e.getMessage()), null,
						null, false);
			} catch (Exception e1) {
				myLogger.error("Sendmail: " + e1.getMessage());
				// TODO: zur weiteren Analyse vorerst als Fehler, eventuell umbauen auf
				// wiederholen?
				getVersandFac().setStatus(versandauftragDto.getIId(), VersandFac.STATUS_FEHLGESCHLAGEN,
						(e1.getMessage().length() > 1000 ? e1.getMessage().substring(0, 1000) : e1.getMessage()), null,
						null, false);
			}
		}
		return success;
	}

	private javax.mail.Session getMailSession(TheClientDto theClientDto) {
		boolean useDbPropertiesSession = MailServiceParameterSource.DB
				.equals(getParameterFac().getMailServiceParameter(theClientDto.getMandant()));

		return useDbPropertiesSession ? javax.mail.Session.getInstance(getMailPropertiesCWertNotNull(theClientDto))
				: mailSession;
	}

	private Properties getMailPropertiesCWertNotNull(TheClientDto theClientDto) {
		List<MailProperty> mailProps = MailPropertyQuery.listByMandantCWertNotNull(em, theClientDto.getMandant());
		Properties properties = new Properties();
		for (MailProperty entity : mailProps) {
			properties.setProperty(entity.getPk().getCNr(), entity.getCWert());
		}
		return properties;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void anhaengeLoeschen(Integer versandauftragIId) {
		Query query = em.createNamedQuery("VersandanhangfindByVersandauftragIID");
		query.setParameter(1, versandauftragIId);
		@SuppressWarnings("unchecked")
		List<Versandanhang> versandanhaenge = (List<Versandanhang>) query.getResultList();
		Iterator<Versandanhang> it = versandanhaenge.iterator();
		while (it.hasNext()) {
			Versandanhang anhang = it.next();
			anhang.setOInhalt(null);
			em.merge(anhang);
		}
		em.flush();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void ablageIMAP(Integer iid, String cMandant) {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, iid);
		boolean success = false;
		if (versandauftrag == null) {
			myLogger.error("Versandauftrag em.find IId=" + iid + " = null!");
			return;
		}

		if (!versandauftrag.getStatusCNr().equals(VersandFac.STATUS_TEILERLEDIGT)) {
			myLogger.error("Versandauftrag nicht im STATUS_TEILERLEDIGT! IId=" + iid);
			return;
		}

		LPMailDto lpMailDto = (LPMailDto) HelperServer.getObjectFromByteArray(versandauftrag.getOVersandinfo());
		if (lpMailDto == null) {
			myLogger.error("FAIL iid=" + iid + " LPMailDto = null");
			String cStatustext = getStatustextImap(versandauftrag, " IMAP Ablage fehlgeschlagen! (LPMailDto = null)");
			success = getVersandFac().setStatus(versandauftrag.getIId(), VersandFac.STATUS_ERLEDIGT, cStatustext, null,
					null, false);
			return;
		}

		if (lpMailDto.getTheClientDto().getMandant().equals(cMandant)) {
			// ist Mandant des Timers -> verarbeiten

			// als erstes den Status anpassen bevor die Ablage erfolgt, da diese laenger
			// dauern kann, besonders im Fehlerfall
			success = getVersandFac().setStatus(versandauftrag.getIId(), VersandFac.STATUS_ERLEDIGT, null, null, null,
					false);
			// danach unbedingt neu lesen!
			versandauftrag = em.find(Versandauftrag.class, iid);
			if (versandauftrag == null || !versandauftrag.getStatusCNr().equals(VersandFac.STATUS_ERLEDIGT)) {
				myLogger.error("Versandauftrag Statuswechsel fehlgeschlagen! IId=" + iid);
				return;
			}

			javax.mail.Session session = getMailSession(lpMailDto.getTheClientDto());
			LPMail mail = new LPMail(session);
			ByteArrayInputStream bai = new ByteArrayInputStream(versandauftrag.getOMessage());
			Message message = null;
			String cStatustext = null;
			boolean clearInhalt = false;
			try {
				message = new MimeMessage(session, bai);
			} catch (MessagingException e1) {
				myLogger.error("FAIL Deserialize Message " + e1.getMessage());
				cStatustext = getStatustextImap(versandauftrag,
						" IMAP Ablage fehlgeschlagen! (FAIL Deserialize Message)");
			}
			if (null == message) {
				myLogger.error("FAIL iid=" + iid + " Message = null");
				cStatustext = getStatustextImap(versandauftrag, " IMAP Ablage fehlgeschlagen! (Message = null)");
			} else {
				String imapServer = lpMailDto.getImapServer();
				if (imapServer != null) {
					// Ablegen in den versandten Mails
					if (imapServer.length() > 0) {
						String imapBenutzer = "";
						try {
							if (lpMailDto.getImapBenutzer() != null) {
								imapBenutzer = lpMailDto.getImapBenutzer();
								mail.store(imapServer, imapBenutzer, lpMailDto.getImapBenutzerKennwort(),
										lpMailDto.getSentFolder(), message);
								clearInhalt = true;
							} else {
								if (lpMailDto.getImapAdmin() != null) {
									imapBenutzer = lpMailDto.getImapAdmin();
									mail.store(imapServer, imapBenutzer, lpMailDto.getImapAdminKennwort(),
											lpMailDto.getSentFolder(), message);
									clearInhalt = true;
								} else {
									myLogger.warn("IMAP Kein Benutzer definiert.");
									cStatustext = getStatustextImap(versandauftrag,
											"IMAP Ablage fehlgeschlagen! (Kein Benutzer definiert)");
								}

							}
						} catch (MessagingException e) {
							myLogger.error("IMAP FAIL USER:" + imapBenutzer + " " + versandauftrag.toLogString());
							myLogger.error(e.getMessage(), e);
							cStatustext = getStatustextImap(versandauftrag,
									" IMAP Ablage fehlgeschlagen! " + e.getMessage());
						}
					}
				}
			}
			success = getVersandFac().setStatus(versandauftrag.getIId(), VersandFac.STATUS_ERLEDIGT, cStatustext, null,
					null, clearInhalt);
		}
	}

	private String getStatustextImap(Versandauftrag versandauftrag, String err) {
		String status = versandauftrag.getCStatustext();
		if (!status.endsWith(err)) {
			if (status.length() > (1000 - err.length())) {
				status = status.substring(0, (1000 - err.length())) + err;
			} else {
				status = status + err;
			}
		}
		return status;
	}

	@Asynchronous
	public void performJobSmtp(TheClientDto theClientDto) {
		VersandauftragDto[] auftraege = getVersandFac().versandauftragFindOffen(10);
		myLogger.info("Offene Auftraege: " + auftraege.length);
		LPMailDto lpMailDto = null;
		boolean success = false;
		for (int i = 0; i < auftraege.length; i++) {
			// versenden
			Versandauftrag versandauftrag = em.find(Versandauftrag.class, auftraege[i].getIId());
			if (versandauftrag != null && versandauftrag.getStatusCNr() == null && versandauftrag.getCJobid() == null) {
				if (versandauftrag.getOVersandinfo() != null) {
					lpMailDto = (LPMailDto) HelperServer.getObjectFromByteArray(versandauftrag.getOVersandinfo());
				}
				if ((lpMailDto != null) && lpMailDto.getTheClientDto().getMandant().equals(theClientDto.getMandant())) {

					String jobId = UUID.randomUUID().toString();
					try {
						success = getVersandFac().setJobid(versandauftrag.getIId(), jobId);
					} catch (PersistenceException e) {
						myLogger.error("Set jobId: " + e.getMessage());
					}

					if (versandauftrag != null && success) {
						VersandauftragDto versandauftragDto = versandauftragFindByPrimaryKey(versandauftrag.getIId());
						success = getVersandFac().sendMail(versandauftragDto, lpMailDto);
					}

					// PJ20063
					if (!success) {
						getNachrichtenFac().nachrichtErstellen(NachrichtenFac.Art.EMAIL_VERSAND_FEHLGESCHLAGEN,
								"Email-Versand mit dem Betreff -" + versandauftrag.getCBetreff() + "-"
										+ " fehlgeschlagen",
								versandauftrag.getCStatustext(), null, null, lpMailDto.getTheClientDto());
					}
				}
			}
		}
	}

	@Asynchronous
	public void performJobImap(TheClientDto theClientDto) {
		// Anmerkung: der Versandauftrag hat derzeit kein Feld Mandant -> die 10
		// Auftraege koennen von verschiedenen Mandanten sein
		// und es werden daher eventuell weniger Auftraege pro Timer verarbeitet!
		VersandauftragDto[] auftraege = getVersandFac().versandauftragFindAblage(10);
		myLogger.info("Offene Auftraege fuer IMAPablage: " + auftraege.length);
		for (int i = 0; i < auftraege.length; i++) {
			// ablegen
			if (auftraege[i].getStatusCNr().equals(VersandFac.STATUS_TEILERLEDIGT))
				getVersandFac().ablageIMAP(auftraege[i].getIId(), theClientDto.getMandant());
		}
	}

}
