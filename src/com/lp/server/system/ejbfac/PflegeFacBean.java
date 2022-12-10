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

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.artikel.ejb.Artikelkommentarspr;
import com.lp.server.artikel.ejb.ArtikelkommentarsprPK;
import com.lp.server.artikel.ejb.Artikelsperren;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.ArtikelsprPK;
import com.lp.server.artikel.fastlanereader.WarenbewegungenHandler;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelkommentar;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelkommentarspr;
import com.lp.server.artikel.fastlanereader.generated.FLRLagerbewegung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsnrchnrDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.HandlagerbewegungDto;
import com.lp.server.artikel.service.LagerabgangursprungDto;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.bestellung.fastlanereader.generated.FLRWareneingangspositionen;
import com.lp.server.fertigung.fastlanereader.generated.FLRLosAuftrag;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.partner.fastlanereader.generated.FLRLieferant;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.ejb.Mwstsatzbez;
import com.lp.server.system.ejb.Spediteur;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung;
import com.lp.server.system.fastlanereader.generated.FLRPaneldaten;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFolder;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.GeaenderteChargennummernDto;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.SpediteurDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class PflegeFacBean extends Facade implements com.lp.server.system.service.PflegeFac {

	@PersistenceContext
	private EntityManager em;

	public ArrayList<GeaenderteChargennummernDto> automatischeChargennummernAusWEPsNachtragen(String artikelNummerVon,
			String artikelnummerBis, DatumsfilterVonBis dVonBis, TheClientDto theClientDto) {

		ArrayList<GeaenderteChargennummernDto> alWeps = new ArrayList<GeaenderteChargennummernDto>();

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT wep FROM FLRWareneingangspositionen wep WHERE wep.flrbestellposition.flrartikel.b_chargennrtragend=1";

		queryString += " AND wep.flrbestellposition.flrartikel.mandant_c_nr ='" + theClientDto.getMandant() + "'";

		if (artikelNummerVon != null) {
			queryString += " AND wep.flrbestellposition.flrartikel.c_nr >='" + artikelNummerVon + "'";
		}
		if (artikelnummerBis != null) {
			String artikelNrBis_Gefuellt = Helper.fitString2Length(artikelnummerBis, 25, '_');
			queryString += " AND wep.flrbestellposition.flrartikel.c_nr <='" + artikelNrBis_Gefuellt + "'";
		}

		if (dVonBis != null) {
			if (dVonBis.getTimestampVon() != null) {
				queryString += " AND wep.flrwareneingang.t_wareneingansdatum >='"
						+ Helper.formatTimestampWithSlashes(dVonBis.getTimestampVon()) + "'";
			}

			if (dVonBis.getTimestampBis() != null) {
				queryString += " AND wep.flrwareneingang.t_wareneingansdatum <'"
						+ Helper.formatTimestampWithSlashes(dVonBis.getTimestampBis()) + "'";
			}

		}

		queryString += " ORDER BY wep.flrbestellposition.flrartikel.c_nr ASC , wep.flrwareneingang.t_wareneingansdatum ASC ";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {

			FLRWareneingangspositionen wep = (FLRWareneingangspositionen) resultListIterator.next();

			List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(LocaleFac.BELEGART_BESTELLUNG,
							wep.getI_id());

			for (int i = 0; i < snrs.size(); i++) {

				SeriennrChargennrMitMengeDto snrDto = snrs.get(i);

				LagerbewegungDto lbewDto = getLagerFac().getLetzteintrag(LocaleFac.BELEGART_BESTELLUNG, wep.getI_id(),
						snrDto.getCSeriennrChargennr());

				if (Helper.short2Boolean(lbewDto.getBVollstaendigverbraucht()) == false) {

					String chnrNeu = getWareneingangFac()
							.generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(
									wep.getBestellposition_i_id(), wep.getWareneingang_i_id());

					if (!chnrNeu.equals(snrDto.getCSeriennrChargennr())) {
						if (!snrDto.getCSeriennrChargennr().trim().toUpperCase().startsWith("EX")) {

							try {
								getLagerFac().aendereEinzelneSerienChargennummerEinesArtikel(
										wep.getFlrbestellposition().getFlrartikel().getI_id(),
										snrDto.getCSeriennrChargennr(), chnrNeu, snrDto.getCVersion(),
										snrDto.getCVersion(), theClientDto);
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							GeaenderteChargennummernDto gDto = new GeaenderteChargennummernDto();
							gDto.setChnrAlt(snrDto.getCSeriennrChargennr());
							gDto.setChnrNeu(chnrNeu);
							gDto.setBestellpositionIId(wep.getBestellposition_i_id());
							gDto.setWepIId(wep.getI_id());
							alWeps.add(gDto);
						}
					}
				}
			}

		}

		session.close();

		return alWeps;

	}

	public ArrayList<String> sp9000(boolean bFalscheAnsprechpartnerLeeren, TheClientDto theClientDto) {
		// Alle gewaehlten SNR-Artikel am Hauptlager abbuchen

		ArrayList<String> returnString = new ArrayList<String>();

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Session sessionUpdate = FLRSessionFactory.getFactory().openSession();

		// Angebote
		String queryString = "select ab.i_id, ab.c_nr from angb_angebot ab"
				+ " left join part_kunde k on ab.kunde_i_id_rechnungsadresse = k.i_id"
				+ " where ansprechpartner_i_id_rechnungsadresse not in ("
				+ "	select i_id from part_ansprechpartner ap" + "	where ap.partner_i_id = k.partner_i_id)"
				+ " and ansprechpartner_i_id_rechnungsadresse is not null" + " order by c_nr desc" + "";

		org.hibernate.Query query = session.createSQLQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = (Object[]) resultListIterator.next();

			Integer angebotIId = (Integer) zeile[0];
			String angebotCNr = (String) zeile[1];

			returnString.add("Angebot " + angebotCNr);

			if (bFalscheAnsprechpartnerLeeren == true) {
				org.hibernate.Query queryUpdate = sessionUpdate.createSQLQuery(
						"update angb_angebot SET ansprechpartner_i_id_rechnungsadresse = NULL WHERE i_id="
								+ angebotIId);
				queryUpdate.executeUpdate();
			}

		}

		// Auftraege
		queryString = "select ab.i_id,ab.c_nr from auft_auftrag ab "
				+ " left join part_kunde k on ab.kunde_i_id_rechnungsadresse = k.i_id"
				+ " where ansprechpartner_i_id_rechnungsadresse not in ("
				+ "	select i_id from part_ansprechpartner ap" + "	where ap.partner_i_id = k.partner_i_id)"
				+ " and ansprechpartner_i_id_rechnungsadresse is not null" + " order by c_nr desc";

		query = session.createSQLQuery(queryString);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = (Object[]) resultListIterator.next();

			Integer auftragIId = (Integer) zeile[0];
			String auftragCNr = (String) zeile[1];

			returnString.add("Auftrag " + auftragCNr);

			if (bFalscheAnsprechpartnerLeeren == true) {
				org.hibernate.Query queryUpdate = sessionUpdate.createSQLQuery(
						"update auft_auftrag SET ansprechpartner_i_id_rechnungsadresse = NULL WHERE i_id="
								+ auftragIId);
				queryUpdate.executeUpdate();
			}

		}

		// Lieferscheine
		queryString = "select ls.i_id, ls.c_nr from ls_lieferschein ls"
				+ " left join part_kunde k on ls.kunde_i_id_rechnungsadresse = k.i_id"
				+ " left join part_partner p on k.partner_i_id = p.i_id"
				+ " where ansprechpartner_i_id_rechnungsadresse not in ("
				+ "	select i_id from part_ansprechpartner ap" + "	where ap.partner_i_id = k.partner_i_id)"
				+ " and ansprechpartner_i_id_rechnungsadresse is not null" + " order by c_nr desc";

		query = session.createSQLQuery(queryString);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = (Object[]) resultListIterator.next();

			Integer lieferscheinIId = (Integer) zeile[0];
			String lieferscheinCNr = (String) zeile[1];

			returnString.add("Lieferschein " + lieferscheinCNr);

			if (bFalscheAnsprechpartnerLeeren == true) {
				org.hibernate.Query queryUpdate = sessionUpdate.createSQLQuery(
						"update ls_lieferschein SET ansprechpartner_i_id_rechnungsadresse = NULL WHERE i_id="
								+ lieferscheinIId);
				queryUpdate.executeUpdate();
			}

		}

		// Rechnungen
		queryString = "select re.i_id, re.c_nr from rech_rechnung re "
				+ " left join part_kunde k on re.kunde_i_id = k.i_id "
				+ " left join part_partner p on k.partner_i_id = p.i_id " + " where ansprechpartner_i_id not in ( "
				+ "	select i_id from part_ansprechpartner ap " + "	where ap.partner_i_id = k.partner_i_id) "
				+ " and ansprechpartner_i_id is not null " + " order by c_nr desc";

		query = session.createSQLQuery(queryString);

		results = query.list();
		resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			Object[] zeile = (Object[]) resultListIterator.next();

			Integer rechnungIId = (Integer) zeile[0];
			String rechnungCNr = (String) zeile[1];

			returnString.add("Rechnung " + rechnungCNr);

			if (bFalscheAnsprechpartnerLeeren == true) {
				org.hibernate.Query queryUpdate = sessionUpdate.createSQLQuery(
						"update rech_rechnung SET ansprechpartner_i_id = NULL WHERE i_id=" + rechnungIId);
				queryUpdate.executeUpdate();
			}

		}

		session.close();

		return returnString;

	}

	public void sp2597(TheClientDto theClientDto) {
		// Alle gewaehlten SNR-Artikel am Hauptlager abbuchen

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

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
				Query queryEM = em.createNamedQuery("ArtikelsperrenfindByArtikelIIdSperrenIId");
				queryEM.setParameter(1, a.getI_id());
				queryEM.setParameter(2, 13);
				// @todo getSingleResult oder getResultList ?
				Artikelsperren doppelt = (Artikelsperren) queryEM.getSingleResult();

				// Wenn berits vorhanden, dann auslassen

				continue;
			} catch (NoResultException ex) {

			}

			// Sonst neu eintragen

			// Neue ID holen

			Query queryNext = em.createNamedQuery("ArtikelsperrenejbSelectNextReihung");
			queryNext.setParameter(1, a.getI_id());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

			String zeile = "INSERT INTO WW_ARTIKELSPERREN (I_ID,ARTIKEL_I_ID,SPERREN_I_ID,T_AENDERN,PERSONAL_I_ID_AENDERN,C_GRUND,I_SORT) VALUES ("
					+ iStartID + "," + a.getI_id() + ",13,'2014-07-15',11,'Bauteil neu erstellt'," + i + ");";
			rueckgabe += zeile + new String(CRLFAscii);

			iStartID++;

		}

		session.close();

		System.out.println(rueckgabe);

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pj19519(TheClientDto theClientDto) {

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct flrlos.i_id , (select sum(flrfehlmenge.n_menge) FROM FLRFehlmenge flrfehlmenge WHERE flrfehlmenge.flrlossollmaterial.flrlos = flrlos.i_id AND flrfehlmenge.n_menge>0) FROM FLRLos AS flrlos ";

		queryString += " WHERE flrlos.stueckliste_i_id IS NULL AND flrlos.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND flrlos.status_c_nr='" + LocaleFac.STATUS_IN_PRODUKTION + "'";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		int i = 0;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer los_i_id = (Integer) o[0];
			boolean bFehlmengenVorhande = false;

			if (o[1] != null && ((BigDecimal) o[1]).doubleValue() != 0) {
				bFehlmengenVorhande = true;
			}

			if (bFehlmengenVorhande == false) {
				try {
					getFertigungFac().manuellErledigen(los_i_id, false, theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
			i++;
			System.out.println(i);

		}

		session.close();

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String sp2486(Set<Integer> artikelIds, TheClientDto theClientDto) {
		// Alle gewaehlten SNR-Artikel am Hauptlager abbuchen

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

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
						.lagerabgangursprungFindByLagerbewegungIIdBuchung(o.getI_id_buchung());

				for (int i = 0; i < urspruenge.length; i++) {

					LagerbewegungDto[] bewUrspruenge = getLagerFac()
							.lagerbewegungFindByIIdBuchung(urspruenge[i].getILagerbewegungidursprung());

					for (int j = 0; j < bewUrspruenge.length; j++) {
						getLagerFac().versionPerEntityManagerUpdaten(bewUrspruenge[j].getIId(),
								o.getC_seriennrchargennr(), o.getC_version());

					}

					LagerabgangursprungDto[] abgange = getLagerFac()
							.lagerabgangursprungFindByLagerbewegungIIdBuchungsursprung(
									urspruenge[i].getILagerbewegungidursprung());

					for (int j = 0; j < abgange.length; j++) {

						LagerbewegungDto[] abgang = getLagerFac()
								.lagerbewegungFindByIIdBuchung(abgange[j].getILagerbewegungid());

						for (int k = 0; k < abgang.length; k++) {

							if (Helper.short2boolean(abgang[k].getBHistorie()) == true) {

								getLagerFac().versionPerEntityManagerUpdaten(abgang[k].getIId(),
										o.getC_seriennrchargennr(), o.getC_version());
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
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			Iterator it = artikelIds.iterator();

			while (it.hasNext()) {

				Integer artikelId = (Integer) it.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelId, theClientDto);

				if (Helper.short2boolean(aDto.getBSeriennrtragend()) == true) {

					SeriennrChargennrAufLagerDto[] snrs = getLagerFac().getAllSerienChargennrAufLagerInfoDtos(artikelId,
							hauptlagerIId, true, null, theClientDto);

					if (snrs.length > 0) {

						for (int i = 0; i < snrs.length; i++) {

							HandlagerbewegungDto handlagerbewegungDto = new HandlagerbewegungDto();
							handlagerbewegungDto.setArtikelIId(artikelId);
							handlagerbewegungDto.setLagerIId(hauptlagerIId);
							handlagerbewegungDto.setSeriennrChargennrMitMenge(SeriennrChargennrMitMengeDto
									.erstelleDtoAusEinerSeriennummer(snrs[i].getCSeriennrChargennr()));
							handlagerbewegungDto.setCKommentar("HeliumV");
							handlagerbewegungDto.setBAbgang(Helper.boolean2Short(true));
							handlagerbewegungDto.setNMenge(BigDecimal.ONE);
							handlagerbewegungDto.setNVerkaufspreis(getLagerFac()
									.getGemittelterGestehungspreisEinesLagers(artikelId, hauptlagerIId, theClientDto));

							try {
								getLagerFac().createHandlagerbewegung(handlagerbewegungDto, theClientDto);
							} catch (EJBExceptionLP e) {

								String zeile = "Fehler bei Abbuchung:" + aDto.getCNr() + " SNR: "
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

	public Integer holeKopiertenSpediteurPJ18612(Integer spediteurlIId_001, TheClientDto theClientDto) {

		if (spediteurlIId_001 == null) {
			return null;
		}

		try {
			SpediteurDto sDto = getMandantFac().spediteurFindByPrimaryKey(spediteurlIId_001);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em.createNamedQuery("SpediteurfindByMandantSpediteurname");
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

	public Integer holeKopiertesZahlungszielPJ18612(Integer zahlungszielIId_001, TheClientDto theClientDto) {

		if (zahlungszielIId_001 == null) {
			return null;
		}

		try {
			ZahlungszielDto zDto = getMandantFac().zahlungszielFindByPrimaryKey(zahlungszielIId_001, theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em.createNamedQuery("ZahlungszielfindByCBezMandantCNr");
				queryEM.setParameter(1, "002");
				queryEM.setParameter(2, zDto.getCBez());
				Zahlungsziel vorhanden = (Zahlungsziel) queryEM.getSingleResult();

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

	public Integer holeKopiertenMwstsatzbezPJ18612(Integer mwstsatzbezIId_001, TheClientDto theClientDto) {

		if (mwstsatzbezIId_001 == null) {
			return null;
		}

		try {
			MwstsatzbezDto zDto = getMandantFac().mwstsatzbezFindByPrimaryKey(mwstsatzbezIId_001, theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em.createNamedQuery("MwstsatzbezfindByMandantCBezeichnung");
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

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int migriereChargeneigenschaftenUndChargenDokumenteWgSP4129(TheClientDto theClientDto) {

		SessionFactory factory = FLRSessionFactory.getFactory();

		Session session = null;

		session = factory.openSession();

		// Zuerst alle Paneldaten in den Speicher lesen und nach cKeyGruppieren
		String sQuery = " SELECT pd FROM FLRPaneldaten pd WHERE pd.panel_c_nr ='" + PanelFac.PANEL_CHARGENEIGENSCHAFTEN
				+ "' ORDER BY pd.c_key DESC";

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> resultList = query.list();

		LinkedHashMap<String, ArrayList<PaneldatenDto>> hmPanelDatenGruppiertNachCKey = new LinkedHashMap<String, ArrayList<PaneldatenDto>>();

		Iterator<?> it = resultList.iterator();
		while (it.hasNext()) {
			FLRPaneldaten pd = (FLRPaneldaten) it.next();
			try {
				PaneldatenDto paneldatenDto = getPanelFac().paneldatenFindByPrimaryKey(pd.getI_id());

				ArrayList alEinesKeys = null;

				if (hmPanelDatenGruppiertNachCKey.containsKey(pd.getC_key())) {
					alEinesKeys = hmPanelDatenGruppiertNachCKey.get(pd.getC_key());
				} else {
					alEinesKeys = new ArrayList();
				}

				alEinesKeys.add(paneldatenDto);

				hmPanelDatenGruppiertNachCKey.put(pd.getC_key(), alEinesKeys);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		session.close();

		// Nun alle loeschen
		session = FLRSessionFactory.getFactory().openSession();

		String hqlDelete = "delete FROM FLRPaneldaten pd WHERE pd.panel_c_nr ='" + PanelFac.PANEL_CHARGENEIGENSCHAFTEN
				+ "'";
		session.createQuery(hqlDelete).executeUpdate();

		session.close();

		Iterator ithm = hmPanelDatenGruppiertNachCKey.keySet().iterator();

		int k = 0;
		while (ithm.hasNext()) {
			k++;
			System.out.println("Zeile " + k + " von " + hmPanelDatenGruppiertNachCKey.size());
			// Dokumentenpfad alt
			String cKey = (String) ithm.next();

			try {
				LagerbewegungDto buchung = getLagerFac().getJuengsteBuchungEinerBuchungsNummer(new Integer(cKey));
				if (buchung != null) {
					BelegInfos bi = getLagerFac().getBelegInfos(buchung.getCBelegartnr(), buchung.getIBelegartid(),
							buchung.getIBelegartpositionid(), theClientDto);
					String zusatz = "";

					if (bi.getZusatz() != null) {
						zusatz = " " + bi.getZusatz();
					}

					String snrchr = "KEINE_SNRCHNR";

					if (buchung.getCSeriennrchargennr() != null) {
						snrchr = buchung.getCSeriennrchargennr();
					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(buchung.getArtikelIId(),
							theClientDto);

					// Quelle
					DocPath dpOld = new HeliumDocPath().add(new DocNodeLiteral(artikelDto.getMandantCNr()))
							.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL))
							.add(new DocNodeFolder(artikelDto.getCNr()))
							.add(new DocNodeFolder(WarenbewegungenHandler.DOCNODE_CHARGENDOKUMENTE))
							.add(new DocNodeFolder(snrchr)).add(new DocNodeFolder(
									buchung.getCBelegartnr().trim() + " " + bi.getBelegnummer() + zusatz));

					// Ziel
					DocPath dpZiel = new HeliumDocPath().add(new DocNodeLiteral(artikelDto.getMandantCNr()))
							.add(new DocNodeFolder(DocNodeBase.BELEGART_ARTIKEL))
							.add(new DocNodeFolder(artikelDto.getCNr()))
							.add(new DocNodeFolder(WarenbewegungenHandler.DOCNODE_CHARGENDOKUMENTE))
							.add(new DocNodeFolder(snrchr));

					getJCRDocFac().verschiebeBzwKopiereDokumentInAnderenDocPath(dpOld, dpZiel);

					// Key in PANELDATEN Updaten

					Integer artikelsnrchnrIIdBereitsEigenschaftenVorhanden = getLagerFac()
							.artikelsnrchnrIIdFindByArtikelIIdCSeriennrchargennr(artikelDto.getIId(), snrchr);

					if (artikelsnrchnrIIdBereitsEigenschaftenVorhanden == null) {
						Integer keyNeu = getLagerFac().updateArtikelsnrchnr(artikelDto.getIId(), snrchr, theClientDto);

						ArrayList<PaneldatenDto> alPanelDatenEinesKeys = hmPanelDatenGruppiertNachCKey.get(cKey);

						for (int i = 0; i < alPanelDatenEinesKeys.size(); i++) {
							alPanelDatenEinesKeys.get(i).setCKey(keyNeu + "");

						}

						PaneldatenDto[] dtos = new PaneldatenDto[alPanelDatenEinesKeys.size()];
						dtos = (PaneldatenDto[]) alPanelDatenEinesKeys.toArray(dtos);

						try {
							getPanelFac().createPaneldaten(dtos, theClientDto);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {
					int u = 0;
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return 0;
	}

	public Integer holeKopierteLieferartPJ18612(Integer lieferantIId_001, TheClientDto theClientDto) {

		if (lieferantIId_001 == null) {
			return null;
		}

		try {
			LieferartDto lDto = getLocaleFac().lieferartFindByPrimaryKey(lieferantIId_001, theClientDto);

			try {
				// duplicateunique: Pruefung: Artikelgruppe bereits
				// vorhanden.
				Query queryEM = em.createNamedQuery("LieferartfindbyCNrMandantCNr");
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
	public void textAusPdfInXKommentarAktualisieren(TheClientDto theClientDto) {

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT k FROM FLRArtikelkommentarspr k WHERE k.artikelkommentar.datenformat_c_nr='"
				+ MediaFac.DATENFORMAT_MIMETYPE_APP_PDF + "'";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRArtikelkommentarspr komm = (FLRArtikelkommentarspr) resultListIterator.next();
			komm.getArtikelkommentar().getFlrartikel().getC_nr();
			getArtikelkommentarFac().textAusPdfInXKommentarAktualisieren(komm.getArtikelkommentar().getI_id(),
					komm.getLocale().getC_nr());

		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void telefonnummerntabelleSynchronisieren(TheClientDto theClientDto) {

		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT p FROM FLRPartner p ";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRPartner flrPartner = (FLRPartner) resultListIterator.next();

			getPartnerFac().telefonnummerFuerTapiSynchronisieren(flrPartner.getI_id(), null, theClientDto);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<String> loseMitErledigtenAuftraegenErledigen(TheClientDto theClientDto) {
		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT la FROM FLRLosAuftrag la WHERE  la.mandant_c_nr='001' AND la.status_c_nr IN ('"
				+ FertigungFac.STATUS_ANGELEGT + "','" + FertigungFac.STATUS_IN_PRODUKTION + "','"
				+ FertigungFac.STATUS_AUSGEGEBEN + "') ORDER BY la.c_nr ASC";

		org.hibernate.Query query = session.createQuery(queryString);

		ArrayList<String> returnString = new ArrayList<String>();

		int i = 0;
		int iErledigt = 0;
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		while (resultListIterator.hasNext()) {
			FLRLosAuftrag flrLosAuftrag = (FLRLosAuftrag) resultListIterator.next();
			i++;
			if (flrLosAuftrag.getFlrauftrag() != null || flrLosAuftrag.getFlrauftragposition() != null) {

				Integer auftragIId = null;
				if (flrLosAuftrag.getFlrauftrag() != null) {

					auftragIId = flrLosAuftrag.getFlrauftrag().getI_id();

				} else if (flrLosAuftrag.getFlrauftragposition() != null) {
					auftragIId = flrLosAuftrag.getFlrauftragposition().getFlrauftrag().getI_id();
				}

				if (auftragIId != null) {

					AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(auftragIId);

					if (aDto.getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {

						boolean bZeitenVorhanden = getZeiterfassungFac()
								.sindBelegzeitenVorhanden(LocaleFac.BELEGART_LOS, flrLosAuftrag.getI_id());

						try {
							if (flrLosAuftrag.getStatus_c_nr().equals(FertigungFac.STATUS_ANGELEGT)
									&& bZeitenVorhanden == false) {
								getFertigungFac().storniereLos(flrLosAuftrag.getI_id(), false, theClientDto);
							} else {
								getFertigungFac().manuellErledigen(flrLosAuftrag.getI_id(), false, theClientDto);
							}

							iErledigt++;

							System.out.println("Lose " + flrLosAuftrag.getC_nr() + " erledigt Gesamt: " + iErledigt
									+ " Aktuelle Position " + i + " von " + results.size());

							returnString.add("Los " + flrLosAuftrag.getC_nr() + " erledigt");

						} catch (Throwable e) {

							returnString.add("Fehler bei Los " + flrLosAuftrag.getC_nr());

							System.out.println("Fehler bei Los " + flrLosAuftrag.getC_nr());
						}

					}

				}

			}

		}
		return returnString;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pj18612(TheClientDto theClientDto) {

		if (!theClientDto.getMandant().equals("002")) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PJ18612_BENUTZER_MUSS_AN_MANDANT_002_ANGEMELDET_SEIN,
					"FEHLER-> BENUTZER MUSS AN MANDANT 002 ANGEMELDET SEIN");
		}
		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT l FROM FLRLieferant l WHERE  l.mandant_c_nr='001'";

		org.hibernate.Query query = session.createQuery(queryString);
		try {
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			int u = 0;
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {
				u++;
				System.out.print(u + " von " + results.size());

				FLRLieferant o = (FLRLieferant) resultListIterator.next();

				LieferantDto lfDtoOriginal = getLieferantFac().lieferantFindByPrimaryKey(o.getI_id(), theClientDto);

				Integer lieferantIId_Original = lfDtoOriginal.getIId();

				// Wenn der Lieferant noch nicht vorhanden ist

				LieferantDto lfDto = getLieferantFac()
						.lieferantFindByiIdPartnercNrMandantOhneExc(o.getFlrpartner().getI_id(), "002", theClientDto);
				if (lfDto == null) {

					lfDtoOriginal.setMandantCNr("002");
					lfDtoOriginal.setIId(null);
					lfDtoOriginal.setKontoIIdKreditorenkonto(null);
					lfDtoOriginal.setKontoIIdWarenkonto(null);
					lfDtoOriginal.setIIdKostenstelle(null);
					lfDtoOriginal.setPersonalIIdAendern(theClientDto.getIDPersonal());
					lfDtoOriginal.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
					lfDtoOriginal.setPersonalIIdFreigabe(null);
					lfDtoOriginal.setKontoIIdKreditorenkonto(null);

					Integer zahlungszielIId_002 = getPflegeFac()
							.holeKopiertesZahlungszielPJ18612(lfDtoOriginal.getZahlungszielIId(), theClientDto);
					lfDtoOriginal.setZahlungszielIId(zahlungszielIId_002);

					Integer spediteurIId_002 = getPflegeFac()
							.holeKopiertenSpediteurPJ18612(lfDtoOriginal.getIdSpediteur(), theClientDto);
					lfDtoOriginal.setIdSpediteur(spediteurIId_002);

					Integer lieferartIId_002 = getPflegeFac()
							.holeKopierteLieferartPJ18612(lfDtoOriginal.getLieferartIId(), theClientDto);
					lfDtoOriginal.setLieferartIId(lieferartIId_002);

					lfDtoOriginal.setLagerIIdZubuchungslager(hauptlagerIId);

					lfDtoOriginal.setCKundennr(null);

					Integer mwstsatzbezIId_002 = getPflegeFac()
							.holeKopiertenMwstsatzbezPJ18612(lfDtoOriginal.getMwstsatzbezIId(), theClientDto);
					lfDtoOriginal.setMwstsatzbezIId(mwstsatzbezIId_002);

					Integer lieferantIIdNeu = getLieferantFac().createLieferant(lfDtoOriginal, theClientDto);

					// Artikellieferant anlegen

					ArtikellieferantDto[] alDtos = getArtikelFac()
							.artikellieferantFindByLieferantIId(lieferantIId_Original, theClientDto);
					for (int i = 0; i < alDtos.length; i++) {
						ArtikellieferantDto alDto = alDtos[i];
						alDto.setIId(null);
						alDto.setLieferantIId(lieferantIIdNeu);
						getArtikelFac().createArtikellieferant(alDto, theClientDto);
					}

				}

			}

			session.close();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

}
