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
package com.lp.server.projekt.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.angebot.ejb.Angebot;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.assembler.HistoryDtoAssembler;
import com.lp.server.projekt.assembler.ProjektDtoAssembler;
import com.lp.server.projekt.ejb.History;
import com.lp.server.projekt.ejb.Projekt;
import com.lp.server.projekt.ejb.Projektstatus;
import com.lp.server.projekt.ejb.ProjektstatusPK;
import com.lp.server.projekt.fastlanereader.ProjektverlaufHandler;
import com.lp.server.projekt.fastlanereader.generated.FLRHistory;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.HistoryartDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.projekt.service.ProjektServiceFac;
import com.lp.server.projekt.service.ProjektStatusDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocNodeProjektHistory;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ProjektFacBean extends Facade implements ProjektFac {
	@PersistenceContext
	private EntityManager em;

	private Integer getMaxISort(Integer personal_i_id_zugewiesener, TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iSort = null;
		String mandantCNr = theClientDto.getMandant();
		try {
			Query query = em.createNamedQuery("ProjektejbSelectMaxISort");
			query.setParameter(1, personal_i_id_zugewiesener);
			query.setParameter(2, mandantCNr);
			iSort = (Integer) query.getSingleResult();
			if (iSort == null) {
				iSort = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, new Exception(e));
		}
		return iSort;
	}

	public String istPartnerBeiEinemMandantenGesperrt(Integer partnerIId, TheClientDto theClientDto) {
		try {
			MandantDto[] mDto = getMandantFac().mandantFindAll(theClientDto);
			for (int i = 0; i < mDto.length; i++) {
				KundeDto kDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerIId, mDto[i].getCNr(),
						theClientDto);
				if (kDto != null && kDto.getTLiefersperream() != null) {
					return mDto[i].getCNr();
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	public Double berechneGesamtSchaetzung(Integer personal_i_id_zugewiesener, TheClientDto theClientDto) {
		double dSchaetzung = 0.0;
		String mandantCNr = theClientDto.getMandant();
		HvTypedQuery<Projekt> query = new HvTypedQuery<Projekt>(
				em.createNamedQuery("ProjektfindByPersonalIIdMandantCNrISort"));
		query.setParameter(1, personal_i_id_zugewiesener);
		query.setParameter(2, mandantCNr);
		Collection<Projekt> cl = query.getResultList();
		for (Projekt projekt : cl) {
			if (projekt.getFDauer() != null)
				dSchaetzung = dSchaetzung + projekt.getFDauer();
		}
		return dSchaetzung;
	}

	public Integer createProjekt(ProjektDto projektDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(projektDto, "projektDto");
		Integer projektIId = null;
		String projektCNr = null;
		try {

			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(projektDto.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(projektDto.getMandantCNr(), getDate());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr, PKConst.PK_PROJEKT,
					projektDto.getBereichIId() + "", projektDto.getMandantCNr(), theClientDto);

			projektIId = bnr.getPrimaryKey();
			projektCNr = f.format(bnr);
			projektDto.setIId(projektIId);
			projektDto.setCNr(projektCNr);
			projektDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			projektDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Projekt projekt = new Projekt(projektDto.getIId(), projektDto.getKategorieCNr(), projektDto.getCTitel(),
					projektDto.getPersonalIIdErzeuger(), projektDto.getPersonalIIdZugewiesener(), projektDto.getIPrio(),
					projektDto.getTZielwunschdatum(), projektDto.getPartnerIId(), projektDto.getIVerrechenbar(),
					projektDto.getPersonalIIdAnlegen(), projektDto.getPersonalIIdAendern(), projektDto.getStatusCNr(),
					projektDto.getMandantCNr(), projektDto.getTZeit(), projektDto.getProjekttypCNr(),
					projektDto.getDDauer(), projektDto.getCNr(), projektDto.getBFreigegeben(),
					projektDto.getBereichIId());
			em.persist(projekt);
			em.flush();

			projektDto.setTAnlegen(projekt.getTAnlegen());
			projektDto.setTAendern(projekt.getTAendern());
			setProjektFromProjektDto(projekt, projektDto);

			if (getMandantFac().hatModulNachrichten(theClientDto) && !projektDto.getPersonalIIdZugewiesener().equals(theClientDto.getIDPersonal())) {
				getNachrichtenFac().nachrichtProjektZugeordnet(projekt.getIId(),
						projektDto.getPersonalIIdZugewiesener(), theClientDto);
			}

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return projektIId;
	}

	public void removeProjekt(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {

		Projekt toStorno = em.find(Projekt.class, iId);

		if (getZeiterfassungFac().sindBelegzeitenVorhanden(LocaleFac.BELEGART_PROJEKT, iId)) {
			// Dann darf nicht storniert werden
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN,
					new Exception("FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN"));
		}

		// SP1752 -> Zuerst pruefen, ob der Status Storniert vorhanden ist

		javax.persistence.Query query = em.createNamedQuery("ProjektStatusfindMandantCNrCNr");
		query.setParameter(1, toStorno.getMandantCNr());
		query.setParameter(2, ProjektServiceFac.PROJEKT_STATUS_STORNIERT);
		Collection<?> cl = query.getResultList();

		if (cl.isEmpty()) {

			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("ProjektStatusejbSelectNextReihung");
				querynext.setParameter(1, toStorno.getMandantCNr());
				i = (Integer) querynext.getSingleResult();
			} catch (NoResultException ex) {
				// nothing here
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

			ProjektStatusDto psDto = new ProjektStatusDto();
			psDto.setCNr(ProjektServiceFac.PROJEKT_STATUS_STORNIERT);
			psDto.setMandantCNr(theClientDto.getMandant());
			psDto.setBAenderungprotokollieren(Helper.boolean2Short(false));
			psDto.setISort(i);
			try {
				getProjektServiceFac().createProjektStatus(psDto, theClientDto);
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		// Status auf Storniert setzen
		toStorno.setProjProjektstatusCNr(ProjektServiceFac.PROJEKT_STATUS_STORNIERT);
		em.merge(toStorno);
		em.flush();

	}

	public void removeProjekt(ProjektDto projektDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (projektDto != null) {
			Integer iId = projektDto.getIId();
			removeProjekt(iId, theClientDto);
		}
	}

	public void updateProjekt(ProjektDto projektDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(projektDto, "projektDto");
		projektDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		projektDto.setTAendern(getTimestamp());
		Projekt projekt = em.find(Projekt.class, projektDto.getIId());
		if (projekt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (!projekt.getBereichIId().equals(projektDto.getBereichIId())) {
			// Neues Projekt anlegen und dessen Nummer verwenden und das
			// neuaqngelegt dann wieder loeschen

			ProjektDto projektDtoZuLoeschen = projektFindByPrimaryKey(projektDto.getIId());
			projektDtoZuLoeschen.setBereichIId(projektDto.getBereichIId());

			Integer projektIIdZuLoeschen = createProjekt(projektDtoZuLoeschen, theClientDto);
			projektDtoZuLoeschen = projektFindByPrimaryKey(projektIIdZuLoeschen);
			// Belegnummern tauschen
			projektDto.setCNr(projektDtoZuLoeschen.getCNr());
			Projekt projektZuLoeschen = em.find(Projekt.class, projektIIdZuLoeschen);
			projektZuLoeschen.setCNr(projekt.getCNr());
			projektZuLoeschen.setProjProjektstatusCNr(ProjektServiceFac.PROJEKT_STATUS_STORNIERT);
			projektZuLoeschen.setProjektIIdNachfolger(projektDto.getIId());
			projektZuLoeschen.setBereichIId(projekt.getBereichIId());
			em.merge(projektZuLoeschen);
			verschiebeProjektdokumente(projektFindByPrimaryKey(projekt.getIId()), projektDto);
		}

		// PJ21351
		if (!projekt.getTZielwunschdatum().equals(projektDto.getTZielwunschdatum())) {
			nachfassterminInAngebotUpdaten(projekt.getIId(), projektDto.getTZielwunschdatum(), theClientDto);
		}

		if (getMandantFac().hatModulNachrichten(theClientDto)  && !projektDto.getPersonalIIdZugewiesener().equals(theClientDto.getIDPersonal())) {

			if (!projekt.getPersonalIIdZugewiesener().equals(projektDto.getPersonalIIdZugewiesener())) {

				getNachrichtenFac().nachrichtProjektZugeordnet(projekt.getIId(),
						projektDto.getPersonalIIdZugewiesener(), theClientDto);
			}
		}

		// PJ 17288 Statusaenderung protokollieren
		if (!projekt.getProjProjektstatusCNr().equals(projektDto.getStatusCNr())) {
			// PJ 18156 Wenn ein Projekt Storniert werden soll und Zeiten drauf
			// sind, dann darf das nicht gehen
			if (projektDto.getStatusCNr().equals(ProjektServiceFac.PROJEKT_STATUS_STORNIERT)) {
				if (getZeiterfassungFac().sindBelegzeitenVorhanden(LocaleFac.BELEGART_PROJEKT, projektDto.getIId())) {
					// Dann darf nicht storniert werden
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PROJEKT_DARF_NICHT_STORNIERT_WERDEN_ZEITEN_VORHANDEN,
							"");
				}
			}

			ProjektstatusPK pk = new ProjektstatusPK(projektDto.getStatusCNr(), theClientDto.getMandant());
			Projektstatus projektstatus = em.find(Projektstatus.class, pk);
			if (projektstatus != null && Helper.short2boolean(projektstatus.getBAenderungprotokollieren()) == true) {

				HistoryDto historyDto = new HistoryDto();
				historyDto.setCTitel(
						getTextRespectUISpr("proj.statusaenderung", theClientDto.getMandant(), theClientDto.getLocUi())
								+ " " + projektDto.getStatusCNr().trim());
				historyDto.setTBelegDatum(new java.sql.Timestamp(System.currentTimeMillis()));
				historyDto.setProjektIId(projekt.getIId());
				historyDto.setPersonalIId(theClientDto.getIDPersonal());

				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto);

				String text = personalDto.getPartnerDto().formatFixTitelName1Name2() + " hat am "
						+ Helper.formatDatumZeit(historyDto.getTBelegDatum(), theClientDto.getLocUi());

				text += " den Status auf " + projektDto.getStatusCNr().trim() + " ge\u00E4ndert.";

				historyDto.setXText(text);

				createHistory(historyDto, theClientDto);
			}
		}

		if (!projekt.getProjProjektstatusCNr().equals(ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT))
			if (projektDto.getStatusCNr().equals(ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT))
				projektDto.setISort(null);
		// 15547

		try {

			boolean bVorherErledigt = false;

			ProjektStatusDto statusDtoVorher = getProjektServiceFac()
					.projektStatusFindByPrimaryKey(projekt.getProjProjektstatusCNr(), theClientDto);
			if (Helper.short2boolean(statusDtoVorher.getBErledigt())) {
				bVorherErledigt = true;
			}

			ProjektStatusDto statusDtoJetzt = getProjektServiceFac()
					.projektStatusFindByPrimaryKey(projektDto.getStatusCNr(), theClientDto);

			boolean bJetztErledigt = false;

			if (Helper.short2boolean(statusDtoJetzt.getBErledigt())) {
				bJetztErledigt = true;
			}

			if (bVorherErledigt == true && bJetztErledigt == true) {

			} else if (bVorherErledigt == false && bJetztErledigt == true) {
				projektDto.setTErledigt(new Timestamp(System.currentTimeMillis()));
				projektDto.setPersonalIIdErlediger(theClientDto.getIDPersonal());
			} else if (bVorherErledigt == true && bJetztErledigt == false) {
				projektDto.setTErledigt(null);
				projektDto.setPersonalIIdErlediger(null);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		setProjektFromProjektDto(projekt, projektDto);

	}

	private void verschiebeProjektdokumente(ProjektDto projektDtoAlt, ProjektDto projektDtoNeu) {
		// History

		BereichDto bereichZuLoeschen = getProjektServiceFac().bereichFindByPrimaryKey(projektDtoAlt.getBereichIId());
		BereichDto bereichNeu = getProjektServiceFac().bereichFindByPrimaryKey(projektDtoNeu.getBereichIId());

		HistoryDto[] historyDtos = historyFindByProjektIid(projektDtoAlt.getIId());
		for (int i = 0; i < historyDtos.length; i++) {

			getJCRDocFac().verschiebeBzwKopiereDokumentInAnderenDocPath(
					new DocPath(new DocNodeProjektHistory(historyDtos[i], projektDtoAlt, bereichZuLoeschen)),
					new DocPath(new DocNodeProjektHistory(historyDtos[i], projektDtoNeu, bereichNeu)), false);

		}

		// Dokumente verschieben SP5310
		getJCRDocFac().verschiebeBzwKopiereDokumentInAnderenDocPath(
				new DocPath(new DocNodeProjekt(projektDtoAlt, bereichZuLoeschen)),
				new DocPath(new DocNodeProjekt(projektDtoNeu, bereichNeu)), false);

	}

	public ProjektDto projektFindByPrimaryKeyOhneExc(Integer iId) {
		Projekt projekt = em.find(Projekt.class, iId);

		return projekt == null ? null : assembleProjektDto(projekt);
	}

	public ProjektDto projektFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		ProjektDto projektDto = projektFindByPrimaryKeyOhneExc(iId);
		if (projektDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return projektDto;
	}

	public ProjektDto[] projektFindByPartnerIIdMandantCNr(Integer iPartnerId, String cNrMandant) throws EJBExceptionLP {
		// try {
		javax.persistence.Query query = em.createNamedQuery("ProjektfindByPartnerIIdMandantCNr");
		query.setParameter(1, iPartnerId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		// }
		return assembleProjektDtos(cl);

		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, fe);
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public ProjektDto projektfindByBereichIIdArtikelIId(Integer bereichIId, Integer artikelIId) {

		javax.persistence.Query query = em.createNamedQuery("ProjektfindByBereichIIdArtikelIId");
		query.setParameter(1, bereichIId);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();

		if (cl.size() > 0) {
			Projekt projekt = (Projekt) cl.iterator().next();

			return assembleProjektDto(projekt);
		} else {
			return null;
		}

	}

	public LinkedHashMap<String, ProjektVerlaufHelperDto> getProjektVerlauf(Integer projektIId,
			TheClientDto theClientDto) {
		try {
			FilterKriterium[] filterKrit = new FilterKriterium[1];
			FilterKriterium krit1 = new FilterKriterium("projekt_i_id", true, projektIId.toString(),
					FilterKriterium.OPERATOR_EQUAL, false);

			filterKrit[0] = krit1;

			QueryParameters p = new QueryParameters(QueryParameters.UC_ID_PROJEKTVERLAUF, null,
					new FilterBlock(filterKrit, "AND"), null, null);

			ProjektverlaufHandler pv = new ProjektverlaufHandler();
			pv.setCurrentUser(theClientDto);
			pv.setQuery(p);

			return pv.setInhalt();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public ProjektDto[] projektFindByPartnerIIdMandantCNrOhneExc(Integer iPartnerId, String cNrMandant)
			throws EJBExceptionLP {
		// try {
		javax.persistence.Query query = em.createNamedQuery("ProjektfindByPartnerIIdMandantCNr");
		query.setParameter(1, iPartnerId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		return assembleProjektDtos(cl);
	}

	public ProjektDto projektFindByMandantCNrCNrOhneExc(String cNrMandantI, String cNrI) {
		Query query = em.createNamedQuery("ProjektfindByCNrMandantCNr");
		query.setParameter(1, cNrI);
		query.setParameter(2, cNrMandantI);

		Projekt projekt;
		try {
			projekt = (Projekt) query.getSingleResult();
			return assembleProjektDto(projekt);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public ProjektDto[] projektFindByCNrMandantCNr(String cNr, String mandantCNr) {
		javax.persistence.Query query = em.createNamedQuery("ProjektfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		Collection<?> projekt = query.getResultList();
		return assembleProjektDtos(projekt);
	}

	public void toggleInternErledigt(Integer projektIId, TheClientDto theClientDto) {
		Projekt projekt = em.find(Projekt.class, projektIId);
		if (projekt.getTInternerledigt() == null) {
			projekt.setTInternerledigt(new Timestamp(System.currentTimeMillis()));
			projekt.setPersonalIIdInternerledigt(theClientDto.getIDPersonal());
		} else {
			projekt.setTInternerledigt(null);
			projekt.setPersonalIIdInternerledigt(null);
		}
	}

	public ArrayList<String> getVorgaengerProjekte(Integer projektIId) {
		ArrayList<String> projekte = new ArrayList<String>();
		HvTypedQuery<Projekt> query = new HvTypedQuery<Projekt>(
				em.createNamedQuery("ProjektfindByProjektIIdNachfolger"));
		query.setParameter(1, projektIId);
		Collection<Projekt> cl = query.getResultList();
		for (Projekt projekt : cl) {
			projekte.add(getProjektServiceFac().bereichFindByPrimaryKey(projekt.getBereichIId()).getCBez() + " "
					+ projekt.getCNr());
		}

		return projekte;
	}

	public ProjektDto[] projektFindByAnsprechpartnerIIdMandantCNr(Integer iAnsprechpartnerId, String cNrMandant)
			throws EJBExceptionLP {
		javax.persistence.Query query = em.createNamedQuery("ProjektfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iAnsprechpartnerId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		return assembleProjektDtos(cl);
	}

	public ProjektDto[] projektFindByAnsprechpartnerIIdMandantCNrOhneExc(Integer iAnsprechpartnerId,
			String cNrMandant) {
		javax.persistence.Query query = em.createNamedQuery("ProjektfindByAnsprechpartnerIIdMandantCNr");
		query.setParameter(1, iAnsprechpartnerId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		return assembleProjektDtos(cl);
	}

	private void setProjektFromProjektDto(Projekt projekt, ProjektDto projektDto) {
		projekt.setKategorieCNr(projektDto.getKategorieCNr());
		projekt.setCTitel(projektDto.getCTitel());
		projekt.setPersonalIIdErzeuger(projektDto.getPersonalIIdErzeuger());
		projekt.setPersonalIIdZugewiesener(projektDto.getPersonalIIdZugewiesener());
		projekt.setProjProjekttypCNr(projektDto.getProjekttypCNr());
		projekt.setIPrio(projektDto.getIPrio());
		projekt.setProjProjektstatusCNr(projektDto.getStatusCNr());
		projekt.setMandantCNr(projektDto.getMandantCNr());
		projekt.setOAttachments(projektDto.getOAttachments());
		projekt.setCAttachmentstype(projektDto.getCAttachmentsType());
		projekt.setCDateiname(projektDto.getCDateiname());
		projekt.setXFreetext(projektDto.getXFreetext());
		projekt.setTZielwunschdatum(projektDto.getTZielwunschdatum());
		projekt.setPartnerIId(projektDto.getPartnerIId());
		projekt.setAnsprechpartnerIId(projektDto.getAnsprechpartnerIId());
		projekt.setIVerrechenbar(projektDto.getIVerrechenbar());
		projekt.setPersonalIIdAnlegen(projektDto.getPersonalIIdAnlegen());
		projekt.setTAnlegen(projektDto.getTAnlegen());
		projekt.setPersonalIIdAendern(projektDto.getPersonalIIdAendern());
		projekt.setTAendern(projektDto.getTAendern());
		projekt.setFDauer(projektDto.getDDauer());
		projekt.setTZeit(projektDto.getTZeit());
		projekt.setTErledigt(projektDto.getTErledigt());
		projekt.setPersonalIIdErlediger(projektDto.getPersonalIIdErlediger());
		projekt.setCNr(projektDto.getCNr());
		projekt.setBFreigegeben(projektDto.getBFreigegeben());
		projekt.setISort(projektDto.getISort());
		projekt.setIWahrscheinlichkeit(projektDto.getIWahrscheinlichkeit());
		projekt.setNUmsatzgeplant(projektDto.getNUmsatzgeplant());
		projekt.setProjekterledigungsgrundIId(projektDto.getProjekterledigungsgrundIId());
		projekt.setBereichIId(projektDto.getBereichIId());
		projekt.setProjektIIdNachfolger(projektDto.getProjektIIdNachfolger());
		projekt.setTInternerledigt(projektDto.getTInternerledigt());
		projekt.setPersonalIIdInternerledigt(projektDto.getPersonalIIdInternerledigt());
		projekt.setBuildNumber(projektDto.getBuildNumber());
		projekt.setDeployNumber(projektDto.getDeployNumber());
		projekt.setPartnerIIdBetreiber(projektDto.getPartnerIIdBetreiber());
		projekt.setVkfortschrittIId(projektDto.getVkfortschrittIId());
		projekt.setTRealisierung(projektDto.getTRealisierung());
		projekt.setAnsprechpartnerIIdBetreiber(projektDto.getAnsprechpartnerIIdBetreiber());
		projekt.setArtikelIId(projektDto.getArtikelIId());
		projekt.setEditorContentIId(projektDto.hasContentId() ? projektDto.getContentId().id() : null);
		em.merge(projekt);
		em.flush();
	}

	private ProjektDto assembleProjektDto(Projekt projekt) {
		return ProjektDtoAssembler.createDto(projekt);
	}

	private ProjektDto[] assembleProjektDtos(Collection<?> projekts) {
		return ProjektDtoAssembler.createDtos(projekts);
	}

	public Integer createHistory(HistoryDto historyDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Integer historyIId = null;
		PKGeneratorObj pkGen = new PKGeneratorObj();
		historyIId = pkGen.getNextPrimaryKey(PKConst.PK_HISTORY);
		historyDto.setIId(historyIId);
		if (historyDto.getTBelegDatum() == null) {
			historyDto.setTBelegDatum(new Timestamp(System.currentTimeMillis()));
		}

		if (historyDto.getBHtml() == null) {
			historyDto.setBHtml(Helper.getShortFalse());
		}

		if (historyDto.getFErledigungsgrad() == null) {
			historyDto.setFErledigungsgrad(0D);
		}

		historyDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		historyDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		historyDto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		historyDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		historyDto.setPersonalIId(theClientDto.getIDPersonal());
		Projekt projekt = em.find(Projekt.class, historyDto.getProjektIId());
		try {
			History history = new History(historyDto.getIId(), historyDto.getPersonalIId(), historyDto.getTBelegDatum(),
					historyDto.getXText(), projekt, historyDto.getBHtml(), historyDto.getPersonalIIdAnlegen(),
					historyDto.getTAnlegen(), historyDto.getPersonalIIdAendern(), historyDto.getTAendern(),
					historyDto.getFErledigungsgrad());
			em.persist(history);
			em.flush();
			setHistoryFromHistoryDto(history, historyDto);

			zielterminUpdaten(historyDto, theClientDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception());
		}

		return historyIId;
	}

	private void nachfassterminInAngebotUpdaten(Integer projektIId, Timestamp tZielwunschdatum,
			TheClientDto theClientDto) {
		boolean bNAchfassterminUpdaten = false;
		try {
			ParametermandantDto pm = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_NACHFASSTERMIN_AUS_PROJEKT_AKTUALISIEREN);
			bNAchfassterminUpdaten = (Boolean) pm.getCWertAsObject();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bNAchfassterminUpdaten) {

			javax.persistence.Query query = em.createNamedQuery("AngebotfindByProjektIId");
			query.setParameter(1, projektIId);
			Collection<?> cl = query.getResultList();

			Iterator it = cl.iterator();
			while (it.hasNext()) {
				Angebot angebot = (Angebot) it.next();
				if (angebot.getAngebotstatusCNr().equals(LocaleFac.STATUS_OFFEN)) {
					angebot.setTNachfasstermin(tZielwunschdatum);
				}
			}
		}

	}

	private void zielterminUpdaten(HistoryDto historyDto, TheClientDto theClientDto) {
		// PJ16517
		if (historyDto.getHistoryartIId() != null) {
			HistoryartDto ha = getProjektServiceFac().historyartFindByPrimaryKey(historyDto.getHistoryartIId());

			if (Helper.short2Boolean(ha.getBAktualisierezieltermin()) == true) {
				Session session = FLRSessionFactory.getFactory().openSession();
				String queryString = "SELECT h FROM FLRHistory h WHERE h.flrhistoryart.b_aktualisierezieltermin=1 AND h.projekt_i_id="
						+ historyDto.getProjektIId() + " ORDER BY h.t_belegdatum DESC";

				org.hibernate.Query query = session.createQuery(queryString);
				query.setMaxResults(1);
				List<?> results = query.list();
				if (results.size() > 0) {

					FLRHistory flrHistory = (FLRHistory) results.iterator().next();
					// SP4193 Wenn ich der letzte Eintrag mit
					// "aktualisiere Zieltermin" bin, dann Zieltermin updaten

					if (flrHistory.getI_id().equals(historyDto.getIId())) {

						// Zieltermin updaten
						Projekt p = em.find(Projekt.class, historyDto.getProjektIId());
						p.setTZielwunschdatum(historyDto.getTBelegDatum());

						nachfassterminInAngebotUpdaten(historyDto.getProjektIId(), historyDto.getTBelegDatum(),
								theClientDto);

					}

				}
			}
		}
	}

	public void removeHistory(Integer iId) throws EJBExceptionLP {
		History toRemove = em.find(History.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeHistory(HistoryDto historyDto, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Integer iId = historyDto.getIId();
			removeHistory(iId);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception(t));
		}

	}

	public void updateHistory(HistoryDto historyDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (historyDto != null) {
			Integer iId = historyDto.getIId();

			historyDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			historyDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

			History history = em.find(History.class, iId);
			if (history == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setHistoryFromHistoryDto(history, historyDto);

			zielterminUpdaten(historyDto, theClientDto);

		}
	}

	public HistoryDto historyFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		HistoryDto history = historyFindByPrimaryKeyOhneExc(iId);
		if (history == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return history;
	}

	public HistoryDto historyFindByPrimaryKeyOhneExc(Integer iId) {
		History history = em.find(History.class, iId);
		return history == null ? null : assembleHistoryDto(history);
	}

	public HistoryDto[] historyFindByProjektIid(Integer iId) throws EJBExceptionLP {
		Projekt projekt = em.find(Projekt.class, iId);
		Collection<History> cl = projekt.getHistoryCollection();

		// javax.persistence.Query query =
		// em.createNamedQuery("HistoryfindByProjektIid");
		// query.setParameter(1, iId);
		// Collection<?> cl = query.getResultList();
		return assembleHistoryDtos(cl);

	}

	private void setHistoryFromHistoryDto(History history, HistoryDto historyDto) {
		if (historyDto.getPersonalIId() != null) {
			history.setPersonalIId(historyDto.getPersonalIId());
		}
		if (historyDto.getTBelegDatum() != null) {
			history.setTBelegdatum(historyDto.getTBelegDatum());
		}
		history.setXText(historyDto.getXText());
		history.setCTitel(historyDto.getCTitel());
		history.setHistoryartIId(historyDto.getHistoryartIId());
		// history.setProjektIId(historyDto.getProjektIId());
		history.setBHtml(historyDto.getBHtml());

		history.setTAendern(historyDto.getTAendern());
		history.setTAnlegen(historyDto.getTAnlegen());
		history.setPersonalIIdAendern(historyDto.getPersonalIIdAendern());
		history.setPersonalIIdAnlegen(historyDto.getPersonalIIdAnlegen());

		history.setPersonalIIdWirddurchgefuehrt(historyDto.getPersonalIIdWirddurchgefuehrt());
		history.setFErledigungsgrad(historyDto.getFErledigungsgrad());

		history.setNDauerGeplant(historyDto.getNDauerGeplant());

		em.merge(history);
		em.flush();
	}

	private HistoryDto assembleHistoryDto(History history) {
		return HistoryDtoAssembler.createDto(history);
	}

	private HistoryDto[] assembleHistoryDtos(Collection<?> historys) {
		List<HistoryDto> list = new ArrayList<HistoryDto>();
		if (historys != null) {
			Iterator<?> iterator = historys.iterator();
			while (iterator.hasNext()) {
				History history = (History) iterator.next();
				list.add(assembleHistoryDto(history));
			}
		}
		HistoryDto[] returnArray = new HistoryDto[list.size()];
		return list.toArray(returnArray);
	}

	public void inQueueAufnehmen(Integer iIdPosition1I, TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iSort = null;
		Projekt projekt = em.find(Projekt.class, iIdPosition1I);
		if (projekt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (projekt.getISort() == null) {
			iSort = getMaxISort(projekt.getPersonalIIdZugewiesener(), theClientDto);
			projekt.setISort(iSort + 1);
		}
		if (projekt.getProjProjektstatusCNr().equals(ProjektServiceFac.PROJEKT_STATUS_ERLEDIGT))
			projekt.setProjProjektstatusCNr(ProjektServiceFac.PROJEKT_STATUS_OFFEN);
	}

	public void ausQueueEntfernen(Integer iIdPosition1I, TheClientDto theClientDto) throws EJBExceptionLP {
		Projekt projekt = em.find(Projekt.class, iIdPosition1I);
		if (projekt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		projekt.setISort(null);
	}

	public void vertauscheProjekte(Integer iIdPosition1I, Integer iIdPosition2I, int min) throws EJBExceptionLP {
		Projekt oProjekt = em.find(Projekt.class, iIdPosition1I);
		if (oProjekt == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		Projekt oProjekt2 = em.find(Projekt.class, iIdPosition2I);
		Integer iSort1 = oProjekt.getISort();
		Integer iSort2 = oProjekt2.getISort();
		// das zweite iSort auf ungueltig setzen, damit UK constraint nicht
		// verletzt wird
		oProjekt2.setISort(new Integer(-1));
		oProjekt.setISort(iSort2);
		oProjekt2.setISort(iSort1);
	}

	public String getBelegnr(Integer projektNummer, Integer geschaeftsjahr, TheClientDto theClientDto)
			throws RemoteException {
		LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());

		ParametermandantDto pm = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG);
		String mk = pm.getCWert().trim();

		LpBelegnummer bnr = new LpBelegnummer(geschaeftsjahr, mk, projektNummer);
		return f.format(bnr);
	}

}
