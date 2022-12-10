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
package com.lp.server.anfrage.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.anfrage.ejb.Anfragepositionlieferdaten;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.anfrage.service.AnfragepositionDtoAssembler;
import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDto;
import com.lp.server.anfrage.service.AnfragepositionlieferdatenDtoAssembler;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ErsatztypenDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Beleg;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.isort.CompositeISort;
import com.lp.server.util.isort.IPrimitiveSwapper;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AnfragepositionFacBean extends Beleg implements AnfragepositionFac, IPrimitiveSwapper {

	@PersistenceContext
	private EntityManager em;

	// Anfrageposition
	// -----------------------------------------------------------

	public Integer createAnfrageposition(AnfragepositionDto anfragepositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return createAnfrageposition(anfragepositionDtoI, false, theClientDto);
	}

	public Integer createAnfrageposition(AnfragepositionDto anfragepositionDtoI, boolean bMitErsatztypen,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAnfragepositionDto(anfragepositionDtoI);

		Integer iIdAnfrageposition = new Integer(-1);
		pruefePflichtfelderBelegposition(anfragepositionDtoI, theClientDto);
		try {
			getAnfrageFac().pruefeUndSetzeAnfragestatusBeiAenderung(anfragepositionDtoI.getBelegIId(), true,
					theClientDto);

			// eventuell einen Handartikel anlegen
			if (anfragepositionDtoI.getPositionsartCNr()
					.equalsIgnoreCase(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(anfragepositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(anfragepositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(anfragepositionDtoI.getEinheitCNr());

				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

				anfragepositionDtoI.setArtikelIId(iIdArtikel);

				myLogger.info("Handartikel wurde angelegt, iIdArtikel:" + iIdArtikel);
			}

			// PK generieren
			iIdAnfrageposition = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_ANFRAGEPOSITION);

			// Sortierung: falls nicht anders definiert, hinten dran haengen.
			if (anfragepositionDtoI.getISort() == null) {
				int iSortNeu = getMaxISort(anfragepositionDtoI.getBelegIId()) + 1;
				anfragepositionDtoI.setISort(iSortNeu);
			}

			Anfrageposition anfrageposition = new Anfrageposition(iIdAnfrageposition, anfragepositionDtoI.getBelegIId(),
					anfragepositionDtoI.getISort(), anfragepositionDtoI.getPositionsartCNr());
			em.persist(anfrageposition);
			em.flush();

			setAnfragepositionFromAnfragepositionDto(anfrageposition, anfragepositionDtoI);

			getBelegartmediaFac().kopiereBelegartmedia(anfragepositionDtoI.getUsecaseIIdQuelle(),
					anfragepositionDtoI.getIKeyQuelle(), QueryParameters.UC_ID_ANFRAGEPOSITION, iIdAnfrageposition,
					theClientDto);

			// parallel eine Dummy Anfragepositionlieferdaten anlegen
			if (anfragepositionDtoI.getPositionsartCNr().equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
					|| anfragepositionDtoI.getPositionsartCNr()
							.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
				AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = new AnfragepositionlieferdatenDto();
				anfragepositionlieferdatenDto.setAnfragepositionIId(iIdAnfrageposition);

				// auskommentiert aufgrund SP6581
				// anfragepositionlieferdatenDto.setIAnlieferzeit(new Integer(0));

				anfragepositionlieferdatenDto.setNAnliefermenge(Helper.getBigDecimalNull());
				anfragepositionlieferdatenDto.setNNettogesamtpreis(Helper.getBigDecimalNull());
				anfragepositionlieferdatenDto.setNNettogesamtpreisminusrabatt(Helper.getBigDecimalNull());

				anfragepositionlieferdatenDto.setBErfasst(new Short((short) 0));

				createAnfragepositionlieferdaten(anfragepositionlieferdatenDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		// eine Ebene Ersatztypen anlegen
		if (bMitErsatztypen) {
			if (anfragepositionDtoI.getPositionsartCNr().equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
					&& anfragepositionDtoI.getArtikelIId() != null) {
				ErsatztypenDto[] ersatztypenDtos = getArtikelFac()
						.ersatztypenFindByArtikelIId(anfragepositionDtoI.getArtikelIId());
				for (int k = 0; k < ersatztypenDtos.length; k++) {
					anfragepositionDtoI.setArtikelIId(ersatztypenDtos[k].getArtikelIIdErsatz());
					anfragepositionDtoI.setIId(null);

					// SP5947
					anfragepositionDtoI.setISort(null);

					anfragepositionDtoI.setAnfragepositionIdZugehoerig(iIdAnfrageposition);
					createAnfrageposition(anfragepositionDtoI, false, theClientDto);

				}
			}

		}

		return iIdAnfrageposition;
	}

	/**
	 * Eine bestehende Anfrageposition loeschen.
	 * 
	 * @param anfragepositionDtoI die Anfrageposition
	 * @param theClientDto        der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeAnfrageposition(AnfragepositionDto anfragepositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfragepositionDto(anfragepositionDtoI);

		try {
			getAnfrageFac().pruefeUndSetzeAnfragestatusBeiAenderung(anfragepositionDtoI.getBelegIId(), true,
					theClientDto);

			// Die Ersatztypen loeschen
			Query query = em.createNamedQuery("AnfragepositionFindByAnfragepositionIdZugehoerig");
			query.setParameter(1, anfragepositionDtoI.getIId());
			Collection c = query.getResultList();
			Iterator it = c.iterator();

			while (it.hasNext()) {
				Anfrageposition ald = (Anfrageposition) it.next();
				removeAnfrageposition(anfragepositionFindByPrimaryKey(ald.getIId(), theClientDto), theClientDto);
			}

			// zuerst die zugehoerige Anfragepositionlieferdaten loeschen
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
					anfragepositionDtoI.getIId());

			if (anfragepositionlieferdatenDto != null) {
				removeAnfragepositionlieferdaten(anfragepositionlieferdatenDto, theClientDto);
			}

			Anfrageposition toRemove = em.find(Anfrageposition.class, anfragepositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, new Exception(
						"Fehler beim l\u00F6schen der Anfragenposition. Es gibt keine Anfrageposition mit der Iid "
								+ anfragepositionDtoI.getIId()));
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

			// die Sortierung muss angepasst werden
			sortierungAnpassenBeiLoeschenEinerPosition(anfragepositionDtoI.getBelegIId(),
					anfragepositionDtoI.getISort().intValue());

			// den Nettogesamtwert der Anfrage neu berechnen
			Integer anfrageIId = getAnfrageFac()
					.anfrageFindByPrimaryKey(anfragepositionDtoI.getBelegIId(), theClientDto).getIId();

			getAnfrageFac().setzeNettogesamtwert(anfrageIId,
					getAnfrageFac().berechneNettowertGesamt(anfrageIId, theClientDto), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (RemoveException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, t);
		// }
		catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception(t));
		}

		myLogger.exit("Die Anfrageposition wurde geloescht.");
	}

	/**
	 * Wenn fuer eine Anfrage eine Position geloescht wurden, dann muss die
	 * Sortierung der Positionen angepasst werden, damit keine Luecken entstehen.
	 * <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server aufgerufen.
	 * 
	 * @param iIdAnfrageI                    PK der Anfrage
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @throws Throwable Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPosition(Integer iIdAnfrageI, int iSortierungGeloeschtePositionI)
			throws Throwable {
		Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
		query.setParameter(1, iIdAnfrageI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Anfrageposition anfrageposition = (Anfrageposition) it.next();

			if (anfrageposition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				anfrageposition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}
	}

	/**
	 * Eine bestehende Anfrageposition aktualisieren.
	 * 
	 * @param anfragepositionDtoI die Anfrageposition
	 * @param theClientDto        der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAnfrageposition(AnfragepositionDto anfragepositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAnfragepositionDto(anfragepositionDtoI);
		try {
			pruefePflichtfelderBelegposition(anfragepositionDtoI, theClientDto);
			getAnfrageFac().pruefeUndSetzeAnfragestatusBeiAenderung(anfragepositionDtoI.getBelegIId(), true,
					theClientDto);

			Anfrageposition anfrageposition = em.find(Anfrageposition.class, anfragepositionDtoI.getIId());
			if (anfrageposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
						"Fehler bei updateAnfragePosition. Es gibt keine Anfrageposition mit iid "
								+ anfragepositionDtoI.getIId() + "\nAnfragepositionDto.toString:"
								+ anfragepositionDtoI.toString());
			}

			setAnfragepositionFromAnfragepositionDto(anfrageposition, anfragepositionDtoI);

			// spezielle Behandlung fuer eine Handeingabeposition
			if (anfrageposition.getAnfragepositionartCNr().equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(anfrageposition.getArtikelIId(),
						theClientDto);

				ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();
				oArtikelsprDto.setCBez(anfragepositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(anfragepositionDtoI.getCZusatzbez());

				artikelDto.setArtikelsprDto(oArtikelsprDto);
				artikelDto.setEinheitCNr(anfragepositionDtoI.getEinheitCNr());

				getArtikelFac().updateArtikel(artikelDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
		// t);
		// }

		myLogger.exit("Die Anfrageposition wurde aktualisiert.");
	}

	public AnfragepositionDto anfragepositionFindByPrimaryKey(Integer iIdAnfragepositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// check2(theClientDto);
		// checkAnfragepositionIId(iIdAnfragepositionI);

		AnfragepositionDto anfragepositionDto = anfragepositionFindByPrimaryKeyOhneExc(iIdAnfragepositionI);

		if (anfragepositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"FEhler bei FindAnfrageposition by Primary Key. Es gibt keine Position mit iid "
							+ iIdAnfragepositionI);
		}
		return anfragepositionDto;
	}

	public AnfragepositionDto anfragepositionFindByPrimaryKeyOhneExc(Integer iIdAnfragepositionI) {

		Anfrageposition anfrageposition = em.find(Anfrageposition.class, iIdAnfragepositionI);
		if (anfrageposition == null) {
			return null;
		}
		return assembleAnfragepositionDto(anfrageposition);
	}

	public AnfragepositionDto[] anfragepositionFindByAnfrageIIdArtikelIId(Integer iIdAnfrageI, Integer iIdArtikelI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.info("AnfrageIId: " + iIdAnfrageI + ", ArtikelIId: " + iIdArtikelI);

		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdAnfrageI == null"));
		}

		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("iIdArtikelI == null"));
		}

		AnfragepositionDto[] aAnfragepositionDto = null;

		// try {
		Query query = em.createNamedQuery("AnfragepositionfindByAnfrageIIdArtikelIId");
		query.setParameter(1, iIdAnfrageI);
		query.setParameter(2, iIdArtikelI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, null);
		// }

		aAnfragepositionDto = assembleAnfragepositionDtos(cl);
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FIND, t);
		// }

		return aAnfragepositionDto;
	}

	/**
	 * Das maximale iSort bei den Anfragepositionen fuer eine bestimmte Anfrage
	 * bestimmen.
	 * 
	 * @param iIdAnfrageI PK der Anfrage
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	private Integer getMaxISort(Integer iIdAnfrageI) {
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("AnfragepositionejbSelectMaxISort");
			query.setParameter(1, iIdAnfrageI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Exception t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, t);
		}
		return iiMaxISortO;
	}

	public void vertauscheAnfragepositionenMinus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP {
		CompositeISort<Anfrageposition> comp = new CompositeISort<Anfrageposition>(
				new AnfragepositionSwapper(this, em));
		comp.vertauschePositionenMinus(iIdBasePosition, possibleIIds);

	}

	public void vertauscheAnfragepositionenPlus(Integer iIdBasePosition, List<Integer> possibleIIds,
			TheClientDto theClientDto) throws EJBExceptionLP {
		CompositeISort<Anfrageposition> comp = new CompositeISort<Anfrageposition>(
				new AnfragepositionSwapper(this, em));
		comp.vertauschePositionenPlus(iIdBasePosition, possibleIIds);

	}

	public void vertauschePositionen(Integer iIdAnfrageposition1I, Integer iIdAnfrageposition2I) throws EJBExceptionLP {

		myLogger.info("Vertausche: " + iIdAnfrageposition1I + ", " + iIdAnfrageposition2I);
		Anfrageposition oPosition1 = em.find(Anfrageposition.class, iIdAnfrageposition1I);
		Anfrageposition oPosition2 = em.find(Anfrageposition.class, iIdAnfrageposition2I);
		Integer iSort1 = oPosition1.getISort();
		Integer iSort2 = oPosition2.getISort();

		// das zweite iSort auf ungueltig setzen, damit UK constraint
		// nicht verletzt wird
		oPosition2.setISort(new Integer(-1));

		oPosition1.setISort(iSort2);
		oPosition2.setISort(iSort1);

		myLogger.exit("Positionen vertauscht.");
	}

	/**
	 * Zwei bestehende Anfragepositionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPosition1I PK der ersten Position
	 * @param iIdPosition2I PK der zweiten Position
	 * @param theClientDto  der aktuelle Benutzer
	 * @throws EJBExceptionLP  Ausnahme
	 * @throws RemoteException
	 */
	public void vertauscheAnfragepositionen(Integer iIdPosition1I, Integer iIdPosition2I, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		myLogger.entry();

		Anfrageposition oPosition1 = em.find(Anfrageposition.class, iIdPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"Fehler beim suchen der Anfrageposition. Es gibt keine Position mit der IId " + iIdPosition1I));
		}

		Anfrageposition oPosition2 = em.find(Anfrageposition.class, iIdPosition2I);

		AnfrageDto anfrageDto = null;
		try {
			anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(oPosition1.getAnfrageIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		if (anfrageDto.getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT)) {
			getAnfrageFac().pruefeUndSetzeAnfragestatusBeiAenderung(anfrageDto.getIId(), true, theClientDto);

			Integer iSort1 = oPosition1.getISort();
			Integer iSort2 = oPosition2.getISort();

			// das zweite iSort auf ungueltig setzen, damit UK constraint nicht
			// verletzt wird
			oPosition2.setISort(new Integer(-1));

			oPosition1.setISort(iSort2);
			oPosition2.setISort(iSort1);
		}
	}

	public void sortiereNachArtikelnummer(Integer anfrageIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
		query.setParameter(1, anfrageIId);
		AnfragepositionDto[] dtos = assembleAnfragepositionDtos(query.getResultList());

		for (int i = dtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				AnfragepositionDto o = dtos[j];

				AnfragepositionDto o1 = dtos[j + 1];

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
			Anfrageposition pos = em.find(Anfrageposition.class, dtos[i].getIId());

			pos.setISort(iSort);

			em.merge(pos);
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
	 * @param iIdAnfrageI              PK der Anfrage
	 * @param iSortierungNeuePositionI die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer iIdAnfrageI,
			int iSortierungNeuePositionI) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
		query.setParameter(1, iIdAnfrageI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Anfrageposition oPosition = (Anfrageposition) it.next();

			if (oPosition.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPosition.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		// }
	}

	private void setAnfragepositionFromAnfragepositionDto(Anfrageposition anfrageposition,
			AnfragepositionDto anfragepositionDto) {

		anfrageposition.setAnfrageIId(anfragepositionDto.getBelegIId());
		anfrageposition.setISort(anfragepositionDto.getISort());
		anfrageposition.setAnfragepositionartCNr(anfragepositionDto.getPositionsartCNr());
		anfrageposition.setArtikelIId(anfragepositionDto.getArtikelIId());

		anfrageposition.setCBez(anfragepositionDto.getCBez());
		anfrageposition.setCZbez(anfragepositionDto.getCZusatzbez());
		anfrageposition.setBArtikelbezeichnunguebersteuert(anfragepositionDto.getBArtikelbezeichnunguebersteuert());
		anfrageposition.setXTextinhalt(anfragepositionDto.getXTextinhalt());
		anfrageposition.setMediastandardIId(anfragepositionDto.getMediastandardIId());

		anfrageposition.setNMenge(Helper.rundeKaufmaennisch(anfragepositionDto.getNMenge(), 4));

		anfrageposition.setEinheitCNr(anfragepositionDto.getEinheitCNr());

		anfrageposition.setNRichtpreis(Helper.rundeKaufmaennisch(anfragepositionDto.getNRichtpreis(), 4));
		anfrageposition.setAnfragepositionIdZugehoerig(anfragepositionDto.getAnfragepositionIdZugehoerig());
		anfrageposition.setLossollmaterialIId(anfragepositionDto.getLossollmaterialIId());
		em.merge(anfrageposition);
		em.flush();
	}

	private AnfragepositionDto assembleAnfragepositionDto(Anfrageposition anfrageposition) {
		return AnfragepositionDtoAssembler.createDto(anfrageposition);
	}

	private AnfragepositionDto[] assembleAnfragepositionDtos(Collection<?> anfragepositions) {
		List<AnfragepositionDto> list = new ArrayList<AnfragepositionDto>();
		if (anfragepositions != null) {
			Iterator<?> iterator = anfragepositions.iterator();
			while (iterator.hasNext()) {
				Anfrageposition anfrageposition = (Anfrageposition) iterator.next();
				list.add(assembleAnfragepositionDto(anfrageposition));
			}
		}
		AnfragepositionDto[] returnArray = new AnfragepositionDto[list.size()];
		return (AnfragepositionDto[]) list.toArray(returnArray);
	}

	private void checkAnfragepositionDto(AnfragepositionDto anfragepositionDtoI) throws EJBExceptionLP {
		if (anfragepositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("anfragepositionDtoI == null"));
		}

		myLogger.info("AnfragepositionDtoI: " + anfragepositionDtoI.toString());
	}

	// Anfragepositionlieferdaten
	// ------------------------------------------------

	/**
	 * Eine Anfragepositionlieferdaten neu anlegen.
	 * 
	 * @param anfragepositionlieferdatenDtoI die Anfragepositionlieferdaten
	 * @param theClientDto                   der aktuelle Benutzer
	 * @return Integer PK der Anfragepositionlieferdaten
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer createAnfragepositionlieferdaten(AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkAnfragepositionlieferdatenDto(anfragepositionlieferdatenDtoI);

		Integer iIdAnfragepositionlieferdaten;

		try {
			// das Neuanlegen einer Anfragepositionlieferdaten veraendert nicht
			// des Status der Anfrage

			// PK generieren
			iIdAnfragepositionlieferdaten = getPKGeneratorObj()
					.getNextPrimaryKey(PKConst.PK_ANFRAGEPOSITIONLIEFERDATEN);

			Anfragepositionlieferdaten anfragepositionlieferdaten = new Anfragepositionlieferdaten(
					iIdAnfragepositionlieferdaten, anfragepositionlieferdatenDtoI.getAnfragepositionIId(),
					anfragepositionlieferdatenDtoI.getIAnlieferzeit(),
					anfragepositionlieferdatenDtoI.getNAnliefermenge(),
					anfragepositionlieferdatenDtoI.getNNettogesamtpreis());
			em.persist(anfragepositionlieferdaten);
			em.flush();

			setAnfragepositionlieferdatenFromAnfragepositionlieferdatenDto(anfragepositionlieferdaten,
					anfragepositionlieferdatenDtoI);

			befuelleZusaetzlichesPreisfeld(iIdAnfragepositionlieferdaten, theClientDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
		myLogger.exit("AnfragelieferdatenIId: " + iIdAnfragepositionlieferdaten);

		return iIdAnfragepositionlieferdaten;
	}

	/**
	 * Fuer eine bestehende Anfragepositionlieferdaten vom Typ Ident oder
	 * Handeingabe wird das zusaetzliche Preisfeld befuellt.
	 * 
	 * @param iIdPositionI PK der Position
	 * @param theClientDto der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void befuelleZusaetzlichesPreisfeld(Integer iIdPositionI, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Anfragepositionlieferdaten anfragepositionlieferdaten = em.find(Anfragepositionlieferdaten.class,
					iIdPositionI);
			if (anfragepositionlieferdaten == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, new Exception(
						"Fehler beim suchen der Anfragepositionlieferdaten. Es gibt keine Lieferdaten f\u00FCr die Position mit der iid "
								+ iIdPositionI));
			}

			Anfrageposition anfrageposition = em.find(Anfrageposition.class,
					anfragepositionlieferdaten.getAnfragepositionIId());

			if (anfrageposition.getAnfragepositionartCNr().equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
					|| anfrageposition.getAnfragepositionartCNr()
							.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {

				AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(anfrageposition.getAnfrageIId(),
						theClientDto);

				// - Allgemeiner Rabatt
				BigDecimal bdAllgemeinerRabatt = new BigDecimal(anfrageDto.getFAllgemeinerRabattsatz().doubleValue())
						.movePointLeft(2);
				bdAllgemeinerRabatt = Helper.rundeKaufmaennisch(bdAllgemeinerRabatt, 4);

				BigDecimal bdNettogesamtpreisAllgemeinerRabattSumme = anfragepositionlieferdaten.getNNettogesamtpreis()
						.multiply(bdAllgemeinerRabatt);
				bdNettogesamtpreisAllgemeinerRabattSumme = Helper
						.rundeKaufmaennisch(bdNettogesamtpreisAllgemeinerRabattSumme, 4);
				anfragepositionlieferdaten.setNNettogesamtpreisminusrabatt(anfragepositionlieferdaten
						.getNNettogesamtpreis().subtract(bdNettogesamtpreisAllgemeinerRabattSumme));
				em.merge(anfragepositionlieferdaten);
				em.flush();
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		// }
	}

	/**
	 * Eine bestehende Anfragepositionlieferdaten loeschen.
	 * 
	 * @param anfragepositionlieferdatenDtoI die Anfragepositionlieferdaten
	 * @param theClientDto                   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeAnfragepositionlieferdaten(AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragepositionlieferdatenDto(anfragepositionlieferdatenDtoI);

		// try {
		// Loeschen der Auftragpositionlieferdaten aendert nicht den Status

		Anfragepositionlieferdaten toRemove = em.find(Anfragepositionlieferdaten.class,
				anfragepositionlieferdatenDtoI.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					new Exception("Die Lieferdaten konnten nicht entfernt werden da es keine Lieferdaten mit iid "
							+ anfragepositionlieferdatenDtoI.getIId() + " gibt"));
		}

		// Anfrageposition pos = em.find(Anfrageposition.class,
		// toRemove.getAnfragepositionIId());
		// pos
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
		// }
		// catch (RemoveException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_LOESCHEN,
		// t);
		// }
	}

	/**
	 * Eine bestehende Anfragepositionlieferdaten loeschen.
	 * 
	 * @param anfragepositionlieferdatenDtoI die Anfragepositionlieferdaten
	 * @param theClientDto                   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void resetAnfragepositionlieferdaten(AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragepositionlieferdatenDto(anfragepositionlieferdatenDtoI);

		try {
			// geloescht wird die Anfragepositionlieferdaten, wenn die
			// zugehoerige
			// Anfrageposition geloescht wird; hier wird sie nur zurueckgesetzt
			anfragepositionlieferdatenDtoI.resetContent();

			Anfragepositionlieferdaten anfragepositionlieferdaten = em.find(Anfragepositionlieferdaten.class,
					anfragepositionlieferdatenDtoI.getIId());
			if (anfragepositionlieferdaten == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						new Exception("Es sind keine Lieferdaten mit iid " + anfragepositionlieferdatenDtoI.getIId()
								+ " vorhanden um zur\u00FCckgesetzt zu werden"));
			}

			setAnfragepositionlieferdatenFromAnfragepositionlieferdatenDto(anfragepositionlieferdaten,
					anfragepositionlieferdatenDtoI);

			// das Loeschen einer Anfragepositionlieferdaten kann den Status
			// einer Anfrage von 'Erfasst' auf 'Offen' setzen
			Anfrageposition anfrageposition = em.find(Anfrageposition.class,
					anfragepositionlieferdatenDtoI.getAnfragepositionIId());

			AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(anfrageposition.getAnfrageIId(),
					theClientDto);

			if (anfrageDto.getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_ERFASST)) {
				// alle Anfragepositionen zur aktuellen Anfrage
				Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
				query.setParameter(1, anfrageDto.getIId());
				Collection<?> cl = query.getResultList();
				Iterator<?> it = cl.iterator();
				int iAnzahl = 0;

				while (it.hasNext()) {
					Anfrageposition anfragepositionNext = (Anfrageposition) it.next();

					if (anfragepositionNext.getAnfragepositionartCNr()
							.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT)
							|| anfragepositionNext.getAnfragepositionartCNr()
									.equals(AnfrageServiceFac.ANFRAGEPOSITIONART_HANDEINGABE)) {
						AnfragepositionlieferdatenDto anfragepositionlieferdatenNext = anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
								anfragepositionNext.getIId());

						if (anfragepositionlieferdatenNext != null
								&& Helper.short2boolean(anfragepositionlieferdatenNext.getBErfasst())) {
							iAnzahl++;
						}
					}
				}

				if (iAnzahl == 0) {
					// es gibt keine erfassten mengenbehafteten Positionen mehr
					getAnfrageFac().setzeAnfragestatus(AnfrageServiceFac.ANFRAGESTATUS_OFFEN, anfrageDto.getIId(),
							theClientDto);
					getAnfrageFac().setzeNettogesamtwert(anfrageDto.getIId(), null, theClientDto);
				} else {
					getAnfrageFac().setzeNettogesamtwert(anfrageDto.getIId(),
							getAnfrageFac().berechneNettowertGesamt(anfrageDto.getIId(), theClientDto), theClientDto);
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		// }
	}

	/**
	 * Eine bestehende Anfragepositionlieferdaten aktualisieren.
	 * 
	 * @param anfragepositionlieferdatenDtoI die Anfragepositionlieferdaten
	 * @param theClientDto                   der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAnfragepositionlieferdaten(AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		checkAnfragepositionlieferdatenDto(anfragepositionlieferdatenDtoI);

		try {
			// die zugehoerigen Datensaetze bestimmen
			Anfrageposition anfrageposition = em.find(Anfrageposition.class,
					anfragepositionlieferdatenDtoI.getAnfragepositionIId());
			if (anfrageposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
						"Fehler bei updateAnfragepositionLieferdaten. Es gibt keine Anfrageposition mit der iid "
								+ anfragepositionlieferdatenDtoI.getAnfragepositionIId()
								+ "\nanfragepositionlieferdatenDto.toString: "
								+ anfragepositionlieferdatenDtoI.toString());
			}
			AnfrageDto anfrageDto = getAnfrageFac().anfrageFindByPrimaryKey(anfrageposition.getAnfrageIId(),
					theClientDto);

			// jede Position wird auf erfasst gesetzt
			anfragepositionlieferdatenDtoI.setBErfasst(new Short((short) 1));

			Anfragepositionlieferdaten anfragepositionlieferdaten = em.find(Anfragepositionlieferdaten.class,
					anfragepositionlieferdatenDtoI.getIId());

			setAnfragepositionlieferdatenFromAnfragepositionlieferdatenDto(anfragepositionlieferdaten,
					anfragepositionlieferdatenDtoI);

			befuelleZusaetzlichesPreisfeld(anfragepositionlieferdatenDtoI.getIId(), theClientDto);

			// --Auskommentiert von CK lt. IMS 2191
			/*
			 * if (anfrageposition.getAnfragepositionartCNr().equals(AnfrageServiceFac .
			 * ANFRAGEPOSITIONART_IDENT)) {
			 * 
			 * // die Artikelbezeichnung des Lieferanten zurueckpflegen ArtikellieferantDto
			 * artikellieferantDto = getArtikelFac().
			 * artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
			 * anfrageposition.getArtikelIId(), anfrageDto.getLieferantIIdAnfrageadresse(),
			 * theClientDto);
			 * 
			 * // ad Waehrung: // - Anfragepositionen sind immer in Anfragewaehrung
			 * hinterlegt // - Artikellieferantendaten sind immer in Lieferantenwaehrung
			 * hinterlegt String cNrAnfragewaehrung = anfrageDto.getWaehrungCNr(); String
			 * cNrLieferantenwaehrung = getLieferantFac().lieferantFindByPrimaryKey(
			 * anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto).getWaehrungCNr();
			 * 
			 * BigDecimal bdWechselkurs = getLocaleFac().getWechselkurs2(cNrAnfragewaehrung,
			 * cNrLieferantenwaehrung, theClientDto);
			 * 
			 * if (artikellieferantDto != null) { artikellieferantDto.setCBezbeilieferant
			 * (anfragepositionlieferdatenDtoI. getCArtikelbezeichnungbeimlieferanten());
			 * artikellieferantDto.setTPreisgueltigab(getTimestamp());
			 * 
			 * // - der Einzelpreis beim Lieferanten bleibt bestehen // - der Gesamtpreis
			 * beim Lieferanten wird zurueckgeschrieben // - der Rabatt muss neu berechnet
			 * und zurueckgeschrieben werden artikellieferantDto
			 * .setNNettopreis(getBetragMalWechselkurs2(anfragepositionlieferdaten .
			 * getNNettogesamtpreisminusrabatt(), bdWechselkurs));
			 * 
			 * if (artikellieferantDto.getFRabatt() != null) { // der Einzelpreis muss immer
			 * existieren BigDecimal bdRabattsumme =
			 * (artikellieferantDto.getNNettopreis().subtract(
			 * artikellieferantDto.getNEinzelpreis()).negate());
			 * 
			 * BigDecimal bdRabattsatz = new BigDecimal(0);
			 * 
			 * if (artikellieferantDto.getNEinzelpreis().doubleValue() != 0) { bdRabattsatz
			 * = bdRabattsumme.divide( artikellieferantDto.getNEinzelpreis(), 4,
			 * BigDecimal.ROUND_HALF_UP). movePointRight(2); }
			 * 
			 * artikellieferantDto.setFRabatt(new Double(bdRabattsatz.doubleValue())); }
			 * 
			 * getArtikelFac().updateArtikellieferant(artikellieferantDto); } else {
			 * LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
			 * anfrageDto.getLieferantIIdAnfrageadresse(), theClientDto);
			 * 
			 * artikellieferantDto = new ArtikellieferantDto(); artikellieferantDto
			 * .setArtikelIId(anfrageposition.getArtikelIId());
			 * artikellieferantDto.setLieferantIId(lieferantDto.getIId());
			 * artikellieferantDto .setMandantCNr(getTheClient(theClientDto).getMandant());
			 * artikellieferantDto.setTPreisgueltigab(getTimestamp()); artikellieferantDto
			 * .setCBezbeilieferant(anfragepositionlieferdatenDtoI.
			 * getCArtikelbezeichnungbeimlieferanten());
			 * artikellieferantDto.setNNettopreis(getBetragMalWechselkurs2(
			 * anfragepositionlieferdaten.getNNettogesamtpreisminusrabatt(),
			 * bdWechselkurs)); artikellieferantDto.setFRabatt(new Double(0));
			 * artikellieferantDto .setNEinzelpreis(artikellieferantDto.getNNettopreis());
			 * 
			 * getArtikelFac().createArtikellieferant(artikellieferantDto); } } --CK IMS
			 * 2191
			 */

			// das Aktualisieren einer Anfragepositionlieferdaten kann den
			// Status
			// einer Anfrage von 'Offen' auf 'Erfasst' setzen
			if (anfrageDto.getStatusCNr().equals(AnfrageServiceFac.ANFRAGESTATUS_OFFEN)) {
				getAnfrageFac().setzeAnfragestatus(AnfrageServiceFac.ANFRAGESTATUS_ERFASST, anfrageDto.getIId(),
						theClientDto);
			}

			befuelleZusaetzlichesPreisfeld(anfragepositionlieferdatenDtoI.getIId(), theClientDto);

			getAnfrageFac().setzeNettogesamtwert(anfrageDto.getIId(),
					getAnfrageFac().berechneNettowertGesamt(anfrageDto.getIId(), theClientDto), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEIM_UPDATE,
		// t);
		// }
	}

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByPrimaryKey(
			Integer iIdAnfragepositionlieferdatenI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdAnfragepositionlieferdatenI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnfragepositionlieferdatenI == null"));
		}

		AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;

		Anfragepositionlieferdaten anfragepositionlieferdaten = em.find(Anfragepositionlieferdaten.class,
				iIdAnfragepositionlieferdatenI);
		if (anfragepositionlieferdaten == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfragepositionLieferdatenFindbyPrimaryKey. Es gibt keine LIeferdaten mit iid "
							+ iIdAnfragepositionlieferdatenI);
		}
		anfragepositionlieferdatenDto = assembleAnfragepositionlieferdatenDto(anfragepositionlieferdaten);

		// die zugehoerigen Datensaetze bestimmen
		Anfrageposition anfrageposition = em.find(Anfrageposition.class,
				anfragepositionlieferdatenDto.getAnfragepositionIId());
		if (anfrageposition == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei AnfragepositionLieferdatenFindbyPrimaryKey. Es gibt keine Anfrageposition mit iid "
							+ anfragepositionlieferdatenDto.getAnfragepositionIId()
							+ "\n anfragepositionlieferdatenDto.toString: " + anfragepositionlieferdatenDto.toString());
		}

		return anfragepositionlieferdatenDto;
	}

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByAnfragepositionIId(Integer iIdAnfragepositionI)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdAnfragepositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdAnfragepositionI == null"));
		}

		AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;

		try {
			Query query = em.createNamedQuery("AnfragepositionlieferdatenfindByAnfrageposition");
			query.setParameter(1, iIdAnfragepositionI);
			Anfragepositionlieferdaten anfragepositionlieferdaten = (Anfragepositionlieferdaten) query
					.getSingleResult();
			anfragepositionlieferdatenDto = assembleAnfragepositionlieferdatenDto(anfragepositionlieferdaten);
		} catch (NoResultException t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, t);
		} catch (NonUniqueResultException e1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT,
					"Fehler bei anfragepositionLieferdatenFindByAnfrageposition.\n"
							+ "Es gibt mehr als ein Lieferdatendto f\u00FCr anfragepositioniid " + iIdAnfragepositionI);
		}

		return anfragepositionlieferdatenDto;
	}

	public AnfragepositionlieferdatenDto anfragepositionlieferdatenFindByAnfragepositionIIdOhneExc(
			Integer iIdAnfragepositionI) {
		AnfragepositionlieferdatenDto anfragepositionlieferdatenDto = null;

		try {
			anfragepositionlieferdatenDto = anfragepositionlieferdatenFindByAnfragepositionIId(iIdAnfragepositionI);
		} catch (Throwable t) {
			myLogger.warn("iIdAnfragepositionI=" + iIdAnfragepositionI, t);
		}

		return anfragepositionlieferdatenDto;
	}

	/**
	 * Berechnet die Anzahl der mengenbehafteten Positionen zu einer bestimmten
	 * Anfrage.
	 * 
	 * @param iIdAnfrageI  PK der Anfrage
	 * @param theClientDto der aktuelle Benutzer
	 * @return int die Anzahl der Positonen
	 * @throws EJBExceptionLP Ausnahme
	 */
	public int getAnzahlMengenbehafteteAnfragepositionen(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iIdAnfrageI == null"));
		}

		int iAnzahl = 0;

		// try {
		Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
		query.setParameter(1, iIdAnfrageI);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }

		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Anfrageposition pos = ((Anfrageposition) iter.next());

			if (pos.getNMenge() != null) {
				iAnzahl++;
			}
		}
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// t);
		// }

		myLogger.exit("Anzahl: " + iAnzahl);

		return iAnzahl;
	}

	public AnfragepositionDto[] anfragepositionFindByAnfrage(Integer iIdAnfrageI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (iIdAnfrageI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL, new Exception("iIdAnfrageI == null"));
		}

		AnfragepositionDto[] aAnfragepositionDto = null;

		// try {
		Query query = em.createNamedQuery("AnfragepositionfindByAnfrage");
		query.setParameter(1, iIdAnfrageI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }

		aAnfragepositionDto = assembleAnfragepositionDtos(cl);
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// t);
		// }

		return aAnfragepositionDto;
	}

	public AnfragepositionlieferdatenDto[] anfragepositionlieferdatenFindByAnfragepositionIIdBErfasst(
			Integer iIdAnfragepositionI, Short bErfasstI) throws EJBExceptionLP {
		myLogger.entry();

		if (iIdAnfragepositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL, new Exception("iIdAnfragepositionI == null"));
		}

		if (bErfasstI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HOME_IS_NULL, new Exception("bErfasstI == null"));
		}

		AnfragepositionlieferdatenDto[] aAnfragepositionlieferdatenDto = null;

		// try {
		Query query = em.createNamedQuery("AnfragepositionlieferdatenfindByAnfragepositionIIdErfasst");
		query.setParameter(1, iIdAnfragepositionI);
		query.setParameter(2, bErfasstI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// null);
		// }

		aAnfragepositionlieferdatenDto = assembleAnfragepositionlieferdatenDtos(cl);
		// }
		// catch (FinderException t) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
		// t);
		// }

		return aAnfragepositionlieferdatenDto;
	}

	private void setAnfragepositionlieferdatenFromAnfragepositionlieferdatenDto(
			Anfragepositionlieferdaten anfragepositionlieferdaten,
			AnfragepositionlieferdatenDto anfragepositionlieferdatenDto) {
		anfragepositionlieferdaten.setAnfragepositionIId(anfragepositionlieferdatenDto.getAnfragepositionIId());
		anfragepositionlieferdaten.setIAnlieferzeit(anfragepositionlieferdatenDto.getIAnlieferzeit());
		anfragepositionlieferdaten.setNAnliefermenge(anfragepositionlieferdatenDto.getNAnliefermenge());
		anfragepositionlieferdaten.setNNettogesamtpreis(anfragepositionlieferdatenDto.getNNettogesamtpreis());
		anfragepositionlieferdaten
				.setNNettogesamtpreisminusrabatt(anfragepositionlieferdatenDto.getNNettogesamtpreisminusrabatt());
		anfragepositionlieferdaten.setBErfasst(anfragepositionlieferdatenDto.getBErfasst());

		anfragepositionlieferdaten.setCArtikelnrlieferant(anfragepositionlieferdatenDto.getCArtikelnrlieferant());
		anfragepositionlieferdaten.setCBezbeilieferant(anfragepositionlieferdatenDto.getCBezbeilieferant());
		anfragepositionlieferdaten.setNMindestbestellmenge(anfragepositionlieferdatenDto.getNMindestbestellmenge());
		anfragepositionlieferdaten.setNStandardmenge(anfragepositionlieferdatenDto.getNStandardmenge());
		anfragepositionlieferdaten.setNVerpackungseinheit(anfragepositionlieferdatenDto.getNVerpackungseinheit());
		anfragepositionlieferdaten.setZertifikatartIId(anfragepositionlieferdatenDto.getZertifikatartIId());
		anfragepositionlieferdaten.setTPreisgueltigab(anfragepositionlieferdatenDto.getTPreisgueltigab());

		em.merge(anfragepositionlieferdaten);
		em.flush();
	}

	private AnfragepositionlieferdatenDto assembleAnfragepositionlieferdatenDto(
			Anfragepositionlieferdaten anfragepositionlieferdaten) {
		return AnfragepositionlieferdatenDtoAssembler.createDto(anfragepositionlieferdaten);
	}

	private AnfragepositionlieferdatenDto[] assembleAnfragepositionlieferdatenDtos(
			Collection<?> anfragepositionlieferdatens) {
		List<AnfragepositionlieferdatenDto> list = new ArrayList<AnfragepositionlieferdatenDto>();
		if (anfragepositionlieferdatens != null) {
			Iterator<?> iterator = anfragepositionlieferdatens.iterator();
			while (iterator.hasNext()) {
				Anfragepositionlieferdaten anfragepositionlieferdaten = (Anfragepositionlieferdaten) iterator.next();
				list.add(assembleAnfragepositionlieferdatenDto(anfragepositionlieferdaten));
			}
		}
		AnfragepositionlieferdatenDto[] returnArray = new AnfragepositionlieferdatenDto[list.size()];
		return (AnfragepositionlieferdatenDto[]) list.toArray(returnArray);
	}

	private void checkAnfragepositionlieferdatenDto(AnfragepositionlieferdatenDto anfragepositionlieferdatenDtoI)
			throws EJBExceptionLP {
		if (anfragepositionlieferdatenDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("anfragepositionlieferdatenDtoI == null"));
		}
	}

	public BelegpositionDto getNextBelegPosDto(int iIdBelegPosI, TheClientDto theClientDto) throws Exception {
		return anfragepositionFindByPrimaryKey(iIdBelegPosI, theClientDto);
	}

	/**
	 * hole zu bSPosDtoI den OF-price als node.
	 * 
	 * @param docI         Document
	 * @param belegPosDtoI BestellpositionDto
	 * @param theClientDto String
	 * @return Node
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * @throws Exception
	 */
	public Node getPriceAsNode(Document docI, BelegpositionDto belegPosDtoI, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP, Exception {

		AnfragepositionDto anfragePosDto = (AnfragepositionDto) belegPosDtoI;
		Node nodePriceOF = docI.createElement(SystemFac.SCHEMA_OF_PRICE);

		nodePriceOF = super.getPriceAsNode(docI, belegPosDtoI, theClientDto.getIDUser());

		// Price: BasePrice
		addOFElement(nodePriceOF, docI, anfragePosDto.getNRichtpreis(), SystemFac.SCHEMA_OF_PRICE_BASEPRICE);

		return nodePriceOF;
	}

	/**
	 * fuege zu belegpositionDtoI die speziellen HV-belegpos-feature-felder.
	 * 
	 * @param belegPosDtoI  BestellpositionDto
	 * @param nodeFeaturesI Node
	 * @param docI          Document
	 * @return Document
	 * @throws DOMException
	 */
	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI, Node nodeFeaturesI, Document docI)
			throws DOMException {
		// nothing here

		return null;
	}

	private void pruefePflichtfelderBelegposition(AnfragepositionDto afPosDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		super.pruefePflichtfelderBelegpositionDto(afPosDto, theClientDto);

		if (afPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
				|| afPosDto.getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			if (afPosDto.getNRichtpreis() == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
						new Exception("anfragepositionDto.getNRichtpreis() == null"));
			}
		}
	}

	public Integer getPositionNummer(Integer anfragepositionIId, TheClientDto theClientDto) {
		Hashtable<Integer, Integer> nrn = new Hashtable<Integer, Integer>();
		Integer nr = new Integer(1);
		AnfragepositionDto anfragepositionDto = anfragepositionFindByPrimaryKey(anfragepositionIId, theClientDto);
		AnfragepositionDto[] alleAnfragepositionDtos = anfragepositionFindByAnfrage(anfragepositionDto.getBelegIId(),
				theClientDto);
		for (int i = 0; i < alleAnfragepositionDtos.length; i++) {
			if (alleAnfragepositionDtos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_IDENT)
					|| alleAnfragepositionDtos[i].getPositionsartCNr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
				nrn.put(alleAnfragepositionDtos[i].getIId(), nr);
				nr++;
			}
		}

		return nrn.get(anfragepositionIId);
	}

}
