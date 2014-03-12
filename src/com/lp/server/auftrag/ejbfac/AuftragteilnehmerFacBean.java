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
package com.lp.server.auftrag.ejbfac;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.auftrag.ejb.Auftragteilnehmer;
import com.lp.server.auftrag.service.AuftragteilnehmerDto;
import com.lp.server.auftrag.service.AuftragteilnehmerDtoAssembler;
import com.lp.server.auftrag.service.AuftragteilnehmerFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class AuftragteilnehmerFacBean
extends Facade implements AuftragteilnehmerFac {
	@PersistenceContext
	private EntityManager em;


	/**
	 * Einen neuen Auftragsteilnehmer anlegen.
	 * @param auftragteilnehmerDto die Daten des Teilnehmers
	 * @param pUser der aktuelle User
	 * @return Integer PK des Teilnehmers
	 * @throws EJBExceptionLP
	 */
	public Integer createAuftragteilnehmer(AuftragteilnehmerDto
			auftragteilnehmerDto, String pUser)
	throws EJBExceptionLP {
		final String METHOD_NAME = "createAuftragteilnehmer";
		myLogger.entry();

		// @todo check param PJ 3828

		Integer pkTeilnehmer = null;

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			pkTeilnehmer = pkGen.getNextPrimaryKey(PKConst.PK_AUFTRAGTEILNEHMER);

			// festhalten, wer den Datensatz angelegt hat
			TheClientDto client = getTheClient(pUser);

			Auftragteilnehmer auftragteilnehmer = new Auftragteilnehmer(pkTeilnehmer,auftragteilnehmerDto.getISort(),auftragteilnehmerDto.getAuftragIId(),auftragteilnehmerDto.getPartnerIIdAuftragteilnehmer(),auftragteilnehmerDto.getAuftragteilnehmerfunktionIId(),client.getIDPersonal());
			em.persist(auftragteilnehmer);
  em.flush();

			setAuftragteilnehmerFromAuftragteilnehmerDto(auftragteilnehmer,
					auftragteilnehmerDto);

			// @todo eine Aufgabe beim neuen Teilnehmer erzeugen  PJ 3829
		}
		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return pkTeilnehmer;
	}


	/**
	 * Einen Auftragteilnehmer ueber seinen key aus der DB loeschen.
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 */
	private void removeAuftragteilnehmer(Integer iId)
	throws EJBExceptionLP {
		//   try {
Auftragteilnehmer toRemove = em.find(Auftragteilnehmer.class, iId);
if (toRemove == null) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
}
try {
  em.remove(toRemove);
  em.flush();
} catch (EntityExistsException er) {
  throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
}
		//   }
		//  catch (RemoveException ex) {
		//     throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		//  }
	}


	/**
	 * Einen Auftragteilnehmer von der der db loeschen.
	 * @param auftragteilnehmerDto AuftragteilnehmerDto
	 * @throws EJBExceptionLP
	 */
	public void removeAuftragteilnehmer(AuftragteilnehmerDto auftragteilnehmerDto)
	throws EJBExceptionLP {
		final String METHOD_NAME = "removeAuftragteilnehmer";
		myLogger.entry();



		try {
			removeAuftragteilnehmer(auftragteilnehmerDto.getIId());

			sortierungAnpassenBeiLoeschenEinerPosition(
					auftragteilnehmerDto.getAuftragIId(),
					auftragteilnehmerDto.getISort().intValue());
		}
		catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.
					FEHLER_BEIM_LOESCHEN, new Exception(t));
		}
	}


	/**
	 * Einen Auftragsteilnehmer aktualisieren.
	 * @param auftragteilnehmerDto AuftragteilnehmerDto
	 * @throws EJBExceptionLP
	 */
	public void updateAuftragteilnehmer(AuftragteilnehmerDto auftragteilnehmerDto)
	throws EJBExceptionLP {
		final String METHOD_NAME = "updateAuftragteilnehmer";
		myLogger.entry();

		// @todo check param PJ 3828

		Integer iId = auftragteilnehmerDto.getIId();

		//    try {
		Auftragteilnehmer auftragteilnehmer = em.find(Auftragteilnehmer.class, iId);
		if (auftragteilnehmer == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setAuftragteilnehmerFromAuftragteilnehmerDto(auftragteilnehmer,
				auftragteilnehmerDto);
		//     }
		//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		//     }
	}


	/**
	 * Einen bestimmten Auftragteilnehmer ueber seinen Schluessel holen.
	 * @param iId Integer
	 * @throws EJBExceptionLP
	 * @return AuftragteilnehmerDto
	 */
	public AuftragteilnehmerDto auftragteilnehmerFindByPrimaryKey(Integer iId)
	throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.
					FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iId == null"));
		}

		AuftragteilnehmerDto teilnehmerDto = null;

		//    try {
		Auftragteilnehmer teilnehmer = em.find(Auftragteilnehmer.class, iId);
		if (teilnehmer == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		teilnehmerDto = this.assembleAuftragteilnehmerDto(teilnehmer);
		//     }
		//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		//     }

		return teilnehmerDto;
	}


	public AuftragteilnehmerDto[] auftragteilnehmerFindByPartnerIIdAuftragteilnehmer(Integer iId)
	throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.
					FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("iId == null"));
		}

		AuftragteilnehmerDto[] teilnehmerDtos = null;

		//    try {
		Query query = em.createNamedQuery("AuftragteilnehmerfindByPartnerIidTeilnehmer");
		query.setParameter(1, iId);
		Collection<?> cTeilnehmer = query.getResultList();
//		if (cTeilnehmer.isEmpty()) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
//		}
		teilnehmerDtos = this.assembleAuftragteilnehmerDtos(cTeilnehmer);
		//     }
		//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		//     }

		return teilnehmerDtos;
	}

	/**
	 * Feststellen, ob ein bestimmter Partner bereits in der Teilnehmerliste eines
	 * bestimmten Auftrags enthalten ist.
	 * @param iIdPartnerI PK des Partners
	 * @param iIdAuftragI PK des Auftrags
	 * @return boolean true, wenn der Partner in der Liste der Teilnehmer enthalten ist
	 * @throws EJBExceptionLP Ausnahme
	 */
	public boolean istPartnerEinAuftragteilnehmer(Integer iIdPartnerI,
			Integer iIdAuftragI)
	throws EJBExceptionLP {
		boolean bIstTeilnehmerO = false;



		if (iIdPartnerI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdPartnerI == null"));
		}

		if (iIdAuftragI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAuftragI == null"));
		}

		//    try {
		Query query = em.createNamedQuery("AuftragteilnehmerfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> collection = query.getResultList();
//		if (collection.isEmpty()) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
//		}

		Iterator<?> it = collection.iterator();

		while (it.hasNext()) {
			Auftragteilnehmer auftragteilnehmer = (Auftragteilnehmer) it.next();

			if (auftragteilnehmer.getPartnerIIdAuftragteilnehmer().intValue() ==
				iIdPartnerI.intValue()) {
				bIstTeilnehmerO = true;
			}
		}
		//     }
	//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		//     }

		return bIstTeilnehmerO;
	}


	// Methoden zur Bestimmung der Sortierung der Auftragteilnehmer --------------

	/**
	 * Das maximale iSort bei den Teilnehmer fuer einen bestimmten
	 * Auftrag bestimmen.
	 * @param iIdAuftragI der aktuelle Auftrag
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer getMaxISort(Integer iIdAuftragI)
	throws EJBExceptionLP {
	    Integer iiMaxISortO = null;
		try{
		Query query = em.createNamedQuery("AuftragteilnehmerejbSelectMaxISort");
		query.setParameter(1, iIdAuftragI);
		iiMaxISortO =(Integer) query.getSingleResult();
		if (iiMaxISortO == null) {
	        iiMaxISortO = new Integer(0);
	      }
		}
		catch(Throwable e){
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, new Exception(e));
		}
		return iiMaxISortO;
	}


	/**
	 * Zwei bestehende Teilnehmer in Bezug auf ihr iSort umreihen.
	 * @param iIdPosition1I PK der ersten Position
	 * @param iIdPosition2I PK der zweiten Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void vertauscheAuftragteilnehmer(Integer iIdPosition1I,
			Integer iIdPosition2I)
	throws EJBExceptionLP {
		final String METHOD_NAME = "vertauscheAuftragteilnehmer";
		myLogger.entry();

		//    try {
		Auftragteilnehmer oPosition1 =em.find(Auftragteilnehmer.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		Auftragteilnehmer oPosition2 =em.find(Auftragteilnehmer.class, iIdPosition2I);

		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// das zweite iSort auf ungueltig setzen, damit UK constraint nicht verletzt wird
		oPosition2.setISort(new Integer( -1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);
		//     }
		//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		//     }
	}


	/**
	 * Wenn eine neue Position im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz.
	 * <br>Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * @param iIdAuftragI der aktuelle Auftrag
	 * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdAuftragI,
			int iSortierungNeuePositionI)
	throws EJBExceptionLP {
		//    try {
		Query query = em.createNamedQuery("AuftragteilnehmerfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> cl = query.getResultList();
// 		if (cl.isEmpty()) {
// 			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
// 		}
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Auftragteilnehmer oPosition = (Auftragteilnehmer) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		//     }
	//     catch (FinderException ex) {
		//       throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		//     }
	}


	/**
	 * Wenn fuer einen Auftrag eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken entstehen.
	 * <br>Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * @param iIdAuftragI PK des Auftrags
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @throws Throwable Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(Integer iIdAuftragI,
			int iSortierungGeloeschtePositionI)
	throws Throwable {
		Query query = em.createNamedQuery("AuftragteilnehmerfindByAuftrag");
		query.setParameter(1, iIdAuftragI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Auftragteilnehmer oPosition = (Auftragteilnehmer) it.next();

			if (oPosition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				oPosition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}


	private void setAuftragteilnehmerFromAuftragteilnehmerDto(Auftragteilnehmer auftragteilnehmer, AuftragteilnehmerDto auftragteilnehmerDto) {
		if (auftragteilnehmerDto.getISort() != null) {
			auftragteilnehmer.setISort(auftragteilnehmerDto.getISort());
		}
		if (auftragteilnehmerDto.getAuftragIId() != null) {
			auftragteilnehmer.setAuftragIId(auftragteilnehmerDto.getAuftragIId());
		}
		auftragteilnehmer.setBIstexternerteilnehmer(auftragteilnehmerDto.
				getBIstExternerTeilnehmer());
		if (auftragteilnehmerDto.getPartnerIIdAuftragteilnehmer() != null) {
			auftragteilnehmer.setPartnerIIdAuftragteilnehmer(auftragteilnehmerDto.
					getPartnerIIdAuftragteilnehmer());
		}
		auftragteilnehmer.setFunktionIId(auftragteilnehmerDto.
				getAuftragteilnehmerfunktionIId());
		if (auftragteilnehmerDto.getTAnlegen() != null) {
			auftragteilnehmer.setTAnlegen(auftragteilnehmerDto.getTAnlegen());
		}
		if (auftragteilnehmerDto.getPersonalIIDAnlegen() != null) {
			auftragteilnehmer.setPersonalIIdAnlegen(auftragteilnehmerDto.
					getPersonalIIDAnlegen());
		}
		em.merge(auftragteilnehmer);
  em.flush();
	}


	private AuftragteilnehmerDto assembleAuftragteilnehmerDto(Auftragteilnehmer
			auftragteilnehmer) {
		return AuftragteilnehmerDtoAssembler.createDto(auftragteilnehmer);
	}


	private AuftragteilnehmerDto[] assembleAuftragteilnehmerDtos(Collection<?>
			auftragteilnehmers) {
		List<AuftragteilnehmerDto> list = new ArrayList<AuftragteilnehmerDto>();
		if (auftragteilnehmers != null) {
			Iterator<?> iterator = auftragteilnehmers.iterator();
			while (iterator.hasNext()) {
				Auftragteilnehmer auftragteilnehmer = (Auftragteilnehmer) iterator.next();
				list.add(assembleAuftragteilnehmerDto(auftragteilnehmer));
			}
		}
		AuftragteilnehmerDto[] returnArray = new AuftragteilnehmerDto[list.size()];
		return (AuftragteilnehmerDto[]) list.toArray(returnArray);
	}


}
