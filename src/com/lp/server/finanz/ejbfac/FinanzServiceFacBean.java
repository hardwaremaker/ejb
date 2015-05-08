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
package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.ejb.Bankverbindung;
import com.lp.server.finanz.ejb.Buchung;
import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.ejb.Buchungsart;
import com.lp.server.finanz.ejb.Buchungsartspr;
import com.lp.server.finanz.ejb.BuchungsartsprPK;
import com.lp.server.finanz.ejb.Konto;
import com.lp.server.finanz.ejb.Kontoart;
import com.lp.server.finanz.ejb.Kontoartspr;
import com.lp.server.finanz.ejb.KontoartsprPK;
import com.lp.server.finanz.ejb.Kontotyp;
import com.lp.server.finanz.ejb.Kontotypspr;
import com.lp.server.finanz.ejb.KontotypsprPK;
import com.lp.server.finanz.ejb.Mahnspesen;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejb.Mahntext;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Steuerkategoriekonto;
import com.lp.server.finanz.ejb.SteuerkategoriekontoPK;
import com.lp.server.finanz.ejb.Uvaart;
import com.lp.server.finanz.ejb.Uvaartspr;
import com.lp.server.finanz.ejb.UvaartsprPK;
import com.lp.server.finanz.ejb.Uvaverprobung;
import com.lp.server.finanz.ejb.Warenverkehrsnummer;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzLaenderart;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzWarenverkehrsnummer;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.BuchungsartDto;
import com.lp.server.finanz.service.BuchungsartDtoAssembler;
import com.lp.server.finanz.service.BuchungsartsprDto;
import com.lp.server.finanz.service.BuchungsartsprDtoAssembler;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoartDto;
import com.lp.server.finanz.service.KontoartDtoAssembler;
import com.lp.server.finanz.service.KontoartsprDto;
import com.lp.server.finanz.service.KontoartsprDtoAssembler;
import com.lp.server.finanz.service.KontotypDto;
import com.lp.server.finanz.service.KontotypDtoAssembler;
import com.lp.server.finanz.service.KontotypsprDto;
import com.lp.server.finanz.service.KontotypsprDtoAssembler;
import com.lp.server.finanz.service.LaenderartDto;
import com.lp.server.finanz.service.LaenderartDtoAssembler;
import com.lp.server.finanz.service.LaenderartsprDto;
import com.lp.server.finanz.service.LaenderartsprDtoAssembler;
import com.lp.server.finanz.service.MahnspesenDto;
import com.lp.server.finanz.service.MahnspesenDtoAssembler;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.finanz.service.MahntextDtoAssembler;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategorieDtoAssembler;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDtoAssembler;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.finanz.service.UvaartDtoAssembler;
import com.lp.server.finanz.service.UvaartsprDto;
import com.lp.server.finanz.service.UvaartsprDtoAssembler;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.finanz.service.UvaverprobungDtoAssembler;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDtoAssembler;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungzahlung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.rechnung.service.RechnungzahlungDto;
import com.lp.server.system.ejb.Laenderart;
import com.lp.server.system.ejb.Laenderartspr;
import com.lp.server.system.ejb.LaenderartsprPK;
import com.lp.server.system.ejb.Land;
import com.lp.server.system.ejb.Mandant;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FinanzServiceFacBean extends Facade implements FinanzServiceFac {
	@PersistenceContext
	private EntityManager em;

	public KontotypsprDto kontotypsprFindByPrimaryKey(String kontotypCNr,
			String localeCNr) throws EJBExceptionLP {
		try {
			KontotypsprPK kontotypsprPK = new KontotypsprPK();
			kontotypsprPK.setKontotypCNr(kontotypCNr);
			kontotypsprPK.setLocaleCNr(localeCNr);
			Kontotypspr kontotypspr = em.find(Kontotypspr.class, kontotypsprPK);
			if (kontotypspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKontotypsprDto(kontotypspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public Integer createMahnspesen(MahnspesenDto dto) {

		try {
			Query query = em
					.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
			query.setParameter(1, dto.getIMahnstufe());
			query.setParameter(2, dto.getMandantCNr());
			query.setParameter(3, dto.getWaehrungCNr());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FB_MAHNSPESEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MAHNSPESEN);
			dto.setIId(pk);

			Mahnspesen bean = new Mahnspesen(dto.getIId(), dto.getMandantCNr(),
					dto.getIMahnstufe(), dto.getWaehrungCNr(),
					dto.getNMahnspesen());
			em.persist(bean);
			em.flush();
			setMahnspesenFromMahnspesenDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMahnspesen(MahnspesenDto dto) {
		Mahnspesen toRemove = em.find(Mahnspesen.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public MahnspesenDto mahnspesenFindByPrimaryKey(Integer iId) {
		Mahnspesen ialle = em.find(Mahnspesen.class, iId);
		return MahnspesenDtoAssembler.createDto(ialle);
	}

	public void updateMahnspesen(MahnspesenDto dto) {
		Mahnspesen ialle = em.find(Mahnspesen.class, dto.getIId());

		try {
			Query query = em
					.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
			query.setParameter(1, dto.getIMahnstufe());
			query.setParameter(2, dto.getMandantCNr());
			query.setParameter(3, dto.getWaehrungCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Mahnspesen) query.getSingleResult())
					.getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"FB_MAHNSPESEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setMahnspesenFromMahnspesenDto(ialle, dto);
	}

	private KontotypsprDto assembleKontotypsprDto(Kontotypspr kontotypspr) {
		return KontotypsprDtoAssembler.createDto(kontotypspr);
	}

	public KontotypDto kontotypFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		Kontotyp kontotyp = em.find(Kontotyp.class, cNr);
		if (kontotyp == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleKontotypDto(kontotyp);
	}

	private void setKontotypFromKontotypDto(Kontotyp kontotyp,
			KontotypDto kontotypDto) {
		em.merge(kontotyp);
		em.flush();
	}

	private KontotypDto assembleKontotypDto(Kontotyp kontotyp) {
		return KontotypDtoAssembler.createDto(kontotyp);
	}

	// public UvaartDto uvaartFindByPrimaryKey(Integer iId,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// Uvaart uvaart = em.find(Uvaart.class, iId);
	// if (uvaart == null) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	// }
	// UvaartDto uvaartDto = assembleUvaartDto(uvaart);
	//
	// uvaartDto.setUvaartsprDto(new UvaartsprDto());
	// uvaartDto.getUvaartsprDto().setUvaartIId(iId);
	// uvaartDto.getUvaartsprDto().setLocaleCNr(
	// theClientDto.getLocUiAsString());
	// uvaartDto.getUvaartsprDto().setCBez(
	// uebersetzeUvaart(iId, theClientDto.getLocUi()));
	// // uvaartDto.getUvaartsprDto().setCBez(getUvaartSprachBezeichnung(iId,
	// theClientDto.getLocUi()));
	// if (uvaartDto.getUvaartsprDto().getCBez() == null){
	// uvaartDto.getUvaartsprDto().setCBez(uvaart.getCNr());
	// }
	// return uvaartDto;
	// }

	// *** wp ***
	// ***

	public UvaartDto uvaartFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Uvaart uvaart = em.find(Uvaart.class, iId);

		if (uvaart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		UvaartDto uvaartDto = assembleUvaartDto(uvaart);

		UvaartsprDto sprDto = getUvaartsprDto(iId, theClientDto.getLocUi(),
				theClientDto.getLocMandant());

		uvaartDto.setUvaartsprDto(sprDto);

		return uvaartDto;
	}

	// ***
	// *** wp ***

	private void setUvaartFromUvaartDto(Uvaart uvaart, UvaartDto uvaartDto) {
		uvaart.setCNr(uvaartDto.getCNr());
		uvaart.setMandantCNr(uvaartDto.getMandantCNr());
		uvaart.setCKennzeichen(uvaartDto.getCKennzeichen());
		uvaart.setISort(uvaartDto.getISort());
		uvaart.setBInvertiert(uvaartDto.getBInvertiert());
		em.merge(uvaart);
		em.flush();
	}

	private UvaartDto assembleUvaartDto(Uvaart uvaart) {
		return UvaartDtoAssembler.createDto(uvaart);
	}

	private UvaartDto[] assembleUvaartDtos(Collection<?> uvaarts) {
		List<UvaartDto> list = new ArrayList<UvaartDto>();
		if (uvaarts != null) {
			Iterator<?> iterator = uvaarts.iterator();
			while (iterator.hasNext()) {
				Uvaart uvaart = (Uvaart) iterator.next();
				list.add(assembleUvaartDto(uvaart));
			}
		}
		UvaartDto[] returnArray = new UvaartDto[list.size()];
		return (UvaartDto[]) list.toArray(returnArray);
	}

	public UvaartsprDto uvaartsprFindByPrimaryKey() throws EJBExceptionLP {
		try {
			UvaartsprPK uvaartsprPK = new UvaartsprPK();
			Uvaartspr uvaartspr = em.find(Uvaartspr.class, uvaartsprPK);
			if (uvaartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleUvaartsprDto(uvaartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void setUvaartsprFromUvaartsprDto(Uvaartspr uvaartspr,
			UvaartsprDto uvaartsprDto) {
		uvaartspr.setCBez(uvaartsprDto.getCBez());
		em.merge(uvaartspr);
		em.flush();
	}

	private UvaartsprDto assembleUvaartsprDto(Uvaartspr uvaartspr) {
		return UvaartsprDtoAssembler.createDto(uvaartspr);
	}

	public KontoartsprDto kontoartsprFindByPrimaryKey() throws EJBExceptionLP {
		try {
			KontoartsprPK kontoartsprPK = new KontoartsprPK();
			Kontoartspr kontoartspr = em.find(Kontoartspr.class, kontoartsprPK);
			if (kontoartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKontoartsprDto(kontoartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void setSteuerkategorieFromSteuerkategorieDto(
			Steuerkategorie steuerkategorie,
			SteuerkategorieDto steuerkategorieDto) {
		steuerkategorie.setISort(steuerkategorieDto.getISort());
		steuerkategorie.setCNr(steuerkategorieDto.getCNr());
		steuerkategorie.setCBez(steuerkategorieDto.getCBez());
		steuerkategorie.setbReversecharge(steuerkategorieDto
				.getBReversecharge());
		steuerkategorie.setISort(steuerkategorieDto.getISort());
		steuerkategorie.setKontoIIdForderungen(steuerkategorieDto
				.getKontoIIdForderungen());
		steuerkategorie.setKontoIIdVerbindlichkeiten(steuerkategorieDto
				.getKontoIIdVerbindlichkeiten());
		steuerkategorie.setKontoIIdKursgewinn(steuerkategorieDto
				.getKontoIIdKursgewinn());
		steuerkategorie.setKontoIIdKursverlust(steuerkategorieDto
				.getKontoIIdKursverlust());
		em.merge(steuerkategorie);
		em.flush();
	}

	private void setSteuerkategoriekontoFromSteuerkategoriekontoDto(
			Steuerkategoriekonto steuerkategoriekonto,
			SteuerkategoriekontoDto steuerkategoriekontoDto) {
		// steuerkategoriekonto.setSteuerkategorieIId(steuerkategoriekontoDto.getSteuerkategorieIId());
		// steuerkategoriekonto.setMwstsatzbezIId(steuerkategoriekontoDto.getMwstsatzbezIId());
		steuerkategoriekonto.setKontoIIdVk(steuerkategoriekontoDto
				.getKontoIIdVk());
		steuerkategoriekonto.setKontoIIdEk(steuerkategoriekontoDto
				.getKontoIIdEk());
		steuerkategoriekonto.setKontoIIdSkontoVk(steuerkategoriekontoDto
				.getKontoIIdSkontoVk());
		steuerkategoriekonto.setKontoIIdSkontoEk(steuerkategoriekontoDto
				.getKontoIIdSkontoEk());
		steuerkategoriekonto.setKontoIIdEinfuhrUst(steuerkategoriekontoDto
				.getKontoIIdEinfuhrUst());
		em.merge(steuerkategoriekonto);
		em.flush();
	}

	private void setKontoartsprFromKontoartsprDto(Kontoartspr kontoartspr,
			KontoartsprDto kontoartsprDto) {
		kontoartspr.setCBez(kontoartsprDto.getCBez());
		em.merge(kontoartspr);
		em.flush();
	}

	private KontoartsprDto assembleKontoartsprDto(Kontoartspr kontoartspr) {
		return KontoartsprDtoAssembler.createDto(kontoartspr);
	}

	// public KontoartDto kontoartFindByPrimaryKey(String cNr,
	// TheClientDto theClientDto) throws EJBExceptionLP {
	// // try {
	// Kontoart kontoart = em.find(Kontoart.class, cNr);
	// if (kontoart == null) {
	// throw new EJBExceptionLP(
	// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
	// }
	// KontoartDto kontoartDto = assembleKontoartDto(kontoart);
	//
	// kontoartDto.setKontoartsprDto(new KontoartsprDto());
	// kontoartDto.getKontoartsprDto().setKontoartCNr(cNr);
	// kontoartDto.getKontoartsprDto().setLocaleCNr(
	// theClientDto.getLocUiAsString());
	// kontoartDto.getKontoartsprDto().setCBez(
	// uebersetzeKontoart(cNr, theClientDto.getLocUi()));
	// //
	// kontoartDto.getKontoartsprDto().setCBez(getKontoartSprachBezeichnung(cNr,
	// theClientDto.getLocUi()));
	// return kontoartDto;
	// // }
	// // catch (FinderException e) {
	// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	// // e);
	// // }
	// }

	// *** wp ***
	// ***

	public KontoartDto kontoartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {

		Kontoart kontoart = em.find(Kontoart.class, cNr);

		if (kontoart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		KontoartDto kontoartDto = assembleKontoartDto(kontoart);

		KontoartsprDto sprDto = getKontoartSprDto(cNr, theClientDto.getLocUi(),
				theClientDto.getLocMandant());

		kontoartDto.setKontoartsprDto(sprDto);

		return kontoartDto;
	}

	// ***
	// *** wp ***

	public KontoartDto[] kontoartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("KontoartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		return assembleKontoartDtos(cl);
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	private void setKontoartFromKontoartDto(Kontoart kontoart,
			KontoartDto kontoartDto) {
		kontoart.setISort(kontoartDto.getISort());
		em.merge(kontoart);
		em.flush();
	}

	private KontoartDto assembleKontoartDto(Kontoart kontoart) {
		return KontoartDtoAssembler.createDto(kontoart);
	}

	private KontoartDto[] assembleKontoartDtos(Collection<?> kontoarts) {
		List<KontoartDto> list = new ArrayList<KontoartDto>();
		if (kontoarts != null) {
			Iterator<?> iterator = kontoarts.iterator();
			while (iterator.hasNext()) {
				Kontoart kontoart = (Kontoart) iterator.next();
				list.add(assembleKontoartDto(kontoart));
			}
		}
		KontoartDto[] returnArray = new KontoartDto[list.size()];
		return (KontoartDto[]) list.toArray(returnArray);
	}

	// *** wp ***
	// ***

	// /**
	// * Uebersetzt eine UVA Art optimal. 1.Versuch: mit locale1 2.Versuch: mit
	// * locale2 3.Versuch: cNr
	// *
	// * @param iId der UVA Art
	// * @param locale1
	// * erste zu versuchende Locale
	// * @param locale2
	// * zweite zu versuchende Locale
	// * @return String
	// */
	// public String uebersetzeUvaartOptimal(Integer iId, Locale locale1,
	// Locale locale2) {
	// String tempCnr = uebersetzeUvaart(iId, locale1);
	// if (tempCnr == null) {
	// tempCnr = uebersetzeUvaart(iId, locale2);
	// }
	// return tempCnr;
	// }
	//
	// /**
	// * Uebersetzt eine UVA Art in die Sprache des uebergebenen Locales.
	// *
	// * @param iId der UVA Art
	// * @param locale
	// * zu versuchende Locale
	// * @throws FinderException
	// * @return String
	// */
	// private String uebersetzeUvaart(Integer iId, Locale locale) {
	// String cLocale = Helper.locale2String(locale);
	// // try {
	// Uvaartspr uvaartspr = em.find(Uvaartspr.class, new UvaartsprPK(iId,
	// cLocale));
	// if (uvaartspr == null) {
	// Uvaart uvaart = em.find(Uvaart.class, iId);
	// return uvaart.getCNr();
	// }
	// return uvaartspr.getCBez();
	// }

	public String uebersetzeUvaartOptimal(Integer iId, Locale localeUi,
			Locale localeMandant) {
		Validator.notNull(localeUi, "localeUi");
		Validator.notNull(localeMandant, "localeMandant");

		UvaartsprDto uvaartsprDto = getUvaartsprDto(iId, localeUi,
				localeMandant);
		return uvaartsprDto.getCBez();
	}

	private UvaartsprDto getUvaartsprDto(Integer iId, Locale localeUi,
			Locale localeMandant) {

		// Uvaartspr uvaartspr = em.find(Uvaartspr.class, new UvaartsprPK(iId,
		// Helper.locale2String(localeUi)));

		Uvaartspr uvaartspr = getUvaartspr(iId, localeUi);

		if (uvaartspr == null) {
			uvaartspr = getUvaartspr(iId, localeMandant);
		}
		if (uvaartspr == null) {
			uvaartspr = new Uvaartspr(iId, Helper.locale2String(localeUi));
			Uvaart uvaart = em.find(Uvaart.class, iId);
			uvaartspr.setCBez(uvaart.getCNr());
		}

		return assembleUvaartsprDto(uvaartspr);

	}

	private Uvaartspr getUvaartspr(Integer iId, Locale locale) {
		Uvaartspr uvaartspr = em.find(Uvaartspr.class, new UvaartsprPK(iId,
				Helper.locale2String(locale)));
		return uvaartspr;
	}

	{
		// /**
		// * Uebersetzt eine Kontoart optimal. 1.Versuch: mit locale1 2.Versuch:
		// mit
		// * locale2 3.Versuch: cNr
		// *
		// * @param cNr
		// * String
		// * @param locale1
		// * Locale
		// * @param locale2
		// * Locale
		// * @return String
		// */
		// public String uebersetzeKontoartOptimal(String cNr, Locale locale1,
		// Locale locale2) {
		//
		// String tempCnr = uebersetzeKontoart(cNr, locale1);
		// if (tempCnr == null || tempCnr.equals(cNr)) {
		// tempCnr = uebersetzeKontoart(cNr, locale2);
		// }
		// return tempCnr == null ? cNr : tempCnr;
		// }

		// /**
		// * Uebersetzt eine Kontoart in die Sprache des uebergebenen Locales.
		// *
		// * @param cNr
		// * String
		// * @param locale
		// * Locale
		// * @throws FinderException
		// * @return String
		// */
		// private String uebersetzeKontoart(String cNr, Locale locale) {
		// String cLocale = Helper.locale2String(locale);
		// // try {
		// Kontoartspr kontoartspr = em.find(Kontoartspr.class, new
		// KontoartsprPK(
		// cNr, cLocale));
		// if (kontoartspr == null) {
		// return cNr;
		// }
		// return kontoartspr.getCBez();
		//
		// // }
		// // catch (FinderException ex1) {
		// // return cNr;
		// // }
		// }

		// private String uebersetzeKontoart(String cNr, Locale locale) {
		//
		// Kontoartspr kontoartspr = em.find(Kontoartspr.class, new
		// KontoartsprPK(cNr, Helper.locale2String(locale)));
		// return kontoartspr == null ? cNr : kontoartspr.getCBez();
		//
		// }
	}

	public String uebersetzeKontoartOptimal(String cNr, Locale localeUi,
			Locale localeMandant) {
		Validator.notNull(localeUi, "localeUi");
		Validator.notNull(localeMandant, "localeMandant");

		KontoartsprDto kontoartsprDto = getKontoartSprDto(cNr, localeUi,
				localeMandant);
		return kontoartsprDto.getCBez();
	}

	private KontoartsprDto getKontoartSprDto(String cNr, Locale localeUi,
			Locale localeMandant) {

		// Kontoartspr kontoartspr = em.find(Kontoartspr.class, new
		// KontoartsprPK(cNr, Helper.locale2String(localeUi)));
		Kontoartspr kontoartspr = getKontoartspr(cNr, localeUi);

		if (kontoartspr == null) {
			// kontoartspr = em.find(Kontoartspr.class, new KontoartsprPK(cNr,
			// Helper.locale2String(localeMandant)));
			kontoartspr = getKontoartspr(cNr, localeMandant);
		}
		if (kontoartspr == null) {
			kontoartspr = new Kontoartspr(cNr, Helper.locale2String(localeUi));
			kontoartspr.setCBez(cNr);
		}

		return assembleKontoartsprDto(kontoartspr);

	}

	private Kontoartspr getKontoartspr(String cNr, Locale locale) {
		Kontoartspr kontoartspr = em.find(Kontoartspr.class, new KontoartsprPK(
				cNr, Helper.locale2String(locale)));
		return kontoartspr;
	}

	// ***
	// *** wp ***

	/**
	 * Uebersetzt einen Kontotyp optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzeKontotypOptimal(String cNr, Locale locale1,
			Locale locale2) {
		String tempCnr = uebersetzeKontotyp(cNr, locale1);
		if (tempCnr == null) {
			tempCnr = uebersetzeKontotyp(cNr, locale2);
		}
		if (tempCnr == null) {
			return cNr;
		} else {
			return tempCnr;
		}
	}

	/**
	 * Uebersetzt einen Kontotyp in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeKontotyp(String cNr, Locale locale) {
		String cLocale = Helper.locale2String(locale);
		Kontotypspr kontotypspr = em.find(Kontotypspr.class, new KontotypsprPK(
				cNr, cLocale));
		if (kontotypspr == null) {
			return null;
		}
		return kontotypspr.getCBez();
	}

	public void createKontoart(KontoartDto kontoartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// super.check(theClientDto);
		myLogger.logData(kontoartDto);
		if (kontoartDto == null) {
			return;
		}
		try {
			Kontoart kontoart = new Kontoart(kontoartDto.getCNr());
			em.persist(kontoart);
			em.flush();
			setKontoartFromKontoartDto(kontoart, kontoartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createSteuerkategorie(SteuerkategorieDto steuerkategorieDto,
			TheClientDto theClientDto) {
		if (steuerkategorieDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("steuerkategorieDto == null"));
		}
		if (steuerkategorieDto.getCNr() == null
				|| steuerkategorieDto.getBReversecharge() == null
				|| steuerkategorieDto.getCBez() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"steuerkategorieDto.getCNr() == null || steuerkategorieDto.getBReversecharge() == null || steuerkategorieDto.getCBez() == null"));
		}

		steuerkategorieDto.setMandantCNr(theClientDto.getMandant());

		try {

			// Query query =
			// em.createNamedQuery("SteuerkategorieByCNrMandantCNr");
			Query query = em
					.createNamedQuery("SteuerkategorieByCNrFinanzamtIIDMandant");
			query.setParameter(1, steuerkategorieDto.getCNr());
			query.setParameter(2, steuerkategorieDto.getFinanzamtIId());
			query.setParameter(3, theClientDto.getMandant());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FB_STEUERKATEGORIE.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STEUERKATEGORIE);
			steuerkategorieDto.setIId(pk);

			Query queryNext = em
					.createNamedQuery("SteuerkategorieejbSelectNextReihung");
			queryNext.setParameter(1, theClientDto.getMandant());

			Integer i = (Integer) queryNext.getSingleResult();
			i = i == null ? 0 : new Integer(i + 1);

			steuerkategorieDto.setISort(i);
			Steuerkategorie artgru = new Steuerkategorie(
					steuerkategorieDto.getIId(), steuerkategorieDto.getCNr(),
					steuerkategorieDto.getBReversecharge(),
					steuerkategorieDto.getMandantCNr(),
					steuerkategorieDto.getCBez(),
					steuerkategorieDto.getFinanzamtIId());
			artgru.setISort(steuerkategorieDto.getISort());
			em.persist(artgru);
			em.flush();
			setSteuerkategorieFromSteuerkategorieDto(artgru, steuerkategorieDto);

			return steuerkategorieDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeSteuerkategorie(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtgru. Es gibt keine Artgru mit der iid "
							+ iId);
		}
		em.remove(steuerkategorie);
		em.flush();

	}

	public SteuerkategorieDto steuerkategorieFindByPrimaryKey(Integer iId,
			TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}
		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei steuerkategorie FindByPrimaryKey. Es gibt keine steuerkategorie mit iid "
							+ iId);
		}
		SteuerkategorieDto steuerkategorieDto = assembleBuchungsartDto(steuerkategorie);
		return steuerkategorieDto;
	}

	public void updateSteuerkategorie(SteuerkategorieDto steuerkategorieDto,
			TheClientDto theClientDto) {
		if (steuerkategorieDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("steuerkategorieDto == null"));
		}
		if (steuerkategorieDto.getIId() == null
				|| steuerkategorieDto.getCNr() == null
				|| steuerkategorieDto.getBReversecharge() == null
				|| steuerkategorieDto.getCBez() == null
				|| steuerkategorieDto.getFinanzamtIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"steuerkategorieDto.getiId() == null || steuerkategorieDto.getCNr() == null || steuerkategorieDto.getBReversecharge() == null || steuerkategorieDto.getCBez() == null || steuerkategorieDto.getFinanzamtIId() == null"));
		}

		Integer iId = steuerkategorieDto.getIId();
		Steuerkategorie steuerkategorie = null;
		// try {
		steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSteuerkategorie. Es gibt keine Steuerkategorie mit iid "
							+ iId);

		}

		// duplicateunique: Pruefung: Steuerkategorie bereits vorhanden.
		try {
			Query query = em
					.createNamedQuery("SteuerkategorieByCNrFinanzamtIIDMandant");
			query.setParameter(1, steuerkategorieDto.getCNr());
			query.setParameter(2, steuerkategorieDto.getFinanzamtIId());
			query.setParameter(3, theClientDto.getMandant());
			Steuerkategorie doppelt = (Steuerkategorie) query.getSingleResult();
			if (doppelt.getIId().compareTo(iId) != 0)
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"FB_STEUERKATEGORIE.UK"));
		} catch (NoResultException ex) {
			//
		}

		setSteuerkategorieFromSteuerkategorieDto(steuerkategorie,
				steuerkategorieDto);

	}

	public Integer createSteuerkategoriekonto(
			SteuerkategoriekontoDto steuerkategoriekontoDto,
			TheClientDto theClientDto) {
		if (steuerkategoriekontoDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("steuerkategoriekontoDto == null"));
		}
		if (steuerkategoriekontoDto.getSteuerkategorieIId() == null
				|| steuerkategoriekontoDto.getMwstsatzbezIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"steuerkategoriekontoDto.getSteuerkategorieIId() == null || steuerkategorieDto.getMwstsatzbezIId() == null"));
		}

		// steuerkategoriekontoDto.setMandantCNr(theClientDto.getMandant());

		try {

			Query query = em
					.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIIdandMwStSatzBeziid");
			query.setParameter(1,
					steuerkategoriekontoDto.getSteuerkategorieIId());
			query.setParameter(2, steuerkategoriekontoDto.getMwstsatzbezIId());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FB_STEUERKATEGORIEKONTO.UK"));
		} catch (NoResultException ex) {

		}
		try {
			Steuerkategoriekonto steuerkategoriekonto = new Steuerkategoriekonto(
					steuerkategoriekontoDto.getSteuerkategorieIId(),
					steuerkategoriekontoDto.getMwstsatzbezIId());

			em.persist(steuerkategoriekonto);
			em.flush();
			setSteuerkategoriekontoFromSteuerkategoriekontoDto(
					steuerkategoriekonto, steuerkategoriekontoDto);

			return steuerkategoriekontoDto.getSteuerkategorieIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeSteuerkategoriekonto(Integer steuerkategorieiId) {
		if (steuerkategorieiId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("steuerkategorieiId == null"));
		}

		Steuerkategoriekonto steuerkategoriekonto = em.find(
				Steuerkategoriekonto.class, steuerkategorieiId);
		if (steuerkategoriekonto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeSteuerkategoriekonto. Es gibt keine Steuerkategoriekonto mit der steuerkategorieiId "
							+ steuerkategorieiId);
		}
		em.remove(steuerkategoriekonto);
		em.flush();

	}

	public SteuerkategoriekontoDto steuerkategoriekontoFindByPrimaryKey(
			Integer steuerkategorieIId, Integer mwstsatzbezIId) {
		if (steuerkategorieIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("steuerkategorieiId == null"));
		}
		if (mwstsatzbezIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("mwstsatzbezIId == null"));
		}

		SteuerkategoriekontoPK pk = new SteuerkategoriekontoPK(
				steuerkategorieIId, mwstsatzbezIId);

		Steuerkategoriekonto steuerkategoriekonto = em.find(
				Steuerkategoriekonto.class, pk);
		if (steuerkategoriekonto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei steuerkategorie FindByPrimaryKey. Es gibt keine steuerkategorie mit iid "
							+ steuerkategorieIId
							+ " und mwstsatzbezIId "
							+ mwstsatzbezIId + ".");
		}
		SteuerkategoriekontoDto steuerkategoriekontoDto = assembleBuchungsartkontoDto(steuerkategoriekonto);
		return steuerkategoriekontoDto;
	}

	public SteuerkategoriekontoDto[] steuerkategoriekontoFindAll(
			Integer steuerkategorieIId) throws EJBExceptionLP {

		SteuerkategoriekontoDto[] steuerkategoriekontoDtoO = null;

		Query query = em
				.createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		query.setParameter(1, steuerkategorieIId);
		// Object test = query.getSingleResult();
		Collection<?> cl = query.getResultList();
		steuerkategoriekontoDtoO = assembleSteuerkategoriekontoDtos(cl);
		return steuerkategoriekontoDtoO;
	}

	private SteuerkategoriekontoDto[] assembleSteuerkategoriekontoDtos(
			Collection<?> steuerkategoriekontos) {
		return SteuerkategoriekontoDtoAssembler
				.createDtos(steuerkategoriekontos);
	}

	public void updateSteuerkategoriekonto(
			SteuerkategoriekontoDto steuerkategoriekontoDto,
			TheClientDto theClientDto) {
		if (steuerkategoriekontoDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("steuerkategoriekontoDto == null"));
		}
		if (steuerkategoriekontoDto.getSteuerkategorieIId() == null
				|| steuerkategoriekontoDto.getMwstsatzbezIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"steuerkategoriekontoDto.getSteuerkategorieIId() == null || steuerkategorieDto.getMwstsatzbezIId() == null"));
		}

		SteuerkategoriekontoPK steuerkategoriekontoPK = new SteuerkategoriekontoPK(
				steuerkategoriekontoDto.getSteuerkategorieIId(),
				steuerkategoriekontoDto.getMwstsatzbezIId());
		Steuerkategoriekonto steuerkategoriekonto = null;
		steuerkategoriekonto = em.find(Steuerkategoriekonto.class,
				steuerkategoriekontoPK);
		if (steuerkategoriekonto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSteuerkategoriekonto. Es gibt keine Steuerkategoriekontodefinition mit steuerkategorieiid "
							+ steuerkategoriekontoDto.getSteuerkategorieIId()
							+ " und mwstsatzbeziid "
							+ steuerkategoriekontoDto.getMwstsatzbezIId());
		}
		try {
			Map<Integer, String> mwstAll = getMandantFac()
					.mwstsatzbezFindAllByMandant(theClientDto.getMandant(),
							theClientDto);
			for (Integer mwstIid : mwstAll.keySet()) {
				if (mwstIid.intValue() == steuerkategoriekontoDto
						.getMwstsatzbezIId())
					continue;
				HvTypedQuery<Steuerkategoriekonto> query;
				if (steuerkategoriekontoDto.getKontoIIdEk() != null) {
					query = new HvTypedQuery<Steuerkategoriekonto>(
							em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
					query.setParameter(1,
							steuerkategoriekontoDto.getKontoIIdEk());
					query.setParameter(2, mwstIid);
					List<Steuerkategoriekonto> result = query.getResultList();
					if (result.size() > 0) {
						throwExceptionFinanzKontoInAndererMwstVerwendet(
								steuerkategoriekontoDto,
								SteuerkategoriekontoDtoAssembler
										.createDto(result.get(0)),
								steuerkategoriekontoDto.getKontoIIdEk(),
								theClientDto);
					}
				}
				if (steuerkategoriekontoDto.getKontoIIdVk() != null) {
					query = new HvTypedQuery<Steuerkategoriekonto>(
							em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
					query.setParameter(1,
							steuerkategoriekontoDto.getKontoIIdVk());
					query.setParameter(2, mwstIid);
					List<Steuerkategoriekonto> result = query.getResultList();
					if (result.size() > 0) {
						throwExceptionFinanzKontoInAndererMwstVerwendet(
								steuerkategoriekontoDto,
								SteuerkategoriekontoDtoAssembler
										.createDto(result.get(0)),
								steuerkategoriekontoDto.getKontoIIdVk(),
								theClientDto);
					}
				}
			}
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		setSteuerkategoriekontoFromSteuerkategoriekontoDto(
				steuerkategoriekonto, steuerkategoriekontoDto);
	}

	private void throwExceptionFinanzKontoInAndererMwstVerwendet(
			SteuerkategoriekontoDto kat1, SteuerkategoriekontoDto kat2,
			Integer kontoIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		SteuerkategorieDto stk1 = steuerkategorieFindByPrimaryKey(
				kat1.getSteuerkategorieIId(), theClientDto);
		SteuerkategorieDto stk2 = steuerkategorieFindByPrimaryKey(
				kat2.getSteuerkategorieIId(), theClientDto);
		MwstsatzbezDto mwst1 = getMandantFac().mwstsatzbezFindByPrimaryKey(
				kat1.getMwstsatzbezIId(), theClientDto);
		MwstsatzbezDto mwst2 = getMandantFac().mwstsatzbezFindByPrimaryKey(
				kat2.getMwstsatzbezIId(), theClientDto);
		KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);

		throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET,
				"FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET", konto.getCNr()
						+ ", " + konto.getCBez(), stk1.getCBez(),
				mwst1.getCBezeichnung(), stk2.getCBez(),
				mwst2.getCBezeichnung());
	}

	public void createKontotyp(KontotypDto kontotypDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// super.check(theClientDto);
		myLogger.logData(kontotypDto);
		if (kontotypDto == null) {
			return;
		}
		try {
			Kontotyp kontotyp = new Kontotyp(kontotypDto.getCNr());
			em.persist(kontotyp);
			em.flush();
			setKontotypFromKontotypDto(kontotyp, kontotypDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void createUvaart(UvaartDto uvaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// super.check(theClientDto);
		myLogger.logData(uvaartDto);
		if (uvaartDto == null) {
			return;
		}

		try {
			Uvaart uvaart = new Uvaart(uvaartDto.getCNr());
			if (uvaartDto.getIId() == null) {
				Integer iid = getPKGeneratorObj().getNextPrimaryKey(
						PKConst.PK_UVAART);
				uvaartDto.setIId(iid);
				uvaart.setIId(iid);
			}
			setUvaartFromUvaartDto(uvaart, uvaartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahntextDto);
		if (mahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mahntextDto == null"));
		}
		mahntextDto.setIId(getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_MAHNTEXT));
		mahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		mahntextDto.setMandantCNr(theClientDto.getMandant());
		// wenns dazu noch keine mahnstufe gibt muss ich die kreieren
		try {
			MahnstufeDto mahnstufeDto = getMahnwesenFac()
					.mahnstufeFindByPrimaryKey(
							new MahnstufePK(mahntextDto.getMahnstufeIId(),
									mahntextDto.getMandantCNr()));
			if (mahnstufeDto == null) {
				mahnstufeDto = new MahnstufeDto();
				mahnstufeDto.setIId(mahntextDto.getMahnstufeIId());
				mahnstufeDto.setITage(new Integer(10));
				getMahnwesenFac().createMahnstufe(mahnstufeDto, theClientDto);
			}
			Mahntext mahntext = new Mahntext(mahntextDto.getIId(),
					mahntextDto.getMandantCNr(), mahntextDto.getLocaleCNr(),
					mahntextDto.getMahnstufeIId(), mahntextDto.getCTextinhalt());
			setMahntextFromMahntextDto(mahntext, mahntextDto);
			return mahntextDto.getIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahntextDto);
		if (mahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mahntextDto == null"));
		}
		// try {
		if (mahntextDto.getMahnstufeIId().intValue() == 1
				|| mahntextDto.getMahnstufeIId().intValue() == 2
				|| mahntextDto.getMahnstufeIId().intValue() == 3) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					new Exception("Mahnstufe "
							+ mahntextDto.getMahnstufeIId().intValue()
							+ " darf nicht geloescht werden"));
		}
		Integer iId = mahntextDto.getIId();
		Mahntext toRemove = em.find(Mahntext.class, iId);
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

	public void updateMahntext(MahntextDto mahntextDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (mahntextDto != null) {
			Integer iId = mahntextDto.getIId();
			try {
				Mahntext mahntext = em.find(Mahntext.class, iId);
				setMahntextFromMahntextDto(mahntext, mahntextDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public MahntextDto mahntextFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		try {
			Mahntext mahntext = em.find(Mahntext.class, iId);
			if (mahntext == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleMahntextDto(mahntext);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setMahntextFromMahntextDto(Mahntext mahntext,
			MahntextDto mahntextDto) {
		mahntext.setMandantCNr(mahntextDto.getMandantCNr());
		mahntext.setLocaleCNr(mahntextDto.getLocaleCNr());
		mahntext.setMahnstufeIId(mahntextDto.getMahnstufeIId());
		mahntext.setXTextinhalt(mahntextDto.getCTextinhalt());
		em.merge(mahntext);
		em.flush();
	}

	private void setMahnspesenFromMahnspesenDto(Mahnspesen mahnspesen,
			MahnspesenDto mahnspesenDto) {
		mahnspesen.setMandantCNr(mahnspesenDto.getMandantCNr());
		mahnspesen.setIMahnstufe(mahnspesenDto.getIMahnstufe());
		mahnspesen.setWaehrungCNr(mahnspesenDto.getWaehrungCNr());
		mahnspesen.setNMahnspesen(mahnspesenDto.getNMahnspesen());
		em.merge(mahnspesen);
		em.flush();
	}

	private MahntextDto assembleMahntextDto(Mahntext mahntext) {
		return MahntextDtoAssembler.createDto(mahntext);
	}

	public MahntextDto createDefaultMahntext(Integer mahnstufeIId,
			String sTextinhaltI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		MahntextDto oMahntextDto = new MahntextDto();
		oMahntextDto.setMahnstufeIId(mahnstufeIId);
		oMahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		oMahntextDto.setMandantCNr(theClientDto.getMandant());
		oMahntextDto.setCTextinhalt(sTextinhaltI);
		oMahntextDto.setIId(createMahntext(oMahntextDto, theClientDto));
		return oMahntextDto;
	}

	public MahntextDto mahntextFindByMandantLocaleCNr(String pMandant,
			String pSprache, Integer mahnstufeIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("MahntextfindByMandantLocaleCNr");
			query.setParameter(1, pMandant);
			query.setParameter(2, pSprache);
			query.setParameter(3, mahnstufeIId);
			Mahntext mahntext = (Mahntext) query.getSingleResult();
			return assembleMahntextDto(mahntext);
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
	}

	public void updateKontoart(KontoartDto kontoartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(kontoartDto);
		// code begin
		if (kontoartDto != null) {
			String cNr = kontoartDto.getCNr();
			try {
				Kontoart kontoart = em.find(Kontoart.class, cNr);
				setKontoartFromKontoartDto(kontoart, kontoartDto);
				// sprache
				if (kontoartDto.getKontoartsprDto() != null) {
					updateKontoartspr(kontoartDto.getKontoartsprDto(),
							theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateUvaart(UvaartDto uvaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(uvaartDto);
		// code begin
		if (uvaartDto != null) {
			Integer iId = uvaartDto.getIId();
			try {
				Uvaart uvaart = em.find(Uvaart.class, iId);
				setUvaartFromUvaartDto(uvaart, uvaartDto);
				// sprache
				if (uvaartDto.getUvaartsprDto() != null) {
					updateUvaartspr(uvaartDto.getUvaartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void updateUvaartspr(UvaartsprDto uvaartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(uvaartsprDto);
		// code begin
		if (uvaartsprDto != null) {
			Integer iId = uvaartsprDto.getUvaartIId();
			String localeCNr = uvaartsprDto.getLocaleCNr();
			UvaartsprPK pk = new UvaartsprPK(iId, localeCNr);
			Uvaartspr uvaartspr = em.find(Uvaartspr.class, pk);
			if (uvaartspr == null) {
				// diese Uebersetzung gibt es nocht nicht
				uvaartspr = createUvaartspr(uvaartsprDto, theClientDto);
			}
			setUvaartsprFromUvaartsprDto(uvaartspr, uvaartsprDto);
		}
	}

	private void updateKontoartspr(KontoartsprDto kontoartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(kontoartsprDto);
		// code begin
		if (kontoartsprDto != null) {
			String cNr = kontoartsprDto.getKontoartCNr();
			// String localeCNr = kontoartsprDto.getLocaleCNr();
			String localeCNr = Helper.locale2String(theClientDto.getLocUi());
			// try {
			KontoartsprPK pk = new KontoartsprPK(cNr, localeCNr);
			Kontoartspr kontoartspr = em.find(Kontoartspr.class, pk);
			if (kontoartspr == null) {
				kontoartspr = new Kontoartspr(cNr, localeCNr);
			}
			setKontoartsprFromKontoartsprDto(kontoartspr, kontoartsprDto);
		}
	}

	private Uvaartspr createUvaartspr(UvaartsprDto uvaartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(uvaartsprDto);
		// code begin
		Uvaartspr uvaartspr = null;
		if (uvaartsprDto != null) {
			try {
				uvaartspr = new Uvaartspr(uvaartsprDto.getUvaartIId(),
						uvaartsprDto.getLocaleCNr());
				em.persist(uvaartspr);
				em.flush();
				setUvaartsprFromUvaartsprDto(uvaartspr, uvaartsprDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
			}
		}
		return uvaartspr;
	}

	public void createLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (laenderartDto == null) {
			return;
		}
		try {
			Laenderart laenderart = new Laenderart(laenderartDto.getCNr());
			em.persist(laenderart);
			em.flush();
			setLaenderartFromLaenderartDto(laenderart, laenderartDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (laenderartDto != null) {
			String cNr = laenderartDto.getCNr();
			Laenderart toRemove = em.find(Laenderart.class, cNr);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void updateLaenderart(LaenderartDto laenderartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(laenderartDto);
		// code begin
		if (laenderartDto != null) {
			String cNr = laenderartDto.getCNr();
			try {
				Laenderart laenderart = em.find(Laenderart.class, cNr);
				setLaenderartFromLaenderartDto(laenderart, laenderartDto);
				// sprache
				if (laenderartDto.getLaenderartsprDto() != null) {
					updateLaenderartspr(laenderartDto.getLaenderartsprDto(),
							theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public LaenderartDto laenderartFindByPrimaryKey(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		Laenderart laenderart = em.find(Laenderart.class, cNr);
		if (laenderart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		LaenderartDto laenderartDto = assembleLaenderartDto(laenderart);

		laenderartDto.setLaenderartsprDto(new LaenderartsprDto());
		laenderartDto.getLaenderartsprDto().setLaenderartCNr(cNr);
		laenderartDto.getLaenderartsprDto().setLocaleCNr(
				theClientDto.getLocUiAsString());
		laenderartDto.getLaenderartsprDto().setCBez(
				uebersetzeLaenderart(cNr, theClientDto.getLocUi()));
		return laenderartDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	private void setLaenderartFromLaenderartDto(Laenderart laenderart,
			LaenderartDto laenderartDto) {
		laenderart.setISort(laenderartDto.getISort());
		em.merge(laenderart);
		em.flush();
	}

	private LaenderartDto assembleLaenderartDto(Laenderart laenderart) {
		return LaenderartDtoAssembler.createDto(laenderart);
	}

	public void createLaenderartspr(LaenderartsprDto laenderartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(laenderartsprDto);
		// code begin
		if (laenderartsprDto == null) {
			return;
		}
		try {
			Laenderartspr laenderartspr = new Laenderartspr(
					laenderartsprDto.getLaenderartCNr(),
					laenderartsprDto.getLocaleCNr());
			em.persist(laenderartspr);
			em.flush();
			setLaenderartsprFromLaenderartsprDto(laenderartspr,
					laenderartsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLaenderartspr(LaenderartsprDto laenderartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			if (laenderartsprDto != null) {
				String laenderartCNr = laenderartsprDto.getLaenderartCNr();
				String localeCNr = laenderartsprDto.getLocaleCNr();
				Laenderartspr toRemove = em.find(Laenderartspr.class,
						new LaenderartsprPK(laenderartCNr, localeCNr));
				if (toRemove == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
				}
				try {
					em.remove(toRemove);
					em.flush();
				} catch (EntityExistsException er) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
				}
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateLaenderartspr(LaenderartsprDto laenderartsprDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(laenderartsprDto);
		// code begin
		if (laenderartsprDto != null) {
			String cNr = laenderartsprDto.getLaenderartCNr();
			String localeCNr = laenderartsprDto.getLocaleCNr();
			// try {
			LaenderartsprPK pk = new LaenderartsprPK(cNr, localeCNr);
			Laenderartspr laenderartspr = em.find(Laenderartspr.class, pk);
			if (laenderartspr == null) {
				laenderartspr = new Laenderartspr(cNr, localeCNr);
			}
			setLaenderartsprFromLaenderartsprDto(laenderartspr,
					laenderartsprDto);
			// }
			// catch (FinderException ex) {
			// diese Uebersetzung gibt es nocht nicht
			// createLaenderartspr(laenderartsprDto, theClientDto);
			// }
			// catch (Exception e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			// }
		}
	}

	public LaenderartsprDto laenderartsprFindByPrimaryKey(String laenderartCNr,
			String localeCNr) throws EJBExceptionLP {
		try {
			LaenderartsprPK laenderartsprPK = new LaenderartsprPK();
			laenderartsprPK.setLaenderartCNr(laenderartCNr);
			laenderartsprPK.setLocaleCNr(localeCNr);
			Laenderartspr laenderartspr = em.find(Laenderartspr.class,
					laenderartsprPK);
			if (laenderartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleLaenderartsprDto(laenderartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setLaenderartsprFromLaenderartsprDto(
			Laenderartspr laenderartspr, LaenderartsprDto laenderartsprDto) {
		laenderartspr.setCBez(laenderartsprDto.getCBez());
		em.merge(laenderartspr);
		em.flush();
	}

	private LaenderartsprDto assembleLaenderartsprDto(
			Laenderartspr laenderartspr) {
		return LaenderartsprDtoAssembler.createDto(laenderartspr);
	}

	/**
	 * Uebersetzt eine Laenderart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzeLaenderartOptimal(String cNr, Locale locale1,
			Locale locale2) {
		String tempCnr = uebersetzeLaenderart(cNr, locale1);
		if (tempCnr.equals(cNr)) {
			tempCnr = uebersetzeLaenderart(cNr, locale2);
		}
		return tempCnr;
	}

	/**
	 * Uebersetzt eine Laenderart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeLaenderart(String cNr, Locale locale) {
		String cLocale = Helper.locale2String(locale);
		// try {
		Laenderartspr laenderartspr = em.find(Laenderartspr.class,
				new LaenderartsprPK(cNr, cLocale));
		if (laenderartspr == null) {
			return cNr;
		}
		return laenderartspr.getCBez();

		// }
		// catch (FinderException ex) {
		// return cNr;
		// }
	}

	/**
	 * Finden der Laenderart eines Partners
	 * 
	 * @param partnerIId
	 *            Integer
	 * @param theClientDto
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public String getLaenderartZuPartner(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					partnerIId, theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			return getLaenderartZuPartner(mandantDto, partnerDto, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	/**
	 * Finden der Laenderart eines Partners in Bezug auf den Mandanten.
	 * 
	 * @param mandantDto
	 *            Integer
	 * @param partnerDto
	 *            PartnerDto
	 * @param theClientDto
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public String getLaenderartZuPartner(MandantDto mandantDto,
			PartnerDto partnerDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		return getLaenderartZuPartner(mandantDto.getPartnerDto(), partnerDto,
				theClientDto);
	}

	/**
	 * Finden der Laenderart eines Partners in Bezug auf das Land eines
	 * Basispartners.
	 * 
	 * @param partnerDtoBasis
	 *            Integer
	 * @param partnerDto
	 *            PartnerDto
	 * @param theClientDto
	 *            String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	public String getLaenderartZuPartner(PartnerDto partnerDtoBasis,
			PartnerDto partnerDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		LandDto landDto = null;
		if (!getMandantFac().darfAnwenderAufModulZugreifen(
				LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto)) {
			if (partnerDto.getLandIIdAbweichendesustland() != null) {
				// Land uebersteuert PJ 15304

				landDto = getSystemFac().landFindByPrimaryKey(
						partnerDto.getLandIIdAbweichendesustland());

			}
		}
		if ((landDto != null)
				|| (partnerDto.getLandplzortDto() != null && partnerDto
						.getLandplzortDto().getLandDto() != null)) {
			if (partnerDtoBasis.getLandplzortDto() != null
					&& partnerDtoBasis.getLandplzortDto().getLandDto() != null) {
				String partnerLKZ;
				if (landDto != null) {
					partnerLKZ = landDto.getCLkz();
				} else {
					partnerLKZ = partnerDto.getLandplzortDto().getLandDto()
							.getCLkz();
				}
				String mandantLKZ = partnerDtoBasis.getLandplzortDto()
						.getLandDto().getCLkz();

				String gemeinsamesLKZ = null;
				if (partnerDtoBasis.getLandplzortDto().getLandDto()
						.getLandIIdGemeinsamespostland() != null) {
					// fuer Liechtenstein ist die Schweiz wie INLAND
					Land land = em.find(Land.class, partnerDtoBasis
							.getLandplzortDto().getLandDto()
							.getLandIIdGemeinsamespostland());
					gemeinsamesLKZ = land.getCLkz();
				}
				// Land gleich -> Inland
				if (mandantLKZ.equals(partnerLKZ)
						|| (gemeinsamesLKZ != null && gemeinsamesLKZ
								.equals(partnerLKZ))) {
					return FinanzFac.LAENDERART_INLAND;
				} else {
					LandDto mandantLandDto = partnerDtoBasis.getLandplzortDto()
							.getLandDto();
					LandDto partnerLandDto = null;

					if (landDto != null) {
						partnerLandDto = landDto;
					} else {
						partnerLandDto = partnerDto.getLandplzortDto()
								.getLandDto();
					}

					// wenn ich in der EU bin
					if (mandantLandDto.getEUMitglied() != null
							&& !mandantLandDto.getEUMitglied().after(getDate())) {
						// und der Partner ist auch EU-Mitglied
						if (partnerLandDto.getEUMitglied() != null
								&& !partnerLandDto.getEUMitglied().after(
										getDate())) {
							if (partnerDto.getCUid() != null) {
								return FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID;
							} else {
								return FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID;
							}
						}
						// Partner ist nicht in der EU
						else {
							return FinanzFac.LAENDERART_DRITTLAND;
						}
					}
					// Mandant nicht in der EU und auch nicht Inland
					else {
						return FinanzFac.LAENDERART_DRITTLAND;
					}
				}
			}
			// Mandant hat keine Adresse zugewiesen
			else {
				return null;
			}
		}
		// Partner hat keine Adresse zugewiesen
		else {
			return null;
		}
	}

	public HashMap<String, String> getAllLaenderartenMitUebersetzung(
			Locale locale, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(locale, theClientDto.getIDUser());
		HashMap<String, String> hm = new HashMap<String, String>();
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzLaenderart.class);
			List<?> list = c.list();
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzLaenderart item = (FLRFinanzLaenderart) iter.next();
				hm.put(item.getC_nr(),
						uebersetzeLaenderartOptimal(item.getC_nr(), locale,
								locale));
			}
		} finally {
			closeSession(session);
		}
		return hm;
	}

	/**
	 * Eine Buchungsart anhand PK finden.
	 * 
	 * @param cNr
	 *            String
	 * @throws EJBExceptionLP
	 * @return BuchungsartDto
	 */
	public BuchungsartDto buchungsartFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		// try {
		Buchungsart buchungsart = em.find(Buchungsart.class, cNr);
		if (buchungsart == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBuchungsartDto(buchungsart);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Alle Buchungsarten finden.
	 * 
	 * @throws EJBExceptionLP
	 * @return BuchungsartDto
	 */
	public BuchungsartDto[] buchungsartFindAll() throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("BuchungsartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		return assembleBuchungsartDtos(cl);
		// }
		// catch (FinderException e) {

		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	private SteuerkategorieDto assembleBuchungsartDto(
			Steuerkategorie steuerkategorie) {
		return SteuerkategorieDtoAssembler.createDto(steuerkategorie);
	}

	private SteuerkategoriekontoDto assembleBuchungsartkontoDto(
			Steuerkategoriekonto steuerkategoriekonto) {
		return SteuerkategoriekontoDtoAssembler.createDto(steuerkategoriekonto);
	}

	private BuchungsartDto assembleBuchungsartDto(Buchungsart buchungsart) {
		return BuchungsartDtoAssembler.createDto(buchungsart);
	}

	private BuchungsartDto[] assembleBuchungsartDtos(Collection<?> buchungsarts) {
		List<BuchungsartDto> list = new ArrayList<BuchungsartDto>();
		if (buchungsarts != null) {
			Iterator<?> iterator = buchungsarts.iterator();
			while (iterator.hasNext()) {
				Buchungsart buchungsart = (Buchungsart) iterator.next();
				list.add(assembleBuchungsartDto(buchungsart));
			}
		}
		BuchungsartDto[] returnArray = new BuchungsartDto[list.size()];
		return (BuchungsartDto[]) list.toArray(returnArray);
	}

	public BuchungsartsprDto buchungsartsprFindByPrimaryKey(
			String buchungsartCNr, String spracheCNr) throws EJBExceptionLP {
		try {
			BuchungsartsprPK buchungsartsprPK = new BuchungsartsprPK();
			buchungsartsprPK.setBuchungsartCNr(buchungsartCNr);
			buchungsartsprPK.setLocaleCNr(spracheCNr);
			Buchungsartspr buchungsartspr = em.find(Buchungsartspr.class,
					buchungsartsprPK);
			if (buchungsartspr == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBuchungsartsprDto(buchungsartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private BuchungsartsprDto assembleBuchungsartsprDto(
			Buchungsartspr buchungsartspr) {
		return BuchungsartsprDtoAssembler.createDto(buchungsartspr);
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von
	 * Buchungsarten.
	 * 
	 * @param pArray
	 *            Positionsarten
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return TreeMap
	 * @throws EJBExceptionLP
	 */
	public TreeMap<String, String> uebersetzeBuchungsartOptimal(
			DatenspracheIf[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			tm.put(pArray[i].getCNr(),
					uebersetzeBuchungsartOptimal(pArray[i].getCNr(), locale1,
							locale2));
		}
		return tm;
	}

	/**
	 * Uebersetzt eine Buchungsart optimal. 1.Versuch: mit locale1 2.Versuch:
	 * mit locale2 3.Versuch: cNr
	 * 
	 * @param cNr
	 *            String
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @return String
	 */
	public String uebersetzeBuchungsartOptimal(String cNr, Locale locale1,
			Locale locale2) {
		String uebersetzung = null;
		// try {
		uebersetzung = uebersetzeBuchungsart(cNr, locale1);
		// }
		// catch (FinderException ex) {
		{ // @ToDo FinderException
			// try {
			uebersetzung = uebersetzeBuchungsart(cNr, locale2);
			// }
			// catch (FinderException ex1) {
			{ // @ToDo FinderException
				uebersetzung = cNr;
			}
			// uebersetzung = cNr;
			// }
		}
		// try {
		// uebersetzung = uebersetzeBuchungsart(cNr, locale2);
		// }
		// catch (FinderException ex1) {
		// uebersetzung = cNr;
		// }
		// }
		return uebersetzung;
	}

	/**
	 * Uebersetzt eine Buchungsart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr
	 *            String
	 * @param locale
	 *            Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeBuchungsart(String cNr, Locale locale)
			throws EJBExceptionLP {
		String cLocale = null;
		try {
			cLocale = Helper.locale2String(locale);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
		Buchungsartspr buchungsartspr = em.find(Buchungsartspr.class,
				new BuchungsartsprPK(cNr, cLocale));
		if (buchungsartspr == null) {
			return cNr;
		}
		return buchungsartspr.getCBez();
	}

	/**
	 * Alle Buchungsarten mit Uebersetzung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return TreeMap
	 * @param locale1
	 *            Locale
	 * @param locale2
	 *            Locale
	 * @throws RemoteException
	 */
	public TreeMap getAllBuchungsarten(Locale locale1, Locale locale2,
			String mandantCNr) throws EJBExceptionLP, RemoteException {
		BuchungsartDto[] arten = buchungsartFindAll();
		TreeMap<String, String> maparten = uebersetzeBuchungsartOptimal(arten,
				locale1, locale2);
		ParametermandantDto p = getParameterFac()
				.parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL,
						ParameterFac.KATEGORIE_FINANZ, mandantCNr);
		if (!p.asBoolean())
			maparten.remove(FinanzFac.BUCHUNGSART_MWST_ABSCHLUSS);

		return maparten;
	}

	public UvaartDto[] uvaartFindAll(String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery("UvaartfindAll");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		return assembleUvaartDtos(cl);
	}

	public void createBuchungsart(BuchungsartDto buchungsartDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// super.check(theClientDto);
		if (buchungsartDto == null) {
			return;
		}
		myLogger.logData(buchungsartDto.toString());
		try {
			Buchungsart buchungsart = new Buchungsart(buchungsartDto.getCNr());
			em.persist(buchungsart);
			em.flush();
			setBuchungsartFromBuchungsartDto(buchungsart, buchungsartDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	/***
	 * setBuchungsartFromBuchungsartDto** @param buchungsart Buchungsart* @param
	 * buchungsartDto BuchungsartDto
	 */
	private void setBuchungsartFromBuchungsartDto(Buchungsart buchungsart,
			BuchungsartDto buchungsartDto) {
		em.merge(buchungsart);
		em.flush();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void createWarenverkehrsnummernUndLoescheAlte(
			WarenverkehrsnummerDto[] warenverkehrsnummerDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (warenverkehrsnummerDtos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("warenverkehrsnummerDtos == null"));
		}
		myLogger.warn(theClientDto.getIDUser(),
				"createWarenverkehrsnummernUndLoescheAlte: "
						+ warenverkehrsnummerDtos.length + " neue Eintraege.");
		// zuerst die alten loeschen
		getFinanzServiceFac().removeAllWarenverkehrsnummern();
		// Session session = null;
		// // try {
		// SessionFactory factory = FLRSessionFactory.getFactory();
		// session = factory.openSession();
		// Criteria c =
		// session.createCriteria(FLRFinanzWarenverkehrsnummer.class);
		// List<?> list = c.list();
		// for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
		// FLRFinanzWarenverkehrsnummer item = (FLRFinanzWarenverkehrsnummer)
		// iter
		// .next();
		// Warenverkehrsnummer toRemove = em.find(Warenverkehrsnummer.class,
		// item.getC_nr());
		// if (toRemove == null) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, null);
		// }
		// try {
		// em.remove(toRemove);
		// em.flush();
		// } catch (EntityExistsException er) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
		// er);
		// }
		// }
		// // }
		// // catch (RemoveException ex) {
		// // throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// // }
		// // finally {
		// closeSession(session);
		// // }
		// nun die neuen anlegen.
		try {
			for (int i = 0; i < warenverkehrsnummerDtos.length; i++) {
				getFinanzServiceFac().createWarenverkehrsnummer(
						warenverkehrsnummerDtos[i]);
				// Warenverkehrsnummer warenverkehrsnummer = new
				// Warenverkehrsnummer(
				// warenverkehrsnummerDtos[i].getCNr(),
				// warenverkehrsnummerDtos[i].getCBez());
				// em.persist(warenverkehrsnummer);
				// em.flush();
				// setWarenverkehrsnummerFromWarenverkehrsnummerDto(
				// warenverkehrsnummer, warenverkehrsnummerDtos[i]);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeAllWarenverkehrsnummern() {
		Session session = null;
		// try {
		SessionFactory factory = FLRSessionFactory.getFactory();
		session = factory.openSession();
		Criteria c = session.createCriteria(FLRFinanzWarenverkehrsnummer.class);
		List<?> list = c.list();
		for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
			FLRFinanzWarenverkehrsnummer item = (FLRFinanzWarenverkehrsnummer) iter
					.next();
			Warenverkehrsnummer toRemove = em.find(Warenverkehrsnummer.class,
					item.getC_nr());
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		}
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
		// finally {
		closeSession(session);
	}

	public void createWarenverkehrsnummer(
			WarenverkehrsnummerDto warenverkehrsnummerDto) {
		Warenverkehrsnummer warenverkehrsnummer = new Warenverkehrsnummer(
				warenverkehrsnummerDto.getCNr(),
				warenverkehrsnummerDto.getCBez());
		em.persist(warenverkehrsnummer);
		em.flush();
		setWarenverkehrsnummerFromWarenverkehrsnummerDto(warenverkehrsnummer,
				warenverkehrsnummerDto);
	}

	public void vertauscheSteuerkategorie(Integer iiD1, Integer iId2) {
		Steuerkategorie oSteuerkategorie1 = em
				.find(Steuerkategorie.class, iiD1);
		if (oSteuerkategorie1 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheSteuerkategorie. Es gibt keine Steuerkategorie mit iid1 "
							+ iiD1);
		}

		Steuerkategorie oSteuerkategorie2 = em
				.find(Steuerkategorie.class, iId2);
		if (oSteuerkategorie2 == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheSteuerkategorie. Es gibt keinen Steuerkategorie mit iid2 "
							+ iId2);
		}

		Integer iSort1 = oSteuerkategorie1.getISort();
		Integer iSort2 = oSteuerkategorie2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oSteuerkategorie2.setISort(new Integer(-1));
		oSteuerkategorie1.setISort(iSort2);
		oSteuerkategorie2.setISort(iSort1);

	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKey(String cNr)
			throws EJBExceptionLP {
		Warenverkehrsnummer warenverkehrsnummer = em.find(
				Warenverkehrsnummer.class, cNr);
		if (warenverkehrsnummer == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleWarenverkehrsnummerDto(warenverkehrsnummer);
	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKeyOhneExc(
			String cNr) throws EJBExceptionLP {
		Warenverkehrsnummer warenverkehrsnummer = em.find(
				Warenverkehrsnummer.class, cNr);
		if (warenverkehrsnummer == null) {
			return null;
		}
		return assembleWarenverkehrsnummerDto(warenverkehrsnummer);
	}

	private void setWarenverkehrsnummerFromWarenverkehrsnummerDto(
			Warenverkehrsnummer warenverkehrsnummer,
			WarenverkehrsnummerDto warenverkehrsnummerDto) {
		warenverkehrsnummer.setCBez(warenverkehrsnummerDto.getCBez());
		em.merge(warenverkehrsnummer);
		em.flush();
	}

	private WarenverkehrsnummerDto assembleWarenverkehrsnummerDto(
			Warenverkehrsnummer warenverkehrsnummer) {
		return WarenverkehrsnummerDtoAssembler.createDto(warenverkehrsnummer);
	}

	/**
	 * Finanzamtsbuchungen f&uuml;r Periode durchf&uuml;hren
	 * 
	 * wird vor UVA Verprobung ausgef&uuml;hrt
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 * 
	 */

	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		FinanzamtDto[] finanzamtDtos = getFinanzFac()
				.finanzamtFindAllByMandantCNr(theClientDto);
		for (FinanzamtDto finanzamt : finanzamtDtos) {
			createFinanzamtsbuchungen(geschaeftsjahr, periode,
					finanzamt.getPartnerIId(), theClientDto);
		}
	}

	/**
	 * Finanzamtsbuchungen f&uuml;r Periode auf ein Finanzamt durchf&uuml;hren
	 * wird vor UVA Verprobung ausgef&uuml;hrt
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 * 
	 */
	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode,
			int finanzamtIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		// der Ablauf ist derzeit hart kodiert, die Konten werden ueber
		// Kontoarten definiert

		// PJ18633 mit Parameter PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL werden
		// hier KEINE Buchungen durchgefuehrt!
		ParametermandantDto p = getParameterFac()
				.parametermandantFindByPrimaryKey(
						ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL,
						ParameterFac.KATEGORIE_FINANZ,
						theClientDto.getMandant());
		if (p.asBoolean())
			return;

		Timestamp tVonBis[] = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date buchungsDatum = Helper.addiereTageZuDatum(
				new Date(tVonBis[1].getTime()), -1); // letzter Tag der Periode

		HashMap<String, Integer> hmAZ = null;

		// alte Buchungen stornieren
		// SP2015/03280 alle AKZ merken
		hmAZ = getBuchenFac().storniereFinanzamtsbuchungen(geschaeftsjahr,
				buchungsDatum, finanzamtIId, theClientDto);

		ArrayList<KontoDto> sammellist = new ArrayList<KontoDto>();
		// Alle Ust Konten auf UST Sammelkonto
		KontoDto konto = saldiereKontoartAufSammelkonto(KONTOART_UST,
				KONTOART_UST_SAMMEL, buchungsDatum, geschaeftsjahr, periode,
				finanzamtIId, theClientDto);
		if (konto != null)
			sammellist.add(konto);
		// Alle Vst Konten auf VST Sammelkonto
		konto = saldiereKontoartAufSammelkonto(KONTOART_VST,
				KONTOART_VST_SAMMEL, buchungsDatum, geschaeftsjahr, periode,
				finanzamtIId, theClientDto);
		if (konto != null)
			sammellist.add(konto);
		if (sammellist.size() == 0)
			return; // 18533
		// UST Sammel + VST Sammel auf FA Steuer Verrechnung
		KontoDto kontoZielDto = getSammelkonto(
				KONTOART_UST_ODER_ERWERBSSTEUERKONTO, finanzamtIId,
				theClientDto);
		if (kontoZielDto == null)
			return; // rk: SP 2424
		saldoBuchungAufKonto(sammellist, kontoZielDto.getIId(), geschaeftsjahr,
				periode, buchungsDatum, theClientDto);
		// FA Steuer Verrechnung auf Zahllast Konto
		KontoDto zahllastKontoDto = getSammelkonto(KONTOART_FA_ZAHLLAST,
				finanzamtIId, theClientDto);
		if (zahllastKontoDto == null)
			return; // rk: SP 2424
		saldoBuchungAufKonto(new KontoDto[] { kontoZielDto },
				zahllastKontoDto.getIId(), geschaeftsjahr, periode,
				buchungsDatum, theClientDto);
		// Alle Abgaben auf Zahllastkonto
		saldiereKontoartAufSammelkonto(KONTOART_ABGABEN, KONTOART_FA_ZAHLLAST,
				buchungsDatum, geschaeftsjahr, periode, finanzamtIId,
				theClientDto);
		if (!hmAZ.isEmpty())
			// SP2015/03280 alle gemerkten AKZ wieder setzen wenn Buchungsdetail
			// wieder vorhanden
			getBuchenFac().setAuszifferungenFinanzamtsbuchungen(geschaeftsjahr,
					buchungsDatum, finanzamtIId, hmAZ, theClientDto);
	}

	public KontoDto getSammelkonto(String kontoartCNr, Integer finanzamtIId,
			TheClientDto theClientDto) {
		KontoDto[] kontoDtos;
		if (finanzamtIId != null)
			kontoDtos = getFinanzFac().kontoFindAllByKontoartMandantFinanzamt(
					kontoartCNr, theClientDto.getMandant(), finanzamtIId);
		else
			kontoDtos = getFinanzFac().kontoFindAllByKontoartMandant(
					kontoartCNr, theClientDto.getMandant());

		KontoDto sammelkonto = null;
		if (kontoDtos.length == 0)
			return null;
		// throw new
		// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
		// "Kein Sammelkonto f\u00FCr Kontoart " + kontoartCNr);
		// else
		if (kontoDtos.length > 1) {
			String s = "";
			for (int i = 0; i < kontoDtos.length; i++)
				s = s + kontoDtos[i].getCNr() + " ";
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Mehrfache Sammelkonten definiert f\u00FCr Kontoart "
							+ kontoartCNr + ": " + s);
		} else
			sammelkonto = kontoDtos[0];
		return sammelkonto;
	}

	private KontoDto saldiereKontoartAufSammelkonto(String kontoartCNr,
			String kontoartCNrSammelkonto, Date buchungsDatum,
			int geschaeftsjahr, int periode, int finanzamtIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// es darf nur ein Sammelkonto geben
		KontoDto sammelkonto = getSammelkonto(kontoartCNrSammelkonto,
				finanzamtIId, theClientDto);
		if (sammelkonto == null) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
			// "Kein Sammelkonto f\u00FCr Kontoart " + kontoartCNrSammelkonto);
			// //PJ 18533
			return null;
		}
		KontoDto[] kontoDtos = getFinanzFac()
				.kontoFindAllByKontoartMandantFinanzamt(kontoartCNr,
						theClientDto.getMandant(), finanzamtIId);
		saldoBuchungAufKonto(kontoDtos, sammelkonto.getIId(), geschaeftsjahr,
				periode, buchungsDatum, theClientDto);
		return sammelkonto;
	}

	private void saldoBuchungAufKonto(ArrayList<KontoDto> sammellist,
			Integer kontoIIdZiel, int geschaeftsjahr, int periode,
			Date buchungsDatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		saldoBuchungAufKonto(
				sammellist.toArray(new KontoDto[sammellist.size()]),
				kontoIIdZiel, geschaeftsjahr, periode, buchungsDatum,
				theClientDto);
	}

	private void saldoBuchungAufKonto(KontoDto[] kontoDtos,
			Integer kontoIIdZiel, int geschaeftsjahr, int periode,
			Date buchungsDatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BuchungDto buchungDto = new BuchungDto(FinanzFac.BUCHUNGSART_UMBUCHUNG,
				buchungsDatum, "Sammelbuchung", geschaeftsjahr, null, true,
				false);
		ArrayList<BuchungdetailDto> list = getSammelbuchungdetails(kontoDtos,
				kontoIIdZiel, geschaeftsjahr, periode, theClientDto);
		BigDecimal summe = new BigDecimal(0);
		if (list.size() > 0) {
			for (BuchungdetailDto detail : list) {
				summe = summe.add(detail.getBuchungdetailartCNr().equals(
						BuchenFac.SollBuchung) ? detail.getNBetrag() : detail
						.getNBetrag().negate());
			}

			// Gegenbuchung erzeugen fuer Summe der Buchungen
			BuchungdetailDto detail = new BuchungdetailDto(list.get(0)
					.getBuchungdetailartCNr(), kontoIIdZiel,
					list.size() == 1 ? list.get(0).getKontoIId() : null,
					summe.abs(), new BigDecimal(0));
			if (detail.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung)) {
				if (summe.signum() < 0) {
					//
				} else {
					detail.swapSollHaben();
				}
			} else {
				if (summe.signum() < 0) {
					detail.swapSollHaben();
				} else {
					//
				}
			}
			list.add(0, detail);
			Mandant mandant = em.find(Mandant.class, theClientDto.getMandant());
			buchungDto.setKostenstelleIId(mandant.getKostenstelleIId());
			buchungDto.setCBelegnummer("FB" + geschaeftsjahr + "/" + periode);
			buchungDto = getBuchenFac().buchen(buchungDto,
					list.toArray(new BuchungdetailDto[list.size()]), true,
					theClientDto);
		}
	}

	private ArrayList<BuchungdetailDto> getSammelbuchungdetails(
			KontoDto[] kontoDtos, Integer kontoIIdGegenkonto,
			int geschaeftsjahr, int periode, TheClientDto theClientDto) {
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		for (int i = 0; i < kontoDtos.length; i++) {
			BigDecimal saldo = getBuchenFac().getSaldoOhneEBVonKonto(
					kontoDtos[i].getIId(), geschaeftsjahr, periode,
					theClientDto);
			if (saldo.signum() < 0) {
				BuchungdetailDto detailDto = new BuchungdetailDto(
						BuchenFac.SollBuchung, kontoDtos[i].getIId(),
						kontoIIdGegenkonto, saldo.abs(), new BigDecimal(0));
				list.add(detailDto);
			} else if (saldo.signum() > 0) {
				BuchungdetailDto detailDto = new BuchungdetailDto(
						BuchenFac.HabenBuchung, kontoDtos[i].getIId(),
						kontoIIdGegenkonto, saldo, new BigDecimal(0));
				list.add(detailDto);
			}
		}
		return list;
	}

	/**
	 * Tagesabschluss fuer das Geschaeftsjahr Periodenweise pr&uuml;fen und
	 * updaten
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 */
	/*
	 * AD nicht verwendet
	 * 
	 * @SuppressWarnings("unchecked") public void createTagesabschluss(int
	 * geschaeftsjahr, TheClientDto theclientDto) throws EJBExceptionLP,
	 * RemoteException { int periode = getAktuellePeriode(); // TODO: default
	 * wert aus mandant Integer kostenstelleIId = new Integer(11); Query query =
	 * em.createNamedQuery("SteuerkategorieByMandantCNr"); query.setParameter(1,
	 * theclientDto.getMandant()); List<Steuerkategorie> list =
	 * query.getResultList(); Iterator<Steuerkategorie> iter = list.iterator();
	 * while (iter.hasNext()) { Steuerkategorie stk = iter.next(); for (int i =
	 * 1; i <= periode; i++) { // Forderungen verbuchen
	 * verbucheForderungen(stk.getIId(), stk.getKontoIIdForderungen(),
	 * geschaeftsjahr, i, kostenstelleIId, theclientDto); // Verbindlichkeiten
	 * verbuchen } } // Ust Sammelkonto // Vst Sammelkonto
	 * 
	 * }
	 */

	/*
	 * AD nicht verwendet
	 * 
	 * @SuppressWarnings("unchecked") private void verbucheForderungen(Integer
	 * steuerkategorieIId, Integer sammelKontoIId, int geschaeftsjahr, int
	 * periode, Integer kostenstelleIId, TheClientDto theClientDto) throws
	 * EJBExceptionLP, RemoteException { Query query =
	 * em.createNamedQuery("KontofindByKontotypSteuerkategorie");
	 * query.setParameter(1, FinanzServiceFac.KONTOTYP_DEBITOR);
	 * query.setParameter(2, steuerkategorieIId); List<Konto> list =
	 * query.getResultList(); if (list.size() > 0) { Iterator<Konto> iter =
	 * list.iterator(); BigDecimal saldo = new BigDecimal(0); HashMap hmSaldo =
	 * new HashMap(); int cnt = 0; while (iter.hasNext()) { Konto konto =
	 * iter.next(); BigDecimal saldoKonto =
	 * getBuchenFac().getSaldoOhneEBVonKonto( konto.getIId(), geschaeftsjahr,
	 * periode, theClientDto); if (saldoKonto.doubleValue() != 0) { saldo =
	 * saldo.add(saldoKonto); KontoSaldo ks = new KontoSaldo(konto.getIId(),
	 * konto, saldoKonto); hmSaldo.put(new Integer(cnt), ks); cnt++; } } if
	 * (hmSaldo.size() > 0) { BuchungDto b = new BuchungDto(); //
	 * b.setBelegartCNr(belegartCNr);
	 * b.setBuchungsartCNr(FinanzFac.BUCHUNGSART_UMBUCHUNG);
	 * b.setCBelegnummer("Abschluss" + periode); b.setCText("Tagesabschluss");
	 * Calendar c = Calendar.getInstance(); c.set(Calendar.YEAR,
	 * geschaeftsjahr); c.set(Calendar.MONTH, periode);
	 * c.set(Calendar.DAY_OF_MONTH, 0); b.setDBuchungsdatum((Date)
	 * Helper.cutDate(c.getTime())); b.setIGeschaeftsjahr(geschaeftsjahr);
	 * b.setKostenstelleIId(kostenstelleIId); BuchungdetailDto[] bd = new
	 * BuchungdetailDto[hmSaldo.size() + 1]; bd[0] = new BuchungdetailDto();
	 * bd[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
	 * bd[0].setKontoIId(sammelKontoIId); bd[0].setNBetrag(saldo);
	 * bd[0].setNUst(new BigDecimal(0)); for (int i = 0; i < hmSaldo.size();
	 * i++) { KontoSaldo ks = (KontoSaldo) hmSaldo.get(new Integer(i)); bd[i +
	 * 1] = new BuchungdetailDto(); bd[i +
	 * 1].setBuchungdetailartCNr(BuchenFac.HabenBuchung); bd[i +
	 * 1].setKontoIId(ks.kontoIId); bd[i +
	 * 1].setKontoIIdGegenkonto(sammelKontoIId); bd[i + 1].setNBetrag(ks.saldo);
	 * bd[i + 1].setNUst(new BigDecimal(0)); } getBuchenFac().buchen(b, bd,
	 * false, theClientDto); } } }
	 */

	class KontoSaldo {
		Integer kontoIId;
		Konto konto;
		BigDecimal saldo;

		KontoSaldo(Integer kontoIIdI, Konto kontoI, BigDecimal saldoI) {
			kontoIId = kontoIIdI;
			konto = kontoI;
			saldo = saldoI;
		}
	}

	// public UvaartDto uvaartFindByCnrMandant(String cNr,
	// TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
	// Query query = em.createNamedQuery("UvaartfindByCNrMandant");
	// query.setParameter(1, cNr);
	// query.setParameter(2, theClientDto.getMandant());
	//
	// try {
	// Uvaart uvaart = (Uvaart) query.getSingleResult();
	//
	// UvaartDto uvaartDto = assembleUvaartDto(uvaart);
	//
	// uvaartDto.setUvaartsprDto(new UvaartsprDto());
	// uvaartDto.getUvaartsprDto().setUvaartIId(uvaart.getIId());
	// uvaartDto.getUvaartsprDto().setLocaleCNr(
	// theClientDto.getLocUiAsString());
	// uvaartDto.getUvaartsprDto().setCBez(
	// uebersetzeUvaart(uvaart.getIId(), theClientDto.getLocUi()));
	// if (uvaartDto.getUvaartsprDto().getCBez() == null)
	// uvaartDto.getUvaartsprDto().setCBez(uvaart.getCNr());
	// return uvaartDto;
	// } catch (NoResultException ex) {
	// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
	// "Uvaart nicht gefunden cNr:" + cNr + " Mandant:"
	// + theClientDto.getMandant());
	// }
	// }

	// public UvaartDto uvaartFindByCnrMandantOhneExc(String cNr,
	// TheClientDto theClientDto) throws RemoteException {
	// Query query = em.createNamedQuery("UvaartfindByCNrMandant");
	// query.setParameter(1, cNr);
	// query.setParameter(2, theClientDto.getMandant());
	//
	// UvaartDto uvaartDto = null;
	//
	// try {
	// Uvaart uvaart = (Uvaart) query.getSingleResult();
	//
	// uvaartDto = assembleUvaartDto(uvaart);
	//
	// uvaartDto.setUvaartsprDto(new UvaartsprDto());
	// uvaartDto.getUvaartsprDto().setUvaartIId(uvaart.getIId());
	// uvaartDto.getUvaartsprDto().setLocaleCNr(
	// theClientDto.getLocUiAsString());
	// uvaartDto.getUvaartsprDto().setCBez(
	// uebersetzeUvaart(uvaart.getIId(), theClientDto.getLocUi()));
	// if (uvaartDto.getUvaartsprDto().getCBez() == null)
	// uvaartDto.getUvaartsprDto().setCBez(uvaart.getCNr());
	// return uvaartDto;
	// } catch (NoResultException ex) {
	// }
	// return uvaartDto;
	// }

	// *** wp ***
	// ***

	public UvaartDto uvaartFindByCnrMandant(String cNr,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		try {
			return getUvaartDto(cNr, theClientDto);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Uvaart nicht gefunden cNr:" + cNr + " Mandant:"
							+ theClientDto.getMandant());
		}
	}

	public UvaartDto uvaartFindByCnrMandantOhneExc(String cNr,
			TheClientDto theClientDto) throws RemoteException {
		try {
			return getUvaartDto(cNr, theClientDto);
		} catch (NoResultException ex) {
		}
		return null;
	}

	private Query uvaartDtoQuery(String cNr, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("UvaartfindByCNrMandant");
		query.setParameter(1, cNr);
		query.setParameter(2, theClientDto.getMandant());
		return query;
	}

	private UvaartDto getUvaartDto(String cNr, TheClientDto theClientDto) {
		Query query = uvaartDtoQuery(cNr, theClientDto);
		Uvaart uvaart = (Uvaart) query.getSingleResult();
		UvaartDto uvaartDto = assembleUvaartDto(uvaart);

		UvaartsprDto sprDto = getUvaartsprDto(uvaart.getIId(),
				theClientDto.getLocUi(), theClientDto.getLocMandant());

		uvaartDto.setUvaartsprDto(sprDto);

		return uvaartDto;
	}

	// ***
	// *** wp ***

	public Integer getUstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId) {
		Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class,
				new SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId));
		if (stkk != null)
			return stkk.getKontoIIdVk();
		else
			return null;
	}

	public Integer getEUstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId) {
		Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class,
				new SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId));
		if (stkk != null)
			return stkk.getKontoIIdEinfuhrUst();
		else
			return null;
	}

	public Integer getVstKontoFuerSteuerkategorie(Integer steuerkategorieIId,
			Integer mwstsatzbezId) {
		Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class,
				new SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId));
		if (stkk != null)
			return stkk.getKontoIIdEk();
		else
			return null;
	}

	@TransactionTimeout(value = 600)
	public void verbucheBelegePeriode(Integer geschaeftsjahr, int periode,
			boolean alleNeu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException {
		getBelegbuchungFac(theClientDto.getMandant()).verbucheBelegePeriode(
				geschaeftsjahr, periode, alleNeu, theClientDto);
	}

	@TransactionTimeout(value = 300)
	public ArrayList<FibuFehlerDto> pruefeBelegeKurse(Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		ArrayList<FibuFehlerDto> fehler = new ArrayList<FibuFehlerDto>();
		fehler.addAll(pruefeKurseRechnungen(fehler, geschaeftsjahr, nurPruefen,
				theClientDto));
		fehler.addAll(pruefeKurseRechnungZahlungen(fehler, geschaeftsjahr,
				nurPruefen, theClientDto));
		fehler.addAll(pruefeKurseEingangsrechnungen(fehler, geschaeftsjahr,
				nurPruefen, theClientDto));
		fehler.addAll(pruefeKurseEingangsrechnungZahlungen(fehler,
				geschaeftsjahr, nurPruefen, theClientDto));
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseRechnungen(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = null;
		try {
			rechnungen = getRechnungFac().rechnungFindByBelegdatumVonBis(
					theClientDto.getMandant(), dBeginn, dEnd);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(
						RechnungFac.STATUS_ANGELEGT)
						&& !rechnungen[i].getStatusCNr().equals(
								RechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getNKurs().doubleValue() != 1) {
						BigDecimal alterKurs = rechnungen[i].getNKurs();
						BigDecimal alterWert = rechnungen[i].getNWert();
						WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
								theClientDto.getSMandantenwaehrung(),
								rechnungen[i].getWaehrungCNr(),
								new Date(rechnungen[i].getTBelegdatum()
										.getTime()), theClientDto);
						if (kursDto.getNKurs().compareTo(
								rechnungen[i].getNKurs()) != 0) {
							if (nurPruefen) {
								fehler.add(new FibuFehlerDto(
										LocaleFac.BELEGART_FIBU_RECHNUNG,
										rechnungen[i].getIId(), rechnungen[i]
												.getCNr(),
										FibuFehlerDto.FEHLER_KURS));
								myLogger.error("Kursabweichung: AR"
										+ rechnungen[i].getCNr()
										+ " von "
										+ alterKurs.toString()
										+ " auf "
										+ kursDto.getNKurs().toString()
										+ " Differenz: "
										+ alterWert
												.subtract(
														rechnungen[i]
																.getNWertfw()
																.divide(kursDto
																		.getNKurs(),
																		FinanzFac.NACHKOMMASTELLEN,
																		BigDecimal.ROUND_HALF_EVEN))
												.toString());
							} else {
								rechnungen[i].setNKurs(kursDto.getNKurs());
								rechnungen[i].setNWert(rechnungen[i]
										.getNWertfw().divide(
												kursDto.getNKurs(),
												FinanzFac.NACHKOMMASTELLEN,
												BigDecimal.ROUND_HALF_EVEN));
								rechnungen[i].setNWertust(rechnungen[i]
										.getNWertustfw().divide(
												kursDto.getNKurs(),
												FinanzFac.NACHKOMMASTELLEN,
												BigDecimal.ROUND_HALF_EVEN));
								getRechnungFac().updateRechnung(rechnungen[i]);
								myLogger.error("Kurs korrigiert: AR"
										+ rechnungen[i].getCNr()
										+ " von "
										+ alterKurs.toString()
										+ " auf "
										+ kursDto.getNKurs().toString()
										+ " Differenz: "
										+ alterWert.subtract(
												rechnungen[i].getNWert())
												.toString());
							}
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseRechnungZahlungen(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getRechnungFac()
					.rechnungzahlungIdsByMandantZahldatumVonBis(
							theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				RechnungzahlungDto zahlungDto = getRechnungFac()
						.rechnungzahlungFindByPrimaryKey(zahlungenIDs.get(i));
				if (zahlungDto.getNKurs().doubleValue() != 1) {
					Rechnung rechnung = em.find(Rechnung.class,
							zahlungDto.getRechnungIId());
					BigDecimal alterKurs = zahlungDto.getNKurs();
					BigDecimal alterBetrag = zahlungDto.getNBetrag();
					WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
							theClientDto.getSMandantenwaehrung(),
							rechnung.getWaehrungCNr(),
							zahlungDto.getDZahldatum(), theClientDto);
					if (kursDto.getNKurs().compareTo(zahlungDto.getNKurs()) != 0) {
						if (nurPruefen) {
							fehler.add(new FibuFehlerDto(
									LocaleFac.BELEGART_REZAHLUNG, rechnung
											.getIId(), rechnung.getCNr(),
									FibuFehlerDto.FEHLER_KURS));
							myLogger.error("Kursabweichung: ARZahlung"
									+ rechnung.getCNr()
									+ " von "
									+ alterKurs.toString()
									+ " auf "
									+ kursDto.getNKurs().toString()
									+ " Differenz: "
									+ alterBetrag
											.subtract(
													zahlungDto
															.getNBetragfw()
															.divide(kursDto
																	.getNKurs(),
																	FinanzFac.NACHKOMMASTELLEN,
																	BigDecimal.ROUND_HALF_EVEN))
											.toString());
						} else {
							zahlungDto.setNKurs(kursDto.getNKurs());
							zahlungDto.setNBetrag(zahlungDto.getNBetragfw()
									.divide(kursDto.getNKurs(),
											FinanzFac.NACHKOMMASTELLEN,
											BigDecimal.ROUND_HALF_EVEN));
							zahlungDto.setNBetragUst(zahlungDto
									.getNBetragUstfw().divide(
											kursDto.getNKurs(),
											FinanzFac.NACHKOMMASTELLEN,
											BigDecimal.ROUND_HALF_EVEN));
							getRechnungFac().updateRechnungzahlung(zahlungDto);
							myLogger.error("Kurs korrigiert: ARZahlung"
									+ rechnung.getCNr()
									+ " von "
									+ alterKurs.toString()
									+ " auf "
									+ kursDto.getNKurs().toString()
									+ " Differenz: "
									+ alterBetrag.subtract(
											zahlungDto.getNBetrag()).toString());
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseEingangsrechnungen(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = null;
		try {
			rechnungen = getEingangsrechnungFac()
					.eingangsrechnungFindByMandantCNrDatumVonBis(
							theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(
						EingangsrechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getNKurs().doubleValue() != 1) {
						BigDecimal alterKurs = rechnungen[i].getNKurs();
						BigDecimal alterWert = rechnungen[i].getNBetrag();
						WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
								theClientDto.getSMandantenwaehrung(),
								rechnungen[i].getWaehrungCNr(),
								new Date(rechnungen[i].getDBelegdatum()
										.getTime()), theClientDto);
						BigDecimal neuerKurs = new BigDecimal(1).divide(
								kursDto.getNKurs(), 6,
								BigDecimal.ROUND_HALF_EVEN);
						if (neuerKurs.compareTo(rechnungen[i].getNKurs()) != 0) {
							if (nurPruefen) {
								fehler.add(new FibuFehlerDto(
										LocaleFac.BELEGART_EINGANGSRECHNUNG,
										rechnungen[i].getIId(), rechnungen[i]
												.getCNr(),
										FibuFehlerDto.FEHLER_KURS));
								myLogger.error("Kursabweichung: ER"
										+ rechnungen[i].getCNr()
										+ " von "
										+ alterKurs.toString()
										+ " auf "
										+ neuerKurs.toString()
										+ " Differenz: "
										+ alterWert
												.subtract(
														rechnungen[i]
																.getNBetragfw()
																.divide(kursDto
																		.getNKurs(),
																		FinanzFac.NACHKOMMASTELLEN,
																		BigDecimal.ROUND_HALF_EVEN))
												.toString());
							} else {
								rechnungen[i].setNKurs(neuerKurs);
								rechnungen[i].setNBetrag(rechnungen[i]
										.getNBetragfw().divide(
												kursDto.getNKurs(),
												FinanzFac.NACHKOMMASTELLEN,
												BigDecimal.ROUND_HALF_EVEN));
								rechnungen[i].setNUstBetrag(rechnungen[i]
										.getNUstBetragfw().divide(
												kursDto.getNKurs(),
												FinanzFac.NACHKOMMASTELLEN,
												BigDecimal.ROUND_HALF_EVEN));
								if (i >= 286)
									System.out.println(i);
								getEingangsrechnungFac()
										.updateEingangsrechnung(rechnungen[i],
												theClientDto);
								myLogger.error("Kurs korrigiert: ER"
										+ rechnungen[i].getCNr()
										+ " von "
										+ alterKurs.toString()
										+ " auf "
										+ neuerKurs.toString()
										+ " Differenz: "
										+ alterWert.subtract(
												rechnungen[i].getNBetrag())
												.toString());
							}
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseEingangsrechnungZahlungen(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getEingangsrechnungFac()
					.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
							theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				EingangsrechnungzahlungDto erzDto = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByPrimaryKey(
								zahlungenIDs.get(i));

				if (erzDto.getNKurs().doubleValue() != 1
						&& !Helper.short2boolean(erzDto.getBKursuebersteuert())) {
					Integer rechnungIId = erzDto.getEingangsrechnungIId();
					Eingangsrechnung er = em.find(Eingangsrechnung.class,
							rechnungIId);
					BigDecimal alterKurs = erzDto.getNKurs();
					BigDecimal alterBetrag = erzDto.getNBetrag();
					WechselkursDto kursDto = getLocaleFac().getKursZuDatum(
							theClientDto.getSMandantenwaehrung(),
							er.getWaehrungCNr(), erzDto.getTZahldatum(),
							theClientDto);
					if (kursDto.getNKurs().compareTo(erzDto.getNKurs()) != 0) {
						if (nurPruefen) {
							fehler.add(new FibuFehlerDto(
									LocaleFac.BELEGART_ERZAHLUNG, er.getIId(),
									er.getCNr(), FibuFehlerDto.FEHLER_KURS));
							myLogger.error("Kursabweichung: ERZahlung"
									+ er.getCNr()
									+ " von "
									+ alterKurs.toString()
									+ " auf "
									+ kursDto.getNKurs().toString()
									+ " Differenz: "
									+ alterBetrag
											.subtract(
													erzDto.getNBetragfw()
															.divide(kursDto
																	.getNKurs(),
																	FinanzFac.NACHKOMMASTELLEN,
																	BigDecimal.ROUND_HALF_EVEN))
											.toString());
						} else {
							erzDto.setNKurs(kursDto.getNKurs());
							erzDto.setNBetrag(erzDto.getNBetragfw().divide(
									kursDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN,
									BigDecimal.ROUND_HALF_EVEN));
							erzDto.setNBetragust(erzDto.getNBetragustfw()
									.divide(kursDto.getNKurs(),
											FinanzFac.NACHKOMMASTELLEN,
											BigDecimal.ROUND_HALF_EVEN));
							getEingangsrechnungFac()
									.updateEingangsrechnungzahlung(erzDto);
							myLogger.error("Kurs korrigiert: ERZahlung"
									+ er.getCNr()
									+ " von "
									+ alterKurs.toString()
									+ " auf "
									+ kursDto.getNKurs().toString()
									+ " Differenz: "
									+ alterBetrag.subtract(erzDto.getNBetrag())
											.toString());
						}
					}
				}
			}
		}
		return fehler;
	}

	public ArrayList<FibuFehlerDto> pruefeBelegePeriode(Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		// SP 2014/02892
		// bei ISTVERSTEURER keine Pruefung von AR und ER
		// bei MISCHVERSTEURER keine Pruefung von AR
		boolean istVersteurer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ISTVERSTEURER,
						theClientDto.getMandant());
		boolean mischVersteurer = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MISCHVERSTEURER,
						theClientDto.getMandant());

		ArrayList<FibuFehlerDto> fehler = new ArrayList<FibuFehlerDto>();
		if (!istVersteurer && !mischVersteurer)
			fehler = pruefeRechnungenPeriode(fehler, geschaeftsjahr, periode,
					pruefeBelegInFibu, theClientDto);
		fehler = pruefeRechnungZahlungenPeriode(fehler, geschaeftsjahr,
				periode, pruefeBelegInFibu, theClientDto);
		if (!istVersteurer)
			fehler = pruefeEingangsrechnungenPeriode(fehler, geschaeftsjahr,
					periode, pruefeBelegInFibu, theClientDto);
		fehler = pruefeEingangsrechnungZahlungenPeriode(fehler, geschaeftsjahr,
				periode, pruefeBelegInFibu, theClientDto);
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeRechnungZahlungenPeriode(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// hier wird nur geprueft ob in Fibu wenn erforderlich
		if (pruefeBelegInFibu) {
			Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
					geschaeftsjahr, periode, theClientDto);
			Date dBeginn = new Date(tVonBis[0].getTime());
			Date dEnd = new Date(tVonBis[1].getTime());

			List<Integer> zahlungenIDs = null;
			try {
				zahlungenIDs = getRechnungFac()
						.rechnungzahlungIdsByMandantZahldatumVonBis(
								theClientDto.getMandant(), dBeginn, dEnd);
			} catch (EJBExceptionLP e1) {
				e1.printStackTrace();
			}
			if (zahlungenIDs != null) {
				for (int i = 0; i < zahlungenIDs.size(); i++) {
					if (!isBelegInFibu(LocaleFac.BELEGART_REZAHLUNG,
							zahlungenIDs.get(i), theClientDto)) {
						Rechnungzahlung zahlung = em.find(
								Rechnungzahlung.class, zahlungenIDs.get(i));
						Integer rechnungIId = zahlung.getRechnungIId();
						Rechnung ar = em.find(Rechnung.class, rechnungIId);
						fehler.add(new FibuFehlerDto(
								LocaleFac.BELEGART_REZAHLUNG, rechnungIId, ar
										.getCNr(),
								FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeRechnungenPeriode(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = null;
		try {
			rechnungen = getRechnungFac().rechnungFindByBelegdatumVonBis(
					theClientDto.getMandant(), dBeginn, dEnd);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		}
		if (rechnungen != null) {
			boolean bRechnung0Erlaubt = false;
			try {
				if (getParameterFac()
						.getMandantparameter(
								theClientDto.getMandant(),
								ParameterFac.KATEGORIE_FINANZ,
								ParameterFac.PARAMETER_FINANZ_RECHNUNG_WERT0_ERLAUBT)
						.getCWert().equals("1"))
					bRechnung0Erlaubt = true;
			} catch (RemoteException e) {
				//
			}

			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getRechnungartCNr().equals(
						RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
					if (rechnungen[i].getStatusCNr().equals(
							RechnungFac.STATUS_ANGELEGT)) {
						fehler.add(new FibuFehlerDto(rechnungen[i]
								.getRechnungartCNr(), rechnungen[i].getIId(),
								rechnungen[i].getCNr(),
								FibuFehlerDto.FEHLER_STATUS));
					} else if (!rechnungen[i].getStatusCNr().equals(
							RechnungFac.STATUS_STORNIERT)) {
						if (!bRechnung0Erlaubt
								|| (rechnungen[i].getNWert().signum() != 0)) {
							// SP 2013/01327 nur Rechnungen mit Wert <> 0
							// abhaengig vom Parameter
							if (pruefeBelegInFibu
									&& !isBelegInFibu(
											LocaleFac.BELEGART_RECHNUNG,
											rechnungen[i].getIId(),
											theClientDto)) {
								fehler.add(new FibuFehlerDto(
										LocaleFac.BELEGART_RECHNUNG,
										rechnungen[i].getIId(), rechnungen[i]
												.getCNr(),
										FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
							}
						}
					}
				}
			}
		}
		return fehler;
	}

	/**
	 * 
	 * @param belegart
	 * @param belegIId
	 * @param theClientDto
	 * @return true .. wenn Beleg in Fibu &uuml;bernommen oder aufgrund des Typs
	 *         nicht &uuml;bernommen werden muss oder storniert ist
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	private boolean isBelegInFibu(String belegart, Integer belegIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		BelegbuchungDto bbDto = null;
		if (belegart.equals(LocaleFac.BELEGART_RECHNUNG)) {
			Rechnung rechnung = em.find(Rechnung.class, belegIId);
			String rechnungtyp = null;
			try {
				RechnungartDto rechnungartDto = getRechnungServiceFac()
						.rechnungartFindByPrimaryKey(
								rechnung.getRechnungartCNr(), theClientDto);
				rechnungtyp = rechnungartDto.getRechnungtypCNr();
			} catch (Exception ex) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_RECHNUNG, rechnung.getIId());
			} else if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_GUTSCHRIFT,
								rechnung.getIId());
			}

		} else if (belegart.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
			bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(
							LocaleFac.BELEGART_EINGANGSRECHNUNG, belegIId);

		} else if (belegart.equals(LocaleFac.BELEGART_REZAHLUNG)) {
			Rechnungzahlung zahlung = em.find(Rechnungzahlung.class, belegIId);
			if (zahlung.getZahlungsartCNr()
					.equals(RechnungFac.ZAHLUNGSART_BANK)
					|| zahlung.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_BAR)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_REZAHLUNG, belegIId);
			} else {
				// wird nicht gebucht --> true liefern
				return true;
			}

		} else if (belegart.equals(LocaleFac.BELEGART_ERZAHLUNG)) {
			Eingangsrechnungzahlung zahlung = em.find(
					Eingangsrechnungzahlung.class, belegIId);
			if (zahlung.getZahlungsartCNr()
					.equals(RechnungFac.ZAHLUNGSART_BANK)
					|| zahlung.getZahlungsartCNr().equals(
							RechnungFac.ZAHLUNGSART_BAR)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(
								LocaleFac.BELEGART_ERZAHLUNG, belegIId);
			} else {
				// wird nicht gebucht --> true liefern
				return true;
			}
		}

		if (bbDto != null)
			return true;
		return false;
	}

	private ArrayList<FibuFehlerDto> pruefeEingangsrechnungenPeriode(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = null;
		try {
			rechnungen = getEingangsrechnungFac()
					.eingangsrechnungFindByMandantCNrDatumVonBis(
							theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(
						EingangsrechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getKostenstelleIId() == null) {
						try {
							BigDecimal bdOffen = getEingangsrechnungFac()
									.getWertNochNichtKontiert(
											rechnungen[i].getIId());
							if (bdOffen.doubleValue() != 0) {
								fehler.add(new FibuFehlerDto(
										LocaleFac.BELEGART_EINGANGSRECHNUNG,
										rechnungen[i].getIId(),
										rechnungen[i].getCNr(),
										FibuFehlerDto.FEHLER_NICHT_VOLLSTAENDIG_KONTIERT));
							}
						} catch (EJBExceptionLP e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					if (pruefeBelegInFibu
							&& !isBelegInFibu(
									LocaleFac.BELEGART_EINGANGSRECHNUNG,
									rechnungen[i].getIId(), theClientDto)) {
						fehler.add(new FibuFehlerDto(
								LocaleFac.BELEGART_EINGANGSRECHNUNG,
								rechnungen[i].getIId(), rechnungen[i].getCNr(),
								FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeEingangsrechnungZahlungenPeriode(
			ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
				geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getEingangsrechnungFac()
					.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(
							theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				Eingangsrechnungzahlung erz = em.find(
						Eingangsrechnungzahlung.class, zahlungenIDs.get(i));
				Integer rechnungIId = erz.getEingangsrechnungIId();
				Eingangsrechnung er = em.find(Eingangsrechnung.class,
						rechnungIId);
				if (er.getKontoIId() == null) {
					try {
						BigDecimal bdOffen = getEingangsrechnungFac()
								.getWertNochNichtKontiert(rechnungIId);
						if (bdOffen.doubleValue() != 0) {
							fehler.add(new FibuFehlerDto(
									LocaleFac.BELEGART_ERZAHLUNG,
									rechnungIId,
									er.getCNr(),
									FibuFehlerDto.FEHLER_NICHT_VOLLSTAENDIG_KONTIERT));
						}
					} catch (EJBExceptionLP e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				if (pruefeBelegInFibu
						&& !isBelegInFibu(LocaleFac.BELEGART_ERZAHLUNG,
								zahlungenIDs.get(i), theClientDto)) {
					fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_ERZAHLUNG,
							rechnungIId, er.getCNr(),
							FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
				}
			}
		}
		return fehler;
	}

	public Integer createUvaverprobung(UvaverprobungDto uvaverprobungDto,
			TheClientDto theClientDto) {
		if (uvaverprobungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("uvaverprobungDto == null"));
		}
		if (uvaverprobungDto.getIGeschaeftsjahr() == null
				|| uvaverprobungDto.getIMonat() == null
				|| uvaverprobungDto.getFinanzamtIId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"uvaverprobungDto.getIGeschaeftsjahr() == null || uvaverprobungDto.getIMonat() == null || uvaverprobungDto.getFinanzamtIId() == null"));
		}

		uvaverprobungDto.setMandantCNr(theClientDto.getMandant());

		try {

			Query query = em
					.createNamedQuery("UvaverprobungfindByGeschaeftsjahrMonatFinanzamtMandant");
			query.setParameter(1, uvaverprobungDto.getIGeschaeftsjahr());
			query.setParameter(2, uvaverprobungDto.getIMonat().intValue()
					+ uvaverprobungDto.getIAbrechnungszeitraum());
			query.setParameter(3, uvaverprobungDto.getFinanzamtIId());
			query.setParameter(4, uvaverprobungDto.getMandantCNr());
			Uvaverprobung doppelt = (Uvaverprobung) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("FB_UVAVERPROBUNG.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_UVAVERPROBUNG);
			uvaverprobungDto.setIId(pk);

			Uvaverprobung uvap = new Uvaverprobung(uvaverprobungDto.getIId(),
					uvaverprobungDto.getIGeschaeftsjahr(), uvaverprobungDto
							.getIMonat().intValue()
							+ uvaverprobungDto.getIAbrechnungszeitraum(),
					uvaverprobungDto.getMandantCNr(),
					uvaverprobungDto.getFinanzamtIId(),
					theClientDto.getIDPersonal());
			uvap.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			em.persist(uvap);
			em.flush();
			return uvaverprobungDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception(e));
		}
	}

	public void removeUvaverprobung(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		Uvaverprobung uvap = em.find(Uvaverprobung.class, iId);
		if (uvap == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeUvaverprobung. Es gibt keine Uvaverprobung mit der iid "
							+ iId);
		}

		// zuerst die zugehoerigen verprobungen zuruecksetzen
		Query query = em.createNamedQuery("BuchungResetUvaverprobung");
		query.setParameter(1, iId);
		query.executeUpdate();

		em.remove(uvap);
		em.flush();
	}

	@SuppressWarnings("unchecked")
	public Integer uvaVerprobung(ReportUvaKriterienDto krit,
			TheClientDto theClientDto) {
		// Letzte Verprobung fuer das Finanzamt holen
		Query query = em
				.createNamedQuery("UvaverprobungfindLastByFinanzamtIIdMandant");
		query.setParameter(1, krit.getFinanzamtIId());
		query.setParameter(2, theClientDto.getMandant());
		query.setMaxResults(1);
		List<Uvaverprobung> list = (List<Uvaverprobung>) query.getResultList();
		boolean durchfuehren = true;
		if (list.size() > 0) {
			Uvaverprobung uvap = list.get(0);
			if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
				if (uvap.getGeschaeftsjahr().intValue() == krit
						.getIGeschaeftsjahr())
					if (uvap.getIMonat() >= krit.getIPeriode())
						durchfuehren = false;
			} else if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				if (uvap.getGeschaeftsjahr().intValue() > krit
						.getIGeschaeftsjahr())
					durchfuehren = false;
				else if (uvap.getGeschaeftsjahr().intValue() == krit
						.getIGeschaeftsjahr()) {
					if ((uvap.getIMonat() % UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL) >= krit
							.getIPeriode())
						durchfuehren = false;
				}
			} else if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {

			}
		}
		if (durchfuehren) {
			Integer[] perioden = null;
			UvaverprobungDto uvaverprobungDto = new UvaverprobungDto();
			if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
				perioden = new Integer[1];
				perioden[0] = krit.getIPeriode();
				uvaverprobungDto
						.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_MONAT);
			} else if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				perioden = HelperServer
						.getMonateFuerQuartal(krit.getIPeriode());
				uvaverprobungDto
						.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL);
			} else if (krit.getSAbrechnungszeitraum().equals(
					FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
				uvaverprobungDto
						.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_JAHR);
			}

			uvaverprobungDto.setIGeschaeftsjahr(krit.getIGeschaeftsjahr());
			uvaverprobungDto.setIMonat(krit.getIPeriode());
			uvaverprobungDto.setFinanzamtIId(krit.getFinanzamtIId());
			Integer uvaverprobungIId = createUvaverprobung(uvaverprobungDto,
					theClientDto);

			// alle Belege markieren
			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT DISTINCT o.buchung_i_id FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id IN (SELECT k.i_id FROM FLRFinanzKonto k WHERE k.finanzamt_i_id = :pFinanzamt)"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			org.hibernate.Query hquery = session.createQuery(queryString);
			for (int i = 0; i < perioden.length; i++) {
				Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(
						krit.getIGeschaeftsjahr(), perioden[i], theClientDto);
				hquery.setParameter("pFinanzamt", krit.getFinanzamtIId());
				hquery.setParameter("pVon", tVonBis[0]);
				hquery.setParameter("pEnd", tVonBis[1]);
				List<?> results = hquery.list();
				Iterator<Integer> it = (Iterator<Integer>) results.iterator();
				while (it.hasNext()) {
					verprobe(it.next(), uvaverprobungIId);
				}
			}
			return uvaverprobungIId;
		} else {
			return null;
		}
	}

	private void verprobe(Integer buchungIId, Integer uvaverprobungIId) {
		Buchung buchung = em.find(Buchung.class, buchungIId);
		buchung.setUvaverprobungIId(uvaverprobungIId);
		em.merge(buchung);
	}

	@SuppressWarnings("unchecked")
	public UvaverprobungDto getLetzteVerprobung(Integer partnerIIdFinanzamt,
			TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("UvaverprobungfindLastByFinanzamtIIdMandant");
		query.setParameter(1, partnerIIdFinanzamt);
		query.setParameter(2, theClientDto.getMandant());
		query.setMaxResults(1);
		List<Uvaverprobung> list = (List<Uvaverprobung>) query.getResultList();
		if (list.size() > 0) {
			Uvaverprobung uvap = list.get(0);
			return assembleUvaverprobungDto(uvap);
		}
		return null;
	}

	public Map<Integer, Integer> getAllIIdsUstkontoMitIIdMwstBez(
			Integer finanzamtIId, TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto,
				STEUERART_UST);
	}

	public Map<Integer, Integer> getAllIIdsVstkontoMitIIdMwstBez(
			Integer finanzamtIId, TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto,
				STEUERART_VST);
	}

	public Map<Integer, Integer> getAllIIdsEUstkontoMitIIdMwstBez(
			Integer finanzamtIId, TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto,
				STEUERART_EUST);
	}

	public Map<Integer, Integer> getAllIIdsSteuerkontoMitIIdMwstBez(
			Integer finanzamtIId, TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto,
				null);
	}

	/**
	 * 
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als
	 *         Value
	 */
	private Map<Integer, Integer> getAllIIdsSteuerkontoMitIIdMwstBez(
			Integer finanzamtIId, TheClientDto theClientDto, String steuerart) {
		Map<Integer, Integer> mwstKonten = new HashMap<Integer, Integer>();
		SteuerkategorieDto[] stkDtos = steuerkategorieFindByFinanzamtIId(
				finanzamtIId, theClientDto);
		for (SteuerkategorieDto stkat : stkDtos) {
			SteuerkategoriekontoDto[] stkks = steuerkategoriekontoFindAll(stkat
					.getIId());
			for (SteuerkategoriekontoDto stkk : stkks) {
				if (steuerart == null || STEUERART_VST.equals(steuerart)) {
					if (mwstKonten.containsKey(stkk.getKontoIIdEk()))
						mwstKonten.put(stkk.getKontoIIdEk(), null);
					else
						mwstKonten.put(stkk.getKontoIIdEk(),
								stkk.getMwstsatzbezIId());
				}
				if (steuerart == null || STEUERART_UST.equals(steuerart)) {
					if (mwstKonten.containsKey(stkk.getKontoIIdVk()))
						mwstKonten.put(stkk.getKontoIIdVk(), null);
					else
						mwstKonten.put(stkk.getKontoIIdVk(),
								stkk.getMwstsatzbezIId());
				}
				if (steuerart == null || STEUERART_EUST.equals(steuerart)) {
					if (mwstKonten.containsKey(stkk.getKontoIIdEinfuhrUst()))
						mwstKonten.put(stkk.getKontoIIdEinfuhrUst(), null);
					else
						mwstKonten.put(stkk.getKontoIIdEinfuhrUst(),
								stkk.getMwstsatzbezIId());
				}
			}
		}
		return mwstKonten;
	}

	public Set<Integer> getAllMitlaufendeKonten(Integer finanzamtIId,
			TheClientDto theClientDto) {
		Set<Integer> mitlaufendeKonten = new HashSet<Integer>();
		SteuerkategorieDto[] stkDtos = steuerkategorieFindByFinanzamtIId(
				finanzamtIId, theClientDto);
		for (SteuerkategorieDto stkat : stkDtos) {
			if (stkat.getKontoIIdForderungen() != null)
				mitlaufendeKonten.add(stkat.getKontoIIdForderungen());
			if (stkat.getKontoIIdVerbindlichkeiten() != null)
				mitlaufendeKonten.add(stkat.getKontoIIdVerbindlichkeiten());
		}
		return mitlaufendeKonten;

	}

	private UvaverprobungDto assembleUvaverprobungDto(
			Uvaverprobung uvaverprobung) {
		return UvaverprobungDtoAssembler.createDto(uvaverprobung);
	}

	public void createDefaultSteuerkategoriekonto(Integer steuerkategorieIId,
			TheClientDto theClientDto) {
		MwstsatzbezDto[] mwstsatzbezDtos = getMandantFac()
				.mwstsatzbezFindAllByMandantAsDto(theClientDto.getMandant(),
						theClientDto);
		if (mwstsatzbezDtos.length > 0) {
			for (int i = 0; i < mwstsatzbezDtos.length; i++) {
				// if (!mwstsatzbezDtos[i].getBHandeingabe()) {
				SteuerkategoriekontoDto steuerkategoriekontoDto = new SteuerkategoriekontoDto();
				steuerkategoriekontoDto
						.setSteuerkategorieIId(steuerkategorieIId);
				steuerkategoriekontoDto.setMwstsatzbezIId(mwstsatzbezDtos[i]
						.getIId());
				createSteuerkategoriekonto(steuerkategoriekontoDto,
						theClientDto);
				// }
			}
		}
	}

	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(String nr,
			Integer finanzamtIId, TheClientDto theClient) {
		Query query = em
				.createNamedQuery("SteuerkategorieByCNrFinanzamtIIDMandant");
		query.setParameter(1, nr);
		query.setParameter(2, finanzamtIId);
		query.setParameter(3, theClient.getMandant());
		Steuerkategorie steuerkategorie = null;
		try {
			steuerkategorie = (Steuerkategorie) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		return SteuerkategorieDtoAssembler.createDto(steuerkategorie);
	}

	public SteuerkategorieDto[] steuerkategorieFindByFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClient) {
		Query query = em
				.createNamedQuery("SteuerkategorieByFinanzamtIIDMandant");
		query.setParameter(1, finanzamtIId);
		query.setParameter(2, theClient.getMandant());

		SteuerkategorieDto[] result = null;

		try {
			Collection<?> cl = query.getResultList();
			result = SteuerkategorieDtoAssembler.createDtos(cl);
		} catch (NoResultException e) {
		}

		return result;
	}

	private SteuerkategorieDto setupDefaultSteuerkategorie(String cnr,
			String cBez, int reverseCharge) {
		SteuerkategorieDto dto = new SteuerkategorieDto();
		dto.setCNr(cnr);
		dto.setCBez(cBez);
		dto.setBReversecharge((short) reverseCharge);
		return dto;
	}

	public void createIfNeededSteuerkategorieForFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClientDto) {
		SteuerkategorieDto[] kategorien = steuerkategorieFindByFinanzamtIId(
				finanzamtIId, theClientDto);
		if (kategorien != null && kategorien.length > 0)
			return;

		// Die Standardwerte werden nur installiert wenn noch gar nichts
		// vorhanden ist
		kategorien = new SteuerkategorieDto[6];
		kategorien[0] = setupDefaultSteuerkategorie("Inland", "Inland", 0);
		kategorien[1] = setupDefaultSteuerkategorie("InlandReverse",
				"Inland Reverse Charge", 1);
		kategorien[2] = setupDefaultSteuerkategorie("AuslandEUmUID",
				"Ausland EU mit UID", 0);
		kategorien[3] = setupDefaultSteuerkategorie("AuslandEUoUID",
				"Ausland EU ohne UID", 0);
		kategorien[4] = setupDefaultSteuerkategorie("Ausland",
				"Ausland nicht EU", 0);
		kategorien[5] = setupDefaultSteuerkategorie("AuslandReverse",
				"Ausland Reverse Charge", 1);

		Integer ids[] = new Integer[6];

		for (int i = 0; i < kategorien.length; i++) {
			kategorien[i].setFinanzamtIId(finanzamtIId);
			ids[i] = createSteuerkategorie(kategorien[i], theClientDto);
		}
	}

	private void createIfNeededSteuerkategorieKontoForMwstsatzbez(
			SteuerkategorieDto[] kategorien, MwstsatzbezDto[] mwstsatzbez,
			TheClientDto theClientDto) {
		for (SteuerkategorieDto steuerkategorieDto : kategorien) {
			for (MwstsatzbezDto mwstsatzbezDto : mwstsatzbez) {
				try {
					steuerkategoriekontoFindByPrimaryKey(
							steuerkategorieDto.getIId(),
							mwstsatzbezDto.getIId());
				} catch (EJBExceptionLP ex) {
					SteuerkategoriekontoDto newKonto = new SteuerkategoriekontoDto();
					newKonto.setSteuerkategorieIId(steuerkategorieDto.getIId());
					newKonto.setMwstsatzbezIId(mwstsatzbezDto.getIId());
					createSteuerkategoriekonto(newKonto, theClientDto);
				}
			}
		}
	}

	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(
			Integer finanzamtIId, TheClientDto theClientDto) {
		SteuerkategorieDto[] kategorien = steuerkategorieFindByFinanzamtIId(
				finanzamtIId, theClientDto);
		if (null == kategorien || kategorien.length == 0) {
			createIfNeededSteuerkategorieForFinanzamtIId(finanzamtIId,
					theClientDto);
			kategorien = steuerkategorieFindByFinanzamtIId(finanzamtIId,
					theClientDto);
		}
		if (null == kategorien)
			return;

		// for (SteuerkategorieDto steuerkategorieDto : kategorien) {
		// createDefaultSteuerkategoriekonto(steuerkategorieDto.getIId(),
		// theClientDto) ;
		// }

		MwstsatzbezDto[] mwstsatzbez = getMandantFac()
				.mwstsatzbezFindAllByMandantAsDto(theClientDto.getMandant(),
						theClientDto);

		createIfNeededSteuerkategorieKontoForMwstsatzbez(kategorien,
				mwstsatzbez, theClientDto);
	}

	public Integer createAuszifferung(Integer[] buchungdetailIds,
			TheClientDto theClient) {
		List<Buchungdetail> buchungdetails = new ArrayList<Buchungdetail>();

		if (buchungdetailIds.length == 0)
			return null;

		Integer azk = null;
		for (Integer iid : buchungdetailIds) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iid);
			if (buchungdetail == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						"Nicht gefunden: Buchungdetail ID " + iid);
			}
			Buchung buchung = em.find(Buchung.class,
					buchungdetail.getBuchungIId());
			if (buchung.getTStorniert() != null)
				return null;
			if (azk == null) {
				azk = buchungdetail.getIAusziffern();
			} else if (buchungdetail.getIAusziffern() != null
					&& !azk.equals(buchungdetail.getIAusziffern())) {
				return null; // es gibt bereits 2 verschiedene AZK
			}
			buchungdetails.add(buchungdetail);
		}

		if (azk == null) {
			Query query = em.createNamedQuery("BuchungdetailMaxIAusziffern");
			query.setParameter(1, buchungdetails.get(0).getKontoIId());
			azk = (Integer) query.getSingleResult();
			if (azk == null)
				azk = new Integer(0);
			azk += 1;
		}
		for (Buchungdetail buchungdetail : buchungdetails) {
			if (buchungdetail != null) {
				buchungdetail.setIAusziffern(azk);
				em.merge(buchungdetail);
			}
		}
		em.flush();
		return azk;
	}

	public void removeAuszifferung(Integer[] buchungdetailIds,
			TheClientDto theClient) {
		for (int i = 0; i < buchungdetailIds.length; i++) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class,
					buchungdetailIds[i]);
			if (buchungdetail != null) {
				buchungdetail.setIAusziffern(null);
				em.merge(buchungdetail);
			}
		}
		em.flush();
	}

	public Integer updateAuszifferung(Integer auszifferKennzeichen,
			Integer[] buchungdetailIds, TheClientDto theClient) {
		if (auszifferKennzeichen == null) {
			// SP 2013/01093
			// aus erster Buchung mit Kennzeichen holen
			for (int i = 0; i < buchungdetailIds.length; i++) {
				Buchungdetail buchungdetail = em.find(Buchungdetail.class,
						buchungdetailIds[i]);
				if (buchungdetail == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
							"Nicht gefunden: Buchungdetail ID "
									+ buchungdetailIds[i]);
				}
				auszifferKennzeichen = buchungdetail.getIAusziffern();
				if (auszifferKennzeichen != null)
					break;
			}
		}
		for (int i = 0; i < buchungdetailIds.length; i++) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class,
					buchungdetailIds[i]);
			if (buchungdetail != null) {
				if ((buchungdetail.getIAusziffern() == null)
						|| (buchungdetail.getIAusziffern().compareTo(
								auszifferKennzeichen) != 0)) {
					buchungdetail.setIAusziffern(auszifferKennzeichen);
					em.merge(buchungdetail);
				}
			}
		}
		em.flush();
		return auszifferKennzeichen;
	}

	public boolean isFibuLandunterschiedlich(Integer partner1IId,
			Integer partner2IId) {
		Partner partner1 = em.find(Partner.class, partner1IId);
		Partner partner2 = em.find(Partner.class, partner2IId);
		Integer land1IId = null;
		Integer land2IId = null;

		if (partner1.getLandIIdAbweichendesustland() != null)
			land1IId = partner1.getLandIIdAbweichendesustland();
		else
			land1IId = getSystemFac().getPartnerLandIId(partner1IId);

		if (partner2.getLandIIdAbweichendesustland() != null)
			land2IId = partner2.getLandIIdAbweichendesustland();
		else
			land2IId = getSystemFac().getPartnerLandIId(partner2IId);

		return (land1IId.compareTo(land2IId) != 0);
	}

	public UvaverprobungDto uvaVerprobungFindbyFinanzamtIIdGeschaeftsjahrPeriodeAbrechnungszeitraumMandant(
			Integer finanzamtIId, int iGeschaeftsjahr, int iPeriode,
			int iAbrechnungszeitraum, TheClientDto theClientDto) {
		HvTypedQuery<Uvaverprobung> query = new HvTypedQuery<Uvaverprobung>(
				em.createNamedQuery("UvaverprobungfindByGeschaeftsjahrMonatFinanzamtMandant"));
		query.setParameter(1, iGeschaeftsjahr);
		query.setParameter(2, iPeriode + iAbrechnungszeitraum);
		query.setParameter(3, finanzamtIId);
		query.setParameter(4, theClientDto.getMandant());

		List<Uvaverprobung> list = query.getResultList();
		if (list == null || list.size() != 1)
			return null;
		return assembleUvaverprobungDto(list.get(0));
	}

	@Override
	public List<SteuerkategoriekontoDto> steuerkategorieFindByKontoIIdMwstSatzBezIId(
			Integer kontoIId, Integer mwstSatzBezIId) {
		HvTypedQuery<Steuerkategoriekonto> query = new HvTypedQuery<Steuerkategoriekonto>(
				em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
		query.setParameter(1, kontoIId).setParameter(2, mwstSatzBezIId);
		return Arrays.asList(assembleSteuerkategoriekontoDtos(query
				.getResultList()));
	}

	@Override
	public BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId,
			TheClientDto theClientDto) {

		if (getBenutzerServicesFac().hatRecht(RechteFac.RECHT_FB_FINANZ_R,
				theClientDto)
				|| getBenutzerServicesFac().hatRecht(
						RechteFac.RECHT_FB_FINANZ_CUD, theClientDto)) {
			HvTypedQuery<Bankverbindung> query = new HvTypedQuery<Bankverbindung>(
					em.createNamedQuery(Bankverbindung.BankverbindungFindForLiquiditaetsvorschau));
			query.setParameter("mandant", theClientDto.getMandant());
			List<Bankverbindung> bankverbindungen = query.getResultList();
			BigDecimal saldo = BigDecimal.ZERO;
			for (Bankverbindung bv : bankverbindungen) {
				saldo = saldo.add(getBuchenFac()
						.getAktuellenSaldoVonKontoFuerGeschaeftsjahr(
								bv.getKontoIId(), geschaeftsjahrIId));
			}
			return saldo;
		} else {
			// SP18971
			return null;
		}

	}

}
