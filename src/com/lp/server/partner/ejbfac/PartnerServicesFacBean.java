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
package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.partner.ejb.Beauskunftung;
import com.lp.server.partner.ejb.Branche;
import com.lp.server.partner.ejb.Branchespr;
import com.lp.server.partner.ejb.BranchesprPK;
import com.lp.server.partner.ejb.Dsgvokategorie;
import com.lp.server.partner.ejb.Dsgvokategoriespr;
import com.lp.server.partner.ejb.DsgvokategoriesprPK;
import com.lp.server.partner.ejb.Dsgvotext;
import com.lp.server.partner.ejb.Identifikation;
import com.lp.server.partner.ejb.Identifikationspr;
import com.lp.server.partner.ejb.IdentifikationsprPK;
import com.lp.server.partner.ejb.Kommunikationsart;
import com.lp.server.partner.ejb.Kommunikationsartspr;
import com.lp.server.partner.ejb.KommunikationsartsprPK;
import com.lp.server.partner.ejb.Kontaktart;
import com.lp.server.partner.ejb.Liefermengen;
import com.lp.server.partner.ejb.Newslettergrund;
import com.lp.server.partner.ejb.Partnerbild;
import com.lp.server.partner.ejb.Partnerkommentar;
import com.lp.server.partner.ejb.Partnerkommentarart;
import com.lp.server.partner.ejb.Partnerkommentardruck;
import com.lp.server.partner.ejb.Selektion;
import com.lp.server.partner.ejb.Selektionspr;
import com.lp.server.partner.ejb.SelektionsprPK;
import com.lp.server.partner.ejb.Serienbrief;
import com.lp.server.partner.ejb.Serienbriefselektion;
import com.lp.server.partner.ejb.SerienbriefselektionPK;
import com.lp.server.partner.ejb.Serienbriefselektionnegativ;
import com.lp.server.partner.ejb.SerienbriefselektionnegativPK;
import com.lp.server.partner.fastlanereader.generated.FLRKontaktart;
import com.lp.server.partner.fastlanereader.generated.FLRPartnerkommentardruck;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.BeauskunftungDto;
import com.lp.server.partner.service.BeauskunftungDtoAssembler;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.BrancheDtoAssembler;
import com.lp.server.partner.service.BranchesprDto;
import com.lp.server.partner.service.BranchesprDtoAssembler;
import com.lp.server.partner.service.DsgvokategorieDto;
import com.lp.server.partner.service.DsgvokategorieDtoAssembler;
import com.lp.server.partner.service.DsgvokategoriesprDtoAssembler;
import com.lp.server.partner.service.DsgvotextDto;
import com.lp.server.partner.service.DsgvotextDtoAssembler;
import com.lp.server.partner.service.IdentifikationDto;
import com.lp.server.partner.service.IdentifikationDtoAssembler;
import com.lp.server.partner.service.IdentifikationsprDto;
import com.lp.server.partner.service.IdentifikationsprDtoAssembler;
import com.lp.server.partner.service.KommunikationsartDto;
import com.lp.server.partner.service.KommunikationsartDtoAssembler;
import com.lp.server.partner.service.KommunikationsartsprDto;
import com.lp.server.partner.service.KommunikationsartsprDtoAssembler;
import com.lp.server.partner.service.KontaktartDto;
import com.lp.server.partner.service.KontaktartDtoAssembler;
import com.lp.server.partner.service.KundeSelectCriteriaDto;
import com.lp.server.partner.service.LiefermengenDto;
import com.lp.server.partner.service.LiefermengenDtoAssembler;
import com.lp.server.partner.service.NewslettergrundDto;
import com.lp.server.partner.service.NewslettergrundDtoAssembler;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerServicesFac;
import com.lp.server.partner.service.PartnerkommentarDto;
import com.lp.server.partner.service.PartnerkommentarDtoAssembler;
import com.lp.server.partner.service.PartnerkommentarartDto;
import com.lp.server.partner.service.PartnerkommentarartDtoAssembler;
import com.lp.server.partner.service.PartnerkommentardruckDto;
import com.lp.server.partner.service.PartnerkommentardruckDtoAssembler;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.SelektionDtoAssembler;
import com.lp.server.partner.service.SelektionsprDto;
import com.lp.server.partner.service.SelektionsprDtoAssembler;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefDtoAssembler;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionDtoAssembler;
import com.lp.server.partner.service.SerienbriefselektionnegativDto;
import com.lp.server.partner.service.SerienbriefselektionnegativDtoAssembler;
import com.lp.server.reklamation.ejb.Fehler;
import com.lp.server.reklamation.ejb.Fehlerspr;
import com.lp.server.reklamation.ejb.FehlersprPK;
import com.lp.server.reklamation.ejb.Schwere;
import com.lp.server.reklamation.ejb.Schwerespr;
import com.lp.server.reklamation.ejb.SchweresprPK;
import com.lp.server.reklamation.service.FehlerDto;
import com.lp.server.reklamation.service.FehlersprDto;
import com.lp.server.reklamation.service.FehlersprDtoAssembler;
import com.lp.server.reklamation.service.SchwereDto;
import com.lp.server.stueckliste.ejb.Montageart;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.util.EJBExceptionLP;
import com.lp.util.HVPdfReport;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

@Stateless
public class PartnerServicesFacBean extends Facade implements PartnerServicesFac, PartnerServicesFacLocal {

	@PersistenceContext
	private EntityManager em;

	public String createKommunikationsart(KommunikationsartDto kommunikationsartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (kommunikationsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kommunikationsartDtoI == null"));
		}

		Kommunikationsart kommunikationsart = null;
		try {
			kommunikationsart = new Kommunikationsart(kommunikationsartDtoI.getCNr());
			em.persist(kommunikationsart);
			em.flush();
			if (kommunikationsartDtoI.getKommunikationsartsprDto() != null) {
				kommunikationsartDtoI.getKommunikationsartsprDto()
						.setKommunikatiosartCNr(kommunikationsartDtoI.getCNr());
				Kommunikationsartspr kommunikationsartspr = new Kommunikationsartspr(kommunikationsartDtoI.getCNr(),
						theClientDto.getLocUiAsString());
				em.persist(kommunikationsartspr);
				em.flush();
				setKommunikationsartsprFromKommunikationsartsprDto(kommunikationsartspr,
						kommunikationsartDtoI.getKommunikationsartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return kommunikationsart.getCNr();
	}

	public void removeKommunikationsart(String cNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNrI == null"));
		}

		Query query = em.createNamedQuery("KommunikationsartsprfindByKommunikationsartCNr");
		query.setParameter(1, cNrI);
		Collection<?> c = query.getResultList();
		try {
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Kommunikationsartspr item = (Kommunikationsartspr) iter.next();
				em.remove(item);
			}
			Kommunikationsart kommunikationsart = em.find(Kommunikationsart.class, cNrI);
			if (kommunikationsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(kommunikationsart);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeKommunikationsart(KommunikationsartDto kommunikationsartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (kommunikationsartDtoI != null) {
			String cNr = kommunikationsartDtoI.getCNr();
			removeKommunikationsart(cNr, theClientDto);
		}
	}

	public LPDatenSubreport getSubreportAllerPartnerkommentare(Integer partnerIId, boolean bKunde,
			TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRPartnerkommentardruck AS druck WHERE druck.flrpartnerkommentar.partner_i_id="
				+ partnerIId + " AND druck.flrpartnerkommentar.i_art=" + PARTNERKOMMENTARART_MITDRUCKEN
				+ "  AND druck.flrpartnerkommentar.b_kunde= " + Helper.boolean2Short(bKunde)
				+ " ORDER BY druck.flrpartnerkommentar.flrpartnerkommentarart.c_bez, druck.flrpartnerkommentar";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<FLRPartnerkommentardruck> resultList = hquery.list();
		List<FLRPartnerkommentardruck> uniqueList = new ArrayList<FLRPartnerkommentardruck>();
		// Duplikate entfernen, da ein Kommentar evtl bei mehreren Belegen mitgedruckt
		// werden sollte, wir wollen hier jeden nur einmal
		// Durch query schon nach Partnerkommentarart -> c_bez und dann i_id sortiert.
		// Dadurch duplikate immer nacheinander
		for (FLRPartnerkommentardruck druck : resultList) {
			if (!uniqueList.isEmpty()) {
				FLRPartnerkommentardruck last = uniqueList.get(uniqueList.size() - 1);
				if (last.getFlrpartnerkommentar().getI_id() != druck.getFlrpartnerkommentar().getI_id()) {
					uniqueList.add(druck);
				}
			} else {
				uniqueList.add(druck);
			}
		}
		return createSubreportPartnerkommentar(theClientDto, uniqueList);
	}

	public LPDatenSubreport getSubreportAllerMitzudruckendenPartnerkommentare(Integer partnerIId, boolean bKunde,
			String belegartCNr, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRPartnerkommentardruck AS druck WHERE druck.flrpartnerkommentar.partner_i_id="
				+ partnerIId + " AND druck.belegart_c_nr='" + belegartCNr + "'  AND druck.flrpartnerkommentar.i_art="
				+ PARTNERKOMMENTARART_MITDRUCKEN + "  AND druck.flrpartnerkommentar.b_kunde= "
				+ Helper.boolean2Short(bKunde) + " ORDER BY druck.flrpartnerkommentar.flrpartnerkommentarart.c_bez ";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		return createSubreportPartnerkommentar(theClientDto, resultList);

	}

	private LPDatenSubreport createSubreportPartnerkommentar(TheClientDto theClientDto, List<?> resultList) {
		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<Object[]> subreportArtikelkommentare = new ArrayList<Object[]>();

		String[] fieldnames = new String[] { "F_KOMMENTARART", "F_MIMETYPE", "F_BILD", "F_KOMMENTAR", "F_PDF_OBJECT" };

		int iFeld_Subreport_Kommentarart = 0;
		int iFeld_Subreport_Mimetype = 1;
		int iFeld_Subreport_Bild = 2;
		int iFeld_Subreport_Kommentar = 3;
		int iFeld_Subreport_Pdf = 4;
		int iFeld_Subreport_iAnzahlSpalten = 5;

		while (resultListIterator.hasNext()) {
			FLRPartnerkommentardruck flr = (FLRPartnerkommentardruck) resultListIterator.next();
			PartnerkommentarDto partnerkommentarDto = partnerkommentarFindByPrimaryKey(
					flr.getFlrpartnerkommentar().getI_id(), theClientDto);

			Object[] oZeileVorlage = new Object[iFeld_Subreport_iAnzahlSpalten];
			oZeileVorlage[iFeld_Subreport_Kommentarart] = flr.getFlrpartnerkommentar().getFlrpartnerkommentarart()
					.getC_bez();
			oZeileVorlage[iFeld_Subreport_Mimetype] = partnerkommentarDto.getDatenformatCNr();
			oZeileVorlage[iFeld_Subreport_Kommentar] = partnerkommentarDto.getXKommentar();

			// Text Kommentar
			if (partnerkommentarDto.getDatenformatCNr().trim().indexOf(MediaFac.DATENFORMAT_MIMETYPEART_TEXT) != -1) {

				Object[] oZeile = oZeileVorlage.clone();
				subreportArtikelkommentare.add(oZeile);

			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
					|| partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
					|| partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
				byte[] bild = partnerkommentarDto.getOMedia();
				if (bild != null) {
					java.awt.image.BufferedImage myImage = Helper.byteArrayToImage(bild);

					Object[] oZeile = oZeileVorlage.clone();
					oZeile[iFeld_Subreport_Bild] = myImage;

					subreportArtikelkommentare.add(oZeile);

				}
			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {

				byte[] bild = partnerkommentarDto.getOMedia();

				java.awt.image.BufferedImage[] tiffs = Helper.tiffToImageArray(bild);
				if (tiffs != null) {
					for (int k = 0; k < tiffs.length; k++) {

						Object[] oZeile = oZeileVorlage.clone();
						oZeile[iFeld_Subreport_Bild] = tiffs[k];

						subreportArtikelkommentare.add(oZeile);

					}
				}

			} else if (partnerkommentarDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				byte[] pdf = partnerkommentarDto.getOMedia();
				HVPdfReport pdfObject = new HVPdfReport(pdf);
				Object[] oZeile = oZeileVorlage.clone();
				oZeile[iFeld_Subreport_Pdf] = pdfObject;
				subreportArtikelkommentare.add(oZeile);
			}

		}

		Object[][] dataSub = new Object[subreportArtikelkommentare.size()][fieldnames.length];
		dataSub = (Object[][]) subreportArtikelkommentare.toArray(dataSub);

		return new LPDatenSubreport(dataSub, fieldnames);
	}

	public void updateKommunikationsart(KommunikationsartDto kommunikationsartDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (kommunikationsartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("kommunikationsartDtoI == null"));
		}

		String cNr = kommunikationsartDtoI.getCNr();

		try {
			Kommunikationsart kommunikationsart = em.find(Kommunikationsart.class, cNr);
			if (kommunikationsart == null) { // @ToDo null Pruefung?
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (kommunikationsartDtoI.getKommunikationsartsprDto() != null && kommunikationsartDtoI.getCNr() != null) {

				// upd
				Kommunikationsartspr kommunikationsartspr = em.find(Kommunikationsartspr.class,
						new KommunikationsartsprPK(theClientDto.getLocUiAsString(), kommunikationsartDtoI.getCNr()));

				if (kommunikationsartspr == null) {
					Kommunikationsartspr kommunikationsartsprneu = new Kommunikationsartspr(
							kommunikationsartDtoI.getCNr(), theClientDto.getLocUiAsString());
					kommunikationsartsprneu.setCBez(kommunikationsartDtoI.getKommunikationsartsprDto().getCBez());

					em.persist(kommunikationsartsprneu);
					em.flush();
				} else {
					setKommunikationsartsprFromKommunikationsartsprDto(kommunikationsartspr,
							kommunikationsartDtoI.getKommunikationsartsprDto());

				}

			}
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public KommunikationsartDto kommunikationsartFindByPrimaryKey(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		KommunikationsartDto kommunikationsartDto = null;
		// try {
		Kommunikationsart kommunikationsart = em.find(Kommunikationsart.class, cNrI);
		if (kommunikationsart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		kommunikationsartDto = assembleKommunikationsartDto(kommunikationsart);

		try {
			Kommunikationsartspr kommunikationsartspr = em.find(Kommunikationsartspr.class,
					new KommunikationsartsprPK(theClientDto.getLocUiAsString(), cNrI));
			kommunikationsartDto.setKommunikationsartsprDto(assembleKommunikationsartsprDto(kommunikationsartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return kommunikationsartDto;
	}

	public KommunikationsartDto[] kommunikationsartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KommunikationsartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleKommunikationsartDtos(cl);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }

	}

	private KommunikationsartDto assembleKommunikationsartDto(Kommunikationsart kommunikationsart) {
		return KommunikationsartDtoAssembler.createDto(kommunikationsart);
	}

	private KommunikationsartDto[] assembleKommunikationsartDtos(Collection<?> kommunikationsarts) {

		List<KommunikationsartDto> list = new ArrayList<KommunikationsartDto>();
		if (kommunikationsarts != null) {
			Iterator<?> iterator = kommunikationsarts.iterator();
			while (iterator.hasNext()) {
				Kommunikationsart kommunikationsart = (Kommunikationsart) iterator.next();
				list.add(assembleKommunikationsartDto(kommunikationsart));
			}
		}
		KommunikationsartDto[] returnArray = new KommunikationsartDto[list.size()];
		return (KommunikationsartDto[]) list.toArray(returnArray);
	}

	public KundeSelectCriteriaDto getSerienbriefSelektionsKriterien(Integer serienbriefIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		SerienbriefDto serienbriefDto = null;
		try {
			serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		KundeSelectCriteriaDto alKundeSelectCriteriaDtoI = new KundeSelectCriteriaDto();

		alKundeSelectCriteriaDtoI.setCNrMandant(serienbriefDto.getMandantCNr());

		alKundeSelectCriteriaDtoI.setBAnsprechpartnerfktAuchOhne(
				Helper.short2boolean(serienbriefDto.getBAnsprechpartnerfunktionAuchOhne()));

		alKundeSelectCriteriaDtoI.setBInteressenten(Helper.short2boolean(serienbriefDto.getBGehtAnInteressenten()));

		alKundeSelectCriteriaDtoI.setBKunden(Helper.short2boolean(serienbriefDto.getBGehtAnKunden()));

		alKundeSelectCriteriaDtoI.setBVersteckt(Helper.short2boolean(serienbriefDto.getBVersteckteDabei()));

		alKundeSelectCriteriaDtoI.setIIdSerienbrief(serienbriefIId);

		alKundeSelectCriteriaDtoI.setIIdAnsprechpartnerfkt(serienbriefDto.getAnsprechpartnerfunktionIId());

		if (serienbriefDto.getCPlz() != null && !serienbriefDto.getCPlz().equals("")) {
			alKundeSelectCriteriaDtoI.setSPLZ(serienbriefDto.getCPlz() + "%");
		} else {
			alKundeSelectCriteriaDtoI.setSPLZ(null);
		}

		if (serienbriefDto.getLandIId() != null) {

			alKundeSelectCriteriaDtoI
					.setSLKZ(getSystemFac().landFindByPrimaryKey(serienbriefDto.getLandIId()).getCLkz());
		} else {
			alKundeSelectCriteriaDtoI.setSLKZ(null);
		}
		return alKundeSelectCriteriaDtoI;

	}

	public Map getAllNewslettergrund(TheClientDto theClientDto) {

		Map<Integer, String> content = null;

		// try {
		Query query = em.createNamedQuery("NewslettergrundfindAll");
		Collection<?> allArten = query.getResultList();
		content = new TreeMap<Integer, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Newslettergrund newslettergrund = (Newslettergrund) iter.next();

			Integer key = newslettergrund.getIId();
			String value = newslettergrund.getCBez();

			content.put(key, value);
		}

		return content;
	}

	public Map getAllKontaktart(TheClientDto theClientDto) {

		Map<Integer, String> content = null;

		// try {
		Query query = em.createNamedQuery("KontaktartfindAll");
		Collection<?> allArten = query.getResultList();
		content = new TreeMap<Integer, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Kontaktart kontaktart = (Kontaktart) iter.next();

			Integer key = kontaktart.getIId();
			String value = kontaktart.getCBez();

			content.put(key, value);
		}

		return content;
	}

	public Map getAllKommunikationsArten(String cNrLocaleI, TheClientDto theClientDto) {

		Map<String, String> content = null;

		// try {
		Query query = em.createNamedQuery("KommunikationsartfindAll");
		Collection<?> allArten = query.getResultList();
		content = new TreeMap<String, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Kommunikationsart kommunikationsartTemp = (Kommunikationsart) iter.next();

			String key = kommunikationsartTemp.getCNr();
			String value = null;
			// try {
			Kommunikationsartspr kommunikationsartspr = em.find(Kommunikationsartspr.class,
					new KommunikationsartsprPK(cNrLocaleI, kommunikationsartTemp.getCNr()));
			if (kommunikationsartspr == null) {
				value = kommunikationsartTemp.getCNr();
			} else {
				value = kommunikationsartspr.getCBez();
			}
			// }
			// catch (FinderException ex1) {
			// value = kommunikationsartTemp.getCNr();
			// }
			content.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return content;
	}

	public Map getAllBranche(String cNrLocaleI, TheClientDto theClientDto) {

		Map<Integer, String> content = null;

		// try {
		Query query = em.createNamedQuery("BranchefindAll");
		Collection<?> allArten = query.getResultList();
		content = new TreeMap<Integer, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Branche brancheTemp = (Branche) iter.next();

			Integer key = brancheTemp.getIId();
			String value = null;
			// try {
			Branchespr branchespr = em.find(Branchespr.class, new BranchesprPK(brancheTemp.getIId(), cNrLocaleI));
			if (branchespr == null) {
				value = brancheTemp.getCNr();
			} else {
				value = branchespr.getCBez();
			}

			content.put(key, value);
		}

		return content;
	}

	public Map getAllDSGVOKategorie(TheClientDto theClientDto) {

		Map<Integer, String> content = null;

		// try {
		Query query = em.createNamedQuery("DsgvokategoriefindAll");
		Collection<?> allArten = query.getResultList();
		content = new LinkedHashMap<Integer, String>();

		Iterator<?> iter = allArten.iterator();
		while (iter.hasNext()) {
			Dsgvokategorie temp = (Dsgvokategorie) iter.next();

			Integer key = temp.getIId();
			String value = null;
			// try {
			Dsgvokategoriespr spr = em.find(Dsgvokategoriespr.class,
					new DsgvokategoriesprPK(theClientDto.getLocUiAsString(), temp.getIId()));
			if (spr == null || spr != null && spr.getCBez() == null) {
				value = temp.getCNr();
			} else {
				value = spr.getCBez();
			}

			content.put(key, value);
		}

		return content;
	}

	public KontaktartDto getVorschlagFuerWiedervorlageAusSerienbrief() {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "FROM FLRKontaktart AS ka WHERE lower(c_bez) LIKE 'tel%'";

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		if (resultList.size() > 0) {
			FLRKontaktart ka = (FLRKontaktart) resultList.iterator().next();
			return kontaktartFindByPrimaryKey(ka.getI_id());

		} else {
			return null;
		}

	}

	public Integer createKontaktart(KontaktartDto kontaktartDto) {
		if (kontaktartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kontaktartDto == null"));
		}
		if (kontaktartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("kontaktartDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("KontaktartfindByCBez");
			query.setParameter(1, kontaktartDto.getCBez());
			Kontaktart doppelt = (Kontaktart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_KONTAKTART.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KONTAKTART);
			kontaktartDto.setIId(pk);

			Kontaktart kontaktart = new Kontaktart(kontaktartDto.getIId(), kontaktartDto.getCBez());
			em.persist(kontaktart);
			em.flush();
			setKontaktartFromKontaktartDto(kontaktart, kontaktartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return kontaktartDto.getIId();
	}

	public Integer createBeauskunftung(BeauskunftungDto dto, TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BEAUSKUNFTUNG);
			dto.setIId(pk);

			dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			dto.setTAnlegen(new Timestamp(System.currentTimeMillis()));

			Beauskunftung bean = new Beauskunftung(dto.getIId(), dto.getPartnerIId(), dto.getIdentifikationIId(),
					dto.getBKostenpflichtig(), dto.getPersonalIIdAnlegen(), dto.getTAnlegen());
			em.persist(bean);
			em.flush();
			setBeauskunftungFromBeauskunftungDto(bean, dto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return dto.getIId();
	}

	public Integer createDsgvotext(DsgvotextDto dto, TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DSGVOTEXT);
			dto.setIId(pk);

			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("DsgvotextejbSelectNextReihung");
				querynext.setParameter(1, theClientDto.getMandant());
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
			dto.setISort(i);

			Dsgvotext bean = new Dsgvotext(dto.getIId(), dto.getMandantCNr(), dto.getDsgvokategorieIId(),
					dto.getBKopftext(), dto.getISort(), dto.getXInhalt());
			em.persist(bean);
			em.flush();
			setDsgvotextFromDsgvotextDto(bean, dto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return dto.getIId();
	}

	public LiefermengenDto liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(Integer artikelIId,
			Integer kundeIIdLieferadresse, Timestamp tDatum) {
		Query query = em.createNamedQuery("LiefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum");
		query.setParameter(1, artikelIId);
		query.setParameter(2, kundeIIdLieferadresse);
		query.setParameter(3, tDatum);
		Collection c = query.getResultList();
		if (c.size() == 0) {
			return null;
		} else {
			return LiefermengenDtoAssembler.createDto((Liefermengen) c.iterator().next());
		}
	}

	public Integer createLiefermengen(LiefermengenDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getArtikelIId() == null || dto.getKundeIIdLieferadresse() == null || dto.getTDatum() == null
				|| dto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"dto.getArtikelIId() == null || dto.getKundeIIdLieferadresse() == null || dto.getTDatum() == null || dto.getNMenge() == null"));
		}
		try {
			Query query = em.createNamedQuery("LiefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getKundeIIdLieferadresse());
			query.setParameter(3, dto.getTDatum());
			Liefermengen doppelt = (Liefermengen) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_LIEFERMENGEN.UK"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LIEFERMENGEN);
			dto.setIId(pk);

			Liefermengen liefermengen = new Liefermengen(dto.getIId(), dto.getArtikelIId(),
					dto.getKundeIIdLieferadresse(), dto.getTDatum(), dto.getNMenge());
			em.persist(liefermengen);
			em.flush();
			setLiefermengenFromLiefermengenDto(liefermengen, dto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return dto.getIId();
	}

	public Integer createLiefermengenUnique(LiefermengenDto dto) {
		Validator.dtoNotNull(dto, "liefermengenDto");
		return createLiefermengenUniqueImpl(0, dto);
	}

	private Integer createLiefermengenUniqueImpl(int count, LiefermengenDto lmDto) {
		LiefermengenDto foundLmDto = liefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum(lmDto.getArtikelIId(),
				lmDto.getKundeIIdLieferadresse(), lmDto.getTDatum());
		if (foundLmDto == null) {
			return createLiefermengen(lmDto);
		} else {
			if (!foundLmDto.getNMenge().equals(lmDto.getNMenge())) {
				if (++count > 200) {
					myLogger.warn(
							"Bereits der '" + count + "' Versuch " + lmDto.toString() + "' einzufuegen. Abbruch!");
					return null;
				} else {
					lmDto.setTDatum(new Timestamp(lmDto.getTDatum().getTime() + 1000));
					return createLiefermengenUniqueImpl(count, lmDto);
				}
			} else {
				myLogger.warn(lmDto.toString() + " bereits vorhanden. Ignoriert.");
				return lmDto.getIId();
			}
		}
	}

	public void removeKontaktart(KontaktartDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		// try {
		Integer iId = dto.getIId();
		Kontaktart toRemove = em.find(Kontaktart.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, e);
		// }

	}

	public void removeLiefermengen(LiefermengenDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("dto.getIId() == null"));
		}

		Integer iId = dto.getIId();
		Liefermengen toRemove = em.find(Liefermengen.class, iId);
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

	public void removeBeauskunftung(BeauskunftungDto dto) {

		Integer iId = dto.getIId();
		Beauskunftung toRemove = em.find(Beauskunftung.class, iId);
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

	public void removeDsgvotext(DsgvotextDto dto) {

		Integer iId = dto.getIId();
		Dsgvotext toRemove = em.find(Dsgvotext.class, iId);
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

	public void updateKontaktart(KontaktartDto kontaktartDto) {
		if (kontaktartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("kontaktartDto == null"));
		}
		if (kontaktartDto.getIId() == null || kontaktartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("kontaktartDto.getIId() == null || kontaktartDto.getCBez() == null"));
		}

		Integer iId = kontaktartDto.getIId();
		// try {
		Kontaktart kontaktart = em.find(Kontaktart.class, iId);
		if (kontaktart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("KontaktartfindByCBez");
			query.setParameter(1, kontaktartDto.getCBez());
			Integer iIdVorhanden = ((Kontaktart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_KONTAKTART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		setKontaktartFromKontaktartDto(kontaktart, kontaktartDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void updateLiefermengen(LiefermengenDto dto) {
		if (dto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("dto == null"));
		}
		if (dto.getIId() == null || dto.getArtikelIId() == null || dto.getKundeIIdLieferadresse() == null
				|| dto.getTDatum() == null || dto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"dto.getIId() == null || dto.getArtikelIId() == null || dto.getKundeIIdLieferadresse() == null || dto.getTDatum() == null || dto.getNMenge() == null"));
		}

		Integer iId = dto.getIId();
		// try {
		Liefermengen liefermengen = em.find(Liefermengen.class, iId);
		if (liefermengen == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("LiefermengenFindByArtikelIIdKundeIIdLieferadresseTDatum");
			query.setParameter(1, dto.getArtikelIId());
			query.setParameter(2, dto.getKundeIIdLieferadresse());
			query.setParameter(3, dto.getTDatum());
			Integer iIdVorhanden = ((Liefermengen) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_LIEFERMENGEN.UC"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		setLiefermengenFromLiefermengenDto(liefermengen, dto);

	}

	public void updateBeauskunftung(BeauskunftungDto dto) {

		Integer iId = dto.getIId();
		// try {
		Beauskunftung beauskunftung = em.find(Beauskunftung.class, iId);
		if (beauskunftung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setBeauskunftungFromBeauskunftungDto(beauskunftung, dto);

	}

	public void updateDsgvotext(DsgvotextDto dto) {

		Integer iId = dto.getIId();
		// try {
		Dsgvotext bean = em.find(Dsgvotext.class, iId);
		setDsgvotextFromDsgvotextDto(bean, dto);

	}

	public KontaktartDto kontaktartFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Kontaktart kontaktart = em.find(Kontaktart.class, iId);
		if (kontaktart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKontaktartDto(kontaktart);

	}

	public BeauskunftungDto beauskunftungFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Beauskunftung beauskunftung = em.find(Beauskunftung.class, iId);
		if (beauskunftung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return BeauskunftungDtoAssembler.createDto(beauskunftung);

	}

	public DsgvotextDto dsgvotextFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Dsgvotext dsgvotext = em.find(Dsgvotext.class, iId);
		if (dsgvotext == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return DsgvotextDtoAssembler.createDto(dsgvotext);

	}

	public LiefermengenDto liefermengenFindByPrimaryKey(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Liefermengen bean = em.find(Liefermengen.class, iId);
		if (bean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return LiefermengenDtoAssembler.createDto(bean);

	}

	private void setBeauskunftungFromBeauskunftungDto(Beauskunftung bean, BeauskunftungDto dto) {
		bean.setBKostenpflichtig(dto.getBKostenpflichtig());
		bean.setIdentifikationIId(dto.getIdentifikationIId());
		bean.setPartnerIId(dto.getPartnerIId());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());
		bean.setTAnlegen(dto.getTAnlegen());

		em.merge(bean);
		em.flush();
	}

	private void setDsgvotextFromDsgvotextDto(Dsgvotext bean, DsgvotextDto dto) {
		bean.setBKopftext(dto.getBKopftext());
		bean.setDsgvokategorieIId(dto.getDsgvokategorieIId());
		bean.setISort(dto.getISort());
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setXInhalt(dto.getXInhalt());

		em.merge(bean);
		em.flush();
	}

	private void setKontaktartFromKontaktartDto(Kontaktart kontaktart, KontaktartDto kontaktartDto) {
		kontaktart.setCBez(kontaktartDto.getCBez());
		em.merge(kontaktart);
		em.flush();
	}

	private void setLiefermengenFromLiefermengenDto(Liefermengen bean, LiefermengenDto dto) {
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setKundeIIdLieferadresse(dto.getKundeIIdLieferadresse());
		bean.setNMenge(dto.getNMenge());
		bean.setTDatum(dto.getTDatum());
		bean.setCLstext(dto.getCLstext());
		em.merge(bean);
		em.flush();
	}

	private KontaktartDto assembleKontaktartDto(Kontaktart kontaktart) {
		return KontaktartDtoAssembler.createDto(kontaktart);
	}

	private PartnerkommentarDto assemblePartnerkommentarDto(Partnerkommentar partnerkommentar) {
		return PartnerkommentarDtoAssembler.createDto(partnerkommentar);
	}

	private KontaktartDto[] assembleKontaktartDtos(Collection<?> kontaktarts) {
		List<KontaktartDto> list = new ArrayList<KontaktartDto>();
		if (kontaktarts != null) {
			Iterator<?> iterator = kontaktarts.iterator();
			while (iterator.hasNext()) {
				Kontaktart aufnahmeart = (Kontaktart) iterator.next();
				list.add(assembleKontaktartDto(aufnahmeart));
			}
		}
		KontaktartDto[] returnArray = new KontaktartDto[list.size()];
		return (KontaktartDto[]) list.toArray(returnArray);
	}

	public Integer createBranche(BrancheDto brancheDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (brancheDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("brancheDtoI == null"));
		}

		Integer iId = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			iId = pkGen.getNextPrimaryKey(PKConst.PK_BRANCHE);
			brancheDtoI.setIId(iId);

			Branche branche = new Branche(brancheDtoI.getCNr(), brancheDtoI.getIId());
			em.persist(branche);
			em.flush();

			if (brancheDtoI.getBranchesprDto() != null) {
				Branchespr branchespr = new Branchespr(brancheDtoI.getIId(), theClientDto.getLocUiAsString());
				em.persist(branchespr);
				em.flush();
				setBranchesprFromBranchesprDto(branchespr, brancheDtoI.getBranchesprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public Integer createIdentifikation(IdentifikationDto dto, TheClientDto theClientDto) {

		Integer iId = null;
		try {
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			iId = pkGen.getNextPrimaryKey(PKConst.PK_IDENTIFIKATION);
			dto.setIId(iId);

			try {
				Query query = em.createNamedQuery("IdentifikationfindByCNr");
				query.setParameter(1, dto.getCNr());
				Identifikation doppelt = (Identifikation) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PART_IDENTIFIKATION.C_NR"));
				}
			} catch (NoResultException ex) {
				//
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

			Identifikation identifikation = new Identifikation(dto.getIId(), dto.getCNr());
			em.persist(identifikation);
			em.flush();
			setIdentifikationFromIdentifikationDto(identifikation, dto);
			if (dto.getIdentifikationsprDto() != null) {
				Identifikationspr spr = new Identifikationspr(theClientDto.getLocUiAsString(), dto.getIId());
				spr.setCBez(dto.getIdentifikationsprDto().getCBez());
				em.persist(spr);
				em.flush();
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public void removeIdentifikation(IdentifikationDto dto) {

		// try {
		Integer iId = dto.getIId();

		try {
			Query query = em.createNamedQuery("IdentifikationsprfindByIdentifikationIId");
			query.setParameter(1, iId);
			Collection<?> allspr = query.getResultList();
			Iterator<?> iter = allspr.iterator();
			while (iter.hasNext()) {
				Identifikationspr sprTemp = (Identifikationspr) iter.next();
				em.remove(sprTemp);
			}
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

		Identifikation toRemove = em.find(Identifikation.class, iId);
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

	public void updateIdentifikation(IdentifikationDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();
		// try {
		Identifikation identifikation = em.find(Identifikation.class, iId);

		try {
			Query query = em.createNamedQuery("IdentifikationfindByCNr");
			query.setParameter(1, dto.getCNr());
			Integer iIdVorhanden = ((Identifikation) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_IDENTIFIKATION.CNR"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		setIdentifikationFromIdentifikationDto(identifikation, dto);
		if (dto.getIdentifikationsprDto() != null) {

			Identifikationspr spr = em.find(Identifikationspr.class,
					new IdentifikationsprPK(theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				try {
					spr = new Identifikationspr(theClientDto.getLocUiAsString(), iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex7);

				}
			}
			spr.setCBez(dto.getIdentifikationsprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	public void updateDsgvokategorie(DsgvokategorieDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		if (dto.getDsgvokategoriesprDto() != null) {

			Dsgvokategoriespr spr = em.find(Dsgvokategoriespr.class,
					new DsgvokategoriesprPK(theClientDto.getLocUiAsString(), iId));
			if (spr == null) {
				try {
					spr = new Dsgvokategoriespr(theClientDto.getLocUiAsString(), iId);

				} catch (EntityExistsException ex7) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, ex7);

				}
			}
			spr.setCBez(dto.getDsgvokategoriesprDto().getCBez());
			em.persist(spr);
			em.flush();
		}

	}

	public void removeDsgvokategorie(DsgvokategorieDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		if (dto.getDsgvokategoriesprDto() != null) {

			Dsgvokategoriespr spr = em.find(Dsgvokategoriespr.class,
					new DsgvokategoriesprPK(theClientDto.getLocUiAsString(), iId));
			if (spr != null) {

				em.remove(spr);
				em.flush();
			}

		}

	}

	public IdentifikationDto identifikationFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {

		Identifikation bean = em.find(Identifikation.class, iId);

		IdentifikationDto dto = IdentifikationDtoAssembler.createDto(bean);

		Identifikationspr spr = em.find(Identifikationspr.class,
				new IdentifikationsprPK(theClientDto.getLocUiAsString(), iId));

		if (spr == null) {
			spr = em.find(Identifikationspr.class, new IdentifikationsprPK(theClientDto.getLocKonzernAsString(), iId));

		}
		IdentifikationsprDto sprDto = null;
		if (spr != null) {
			sprDto = IdentifikationsprDtoAssembler.createDto(spr);
		}
		dto.setIdentifikationsprDto(sprDto);
		return dto;

	}

	private void setIdentifikationFromIdentifikationDto(Identifikation bean, IdentifikationDto dto) {
		bean.setCNr(dto.getCNr());
		em.merge(bean);
		em.flush();
	}

	public void removeBranche(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI == null"));
		}

		try {
			Query query = em.createNamedQuery("BranchesprfindByBrancheIId");
			query.setParameter(1, iIdI);
			Collection<?> allSpr = query.getResultList();
			Iterator<?> iter = allSpr.iterator();
			while (iter.hasNext()) {
				Branchespr sprTemp = (Branchespr) iter.next();
				em.remove(sprTemp);
			}
			Branche branche = em.find(Branche.class, iIdI);
			em.remove(branche);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeBranche(BrancheDto brancheDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (brancheDtoI != null) {
			Integer iId = brancheDtoI.getIId();
			removeBranche(iId, theClientDto);
		}
	}

	public void updateBranche(BrancheDto brancheDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (brancheDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("brancheDtoI == null"));
		}

		Integer iId = brancheDtoI.getIId();
		try {
			Branche branche = em.find(Branche.class, iId);
			setBrancheFromBrancheDto(branche, brancheDtoI);

			if (brancheDtoI.getBranchesprDto() != null) {
				// -- upd oder create
				if (brancheDtoI.getBranchesprDto().getBrancheIId() == null) {
					// create
					// Key(teil) setzen.
					brancheDtoI.getBranchesprDto().setBrancheIId(iId);
					createBranchespr(brancheDtoI.getBranchesprDto(), theClientDto);
				} else {
					// upd
					updateBranchespr(brancheDtoI.getBranchesprDto(), theClientDto);
				}
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public BrancheDto brancheFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI == null"));
		}

		BrancheDto brancheDto = null;
		// try {
		Branche branche = em.find(Branche.class, iIdI);
		if (branche == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		brancheDto = assembleBrancheDto(branche);

		try {
			Branchespr branchespr = em.find(Branchespr.class, new BranchesprPK(iIdI, theClientDto.getLocUiAsString()));
			if (branchespr != null) {
				BranchesprDto branchesprDto = assembleBranchesprDto(branchespr);
				brancheDto.setBranchesprDto(branchesprDto);
			}
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return brancheDto;
	}

	private void setBrancheFromBrancheDto(Branche branche, BrancheDto brancheDtoI) {
		branche.setCNr(brancheDtoI.getCNr());
		em.merge(branche);
		em.flush();
	}

	private BrancheDto assembleBrancheDto(Branche branche) {
		return BrancheDtoAssembler.createDto(branche);
	}

	public BranchesprPK createBranchespr(BranchesprDto branchesprDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Branchespr branchespr = new Branchespr(branchesprDtoI.getBrancheIId(), branchesprDtoI.getLocaleCNr());
			em.persist(branchespr);
			em.flush();
			setBranchesprFromBranchesprDto(branchespr, branchesprDtoI);
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return null;
	}

	public void removeBranchespr(Integer brancheIId, String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		BranchesprPK branchesprPK = new BranchesprPK();
		branchesprPK.setBrancheIId(brancheIId);
		branchesprPK.setLocaleCNr(localeCNrI);
		Branchespr toRemove = em.find(Branchespr.class, branchesprPK);
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

	public void removeBranchespr(BranchesprDto branchesprDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (branchesprDtoI != null) {
			Integer brancheIId = branchesprDtoI.getBrancheIId();
			String localeCNr = branchesprDtoI.getLocaleCNr();
			removeBranchespr(brancheIId, localeCNr, theClientDto);
		}
	}

	public void updateBranchespr(BranchesprDto branchesprDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (branchesprDtoI != null) {
			BranchesprPK branchesprPK = new BranchesprPK();
			branchesprPK.setBrancheIId(branchesprDtoI.getBrancheIId());
			branchesprPK.setLocaleCNr(branchesprDtoI.getLocaleCNr());
			// try {
			Branchespr branchespr = em.find(Branchespr.class, branchesprPK);
			if (branchespr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBranchesprFromBranchesprDto(branchespr, branchesprDtoI);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public BranchesprDto branchesprFindByPrimaryKey(Integer brancheIId, String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// try {
		BranchesprPK branchesprPK = new BranchesprPK();
		branchesprPK.setBrancheIId(brancheIId);
		branchesprPK.setLocaleCNr(localeCNrI);
		Branchespr branchespr = em.find(Branchespr.class, branchesprPK);
		if (branchespr == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBranchesprDto(branchespr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public BranchesprDto[] branchesprFindByBrancheIId(Integer iIdBrancheI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BranchesprfindByBrancheIId");
		query.setParameter(1, iIdBrancheI);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleBranchesprDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setBranchesprFromBranchesprDto(Branchespr branchespr, BranchesprDto branchesprDtoI) {
		branchespr.setCBez(branchesprDtoI.getCBez());
		em.merge(branchespr);
		em.flush();
	}

	private void setKommunikationsartsprFromKommunikationsartsprDto(Kommunikationsartspr kommunikationsartspr,
			KommunikationsartsprDto kommunikationsartsprDtoI) {

		kommunikationsartspr.setCBez(kommunikationsartsprDtoI.getCBez());
		em.merge(kommunikationsartspr);
		em.flush();
	}

	private BranchesprDto assembleBranchesprDto(Branchespr branchespr) {
		return BranchesprDtoAssembler.createDto(branchespr);
	}

	private KommunikationsartsprDto assembleKommunikationsartsprDto(Kommunikationsartspr kommunikationsartspr) {
		return KommunikationsartsprDtoAssembler.createDto(kommunikationsartspr);
	}

	private BranchesprDto[] assembleBranchesprDtos(Collection<?> branchesprs) {
		List<BranchesprDto> list = new ArrayList<BranchesprDto>();
		if (branchesprs != null) {
			Iterator<?> iterator = branchesprs.iterator();
			while (iterator.hasNext()) {
				Branchespr branchespr = (Branchespr) iterator.next();
				list.add(assembleBranchesprDto(branchespr));
			}
		}
		BranchesprDto[] returnArray = new BranchesprDto[list.size()];
		return (BranchesprDto[]) list.toArray(returnArray);
	}

	/**
	 * F&uuml;r einen Beleg muss die Briefanrede localeabh&auml;ngig angezeigt
	 * werden k&ouml;nnen.
	 * 
	 * @param iIdAnsprechpartnerI PK des Ansprechpartners, null erlaubt
	 * @param iIdPartnerI         PK des Partners, not null
	 * @param locBelegI           in diesem Locale soll die Anrede erscheinen, not
	 *                            null
	 * @param theClientDto        der aktuelle Benutzer
	 * @return String die Briefanrede
	 * @throws EJBExceptionLP Ausnahme
	 */
	public String getBriefanredeFuerBeleg(Integer iIdAnsprechpartnerI, Integer iIdPartnerI, Locale locBelegI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdPartnerI == null"));
		}
		if (locBelegI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("locBelegI == null"));
		}

		String sBriefanrede = null;
		try {
			if (iIdAnsprechpartnerI != null) {
				AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(iIdAnsprechpartnerI, theClientDto);
				sBriefanrede = getPartnerFac().formatBriefAnrede(ansprechpartnerDto.getPartnerDto(), locBelegI,
						theClientDto);
			} else if (iIdPartnerI != null) {
				PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
				sBriefanrede = getPartnerFac().formatBriefAnrede(partnerDto, locBelegI, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return sBriefanrede;
	}

	public Integer createSelektion(SelektionDto selektionDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (selektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("selektionDtoI == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERSELEKTION);
		selektionDtoI.setIId(iId);

		Selektion selektion = null;
		try {
			selektion = new Selektion(selektionDtoI.getIId(), selektionDtoI.getCNr(), selektionDtoI.getMandantCNr());
			em.persist(selektion);
			em.flush();
			setSelektionFromSelektionDto(selektion, selektionDtoI);
			if (selektionDtoI.getSelektionsprDto() != null) {
				selektionDtoI.getSelektionsprDto().setSelektionIId(selektionDtoI.getIId());
				Selektionspr selektionspr = new Selektionspr(selektionDtoI.getIId(), theClientDto.getLocUiAsString());
				em.persist(selektionspr);
				em.flush();
				setSelektionsprFromSelektionsprDto(selektionspr, selektionDtoI.getSelektionsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return selektion.getIId();
	}

	public void removeSelektion(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iId == null"));
		}

		try {
			Query query = em.createNamedQuery("SelektionsprfindBySelektionIId");
			query.setParameter(1, iId);
			Collection<?> c = query.getResultList();
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
				Selektionspr item = (Selektionspr) iter.next();
				em.remove(item);
			}
			Selektion selektion = em.find(Selektion.class, iId);
			if (selektion == null) { // @ToDo null Pruefung?
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			em.remove(selektion);
			em.flush();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateSelektion(SelektionDto selektionDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		Validator.notNull(selektionDtoI, "selektionDtoI");

		Integer iId = selektionDtoI.getIId();
		try {
			Selektion selektion = em.find(Selektion.class, iId);
			if (selektion == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			if (selektionDtoI.getSelektionsprDto() != null) {
				// -- upd oder create
				if (selektionDtoI.getSelektionsprDto().getSelektionIId() == null) {
					// create
					// Key(teil) setzen.
					selektionDtoI.getSelektionsprDto().setSelektionIId(selektionDtoI.getIId());

					Selektionspr selektionspr = new Selektionspr(selektionDtoI.getIId(),
							theClientDto.getLocUiAsString());
					em.persist(selektionspr);
					em.flush();

					setSelektionsprFromSelektionsprDto(selektionspr, selektionDtoI.getSelektionsprDto());
				} else {
					// upd
					Selektionspr selektionspr = em.find(Selektionspr.class,
							new SelektionsprPK(selektionDtoI.getIId(), theClientDto.getLocUiAsString()));

					setSelektionsprFromSelektionsprDto(selektionspr, selektionDtoI.getSelektionsprDto());
				}
			}
			setSelektionFromSelektionDto(selektion, selektionDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public SelektionDto selektionFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		SelektionDto selektionDto = null;
		// try {
		Selektion selektion = em.find(Selektion.class, iId);
		if (selektion == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		selektionDto = assembleSelektionDto(selektion);

		try {
			Selektionspr selektionspr = em.find(Selektionspr.class,
					new SelektionsprPK(iId, theClientDto.getLocUiAsString()));
			selektionDto.setSelektionsprDto(assembleSelektionsprDto(selektionspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return selektionDto;
	}

	public SelektionDto selektionFindByCNrMandantCNr(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		SelektionDto selektionDto = null;

		try {

			Query query = em.createNamedQuery("SelektionfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, theClientDto.getMandant());

			Selektion selektion = (Selektion) query.getSingleResult();

			selektionDto = assembleSelektionDto(selektion);

		} catch (NoResultException ex) {
			// nothing here
		}

		return selektionDto;
	}

	private void setSelektionFromSelektionDto(Selektion selektion, SelektionDto selektionDto) {
		selektion.setCNr(selektionDto.getCNr());
		selektion.setMandantCNr(selektionDto.getMandantCNr());
		selektion.setbWebshop(selektionDto.getbWebshop());

		em.merge(selektion);
		em.flush();
	}

	private SelektionDto assembleSelektionDto(Selektion selektion) {
		return SelektionDtoAssembler.createDto(selektion);
	}

	public Integer createSelektionspr(SelektionsprDto selektionsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			Selektionspr selektionspr = new Selektionspr(selektionsprDto.getSelektionIId(),
					selektionsprDto.getLocaleCNr());
			em.persist(selektionspr);
			em.flush();
			setSelektionsprFromSelektionsprDto(selektionspr, selektionsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
		return null;
	}

	public void removeSelektionspr(Integer selektionIId, String localeCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		SelektionsprPK selektionPK = new SelektionsprPK();
		selektionPK.setSelektionIId(selektionIId);
		selektionPK.setLocaleCNr(localeCNrI);
		Selektionspr toRemove = em.find(Selektionspr.class, selektionPK);
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

	public void updateSelektionspr(SelektionsprDto selektionsprDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (selektionsprDto != null) {
			SelektionsprPK selektionsprPK = new SelektionsprPK();
			selektionsprPK.setSelektionIId(selektionsprDto.getSelektionIId());
			selektionsprPK.setLocaleCNr(selektionsprDto.getLocaleCNr());
			try {
				Selektionspr selektionspr = em.find(Selektionspr.class, selektionsprPK);
				setSelektionsprFromSelektionsprDto(selektionspr, selektionsprDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(e);
			}
		}
	}

	public SelektionsprDto selektionsprFindByPrimaryKey(Integer selektionIId, String localeCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			SelektionsprPK selektionsprPK = new SelektionsprPK();
			selektionsprPK.setSelektionIId(selektionIId);
			selektionsprPK.setLocaleCNr(localeCNr);
			Selektionspr selektionspr = em.find(Selektionspr.class, selektionsprPK);
			if (selektionspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleSelektionsprDto(selektionspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}

	private void setSelektionsprFromSelektionsprDto(Selektionspr selektionspr, SelektionsprDto selektionsprDto) {
		selektionspr.setCBez(selektionsprDto.getCBez());
		em.merge(selektionspr);
		em.flush();
	}

	private SelektionsprDto assembleSelektionsprDto(Selektionspr selektionspr) {
		return SelektionsprDtoAssembler.createDto(selektionspr);
	}

	public void updateSerienbriefMailtext(SerienbriefDto serienbriefDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Serienbrief stueckliste = em.find(Serienbrief.class, serienbriefDto.getIId());
		stueckliste.setXMailtext(serienbriefDto.getXMailtext());
	}

	public Integer createSerienbrief(SerienbriefDto serienbriefDto, TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (serienbriefDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("serienbriefDto == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERSERIENBRIEF);
		serienbriefDto.setIId(iId);

		// befuelle Felder am Server.
		serienbriefDto.setMandantCNr(theClientDto.getMandant());
		serienbriefDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		serienbriefDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		// die ts anlegen, aendern nur am server
		serienbriefDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
		serienbriefDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		Serienbrief serienbrief = null;
		try {
			serienbrief = new Serienbrief(serienbriefDto.getIId(), serienbriefDto.getCBez(),
					serienbriefDto.getMandantCNr(), serienbriefDto.getPersonalIIdAnlegen(),
					serienbriefDto.getPersonalIIdAendern(), serienbriefDto.getBGehtAnKunden(),
					serienbriefDto.getBGehtAnInteressenten(), serienbriefDto.getBVersteckteDabei(),
					serienbriefDto.getBAnsprechpartnerfunktionAuchOhne(), serienbriefDto.getBGehtanlieferanten(),
					serienbriefDto.getBGehtanmoeglichelieferanten(), serienbriefDto.getBGehtanpartner(),
					serienbriefDto.getBMitzugeordnetenfirmen(), Helper.boolean2Short(serienbriefDto.isNewsletter()),
					serienbriefDto.getBSelektionenLogischesOder(),
					serienbriefDto.getBWennkeinanspmitfktDannersteransp(), serienbriefDto.getBHtml(),
					serienbriefDto.getLocaleCNr());
			em.persist(serienbrief);
			em.flush();
			setSerienbriefFromSerienbriefDto(serienbrief, serienbriefDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return serienbrief.getIId();
	}

	public NewslettergrundDto newslettergrundFindByCBez(String cBez) {
		try {
			Query query = em.createNamedQuery("NewslettergrundfindByCBez");
			query.setParameter(1, cBez);
			Newslettergrund n = (Newslettergrund) query.getSingleResult();
			if (n != null) {
				return NewslettergrundDtoAssembler.createDto(n);
			}
		} catch (NoResultException ex) {
			//
		}
		return null;
	}

	public Integer createNewslettergrund(NewslettergrundDto dto, TheClientDto theClientDto) {

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer iId = pkGen.getNextPrimaryKey(PKConst.PK_NEWSLETTERGRUND);
		dto.setIId(iId);

		try {
			Query query = em.createNamedQuery("NewslettergrundfindByCBez");
			query.setParameter(1, dto.getCBez());
			Newslettergrund doppelt = (Newslettergrund) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_NEWSLETTERGRUND.CBEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		Newslettergrund newslettergrund = null;
		try {
			newslettergrund = new Newslettergrund(dto.getIId(), dto.getCBez(), dto.getBAngemeldet());
			em.persist(newslettergrund);
			em.flush();
			setNewslettergrundFromNewslettergrundDto(newslettergrund, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		HvDtoLogger<NewslettergrundDto> newslettergrundLogger = new HvDtoLogger<NewslettergrundDto>(em, theClientDto);
		newslettergrundLogger.logInsert(dto);

		return dto.getIId();
	}

	public void removeNewslettergrund(Integer iIdI, TheClientDto theClientDto) {

		Newslettergrund toRemove = em.find(Newslettergrund.class, iIdI);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		NewslettergrundDto newslettergrundDto = NewslettergrundDtoAssembler.createDto(toRemove);

		HvDtoLogger<NewslettergrundDto> newslettergrundLogger = new HvDtoLogger<NewslettergrundDto>(em, theClientDto);
		newslettergrundLogger.logDelete(newslettergrundDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeSerienbrief(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		Serienbrief toRemove = em.find(Serienbrief.class, iIdI);
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

	public void updateSerienbrief(SerienbriefDto serienbriefDtoI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (serienbriefDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefDtoI == null"));
		}

		Integer iId = serienbriefDtoI.getIId();
		// try {
		Serienbrief serienbrief = em.find(Serienbrief.class, iId);
		if (serienbrief == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSerienbriefFromSerienbriefDto(serienbrief, serienbriefDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void updateNewslettergrund(NewslettergrundDto dtoI, TheClientDto theClientDto) {

		if (dtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("dtoI == null"));
		}

		Integer iId = dtoI.getIId();

		Newslettergrund newslettergrund = em.find(Newslettergrund.class, iId);
		if (newslettergrund == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		NewslettergrundDto dto_Vorher = newslettergrundFindByPrimaryKey(dtoI.getIId(), theClientDto);

		HvDtoLogger<NewslettergrundDto> artikelLogger = new HvDtoLogger<NewslettergrundDto>(em, theClientDto);
		artikelLogger.log(dto_Vorher, dtoI);

		try {
			Query query = em.createNamedQuery("NewslettergrundfindByCBez");
			query.setParameter(1, dtoI.getCBez());
			Integer iIdVorhanden = ((Newslettergrund) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_NEWSLETTERGRUND.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		setNewslettergrundFromNewslettergrundDto(newslettergrund, dtoI);

	}

	public SerienbriefDto serienbriefFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		// try {
		Serienbrief serienbrief = em.find(Serienbrief.class, iIdI);
		if (serienbrief == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSerienbriefDto(serienbrief);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public NewslettergrundDto newslettergrundFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdI == null"));
		}

		// try {
		Newslettergrund newslettergrund = em.find(Newslettergrund.class, iIdI);
		if (newslettergrund == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return NewslettergrundDtoAssembler.createDto(newslettergrund);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setSerienbriefFromSerienbriefDto(Serienbrief serienbrief, SerienbriefDto serienbriefDto) {
		serienbrief.setCBez(serienbriefDto.getCBez());
		serienbrief.setCPlz(serienbriefDto.getCPlz());
		serienbrief.setMandantCNr(serienbriefDto.getMandantCNr());
		serienbrief.setTAnlegen(serienbriefDto.getTAnlegen());
		serienbrief.setPersonalIIdAnlegen(serienbriefDto.getPersonalIIdAnlegen());
		serienbrief.setTAendern(serienbriefDto.getTAendern());
		serienbrief.setPersonalIIdAendern(serienbriefDto.getPersonalIIdAendern());
		serienbrief.setBGehtankunden(serienbriefDto.getBGehtAnKunden());
		serienbrief.setBGehtaninteressenten(serienbriefDto.getBGehtAnInteressenten());
		serienbrief.setBGehtanlieferanten(serienbriefDto.getBGehtanlieferanten());
		serienbrief.setBGehtanpartner(serienbriefDto.getBGehtanpartner());
		serienbrief.setBGehtanmoeglichelieferanten(serienbriefDto.getBGehtanmoeglichelieferanten());
		serienbrief.setBVerstecktedabei(serienbriefDto.getBVersteckteDabei());
		serienbrief.setAnsprechpartnerfunktionIId(serienbriefDto.getAnsprechpartnerfunktionIId());
		serienbrief.setBAnsprechpartnerfunktionauchohne(serienbriefDto.getBAnsprechpartnerfunktionAuchOhne());
		serienbrief.setCBetreff(serienbriefDto.getSBetreff());
		serienbrief.setXText(serienbriefDto.getSXText());
		serienbrief.setLandIId(serienbriefDto.getLandIId());
		serienbrief.setNAbumsatz(serienbriefDto.getNAbumsatz());
		serienbrief.setNBisumsatz(serienbriefDto.getNBisumsatz());
		serienbrief.setTUmsatzab(serienbriefDto.getTUmsatzab());
		serienbrief.setTUmsatzbis(serienbriefDto.getTUmsatzbis());
		serienbrief.setBMitzugeordnetenfirmen(serienbriefDto.getBMitzugeordnetenfirmen());
		serienbrief.setBrancheIId(serienbriefDto.getBrancheIId());
		serienbrief.setPartnerklasseIId(serienbriefDto.getPartnerklasseIId());
		serienbrief.setXMailtext(serienbriefDto.getXMailtext());
		serienbrief.setbNewsletter(Helper.boolean2Short(serienbriefDto.isNewsletter()));
		serienbrief.setBSelektionenLogischesOder(serienbriefDto.getBSelektionenLogischesOder());
		serienbrief.setBWennkeinanspmitfktDannersteransp(serienbriefDto.getBWennkeinanspmitfktDannersteransp());
		serienbrief.setBHtml(serienbriefDto.getBHtml());
		serienbrief.setLocaleCNr(serienbriefDto.getLocaleCNr());
		em.merge(serienbrief);
		em.flush();
	}

	private void setNewslettergrundFromNewslettergrundDto(Newslettergrund bean, NewslettergrundDto dto) {
		bean.setCBez(dto.getCBez());
		bean.setBAngemeldet(dto.getBAngemeldet());

		em.merge(bean);
		em.flush();
	}

	private SerienbriefDto assembleSerienbriefDto(Serienbrief serienbrief) {
		return SerienbriefDtoAssembler.createDto(serienbrief);
	}

	public SerienbriefselektionPK createSerienbriefselektion(SerienbriefselektionDto serienbriefselektionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (serienbriefselektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("serienbriefselektionDtoI == null"));
		}

		Serienbriefselektion serienbriefselektion = null;
		try {
			serienbriefselektion = new Serienbriefselektion(serienbriefselektionDtoI.getSerienbriefIId(),
					serienbriefselektionDtoI.getSelektionIId());
			serienbriefselektion.setCBemerkung("");
			em.persist(serienbriefselektion);

			setSerienbriefselektionFromSerienbriefselektionDto(serienbriefselektion, serienbriefselektionDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new SerienbriefselektionPK(serienbriefselektion.getPk().getSerienbriefIId(),
				serienbriefselektion.getPk().getSelektionIId());
	}

	public SerienbriefselektionnegativPK createSerienbriefselektionnegativ(
			SerienbriefselektionnegativDto serienbriefselektionnegativDtoI, TheClientDto theClientDto) {

		// precondition
		if (serienbriefselektionnegativDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("serienbriefselektionDtoI == null"));
		}

		Serienbriefselektionnegativ serienbriefselektionnegativ = null;
		try {
			serienbriefselektionnegativ = new Serienbriefselektionnegativ(
					serienbriefselektionnegativDtoI.getSerienbriefIId(),
					serienbriefselektionnegativDtoI.getSelektionIId());
			em.persist(serienbriefselektionnegativ);
			em.flush();
			setSerienbriefselektionnegativFromSerienbriefselektionnegativDto(serienbriefselektionnegativ,
					serienbriefselektionnegativDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return new SerienbriefselektionnegativPK(serienbriefselektionnegativ.getPk().getSerienbriefIId(),
				serienbriefselektionnegativ.getPk().getSelektionIId());
	}

	public void removeSerienbriefselektion(Integer serienbriefIIdI, Integer selektionIIdI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("selektionIIdI == null"));
		}

		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionPK.setSelektionIId(selektionIIdI);
		Serienbriefselektion toRemove = em.find(Serienbriefselektion.class, serienbriefselektionPK);
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

	public void removeSerienbriefselektionnegativ(Integer serienbriefIIdI, Integer selektionIIdI,
			TheClientDto theClientDto) {

		// precondition
		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("selektionIIdI == null"));
		}

		SerienbriefselektionnegativPK serienbriefselektionnegativPK = new SerienbriefselektionnegativPK();
		serienbriefselektionnegativPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionnegativPK.setSelektionIId(selektionIIdI);
		Serienbriefselektionnegativ toRemove = em.find(Serienbriefselektionnegativ.class,
				serienbriefselektionnegativPK);
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

	public void updateSerienbriefselektionnegativ(SerienbriefselektionnegativDto serienbriefselektionnegativDtoI,
			TheClientDto theClientDto) {
		// precondition
		if (serienbriefselektionnegativDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefselektionDtoI == null"));
		}

		SerienbriefselektionnegativPK serienbriefselektionnegativPK = new SerienbriefselektionnegativPK();
		serienbriefselektionnegativPK.setSerienbriefIId(serienbriefselektionnegativDtoI.getSerienbriefIId());
		serienbriefselektionnegativPK.setSelektionIId(serienbriefselektionnegativDtoI.getSelektionIId());

		Serienbriefselektionnegativ serienbriefselektionnegativ = em.find(Serienbriefselektionnegativ.class,
				serienbriefselektionnegativPK);
		if (serienbriefselektionnegativ == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSerienbriefselektionnegativFromSerienbriefselektionnegativDto(serienbriefselektionnegativ,
				serienbriefselektionnegativDtoI);

	}

	public void updateSerienbriefselektion(SerienbriefselektionDto serienbriefselektionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// precondition
		if (serienbriefselektionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefselektionDtoI == null"));
		}

		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefselektionDtoI.getSerienbriefIId());
		serienbriefselektionPK.setSelektionIId(serienbriefselektionDtoI.getSelektionIId());
		// try {
		Serienbriefselektion serienbriefselektion = em.find(Serienbriefselektion.class, serienbriefselektionPK);
		if (serienbriefselektion == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setSerienbriefselektionFromSerienbriefselektionDto(serienbriefselektion, serienbriefselektionDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public SerienbriefselektionDto[] serienbriefselektionFindBySerienbriefIId(Integer serienbriefIId) {
		if (serienbriefIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("SerienbriefselektionfindBySerienbriefIId");
		query.setParameter(1, serienbriefIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleSerienbriefselektionDtos(cl);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public SerienbriefselektionnegativDto[] serienbriefselektionnegativFindBySerienbriefIId(Integer serienbriefIId) {
		if (serienbriefIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("SerienbriefselektionnegativfindBySerienbriefIId");
		query.setParameter(1, serienbriefIId);
		Collection<?> cl = query.getResultList();

		return SerienbriefselektionnegativDtoAssembler.createDtos(cl);

	}

	public SerienbriefselektionDto serienbriefselektionFindByPrimaryKey(Integer serienbriefIIdI, Integer selektionIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition

		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("selektionIIdI == null"));
		}

		// try {
		SerienbriefselektionPK serienbriefselektionPK = new SerienbriefselektionPK();
		serienbriefselektionPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionPK.setSelektionIId(selektionIIdI);
		Serienbriefselektion serienbriefselektion = em.find(Serienbriefselektion.class, serienbriefselektionPK);
		if (serienbriefselektion == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleSerienbriefselektionDto(serienbriefselektion);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public SerienbriefselektionnegativDto serienbriefselektionnegativFindByPrimaryKey(Integer serienbriefIIdI,
			Integer selektionIIdI, TheClientDto theClientDto) {

		// precondition

		if (serienbriefIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("serienbriefIIdI == null"));
		}
		if (selektionIIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("selektionIIdI == null"));
		}

		// try {
		SerienbriefselektionnegativPK serienbriefselektionnegativPK = new SerienbriefselektionnegativPK();
		serienbriefselektionnegativPK.setSerienbriefIId(serienbriefIIdI);
		serienbriefselektionnegativPK.setSelektionIId(selektionIIdI);
		Serienbriefselektionnegativ serienbriefselektionnegativ = em.find(Serienbriefselektionnegativ.class,
				serienbriefselektionnegativPK);
		if (serienbriefselektionnegativ == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return SerienbriefselektionnegativDtoAssembler.createDto(serienbriefselektionnegativ);
	}

	private void setSerienbriefselektionFromSerienbriefselektionDto(Serienbriefselektion serienbriefselektion,
			SerienbriefselektionDto serienbriefselektionDto) {
		serienbriefselektion.setCBemerkung(serienbriefselektionDto.getSBemerkung());
		em.merge(serienbriefselektion);
		em.flush();
	}

	private void setSerienbriefselektionnegativFromSerienbriefselektionnegativDto(
			Serienbriefselektionnegativ serienbriefselektionnegativ,
			SerienbriefselektionnegativDto serienbriefselektionnegativDto) {
		serienbriefselektionnegativ.setCBemerkung(serienbriefselektionnegativDto.getSBemerkung());
		em.merge(serienbriefselektionnegativ);
		em.flush();
	}

	private SerienbriefselektionDto assembleSerienbriefselektionDto(Serienbriefselektion serienbriefselektion) {
		return SerienbriefselektionDtoAssembler.createDto(serienbriefselektion);
	}

	private SerienbriefselektionDto[] assembleSerienbriefselektionDtos(Collection<?> branchesprs) {
		List<SerienbriefselektionDto> list = new ArrayList<SerienbriefselektionDto>();
		if (branchesprs != null) {
			Iterator<?> iterator = branchesprs.iterator();
			while (iterator.hasNext()) {
				Serienbriefselektion serienbriefselektion = (Serienbriefselektion) iterator.next();
				list.add(assembleSerienbriefselektionDto(serienbriefselektion));
			}
		}
		SerienbriefselektionDto[] returnArray = new SerienbriefselektionDto[list.size()];
		return (SerienbriefselektionDto[]) list.toArray(returnArray);
	}

	private PartnerkommentarartDto assemblePartnerkommentarartDto(Partnerkommentarart partnerkommentarart) {
		return PartnerkommentarartDtoAssembler.createDto(partnerkommentarart);
	}

	public PartnerkommentarartDto partnerkommentarartFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Partnerkommentarart art = em.find(Partnerkommentarart.class, iId);
		PartnerkommentarartDto artDto = assemblePartnerkommentarartDto(art);
		return artDto;

	}

	public Integer createPartnerkommentarart(PartnerkommentarartDto artDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("PartnerkommentarartfindByCBez");
			query.setParameter(1, artDto.getCBez());
			Partnerkommentarart doppelt = (Partnerkommentarart) query.getSingleResult();
			// if (doppelt != null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PART_PARTNERKOMMENTARART.C_BEZ"));
		} catch (NoResultException ex) {
			//
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERKOMMENTARART);
			artDto.setIId(pk);

			Partnerkommentarart partnerkommentarart = new Partnerkommentarart(artDto.getIId(), artDto.getCBez());
			em.persist(partnerkommentarart);
			em.flush();
			setPartnerkommentarartFromPartnerkommentarartDto(partnerkommentarart, artDto);

			return artDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setPartnerkommentarartFromPartnerkommentarartDto(Partnerkommentarart art,
			PartnerkommentarartDto artDto) {
		art.setCBez(artDto.getCBez());
		em.merge(art);
		em.flush();
	}

	public void removePartnerkommentarart(PartnerkommentarartDto artDto) {
		Partnerkommentarart artikelkommentarart = em.find(Partnerkommentarart.class, artDto.getIId());
		em.remove(artikelkommentarart);
		em.flush();

	}

	public void updatePartnerkommentarart(PartnerkommentarartDto artDto, TheClientDto theClientDto) {

		Integer iId = artDto.getIId();

		Partnerkommentarart partnerkommentarart = em.find(Partnerkommentarart.class, iId);

		try {
			Query query = em.createNamedQuery("PartnerkommentarartfindByCBez");
			query.setParameter(1, artDto.getCBez());
			Integer iIdVorhanden = ((Partnerkommentarart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_PARTNERKOMMENTARART.C_BEZ"));
			}
		} catch (NoResultException ex) {
			//
		}

		setPartnerkommentarartFromPartnerkommentarartDto(partnerkommentarart, artDto);

	}

	public Integer createPartnerkommentar(PartnerkommentarDto partnerkommentarDto, TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("PartnerkommentarfindByPartnerIIdPartnerkommentarartIIdDatenformatCNrBKunde");
			query.setParameter(1, partnerkommentarDto.getPartnerIId());
			query.setParameter(2, partnerkommentarDto.getPartnerkommentarartIId());
			query.setParameter(3, partnerkommentarDto.getDatenformatCNr());
			query.setParameter(4, partnerkommentarDto.getBKunde());
			Partnerkommentar doppelt = (Partnerkommentar) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PART_PARTNERKOMMENTAR.UC"));
		} catch (NoResultException ex) {
			//
		}
		try {

			partnerkommentarDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			partnerkommentarDto.setTAendern(new Timestamp(System.currentTimeMillis()));

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERKOMMENTAR);
			partnerkommentarDto.setIId(pk);

			Query queryNext = em.createNamedQuery("PartnerkommentarejbSelectNextReihungByBKunde");
			queryNext.setParameter(1, partnerkommentarDto.getPartnerIId());
			queryNext.setParameter(2, partnerkommentarDto.getBKunde());

			Integer iSort = (Integer) queryNext.getSingleResult();
			if (iSort == null) {
				iSort = 0;
			}

			iSort = new Integer(iSort + 1);

			partnerkommentarDto.setISort(iSort);

			Partnerkommentar partnerkommentar = new Partnerkommentar(partnerkommentarDto.getIId(),
					partnerkommentarDto.getPartnerIId(), partnerkommentarDto.getPartnerkommentarartIId(),
					partnerkommentarDto.getDatenformatCNr(), partnerkommentarDto.getBKunde(),
					partnerkommentarDto.getIArt(), partnerkommentarDto.getISort(),
					partnerkommentarDto.getPersonalIIdAendern(), partnerkommentarDto.getTAendern());
			em.persist(partnerkommentar);
			em.flush();
			setPartnerkommentarFromPartnerkommentarDto(partnerkommentar, partnerkommentarDto);

			if (partnerkommentarDto.getPartnerkommentardruckDto() != null) {
				for (int i = 0; i < partnerkommentarDto.getPartnerkommentardruckDto().length; i++) {
					PartnerkommentardruckDto dto = partnerkommentarDto.getPartnerkommentardruckDto()[i];

					Integer pkDruck = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERKOMMENTARDRUCK);

					Partnerkommentardruck pd = new Partnerkommentardruck(pkDruck, partnerkommentarDto.getIId(),
							dto.getBelegartCNr());

					em.persist(pd);
					em.flush();
				}
			}

			return partnerkommentarDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updatePartnerkommentar(PartnerkommentarDto partnerkommentarDto, TheClientDto theClientDto) {

		Integer iId = partnerkommentarDto.getIId();

		Partnerkommentar partnerkommentar = null;
		// try {
		partnerkommentar = em.find(Partnerkommentar.class, iId);

		Query query = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIId");
		query.setParameter(1, partnerkommentarDto.getIId());
		Collection<?> allDruck = query.getResultList();
		Iterator<?> iterAllDruck = allDruck.iterator();
		try {
			while (iterAllDruck.hasNext()) {
				Partnerkommentardruck temp = (Partnerkommentardruck) iterAllDruck.next();
				em.remove(temp);
			}
			em.flush();
		} catch (EntityExistsException ex2) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex2);
		}
		if (partnerkommentarDto.getPartnerkommentardruckDto() != null) {

			for (int i = 0; i < partnerkommentarDto.getPartnerkommentardruckDto().length; i++) {
				PartnerkommentardruckDto dto = partnerkommentarDto.getPartnerkommentardruckDto()[i];
				PKGeneratorObj pkGen = new PKGeneratorObj();
				Integer pkDruck = pkGen.getNextPrimaryKey(PKConst.PK_PARTNERKOMMENTARDRUCK);

				Partnerkommentardruck pd = new Partnerkommentardruck(pkDruck, partnerkommentarDto.getIId(),
						dto.getBelegartCNr());

				em.persist(pd);
				em.flush();
			}

		}

		try {
			query = em.createNamedQuery("PartnerkommentarfindByPartnerIIdPartnerkommentarartIIdDatenformatCNrBKunde");
			query.setParameter(1, partnerkommentarDto.getPartnerIId());
			query.setParameter(2, partnerkommentarDto.getPartnerkommentarartIId());
			query.setParameter(3, partnerkommentarDto.getDatenformatCNr());
			query.setParameter(4, partnerkommentarDto.getBKunde());
			Integer iIdVorhanden = ((Partnerkommentar) query.getSingleResult()).getIId();

			if (iId.equals(iIdVorhanden) == false) {

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PART_PARTNERKOMMENTAR.UC"));
			}
		} catch (NoResultException ex) {
			//
		}
		partnerkommentarDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		partnerkommentarDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		setPartnerkommentarFromPartnerkommentarDto(partnerkommentar, partnerkommentarDto);

	}

	public PartnerkommentarDto partnerkommentarFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {

		Partnerkommentar partnerkommentar = em.find(Partnerkommentar.class, iId);

		PartnerkommentarDto partnerkommentarDto = assemblePartnerkommentarDto(partnerkommentar);

		Query query = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIId");
		query.setParameter(1, partnerkommentarDto.getIId());
		Collection<?> cl = query.getResultList();
		partnerkommentarDto.setPartnerkommentardruckDto(PartnerkommentardruckDtoAssembler.createDtos(cl));

		return partnerkommentarDto;

	}

	public byte[] partnerbildFindByPartnerIId(Integer partnerIId) {

		Partnerbild bild = em.find(Partnerbild.class, partnerIId);

		if (bild != null) {
			return bild.getOBild();
		} else {
			return null;
		}

	}

	public void updatePartnerbild(Integer partnerIId, byte[] bild) {

		Partnerbild partnerbild = em.find(Partnerbild.class, partnerIId);

		if (bild != null) {
			if (partnerbild == null) {
				partnerbild = new Partnerbild(partnerIId, bild);
			} else {
				partnerbild.setOBild(bild);
			}
			em.merge(partnerbild);
		} else {
			if (partnerbild != null) {
				em.remove(partnerbild);
			}
		}

	}

	public void removePartnerkommentar(PartnerkommentarDto partnerkommentarDto) {

		try {

			Query query = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIId");
			query.setParameter(1, partnerkommentarDto.getIId());
			Collection<?> allDruck = query.getResultList();
			Iterator<?> iterAllDruck = allDruck.iterator();
			while (iterAllDruck.hasNext()) {
				Partnerkommentardruck artklasprTemp = (Partnerkommentardruck) iterAllDruck.next();
				em.remove(artklasprTemp);
			}

			Partnerkommentar partnerkommentar = em.find(Partnerkommentar.class, partnerkommentarDto.getIId());

			em.remove(partnerkommentar);
			em.flush();

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

	}

	private void setPartnerkommentarFromPartnerkommentarDto(Partnerkommentar partnerkommentar,
			PartnerkommentarDto partnerkommentarDto) {
		partnerkommentar.setPartnerIId(partnerkommentarDto.getPartnerIId());
		partnerkommentar.setPartnerkommentarartIId(partnerkommentarDto.getPartnerkommentarartIId());
		partnerkommentar.setBKunde(partnerkommentarDto.getBKunde());
		partnerkommentar.setIArt(partnerkommentarDto.getIArt());
		partnerkommentar.setDatenformatCNr(partnerkommentarDto.getDatenformatCNr());
		partnerkommentar.setISort(partnerkommentarDto.getISort());
		partnerkommentar.setXKommentar(partnerkommentarDto.getXKommentar());
		partnerkommentar.setOMedia(partnerkommentarDto.getOMedia());
		partnerkommentar.setCDateiname(partnerkommentarDto.getCDateiname());
		partnerkommentar.setTFiledatum(partnerkommentarDto.getTFiledatum());
		partnerkommentar.setTAendern(partnerkommentarDto.getTAendern());
		partnerkommentar.setPersonalIIdAendern(partnerkommentarDto.getPersonalIIdAendern());
		em.merge(partnerkommentar);
		em.flush();
	}

	public ArrayList<byte[]> getPartnerkommentarBilderUndPDFAlsBilderUmgewandelt(Integer partnerIId, boolean bKunde,
			String belegartCNr, Integer iArt, TheClientDto theClientDto) {

		ArrayList<byte[]> al = new ArrayList<byte[]>();

		// try {
		Query query = em.createNamedQuery("PartnerkommentarfindByPartnerIIdBKunde");
		query.setParameter(1, partnerIId);
		query.setParameter(2, Helper.boolean2Short(bKunde));
		Collection<?> cl = query.getResultList();

		PartnerkommentarDto[] dtos = PartnerkommentarDtoAssembler.createDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				if (dtos[i].getIArt().equals(iArt)) {

					PartnerkommentarDto dto = dtos[i];

					Query query1 = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIIdBelegartCNr");
					query1.setParameter(1, dtos[i].getIId());
					query1.setParameter(2, belegartCNr);

					try {

						Partnerkommentardruck partnerkommentardruck = (Partnerkommentardruck) query1.getSingleResult();
						if (partnerkommentardruck != null) {
							if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
									|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
									|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
									|| dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
								if (dto.getOMedia() != null) {
									al.add(dtos[i].getOMedia());

								}
							} else if (dto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
								if (dto.getOMedia() != null) {
									byte[][] pdfseiten = getSystemFac()
											.konvertierePDFFileInEinzelneBilder(dto.getOMedia(), 72);

									for (int j = 0; j < pdfseiten.length; j++) {
										al.add(pdfseiten[j]);
									}
								}

							}
						}

					} catch (NoResultException e) {
						// nothing here
					}
				}
			}

		}

		return al;

	}

	public ArrayList<PartnerkommentarDto> getPartnerkommentarAnhaenge(Integer partnerIId, boolean bKunde,
			String belegartCNr, TheClientDto theClientDto) {

		ArrayList<PartnerkommentarDto> al = new ArrayList<PartnerkommentarDto>();

		// try {
		Query query = em.createNamedQuery("PartnerkommentarfindByPartnerIIdBKunde");
		query.setParameter(1, partnerIId);
		query.setParameter(2, Helper.boolean2Short(bKunde));
		Collection<?> cl = query.getResultList();

		PartnerkommentarDto[] dtos = PartnerkommentarDtoAssembler.createDtos(cl);

		if (dtos != null && dtos.length > 0) {

			for (int i = 0; i < dtos.length; i++) {

				if (dtos[i].getIArt().equals(PartnerServicesFac.PARTNERKOMMENTARART_ANHANG)) {

					PartnerkommentarDto dto = dtos[i];

					Query query1 = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIIdBelegartCNr");
					query1.setParameter(1, dtos[i].getIId());
					query1.setParameter(2, belegartCNr);

					try {

						Partnerkommentardruck partnerkommentardruck = (Partnerkommentardruck) query1.getSingleResult();
						if (partnerkommentardruck != null && dto.getOMedia() != null) {
							if (dto.getOMedia() != null) {
								al.add(dto);
							}
						}
					} catch (NoResultException e) {
						// nothing here
					}
				}
			}

		}

		return al;

	}

	public ArrayList<PartnerkommentarDto> getPartnerhinweise(Integer partnerIId, boolean bKunde, String belegartCNr,
			TheClientDto theClientDto) {
		ArrayList<PartnerkommentarDto> al = new ArrayList<PartnerkommentarDto>();
		try {
			Query query = em.createNamedQuery("PartnerkommentarfindByPartnerIIdBKunde");
			query.setParameter(1, partnerIId);
			query.setParameter(2, Helper.boolean2Short(bKunde));

			Collection<?> cl = query.getResultList();
			PartnerkommentarDto[] dtos = PartnerkommentarDtoAssembler.createDtos(cl);

			for (int i = 0; i < dtos.length; i++) {
				if (dtos[i].getIArt() == PARTNERKOMMENTARART_HINWEIS) {

					if (dtos[i].getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {

						Query query1 = em.createNamedQuery("PartnerkommentardruckfindByPartnerkommentarIIdBelegartCNr");
						query1.setParameter(1, dtos[i].getIId());
						query1.setParameter(2, belegartCNr);

						try {

							Partnerkommentardruck partnerkommentardruck = (Partnerkommentardruck) query1
									.getSingleResult();
							if (partnerkommentardruck != null) {

								al.add(dtos[i]);

							}
						} catch (NoResultException e) {
							// nothing here
						}

					}

				}
			}
		} catch (NoResultException e) {
			// nothing here
		}

		return al;
	}

	public DsgvokategorieDto dsgvokategorieFindByPrimaryKey(Integer iIdI, TheClientDto theClientDto) {

		if (iIdI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iIdI == null"));
		}

		// try {
		Dsgvokategorie bean = em.find(Dsgvokategorie.class, iIdI);
		if (bean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		DsgvokategorieDto dto = DsgvokategorieDtoAssembler.createDto(bean);

		try {
			Dsgvokategoriespr spr = em.find(Dsgvokategoriespr.class,
					new DsgvokategoriesprPK(theClientDto.getLocUiAsString(), iIdI));
			dto.setDsgvokategoriesprDto(DsgvokategoriesprDtoAssembler.createDto(spr));
		} catch (Throwable t) {
			// nothing here.
		}

		return dto;

	}

	public void vertauscheDsgvotext(Integer id1, Integer id2) {
		// try {
		Dsgvotext dsgvotext1 = em.find(Dsgvotext.class, id1);
		if (dsgvotext1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Dsgvotext dsgvotext2 = em.find(Dsgvotext.class, id2);
		if (dsgvotext2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = dsgvotext1.getISort();
		Integer iSort2 = dsgvotext2.getISort();

		dsgvotext2.setISort(new Integer(-1));

		dsgvotext1.setISort(iSort2);
		dsgvotext2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

}
