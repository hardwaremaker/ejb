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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import com.lp.server.personal.ejb.Personal;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.ejb.Versandanhang;
import com.lp.server.system.ejb.Versandauftrag;
import com.lp.server.system.ejb.Versandstatus;
import com.lp.server.system.fastlanereader.generated.FLRVersandauftrag;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.mail.service.LPMail;
import com.lp.server.system.mail.service.LPMailDto;
import com.lp.server.system.mail.service.MailProcessorFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MailtextDto;
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
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.report.PersonRpt;

@Stateless
public class VersandFacBean extends Facade implements VersandFac {
	@PersistenceContext
	private EntityManager em;

	public VersandauftragDto createVersandauftrag(
			VersandauftragDto versandauftragDto,
			boolean bDokumenteMitanhaengen, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragDto, theClientDto.getIDUser());
		// begin
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_VERSANDAUFTRAG);
		versandauftragDto.setIId(iId);
		// wenn kein sendezeitpunkt angegeben, dann jetzt
		try {
			if (versandauftragDto.getTSendezeitpunktwunsch() == null) {
				GregorianCalendar gc = new GregorianCalendar();
				ParametermandantDto pm = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				int iDelay = Integer.parseInt(pm.getCWert());
				gc.set(Calendar.MINUTE, gc.get(Calendar.MINUTE) + iDelay);
				versandauftragDto.setTSendezeitpunktwunsch(new Timestamp(gc
						.getTimeInMillis()));
			}
			// Default keine Empfangsbestaetigung
			if (versandauftragDto.getBEmpfangsbestaetigung() == null) {
				versandauftragDto.setBEmpfangsbestaetigung(Helper
						.boolean2Short(false));
			}
			// ein neuer ist immer auf angelegt
			versandauftragDto.setPersonalIId(theClientDto.getIDPersonal());
			Versandauftrag versandauftrag = new Versandauftrag(
					versandauftragDto.getIId(),
					versandauftragDto.getCEmpfaenger(),
					versandauftragDto.getTSendezeitpunktwunsch(),
					versandauftragDto.getPersonalIId(),
					versandauftragDto.getPartnerIIdEmpfaenger(),
					versandauftragDto.getPartnerIIdSender(),
					versandauftragDto.getOInhalt(),
					versandauftragDto.getBEmpfangsbestaetigung());

			versandauftragDto.setTAnlegen(versandauftrag.getTAnlegen());
			setVersandauftragFromVersandauftragDto(versandauftrag,
					versandauftragDto);
			em.persist(versandauftrag);
			em.flush();

			// PJ14774

			if (bDokumenteMitanhaengen == true
					&& versandauftragDto.getBelegartCNr() != null
					&& versandauftragDto.getIIdBeleg() != null) {

				if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)) {
					LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
							.getLieferscheinPositionenByLieferschein(
									versandauftragDto.getIIdBeleg(),
									theClientDto);

					for (int i = 0; i < lsPos.length; i++) {

						if (lsPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {

							ArrayList<WarenzugangsreferenzDto> dtos = getLagerFac()
									.getWareneingangsreferenz(
											LocaleFac.BELEGART_LIEFERSCHEIN,
											lsPos[i].getIId(),
											lsPos[i].getSeriennrChargennrMitMenge(),
											true, theClientDto);

							for (int z = 0; z < dtos.size(); z++) {
								if (dtos.get(z).getJcrdocs() != null) {
									ArrayList<JCRDocDto> anhaenge = dtos.get(z)
											.getJcrdocs();

									// lt Hr. Brennecke immer nur den letzten
									// verwenden
									if (anhaenge.size() > 0) {
										JCRDocDto jcrDoc = (JCRDocDto) anhaenge
												.get(0);
										VersandanhangDto versandanhangDto = new VersandanhangDto();
										versandanhangDto
												.setVersandauftragIId(versandauftragDto
														.getIId());
										versandanhangDto.setCDateiname(jcrDoc
												.getsFilename());

										versandanhangDto.setOInhalt(jcrDoc
												.getbData());
										createVersandanhang(versandanhangDto,
												theClientDto);
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
				if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_LIEFERSCHEIN)) {
					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(
									versandauftragDto.getIIdBeleg(),
									theClientDto);

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(
							lsDto.getKundeIIdLieferadresse(), theClientDto);

					LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
							.getLieferscheinPositionenByLieferschein(
									versandauftragDto.getIIdBeleg(),
									theClientDto);

					for (int i = 0; i < lsPos.length; i++) {

						if (lsPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {

							if (!hmArtikelAnhaenge.containsKey(lsPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(lsPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																lsPos[i].getArtikelIId(),
																LocaleFac.BELEGART_LIEFERSCHEIN,
																kDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}

						}
					}

				} else if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_ANGEBOT)) {
					AngebotDto belegDto = getAngebotFac()
							.angebotFindByPrimaryKey(
									versandauftragDto.getIIdBeleg(),
									theClientDto);

					KundeDto kDto = getKundeFac()
							.kundeFindByPrimaryKey(
									belegDto.getKundeIIdAngebotsadresse(),
									theClientDto);

					AngebotpositionDto[] belegposPos = getAngebotpositionFac()
							.angebotpositionFindByAngebotIId(belegDto.getIId(),
									theClientDto);

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(belegposPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																belegposPos[i]
																		.getArtikelIId(),
																LocaleFac.BELEGART_ANGEBOT,
																kDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_AUFTRAG)) {
					AuftragDto belegDto = getAuftragFac()
							.auftragFindByPrimaryKey(
									versandauftragDto.getIIdBeleg());

					KundeDto kDto = getKundeFac()
							.kundeFindByPrimaryKey(
									belegDto.getKundeIIdAuftragsadresse(),
									theClientDto);

					AuftragpositionDto[] belegposPos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(belegDto.getIId());

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(belegposPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																belegposPos[i]
																		.getArtikelIId(),
																LocaleFac.BELEGART_AUFTRAG,
																kDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_RECHNUNG)
						|| versandauftragDto.getBelegartCNr().equals(
								LocaleFac.BELEGART_GUTSCHRIFT)) {
					RechnungDto belegDto = getRechnungFac()
							.rechnungFindByPrimaryKey(
									versandauftragDto.getIIdBeleg());

					KundeDto kDto = getKundeFac().kundeFindByPrimaryKey(
							belegDto.getKundeIId(), theClientDto);

					RechnungPositionDto[] belegposPos = getRechnungFac()
							.rechnungPositionFindByRechnungIId(
									belegDto.getIId());

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(belegposPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																belegposPos[i]
																		.getArtikelIId(),
																LocaleFac.BELEGART_RECHNUNG,
																kDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_ANFRAGE)) {
					AnfrageDto belegDto = getAnfrageFac()
							.anfrageFindByPrimaryKey(
									versandauftragDto.getIIdBeleg(),
									theClientDto);

					LieferantDto lDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									belegDto.getLieferantIIdAnfrageadresse(),
									theClientDto);

					AnfragepositionDto[] belegposPos = getAnfragepositionFac()
							.anfragepositionFindByAnfrage(belegDto.getIId(),
									theClientDto);

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(belegposPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																belegposPos[i]
																		.getArtikelIId(),
																LocaleFac.BELEGART_ANFRAGE,
																lDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}
						}

					}
				} else if (versandauftragDto.getBelegartCNr().equals(
						LocaleFac.BELEGART_BESTELLUNG)) {
					BestellungDto belegDto = getBestellungFac()
							.bestellungFindByPrimaryKey(
									versandauftragDto.getIIdBeleg());

					LieferantDto lDto = getLieferantFac()
							.lieferantFindByPrimaryKey(
									belegDto.getLieferantIIdBestelladresse(),
									theClientDto);

					BestellpositionDto[] belegposPos = getBestellpositionFac()
							.bestellpositionFindByBestellung(belegDto.getIId(),
									theClientDto);

					for (int i = 0; i < belegposPos.length; i++) {

						if (belegposPos[i].getPositionsartCNr().equals(
								LocaleFac.POSITIONSART_IDENT)) {
							if (!hmArtikelAnhaenge.containsKey(belegposPos[i]
									.getArtikelIId())) {
								hmArtikelAnhaenge
										.put(belegposPos[i].getArtikelIId(),
												getArtikelkommentarFac()
														.artikelkommentardruckFindByArtikelIIdBelegartCNrAnhaenge(
																belegposPos[i]
																		.getArtikelIId(),
																LocaleFac.BELEGART_BESTELLUNG,
																lDto.getPartnerDto()
																		.getLocaleCNrKommunikation(),
																theClientDto));
							}
						}

					}
				}

				Iterator<Integer> it = hmArtikelAnhaenge.keySet().iterator();
				while (it.hasNext()) {
					Integer artikelId = (Integer) it.next();
					ArtikelkommentarDto[] anhangeArtikel = hmArtikelAnhaenge
							.get(artikelId);

					for (int i = 0; i < anhangeArtikel.length; i++) {
						if (anhangeArtikel[i].getArtikelkommentarsprDto() != null
								&& anhangeArtikel[i]
										.getArtikelkommentarsprDto()
										.getCDateiname() != null) {
							VersandanhangDto versandanhangDto = new VersandanhangDto();
							versandanhangDto
									.setVersandauftragIId(versandauftragDto
											.getIId());
							versandanhangDto.setCDateiname(anhangeArtikel[i]
									.getArtikelkommentarsprDto()
									.getCDateiname());

							versandanhangDto.setOInhalt(anhangeArtikel[i]
									.getArtikelkommentarsprDto().getOMedia());
							createVersandanhang(versandanhangDto, theClientDto);
						}
					}

				}
			}

			// Anlegen des Versandauftrags in JCR
			getJCRDocFac().saveVersandauftragAsDocument(versandauftragDto,
					false, theClientDto);
			doSendMailOrFax(versandauftragDto, theClientDto);
			return versandauftragDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	private void doSendMailOrFax(VersandauftragDto versandauftragDto,
			TheClientDto theClientDto) {
		if (versandauftragDto.getCEmpfaenger().contains("@")) {
			/*
			 * //sofort senden versandauftragDto =
			 * sendMailSofort(versandauftragDto, theClientDto);
			 * updateVersandauftrag(versandauftragDto, theClientDto);
			 */

			// ueber Timer senden
			sendMail(versandauftragDto, false, theClientDto);
		} else {
			// ist ein Fax
			String faxDomain = null;
			String mailAdmin = null;
			try {
				ParametermandantDto pm = getParameterFac()
						.parametermandantFindByPrimaryKey(
								ParameterFac.PARAMETER_SMTPSERVER_FAXDOMAIN,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG,
								theClientDto.getMandant());
				faxDomain = getParameterFac().parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_SMTPSERVER_FAXDOMAIN,
						ParameterFac.KATEGORIE_VERSANDAUFTRAG,
						theClientDto.getMandant()).getCWert();
				mailAdmin = getParameterFac().parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_MAILADRESSE_ADMIN,
						ParameterFac.KATEGORIE_VERSANDAUFTRAG,
						theClientDto.getMandant()).getCWert();
			} catch (EJBExceptionLP e) {
				myLogger.warn(e.getMessage());
			} catch (RemoteException e) {
				myLogger.warn(e.getMessage());
			}
			if (faxDomain != null)
				if (faxDomain.length() > 0)
					sendFaxAsMail(versandauftragDto, theClientDto, faxDomain,
							mailAdmin);
		}
	}

	private void sendFaxAsMail(VersandauftragDto versandauftragDto,
			TheClientDto theClientDto, String faxDomain, String mailAdmin) {
		versandauftragDto.setCEmpfaenger(HelperServer.formatFaxnummerFuerMail(
				versandauftragDto.getCEmpfaenger(), faxDomain));

		if (versandauftragDto.getCAbsenderadresse() == null)
			if (mailAdmin == null)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						"MailAdmin nicht angelegt");
			else
				versandauftragDto.setCAbsenderadresse(mailAdmin);

		sendMail(versandauftragDto, true, theClientDto);
	}

	@EJB
	private MailProcessorFac mailProcessor;

	private void sendMail(VersandauftragDto versandauftragDto, boolean isFax,
			TheClientDto theClientDto) {
		LPMailDto lpMailDto = new LPMailDto();
		lpMailDto.setFax(isFax);

		String smtpServer = null;
		String smtpBenutzer = null;
		String smtpKennwort = null;
		try {
			smtpServer = getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_SMTPSERVER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					theClientDto.getMandant()).getCWert();
			smtpBenutzer = getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_SMTPSERVER_BENUTZER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					theClientDto.getMandant()).getCWert();
			if (smtpBenutzer.length() > 0)
				smtpKennwort = getParameterFac()
						.parametermandantFindByPrimaryKey(
								ParameterFac.PARAMETER_SMTPSERVER_KENNWORT,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG,
								theClientDto.getMandant()).getCWert();
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
					imapServer = getParameterFac()
							.parametermandantFindByPrimaryKey(
									ParameterFac.PARAMETER_IMAPSERVER,
									ParameterFac.KATEGORIE_VERSANDAUFTRAG,
									theClientDto.getMandant()).getCWert();
				} catch (EJBExceptionLP e) {
					myLogger.warn(e.getMessage());
				} catch (RemoteException e) {
					myLogger.warn(e.getMessage());
				}
				lpMailDto.setImapServer(imapServer);
				if (imapServer.length() > 0) {
					Personal personal = em.find(Personal.class,
							versandauftragDto.getPersonalIId());
					if (personal != null) {
						lpMailDto.setImapBenutzer(personal.getCImapbenutzer());
						lpMailDto.setImapBenutzerKennwort(personal
								.getCImapkennwort());
					}
					String imapAdminKennwort = null;
					String imapAdmin = "";
					try {
						imapAdmin = getParameterFac()
								.parametermandantFindByPrimaryKey(
										ParameterFac.PARAMETER_IMAPSERVER_ADMIN,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG,
										theClientDto.getMandant()).getCWert();
					} catch (EJBExceptionLP e) {
						myLogger.warn(e.getMessage());
					} catch (RemoteException e) {
						myLogger.warn(e.getMessage());
					}
					lpMailDto.setImapAdmin(imapAdmin);
					if (imapAdmin.length() > 0) {
						try {
							imapAdminKennwort = getParameterFac()
									.parametermandantFindByPrimaryKey(
											ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT,
											ParameterFac.KATEGORIE_VERSANDAUFTRAG,
											theClientDto.getMandant())
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
								.parametermandantFindByPrimaryKey(
										ParameterFac.PARAMETER_IMAPSERVER_SENTFOLDER,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG,
										theClientDto.getMandant()).getCWert();
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
										.parametermandantFindByPrimaryKey(
												ParameterFac.PARAMETER_SMTPSERVER_FAXDOMAIN,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG,
												theClientDto.getMandant())
										.getCWert());
						lpMailDto
								.setMailAdmin(getParameterFac()
										.parametermandantFindByPrimaryKey(
												ParameterFac.PARAMETER_MAILADRESSE_ADMIN,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG,
												theClientDto.getMandant())
										.getCWert());
						lpMailDto
								.setFaxAnbindung(((Integer) getParameterFac()
										.parametermandantFindByPrimaryKey(
												ParameterFac.PARAMETER_SMTPSERVER_FAXANBINDUNG,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG,
												theClientDto.getMandant())
										.getCWertAsObject()).intValue());
						if (lpMailDto.getFaxAnbindung() == LPMail.FAXANBINDUNG_XPIRIO) {
							lpMailDto
									.setXpirioKennwort(getParameterFac()
											.parametermandantFindByPrimaryKey(
													ParameterFac.PARAMETER_SMTPSERVER_XPIRIO_KENNWORT,
													ParameterFac.KATEGORIE_VERSANDAUFTRAG,
													theClientDto.getMandant())
											.getCWert());
						}
					} catch (EJBExceptionLP e) {
						myLogger.warn(e.getMessage());
					} catch (RemoteException e) {
						myLogger.warn(e.getMessage());
					}
				}
				lpMailDto.setTSendezeitpunktwunsch(versandauftragDto
						.getTSendezeitpunktwunsch());
				@SuppressWarnings("unused")
				javax.ejb.TimerHandle handle = mailProcessor.addMail(lpMailDto);
			}
		}

	}

	// TODO: Anhanege bei sendMailSofort
	private VersandauftragDto sendMailSofort(
			VersandauftragDto versandauftragDto, TheClientDto theClientDto) {
		String smtpServer = null;
		String smtpBenutzer = null;
		String smtpKennwort = null;
		try {
			smtpServer = getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_SMTPSERVER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					theClientDto.getMandant()).getCWert();
			smtpBenutzer = getParameterFac().parametermandantFindByPrimaryKey(
					ParameterFac.PARAMETER_SMTPSERVER_BENUTZER,
					ParameterFac.KATEGORIE_VERSANDAUFTRAG,
					theClientDto.getMandant()).getCWert();
			if (smtpBenutzer.length() > 0)
				smtpKennwort = getParameterFac()
						.parametermandantFindByPrimaryKey(
								ParameterFac.PARAMETER_SMTPSERVER_KENNWORT,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG,
								theClientDto.getMandant()).getCWert();
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
					List<String> anhaenge = new ArrayList();
					if (f != null)
						anhaenge.add(f.getPath());
					message = mail.createMail(versandauftragDto.getCBetreff(),
							versandauftragDto.getCText(), null,
							versandauftragDto.getCEmpfaenger(),
							versandauftragDto.getCAbsenderadresse(),
							versandauftragDto.getCCcempfaenger(), null,
							anhaenge);
					mail.send(smtpServer, smtpBenutzer, smtpKennwort, message);
					myLogger.entry("[Mailversand] "
							+ Helper.formatTime(
									new Timestamp(System.currentTimeMillis()),
									new Locale("deAT"))
							+ versandauftragDto.toLogString());
					versandauftragDto.setStatusCNr(VersandFac.STATUS_ERLEDIGT);
					versandauftragDto.setCStatustext("Nachricht versandt.");
					try {
						String imapServer = getParameterFac()
								.parametermandantFindByPrimaryKey(
										ParameterFac.PARAMETER_IMAPSERVER,
										ParameterFac.KATEGORIE_VERSANDAUFTRAG,
										theClientDto.getMandant()).getCWert();
						String imapAdmin = null;
						String imapAdminKennwort = null;
						if (imapServer.length() > 0) {
							imapAdmin = getParameterFac()
									.parametermandantFindByPrimaryKey(
											ParameterFac.PARAMETER_IMAPSERVER_ADMIN,
											ParameterFac.KATEGORIE_VERSANDAUFTRAG,
											theClientDto.getMandant())
									.getCWert();
							if (imapAdmin.length() > 0)
								imapAdminKennwort = getParameterFac()
										.parametermandantFindByPrimaryKey(
												ParameterFac.PARAMETER_IMAPSERVER_ADMIN_KENNWORT,
												ParameterFac.KATEGORIE_VERSANDAUFTRAG,
												theClientDto.getMandant())
										.getCWert();
						}
						if (imapServer.length() > 0)
							mail.store(imapServer, imapAdmin,
									imapAdminKennwort,
									new LPMailDto().getSentFolder(), message);
					} catch (Exception e) {
						myLogger.warn(e.getMessage());
					}
				} catch (MessagingException e) {
					myLogger.error(e.getMessage());
					versandauftragDto
							.setStatusCNr(VersandFac.STATUS_FEHLGESCHLAGEN);
					versandauftragDto.setCStatustext(e.getMessage());
				}
			}
		}
		return versandauftragDto;
	}

	public void removeVersandauftrag(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Zuerst Timer loeschen
		mailProcessor.killTimer(versandauftragIId);

		// Zuerst Anhaenge loeschen
		Query query = em
				.createNamedQuery("VersandanhangfindByVersandauftragIID");
		query.setParameter(1, versandauftragIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Versandanhang versandanhang = (Versandanhang) iterator.next();
			em.remove(versandanhang);
			em.flush();
		}

		// Dann den Versandauftrag
		Versandauftrag versandauftrag = em.find(Versandauftrag.class,
				versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(versandauftrag);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void storniereVersandauftrag(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class,
				versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		// Zuerst Timer loeschen
		mailProcessor.killTimer(versandauftragIId);

		if (versandauftrag.getStatusCNr() == null) {
			versandauftrag.setStatusCNr(VersandFac.STATUS_STORNIERT);
		} else if (versandauftrag.getStatusCNr().equals(
				VersandFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT,
					new Exception("Versandauftrag iId=" + versandauftragIId
							+ " wurde bereits versandt."));
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_KANN_NICHT_STORNIERT_WERDEN,
					new Exception("Versandauftrag iId=" + versandauftragIId
							+ " kann nicht storniert werden. Status="
							+ versandauftrag.getStatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public VersandauftragDto updateVersandauftrag(
			VersandauftragDto versandauftragDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragDto);
		// begin
		Integer iId = versandauftragDto.getIId();
		try {
			Versandauftrag versandauftrag = em.find(Versandauftrag.class, iId);
			setVersandauftragFromVersandauftragDto(versandauftrag,
					versandauftragDto);
			return versandauftragDto;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	public VersandanhangDto updateVersandanhang(
			VersandanhangDto versandanhangDto) {
		Versandanhang versandanhang = em.find(Versandanhang.class,
				versandanhangDto.getIId());
		setVersandanhangFromVersandanhangDto(versandanhang, versandanhangDto);
		return versandanhangDto;
	}

	public VersandauftragDto versandauftragFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		VersandauftragDto versandauftrag = versandauftragFindByPrimaryKeyOhneExc(iId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return versandauftrag;
	}

	public VersandauftragDto versandauftragFindByPrimaryKeyOhneExc(Integer iId) {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class, iId);
		return versandauftrag == null ? null
				: assembleVersandauftragDto(versandauftrag);
	}

	public VersandanhangDto versandanhangFindByPrimaryKeyOhneExc(Integer iId) {
		Versandanhang versandanhang = em.find(Versandanhang.class, iId);
		return versandanhang == null ? null
				: assembleVersandanhangDto(versandanhang);
	}

	private void setVersandauftragFromVersandauftragDto(
			Versandauftrag versandauftrag, VersandauftragDto versandauftragDto) {
		versandauftrag.setCEmpfaenger(versandauftragDto.getCEmpfaenger());
		versandauftrag.setCCcempfaenger(versandauftragDto.getCCcempfaenger());
		versandauftrag.setCBetreff(versandauftragDto.getCBetreff());
		versandauftrag.setCText(versandauftragDto.getCText());
		versandauftrag.setCAbsenderadresse(versandauftragDto
				.getCAbsenderadresse());
		versandauftrag.setCDateiname(versandauftragDto.getCDateiname());
		versandauftrag.setTSendezeitpunktwunsch(versandauftragDto
				.getTSendezeitpunktwunsch());
		versandauftrag.setTSendezeitpunkt(versandauftragDto
				.getTSendezeitpunkt());
		versandauftrag.setPersonalIId(versandauftragDto.getPersonalIId());
		versandauftrag.setTAnlegen(versandauftragDto.getTAnlegen());
		versandauftrag.setPartnerIIdEmpfaenger(versandauftragDto
				.getPartnerIIdEmpfaenger());
		versandauftrag.setPartnerIIdSender(versandauftragDto
				.getPartnerIIdSender());
		versandauftrag.setBelegartCNr(versandauftragDto.getBelegartCNr());
		versandauftrag.setIBelegIId(versandauftragDto.getIIdBeleg());
		versandauftrag.setBEmpfangsbestaetigung(versandauftragDto
				.getBEmpfangsbestaetigung());
		// READ ONLY
		// versandauftrag.setStatusCNr(versandauftragDto.getStatusCNr());
		versandauftrag.setCStatustext(versandauftragDto.getCStatustext());
		versandauftrag.setOInhalt(versandauftragDto.getOInhalt());
		// em.merge(versandauftrag);
	}

	private VersandauftragDto assembleVersandauftragDto(
			Versandauftrag versandauftrag) {
		return VersandauftragDtoAssembler.createDto(versandauftrag);
	}

	private VersandanhangDto assembleVersandanhangDto(
			Versandanhang versandanhang) {
		return VersandanhangDtoAssembler.createDto(versandanhang);
	}

	private VersandauftragDto[] assembleVersandauftragDtos(
			Collection<?> versandauftrags) {
		List<VersandauftragDto> list = new ArrayList<VersandauftragDto>();
		if (versandauftrags != null) {
			Iterator<?> iterator = versandauftrags.iterator();
			while (iterator.hasNext()) {
				Versandauftrag versandauftrag = (Versandauftrag) iterator
						.next();
				list.add(assembleVersandauftragDto(versandauftrag));
			}
		}
		VersandauftragDto[] returnArray = new VersandauftragDto[list.size()];
		return (VersandauftragDto[]) list.toArray(returnArray);
	}

	public String getDefaultBetreffForBelegEmail(MailtextDto mailtextDto,
			String belegartCNr, Integer iIdBeleg, Locale locSprache,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LpMailText mt = createLpMailText(new LpMailText(), mailtextDto,
				theClientDto);
		if (mt != null) {
			String betreff = mt.transformBetreff(mailtextDto, theClientDto);
			if (betreff != null)
				return betreff;
		}
		if (belegartCNr == null || iIdBeleg == null) {
			return "kein Beleg.pdf";
			/**
			 * @todo PJ 4628
			 */
		} else {
			return getDefaultDateinameForBelegEmail(belegartCNr, iIdBeleg,
					locSprache, theClientDto);
		}
	}

	private LpMailText createLpMailText(LpMailText mt, MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(mailtextDto, "mailtextDto");
		try {
			String sAnrede;
			Locale locDruck;
			if (mailtextDto.getParamLocale() != null) {
				locDruck = mailtextDto.getParamLocale();
			} else {
				locDruck = theClientDto.getLocUi();
			}
			if (mailtextDto.getMailAnprechpartnerIId() != null
					&& mailtextDto.getMailPartnerIId() != null) {
				sAnrede = getPartnerServicesFac()
						.getBriefanredeFuerBeleg(
								mailtextDto.getMailAnprechpartnerIId(),
								mailtextDto.getMailPartnerIId(), locDruck,
								theClientDto);
			} else {
				// neutrale Anrede
				if (mailtextDto.getMailPartnerIId() != null) {
					sAnrede = getBriefanredeNeutralOderPrivatperson(
							mailtextDto.getMailPartnerIId(), locDruck,
							theClientDto);
				} else {
					sAnrede = getTextRespectUISpr(
							"lp.anrede.sehrgeehrtedamenundherren",
							theClientDto.getMandant(), locDruck);
				}

			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_ANREDE_ANSPRECHPARTNER,
					sAnrede);
			mt.addParameter(VersandFac.MAIL_PARAMETER_BELEGNUMMER,
					mailtextDto.getMailBelegnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEZEICHNUNG,
					mailtextDto.getMailBezeichnung());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BELEGDATUM, Helper
					.formatDatum(mailtextDto.getMailBelegdatum(),
							mailtextDto.getParamLocale()));
			mt.addParameter(VersandFac.MAIL_PARAMETER_PROJEKT,
					mailtextDto.getMailProjekt());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER,
					mailtextDto.getMailVertreterAlsString());

			mt.addParameter(VersandFac.MAIL_PARAMETER_FUSSTEXT,
					Helper.removeStyles(mailtextDto.getMailFusstext()));
			mt.addParameter(VersandFac.MAIL_PARAMETER_TEXT,
					mailtextDto.getMailText());
			mt.addParameter(VersandFac.MAIL_PARAMETER_KUNDENBESTELLNUMMER,
					mailtextDto.getKundenbestellnummer());
			mt.addParameter(VersandFac.MAIL_PARAMETER_ABNUMMER,
					mailtextDto.getAbnummer());

			PersonRpt personRpt = null;

			if (mailtextDto.getMailVertreter() != null) {
				personRpt = getPersonalFac().getPersonRpt(
						mailtextDto.getMailVertreter().getIId(), theClientDto);
			} else {
				personRpt = getPersonalFac().getPersonRpt(
						theClientDto.getIDPersonal(), theClientDto);
			}

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_EMAIL,
					personRpt.getSEmail());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_MOBIL,
					personRpt.getSMobil());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_NACHNAME,
					personRpt.getSNachname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONDWFIRMA,
					personRpt.getSTelefonDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONFIRMA,
					personRpt.getSTelefonFirma());
			mt.addParameter(
					VersandFac.MAIL_PARAMETER_VERTRETER_TELEFONFIRMAMITDW,
					personRpt.getSTelefonFirmaMitDurchwahlBearbeiter());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_TITEL,
					personRpt.getSTitel());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_VORNAME,
					personRpt.getSVorname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_NTITEL,
					personRpt.getSNtitel());

			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_DIREKTFAX,
					personRpt.getSDirektfax());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_FAXDWFIRMA,
					personRpt.getSFaxDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_VERTRETER_FAXFIRMAMITDW,
					personRpt.getSFaxFirmaMitDurchwahlBearbeiter());

			PersonalDto personalDto = getPersonalFac()
					.personalFindByPrimaryKeySmall(theClientDto.getIDPersonal());

			mt.addParameter(
					VersandFac.MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSFUNKTION,
					personalDto.getCUnterschriftsfunktion());
			mt.addParameter(
					VersandFac.MAIL_PARAMETER_VERTRETER_UNTERSCHRIFTSTEXT,
					personalDto.getCUnterschriftstext());

			personRpt = getPersonalFac().getPersonRpt(
					theClientDto.getIDPersonal(), theClientDto);

			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_EMAIL,
					personRpt.getSEmail());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_MOBIL,
					personRpt.getSMobil());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_NACHNAME,
					personRpt.getSNachname());
			mt.addParameter(
					VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONDWFIRMA,
					personRpt.getSTelefonDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONFIRMA,
					personRpt.getSTelefonFirma());
			mt.addParameter(
					VersandFac.MAIL_PARAMETER_BEARBEITER_TELEFONFIRMAMITDW,
					personRpt.getSTelefonFirmaMitDurchwahlBearbeiter());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_TITEL,
					personRpt.getSTitel());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_VORNAME,
					personRpt.getSVorname());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_NTITEL,
					personRpt.getSNtitel());

			// PJ14540
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_DIREKTFAX,
					personRpt.getSDirektfax());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_FAXDWFIRMA,
					personRpt.getSFaxDWFirma());
			mt.addParameter(VersandFac.MAIL_PARAMETER_BEARBEITER_FAXFIRMAMITDW,
					personRpt.getSFaxFirmaMitDurchwahlBearbeiter());

			personalDto = getPersonalFac().personalFindByPrimaryKeySmall(
					theClientDto.getIDPersonal());

			mt.addParameter(
					VersandFac.MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSFUNKTION,
					personalDto.getCUnterschriftsfunktion());
			mt.addParameter(
					VersandFac.MAIL_PARAMETER_BEARBEITER_UNTERSCHRIFTSTEXT,
					personalDto.getCUnterschriftstext());

			// PJ 17506
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_LIEFERSCHEIN,
					mailtextDto.getRekla_lieferschein());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_RECHNUNG,
					mailtextDto.getRekla_rechnung());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_KNDREKLANR,
					mailtextDto.getRekla_kndreklanr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_KNDLSNR,
					mailtextDto.getRekla_kndlsnr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_LSNR,
					mailtextDto.getRekla_we_lsnr());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_WEDATUM,
					mailtextDto.getRekla_we_datum());
			mt.addParameter(VersandFac.MAIL_PARAMETER_REKLA_WE_LSDATUM,
					mailtextDto.getRekla_we_lsdatum());

			if (personalDto.getPartnerIIdFirma() != null) {
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(
								personalDto.getPartnerIIdFirma(), theClientDto);

				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME1,
						partnerDto.getCName1nachnamefirmazeile1());
				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME2,
						partnerDto.getCName2vornamefirmazeile2());
				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_NAME3,
						partnerDto.getCName3vorname2abteilung());

				mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_STRASSE,
						partnerDto.getCStrasse());

				if (partnerDto.getLandplzortDto() != null) {
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_LKZ,
							partnerDto.getLandplzortDto().getLandDto()
									.getCLkz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_PLZ,
							partnerDto.getLandplzortDto().getCPlz());
					mt.addParameter(VersandFac.MAIL_PARAMETER_FIRMA_ORT,
							partnerDto.getLandplzortDto().getOrtDto()
									.getCName());
				}
			}
			return mt;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public String getDefaultTextForBelegEmail(MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		LpMailText mt = createLpMailText(new LpMailText(), mailtextDto,
				theClientDto);
		return mt != null ? mt.transformText(mailtextDto, theClientDto) : null;
	}

	public String getDefaultTextForBelegHtmlEmail(MailtextDto mailtextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LpMailText mt = createLpMailText(new LpHtmlMailText(), mailtextDto,
				theClientDto);
		return mt != null ? mt.transformText(mailtextDto, theClientDto) : null;
	}

	public String getVersandstatus(String belegartCNr, Integer i_belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
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
					status = getTextRespectUISpr("lp.system.postausgang",
							mandantCNr, locUI);

				}

				if (flr.getC_empfaenger().contains("@")) {
					status += "/"
							+ getTextRespectUISpr("lp.email", mandantCNr, locUI);
				} else {
					status += "/"
							+ getTextRespectUISpr("lp.fax", mandantCNr, locUI);
				}

				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKeySmall(flr.getPersonal_i_id());
				status += "/"
						+ personalDto.getCKurzzeichen()
						+ "/"
						+ Helper.formatDatumZeit(
								flr.getT_sendezeitpunktwunsch(), locUI);

				return status;
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	public String getFormattedVersandstatus(String belegartCNr,
			Integer i_belegIId, TheClientDto theClientDto)
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
					java.util.Date dSendAndFive = new java.util.Date(
							dSend.getTime() + lfiveminutes);
					int iComp = dNow.compareTo(dSendAndFive);
					if (iComp > 0) {
						// red
						status = "~r";
					} else {
						// black
						status = "~b";
					}
					String sDateFull = Helper.formatDatumZeit(
							flr.getT_sendezeitpunktwunsch(),
							theClientDto.getLocUi());
					String[] sTempstring = sDateFull.split(":");
					String sDate = sTempstring[0] + ":" + sTempstring[1];
					if (flr.getC_empfaenger().contains("@")) {
						status = status
								+ getTextRespectUISpr("kund.sent", mandantCNr,
										locUI) + " " + sDate;
					} else {
						status = status
								+ getTextRespectUISpr("kund.sent.fax",
										mandantCNr, locUI) + " " + sDate;
					}
					return status;
				} else {
					if (flr.getC_empfaenger().contains("@")) {
						return getTextRespectUISpr("lp.versandstatus.mail",
								mandantCNr, locUI) + " " + status;
					} else {
						return getTextRespectUISpr("lp.versandstatus.fax",
								mandantCNr, locUI) + " " + status;
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

	public String getDefaultDateinameForBelegEmail(String belegartCNr,
			Integer iIdBeleg, Locale locSprache, TheClientDto theClientDto)
			throws EJBExceptionLP {

		String sBelegart = null;
		String sBelegnummer = null;
		if (belegartCNr == null || iIdBeleg == null) {
			return "kein Beleg.pdf";
			/**
			 * @todo PJ 4628
			 */
		}
		try {
			sBelegart = getLocaleFac().uebersetzeBelegartOptimal(belegartCNr,
					locSprache, locSprache);
		} catch (RemoteException ex) {
			// unuebersetzt ist besser als nix
			sBelegart = belegartCNr;
		}
		try {
			if (belegartCNr.equals(LocaleFac.BELEGART_AUFTRAG)) {
				sBelegnummer = getAuftragFac()
						.auftragFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_BESTELLUNG)) {
				sBelegnummer = getBestellungFac().bestellungFindByPrimaryKey(
						iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_GUTSCHRIFT)
					|| belegartCNr.equals(LocaleFac.BELEGART_RECHNUNG)
					|| belegartCNr.equals(LocaleFac.BELEGART_PROFORMARECHNUNG)) {
				sBelegnummer = getRechnungFac().rechnungFindByPrimaryKey(
						iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_LIEFERSCHEIN)) {
				sBelegnummer = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(iIdBeleg, theClientDto)
						.getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANGEBOT)) {
				sBelegnummer = getAngebotFac().angebotFindByPrimaryKey(
						iIdBeleg, theClientDto).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_ANFRAGE)) {
				sBelegnummer = getAnfrageFac().anfrageFindByPrimaryKey(
						iIdBeleg, theClientDto).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_PROJEKT)) {
				sBelegnummer = getProjektFac()
						.projektFindByPrimaryKey(iIdBeleg).getCNr();
			} else if (belegartCNr.equals(LocaleFac.BELEGART_INSERAT)) {
				sBelegnummer = getInseratFac()
						.inseratFindByPrimaryKey(iIdBeleg).getCNr();
			} else {
				// return "Unbekannte Belegart";
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
						"belegart " + belegartCNr + " nicht implementiert"));
			}
		} catch (Exception ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex1);
		}
		String sDateiname = sBelegart.trim() + " " + sBelegnummer.trim()
				+ ".pdf";
		// keine "/"
		sDateiname = sDateiname.replaceAll("/", "-");
		return sDateiname;
	}

	public void createVersandstatus(VersandstatusDto versandstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Versandstatus versandstatus = new Versandstatus(
					versandstatusDto.getStatusCNr(),
					versandstatusDto.getISort());
			em.persist(versandstatus);
			em.flush();
			setVersandstatusFromVersandstatusDto(versandstatus,
					versandstatusDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeVersandstatus(VersandstatusDto versandstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (versandstatusDto != null) {
			String statusCNr = versandstatusDto.getStatusCNr();
			try {
				Versandstatus toRemove = em
						.find(Versandstatus.class, statusCNr);
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
			}
		}
	}

	public void updateVersandstatus(VersandstatusDto versandstatusDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (versandstatusDto != null) {
			String statusCNr = versandstatusDto.getStatusCNr();
			try {
				Versandstatus versandstatus = em.find(Versandstatus.class,
						statusCNr);
				setVersandstatusFromVersandstatusDto(versandstatus,
						versandstatusDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public VersandstatusDto versandstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		try {
			Versandstatus versandstatus = em.find(Versandstatus.class,
					statusCNr);
			if (versandstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleVersandstatusDto(versandstatus);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIId(
			Integer partnerIId) throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em
				.createNamedQuery("VersandauftragfindByEmpfaengerPartnerIId");
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

	public VersandauftragDto[] versandauftragFindByEmpfaengerPartnerIIdOhneExc(
			Integer partnerIId) throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em
				.createNamedQuery("VersandauftragfindByEmpfaengerPartnerIId");
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

	public VersandauftragDto[] versandauftragFindBySenderPartnerIId(
			Integer partnerIId) throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em
				.createNamedQuery("VersandauftragfindBySenderPartnerIId");
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

	public VersandauftragDto[] versandauftragFindBySenderPartnerIIdOhneExc(
			Integer partnerIId) throws EJBExceptionLP, RemoteException {
		VersandauftragDto[] versandauftragDtos = null;
		// try {
		Query query = em
				.createNamedQuery("VersandauftragfindBySenderPartnerIId");
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

	public VersandanhangDto[] VersandanhangFindByVersandauftragIID(
			Integer versandauftragIID) throws EJBExceptionLP, RemoteException {
		// try {
		Query query = em
				.createNamedQuery("VersandanhangfindByVersandauftragIID");
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

	private VersandanhangDto[] assembleVersandanhangDtos(
			Collection<?> versandanhaenge) {
		List<VersandanhangDto> list = new ArrayList<VersandanhangDto>();
		if (versandanhaenge != null) {
			Iterator<?> iterator = versandanhaenge.iterator();
			while (iterator.hasNext()) {
				Versandanhang versandanhang = (Versandanhang) iterator.next();
				list.add(assembleVersandanhangDto(versandanhang));
			}
		}
		VersandanhangDto[] returnArray = new VersandanhangDto[list.size()];
		return (VersandanhangDto[]) list.toArray(returnArray);
	}

	private void setVersandstatusFromVersandstatusDto(
			Versandstatus versandstatus, VersandstatusDto versandstatusDto) {
		versandstatus.setISort(versandstatusDto.getISort());
		em.merge(versandstatus);
		em.flush();
	}

	private void setVersandanhangFromVersandanhangDto(
			Versandanhang versandanhang, VersandanhangDto versandanhangDto) {
		versandanhang.setCDateiname(versandanhangDto.getCDateiname());
		versandanhang.setIId(versandanhangDto.getIId());
		versandanhang.setOInhalt(versandanhangDto.getOInhalt());
		versandanhang.setVersandauftragIId(versandanhangDto
				.getVersandauftragIId());
		em.merge(versandanhang);
		em.flush();
	}

	private VersandstatusDto assembleVersandstatusDto(
			Versandstatus versandstatus) {
		return VersandstatusDtoAssembler.createDto(versandstatus);
	}

	private VersandstatusDto[] assembleVersandstatusDtos(
			Collection<?> versandstatuss) {
		List<VersandstatusDto> list = new ArrayList<VersandstatusDto>();
		if (versandstatuss != null) {
			Iterator<?> iterator = versandstatuss.iterator();
			while (iterator.hasNext()) {
				Versandstatus versandstatus = (Versandstatus) iterator.next();
				list.add(assembleVersandstatusDto(versandstatus));
			}
		}
		VersandstatusDto[] returnArray = new VersandstatusDto[list.size()];
		return (VersandstatusDto[]) list.toArray(returnArray);
	}

	public Integer versandauftragFindFehlgeschlagenen(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// begin
		Integer iId = null;
		// try {
		Query query = em
				.createNamedQuery("VersandauftragfindByPersonalIIdStatus");
		query.setParameter(1, theClientDto.getIDPersonal());
		query.setParameter(2, VersandFac.STATUS_FEHLGESCHLAGEN);
		Versandauftrag versandauftrag = (Versandauftrag) query
				.getSingleResult();
		if (versandauftrag == null) {
			return null;
		}
		iId = versandauftrag.getIId();
		// }
		// catch (FinderException ex) {
		// }
		return iId;
	}

	public VersandauftragDto versandauftragFindNextNotInDoc() {
		Query query = em.createNamedQuery("VersandauftragfindAllOhneDaten");
		query.setMaxResults(1);
		try {
			Versandauftrag versandauftrag = (Versandauftrag) query
					.getSingleResult();
			VersandauftragDto versandauftragDto = assembleVersandauftragDto(versandauftrag);
			return versandauftragDto;
		} catch (javax.persistence.NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_JCR_KEINE_AUFTRAEGE_ZU_KOPIEREN, e);
		}

	}

	public void sendeVersandauftragErneut(Integer versandauftragIId,
			Timestamp tSendezeitpunktWunsch, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
				ParametermandantDto pmBeginnMonat = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_DELAY_E_MAILVERSAND);
				delay = Integer.parseInt(pmBeginnMonat.getCWert());
			} catch (RemoteException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						new Exception("Parameter "
								+ ParameterFac.PARAMETER_DELAY_E_MAILVERSAND
								+ " konnte nicht gefunden werden"));
			}
			gc.set(GregorianCalendar.MINUTE, gc.get(GregorianCalendar.MINUTE)
					+ delay);
			tSendezeitpunktWunsch = new Timestamp(gc.getTimeInMillis());
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class,
				versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (versandauftrag.getStatusCNr() == null
				|| versandauftrag.getStatusCNr().equals(
						VersandFac.STATUS_DATEN_UNGUELTIG)
				|| versandauftrag.getStatusCNr().equals(
						VersandFac.STATUS_STORNIERT)
				|| versandauftrag.getStatusCNr().equals(
						VersandFac.STATUS_FEHLGESCHLAGEN)) {
			versandauftrag.setStatusCNr(null);
			versandauftrag.setTSendezeitpunktwunsch(tSendezeitpunktWunsch);
		} else if (versandauftrag.getStatusCNr().equals(
				VersandFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_SYSTEM_VERSANDAUFTRAG_WURDE_BEREITS_VERSANDT,
					new Exception("Versandauftrag iId=" + versandauftragIId
							+ " wurde bereits versandt."));
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"Versandauftrag iId=" + versandauftragIId
							+ " hat ungueltigen Status. Status="
							+ versandauftrag.getStatusCNr()));
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void sendeVersandauftragSofort(Integer versandauftragIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(versandauftragIId);
		// begin
		if (versandauftragIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("versandauftragIId == null"));
		}
		// try {
		Versandauftrag versandauftrag = em.find(Versandauftrag.class,
				versandauftragIId);
		if (versandauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		// Wenn er noch nicht versandt wurde
		if (versandauftrag.getStatusCNr() == null) {
			versandauftrag.setTSendezeitpunktwunsch(getTimestamp());

			String smtpServer = null;
			try {
				smtpServer = getParameterFac()
						.parametermandantFindByPrimaryKey(
								ParameterFac.PARAMETER_SMTPSERVER,
								ParameterFac.KATEGORIE_VERSANDAUFTRAG,
								theClientDto.getMandant()).getCWert();
			} catch (EJBExceptionLP e1) {
				myLogger.error(e1.getMessage());
			} catch (RemoteException e1) {
				myLogger.error(e1.getMessage());
			}
			if (smtpServer != null) {
				javax.ejb.TimerHandle handle = mailProcessor.updateTimer(
						versandauftragIId,
						versandauftrag.getTSendezeitpunktwunsch());
				if (handle == null) {
					// kein Timer fuer den Auftrag vorhanden
					myLogger.warn("Kein Timer gefunden fuer Versandauftrag: "
							+ versandauftragIId);
					VersandauftragDto versandauftragDto = assembleVersandauftragDto(versandauftrag);
					doSendMailOrFax(versandauftragDto, theClientDto);
					myLogger.warn("Versandauftrag erneut versandt: "
							+ versandauftragIId);
				}
			}
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

	public VersandanhangDto createVersandanhang(
			VersandanhangDto versandanhangDto, TheClientDto theClientDto) {
		myLogger.logData(versandanhangDto, theClientDto.getIDUser());
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_VERSANDANHANG);
		versandanhangDto.setIId(iId);
		try {
			Versandanhang versandanhang = new Versandanhang(
					versandanhangDto.getIId());
			versandanhang.setCDateiname(versandanhangDto.getCDateiname());
			versandanhang.setOInhalt(versandanhangDto.getOInhalt());
			versandanhang.setVersandauftragIId(versandanhangDto
					.getVersandauftragIId());
			em.persist(versandanhang);
			em.flush();
			// Auch in JCR speichern da spaeter wieder geloescht wird
			// TODO UNCOMMENT AFTER VERSANDDIENST VERSION X.X.X IS INSTALLED
			getJCRDocFac().saveVersandanhangAsDocument(versandanhangDto, false,
					theClientDto);
			return versandanhangDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception());
		}
	}

	public void createVersandanhaenge(ArrayList<VersandanhangDto> alAnhaenge,
			TheClientDto theClientDto)  {

		for (int i = 0; i < alAnhaenge.size(); i++) {
			createVersandanhang(alAnhaenge.get(i), theClientDto);
		}

	}
}
