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
 *******************************************************************************/
package com.lp.server.system.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artikelsperren;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejb.Spediteur;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class PflegeFacBean extends Facade implements
		com.lp.server.system.service.PflegeFac {

	@PersistenceContext
	private EntityManager em;

	public void sp2597(TheClientDto theClientDto) {
		// Alle gewaehlten SNR-Artikel am Hauptlager abbuchen

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		org.hibernate.Session session = FLRSessionFactory.getFactory()
				.openSession();

		String queryString = "SELECT a FROM FLRArtikel a WHERE a.artikelart_c_nr <>'Handartikel'";

		org.hibernate.Query query = session.createQuery(queryString);

		int iStartID = 1000;
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRArtikel a = (FLRArtikel) resultListIterator.next();

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em
						.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
				queryEM.setParameter(1, a.getI_id());
				queryEM.setParameter(2, 13);
				// @todo getSingleResult oder getResultList ?
				Artikelsperren doppelt = (Artikelsperren) queryEM
						.getSingleResult();

				// Wenn berits vorhanden, dann auslassen

				continue;
			} catch (NoResultException ex) {

			}

			// Sonst neu eintragen

			// Neue ID holen

			Query queryNext = em
					.createNamedQuery("ArtikelsperrenejbSelectNextReihung");
			queryNext.setParameter(1, a.getI_id());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

			String zeile = "INSERT INTO WW_ARTIKELSPERREN (I_ID,ARTIKEL_I_ID,SPERREN_I_ID,T_AENDERN,PERSONAL_I_ID_AENDERN,C_GRUND,I_SORT) VALUES ("
					+ iStartID
					+ ","
					+ a.getI_id()
					+ ",13,'2014-07-15',11,'Bauteil neu erstellt'," + i + ");";
			rueckgabe += zeile + new String(CRLFAscii);

			iStartID++;

		}

		session.close();

		System.out.println(rueckgabe);

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String sp2486(Set<Integer> artikelIds, TheClientDto theClientDto) {
		// Alle gewaehlten SNR-Artikel am Hauptlager abbuchen

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		org.hibernate.Session session = FLRSessionFactory.getFactory()
				.openSession();

		String queryString = "SELECT l FROM FLRLagerbewegung l WHERE  l.c_seriennrchargennr IS NOT NULL AND l.b_abgang=1 AND l.b_historie=0"; // AND
		// l.flrartikel.i_id=16257";

		org.hibernate.Query query = session.createQuery(queryString);

		int u = 0;
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRLagerbewegung o = (FLRLagerbewegung) resultListIterator.next();

			if (o.getI_id_buchung() == 134645) {
				int z = 0;
			}

			// Zugang suchen und angleichen

			try {
				LagerabgangursprungDto[] urspruenge = getLagerFac()
						.lagerabgangursprungFindByLagerbewegungIIdBuchung(
								o.getI_id_buchung());

				for (int i = 0; i < urspruenge.length; i++) {

					LagerbewegungDto[] bewUrspruenge = getLagerFac()
							.lagerbewegungFindByIIdBuchung(
									urspruenge[i].getILagerbewegungidursprung());

					for (int j = 0; j < bewUrspruenge.length; j++) {
						getLagerFac().versionPerEntityManagerUpdaten(
								bewUrspruenge[j].getIId(),
								o.getC_seriennrchargennr(), o.getC_version());

					}

					LagerabgangursprungDto[] abgange = getLagerFac()
							.lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(
									urspruenge[i].getILagerbewegungidursprung());

					for (int j = 0; j < abgange.length; j++) {

						LagerbewegungDto[] abgang = getLagerFac()
								.lagerbewegungFindByIIdBuchung(
										abgange[j].getILagerbewegungid());

						for (int k = 0; k < abgang.length; k++) {

							if (Helper.short2boolean(abgang[k].getBHistorie()) == true) {

								getLagerFac().versionPerEntityManagerUpdaten(
										abgang[k].getIId(),
										o.getC_seriennrchargennr(),
										o.getC_version());
							}
						}
					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			u++;
			System.out.println(u);

		}

		session.close();
		try {
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(
					theClientDto).getIId();

			Iterator it = artikelIds.iterator();

			while (it.hasNext()) {

				Integer artikelId = (Integer) it.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(
						artikelId, theClientDto);

				if (Helper.short2boolean(aDto.getBSeriennrtragend()) == true) {

					SeriennrChargennrAufLagerDto[] snrs = getLagerFac()
							.getAllSerienChargennrAufLagerInfoDtos(artikelId,
									hauptlagerIId, true, null, theClientDto);

					if (snrs.length > 0) {

						for (int i = 0; i < snrs.length; i++) {

							HandlagerbewegungDto handlagerbewegungDto = new HandlagerbewegungDto();
							handlagerbewegungDto.setArtikelIId(artikelId);
							handlagerbewegungDto.setLagerIId(hauptlagerIId);
							handlagerbewegungDto
									.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
											.erstelleDtoAusEinerSeriennummer(snrs[i]
													.getCSeriennrChargennr()));
							handlagerbewegungDto.setCKommentar("HeliumV");
							handlagerbewegungDto.setBAbgang(Helper
									.boolean2Short(true));
							handlagerbewegungDto.setNMenge(BigDecimal.ONE);
							handlagerbewegungDto
									.setNVerkaufspreis(getLagerFac()
											.getGemittelterGestehungspreisEinesLagers(
													artikelId, hauptlagerIId,
													theClientDto));

							try {
								getLagerFac().createHandlagerbewegung(
										handlagerbewegungDto, theClientDto);
							} catch (EJBExceptionLP e) {

								String zeile = "Fehler bei Abbuchung:"
										+ aDto.getCNr() + " SNR: "
										+ snrs[i].getCSeriennrChargennr();
								rueckgabe += zeile + new String(CRLFAscii);

							}

						}

					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return rueckgabe;

	}

	public Integer holeKopiertenSpediteurPJ18612(Integer spediteurlIId_001,
			TheClientDto theClientDto) {

		if (spediteurlIId_001 == null) {
			return null;
		}

		try {
			SpediteurDto sDto = getMandantFac().spediteurFindByPrimaryKey(
					spediteurlIId_001);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em
						.createNamedQuery("SpediteurfindByMandantSpediteurname");
				queryEM.setParameter(1, "002");
				queryEM.setParameter(2, sDto.getCNamedesspediteurs());
				Spediteur vorhanden = (Spediteur) queryEM.getSingleResult();

				// Wenn bereits vorhanden, dann zurueckgeben
				return vorhanden.getIId();
			} catch (NoResultException ex) {

				// Wenn nicht, dann anlegen
				sDto.setMandantCNr("002");
				sDto.setIId(null);
				return getMandantFac().createSpediteur(sDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;

	}

	public Integer holeKopiertesZahlungszielPJ18612(
			Integer zahlungszielIId_001, TheClientDto theClientDto) {

		if (zahlungszielIId_001 == null) {
			return null;
		}

		try {
			ZahlungszielDto zDto = getMandantFac()
					.zahlungszielFindByPrimaryKey(zahlungszielIId_001,
							theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em
						.createNamedQuery("ZahlungszielfindByCBezMandantCNr");
				queryEM.setParameter(1, "002");
				queryEM.setParameter(2, zDto.getCBez());
				Zahlungsziel vorhanden = (Zahlungsziel) queryEM
						.getSingleResult();

				// Wenn bereits vorhanden, dann zurueckgeben
				return vorhanden.getIId();
			} catch (NoResultException ex) {

				// Wenn nicht, dann anlegen#
				zDto.setIId(null);
				zDto.setMandantCNr("002");
				return getMandantFac().createZahlungsziel(zDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;

	}

	public Integer holeKopiertenMwstsatzbezPJ18612(Integer mwstsatzbezIId_001,
			TheClientDto theClientDto) {

		if (mwstsatzbezIId_001 == null) {
			return null;
		}

		try {
			MwstsatzbezDto zDto = getMandantFac().mwstsatzbezFindByPrimaryKey(
					mwstsatzbezIId_001, theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em
						.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
				queryEM.setParameter(1, "002");
				queryEM.setParameter(2, zDto.getCBezeichnung());
				Mwstsatzbez vorhanden = (Mwstsatzbez) queryEM.getSingleResult();

				// Wenn bereits vorhanden, dann zurueckgeben
				return vorhanden.getIId();
			} catch (NoResultException ex) {

				// Wenn nicht, dann anlegen#
				zDto.setIId(null);
				zDto.setMandantCNr("002");
				return getMandantFac().createMwstsatzbez(zDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;

	}

	public Integer holeKopierteLieferartPJ18612(Integer lieferantIId_001,
			TheClientDto theClientDto) {

		if (lieferantIId_001 == null) {
			return null;
		}

		try {
			LieferartDto lDto = getLocaleFac().lieferartFindByPrimaryKey(
					lieferantIId_001, theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em
						.createNamedQuery("LieferartfindbyCNrMandantCNr");
				queryEM.setParameter(1, lDto.getCNr());
				queryEM.setParameter(2, "002");
				Lieferart vorhanden = (Lieferart) queryEM.getSingleResult();

				// Wenn bereits vorhanden, dann zurueckgeben
				return vorhanden.getIId();
			} catch (NoResultException ex) {
				// Wenn nicht, dann anlegen#
				lDto.setIId(null);
				lDto.setMandantCNr("002");
				return getLocaleFac().createLieferart(lDto, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return null;

	}

	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pj18612(TheClientDto theClientDto) {

		if (!theClientDto.getMandant().equals("002")) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN,
					"FEHLER-> BENUTZER MUSS AN MANDANT 002 ANGEMELDET SEIN");
		}
		org.hibernate.Session session = FLRSessionFactory.getFactory()
				.openSession();

		String queryString = "SELECT l FROM FLRLieferant l WHERE  l.mandant_c_nr='001'";

		org.hibernate.Query query = session.createQuery(queryString);
		try {
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(
					theClientDto).getIId();

			int u = 0;
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {
				u++;
				System.out.print(u+" von "+results.size());
				
				FLRLieferant o = (FLRLieferant) resultListIterator.next();

				LieferantDto lfDtoOriginal = getLieferantFac()
						.lieferantFindByPrimaryKey(o.getI_id(), theClientDto);

				Integer lieferantIId_Original = lfDtoOriginal.getIId();

				// Wenn der Lieferant noch nicht vorhanden ist

				LieferantDto lfDto = getLieferantFac()
						.lieferantFindByiIdPartnercNrMandantOhneExc(
								o.getFlrpartner().getI_id(), "002",
								theClientDto);
				if (lfDto == null) {

					lfDtoOriginal.setMandantCNr("002");
					lfDtoOriginal.setIId(null);
					lfDtoOriginal.setKontoIIdKreditorenkonto(null);
					lfDtoOriginal.setKontoIIdWarenkonto(null);
					lfDtoOriginal.setIIdKostenstelle(null);
					lfDtoOriginal.setPersonalIIdAendern(theClientDto
							.getIDPersonal());
					lfDtoOriginal.setPersonalIIdAnlegen(theClientDto
							.getIDPersonal());
					lfDtoOriginal.setPersonalIIdFreigabe(null);
					lfDtoOriginal.setKontoIIdKreditorenkonto(null);

					Integer zahlungszielIId_002 = getPflegeFac().holeKopiertesZahlungszielPJ18612(
							lfDtoOriginal.getZahlungszielIId(), theClientDto);
					lfDtoOriginal.setZahlungszielIId(zahlungszielIId_002);

					Integer spediteurIId_002 = getPflegeFac().holeKopiertenSpediteurPJ18612(
							lfDtoOriginal.getIdSpediteur(), theClientDto);
					lfDtoOriginal.setIdSpediteur(spediteurIId_002);

					Integer lieferartIId_002 = getPflegeFac().holeKopierteLieferartPJ18612(
							lfDtoOriginal.getLieferartIId(), theClientDto);
					lfDtoOriginal.setLieferartIId(lieferartIId_002);

					lfDtoOriginal.setLagerIIdZubuchungslager(hauptlagerIId);

					lfDtoOriginal.setCKundennr(null);

					Integer mwstsatzbezIId_002 = getPflegeFac().holeKopiertenMwstsatzbezPJ18612(
							lfDtoOriginal.getMwstsatzbezIId(), theClientDto);
					lfDtoOriginal.setMwstsatzbezIId(mwstsatzbezIId_002);

					Integer lieferantIIdNeu = getLieferantFac()
							.createLieferant(lfDtoOriginal, theClientDto);

					// Artikellieferant anlegen

					ArtikellieferantDto[] alDtos = getArtikelFac()
							.artikellieferantFindByLieferantIId(
									lieferantIId_Original, theClientDto);
					for (int i = 0; i < alDtos.length; i++) {
						ArtikellieferantDto alDto = alDtos[i];
						alDto.setIId(null);
						alDto.setLieferantIId(lieferantIIdNeu);
						getArtikelFac().createArtikellieferant(alDto,
								theClientDto);
					}

				}

			}
			
			
			session.close();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

}
