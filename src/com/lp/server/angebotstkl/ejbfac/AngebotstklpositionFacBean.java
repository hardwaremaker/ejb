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
package com.lp.server.angebotstkl.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionDtoAssembler;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Beleg;
import com.lp.server.util.IOpenFactory;
import com.lp.service.BelegpositionDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AngebotstklpositionFacBean extends Beleg implements
		AngebotstklpositionFac, IOpenFactory {

	@PersistenceContext
	private EntityManager em;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			Integer agstklIId, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
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

	/**
	 * Zwei bestehende Positionen in Bezug auf ihr iSort umreihen.
	 * 
	 * @param idPosition1I
	 *            PK der ersten Preisliste
	 * @param idPosition2I
	 *            PK der zweiten Preisliste
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheAgstklpositionen(Integer idPosition1I,
			Integer idPosition2I) throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Agstklposition oPosition1 = em.find(Agstklposition.class, idPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"Fehler bei vertauscheAgstklPositionen. Es gibt keine Position mit iid "
							+ idPosition1I);
		}

		Agstklposition oPosition2 = em.find(Agstklposition.class, idPosition2I);

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

	public void kopiereAgstklPositionen(Integer agstklIId_Quelle,
			Integer agstklIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (agstklIId_Quelle == null || agstklIId_Ziel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"agstklIId_Quelle == null || agstklIId_Ziel == null"));
		}

		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, agstklIId_Quelle);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		//
		// }

		AgstklpositionDto[] dtos = assembleAgstklpositionDtos(cl);

		for (int i = 0; i < dtos.length; i++) {
			AgstklpositionDto dto = dtos[i];
			dto.setAgstklIId(agstklIId_Ziel);

			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall(dto.getArtikelIId(),
							theClientDto);
			if (artikelDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {
				dto.setAgstklpositionsartCNr(LocaleFac.POSITIONSART_HANDEINGABE);
			} else {
				dto.setAgstklpositionsartCNr(LocaleFac.POSITIONSART_IDENT);
			}

			dto.setISort(null);// Damit automatisch drangehaengt wird
			createAgstklposition(dto, theClientDto);
		}

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);

		// }
	}

	public void preiseGemaessKalkulationsart2Updaten(Integer agstklIId,
			TheClientDto theClientDto) {

		AgstklDto agstklDto = null;
		try {
			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklIId);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		AgstklpositionDto[] dtos = agstklpositionFindByAgstklIId(agstklIId,
				theClientDto);
		for (int i = 0; i < dtos.length; i++) {
			AgstklpositionDto dto = dtos[i];

			dto = getAngebotstklFac()
					.befuellePositionMitPreisenKalkulationsart2(theClientDto,
							agstklDto.getWaehrungCNr(), dto.getArtikelIId(),
							dto.getNMenge(), dto);
			updateAgstklposition(dto, theClientDto);

		}
	}

	/**
	 * Eine neue Angebotsstuecklistenposition anlegen.
	 * 
	 * @param agstklpositionDtoI
	 *            die neue Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Position
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		Integer agstklpositionIId = null;

		try {
			// Handartikel anlegen
			if (agstklpositionDtoI.getAgstklpositionsartCNr().equalsIgnoreCase(
					AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(agstklpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(agstklpositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(agstklpositionDtoI.getEinheitCNr());
				// oArtikelDto.setMwstsatzIId(angebotpositionDtoI.getMwstsatzIId());

				Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto,
						theClientDto);

				agstklpositionDtoI.setArtikelIId(iIdArtikel);

				myLogger.info("Handartikel wurde angelegt, iIdArtikel:"
						+ iIdArtikel);
			}

			
			if(agstklpositionDtoI.getBAufschlaggesamtFixiert()==null){
				agstklpositionDtoI.setBAufschlaggesamtFixiert(Helper.boolean2Short(false));
			}
			if(agstklpositionDtoI.getNGestehungspreis()==null){
				agstklpositionDtoI.setNGestehungspreis(new BigDecimal(0));
			}
			
			// generieren von primary key
			agstklpositionIId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_AGSTKLPOSITION);

			agstklpositionDtoI.setIId(agstklpositionIId);
			if (agstklpositionDtoI.getISort() == null) {
				Query query = em
						.createNamedQuery("AgstklpositionejbSelectMaxISort");
				query.setParameter(1, agstklpositionDtoI.getAgstklIId());
				Integer i = (Integer) query.getSingleResult();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				agstklpositionDtoI.setISort(i);
			}
			Agstklposition agstklposition = new Agstklposition(
					agstklpositionDtoI.getIId(),
					agstklpositionDtoI.getAgstklIId(),
					agstklpositionDtoI.getAgstklpositionsartCNr(),
					agstklpositionDtoI.getBArtikelbezeichnunguebersteuert(),
					agstklpositionDtoI.getBRabattsatzuebersteuert(),
					agstklpositionDtoI.getNGestehungspreis(),
					agstklpositionDtoI.getBDrucken());
			em.persist(agstklposition);
			em.flush();

			setAgstklpositionFromAgstklpositionDto(agstklposition,
					agstklpositionDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(t));
		}

		myLogger.exit("Angebotstklposition wurde angelegt.");

		return agstklpositionIId;
	}

	/**
	 * Eine bestehende Angebotsstuecklistenposition loeschen.
	 * 
	 * @param agstklpositionDtoI
	 *            die zu loeschende Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		try {
			Agstklposition toRemove = em.find(Agstklposition.class,
					agstklpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAgstklPosition. Es gibt keine Position mit iid "
								+ agstklpositionDtoI.getIId()
								+ "\nagstklpositionDtoI.toString() "
								+ agstklpositionDtoI.toString());
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}

			// die Sortierung muss angepasst werden
			sortierungAnpassenBeimLoeschenEinerPosition(
					agstklpositionDtoI.getAgstklIId(), agstklpositionDtoI
							.getISort().intValue(), theClientDto);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
					new Exception(t));
		}
	}

	/**
	 * Eine bestehende Angebotsstuecklistenposition aktualisieren.
	 * 
	 * @param agstklpositionDtoI
	 *            die zu aktualisierende Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateAgstklposition(AgstklpositionDto agstklpositionDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		try {
			Agstklposition agstklposition = em.find(Agstklposition.class,
					agstklpositionDtoI.getIId());
			if (agstklposition == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAgstklposition. Es gibt keine iid "
								+ agstklpositionDtoI.getIId()
								+ "\nagstklpositionDtoI.toString: "
								+ agstklpositionDtoI.toString());
			}

			setAgstklpositionFromAgstklpositionDto(agstklposition,
					agstklpositionDtoI);

			// spezielle Behandlung fuer eine Handeingabeposition
			if (agstklpositionDtoI.getAgstklpositionsartCNr().equals(
					AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								agstklpositionDtoI.getArtikelIId(),
								theClientDto);

				ArtikelsprDto oArtikelsprDto = artikelDto.getArtikelsprDto();
				oArtikelsprDto.setCBez(agstklpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(agstklpositionDtoI.getCZusatzbez());

				artikelDto.setArtikelsprDto(oArtikelsprDto);
				artikelDto.setEinheitCNr(agstklpositionDtoI.getEinheitCNr());
				// artikelDto.setMwstsatzIId(agstklpositionDtoI.getMwstsatzIId());

				getArtikelFac().updateArtikel(artikelDto, theClientDto);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}
	}

	public AgstklpositionDto agstklpositionFindByPrimaryKey(
			Integer iIdAgstklpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AgstklpositionDto agstklpositionDto = agstklpositionFindByPrimaryKeyOhneExc(iIdAgstklpositionI);

		if (agstklpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei agstklpositionfindbyPrimaryKey. Es gibt keine Position mit iid "
							+ iIdAgstklpositionI);
		}

		return agstklpositionDto;
	}

	public AgstklpositionDto agstklpositionFindByPrimaryKeyOhneExc(
			Integer iIdAgstklpositionI) {

		Agstklposition agstklposition = em.find(Agstklposition.class,
				iIdAgstklpositionI);
		if (agstklposition == null) {
			return null;
		}
		return assembleAgstklpositionDto(agstklposition);
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIId(
			Integer iIdAgstklI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }

		aAgstklpositionDto = assembleAgstklpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }

		return aAgstklpositionDto;
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdOhneExc(
			Integer iIdAgstklI, TheClientDto theClientDto) {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, "");
		// }

		aAgstklpositionDto = assembleAgstklpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, ex);
		// }

		return aAgstklpositionDto;
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdBDruckenOhneExc(
			Integer iIdAgstklI, Short bDruckenI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em
				.createNamedQuery("AgstklpositionfindByAgstklIIdBDrucken");
		query.setParameter(1, iIdAgstklI);
		query.setParameter(2, bDruckenI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, "");
		// }

		aAgstklpositionDto = assembleAgstklpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, ex);
		// }

		return aAgstklpositionDto;
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdMengeNotNullOhneExc(
			Integer iIdAgstklI, TheClientDto theClientDto) {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em
				.createNamedQuery("AgstklpositionfindByAgstklIIdMengeNotNull");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, "");
		// }

		aAgstklpositionDto = assembleAgstklpositionDtos(cl);
		// }
		// catch (FinderException ex) {
		// myLogger.warn("iIdAgstklI=" + iIdAgstklI, ex);
		// }

		return aAgstklpositionDto;
	}

	/**
	 * Das maximale iSort bei den Angebotsstuecklistenpositionen fuer eine
	 * bestimmte Angebotsstueckliste bestimmen.
	 * 
	 * @param iIdAgstklI
	 *            PK der Agstkl
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer getMaxISort(Integer iIdAgstklI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		checkAgstklpositionIId(iIdAgstklI);
		Integer iiMaxISortO = null;
		try {
			Query query = em
					.createNamedQuery("AgstklpositionejbSelectMaxISort");
			query.setParameter(1, iIdAgstklI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(e));
		}
		// myLogger.exit("Max isort: " + maxISort);
		return iiMaxISortO;

	}

	private void setAgstklpositionFromAgstklpositionDto(
			Agstklposition agstklposition, AgstklpositionDto agstklpositionDto) {
		agstklposition.setAgstklIId(agstklpositionDto.getAgstklIId());
		agstklposition.setISort(agstklpositionDto.getISort());
		agstklposition.setAgstklpositionsartCNr(agstklpositionDto
				.getAgstklpositionsartCNr());
		agstklposition.setArtikelIId(agstklpositionDto.getArtikelIId());
		agstklposition.setCBez(agstklpositionDto.getCBez());
		agstklposition.setCZbez(agstklpositionDto.getCZbez());
		agstklposition.setBArtikelbezeichnunguebersteuert(agstklpositionDto
				.getBArtikelbezeichnunguebersteuert());
		agstklposition.setNMenge(agstklpositionDto.getNMenge());
		agstklposition.setEinheitCNr(agstklpositionDto.getEinheitCNr());
		agstklposition.setFRabattsatz(agstklpositionDto.getFRabattsatz());
		agstklposition.setBRabattsatzuebersteuert(agstklpositionDto
				.getBRabattsatzuebersteuert());
		agstklposition.setFZusatzrabattsatz(agstklpositionDto
				.getFZusatzrabattsatz());
		agstklposition.setNNettoeinzelpreis(agstklpositionDto
				.getNNettoeinzelpreis());
		agstklposition.setNNettogesamtpreis(agstklpositionDto
				.getNNettogesamtpreis());
		agstklposition.setBDrucken(agstklpositionDto.getBDrucken());
		agstklposition.setNGestehungspreis(agstklpositionDto
				.getNGestehungspreis());
		agstklposition.setNMaterialzuschlag(agstklpositionDto
				.getNMaterialzuschlag());

		agstklposition.setBAufschlaggesamtFixiert(agstklpositionDto
				.getBAufschlaggesamtFixiert());
		agstklposition.setFAufschlag(agstklpositionDto.getFAufschlag());
		agstklposition.setNAufschlag(agstklpositionDto.getNAufschlag());
		agstklposition.setNNettogesamtmitaufschlag(agstklpositionDto
				.getNNettogesamtmitaufschlag());
		em.merge(agstklposition);
		em.flush();
	}

	private AgstklpositionDto assembleAgstklpositionDto(
			Agstklposition agstklposition) {
		return AgstklpositionDtoAssembler.createDto(agstklposition);
	}

	private AgstklpositionDto[] assembleAgstklpositionDtos(
			Collection<?> agstklpositions) {
		List<AgstklpositionDto> list = new ArrayList<AgstklpositionDto>();
		if (agstklpositions != null) {
			Iterator<?> iterator = agstklpositions.iterator();
			while (iterator.hasNext()) {
				Agstklposition agstklposition = (Agstklposition) iterator
						.next();
				list.add(assembleAgstklpositionDto(agstklposition));
			}
		}
		AgstklpositionDto[] returnArray = new AgstklpositionDto[list.size()];
		return (AgstklpositionDto[]) list.toArray(returnArray);
	}

	private void checkAgstklpositionDto(AgstklpositionDto agstklpositionDtoI)
			throws EJBExceptionLP {
		if (agstklpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("agstklpositionI == null"));
		}

		myLogger.info("AgstklpositionDto: " + agstklpositionDtoI.toString());
	}

	private void checkAgstklpositionIId(Integer iIdAgstklpositionI)
			throws EJBExceptionLP {
		if (iIdAgstklpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("iIdAgstklpositionI == null"));
		}

		myLogger.info("AgstklpositionIId: " + iIdAgstklpositionI.toString());
	}

	/**
	 * Wenn fuer eine Angebotsstueckliste eine Position geloescht wurden, dann
	 * muss die Sortierung der Positionen angepasst werden, damit keine Luecken
	 * entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param iIdAngebotstklI
	 *            PK des Angebots
	 * @param iSortierungGeloeschtePositionI
	 *            die Position der geloschten Position
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void sortierungAnpassenBeimLoeschenEinerPosition(
			Integer iIdAngebotstklI, int iSortierungGeloeschtePositionI,
			TheClientDto theClientDto) throws Throwable {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, iIdAngebotstklI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Agstklposition agstklposition = (Agstklposition) it.next();

			if (agstklposition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				agstklposition.setISort(new Integer(
						iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}

		myLogger.exit("Die Sortierung wurde angepasst.");
	}

	/**
	 * Auf Belegdrucken muessen die Positionen einer Agstkl als Textblock
	 * angezigt werden. In dieser Methode wird dieser Block zusammengebaut.
	 * 
	 * @param aAgstklpositionDto
	 *            die Positionen der Stueckliste
	 * @param nMengeStuecklisteI
	 *            wieviele Einheiten der Stueckliste werden benoetigt
	 * @param locDruckI
	 *            Locale
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return String Textblock zum Andrucken
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public String getAgstklpositionenAsTextblock(
			AgstklpositionDto[] aAgstklpositionDto,
			BigDecimal nMengeStuecklisteI, Locale locDruckI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (aAgstklpositionDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("aAgstklpositionDto == null"));
		}

		String cTextblock = null;

		try {
			StringBuffer buff = new StringBuffer("");

			// jede Position eine Zeile im Druck unter dem Artikel einfuegen
			for (int j = 0; j < aAgstklpositionDto.length; j++) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								aAgstklpositionDto[j].getArtikelIId(),
								theClientDto);

				// UW, 07.02.06: Die Stuecklistenpositionen werden immer fuer 1
				// Stueckliste angedruckt
				// BigDecimal nMengeStuecklistenposition =
				// nMengeStuecklisteI.multiply(aAgstklpositionDto[j].getNMenge());

				nMengeStuecklisteI = Helper.rundeKaufmaennisch(
						nMengeStuecklisteI,
						getMandantFac().getNachkommastellenMenge(
								theClientDto.getMandant()).intValue());

				// cNr des Artikels andrucken, wenn es kein Handartikel ist
				String artikelCNr = "";
				if (!artikelDto.getArtikelartCNr().equals(
						ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					artikelCNr = artikelDto.getCNr() + " ";
				}

				buff.append(nMengeStuecklisteI)
						.append(" ")
						.append(aAgstklpositionDto[j].getEinheitCNr().trim())
						.append(" ")
						.append(artikelCNr)
						.append(getArtikelFac()
								.formatArtikelbezeichnungEinzeiligOhneExc(
										artikelDto.getIId(), locDruckI));

				if (j < aAgstklpositionDto.length - 1) {
					buff.append("\n");
				}

				cTextblock = buff.toString();
			}
		} catch (RemoteException re) {
			throwEJBExceptionLPRespectOld(re);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					new Exception(t));
		}

		return cTextblock;
	}

	public AgstklpositionDto getNextBelegPosDto(int iIdBelegPosI,
			TheClientDto theClientDto) throws Exception {
		return agstklpositionFindByPrimaryKey(iIdBelegPosI, theClientDto);
	}

	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI,
			Node nodeFeaturesI, Document docI) throws Exception, DOMException {

		AgstklpositionDto bSPos = (AgstklpositionDto) belegPosDtoI;

		// Feature: Drucken
		addHVFeature(nodeFeaturesI, docI,
				AngebotstklpositionFac.SCHEMA_HV_FEATURE_DRUCKEN,
				bSPos.getBDrucken());
		return nodeFeaturesI;
	}

}
