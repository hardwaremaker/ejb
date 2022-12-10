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
package com.lp.server.lieferschein.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Lagerbewegung;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelreservierungDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.GeraetesnrDto;
import com.lp.server.artikel.service.MaterialzuschlagDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.ejb.AuftragpositionQuery;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragpositionReport;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.GesamtkalkulationDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.forecast.ejb.Fclieferadresse;
import com.lp.server.forecast.ejb.FclieferadresseQuery;
import com.lp.server.forecast.ejb.Forecast;
import com.lp.server.forecast.ejb.Forecastauftrag;
import com.lp.server.forecast.ejb.ForecastauftragQuery;
import com.lp.server.forecast.ejb.Forecastposition;
import com.lp.server.forecast.ejb.ForecastpositionQuery;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.ejb.LieferscheinpositionQuery;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDtoAssembler;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocNodeWEPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.AuftragpositionId;
import com.lp.server.util.Facade;
import com.lp.server.util.ForecastpositionId;
import com.lp.server.util.HelperServer;
import com.lp.server.util.IArtikelsetPreisUpdate;
import com.lp.server.util.IBelegVerkaufEntity;
import com.lp.server.util.IPositionNumber;
import com.lp.server.util.LieferscheinPositionNumberAdapter;
import com.lp.server.util.MwstsatzbezId;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.PositionNumberHandlerLieferschein;
import com.lp.server.util.ZwsPositionMapper;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.Pair;

@Stateless
public class LieferscheinpositionFacBean extends Facade
		implements LieferscheinpositionFac, IPrimitiveSwapper, IPositionNumber {
	@PersistenceContext
	private EntityManager em;

	public BigDecimal getGesamtpreisPosition(Integer iIdPositionI, TheClientDto theClientDto) throws EJBExceptionLP {
		BigDecimal wert = new BigDecimal(0);
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria crit = session.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID, iIdPositionI));
			crit.addOrder(Order.asc("i_sort"));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter.next();
				if (pos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)
						|| pos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
					wert = wert.add(
							pos.getN_menge().multiply(pos.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()));
				} else if (pos.getPositionsart_c_nr().equals(AuftragServiceFac.AUFTRAGPOSITIONART_POSITION)) {
					if (pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_BEGINN))
						if (pos.getPosition_i_id() != null) {
							BigDecimal posWert = new BigDecimal(0);
							session = factory.openSession();
							Criteria critPosition = session.createCriteria(FLRLieferscheinposition.class);
							critPosition.add(Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONART_POSITION_I_ID,
									pos.getI_id()));
							List<?> posList = critPosition.list();
							for (Iterator<?> ipos = posList.iterator(); ipos.hasNext();) {
								FLRLieferscheinposition item = (FLRLieferscheinposition) ipos.next();
								if (!pos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
									posWert = posWert.add(item.getN_menge()
											.multiply(item.getN_nettogesamtpreisplusversteckteraufschlagminusrabatt()));
								}
							}
							wert = wert.add(posWert);
						}
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return wert;
	}

	public TreeMap<String, ArrayList<JCRDocDto>> getWEPDokumente(
			String belegartCnr, Integer belegartpositionId, 
			String belegartDokumentenablage, String gruppierung, boolean alleVersionen, TheClientDto theClientDto) {
		TreeMap<String, ArrayList<JCRDocDto>> dokumente = new TreeMap<String, ArrayList<JCRDocDto>>();
		List<WarenzugangsreferenzDto> zugangsreferenzen = getLagerFac().getWareneingangsreferenz(
				belegartCnr, belegartpositionId, null, false, theClientDto);
		
		try {
			for (WarenzugangsreferenzDto ref : zugangsreferenzen) {
				if (!LocaleFac.BELEGART_LOSABLIEFERUNG.equals(ref.getBelegart())) {
					continue;
				}
				
				myLogger.info("ref: " + ref.getBelegartIId() + ".");
				TreeMap<String, List<JCRDocDto>> elements = getJCRDocFac()
						.getLosablieferungsDokumente(ref.getBelegartIId(), 
						belegartDokumentenablage, gruppierung, alleVersionen, theClientDto);
				for (Entry<String, List<JCRDocDto>> entry : elements.entrySet()) {
					if (dokumente.containsKey(entry.getKey())) {
						dokumente.get(entry.getKey()).addAll(entry.getValue());
					} else {
						ArrayList<JCRDocDto> docs = new ArrayList<JCRDocDto>(entry.getValue());
						dokumente.put(entry.getKey(), docs);
					}
				}
			}
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);	
		}
		
		return dokumente;
	}
	
	public TreeMap<String, ArrayList<JCRDocDto>> getWEPDokumente0(String belegartCNr, Integer belegartpositionIId,
			String sBelegartDokumentenablage, String sGruppierung, boolean bAlleVersionen, TheClientDto theClientDto) {

		ArrayList<WarenzugangsreferenzDto> zugangsreferenzen = getLagerFac().getWareneingangsreferenz(belegartCNr,
				belegartpositionIId, null, false, theClientDto);

		TreeMap<String, ArrayList<JCRDocDto>> alDokumente = new TreeMap<String, ArrayList<JCRDocDto>>();

		HashSet<Integer> hsWEPBereitsBeruecksichtigt = new HashSet();

		for (int k = 0; k < zugangsreferenzen.size(); k++) {

			WarenzugangsreferenzDto ref = zugangsreferenzen.get(k);

			if (ref.getBelegart().equals(LocaleFac.BELEGART_LOSABLIEFERUNG)) {

				try {

					LosDto losDto = getFertigungFac().losFindByPrimaryKey(ref.getBelegartIId());

					GesamtkalkulationDto gkDto = getFertigungReportFac().getDatenGesamtkalkulation(losDto.getIId(),
							null, 99, theClientDto);

					for (int i = 0; i < gkDto.getAlDaten().size(); i++) {

						Object[] zeileGK = (Object[]) gkDto.getAlDaten().get(i);

						String belegnummerZugang = (String) zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGNUMMER_ZUGANG];
						String belegartZugang = (String) zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGART_ZUGANG];
						Integer belegartpositionIIdZugang = (Integer) zeileGK[FertigungReportFac.GESAMTKALKULATION_BELEGARTPOSITION_I_ID_ZUGANG];

						String artikelnummer = (String) zeileGK[FertigungReportFac.GESAMTKALKULATION_ARTIKELNUMMER];

						if (belegartZugang != null && belegartZugang.equals(LocaleFac.BELEGART_BESTELLUNG.trim())
								&& belegartpositionIIdZugang != null) {

							WareneingangspositionDto wepDto = getWareneingangFac()
									.wareneingangspositionFindByPrimaryKeyOhneExc(belegartpositionIIdZugang);
							if (wepDto != null) {
								if (!hsWEPBereitsBeruecksichtigt.contains(wepDto.getIId())) {
									WareneingangDto weDto = getWareneingangFac()
											.wareneingangFindByPrimaryKey(wepDto.getWareneingangIId());

									BestellungDto bsDto = getBestellungFac()
											.bestellungFindByPrimaryKey(weDto.getBestellungIId());

									myLogger.info("Artikel:" + artikelnummer + ", Bestellung: " + bsDto.getCNr() + ", wepId=" + wepDto.getIId());
									hsWEPBereitsBeruecksichtigt.add(wepDto.getIId());

									DocPath docPath = new DocPath(new DocNodeWEPosition(wepDto, weDto, bsDto));
									try {
//										List<DocNodeBase> docs = getJCRDocFac().getDocNodeChildrenFromNode(docPath,
//												theClientDto);
										List<DocNodeBase> docs = getJCRDocFac().getDocNodeChildrenFromNode(docPath,
												theClientDto);

										for (int y = 0; y < docs.size(); y++) {

											DocNodeBase base = docs.get(y);

											if (base.getNodeType() == DocNodeBase.FILE) {

												JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();

												if (sBelegartDokumentenablage != null) {
													if (jcrDocDto.getsBelegart() == null || !jcrDocDto.getsBelegart()
															.equals(sBelegartDokumentenablage)) {
														continue;
													}
												}

												if (sGruppierung != null) {
													if (jcrDocDto.getsGruppierung() == null
															|| !jcrDocDto.getsGruppierung().equals(sGruppierung)) {
														continue;
													}
												}

												ArrayList<JCRDocDto> alDokumenteEinesArtikels = null;

												if (alDokumente.containsKey(artikelnummer)) {
													alDokumenteEinesArtikels = alDokumente.get(artikelnummer);
												} else {
													alDokumenteEinesArtikels = new ArrayList<JCRDocDto>();
												}

												if (bAlleVersionen) {
													ArrayList<DocNodeVersion> versions = getJCRDocFac()
															.getAllVersions(jcrDocDto);

													for (int j = 0; j < versions.size(); j++) {
														JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();
														JCRDocDto jcrData = getJCRDocFac().getData(jcrDocDtoVersion);
														alDokumenteEinesArtikels.add(jcrData);
													}
												} else {
													DocNodeVersion version = getJCRDocFac()
															.getLastVersionOfJcrDoc(jcrDocDto);
													JCRDocDto jcrData = getJCRDocFac().getData(version.getJCRDocDto());
													alDokumenteEinesArtikels.add(jcrData);
												}

												alDokumente.put(artikelnummer, alDokumenteEinesArtikels);

											}
										}
									} catch (Throwable e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}
		}

		return alDokumente;
	}

	private BigDecimal getGestpreisFuerRuecklieferung(LieferscheinpositionDto oDtoI, Integer lagerIId,
			String sSerienchargennummer, TheClientDto theClientDto) {
		BigDecimal gestpreis = new BigDecimal(0);
		try {
			gestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(oDtoI.getArtikelIId(), lagerIId,
					theClientDto);

			if (oDtoI.getAuftragpositionIId() != null) {

				Integer auftragIId = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(oDtoI.getAuftragpositionIId()).getBelegIId();
				// Nachsehen
				Query query = em.createNamedQuery("AuftragpositionfindByAuftragIIdArtikelIId");
				query.setParameter(1, auftragIId);
				query.setParameter(2, oDtoI.getArtikelIId());
				Collection c = query.getResultList();

				if (c.size() > 0) {
					Auftragposition p = (Auftragposition) c.iterator().next();

					query = em.createNamedQuery("LieferscheinpositionfindPositiveByAuftragpositionIIdArtikelIId");
					query.setParameter(1, p.getIId());
					query.setParameter(2, oDtoI.getArtikelIId());
					c = query.getResultList();
					if (c.size() > 0) {
						Lieferscheinposition l = (Lieferscheinposition) c.iterator().next();
						gestpreis = getLagerFac().getGestehungspreisEinerAbgangspositionMitTransaktion(
								LocaleFac.BELEGART_LIEFERSCHEIN, l.getIId(), sSerienchargennummer);
					}

				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return gestpreis;

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(600)
	public Integer createLieferscheinposition(LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return createLieferscheinposition(lieferscheinpositionDtoI, bArtikelSetAufloesen,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
	}

	public Integer reservierungAufloesen(Integer auftragIId, LieferscheinpositionDto lsPosDto,
			TheClientDto theClientDto) {
		try {
			Integer lieferscheinIId = getAuftragFac().vorhandenenLieferscheinEinesAuftagsHolenBzwNeuAnlegen(auftragIId,
					theClientDto);
			lsPosDto.setBelegIId(lieferscheinIId);

			createLieferscheinposition(lsPosDto, true, theClientDto);
			return lieferscheinIId;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	/**
	 * Eine neue Lieferscheinposition anlegen. <br>
	 * Preisinformationen werden in Lieferscheinwaehrung abgelegt.
	 * <ul>
	 * <li>PK generieren.
	 * <li>Bei Positionsart Handeingabe einen Handartikel anlegen, wenn es noch
	 * keinen gibt.
	 * <li>Lieferscheinstatus anpassen.
	 * <li>Lieferscheinposition anlegen und die zusaetzlichen Preisfelder befuellen.
	 * <li>Bei Bezug auf Auftrag die Auftragreservierung anpassen.
	 * <li>Bei Positionsart Ident eine Lagerbuchung ausloesen.
	 * </ul>
	 * 
	 * @param lieferscheinpositionDtoI die Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @return Integer PK der neuen Lieferscheinposition
	 * @throws RemoteException
	 */
	@org.jboss.ejb3.annotation.TransactionTimeout(600)
	public Integer createLieferscheinposition(LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen, List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return createLieferscheinpositionService(lieferscheinpositionDtoI, bArtikelSetAufloesen, identities, false,
				theClientDto);
		/*
		 * checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		 * 
		 * if (lieferscheinpositionDtoI.getPositionsartCNr().equals(
		 * LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME) &&
		 * lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
		 * lieferscheinpositionDtoI.getBelegIId(),
		 * LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME) != null) { throw
		 * new EJBExceptionLP(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT, new
		 * Exception("Eine Position Endsumme existiert bereits.")); }
		 * 
		 * // PK generieren Integer iIdLieferscheinposition = getPKGeneratorObj()
		 * .getNextPrimaryKey(PKConst.PK_LIEFERSCHEINPOSITION);
		 * lieferscheinpositionDtoI.setIId(iIdLieferscheinposition);
		 * 
		 * // Sortierung: falls nicht anders definiert, hinten dran haengen. if
		 * (lieferscheinpositionDtoI.getISort() == null) { int iSortNeu =
		 * getMaxISort(lieferscheinpositionDtoI.getBelegIId()) + 1;
		 * lieferscheinpositionDtoI.setISort(iSortNeu); }
		 * 
		 * if (lieferscheinpositionDtoI.getBKeinlieferrest() == null) {
		 * lieferscheinpositionDtoI.setBKeinlieferrest(new Short((short) 0)); }
		 * 
		 * // Tests schicken auch mal null in das Feld if (lieferscheinpositionDtoI
		 * .getNNettoeinzelpreisplusversteckteraufschlag() == null) { if
		 * (lieferscheinpositionDtoI.isMengenbehaftet()) { lieferscheinpositionDtoI
		 * .setNEinzelpreisplusversteckteraufschlag(BigDecimal.ZERO); } } if
		 * (lieferscheinpositionDtoI
		 * .getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() == null) { if
		 * (lieferscheinpositionDtoI.isMengenbehaftet()) { lieferscheinpositionDtoI
		 * .setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO); }
		 * }
		 * 
		 * // wenn es sich um eine Handeingabeposition handelt und kein bestehender //
		 * Artikel mitgeliefert wird, muss ein Handartikel angelegt werden if
		 * (lieferscheinpositionDtoI.isHandeingabe() &&
		 * lieferscheinpositionDtoI.getArtikelIId() == null) { Integer iIdArtikel =
		 * null;
		 * 
		 * try { ArtikelDto oArtikelDto = new ArtikelDto();
		 * oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
		 * 
		 * ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
		 * oArtikelsprDto.setCBez(lieferscheinpositionDtoI.getCBez());
		 * oArtikelsprDto.setCZbez(lieferscheinpositionDtoI .getCZusatzbez());
		 * 
		 * oArtikelDto.setArtikelsprDto(oArtikelsprDto);
		 * oArtikelDto.setEinheitCNr(lieferscheinpositionDtoI .getEinheitCNr());
		 * 
		 * // Der Artikel erhaelt die Mwst-Satz-Bezeichnung MwstsatzDto mwstsatzDto =
		 * getMandantFac() .mwstsatzFindByPrimaryKey(
		 * lieferscheinpositionDtoI.getMwstsatzIId(), theClientDto);
		 * oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId()); //
		 * Handartikel anlegen iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
		 * theClientDto); } catch (RemoteException ex) {
		 * throwEJBExceptionLPRespectOld(ex); }
		 * 
		 * lieferscheinpositionDtoI.setArtikelIId(iIdArtikel); }
		 * 
		 * LieferscheinpositionDto vorherigelieferscheinpositionDtoI = null; try { int
		 * iSort = getMaxISort(lieferscheinpositionDtoI.getBelegIId()); if
		 * (lieferscheinpositionDtoI.getISort() != null) { iSort =
		 * lieferscheinpositionDtoI.getISort() - 1; } Query query = em
		 * .createNamedQuery("LieferscheinpositionfindByLieferscheinISort");
		 * query.setParameter(1, lieferscheinpositionDtoI.getLieferscheinIId());
		 * query.setParameter(2, iSort); vorherigelieferscheinpositionDtoI =
		 * assembleLieferscheinpositionDto((Lieferscheinposition) query
		 * .getSingleResult());
		 * 
		 * } catch (NoResultException ex1) { }
		 * 
		 * LieferscheinDto lsDto = getLieferscheinFac() .lieferscheinFindByPrimaryKey(
		 * lieferscheinpositionDtoI.getBelegIId(), theClientDto);
		 * 
		 * if(getMandantFac().hatZusatzfunktionForecastAuftragVerteilen(theClientDto)) {
		 * if(lieferscheinpositionDtoI.getArtikelIId() != null) { DistributeResult r =
		 * distributeForecast(lsDto, lieferscheinpositionDtoI,
		 * vorherigelieferscheinpositionDtoI, bArtikelSetAufloesen, identities,
		 * theClientDto); if(r.getAmount().signum() == 0) { return
		 * r.getLastPositionId(); }
		 * 
		 * r = distributeOrder(lsDto, lieferscheinpositionDtoI,
		 * vorherigelieferscheinpositionDtoI, bArtikelSetAufloesen, identities,
		 * theClientDto); if(r.getAmount().signum() == 0) { return
		 * r.getLastPositionId(); }
		 * 
		 * throw EJBExcFactory.keinForecastOderAuftragGefunden(
		 * lieferscheinpositionDtoI.getArtikelIId(), lsDto.getKundeIIdLieferadresse());
		 * } }
		 * 
		 * return createLieferscheinpositionImpl(lsDto,
		 * lieferscheinpositionDtoI,vorherigelieferscheinpositionDtoI,
		 * bArtikelSetAufloesen, identities, theClientDto);
		 */
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(600)
	@Override
	public Integer createLieferscheinpositionService(LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen, List<SeriennrChargennrMitMengeDto> identities, boolean erlaubeVerteilen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		if (lieferscheinpositionDtoI.getPositionsartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)
				&& lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
						lieferscheinpositionDtoI.getBelegIId(),
						LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME) != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ENDSUMME_EXISTIERT,
					new Exception("Eine Position Endsumme existiert bereits."));
		}

		// PK generieren
		Integer iIdLieferscheinposition = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LIEFERSCHEINPOSITION);
		lieferscheinpositionDtoI.setIId(iIdLieferscheinposition);

		// Sortierung: falls nicht anders definiert, hinten dran haengen.
		if (lieferscheinpositionDtoI.getISort() == null) {
			int iSortNeu = getMaxISort(lieferscheinpositionDtoI.getBelegIId()) + 1;
			lieferscheinpositionDtoI.setISort(iSortNeu);
		}

		if (lieferscheinpositionDtoI.getBKeinlieferrest() == null) {
			lieferscheinpositionDtoI.setBKeinlieferrest(new Short((short) 0));
		}

		// Tests schicken auch mal null in das Feld
		if (lieferscheinpositionDtoI.getNNettoeinzelpreisplusversteckteraufschlag() == null) {
			if (lieferscheinpositionDtoI.isMengenbehaftet()) {
				lieferscheinpositionDtoI.setNEinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			}
		}
		if (lieferscheinpositionDtoI.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte() == null) {
			if (lieferscheinpositionDtoI.isMengenbehaftet()) {
				lieferscheinpositionDtoI.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			}
		}

		// wenn es sich um eine Handeingabeposition handelt und kein bestehender
		// Artikel mitgeliefert wird, muss ein Handartikel angelegt werden
		if (lieferscheinpositionDtoI.isHandeingabe() && lieferscheinpositionDtoI.getArtikelIId() == null) {
			Integer iIdArtikel = null;

			try {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(lieferscheinpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(lieferscheinpositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(lieferscheinpositionDtoI.getEinheitCNr());

				// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
				MwstsatzDto mwstsatzDto = getMandantFac()
						.mwstsatzFindByPrimaryKey(lieferscheinpositionDtoI.getMwstsatzIId(), theClientDto);
				oArtikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
				// Handartikel anlegen
				iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

			lieferscheinpositionDtoI.setArtikelIId(iIdArtikel);
		}

		LieferscheinpositionDto vorherigelieferscheinpositionDtoI = null;
		try {
			int iSort = getMaxISort(lieferscheinpositionDtoI.getBelegIId());
			if (lieferscheinpositionDtoI.getISort() != null) {
				iSort = lieferscheinpositionDtoI.getISort() - 1;
			}
			Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinISort");
			query.setParameter(1, lieferscheinpositionDtoI.getLieferscheinIId());
			query.setParameter(2, iSort);
			vorherigelieferscheinpositionDtoI = assembleLieferscheinpositionDto(
					(Lieferscheinposition) query.getSingleResult());

		} catch (NoResultException ex1) {
		}

		LieferscheinDto lsDto = getLieferscheinFac()
				.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getBelegIId(), theClientDto);

		// Es ist kein Verteilen, wenn Forecast oder Auftrag bereits gesetzt
		if (erlaubeVerteilen && (lieferscheinpositionDtoI.getAuftragpositionIId() != null
				|| lieferscheinpositionDtoI.getForecastpositionIId() != null)) {
			erlaubeVerteilen = false;
		}

		// Es ist kein Verteilen, wenn Lieferantenlieferschein (Rueckliefern von Ware)
		if (erlaubeVerteilen && LieferscheinFac.LSART_LIEFERANT.equals(lsDto.getLieferscheinartCNr())) {
			erlaubeVerteilen = false;
		}

		if (erlaubeVerteilen && lieferscheinpositionDtoI.getArtikelIId() != null
				&& getMandantFac().hatZusatzfunktionForecastAuftragVerteilen(theClientDto)) {
			DistributeResult r = distributeForecast(lsDto, lieferscheinpositionDtoI, vorherigelieferscheinpositionDtoI,
					bArtikelSetAufloesen, identities, theClientDto);
			if (r.getAmount().signum() == 0) {
				return r.getLastPositionId();
			}

			r = distributeOrder(lsDto, lieferscheinpositionDtoI, vorherigelieferscheinpositionDtoI,
					bArtikelSetAufloesen, identities, theClientDto);
			if (r.getAmount().signum() == 0) {
				return r.getLastPositionId();
			}

			r = distributeVkPreispflichtig(lsDto, lieferscheinpositionDtoI, vorherigelieferscheinpositionDtoI,
					bArtikelSetAufloesen, identities, theClientDto);
			if (r.getAmount().signum() == 0) {
				return r.getLastPositionId();
			}

			throw EJBExcFactory.keinForecastOderAuftragGefunden(lieferscheinpositionDtoI.getArtikelIId(),
					lsDto.getKundeIIdLieferadresse());
		}

		return createLieferscheinpositionImpl(lsDto, lieferscheinpositionDtoI, vorherigelieferscheinpositionDtoI,
				bArtikelSetAufloesen, identities, theClientDto);
	}

	private Integer createLieferscheinpositionImpl(LieferscheinDto lsDto,
			LieferscheinpositionDto lieferscheinpositionDtoI, LieferscheinpositionDto vorherigelieferscheinpositionDtoI,
			boolean bArtikelSetAufloesen, List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws RemoteException {
		// wg. PJ19087
		if (lieferscheinpositionDtoI.getNNettoeinzelpreis() == null) {
			lieferscheinpositionDtoI = (LieferscheinpositionDto) befuellePreisfelderAnhandVKPreisfindung(
					lieferscheinpositionDtoI, lsDto.getTBelegdatum(), lsDto.getKundeIIdRechnungsadresse(),
					lsDto.getWaehrungCNr(), theClientDto);
		}

		pruefePflichtfelderBelegposition(lieferscheinpositionDtoI, theClientDto);

		lieferscheinpositionDtoI = (LieferscheinpositionDto) befuellepositionBelegpositionDtoVerkauf(
				vorherigelieferscheinpositionDtoI, lieferscheinpositionDtoI, theClientDto);

		if (lieferscheinpositionDtoI.getPositioniId() == null && lieferscheinpositionDtoI.getTypCNr() != null) {
			if (!lieferscheinpositionDtoI.getLieferscheinpositionartCNr().equals(LocaleFac.POSITIONSART_POSITION))
				lieferscheinpositionDtoI = befuelleZusaetzlichePositionfelder(lieferscheinpositionDtoI, theClientDto);
		}

		if (lieferscheinpositionDtoI.getTypCNr() != null
				&& (lieferscheinpositionDtoI.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)
						|| lieferscheinpositionDtoI.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE2))
				&& lieferscheinpositionDtoI.getPositioniId() != null) {
			LieferscheinpositionDto[] dtos = lieferscheinpositionFindByPositionIId(
					lieferscheinpositionDtoI.getPositioniId());
			for (int i = 0; i < dtos.length; i++) {
				if (lieferscheinpositionDtoI.getMwstsatzIId() != null) {
					if (dtos[i].getMwstsatzIId() != null) {
						if (!lieferscheinpositionDtoI.getMwstsatzIId().equals(dtos[i].getMwstsatzIId())) {
							// MWST-Saetze innerhalb "Position" muessen
							// immer gleich sein
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_STEUERSATZ_INNERHALB_UNTERPOSITIONEN_UNGLEICH, "");
						}
					}
				}
			}
		}

		// die Lieferscheinposition anlegen
		Lieferscheinposition lieferscheinposition = null;

		try {

			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {

				AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(lieferscheinpositionDtoI.getAuftragpositionIId());
				if (lieferscheinpositionDtoI.getXTextinhalt() == null) {
					lieferscheinpositionDtoI.setXTextinhalt(auftragpositionDto.getXTextinhalt());
				}
			} else {
				// Wenn kein Auftragspositionsbezug, dann Zuschlag/Datum holen

				if (lieferscheinpositionDtoI.getPositionsartCNr()
						.equalsIgnoreCase(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					if (lieferscheinpositionDtoI.getNMaterialzuschlagKurs() == null) {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(lieferscheinpositionDtoI.getArtikelIId(), theClientDto);
						if (artikelDto.getMaterialIId() != null) {

							MaterialzuschlagDto mDto = getMaterialFac().getKursMaterialzuschlagDtoVKInZielwaehrung(
									lsDto.getKundeIIdRechnungsadresse(), artikelDto.getIId(),
									new java.sql.Date(lsDto.getTBelegdatum().getTime()), lsDto.getWaehrungCNr(),
									theClientDto);
							if (mDto != null) {
								lieferscheinpositionDtoI.setNMaterialzuschlagKurs(mDto.getNZuschlag());
								lieferscheinpositionDtoI.setTMaterialzuschlagDatum(mDto.getTGueltigab());
							}
						}
					}

				}

			}

			getLieferscheinFac()
					.pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinpositionDtoI.getLieferscheinIId());

			lieferscheinposition = new Lieferscheinposition(lieferscheinpositionDtoI.getIId(),
					lieferscheinpositionDtoI.getLieferscheinIId(), lieferscheinpositionDtoI.getISort(),
					lieferscheinpositionDtoI.getLieferscheinpositionartCNr(),
					lieferscheinpositionDtoI.getBNettopreisuebersteuert(),
					lieferscheinpositionDtoI.getBKeinlieferrest());
			lieferscheinposition.setBZwsPositionspreisZeigen(Helper.getShortTrue());
			em.persist(lieferscheinposition);
			em.flush();

			setLieferscheinpositionFromLieferscheinpositionDto(lieferscheinposition, lieferscheinpositionDtoI);

			
			getBelegartmediaFac().kopiereBelegartmedia(lieferscheinpositionDtoI.getUsecaseIIdQuelle(),
					lieferscheinpositionDtoI.getIKeyQuelle(), QueryParameters.UC_ID_LIEFERSCHEINPOSITION, lieferscheinpositionDtoI.getIId(),
					theClientDto);
			
			
			
			befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(), theClientDto);

			LieferscheinpositionDto[] alleLsPos = lieferscheinpositionFindByLieferscheinIId(
					lieferscheinpositionDtoI.getLieferscheinIId());
			Set<Integer> modifiedPositions = new HashSet<Integer>();
			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

			getBelegVerkaufFac().berechneBelegpositionVerkauf(lieferscheinpositionDtoI, lieferscheinDto, alleLsPos,
					modifiedPositions);

			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
				getBelegartmediaFac().kopiereBelegartmedia(QueryParameters.UC_ID_AUFTRAGPOSITION,
						lieferscheinpositionDtoI.getAuftragpositionIId(), QueryParameters.UC_ID_LIEFERSCHEINPOSITION,
						lieferscheinpositionDtoI.getIId(), theClientDto);
			}

			// IMS 2129
			if (lieferscheinpositionDtoI.getNMenge() != null
					&& lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
				// Wenn es einen Bezug zum Auftrag gibt, muss die
				// Auftragposition angepasst werden
				if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
					getAuftragpositionFac().updateOffeneMengeAuftragposition(
							lieferscheinpositionDtoI.getAuftragpositionIId(), theClientDto);
				}

				if (lieferscheinpositionDtoI.getForecastpositionIId() != null) {

					getForecastFac().reservierungenMitForecastSynchronisieren(null, null,
							lieferscheinpositionDtoI.getForecastpositionIId());
				}

				// Wenn es sich um eine Artikelposition handelt, muss
				// eine neue Lagerbuchung gemacht werden. Es gilt: Handartikel
				// sind nicht lagerbewirtschaftet.
				if (lieferscheinpositionDtoI.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bucheAbLager(lieferscheinpositionDtoI, theClientDto);
				}
			} else if (lieferscheinpositionDtoI.getNMenge() != null
					&& lieferscheinpositionDtoI.getNMenge().doubleValue() < 0) {
				// Wenn es einen Bezug zum Auftrag gibt, muss die
				// Auftragposition angepasst werden
				if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
					getAuftragpositionFac().updateOffeneMengeAuftragposition(
							lieferscheinpositionDtoI.getAuftragpositionIId(), theClientDto);
				}
				if (lieferscheinpositionDtoI.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					bucheZuLager(lieferscheinpositionDtoI, theClientDto);
				}
			} /*
				 * lt. WH 16.1.2013 else { if (lieferscheinpositionDtoI.getAuftragpositionIId()
				 * != null) { AuftragpositionDto auftragpositionDto = getAuftragpositionFac()
				 * .auftragpositionFindByPrimaryKey( lieferscheinpositionDtoI
				 * .getAuftragpositionIId()); auftragpositionDto .setAuftragpositionstatusCNr
				 * (AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT); getAuftragpositionFac()
				 * .updateAuftragpositionOhneWeitereAktion( auftragpositionDto, theClientDto); }
				 * }
				 */

			sortierungAnpassenInBezugAufEndsumme(lieferscheinpositionDtoI.getBelegIId(), theClientDto);

			// PJ 14648 Wenn Setartikel, dann die zugehoerigen Artikel ebenfalls
			// buchen:
			if (bArtikelSetAufloesen == true && lieferscheinpositionDtoI.getArtikelIId() != null) {

				if (identities == null) {
					identities = new ArrayList<SeriennrChargennrMitMengeDto>();
				}
				resolveArtikelset(lieferscheinpositionDtoI, identities, theClientDto);
			}

			getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return lieferscheinpositionDtoI.getIId();
	}

	private void resolveArtikelset(LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(lieferscheinpositionDtoI.getArtikelIId(), theClientDto);
		if (stklDto == null)
			return;
		if (!StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(stklDto.getStuecklisteartCNr()))
			return;

		Integer artikelsetHeadIIdLieferscheinPositionIId = lieferscheinpositionDtoI.getIId();

		Integer auftragpositionIId = lieferscheinpositionDtoI.getAuftragpositionIId();
		if (auftragpositionIId != null) {
			resolveArtikelsetFromAuftragArtikelset(lieferscheinpositionDtoI, identities, auftragpositionIId,
					theClientDto);
		} else {
			resolveArtikelsetFromStueckliste(lieferscheinpositionDtoI, identities, stklDto.getIId(), theClientDto);
		}

		preiseEinesArtikelsetsUpdaten(artikelsetHeadIIdLieferscheinPositionIId, theClientDto);
	}

	private void resolveArtikelsetFromStueckliste(LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities, Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lieferscheinPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);

		List<?> m = null;
		try {
			m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
					StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, false, false,
					lieferscheinpositionDtoI.getNMenge(), null, true);
		} catch (RemoteException ex4) {
			throwEJBExceptionLPRespectOld(ex4);
		}

		Iterator<?> it = m.listIterator();
		List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
				identities);

		while (it.hasNext()) {
			StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
			StuecklistepositionDto position = struktur.getStuecklistepositionDto();

			// SP6099 Kalkulatorische Artikel auslassen
			if (position.getArtikelIId() != null) {
				Artikel artikel = em.find(Artikel.class, position.getArtikelIId());
				if (Helper.short2boolean(artikel.getBKalkulatorisch())) {
					continue;
				}
			}

			lieferscheinpositionDtoI.setNEinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNMwstbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNRabattbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setFZusatzrabattsatz(0D);
			lieferscheinpositionDtoI.setFRabattsatz(0D);
			lieferscheinpositionDtoI.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNBruttoeinzelpreis(BigDecimal.ZERO);

			lieferscheinpositionDtoI.setNMenge(Helper
					.rundeKaufmaennisch(position.getNZielmenge(lieferscheinPositionDtoKopfartikel.getNMenge()), 4));

			lieferscheinpositionDtoI.setArtikelIId(position.getArtikelIId());
			lieferscheinpositionDtoI.setEinheitCNr(position.getEinheitCNr());
			lieferscheinpositionDtoI.setPositioniIdArtikelset(lieferscheinPositionDtoKopfartikel.getIId());
			lieferscheinpositionDtoI.setIId(null);

			int iSort = lieferscheinpositionDtoI.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
					iSort);

			lieferscheinpositionDtoI.setISort(iSort);
			lieferscheinpositionDtoI.setSeriennrChargennrMitMenge(null);
			lieferscheinpositionDtoI.setIId(null);

			getBelegVerkaufFac().setupPositionWithIdentities(lieferscheinpositionDtoI, notyetUsedIdentities,
					theClientDto);

			createLieferscheinposition(lieferscheinpositionDtoI, false, theClientDto);
		}
	}

	private void resolveArtikelsetFromAuftragArtikelset(LieferscheinpositionDto lieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities, Integer auftragpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lieferscheinPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);

		Auftragposition headAuftragposition = em.find(Auftragposition.class, auftragpositionIId);
		BigDecimal auftragposHeadMenge = headAuftragposition.getNMenge();
		BigDecimal lsposHeadMenge = lieferscheinPositionDtoKopfartikel.getNMenge();

		List<Auftragposition> auftragpositionen = AuftragpositionQuery.listByPositionIIdArtikelset(em,
				auftragpositionIId);

		for (Auftragposition auftragposition : auftragpositionen) {
			if (auftragposition.getNMenge() != null && auftragposition.getNOffeneMenge().signum() == 0) {
				continue;
			}

			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities = new ArrayList<SeriennrChargennrMitMengeDto>(
					identities);

			// SP6099 Kalkulatorische Artikel auslassen
			if (auftragposition.getArtikelIId() != null) {
				Artikel artikel = em.find(Artikel.class, auftragposition.getArtikelIId());
				if (Helper.short2boolean(artikel.getBKalkulatorisch())) {
					continue;
				}
			}

			lieferscheinpositionDtoI.setNEinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreis(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNMwstbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setFZusatzrabattsatz(0D);
			lieferscheinpositionDtoI.setFRabattsatz(0D);
			lieferscheinpositionDtoI.setNRabattbetrag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreisplusversteckteraufschlag(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(BigDecimal.ZERO);
			lieferscheinpositionDtoI.setNBruttoeinzelpreis(BigDecimal.ZERO);

			BigDecimal m = auftragposition.getNMenge().multiply(lsposHeadMenge).divide(auftragposHeadMenge, 4,
					BigDecimal.ROUND_HALF_EVEN);
			lieferscheinpositionDtoI.setNMenge(m);
			lieferscheinpositionDtoI.setArtikelIId(auftragposition.getArtikelIId());
			lieferscheinpositionDtoI.setEinheitCNr(auftragposition.getEinheitCNr());
			lieferscheinpositionDtoI.setAuftragpositionIId(auftragposition.getIId());
			lieferscheinpositionDtoI.setPositioniIdArtikelset(lieferscheinPositionDtoKopfartikel.getIId());
			lieferscheinpositionDtoI.setIId(null);

			int iSort = lieferscheinpositionDtoI.getISort() + 1;

			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
					iSort);

			lieferscheinpositionDtoI.setISort(iSort);
			lieferscheinpositionDtoI.setSeriennrChargennrMitMenge(null);
			lieferscheinpositionDtoI.setIId(null);
			getBelegVerkaufFac().setupPositionWithIdentities(lieferscheinpositionDtoI, notyetUsedIdentities,
					theClientDto);

			createLieferscheinposition(lieferscheinpositionDtoI, false, theClientDto);
		}
	}

	private boolean hasSeriennrchargennr(List<SeriennrChargennrMitMengeDto> snrs) {
		if (null == snrs)
			return false;
		if (snrs.size() == 0)
			return false;

		if (snrs.size() > 1)
			return true;

		List<GeraetesnrDto> geraete = snrs.get(0).getAlGeraetesnr();
		if (geraete != null && geraete.size() > 0)
			return true;

		return snrs.get(0).getCSeriennrChargennr() != null;
	}

	public List<SeriennrChargennrMitMengeDto> getSeriennrchargennrForArtikelsetPosition(Integer lieferscheinposIId)
			throws EJBExceptionLP {
		if (lieferscheinposIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("lieferscheinposIId == null"));
		}

		List<SeriennrChargennrMitMengeDto> allSnrs = new ArrayList<SeriennrChargennrMitMengeDto>();
		List<Lieferscheinposition> positions = LieferscheinpositionQuery.listByPositionIIdArtikelset(em,
				lieferscheinposIId);
		for (Lieferscheinposition lieferscheinposition : positions) {
			List<SeriennrChargennrMitMengeDto> snrs = getLagerFac()
					.getAllSeriennrchargennrEinerBelegartpositionOhneChargeneigenschaften(
							LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinposition.getIId());
			if (hasSeriennrchargennr(snrs)) {
				allSnrs.addAll(snrs);
			}
		}

		return allSnrs;
	}

	public LieferscheinpositionDto befuelleZusaetzlichePositionfelder(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
			Session session = null;
			// try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			FLRAuftragposition identAuftragposition = (FLRAuftragposition) session.load(FLRAuftragposition.class,
					lieferscheinpositionDtoI.getAuftragpositionIId());
			FLRAuftragposition position = (FLRAuftragposition) session.load(FLRAuftragposition.class,
					identAuftragposition.getPosition_i_id());
			int ipsort = 0;
			ipsort = identAuftragposition.getI_sort() - position.getI_sort();
			Criteria crit = session.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq("auftragposition_i_id", position.getI_id()));
			crit.add(Restrictions.eq("flrlieferschein.i_id", lieferscheinpositionDtoI.getLieferscheinIId()));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			if (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter.next();
				lieferscheinpositionDtoI.setPositioniId(pos.getI_id());
				lieferscheinpositionDtoI.setISort(pos.getI_sort() + ipsort);
				Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
				query.setParameter(1, lieferscheinpositionDtoI.getLieferscheinIId());
				Collection<?> cl = query.getResultList();
				// if (cl.isEmpty()) {
				// return lieferscheinpositionDtoI;
				// }
				Iterator<?> it = cl.iterator();
				while (it.hasNext()) {
					Lieferscheinposition oPosition = (Lieferscheinposition) it.next();
					if (oPosition.getISort().intValue() > lieferscheinpositionDtoI.getISort()) {
						oPosition.setISort(oPosition.getISort() + 1);
					}
				}

				sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
						lieferscheinpositionDtoI.getISort());
				getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
				return lieferscheinpositionDtoI;
			}
			// }
			// catch (FinderException ex) {
			// return lieferscheinpositionDtoI;
			// }

			if (session != null) {
				session.close();
			}
		}
		return lieferscheinpositionDtoI;
	}

	/**
	 * Fuer eine bestehende Lieferscheinposition vom Typ Ident oder Handeingabe
	 * werden die zusaetzlichen Preisfelder befuellt.
	 * 
	 * @param iIdPositionI PK der Position
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 * @throws EJBExceptionLP
	 */
	public LieferscheinpositionDto befuelleZusaetzlichePreisfelder(Integer iIdPositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			Lieferscheinposition oPosition = em.find(Lieferscheinposition.class, iIdPositionI);
			if (oPosition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByPrimaryKey(iIdPositionI,
					theClientDto);
			if (oPosition.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)
					|| oPosition.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

				LieferscheinDto lieferscheinDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(oPosition.getLieferscheinIId(), theClientDto);
				lieferscheinpositionDto = (LieferscheinpositionDto) getBelegVerkaufFac()
						.berechneBelegpositionVerkauf(lieferscheinpositionDto, lieferscheinDto);

				oPosition.setNNettoeinzelpreisplusversteckteraufschlag(
						lieferscheinpositionDto.getNEinzelpreisplusversteckteraufschlag());
				oPosition.setNNettogesamtpreisplusversteckteraufschlag(
						lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlag());
				oPosition.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
						lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());

				/*
				 * // den versteckten Aufschlag aus den Konditionen beruecksichtigen BigDecimal
				 * bdVersteckterAufschlag = new BigDecimal(ls. getFVersteckteraufschlag().
				 * doubleValue()).movePointLeft(2); bdVersteckterAufschlag =
				 * Helper.rundeKaufmaennisch(bdVersteckterAufschlag, 4);
				 * 
				 * BigDecimal bdNettoeinzelpreisVersteckterAufschlagSumme = oPosition
				 * .getNNettoeinzelpreis().multiply(bdVersteckterAufschlag);
				 * bdNettoeinzelpreisVersteckterAufschlagSumme = Helper.rundeKaufmaennisch(
				 * bdNettoeinzelpreisVersteckterAufschlagSumme, 4);
				 * 
				 * 
				 * 
				 * oPosition.setNNettoeinzelpreisplusversteckteraufschlag(oPosition .
				 * getNNettoeinzelpreis(). add( bdNettoeinzelpreisVersteckterAufschlagSumme));
				 * 
				 * BigDecimal bdNettogesamtpreisVersteckterAufschlagSumme = oPosition
				 * .getNNettogesamtpreis().multiply(bdVersteckterAufschlag);
				 * bdNettogesamtpreisVersteckterAufschlagSumme = Helper.rundeKaufmaennisch(
				 * bdNettogesamtpreisVersteckterAufschlagSumme, 4);
				 * oPosition.setNNettogesamtpreisplusversteckteraufschlag (oPosition.
				 * getNNettogesamtpreis(). add( bdNettogesamtpreisVersteckterAufschlagSumme));
				 * 
				 * // die Abschlaege werden auf Basis des Versteckten Aufschlags beruecksichtigt
				 * 
				 * // - Allgemeiner Rabatt BigDecimal bdAllgemeinerRabatt = new BigDecimal(ls.
				 * getFAllgemeinerrabatt(). doubleValue()).movePointLeft(2); bdAllgemeinerRabatt
				 * = Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);
				 * 
				 * BigDecimal bdNettogesamtpreisAllgemeinerRabattSumme = oPosition
				 * .getNNettogesamtpreisplusversteckteraufschlag().multiply(
				 * bdAllgemeinerRabatt); bdNettogesamtpreisAllgemeinerRabattSumme =
				 * Helper.rundeKaufmaennisch( bdNettogesamtpreisAllgemeinerRabattSumme, 4);
				 * 
				 * 
				 * 
				 * 
				 * oPosition.setNNettogesamtpreisplusversteckteraufschlagminusrabatt (oPosition.
				 * getNNettogesamtpreisplusversteckteraufschlag().subtract(
				 * bdNettogesamtpreisAllgemeinerRabattSumme));
				 */
			}

			return lieferscheinpositionDto;

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	// IMS 2129
	public void bucheZuLager(LieferscheinpositionDto oDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (oDtoI == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("oDtoI == null"));
			}

			LieferscheinDto oAktuellerLieferschein = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oDtoI.getLieferscheinIId(), theClientDto);
			Integer iMandantenLager = null;
			Integer iKundeLager = null;
			// Kunden Lager
			if (oAktuellerLieferschein.getZiellagerIId() != null) {

				iKundeLager = oAktuellerLieferschein.getZiellagerIId();
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1).divide(new BigDecimal(
							oAktuellerLieferschein.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()), 4,
							BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				if (iMandantenLager.equals(iKundeLager)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER,
							new Exception("FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER"));
				}

				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(), oAktuellerLieferschein.getIId(),
						oDtoI.getIId(), oDtoI.getArtikelIId(), oDtoI.getNMenge().negate(), bdNettogesamtpreis,
						iKundeLager, oDtoI.getSeriennrChargennrMitMenge(), oAktuellerLieferschein.getTBelegdatum(),
						theClientDto);

				BigDecimal gestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(oDtoI.getArtikelIId(),
						iMandantenLager, theClientDto);

				getLagerFac().bucheZu(LocaleFac.BELEGART_LSZIELLAGER, oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge().negate(), gestpreis, iMandantenLager,
						oDtoI.getSeriennrChargennrMitMenge(), new java.sql.Timestamp(System.currentTimeMillis()),
						theClientDto, oAktuellerLieferschein.getBelegartCNr(), oDtoI.getIId());

			}
			// Mandanten Lager
			else {
				iMandantenLager = oAktuellerLieferschein.getLagerIId();
				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}
				String s = null;

				if (oDtoI.getSeriennrChargennrMitMenge() != null && oDtoI.getSeriennrChargennrMitMenge().size() > 0) {
					s = oDtoI.getSeriennrChargennrMitMenge().get(0).getCSeriennrChargennr();
				}

				getLagerFac().bucheZu(oAktuellerLieferschein.getBelegartCNr(), oAktuellerLieferschein.getIId(),
						oDtoI.getIId(), oDtoI.getArtikelIId(), oDtoI.getNMenge().negate(),
						getGestpreisFuerRuecklieferung(oDtoI, iMandantenLager, s, theClientDto), iMandantenLager,
						oDtoI.getSeriennrChargennrMitMenge(), oAktuellerLieferschein.getTBelegdatum(), theClientDto);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Lagerbuchung fuer eine bestimmte Lieferscheinposition machen. <br>
	 * Lagerbuchungen sind absolut, es zaehlt die Menge, die im uebergebenen Dto
	 * enthalten ist.
	 * 
	 * @param oDtoI        die Position
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void bucheAbLager(LieferscheinpositionDto oDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (oDtoI == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("oDtoI == null"));
			}

			// die zu buchende SNR- oder Chargennummer pruefen
			List<SeriennrChargennrMitMengeDto> sSerienchargennummer = oDtoI.getSeriennrChargennrMitMenge();

			LieferscheinDto oAktuellerLieferschein = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oDtoI.getLieferscheinIId(), theClientDto);

			KostenstelleDto kostenstelleDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(oAktuellerLieferschein.getKostenstelleIId());

			if (kostenstelleDto.getLagerIIdOhneabbuchung() != null) {
				if (oAktuellerLieferschein.getLagerIId().equals(kostenstelleDto.getLagerIIdOhneabbuchung())) {
					// PJ14813
					return;
				}
			}

			Integer iMandantenLager = null;
			Integer iKundeLager = null;
			// Kunden Lager
			if (oAktuellerLieferschein.getZiellagerIId() != null) {
				iKundeLager = oAktuellerLieferschein.getZiellagerIId();
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1).divide(new BigDecimal(
							oAktuellerLieferschein.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()), 4,
							BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				if (iMandantenLager.equals(iKundeLager)) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER,
							new Exception("FEHLER_ZIELLAGER_GLEICH_ABBUCHUNGSLAGER"));
				}

				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(), oAktuellerLieferschein.getIId(),
						oDtoI.getIId(), oDtoI.getArtikelIId(), oDtoI.getNMenge(), bdNettogesamtpreis, iMandantenLager,
						sSerienchargennummer, oAktuellerLieferschein.getTBelegdatum(), theClientDto);
				BigDecimal gestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(oDtoI.getArtikelIId(),
						iMandantenLager, theClientDto);
				getLagerFac().bucheZu(LocaleFac.BELEGART_LSZIELLAGER, oAktuellerLieferschein.getIId(), oDtoI.getIId(),
						oDtoI.getArtikelIId(), oDtoI.getNMenge(), gestpreis, iKundeLager, sSerienchargennummer,
						oAktuellerLieferschein.getTBelegdatum(), theClientDto, oAktuellerLieferschein.getBelegartCNr(),
						oDtoI.getIId());

			}
			// Mandanten Lager
			else {
				iMandantenLager = oAktuellerLieferschein.getLagerIId();

				// PJ18261
				if (oDtoI.getLagerIId() != null) {
					iMandantenLager = oDtoI.getLagerIId();
				}

				// den Nettogesamtpreis in Mandantenwaehrung umrechnen
				BigDecimal bdNettogesamtpreis = oDtoI.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte();

				if (!oAktuellerLieferschein.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
					BigDecimal ddFaktor = new BigDecimal(1).divide(new BigDecimal(
							oAktuellerLieferschein.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()), 4,
							BigDecimal.ROUND_HALF_EVEN);

					bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
				}

				// absolute Lagerbuchung, funktioniert fuer nicht
				// lagerbewirtschaftete Artikel;
				// es gilt: Lagerbewertete Artikel buchen implizit auf KEIN
				// LAGER
				getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(), oAktuellerLieferschein.getIId(),
						oDtoI.getIId(), oDtoI.getArtikelIId(), oDtoI.getNMenge(), bdNettogesamtpreis, iMandantenLager,
						sSerienchargennummer, oAktuellerLieferschein.getTBelegdatum(), theClientDto);

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeLieferscheinpositionen(Object[] idsI, TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinpositionDto lspos = null;
		Integer pk = null;
		for (int i = 0; i < idsI.length; i++) {
			pk = new Integer(idsI[i].toString());
			lspos = lieferscheinpositionFindByPrimaryKey(pk, theClientDto);
			removeLieferscheinposition(lspos, theClientDto);
		}
	}

	public void removeLieferscheinposition(LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// PJ20146
		if (lieferscheinpositionDtoI.getWareneingangspositionIIdAndererMandant() != null) {
			return;
		}

		removeLieferscheinPositionImpl(lieferscheinpositionDtoI, true, theClientDto);
	}

	private void removeLieferscheinPositionImpl(LieferscheinpositionDto lieferscheinpositionDtoI,
			boolean artikelsetAufloesen, TheClientDto theClientDto) {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		try {
			getLieferscheinFac()
					.pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinpositionDtoI.getLieferscheinIId());

			LieferscheinDto ls = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

			if (lieferscheinpositionDtoI.getPositioniIdArtikelset() == null) {
				Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
				query.setParameter(1, lieferscheinpositionDtoI.getIId());
				Collection<?> lieferscheinpositionDtos = query.getResultList();
				LieferscheinpositionDto[] zugehoerigeLSPosDtos = assembleLieferscheinpositionDtos(
						lieferscheinpositionDtos);

				for (int i = 0; i < zugehoerigeLSPosDtos.length; i++) {
					removeLieferscheinPositionImpl(zugehoerigeLSPosDtos[i], false, theClientDto);
				}
			}

			if (ls.getZiellagerIId() != null) {
				storniereLieferscheinpositionZielLager(ls, lieferscheinpositionDtoI.getIId(), theClientDto);
			} else {
				// Rollback aller Buchungen, die das Anlegen bzw. Aendern der
				// Lieferscheinposition ausgeloest hat
				storniereLieferscheinposition(lieferscheinpositionDtoI.getIId(), theClientDto);
			}
			// jetzt die lieferscheinposition loeschen
			Lieferscheinposition toRemove = em.find(Lieferscheinposition.class, lieferscheinpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();

				if (artikelsetAufloesen && lieferscheinpositionDtoI.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(lieferscheinpositionDtoI.getPositioniIdArtikelset(), theClientDto);
				}
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
			getAuftragpositionFac().updateOffeneMengeAuftragposition(lieferscheinpositionDtoI.getAuftragpositionIId(),
					theClientDto);

			if (lieferscheinpositionDtoI.getForecastpositionIId() != null) {
				getForecastFac().reservierungenMitForecastSynchronisieren(null, null,
						lieferscheinpositionDtoI.getForecastpositionIId());
			}

			sortierungAnpassenBeiLoeschenEinerPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
					lieferscheinpositionDtoI.getISort().intValue());

			// wegen Aenderungszeitpunkt
			getLieferscheinFac().updateLieferscheinOhneWeitereAktion(ls, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			EJBExceptionLP ex = null;
			if (t instanceof EJBExceptionLP) {
				ex = (EJBExceptionLP) t;
			}
			if (ex != null) {
				EJBExceptionLP toThrow = new EJBExceptionLP(ex);
				throw toThrow;
			}
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		}
	}

	/**
	 * Eine Lieferscheinposition loeschen.
	 * 
	 * @param lieferscheinpositionDtoI die zu loeschene Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeLieferscheinposition0(LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// PJ20146
		if (lieferscheinpositionDtoI.getWareneingangspositionIIdAndererMandant() != null) {
			return;
		}

		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		try {
			getLieferscheinFac()
					.pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinpositionDtoI.getLieferscheinIId());

			LieferscheinDto ls = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

			if (lieferscheinpositionDtoI.getPositioniIdArtikelset() == null) {

				Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
				query.setParameter(1, lieferscheinpositionDtoI.getIId());
				Collection<?> lieferscheinpositionDtos = query.getResultList();
				LieferscheinpositionDto[] zugehoerigeLSPosDtos = assembleLieferscheinpositionDtos(
						lieferscheinpositionDtos);

				for (int i = 0; i < zugehoerigeLSPosDtos.length; i++) {
					removeLieferscheinposition(zugehoerigeLSPosDtos[i], theClientDto);
				}
			}

			if (ls.getZiellagerIId() != null) {
				storniereLieferscheinpositionZielLager(ls, lieferscheinpositionDtoI.getIId(), theClientDto);
			} else {
				// Rollback aller Buchungen, die das Anlegen bzw. Aendern der
				// Lieferscheinposition ausgeloest hat
				storniereLieferscheinposition(lieferscheinpositionDtoI.getIId(), theClientDto);
			}
			// jetzt die lieferscheinposition loeschen
			Lieferscheinposition toRemove = em.find(Lieferscheinposition.class, lieferscheinpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();

				if (lieferscheinpositionDtoI.getPositioniIdArtikelset() != null) {
					preiseEinesArtikelsetsUpdaten(lieferscheinpositionDtoI.getPositioniIdArtikelset(), theClientDto);
				}

			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}
			getAuftragpositionFac().updateOffeneMengeAuftragposition(lieferscheinpositionDtoI.getAuftragpositionIId(),
					theClientDto);

			if (lieferscheinpositionDtoI.getForecastpositionIId() != null) {

				getForecastFac().reservierungenMitForecastSynchronisieren(null, null,
						lieferscheinpositionDtoI.getForecastpositionIId());
			}

			sortierungAnpassenBeiLoeschenEinerPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
					lieferscheinpositionDtoI.getISort().intValue());

			// wegen Aenderungszeitpunkt
			getLieferscheinFac().updateLieferscheinOhneWeitereAktion(ls, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			EJBExceptionLP ex = null;
			if (t instanceof EJBExceptionLP) {
				ex = (EJBExceptionLP) t;
			}
			if (ex != null) {
				EJBExceptionLP toThrow = new EJBExceptionLP(ex);
				throw toThrow;
			}
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		}
	}

	/**
	 * Eine Lieferscheinposition stornieren. <br>
	 * Der Storno einer Lieferscheinposition loest ein Rollback der Aktionen aus,
	 * die beim Anlegen bzw. Aendern der Position durchgefuehrt wurden. <br>
	 * Die Lieferscheinposition selbst bleibt mit ihren Informationen bestehen. <br>
	 * Der Storno einer Lieferscheinposition kann nicht aufgehoben werden. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param iIdLieferscheinpositionI PK der Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void storniereLieferscheinposition(Integer iIdLieferscheinpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinpositionIId(iIdLieferscheinpositionI);

		LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByPrimaryKey(iIdLieferscheinpositionI,
				theClientDto);

		// IDENT + Bezug zu Auftrag
		// Artikelreservierung anpassen
		// Auftragposition korrigieren
		// Lagerbuchung korrigieren
		// IDENT ohne Bezug Auftrag
		// Lagerbuchung korrigieren
		// HANDEINGABE + Bezug zu Auftrag
		// Auftragposition korrigieren
		// HANDEINGABE ohne Bezug zu Auftrag

		if (lieferscheinpositionDto.getAuftragpositionIId() != null && lieferscheinpositionDto.getNMenge() == null) {
			try {
				AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
						.auftragpositionFindByPrimaryKey(lieferscheinpositionDto.getAuftragpositionIId());
				oAuftragpositionDto.setAuftragpositionstatusCNr(AuftragServiceFac.AUFTRAGPOSITIONSTATUS_OFFEN);
				getAuftragpositionFac().updateAuftragpositionOhneWeitereAktion(oAuftragpositionDto, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		// Schritt 2: Wenn die Lieferscheinposition einen Ident
		// Artikel beinhaltet, muessen weitere Schritte zum Rollback unternommen
		// werden
		if (lieferscheinpositionDto.getArtikelIId() != null) {
			if (lieferscheinpositionDto.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

				if (lieferscheinpositionDto.getWareneingangspositionIIdAndererMandant() != null) {
					try {
						WareneingangspositionDto weposDto = getWareneingangFac().wareneingangspositionFindByPrimaryKey(
								lieferscheinpositionDto.getWareneingangspositionIIdAndererMandant());
						BestellpositionDto bestellpositionDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(weposDto.getBestellpositionIId());
						BestellungDto bestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(bestellpositionDto.getBestellungIId());

						ArrayList al = new ArrayList();
						al.add(bestellungDto.getCNr());
						al.add(bestellungDto.getMandantCNr());

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_LIEFERSCHEINPOSITION_MIT_WE_ANDERERMANDANT_VERKNUEPFT, al,
								new Exception("FEHLER_LIEFERSCHEINPOSITION_MIT_WE_ANDERERMANDANT_VERKNUEPFT"));

					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					}
				}

				if (lieferscheinpositionDto.getNMenge().doubleValue() > 0) {
					// Schritt 2b: die Lagerbuchung rueckgaengig machen;
					// besondere
					// Beruecksichtigung von SNR und Chargennummer
					lieferscheinpositionDto.setNMenge(Helper.getBigDecimalNull()); // die Lagerbuchung bezieht
					// sich immer auf eine
					// bestimmte Position!
					bucheAbLager(lieferscheinpositionDto, theClientDto);
				} else if (lieferscheinpositionDto.getNMenge().doubleValue() < 0) {
					lieferscheinpositionDto.setNMenge(Helper.getBigDecimalNull()); // die Lagerbuchung bezieht
					// sich immer auf eine
					// bestimmte Position!
					bucheZuLager(lieferscheinpositionDto, theClientDto);
				}
			}

			if (lieferscheinpositionDto.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
					|| lieferscheinpositionDto.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {

				try {
					if (lieferscheinpositionDto.getAuftragpositionIId() != null) {
						getAuftragpositionFac().updateOffeneMengeAuftragposition(
								lieferscheinpositionDto.getAuftragpositionIId(), theClientDto);
					}
				} catch (RemoteException ex) {
					throwEJBExceptionLPRespectOld(ex);
				}

			}

		}

		// SP4724 Verbindung zu Auftrag muss geloescht werden
		Lieferscheinposition lsPos = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);
		lsPos.setAuftragpositionIId(null);
		em.merge(lsPos);

	}

	/**
	 * Eine Lieferscheinposition stornieren. <br>
	 * Der Storno einer Lieferscheinposition loest ein Rollback der Aktionen aus,
	 * die beim Anlegen bzw. Aendern der Position durchgefuehrt wurden. <br>
	 * Die Lieferscheinposition selbst bleibt mit ihren Informationen bestehen. <br>
	 * Der Storno einer Lieferscheinposition kann nicht aufgehoben werden. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param oAktuellerLieferschein   LieferscheinDto
	 * @param iIdLieferscheinpositionI PK der Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void storniereLieferscheinpositionZielLager(LieferscheinDto oAktuellerLieferschein,
			Integer iIdLieferscheinpositionI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionIId(iIdLieferscheinpositionI);
		LieferscheinpositionDto oDtoI = lieferscheinpositionFindByPrimaryKey(iIdLieferscheinpositionI, theClientDto);

		try {

			if (oDtoI.getArtikelIId() != null) {
				if (oDtoI.getLieferscheinpositionartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
					if (oDtoI.getAuftragpositionIId() != null) {
						rollbackReservierungZuAuftragposition(oDtoI, theClientDto);
					}
					if (oDtoI.getNMenge().doubleValue() > 0) {
						// lieferscheinpositionDto.setNMenge(Helper.
						// getBigDecimalNull()); // die Lagerbuchung bezieht
						// sich immer auf eine bestimmte Position!

						Integer iQuelleLager = null;
						Integer iZielLager = null;

						iZielLager = oAktuellerLieferschein.getLagerIId();

						// SP2159 //PJ18261
						if (oDtoI.getLagerIId() != null) {
							iZielLager = oDtoI.getLagerIId();
						}

						iQuelleLager = oAktuellerLieferschein.getZiellagerIId();
						// den Nettogesamtpreis in Mandantenwaehrung umrechnen
						BigDecimal bdNettogesamtpreis = oDtoI.getNNettoeinzelpreis();

						if (!oAktuellerLieferschein.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
							BigDecimal ddFaktor = new BigDecimal(1).divide(
									new BigDecimal(oAktuellerLieferschein
											.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()),
									4, BigDecimal.ROUND_HALF_EVEN);

							bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
						}

						// absolute Lagerbuchung, funktioniert fuer nicht
						// lagerbewirtschaftete Artikel;
						// es gilt: Lagerbewertete Artikel buchen implizit
						// auf KEIN LAGER
						getLagerFac().bucheZu(LocaleFac.BELEGART_LSZIELLAGER, oAktuellerLieferschein.getIId(),
								oDtoI.getIId(), oDtoI.getArtikelIId(), new BigDecimal(0), bdNettogesamtpreis,
								iQuelleLager, null, new java.sql.Timestamp(System.currentTimeMillis()), theClientDto);
						getLagerFac().bucheAb(oAktuellerLieferschein.getBelegartCNr(), oAktuellerLieferschein.getIId(),
								oDtoI.getIId(), oDtoI.getArtikelIId(), new BigDecimal(0), bdNettogesamtpreis,
								iZielLager, (List) null, oAktuellerLieferschein.getTBelegdatum(), theClientDto);

					} else if (oDtoI.getNMenge().doubleValue() < 0) {

						ArtikelDto oArtikelDto = getArtikelFac().artikelFindByPrimaryKey(oDtoI.getArtikelIId(),
								theClientDto);

						// die zu buchende SNR- oder Chargennummer pruefen
						List<SeriennrChargennrMitMengeDto> sSerienchargennummer = oDtoI.getSeriennrChargennrMitMenge();

						if (Helper.short2boolean(oArtikelDto.getBChargennrtragend())) {
							if (sSerienchargennummer == null) {
								throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_ISTCHARGENNUMMERNBEHAFET,
										new Exception("sSerienchargennummer == null"));
							}
						}

						if (Helper.short2boolean(oArtikelDto.getBSeriennrtragend())) {
							if (sSerienchargennummer == null) {
								throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_ISTSERIENNUMMERNBEHAFTET,
										new Exception("sSerienchargennummer == null"));
							}

							if (sSerienchargennummer.size() != oDtoI.getNMenge().abs().intValue()
									&& oDtoI.getNMenge().intValue() != 0) {
								throw new EJBExceptionLP(EJBExceptionLP.ARTIKEL_ANZAHLSERIENNUMMERNNICHTKORREKT,
										new Exception("Gewuenschte Menge: " + oDtoI.getNMenge().intValue()));
							}
						}

						Integer iQuelleLager = null;
						Integer iZielLager = null;

						iZielLager = oAktuellerLieferschein.getZiellagerIId();
						iQuelleLager = oAktuellerLieferschein.getLagerIId();

						// SP2159 //PJ18261
						if (oDtoI.getLagerIId() != null) {
							iQuelleLager = oDtoI.getLagerIId();
						}

						// den Nettogesamtpreis in Mandantenwaehrung umrechnen
						BigDecimal bdNettogesamtpreis = oDtoI.getNNettoeinzelpreis();

						if (!oAktuellerLieferschein.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())) {
							BigDecimal ddFaktor = new BigDecimal(1).divide(new BigDecimal(oAktuellerLieferschein
									.getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue()));

							bdNettogesamtpreis = bdNettogesamtpreis.multiply(ddFaktor);
						}

						// bei chargennummerbehafteten Artikeln kann es sein,
						// dass nicht die gesamte Charge verbraucht wird
						if (Helper.short2boolean(oArtikelDto.getBChargennrtragend())) {

							BigDecimal dAbzubuchendeMengeGesamt = oDtoI.getNMenge();

							for (int i = 0; i < sSerienchargennummer.size(); i++) {
								String sChargennummer = sSerienchargennummer.get(i).getCSeriennrChargennr();

								BigDecimal dMengeDerCharge = getLagerFac().getMengeAufLager(oArtikelDto.getIId(),
										oAktuellerLieferschein.getLagerIId(), sChargennummer, theClientDto);

								BigDecimal dAbzubuchendeMengeEinzel = new BigDecimal(-1);

								if (dAbzubuchendeMengeGesamt.doubleValue() < dMengeDerCharge.doubleValue()) {
									dAbzubuchendeMengeEinzel = dAbzubuchendeMengeGesamt;
								} else {
									dAbzubuchendeMengeEinzel = dMengeDerCharge;
								}
								getLagerFac()
										.bucheUm(oDtoI.getArtikelIId(), iQuelleLager, oDtoI.getArtikelIId(), iZielLager,
												dAbzubuchendeMengeEinzel.negate(),
												SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
														sChargennummer, dAbzubuchendeMengeEinzel.negate()),
												"Umbuchung", null, theClientDto);
								dAbzubuchendeMengeGesamt = dAbzubuchendeMengeGesamt.subtract(dAbzubuchendeMengeEinzel);
							}
						} else if (Helper.short2boolean(oArtikelDto.getBSeriennrtragend())) {

							BigDecimal nMengeAbsolut = new BigDecimal(1);

							// eine Lagerbuchung fuer SNR Artikel stornieren
							if (oDtoI.getNMenge().intValue() == 0) {
								nMengeAbsolut = Helper.getBigDecimalNull();
							}

							for (int i = 0; i < sSerienchargennummer.size(); i++) {
								getLagerFac().bucheUm(oDtoI.getArtikelIId(), iQuelleLager, oDtoI.getArtikelIId(),
										iZielLager, nMengeAbsolut,
										SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
												sSerienchargennummer.get(i).getCSeriennrChargennr(), nMengeAbsolut),
										"Umbuchung", null, theClientDto);
							}
						} else {
							// absolute Lagerbuchung, funktioniert fuer nicht
							// lagerbewirtschaftete Artikel;
							// es gilt: Lagerbewertete Artikel buchen implizit
							// auf KEIN LAGER
							getLagerFac().bucheUm(oDtoI.getArtikelIId(), iQuelleLager, oDtoI.getArtikelIId(),
									iZielLager, oDtoI.getNMenge().negate(), null, "Umbuchung", null, theClientDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_STORNIEREN, new Exception(t));
		}
	}

	/**
	 * Die Reservierung fuer die (Teil) Lieferung einer Auftragposition
	 * zuruecknehmen. <br>
	 * Es gilt: Eine Auftragposition kann ueberliefert worden sein.
	 * 
	 * @param lieferscheinpositionDtoI die Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void rollbackReservierungZuAuftragposition(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		try {
			// die Lieferscheinposition bezieht sich auf die folgende
			// Auftragposition
			AuftragpositionDto oAuftragpositionDto = getAuftragpositionFac()
					.auftragpositionFindByPrimaryKey(lieferscheinpositionDtoI.getAuftragpositionIId());

			// fuer die Auftragposition kann eine Artikelreservierung existieren
			ArtikelreservierungDto oReservierungDto = getReservierungFac().getArtikelreservierung(
					LocaleFac.BELEGART_AUFTRAG, lieferscheinpositionDtoI.getAuftragpositionIId());

			// Artikelreservierungen werden absolut gebucht. Es gilt: Reserviert
			// wird
			// maximal die Menge, die in der Auftragposition vorgesehen ist.
			BigDecimal nAbsoluteMengeFuerArtikelreservierung = null;

			if (oReservierungDto != null) {
				// die nicht gelieferte Menge muss zur reservierten Menge
				// addiert werden
				nAbsoluteMengeFuerArtikelreservierung = oReservierungDto.getNMenge()
						.add(lieferscheinpositionDtoI.getNMenge());

				// eine eventuelle Ueberlieferung beruecksichtigen
				if (nAbsoluteMengeFuerArtikelreservierung.doubleValue() > oAuftragpositionDto.getNMenge()
						.doubleValue()) {
					nAbsoluteMengeFuerArtikelreservierung = oAuftragpositionDto.getNMenge();
				}

				oReservierungDto.setNMenge(nAbsoluteMengeFuerArtikelreservierung); // absolut
				// buchen

				getReservierungFac().updateArtikelreservierung(oReservierungDto);
			} else {
				// eine neue Reservierung machen
				oReservierungDto = new ArtikelreservierungDto();
				oReservierungDto.setArtikelIId(lieferscheinpositionDtoI.getArtikelIId());
				oReservierungDto.setCBelegartnr(LocaleFac.BELEGART_AUFTRAG);
				oReservierungDto.setIBelegartpositionid(lieferscheinpositionDtoI.getAuftragpositionIId());
				oReservierungDto.setTLiefertermin(oAuftragpositionDto.getTUebersteuerbarerLiefertermin());

				// die nicht gelieferte Menge ist die zu reservierende Menge
				nAbsoluteMengeFuerArtikelreservierung = lieferscheinpositionDtoI.getNMenge();

				// eine eventuelle Ueberlieferung beruecksichtigen
				if (nAbsoluteMengeFuerArtikelreservierung.doubleValue() > oAuftragpositionDto.getNMenge()
						.doubleValue()) {
					nAbsoluteMengeFuerArtikelreservierung = oAuftragpositionDto.getNMenge();
				}

				oReservierungDto.setNMenge(nAbsoluteMengeFuerArtikelreservierung);

				getReservierungFac().createArtikelreservierung(oReservierungDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void checkLieferscheinpositionDto(LieferscheinpositionDto lieferscheinpositionDto) throws EJBExceptionLP {
		if (lieferscheinpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("lieferscheinpositionDto == null"));
		}

		myLogger.info("LieferscheinpositionDto: " + lieferscheinpositionDto.toString());
	}

	private void checkLieferscheinpositionIId(Integer iIdlieferscheinpositionI) throws EJBExceptionLP {
		if (iIdlieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdlieferscheinpositionI == null"));
		}

		myLogger.info("LieferscheinpositionIId: " + iIdlieferscheinpositionI.toString());
	}

	/**
	 * Anzahl aller Positionen eines Lieferscheins bestimmen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     String
	 * @return int Anzahl aller Positionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public int berechneAnzahlMengenbehaftetePositionen(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		int iAnzahlO = 0;
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();
			if (oPosition.getNMenge() != null) {
				iAnzahlO++;
			}
		}
		return iAnzahlO;
	}

	public void berechnePauschalposition(BigDecimal neuWert, Integer positionIId, Integer belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BigDecimal altWert = getGesamtpreisPosition(positionIId, theClientDto);
		LieferscheinDto lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(belegIId, theClientDto);
		LieferscheinpositionDto[] lieferscheinpositionDtos = lieferscheinpositionFindByPositionIId(positionIId);
		for (int i = 0; i < lieferscheinpositionDtos.length; i++) {
			LieferscheinpositionDto lieferscheinpositionDto = (LieferscheinpositionDto) getBelegVerkaufFac()
					.berechnePauschalposition(lieferscheinpositionDtos[i], lieferscheinDto, neuWert, altWert);

			Lieferscheinposition position = em.find(Lieferscheinposition.class, lieferscheinpositionDto.getIId());
			position.setNNettogesamtpreis(lieferscheinpositionDto.getNNettoeinzelpreis());
			position.setNNettogesamtpreisplusversteckteraufschlag(
					lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlag());
			position.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
					lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
			position.setBNettopreisuebersteuert(Helper.boolean2Short(true));
			try {

				Query queryLB = em
						.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
				queryLB.setParameter(1, LocaleFac.BELEGART_LIEFERSCHEIN);
				queryLB.setParameter(2, position.getIId());
				queryLB.setParameter(3, null);
				Lagerbewegung lagerbewegung = (Lagerbewegung) queryLB.getSingleResult();
				lagerbewegung.setNVerkaufspreis(lieferscheinpositionDto.getNNettoeinzelpreis());
			} catch (NoResultException ex) {

			}
		}
	}

	/**
	 * Die Anzahl der mengenbehafteten Positionen eines Lieferscheins bestimmen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @return int die Anzahl der mengenbehafteten Positionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public int berechneAnzahlArtikelpositionen(Integer iIdLieferscheinI) throws EJBExceptionLP {
		final String METHOD_NAME = "berechneAnzahlArtikelpositionen";
		myLogger.entry();

		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		int iAnzahlO = 0;
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getLieferscheinpositionartCNr().equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)
					|| oPosition.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
				iAnzahlO++;
			}
		}
		return iAnzahlO;
	}

	private void setLieferscheinpositionFromLieferscheinpositionDto(Lieferscheinposition lieferscheinposition,
			LieferscheinpositionDto lieferscheinpositionDto) {
		if (lieferscheinpositionDto.getLieferscheinIId() != null) {
			lieferscheinposition.setLieferscheinIId(lieferscheinpositionDto.getLieferscheinIId());
		}
		if (lieferscheinpositionDto.getISort() != null) {
			lieferscheinposition.setISort(lieferscheinpositionDto.getISort());
		}
		if (lieferscheinpositionDto.getLieferscheinpositionartCNr() != null) {
			lieferscheinposition.setLieferscheinpositionartCNr(lieferscheinpositionDto.getLieferscheinpositionartCNr());
		}
		if (lieferscheinpositionDto.getArtikelIId() != null) {
			lieferscheinposition.setArtikelIId(lieferscheinpositionDto.getArtikelIId());
		}

		// die Bezeichnung kann null sein
		lieferscheinposition.setCBez(lieferscheinpositionDto.getCBez());
		lieferscheinposition.setCZbez(lieferscheinpositionDto.getCZusatzbez());

		if (lieferscheinpositionDto.getBArtikelbezeichnunguebersteuert() != null) {
			lieferscheinposition
					.setBArtikelbezeichnunguebersteuert(lieferscheinpositionDto.getBArtikelbezeichnunguebersteuert());
		}

		lieferscheinposition.setXTextinhalt(lieferscheinpositionDto.getXTextinhalt());
		lieferscheinposition.setMediastandardIId(lieferscheinpositionDto.getMediastandardIId());

		if (lieferscheinpositionDto.getAuftragpositionIId() != null) {
			lieferscheinposition.setAuftragpositionIId(lieferscheinpositionDto.getAuftragpositionIId());
		}
		if (lieferscheinpositionDto.getForecastpositionIId() != null) {
			lieferscheinposition.setForecastpositionIId(lieferscheinpositionDto.getForecastpositionIId());
		}

		if (lieferscheinpositionDto.getNMenge() != null) {
			lieferscheinposition.setNMenge(lieferscheinpositionDto.getNMenge());
		}
		if (lieferscheinpositionDto.getEinheitCNr() != null) {
			lieferscheinposition.setEinheitCNr(lieferscheinpositionDto.getEinheitCNr());
		}
		lieferscheinposition.setFRabattsatz(lieferscheinpositionDto.getFRabattsatz());
		if (lieferscheinpositionDto.getBRabattsatzuebersteuert() != null) {
			lieferscheinposition.setBRabattsatzuebersteuert(lieferscheinpositionDto.getBRabattsatzuebersteuert());
		}
		lieferscheinposition.setFZusatzrabattsatz(lieferscheinpositionDto.getFZusatzrabattsatz());
		lieferscheinposition.setFKupferzuschlag(lieferscheinpositionDto.getFKupferzuschlag());
		if (lieferscheinpositionDto.getMwstsatzIId() != null) {
			lieferscheinposition.setMwstsatzIId(lieferscheinpositionDto.getMwstsatzIId());
		}
		if (lieferscheinpositionDto.getBMwstsatzuebersteuert() != null) {
			lieferscheinposition.setBMwstsatzuebersteuert(lieferscheinpositionDto.getBMwstsatzuebersteuert());
		}
		lieferscheinposition.setBNettopreisuebersteuert(lieferscheinpositionDto.getBNettopreisuebersteuert());
		lieferscheinposition.setNNettoeinzelpreis(lieferscheinpositionDto.getNEinzelpreis());
		lieferscheinposition.setNNettoeinzelpreisplusversteckteraufschlag(
				lieferscheinpositionDto.getNEinzelpreisplusversteckteraufschlag());
		lieferscheinposition.setNRabattbetrag(lieferscheinpositionDto.getNRabattbetrag());
		lieferscheinposition.setNNettogesamtpreis(lieferscheinpositionDto.getNNettoeinzelpreis());
		lieferscheinposition.setNNettogesamtpreisplusversteckteraufschlag(
				lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlag());
		lieferscheinposition.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
				lieferscheinpositionDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
		lieferscheinposition.setNMwstbetrag(lieferscheinpositionDto.getNMwstbetrag());
		lieferscheinposition.setNBruttogesamtpreis(lieferscheinpositionDto.getNBruttoeinzelpreis());
		lieferscheinposition.setPositionIId(lieferscheinpositionDto.getPositioniId());
		lieferscheinposition.setPositionIIdArtikelset(lieferscheinpositionDto.getPositioniIdArtikelset());
		lieferscheinposition.setTypCNr(lieferscheinpositionDto.getTypCNr());
		lieferscheinposition.setVerleihIId(lieferscheinpositionDto.getVerleihIId());
		lieferscheinposition.setKostentraegerIId(lieferscheinpositionDto.getKostentraegerIId());
		lieferscheinposition.setBKeinlieferrest(lieferscheinpositionDto.getBKeinlieferrest());
		lieferscheinposition.setZwsVonPosition(lieferscheinpositionDto.getZwsVonPosition());
		lieferscheinposition.setZwsBisPosition(lieferscheinpositionDto.getZwsBisPosition());
		lieferscheinposition.setZwsNettoSumme(lieferscheinpositionDto.getZwsNettoSumme());
		if (lieferscheinpositionDto.getBZwsPositionspreisZeigen() != null) {
			lieferscheinposition.setBZwsPositionspreisZeigen(lieferscheinpositionDto.getBZwsPositionspreisZeigen());
		} else {
			lieferscheinposition.setBZwsPositionspreisZeigen(Helper.boolean2Short(true));
		}
		lieferscheinposition.setCLvposition(lieferscheinpositionDto.getCLvposition());
		lieferscheinposition.setNMaterialzuschlag(lieferscheinpositionDto.getNMaterialzuschlag());
		lieferscheinposition.setLagerIId(lieferscheinpositionDto.getLagerIId());

		lieferscheinposition.setNMaterialzuschlagKurs(lieferscheinpositionDto.getNMaterialzuschlagKurs());
		lieferscheinposition.setTMaterialzuschlagDatum(lieferscheinpositionDto.getTMaterialzuschlagDatum());
		lieferscheinposition.setWareneingangspositionIIdAndererMandant(
				lieferscheinpositionDto.getWareneingangspositionIIdAndererMandant());
		lieferscheinposition.setPositionIIdZugehoerig(lieferscheinpositionDto.getPositionIIdZugehoerig());

		lieferscheinposition.setNDimBreite(lieferscheinpositionDto.getNDimBreite());
		lieferscheinposition.setNDimHoehe(lieferscheinpositionDto.getNDimHoehe());
		lieferscheinposition.setNDimTiefe(lieferscheinpositionDto.getNDimTiefe());
		lieferscheinposition.setNDimMenge(lieferscheinpositionDto.getNDimMenge());

		em.merge(lieferscheinposition);
		em.flush();
	}

	private LieferscheinpositionDto assembleLieferscheinpositionDto(Lieferscheinposition lieferscheinposition,
			boolean ladeSerienChargennummern) {
		LieferscheinpositionDto lieferscheinpositionDto = LieferscheinpositionDtoAssembler
				.createDto(lieferscheinposition);
		if (ladeSerienChargennummern) {
			lieferscheinpositionDto.setSeriennrChargennrMitMenge(
					getLagerFac().getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
							LocaleFac.BELEGART_LIEFERSCHEIN, lieferscheinpositionDto.getIId()));
		}

		return lieferscheinpositionDto;
	}

	private LieferscheinpositionDto assembleLieferscheinpositionDto(Lieferscheinposition lieferscheinposition) {
		return assembleLieferscheinpositionDto(lieferscheinposition, true);
		// LieferscheinpositionDto lieferscheinpositionDto =
		// LieferscheinpositionDtoAssembler
		// .createDto(lieferscheinposition);
		// lieferscheinpositionDto.setSeriennrChargennrMitMenge(getLagerFac()
		// .getAllSeriennrchargennrEinerBelegartpositionUeberHibernate(
		// LocaleFac.BELEGART_LIEFERSCHEIN,
		// lieferscheinpositionDto.getIId()));
		//
		// return lieferscheinpositionDto;
	}

	private LieferscheinpositionDto[] assembleLieferscheinpositionDtos(Collection<?> lieferscheinpositions,
			boolean ladeSerienChargennummern) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition, ladeSerienChargennummern));
			}
		}
		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[list.size()];
		return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}

	private LieferscheinpositionDto[] assembleLieferscheinpositionDtos(Collection<?> lieferscheinpositions) {
		return assembleLieferscheinpositionDtos(lieferscheinpositions, true);
		//
		// List<LieferscheinpositionDto> list = new
		// ArrayList<LieferscheinpositionDto>();
		// if (lieferscheinpositions != null) {
		// Iterator<?> iterator = lieferscheinpositions.iterator();
		// while (iterator.hasNext()) {
		// Lieferscheinposition lieferscheinposition = (Lieferscheinposition)
		// iterator
		// .next();
		// list.add(assembleLieferscheinpositionDto(lieferscheinposition));
		// }
		// }
		// LieferscheinpositionDto[] returnArray = new
		// LieferscheinpositionDto[list
		// .size()];
		// return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}

	private Collection<LieferscheinpositionDto> assembleLieferscheinpositionDtos(Collection<?> lieferscheinpositions,
			boolean ladeSerienChargennummern, TheClientDto theClientDto) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				Lieferscheinposition lieferscheinposition = (Lieferscheinposition) iterator.next();
				list.add(assembleLieferscheinpositionDto(lieferscheinposition, ladeSerienChargennummern));
			}
		}
		return list;

	}

	private Collection<LieferscheinpositionDto> assembleLieferscheinpositionDtos(Collection<?> lieferscheinpositions,
			TheClientDto theClientDto) {
		return assembleLieferscheinpositionDtos(lieferscheinpositions, true, theClientDto);
		//
		// List<LieferscheinpositionDto> list = new
		// ArrayList<LieferscheinpositionDto>();
		// if (lieferscheinpositions != null) {
		// Iterator<?> iterator = lieferscheinpositions.iterator();
		// while (iterator.hasNext()) {
		// Lieferscheinposition lieferscheinposition = (Lieferscheinposition)
		// iterator
		// .next();
		// list.add(assembleLieferscheinpositionDto(lieferscheinposition));
		// }
		// }
		// return list;
	}

	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinIId(Integer iIdLieferscheinI)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos);
	}

	@Override
	public Collection<LieferscheinpositionDto> lieferscheinpositionFindByLieferschein(Integer lieferscheinId,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, lieferscheinId);
		Collection<?> dtos = query.getResultList();
		return assembleLieferscheinpositionDtos(dtos, true, theClientDto);
	}

	public LieferscheinpositionDto[] lieferscheinpositionFindByPositionIId(Integer iIdPositionI) throws EJBExceptionLP {
		Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIId");
		query.setParameter(1, iIdPositionI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos);
	}

	public HashMap lieferscheinpositionFindByLieferscheinIIdAuftragIId(Integer lieferscheinIId, Integer iIdAuftragI,
			TheClientDto theClientDto) {
		Session session = null;
		HashMap alIds = new HashMap();

		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			// Suchen aller LS-Positionen, die sich auf diesen Auftrag beziehen.
			Criteria c = session.createCriteria(FLRLieferscheinposition.class);
			c.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRLIEFERSCHEIN)
					.add(Restrictions.eq("i_id", lieferscheinIId));
			c.createCriteria(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_FLRPOSITIONENSICHTAUFTRAG)
					.add(Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAG_I_ID, iIdAuftragI));
			// Query ausfuehren
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRLieferscheinposition item = (FLRLieferscheinposition) iter.next();

				alIds.put(item.getI_id(), item.getI_id());

			}

			return alIds;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Collection<LieferscheinpositionDto> lieferscheinpositionFindByLieferscheinIId(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		return assembleLieferscheinpositionDtos(lieferscheinpositionDtos, theClientDto);
	}

	/**
	 * Alle mengenbehafteten Positionen zu einem bestimmten Lieferschein holen.
	 * 
	 * @param iIdLieferscheinI PK des Lieferscheins
	 * @param theClientDto     String der aktuelle Benutzer
	 * @return LieferscheinpositionDto[] alle mengenbehafteten Positionen zu einem
	 *         bestimmten Lieferschein
	 * @throws EJBExceptionLP Ausnahme
	 */
	public LieferscheinpositionDto[] lieferscheinpositionFindByLieferscheinMenge(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "lieferscheinpositionFindByLieferscheinMenge";
		myLogger.entry();
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinI == null"));
		}
		LieferscheinpositionDto[] aLieferscheinpositionDtos = null;
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinMenge");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		aLieferscheinpositionDtos = assembleLieferscheinpositionDtos(cl);
		return aLieferscheinpositionDtos;
	}

	public LieferscheinpositionDto[] getLieferscheinPositionenByLieferschein(Integer iIdLieferscheinI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		ArrayList<LieferscheinpositionDto> dtos = new ArrayList<LieferscheinpositionDto>();
		LieferscheinpositionDto[] aLieferscheinpositionDto = null;
		Session sesion = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			sesion = factory.openSession();
			Criteria crit = sesion.createCriteria(FLRLieferscheinposition.class);
			crit.add(Restrictions.eq("flrlieferschein.i_id", iIdLieferscheinI));
			crit.add(Restrictions.isNull("position_i_id"));

			crit.addOrder(Order.asc("i_sort"));
			List<?> l = crit.list();
			Iterator<?> iter = l.iterator();
			while (iter.hasNext()) {
				FLRLieferscheinposition pos = (FLRLieferscheinposition) iter.next();
				LieferscheinpositionDto apositionDto = getLieferscheinpositionFac()
						.lieferscheinpositionFindByPrimaryKey(pos.getI_id(), theClientDto);
				if (pos.getPositionsart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
					if (!pos.getC_zbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
						dtos.add(apositionDto);
					}
				} else {
					dtos.add(apositionDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (sesion != null) {
				sesion.close();
			}
		}
		aLieferscheinpositionDto = new LieferscheinpositionDto[dtos.size()];
		for (int i = 0; i < dtos.size(); i++) {
			aLieferscheinpositionDto[i] = dtos.get(i);
		}
		return aLieferscheinpositionDto;
	}

	/**
	 * Der Kupferzuschlag einer Position kann von aussen gesetzt werden (z.B. durch
	 * die Rechnung). <br>
	 * Das Setzen des Kupferzuschlags zieht die folgenden Schritte nach sich: <br>
	 * - Die Werte der Lieferscheinposition werden neu berechnet und in der DB
	 * gespeichert <br>
	 * - Der Gesamtwert des Lieferscheins wird neu berechnet und in der DB
	 * gespeichert
	 * 
	 * @param iIdLieferscheinpositionI PK der Lieferscheinposition
	 * @param ddKupferzuschlagI        der Kupferzuschlag in Prozent, z.B. 30
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void setzeKupferzuschlag(Integer iIdLieferscheinpositionI, Double ddKupferzuschlagI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "setzeKupferzuschlag";
		myLogger.entry();

		if (iIdLieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinpositionI == null"));
		}

		// der Kupferzuschlag kann auf 0 gesetzt werden, ein Wechsel auf NULL
		// ist nicht moeglich
		if (ddKupferzuschlagI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("ddKupferzuschlagI == null"));
		}
		Lieferscheinposition oPosition = null;
		oPosition = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);
		if (oPosition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		try {
			// Schritt 1: den Kupfzuschlag setzen
			oPosition.setFKupferzuschlag(ddKupferzuschlagI);

			// Schritt 2: die Werte der Lieferscheinposition neu berechnen

			// Nettoeinzelpreis der Position bleibt unveraendert
			// NettoeinzelpreisPlusVersteckterAufschlag der Position bleibt
			// unveraendert und dient als Berechnungsgrundlage
			// Nettogesamtpreis, Rabattbetrag, Mwstbetrag und Bruttogesamtpreis
			// werden im PanelPositionen angezeigt und bleiben UNVERAENDERT
			// NettogesamtpreisPlusVersteckterAufschlag wird NEU berechnet
			// NettogesamtpreisPlusVersteckterAufschlagMinusRabatt wird NEU
			// berechnet

			BigDecimal bdBetragTemp = oPosition.getNNettoeinzelpreisplusversteckteraufschlag();

			// + Kupferzuschlag
			BigDecimal bdKupferzuschlagBetrag = oPosition.getNNettoeinzelpreisplusversteckteraufschlag()
					.multiply(new BigDecimal(oPosition.getFKupferzuschlag().doubleValue()).movePointLeft(2));
			bdKupferzuschlagBetrag = Helper.rundeKaufmaennisch(bdKupferzuschlagBetrag, 4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.add(bdKupferzuschlagBetrag);

			// - Rabattsatz
			BigDecimal bdRabattsatzBetrag = bdBetragTemp
					.multiply(new BigDecimal(oPosition.getFRabattsatz().doubleValue()).movePointLeft(2));
			bdRabattsatzBetrag = Helper.rundeKaufmaennisch(bdRabattsatzBetrag, 4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.subtract(bdRabattsatzBetrag);

			oPosition.setNNettogesamtpreisplusversteckteraufschlag(bdBetragTemp); // NEU

			LieferscheinDto oLieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oPosition.getLieferscheinIId(), theClientDto);

			// - Allgemeiner Rabatt aus den Konditionen
			BigDecimal bdAllgemeinerRabattBetrag = bdBetragTemp.multiply(
					new BigDecimal(oLieferscheinDto.getFAllgemeinerRabattsatz().doubleValue()).movePointLeft(2));
			bdAllgemeinerRabattBetrag = Helper.rundeKaufmaennisch(bdAllgemeinerRabattBetrag, 4);

			// -----------------------------
			bdBetragTemp = bdBetragTemp.subtract(bdAllgemeinerRabattBetrag);

			oPosition.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(bdBetragTemp); // NEU

			// Schritt 3: Den Wert des Lieferscheins neu berechnen und setzen
			BigDecimal bdGesamtwertInLieferscheinwaehrung = getLieferscheinFac()
					.berechneGesamtwertLieferscheinAusPositionen(oPosition.getLieferscheinIId(), theClientDto);

			bdGesamtwertInLieferscheinwaehrung = Helper.rundeKaufmaennisch(bdGesamtwertInLieferscheinwaehrung, 4);
			checkNumberFormat(bdGesamtwertInLieferscheinwaehrung);

			oLieferscheinDto.setNGesamtwertInLieferscheinwaehrung(bdGesamtwertInLieferscheinwaehrung);

			getLieferscheinFac().updateLieferschein(oLieferscheinDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	// Methoden zur Bestimmung der Sortierung der Lieferscheinpositionen
	// ---------

	/**
	 * Das maximale iSort bei den Lieferscheinpositionen fuer einen bestimmten
	 * Mandanten bestimmen.
	 * 
	 * @param iIdLieferscheinI der aktuelle Lieferschein
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	private Integer getMaxISort(Integer iIdLieferscheinI) throws EJBExceptionLP {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("LieferscheinpositionejbSelectMaxISort");
			query.setParameter(1, iIdLieferscheinI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, new Exception(t));
		}
		return iiMaxISortO;
	}

	public boolean gehoertZuArtikelset(Integer lieferscheinpositionIId) {

		Lieferscheinposition oPosition1 = em.find(Lieferscheinposition.class, lieferscheinpositionIId);

		if (oPosition1.getPositionIIdArtikelset() != null) {
			return true;
		}

		Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
		query.setParameter(1, lieferscheinpositionIId);
		Collection<?> lieferscheinpositionDtos = query.getResultList();
		LieferscheinpositionDto[] zugehoerigeLSPosDtos = assembleLieferscheinpositionDtos(lieferscheinpositionDtos);

		if (zugehoerigeLSPosDtos != null && zugehoerigeLSPosDtos.length > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Zwei bestehende Lieferscheinpositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I PK der ersten Position
	 * @param iIdPosition2I PK der zweiten Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void vertauschePositionen(Integer iIdPosition1I, Integer iIdPosition2I) throws EJBExceptionLP {
		Lieferscheinposition oPosition1 = em.find(Lieferscheinposition.class, iIdPosition1I);

		Lieferscheinposition oPosition2 = em.find(Lieferscheinposition.class, iIdPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() == null) {

			// das zweite iSort auf ungueltig setzen, damit UK
			// constraint nicht verletzt wird
			oPosition2.setISort(new Integer(-1));

			oPosition1.setISort(iSort2);
			oPosition2.setISort(iSort1);
		} else if (oPosition1.getTypCNr() == null && oPosition2.getTypCNr() != null) {

			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null && oPosition2.getCZbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
					Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinISort");
					query.setParameter(1, oPosition2.getLieferscheinIId());
					query.setParameter(2, oPosition2.getISort() - 1);
					Lieferscheinposition oPos = (Lieferscheinposition) query.getSingleResult();
					oPosition1.setTypCNr(oPos.getTypCNr());
					oPosition1.setPositionIId(oPos.getPositionIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			} else if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_ALLES)
					|| oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_VERDICHTET)
					|| oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_OHNEPREISE)
					|| oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_MITPREISE)) {
				if (oPosition2.getCZbez() != null && oPosition2.getCZbez().equals(LocaleFac.POSITIONBEZ_BEGINN)) {
					oPosition1.setTypCNr(LocaleFac.POSITIONTYP_EBENE1);
					oPosition1.setPositionIId(oPosition2.getIId());
					// das zweite iSort auf ungueltig setzen, damit UK
					// constraint nicht verletzt wird
					oPosition2.setISort(new Integer(-1));

					oPosition1.setISort(iSort2);
					oPosition2.setISort(iSort1);
				}
			}
		} else if (oPosition1.getTypCNr() != null && oPosition2.getTypCNr() == null) {

		} else if (oPosition1.getTypCNr() != null && oPosition2.getTypCNr() != null) {
			if (oPosition2.getTypCNr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
				if (oPosition2.getCZbez() != null && oPosition2.getCZbez().equals(LocaleFac.POSITIONBEZ_ENDE)) {
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

	public void vertauscheLieferscheinpositionenMinus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Lieferscheinposition> comp = new CompositeISort<Lieferscheinposition>(
				new LieferscheinpositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);
		lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto);
		getLieferscheinFac().updateTAendern(
				lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto).getLieferscheinIId(), theClientDto);
	}

	public void vertauscheLieferscheinpositionenPlus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		CompositeISort<Lieferscheinposition> comp = new CompositeISort<Lieferscheinposition>(
				new LieferscheinpositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);
		lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto);
		getLieferscheinFac().updateTAendern(
				lieferscheinpositionFindByPrimaryKey(iIdBasePosition, theClientDto).getLieferscheinIId(), theClientDto);
	}

	public void sortiereNachAuftragsnummer(Integer lieferscheinIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, lieferscheinIId);
		LieferscheinpositionDto[] dtos = assembleLieferscheinpositionDtos(query.getResultList());

		try {
			for (int i = dtos.length - 1; i > 0; --i) {
				for (int j = 0; j < i; ++j) {
					LieferscheinpositionDto o = dtos[j];
					LieferscheinpositionDto o1 = dtos[j + 1];

					String abNR = "";

					if (o.getAuftragpositionIId() != null) {

						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(o.getAuftragpositionIId());

						AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());

						abNR = Helper.fitString2Length(aDto.getCNr(), 40, ' ')
								+ Helper.fitString2LengthAlignRight(aufposDto.getISort() + "", 5, '0');

					} else {
						abNR = Helper.fitString2Length("9", 40, '9');
					}

					String abNr1 = "";

					if (o1.getAuftragpositionIId() != null) {
						AuftragpositionDto aufposDto = getAuftragpositionFac()
								.auftragpositionFindByPrimaryKey(o1.getAuftragpositionIId());
						AuftragDto aDto = getAuftragFac().auftragFindByPrimaryKey(aufposDto.getBelegIId());
						abNr1 = Helper.fitString2Length(aDto.getCNr(), 40, ' ')
								+ Helper.fitString2LengthAlignRight(aufposDto.getISort() + "", 5, '0');

					} else {
						abNr1 = Helper.fitString2Length("9", 40, '9');
					}

					if (abNR.compareTo(abNr1) > 0) {
						dtos[j] = o1;
						dtos[j + 1] = o;
					}
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		int iSort = 1;
		for (int i = 0; i < dtos.length; i++) {
			Lieferscheinposition lspos = em.find(Lieferscheinposition.class, dtos[i].getIId());

			lspos.setISort(iSort);

			em.merge(lspos);
			em.flush();

			iSort++;
		}

	}

	public void sortiereNachArtikelnummer(Integer lieferscheinIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, lieferscheinIId);
		LieferscheinpositionDto[] dtos = assembleLieferscheinpositionDtos(query.getResultList());

		for (int i = 0; i < dtos.length; i++) {
			if (dtos[i].isIntelligenteZwischensumme()) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UMSORTIEREN_INTELLIGENTE_ZWS,
						new Exception("FEHLER_UMSORTIEREN_INTELLIGENTE_ZWS"));
			}
		}

		for (int i = dtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				LieferscheinpositionDto o = dtos[j];

				LieferscheinpositionDto o1 = dtos[j + 1];

				String artikelNR = "";

				if (o.getArtikelIId() != null) {
					artikelNR = getArtikelFac().artikelFindByPrimaryKeySmall(o.getArtikelIId(), theClientDto).getCNr();
				}
				String artikelNR1 = "";

				if (o1.getArtikelIId() != null) {
					artikelNR1 = getArtikelFac().artikelFindByPrimaryKeySmall(o1.getArtikelIId(), theClientDto)
							.getCNr();

				}

				if (artikelNR.compareTo(artikelNR1) > 0) {
					dtos[j] = o1;
					dtos[j + 1] = o;
				}
			}
		}

		int iSort = 1;
		for (int i = 0; i < dtos.length; i++) {
			Lieferscheinposition lspos = em.find(Lieferscheinposition.class, dtos[i].getIId());

			lspos.setISort(iSort);

			em.merge(lspos);
			em.flush();

			iSort++;
		}

	}

	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param iIdLieferscheinI         der aktuelle Lieferschein
	 * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdLieferscheinI,
			int iSortierungNeuePositionI) throws EJBExceptionLP {
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
	}

	private LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(
			Integer iIdLieferscheinI, String positionsartCNrI) throws NoResultException, NonUniqueResultException {
		LieferscheinpositionDto angebotpositionDto = null;
		Query query = em
				.createNamedQuery("LieferscheinpositionpositionfindByLieferscheinIIdLieferscheinpositionartCNr");
		query.setParameter(1, iIdLieferscheinI);
		query.setParameter(2, positionsartCNrI);
		Lieferscheinposition lieferscheinposition = (Lieferscheinposition) query.getSingleResult();
		angebotpositionDto = assembleLieferscheinpositionDto(lieferscheinposition);
		return angebotpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
			Integer iIdAngebotI, String positionsartCNrI) {
		LieferscheinpositionDto lieferscheinpositionDto = null;
		try {
			lieferscheinpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(iIdAngebotI,
					positionsartCNrI);
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex) {
		}
		return lieferscheinpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNr(Integer iIdLieferscheinI,
			String positionsartCNrI) throws EJBExceptionLP {
		LieferscheinpositionDto angebotpositionDto = null;
		try {
			angebotpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrImpl(iIdLieferscheinI,
					positionsartCNrI);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler. Es gibt mehere Angebotspositionen mit positionsart " + positionsartCNrI
							+ " fuer angebotIId " + iIdLieferscheinI);
		}
		return angebotpositionDto;
	}

	public void positionenAnhandAuftragsreihenfolgeAnordnen(Integer iIdLieferscheinI, TheClientDto theClientDto) {

		TreeMap<Integer, Lieferscheinposition> tmLspositionenMitAuftragsbezug = new TreeMap<Integer, Lieferscheinposition>();

		ArrayList<Lieferscheinposition> alLsPositionenOhneauftragsbezug = new ArrayList<Lieferscheinposition>();

		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> lieferscheinpositionDtos = query.getResultList();

		Iterator it = lieferscheinpositionDtos.iterator();

		while (it.hasNext()) {
			Lieferscheinposition lspos = (Lieferscheinposition) it.next();

			if (lspos.getAuftragpositionIId() != null) {

				Auftragposition ap = em.find(Auftragposition.class, lspos.getAuftragpositionIId());

				tmLspositionenMitAuftragsbezug.put(ap.getISort(), lspos);
			} else {
				alLsPositionenOhneauftragsbezug.add(lspos);
			}

		}

		// Pj18433
		// Nun neu durchsortieren, beginnend mit 1

		int isort = 1;

		Iterator<Integer> it1 = tmLspositionenMitAuftragsbezug.keySet().iterator();
		while (it1.hasNext()) {
			Lieferscheinposition lspos = tmLspositionenMitAuftragsbezug.get(it1.next());

			lspos.setISort(isort);
			em.merge(lspos);
			isort++;

		}

		// Nun die zusaetzlichen Positionen hinten dranhangen
		for (int i = 0; i < alLsPositionenOhneauftragsbezug.size(); i++) {
			Lieferscheinposition lspos = alLsPositionenOhneauftragsbezug.get(i);

			lspos.setISort(isort);
			em.merge(lspos);
			isort++;
		}

	}

	public void sortierungAnpassenInBezugAufEndsumme(Integer iIdLieferscheinI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdLieferscheinI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAngebotI == null"));
		}
		myLogger.logData(iIdLieferscheinI);
		LieferscheinpositionDto lieferscheinpositionDto = lieferscheinpositionFindByAngebotIIdAngebotpositionsartCNrOhneExc(
				iIdLieferscheinI, LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME);

		if (lieferscheinpositionDto != null) {
			LieferscheinpositionDto[] aLieferscheinDto = lieferscheinpositionFindByLieferscheinIId(iIdLieferscheinI);

			for (int i = 0; i < aLieferscheinDto.length; i++) {
				if (aLieferscheinDto[i].getPositionsartCNr()
						.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_ENDSUMME)) {
					int iIndexLetztePreisbehaftetePositionNachEndsumme = -1;

					for (int j = i + 1; j < aLieferscheinDto.length; j++) {
						if (aLieferscheinDto[j].getNEinzelpreis() != null) {
							// die Position der letzten preisbehafteten Position
							// nach der Endsumme bestimmen
							iIndexLetztePreisbehaftetePositionNachEndsumme = j;
						}
					}

					if (iIndexLetztePreisbehaftetePositionNachEndsumme != -1) {
						// die Endsumme muss nach die letzte preisbehaftete
						// Position verschoben werden
						for (int k = i; k < iIndexLetztePreisbehaftetePositionNachEndsumme; k++) {
							vertauschePositionen(aLieferscheinDto[i].getIId(), aLieferscheinDto[k + 1].getIId());
						}
					}
				}
			}
		}
	}

	/**
	 * Wenn fuer einen Lieferschein eine Position geloescht wurde, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken entstehen.
	 * <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server aufgerufen.
	 * 
	 * @param iIdLieferscheinI               PK des Lieferscheins
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @throws Throwable Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(Integer iIdLieferscheinI,
			int iSortierungGeloeschtePositionI) throws Throwable {
		Query query = em.createNamedQuery("LieferscheinpositionfindByLieferschein");
		query.setParameter(1, iIdLieferscheinI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Lieferscheinposition oPosition = (Lieferscheinposition) it.next();

			if (oPosition.getPositionIId() != null) {
				LieferscheinpositionDto posBegin = lieferscheinpositionFindByLieferscheinIIdPositionIId(
						oPosition.getLieferscheinIId(), oPosition.getPositionIId());
				if (posBegin == null) {
					oPosition.setPositionIId(null);
					oPosition.setTypCNr(null);
				}
			}
			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	/**
	 * Eine bestimmte Lieferscheinposition holen.
	 * 
	 * @param iIdLieferscheinpositionI PK der Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @return LieferscheinpositionDto die Lieferscheinposition
	 * @throws EJBExceptionLP Ausnahme
	 */
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKey(Integer iIdLieferscheinpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		LieferscheinpositionDto lieferscheinpositionDto = null;
		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		lieferscheinpositionDto = assembleLieferscheinpositionDto(lieferscheinposition);

		return lieferscheinpositionDto;
	}

	public LieferscheinpositionDto lieferscheinpositionFindByLieferscheinIIdPositionIId(Integer lieferscheinIId,
			Integer lieferscheinpositionIId) throws EJBExceptionLP {
		Lieferscheinposition lieferscheinposition = null;
		if (lieferscheinIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("lieferscheinIId == null"));
		}
		if (lieferscheinpositionIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdPosition == null"));
		}
		try {
			Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdPositionIId");
			query.setParameter(1, lieferscheinIId);
			query.setParameter(2, lieferscheinpositionIId);
			lieferscheinposition = (Lieferscheinposition) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		return assembleLieferscheinpositionDto(lieferscheinposition);
	}

	/**
	 * Eine Lieferscheinposition ueber ihren PK holen.
	 * 
	 * @param iIdLieferscheinpositionI PK der Position
	 * @param theClientDto             der aktuelle Benutzer
	 * @return LieferscheinpositionDto die Position
	 */
	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKeyOhneExc(Integer iIdLieferscheinpositionI,
			TheClientDto theClientDto) {

		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);

		return lieferscheinposition == null ? null : assembleLieferscheinpositionDto(lieferscheinposition);
	}

	public LieferscheinpositionDto lieferscheinpositionFindByPrimaryKeyOhneExcUndOhneSnrChnrList(
			Integer iIdLieferscheinpositionI) {

		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);

		return lieferscheinposition == null ? null : LieferscheinpositionDtoAssembler.createDto(lieferscheinposition);
	}

	public LieferscheinpositionDto lieferscheinpositionFindPositionIIdISort(Integer positionIId, Integer iSort)
			throws EJBExceptionLP {
		Lieferscheinposition lieferscheinposition = null;
		try {
			Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdISort");
			query.setParameter(1, positionIId);
			query.setParameter(2, iSort);
			lieferscheinposition = (Lieferscheinposition) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
		return assembleLieferscheinpositionDto(lieferscheinposition);
	}

	/**
	 * Alle Lieferscheinposition holen, die Bezug zu einer bestimmten
	 * Auftragposition haben.
	 * 
	 * @param iIdAuftragpositionI PK der Auftragposition
	 * @param theClientDto        der aktuelle Benutzer
	 * @return LieferscheinpositionDto[] die Lieferscheinpositionen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(Integer iIdAuftragpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		return lieferscheinpositionFindByAuftragpositionIId(iIdAuftragpositionI, true, theClientDto);
		// // checkAndLogData(theClientDto, iIdAuftragpositionI);
		// if (iIdAuftragpositionI == null) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
		// new Exception("iIdAuftragpositionI == null"));
		// }
		// LieferscheinpositionDto[] aPositionDtos = null;
		// Query query = em
		// .createNamedQuery("LieferscheinpositionfindByAuftragposition");
		// query.setParameter(1, iIdAuftragpositionI);
		// Collection<?> cl = query.getResultList();
		// aPositionDtos = assembleLieferscheinpositionDtos(cl);
		// return aPositionDtos;
	}

	public LieferscheinpositionDto[] lieferscheinpositionFindByAuftragpositionIId(Integer iIdAuftragpositionI,
			boolean ladeSerienChargennummern, TheClientDto theClientDto) throws EJBExceptionLP {
		// checkAndLogData(theClientDto, iIdAuftragpositionI);
		if (iIdAuftragpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragpositionI == null"));
		}
		LieferscheinpositionDto[] aPositionDtos = null;
		Query query = em.createNamedQuery("LieferscheinpositionfindByAuftragposition");
		query.setParameter(1, iIdAuftragpositionI);
		Collection<?> cl = query.getResultList();
		aPositionDtos = assembleLieferscheinpositionDtos(cl, ladeSerienChargennummern);
		return aPositionDtos;
	}

	private void preiseEinesArtikelsetsUpdaten(Integer positionIIdKopfartikel, TheClientDto theClientDto) {

		LieferscheinpositionDto lieferscheinPositionDtoKopfartikel = lieferscheinpositionFindByPrimaryKey(
				positionIIdKopfartikel, theClientDto);

		Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdArtikelset");
		query.setParameter(1, lieferscheinPositionDtoKopfartikel.getIId());
		Collection<IBelegVerkaufEntity> lieferscheinpositions = query.getResultList();

		Collection<?> lieferscheinpositionDtos = query.getResultList();
		try {
			final LieferscheinDto lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(
					lieferscheinPositionDtoKopfartikel.getLieferscheinIId(), theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(lieferscheinDto.getKundeIIdRechnungsadresse(),
					theClientDto);

			Integer mwstsatzbezIId = getMandantFac()
					.mwstsatzFindByPrimaryKey(lieferscheinPositionDtoKopfartikel.getMwstsatzIId(), theClientDto)
					.getIIMwstsatzbezId();

			// Zuerst Gesamtwert berechnen
			final BigDecimal bdMenge = lieferscheinPositionDtoKopfartikel.getNMenge();

			BigDecimal bdNettoeinzelpreis = lieferscheinPositionDtoKopfartikel.getNNettoeinzelpreis();

			BigDecimal bdGesamtwertposition = bdMenge.multiply(bdNettoeinzelpreis);

			getBelegVerkaufFac().preiseEinesArtikelsetsUpdaten(lieferscheinpositions, bdMenge, bdGesamtwertposition,
					kundeDto, new MwstsatzbezId(mwstsatzbezIId), lieferscheinDto.getWaehrungCNr(),
					new IArtikelsetPreisUpdate() {
						@Override
						public void initializePreis(IBelegVerkaufEntity positionEntity) {
							Lieferscheinposition lpos = (Lieferscheinposition) positionEntity;
							lpos.setNBruttogesamtpreis(BigDecimal.ZERO);
						}

						@Override
						public void setPreis(IBelegVerkaufEntity positionEntity, BigDecimal nettoPrice) {
							Lieferscheinposition lpos = (Lieferscheinposition) positionEntity;
							lpos.setNBruttogesamtpreis(nettoPrice);
						}

						@Override
						public void addPreis(IBelegVerkaufEntity positionEntity, BigDecimal addNettoPrice) {
							Lieferscheinposition lpos = (Lieferscheinposition) positionEntity;
							lpos.setNBruttogesamtpreis(lpos.getNBruttogesamtpreis().add(addNettoPrice));
						}
					}, theClientDto);
			getLieferscheinFac().updateTAendern(lieferscheinDto.getIId(), theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public ArrayList<LieferscheinpositionDto> preiseAusAuftragspositionenUebernehmen(Integer auftragIId,
			TheClientDto theClientDto) {

		ArrayList<LieferscheinpositionDto> rueckgabe = new ArrayList<LieferscheinpositionDto>();

		try {

			HashMap<Integer, LieferscheinDto> hmBetroffeneLieferscheine = new HashMap<Integer, LieferscheinDto>();

			AuftragpositionDto[] posDtos = getAuftragpositionFac().auftragpositionFindByAuftrag(auftragIId);

			for (int i = 0; i < posDtos.length; i++) {

				LieferscheinpositionDto[] lsPosDtos = lieferscheinpositionFindByAuftragpositionIId(posDtos[i].getIId(),
						theClientDto);

				for (int j = 0; j < lsPosDtos.length; j++) {
					LieferscheinpositionDto lsPosDto = lsPosDtos[j];

					LieferscheinDto lsDto = getLieferscheinFac()
							.lieferscheinFindByPrimaryKey(lsPosDto.getLieferscheinIId(), theClientDto);

					if (!lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_VERRECHNET)
							&& !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ERLEDIGT)
							&& !lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_STORNIERT)) {
						if (lsPosDto.getNMenge() != null) {

							getLieferscheinFac().setzeStatusLieferschein(lsDto.getIId(),
									LieferscheinFac.LSSTATUS_ANGELEGT);

							lsPosDto.setNBruttoeinzelpreis(posDtos[i].getNBruttoeinzelpreis());
							lsPosDto.setNEinzelpreis(posDtos[i].getNEinzelpreis());
							lsPosDto.setNEinzelpreisplusversteckteraufschlag(
									posDtos[i].getNEinzelpreisplusversteckteraufschlag());
							lsPosDto.setNMaterialzuschlag(posDtos[i].getNMaterialzuschlag());
							lsPosDto.setNMwstbetrag(posDtos[i].getNMwstbetrag());
							lsPosDto.setNNettoeinzelpreis(posDtos[i].getNNettoeinzelpreis());
							lsPosDto.setNNettoeinzelpreisplusversteckteraufschlag(
									posDtos[i].getNNettoeinzelpreisplusversteckteraufschlag());
							lsPosDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
									posDtos[i].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
							lsPosDto.setNRabattbetrag(posDtos[i].getNRabattbetrag());
							lsPosDto.setMwstsatzIId(posDtos[i].getMwstsatzIId());
							lsPosDto.setFRabattsatz(posDtos[i].getFRabattsatz());
							lsPosDto.setFZusatzrabattsatz(posDtos[i].getFZusatzrabattsatz());

							updateLieferscheinpositionOhneWeitereAktion(lsPosDto, theClientDto);

							if (!hmBetroffeneLieferscheine.containsKey(lsDto.getIId())) {
								hmBetroffeneLieferscheine.put(lsDto.getIId(), lsDto);
							}

						}
					} else {
						rueckgabe.add(lsPosDto);
					}

				}

			}

			Iterator it = hmBetroffeneLieferscheine.keySet().iterator();

			while (it.hasNext()) {

				LieferscheinDto lsDto = hmBetroffeneLieferscheine.get(it.next());
				if (!lsDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT)) {
					getLieferscheinFac().aktiviereLieferschein(lsDto.getIId(), theClientDto);
				}

			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return rueckgabe;
	}

	/**
	 * Eine Lieferscheinposition aktualisieren. <br>
	 * Chargenbehaftete Lieferscheinpositionen koennen nicht aktualisiert werden.
	 * 
	 * @param oLieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param theClientDto              der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	public Integer updateLieferscheinposition(LieferscheinpositionDto oLieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdLieferscheinposition = null;
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		pruefePflichtfelderBelegposition(oLieferscheinpositionDtoI, theClientDto);

		if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
			updateLieferscheinpositionHandeingabe(oLieferscheinpositionDtoI, theClientDto);
		} else if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
			// auf KEIN LAGER)
			iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(oLieferscheinpositionDtoI,
					theClientDto);

		} else {
			// die Lieferscheinposition und den Lieferscheinstatus
			// korrigieren
			updateLieferscheinstatusLieferscheinposition(oLieferscheinpositionDtoI, theClientDto);
		}
		getLieferscheinFac().updateTAendern(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		return iIdLieferscheinposition;
	}

	public Integer updateLieferscheinposition(LieferscheinpositionDto oLieferscheinpositionDtoI,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Integer iIdLieferscheinposition = null;
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		pruefePflichtfelderBelegposition(oLieferscheinpositionDtoI, theClientDto);

		if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
			updateLieferscheinpositionHandeingabe(oLieferscheinpositionDtoI, theClientDto);
		} else if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
				.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {

			// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
			// auf KEIN LAGER)
			iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(oLieferscheinpositionDtoI,
					identities, theClientDto);

			// PJ20394
			if (parameterDazugehoertMengeNeuberechnen(theClientDto.getMandant())) {
				Query query = em.createNamedQuery("LieferscheinpositionfindByPositionIIdZugehoerig");
				query.setParameter(1, oLieferscheinpositionDtoI.getIId());
				Collection<?> posZugehoerig = query.getResultList();

				if (posZugehoerig != null && posZugehoerig.size() > 0) {
					Iterator it = posZugehoerig.iterator();
					while (it.hasNext()) {
						Lieferscheinposition lp = (Lieferscheinposition) it.next();
						LieferscheinpositionDto lpDtoZugehoerig = lieferscheinpositionFindByPrimaryKey(lp.getIId(),
								theClientDto);
						lpDtoZugehoerig.setNMenge(mengeZugehoerigerArtikelNeuBerechnen(oLieferscheinpositionDtoI,
								lpDtoZugehoerig, theClientDto));
						updateLieferscheinposition(lpDtoZugehoerig, theClientDto);

					}

				}
			}

		} else {
			// die Lieferscheinposition und den Lieferscheinstatus
			// korrigieren
			updateLieferscheinstatusLieferscheinposition(oLieferscheinpositionDtoI, theClientDto);
		}
		getLieferscheinFac().updateTAendern(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		return iIdLieferscheinposition;
	}

	public void lieferscheinpositionKeinLieferrestEintragen(Integer lieferscheinpositionIId, boolean bKeinLieferrest,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, lieferscheinpositionIId);
		lieferscheinposition.setBKeinlieferrest(Helper.boolean2Short(bKeinLieferrest));
		getLieferscheinFac().updateTAendern(lieferscheinposition.getLieferscheinIId(), theClientDto);
	}

	/**
	 * Eine Lieferscheinposition vom Typ Handeingabe aktualisieren. <br>
	 * Eine Handeingabeposition bucht nicht auf Lager. <br>
	 * Vorsicht: Es kann mehrere Lieferscheine zu einem Auftrag geben, ausserdem
	 * kann eine Auftragposition teil- oder ueberliefert sein.
	 * 
	 * @param lieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void updateLieferscheinpositionHandeingabe(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);

		try {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lieferscheinpositionDtoI.getArtikelIId(),
					theClientDto);

			// den Handartikel korrigieren
			ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();

			// SP4906
			if (oArtikelsprDto == null) {
				oArtikelsprDto = new ArtikelsprDto();
			}

			oArtikelsprDto.setCBez(lieferscheinpositionDtoI.getCBez());
			oArtikelsprDto.setCZbez(lieferscheinpositionDtoI.getCZusatzbez());

			artikelDto.setArtikelsprDto(oArtikelsprDto);
			artikelDto.setEinheitCNr(lieferscheinpositionDtoI.getEinheitCNr());

			// Der Artikel erhaelt die Mwst-Satz-Bezeichnung
			MwstsatzDto mwstsatzDto = getMandantFac()
					.mwstsatzFindByPrimaryKey(lieferscheinpositionDtoI.getMwstsatzIId(), theClientDto);
			artikelDto.setMwstsatzbezIId(mwstsatzDto.getIIMwstsatzbezId());
			// Artikel speichern
			getArtikelFac().updateArtikel(artikelDto, theClientDto);

			LieferscheinpositionDto lieferscheinpositionBisherDto = lieferscheinpositionFindByPrimaryKey(
					lieferscheinpositionDtoI.getIId(), theClientDto);

			// die Lieferscheinposition und den Lieferscheinstatus korrigieren
			updateLieferscheinstatusLieferscheinposition(lieferscheinpositionDtoI, theClientDto);

			// die offene Menge im Auftrag korrigieren
			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
				// Mengenaenderung bestimmen

				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						lieferscheinpositionDtoI.getAuftragpositionIId(), theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Wiederkehrende Aktionen beim aktualisieren einer Lieferscheinposition. <br>
	 * - Status des Lieferscheins aktualisieren <br>
	 * - die Lieferscheinposition aktualisieren <br>
	 * - die zusaetzlichen Preisfelder der Lieferscheinposition neu berechnen
	 * 
	 * @param lieferscheinpositionDtoI die Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void updateLieferscheinstatusLieferscheinposition(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			getLieferscheinFac()
					.pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinpositionDtoI.getLieferscheinIId());

			Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class,
					lieferscheinpositionDtoI.getIId());
			if (lieferscheinposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setLieferscheinpositionFromLieferscheinpositionDto(lieferscheinposition, lieferscheinpositionDtoI);
			befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(), theClientDto);

			if (lieferscheinpositionDtoI.isIntelligenteZwischensumme()) {
				LieferscheinpositionDto[] alleLsPos = lieferscheinpositionFindByLieferscheinIId(
						lieferscheinpositionDtoI.getLieferscheinIId());
				Set<Integer> modifiedPositions = new HashSet<Integer>();
				LieferscheinDto lieferscheinDto = getLieferscheinFac()
						.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

				getBelegVerkaufFac().berechneBelegpositionVerkauf(lieferscheinpositionDtoI, lieferscheinDto, alleLsPos,
						modifiedPositions);
				System.out.println("Modified positions " + modifiedPositions.size());
				for (Integer modifiedPositionIndex : modifiedPositions) {
					Lieferscheinposition lspos = em.find(Lieferscheinposition.class,
							alleLsPos[modifiedPositionIndex].getIId());
					lspos.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(alleLsPos[modifiedPositionIndex]
							.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
					em.merge(lspos);
					em.flush();
				}

				for (Integer index : modifiedPositions) {
					if ((alleLsPos[index].getNMenge() != null) && (alleLsPos[index].getAuftragpositionIId() == null)
							&& alleLsPos[index].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
						int signum = alleLsPos[index].getNMenge().signum();
						if (signum > 0) {
							bucheAbLager(alleLsPos[index], theClientDto);
						} else if (signum < 0) {
							bucheZuLager(alleLsPos[index], theClientDto);
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem lagerbewirtschafteten oder
	 * nicht lagerbewirtschafteten Artikel aktualisieren. <br>
	 * Eine nicht lagerbewirtschaftete Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	private Integer updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
			LieferscheinpositionDto lieferscheinpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(lieferscheinpositionDtoI,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
		// Integer iIdLieferscheinposition = null;
		//
		// // die Lagerbuchung und die Lieferscheinposition korrigieren
		// LieferscheinpositionDto lieferscheinpositionBisherDto =
		// lieferscheinpositionFindByPrimaryKey(
		// lieferscheinpositionDtoI.getIId(), theClientDto);
		// removeLieferscheinposition(lieferscheinpositionBisherDto,
		// theClientDto);
		// sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
		// lieferscheinpositionDtoI.getLieferscheinIId(),
		// lieferscheinpositionDtoI.getISort());
		// iIdLieferscheinposition = createLieferscheinposition(
		// lieferscheinpositionDtoI, true, theClientDto);
		//
		// return iIdLieferscheinposition;
	}

	private boolean updateArtikelsetMengen(LieferscheinpositionDto positionDto, BigDecimal neueMenge,
			List<SeriennrChargennrMitMengeDto> notyetUsedIdentities, TheClientDto theClientDto) throws RemoteException {
		if (positionDto.getArtikelIId() == null)
			return false;

		StuecklisteDto stklDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(positionDto.getArtikelIId(), theClientDto);
		if (stklDto == null)
			return false;
		if (!stklDto.isSetartikel())
			return false;

		// Wir haben einen Setartikel-Kopf und es wurde die Menge geaendert ->
		// die Mengen des Sets anpassen
		List<Lieferscheinposition> setPositions = LieferscheinpositionQuery.listByPositionIIdArtikelset(em,
				positionDto.getIId());

		BigDecimal oldHeadAmount = positionDto.getNMenge();
		BigDecimal newHeadAmount = neueMenge;

		for (Lieferscheinposition lspos : setPositions) {
			LieferscheinpositionDto posDto = assembleLieferscheinpositionDto(lspos);

			if (posDto.getNMenge() != null) {
				posDto.setNMenge(posDto.getNMenge().multiply(newHeadAmount).divide(oldHeadAmount));
				// Alte Seriennummern rausschmeissen
//				posDto.setSeriennrChargennrMitMenge(null);
				getBelegVerkaufFac().setupChangePositionWithIdentities(false, posDto, notyetUsedIdentities,
						theClientDto);

				updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(posDto, notyetUsedIdentities, theClientDto);
			}
		}

		return true;
	}

	private Integer updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(
			LieferscheinpositionDto lieferscheinpositionDtoI, List<SeriennrChargennrMitMengeDto> identities,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// die Lagerbuchung und die Lieferscheinposition korrigieren
		LieferscheinpositionDto lieferscheinpositionBisherDto = lieferscheinpositionFindByPrimaryKey(
				lieferscheinpositionDtoI.getIId(), theClientDto);

		Integer iIdLieferscheinposition = lieferscheinpositionBisherDto.getIId();

		List<ZwsRemoveData> zwsRemoveDatas = findPossibleIntZwsPositions(lieferscheinpositionDtoI, theClientDto);

		boolean bLagerGleich = false;

		if (lieferscheinpositionBisherDto.getLagerIId() == null && lieferscheinpositionDtoI.getLagerIId() == null) {
			bLagerGleich = true;
		} else if (lieferscheinpositionBisherDto.getLagerIId() != null
				&& lieferscheinpositionBisherDto.getLagerIId().equals(lieferscheinpositionDtoI.getLagerIId())) {
			bLagerGleich = true;
		}

		if (lieferscheinpositionBisherDto.getArtikelIId().equals(lieferscheinpositionDtoI.getArtikelIId())
				&& ((lieferscheinpositionDtoI.getNMenge().doubleValue() > 0
						&& lieferscheinpositionBisherDto.getNMenge().doubleValue() > 0)
						|| (lieferscheinpositionDtoI.getNMenge().doubleValue() < 0
								&& lieferscheinpositionBisherDto.getNMenge().doubleValue() < 0))
				&& bLagerGleich == true) {

			Lieferscheinposition lsposZeile = em.find(Lieferscheinposition.class,
					lieferscheinpositionBisherDto.getIId());
			setLieferscheinpositionFromLieferscheinpositionDto(lsposZeile, lieferscheinpositionDtoI);
			boolean isArtikelsetKopf = updateArtikelsetMengen(lieferscheinpositionBisherDto,
					lieferscheinpositionDtoI.getNMenge(), identities, theClientDto);

			if (isArtikelsetKopf) {
				preiseEinesArtikelsetsUpdaten(lieferscheinpositionDtoI.getIId(), theClientDto);
			}

			LieferscheinDto lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
			// SP4317
			if (lieferscheinpositionDtoI.getNMaterialzuschlag() != null
					&& lieferscheinpositionBisherDto.getNMaterialzuschlag() != null
					&& lieferscheinpositionDtoI.getNMaterialzuschlag().doubleValue() != lieferscheinpositionBisherDto
							.getNMaterialzuschlag().doubleValue()) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(lieferscheinpositionDtoI.getArtikelIId(), theClientDto);
				if (artikelDto.getMaterialIId() != null) {

					MaterialzuschlagDto mDto = getMaterialFac().getKursMaterialzuschlagDtoVKInZielwaehrung(
							lieferscheinDto.getKundeIIdRechnungsadresse(), artikelDto.getIId(),
							new java.sql.Date(lieferscheinDto.getTBelegdatum().getTime()),
							lieferscheinDto.getWaehrungCNr(), theClientDto);
					if (mDto != null) {
						lieferscheinpositionDtoI.setNMaterialzuschlagKurs(mDto.getNZuschlag());
						lieferscheinpositionDtoI.setTMaterialzuschlagDatum(mDto.getTGueltigab());
					}
				}
			}

			// Es muss nur der Preis upgedatet werden
			LieferscheinpositionDto[] alleLsPos = lieferscheinpositionFindByLieferscheinIId(
					lieferscheinpositionDtoI.getLieferscheinIId());
			Set<Integer> modifiedPositions = new HashSet<Integer>();

			lieferscheinpositionDtoI=  (LieferscheinpositionDto) getBelegVerkaufFac().berechneBelegpositionVerkauf(lieferscheinpositionDtoI, lieferscheinDto, alleLsPos,
					modifiedPositions);

			lsposZeile = em.find(Lieferscheinposition.class, lieferscheinpositionBisherDto.getIId());
			setLieferscheinpositionFromLieferscheinpositionDto(lsposZeile, lieferscheinpositionDtoI);
			int signumLsPos = lieferscheinpositionDtoI.getNMenge().signum();
			if (signumLsPos > 0) {
				bucheAbLager(lieferscheinpositionDtoI, theClientDto);
			} else if (signumLsPos < 0) {
				bucheZuLager(lieferscheinpositionDtoI, theClientDto);
			}

			System.out.println("Modified positions " + modifiedPositions.size());
			for (Integer modifiedPositionIndex : modifiedPositions) {
				Lieferscheinposition lspos = em.find(Lieferscheinposition.class,
						alleLsPos[modifiedPositionIndex].getIId());
				lspos.setNNettogesamtpreisplusversteckteraufschlag(
						alleLsPos[modifiedPositionIndex].getNNettoeinzelpreisplusversteckteraufschlag());
				lspos.setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
						alleLsPos[modifiedPositionIndex].getNNettoeinzelpreisplusversteckteraufschlagminusrabatte());
				em.merge(lspos);
				em.flush();
			}

			for (Integer index : modifiedPositions) {
				if ((alleLsPos[index].getNMenge() != null) && (alleLsPos[index].getAuftragpositionIId() == null)
						&& alleLsPos[index].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
					int signum = alleLsPos[index].getNMenge().signum();
					if (signum > 0) {
						bucheAbLager(alleLsPos[index], theClientDto);
					} else if (signum < 0) {
						bucheZuLager(alleLsPos[index], theClientDto);
					}
				}
			}

			// die offene Menge im Auftrag korrigieren
			if (lieferscheinpositionDtoI.getAuftragpositionIId() != null) {
				// Mengenaenderung bestimmen

				getAuftragpositionFac().updateOffeneMengeAuftragposition(
						lieferscheinpositionDtoI.getAuftragpositionIId(), theClientDto);
			}
			if (lieferscheinpositionDtoI.getForecastpositionIId() != null) {

				getForecastFac().reservierungenMitForecastSynchronisieren(null, null,
						lieferscheinpositionDtoI.getForecastpositionIId());
			}

		} else {

			removeLieferscheinposition(lieferscheinpositionBisherDto, theClientDto);
			sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(lieferscheinpositionDtoI.getLieferscheinIId(),
					lieferscheinpositionDtoI.getISort());

			iIdLieferscheinposition = createLieferscheinposition(lieferscheinpositionDtoI, true, identities,
					theClientDto);

			for (ZwsRemoveData zwsRemoveData : zwsRemoveDatas) {
				Integer newPosId = getLSPositionIIdFromPositionNummer(zwsRemoveData.getZwsPos().getBelegIId(),
						zwsRemoveData.getPosNr());
				if (newPosId.equals(iIdLieferscheinposition)) {
					Lieferscheinposition lspos = em.find(Lieferscheinposition.class,
							zwsRemoveData.getZwsPos().getIId());
					if (zwsRemoveData.isFrom()) {
						lspos.setZwsVonPosition(newPosId);
					} else {
						lspos.setZwsBisPosition(newPosId);
					}
					em.merge(lspos);
					em.flush();
				} else {
					myLogger.error("Die errechnete PositionsId '" + newPosId + "' stimmt nicht der erzeugten '"
							+ iIdLieferscheinposition + "'\u00fcberein. LS-Beleg: '"
							+ zwsRemoveData.getZwsPos().getBelegIId() + "'.");
				}
			}
		}
		return iIdLieferscheinposition;
	}

	private class ZwsRemoveData {
		private LieferscheinpositionDto zwsPos;
		private Integer posNr;
		private boolean isFrom;

		public ZwsRemoveData(LieferscheinpositionDto zwsPos, boolean isFrom, Integer posNr) {
			this.zwsPos = zwsPos;
			this.isFrom = isFrom;
			this.posNr = posNr;
		}

		public LieferscheinpositionDto getZwsPos() {
			return zwsPos;
		}

		public Integer getPosNr() {
			return posNr;
		}

		public boolean isFrom() {
			return this.isFrom;
		}
	}

	/**
	 * Spezialbehandlung der Position, die in einem Von bzw. Bis der IntZws
	 * ist.</br>
	 * <p>
	 * Wenn die Von-Pos oder auch die Bis-Pos entfernt und wieder neu eingef&uuml;gt
	 * wird, dann stimmt nat&uuml;rlich diese IId nicht mehr mit jener &uuml;berein,
	 * die in der ZwsPos eingetragen ist
	 * </p>
	 * 
	 * @param removedDto
	 * @param theClientDto
	 */
	private List<ZwsRemoveData> findPossibleIntZwsPositions(LieferscheinpositionDto removedDto,
			TheClientDto theClientDto) {
		List<ZwsRemoveData> foundZwsPosIds = new ArrayList<ZwsRemoveData>();
		LieferscheinpositionDto[] lsPosDtos = lieferscheinpositionFindByLieferscheinIId(removedDto.getBelegIId());
		for (LieferscheinpositionDto lsPosDto : lsPosDtos) {
			if (lsPosDto.isIntelligenteZwischensumme()) {
				if (lsPosDto.getZwsVonPosition().equals(removedDto.getIId())) {
					ZwsRemoveData zwsRemoveData = new ZwsRemoveData(lsPosDto, true,
							getLSPositionNummer(lsPosDto.getZwsVonPosition()));
					foundZwsPosIds.add(zwsRemoveData);
					continue;
				}
				if (lsPosDto.getZwsBisPosition().equals(removedDto.getIId())) {
					ZwsRemoveData zwsRemoveData = new ZwsRemoveData(lsPosDto, false,
							getLSPositionNummer(lsPosDto.getZwsBisPosition()));
					foundZwsPosIds.add(zwsRemoveData);
					continue;
				}
			}
		}
		return foundZwsPosIds;
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem nicht lagerbewirtschafteten
	 * Artikel aktualisieren. <br>
	 * Eine solche Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	private void updateLieferscheinpositionIdentChargennr(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// es duerfen keine mengen- und somit lagerrelevanten Teile veraendert
		// werden
		updateLieferscheinstatusLieferscheinposition(lieferscheinpositionDtoI, theClientDto);
	}

	/**
	 * Eine Lieferscheinposition vom Typ Ident mit einem nicht lagerbewirtschafteten
	 * Artikel aktualisieren. <br>
	 * Eine solche Position bucht intern auf KEIN LAGER.
	 * 
	 * @param lieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	private Integer updateLieferscheinpositionIdentSeriennr(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iIdLieferscheinposition = null;
		iIdLieferscheinposition = updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(lieferscheinpositionDtoI,
				theClientDto);
		return iIdLieferscheinposition;
	}

	public Integer istLieferscheinpositionMitWEPosAnderermandantVerknuepft(Integer wareneingangspositionIId) {

		Query query = em.createNamedQuery("LieferscheinpositionfindByWareneingangspositionIIdAndererMandant");
		query.setParameter(1, wareneingangspositionIId);
		if (query.getResultList().size() > 0) {
			return ((Lieferscheinposition) query.getResultList().iterator().next()).getIId();
		} else {
			return null;
		}

	}

	/**
	 * Feststellen, ob eine Lieferscheinposition einen lagerbewirtschafteten Artikel
	 * enthaelt.
	 * 
	 * @param iIdLieferscheinpositionI PK der Lieferscheinposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @return boolean true, wenn die Lieferscheinposition einen
	 *         lagerbewirtschafteten Artikel enthaelt
	 * @throws EJBExceptionLP Ausnahme
	 */
	public boolean enthaeltLieferscheinpositionLagerbewirtschaftetenArtikel(Integer iIdLieferscheinpositionI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		if (iIdLieferscheinpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLieferscheinpositionI == null"));
		}

		boolean bLagerbewirtschaftet = false;

		Lieferscheinposition lieferscheinposition = null;

		lieferscheinposition = em.find(Lieferscheinposition.class, iIdLieferscheinpositionI);
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (lieferscheinposition.getArtikelIId() != null) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(lieferscheinposition.getArtikelIId(),
					theClientDto);

			if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
				bLagerbewirtschaftet = true;
			}
		}

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return bLagerbewirtschaftet;
	}

	/**
	 * Spezielle Methode zum Erfassen einer mengenbehafteten auftragbezogenen
	 * Lieferscheinposition aus der Sicht Auftrag heraus. Diese Methode wird nur
	 * dann aufgerufen, wenn zu einer Auftragposition bereits eine
	 * Lieferscheinposition erfasst wurde. Das bedeutet, dass die eingegebene Menge
	 * zur bestehenden Menge addiert werden muss. <br>
	 * Es gilt: Die eingegebene Menge muss > 0 sein.
	 * 
	 * @param lieferscheinpositionDtoI dieser Teil der Auftragposition wird als
	 *                                 Lieferscheinposition erfasst
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateLieferscheinpositionSichtAuftrag(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		try {
			// IMS 2129
			if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
				getLieferscheinFac()
						.pruefeUndSetzeLieferscheinstatusBeiAenderung(lieferscheinpositionDtoI.getLieferscheinIId());

				BigDecimal nZusaetzlicheMenge = lieferscheinpositionDtoI.getNMenge();

				if (nZusaetzlicheMenge.doubleValue() > 0) {

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Lieferscheinposition.

					Lieferscheinposition lieferscheinpositionBisher = em.find(Lieferscheinposition.class,
							lieferscheinpositionDtoI.getIId());
					if (lieferscheinpositionBisher == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
					}

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					lieferscheinpositionDtoI.setNMenge(lieferscheinpositionBisher.getNMenge().add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (lieferscheinpositionDtoI.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// die Lagerbuchung ist absolut

						bucheAbLager(lieferscheinpositionDtoI, theClientDto);
						// die Lieferscheinposition mit den neuen Werten
						// aktualisieren
						lieferscheinpositionBisher.setNMenge(lieferscheinpositionDtoI.getNMenge());
						lieferscheinpositionBisher
								.setPositionIIdArtikelset(lieferscheinpositionDtoI.getPositioniIdArtikelset());
						em.merge(lieferscheinpositionBisher);
						em.flush();

					} else {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN,
								new Exception("nMenge <= 0"));
					}
				} else if (lieferscheinpositionDtoI.getNMenge().doubleValue() > 0) {
					getLieferscheinFac().pruefeUndSetzeLieferscheinstatusBeiAenderung(
							lieferscheinpositionDtoI.getLieferscheinIId());

					nZusaetzlicheMenge = lieferscheinpositionDtoI.getNMenge();

					// die zugehoerige
					// Auftragreservierung anpassen.
					// Achtung: Parameter ist die Aenderung in der Menge der
					// Lieferscheinposition.

					Lieferscheinposition lieferscheinpositionBisher = em.find(Lieferscheinposition.class,
							lieferscheinpositionDtoI.getIId());

					// Schritt 2: Die neue Menge der Lieferscheinposition
					// bestimmen.
					lieferscheinpositionDtoI.setNMenge(lieferscheinpositionBisher.getNMenge().add(nZusaetzlicheMenge));

					// Schritt 2: Wenn es sich um eine Artikelposition handelt,
					// muss die
					// Lagerbuchung aktualisiert werden, die uebergebene Menge
					// muss die
					// Menge der neu zu erfassenden Position sein, entsprechend
					// muessen Seriennrchargennr angepasst sein
					if (lieferscheinpositionDtoI.getLieferscheinpositionartCNr()
							.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
						// die Lagerbuchung ist absolut
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(lieferscheinpositionDtoI.getArtikelIId(), theClientDto);

						bucheZuLager(lieferscheinpositionDtoI, theClientDto);
					}
					// die Lieferscheinposition mit den neuen Werten
					// aktualisieren
					lieferscheinpositionBisher.setNMenge(lieferscheinpositionDtoI.getNMenge());

				}
			}
			getAuftragpositionFac().updateOffeneMengeAuftragposition(lieferscheinpositionDtoI.getAuftragpositionIId(),
					theClientDto);
			getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		}
	}

	/**
	 * Aktualisieren einer Lieferscheinposition ohne weitere Aktion.
	 * 
	 * @param lieferscheinpositionDtoI die Auftragposition
	 * @param theClientDto             der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	public void updateLieferscheinpositionOhneWeitereAktion(LieferscheinpositionDto lieferscheinpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		checkLieferscheinpositionDto(lieferscheinpositionDtoI);
		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class,
				lieferscheinpositionDtoI.getIId());
		if (lieferscheinposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setLieferscheinpositionFromLieferscheinpositionDto(lieferscheinposition, lieferscheinpositionDtoI);
		befuelleZusaetzlichePreisfelder(lieferscheinpositionDtoI.getIId(), theClientDto);
		getLieferscheinFac().updateTAendern(lieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
	}

	public void updateLieferscheinpositionAusRechnung(LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, TheClientDto theClientDto) throws EJBExceptionLP {
		updateLieferscheinpositionAusRechnung(oLieferscheinpositionDtoI, rechnungpositionIId,
				new ArrayList<SeriennrChargennrMitMengeDto>(), theClientDto);
	}

	/**
	 * Eine Lieferscheinposition aus der Rechnung nachtraeglich bearbeiten. <br>
	 * Chargenbehaftete Lieferscheinpositionen koennen nicht aktualisiert werden.
	 * 
	 * @param oLieferscheinpositionDtoI die aktuelle Lieferscheinposition
	 * @param rechnungpositionIId       Integer
	 * @param snrs                      die Seriennummern/Chargeninfos
	 * @param theClientDto              der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateLieferscheinpositionAusRechnung(LieferscheinpositionDto oLieferscheinpositionDtoI,
			Integer rechnungpositionIId, List<SeriennrChargennrMitMengeDto> snrs, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkLieferscheinpositionDto(oLieferscheinpositionDtoI);
		try {
			LieferscheinDto lsDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
			// Id der Rechnung merken
			Integer rechnungIId = lsDto.getRechnungIId();
			// Status wieder auf geliefert setzen
			getLieferscheinFacLocal().setzeStatusLieferschein(oLieferscheinpositionDtoI.getLieferscheinIId(),
					LieferscheinFac.LSSTATUS_GELIEFERT, null, theClientDto);
			// jetzt kann die Position upgedatet werden
			if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_HANDEINGABE)) {
				updateLieferscheinpositionHandeingabe(oLieferscheinpositionDtoI, theClientDto);
			} else if (oLieferscheinpositionDtoI.getLieferscheinpositionartCNr()
					.equals(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT)) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(oLieferscheinpositionDtoI.getArtikelIId(), theClientDto);

				if (Helper.short2boolean(artikelDto.getBChargennrtragend())) {
					updateLieferscheinpositionIdentChargennr(oLieferscheinpositionDtoI, theClientDto);
				} else if (Helper.short2boolean(artikelDto.getBSeriennrtragend())) {
					updateLieferscheinpositionIdentSeriennr(oLieferscheinpositionDtoI, theClientDto);
				} else {
					// lagerbewirtschaftet & !lagerbewirtschaftet (bucht intern
					// auf KEIN LAGER)
					updateLieferscheinpositionIdentLagerbewUndNichtLagerbew(oLieferscheinpositionDtoI, snrs,
							theClientDto);
				}
			} else {
				// die Lieferscheinposition und den Lieferscheinstatus
				// korrigieren
				updateLieferscheinstatusLieferscheinposition(oLieferscheinpositionDtoI, theClientDto);
			}
			// jetzt den LS wieder aktivieren
			getLieferscheinFac().aktiviereLieferschein(oLieferscheinpositionDtoI.getLieferscheinIId(), theClientDto);
			// Lieferschein wieder auf verrechnet setzen
			getLieferscheinFacLocal().setzeStatusLieferschein(oLieferscheinpositionDtoI.getLieferscheinIId(),
					LieferscheinFac.LSSTATUS_VERRECHNET, rechnungIId, theClientDto);
			// jetzt kommt noch ein update auf die Rechnungsposition, damit der
			// Preis wieder stimmt
			RechnungPositionDto rePosDto = getRechnungFac().rechnungPositionFindByPrimaryKey(rechnungpositionIId);
			getRechnungFac().updateRechnungPosition(rePosDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	private void pruefePflichtfelderBelegposition(LieferscheinpositionDto lsPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDtoVerkauf(lsPosDto, theClientDto);

		/**
		 * @todo hier spezielle positionsarten pruefen
		 */
	}

	/*
	 * Pr&uuml;ft, bzw. ersetzt Preise im Lager, die nicht in Mandantenwaehrung sind
	 */
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pruefeVKPreisAufLagerbewegung(TheClientDto theClientDto) throws EJBExceptionLP {

		// Lieferschein

		Query query = em.createNamedQuery("LieferscheinpositionfindByPositionartCNr");
		query.setParameter(1, LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		Collection<?> cl = query.getResultList();
		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Lieferscheinposition position = (Lieferscheinposition) iterator.next();
			Query queryLB = em.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIIdCSeriennrchargennr");
			queryLB.setParameter(1, LocaleFac.BELEGART_LIEFERSCHEIN);
			queryLB.setParameter(2, position.getIId());
			queryLB.setParameter(3, null);

			LieferscheinDto lieferscheinDto = null;

			try {
				lieferscheinDto = getLieferscheinFac().lieferscheinFindByPrimaryKey(position.getLieferscheinIId(),
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			Collection<?> clLB = queryLB.getResultList();
			Iterator<?> iteratorLB = clLB.iterator();
			while (iteratorLB.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iteratorLB.next();

				// Lieferschein holen

				BigDecimal preisInMandantenwaehrung = position
						.getNNettogesamtpreisplusversteckteraufschlagminusrabatte();

				if (!lieferscheinDto.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())
						&& position.getNMenge().doubleValue() != 0) {
					preisInMandantenwaehrung = position.getNNettogesamtpreisplusversteckteraufschlagminusrabatte()
							.divide(new BigDecimal(lieferscheinDto.getFWechselkursmandantwaehrungzubelegwaehrung()), 4,
									BigDecimal.ROUND_HALF_EVEN);
				}

				try {
					com.lp.server.artikel.service.LagerbewegungDto lagerbewegungDto = getLagerFac()
							.lagerbewegungFindByPrimaryKey(lagerbewegung.getIId());
					lagerbewegungDto.setNVerkaufspreis(preisInMandantenwaehrung);
					System.out.println("XXXXXXXXXXXXXXXXXXX " + position.getIId() + "  " + preisInMandantenwaehrung);
					getLagerFac().updateLagerbewegung(lagerbewegungDto, theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}

		// Rechnung

		query = em.createNamedQuery("RechnungpositionfindByPositionartCNr");
		query.setParameter(1, LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		Collection<?> clRechpos = query.getResultList();
		Iterator<?> iteratorRechpos = clRechpos.iterator();
		while (iteratorRechpos.hasNext()) {
			Rechnungposition position = (Rechnungposition) iteratorRechpos.next();
			Query queryLB = em.createNamedQuery("LagerbewegungfindByBelegartCNrBelegartPositionIId");
			queryLB.setParameter(1, LocaleFac.BELEGART_RECHNUNG);
			queryLB.setParameter(2, position.getIId());

			RechnungDto rechnungDto = null;

			try {
				rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(position.getRechnungIId());

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			Collection<?> clLB = queryLB.getResultList();
			Iterator<?> iteratorLB = clLB.iterator();
			while (iteratorLB.hasNext()) {
				Lagerbewegung lagerbewegung = (Lagerbewegung) iteratorLB.next();

				BigDecimal preisInMandantenwaehrung = position.getNNettoeinzelpreisplusaufschlagminusrabatt();

				if (!rechnungDto.getWaehrungCNr().equals(theClientDto.getSMandantenwaehrung())
						&& position.getNMenge().doubleValue() != 0) {
					preisInMandantenwaehrung = position.getNNettoeinzelpreisplusaufschlagminusrabatt().divide(
							new BigDecimal(rechnungDto.getFWechselkursmandantwaehrungzubelegwaehrung()), 4,
							BigDecimal.ROUND_HALF_EVEN);
				}

				try {
					com.lp.server.artikel.service.LagerbewegungDto lagerbewegungDto = getLagerFac()
							.lagerbewegungFindByPrimaryKey(lagerbewegung.getIId());
					lagerbewegungDto.setNVerkaufspreis(preisInMandantenwaehrung);
					System.out.println("XXXXXXXXXXXXXXXXXXX " + position.getIId() + "  " + preisInMandantenwaehrung);
					getLagerFac().updateLagerbewegung(lagerbewegungDto, theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}

	}

	public Integer createLieferscheinAusLieferschein(Integer lieferscheinIId, boolean bUebernimmKonditionenDesKunden,
			TheClientDto theClientDto) {
		Integer lieferrscheinIIdNeu = null;
		try {
			LieferscheinDto lieferscheinDtoVorhanden = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(lieferscheinIId, theClientDto);
			lieferscheinDtoVorhanden.setIId(null);
			lieferscheinDtoVorhanden.setCNr(null);
			lieferscheinDtoVorhanden.setRechnungIId(null);
			lieferscheinDtoVorhanden.setAuftragIId(null);
			if (lieferscheinDtoVorhanden.getLieferscheinartCNr().equals(LieferscheinFac.LSART_AUFTRAG)) {
				lieferscheinDtoVorhanden.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
			}

			lieferscheinDtoVorhanden.setNGesamtwertinbelegwaehrung(null);
			lieferscheinDtoVorhanden.setNGesamtwertInLieferscheinwaehrung(null);
			lieferscheinDtoVorhanden.setNGestehungswertInMandantenwaehrung(null);

			lieferscheinDtoVorhanden
					.setTBelegdatum(Helper.cutTimestamp(new java.sql.Timestamp(System.currentTimeMillis())));

			lieferscheinDtoVorhanden.setTLieferaviso(null);

			if (bUebernimmKonditionenDesKunden) {

				KundeDto kundeDto = getKundeFac()
						.kundeFindByPrimaryKey(lieferscheinDtoVorhanden.getKundeIIdLieferadresse(), theClientDto);
				Double dAllgemeinerrabattsatz = new Double(0);
				if (kundeDto.getFRabattsatz() != null) {
					dAllgemeinerrabattsatz = kundeDto.getFRabattsatz();
				}
				lieferscheinDtoVorhanden.setFAllgemeinerRabattsatz(dAllgemeinerrabattsatz);

				if (kundeDto.getLieferartIId() != null) {
					lieferscheinDtoVorhanden.setLieferartIId(kundeDto.getLieferartIId());
				}
				if (kundeDto.getZahlungszielIId() != null) {
					lieferscheinDtoVorhanden.setZahlungszielIId(kundeDto.getZahlungszielIId());
				}
				if (kundeDto.getSpediteurIId() != null) {
					lieferscheinDtoVorhanden.setSpediteurIId(kundeDto.getSpediteurIId());
				}

			}

			lieferscheinDtoVorhanden.setTStorniert(null);
			lieferscheinDtoVorhanden.setPersonalIIdStorniert(null);

			lieferscheinDtoVorhanden.setTManuellerledigt(null);
			lieferscheinDtoVorhanden.setPersonalIIdManuellerledigt(null);
			lieferscheinDtoVorhanden.setStatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);

			// SP6638 Ist Vertreter noch im Unternehmen
			lieferscheinDtoVorhanden.setPersonalIIdVertreter(
					pruefeVertreterUndHoleBeiBedarfAktuellenAusKunde(lieferscheinDtoVorhanden.getPersonalIIdVertreter(),
							lieferscheinDtoVorhanden.getKundeIIdLieferadresse(),
							lieferscheinDtoVorhanden.getTBelegdatum(), theClientDto));

			lieferrscheinIIdNeu = getLieferscheinFac().createLieferschein(lieferscheinDtoVorhanden, theClientDto);

			LieferscheinpositionDto[] posDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);

			Integer positionIIdZugehoerig = null;

			for (int i = 0; i < posDtos.length; i++) {
				LieferscheinpositionDto posDto = posDtos[i];

				// Lieferschein auslassen

				if (posDto.getAuftragpositionIId() == null) {
					try {
						posDto.setIId(null);
						posDto.setLieferscheinIId(lieferrscheinIIdNeu);
						posDto.setBelegIId(lieferrscheinIIdNeu);
						posDto.setPositioniIdArtikelset(null);
						posDto.setAuftragpositionIId(null);
						posDto.setNMaterialzuschlagKurs(null);
						posDto.setTMaterialzuschlagDatum(null);

						if (posDto.getPositionsartCNr().equals(RechnungFac.POSITIONSART_RECHNUNG_HANDEINGABE)) {
							posDto.setArtikelIId(null);
						}

						if (posDto.getPositionsartCNr().equals(RechnungFac.POSITIONSART_RECHNUNG_IDENT)) {

							if (posDto.getNMenge() != null && posDto.getNEinzelpreis() != null) {

								// SP2157 Fremdwaehrungsrechnung muss denselben
								// Preis wie damals behalten
								VerkaufspreisDto verkaufspreisDto = getVkPreisfindungFac().berechnePreisfelder(
										posDto.getNEinzelpreis(), posDto.getFRabattsatz(),
										posDto.getFZusatzrabattsatz(), posDto.getMwstsatzIId(), 4, // @
										// todo
										// Konstante
										// PJ
										// 3778
										theClientDto);

								posDto.setNEinzelpreis(verkaufspreisDto.einzelpreis);
								posDto.setNNettoeinzelpreis(verkaufspreisDto.nettopreis);
								posDto.setNBruttoeinzelpreis(verkaufspreisDto.bruttopreis);

							}

							if (posDtos[i].getPositionIIdZugehoerig() != null) {
								posDto.setPositionIIdZugehoerig(positionIIdZugehoerig);
							}

							ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(posDto.getArtikelIId(),
									theClientDto);

							if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
								BigDecimal lagerstand = getLagerFac().getLagerstand(posDto.getArtikelIId(),
										lieferscheinDtoVorhanden.getLagerIId(), theClientDto);
								if (lagerstand.doubleValue() < posDto.getNMenge().doubleValue()) {
									posDto.setNMenge(lagerstand);
								}
							}

							if (Helper.short2boolean(artikelDto.getBSeriennrtragend())
									|| Helper.short2boolean(artikelDto.getBChargennrtragend())) {
								posDto.setNMenge(new BigDecimal(0));
							}

						}

						if (posDto.isIntelligenteZwischensumme()) {
							ZwsPositionMapper mapper = new ZwsPositionMapper(this, this);
							mapper.map(posDto, posDtos[i], lieferrscheinIIdNeu);
						}
						Integer lsposIId = getLieferscheinpositionFac().createLieferscheinposition(posDto, true,
								theClientDto);

						if (posDto.getPositionIIdZugehoerig() == null) {
							positionIIdZugehoerig = lsposIId;
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return lieferrscheinIIdNeu;
	}

	private LieferscheinDto findLieferscheinByLieferscheinpositionIId(Integer lieferscheinpositionIId) {
		if (null == lieferscheinpositionIId)
			return null;

		Lieferscheinposition lieferscheinposition = em.find(Lieferscheinposition.class, lieferscheinpositionIId);
		return getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinposition.getLieferscheinIId());

	}

	public Integer getLSPositionIIdFromPositionNummer(Integer lieferscheinIId, Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionIIdFromPositionNummer(lieferscheinIId, position,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getPositionIIdFromPositionNummer(Integer lieferscheinIId, Integer position) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(getAuftragpositionFac(),
				getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinIId));
		return numberHandler.getPositionIIdFromPositionNummer(lieferscheinIId, position,
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getPositionNummer(lsposIId, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(getAuftragpositionFac(),
				findLieferscheinByLieferscheinpositionIId(lsposIId));
		return numberHandler.getPositionNummer(lsposIId, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSLastPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(lsposIId, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLastPositionNummer(Integer lsposIId) {
		PositionNumberHandler numberHandler = new PositionNumberHandlerLieferschein(getAuftragpositionFac(),
				findLieferscheinByLieferscheinpositionIId(lsposIId));
		return numberHandler.getLastPositionNummer(lsposIId, new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getLSHighestPositionNumber(Integer lieferscheinIId) {
		LieferscheinpositionDto lieferscheinposDtos[] = lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);
		if (lieferscheinposDtos.length == 0)
			return 0;

		PositionNumberHandler numberHandler = new PositionNumberHandler();
		return numberHandler.getLastPositionNummer(lieferscheinposDtos[lieferscheinposDtos.length - 1].getIId(),
				new LieferscheinPositionNumberAdapter(em));
	}

	public Integer getHighestPositionNumber(Integer lieferscheinIId) throws EJBExceptionLP {
		LieferscheinpositionDto lieferscheinposDtos[] = lieferscheinpositionFindByLieferscheinIId(lieferscheinIId);
		if (lieferscheinposDtos.length == 0)
			return 0;

		return getLastPositionNummer(lieferscheinposDtos[lieferscheinposDtos.length - 1].getIId());
	}

	public boolean pruefeAufGleichenMwstSatz(Integer auftragIId, Integer vonPositionNumber, Integer bisPositionNumber)
			throws EJBExceptionLP {
		LieferscheinpositionDto dtos[] = lieferscheinpositionFindByLieferscheinIId(auftragIId);

		return getBelegVerkaufFac().pruefeAufGleichenMwstSatz(dtos, vonPositionNumber, bisPositionNumber);
	}

	public LieferscheinpositionDto setupLieferscheinpositionDto(LieferscheinDto ls, ArtikelDto artikel,
			BigDecimal menge, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lspos = new LieferscheinpositionDto();
		lspos.setArtikelIId(artikel.getIId());
		lspos.setLieferscheinIId(ls.getIId());
		lspos.setLieferscheinpositionartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		lspos.setNMenge(menge);
		lspos.setEinheitCNr(artikel.getEinheitCNr());

		lspos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		lspos.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		lspos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		lspos.setBRabattsatzuebersteuert(Helper.boolean2Short(false));

		return lspos;
	}

	@Override
	public void preiseEinesArtikelsetsUpdatenLocal(Integer positionIIdKopfartikel, TheClientDto theClientDto) {
		preiseEinesArtikelsetsUpdaten(positionIIdKopfartikel, theClientDto);
	}

	@Override
	public LieferscheinpositionDto lieferscheinpositionKopfFindByLieferscheinIdAuftragpositionId(Integer lieferscheinId,
			Integer auftragpositionId, TheClientDto theClientDto) {
		try {
			Auftragposition abpos = em.find(Auftragposition.class, auftragpositionId);
			if (abpos.getPositionIIdArtikelset() != null) {
				auftragpositionId = abpos.getPositionIIdArtikelset();
			}
			Query query = em.createNamedQuery("LieferscheinpositionfindByLieferscheinIIdAuftragpositionIId");
			query.setParameter(1, lieferscheinId);
			query.setParameter(2, auftragpositionId);
			List<Lieferscheinposition> lsPositions = query.getResultList();
			if (lsPositions.size() == 0)
				return null;

			return LieferscheinpositionDtoAssembler.createDto(lsPositions.get(0));
		} catch (Throwable t) {
			myLogger.error("Keine LSPositionen ermittelbar", t);
		}

		return null;
	}

	private BigDecimal getOffeneMenge(Integer lieferKundeId, Integer artikelId, TheClientDto theClientDto) {
		List<Pair<ForecastpositionId, BigDecimal>> listFcpos = getOffeneForecastMenge(lieferKundeId, artikelId);
		if (listFcpos.size() != 0) {
			return sum(listFcpos);
		}

		if (listFcpos.size() == 0) {
			List<Pair<AuftragpositionId, BigDecimal>> listAbpos = getOffeneAuftragMenge(lieferKundeId, artikelId,
					theClientDto);
			return sum(listAbpos);
		}

		return BigDecimal.ZERO;
	}

	private <T> BigDecimal sum(List<Pair<T, BigDecimal>> positions) {
		BigDecimal d = BigDecimal.ZERO;
		for (Pair<T, BigDecimal> pair : positions) {
			d = d.add(pair.getValue());
		}
		return d;
	}

	private DistributeResult distributeOrder(LieferscheinDto lsDto, LieferscheinpositionDto lsposDto,
			LieferscheinpositionDto vorherigeLsposDto, boolean artikelsetAufloesen,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto) throws RemoteException {
		List<Pair<AuftragpositionId, BigDecimal>> listAbpos = getOffeneAuftragMenge(lsDto.getKundeIIdLieferadresse(),
				lsposDto.getArtikelIId(), theClientDto);
		DistributeResult result = new DistributeResult(lsposDto.getNMenge());
		if (listAbpos.size() == 0)
			return result;

		BigDecimal zuVerteilen = lsposDto.getNMenge();
		Integer lastPosId = null;
		for (Pair<AuftragpositionId, BigDecimal> pair : listAbpos) {
			if (zuVerteilen.signum() == 0)
				break;

			Auftragposition abpos = em.find(Auftragposition.class, pair.getKey().id());
			BigDecimal offen = abpos.getNOffeneMenge();
			if (offen.signum() < 0)
				continue;

			BigDecimal m = offen.min(zuVerteilen);
			if (m.signum() > 0) {
				lsposDto.setNMenge(m);
				lsposDto.setAuftragpositionIId(abpos.getIId());
				lsposDto.setNEinzelpreis(abpos.getNNettoeinzelpreis());
				lsposDto.setNNettoeinzelpreis(abpos.getNNettoeinzelpreis());
				lsposDto.setNEinzelpreisplusversteckteraufschlag(abpos.getNNettoeinzelpreisplusversteckteraufschlag());
				lsposDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(
						abpos.getNNettogesamtpreisplusversteckteraufschlagminusrabatte());
				lsposDto.setNMaterialzuschlagKurs(abpos.getNMaterialzuschlagKurs());
				lsposDto.setNMaterialzuschlag(abpos.getNMaterialzuschlag());
				mwstsatzBestimmenUndNeuBerechnen(lsposDto, lsDto.getKundeIIdLieferadresse(), lsDto.getTBelegdatum(),
						theClientDto);
				// Falls die Menge auf mehrere Pos aufgeteilt werden muss,
				// sollen alle Pos den gleichen Einzelpreis haben (sonst
				// entstehen unterschiedliche Preise, weil beispielsweise
				// unterschiedliche Staffelpreis)
				if (lastPosId != null) {
					// Es wird eine weitere Position angelegt
					Integer iIdLieferscheinposition = getPKGeneratorObj()
							.getNextPrimaryKey(PKConst.PK_LIEFERSCHEINPOSITION);
					lsposDto.setIId(iIdLieferscheinposition);

					lsposDto.setISort(getMaxISort(lsDto.getIId()) + 1);
				}

				lastPosId = createLieferscheinpositionImpl(lsDto, lsposDto, vorherigeLsposDto, artikelsetAufloesen,
						identities, theClientDto);
				result.addLsPositionId(lastPosId);

				if (lsDto.getAuftragIId() == null) {
					// Einen Auftragsbezug herstellen, sofern noch keiner existiert
					lsDto.setAuftragIId(abpos.getAuftragIId());
					Lieferschein ls = em.find(Lieferschein.class, lsDto.getIId());
					ls.setAuftragIId(abpos.getAuftragIId());
					ls.setLieferscheinartCNr(LieferscheinFac.LSART_AUFTRAG);
					em.merge(ls);
				}
			}

			zuVerteilen = zuVerteilen.subtract(m);
		}

		if (zuVerteilen.signum() != 0 && lastPosId != null) {
			lsposDto.setNMenge(lsposDto.getNMenge().add(zuVerteilen));
			updateLieferscheinposition(lsposDto, identities, theClientDto);
			zuVerteilen = BigDecimal.ZERO;
		}

		result.setAmount(zuVerteilen);
		return result;
	}

	private DistributeResult distributeForecast(LieferscheinDto lsDto, LieferscheinpositionDto lsposDto,
			LieferscheinpositionDto vorherigeLsposDto, boolean artikelsetAufloesen,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto) throws RemoteException {
		List<Pair<ForecastpositionId, BigDecimal>> listFcpos = getOffeneForecastMenge(lsDto.getKundeIIdLieferadresse(),
				lsposDto.getArtikelIId());
		DistributeResult result = new DistributeResult(lsposDto.getNMenge());
		if (listFcpos.size() == 0)
			return result;

		BigDecimal zuVerteilen = lsposDto.getNMenge();
		BigDecimal nettoEinzelPreis = null;
		Integer lastPosId = null;

		for (Pair<ForecastpositionId, BigDecimal> pair : listFcpos) {
			if (zuVerteilen.signum() == 0)
				break;

			BigDecimal bereitsGeliefert = getForecastFac().getBereitsGelieferteMenge(pair.getKey().id());
			Forecastposition fcpos = em.find(Forecastposition.class, pair.getKey().id());
			BigDecimal offen = fcpos.getNMenge().subtract(bereitsGeliefert);
			if (offen.signum() < 0)
				continue;

			BigDecimal m = offen.min(zuVerteilen);
			if (m.signum() > 0) {
				lsposDto.setNMenge(m);
				lsposDto.setForecastpositionIId(fcpos.getIId());

				// Falls die Menge auf mehrere Pos aufgeteilt werden muss,
				// sollen alle Pos den gleichen Einzelpreis haben (sonst
				// entstehen unterschiedliche Preise, weil beispielsweise
				// unterschiedliche Staffelpreis)
				if (nettoEinzelPreis != null) {
					// Es wird eine weitere Position angelegt
					Integer iIdLieferscheinposition = getPKGeneratorObj()
							.getNextPrimaryKey(PKConst.PK_LIEFERSCHEINPOSITION);
					lsposDto.setIId(iIdLieferscheinposition);

					lsposDto.setISort(getMaxISort(lsDto.getIId()) + 1);
					lsposDto.setNNettoeinzelpreis(nettoEinzelPreis);
				}

				lastPosId = createLieferscheinpositionImpl(lsDto, lsposDto, vorherigeLsposDto, artikelsetAufloesen,
						identities, theClientDto);
				result.addLsPositionId(lastPosId);

				if (nettoEinzelPreis == null) {
					nettoEinzelPreis = lsposDto.getNNettoeinzelpreis();
				}
			}

			zuVerteilen = zuVerteilen.subtract(m);
		}

		if (zuVerteilen.signum() != 0 && lastPosId != null) {
			lsposDto.setNMenge(lsposDto.getNMenge().add(zuVerteilen));
			updateLieferscheinposition(lsposDto, identities, theClientDto);
			zuVerteilen = BigDecimal.ZERO;
		}

		result.setAmount(zuVerteilen);
		return result;
	}

	private DistributeResult distributeVkPreispflichtig(LieferscheinDto lsDto, LieferscheinpositionDto lsposDto,
			LieferscheinpositionDto vorherigeLsposDto, boolean artikelsetAufloesen,
			List<SeriennrChargennrMitMengeDto> identities, TheClientDto theClientDto) throws RemoteException {
		DistributeResult result = new DistributeResult(lsposDto.getNMenge());

		if (lsposDto.isMengenbehaftet() && lsposDto.getNMenge() != null && lsposDto.getNMenge().signum() != 0
				&& lsposDto.getArtikelIId() != null) {
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(lsposDto.getArtikelIId(), theClientDto);
			if (Helper.isFalse(aDto.getBVkpreispflichtig()) || Helper.isFalse(aDto.getBLagerbewirtschaftet())) {
				Integer lastPosId = createLieferscheinpositionImpl(lsDto, lsposDto, vorherigeLsposDto,
						artikelsetAufloesen, identities, theClientDto);
				result.addLsPositionId(lastPosId);
				result.setAmount(BigDecimal.ZERO);
			}
		}

		return result;
	}

	private List<Pair<AuftragpositionId, BigDecimal>> getOffeneAuftragMenge(Integer lieferKundeId, Integer artikelId,
			TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria crit = session.createCriteria(FLRAuftragpositionReport.class);

		crit.createAlias("flrauftrag", "a");

		// Einschraenkung auf den aktuellen Mandanten
		crit.add(Restrictions.eq("a." + AuftragFac.FLR_AUFTRAG_MANDANT_C_NR, theClientDto.getMandant()));
		// Einschraenkung nach Auftragart

		crit.add(Restrictions.ne(AuftragpositionFac.FLR_AUFTRAGPOSITION_AUFTRAGPOSITIONSTATUS_C_NR,
				AuftragServiceFac.AUFTRAGPOSITIONSTATUS_ERLEDIGT));
		crit.add(Restrictions.isNotNull(AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE));
		crit.add(Restrictions.eq(AuftragpositionFac.FLR_AUFTRAGPOSITION_ARTIKEL_I_ID, artikelId));

		crit.add(Restrictions.ne("a." + AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR, AuftragServiceFac.AUFTRAGART_RAHMEN));
		crit.add(Restrictions.ne("a." + AuftragFac.FLR_AUFTRAG_AUFTRAGART_C_NR,
				AuftragServiceFac.AUFTRAGART_WIEDERHOLEND));

		// Einschraenkung nach Status Offen, Teilerledigt
		Collection<String> cStati = new LinkedList<String>();
		cStati.add(AuftragServiceFac.AUFTRAGSTATUS_OFFEN);

		cStati.add(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT);
		crit.add(Restrictions.in("a." + AuftragFac.FLR_AUFTRAG_AUFTRAGSTATUS_C_NR, cStati));

		// Sortierung nach Kunde, Lieferadresse, Artikel,Termin ASC
		crit.add(Restrictions.eq("a." + AuftragFac.FLR_AUFTRAG_KUNDE_I_ID_LIEFERADRESSE, lieferKundeId));

		crit.addOrder(Order.asc(AuftragpositionFac.FLR_AUFTRAGPOSITION_T_UEBERSTEUERTERLIEFERTERMIN));

		crit.addOrder(Order.asc("a." + AuftragFac.FLR_AUFTRAG_T_LIEFERTERMIN));

		List<FLRAuftragpositionReport> positions = crit.list();
		List<Pair<AuftragpositionId, BigDecimal>> results = new ArrayList<Pair<AuftragpositionId, BigDecimal>>();
		for (FLRAuftragpositionReport pos : positions) {
			results.add(new Pair<AuftragpositionId, BigDecimal>(new AuftragpositionId(pos.getI_id()),
					pos.getN_offenemenge()));
		}
		return results;
	}

	private List<Pair<ForecastpositionId, BigDecimal>> getOffeneForecastMenge(Integer lieferKundeId,
			Integer artikelId) {
		List<Fclieferadresse> fcLieferadressen = FclieferadresseQuery.listByKundeIId(lieferKundeId, em);
		if (fcLieferadressen.size() == 0) {
			return new ArrayList<Pair<ForecastpositionId, BigDecimal>>();
		}
		if (fcLieferadressen.size() != 1) {
			throw EJBExcFactory.fcLieferadresseMehrdeutig(fcLieferadressen.size(), lieferKundeId);
		}

		Integer fclieferadresseId = fcLieferadressen.get(0).getiId();
		List<Forecastauftrag> forecastauftraege = ForecastauftragQuery.listByForecastlieferadresseIIdStatusCNr(em,
				fclieferadresseId, LocaleFac.STATUS_FREIGEGEBEN);
		if (forecastauftraege.size() == 0) {
			return new ArrayList<Pair<ForecastpositionId, BigDecimal>>();
		}

		Integer fcAuftragId = forecastauftraege.get(0).getIId();
		Forecast fc = em.find(Forecast.class, fcLieferadressen.get(0).getForecastIId());

		List<Forecastposition> positions = ForecastpositionQuery.listByForecastauftragIIdArtikelIId(em, fcAuftragId,
				artikelId);

		List<Pair<ForecastpositionId, BigDecimal>> results = new ArrayList<Pair<ForecastpositionId, BigDecimal>>();
		for (Forecastposition position : positions) {
			if (LocaleFac.STATUS_ERLEDIGT.equals(position.getStatusCNr())) {
				continue;
			}

			String fcartCnr = position.getForecastartCNr();
			if (fcartCnr == null) {
				fcartCnr = HelperServer.getForecastartEienrForecastposition(new Date(position.getTTermin().getTime()),
						fc.getITageCod(), fc.getIWochenCow(), fc.getIMonateFca());
			}
			if (ForecastFac.FORECASTART_CALL_OFF_TAG.equals(fcartCnr)) {
				results.add(new Pair<ForecastpositionId, BigDecimal>(new ForecastpositionId(position.getIId()),
						position.getNMenge()));
			}
		}

		return results;
	}

	public class DistributeResult {
		private List<Integer> lspositionIds = new ArrayList<Integer>();
		private BigDecimal amount = BigDecimal.ZERO;

		public DistributeResult() {
		}

		public DistributeResult(BigDecimal amount) {
			setAmount(amount);
		}

		public void addLsPositionId(Integer positionId) {
			lspositionIds.add(positionId);
		}

		public List<Integer> getLsPositionIds() {
			return lspositionIds;
		}

		public Integer getLastPositionId() {
			int size = lspositionIds.size();
			if (size == 0) {
				throw new IllegalArgumentException();
			}
			return lspositionIds.get(size - 1);
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public BigDecimal getAmount() {
			return this.amount;
		}
	}
}
