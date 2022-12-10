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
package com.lp.server.angebotstkl.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
// import org.hibernate.util.StringHelper;
import org.modelmapper.ModelMapper;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebot;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.ejb.Agstkl;
import com.lp.server.angebotstkl.ejb.Agstklarbeitsplan;
import com.lp.server.angebotstkl.ejb.Agstklaufschlag;
import com.lp.server.angebotstkl.ejb.Agstklmengenstaffel;
import com.lp.server.angebotstkl.ejb.AgstklmengenstaffelSchnellerfassung;
import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.ejb.Agstklpositionsart;
import com.lp.server.angebotstkl.ejb.Aufschlag;
import com.lp.server.angebotstkl.ejb.Einkaufsangebot;
import com.lp.server.angebotstkl.ejb.Einkaufsangebotposition;
import com.lp.server.angebotstkl.ejb.EkWeblieferantQuery;
import com.lp.server.angebotstkl.ejb.Ekaglieferant;
import com.lp.server.angebotstkl.ejb.Ekgruppe;
import com.lp.server.angebotstkl.ejb.Ekgruppelieferant;
import com.lp.server.angebotstkl.ejb.Ekweblieferant;
import com.lp.server.angebotstkl.ejb.Positionlieferant;
import com.lp.server.angebotstkl.ejb.WebFindChips;
import com.lp.server.angebotstkl.ejb.WebFindChipsQuery;
import com.lp.server.angebotstkl.ejb.Webabfrage;
import com.lp.server.angebotstkl.ejb.Weblieferant;
import com.lp.server.angebotstkl.ejb.WeblieferantQuery;
import com.lp.server.angebotstkl.ejb.Webpartner;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREinkaufsangebotposition;
import com.lp.server.angebotstkl.fastlanereader.generated.FLREkaglieferant;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRPositionlieferant;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklaufschlagDto;
import com.lp.server.angebotstkl.service.AgstklaufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelSchnellerfassungDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelSchnellerfassungDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDto;
import com.lp.server.angebotstkl.service.AgstklpositionsartDtoAssembler;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.AufschlagDto;
import com.lp.server.angebotstkl.service.AufschlagDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsagstklImportSpezifikation;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoAssembler;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoFuerOptimieren;
import com.lp.server.angebotstkl.service.EkagLieferantoptimierenDto;
import com.lp.server.angebotstkl.service.EkaglieferantDto;
import com.lp.server.angebotstkl.service.EkaglieferantDtoAssembler;
import com.lp.server.angebotstkl.service.EkgruppeDto;
import com.lp.server.angebotstkl.service.EkgruppeDtoAssembler;
import com.lp.server.angebotstkl.service.EkgruppelieferantDto;
import com.lp.server.angebotstkl.service.EkgruppelieferantDtoAssembler;
import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.EkweblieferantDtoAssembler;
import com.lp.server.angebotstkl.service.IEkpositionWandlerBeanServices;
import com.lp.server.angebotstkl.service.IWeblieferant;
import com.lp.server.angebotstkl.service.IWebpartner;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.ImportLumiQuoteXlsxDto;
import com.lp.server.angebotstkl.service.PositionlieferantDto;
import com.lp.server.angebotstkl.service.PositionlieferantDtoAssembler;
import com.lp.server.angebotstkl.service.WebFindChipsDto;
import com.lp.server.angebotstkl.service.WebPartnerDtoAssembler;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;
import com.lp.server.angebotstkl.service.WeblieferantDto;
import com.lp.server.angebotstkl.service.WeblieferantDtoAssembler;
import com.lp.server.angebotstkl.service.WebpartnerDto;
import com.lp.server.artikel.ejb.Inventurliste;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.HerstellerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.ImportVATXlsxDto;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.PartnerQuery;
import com.lp.server.partner.ejb.WeblieferantFarnell;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import jxl.Cell;
import jxl.CellType;
import jxl.CellView;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

@Stateless
public class AngebotstklFacBean extends Facade implements AngebotstklFac, IImportHead, IImportPositionen {
	private static final long serialVersionUID = -172684174450687277L;

	private String ALTERNATIV = "alternativ";

	@PersistenceContext
	private EntityManager em;

	@EJB
	private transient AngebotstklpositionLocalFac angebotstklPositionLocalFac;

	@Transient
	private transient ModelMapper mapper = null;

	protected ModelMapper getMapper() {
		if (mapper == null) {
			mapper = new ModelMapper();
		}
		return mapper;
	}

	public Map<String, String> getAllAgstklpositionsart() throws EJBExceptionLP {

		myLogger.entry();
		TreeMap tmArten = new TreeMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("AgstklpositionsartfindAll");

		Collection<?> clArten = query.getResultList();
		// if (clArten.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Agstklpositionsart agstklpositionsartTemp = (Agstklpositionsart) itArten.next();
			Object key = agstklpositionsartTemp.getPositionsartCNr();
			Object value = key;
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return tmArten;
	}

	public AufschlagDto[] aufschlagFindByBMaterial(Integer agstklIId, Short bMaterial, TheClientDto theClientDto) {

		boolean bBereitsEintraegeVorhanden = false;

		Query queryAgstkl = em.createNamedQuery("AgstklaufschlagFindByAgstklIId");
		queryAgstkl.setParameter(1, agstklIId);
		Collection<?> clAgstkl = queryAgstkl.getResultList();

		if (clAgstkl.size() > 0) {
			bBereitsEintraegeVorhanden = true;
		}

		Query query = em.createNamedQuery("AufschlagFindByMandantCNrBMaterial");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, bMaterial);
		Collection<?> cl = query.getResultList();

		AufschlagDto[] dtos = AufschlagDtoAssembler.createDtos(cl);
		for (int i = 0; i < dtos.length; i++) {
			try {
				Query query2 = em.createNamedQuery("AgstklaufschlagFindByAgstklIIdAufschlagIId");
				query2.setParameter(1, agstklIId);
				query2.setParameter(2, dtos[i].getIId());
				Agstklaufschlag agstklaufschlag = (Agstklaufschlag) query2.getSingleResult();
				dtos[i].setAgstklaufschlagDto(AgstklaufschlagDtoAssembler.createDto(agstklaufschlag));
			} catch (NoResultException ex) {

				AgstklaufschlagDto aga = new AgstklaufschlagDto();
				aga.setAgstklIId(agstklIId);
				aga.setAufschlagIId(dtos[i].getIId());
				if (bBereitsEintraegeVorhanden == true) {
					aga.setFAufschlag(0D);
				} else {
					aga.setFAufschlag(dtos[i].getFAufschlag());
				}
				dtos[i].setAgstklaufschlagDto(aga);
			}
		}

		return dtos;

	}

	public LPDatenSubreport getSubreportAgstklMengenstaffel(Integer iIdAngebotstkl, TheClientDto theClientDto) {
		ArrayList alDatenSubreport = new ArrayList();
		String[] fieldnamesMengenstaffel = new String[] { "Menge", "MaterialeinsatzLief1", "AZEinsatzLief1",
				"VKPreisAusAgstkl", "VKPreisGewaehlt", "DBPreis_VKPreis", "DBPreisProzent_VKPreis",
				"DBPreis_VKPreisGewaehlt", "DBPreisProzent_VKPreisGewaehlt", "Person_Aendern", "Datum_Aendern",
				"VKPreisAusKundenpreisfindung", "PreiseEingefroren" };

		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
		query.setParameter(1, iIdAngebotstkl);

		Collection c = query.getResultList();
		Iterator it = c.iterator();

		while (it.hasNext()) {

			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();

			BigDecimal bdWareneinsatz = agstklmengenstaffel.getNMaterialeinsatzLief1();

			if (bdWareneinsatz == null) {
				bdWareneinsatz = getAngebotstklFac().getWareneinsatzLief1(agstklmengenstaffel.getNMenge(),
						agstklmengenstaffel.getAgstklIId(), theClientDto);
			}

			BigDecimal bdAZEinsatz = agstklmengenstaffel.getNAzeinsatzLief1();

			if (bdAZEinsatz == null) {
				bdAZEinsatz = getAngebotstklFac().getAZeinsatzLief1(agstklmengenstaffel.getNMenge(),
						agstklmengenstaffel.getAgstklIId(), theClientDto);
			}

			BigDecimal bdVkpreis = agstklmengenstaffel.getNVkpreis();

			BigDecimal[] bdVkpreise = getAngebotstklFac().getVKPreis(agstklmengenstaffel.getNMenge(),
					agstklmengenstaffel.getAgstklIId(), theClientDto);

			if (bdVkpreis == null) {
				bdVkpreis = bdVkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS];
			}

			Object[] zeile = new Object[fieldnamesMengenstaffel.length];
			zeile[0] = agstklmengenstaffel.getNMenge();
			zeile[1] = bdWareneinsatz;

			zeile[2] = bdAZEinsatz;
			zeile[3] = bdVkpreis;
			zeile[11] = bdVkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG];

			BigDecimal dbPreis = bdVkpreis.subtract(bdWareneinsatz).subtract(bdAZEinsatz);

			zeile[5] = dbPreis;

			BigDecimal dbPreisProzent = BigDecimal.ZERO;
			if (bdVkpreis.doubleValue() != 0) {
				dbPreisProzent = dbPreis.divide(bdVkpreis, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			}

			zeile[6] = dbPreisProzent;

			if (agstklmengenstaffel.getNVkpreisGewaehlt() != null) {
				zeile[4] = agstklmengenstaffel.getNVkpreisGewaehlt();

				BigDecimal dbPreisVKGewaehlt = agstklmengenstaffel.getNVkpreisGewaehlt().subtract(bdWareneinsatz)
						.subtract(bdAZEinsatz);

				zeile[7] = dbPreisVKGewaehlt;

				BigDecimal dbPreisProzentVKGewaehlt = BigDecimal.ZERO;
				if (bdVkpreis.doubleValue() != 0) {
					dbPreisProzentVKGewaehlt = dbPreisVKGewaehlt
							.divide(agstklmengenstaffel.getNVkpreisGewaehlt(), 4, BigDecimal.ROUND_HALF_UP)
							.multiply(new BigDecimal(100));
				}

				zeile[8] = dbPreisProzentVKGewaehlt;
			}

			zeile[9] = getPersonalFac().personalFindByPrimaryKeySmall(agstklmengenstaffel.getPersonalIIdAendern())
					.getCKurzzeichen();
			zeile[10] = agstklmengenstaffel.getTAendern();

			if (agstklmengenstaffel.getNAzeinsatzLief1() != null) {
				zeile[12] = Boolean.TRUE;
			} else {
				zeile[12] = Boolean.FALSE;
			}

			alDatenSubreport.add(zeile);

		}

		Object[][] dataSubAufschlag = new Object[alDatenSubreport.size()][fieldnamesMengenstaffel.length];
		dataSubAufschlag = (Object[][]) alDatenSubreport.toArray(dataSubAufschlag);
		return new LPDatenSubreport(dataSubAufschlag, fieldnamesMengenstaffel);
	}

	public LPDatenSubreport getSubreportAgstklMengenstaffelSchnellerfassung(Integer iIdAngebotstkl,
			TheClientDto theClientDto) {
		ArrayList alDatenSubreport = new ArrayList();
		String[] fieldnamesMengenstaffel = new String[] { "Menge", "AufschlagMaterial", "AufschlagAZ", "WertMaterial",
				"WertAZ", "PreisEinheit" };

		Query query = em.createNamedQuery("AgstklmengenstaffelSchnellerfassungFindByAgstklIId");
		query.setParameter(1, iIdAngebotstkl);

		Collection c = query.getResultList();
		Iterator it = c.iterator();

		while (it.hasNext()) {

			AgstklmengenstaffelSchnellerfassung agstklmengenstaffel = (AgstklmengenstaffelSchnellerfassung) it.next();

			Object[] zeile = new Object[fieldnamesMengenstaffel.length];
			zeile[0] = agstklmengenstaffel.getNMenge();
			zeile[1] = agstklmengenstaffel.getNAufschlagMaterial();
			zeile[2] = agstklmengenstaffel.getNAufschlagAz();
			zeile[3] = agstklmengenstaffel.getNWertMaterial();
			zeile[4] = agstklmengenstaffel.getNWertAz();
			zeile[5] = agstklmengenstaffel.getNPreisEinheit();

			alDatenSubreport.add(zeile);

		}

		Object[][] dataSubAufschlag = new Object[alDatenSubreport.size()][fieldnamesMengenstaffel.length];
		dataSubAufschlag = (Object[][]) alDatenSubreport.toArray(dataSubAufschlag);
		return new LPDatenSubreport(dataSubAufschlag, fieldnamesMengenstaffel);
	}

	public Integer createAgstkl(AgstklDto agstklDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("agstklDto == null"));
		}
		if (agstklDto.getKundeIId() == null || agstklDto.getWaehrungCNr() == null
				|| agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null
				|| agstklDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"agstklDto.getKundeIId() == null || agstklDto.getWaehrungCNr() == null || agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null || agstklDto.getTBelegdatum() == null"));
		}

		try {

			// generieren von primary key & auftragsnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(PKConst.PK_AGSTKL,
					theClientDto.getMandant(), theClientDto);

			agstklDto.setIId(bnr.getPrimaryKey());
			agstklDto.setCNr(f.format(bnr));
			agstklDto.setMandantCNr(theClientDto.getMandant());

			agstklDto.setBDatengeaendert(Helper.boolean2Short(false));

			if (agstklDto.getBVorlage() == null) {
				agstklDto.setBVorlage(Helper.boolean2Short(false));
			}

			agstklDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			agstklDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			agstklDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			agstklDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			agstklDto.setBelegartCNr(LocaleFac.BELEGART_AGSTUECKLISTE);

			if (agstklDto.getIEkpreisbasis() == null) {
				try {
					ParametermandantDto parameterMand = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE, ParameterFac.PARAMETER_EK_PREISBASIS);
					int iEkPreisbasis = (Integer) parameterMand.getCWertAsObject();
					agstklDto.setIEkpreisbasis(iEkPreisbasis);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			Agstkl agstkl = new Agstkl(agstklDto.getIId(), agstklDto.getMandantCNr(), agstklDto.getCNr(),
					agstklDto.getBelegartCNr(), agstklDto.getKundeIId(), agstklDto.getTBelegdatum(),
					agstklDto.getWaehrungCNr(), agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung(),
					agstklDto.getPersonalIIdAnlegen(), agstklDto.getPersonalIIdAendern(), agstklDto.getIEkpreisbasis(),
					agstklDto.getBDatengeaendert(), agstklDto.getBVorlage());
			em.persist(agstkl);
			em.flush();
			setAgstklFromAgstklDto(agstkl, agstklDto);
			return agstklDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeAgstkl(AgstklDto agstklDto) throws EJBExceptionLP {
		myLogger.entry();
		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("agstklDto == null"));
		}
		if (agstklDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("agstklDto.getIId() == null"));
		}
		// try {
		// try {
		Agstkl toRemove = em.find(Agstkl.class, agstklDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAgstkl es gibt keine iid " + agstklDto.getIId() + "\nagstklDto.toString: "
							+ agstklDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

	}

	public void removeEkgruppe(EkgruppeDto ekgruppeDto) {
		Ekgruppe toRemove = em.find(Ekgruppe.class, ekgruppeDto.getIId());
		em.remove(toRemove);
		em.flush();

	}

	public void removeEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto) {
		Ekgruppelieferant toRemove = em.find(Ekgruppelieferant.class, ekgruppelieferantDto.getIId());
		em.remove(toRemove);
		em.flush();

	}

	public void removeEkaglieferant(EkaglieferantDto ekaglieferantDto) {
		Ekaglieferant toRemove = em.find(Ekaglieferant.class, ekaglieferantDto.getIId());
		em.remove(toRemove);
		em.flush();

	}

	public void removePositionlieferant(PositionlieferantDto positionlieferantDto) {
		Positionlieferant toRemove = em.find(Positionlieferant.class, positionlieferantDto.getIId());
		em.remove(toRemove);
		em.flush();

	}

	public void updateAgstkl(AgstklDto agstklDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("agstklDto == null"));
		}
		if (agstklDto.getIId() == null || agstklDto.getWaehrungCNr() == null
				|| agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null
				|| agstklDto.getBelegartCNr() == null || agstklDto.getKundeIId() == null
				|| agstklDto.getMandantCNr() == null || agstklDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"agstklDto.getIId() == null || agstklDto.getWaehrungCNr() == null || agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung() == null ||  agstklDto.getBelegartCNr() == null || agstklDto.getKundeIId() == null  || agstklDto.getMandantCNr() == null || agstklDto.getTBelegdatum() == null"));
		}
		Integer iId = agstklDto.getIId();

		Agstkl agstkl = null;
		// try {
		agstkl = em.find(Agstkl.class, iId);
		if (agstkl == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateAgstkl es gibt keine iid " + agstklDto.getIId() + "\nagstklDto.toString(): "
							+ agstklDto.toString());

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		try {
			Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
			query.setParameter(1, agstklDto.getCNr());
			query.setParameter(2, agstklDto.getMandantCNr());
			Integer iIdVorhanden = ((Agstkl) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AG_AGSTKL.CNR"));
			}
		} catch (NoResultException ex) {

		}
		agstklDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		agstklDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		setAgstklFromAgstklDto(agstkl, agstklDto);

	}

	public AgstklDto agstklFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}

		AgstklDto agstklDto = agstklFindByPrimaryKeyOhneExc(iId);

		if (agstklDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei agstklFindByPrimaryKey. Es gibt keine iid " + iId);

		}
		return agstklDto;

	}

	public AgstklDto agstklFindByPrimaryKeyOhneExc(Integer iId) {
		Agstkl agstkl = em.find(Agstkl.class, iId);
		if (agstkl == null) {
			return null;
		}
		return assembleAgstklDto(agstkl);

	}

	public AgstklDto[] agstklFindByKundeIIdMandantCNr(Integer iIdKunde, String cNrMandant) throws EJBExceptionLP {
		if (iIdKunde == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdKunde == null"));
		}
		// try {
		Query query = em.createNamedQuery("AgstklfindByKundeIIdMandantCNr");
		query.setParameter(1, iIdKunde);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// null);
		// }
		return assembleAgstklDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public AgstklDto[] agstklFindByKundeIIdMandantCNrOhneExc(Integer iIdKunde, String cNrMandant) {
		if (iIdKunde == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("iIdKunde == null"));
		}
		try {
			Query query = em.createNamedQuery("AgstklfindByKundeIIdMandantCNr");
			query.setParameter(1, iIdKunde);
			query.setParameter(2, cNrMandant);
			return assembleAgstklDtos((Collection<?>) query.getResultList());
		} catch (Throwable t) {
			myLogger.warn("iIdKunde=" + iIdKunde + " cNrMandant=" + cNrMandant, t);
			return null;
		}
	}

	private void setAgstklFromAgstklDto(Agstkl agstkl, AgstklDto agstklDto) {
		agstkl.setMandantCNr(agstklDto.getMandantCNr());
		agstkl.setCNr(agstklDto.getCNr());
		agstkl.setBelegartCNr(agstklDto.getBelegartCNr());
		agstkl.setKundeIId(agstklDto.getKundeIId());
		agstkl.setAnsprechpartnerIIdKunde(agstklDto.getAnsprechpartnerIIdKunde());
		agstkl.setCBez(agstklDto.getCBez());
		agstkl.setWaehrungCNr(agstklDto.getWaehrungCNr());
		agstkl.setFWechselkursmandantwaehrungzuagstklwaehrung(
				agstklDto.getFWechselkursmandantwaehrungzuagstklwaehrung());
		agstkl.setPersonalIIdAnlegen(agstklDto.getPersonalIIdAnlegen());
		agstkl.setTAnlegen(agstklDto.getTAnlegen());
		agstkl.setPersonalIIdAendern(agstklDto.getPersonalIIdAendern());
		agstkl.setTAendern(agstklDto.getTAendern());
		agstkl.setTBelegdatum(agstklDto.getTBelegdatum());
		agstkl.setProjektIId(agstklDto.getProjektIId());
		agstkl.setIEkpreisbasis(agstklDto.getIEkpreisbasis());
		agstkl.setBDatengeaendert(agstklDto.getBDatengeaendert());
		agstkl.setStuecklisteIId(agstklDto.getStuecklisteIId());
		agstkl.setBVorlage(agstklDto.getBVorlage());
		agstkl.setOMEdia(agstklDto.getOMedia());
		agstkl.setDatenformatCNr(agstklDto.getDatenformatCNr());
		agstkl.setIHoeheDialog(agstklDto.getIHoeheDialog());
		agstkl.setCDateiname(agstklDto.getCDateiname());
		agstkl.setCZeichnungsnummer(agstklDto.getCZeichnungsnummer());
		agstkl.setNInitialkosten(agstklDto.getNInitialkosten());

		em.merge(agstkl);
		em.flush();
	}

	private void setAufschlagFromAufschlagDto(Aufschlag aufschlag, AufschlagDto aufschlagDto) {
		aufschlag.setMandantCNr(aufschlagDto.getMandantCNr());
		aufschlag.setCBez(aufschlagDto.getCBez());
		aufschlag.setFAufschlag(aufschlagDto.getFAufschlag());
		aufschlag.setBMaterial(aufschlagDto.getBMaterial());

		em.merge(aufschlag);
		em.flush();
	}

	private void setEkgruppeFromEkgruppeDto(Ekgruppe ekgruppe, EkgruppeDto ekgruppeDto) {
		ekgruppe.setMandantCNr(ekgruppeDto.getMandantCNr());
		ekgruppe.setCBez(ekgruppeDto.getCBez());

		em.merge(ekgruppe);
		em.flush();
	}

	private void setEkgruppelieferantFromEkgruppelieferantDto(Ekgruppelieferant ekgruppelieferant,
			EkgruppelieferantDto ekgruppelieferantDto) {
		ekgruppelieferant.setEkgruppeIId(ekgruppelieferantDto.getEkgruppeIId());
		ekgruppelieferant.setLieferantIId(ekgruppelieferantDto.getLieferantIId());
		ekgruppelieferant.setAnsprechpartnerIId(ekgruppelieferantDto.getAnsprechpartnerIId());

		em.merge(ekgruppelieferant);
		em.flush();
	}

	private void setEkaglieferantFromEkgruppelieferantDto(Ekaglieferant ekaglieferant,
			EkaglieferantDto ekaglieferantDto) {
		ekaglieferant.setEinkaufsangebotIId(ekaglieferantDto.getEinkaufsangebotIId());
		ekaglieferant.setLieferantIId(ekaglieferantDto.getLieferantIId());
		ekaglieferant.setCAngebotsnummer(ekaglieferantDto.getCAngebotsnummer());
		ekaglieferant.setTImport(ekaglieferantDto.getTImport());
		ekaglieferant.setWaehrungCNr(ekaglieferantDto.getWaehrungCNr());
		ekaglieferant.setLieferantIId(ekaglieferantDto.getLieferantIId());
		ekaglieferant.setAnsprechpartnerIId(ekaglieferantDto.getAnsprechpartnerIId());
		ekaglieferant.setTVersand(ekaglieferantDto.getTVersand());
		ekaglieferant.setNAufschlag(ekaglieferantDto.getNAufschlag());
		em.merge(ekaglieferant);
		em.flush();
	}

	private void setPositionlieferantFromPositionlieferantDto(Positionlieferant positionlieferant,
			PositionlieferantDto positionlieferantDto) {
		positionlieferant.setEinkaufsangebotpositionIId(positionlieferantDto.getEinkaufsangebotpositionIId());
		positionlieferant.setEgaklieferantIId(positionlieferantDto.getEgaklieferantIId());
		positionlieferant.setCArtikelnrlieferant(positionlieferantDto.getCArtikelnrlieferant());

		positionlieferant.setCBemerkung(positionlieferantDto.getCBemerkung());
		positionlieferant.setCBemerkungIntern(positionlieferantDto.getCBemerkungIntern());
		positionlieferant.setCBemerkungVerkauf(positionlieferantDto.getCBemerkungVerkauf());
		positionlieferant.setILieferzeitinkw(positionlieferantDto.getILieferzeitinkw());
		positionlieferant.setNLagerstand(positionlieferantDto.getNLagerstand());
		positionlieferant.setNMindestbestellmenge(positionlieferantDto.getNMindestbestellmenge());
		positionlieferant.setNPreisMenge1(positionlieferantDto.getNPreisMenge1());
		positionlieferant.setNPreisMenge2(positionlieferantDto.getNPreisMenge2());
		positionlieferant.setNPreisMenge3(positionlieferantDto.getNPreisMenge3());
		positionlieferant.setNPreisMenge4(positionlieferantDto.getNPreisMenge4());
		positionlieferant.setNPreisMenge5(positionlieferantDto.getNPreisMenge5());

		positionlieferant.setNTransportkosten(positionlieferantDto.getNTransportkosten());
		positionlieferant.setNVerpackungseinheit(positionlieferantDto.getNVerpackungseinheit());
		positionlieferant.setTAendern(positionlieferantDto.getTAendern());

		positionlieferant.setBMenge1Bestellen(positionlieferantDto.getBMenge1Bestellen());
		positionlieferant.setBMenge2Bestellen(positionlieferantDto.getBMenge2Bestellen());
		positionlieferant.setBMenge3Bestellen(positionlieferantDto.getBMenge3Bestellen());
		positionlieferant.setBMenge4Bestellen(positionlieferantDto.getBMenge4Bestellen());
		positionlieferant.setBMenge5Bestellen(positionlieferantDto.getBMenge5Bestellen());

		em.merge(positionlieferant);
		em.flush();
	}

	private void setAgstklmengenstaffelFromAgstklmengenstaffelDto(Agstklmengenstaffel agstklmengenstaffel,
			AgstklmengenstaffelDto agstklmengenstaffelDto) {
		agstklmengenstaffel.setAgstklIId(agstklmengenstaffelDto.getAgstklIId());
		agstklmengenstaffel.setNAzeinsatzLief1(agstklmengenstaffelDto.getNAzeinsatzLief1());
		agstklmengenstaffel.setNMaterialeinsatzLief1(agstklmengenstaffelDto.getNMaterialeinsatzLief1());
		agstklmengenstaffel.setNMenge(agstklmengenstaffelDto.getNMenge());
		agstklmengenstaffel.setNVkpreis(agstklmengenstaffelDto.getNVkpreis());
		agstklmengenstaffel.setNVkpreisGewaehlt(agstklmengenstaffelDto.getNVkpreisGewaehlt());
		agstklmengenstaffel.setPersonalIIdAendern(agstklmengenstaffelDto.getPersonalIIdAendern());
		agstklmengenstaffel.setTAendern(agstklmengenstaffelDto.getTAendern());

		em.merge(agstklmengenstaffel);
		em.flush();
	}

	private AgstklDto assembleAgstklDto(Agstkl agstkl) {
		return AgstklDtoAssembler.createDto(agstkl);
	}

	private AgstklDto[] assembleAgstklDtos(Collection<?> agstkls) {
		List<AgstklDto> list = new ArrayList<AgstklDto>();
		if (agstkls != null) {
			Iterator<?> iterator = agstkls.iterator();
			while (iterator.hasNext()) {
				Agstkl agstkl = (Agstkl) iterator.next();
				list.add(assembleAgstklDto(agstkl));
			}
		}
		AgstklDto[] returnArray = new AgstklDto[list.size()];
		return (AgstklDto[]) list.toArray(returnArray);
	}

	public void createAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto == null) {
			return;
		}
		try {

			Agstklpositionsart agstklpositionsart = new Agstklpositionsart(agstklpositionsartDto.getPositionsartCNr(),
					agstklpositionsartDto.getISort());
			em.persist(agstklpositionsart);
			em.flush();
			setAgstklpositionsartFromAgstklpositionsartDto(agstklpositionsart, agstklpositionsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		}
	}

	public void removeAgstklpositionsart(String positionsartCNr) throws EJBExceptionLP {
		try {
			Agstklpositionsart toRemove = em.find(Agstklpositionsart.class, positionsartCNr);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAgstklPositionart. Es gibt keine Positionsart " + positionsartCNr);
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

	public void removeAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto != null) {
			String positionsartCNr = agstklpositionsartDto.getPositionsartCNr();
			removeAgstklpositionsart(positionsartCNr);
		}
	}

	public void updateAgstklpositionsart(AgstklpositionsartDto agstklpositionsartDto) throws EJBExceptionLP {
		if (agstklpositionsartDto != null) {
			String positionsartCNr = agstklpositionsartDto.getPositionsartCNr();
			try {
				Agstklpositionsart agstklpositionsart = em.find(Agstklpositionsart.class, positionsartCNr);
				setAgstklpositionsartFromAgstklpositionsartDto(agstklpositionsart, agstklpositionsartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public AgstklpositionsartDto agstklpositionsartFindByPrimaryKey(String positionsartCNr) throws EJBExceptionLP {
		try {
			Agstklpositionsart agstklpositionsart = em.find(Agstklpositionsart.class, positionsartCNr);
			if (agstklpositionsart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei agstklpositionartfindbypriomaryKey. Es gibt keine Positionsart " + positionsartCNr);
			}
			return assembleAgstklpositionsartDto(agstklpositionsart);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setAgstklpositionsartFromAgstklpositionsartDto(Agstklpositionsart agstklpositionsart,
			AgstklpositionsartDto agstklpositionsartDto) {
		agstklpositionsart.setISort(agstklpositionsartDto.getISort());
		em.merge(agstklpositionsart);
		em.flush();
	}

	private AgstklpositionsartDto assembleAgstklpositionsartDto(Agstklpositionsart agstklpositionsart) {
		return AgstklpositionsartDtoAssembler.createDto(agstklpositionsart);
	}

	private AgstklpositionsartDto[] assembleAgstklpositionsartDtos(Collection<?> agstklpositionsarts) {
		List<AgstklpositionsartDto> list = new ArrayList<AgstklpositionsartDto>();
		if (agstklpositionsarts != null) {
			Iterator<?> iterator = agstklpositionsarts.iterator();
			while (iterator.hasNext()) {
				Agstklpositionsart agstklpositionsart = (Agstklpositionsart) iterator.next();
				list.add(assembleAgstklpositionsartDto(agstklpositionsart));
			}
		}
		AgstklpositionsartDto[] returnArray = new AgstklpositionsartDto[list.size()];
		return (AgstklpositionsartDto[]) list.toArray(returnArray);
	}

	public String getAngeboteDieBestimmteAngebotsstuecklisteVerwenden(Integer agstklIId, TheClientDto theClientDto)
			throws EJBExceptionLP {

		String sText = "";

		FLRAngebotposition flrStuecklisteposition = new FLRAngebotposition();
		flrStuecklisteposition.setAgstkl_i_id(agstklIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRAngebotposition.class)
				.add(Example.create(flrStuecklisteposition)); // .createAlias(
		// AngebotpositionFac
		// .
		// FLR_ANGEBOTPOSITION_FLRANGEBOT, "a");
		crit.addOrder(Order.desc("i_id"));

		List<?> resultList = crit.list();

		Iterator<?> resultListIterator = resultList.iterator();
		FLRAngebot letztesAngebot = null;
		while (resultListIterator.hasNext()) {
			FLRAngebotposition flrAngebotposition = (FLRAngebotposition) resultListIterator.next();
			if (letztesAngebot == null || letztesAngebot.equals(flrAngebotposition.getFlrangebot())) {
				letztesAngebot = flrAngebotposition.getFlrangebot();

				sText = sText + flrAngebotposition.getFlrangebot().getC_nr();
				if (flrAngebotposition.getFlrangebot().getC_bez() != null) {
					sText = sText + ", " + flrAngebotposition.getFlrangebot().getC_bez();
				}
				sText = sText + ", " + flrAngebotposition.getFlrangebot().getFlrkunde().getFlrpartner()
						.getC_name1nachnamefirmazeile1();
				sText = sText + "\n";
			}
		}
		session.close();
		return sText;
	}

	/**
	 * Liefert den kalkulatorischen Wert einer Angebotsstueckliste.
	 * 
	 * @param iIdAgstklI   PK der Agstkl
	 * @param cNrWaehrungI die gewuenschte Waehrung
	 * @param theClientDto der aktuelle Benutzer
	 * @return BigDecimal der kalkulatorische Wert
	 * @throws EJBExceptionLP Ausnahme
	 */
	public BigDecimal berechneKalkulatorischenAgstklwert(Integer iIdAgstklI, BigDecimal nMengenstaffel,
			String cNrWaehrungI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdAgstklI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iIdAgstklI == null"));
		}

		if (cNrWaehrungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("cNrWaehrungI == null"));
		}

		BigDecimal nWert = new BigDecimal(0);

		try {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN,
					theClientDto) && nMengenstaffel != null) {

				Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdKleinerGleichNMenge");
				query.setParameter(1, iIdAgstklI);
				query.setParameter(2, nMengenstaffel);

				query.setMaxResults(1);
				Collection c = query.getResultList();
				Iterator it = c.iterator();

				BigDecimal bdWareneinsatz = null;
				BigDecimal bdAZEinsatz = null;

				if (it.hasNext()) {
					Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();

					bdWareneinsatz = agstklmengenstaffel.getNMaterialeinsatzLief1();
					bdAZEinsatz = agstklmengenstaffel.getNAzeinsatzLief1();
				}

				if (bdWareneinsatz == null) {
					bdWareneinsatz = getAngebotstklFac().getWareneinsatzLief1(nMengenstaffel, iIdAgstklI, theClientDto);
				}
				if (bdAZEinsatz == null) {
					bdAZEinsatz = getAngebotstklFac().getAZeinsatzLief1(nMengenstaffel, iIdAgstklI, theClientDto);
				}

				nWert = bdAZEinsatz.add(bdWareneinsatz);

			} else {

				AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
						.agstklpositionFindByAgstklIIdMengeNotNullOhneExc(iIdAgstklI, theClientDto);

				if (aAgstklpositionDto != null) {
					for (int i = 0; i < aAgstklpositionDto.length; i++) {
						AgstklpositionDto agstklpositionDto = aAgstklpositionDto[i];

						// SP8216 zuvor wurden nur positive Mengen beruecksichtigt
						// nun natuerlich auch die negativen
						if (agstklpositionDto.getNMenge() != null && agstklpositionDto.getNMenge().signum() != 0) {
							BigDecimal nMenge = agstklpositionDto.getNMenge();
							// Grundlage ist der Nettogesamtwert der Position in
							// Agstklwaehrung
							BigDecimal nWertDerPosition = nMenge.multiply(agstklpositionDto.getNNettogesamtpreis());
							// PJ18040 Aufschlaege hinzufuegen
							boolean bMaterial = true;
							if (agstklpositionDto.getArtikelIId() != null) {
								ArtikelDto aDto = getArtikelFac()
										.artikelFindByPrimaryKeySmall(agstklpositionDto.getArtikelIId(), theClientDto);
								if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT) == true) {
									bMaterial = false;
								}
							}

							AufschlagDto[] aufschlagDtos = aufschlagFindByBMaterial(agstklpositionDto.getAgstklIId(),
									Helper.boolean2Short(bMaterial), theClientDto);

							Double dAufschlaege = 0D;

							for (int k = 0; k < aufschlagDtos.length; k++) {

								dAufschlaege = dAufschlaege + aufschlagDtos[k].getAgstklaufschlagDto().getFAufschlag();

							}
							nWertDerPosition = nWertDerPosition
									.add(Helper.getProzentWert(nWertDerPosition, new BigDecimal(dAufschlaege), 2));

							nWert = nWert.add(nWertDerPosition);
						}

					}
				}
			}

			// der kalkulatorische Wert ist nun in der Waehrung der
			// Agstkl bekannt
			AgstklDto agstklDto = agstklFindByPrimaryKey(iIdAgstklI);

			if (!cNrWaehrungI.equals(agstklDto.getWaehrungCNr())) {
				nWert = getLocaleFac().rechneUmInAndereWaehrungZuDatum(nWert, agstklDto.getWaehrungCNr(), cNrWaehrungI,
						new Date(System.currentTimeMillis()), theClientDto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
		}

		return nWert;
	}

	public BigDecimal[] berechneAgstklMaterialwertUndArbeitszeitwert(Integer iIdAgstklI, TheClientDto theClientDto) {

		if (iIdAgstklI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iIdAgstklI == null"));
		}

		BigDecimal[] nWerte = new BigDecimal[2];
		nWerte[0] = new BigDecimal(0);
		nWerte[1] = new BigDecimal(0);

		try {
			AgstklpositionDto[] aAgstklpositionDto = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIIdMengeNotNullOhneExc(iIdAgstklI, theClientDto);

			if (aAgstklpositionDto != null) {
				for (int i = 0; i < aAgstklpositionDto.length; i++) {
					AgstklpositionDto agstklpositionDto = aAgstklpositionDto[i];

					// SP8216 zuvor wurden nur positive Mengen beruecksichtigt
					// nun natuerlich auch die negativen
					if (agstklpositionDto.getNMenge() != null && agstklpositionDto.getNMenge().signum() != 0) {

						boolean bArbeitszeit = false;
						if (agstklpositionDto.getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(agstklpositionDto.getArtikelIId(), theClientDto);
							if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARBEITSZEIT)) {
								bArbeitszeit = true;
							}
						}

						BigDecimal nMenge = agstklpositionDto.getNMenge();

						// Grundlage ist der Nettogesamtwert der Position in
						// Agstklwaehrung
						BigDecimal nWertDerPosition = nMenge.multiply(agstklpositionDto.getNNettogesamtpreis());

						if (bArbeitszeit == false) {
							nWerte[0] = nWerte[0].add(nWertDerPosition);
						} else {
							nWerte[1] = nWerte[1].add(nWertDerPosition);
						}

					}

					// der kalkulatorische Wert ist nun in der Waehrung der
					// Agstkl bekannt

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return nWerte;
	}

	public void vertauscheEinkausangebotpositionen(Integer idPosition1I, Integer idPosition2I) throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Einkaufsangebotposition oPosition1 = em.find(Einkaufsangebotposition.class, idPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"Fehler bei vertauscheEinkaufsangebotpositionen. Es gibt keine Einkaufsangebotpos mit der iid "
							+ idPosition1I);
		}

		Einkaufsangebotposition oPosition2 = em.find(Einkaufsangebotposition.class, idPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// iSort der zweiten Position auf ungueltig setzen, damit UK constraint
		// nicht verletzt wird
		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
		query.setParameter(1, agstklIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Agstklposition oPreisliste = (Agstklposition) it.next();

			if (oPreisliste.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPreisliste.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }

	}

	public Integer createEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto, TheClientDto theClientDto) {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getKundeIId() == null || einkaufsangebotDto.getNMenge1() == null
				|| einkaufsangebotDto.getNMenge2() == null || einkaufsangebotDto.getNMenge3() == null
				|| einkaufsangebotDto.getNMenge4() == null || einkaufsangebotDto.getNMenge5() == null
				|| einkaufsangebotDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufsangebotDto.getKundeIId() == null || einkaufsangebotDto.getNMenge1() == null || einkaufsangebotDto.getNMenge2() == null || einkaufsangebotDto.getNMenge3() == null || einkaufsangebotDto.getNMenge4() == null || einkaufsangebotDto.getNMenge5() == null || einkaufsangebotDto.getTBelegdatum() == null"));
		}

		try {

			// generieren von primary key & auftragsnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(PKConst.PK_EINKAUFSANGEBOT,
					theClientDto.getMandant(), theClientDto);

			einkaufsangebotDto.setIId(bnr.getPrimaryKey());
			einkaufsangebotDto.setCNr(f.format(bnr));
			einkaufsangebotDto.setMandantCNr(theClientDto.getMandant());

			einkaufsangebotDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			einkaufsangebotDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			einkaufsangebotDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			einkaufsangebotDto.setTAendern(new Timestamp(System.currentTimeMillis()));
			if (einkaufsangebotDto.getBRoHs() == null) {
				einkaufsangebotDto.setBRoHs(Helper.boolean2Short(false));
			}
			if (einkaufsangebotDto.getBKundeExportieren() == null) {
				einkaufsangebotDto.setBKundeExportieren(Helper.boolean2Short(false));
			}
			if (einkaufsangebotDto.getBOptimierenMinmenge() == null) {
				einkaufsangebotDto.setBOptimierenMinmenge(Helper.boolean2Short(false));
			}
			if (einkaufsangebotDto.getBOptimierenVerpackungseinheit() == null) {
				einkaufsangebotDto.setBOptimierenVerpackungseinheit(Helper.boolean2Short(false));
			}

			Einkaufsangebot einkaufsangebot = new Einkaufsangebot(einkaufsangebotDto.getIId(),
					einkaufsangebotDto.getMandantCNr(), einkaufsangebotDto.getCNr(),
					einkaufsangebotDto.getTBelegdatum(), einkaufsangebotDto.getKundeIId(),
					einkaufsangebotDto.getNMenge1(), einkaufsangebotDto.getNMenge2(), einkaufsangebotDto.getNMenge3(),
					einkaufsangebotDto.getNMenge4(), einkaufsangebotDto.getNMenge5(),
					einkaufsangebotDto.getPersonalIIdAnlegen(), einkaufsangebotDto.getPersonalIIdAendern(),
					einkaufsangebotDto.getBRoHs(), einkaufsangebotDto.getBKundeExportieren(),
					einkaufsangebotDto.getBOptimierenMinmenge(), einkaufsangebotDto.getBOptimierenVerpackungseinheit());
			em.persist(einkaufsangebot);
			em.flush();
			setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot, einkaufsangebotDto);

			// PJ19155 Weblieferanten als Ekweblieferanten uebernehmen
			uebernehmeWeblieferantenAusGrunddaten(einkaufsangebotDto.getIId(), theClientDto);

			return einkaufsangebotDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws EJBExceptionLP {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufsangebotDto.getIId() == null"));
		}

		Integer iId = einkaufsangebotDto.getIId();
		// try {
		// try {
		Einkaufsangebot toRemove = em.find(Einkaufsangebot.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeEinkaufsangebot. Es gibt keine iid " + iId + "\neinkaufsangebotDto.toString: "
							+ einkaufsangebotDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }

	}

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (einkaufsangebotDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("einkaufsangebotDto == null"));
		}
		if (einkaufsangebotDto.getIId() == null || einkaufsangebotDto.getKundeIId() == null
				|| einkaufsangebotDto.getNMenge1() == null || einkaufsangebotDto.getNMenge2() == null
				|| einkaufsangebotDto.getNMenge3() == null || einkaufsangebotDto.getNMenge4() == null
				|| einkaufsangebotDto.getNMenge5() == null || einkaufsangebotDto.getTBelegdatum() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufsangebotDto.getIId() == null || einkaufsangebotDto.getKundeIId() == null || einkaufsangebotDto.getNMenge1() == null || einkaufsangebotDto.getNMenge2() == null || einkaufsangebotDto.getNMenge3() == null || einkaufsangebotDto.getNMenge4() == null || einkaufsangebotDto.getNMenge5() == null || einkaufsangebotDto.getTBelegdatum() == null"));
		}

		Integer iId = einkaufsangebotDto.getIId();

		einkaufsangebotDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		einkaufsangebotDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		// try {
		Einkaufsangebot einkaufsangebot = em.find(Einkaufsangebot.class, iId);
		if (einkaufsangebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsangebot. es gibt kein Einkaufsangebot mit der iid "
							+ einkaufsangebotDto.getIId() + "\neinkaufsangebotDto.toString(): "
							+ einkaufsangebotDto.toString());

		}
		setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot, einkaufsangebotDto);
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

	}

	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Einkaufsangebot einkaufsangebot = em.find(Einkaufsangebot.class, iId);
		if (einkaufsangebot == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei FindByPrimaryKey. es gibt keine Einkaufsangebot mit der iid " + iId);
		}
		return assembleEinkaufsangebotDto(einkaufsangebot);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }

	}

	private void setEinkaufsangebotFromEinkaufsangebotDto(Einkaufsangebot einkaufsangebot,
			EinkaufsangebotDto einkaufsangebotDto) {
		einkaufsangebot.setMandantCNr(einkaufsangebotDto.getMandantCNr());
		einkaufsangebot.setCNr(einkaufsangebotDto.getCNr());
		einkaufsangebot.setTBelegdatum(einkaufsangebotDto.getTBelegdatum());
		einkaufsangebot.setCProjekt(einkaufsangebotDto.getCProjekt());
		einkaufsangebot.setKundeIId(einkaufsangebotDto.getKundeIId());
		einkaufsangebot.setAnsprechpartnerIId(einkaufsangebotDto.getAnsprechpartnerIId());
		einkaufsangebot.setNMenge1(einkaufsangebotDto.getNMenge1());
		einkaufsangebot.setNMenge2(einkaufsangebotDto.getNMenge2());
		einkaufsangebot.setNMenge3(einkaufsangebotDto.getNMenge3());
		einkaufsangebot.setNMenge4(einkaufsangebotDto.getNMenge4());
		einkaufsangebot.setNMenge5(einkaufsangebotDto.getNMenge5());
		einkaufsangebot.setPersonalIIdAnlegen(einkaufsangebotDto.getPersonalIIdAnlegen());
		einkaufsangebot.setTAnlegen(einkaufsangebotDto.getTAnlegen());
		einkaufsangebot.setPersonalIIdAendern(einkaufsangebotDto.getPersonalIIdAendern());
		einkaufsangebot.setTAendern(einkaufsangebotDto.getTAendern());
		einkaufsangebot.setTLiefertermin(einkaufsangebotDto.getTLiefertermin());
		einkaufsangebot.setBRoHs(einkaufsangebotDto.getBRoHs());
		einkaufsangebot.setIAnzahlwebabfragen(einkaufsangebotDto.getIAnzahlwebabfragen());
		einkaufsangebot.setBKundeExportieren(einkaufsangebotDto.getBKundeExportieren());
		einkaufsangebot.setBOptimierenMinmenge(einkaufsangebotDto.getBOptimierenMinmenge());
		einkaufsangebot.setBOptimierenVerpackungseinheit(einkaufsangebotDto.getBOptimierenVerpackungseinheit());
		einkaufsangebot.setIOptimierenLieferzeit(einkaufsangebotDto.getIOptimierenLieferzeit());
		einkaufsangebot.setIOptimierenMenge(einkaufsangebotDto.getIOptimierenMenge());
		einkaufsangebot.setISortierung(einkaufsangebotDto.getISortierung());
		einkaufsangebot.setTFertigungstermin(einkaufsangebotDto.getTFertigungstermin());
		einkaufsangebot.setCKommentar(einkaufsangebotDto.getCKommentar());
		em.merge(einkaufsangebot);
		em.flush();
	}

	private EinkaufsangebotDto assembleEinkaufsangebotDto(Einkaufsangebot einkaufsangebot) {
		return EinkaufsangebotDtoAssembler.createDto(einkaufsangebot);
	}

	private EinkaufsangebotDto[] assembleEinkaufsangebotDtos(Collection<?> einkaufsangebots) {
		List<EinkaufsangebotDto> list = new ArrayList<EinkaufsangebotDto>();
		if (einkaufsangebots != null) {
			Iterator<?> iterator = einkaufsangebots.iterator();
			while (iterator.hasNext()) {
				Einkaufsangebot einkaufsangebot = (Einkaufsangebot) iterator.next();
				list.add(assembleEinkaufsangebotDto(einkaufsangebot));
			}
		}
		EinkaufsangebotDto[] returnArray = new EinkaufsangebotDto[list.size()];
		return (EinkaufsangebotDto[]) list.toArray(returnArray);
	}

	public Integer createEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto,
			TheClientDto theClientDto) {
		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getBelegIId() == null
				|| einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null
				|| einkaufsangebotpositionDto.getPositionsartCNr() == null
				|| einkaufsangebotpositionDto.getBMitdrucken() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufsangebotpositionDto.getBelegIId() == null || einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null || einkaufsangebotpositionDto.getAgstklpositionsartCNr() == null"));
		}

		Integer einkaufsangebotpositionIId = null;

		einkaufsangebotpositionDto.setNMenge(Helper.rundeKaufmaennisch(einkaufsangebotpositionDto.getNMenge(), 4));

		try {

			if (einkaufsangebotpositionDto.getPositionsartCNr()
					.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				einkaufsangebotpositionDto.setArtikelIId(null);
			}

			if (einkaufsangebotpositionDto.getCZusatzbez() != null
					&& einkaufsangebotpositionDto.getCZusatzbez().length() > 79) {
				einkaufsangebotpositionDto.setCZusatzbez(einkaufsangebotpositionDto.getCZusatzbez().substring(0, 79));
			}

			// generieren von primary key
			einkaufsangebotpositionIId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_EINKAUFSANGEBOTPOSITION);

			einkaufsangebotpositionDto.setIId(einkaufsangebotpositionIId);
			if (einkaufsangebotpositionDto.getISort() == null) {
				Query query = em.createNamedQuery("EinkaufsangebotpositionejbSelectMaxISort");
				query.setParameter(1, einkaufsangebotpositionDto.getBelegIId());
				Integer i = (Integer) query.getSingleResult();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				einkaufsangebotpositionDto.setISort(i);
			}
			Einkaufsangebotposition einkaufsangebotposition = new Einkaufsangebotposition(
					einkaufsangebotpositionDto.getIId(), einkaufsangebotpositionDto.getBelegIId(),
					einkaufsangebotpositionDto.getPositionsartCNr(),
					einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert(),
					einkaufsangebotpositionDto.getBMitdrucken());
			em.persist(einkaufsangebotposition);
			em.flush();
			setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(einkaufsangebotposition,
					einkaufsangebotpositionDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(t));
		}
		return einkaufsangebotpositionIId;

	}

	public void removeEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws EJBExceptionLP {
		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("einkaufsangebotpositionDto.getIId() == null"));
		}
		// try {
		Einkaufsangebotposition toRemove = em.find(Einkaufsangebotposition.class, einkaufsangebotpositionDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeEinkaufsangebotposition. Es gibt keine Position mit iid "
							+ einkaufsangebotpositionDto.getIId() + "\neinkaufsangebotpositionDto.toString: "
							+ einkaufsangebotpositionDto.toString());
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void updateEinkaufsangebotposition(EinkaufsangebotpositionDto einkaufsangebotpositionDto)
			throws EJBExceptionLP {

		if (einkaufsangebotpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("einkaufsangebotpositionDto == null"));
		}
		if (einkaufsangebotpositionDto.getIId() == null || einkaufsangebotpositionDto.getBelegIId() == null
				|| einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null
				|| einkaufsangebotpositionDto.getPositionsartCNr() == null
				|| einkaufsangebotpositionDto.getBMitdrucken() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"einkaufsangebotpositionDto.getIId() == null || einkaufsangebotpositionDto.getBelegIId() == null || einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert() == null || einkaufsangebotpositionDto.getAgstklpositionsartCNr() == null"));
		}

		if (einkaufsangebotpositionDto.getPositionsartCNr()
				.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
			einkaufsangebotpositionDto.setArtikelIId(null);
		}
		einkaufsangebotpositionDto.setNMenge(Helper.rundeKaufmaennisch(einkaufsangebotpositionDto.getNMenge(), 4));
		Integer iId = einkaufsangebotpositionDto.getIId();
		// try {
		Einkaufsangebotposition einkaufsangebotposition = em.find(Einkaufsangebotposition.class, iId);
		if (einkaufsangebotposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateEinkaufsangebotsposition. Es gibt keine iid " + iId
							+ "\neinkaufsangebotpositionDto.toString: " + einkaufsangebotpositionDto.toString());
		}
		setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(einkaufsangebotposition, einkaufsangebotpositionDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public void kopierePositionenAusStueckliste(Integer stuecklisteIId, Integer agstklIId, TheClientDto theClientDto) {
		StuecklistepositionDto[] stklPosDtos = getStuecklisteFac()
				.stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);

		try {

			for (int i = 0; i < stklPosDtos.length; i++) {
				StuecklistepositionDto stklPosDto = stklPosDtos[i];

				AgstklpositionDto agstklpositionDtoI = new AgstklpositionDto();
				agstklpositionDtoI.setAgstklIId(agstklIId);

				agstklpositionDtoI.setNMenge(stklPosDto.getNMenge());
				agstklpositionDtoI.setCPosition(stklPosDto.getCPosition());

				// SP5159 Zielmenge
				agstklpositionDtoI
						.setNMenge(getStuecklisteFac().berechneZielmenge(stklPosDtos[i].getIId(), theClientDto));

				agstklpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
				agstklpositionDtoI.setBDrucken(stklPosDto.getBMitdrucken());
				agstklpositionDtoI.setBRabattsatzuebersteuert(Helper.getShortFalse());

				ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(stklPosDto.getArtikelIId(), theClientDto);
				agstklpositionDtoI.setEinheitCNr(a.getEinheitCNr());
				agstklpositionDtoI.setArtikelIId(a.getIId());
				if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					agstklpositionDtoI.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE);
					if (a.getArtikelsprDto() != null) {
						agstklpositionDtoI.setCBez(a.getArtikelsprDto().getCBez());
						agstklpositionDtoI.setCZbez(a.getArtikelsprDto().getCZbez());
					}
				} else {
					agstklpositionDtoI.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

				}

				agstklpositionDtoI = angebotstklPositionLocalFac
						.befuelleMitPreisenNachKalkulationsart(agstklpositionDtoI, theClientDto);

				getAngebotstklpositionFac().createAgstklposition(agstklpositionDtoI, theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void kopiereArbeitsplanAusStuecklisteInPositionen(Integer stuecklisteIId, Integer agstklIId,
			TheClientDto theClientDto) {

		try {
			StuecklistearbeitsplanDto[] arbeitsplanPos = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId, theClientDto);
			for (int i = 0; i < arbeitsplanPos.length; i++) {
				StuecklistearbeitsplanDto arbeitsplanPosDto = arbeitsplanPos[i];

				AgstklpositionDto agstklpositionDtoI = new AgstklpositionDto();
				agstklpositionDtoI.setAgstklIId(agstklIId);

				agstklpositionDtoI.setBInitial(arbeitsplanPosDto.getBInitial());
				agstklpositionDtoI.setNMenge(Helper.berechneGesamtzeitInStunden(arbeitsplanPosDto.getLRuestzeit(),
						arbeitsplanPosDto.getLStueckzeit(), new BigDecimal(1), null, null));
				agstklpositionDtoI.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));

				// SP4465
				agstklpositionDtoI.setBDrucken(Helper.boolean2Short(false));

				ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(arbeitsplanPosDto.getArtikelIId(), theClientDto);
				agstklpositionDtoI.setArtikelIId(a.getIId());

				agstklpositionDtoI.setAgstklpositionsartCNr(AngebotstklServiceFac.AGSTKLPOSITIONART_IDENT);

				agstklpositionDtoI.setBRabattsatzuebersteuert(Helper.getShortFalse());
				agstklpositionDtoI.setBNettopreisuebersteuert(Helper.boolean2Short(false));

				BigDecimal gestpreis = getLagerFac().getGemittelterGestehungspreisEinesLagers(
						arbeitsplanPosDto.getArtikelIId(),
						getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId(), theClientDto);
				agstklpositionDtoI.setNGestehungspreis(gestpreis);

				// angebotstklPositionLocalFac
				// .befuellePositionMitPreisenKalkulationsart2(
				// theClientDto, agstklDto.getWaehrungCNr(),
				// arbeitsplanPosDto.getArtikelIId(),
				// agstklpositionDtoI.getNMenge(),
				// agstklpositionDtoI);
				agstklpositionDtoI = angebotstklPositionLocalFac
						.befuelleMitPreisenNachKalkulationsart(agstklpositionDtoI, theClientDto);
				getAngebotstklpositionFac().createAgstklposition(agstklpositionDtoI, theClientDto);

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void kopiereEkgruppeInEkaglieferant(Integer einkaufangebotId, Integer ekgruppeIId,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery("EkgruppelieferantFindByEkgruppeIId");
		query.setParameter(1, ekgruppeIId);
		Collection<?> cl = query.getResultList();
		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Ekgruppelieferant l = (Ekgruppelieferant) it.next();

			try {
				Query query2 = em.createNamedQuery("EkaglieferantFindByEinkaufsangebotIIdLieferantIId");
				query2.setParameter(1, einkaufangebotId);
				query2.setParameter(2, l.getLieferantIId());
				// @todo getSingleResult oder getResultList ?
				Ekaglieferant doppelt = (Ekaglieferant) query2.getSingleResult();
				// Auslassen
			} catch (NoResultException ex) {
				EkaglieferantDto dto = new EkaglieferantDto();
				dto.setEinkaufsangebotIId(einkaufangebotId);

				LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(l.getLieferantIId(), theClientDto);

				dto.setLieferantIId(l.getLieferantIId());
				dto.setAnsprechpartnerIId(l.getAnsprechpartnerIId());
				dto.setWaehrungCNr(lfDto.getWaehrungCNr());
				dto.setNAufschlag(BigDecimal.ZERO);

				createEkaglieferant(dto, theClientDto);
			}

		}

	}

	public void kopiereArbeitsplanAusStuecklisteInArbeitsplan(Integer stuecklisteIId, Integer agstklIId,
			TheClientDto theClientDto) {

		StuecklistearbeitsplanDto[] arbeitsplanPos = getStuecklisteFac()
				.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId, theClientDto);
		for (int i = 0; i < arbeitsplanPos.length; i++) {
			StuecklistearbeitsplanDto arbeitsplanPosDto = arbeitsplanPos[i];

			AgstklarbeitsplanDto agstklarbeitsplanDto = new AgstklarbeitsplanDto();
			agstklarbeitsplanDto.setAgstklIId(agstklIId);
			agstklarbeitsplanDto.setAgartCNr(arbeitsplanPosDto.getAgartCNr());
			agstklarbeitsplanDto.setArtikelIId(arbeitsplanPosDto.getArtikelIId());
			agstklarbeitsplanDto.setBNurmaschinenzeit(arbeitsplanPosDto.getBNurmaschinenzeit());
			agstklarbeitsplanDto.setBInitial(arbeitsplanPosDto.getBInitial());
			agstklarbeitsplanDto.setCKommentar(arbeitsplanPosDto.getCKommentar());
			agstklarbeitsplanDto.setIArbeitsgang(arbeitsplanPosDto.getIArbeitsgang());
			agstklarbeitsplanDto.setIAufspannung(arbeitsplanPosDto.getIAufspannung());
			agstklarbeitsplanDto.setIUnterarbeitsgang(arbeitsplanPosDto.getIUnterarbeitsgang());
			agstklarbeitsplanDto.setLRuestzeit(arbeitsplanPosDto.getLRuestzeit());
			agstklarbeitsplanDto.setLStueckzeit(arbeitsplanPosDto.getLStueckzeit());
			agstklarbeitsplanDto.setMaschineIId(arbeitsplanPosDto.getMaschineIId());
			agstklarbeitsplanDto.setXLangtext(arbeitsplanPosDto.getXLangtext());

			getAngebotstklFac().createAgstklarbeitsplan(agstklarbeitsplanDto, theClientDto);

		}

	}

	public AgstklarbeitsplanDto[] agstklarbeitsplanFindByAgstklIId(Integer iIdAgstklI, TheClientDto theClientDto) {
		AgstklarbeitsplanDto[] agstklarbeitsplanDto = null;
		Query query = em.createNamedQuery("AgstklarbeitsplanFindByAgstklIIdOrderByArbeitsgang");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();
		agstklarbeitsplanDto = AgstklarbeitsplanDtoAssembler.createDtos(cl);
		return agstklarbeitsplanDto;
	}

	public void kopiereAgstklArbeitsplan(Integer agstklIId_Quelle, Integer agstklIId_Ziel, TheClientDto theClientDto) {
		if (agstklIId_Quelle == null || agstklIId_Ziel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("agstklIId_Quelle == null || agstklIId_Ziel == null"));
		}

		Query query = em.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
		query.setParameter(1, agstklIId_Quelle);
		Collection<?> cl = query.getResultList();

		AgstklarbeitsplanDto[] dtos = AgstklarbeitsplanDtoAssembler.createDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			AgstklarbeitsplanDto dto = dtos[i];
			dto.setAgstklIId(agstklIId_Ziel);
			createAgstklarbeitsplan(dto, theClientDto);
		}

	}

	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(Integer iId) throws EJBExceptionLP {

		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iId == null"));
		}

		// try {
		Einkaufsangebotposition einkaufsangebotposition = em.find(Einkaufsangebotposition.class, iId);
		if (einkaufsangebotposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei EinkaufspositionFindByPrimaryKey. Es gibt keine iid " + iId);
		}
		return assembleEinkaufsangebotpositionDto(einkaufsangebotposition);
		// }
		// catch (FinderException fe) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// fe);

		// }

	}

	@Override
	public List<EinkaufsangebotpositionDto> einkaufsangebotpositionenFindByPrimaryKeys(Integer[] iIds)
			throws EJBExceptionLP {
		Validator.notNull(iIds, "Einkaufsangebotpositionen IIds");

		List<EinkaufsangebotpositionDto> positionen = new ArrayList<EinkaufsangebotpositionDto>();
		for (Integer iId : iIds) {
			positionen.add(einkaufsangebotpositionFindByPrimaryKey(iId));
		}

		return positionen;
	}

	public Integer einkaufsangebotpositionGetMaxISort(Integer einkaufsangebotIId) {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("EinkaufsangebotpositionejbSelectMaxISort");
			query.setParameter(1, einkaufsangebotIId);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler. bei einkaufsangebotpositiongetmaxisort trat ein Fehler auf. iid: " + einkaufsangebotIId);
		}
		return iiMaxISortO;
	}

	private void setEinkaufsangebotpositionFromEinkaufsangebotpositionDto(
			Einkaufsangebotposition einkaufsangebotposition, EinkaufsangebotpositionDto einkaufsangebotpositionDto) {
		einkaufsangebotposition.setEinkaufsangebotIId(einkaufsangebotpositionDto.getBelegIId());
		einkaufsangebotposition.setISort(einkaufsangebotpositionDto.getISort());
		einkaufsangebotposition.setAgstklpositionsartCNr(einkaufsangebotpositionDto.getPositionsartCNr());
		einkaufsangebotposition.setArtikelIId(einkaufsangebotpositionDto.getArtikelIId());
		einkaufsangebotposition.setCBemerkung(einkaufsangebotpositionDto.getCBemerkung());
		einkaufsangebotposition.setCBez(einkaufsangebotpositionDto.getCBez());
		einkaufsangebotposition.setCZbez(einkaufsangebotpositionDto.getCZusatzbez());
		einkaufsangebotposition
				.setBArtikelbezeichnunguebersteuert(einkaufsangebotpositionDto.getBArtikelbezeichnunguebersteuert());
		einkaufsangebotposition.setNMenge(einkaufsangebotpositionDto.getNMenge());
		einkaufsangebotposition.setNPreis1(einkaufsangebotpositionDto.getNPreis1());
		einkaufsangebotposition.setNPreis2(einkaufsangebotpositionDto.getNPreis2());
		einkaufsangebotposition.setNPreis3(einkaufsangebotpositionDto.getNPreis3());
		einkaufsangebotposition.setNPreis4(einkaufsangebotpositionDto.getNPreis4());
		einkaufsangebotposition.setNPreis5(einkaufsangebotpositionDto.getNPreis5());
		einkaufsangebotposition.setEinheitCNr(einkaufsangebotpositionDto.getEinheitCNr());

		einkaufsangebotposition.setIVerpackungseinheit(einkaufsangebotpositionDto.getIVerpackungseinheit());
		einkaufsangebotposition.setIWiederbeschaffungszeit(einkaufsangebotpositionDto.getIWiederbeschaffungszeit());
		einkaufsangebotposition.setFMindestbestellmenge(einkaufsangebotpositionDto.getFMindestbestellmenge());
		einkaufsangebotposition.setCPosition(einkaufsangebotpositionDto.getCPosition());
		einkaufsangebotposition.setCInternebemerkung(einkaufsangebotpositionDto.getCInternebemerkung());
		einkaufsangebotposition.setBMitdrucken(einkaufsangebotpositionDto.getBMitdrucken());

		einkaufsangebotposition.setCKommentar1(einkaufsangebotpositionDto.getCKommentar1());
		einkaufsangebotposition.setCKommentar2(einkaufsangebotpositionDto.getCKommentar2());

		einkaufsangebotposition.setHerstellerIId(einkaufsangebotpositionDto.getHerstellerIId());
		einkaufsangebotposition.setLieferantIId(einkaufsangebotpositionDto.getLieferantIId());
		einkaufsangebotposition.setTLetztewebabfrage(einkaufsangebotpositionDto.getTLetztewebabfrage());
		einkaufsangebotposition.setCBuyerurl(einkaufsangebotpositionDto.getCBuyerurl());
		einkaufsangebotposition.setCArtikelnrhersteller(einkaufsangebotpositionDto.getCArtikelnrhersteller());

		einkaufsangebotposition.setPositionlieferantIIdUebersteuertMenge1(
				einkaufsangebotpositionDto.getPositionlieferantIIdUebersteuertMenge1());
		einkaufsangebotposition.setPositionlieferantIIdUebersteuertMenge2(
				einkaufsangebotpositionDto.getPositionlieferantIIdUebersteuertMenge2());
		einkaufsangebotposition.setPositionlieferantIIdUebersteuertMenge3(
				einkaufsangebotpositionDto.getPositionlieferantIIdUebersteuertMenge3());
		einkaufsangebotposition.setPositionlieferantIIdUebersteuertMenge4(
				einkaufsangebotpositionDto.getPositionlieferantIIdUebersteuertMenge4());
		einkaufsangebotposition.setPositionlieferantIIdUebersteuertMenge5(
				einkaufsangebotpositionDto.getPositionlieferantIIdUebersteuertMenge5());
		einkaufsangebotposition.setCArtikelbezhersteller(einkaufsangebotpositionDto.getCArtikelbezhersteller());
		einkaufsangebotposition.setCZbez2(einkaufsangebotpositionDto.getCZbez2());
		einkaufsangebotposition.setILfdnummer(einkaufsangebotpositionDto.getILfdnummer());
		em.merge(einkaufsangebotposition);
		em.flush();
	}

	private EinkaufsangebotpositionDto assembleEinkaufsangebotpositionDto(
			Einkaufsangebotposition einkaufsangebotposition) {
		return EinkaufsangebotpositionDtoAssembler.createDto(einkaufsangebotposition);
	}

	private EinkaufsangebotpositionDto[] assembleEinkaufsangebotpositionDtos(Collection<?> einkaufsangebotpositions) {
		List<EinkaufsangebotpositionDto> list = new ArrayList<EinkaufsangebotpositionDto>();
		if (einkaufsangebotpositions != null) {
			Iterator<?> iterator = einkaufsangebotpositions.iterator();
			while (iterator.hasNext()) {
				Einkaufsangebotposition einkaufsangebotposition = (Einkaufsangebotposition) iterator.next();
				list.add(assembleEinkaufsangebotpositionDto(einkaufsangebotposition));
			}
		}
		EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[list.size()];
		return (EinkaufsangebotpositionDto[]) list.toArray(returnArray);
	}

	public Integer createAufschlag(AufschlagDto aufschlagDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("AufschlagFindByMandantCNrCBez");
			query.setParameter(1, aufschlagDto.getMandantCNr());
			query.setParameter(2, aufschlagDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Aufschlag doppelt = (Aufschlag) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_AUFSCHLAG.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AUFSCHLAG);
		aufschlagDto.setIId(pk);

		Aufschlag aufschlag = new Aufschlag(aufschlagDto.getIId(), aufschlagDto.getMandantCNr(),
				aufschlagDto.getBMaterial(), aufschlagDto.getFAufschlag(), aufschlagDto.getCBez());
		em.persist(aufschlag);
		em.flush();
		setAufschlagFromAufschlagDto(aufschlag, aufschlagDto);
		return aufschlagDto.getIId();
	}

	public Integer createEkgruppe(EkgruppeDto ekgruppeDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("EkgruppeFindByMandantCNrCBez");
			query.setParameter(1, ekgruppeDto.getMandantCNr());
			query.setParameter(2, ekgruppeDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Ekgruppe doppelt = (Ekgruppe) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_EKGRUPPE.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EKGRUPPE);
		ekgruppeDto.setIId(pk);

		Ekgruppe aufschlag = new Ekgruppe(ekgruppeDto.getIId(), ekgruppeDto.getMandantCNr(), ekgruppeDto.getCBez());
		em.persist(aufschlag);
		em.flush();
		setEkgruppeFromEkgruppeDto(aufschlag, ekgruppeDto);
		return ekgruppeDto.getIId();
	}

	public Integer createEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("EkgruppelieferantFindByEkgruppeIIdLieferantIId");
			query.setParameter(1, ekgruppelieferantDto.getEkgruppeIId());
			query.setParameter(2, ekgruppelieferantDto.getLieferantIId());
			// @todo getSingleResult oder getResultList ?
			Ekgruppelieferant doppelt = (Ekgruppelieferant) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_EKGRUPPELIEFERANT.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EKGRUPPELIEFERANT);
		ekgruppelieferantDto.setIId(pk);

		Ekgruppelieferant ekgruppelieferant = new Ekgruppelieferant(ekgruppelieferantDto.getIId(),
				ekgruppelieferantDto.getEkgruppeIId(), ekgruppelieferantDto.getLieferantIId());
		em.persist(ekgruppelieferant);
		em.flush();
		setEkgruppelieferantFromEkgruppelieferantDto(ekgruppelieferant, ekgruppelieferantDto);
		return ekgruppelieferantDto.getIId();
	}

	public Integer createEkaglieferant(EkaglieferantDto ekaglieferantDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("EkaglieferantFindByEinkaufsangebotIIdLieferantIId");
			query.setParameter(1, ekaglieferantDto.getEinkaufsangebotIId());
			query.setParameter(2, ekaglieferantDto.getLieferantIId());
			// @todo getSingleResult oder getResultList ?
			Ekaglieferant doppelt = (Ekaglieferant) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_EKAGLIEFERANT.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_EKAGLIEFERANT);
		ekaglieferantDto.setIId(pk);

		Ekaglieferant ekaglieferant = new Ekaglieferant(ekaglieferantDto.getIId(),
				ekaglieferantDto.getEinkaufsangebotIId(), ekaglieferantDto.getLieferantIId(),
				ekaglieferantDto.getWaehrungCNr(), ekaglieferantDto.getNAufschlag());
		em.persist(ekaglieferant);
		em.flush();
		setEkaglieferantFromEkgruppelieferantDto(ekaglieferant, ekaglieferantDto);
		return ekaglieferantDto.getIId();
	}

	public Integer createPositionlieferant(PositionlieferantDto positionlieferantDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("PositionlieferantfindByEinkaufsangebotpositionIIdEgaklieferantIId");
			query.setParameter(1, positionlieferantDto.getEinkaufsangebotpositionIId());
			query.setParameter(2, positionlieferantDto.getEgaklieferantIId());
			// @todo getSingleResult oder getResultList ?
			Positionlieferant doppelt = (Positionlieferant) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_POSITIONLIEFERANT.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_POSITIONLIEFERANT);
		positionlieferantDto.setIId(pk);
		positionlieferantDto.setTAendern(getTimestamp());

		Positionlieferant positionlieferant = new Positionlieferant(positionlieferantDto.getIId(),
				positionlieferantDto.getEinkaufsangebotpositionIId(), positionlieferantDto.getEgaklieferantIId(),
				positionlieferantDto.getTAendern());
		em.persist(positionlieferant);
		em.flush();
		setPositionlieferantFromPositionlieferantDto(positionlieferant, positionlieferantDto);
		return positionlieferantDto.getIId();
	}

	public Integer createAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelDto.getNMenge());

			Agstklmengenstaffel doppelt = (Agstklmengenstaffel) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("AS_AGSTKLMENGENSTAFFEL.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLMENGENSTAFFEL);
		agstklmengenstaffelDto.setIId(pk);
		agstklmengenstaffelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		agstklmengenstaffelDto.setTAendern(new Timestamp(System.currentTimeMillis()));

		Agstklmengenstaffel agstklmengenstaffel = new Agstklmengenstaffel(agstklmengenstaffelDto.getIId(),
				agstklmengenstaffelDto.getAgstklIId(), agstklmengenstaffelDto.getNMenge(),
				agstklmengenstaffelDto.getPersonalIIdAendern(), agstklmengenstaffelDto.getTAendern());
		em.persist(agstklmengenstaffel);
		em.flush();
		setAgstklmengenstaffelFromAgstklmengenstaffelDto(agstklmengenstaffel, agstklmengenstaffelDto);
		return agstklmengenstaffelDto.getIId();
	}

	public Integer createAgstklmengenstaffelSchnellerfassung(
			AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("AgstklmengenstaffelSchnellerfassungFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelSchnellerfassungDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelSchnellerfassungDto.getNMenge());

			AgstklmengenstaffelSchnellerfassung doppelt = (AgstklmengenstaffelSchnellerfassung) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("AS_AGSTKLMENGENSTAFFELSCHNELLERFASSUNG.UK"));
		} catch (NoResultException ex) {

		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLMENGENSTAFFELSCHNELLEERFASSUNG);
		agstklmengenstaffelSchnellerfassungDto.setIId(pk);

		AgstklmengenstaffelSchnellerfassung agstklmengenstaffel = new AgstklmengenstaffelSchnellerfassung(
				agstklmengenstaffelSchnellerfassungDto.getIId(), agstklmengenstaffelSchnellerfassungDto.getAgstklIId(),
				agstklmengenstaffelSchnellerfassungDto.getNMenge(),
				agstklmengenstaffelSchnellerfassungDto.getNAufschlagAz(),
				agstklmengenstaffelSchnellerfassungDto.getNAufschlagMaterial(),
				agstklmengenstaffelSchnellerfassungDto.getNWertMaterial(),
				agstklmengenstaffelSchnellerfassungDto.getNWertAz(),
				agstklmengenstaffelSchnellerfassungDto.getNPreisEinheit());
		em.persist(agstklmengenstaffel);
		em.flush();

		return agstklmengenstaffelSchnellerfassungDto.getIId();
	}

	public void removeAufschlag(Integer aufschlagIId) {
		Aufschlag toRemove = em.find(Aufschlag.class, aufschlagIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAgstklmengenstaffel(Integer agstklmengenstaffelIId) {
		Agstklmengenstaffel toRemove = em.find(Agstklmengenstaffel.class, agstklmengenstaffelIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAgstklmengenstaffelSchnellerfassung(Integer agstklmengenstaffelschnellerfassungIId) {
		AgstklmengenstaffelSchnellerfassung toRemove = em.find(AgstklmengenstaffelSchnellerfassung.class,
				agstklmengenstaffelschnellerfassungIId);
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public BigDecimal getWareneinsatzLief1(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto) {
		BigDecimal bdWareneinsatzLief1 = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac().agstklpositionFindByAgstklIId(agstklIId,
					theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				BigDecimal bdFaktorFuerRuestmenge = BigDecimal.ONE;

				if (Helper.short2boolean(dto.getBRuestmenge()) && bdMenge.doubleValue() != 0) {
					bdFaktorFuerRuestmenge = BigDecimal.ONE.divide(bdMenge, 4, BigDecimal.ROUND_HALF_EVEN);
				}

				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);

					if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARTIKEL)) {
						// MATERIAL

						// todo PJ18725
						// Wenn Parameterv EK_PREISBASIS=0 (LIEF1PREIS), dann so
						// wie nachstehend

						if (agstklDto.getIEkpreisbasis().intValue() == EK_PREISBASIS_LIEF1PREIS) {

							ArtikellieferantDto alDto = getArtikelFac()
									.getArtikelEinkaufspreisDesBevorzugtenLieferanten(dto.getArtikelIId(),
											bdMenge.multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge),
											agstklDto.getWaehrungCNr(), theClientDto);
							if (alDto != null && alDto.getNNettopreis() != null) {
								bdWareneinsatzLief1 = bdWareneinsatzLief1.add(alDto.getNNettopreis()
										.multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge));
							}
						} else {

							// Wenn Parameterv EK_PREISBASIS=1 (NETTOPREIS),
							// dann so
							// wie nachstehend (Nettopreis aus Position)

							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(dto.getNNettogesamtpreis()
									.multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge));

						}
					} else if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {

						// SP5252
						bdWareneinsatzLief1 = bdWareneinsatzLief1.add(
								dto.getNNettogesamtpreis().multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge));

					} else {
						// AZ
					}
				}
			}

			ArrayList<AgstklmaterialDto> materialDtos = getAngebotstklpositionFac()
					.agstklmaterialFindByAgstklIId(agstklIId, theClientDto);

			for (AgstklmaterialDto materialDto : materialDtos) {

				if (materialDto.getNGewichtpreis() != null) {
					bdWareneinsatzLief1 = bdWareneinsatzLief1.add(materialDto.getNGewichtpreis());
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return bdWareneinsatzLief1;
	}

	public BigDecimal getAZeinsatzLief1(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto) {
		BigDecimal bdWareneinsatzLief1 = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac().agstklpositionFindByAgstklIId(agstklIId,
					theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {
					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);

					if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)
							|| aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARTIKEL)) {
						// MATERIAL

					} else {
						// AZ
						ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
								dto.getArtikelIId(), bdMenge.multiply(dto.getNMenge()), agstklDto.getWaehrungCNr(),
								theClientDto);
						if (alDto != null && alDto.getNNettopreis() != null) {
							bdWareneinsatzLief1 = bdWareneinsatzLief1
									.add(alDto.getNNettopreis().multiply(dto.getNMenge()));
						}
					}
				}
			}

			Query query = em.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
			query.setParameter(1, agstklIId);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			while (it.hasNext()) {
				Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(ap.getLRuestzeit(), ap.getLStueckzeit(),
						bdMenge, new BigDecimal(1), ap.getIAufspannung());
				if (bdMenge.doubleValue() != 0) {

					bdGesamtzeit = bdGesamtzeit.divide(bdMenge, BigDecimal.ROUND_HALF_UP, 4);
				}

				if (bdGesamtzeit != null) {

					if (Helper.short2boolean(ap.getBNurmaschinenzeit()) == false) {

						ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(ap.getArtikelIId(), null,
								bdMenge.multiply(bdGesamtzeit), agstklDto.getWaehrungCNr(),
								new java.sql.Date(agstklDto.getTBelegdatum().getTime()), theClientDto);
						if (alDto != null && alDto.getNNettopreis() != null) {
							bdWareneinsatzLief1 = bdWareneinsatzLief1
									.add(alDto.getNNettopreis().multiply(bdGesamtzeit));
						}
					}

					if (ap.getMaschineIId() != null) {

						BigDecimal bdMaschinenkosten = getZeiterfassungFac()
								.getMaschinenKostenZumZeitpunkt(ap.getMaschineIId(), agstklDto.getTBelegdatum(),
										LocaleFac.BELEGART_AGSTUECKLISTE, ap.getIId())
								.getBdStundensatz();

						if (bdMaschinenkosten != null) {

							bdWareneinsatzLief1 = bdWareneinsatzLief1.add(bdMaschinenkosten.multiply(bdGesamtzeit));

						}

					}

				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return bdWareneinsatzLief1;
	}

	public PositionlieferantDto getGuenstigstenLieferant(Integer einkaufsangebotpositionIId, int iMenge,
			TheClientDto theClientDto) {
		return getGuenstigstenLieferant(einkaufsangebotpositionIId, iMenge, null, false, false, theClientDto);
	}

	public PositionlieferantDto getGuenstigstenLieferant(Integer einkaufsangebotpositionIId, int iMenge,
			Integer iLieferzeitInKW, boolean bMindestbestellmengeBeruecksichtigen,
			boolean bVerpackungseinheitBeruecksichtigen, TheClientDto theClientDto) {
		BigDecimal bdGuenstigsterPreis = null;

		Session sessionLF = FLRSessionFactory.getFactory().openSession();
		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.einkaufsangebotposition_i_id="
				+ einkaufsangebotpositionIId;

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		Integer positionlieferantIIdZuVerwenden = null;

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			BigDecimal bdPreisTemp = null;
			BigDecimal bdMengeTemp = null;
			if (iMenge == MENGE_1) {
				bdPreisTemp = poslief.getN_preis_menge1();
				bdMengeTemp = poslief.getFlreinkaufsangebotposition().getN_menge()
						.multiply(poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge1());

			} else if (iMenge == MENGE_2) {
				bdPreisTemp = poslief.getN_preis_menge2();

				bdMengeTemp = poslief.getFlreinkaufsangebotposition().getN_menge()
						.multiply(poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge2());

			} else if (iMenge == MENGE_3) {
				bdPreisTemp = poslief.getN_preis_menge3();

				bdMengeTemp = poslief.getFlreinkaufsangebotposition().getN_menge()
						.multiply(poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge2());

			} else if (iMenge == MENGE_4) {
				bdPreisTemp = poslief.getN_preis_menge4();

				bdMengeTemp = poslief.getFlreinkaufsangebotposition().getN_menge()
						.multiply(poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge4());

			} else if (iMenge == MENGE_5) {
				bdPreisTemp = poslief.getN_preis_menge5();

				bdMengeTemp = poslief.getFlreinkaufsangebotposition().getN_menge()
						.multiply(poslief.getFlreinkaufsangebotposition().getFlreinkaufsangebot().getN_menge5());

			}

			if (bdPreisTemp != null) {
				BigDecimal aufschlag = Helper.getProzentWert(bdPreisTemp,
						poslief.getFlrekaglieferant().getN_aufschlag(), 6);
				bdPreisTemp = bdPreisTemp.add(aufschlag);
			}

			if (iLieferzeitInKW != null) {

				// Wenn ausreichend auf Lager dann spielt die Lieferzeit keine Roll
				if (poslief.getN_lagerstand() != null
						&& poslief.getN_lagerstand().doubleValue() >= bdMengeTemp.doubleValue()) {
					// OK
				} else {

					// Wennd er Lieferant keine Lieferzeit angegeben hat und zuwenig auf Lager hat,
					// dann wird diese Zeile nict beruecksichtigt
					if (poslief.getI_lieferzeitinkw() == null
							|| poslief.getI_lieferzeitinkw().intValue() > iLieferzeitInKW.intValue()) {
						// Zeile wird nicht mehr beruecksichtigt
						continue;
					}
				}

			}

			// Mindestbestellmenge beruecksichtigen
			if (bMindestbestellmengeBeruecksichtigen == true && bdPreisTemp != null
					&& poslief.getN_mindestbestellmenge() != null
					&& bdMengeTemp.doubleValue() < poslief.getN_mindestbestellmenge().doubleValue()) {

				if (bdMengeTemp.doubleValue() != 0) {
					bdPreisTemp = bdPreisTemp.divide(bdMengeTemp, 12, BigDecimal.ROUND_HALF_EVEN)
							.multiply(poslief.getN_mindestbestellmenge());
					bdMengeTemp = poslief.getN_mindestbestellmenge();
				}

			}

			// Verpackungseinheit
			if (bVerpackungseinheitBeruecksichtigen && bdPreisTemp != null
					&& poslief.getN_verpackungseinheit() != null) {

				double d = bdMengeTemp.doubleValue() % poslief.getN_verpackungseinheit().doubleValue();
				if (d != 0) {

					BigDecimal bdFehlende = poslief.getN_verpackungseinheit().subtract(bdMengeTemp);

					if (bdMengeTemp.doubleValue() != 0) {
						bdPreisTemp = bdPreisTemp.divide(bdMengeTemp, 12, BigDecimal.ROUND_HALF_EVEN)
								.multiply(bdMengeTemp.add(bdFehlende));

					}

					bdMengeTemp = bdMengeTemp.add(bdFehlende);

				}

			}

			if (bdPreisTemp != null) {
				// umrechnen
				try {
					bdPreisTemp = Helper.rundeKaufmaennisch(
							getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdPreisTemp,
									poslief.getFlrekaglieferant().getWaehrung_c_nr(),
									theClientDto.getSMandantenwaehrung(), getDate(), theClientDto),
							getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant()));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			if (bdGuenstigsterPreis == null && bdPreisTemp != null) {
				bdGuenstigsterPreis = bdPreisTemp;
				positionlieferantIIdZuVerwenden = poslief.getI_id();
			}

			if (bdPreisTemp != null && bdGuenstigsterPreis != null
					&& bdGuenstigsterPreis.doubleValue() > bdPreisTemp.doubleValue()) {
				bdGuenstigsterPreis = bdPreisTemp;
				positionlieferantIIdZuVerwenden = poslief.getI_id();
			}

		}

		if (positionlieferantIIdZuVerwenden != null) {

			PositionlieferantDto posliefDto = positionlieferantFindByPrimaryKeyInZielWaehrung(
					positionlieferantIIdZuVerwenden, theClientDto.getSMandantenwaehrung(), theClientDto);

			if (iMenge == MENGE_1) {
				posliefDto.setNPreisMenge1(bdGuenstigsterPreis);
			} else if (iMenge == MENGE_2) {
				posliefDto.setNPreisMenge2(bdGuenstigsterPreis);
			} else if (iMenge == MENGE_3) {
				posliefDto.setNPreisMenge3(bdGuenstigsterPreis);
			} else if (iMenge == MENGE_4) {
				posliefDto.setNPreisMenge4(bdGuenstigsterPreis);
			} else if (iMenge == MENGE_5) {
				posliefDto.setNPreisMenge5(bdGuenstigsterPreis);

			}

			return posliefDto;
		} else {
			return null;
		}
	}

	public PositionlieferantDto getSchnellstenLieferant(Integer einkaufsangebotpositionIId, TheClientDto theClientDto) {

		Session sessionLF = FLRSessionFactory.getFactory().openSession();
		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.einkaufsangebotposition_i_id="
				+ einkaufsangebotpositionIId;

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		Integer positionlieferantIIdZuVerwenden = null;

		Integer iKW = null;

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			if (iKW == null && poslief.getI_lieferzeitinkw() != null) {
				iKW = poslief.getI_lieferzeitinkw();
				positionlieferantIIdZuVerwenden = poslief.getI_id();
			}

			if (iKW != null && poslief.getI_lieferzeitinkw() != null) {
				if (poslief.getI_lieferzeitinkw().intValue() < iKW) {
					iKW = poslief.getI_lieferzeitinkw();
					positionlieferantIIdZuVerwenden = poslief.getI_id();
				}
			}

		}

		if (positionlieferantIIdZuVerwenden != null) {
			return positionlieferantFindByPrimaryKeyInZielWaehrung(positionlieferantIIdZuVerwenden,
					theClientDto.getSMandantenwaehrung(), theClientDto);
		} else {
			return null;
		}
	}

	public void bestelltFlagSetzen(Integer positionlieferantIId, TheClientDto theClientDto) {

		PositionlieferantDto positionlieferantDto = positionlieferantFindByPrimaryKey(positionlieferantIId);
		Session sessionLF = FLRSessionFactory.getFactory().openSession();
		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.einkaufsangebotposition_i_id="
				+ positionlieferantDto.getEinkaufsangebotpositionIId() + " AND poslief.i_id<>" + positionlieferantIId;

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			Positionlieferant positionlieferant = em.find(Positionlieferant.class, poslief.getI_id());

			if (Helper.short2boolean(positionlieferantDto.getBMenge1Bestellen())
					&& Helper.short2boolean(poslief.getB_menge1_bestellen())) {
				positionlieferant.setBMenge1Bestellen(Helper.boolean2Short(false));
			}

			if (Helper.short2boolean(positionlieferantDto.getBMenge2Bestellen())
					&& Helper.short2boolean(poslief.getB_menge2_bestellen())) {
				positionlieferant.setBMenge2Bestellen(Helper.boolean2Short(false));
			}

			if (Helper.short2boolean(positionlieferantDto.getBMenge3Bestellen())
					&& Helper.short2boolean(poslief.getB_menge3_bestellen())) {
				positionlieferant.setBMenge3Bestellen(Helper.boolean2Short(false));
			}

			if (Helper.short2boolean(positionlieferantDto.getBMenge4Bestellen())
					&& Helper.short2boolean(poslief.getB_menge4_bestellen())) {
				positionlieferant.setBMenge4Bestellen(Helper.boolean2Short(false));
			}

			if (Helper.short2boolean(positionlieferantDto.getBMenge5Bestellen())
					&& Helper.short2boolean(poslief.getB_menge5_bestellen())) {
				positionlieferant.setBMenge5Bestellen(Helper.boolean2Short(false));
			}

			em.merge(positionlieferant);
			em.flush();

		}

	}

	public void lieferantenOptimierenArtikelnummerLFSpeichern(Integer einkaufsangebotIId, TheClientDto theClientDto) {

		Session sessionLF = FLRSessionFactory.getFactory().openSession();

		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.flreinkaufsangebotposition.flreinkaufsangebot.i_id="
				+ einkaufsangebotIId;

		EinkaufsangebotDto ekagDto = einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			if ((Helper.short2boolean(poslief.getB_menge1_bestellen())
					|| Helper.short2boolean(poslief.getB_menge2_bestellen())
					|| Helper.short2boolean(poslief.getB_menge3_bestellen())
					|| Helper.short2boolean(poslief.getB_menge4_bestellen())
					|| Helper.short2boolean(poslief.getB_menge5_bestellen()))
					&& poslief.getC_artikelnrlieferant() != null
					&& poslief.getFlreinkaufsangebotposition().getFlrartikel() != null) {

				Timestamp tGueltigab = Helper.cutTimestamp(getTimestamp());

				if (poslief.getFlrekaglieferant().getT_import() != null) {
					tGueltigab = Helper
							.cutTimestamp(new Timestamp(poslief.getFlrekaglieferant().getT_import().getTime()));
				}

				ArtikellieferantDto alDto = getArtikelFac()
						.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(
								poslief.getFlreinkaufsangebotposition().getFlrartikel().getI_id(),
								poslief.getFlrekaglieferant().getLieferant_i_id(), tGueltigab, theClientDto);

				try {
					if (alDto != null) {

						if (alDto.getCArtikelnrlieferant() == null || (alDto.getCArtikelnrlieferant() != null
								&& !alDto.getCArtikelnrlieferant().equals(poslief.getC_artikelnrlieferant()))) {
							alDto.setCArtikelnrlieferant(poslief.getC_artikelnrlieferant());
							getArtikelFac().updateArtikellieferant(alDto, theClientDto);
						}

					} else {
						// Neu anlegen
						alDto = new ArtikellieferantDto();
						alDto.setArtikelIId(poslief.getFlreinkaufsangebotposition().getFlrartikel().getI_id());
						alDto.setLieferantIId(poslief.getFlrekaglieferant().getLieferant_i_id());
						alDto.setTPreisgueltigab(tGueltigab);

						BigDecimal bdPreis = null;
						if (Helper.short2boolean(poslief.getB_menge1_bestellen())) {
							bdPreis = poslief.getN_preis_menge1();
						} else if (Helper.short2boolean(poslief.getB_menge2_bestellen())) {
							bdPreis = poslief.getN_preis_menge2();
						} else if (Helper.short2boolean(poslief.getB_menge3_bestellen())) {
							bdPreis = poslief.getN_preis_menge3();
						} else if (Helper.short2boolean(poslief.getB_menge4_bestellen())) {
							bdPreis = poslief.getN_preis_menge4();
						} else if (Helper.short2boolean(poslief.getB_menge5_bestellen())) {
							bdPreis = poslief.getN_preis_menge5();
						}

						if (bdPreis != null) {

							alDto.setNEinzelpreis(bdPreis);

							alDto.setFRabatt(0D);
							alDto.setNNettopreis(bdPreis);
							alDto.setBRabattbehalten(Helper.boolean2Short(false));
							alDto.setCArtikelnrlieferant(poslief.getC_artikelnrlieferant());
							if (poslief.getN_mindestbestellmenge() != null) {
								alDto.setFMindestbestelmenge(poslief.getN_mindestbestellmenge().doubleValue());
							}
							alDto.setNVerpackungseinheit(poslief.getN_verpackungseinheit());

							alDto.setIWiederbeschaffungszeit(poslief.getI_lieferzeitinkw());
							Integer artikellieferantIId = getArtikelFac().createArtikellieferant(alDto, theClientDto);

							// Staffeln anlegen
							// 1
							if (Helper.short2boolean(poslief.getB_menge1_bestellen())
									&& poslief.getN_preis_menge1() != null
									&& poslief.getN_preis_menge1().doubleValue() > 0
									&& ekagDto.getNMenge1().doubleValue() != 0) {
								staffelAnlegen(artikellieferantIId,
										ekagDto.getNMenge1()
												.multiply(poslief.getFlreinkaufsangebotposition().getN_menge()),
										bdPreis, poslief.getN_preis_menge1(), alDto.getTPreisgueltigab(),
										poslief.getI_lieferzeitinkw(), poslief.getC_artikelnrlieferant(), theClientDto);
							}
							// 2
							if (Helper.short2boolean(poslief.getB_menge2_bestellen())
									&& poslief.getN_preis_menge2() != null
									&& poslief.getN_preis_menge2().doubleValue() > 0
									&& ekagDto.getNMenge2().doubleValue() != 0) {
								staffelAnlegen(artikellieferantIId,
										ekagDto.getNMenge2()
												.multiply(poslief.getFlreinkaufsangebotposition().getN_menge()),
										bdPreis, poslief.getN_preis_menge2(), alDto.getTPreisgueltigab(),
										poslief.getI_lieferzeitinkw(), poslief.getC_artikelnrlieferant(), theClientDto);
							}
							// 3
							if (Helper.short2boolean(poslief.getB_menge3_bestellen())
									&& poslief.getN_preis_menge3() != null
									&& poslief.getN_preis_menge3().doubleValue() > 0
									&& ekagDto.getNMenge3().doubleValue() != 0) {
								staffelAnlegen(artikellieferantIId,
										ekagDto.getNMenge3()
												.multiply(poslief.getFlreinkaufsangebotposition().getN_menge()),
										bdPreis, poslief.getN_preis_menge3(), alDto.getTPreisgueltigab(),
										poslief.getI_lieferzeitinkw(), poslief.getC_artikelnrlieferant(), theClientDto);
							}
							// 4
							if (Helper.short2boolean(poslief.getB_menge4_bestellen())
									&& poslief.getN_preis_menge4() != null
									&& poslief.getN_preis_menge4().doubleValue() > 0
									&& ekagDto.getNMenge4().doubleValue() != 0) {
								staffelAnlegen(artikellieferantIId,
										ekagDto.getNMenge4()
												.multiply(poslief.getFlreinkaufsangebotposition().getN_menge()),
										bdPreis, poslief.getN_preis_menge4(), alDto.getTPreisgueltigab(),
										poslief.getI_lieferzeitinkw(), poslief.getC_artikelnrlieferant(), theClientDto);
							}
							// 5
							if (Helper.short2boolean(poslief.getB_menge5_bestellen())
									&& poslief.getN_preis_menge5() != null
									&& poslief.getN_preis_menge5().doubleValue() > 0
									&& ekagDto.getNMenge1().doubleValue() != 0) {
								staffelAnlegen(artikellieferantIId,
										ekagDto.getNMenge5()
												.multiply(poslief.getFlreinkaufsangebotposition().getN_menge()),
										bdPreis, poslief.getN_preis_menge5(), alDto.getTPreisgueltigab(),
										poslief.getI_lieferzeitinkw(), poslief.getC_artikelnrlieferant(), theClientDto);
							}

						}

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}
	}

	public void staffelAnlegen(Integer artikellieferantIId, BigDecimal bdMenge, BigDecimal bdEinzelPreis,
			BigDecimal bdNettopreis, Timestamp tGueltigab, Integer iWBZ, String artikelnummerLF,
			TheClientDto theClientDto) throws RemoteException {
		ArtikellieferantstaffelDto staffelDto = new ArtikellieferantstaffelDto();
		staffelDto.setArtikellieferantIId(artikellieferantIId);
		staffelDto.setBRabattbehalten((short) 0);
		staffelDto.setNMenge(bdMenge);
		staffelDto.setFRabatt(0D);
		staffelDto.setNNettopreis(bdNettopreis);
		staffelDto.setTPreisgueltigab(tGueltigab);

		staffelDto.setCArtikelnrlieferant(artikelnummerLF);

		staffelDto.setIWiederbeschaffungszeit(iWBZ);

		BigDecimal bdRabattsumme = bdEinzelPreis.subtract(bdNettopreis);

		BigDecimal bdRabattsatz = new BigDecimal(0);
		if (bdEinzelPreis.doubleValue() != 0) {
			bdRabattsatz = bdRabattsumme.divide(bdEinzelPreis, 4, BigDecimal.ROUND_HALF_EVEN).movePointRight(2);

		}

		staffelDto.setFRabatt(bdRabattsatz.doubleValue());

		staffelDto.setBRabattbehalten(Helper.boolean2Short(false));

		getArtikelFac().createArtikellieferantstaffel(staffelDto, theClientDto);
	}

	public void lieferantenOptimierenbestellen(EkagLieferantoptimierenDto ekagLieferantoptimierenDto, int iMenge,
			boolean bZuruecknehmen, TheClientDto theClientDto) {

		// Immer vorher alle Loeschen
		Integer einaufsangebotIId = ekagLieferantoptimierenDto.getEkagDto().getIId();

		Session sessionLF = FLRSessionFactory.getFactory().openSession();

		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.flreinkaufsangebotposition.flreinkaufsangebot.i_id="
				+ einaufsangebotIId;

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			PositionlieferantDto posliefDto = positionlieferantFindByPrimaryKey(poslief.getI_id());

			if (iMenge == MENGE_1) {
				posliefDto.setBMenge1Bestellen(Helper.boolean2Short(false));
			}
			if (iMenge == MENGE_2) {
				posliefDto.setBMenge2Bestellen(Helper.boolean2Short(false));
			}
			if (iMenge == MENGE_3) {
				posliefDto.setBMenge3Bestellen(Helper.boolean2Short(false));
			}
			if (iMenge == MENGE_4) {
				posliefDto.setBMenge4Bestellen(Helper.boolean2Short(false));
			}
			if (iMenge == MENGE_5) {
				posliefDto.setBMenge5Bestellen(Helper.boolean2Short(false));
			}

			updatePositionlieferant(posliefDto, false, theClientDto);

		}

		// Und dann neu eintragen
		if (bZuruecknehmen == false) {

			int iAnzahl = ekagLieferantoptimierenDto.getPos().size();

			for (int i = 0; i < iAnzahl; i++) {
				EinkaufsangebotpositionDtoFuerOptimieren posDto = ekagLieferantoptimierenDto.getPos().get(i);

				// 1
				if (iMenge == MENGE_1) {
					if (posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1() != null) {

						Positionlieferant positionlieferant = em.find(Positionlieferant.class,
								posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge1());
						positionlieferant.setBMenge1Bestellen(Helper.boolean2Short(true));
						em.merge(positionlieferant);
						em.flush();

					} else {
						if (posDto.getGuenstigsterLieferantMenge1() != null) {
							PositionlieferantDto posliefDto = posDto.getGuenstigsterLieferantMenge1();

							Positionlieferant positionlieferant = em.find(Positionlieferant.class, posliefDto.getIId());
							positionlieferant.setBMenge1Bestellen(Helper.boolean2Short(true));
							em.merge(positionlieferant);
							em.flush();
						}
					}
				}
				// 2
				if (iMenge == MENGE_2) {
					if (posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2() != null) {

						Positionlieferant positionlieferant = em.find(Positionlieferant.class,
								posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge2());
						positionlieferant.setBMenge2Bestellen(Helper.boolean2Short(true));
						em.merge(positionlieferant);
						em.flush();

					} else {
						if (posDto.getGuenstigsterLieferantMenge2() != null) {
							PositionlieferantDto posliefDto = posDto.getGuenstigsterLieferantMenge2();

							Positionlieferant positionlieferant = em.find(Positionlieferant.class, posliefDto.getIId());
							positionlieferant.setBMenge2Bestellen(Helper.boolean2Short(true));
							em.merge(positionlieferant);
							em.flush();
						}
					}
				}

				// 3
				if (iMenge == MENGE_3) {
					if (posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3() != null) {

						Positionlieferant positionlieferant = em.find(Positionlieferant.class,
								posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge3());
						positionlieferant.setBMenge3Bestellen(Helper.boolean2Short(true));
						em.merge(positionlieferant);
						em.flush();

					} else {
						if (posDto.getGuenstigsterLieferantMenge3() != null) {
							PositionlieferantDto posliefDto = posDto.getGuenstigsterLieferantMenge3();

							Positionlieferant positionlieferant = em.find(Positionlieferant.class, posliefDto.getIId());
							positionlieferant.setBMenge3Bestellen(Helper.boolean2Short(true));
							em.merge(positionlieferant);
							em.flush();
						}
					}
				}

				// 4
				if (iMenge == MENGE_4) {
					if (posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4() != null) {

						Positionlieferant positionlieferant = em.find(Positionlieferant.class,
								posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge4());
						positionlieferant.setBMenge4Bestellen(Helper.boolean2Short(true));
						em.merge(positionlieferant);
						em.flush();

					} else {
						if (posDto.getGuenstigsterLieferantMenge4() != null) {
							PositionlieferantDto posliefDto = posDto.getGuenstigsterLieferantMenge4();

							Positionlieferant positionlieferant = em.find(Positionlieferant.class, posliefDto.getIId());
							positionlieferant.setBMenge4Bestellen(Helper.boolean2Short(true));
							em.merge(positionlieferant);
							em.flush();
						}
					}
				}

				// 5
				if (iMenge == MENGE_5) {
					if (posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5() != null) {

						Positionlieferant positionlieferant = em.find(Positionlieferant.class,
								posDto.getEinkaufsangebotpositionDto().getPositionlieferantIIdUebersteuertMenge5());
						positionlieferant.setBMenge5Bestellen(Helper.boolean2Short(true));
						em.merge(positionlieferant);
						em.flush();

					} else {
						if (posDto.getGuenstigsterLieferantMenge5() != null) {
							PositionlieferantDto posliefDto = posDto.getGuenstigsterLieferantMenge5();

							Positionlieferant positionlieferant = em.find(Positionlieferant.class, posliefDto.getIId());
							positionlieferant.setBMenge5Bestellen(Helper.boolean2Short(true));
							em.merge(positionlieferant);
							em.flush();
						}
					}
				}
			}

		}

	}

	public EkagLieferantoptimierenDto getEkagLieferantoptimierenDto(Integer einkaufsangebotIId, Integer iLieferzeitInKW,
			int iMenge, boolean bMindestbestellmengeBeruecksichtigen, boolean bVerpackungseinheitBeruecksichtigen,
			int sortierung, String filterArtikelnummer, String filterArtikelbezeichnung, TheClientDto theClientDto) {

		EinkaufsangebotDto ekagDto = einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		EkagLieferantoptimierenDto dto = new EkagLieferantoptimierenDto(ekagDto);
		dto.setEkagDto(ekagDto);

		Query query = em.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
		query.setParameter(1, einkaufsangebotIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> it = cl.iterator();

		ArrayList al = new ArrayList();
		while (it.hasNext()) {
			Einkaufsangebotposition bean = (Einkaufsangebotposition) it.next();

			EinkaufsangebotpositionDtoFuerOptimieren optDto = new EinkaufsangebotpositionDtoFuerOptimieren();

			EinkaufsangebotpositionDto ekagPosDto = (EinkaufsangebotpositionDto) assembleEinkaufsangebotpositionDto(
					bean);

			String artikel = "";
			String artikelBez = "";
			String artikelZbez = "";

			if (ekagPosDto.getPositionsartCNr().equals(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE)) {

				artikelBez = ekagPosDto.getCBez();
				artikelZbez = ekagPosDto.getCZusatzbez();

			} else {

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(ekagPosDto.getArtikelIId(),
						theClientDto);

				artikel = artikelDto.getCNr();
				artikelBez = artikelDto.getCBezAusSpr();
				artikelZbez = artikelDto.getCZBezAusSpr();
			}

			if (filterArtikelnummer != null && filterArtikelnummer.length() > 0) {
				String krit = filterArtikelnummer.trim().toLowerCase();

				String suchstring = "";

				if (artikel != null && artikel.length() > 0) {
					suchstring = artikel.toLowerCase();
				}

				if (krit.startsWith("%")) {
					if (!suchstring.endsWith(krit.substring(1))) {
						continue;
					}
				} else {

					if (!suchstring.contains(krit)) {
						continue;
					}
				}
			}

			if (filterArtikelbezeichnung != null && filterArtikelbezeichnung.length() > 0) {
				String krit = filterArtikelbezeichnung.trim().toLowerCase();

				String suchstring = (artikelBez + " " + artikelZbez).toLowerCase();
				if (krit.startsWith("%")) {
					if (!suchstring.endsWith(krit.substring(1))) {
						continue;
					}
				} else {
					if (!suchstring.contains(krit)) {
						continue;
					}
				}

			}

			optDto.setEinkaufsangebotpositionDto(ekagPosDto);

			PositionlieferantDto poslief1 = getGuenstigstenLieferant(bean.getIId(), MENGE_1, iLieferzeitInKW,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, theClientDto);
			PositionlieferantDto poslief2 = getGuenstigstenLieferant(bean.getIId(), MENGE_2, iLieferzeitInKW,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, theClientDto);
			PositionlieferantDto poslief3 = getGuenstigstenLieferant(bean.getIId(), MENGE_3, iLieferzeitInKW,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, theClientDto);
			PositionlieferantDto poslief4 = getGuenstigstenLieferant(bean.getIId(), MENGE_4, iLieferzeitInKW,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, theClientDto);
			PositionlieferantDto poslief5 = getGuenstigstenLieferant(bean.getIId(), MENGE_5, iLieferzeitInKW,
					bMindestbestellmengeBeruecksichtigen, bVerpackungseinheitBeruecksichtigen, theClientDto);

			if (iLieferzeitInKW == null || iLieferzeitInKW != null && iMenge == MENGE_1) {
				optDto.setGuenstigsterLieferantMenge1(poslief1);
			}

			if (iLieferzeitInKW == null || iLieferzeitInKW != null && iMenge == MENGE_2) {
				optDto.setGuenstigsterLieferantMenge2(poslief2);
			}
			if (iLieferzeitInKW == null || iLieferzeitInKW != null && iMenge == MENGE_3) {
				optDto.setGuenstigsterLieferantMenge3(poslief3);
			}
			if (iLieferzeitInKW == null || iLieferzeitInKW != null && iMenge == MENGE_4) {
				optDto.setGuenstigsterLieferantMenge4(poslief4);
			}
			if (iLieferzeitInKW == null || iLieferzeitInKW != null && iMenge == MENGE_5) {
				optDto.setGuenstigsterLieferantMenge5(poslief5);
			}

			al.add(optDto);

		}

		if (sortierung > SORTIERUNG_WIE_ERFASST) {

			for (int m = al.size() - 1; m > 0; --m) {
				for (int n = 0; n < m; ++n) {
					EinkaufsangebotpositionDtoFuerOptimieren o1 = (EinkaufsangebotpositionDtoFuerOptimieren) al.get(n);
					EinkaufsangebotpositionDtoFuerOptimieren o2 = (EinkaufsangebotpositionDtoFuerOptimieren) al
							.get(n + 1);

					String s1 = "";
					String s2 = "";

					if (sortierung == SORTIERUNG_ARTIKELNUMMER) {

						if (o1.getEinkaufsangebotpositionDto().getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									o1.getEinkaufsangebotpositionDto().getArtikelIId(), theClientDto);
							s1 = aDto.getCNr();
						}
						if (o2.getEinkaufsangebotpositionDto().getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									o2.getEinkaufsangebotpositionDto().getArtikelIId(), theClientDto);
							s2 = aDto.getCNr();
						}

					}
					if (sortierung == SORTIERUNG_BEZEICHNUNG) {

						if (o1.getEinkaufsangebotpositionDto().getPositionsartCNr()
								.equals(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE)) {
							s1 = o1.getEinkaufsangebotpositionDto().getCBez();
						} else if (o1.getEinkaufsangebotpositionDto().getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									o1.getEinkaufsangebotpositionDto().getArtikelIId(), theClientDto);
							s1 = aDto.getCBezAusSpr();
						}

						if (o2.getEinkaufsangebotpositionDto().getPositionsartCNr()
								.equals(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE)) {
							s2 = o2.getEinkaufsangebotpositionDto().getCBez();
						} else if (o2.getEinkaufsangebotpositionDto().getArtikelIId() != null) {
							ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
									o2.getEinkaufsangebotpositionDto().getArtikelIId(), theClientDto);
							s2 = aDto.getCBezAusSpr();
						}

					}
					if (sortierung == SORTIERUNG_HERSTELLERNUMER) {
						s1 = o1.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
						s2 = o2.getEinkaufsangebotpositionDto().getCArtikelnrhersteller();
					}

					if (sortierung == SORTIERUNG_GUENST_PREIS1) {
						if (o1.getGuenstigsterLieferantMenge1() != null
								&& o1.getGuenstigsterLieferantMenge1().getNPreisMenge1() != null) {
							s1 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o1.getGuenstigsterLieferantMenge1().getNPreisMenge1(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
						if (o2.getGuenstigsterLieferantMenge1() != null
								&& o2.getGuenstigsterLieferantMenge1().getNPreisMenge1() != null) {
							s2 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o2.getGuenstigsterLieferantMenge1().getNPreisMenge1(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
					}

					if (sortierung == SORTIERUNG_GUENST_PREIS2) {
						if (o1.getGuenstigsterLieferantMenge2() != null
								&& o1.getGuenstigsterLieferantMenge2().getNPreisMenge2() != null) {
							s1 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o1.getGuenstigsterLieferantMenge2().getNPreisMenge2(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
						if (o2.getGuenstigsterLieferantMenge2() != null
								&& o2.getGuenstigsterLieferantMenge2().getNPreisMenge2() != null) {
							s2 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o2.getGuenstigsterLieferantMenge2().getNPreisMenge2(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
					}

					if (sortierung == SORTIERUNG_GUENST_PREIS3) {
						if (o1.getGuenstigsterLieferantMenge3() != null
								&& o1.getGuenstigsterLieferantMenge3().getNPreisMenge3() != null) {
							s1 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o1.getGuenstigsterLieferantMenge3().getNPreisMenge3(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
						if (o2.getGuenstigsterLieferantMenge3() != null
								&& o2.getGuenstigsterLieferantMenge3().getNPreisMenge3() != null) {
							s2 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o2.getGuenstigsterLieferantMenge3().getNPreisMenge3(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
					}

					if (sortierung == SORTIERUNG_GUENST_PREIS4) {
						if (o1.getGuenstigsterLieferantMenge4() != null
								&& o1.getGuenstigsterLieferantMenge4().getNPreisMenge4() != null) {
							s1 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o1.getGuenstigsterLieferantMenge4().getNPreisMenge4(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
						if (o2.getGuenstigsterLieferantMenge4() != null
								&& o2.getGuenstigsterLieferantMenge4().getNPreisMenge4() != null) {
							s2 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o2.getGuenstigsterLieferantMenge4().getNPreisMenge4(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
					}

					if (sortierung == SORTIERUNG_GUENST_PREIS5) {
						if (o1.getGuenstigsterLieferantMenge5() != null
								&& o1.getGuenstigsterLieferantMenge5().getNPreisMenge5() != null) {
							s1 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o1.getGuenstigsterLieferantMenge5().getNPreisMenge5(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
						if (o2.getGuenstigsterLieferantMenge5() != null
								&& o2.getGuenstigsterLieferantMenge5().getNPreisMenge5() != null) {
							s2 = Helper.fitString2LengthAlignRight(
									Helper.formatZahl(o2.getGuenstigsterLieferantMenge5().getNPreisMenge5(), 6,
											theClientDto.getLocUi()),
									20, ' ');
						}
					}

					if (s1 == null) {
						s1 = "";
					}
					if (s2 == null) {
						s2 = "";
					}

					if (s1.compareTo(s2) > 0) {
						al.set(n, o2);
						al.set(n + 1, o1);
					}

				}
			}

		}

		dto.setPos(al);

		return dto;
	}

	public PositionlieferantDto getBestelltLieferant(Integer positionlieferantIId, int iMenge,
			TheClientDto theClientDto) {

		PositionlieferantDto positionlieferantDto = positionlieferantFindByPrimaryKey(positionlieferantIId);

		Session sessionLF = FLRSessionFactory.getFactory().openSession();
		String queryStringLF = "SELECT poslief FROM FLRPositionlieferant AS poslief WHERE poslief.einkaufsangebotposition_i_id="
				+ positionlieferantDto.getEinkaufsangebotpositionIId();

		if (iMenge == MENGE_1) {
			queryStringLF += " AND b_menge1_bestellen=1";
		} else if (iMenge == MENGE_2) {
			queryStringLF += " AND b_menge2_bestellen=1";
		} else if (iMenge == MENGE_3) {
			queryStringLF += " AND b_menge3_bestellen=1";
		} else if (iMenge == MENGE_4) {
			queryStringLF += " AND b_menge4_bestellen=1";
		} else if (iMenge == MENGE_5) {
			queryStringLF += " AND b_menge5_bestellen=1";
		}

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		while (resultListIteratorLF.hasNext()) {
			FLRPositionlieferant poslief = (FLRPositionlieferant) resultListIteratorLF.next();

			return positionlieferantFindByPrimaryKeyInZielWaehrung(poslief.getI_id(),
					theClientDto.getSMandantenwaehrung(), theClientDto);

		}

		return null;
	}

	public void preiseDerMengenstaffelnNeuKalkulieren(Integer agstklIId, TheClientDto theClientDto) {
		AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
		query.setParameter(1, agstklIId);

		Collection c = query.getResultList();
		Iterator it = c.iterator();

		while (it.hasNext()) {

			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();

			agstklmengenstaffel.setNMaterialeinsatzLief1(
					getAngebotstklFac().getWareneinsatzLief1(agstklmengenstaffel.getNMenge(), agstklIId, theClientDto));
			agstklmengenstaffel.setNAzeinsatzLief1(
					getAngebotstklFac().getAZeinsatzLief1(agstklmengenstaffel.getNMenge(), agstklIId, theClientDto));

			BigDecimal[] vkpreise = getVKPreis(agstklmengenstaffel.getNMenge(), agstklIId, theClientDto);

			agstklmengenstaffel.setNVkpreis(vkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS]);

			// auskommentiert aufgrund SP5252 Gewaehlter Preis muss erhalten
			// bleiben
			// agstklmengenstaffel
			// .setNVkpreisGewaehlt(vkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG]);

			em.merge(agstklmengenstaffel);
			em.flush();

		}

		agstklDto.setBDatengeaendert(Helper.boolean2Short(false));
		updateAgstkl(agstklDto, theClientDto);
	}

	public BigDecimal getVKPreisGewaehlt(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto) {

		BigDecimal bdVkPreisGewaehlt = null;

		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdKleinerGleichNMenge");
		query.setParameter(1, agstklIId);
		query.setParameter(2, bdMenge);

		query.setMaxResults(1);
		Collection c = query.getResultList();
		Iterator it = c.iterator();

		if (it.hasNext()) {
			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();
			return agstklmengenstaffel.getNVkpreisGewaehlt();
		}
		return bdVkPreisGewaehlt;
	}

	public BigDecimal[] getVKPreis(BigDecimal bdMenge, Integer agstklIId, TheClientDto theClientDto) {
		BigDecimal bdVkPreisAnhandPositionspreisPlusAufschlag = BigDecimal.ZERO;
		BigDecimal bdVkPreisAnhandKundenpreisfindung = BigDecimal.ZERO;

		try {

			AgstklDto agstklDto = agstklFindByPrimaryKey(agstklIId);

			AgstklpositionDto[] dtos = getAngebotstklpositionFac().agstklpositionFindByAgstklIId(agstklIId,
					theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(agstklDto.getKundeIId(), theClientDto);

			MwstsatzDto mwstsatzKunde = kundeDto.getMwstsatzbezIId() != null
					? getMandantFac().mwstsatzFindZuDatum(kundeDto.getMwstsatzbezIId(), agstklDto.getTBelegdatum())
					: null;
			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				BigDecimal bdFaktorFuerRuestmenge = BigDecimal.ONE;

				if (Helper.short2boolean(dto.getBRuestmenge()) && bdMenge.doubleValue() != 0) {
					bdFaktorFuerRuestmenge = BigDecimal.ONE.divide(bdMenge, 4, BigDecimal.ROUND_HALF_EVEN);
				}

				if (dto.getArtikelIId() != null && dto.getNMenge() != null
						&& dto.getNNettogesamtmitaufschlag() != null) {

					BigDecimal kundenVKPreisProEinheit = BigDecimal.ZERO;
					if (kundeDto.getMwstsatzbezIId() != null) {
						VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(dto.getArtikelIId(),
								agstklDto.getKundeIId(),
								bdMenge.multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge),
								new Date(agstklDto.getTBelegdatum().getTime()),
								kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//										getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//												kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
								mwstsatzKunde.getIId(), agstklDto.getWaehrungCNr(), theClientDto);

						VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

						if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {
							kundenVKPreisProEinheit = kundenVKPreisDto.nettopreis;
						}
					}

					if (dto.getAgstklpositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
						bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
								.add(dto.getNNettogesamtmitaufschlag().multiply(dto.getNMenge())
										.multiply(bdFaktorFuerRuestmenge));

					} else {
						bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung.add(
								kundenVKPreisProEinheit.multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge));

					}
					bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag.add(dto
							.getNNettogesamtmitaufschlag().multiply(dto.getNMenge()).multiply(bdFaktorFuerRuestmenge));

				}
			}

			Query query = em.createNamedQuery("AgstklarbeitsplanFindByAgstklIId");
			query.setParameter(1, agstklIId);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			while (it.hasNext()) {
				Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();

				BigDecimal bdGesamtzeit = Helper.berechneGesamtzeitInStunden(ap.getLRuestzeit(), ap.getLStueckzeit(),
						bdMenge, new BigDecimal(1), ap.getIAufspannung());

				if (bdGesamtzeit != null) {

					BigDecimal bdZeitProStueck = bdGesamtzeit;
					if (bdMenge.doubleValue() != 0) {

						bdZeitProStueck = bdGesamtzeit.divide(bdMenge, 4, BigDecimal.ROUND_HALF_UP);
					}

					if (Helper.short2boolean(ap.getBNurmaschinenzeit()) == false) {

						if (kundeDto.getMwstsatzbezIId() != null) {
							VkpreisfindungDto vkpreisDto = getVkPreisfindungFac().verkaufspreisfindung(
									ap.getArtikelIId(), agstklDto.getKundeIId(), bdGesamtzeit,
									new Date(agstklDto.getTBelegdatum().getTime()),
									kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//									getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(
//											kundeDto.getMwstsatzbezIId(), theClientDto).getIId(),
									mwstsatzKunde.getIId(), agstklDto.getWaehrungCNr(), theClientDto);

							VerkaufspreisDto kundenVKPreisDto = Helper.getVkpreisBerechnet(vkpreisDto);

							if (kundenVKPreisDto != null && kundenVKPreisDto.nettopreis != null) {

								bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag
										.add(kundenVKPreisDto.nettopreis.multiply(bdZeitProStueck));

								bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
										.add(kundenVKPreisDto.nettopreis.multiply(bdZeitProStueck));

							}
						}
					}

					if (ap.getMaschineIId() != null) {
						BigDecimal kostenVK = getZeiterfassungFac().getMaschinenKostenZumZeitpunkt(ap.getMaschineIId(),
								agstklDto.getTBelegdatum(), LocaleFac.BELEGART_AGSTUECKLISTE, ap.getIId())
								.getBdStundensatzVK();

						if (kostenVK != null) {
							bdVkPreisAnhandPositionspreisPlusAufschlag = bdVkPreisAnhandPositionspreisPlusAufschlag
									.add(kostenVK.multiply(bdZeitProStueck));
							bdVkPreisAnhandKundenpreisfindung = bdVkPreisAnhandKundenpreisfindung
									.add(kostenVK.multiply(bdZeitProStueck));

						}

					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return new BigDecimal[] { bdVkPreisAnhandPositionspreisPlusAufschlag, bdVkPreisAnhandKundenpreisfindung };

	}

	public void updateAufschlag(AufschlagDto aufschlagDto, TheClientDto theClientDto) {

		Aufschlag aufschlag = em.find(Aufschlag.class, aufschlagDto.getIId());

		try {
			Query query = em.createNamedQuery("AufschlagFindByMandantCNrCBez");
			query.setParameter(1, aufschlagDto.getMandantCNr());
			query.setParameter(2, aufschlagDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Aufschlag) query.getSingleResult()).getIId();
			if (aufschlagDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_AUFSCHLAG.UK"));
			}

		} catch (NoResultException ex) {

		}

		setAufschlagFromAufschlagDto(aufschlag, aufschlagDto);
	}

	public void updateEkgruppe(EkgruppeDto ekgruppeDto, TheClientDto theClientDto) {

		Ekgruppe ekgruppe = em.find(Ekgruppe.class, ekgruppeDto.getIId());

		try {
			Query query = em.createNamedQuery("EkgruppeFindByMandantCNrCBez");
			query.setParameter(1, ekgruppeDto.getMandantCNr());
			query.setParameter(2, ekgruppeDto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Ekgruppe) query.getSingleResult()).getIId();
			if (ekgruppeDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_EKGRUPPE.UK"));
			}

		} catch (NoResultException ex) {

		}

		setEkgruppeFromEkgruppeDto(ekgruppe, ekgruppeDto);
	}

	public void updateEkgruppelieferant(EkgruppelieferantDto ekgruppelieferantDto, TheClientDto theClientDto) {

		Ekgruppelieferant ekgruppelieferant = em.find(Ekgruppelieferant.class, ekgruppelieferantDto.getIId());

		try {
			Query query = em.createNamedQuery("EkgruppelieferantFindByEkgruppeIIdLieferantIId");
			query.setParameter(1, ekgruppelieferantDto.getEkgruppeIId());
			query.setParameter(2, ekgruppelieferantDto.getLieferantIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Ekgruppelieferant) query.getSingleResult()).getIId();
			if (ekgruppelieferantDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("AS_EKGRUPPELIEFERANT.UK"));
			}

		} catch (NoResultException ex) {

		}

		setEkgruppelieferantFromEkgruppelieferantDto(ekgruppelieferant, ekgruppelieferantDto);
	}

	public void updateEkaglieferant(EkaglieferantDto ekaglieferantDto, TheClientDto theClientDto) {

		Ekaglieferant ekaglieferant = em.find(Ekaglieferant.class, ekaglieferantDto.getIId());

		try {
			Query query = em.createNamedQuery("EkaglieferantFindByEinkaufsangebotIIdLieferantIId");
			query.setParameter(1, ekaglieferantDto.getEinkaufsangebotIId());
			query.setParameter(2, ekaglieferantDto.getLieferantIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Ekaglieferant) query.getSingleResult()).getIId();
			if (ekaglieferantDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("AS_EKGRUPPELIEFERANT.UK"));
			}

		} catch (NoResultException ex) {

		}

		setEkaglieferantFromEkgruppelieferantDto(ekaglieferant, ekaglieferantDto);
	}

	public void updatePositionlieferant(PositionlieferantDto positionlieferantDto, TheClientDto theClientDto) {
		updatePositionlieferant(positionlieferantDto, true, theClientDto);
	}

	public void updatePositionlieferant(PositionlieferantDto positionlieferantDto, boolean bestelltFlagSetzen,
			TheClientDto theClientDto) {

		Positionlieferant positionlieferant = em.find(Positionlieferant.class, positionlieferantDto.getIId());

		positionlieferantDto.setTAendern(getTimestamp());

		try {
			Query query = em.createNamedQuery("PositionlieferantfindByEinkaufsangebotpositionIIdEgaklieferantIId");
			query.setParameter(1, positionlieferantDto.getEinkaufsangebotpositionIId());
			query.setParameter(2, positionlieferantDto.getEgaklieferantIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Positionlieferant) query.getSingleResult()).getIId();
			if (positionlieferantDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("AS_POSITIONLIEFERANT.UK"));
			}

		} catch (NoResultException ex) {

		}

		setPositionlieferantFromPositionlieferantDto(positionlieferant, positionlieferantDto);

		if (bestelltFlagSetzen) {
			bestelltFlagSetzen(positionlieferant.getIId(), theClientDto);
		}

	}

	public boolean sindMengenstaffelnvorhandenUndPreiseFixiert(Integer iIdAngebotstkl) {

		boolean b = false;

		Agstkl agstkl = em.find(Agstkl.class, iIdAngebotstkl);

		// Wenn das Flag schon einmal gesetzt wurde, dann Meldung nicht mehr
		// anzeigen
		if (Helper.short2boolean(agstkl.getBDatengeaendert()) == true) {
			return false;
		}

		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
		query.setParameter(1, iIdAngebotstkl);

		Collection c = query.getResultList();
		Iterator it = c.iterator();

		while (it.hasNext()) {

			Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();

			if (agstklmengenstaffel.getNVkpreisGewaehlt() != null) {
				return true;
			}

		}

		return b;
	}

	public void updateAgstklmengenstaffel(AgstklmengenstaffelDto agstklmengenstaffelDto, TheClientDto theClientDto) {

		Agstklmengenstaffel agstklmengenstaffel = em.find(Agstklmengenstaffel.class, agstklmengenstaffelDto.getIId());

		try {
			Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelDto.getNMenge());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Agstklmengenstaffel) query.getSingleResult()).getIId();
			if (agstklmengenstaffelDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("AS_AGSTKLMENGENSTAFFEL.UK"));
			}

		} catch (NoResultException ex) {

		}
		agstklmengenstaffelDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		agstklmengenstaffelDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		setAgstklmengenstaffelFromAgstklmengenstaffelDto(agstklmengenstaffel, agstklmengenstaffelDto);
	}

	public void updateAgstklmengenstaffelSchnellerfassung(AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelDto,
			TheClientDto theClientDto) {

		AgstklmengenstaffelSchnellerfassung agstklmengenstaffel = em.find(AgstklmengenstaffelSchnellerfassung.class,
				agstklmengenstaffelDto.getIId());

		try {
			Query query = em.createNamedQuery("AgstklmengenstaffelSchnellerfassungFindByAgstklIIdNMenge");
			query.setParameter(1, agstklmengenstaffelDto.getAgstklIId());
			query.setParameter(2, agstklmengenstaffelDto.getNMenge());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((AgstklmengenstaffelSchnellerfassung) query.getSingleResult()).getIId();
			if (agstklmengenstaffelDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("AS_AGSTKLMENGENSTAFFEL_SCHNELLERFASSUNG.UK"));
			}

		} catch (NoResultException ex) {

		}

		agstklmengenstaffel.setNAufschlagAz(agstklmengenstaffelDto.getNAufschlagAz());
		agstklmengenstaffel.setNAufschlagMaterial(agstklmengenstaffelDto.getNAufschlagMaterial());
		agstklmengenstaffel.setNMenge(agstklmengenstaffelDto.getNMenge());
		agstklmengenstaffel.setNWertAz(agstklmengenstaffelDto.getNWertAz());
		agstklmengenstaffel.setNWertMaterial(agstklmengenstaffelDto.getNWertMaterial());
		agstklmengenstaffel.setNPreisEinheit(agstklmengenstaffelDto.getNPreisEinheit());

	}

	public AufschlagDto aufschlagFindByPrimaryKey(Integer iId) {
		Aufschlag aufschlag = em.find(Aufschlag.class, iId);
		return AufschlagDtoAssembler.createDto(aufschlag);
	}

	public EkgruppeDto ekgruppeFindByPrimaryKey(Integer iId) {
		Ekgruppe aufschlag = em.find(Ekgruppe.class, iId);
		return EkgruppeDtoAssembler.createDto(aufschlag);
	}

	public EkgruppelieferantDto ekgruppelieferantFindByPrimaryKey(Integer iId) {
		Ekgruppelieferant bean = em.find(Ekgruppelieferant.class, iId);
		return EkgruppelieferantDtoAssembler.createDto(bean);
	}

	public EkaglieferantDto ekaglieferantFindByPrimaryKey(Integer iId) {
		Ekaglieferant bean = em.find(Ekaglieferant.class, iId);
		return EkaglieferantDtoAssembler.createDto(bean);
	}

	public EkaglieferantDto[] ekaglieferantFindByEinkaufsangebotIId(Integer einkaufsangebotIId) {

		Query query = em.createNamedQuery("EkaglieferantFindByEinkaufsangebotIId");
		query.setParameter(1, einkaufsangebotIId);
		Collection c = query.getResultList();

		return EkaglieferantDtoAssembler.createDtos(c);
	}

	public PositionlieferantDto positionlieferantFindByPrimaryKey(Integer iId) {
		Positionlieferant bean = em.find(Positionlieferant.class, iId);
		return PositionlieferantDtoAssembler.createDto(bean);
	}

	public PositionlieferantDto positionlieferantFindByPrimaryKeyInZielWaehrung(Integer iId, String zielwaehrungCNr,
			TheClientDto theClientDto) {
		Positionlieferant bean = em.find(Positionlieferant.class, iId);

		// Noch in MandantenwaehrungUmrechnen
		Ekaglieferant ekaglieferant = em.find(Ekaglieferant.class, bean.getEgaklieferantIId());

		String waehrung = ekaglieferant.getWaehrungCNr();

		PositionlieferantDto dto = PositionlieferantDtoAssembler.createDto(bean);
		if (!waehrung.equals(zielwaehrungCNr)) {

			try {
				int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

				if (dto.getNPreisMenge1() != null) {

					BigDecimal aufschlag = Helper.getProzentWert(dto.getNPreisMenge1(), ekaglieferant.getNAufschlag(),
							iNachkommastellenPreis);

					BigDecimal bdPreis = Helper
							.rundeKaufmaennisch(
									getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNPreisMenge1().add(aufschlag),
											waehrung, zielwaehrungCNr, getDate(), theClientDto),
									iNachkommastellenPreis);

					dto.setNPreisMenge1(bdPreis);
				}
				if (dto.getNPreisMenge2() != null) {

					BigDecimal aufschlag = Helper.getProzentWert(dto.getNPreisMenge2(), ekaglieferant.getNAufschlag(),
							iNachkommastellenPreis);

					BigDecimal bdPreis = Helper
							.rundeKaufmaennisch(
									getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNPreisMenge2().add(aufschlag),
											waehrung, zielwaehrungCNr, getDate(), theClientDto),
									iNachkommastellenPreis);

					dto.setNPreisMenge2(bdPreis);

				}
				if (dto.getNPreisMenge3() != null) {

					BigDecimal aufschlag = Helper.getProzentWert(dto.getNPreisMenge3(), ekaglieferant.getNAufschlag(),
							iNachkommastellenPreis);

					BigDecimal bdPreis = Helper
							.rundeKaufmaennisch(
									getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNPreisMenge3().add(aufschlag),
											waehrung, zielwaehrungCNr, getDate(), theClientDto),
									iNachkommastellenPreis);

					dto.setNPreisMenge3(bdPreis);

				}
				if (dto.getNPreisMenge4() != null) {

					BigDecimal aufschlag = Helper.getProzentWert(dto.getNPreisMenge4(), ekaglieferant.getNAufschlag(),
							iNachkommastellenPreis);

					BigDecimal bdPreis = Helper
							.rundeKaufmaennisch(
									getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNPreisMenge4().add(aufschlag),
											waehrung, zielwaehrungCNr, getDate(), theClientDto),
									iNachkommastellenPreis);

					dto.setNPreisMenge4(bdPreis);

				}
				if (dto.getNPreisMenge5() != null) {

					BigDecimal aufschlag = Helper.getProzentWert(dto.getNPreisMenge5(), ekaglieferant.getNAufschlag(),
							iNachkommastellenPreis);

					BigDecimal bdPreis = Helper
							.rundeKaufmaennisch(
									getLocaleFac().rechneUmInAndereWaehrungZuDatum(dto.getNPreisMenge5().add(aufschlag),
											waehrung, zielwaehrungCNr, getDate(), theClientDto),
									iNachkommastellenPreis);

					dto.setNPreisMenge5(bdPreis);
				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return dto;
	}

	public Integer createAngebotstuecklisteAusSchnellerfassungUndErzeugeAngebotsposition(AgstklDto agstklDto,
			ArrayList<AgstklarbeitsplanDto> agstklarbeitsplanDtos, ArrayList<AgstklpositionDto> agstklpositionDtos,
			ArrayList<AgstklmengenstaffelSchnellerfassungDto> agstklmengenstaffelDtos,
			ArrayList<AgstklmaterialDto> agstklmaterialDtos, Integer angebotIId, BigDecimal bdMenge, BigDecimal bdPreis,
			int iDialoghoehe, AngebotpositionDto angebotpositionDtoVorhanden, TheClientDto theClientDto) {

		// Dialoghoehe zureuckspeichern
		Agstkl agstkl = em.find(Agstkl.class, agstklDto.getIId());
		agstkl.setIHoeheDialog(iDialoghoehe);

		Integer agstklIId = agstklDto.getIId();

		if (angebotpositionDtoVorhanden == null) {

			agstklDto.setIId(null);
			agstklDto.setBVorlage(Helper.boolean2Short(false));
			agstklDto.setCNr(null);
			agstklIId = createAgstkl(agstklDto, theClientDto);
		} else {
			updateAgstkl(agstklDto);
		}

		if (angebotpositionDtoVorhanden != null) {
			// Vorher alle loeschen
			try {
				AgstklpositionDto[] agstklpositionDtosVorhanden = getAngebotstklpositionFac()
						.agstklpositionFindByAgstklIId(agstklDto.getIId(), theClientDto);
				for (int j = 0; j < agstklpositionDtosVorhanden.length; j++) {
					getAngebotstklpositionFac().removeAgstklposition(agstklpositionDtosVorhanden[j], theClientDto);
				}

				ArrayList<AgstklmaterialDto> agstklmaterialDtosVorhanden = getAngebotstklpositionFac()
						.agstklmaterialFindByAgstklIId(agstklDto.getIId(), theClientDto);
				for (int j = 0; j < agstklmaterialDtosVorhanden.size(); j++) {
					getAngebotstklpositionFac().removeAgstklmaterial(agstklmaterialDtosVorhanden.get(j), theClientDto);
				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			AgstklarbeitsplanDto[] aAgstklarbeitsplanDtosVorhanden = getAngebotstklFac()
					.agstklarbeitsplanFindByAgstklIId(agstklDto.getIId(), theClientDto);

			for (int j = 0; j < aAgstklarbeitsplanDtosVorhanden.length; j++) {
				removeAgstklarbeitsplan(aAgstklarbeitsplanDtosVorhanden[j], theClientDto);
			}
			AgstklmengenstaffelSchnellerfassungDto[] agstklmengenstaffelDtosVorhanden = getAngebotstklFac()
					.agstklmengenstaffelSchnellerfassungFindByAgstklIId(agstklDto.getIId(), theClientDto);

			for (int j = 0; j < agstklmengenstaffelDtosVorhanden.length; j++) {
				removeAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelDtosVorhanden[j].getIId());
			}
		}

		for (AgstklarbeitsplanDto aDto : agstklarbeitsplanDtos) {
			aDto.setAgstklIId(agstklIId);
			createAgstklarbeitsplan(aDto, theClientDto);
		}

		for (AgstklpositionDto aDto : agstklpositionDtos) {
			aDto.setAgstklIId(agstklIId);
			try {

				if (aDto.getFAufschlag() == null) {
					aDto.setFAufschlag(0D);
					aDto.setNNettogesamtmitaufschlag(aDto.getNNettogesamtpreis());
					aDto.setNAufschlag(BigDecimal.ZERO);
				}

				getAngebotstklpositionFac().createAgstklposition(aDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		for (AgstklmengenstaffelSchnellerfassungDto aDto : agstklmengenstaffelDtos) {
			if (aDto.getNMenge() != null) {
				aDto.setAgstklIId(agstklIId);
				createAgstklmengenstaffelSchnellerfassung(aDto, theClientDto);
			}
		}

		for (AgstklmaterialDto aDto : agstklmaterialDtos) {
			aDto.setAgstklIId(agstklIId);
			getAngebotstklpositionFac().createAgstklmaterial(aDto, theClientDto);
		}

		try {
			AngebotDto agDto = getAngebotFac().angebotFindByPrimaryKey(angebotIId, theClientDto);

			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(agDto.getKundeIIdRechnungsadresse(), theClientDto);
			Integer mwstsatzBezId = kundeDto.getMwstsatzbezIId();

			MwstsatzDto mwstsatzDtoAktuell = getMandantFac().mwstsatzZuDatumValidate(mwstsatzBezId,
					agDto.getTBelegdatum(), theClientDto);

			AngebotpositionDto posMmzDto = new AngebotpositionDto();
			if (angebotpositionDtoVorhanden != null) {
				posMmzDto = angebotpositionDtoVorhanden;
			}

			posMmzDto.setAgstklIId(agstklIId);
			posMmzDto.setPositionsartCNr(AngebotServiceFac.ANGEBOTPOSITIONART_AGSTUECKLISTE);
			posMmzDto.setBelegIId(angebotIId);
			posMmzDto.setNMenge(bdMenge);
			posMmzDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);

			posMmzDto.setMwstsatzIId(mwstsatzDtoAktuell.getIId());
			posMmzDto.setBAlternative(Helper.boolean2Short(false));

			BigDecimal bdNettoeinzel = bdPreis;// noch

			bdNettoeinzel = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdNettoeinzel, agstklDto.getWaehrungCNr(),
					agDto.getWaehrungCNr(), new Date(agDto.getTBelegdatum().getTime()), theClientDto);

			// in
			// Belegwaehrung
			// umrechnen
			posMmzDto.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(bdNettoeinzel);
			posMmzDto.setNNettoeinzelpreis(bdNettoeinzel);
			posMmzDto.setNEinzelpreis(bdNettoeinzel);
			posMmzDto.setFRabattsatz(0D);
			posMmzDto.setNRabattbetrag(BigDecimal.ZERO);
			posMmzDto.setFZusatzrabattsatz(0D);
			posMmzDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

			BigDecimal mwstBetrag = posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte()
					.multiply(new BigDecimal(mwstsatzDtoAktuell.getFMwstsatz()).movePointLeft(2));

			posMmzDto.setNMwstbetrag(mwstBetrag);

			posMmzDto.setNBruttoeinzelpreis(
					posMmzDto.getNNettoeinzelpreisplusversteckteraufschlagminusrabatte().add(mwstBetrag));

			if (posMmzDto.getIId() == null) {

				Integer angebotpositionIId = getAngebotpositionFac().createAngebotposition(posMmzDto, theClientDto);
				return angebotpositionIId;
			} else {

				getAngebotpositionFac().updateAngebotposition(posMmzDto, theClientDto);

				return posMmzDto.getIId();
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

	}

	public AgstklmengenstaffelDto[] agstklmengenstaffelFindByAgstklIId(Integer agstklIId, TheClientDto theClientDto) {

		AgstklmengenstaffelDto[] agstklmengenstaffelDto = null;
		Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
		query.setParameter(1, agstklIId);
		Collection<?> cl = query.getResultList();
		agstklmengenstaffelDto = AgstklmengenstaffelDtoAssembler.createDtos(cl);
		return agstklmengenstaffelDto;

	}

	public AgstklmengenstaffelSchnellerfassungDto[] agstklmengenstaffelSchnellerfassungFindByAgstklIId(
			Integer agstklIId, TheClientDto theClientDto) {

		AgstklmengenstaffelSchnellerfassungDto[] agstklmengenstaffelDto = null;
		Query query = em.createNamedQuery("AgstklmengenstaffelSchnellerfassungFindByAgstklIId");
		query.setParameter(1, agstklIId);
		Collection<?> cl = query.getResultList();
		agstklmengenstaffelDto = AgstklmengenstaffelSchnellerfassungDtoAssembler.createDtos(cl);
		return agstklmengenstaffelDto;

	}

	public AgstklmengenstaffelDto agstklmengenstaffelFindByPrimaryKey(Integer iId) {
		Agstklmengenstaffel agstklmengenstaffel = em.find(Agstklmengenstaffel.class, iId);
		return AgstklmengenstaffelDtoAssembler.createDto(agstklmengenstaffel);
	}

	public AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelSchnellerfassungFindByPrimaryKey(Integer iId) {
		AgstklmengenstaffelSchnellerfassung agstklmengenstaffel = em.find(AgstklmengenstaffelSchnellerfassung.class,
				iId);
		return AgstklmengenstaffelSchnellerfassungDtoAssembler.createDto(agstklmengenstaffel);
	}

	public void removeAgstkl(Integer iId) {
		Agstkl toRemove = em.find(Agstkl.class, iId);
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeAgstkl. Es gibt keine Agstkl mit der iid " + iId);
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateAgstklaufschlag(Integer agstklIId, AufschlagDto[] aufschlagDtos, TheClientDto theClientDto) {

		for (int i = 0; i < aufschlagDtos.length; i++) {

			try {
				Query query2 = em.createNamedQuery("AgstklaufschlagFindByAgstklIIdAufschlagIId");
				query2.setParameter(1, agstklIId);
				query2.setParameter(2, aufschlagDtos[i].getIId());
				Agstklaufschlag agstklaufschlag = (Agstklaufschlag) query2.getSingleResult();
				agstklaufschlag.setFAufschlag(aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag());
			} catch (NoResultException ex) {
				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLAUFSCHLAG);
				Agstklaufschlag agstklaufschlag = new Agstklaufschlag(pk, agstklIId, aufschlagDtos[i].getIId(),
						aufschlagDtos[i].getAgstklaufschlagDto().getFAufschlag());
				em.merge(agstklaufschlag);
				em.flush();

			}
		}

		if (aufschlagDtos.length > 0) {
			Agstkl agstkl = em.find(Agstkl.class, agstklIId);
			agstkl.setTAendern(new Timestamp(System.currentTimeMillis()));
			agstkl.setPersonalIIdAendern(theClientDto.getIDPersonal());
		}

	}

	public void updateAgstkl(AgstklDto agstklDto) {
		if (agstklDto != null) {
			Integer iId = agstklDto.getIId();

			Agstkl agstkl = em.find(Agstkl.class, iId);
			setAgstklFromAgstklDto(agstkl, agstklDto);
		}
	}

	public void updateAgstkls(AgstklDto[] agstklDtos) throws RemoteException {
		if (agstklDtos != null) {
			for (int i = 0; i < agstklDtos.length; i++) {
				updateAgstkl(agstklDtos[i]);
			}
		}
	}

	public AgstklDto agstklFindByCNrMandantCNr(String cNr, String mandantCNr) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
			query.setParameter(1, cNr);
			query.setParameter(2, mandantCNr);
			Agstkl agstkl = (Agstkl) query.getSingleResult();
			if (agstkl == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						"Fehler bei find agstklbycnrmandantcnr. Es konnte keine Agstkl mit cnr " + cNr
								+ " fuer den Mandanten " + mandantCNr + " gefunden werden");
			}
			return assembleAgstklDto(agstkl);
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public AgstklDto agstklFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr) {
		Query query = em.createNamedQuery("AgstklfindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Agstkl agstkl = (Agstkl) query.getSingleResult();
			return assembleAgstklDto(agstkl);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Integer createEinkaufsangebotpositions(EinkaufsangebotpositionDto[] einkaufsangebotpositionDtos,
			TheClientDto theClientDto) {
		Integer iId = null;
		for (int i = 0; i < einkaufsangebotpositionDtos.length; i++) {
			iId = createEinkaufsangebotposition(einkaufsangebotpositionDtos[i], theClientDto);
		}
		return iId;
	}

	public AgstklDto[] agstklFindByAnsprechpartnerIIdKunde(Integer iAnsprechpartnerIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("AgstklfindByAnsprechpartnerIIdKunde");
			query.setParameter(1, iAnsprechpartnerIId);
			// @todo getSingleResult oder getResultList ?
			return assembleAgstklDtos((Collection<?>) query.getResultList());
			// @ToDo null Pruefung?
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public Integer createWeblieferant(WeblieferantDto dto, TheClientDto theClientDto) {
		Validator.notNull(dto.getWebpartnerIId(), "webpartnerIId");

		try {
			WeblieferantQuery.findByWebpartnerIId(em, dto.getWebpartnerIId());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_WEBLIEFERANT.UK"));
		} catch (NoResultException ex) {
		}

		try {
			Integer iSort = WeblieferantQuery.maxISort(em);
			if (iSort == null)
				iSort = new Integer(0);
			dto.setISort(iSort + 1);
			Weblieferant entity = getMapper().map(dto, Weblieferant.class);
			em.persist(entity);
			em.flush();

			return entity.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createEkweblieferant(EkweblieferantDto dto, TheClientDto theClientDto) {
		Validator.notNull(dto.getEinkaufsangebotIId(), "einkaufsangebotIId");
		Validator.notNull(dto.getWebpartnerIId(), "webpartnerIId");

		try {
			EkWeblieferantQuery.findByEinkaufsangebotIIdWebpartnerIId(em, dto.getEinkaufsangebotIId(),
					dto.getWebpartnerIId());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("AS_WEBLIEFERANT.UK"));
		} catch (NoResultException ex) {
		}

		try {
			Integer iSort = EkWeblieferantQuery.maxISort(em, dto.getEinkaufsangebotIId());
			if (iSort == null)
				iSort = new Integer(0);
			dto.setISort(iSort + 1);

			Ekweblieferant entity = getMapper().map(dto, Ekweblieferant.class);
			em.persist(entity);
			em.flush();

			return entity.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeWeblieferant(Integer weblieferantIId) {
		Validator.pkFieldNotNull(weblieferantIId, "iId");

		Weblieferant toRemove = em.find(Weblieferant.class, weblieferantIId);

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

	public void removeEkweblieferant(Integer ekweblieferantIId) {
		Validator.pkFieldNotNull(ekweblieferantIId, "iId");

		Ekweblieferant toRemove = em.find(Ekweblieferant.class, ekweblieferantIId);

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

	public void updateWeblieferant(WeblieferantDto dto) {
		Validator.pkFieldNotNull(dto.getIId(), "iId");

		Weblieferant weblieferant = em.find(Weblieferant.class, dto.getIId());
		if (weblieferant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		weblieferant = getMapper().map(dto, Weblieferant.class);
		em.merge(weblieferant);
		em.flush();
	}

	public void updateEkweblieferant(EkweblieferantDto dto) {
		Validator.pkFieldNotNull(dto.getIId(), "iId");

		Ekweblieferant weblieferant = em.find(Ekweblieferant.class, dto.getIId());
		if (weblieferant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		weblieferant = getMapper().map(dto, Ekweblieferant.class);
		em.merge(weblieferant);
		em.flush();
	}

	public WeblieferantDto weblieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException {
		return weblieferantFindByPrimaryKey(iId, false, theClientDto);
	}

	public WeblieferantDto weblieferantFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException {
		Validator.pkFieldNotNull(iId, "iId");

		Weblieferant entity = em.find(Weblieferant.class, iId);
		WeblieferantDto dto = WeblieferantDtoAssembler.createDto(entity);

		if (fullLoad) {
			dto = loadDtosOfWeblieferantDto(dto, theClientDto);
		}
		return dto;
	}

	private WeblieferantDto loadDtosOfWeblieferantDto(WeblieferantDto dto, TheClientDto theClientDto)
			throws RemoteException {
		if (dto == null || dto.getWebpartnerIId() == null)
			return dto;

		dto.setWebpartnerDto(webpartnerFindByPrimaryKey(dto.getWebpartnerIId(), true, theClientDto));
		dto.setDtosLoaded(true);
		return dto;
	}

	private List<WeblieferantDto> loadDtosOfWeblieferantDtoFromList(List<WeblieferantDto> list,
			TheClientDto theClientDto) throws RemoteException {
		List<WeblieferantDto> returnList = new ArrayList<WeblieferantDto>();
		for (WeblieferantDto dto : list) {
			dto = loadDtosOfWeblieferantDto(dto, theClientDto);
			returnList.add(dto);
		}
		return returnList;
	}

	private List<EkweblieferantDto> loadDtosOfEkweblieferantDtoFromList(List<EkweblieferantDto> list,
			TheClientDto theClientDto) throws RemoteException {
		List<EkweblieferantDto> returnList = new ArrayList<EkweblieferantDto>();
		for (WeblieferantDto dto : list) {
			dto = loadDtosOfWeblieferantDto(dto, theClientDto);
			returnList.add((EkweblieferantDto) dto);
		}
		return returnList;
	}

	public EkweblieferantDto ekweblieferantFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws RemoteException {
		return ekweblieferantFindByPrimaryKey(iId, false, theClientDto);
	}

	public EkweblieferantDto ekweblieferantFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException {
		Validator.pkFieldNotNull(iId, "iId");

		Ekweblieferant entity = em.find(Ekweblieferant.class, iId);
		EkweblieferantDto dto = EkweblieferantDtoAssembler.createDto(entity);

		if (fullLoad) {
			dto = (EkweblieferantDto) loadDtosOfWeblieferantDto(dto, theClientDto);
		}
		return dto;
	}

	public void vertauscheWeblieferant(Integer iIdPosition1I, Integer iIdPosition2I) {
		Validator.pkFieldNotNull(iIdPosition1I, "iIdPosition1I");
		Validator.pkFieldNotNull(iIdPosition2I, "iIdPosition2I");

		Weblieferant oPosition1 = em.find(Weblieferant.class, iIdPosition1I);
		Weblieferant oPosition2 = em.find(Weblieferant.class, iIdPosition2I);
		if (oPosition1 == null || oPosition2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
	}

	public void verdichteEinkaufsangebotPositionen(Integer einkaufsangebotIId, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
		query.setParameter(1, einkaufsangebotIId);
		Collection<?> cl = query.getResultList();

		Iterator<?> it = cl.iterator();

		LinkedHashMap<String, EinkaufsangebotpositionDto> lhmVerdichtet = new LinkedHashMap<String, EinkaufsangebotpositionDto>();

		ArrayList al = new ArrayList();
		while (it.hasNext()) {
			Einkaufsangebotposition bean = (Einkaufsangebotposition) it.next();

			String positionsart = bean.getAgstklpositionsartCNr();

			String vergleich = positionsart;

			if (positionsart.equals(LocaleFac.POSITIONSART_IDENT)) {
				vergleich += bean.getArtikelIId();
			} else {
				vergleich += bean.getCBez() + " " + bean.getCArtikelnrhersteller();
			}

			if (lhmVerdichtet.containsKey(vergleich)) {
				// Zu einer vorhandennen addieren
				EinkaufsangebotpositionDto pos = lhmVerdichtet.get(vergleich);
				pos.setNMenge(pos.getNMenge().add(bean.getNMenge()));

				boolean bZuLang = false;

				// Position
				if (pos.getCPosition() != null && bean.getCPosition() != null) {

					pos.setCPosition(pos.getCPosition() + ", " + bean.getCPosition());

				} else if (pos.getCPosition() == null && bean.getCPosition() != null) {
					pos.setCPosition(bean.getCPosition());
				}

				if (pos.getCPosition() != null && pos.getCPosition().length() > 3000) {
					bZuLang = true;
				}

				// Bemerkung
				if (pos.getCBemerkung() != null && bean.getCBemerkung() != null) {

					pos.setCBemerkung(pos.getCBemerkung() + ", " + bean.getCBemerkung());

				} else if (pos.getCBemerkung() == null && bean.getCBemerkung() != null) {
					pos.setCBemerkung(bean.getCBemerkung());
				}

				if (pos.getCBemerkung() != null && pos.getCBemerkung().length() > 300) {
					bZuLang = true;
				}

				// Internebemerkung
				if (pos.getCInternebemerkung() != null && bean.getCInternebemerkung() != null) {

					pos.setCInternebemerkung(pos.getCInternebemerkung() + ", " + bean.getCInternebemerkung());

				} else if (pos.getCInternebemerkung() == null && bean.getCInternebemerkung() != null) {
					pos.setCInternebemerkung(bean.getCInternebemerkung());
				}

				if (pos.getCInternebemerkung() != null && pos.getCInternebemerkung().length() > 300) {
					bZuLang = true;
				}

				if (bZuLang == true) {

					ArrayList alError = new ArrayList();

					if (positionsart.equals(LocaleFac.POSITIONSART_IDENT)) {
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(pos.getArtikelIId(), theClientDto);
						alError.add(aDto.getCNr());
					} else {
						alError.add(pos.getCBez());
					}

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINKAUFSANGEBOT_VERDICHTEN_TEXTE_ZU_LANG, alError,
							new Exception(""));

				}

				em.remove(bean);
				em.flush();

				lhmVerdichtet.put(vergleich, pos);
			} else {
				lhmVerdichtet.put(vergleich, einkaufsangebotpositionFindByPrimaryKey(bean.getIId()));
			}

		}

		Iterator it2 = lhmVerdichtet.keySet().iterator();
		while (it2.hasNext()) {
			String key = (String) it2.next();
			EinkaufsangebotpositionDto pos = lhmVerdichtet.get(key);
			updateEinkaufsangebotposition(pos);
		}

	}

	public void vertauscheEkweblieferant(Integer iIdPosition1I, Integer iIdPosition2I) {
		Validator.pkFieldNotNull(iIdPosition1I, "iIdPosition1I");
		Validator.pkFieldNotNull(iIdPosition2I, "iIdPosition2I");

		Ekweblieferant oPosition1 = em.find(Ekweblieferant.class, iIdPosition1I);
		Ekweblieferant oPosition2 = em.find(Ekweblieferant.class, iIdPosition2I);
		if (oPosition1 == null || oPosition2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
	}

	public void removeEinkaufsangebot(Integer iId) throws EJBExceptionLP {
		try {
			Einkaufsangebot toRemove = em.find(Einkaufsangebot.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeEinkaufsangebot. Es gibt keine iid " + iId);
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

	public void updateEinkaufsangebot(EinkaufsangebotDto einkaufsangebotDto) throws EJBExceptionLP {
		if (einkaufsangebotDto != null) {
			Integer iId = einkaufsangebotDto.getIId();
			try {
				Einkaufsangebot einkaufsangebot = em.find(Einkaufsangebot.class, iId);
				setEinkaufsangebotFromEinkaufsangebotDto(einkaufsangebot, einkaufsangebotDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateEinkaufsangebots(EinkaufsangebotDto[] einkaufsangebotDtos) throws EJBExceptionLP {
		if (einkaufsangebotDtos != null) {
			for (int i = 0; i < einkaufsangebotDtos.length; i++) {
				updateEinkaufsangebot(einkaufsangebotDtos[i]);
			}
		}
	}

	public EinkaufsangebotDto[] einkaufsangebotFindByAnsprechpartnerIId(Integer iAnsprechpartnerIId)
			throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("EinkaufsangebotfindByAnsprechpartnerIId");
			query.setParameter(1, iAnsprechpartnerIId);
			return assembleEinkaufsangebotDtos((Collection<?>) query.getResultList());
			// }
			// catch (FinderException fe) {
			// throw fe;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public Integer createAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_AGSTKLARBEITSPLAN);
		agstklarbeitsplanDto.setIId(pk);

		if (agstklarbeitsplanDto.getBInitial() == null) {
			agstklarbeitsplanDto.setBInitial(Helper.boolean2Short(false));
		}

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklarbeitsplanDto.getArtikelIId(),
				theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
			// PJ 16396
			if (agstklarbeitsplanDto.getAgartCNr() != null) {
				Query query = em.createNamedQuery("AgstklarbeitsplanfindByAgstklIIdIArbeitsgangnummer");
				query.setParameter(1, agstklarbeitsplanDto.getAgstklIId());
				query.setParameter(2, agstklarbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();
					if (ap.getAgartCNr() == null) {
						agstklarbeitsplanDto.setMaschineIId(ap.getMaschineIId());
						break;
					}
				}
			}
		}

		try {
			Agstklarbeitsplan agstklarbeitsplan = new Agstklarbeitsplan(agstklarbeitsplanDto.getIId(),
					agstklarbeitsplanDto.getAgstklIId(), agstklarbeitsplanDto.getIArbeitsgang(),
					agstklarbeitsplanDto.getArtikelIId(), agstklarbeitsplanDto.getLStueckzeit(),
					agstklarbeitsplanDto.getLRuestzeit(), agstklarbeitsplanDto.getBNurmaschinenzeit(),
					agstklarbeitsplanDto.getBInitial());
			em.persist(agstklarbeitsplan);
			em.flush();
			setAgstklarbeitsplanFromAgstklarbeitsplanDto(agstklarbeitsplan, agstklarbeitsplanDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return agstklarbeitsplanDto.getIId();
	}

	private void setAgstklarbeitsplanFromAgstklarbeitsplanDto(Agstklarbeitsplan agstklarbeitsplan,
			AgstklarbeitsplanDto agstklarbeitsplanDto) {
		agstklarbeitsplan.setAgstklIId(agstklarbeitsplanDto.getAgstklIId());
		agstklarbeitsplan.setIArbeitsgang(agstklarbeitsplanDto.getIArbeitsgang());
		agstklarbeitsplan.setArtikelIId(agstklarbeitsplanDto.getArtikelIId());
		agstklarbeitsplan.setLStueckzeit(agstklarbeitsplanDto.getLStueckzeit());
		agstklarbeitsplan.setLRuestzeit(agstklarbeitsplanDto.getLRuestzeit());
		agstklarbeitsplan.setCKommentar(agstklarbeitsplanDto.getCKommentar());
		agstklarbeitsplan.setXLangtext(agstklarbeitsplanDto.getXLangtext());
		agstklarbeitsplan.setMaschineIId(agstklarbeitsplanDto.getMaschineIId());
		agstklarbeitsplan.setIAufspannung(agstklarbeitsplanDto.getIAufspannung());
		agstklarbeitsplan.setAgartCNr(agstklarbeitsplanDto.getAgartCNr());
		agstklarbeitsplan.setIUnterarbeitsgang(agstklarbeitsplanDto.getIUnterarbeitsgang());
		agstklarbeitsplan.setBNurmaschinenzeit(agstklarbeitsplanDto.getBNurmaschinenzeit());
		agstklarbeitsplan.setNStundensatzMann(agstklarbeitsplanDto.getNStundensatzMann());
		agstklarbeitsplan.setNStundensatzMaschine(agstklarbeitsplanDto.getNStundensatzMaschine());
		agstklarbeitsplan.setBInitial(agstklarbeitsplanDto.getBInitial());

		em.merge(agstklarbeitsplan);
		em.flush();
	}

	public void updateAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {

		Integer iId = agstklarbeitsplanDto.getIId();
		// try {
		Agstklarbeitsplan agstklarbeitsplan = em.find(Agstklarbeitsplan.class, iId);
		if (agstklarbeitsplan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		setAgstklarbeitsplanFromAgstklarbeitsplanDto(agstklarbeitsplan, agstklarbeitsplanDto);

		// PJ 16396
		if (agstklarbeitsplanDto.getMaschineIId() != null && agstklarbeitsplanDto.getAgartCNr() == null
				&& agstklarbeitsplanDto.getIArbeitsgang() != 0) {

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agstklarbeitsplanDto.getArtikelIId(),
					theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em.createNamedQuery("AgstklarbeitsplanfindByAgstklIIdIArbeitsgangnummer");
				query.setParameter(1, agstklarbeitsplanDto.getAgstklIId());
				query.setParameter(2, agstklarbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Agstklarbeitsplan ap = (Agstklarbeitsplan) it.next();
					if (!ap.getIId().equals(agstklarbeitsplanDto.getIId())) {
						ArtikelDto artikelDtoPos = getArtikelFac().artikelFindByPrimaryKeySmall(ap.getArtikelIId(),
								theClientDto);
						if (Helper.short2boolean(artikelDtoPos.getbReinemannzeit()) == false) {
							ap.setMaschineIId(agstklarbeitsplanDto.getMaschineIId());
						}
					}
				}
			}
		}

	}

	private AgstklarbeitsplanDto assembleAgstklarbeitsplanDto(Agstklarbeitsplan agstklarbeitsplan) {
		return AgstklarbeitsplanDtoAssembler.createDto(agstklarbeitsplan);
	}

	public AgstklarbeitsplanDto agstklarbeitsplanFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		Agstklarbeitsplan agstklarbeitsplan = em.find(Agstklarbeitsplan.class, iId);
		return assembleAgstklarbeitsplanDto(agstklarbeitsplan);
	}

	public void removeAgstklarbeitsplan(AgstklarbeitsplanDto agstklarbeitsplanDto, TheClientDto theClientDto) {
		Agstklarbeitsplan toRemove = em.find(Agstklarbeitsplan.class, agstklarbeitsplanDto.getIId());
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

	public Integer getNextArbeitsgang(Integer agstklIId, TheClientDto theClientDto) {
		if (agstklIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("stuecklisteIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("AgstklarbeitsplanejbSelectNextReihung");
				querynext.setParameter(1, agstklIId);
				i = (Integer) querynext.getSingleResult();
				if (i == null) {
					return new Integer(10);
				}

				if (i != null) {
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
					Integer iErhoehung = (Integer) parameter.getCWertAsObject();
					i = new Integer(i.intValue() + iErhoehung.intValue());
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			return i;

		} catch (NoResultException e) {
			return new Integer(10);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

	}

	@Override
	public BelegpositionDto getNewPositionDto() {
		return new EinkaufsangebotpositionDto();
	}

	@Override
	/**
	 * Erstellt ein EinkaufsangebotpositionsDto und setzt die erforderlichen
	 * Parameter.
	 * 
	 * @param spez,         Spezifikation des Stklimports
	 * @param result,       einzelnes Result des Stklimports
	 * @param theClientDto, der aktuelle Benutzer
	 * 
	 * @return das vorbef&uuml;llte PositionsDto
	 */
	public BelegpositionDto preparePositionDtoAusImportResult(BelegpositionDto posDto, StklImportSpezifikation spez,
			IStklImportResult result, TheClientDto theClientDto) {

		posDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		EinkaufsangebotpositionDto ekPosDto = (EinkaufsangebotpositionDto) posDto;
		ekPosDto.setBMitdrucken(Helper.getShortFalse());
		ekPosDto.setCPosition(Helper.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.POSITION),
				AngebotstklFac.FieldLength.EINKAUFSANGEBOTPOSITION_CPOSITION));
		ekPosDto.setCBemerkung(
				Helper.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.REFERENZBEZEICHNUNG),
						AngebotstklFac.FieldLength.EINKAUFSANGEBOTPOSITION_CBEMERKUNG));
		ekPosDto.setCInternebemerkung(
				Helper.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.INTERNE_BEMERKUNG),
						AngebotstklFac.FieldLength.EINKAUFSANGEBOTPOSITION_CINTERNEBEMERKUNG));
		ekPosDto.setCArtikelnrhersteller(
				Helper.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.HERSTELLERARTIKELNUMMER),
						AngebotstklFac.FieldLength.EINKAUFSANGEBOTPOSITION_CHERSTELLERARTIKELNUMMER));
		Integer artikelBezLaenge = getParameterFac().getArtikelLaengeBezeichungen(theClientDto.getMandant());
		ekPosDto.setCArtikelbezhersteller(Helper
				.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.HERSTELLERBEZ), artikelBezLaenge));

		String herstellerCNr = Helper.cutString(result.getValues().get(EinkaufsagstklImportSpezifikation.HERSTELLER),
				ArtikelFac.MAX_HERSTELLER_NAME);
		if (!Helper.isStringEmpty(herstellerCNr)) {
			try {
				Integer herstellerIId = getArtikelimportFac().herstellerSuchenUndAnlegen(theClientDto, herstellerCNr);
				ekPosDto.setHerstellerIId(herstellerIId);
			} catch (RemoteException e) {
				myLogger.error("RemoteException beim Finden/Erzeugen von Hersteller '" + herstellerCNr + "'", e);
			}
		}

		String bez2 = result.getValues().get(EinkaufsagstklImportSpezifikation.BEZEICHNUNG2);
		bez2 = bez2 != null ? bez2.trim() + " " : "";

		String bez3 = result.getValues().get(EinkaufsagstklImportSpezifikation.BEZEICHNUNG3);
		String cZbez2 = bez3 != null ? bez2 + bez3.trim() : bez2;
		ekPosDto.setCZbez2(Helper.cutString(cZbez2, FieldLength.EINKAUFSANGEBOTPOSITION_CZBEZ2));

		HvOptional<String> lfdNummer = HvOptional
				.ofNullable(result.getValues().get(EinkaufsagstklImportSpezifikation.LAUFENDE_NUMMER));
		if (lfdNummer.isPresent() && Helper.istStringNumerisch(lfdNummer.get())) {
			ekPosDto.setILfdnummer(Integer.parseInt(lfdNummer.get()));
		}

		return posDto;
	}

	@Override
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		createEinkaufsangebotpositions(posDtos.toArray(new EinkaufsangebotpositionDto[posDtos.size()]), theClientDto);
	}

	@Override
	public Integer getBezugsobjektIIdDerStueckliste(StklImportSpezifikation spez, TheClientDto theClientDto)
			throws RemoteException {
		EinkaufsangebotDto einkaufsangebotDto = einkaufsangebotFindByPrimaryKey(spez.getStklIId());

		return einkaufsangebotDto == null ? null : einkaufsangebotDto.getKundeIId();
	}

	@Override
	public IImportPositionen asPositionImporter() {
		return this;
	}

	@Override
	public IImportHead asHeadImporter() {
		return this;
	}

	@Override
	public Integer createWebpartner(IWebpartnerDto dto, TheClientDto theClientDto) {
		Validator.notNull(dto.getWebabfrageIId(), "webabfrageIId");

		Webabfrage webabfrage = em.find(Webabfrage.class, dto.getWebabfrageIId());
		IWebpartner entity = null;

		if (WebabfrageTyp.FINDCHIPS.equals(webabfrage.getITyp())) {
			entity = getMapper().map(dto, WebFindChips.class);
		} else if (WebabfrageTyp.FARNELL.equals(webabfrage.getITyp())) {
			entity = getMapper().map(dto, WeblieferantFarnell.class);
		}

		if (entity == null)
			return null;

		em.persist(entity);
		em.flush();

		return entity.getIId();
	}

	@Override
	public void updateWebpartner(IWebpartnerDto dto) {
		Validator.pkFieldNotNull(dto.getIId(), "iId");
		Validator.notNull(dto.getWebabfrageIId(), "webabfrageIId");

		Webabfrage webabfrage = em.find(Webabfrage.class, dto.getWebabfrageIId());
		IWebpartner entity = null;
		if (WebabfrageTyp.FINDCHIPS.equals(webabfrage.getITyp())) {
			entity = em.find(WebFindChips.class, dto.getIId());
			entity = getMapper().map(dto, WebFindChips.class);
		} else if (WebabfrageTyp.FARNELL.equals(webabfrage.getITyp())) {
			entity = em.find(WeblieferantFarnell.class, dto.getIId());
			entity = getMapper().map(dto, WeblieferantFarnell.class);
		} else {
			myLogger.warn("Webanfrage-Typ mit iTyp=" + webabfrage.getITyp() + " fuer Webpartner-Update nicht gefunden");
			return;
		}

		if (entity == null) {
			myLogger.warn("Webpartner mit iId=" + dto.getIId() + " fuer Update nicht gefunden");
			return;
		}

		em.merge(entity);
		em.flush();
	}

	@Override
	public void removeWebpartner(Integer iId) {
		Validator.pkFieldNotNull(iId, "iId");

		Webpartner entity = em.find(Webpartner.class, iId);

		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Webpartner mit iId=" + iId + " nicht gefunden");
		}

		try {
			em.remove(entity);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	@Override
	public WebpartnerDto webpartnerFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException {
		return webpartnerFindByPrimaryKey(iId, false, theClientDto);
	}

	@Override
	public WebpartnerDto webpartnerFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException {
		Validator.pkFieldNotNull(iId, "iId");

		Webpartner entity = em.find(Webpartner.class, iId);
		WebpartnerDto dto = WebPartnerDtoAssembler.createDto(entity);

		if (fullLoad) {
			dto = (WebpartnerDto) loadDtosOfWebpartnerDto(dto, theClientDto);
		}

		return dto;
	}

	@Override
	public WebFindChipsDto webfindchipsFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws RemoteException {
		return webfindchipsFindByPrimaryKey(iId, false, theClientDto);
	}

	@Override
	public WebFindChipsDto webfindchipsFindByPrimaryKey(Integer iId, Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException {
		Validator.pkFieldNotNull(iId, "iId");

		WebFindChips entity = em.find(WebFindChips.class, iId);
		WebFindChipsDto dto = WebPartnerDtoAssembler.createDto(entity);

		if (fullLoad) {
			dto = (WebFindChipsDto) loadDtosOfWebpartnerDto(dto, theClientDto);
		}

		return dto;
	}

	@Override
	public List<WebFindChipsDto> webfindchipsFindByMandantCNr(Boolean fullLoad, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "theClientDto.getMandant()");

		List<WebFindChips> list = WebFindChipsQuery.listByMandantCNr(em, theClientDto.getMandant());
		List<WebFindChipsDto> dtoList = WebPartnerDtoAssembler.createDtos(list);

		if (fullLoad) {
			for (WebFindChipsDto dto : dtoList) {
				dto = (WebFindChipsDto) loadDtosOfWebpartnerDto(dto, theClientDto);
			}
		}

		return dtoList;
	}

	@Override
	public List<WebFindChipsDto> webfindchipsFindByMandantCNr(TheClientDto theClientDto) throws RemoteException {
		return webfindchipsFindByMandantCNr(false, theClientDto);
	}

	@Override
	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten(Boolean fullLoad,
			TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(theClientDto, "theClientDto");
		Validator.notEmpty(theClientDto.getMandant(), "theClientDto.getMandant()");

		List<WebFindChips> list = WebFindChipsQuery.listByMandantCNrWithNullLieferanten(em, theClientDto.getMandant());
		List<WebFindChipsDto> dtoList = WebPartnerDtoAssembler.createDtos(list);

		if (fullLoad) {
			for (WebFindChipsDto dto : dtoList) {
				dto = (WebFindChipsDto) loadDtosOfWebpartnerDto(dto, theClientDto);
			}
		}

		return dtoList;
	}

	@Override
	public List<WebFindChipsDto> webfindchipsFindByMandantCNrWithNullLieferanten(TheClientDto theClientDto)
			throws RemoteException {
		return webfindchipsFindByMandantCNr(false, theClientDto);
	}

	private IWebpartnerDto loadDtosOfWebpartnerDto(IWebpartnerDto webpartnerDto, TheClientDto theClientDto)
			throws RemoteException {
		if (webpartnerDto == null || webpartnerDto.getLieferantIId() == null)
			return webpartnerDto;

		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKeyOhneExc(webpartnerDto.getLieferantIId(),
				theClientDto);
		webpartnerDto.setLieferantDto(lieferantDto != null ? lieferantDto : new LieferantDto());
		((WebpartnerDto) webpartnerDto).setDtosLoaded(lieferantDto != null ? true : false);

		return webpartnerDto;
	}

	private List<IWebpartnerDto> loadDtosOfWebpartnerDtoFromList(List<IWebpartnerDto> list, TheClientDto theClientDto)
			throws RemoteException {
		List<IWebpartnerDto> returnList = new ArrayList<IWebpartnerDto>();
		for (IWebpartnerDto dto : list) {
			returnList.add(loadDtosOfWebpartnerDto(dto, theClientDto));
		}
		return returnList;
	}

	@Override
	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp, TheClientDto theClientDto)
			throws RemoteException {
		return weblieferantFindByWebabfrageTyp(webabfrageITyp, false, theClientDto);
	}

	@Override
	public List<WeblieferantDto> weblieferantFindByWebabfrageTyp(Integer webabfrageITyp, Boolean fullLoad,
			TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(webabfrageITyp, "webabfrageITyp");
		Validator.notNull(theClientDto, "theClientDto");

		List<Weblieferant> weblieferanten = WeblieferantQuery.listByWebabfrageTypIIdMandantCNr(em, webabfrageITyp,
				theClientDto.getMandant());
		List<WeblieferantDto> list = WeblieferantDtoAssembler.createDtos(weblieferanten);

		if (fullLoad) {
			list = loadDtosOfWeblieferantDtoFromList(list, theClientDto);
		}
		return list;
	}

	@Override
	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer iId,
			Integer webabfrageTyp, TheClientDto theClientDto) throws RemoteException {
		return ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(iId, webabfrageTyp, false, theClientDto);
	}

	@Override
	public List<EkweblieferantDto> ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(Integer iId,
			Integer webabfrageTyp, Boolean fullLoad, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(iId, "einkaufsangebotIId");

		List<Ekweblieferant> ekweblieferanten = EkWeblieferantQuery.listByEinkaufsangebotIIdWebabfrageTyp(em, iId,
				webabfrageTyp);
		List<EkweblieferantDto> dtoList = EkweblieferantDtoAssembler.createDtos(ekweblieferanten);

		if (fullLoad) {
			dtoList = loadDtosOfEkweblieferantDtoFromList(dtoList, theClientDto);
		}
		return dtoList;
	}

	private void uebernehmeWeblieferantenAusGrunddaten(Integer einkaufsangebotIId, TheClientDto theClientDto) {
		List<Weblieferant> weblieferanten = WeblieferantQuery.listByMandantCNr(em, theClientDto.getMandant());

		for (IWeblieferant wl : weblieferanten) {
			EkweblieferantDto ekweblieferantDto = new EkweblieferantDto();
			ekweblieferantDto.setEinkaufsangebotIId(einkaufsangebotIId);
			ekweblieferantDto.setWebpartnerIId(wl.getWebpartnerIId());
			createEkweblieferant(ekweblieferantDto, theClientDto);
		}
	}

	@Override
	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebot(Integer iId, TheClientDto theClientDto)
			throws RemoteException {
		HvTypedQuery<Integer> query = new HvTypedQuery<Integer>(
				em.createNamedQuery("EinkaufsangebotpositionIIdfindByEinkaufsangebotIId"));
		query.setParameter(1, iId);
		List<Integer> iIds = query.getResultList();

		return getWebabfragepositionenByEinkaufsangebotpositionen(iIds, theClientDto);
	}

	@Override
	public List<WebabfragepositionDto> getWebabfragepositionenByEinkaufsangebotpositionen(List<Integer> iIds,
			TheClientDto theClientDto) throws RemoteException {
		List<EinkaufsangebotpositionDto> ekpositionenDto = einkaufsangebotpositionenFindByPrimaryKeys(
				iIds.toArray(new Integer[iIds.size()]));
		List<WebabfragepositionDto> webabfragepositionenDto = new ArrayList<WebabfragepositionDto>();

		for (EinkaufsangebotpositionDto ekposDto : ekpositionenDto) {
			WebabfragepositionDto waposDto = new WebabfragepositionDto();
			waposDto.setEinkaufsangebotpositionDto(ekposDto);
			ArtikelDto artikelDto = ekposDto.getArtikelIId() == null ? null
					: getArtikelFac().artikelFindByPrimaryKeySmall(ekposDto.getArtikelIId(), theClientDto);
			waposDto.setArtikelDto(artikelDto);
			ArtikellieferantDto[] artikellieferantDtos = artikelDto == null ? null
					: getArtikelFac().artikellieferantFindByArtikelIId(artikelDto.getIId(), theClientDto);
			waposDto.setArtikellieferantDtos(artikellieferantDtos == null ? null : Arrays.asList(artikellieferantDtos));
			webabfragepositionenDto.add(waposDto);
		}

		return webabfragepositionenDto;
	}

	@Override
	public WebFindChipsDto webfindchipsFindByDistributorId(String distributorId) throws RemoteException {
		WebFindChips entity = WebFindChipsQuery.resultByDistributorId(em, distributorId);

		return WebPartnerDtoAssembler.createDto(entity);
	}

	public ArtikelDto findeArtikelZuEinkaufsangebotpositionHandeingabe(Integer positionIId, TheClientDto theClientDto)
			throws RemoteException {
		IEkpositionWandlerBeanServices beanService = new EkpositionWandlerBeanService(theClientDto, this,
				getArtikelFac());
		EkpositionHandartikelWandler converter = new EkpositionHandartikelWandler(beanService);

		return converter.findeArtikelZuEinkaufsangebotpositionHandeingabe(positionIId);
	}

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(List<Integer> positionIIds, String artikelCNr,
			TheClientDto theClientDto) throws RemoteException {
		IEkpositionWandlerBeanServices beanService = new EkpositionWandlerBeanService(theClientDto, this,
				getArtikelFac());
		EkpositionHandartikelWandler converter = new EkpositionHandartikelWandler(beanService);

		return converter.einkaufsangebotWandleHandartikelUmUndFasseZusammen(positionIIds, artikelCNr);
	}

	public Integer einkaufsangebotWandleHandartikelUmUndFasseZusammen(Integer positionIId, String artikelCNr,
			TheClientDto theClientDto) throws RemoteException {
		IEkpositionWandlerBeanServices beanService = new EkpositionWandlerBeanService(theClientDto, this,
				getArtikelFac());
		EkpositionHandartikelWandler converter = new EkpositionHandartikelWandler(beanService);

		return converter.einkaufsangebotWandleHandartikelUmUndFasseZusammen(positionIId, artikelCNr);
	}

	public List<Integer> einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(Integer einkaufsangebotIId) {
		HvTypedQuery<Integer> query = new HvTypedQuery<Integer>(
				em.createNamedQuery("EinkaufsangebotpositionIIdfindByEinkaufsangebotIId"));
		query.setParameter(1, einkaufsangebotIId);
		List<Integer> positionenIIds = query.getResultList();

		return positionenIIds;
	}

	private void setzeSpaltenbreite(WritableSheet sheet, int iSpalte, int iBreite) {

		Cell[] cells = sheet.getColumn(iSpalte);
		int longestStrLen = iBreite;

		if (cells.length == 0)
			return;

		/* If not found, skip the column. */
		if (longestStrLen == -1)
			return;

		/* If wider than the max width, crop width */
		if (longestStrLen > 255)
			longestStrLen = 255;

		CellView cv = sheet.getColumnView(iSpalte);
		cv.setSize(longestStrLen * 256 + 100); /* Every character is 256 units wide, so scale it. */
		sheet.setColumnView(iSpalte, cv);

	}

	private ArrayList<String> lumiQuoteZeileVerarbeiten(int iTypMenge, Integer einkaufsangebotIId,
			LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>> hmLieferanten, TheClientDto theClientDto) {

		ArrayList<String> lieferantAngelegt = new ArrayList<String>();
		try {
			Iterator<String> itLieferanten = hmLieferanten.keySet().iterator();

			while (itLieferanten.hasNext()) {
				String lieferant = itLieferanten.next();

				List<Partner> partners = PartnerQuery.listByKbez(em, lieferant);

				PartnerDto pDto = null;
				if (partners.size() == 0) {
					pDto = new PartnerDto();
					pDto.setCKbez(lieferant);
					pDto.setCName1nachnamefirmazeile1(lieferant);
					pDto.setPartnerartCNr(PartnerFac.PARTNERART_ADRESSE);
					pDto.setBVersteckt(false);
					pDto.setLocaleCNrKommunikation(theClientDto.getLocUiAsString());

					Integer partnerIId = getPartnerFac().createPartner(pDto, theClientDto);

					pDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

				} else {
					Partner partner = partners.get(0);
					pDto = getPartnerFac().partnerFindByPrimaryKey(partner.getIId(), theClientDto);
				}

				LieferantDto lfDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(pDto.getIId(),
						theClientDto.getMandant(), theClientDto);

				Integer lieferantIId = null;

				if (lfDto != null) {
					lieferantIId = lfDto.getIId();
				} else {
					LieferantDto lfDtoNeu = new LieferantDto();
					lfDtoNeu.setPartnerIId(pDto.getIId());
					lfDtoNeu.setPartnerDto(pDto);
					lfDtoNeu.setMandantCNr(theClientDto.getMandant());
					lfDtoNeu.setWaehrungCNr(theClientDto.getSMandantenwaehrung());

					MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
					lfDtoNeu.setIdSpediteur(mDto.getSpediteurIIdLF());
					lfDtoNeu.setLieferartIId(mDto.getLieferartIIdLF());
					lfDtoNeu.setZahlungszielIId(mDto.getZahlungszielIIdLF());
					lfDtoNeu.setBIgErwerb(Helper.boolean2Short(false));

					lieferantIId = getLieferantFac().createLieferant(lfDtoNeu, theClientDto);

					lieferantAngelegt.add(lieferant);

				}

				LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto);

				// EKAGLIEFERANT

				Integer ekaglieferantIId = null;
				try {
					Query query = em.createNamedQuery("EkaglieferantFindByEinkaufsangebotIIdLieferantIId");
					query.setParameter(1, einkaufsangebotIId);
					query.setParameter(2, lieferantDto.getIId());
					// @todo getSingleResult oder getResultList ?
					Ekaglieferant doppelt = (Ekaglieferant) query.getSingleResult();
					ekaglieferantIId = doppelt.getIId();
				} catch (NoResultException ex) {
					EkaglieferantDto dto = new EkaglieferantDto();
					dto.setEinkaufsangebotIId(einkaufsangebotIId);

					dto.setLieferantIId(lieferantDto.getIId());
					dto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
					dto.setNAufschlag(BigDecimal.ZERO);

					ekaglieferantIId = createEkaglieferant(dto, theClientDto);
				}

				ArrayList<ImportLumiQuoteXlsxDto> alZeilen = hmLieferanten.get(lieferant);

				for (ImportLumiQuoteXlsxDto zeileDto : alZeilen) {

					String hvartikelnummer = zeileDto.getHVArtikelnummer();

					EinkaufsangebotpositionDto einkaufsangebotpositionDto = new EinkaufsangebotpositionDto();
					einkaufsangebotpositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					if (hvartikelnummer != null && hvartikelnummer.length() > 0) {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(hvartikelnummer,
								theClientDto.getMandant());

						if (artikelDto != null) {
							// HV-Artikel
							einkaufsangebotpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
							einkaufsangebotpositionDto.setPositionsartCNr(AngebotstklFac.POSITIONSART_AGSTKL_IDENT);
							einkaufsangebotpositionDto.setArtikelIId(artikelDto.getIId());

						} else {
							// Handartikel

							einkaufsangebotpositionDto
									.setPositionsartCNr(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE);
							einkaufsangebotpositionDto.setCBez(zeileDto.getBezeichnung());
							einkaufsangebotpositionDto.setCZusatzbez(
									"Internal Part number " + hvartikelnummer + " in HV-Artikelstamm nicht gefunden");

						}

					} else {
						// Handartikel

						einkaufsangebotpositionDto.setPositionsartCNr(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE);
						einkaufsangebotpositionDto.setCBez(zeileDto.getBezeichnung());
						einkaufsangebotpositionDto.setCZusatzbez("Keine Internal Part number in XLS-Datei vorhanden");
						einkaufsangebotpositionDto.setCArtikelnrhersteller(zeileDto.getHerstellernummer());

					}

					Integer einkaufsangebotpositionIId = null;

					// Gibts es schon eine

					// Anhand position suchen

					Query qtemp = em.createQuery("SELECT e FROM Einkaufsangebotposition e WHERE e.einkaufsangebotIId="
							+ einkaufsangebotIId + " AND e.cBemerkung='" + zeileDto.getPosition() + "'");

					Collection c = qtemp.getResultList();

					if (c.size() > 0) {
						Einkaufsangebotposition ekag = (Einkaufsangebotposition) c.iterator().next();

						einkaufsangebotpositionIId = ekag.getIId();

					}

					if (einkaufsangebotpositionIId == null) {

						einkaufsangebotpositionDto.setBelegIId(einkaufsangebotIId);

						einkaufsangebotpositionDto.setNMenge(zeileDto.getMenge());

						einkaufsangebotpositionDto.setCBemerkung(zeileDto.getPosition());

						einkaufsangebotpositionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
						einkaufsangebotpositionDto.setBMitdrucken(Helper.boolean2Short(true));

						if (einkaufsangebotpositionDto.getArtikelIId() != null) {
							BigDecimal gestpreis = getLagerFac().getGemittelterGestehungspreisAllerLaegerEinesMandanten(
									einkaufsangebotpositionDto.getArtikelIId(), theClientDto);

							einkaufsangebotpositionDto.setNPreis1(gestpreis);
							einkaufsangebotpositionDto.setNPreis2(gestpreis);
							einkaufsangebotpositionDto.setNPreis3(gestpreis);
							einkaufsangebotpositionDto.setNPreis4(gestpreis);
							einkaufsangebotpositionDto.setNPreis5(gestpreis);
						} else {
							einkaufsangebotpositionDto.setNPreis1(BigDecimal.ZERO);
							einkaufsangebotpositionDto.setNPreis2(BigDecimal.ZERO);
							einkaufsangebotpositionDto.setNPreis3(BigDecimal.ZERO);
							einkaufsangebotpositionDto.setNPreis4(BigDecimal.ZERO);
							einkaufsangebotpositionDto.setNPreis5(BigDecimal.ZERO);
						}

						einkaufsangebotpositionIId = createEinkaufsangebotposition(einkaufsangebotpositionDto,
								theClientDto);
					}

					Query qtempPosliefe = em
							.createQuery("SELECT o FROM Positionlieferant o WHERE o.einkaufsangebotpositionIId="
									+ einkaufsangebotpositionIId + " AND o.egaklieferantIId=" + ekaglieferantIId);

					Collection cPoslief = qtempPosliefe.getResultList();

					if (cPoslief.size() > 0) {
						Positionlieferant poslief = (Positionlieferant) cPoslief.iterator().next();

						if (iTypMenge == MENGE_1) {
							poslief.setNPreisMenge1(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_2) {
							poslief.setNPreisMenge2(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_3) {
							poslief.setNPreisMenge3(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_4) {
							poslief.setNPreisMenge4(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_5) {
							poslief.setNPreisMenge5(zeileDto.getPreis());
						}
						em.merge(poslief);
						em.flush();

					} else {

						PositionlieferantDto positionlieferantDto = new PositionlieferantDto();
						positionlieferantDto.setEgaklieferantIId(ekaglieferantIId);
						positionlieferantDto.setEinkaufsangebotpositionIId(einkaufsangebotpositionIId);

						positionlieferantDto.setILieferzeitinkw(zeileDto.getiLieferzeitInTagen() / 7);

						positionlieferantDto.setCBemerkungIntern(zeileDto.getBemerkungLF());
						positionlieferantDto.setNLagerstand(zeileDto.getLagerstandLF());

						positionlieferantDto.setCArtikelnrlieferant(zeileDto.getLf_artikelnummer());

						if (iTypMenge == MENGE_1) {
							positionlieferantDto.setNPreisMenge1(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_2) {
							positionlieferantDto.setNPreisMenge2(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_3) {
							positionlieferantDto.setNPreisMenge3(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_4) {
							positionlieferantDto.setNPreisMenge4(zeileDto.getPreis());
						}
						if (iTypMenge == MENGE_5) {
							positionlieferantDto.setNPreisMenge5(zeileDto.getPreis());
						}

						createPositionlieferant(positionlieferantDto, theClientDto);

					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// EKAG-Position suchen bzw. anlegen

		Query query = em.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
		query.setParameter(1, einkaufsangebotIId);
		Collection<?> cl = query.getResultList();

		EinkaufsangebotpositionDto[] posDtos = EinkaufsangebotpositionDtoAssembler.createDtos(cl);

		// Lieferant Suchen und anlegen

		EkaglieferantDto ekaglieferantDto = new EkaglieferantDto();

		/*
		 * //Positionlieferant anlegen
		 * 
		 * PositionlieferantDto positionlieferantDto = new PositionlieferantDto();
		 * 
		 * positionlieferantDto.setEinkaufsangebotpositionIId(einkaufsangebotpositionIId
		 * ); positionlieferantDto.setEgaklieferantIId(ekaglieferantIId);
		 * 
		 * // ArtikelnummerLieferant if (sZeilePositionen[15].getContents() != null) {
		 * positionlieferantDto
		 * .setCArtikelnrlieferant(sZeilePositionen[15].getContents()); }
		 * 
		 * positionlieferantDto.setNLagerstand(
		 * getBigDecimalFromCell(sZeilePositionen[16], iNachkommastellenMenge));
		 * 
		 * BigDecimal bdLieferzeit = getBigDecimalFromCell(sZeilePositionen[17], 0); if
		 * (bdLieferzeit != null) {
		 * positionlieferantDto.setILieferzeitinkw(bdLieferzeit.intValue()); }
		 * 
		 * positionlieferantDto.setNVerpackungseinheit(
		 * getBigDecimalFromCell(sZeilePositionen[18], iNachkommastellenMenge));
		 * positionlieferantDto.setNMindestbestellmenge(
		 * getBigDecimalFromCell(sZeilePositionen[19], iNachkommastellenMenge));
		 * positionlieferantDto.setNTransportkosten(
		 * getBigDecimalFromCell(sZeilePositionen[20], iNachkommastellenPreis));
		 * 
		 * positionlieferantDto.setNPreisMenge1(
		 * getBigDecimalFromCell(sZeilePositionen[21], iNachkommastellenPreis));
		 * positionlieferantDto.setNPreisMenge2(
		 * getBigDecimalFromCell(sZeilePositionen[22], iNachkommastellenPreis));
		 * positionlieferantDto.setNPreisMenge3(
		 * getBigDecimalFromCell(sZeilePositionen[23], iNachkommastellenPreis));
		 * positionlieferantDto.setNPreisMenge4(
		 * getBigDecimalFromCell(sZeilePositionen[24], iNachkommastellenPreis));
		 * positionlieferantDto.setNPreisMenge5(
		 * getBigDecimalFromCell(sZeilePositionen[25], iNachkommastellenPreis));
		 * 
		 * if (sZeilePositionen[26].getContents() != null) {
		 * positionlieferantDto.setCBemerkung(sZeilePositionen[26].getContents()); }
		 * 
		 * // Zuerst alten eintrag loeschen try { Query query1 = em.createNamedQuery(
		 * "PositionlieferantfindByEinkaufsangebotpositionIIdEgaklieferantIId");
		 * query1.setParameter(1, einkaufsangebotpositionIId); query1.setParameter(2,
		 * ekaglieferantIId); // @todo getSingleResult oder getResultList ? Integer
		 * iIdVorhanden = ((Positionlieferant) query1.getSingleResult()) .getIId();
		 * 
		 * PositionlieferantDto positionlieferantDtoVorhanden =
		 * positionlieferantFindByPrimaryKey( iIdVorhanden);
		 * removePositionlieferant(positionlieferantDtoVorhanden);
		 * 
		 * } catch (NoResultException ex) {
		 * 
		 * }
		 * 
		 * createPositionlieferant(positionlieferantDto, theClientDto);
		 * 
		 * }
		 */

		return lieferantAngelegt;

	}

	public ArrayList<String> leseLumiquoteXLSEin(Integer einkaufsangebotIId,
			TreeMap<BigDecimal, LinkedHashMap<String, ArrayList<ImportLumiQuoteXlsxDto>>> hmNachMengenUndLieferantenGruppiert,
			TheClientDto theClientDto) {

		ArrayList<String> alNeueLieferanten = new ArrayList<String>();

		EinkaufsangebotDto ekAGDto = einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		// Als erstes die Mengen in die Kopfdaten schreiben

		Iterator<BigDecimal> itMengen = hmNachMengenUndLieferantenGruppiert.keySet().iterator();

		if (itMengen.hasNext()) {
			BigDecimal bdMenge = itMengen.next();

			ekAGDto.setNMenge1(bdMenge);
			alNeueLieferanten.addAll(lumiQuoteZeileVerarbeiten(MENGE_1, einkaufsangebotIId,
					hmNachMengenUndLieferantenGruppiert.get(bdMenge), theClientDto));
		}

		if (itMengen.hasNext()) {
			BigDecimal bdMenge = itMengen.next();
			ekAGDto.setNMenge2(bdMenge);
			alNeueLieferanten.addAll(lumiQuoteZeileVerarbeiten(MENGE_2, einkaufsangebotIId,
					hmNachMengenUndLieferantenGruppiert.get(bdMenge), theClientDto));

		}

		if (itMengen.hasNext()) {
			BigDecimal bdMenge = itMengen.next();
			ekAGDto.setNMenge3(bdMenge);
			alNeueLieferanten.addAll(lumiQuoteZeileVerarbeiten(MENGE_3, einkaufsangebotIId,
					hmNachMengenUndLieferantenGruppiert.get(bdMenge), theClientDto));

		}

		if (itMengen.hasNext()) {
			BigDecimal bdMenge = itMengen.next();
			ekAGDto.setNMenge4(bdMenge);
			alNeueLieferanten.addAll(lumiQuoteZeileVerarbeiten(MENGE_4, einkaufsangebotIId,
					hmNachMengenUndLieferantenGruppiert.get(bdMenge), theClientDto));

		}

		if (itMengen.hasNext()) {
			BigDecimal bdMenge = itMengen.next();
			ekAGDto.setNMenge5(bdMenge);
			alNeueLieferanten.addAll(lumiQuoteZeileVerarbeiten(MENGE_5, einkaufsangebotIId,
					hmNachMengenUndLieferantenGruppiert.get(bdMenge), theClientDto));

		}

		updateEinkaufsangebot(ekAGDto);

		return alNeueLieferanten;
	}

	public void leseXLSEinesLieferantenEin(Integer ekaglieferantIId, byte[] xlsDatei, TheClientDto theClientDto) {
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			Workbook workbook = Workbook.getWorkbook(is, ws);

			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());

			Sheet sheet = workbook.getSheet(0);

			EkaglieferantDto ekaglieferantDto = ekaglieferantFindByPrimaryKey(ekaglieferantIId);
			LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(ekaglieferantDto.getLieferantIId(),
					theClientDto);
			EinkaufsangebotDto einkaufsangebotDto = einkaufsangebotFindByPrimaryKey(
					ekaglieferantDto.getEinkaufsangebotIId());

			Query query = em.createNamedQuery("EinkaufsangebotpositionfindByEinkaufsangebotIId");
			query.setParameter(1, einkaufsangebotDto.getIId());
			Collection<?> cl = query.getResultList();

			EinkaufsangebotpositionDto[] posDtos = EinkaufsangebotpositionDtoAssembler.createDtos(cl);

			if (lieferantDto.getIKreditorenkontoAsIntegerNotiId() != null) {
				if (sheet.getRows() > 6) {

					// Hier muessen die EK-AG Nr und die Kreditorennummer vorhanden sein
					Cell[] sZeileKopfdaten = sheet.getRow(2);

					if (sZeileKopfdaten.length > 19) {
						String ekAGNR = sZeileKopfdaten[0].getContents();

						String kreditorennummer = sZeileKopfdaten[17].getContents();
						String angebotsnummmerLF = sZeileKopfdaten[21].getContents();

						if (einkaufsangebotDto.getCNr().equals(ekAGNR)) {
							if ((lieferantDto.getIKreditorenkontoAsIntegerNotiId() + "").equals(kreditorennummer)) {

								LinkedHashMap<String, Cell[]> hmGruppiertNachArtikel = new LinkedHashMap<String, Cell[]>();

								for (int i = 6; i < sheet.getRows(); i++) {
									Cell[] sZeilePositionen = sheet.getRow(i);

									if (sZeilePositionen.length > 26) {

										BigDecimal preis1 = getBigDecimalFromCell(sZeilePositionen[21],
												iNachkommastellenPreis);
										BigDecimal preis2 = getBigDecimalFromCell(sZeilePositionen[22],
												iNachkommastellenPreis);
										BigDecimal preis3 = getBigDecimalFromCell(sZeilePositionen[23],
												iNachkommastellenPreis);
										BigDecimal preis4 = getBigDecimalFromCell(sZeilePositionen[24],
												iNachkommastellenPreis);
										BigDecimal preis5 = getBigDecimalFromCell(sZeilePositionen[25],
												iNachkommastellenPreis);

										if (preis1 != null || preis2 != null || preis3 != null || preis4 != null
												|| preis5 != null) {
											String artikelnummer = sZeilePositionen[2].getContents();

											if (artikelnummer.equals("~")) {
												artikelnummer += i + "";
											}

											if (hmGruppiertNachArtikel.containsKey(artikelnummer)) {
												// FEHLER
												ArrayList alDaten = new ArrayList();

												alDaten.add(artikelnummer);
												alDaten.add(i);

												throw new EJBExceptionLP(
														EJBExceptionLP.FEHLER_EKAGLIERANT_IMPORT_MEHRERE_BEFUELLTE_ZEILEN_EINES_ARTIKELS,
														alDaten, new Exception());
											} else {
												hmGruppiertNachArtikel.put(artikelnummer, sZeilePositionen);
											}

										}
									}

								}

								Iterator<String> it = hmGruppiertNachArtikel.keySet().iterator();

								while (it.hasNext()) {

									String artikelnummer = it.next();

									Cell[] sZeilePositionen = hmGruppiertNachArtikel.get(artikelnummer);

									// Spalte 3 muss die Artikelnummer sein

									String bezeichnungWennHandeingabe = sZeilePositionen[4].getContents();

									String zusatzbezeichnungWennHandeingabe = sZeilePositionen[5].getContents();
									if (zusatzbezeichnungWennHandeingabe == null) {
										zusatzbezeichnungWennHandeingabe = "";
									}

									// Mit diesen Daten die Position suchen

									Integer einkaufsangebotpositionIId = null;

									for (int p = 0; p < posDtos.length; p++) {

										String zusatzbezeichnungAusPosition = posDtos[p].getCZusatzbez();
										if (zusatzbezeichnungAusPosition == null) {
											zusatzbezeichnungAusPosition = "";
										}

										if (posDtos[p].getArtikelIId() != null && !artikelnummer.startsWith("~")) {

											ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
													posDtos[p].getArtikelIId(), theClientDto);

											if (aDto.getCNr().equals(artikelnummer)) {
												einkaufsangebotpositionIId = posDtos[p].getIId();
												break;
											}
										} else if (artikelnummer.startsWith("~") && posDtos[p].getCBez() != null
												&& posDtos[p].getCBez().equals(bezeichnungWennHandeingabe)
												&& zusatzbezeichnungWennHandeingabe
														.equals(zusatzbezeichnungAusPosition)) {
											// Handartikel

											// SP8278

											String herstellernummerXLS = "";
											String herstellernummerPOS = "";

											if (sZeilePositionen[8].getContents() != null) {
												herstellernummerXLS = sZeilePositionen[8].getContents();
											}

											if (posDtos[p].getCArtikelnrhersteller() != null) {
												herstellernummerPOS = posDtos[p].getCArtikelnrhersteller();
											}

											String[] herstellerTeilePOS = herstellernummerPOS.split("\\|");

											for (int i = 0; i < herstellerTeilePOS.length; i++) {

												if (herstellernummerXLS.equals(herstellerTeilePOS[i].trim())) {
													einkaufsangebotpositionIId = posDtos[p].getIId();
													break;
												}

											}

										}

									}

									if (einkaufsangebotpositionIId != null) {

										PositionlieferantDto positionlieferantDto = new PositionlieferantDto();

										positionlieferantDto.setEinkaufsangebotpositionIId(einkaufsangebotpositionIId);
										positionlieferantDto.setEgaklieferantIId(ekaglieferantIId);

										// ArtikelnummerLieferant
										if (sZeilePositionen[15].getContents() != null) {
											positionlieferantDto
													.setCArtikelnrlieferant(sZeilePositionen[15].getContents());
										}

										positionlieferantDto.setNLagerstand(
												getBigDecimalFromCell(sZeilePositionen[16], iNachkommastellenMenge));

										BigDecimal bdLieferzeit = getBigDecimalFromCell(sZeilePositionen[17], 0);
										if (bdLieferzeit != null) {
											positionlieferantDto.setILieferzeitinkw(bdLieferzeit.intValue());
										}

										positionlieferantDto.setNVerpackungseinheit(
												getBigDecimalFromCell(sZeilePositionen[18], iNachkommastellenMenge));
										positionlieferantDto.setNMindestbestellmenge(
												getBigDecimalFromCell(sZeilePositionen[19], iNachkommastellenMenge));
										positionlieferantDto.setNTransportkosten(
												getBigDecimalFromCell(sZeilePositionen[20], iNachkommastellenPreis));

										positionlieferantDto.setNPreisMenge1(
												getBigDecimalFromCell(sZeilePositionen[21], iNachkommastellenPreis));
										positionlieferantDto.setNPreisMenge2(
												getBigDecimalFromCell(sZeilePositionen[22], iNachkommastellenPreis));
										positionlieferantDto.setNPreisMenge3(
												getBigDecimalFromCell(sZeilePositionen[23], iNachkommastellenPreis));
										positionlieferantDto.setNPreisMenge4(
												getBigDecimalFromCell(sZeilePositionen[24], iNachkommastellenPreis));
										positionlieferantDto.setNPreisMenge5(
												getBigDecimalFromCell(sZeilePositionen[25], iNachkommastellenPreis));

										if (sZeilePositionen[26].getContents() != null) {
											positionlieferantDto.setCBemerkung(sZeilePositionen[26].getContents());
										}

										// Zuerst alten eintrag loeschen
										try {
											Query query1 = em.createNamedQuery(
													"PositionlieferantfindByEinkaufsangebotpositionIIdEgaklieferantIId");
											query1.setParameter(1, einkaufsangebotpositionIId);
											query1.setParameter(2, ekaglieferantIId);
											// @todo getSingleResult oder getResultList ?
											Integer iIdVorhanden = ((Positionlieferant) query1.getSingleResult())
													.getIId();

											PositionlieferantDto positionlieferantDtoVorhanden = positionlieferantFindByPrimaryKey(
													iIdVorhanden);
											removePositionlieferant(positionlieferantDtoVorhanden);

										} catch (NoResultException ex) {

										}

										createPositionlieferant(positionlieferantDto, theClientDto);

									}

								}

								ekaglieferantDto.setCAngebotsnummer(angebotsnummmerLF);
								ekaglieferantDto.setTImport(getTimestamp());
								updateEkaglieferant(ekaglieferantDto, theClientDto);

							} else {
								ArrayList alDaten = new ArrayList();

								alDaten.add(lieferantDto.getIKreditorenkontoAsIntegerNotiId() + "");
								alDaten.add(kreditorennummer);

								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_EKAGLIERANT_IMPORT_KREDITORENNUMMER_FALSCH, alDaten,
										new Exception());

							}

						}

					}

				}

			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private BigDecimal getBigDecimalFromCell(Cell c, int iNachkommastellen) {

		if (c != null && c.getContents() != null && c.getContents().length() > 0) {

			if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

				double d = ((NumberCell) c).getValue();
				return Helper.rundeKaufmaennisch(new BigDecimal(d), iNachkommastellen);

			}

		}
		return null;
	}

	public Integer erzeugeStuecklisteAusAgstkl(Integer agstklIId, String artikelnummerNeu, TheClientDto theClientDto) {

		Agstkl agstkl = em.find(Agstkl.class, agstklIId);

		ArtikelDto aDto = new ArtikelDto();
		aDto.setCNr(artikelnummerNeu);

		aDto.setCReferenznr(agstkl.getCZeichnungsnummer());

		ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
		oArtikelsprDto.setCBez(agstkl.getCBez());

		// PJ22552
		if (agstkl.getNInitialkosten() != null) {
			oArtikelsprDto.setCZbez2("IK: " + Helper.formatZahl(agstkl.getNInitialkosten(), theClientDto.getLocUi()));
		}

		aDto.setArtikelsprDto(oArtikelsprDto);

		aDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
		aDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
		aDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		aDto.setBVersteckt(Helper.boolean2Short(false));
		try {
			Integer artikelIId = getArtikelFac().createArtikel(aDto, theClientDto);

			StuecklisteDto stklDto = new StuecklisteDto();
			stklDto.setArtikelIId(artikelIId);

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

			stklDto.setBAusgabeunterstueckliste(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			parameter = (ParametermandantDto) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

			stklDto.setBMaterialbuchungbeiablieferung(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			FertigungsgruppeDto[] fertigungsgruppeDtos = getStuecklisteFac()
					.fertigungsgruppeFindByMandantCNr(theClientDto.getMandant(), theClientDto);

			if (fertigungsgruppeDtos.length > 0) {
				stklDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
			}
			stklDto.setLagerIIdZiellager(getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
			stklDto.setNLosgroesse(new BigDecimal(1));
			stklDto.setNErfassungsfaktor(new BigDecimal(1));

			stklDto.setBFremdfertigung(Helper.boolean2Short(false));

			stklDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

			Integer stuecklisteIId = getStuecklisteFac().createStueckliste(stklDto, theClientDto);

			AgstklarbeitsplanDto[] agstklarbeitsplan = agstklarbeitsplanFindByAgstklIId(agstklIId, theClientDto);
			for (int i = 0; i < agstklarbeitsplan.length; i++) {
				AgstklarbeitsplanDto apDto = agstklarbeitsplan[i];
				StuecklistearbeitsplanDto stkApDto = new StuecklistearbeitsplanDto();
				stkApDto.setStuecklisteIId(stuecklisteIId);

				stkApDto.setArtikelIId(apDto.getArtikelIId());
				stkApDto.setLRuestzeit(apDto.getLRuestzeit());
				stkApDto.setLStueckzeit(apDto.getLStueckzeit());
				stkApDto.setMaschineIId(apDto.getMaschineIId());
				stkApDto.setBNurmaschinenzeit(apDto.getBNurmaschinenzeit());
				stkApDto.setBInitial(apDto.getBInitial());
				stkApDto.setIArbeitsgang(apDto.getIArbeitsgang());
				stkApDto.setIUnterarbeitsgang(apDto.getIUnterarbeitsgang());
				stkApDto.setIAufspannung(apDto.getIAufspannung());
				stkApDto.setAgartCNr(apDto.getAgartCNr());
				stkApDto.setCKommentar(apDto.getCKommentar());
				stkApDto.setXLangtext(apDto.getXLangtext());

				getStuecklisteFac().createStuecklistearbeitsplan(stkApDto, theClientDto);

			}
			getStuecklisteFac().kopiereAusAgstkl(agstklIId, stuecklisteIId, theClientDto);

			agstkl.setStuecklisteIId(stuecklisteIId);

			em.merge(agstkl);
			em.flush();

			// OFFEN
			// VK-PReise anlegen

			Query query = em.createNamedQuery("AgstklmengenstaffelFindByAgstklIId");
			query.setParameter(1, agstklIId);

			Collection c = query.getResultList();
			Iterator it = c.iterator();

			while (it.hasNext()) {

				Agstklmengenstaffel agstklmengenstaffel = (Agstklmengenstaffel) it.next();

				if (agstklmengenstaffel.getNVkpreisGewaehlt() != null) {

					VkpfMengenstaffelDto vkpfMengenstaffelDto = new VkpfMengenstaffelDto();
					vkpfMengenstaffelDto.setArtikelIId(artikelIId);
					vkpfMengenstaffelDto.setNMenge(agstklmengenstaffel.getNMenge());
					vkpfMengenstaffelDto.setVkpfartikelpreislisteIId(null);

					vkpfMengenstaffelDto.setBAllepreislisten(Helper.boolean2Short(false));

					vkpfMengenstaffelDto.setTPreisgueltigab(getDate());

					vkpfMengenstaffelDto.setNArtikelfixpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							agstklmengenstaffel.getNVkpreisGewaehlt(), agstkl.getWaehrungCNr(),
							theClientDto.getSMandantenwaehrung(), getDate(), theClientDto));
					vkpfMengenstaffelDto.setFArtikelstandardrabattsatz(0D);

					getVkPreisfindungFac().createVkpfMengenstaffel(vkpfMengenstaffelDto, theClientDto);

				}

			}

			return stuecklisteIId;

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public LinkedHashMap<Integer, byte[]> erzeugeXLSFuerLieferanten(Integer einkaufsangebotIId, Integer lieferantIId,
			TheClientDto theClientDto) {

		LinkedHashMap<Integer, byte[]> hmXLS = new LinkedHashMap<Integer, byte[]>();

		Session sessionLF = FLRSessionFactory.getFactory().openSession();
		String queryStringLF = "SELECT lf FROM FLREkaglieferant AS lf WHERE lf.einkaufsangebot_i_id="
				+ einkaufsangebotIId;

		if (lieferantIId != null) {
			queryStringLF += " AND lf.lieferant_i_id=" + lieferantIId;
		}

		queryStringLF += " ORDER BY lf.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC";

		org.hibernate.Query queryLF = sessionLF.createQuery(queryStringLF);
		List<?> resultListLF = queryLF.list();

		Iterator<?> resultListIteratorLF = resultListLF.iterator();

		EinkaufsangebotDto einkaufsangebotDto = einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);

		while (resultListIteratorLF.hasNext()) {
			FLREkaglieferant lf = (FLREkaglieferant) resultListIteratorLF.next();

			LieferantDto lfDto = getLieferantFac().lieferantFindByPrimaryKey(lf.getLieferant_i_id(), theClientDto);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			WritableWorkbook workbook = null;
			try {
				WorkbookSettings ws = new WorkbookSettings();
				ws.setEncoding("Cp1252");

				workbook = Workbook.createWorkbook(os, ws);

			} catch (IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			}

			try {
				WritableSheet sheet = workbook.createSheet("Artikel", 0);

				sheet.getSettings().setVerticalFreeze(6);
				sheet.getSettings().setProtected(true);

				Session session = FLRSessionFactory.getFactory().openSession();

				session.enableFilter("filterMandant").setParameter("paramMandant", theClientDto.getMandant());
				session.enableFilter("filterLocale").setParameter("paramLocale",
						Helper.locale2String(theClientDto.getLocUi()));

				String queryString = "SELECT pos FROM FLREinkaufsangebotposition AS pos WHERE pos.einkaufsangebot_i_id="
						+ einkaufsangebotIId;
				queryString += " ORDER BY pos.i_sort ASC";

				org.hibernate.Query query = session.createQuery(queryString);

				List<?> resultList = query.list();

				Iterator<?> resultListIterator = resultList.iterator();

				// 1. Zeile = Ueberschrift
				VkpfartikelpreislisteDto[] vkpfartikelpreislisteDtos = null;
				try {
					vkpfartikelpreislisteDtos = getVkPreisfindungFac()
							.getAlleAktivenPreislisten(Helper.boolean2Short(true), theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				// Datumsformat
				DateFormat customDateFormat = new DateFormat("dd.MM.yyyy");
				WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);

				// Zahlenformat

				int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());

				String sNachkomma = "";
				for (int i = 0; i < iNachkommastellenPreis; i++) {
					sNachkomma += "#";
				}

				NumberFormat customNumberFormatPreis = new NumberFormat("#######0." + sNachkomma);

				int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());

				String sNachkommaMenge = "";
				for (int i = 0; i < iNachkommastellenMenge; i++) {
					sNachkommaMenge += "#";
				}

				workbook.setColourRGB(Colour.SKY_BLUE, 68, 114, 196);

				NumberFormat customNumberFormatMenge = new NumberFormat("#######0." + sNachkommaMenge);
				WritableCellFormat numberFormatMenge = new WritableCellFormat(customNumberFormatMenge);

				WritableCellFormat cellformatHintergrundBlau = new WritableCellFormat();
				cellformatHintergrundBlau.setBackground(Colour.SKY_BLUE);

				WritableFont cellFont = new WritableFont(cellformatHintergrundBlau.getFont());
				cellFont.setBoldStyle(WritableFont.BOLD);
				cellFont.setColour(Colour.WHITE);

				cellformatHintergrundBlau.setFont(cellFont);

				WritableCellFormat cellformatHintergrundGelb = new WritableCellFormat();
				cellformatHintergrundGelb.setBackground(Colour.YELLOW);

				WritableCellFormat cellformatHintergrundHellGelbString = new WritableCellFormat();
				cellformatHintergrundHellGelbString.setBackground(Colour.VERY_LIGHT_YELLOW);
				cellformatHintergrundHellGelbString.setLocked(false);

				WritableCellFormat cellformatHintergrundHellGelbPreis = new WritableCellFormat(customNumberFormatPreis);
				cellformatHintergrundHellGelbPreis.setBackground(Colour.VERY_LIGHT_YELLOW);
				cellformatHintergrundHellGelbPreis.setLocked(false);

				WritableCellFormat cellformatHintergrundHellGelbMenge = new WritableCellFormat(customNumberFormatMenge);
				cellformatHintergrundHellGelbMenge.setBackground(Colour.VERY_LIGHT_YELLOW);
				cellformatHintergrundHellGelbMenge.setLocked(false);

				WritableCellFormat cellformatHintergrundHellGelbMengeInteger = new WritableCellFormat(
						new NumberFormat("#######0"));
				cellformatHintergrundHellGelbMengeInteger.setBackground(Colour.VERY_LIGHT_YELLOW);
				cellformatHintergrundHellGelbMengeInteger.setLocked(false);

				// 1.Zeile Absenderdaten

				int iZeile = 0;

				MandantDto mDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

				String s = "";
				s = s + mDto.getPartnerDto().getCName1nachnamefirmazeile1();
				s = s + ", ";

				s = s + mDto.getPartnerDto().formatAdresse();

				sheet.addCell(new Label(0, iZeile, s, cellformatHintergrundBlau));
				sheet.mergeCells(0, iZeile, 5, iZeile);

				iZeile++;
				// 2. Zeile Ueberschriften EKAGSTKL + Lieferant
				sheet.addCell(new Label(0, iZeile, "Anfrageliste Nr.", cellformatHintergrundBlau));
				sheet.addCell(new Label(1, iZeile, "Projekt", cellformatHintergrundBlau));

				sheet.mergeCells(1, iZeile, 3, iZeile);

				if (Helper.short2boolean(einkaufsangebotDto.getBKundeExportieren())) {
					sheet.addCell(new Label(4, iZeile, "Kunde", cellformatHintergrundBlau));
				} else {
					sheet.addCell(new Label(4, iZeile, "", cellformatHintergrundBlau));
				}

				sheet.mergeCells(4, iZeile, 9, iZeile);

				sheet.addCell(new Label(10, iZeile, "Menge 1", cellformatHintergrundBlau));
				sheet.addCell(new Label(11, iZeile, "Menge 2", cellformatHintergrundBlau));
				sheet.addCell(new Label(12, iZeile, "Menge 3", cellformatHintergrundBlau));
				sheet.addCell(new Label(13, iZeile, "Menge 4", cellformatHintergrundBlau));
				sheet.addCell(new Label(14, iZeile, "Menge 5", cellformatHintergrundBlau));

				sheet.addCell(new Label(15, iZeile, "Lieferant", cellformatHintergrundGelb));
				sheet.mergeCells(15, iZeile, 16, iZeile);

				sheet.addCell(new Label(17, iZeile, "Kreditorennummer", cellformatHintergrundGelb));
				sheet.mergeCells(17, iZeile, 18, iZeile);
				sheet.addCell(new Label(19, iZeile, "Kundennummer", cellformatHintergrundGelb));
				sheet.mergeCells(19, iZeile, 20, iZeile);
				sheet.addCell(new Label(21, iZeile, "Angebotsnummer", cellformatHintergrundGelb));
				sheet.mergeCells(21, iZeile, 22, iZeile);

				iZeile++;
				// 3. Zeile Inhalt
				sheet.addCell(new Label(0, iZeile, lf.getFlreinkaufsangebot().getC_nr()));
				sheet.addCell(new Label(1, iZeile, lf.getFlreinkaufsangebot().getC_projekt()));
				sheet.mergeCells(1, iZeile, 3, iZeile);
				if (Helper.short2boolean(einkaufsangebotDto.getBKundeExportieren())) {

					sheet.addCell(new Label(4, iZeile, HelperServer
							.formatNameAusFLRPartner(lf.getFlreinkaufsangebot().getFlrkunde().getFlrpartner())));
				} else {
					sheet.addCell(new Label(4, iZeile, ""));
				}
				sheet.mergeCells(4, iZeile, 6, iZeile);
				sheet.addCell(new jxl.write.Number(10, iZeile, lf.getFlreinkaufsangebot().getN_menge1().doubleValue()));
				sheet.addCell(new jxl.write.Number(11, iZeile, lf.getFlreinkaufsangebot().getN_menge2().doubleValue()));
				sheet.addCell(new jxl.write.Number(12, iZeile, lf.getFlreinkaufsangebot().getN_menge3().doubleValue()));
				sheet.addCell(new jxl.write.Number(13, iZeile, lf.getFlreinkaufsangebot().getN_menge4().doubleValue()));
				sheet.addCell(new jxl.write.Number(14, iZeile, lf.getFlreinkaufsangebot().getN_menge5().doubleValue()));

				sheet.addCell(new Label(15, iZeile,
						HelperServer.formatNameAusFLRPartner(lf.getFlrlieferant().getFlrpartner())));
				sheet.mergeCells(15, iZeile, 16, iZeile);
				if (lf.getFlrlieferant().getFlrkonto() != null) {
					sheet.addCell(new Label(17, iZeile, lf.getFlrlieferant().getFlrkonto().getC_nr()));
				}
				sheet.mergeCells(17, iZeile, 18, iZeile);

				sheet.addCell(new Label(19, iZeile, lfDto.getCKundennr()));
				sheet.mergeCells(19, iZeile, 20, iZeile);
				sheet.addCell(new Label(21, iZeile, "", cellformatHintergrundHellGelbString));
				sheet.mergeCells(21, iZeile, 22, iZeile);
				iZeile++;
				// 4. Zeile LEER
				iZeile++;
				// 5. Zeile Ueberschriften Positionen
				iZeile++;

				sheet.addCell(new Label(0, iZeile, "Menge", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 0, 15);

				sheet.addCell(new Label(1, iZeile, "Einheit", cellformatHintergrundBlau));

				sheet.addCell(new Label(2, iZeile, "Artikelnummer", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 2, 20);

				sheet.addCell(new Label(3, iZeile, "Artikelgruppe", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 3, 20);

				sheet.addCell(new Label(4, iZeile, "Bezeichnung", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 4, 40);

				sheet.addCell(new Label(5, iZeile, "Zusatzbezeichnung", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 5, 40);

				sheet.addCell(new Label(6, iZeile, "Zusatzbezeichnung2", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 6, 40);

				sheet.addCell(new Label(7, iZeile, "Hersteller", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 7, 20);

				sheet.addCell(new Label(8, iZeile, "HerstellerArtikelnummer", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 8, 20);

				sheet.addCell(new Label(9, iZeile, "ArtikelnummerLieferant", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 9, 20);

				sheet.addCell(new Label(10, iZeile, "Menge 1", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 10, 13);

				sheet.addCell(new Label(11, iZeile, "Menge 2", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 11, 13);

				sheet.addCell(new Label(12, iZeile, "Menge 3", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 12, 13);

				sheet.addCell(new Label(13, iZeile, "Menge 4", cellformatHintergrundBlau));
				setzeSpaltenbreite(sheet, 13, 13);

				WritableCellFormat cellformatRandRechts = new WritableCellFormat();
				cellformatRandRechts.setBorder(Border.RIGHT, BorderLineStyle.MEDIUM);
				cellformatRandRechts.setBackground(Colour.SKY_BLUE);
				WritableFont cellFontRechts = new WritableFont(cellformatHintergrundBlau.getFont());
				cellFontRechts.setBoldStyle(WritableFont.BOLD);
				cellFontRechts.setColour(Colour.WHITE);

				cellformatRandRechts.setFont(cellFontRechts);

				sheet.addCell(new Label(14, iZeile, "Menge 5", cellformatRandRechts));
				setzeSpaltenbreite(sheet, 14, 13);

				// AB HIER VOM LIEF auszufuellen

				sheet.addCell(new Label(15, iZeile, "Lieferantenartikelnummer", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 15, 25);

				sheet.addCell(new Label(16, iZeile, "Lagerstand LF", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 16, 15);

				sheet.addCell(new Label(17, iZeile, "Lieferzeit in KW", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 17, 15);

				sheet.addCell(new Label(18, iZeile, "Verpackungseinheit", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 18, 15);

				sheet.addCell(new Label(19, iZeile, "Mindestbestellmenge", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 19, 15);
				sheet.addCell(new Label(20, iZeile, "Transportkosten", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 20, 15);

				sheet.addCell(
						new Label(21, iZeile, lf.getWaehrung_c_nr() + "/Einheit Menge 1", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 21, 20);

				sheet.addCell(
						new Label(22, iZeile, lf.getWaehrung_c_nr() + "/Einheit Menge 2", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 22, 20);

				sheet.addCell(
						new Label(23, iZeile, lf.getWaehrung_c_nr() + "/Einheit Menge 3", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 23, 20);

				sheet.addCell(
						new Label(24, iZeile, lf.getWaehrung_c_nr() + "/Einheit Menge 4", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 24, 20);

				sheet.addCell(
						new Label(25, iZeile, lf.getWaehrung_c_nr() + "/Einheit Menge 5", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 25, 20);

				sheet.addCell(new Label(26, iZeile, "Bemerkung", cellformatHintergrundGelb));
				setzeSpaltenbreite(sheet, 26, 100);

				while (resultListIterator.hasNext()) {

					FLREinkaufsangebotposition position = (FLREinkaufsangebotposition) resultListIterator.next();

					EinkaufsangebotpositionDto posDto = einkaufsangebotpositionFindByPrimaryKey(position.getI_id());

					String[] hersteller = null;
					if (posDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
							&& position.getFlrartikel() != null
							&& position.getFlrartikel().getC_artikelnrhersteller() != null) {

						hersteller = position.getFlrartikel().getC_artikelnrhersteller().split("\\|");

					} else if (posDto.getCArtikelnrhersteller() != null) {

						hersteller = posDto.getCArtikelnrhersteller().split("\\|");

					} else {
						hersteller = new String[] { "" };
					}

					for (int i = 0; i < hersteller.length; i++) {
						iZeile++;

						if (i == 0) {

							sheet.addCell(new jxl.write.Number(0, iZeile, position.getN_menge().doubleValue(),
									numberFormatMenge));
						} else {
							sheet.addCell(new Label(0, iZeile, ALTERNATIV));
						}
						if (position.getEinheit_c_nr() != null) {
							sheet.addCell(new Label(1, iZeile, position.getEinheit_c_nr().trim()));
						}
						ArtikelDto aDto = null;
						if (position.getFlrartikel() != null) {
							aDto = getArtikelFac().artikelFindByPrimaryKeySmall(position.getFlrartikel().getI_id(),
									theClientDto);
							sheet.addCell(new Label(2, iZeile, aDto.getCNr()));

							if (position.getFlrartikel().getFlrartikelgruppe() != null) {
								sheet.addCell(
										new Label(3, iZeile, position.getFlrartikel().getFlrartikelgruppe().getC_nr()));
							}

							sheet.addCell(new Label(4, iZeile, aDto.getCBezAusSpr()));
							sheet.addCell(new Label(5, iZeile, aDto.getCZBezAusSpr()));
							sheet.addCell(new Label(6, iZeile, aDto.getCZBez2AusSpr()));

							ArtikellieferantDto alDto = getArtikelFac()
									.artikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabOhneExc(aDto.getIId(),
											lf.getLieferant_i_id(),
											new Timestamp(lf.getFlreinkaufsangebot().getT_belegdatum().getTime()),
											theClientDto);

							if (alDto != null && alDto.getCArtikelnrlieferant() != null) {
								sheet.addCell(new Label(9, iZeile, alDto.getCArtikelnrlieferant()));
							}

						} else {

							sheet.addCell(new Label(2, iZeile, "~"));
							sheet.addCell(new Label(4, iZeile, position.getC_bez()));
							sheet.addCell(new Label(5, iZeile, position.getC_zbez()));
							sheet.addCell(new Label(6, iZeile, posDto.getCZbez2()));

						}

						if (posDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT) && aDto != null) {
							if (aDto.getHerstellerIId() != null) {
								HerstellerDto hstDto = getArtikelFac()
										.herstellerFindByPrimaryKey(aDto.getHerstellerIId(), theClientDto);
								sheet.addCell(new Label(7, iZeile, hstDto.getCNr()));
							}

							sheet.addCell(new Label(8, iZeile, hersteller[i].trim()));
						} else {
							if (posDto.getHerstellerIId() != null) {
								HerstellerDto hstDto = getArtikelFac()
										.herstellerFindByPrimaryKey(posDto.getHerstellerIId(), theClientDto);
								sheet.addCell(new Label(7, iZeile, hstDto.getCNr()));
							}

							sheet.addCell(new Label(8, iZeile, hersteller[i].trim()));
						}

						if (i == 0) {

							sheet.addCell(new jxl.write.Number(
									10, iZeile, position.getN_menge()
											.multiply(position.getFlreinkaufsangebot().getN_menge1()).doubleValue(),
									numberFormatMenge));
							sheet.addCell(new jxl.write.Number(
									11, iZeile, position.getN_menge()
											.multiply(position.getFlreinkaufsangebot().getN_menge2()).doubleValue(),
									numberFormatMenge));
							sheet.addCell(new jxl.write.Number(
									12, iZeile, position.getN_menge()
											.multiply(position.getFlreinkaufsangebot().getN_menge3()).doubleValue(),
									numberFormatMenge));
							sheet.addCell(new jxl.write.Number(
									13, iZeile, position.getN_menge()
											.multiply(position.getFlreinkaufsangebot().getN_menge4()).doubleValue(),
									numberFormatMenge));
							sheet.addCell(new jxl.write.Number(
									14, iZeile, position.getN_menge()
											.multiply(position.getFlreinkaufsangebot().getN_menge5()).doubleValue(),
									numberFormatMenge));

						} else {
							sheet.addCell(new jxl.write.Number(10, iZeile, 0, numberFormatMenge));
							sheet.addCell(new jxl.write.Number(11, iZeile, 0, numberFormatMenge));
							sheet.addCell(new jxl.write.Number(12, iZeile, 0, numberFormatMenge));
							sheet.addCell(new jxl.write.Number(13, iZeile, 0, numberFormatMenge));
							sheet.addCell(new jxl.write.Number(14, iZeile, 0, numberFormatMenge));
						}

						// LFArtNr
						sheet.addCell(new jxl.write.Blank(15, iZeile, cellformatHintergrundHellGelbString));
						// Lagerstand
						sheet.addCell(new jxl.write.Blank(16, iZeile, cellformatHintergrundHellGelbMenge));
						// Lieferzeit in Wochen
						sheet.addCell(new jxl.write.Blank(17, iZeile, cellformatHintergrundHellGelbMengeInteger));
						// Verpackungseinheit
						sheet.addCell(new jxl.write.Blank(18, iZeile, cellformatHintergrundHellGelbMengeInteger));
						// Mindestbestellmenge
						sheet.addCell(new jxl.write.Blank(19, iZeile, cellformatHintergrundHellGelbMengeInteger));

						// Transportkosten
						sheet.addCell(new jxl.write.Blank(20, iZeile, cellformatHintergrundHellGelbPreis));
						// Preise
						sheet.addCell(new jxl.write.Blank(21, iZeile, cellformatHintergrundHellGelbPreis));
						sheet.addCell(new jxl.write.Blank(22, iZeile, cellformatHintergrundHellGelbPreis));
						sheet.addCell(new jxl.write.Blank(23, iZeile, cellformatHintergrundHellGelbPreis));
						sheet.addCell(new jxl.write.Blank(24, iZeile, cellformatHintergrundHellGelbPreis));
						sheet.addCell(new jxl.write.Blank(25, iZeile, cellformatHintergrundHellGelbPreis));
						// Bemerkung
						sheet.addCell(new jxl.write.Blank(26, iZeile, cellformatHintergrundHellGelbString));

						WritableCellFormat cellFormatUmlocked = new WritableCellFormat();
						cellFormatUmlocked.setLocked(false);
					}
				}

				session.close();
				workbook.write();
				workbook.close();
			} catch (IOException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);

			} catch (RowsExceededException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			} catch (WriteException e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			}

			hmXLS.put(lfDto.getIId(), os.toByteArray());

		}
		return hmXLS;
	}

}
