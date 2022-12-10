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

import com.lp.server.angebotstkl.ejb.Agstklmaterial;
import com.lp.server.angebotstkl.ejb.Agstklposition;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AgstklImportSpezifikation;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklmaterialDtoAssembler;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AgstklpositionDtoAssembler;
import com.lp.server.angebotstkl.service.AngebotstklServiceFac;
import com.lp.server.angebotstkl.service.AngebotstklpositionFac;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.IImportPositionen;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Beleg;
import com.lp.server.util.IOpenFactory;
import com.lp.server.util.Validator;
import com.lp.service.BelegpositionDto;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class AngebotstklpositionFacBean extends Beleg
		implements AngebotstklpositionFac, AngebotstklpositionLocalFac, IOpenFactory, IImportPositionen, IImportHead {
	private static final long serialVersionUID = -8293133865190114430L;

	@PersistenceContext
	private EntityManager em;

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(Integer agstklIId, int iSortierungNeuePositionI)
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
	 * @param idPosition1I PK der ersten Preisliste
	 * @param idPosition2I PK der zweiten Preisliste
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void vertauscheAgstklpositionen(Integer idPosition1I, Integer idPosition2I) throws EJBExceptionLP {
		myLogger.entry();

		// try {
		Agstklposition oPosition1 = em.find(Agstklposition.class, idPosition1I);
		if (oPosition1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,
					"Fehler bei vertauscheAgstklPositionen. Es gibt keine Position mit iid " + idPosition1I);
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

	public void kopiereAgstklPositionen(Integer agstklIId_Quelle, Integer agstklIId_Ziel, TheClientDto theClientDto)
			throws EJBExceptionLP {
		kopiereAgstklPositionenImpl(agstklIId_Quelle, agstklIId_Ziel, theClientDto);
	}

	public void kopiereAgstklPositionenMitPreisUpdate(Integer agstklIId_Quelle, Integer agstklIId_Ziel,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		kopiereAgstklPositionenImpl(agstklIId_Quelle, agstklIId_Ziel, theClientDto);
		preiseGemaessKalkulationsartUpdatenImpl(agstklIId_Ziel, theClientDto);
	}

	/**
	 * @param agstklIId_Quelle
	 * @param agstklIId_Ziel
	 * @param theClientDto
	 */
	private void kopiereAgstklPositionenImpl(Integer agstklIId_Quelle, Integer agstklIId_Ziel,
			TheClientDto theClientDto) {
		Validator.pkFieldNotNull(agstklIId_Quelle, "agstklIId_Quelle");
		Validator.pkFieldNotNull(agstklIId_Ziel, "agstklIId_Ziel");

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

			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(dto.getArtikelIId(), theClientDto);
			if (artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
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

	private void preiseGemaessKalkulationsartUpdatenImpl(Integer agstklIId, final TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		PriceCalculatorFactory priceCalculatorFactory = new PriceCalculatorFactory();
		IPriceCalculator priceCalculator = priceCalculatorFactory
				.getPriceCalculator(getParameterFac().getKalkulationsart(theClientDto.getMandant()));

		priceCalculator.fillWithPrices(agstklIId, theClientDto, new IFilledAgstklpositionDto() {
			@Override
			public void process(AgstklpositionDto agstklpositionDto) {
				updateAgstklposition(agstklpositionDto, theClientDto);
			}
		});
	}

	public void preiseGemaessKalkulationsartUpdaten(Integer agstklIId, final TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.pkFieldNotNull(agstklIId, "agstklIId");

		preiseGemaessKalkulationsartUpdatenImpl(agstklIId, theClientDto);

//		AgstklDto agstklDto = null;
//		int iKalkulationsart = -1;
//		try {
//			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklIId);
//
//			ParametermandantDto parameterMand = getParameterFac()
//					.getMandantparameter(theClientDto.getMandant(),
//							ParameterFac.KATEGORIE_ANGEBOTSSTUECKLISTE,
//							ParameterFac.PARAMETER_KALKULATIONSART);
//			iKalkulationsart = (Integer) parameterMand.getCWertAsObject();
//
//		} catch (RemoteException e) {
//			throwEJBExceptionLPRespectOld(e);
//		}
//
//		KundeDto kdDto = getKundeFac().kundeFindByPrimaryKey(
//				agstklDto.getKundeIId(), theClientDto);
//
//		AgstklpositionDto[] dtos = agstklpositionFindByAgstklIId(agstklIId,
//				theClientDto);
//		for (int i = 0; i < dtos.length; i++) {
//			AgstklpositionDto dto = dtos[i];
//
//			// EK-Preisbezug
//			if (iKalkulationsart == 2 || iKalkulationsart == 3) {
//
//				dto = befuellePositionMitPreisenKalkulationsart2(
//								theClientDto, agstklDto.getWaehrungCNr(),
//								dto.getArtikelIId(), dto.getNMenge(), dto);
//			} else {
//
//				if (dto.getArtikelIId() != null && dto.getNMenge() != null) {
//					VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac()
//							.verkaufspreisfindung(
//									dto.getArtikelIId(),
//									agstklDto.getKundeIId(),
//									dto.getNMenge(),
//									new java.sql.Date(agstklDto
//											.getTBelegdatum().getTime()),
//									kdDto.getVkpfArtikelpreislisteIIdStdpreisliste(),
//									kdDto.getMwstsatzbezIId(),
//									agstklDto.getWaehrungCNr(), theClientDto);
//
//					VerkaufspreisDto verkaufspreisDtoInZielwaehrung = Helper
//							.getVkpreisBerechnet(vkpreisfindungDto);
//
//					if (verkaufspreisDtoInZielwaehrung != null) {
//						dto.setNAufschlag(BigDecimal.ZERO);
//						dto.setFAufschlag(0D);
//						dto.setFZusatzrabattsatz(0D);
//
//						dto.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.einzelpreis);
//						dto.setFRabattsatz(verkaufspreisDtoInZielwaehrung.rabattsatz);
//						dto.setNNettogesamtpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
//						dto.setNNettogesamtmitaufschlag(verkaufspreisDtoInZielwaehrung.nettopreis);
//
//					}
//
//				}
//			}
//			updateAgstklposition(dto, theClientDto);
//
//		}
	}

	@Override
	/**
	 * Mehrere Angebotspositionen anlegen.
	 * 
	 * @param agstklpositionDtos, die neuen Positionen
	 * @param theClientDto,       der aktuelle Benutzer
	 * @return iId, der zuletzt angelegten neuen Position
	 */
	public Integer createAngebotpositions(AgstklpositionDto[] agstklpositionDtos, TheClientDto theClientDto) {
		Integer iId = null;
		for (int i = 0; i < agstklpositionDtos.length; i++) {
			iId = createAgstklposition(agstklpositionDtos[i], theClientDto);
		}
		return iId;
	}

	/**
	 * Eine neue Angebotsstuecklistenposition anlegen.
	 * 
	 * @param agstklpositionDtoI die neue Position
	 * @param theClientDto       der aktuelle Benutzer
	 * @return Integer PK der neuen Position
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer createAgstklposition(AgstklpositionDto agstklpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		Integer agstklpositionIId = null;

		try {
			// Handartikel anlegen
			if (agstklpositionDtoI.getAgstklpositionsartCNr()
					.equalsIgnoreCase(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				ArtikelDto oArtikelDto = new ArtikelDto();
				oArtikelDto.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);

				ArtikelsprDto oArtikelsprDto = new ArtikelsprDto();
				oArtikelsprDto.setCBez(agstklpositionDtoI.getCBez());
				oArtikelsprDto.setCZbez(agstklpositionDtoI.getCZusatzbez());

				oArtikelDto.setArtikelsprDto(oArtikelsprDto);
				oArtikelDto.setEinheitCNr(agstklpositionDtoI.getEinheitCNr());
				// oArtikelDto.setMwstsatzIId(angebotpositionDtoI.getMwstsatzIId());

				if (agstklpositionDtoI.getArtikelIId() == null) {
					Integer iIdArtikel = getArtikelFac().createArtikel(oArtikelDto, theClientDto);

					agstklpositionDtoI.setArtikelIId(iIdArtikel);
					myLogger.info("Handartikel wurde angelegt, iIdArtikel:" + iIdArtikel);
				}

			}

			if (agstklpositionDtoI.getBAufschlaggesamtFixiert() == null) {
				agstklpositionDtoI.setBAufschlaggesamtFixiert(Helper.boolean2Short(false));
			}
			if (agstklpositionDtoI.getBMitPreisen() == null) {
				agstklpositionDtoI.setBMitPreisen(Helper.boolean2Short(false));
			}
			if (agstklpositionDtoI.getNGestehungspreis() == null) {
				agstklpositionDtoI.setNGestehungspreis(new BigDecimal(0));
			}
			if (agstklpositionDtoI.getBRuestmenge() == null) {
				agstklpositionDtoI.setBRuestmenge(Helper.boolean2Short(false));
			}
			if (agstklpositionDtoI.getBInitial() == null) {
				agstklpositionDtoI.setBInitial(Helper.boolean2Short(false));
			}

			// generieren von primary key
			agstklpositionIId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AGSTKLPOSITION);

			agstklpositionDtoI.setIId(agstklpositionIId);
			if (agstklpositionDtoI.getISort() == null) {
				Query query = em.createNamedQuery("AgstklpositionejbSelectMaxISort");
				query.setParameter(1, agstklpositionDtoI.getAgstklIId());
				Integer i = (Integer) query.getSingleResult();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				agstklpositionDtoI.setISort(i);
			}
			Agstklposition agstklposition = new Agstklposition(agstklpositionDtoI.getIId(),
					agstklpositionDtoI.getAgstklIId(), agstklpositionDtoI.getAgstklpositionsartCNr(),
					agstklpositionDtoI.getBArtikelbezeichnunguebersteuert(),
					agstklpositionDtoI.getBRabattsatzuebersteuert(), agstklpositionDtoI.getNGestehungspreis(),
					agstklpositionDtoI.getBDrucken(), agstklpositionDtoI.getBMitPreisen(),agstklpositionDtoI.getBRuestmenge(),agstklpositionDtoI.getBInitial());
			em.persist(agstklposition);
			em.flush();

			setAgstklpositionFromAgstklpositionDto(agstklposition, agstklpositionDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(t));
		}

		myLogger.exit("Angebotstklposition wurde angelegt.");

		return agstklpositionIId;
	}

	public Integer createAgstklmaterial(AgstklmaterialDto dto, TheClientDto theClientDto) {
		Integer agstklpositionIId = null;

		try {

			// generieren von primary key
			agstklpositionIId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_AGSTKLMATERIAL);

			dto.setIId(agstklpositionIId);
			if (dto.getISort() == null) {
				Query query = em.createNamedQuery("AgstklmaterialejbSelectMaxISort");
				query.setParameter(1, dto.getAgstklIId());
				Integer i = (Integer) query.getSingleResult();
				if (i == null) {
					i = new Integer(0);
				}
				i = new Integer(i.intValue() + 1);
				dto.setISort(i);
			}
			Agstklmaterial agstklmaterial = new Agstklmaterial(dto.getIId(), dto.getAgstklIId(), dto.getMaterialIId(),
					dto.getISort());
			em.persist(agstklmaterial);
			em.flush();

			setAgstklmaterialFromAgstklmaterialDto(agstklmaterial, dto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(t));
		}
		return agstklpositionIId;
	}

	/**
	 * Eine bestehende Angebotsstuecklistenposition loeschen.
	 * 
	 * @param agstklpositionDtoI die zu loeschende Position
	 * @param theClientDto       der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void removeAgstklposition(AgstklpositionDto agstklpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		try {
			Agstklposition toRemove = em.find(Agstklposition.class, agstklpositionDtoI.getIId());
			if (toRemove == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei removeAgstklPosition. Es gibt keine Position mit iid " + agstklpositionDtoI.getIId()
								+ "\nagstklpositionDtoI.toString() " + agstklpositionDtoI.toString());
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
			}

			// die Sortierung muss angepasst werden
			sortierungAnpassenBeimLoeschenEinerPosition(agstklpositionDtoI.getAgstklIId(),
					agstklpositionDtoI.getISort().intValue(), theClientDto);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, new Exception(t));
		}
	}

	public void removeAgstklmaterial(AgstklmaterialDto dto, TheClientDto theClientDto) {

		Agstklmaterial toRemove = em.find(Agstklmaterial.class, dto.getIId());

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateAgstpositionenOptionen(Integer angebotstklIId, boolean bAufBelegMitdrucken, boolean bMitPreisen,
			ArrayList<Integer> selectedIds, TheClientDto theClientDto) {

		if (selectedIds == null) {
			AgstklpositionDto[] posDto = agstklpositionFindByAgstklIId(angebotstklIId, theClientDto);

			for (int i = 0; i < posDto.length; i++) {
				Agstklposition agstklposition = em.find(Agstklposition.class, posDto[i].getIId());

				agstklposition.setBDrucken(Helper.boolean2Short(bAufBelegMitdrucken));
				agstklposition.setBMitPreisen(Helper.boolean2Short(bMitPreisen));

				em.merge(agstklposition);
				em.flush();
			}

		} else {

			for (int i = 0; i < selectedIds.size(); i++) {
				Agstklposition agstklposition = em.find(Agstklposition.class, selectedIds.get(i));

				agstklposition.setBDrucken(Helper.boolean2Short(bAufBelegMitdrucken));
				agstklposition.setBMitPreisen(Helper.boolean2Short(bMitPreisen));

				em.merge(agstklposition);
				em.flush();
			}

		}

	}

	/**
	 * Eine bestehende Angebotsstuecklistenposition aktualisieren.
	 * 
	 * @param agstklpositionDtoI die zu aktualisierende Position
	 * @param theClientDto       der aktuelle Benutzer
	 * @throws EJBExceptionLP Ausnahme
	 */
	public void updateAgstklposition(AgstklpositionDto agstklpositionDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkAgstklpositionDto(agstklpositionDtoI);

		try {
			Agstklposition agstklposition = em.find(Agstklposition.class, agstklpositionDtoI.getIId());
			if (agstklposition == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
						"Fehler bei updateAgstklposition. Es gibt keine iid " + agstklpositionDtoI.getIId()
								+ "\nagstklpositionDtoI.toString: " + agstklpositionDtoI.toString());
			}

			setAgstklpositionFromAgstklpositionDto(agstklposition, agstklpositionDtoI);

			// spezielle Behandlung fuer eine Handeingabeposition
			if (agstklpositionDtoI.getAgstklpositionsartCNr()
					.equals(AngebotstklServiceFac.AGSTKLPOSITIONART_HANDEINGABE)) {
				// in diesem Fall muss auch der angelegte Handartikel
				// aktualisiert werden
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(agstklpositionDtoI.getArtikelIId(),
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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		}
	}

	public void updateAgstklmaterial(AgstklmaterialDto dto, TheClientDto theClientDto) {
		Agstklmaterial bean = em.find(Agstklmaterial.class, dto.getIId());
		setAgstklmaterialFromAgstklmaterialDto(bean, dto);

	}

	public AgstklpositionDto agstklpositionFindByPrimaryKey(Integer iIdAgstklpositionI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		AgstklpositionDto agstklpositionDto = agstklpositionFindByPrimaryKeyOhneExc(iIdAgstklpositionI);

		if (agstklpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Fehler bei agstklpositionfindbyPrimaryKey. Es gibt keine Position mit iid " + iIdAgstklpositionI);
		}

		return agstklpositionDto;
	}

	public AgstklmaterialDto agstklmaterialFindByPrimaryKey(Integer agstklmaterialIId, TheClientDto theClientDto) {
		Agstklmaterial agstklmaterial = em.find(Agstklmaterial.class, agstklmaterialIId);
		return AgstklmaterialDtoAssembler.createDto(agstklmaterial);
	}

	public AgstklpositionDto agstklpositionFindByPrimaryKeyOhneExc(Integer iIdAgstklpositionI) {

		Agstklposition agstklposition = em.find(Agstklposition.class, iIdAgstklpositionI);
		if (agstklposition == null) {
			return null;
		}
		return assembleAgstklpositionDto(agstklposition);
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIId(Integer iIdAgstklI, TheClientDto theClientDto)
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

	public ArrayList<AgstklmaterialDto> agstklmaterialFindByAgstklIId(Integer iIdAgstklI, TheClientDto theClientDto) {
		ArrayList<AgstklmaterialDto> list = new ArrayList<AgstklmaterialDto>();

		Query query = em.createNamedQuery("AgstklmaterialfindByAgstklIId");
		query.setParameter(1, iIdAgstklI);
		Collection<?> cl = query.getResultList();

		Iterator it = cl.iterator();
		while (it.hasNext()) {
			list.add(AgstklmaterialDtoAssembler.createDto((Agstklmaterial) it.next()));
		}

		return list;
	}

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdOhneExc(Integer iIdAgstklI, TheClientDto theClientDto) {
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

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdBDruckenOhneExc(Integer iIdAgstklI, Short bDruckenI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIIdBDrucken");
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

	public AgstklpositionDto[] agstklpositionFindByAgstklIIdMengeNotNullOhneExc(Integer iIdAgstklI,
			TheClientDto theClientDto) {
		AgstklpositionDto[] aAgstklpositionDto = null;

		// try {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIIdMengeNotNull");
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
	 * Das maximale iSort bei den Angebotsstuecklistenpositionen fuer eine bestimmte
	 * Angebotsstueckliste bestimmen.
	 * 
	 * @param iIdAgstklI   PK der Agstkl
	 * @param theClientDto der aktuelle Benutzer
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP Ausnahme
	 */
	public Integer getMaxISort(Integer iIdAgstklI, TheClientDto theClientDto) throws EJBExceptionLP {

		checkAgstklpositionIId(iIdAgstklI);
		Integer iiMaxISortO = null;
		try {
			Query query = em.createNamedQuery("AgstklpositionejbSelectMaxISort");
			query.setParameter(1, iIdAgstklI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT, new Exception(e));
		}
		// myLogger.exit("Max isort: " + maxISort);
		return iiMaxISortO;

	}

	private void setAgstklpositionFromAgstklpositionDto(Agstklposition agstklposition,
			AgstklpositionDto agstklpositionDto) {
		agstklposition.setAgstklIId(agstklpositionDto.getAgstklIId());
		agstklposition.setISort(agstklpositionDto.getISort());
		agstklposition.setAgstklpositionsartCNr(agstklpositionDto.getAgstklpositionsartCNr());
		agstklposition.setArtikelIId(agstklpositionDto.getArtikelIId());
		agstklposition.setCBez(agstklpositionDto.getCBez());
		agstklposition.setCZbez(agstklpositionDto.getCZbez());
		agstklposition.setBArtikelbezeichnunguebersteuert(agstklpositionDto.getBArtikelbezeichnunguebersteuert());
		agstklposition.setNMenge(agstklpositionDto.getNMenge());
		agstklposition.setEinheitCNr(agstklpositionDto.getEinheitCNr());
		agstklposition.setFRabattsatz(agstklpositionDto.getFRabattsatz());
		agstklposition.setBRabattsatzuebersteuert(agstklpositionDto.getBRabattsatzuebersteuert());
		agstklposition.setFZusatzrabattsatz(agstklpositionDto.getFZusatzrabattsatz());
		agstklposition.setNNettoeinzelpreis(agstklpositionDto.getNNettoeinzelpreis());
		agstklposition.setNNettogesamtpreis(agstklpositionDto.getNNettogesamtpreis());
		agstklposition.setBDrucken(agstklpositionDto.getBDrucken());
		agstklposition.setNGestehungspreis(agstklpositionDto.getNGestehungspreis());
		agstklposition.setNMaterialzuschlag(agstklpositionDto.getNMaterialzuschlag());

		agstklposition.setBAufschlaggesamtFixiert(agstklpositionDto.getBAufschlaggesamtFixiert());
		agstklposition.setFAufschlag(agstklpositionDto.getFAufschlag());
		agstklposition.setNAufschlag(agstklpositionDto.getNAufschlag());
		agstklposition.setNNettogesamtmitaufschlag(agstklpositionDto.getNNettogesamtmitaufschlag());
		agstklposition.setCPosition(agstklpositionDto.getCPosition());
		agstklposition.setBMitPreisen(agstklpositionDto.getBMitPreisen());
		agstklposition.setBRuestmenge(agstklpositionDto.getBRuestmenge());
		agstklposition.setBInitial(agstklpositionDto.getBInitial());
		
		em.merge(agstklposition);
		em.flush();
	}

	private void setAgstklmaterialFromAgstklmaterialDto(Agstklmaterial bean, AgstklmaterialDto dto) {
		bean.setAgstklIId(dto.getAgstklIId());
		bean.setISort(dto.getISort());
		bean.setMaterialIId(dto.getMaterialIId());
		bean.setCBez(dto.getCBez());
		bean.setCMaterialtyp(dto.getCMaterialtyp());
		bean.setNDimension1(dto.getNDimension1());
		bean.setNDimension2(dto.getNDimension2());
		bean.setNDimension3(dto.getNDimension3());
		bean.setNGewichtpreis(dto.getNGewichtpreis());
		bean.setNGewicht(dto.getNGewicht());

		em.merge(bean);
		em.flush();
	}

	private AgstklpositionDto assembleAgstklpositionDto(Agstklposition agstklposition) {
		return AgstklpositionDtoAssembler.createDto(agstklposition);
	}

	private AgstklpositionDto[] assembleAgstklpositionDtos(Collection<?> agstklpositions) {
		List<AgstklpositionDto> list = new ArrayList<AgstklpositionDto>();
		if (agstklpositions != null) {
			Iterator<?> iterator = agstklpositions.iterator();
			while (iterator.hasNext()) {
				Agstklposition agstklposition = (Agstklposition) iterator.next();
				list.add(assembleAgstklpositionDto(agstklposition));
			}
		}
		AgstklpositionDto[] returnArray = new AgstklpositionDto[list.size()];
		return (AgstklpositionDto[]) list.toArray(returnArray);
	}

	private void checkAgstklpositionDto(AgstklpositionDto agstklpositionDtoI) throws EJBExceptionLP {
		if (agstklpositionDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("agstklpositionI == null"));
		}

		myLogger.info("AgstklpositionDto: " + agstklpositionDtoI.toString());
	}

	private void checkAgstklpositionIId(Integer iIdAgstklpositionI) throws EJBExceptionLP {
		if (iIdAgstklpositionI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("iIdAgstklpositionI == null"));
		}

		myLogger.info("AgstklpositionIId: " + iIdAgstklpositionI.toString());
	}

	/**
	 * Wenn fuer eine Angebotsstueckliste eine Position geloescht wurden, dann muss
	 * die Sortierung der Positionen angepasst werden, damit keine Luecken
	 * entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server aufgerufen.
	 * 
	 * @param iIdAngebotstklI                PK des Angebots
	 * @param iSortierungGeloeschtePositionI die Position der geloschten Position
	 * @param theClientDto                   der aktuelle Benutzer
	 * @throws Throwable Ausnahme
	 */
	private void sortierungAnpassenBeimLoeschenEinerPosition(Integer iIdAngebotstklI,
			int iSortierungGeloeschtePositionI, TheClientDto theClientDto) throws Throwable {
		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, iIdAngebotstklI);
		Collection<?> clPositionen = query.getResultList();
		Iterator<?> it = clPositionen.iterator();

		while (it.hasNext()) {
			Agstklposition agstklposition = (Agstklposition) it.next();

			if (agstklposition.getISort().intValue() > iSortierungGeloeschtePositionI) {
				agstklposition.setISort(new Integer(iSortierungGeloeschtePositionI));
				iSortierungGeloeschtePositionI++;
			}
		}

		myLogger.exit("Die Sortierung wurde angepasst.");
	}

	public void sortiereNachArtikelnummer(Integer agstklIId, TheClientDto theClientDto) {

		Query query = em.createNamedQuery("AgstklpositionfindByAgstklIId");
		query.setParameter(1, agstklIId);
		AgstklpositionDto[] dtos = assembleAgstklpositionDtos(query.getResultList());

		for (int i = dtos.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				AgstklpositionDto o = dtos[j];

				AgstklpositionDto o1 = dtos[j + 1];

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
			Agstklposition lspos = em.find(Agstklposition.class, dtos[i].getIId());

			lspos.setISort(iSort);

			em.merge(lspos);
			em.flush();

			iSort++;
		}

	}

	/**
	 * Auf Belegdrucken muessen die Positionen einer Agstkl als Textblock angezigt
	 * werden. In dieser Methode wird dieser Block zusammengebaut.
	 * 
	 * @param aAgstklpositionDto die Positionen der Stueckliste
	 * @param nMengeStuecklisteI wieviele Einheiten der Stueckliste werden benoetigt
	 * @param locDruckI          Locale
	 * @param theClientDto       der aktuelle Benutzer
	 * @return String Textblock zum Andrucken
	 * @throws EJBExceptionLP Ausnahme
	 */
	public String getAgstklpositionenAsTextblock(AgstklpositionDto[] aAgstklpositionDto, BigDecimal nMengeStuecklisteI,
			Locale locDruckI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (aAgstklpositionDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("aAgstklpositionDto == null"));
		}

		String cTextblock = null;

		try {
			StringBuffer buff = new StringBuffer("");

			// jede Position eine Zeile im Druck unter dem Artikel einfuegen
			for (int j = 0; j < aAgstklpositionDto.length; j++) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aAgstklpositionDto[j].getArtikelIId(),
						theClientDto);

				// UW, 07.02.06: Die Stuecklistenpositionen werden immer fuer 1
				// Stueckliste angedruckt
				// BigDecimal nMengeStuecklistenposition =
				// nMengeStuecklisteI.multiply(aAgstklpositionDto[j].getNMenge());

				nMengeStuecklisteI = Helper.rundeKaufmaennisch(nMengeStuecklisteI,
						getMandantFac().getNachkommastellenMenge(theClientDto.getMandant()).intValue());

				// cNr des Artikels andrucken, wenn es kein Handartikel ist
				String artikelCNr = "";
				if (!artikelDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
					artikelCNr = artikelDto.getCNr() + " ";
				}

				buff.append(nMengeStuecklisteI).append(" ").append(aAgstklpositionDto[j].getEinheitCNr().trim())
						.append(" ").append(artikelCNr).append(getArtikelFac()
								.formatArtikelbezeichnungEinzeiligOhneExc(artikelDto.getIId(), locDruckI));

				if (j < aAgstklpositionDto.length - 1) {
					buff.append("\n");
				}

				cTextblock = buff.toString();
			}
		} catch (RemoteException re) {
			throwEJBExceptionLPRespectOld(re);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN, new Exception(t));
		}

		return cTextblock;
	}

	public AgstklpositionDto getNextBelegPosDto(int iIdBelegPosI, TheClientDto theClientDto) throws Exception {
		return agstklpositionFindByPrimaryKey(iIdBelegPosI, theClientDto);
	}

	public Node addBelegPosSpecialFeatures(BelegpositionDto belegPosDtoI, Node nodeFeaturesI, Document docI)
			throws Exception, DOMException {

		AgstklpositionDto bSPos = (AgstklpositionDto) belegPosDtoI;

		// Feature: Drucken
		addHVFeature(nodeFeaturesI, docI, AngebotstklpositionFac.SCHEMA_HV_FEATURE_DRUCKEN, bSPos.getBDrucken());
		return nodeFeaturesI;
	}

	@Override
	public BelegpositionDto getNewPositionDto() {
		return new AgstklpositionDto();
	}

	@Override
	/**
	 * Erstellt ein Angebotst&uuml;cklistenpositionsDto und setzt die erforderlichen
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

		((AgstklpositionDto) posDto).setBDrucken(Helper.getShortFalse());
		posDto.setBArtikelbezeichnunguebersteuert(Helper.getShortFalse());
		((AgstklpositionDto) posDto)
				.setCPosition(Helper.cutString(result.getValues().get(AgstklImportSpezifikation.POSITION),
						StuecklisteFac.FieldLength.STUECKLISTEPOSITION_POSITION));
		try {
			posDto = befuelleMitPreisenNachKalkulationsart((AgstklpositionDto) posDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return posDto;
	}

	@Override
	public void createPositions(List<BelegpositionDto> posDtos, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		createAngebotpositions(posDtos.toArray(new AgstklpositionDto[posDtos.size()]), theClientDto);
	}

	@Override
	public Integer getBezugsobjektIIdDerStueckliste(StklImportSpezifikation spez, TheClientDto theClientDto)
			throws RemoteException {
		AgstklDto agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(spez.getStklIId());

		return agstklDto == null ? null : agstklDto.getKundeIId();
	}

	@Override
	public IImportPositionen asPositionImporter() {
		return this;
	}

	@Override
	public IImportHead asHeadImporter() {
		return this;
	}

	private AgstklpositionDto befuelleMitPreisenNachKalkulationsartImpl(AgstklpositionDto agstklpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Validator.dtoNotNull(agstklpositionDto, "agstklpositionDto");
		Validator.notNull(agstklpositionDto.getAgstklIId(), "agstklpositionDto.getAgstklIId()");

		PriceCalculatorFactory priceCalculatorFactory = new PriceCalculatorFactory();
		IPriceCalculator priceCalculator = priceCalculatorFactory
				.getPriceCalculator(getParameterFac().getKalkulationsart(theClientDto.getMandant()));

		return priceCalculator.fillWithPrices(agstklpositionDto, theClientDto);
	}

	public AgstklpositionDto befuelleMitPreisenNachKalkulationsart(AgstklpositionDto agstklpositionDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		return befuelleMitPreisenNachKalkulationsartImpl(agstklpositionDto, theClientDto);
	}

	private class PriceCalculatorFactory {
		IPriceCalculator getPriceCalculator(Integer kalkulationsart) {
			if (kalkulationsart == null)
				throw new IllegalArgumentException("kalkulationsart ist 'null'");

			if (kalkulationsart == 1) {
				return new Calc1PriceCalculator();
			}
			if (kalkulationsart == 2) {
				return new Calc2PriceCalculator();
			}
			if (kalkulationsart == 3) {
				return new Calc3PriceCalculator();
			}

			throw new IllegalArgumentException("Unbekannte Kalkulationsart '" + kalkulationsart + "'");
		}
	}

	private interface IFilledAgstklpositionDto {
		void process(AgstklpositionDto agstklpositionDto);
	}

	private interface IPriceCalculator {
		AgstklpositionDto fillWithPrices(AgstklpositionDto agstklpositionDto, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException;

		void fillWithPrices(Integer agstklIId, TheClientDto theClientDto, IFilledAgstklpositionDto iterator)
				throws EJBExceptionLP, RemoteException;
	}

	/**
	 * Berechnet und f&uuml;llt die Preise einer Angebotsstklposition nach
	 * Kalkulationsart 3 (Einkaufspreisbezug mit Aufschlag)
	 * 
	 */
	private class Calc3PriceCalculator extends Calc2PriceCalculator {

		protected AgstklpositionDto fillWithPricesImpl(AgstklpositionDto agstklpositionDto)
				throws EJBExceptionLP, RemoteException {
			agstklpositionDto = super.fillWithPricesImpl(agstklpositionDto);

			if (LocaleFac.POSITIONSART_HANDEINGABE.equals(agstklpositionDto.getPositionsartCNr())) {
				return agstklpositionDto;
			}

			Double aufschlag = getParameterFac().getDefaultAufschlag(getTheClientDto().getMandant());
			agstklpositionDto.setFAufschlag(aufschlag);
			agstklpositionDto.setBAufschlaggesamtFixiert(Helper.getShortFalse());

			BigDecimal bdAufschlag = Helper.getProzentWert(agstklpositionDto.getNNettogesamtpreis(),
					new BigDecimal(aufschlag), 4);
			agstklpositionDto.setNAufschlag(bdAufschlag);
			agstklpositionDto.setNNettogesamtmitaufschlag(agstklpositionDto.getNNettogesamtpreis().add(bdAufschlag));
			return agstklpositionDto;
		}
	}

	/**
	 * Berechnet und f&uuml;llt die Preise einer Angebotsstklposition nach
	 * Kalkulationsart 2 (Einkaufspreisbezug)
	 * 
	 */
	private class Calc2PriceCalculator implements IPriceCalculator {
		private AgstklDto agstklDto;
		protected TheClientDto theClientDto;

		@Override
		public AgstklpositionDto fillWithPrices(AgstklpositionDto agstklpositionDto, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			initHeadData(agstklpositionDto.getAgstklIId(), theClientDto);
			return fillWithPricesImpl(agstklpositionDto);
		}

		@Override
		public void fillWithPrices(Integer agstklIId, TheClientDto theClientDto, IFilledAgstklpositionDto iterator)
				throws EJBExceptionLP, RemoteException {
			initHeadData(agstklIId, theClientDto);
			AgstklpositionDto[] dtos = agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			for (AgstklpositionDto dto : dtos) {
				iterator.process(fillWithPricesImpl(dto));
			}
		}

		private void initHeadData(Integer agstklIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			this.theClientDto = theClientDto;
			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklIId);
		}

		protected TheClientDto getTheClientDto() {
			return theClientDto;
		}

		protected AgstklpositionDto fillWithPricesImpl(AgstklpositionDto agstklpositionDto)
				throws EJBExceptionLP, RemoteException {
			if (LocaleFac.POSITIONSART_HANDEINGABE.equals(agstklpositionDto.getPositionsartCNr())) {
				fillWithPreisRabattDefaults(agstklpositionDto);
				return agstklpositionDto;
			}

			if (agstklpositionDto.getArtikelIId() == null || agstklpositionDto.getNMenge() == null) {
				fillWithPreisRabattDefaults(agstklpositionDto);
				return agstklpositionDto;
			}

			int iNachkommastellen = 2;
			ArtikellieferantDto artliefDto = null;
			try {
				iNachkommastellen = getMandantFac().getNachkommastellenPreisEK(theClientDto.getMandant());
				artliefDto = getArtikelFac().getArtikelEinkaufspreis(agstklpositionDto.getArtikelIId(), null,
						agstklpositionDto.getNMenge(), agstklDto.getWaehrungCNr(),
						Helper.extractDate(agstklDto.getTBelegdatum()), theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (artliefDto == null || artliefDto.getNEinzelpreis() == null) {
				fillWithPreisRabattDefaults(agstklpositionDto);
				return agstklpositionDto;
			}

			agstklpositionDto.setFRabattsatz(artliefDto.getFRabatt());
			agstklpositionDto.setNMaterialzuschlag(artliefDto.getNMaterialzuschlag());
			agstklpositionDto.setNNettoeinzelpreis(artliefDto.getNEinzelpreis());
			BigDecimal nettogesamtpreis = artliefDto.getNEinzelpreis().subtract(Helper.getProzentWert(
					artliefDto.getNEinzelpreis(), BigDecimal.valueOf(artliefDto.getFRabatt()), iNachkommastellen));
			nettogesamtpreis = nettogesamtpreis.add(artliefDto.getNMaterialzuschlag());
			agstklpositionDto.setNNettogesamtpreis(nettogesamtpreis);

			if (Helper.short2boolean(artliefDto.getBRabattbehalten())) {
				agstklpositionDto.setBRabattsatzuebersteuert(Helper.getShortTrue());
				agstklpositionDto.setBNettopreisuebersteuert(Helper.getShortFalse());
			} else {
				agstklpositionDto.setBNettopreisuebersteuert(Helper.getShortTrue());
				agstklpositionDto.setBRabattsatzuebersteuert(Helper.getShortFalse());
			}

			return agstklpositionDto;
		}
	}

	/**
	 * Berechnet und f&uuml;llt die Preise einer Angebotsstklposition nach
	 * Kalkulationsart 1 (Verkaufspreisbezug)
	 * 
	 */
	private class Calc1PriceCalculator implements IPriceCalculator {
		private AgstklDto agstklDto;
		private KundeDto kundeDto;
		private TheClientDto theClientDto;

		@Override
		public AgstklpositionDto fillWithPrices(AgstklpositionDto agstklpositionDto, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			initHeadData(agstklpositionDto.getAgstklIId(), theClientDto);
			return fillWithPricesImpl(agstklpositionDto);
		}

		@Override
		public void fillWithPrices(Integer agstklIId, TheClientDto theClientDto, IFilledAgstklpositionDto iterator)
				throws EJBExceptionLP, RemoteException {
			initHeadData(agstklIId, theClientDto);
			AgstklpositionDto[] dtos = agstklpositionFindByAgstklIId(agstklIId, theClientDto);

			for (AgstklpositionDto dto : dtos) {
				iterator.process(fillWithPricesImpl(dto));
			}
		}

		private void initHeadData(Integer agstklIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			this.theClientDto = theClientDto;
			agstklDto = getAngebotstklFac().agstklFindByPrimaryKey(agstklIId);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(agstklDto.getKundeIId(), theClientDto);
		}

		private AgstklpositionDto fillWithPricesImpl(AgstklpositionDto dto) throws EJBExceptionLP, RemoteException {
			if (LocaleFac.POSITIONSART_HANDEINGABE.equals(dto.getPositionsartCNr())) {
				fillWithPreisRabattDefaults(dto);
				return dto;
			}

			if (dto.getArtikelIId() == null || dto.getNMenge() == null) {
				fillWithPreisRabattDefaults(dto);
				return dto;
			}

			VkpreisfindungDto vkpreisfindungDto = getVkPreisfindungFac().verkaufspreisfindung(dto.getArtikelIId(),
					agstklDto.getKundeIId(), dto.getNMenge(), Helper.extractDate(agstklDto.getTBelegdatum()),
					kundeDto.getVkpfArtikelpreislisteIIdStdpreisliste(), kundeDto.getMwstsatzbezIId(),
					agstklDto.getWaehrungCNr(), theClientDto);

			VerkaufspreisDto verkaufspreisDtoInZielwaehrung = Helper.getVkpreisBerechnet(vkpreisfindungDto);

			if (verkaufspreisDtoInZielwaehrung != null) {
				dto.setNAufschlag(BigDecimal.ZERO);
				dto.setFAufschlag(0D);
				dto.setFZusatzrabattsatz(0D);
				dto.setNMaterialzuschlag(verkaufspreisDtoInZielwaehrung.bdMaterialzuschlag);
				dto.setNNettoeinzelpreis(verkaufspreisDtoInZielwaehrung.einzelpreis);
				dto.setFRabattsatz(verkaufspreisDtoInZielwaehrung.rabattsatz);
				dto.setNNettogesamtpreis(verkaufspreisDtoInZielwaehrung.nettopreis);
				dto.setNNettogesamtmitaufschlag(verkaufspreisDtoInZielwaehrung.nettopreis);
				if (dto.getBRabattsatzuebersteuert() == null) {
					dto.setBRabattsatzuebersteuert(Helper.getShortFalse());
				}
			} else {
				fillWithPreisRabattDefaults(dto);
				fillWithMaterialzuschlag(dto);
				dto.setNNettogesamtpreis(dto.getNMaterialzuschlag());
			}

			fillWithGestehungspreis(dto);

			return dto;
		}

		private void fillWithGestehungspreis(AgstklpositionDto dto) throws EJBExceptionLP, RemoteException {

			if (LocaleFac.POSITIONSART_IDENT.equals(dto.getPositionsartCNr()) && dto.getArtikelIId() != null) {
				dto.setNGestehungspreis(getLagerFac().getGemittelterGestehungspreisEinesLagers(dto.getArtikelIId(),
						getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId(), theClientDto));
			} else {
				dto.setNGestehungspreis(BigDecimal.ZERO);
			}
		}

		private void fillWithMaterialzuschlag(AgstklpositionDto dto) {
			BigDecimal nMaterialzuschlag = getMaterialFac().getMaterialzuschlagVKInZielwaehrung(dto.getArtikelIId(),
					null, Helper.extractDate(agstklDto.getTBelegdatum()), agstklDto.getWaehrungCNr(), theClientDto);
			dto.setNMaterialzuschlag(nMaterialzuschlag != null ? nMaterialzuschlag : BigDecimal.ZERO);
		}
	}

	private void fillWithPreisRabattDefaults(AgstklpositionDto dto) {
		if (dto.getFRabattsatz() == null)
			dto.setFRabattsatz(0D);
		if (dto.getNNettoeinzelpreis() == null)
			dto.setNNettoeinzelpreis(new BigDecimal(0));
		if (dto.getNNettogesamtpreis() == null)
			dto.setNNettogesamtpreis(new BigDecimal(0));
		if (dto.getBNettopreisuebersteuert() == null)
			dto.setBNettopreisuebersteuert(Helper.getShortTrue());
		if (dto.getBRabattsatzuebersteuert() == null)
			dto.setBRabattsatzuebersteuert(Helper.getShortFalse());
	}
}
