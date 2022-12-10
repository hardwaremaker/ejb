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
package com.lp.server.stueckliste.ejbfac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
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

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotServiceFac;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentardruck;
import com.lp.server.artikel.ejb.Geometrie;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.artikel.fastlanereader.generated.FLRGeometrie;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.benutzer.ejb.Fertigungsgrupperolle;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.ejb.Los;
import com.lp.server.fertigung.ejb.Lossollarbeitsplan;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.ejb.Alternativmaschine;
import com.lp.server.stueckliste.ejb.Apkommentar;
import com.lp.server.stueckliste.ejb.Apkommentarspr;
import com.lp.server.stueckliste.ejb.ApkommentarsprPK;
import com.lp.server.stueckliste.ejb.Arbeitsgangart;
import com.lp.server.stueckliste.ejb.Fertigungsgruppe;
import com.lp.server.stueckliste.ejb.Kommentarimport;
import com.lp.server.stueckliste.ejb.Montageart;
import com.lp.server.stueckliste.ejb.Posersatz;
import com.lp.server.stueckliste.ejb.Pruefart;
import com.lp.server.stueckliste.ejb.Pruefartspr;
import com.lp.server.stueckliste.ejb.PruefartsprPK;
import com.lp.server.stueckliste.ejb.Pruefkombination;
import com.lp.server.stueckliste.ejb.Pruefkombinationspr;
import com.lp.server.stueckliste.ejb.PruefkombinationsprPK;
import com.lp.server.stueckliste.ejb.Stklagerentnahme;
import com.lp.server.stueckliste.ejb.Stklparameter;
import com.lp.server.stueckliste.ejb.Stklparameterspr;
import com.lp.server.stueckliste.ejb.StklparametersprPK;
import com.lp.server.stueckliste.ejb.Stklpruefplan;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.ejb.StuecklisteQuery;
import com.lp.server.stueckliste.ejb.StuecklisteScriptart;
import com.lp.server.stueckliste.ejb.Stuecklistearbeitsplan;
import com.lp.server.stueckliste.ejb.Stuecklisteart;
import com.lp.server.stueckliste.ejb.Stuecklisteeigenschaft;
import com.lp.server.stueckliste.ejb.Stuecklisteeigenschaftart;
import com.lp.server.stueckliste.ejb.Stuecklisteposition;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPosersatz;
import com.lp.server.stueckliste.fastlanereader.generated.FLRPruefkombination;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.AlternativmaschineDto;
import com.lp.server.stueckliste.service.AlternativmaschineDtoAssembler;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.ApkommentarDtoAssembler;
import com.lp.server.stueckliste.service.ApkommentarsprDto;
import com.lp.server.stueckliste.service.ApkommentarsprDtoAssembler;
import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDtoAssembler;
import com.lp.server.stueckliste.service.FlatPartlistInfo;
import com.lp.server.stueckliste.service.FlatPartlistPositionInfo;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.KommentarimportDto;
import com.lp.server.stueckliste.service.KommentarimportDtoAssembler;
import com.lp.server.stueckliste.service.KundenStuecklistepositionDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.MontageartDtoAssembler;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.PosersatzDtoAssembler;
import com.lp.server.stueckliste.service.PruefartDto;
import com.lp.server.stueckliste.service.PruefartDtoAssembler;
import com.lp.server.stueckliste.service.PruefartsprDto;
import com.lp.server.stueckliste.service.PruefartsprDtoAssembler;
import com.lp.server.stueckliste.service.PruefkombinationDto;
import com.lp.server.stueckliste.service.PruefkombinationDtoAssembler;
import com.lp.server.stueckliste.service.PruefkombinationsprDto;
import com.lp.server.stueckliste.service.PruefkombinationsprDtoAssembler;
import com.lp.server.stueckliste.service.StklINFRAHelperDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDto;
import com.lp.server.stueckliste.service.StklagerentnahmeDtoAssembler;
import com.lp.server.stueckliste.service.StklparameterDto;
import com.lp.server.stueckliste.service.StklparameterDtoAssembler;
import com.lp.server.stueckliste.service.StklparametersprDto;
import com.lp.server.stueckliste.service.StklparametersprDtoAssembler;
import com.lp.server.stueckliste.service.StklpruefplanDto;
import com.lp.server.stueckliste.service.StklpruefplanDtoAssembler;
import com.lp.server.stueckliste.service.StrukturDatenParamDto;
import com.lp.server.stueckliste.service.StrukturDatenParamDto.Sort;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklisteAufgeloest;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteFacLocal;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklisteScriptartDto;
import com.lp.server.stueckliste.service.StuecklisteScriptartDtoAssembler;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteartDto;
import com.lp.server.stueckliste.service.StuecklisteartDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDtoAssembler;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Einheit;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.KundeId;
import com.lp.server.util.ReportSqlExecutor;
import com.lp.server.util.StuecklisteId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.service.plscript.DebuggingScriptRuntime;
import com.lp.service.plscript.ScriptRuntime;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.CellReferenceHelper;
import jxl.read.biff.BiffException;

@Stateless
public class StuecklisteFacBean extends Facade implements StuecklisteFac, IImportPositionen, IImportHead {
	private static final long serialVersionUID = -6240506749470323303L;

	@PersistenceContext
	private EntityManager em;

	@EJB
	private transient StuecklisteFacLocal stuecklisteFacLocalBean;

	public Integer createMontageart(MontageartDto montageartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("montageartDto == null"));
		}
		if (montageartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("montageartDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("MontageartfindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, montageartDto.getCBez());
			Montageart doppelt = (Montageart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_MONTAGEART.C_BEZ"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MONATAGEART);
		montageartDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("MontageartejbSelectNextReihung");
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
		montageartDto.setISort(i);
		montageartDto.setMandantCNr(theClientDto.getMandant());
		try {
			Montageart montageart = new Montageart(montageartDto.getIId(), montageartDto.getCBez(),
					montageartDto.getMandantCNr(), montageartDto.getISort());
			em.persist(montageart);
			em.flush();
			setMontageartFromMontageartDto(montageart, montageartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return montageartDto.getIId();
	}

	public Integer createPosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto) {

		if (posersatzDto.getArtikelIIdErsatz() == null || posersatzDto.getStuecklistepositionIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"posersatzDto.getArtikelIIdErsatz() == null || posersatzDto.getStuecklistepositionIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("PosersatzfindByStuecklistepositionIIdArtikelIIdErsatz");
			query.setParameter(1, posersatzDto.getStuecklistepositionIId());
			query.setParameter(2, posersatzDto.getArtikelIIdErsatz());
			Posersatz doppelt = (Posersatz) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STKL_POSERSATZ.UC"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_POSERSATZ);
		posersatzDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("PosersatzejbSelectNextReihung");
			querynext.setParameter(1, posersatzDto.getStuecklistepositionIId());
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
		posersatzDto.setISort(i);
		try {
			Posersatz posersatz = new Posersatz(posersatzDto.getIId(), posersatzDto.getStuecklistepositionIId(),
					posersatzDto.getArtikelIIdErsatz(), posersatzDto.getISort());
			em.persist(posersatz);
			em.flush();
			setPosersatzFromPosersatzDto(posersatz, posersatzDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		HvDtoLogger<PosersatzDto> hvLogger = new HvDtoLogger<PosersatzDto>(em,
				stuecklistepositionFindByPrimaryKey(posersatzDto.getStuecklistepositionIId(), theClientDto)
						.getStuecklisteIId(),
				theClientDto);
		hvLogger.logInsert(posersatzDto);

		return posersatzDto.getIId();
	}

	public List<Integer> getMoeglicheMaschinen(Integer lossollarbeitsplanIId, TheClientDto theClientDto) {

		// Zuerst arbeitsplan suchen
		Lossollarbeitsplan la = em.find(Lossollarbeitsplan.class, lossollarbeitsplanIId);
		Los l = em.find(Los.class, la.getLosIId());

		Integer stuecklistearbeitsplanIId = null;

		ArrayList<Integer> moeglicheMaschinen = new ArrayList<Integer>();

		ArrayList<StuecklistearbeitsplanDto> alStuecklistePositionen = new ArrayList<StuecklistearbeitsplanDto>();

		// nun den gesamten Arbeitsplan der Stueckliste ins Los
		// kopieren

		if (l.getStuecklisteIId() != null) {

			StuecklistearbeitsplanDto[] stkPos = getStuecklisteFac()
					.stuecklistearbeitsplanFindByStuecklisteIId(l.getStuecklisteIId(), theClientDto);
			for (int i = 0; i < stkPos.length; i++) {
				alStuecklistePositionen.add(stkPos[i]);
			}
			for (int i = 0; i < alStuecklistePositionen.size(); i++) {
				if (la.getIArbeitsgangnummer().equals(alStuecklistePositionen.get(i).getIArbeitsgang())) {

					boolean bUnterarbeitsgangStimmtZusammen = false;

					if (la.getIUnterarbeitsgang() == null
							&& alStuecklistePositionen.get(i).getIUnterarbeitsgang() == null) {
						bUnterarbeitsgangStimmtZusammen = true;
					} else {
						if (la.getIUnterarbeitsgang() != null && la.getIUnterarbeitsgang()
								.equals(alStuecklistePositionen.get(i).getIUnterarbeitsgang())) {
							bUnterarbeitsgangStimmtZusammen = true;
						}
					}

					if (la.getArtikelIIdTaetigkeit().equals(alStuecklistePositionen.get(i).getArtikelIId())
							&& bUnterarbeitsgangStimmtZusammen) {

						stuecklistearbeitsplanIId = alStuecklistePositionen.get(i).getIId();

						if (alStuecklistePositionen.get(i).getMaschineIId() != null) {
							moeglicheMaschinen.add(alStuecklistePositionen.get(i).getMaschineIId());
						}

						break;

					}
				}
			}
		}

		if (stuecklistearbeitsplanIId != null) {

			Query query = em.createNamedQuery("AlternativmaschineFindByStuecklistearbeitsplanIId");
			query.setParameter(1, stuecklistearbeitsplanIId);

			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Alternativmaschine a = (Alternativmaschine) it.next();

				moeglicheMaschinen.add(a.getMaschineIId());

			}

		}
		return moeglicheMaschinen;
	}

	public AlternativmaschineDto[] alternativmaschineFindByStuecklistearbeitsplanIId(
			Integer stuecklistearbeitsplanIId) {
		Query query = em.createNamedQuery("AlternativmaschineFindByStuecklistearbeitsplanIId");
		query.setParameter(1, stuecklistearbeitsplanIId);
		Collection c = query.getResultList();
		return AlternativmaschineDtoAssembler.createDtos(c);
	}

	public Integer createAlternativmaschine(AlternativmaschineDto alternativmaschineDto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("AlternativmaschineFindByStuecklistearbeitsplanIIdMaschineIId");
			query.setParameter(1, alternativmaschineDto.getStuecklistearbeitsplanIId());
			query.setParameter(2, alternativmaschineDto.getMaschineIId());
			Alternativmaschine doppelt = (Alternativmaschine) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STK_ALTERNATIVMASCHINE.UK"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ALTERNATIVMASCHINE);
		alternativmaschineDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("AlternativmaschineejbSelectNextReihung");
			querynext.setParameter(1, alternativmaschineDto.getStuecklistearbeitsplanIId());
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
		alternativmaschineDto.setISort(i);
		try {
			Alternativmaschine alternativmaschine = new Alternativmaschine(alternativmaschineDto.getIId(),
					alternativmaschineDto.getStuecklistearbeitsplanIId(), alternativmaschineDto.getMaschineIId(),
					alternativmaschineDto.getNKorrekturfaktor(), alternativmaschineDto.getISort());
			em.persist(alternativmaschine);
			em.flush();
			setAlternativmaschineFromAlternativmaschineDto(alternativmaschine, alternativmaschineDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return alternativmaschineDto.getIId();
	}

	public void vertauscheStuecklisteposition(Integer iIdPosition1I, Integer iIdPosition2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Stuecklisteposition oPosition1 = em.find(Stuecklisteposition.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklisteposition oPosition2 = em.find(Stuecklisteposition.class, iIdPosition2I);
		if (oPosition2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void vertauscheAlternativmaschine(Integer iIdPosition1I, Integer iIdPosition2I) {

		Alternativmaschine oPosition1 = em.find(Alternativmaschine.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Alternativmaschine oPosition2 = em.find(Alternativmaschine.class, iIdPosition2I);
		if (oPosition2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);

	}

	public void vertauscheMontageart(Integer iIdMontageart1I, Integer iIdMontageart2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Montageart oMontageart1 = em.find(Montageart.class, iIdMontageart1I);
		if (oMontageart1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Montageart oMontageart2 = em.find(Montageart.class, iIdMontageart2I);
		if (oMontageart2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oMontageart1.getISort();
		Integer iSort2 = oMontageart2.getISort();

		oMontageart2.setISort(new Integer(-1));

		oMontageart1.setISort(iSort2);
		oMontageart2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void vertauscheStklpruefplan(Integer iId1, Integer iId2) {

		Stklpruefplan o1 = em.find(Stklpruefplan.class, iId1);
		if (o1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stklpruefplan o2 = em.find(Stklpruefplan.class, iId2);
		if (o2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));

		o1.setISort(iSort2);
		o2.setISort(iSort1);

	}

	public void vertauscheStklparameter(Integer iId1, Integer iId2) {

		Stklparameter o1 = em.find(Stklparameter.class, iId1);
		if (o1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stklparameter o2 = em.find(Stklparameter.class, iId2);
		if (o2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = o1.getISort();
		Integer iSort2 = o2.getISort();

		o2.setISort(new Integer(-1));

		o1.setISort(iSort2);
		o2.setISort(iSort1);

	}

	public void vertauschePosersatz(Integer iIdPosersatz1I, Integer iIdPosersatz2I) {
		Posersatz oPosersatz1 = em.find(Posersatz.class, iIdPosersatz1I);
		if (oPosersatz1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Posersatz oPosersatz2 = em.find(Posersatz.class, iIdPosersatz2I);
		if (oPosersatz2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosersatz1.getISort();
		Integer iSort2 = oPosersatz2.getISort();

		oPosersatz2.setISort(new Integer(-1));

		oPosersatz1.setISort(iSort2);
		oPosersatz2.setISort(iSort1);

	}

	public void vertauscheStuecklisteeigenschaftart(Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Stuecklisteeigenschaftart oStuecklisteeigenschaftart1 = em.find(Stuecklisteeigenschaftart.class,
				iIdStuecklisteeigenschaftart1I);
		if (oStuecklisteeigenschaftart1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklisteeigenschaftart oStuecklisteeigenschaftart2 = em.find(Stuecklisteeigenschaftart.class,
				iIdStuecklisteeigenschaftart2I);
		if (oStuecklisteeigenschaftart2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oStuecklisteeigenschaftart1.getISort();
		Integer iSort2 = oStuecklisteeigenschaftart2.getISort();

		oStuecklisteeigenschaftart2.setISort(new Integer(-1));

		oStuecklisteeigenschaftart1.setISort(iSort2);
		oStuecklisteeigenschaftart2.setISort(iSort1);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public void removeMontageart(MontageartDto montageartDto) throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("montageartDto == null"));
		}
		if (montageartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("montageartDto.getIId() == null"));
		}
		// try {
		Montageart toRemove = em.find(Montageart.class, montageartDto.getIId());
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
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void removePosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto) {

		Posersatz toRemove = em.find(Posersatz.class, posersatzDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<PosersatzDto> hvLogger = new HvDtoLogger<PosersatzDto>(em,
				stuecklistepositionFindByPrimaryKey(posersatzDto.getStuecklistepositionIId(), theClientDto)
						.getStuecklisteIId(),
				theClientDto);
		hvLogger.logDelete(posersatzDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeAlternativmaschine(AlternativmaschineDto dto) {

		Alternativmaschine toRemove = em.find(Alternativmaschine.class, dto.getIId());
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

	public void updateMontageart(MontageartDto montageartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("montageartDto == null"));
		}
		if (montageartDto.getIId() == null || montageartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("montageartDto.getIId() == null || montageartDto.getCBez() == null"));
		}
		Integer iId = montageartDto.getIId();
		Montageart montageart = null;
		// try {
		montageart = em.find(Montageart.class, iId);
		if (montageart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

		try {
			Query query = em.createNamedQuery("MontageartfindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, montageartDto.getCBez());
			Integer iIdVorhanden = ((Montageart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_MONTAGEART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setMontageartFromMontageartDto(montageart, montageartDto);
	}

	public void updatePosersatz(PosersatzDto posersatzDto, TheClientDto theClientDto) {
		if (posersatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("montageartDto == null"));
		}
		if (posersatzDto.getIId() == null || posersatzDto.getArtikelIIdErsatz() == null
				|| posersatzDto.getStuecklistepositionIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"posersatzDto.getIId() == null || posersatzDto.getArtikelIIdErsatz() == null || posersatzDto.getStuecklistepositionIId() == null"));
		}
		Integer iId = posersatzDto.getIId();
		Posersatz posersatz = em.find(Posersatz.class, iId);
		if (posersatz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em.createNamedQuery("PosersatzfindByStuecklistepositionIIdArtikelIIdErsatz");
			query.setParameter(1, posersatzDto.getStuecklistepositionIId());
			query.setParameter(2, posersatzDto.getArtikelIIdErsatz());
			Integer iIdVorhanden = ((Posersatz) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STKL_POSERSATZ.UC"));
			}

		} catch (NoResultException ex) {
			// nix
		}

		PosersatzDto posersatzDto_vorher = posersatzFindByPrimaryKey(posersatzDto.getIId(), theClientDto);

		HvDtoLogger<PosersatzDto> stuecklisteLogger = new HvDtoLogger<PosersatzDto>(em,
				stuecklistepositionFindByPrimaryKey(posersatzDto.getStuecklistepositionIId(), theClientDto)
						.getStuecklisteIId(),
				theClientDto);
		stuecklisteLogger.log(posersatzDto_vorher, posersatzDto);

		setPosersatzFromPosersatzDto(posersatz, posersatzDto);
	}

	public void updateAlternativmaschine(AlternativmaschineDto alternativmaschineDto, TheClientDto theClientDto) {

		Integer iId = alternativmaschineDto.getIId();
		Alternativmaschine alternativmaschine = em.find(Alternativmaschine.class, iId);
		if (alternativmaschine == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em.createNamedQuery("AlternativmaschineFindByStuecklistearbeitsplanIIdMaschineIId");
			query.setParameter(1, alternativmaschineDto.getStuecklistearbeitsplanIId());
			query.setParameter(2, alternativmaschineDto.getMaschineIId());
			Integer iIdVorhanden = ((Alternativmaschine) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_ALTERNATIVMASCHINE.UK"));
			}

		} catch (NoResultException ex) {
			// nix
		}

		setAlternativmaschineFromAlternativmaschineDto(alternativmaschine, alternativmaschineDto);
	}

	public MontageartDto montageartFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Montageart montageart = em.find(Montageart.class, iId);
		if (montageart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMontageartDto(montageart);
	}

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Posersatz posersatz = em.find(Posersatz.class, iId);
		if (posersatz == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePosersatzDto(posersatz);

	}

	public AlternativmaschineDto alternativmaschineFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Alternativmaschine bean = em.find(Alternativmaschine.class, iId);
		if (bean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return AlternativmaschineDtoAssembler.createDto(bean);

	}

	public PosersatzDto[] posersatzFindByStuecklistepositionIId(Integer stuecklistepositionIId) {

		Query query = em.createNamedQuery("PosersatzfindByStuecklistepositionIId");
		query.setParameter(1, stuecklistepositionIId);
		return assemblePosersatzDtos(query.getResultList());

	}

	public MontageartDto[] montageartFindByMandantCNr(TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("MontageartfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		return assembleMontageartDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }

	}

	public MontageartDto montageartFindByMandantCNrCBez(String cBez, TheClientDto theClientDto) throws EJBExceptionLP {
		if (cBez == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception("cBez == null"));
		}
		// try {
		Query query = em.createNamedQuery("MontageartfindByMandantCNrCBez");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cBez);
		Montageart montageart = (Montageart) query.getSingleResult();
		if (montageart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMontageartDto(montageart);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public MontageartDto montageartFindByMandantCNrCBezOhneExc(String mandantCNr, String cBez) throws EJBExceptionLP {
		if (cBez == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception("cBez == null"));
		}
		try {
			Query query = em.createNamedQuery("MontageartfindByMandantCNrCBez");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, cBez);
			Montageart montageart = (Montageart) query.getSingleResult();
			if (montageart == null) {
				return null;
			}
			return assembleMontageartDto(montageart);
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setMontageartFromMontageartDto(Montageart montageart, MontageartDto montageartDto) {
		montageart.setCBez(montageartDto.getCBez());
		montageart.setISort(montageartDto.getISort());
		montageart.setMandantCNr(montageartDto.getMandantCNr());
		montageart.setArtikelIId(montageartDto.getArtikelIId());
		em.merge(montageart);
		em.flush();
	}

	private void setKommentarimportFromKommentarimportDto(Kommentarimport kommentarimport,
			KommentarimportDto kommentarimportDto) {
		kommentarimport.setBelegartCNr(kommentarimportDto.getBelegartCNr());
		kommentarimport.setArtikelkommentarartIId(kommentarimportDto.getArtikelkommentarartIId());
		em.merge(kommentarimport);
		em.flush();
	}

	private void setAlternativmaschineFromAlternativmaschineDto(Alternativmaschine alternativmaschine,
			AlternativmaschineDto alternativmaschineDto) {
		alternativmaschine.setMaschineIId(alternativmaschineDto.getMaschineIId());
		alternativmaschine.setStuecklistearbeitsplanIId(alternativmaschineDto.getStuecklistearbeitsplanIId());
		alternativmaschine.setISort(alternativmaschineDto.getISort());
		alternativmaschine.setNKorrekturfaktor(alternativmaschineDto.getNKorrekturfaktor());
		em.merge(alternativmaschine);
		em.flush();
	}

	private void setPosersatzFromPosersatzDto(Posersatz posersatz, PosersatzDto posersatzDto) {
		posersatz.setArtikelIIdErsatz(posersatzDto.getArtikelIIdErsatz());
		posersatz.setISort(posersatzDto.getISort());
		posersatz.setStuecklistepositionIId(posersatzDto.getStuecklistepositionIId());
		em.merge(posersatz);
		em.flush();
	}

	private MontageartDto assembleMontageartDto(Montageart montageart) {
		return MontageartDtoAssembler.createDto(montageart);
	}

	private PosersatzDto assemblePosersatzDto(Posersatz posersatz) {
		return PosersatzDtoAssembler.createDto(posersatz);
	}

	private PosersatzDto[] assemblePosersatzDtos(Collection<?> stuecklistes) {
		List<PosersatzDto> list = new ArrayList<PosersatzDto>();
		if (stuecklistes != null) {
			Iterator<?> iterator = stuecklistes.iterator();
			while (iterator.hasNext()) {
				Posersatz stueckliste = (Posersatz) iterator.next();
				list.add(assemblePosersatzDto(stueckliste));
			}
		}
		PosersatzDto[] returnArray = new PosersatzDto[list.size()];
		return (PosersatzDto[]) list.toArray(returnArray);
	}

	private MontageartDto[] assembleMontageartDtos(Collection<?> montagearts) {
		List<MontageartDto> list = new ArrayList<MontageartDto>();
		if (montagearts != null) {
			Iterator<?> iterator = montagearts.iterator();
			while (iterator.hasNext()) {
				Montageart montageart = (Montageart) iterator.next();
				list.add(assembleMontageartDto(montageart));
			}
		}
		MontageartDto[] returnArray = new MontageartDto[list.size()];
		return (MontageartDto[]) list.toArray(returnArray);
	}

	public Integer createStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto.getCBez() == null"));
		}
		try {
			Query query = em.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, stuecklisteeigenschaftartDto.getCBez());
			Stuecklisteeigenschaftart doppelt = (Stuecklisteeigenschaftart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_STUECKLISTEEIGENSCHAFTART.C_BEZ"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEEIGENSCHAFTART);
		stuecklisteeigenschaftartDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("StuecklisteeigenschaftartejbSelectNextReihung");
			i = (Integer) querynext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		stuecklisteeigenschaftartDto.setISort(i);

		try {
			Stuecklisteeigenschaftart stuecklisteeigenschaftart = new Stuecklisteeigenschaftart(
					stuecklisteeigenschaftartDto.getIId(), stuecklisteeigenschaftartDto.getCBez(),
					stuecklisteeigenschaftartDto.getISort());
			em.persist(stuecklisteeigenschaftart);
			em.flush();
			setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(stuecklisteeigenschaftart,
					stuecklisteeigenschaftartDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return stuecklisteeigenschaftartDto.getIId();

	}

	public void removeStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws EJBExceptionLP {
		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto.getIId() == null"));
		}
		// try {
		Stuecklisteeigenschaftart toRemove = em.find(Stuecklisteeigenschaftart.class,
				stuecklisteeigenschaftartDto.getIId());
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
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateStuecklisteeigenschaftart(StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getIId() == null || stuecklisteeigenschaftartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklisteeigenschaftartDto.getIId() == null || stuecklisteeigenschaftartDto.getCBez() == null"));
		}

		Integer iId = stuecklisteeigenschaftartDto.getIId();
		// try {
		Stuecklisteeigenschaftart stuecklisteeigenschaftart = em.find(Stuecklisteeigenschaftart.class, iId);
		if (stuecklisteeigenschaftart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, stuecklisteeigenschaftartDto.getCBez());
			Integer iIdVorhanden = ((Stuecklisteeigenschaftart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_STUECKLISTEEIGENSCHAFTART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(stuecklisteeigenschaftart,
				stuecklisteeigenschaftartDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public void updatePruefkombination(PruefkombinationDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		Pruefkombination bean = em.find(Pruefkombination.class, iId);

		dto.setTAendern(new Timestamp(System.currentTimeMillis()));

		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		try {

			PruefartDto pruefartDto = pruefartFindByPrimaryKey(dto.getPruefartIId(), theClientDto);

			if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
				if (dto.getVerschleissteilIId() == null || dto.getArtikelIIdLitze() == null) {

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
							new Exception("dto.getVerschleissteilIId() == null || dto.getArtikelIIdLitze() == null"));
				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
					if (dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null
							|| dto.getNCrimpbreiteIsolation() == null || dto.getNCrimphoeheIsolation() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null || dto.getNCrimpbreiteIsolation() == null || dto.getNCrimphoeheIsolation() == null"));
					}
				}
				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
					if (dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null"));
					}
					dto.setNCrimpbreiteIsolation(null);
					dto.setNCrimphoeheIsolation(null);
				}
			} else {
				dto.setVerschleissteilIId(null);

				dto.setNCrimpbreitDraht(null);
				dto.setNCrimphoeheDraht(null);
				dto.setNCrimpbreiteIsolation(null);
				dto.setNCrimphoeheIsolation(null);
				dto.setNToleranzCrimpbreitDraht(null);
				dto.setNToleranzCrimphoeheDraht(null);
				dto.setNToleranzCrimpbreiteIsolation(null);
				dto.setNToleranzCrimphoeheIsolation(null);

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
					dto.setArtikelIIdKontakt(null);

					dto.setNWert(null);
				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {

					if (dto.getArtikelIIdKontakt() == null || dto.getArtikelIIdLitze() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getArtikelIIdKontakt() == null || dto.getArtikelIIdLitze() == null"));
					}
					dto.setNToleranzWert(null);

				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)
						|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)) {
					dto.setNToleranzWert(null);
					dto.setArtikelIIdLitze(null);
					dto.setNWert(null);
				}

			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		setPruefkombinationFromPruefkombinationDto(bean, dto);
		try {
			if (dto.getPruefkombinationsprDto() != null) {
				try {
					Pruefkombinationspr pruefkombinationspr = em.find(Pruefkombinationspr.class,
							new PruefkombinationsprPK(theClientDto.getLocUiAsString(), iId));
					if (pruefkombinationspr == null) {
						pruefkombinationspr = new Pruefkombinationspr(theClientDto.getLocUiAsString(),
								dto.getPruefkombinationsprDto().getCBez(), iId);
						em.persist(pruefkombinationspr);
						em.flush();
					}
					pruefkombinationspr.setCBez(dto.getPruefkombinationsprDto().getCBez());

				} catch (NoResultException ex) {
					Pruefkombinationspr pruefkombinationspr = new Pruefkombinationspr(theClientDto.getLocUiAsString(),
							dto.getPruefkombinationsprDto().getCBez(), iId);
					em.persist(pruefkombinationspr);

				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public PruefkombinationDto[] pruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze(Integer artikelIIdKontakt,
			Integer artikelIIdLitze, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("PruefkombinationFindByArtikelIIdKontaktArtikelIIdLitze");
		query.setParameter(1, artikelIIdKontakt);
		query.setParameter(2, artikelIIdLitze);
		Collection<?> clArten = query.getResultList();

		return PruefkombinationDtoAssembler.createDtos(clArten);
	}

	public PruefkombinationDto pruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId(
			Integer pruefartIId, Integer artikelIIdKontakt, Integer artikelIIdLitze, Integer verschleissteilIId,
			TheClientDto theClientDto) {

		Query query = em.createNamedQuery(
				"PruefkombinationFindByPruefartIIdArtikelIIdKontaktArtikelIIdLitzeVerschleissteilIId");
		query.setParameter(1, pruefartIId);
		query.setParameter(2, artikelIIdKontakt);
		query.setParameter(3, artikelIIdLitze);
		query.setParameter(4, verschleissteilIId);
		try {
			Pruefkombination pk = (Pruefkombination) query.getSingleResult();

			return PruefkombinationDtoAssembler.createDto(pk);
		} catch (NoResultException ex) {
			return null;
		}

	}

	public void updateStklpruefplan(StklpruefplanDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		Integer artikelIIdKontakt = null;
		if (dto.getStuecklistepositionIIdKontakt() != null) {
			artikelIIdKontakt = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdKontakt(),
					theClientDto).getArtikelIId();
		}

		Integer artikelIIdLitze = null;

		if (dto.getStuecklistepositionIIdLitze() != null) {
			artikelIIdLitze = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdLitze(), theClientDto)
					.getArtikelIId();
		}

		Integer artikelIIdLitze2 = null;

		if (dto.getStuecklistepositionIIdLitze2() != null) {
			artikelIIdLitze2 = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdLitze2(), theClientDto)
					.getArtikelIId();
		}

		pruefeObPruefplanInPruefkombinationVorhanden(dto.getStuecklisteId(), dto.getPruefartIId(), artikelIIdKontakt,
				artikelIIdLitze, artikelIIdLitze2, dto.getVerschleissteilIId(), dto.getPruefkombinationId(), true,
				theClientDto);

		Stklpruefplan bean = em.find(Stklpruefplan.class, iId);

		dto.setTAendern(new Timestamp(System.currentTimeMillis()));

		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		setStklpruefplanFromStklpruefplanDto(bean, dto);

	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Stuecklisteeigenschaftart stuecklisteeigenschaftart = em.find(Stuecklisteeigenschaftart.class, iId);
		if (stuecklisteeigenschaftart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(String cBez) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, cBez);
			Stuecklisteeigenschaftart stuecklisteeigenschaftart = (Stuecklisteeigenschaftart) query.getSingleResult();
			if (stuecklisteeigenschaftart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
	}

	private void setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(
			Stuecklisteeigenschaftart stuecklisteeigenschaftart,
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto) {
		stuecklisteeigenschaftart.setCBez(stuecklisteeigenschaftartDto.getCBez());
		stuecklisteeigenschaftart.setISort(stuecklisteeigenschaftartDto.getISort());
		em.merge(stuecklisteeigenschaftart);
		em.flush();
	}

	private StuecklisteeigenschaftartDto assembleStuecklisteeigenschaftartDto(
			Stuecklisteeigenschaftart stuecklisteeigenschaftart) {
		return StuecklisteeigenschaftartDtoAssembler.createDto(stuecklisteeigenschaftart);
	}

	private StuecklisteeigenschaftartDto[] assembleStuecklisteeigenschaftartDtos(
			Collection<?> stuecklisteeigenschaftarts) {
		List<StuecklisteeigenschaftartDto> list = new ArrayList<StuecklisteeigenschaftartDto>();
		if (stuecklisteeigenschaftarts != null) {
			Iterator<?> iterator = stuecklisteeigenschaftarts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteeigenschaftart stuecklisteeigenschaftart = (Stuecklisteeigenschaftart) iterator.next();
				list.add(assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart));
			}
		}
		StuecklisteeigenschaftartDto[] returnArray = new StuecklisteeigenschaftartDto[list.size()];
		return (StuecklisteeigenschaftartDto[]) list.toArray(returnArray);
	}

	public Integer createStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.dtoNotNull(stuecklisteDto, "stuecklisteDto");
		Validator.notNull(stuecklisteDto.getArtikelIId(), "stuecklisteDto.getArtikelIId()");
		Validator.notNull(stuecklisteDto.getBFremdfertigung(), "stuecklisteDto.getBFremdfertigung()");
		Validator.notNull(stuecklisteDto.getBAusgabeunterstueckliste(),
				"(stuecklisteDto.getBAusgabeunterstueckliste()");
		Validator.notNull(stuecklisteDto.getBMaterialbuchungbeiablieferung(),
				"stuecklisteDto.getBMaterialbuchungbeiablieferung()");
		Validator.notNull(stuecklisteDto.getStuecklisteartCNr(), "stuecklisteDto.getStuecklisteartCNr()");
		Validator.notNull(stuecklisteDto.getLagerIIdZiellager(), "stuecklisteDto.getLagerIIdZiellager()");
		Validator.notNull(stuecklisteDto.getFertigungsgruppeIId(), "stuecklisteDto.getFertigungsgruppeIId()");
		Validator.notNull(stuecklisteDto.getNErfassungsfaktor(), "stuecklisteDto.getNErfassungsfaktor()");

		// SP7932
		if (stuecklisteDto.getNErfassungsfaktor().doubleValue() <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN,
					new Exception("FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN"));
		}

		if (stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
			stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));
		}

		// PJ19179 Wenn ZENTRALER_ARTIKELSTAMM dann ein Artikel nur einmal
		// Konzernweit als Stueckliste definiert werden
		if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {

			Query query = em.createNamedQuery("StuecklistefindByArtikelIId");
			query.setParameter(1, stuecklisteDto.getArtikelIId());

			Collection c = query.getResultList();
			if (c.size() > 0) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STKL_STUECKLISTE.UC"));
			}

		} else {
			try {
				Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, stuecklisteDto.getArtikelIId());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste doppelt = (Stueckliste) query.getSingleResult();
				if (doppelt != null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("STKL_STUECKLISTE.UC"));
				}
			} catch (NoResultException ex1) {
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTE);
		stuecklisteDto.setIId(pk);

		stuecklisteDto.setMandantCNr(theClientDto.getMandant());

		stuecklisteDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendernarbeitsplan(theClientDto.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendernposition(theClientDto.getIDPersonal());

		stuecklisteDto.setTAnlegen(new java.sql.Timestamp(System.currentTimeMillis()));
		stuecklisteDto.setTAendernarbeitsplan(new java.sql.Timestamp(System.currentTimeMillis()));
		stuecklisteDto.setTAendernposition(new java.sql.Timestamp(System.currentTimeMillis()));
		stuecklisteDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));

		if (stuecklisteDto.getBDruckeinlagerstandsdetail() == null) {
			stuecklisteDto.setBDruckeinlagerstandsdetail(Helper.boolean2Short(false));
		}

		if (stuecklisteDto.getBMitFormeln() == null) {
			stuecklisteDto.setBMitFormeln(Helper.boolean2Short(false));
		}

		if (stuecklisteDto.getBJahreslos() == null) {
			stuecklisteDto.setBJahreslos(Helper.boolean2Short(false));
		}

		if (stuecklisteDto.getBHierarchischeChargennummern() == null) {
			stuecklisteDto.setBHierarchischeChargennummern(Helper.boolean2Short(false));
		}

		// Losgroesse defaultmaessig 1
		stuecklisteDto.setNLosgroesse(new java.math.BigDecimal(1));
		try {
			if (stuecklisteDto.getBUeberlieferbar() == null) {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
						ParameterFac.PARAMETER_DEFAULT_UEBERLIEFERBAR);

				stuecklisteDto.setBUeberlieferbar(Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

			}

			if (stuecklisteDto.getBKeineAutomatischeMaterialbuchung() == null) {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
						ParameterFac.PARAMETER_KEINE_AUTOMATISCHE_MATERIALBUCHUNG);

				stuecklisteDto.setBKeineAutomatischeMaterialbuchung(
						Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));
			}

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);
			boolean bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();

			if (bReihenfolgenplanung) {
				if (stuecklisteDto.getIReihenfolge() == null || stuecklisteDto.getIReihenfolge() < 0
						|| stuecklisteDto.getIReihenfolge() > 89) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE,
							new Exception("FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE"));
				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		try {
			Stueckliste stueckliste = new Stueckliste(stuecklisteDto.getIId(), stuecklisteDto.getArtikelIId(),
					stuecklisteDto.getMandantCNr(), stuecklisteDto.getBFremdfertigung(),
					stuecklisteDto.getNLosgroesse(), stuecklisteDto.getTAendernposition(),
					stuecklisteDto.getPersonalIIdAendernposition(), stuecklisteDto.getTAendernarbeitsplan(),
					stuecklisteDto.getPersonalIIdAendernarbeitsplan(), stuecklisteDto.getPersonalIIdAnlegen(),
					stuecklisteDto.getPersonalIIdAendern(), stuecklisteDto.getFertigungsgruppeIId(),
					stuecklisteDto.getStuecklisteartCNr(), stuecklisteDto.getBMaterialbuchungbeiablieferung(),
					stuecklisteDto.getBAusgabeunterstueckliste(), stuecklisteDto.getBUeberlieferbar(),
					stuecklisteDto.getNErfassungsfaktor(), stuecklisteDto.getLagerIIdZiellager(),
					stuecklisteDto.getBDruckeinlagerstandsdetail(),
					stuecklisteDto.getBKeineAutomatischeMaterialbuchung(), stuecklisteDto.getStuecklisteScriptartIId(),
					stuecklisteDto.getBMitFormeln(), stuecklisteDto.getBJahreslos(),
					stuecklisteDto.getBHierarchischeChargennummern());
			em.persist(stueckliste);
			em.flush();
			setStuecklisteFromStuecklisteDto(stueckliste, stuecklisteDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		ParametermandantDto parameter;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_ARBEITSPLAN_DEFAULT_STUECKLISTE);

			String artikelnummer = parameter.getCWert();

			if (artikelnummer.trim().length() > 0) {
				ArtikelDto[] artikelDto = getArtikelFac().artikelFindByCNrOhneExcAlleMandanten(artikelnummer);
				if (artikelDto != null) {
					for (int i = 0; i < artikelDto.length; i++) {

						if (theClientDto.getMandant().equals(artikelDto[i].getMandantCNr())) {

							StuecklisteDto stklDto = stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelDto[i].getIId(), theClientDto);

							if (artikelDto[i] != null) {
								StuecklistearbeitsplanDto[] arbDtos = stuecklistearbeitsplanFindByStuecklisteIId(
										stklDto.getIId(), theClientDto);

								for (int j = 0; j < arbDtos.length; j++) {
									StuecklistearbeitsplanDto stuecklistearbeitsplanDto = arbDtos[j];
									stuecklistearbeitsplanDto.setIId(null);
									stuecklistearbeitsplanDto.setStuecklisteIId(stuecklisteDto.getIId());
									createStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);
								}

							}

						}

					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		HvDtoLogger<StuecklisteDto> stuecklisteLogger = new HvDtoLogger<StuecklisteDto>(em, theClientDto);
		stuecklisteLogger.logInsert(stuecklisteDto);

		return stuecklisteDto.getIId();
	}

	public void removeStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getIId() == null"));
		}
		// try {
		Stueckliste toRemove = em.find(Stueckliste.class, stuecklisteDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklisteDto> stuecklisteLogger = new HvDtoLogger<StuecklisteDto>(em, theClientDto);
		stuecklisteLogger.logDelete(stuecklisteDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updateStuecklisteLosgroesse(Integer stuecklisteIId, BigDecimal nLosgroesse) {
		// Lossgroesse updaten
		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklisteIId);
		stueckliste.setNLosgroesse(nLosgroesse);
		em.merge(stueckliste);
		em.flush();
	}

	public void updateStueckliste(StuecklisteDto stuecklisteDto, TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null || stuecklisteDto.getArtikelIId() == null
				|| stuecklisteDto.getBFremdfertigung() == null || stuecklisteDto.getNLosgroesse() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklisteDto.getIId() == null || stuecklisteDto.getArtikelIId() == null || stuecklisteDto.getBFremdfertigung() == null || stuecklisteDto.getNLosgroesse() == null"));
		}
		if (stuecklisteDto.getBAusgabeunterstueckliste() == null
				|| stuecklisteDto.getBMaterialbuchungbeiablieferung() == null
				|| stuecklisteDto.getStuecklisteartCNr() == null || stuecklisteDto.getLagerIIdZiellager() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklisteDto.getBAusgabeunterstueckliste() == null || stuecklisteDto.getBMaterialbuchungbeiablieferung() == null || stuecklisteDto.getStuecklisteartCNr() == null || stuecklisteDto.getLagerIIdZiellager() == null"));
		}

		if (stuecklisteDto.getFertigungsgruppeIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getFertigungsgruppeIId() == null"));
		}
		if (stuecklisteDto.getNErfassungsfaktor() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getNErfassungsfaktor() == null"));
		}
		if (stuecklisteDto.getNLosgroesse().doubleValue() <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN,
					new Exception("stuecklisteDto.getNLosgroesse().doubleValue()<=0"));

		}

		// SP7932
		if (stuecklisteDto.getNErfassungsfaktor().doubleValue() <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN,
					new Exception("FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN"));
		}

		if (stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE)) {
			stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));
		}

		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_FERTIGUNG,
					ParameterFac.PARAMETER_REIHENFOLGENPLANUNG);
			boolean bReihenfolgenplanung = (Boolean) parameter.getCWertAsObject();

			if (bReihenfolgenplanung) {
				if (stuecklisteDto.getIReihenfolge() == null || stuecklisteDto.getIReihenfolge() < 0
						|| stuecklisteDto.getIReihenfolge() > 89) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE,
							new Exception("FEHLER_STUECKLISTE_UNGUELTIGE_REIHENFOLGE"));
				}
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		vergleicheStuecklisteDtoVorherNachherUndLoggeAenderungen(stuecklisteDto, theClientDto);

		Integer iId = stuecklisteDto.getIId();

		// try {
		Stueckliste stueckliste = em.find(Stueckliste.class, iId);
		if (stueckliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, stuecklisteDto.getArtikelIId());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Stueckliste) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STKL_STEUCKLISTE.UC"));
			}

		} catch (NoResultException ex) {
			//
		}
		stuecklisteDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteDto.setTAendern(new java.sql.Timestamp(System.currentTimeMillis()));
		setStuecklisteFromStuecklisteDto(stueckliste, stuecklisteDto);
	}

	private void vergleicheStuecklisteDtoVorherNachherUndLoggeAenderungen(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) {
		StuecklisteDto stuecklisteDto_vorher = stuecklisteFindByPrimaryKey(stuecklisteDto.getIId(), theClientDto);

		HvDtoLogger<StuecklisteDto> stuecklisteLogger = new HvDtoLogger<StuecklisteDto>(em, theClientDto);
		stuecklisteLogger.log(stuecklisteDto_vorher, stuecklisteDto);
	}

	private void vergleicheStuecklistepositionDtoVorherNachherUndLoggeAenderungen(
			StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto) {
		StuecklistepositionDto stuecklistepositionDto_vorher = stuecklistepositionFindByPrimaryKey(
				stuecklistepositionDto.getIId(), theClientDto);

		HvDtoLogger<StuecklistepositionDto> stuecklisteLogger = new HvDtoLogger<StuecklistepositionDto>(em,
				stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		stuecklisteLogger.log(stuecklistepositionDto_vorher, stuecklistepositionDto);
	}

	private void vergleicheStuecklistearbeitsplanDtoVorherNachherUndLoggeAenderungen(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto, TheClientDto theClientDto) {
		StuecklistearbeitsplanDto stuecklistearbeitsplanDto_vorher = stuecklistearbeitsplanFindByPrimaryKey(
				stuecklistearbeitsplanDto.getIId(), theClientDto);

		HvDtoLogger<StuecklistearbeitsplanDto> stuecklisteLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(em,
				stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		stuecklisteLogger.log(stuecklistearbeitsplanDto_vorher, stuecklistearbeitsplanDto);
	}

	/**
	 * Datet nur X_KOMMENTAR up
	 * 
	 * @param stuecklisteDto stuecklisteDto
	 * @param theClientDto   der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getIId() == null"));
		}
		// try {
		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklisteDto.getIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		stueckliste.setXKommentar(stuecklisteDto.getXKommentar());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		StuecklisteDto stuecklisteDto = stuecklisteFindByPrimaryKeyOhneExc(iId, theClientDto);

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return stuecklisteDto;
	}

	public StuecklisteDto stuecklisteFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto) {

		Stueckliste stueckliste = em.find(Stueckliste.class, iId);
		if (stueckliste == null) {
			return null;
		}
		StuecklisteDto stuecklisteDto = assembleStuecklisteDto(stueckliste);

		stuecklisteDto
				.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(stuecklisteDto.getArtikelIId(), theClientDto));
		if (stuecklisteDto.getAuftragIIdLeitauftrag() != null) {
			stuecklisteDto
					.setAuftragDto(getAuftragFac().auftragFindByPrimaryKey(stuecklisteDto.getAuftragIIdLeitauftrag()));
		}
		if (stuecklisteDto.getStuecklisteScriptartIId() != null) {
			stuecklisteDto
					.setScriptartDto(stuecklisteScriptartFindByPrimaryKey(stuecklisteDto.getStuecklisteScriptartIId()));
		}
		return stuecklisteDto;
	}

	public boolean pruefeObArtikelStuecklistenstrukturSchonVorhanden(Integer stuecklisteIId_Wurzel,
			Integer stuecklisteIId_ZuSuchende, TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteIId_Wurzel == null || stuecklisteIId_ZuSuchende == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteIId_Wurzel == null || stuecklisteIId_ZuSuchende == null"));
		}
		// Hole Alle Positionen der Wuzel
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId_Wurzel);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.add(Example.create(flrStuecklisteposition));

		List<?> results = crit.list();
		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		session.close();
		for (int i = 0; i < returnArray.length; i++) {

			try {

				Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stkl = (Stueckliste) query.getSingleResult();
				if (stkl.getIId().equals(stuecklisteIId_ZuSuchende)) {
					// Wenn Stueckliste gefinden wurde, dann true zurueckgeben
					return true;
				} else {
					// sonst in der naechsten Ebene weitersuchen
					return pruefeObArtikelStuecklistenstrukturSchonVorhanden(stkl.getIId(), stuecklisteIId_ZuSuchende,
							theClientDto);

				}
			} catch (NoResultException ex) {
				// Dann keine Stueckliste
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
		}
		return false;
	}

	public void sortiereNachArtikelnummer(Integer stuecklisteIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		StuecklistepositionDto[] dtos = assembleStuecklistepositionDtos(query.getResultList());

		for (int i = dtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				StuecklistepositionDto o = dtos[j];

				StuecklistepositionDto o1 = dtos[j + 1];

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
			Stuecklisteposition lspos = em.find(Stuecklisteposition.class, dtos[i].getIId());

			lspos.setISort(iSort);

			em.merge(lspos);
			em.flush();

			iSort++;
		}

	}

	public void arbeitsgaengeNeuNummerieren(Integer stuecklisteIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		StuecklistearbeitsplanDto[] dtos = assembleStuecklistearbeitsplanDtos(query.getResultList());

		TreeMap<Integer, ArrayList<StuecklistearbeitsplanDto>> tm = new TreeMap<Integer, ArrayList<StuecklistearbeitsplanDto>>();
		for (int i = 0; i < dtos.length; i++) {
			Integer iArbeitsgang = dtos[i].getIArbeitsgang();
			ArrayList<StuecklistearbeitsplanDto> list = null;

			if (tm.containsKey(iArbeitsgang)) {
				list = tm.get(iArbeitsgang);
			} else {
				list = new ArrayList<StuecklistearbeitsplanDto>();
			}

			list.add(dtos[i]);

			tm.put(iArbeitsgang, list);

		}

		Iterator it = tm.keySet().iterator();
		try {

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
			Integer iErhoehung = (Integer) parameter.getCWertAsObject();

			// Start ist immer bei 10
			Integer iAgAktuell = 10;

			while (it.hasNext()) {
				ArrayList<StuecklistearbeitsplanDto> list = tm.get(it.next());
				for (int i = 0; i < list.size(); i++) {
					StuecklistearbeitsplanDto apDto = list.get(i);

					Stuecklistearbeitsplan ap = em.find(Stuecklistearbeitsplan.class, apDto.getIId());

					ap.setIArbeitsgang(iAgAktuell);
				}

				iAgAktuell = iAgAktuell + iErhoehung;

			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer stueckliste,
			int iSortierungNeuePositionI) throws EJBExceptionLP {

		Collection<Stuecklisteposition> cl = listPositionenById(stueckliste);
		for (Stuecklisteposition oPosition : cl) {
			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
	}

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.add(Example.create(flrStuecklisteposition));

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);
		session.close();

		List<StuecklisteDto> l = new ArrayList<StuecklisteDto>();
		for (int i = 0; i < returnArray.length; i++) {

			try {
				Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stkl = (Stueckliste) query.getSingleResult();
				if (stkl != null) {
					// try {
					Stueckliste stueckliste = em.find(Stueckliste.class, stkl.getIId());
					if (stueckliste == null) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
					}
					StuecklisteDto stuecklisteDto = assembleStuecklisteDto(stueckliste);
					l.add(stuecklisteDto);
					// }
					// catch (FinderException ex1) {
					// throw new EJBExceptionLP(EJBExceptionLP.
					// FEHLER_BEI_FINDBYPRIMARYKEY,
					// ex1);
					// }

				}
			} catch (NoResultException ex) {
				// Dann keine Stueckliste
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

		}
		StuecklisteDto[] unterstuecklisten = new StuecklisteDto[l.size()];
		unterstuecklisten = (StuecklisteDto[]) l.toArray(unterstuecklisten);
		return unterstuecklisten;

	}

	public StuecklisteDto stuecklisteFindByArtikelIIdMandantCNrOhneExc(Integer artikelIId, String mandantCNr) {
		Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, mandantCNr);
		try {
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			return assembleStuecklisteDto(stueckliste);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public StuecklisteDto[] stuecklisteFindByArtikelIId(Integer artikelIId) {
		Query query = em.createNamedQuery("StuecklistefindByArtikelIId");
		query.setParameter(1, artikelIId);

		try {
			Collection s = query.getResultList();
			return assembleStuecklisteDtos(s);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIId(Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("artikelIId == null"));
		}

		try {
			Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, theClientDto.getMandant());
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			if (stueckliste == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleStuecklisteDto(stueckliste);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(Integer iIdArtikelI, TheClientDto theClientDto) {

		if (iIdArtikelI == null) {
			return null;
		}

		try {
			Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, iIdArtikelI);
			query.setParameter(2, theClientDto.getMandant());
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			if (stueckliste == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleStuecklisteDto(stueckliste);
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException ex1) {
			return null;
		}

	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(Integer iIdArtikelI, String mandantCNr) {

		if (iIdArtikelI == null) {
			return null;
		}

		try {
			Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, iIdArtikelI);
			query.setParameter(2, mandantCNr);
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			if (stueckliste == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleStuecklisteDto(stueckliste);
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException ex1) {
			return null;
		}

	}

	public StuecklisteDto stuecklistefindByCFremdsystemnrMandantCNr(String cFremdsystemnr, String mandantCNr) {

		try {
			Query query = em.createNamedQuery("StuecklistefindByCFremdsystemnrMandantCNr");
			query.setParameter(1, cFremdsystemnr);
			query.setParameter(2, mandantCNr);
			Collection c = query.getResultList();
			if (c.size() > 0) {
				return assembleStuecklisteDto((Stueckliste) c.iterator().next());
			} else {
				return null;
			}

		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException ex1) {
			return null;
		}

	}

	public Integer pruefeObPruefplanInPruefkombinationVorhanden(Integer stuecklisteIId, Integer pruefartIId,
			Integer artikelIIdKontakt, Integer artikelIIdLitze, Integer artikelIIdLitze2, Integer verschleissteilIId,
			Integer pruefkombinationIId, boolean throwException, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(pruefartIId, "pruefartIId");

		List<Integer> pruefartenOhnePruefkombi = getPruefartenOhnePruefkombination(theClientDto);
		if (pruefartenOhnePruefkombi.contains(pruefartIId)) {
			return null;
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT pk FROM FLRPruefkombination pk WHERE pk.flrpruefart.i_id=" + pruefartIId;

		PruefartDto pruefartDto = pruefartFindByPrimaryKey(pruefartIId, theClientDto);

		// Werkzeug und Litze muss nur bei Crimpen befuellt sein
		if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
			sQuery += " AND pk.artikel_i_id_kontakt=" + artikelIIdKontakt + " AND pk.artikel_i_id_litze="
					+ artikelIIdLitze + " AND pk.verschleissteil_i_id IS NULL";

		} else if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
				|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)
				|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {
			sQuery += " AND pk.artikel_i_id_kontakt=" + artikelIIdKontakt + " AND pk.artikel_i_id_litze="
					+ artikelIIdLitze + " AND pk.verschleissteil_i_id=" + verschleissteilIId;

			if (artikelIIdLitze2 != null) {
				sQuery += " AND pk.b_doppelanschlag=1 AND pk.artikel_i_id_litze2=" + artikelIIdLitze2;
			} else {
				sQuery += " AND pk.b_doppelanschlag=0";
			}

		} else if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {

			if (artikelIIdLitze != null) {

				sQuery += " AND pk.artikel_i_id_kontakt IS NULL AND pk.artikel_i_id_litze=" + artikelIIdLitze
						+ " AND pk.verschleissteil_i_id IS NULL ";
			} else if (pruefkombinationIId != null) {
				sQuery += " AND pk.i_id=" + pruefkombinationIId + " ";
			}
		} else if (StuecklisteFac.PRUEFART_FREIE_PRUEFUNG.equals(pruefartDto.getCNr())) {
			if (pruefkombinationIId != null) {
				sQuery += " AND pk.i_id=" + pruefkombinationIId + " ";
			}
		} else {
			if (artikelIIdKontakt != null) {
				sQuery += " AND pk.artikel_i_id_kontakt=" + artikelIIdKontakt
						+ " AND pk.artikel_i_id_litze IS NULL AND pk.verschleissteil_i_id IS NULL ";

			} else if (pruefkombinationIId != null) {
				sQuery += " AND pk.i_id=" + pruefkombinationIId + " ";
			}
		}

		sQuery += " ORDER BY pk.b_standard DESC";

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);
		List<?> results = query.list();

		if (results.size() == 0) {
			if (throwException == true) {

				ArrayList al = new ArrayList();

				al.add(pruefartDto.getBezeichnung());
				if (artikelIIdKontakt != null) {

					ArtikelDto aDtoKontakt = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIIdKontakt,
							theClientDto);
					al.add(aDtoKontakt.getCNr());
				} else {
					al.add(null);
				}

				if (artikelIIdLitze != null) {

					ArtikelDto aDtoLitze = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIIdLitze, theClientDto);
					al.add(aDtoLitze.getCNr());
				} else {
					al.add(null);
				}

				if (verschleissteilIId != null) {

					al.add(getArtikelFac().verschleissteilFindByPrimaryKey(verschleissteilIId).getBezeichnung());

				} else {
					al.add(null);
				}

				if (stuecklisteIId != null) {
					StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId,
							theClientDto);

					al.add(stklDto.getArtikelDto().getCNr());
					al.add(stklDto.getArtikelDto().getCBezAusSpr());
				} else {
					al.add(null);
					al.add(null);
				}

				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_KEINE_ENTSPRECHUNG_IN_PRUEFKOMBINATION, al,
						new Exception("FEHLER_KEINE_ENTSPRECHUNG_IN_PRUEFKOMBINATION"));
			} else {
				return null;
			}

		} else {
			FLRPruefkombination flr = (FLRPruefkombination) results.iterator().next();

			return flr.getI_id();
		}

	}

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNr(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("StuecklistefindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleStuecklisteDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNrOhneExc(Integer partnerIId, String mandantCNr,
			TheClientDto theClientDto) {
		// try {
		Query query = em.createNamedQuery("StuecklistefindByPartnerIIdMandantCNr");
		query.setParameter(1, partnerIId);
		query.setParameter(2, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// return null;
		// }
		return assembleStuecklisteDtos(cl);
		// }
		// catch (Throwable t) {
		// // nothing here, das braucht zuviel Zeit und bringt bei OhneExc
		// nichts
		// return null;
		// }
	}

	private void setStuecklisteFromStuecklisteDto(Stueckliste stueckliste, StuecklisteDto stuecklisteDto) {
		stueckliste.setArtikelIId(stuecklisteDto.getArtikelIId());
		stueckliste.setBFremdfertigung(stuecklisteDto.getBFremdfertigung());
		stueckliste.setBMaterialbuchungbeiablieferung(stuecklisteDto.getBMaterialbuchungbeiablieferung());
		stueckliste.setBAusgabeunterstueckliste(stuecklisteDto.getBAusgabeunterstueckliste());
		stueckliste.setAuftragIIdLeitauftrag(stuecklisteDto.getAuftragIIdLeitauftrag());
		stueckliste.setFertigungsgruppeIId(stuecklisteDto.getFertigungsgruppeIId());
		stueckliste.setNLosgroesse(stuecklisteDto.getNLosgroesse());
		stueckliste.setNDefaultdurchlaufzeit(stuecklisteDto.getNDefaultdurchlaufzeit());
		stueckliste.setPartnerIId(stuecklisteDto.getPartnerIId());
		stueckliste.setXKommentar(stuecklisteDto.getXKommentar());
		stueckliste.setTAendernarbeitsplan(stuecklisteDto.getTAendernarbeitsplan());
		stueckliste.setStuecklisteartCNr(stuecklisteDto.getStuecklisteartCNr());
		stueckliste.setNErfassungsfaktor(stuecklisteDto.getNErfassungsfaktor());
		stueckliste.setPersonalIIdAendernarbeitsplan(stuecklisteDto.getPersonalIIdAendernarbeitsplan());
		stueckliste.setTAendernposition(stuecklisteDto.getTAendernposition());
		stueckliste.setPersonalIIdAendernposition(stuecklisteDto.getPersonalIIdAendernposition());
		stueckliste.setTAendern(stuecklisteDto.getTAendern());
		stueckliste.setPersonalIIdAendern(stuecklisteDto.getPersonalIIdAendern());
		stueckliste.setLagerIIdZiellager(stuecklisteDto.getLagerIIdZiellager());
		stueckliste.setStuecklisteIIdEk(stuecklisteDto.getStuecklisteIIdEk());
		stueckliste.setBUeberlieferbar(stuecklisteDto.getBUeberlieferbar());
		stueckliste.setBDruckeinlagerstandsdetail(stuecklisteDto.getBDruckeinlagerstandsdetail());
		stueckliste.setBKeineAutomatischeMaterialbuchung(stuecklisteDto.getBKeineAutomatischeMaterialbuchung());
		stueckliste.setPersonalIIdFreigabe(stuecklisteDto.getPersonalIIdFreigabe());
		stueckliste.setTFreigabe(stuecklisteDto.getTFreigabe());
		stueckliste.setStuecklisteScriptartIId(stuecklisteDto.getStuecklisteScriptartIId());
		stueckliste.setCFremdsystemnr(stuecklisteDto.getCFremdsystemnr());
		stueckliste.setBMitFormeln(stuecklisteDto.getBMitFormeln());
		stueckliste.setStuecklisteIIdFormelstueckliste(stuecklisteDto.getStuecklisteIIdFormelstueckliste());
		stueckliste.setIReihenfolge(stuecklisteDto.getIReihenfolge());
		stueckliste.setBJahreslos(stuecklisteDto.getBJahreslos());
		stueckliste.setBHierarchischeChargennummern(stuecklisteDto.getBHierarchischeChargennummern());

		em.merge(stueckliste);
		em.flush();
	}

	private StuecklisteDto assembleStuecklisteDto(Stueckliste stueckliste) {
		return StuecklisteDtoAssembler.createDto(stueckliste);
	}

	private StuecklisteDto[] assembleStuecklisteDtos(Collection<?> stuecklistes) {
		List<StuecklisteDto> list = new ArrayList<StuecklisteDto>();
		if (stuecklistes != null) {
			Iterator<?> iterator = stuecklistes.iterator();
			while (iterator.hasNext()) {
				Stueckliste stueckliste = (Stueckliste) iterator.next();
				list.add(assembleStuecklisteDto(stueckliste));
			}
		}
		StuecklisteDto[] returnArray = new StuecklisteDto[list.size()];
		return (StuecklisteDto[]) list.toArray(returnArray);
	}

	public Integer createStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftDto == null"));
		}
		if (stuecklisteeigenschaftDto.getCBez() == null || stuecklisteeigenschaftDto.getStuecklisteIId() == null
				|| stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklisteeigenschaftDto.getCBez() == null || stuecklisteeigenschaftDto.getStuecklisteIId() == null || stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null"));
		}
		try {
			Query query = em.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIIdStuecklisteeigenschaftartIId");
			query.setParameter(1, stuecklisteeigenschaftDto.getStuecklisteIId());
			query.setParameter(2, stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId());
			Stuecklisteeigenschaft doppelt = (Stuecklisteeigenschaft) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_STUECKLISTEEIGENSCHAFT.UC"));
			}
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEEIGENSCHAFT);
		stuecklisteeigenschaftDto.setIId(pk);
		stuecklisteeigenschaftDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteeigenschaftDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		try {
			Stuecklisteeigenschaft stuecklisteeigenschaft = new Stuecklisteeigenschaft(
					stuecklisteeigenschaftDto.getIId(), stuecklisteeigenschaftDto.getStuecklisteIId(),
					stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId(), stuecklisteeigenschaftDto.getCBez(),
					stuecklisteeigenschaftDto.getPersonalIIdAendern(), stuecklisteeigenschaftDto.getTAendern());
			em.persist(stuecklisteeigenschaft);
			em.flush();
			setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(stuecklisteeigenschaft, stuecklisteeigenschaftDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return stuecklisteeigenschaftDto.getIId();

	}

	public void removeStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
			throws EJBExceptionLP {
		if (stuecklisteeigenschaftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftDto == null"));
		}
		if (stuecklisteeigenschaftDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteeigenschaftDto.getIId() == null"));
		}
		// try {
		Stuecklisteeigenschaft toRemove = em.find(Stuecklisteeigenschaft.class, stuecklisteeigenschaftDto.getIId());
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
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }

	}

	public void updateStuecklisteeigenschaft(StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftDto == null"));
		}
		if (stuecklisteeigenschaftDto.getIId() == null || stuecklisteeigenschaftDto.getCBez() == null
				|| stuecklisteeigenschaftDto.getStuecklisteIId() == null
				|| stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklisteeigenschaftDto.getIId() == null || stuecklisteeigenschaftDto.getCBez() == null || stuecklisteeigenschaftDto.getStuecklisteIId() == null || stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null"));
		}
		stuecklisteeigenschaftDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteeigenschaftDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		Integer iId = stuecklisteeigenschaftDto.getIId();
		// try {
		Stuecklisteeigenschaft stuecklisteeigenschaft = em.find(Stuecklisteeigenschaft.class, iId);
		if (stuecklisteeigenschaft == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(stuecklisteeigenschaft, stuecklisteeigenschaftDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Stuecklisteeigenschaft stuecklisteeigenschaft = em.find(Stuecklisteeigenschaft.class, iId);
		if (stuecklisteeigenschaft == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStuecklisteeigenschaftDto(stuecklisteeigenschaft);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByStuecklisteIIdStuecklisteeigenschaftartIId(
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIIdStuecklisteeigenschaftartIId");
			query.setParameter(1, stuecklisteIId);
			query.setParameter(2, stuecklisteeigenschaftartIId);
			Stuecklisteeigenschaft stuecklisteeigenschaft = (Stuecklisteeigenschaft) query.getSingleResult();
			if (stuecklisteeigenschaft == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleStuecklisteeigenschaftDto(stuecklisteeigenschaft);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
	}

	public StuecklisteeigenschaftDto[] stuecklisteeigenschaftFindByStuecklisteIId(Integer stuecklisteIId)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, null);
		// }
		StuecklisteeigenschaftDto[] dtos = assembleStuecklisteeigenschaftDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			dtos[i].setStuecklisteeigenschaftartDto(assembleStuecklisteeigenschaftartDto(
					em.find(Stuecklisteeigenschaftart.class, dtos[i].getStuecklisteeigenschaftartIId())));
		}

		return dtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, e);
		// }
	}

	private void setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(Stuecklisteeigenschaft stuecklisteeigenschaft,
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto) {
		stuecklisteeigenschaft.setStuecklisteIId(stuecklisteeigenschaftDto.getStuecklisteIId());
		stuecklisteeigenschaft
				.setStuecklisteeigenschaftartIId(stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId());
		stuecklisteeigenschaft.setCBez(stuecklisteeigenschaftDto.getCBez());
		stuecklisteeigenschaft.setPersonalIIdAendern(stuecklisteeigenschaftDto.getPersonalIIdAendern());
		stuecklisteeigenschaft.setTAendern(stuecklisteeigenschaftDto.getTAendern());
		em.merge(stuecklisteeigenschaft);
		em.flush();
	}

	private StuecklisteeigenschaftDto assembleStuecklisteeigenschaftDto(Stuecklisteeigenschaft stuecklisteeigenschaft) {
		return StuecklisteeigenschaftDtoAssembler.createDto(stuecklisteeigenschaft);
	}

	private StuecklisteeigenschaftDto[] assembleStuecklisteeigenschaftDtos(Collection<?> stuecklisteeigenschafts) {
		List<StuecklisteeigenschaftDto> list = new ArrayList<StuecklisteeigenschaftDto>();
		if (stuecklisteeigenschafts != null) {
			Iterator<?> iterator = stuecklisteeigenschafts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteeigenschaft stuecklisteeigenschaft = (Stuecklisteeigenschaft) iterator.next();
				list.add(assembleStuecklisteeigenschaftDto(stuecklisteeigenschaft));
			}
		}
		StuecklisteeigenschaftDto[] returnArray = new StuecklisteeigenschaftDto[list.size()];
		return (StuecklisteeigenschaftDto[]) list.toArray(returnArray);
	}

	public Integer createStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getStuecklisteIId() == null || stuecklistearbeitsplanDto.getArtikelIId() == null
				|| stuecklistearbeitsplanDto.getLRuestzeit() == null
				|| stuecklistearbeitsplanDto.getIArbeitsgang() == null
				|| stuecklistearbeitsplanDto.getLStueckzeit() == null
				|| stuecklistearbeitsplanDto.getBNurmaschinenzeit() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklistearbeitsplanDto.getStuecklisteIId() == null || stuecklistearbeitsplanDto.getArtikelIId() == null || stuecklistearbeitsplanDto.getTRuestzeit() == null || stuecklistearbeitsplanDto.getIArbeitsgang() == null || stuecklistearbeitsplanDto.getTStueckzeit() == null || stuecklistearbeitsplanDto.getBNurmaschinenzeit()"));
		}

		Stueckliste stkl = em.find(Stueckliste.class, stuecklistearbeitsplanDto.getStuecklisteIId());

		if (!Helper.short2boolean(stkl.getBMitFormeln())) {
			stuecklistearbeitsplanDto.setXFormel(null);
		}

		Validator.entityFound(stkl, stuecklistearbeitsplanDto.getStuecklisteIId());
		verifyStuecklistearbeitsplanFormel(stkl, stuecklistearbeitsplanDto, theClientDto);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEARBEITSPLAN);
		stuecklistearbeitsplanDto.setIId(pk);

		
		if (stuecklistearbeitsplanDto.getBInitial() == null) {
			stuecklistearbeitsplanDto.setBInitial(Helper.boolean2Short(false));
		}
		
		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(stuecklistearbeitsplanDto.getArtikelIId(),
				theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
			// PJ 16396
			if (stuecklistearbeitsplanDto.getAgartCNr() != null) {
				Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
				query.setParameter(1, stuecklistearbeitsplanDto.getStuecklisteIId());
				query.setParameter(2, stuecklistearbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklistearbeitsplan ap = (Stuecklistearbeitsplan) it.next();
					if (ap.getAgartCNr() == null) {
						stuecklistearbeitsplanDto.setMaschineIId(ap.getMaschineIId());
						break;
					}
				}
			}
		}

		try {
			Stuecklistearbeitsplan stuecklistearbeitsplan = new Stuecklistearbeitsplan(
					stuecklistearbeitsplanDto.getIId(), stuecklistearbeitsplanDto.getStuecklisteIId(),
					stuecklistearbeitsplanDto.getIArbeitsgang(), stuecklistearbeitsplanDto.getArtikelIId(),
					stuecklistearbeitsplanDto.getLStueckzeit(), stuecklistearbeitsplanDto.getLRuestzeit(),
					stuecklistearbeitsplanDto.getBNurmaschinenzeit(), stuecklistearbeitsplanDto.getBInitial());
			em.persist(stuecklistearbeitsplan);
			em.flush();
			setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(stuecklistearbeitsplan, stuecklistearbeitsplanDto);

			Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistearbeitsplanDto.getStuecklisteIId());
			if (stueckliste == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto.getIDPersonal());
			stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System.currentTimeMillis()));

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		HvDtoLogger<StuecklistearbeitsplanDto> hvLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(em,
				stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		hvLogger.logInsert(stuecklistearbeitsplanDto);

		return stuecklistearbeitsplanDto.getIId();
	}

	public void removeStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistearbeitsplanDto.getIId() == null"));
		}

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistearbeitsplanDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklistearbeitsplan toRemove = em.find(Stuecklistearbeitsplan.class, stuecklistearbeitsplanDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklistearbeitsplanDto> hvLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(em,
				stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		hvLogger.logDelete(stuecklistearbeitsplanDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto.getIDPersonal());
		stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System.currentTimeMillis()));

		// }
		// catch (RemoveException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public void removeAlleStuecklistearbeitsplan(Integer stuecklisteId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull("stuecklisteId", "stuecklisteId");
		StuecklistearbeitsplanDto[] arbeitsplanDtos = stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteId,
				theClientDto);

		for (StuecklistearbeitsplanDto stuecklistearbeitsplanDto : arbeitsplanDtos) {
			removeStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);
		}
	}

	public void updateStuecklistearbeitsplan(StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getIId() == null || stuecklistearbeitsplanDto.getStuecklisteIId() == null
				|| stuecklistearbeitsplanDto.getIArbeitsgang() == null
				|| stuecklistearbeitsplanDto.getArtikelIId() == null
				|| stuecklistearbeitsplanDto.getLRuestzeit() == null
				|| stuecklistearbeitsplanDto.getLStueckzeit() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklistearbeitsplanDto.getIId() == null || stuecklistearbeitsplanDto.getStuecklisteIId() == null || stuecklistearbeitsplanDto.getIArbeitsgang() == null || stuecklistearbeitsplanDto.getArtikelIId() == null || stuecklistearbeitsplanDto.getTRuestzeit() == null || stuecklistearbeitsplanDto.getTStueckzeit() == null"));
		}

		Integer iId = stuecklistearbeitsplanDto.getIId();

		Stuecklistearbeitsplan stuecklistearbeitsplan = em.find(Stuecklistearbeitsplan.class, iId);
		Validator.entityFound(stuecklistearbeitsplan, iId);

		Stueckliste stkl = em.find(Stueckliste.class, stuecklistearbeitsplanDto.getStuecklisteIId());
		Validator.entityFound(stkl, stuecklistearbeitsplanDto.getStuecklisteIId());
		verifyStuecklistearbeitsplanFormel(stkl, stuecklistearbeitsplanDto, theClientDto);

		vergleicheStuecklistearbeitsplanDtoVorherNachherUndLoggeAenderungen(stuecklistearbeitsplanDto, theClientDto);

		if (stuecklistearbeitsplanDto.getIMaschinenversatztage() != null) {

			int iVorher = 0;

			if (stuecklistearbeitsplan.getIMaschinenversatztage() != null) {
				iVorher = stuecklistearbeitsplan.getIMaschinenversatztage();
			}

			if (iVorher != stuecklistearbeitsplanDto.getIMaschinenversatztage()) {

				int delta = stuecklistearbeitsplanDto.getIMaschinenversatztage() - iVorher;

				if (delta != 0) {
					StuecklistearbeitsplanDto[] saDtos = stuecklistearbeitsplanFindByStuecklisteIId(
							stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);

					for (int i = 0; i < saDtos.length; i++) {
						if (stuecklistearbeitsplanDto.getIId().equals(saDtos[i].getIId())) {

							for (int j = i + 1; j < saDtos.length; j++) {
								Stuecklistearbeitsplan stuecklistearbeitsplanTemp = em
										.find(Stuecklistearbeitsplan.class, saDtos[j].getIId());

								if (stuecklistearbeitsplanTemp.getIMaschinenversatztage() != null) {
									stuecklistearbeitsplanTemp.setIMaschinenversatztage(
											stuecklistearbeitsplanTemp.getIMaschinenversatztage() + delta);

								} else {
									stuecklistearbeitsplanTemp.setIMaschinenversatztage(delta);
								}
								em.merge(stuecklistearbeitsplanTemp);
								em.flush();
							}
							break;
						}
					}
				}
			}

		}

		setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(stuecklistearbeitsplan, stuecklistearbeitsplanDto);

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistearbeitsplanDto.getStuecklisteIId());

		stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto.getIDPersonal());
		stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System.currentTimeMillis()));

		em.merge(stueckliste);
		em.flush();

		// PJ 16396
		if (stuecklistearbeitsplanDto.getMaschineIId() != null && stuecklistearbeitsplanDto.getAgartCNr() == null
				&& stuecklistearbeitsplanDto.getIArbeitsgang() != 0) {

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(stuecklistearbeitsplanDto.getArtikelIId(), theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
				query.setParameter(1, stueckliste.getIId());
				query.setParameter(2, stuecklistearbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklistearbeitsplan ap = (Stuecklistearbeitsplan) it.next();
					if (!ap.getIId().equals(stuecklistearbeitsplanDto.getIId())) {
						ArtikelDto artikelDtoPos = getArtikelFac().artikelFindByPrimaryKeySmall(ap.getArtikelIId(),
								theClientDto);
						if (Helper.short2boolean(artikelDtoPos.getbReinemannzeit()) == false) {
							ap.setMaschineIId(stuecklistearbeitsplanDto.getMaschineIId());
						}
					}
				}
			}
		}

	}

	public void wechsleMandantEinerSteckliste(Integer stklIId, String mandantCNrNeu, TheClientDto theClientDto) {

		boolean b = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, mandantCNrNeu);

		if (b == true) {

			boolean bRecht = getBenutzerServicesFac().hatRechtInZielmandant(RechteFac.RECHT_STK_STUECKLISTE_CUD,
					mandantCNrNeu, theClientDto);

			if (bRecht == true) {

				if (!theClientDto.getMandant().equals(mandantCNrNeu)) {

					// Maschinen aus AP loeschen
					StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac()
							.stuecklistearbeitsplanFindByStuecklisteIId(stklIId, theClientDto);
					for (int i = 0; i < apDtos.length; i++) {
						apDtos[i].setMaschineIId(null);
						updateStuecklistearbeitsplan(apDtos[i], theClientDto);
					}

					// Montageart mit der gleichen Bezeichnung im anderen
					// Mandanten suchen
					StuecklistepositionDto[] posDtos = getStuecklisteFac()
							.stuecklistepositionFindByStuecklisteIId(stklIId, theClientDto);
					for (int i = 0; i < posDtos.length; i++) {

						MontageartDto mDtoVorhanden = montageartFindByPrimaryKey(posDtos[i].getMontageartIId(),
								theClientDto);

						MontageartDto mDtoNeu = montageartFindByMandantCNrCBezOhneExc(mandantCNrNeu,
								mDtoVorhanden.getCBez());

						Integer montageartIIdNeu = null;
						if (mDtoNeu != null) {
							montageartIIdNeu = mDtoNeu.getIId();
						} else {
							// Default Montageart holen

							Query query = em.createNamedQuery("MontageartfindByMandantCNr");
							query.setParameter(1, mandantCNrNeu);

							Collection<?> cl = query.getResultList();
							Montageart m = (Montageart) cl.iterator().next();
							montageartIIdNeu = m.getIId();

						}

						Stuecklisteposition stuecklistepos = em.find(Stuecklisteposition.class, posDtos[i].getIId());

						stuecklistepos.setMontageartIId(montageartIIdNeu);
						em.merge(stuecklistepos);
						em.flush();

					}

					// Abbuchungslaeger loeschen
					StklagerentnahmeDto[] abbuchunglaegerDtos = getStuecklisteFac()
							.stklagerentnahmeFindByStuecklisteIId(stklIId);
					for (int i = 0; i < abbuchunglaegerDtos.length; i++) {
						getStuecklisteFac().removeStklagerentnahme(abbuchunglaegerDtos[i], theClientDto);
					}

					Stueckliste stueckliste = em.find(Stueckliste.class, stklIId);
					stueckliste.setMandantCNr(mandantCNrNeu);

					// Default Fertigungsgruppe der anderen Mandanten verwenden
					stueckliste.setFertigungsgruppeIId(
							fertigungsgruppeFindByMandantCNr(mandantCNrNeu, theClientDto)[0].getIId());

					// Kunde loeschen
					stueckliste.setPartnerIId(null);
					// Scriptart loeschen
					stueckliste.setStuecklisteScriptartIId(null);

					// Ziel-Lager auf Hauptlager des neuen Mandanten setzen
					stueckliste.setLagerIIdZiellager(getLagerFac().getHauptlagerEinesMandanten(mandantCNrNeu).getIId());

					em.merge(stueckliste);
					em.flush();

				}

			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_STKL_MANDANTENWECHSEL_KEINE_BERECHTIGUNG,
						new Exception("FEHLER_STKL_MANDANTENWECHSEL_KEINE_BERECHTIGUNG"));

			}

		}

	}

	public Object[] kopiereStueckliste(Integer stuecklisteIId, String artikelnummerNeu, java.util.HashMap zuKopieren,
			Integer herstellerIIdNeu, Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		StuecklisteDto stklDto = stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		Object[] o = getArtikelFac().kopiereArtikel(stklDto.getArtikelIId(), artikelnummerNeu, zuKopieren,
				herstellerIIdNeu, stuecklistepositionIId, theClientDto);
		stklDto.setIId(null);
		stklDto.setArtikelDto(null);
		stklDto.setArtikelIId((Integer) o[0]);

		Integer stuecklisteIIdNeu = createStueckliste(stklDto, theClientDto);
		o[0] = stuecklisteIIdNeu;

		StklparameterDto[] stklparameterDtos = stklparameterFindByStuecklisteIId(stuecklisteIId, theClientDto);
		for (int i = 0; i < stklparameterDtos.length; i++) {
			stklparameterDtos[i].setIId(null);
			stklparameterDtos[i].setStuecklisteIId(stuecklisteIIdNeu);
			createStklparameter(stklparameterDtos[i], theClientDto);
		}

		HashMap<Integer, Integer> hmPosIdAltNeu = new HashMap<Integer, Integer>();
		StuecklistepositionDto[] posDtos = stuecklistepositionFindByStuecklisteIId(stuecklisteIId, theClientDto);
		for (int i = 0; i < posDtos.length; i++) {
			StuecklistepositionDto posDto = posDtos[i];
			Integer posIIdAlt = posDto.getIId();

			posDto.setIId(null);
			posDto.setStuecklisteIId(stuecklisteIIdNeu);

			ArtikelDto aDtoPosition = getArtikelFac().artikelFindByPrimaryKey(posDto.getArtikelIId(), theClientDto);

			if (aDtoPosition.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				posDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
				posDto.setSHandeingabe(aDtoPosition.formatBezeichnung());
			} else {
				posDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
			}

			Integer posIIdNeu = createStuecklisteposition(posDto, theClientDto);

			hmPosIdAltNeu.put(posIIdAlt, posIIdNeu);

			PosersatzDto[] posersatzDtos = posersatzFindByStuecklistepositionIId(posIIdAlt);
			for (int j = 0; j < posersatzDtos.length; j++) {
				posersatzDtos[j].setIId(null);
				posersatzDtos[j].setStuecklistepositionIId(posIIdNeu);
				createPosersatz(posersatzDtos[j], theClientDto);
			}

		}

		StuecklistearbeitsplanDto[] apDtos = stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId, theClientDto);

		for (int i = 0; i < apDtos.length; i++) {
			apDtos[i].setIId(null);

			apDtos[i].setStuecklisteIId(stuecklisteIIdNeu);

			if (apDtos[i].getStuecklistepositionIId() != null) {
				if (hmPosIdAltNeu.containsKey(apDtos[i].getStuecklistepositionIId())) {
					apDtos[i].setStuecklistepositionIId(hmPosIdAltNeu.get(apDtos[i].getStuecklistepositionIId()));
				} else {
					apDtos[i].setStuecklistepositionIId(null);
				}
			}

			createStuecklistearbeitsplan(apDtos[i], theClientDto);

		}

		StklagerentnahmeDto[] lagerDtos = stklagerentnahmeFindByStuecklisteIId(stuecklisteIId);

		for (int i = 0; i < lagerDtos.length; i++) {
			lagerDtos[i].setIId(null);
			lagerDtos[i].setStuecklisteIId(stuecklisteIIdNeu);
			createStklagerentnahme(lagerDtos[i], theClientDto);
		}

		StklpruefplanDto[] pruefplanDtos = stklpruefplanFindByStuecklisteIId(stuecklisteIId);

		for (int i = 0; i < pruefplanDtos.length; i++) {
			pruefplanDtos[i].setIId(null);
			pruefplanDtos[i].setStuecklisteId(stuecklisteIIdNeu);

			if (pruefplanDtos[i].getStuecklistepositionIIdKontakt() != null) {
				if (hmPosIdAltNeu.containsKey(pruefplanDtos[i].getStuecklistepositionIIdKontakt())) {
					pruefplanDtos[i].setStuecklistepositionIIdKontakt(
							hmPosIdAltNeu.get(pruefplanDtos[i].getStuecklistepositionIIdKontakt()));
				} else {
					pruefplanDtos[i].setStuecklistepositionIIdKontakt(null);
				}
			}

			if (pruefplanDtos[i].getStuecklistepositionIIdLitze() != null) {
				if (hmPosIdAltNeu.containsKey(pruefplanDtos[i].getStuecklistepositionIIdLitze())) {
					pruefplanDtos[i].setStuecklistepositionIIdLitze(
							hmPosIdAltNeu.get(pruefplanDtos[i].getStuecklistepositionIIdLitze()));
				} else {
					pruefplanDtos[i].setStuecklistepositionIIdLitze(null);
				}
			}

			if (pruefplanDtos[i].getStuecklistepositionIIdLitze2() != null) {
				if (hmPosIdAltNeu.containsKey(pruefplanDtos[i].getStuecklistepositionIIdLitze2())) {
					pruefplanDtos[i].setStuecklistepositionIIdLitze2(
							hmPosIdAltNeu.get(pruefplanDtos[i].getStuecklistepositionIIdLitze2()));
				} else {
					pruefplanDtos[i].setStuecklistepositionIIdLitze2(null);
				}
			}

			createStklpruefplan(pruefplanDtos[i], theClientDto);
		}

		StuecklisteeigenschaftDto[] eingenschaftenDtos = stuecklisteeigenschaftFindByStuecklisteIId(stuecklisteIId);
		for (int i = 0; i < eingenschaftenDtos.length; i++) {
			eingenschaftenDtos[i].setIId(null);
			eingenschaftenDtos[i].setStuecklisteIId(stuecklisteIIdNeu);
			createStuecklisteeigenschaft(eingenschaftenDtos[i], theClientDto);
		}

		return o;

	}

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId, TheClientDto theClientDto) {

		try {
			AgstklpositionDto[] dtos = getAngebotstklpositionFac().agstklpositionFindByAgstklIId(agstklIId,
					theClientDto);

			MontageartDto[] monatageartdtos = null;

			monatageartdtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);
				if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)
						|| aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_ARTIKEL)) {
					StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
					stuecklistepositionDto.setStuecklisteIId(stuecklisteIId);

					stuecklistepositionDto.setNMenge(dto.getNMenge());
					stuecklistepositionDto.setEinheitCNr(aDto.getEinheitCNr());
					stuecklistepositionDto.setBMitdrucken(dto.getBDrucken());
					stuecklistepositionDto.setBRuestmenge(dto.getBRuestmenge());
					stuecklistepositionDto.setBInitial(dto.getBInitial());
					stuecklistepositionDto.setCPosition(dto.getCPosition());
					if (aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
						if (aDto.getArtikelsprDto() != null) {
							stuecklistepositionDto.setSHandeingabe(aDto.getArtikelsprDto().getCBez());
						}
					} else {
						stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stuecklistepositionDto.setArtikelIId(aDto.getIId());
					}

					if (monatageartdtos != null && monatageartdtos.length > 0) {
						stuecklistepositionDto.setMontageartIId(monatageartdtos[0].getIId());
					}

					createStuecklisteposition(stuecklistepositionDto, theClientDto);
				} else {
					StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
					stuecklistearbeitsplanDto.setArtikelIId(aDto.getIId());
					stuecklistearbeitsplanDto.setStuecklisteIId(stuecklisteIId);
					stuecklistearbeitsplanDto.setLRuestzeit(0L);
					stuecklistearbeitsplanDto
							.setLStueckzeit(dto.getNMenge().multiply(new BigDecimal(60 * 60 * 1000)).longValue());
					stuecklistearbeitsplanDto.setBNurmaschinenzeit(Helper.boolean2Short(false));
					stuecklistearbeitsplanDto.setIArbeitsgang(getNextArbeitsgang(stuecklisteIId, theClientDto));

					createStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);
				}

			}

			ArrayList<AgstklmaterialDto> alMaterial = getAngebotstklpositionFac()
					.agstklmaterialFindByAgstklIId(agstklIId, theClientDto);

			for (AgstklmaterialDto materialDto : alMaterial) {

				StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
				stuecklistepositionDto.setStuecklisteIId(stuecklisteIId);

				stuecklistepositionDto.setNMenge(BigDecimal.ONE);
				stuecklistepositionDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
				stuecklistepositionDto.setBMitdrucken(Helper.boolean2Short(true));

				stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
				
				
				
				String cBez=materialDto.getCBez();
				
				if(materialDto.getNDimension1()!=null) {
					cBez+=" "+Helper.formatZahl(materialDto.getNDimension1(), 2, theClientDto.getLocUi());
				}
				if(materialDto.getNDimension2()!=null) {
					cBez+=" x "+Helper.formatZahl(materialDto.getNDimension2(), 2, theClientDto.getLocUi());
				}
				if(materialDto.getNDimension3()!=null) {
					cBez+= " x "+Helper.formatZahl(materialDto.getNDimension3(), 2, theClientDto.getLocUi());
				}
				
				if(materialDto.getNGewicht()!=null) {
					cBez+= " "+Helper.formatZahl(materialDto.getNGewicht(), 2, theClientDto.getLocUi())+"kg";
				}
				
				stuecklistepositionDto.setSHandeingabe(cBez);
				

				if (monatageartdtos != null && monatageartdtos.length > 0) {
					stuecklistepositionDto.setMontageartIId(monatageartdtos[0].getIId());
				}

				createStuecklisteposition(stuecklistepositionDto, theClientDto);

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null"));
		}

		// try {
		Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId_Quelle);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		StuecklistepositionDto[] dtos = assembleStuecklistepositionDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			StuecklistepositionDto dto = dtos[i];
			dto.setStuecklisteIId(stuecklisteIId_Ziel);

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);
			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				dto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);

				// SP1944
				if (artikelDto.getArtikelsprDto() != null) {
					dto.setSHandeingabe(artikelDto.getArtikelsprDto().getCBez());
				}

			} else {
				dto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
			}

			dto.setISort(null); // Damit automatisch drangehaengt wird
			createStuecklisteposition(dto, theClientDto);
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);

		// }
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void artikelErsetzen(Integer artikelIIdVon, Integer artikelIIdDurch, TheClientDto theClientDto) {

		// Zuerst arbeitsplan

		StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac().stuecklistearbeitsplanFindByArtikelIId(artikelIIdVon,
				theClientDto);
		for (int i = 0; i < apDtos.length; i++) {
			try {
				StuecklistearbeitsplanDto apDto = getStuecklisteFac()
						.stuecklistearbeitsplanFindByPrimaryKey(apDtos[i].getIId(), theClientDto);
				apDto.setArtikelIId(artikelIIdDurch);
				getStuecklisteFac().updateStuecklistearbeitsplan(apDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}
		// Dann Material

		StuecklistepositionDto[] posDtos = getStuecklisteFac().stuecklistepositionFindByArtikelIId(artikelIIdVon,
				theClientDto);
		for (int i = 0; i < posDtos.length; i++) {

			try {
				StuecklistepositionDto posDto = getStuecklisteFac()
						.stuecklistepositionFindByPrimaryKey(posDtos[i].getIId(), theClientDto);
				posDto.setArtikelIId(artikelIIdDurch);
				getStuecklisteFac().updateStuecklisteposition(posDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void bevorzugtenArtikelEintragen(Integer artikelIId_Bevorzugt, TheClientDto theClientDto) {
		try {

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId_Bevorzugt, theClientDto);

			if (Helper.short2boolean(aDto.getBBevorzugt())) {
				ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
				int iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

				String artikelnummerOhneHst = aDto.getCNr().substring(0, iLaengeArtikelnummer);

				Session session = FLRSessionFactory.getFactory().openSession();

				String queryString = "SELECT pos FROM FLRStuecklisteposition AS pos WHERE pos.flrstueckliste.mandant_c_nr='"
						+ theClientDto.getMandant() + "' AND pos.flrartikel.c_nr LIKE '" + artikelnummerOhneHst
						+ "%' AND pos.flrartikel.c_nr<>'" + aDto.getCNr() + "'";

				org.hibernate.Query query = session.createQuery(queryString);
				List<?> resultList = query.list();
				Iterator<?> resultListIterator = resultList.iterator();
				while (resultListIterator.hasNext()) {
					FLRStuecklisteposition pos = (FLRStuecklisteposition) resultListIterator.next();

					StuecklistepositionDto stklposDto = getStuecklisteFac()
							.stuecklistepositionFindByPrimaryKey(pos.getI_id(), theClientDto);
					stklposDto.setArtikelIId(artikelIId_Bevorzugt);

					getStuecklisteFac().updateStuecklisteposition(stklposDto, theClientDto);

				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Material

		/*
		 * StuecklistepositionDto[] posDtos =
		 * getStuecklisteFac().stuecklistepositionFindByArtikelIId(artikelIIdVon,
		 * theClientDto); for (int i = 0; i < posDtos.length; i++) {
		 * 
		 * try { StuecklistepositionDto posDto = getStuecklisteFac()
		 * .stuecklistepositionFindByPrimaryKey(posDtos[i].getIId(), theClientDto);
		 * posDto.setArtikelIId(artikelIIdDurch);
		 * getStuecklisteFac().updateStuecklisteposition(posDto, theClientDto); } catch
		 * (RemoteException e) { throwEJBExceptionLPRespectOld(e); }
		 * 
		 * }
		 */

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void maschineErsetzen(Integer maschineIIdVon, Integer maschineIIdDurch, TheClientDto theClientDto) {
		StuecklistearbeitsplanDto[] apDtos = getStuecklisteFac().stuecklistearbeitsplanFindByMaschineIId(maschineIIdVon,
				theClientDto);
		for (int i = 0; i < apDtos.length; i++) {
			try {
				StuecklistearbeitsplanDto apDto = getStuecklisteFac()
						.stuecklistearbeitsplanFindByPrimaryKey(apDtos[i].getIId(), theClientDto);
				apDto.setMaschineIId(maschineIIdDurch);
				getStuecklisteFac().updateStuecklistearbeitsplan(apDto, theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

	}

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> struktur,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach, Integer stuecklisteIId, TheClientDto theClientDto) {

		ArrayList<ArtikelDto> alNichtimportiert = importiereStuecklistenstrukturSiemensNX(struktur, stuecklisteIId,
				theClientDto, null, false);

		if (listeFlach != null && listeFlach.size() > 0) {
			// Vom EK-Vorschlag abziehen
			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(listeFlach.get(0).getArtikelIId(), theClientDto);

			if (stklDto != null && stklDto.getStuecklisteIIdEk() != null) {

				Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
				query.setParameter(1, stklDto.getStuecklisteIIdEk());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklisteposition pos = (Stuecklisteposition) it.next();

					if (pos.getArtikelIId() != null) {
						BigDecimal posmenge = pos.getNMenge();
						for (int j = 0; j < listeFlach.size(); j++) {
							Integer artikelIId = listeFlach.get(j).getArtikelIId();

							if (pos.getArtikelIId().equals(artikelIId)) {
								posmenge = posmenge.subtract(listeFlach.get(j).getMenge());
							}

						}

						if (posmenge.doubleValue() <= 0) {
							em.remove(pos);
						} else {
							pos.setNMenge(posmenge);
							em.merge(pos);
							em.flush();
						}

					}

				}

			}
		}

		return alNichtimportiert;
	}

	private ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> struktur, Integer stuecklisteIId, TheClientDto theClientDto,
			ArrayList<ArtikelDto> alNichtimportiert, boolean bNichtmehrImportieren) {

		String defaultEinheit = null;
		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);

			defaultEinheit = parameter.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		for (int i = 0; i < struktur.size(); i++) {

			boolean bStuecklisteHatBereitsPositionen = false;
			StrukturierterImportSiemensNXDto stkl = struktur.get(i);

			Integer artikelIId = stkl.getArtikelIId();

			if (stkl.getPositionen() != null && stkl.getPositionen().size() > 0) {

				Integer stklIId = null;
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);

				if (stklDto != null) {
					stklIId = stklDto.getIId();
					if (alNichtimportiert == null) {
						alNichtimportiert = new ArrayList<ArtikelDto>();
					}

					Query query = em
							.createNamedQuery("StuecklistepositionfindByStuecklisteIIdArtikelIIdOrderByArtikelIId");
					query.setParameter(1, stklDto.getIId());
					Collection<?> cl = query.getResultList();
					StuecklistepositionDto[] stklPosSortierttnachArtikelId = assembleStuecklistepositionDtos(cl);

					ArrayList<StrukturierterImportSiemensNXDto> posNX = stkl.getPositionenSortiertNachArtikelIId();

					// Vergleichen, ob sich die Artikel veraendert haben

					if (stklPosSortierttnachArtikelId.length > 0) {
						bStuecklisteHatBereitsPositionen = true;
						if (stklPosSortierttnachArtikelId.length == posNX.size()) {
							for (int j = 0; j < stklPosSortierttnachArtikelId.length; j++) {

								double dBreiteVorhanden = 0;
								if (stklPosSortierttnachArtikelId[j].getFDimension1() != null) {
									dBreiteVorhanden = Math
											.round(stklPosSortierttnachArtikelId[j].getFDimension1() * 100.) / 100.;
								}
								double dLaengeVorhanden = 0;
								if (stklPosSortierttnachArtikelId[j].getFDimension2() != null) {
									dLaengeVorhanden = Math
											.round(stklPosSortierttnachArtikelId[j].getFDimension2() * 100.) / 100.;
								}

								if (!stklPosSortierttnachArtikelId[j].getArtikelIId()
										.equals(posNX.get(j).getArtikelIId())
										|| stklPosSortierttnachArtikelId[j].getNMenge().doubleValue() != posNX.get(j)
												.getMenge().doubleValue()
										|| dBreiteVorhanden != posNX.get(j).getDBreite()
										|| dLaengeVorhanden != posNX.get(j).getDLaenge()) {

									// Fehlermeldung
									ArrayList al = new ArrayList();

									ArtikelDto artikelDtoStkl = getArtikelFac()
											.artikelFindByPrimaryKeySmall(stklDto.getArtikelIId(), theClientDto);
									al.add(artikelDtoStkl);

									ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
											stklPosSortierttnachArtikelId[j].getArtikelIId(), theClientDto);
									al.add(artikelDto);
									throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH, al,
											new Exception(""));
								}

							}
						} else {
							// Fehlermeldung
							ArrayList al = new ArrayList();

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(stklDto.getArtikelIId(), theClientDto);
							al.add(artikelDto);
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH, al,
									new Exception());

						}
					}

				} else {

					try {

						StuecklisteDto stuecklisteDto = new StuecklisteDto();
						stuecklisteDto.setArtikelIId(artikelIId);

						try {
							ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stuecklisteDto.setBAusgabeunterstueckliste(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stuecklisteDto.setBMaterialbuchungbeiablieferung(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = fertigungsgruppeFindByMandantCNr(
									theClientDto.getMandant(), theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stuecklisteDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
							}
							stuecklisteDto.setLagerIIdZiellager(
									getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
							stuecklisteDto.setNLosgroesse(new BigDecimal(1));
							stuecklisteDto.setNErfassungsfaktor(new BigDecimal(1));

							// Lt. Fr. Uhlig duerfen nur Hilfsstuecklisezn
							// angelegt werden
							stuecklisteDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));
						stklIId = getStuecklisteFac().createStueckliste(stuecklisteDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				importiereStuecklistenstrukturSiemensNX(stkl.getPositionen(), stklIId, theClientDto, alNichtimportiert,
						bStuecklisteHatBereitsPositionen);
			}
			if (stuecklisteIId != null && stkl.getMenge() != null && bNichtmehrImportieren == false) {
				try {
					StuecklistepositionDto stklposDto = new StuecklistepositionDto();

					stklposDto.setStuecklisteIId(stuecklisteIId);
					stklposDto.setArtikelIId(artikelIId);
					stklposDto.setEinheitCNr(defaultEinheit);
					stklposDto.setNMenge(stkl.getMenge());
					stklposDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
					stklposDto.setBMitdrucken(Helper.boolean2Short(true));

					if (stkl.getDLaenge() != 0 && stkl.getDBreite() != 0) {
						stklposDto.setEinheitCNr(SystemFac.EINHEIT_QUADRATMILLIMETER);
						stklposDto.setFDimension1(new Float(stkl.getDBreite()));
						stklposDto.setFDimension2(new Float(stkl.getDLaenge()));
					} else {
						if (stkl.getDLaenge() != 0) {
							stklposDto.setEinheitCNr(SystemFac.EINHEIT_MILLIMETER);
							stklposDto.setFDimension1(new Float(stkl.getDLaenge()));
						}
					}

					MontageartDto[] dtos = montageartFindByMandantCNr(theClientDto);

					if (dtos != null && dtos.length > 0) {
						stklposDto.setMontageartIId(dtos[0].getIId());
					}

					getStuecklisteFac().createStuecklisteposition(stklposDto, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		return alNichtimportiert;
	}

	private void addToParentINFRA(ArrayList<StklINFRAHelperDto> list, StklINFRAHelperDto row) {
		StklINFRAHelperDto lastRow = null;

		char c = '.';// das zu zaehlende Zeichen
		int counter = 0;// Zaehler
		for (int z = row.ebene.length() - 1; z >= 0; z--) {
			if (row.ebene.charAt(z) == c)
				counter++;
		}

		int rowEbenen = counter;

		if (list.size() > 0 && rowEbenen > 1) {
			lastRow = list.get(list.size() - 1);
			while (lastRow.unterpositionen.size() > 0 && (--rowEbenen > 1)) {
				lastRow = lastRow.unterpositionen.get(lastRow.unterpositionen.size() - 1);
			}

			lastRow.unterpositionen.add(row);
		} else {
			list.add(row);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void importiereStuecklistenINFRA(HashMap<String, HashMap<String, byte[]>> hmDateien,
			TheClientDto theClientDto) {

		try {

			Iterator<String> it = hmDateien.keySet().iterator();

			while (it.hasNext()) {

				String stuecklistenname = it.next();

				ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(stuecklistenname,
						theClientDto.getMandant());

				Integer artikelIId = null;

				if (artikelDto == null) {

					ArtikelDto aDto = new ArtikelDto();
					aDto.setCNr(stuecklistenname);
					aDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
					aDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					aDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
					aDto.setBVersteckt(Helper.boolean2Short(false));

					artikelIId = getArtikelFac().createArtikel(aDto, theClientDto);

				} else {
					artikelIId = artikelDto.getIId();
				}

				StuecklisteDto stklDto = stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
						theClientDto.getMandant());

				if (stklDto == null) {
					// Nur wenn noch keine STKL vorhanden
					HashMap<String, byte[]> dateien = hmDateien.get(stuecklistenname);

					byte[] xlsFile = dateien.get("XLS");
					try {
						ByteArrayInputStream is = new ByteArrayInputStream(xlsFile);
						Workbook workbook = Workbook.getWorkbook(is);

						Sheet sheet = workbook.getSheet(0);

						ArrayList<StklINFRAHelperDto> alDaten = new ArrayList<StklINFRAHelperDto>();

						if (sheet.getRows() > 1 && sheet.getColumns() > 4) {

							for (int i = 1; i < sheet.getRows(); i++) {
								Cell[] sZeile = sheet.getRow(i);

								String ebene = sZeile[CellReferenceHelper.getColumn("A")].getContents();

								String posnr = sZeile[CellReferenceHelper.getColumn("B")].getContents();
								String artikelnr = sZeile[CellReferenceHelper.getColumn("C")].getContents();
								String bezeichnung = sZeile[CellReferenceHelper.getColumn("D")].getContents();

								String kommentar1 = sZeile[CellReferenceHelper.getColumn("J")].getContents();
								String kommentar2 = sZeile[CellReferenceHelper.getColumn("K")].getContents();

								StklINFRAHelperDto posDto = new StklINFRAHelperDto();
								posDto.artikelnummer = artikelnr;
								posDto.bezeichnung = bezeichnung;
								posDto.position = posnr;
								posDto.ebene = ebene;

								BigDecimal menge = BigDecimal.ZERO;

								String sMenge = sZeile[CellReferenceHelper.getColumn("E")].getContents();

								if (sMenge != null && sMenge.contains(",")) {
									sMenge = sMenge.replaceAll(",", ".");
									menge = new BigDecimal(sMenge);
								}
								posDto.menge = menge;

								// Wenn Artikelnr Leer, dann ist das die
								// Zusatzbezeichnung der vorherigen Zeile
								if (artikelnr == "") {
									if (i == 1) {
										posDto.bHandeingabe = true;
										alDaten.add(posDto);
									} else {

										StklINFRAHelperDto vorheriger = alDaten.get(alDaten.size() - 1);

										if (vorheriger.kommentar.length() == 0) {
											vorheriger.kommentar = bezeichnung;
										} else {
											vorheriger.kommentar = bezeichnung + "|" + vorheriger.kommentar;
										}

										alDaten.set(alDaten.size() - 1, vorheriger);

									}
								} else {

									posDto.kommentar = kommentar1;
									if (kommentar2 != null && kommentar2.length() > 0) {
										posDto.kommentar += "|" + kommentar2;
									}

									alDaten.add(posDto);

								}

							}
						}

						// Nun noch verschachteln
						ArrayList<StklINFRAHelperDto> gesamtliste = new ArrayList<StklINFRAHelperDto>();

						for (int i = 0; i < alDaten.size(); i++) {

							addToParentINFRA(gesamtliste, alDaten.get(i));
						}

						// In Kopfstuecklsite verpacken
						ArrayList<StklINFRAHelperDto> kopf = new ArrayList<StklINFRAHelperDto>();

						StklINFRAHelperDto kopfDto = new StklINFRAHelperDto();
						kopfDto.artikelnummer = stuecklistenname;
						kopfDto.menge = BigDecimal.ONE;
						kopfDto.unterpositionen = gesamtliste;
						kopf.add(kopfDto);

						MontageartDto[] dtos = getStuecklisteFac().montageartFindByMandantCNr(theClientDto);

						Integer monatgartIId = null;
						if (dtos != null && dtos.length > 0) {
							monatgartIId = dtos[0].getIId();
						}

						getStuecklisteFac().stklINFRAanlegen(kopf, true, null, monatgartIId, theClientDto);

						// Nun alles anlegen, was noch nicht vorhandens ist

					} catch (BiffException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
					} catch (IOException e) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
					}

					dateien.remove("XLS");

					stklDto = stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId, theClientDto.getMandant());
					if (stklDto != null) {
						Iterator itPDF = dateien.keySet().iterator();

						while (itPDF.hasNext()) {
							String pdfFileName = (String) itPDF.next();
							byte[] pdfFile = dateien.get(pdfFileName);
							// In Dokumentenbalage
							PrintInfoDto oInfo = getJCRDocFac().getPathAndPartnerAndTable(stklDto.getIId(),
									QueryParameters.UC_ID_STUECKLISTE, theClientDto);

							DocPath docPath = null;
							if (oInfo != null) {
								docPath = oInfo.getDocPath();
							}
							if (docPath != null) {

								String sName = pdfFileName;

								JCRDocDto jcrDocDto = new JCRDocDto();

								Integer iPartnerIId = null;
								MandantDto mandantDto = getMandantFac()
										.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
								iPartnerIId = mandantDto.getPartnerIId();

								jcrDocDto.setbData(pdfFile);

								jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
								jcrDocDto.setlPartner(iPartnerIId);
								jcrDocDto.setsBelegnummer("");
								jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
								jcrDocDto.setlAnleger(theClientDto.getIDPersonal());
								jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
								jcrDocDto.setsSchlagworte(" ");
								jcrDocDto.setsName(sName);
								jcrDocDto.setsFilename(sName);
								if (oInfo.getTable() != null) {
									jcrDocDto.setsTable(oInfo.getTable());
								} else {
									jcrDocDto.setsTable(" ");
								}
								jcrDocDto.setsRow(" ");

								jcrDocDto.setsMIME(".pdf");
								jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
								jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
								jcrDocDto.setbVersteckt(false);
								jcrDocDto.setlVersion(getJCRDocFac().getNextVersionNumer(jcrDocDto));
								getJCRDocFac().addNewDocumentOrNewVersionOfDocument(jcrDocDto, theClientDto);

							}

						}
					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void stklINFRAanlegen(ArrayList<StklINFRAHelperDto> alDaten, boolean bKopf, Integer stuecklisteIId,
			Integer montageartIId, TheClientDto theClientDto) {

		for (int i = 0; i < alDaten.size(); i++) {
			try {
				StklINFRAHelperDto zeile = alDaten.get(i);

				String zeileKommentar = zeile.kommentar;
				if (zeileKommentar.length() > 79) {
					zeileKommentar = zeileKommentar.substring(0, 79);
				}

				if (zeile.bHandeingabe == false) {

					// Zuerst Artikel suchen und anlegen
					ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrMandantCNrOhneExc(zeile.artikelnummer,
							theClientDto.getMandant());

					Integer artikelIId = null;

					if (artikelDto == null) {

						ArtikelDto aDto = new ArtikelDto();
						aDto.setCNr(zeile.artikelnummer);
						aDto.setBLagerbewirtschaftet(Helper.boolean2Short(true));
						aDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
						aDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
						aDto.setBVersteckt(Helper.boolean2Short(false));
						ArtikelsprDto asprDto = new ArtikelsprDto();
						asprDto.setCBez(zeile.bezeichnung);
						aDto.setArtikelsprDto(asprDto);

						artikelIId = getArtikelFac().createArtikel(aDto, theClientDto);

					} else {
						artikelIId = artikelDto.getIId();
					}

					if (zeile.unterpositionen.size() > 0) {

						// Wenn Stkl vorhanden, dann auslassen

						StuecklisteDto stklDto = stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelIId,
								theClientDto.getMandant());

						if (stklDto == null) {
							// Stueckliste anlegen

							StuecklisteDto stuecklisteDto = new StuecklisteDto();
							stuecklisteDto.setArtikelIId(artikelIId);

							try {
								ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
										.getMandantparameter(theClientDto.getMandant(),
												ParameterFac.KATEGORIE_STUECKLISTE,
												ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

								stuecklisteDto.setBAusgabeunterstueckliste(
										Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

								parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
										theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
										ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

								stuecklisteDto.setBMaterialbuchungbeiablieferung(
										Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

								FertigungsgruppeDto[] fertigungsgruppeDtos = fertigungsgruppeFindByMandantCNr(
										theClientDto.getMandant(), theClientDto);

								if (fertigungsgruppeDtos.length > 0) {
									stuecklisteDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
								}
								stuecklisteDto.setLagerIIdZiellager(
										getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
								stuecklisteDto.setNLosgroesse(new BigDecimal(1));
								stuecklisteDto.setNErfassungsfaktor(new BigDecimal(1));

								stuecklisteDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

							stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));
							Integer stuecklisteIIdNeu = getStuecklisteFac().createStueckliste(stuecklisteDto,
									theClientDto);

							getStuecklisteFac().stklINFRAanlegen(zeile.unterpositionen, false, stuecklisteIIdNeu,
									montageartIId, theClientDto);

						}

					}

					if (bKopf == false) {
						StuecklistepositionDto stklposDto = new StuecklistepositionDto();

						stklposDto.setStuecklisteIId(stuecklisteIId);
						stklposDto.setArtikelIId(artikelIId);
						stklposDto.setMontageartIId(montageartIId);
						stklposDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
						stklposDto.setNMenge(zeile.menge);
						stklposDto.setCPosition(zeile.position);
						stklposDto.setCKommentar(zeileKommentar);
						stklposDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stklposDto.setBMitdrucken(Helper.boolean2Short(true));

						getStuecklisteFac().createStuecklisteposition(stklposDto, theClientDto);

					}

				} else {

					StuecklistepositionDto stklposDto = new StuecklistepositionDto();
					stklposDto.setMontageartIId(montageartIId);
					stklposDto.setStuecklisteIId(stuecklisteIId);
					stklposDto.setSHandeingabe(zeile.bezeichnung);
					stklposDto.setEinheitCNr(SystemFac.EINHEIT_STUECK);
					stklposDto.setNMenge(BigDecimal.ONE);
					stklposDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
					stklposDto.setBMitdrucken(Helper.boolean2Short(true));
					stklposDto.setCPosition(zeile.position);
					stklposDto.setCKommentar(zeileKommentar);

					getStuecklisteFac().createStuecklisteposition(stklposDto, theClientDto);

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

	}

	@org.jboss.ejb3.annotation.TransactionTimeout(5000)
	public ArrayList importiereStuecklistenstruktur(ArrayList<StrukturierterImportDto> struktur, Integer stuecklisteIId,
			TheClientDto theClientDto, boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag) throws EJBExceptionLP, RemoteException {
		return importiereStuecklistenstruktur(struktur, stuecklisteIId, theClientDto, null, bAnfragevorschlagErzeugen,
				tLieferterminfuerAnfrageVorschlag, new BigDecimal(1));
	}

	public ArrayList importiereStuecklistenstruktur(ArrayList<StrukturierterImportDto> struktur, Integer stuecklisteIId,
			TheClientDto theClientDto, ArrayList alArtikelWirdAngelegt, boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag, BigDecimal bdMultiplikatorFuerAnfragevorschlag)
			throws EJBExceptionLP, RemoteException {

		if (alArtikelWirdAngelegt == null) {
			alArtikelWirdAngelegt = new ArrayList();
		}

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		String defaultEinheit = null;
		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);

			defaultEinheit = parameter.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArrayList<Stuecklisteposition> alVorhandeneStuecklistenpositionen = new ArrayList<Stuecklisteposition>();
		if (stuecklisteIId != null) {
			Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
			query.setParameter(1, stuecklisteIId);
			Collection<Stuecklisteposition> cl = query.getResultList();
			alVorhandeneStuecklistenpositionen = new ArrayList<Stuecklisteposition>(cl);
		}
		for (int i = 0; i < struktur.size(); i++) {

			StrukturierterImportDto stkl = struktur.get(i);

			try {
				Query artikelQuery = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
				artikelQuery.setParameter(1, stkl.getArtikelnr());
				artikelQuery.setParameter(2, theClientDto.getMandant());
				Artikel artikel = (Artikel) artikelQuery.getSingleResult();
			} catch (NoResultException e) {
				alArtikelWirdAngelegt.add(stkl.getArtikelnr());
			}

			try {
				if (stkl.getEinheitCNrZielmenge() != null) {
					EinheitDto ehtDto = getSystemFac().einheitFindByPrimaryKeyOhneExc(stkl.getEinheitCNrZielmenge(),
							theClientDto);

					if (ehtDto == null) {
						ArrayList al = new ArrayList();
						al.add("Folgende Zielmengeneinheit ist nicht vorhanden: " + stkl.getEinheitCNrZielmenge()
								+ ". Position " + stkl.getPosnr());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN, al,
								new Exception("FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
					} else {
						boolean bFehlerInDimensionen = false;

						if (ehtDto.getIDimension() == 0 && (stkl.getDimension1() != null || stkl.getDimension2() != null
								|| stkl.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}
						if (ehtDto.getIDimension() == 1 && (stkl.getDimension1() == null || stkl.getDimension2() != null
								|| stkl.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}

						if (ehtDto.getIDimension() == 2 && (stkl.getDimension1() == null || stkl.getDimension2() == null
								|| stkl.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}

						if (ehtDto.getIDimension() == 3 && (stkl.getDimension1() == null || stkl.getDimension2() == null
								|| stkl.getDimension3() == null)) {
							bFehlerInDimensionen = true;
						}

						if (bFehlerInDimensionen) {
							ArrayList al = new ArrayList();
							al.add("Die Zielmengeneinheit " + stkl.getEinheitCNrZielmenge() + " erwartet "
									+ ehtDto.getIDimension()
									+ " Dimensionen. Diese sind jedoch nicht korrekt definiert. Position "
									+ stkl.getPosnr());
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN, al,
									new Exception("FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
						}
					}

				} else {

					if (stkl.getDimension1() != null || stkl.getDimension2() != null || stkl.getDimension3() != null) {
						ArrayList al = new ArrayList();
						al.add("Es sind Dimensionen vorhanden, jedoch keine Zielmengeneinheit. Position "
								+ stkl.getPosnr());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN, al,
								new Exception("FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Integer artikelIId = artikelFindenBzwNeuAnlegen(theClientDto, defaultEinheit, stkl);

			if ((stkl.getPositionen() != null && stkl.getPositionen().size() > 0)
					|| (stkl.getArtikelIIds_arbeitsgaenge() != null
							&& stkl.getArtikelIIds_arbeitsgaenge().size() > 0)) {

				Integer stklIId = null;
				StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
						theClientDto);

				if (stklDto != null) {
					stklIId = stklDto.getIId();
				} else {

					try {

						StuecklisteDto stuecklisteDto = new StuecklisteDto();
						stuecklisteDto.setArtikelIId(artikelIId);

						try {
							ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stuecklisteDto.setBAusgabeunterstueckliste(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stuecklisteDto.setBMaterialbuchungbeiablieferung(
									Helper.boolean2Short((Boolean) parameter.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = fertigungsgruppeFindByMandantCNr(
									theClientDto.getMandant(), theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stuecklisteDto.setFertigungsgruppeIId(fertigungsgruppeDtos[0].getIId());
							}
							stuecklisteDto.setLagerIIdZiellager(
									getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId());
							stuecklisteDto.setNLosgroesse(new BigDecimal(1));
							stuecklisteDto.setNErfassungsfaktor(new BigDecimal(1));

							stuecklisteDto.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						stuecklisteDto.setBFremdfertigung(Helper.boolean2Short(false));
						stklIId = getStuecklisteFac().createStueckliste(stuecklisteDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				// PJ20186 AGs importieren
				if (stkl.getArtikelIIds_arbeitsgaenge().size() > 0) {
					// Wenn zumindest ein Arbeitsgang in Import-File, dann
					// alle vorhandenen loeschen und neu eintragen
					StuecklistearbeitsplanDto[] vorhandeneDtos = stuecklistearbeitsplanFindByStuecklisteIId(stklIId,
							theClientDto);
					for (int j = 0; j < vorhandeneDtos.length; j++) {
						removeStuecklistearbeitsplan(vorhandeneDtos[j], theClientDto);
					}

					for (int j = 0; j < stkl.getArtikelIIds_arbeitsgaenge().size(); j++) {
						Integer artikelIIdArbeitsgang = stkl.getArtikelIIds_arbeitsgaenge().get(j);
						StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
						stuecklistearbeitsplanDto.setArtikelIId(artikelIIdArbeitsgang);
						stuecklistearbeitsplanDto.setStuecklisteIId(stklIId);
						stuecklistearbeitsplanDto.setIArbeitsgang(j + 1);
						stuecklistearbeitsplanDto.setLRuestzeit(new Long(0));
						stuecklistearbeitsplanDto.setLStueckzeit(new Long(0));
						stuecklistearbeitsplanDto.setBNurmaschinenzeit(Helper.boolean2Short(false));

						createStuecklistearbeitsplan(stuecklistearbeitsplanDto, theClientDto);
					}

				}

				alArtikelWirdAngelegt = importiereStuecklistenstruktur(stkl.getPositionen(), stklIId, theClientDto,
						alArtikelWirdAngelegt, bAnfragevorschlagErzeugen, tLieferterminfuerAnfrageVorschlag,
						bdMultiplikatorFuerAnfragevorschlag.multiply(new BigDecimal(stkl.getMenge())));
			}
			if (stuecklisteIId != null && stkl.getMenge() != null) {
				try {

					// Artikel suchen und nachsehen ob bereits in der
					// Stueckliste vorhanden

					String posEinheit = defaultEinheit;
					if (stkl.getEinheitCNrZielmenge() != null) {
						posEinheit = stkl.getEinheitCNrZielmenge();

						posEinheit = Helper.fitString2Length(posEinheit, 15, ' ');
						ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
						try {
							getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1), aDto.getEinheitCNr(), posEinheit,
									null, theClientDto);
						} catch (EJBExceptionLP e) {
							if (e.getCode() == EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT) {
								List<?> alErr = e.getAlInfoForTheClient();
								String eht = "";
								if (alErr != null && alErr.size() > 0) {
									eht = alErr.get(0) + "";
								}
								List<Object> al = new ArrayList<Object>();
								al.add("Die Einheiten " + eht + " k\u00f6nnen nicht konvertiert werden. Position "
										+ stkl.getPosnr());
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN, al,
										new Exception("FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
							} else {
								throw e;
							}
						}

					}

					posEinheit = Helper.fitString2Length(posEinheit, 15, ' ');

					Float dimension1Neu = null;
					Float dimension2Neu = null;
					Float dimension3Neu = null;
					if (stkl.getDimension1() != null) {
						dimension1Neu = new Float(stkl.getDimension1());
					}
					if (stkl.getDimension2() != null) {
						dimension2Neu = new Float(stkl.getDimension2());
					}
					if (stkl.getDimension3() != null) {
						dimension3Neu = new Float(stkl.getDimension3());
					}

					Stuecklisteposition posGefunden = null;
					for (int sp = 0; sp < alVorhandeneStuecklistenpositionen.size(); sp++) {
						Stuecklisteposition stklpos = alVorhandeneStuecklistenpositionen.get(sp);

						if (stklpos.getArtikelIId().equals(artikelIId)) {
							if (stklpos.getEinheitCNr().equals(posEinheit)) {

								if (stklpos.getFDimension1() != null
										&& !stklpos.getFDimension1().equals(dimension1Neu)) {
									continue;
								}
								if (stklpos.getFDimension2() != null
										&& !stklpos.getFDimension2().equals(dimension2Neu)) {
									continue;
								}
								if (stklpos.getFDimension3() != null
										&& !stklpos.getFDimension3().equals(dimension3Neu)) {
									continue;
								}

								posGefunden = stklpos;

								alVorhandeneStuecklistenpositionen.remove(sp);

								break;
							}
						}

					}

					// Wenn die Position gefunden wurde, dann updaten

					if (posGefunden != null) {
						posGefunden.setFDimension1(dimension1Neu);
						posGefunden.setFDimension2(dimension2Neu);
						posGefunden.setFDimension3(dimension3Neu);
						posGefunden.setEinheitCNr(posEinheit);
						posGefunden.setNMenge(new BigDecimal(stkl.getMenge()));
					} else {
						// Sonst neu anlegen
						StuecklistepositionDto stklposDto = new StuecklistepositionDto();

						stklposDto.setStuecklisteIId(stuecklisteIId);
						stklposDto.setArtikelIId(artikelIId);

						stklposDto.setFDimension1(dimension1Neu);
						stklposDto.setFDimension2(dimension2Neu);
						stklposDto.setFDimension3(dimension3Neu);
						stklposDto.setEinheitCNr(posEinheit);

						// Wenn Artikel in der Stueckliste bereits vorhanden,
						// dann
						// auslassen

						stklposDto.setNMenge(new BigDecimal(stkl.getMenge()));
						stklposDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stklposDto.setBMitdrucken(Helper.boolean2Short(false));

						MontageartDto[] dtos = montageartFindByMandantCNr(theClientDto);

						if (dtos != null && dtos.length > 0) {
							stklposDto.setMontageartIId(dtos[0].getIId());
						}

						getStuecklisteFac().createStuecklisteposition(stklposDto, theClientDto);
					}

					if (bAnfragevorschlagErzeugen) {
						if (stkl.getMenge() != null && stkl.getMenge().doubleValue() > 0) {
							getBestellvorschlagFac().bestellvorschlagDtoErzeugen(null, theClientDto.getMandant(),
									artikelIId, null, null, tLieferterminfuerAnfrageVorschlag,
									new BigDecimal(stkl.getMenge()).multiply(bdMultiplikatorFuerAnfragevorschlag), null,
									null, null, null, null, null, bZentralerArtikelstamm, theClientDto);
						}
					}

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}

		// Alle nun noch uebrigen Positionen entweder entfernen oder auf 0
		// setzen
		for (int i = 0; i < alVorhandeneStuecklistenpositionen.size(); i++) {
			Stuecklisteposition stklpos = alVorhandeneStuecklistenpositionen.get(i);

			Query queryVorhandene = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklistepositionIId");
			queryVorhandene.setParameter(1, stklpos.getIId());

			Collection<?> cl = queryVorhandene.getResultList();

			if (cl.size() > 0) {
				stklpos.setNMenge(BigDecimal.ZERO);
			} else {
				em.remove(stklpos);
			}

		}

		return alArtikelWirdAngelegt;
	}

	public Integer artikelFindenBzwNeuAnlegen(TheClientDto theClientDto, String defaultEinheit,
			StrukturierterImportDto stkl) throws EJBExceptionLP, RemoteException {
		Integer artikelIId = null;

		try {
			Query artikelQuery = em.createNamedQuery("ArtikelfindByCNrMandantCNr");
			artikelQuery.setParameter(1, stkl.getArtikelnr());
			artikelQuery.setParameter(2, theClientDto.getMandant());
			Artikel artikel = (Artikel) artikelQuery.getSingleResult();

			artikelIId = artikel.getIId();

			// Neue Versionen werden seit PJ18330 gar nicht mehr erzeugt

		} catch (NoResultException e) {

			// Lt. Hr. Ehrengruber muessen ALLE Artikel, die nicht vorhanden
			// sind, neu angelegt werden

			/*
			 * if (stkl.getAnhaengeMitFileDatum() == null ||
			 * stkl.getAnhaengeMitFileDatum().size() == 0) { // Fehler, da Normteil immer
			 * vorhanhen sein muss
			 * 
			 * ArrayList alDaten = new ArrayList();
			 * 
			 * alDaten.add(stkl.getArtikelnr());
			 * 
			 * throw new EJBExceptionLP(
			 * EJBExceptionLP.FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN, alDaten, new
			 * Exception( "FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN:" +
			 * stkl.getArtikelnr())); }
			 */
		}

		if (artikelIId == null) {
			// Neu anlegen
			ArtikelDto artikelDto = new ArtikelDto();

			artikelDto.setCNr(stkl.getArtikelnr());

			artikelDto.setBVersteckt(Helper.boolean2Short(false));

			artikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);

			ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();

			String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] { 40, 40 }, stkl.getArtikelbez());
			if (bezeichnungen != null) {
				oArtikelsprDto.setCBez(bezeichnungen[0]);
				oArtikelsprDto.setCZbez(bezeichnungen[1]);
			}

			if (stkl.getAbmessungen() != null && stkl.getAbmessungen().length() > 0) {
				oArtikelsprDto.setCZbez2(stkl.getAbmessungen());
			}

			// Material
			artikelDto.setMaterialIId(stkl.getMaterial());
			artikelDto.setArtikelsprDto(oArtikelsprDto);
			if (stkl.getEinheitCNrZielmenge() != null) {
				artikelDto.setEinheitCNr(Helper.fitString2Length(stkl.getEinheitCNrZielmenge(), 15, ' '));
			} else {
				artikelDto.setEinheitCNr(defaultEinheit);
			}

			artikelDto.setFGewichtkg(stkl.getGewicht());

			// Liefergruppe
			if (stkl.getLiefergruppe() != null && stkl.getLiefergruppe().length() > 0) {

				Integer liefergruppeIId = null;
				try {
					Query lfq = em.createNamedQuery("LfliefergruppeFindByCNrMandantCNr");
					lfq.setParameter(1, stkl.getLiefergruppe());
					lfq.setParameter(2, theClientDto.getMandant());
					Lfliefergruppe lfliefergruppe = (Lfliefergruppe) lfq.getSingleResult();
					liefergruppeIId = lfliefergruppe.getIId();
				} catch (NoResultException e) {
					// Wenns eine Liefergruppe nicht gibt, dann Fehler
					ArrayList alInfo = new ArrayList();
					alInfo.add(stkl.getLiefergruppe());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN,
							alInfo, new Exception("FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN"));
				}
				artikelDto.setLfliefergruppeIId(liefergruppeIId);
			}

			artikelIId = getArtikelFac().createArtikel(artikelDto, theClientDto);

		}

		// Zuerst alle Anhaenge loeschen damit immer die aktuellsten eingetragen
		// sind

		// Eventuell vorhandene Anhaenge loeschen
		Query artikelQ = em.createNamedQuery("KommentarimportFindAll");
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			try {
				Query artikelkommenentarVorhanden = em
						.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIId");
				artikelkommenentarVorhanden.setParameter(1, artikelIId);
				artikelkommenentarVorhanden.setParameter(2, kiDtos[i].getArtikelkommentarartIId());
				Collection vorhanden = artikelkommenentarVorhanden.getResultList();
				Iterator itKomm = vorhanden.iterator();
				while (itKomm.hasNext()) {
					Artikelkommentar ak = (Artikelkommentar) itKomm.next();
					ArtikelkommentarDto aDto = getArtikelkommentarFac().artikelkommentarFindByPrimaryKey(ak.getIId(),
							theClientDto);
					getArtikelkommentarFac().removeArtikelkommentar(aDto);
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		// Anhaenge
		if (stkl.getAnhaengeMitFileDatum() != null && stkl.getAnhaengeMitFileDatum().size() > 0) {
			// ANHAENGE

			Iterator it = stkl.getAnhaengeMitFileDatum().keySet().iterator();
			String dateiname = (String) it.next();
			String datenformat = MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT;
			if (dateiname.toLowerCase().endsWith(".pdf")) {
				datenformat = MediaFac.DATENFORMAT_MIMETYPE_APP_PDF;
			} else if (dateiname.toLowerCase().endsWith(".jpg")) {
				datenformat = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG;
			} else if (dateiname.toLowerCase().endsWith(".gif")) {
				datenformat = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF;
			} else if (dateiname.toLowerCase().endsWith(".png")) {
				datenformat = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG;
			} else {
				datenformat = MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT;
			}

			Object[] oDatei = stkl.getAnhaengeMitFileDatum().get(dateiname);
			byte[] datei = (byte[]) oDatei[0];
			Timestamp tFileDatum = (Timestamp) oDatei[1];

			for (int i = 0; i < kiDtos.length; i++) {
				ArtikelkommentarDto kommDto = new ArtikelkommentarDto();
				kommDto.setArtikelIId(artikelIId);
				kommDto.setIArt(ArtikelkommentarFac.ARTIKELKOMMENTARART_ANHANG);
				kommDto.setArtikelkommentarartIId(kiDtos[i].getArtikelkommentarartIId());
				kommDto.setBDefaultbild(Helper.boolean2Short(false));
				kommDto.setBDateiverweis(Helper.boolean2Short(false));

				kommDto.setDatenformatCNr(datenformat);

				ArtikelkommentarsprDto sprDto = new ArtikelkommentarsprDto();
				sprDto.setCDateiname(dateiname);
				sprDto.setOMedia(datei);
				sprDto.setTFiledatum(tFileDatum);

				kommDto.setArtikelkommentarsprDto(sprDto);

				try {
					Query artikelkommenentarVorhanden = em
							.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIIdDatenformatCNr");
					artikelkommenentarVorhanden.setParameter(1, artikelIId);
					artikelkommenentarVorhanden.setParameter(2, kiDtos[i].getArtikelkommentarartIId());
					artikelkommenentarVorhanden.setParameter(3, datenformat);
					Artikelkommentar vorhanden = (Artikelkommentar) artikelkommenentarVorhanden.getSingleResult();
					if (vorhanden != null) {
						// Artikelkommentardruck updaten
						try {
							Query artikelkommenentarDruckVorhanden = em.createNamedQuery(
									"ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
							artikelkommenentarDruckVorhanden.setParameter(1, artikelIId);
							artikelkommenentarDruckVorhanden.setParameter(2, vorhanden.getIId());
							artikelkommenentarDruckVorhanden.setParameter(3, kiDtos[i].getBelegartCNr());
							Artikelkommentardruck druckVorhanden = (Artikelkommentardruck) artikelkommenentarDruckVorhanden
									.getSingleResult();
						} catch (NoResultException ex) {
							// neu hinzufuegen
							ArtikelkommentardruckDto druckDto = new ArtikelkommentardruckDto();
							druckDto.setBelegartCNr(kiDtos[i].getBelegartCNr());
							druckDto.setArtikelkommentarIId(vorhanden.getIId());
							druckDto.setArtikelIId(artikelIId);
							try {
								getArtikelkommentarFac().createArtikelkommentardruck(druckDto);
							} catch (RemoteException ex3) {

								throwEJBExceptionLPRespectOld(ex3);
							}

						}

					}
				} catch (NoResultException ex) {
					ArtikelkommentardruckDto[] druckDto = new ArtikelkommentardruckDto[1];
					druckDto[0] = new ArtikelkommentardruckDto();
					druckDto[0].setBelegartCNr(kiDtos[i].getBelegartCNr());

					kommDto.setArtikelkommentardruckDto(druckDto);
					try {
						getArtikelkommentarFac().createArtikelkommentar(kommDto, theClientDto);
					} catch (RemoteException ex3) {

						throwEJBExceptionLPRespectOld(ex3);
					}
				}

			}

		}
		return artikelIId;
	}

	public void kopiereParameterEinerStueckliste(Integer stuecklisteIIdQuelle, Integer stuecklisteIIdZiel,
			Map<String, Object> konfigurationsWerte, TheClientDto theClientDto) {
		// try {
		Query query = em.createNamedQuery("StklparameterFindByStuecklisteIId");
		query.setParameter(1, stuecklisteIIdQuelle);
		// @todo getSingleResult oder getResultList ?
		StklparameterDto[] stklparameterDtos = StklparameterDtoAssembler.createDtos(query.getResultList());
		query = em.createNamedQuery("StklparameterFindByStuecklisteIIdCNr");
		for (int i = 0; i < stklparameterDtos.length; i++) {
			query.setParameter(1, stuecklisteIIdZiel);
			query.setParameter(2, stklparameterDtos[i].getCNr());
			try {
				Stklparameter stklparameter = (Stklparameter) query.getSingleResult();

			} catch (NoResultException ex2) {
				// Dann eintragen

				Integer stklparamterIIdOld = stklparameterDtos[i].getIId();
				StklparameterDto stklparameterDto = stklparameterDtos[i];
				stklparameterDto.setIId(null);
				stklparameterDto.setISort(null);
				stklparameterDto.setStuecklisteIId(stuecklisteIIdZiel);

				// Wert
				if (konfigurationsWerte != null && konfigurationsWerte.containsKey(stklparameterDtos[i].getCNr())) {
					Object value = konfigurationsWerte.get(stklparameterDtos[i].getCNr());

					if (value != null) {
						if (value instanceof com.lp.server.util.KundeId) {
							com.lp.server.util.KundeId kdId = ((com.lp.server.util.KundeId) value);

							stklparameterDto.setCWert(kdId.id().toString());

						} else if (value instanceof com.lp.server.stueckliste.service.ItemId) {
							com.lp.server.stueckliste.service.ItemId itemId = ((com.lp.server.stueckliste.service.ItemId) value);
							stklparameterDto.setCWert(itemId.getId().toString());
						} else {

							stklparameterDto.setCWert(value.toString());
						}
					}
				}

				Integer stklparameterIId = createStklparameter(stklparameterDto, theClientDto);

				Query queryspr = em.createNamedQuery("StklparametersprfindByStklparameterIId");
				queryspr.setParameter(1, stklparamterIIdOld);
				Collection<?> allspr = queryspr.getResultList();
				Iterator<?> iter = allspr.iterator();
				while (iter.hasNext()) {
					Stklparameterspr stklparameterspr = (Stklparameterspr) iter.next();

					Stklparameterspr neu = new Stklparameterspr(theClientDto.getLocUiAsString(), stklparameterIId,
							stklparameterspr.getCBez());
					em.persist(neu);
					em.flush();
				}

			}
		}

	}

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle, Integer stuecklisteIId_Ziel,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null"));
		}

		// try {
		Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId_Quelle);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		//
		// }
		StuecklistearbeitsplanDto[] dtos = assembleStuecklistearbeitsplanDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			StuecklistearbeitsplanDto dto = dtos[i];
			dto.setStuecklisteIId(stuecklisteIId_Ziel);
			// SP6932
			dto.setStuecklistepositionIId(null);

			// lt. WH 2010-09-06 bei P&S muss der Arbeitsgang erhalten bleiben
			/*
			 * Integer iArbeitsgang = getNextArbeitsgang(stuecklisteIId_Ziel, theClientDto);
			 * 
			 * if (iArbeitsgang == null) { iArbeitsgang = new Integer(10); }
			 * 
			 * dto.setIArbeitsgang(iArbeitsgang); // Damit automatisch drangehaengt
			 */
			// wird
			createStuecklistearbeitsplan(dto, theClientDto);
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);

		// }
	}

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Stuecklistearbeitsplan stuecklistearbeitsplan = em.find(Stuecklistearbeitsplan.class, iId);
		if (stuecklistearbeitsplan == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		StuecklistearbeitsplanDto stuecklistearbeitsplanDto = assembleStuecklistearbeitsplanDto(stuecklistearbeitsplan);

		stuecklistearbeitsplanDto.setArtikelDto(
				getArtikelFac().artikelFindByPrimaryKey(stuecklistearbeitsplanDto.getArtikelIId(), theClientDto));

		return stuecklistearbeitsplanDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public Integer getNextArbeitsgang(Integer stuecklisteIId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("stuecklisteIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("StuecklistearbeitsplanejbSelectNextReihung");
				querynext.setParameter(1, stuecklisteIId);
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

	public Integer getNextFertigungsgruppe(TheClientDto theClientDto) throws EJBExceptionLP {

		try {
			Query querynext = em.createNamedQuery("FertigungsgruppeejbSelectNextReihung");
			querynext.setParameter(1, theClientDto.getMandant());

			Integer i = (Integer) querynext.getSingleResult();
			if (i != null) {
				i = 1 + 1;
			} else {
				i = 1;
			}
			return i;
		} catch (NoResultException ex) {
			return new Integer(1);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

	}

	private void setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(Stuecklistearbeitsplan stuecklistearbeitsplan,
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto) {
		stuecklistearbeitsplan.setStuecklisteIId(stuecklistearbeitsplanDto.getStuecklisteIId());
		stuecklistearbeitsplan.setIArbeitsgang(stuecklistearbeitsplanDto.getIArbeitsgang());
		stuecklistearbeitsplan.setArtikelIId(stuecklistearbeitsplanDto.getArtikelIId());
		stuecklistearbeitsplan.setLStueckzeit(stuecklistearbeitsplanDto.getLStueckzeit());
		stuecklistearbeitsplan.setLRuestzeit(stuecklistearbeitsplanDto.getLRuestzeit());
		stuecklistearbeitsplan.setCKommentar(stuecklistearbeitsplanDto.getCKommentar());
		stuecklistearbeitsplan.setXLangtext(stuecklistearbeitsplanDto.getXLangtext());
		stuecklistearbeitsplan.setMaschineIId(stuecklistearbeitsplanDto.getMaschineIId());
		stuecklistearbeitsplan.setIAufspannung(stuecklistearbeitsplanDto.getIAufspannung());
		stuecklistearbeitsplan.setAgartCNr(stuecklistearbeitsplanDto.getAgartCNr());
		stuecklistearbeitsplan.setIUnterarbeitsgang(stuecklistearbeitsplanDto.getIUnterarbeitsgang());
		stuecklistearbeitsplan.setIMaschinenversatztage(stuecklistearbeitsplanDto.getIMaschinenversatztage());

		stuecklistearbeitsplan.setBNurmaschinenzeit(stuecklistearbeitsplanDto.getBNurmaschinenzeit());
		stuecklistearbeitsplan.setStuecklistepositionIId(stuecklistearbeitsplanDto.getStuecklistepositionIId());
		stuecklistearbeitsplan.setApkommentarIId(stuecklistearbeitsplanDto.getApkommentarIId());
		stuecklistearbeitsplan.setNPpm(stuecklistearbeitsplanDto.getNPpm());
		stuecklistearbeitsplan.setXFormel(stuecklistearbeitsplanDto.getXFormel());
		stuecklistearbeitsplan.setIMitarbeitergleichzeitig(stuecklistearbeitsplanDto.getIMitarbeitergleichzeitig());
		stuecklistearbeitsplan.setBInitial(stuecklistearbeitsplanDto.getBInitial());
		em.merge(stuecklistearbeitsplan);
		em.flush();
	}

	private StuecklistearbeitsplanDto assembleStuecklistearbeitsplanDto(Stuecklistearbeitsplan stuecklistearbeitsplan) {
		return StuecklistearbeitsplanDtoAssembler.createDto(stuecklistearbeitsplan);
	}

	private StuecklistearbeitsplanDto[] assembleStuecklistearbeitsplanDtos(Collection<?> stuecklistearbeitsplans) {
		List<StuecklistearbeitsplanDto> list = new ArrayList<StuecklistearbeitsplanDto>();
		if (stuecklistearbeitsplans != null) {
			Iterator<?> iterator = stuecklistearbeitsplans.iterator();
			while (iterator.hasNext()) {
				Stuecklistearbeitsplan stuecklistearbeitsplan = (Stuecklistearbeitsplan) iterator.next();
				list.add(assembleStuecklistearbeitsplanDto(stuecklistearbeitsplan));
			}
		}
		StuecklistearbeitsplanDto[] returnArray = new StuecklistearbeitsplanDto[list.size()];
		return (StuecklistearbeitsplanDto[]) list.toArray(returnArray);
	}

	public java.util.HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(Integer artikelIId,
			TheClientDto theClientDto) {
		String sMandant = theClientDto.getMandant();
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class);

		// PJ19400
		if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
				theClientDto)) {
			crit.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
					"s").add(Restrictions.eq("s.mandant_c_nr", sMandant));
		}

		crit.createAlias(com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a")
				.add(Restrictions.eq("a.i_id", artikelIId)).addOrder(Order.asc("a.c_nr"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		java.util.HashMap<Integer, String> al = new java.util.HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition stuecklisteposition = (FLRStuecklisteposition) resultListIterator.next();
			al.put(stuecklisteposition.getStueckliste_i_id(), "");
		}
		return al;
	}

	public void sollzeitenAnhandLosistzeitenAktualisieren(Integer stuecklisteIId, java.sql.Date tVon,
			java.sql.Date tBis, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = factory.openSession();
		Criteria c = session.createCriteria(FLRLos.class);

		c.add(Restrictions.eq(FertigungFac.FLR_LOS_STUECKLISTE_I_ID, stuecklisteIId));

		c.add(Restrictions.or(Restrictions.between(FertigungFac.FLR_LOS_T_ERLEDIGT, tVon, tBis),
				Restrictions.between(FertigungFac.FLR_LOSREPORT_T_MANUELLERLEDIGT, tVon, tBis)));

		List<?> results = c.list();

		Iterator<?> resultListIterator = results.iterator();

		class AbgeliefertIstZeit {

			AbgeliefertIstZeit(BigDecimal bdAbgeliefert, BigDecimal bdIstZeit) {
				this.bdAbgeliefert = bdAbgeliefert;
				this.bdIstZeit = bdIstZeit;
			}

			private BigDecimal bdAbgeliefert = BigDecimal.ZERO;
			private BigDecimal bdIstZeit = BigDecimal.ZERO;

			public BigDecimal getBdAbgeliefert() {
				return bdAbgeliefert;
			}

			public void addAbgeliefert(BigDecimal bdAbgeliefert) {
				this.bdAbgeliefert = this.bdAbgeliefert.add(bdAbgeliefert);
			}

			public BigDecimal getBdIstZeit() {
				return bdIstZeit;
			}

			public void addIstZeit(BigDecimal bdIstZeit) {
				this.bdIstZeit = this.bdIstZeit.add(bdIstZeit);
			}
		}

		HashMap<Integer, AbgeliefertIstZeit> hmZeiten = new HashMap<Integer, AbgeliefertIstZeit>();

		ArrayList<StuecklistearbeitsplanDto> alStuecklistePositionen = new ArrayList<StuecklistearbeitsplanDto>();

		// nun den gesamten Arbeitsplan der Stueckliste ins Los
		// kopieren
		StuecklistearbeitsplanDto[] stkPos = getStuecklisteFac()
				.stuecklistearbeitsplanFindByStuecklisteIId(stuecklisteIId, theClientDto);
		for (int i = 0; i < stkPos.length; i++) {
			alStuecklistePositionen.add(stkPos[i]);
		}

		while (resultListIterator.hasNext()) {
			FLRLos los = (FLRLos) resultListIterator.next();

			// Vergleichen, ob alle Lose denselben Arbeitsplan wie die Stueckliste haben

			try {

				BigDecimal bdAbgeliefert = getFertigungFac().getErledigteMenge(los.getI_id(), theClientDto);

				LossollarbeitsplanDto[] losAPDtos = getFertigungFac().lossollarbeitsplanFindByLosIId(los.getI_id());

				if (losAPDtos.length != stkPos.length) {
					ArrayList al = new ArrayList();
					al.add(los.getC_nr());

					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_ANZAHL_UNGLEICH, al,
							new Exception("FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_ANZAHL_UNGLEICH"));
				}

				AuftragzeitenDto[] mannZeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(LocaleFac.BELEGART_LOS,
						los.getI_id(), null, null, null, null, ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL,
						theClientDto);

				for (int u = 0; u < losAPDtos.length; u++) {

					LossollarbeitsplanDto losapDto = losAPDtos[u];

					boolean bInStuecklisteGefunden = false;
					for (int j = 0; j < stkPos.length; j++) {

						StuecklistearbeitsplanDto stkAPDto = stkPos[j];

						if (stkAPDto.getIArbeitsgang().equals(losapDto.getIArbeitsgangnummer())
								&& stkAPDto.getArtikelIId().equals(losapDto.getArtikelIIdTaetigkeit())) {
							if ((stkAPDto.getIUnterarbeitsgang() == null && losapDto.getIUnterarbeitsgang() == null)
									|| (stkAPDto.getIUnterarbeitsgang() != null && stkAPDto.getIUnterarbeitsgang()
											.equals(losapDto.getIUnterarbeitsgang()))) {

								bInStuecklisteGefunden = true;

								BigDecimal bdZeitPerson = new BigDecimal(0);

								for (int i = 0; i < mannZeiten.length; i++) {
									if (losapDto.getIId().equals(mannZeiten[i].getBelegpositionIId())) {
										bdZeitPerson = bdZeitPerson
												.add(new BigDecimal(mannZeiten[i].getDdDauer().doubleValue()));
									}
								}

								AbgeliefertIstZeit abgeliefertIstZeit = null;
								if (hmZeiten.containsKey(stkAPDto.getIId())) {
									abgeliefertIstZeit = hmZeiten.get(stkAPDto.getIId());
									abgeliefertIstZeit.addAbgeliefert(bdAbgeliefert);
									abgeliefertIstZeit.addIstZeit(bdZeitPerson);
								} else {
									abgeliefertIstZeit = new AbgeliefertIstZeit(bdAbgeliefert, bdZeitPerson);
								}
								hmZeiten.put(stkAPDto.getIId(), abgeliefertIstZeit);

							}
						}
					}

					if (bInStuecklisteGefunden == false) {
						ArrayList al = new ArrayList();
						al.add(los.getC_nr());
						al.add(losapDto.getIArbeitsgangnummer());
						al.add(losapDto.getIUnterarbeitsgang());
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKeySmall(losapDto.getArtikelIIdTaetigkeit(), theClientDto);

						al.add(aDto.formatArtikelbezeichnung());

						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_IN_STKL_NICHT_GEFUNDEN,
								al, new Exception(
										"FEHLER_FERTIGUNG_UPDATE_STKL_SOLLZEIT_ARBEITSPLAN_IN_STKL_NICHT_GEFUNDEN"));

					}

				}

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		Iterator itAP_ids = hmZeiten.keySet().iterator();
		while (itAP_ids.hasNext()) {
			Integer stuecklistearbeitsplanIId = (Integer) itAP_ids.next();
			AbgeliefertIstZeit abgeliefertIstZeit = hmZeiten.get(stuecklistearbeitsplanIId);

			if (abgeliefertIstZeit.getBdAbgeliefert().doubleValue() > 0) {
				try {
					StuecklistearbeitsplanDto apDto = getStuecklisteFac()
							.stuecklistearbeitsplanFindByPrimaryKey(stuecklistearbeitsplanIId, theClientDto);

					BigDecimal bdNeueStueckzeit = abgeliefertIstZeit.getBdIstZeit()
							.divide(abgeliefertIstZeit.getBdAbgeliefert(), 10, BigDecimal.ROUND_HALF_EVEN);
					Long lStueckzeit = bdNeueStueckzeit.multiply(new BigDecimal(3600000)).longValue();
					apDto.setLStueckzeit(lStueckzeit);

					updateStuecklistearbeitsplan(apDto, theClientDto);

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

	}

	/**
	 * uselocalspr: 0 Hole alle Artikelarten nach Spr.
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllStuecklisteart(TheClientDto theClientDto) {

		TreeMap<String, String> tmArten = new TreeMap<String, String>();
		// try {

		boolean bSetartikel = getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_SETARTIKEL,
				theClientDto);

		Query query = em.createNamedQuery("StuecklisteartfindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Stuecklisteart stuecklisteart = (Stuecklisteart) itArten.next();

			if (stuecklisteart.getCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL) && bSetartikel == false) {
				continue;
			}

			tmArten.put(stuecklisteart.getCNr(), stuecklisteart.getCBez());
		}

		return tmArten;
	}

	public Map getAllPruefart(TheClientDto theClientDto) {

		TreeMap<Integer, String> tmArten = new TreeMap<Integer, String>();
		// try {

		Query query = em.createNamedQuery("PruefartfindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Pruefart pruefart = (Pruefart) itArten.next();

			String value = pruefart.getCNr();

			Pruefartspr pruefartspr = em.find(Pruefartspr.class,
					new PruefartsprPK(theClientDto.getLocUiAsString(), pruefart.getIId()));
			if (pruefartspr != null && pruefartspr.getCBez() != null) {

				value = pruefartspr.getCBez();
			}

			tmArten.put(pruefart.getIId(), value);
		}

		return tmArten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public VerkaufspreisDto getKalkuliertenVerkaufspreisAusGesamtkalkulation(Integer artikelIId, BigDecimal bdMenge,
			java.sql.Date belegdatum, String waehrungCNr, TheClientDto theClientDto) {

		VerkaufspreisDto verkaufspreisDto = null;

		StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId,
				theClientDto);

		if (stklDto != null) {
			JasperPrintLP print = getStuecklisteReportFac().printGesamtkalkulation(stklDto.getIId(), bdMenge, false,
					false, false, false, true, false, false, null, null, null, false, null, theClientDto);

			Map<String, Object> parameters = print.getMapParameters();

			BigDecimal bdSelbstkostenLief = (BigDecimal) parameters.get("P_SELBSTKOSTENLIEF");
			Double dSelbstkostenLief = (Double) parameters.get("P_STUECKLISTE_MINDESTDECKUNGSBEITRAG");

			BigDecimal bdBerechneterPreis = bdSelbstkostenLief
					.multiply((new BigDecimal((1 + dSelbstkostenLief / 100))));

			try {
				bdBerechneterPreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdBerechneterPreis,
						theClientDto.getSMandantenwaehrung(), waehrungCNr, belegdatum, theClientDto);
				bdBerechneterPreis = Helper.rundeKaufmaennisch(bdBerechneterPreis, 4);
				verkaufspreisDto = new VerkaufspreisDto();
				verkaufspreisDto.einzelpreis = bdBerechneterPreis;
				verkaufspreisDto.nettopreis = bdBerechneterPreis;
				verkaufspreisDto.bKommtVonFixpreis = true;
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

		}

		return verkaufspreisDto;
	}

	public Map getEingeschraenkteFertigungsgruppen(TheClientDto theClientDto) {
		Query query = em.createNamedQuery("FertigungsgrupperollefindBySystemrolleIId");
		query.setParameter(1, theClientDto.getSystemrolleIId());
		Collection<Fertigungsgrupperolle> clArten = query.getResultList();

		if (clArten.size() == 0) {
			return null;
		}

		List<Fertigungsgruppe> fgrs = new ArrayList<Fertigungsgruppe>();
		for (Fertigungsgrupperolle fgr : clArten) {
			Fertigungsgruppe fg = em.find(Fertigungsgruppe.class, fgr.getFertigungsgruppeIId());
			if (fg != null && fg.getMandantCNr().equals(theClientDto.getMandant())) {
				fgrs.add(fg);
			}
		}

		Collections.sort(fgrs, new Comparator<Fertigungsgruppe>() {
			@Override
			public int compare(Fertigungsgruppe arg0, Fertigungsgruppe arg1) {
				int diff = arg0.getISort().compareTo(arg1.getISort());
				if (diff == 0) {
					diff = arg0.getCBez().compareTo(arg1.getCBez());
				}
				return diff;
			}
		});

		LinkedHashMap<Integer, String> returnMap = new LinkedHashMap<Integer, String>();
		for (Fertigungsgruppe fertigungsgruppe : fgrs) {
			returnMap.put(fertigungsgruppe.getIId(), fertigungsgruppe.getCBez());
		}
		return returnMap;
		/*
		 * if (clArten.size() == 0) { return null; } else {
		 * 
		 * TreeMap<Integer, Fertigungsgruppe> tmArten = new TreeMap<Integer,
		 * Fertigungsgruppe>(); Iterator<?> itArten = clArten.iterator();
		 * 
		 * while (itArten.hasNext()) { Fertigungsgrupperolle fertigungsgrupperolle =
		 * (Fertigungsgrupperolle) itArten .next();
		 * 
		 * Fertigungsgruppe fertigungsgruppe = em.find( Fertigungsgruppe.class,
		 * fertigungsgrupperolle.getFertigungsgruppeIId());
		 * 
		 * tmArten.put(fertigungsgruppe.getISort(), fertigungsgruppe); }
		 * LinkedHashMap<Integer, String> returnMap = new LinkedHashMap<Integer,
		 * String>(); Iterator<?> itSorted = tmArten.values().iterator(); while
		 * (itSorted.hasNext()) { Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe)
		 * itSorted .next();
		 * 
		 * returnMap.put(fertigungsgruppe.getIId(), fertigungsgruppe.getCBez());
		 * 
		 * } return returnMap; }
		 */
	}

	public Map getAllFertigungsgrupe(TheClientDto theClientDto) {

		LinkedHashMap<Integer, String> tmArten = new LinkedHashMap<Integer, String>();

		Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) itArten.next();

			tmArten.put(fertigungsgruppe.getIId(), fertigungsgruppe.getCBez());
		}

		return tmArten;
	}

	public Integer createStuecklistepositions(StuecklistepositionDto[] stuecklistepositionDtos,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Integer iId = null;
		for (int i = 0; i < stuecklistepositionDtos.length; i++) {
			iId = createStuecklisteposition(stuecklistepositionDtos[i], theClientDto);
		}
		return iId;
	}

	public Integer createStuecklistearbeitsplans(StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = null;
		for (int i = 0; i < stuecklistearbeitsplanDtos.length; i++) {
			iId = createStuecklistearbeitsplan(stuecklistearbeitsplanDtos[i], theClientDto);
		}
		return iId;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public BigDecimal getGesamtgewichtEinerStuecklisteInKg(Integer stuecklisteIId, BigDecimal nLosgroesse,
			TheClientDto theClientDto) {

		BigDecimal bdGesamtgewichtInKG = BigDecimal.ZERO;

		JasperPrintLP print = getStuecklisteReportFac().printGesamtkalkulation(stuecklisteIId, nLosgroesse, false,
				false, false, false, true, false, false, null, null, null, false, null, theClientDto);

		Object[][] daten = print.getDatenMitUeberschrift();

		if (daten.length > 0) {

			int spalteGewicht = 0;
			int spalteMenge = 0;

			for (int i = 0; i < daten[0].length; i++) {

				String ueberschrift = (String) daten[0][i];

				if (ueberschrift.equals("Artikelgewicht")) {
					spalteGewicht = i;
				}
				if (ueberschrift.equals("Menge")) {
					spalteMenge = i;
				}

			}

			for (int i = 1; i < daten.length; i++) {
				Double gewicht = (Double) daten[i][spalteGewicht];
				BigDecimal bdMenge = (BigDecimal) daten[i][spalteMenge];

				if (gewicht != null && bdMenge != null) {
					bdGesamtgewichtInKG = bdGesamtgewichtInKG.add(bdMenge.multiply(new BigDecimal(gewicht)));
				}

			}

		}

		return bdGesamtgewichtInKG;
	}

	public Integer createStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getStuecklisteIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getStuecklisteIId() == null"));

		}
		if (stuecklistepositionDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getNMenge() == null"));

		}
		if (stuecklistepositionDto.getEinheitCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getEinheitCNr() == null"));

		}
		if (stuecklistepositionDto.getPositionsartCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getPositionsartCNr() == null"));

		}
		if (stuecklistepositionDto.getMontageartIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getMontageartIId() == null"));

		}

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistepositionDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (!Helper.short2boolean(stueckliste.getBMitFormeln())) {
			stuecklistepositionDto.setXFormel(null);
		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			// PRUEFEN, OB KEIN DEADLOCK:
			try {
				Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, stuecklistepositionDto.getArtikelIId());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stueckliste1 = (Stueckliste) query.getSingleResult();
				if (stueckliste1 != null) {
					Integer stuecklisteIId = stueckliste1.getIId();
					if (pruefeObArtikelStuecklistenstrukturSchonVorhanden(stuecklisteIId,
							stuecklistepositionDto.getStuecklisteIId(), theClientDto)
							|| stuecklistepositionDto.getArtikelIId()
									.equals(stuecklisteFindByPrimaryKey(stuecklistepositionDto.getStuecklisteIId(),
											theClientDto).getArtikelIId())) {

						String artikelnummer = getArtikelFac()
								.artikelFindByPrimaryKeySmall(stuecklistepositionDto.getArtikelIId(), theClientDto)
								.getCNr();
						ArrayList alInfo = new ArrayList();
						alInfo.add(artikelnummer);
						throw new EJBExceptionLP(EJBExceptionLP.STUECKLISTE_DEADLOCK, alInfo,
								new Exception("DEADLOCK"));

					}

				}
			} catch (NoResultException ex2) {
				// Wenn keine Stueckliste, dann egal
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

			// PJ 14647

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stuecklistepositionDto.getArtikelIId(), theClientDto);

			// PJ 16622
			if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				Artikel artikel = em.find(Artikel.class, stuecklistepositionDto.getArtikelIId());
				// SP3353
				Artikel artikelStueckliste = em.find(Artikel.class, stueckliste.getArtikelIId());

				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {
					if (Helper.short2boolean(artikelStueckliste.getBSeriennrtragend())) {

						if (stuecklistepositionDto.getNMenge().doubleValue() != 1) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
									new Exception(
											"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

						}
					}
				}

			}

			if (stuecklisteDto != null
					&& stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
						new Exception("FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH"));

			}

			/*
			 * auskommentiert wg. PJ 16827 if (stuecklisteDto != null &&
			 * !stueckliste.getStuecklisteartCNr().equals(
			 * StuecklisteFac.STUECKLISTEART_STUECKLISTE)) {
			 * 
			 * throw new EJBExceptionLP( EJBExceptionLP.
			 * FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN
			 * , new Exception(
			 * "FEHLER_STUECKLISTENART_ARTIKELSET_BZW_ARTIKELSET_DARF_KEINE_STUECKLISTE_ENTHALTEN"
			 * ));
			 * 
			 * }
			 */

		}

		if (stuecklistepositionDto.getIBeginnterminoffset() == null) {
			stuecklistepositionDto.setIBeginnterminoffset(0);
		}

		if (stuecklistepositionDto.getBRuestmenge() == null) {
			stuecklistepositionDto.setBRuestmenge(Helper.boolean2Short(false));
		}
		
		if (stuecklistepositionDto.getBInitial() == null) {
			stuecklistepositionDto.setBInitial(Helper.boolean2Short(false));
		}
		

		verifyStuecklistepositionFormel(stueckliste, stuecklistepositionDto, theClientDto);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEPOSITION);
		stuecklistepositionDto.setIId(pk);
		if (stuecklistepositionDto.getISort() == null) {
			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("StuecklistepositionejbSelectNextReihung");
				querynext.setParameter(1, stuecklistepositionDto.getStuecklisteIId());
				i = (Integer) querynext.getSingleResult();
			} catch (NoResultException ex) {
			}
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			stuecklistepositionDto.setISort(i);

		}

		java.sql.Timestamp sqlTimestamp = getTimestamp();

		stuecklistepositionDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		stuecklistepositionDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklistepositionDto.setTAnlegen(sqlTimestamp);
		stuecklistepositionDto.setTAendern(sqlTimestamp);

		if (theClientDto.getIStatus() != null) {
			stuecklistepositionDto.setAnsprechpartnerIIdAnlegen(theClientDto.getIStatus());
			stuecklistepositionDto.setAnsprechpartnerIIdAendern(theClientDto.getIStatus());
			stuecklistepositionDto.setTAnlegenAnsprechpartner(sqlTimestamp);
			stuecklistepositionDto.setTAendernAnsprechpartner(sqlTimestamp);
		}

		try {

			if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				// einen Handartikel anlegen
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(stuecklistepositionDto.getSHandeingabe());
				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(stuecklistepositionDto.getEinheitCNr());

				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

				stuecklistepositionDto.setArtikelIId(iIdArtikel);
			}

			Stuecklisteposition stuecklisteposition = new Stuecklisteposition(stuecklistepositionDto.getIId(),
					stuecklistepositionDto.getStuecklisteIId(), stuecklistepositionDto.getArtikelIId(),
					stuecklistepositionDto.getNMenge(), stuecklistepositionDto.getEinheitCNr(),
					stuecklistepositionDto.getMontageartIId(), stuecklistepositionDto.getISort(),
					stuecklistepositionDto.getBMitdrucken(), stuecklistepositionDto.getPersonalIIdAnlegen(),
					stuecklistepositionDto.getPersonalIIdAendern(), stuecklistepositionDto.getIBeginnterminoffset(),
					stuecklistepositionDto.getBRuestmenge(),stuecklistepositionDto.getBInitial(), stuecklistepositionDto.getXFormel());
			em.persist(stuecklisteposition);
			em.flush();
			setStuecklistepositionFromStuecklistepositionDto(stuecklisteposition, stuecklistepositionDto);

			stueckliste.setPersonalIIdAendernposition(theClientDto.getIDPersonal());
			stueckliste.setTAendernposition(getTimestamp());
			em.merge(stueckliste);
			em.flush();

			// PJ22049
			if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
				posersatzAngleichen(stuecklistepositionDto, theClientDto);

			}

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

		HvDtoLogger<StuecklistepositionDto> hvLogger = new HvDtoLogger<StuecklistepositionDto>(em,
				stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		hvLogger.logInsert(stuecklistepositionDto);

		return stuecklistepositionDto.getIId();
	}

	private void verifyStuecklistepositionFormel(Stueckliste stueckliste, StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) {
		if (getMandantFac().hatZusatzfunktionStuecklisteMitFormeln(theClientDto) && stueckliste.getBMitFormeln() > 0
				&& stuecklistepositionDto.getXFormel() != null) {
			StklparameterDto[] stklparamDtos = stklparameterFindByStuecklisteIId(stueckliste.getIId(), theClientDto);
			ScriptCompiler scc = new ScriptCompiler(stueckliste.getIId(), stklparamDtos);
			ScriptRunnerFLRStuecklisteposition scr = new ScriptRunnerFLRStuecklisteposition(scc);
			scr.compile(stuecklistepositionDto);
		}
	}

	private void verifyStuecklistearbeitsplanFormel(Stueckliste stueckliste, StuecklistearbeitsplanDto arbeitsplanDto,
			TheClientDto theClientDto) {
		if (getMandantFac().hatZusatzfunktionStuecklisteMitFormeln(theClientDto) && stueckliste.getBMitFormeln() > 0
				&& arbeitsplanDto.getXFormel() != null) {
			StklparameterDto[] stklparamDtos = stklparameterFindByStuecklisteIId(stueckliste.getIId(), theClientDto);
			ScriptCompiler scc = new ScriptCompiler(stueckliste.getIId(), stklparamDtos);
			ScriptRunnerFLRStuecklistearbeitsplan scr = new ScriptRunnerFLRStuecklistearbeitsplan(scc);
			scr.compile(arbeitsplanDto);
		}
	}

	private void posersatzAngleichen(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto) {
		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			// Nur wenn sich was geaendert hat

			PosersatzDto[] posersatzDtos = posersatzFindByStuecklistepositionIId(stuecklistepositionDto.getIId());

			int i0 = posersatzDtos.length;
			int i1 = 0;

			if (stuecklistepositionDto.getPosersatzDtos() != null
					&& stuecklistepositionDto.getPosersatzDtos().size() > 0) {
				i1 = stuecklistepositionDto.getPosersatzDtos().size();
			}

			boolean bGleich = true;

			if (i0 != i1) {
				bGleich = false;
			} else {

				for (int i = 0; i < posersatzDtos.length; i++) {
					if (!posersatzDtos[i].getArtikelIIdErsatz()
							.equals(stuecklistepositionDto.getPosersatzDtos().get(i).getArtikelIIdErsatz())) {
						bGleich = false;
					}
				}

			}

			if (bGleich == false) {
				// Zuerst alle loeschen
				for (int i = 0; i < posersatzDtos.length; i++) {
					Posersatz posersatz = em.find(Posersatz.class, posersatzDtos[i].getIId());
					em.remove(posersatz);
				}

				// und dann neu anlegen

				if (stuecklistepositionDto.getPosersatzDtos() != null) {
					for (int i = 0; i < stuecklistepositionDto.getPosersatzDtos().size(); i++) {

						PosersatzDto posersatzDto = new PosersatzDto();
						posersatzDto.setStuecklistepositionIId(stuecklistepositionDto.getIId());
						posersatzDto.setArtikelIIdErsatz(
								stuecklistepositionDto.getPosersatzDtos().get(i).getArtikelIIdErsatz());
						posersatzDto.setISort(stuecklistepositionDto.getPosersatzDtos().get(i).getISort());
						createPosersatz(posersatzDto, theClientDto);
					}
				}

			}

		}
	}

	public void removeStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getIId() == null"));
		}

		Stuecklisteposition toRemove = em.find(Stuecklisteposition.class, stuecklistepositionDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklistepositionDto> hvLogger = new HvDtoLogger<StuecklistepositionDto>(em,
				stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		hvLogger.logDelete(stuecklistepositionDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistepositionDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		stueckliste.setPersonalIIdAendernposition(theClientDto.getIDPersonal());
		stueckliste.setTAendernposition(new java.sql.Timestamp(System.currentTimeMillis()));
	}

	public void updateStuecklisteposition(StuecklistepositionDto stuecklistepositionDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getIId() == null || stuecklistepositionDto.getStuecklisteIId() == null
				|| stuecklistepositionDto.getPositionsartCNr() == null || stuecklistepositionDto.getNMenge() == null
				|| stuecklistepositionDto.getEinheitCNr() == null || stuecklistepositionDto.getMontageartIId() == null
				|| stuecklistepositionDto.getBMitdrucken() == null || stuecklistepositionDto.getISort() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklistepositionDto.getIId() == null || stuecklistepositionDto.getStuecklisteIId() == null || stuecklistepositionDto.getArtikelIId() == null || stuecklistepositionDto.getNMenge() == null ||s tuecklistepositionDto.getPositionsartCNr() == null || stuecklistepositionDto.getEinheitCNr() == null || stuecklistepositionDto.getMontageartIId() == null || stuecklistepositionDto.getBMitdrucken() == null || stuecklistepositionDto.getISort() == null"));
		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)
				&& stuecklistepositionDto.getSHandeingabe() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE) && stuecklistepositionDto.getSHandeingabe()  ==  null"));

		}
		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
				&& stuecklistepositionDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT) && stuecklistepositionDto.getArtikelIId() == null"));

		}

		// vergleicheStuecklistepositionDtoVorherNachherUndLoggeAenderungen(
		// stuecklistepositionDto, theClientDto);

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklistepositionDto.getStuecklisteIId());

		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stuecklistepositionDto.getArtikelIId(), theClientDto);

			// PJ 16622

			if (getMandantFac().hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				Artikel artikel = em.find(Artikel.class, stuecklistepositionDto.getArtikelIId());
				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

					// SP3353
					Artikel artikelStueckliste = em.find(Artikel.class, stueckliste.getArtikelIId());
					if (Helper.short2boolean(artikelStueckliste.getBSeriennrtragend())) {
						if (stuecklistepositionDto.getNMenge().doubleValue() != 1) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
									new Exception(
											"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

						}
					}

				}

			}

			if (stuecklisteDto != null
					&& stuecklisteDto.getStuecklisteartCNr().equals(StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
						new Exception("FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH"));

			}
			/*
			 * auskommentiert wg. PJ 16827 if (stuecklisteDto != null &&
			 * !stueckliste.getStuecklisteartCNr().equals(
			 * StuecklisteFac.STUECKLISTEART_STUECKLISTE)) {
			 * 
			 * throw new EJBExceptionLP( EJBExceptionLP.
			 * FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN
			 * , new Exception(
			 * "FEHLER_STUECKLISTENART_ARTIKELSET_BZW_HILFSSTUECKLISTE_DARF_KEINE_STUECKLISTE_ENTHALTEN"
			 * ));
			 * 
			 * }
			 */

		}

		// PRUEFEN, OB KEIN DEADLOCK:
		try {
			Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, stuecklistepositionDto.getArtikelIId());
			query.setParameter(2, theClientDto.getMandant());
			Integer stuecklisteIId = ((Stueckliste) query.getSingleResult()).getIId();
			if (pruefeObArtikelStuecklistenstrukturSchonVorhanden(stuecklisteIId,
					stuecklistepositionDto.getStuecklisteIId(), theClientDto)
					|| stuecklistepositionDto.getArtikelIId().equals(
							stuecklisteFindByPrimaryKey(stuecklistepositionDto.getStuecklisteIId(), theClientDto)
									.getArtikelIId())) {
				throw new EJBExceptionLP(EJBExceptionLP.STUECKLISTE_DEADLOCK, new Exception("DEADLOCK"));

			}
		} catch (NoResultException ex2) {
			// Wenn keine Stueckliste, dann egal
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		Integer iId = stuecklistepositionDto.getIId();
		Stuecklisteposition stuecklisteposition = em.find(Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			// einen Handartikel anlegen
			ArtikelDto oArtikelDto = new ArtikelDto();
			oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

			ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
			oArtikelsprDto.setCBez(stuecklistepositionDto.getSHandeingabe());
			oArtikelDto.setArtikelsprDto(oArtikelsprDto);
			oArtikelDto.setEinheitCNr(stuecklistepositionDto.getEinheitCNr());

			Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

			stuecklistepositionDto.setArtikelIId(iIdArtikel);
		}

		verifyStuecklistepositionFormel(stueckliste, stuecklistepositionDto, theClientDto);

		java.sql.Timestamp sqlTimestamp = getTimestamp();
		stuecklistepositionDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklistepositionDto.setTAendern(sqlTimestamp);
		if (theClientDto.getIStatus() != null) {
			stuecklistepositionDto.setAnsprechpartnerIIdAendern(theClientDto.getIStatus());
			stuecklistepositionDto.setTAendernAnsprechpartner(sqlTimestamp);
		}

		vergleicheStuecklistepositionDtoVorherNachherUndLoggeAenderungen(stuecklistepositionDto, theClientDto);
		setStuecklistepositionFromStuecklistepositionDto(stuecklisteposition, stuecklistepositionDto);

		stueckliste.setPersonalIIdAendernposition(theClientDto.getIDPersonal());
		stueckliste.setTAendernposition(sqlTimestamp);

		// PJ22049
		if (stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)) {
			posersatzAngleichen(stuecklistepositionDto, theClientDto);

		}

	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Stuecklisteposition stuecklisteposition = em.find(Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleAndLoad(stuecklisteposition, theClientDto);
		// StuecklistepositionDto stuecklistepositionDto =
		// assembleStuecklistepositionDto(stuecklisteposition);
		// Montageart montageart = em.find(Montageart.class,
		// stuecklistepositionDto.getMontageartIId());
		// if (montageart == null) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		// }
		// stuecklistepositionDto
		// .setMontageartDto(assembleMontageartDto(montageart));
		//
		// ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(
		// stuecklistepositionDto.getArtikelIId(), theClientDto);
		// stuecklistepositionDto.setArtikelDto(a);
		//
		// if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
		// stuecklistepositionDto
		// .setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		// } else {
		// stuecklistepositionDto
		// .setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		// }
		//
		// return stuecklistepositionDto;
	}

	private StuecklistepositionDto assembleAndLoad(Stuecklisteposition position, TheClientDto theClientDto) {
		StuecklistepositionDto stuecklistepositionDto = assembleStuecklistepositionDto(position);
		Montageart montageart = em.find(Montageart.class, stuecklistepositionDto.getMontageartIId());
		if (montageart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		stuecklistepositionDto.setMontageartDto(assembleMontageartDto(montageart));

		PosersatzDto[] posersatzDtos = getStuecklisteFac()
				.posersatzFindByStuecklistepositionIId(stuecklistepositionDto.getIId());
		ArrayList<PosersatzDto> alPosersatzDto = new ArrayList();
		for (int j = 0; j < posersatzDtos.length; j++) {
			alPosersatzDto.add(posersatzDtos[j]);
		}
		stuecklistepositionDto.setPosersatzDtos(alPosersatzDto);

		ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(stuecklistepositionDto.getArtikelIId(), theClientDto);
		stuecklistepositionDto.setArtikelDto(a);

		if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		} else {
			stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		}

		return stuecklistepositionDto;
	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKeyOhneExc(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			return null;
		}

		Stuecklisteposition stuecklisteposition = em.find(Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			return null;
		}
		StuecklistepositionDto stuecklistepositionDto = assembleStuecklistepositionDto(stuecklisteposition);
		Montageart montageart = em.find(Montageart.class, stuecklistepositionDto.getMontageartIId());
		if (montageart == null) {
			return null;
		}
		stuecklistepositionDto.setMontageartDto(assembleMontageartDto(montageart));

		ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(stuecklistepositionDto.getArtikelIId(), null);
		stuecklistepositionDto.setArtikelDto(a);

		if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		} else {
			stuecklistepositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		}

		return stuecklistepositionDto;
	}

	private Collection<Stuecklisteposition> listPositionenById(Integer stuecklisteId) {
		Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stuecklisteId);
		return query.getResultList();
	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(stuecklisteIId, "stuecklisteIId");
		Collection<Stuecklisteposition> cl = listPositionenById(stuecklisteIId);
		StuecklistepositionDto[] stuecklistepositionDto = assembleStuecklistepositionDtos(cl);
		return stuecklistepositionDto;
	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdAllData(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(stuecklisteIId, "stuecklisteIId");
		Validator.notNull(theClientDto, "theClientDto");

		Collection<Stuecklisteposition> cl = listPositionenById(stuecklisteIId);
		StuecklistepositionDto[] stuecklistepositionDto = assembleLoadStuecklistepositionDtos(cl, theClientDto);
		return stuecklistepositionDto;
	}

	public List<KundenStuecklistepositionDto> stuecklistepositionFindByStuecklisteIIdAllData(Integer stuecklisteIId,
			boolean withPrice, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.notNull(stuecklisteIId, "stuecklisteIId");
		Validator.notNull(theClientDto, "theClientDto");

		Collection<Stuecklisteposition> cl = listPositionenById(stuecklisteIId);
		return transformToKundenstuecklisteDto(cl, withPrice, theClientDto);
	}

	private List<KundenStuecklistepositionDto> transformToKundenstuecklisteDto(Collection<Stuecklisteposition> cl,
			boolean withPrice, TheClientDto theClientDto) throws RemoteException {
		KundeDto kundeDto = null;
		if (theClientDto.getIStatus() != null) {
			kundeDto = getKundeFac().kundeFindByAnsprechpartnerIdcNrMandantOhneExc(theClientDto.getIStatus(),
					theClientDto.getMandant(), theClientDto);
			if (kundeDto != null && !kundeDto.getMandantCNr().equals(theClientDto.getMandant())) {
				kundeDto = null;
			}
		}

		java.sql.Date datePrice = new Date(System.currentTimeMillis());
		// java.sql.Date datePrice = null ;
		java.sql.Date stuecklisteDate = null;
		List<KundenStuecklistepositionDto> stuecklistePositionDtos = new ArrayList<KundenStuecklistepositionDto>();
		for (Stuecklisteposition position : cl) {
			StuecklistepositionDto dto = assembleAndLoad(position, theClientDto);
			// if(dto.getArtikelDto().getBVersteckt() > 0) continue ;

			KundenStuecklistepositionDto kundenPosDto = new KundenStuecklistepositionDto(dto);
			stuecklistePositionDtos.add(kundenPosDto);

			if (kundeDto == null)
				continue;

			ArtikelsperrenDto[] sperrenDtos = getArtikelFac().artikelsperrenFindByArtikelIId(dto.getArtikelIId());
			kundenPosDto.setArtikelGesperrt(sperrenDtos != null && sperrenDtos.length > 0);

			KundesokoDto sokoDto = getKundesokoFac().kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
					kundeDto.getIId(), dto.getArtikelDto().getIId(), datePrice);
			// new Date(dto.getTAnlegen().getTime()));
			if (sokoDto != null) {
				kundenPosDto.setKundenartikelNummer(sokoDto.getCKundeartikelnummer());
			}

			if (withPrice == false)
				continue;

			Integer mwstsatzbezId = dto.getArtikelDto().getMwstsatzbezIId();
			if (mwstsatzbezId == null) {
				mwstsatzbezId = kundeDto.getMwstsatzbezIId();
			}
			// TODO Umrechnen in Lagereinheit
			VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(
					dto.getArtikelDto().getIId(), kundeDto.getIId(), dto.getNMenge(), datePrice,
					kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), mwstsatzbezId, kundeDto.getCWaehrung(),
					theClientDto);

			BigDecimal p = getPriceFromPreisfindung(vkpreisfindungDto);
			kundenPosDto.setVkPreis(p);
		}

		return stuecklistePositionDtos;
	}

	private BigDecimal getMinimumPrice(BigDecimal minimum, VerkaufspreisDto priceDto) {
		if (priceDto != null && priceDto.nettopreis != null) {
			return null == minimum ? priceDto.nettopreis : minimum.min(priceDto.nettopreis);
		}

		return minimum;
	}

	private BigDecimal getPriceFromPreisfindung(VkpreisfindungDto vkPreisDto) {
		BigDecimal p = getMinimumPrice(null, vkPreisDto.getVkpStufe3());
		if (p != null)
			return p;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe2());
		if (p != null)
			return p;

		p = getMinimumPrice(null, vkPreisDto.getVkpStufe1());
		if (p != null)
			return p;

		p = getMinimumPrice(null, vkPreisDto.getVkpPreisbasis());
		if (p != null)
			return p;

		return BigDecimal.ZERO;
	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdBMitdrucken(Integer stuecklisteIId,
			Short bMitdrucken, TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null || bMitdrucken == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null || bMitdrucken==null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIIdBMitdrucken");
		query.setParameter(1, stuecklisteIId);
		query.setParameter(2, bMitdrucken);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		StuecklistepositionDto[] stuecklistepositionDto = assembleStuecklistepositionDtos(cl);
		return stuecklistepositionDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdArtikelIId(Integer stuecklisteIId,
			Integer artikelIId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIIdArtikelIId");
		query.setParameter(1, stuecklisteIId);
		query.setParameter(2, artikelIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		StuecklistepositionDto[] stuecklistepositionDto = assembleStuecklistepositionDtos(cl);

		return stuecklistepositionDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }

	}

	public StuecklistepositionDto[] stuecklistepositionFindByArtikelIId(Integer artikelIId, TheClientDto theClientDto) {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistepositionfindByArtikelIId");
		query.setParameter(1, artikelIId);
		Collection<?> cl = query.getResultList();

		StuecklistepositionDto[] stuecklistepositionDto = assembleStuecklistepositionDtos(cl);

		return stuecklistepositionDto;

	}

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// null);
		// }
		StuecklistearbeitsplanDto[] stuecklistepositionDto = assembleStuecklistearbeitsplanDtos(cl);
		return stuecklistepositionDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND,
		// e);
		// }
	}

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByArtikelIId(Integer stuecklisteIId,
			TheClientDto theClientDto) {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistearbeitsplanfindByArtikelIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		StuecklistearbeitsplanDto[] stuecklistepositionDto = assembleStuecklistearbeitsplanDtos(cl);
		return stuecklistepositionDto;
	}

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByMaschineIId(Integer stuecklisteIId,
			TheClientDto theClientDto) {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em.createNamedQuery("StuecklistearbeitsplanfindByMaschineIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		StuecklistearbeitsplanDto[] stuecklistepositionDto = assembleStuecklistearbeitsplanDtos(cl);
		return stuecklistepositionDto;
	}

	private void setStuecklistepositionFromStuecklistepositionDto(Stuecklisteposition stuecklisteposition,
			StuecklistepositionDto stuecklistepositionDto) {
		stuecklisteposition.setStuecklisteIId(stuecklistepositionDto.getStuecklisteIId());
		stuecklisteposition.setArtikelIId(stuecklistepositionDto.getArtikelIId());
		stuecklisteposition.setNMenge(stuecklistepositionDto.getNMenge());
		stuecklisteposition.setNKalkpreis(stuecklistepositionDto.getNKalkpreis());
		stuecklisteposition.setEinheitCNr(stuecklistepositionDto.getEinheitCNr());
		stuecklisteposition.setFDimension1(stuecklistepositionDto.getFDimension1());
		stuecklisteposition.setFDimension2(stuecklistepositionDto.getFDimension2());
		stuecklisteposition.setFDimension3(stuecklistepositionDto.getFDimension3());
		stuecklisteposition.setCPosition(stuecklistepositionDto.getCPosition());
		stuecklisteposition.setCKommentar(stuecklistepositionDto.getCKommentar());
		stuecklisteposition.setMontageartIId(stuecklistepositionDto.getMontageartIId());
		stuecklisteposition.setILfdnummer(stuecklistepositionDto.getILfdnummer());
		stuecklisteposition.setISort(stuecklistepositionDto.getISort());
		stuecklisteposition.setBMitdrucken(stuecklistepositionDto.getBMitdrucken());
		stuecklisteposition.setPersonalIIdAendern(stuecklistepositionDto.getPersonalIIdAendern());
		stuecklisteposition.setPersonalIIdAnlegen(stuecklistepositionDto.getPersonalIIdAnlegen());
		stuecklisteposition.setTAendern(stuecklistepositionDto.getTAendern());
		stuecklisteposition.setTAnlegen(stuecklistepositionDto.getTAnlegen());
		stuecklisteposition.setIBeginnterminoffset(stuecklistepositionDto.getIBeginnterminoffset());
		stuecklisteposition.setTAnlegenAnsprechpartner(stuecklistepositionDto.getTAnlegenAnsprechpartner());
		stuecklisteposition.setTAendernAnsprechpartner(stuecklistepositionDto.getTAendernAnsprechpartner());
		stuecklisteposition.setAnsprechpartnerIIdAendern(stuecklistepositionDto.getAnsprechpartnerIIdAendern());
		stuecklisteposition.setAnsprechpartnerIIdAnlegen(stuecklistepositionDto.getAnsprechpartnerIIdAnlegen());
		stuecklisteposition.setBRuestmenge(stuecklistepositionDto.getBRuestmenge());
		stuecklisteposition.setXFormel(stuecklistepositionDto.getXFormel());
		stuecklisteposition.setBInitial(stuecklistepositionDto.getBInitial());
		em.merge(stuecklisteposition);
		em.flush();
	}

	private StuecklistepositionDto assembleStuecklistepositionDto(Stuecklisteposition stuecklisteposition) {
		return StuecklistepositionDtoAssembler.createDto(stuecklisteposition);
	}

	private StuecklistepositionDto[] assembleStuecklistepositionDtos(Collection<?> stuecklistepositions) {
		List<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
		if (stuecklistepositions != null) {
			Iterator<?> iterator = stuecklistepositions.iterator();
			while (iterator.hasNext()) {
				Stuecklisteposition stuecklisteposition = (Stuecklisteposition) iterator.next();
				list.add(assembleStuecklistepositionDto(stuecklisteposition));
			}
		}
		StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list.size()];
		return (StuecklistepositionDto[]) list.toArray(returnArray);
	}

	private StuecklistepositionDto[] assembleLoadStuecklistepositionDtos(Collection<?> stuecklistepositions,
			TheClientDto theClientDto) {
		List<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
		if (stuecklistepositions != null) {
			Iterator<?> iterator = stuecklistepositions.iterator();
			while (iterator.hasNext()) {
				Stuecklisteposition stuecklisteposition = (Stuecklisteposition) iterator.next();
				list.add(assembleAndLoad(stuecklisteposition, theClientDto));
			}
		}
		StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list.size()];
		return (StuecklistepositionDto[]) list.toArray(returnArray);
	}

	// PJ21211
	private BigDecimal berechneMengeVerschnittNachAbmessungFLR(BigDecimal nMenge, BigDecimal nLosgroesse,
			EinheitDto einheit, FLRStuecklisteposition stklPosition, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		FLRArtikelliste artikel = stklPosition.getFlrartikel();

		FLRGeometrie geometrie = artikel.getFlrgeometrie();

		if (geometrie == null) {
			return nMenge;
		}

		Double dimension1 = stklPosition.getF_dimension1();
		Double dimension2 = stklPosition.getF_dimension2();
		if (einheit.getIDimension() == 1 && geometrie.getF_breite() != null && geometrie.getF_breite().doubleValue() > 0
				&& dimension1 != null) {

			BigDecimal breiteInArtikeleinheit = getSystemFac().rechneUmInAndereEinheit(
					new BigDecimal(geometrie.getF_breite()), SystemFac.EINHEIT_MILLIMETER, artikel.getEinheit_c_nr(),
					stklPosition.getI_id(), theClientDto);
			BigDecimal mengeDim1Umgerechnet = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(dimension1),
					einheit.getCNr(), artikel.getEinheit_c_nr(), stklPosition.getI_id(), theClientDto);
			return Helper.berechneBenoetigteMenge1D(mengeDim1Umgerechnet, breiteInArtikeleinheit, nMenge, nLosgroesse);
		} else if (einheit.getIDimension() == 2 && geometrie.getF_breite() != null
				&& geometrie.getF_breite().doubleValue() > 0 && geometrie.getF_hoehe() != null
				&& geometrie.getF_hoehe().doubleValue() > 0 && dimension1 != null && dimension2 != null
				&& (einheit.getCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
						|| einheit.getCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER)
								&& (artikel.getEinheit_c_nr().equals(SystemFac.EINHEIT_QUADRATMETER)
										|| artikel.getEinheit_c_nr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER)))) {

			BigDecimal breiteInMillimeter = new BigDecimal(dimension1);
			BigDecimal hoeheInMillimeter = new BigDecimal(dimension2);
			if (einheit.getCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				breiteInMillimeter = getSystemFac().rechneUmInAndereEinheit(breiteInMillimeter, SystemFac.EINHEIT_METER,
						SystemFac.EINHEIT_MILLIMETER, stklPosition.getI_id(), theClientDto);
				hoeheInMillimeter = getSystemFac().rechneUmInAndereEinheit(hoeheInMillimeter, SystemFac.EINHEIT_METER,
						SystemFac.EINHEIT_MILLIMETER, stklPosition.getI_id(), theClientDto);
			}

			BigDecimal artikelBreiteMillimeter = new BigDecimal(geometrie.getF_breite());
			BigDecimal artikelHoeheMillimeter = new BigDecimal(geometrie.getF_hoehe());

			BigDecimal menge = Helper.berechneBenoetigteMenge2D(hoeheInMillimeter, breiteInMillimeter,
					artikelHoeheMillimeter, artikelBreiteMillimeter, nMenge, nLosgroesse);

			if (artikel.getEinheit_c_nr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				menge = getSystemFac().rechneUmInAndereEinheit(menge, SystemFac.EINHEIT_QUADRATMILLIMETER,
						SystemFac.EINHEIT_QUADRATMETER, stklPosition.getI_id(), theClientDto);
			}
			return menge;
		} else {
			myLogger.logKritisch("Stueckliste " + stklPosition.getFlrstueckliste().getFlrartikel().getC_nr()
					+ " +Material-Artikel " + artikel.getC_nr() + " Umrechnung nach Zielmenge nicht moeglich");

			BigDecimal bdMenge = nMenge;

			bdMenge.multiply(berechneDimensionGroesse(einheit.getIDimension(), stklPosition.getF_dimension1(),
					stklPosition.getF_dimension2(), stklPosition.getF_dimension3()));

			bdMenge = getSystemFac().rechneUmInAndereEinheit(bdMenge, artikel.getEinheit_c_nr(), einheit.getCNr(),
					stklPosition.getI_id(), theClientDto);

			return bdMenge;
		}

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStuecklisteMitArbeitsplan(Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUeberAlleMandanten, boolean bFremdfertigungAufloesen, boolean minBSMengeUndVPEBeruecksichtigen,
			String mandantCNrStueckliste) {

		if (nSatzgroesse == null) {
			nSatzgroesse = new BigDecimal(1);
		}

		if (strukturMap == null) {
			strukturMap = new ArrayList<StuecklisteMitStrukturDto>();
		}
		if (iEbene > 50) {
			throw new EJBExceptionLP(new Exception("STUECKLISTEN DEADLOCK"));
		}

		MandantDto mandantDtoFuerMandantFeld = null;

		try {
			if (mandantCNrStueckliste == null) {
				mandantDtoFuerMandantFeld = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);
			} else {
				mandantDtoFuerMandantFeld = getMandantFac().mandantFindByPrimaryKey(mandantCNrStueckliste,
						theClientDto);
			}
		} catch (RemoteException e1) {
			throwEJBExceptionLPRespectOld(e1);
		}

		// Alle Positionen einer Stueckliste holen, sortiert nach Artikelart und
		// nach der angegebenen Option
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.add(Example.create(flrStuecklisteposition))
				.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a");
		if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
			crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
			crit.addOrder(Order.asc("a.c_nr"));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {
			crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE) {
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));
		}

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results.size()];

		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		// SP3218 Zuerst Zielmengen berechnen

		for (int i = 0; i < returnArray.length; i++) {

			try {

				// Menge nach Zieleinheit umrechnen

				BigDecimal bdMenge = returnArray[i].getN_menge();

				// Wenn Ruestmenge, dann ist Satzgroesse immer 1
				if (!Helper.short2boolean(returnArray[i].getB_ruestmenge())) {
					bdMenge = bdMenge.multiply(nSatzgroesse);
				}

				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
						theClientDto);

				// Positionsmenge nach Zielenge umrechnen
				int dimension = einheitDto.getIDimension().intValue();

				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG);

				boolean bVerschnittNachAbmessung = (Boolean) parameter.getCWertAsObject();

				// PJ21211
				if (bVerschnittNachAbmessung && (dimension == 1 || dimension == 2)) {

					bdMenge = berechneMengeVerschnittNachAbmessungFLR(bdMenge, nLosgroesse, einheitDto, returnArray[i],
							theClientDto);

					returnArray[i].bdZielmenge = bdMenge;
				} else {

					bdMenge = bdMenge.multiply(berechneDimensionGroesse(dimension, returnArray[i].getF_dimension1(),
							returnArray[i].getF_dimension2(), returnArray[i].getF_dimension3()));

					BigDecimal faktor = null;
					String artikelnummerStueckliste = returnArray[i].getFlrstueckliste().getFlrartikel().getC_nr();
					try {
						faktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
								returnArray[i].getFlrartikel().getEinheit_c_nr(), returnArray[i].getEinheit_c_nr(),
								returnArray[i].getI_id(), theClientDto);
					} catch (EJBExceptionLP ex2) {
						// Projekte 3509/10048/9918
						String positionsArtikel = returnArray[i].getFlrartikel().getC_nr();
						String position = "";
						if (returnArray[i].getI_sort() != null) {
							position = returnArray[i].getI_sort() + "";
						}
						List<Object> info = ex2.getAlInfoForTheClient();
						// Nachricht zusammenbauen
						String meldung = "Zu finden in Ebene " + (iEbene + 1) + ", St\u00FCckliste: "
								+ artikelnummerStueckliste + "\r\n";
						meldung += "Position " + position + "-> Artikelnummer: " + positionsArtikel;
						info.set(0, meldung);
						ex2.setAlInfoForTheClient(info);
						throwEJBExceptionLPRespectOld(new RemoteException("", ex2));
					}
					if (faktor.compareTo(BigDecimal.ZERO) != 0) {
						bdMenge = bdMenge.divide(faktor, 12, BigDecimal.ROUND_HALF_EVEN);

						bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
								returnArray[i].getFlrartikel().getF_verschnittfaktor(),
								returnArray[i].getFlrartikel().getF_verschnittbasis(), nLosgroesse,
								returnArray[i].getFlrartikel().getF_fertigungs_vpe());
						// PJ 14352
						if (Helper.short2boolean(returnArray[i].getB_ruestmenge()) == false
								&& returnArray[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue() != 0) {
							// SP4930 Nur Durch Erfassungsfaktor dividieren, wenn
							// keine Ruestmenge
							bdMenge = bdMenge.divide(
									new BigDecimal(
											returnArray[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue()),
									12, BigDecimal.ROUND_HALF_EVEN);
						}

						returnArray[i].bdZielmenge = bdMenge;

					} else {
						ArrayList<Object> al = new ArrayList<Object>();

						EinheitDto von = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
								theClientDto);
						EinheitDto zu = getSystemFac().einheitFindByPrimaryKey(
								returnArray[i].getFlrartikel().getEinheit_c_nr(), theClientDto);

						al.add(von.formatBez() + " <-> " + zu.formatBez());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT, al,
								new Exception("FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
					}
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		// NEU Projekt 9730
		ArrayList<FLRStuecklisteposition> alKomp = new ArrayList();
		if (bGleichePositionenZusammenfassen == true) {
			// Positionen zusammenfassen, wenn der Artikel und die
			// Positionseinheit gleich sind
			// Nur wenns kein Arbeitsplan ist und in derselben Ebene
			for (int i = 0; i < returnArray.length; i++) {
				boolean bGefunden = false;

				for (int j = 0; j < alKomp.size(); j++) {
					FLRStuecklisteposition temp = (FLRStuecklisteposition) alKomp.get(j);

					if (temp.getFlrartikel().getI_id().equals(returnArray[i].getFlrartikel().getI_id())
							&& temp.getEinheit_c_nr().equals(returnArray[i].getEinheit_c_nr())
							&& temp.getB_ruestmenge().equals(returnArray[i].getB_ruestmenge())) {
						temp.setN_menge(temp.getN_menge().add(returnArray[i].getN_menge()));
						temp.bdZielmenge = temp.bdZielmenge.add(returnArray[i].bdZielmenge);
						alKomp.set(j, temp);

						bGefunden = true;
					}

				}
				if (!bGefunden) {
					alKomp.add(returnArray[i]);
				}

			}
			returnArray = new FLRStuecklisteposition[alKomp.size()];
			returnArray = (FLRStuecklisteposition[]) alKomp.toArray(returnArray);
		}
		// -ENDE NEU

		// --ADD ARBEITSPLAN

		// Arbeitsplan
		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteIId);

		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit2 = session2.createCriteria(FLRStuecklistearbeitsplan.class)
				.add(Example.create(flrStuecklistearbeitsplan))
				.createAlias(StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL, "a");
		crit2.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));

		crit2.addOrder(Order.asc("i_arbeitsgang"));
		crit2.addOrder(Order.asc("i_unterarbeitsgang"));

		List<?> results2 = crit2.list();

		FLRStuecklistearbeitsplan[] arbeitsplan = new FLRStuecklistearbeitsplan[results2.size()];
		arbeitsplan = (FLRStuecklistearbeitsplan[]) results2.toArray(arbeitsplan);
		for (int i = 0; i < arbeitsplan.length; i++) {

			Long stueckzeit = new Long(
					(long) (arbeitsplan[i].getL_stueckzeit().longValue() * nSatzgroesse.doubleValue()));

			if (arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue() != 0) {
				stueckzeit = (long) (stueckzeit

						/ arbeitsplan[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue());
			}

			int iBasisKalkulation = 0;
			try {
				ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION);
				iBasisKalkulation = (Integer) parameterDto.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Long ruestzeit = arbeitsplan[i].getL_ruestzeit();

			// SP6514
			double posmenge = nSatzgroesse.multiply(nLosgroesse).doubleValue();

			if (posmenge == 0) {
				ruestzeit = 0L;
			}

			if (iBasisKalkulation == 1
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse() != null
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse().doubleValue() > 0
					&& iEbene > 0) {

				double fsgmenge = arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse()
						.doubleValue();

				while (fsgmenge < posmenge) {

					fsgmenge += arbeitsplan[i].getFlrstueckliste().getFlrartikel().getF_fertigungssatzgroesse()
							.doubleValue();
				}

				ruestzeit = (long) ((ruestzeit / fsgmenge) * nSatzgroesse.doubleValue() * nLosgroesse.doubleValue());

			}

			if (arbeitsplan[i].getFlrmaschine() == null
					|| Helper.short2boolean(arbeitsplan[i].getB_nurmaschinenzeit()) == false) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanDto.setIId(arbeitsplan[i].getI_id());
				stuecklistearbeitsplanDto.setArtikelIId(arbeitsplan[i].getFlrartikel().getI_id());
				stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				stuecklistearbeitsplanDto.setLRuestzeit(ruestzeit);
				stuecklistearbeitsplanDto.setBNurmaschinenzeit(arbeitsplan[i].getB_nurmaschinenzeit());
				stuecklistearbeitsplanDto.setBInitial(arbeitsplan[i].getB_initial());
				stuecklistearbeitsplanDto.setIAufspannung(arbeitsplan[i].getI_aufspannung());
				stuecklistearbeitsplanDto.setXFormel(arbeitsplan[i].getX_formel());

				stuecklistearbeitsplanDto.setLStueckzeit(stueckzeit);
				StuecklisteMitStrukturDto stuecklisteMitStrukturDto = new StuecklisteMitStrukturDto(iEbene, null);
				stuecklisteMitStrukturDto.setStuecklistearbeitsplanDto(stuecklistearbeitsplanDto);
				stuecklisteMitStrukturDto.setBArbeitszeit(true);

				stuecklistearbeitsplanDto.setIArbeitsgang(arbeitsplan[i].getI_arbeitsgang());
				stuecklistearbeitsplanDto.setIUnterarbeitsgang(arbeitsplan[i].getI_unterarbeitsgang());
				stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i].getC_kommentar());

				stuecklisteMitStrukturDto.setTFreigabe(arbeitsplan[i].getFlrstueckliste().getT_freigabe());

				stuecklisteMitStrukturDto.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
				stuecklisteMitStrukturDto.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());

				if (arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
					stuecklisteMitStrukturDto.setCKurzzeichenPersonFreigabe(
							arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
				}

				strukturMap.add(stuecklisteMitStrukturDto);
			}
			// Wenn Maschine, dann zusaetzlichen Eintrag erstellen:
			if (arbeitsplan[i].getFlrmaschine() != null) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanMaschineDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanMaschineDto.setIId(arbeitsplan[i].getI_id());
				stuecklistearbeitsplanMaschineDto.setArtikelIId(arbeitsplan[i].getFlrartikel().getI_id());
				stuecklistearbeitsplanMaschineDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				stuecklistearbeitsplanMaschineDto.setLRuestzeit(ruestzeit);
				stuecklistearbeitsplanMaschineDto.setLStueckzeit(stueckzeit);
				stuecklistearbeitsplanMaschineDto.setMaschineIId(arbeitsplan[i].getMaschine_i_id());
				stuecklistearbeitsplanMaschineDto.setBNurmaschinenzeit(arbeitsplan[i].getB_nurmaschinenzeit());
				stuecklistearbeitsplanMaschineDto.setBInitial(arbeitsplan[i].getB_initial());
				stuecklistearbeitsplanMaschineDto.setIAufspannung(arbeitsplan[i].getI_aufspannung());

				stuecklistearbeitsplanMaschineDto.setXFormel(arbeitsplan[i].getX_formel());

				stuecklistearbeitsplanMaschineDto.setIArbeitsgang(arbeitsplan[i].getI_arbeitsgang());
				stuecklistearbeitsplanMaschineDto.setIUnterarbeitsgang(arbeitsplan[i].getI_unterarbeitsgang());
				stuecklistearbeitsplanMaschineDto.setCKommentar(arbeitsplan[i].getC_kommentar());
				StuecklisteMitStrukturDto stuecklisteMitStrukturMaschineDto = new StuecklisteMitStrukturDto(iEbene,
						null);
				stuecklisteMitStrukturMaschineDto.setStuecklistearbeitsplanDto(stuecklistearbeitsplanMaschineDto);
				stuecklisteMitStrukturMaschineDto.setBArbeitszeit(true);
				stuecklisteMitStrukturMaschineDto.setBMaschinenzeit(true);

				stuecklisteMitStrukturMaschineDto.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
				stuecklisteMitStrukturMaschineDto.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());

				stuecklisteMitStrukturMaschineDto.setTFreigabe(arbeitsplan[i].getFlrstueckliste().getT_freigabe());
				if (arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
					stuecklisteMitStrukturMaschineDto.setCKurzzeichenPersonFreigabe(
							arbeitsplan[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
				}

				strukturMap.add(stuecklisteMitStrukturMaschineDto);
			}

		}

		// -- END ADD ARBEITSPLAN

		for (int i = 0; i < returnArray.length; i++) {
			StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
			stuecklistepositionDto.setArtikelIId(returnArray[i].getFlrartikel().getI_id());

			stuecklistepositionDto
					.setBdVerschnittmengeDesArtikels(returnArray[i].getFlrartikel().getN_verschnittmenge());

			stuecklistepositionDto.setCKommentar(returnArray[i].getC_kommentar());
			stuecklistepositionDto.setCPosition(returnArray[i].getC_position());
			stuecklistepositionDto.setEinheitCNr(returnArray[i].getEinheit_c_nr());
			stuecklistepositionDto.setNMenge(returnArray[i].getN_menge());
			stuecklistepositionDto.setBMitdrucken(returnArray[i].getB_mitdrucken());
			

			stuecklistepositionDto.setNZielmenge(returnArray[i].bdZielmenge);

			stuecklistepositionDto.setNKalkpreis(returnArray[i].getN_kalkpreis());
			stuecklistepositionDto.setILfdnummer(returnArray[i].getI_lfdnummer());
			stuecklistepositionDto.setIId(returnArray[i].getI_id());
			stuecklistepositionDto.setCKommentar(returnArray[i].getC_kommentar());
			stuecklistepositionDto.setStuecklisteIId(returnArray[i].getStueckliste_i_id());

			stuecklistepositionDto.setBRuestmenge(returnArray[i].getB_ruestmenge());
			stuecklistepositionDto.setBInitial(returnArray[i].getB_initial());
			stuecklistepositionDto.setXFormel(returnArray[i].getX_formel());

			stuecklistepositionDto.setHierarchiemenge_NOT_IN_DB(nSatzgroesse);

			stuecklistepositionDto.setfLagermindeststandAusKopfartikel(
					returnArray[i].getFlrstueckliste().getFlrartikel().getF_lagermindest());

			MontageartDto ma = new MontageartDto();
			ma.setIId(returnArray[i].getFlrmontageart().getI_id());
			ma.setCBez(returnArray[i].getFlrmontageart().getC_bez());
			stuecklistepositionDto.setMontageartDto(ma);
			if (returnArray[i].getF_dimension1() != null) {
				stuecklistepositionDto.setFDimension1(new Float(returnArray[i].getF_dimension1().doubleValue()));
			}
			if (returnArray[i].getF_dimension2() != null) {
				stuecklistepositionDto.setFDimension2(new Float(returnArray[i].getF_dimension2().doubleValue()));
			}
			if (returnArray[i].getF_dimension3() != null) {
				stuecklistepositionDto.setFDimension3(new Float(returnArray[i].getF_dimension3().doubleValue()));
			}

			// PJ21296 Als allererstes minBs und VPE aus Lief1 beruecksichtigen
			if (minBSMengeUndVPEBeruecksichtigen && returnArray[i].getFlrartikel() != null) {
				try {
					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(
							returnArray[i].getFlrartikel().getI_id(), returnArray[i].bdZielmenge,
							theClientDto.getSMandantenwaehrung(), theClientDto);
					if (alDto != null) {

						if (alDto.getFMindestbestelmenge() != null) {
							stuecklistepositionDto
									.setBdMindestbestellmenge_NOT_IN_DB(new BigDecimal(alDto.getFMindestbestelmenge()));
						}

						stuecklistepositionDto.setBdVPE_NOT_IN_DB(alDto.getNVerpackungseinheit());

					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			StuecklisteMitStrukturDto zeile = new StuecklisteMitStrukturDto(iEbene, stuecklistepositionDto);

			zeile.setTFreigabe(returnArray[i].getFlrstueckliste().getT_freigabe());
			if (returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
				zeile.setCKurzzeichenPersonFreigabe(
						returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
			}

			if (bMitUnterstuecklisten == true) {

				MandantDto[] mandantenDtos = null;

				boolean bIstBeiKeinemMandantenEineStueckliste = true;
				try {
					if (bUeberAlleMandanten) {
						mandantenDtos = getMandantFac().mandantFindAll(theClientDto);
					} else {
						mandantenDtos = new MandantDto[] {
								getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto) };
					}
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				for (int k = 0; k < mandantenDtos.length; k++) {
					zeile.setMandantCNr(mandantenDtos[k].getCNr());
					zeile.setMandantCKbez(mandantenDtos[k].getCKbez());

					try {
						Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
						query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
						query.setParameter(2, mandantenDtos[k].getCNr());
						Stueckliste stkl = (Stueckliste) query.getSingleResult();
						if (stkl != null) {
							bIstBeiKeinemMandantenEineStueckliste = false;

							zeile.setBIstFremdfertigung(Helper.short2boolean(stkl.getBFremdfertigung()));

							strukturMap.add(zeile.clone());
							// wenn Fremdfertigungsstueckliste, dann keine
							// Aufloesung (PJ21238)
							if (Helper.short2boolean(stkl.getBFremdfertigung()) == false
									|| bFremdfertigungAufloesen == true) {

								// SP6514
								if (stuecklistepositionDto.getNZielmenge().doubleValue() == 0) {
									boolean bZielmengeDarf0Sein = false;
									try {
										ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
												theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
												ParameterFac.PARAMETER_ZIELMENGE_GLEICH_0_ZULAESSIG);
										bZielmengeDarf0Sein = (Boolean) parameterDto.getCWertAsObject();
									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);
									}

									if (bZielmengeDarf0Sein == false) {
										ArrayList al = new ArrayList();
										al.add(returnArray[i].getFlrartikel().getC_nr());

										al.add(returnArray[i].getFlrstueckliste().getFlrartikel().getC_nr());

										stuecklistepositionDto.getArtikelDto();
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL,
												al, new Exception(
														"FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL"));
									}
								}

								query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
								query.setParameter(1, stkl.getIId());
								int iAnzahlPositionenArbeitsplan = query.getResultList().size();
								// Wenn keine Positionen und kein Arbeitsplan,
								// dann
								// auch keinen Aufloesung

								// Aufgrund von PJ20715 auskommentiert

								// if (iAnzahlPositionen
								// + iAnzahlPositionenArbeitsplan > 0) {
								StuecklisteMitStrukturDto strukturTemp = (StuecklisteMitStrukturDto) strukturMap
										.get(strukturMap.size() - 1);
								strukturTemp.setBStueckliste(true);
								strukturTemp.setIAnzahlArbeitsschritte(iAnzahlPositionenArbeitsplan);
								strukturTemp.setStuecklisteDto(assembleStuecklisteDto(stkl));
								strukturTemp.setDurchlaufzeit(stkl.getNDefaultdurchlaufzeit());
								strukturMap.set(strukturMap.size() - 1, strukturTemp);

								strukturMap = getStrukturDatenEinerStuecklisteMitArbeitsplan(stkl.getIId(),
										theClientDto, iOptionSortierung, iEbene + 1, strukturMap, bMitUnterstuecklisten,
										bGleichePositionenZusammenfassen, nLosgroesse,
										stuecklistepositionDto.getNZielmenge(), bUeberAlleMandanten,
										bFremdfertigungAufloesen, minBSMengeUndVPEBeruecksichtigen,
										stkl.getMandantCNr()); // .multiply(
								// nSatzgroesse
								// )); ->
								// Ausgebaut
								// wegen
								// Naemo-
								// Stueckliste
								// FC002
								// }
							}
							break;
						}
					} catch (NoResultException ex) {
						// Dann keine Stueckliste

						if (bUeberAlleMandanten == true) {

							if (iEbene == 0) {
								// NIX
							} else if (iEbene > 0) {
								if (mandantCNrStueckliste == null
										|| mandantCNrStueckliste.equals(mandantenDtos[k].getCNr())) {
									strukturMap.add(zeile.clone());
								}
							}

						} else {
							strukturMap.add(zeile.clone());
						}

					} catch (NonUniqueResultException ex1) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
					}
				}

				// SP4523
				if (bUeberAlleMandanten == true && iEbene == 0 && bIstBeiKeinemMandantenEineStueckliste == true) {
					zeile.setMandantCNr(mandantDtoFuerMandantFeld.getCNr());
					zeile.setMandantCKbez(mandantDtoFuerMandantFeld.getCKbez());
					strukturMap.add(zeile.clone());
				}

			} else {

				strukturMap.add(zeile);

			}
		}

		session.close();

		return strukturMap;

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStueckliste(Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen) {
		return getStrukturDatenEinerStueckliste(new Integer[] { stuecklisteIId }, theClientDto, iOptionSortierung,
				iEbene, strukturMap, bMitUnterstuecklisten, bGleichePositionenZusammenfassen, nLosgroesse, nSatzgroesse,
				bUnterstklstrukurBelassen, false);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStueckliste(Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen, boolean fremdfertigungAufloesen) {
		return getStrukturDatenEinerStueckliste(new Integer[] { stuecklisteIId }, theClientDto, iOptionSortierung,
				iEbene, strukturMap, bMitUnterstuecklisten, bGleichePositionenZusammenfassen, nLosgroesse, nSatzgroesse,
				bUnterstklstrukurBelassen, fremdfertigungAufloesen);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStueckliste(Integer[] stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse, BigDecimal nSatzgroesse,
			boolean bUnterstklstrukurBelassen, boolean fremdfertigungAufloesen) {

		if (nSatzgroesse == null) {
			nSatzgroesse = new BigDecimal(1);
		}
		if (strukturMap == null) {
			strukturMap = new ArrayList<StuecklisteMitStrukturDto>();
		}
		if (iEbene > 50) {
			throw new EJBExceptionLP(new Exception("STUECKLISTEN DEADLOCK"));
		}

		// Alle Positionen einer Stueckliste holen, sortiert nach Artikelart und
		// nach der angegebenen Option

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(FLRStuecklisteposition.class)
				.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a");
		crit.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE, "s");

		crit.add(Restrictions.in("s.i_id", stuecklisteIId));

		if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
			crit.addOrder(Order.asc("a.c_nr"));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION));
		} else {
			crit.addOrder(Order.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));
		}

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		// SP3218 Zuerst alle Zielmengen berechnen
		for (int i = 0; i < returnArray.length; i++) {

			try {

				// Menge nach Zieleinheit umrechnen
				BigDecimal bdMenge = returnArray[i].getN_menge().multiply(nSatzgroesse);

				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
						theClientDto);

				// Positionsmenge nach Zielenge umrechnen
				int dimension = einheitDto.getIDimension().intValue();

				ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG);

				boolean bVerschnittNachAbmessung = (Boolean) parameter.getCWertAsObject();

				// PJ21211
				if (bVerschnittNachAbmessung && (dimension == 1 || dimension == 2)) {
					bdMenge = berechneMengeVerschnittNachAbmessungFLR(bdMenge, nLosgroesse, einheitDto, returnArray[i],
							theClientDto);
					returnArray[i].bdZielmenge = bdMenge;
				} else {

					if (dimension == 1) {
						if (returnArray[i].getF_dimension1() != null) {
							bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()));
						}
					} else if (dimension == 2) {
						if (returnArray[i].getF_dimension1() != null && returnArray[i].getF_dimension2() != null) {
							bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()))
									.multiply(new BigDecimal(returnArray[i].getF_dimension2().doubleValue()));
						}
					} else if (dimension == 3) {
						if (returnArray[i].getF_dimension1() != null && returnArray[i].getF_dimension2() != null
								&& returnArray[i].getF_dimension3() != null) {
							bdMenge = bdMenge.multiply(new BigDecimal(returnArray[i].getF_dimension1().doubleValue()))
									.multiply(new BigDecimal(returnArray[i].getF_dimension2().doubleValue()))
									.multiply(new BigDecimal(returnArray[i].getF_dimension3().doubleValue()));
						}
					}
					BigDecimal faktor = null;
					if (returnArray[i].getFlrartikel().getEinheit_c_nr().equals(returnArray[i].getEinheit_c_nr())) {
						faktor = new BigDecimal(1);
					} else {
						faktor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1),
								returnArray[i].getFlrartikel().getEinheit_c_nr(), returnArray[i].getEinheit_c_nr(),
								returnArray[i].getI_id(), theClientDto);
					}
					if (faktor.doubleValue() != 0) {
						bdMenge = bdMenge.divide(faktor, 12, BigDecimal.ROUND_HALF_EVEN);

						bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
								returnArray[i].getFlrartikel().getF_verschnittfaktor(),
								returnArray[i].getFlrartikel().getF_verschnittbasis(), nLosgroesse,
								returnArray[i].getFlrartikel().getF_fertigungs_vpe());

						// PJ 14352
						if (Helper.short2boolean(returnArray[i].getB_ruestmenge()) == false) {
							// SP4930 Nur Durch Erfassungsfaktor dividieren, wenn
							// keine Ruestmenge

							// SP7932
							if (returnArray[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue() <= 0) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN,
										new Exception("FEHLER_ERFASSUNGSFAKTOR_MUSS_GROESSER_0_SEIN"));
							}

							bdMenge = bdMenge.divide(
									new BigDecimal(
											returnArray[i].getFlrstueckliste().getN_erfassungsfaktor().doubleValue()),
									12, BigDecimal.ROUND_HALF_EVEN);
						}

						returnArray[i].bdZielmenge = bdMenge;

					} else {
						ArrayList<Object> al = new ArrayList<Object>();

						EinheitDto von = getSystemFac().einheitFindByPrimaryKey(returnArray[i].getEinheit_c_nr(),
								theClientDto);
						EinheitDto zu = getSystemFac().einheitFindByPrimaryKey(
								returnArray[i].getFlrartikel().getEinheit_c_nr(), theClientDto);
						al.add(von.formatBez() + " <-> " + zu.formatBez());
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT, al,
								new Exception("FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
					}
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}
		}

		// NEU Projekt 9730
		ArrayList alKomp = new ArrayList<FLRStuecklisteposition>();
		if (bGleichePositionenZusammenfassen == true) {
			// Positionen zusammenfassen, wenn der Artikel und die
			// Positionseinheit gleich sind
			// Nur wenns kein Arbeitsplan ist und in derselben Ebene
			for (int i = 0; i < returnArray.length; i++) {
				boolean bGefunden = false;

				for (int j = 0; j < alKomp.size(); j++) {
					FLRStuecklisteposition temp = (FLRStuecklisteposition) alKomp.get(j);

					if (temp.getFlrartikel().getI_id().equals(returnArray[i].getFlrartikel().getI_id())
							&& temp.getEinheit_c_nr().equals(returnArray[i].getEinheit_c_nr())
							&& temp.getB_ruestmenge().equals(returnArray[i].getB_ruestmenge())) {
						temp.setN_menge(temp.getN_menge().add(returnArray[i].getN_menge()));
						temp.bdZielmenge = temp.bdZielmenge.add(returnArray[i].bdZielmenge);

						// SP1167
						BigDecimal bdKalkPreis = temp.getN_kalkpreis();
						if (bdKalkPreis != null && returnArray[i].getN_kalkpreis() != null
								&& bdKalkPreis.doubleValue() < returnArray[i].getN_kalkpreis().doubleValue()) {
							temp.setN_kalkpreis(returnArray[i].getN_kalkpreis());
						} else if (bdKalkPreis == null && returnArray[i].getN_kalkpreis() != null) {
							temp.setN_kalkpreis(returnArray[i].getN_kalkpreis());
						}

						if (temp.getC_position() != null) {
							if (returnArray[i].getC_position() != null) {
								temp.setC_position(temp.getC_position() + "|" + returnArray[i].getC_position());
							} else {
								temp.setC_position(returnArray[i].getC_position());
							}
						} else {
							temp.setC_position(returnArray[i].getC_position());
						}

						if (temp.getC_kommentar() != null) {
							if (returnArray[i].getC_kommentar() != null) {
								temp.setC_kommentar(temp.getC_kommentar() + "|" + returnArray[i].getC_kommentar());
							} else {
								temp.setC_kommentar(returnArray[i].getC_kommentar());
							}
						} else {
							temp.setC_kommentar(returnArray[i].getC_kommentar());
						}

						if (Helper.short2boolean(temp.getB_mitdrucken()) == true
								&& Helper.short2boolean(returnArray[i].getB_mitdrucken()) == true) {

						} else {
							temp.setB_mitdrucken(Helper.boolean2Short(false));
						}

						alKomp.set(j, temp);

						bGefunden = true;
					}

				}
				if (!bGefunden) {
					alKomp.add(returnArray[i]);
				}

			}
			returnArray = new FLRStuecklisteposition[alKomp.size()];
			returnArray = (FLRStuecklisteposition[]) alKomp.toArray(returnArray);
		}
		// -ENDE NEU

		for (int i = 0; i < returnArray.length; i++) {

			StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
			stuecklistepositionDto.setArtikelIId(returnArray[i].getFlrartikel().getI_id());

			stuecklistepositionDto
					.setBdVerschnittmengeDesArtikels(returnArray[i].getFlrartikel().getN_verschnittmenge());

			stuecklistepositionDto.setCKommentar(returnArray[i].getC_kommentar());
			stuecklistepositionDto.setCPosition(returnArray[i].getC_position());
			stuecklistepositionDto.setEinheitCNr(returnArray[i].getEinheit_c_nr());
			stuecklistepositionDto.setILfdnummer(returnArray[i].getI_lfdnummer());
			stuecklistepositionDto.setNMenge(returnArray[i].getN_menge());
			stuecklistepositionDto.setNZielmenge(returnArray[i].bdZielmenge);
			stuecklistepositionDto.setNKalkpreis(returnArray[i].getN_kalkpreis());
			stuecklistepositionDto.setIId(returnArray[i].getI_id());
			stuecklistepositionDto.setISort(returnArray[i].getI_sort());
			stuecklistepositionDto.setBMitdrucken(returnArray[i].getB_mitdrucken());
			stuecklistepositionDto.setBRuestmenge(returnArray[i].getB_ruestmenge());
			stuecklistepositionDto.setBInitial(returnArray[i].getB_initial());

			stuecklistepositionDto.setStuecklisteIId(returnArray[i].getStueckliste_i_id());
			stuecklistepositionDto.setXFormel(returnArray[i].getX_formel());

			MontageartDto ma = new MontageartDto();
			ma.setIId(returnArray[i].getFlrmontageart().getI_id());
			ma.setCBez(returnArray[i].getFlrmontageart().getC_bez());
			stuecklistepositionDto.setMontageartDto(ma);
			if (returnArray[i].getF_dimension1() != null) {
				stuecklistepositionDto.setFDimension1(new Float(returnArray[i].getF_dimension1().doubleValue()));
			}
			if (returnArray[i].getF_dimension2() != null) {
				stuecklistepositionDto.setFDimension2(new Float(returnArray[i].getF_dimension2().doubleValue()));
			}
			if (returnArray[i].getF_dimension3() != null) {
				stuecklistepositionDto.setFDimension3(new Float(returnArray[i].getF_dimension3().doubleValue()));
			}

			StuecklisteMitStrukturDto zeile = new StuecklisteMitStrukturDto(iEbene, stuecklistepositionDto);

			zeile.setTFreigabe(returnArray[i].getFlrstueckliste().getT_freigabe());

			zeile.setBIstFremdfertigung(Helper.short2boolean(returnArray[i].getFlrstueckliste().getB_fremdfertigung()));

			if (returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe() != null) {
				zeile.setCKurzzeichenPersonFreigabe(
						returnArray[i].getFlrstueckliste().getFlrpersonal_freigabe().getC_kurzzeichen());
			}

			if (bMitUnterstuecklisten == true && bUnterstklstrukurBelassen == false
					&& bGleichePositionenZusammenfassen == true) {
				boolean bGefunden = false;
				for (int k = 0; k < strukturMap.size(); k++) {
					StuecklisteMitStrukturDto tempDto = (StuecklisteMitStrukturDto) strukturMap.get(k);
					if (tempDto.getStuecklistepositionDto().getArtikelIId()
							.equals(stuecklistepositionDto.getArtikelIId())
							&& tempDto.getStuecklistepositionDto().getEinheitCNr()
									.equals(stuecklistepositionDto.getEinheitCNr())) {
						tempDto.getStuecklistepositionDto().setNMenge(tempDto.getStuecklistepositionDto().getNMenge()
								.add(stuecklistepositionDto.getNMenge()));
						tempDto.getStuecklistepositionDto().setNZielmenge(tempDto.getStuecklistepositionDto()
								.getNZielmenge().add(stuecklistepositionDto.getNZielmenge()));
						strukturMap.set(k, tempDto);
						bGefunden = true;
						break;
					}
				}
				if (bGefunden == false) {
					strukturMap.add(zeile);
				}
			} else {
				strukturMap.add(zeile);
			}

			if (bMitUnterstuecklisten == true) {
				try {
					Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
					query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
					query.setParameter(2, theClientDto.getMandant());
					Stueckliste stkl = (Stueckliste) query.getSingleResult();
					if (stkl != null) {

						// wenn Fremdfertigungsstueckliste, dann keine
						// Aufloesung
						if (Helper.short2boolean(stkl.getBFremdfertigung()) == false
								|| fremdfertigungAufloesen == true) {

							// SP6514
							if (Helper.short2boolean(stkl.getBFremdfertigung()) == false) {
								if (stuecklistepositionDto.getNZielmenge().doubleValue() == 0) {
									boolean bZielmengeDarf0Sein = false;
									try {
										ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
												theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
												ParameterFac.PARAMETER_ZIELMENGE_GLEICH_0_ZULAESSIG);
										bZielmengeDarf0Sein = (Boolean) parameterDto.getCWertAsObject();
									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);
									}

									if (bZielmengeDarf0Sein == false) {
										ArrayList al = new ArrayList();
										al.add(returnArray[i].getFlrartikel().getC_nr());

										al.add(returnArray[i].getFlrstueckliste().getFlrartikel().getC_nr());

										stuecklistepositionDto.getArtikelDto();
										throw new EJBExceptionLP(
												EJBExceptionLP.FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL,
												al, new Exception(
														"FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL"));
									}
								}
							}
							query = em.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionen = query.getResultList().size();
							query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionenArbeitsplan = query.getResultList().size();
							// Wenn keine Positionen und kein Arbeitsplan, dann
							// auch keinen Aufloesung
							if (iAnzahlPositionen + iAnzahlPositionenArbeitsplan > 0) {
								StuecklisteMitStrukturDto strukturTemp = (StuecklisteMitStrukturDto) strukturMap
										.get(strukturMap.size() - 1);
								strukturTemp.setBStueckliste(true);
								strukturTemp.setIAnzahlArbeitsschritte(iAnzahlPositionenArbeitsplan);
								strukturTemp.setStuecklisteDto(assembleStuecklisteDto(stkl));
								strukturMap.set(strukturMap.size() - 1, strukturTemp);
								// Wenn Unterstklstrukur nicht belassen, dann
								// Kopfstuecklisten weglassen (lt. WH
								// 2008-06-09)
								if (bUnterstklstrukurBelassen == false) {
									strukturMap.remove(strukturMap.size() - 1);
									iEbene--;
								}

								strukturMap = getStrukturDatenEinerStueckliste(stkl.getIId(), theClientDto,
										iOptionSortierung, iEbene + 1, strukturMap, bMitUnterstuecklisten,
										bGleichePositionenZusammenfassen, nLosgroesse,
										stuecklistepositionDto.getNZielmenge(), bUnterstklstrukurBelassen,
										fremdfertigungAufloesen); // .multiply(
								// nSatzgroesse
								// )); ->
								// Ausgebaut
								// wegen
								// Naemo-
								// Stueckliste
								// FC002
							} else {
								if (bUnterstklstrukurBelassen == false) {
									strukturMap.remove(strukturMap.size() - 1);
								}
							}
						}
					}
				} catch (NoResultException ex) {
					// Dann keine Stueckliste
				} catch (NonUniqueResultException ex1) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
				}
			}
		}

		session.close();

		return strukturMap;
	}

	private BigDecimal berechneMengeVerschnittNachAbmessung(BigDecimal bdMenge, BigDecimal nLosgroesse, Artikel artikel,
			Einheit einheit, Stuecklisteposition stklPosition, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Geometrie geometrie = em.find(Geometrie.class, artikel.getIId());

		if (geometrie == null) {
			return bdMenge;
		}

		int dimension = einheit.getIDimension();

		Float dimension1 = stklPosition.getFDimension1();
		Float dimension2 = stklPosition.getFDimension2();

		Double fBreite = geometrie.getFBreite();
		Double fHoehe = geometrie.getFHoehe();

		if (dimension == 1 && fBreite != null && fBreite.doubleValue() > 0 && fHoehe != null
				&& fHoehe.doubleValue() > 0) {
			BigDecimal mengeDim1Umgerechnet = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(dimension1),
					stklPosition.getEinheitCNr(), artikel.getEinheitCNr(), stklPosition.getIId(), theClientDto);

			BigDecimal breiteInArtikeleinheit = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(fBreite),
					SystemFac.EINHEIT_MILLIMETER, artikel.getEinheitCNr(), stklPosition.getIId(), theClientDto);

			return Helper.berechneBenoetigteMenge1D(mengeDim1Umgerechnet, breiteInArtikeleinheit, bdMenge, nLosgroesse);
		} else if (dimension == 2 && fBreite != null && fBreite.doubleValue() > 0 && fHoehe != null
				&& fHoehe.doubleValue() > 0
				&& (stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
						|| stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER))
				&& (artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
						|| artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER))) {

			BigDecimal breiteInMillimeter = new BigDecimal(dimension1);
			BigDecimal hoeheInMillimeter = new BigDecimal(dimension2);

			if (stklPosition.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				breiteInMillimeter = getSystemFac().rechneUmInAndereEinheit(breiteInMillimeter,
						SystemFac.EINHEIT_QUADRATMETER, SystemFac.EINHEIT_QUADRATMILLIMETER, stklPosition.getIId(),
						theClientDto);
				hoeheInMillimeter = getSystemFac().rechneUmInAndereEinheit(hoeheInMillimeter,
						SystemFac.EINHEIT_QUADRATMETER, SystemFac.EINHEIT_QUADRATMILLIMETER, stklPosition.getIId(),
						theClientDto);
			}

			BigDecimal artikelHoeheInMillimeter = new BigDecimal(fHoehe);
			BigDecimal artikelBreiteInMillimeter = new BigDecimal(fBreite);

			bdMenge = Helper.berechneBenoetigteMenge2D(hoeheInMillimeter, breiteInMillimeter, artikelHoeheInMillimeter,
					artikelBreiteInMillimeter, bdMenge, nLosgroesse);

			if (artikel.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)) {
				bdMenge = getSystemFac().rechneUmInAndereEinheit(bdMenge, SystemFac.EINHEIT_QUADRATMILLIMETER,
						SystemFac.EINHEIT_QUADRATMETER, stklPosition.getIId(), theClientDto);
			}

			return bdMenge;
		} else {
			Stueckliste stkl = em.find(Stueckliste.class, stklPosition.getStuecklisteIId());
			Artikel artikelStkl = em.find(Artikel.class, stkl.getArtikelIId());
			myLogger.logKritisch("Stueckliste " + artikelStkl.getCNr() + " +Material-Artikel " + artikel.getCNr()
					+ " Umrechnung nach Zielmenge nicht moeglich");

			bdMenge = bdMenge.multiply(berechneDimensionGroesse(einheit.getIDimension(), stklPosition.getFDimension1(),
					stklPosition.getFDimension2(), stklPosition.getFDimension3()));

			bdMenge = getSystemFac().rechneUmInAndereEinheit(bdMenge, artikel.getEinheitCNr(), einheit.getCNr(),
					stklPosition.getIId(), theClientDto);

			return bdMenge;
		}

	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return berechneZielmenge(stuecklistepositionIId, theClientDto, BigDecimal.ONE);
	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId, TheClientDto theClientDto,
			BigDecimal nLosgroesse) throws EJBExceptionLP {

		// Menge nach Zieleinheit umrechnen

		Stuecklisteposition stuecklisteposition = em.find(Stuecklisteposition.class, stuecklistepositionIId);

		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklisteposition.getStuecklisteIId());

		BigDecimal bdMenge = stuecklisteposition.getNMenge();

		try {
			Artikel artikel = em.find(Artikel.class, stuecklisteposition.getArtikelIId());

			Einheit einheit = em.find(Einheit.class, stuecklisteposition.getEinheitCNr());

			// Positionsmenge nach Zielenge umrechnen

			BigDecimal divisor = getSystemFac().rechneUmInAndereEinheit(new BigDecimal(1), artikel.getEinheitCNr(),
					stuecklisteposition.getEinheitCNr(), stuecklisteposition.getIId(), theClientDto);

			int dimension = einheit.getIDimension().intValue();

			ParametermandantDto parameter = (ParametermandantDto) getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VERSCHNITT_NACH_ABMESSUNG);

			boolean bVerschnittNachAbmessung = (Boolean) parameter.getCWertAsObject();

			// PJ21211
			if (bVerschnittNachAbmessung && (dimension == 1 || dimension == 2)) {

				bdMenge = berechneMengeVerschnittNachAbmessung(bdMenge, nLosgroesse, artikel, einheit,
						stuecklisteposition, theClientDto);

			} else {

				bdMenge = bdMenge.multiply(berechneDimensionGroesse(dimension, stuecklisteposition.getFDimension1(),
						stuecklisteposition.getFDimension2(), stuecklisteposition.getFDimension3()));

				if (divisor != null && divisor.compareTo(BigDecimal.ZERO) != 0) {
					bdMenge = bdMenge.divide(divisor, 12, BigDecimal.ROUND_HALF_EVEN);

					bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge, artikel.getFVerschnittfaktor(),
							artikel.getFVerschnittbasis(), stueckliste.getNLosgroesse(), artikel.getFFertigungsVpe());
				}

				if (stueckliste.getNErfassungsfaktor() != null
						&& stueckliste.getNErfassungsfaktor().doubleValue() != 0) {
					bdMenge = bdMenge.divide(new BigDecimal(stueckliste.getNErfassungsfaktor().doubleValue()), 12,
							BigDecimal.ROUND_HALF_EVEN);
				}

			}
			bdMenge = bdMenge.multiply(nLosgroesse);
			try {
				Query query = em.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, stuecklisteposition.getArtikelIId());
				query.setParameter(2, stueckliste.getMandantCNr());
				Stueckliste stkl = (Stueckliste) query.getSingleResult();
				if (stkl != null && Helper.short2boolean(stkl.getBFremdfertigung()) == false) {

					if (bdMenge.doubleValue() == 0) {
						boolean bZielmengeDarf0Sein = false;
						try {
							ParametermandantDto parameterDto = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.PARAMETER_ZIELMENGE_GLEICH_0_ZULAESSIG);
							bZielmengeDarf0Sein = (Boolean) parameterDto.getCWertAsObject();
						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						if (bZielmengeDarf0Sein == false) {
							ArrayList al = new ArrayList();
							al.add(artikel.getCNr());

							Artikel artikelStkl = em.find(Artikel.class, stueckliste.getArtikelIId());

							al.add(artikelStkl.getCNr());

							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL,
									al, new Exception(
											"FEHLER_STUECKLISTE_EIGENGEFERTIGTE_UNTERSTUECKLISTE_MIT_ZIELMENGE_GLEICH_NULL"));
						}
					}
				}

			} catch (NoResultException ex) {
				//
			}

			return bdMenge;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	private BigDecimal berechneDimensionGroesse(int dimension, Number dim1, Number dim2, Number dim3) {
		BigDecimal menge = BigDecimal.ONE;
		switch (dimension) {
		case 3:
			if (dim3 != null) {
				menge = menge.multiply(new BigDecimal(dim3.doubleValue()));
			}
			/* Fall Through */
		case 2:
			if (dim2 != null) {
				menge = menge.multiply(new BigDecimal(dim2.doubleValue()));
			}
			/* Fall Through */
		case 1:
			if (dim1 != null) {
				menge = menge.multiply(new BigDecimal(dim1.doubleValue()));
			}
		}

		return menge;
	}

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId, boolean bMitVersteckten,
			TheClientDto theClientDto) {
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

		int iNachkommastellenMenge = 2;
		try {
			iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT pos.flrartikel.i_id,pos.flrartikel.c_nr, (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=pos.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "'),pos  FROM FLRStuecklisteposition AS pos WHERE pos.stueckliste_i_id=" + stuecklisteIId + "";

		if (bMitVersteckten == false) {
			queryString += " AND pos.flrartikel.b_versteckt=0";
		}

		queryString += " ORDER BY pos.flrartikel.c_nr";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		int iLfd = 0;
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer artikelIId = (Integer) o[0];

			String c_nr = (String) o[1];
			String c_bez = (String) o[2];
			FLRStuecklisteposition pos = (FLRStuecklisteposition) o[3];

			String s = c_nr;

			if (c_bez != null) {

				s += " " + c_bez;
			}

			s = s + " --> " + Helper.formatZahl(pos.getN_menge(), iNachkommastellenMenge, theClientDto.getLocUi()) + " "
					+ pos.getEinheit_c_nr().trim();

			if (tm.containsKey(s)) {
				s += "|" + iLfd;
				iLfd++;
			}

			tm.put(s, artikelIId);

			// }

		}

		session.close();
		return tm;
	}

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten(boolean bMitVersteckten, TheClientDto theClientDto) {
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		String queryString = "SELECT STKL.I_ID, A.C_NR,(SELECT SPR.C_BEZ FROM WW_ARTIKELSPR SPR WHERE SPR.LOCALE_C_NR='"
				+ sLocUI
				+ "' AND SPR.ARTIKEL_I_ID=A.I_ID ) FROM WW_ARTIKEL A RIGHT OUTER JOIN STK_STUECKLISTE STKL ON A.I_ID = STKL.ARTIKEL_I_ID WHERE (SELECT count(*) FROM STK_STUECKLISTEPOSITION POS WHERE POS.ARTIKEL_I_ID=A.I_ID)=0 AND STKL.MANDANT_C_NR='"
				+ theClientDto.getMandant() + "'";

		if (bMitVersteckten == false) {
			queryString += " AND A.B_VERSTECKT=0";
		}
		org.hibernate.Query query = session.createSQLQuery(queryString);

		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			String c_nr = (String) o[1];
			String c_bez = (String) o[2];

			/*
			 * String sQuery2=
			 * "SELECT pos.i_id FROM FLRStuecklistepositionFuerDruck AS pos WHERE pos.artikel_i_id="
			 * +flrStueckliste.getArtikel_i_id();
			 * 
			 * 
			 * Session session2 = FLRSessionFactory.getFactory().openSession();
			 * 
			 * org.hibernate.Query query2 = session2.createQuery(sQuery2);
			 * query2.setMaxResults(1); List<?> resultList2 = query2.list();
			 * 
			 * if (resultList2.size() == 0) {
			 */

			// Es ist eine Wurzel
			String s = c_nr;

			if (c_bez != null) {

				s += " " + c_bez;
			}

			tm.put(s, (Integer) o[0]);

			// }

		}

		session.close();
		return tm;
	}

	public Integer createApkommentar(ApkommentarDto dto, TheClientDto theClientDto) {

		dto.setMandantCNr(theClientDto.getMandant());

		try {
			Query query = em.createNamedQuery("ApkommentarFindByCNrMandantCNr");
			query.setParameter(1, dto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			Apkommentar doppelt = (Apkommentar) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STK_APKOMMENTAR.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_APKOMMENTAR);
			dto.setIId(pk);

			Apkommentar apkommentar = new Apkommentar(dto.getIId(), dto.getCNr(), dto.getMandantCNr());
			em.persist(apkommentar);
			em.flush();
			setApkommentarFromApkommentarDto(apkommentar, dto);
			if (dto.getApkommentarsprDto() != null) {
				Apkommentarspr apkommentarspr = new Apkommentarspr(theClientDto.getLocUiAsString(),
						dto.getApkommentarsprDto().getCBez(), dto.getIId());
				em.persist(apkommentarspr);
				em.flush();

			}
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createPruefkombination(PruefkombinationDto dto, TheClientDto theClientDto) {

		try {

			PruefartDto pruefartDto = pruefartFindByPrimaryKey(dto.getPruefartIId(), theClientDto);

			// Werkzeug und Litze muss nur bei Crimpen befuellt sein
			if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)
					|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
				if (dto.getVerschleissteilIId() == null || dto.getArtikelIIdLitze() == null) {

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
							new Exception("dto.getVerschleissteilIId() == null || dto.getArtikelIIdLitze() == null"));
				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO)) {
					if (dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null
							|| dto.getNCrimpbreiteIsolation() == null || dto.getNCrimphoeheIsolation() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null || dto.getNCrimpbreiteIsolation() == null || dto.getNCrimphoeheIsolation() == null"));
					}
				}
				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO)) {
					if (dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getNCrimpbreitDraht() == null || dto.getNCrimphoeheDraht() == null"));
					}
					dto.setNCrimpbreiteIsolation(null);
					dto.setNCrimphoeheIsolation(null);
				}
			} else {
				dto.setVerschleissteilIId(null);

				dto.setNCrimpbreitDraht(null);
				dto.setNCrimphoeheDraht(null);
				dto.setNCrimpbreiteIsolation(null);
				dto.setNCrimphoeheIsolation(null);
				dto.setNToleranzCrimpbreitDraht(null);
				dto.setNToleranzCrimphoeheDraht(null);
				dto.setNToleranzCrimpbreiteIsolation(null);
				dto.setNToleranzCrimphoeheIsolation(null);

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_MASSPRUEFUNG)) {
					dto.setArtikelIIdKontakt(null);

					dto.setNWert(null);
				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_KRAFTMESSUNG)) {

					if (dto.getArtikelIIdKontakt() == null || dto.getArtikelIIdLitze() == null) {

						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
								"dto.getArtikelIIdKontakt() == null || dto.getArtikelIIdLitze() == null"));
					}
					dto.setNToleranzWert(null);

				}

				if (pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_ELEKTRISCHE_PRUEFUNG)
						|| pruefartDto.getCNr().equals(StuecklisteFac.PRUEFART_OPTISCHE_PRUEFUNG)) {
					dto.setNToleranzWert(null);
					dto.setArtikelIIdLitze(null);
					dto.setNWert(null);
				}

			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_PRUEFKOMBINATION);
			dto.setIId(pk);

			dto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			dto.setTAendern(new Timestamp(System.currentTimeMillis()));
			dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			dto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Pruefkombination pruefkombination = new Pruefkombination(dto.getIId(), dto.getPruefartIId(),
					dto.getBStandard(), dto.getPersonalIIdAnlegen(), dto.getTAnlegen(), dto.getPersonalIIdAendern(),
					dto.getTAendern(), dto.getBDoppelanschlag());
			em.persist(pruefkombination);
			em.flush();
			setPruefkombinationFromPruefkombinationDto(pruefkombination, dto);

			if (dto.getPruefkombinationsprDto() != null) {
				Pruefkombinationspr apkommentarspr = new Pruefkombinationspr(theClientDto.getLocUiAsString(),
						dto.getPruefkombinationsprDto().getCBez(), dto.getIId());
				em.persist(apkommentarspr);
				em.flush();

			}

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public Integer createProduktstuecklisteAnhandFormelstuecklisteUndErzeugeAngebot(Integer angebotIId,
			Integer stuecklisteIId_Formelstueckliste, BigDecimal losgroesse, Integer kundeIId,
			Map<String, Object> konfigurationsWerte, TheClientDto theClientDto) {

		try {
			Integer stuecklisteIIdNeu = getStuecklisteFac().createProduktstuecklisteAnhandFormelstueckliste(
					stuecklisteIId_Formelstueckliste, losgroesse, konfigurationsWerte, kundeIId, theClientDto);

			KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kundeIId, theClientDto);

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIIdNeu, theClientDto);

			stklDto.setPartnerIId(kdDto.getPartnerIId());

			getStuecklisteFac().updateStueckliste(stklDto, theClientDto);

			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(stklDto.getArtikelIId(), theClientDto);

			AngebotDto angebotDto = null;

			if (angebotIId == null) {

				angebotDto = new AngebotDto();
				angebotDto.setMandantCNr(theClientDto.getMandant());
				angebotDto.setArtCNr(AngebotServiceFac.ANGEBOTART_FREI);
				angebotDto.setStatusCNr(AngebotServiceFac.ANGEBOTSTATUS_ANGELEGT);
				angebotDto.setBelegartCNr(LocaleFac.BELEGART_ANGEBOT);
				angebotDto.setKundeIIdAngebotsadresse(kundeIId);
				angebotDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
				angebotDto.setTAnfragedatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
				angebotDto.setPersonalIIdVertreter(theClientDto.getIDPersonal());

				ParametermandantDto parametermandantDto = getParameterFac().getMandantparameter(
						theClientDto.getMandant(), ParameterFac.KATEGORIE_ANGEBOT,
						ParameterFac.PARAMETER_DEFAULT_ANGEBOT_GUELTIGKEIT);

				int iAngebotGueltigkeitsdauer = ((Integer) parametermandantDto.getCWertAsObject()).intValue();

				// die angegebene Anzahl von Tagen zum heutigen Tag dazuzaehlen
				GregorianCalendar gc = new GregorianCalendar();
				gc.add(Calendar.DATE, iAngebotGueltigkeitsdauer);
				Timestamp tAngebotGueltigkeitsdauer = new Timestamp(gc.getTimeInMillis());

				angebotDto.setTAngebotsgueltigkeitbis(tAngebotGueltigkeitsdauer);

				angebotDto.setWaehrungCNr(kdDto.getCWaehrung());

				BigDecimal cu = getLocaleFac().getWechselkurs2(theClientDto.getSMandantenwaehrung(),
						kdDto.getCWaehrung(), theClientDto);
				angebotDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(cu.doubleValue()));
				angebotDto.setAngeboteinheitCNr(AngebotServiceFac.ANGEBOTEINHEIT_TAG);

				angebotDto.setILieferzeitinstunden(0);

				angebotDto.setKostenstelleIId(kdDto.getKostenstelleIId());
				angebotDto.setTNachfasstermin(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));

				angebotDto.setFAuftragswahrscheinlichkeit(new Double(0));
				angebotDto.setFAllgemeinerRabattsatz(new Double(0));
				angebotDto.setFVersteckterAufschlag(new Double(0));
				angebotDto.setFProjektierungsrabattsatz(new Double(0));

				angebotDto.setLieferartIId(kdDto.getLieferartIId());
				angebotDto.setZahlungszielIId(kdDto.getZahlungszielIId());
				angebotDto.setSpediteurIId(kdDto.getSpediteurIId());

				angebotDto.setIGarantie(0);

				if (kdDto.getIGarantieinmonaten() != null) {
					angebotDto.setIGarantie(kdDto.getIGarantieinmonaten());
				}
				angebotIId = getAngebotFac().createAngebot(angebotDto, theClientDto);

			} else {
				angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotIId, theClientDto);
			}
			AngebotpositionDto angebotpositionDto = new AngebotpositionDto();

			angebotpositionDto.setBelegIId(angebotIId);
			angebotpositionDto.setArtikelIId(aDto.getIId());
			angebotpositionDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
			angebotpositionDto.setNMenge(losgroesse);
			angebotpositionDto.setEinheitCNr(aDto.getEinheitCNr());
			angebotpositionDto.setBAlternative(Helper.boolean2Short(false));
			angebotpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

			angebotpositionDto = (AngebotpositionDto) befuellePreisfelderAnhandVKPreisfindung(angebotpositionDto,
					angebotDto.getTBelegdatum(), kundeIId, theClientDto.getSMandantenwaehrung(), theClientDto);
			angebotpositionDto.setNGestehungspreis(BigDecimal.ZERO);
			getAngebotpositionFac().createAngebotposition(angebotpositionDto, false, theClientDto);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return angebotIId;
	}

	public Integer createProduktstuecklisteAnhandFormelstueckliste(Integer stuecklisteIId_Formelstueckliste,
			BigDecimal losgroesse, Map<String, Object> konfigurationsWerte, Integer kundeIId,
			TheClientDto theClientDto) {
		ReportSqlExecutor sqlExecutor = null;
		try {
			ScriptRuntime rt = prepareRuntime(new DebuggingScriptRuntime(), konfigurationsWerte);
			rt.beProduktStueckliste();

			StrukturDatenParamDto paramDto = new StrukturDatenParamDto();
//			paramDto.setSortierung(Sort.Artikelnummer).setLosgroesse(BigDecimal.ZERO).setUeberAlleMandanten(false)
			paramDto.setSortierung(Sort.Ohne).setLosgroesse(BigDecimal.ZERO).setUeberAlleMandanten(false)
					.beMitUnterstuecklisten().setGleichePositionenZusammenfassen(false).setRuntime(rt);
			Connection c = getReportConnectionFacLocal().getConnectionWithEjbEx(theClientDto.getIDUser());
			sqlExecutor = new ReportSqlExecutor(c);
			rt.setSql(sqlExecutor);

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_ANZAHL_STELLEN_LFD_NR_PRODUKTSTUECKLISTE);
			int iAnzahlStellenLfdNr = (Integer) parameter.getCWertAsObject();

			LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant(), getDate());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(iGeschaeftsjahr,
					PKConst.PK_LFDNR_PRODUKTSTUECKLISTE, theClientDto.getMandant(), theClientDto);

			String gfJahr = (iGeschaeftsjahr + "").substring(2);
			String postfix = "_" + gfJahr
					+ Helper.fitString2LengthAlignRight(bnr.getBelegNummer() + "", iAnzahlStellenLfdNr, '0');

			StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByPrimaryKey(stuecklisteIId_Formelstueckliste,
					theClientDto);

			StuecklisteAufgeloest stuecklisteInfoDto = stuecklisteFacLocalBean
					.getStrukturdatenEinesArtikelsStrukturiert(stklDto.getArtikelIId(), false, 99, false, losgroesse,
							true, false, 0, paramDto, theClientDto);

			Integer stuecklisteIIdNeuKopf = null;

			Integer stuecklisteIIdNeu = createUnterstueckliste(stuecklisteInfoDto.getStuecklisteDto(),
					stuecklisteInfoDto.getPositionen(), stuecklisteInfoDto.getArbeitsplanPositionen(), postfix,
					konfigurationsWerte, theClientDto);

			StuecklisteDto stklDtoNeu = stuecklisteFindByPrimaryKey(stuecklisteIIdNeu, theClientDto);

			JasperPrintLP print = getStuecklisteReportFac().printGesamtkalkulationKonfigurator(
					stuecklisteIId_Formelstueckliste, losgroesse, false, false, false, false, false,
					konfigurationsWerte, kundeIId, false, null, null, null, null, theClientDto);

			Map m = print.getMapParameters();

			BigDecimal bdErrechneterPreisInMandantenwahrung = BigDecimal.ZERO;
			if (m.containsKey("P_SELBSTKOSTENLIEF")) {
				bdErrechneterPreisInMandantenwahrung = (BigDecimal) m.get("P_SELBSTKOSTENLIEF");
			}

			if (m.containsKey("P_STUECKLISTE_MINDESTDECKUNGSBEITRAG")) {

				Double mindestedeckungsbeitrag = (Double) m.get("P_STUECKLISTE_MINDESTDECKUNGSBEITRAG");

				bdErrechneterPreisInMandantenwahrung = bdErrechneterPreisInMandantenwahrung
						.multiply(new BigDecimal(1 + mindestedeckungsbeitrag.doubleValue() / 100));
			}

			bdErrechneterPreisInMandantenwahrung = Helper.rundeKaufmaennisch(bdErrechneterPreisInMandantenwahrung,
					getMandantFac().getNachkommastellenPreisVK(theClientDto.getMandant()));

			if (bdErrechneterPreisInMandantenwahrung != null) {
				VkPreisfindungEinzelverkaufspreisDto preisbasisDto = new VkPreisfindungEinzelverkaufspreisDto();

				preisbasisDto.setArtikelIId(stklDtoNeu.getArtikelIId());
				preisbasisDto.setMandantCNr(theClientDto.getMandant());
				preisbasisDto.setNVerkaufspreisbasis(bdErrechneterPreisInMandantenwahrung);
				preisbasisDto
						.setTVerkaufspreisbasisgueltigab(Helper.cutDate(new java.sql.Date(System.currentTimeMillis())));

				getVkPreisfindungFac().createVkPreisfindungEinzelverkaufspreis(preisbasisDto, theClientDto);
			}

			return stuecklisteIIdNeu;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		} finally {
			if (sqlExecutor != null) {
				sqlExecutor.close();
				try {
					getReportConnectionFacLocal().closeConnection(theClientDto.getIDUser(), null);
				} catch (SQLException e) {
					myLogger.error("Closing physical connection", e);
				}
			}
		}
	}

	private Integer createUnterstueckliste(StuecklisteDto stklDtoKopf, ArrayList<StuecklisteAufgeloest> positionen,
			ArrayList<StuecklistearbeitsplanDto> apdtos, String lfdnummer, Map<String, Object> konfigurationsWerte,
			TheClientDto theClientDto) {

		try {

			Integer stuecklisteIIdNeu = null;

			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_ARTIKELNUMMER);
			int iLaengeArtikelnummer = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_ANZAHL_STELLEN_LFD_NR_PRODUKTSTUECKLISTE);
			int iAnzahlStellenLfdNr = (Integer) parameter.getCWertAsObject();

			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_DIMENSIONEN_BESTELLEN);
			boolean bDimensionenBestellen = (Boolean) parameter.getCWertAsObject();

			if (Helper.short2boolean(stklDtoKopf.getBMitFormeln())) {

				if (stklDtoKopf.getArtikelDto() == null) {
					stklDtoKopf.setArtikelDto(
							getArtikelFac().artikelFindByPrimaryKey(stklDtoKopf.getArtikelIId(), theClientDto));
				}

				String artikelnummerNeu = Helper.fitString2Length(stklDtoKopf.getArtikelDto().getCNr(),
						iLaengeArtikelnummer - (iAnzahlStellenLfdNr + 3), '_') + lfdnummer;

				Integer stuecklisteIIdFormelstueckliste = stklDtoKopf.getIId();

				HashMap zuKopieren = new HashMap();
				zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG, "");
				// SP6340
				zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE, "");

				String cBezUebersteuert = null;
				String cKbezUebersteuert = null;
				String cZbezUebersteuert = null;
				String cZbez2Uebersteuert = null;

				for (int i = 0; i < positionen.size(); i++) {

					StuecklisteAufgeloest positionAufgeloest = positionen.get(i);
					StuecklistepositionDto position = positionAufgeloest.getStuecklistepositionDto();

					// PJ20862
					if (position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().hatCnr()) {
						if (position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getGeneriereCnr()) {
							artikelnummerNeu = getArtikelFac().generiereNeueArtikelnummer(
									position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getCnr(), theClientDto);
						} else {
							artikelnummerNeu = position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getCnr();
						}
						cBezUebersteuert = position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getcBez();
						cKbezUebersteuert = position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getcKbez();
						cZbezUebersteuert = position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getcZbez();
						cZbez2Uebersteuert = position.getFormelUebergeordneteArtikelCNr_NOT_IN_DB().getcZbez2();
						break;
					}

				}

				Object[] o = getArtikelFac().kopiereArtikel(stklDtoKopf.getArtikelIId(), artikelnummerNeu, zuKopieren,
						null, null, theClientDto);

				if (cBezUebersteuert != null || cKbezUebersteuert != null || cZbezUebersteuert != null
						|| cZbez2Uebersteuert != null) {
					Integer artikelIIdNeu = (Integer) o[0];

					ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKey(artikelIIdNeu, theClientDto);
					if (cBezUebersteuert != null) {
						aDto.getArtikelsprDto().setCBez(cBezUebersteuert);

					}
					if (cKbezUebersteuert != null) {
						if (cKbezUebersteuert.equals("")) {
							aDto.getArtikelsprDto().setCKbez(null);
						} else {
							aDto.getArtikelsprDto().setCKbez(cKbezUebersteuert);
						}

					}
					if (cZbezUebersteuert != null) {
						if (cZbezUebersteuert.equals("")) {
							aDto.getArtikelsprDto().setCZbez(null);
						} else {
							aDto.getArtikelsprDto().setCZbez(cZbezUebersteuert);
						}

					}
					if (cZbez2Uebersteuert != null) {
						if (cZbez2Uebersteuert.equals("")) {
							aDto.getArtikelsprDto().setCZbez2(null);
						} else {
							aDto.getArtikelsprDto().setCZbez2(cZbez2Uebersteuert);
						}

					}
					getArtikelFac().updateArtikel(aDto, theClientDto);
				}

				stklDtoKopf.setIId(null);
				stklDtoKopf.setArtikelDto(null);
				stklDtoKopf.setArtikelIId((Integer) o[0]);
				stklDtoKopf.setBMitFormeln(Helper.boolean2Short(false));
				stklDtoKopf.setStuecklisteIIdFormelstueckliste(stuecklisteIIdFormelstueckliste);

				// PJ20862
				if (konfigurationsWerte != null) {
					Iterator it = konfigurationsWerte.values().iterator();
					while (it.hasNext()) {
						Object oTemp = it.next();
						if (oTemp instanceof KundeId) {
							KundeId kdid = (KundeId) oTemp;
							if (kdid != null && kdid.id() != null) {
								KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(kdid.id(), theClientDto);
								stklDtoKopf.setPartnerIId(kdDto.getPartnerIId());
								break;
							}
						}
					}
				}

				stuecklisteIIdNeu = createStueckliste(stklDtoKopf, theClientDto);

				// parameter
				kopiereParameterEinerStueckliste(stuecklisteIIdFormelstueckliste, stuecklisteIIdNeu,
						konfigurationsWerte, theClientDto);

				stklDtoKopf = stuecklisteFindByPrimaryKey(stuecklisteIIdNeu, theClientDto);

			}

			for (int i = 0; i < positionen.size(); i++) {

				StuecklisteAufgeloest positionAufgeloest = positionen.get(i);
				StuecklistepositionDto position = positionAufgeloest.getStuecklistepositionDto();

				// SP5930 Wenn Zielmenge 0, dann auslassen
				if (position.getNZielmenge().doubleValue() != 0) {

					StuecklistepositionDto stklpositionDtoOriginal = stuecklistepositionFindByPrimaryKey(
							position.getIId(), theClientDto);
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(position.getArtikelIId(),
							theClientDto);

					if (artikelDto.getCNr().equals("Z_SCHLEIFE")) {
						int z = 0;
					}

					// PJ20563
					if (bDimensionenBestellen) {

						EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(artikelDto.getEinheitCNr(),
								theClientDto);

						if (einheitDto.getIDimension() > 0) {

							// SP6280 Wenn die Positionsmengeneinheit bereits
							// mm/mm2 oder mm3 ist,
							// dann OK, ansonsten daraufhin umrechen, da immer
							// mm bestellt werden.

							BigDecimal faktor = BigDecimal.ONE;
							if (position.getEinheitCNr().equals(SystemFac.EINHEIT_MILLIMETER)
									|| position.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMILLIMETER)
									|| position.getEinheitCNr().equals(SystemFac.EINHEIT_KUBIKMILLIMETER)) {

							} else if (position.getEinheitCNr().equals(SystemFac.EINHEIT_METER)
									|| position.getEinheitCNr().equals(SystemFac.EINHEIT_QUADRATMETER)
									|| position.getEinheitCNr().equals(SystemFac.EINHEIT_KUBIKMETER)) {
								faktor = getSystemFac().rechneUmInAndereEinheit(faktor, SystemFac.EINHEIT_METER,
										SystemFac.EINHEIT_MILLIMETER, stklpositionDtoOriginal.getIId(), theClientDto);
							}

							Integer iDim1 = null;
							Integer iDim2 = null;
							Integer iDim3 = null;
							if (position.getFDimension1() != null) {

								iDim1 = new BigDecimal(position.getFDimension1().floatValue()).multiply(faktor)
										.intValue();
							}
							if (position.getFDimension2() != null) {
								iDim2 = new BigDecimal(position.getFDimension2().floatValue()).multiply(faktor)
										.intValue();
							}
							if (position.getFDimension3() != null) {
								iDim3 = new BigDecimal(position.getFDimension3().floatValue()).multiply(faktor)
										.intValue();
							}

							Integer artikelIIdNeu = getArtikelFac().kopiereArtikelFuerDimensionenBestellen(
									artikelDto.getIId(), position.getNZielmenge(), iDim1, iDim2, iDim3, theClientDto);

							artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIIdNeu, theClientDto);

							position.setFDimension1(null);
							position.setFDimension2(null);
							position.setFDimension3(null);
							position.setEinheitCNr(SystemFac.EINHEIT_STUECK);

						}

					}

					if (positionAufgeloest.getStuecklisteDto() != null
							&& Helper.short2boolean(positionAufgeloest.getStuecklisteDto().getBMitFormeln())) {
						Integer stucklisteIId = createUnterstueckliste(positionAufgeloest.getStuecklisteDto(),
								positionAufgeloest.getPositionen(), positionAufgeloest.getArbeitsplanPositionen(),
								lfdnummer, konfigurationsWerte, theClientDto);

						if (stucklisteIId != null) {
							StuecklisteDto stklDtoNeu = stuecklisteFindByPrimaryKey(stucklisteIId, theClientDto);

							artikelDto = stklDtoNeu.getArtikelDto();
						}

					} else {
						if (!bDimensionenBestellen) {
							// PJ20862
							if (position.getFormelArtikelCNr_NOT_IN_DB().hatCnr()) {

								HashMap zuKopieren = new HashMap();
								zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_MINDESTDECKUNGSBEITRAG, "");
								// SP6340
								zuKopieren.put(ArtikelFac.ARTIKEL_KOPIEREN_KOMMENTARE, "");

								String arikelnummerNeu = position.getFormelArtikelCNr_NOT_IN_DB().getCnr();
								if (position.getFormelArtikelCNr_NOT_IN_DB().getGeneriereCnr()) {
									arikelnummerNeu = getArtikelFac().generiereNeueArtikelnummer(
											position.getFormelArtikelCNr_NOT_IN_DB().getCnr(), theClientDto);
								}

								Object[] o = getArtikelFac().kopiereArtikel(artikelDto.getIId(), arikelnummerNeu,
										zuKopieren, null, null, theClientDto);

								Integer artikelIIdNeu = (Integer) o[0];
								artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIIdNeu, theClientDto);

								if (position.getFormelArtikelCNr_NOT_IN_DB().getcBez() != null
										|| position.getFormelArtikelCNr_NOT_IN_DB().getcKbez() != null
										|| position.getFormelArtikelCNr_NOT_IN_DB().getcZbez() != null
										|| position.getFormelArtikelCNr_NOT_IN_DB().getcZbez2() != null) {

									if (position.getFormelArtikelCNr_NOT_IN_DB().getcBez() != null) {

										artikelDto.getArtikelsprDto()
												.setCBez(position.getFormelArtikelCNr_NOT_IN_DB().getcBez());

									}
									if (position.getFormelArtikelCNr_NOT_IN_DB().getcKbez() != null) {

										// Bei Leerstring auf NULL setzen
										if (position.getFormelArtikelCNr_NOT_IN_DB().getcKbez().equals("")) {
											artikelDto.getArtikelsprDto().setCKbez(null);
										} else {
											artikelDto.getArtikelsprDto()
													.setCKbez(position.getFormelArtikelCNr_NOT_IN_DB().getcKbez());
										}

									}
									if (position.getFormelArtikelCNr_NOT_IN_DB().getcZbez() != null) {
										// Bei Leerstring auf NULL setzen
										if (position.getFormelArtikelCNr_NOT_IN_DB().getcZbez().equals("")) {
											artikelDto.getArtikelsprDto().setCZbez(null);
										} else {
											artikelDto.getArtikelsprDto()
													.setCZbez(position.getFormelArtikelCNr_NOT_IN_DB().getcZbez());
										}

									}
									if (position.getFormelArtikelCNr_NOT_IN_DB().getcZbez2() != null) {
										// Bei Leerstring auf NULL setzen
										if (position.getFormelArtikelCNr_NOT_IN_DB().getcZbez2().equals("")) {
											artikelDto.getArtikelsprDto().setCZbez2(null);
										} else {
											artikelDto.getArtikelsprDto()
													.setCZbez2(position.getFormelArtikelCNr_NOT_IN_DB().getcZbez2());
										}

									}
									getArtikelFac().updateArtikel(artikelDto, theClientDto);
								}

							}
						}
					}
					stklpositionDtoOriginal.setArtikelDto(artikelDto);
					stklpositionDtoOriginal.setArtikelIId(artikelDto.getIId());

					stklpositionDtoOriginal.setEinheitCNr(position.getEinheitCNr());

					stklpositionDtoOriginal.setNMenge(position.getNMenge());

					stklpositionDtoOriginal.setFDimension1(position.getFDimension1());
					stklpositionDtoOriginal.setFDimension2(position.getFDimension2());
					stklpositionDtoOriginal.setFDimension3(position.getFDimension3());

					stklpositionDtoOriginal.setStuecklisteIId(stklDtoKopf.getIId());
					stklpositionDtoOriginal.setIId(null);
					stklpositionDtoOriginal.setXFormel(null);

					if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						stklpositionDtoOriginal.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);

						// SP1944
						if (artikelDto.getArtikelsprDto() != null) {
							stklpositionDtoOriginal.setSHandeingabe(artikelDto.getArtikelsprDto().getCBez());
						}

					} else {
						stklpositionDtoOriginal.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
					}

					createStuecklisteposition(stklpositionDtoOriginal, theClientDto);
				}
			}

			for (int j = 0; j < apdtos.size(); j++) {
				StuecklistearbeitsplanDto stklapDto = apdtos.get(j);
				stklapDto.setStuecklisteIId(stklDtoKopf.getIId());
				stklapDto.setIId(null);
				stklapDto.setXFormel(null);
				stklapDto.setStuecklistepositionIId(null);

				// SP5930
				if (stklapDto.getLRuestzeit() != 0 || stklapDto.getLStueckzeit() != 0) {
					createStuecklistearbeitsplan(stklapDto, theClientDto);
				}

			}

			return stuecklisteIIdNeu;

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}

	}

	public Integer wirdArtikelInFreigegebenerStuecklisteVerwendet(Integer artikelIId,
			boolean freigabeDerStuecklistenZuruecknehmen, TheClientDto theClientDto) {
		return wirdArtikelInFreigegebenerStuecklisteVerwendet(artikelIId, freigabeDerStuecklistenZuruecknehmen, null,
				theClientDto);
	}

	private Integer wirdArtikelInFreigegebenerStuecklisteVerwendet(Integer artikelIId,
			boolean freigabeDerStuecklistenZuruecknehmen, HashSet<Integer> hsBereitsBeruecksichtigteArtikel,
			TheClientDto theClientDto) {

		if (hsBereitsBeruecksichtigteArtikel == null) {
			hsBereitsBeruecksichtigteArtikel = new HashSet<Integer>();
		}

		hsBereitsBeruecksichtigteArtikel.add(artikelIId);

		com.lp.server.artikel.service.ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId,
				theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT pos FROM FLRStuecklisteposition pos WHERE pos.flrstueckliste.t_freigabe IS NOT NULL AND pos.flrartikel.i_id="
				+ artikelIId;

		org.hibernate.Query hquery = session.createQuery(sQuery);

		List<?> resultList = hquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition flrStuecklisteposition = (FLRStuecklisteposition) resultListIterator.next();
			if (freigabeDerStuecklistenZuruecknehmen) {
				getStuecklisteFac().toggleFreigabe(flrStuecklisteposition.getStueckliste_i_id(), theClientDto);
			} else {
				return flrStuecklisteposition.getStueckliste_i_id();
			}

		}
		session.close();

		session = FLRSessionFactory.getFactory().openSession();

		sQuery = "SELECT ers FROM FLRPosersatz ers WHERE ers.flrstuecklisteposition.flrstueckliste.t_freigabe IS NOT NULL AND ers.flrartikel.i_id="
				+ artikelIId;

		hquery = session.createQuery(sQuery);

		resultList = hquery.list();
		resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRPosersatz flrPosersatz = (FLRPosersatz) resultListIterator.next();
			if (freigabeDerStuecklistenZuruecknehmen) {
				getStuecklisteFac().toggleFreigabe(
						flrPosersatz.getFlrstuecklisteposition().getFlrstueckliste().getI_id(), theClientDto);
			} else {
				return flrPosersatz.getFlrstuecklisteposition().getFlrstueckliste().getI_id();
			}
		}

		ErsatztypenDto[] ersatztypenDtos = getArtikelFac().ersatztypenfindByArtikelIIdErsatz(artikelDto.getIId());

		for (int k = 0; k < ersatztypenDtos.length; k++) {

			if (!hsBereitsBeruecksichtigteArtikel.contains(ersatztypenDtos[k].getArtikelIId())) {
				return wirdArtikelInFreigegebenerStuecklisteVerwendet(ersatztypenDtos[k].getArtikelIId(),
						freigabeDerStuecklistenZuruecknehmen, hsBereitsBeruecksichtigteArtikel, theClientDto);
			}
		}

		return null;

	}

	public Integer createStklpruefplan(StklpruefplanDto dto, TheClientDto theClientDto) {

		try {

			Integer artikelIIdKontakt = null;
			if (dto.getStuecklistepositionIIdKontakt() != null) {
				artikelIIdKontakt = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdKontakt(),
						theClientDto).getArtikelIId();
			}

			Integer artikelIIdLitze = null;

			if (dto.getStuecklistepositionIIdLitze() != null) {
				artikelIIdLitze = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdLitze(),
						theClientDto).getArtikelIId();
			}

			Integer artikelIIdLitze2 = null;

			if (dto.getStuecklistepositionIIdLitze2() != null) {
				artikelIIdLitze2 = stuecklistepositionFindByPrimaryKey(dto.getStuecklistepositionIIdLitze2(),
						theClientDto).getArtikelIId();
			}

			pruefeObPruefplanInPruefkombinationVorhanden(dto.getStuecklisteId(), dto.getPruefartIId(),
					artikelIIdKontakt, artikelIIdLitze, artikelIIdLitze2, dto.getVerschleissteilIId(),
					dto.getPruefkombinationId(), true, theClientDto);

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STKLPRUEFPLAN);
			dto.setIId(pk);

			Integer i = null;
			try {
				Query querynext = em.createNamedQuery("StklpruefplanejbSelectNextReihung");
				querynext.setParameter(1, dto.getStuecklisteId());
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

			dto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			dto.setTAendern(new Timestamp(System.currentTimeMillis()));
			dto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			dto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Stklpruefplan stklpruefplan = new Stklpruefplan(dto.getIId(), dto.getStuecklisteId(), dto.getPruefartIId(),
					dto.getStuecklistepositionIIdKontakt(), dto.getBDoppelanschlag(), dto.getISort(),
					dto.getPersonalIIdAnlegen(), dto.getTAnlegen(), dto.getPersonalIIdAendern(), dto.getTAendern());
			em.persist(stklpruefplan);
			em.flush();
			setStklpruefplanFromStklpruefplanDto(stklpruefplan, dto);

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void updateApkommentar(ApkommentarDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();
		Apkommentar apkommentar = null;
		// try {
		apkommentar = em.find(Apkommentar.class, iId);

		// duplicateunique: Pruefung: Artikelgruppe bereits vorhanden.
		try {
			Query query = em.createNamedQuery("ApkommentarFindByCNrMandantCNr");
			query.setParameter(1, dto.getCNr());
			query.setParameter(2, theClientDto.getMandant());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Apkommentar) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("STK_APKOMMENTAR.UK"));

			}
		} catch (NoResultException ex) {

		}

		setApkommentarFromApkommentarDto(apkommentar, dto);
		try {
			if (dto.getApkommentarsprDto() != null) {
				try {
					Apkommentarspr apkommentarspr = em.find(Apkommentarspr.class,
							new ApkommentarsprPK(theClientDto.getLocUiAsString(), iId));
					if (apkommentarspr == null) {
						apkommentarspr = new Apkommentarspr(theClientDto.getLocUiAsString(),
								dto.getApkommentarsprDto().getCBez(), iId);
						em.persist(apkommentarspr);
						em.flush();
					}
					apkommentarspr.setCBez(dto.getApkommentarsprDto().getCBez());

				} catch (NoResultException ex) {
					Apkommentarspr apkommentarspr = new Apkommentarspr(theClientDto.getLocUiAsString(),
							dto.getApkommentarsprDto().getCBez(), iId);
					em.persist(apkommentarspr);

				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updatePruefartspr(PruefartDto dto, TheClientDto theClientDto) {
		try {
			if (dto.getPruefartsprDto() != null) {
				try {
					Pruefartspr pruefartspr = em.find(Pruefartspr.class,
							new PruefartsprPK(theClientDto.getLocUiAsString(), dto.getIId()));
					if (pruefartspr == null) {
						pruefartspr = new Pruefartspr(theClientDto.getLocUiAsString(),
								dto.getPruefartsprDto().getCBez(), dto.getIId());
						em.persist(pruefartspr);
						em.flush();
					}
					pruefartspr.setCBez(dto.getPruefartsprDto().getCBez());

				} catch (NoResultException ex) {
					Pruefartspr pruefartspr = new Pruefartspr(theClientDto.getLocUiAsString(),
							dto.getPruefartsprDto().getCBez(), dto.getIId());
					em.persist(pruefartspr);

				}
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeApkommentar(ApkommentarDto dto) {

		try {
			Query query = em.createNamedQuery("ApkommentarsprByApkommentarIId");
			query.setParameter(1, dto.getIId());
			Collection<?> allArtgruspr = query.getResultList();
			Iterator<?> iter = allArtgruspr.iterator();
			while (iter.hasNext()) {
				Apkommentarspr temp = (Apkommentarspr) iter.next();
				em.remove(temp);
			}
			Apkommentar apkommentar = em.find(Apkommentar.class, dto.getIId());

			em.remove(apkommentar);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

	}

	public ApkommentarDto apkommentarFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Apkommentar apkommentar = em.find(Apkommentar.class, iId);

		ApkommentarDto dto = ApkommentarDtoAssembler.createDto(apkommentar);
		ApkommentarsprDto apkommentarsprDto = null;
		Apkommentarspr apkommentarspr = em.find(Apkommentarspr.class,
				new ApkommentarsprPK(theClientDto.getLocUiAsString(), iId));
		if (apkommentarspr != null) {
			apkommentarsprDto = assembleApkommentarsprDto(apkommentarspr);
		}
		if (apkommentarsprDto == null) {
			apkommentarspr = em.find(Apkommentarspr.class,
					new ApkommentarsprPK(theClientDto.getLocKonzernAsString(), iId));
			if (apkommentarspr != null) {
				apkommentarsprDto = assembleApkommentarsprDto(apkommentarspr);
			}
		}
		dto.setApkommentarsprDto(apkommentarsprDto);
		return dto;
	}

	public PruefartDto pruefartFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Pruefart pruefart = em.find(Pruefart.class, iId);

		PruefartDto dto = PruefartDtoAssembler.createDto(pruefart);
		PruefartsprDto pruefartsprDto = null;
		Pruefartspr pruefartspr = em.find(Pruefartspr.class, new PruefartsprPK(theClientDto.getLocUiAsString(), iId));
		if (pruefartspr != null) {
			pruefartsprDto = PruefartsprDtoAssembler.createDto(pruefartspr);
		}
		if (pruefartsprDto == null) {
			pruefartspr = em.find(Pruefartspr.class, new PruefartsprPK(theClientDto.getLocKonzernAsString(), iId));
			if (pruefartspr != null) {
				pruefartsprDto = PruefartsprDtoAssembler.createDto(pruefartspr);
			}
		}
		dto.setPruefartsprDto(pruefartsprDto);
		return dto;
	}

	public PruefartDto pruefartFindByCNr(String cNr, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("PruefartFindByCNr");
		query.setParameter(1, cNr);
		Pruefart pruefart = (Pruefart) query.getSingleResult();
		return pruefartFindByPrimaryKey(pruefart.getIId(), theClientDto);
	}

	private ApkommentarsprDto assembleApkommentarsprDto(Apkommentarspr apkommentarspr) {
		return ApkommentarsprDtoAssembler.createDto(apkommentarspr);
	}

	private void setApkommentarFromApkommentarDto(Apkommentar apkommentar, ApkommentarDto apkommentarDto) {
		apkommentar.setCNr(apkommentarDto.getCNr());

		em.merge(apkommentar);
		em.flush();
	}

	private void setPruefkombinationFromPruefkombinationDto(Pruefkombination bean, PruefkombinationDto dto) {

		bean.setIId(dto.getIId());
		bean.setPruefartIId(dto.getPruefartIId());
		bean.setArtikelIIdKontakt(dto.getArtikelIIdKontakt());
		bean.setArtikelIIdLitze(dto.getArtikelIIdLitze());
		bean.setBStandard(dto.getBStandard());
		bean.setNCrimpbreitDraht(dto.getNCrimpbreitDraht());
		bean.setNCrimphoeheDraht(dto.getNCrimphoeheDraht());
		bean.setNCrimphoeheIsolation(dto.getNCrimphoeheIsolation());
		bean.setNCrimpbreiteIsolation(dto.getNCrimpbreiteIsolation());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());

		bean.setNToleranzCrimpbreitDraht(dto.getNToleranzCrimpbreitDraht());
		bean.setNToleranzCrimphoeheDraht(dto.getNToleranzCrimphoeheDraht());
		bean.setNToleranzCrimphoeheIsolation(dto.getNToleranzCrimphoeheIsolation());
		bean.setNToleranzCrimpbreiteIsolation(dto.getNToleranzCrimpbreiteIsolation());

		bean.setNToleranzWert(dto.getNToleranzWert());
		bean.setNWert(dto.getNWert());

		bean.setTAendern(dto.getTAendern());
		bean.setTAnlegen(dto.getTAnlegen());
		bean.setVerschleissteilIId(dto.getVerschleissteilIId());

		bean.setBDoppelanschlag(dto.getBDoppelanschlag());
		bean.setArtikelIIdLitze2(dto.getArtikelIIdLitze2());
		bean.setNAbzugskraftLitze(dto.getNAbzugskraftLitze());
		bean.setNAbzugskraftLitze2(dto.getNAbzugskraftLitze2());

		em.merge(bean);
		em.flush();
	}

	private void setStklpruefplanFromStklpruefplanDto(Stklpruefplan bean, StklpruefplanDto dto) {

		bean.setIId(dto.getIId());
		bean.setStuecklistepositionIIdKontakt(dto.getStuecklistepositionIIdKontakt());
		bean.setStuecklistepositionIIdLitze(dto.getStuecklistepositionIIdLitze());
		bean.setStuecklisteId(dto.getStuecklisteId());
		bean.setPruefartIId(dto.getPruefartIId());
		bean.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		bean.setPersonalIIdAnlegen(dto.getPersonalIIdAnlegen());

		bean.setTAendern(dto.getTAendern());
		bean.setTAnlegen(dto.getTAnlegen());
		bean.setISort(dto.getISort());
		bean.setBDoppelanschlag(dto.getBDoppelanschlag());
		bean.setStuecklistepositionIIdLitze2(dto.getStuecklistepositionIIdLitze2());
		bean.setVerschleissteilIId(dto.getVerschleissteilIId());
		bean.setPruefkombinationId(dto.getPruefkombinationId());

		em.merge(bean);
		em.flush();
	}

	public Integer createFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getCBez() == null || fertigungsgruppeDto.getISort() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("fertigungsgruppeDto.getCBez() == null || fertigungsgruppeDto.getISort() == null"));
		}
		fertigungsgruppeDto.setMandantCNr(theClientDto.getMandant());
		try {
			Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, fertigungsgruppeDto.getCBez());
			Fertigungsgruppe doppelt = (Fertigungsgruppe) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_FERTIGUNGSGRUPPE.C_BEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FERTIGUNGSGRUPPE);
		fertigungsgruppeDto.setIId(pk);

		try {
			Fertigungsgruppe fertigungsgruppe = new Fertigungsgruppe(fertigungsgruppeDto.getIId(),
					fertigungsgruppeDto.getMandantCNr(), fertigungsgruppeDto.getCBez(), fertigungsgruppeDto.getISort());
			em.persist(fertigungsgruppe);
			em.flush();
			setFertigungsgruppeFromFertigungsgruppeDto(fertigungsgruppe, fertigungsgruppeDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return fertigungsgruppeDto.getIId();
	}

	public Integer createKommentarimport(KommentarimportDto dto) {

		try {
			Query query = em.createNamedQuery("KommentarimportFindByBelegartCNr");
			query.setParameter(1, dto.getBelegartCNr());
			// @todo getSingleResult oder getResultList ?
			Kommentarimport doppelt = (Kommentarimport) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("STK_KOMMENTARIMPORT.BELEGART_C_NR"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		// Es darf nur eine Kommentarart geben
		Query artikelQ = em.createNamedQuery("KommentarimportFindAll");
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			if (!kiDtos[i].getArtikelkommentarartIId().equals(dto.getArtikelkommentarartIId())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STK_KOMMENTARIMPORT.ARTIKELKOMMENTARART_I_I_D"));
			}
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOMMENTARIMPORT);
			dto.setIId(pk);

			Kommentarimport bean = new Kommentarimport(dto.getIId(), dto.getBelegartCNr(),
					dto.getArtikelkommentarartIId());
			em.persist(bean);
			em.flush();
			setKommentarimportFromKommentarimportDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId) {

		Collection<Stuecklisteposition> clPos = listPositionenById(stuecklisteIId);
		for (Stuecklisteposition pos : clPos) {
			// Vorher Verweis in Arbeitsplan loeschen
			Query query = em.createNamedQuery("StuecklistearbeitsplanfindByStuecklistepositionIId");
			query.setParameter(1, pos.getIId());

			Collection<?> cl = query.getResultList();

			Iterator<?> it = cl.iterator();

			while (it.hasNext()) {
				Stuecklistearbeitsplan aplan = (Stuecklistearbeitsplan) it.next();
				aplan.setStuecklistepositionIId(null);
				em.merge(aplan);
				em.flush();
			}

			em.remove(pos);
		}
	}

	public void removeKommentarimport(KommentarimportDto dto) {
		Kommentarimport toRemove = em.find(Kommentarimport.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateKommentarimport(KommentarimportDto dto) {
		Kommentarimport ialle = em.find(Kommentarimport.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("KommentarimportFindByBelegartCNr");
			query.setParameter(1, dto.getBelegartCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Kommentarimport) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STK_KOMMENTARIMPORT.BELEGART_C_NR"));
			}
		} catch (NoResultException ex) {

		}

		// Es darf nur eine Kommentarart geben
		Query artikelQ = em.createNamedQuery("KommentarimportFindAll");
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			if (!kiDtos[i].getArtikelkommentarartIId().equals(dto.getArtikelkommentarartIId())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STK_KOMMENTARIMPORT.ARTIKELKOMMENTARART_I_I_D"));
			}
		}

		setKommentarimportFromKommentarimportDto(ialle, dto);
	}

	public KommentarimportDto kommentarimportFindByPrimaryKey(Integer iId) {
		Kommentarimport ialle = em.find(Kommentarimport.class, iId);
		return KommentarimportDtoAssembler.createDto(ialle);
	}

	public KommentarimportDto[] getAllkommentarimport() {

		Query query = em.createNamedQuery("KommentarimportFindAll");
		// @todo getSingleResult oder getResultList ?
		Collection c = query.getResultList();
		return KommentarimportDtoAssembler.createDtos(c);
	}

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto) throws EJBExceptionLP {
		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("fertigungsgruppeDto.getIId() == null"));
		}
		// try {
		Fertigungsgruppe toRemove = em.find(Fertigungsgruppe.class, fertigungsgruppeDto.getIId());
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
		// FEHLER_BEIM_LOESCHEN,
		// e);
		// }
	}

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getIId() == null || fertigungsgruppeDto.getCBez() == null
				|| fertigungsgruppeDto.getISort() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception(
					"fertigungsgruppeDto.getIId() == null || fertigungsgruppeDto.getCBez() == null || fertigungsgruppeDto.getISort() == null"));
		}
		Integer iId = fertigungsgruppeDto.getIId();
		Fertigungsgruppe fertigungsgruppe = null;
		// try {
		fertigungsgruppe = em.find(Fertigungsgruppe.class, iId);
		if (fertigungsgruppe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

		try {
			Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, fertigungsgruppeDto.getCBez());
			Integer iIdVorhanden = ((Fertigungsgruppe) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_FERTIGUNGSGRUPPE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setFertigungsgruppeFromFertigungsgruppeDto(fertigungsgruppe, fertigungsgruppeDto);

	}

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		Fertigungsgruppe fertigungsgruppe = em.find(Fertigungsgruppe.class, iId);
		if (fertigungsgruppe == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleFertigungsgruppeDto(fertigungsgruppe);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr(String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("mandantCNr == null"));
		}

		Map m = getEingeschraenkteFertigungsgruppen(theClientDto);

		if (m == null) {

			Query query2 = em.createNamedQuery("FertigungsgruppefindByMandantCNr");
			query2.setParameter(1, mandantCNr);
			Collection<?> cl = query2.getResultList();

			return assembleFertigungsgruppeDtos(cl);
		} else {
			FertigungsgruppeDto[] dtos = new FertigungsgruppeDto[m.size()];
			Iterator it = m.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Integer key = (Integer) it.next();
				dtos[i] = assembleFertigungsgruppeDto(em.find(Fertigungsgruppe.class, key));

				i++;
			}

			return dtos;

		}

	}

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBez(String mandantCNr, String cBez)
			throws EJBExceptionLP {
		FertigungsgruppeDto fertigungsgruppeDto = fertigungsgruppeFindByMandantCNrCBezOhneExc(mandantCNr, cBez);
		if (null == fertigungsgruppeDto) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return fertigungsgruppeDto;
	}

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBezOhneExc(String mandantCNr, String cBez) {
		Validator.notEmpty(mandantCNr, "mandantCNr");
		Validator.notEmpty(cBez, "cBez");

		Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
		query.setParameter(1, mandantCNr);
		query.setParameter(2, cBez);
		try {
			Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) query.getSingleResult();
			return assembleFertigungsgruppeDto(fertigungsgruppe);
		} catch (NoResultException e) {
			return null;
		}
	}

	private void setFertigungsgruppeFromFertigungsgruppeDto(Fertigungsgruppe fertigungsgruppe,
			FertigungsgruppeDto fertigungsgruppeDto) {
		fertigungsgruppe.setMandantCNr(fertigungsgruppeDto.getMandantCNr());
		fertigungsgruppe.setCBez(fertigungsgruppeDto.getCBez());
		fertigungsgruppe.setISort(fertigungsgruppeDto.getISort());
		fertigungsgruppe.setIFormularnummer(fertigungsgruppeDto.getIFormularnummer());
		em.merge(fertigungsgruppe);
		em.flush();
	}

	private FertigungsgruppeDto assembleFertigungsgruppeDto(Fertigungsgruppe fertigungsgruppe) {
		return FertigungsgruppeDtoAssembler.createDto(fertigungsgruppe);
	}

	private FertigungsgruppeDto[] assembleFertigungsgruppeDtos(Collection<?> fertigungsgruppes) {
		List<FertigungsgruppeDto> list = new ArrayList<FertigungsgruppeDto>();
		if (fertigungsgruppes != null) {
			Iterator<?> iterator = fertigungsgruppes.iterator();
			while (iterator.hasNext()) {
				Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) iterator.next();
				list.add(assembleFertigungsgruppeDto(fertigungsgruppe));
			}
		}
		FertigungsgruppeDto[] returnArray = new FertigungsgruppeDto[list.size()];
		return (FertigungsgruppeDto[]) list.toArray(returnArray);
	}

	public void createStuecklisteart(StuecklisteartDto stuecklisteartDto) throws EJBExceptionLP {
		if (stuecklisteartDto == null) {
			return;
		}
		try {
			Stuecklisteart stuecklisteart = new Stuecklisteart(stuecklisteartDto.getCNr(), stuecklisteartDto.getCBez(),
					stuecklisteartDto.getISort());
			em.persist(stuecklisteart);
			em.flush();
			setStuecklisteartFromStuecklisteartDto(stuecklisteart, stuecklisteartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeStuecklisteart(String cNr) throws EJBExceptionLP {
		// try {
		Stuecklisteart toRemove = em.find(Stuecklisteart.class, cNr);
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
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	public void removePruefkombination(Integer iId) {

		Query query = em.createNamedQuery("PruefkombinationsprsprByPruefkombinationIId");
		query.setParameter(1, iId);
		Collection<?> allArtgruspr = query.getResultList();
		Iterator<?> iter = allArtgruspr.iterator();
		while (iter.hasNext()) {
			Pruefkombinationspr temp = (Pruefkombinationspr) iter.next();
			em.remove(temp);
		}

		Pruefkombination toRemove = em.find(Pruefkombination.class, iId);
		em.remove(toRemove);
		em.flush();

	}

	public void removeStklpruefplan(Integer iId) {
		Stklpruefplan toRemove = em.find(Stklpruefplan.class, iId);
		em.remove(toRemove);
		em.flush();

	}

	public void removeStuecklisteart(StuecklisteartDto stuecklisteartDto) throws EJBExceptionLP {
		if (stuecklisteartDto != null) {
			String cNr = stuecklisteartDto.getCNr();
			removeStuecklisteart(cNr);
		}
	}

	public void updateStuecklisteart(StuecklisteartDto stuecklisteartDto) throws EJBExceptionLP {
		if (stuecklisteartDto != null) {
			String cNr = stuecklisteartDto.getCNr();
			try {
				Stuecklisteart stuecklisteart = em.find(Stuecklisteart.class, cNr);
				setStuecklisteartFromStuecklisteartDto(stuecklisteart, stuecklisteartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateStuecklistearts(StuecklisteartDto[] stuecklisteartDtos) throws EJBExceptionLP {
		if (stuecklisteartDtos != null) {
			for (int i = 0; i < stuecklisteartDtos.length; i++) {
				updateStuecklisteart(stuecklisteartDtos[i]);
			}
		}
	}

	public StuecklisteartDto stuecklisteartFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		Stuecklisteart stuecklisteart = em.find(Stuecklisteart.class, cNr);
		if (stuecklisteart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleStuecklisteartDto(stuecklisteart);
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }
		// catch (Exception e) {
		// throw new EJBException(e.getMessage());
		// }
	}

	private void setStuecklisteartFromStuecklisteartDto(Stuecklisteart stuecklisteart,
			StuecklisteartDto stuecklisteartDto) {
		stuecklisteart.setCBez(stuecklisteartDto.getCBez());
		stuecklisteart.setISort(stuecklisteartDto.getISort());
		em.merge(stuecklisteart);
		em.flush();
	}

	private StuecklisteartDto assembleStuecklisteartDto(Stuecklisteart stuecklisteart) {
		return StuecklisteartDtoAssembler.createDto(stuecklisteart);
	}

	private StuecklisteartDto[] assembleStuecklisteartDtos(Collection<?> stuecklistearts) {
		List<StuecklisteartDto> list = new ArrayList<StuecklisteartDto>();
		if (stuecklistearts != null) {
			Iterator<?> iterator = stuecklistearts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteart stuecklisteart = (Stuecklisteart) iterator.next();
				list.add(assembleStuecklisteartDto(stuecklisteart));
			}
		}
		StuecklisteartDto[] returnArray = new StuecklisteartDto[list.size()];
		return (StuecklisteartDto[]) list.toArray(returnArray);
	}

	public List<Integer> getSeriennrChargennrArtikelIIdsFromStueckliste(Integer stuecklisteIId, BigDecimal nmenge,
			TheClientDto theClientDto) throws EJBExceptionLP {
		List<Integer> itemsWithIdentities = new ArrayList<Integer>();
		List<StuecklisteMitStrukturDto> m = getStrukturDatenEinerStueckliste(stuecklisteIId, theClientDto,
				StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, false, false, nmenge, null,
				true, false);
		for (StuecklisteMitStrukturDto stuecklisteMitStrukturDto : m) {
			Integer artikelIId = stuecklisteMitStrukturDto.getStuecklistepositionDto().getArtikelIId();
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelIId, theClientDto);
			if (artikelDto.isChargennrtragend() || artikelDto.isSeriennrtragend()) {
				itemsWithIdentities.add(artikelDto.getIId());
			}
		}

		return itemsWithIdentities;
	}

	protected String getHandartikelBez(IStklImportResult r) {
		Map<String, String> m = r.getValues();
		String[] types = { StklImportSpezifikation.BEZEICHNUNG, StklImportSpezifikation.BEZEICHNUNG1,
				StklImportSpezifikation.BEZEICHNUNG2, StklImportSpezifikation.BEZEICHNUNG3,
				StklImportSpezifikation.ARTIKELNUMMER, StklImportSpezifikation.HERSTELLERARTIKELNUMMER,
				StklImportSpezifikation.KUNDENARTIKELNUMMER, StklImportSpezifikation.SI_WERT,
				StklImportSpezifikation.BAUFORM, StklImportSpezifikation.ARTIKELNUMMER };
		for (String type : types) {
			String bez = m.get(type);
			if (bez != null && !bez.isEmpty())
				return bez;
		}
		return null;
	}

	public void updateStuecklisteposition(StuecklistepositionDto originalDto, StuecklistepositionDto aenderungDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.notNull(originalDto, "originalDto");
		Validator.notNull(aenderungDto, "aenderungDto");

		StuecklistepositionDto rereadDto = stuecklistepositionFindByPrimaryKey(originalDto.getIId(), theClientDto);
		if (!rereadDto.equals(originalDto)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_MODIFIZIERT_UPDATE, originalDto.getIId().toString());
		}

		if (rereadDto.getIId().compareTo(aenderungDto.getIId()) != 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL, aenderungDto.getIId().toString());
		}

		rereadDto.setArtikelIId(aenderungDto.getArtikelIId());
		rereadDto.setArtikelDto(null);
		rereadDto.setNMenge(aenderungDto.getNMenge());
		rereadDto.setEinheitCNr(aenderungDto.getEinheitCNr());
		rereadDto.setNZielmenge(null);
		rereadDto.setCPosition(aenderungDto.getCPosition());
		rereadDto.setCKommentar(aenderungDto.getCKommentar());
		updateStuecklisteposition(rereadDto, theClientDto);
	}

	@Override
	public void removeStuecklisteposition(StuecklistepositionDto originalDto, StuecklistepositionDto aenderungDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.notNull(originalDto, "originalDto");
		Validator.notNull(aenderungDto, "aenderungDto");

		StuecklistepositionDto rereadDto = stuecklistepositionFindByPrimaryKey(originalDto.getIId(), theClientDto);
		if (!rereadDto.equals(originalDto)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_MODIFIZIERT_REMOVE, originalDto.getIId().toString());
		}

		removeStuecklisteposition(originalDto, theClientDto);
	}

	public StklagerentnahmeDto createStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto,
			TheClientDto theClientDto) {

		stklagerentnahmeDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_STKLAGERENTNAHME);
		stklagerentnahmeDto.setIId(iId);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("StklagerentnahmeejbSelectNextReihung");
			querynext.setParameter(1, stklagerentnahmeDto.getStuecklisteIId());
			i = (Integer) querynext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}
		stklagerentnahmeDto.setISort(i);

		try {
			Stklagerentnahme stklagerentnahme = new Stklagerentnahme(stklagerentnahmeDto.getIId(),
					stklagerentnahmeDto.getStuecklisteIId(), stklagerentnahmeDto.getLagerIId(),
					stklagerentnahmeDto.getISort(), stklagerentnahmeDto.getPersonalIIdAendern());
			em.persist(stklagerentnahme);
			em.flush();
			stklagerentnahmeDto.setTAendern(stklagerentnahme.getTAendern());
			setStklagerentnahmeFromStklagerentnahmeDto(stklagerentnahme, stklagerentnahmeDto);
			return stklagerentnahmeDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto, TheClientDto theClientDto) {

		if (stklagerentnahmeDto != null) {

			Query query = em.createNamedQuery("StklagerentnahmefindByStuecklisteIId");
			query.setParameter(1, stklagerentnahmeDto.getStuecklisteIId());
			Collection<?> cl = query.getResultList();

			Iterator it = cl.iterator();
			int iSort = 1;
			while (it.hasNext()) {

				Stklagerentnahme stklentnahme = (Stklagerentnahme) it.next();

				if (stklagerentnahmeDto != null && stklentnahme.getIId().equals(stklagerentnahmeDto.getIId())) {
					em.remove(stklentnahme);
					em.flush();
				} else {
					stklentnahme.setISort(iSort);
					iSort++;
				}

			}

		}

	}

	public StklagerentnahmeDto stklagerentnahmeFindByPrimaryKey(Integer iId) {

		Stklagerentnahme stklagerentnahme = em.find(Stklagerentnahme.class, iId);
		if (stklagerentnahme == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStklagerentnahmeDto(stklagerentnahme);

	}

	public StklagerentnahmeDto updateStklagerentnahme(StklagerentnahmeDto stklagerentnahmeDto,
			TheClientDto theClientDto) {
		Integer iId = stklagerentnahmeDto.getIId();

		Stklagerentnahme stklagerentnahme = em.find(Stklagerentnahme.class, iId);
		if (stklagerentnahme == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setStklagerentnahmeFromStklagerentnahmeDto(stklagerentnahme, stklagerentnahmeDto);
		return stklagerentnahmeDto;

	}

	public StklagerentnahmeDto[] stklagerentnahmeFindByStuecklisteIId(Integer stuecklisteIId) {
		// try {
		Query query = em.createNamedQuery("StklagerentnahmefindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();

		return StklagerentnahmeDtoAssembler.createDtos(cl);

	}

	private StklagerentnahmeDto assembleStklagerentnahmeDto(Stklagerentnahme stklagerentnahme) {
		return StklagerentnahmeDtoAssembler.createDto(stklagerentnahme);
	}

	private void setStklagerentnahmeFromStklagerentnahmeDto(Stklagerentnahme stklagerentnahme,
			StklagerentnahmeDto stklagerentnahmeDto) {
		stklagerentnahme.setStuecklisteIId(stklagerentnahmeDto.getStuecklisteIId());
		stklagerentnahme.setLagerIId(stklagerentnahmeDto.getLagerIId());
		stklagerentnahme.setISort(stklagerentnahmeDto.getISort());
		stklagerentnahme.setTAendern(stklagerentnahmeDto.getTAendern());
		stklagerentnahme.setPersonalIIdAendern(stklagerentnahmeDto.getPersonalIIdAendern());
		em.merge(stklagerentnahme);
		em.flush();
	}

	public void vertauscheStklagerentnahme(Integer iiDLagerentnahme1, Integer iIdLagerentnahme2) {

		Stklagerentnahme oLieferant1 = em.find(Stklagerentnahme.class, iiDLagerentnahme1);
		if (oLieferant1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stklagerentnahme oLieferant2 = em.find(Stklagerentnahme.class, iIdLagerentnahme2);
		if (oLieferant2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oLieferant1.getISort();
		Integer iSort2 = oLieferant2.getISort();
		// iSort der zweiten Loslagerentnahme auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oLieferant2.setISort(new Integer(-1));
		em.merge(oLieferant2);
		em.flush();
		oLieferant1.setISort(iSort2);
		oLieferant2.setISort(iSort1);

	}

	public void removeLockDerPruefkombinationWennIchIhnSperre(TheClientDto theClientDto) {
		try {

			String mandant = theClientDto.getMandant();

			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(StuecklisteFac.LOCKME_PRUEFKOMBINATION,
					mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					getTheJudgeFac().removeLockedObject(lockMeDtoLock[0]);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void pruefeBearbeitenDerPruefkombinationErlaubt(TheClientDto theClientDto) {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			lockMeDto.setCWas(mandant);
			lockMeDto.setCWer(StuecklisteFac.LOCKME_PRUEFKOMBINATION);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(StuecklisteFac.LOCKME_PRUEFKOMBINATION,
					mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PRUEFKOMBINATION_IST_GESPERRT, al,
							new Exception("Pruefkombination auf Mandant " + mandant + " gesperrt durch Personal Id "
									+ lockMeDtoLock[0].getPersonalIIdLocker()));
				}
			} else {
				// dann sperren
				lockMeDto.setPersonalIIdLocker(theClientDto.getIDPersonal());
				getTheJudgeFac().addLockedObject(lockMeDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void toggleFreigabe(Integer stuecklisteIId, TheClientDto theClientDto) {

		// WG logging
		StuecklisteDto stklDto = stuecklisteFindByPrimaryKey(stuecklisteIId, theClientDto);

		if (stklDto.getTFreigabe() == null) {
			stklDto.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			stklDto.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
		} else {

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ARTIKELFREIGABE,
					theClientDto)) {

				// PJ21640
				if (stklDto.getArtikelDto() != null && stklDto.getArtikelDto().getTFreigabe() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_STK_FREIGABE_RUECKGAENGIG_NUR_WENN_ARTIKEL_NICHT_FREIGEGEBEN,
							new Exception("FEHLER_STK_FREIGABE_RUECKGAENGIG_NUR_WENN_ARTIKEL_NICHT_FREIGEGEBEN"));
				}

			}

			stklDto.setTFreigabe(null);
			stklDto.setPersonalIIdFreigabe(null);
		}

		vergleicheStuecklisteDtoVorherNachherUndLoggeAenderungen(stklDto, theClientDto);

		Stueckliste stkl = em.find(Stueckliste.class, stuecklisteIId);
		if (stkl.getTFreigabe() == null) {
			stkl.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			stkl.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
			stklDto.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			stklDto.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
		} else {
			stkl.setTFreigabe(null);
			stkl.setPersonalIIdFreigabe(null);
			stklDto.setTFreigabe(null);
			stklDto.setPersonalIIdFreigabe(null);
		}

	}

	@Override
	public BelegpositionDto getNewPositionDto() {
		return new StuecklistepositionDto();
	}

	@Override
	public BelegpositionDto preparePositionDtoAusImportResult(BelegpositionDto posDto, StklImportSpezifikation spez,
			IStklImportResult result, TheClientDto theClientDto) {
		StuecklistepositionDto stklPosDto = ((StuecklistepositionDto) posDto);
		stklPosDto.setCPosition(Helper.cutString(result.getValues().get(FertigungsStklImportSpezifikation.POSITION),
				StuecklisteFac.FieldLength.STUECKLISTEPOSITION_POSITION));
		stklPosDto.setMontageartIId(((FertigungsStklImportSpezifikation) spez).getMontageartIId());
		stklPosDto.setBMitdrucken(Helper.boolean2Short(false));
		stklPosDto.setCKommentar(Helper.cutString(result.getValues().get(FertigungsStklImportSpezifikation.KOMMENTAR),
				StuecklisteFac.FieldLength.STUECKLISTEPOSITION_KOMMENTAR));

		HvOptional<String> lfdNummer = HvOptional
				.ofNullable(result.getValues().get(FertigungsStklImportSpezifikation.LAUFENDE_NUMMER));
		if (lfdNummer.isPresent() && Helper.istStringNumerisch(lfdNummer.get())) {
			stklPosDto.setILfdnummer(Integer.parseInt(lfdNummer.get()));
		}

		if (ArtikelFac.ARTIKELART_HANDARTIKEL.equals(result.getSelectedArtikelDto().getArtikelartCNr())) {
			stklPosDto.setSHandeingabe(getHandartikelBez(result));
			return stklPosDto;
		}

		try {
			ArtikelDto artikelDto = result.getSelectedArtikelDto();
			EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(artikelDto.getEinheitCNr(), theClientDto);
			Integer dimArtikel = einheitDto.getIDimension();
			if (dimArtikel == 0)
				return stklPosDto;

			stklPosDto.setEinheitCNr(dimArtikel == 1 ? SystemFac.EINHEIT_MILLIMETER
					: (dimArtikel == 2 ? SystemFac.EINHEIT_QUADRATMILLIMETER : SystemFac.EINHEIT_KUBIKMILLIMETER));

			HvOptional<BigDecimal> dimension = HvOptional.empty();
			if (dimArtikel > 0) {
				dimension = convertToBigDecimal(result, StklImportSpezifikation.DIM_BREITE);
				stklPosDto.setFDimension1(
						dimension.isPresent() ? dimension.get().floatValue() : BigDecimal.ZERO.floatValue());
			}

			if (dimArtikel > 1) {
				dimension = convertToBigDecimal(result, StklImportSpezifikation.DIM_HOEHE);
				stklPosDto.setFDimension2(
						dimension.isPresent() ? dimension.get().floatValue() : BigDecimal.ZERO.floatValue());
			}

			if (dimArtikel > 2) {
				dimension = convertToBigDecimal(result, StklImportSpezifikation.DIM_TIEFE);
				stklPosDto.setFDimension3(
						dimension.isPresent() ? dimension.get().floatValue() : BigDecimal.ZERO.floatValue());
			}
		} catch (EJBExceptionLP e) {
		} catch (RemoteException e) {
			myLogger.error("Fehler beim intelligenten Stkl.-Import", e);
		}

		return stklPosDto;
	}

	private HvOptional<BigDecimal> convertToBigDecimal(IStklImportResult result, String spezValue) {
		String value = result.getValues().get(spezValue);
		if (value == null)
			return HvOptional.empty();

		return HvOptional.ofNullable(Helper.toBigDecimal(value));
	}

	@Override
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		createStuecklistepositions(posDtos.toArray(new StuecklistepositionDto[posDtos.size()]), theClientDto);
	}

	@Override
	public Integer getBezugsobjektIIdDerStueckliste(StklImportSpezifikation spez, TheClientDto theClientDto)
			throws RemoteException {
		StuecklisteDto stuecklisteDto = stuecklisteFindByPrimaryKey(spez.getStklIId(), theClientDto);
		if (stuecklisteDto == null || stuecklisteDto.getPartnerIId() == null) {
			return null;
		}

		KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(stuecklisteDto.getPartnerIId(),
				theClientDto.getMandant(), theClientDto);

		return kundeDto == null ? null : kundeDto.getIId();
	}

	@Override
	public IImportPositionen asPositionImporter() {
		return this;
	}

	@Override
	public IImportHead asHeadImporter() {
		return this;
	}

	public ArrayList<Integer> getVorgeschlageneVerschleissteile(Integer artikelIIdKontakt, Integer artikelIIdLitze,
			Integer artikelIIdLitze2, boolean bDoppelanschlag) {
		ArrayList<Integer> al = new ArrayList<Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT distinct pk.verschleissteil_i_id FROM FLRPruefkombination pk WHERE pk.flrpruefart.c_nr IN ('"
				+ StuecklisteFac.PRUEFART_CRIMPEN_MIT_ISO + "','" + StuecklisteFac.PRUEFART_CRIMPEN_OHNE_ISO + "')";

		sQuery += " AND pk.artikel_i_id_kontakt=" + artikelIIdKontakt;
		sQuery += " AND pk.artikel_i_id_litze=" + artikelIIdLitze;

		if (bDoppelanschlag) {
			sQuery += " AND pk.artikel_i_id_litze2=" + artikelIIdLitze2;
		} else {
			sQuery += " AND pk.artikel_i_id_litze2 IS NULL ";
		}

		org.hibernate.Query query = session.createQuery(sQuery);

		List<?> results = query.list();

		Iterator it = results.iterator();

		while (it.hasNext()) {
			Integer i = (Integer) it.next();
			al.add(i);
		}

		return al;

	}

	public Integer createStuecklisteScriptart(StuecklisteScriptartDto stuecklisteScriptartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(stuecklisteScriptartDto, "stuecklisteScriptartDto");
		Validator.pkFieldNotNull(stuecklisteScriptartDto.getCBez(), "cbez");
		try {
			Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, stuecklisteScriptartDto.getCBez());
			StuecklisteScriptart doppelt = (StuecklisteScriptart) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_STUECKLISTESCRIPTART.C_BEZ"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTESCRIPTART);
		stuecklisteScriptartDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em.createNamedQuery("StuecklisteScriptartejbSelectNextReihung");
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
		stuecklisteScriptartDto.setISort(i);
		stuecklisteScriptartDto.setMandantCNr(theClientDto.getMandant());
		try {
			StuecklisteScriptart scriptart = new StuecklisteScriptart(stuecklisteScriptartDto.getIId(),
					stuecklisteScriptartDto.getCBez(), stuecklisteScriptartDto.getMandantCNr(),
					stuecklisteScriptartDto.getCScript(), stuecklisteScriptartDto.getISort());
			em.persist(scriptart);
			em.flush();
			setStuecklisteScriptartFromStuecklisteScriptartDto(scriptart, stuecklisteScriptartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return stuecklisteScriptartDto.getIId();
	}

	public void updateStuecklisteScriptart(StuecklisteScriptartDto scriptartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(scriptartDto, "scriptartDto");
		Validator.notNull(scriptartDto.getIId(), "scriptartDto.getIId()");
		Validator.notEmpty(scriptartDto.getCBez(), "scriptartDto.getCBez()");

		Integer iId = scriptartDto.getIId();
		StuecklisteScriptart scriptart = em.find(StuecklisteScriptart.class, iId);
		if (scriptart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		try {
			Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, scriptartDto.getCBez());
			Integer iIdVorhanden = ((StuecklisteScriptart) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STKL_STUECKLISTESCRIPTART.C_BEZ"));
			}
		} catch (NoResultException ex) {
			// nix
		}
		setStuecklisteScriptartFromStuecklisteScriptartDto(scriptart, scriptartDto);
	}

	public void removeStuecklisteScriptart(StuecklisteScriptartDto scriptartDto) throws EJBExceptionLP {
		Validator.notNull(scriptartDto, "scriptartDto");
		Validator.pkFieldNotNull(scriptartDto.getIId(), "scriptartDto.getIId()");

		StuecklisteScriptart toRemove = em.find(StuecklisteScriptart.class, scriptartDto.getIId());
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

	public StuecklisteScriptartDto[] stuecklisteScriptartFindByMandantCNr(TheClientDto theClientDto)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<StuecklisteScriptart> cl = query.getResultList();
		return assembleStuecklisteScriptartDtos(cl);
	}

	public StuecklisteScriptartDto stuecklisteScriptartFindByMandantCNrCBez(String cBez, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notEmpty(cBez, "cBez");

		Query query = em.createNamedQuery("StuecklisteScriptartfindByMandantCNrCBez");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cBez);
		StuecklisteScriptart scriptart = (StuecklisteScriptart) query.getSingleResult();
		if (scriptart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStuecklisteScriptartDto(scriptart);
	}

	public StuecklisteScriptartDto stuecklisteScriptartFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");
		StuecklisteScriptart scriptart = em.find(StuecklisteScriptart.class, iId);
		if (scriptart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return assembleStuecklisteScriptartDto(scriptart);
	}

	public PruefkombinationDto pruefkombinationFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {

		Pruefkombination bean = em.find(Pruefkombination.class, iId);
		PruefkombinationDto dto = PruefkombinationDtoAssembler.createDto(bean);

		PruefkombinationsprDto sprDto = null;
		Pruefkombinationspr spr = em.find(Pruefkombinationspr.class,
				new PruefkombinationsprPK(theClientDto.getLocUiAsString(), iId));
		if (spr != null) {
			sprDto = PruefkombinationsprDtoAssembler.createDto(spr);
		}
		if (sprDto == null) {
			spr = em.find(Pruefkombinationspr.class,
					new PruefkombinationsprPK(theClientDto.getLocKonzernAsString(), iId));
			if (spr != null) {
				sprDto = PruefkombinationsprDtoAssembler.createDto(spr);
			}
		}
		dto.setPruefkombinationsprDto(sprDto);

		return dto;
	}

	public StklpruefplanDto stklpruefplanFindByPrimaryKey(Integer iId) {

		Stklpruefplan bean = em.find(Stklpruefplan.class, iId);

		return StklpruefplanDtoAssembler.createDto(bean);
	}

	public StklpruefplanDto[] stklpruefplanFindByStuecklisteIId(Integer stuecklisteIId) {

		Query query = em.createNamedQuery("StklpruefplanFindByStuecklisteId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		return StklpruefplanDtoAssembler.createDtos(cl);
	}

	public void vertauscheStuecklisteScriptart(Integer iId1, Integer iId2) throws EJBExceptionLP {
		StuecklisteScriptart scriptart1 = em.find(StuecklisteScriptart.class, iId1);
		if (iId1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		StuecklisteScriptart scriptart2 = em.find(StuecklisteScriptart.class, iId2);
		if (iId2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = scriptart1.getISort();
		Integer iSort2 = scriptart2.getISort();

		scriptart2.setISort(new Integer(-1));

		scriptart1.setISort(iSort2);
		scriptart2.setISort(iSort1);

		em.merge(scriptart1);
		em.merge(scriptart2);
		em.flush();
	}

	public void createOrUpdatePositionsArbeitsplans(Integer stuecklisteId, StuecklistepositionDto[] positionDtos,
			StuecklistearbeitsplanDto[] arbeitsPlanDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.notNull(stuecklisteId, "stuecklisteId");
		Validator.notNull(positionDtos, "positionDtos");
		Validator.notNull(arbeitsPlanDtos, "arbeitsPlanDtos");

		removeAlleStuecklistenpositionen(stuecklisteId);
		// removeAlleStuecklistearbeitsplan(stuecklisteId, theClientDto);

		createStuecklistepositions(positionDtos, theClientDto);
		// createStuecklistearbeitsplans(arbeitsPlanDtos, theClientDto) ;
	}

	private StuecklisteScriptartDto assembleStuecklisteScriptartDto(StuecklisteScriptart scriptart) {
		return StuecklisteScriptartDtoAssembler.createDto(scriptart);
	}

	private StuecklisteScriptartDto[] assembleStuecklisteScriptartDtos(Collection<StuecklisteScriptart> scriptarts) {
		return StuecklisteScriptartDtoAssembler.createDtos(scriptarts);
	}

	private void setStuecklisteScriptartFromStuecklisteScriptartDto(StuecklisteScriptart scriptart,
			StuecklisteScriptartDto scriptartDto) {
		scriptart.setCBez(scriptartDto.getCBez());
		scriptart.setISort(scriptartDto.getISort());
		scriptart.setMandantCNr(scriptartDto.getMandantCNr());
		scriptart.setCScript(scriptartDto.getCScript());
		em.merge(scriptart);
		em.flush();
	}

	public FertigungsgruppeDto setupDefaultFertigungsgruppe(String cBez, TheClientDto theClientDto) {
		FertigungsgruppeDto fertigungsgruppeDto = new FertigungsgruppeDto();
		fertigungsgruppeDto.setCBez(cBez);
		fertigungsgruppeDto.setMandantCNr(theClientDto.getMandant());
		fertigungsgruppeDto.setISort(getNextFertigungsgruppe(theClientDto));
		return fertigungsgruppeDto;
	}

	public ApkommentarDto apkommentartFindByCNrMandantCNrOhneExc(String cNr, String mandantCNr) {
		Validator.notEmpty(mandantCNr, "mandantCNr");
		Validator.notEmpty(cNr, "cNr");

		Query query = em.createNamedQuery("ApkommentarFindByCNrMandantCNr");
		query.setParameter(1, cNr);
		query.setParameter(2, mandantCNr);
		try {
			Apkommentar apkommentar = (Apkommentar) query.getSingleResult();
			return ApkommentarDtoAssembler.createDto(apkommentar);
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public Map<String, String> getAllArbeitsgangarten() {
		Map<String, String> hmArbeitsgangarten = new TreeMap<String, String>();
		Query query = em.createNamedQuery("ArbeitsgangartfindAll");
		Collection<Arbeitsgangart> clArten = query.getResultList();
		if (clArten == null)
			return hmArbeitsgangarten;

		for (Arbeitsgangart agart : clArten) {
			hmArbeitsgangarten.put(agart.getCNr(), agart.getCNr());
		}

		return hmArbeitsgangarten;
	}

	public StklparameterDto stklparameterFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {

		StklparameterDto stklparameterDto = null;
		Stklparameter stklparameter = em.find(Stklparameter.class, iId);

		stklparameterDto = StklparameterDtoAssembler.createDto(stklparameter);
		// jetzt die Spr
		StklparametersprDto stklparametersprDto = null;

		try {
			StklparametersprPK nachrichtenartsprPK = new StklparametersprPK(theClientDto.getLocUiAsString(),
					stklparameterDto.getIId());
			Stklparameterspr stklparameterspr = em.find(Stklparameterspr.class, nachrichtenartsprPK);
			if (stklparameterspr != null) {
				stklparametersprDto = StklparametersprDtoAssembler.createDto(stklparameterspr);
			}
		} catch (Throwable t) {
			// ignore
		}
		stklparameterDto.setStklparametersprDto(stklparametersprDto);
		return stklparameterDto;
	}

	public StklparameterDto[] stklparameterFindByStuecklisteIId(Integer stuecklisteIId, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("StklparameterFindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> clArten = query.getResultList();
		return StklparameterDtoAssembler.createDtos(clArten);
	}

	public StklparameterDto stklparameterFindByStuecklisteIIdCNr(Integer stuecklisteIId, String cNr,
			TheClientDto theClientDto) {
		Query query = em.createNamedQuery("StklparameterFindByStuecklisteIIdCNr");
		query.setParameter(1, stuecklisteIId);
		query.setParameter(2, cNr);
		try {
			return StklparameterDtoAssembler.createDto((Stklparameter) query.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	public Integer createStklparameter(StklparameterDto dto, TheClientDto theClientDto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STKLPARAMETER);
			dto.setIId(pk);

			Query queryNext = em.createNamedQuery("StklparameterSelectNextReihung");
			queryNext.setParameter(1, dto.getStuecklisteIId());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			dto.setISort(i);

			Stklparameter stklparameter = new Stklparameter(dto.getIId(), dto.getStuecklisteIId(), dto.getCNr(),
					dto.getCTyp(), dto.getISort(), dto.getBCombobox(), dto.getBMandatory());

			setStklparameterFromStklparameterDto(stklparameter, dto);
			if (dto.getStklparametersprDto() != null) {
				Stklparameterspr spr = new Stklparameterspr(theClientDto.getLocUiAsString(), dto.getIId(),
						dto.getStklparametersprDto().getCBez());
				em.persist(spr);
				em.flush();
				setStklparametersprFromStklparametersprDto(spr, dto.getStklparametersprDto());
			}
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeStklparameter(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		// try {
		try {
			Query query = em.createNamedQuery("StklparametersprfindByStklparameterIId");
			query.setParameter(1, iId);
			Collection<?> allArtklaspr = query.getResultList();
			Iterator<?> iter = allArtklaspr.iterator();
			while (iter.hasNext()) {
				Stklparameterspr artklasprTemp = (Stklparameterspr) iter.next();
				em.remove(artklasprTemp);
			}
			Stklparameter artkla = em.find(Stklparameter.class, iId);
			if (artkla != null) {
				em.remove(artkla);
				em.flush();
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}

	}

	public void updateStklparameter(StklparameterDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		Stklparameter artkla = null;

		artkla = em.find(Stklparameter.class, iId);

		try {
			Query query = em.createNamedQuery("StklparameterFindByStuecklisteIIdCNr");
			query.setParameter(1, dto.getStuecklisteIId());
			query.setParameter(2, dto.getCNr());
			Integer iIdVorhanden = ((Stklparameter) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("STK_STKLPARAMETER.CNR"));
			}
		} catch (NoResultException ex) {

		}
		try {

			setStklparameterFromStklparameterDto(artkla, dto);

			if (dto.getStklparametersprDto() != null) {

				Stklparameterspr artklaspr = em.find(Stklparameterspr.class,
						new StklparametersprPK(theClientDto.getLocUiAsString(), iId));

				if (artklaspr == null) {
					artklaspr = new Stklparameterspr(theClientDto.getLocUiAsString(), iId,
							dto.getStklparametersprDto().getCBez());
					em.persist(artklaspr);
					em.flush();

				}
				setStklparametersprFromStklparametersprDto(artklaspr, dto.getStklparametersprDto());

			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setStklparametersprFromStklparametersprDto(Stklparameterspr stklparameterspr,
			StklparametersprDto shopgruppesprDto) {
		stklparameterspr.setCBez(shopgruppesprDto.getCBez());
		em.merge(stklparameterspr);
		em.flush();
	}

	private void setStklparameterFromStklparameterDto(Stklparameter stklparameter, StklparameterDto stklparameterDto) {
		stklparameter.setCNr(stklparameterDto.getCNr());
		stklparameter.setStuecklisteIId(stklparameterDto.getStuecklisteIId());
		stklparameter.setCTyp(stklparameterDto.getCTyp());
		stklparameter.setNMax(stklparameterDto.getNMax());
		stklparameter.setnMin(stklparameterDto.getNMin());
		stklparameter.setISort(stklparameterDto.getISort());
		stklparameter.setBCombobox(stklparameterDto.getBCombobox());
		stklparameter.setCBereich(stklparameterDto.getCBereich());
		stklparameter.setBMandatory(stklparameterDto.getBMandatory());
		stklparameter.setCWert(stklparameterDto.getCWert());

		em.merge(stklparameter);
		em.flush();
	}

	public boolean istArtikelArtikelset(Integer artikelId, String mandantCnr) {
		Validator.notNull(artikelId, "artikelId");
		StuecklisteDto dto = stuecklisteFindByArtikelIIdMandantCNrOhneExc(artikelId, mandantCnr);
		if (dto == null)
			return false;

		return StuecklisteFac.STUECKLISTEART_SETARTIKEL.equals(dto.getStuecklisteartCNr());
	}

	public Map<Integer, String> getAllPruefartenFuerPruefkombinationen(TheClientDto theClientDto) {
		Map<Integer, String> mapPruefarten = getAllPruefart(theClientDto);
		for (Integer pruefartId : getPruefartenOhnePruefkombination(theClientDto)) {
			mapPruefarten.remove(pruefartId);
		}
		return mapPruefarten;
	}

	public List<Integer> getPruefartenOhnePruefkombination(TheClientDto theClientDto) {
		List<Integer> pruefartenIds = new ArrayList<Integer>();
		PruefartDto pruefartMaterialstatusDto = pruefartFindByCNr(PRUEFART_MATERIALSTATUS, theClientDto);
		pruefartenIds.add(pruefartMaterialstatusDto.getIId());

		return pruefartenIds;
	}

	@Override
	public FlatPartlistInfo getFlatPartlistInfo(StuecklisteId stuecklisteId, TheClientDto theClientDto) {
		Validator.pkFieldNotNull(stuecklisteId, "stuecklisteId");

		Stueckliste stkl = em.find(Stueckliste.class, stuecklisteId.id());
		Validator.entityFound(stkl, stuecklisteId.id());

		Artikel stklArtikel = em.find(Artikel.class, stkl.getArtikelIId());
		Validator.entityFound(stklArtikel, stkl.getArtikelIId());

		FlatPartlistInfo flatPartlist = new FlatPartlistInfo();
		flatPartlist.setId(stuecklisteId);
		flatPartlist.setArtikelId(new ArtikelId(stkl.getArtikelIId()));
		flatPartlist.setArtikelCnr(stklArtikel.getCNr());
		flatPartlist.setPositions(getFlatPartlistPositionsImpl(stuecklisteId, theClientDto.getMandant()));

		return flatPartlist;
	}

	private List<FlatPartlistPositionInfo> getFlatPartlistPositionsImpl(StuecklisteId stuecklisteId,
			String mandantCnr) {
		List<FlatPartlistPositionInfo> positions = new ArrayList<FlatPartlistPositionInfo>();
		for (Stuecklisteposition pos : listPositionenById(stuecklisteId.id())) {
			Stueckliste stueckliste = StuecklisteQuery.findByArtikelIIdMandantCnr(em,
					new ArtikelId(pos.getArtikelIId()), mandantCnr);
			if (stueckliste != null) {
				if (StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE.equals(stueckliste.getStuecklisteartCNr())) {
					List<FlatPartlistPositionInfo> hsPositions = getFlatPartlistPositionsImpl(
							new StuecklisteId(stueckliste.getIId()), mandantCnr);
					Artikel hsArtikel = em.find(Artikel.class, stueckliste.getArtikelIId());
					hsPositions.forEach(p -> {
						p.setStklArtikelId(new ArtikelId(hsArtikel.getIId()));
						p.setStklArtikelCnr(hsArtikel.getCNr());
					});
					positions.addAll(hsPositions);
				}
			} else {
				Artikel artikel = em.find(Artikel.class, pos.getArtikelIId());
				FlatPartlistPositionInfo flatPosition = new FlatPartlistPositionInfo();
				flatPosition.setArtikelId(new ArtikelId(artikel.getIId()));
				flatPosition.setArtikelCnr(artikel.getCNr());

				positions.add(flatPosition);
			}
		}

		return positions;
	}
}
