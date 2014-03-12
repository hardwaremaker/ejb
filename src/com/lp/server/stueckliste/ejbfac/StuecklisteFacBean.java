/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelkommentar;
import com.lp.server.artikel.ejb.Artikelkommentardruck;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikelkommentarDtoAssembler;
import com.lp.server.artikel.service.ArtikelkommentarFac;
import com.lp.server.artikel.service.ArtikelkommentardruckDto;
import com.lp.server.artikel.service.ArtikelkommentarsprDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.benutzer.ejb.Fertigungsgrupperolle;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollmaterial;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.partner.ejb.Lfliefergruppe;
import com.lp.server.stueckliste.ejb.Fertigungsgruppe;
import com.lp.server.stueckliste.ejb.Kommentarimport;
import com.lp.server.stueckliste.ejb.Montageart;
import com.lp.server.stueckliste.ejb.Posersatz;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.ejb.Stuecklistearbeitsplan;
import com.lp.server.stueckliste.ejb.Stuecklisteart;
import com.lp.server.stueckliste.ejb.Stuecklisteeigenschaft;
import com.lp.server.stueckliste.ejb.Stuecklisteeigenschaftart;
import com.lp.server.stueckliste.ejb.Stuecklisteposition;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDtoAssembler;
import com.lp.server.stueckliste.service.KommentarimportDto;
import com.lp.server.stueckliste.service.KommentarimportDtoAssembler;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.MontageartDtoAssembler;
import com.lp.server.stueckliste.service.PosersatzDto;
import com.lp.server.stueckliste.service.PosersatzDtoAssembler;
import com.lp.server.stueckliste.service.StrukturierterImportDto;
import com.lp.server.stueckliste.service.StrukturierterImportSiemensNXDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteartDto;
import com.lp.server.stueckliste.service.StuecklisteartDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftDtoAssembler;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDtoAssembler;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.stueckliste.service.StuecklistepositionDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.logger.HvDtoLogger;
import com.lp.service.StuecklisteInfoDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class StuecklisteFacBean extends Facade implements StuecklisteFac {

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public Integer createMontageart(MontageartDto montageartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("montageartDto == null"));
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
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_MONTAGEART.C_BEZ"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MONATAGEART);
		montageartDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em
					.createNamedQuery("MontageartejbSelectNextReihung");
			querynext.setParameter(1, theClientDto.getMandant());
			i = (Integer) querynext.getSingleResult();
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		if (i == null) {
			i = new Integer(0);
		}
		i = new Integer(i.intValue() + 1);
		montageartDto.setISort(i);
		montageartDto.setMandantCNr(theClientDto.getMandant());
		try {
			Montageart montageart = new Montageart(montageartDto.getIId(),
					montageartDto.getCBez(), montageartDto.getMandantCNr(),
					montageartDto.getISort());
			em.persist(montageart);
			em.flush();
			setMontageartFromMontageartDto(montageart, montageartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return montageartDto.getIId();
	}

	public Integer createPosersatz(PosersatzDto posersatzDto,
			TheClientDto theClientDto) {

		if (posersatzDto.getArtikelIIdErsatz() == null
				|| posersatzDto.getStuecklistepositionIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"posersatzDto.getArtikelIIdErsatz() == null || posersatzDto.getStuecklistepositionIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("PosersatzfindByStuecklistepositionIIdArtikelIIdErsatz");
			query.setParameter(1, posersatzDto.getStuecklistepositionIId());
			query.setParameter(2, posersatzDto.getArtikelIIdErsatz());
			Posersatz doppelt = (Posersatz) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_POSERSATZ.UC"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_POSERSATZ);
		posersatzDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em
					.createNamedQuery("PosersatzejbSelectNextReihung");
			querynext.setParameter(1, posersatzDto.getStuecklistepositionIId());
			i = (Integer) querynext.getSingleResult();
		} catch (NoResultException ex) {
			// nothing here
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		if (i == null) {
			i = new Integer(0);
		}
		i = new Integer(i.intValue() + 1);
		posersatzDto.setISort(i);
		try {
			Posersatz posersatz = new Posersatz(posersatzDto.getIId(),
					posersatzDto.getStuecklistepositionIId(),
					posersatzDto.getArtikelIIdErsatz(), posersatzDto.getISort());
			em.persist(posersatz);
			em.flush();
			setPosersatzFromPosersatzDto(posersatz, posersatzDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return posersatzDto.getIId();
	}

	public void vertauscheStuecklisteposition(Integer iIdPosition1I,
			Integer iIdPosition2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Stuecklisteposition oPosition1 = em.find(Stuecklisteposition.class,
				iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklisteposition oPosition2 = em.find(Stuecklisteposition.class,
				iIdPosition2I);
		if (oPosition2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void vertauscheMontageart(Integer iIdMontageart1I,
			Integer iIdMontageart2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Montageart oMontageart1 = em.find(Montageart.class, iIdMontageart1I);
		if (oMontageart1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Montageart oMontageart2 = em.find(Montageart.class, iIdMontageart2I);
		if (oMontageart2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void vertauschePosersatz(Integer iIdPosersatz1I,
			Integer iIdPosersatz2I) {
		Posersatz oPosersatz1 = em.find(Posersatz.class, iIdPosersatz1I);
		if (oPosersatz1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Posersatz oPosersatz2 = em.find(Posersatz.class, iIdPosersatz2I);
		if (oPosersatz2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Integer iSort1 = oPosersatz1.getISort();
		Integer iSort2 = oPosersatz2.getISort();

		oPosersatz2.setISort(new Integer(-1));

		oPosersatz1.setISort(iSort2);
		oPosersatz2.setISort(iSort1);

	}

	public void vertauscheStuecklisteeigenschaftart(
			Integer iIdStuecklisteeigenschaftart1I,
			Integer iIdStuecklisteeigenschaftart2I) throws EJBExceptionLP {
		myLogger.entry();
		// try {
		Stuecklisteeigenschaftart oStuecklisteeigenschaftart1 = em
				.find(Stuecklisteeigenschaftart.class,
						iIdStuecklisteeigenschaftart1I);
		if (oStuecklisteeigenschaftart1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklisteeigenschaftart oStuecklisteeigenschaftart2 = em
				.find(Stuecklisteeigenschaftart.class,
						iIdStuecklisteeigenschaftart2I);
		if (oStuecklisteeigenschaftart2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void removeMontageart(MontageartDto montageartDto)
			throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("montageartDto == null"));
		}
		if (montageartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("montageartDto.getIId() == null"));
		}
		// try {
		Montageart toRemove = em.find(Montageart.class, montageartDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void removePosersatz(PosersatzDto posersatzDto) {

		Posersatz toRemove = em.find(Posersatz.class, posersatzDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateMontageart(MontageartDto montageartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (montageartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("montageartDto == null"));
		}
		if (montageartDto.getIId() == null || montageartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"montageartDto.getIId() == null || montageartDto.getCBez() == null"));
		}
		Integer iId = montageartDto.getIId();
		Montageart montageart = null;
		// try {
		montageart = em.find(Montageart.class, iId);
		if (montageart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

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
			Integer iIdVorhanden = ((Montageart) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_MONTAGEART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setMontageartFromMontageartDto(montageart, montageartDto);
	}

	public void updatePosersatz(PosersatzDto posersatzDto,
			TheClientDto theClientDto) {
		if (posersatzDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("montageartDto == null"));
		}
		if (posersatzDto.getIId() == null
				|| posersatzDto.getArtikelIIdErsatz() == null
				|| posersatzDto.getStuecklistepositionIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"posersatzDto.getIId() == null || posersatzDto.getArtikelIIdErsatz() == null || posersatzDto.getStuecklistepositionIId() == null"));
		}
		Integer iId = posersatzDto.getIId();
		Posersatz posersatz = em.find(Posersatz.class, iId);
		if (posersatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em
					.createNamedQuery("PosersatzfindByStuecklistepositionIIdArtikelIIdErsatz");
			query.setParameter(1, posersatzDto.getStuecklistepositionIId());
			query.setParameter(2, posersatzDto.getArtikelIIdErsatz());
			Integer iIdVorhanden = ((Posersatz) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_POSERSATZ.UC"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setPosersatzFromPosersatzDto(posersatz, posersatzDto);
	}

	public MontageartDto montageartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Montageart montageart = em.find(Montageart.class, iId);
		if (montageart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMontageartDto(montageart);
	}

	public PosersatzDto posersatzFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Posersatz posersatz = em.find(Posersatz.class, iId);
		if (posersatz == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assemblePosersatzDto(posersatz);

	}

	public PosersatzDto[] posersatzFindByStuecklistepositionIId(
			Integer stuecklistepositionIId) {

		Query query = em
				.createNamedQuery("PosersatzfindByStuecklistepositionIId");
		query.setParameter(1, stuecklistepositionIId);
		return assemblePosersatzDtos(query.getResultList());

	}

	public MontageartDto[] montageartFindByMandantCNr(TheClientDto theClientDto)
			throws EJBExceptionLP {
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

	public MontageartDto montageartFindByMandantCNrCBez(String cBez,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (cBez == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("cBez == null"));
		}
		// try {
		Query query = em.createNamedQuery("MontageartfindByMandantCNrCBez");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, cBez);
		Montageart montageart = (Montageart) query.getSingleResult();
		if (montageart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMontageartDto(montageart);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setMontageartFromMontageartDto(Montageart montageart,
			MontageartDto montageartDto) {
		montageart.setCBez(montageartDto.getCBez());
		montageart.setISort(montageartDto.getISort());
		montageart.setMandantCNr(montageartDto.getMandantCNr());
		em.merge(montageart);
		em.flush();
	}

	private void setKommentarimportFromKommentarimportDto(
			Kommentarimport kommentarimport,
			KommentarimportDto kommentarimportDto) {
		kommentarimport.setBelegartCNr(kommentarimportDto.getBelegartCNr());
		kommentarimport.setArtikelkommentarartIId(kommentarimportDto
				.getArtikelkommentarartIId());
		em.merge(kommentarimport);
		em.flush();
	}

	private void setPosersatzFromPosersatzDto(Posersatz posersatz,
			PosersatzDto posersatzDto) {
		posersatz.setArtikelIIdErsatz(posersatzDto.getArtikelIIdErsatz());
		posersatz.setISort(posersatzDto.getISort());
		posersatz.setStuecklistepositionIId(posersatzDto
				.getStuecklistepositionIId());
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

	public Integer createStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getCBez() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteeigenschaftartDto.getCBez() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, stuecklisteeigenschaftartDto.getCBez());
			Stuecklisteeigenschaftart doppelt = (Stuecklisteeigenschaftart) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_STUECKLISTEEIGENSCHAFTART.C_BEZ"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen
				.getNextPrimaryKey(PKConst.PK_STUECKLISTEEIGENSCHAFTART);
		stuecklisteeigenschaftartDto.setIId(pk);

		Integer i = null;
		try {
			Query querynext = em
					.createNamedQuery("StuecklisteeigenschaftartejbSelectNextReihung");
			i = (Integer) querynext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);

		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		stuecklisteeigenschaftartDto.setISort(i);

		try {
			Stuecklisteeigenschaftart stuecklisteeigenschaftart = new Stuecklisteeigenschaftart(
					stuecklisteeigenschaftartDto.getIId(),
					stuecklisteeigenschaftartDto.getCBez(),
					stuecklisteeigenschaftartDto.getISort());
			em.persist(stuecklisteeigenschaftart);
			em.flush();
			setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(
					stuecklisteeigenschaftart, stuecklisteeigenschaftartDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return stuecklisteeigenschaftartDto.getIId();

	}

	public void removeStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto)
			throws EJBExceptionLP {
		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteeigenschaftartDto.getIId() == null"));
		}
		// try {
		Stuecklisteeigenschaftart toRemove = em.find(
				Stuecklisteeigenschaftart.class,
				stuecklisteeigenschaftartDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updateStuecklisteeigenschaftart(
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftartDto == null"));
		}
		if (stuecklisteeigenschaftartDto.getIId() == null
				|| stuecklisteeigenschaftartDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteeigenschaftartDto.getIId() == null || stuecklisteeigenschaftartDto.getCBez() == null"));
		}

		Integer iId = stuecklisteeigenschaftartDto.getIId();
		// try {
		Stuecklisteeigenschaftart stuecklisteeigenschaftart = em.find(
				Stuecklisteeigenschaftart.class, iId);
		if (stuecklisteeigenschaftart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em
					.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, stuecklisteeigenschaftartDto.getCBez());
			Integer iIdVorhanden = ((Stuecklisteeigenschaftart) query
					.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_STUECKLISTEEIGENSCHAFTART.C_BEZ"));
			}

		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(
				stuecklisteeigenschaftart, stuecklisteeigenschaftartDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Stuecklisteeigenschaftart stuecklisteeigenschaftart = em.find(
				Stuecklisteeigenschaftart.class, iId);
		if (stuecklisteeigenschaftart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public StuecklisteeigenschaftartDto stuecklisteeigenschaftartFindByCBez(
			String cBez) throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("StuecklisteeigenschaftartfindByCBez");
			query.setParameter(1, cBez);
			Stuecklisteeigenschaftart stuecklisteeigenschaftart = (Stuecklisteeigenschaftart) query
					.getSingleResult();
			if (stuecklisteeigenschaftart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	private void setStuecklisteeigenschaftartFromStuecklisteeigenschaftartDto(
			Stuecklisteeigenschaftart stuecklisteeigenschaftart,
			StuecklisteeigenschaftartDto stuecklisteeigenschaftartDto) {
		stuecklisteeigenschaftart.setCBez(stuecklisteeigenschaftartDto
				.getCBez());
		stuecklisteeigenschaftart.setISort(stuecklisteeigenschaftartDto
				.getISort());
		em.merge(stuecklisteeigenschaftart);
		em.flush();
	}

	private StuecklisteeigenschaftartDto assembleStuecklisteeigenschaftartDto(
			Stuecklisteeigenschaftart stuecklisteeigenschaftart) {
		return StuecklisteeigenschaftartDtoAssembler
				.createDto(stuecklisteeigenschaftart);
	}

	private StuecklisteeigenschaftartDto[] assembleStuecklisteeigenschaftartDtos(
			Collection<?> stuecklisteeigenschaftarts) {
		List<StuecklisteeigenschaftartDto> list = new ArrayList<StuecklisteeigenschaftartDto>();
		if (stuecklisteeigenschaftarts != null) {
			Iterator<?> iterator = stuecklisteeigenschaftarts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteeigenschaftart stuecklisteeigenschaftart = (Stuecklisteeigenschaftart) iterator
						.next();
				list.add(assembleStuecklisteeigenschaftartDto(stuecklisteeigenschaftart));
			}
		}
		StuecklisteeigenschaftartDto[] returnArray = new StuecklisteeigenschaftartDto[list
				.size()];
		return (StuecklisteeigenschaftartDto[]) list.toArray(returnArray);
	}

	public Integer createStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getArtikelIId() == null
				|| stuecklisteDto.getBFremdfertigung() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getArtikelIId() == null || stuecklisteDto.getBFremdfertigung() == null"));
		}
		if (stuecklisteDto.getBAusgabeunterstueckliste() == null
				|| stuecklisteDto.getBMaterialbuchungbeiablieferung() == null
				|| stuecklisteDto.getStuecklisteartCNr() == null
				|| stuecklisteDto.getLagerIIdZiellager() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getBAusgabeunterstueckliste() == null || stuecklisteDto.getBMaterialbuchungbeiablieferung() == null || stuecklisteDto.getStuecklisteartCNr() == null || stuecklisteDto.getLagerIIdZiellager() == null"));
		}
		if (stuecklisteDto.getFertigungsgruppeIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getFertigungsgruppeIId() == null"));
		}
		if (stuecklisteDto.getIErfassungsfaktor() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getIErfassungsfaktor() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, stuecklisteDto.getArtikelIId());
			query.setParameter(2, theClientDto.getMandant());
			Stueckliste doppelt = (Stueckliste) query.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_STUECKLISTE.UC"));
			}
		} catch (NoResultException ex1) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTE);
		stuecklisteDto.setIId(pk);

		stuecklisteDto.setMandantCNr(theClientDto.getMandant());

		stuecklisteDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendernarbeitsplan(theClientDto
				.getIDPersonal());
		stuecklisteDto.setPersonalIIdAendernposition(theClientDto
				.getIDPersonal());

		stuecklisteDto.setTAnlegen(new java.sql.Timestamp(System
				.currentTimeMillis()));
		stuecklisteDto.setTAendernarbeitsplan(new java.sql.Timestamp(System
				.currentTimeMillis()));
		stuecklisteDto.setTAendernposition(new java.sql.Timestamp(System
				.currentTimeMillis()));
		stuecklisteDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		if (stuecklisteDto.getBDruckeinlagerstandsdetail() == null) {
			stuecklisteDto.setBDruckeinlagerstandsdetail(Helper
					.boolean2Short(false));
		}

		// Losgroesse defaultmaessig 1
		stuecklisteDto.setNLosgroesse(new java.math.BigDecimal(1));
		try {
			if (stuecklisteDto.getBUeberlieferbar() == null) {
				ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_STUECKLISTE,
								ParameterFac.PARAMETER_DEFAULT_UEBERLIEFERBAR);

				stuecklisteDto.setBUeberlieferbar(Helper
						.boolean2Short((Boolean) parameter.getCWertAsObject()));

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		try {
			Stueckliste stueckliste = new Stueckliste(stuecklisteDto.getIId(),
					stuecklisteDto.getArtikelIId(),
					stuecklisteDto.getMandantCNr(),
					stuecklisteDto.getBFremdfertigung(),
					stuecklisteDto.getNLosgroesse(),
					stuecklisteDto.getTAendernposition(),
					stuecklisteDto.getPersonalIIdAendernposition(),
					stuecklisteDto.getTAendernarbeitsplan(),
					stuecklisteDto.getPersonalIIdAendernarbeitsplan(),
					stuecklisteDto.getPersonalIIdAnlegen(),
					stuecklisteDto.getPersonalIIdAendern(),
					stuecklisteDto.getFertigungsgruppeIId(),
					stuecklisteDto.getStuecklisteartCNr(),
					stuecklisteDto.getBMaterialbuchungbeiablieferung(),
					stuecklisteDto.getBAusgabeunterstueckliste(),
					stuecklisteDto.getBUeberlieferbar(),
					stuecklisteDto.getIErfassungsfaktor(),
					stuecklisteDto.getLagerIIdZiellager(),
					stuecklisteDto.getBDruckeinlagerstandsdetail());
			em.persist(stueckliste);
			em.flush();
			setStuecklisteFromStuecklisteDto(stueckliste, stuecklisteDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		ParametermandantDto parameter;
		try {
			parameter = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_ARBEITSPLAN_DEFAULT_STUECKLISTE);

			String artikelnummer = parameter.getCWert();

			if (artikelnummer.trim().length() > 0) {
				ArtikelDto[] artikelDto = getArtikelFac()
						.artikelFindByCNrOhneExc(artikelnummer);
				if (artikelDto != null) {
					for (int i = 0; i < artikelDto.length; i++) {

						if (theClientDto.getMandant().equals(
								artikelDto[i].getMandantCNr())) {

							StuecklisteDto stklDto = stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelDto[i].getIId(), theClientDto);

							if (artikelDto[i] != null) {
								StuecklistearbeitsplanDto[] arbDtos = stuecklistearbeitsplanFindByStuecklisteIId(
										stklDto.getIId(), theClientDto);

								for (int j = 0; j < arbDtos.length; j++) {
									StuecklistearbeitsplanDto stuecklistearbeitsplanDto = arbDtos[j];
									stuecklistearbeitsplanDto.setIId(null);
									stuecklistearbeitsplanDto
											.setStuecklisteIId(stuecklisteDto
													.getIId());
									createStuecklistearbeitsplan(
											stuecklistearbeitsplanDto,
											theClientDto);
								}

							}

						}

					}
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		HvDtoLogger<StuecklisteDto> zeitdatenLogger = new HvDtoLogger<StuecklisteDto>(
				em, theClientDto);
		zeitdatenLogger.logInsert(stuecklisteDto);

		return stuecklisteDto.getIId();
	}

	public void removeStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getIId() == null"));
		}
		// try {
		Stueckliste toRemove = em.find(Stueckliste.class,
				stuecklisteDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklisteDto> stuecklisteLogger = new HvDtoLogger<StuecklisteDto>(
				em, theClientDto);
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

	public void updateStuecklisteLosgroesse(Integer stuecklisteIId,
			BigDecimal nLosgroesse) {
		// Lossgroesse updaten
		Stueckliste stueckliste = em.find(Stueckliste.class, stuecklisteIId);
		stueckliste.setNLosgroesse(nLosgroesse);
		em.merge(stueckliste);
		em.flush();
	}

	public void updateStueckliste(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null
				|| stuecklisteDto.getArtikelIId() == null
				|| stuecklisteDto.getBFremdfertigung() == null
				|| stuecklisteDto.getNLosgroesse() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getIId() == null || stuecklisteDto.getArtikelIId() == null || stuecklisteDto.getBFremdfertigung() == null || stuecklisteDto.getNLosgroesse() == null"));
		}
		if (stuecklisteDto.getBAusgabeunterstueckliste() == null
				|| stuecklisteDto.getBMaterialbuchungbeiablieferung() == null
				|| stuecklisteDto.getStuecklisteartCNr() == null
				|| stuecklisteDto.getLagerIIdZiellager() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getBAusgabeunterstueckliste() == null || stuecklisteDto.getBMaterialbuchungbeiablieferung() == null || stuecklisteDto.getStuecklisteartCNr() == null || stuecklisteDto.getLagerIIdZiellager() == null"));
		}

		if (stuecklisteDto.getFertigungsgruppeIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getFertigungsgruppeIId() == null"));
		}
		if (stuecklisteDto.getIErfassungsfaktor() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteDto.getIErfassungsfaktor() == null"));
		}
		if (stuecklisteDto.getNLosgroesse().doubleValue() <= 0) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_MUSS_GROESSER_0_SEIN, new Exception(
							"stuecklisteDto.getNLosgroesse().doubleValue()<=0"));

		}

		vergleicheStuecklisteDtoVorherNachherUndLoggeAenderungen(
				stuecklisteDto, theClientDto);

		Integer iId = stuecklisteDto.getIId();

		// try {
		Stueckliste stueckliste = em.find(Stueckliste.class, iId);
		if (stueckliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		try {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, stuecklisteDto.getArtikelIId());
			query.setParameter(2, theClientDto.getMandant());
			Integer iIdVorhanden = ((Stueckliste) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_STEUCKLISTE.UC"));
			}

		} catch (NoResultException ex) {
			//
		}
		stuecklisteDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		stuecklisteDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));
		setStuecklisteFromStuecklisteDto(stueckliste, stuecklisteDto);
	}

	private void vergleicheStuecklisteDtoVorherNachherUndLoggeAenderungen(
			StuecklisteDto stuecklisteDto, TheClientDto theClientDto) {
		StuecklisteDto stuecklisteDto_vorher = stuecklisteFindByPrimaryKey(
				stuecklisteDto.getIId(), theClientDto);

		HvDtoLogger<StuecklisteDto> stuecklisteLogger = new HvDtoLogger<StuecklisteDto>(
				em, theClientDto);
		stuecklisteLogger.log(stuecklisteDto_vorher, stuecklisteDto);
	}

	private void vergleicheStuecklistepositionDtoVorherNachherUndLoggeAenderungen(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) {
		StuecklistepositionDto stuecklistepositionDto_vorher = stuecklistepositionFindByPrimaryKey(
				stuecklistepositionDto.getIId(), theClientDto);

		HvDtoLogger<StuecklistepositionDto> stuecklisteLogger = new HvDtoLogger<StuecklistepositionDto>(
				em, stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		stuecklisteLogger.log(stuecklistepositionDto_vorher,
				stuecklistepositionDto);
	}

	private void vergleicheStuecklistearbeitsplanDtoVorherNachherUndLoggeAenderungen(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) {
		StuecklistearbeitsplanDto stuecklistearbeitsplanDto_vorher = stuecklistearbeitsplanFindByPrimaryKey(
				stuecklistearbeitsplanDto.getIId(), theClientDto);

		HvDtoLogger<StuecklistearbeitsplanDto> stuecklisteLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(
				em, stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		stuecklisteLogger.log(stuecklistearbeitsplanDto_vorher,
				stuecklistearbeitsplanDto);
	}

	/**
	 * Datet nur X_KOMMENTAR up
	 * 
	 * @param stuecklisteDto
	 *            stuecklisteDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateStuecklisteKommentar(StuecklisteDto stuecklisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteDto == null"));
		}
		if (stuecklisteDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteDto.getIId() == null"));
		}
		// try {
		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklisteDto.getIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		stueckliste.setXKommentar(stuecklisteDto.getXKommentar());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public StuecklisteDto stuecklisteFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		StuecklisteDto stuecklisteDto = stuecklisteFindByPrimaryKeyOhneExc(iId,
				theClientDto);

		if (stuecklisteDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		return stuecklisteDto;
	}

	public StuecklisteDto stuecklisteFindByPrimaryKeyOhneExc(Integer iId,
			TheClientDto theClientDto) {

		Stueckliste stueckliste = em.find(Stueckliste.class, iId);
		if (stueckliste == null) {
			return null;
		}
		StuecklisteDto stuecklisteDto = assembleStuecklisteDto(stueckliste);

		stuecklisteDto.setArtikelDto(getArtikelFac().artikelFindByPrimaryKey(
				stuecklisteDto.getArtikelIId(), theClientDto));
		if (stuecklisteDto.getAuftragIIdLeitauftrag() != null) {
			stuecklisteDto.setAuftragDto(getAuftragFac()
					.auftragFindByPrimaryKey(
							stuecklisteDto.getAuftragIIdLeitauftrag()));
		}
		return stuecklisteDto;
	}

	public boolean pruefeObArtikelStuecklistenstrukturSchonVorhanden(
			Integer stuecklisteIId_Wurzel, Integer stuecklisteIId_ZuSuchende,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteIId_Wurzel == null || stuecklisteIId_ZuSuchende == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteIId_Wurzel == null || stuecklisteIId_ZuSuchende == null"));
		}
		// Hole Alle Positionen der Wuzel
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId_Wurzel);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(
				FLRStuecklisteposition.class).add(
				Example.create(flrStuecklisteposition));

		List<?> results = crit.list();
		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results
				.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		session.close();
		for (int i = 0; i < returnArray.length; i++) {

			try {

				Query query = em
						.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stkl = (Stueckliste) query.getSingleResult();
				if (stkl.getIId().equals(stuecklisteIId_ZuSuchende)) {
					// Wenn Stueckliste gefinden wurde, dann true zurueckgeben
					return true;
				} else {
					// sonst in der naechsten Ebene weitersuchen
					return pruefeObArtikelStuecklistenstrukturSchonVorhanden(
							stkl.getIId(), stuecklisteIId_ZuSuchende,
							theClientDto);

				}
			} catch (NoResultException ex) {
				// Dann keine Stueckliste
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}
		}
		return false;
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer stueckliste, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		// try {

		Query query = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stueckliste);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Stuecklisteposition oPosition = (Stuecklisteposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public StuecklisteDto[] unterstuecklistenFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session.createCriteria(
				FLRStuecklisteposition.class).add(
				Example.create(flrStuecklisteposition));

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results
				.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);
		session.close();

		List<StuecklisteDto> l = new ArrayList<StuecklisteDto>();
		for (int i = 0; i < returnArray.length; i++) {

			try {
				Query query = em
						.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, returnArray[i].getFlrartikel().getI_id());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stkl = (Stueckliste) query.getSingleResult();
				if (stkl != null) {
					// try {
					Stueckliste stueckliste = em.find(Stueckliste.class,
							stkl.getIId());
					if (stueckliste == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

		}
		StuecklisteDto[] unterstuecklisten = new StuecklisteDto[l.size()];
		unterstuecklisten = (StuecklisteDto[]) l.toArray(unterstuecklisten);
		return unterstuecklisten;

	}

	public StuecklisteDto stuecklisteFindByArtikelIIdMandantCNrOhneExc(
			Integer artikelIId, String mandantCNr) {
		Query query = em
				.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
		query.setParameter(1, artikelIId);
		query.setParameter(2, mandantCNr);
		try {
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			return assembleStuecklisteDto(stueckliste);
		} catch (NoResultException ex) {
			return null;
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIId(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("artikelIId == null"));
		}

		try {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	public StuecklisteDto stuecklisteFindByMandantCNrArtikelIIdOhneExc(
			Integer iIdArtikelI, TheClientDto theClientDto) {

		if (iIdArtikelI == null) {
			return null;
		}

		try {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
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

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNr(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("StuecklistefindByPartnerIIdMandantCNr");
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

	public StuecklisteDto[] stuecklisteFindByPartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String mandantCNr, TheClientDto theClientDto) {
		// try {
		Query query = em
				.createNamedQuery("StuecklistefindByPartnerIIdMandantCNr");
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

	private void setStuecklisteFromStuecklisteDto(Stueckliste stueckliste,
			StuecklisteDto stuecklisteDto) {
		stueckliste.setArtikelIId(stuecklisteDto.getArtikelIId());
		stueckliste.setBFremdfertigung(stuecklisteDto.getBFremdfertigung());
		stueckliste.setBMaterialbuchungbeiablieferung(stuecklisteDto
				.getBMaterialbuchungbeiablieferung());
		stueckliste.setBAusgabeunterstueckliste(stuecklisteDto
				.getBAusgabeunterstueckliste());
		stueckliste.setAuftragIIdLeitauftrag(stuecklisteDto
				.getAuftragIIdLeitauftrag());
		stueckliste.setFertigungsgruppeIId(stuecklisteDto
				.getFertigungsgruppeIId());
		stueckliste.setNLosgroesse(stuecklisteDto.getNLosgroesse());
		stueckliste.setNDefaultdurchlaufzeit(stuecklisteDto
				.getNDefaultdurchlaufzeit());
		stueckliste.setPartnerIId(stuecklisteDto.getPartnerIId());
		stueckliste.setXKommentar(stuecklisteDto.getXKommentar());
		stueckliste.setTAendernarbeitsplan(stuecklisteDto
				.getTAendernarbeitsplan());
		stueckliste.setStuecklisteartCNr(stuecklisteDto.getStuecklisteartCNr());
		stueckliste.setIErfassungsfaktor(stuecklisteDto.getIErfassungsfaktor());
		stueckliste.setPersonalIIdAendernarbeitsplan(stuecklisteDto
				.getPersonalIIdAendernarbeitsplan());
		stueckliste.setTAendernposition(stuecklisteDto.getTAendernposition());
		stueckliste.setPersonalIIdAendernposition(stuecklisteDto
				.getPersonalIIdAendernposition());
		stueckliste.setTAendern(stuecklisteDto.getTAendern());
		stueckliste.setPersonalIIdAendern(stuecklisteDto
				.getPersonalIIdAendern());
		stueckliste.setLagerIIdZiellager(stuecklisteDto.getLagerIIdZiellager());
		stueckliste.setStuecklisteIIdEk(stuecklisteDto.getStuecklisteIIdEk());
		stueckliste.setBUeberlieferbar(stuecklisteDto.getBUeberlieferbar());
		stueckliste.setBDruckeinlagerstandsdetail(stuecklisteDto
				.getBDruckeinlagerstandsdetail());
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

	public Integer createStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftDto == null"));
		}
		if (stuecklisteeigenschaftDto.getCBez() == null
				|| stuecklisteeigenschaftDto.getStuecklisteIId() == null
				|| stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteeigenschaftDto.getCBez() == null || stuecklisteeigenschaftDto.getStuecklisteIId() == null || stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null"));
		}
		try {
			Query query = em
					.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIIdStuecklisteeigenschaftartIId");
			query.setParameter(1, stuecklisteeigenschaftDto.getStuecklisteIId());
			query.setParameter(2,
					stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId());
			Stuecklisteeigenschaft doppelt = (Stuecklisteeigenschaft) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_STUECKLISTEEIGENSCHAFT.UC"));
			}
		} catch (NoResultException ex) {
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEEIGENSCHAFT);
		stuecklisteeigenschaftDto.setIId(pk);
		stuecklisteeigenschaftDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		stuecklisteeigenschaftDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		try {
			Stuecklisteeigenschaft stuecklisteeigenschaft = new Stuecklisteeigenschaft(
					stuecklisteeigenschaftDto.getIId(),
					stuecklisteeigenschaftDto.getStuecklisteIId(),
					stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId(),
					stuecklisteeigenschaftDto.getCBez(),
					stuecklisteeigenschaftDto.getPersonalIIdAendern(),
					stuecklisteeigenschaftDto.getTAendern());
			em.persist(stuecklisteeigenschaft);
			em.flush();
			setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(
					stuecklisteeigenschaft, stuecklisteeigenschaftDto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return stuecklisteeigenschaftDto.getIId();

	}

	public void removeStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto)
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
		Stuecklisteeigenschaft toRemove = em.find(Stuecklisteeigenschaft.class,
				stuecklisteeigenschaftDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updateStuecklisteeigenschaft(
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklisteeigenschaftDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklisteeigenschaftDto == null"));
		}
		if (stuecklisteeigenschaftDto.getIId() == null
				|| stuecklisteeigenschaftDto.getCBez() == null
				|| stuecklisteeigenschaftDto.getStuecklisteIId() == null
				|| stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklisteeigenschaftDto.getIId() == null || stuecklisteeigenschaftDto.getCBez() == null || stuecklisteeigenschaftDto.getStuecklisteIId() == null || stuecklisteeigenschaftDto.getStuecklisteeigenschaftartIId() == null"));
		}
		stuecklisteeigenschaftDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		stuecklisteeigenschaftDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		Integer iId = stuecklisteeigenschaftDto.getIId();
		// try {
		Stuecklisteeigenschaft stuecklisteeigenschaft = em.find(
				Stuecklisteeigenschaft.class, iId);
		if (stuecklisteeigenschaft == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(
				stuecklisteeigenschaft, stuecklisteeigenschaftDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public StuecklisteeigenschaftDto stuecklisteeigenschaftFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Stuecklisteeigenschaft stuecklisteeigenschaft = em.find(
				Stuecklisteeigenschaft.class, iId);
		if (stuecklisteeigenschaft == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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
			Integer stuecklisteIId, Integer stuecklisteeigenschaftartIId)
			throws EJBExceptionLP {
		try {
			Query query = em
					.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIIdStuecklisteeigenschaftartIId");
			query.setParameter(1, stuecklisteIId);
			query.setParameter(2, stuecklisteeigenschaftartIId);
			Stuecklisteeigenschaft stuecklisteeigenschaft = (Stuecklisteeigenschaft) query
					.getSingleResult();
			if (stuecklisteeigenschaft == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleStuecklisteeigenschaftDto(stuecklisteeigenschaft);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
	}

	public StuecklisteeigenschaftDto[] stuecklisteeigenschaftFindByStuecklisteIId(
			Integer stuecklisteIId) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("StuecklisteeigenschaftfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, null);
		// }
		StuecklisteeigenschaftDto[] dtos = assembleStuecklisteeigenschaftDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			dtos[i].setStuecklisteeigenschaftartDto(assembleStuecklisteeigenschaftartDto(em
					.find(Stuecklisteeigenschaftart.class,
							dtos[i].getStuecklisteeigenschaftartIId())));
		}

		return dtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, e);
		// }
	}

	private void setStuecklisteeigenschaftFromStuecklisteeigenschaftDto(
			Stuecklisteeigenschaft stuecklisteeigenschaft,
			StuecklisteeigenschaftDto stuecklisteeigenschaftDto) {
		stuecklisteeigenschaft.setStuecklisteIId(stuecklisteeigenschaftDto
				.getStuecklisteIId());
		stuecklisteeigenschaft
				.setStuecklisteeigenschaftartIId(stuecklisteeigenschaftDto
						.getStuecklisteeigenschaftartIId());
		stuecklisteeigenschaft.setCBez(stuecklisteeigenschaftDto.getCBez());
		stuecklisteeigenschaft.setPersonalIIdAendern(stuecklisteeigenschaftDto
				.getPersonalIIdAendern());
		stuecklisteeigenschaft.setTAendern(stuecklisteeigenschaftDto
				.getTAendern());
		em.merge(stuecklisteeigenschaft);
		em.flush();
	}

	private StuecklisteeigenschaftDto assembleStuecklisteeigenschaftDto(
			Stuecklisteeigenschaft stuecklisteeigenschaft) {
		return StuecklisteeigenschaftDtoAssembler
				.createDto(stuecklisteeigenschaft);
	}

	private StuecklisteeigenschaftDto[] assembleStuecklisteeigenschaftDtos(
			Collection<?> stuecklisteeigenschafts) {
		List<StuecklisteeigenschaftDto> list = new ArrayList<StuecklisteeigenschaftDto>();
		if (stuecklisteeigenschafts != null) {
			Iterator<?> iterator = stuecklisteeigenschafts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteeigenschaft stuecklisteeigenschaft = (Stuecklisteeigenschaft) iterator
						.next();
				list.add(assembleStuecklisteeigenschaftDto(stuecklisteeigenschaft));
			}
		}
		StuecklisteeigenschaftDto[] returnArray = new StuecklisteeigenschaftDto[list
				.size()];
		return (StuecklisteeigenschaftDto[]) list.toArray(returnArray);
	}

	public Integer createStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getStuecklisteIId() == null
				|| stuecklistearbeitsplanDto.getArtikelIId() == null
				|| stuecklistearbeitsplanDto.getLRuestzeit() == null
				|| stuecklistearbeitsplanDto.getIArbeitsgang() == null
				|| stuecklistearbeitsplanDto.getLStueckzeit() == null
				|| stuecklistearbeitsplanDto.getBNurmaschinenzeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistearbeitsplanDto.getStuecklisteIId() == null || stuecklistearbeitsplanDto.getArtikelIId() == null || stuecklistearbeitsplanDto.getTRuestzeit() == null || stuecklistearbeitsplanDto.getIArbeitsgang() == null || stuecklistearbeitsplanDto.getTStueckzeit() == null || stuecklistearbeitsplanDto.getBNurmaschinenzeit()"));
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEARBEITSPLAN);
		stuecklistearbeitsplanDto.setIId(pk);

		ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(
				stuecklistearbeitsplanDto.getArtikelIId(), theClientDto);
		// PJ 16851
		if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {
			// PJ 16396
			if (stuecklistearbeitsplanDto.getAgartCNr() != null) {
				Query query = em
						.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
				query.setParameter(1,
						stuecklistearbeitsplanDto.getStuecklisteIId());
				query.setParameter(2,
						stuecklistearbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklistearbeitsplan ap = (Stuecklistearbeitsplan) it
							.next();
					if (ap.getAgartCNr() == null) {
						stuecklistearbeitsplanDto.setMaschineIId(ap
								.getMaschineIId());
						break;
					}
				}
			}
		}

		try {
			Stuecklistearbeitsplan stuecklistearbeitsplan = new Stuecklistearbeitsplan(
					stuecklistearbeitsplanDto.getIId(),
					stuecklistearbeitsplanDto.getStuecklisteIId(),
					stuecklistearbeitsplanDto.getIArbeitsgang(),
					stuecklistearbeitsplanDto.getArtikelIId(),
					stuecklistearbeitsplanDto.getLStueckzeit(),
					stuecklistearbeitsplanDto.getLRuestzeit(),
					stuecklistearbeitsplanDto.getBNurmaschinenzeit());
			em.persist(stuecklistearbeitsplan);
			em.flush();
			setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(
					stuecklistearbeitsplan, stuecklistearbeitsplanDto);

			Stueckliste stueckliste = em.find(Stueckliste.class,
					stuecklistearbeitsplanDto.getStuecklisteIId());
			if (stueckliste == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}

			stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto
					.getIDPersonal());
			stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System
					.currentTimeMillis()));

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		HvDtoLogger<StuecklistearbeitsplanDto> hvLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(
				em, stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		hvLogger.logInsert(stuecklistearbeitsplanDto);

		return stuecklistearbeitsplanDto.getIId();
	}

	public void removeStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistearbeitsplanDto.getIId() == null"));
		}

		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklistearbeitsplanDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Stuecklistearbeitsplan toRemove = em.find(Stuecklistearbeitsplan.class,
				stuecklistearbeitsplanDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklistearbeitsplanDto> hvLogger = new HvDtoLogger<StuecklistearbeitsplanDto>(
				em, stuecklistearbeitsplanDto.getStuecklisteIId(), theClientDto);
		hvLogger.logDelete(stuecklistearbeitsplanDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto
				.getIDPersonal());
		stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System
				.currentTimeMillis()));

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

	public void updateStuecklistearbeitsplan(
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistearbeitsplanDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistearbeitsplanDto == null"));
		}
		if (stuecklistearbeitsplanDto.getIId() == null
				|| stuecklistearbeitsplanDto.getStuecklisteIId() == null
				|| stuecklistearbeitsplanDto.getIArbeitsgang() == null
				|| stuecklistearbeitsplanDto.getArtikelIId() == null
				|| stuecklistearbeitsplanDto.getLRuestzeit() == null
				|| stuecklistearbeitsplanDto.getLStueckzeit() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistearbeitsplanDto.getIId() == null || stuecklistearbeitsplanDto.getStuecklisteIId() == null || stuecklistearbeitsplanDto.getIArbeitsgang() == null || stuecklistearbeitsplanDto.getArtikelIId() == null || stuecklistearbeitsplanDto.getTRuestzeit() == null || stuecklistearbeitsplanDto.getTStueckzeit() == null"));
		}

		vergleicheStuecklistearbeitsplanDtoVorherNachherUndLoggeAenderungen(
				stuecklistearbeitsplanDto, theClientDto);

		Integer iId = stuecklistearbeitsplanDto.getIId();
		// try {
		Stuecklistearbeitsplan stuecklistearbeitsplan = em.find(
				Stuecklistearbeitsplan.class, iId);
		if (stuecklistearbeitsplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		if (stuecklistearbeitsplanDto.getIMaschinenversatztage() != null) {

			int iVorher = 0;

			if (stuecklistearbeitsplan.getIMaschinenversatztage() != null) {
				iVorher = stuecklistearbeitsplan.getIMaschinenversatztage();
			}

			if (iVorher != stuecklistearbeitsplanDto.getIMaschinenversatztage()) {

				int delta = stuecklistearbeitsplanDto
						.getIMaschinenversatztage() - iVorher;

				if (delta != 0) {
					StuecklistearbeitsplanDto[] saDtos = stuecklistearbeitsplanFindByStuecklisteIId(
							stuecklistearbeitsplanDto.getStuecklisteIId(),
							theClientDto);

					for (int i = 0; i < saDtos.length; i++) {
						if (stuecklistearbeitsplanDto.getIId().equals(
								saDtos[i].getIId())) {

							for (int j = i + 1; j < saDtos.length; j++) {
								Stuecklistearbeitsplan stuecklistearbeitsplanTemp = em
										.find(Stuecklistearbeitsplan.class,
												saDtos[j].getIId());

								if (stuecklistearbeitsplanTemp
										.getIMaschinenversatztage() != null) {
									stuecklistearbeitsplanTemp
											.setIMaschinenversatztage(stuecklistearbeitsplanTemp
													.getIMaschinenversatztage()
													+ delta);

								} else {
									stuecklistearbeitsplanTemp
											.setIMaschinenversatztage(delta);
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

		setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(
				stuecklistearbeitsplan, stuecklistearbeitsplanDto);

		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklistearbeitsplanDto.getStuecklisteIId());

		stueckliste.setPersonalIIdAendernarbeitsplan(theClientDto
				.getIDPersonal());
		stueckliste.setTAendernarbeitsplan(new java.sql.Timestamp(System
				.currentTimeMillis()));

		em.merge(stueckliste);
		em.flush();

		// PJ 16396
		if (stuecklistearbeitsplanDto.getMaschineIId() != null
				&& stuecklistearbeitsplanDto.getAgartCNr() == null
				&& stuecklistearbeitsplanDto.getIArbeitsgang() != 0) {

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(
							stuecklistearbeitsplanDto.getArtikelIId(),
							theClientDto);
			// PJ 16851
			if (Helper.short2boolean(artikelDto.getbReinemannzeit()) == false) {

				Query query = em
						.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIIdIArbeitsgangnummer");
				query.setParameter(1, stueckliste.getIId());
				query.setParameter(2,
						stuecklistearbeitsplanDto.getIArbeitsgang());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklistearbeitsplan ap = (Stuecklistearbeitsplan) it
							.next();
					if (!ap.getIId().equals(stuecklistearbeitsplanDto.getIId())) {
						ArtikelDto artikelDtoPos = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										ap.getArtikelIId(), theClientDto);
						if (Helper.short2boolean(artikelDtoPos
								.getbReinemannzeit()) == false) {
							ap.setMaschineIId(stuecklistearbeitsplanDto
									.getMaschineIId());
						}
					}
				}
			}
		}

	}

	public void kopiereAusAgstkl(Integer agstklIId, Integer stuecklisteIId,
			TheClientDto theClientDto) {

		try {
			AgstklpositionDto[] dtos = getAngebotstklpositionFac()
					.agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			MontageartDto[] monatageartdtos = null;

			monatageartdtos = getStuecklisteFac().montageartFindByMandantCNr(
					theClientDto);

			for (int i = 0; i < dtos.length; i++) {
				AgstklpositionDto dto = dtos[i];

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						dto.getArtikelIId(), theClientDto);
				if (aDto.getArtikelartCNr().equals(
						ArtikelFac.ARTIKELART_HANDARTIKEL)
						|| aDto.getArtikelartCNr().equals(
								ArtikelFac.ARTIKELART_ARTIKEL)) {
					StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
					stuecklistepositionDto.setStuecklisteIId(stuecklisteIId);

					stuecklistepositionDto.setNMenge(dto.getNMenge());
					stuecklistepositionDto.setEinheitCNr(aDto.getEinheitCNr());
					stuecklistepositionDto.setBMitdrucken(dto.getBDrucken());
					if (aDto.getArtikelartCNr().equals(
							ArtikelFac.ARTIKELART_HANDARTIKEL)) {
						stuecklistepositionDto
								.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
						if (aDto.getArtikelsprDto() != null) {
							stuecklistepositionDto.setSHandeingabe(aDto
									.getArtikelsprDto().getCBez());
						}
					} else {
						stuecklistepositionDto
								.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stuecklistepositionDto.setArtikelIId(aDto.getIId());
					}

					if (monatageartdtos != null && monatageartdtos.length > 0) {
						stuecklistepositionDto
								.setMontageartIId(monatageartdtos[0].getIId());
					}

					createStuecklisteposition(stuecklistepositionDto,
							theClientDto);
				} else {
					StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
					stuecklistearbeitsplanDto.setArtikelIId(aDto.getIId());
					stuecklistearbeitsplanDto.setStuecklisteIId(stuecklisteIId);
					stuecklistearbeitsplanDto.setLRuestzeit(0L);
					stuecklistearbeitsplanDto.setLStueckzeit(dto.getNMenge()
							.multiply(new BigDecimal(60 * 60 * 1000))
							.longValue());
					stuecklistearbeitsplanDto.setBNurmaschinenzeit(Helper
							.boolean2Short(false));
					stuecklistearbeitsplanDto
							.setIArbeitsgang(getNextArbeitsgang(stuecklisteIId,
									theClientDto));

					createStuecklistearbeitsplan(stuecklistearbeitsplanDto,
							theClientDto);
				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public void kopiereStuecklistenPositionen(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId_Quelle);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		StuecklistepositionDto[] dtos = assembleStuecklistepositionDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			StuecklistepositionDto dto = dtos[i];
			dto.setStuecklisteIId(stuecklisteIId_Ziel);

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(dto.getArtikelIId(),
							theClientDto);
			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
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

	public void artikelErsetzten(Integer artikelIIdVon,
			Integer artikelIIdDurch, TheClientDto theClientDto) {

		// Zuerst arbeitsplan

		Query query = em
				.createNamedQuery("StuecklistearbeitsplanfindByArtikelIId");
		query.setParameter(1, artikelIIdVon);
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			Stuecklistearbeitsplan ap = (Stuecklistearbeitsplan) it.next();
			ap.setArtikelIId(artikelIIdDurch);

			Stueckliste stk = em
					.find(Stueckliste.class, ap.getStuecklisteIId());
			stk.setTAendernarbeitsplan(new Timestamp(System.currentTimeMillis()));
			stk.setPersonalIIdAendernarbeitsplan(theClientDto.getIDPersonal());

		}
		// Dann Material

		query = em.createNamedQuery("StuecklistepositionfindByArtikelIId");
		query.setParameter(1, artikelIIdVon);
		cl = query.getResultList();

		it = cl.iterator();
		while (it.hasNext()) {
			Stuecklisteposition pos = (Stuecklisteposition) it.next();
			pos.setArtikelIId(artikelIIdDurch);

			Stueckliste stk = em.find(Stueckliste.class,
					pos.getStuecklisteIId());
			stk.setTAendernposition(new Timestamp(System.currentTimeMillis()));
			stk.setPersonalIIdAendernposition(theClientDto.getIDPersonal());

		}

	}

	public ArrayList<ArtikelDto> importiereStuecklistenstrukturSiemensNX(
			ArrayList<StrukturierterImportSiemensNXDto> struktur,
			ArrayList<StrukturierterImportSiemensNXDto> listeFlach,
			Integer stuecklisteIId, TheClientDto theClientDto) {

		ArrayList<ArtikelDto> alNichtimportiert = importiereStuecklistenstrukturSiemensNX(
				struktur, stuecklisteIId, theClientDto, null, false);

		if (listeFlach != null && listeFlach.size() > 0) {
			// Vom EK-Vorschlag abziehen
			StuecklisteDto stklDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							listeFlach.get(0).getArtikelIId(), theClientDto);

			if (stklDto != null && stklDto.getStuecklisteIIdEk() != null) {

				Query query = em
						.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
				query.setParameter(1, stklDto.getStuecklisteIIdEk());
				Collection<?> cl = query.getResultList();
				Iterator it = cl.iterator();
				while (it.hasNext()) {
					Stuecklisteposition pos = (Stuecklisteposition) it.next();

					if (pos.getArtikelIId() != null) {
						BigDecimal posmenge = pos.getNMenge();
						for (int j = 0; j < listeFlach.size(); j++) {
							Integer artikelIId = listeFlach.get(j)
									.getArtikelIId();

							if (pos.getArtikelIId().equals(artikelIId)) {
								posmenge = posmenge.subtract(listeFlach.get(j)
										.getMenge());
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
			ArrayList<StrukturierterImportSiemensNXDto> struktur,
			Integer stuecklisteIId, TheClientDto theClientDto,
			ArrayList<ArtikelDto> alNichtimportiert,
			boolean bNichtmehrImportieren) {

		String defaultEinheit = null;
		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
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
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								artikelIId, theClientDto);

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

					ArrayList<StrukturierterImportSiemensNXDto> posNX = stkl
							.getPositionenSortiertNachArtikelIId();

					// Vergleichen, ob sich die Artikel veraendert haben

					if (stklPosSortierttnachArtikelId.length > 0) {
						bStuecklisteHatBereitsPositionen = true;
						if (stklPosSortierttnachArtikelId.length == posNX
								.size()) {
							for (int j = 0; j < stklPosSortierttnachArtikelId.length; j++) {

								double dBreiteVorhanden = 0;
								if (stklPosSortierttnachArtikelId[j]
										.getFDimension1() != null) {
									dBreiteVorhanden = Math
											.round(stklPosSortierttnachArtikelId[j]
													.getFDimension1() * 100.) / 100.;
								}
								double dLaengeVorhanden = 0;
								if (stklPosSortierttnachArtikelId[j]
										.getFDimension2() != null) {
									dLaengeVorhanden = Math
											.round(stklPosSortierttnachArtikelId[j]
													.getFDimension2() * 100.) / 100.;
								}

								if (!stklPosSortierttnachArtikelId[j]
										.getArtikelIId().equals(
												posNX.get(j).getArtikelIId())
										|| stklPosSortierttnachArtikelId[j]
												.getNMenge().doubleValue() != posNX
												.get(j).getMenge()
												.doubleValue()
										|| dBreiteVorhanden != posNX.get(j)
												.getDBreite()
										|| dLaengeVorhanden != posNX.get(j)
												.getDLaenge()) {

									// Fehlermeldung
									ArrayList al = new ArrayList();

									ArtikelDto artikelDtoStkl = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													stklDto.getArtikelIId(),
													theClientDto);
									al.add(artikelDtoStkl);

									ArtikelDto artikelDto = getArtikelFac()
											.artikelFindByPrimaryKeySmall(
													stklPosSortierttnachArtikelId[j]
															.getArtikelIId(),
													theClientDto);
									al.add(artikelDto);
									throw new EJBExceptionLP(
											EJBExceptionLP.FEHLER_IMPORT_NX_STKL_UNTERSCHIEDLICH,
											al, new Exception(""));
								}

							}
						} else {
							// Fehlermeldung
							ArrayList al = new ArrayList();

							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKeySmall(
											stklDto.getArtikelIId(),
											theClientDto);
							al.add(artikelDto);
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_IMPORT_NX_STKL_LAENGE_UNTERSCHIEDLICH,
									al, new Exception());

						}
					}

				} else {

					try {

						StuecklisteDto stuecklisteDto = new StuecklisteDto();
						stuecklisteDto.setArtikelIId(artikelIId);

						try {
							ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stuecklisteDto.setBAusgabeunterstueckliste(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stuecklisteDto
									.setBMaterialbuchungbeiablieferung(Helper
											.boolean2Short((Boolean) parameter
													.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = fertigungsgruppeFindByMandantCNr(
									theClientDto.getMandant(), theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stuecklisteDto
										.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
												.getIId());
							}
							stuecklisteDto.setLagerIIdZiellager(getLagerFac()
									.getHauptlagerDesMandanten(theClientDto)
									.getIId());
							stuecklisteDto.setNLosgroesse(new BigDecimal(1));
							stuecklisteDto.setIErfassungsfaktor(1);

							// Lt. Fr. Uhlig duerfen nur Hilfsstuecklisezn
							// angelegt werden
							stuecklisteDto
									.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_HILFSSTUECKLISTE);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						stuecklisteDto.setBFremdfertigung(Helper
								.boolean2Short(false));
						stklIId = getStuecklisteFac().createStueckliste(
								stuecklisteDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				importiereStuecklistenstrukturSiemensNX(stkl.getPositionen(),
						stklIId, theClientDto, alNichtimportiert,
						bStuecklisteHatBereitsPositionen);
			}
			if (stuecklisteIId != null && stkl.getMenge() != null
					&& bNichtmehrImportieren == false) {
				try {
					StuecklistepositionDto stklposDto = new StuecklistepositionDto();

					stklposDto.setStuecklisteIId(stuecklisteIId);
					stklposDto.setArtikelIId(artikelIId);
					stklposDto.setEinheitCNr(defaultEinheit);
					stklposDto.setNMenge(stkl.getMenge());
					stklposDto.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
					stklposDto.setBMitdrucken(Helper.boolean2Short(true));

					if (stkl.getDLaenge() != 0 && stkl.getDBreite() != 0) {
						stklposDto
								.setEinheitCNr(SystemFac.EINHEIT_QUADRATMILLIMETER);
						stklposDto.setFDimension1(new Float(stkl.getDBreite()));
						stklposDto.setFDimension2(new Float(stkl.getDLaenge()));
					} else {
						if (stkl.getDLaenge() != 0) {
							stklposDto
									.setEinheitCNr(SystemFac.EINHEIT_MILLIMETER);
							stklposDto.setFDimension1(new Float(stkl
									.getDLaenge()));
						}
					}

					MontageartDto[] dtos = montageartFindByMandantCNr(theClientDto);

					if (dtos != null && dtos.length > 0) {
						stklposDto.setMontageartIId(dtos[0].getIId());
					}

					getStuecklisteFac().createStuecklisteposition(stklposDto,
							theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

		}

		return alNichtimportiert;
	}

	@TransactionTimeout(5000)
	public ArrayList importiereStuecklistenstruktur(
			ArrayList<StrukturierterImportDto> struktur,
			Integer stuecklisteIId, TheClientDto theClientDto,
			boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag) {
		return importiereStuecklistenstruktur(struktur, stuecklisteIId,
				theClientDto, null, bAnfragevorschlagErzeugen,
				tLieferterminfuerAnfrageVorschlag, new BigDecimal(1));
	}

	public ArrayList importiereStuecklistenstruktur(
			ArrayList<StrukturierterImportDto> struktur,
			Integer stuecklisteIId, TheClientDto theClientDto,
			ArrayList alArtikelWirdAngelegt, boolean bAnfragevorschlagErzeugen,
			java.sql.Timestamp tLieferterminfuerAnfrageVorschlag,
			BigDecimal bdMultiplikatorFuerAnfragevorschlag) {

		if (alArtikelWirdAngelegt == null) {
			alArtikelWirdAngelegt = new ArrayList();
		}

		String defaultEinheit = null;
		try {
			ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT);

			defaultEinheit = parameter.getCWert();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		ArrayList<Stuecklisteposition> alVorhandeneStuecklistenpositionen = new ArrayList<Stuecklisteposition>();
		if (stuecklisteIId != null) {
			Query query = em
					.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
			query.setParameter(1, stuecklisteIId);
			Collection<Stuecklisteposition> cl = query.getResultList();
			alVorhandeneStuecklistenpositionen = new ArrayList<Stuecklisteposition>(
					cl);
		}
		for (int i = 0; i < struktur.size(); i++) {

			StrukturierterImportDto stkl = struktur.get(i);

			try {
				Query artikelQuery = em
						.createNamedQuery("ArtikelfindByCNrMandantCNr");
				artikelQuery.setParameter(1, stkl.getArtikelnr());
				artikelQuery.setParameter(2, theClientDto.getMandant());
				Artikel artikel = (Artikel) artikelQuery.getSingleResult();
			} catch (NoResultException e) {
				alArtikelWirdAngelegt.add(stkl.getArtikelnr());
			}

			try {
				if (stkl.getEinheitCNrZielmenge() != null) {
					EinheitDto ehtDto = getSystemFac()
							.einheitFindByPrimaryKeyOhneExc(
									stkl.getEinheitCNrZielmenge(), theClientDto);

					if (ehtDto == null) {
						ArrayList al = new ArrayList();
						al.add("Folgende Zielmengeneinheit ist nicht vorhanden: "
								+ stkl.getEinheitCNrZielmenge()
								+ ". Position "
								+ stkl.getPosnr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN,
								al,
								new Exception(
										"FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
					} else {
						boolean bFehlerInDimensionen = false;

						if (ehtDto.getIDimension() == 0
								&& (stkl.getDimension1() != null
										|| stkl.getDimension2() != null || stkl
										.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}
						if (ehtDto.getIDimension() == 1
								&& (stkl.getDimension1() == null
										|| stkl.getDimension2() != null || stkl
										.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}

						if (ehtDto.getIDimension() == 2
								&& (stkl.getDimension1() == null
										|| stkl.getDimension2() == null || stkl
										.getDimension3() != null)) {
							bFehlerInDimensionen = true;
						}

						if (ehtDto.getIDimension() == 3
								&& (stkl.getDimension1() == null
										|| stkl.getDimension2() == null || stkl
										.getDimension3() == null)) {
							bFehlerInDimensionen = true;
						}

						if (bFehlerInDimensionen) {
							ArrayList al = new ArrayList();
							al.add("Die Zielmengeneinheit "
									+ stkl.getEinheitCNrZielmenge()
									+ " erwartet "
									+ ehtDto.getIDimension()
									+ " Dimensionen. Diese sind jedoch nicht korrekt definiert. Position "
									+ stkl.getPosnr());
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN,
									al,
									new Exception(
											"FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
						}
					}

				} else {

					if (stkl.getDimension1() != null
							|| stkl.getDimension2() != null
							|| stkl.getDimension3() != null) {
						ArrayList al = new ArrayList();
						al.add("Es sind Dimensionen vorhanden, jedoch keine Zielmengeneinheit. Position "
								+ stkl.getPosnr());
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN,
								al,
								new Exception(
										"FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));

					}

				}
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Integer artikelIId = artikelFindenBzwNeuAnlegen(theClientDto,
					defaultEinheit, stkl);

			if (stkl.getPositionen() != null && stkl.getPositionen().size() > 0) {

				Integer stklIId = null;
				StuecklisteDto stklDto = getStuecklisteFac()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								artikelIId, theClientDto);

				if (stklDto != null) {
					stklIId = stklDto.getIId();
				} else {

					try {

						StuecklisteDto stuecklisteDto = new StuecklisteDto();
						stuecklisteDto.setArtikelIId(artikelIId);

						try {
							ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_UNTERSTUECKLISTEN_AUTOMATISCH_AUSGEBEN);

							stuecklisteDto.setBAusgabeunterstueckliste(Helper
									.boolean2Short((Boolean) parameter
											.getCWertAsObject()));

							parameter = (ParametermandantDto) getParameterFac()
									.getMandantparameter(
											theClientDto.getMandant(),
											ParameterFac.KATEGORIE_STUECKLISTE,
											ParameterFac.PARAMETER_DEFAULT_MATERIALBUCHUNG_BEI_ABLIEFERUNG);

							stuecklisteDto
									.setBMaterialbuchungbeiablieferung(Helper
											.boolean2Short((Boolean) parameter
													.getCWertAsObject()));

							FertigungsgruppeDto[] fertigungsgruppeDtos = fertigungsgruppeFindByMandantCNr(
									theClientDto.getMandant(), theClientDto);

							if (fertigungsgruppeDtos.length > 0) {
								stuecklisteDto
										.setFertigungsgruppeIId(fertigungsgruppeDtos[0]
												.getIId());
							}
							stuecklisteDto.setLagerIIdZiellager(getLagerFac()
									.getHauptlagerDesMandanten(theClientDto)
									.getIId());
							stuecklisteDto.setNLosgroesse(new BigDecimal(1));
							stuecklisteDto.setIErfassungsfaktor(1);

							stuecklisteDto
									.setStuecklisteartCNr(StuecklisteFac.STUECKLISTEART_STUECKLISTE);

						} catch (RemoteException e) {
							throwEJBExceptionLPRespectOld(e);
						}

						stuecklisteDto.setBFremdfertigung(Helper
								.boolean2Short(false));
						stklIId = getStuecklisteFac().createStueckliste(
								stuecklisteDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				alArtikelWirdAngelegt = importiereStuecklistenstruktur(
						stkl.getPositionen(), stklIId, theClientDto,
						alArtikelWirdAngelegt, bAnfragevorschlagErzeugen,
						tLieferterminfuerAnfrageVorschlag,
						bdMultiplikatorFuerAnfragevorschlag
								.multiply(new BigDecimal(stkl.getMenge())));
			}
			if (stuecklisteIId != null && stkl.getMenge() != null) {
				try {

					// Artikel suchen und nachsehen ob bereits in der
					// Stueckliste vorhanden

					String posEinheit = defaultEinheit;
					if (stkl.getEinheitCNrZielmenge() != null) {
						posEinheit = stkl.getEinheitCNrZielmenge();

						posEinheit = Helper.fitString2Length(posEinheit, 15,
								' ');
						ArtikelDto aDto = getArtikelFac()
								.artikelFindByPrimaryKey(artikelIId,
										theClientDto);
						try {
							getSystemFac().rechneUmInAndereEinheit(
									new BigDecimal(1), aDto.getEinheitCNr(),
									posEinheit, null, theClientDto);
						} catch (EJBExceptionLP e) {
							if (e.getCode() == EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT) {
								ArrayList alErr = e.getAlInfoForTheClient();
								String eht = "";
								if (alErr != null && alErr.size() > 0) {
									eht = alErr.get(0) + "";
								}
								ArrayList al = new ArrayList();
								al.add("Die Einheiten "
										+ eht
										+ " k\u00f6nnen nicht konvertiert werden. Position "
										+ stkl.getPosnr());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN,
										al,
										new Exception(
												"FEHLER_IMPORT_STUECKLISTENIMPORT_ALLGEMEIN"));
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
					for (int sp = 0; sp < alVorhandeneStuecklistenpositionen
							.size(); sp++) {
						Stuecklisteposition stklpos = alVorhandeneStuecklistenpositionen
								.get(sp);

						if (stklpos.getArtikelIId().equals(artikelIId)) {
							if (stklpos.getEinheitCNr().equals(posEinheit)) {

								if (stklpos.getFDimension1() != null
										&& !stklpos.getFDimension1().equals(
												dimension1Neu)) {
									continue;
								}
								if (stklpos.getFDimension2() != null
										&& !stklpos.getFDimension2().equals(
												dimension2Neu)) {
									continue;
								}
								if (stklpos.getFDimension3() != null
										&& !stklpos.getFDimension3().equals(
												dimension3Neu)) {
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
						stklposDto
								.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
						stklposDto.setBMitdrucken(Helper.boolean2Short(false));

						MontageartDto[] dtos = montageartFindByMandantCNr(theClientDto);

						if (dtos != null && dtos.length > 0) {
							stklposDto.setMontageartIId(dtos[0].getIId());
						}

						getStuecklisteFac().createStuecklisteposition(
								stklposDto, theClientDto);
					}

					if (bAnfragevorschlagErzeugen) {
						if (stkl.getMenge() != null
								&& stkl.getMenge().doubleValue() > 0) {
							getBestellvorschlagFac()
									.bestellvorschlagDtoErzeugen(
											null,
											theClientDto.getMandant(),
											artikelIId,
											null,
											null,
											tLieferterminfuerAnfrageVorschlag,
											new BigDecimal(stkl.getMenge())
													.multiply(bdMultiplikatorFuerAnfragevorschlag),
											null, theClientDto);
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
			Stuecklisteposition stklpos = alVorhandeneStuecklistenpositionen
					.get(i);

			Query queryVorhandene = em
					.createNamedQuery("StuecklistearbeitsplanfindByStuecklistepositionIId");
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

	public Integer artikelFindenBzwNeuAnlegen(TheClientDto theClientDto,
			String defaultEinheit, StrukturierterImportDto stkl) {
		Integer artikelIId = null;

		try {
			Query artikelQuery = em
					.createNamedQuery("ArtikelfindByCNrMandantCNr");
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
			 * stkl.getAnhaengeMitFileDatum().size() == 0) { // Fehler, da
			 * Normteil immer vorhanhen sein muss
			 * 
			 * ArrayList alDaten = new ArrayList();
			 * 
			 * alDaten.add(stkl.getArtikelnr());
			 * 
			 * throw new EJBExceptionLP(
			 * EJBExceptionLP.FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN,
			 * alDaten, new Exception(
			 * "FEHLER_IMPORT_NORMTEIL_MUSS_VORHANDEN_SEIN:" +
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

			String[] bezeichnungen = Helper.intelligenteWorttrennung(new int[] {
					40, 40 }, stkl.getArtikelbez());
			if (bezeichnungen != null) {
				oArtikelsprDto.setCBez(bezeichnungen[0]);
				oArtikelsprDto.setCZbez(bezeichnungen[1]);
			}

			if (stkl.getAbmessungen() != null
					&& stkl.getAbmessungen().length() > 0) {
				oArtikelsprDto.setCZbez2(stkl.getAbmessungen());
			}

			// Material
			artikelDto.setMaterialIId(stkl.getMaterial());
			artikelDto.setArtikelsprDto(oArtikelsprDto);
			if (stkl.getEinheitCNrZielmenge() != null) {
				artikelDto.setEinheitCNr(Helper.fitString2Length(
						stkl.getEinheitCNrZielmenge(), 15, ' '));
			} else {
				artikelDto.setEinheitCNr(defaultEinheit);
			}

			artikelDto.setFGewichtkg(stkl.getGewicht());

			// Liefergruppe
			if (stkl.getLiefergruppe() != null
					&& stkl.getLiefergruppe().length() > 0) {

				Integer liefergruppeIId = null;
				try {
					Query lfq = em
							.createNamedQuery("LfliefergruppeFindByCNrMandantCNr");
					lfq.setParameter(1, stkl.getLiefergruppe());
					lfq.setParameter(2, theClientDto.getMandant());
					Lfliefergruppe lfliefergruppe = (Lfliefergruppe) lfq
							.getSingleResult();
					liefergruppeIId = lfliefergruppe.getIId();
				} catch (NoResultException e) {
					// Wenns eine Liefergruppe nicht gibt, dann Fehler
					ArrayList alInfo = new ArrayList();
					alInfo.add(stkl.getLiefergruppe());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN,
							alInfo,
							new Exception(
									"FEHLER_LIEFERGUPPE_NICHT_ANGELEGT_IMPORT_ABGEBROCHEN"));
				}
				artikelDto.setLfliefergruppeIId(liefergruppeIId);
			}

			artikelIId = getArtikelFac()
					.createArtikel(artikelDto, theClientDto);

		}

		// Zuerst alle Anhaenge loeschen damit immer die aktuellsten eingetragen
		// sind

		// Eventuell vorhandene Anhaenge loeschen
		Query artikelQ = em.createNamedQuery("KommentarimportFindAll");
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler
				.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			try {
				Query artikelkommenentarVorhanden = em
						.createNamedQuery("ArtikelkommentarfindByArtikelIIdArtikelkommentarartIId");
				artikelkommenentarVorhanden.setParameter(1, artikelIId);
				artikelkommenentarVorhanden.setParameter(2,
						kiDtos[i].getArtikelkommentarartIId());
				Collection vorhanden = artikelkommenentarVorhanden
						.getResultList();
				Iterator itKomm = vorhanden.iterator();
				while (itKomm.hasNext()) {
					Artikelkommentar ak = (Artikelkommentar) itKomm.next();
					ArtikelkommentarDto aDto = getArtikelkommentarFac()
							.artikelkommentarFindByPrimaryKey(ak.getIId(),
									theClientDto);
					getArtikelkommentarFac().removeArtikelkommentar(aDto);
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}

		}

		// Anhaenge
		if (stkl.getAnhaengeMitFileDatum() != null
				&& stkl.getAnhaengeMitFileDatum().size() > 0) {
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
				kommDto.setArtikelkommentarartIId(kiDtos[i]
						.getArtikelkommentarartIId());
				kommDto.setBDefaultbild(Helper.boolean2Short(false));

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
					artikelkommenentarVorhanden.setParameter(2,
							kiDtos[i].getArtikelkommentarartIId());
					artikelkommenentarVorhanden.setParameter(3, datenformat);
					Artikelkommentar vorhanden = (Artikelkommentar) artikelkommenentarVorhanden
							.getSingleResult();
					if (vorhanden != null) {
						// Artikelkommentardruck updaten
						try {
							Query artikelkommenentarDruckVorhanden = em
									.createNamedQuery("ArtikelkommentardruckfindByArtikelIIdArtikelkommentarIIdBelegartCNr");
							artikelkommenentarDruckVorhanden.setParameter(1,
									artikelIId);
							artikelkommenentarDruckVorhanden.setParameter(2,
									vorhanden.getIId());
							artikelkommenentarDruckVorhanden.setParameter(3,
									kiDtos[i].getBelegartCNr());
							Artikelkommentardruck druckVorhanden = (Artikelkommentardruck) artikelkommenentarDruckVorhanden
									.getSingleResult();
						} catch (NoResultException ex) {
							// neu hinzufuegen
							ArtikelkommentardruckDto druckDto = new ArtikelkommentardruckDto();
							druckDto.setBelegartCNr(kiDtos[i].getBelegartCNr());
							druckDto.setArtikelkommentarIId(vorhanden.getIId());
							druckDto.setArtikelIId(artikelIId);
							try {
								getArtikelkommentarFac()
										.createArtikelkommentardruck(druckDto);
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
						getArtikelkommentarFac().createArtikelkommentar(
								kommDto, theClientDto);
					} catch (RemoteException ex3) {

						throwEJBExceptionLPRespectOld(ex3);
					}
				}

			}

		}
		return artikelIId;
	}

	public void kopiereStuecklisteArbeitsplan(Integer stuecklisteIId_Quelle,
			Integer stuecklisteIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"stuecklisteIId_Quelle == null || stuecklisteIId_Ziel == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
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

			// lt. WH 2010-09-06 bei P&S muss der Arbeitsgang erhalten bleiben
			/*
			 * Integer iArbeitsgang = getNextArbeitsgang(stuecklisteIId_Ziel,
			 * theClientDto);
			 * 
			 * if (iArbeitsgang == null) { iArbeitsgang = new Integer(10); }
			 * 
			 * dto.setIArbeitsgang(iArbeitsgang); // Damit automatisch
			 * drangehaengt
			 */
			// wird
			createStuecklistearbeitsplan(dto, theClientDto);
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);

		// }
	}

	public StuecklistearbeitsplanDto stuecklistearbeitsplanFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Stuecklistearbeitsplan stuecklistearbeitsplan = em.find(
				Stuecklistearbeitsplan.class, iId);
		if (stuecklistearbeitsplan == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		StuecklistearbeitsplanDto stuecklistearbeitsplanDto = assembleStuecklistearbeitsplanDto(stuecklistearbeitsplan);

		stuecklistearbeitsplanDto
				.setArtikelDto(getArtikelFac()
						.artikelFindByPrimaryKey(
								stuecklistearbeitsplanDto.getArtikelIId(),
								theClientDto));

		return stuecklistearbeitsplanDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }

	}

	public Integer getNextArbeitsgang(Integer stuecklisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklisteIId == null"));
		}
		try {
			Integer i = null;
			try {
				Query querynext = em
						.createNamedQuery("StuecklistearbeitsplanejbSelectNextReihung");
				querynext.setParameter(1, stuecklisteIId);
				i = (Integer) querynext.getSingleResult();
				if (i == null) {
					return new Integer(10);
				}

				if (i != null) {
					ParametermandantDto parameter = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_STUECKLISTE,
									ParameterFac.STUECKLISTE_ERHOEHUNG_ARBEITSGANG);
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	public Integer getNextFertigungsgruppe(TheClientDto theClientDto)
			throws EJBExceptionLP {

		try {
			Query querynext = em
					.createNamedQuery("FertigungsgruppeejbSelectNextReihung");
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

	}

	private void setStuecklistearbeitsplanFromStuecklistearbeitsplanDto(
			Stuecklistearbeitsplan stuecklistearbeitsplan,
			StuecklistearbeitsplanDto stuecklistearbeitsplanDto) {
		stuecklistearbeitsplan.setStuecklisteIId(stuecklistearbeitsplanDto
				.getStuecklisteIId());
		stuecklistearbeitsplan.setIArbeitsgang(stuecklistearbeitsplanDto
				.getIArbeitsgang());
		stuecklistearbeitsplan.setArtikelIId(stuecklistearbeitsplanDto
				.getArtikelIId());
		stuecklistearbeitsplan.setLStueckzeit(stuecklistearbeitsplanDto
				.getLStueckzeit());
		stuecklistearbeitsplan.setLRuestzeit(stuecklistearbeitsplanDto
				.getLRuestzeit());
		stuecklistearbeitsplan.setCKommentar(stuecklistearbeitsplanDto
				.getCKommentar());
		stuecklistearbeitsplan.setXLangtext(stuecklistearbeitsplanDto
				.getXLangtext());
		stuecklistearbeitsplan.setMaschineIId(stuecklistearbeitsplanDto
				.getMaschineIId());
		stuecklistearbeitsplan.setIAufspannung(stuecklistearbeitsplanDto
				.getIAufspannung());
		stuecklistearbeitsplan.setAgartCNr(stuecklistearbeitsplanDto
				.getAgartCNr());
		stuecklistearbeitsplan.setIUnterarbeitsgang(stuecklistearbeitsplanDto
				.getIUnterarbeitsgang());
		stuecklistearbeitsplan
				.setIMaschinenversatztage(stuecklistearbeitsplanDto
						.getIMaschinenversatztage());

		stuecklistearbeitsplan.setBNurmaschinenzeit(stuecklistearbeitsplanDto
				.getBNurmaschinenzeit());
		stuecklistearbeitsplan
				.setStuecklistepositionIId(stuecklistearbeitsplanDto
						.getStuecklistepositionIId());

		em.merge(stuecklistearbeitsplan);
		em.flush();
	}

	private StuecklistearbeitsplanDto assembleStuecklistearbeitsplanDto(
			Stuecklistearbeitsplan stuecklistearbeitsplan) {
		return StuecklistearbeitsplanDtoAssembler
				.createDto(stuecklistearbeitsplan);
	}

	private StuecklistearbeitsplanDto[] assembleStuecklistearbeitsplanDtos(
			Collection<?> stuecklistearbeitsplans) {
		List<StuecklistearbeitsplanDto> list = new ArrayList<StuecklistearbeitsplanDto>();
		if (stuecklistearbeitsplans != null) {
			Iterator<?> iterator = stuecklistearbeitsplans.iterator();
			while (iterator.hasNext()) {
				Stuecklistearbeitsplan stuecklistearbeitsplan = (Stuecklistearbeitsplan) iterator
						.next();
				list.add(assembleStuecklistearbeitsplanDto(stuecklistearbeitsplan));
			}
		}
		StuecklistearbeitsplanDto[] returnArray = new StuecklistearbeitsplanDto[list
				.size()];
		return (StuecklistearbeitsplanDto[]) list.toArray(returnArray);
	}

	public java.util.HashMap<Integer, String> getAlleStuecklistenIIdsFuerVerwendungsnachweis(
			Integer artikelIId, TheClientDto theClientDto) {
		String sMandant = theClientDto.getMandant();
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRStuecklisteposition.class)
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
						"s")
				.add(Restrictions.eq("s.mandant_c_nr", sMandant))
				.createAlias(
						com.lp.server.stueckliste.service.StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL,
						"a").add(Restrictions.eq("a.i_id", artikelIId))
				.addOrder(Order.asc("a.c_nr"));
		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		java.util.HashMap<Integer, String> al = new java.util.HashMap<Integer, String>();

		while (resultListIterator.hasNext()) {
			FLRStuecklisteposition stuecklisteposition = (FLRStuecklisteposition) resultListIterator
					.next();
			al.put(stuecklisteposition.getStueckliste_i_id(), "");
		}
		return al;
	}

	/**
	 * uselocalspr: 0 Hole alle Artikelarten nach Spr.
	 * 
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllStuecklisteart(TheClientDto theClientDto) {

		TreeMap<String, String> tmArten = new TreeMap<String, String>();
		// try {

		boolean bSetartikel = getMandantFac().hatZusatzfunktionberechtigung(
				MandantFac.ZUSATZFUNKTION_SETARTIKEL, theClientDto);

		Query query = em.createNamedQuery("StuecklisteartfindAll");
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Stuecklisteart stuecklisteart = (Stuecklisteart) itArten.next();

			if (stuecklisteart.getCNr().equals(
					StuecklisteFac.STUECKLISTEART_SETARTIKEL)
					&& bSetartikel == false) {
				continue;
			}

			tmArten.put(stuecklisteart.getCNr(), stuecklisteart.getCBez());
		}

		return tmArten;
	}

	public Map getEingeschraenkteFertigungsgruppen(TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("FertigungsgrupperollefindBySystemrolleIId");
		query.setParameter(1, theClientDto.getSystemrolleIId());
		Collection<?> clArten = query.getResultList();

		if (clArten.size() == 0) {
			return null;
		} else {

			TreeMap<Integer, Fertigungsgruppe> tmArten = new TreeMap<Integer, Fertigungsgruppe>();
			Iterator<?> itArten = clArten.iterator();

			while (itArten.hasNext()) {
				Fertigungsgrupperolle fertigungsgrupperolle = (Fertigungsgrupperolle) itArten
						.next();

				Fertigungsgruppe fertigungsgruppe = em.find(
						Fertigungsgruppe.class,
						fertigungsgrupperolle.getFertigungsgruppeIId());

				tmArten.put(fertigungsgruppe.getISort(), fertigungsgruppe);
			}
			LinkedHashMap<Integer, String> returnMap = new LinkedHashMap<Integer, String>();
			Iterator<?> itSorted = tmArten.values().iterator();
			while (itSorted.hasNext()) {
				Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) itSorted
						.next();

				returnMap.put(fertigungsgruppe.getIId(),
						fertigungsgruppe.getCBez());

			}

			return returnMap;
		}
	}

	public Map getAllFertigungsgrupe(TheClientDto theClientDto) {

		LinkedHashMap<Integer, String> tmArten = new LinkedHashMap<Integer, String>();

		Query query = em.createNamedQuery("FertigungsgruppefindByMandantCNr");
		query.setParameter(1, theClientDto.getMandant());
		Collection<?> clArten = query.getResultList();

		Iterator<?> itArten = clArten.iterator();

		while (itArten.hasNext()) {
			Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) itArten
					.next();

			tmArten.put(fertigungsgruppe.getIId(), fertigungsgruppe.getCBez());
		}

		return tmArten;
	}

	public Integer createStuecklistepositions(
			StuecklistepositionDto[] stuecklistepositionDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = null;
		for (int i = 0; i < stuecklistepositionDtos.length; i++) {
			iId = createStuecklisteposition(stuecklistepositionDtos[i],
					theClientDto);
		}
		return iId;
	}

	public Integer createStuecklistearbeitsplans(
			StuecklistearbeitsplanDto[] stuecklistearbeitsplanDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		Integer iId = null;
		for (int i = 0; i < stuecklistearbeitsplanDtos.length; i++) {
			iId = createStuecklistearbeitsplan(stuecklistearbeitsplanDtos[i],
					theClientDto);
		}
		return iId;
	}

	public Integer createStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getStuecklisteIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getStuecklisteIId() == null"));

		}
		if (stuecklistepositionDto.getNMenge() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getNMenge() == null"));

		}
		if (stuecklistepositionDto.getEinheitCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getEinheitCNr() == null"));

		}
		if (stuecklistepositionDto.getPositionsartCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getPositionsartCNr() == null"));

		}
		if (stuecklistepositionDto.getMontageartIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getMontageartIId() == null"));

		}

		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklistepositionDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)) {
			// PRUEFEN, OB KEIN DEADLOCK:
			try {
				Query query = em
						.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
				query.setParameter(1, stuecklistepositionDto.getArtikelIId());
				query.setParameter(2, theClientDto.getMandant());
				Stueckliste stueckliste1 = (Stueckliste) query
						.getSingleResult();
				if (stueckliste1 != null) {
					Integer stuecklisteIId = stueckliste1.getIId();
					if (pruefeObArtikelStuecklistenstrukturSchonVorhanden(
							stuecklisteIId,
							stuecklistepositionDto.getStuecklisteIId(),
							theClientDto)
							|| stuecklistepositionDto.getArtikelIId().equals(
									stuecklisteFindByPrimaryKey(
											stuecklistepositionDto
													.getStuecklisteIId(),
											theClientDto).getArtikelIId())) {

						String artikelnummer = getArtikelFac()
								.artikelFindByPrimaryKeySmall(
										stuecklistepositionDto.getArtikelIId(),
										theClientDto).getCNr();
						ArrayList alInfo = new ArrayList();
						alInfo.add(artikelnummer);
						throw new EJBExceptionLP(
								EJBExceptionLP.STUECKLISTE_DEADLOCK, alInfo,
								new Exception("DEADLOCK"));

					}

				}
			} catch (NoResultException ex2) {
				// Wenn keine Stueckliste, dann egal
			} catch (NonUniqueResultException ex1) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
			}

			// PJ 14647

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							stuecklistepositionDto.getArtikelIId(),
							theClientDto);

			// PJ 16622
			if (getMandantFac().hatZusatzfunktionberechtigung(
					MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				Artikel artikel = em.find(Artikel.class,
						stuecklistepositionDto.getArtikelIId());
				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

					if (stuecklistepositionDto.getNMenge().doubleValue() != 1) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
								new Exception(
										"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

					}

				}

			}

			if (stuecklisteDto != null
					&& stuecklisteDto.getStuecklisteartCNr().equals(
							StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
						new Exception(
								"FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH"));

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

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STUECKLISTEPOSITION);
		stuecklistepositionDto.setIId(pk);
		if (stuecklistepositionDto.getISort() == null) {
			Integer i = null;
			try {
				Query querynext = em
						.createNamedQuery("StuecklistepositionejbSelectNextReihung");
				querynext.setParameter(1,
						stuecklistepositionDto.getStuecklisteIId());
				i = (Integer) querynext.getSingleResult();
			} catch (NoResultException ex) {
			}
			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			stuecklistepositionDto.setISort(i);

		}

		stuecklistepositionDto.setPersonalIIdAnlegen(theClientDto
				.getIDPersonal());
		stuecklistepositionDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		stuecklistepositionDto.setTAnlegen(new java.sql.Timestamp(System
				.currentTimeMillis()));
		stuecklistepositionDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		try {

			if (stuecklistepositionDto.getPositionsartCNr().equals(
					LocaleFac.POSITIONSART_HANDEINGABE)) {
				// einen Handartikel anlegen
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto
						.setCBez(stuecklistepositionDto.getSHandeingabe());
				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(stuecklistepositionDto
						.getEinheitCNr());

				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
						theClientDto);

				stuecklistepositionDto.setArtikelIId(iIdArtikel);
			}

			Stuecklisteposition stuecklisteposition = new Stuecklisteposition(
					stuecklistepositionDto.getIId(),
					stuecklistepositionDto.getStuecklisteIId(),
					stuecklistepositionDto.getArtikelIId(),
					stuecklistepositionDto.getNMenge(),
					stuecklistepositionDto.getEinheitCNr(),
					stuecklistepositionDto.getMontageartIId(),
					stuecklistepositionDto.getISort(),
					stuecklistepositionDto.getBMitdrucken(),
					stuecklistepositionDto.getPersonalIIdAnlegen(),
					stuecklistepositionDto.getPersonalIIdAendern(),
					stuecklistepositionDto.getIBeginnterminoffset());
			em.persist(stuecklisteposition);
			em.flush();
			setStuecklistepositionFromStuecklistepositionDto(
					stuecklisteposition, stuecklistepositionDto);

			stueckliste.setPersonalIIdAendernposition(theClientDto
					.getIDPersonal());
			stueckliste.setTAendernposition(new java.sql.Timestamp(System
					.currentTimeMillis()));

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		}

		HvDtoLogger<StuecklistepositionDto> hvLogger = new HvDtoLogger<StuecklistepositionDto>(
				em, stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		hvLogger.logInsert(stuecklistepositionDto);

		return stuecklistepositionDto.getIId();
	}

	public void removeStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("stuecklistepositionDto.getIId() == null"));
		}

		Stuecklisteposition toRemove = em.find(Stuecklisteposition.class,
				stuecklistepositionDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		HvDtoLogger<StuecklistepositionDto> hvLogger = new HvDtoLogger<StuecklistepositionDto>(
				em, stuecklistepositionDto.getStuecklisteIId(), theClientDto);
		hvLogger.logDelete(stuecklistepositionDto);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklistepositionDto.getStuecklisteIId());
		if (stueckliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		stueckliste.setPersonalIIdAendernposition(theClientDto.getIDPersonal());
		stueckliste.setTAendernposition(new java.sql.Timestamp(System
				.currentTimeMillis()));
	}

	public void updateStuecklisteposition(
			StuecklistepositionDto stuecklistepositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (stuecklistepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("stuecklistepositionDto == null"));
		}
		if (stuecklistepositionDto.getIId() == null
				|| stuecklistepositionDto.getStuecklisteIId() == null
				|| stuecklistepositionDto.getPositionsartCNr() == null
				|| stuecklistepositionDto.getNMenge() == null
				|| stuecklistepositionDto.getEinheitCNr() == null
				|| stuecklistepositionDto.getMontageartIId() == null
				|| stuecklistepositionDto.getBMitdrucken() == null
				|| stuecklistepositionDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getIId() == null || stuecklistepositionDto.getStuecklisteIId() == null || stuecklistepositionDto.getArtikelIId() == null || stuecklistepositionDto.getNMenge() == null ||s tuecklistepositionDto.getPositionsartCNr() == null || stuecklistepositionDto.getEinheitCNr() == null || stuecklistepositionDto.getMontageartIId() == null || stuecklistepositionDto.getBMitdrucken() == null || stuecklistepositionDto.getISort() == null"));
		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_HANDEINGABE)
				&& stuecklistepositionDto.getSHandeingabe() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE) && stuecklistepositionDto.getSHandeingabe()  ==  null"));

		}
		if (stuecklistepositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)
				&& stuecklistepositionDto.getArtikelIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"stuecklistepositionDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT) && stuecklistepositionDto.getArtikelIId() == null"));

		}

		vergleicheStuecklistepositionDtoVorherNachherUndLoggeAenderungen(
				stuecklistepositionDto, theClientDto);

		Stueckliste stueckliste = em.find(Stueckliste.class,
				stuecklistepositionDto.getStuecklisteIId());

		if (stuecklistepositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_IDENT)) {

			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
							stuecklistepositionDto.getArtikelIId(),
							theClientDto);

			// PJ 16622

			if (getMandantFac().hatZusatzfunktionberechtigung(
					MandantFac.ZUSATZFUNKTION_GERAETESERIENNUMMERN,
					theClientDto)) {
				Artikel artikel = em.find(Artikel.class,
						stuecklistepositionDto.getArtikelIId());
				if (Helper.short2boolean(artikel.getBSeriennrtragend())) {

					if (stuecklistepositionDto.getNMenge().doubleValue() != 1) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR,
								new Exception(
										"FEHLER_POSITIONSMENGE_EINES_SNR_ARTIKELS_MUSS_1_SEIN_WENN_GERAETESNR"));

					}

				}

			}

			if (stuecklisteDto != null
					&& stuecklisteDto.getStuecklisteartCNr().equals(
							StuecklisteFac.STUECKLISTEART_SETARTIKEL)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH,
						new Exception(
								"FEHLER_STUECKLISTENART_ARTIKELSET_IN_STUECKLISTENPOSITION_NICHT_MOEGLICH"));

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
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, stuecklistepositionDto.getArtikelIId());
			query.setParameter(2, theClientDto.getMandant());
			Integer stuecklisteIId = ((Stueckliste) query.getSingleResult())
					.getIId();
			if (pruefeObArtikelStuecklistenstrukturSchonVorhanden(
					stuecklisteIId, stuecklistepositionDto.getStuecklisteIId(),
					theClientDto)
					|| stuecklistepositionDto.getArtikelIId().equals(
							stuecklisteFindByPrimaryKey(
									stuecklistepositionDto.getStuecklisteIId(),
									theClientDto).getArtikelIId())) {
				throw new EJBExceptionLP(EJBExceptionLP.STUECKLISTE_DEADLOCK,
						new Exception("DEADLOCK"));

			}
		} catch (NoResultException ex2) {
			// Wenn keine Stueckliste, dann egal
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		Integer iId = stuecklistepositionDto.getIId();
		// try {
		Stuecklisteposition stuecklisteposition = em.find(
				Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}

		if (stuecklistepositionDto.getPositionsartCNr().equals(
				LocaleFac.POSITIONSART_HANDEINGABE)) {
			// einen Handartikel anlegen
			ArtikelDto oArtikelDto = new ArtikelDto();
			oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

			ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
			oArtikelsprDto.setCBez(stuecklistepositionDto.getSHandeingabe());
			oArtikelDto.setArtikelsprDto(oArtikelsprDto);
			oArtikelDto.setEinheitCNr(stuecklistepositionDto.getEinheitCNr());

			Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
					theClientDto);

			stuecklistepositionDto.setArtikelIId(iIdArtikel);
		}

		stuecklistepositionDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		stuecklistepositionDto.setTAendern(new java.sql.Timestamp(System
				.currentTimeMillis()));

		setStuecklistepositionFromStuecklistepositionDto(stuecklisteposition,
				stuecklistepositionDto);

		stueckliste.setPersonalIIdAendernposition(theClientDto.getIDPersonal());
		stueckliste.setTAendernposition(new java.sql.Timestamp(System
				.currentTimeMillis()));

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);

		// }
	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Stuecklisteposition stuecklisteposition = em.find(
				Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		StuecklistepositionDto stuecklistepositionDto = assembleStuecklistepositionDto(stuecklisteposition);
		Montageart montageart = em.find(Montageart.class,
				stuecklistepositionDto.getMontageartIId());
		if (montageart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		stuecklistepositionDto
				.setMontageartDto(assembleMontageartDto(montageart));

		ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(
				stuecklistepositionDto.getArtikelIId(), theClientDto);
		stuecklistepositionDto.setArtikelDto(a);

		if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			stuecklistepositionDto
					.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		} else {
			stuecklistepositionDto
					.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		}

		return stuecklistepositionDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public StuecklistepositionDto stuecklistepositionFindByPrimaryKeyOhneExc(
			Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			return null;
		}

		Stuecklisteposition stuecklisteposition = em.find(
				Stuecklisteposition.class, iId);
		if (stuecklisteposition == null) {
			return null;
		}
		StuecklistepositionDto stuecklistepositionDto = assembleStuecklistepositionDto(stuecklisteposition);
		Montageart montageart = em.find(Montageart.class,
				stuecklistepositionDto.getMontageartIId());
		if (montageart == null) {
			return null;
		}
		stuecklistepositionDto
				.setMontageartDto(assembleMontageartDto(montageart));

		ArtikelDto a = getArtikelFac().artikelFindByPrimaryKey(
				stuecklistepositionDto.getArtikelIId(), null);
		stuecklistepositionDto.setArtikelDto(a);

		if (a.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			stuecklistepositionDto
					.setPositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
		} else {
			stuecklistepositionDto
					.setPositionsartCNr(LocaleFac.POSITIONSART_IDENT);
		}

		return stuecklistepositionDto;
	}

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		query.setParameter(1, stuecklisteIId);
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

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdBMitdrucken(
			Integer stuecklisteIId, Short bMitdrucken, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId == null || bMitdrucken == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null || bMitdrucken==null"));
		}
		// try {
		Query query = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIIdBMitdrucken");
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

	public StuecklistepositionDto[] stuecklistepositionFindByStuecklisteIIdArtikelIId(
			Integer stuecklisteIId, Integer artikelIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIIdArtikelIId");
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

	public StuecklistearbeitsplanDto[] stuecklistearbeitsplanFindByStuecklisteIId(
			Integer stuecklisteIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (stuecklisteIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("stuecklisteIId == null"));
		}
		// try {
		Query query = em
				.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
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

	private void setStuecklistepositionFromStuecklistepositionDto(
			Stuecklisteposition stuecklisteposition,
			StuecklistepositionDto stuecklistepositionDto) {
		stuecklisteposition.setStuecklisteIId(stuecklistepositionDto
				.getStuecklisteIId());
		stuecklisteposition.setArtikelIId(stuecklistepositionDto
				.getArtikelIId());
		stuecklisteposition.setNMenge(stuecklistepositionDto.getNMenge());
		stuecklisteposition.setNKalkpreis(stuecklistepositionDto
				.getNKalkpreis());
		stuecklisteposition.setEinheitCNr(stuecklistepositionDto
				.getEinheitCNr());
		stuecklisteposition.setFDimension1(stuecklistepositionDto
				.getFDimension1());
		stuecklisteposition.setFDimension2(stuecklistepositionDto
				.getFDimension2());
		stuecklisteposition.setFDimension3(stuecklistepositionDto
				.getFDimension3());
		stuecklisteposition.setCPosition(stuecklistepositionDto.getCPosition());
		stuecklisteposition.setCKommentar(stuecklistepositionDto
				.getCKommentar());
		stuecklisteposition.setMontageartIId(stuecklistepositionDto
				.getMontageartIId());
		stuecklisteposition.setILfdnummer(stuecklistepositionDto
				.getILfdnummer());
		stuecklisteposition.setISort(stuecklistepositionDto.getISort());
		stuecklisteposition.setBMitdrucken(stuecklistepositionDto
				.getBMitdrucken());
		stuecklisteposition.setPersonalIIdAendern(stuecklistepositionDto
				.getPersonalIIdAendern());
		stuecklisteposition.setPersonalIIdAnlegen(stuecklistepositionDto
				.getPersonalIIdAnlegen());
		stuecklisteposition.setTAendern(stuecklistepositionDto.getTAendern());
		stuecklisteposition.setTAnlegen(stuecklistepositionDto.getTAnlegen());
		stuecklisteposition.setIBeginnterminoffset(stuecklistepositionDto
				.getIBeginnterminoffset());
		em.merge(stuecklisteposition);
		em.flush();
	}

	private StuecklistepositionDto assembleStuecklistepositionDto(
			Stuecklisteposition stuecklisteposition) {
		return StuecklistepositionDtoAssembler.createDto(stuecklisteposition);
	}

	private StuecklistepositionDto[] assembleStuecklistepositionDtos(
			Collection<?> stuecklistepositions) {
		List<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
		if (stuecklistepositions != null) {
			Iterator<?> iterator = stuecklistepositions.iterator();
			while (iterator.hasNext()) {
				Stuecklisteposition stuecklisteposition = (Stuecklisteposition) iterator
						.next();
				list.add(assembleStuecklistepositionDto(stuecklisteposition));
			}
		}
		StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list
				.size()];
		return (StuecklistepositionDto[]) list.toArray(returnArray);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStuecklisteMitArbeitsplan(
			Integer stuecklisteIId, TheClientDto theClientDto,
			int iOptionSortierung, int iEbene, ArrayList strukturMap,
			boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse) {

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
		FLRStuecklisteposition flrStuecklisteposition = new FLRStuecklisteposition();
		flrStuecklisteposition.setStueckliste_i_id(stuecklisteIId);

		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit = session
				.createCriteria(FLRStuecklisteposition.class)
				.add(Example.create(flrStuecklisteposition))
				.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL,
						"a");
		crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
		if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
			crit.addOrder(Order.asc("a.c_nr"));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {
			crit.addOrder(Order
					.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE) {
			crit.addOrder(Order
					.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));
		}

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results
				.size()];

		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		// NEU Projekt 9730
		ArrayList<FLRStuecklisteposition> alKomp = new ArrayList();
		if (bGleichePositionenZusammenfassen == true) {
			// Positionen zusammenfassen, wenn der Artikel und die
			// Positionseinheit gleich sind
			// Nur wenns kein Arbeitsplan ist und in derselben Ebene
			for (int i = 0; i < returnArray.length; i++) {
				boolean bGefunden = false;

				for (int j = 0; j < alKomp.size(); j++) {
					FLRStuecklisteposition temp = (FLRStuecklisteposition) alKomp
							.get(j);

					if (temp.getFlrartikel().getI_id()
							.equals(returnArray[i].getFlrartikel().getI_id())
							&& temp.getEinheit_c_nr().equals(
									returnArray[i].getEinheit_c_nr())) {
						temp.setN_menge(temp.getN_menge().add(
								returnArray[i].getN_menge()));
						alKomp.set(j, temp);

						bGefunden = true;
					}

				}
				if (!bGefunden) {
					alKomp.add(returnArray[i]);
				}

			}
			returnArray = new FLRStuecklisteposition[alKomp.size()];
			returnArray = (FLRStuecklisteposition[]) alKomp
					.toArray(returnArray);
		}
		// -ENDE NEU

		// --ADD ARBEITSPLAN

		// Arbeitsplan
		FLRStuecklistearbeitsplan flrStuecklistearbeitsplan = new FLRStuecklistearbeitsplan();
		flrStuecklistearbeitsplan.setStueckliste_i_id(stuecklisteIId);

		Session session2 = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Criteria crit2 = session2
				.createCriteria(FLRStuecklistearbeitsplan.class)
				.add(Example.create(flrStuecklistearbeitsplan))
				.createAlias(
						StuecklisteFac.FLR_STUECKLISTEARBEITSPLAN_FLRARTIKEL,
						"a");
		crit2.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));

		crit2.addOrder(Order.asc("i_arbeitsgang"));
		crit2.addOrder(Order.asc("i_unterarbeitsgang"));

		List<?> results2 = crit2.list();

		FLRStuecklistearbeitsplan[] arbeitsplan = new FLRStuecklistearbeitsplan[results2
				.size()];
		arbeitsplan = (FLRStuecklistearbeitsplan[]) results2
				.toArray(arbeitsplan);
		for (int i = 0; i < arbeitsplan.length; i++) {

			Long stueckzeit = new Long((long) (arbeitsplan[i].getL_stueckzeit()
					.longValue() * nSatzgroesse.doubleValue()));

			if (arbeitsplan[i].getFlrstueckliste().getI_erfassungsfaktor() != 0) {
				stueckzeit = stueckzeit
						/ arbeitsplan[i].getFlrstueckliste()
								.getI_erfassungsfaktor();
			}

			int iBasisKalkulation = 0;
			try {
				ParametermandantDto parameterDto = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_STUECKLISTE,
								ParameterFac.PARAMETER_BASIS_GESAMTKALKULATION);
				iBasisKalkulation = (Integer) parameterDto.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			Long ruestzeit = arbeitsplan[i].getL_ruestzeit();

			if (iBasisKalkulation == 1
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel()
							.getF_fertigungssatzgroesse() != null
					&& arbeitsplan[i].getFlrstueckliste().getFlrartikel()
							.getF_fertigungssatzgroesse().doubleValue() > 0) {

				double posmenge = nSatzgroesse.multiply(nLosgroesse)
						.doubleValue();

				double fsgmenge = arbeitsplan[i].getFlrstueckliste()
						.getFlrartikel().getF_fertigungssatzgroesse()
						.doubleValue();

				while (fsgmenge < posmenge) {

					fsgmenge += arbeitsplan[i].getFlrstueckliste()
							.getFlrartikel().getF_fertigungssatzgroesse()
							.doubleValue();
				}

				ruestzeit = (long) ((ruestzeit / fsgmenge)
						* nSatzgroesse.doubleValue() * nLosgroesse
						.doubleValue());

			}

			if (arbeitsplan[i].getFlrmaschine() == null
					|| Helper.short2boolean(arbeitsplan[i]
							.getB_nurmaschinenzeit()) == false) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanDto.setArtikelIId(arbeitsplan[i]
						.getFlrartikel().getI_id());
				stuecklistearbeitsplanDto.setCKommentar(arbeitsplan[i]
						.getC_kommentar());
				stuecklistearbeitsplanDto.setLRuestzeit(ruestzeit);
				stuecklistearbeitsplanDto.setBNurmaschinenzeit(arbeitsplan[i]
						.getB_nurmaschinenzeit());
				stuecklistearbeitsplanDto.setIAufspannung(arbeitsplan[i]
						.getI_aufspannung());

				stuecklistearbeitsplanDto.setLStueckzeit(stueckzeit);
				StuecklisteMitStrukturDto stuecklisteMitStrukturDto = new StuecklisteMitStrukturDto(
						iEbene, null);
				stuecklisteMitStrukturDto
						.setStuecklistearbeitsplanDto(stuecklistearbeitsplanDto);
				stuecklisteMitStrukturDto.setBArbeitszeit(true);
				strukturMap.add(stuecklisteMitStrukturDto);
			}
			// Wenn Maschine, dann zusaetzlichen Eintrag erstellen:
			if (arbeitsplan[i].getFlrmaschine() != null) {

				StuecklistearbeitsplanDto stuecklistearbeitsplanMaschineDto = new StuecklistearbeitsplanDto();
				stuecklistearbeitsplanMaschineDto.setArtikelIId(arbeitsplan[i]
						.getFlrartikel().getI_id());
				stuecklistearbeitsplanMaschineDto.setCKommentar(arbeitsplan[i]
						.getC_kommentar());
				stuecklistearbeitsplanMaschineDto.setLRuestzeit(arbeitsplan[i]
						.getL_ruestzeit());
				stuecklistearbeitsplanMaschineDto.setLStueckzeit(stueckzeit);
				stuecklistearbeitsplanMaschineDto.setMaschineIId(arbeitsplan[i]
						.getMaschine_i_id());
				stuecklistearbeitsplanMaschineDto
						.setBNurmaschinenzeit(arbeitsplan[i]
								.getB_nurmaschinenzeit());
				stuecklistearbeitsplanMaschineDto
						.setIAufspannung(arbeitsplan[i].getI_aufspannung());

				StuecklisteMitStrukturDto stuecklisteMitStrukturMaschineDto = new StuecklisteMitStrukturDto(
						iEbene, null);
				stuecklisteMitStrukturMaschineDto
						.setStuecklistearbeitsplanDto(stuecklistearbeitsplanMaschineDto);
				stuecklisteMitStrukturMaschineDto.setBArbeitszeit(true);
				stuecklisteMitStrukturMaschineDto.setBMaschinenzeit(true);

				strukturMap.add(stuecklisteMitStrukturMaschineDto);
			}

		}

		// -- END ADD ARBEITSPLAN

		for (int i = 0; i < returnArray.length; i++) {
			StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
			stuecklistepositionDto.setArtikelIId(returnArray[i].getFlrartikel()
					.getI_id());
			stuecklistepositionDto.setCKommentar(returnArray[i]
					.getC_kommentar());
			stuecklistepositionDto.setCPosition(returnArray[i].getC_position());
			stuecklistepositionDto.setEinheitCNr(returnArray[i]
					.getEinheit_c_nr());
			stuecklistepositionDto.setNMenge(returnArray[i].getN_menge());
			stuecklistepositionDto.setNKalkpreis(returnArray[i]
					.getN_kalkpreis());
			stuecklistepositionDto.setILfdnummer(returnArray[i]
					.getI_lfdnummer());
			stuecklistepositionDto.setIId(returnArray[i].getI_id());

			try {

				// Menge nach Zieleinheit umrechnen
				BigDecimal bdMenge = returnArray[i].getN_menge().multiply(
						nSatzgroesse);

				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
						returnArray[i].getEinheit_c_nr(), theClientDto);

				// Positionsmenge nach Zielenge umrechnen
				int dimension = einheitDto.getIDimension().intValue();

				if (dimension == 1) {
					if (returnArray[i].getF_dimension1() != null) {
						bdMenge = bdMenge
								.multiply(new BigDecimal(returnArray[i]
										.getF_dimension1().doubleValue()));
					}
				} else if (dimension == 2) {
					if (returnArray[i].getF_dimension1() != null
							&& returnArray[i].getF_dimension2() != null) {
						bdMenge = bdMenge.multiply(
								new BigDecimal(returnArray[i].getF_dimension1()
										.doubleValue())).multiply(
								new BigDecimal(returnArray[i].getF_dimension2()
										.doubleValue()));
					}
				} else if (dimension == 3) {
					if (returnArray[i].getF_dimension1() != null
							&& returnArray[i].getF_dimension2() != null
							&& returnArray[i].getF_dimension3() != null) {
						bdMenge = bdMenge
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension1()
												.doubleValue()))
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension2()
												.doubleValue()))
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension3()
												.doubleValue()));
					}
				}

				BigDecimal faktor = null;
				String artikelnummerStueckliste = returnArray[i]
						.getFlrstueckliste().getFlrartikel().getC_nr();
				try {
					faktor = getSystemFac().rechneUmInAndereEinheit(
							new BigDecimal(1),
							returnArray[i].getFlrartikel().getEinheit_c_nr(),
							returnArray[i].getEinheit_c_nr(),
							returnArray[i].getI_id(), theClientDto);
				} catch (EJBExceptionLP ex2) {
					// Projekte 3509/10048/9918
					String positionsArtikel = returnArray[i].getFlrartikel()
							.getC_nr();
					String position = "";
					if (returnArray[i].getI_sort() != null) {
						position = returnArray[i].getI_sort() + "";
					}
					ArrayList<Object> info = ex2.getAlInfoForTheClient();
					// Nachricht zusammenbauen
					String meldung = (String) info.get(0) + "\r\n";
					meldung += "Zu finden in Ebene " + (iEbene + 1)
							+ ", St\u00FCckliste: " + artikelnummerStueckliste
							+ "\r\n";
					meldung += "Position " + position + "-> Artikelnummer: "
							+ positionsArtikel;
					info.set(0, meldung);
					ex2.setAlInfoForTheClient(info);
					throwEJBExceptionLPRespectOld(new RemoteException("", ex2));
				}
				if (faktor.doubleValue() != 0) {
					bdMenge = bdMenge.divide(faktor, 6,
							BigDecimal.ROUND_HALF_EVEN);

					bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
							returnArray[i].getFlrartikel()
									.getF_verschnittfaktor(), returnArray[i]
									.getFlrartikel().getF_verschnittbasis(),
							nLosgroesse);
					// PJ 14352
					bdMenge = bdMenge.divide(new BigDecimal(returnArray[i]
							.getFlrstueckliste().getI_erfassungsfaktor()
							.doubleValue()), 6, BigDecimal.ROUND_HALF_EVEN);

					stuecklistepositionDto.setNZielmenge(bdMenge);
				} else {
					ArrayList<Object> al = new ArrayList<Object>();

					EinheitDto von = getSystemFac().einheitFindByPrimaryKey(
							returnArray[i].getEinheit_c_nr(), theClientDto);
					EinheitDto zu = getSystemFac().einheitFindByPrimaryKey(
							returnArray[i].getFlrartikel().getEinheit_c_nr(),
							theClientDto);

					al.add(von.formatBez() + " <-> " + zu.formatBez());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
							al,
							new Exception(
									"FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			MontageartDto ma = new MontageartDto();
			ma.setIId(returnArray[i].getFlrmontageart().getI_id());
			ma.setCBez(returnArray[i].getFlrmontageart().getC_bez());
			stuecklistepositionDto.setMontageartDto(ma);
			if (returnArray[i].getF_dimension1() != null) {
				stuecklistepositionDto.setFDimension1(new Float(returnArray[i]
						.getF_dimension1().doubleValue()));
			}
			if (returnArray[i].getF_dimension2() != null) {
				stuecklistepositionDto.setFDimension2(new Float(returnArray[i]
						.getF_dimension2().doubleValue()));
			}
			if (returnArray[i].getF_dimension3() != null) {
				stuecklistepositionDto.setFDimension3(new Float(returnArray[i]
						.getF_dimension3().doubleValue()));
			}

			strukturMap.add(new StuecklisteMitStrukturDto(iEbene,
					stuecklistepositionDto));

			if (bMitUnterstuecklisten == true) {
				try {
					Query query = em
							.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
					query.setParameter(1, returnArray[i].getFlrartikel()
							.getI_id());
					query.setParameter(2, theClientDto.getMandant());
					Stueckliste stkl = (Stueckliste) query.getSingleResult();
					if (stkl != null) {

						// wenn Fremdfertigungsstueckliste, dann keine
						// Aufloesung
						if (Helper.short2boolean(stkl.getBFremdfertigung()) == false) {
							query = em
									.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionen = query.getResultList()
									.size();
							query = em
									.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionenArbeitsplan = query
									.getResultList().size();
							// Wenn keine Positionen und kein Arbeitsplan, dann
							// auch keinen Aufloesung
							if (iAnzahlPositionen
									+ iAnzahlPositionenArbeitsplan > 0) {
								StuecklisteMitStrukturDto strukturTemp = (StuecklisteMitStrukturDto) strukturMap
										.get(strukturMap.size() - 1);
								strukturTemp.setBStueckliste(true);
								strukturTemp
										.setStuecklisteDto(assembleStuecklisteDto(stkl));
								strukturTemp.setDurchlaufzeit(stkl
										.getNDefaultdurchlaufzeit());
								strukturMap.set(strukturMap.size() - 1,
										strukturTemp);

								strukturMap = getStrukturDatenEinerStuecklisteMitArbeitsplan(
										stkl.getIId(), theClientDto,
										iOptionSortierung, iEbene + 1,
										strukturMap, bMitUnterstuecklisten,
										bGleichePositionenZusammenfassen,
										nLosgroesse,
										stuecklistepositionDto.getNZielmenge()); // .multiply(
								// nSatzgroesse
								// )); ->
								// Ausgebaut
								// wegen
								// Naemo-
								// Stueckliste
								// FC002
							}
						}
					}
				} catch (NoResultException ex) {
					// Dann keine Stueckliste
				} catch (NonUniqueResultException ex1) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
				}
			}
		}

		session.close();

		return strukturMap;

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStueckliste(Integer stuecklisteIId,
			TheClientDto theClientDto, int iOptionSortierung, int iEbene,
			ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse, boolean bUnterstklstrukurBelassen) {
		return getStrukturDatenEinerStueckliste(
				new Integer[] { stuecklisteIId }, theClientDto,
				iOptionSortierung, iEbene, strukturMap, bMitUnterstuecklisten,
				bGleichePositionenZusammenfassen, nLosgroesse, nSatzgroesse,
				bUnterstklstrukurBelassen);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public ArrayList getStrukturDatenEinerStueckliste(Integer[] stuecklisteIId,
			TheClientDto theClientDto, int iOptionSortierung, int iEbene,
			ArrayList strukturMap, boolean bMitUnterstuecklisten,
			boolean bGleichePositionenZusammenfassen, BigDecimal nLosgroesse,
			BigDecimal nSatzgroesse, boolean bUnterstklstrukurBelassen) {

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
		org.hibernate.Criteria crit = session.createCriteria(
				FLRStuecklisteposition.class).createAlias(
				StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRARTIKEL, "a");
		crit.createAlias(StuecklisteFac.FLR_STUECKLISTEPOSITION_FLRSTUECKLISTE,
				"s");

		crit.add(Restrictions.in("s.i_id", stuecklisteIId));
		crit.addOrder(Order.asc("a." + ArtikelFac.FLR_ARTIKEL_ARTIKELART_C_NR));
		if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
			crit.addOrder(Order.asc("a.c_nr"));
		} else if (iOptionSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION) {
			crit.addOrder(Order
					.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_C_POSITION));
		} else {
			crit.addOrder(Order
					.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));
		}

		List<?> results = crit.list();

		FLRStuecklisteposition[] returnArray = new FLRStuecklisteposition[results
				.size()];
		returnArray = (FLRStuecklisteposition[]) results.toArray(returnArray);

		// NEU Projekt 9730
		ArrayList alKomp = new ArrayList<FLRStuecklisteposition>();
		if (bGleichePositionenZusammenfassen == true) {
			// Positionen zusammenfassen, wenn der Artikel und die
			// Positionseinheit gleich sind
			// Nur wenns kein Arbeitsplan ist und in derselben Ebene
			for (int i = 0; i < returnArray.length; i++) {
				boolean bGefunden = false;

				for (int j = 0; j < alKomp.size(); j++) {
					FLRStuecklisteposition temp = (FLRStuecklisteposition) alKomp
							.get(j);

					if (temp.getFlrartikel().getI_id()
							.equals(returnArray[i].getFlrartikel().getI_id())
							&& temp.getEinheit_c_nr().equals(
									returnArray[i].getEinheit_c_nr())) {
						temp.setN_menge(temp.getN_menge().add(
								returnArray[i].getN_menge()));

						// SP1167
						BigDecimal bdKalkPreis = temp.getN_kalkpreis();
						if (bdKalkPreis != null
								&& returnArray[i].getN_kalkpreis() != null
								&& bdKalkPreis.doubleValue() < returnArray[i]
										.getN_kalkpreis().doubleValue()) {
							temp.setN_kalkpreis(returnArray[i].getN_kalkpreis());
						} else if (bdKalkPreis == null
								&& returnArray[i].getN_kalkpreis() != null) {
							temp.setN_kalkpreis(returnArray[i].getN_kalkpreis());
						}

						if (temp.getC_position() != null) {
							if (returnArray[i].getC_position() != null) {
								temp.setC_position(temp.getC_position() + "|"
										+ returnArray[i].getC_position());
							} else {
								temp.setC_position(returnArray[i]
										.getC_position());
							}
						} else {
							temp.setC_position(returnArray[i].getC_position());
						}

						if (temp.getC_kommentar() != null) {
							if (returnArray[i].getC_kommentar() != null) {
								temp.setC_kommentar(temp.getC_kommentar() + "|"
										+ returnArray[i].getC_kommentar());
							} else {
								temp.setC_kommentar(returnArray[i]
										.getC_kommentar());
							}
						} else {
							temp.setC_kommentar(returnArray[i].getC_kommentar());
						}

						if (Helper.short2boolean(temp.getB_mitdrucken()) == true
								&& Helper.short2boolean(returnArray[i]
										.getB_mitdrucken()) == true) {

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
			returnArray = (FLRStuecklisteposition[]) alKomp
					.toArray(returnArray);
		}
		// -ENDE NEU

		for (int i = 0; i < returnArray.length; i++) {

			StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
			stuecklistepositionDto.setArtikelIId(returnArray[i].getFlrartikel()
					.getI_id());
			stuecklistepositionDto.setCKommentar(returnArray[i]
					.getC_kommentar());
			stuecklistepositionDto.setCPosition(returnArray[i].getC_position());
			stuecklistepositionDto.setEinheitCNr(returnArray[i]
					.getEinheit_c_nr());
			stuecklistepositionDto.setILfdnummer(returnArray[i]
					.getI_lfdnummer());
			stuecklistepositionDto.setNMenge(returnArray[i].getN_menge());
			stuecklistepositionDto.setNKalkpreis(returnArray[i]
					.getN_kalkpreis());
			stuecklistepositionDto.setIId(returnArray[i].getI_id());
			stuecklistepositionDto.setISort(returnArray[i].getI_sort());
			stuecklistepositionDto.setBMitdrucken(returnArray[i]
					.getB_mitdrucken());

			try {

				// Menge nach Zieleinheit umrechnen
				BigDecimal bdMenge = returnArray[i].getN_menge().multiply(
						nSatzgroesse);

				EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
						returnArray[i].getEinheit_c_nr(), theClientDto);

				// Positionsmenge nach Zielenge umrechnen
				int dimension = einheitDto.getIDimension().intValue();

				if (dimension == 1) {
					if (returnArray[i].getF_dimension1() != null) {
						bdMenge = bdMenge
								.multiply(new BigDecimal(returnArray[i]
										.getF_dimension1().doubleValue()));
					}
				} else if (dimension == 2) {
					if (returnArray[i].getF_dimension1() != null
							&& returnArray[i].getF_dimension2() != null) {
						bdMenge = bdMenge.multiply(
								new BigDecimal(returnArray[i].getF_dimension1()
										.doubleValue())).multiply(
								new BigDecimal(returnArray[i].getF_dimension2()
										.doubleValue()));
					}
				} else if (dimension == 3) {
					if (returnArray[i].getF_dimension1() != null
							&& returnArray[i].getF_dimension2() != null
							&& returnArray[i].getF_dimension3() != null) {
						bdMenge = bdMenge
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension1()
												.doubleValue()))
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension2()
												.doubleValue()))
								.multiply(
										new BigDecimal(returnArray[i]
												.getF_dimension3()
												.doubleValue()));
					}
				}
				BigDecimal faktor = null;
				if (returnArray[i].getFlrartikel().getEinheit_c_nr()
						.equals(returnArray[i].getEinheit_c_nr())) {
					faktor = new BigDecimal(1);
				} else {
					faktor = getSystemFac().rechneUmInAndereEinheit(
							new BigDecimal(1),
							returnArray[i].getFlrartikel().getEinheit_c_nr(),
							returnArray[i].getEinheit_c_nr(),
							returnArray[i].getI_id(), theClientDto);
				}
				if (faktor.doubleValue() != 0) {
					bdMenge = bdMenge
							.divide(faktor, BigDecimal.ROUND_HALF_EVEN);

					bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
							returnArray[i].getFlrartikel()
									.getF_verschnittfaktor(), returnArray[i]
									.getFlrartikel().getF_verschnittbasis(),
							nLosgroesse);

					// PJ 14352
					bdMenge = bdMenge.divide(new BigDecimal(returnArray[i]
							.getFlrstueckliste().getI_erfassungsfaktor()
							.doubleValue()), 4, BigDecimal.ROUND_HALF_EVEN);

					stuecklistepositionDto.setNZielmenge(bdMenge);
				} else {
					ArrayList<Object> al = new ArrayList<Object>();

					EinheitDto von = getSystemFac().einheitFindByPrimaryKey(
							returnArray[i].getEinheit_c_nr(), theClientDto);
					EinheitDto zu = getSystemFac().einheitFindByPrimaryKey(
							returnArray[i].getFlrartikel().getEinheit_c_nr(),
							theClientDto);
					al.add(von.formatBez() + " <-> " + zu.formatBez());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
							al,
							new Exception(
									"FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
				}

			} catch (RemoteException ex1) {
				throwEJBExceptionLPRespectOld(ex1);
			}

			MontageartDto ma = new MontageartDto();
			ma.setIId(returnArray[i].getFlrmontageart().getI_id());
			ma.setCBez(returnArray[i].getFlrmontageart().getC_bez());
			stuecklistepositionDto.setMontageartDto(ma);
			if (returnArray[i].getF_dimension1() != null) {
				stuecklistepositionDto.setFDimension1(new Float(returnArray[i]
						.getF_dimension1().doubleValue()));
			}
			if (returnArray[i].getF_dimension2() != null) {
				stuecklistepositionDto.setFDimension2(new Float(returnArray[i]
						.getF_dimension2().doubleValue()));
			}
			if (returnArray[i].getF_dimension3() != null) {
				stuecklistepositionDto.setFDimension3(new Float(returnArray[i]
						.getF_dimension3().doubleValue()));
			}

			if (bMitUnterstuecklisten == true
					&& bUnterstklstrukurBelassen == false
					&& bGleichePositionenZusammenfassen == true) {
				boolean bGefunden = false;
				for (int k = 0; k < strukturMap.size(); k++) {
					StuecklisteMitStrukturDto tempDto = (StuecklisteMitStrukturDto) strukturMap
							.get(k);
					if (tempDto.getStuecklistepositionDto().getArtikelIId()
							.equals(stuecklistepositionDto.getArtikelIId())
							&& tempDto
									.getStuecklistepositionDto()
									.getEinheitCNr()
									.equals(stuecklistepositionDto
											.getEinheitCNr())) {
						tempDto.getStuecklistepositionDto()
								.setNMenge(
										tempDto.getStuecklistepositionDto()
												.getNMenge()
												.add(stuecklistepositionDto
														.getNMenge()));
						tempDto.getStuecklistepositionDto().setNZielmenge(
								tempDto.getStuecklistepositionDto()
										.getNZielmenge()
										.add(stuecklistepositionDto
												.getNZielmenge()));
						strukturMap.set(k, tempDto);
						bGefunden = true;
						break;
					}
				}
				if (bGefunden == false) {
					strukturMap.add(new StuecklisteMitStrukturDto(iEbene,
							stuecklistepositionDto));
				}
			} else {
				strukturMap.add(new StuecklisteMitStrukturDto(iEbene,
						stuecklistepositionDto));
			}

			if (bMitUnterstuecklisten == true) {
				try {
					Query query = em
							.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
					query.setParameter(1, returnArray[i].getFlrartikel()
							.getI_id());
					query.setParameter(2, theClientDto.getMandant());
					Stueckliste stkl = (Stueckliste) query.getSingleResult();
					if (stkl != null) {

						// wenn Fremdfertigungsstueckliste, dann keine
						// Aufloesung
						if (Helper.short2boolean(stkl.getBFremdfertigung()) == false) {
							query = em
									.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionen = query.getResultList()
									.size();
							query = em
									.createNamedQuery("StuecklistearbeitsplanfindByStuecklisteIId");
							query.setParameter(1, stkl.getIId());
							int iAnzahlPositionenArbeitsplan = query
									.getResultList().size();
							// Wenn keine Positionen und kein Arbeitsplan, dann
							// auch keinen Aufloesung
							if (iAnzahlPositionen
									+ iAnzahlPositionenArbeitsplan > 0) {
								StuecklisteMitStrukturDto strukturTemp = (StuecklisteMitStrukturDto) strukturMap
										.get(strukturMap.size() - 1);
								strukturTemp.setBStueckliste(true);
								strukturTemp
										.setStuecklisteDto(assembleStuecklisteDto(stkl));
								strukturMap.set(strukturMap.size() - 1,
										strukturTemp);
								// Wenn Unterstklstrukur nicht belassen, dann
								// Kopfstuecklisten weglassen (lt. WH
								// 2008-06-09)
								if (bUnterstklstrukurBelassen == false) {
									strukturMap.remove(strukturMap.size() - 1);
									iEbene--;
								}

								strukturMap = getStrukturDatenEinerStueckliste(
										stkl.getIId(), theClientDto,
										iOptionSortierung, iEbene + 1,
										strukturMap, bMitUnterstuecklisten,
										bGleichePositionenZusammenfassen,
										nLosgroesse,
										stuecklistepositionDto.getNZielmenge(),
										bUnterstklstrukurBelassen); // .multiply(
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
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, ex1);
				}
			}
		}

		session.close();

		return strukturMap;
	}

	public BigDecimal berechneStuecklistenGestehungspreisAusPositionen(
			Integer artikelIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal bdSummeGestpreis = null;

		Integer stuecklisteIId = null;
		try {
			Query query = em
					.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
			query.setParameter(1, artikelIId);
			query.setParameter(2, theClientDto.getMandant());
			Stueckliste stueckliste = (Stueckliste) query.getSingleResult();
			if (stueckliste == null) {
				return null;
			}
			stuecklisteIId = stueckliste.getIId();
		} catch (NoResultException ex2) {
			return null;
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		try {
			Query query = em
					.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
			query.setParameter(1, stuecklisteIId);
			Collection<?> cl = query.getResultList();
			StuecklistepositionDto[] dtos = assembleStuecklistepositionDtos(cl);
			bdSummeGestpreis = new BigDecimal(0);

			for (int i = 0; i < dtos.length; i++) {
				try {
					query = em
							.createNamedQuery("StuecklistefindByArtikelIIdMandantCNr");
					query.setParameter(1, dtos[i].getArtikelIId());
					query.setParameter(2, theClientDto.getMandant());
					Stueckliste stueckliste1 = (Stueckliste) query
							.getSingleResult();
				} catch (NoResultException ex) {
					try {
						bdSummeGestpreis = bdSummeGestpreis
								.add(getLagerFac()
										.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
												dtos[i].getArtikelIId(),
												theClientDto));
					} catch (RemoteException ex1) {
						throwEJBExceptionLPRespectOld(ex1);
					}
				}
			}
		} catch (NoResultException ex3) {
			// NIX
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}

		return bdSummeGestpreis;
	}

	public BigDecimal berechneZielmenge(Integer stuecklistepositionIId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// Menge nach Zieleinheit umrechnen

		StuecklistepositionDto stuecklistepositionDto = stuecklistepositionFindByPrimaryKey(
				stuecklistepositionIId, theClientDto);

		StuecklisteDto stuecklisteDto = stuecklisteFindByPrimaryKey(
				stuecklistepositionDto.getStuecklisteIId(), theClientDto);

		BigDecimal bdMenge = stuecklistepositionDto.getNMenge();

		try {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					stuecklistepositionDto.getArtikelIId(), theClientDto);

			EinheitDto einheitDto = getSystemFac().einheitFindByPrimaryKey(
					artikelDto.getEinheitCNr(), theClientDto);

			// Positionsmenge nach Zielenge umrechnen
			int dimension = einheitDto.getIDimension().intValue();

			if (dimension == 1) {
				if (stuecklistepositionDto.getFDimension1() != null) {
					bdMenge = bdMenge.multiply(new BigDecimal(
							stuecklistepositionDto.getFDimension1()
									.doubleValue()));
				}
			} else if (dimension == 2) {
				if (stuecklistepositionDto.getFDimension1() != null
						&& stuecklistepositionDto.getFDimension2() != null) {
					bdMenge = bdMenge.multiply(
							new BigDecimal(stuecklistepositionDto
									.getFDimension1().doubleValue())).multiply(
							new BigDecimal(stuecklistepositionDto
									.getFDimension2().doubleValue()));
				}
			} else if (dimension == 3) {
				if (stuecklistepositionDto.getFDimension1() != null
						&& stuecklistepositionDto.getFDimension2() != null
						&& stuecklistepositionDto.getFDimension3() != null) {
					bdMenge = bdMenge
							.multiply(
									new BigDecimal(stuecklistepositionDto
											.getFDimension1().doubleValue()))
							.multiply(
									new BigDecimal(stuecklistepositionDto
											.getFDimension2().doubleValue()))
							.multiply(
									new BigDecimal(stuecklistepositionDto
											.getFDimension3().doubleValue()));
				}
			}

			BigDecimal divisor = getSystemFac().rechneUmInAndereEinheit(
					new BigDecimal(1), artikelDto.getEinheitCNr(),
					stuecklistepositionDto.getEinheitCNr(),
					stuecklistepositionDto.getIId(), theClientDto);
			if (divisor != null && divisor.doubleValue() != 0) {
				bdMenge = bdMenge.divide(divisor, BigDecimal.ROUND_HALF_EVEN);

				bdMenge = Helper.berechneMengeInklusiveVerschnitt(bdMenge,
						artikelDto.getFVerschnittfaktor(),
						artikelDto.getFVerschnittbasis(),
						stuecklisteDto.getNLosgroesse());
			}

			if (stuecklisteDto.getIErfassungsfaktor() != null
					&& stuecklisteDto.getIErfassungsfaktor().intValue() != 0) {
				bdMenge = bdMenge.divide(new BigDecimal(stuecklisteDto
						.getIErfassungsfaktor().doubleValue()), 4,
						BigDecimal.ROUND_HALF_EVEN);
			}

			return bdMenge;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	/**
	 * Zu einem Artikel die Information ueber saemtliche Unterstuecklisten
	 * holen, wenn es sich um eine Stueckliste handelt. <br>
	 * Diese Methode ist rekursiv.
	 * 
	 * @param artikelIIdI
	 *            PK des Artikels
	 * @param bNurZuDruckendeI
	 *            true, wenn nur die zu druckenden Positionen enthalten sein
	 *            sollen
	 * @param stuecklisteInfoDtoI
	 *            der bisherige Sturkturbaum, initialer Aufruf mit null
	 * @param iEbeneI
	 *            auf welcher Ebene befindet sich der momentan behandelte
	 *            Artikel, initialer Aufruf mit 0
	 * @param iStuecklistenaufloesungTiefeI
	 *            bis zu welcher Ebene soll die Stueckliste aufgeloest werden,
	 *            -1 = alle Ebenen, 0 = ohne Positionen, 1 = die erste
	 *            Positionsebene ohne weiter Aufloesung
	 * @param bBerechneGesamtmengeDerUnterpositionInStuecklisteI
	 *            soll die Gesamtmenge der Unterposition in der uebergeordneten
	 *            Stueckliste berechnet werden oder die Menge in der
	 *            uebergeordneten Position
	 * @param nWievieleEinheitenDerUnterpositionInStuecklisteI
	 *            wieviele Einheiten der Unterposition werden in einer Einheit
	 *            der Stueckliste sein
	 * @param bFremdfertigungAufloesenI
	 *            sollen die Unterpositionen einer Fremdfertigung aufgeloest
	 *            werden
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return ArrayList die Liste der Strukturdaten, enthaelt auch die Anzahl
	 *         der Positionen der Stueckliste
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public StuecklisteInfoDto getStrukturdatenEinesArtikels(
			Integer artikelIIdI, boolean bNurZuDruckendeI,
			StuecklisteInfoDto stuecklisteInfoDtoI, int iEbeneI,
			int iStuecklistenaufloesungTiefeI,
			boolean bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
			BigDecimal nWievieleEinheitenDerUnterpositionInStuecklisteI,
			boolean bFremdfertigungAufloesenI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (artikelIIdI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("artikelIIdI == null"));
		}

		// die StuecklistenInfo beim ersten Aufruf initialisieren
		if (stuecklisteInfoDtoI == null) {
			stuecklisteInfoDtoI = new StuecklisteInfoDto(); // Anzahl
			// Positionen =
			// 0
		}

		// UW 23.03.06 Die naechste Ebene der Positionen wird nicht gelesen,
		// wenn...
		// ...die gewuenschte Tiefe der Stuecklistenaufloesung == 0 ist
		// ...die gewuenschte Tiefe der Stuecklistenaufloesung bereits
		// erreicht ist, wobei gilt:
		// iEbeneI 0 => gewuenschte Tiefe 1, iEbeneI 1 => gewuenschte Tiefe
		// 2 usw.
		if ((iStuecklistenaufloesungTiefeI != 0 && iEbeneI < iStuecklistenaufloesungTiefeI)
				|| iStuecklistenaufloesungTiefeI == -1) {
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIIdI,
							theClientDto);

			// wenn die aktuelle Artikelposition eine Stueckliste ist
			if (stuecklisteDto != null) {
				// fuer den Ausgangsartikel hinterlegen, ob es sich um eine
				// Fremdfertigung handelt
				if (stuecklisteInfoDtoI.getAlStuecklisteAufgeloest().size() == 0) {
					stuecklisteInfoDtoI.setBIstFremdfertigung(stuecklisteDto
							.getBFremdfertigung());
				}

				// fuer die letzte eingefuegte Position hinterlegen, ob es
				// sich um eine STKL
				// und/oder eine Fremdfertigung handelt
				int iIndexLetztePosition = stuecklisteInfoDtoI
						.getAlStuecklisteAufgeloest().size() - 1;

				if (iIndexLetztePosition >= 0) {
					((StuecklisteMitStrukturDto) stuecklisteInfoDtoI
							.getAlStuecklisteAufgeloest().get(
									iIndexLetztePosition))
							.setBStueckliste(true);
					((StuecklisteMitStrukturDto) stuecklisteInfoDtoI
							.getAlStuecklisteAufgeloest().get(
									iIndexLetztePosition))
							.setStuecklisteDto(stuecklisteDto);
					((StuecklisteMitStrukturDto) stuecklisteInfoDtoI
							.getAlStuecklisteAufgeloest().get(
									iIndexLetztePosition))
							.setBIstFremdfertigung(Helper
									.short2boolean(stuecklisteDto
											.getBFremdfertigung()));
				}

				// UW 29.03.06 Die naechste Ebene der Positionen wird
				// gelesen, wenn
				// ...es sicht um eine Fremdfertigung handelt, diese aber
				// trotzdem aufgeloest werden soll
				// ...es sich um keine Fremdfertiung handelt
				// es sich um eine Fremdfertigung handelt und die
				// Fremdfertigung nicht aufgeloest werden soll
				if ((bFremdfertigungAufloesenI && Helper
						.short2boolean(stuecklisteDto.getBFremdfertigung()))
						|| !Helper.short2boolean(stuecklisteDto
								.getBFremdfertigung())) {
					Session session = null;

					try {
						SessionFactory factory = FLRSessionFactory.getFactory();
						session = factory.openSession();

						// Hiberante Criteria fuer alle Tabellen ausgehend
						// von meiner Haupttabelle anlegen,
						// nach denen ich filtern und sortieren kann
						Criteria crit = session
								.createCriteria(FLRStuecklisteposition.class);

						crit.add(Restrictions
								.eq(StuecklisteFac.FLR_STUECKLISTEPOSITION_STUECKLISTE_I_ID,
										stuecklisteDto.getIId()));

						// alle Positionen der Stueckliste, die mitgedruckt
						// werden sollen
						if (bNurZuDruckendeI) {
							crit.add(Restrictions
									.eq(StuecklisteFac.FLR_STUECKLISTEPOSITION_B_MITDRUCKEN,
											Helper.boolean2Short(bNurZuDruckendeI)));
						}

						crit.addOrder(Order
								.asc(StuecklisteFac.FLR_STUECKLISTEPOSITION_I_SORT));

						List<?> list = crit.list();

						Iterator<?> it = list.iterator();

						while (it.hasNext()) {
							FLRStuecklisteposition flrstuecklisteposition = (FLRStuecklisteposition) it
									.next();

							ArtikelDto artikelDtoSmall = new ArtikelDto();
							artikelDtoSmall.setCNr(flrstuecklisteposition
									.getFlrartikel().getC_nr());

							StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
							stuecklistepositionDto
									.setArtikelIId(flrstuecklisteposition
											.getFlrartikel().getI_id());
							stuecklistepositionDto
									.setArtikelDto(artikelDtoSmall);
							stuecklistepositionDto
									.setIId(flrstuecklisteposition.getI_id());
							stuecklistepositionDto
									.setEinheitCNr(flrstuecklisteposition
											.getEinheit_c_nr());

							if (flrstuecklisteposition.getFlrartikel()
									.getI_id() == 31) {
								int u = 0;
							}
							if (flrstuecklisteposition.getFlrartikel()
									.getArtikelart_c_nr()
									.equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
								stuecklistepositionDto.setSHandeingabe("");
							}
							BigDecimal nMengeUnterposition = flrstuecklisteposition
									.getN_menge();

							// Menge nach Zieleinheit umrechnen

							EinheitDto einheitDto = getSystemFac()
									.einheitFindByPrimaryKey(
											flrstuecklisteposition
													.getEinheit_c_nr(),
											theClientDto);

							// Positionsmenge nach Zielenge umrechnen
							int dimension = einheitDto.getIDimension()
									.intValue();

							if (dimension == 1) {
								if (flrstuecklisteposition.getF_dimension1() != null) {
									nMengeUnterposition = nMengeUnterposition
											.multiply(new BigDecimal(
													flrstuecklisteposition
															.getF_dimension1()
															.doubleValue()));
								}
							} else if (dimension == 2) {
								if (flrstuecklisteposition.getF_dimension1() != null
										&& flrstuecklisteposition
												.getF_dimension2() != null) {
									nMengeUnterposition = nMengeUnterposition
											.multiply(
													new BigDecimal(
															flrstuecklisteposition
																	.getF_dimension1()
																	.doubleValue()))
											.multiply(
													new BigDecimal(
															flrstuecklisteposition
																	.getF_dimension2()
																	.doubleValue()));
								}
							} else if (dimension == 3) {
								if (flrstuecklisteposition.getF_dimension1() != null
										&& flrstuecklisteposition
												.getF_dimension2() != null
										&& flrstuecklisteposition
												.getF_dimension3() != null) {
									nMengeUnterposition = nMengeUnterposition
											.multiply(
													new BigDecimal(
															flrstuecklisteposition
																	.getF_dimension1()
																	.doubleValue()))
											.multiply(
													new BigDecimal(
															flrstuecklisteposition
																	.getF_dimension2()
																	.doubleValue()))
											.multiply(
													new BigDecimal(
															flrstuecklisteposition
																	.getF_dimension3()
																	.doubleValue()));
								}
							}
							BigDecimal faktor = null;
							if (flrstuecklisteposition
									.getFlrartikel()
									.getEinheit_c_nr()
									.equals(flrstuecklisteposition
											.getEinheit_c_nr())) {
								faktor = new BigDecimal(1);
							} else {
								faktor = getSystemFac()
										.rechneUmInAndereEinheit(
												new BigDecimal(1),
												flrstuecklisteposition
														.getFlrartikel()
														.getEinheit_c_nr(),
												flrstuecklisteposition
														.getEinheit_c_nr(),
												flrstuecklisteposition
														.getI_id(),
												theClientDto);
							}
							if (faktor.doubleValue() != 0) {
								nMengeUnterposition = nMengeUnterposition
										.divide(faktor,
												BigDecimal.ROUND_HALF_EVEN);

								nMengeUnterposition = Helper
										.berechneMengeInklusiveVerschnitt(
												nMengeUnterposition,
												flrstuecklisteposition
														.getFlrartikel()
														.getF_verschnittfaktor(),
												flrstuecklisteposition
														.getFlrartikel()
														.getF_verschnittbasis(),
												nWievieleEinheitenDerUnterpositionInStuecklisteI);

								// PJ 14352
								nMengeUnterposition = nMengeUnterposition
										.divide(new BigDecimal(
												flrstuecklisteposition
														.getFlrstueckliste()
														.getI_erfassungsfaktor()
														.doubleValue()), 4,
												BigDecimal.ROUND_HALF_EVEN);

								stuecklistepositionDto
										.setNZielmenge(nMengeUnterposition);
							} else {
								ArrayList<Object> al = new ArrayList<Object>();

								EinheitDto von = getSystemFac()
										.einheitFindByPrimaryKey(
												flrstuecklisteposition
														.getEinheit_c_nr(),
												theClientDto);
								EinheitDto zu = getSystemFac()
										.einheitFindByPrimaryKey(
												flrstuecklisteposition
														.getFlrartikel()
														.getEinheit_c_nr(),
												theClientDto);
								al.add(von.formatBez() + " <-> "
										+ zu.formatBez());
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT,
										al,
										new Exception(
												"FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT"));
							}

							// PJ 14352
							if (stuecklisteDto.getIErfassungsfaktor() != null
									&& stuecklisteDto.getIErfassungsfaktor()
											.intValue() != 0) {
								nMengeUnterposition = nMengeUnterposition
										.divide(new BigDecimal(stuecklisteDto
												.getIErfassungsfaktor()
												.doubleValue()), 4,
												BigDecimal.ROUND_HALF_EVEN);
							}

							if (bBerechneGesamtmengeDerUnterpositionInStuecklisteI) {
								// die Menge der Unterposition muss man
								// multiplizieren mit der
								// Menge der uebergeordneten Position, die
								// in einer Einheit der Stuckliste enthalten
								// ist
								nMengeUnterposition = nMengeUnterposition
										.multiply(nWievieleEinheitenDerUnterpositionInStuecklisteI);
							}

							stuecklistepositionDto
									.setNMenge(nMengeUnterposition);

							StuecklisteMitStrukturDto stuecklisteMitStrukturDto = new StuecklisteMitStrukturDto(
									iEbeneI, stuecklistepositionDto);

							stuecklisteInfoDtoI.getAlStuecklisteAufgeloest()
									.add(stuecklisteMitStrukturDto);

							int iAnzahlPositionen = stuecklisteInfoDtoI
									.getIiAnzahlPositionen().intValue();
							iAnzahlPositionen++;
							stuecklisteInfoDtoI
									.setIIAnzahlPositionen(new Integer(
											iAnzahlPositionen));

							stuecklisteInfoDtoI = getStrukturdatenEinesArtikels(
									stuecklistepositionDto.getArtikelIId(),
									bNurZuDruckendeI,
									stuecklisteInfoDtoI,
									iEbeneI + 1,
									iStuecklistenaufloesungTiefeI,
									bBerechneGesamtmengeDerUnterpositionInStuecklisteI,
									nMengeUnterposition,
									bFremdfertigungAufloesenI, theClientDto);
						}
					} catch (EJBExceptionLP ex) {
						throw ex;
					} catch (RemoteException ex) {
						throwEJBExceptionLPRespectOld(ex);
					} catch (Throwable t) {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
								new Exception(t));
					} finally {
						try {
							session.close();
						} catch (HibernateException he) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_HIBERNATE, he);
						}
					}
				}
			}
		}

		return stuecklisteInfoDtoI;
	}

	public TreeMap<String, Integer> holeNaechsteEbene(Integer stuecklisteIId,
			TheClientDto theClientDto) {
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

		int iNachkommastellenMenge = 2;
		try {
			iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(
					theClientDto.getMandant());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT pos.flrartikel.i_id,pos.flrartikel.c_nr, (SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=pos.flrartikel.i_id AND spr.Id.locale='"
				+ theClientDto.getLocUiAsString()
				+ "'),pos  FROM FLRStuecklisteposition AS pos WHERE pos.stueckliste_i_id="
				+ stuecklisteIId + " ORDER BY pos.flrartikel.c_nr";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
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

			s = s
					+ " --> "
					+ Helper.formatZahl(pos.getN_menge(),
							iNachkommastellenMenge, theClientDto.getLocUi())
					+ " " + pos.getEinheit_c_nr();

			tm.put(s, artikelIId);

			// }

		}

		session.close();
		return tm;
	}

	public TreeMap<String, Integer> holeAlleWurzelstuecklisten(
			TheClientDto theClientDto) {
		TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		String queryString = "SELECT STKL.I_ID, A.C_NR,(SELECT SPR.C_BEZ FROM WW_ARTIKELSPR SPR WHERE SPR.LOCALE_C_NR='"
				+ sLocUI
				+ "' AND SPR.ARTIKEL_I_ID=A.I_ID ) FROM WW_ARTIKEL A RIGHT OUTER JOIN STK_STUECKLISTE STKL ON A.I_ID = STKL.ARTIKEL_I_ID WHERE (SELECT count(*) FROM STK_STUECKLISTEPOSITION POS WHERE POS.ARTIKEL_I_ID=A.I_ID)=0";
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

	public Integer createFertigungsgruppe(
			FertigungsgruppeDto fertigungsgruppeDto, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getCBez() == null
				|| fertigungsgruppeDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"fertigungsgruppeDto.getCBez() == null || fertigungsgruppeDto.getISort() == null"));
		}
		fertigungsgruppeDto.setMandantCNr(theClientDto.getMandant());
		try {
			Query query = em
					.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, fertigungsgruppeDto.getCBez());
			Fertigungsgruppe doppelt = (Fertigungsgruppe) query
					.getSingleResult();
			if (doppelt != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_FERTIGUNGSGRUPPE.C_BEZ"));
			}
		} catch (NoResultException ex) {
			//
		} catch (NonUniqueResultException ex1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					ex1);
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FERTIGUNGSGRUPPE);
		fertigungsgruppeDto.setIId(pk);

		try {
			Fertigungsgruppe fertigungsgruppe = new Fertigungsgruppe(
					fertigungsgruppeDto.getIId(),
					fertigungsgruppeDto.getMandantCNr(),
					fertigungsgruppeDto.getCBez(),
					fertigungsgruppeDto.getISort());
			em.persist(fertigungsgruppe);
			em.flush();
			setFertigungsgruppeFromFertigungsgruppeDto(fertigungsgruppe,
					fertigungsgruppeDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		return fertigungsgruppeDto.getIId();
	}

	public Integer createKommentarimport(KommentarimportDto dto) {

		try {
			Query query = em
					.createNamedQuery("KommentarimportFindByBelegartCNr");
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
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler
				.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			if (!kiDtos[i].getArtikelkommentarartIId().equals(
					dto.getArtikelkommentarartIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception(
								"STK_KOMMENTARIMPORT.ARTIKELKOMMENTARART_I_I_D"));
			}
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOMMENTARIMPORT);
			dto.setIId(pk);

			Kommentarimport bean = new Kommentarimport(dto.getIId(),
					dto.getBelegartCNr(), dto.getArtikelkommentarartIId());
			em.persist(bean);
			em.flush();
			setKommentarimportFromKommentarimportDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeAlleStuecklistenpositionen(Integer stuecklisteIId) {

		Query queryStklPos = em
				.createNamedQuery("StuecklistepositionfindByStuecklisteIId");
		queryStklPos.setParameter(1, stuecklisteIId);
		Collection<?> clPos = queryStklPos.getResultList();
		Iterator<?> itPos = clPos.iterator();
		while (itPos.hasNext()) {
			Stuecklisteposition pos = (Stuecklisteposition) itPos.next();

			// Vorher Verweis in Arbeitsplan loeschen
			Query query = em
					.createNamedQuery("StuecklistearbeitsplanfindByStuecklistepositionIId");
			query.setParameter(1, pos.getIId());

			Collection<?> cl = query.getResultList();

			Iterator<?> it = cl.iterator();

			while (it.hasNext()) {
				Stuecklistearbeitsplan aplan = (Stuecklistearbeitsplan) it
						.next();
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
			Query query = em
					.createNamedQuery("KommentarimportFindByBelegartCNr");
			query.setParameter(1, dto.getBelegartCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Kommentarimport) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STK_KOMMENTARIMPORT.BELEGART_C_NR"));
			}
		} catch (NoResultException ex) {

		}

		// Es darf nur eine Kommentarart geben
		Query artikelQ = em.createNamedQuery("KommentarimportFindAll");
		KommentarimportDto[] kiDtos = KommentarimportDtoAssembler
				.createDtos(artikelQ.getResultList());

		for (int i = 0; i < kiDtos.length; i++) {
			if (!kiDtos[i].getArtikelkommentarartIId().equals(
					dto.getArtikelkommentarartIId())) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception(
								"STK_KOMMENTARIMPORT.ARTIKELKOMMENTARART_I_I_D"));
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

	public void removeFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto)
			throws EJBExceptionLP {
		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("fertigungsgruppeDto.getIId() == null"));
		}
		// try {
		Fertigungsgruppe toRemove = em.find(Fertigungsgruppe.class,
				fertigungsgruppeDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void updateFertigungsgruppe(FertigungsgruppeDto fertigungsgruppeDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (fertigungsgruppeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("fertigungsgruppeDto == null"));
		}
		if (fertigungsgruppeDto.getIId() == null
				|| fertigungsgruppeDto.getCBez() == null
				|| fertigungsgruppeDto.getISort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"fertigungsgruppeDto.getIId() == null || fertigungsgruppeDto.getCBez() == null || fertigungsgruppeDto.getISort() == null"));
		}
		Integer iId = fertigungsgruppeDto.getIId();
		Fertigungsgruppe fertigungsgruppe = null;
		// try {
		fertigungsgruppe = em.find(Fertigungsgruppe.class, iId);
		if (fertigungsgruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

		try {
			Query query = em
					.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, fertigungsgruppeDto.getCBez());
			Integer iIdVorhanden = ((Fertigungsgruppe) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"STKL_FERTIGUNGSGRUPPE.C_BEZ"));
			}

		} catch (NoResultException ex) {
			// nix
		}
		setFertigungsgruppeFromFertigungsgruppeDto(fertigungsgruppe,
				fertigungsgruppeDto);

	}

	public FertigungsgruppeDto fertigungsgruppeFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		// try {
		Fertigungsgruppe fertigungsgruppe = em
				.find(Fertigungsgruppe.class, iId);
		if (fertigungsgruppe == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleFertigungsgruppeDto(fertigungsgruppe);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public FertigungsgruppeDto[] fertigungsgruppeFindByMandantCNr(
			String mandantCNr, TheClientDto theClientDto) throws EJBExceptionLP {
		if (mandantCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("mandantCNr == null"));
		}

		Map m = getEingeschraenkteFertigungsgruppen(theClientDto);

		if (m == null) {

			Query query2 = em
					.createNamedQuery("FertigungsgruppefindByMandantCNr");
			query2.setParameter(1, mandantCNr);
			Collection<?> cl = query2.getResultList();

			return assembleFertigungsgruppeDtos(cl);
		} else {
			FertigungsgruppeDto[] dtos = new FertigungsgruppeDto[m.size()];
			Iterator it = m.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Integer key = (Integer) it.next();
				dtos[i] = assembleFertigungsgruppeDto(em.find(
						Fertigungsgruppe.class, key));

				i++;
			}

			return dtos;

		}

	}

	public FertigungsgruppeDto fertigungsgruppeFindByMandantCNrCBez(
			String mandantCNr, String cBez) throws EJBExceptionLP {
		if (mandantCNr == null || cBez == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("mandantCNr == null || cBez == null"));
		}
		try {
			Query query = em
					.createNamedQuery("FertigungsgruppefindByMandantCNrCBez");
			query.setParameter(1, mandantCNr);
			query.setParameter(2, cBez);
			Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) query
					.getSingleResult();
			if (fertigungsgruppe == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			return assembleFertigungsgruppeDto(fertigungsgruppe);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	private void setFertigungsgruppeFromFertigungsgruppeDto(
			Fertigungsgruppe fertigungsgruppe,
			FertigungsgruppeDto fertigungsgruppeDto) {
		fertigungsgruppe.setMandantCNr(fertigungsgruppeDto.getMandantCNr());
		fertigungsgruppe.setCBez(fertigungsgruppeDto.getCBez());
		fertigungsgruppe.setISort(fertigungsgruppeDto.getISort());
		fertigungsgruppe.setIFormularnummer(fertigungsgruppeDto
				.getIFormularnummer());
		em.merge(fertigungsgruppe);
		em.flush();
	}

	private FertigungsgruppeDto assembleFertigungsgruppeDto(
			Fertigungsgruppe fertigungsgruppe) {
		return FertigungsgruppeDtoAssembler.createDto(fertigungsgruppe);
	}

	private FertigungsgruppeDto[] assembleFertigungsgruppeDtos(
			Collection<?> fertigungsgruppes) {
		List<FertigungsgruppeDto> list = new ArrayList<FertigungsgruppeDto>();
		if (fertigungsgruppes != null) {
			Iterator<?> iterator = fertigungsgruppes.iterator();
			while (iterator.hasNext()) {
				Fertigungsgruppe fertigungsgruppe = (Fertigungsgruppe) iterator
						.next();
				list.add(assembleFertigungsgruppeDto(fertigungsgruppe));
			}
		}
		FertigungsgruppeDto[] returnArray = new FertigungsgruppeDto[list.size()];
		return (FertigungsgruppeDto[]) list.toArray(returnArray);
	}

	public void createStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws EJBExceptionLP {
		if (stuecklisteartDto == null) {
			return;
		}
		try {
			Stuecklisteart stuecklisteart = new Stuecklisteart(
					stuecklisteartDto.getCNr(), stuecklisteartDto.getCBez(),
					stuecklisteartDto.getISort());
			em.persist(stuecklisteart);
			em.flush();
			setStuecklisteartFromStuecklisteartDto(stuecklisteart,
					stuecklisteartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeStuecklisteart(String cNr) throws EJBExceptionLP {
		// try {
		Stuecklisteart toRemove = em.find(Stuecklisteart.class, cNr);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
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

	public void removeStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws EJBExceptionLP {
		if (stuecklisteartDto != null) {
			String cNr = stuecklisteartDto.getCNr();
			removeStuecklisteart(cNr);
		}
	}

	public void updateStuecklisteart(StuecklisteartDto stuecklisteartDto)
			throws EJBExceptionLP {
		if (stuecklisteartDto != null) {
			String cNr = stuecklisteartDto.getCNr();
			try {
				Stuecklisteart stuecklisteart = em.find(Stuecklisteart.class,
						cNr);
				setStuecklisteartFromStuecklisteartDto(stuecklisteart,
						stuecklisteartDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateStuecklistearts(StuecklisteartDto[] stuecklisteartDtos)
			throws EJBExceptionLP {
		if (stuecklisteartDtos != null) {
			for (int i = 0; i < stuecklisteartDtos.length; i++) {
				updateStuecklisteart(stuecklisteartDtos[i]);
			}
		}
	}

	public StuecklisteartDto stuecklisteartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
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

	private void setStuecklisteartFromStuecklisteartDto(
			Stuecklisteart stuecklisteart, StuecklisteartDto stuecklisteartDto) {
		stuecklisteart.setCBez(stuecklisteartDto.getCBez());
		stuecklisteart.setISort(stuecklisteartDto.getISort());
		em.merge(stuecklisteart);
		em.flush();
	}

	private StuecklisteartDto assembleStuecklisteartDto(
			Stuecklisteart stuecklisteart) {
		return StuecklisteartDtoAssembler.createDto(stuecklisteart);
	}

	private StuecklisteartDto[] assembleStuecklisteartDtos(
			Collection<?> stuecklistearts) {
		List<StuecklisteartDto> list = new ArrayList<StuecklisteartDto>();
		if (stuecklistearts != null) {
			Iterator<?> iterator = stuecklistearts.iterator();
			while (iterator.hasNext()) {
				Stuecklisteart stuecklisteart = (Stuecklisteart) iterator
						.next();
				list.add(assembleStuecklisteartDto(stuecklisteart));
			}
		}
		StuecklisteartDto[] returnArray = new StuecklisteartDto[list.size()];
		return (StuecklisteartDto[]) list.toArray(returnArray);
	}

	public List<Integer> getSeriennrChargennrArtikelIIdsFromStueckliste(
			Integer stuecklisteIId, BigDecimal nmenge, TheClientDto theClientDto)
			throws EJBExceptionLP {
		List<Integer> itemsWithIdentities = new ArrayList<Integer>();
		List<StuecklisteMitStrukturDto> m = getStrukturDatenEinerStueckliste(
				stuecklisteIId, theClientDto,
				StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
				0, null, false, false, nmenge, null, true);
		for (StuecklisteMitStrukturDto stuecklisteMitStrukturDto : m) {
			Integer artikelIId = stuecklisteMitStrukturDto
					.getStuecklistepositionDto().getArtikelIId();
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					artikelIId, theClientDto);
			if (artikelDto.isChargennrtragend()
					|| artikelDto.isSeriennrtragend()) {
				itemsWithIdentities.add(artikelDto.getIId());
			}
		}

		return itemsWithIdentities;
	}
}
