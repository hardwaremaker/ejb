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
package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.ejb.Auftragseriennrn;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragseriennrn;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDtoAssembler;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.auftrag.service.AuftragseriennrnDto;
import com.lp.server.auftrag.service.AuftragseriennrnDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.format.LpAuftragseriennummerFormat;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.AuftragPositionNumberAdapter;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.service.Artikelset;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AuftragpositionFacBean extends Facade implements
		AuftragpositionFac, IPrimitiveSwapper {
	@PersistenceContext
	private EntityManager em;

	public Integer createAuftragposition(
			AuftragpositionDto auftragpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return createAuftragposition(auftragpositionDtoI, true, theClientDto);
	}

	private void preiseEinesArtikelsetsUpdaten(
			Integer auftragpositionIIdKopfartikel, TheClientDto theClientDto) {

		AuftragpositionDto angbeotpositionDtoKopfartikel = auftragpositionFindByPrimaryKey(auftragpositionIIdKopfartikel);

		Query query = em
				.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
		query.setParameter(1, angbeotpositionDtoKopfartikel.getIId());
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		try {
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					angbeotpositionDtoKopfartikel.getBelegIId());

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
					auftragDto.getKundeIIdRechnungsadresse(), theClientDto);

			Integer mwstsatzbezIId = getMandantFac().mwstsatzFindByPrimaryKey(
					angbeotpositionDtoKopfartikel.getMwstsatzIId(),
					theClientDto).getIIMwstsatzbezId();

			// Zuerst Gesamtwert berechnen
			BigDecimal bdMenge = angbeotpositionDtoKopfartikel.getNMenge();

			BigDecimal bdNettoeinzelpreis = angbeotpositionDtoKopfartikel
					.getNNettoeinzelpreis();

			BigDecimal bdGesamtwertposition = bdMenge
					.multiply(bdNettoeinzelpreis);

			BigDecimal bdGesamtVKwert = new BigDecimal(0);

			Iterator<?> it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Auftragposition struktur = (Auftragposition) it.next();

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								auftragDto.getKundeIIdRechnungsadresse(),

								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(),
								auftragDto.getCAuftragswaehrung(), theClientDto);
				// .getIId(), auftragDto.getWaehrungCNr(), theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper
						.getVkpreisBerechnet(vkpreisDto);

				if (kundenVKPreisDto != null
						&& kundenVKPreisDto.nettopreis != null) {
					bdGesamtVKwert = bdGesamtVKwert
							.add(kundenVKPreisDto.nettopreis.multiply(struktur
									.getNMenge()));
				}

			}

			bdGesamtVKwert = Helper.rundeKaufmaennisch(bdGesamtVKwert, 4);

			it = lieferscheinpositionDtos.iterator();

			while (it.hasNext()) {
				Auftragposition struktur = (Auftragposition) it.next();

				struktur.setNNettoeinzelpreis(new BigDecimal(0));
				struktur.setNNettogesamtpreisplusversteckteraufschlag(new BigDecimal(
						0));
				struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
						0));
				struktur.setNBruttogesamtpreis(new BigDecimal(0));

				// Mehrwertsteuersatz: Kommt immer aus dem Kopfartikel,
				// da dieser die Hauptleistung darstellt

				VkpreisfindungDto vkpreisDto = getVkPreisfindungFac()
						.verkaufspreisfindung(
								struktur.getArtikelIId(),
								auftragDto.getKundeIIdRechnungsadresse(),
								struktur.getNMenge(),
								new java.sql.Date(System.currentTimeMillis()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
								getMandantFac()
										.mwstsatzFindByMwstsatzbezIIdAktuellster(
												mwstsatzbezIId, theClientDto)
										.getIId(),
								auftragDto.getCAuftragswaehrung(), theClientDto);
				// .getIId(), auftragDto.getWaehrungCNr(), theClientDto);

				VerkaufspreisDto kundenVKPreisDto = Helper
						.getVkpreisBerechnet(vkpreisDto);

				if (kundenVKPreisDto != null
						&& kundenVKPreisDto.nettopreis != null
						&& bdGesamtVKwert.doubleValue() != 0) {
					// Preis berechnen
					BigDecimal bdAnteilVKWert = kundenVKPreisDto.nettopreis
							.multiply(struktur.getNMenge().multiply(bdMenge))
							.divide(bdGesamtVKwert, 4,
									BigDecimal.ROUND_HALF_EVEN);

					BigDecimal bdPreis = bdGesamtwertposition.multiply(
							bdAnteilVKWert).divide(
							struktur.getNMenge().multiply(bdMenge), 4,
							BigDecimal.ROUND_HALF_EVEN);

					struktur.setNNettoeinzelpreis(bdPreis);
					struktur.setNMaterialzuschlag(kundenVKPreisDto.bdMaterialzuschlag);
					struktur.setNBruttogesamtpreis(bdPreis);
					struktur.setNMwstbetrag(new BigDecimal(0));
					struktur.setNRabattbetrag(new BigDecimal(0));
					struktur.setNNettogesamtpreisplusversteckteraufschlag(bdPreis);
					struktur.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(bdPreis);
					struktur.setNNettogesamtpreis(bdPreis);

				}

			}
			updateTAendernAuftrag(auftragDto.getIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);

		}
	}

	/**
	 * Eine neue Auftragposition anlegen.
	 * <ul>
	 * <li>Der Status des Auftrags wird angepasst.
	 * <li>Ein neuer Handartikel wird angelegt.
	 * <li>Die Auftragposition wird angelegt.
	 * <li>Die Auftragreservierung wird angelegt, wenn es sich um keinen
	 * Rahmenauftrag handelt.
	 * </ul>
	 * 
	 * @param auftragpositionDtoI
	 *            die neue Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Auftragposition
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAuftragposition(
			AuftragpositionDto auftragpositionDtoI,
			boolean bArtikelSetAufloesen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		checkAuftragpositionDto(auftragpositionDtoI);

		if (auftragpositionDtoI.getPositionsartCNr().equals(
				AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME)
				&& auftragpositionFindByAuftragIIdAuftragpositionsartCNrOhneExc(
						auftragpositionDtoI.getBelegIId(),
						AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME) != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
					new Exception("Eine Position Endsumme existiert bereits."));
		}
		AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
				auftragpositionDtoI.getBelegIId());
		updateTAendernAuftrag(auftragDto.getIId(), theClientDto);

		Integer iIdAuftragposition = null;

		try {

			if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)
					|| auftragpositionDtoI.getPositionsartCNr()
							.equalsIgnoreCase(
									AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				auftragpositionDtoI
						.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				auftragpositionDtoI.setNOffeneMenge(auftragpositionDtoI
						.getNMenge());

				if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					if (auftragpositionDtoI.getNMaterialzuschlagKurs() == null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										auftragpositionDtoI.getArtikelIId(),
										theClientDto);
						if (artikelDto.getMaterialIId() != null) {

							MaterialzuschlagDto mDto = getMaterialFac()
									.getKursMaterialzuschlagDtoInZielwaehrung(
											artikelDto.getMaterialIId(),
											new java.sql.Date(auftragDto
													.getTBelegdatum().getTime()),
											auftragDto.getCAuftragswaehrung(),
											theClientDto);
							if (mDto != null) {
								auftragpositionDtoI
										.setNMaterialzuschlagKurs(mDto
												.getNZuschlag());
								auftragpositionDtoI
										.setTMaterialzuschlagDatum(mDto
												.getTGueltigab());
							}
						}
					}
				}

			}

			getAuftragFac().pruefeUndSetzeAuftragstatusBeiAenderung(
					auftragpositionDtoI.getBelegIId(), theClientDto);

			if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(auftragpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(auftragpositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(auftragpositionDtoI.getEinheitCNr());

				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								auftragpositionDtoI.getMwstsatzIId(),
								theClientDto);
				oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Handartikel anlegen
				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
						theClientDto);

				auftragpositionDtoI.setArtikelIId(iIdArtikel);

			}

			// PK generieren
			iIdAuftragposition = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_AUFTRAGPOSITION);
			auftragpositionDtoI.setIId(iIdAuftragposition);

			// Sortierung: falls nicht anders definiert, hinten dran haengen.
			if (auftragpositionDtoI.getISort() == null) {
				int iSortNeu = getMaxISort(auftragpositionDtoI.getBelegIId()) + 1;
				auftragpositionDtoI.setISort(iSortNeu);
			}

			AuftragpositionDto vorherigeAuftragpositionDtoI = null;
			try {
				int iSort = getMaxISort(auftragpositionDtoI.getBelegIId());
				if (auftragpositionDtoI.getISort() != null) {
					iSort = auftragpositionDtoI.getISort() - 1;
				}
				Query query = em
						.createNamedQuery("AuftragpositionfindByAuftragISort");
				query.setParameter(1, auftragpositionDtoI.getBelegIId());
				query.setParameter(2, iSort);
				vorherigeAuftragpositionDtoI = assembleAuftragpositionDto((Auftragposition) query
						.getSingleResult());
			} catch (NoResultException ex1) {
			}

			// eine neue Artikelreservierung wird angelegt, wenn es sich um
			// keinen Rahmenauftrag handelt
			// Wenn kein Preis mitgegeben wurde, dann wird der Preis lt.
			// VK-Preisfindung verwendet

			if (auftragpositionDtoI.getNNettoeinzelpreis() == null) {
				auftragpositionDtoI = (AuftragpositionDto) befuellePreisfelderAnhandVKPreisfindung(
						auftragpositionDtoI, auftragDto.getTBelegdatum(),
						auftragDto.getKundeIIdRechnungsadresse(),
						auftragDto.getCAuftragswaehrung(), theClientDto);
			}

			auftragpositionDtoI = (AuftragpositionDto) befuellepositionBelegpositionDtoVerkauf(
					vorherigeAuftragpositionDtoI, auftragpositionDtoI,
					theClientDto);

			istSteuersatzInPositionsartPositionGleich(auftragpositionDtoI);

			Auftragposition auftragsposition = new Auftragposition(
					iIdAuftragposition, auftragpositionDtoI.getBelegIId(),
					auftragpositionDtoI.getISort(),
					auftragpositionDtoI.getPositionsartCNr(),
					auftragpositionDtoI.getAuftragpositionstatusCNr(),
					auftragpositionDtoI.getBNettopreisuebersteuert());
			auftragsposition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
			em.persist(auftragsposition);
			em.flush();

			setAuftragpositionFromAuftragpositionDto(auftragsposition,
					auftragpositionDtoI);

			if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				AuftragseriennrnDto auftragseriennrnDto = null;
				if (auftragpositionDtoI.getCSeriennrn() != null) {
					for (int i = 0; i < auftragpositionDtoI.getCSeriennrn().length; i++) {
						auftragseriennrnDto = new AuftragseriennrnDto();
						auftragseriennrnDto.setArtikelIId(auftragpositionDtoI
								.getArtikelIId());
						auftragseriennrnDto
								.setAuftragpositionIId(auftragpositionDtoI
										.getIId());
						auftragseriennrnDto.setCSeriennr(auftragpositionDtoI
								.getCSeriennrn()[i]);
						Integer vNr = getVersionAuftragseriennrn(
								auftragseriennrnDto.getCSeriennr(),
								auftragseriennrnDto.getArtikelIId(),
								theClientDto);
						if (vNr != null) {
							auftragseriennrnDto.setIVersionNr(vNr);
							auftragseriennrnDto
									.setPersonalIIdAnlegen(theClientDto
											.getIDPersonal());
							auftragseriennrnDto.setTAnlegen(new Timestamp(
									System.currentTimeMillis()));
						}
						createAuftragseriennrn(auftragseriennrnDto,
								theClientDto);
					}
				}

			}

			befuelleZusaetzlichePreisfelder(iIdAuftragposition);

			pruefePflichtfelderBelegposition(auftragpositionDtoI, theClientDto);

			// PJ18317
			if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

				// ev. VK-Preis rueckpflegen
				nettogesamtpreisAlsVKPreisbasisRueckpflegen(
						auftragpositionFindByPrimaryKey(iIdAuftragposition),
						theClientDto);
			}

			if (!auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)
					|| !auftragDto.getAuftragartCNr().equals(
							AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
				gleicheAuftragreservierungAnOffeneMengeAn(auftragDto,
						auftragpositionDtoI);
			}
			if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				auftragsposition.setNOffenerahmenmenge(auftragpositionDtoI
						.getNMenge());
				auftragsposition.setTUebersteuerterliefertermin(auftragDto
						.getDLiefertermin());
			}

			sortierungAnpassenInBezugAufEndsumme(
					auftragpositionDtoI.getBelegIId(), theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikle ebenfalls
			// buchen:
			if (bArtikelSetAufloesen == true
					&& auftragpositionDtoI.getArtikelIId() != null) {

				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								auftragpositionDtoI.getArtikelIId(),
								theClientDto);
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {

					AuftragpositionDto auftragpositionDtoKopfartikel = auftragpositionFindByPrimaryKey(auftragpositionDtoI
							.getIId());

					List<?> m = null;
					try {
						m = getStuecklisteFac()
								.getStrukturDatenEinerStueckliste(
										stklDto.getIId(),
										theClientDto,
										StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
										0, null, false, false,
										auftragpositionDtoI.getNMenge(), null,
										true);
					} catch (RemoteException ex4) {
						throwEJBExceptionLPRespectOld(ex4);
					}

					Iterator it = m.listIterator();

					while (it.hasNext()) {
						StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
								.next();
						StuecklistepositionDto position = struktur
								.getStuecklistepositionDto();

						auftragpositionDtoI.setNEinzelpreis(new BigDecimal(0));
						auftragpositionDtoI
								.setNNettoeinzelpreis(new BigDecimal(0));
						auftragpositionDtoI.setNMwstbetrag(new BigDecimal(0));
						auftragpositionDtoI.setNRabattbetrag(new BigDecimal(0));
						auftragpositionDtoI.setFZusatzrabattsatz(0D);
						auftragpositionDtoI.setFRabattsatz(0D);
						auftragpositionDtoI
								.setNNettoeinzelpreisplusversteckteraufschlag(new BigDecimal(
										0));
						auftragpositionDtoI
								.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(new BigDecimal(
										0));
						auftragpositionDtoI
								.setNBruttoeinzelpreis(new BigDecimal(0));

						auftragpositionDtoI.setNMenge(Helper
								.rundeKaufmaennisch(
										position.getNZielmenge().multiply(
												auftragpositionDtoKopfartikel
														.getNMenge()), 4));

						auftragpositionDtoI.setArtikelIId(position
								.getArtikelIId());
						auftragpositionDtoI.setEinheitCNr(position
								.getEinheitCNr());
						auftragpositionDtoI
								.setPositioniIdArtikelset(auftragpositionDtoKopfartikel
										.getIId());
						auftragpositionDtoI.setIId(null);

						int iSort = auftragpositionDtoI.getISort() + 1;

						sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
								auftragpositionDtoI.getBelegIId(), iSort);

						auftragpositionDtoI.setISort(iSort);
						createAuftragposition(auftragpositionDtoI, false,
								theClientDto);

					}
					preiseEinesArtikelsetsUpdaten(
							auftragpositionDtoKopfartikel.getIId(),
							theClientDto);
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return iIdAuftragposition;
	}

	public void loescheAuftragseriennrnEinesAuftragposition(
			Integer iIdAuftragposition, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String hqlDelete = "delete FLRAuftragseriennrn where auftragposition_i_id = "
					+ iIdAuftragposition;
			session.createQuery(hqlDelete).executeUpdate();
		} finally {
			closeSession(session);
		}
	}

	private void istSteuersatzInPositionsartPositionGleich(
			AuftragpositionDto agposDto) {
		if (agposDto.getTypCNr() != null
				&& (agposDto.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1) || agposDto
						.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2))
				&& agposDto.getPositioniId() != null) {
			AuftragpositionDto[] dtos = auftragpositionFindByPositionIId(agposDto
					.getPositioniId());
			for (int i = 0; i < dtos.length; i++) {
				if (agposDto.getMwstsatzIId() != null) {
					if (dtos[i].getMwstsatzIId() != null) {
						if (!agposDto.getMwstsatzIId().equals(
								dtos[i].getMwstsatzIId())) {
							// MWST-Saetze innerhalb "Position" muessen
							// immer gleich sein
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH,
									"");
						}
					}
				}
			}
		}
	}

	/**
	 * Eine bestehende Auftragposition loeschen.
	 * <ul>
	 * <li>Der Status des Auftrags wird angepasst.
	 * <li>Die Auftragreservierung wird geloescht.
	 * <li>Die Auftragposition wird geloescht, die Sortierung wird angepasst.
	 * <li>Wenn die Auftragposition angebotsbezogen ist, wird der Bezug
	 * behandelt.
	 * </ul>
	 * 
	 * @param auftragpositionDtoI
	 *            die Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAuftragposition(AuftragpositionDto auftragpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAuftragpositionDto(auftragpositionDtoI);

		try {
			getAuftragFac().pruefeUndSetzeAuftragstatusBeiAenderung(
					auftragpositionDtoI.getBelegIId(), theClientDto);

			// zuerst Artikelreservierung loeschen
			if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				ArtikelreservierungDto oReservierungDto = getReservierungFac()
						.getArtikelreservierung(LocaleFac.BELEGART_AUFTRAG,
								auftragpositionDtoI.getIId());

				if (oReservierungDto != null) {
					getReservierungFac()
							.removeArtikelreservierung(
									com.lp.server.system.service.LocaleFac.BELEGART_AUFTRAG,
									auftragpositionDtoI.getIId());
				}
			}

			Auftragposition toRemove = em.find(Auftragposition.class,
					auftragpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (auftragpositionDtoI.getPositioniIdArtikelset() == null) {

				Query query = em
						.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
				query.setParameter(1, auftragpositionDtoI.getIId());
				Collection<?> angebotpositionDtos = query.getResultList();
				AuftragpositionDto[] zugehoerigeLSPosDtos = assembleAuftragpositionDtos(angebotpositionDtos);

				for (int i = 0; i < zugehoerigeLSPosDtos.length; i++) {
					removeAuftragposition(zugehoerigeLSPosDtos[i], theClientDto);
				}
			}

			try {
				Query query = em
						.createNamedQuery("AuftragseriennrnfindByAuftragpositionIId");
				query.setParameter(1, auftragpositionDtoI.getIId());
				Collection<?> collsernrn = query.getResultList();
				if (collsernrn != null) {
					loescheAuftragseriennrnEinesAuftragposition(
							auftragpositionDtoI.getIId(), theClientDto);
				}
				em.remove(toRemove);
				em.flush();

				if (auftragpositionDtoI.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(
							auftragpositionDtoI.getPositioniIdArtikelset(),
							theClientDto);
				}

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}

			// poseinfuegen: 1 die Sortierung muss angepasst werden
			sortierungAnpassenBeiLoeschenEinerPosition(
					auftragpositionDtoI.getBelegIId(), auftragpositionDtoI
							.getISort().intValue());

			// wenn der Auftrag aus einem Angebot erstellt wurde und keine
			// Position mehr enthaelt
			// @todo wenn keine Position aus dem Angebot mehr enthalten ist
			// UW->WH PJ 3819
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragpositionDtoI.getBelegIId());

			if (auftragDto.getAngebotIId() != null
					&& auftragpositionDtoI.getNMenge() != null) {
				if (getAnzahlMengenbehafteteAuftragpositionen(
						auftragpositionDtoI.getBelegIId(), theClientDto) == 0) {
					AngebotDto angebotDto = getAngebotFac()
							.angebotFindByPrimaryKey(
									auftragDto.getAngebotIId(), theClientDto);

					if (angebotDto.getStatusCNr().equals(
							AngebotServiceFac.ANGEBOTSTATUS_ERLEDIGT)) {
						// den Status des Angebots auf offen zuruecksetzen
						getAngebotFac().erledigungAufheben(
								auftragDto.getAngebotIId(), theClientDto);
					}

					// die Referenz des Auftrags auf das Angebot loeschen
					auftragDto.setAngebotIId(null);

					getAuftragFac().updateAuftrag(auftragDto, null,
							theClientDto);
				}
			}
			updateTAendernAuftrag(auftragDto.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception(t));
		}
	}

	public void manuellErledigen(Integer iIdpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}

		try {
			Auftragposition oPos = em.find(Auftragposition.class, iIdpositionI);
			if (oPos == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT);
			getReservierungFac().removeArtikelreservierung(
					LocaleFac.BELEGART_AUFTRAG, oPos.getIId());
			getAuftragFac().setzeAuftragstatusAufgrundAuftragpositionstati(
					oPos.getAuftragIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void manuellErledigungAufgeben(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdPositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdBestellpositionI == null"));
		}
		try {
			Auftragposition oPos = em.find(Auftragposition.class, iIdPositionI);
			if (oPos == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			// SP2356 Reservierung natuerlich nur bei Ident-Position
			if (oPos.getAuftragpositionartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
					&& oPos.getArtikelIId() != null) {

				ArtikelreservierungDto reservierungDto = new ArtikelreservierungDto();
				reservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				reservierungDto.setIBelegartpositionid(oPos.getIId());
				reservierungDto.setArtikelIId(oPos.getArtikelIId());
				reservierungDto.setTLiefertermin(oPos
						.getTUebersteuerterliefertermin());
				reservierungDto.setNMenge(oPos.getNOffeneMenge());
				getReservierungFac().createArtikelreservierung(reservierungDto);
			}

			// Wenn keine Mengenbehaftete position, dann auf OFFEN setzen

			if (oPos.getNMenge() == null) {
				oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);
			} else {

				if (oPos.getNMenge().equals(oPos.getNOffeneMenge())) {
					oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_OFFEN); // absolute
					// Menge
					// buchen
				} else {
					oPos.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
				}
			}
			getAuftragFac().setzeAuftragstatusAufgrundAuftragpositionstati(
					oPos.getAuftragIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	protected void updateAuftragposition(
			AuftragpositionDto auftragpositionDtoI,
			boolean recalculateSetPrice, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkAuftragpositionDto(auftragpositionDtoI);
		pruefePflichtfelderBelegposition(auftragpositionDtoI, theClientDto);

		AuftragpositionDto savedAuftragpositionDto = auftragpositionFindByPrimaryKey(auftragpositionDtoI
				.getIId());

		try {
			// die Artikelreservierung wird aktualisieren, wenn es sich um
			// keinen Rahmenauftrag handelt
			AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
					auftragpositionDtoI.getBelegIId());
			updateTAendernAuftrag(auftragDto.getIId(), theClientDto);
			if (auftragpositionDtoI.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)
					|| auftragpositionDtoI.getPositionsartCNr()
							.equalsIgnoreCase(
									AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				LieferscheinpositionDto[] lsposDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByAuftragpositionIId(
								auftragpositionDtoI.getIId(), theClientDto);
				if (lsposDto.length != 0) {
					BigDecimal lsposMenge = auftragpositionDtoI.getNMenge();
					for (int i = 0; i < lsposDto.length; i++) {
						lsposMenge = lsposMenge.subtract(lsposDto[i]
								.getNMenge());
					}
					auftragpositionDtoI.setNOffeneMenge(lsposMenge);
				} else {
					// die offene Menge muss angepasst werden
					auftragpositionDtoI.setNOffeneMenge(auftragpositionDtoI
							.getNMenge());
				}
			}

			if (auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				updateRahmenauftragPosition(auftragpositionDtoI, auftragDto,
						theClientDto);
			} else {
				updateAuftragpositionOhneWeitereAktion(auftragpositionDtoI,
						theClientDto);
			}
			if (!auftragDto.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				gleicheAuftragreservierungAnOffeneMengeAn(auftragDto,
						auftragpositionDtoI);
			}

			// spezielle Behandlung fuer eine Handeingabeposition
			if (auftragpositionDtoI.getPositionsartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								auftragpositionDtoI.getArtikelIId(),
								theClientDto);

				ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();

				if (oArtikelsprDto == null) {
					oArtikelsprDto = new ArtikelsprDto();
				}

				oArtikelsprDto.setCBez(auftragpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(auftragpositionDtoI.getCZusatzbez());

				artikelDto.setArtikelsprDto(oArtikelsprDto);
				artikelDto.setEinheitCNr(auftragpositionDtoI.getEinheitCNr());

				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(
								auftragpositionDtoI.getMwstsatzIId(),
								theClientDto);
				artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Artikel speichern
				getArtikelFac().updateArtikel(artikelDto, theClientDto);
			} else if (auftragpositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(
							AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				if (auftragpositionDtoI.getCSeriennrn() != null) {
					String srnnrnDB = getSeriennummmern(
							auftragpositionDtoI.getIId(), theClientDto);
					String srnnrnClient = Helper
							.erzeugeStringAusStringArray(auftragpositionDtoI
									.getCSeriennrn());
					if (!srnnrnClient.equals(srnnrnDB)) {
						loescheAuftragseriennrnEinesAuftragposition(
								auftragpositionDtoI.getIId(), theClientDto);
						AuftragseriennrnDto auftragseriennrnDto = null;
						for (int i = 0; i < auftragpositionDtoI.getCSeriennrn().length; i++) {
							auftragseriennrnDto = new AuftragseriennrnDto();
							auftragseriennrnDto
									.setArtikelIId(auftragpositionDtoI
											.getArtikelIId());
							auftragseriennrnDto
									.setAuftragpositionIId(auftragpositionDtoI
											.getIId());
							auftragseriennrnDto
									.setCSeriennr(auftragpositionDtoI
											.getCSeriennrn()[i]);
							createAuftragseriennrn(auftragseriennrnDto,
									theClientDto);
						}
					}
				}

			} else // PJ 09/0014648 spezielle Behandlung fuer eine Position
			if (auftragpositionDtoI.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_POSITION)) {
				try {
					Query query = em
							.createNamedQuery("AuftragpositionfindByPositionIId");
					query.setParameter(1, auftragpositionDtoI.getIId());
					Collection<?> cl = query.getResultList();
					Iterator<?> iterator = cl.iterator();
					while (iterator.hasNext()) {
						Auftragposition position = (Auftragposition) iterator
								.next();
						position.setNMenge(auftragpositionDtoI.getNMenge());
					}
				} catch (NoResultException ex) {

				}
			}
			updateOffeneMengeAuftragposition(auftragpositionDtoI.getIId(),
					theClientDto);
			istSteuersatzInPositionsartPositionGleich(auftragpositionDtoI);

			Integer auftragspositionIIdKopfartikel = gehoertZuArtikelset(auftragpositionDtoI
					.getIId());
			if (auftragspositionIIdKopfartikel != null) {

				updateArtikelsetMengen(savedAuftragpositionDto,
						auftragpositionDtoI, theClientDto);

				if (recalculateSetPrice) {
					preiseEinesArtikelsetsUpdaten(
							auftragspositionIIdKopfartikel, theClientDto);
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine bestehende Auftragposition aktualisieren.
	 * <ul>
	 * <li>Der Status des Auftrags wird angepasst.
	 * <li>Die offene Menge der Auftragposition anpassen.
	 * <li>Die Auftragposition aktualisieren.
	 * <li>Die Auftragreservierung an die aktuelle Auftragposition anpassen.
	 * <li>Ein Handartikel wird aktualisiert.
	 * </ul>
	 * 
	 * @param auftragpositionDtoI
	 *            die Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateAuftragposition(AuftragpositionDto auftragpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		updateAuftragposition(auftragpositionDtoI, true, theClientDto);
	}

	private void updateTAendernAuftrag(Integer iid, TheClientDto theClientDto) {
		Auftrag auftrag = em.find(Auftrag.class, iid);
		auftrag.setPersonalIIdAendern(theClientDto.getIDPersonal());
		auftrag.setTAendern(getTimestamp());
	}

	private void updateTAendernAuftragViaPosIId(Integer iid,
			TheClientDto theClientDto) {
		Auftragposition pos = em.find(Auftragposition.class, iid);
		updateTAendernAuftrag(pos.getAuftragIId(), theClientDto);
	}

	private void updateArtikelsetMengen(AuftragpositionDto oldPositionDto,
			AuftragpositionDto newPositionDto, TheClientDto theClientDto) {
		if (newPositionDto.getNMenge() == null)
			return;
		if (newPositionDto.getNMenge().compareTo(oldPositionDto.getNMenge()) == 0)
			return;

		if (newPositionDto.getArtikelIId() == null)
			return;

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
						newPositionDto.getArtikelIId(), theClientDto);
		if (stklDto == null)
			return;
		if (!stklDto.getStuecklisteartCNr().equals(
				StuecklisteFac.STUECKLISTEART_SETARTIKEL))
			return;

		// Wir haben einen Setartikel-Kopf und es wurde die Menge geaendert ->
		// die Mengen des Sets anpassen
		Query query = em
				.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
		query.setParameter(1, newPositionDto.getIId());
		Collection<Auftragposition> auftragpositions = query.getResultList();

		BigDecimal oldHeadAmount = oldPositionDto.getNMenge();
		BigDecimal newHeadAmount = newPositionDto.getNMenge();

		for (Auftragposition auftragposition : auftragpositions) {
			AuftragpositionDto positionDto = assembleAuftragpositionDto(auftragposition);

			if (positionDto.getNMenge() != null) {
				positionDto.setNMenge(positionDto.getNMenge()
						.divide(oldHeadAmount).multiply(newHeadAmount));
				updateAuftragposition(positionDto, false, theClientDto);
			}
		}
		updateTAendernAuftrag(newPositionDto.getBelegIId(), theClientDto);
	}

	public Integer gehoertZuArtikelset(Integer auftragpositionIId) {

		Auftragposition oPosition1 = em.find(Auftragposition.class,
				auftragpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return oPosition1.getPositionIIdArtikelset();
		}

		Query query = em
				.createNamedQuery("AuftragpositionfindByPositionIIdArtikelset");
		query.setParameter(1, auftragpositionIId);
		Collection<?> lieferscheinpositionDtos = query.getResultList();

		if (lieferscheinpositionDtos != null
				&& lieferscheinpositionDtos.size() > 0) {
			return auftragpositionIId;
		} else {
			return null;
		}

	}

	private void nettogesamtpreisAlsVKPreisbasisRueckpflegen(
			AuftragpositionDto auftragpositionDtoI, TheClientDto theClientDto) {
		if (auftragpositionDtoI
				.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() != null
				&& auftragpositionDtoI.getArtikelIId() != null) {

			ParametermandantDto parametermandantDto = null;
			try {
				parametermandantDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VERKAUFSPREIS_RUECKPFLEGE);
			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			boolean b = (Boolean) parametermandantDto.getCWertAsObject();
			if (b == true) {

				BigDecimal preis = auftragpositionDtoI
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(
						auftragpositionDtoI.getBelegIId());
				Query query = em
						.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, auftragpositionDtoI.getArtikelIId());
				query.setParameter(3,
						Helper.cutTimestamp(aDto.getTBelegdatum()));

				try {
					preis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							preis,
							aDto.getCAuftragswaehrung(),
							theClientDto.getSMandantenwaehrung(),
							new Date(Helper.cutTimestamp(aDto.getTBelegdatum())
									.getTime()), theClientDto);
				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				try {

					VkPreisfindungEinzelverkaufspreis vkpreis = (VkPreisfindungEinzelverkaufspreis) query
							.getSingleResult();
					vkpreis.setNVerkaufspreisbasis(preis);
				} catch (NoResultException ex) {
					VkPreisfindungEinzelverkaufspreisDto preisNew = new VkPreisfindungEinzelverkaufspreisDto();
					preisNew.setArtikelIId(auftragpositionDtoI.getArtikelIId());
					preisNew.setTVerkaufspreisbasisgueltigab(new Date(Helper
							.cutTimestamp(aDto.getTBelegdatum()).getTime()));
					preisNew.setMandantCNr(theClientDto.getMandant());
					preisNew.setNVerkaufspreisbasis(preis);
					try {
						getVkPreisfindungFac()
								.createVkPreisfindungEinzelverkaufspreis(
										preisNew, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

			}
		}
	}

	/**
	 * Aktualisieren einer Auftragposition ohne weitere Aktion. TAendern wird
	 * dabei aktualisiert.
	 * 
	 * @param auftragpositionDtoI
	 *            die Auftragposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAuftragpositionOhneWeitereAktion(
			AuftragpositionDto auftragpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAuftragpositionDto(auftragpositionDtoI);

		Auftragposition auftragsposition = em.find(Auftragposition.class,
				auftragpositionDtoI.getIId());
		if (auftragsposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setAuftragpositionFromAuftragpositionDto(auftragsposition,
				auftragpositionDtoI);

		befuelleZusaetzlichePreisfelder(auftragpositionDtoI.getIId());
		updateTAendernAuftrag(auftragpositionDtoI.getBelegIId(), theClientDto);

		// PJ18317
		if (auftragpositionDtoI.getPositionsartCNr().equalsIgnoreCase(
				AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {

			// ev. VK-Preis rueckpflegen
			nettogesamtpreisAlsVKPreisbasisRueckpflegen(auftragpositionDtoI,
					theClientDto);
		}

	}

	private void updateRahmenauftragPosition(
			AuftragpositionDto auftragpositionDtoI, AuftragDto auftragDto,
			TheClientDto theClientDto) {
		Auftragposition auftragsposition = em.find(Auftragposition.class,
				auftragpositionDtoI.getIId());
		if (auftragsposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// auftragpositionDtoI.setNOffeneRahmenMenge(auftragpositionDtoI.getNMenge
		// ());
		auftragsposition.setTUebersteuerterliefertermin(auftragDto
				.getDLiefertermin());
		if (auftragsposition.getArtikelIId() != null) {
			if (auftragsposition.getArtikelIId().equals(
					auftragpositionDtoI.getArtikelIId())) {
				BigDecimal bdAbgerufen = new BigDecimal(0);
				// SK Hier koennte man optmieren und die Positionen nicht holen.
				// ACHTUNG aenderung wuerde sich auch auf Storno auswirken
				AuftragpositionDto[] abrufPos = auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
						auftragpositionDtoI.getIId(), theClientDto);
				for (int i = 0; i < abrufPos.length; i++) {
					if (!AuftragServiceFac.AUFTRAGPOSITIONSTATUS_STORNIERT
							.equals(abrufPos[i].getAuftragpositionstatusCNr())) {
						if (abrufPos[i].getNMenge() != null) {
							bdAbgerufen = bdAbgerufen.add(abrufPos[i]
									.getNMenge());
						}
					}
				}
				BigDecimal bdOffeneMenge = new BigDecimal(0);
				if (auftragpositionDtoI.getNMenge() != null) {
					bdOffeneMenge = auftragpositionDtoI.getNMenge();
				}
				bdOffeneMenge = bdOffeneMenge.subtract(bdAbgerufen);
				auftragpositionDtoI.setNOffeneMenge(bdOffeneMenge);
				if (bdOffeneMenge.compareTo(new BigDecimal(0)) > -1) {
					auftragpositionDtoI.setNOffeneRahmenMenge(bdOffeneMenge);
					setAuftragpositionFromAuftragpositionDto(auftragsposition,
							auftragpositionDtoI);

					befuelleZusaetzlichePreisfelder(auftragpositionDtoI
							.getIId());
				} else {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_MENGENAENDERUNG_UNTER_ABGERUFENE_MENGE_NICHT_ERLAUBT,
							new Exception(
									"Mengenaenderung kleiner als bereits abgerufene Menge ist nicht erlaubt"));
				}

			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_ARTIKELAENDERUNG_BEI_RAHMENPOSUPDATE_NICHT_ERLAUBT,
						new Exception(
								"ArtikelIId darf bei Update eines Rahmens nicht geaendert werden"));
			}
		} else {
			auftragsposition.setXTextinhalt(auftragpositionDtoI
					.getXTextinhalt());
			em.merge(auftragsposition);
			em.flush();
		}

	}

	/**
	 * Eine Auftragposition ueber ihren PK holen.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK der Auftragposition
	 * @return AuftragpositionDto das Dto die Position
	 * @throws EJBExceptionLP
	 */
	public AuftragpositionDto auftragpositionFindByPrimaryKey(
			Integer iIdAuftragpositionI) throws EJBExceptionLP {

		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}

		AuftragpositionDto auftragposDto = auftragpositionFindByPrimaryKeyOhneExc(iIdAuftragpositionI);

		if (auftragposDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return auftragposDto;
	}

	/**
	 * Eine Auftragposition ueber ihren PK holen. <br>
	 * Diese Methode wirft keine Exceptions.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK der Position
	 * @return AuftragpositionDto die Position
	 */
	public AuftragpositionDto auftragpositionFindByPrimaryKeyOhneExc(
			Integer iIdAuftragpositionI) {

		if (iIdAuftragpositionI == null)
			return null;

		Auftragposition auftragposition = em.find(Auftragposition.class,
				iIdAuftragpositionI);
		if (auftragposition == null)
			return null;

		return assembleAuftragspositionDto(auftragposition);
	}

	/**
	 * Alle Auftragpositionen eines bestimmten Auftrags holen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return AuftragpositionDto[] die Positionen
	 * @throws EJBExceptionLP
	 *             Ausanahme
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftrag(Integer iIdAuftragI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> collPositionen = query.getResultList();
		return AuftragpositionDtoAssembler.createDtos(collPositionen);
	}

	public Collection<AuftragpositionDto> auftragpositionFindByAuftragList(
			Integer iIdAuftragI) throws EJBExceptionLP {
		HvTypedQuery<Auftragposition> query = new HvTypedQuery<Auftragposition>(
				em.createNamedQuery("AuftragpositionfindByAuftrag"));
		query.setParameter(1, iIdAuftragI);
		return AuftragpositionDtoAssembler.createDtosAsList(query
				.getResultList());
	}

	public AuftragpositionDto[] auftragpositionFindByPositionIId(
			Integer positionIId) throws EJBExceptionLP {
		Query query = em.createNamedQuery("AuftragpositionfindByPositionIId");
		query.setParameter(1, positionIId);
		Collection<?> collPositionen = query.getResultList();
		return AuftragpositionDtoAssembler.createDtos(collPositionen);
	}

	public AuftragpositionDto[] auftragpositionFindByAuftragIIdNMengeNotNull(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(iIdAuftragI); // UW->MB kann sie jetzt //UW->MB diese
		// Methode sollte den User koennen

		AuftragpositionDto[] aAuftragpositionDto = null;

		// try {
		Query query = em
				.createNamedQuery("AuftragpositionfindByAuftragIIdNMengeNotNull");
		query.setParameter(1, iIdAuftragI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aAuftragpositionDto = AuftragpositionDtoAssembler.createDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAuftragpositionDto;
	}

	/**
	 * Zu einem bestimmten Auftrag alle Positionen holen, bei denen die offene
	 * Menge nicht NULL und nicht 0 ist.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @return AuftragpositionDto[]
	 * @throws EJBExceptionLP
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftragOffeneMenge(
			Integer iIdAuftragI) throws EJBExceptionLP {
		final String METHOD_NAME = "auftragpositionFindByAuftragOffeneMenge";
		myLogger.entry();

		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		AuftragpositionDto[] allePositionen = null;

		// try {
		Query query = em
				.createNamedQuery("AuftragpositionfindByAuftragOffeneMenge");
		query.setParameter(1, iIdAuftragI);
		Collection<?> collPositionen = query.getResultList();
		// if (collPositionen.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		allePositionen = AuftragpositionDtoAssembler.createDtos(collPositionen);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return allePositionen;
	}

	public AuftragpositionDto[] auftragpositionFindByAuftragPositiveMenge(
			Integer iIdAuftragI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "auftragpositionFindByAuftragPositiveMenge";
		myLogger.entry();
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}
		AuftragpositionDto[] allePositionen = null;
		try {
			Query query = em
					.createNamedQuery("AuftragpositionfindByAuftragPositiveMenge");
			query.setParameter(1, iIdAuftragI);
			Collection<?> collPositionen = query.getResultList();
			allePositionen = AuftragpositionDtoAssembler
					.createDtos(collPositionen);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return allePositionen;
	}

	/**
	 * Die Auftragposition an einer bestimmten Position im Auftrag bestimmen.
	 * 
	 * @param iIdAuftrag
	 *            PK des Auftrags
	 * @param iSort
	 *            die Position der Position
	 * @return AuftragpositionDto die Position
	 */
	public AuftragpositionDto auftragpositionFindByAuftragISort(
			Integer iIdAuftrag, Integer iSort) {
		final String METHOD_NAME = "auftragpositionFindByAuftragISort";
		myLogger.entry();

		if (iIdAuftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftrag == null"));
		}

		if (iSort == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iSort == null"));
		}

		AuftragpositionDto oPosition = null;

		// try {
		Query query = em.createNamedQuery("AuftragpositionfindByAuftragISort");
		query.setParameter(1, iIdAuftrag);
		query.setParameter(2, iSort);
		// @todo getSingleResult oder getResultList ?
		Auftragposition position = (Auftragposition) query.getSingleResult();
		if (position == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		oPosition = AuftragpositionDtoAssembler.createDto(position);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return oPosition;
	}

	/**
	 * Alle Auftragpositionen suchen, die zu einem bestimmten Auftrag gehoeren
	 * und eine bestimmte Rahmenposition referenzieren.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iIdRahmenpositionI
	 *            PK der Rahmenposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragpositionDto die Auftragposition, null moeglich
	 */
	public AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionIIdRahmenpositionOhneExc(
			Integer iIdAuftragI, Integer iIdRahmenpositionI,
			TheClientDto theClientDto) {

		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI"));
		}
		myLogger.logData(iIdAuftragI);
		checkAuftragpositionIId(iIdRahmenpositionI);
		AuftragpositionDto auftragpositionDto = null;
		try {
			Query query = em
					.createNamedQuery("AuftragpositionfindByAuftragIIdAuftragpositionIIdRahmenposition");
			query.setParameter(1, iIdAuftragI);
			query.setParameter(2, iIdRahmenpositionI);
			Auftragposition auftragposition = (Auftragposition) query
					.getSingleResult();
			auftragpositionDto = assembleAuftragpositionDto(auftragposition);
		} catch (NoResultException ex) {
			myLogger.warn("iIdAuftragI=" + iIdAuftragI
					+ ", iIdRahmenpositionI=" + iIdRahmenpositionI);
		}

		return auftragpositionDto;
	}

	/**
	 * Alle Auftragpositionen suchen, die eine bestimmte Rahmenposition
	 * referenzieren.
	 * 
	 * @param iIdRahmenpositionI
	 *            PK der Rahmenposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return AuftragpositionDto die Auftragposition, null moeglich
	 */
	public AuftragpositionDto[] auftragpositionFindByAuftragpositionIIdRahmenpositionOhneExc(
			Integer iIdRahmenpositionI, TheClientDto theClientDto) {
		checkAuftragpositionIId(iIdRahmenpositionI);
		AuftragpositionDto[] aAuftragpositionDto = null;
		Query query = em
				.createNamedQuery("AuftragpositionfindByAuftragpositionIIdRahmenposition");
		query.setParameter(1, iIdRahmenpositionI);
		Collection<?> cl = query.getResultList();
		aAuftragpositionDto = assembleAuftragpositionDtos(cl);
		return aAuftragpositionDto;
	}

	private void setAuftragpositionFromAuftragpositionDto(
			Auftragposition auftragposition,
			AuftragpositionDto auftragpositionDto) {
		if (auftragpositionDto.getBelegIId() != null) {
			auftragposition.setAuftragIId(auftragpositionDto.getBelegIId());
		}

		if (auftragpositionDto.getISort() != null) {
			auftragposition.setISort(auftragpositionDto.getISort());
		}

		if (auftragpositionDto.getPositionsartCNr() != null) {
			auftragposition.setAuftragpositionartCNr(auftragpositionDto
					.getPositionsartCNr());
		}
		if (auftragpositionDto.getAuftragpositionstatusCNr() != null) {
			auftragposition.setAuftragpositionstatusCNr(auftragpositionDto
					.getAuftragpositionstatusCNr());
		}
		if (auftragpositionDto.getArtikelIId() != null) {
			auftragposition.setArtikelIId(auftragpositionDto.getArtikelIId());
		}

		auftragposition.setCBezeichnung(auftragpositionDto.getCBez());
		auftragposition.setCZusatzbezeichnung(auftragpositionDto
				.getCZusatzbez());
		auftragposition.setBArtikelbezeichnungUebersteuert(auftragpositionDto
				.getBArtikelbezeichnunguebersteuert());

		auftragposition.setXTextinhalt(auftragpositionDto.getXTextinhalt());
		auftragposition.setMediastandardIId(auftragpositionDto
				.getMediastandardIId());

		if (auftragpositionDto.getNOffeneMenge() != null) {
			auftragposition.setNOffeneMenge(auftragpositionDto
					.getNOffeneMenge());
		}
		if (auftragpositionDto.getNMenge() != null) {
			auftragposition.setNMenge(auftragpositionDto.getNMenge());
		}
		if (auftragpositionDto.getNOffeneRahmenMenge() != null) {
			auftragposition.setNOffenerahmenmenge(auftragpositionDto
					.getNOffeneRahmenMenge());
		}
		if (auftragpositionDto.getEinheitCNr() != null) {
			auftragposition.setEinheitCNr(auftragpositionDto.getEinheitCNr());
		}
		auftragposition.setFRabattsatz(auftragpositionDto.getFRabattsatz());
		if (auftragpositionDto.getBRabattsatzuebersteuert() != null) {
			auftragposition.setBRabattsatzuebersteuert(auftragpositionDto
					.getBRabattsatzuebersteuert());
		}
		auftragposition.setFZusatzrabattsatz(auftragpositionDto
				.getFZusatzrabattsatz());
		auftragposition.setMwstsatzIId(auftragpositionDto.getMwstsatzIId());
		if (auftragpositionDto.getBMwstsatzuebersteuert() != null) {
			auftragposition.setBMwstsatzuebersteuert(auftragpositionDto
					.getBMwstsatzuebersteuert());
		}
		auftragposition.setBNettopreisuebersteuert(auftragpositionDto
				.getBNettopreisuebersteuert());
		auftragposition.setNNettoeinzelpreis(auftragpositionDto
				.getNEinzelpreis());
		auftragposition
				.setNNettoeinzelpreisplusversteckteraufschlag(auftragpositionDto
						.getNEinzelpreisplusversteckteraufschlag());
		auftragposition.setNRabattbetrag(auftragpositionDto.getNRabattbetrag());
		auftragposition
				.setNNettogesamtpreisplusversteckteraufschlag(auftragpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlag());
		auftragposition
				.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(auftragpositionDto
						.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		auftragposition.setNNettogesamtpreis(auftragpositionDto
				.getNNettoeinzelpreis());
		auftragposition.setNMwstbetrag(auftragpositionDto.getNMwstbetrag());
		auftragposition.setNBruttogesamtpreis(auftragpositionDto
				.getNBruttoeinzelpreis());
		if (auftragpositionDto.getTUebersteuerbarerLiefertermin() != null) {
			auftragposition.setTUebersteuerterliefertermin(auftragpositionDto
					.getTUebersteuerbarerLiefertermin());
		}
		if (auftragpositionDto.getBDrucken() != null) {
			auftragposition.setBDrucken(auftragpositionDto.getBDrucken());
		}
		auftragposition.setAuftragpositionIIdRahmenposition(auftragpositionDto
				.getAuftragpositionIIdRahmenposition());
		auftragposition.setCSeriennrchargennr(auftragpositionDto
				.getCSeriennrchargennr());

		if (auftragpositionDto.getPositioniId() != null) {
			auftragposition.setPositionIId(auftragpositionDto.getPositioniId());
		}
		if (auftragpositionDto.getTypCNr() != null) {
			auftragposition.setTypCNr(auftragpositionDto.getTypCNr());
		}
		if (auftragpositionDto.getBdEinkaufpreis() != null) {
			auftragposition.setNEinkaufpreis(auftragpositionDto
					.getBdEinkaufpreis());
		}
		auftragposition.setPositionIIdArtikelset(auftragpositionDto
				.getPositioniIdArtikelset());
		auftragposition.setVerleihIId(auftragpositionDto.getVerleihIId());
		auftragposition.setKostentraegerIId(auftragpositionDto
				.getKostentraegerIId());
		auftragposition.setZwsVonPosition(auftragpositionDto
				.getZwsVonPosition());
		auftragposition.setZwsBisPosition(auftragpositionDto
				.getZwsBisPosition());
		auftragposition.setZwsNettoSumme(auftragpositionDto.getZwsNettoSumme());
		if (auftragpositionDto.getBZwsPositionspreisZeigen() != null) {
			auftragposition.setBZwsPositionspreisZeigen(auftragpositionDto
					.getBZwsPositionspreisZeigen());
		} else {
			auftragposition.setBZwsPositionspreisZeigen(Helper
					.boolean2Short(true));
		}
		auftragposition.setCLvposition(auftragpositionDto.getCLvposition());
		auftragposition.setNMaterialzuschlag(auftragpositionDto
				.getNMaterialzuschlag());
		auftragposition.setLieferantIId(auftragpositionDto.getLieferantIId());
		auftragposition
				.setNEinkaufpreis(auftragpositionDto.getBdEinkaufpreis());

		auftragposition.setNMaterialzuschlagKurs(auftragpositionDto
				.getNMaterialzuschlagKurs());
		auftragposition.setTMaterialzuschlagDatum(auftragpositionDto
				.getTMaterialzuschlagDatum());

		em.merge(auftragposition);
		em.flush();
	}

	private AuftragpositionDto assembleAuftragspositionDto(
			Auftragposition auftragsposition) {
		return AuftragpositionDtoAssembler.createDto(auftragsposition);
	}

	private AuftragpositionDto[] assembleAuftragspositionDtos(
			Collection<?> auftragspositions) {
		List<AuftragpositionDto> list = new ArrayList<AuftragpositionDto>();
		if (auftragspositions != null) {
			Iterator<?> iterator = auftragspositions.iterator();
			while (iterator.hasNext()) {
				Auftragposition auftragsposition = (Auftragposition) iterator
						.next();
				list.add(assembleAuftragspositionDto(auftragsposition));
			}
		}
		AuftragpositionDto[] returnArray = new AuftragpositionDto[list.size()];
		return (AuftragpositionDto[]) list.toArray(returnArray);
	}

	private AuftragpositionDto assembleAuftragpositionDto(
			Auftragposition auftragposition) {
		return AuftragpositionDtoAssembler.createDto(auftragposition);
	}

	private AuftragpositionDto[] assembleAuftragpositionDtos(
			Collection<?> auftragpositions) {
		List<AuftragpositionDto> list = new ArrayList<AuftragpositionDto>();
		if (auftragpositions != null) {
			Iterator<?> iterator = auftragpositions.iterator();
			while (iterator.hasNext()) {
				Auftragposition auftragposition = (Auftragposition) iterator
						.next();
				list.add(assembleAuftragpositionDto(auftragposition));
			}
		}
		AuftragpositionDto[] returnArray = new AuftragpositionDto[list.size()];
		return (AuftragpositionDto[]) list.toArray(returnArray);
	}

	public String getNextSeriennr(Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		String srnnr = null;
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria srnCriteria = session
					.createCriteria(FLRAuftragseriennrn.class);
			srnCriteria.add(Restrictions.eq("artikel_i_id", iIdArtikelI));
			srnCriteria.addOrder(Order.desc("i_sort"));
			List<?> lSrnnrn = srnCriteria.list();
			Iterator<?> iter = lSrnnrn.iterator();
			if (iter.hasNext()) {
				FLRAuftragseriennrn auftragseriennrn = (FLRAuftragseriennrn) iter
						.next();
				LpBelegnummer bnr = new LpBelegnummer(2008,
						theClientDto.getMandant(),
						Integer.valueOf(auftragseriennrn.getC_seriennr()) + 1);
				LpAuftragseriennummerFormat f = new LpAuftragseriennummerFormat(
						auftragseriennrn.getC_seriennr().length());
				srnnr = f.format(bnr);
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return srnnr;
	}

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte = new BigDecimal(
				0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRAuftragposition.class);
			crit.add(Restrictions.eq("position_i_id", iIdPositionI));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRAuftragposition pos = (FLRAuftragposition) iter.next();
				if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_IDENT)
						|| pos.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_HANDEINGABE)) {
					setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte = setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte
							.add(pos.getN_menge()
									.multiply(
											pos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()));
				} else if (pos.getPositionart_c_nr().equals(
						LocaleFac.POSITIONSART_POSITION)) {
					if (pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_BEGINN))
						if (pos.getPosition_i_id() != null) {
							BigDecimal posWert = new BigDecimal(0);
							session = factory.openSession();
							Criteria critPosition = session
									.createCriteria(FLRAuftragposition.class);
							critPosition.add(Restrictions.eq("position_i_id",
									pos.getI_id()));
							List<?> posList = critPosition.list();
							for (Iterator<?> ipos = posList.iterator(); ipos
									.hasNext();) {
								FLRAuftragposition item = (FLRAuftragposition) ipos
										.next();
								if (!pos.getPositionart_c_nr().equals(
										LocaleFac.POSITIONSART_POSITION)) {
									posWert = posWert
											.add(item
													.getN_menge()
													.multiply(
															item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatte()));
								}
							}
							setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte = setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte
									.add(posWert);
						}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return setBdNettogesamtpreisPlusVersteckterAufschlagMinusRabatte;
	}

	public String getSeriennummmern(Integer iIdAuftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		String srnnr = "";
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria srnCriteria = session
					.createCriteria(FLRAuftragseriennrn.class);
			srnCriteria.add(Restrictions.eq("auftragposition_i_id",
					iIdAuftragpositionI));
			srnCriteria.addOrder(Order.asc("i_sort"));
			List<?> lSrnnrn = srnCriteria.list();
			Iterator<?> iter = lSrnnrn.iterator();
			while (iter.hasNext()) {
				FLRAuftragseriennrn auftragseriennrn = (FLRAuftragseriennrn) iter
						.next();
				if (iter.hasNext()) {
					srnnr = srnnr + auftragseriennrn.getC_seriennr() + ",";
				} else {
					srnnr = srnnr + auftragseriennrn.getC_seriennr();
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return srnnr;
	}

	/**
	 * Die Anzahl der Positionen zu einem bestimmten Auftrag holen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return int die Anzahl
	 */
	public int getAnzahlMengenbehafteteAuftragpositionen(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}
		myLogger.logData(iIdAuftragI);
		int iAnzahl = 0;
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> c = query.getResultList();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Auftragposition pos = ((Auftragposition) iter.next());

			if (pos.getNMenge() != null) {

				if (pos.getArtikelIId() != null) {
					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(pos.getArtikelIId(),
									theClientDto);
					if (Helper.short2boolean(aDto.getBKalkulatorisch())) {
						continue;
					}

				}

				iAnzahl++;
			}
		}
		myLogger.exit("Anzahl: " + iAnzahl);

		return iAnzahl;
	}

	public Integer getVersionAuftragseriennrn(String srn, Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iVNr = null;
		Query query = em
				.createNamedQuery("AuftragseriennrnfindByCSeriennrArtikelIId");
		query.setParameter(1, srn);
		query.setParameter(2, artikelIId);
		Collection<?> c = query.getResultList();
		if (c.size() == 0)
			return null;
		query = em.createNamedQuery("AuftragseriennrnejbSelectMaxVersionNr");
		query.setParameter(1, srn);
		query.setParameter(2, artikelIId);
		iVNr = (Integer) query.getSingleResult();
		if (iVNr == null) {
			iVNr = new Integer(0);
		}
		iVNr++;
		return iVNr;
	}

	private static String[] positionartMitStatus = {
			AuftragServiceFac.AUFTRAGPOSITIONART_IDENT,
			AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE,
			AuftragServiceFac.AUFTRAGPOSITIONART_INTELLIGENTE_ZWISCHENSUMME };

	/**
	 * Die Anzahl von Artikelpositionen in einem Auftrag berechnen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param sStatusI
	 *            der Status der gesuchten Positionen, wenn NULL alle
	 *            Artikelpositionen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return int die Anzahl der Artikelpositionen mit diesem Status
	 */
	public int berechneAnzahlArtikelpositionenMitStatus(Integer iIdAuftragI,
			String sStatusI) throws EJBExceptionLP {
		int iAnzahl = 0;
		// alle Positionen dieses Auftrags
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> c = query.getResultList();
		Iterator<?> it = c.iterator();
		while (it.hasNext()) {
			Auftragposition pos = (Auftragposition) it.next();
			if (Helper.isOneOf(pos.getAuftragpositionartCNr(),
					positionartMitStatus)) {
				// if (pos.getAuftragpositionartCNr().equals(
				// AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
				// || pos.getAuftragpositionartCNr().equals(
				// AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {
				if (sStatusI == null) {
					iAnzahl++;
				} else {
					if (pos.getAuftragpositionstatusCNr().equals(sStatusI)) {
						iAnzahl++;
					}
				}
			}
		}
		return iAnzahl;
	}

	private void checkAuftragpositionDto(AuftragpositionDto oAuftragspositionI)
			throws EJBExceptionLP {
		if (oAuftragspositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("auftragspositionDto == null"));
		}

		myLogger.info("AuftragpositionDto: " + oAuftragspositionI.toString());
	}

	private void checkAuftragpositionIId(Integer iIdAuftragpositionI)
			throws EJBExceptionLP {
		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}

		myLogger.info("AuftragpositionIId: " + iIdAuftragpositionI.toString());
	}

	// Methoden zur Bestimmung der Sortierung der
	// Auftragpositionen---------------

	/**
	 * Das maximale iSort bei den Auftragpositionen fuer einen bestimmten
	 * Mandanten bestimmen.
	 * 
	 * @param iIdAuftragI
	 *            der aktuelle Auftrag
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private Integer getMaxISort(Integer iIdAuftragI) throws EJBExceptionLP {
		Integer iiMaxISortO = null;
		try {

			Query query = em
					.createNamedQuery("AuftragpositionejbSelectMaxISort");
			query.setParameter(1, iIdAuftragI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(e));
		}
		return iiMaxISortO;
	}

	public Integer getMinISort(Integer iIdAuftragI) throws EJBExceptionLP {
		Integer iiMinISortO = null;
		try {
			Query query = em
					.createNamedQuery("AuftragpositionejbSelectMinISort");
			query.setParameter(1, iIdAuftragI);
			iiMinISortO = (Integer) query.getSingleResult();
			if (iiMinISortO == null) {
				iiMinISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(e));
		}
		return iiMinISortO;
	}

	/**
	 * Zwei bestehende Auftragpositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I
	 *            PK der ersten Position
	 * @param iIdPosition2I
	 *            PK der zweiten Position
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauschePositionen(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP {
		myLogger.logData(iIdPosition1I + ", " + iIdPosition2I);

		Auftragposition oPosition1 = em.find(Auftragposition.class,
				iIdPosition1I);

		Auftragposition oPosition2 = em.find(Auftragposition.class,
				iIdPosition2I);
		if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() == null) {
			Integer iSort1 = oPosition1.getISort();
			Integer iSort2 = oPosition2.getISort();

			// das zweite iSort auf ungueltig setzen, damit UK
			// constraint nicht verletzt wird
			oPosition2.setISort(new Integer(-1));

			oPosition1.setISort(iSort2);
			oPosition2.setISort(iSort1);
		} else if (oPosition1.getTypCNr() == null
				&& oPosition2.getTypCNr() != null) {
			Integer iSort1 = oPosition1.getISort();
			Integer iSort2 = oPosition2.getISort();

			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZusatzbezeichnung() != null
						&& oPosition2.getCZusatzbezeichnung().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					Query query = em
							.createNamedQuery("AuftragpositionfindByAuftragISort");
					query.setParameter(1, oPosition2.getAuftragIId());
					query.setParameter(2, oPosition2.getISort() - 1);
					// @todo getSingleResult oder getResultList ?
					Auftragposition oPos = (Auftragposition) query
							.getSingleResult();
					oPosition1.setTypCNr(oPos.getTypCNr());
					oPosition1.setPositionIId(oPos.getPositionIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			} else if (oPosition2.getTypCNr().equals(
					LocaleFac.POSITIONTYP_ALLES)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_VERDICHTET)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_OHNEPREISE)
					|| oPosition2.getTypCNr().equals(
							LocaleFac.POSITIONTYP_MITPREISE)) {
				if (oPosition2.getCZusatzbezeichnung() != null
						&& oPosition2.getCZusatzbezeichnung().equals(
								LocaleFac.POSITIONBEZ_BEGINN)) {
					oPosition1.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					oPosition1.setPositionIId(oPosition2.getIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() == null) {

		} else if (oPosition1.getTypCNr() != null
				&& oPosition2.getTypCNr() != null) {
			Integer iSort1 = oPosition1.getISort();
			Integer iSort2 = oPosition2.getISort();
			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZusatzbezeichnung() != null
						&& oPosition2.getCZusatzbezeichnung().equals(
								LocaleFac.POSITIONBEZ_ENDE)) {
					oPosition1.setTypCNr(null);
					oPosition1.setPositionIId(null);
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				} else {
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));
					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		}
	}

	public void vertauscheAuftragpositionenMinus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		CompositeISort<Auftragposition> comp = new CompositeISort<Auftragposition>(
				new AuftragpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		updateTAendernAuftragViaPosIId(iIdBasePosition, theClientDto);
	}

	public void vertauscheAuftragpositionenPlus(Integer iIdBasePosition,
			List<Integer> possibleIIds, TheClientDto theClientDto)
			throws EJBExceptionLP {
		CompositeISort<Auftragposition> comp = new CompositeISort<Auftragposition>(
				new AuftragpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		updateTAendernAuftragViaPosIId(iIdBasePosition, theClientDto);
	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdAuftragI
	 *            der aktuelle Auftrag
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer iIdAuftragI, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Auftragposition oPosition = (Auftragposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
				em.merge(oPosition);
			}

		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdAngebotI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAngebotI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdAngebotI);
		AuftragpositionDto auftragpositionDto = auftragpositionFindByAuftragIIdAuftragpositionsartCNrOhneExc(
				iIdAngebotI, AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME);

		if (auftragpositionDto != null) {
			AuftragpositionDto[] aAuftragpositionDto = auftragpositionFindByAuftrag(iIdAngebotI);

			for (int i = 0; i < aAuftragpositionDto.length; i++) {
				if (aAuftragpositionDto[i].getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_ENDSUMME)) {
					int iIndexLetztePreisbehaftetePositionNachEndsumme = -1;

					for (int j = i + 1; j < aAuftragpositionDto.length; j++) {
						if (aAuftragpositionDto[j].getNEinzelpreis() != null) {
							// die Position der letzten preisbehafteten Position
							// nach der Endsumme bestimmen
							iIndexLetztePreisbehaftetePositionNachEndsumme = j;
						}
					}

					if (iIndexLetztePreisbehaftetePositionNachEndsumme != -1) {
						// die Endsumme muss nach die letzte preisbehaftete
						// Position verschoben werden
						for (int k = i; k < iIndexLetztePreisbehaftetePositionNachEndsumme; k++) {
							vertauschePositionen(
									aAuftragpositionDto[i].getIId(),
									aAuftragpositionDto[k + 1].getIId());
						}
					}
				}
			}
		}
	}

	/**
	 * Wenn fuer einen Auftrag eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken
	 * entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param iSortierungGeloeschtePositionI
	 *            die Position der geloschten Position
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(
			Integer iIdAuftragI, int iSortierungGeloeschtePositionI)
			throws Throwable {
		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Auftragposition oPosition = (Auftragposition) it.next();

			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	/**
	 * Fuer eine bestehende Auftragposition vom Typ Ident oder Handeingabe
	 * werden die zusaetzlichen Preisfelder befuellt.
	 * 
	 * @param iIdPositionI
	 *            PK der Position
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void befuelleZusaetzlichePreisfelder(Integer iIdPositionI)
			throws EJBExceptionLP {
		try {
			Auftragposition oPosition = em.find(Auftragposition.class,
					iIdPositionI);
			if (oPosition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			if (oPosition.getAuftragpositionartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)
					|| oPosition.getAuftragpositionartCNr().equals(
							AuftragServiceFac.AUFTRAGPOSITIONART_HANDEINGABE)) {

				AuftragDto oAuftrag = getAuftragFac().auftragFindByPrimaryKey(
						oPosition.getAuftragIId());
				AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(iIdPositionI);
				oAuftragpositionDto = (AuftragpositionDto) getBelegVerkaufFac()
						.berechneBelegpositionVerkauf(oAuftragpositionDto,
								oAuftrag);

				oPosition
						.setNNettoeinzelpreisplusversteckteraufschlag(oAuftragpositionDto
								.getNEinzelpreisplusversteckteraufschlag());
				oPosition
						.setNNettogesamtpreisplusversteckteraufschlag(oAuftragpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlag());
				oPosition
						.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(oAuftragpositionDto
								.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
				/*
				 * // den versteckten Aufschlag aus den Konditionen
				 * beruecksichtigen BigDecimal bdVersteckterAufschlag = new
				 * BigDecimal(oAuftrag.
				 * getFVersteckterAufschlag().doubleValue()).movePointLeft(2);
				 * bdVersteckterAufschlag =
				 * Helper.rundeKaufmaennisch(bdVersteckterAufschlag, 4);
				 * 
				 * BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme =
				 * oPosition
				 * .getNNettoeinzelpreis().multiply(bdVersteckterAufschlag); //
				 * auf 4 Stellen runden
				 * bdNettoeinzelpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettoeinzelpreisVersteckterAufschlagSumme, 4);
				 * oPosition.setNNettoeinzelpreisplusversteckteraufschlag
				 * (oPosition.getNNettoeinzelpreis().
				 * add(bdNettoeinzelpreisVersteckterAufschlagSumme));
				 * 
				 * BigDecimal bdNettogesamtpreisVersteckterAufschlagSumme =
				 * oPosition
				 * .getNNettogesamtpreis().multiply(bdVersteckterAufschlag); //
				 * auf 4 Stellen runden
				 * bdNettogesamtpreisVersteckterAufschlagSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisVersteckterAufschlagSumme, 4);
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 * oPosition.setNNettogesamtpreisplusversteckteraufschlag(oPosition
				 * .getNNettogesamtpreis().
				 * add(bdNettogesamtpreisVersteckterAufschlagSumme));
				 * 
				 * // die Abschlaege werden auf Basis des Versteckten Aufschlags
				 * beruecksichtigt
				 * 
				 * // - Allgemeiner Rabatt BigDecimal bdAllgemeinerRabattSatz =
				 * new BigDecimal(oAuftrag.getFAllgemeinerRabattsatz().
				 * doubleValue()).movePointLeft(2); bdAllgemeinerRabattSatz =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabattSatz, 4);
				 * 
				 * BigDecimal bdAllgemeinerRabatt = oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlag
				 * ().multiply(bdAllgemeinerRabattSatz); // auf 4 Stellen runden
				 * bdAllgemeinerRabatt =
				 * Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);
				 * 
				 * oPosition.
				 * setNNettogesamtpreisplusversteckteraufschlagminusrabatte
				 * (oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlag().subtract
				 * (bdAllgemeinerRabatt));
				 * 
				 * // - Projektierungsrabatt BigDecimal bdProjektierungsRabatt =
				 * new BigDecimal(oAuftrag.
				 * getFProjektierungsrabattsatz().doubleValue
				 * ()).movePointLeft(2); bdProjektierungsRabatt =
				 * Helper.rundeKaufmaennisch(bdProjektierungsRabatt, 4);
				 * 
				 * BigDecimal bdNettogesamtpreisProjektierungsrabattSumme =
				 * oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlagminusrabatte
				 * ().multiply( bdProjektierungsRabatt); // auf 4 Stellen runden
				 * bdNettogesamtpreisProjektierungsrabattSumme =
				 * Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisProjektierungsrabattSumme, 4);oPosition.
				 * setNNettogesamtpreisplusversteckteraufschlagminusrabatte
				 * (oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlagminusrabatte
				 * ().subtract( bdNettogesamtpreisProjektierungsrabattSumme));
				 */
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Die offene Menge einer Auftragposition nach oben oder unten korrigieren. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Auftragpositionen koennen teilgeliefert werden und es kann mehrere
	 * Lieferscheine zu einer Auftragposition geben.
	 * <li>Auftragpositionen koennen ueberliefert werden, d.h. die offene Menge
	 * kann negativ werden.
	 * <li>Die offene Menge kann nicht groesser als die Gesamtmenge werden.
	 * <li>Der Status der Auftragposition kann sich mit der offenen Menge
	 * aendern.
	 * <li>Der Status des Auftrags kann sich mit der offenen Menge einer
	 * Auftragposition aendern.
	 * <li>Die Aenderung der offenen Menge zieht immer eine Aenderung in der
	 * Artikelreservierung nach sich.
	 * </ul>
	 * <br>
	 * Korrigiert werden muessen ev. auch der Status der Auftragposition und der
	 * Status des zugehoerigen Auftrags.
	 * 
	 * @param iIdAuftragpositionI
	 *            PK der Auftragsposition
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateOffeneMengeAuftragposition(Integer iIdAuftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragpositionI != null) {
			AuftragpositionDto auftragpositionDto = auftragpositionFindByPrimaryKey(iIdAuftragpositionI);
			AuftragDto auftragDto = null;
			try {
				auftragDto = getAuftragFac().auftragFindByPrimaryKey(
						auftragpositionDto.getBelegIId());

				if (auftragpositionDto.getNMenge() != null) {

					if (auftragDto.getAuftragartCNr().equals(
							AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {

						auftragpositionDto.setNOffeneMenge(auftragpositionDto
								.getNMenge());
						auftragpositionDto
								.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);

					} else {

						BigDecimal summeDerLieferscheinPositionen = new BigDecimal(
								0);
						BigDecimal summeDerRechnungsPositionen = new BigDecimal(
								0);
						// Hole alle Lieferscheinposition mit Auftragsbezug

						Session session = FLRSessionFactory.getFactory()
								.openSession();

						String sQuery = "SELECT sum(l.n_menge) FROM FLRLieferscheinposition l WHERE l.auftragposition_i_id="
								+ iIdAuftragpositionI
								+ " AND l.flrlieferschein.lieferscheinstatus_status_c_nr NOT IN ('"
								+ LieferscheinFac.LSSTATUS_STORNIERT + "')";
						org.hibernate.Query query = session.createQuery(sQuery);
						List<?> resultList = query.list();
						Iterator<?> resultListIterator = resultList.iterator();

						if (resultListIterator.hasNext()) {
							summeDerLieferscheinPositionen = (BigDecimal) resultListIterator
									.next();
							if (summeDerLieferscheinPositionen == null) {
								summeDerLieferscheinPositionen = new BigDecimal(
										0);
							}
						}
						session.close();

						// Hole alle Rechnungsposition mit Auftragsbezug
						session = FLRSessionFactory.getFactory().openSession();

						sQuery = "SELECT sum(r.n_menge) FROM FLRRechnungPosition r WHERE r.auftragposition_i_id="
								+ iIdAuftragpositionI
								+ " AND r.flrrechnung.status_c_nr NOT IN ('"
								+ RechnungFac.STATUS_STORNIERT + "')";
						query = session.createQuery(sQuery);
						resultList = query.list();
						resultListIterator = resultList.iterator();

						if (resultListIterator.hasNext()) {
							summeDerRechnungsPositionen = (BigDecimal) resultListIterator
									.next();
							if (summeDerRechnungsPositionen == null) {
								summeDerRechnungsPositionen = new BigDecimal(0);
							}
						}

						// Wenn Auftragspositionsmenge negativ:

						BigDecimal neueOffeneMenge = null;

						if (auftragpositionDto.getNMenge().doubleValue() >= 0) {
							neueOffeneMenge = auftragpositionDto.getNMenge()
									.subtract(summeDerLieferscheinPositionen)
									.subtract(summeDerRechnungsPositionen);

							if (neueOffeneMenge.doubleValue() < 0) {
								neueOffeneMenge = new BigDecimal(0);
							}

						} else {
							neueOffeneMenge = auftragpositionDto.getNMenge()
									.subtract(summeDerLieferscheinPositionen)
									.subtract(summeDerRechnungsPositionen);

							if (neueOffeneMenge.doubleValue() > 0) {
								neueOffeneMenge = new BigDecimal(0);
							}
						}

						// runden lt. mandantenparameter
						neueOffeneMenge = neueOffeneMenge.setScale(
								getMandantFac().getNachkommastellenMenge(
										theClientDto.getMandant()).intValue(),
								BigDecimal.ROUND_HALF_EVEN);

						auftragpositionDto.setNOffeneMenge(neueOffeneMenge);

						if (auftragpositionDto.getNMenge().doubleValue() >= 0) {

							// ev. den Status der Auftragposition korrigieren
							if (auftragpositionDto.getNOffeneMenge()
									.doubleValue() <= 0) {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
							} else if (auftragpositionDto.getNOffeneMenge()
									.compareTo(auftragpositionDto.getNMenge()) == 0) {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
							} else {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_TEILERLEDIGT);
							}
						} else {
							if (auftragpositionDto.getNOffeneMenge()
									.doubleValue() > auftragpositionDto
									.getNMenge().doubleValue()) {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT);
							} else if (auftragpositionDto.getNOffeneMenge()
									.compareTo(auftragpositionDto.getNMenge()) == 0) {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
							} else {
								auftragpositionDto
										.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_TEILERLEDIGT);
							}
						}

					}

				} else {
					auftragpositionDto.setNOffeneMenge(null);
				}

				updateAuftragpositionOhneWeitereAktion(auftragpositionDto,
						theClientDto);
				if (!auftragDto.getAuftragartCNr().equals(
						AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
					gleicheAuftragreservierungAnOffeneMengeAn(auftragDto,
							auftragpositionDto);
				}

				// ev. den Status des gesamten Auftrags korrigieren
				getAuftragFac().setzeAuftragstatusAufgrundAuftragpositionstati(
						auftragpositionDto.getBelegIId(), theClientDto);

			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}
	}

	/**
	 * Die Auftragreservierung fuer eine Auftragposition wird an die aktuelle
	 * offene Menge angepasst. <br>
	 * Es gilt:
	 * <ul>
	 * <li>Alle Identpositionen loesen eine Auftragreservierung aus.
	 * <li>Auftragreservierungen koennen im Gegensatz zur offenen Menge nicht
	 * negativ werden.
	 * <li>Die Menge einer Auftragreservierung wird immer absolut gebucht.
	 * <li>Die Auftragreservierung ist lagerunabhaenig.
	 * <li>
	 * </ul>
	 * 
	 * @param auftragDtoI
	 *            AuftragDto
	 * @param auftragpositionDtoI
	 *            die Auftragposition
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void gleicheAuftragreservierungAnOffeneMengeAn(
			AuftragDto auftragDtoI, AuftragpositionDto auftragpositionDtoI)
			throws EJBExceptionLP {
		checkAuftragpositionDto(auftragpositionDtoI);

		try {
			// Bei Rahmenauftraegen gibt es keine Reservierungen.
			if (!auftragDtoI.getAuftragartCNr().equals(
					AuftragServiceFac.AUFTRAGART_RAHMEN)) {
				if (auftragpositionDtoI.getPositionsartCNr().equals(
						AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
					ArtikelreservierungDto reservierungDto = getReservierungFac()
							.getArtikelreservierung(LocaleFac.BELEGART_AUFTRAG,
									auftragpositionDtoI.getIId());
					if (reservierungDto != null) {
						reservierungDto.setArtikelIId(auftragpositionDtoI
								.getArtikelIId());
						if (auftragpositionDtoI.getNOffeneMenge().doubleValue() > 0) {

							reservierungDto.setNMenge(auftragpositionDtoI
									.getNOffeneMenge());
							reservierungDto
									.setTLiefertermin(auftragpositionDtoI
											.getTUebersteuerbarerLiefertermin());

							getReservierungFac().updateArtikelreservierung(
									reservierungDto);

						}
						// offene Menge ist 0 -> reservierung loeschen
						else if (auftragpositionDtoI.getNOffeneMenge()
								.doubleValue() == 0) {
							getReservierungFac().removeArtikelreservierung(
									reservierungDto.getIId());
						}
						// Offene Menge ist negativ -> 2 faelle betrachten
						else if (auftragpositionDtoI.getNOffeneMenge()
								.doubleValue() < 0) {
							// wenn die Menge positiv ist, dann ist bereits
							// alles geliefert -> reservierung loeschen
							if (auftragpositionDtoI.getNMenge().doubleValue() >= 0) {
								getReservierungFac().removeArtikelreservierung(
										reservierungDto.getIId());
							}
							// wenn die Menge negativ ist, dann gibt es
							// weiterhin eine negative reservierung
							else if (auftragpositionDtoI.getNMenge()
									.doubleValue() < 0) {
								reservierungDto.setNMenge(auftragpositionDtoI
										.getNOffeneMenge());
								reservierungDto
										.setTLiefertermin(auftragpositionDtoI
												.getTUebersteuerbarerLiefertermin());
								getReservierungFac().updateArtikelreservierung(
										reservierungDto);
							}
						}
					} else if (auftragpositionDtoI.getNOffeneMenge()
							.doubleValue() != 0) {
						reservierungDto = new ArtikelreservierungDto();
						reservierungDto
								.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
						reservierungDto
								.setIBelegartpositionid(auftragpositionDtoI
										.getIId());
						reservierungDto.setArtikelIId(auftragpositionDtoI
								.getArtikelIId());
						reservierungDto.setTLiefertermin(auftragpositionDtoI
								.getTUebersteuerbarerLiefertermin());
						reservierungDto.setNMenge(auftragpositionDtoI
								.getNOffeneMenge());
						getReservierungFac().createArtikelreservierung(
								reservierungDto); // absolute Menge buchen
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Fuer einen bestimmten Auftrag die Summe der in den Positionen enthaltenen
	 * Arbeitszeit berechnen.
	 * 
	 * @param iIdAuftragI
	 *            PK des Auftrags
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Double die Soll Arbeitszeit eines bestimmten Auftrags
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Double berechneArbeitszeitSoll(Integer iIdAuftragI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}
		myLogger.logData(iIdAuftragI);
		double dArbeitszeitSoll = 0;

		Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Auftragposition auftragposition = (Auftragposition) it.next();

			if (auftragposition.getAuftragpositionartCNr().equals(
					AuftragServiceFac.AUFTRAGPOSITIONART_IDENT)) {
				if (auftragposition.getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(
									auftragposition.getArtikelIId(),
									theClientDto);

					if (artikelDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
						dArbeitszeitSoll += auftragposition.getNMenge()
								.doubleValue();
					}
				}
			}
		}

		myLogger.exit("Arbeitszeitsoll: " + dArbeitszeitSoll);
		return new Double(dArbeitszeitSoll);
	}

	public void berechnePauschalposition(BigDecimal neuWert,
			Integer positionIId, Integer belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BigDecimal altWert = getGesamtpreisPosition(positionIId, theClientDto);
		AuftragDto auftragDto = getAuftragFac().auftragFindByPrimaryKey(
				belegIId);
		AuftragpositionDto[] auftragpositionDtos = auftragpositionFindByPositionIId(positionIId);
		for (int i = 0; i < auftragpositionDtos.length; i++) {
			AuftragpositionDto auftragpositionDto = (AuftragpositionDto) getBelegVerkaufFac()
					.berechnePauschalposition(auftragpositionDtos[i],
							auftragDto, neuWert, altWert);

			Auftragposition position = em.find(Auftragposition.class,
					auftragpositionDto.getIId());
			position.setNNettogesamtpreis(auftragpositionDto
					.getNNettoeinzelpreis());
			position.setNNettogesamtpreisplusversteckteraufschlag(auftragpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlag());
			position.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(auftragpositionDto
					.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			position.setBNettopreisuebersteuert(Helper.boolean2Short(true));

		}
	}

	private void pruefePflichtfelderBelegposition(AuftragpositionDto abPosDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDtoVerkauf(abPosDto, theClientDto);
	}

	public Integer createAuftragseriennrn(
			AuftragseriennrnDto auftragseriennrnDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Integer iIdAuftragseriennrn = null;
		Integer iiMaxISortO = null;
		if (auftragseriennrnDto == null) {
			return null;
		}
		try {
			// PK generieren
			iIdAuftragseriennrn = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_AUFTRAGSERIENNR);
			auftragseriennrnDto.setIId(iIdAuftragseriennrn);
			// Sortierung: falls nicht anders definiert, hinten dran haengen.
			if (auftragseriennrnDto.getISort() == null) {
				try {
					Query query = em
							.createNamedQuery("AuftragseriennrnejbSelectMaxISort");
					query.setParameter(1, auftragseriennrnDto.getArtikelIId());
					// int iSortNeu = (Integer)query.getSingleResult() +1;
					iiMaxISortO = (Integer) query.getSingleResult();
					if (iiMaxISortO == null) {
						iiMaxISortO = new Integer(0);
					} else {
						iiMaxISortO++;
					}
					auftragseriennrnDto.setISort(iiMaxISortO);
				} catch (NoResultException e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
			}
			Auftragseriennrn auftragseriennrn = new Auftragseriennrn(
					auftragseriennrnDto.getIId(),
					auftragseriennrnDto.getISort(),
					auftragseriennrnDto.getArtikelIId(),
					auftragseriennrnDto.getCSeriennr(),
					auftragseriennrnDto.getAuftragpositionIId(),
					auftragseriennrnDto.getTAnlegen(),
					auftragseriennrnDto.getPersonalIIdAnlegen(),
					auftragseriennrnDto.getIVersionNr());
			em.persist(auftragseriennrn);
			em.flush();

			setAuftragseriennrnFromAuftragseriennrnDto(auftragseriennrn,
					auftragseriennrnDto);
			updateTAendernAuftragViaPosIId(
					auftragseriennrnDto.getAuftragpositionIId(), theClientDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return iIdAuftragseriennrn;
	}

	public void removeAuftragseriennrn(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Auftragseriennrn toRemove = em.find(Auftragseriennrn.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				updateTAendernAuftragViaPosIId(
						toRemove.getAuftragpositionIId(), theClientDto);
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (auftragseriennrnDto != null) {
			Integer iId = auftragseriennrnDto.getIId();
			removeAuftragseriennrn(iId, theClientDto);
		}
	}

	public void updateAuftragseriennrn(AuftragseriennrnDto auftragseriennrnDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (auftragseriennrnDto != null) {
			Integer iId = auftragseriennrnDto.getIId();
			try {
				Auftragseriennrn auftragseriennrn = em.find(
						Auftragseriennrn.class, iId);
				setAuftragseriennrnFromAuftragseriennrnDto(auftragseriennrn,
						auftragseriennrnDto);
				updateTAendernAuftragViaPosIId(
						auftragseriennrn.getAuftragpositionIId(), theClientDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public AuftragseriennrnDto auftragseriennrnFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {,
		Auftragseriennrn auftragseriennrn = em
				.find(Auftragseriennrn.class, iId);
		if (auftragseriennrn == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAuftragseriennrnDto(auftragseriennrn);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public AuftragseriennrnDto auftragseriennrnFindByAuftragpsotionIId(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		AuftragseriennrnDto[] auftragseriennrnDtos = null;
		try {
			Query query = em
					.createNamedQuery("AuftragseriennrnfindByAuftragpositionIId");
			query.setParameter(1, iId);
			Collection<?> auftragseriennrn = query.getResultList();
			auftragseriennrnDtos = assembleAuftragseriennrnDtos(auftragseriennrn);
			if (auftragseriennrnDtos.length != 0)
				return auftragseriennrnDtos[0];
			else
				return null;
		} catch (NoResultException ex) {
			return null;
		}
	}

	public AuftragseriennrnDto[] auftragseriennrnfindByCSeriennrArtikelIId(
			String cSerienNr, Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AuftragseriennrnDto[] auftragseriennrnDtos = null;
		try {
			Query query = em
					.createNamedQuery("AuftragseriennrnfindByCSeriennrArtikelIId");
			query.setParameter(1, cSerienNr);
			query.setParameter(2, artikelIId);
			Collection<?> auftragseriennrn = query.getResultList();
			auftragseriennrnDtos = assembleAuftragseriennrnDtos(auftragseriennrn);
			return auftragseriennrnDtos;
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Object[][] isAuftragseriennrnVorhanden(String[] cSerienNr,
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		Object[][] data = null;
		for (int j = 0; j < cSerienNr.length; j++) {
			StringBuffer sbtext = null;
			AuftragseriennrnDto[] auftragseriennrnDtos = auftragseriennrnfindByCSeriennrArtikelIId(
					cSerienNr[j], artikelIId, theClientDto);
			if (auftragseriennrnDtos.length != 0) {
				data = new Object[cSerienNr.length][3];
				for (int i = 0; i < auftragseriennrnDtos.length; i++) {
					sbtext = new StringBuffer();
					Auftragposition pos = em.find(Auftragposition.class,
							auftragseriennrnDtos[i].getAuftragpositionIId());
					Auftrag auftrag = em.find(Auftrag.class,
							pos.getAuftragIId());
					sbtext.append(auftrag.getCNr());
					if (i != auftragseriennrnDtos.length - 1)
						sbtext.append(",");
				}
				data[j][0] = cSerienNr[j];
				if (sbtext != null)
					data[j][1] = sbtext.toString();
				data[j][2] = getVersionAuftragseriennrn(cSerienNr[j],
						artikelIId, theClientDto);
			}
		}
		return data;
	}

	private void setAuftragseriennrnFromAuftragseriennrnDto(
			Auftragseriennrn auftragseriennrn,
			AuftragseriennrnDto auftragseriennrnDto) {
		auftragseriennrn.setAuftragpositionIId(auftragseriennrnDto
				.getAuftragpositionIId());
		auftragseriennrn.setArtikelIId(auftragseriennrnDto.getArtikelIId());
		auftragseriennrn.setCSeriennr(auftragseriennrnDto.getCSeriennr());
		auftragseriennrn.setISort(auftragseriennrnDto.getISort());
		auftragseriennrn.setCKommentar(auftragseriennrnDto.getCKommentar());
		auftragseriennrn.setTAnlegen(auftragseriennrnDto.getTAnlegen());
		auftragseriennrn.setPersonalIIdAnlegen(auftragseriennrnDto
				.getPersonalIIdAnlegen());
		auftragseriennrn.setIVersionNr(auftragseriennrnDto.getIVersionNr());

		em.merge(auftragseriennrn);
		em.flush();
	}

	private AuftragseriennrnDto assembleAuftragseriennrnDto(
			Auftragseriennrn auftragseriennrn) {
		return AuftragseriennrnDtoAssembler.createDto(auftragseriennrn);
	}

	private AuftragseriennrnDto[] assembleAuftragseriennrnDtos(
			Collection<?> auftragseriennrns) {
		List<AuftragseriennrnDto> list = new ArrayList<AuftragseriennrnDto>();
		if (auftragseriennrns != null) {
			Iterator<?> iterator = auftragseriennrns.iterator();
			while (iterator.hasNext()) {
				Auftragseriennrn auftragseriennrn = (Auftragseriennrn) iterator
						.next();
				list.add(assembleAuftragseriennrnDto(auftragseriennrn));
			}
		}
		AuftragseriennrnDto[] returnArray = new AuftragseriennrnDto[list.size()];
		return (AuftragseriennrnDto[]) list.toArray(returnArray);
	}

	public void pruefeAuftragseriennumern(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String[] srnnrn = null;
		AuftragseriennrnDto auftragseriennrnDto = null;
		try {
			org.hibernate.Criteria crit = session
					.createCriteria(FLRAuftragpositionReport.class);
			org.hibernate.Criteria critAuftrag = crit
					.createCriteria(AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRAUFTRAG);
			critAuftrag.add(Restrictions.ne(
					AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR,
					AuftragServiceFac.AUFTRAGSTATUS_STORNIERT));
			crit.add(Restrictions.isNotNull("c_seriennrchargennr"));
			List<?> aposList = crit.list();
			Iterator<?> aposIterator = aposList.iterator();
			while (aposIterator.hasNext()) {
				FLRAuftragpositionReport item = (FLRAuftragpositionReport) aposIterator
						.next();
				srnnrn = Helper.erzeugeStringArrayAusString(item
						.getC_seriennrchargennr());
				for (int i = 0; i < srnnrn.length; i++) {
					auftragseriennrnDto = new AuftragseriennrnDto();
					auftragseriennrnDto.setAuftragpositionIId(item.getI_id());
					auftragseriennrnDto.setArtikelIId(item.getArtikel_i_id());
					auftragseriennrnDto.setCSeriennr(srnnrn[i]);
					try {
						createAuftragseriennrn(auftragseriennrnDto,
								theClientDto);
						if (i == srnnrn.length - 1) {
							Auftragposition oPos = null;
							oPos = em.find(Auftragposition.class,
									item.getI_id());
							if (oPos == null) {
							}
							oPos.setCSeriennrchargennr(null);
						}
					} catch (EJBExceptionLP ex) {
						System.out.println("auftragposition " + item.getI_id()
								+ " auftrag " + item.getFlrauftrag().getC_nr());
					}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	public AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI) {
		AuftragpositionDto auftragpositionDto = null;
		try {
			auftragpositionDto = auftragpositionFindByAuftragIIdAuftragpositionsartCNrImpl(
					iIdAngebotI, positionsartCNrI);
		} catch (Exception ex) {
			// do nothing
		}
		return auftragpositionDto;
	}

	public AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionsartCNr(
			Integer iIdAngebotI, String positionsartCNrI) throws EJBExceptionLP {
		AuftragpositionDto auftragpositionDto = null;
		try {
			auftragpositionDto = auftragpositionFindByAuftragIIdAuftragpositionsartCNrImpl(
					iIdAngebotI, positionsartCNrI);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler. Es gibt mehere Angebotspositionen mit positionsart "
							+ positionsartCNrI + " fuer angebotIId "
							+ iIdAngebotI);
		}
		return auftragpositionDto;
	}

	private AuftragpositionDto auftragpositionFindByAuftragIIdAuftragpositionsartCNrImpl(
			Integer auftragId, String positionsartCNrI)
			throws NoResultException, NonUniqueResultException {
		Query query = em
				.createNamedQuery("AuftragpositionfindByAuftragIIdAuftragpositionsartCNr");
		query.setParameter(1, auftragId);
		query.setParameter(2, positionsartCNrI);
		Auftragposition auftragposition = (Auftragposition) query
				.getSingleResult();
		return assembleAuftragpositionDto(auftragposition);
	}

	public Integer getPositionNummer(Integer auftragpositionIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionNummer(auftragpositionIId,
				new AuftragPositionNumberAdapter(em));
	}

	/**
	 * Liefert die IId der Position fuer die angegebene Positionsnummer zurueck
	 * 
	 * @param auftragIId
	 * @param position
	 *            die Positionsnummer f&uuml;r die die IId ermittelt werden soll
	 * @return null wenn es position nicht gibt, ansonsten die IId
	 */
	public Integer getPositionIIdFromPositionNummer(Integer auftragIId,
			Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionIIdFromPositionNummer(auftragIId,
				position, new AuftragPositionNumberAdapter(em));
	}

	public Integer getLastPositionNummer(Integer auftragposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(auftragposIId,
				new AuftragPositionNumberAdapter(em));
	}

	public Integer getHighestPositionNumber(Integer auftragIId)
			throws EJBExceptionLP {
		AuftragpositionDto auftragposDtos[] = auftragpositionFindByAuftrag(auftragIId);
		if (auftragposDtos.length == 0)
			return 0;

		return getLastPositionNummer(auftragposDtos[auftragposDtos.length - 1]
				.getIId());
	}

	public boolean pruefeAufGleichenMwstSatz(Integer auftragIId,
			Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		AuftragpositionDto dtos[] = auftragpositionFindByAuftrag(auftragIId);

		return getBelegVerkaufFac().pruefeAufGleichenMwstSatz(dtos,
				vonPositionNumber, bisPositionNumber);
	}

	/**
	 * Ist das komplette Artikelset in ausreichender Menge verf&uuml;gbar?
	 * 
	 * @param positions
	 *            enth&auml;lt alle jene Auftragspositionen die f&uuml;r das
	 *            ArtikelSet relevant sind
	 * @return true wenn alle im Artikelset befindlichen Artikel in
	 *         ausreichender Menge vorhanden sind, ansonsten false
	 */
	public boolean isArtikelSetLagernd(AuftragpositionDto[] positions,
			Integer lagerIId, TheClientDto theClientDto) throws RemoteException {
		for (AuftragpositionDto auftragpositionDto : positions) {
			if (!AuftragServiceFac.AUFTRAGPOSITIONART_IDENT
					.equals(auftragpositionDto.getPositionsartCNr())) {
				continue;
			}

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					auftragpositionDto.getArtikelIId(), theClientDto);

			if (!artikelDto.isLagerbewirtschaftet()) {
				continue;
			}

			BigDecimal available = getLagerFac().getLagerstand(
					artikelDto.getIId(), lagerIId, theClientDto);
			if (available.compareTo(auftragpositionDto.getNOffeneMenge()) < 0)
				return false;
		}

		return true;
	}

	/**
	 * Ermittelt die erf&uuml;llbare Menge/Anzahl eines Artikelsets. Es wird
	 * davon ausgegangen, dass die erste Position (positions[0]) den Kopfartikel
	 * enth&auml;lt und somit auch die Sollmenge (Satzgr&ouml;&szlig;e). Es wird
	 * die noch offene Menge ber&uuml;cksichtigt.
	 * 
	 * @param positions
	 *            enth&auml;lt alle jene Auftragspositionen die f&uuml;r das
	 *            ArtikelSet relevant sind
	 * @param lagerIId
	 *            die Lager-IId von der die Ware entnommen werden soll.
	 * @param theClientDto
	 * @return die erfuellbare Menge fuer dieses Artikelset
	 * 
	 * @throws RemoteException
	 */
	public BigDecimal getErfuellbareMengeArtikelset(
			AuftragpositionDto[] positions, Integer lagerIId,
			TheClientDto theClientDto) throws RemoteException {
		if (positions.length == 0)
			return BigDecimal.ZERO;

		BigDecimal erfuellbareMenge = positions[0].getNOffeneMenge();
		BigDecimal saetze = positions[0].getNMenge();

		for (AuftragpositionDto auftragpositionDto : positions) {
			if (!AuftragServiceFac.AUFTRAGPOSITIONART_IDENT
					.equals(auftragpositionDto.getPositionsartCNr())) {
				continue;
			}

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					auftragpositionDto.getArtikelIId(), theClientDto);

			if (!artikelDto.isLagerbewirtschaftet()) {
				continue;
			}

			BigDecimal available = getLagerFac().getLagerstand(
					artikelDto.getIId(), lagerIId, theClientDto);
			if (available.compareTo(auftragpositionDto.getNOffeneMenge()) < 0) {
				BigDecimal satzFactor = auftragpositionDto.getNMenge().divide(
						saetze);
				BigDecimal m = available.divide(satzFactor);
				if (m.compareTo(erfuellbareMenge) < 0) {
					erfuellbareMenge = m;
				}
			}
		}

		return erfuellbareMenge;
	}

	/**
	 * Alle Artikelsets mit noch offenen Mengen liefern
	 * 
	 * @param positions
	 *            sind alle Auftragspositionen
	 * @param theClientDto
	 * @return eine Liste jene Artikelsets f&uuml;r die es noch offene Mengen im
	 *         Kopfartikel gibt. Es wird das komplette Artikelset des Auftrags
	 *         geliefert.
	 */
	public List<Artikelset> getOffeneAuftragpositionDtoMitArtikelset(
			AuftragpositionDto[] positions, TheClientDto theClientDto) {
		List<Artikelset> artikelSets = new ArrayList<Artikelset>();

		for (int i = 0; i < positions.length; i++) {
			if (positions[i].getNMenge() == null)
				continue;
			if ((positions[i].getNOffeneMenge() == null)
					|| (positions[i].getNOffeneMenge().signum() == 0))
				continue;

			if (positions[i].getArtikelIId() != null) {
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								positions[i].getArtikelIId(), theClientDto);
				if (stklDto != null
						&& stklDto.getStuecklisteartCNr().equals(
								StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
					Artikelset artikelSet = new Artikelset(positions[i]);
					while ((++i < positions.length)
							&& (positions[i].getPositioniIdArtikelset() != null)) {
						artikelSet.addPosition(positions[i]);
					}

					artikelSets.add(artikelSet);
					--i;
				}
			}
		}

		return artikelSets;
	}
}
