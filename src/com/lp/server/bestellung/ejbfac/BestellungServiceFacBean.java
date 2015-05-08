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
package com.lp.server.bestellung.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.artikel.ejb.Artkla;
import com.lp.server.bestellung.ejb.Bestellpositionart;
import com.lp.server.bestellung.ejb.Bestellpositionstatus;
import com.lp.server.bestellung.ejb.Bestellungart;
import com.lp.server.bestellung.ejb.Bestellungartspr;
import com.lp.server.bestellung.ejb.BestellungartsprPK;
import com.lp.server.bestellung.ejb.Bestellungstatus;
import com.lp.server.bestellung.ejb.Bestellungtext;
import com.lp.server.bestellung.ejb.Mahngruppe;
import com.lp.server.bestellung.service.BestellpositionartDto;
import com.lp.server.bestellung.service.BestellpositionartDtoAssembler;
import com.lp.server.bestellung.service.BestellpositionstatusDto;
import com.lp.server.bestellung.service.BestellpositionstatusDtoAssembler;
import com.lp.server.bestellung.service.BestellungServiceFac;
import com.lp.server.bestellung.service.BestellungartDtoAssembler;
import com.lp.server.bestellung.service.BestellungartSprDtoAssembler;
import com.lp.server.bestellung.service.BestellungsartDto;
import com.lp.server.bestellung.service.BestellungsartsprDto;
import com.lp.server.bestellung.service.BestellungstatusDto;
import com.lp.server.bestellung.service.BestellungstatusDtoAssembler;
import com.lp.server.bestellung.service.BestellungtextDto;
import com.lp.server.bestellung.service.BestellungtextDtoAssembler;
import com.lp.server.bestellung.service.MahngruppeDto;
import com.lp.server.bestellung.service.MahngruppeDtoAssembler;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BestellungServiceFacBean extends Facade implements
		BestellungServiceFac {
	@PersistenceContext
	private EntityManager em;

	// Bestellungtext
	// ------------------------------------------------------------

	public Integer createBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkBestellungtextDto(bestellungtextDto);

		// den PK erzeugen und setzen
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_BESTELLUNGTEXT);
		bestellungtextDto.setIId(iId);

		try {
			Bestellungtext bestellungtext = new Bestellungtext(
					bestellungtextDto.getIId(),
					bestellungtextDto.getMandantCNr(),
					bestellungtextDto.getLocaleCNr(),
					bestellungtextDto.getCNr(),
					bestellungtextDto.getXTextinhalt());
			em.persist(bestellungtext);
			em.flush();
			setBestellungtextFromBestellungtextDto(bestellungtext,
					bestellungtextDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return iId;
	}

	public void removeBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkBestellungtextDto(bestellungtextDto);

		// try {
		Bestellungtext toRemove = em.find(Bestellungtext.class,
				bestellungtextDto.getIId());
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }

	}

	public void updateBestellungtext(BestellungtextDto bestellungtextDto)
			throws EJBExceptionLP {
		myLogger.entry();
		checkBestellungtextDto(bestellungtextDto);

		// try {
		Bestellungtext bestellungtext = em.find(Bestellungtext.class,
				bestellungtextDto.getIId());
		if (bestellungtext == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setBestellungtextFromBestellungtextDto(bestellungtext,
				bestellungtextDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public BestellungtextDto bestellungtextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}

		BestellungtextDto textDto = null;

		// try {
		Bestellungtext text = em.find(Bestellungtext.class, iId);
		if (text == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		textDto = assembleBestellungtextDto(text);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return textDto;
	}

	public BestellungtextDto bestellungtextFindByMandantLocaleCNr(
			String cNrMandantI, String cNrLocaleI, String cNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();

		if (cNrMandantI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrMandantI == null"));
		}

		if (cNrLocaleI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrLocaleI == null"));
		}

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrI == null"));
		}

		BestellungtextDto text = null;
		Bestellungtext textObject = null;

		try {
			// Schritt 1 : Sprache des Kunden ist Parameter
			Query query = em
					.createNamedQuery("BestellungtextfindByMandantLocaleCNr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, cNrLocaleI);
			query.setParameter(3, cNrI);
			textObject = (Bestellungtext) query.getSingleResult();
			/*
			 * if (textObject == null) { try { // Schritt 2 : Den Bestellungtext
			 * in Gewaehlte UI-Sprache des // Users anlegen
			 * createDefaultBestellungtext(cNrI, cNrLocaleI, cNrUserI); query =
			 * em .createNamedQuery("BestellungtextfindByMandantLocaleCNr");
			 * query.setParameter(1, theClientDto.getMandant());
			 * query.setParameter(2, cNrLocaleI); query.setParameter(3, cNrI);
			 * textObject = (Bestellungtext) query.getSingleResult(); } catch
			 * (Exception exUi) { } }
			 */
		} catch (NoResultException ex) {
			try {
				// Schritt 2 : Den Bestellungtext in Gewaehlte UI-Sprache des
				// Users
				// anlegen
				createDefaultBestellungtext(cNrI, cNrLocaleI, theClientDto);
				Query query = em
						.createNamedQuery("BestellungtextfindByMandantLocaleCNr");
				query.setParameter(1, theClientDto.getMandant());
				query.setParameter(2, cNrLocaleI);
				query.setParameter(3, cNrI);
				// @todo getSingleResult oder getResultList ?
				textObject = (Bestellungtext) query.getSingleResult();
			} catch (Exception exUi) {
			}
		}

		text = assembleBestellungtextDto(textObject);

		myLogger.logData(text);

		return text;
	}

	public BestellungtextDto createDefaultBestellungtext(String sMediaartI,
			String cNrLocaleI, TheClientDto theClientDto) throws EJBExceptionLP {
		BestellungtextDto oBestellungtextDto = new BestellungtextDto();
		oBestellungtextDto.setCNr(sMediaartI);
		oBestellungtextDto.setLocaleCNr(cNrLocaleI);
		oBestellungtextDto.setMandantCNr(theClientDto.getMandant());
		String cTextinhalt = null;
		if (sMediaartI.equals(MediaFac.MEDIAART_KOPFTEXT)) {
			cTextinhalt = BestellungServiceFac.BESTELLUNG_DEFAULT_KOPFTEXT;
		} else if (sMediaartI.equals(MediaFac.MEDIAART_FUSSTEXT)) {
			cTextinhalt = BestellungServiceFac.BESTELLUNG_DEFAULT_FUSSTEXT;
		}
		oBestellungtextDto.setXTextinhalt(cTextinhalt);
		oBestellungtextDto.setIId(createBestellungtext(oBestellungtextDto));

		return oBestellungtextDto;
	}

	private void setBestellungtextFromBestellungtextDto(
			Bestellungtext bestellungtext, BestellungtextDto bestellungtextDto) {
		bestellungtext.setMandantCNr(bestellungtextDto.getMandantCNr());
		bestellungtext.setLocaleCNr(bestellungtextDto.getLocaleCNr());
		bestellungtext.setCNr(bestellungtextDto.getCNr());
		bestellungtext.setXTextinhalt(bestellungtextDto.getXTextinhalt());
		em.merge(bestellungtext);
		em.flush();
	}

	private BestellungtextDto assembleBestellungtextDto(
			Bestellungtext bestellungtext) {
		return BestellungtextDtoAssembler.createDto(bestellungtext);
	}

	private BestellungtextDto[] assembleBestellungtextDtos(
			Collection<?> bestellungtexts) {
		List<BestellungtextDto> list = new ArrayList<BestellungtextDto>();
		if (bestellungtexts != null) {
			Iterator<?> iterator = bestellungtexts.iterator();
			while (iterator.hasNext()) {
				Bestellungtext bestellungtext = (Bestellungtext) iterator
						.next();
				list.add(assembleBestellungtextDto(bestellungtext));
			}
		}
		BestellungtextDto[] returnArray = new BestellungtextDto[list.size()];
		return (BestellungtextDto[]) list.toArray(returnArray);
	}

	private void checkBestellungtextDto(BestellungtextDto oBestellungtextDtoI)
			throws EJBExceptionLP {
		if (oBestellungtextDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oBestellungtextDtoI == null"));
		}
	}

	// Bestellpositionart
	// ---------------------------------------------------------

	public String createBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkBestellpositionartDto(bestellpositionartDtoI);

		try {
			Bestellpositionart bestellpositionart = new Bestellpositionart(
					bestellpositionartDtoI.getPositionsartCNr(),
					bestellpositionartDtoI.getISort(),
					bestellpositionartDtoI.getBVersteckt());
			em.persist(bestellpositionart);
			em.flush();
			setBestellpositionartFromBestellpositionartDto(bestellpositionart,
					bestellpositionartDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return bestellpositionartDtoI.getPositionsartCNr();
	}

	public void removeBestellpositionart(String positionsartCNrI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkBestellpositionartCNr(positionsartCNrI);

		// try {
		Bestellpositionart toRemove = em.find(Bestellpositionart.class,
				positionsartCNrI);
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void updateBestellpositionart(
			BestellpositionartDto bestellpositionartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkBestellpositionartDto(bestellpositionartDtoI);

		// try {
		Bestellpositionart bestellpositionart = em.find(
				Bestellpositionart.class, bestellpositionartDtoI.getCNr());
		if (bestellpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setBestellpositionartFromBestellpositionartDto(bestellpositionart,
				bestellpositionartDtoI);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public BestellpositionartDto bestellpositionartFindByPrimaryKey(
			String positionsartCNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		checkBestellpositionartCNr(positionsartCNrI);

		BestellpositionartDto bestellpositionartDto = null;

		// try {
		Bestellpositionart bestellpositionart = em.find(
				Bestellpositionart.class, positionsartCNrI);
		if (bestellpositionart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellpositionartDto = assembleBestellpositionartDto(bestellpositionart);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return bestellpositionartDto;
	}

	private void setBestellpositionartFromBestellpositionartDto(
			Bestellpositionart bestellpositionart,
			BestellpositionartDto bestellpositionartDto) {
		bestellpositionart.setISort(bestellpositionartDto.getISort());
		bestellpositionart.setBVersteckt(bestellpositionartDto.getBVersteckt());
		em.merge(bestellpositionart);
		em.flush();
	}

	private void checkBestellpositionartDto(
			BestellpositionartDto bestellpositionartDtoI) throws EJBExceptionLP {
		if (bestellpositionartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bestellpositionartDtoI == null"));
		}

		myLogger.info("BestellpositionartDto: "
				+ bestellpositionartDtoI.toString());
	}

	private void checkBestellpositionartCNr(String cNrBestellpositionartI)
			throws EJBExceptionLP {
		if (cNrBestellpositionartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("cNrBestellpositionartI == null"));
		}

		myLogger.info("BestellpositionartCNr: " + cNrBestellpositionartI);
	}

	private BestellpositionartDto assembleBestellpositionartDto(
			Bestellpositionart bestellpositionart) {
		return BestellpositionartDtoAssembler.createDto(bestellpositionart);
	}

	private BestellpositionartDto[] assembleBestellpositionartDtos(
			Collection<?> bestellpositionarts) {
		List<BestellpositionartDto> list = new ArrayList<BestellpositionartDto>();
		if (bestellpositionarts != null) {
			Iterator<?> iterator = bestellpositionarts.iterator();
			while (iterator.hasNext()) {
				Bestellpositionart bestellpositionart = (Bestellpositionart) iterator
						.next();
				list.add(assembleBestellpositionartDto(bestellpositionart));
			}
		}
		BestellpositionartDto[] returnArray = new BestellpositionartDto[list
				.size()];
		return (BestellpositionartDto[]) list.toArray(returnArray);
	}

	public Map<String, String> getBestellpositionart(Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		// @todo param PJ 3900
		Map<String, String> map = null;
		try {
			Query query = em
					.createNamedQuery("BestellpositionartfindAllEnable");
			Collection<?> arten = query.getResultList();
			// if(arten.isEmpty()){ // @ToDo FinderException
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,
			// null);
			// }
			BestellpositionartDto[] artenDtos = assembleBestellpositionartDtos(arten);
			map = (Map<String, String>) getSystemMultilanguageFac()
					.uebersetzePositionsartOptimal(artenDtos, locale1, locale2);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
		return map;
	}

	public void createBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP {

		try {
			Bestellpositionstatus bestellpositionstatus = new Bestellpositionstatus(
					bestellpositionstatusDto.getStatusCNr(),
					bestellpositionstatusDto.getISort());
			em.persist(bestellpositionstatus);
			em.flush();
			setBestellpositionstatusFromBestellpositionstatusDto(
					bestellpositionstatus, bestellpositionstatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void createMahngruppe(MahngruppeDto mahngruppeDto) {

		try {

			Mahngruppe m = em.find(Mahngruppe.class,
					mahngruppeDto.getArtgruIId());

			if (m != null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"BES_MAHNGRUPPE.UK"));
			}

			Mahngruppe mahngruppe = new Mahngruppe(mahngruppeDto.getArtgruIId());
			em.persist(mahngruppe);
			em.flush();
			setMahngruppeFromMahngruppeDto(mahngruppe, mahngruppeDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeBestellpositionstatus(String statusCNr)
			throws EJBExceptionLP {
		// try {
		Bestellpositionstatus toRemove = em.find(Bestellpositionstatus.class,
				statusCNr);
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeMahngruppe(MahngruppeDto mahngruppeDto) {

		Mahngruppe toRemove = em.find(Mahngruppe.class,
				mahngruppeDto.getArtgruIId());
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

	public void removeBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (bestellpositionstatusDto != null) {
			String statusCNr = bestellpositionstatusDto.getStatusCNr();
			removeBestellpositionstatus(statusCNr);
		}
	}

	public void updateBestellpositionstatus(
			BestellpositionstatusDto bestellpositionstatusDto)
			throws EJBExceptionLP {
		myLogger.entry();

		if (bestellpositionstatusDto != null) {
			String statusCNr = bestellpositionstatusDto.getStatusCNr();
			// try {
			Bestellpositionstatus bestellpositionstatus = em.find(
					Bestellpositionstatus.class, statusCNr);
			if (bestellpositionstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellpositionstatusFromBestellpositionstatusDto(
					bestellpositionstatus, bestellpositionstatusDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateBestellpositionstatuss(
			BestellpositionstatusDto[] bestellpositionstatusDtos)
			throws EJBExceptionLP {
		myLogger.entry();

		if (bestellpositionstatusDtos != null) {
			for (int i = 0; i < bestellpositionstatusDtos.length; i++) {
				updateBestellpositionstatus(bestellpositionstatusDtos[i]);
			}
		}
	}

	public BestellpositionstatusDto bestellpositionstatusFindByPrimaryKey(
			String statusCNr) throws EJBExceptionLP {

		Bestellpositionstatus bestellpositionstatus = em.find(
				Bestellpositionstatus.class, statusCNr);
		if (bestellpositionstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBestellpositionstatusDto(bestellpositionstatus);

	}

	public MahngruppeDto mahngruppeFindByPrimaryKey(Integer artgruIId) {
		return assembleMahngruppeDto(em.find(Mahngruppe.class, artgruIId));

	}

	private void setBestellpositionstatusFromBestellpositionstatusDto(
			Bestellpositionstatus bestellpositionstatus,
			BestellpositionstatusDto bestellpositionstatusDto) {
		bestellpositionstatus.setISort(bestellpositionstatusDto.getISort());
		em.merge(bestellpositionstatus);
		em.flush();
	}

	private void setMahngruppeFromMahngruppeDto(Mahngruppe mahngruppe,
			MahngruppeDto mahngruppeDto) {
		em.merge(mahngruppe);
		em.flush();
	}

	private BestellpositionstatusDto assembleBestellpositionstatusDto(
			Bestellpositionstatus bestellpositionstatus) {
		return BestellpositionstatusDtoAssembler
				.createDto(bestellpositionstatus);
	}

	private MahngruppeDto assembleMahngruppeDto(Mahngruppe mahngruppe) {
		return MahngruppeDtoAssembler.createDto(mahngruppe);
	}

	// private BestellpositionstatusDto[]
	// assembleBestellpositionstatusDtos(Collection<?>
	// bestellpositionstatuss) {
	// List list = new ArrayList();
	// if (bestellpositionstatuss != null) {
	// Iterator<?> iterator = bestellpositionstatuss.iterator();
	// while (iterator.hasNext()) {
	// Bestellpositionstatus bestellpositionstatus = (Bestellpositionstatus)
	// iterator.
	// next();
	// list.add(assembleBestellpositionstatusDto(bestellpositionstatus));
	// }
	// }
	// BestellpositionstatusDto[] returnArray = new
	// BestellpositionstatusDto[list.size()];
	// return (BestellpositionstatusDto[]) list.toArray(returnArray);
	// }

	public String createBestellungart(BestellungsartDto bestellungartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (bestellungartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("BestellungartDtoI == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("theClientDto == null"));
		}

		try {
			Bestellungart bestellungart = new Bestellungart(
					bestellungartDtoI.getCNr(), bestellungartDtoI.getISort());
			em.persist(bestellungart);
			em.flush();

			setBestellungartFromBestellungartDto(bestellungart,
					bestellungartDtoI);

			if (bestellungartDtoI.getBestellungsartsprDto() != null) {
				bestellungartDtoI.getBestellungsartsprDto()
						.setBestellungartCNr(bestellungartDtoI.getCNr());
				createBestellungartspr(
						bestellungartDtoI.getBestellungsartsprDto(),
						theClientDto);
			}

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return bestellungartDtoI.getCNr();
	}

	public String createBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto) {
		// precondition
		if (bestellungartsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bestellungartsprDtoI == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"theClientDto == null"));
		}

		Bestellungartspr bestellungartspr = null;
		try {
			bestellungartspr = new Bestellungartspr(
					bestellungartsprDtoI.getLocaleCNr(),
					bestellungartsprDtoI.getBestellungartCNr());
			em.persist(bestellungartspr);
			em.flush();
			bestellungartspr.setCBez(bestellungartsprDtoI.getCBez());
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return bestellungartspr.getBestellungartCNr();
	}

	public void removeBestellungart(String cNrI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		Bestellungart toRemove = em.find(Bestellungart.class, cNrI);
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeBestellungart(BestellungsartDto bestellungartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (bestellungartDto != null) {
			String cNr = bestellungartDto.getCNr();
			removeBestellungart(bestellungartDto, theClientDto);
		}
	}

	public void removeBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto) {
		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"theClientDto == null"));
		}
		if (bestellungartsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"bestellungartsprDtoI == null"));
		}

		// try {
		Bestellungartspr toRemove = em.find(
				Bestellungartspr.class,
				new BestellungartsprPK(bestellungartsprDtoI
						.getBestellungartCNr(), bestellungartsprDtoI
						.getLocaleCNr()));
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void updateBestellungartspr(BestellungsartsprDto bestellungartsprDto)
			throws EJBExceptionLP {
		if (bestellungartsprDto != null) {
			BestellungartsprPK bestellungartsprPK = new BestellungartsprPK();
			bestellungartsprPK.setLocaleCNr(bestellungartsprDto.getLocaleCNr());
			bestellungartsprPK.setBestellungartCNr(bestellungartsprDto
					.getBestellungartCNr());
			// try {
			Bestellungartspr bestellunggartspr = em.find(
					Bestellungartspr.class, bestellungartsprPK);
			if (bestellunggartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellungartsprFromBestellungartsprDto(bestellunggartspr,
					bestellungartsprDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	private void setBestellungartsprFromBestellungartsprDto(
			Bestellungartspr bestellungartspr,
			BestellungsartsprDto bestellungartsprDto) {
		bestellungartspr.setCBez(bestellungartsprDto.getCBez());
		em.merge(bestellungartspr);
		em.flush();
	}

	public void updateBestellungart(BestellungsartDto bestellungartDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (bestellungartDtoI != null) {
			String cNr = bestellungartDtoI.getCNr();
			// try {
			Bestellungart bestellungart = em.find(Bestellungart.class, cNr);
			if (bestellungart == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellungartFromBestellungartDto(bestellungart,
					bestellungartDtoI);

			if (bestellungartDtoI.getBestellungsartsprDto() != null
					&& bestellungartDtoI.getCNr() != null) {

				BestellungartsprPK bestellungartsprPK = new BestellungartsprPK();
				bestellungartsprPK.setLocaleCNr(bestellungartDtoI
						.getBestellungsartsprDto().getLocaleCNr());
				bestellungartsprPK.setBestellungartCNr(bestellungartDtoI
						.getCNr());

				Bestellungartspr bestellungartspr = em.find(
						Bestellungartspr.class, bestellungartsprPK);
				if (bestellungartspr == null) {
					Bestellungartspr bestellungartsprneu = new Bestellungartspr(
							bestellungartDtoI.getBestellungsartsprDto()
									.getLocaleCNr(), bestellungartDtoI.getCNr());

					em.persist(bestellungartsprneu);
					em.flush();
				} else {
					setBestellungartsprFromBestellungartsprDto(
							bestellungartspr,
							bestellungartDtoI.getBestellungsartsprDto());
				}

			}
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateBestellungartspr(
			BestellungsartsprDto bestellungartsprDtoI, TheClientDto theClientDto) {
		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"theClientDto == null"));
		}
		if (bestellungartsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"bestellungartsprDtoI == null"));
		}

		// try {
		Bestellungartspr bestellungartspr = em.find(Bestellungartspr.class,
				new BestellungartsprPK(bestellungartsprDtoI.getLocaleCNr(),
						bestellungartsprDtoI.getBestellungartCNr()));
		if (bestellungartspr == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellungartspr.setCBez(bestellungartsprDtoI.getCBez());
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	/**
	 * Alle Bestellungarten in der bestmoeglichen Uebersetzung holen.
	 * 
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getBestellungart(Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		myLogger.entry();

		Map<String, String> map = null;

		// try {
		Query query = em.createNamedQuery("BestellungartfindAll");
		Collection<?> arten = query.getResultList();
		// if(arten.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		BestellungsartDto[] artDtos = this.assembleBestellungartDtos(arten);
		map = this.uebersetzeBestellungartOptimal(artDtos, locale1, locale2);
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, ex);
		// }
		return map;
	}

	public BestellungsartDto[] getAllBestellungsArt() {
		Query query = em.createNamedQuery("BestellungartfindAll");
		Collection<?> arten = query.getResultList();
		return assembleBestellungartDtos(arten);
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen fuer ein Array von
	 * Bestellungarten.
	 * 
	 * @param pArray
	 *            Stati
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	private Map uebersetzeBestellungartOptimal(BestellungsartDto[] pArray,
			Locale locale1, Locale locale2) throws EJBExceptionLP {
		myLogger.entry();
		Map uebersetzung = new TreeMap<Object, Object>();
		for (int i = 0; i < pArray.length; i++) {
			String key = pArray[i].getCNr();
			String value = this.uebersetzeBestellungartOptimal(
					pArray[i].getCNr(), locale1, locale2);
			uebersetzung.put(key, value);
		}

		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Bestellungart optimal. 1.Versuch: mit locale1 2.Versuch:
	 * mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            der Name der Bestellungart
	 * @param locale1
	 *            bevorzugtes Locale
	 * @param locale2
	 *            Locale Ersatzlocale
	 * @throws EJBExceptionLP
	 * @return String die Bestellungarten mit Uebersetzung
	 */
	private String uebersetzeBestellungartOptimal(String cNr, Locale locale1,
			Locale locale2) throws EJBExceptionLP {
		myLogger.entry();
		String uebersetzung = "";
		uebersetzung = this.uebersetzeBestellungart(locale1, cNr);
		if (uebersetzung == null) {
			uebersetzung = this.uebersetzeBestellungart(locale2, cNr);
		}
		if (uebersetzung == null) {
			uebersetzung = cNr;
		}
		return uebersetzung;
	}

	/**
	 * Eine Bestellgart in eine bestimmte Sprache uebersetzen.
	 * 
	 * @param pLocale
	 *            Locale
	 * @param pArt
	 *            String
	 * @throws EJBExceptionLP
	 * @return String
	 */
	private String uebersetzeBestellungart(Locale pLocale, String pArt)
			throws EJBExceptionLP {
		myLogger.entry();
		Bestellungartspr spr = null;
		try {
			String locale = Helper.locale2String(pLocale);
			Query query = em
					.createNamedQuery("BestellungartSprfindBySpracheAndCNr");
			query.setParameter(1, locale);
			query.setParameter(2, pArt);
			spr = (Bestellungartspr) query.getSingleResult();
			if (spr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
		} catch (NoResultException ex) {
			return null;
		}
		return spr.getCBez();
	}

	public BestellungsartDto bestellungartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"cNr == null"));
		}
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"theClientDto == null"));
		}
		BestellungsartDto bestellungartDto = null;

		// try {
		Bestellungart bestellungart = em.find(Bestellungart.class, cNr);
		if (bestellungart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		bestellungartDto = assembleBestellungartDto(bestellungart);

		// SprDto
		// try {
		Bestellungartspr bestellungartspr = em.find(
				Bestellungartspr.class,
				new BestellungartsprPK(bestellungartDto.getCNr(), theClientDto
						.getLocUiAsString()));
		if (bestellungartspr != null) {
			bestellungartDto
					.setBestellungsartsprDto(assembleBestellungartsprDto(bestellungartspr));
		}
		// }
		// catch (FinderException t) {
		// nothing here.
		// }
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		return bestellungartDto;
	}

	private BestellungsartsprDto assembleBestellungartsprDto(
			Bestellungartspr bestellungartspr) {
		return BestellungartSprDtoAssembler.createDto(bestellungartspr);
	}

	private void setBestellungartFromBestellungartDto(
			Bestellungart bestellungart, BestellungsartDto bestellungartDto) {
		bestellungart.setISort(bestellungartDto.getISort());
		em.merge(bestellungart);
		em.flush();
	}

	private BestellungsartDto assembleBestellungartDto(
			Bestellungart bestellungart) {
		return BestellungartDtoAssembler.createDto(bestellungart);
	}

	private BestellungsartDto[] assembleBestellungartDtos(
			Collection<?> bestellungarts) {
		List<BestellungsartDto> list = new ArrayList<BestellungsartDto>();
		if (bestellungarts != null) {
			Iterator<?> iterator = bestellungarts.iterator();
			while (iterator.hasNext()) {
				Bestellungart bestellungart = (Bestellungart) iterator.next();
				list.add(assembleBestellungartDto(bestellungart));
			}
		}
		BestellungsartDto[] returnArray = new BestellungsartDto[list.size()];
		return (BestellungsartDto[]) list.toArray(returnArray);
	}

	public String createBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP {
		if (bestellungstatusDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bestellungstatusDto == null"));

		}
		Bestellungstatus bestellungstatus = null;
		try {
			bestellungstatus = new Bestellungstatus(
					bestellungstatusDto.getStatusCNr(),
					bestellungstatusDto.getISort());
			em.persist(bestellungstatus);
			em.flush();
			setBestellungstatusFromBestellungstatusDto(bestellungstatus,
					bestellungstatusDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return bestellungstatus.getStatusCNr();
	}

	public void removeBestellungstatus(String statusCNr) throws EJBExceptionLP {
		// try {
		Bestellungstatus toRemove = em.find(Bestellungstatus.class, statusCNr);
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void removeBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP {
		if (bestellungstatusDto != null) {
			String statusCNr = bestellungstatusDto.getStatusCNr();
			removeBestellungstatus(statusCNr);
		}
	}

	public void updateBestellungstatus(BestellungstatusDto bestellungstatusDto)
			throws EJBExceptionLP {

		myLogger.entry();

		if (bestellungstatusDto != null) {
			String statusCNr = bestellungstatusDto.getStatusCNr();
			// try {
			Bestellungstatus bestellungstatus = em.find(Bestellungstatus.class,
					statusCNr);
			if (bestellungstatus == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellungstatusFromBestellungstatusDto(bestellungstatus,
					bestellungstatusDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateBestellungstatuss(
			BestellungstatusDto[] bestellungstatusDtos) throws EJBExceptionLP {
		myLogger.entry();

		if (bestellungstatusDtos != null) {
			for (int i = 0; i < bestellungstatusDtos.length; i++) {
				updateBestellungstatus(bestellungstatusDtos[i]);
			}
		}
	}

	public BestellungstatusDto bestellungstatusFindByPrimaryKey(String statusCNr)
			throws EJBExceptionLP {
		// try {
		Bestellungstatus bestellungstatus = em.find(Bestellungstatus.class,
				statusCNr);
		if (bestellungstatus == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBestellungstatusDto(bestellungstatus);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	private void setBestellungstatusFromBestellungstatusDto(
			Bestellungstatus bestellungstatus,
			BestellungstatusDto bestellungstatusDto) {
		bestellungstatus.setISort(bestellungstatusDto.getISort());
		em.merge(bestellungstatus);
		em.flush();
	}

	private BestellungstatusDto assembleBestellungstatusDto(
			Bestellungstatus bestellungstatus) {
		return BestellungstatusDtoAssembler.createDto(bestellungstatus);
	}

	private BestellungstatusDto[] assembleBestellungstatusDtos(
			Collection<?> bestellungstatuss) {
		List<BestellungstatusDto> list = new ArrayList<BestellungstatusDto>();
		if (bestellungstatuss != null) {
			Iterator<?> iterator = bestellungstatuss.iterator();
			while (iterator.hasNext()) {
				Bestellungstatus bestellungstatus = (Bestellungstatus) iterator
						.next();
				list.add(assembleBestellungstatusDto(bestellungstatus));
			}
		}
		BestellungstatusDto[] returnArray = new BestellungstatusDto[list.size()];
		return (BestellungstatusDto[]) list.toArray(returnArray);
	}

}
