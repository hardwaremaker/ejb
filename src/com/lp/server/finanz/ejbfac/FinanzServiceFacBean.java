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
import java.util.LinkedHashMap;
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

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;
import com.lp.server.eingangsrechnung.ejb.Eingangsrechnungzahlung;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungzahlungDto;
import com.lp.server.finanz.assembler.BuchungsartDtoAssembler;
import com.lp.server.finanz.assembler.BuchungsartsprDtoAssembler;
import com.lp.server.finanz.assembler.KontoartDtoAssembler;
import com.lp.server.finanz.assembler.KontoartsprDtoAssembler;
import com.lp.server.finanz.assembler.KontotypDtoAssembler;
import com.lp.server.finanz.assembler.KontotypsprDtoAssembler;
import com.lp.server.finanz.assembler.LaenderartDtoAssembler;
import com.lp.server.finanz.assembler.LaenderartsprDtoAssembler;
import com.lp.server.finanz.assembler.MahnspesenDtoAssembler;
import com.lp.server.finanz.assembler.MahntextDtoAssembler;
import com.lp.server.finanz.assembler.ReversechargeartDtoAssembler;
import com.lp.server.finanz.assembler.ReversechargeartsprDtoAssembler;
import com.lp.server.finanz.assembler.SteuerkategorieDtoAssembler;
import com.lp.server.finanz.assembler.SteuerkategoriekontoDtoAssembler;
import com.lp.server.finanz.assembler.UvaFormularDtoAssembler;
import com.lp.server.finanz.assembler.UvaartDtoAssembler;
import com.lp.server.finanz.assembler.UvaartsprDtoAssembler;
import com.lp.server.finanz.assembler.UvaverprobungDtoAssembler;
import com.lp.server.finanz.assembler.WarenverkehrsnummerDtoAssembler;
import com.lp.server.finanz.bl.FinanzValidator;
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
import com.lp.server.finanz.ejb.Reversechargeart;
import com.lp.server.finanz.ejb.ReversechargeartISort;
import com.lp.server.finanz.ejb.ReversechargeartQuery;
import com.lp.server.finanz.ejb.Reversechargeartspr;
import com.lp.server.finanz.ejb.ReversechargeartsprPK;
import com.lp.server.finanz.ejb.Steuerkategorie;
import com.lp.server.finanz.ejb.Steuerkategoriekonto;
import com.lp.server.finanz.ejb.SteuerkategoriekontoQuery;
import com.lp.server.finanz.ejb.UstUebersetzung;
import com.lp.server.finanz.ejb.UstUebersetzungQuery;
import com.lp.server.finanz.ejb.UvaFormular;
import com.lp.server.finanz.ejb.UvaFormularQuery;
import com.lp.server.finanz.ejb.Uvaart;
import com.lp.server.finanz.ejb.Uvaartspr;
import com.lp.server.finanz.ejb.UvaartsprPK;
import com.lp.server.finanz.ejb.Uvaverprobung;
import com.lp.server.finanz.ejb.Warenverkehrsnummer;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzLaenderart;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzWarenverkehrsnummer;
import com.lp.server.finanz.fastlanereader.generated.FLRSteuerkategoriekonto;
import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.server.finanz.service.BucheBelegPeriodeInfoDto;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.BuchungsartDto;
import com.lp.server.finanz.service.BuchungsartsprDto;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoVerifierEntry;
import com.lp.server.finanz.service.KontoartDto;
import com.lp.server.finanz.service.KontoartsprDto;
import com.lp.server.finanz.service.KontotypDto;
import com.lp.server.finanz.service.KontotypsprDto;
import com.lp.server.finanz.service.LaenderartDto;
import com.lp.server.finanz.service.LaenderartsprDto;
import com.lp.server.finanz.service.MahnspesenDto;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahntextDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.ReversechargeartDto;
import com.lp.server.finanz.service.ReversechargeartsprDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.finanz.service.SteuerkategorieEntry;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.finanz.service.UstUebersetzungDto;
import com.lp.server.finanz.service.UstUebersetzungDtoAssembler;
import com.lp.server.finanz.service.UvaFormularDto;
import com.lp.server.finanz.service.UvaartDto;
import com.lp.server.finanz.service.UvaartsprDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.finanz.service.WarenverkehrsnummerDto;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.ejb.Rechnungposition;
import com.lp.server.rechnung.ejb.RechnungpositionQuery;
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
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
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
import com.lp.server.util.HvOptional;
import com.lp.server.util.SteuerkategoriekontoId;
import com.lp.server.util.UvaFormularId;
import com.lp.server.util.UvaartId;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.service.DatenspracheIf;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class FinanzServiceFacBean extends Facade implements FinanzServiceFac {
	@PersistenceContext
	private EntityManager em;

	public KontotypsprDto kontotypsprFindByPrimaryKey(String kontotypCNr, String localeCNr) throws EJBExceptionLP {
		try {
			KontotypsprPK kontotypsprPK = new KontotypsprPK();
			kontotypsprPK.setKontotypCNr(kontotypCNr);
			kontotypsprPK.setLocaleCNr(localeCNr);
			Kontotypspr kontotypspr = em.find(Kontotypspr.class, kontotypsprPK);
			if (kontotypspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKontotypsprDto(kontotypspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	public Integer createMahnspesen(MahnspesenDto dto) {

		try {
			Query query = em.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
			query.setParameter(1, dto.getIMahnstufe());
			query.setParameter(2, dto.getMandantCNr());
			query.setParameter(3, dto.getWaehrungCNr());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_MAHNSPESEN.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_MAHNSPESEN);
			dto.setIId(pk);

			Mahnspesen bean = new Mahnspesen(dto.getIId(), dto.getMandantCNr(), dto.getIMahnstufe(),
					dto.getWaehrungCNr(), dto.getNMahnspesen());
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
			Query query = em.createNamedQuery("MahnspesenFindByIMahnstufeMandantCNrWaehrungCNr");
			query.setParameter(1, dto.getIMahnstufe());
			query.setParameter(2, dto.getMandantCNr());
			query.setParameter(3, dto.getWaehrungCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Mahnspesen) query.getSingleResult()).getIId();
			if (ialle.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_MAHNSPESEN.UK"));
			}
		} catch (NoResultException ex) {

		}

		setMahnspesenFromMahnspesenDto(ialle, dto);
	}

	private KontotypsprDto assembleKontotypsprDto(Kontotypspr kontotypspr) {
		return KontotypsprDtoAssembler.createDto(kontotypspr);
	}

	public KontotypDto kontotypFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		Kontotyp kontotyp = em.find(Kontotyp.class, cNr);
		if (kontotyp == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleKontotypDto(kontotyp);
	}

	private void setKontotypFromKontotypDto(Kontotyp kontotyp, KontotypDto kontotypDto) {
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

	public UvaartDto uvaartFindByPrimaryKey(Integer iId, TheClientDto theClientDto) throws EJBExceptionLP {
		Validator.notNull(iId, "iId");
		Uvaart uvaart = em.find(Uvaart.class, iId);

		if (uvaart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		UvaartDto uvaartDto = assembleUvaartDto(uvaart);

		UvaartsprDto sprDto = getUvaartsprDto(iId, theClientDto.getLocUi(), theClientDto.getLocMandant());

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
		uvaart.setBKeineAuswahlBeiEr(uvaartDto.getBKeineAuswahlBeiEr());
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

	public UvaartsprDto uvaartsprFindByPrimaryKey(Integer uvaartIId,String localeCNr) throws EJBExceptionLP {
		try {
			UvaartsprPK uvaartsprPK = new UvaartsprPK(uvaartIId,localeCNr);
			Uvaartspr uvaartspr = em.find(Uvaartspr.class, uvaartsprPK);
			if (uvaartspr == null) {
				return null;
			}
			return assembleUvaartsprDto(uvaartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void setUvaartsprFromUvaartsprDto(Uvaartspr uvaartspr, UvaartsprDto uvaartsprDto) {
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleKontoartsprDto(kontoartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}
	}

	private void setSteuerkategorieFromSteuerkategorieDto(Steuerkategorie steuerkategorie,
			SteuerkategorieDto steuerkategorieDto) {
		steuerkategorie.setISort(steuerkategorieDto.getISort());
		steuerkategorie.setCNr(steuerkategorieDto.getCNr());
		steuerkategorie.setCBez(steuerkategorieDto.getCBez());
		steuerkategorie.setISort(steuerkategorieDto.getISort());
		steuerkategorie.setKontoIIdForderungen(steuerkategorieDto.getKontoIIdForderungen());
		steuerkategorie.setKontoIIdVerbindlichkeiten(steuerkategorieDto.getKontoIIdVerbindlichkeiten());
		steuerkategorie.setKontoIIdKursgewinn(steuerkategorieDto.getKontoIIdKursgewinn());
		steuerkategorie.setKontoIIdKursverlust(steuerkategorieDto.getKontoIIdKursverlust());
		steuerkategorie.setReversechargeartIId(steuerkategorieDto.getReversechargeartId());
		steuerkategorie.setbReversecharge(Helper.getShortFalse());
		em.merge(steuerkategorie);
		em.flush();
	}

	private void setSteuerkategoriekontoFromSteuerkategoriekontoDto(Steuerkategoriekonto steuerkategoriekonto,
			SteuerkategoriekontoDto steuerkategoriekontoDto) {
		steuerkategoriekonto.setSteuerkategorieIId(steuerkategoriekontoDto.getSteuerkategorieIId());
		steuerkategoriekonto.setMwstsatzbezIId(steuerkategoriekontoDto.getMwstsatzbezIId());
		steuerkategoriekonto.setTGueltigAb(steuerkategoriekontoDto.getTGueltigAb());
		steuerkategoriekonto.setKontoIIdVk(steuerkategoriekontoDto.getKontoIIdVk());
		steuerkategoriekonto.setKontoIIdEk(steuerkategoriekontoDto.getKontoIIdEk());
		steuerkategoriekonto.setKontoIIdSkontoVk(steuerkategoriekontoDto.getKontoIIdSkontoVk());
		steuerkategoriekonto.setKontoIIdSkontoEk(steuerkategoriekontoDto.getKontoIIdSkontoEk());
		steuerkategoriekonto.setKontoIIdEinfuhrUst(steuerkategoriekontoDto.getKontoIIdEinfuhrUst());
		em.merge(steuerkategoriekonto);
		em.flush();
	}

	private void setKontoartsprFromKontoartsprDto(Kontoartspr kontoartspr, KontoartsprDto kontoartsprDto) {
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

	@Override
	public KontoartDto kontoartFindByPrimaryKeyOhneExc(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		Kontoart kontoart = em.find(Kontoart.class, cNr);
		if (kontoart == null) {
			return null;
		}

		KontoartDto kontoartDto = assembleKontoartDto(kontoart);

		KontoartsprDto sprDto = getKontoartSprDto(cNr, theClientDto.getLocUi(), theClientDto.getLocMandant());

		kontoartDto.setKontoartsprDto(sprDto);

		return kontoartDto;
	}

	public KontoartDto kontoartFindByPrimaryKey(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		KontoartDto kontoartDto = kontoartFindByPrimaryKeyOhneExc(cNr, theClientDto);
		if (kontoartDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

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

	private void setKontoartFromKontoartDto(Kontoart kontoart, KontoartDto kontoartDto) {
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

	public String uebersetzeUvaartOptimal(Integer iId, Locale localeUi, Locale localeMandant) {
		Validator.notNull(localeUi, "localeUi");
		Validator.notNull(localeMandant, "localeMandant");

		UvaartsprDto uvaartsprDto = getUvaartsprDto(iId, localeUi, localeMandant);
		return uvaartsprDto.getCBez();
	}

	private UvaartsprDto getUvaartsprDto(Integer iId, Locale localeUi, Locale localeMandant) {

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
		Uvaartspr uvaartspr = em.find(Uvaartspr.class, new UvaartsprPK(iId, Helper.locale2String(locale)));
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

	public String uebersetzeKontoartOptimal(String cNr, Locale localeUi, Locale localeMandant) {
		Validator.notNull(localeUi, "localeUi");
		Validator.notNull(localeMandant, "localeMandant");

		KontoartsprDto kontoartsprDto = getKontoartSprDto(cNr, localeUi, localeMandant);
		return kontoartsprDto.getCBez();
	}

	private KontoartsprDto getKontoartSprDto(String cNr, Locale localeUi, Locale localeMandant) {

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
		Kontoartspr kontoartspr = em.find(Kontoartspr.class, new KontoartsprPK(cNr, Helper.locale2String(locale)));
		return kontoartspr;
	}

	// ***
	// *** wp ***

	/**
	 * Uebersetzt einen Kontotyp optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr     String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return String
	 */
	public String uebersetzeKontotypOptimal(String cNr, Locale locale1, Locale locale2) {
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
	 * @param cNr    String
	 * @param locale Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeKontotyp(String cNr, Locale locale) {
		String cLocale = Helper.locale2String(locale);
		Kontotypspr kontotypspr = em.find(Kontotypspr.class, new KontotypsprPK(cNr, cLocale));
		if (kontotypspr == null) {
			return null;
		}
		return kontotypspr.getCBez();
	}

	public void createKontoart(KontoartDto kontoartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	public Integer createSteuerkategorie(SteuerkategorieDto steuerkategorieDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(steuerkategorieDto, "steuerkategorieDto");
		Validator.notEmpty(steuerkategorieDto.getCNr(), "cnr");
		Validator.notEmpty(steuerkategorieDto.getCBez(), "cbez");
		Validator.notNull(steuerkategorieDto.getReversechargeartId(), "reversechargeartId");

		steuerkategorieDto.setMandantCNr(theClientDto.getMandant());

		try {
			Query query = em.createNamedQuery("SteuerkategorieByCNrReversechargeartIdFinanzamtIIDMandant");
			query.setParameter(1, steuerkategorieDto.getCNr());
			query.setParameter(2, steuerkategorieDto.getReversechargeartId());
			query.setParameter(3, steuerkategorieDto.getFinanzamtIId());
			query.setParameter(4, theClientDto.getMandant());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_STEUERKATEGORIE.UK"));
		} catch (NoResultException ex) {
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STEUERKATEGORIE);
			steuerkategorieDto.setIId(pk);

			Query queryNext = em.createNamedQuery("SteuerkategorieejbSelectNextReihung");
			queryNext.setParameter(1, theClientDto.getMandant());

			Integer i = (Integer) queryNext.getSingleResult();
			i = i == null ? 0 : new Integer(i + 1);

			steuerkategorieDto.setISort(i);
			Steuerkategorie steuerkategorie = new Steuerkategorie(steuerkategorieDto.getIId(),
					steuerkategorieDto.getCNr(), steuerkategorieDto.getMandantCNr(), steuerkategorieDto.getCBez(),
					steuerkategorieDto.getFinanzamtIId(), steuerkategorieDto.getReversechargeartId());
			steuerkategorie.setISort(steuerkategorieDto.getISort());
			em.persist(steuerkategorie);
			em.flush();
			setSteuerkategorieFromSteuerkategorieDto(steuerkategorie, steuerkategorieDto);

			return steuerkategorieDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeSteuerkategorie(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtgru. Es gibt keine Artgru mit der iid " + iId);
		}
		em.remove(steuerkategorie);
		em.flush();

	}

	public void removeSteuerkategoriekonto(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Steuerkategoriekonto steuerkategoriekonto = em.find(Steuerkategoriekonto.class, iId);
		if (steuerkategoriekonto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeArtgru. Es gibt keine Artgru mit der iid " + iId);
		}
		em.remove(steuerkategoriekonto);
		em.flush();

	}

	public SteuerkategorieDto steuerkategorieFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}
		Steuerkategorie steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei steuerkategorie FindByPrimaryKey. Es gibt keine steuerkategorie mit iid " + iId);
		}
		SteuerkategorieDto steuerkategorieDto = assembleSteuerkategorieDto(steuerkategorie);
		return steuerkategorieDto;
	}

	public void updateSteuerkategorie(SteuerkategorieDto steuerkategorieDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(steuerkategorieDto, "steuerkategorieDto");
		Validator.pkFieldNotNull(steuerkategorieDto.getIId(), "iid");
		Validator.notEmpty(steuerkategorieDto.getCNr(), "cnr");
		Validator.notEmpty(steuerkategorieDto.getCBez(), "cbez");
		Validator.notNull(steuerkategorieDto.getFinanzamtIId(), "finanzamtIId");
		Validator.notNull(steuerkategorieDto.getReversechargeartId(), "reversechargeartIId");

		Integer iId = steuerkategorieDto.getIId();
		Steuerkategorie steuerkategorie = null;
		// try {
		steuerkategorie = em.find(Steuerkategorie.class, iId);
		if (steuerkategorie == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateSteuerkategorie. Es gibt keine Steuerkategorie mit iid " + iId);
		}

		// duplicateunique: Pruefung: Steuerkategorie bereits vorhanden.
		try {
			Query query = em.createNamedQuery("SteuerkategorieByCNrReversechargeartIdFinanzamtIIDMandant");
			query.setParameter(1, steuerkategorieDto.getCNr());
			query.setParameter(2, steuerkategorieDto.getReversechargeartId());
			query.setParameter(3, steuerkategorieDto.getFinanzamtIId());
			query.setParameter(4, theClientDto.getMandant());
			Steuerkategorie doppelt = (Steuerkategorie) query.getSingleResult();
			if (doppelt.getIId().compareTo(iId) != 0)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("FB_STEUERKATEGORIE.UK"));
		} catch (NoResultException ex) {
			//
		}

		setSteuerkategorieFromSteuerkategorieDto(steuerkategorie, steuerkategorieDto);

	}

	@Override
	public SteuerkategoriekontoId createSteuerkategoriekonto(SteuerkategoriekontoDto steuerkategoriekontoDto,
			TheClientDto theClientDto) {
		Validator.dtoNotNull(steuerkategoriekontoDto, "steuerkategoriekontoDto");
		Validator.pkFieldNotNull(steuerkategoriekontoDto.getSteuerkategorieIId(), "steuerkategorieId");
		Validator.pkFieldNotNull(steuerkategoriekontoDto.getMwstsatzbezIId(), "mwstsatzbezId");
		Validator.notNull(steuerkategoriekontoDto.getTGueltigAb(), "gueltigAb");

		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		if (q.find(steuerkategoriekontoDto.getSteuerkategorieIId(), steuerkategoriekontoDto.getMwstsatzbezIId(),
				steuerkategoriekontoDto.getTGueltigAb()).isPresent()) {
			throw EJBExcFactory.duplicateUniqueKey("FB_STEUERKATEGORIEKONTO",
					steuerkategoriekontoDto.getSteuerkategorieIId(), steuerkategoriekontoDto.getMwstsatzbezIId(),
					steuerkategoriekontoDto.getTGueltigAb());
		}

		try {
			PKGeneratorObj pkGen = new PKGeneratorObj();
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_STEUERKATEGORIEKONTO);
			Steuerkategoriekonto steuerkategoriekonto = new Steuerkategoriekonto(pk);
			steuerkategoriekonto.setSteuerkategorieIId(steuerkategoriekontoDto.getSteuerkategorieIId());
			steuerkategoriekonto.setMwstsatzbezIId(steuerkategoriekontoDto.getMwstsatzbezIId());
			steuerkategoriekonto.setTGueltigAb(steuerkategoriekontoDto.getTGueltigAb());
			em.persist(steuerkategoriekonto);
			em.flush();

			setSteuerkategoriekontoFromSteuerkategoriekontoDto(steuerkategoriekonto, steuerkategoriekontoDto);

			return new SteuerkategoriekontoId(pk);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	/*
	 * public void removeSteuerkategoriekonto(Integer steuerkategorieiId) {
	 * Validator.pkFieldNotNull(steuerkategorieiId, "steuerkategorieiId");
	 * 
	 * Steuerkategoriekonto steuerkategoriekonto = em.find(
	 * Steuerkategoriekonto.class, steuerkategorieiId); if (steuerkategoriekonto ==
	 * null) { throw new EJBExceptionLP( EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
	 * "Fehler bei removeSteuerkategoriekonto. Es gibt keine Steuerkategoriekonto mit der steuerkategorieiId "
	 * + steuerkategorieiId); } em.remove(steuerkategoriekonto); em.flush();
	 * 
	 * }
	 */

	@Override
	public HvOptional<SteuerkategoriekontoDto> steuerkategoriekontoZuDatum(Integer steuerkategorieIId,
			Integer mwstsatzbezIId, Timestamp gueltigAm) {
		Validator.pkFieldNotNull(steuerkategorieIId, "steuerkategorieIId");
		Validator.pkFieldNotNull(mwstsatzbezIId, "mwstsatzbezIId");
		Validator.notNull(gueltigAm, "gueltigAm");

		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		HvOptional<Steuerkategoriekonto> entry = q.findZuDatum(steuerkategorieIId, mwstsatzbezIId, gueltigAm);
		if (entry.isPresent()) {
			return HvOptional.of(assembleSteuerkategoriekontoDto(entry.get()));
		}

		return HvOptional.empty();
	}

	@Override
	public SteuerkategoriekontoDto steuerkategoriekontoZuDatumValidate(Integer steuerkategorieIId,
			Integer mwstsatzbezIId, Timestamp gueltigAm) {
		HvOptional<SteuerkategoriekontoDto> dto = steuerkategoriekontoZuDatum(steuerkategorieIId, mwstsatzbezIId,
				gueltigAm);
		if (!dto.isPresent()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Es gibt kein steuerkategoriekonto mit steuerkategorieId " + steuerkategorieIId + ", mwstsatzbezId "
							+ mwstsatzbezIId + ", timestamp " + gueltigAm + ".");
		}
		return dto.get();
	}

	@Override
	public SteuerkategoriekontoDto steuerkategoriekontoFindByPrimaryKey(SteuerkategoriekontoId kontoId) {
		Validator.notNull(kontoId, "kontoId");

		Steuerkategoriekonto konto = em.find(Steuerkategoriekonto.class, kontoId.id());
		Validator.entityFound(konto, kontoId.id());
		return assembleSteuerkategoriekontoDto(konto);
	}

	@Override
	public SteuerkategoriekontoDto steuerkategoriekontoFindByPrimaryKey(Integer steuerkategorieIId,
			Integer mwstsatzbezIId, Timestamp gueltigAb) {
		Validator.pkFieldNotNull(steuerkategorieIId, "steuerkategorieIId");
		Validator.pkFieldNotNull(mwstsatzbezIId, "mwstsatzbezIId");
		Validator.notNull(gueltigAb, "gueltigAb");

		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		HvOptional<Steuerkategoriekonto> entry = q.find(steuerkategorieIId, mwstsatzbezIId, gueltigAb);
		if (!entry.isPresent()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Es gibt kein steuerkategoriekonto mit steuerkategorieId " + steuerkategorieIId + ", mwstsatzbezId "
							+ mwstsatzbezIId + ", timestamp " + gueltigAb + ".");
		}
		SteuerkategoriekontoDto steuerkategoriekontoDto = assembleSteuerkategoriekontoDto(entry.get());
		return steuerkategoriekontoDto;
	}

	public SteuerkategoriekontoDto[] steuerkategoriekontoFindAll(Integer steuerkategorieId) throws EJBExceptionLP {
		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		List<Steuerkategoriekonto> entries = q.listSteuerkategorieAll(steuerkategorieId);
		return assembleSteuerkategoriekontoDtos(entries);		

//		return buildSteuerkategoriekontoDtos(entries);
//		return steuerkategoriekontoAllZuDatum(steuerkategorieId, Helper.cut());
		/*
		 * // TODO SP8308: In welchem Kontext verwendet? Ueber alle mwstsatzbez und
		 * Datum? Query query = em
		 * .createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		 * query.setParameter(1, steuerkategorieIId); // Object test =
		 * query.getSingleResult(); Collection<?> cl = query.getResultList(); return
		 * assembleSteuerkategoriekontoDtos(cl);
		 */
	}

	@Override
	public SteuerkategoriekontoDto[] steuerkategoriekontoAllZuDatum(Integer steuerkategorieId, Timestamp gueltigZum) {
		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		List<Steuerkategoriekonto> entries = q.listSteuerkategorie(steuerkategorieId, gueltigZum);
		return buildSteuerkategoriekontoDtos(entries);
		
/*		
		LinkedHashMap lhm = new LinkedHashMap();

		Iterator<?> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Steuerkategoriekonto zeile = (Steuerkategoriekonto) iterator.next();
			lhm.put(zeile.getMwstsatzbezIId(), zeile);
		}
		return assembleSteuerkategoriekontoDtos(lhm.values());
*/		
		/*
		 * Query query = em
		 * .createNamedQuery("SteuerkategoriekontoBySteuerkategorieIId");
		 * query.setParameter(1, steuerkategorieIId); // Object test =
		 * query.getSingleResult(); Collection<?> cl = query.getResultList(); return
		 * assembleSteuerkategoriekontoDtos(cl);
		 */
	}

	private SteuerkategoriekontoDto[] buildSteuerkategoriekontoDtos(List<Steuerkategoriekonto> entries) {
		LinkedHashMap lhm = new LinkedHashMap();

		Iterator<?> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Steuerkategoriekonto zeile = (Steuerkategoriekonto) iterator.next();
			lhm.put(zeile.getMwstsatzbezIId(), zeile);
		}
		return assembleSteuerkategoriekontoDtos(lhm.values());		
	}
	
	private SteuerkategoriekontoDto[] assembleSteuerkategoriekontoDtos(Collection<?> steuerkategoriekontos) {
		return SteuerkategoriekontoDtoAssembler.createDtos(steuerkategoriekontos);
	}

	public void updateSteuerkategoriekonto(SteuerkategoriekontoDto steuerkategoriekontoDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(steuerkategoriekontoDto, "steuerkategoriekontoDto");
		Validator.pkFieldNotNull(steuerkategoriekontoDto.getSteuerkategorieIId(), "steuerkategorieId");
		Validator.pkFieldNotNull(steuerkategoriekontoDto.getMwstsatzbezIId(), "mwstsatzbezId");
		Validator.notNull(steuerkategoriekontoDto.getTGueltigAb(), "gueltigAb");
		/*
		 * SteuerkategoriekontoPK steuerkategoriekontoPK = new SteuerkategoriekontoPK(
		 * steuerkategoriekontoDto.getSteuerkategorieIId(),
		 * steuerkategoriekontoDto.getMwstsatzbezIId());
		 */
		Steuerkategoriekonto steuerkategoriekonto = em.find(Steuerkategoriekonto.class,
				steuerkategoriekontoDto.getId().id());
		Validator.entityFound(steuerkategoriekonto, steuerkategoriekontoDto.getId().id());

		try {
			Map<Integer, String> mwstAll = getMandantFac().mwstsatzbezFindAllByMandant(theClientDto.getMandant(),
					theClientDto);
			SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);

			for (Integer mwstIid : mwstAll.keySet()) {
				if (mwstIid.intValue() == steuerkategoriekontoDto.getMwstsatzbezIId())
					continue;

				if (steuerkategoriekontoDto.getKontoIIdEk() != null) {
					List<Steuerkategoriekonto> entries = q.listKontoId(steuerkategoriekontoDto.getKontoIIdEk(), mwstIid,
							steuerkategoriekontoDto.getTGueltigAb());
					if (entries.size() > 0) {
						throwExceptionFinanzKontoInAndererMwstVerwendet(steuerkategoriekontoDto,
								SteuerkategoriekontoDtoAssembler.createDto(entries.get(0)),
								steuerkategoriekontoDto.getKontoIIdEk(), theClientDto);
					}
				}

				if (steuerkategoriekontoDto.getKontoIIdVk() != null) {
					List<Steuerkategoriekonto> entries = q.listKontoId(steuerkategoriekontoDto.getKontoIIdVk(), mwstIid,
							steuerkategoriekontoDto.getTGueltigAb());
					if (entries.size() > 0) {
						throwExceptionFinanzKontoInAndererMwstVerwendet(steuerkategoriekontoDto,
								SteuerkategoriekontoDtoAssembler.createDto(entries.get(0)),
								steuerkategoriekontoDto.getKontoIIdVk(), theClientDto);
					}
				}
				/*
				 * HvTypedQuery<Steuerkategoriekonto> query; if
				 * (steuerkategoriekontoDto.getKontoIIdEk() != null) { query = new
				 * HvTypedQuery<Steuerkategoriekonto>(
				 * em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
				 * query.setParameter(1, steuerkategoriekontoDto.getKontoIIdEk());
				 * query.setParameter(2, mwstIid); List<Steuerkategoriekonto> result =
				 * query.getResultList(); if (result.size() > 0) {
				 * throwExceptionFinanzKontoInAndererMwstVerwendet( steuerkategoriekontoDto,
				 * SteuerkategoriekontoDtoAssembler .createDto(result.get(0)),
				 * steuerkategoriekontoDto.getKontoIIdEk(), theClientDto); } } if
				 * (steuerkategoriekontoDto.getKontoIIdVk() != null) { query = new
				 * HvTypedQuery<Steuerkategoriekonto>(
				 * em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
				 * query.setParameter(1, steuerkategoriekontoDto.getKontoIIdVk());
				 * query.setParameter(2, mwstIid); List<Steuerkategoriekonto> result =
				 * query.getResultList(); if (result.size() > 0) {
				 * throwExceptionFinanzKontoInAndererMwstVerwendet( steuerkategoriekontoDto,
				 * SteuerkategoriekontoDtoAssembler .createDto(result.get(0)),
				 * steuerkategoriekontoDto.getKontoIIdVk(), theClientDto); } }
				 */
			}
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		setSteuerkategoriekontoFromSteuerkategoriekontoDto(steuerkategoriekonto, steuerkategoriekontoDto);
	}

	private void throwExceptionFinanzKontoInAndererMwstVerwendet(SteuerkategoriekontoDto kat1,
			SteuerkategoriekontoDto kat2, Integer kontoIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		SteuerkategorieDto stk1 = steuerkategorieFindByPrimaryKey(kat1.getSteuerkategorieIId(), theClientDto);
		SteuerkategorieDto stk2 = steuerkategorieFindByPrimaryKey(kat2.getSteuerkategorieIId(), theClientDto);
		MwstsatzbezDto mwst1 = getMandantFac().mwstsatzbezFindByPrimaryKey(kat1.getMwstsatzbezIId(), theClientDto);
		MwstsatzbezDto mwst2 = getMandantFac().mwstsatzbezFindByPrimaryKey(kat2.getMwstsatzbezIId(), theClientDto);
		KontoDto konto = getFinanzFac().kontoFindByPrimaryKey(kontoIId);

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET,
				"FEHLER_FINANZ_KONTO_IN_ANDERER_MWST_VERWENDET", konto.getCNr() + ", " + konto.getCBez(),
				stk1.getCBez(), mwst1.getCBezeichnung(), stk2.getCBez(), mwst2.getCBezeichnung());
	}

	public void createKontotyp(KontotypDto kontotypDto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	public Integer createUvaart(UvaartDto uvaartDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(uvaartDto, "uvaartDto");

		try {
			Uvaart uvaart = new Uvaart(uvaartDto.getCNr());
			if (uvaartDto.getIId() == null) {
				Integer iid = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_UVAART);
				uvaartDto.setIId(iid);
				uvaart.setIId(iid);
			}
			setUvaartFromUvaartDto(uvaart, uvaartDto);
			return uvaartDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createMahntext(MahntextDto mahntextDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahntextDto);
		if (mahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("mahntextDto == null"));
		}
		mahntextDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_MAHNTEXT));
		mahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		mahntextDto.setMandantCNr(theClientDto.getMandant());
		// wenns dazu noch keine mahnstufe gibt muss ich die kreieren
		try {
			MahnstufeDto mahnstufeDto = getMahnwesenFac().mahnstufeFindByPrimaryKey(
					new MahnstufePK(mahntextDto.getMahnstufeIId(), mahntextDto.getMandantCNr()));
			if (mahnstufeDto == null) {
				mahnstufeDto = new MahnstufeDto();
				mahnstufeDto.setIId(mahntextDto.getMahnstufeIId());
				mahnstufeDto.setITage(new Integer(10));
				getMahnwesenFac().createMahnstufe(mahnstufeDto, theClientDto);
			}
			Mahntext mahntext = new Mahntext(mahntextDto.getIId(), mahntextDto.getMandantCNr(),
					mahntextDto.getLocaleCNr(), mahntextDto.getMahnstufeIId(), mahntextDto.getCTextinhalt());
			setMahntextFromMahntextDto(mahntext, mahntextDto);
			return mahntextDto.getIId();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMahntext(MahntextDto mahntextDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahntextDto);
		if (mahntextDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("mahntextDto == null"));
		}
		// try {
		if (mahntextDto.getMahnstufeIId().intValue() == 1 || mahntextDto.getMahnstufeIId().intValue() == 2
				|| mahntextDto.getMahnstufeIId().intValue() == 3) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
					new Exception(
							"Mahnstufe " + mahntextDto.getMahnstufeIId().intValue() + " darf nicht geloescht werden"));
		}
		Integer iId = mahntextDto.getIId();
		Mahntext toRemove = em.find(Mahntext.class, iId);
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
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	public void updateMahntext(MahntextDto mahntextDto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	public MahntextDto mahntextFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		try {
			Mahntext mahntext = em.find(Mahntext.class, iId);
			if (mahntext == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleMahntextDto(mahntext);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setMahntextFromMahntextDto(Mahntext mahntext, MahntextDto mahntextDto) {
		mahntext.setMandantCNr(mahntextDto.getMandantCNr());
		mahntext.setLocaleCNr(mahntextDto.getLocaleCNr());
		mahntext.setMahnstufeIId(mahntextDto.getMahnstufeIId());
		mahntext.setXTextinhalt(mahntextDto.getCTextinhalt());
		em.merge(mahntext);
		em.flush();
	}

	private void setMahnspesenFromMahnspesenDto(Mahnspesen mahnspesen, MahnspesenDto mahnspesenDto) {
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

	public MahntextDto createDefaultMahntext(Integer mahnstufeIId, String sTextinhaltI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		MahntextDto oMahntextDto = new MahntextDto();
		oMahntextDto.setMahnstufeIId(mahnstufeIId);
		oMahntextDto.setLocaleCNr(theClientDto.getLocUiAsString());
		oMahntextDto.setMandantCNr(theClientDto.getMandant());
		oMahntextDto.setCTextinhalt(sTextinhaltI);
		oMahntextDto.setIId(createMahntext(oMahntextDto, theClientDto));
		return oMahntextDto;
	}

	public MahntextDto mahntextFindByMandantLocaleCNr(String pMandant, String pSprache, Integer mahnstufeIId)
			throws EJBExceptionLP {
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

	public void updateKontoart(KontoartDto kontoartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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
					updateKontoartspr(kontoartDto.getKontoartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public void updateUvaart(UvaartDto uvaartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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
					uvaartDto.getUvaartsprDto().setUvaartIId(iId);
					updateUvaartspr(uvaartDto.getUvaartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	private void updateUvaartspr(UvaartsprDto uvaartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(uvaartsprDto);
		// code begin
		if (uvaartsprDto != null) {
			Integer iId = uvaartsprDto.getUvaartIId();
			String localeCNr = uvaartsprDto.getLocaleCNr();
			if (localeCNr == null) {
				localeCNr = Helper.locale2String(theClientDto.getLocUi());
				uvaartsprDto.setLocaleCNr(localeCNr);
			}
			UvaartsprPK pk = new UvaartsprPK(iId, localeCNr);
			Uvaartspr uvaartspr = em.find(Uvaartspr.class, pk);
			if (uvaartspr == null) {
				// diese Uebersetzung gibt es nocht nicht
				uvaartspr = createUvaartspr(uvaartsprDto, theClientDto);
			}
			setUvaartsprFromUvaartsprDto(uvaartspr, uvaartsprDto);
		}
	}

	private void updateKontoartspr(KontoartsprDto kontoartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	private Uvaartspr createUvaartspr(UvaartsprDto uvaartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// log
		myLogger.logData(uvaartsprDto);
		// code begin
		Uvaartspr uvaartspr = null;
		if (uvaartsprDto != null) {
			try {
				uvaartspr = new Uvaartspr(uvaartsprDto.getUvaartIId(), uvaartsprDto.getLocaleCNr());
				em.persist(uvaartspr);
				em.flush();
				setUvaartsprFromUvaartsprDto(uvaartspr, uvaartsprDto);
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
			}
		}
		return uvaartspr;
	}

	public void createLaenderart(LaenderartDto laenderartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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

	public void removeLaenderart(LaenderartDto laenderartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (laenderartDto != null) {
			String cNr = laenderartDto.getCNr();
			Laenderart toRemove = em.find(Laenderart.class, cNr);
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
		// }
		// catch (Exception e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		// }
	}

	public void updateLaenderart(LaenderartDto laenderartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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
					updateLaenderartspr(laenderartDto.getLaenderartsprDto(), theClientDto);
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			}
		}
	}

	public LaenderartDto laenderartFindByPrimaryKey(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		Laenderart laenderart = em.find(Laenderart.class, cNr);
		if (laenderart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		LaenderartDto laenderartDto = assembleLaenderartDto(laenderart);

		laenderartDto.setLaenderartsprDto(new LaenderartsprDto());
		laenderartDto.getLaenderartsprDto().setLaenderartCNr(cNr);
		laenderartDto.getLaenderartsprDto().setLocaleCNr(theClientDto.getLocUiAsString());
		laenderartDto.getLaenderartsprDto().setCBez(uebersetzeLaenderart(cNr, theClientDto.getLocUi()));
		return laenderartDto;
	}

	private void setLaenderartFromLaenderartDto(Laenderart laenderart, LaenderartDto laenderartDto) {
		laenderart.setISort(laenderartDto.getISort());
		em.merge(laenderart);
		em.flush();
	}

	private LaenderartDto assembleLaenderartDto(Laenderart laenderart) {
		return LaenderartDtoAssembler.createDto(laenderart);
	}

	public void createLaenderartspr(LaenderartsprDto laenderartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// log
		myLogger.logData(laenderartsprDto);
		// code begin
		if (laenderartsprDto == null) {
			return;
		}
		try {
			Laenderartspr laenderartspr = new Laenderartspr(laenderartsprDto.getLaenderartCNr(),
					laenderartsprDto.getLocaleCNr());
			em.persist(laenderartspr);
			em.flush();
			setLaenderartsprFromLaenderartsprDto(laenderartspr, laenderartsprDto);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeLaenderartspr(LaenderartsprDto laenderartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			if (laenderartsprDto != null) {
				String laenderartCNr = laenderartsprDto.getLaenderartCNr();
				String localeCNr = laenderartsprDto.getLocaleCNr();
				Laenderartspr toRemove = em.find(Laenderartspr.class, new LaenderartsprPK(laenderartCNr, localeCNr));
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
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void updateLaenderartspr(LaenderartsprDto laenderartsprDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
			setLaenderartsprFromLaenderartsprDto(laenderartspr, laenderartsprDto);
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

	public LaenderartsprDto laenderartsprFindByPrimaryKey(String laenderartCNr, String localeCNr)
			throws EJBExceptionLP {
		try {
			LaenderartsprPK laenderartsprPK = new LaenderartsprPK();
			laenderartsprPK.setLaenderartCNr(laenderartCNr);
			laenderartsprPK.setLocaleCNr(localeCNr);
			Laenderartspr laenderartspr = em.find(Laenderartspr.class, laenderartsprPK);
			if (laenderartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleLaenderartsprDto(laenderartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private void setLaenderartsprFromLaenderartsprDto(Laenderartspr laenderartspr, LaenderartsprDto laenderartsprDto) {
		laenderartspr.setCBez(laenderartsprDto.getCBez());
		em.merge(laenderartspr);
		em.flush();
	}

	private LaenderartsprDto assembleLaenderartsprDto(Laenderartspr laenderartspr) {
		return LaenderartsprDtoAssembler.createDto(laenderartspr);
	}

	/**
	 * Uebersetzt eine Laenderart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr     String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return String
	 */
	public String uebersetzeLaenderartOptimal(String cNr, Locale locale1, Locale locale2) {
		String tempCnr = uebersetzeLaenderart(cNr, locale1);
		if (tempCnr.equals(cNr)) {
			tempCnr = uebersetzeLaenderart(cNr, locale2);
		}
		return tempCnr;
	}

	/**
	 * Uebersetzt eine Laenderart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr    String
	 * @param locale Locale
	 * @throws FinderException
	 * @return String
	 */
	private String uebersetzeLaenderart(String cNr, Locale locale) {
		String cLocale = Helper.locale2String(locale);
		// try {
		Laenderartspr laenderartspr = em.find(Laenderartspr.class, new LaenderartsprPK(cNr, cLocale));
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
	 * @param partnerIId   Integer
	 * @param theClientDto String
	 * @return String
	 * @throws EJBExceptionLP
	 */
/*	
	@Override
	public String getLaenderartZuPartner(Integer partnerIId, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.error("getLaenderartZuPartner fuer PartnerId " + partnerIId + " ohne Datum verwendet! (aktuelles eingesetzt)");
		return getLaenderartZuPartner(partnerIId, getTimestamp(), theClientDto);
	}
*/
	@Override
	public String getLaenderartZuPartner(Integer partnerId, Timestamp gueltigZum, TheClientDto theClientDto) {
		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerId, theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			return getLaenderartZuPartner(mandantDto, partnerDto, gueltigZum, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}		
	}
	
	/**
	 * Finden der Laenderart eines Partners in Bezug auf den Mandanten.
	 * 
	 * @param mandantDto   Integer
	 * @param partnerDto   PartnerDto
	 * @param theClientDto String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	@Override
	public String getLaenderartZuPartner(MandantDto mandantDto, 
			PartnerDto partnerDto, Timestamp gueltigZum,
			TheClientDto theClientDto) throws RemoteException {
		return getLaenderartZuPartner(mandantDto.getPartnerDto(), partnerDto, gueltigZum, theClientDto);
	}

	/**
	 * Finden der Laenderart eines Partners in Bezug auf das Land eines
	 * Basispartners.
	 * 
	 * @param partnerDtoBasis Integer
	 * @param partnerDto      PartnerDto
	 * @param theClientDto    String
	 * @return String
	 * @throws EJBExceptionLP
	 */
	@Override
	public String getLaenderartZuPartner(PartnerDto partnerDtoBasis, 
			PartnerDto partnerDto, Timestamp gueltigZum, TheClientDto theClientDto)
			throws RemoteException {
		LandDto landDto = null;
		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			if (partnerDto.getLandIIdAbweichendesustland() != null) {
				// Land uebersteuert PJ 15304

				landDto = getSystemFac().landFindByPrimaryKey(partnerDto.getLandIIdAbweichendesustland());
			}
		}
		if ((landDto != null)
				|| (partnerDto.getLandplzortDto() != null && partnerDto.getLandplzortDto().getLandDto() != null)) {
			if (partnerDtoBasis.getLandplzortDto() != null && partnerDtoBasis.getLandplzortDto().getLandDto() != null) {
				String partnerLKZ = partnerDto.getLandplzortDto().getLandDto().getCLkz();
//				String partnerLKZ = landDto != null 
//						? landDto.getCLkz()
//						: partnerDto.getLandplzortDto().getLandDto().getCLkz();

				// SP5689 Uebersteuerung des "Ablieferlandes" durch das zum
				// Empfaenger passende Land auf Basis des Finanzamts
				String mandantLKZ = landDto != null ? landDto.getCLkz()
						: partnerDtoBasis.getLandplzortDto().getLandDto().getCLkz();

				String gemeinsamesLKZ = null;
				if (partnerDtoBasis.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland() != null) {
					// fuer Liechtenstein ist die Schweiz wie INLAND
					Land land = em.find(Land.class,
							partnerDtoBasis.getLandplzortDto().getLandDto().getLandIIdGemeinsamespostland());
					gemeinsamesLKZ = land.getCLkz();
				}
				// Land gleich -> Inland
				if (mandantLKZ.equals(partnerLKZ) || (gemeinsamesLKZ != null && gemeinsamesLKZ.equals(partnerLKZ))) {
					return FinanzFac.LAENDERART_INLAND;
				} else {
					LandDto mandantLandDto = landDto != null ? landDto
							: partnerDtoBasis.getLandplzortDto().getLandDto();

					LandDto partnerLandDto = partnerDto.getLandplzortDto().getLandDto();

					// wenn ich in der EU bin
					if (getSystemFac().isEUMitglied(mandantLandDto, gueltigZum)) {
						// und der Partner ist auch EU-Mitglied
						if (getSystemFac().isEUMitglied(partnerLandDto, gueltigZum)) {
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

	public HashMap<String, String> getAllLaenderartenMitUebersetzung(Locale locale, TheClientDto theClientDto)
			throws EJBExceptionLP {
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
				hm.put(item.getC_nr(), uebersetzeLaenderartOptimal(item.getC_nr(), locale, locale));
			}
		} finally {
			closeSession(session);
		}
		return hm;
	}

	/**
	 * Eine Buchungsart anhand PK finden.
	 * 
	 * @param cNr String
	 * @throws EJBExceptionLP
	 * @return BuchungsartDto
	 */
	public BuchungsartDto buchungsartFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// try {
		Buchungsart buchungsart = em.find(Buchungsart.class, cNr);
		if (buchungsart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBuchungsartDto(buchungsart);
	}

	/**
	 * Alle Buchungsarten finden.
	 * 
	 * @throws EJBExceptionLP
	 * @return BuchungsartDto
	 */
	public BuchungsartDto[] buchungsartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("BuchungsartfindAll");
		Collection<?> cl = query.getResultList();
		return assembleBuchungsartDtos(cl);
	}

	private SteuerkategorieDto assembleSteuerkategorieDto(Steuerkategorie steuerkategorie) {
		return SteuerkategorieDtoAssembler.createDto(steuerkategorie);
	}

	private SteuerkategoriekontoDto assembleSteuerkategoriekontoDto(Steuerkategoriekonto steuerkategoriekonto) {
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

	public BuchungsartsprDto buchungsartsprFindByPrimaryKey(String buchungsartCNr, String spracheCNr)
			throws EJBExceptionLP {
		try {
			BuchungsartsprPK buchungsartsprPK = new BuchungsartsprPK();
			buchungsartsprPK.setBuchungsartCNr(buchungsartCNr);
			buchungsartsprPK.setLocaleCNr(spracheCNr);
			Buchungsartspr buchungsartspr = em.find(Buchungsartspr.class, buchungsartsprPK);
			if (buchungsartspr == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			return assembleBuchungsartsprDto(buchungsartspr);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, e);
		}
	}

	private BuchungsartsprDto assembleBuchungsartsprDto(Buchungsartspr buchungsartspr) {
		return BuchungsartsprDtoAssembler.createDto(buchungsartspr);
	}

	/**
	 * Hole die bestmoeglichen Uebersetzungen f&uuml;r ein Array von Buchungsarten.
	 * 
	 * @param pArray  Positionsarten
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return TreeMap
	 * @throws EJBExceptionLP
	 */
	public TreeMap<String, String> uebersetzeBuchungsartOptimal(DatenspracheIf[] pArray, Locale locale1, Locale locale2)
			throws EJBExceptionLP {
		TreeMap<String, String> tm = new TreeMap<String, String>();
		for (int i = 0; i < pArray.length; i++) {
			tm.put(pArray[i].getCNr(), uebersetzeBuchungsartOptimal(pArray[i].getCNr(), locale1, locale2));
		}
		return tm;
	}

	/**
	 * Uebersetzt eine Buchungsart optimal. 1.Versuch: mit locale1 2.Versuch: mit
	 * locale2 3.Versuch: cNr
	 * 
	 * @param cNr     String
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @return String
	 */
	public String uebersetzeBuchungsartOptimal(String cNr, Locale locale1, Locale locale2) {
		Validator.notEmpty(cNr, "cNr");
		Validator.notNull(locale1, "locale1");
		
		HvOptional<String> uebersetzung = uebersetzeBuchungsart(cNr, locale1);
		if (uebersetzung.isPresent()) return uebersetzung.get();
		
		if (locale2 != null) {
			uebersetzung = uebersetzeBuchungsart(cNr, locale2);
			if (uebersetzung.isPresent()) return uebersetzung.get();
		}
		
		return cNr;
	}

	/**
	 * Uebersetzt eine Buchungsart in die Sprache des uebergebenen Locales.
	 * 
	 * @param cNr    String
	 * @param locale Locale
	 * @throws FinderException
	 * @return HvOptional.empty() wenn fuer die Locale nicht vorhanden, oder String leer ist,
	 *   ansonsten String.trim()
	 */
	private HvOptional<String> uebersetzeBuchungsart(String cNr, Locale locale) throws EJBExceptionLP {
		try {
			String cLocale = Helper.locale2String(locale);
			Buchungsartspr buchungsartspr = em.find(Buchungsartspr.class, new BuchungsartsprPK(cNr, cLocale));
			if (buchungsartspr == null) {
				return HvOptional.empty();
			}
			String s = Helper.emptyString(buchungsartspr.getCBez());
			return s.length() == 0 ? HvOptional.empty() : HvOptional.of(s);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	/**
	 * Alle Buchungsarten mit Uebersetzung holen.
	 * 
	 * @throws EJBExceptionLP
	 * @return TreeMap
	 * @param locale1 Locale
	 * @param locale2 Locale
	 * @throws RemoteException
	 */
	public TreeMap getAllBuchungsarten(Locale locale1, Locale locale2, String mandantCNr)
			throws EJBExceptionLP, RemoteException {
		BuchungsartDto[] arten = buchungsartFindAll();
		TreeMap<String, String> maparten = uebersetzeBuchungsartOptimal(arten, locale1, locale2);
		ParametermandantDto p = getParameterFac().getMandantparameter(
				mandantCNr, ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL);
		if (!p.asBoolean()) {
			maparten.remove(FinanzFac.BUCHUNGSART_MWST_ABSCHLUSS);
		}
		
		return maparten;
	}

	public UvaartDto[] uvaartFindAll(String mandantCNr) throws EJBExceptionLP {
		Query query = em.createNamedQuery("UvaartfindAll");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		return assembleUvaartDtos(cl);
	}

	public void createBuchungsart(BuchungsartDto buchungsartDto, TheClientDto theClientDto) throws EJBExceptionLP {
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
	private void setBuchungsartFromBuchungsartDto(Buchungsart buchungsart, BuchungsartDto buchungsartDto) {
		em.merge(buchungsart);
		em.flush();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void createWarenverkehrsnummernUndLoescheAlte(WarenverkehrsnummerDto[] warenverkehrsnummerDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (warenverkehrsnummerDtos == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("warenverkehrsnummerDtos == null"));
		}
		myLogger.warn(theClientDto.getIDUser(),
				"createWarenverkehrsnummernUndLoescheAlte: " + warenverkehrsnummerDtos.length + " neue Eintraege.");
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
				getFinanzServiceFac().createWarenverkehrsnummer(warenverkehrsnummerDtos[i]);
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
			FLRFinanzWarenverkehrsnummer item = (FLRFinanzWarenverkehrsnummer) iter.next();
			Warenverkehrsnummer toRemove = em.find(Warenverkehrsnummer.class, item.getC_nr());
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
		// finally {
		closeSession(session);
	}

	public void createWarenverkehrsnummer(WarenverkehrsnummerDto warenverkehrsnummerDto) {
		Warenverkehrsnummer warenverkehrsnummer = new Warenverkehrsnummer(warenverkehrsnummerDto.getCNr(),
				warenverkehrsnummerDto.getCBez());
		em.persist(warenverkehrsnummer);
		em.flush();
		setWarenverkehrsnummerFromWarenverkehrsnummerDto(warenverkehrsnummer, warenverkehrsnummerDto);
	}

	public void vertauscheSteuerkategorie(Integer iiD1, Integer iId2) {
		Steuerkategorie oSteuerkategorie1 = em.find(Steuerkategorie.class, iiD1);
		if (oSteuerkategorie1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheSteuerkategorie. Es gibt keine Steuerkategorie mit iid1 " + iiD1);
		}

		Steuerkategorie oSteuerkategorie2 = em.find(Steuerkategorie.class, iId2);
		if (oSteuerkategorie2 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei vertauscheSteuerkategorie. Es gibt keinen Steuerkategorie mit iid2 " + iId2);
		}

		Integer iSort1 = oSteuerkategorie1.getISort();
		Integer iSort2 = oSteuerkategorie2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oSteuerkategorie2.setISort(new Integer(-1));
		oSteuerkategorie1.setISort(iSort2);
		oSteuerkategorie2.setISort(iSort1);

	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		Warenverkehrsnummer warenverkehrsnummer = em.find(Warenverkehrsnummer.class, cNr);
		if (warenverkehrsnummer == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleWarenverkehrsnummerDto(warenverkehrsnummer);
	}

	public WarenverkehrsnummerDto warenverkehrsnummerFindByPrimaryKeyOhneExc(String cNr) throws EJBExceptionLP {
		Warenverkehrsnummer warenverkehrsnummer = em.find(Warenverkehrsnummer.class, cNr);
		if (warenverkehrsnummer == null) {
			return null;
		}
		return assembleWarenverkehrsnummerDto(warenverkehrsnummer);
	}

	private void setWarenverkehrsnummerFromWarenverkehrsnummerDto(Warenverkehrsnummer warenverkehrsnummer,
			WarenverkehrsnummerDto warenverkehrsnummerDto) {
		warenverkehrsnummer.setCBez(warenverkehrsnummerDto.getCBez());
		warenverkehrsnummer.setCBM(warenverkehrsnummerDto.getCBM());
		em.merge(warenverkehrsnummer);
		em.flush();
	}

	private WarenverkehrsnummerDto assembleWarenverkehrsnummerDto(Warenverkehrsnummer warenverkehrsnummer) {
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

	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		FinanzamtDto[] finanzamtDtos = getFinanzFac().finanzamtFindAllByMandantCNr(theClientDto);
		for (FinanzamtDto finanzamt : finanzamtDtos) {
			createFinanzamtsbuchungen(geschaeftsjahr, periode, finanzamt.getPartnerIId(), theClientDto);
		}
	}

	/**
	 * Finanzamtsbuchungen f&uuml;r Periode auf ein Finanzamt durchf&uuml;hren wird
	 * vor UVA Verprobung ausgef&uuml;hrt
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 * 
	 */
	public void createFinanzamtsbuchungen(int geschaeftsjahr, int periode, int finanzamtIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// der Ablauf ist derzeit hart kodiert, die Konten werden ueber
		// Kontoarten definiert

		// PJ18633 mit Parameter PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL werden
		// hier KEINE Buchungen durchgefuehrt!
		ParametermandantDto p = getParameterFac().getMandantparameter(
				theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
				ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL);
//		ParametermandantDto p = getParameterFac().parametermandantFindByPrimaryKey(
//				ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL, ParameterFac.KATEGORIE_FINANZ,
//				theClientDto.getMandant());
		if (p.asBoolean())
			return;

		Timestamp tVonBis[] = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date buchungsDatum = Helper.addiereTageZuDatum(new Date(tVonBis[1].getTime()), -1); // letzter Tag der Periode

		HashMap<String, Integer> hmAZ = null;

		// alte Buchungen stornieren
		// SP2015/03280 alle AKZ merken
		hmAZ = getBuchenFac().storniereFinanzamtsbuchungen(geschaeftsjahr, buchungsDatum, finanzamtIId, theClientDto);

		ArrayList<KontoDto> sammellist = new ArrayList<KontoDto>();
		// Alle Ust Konten auf UST Sammelkonto
		KontoDto konto = saldiereKontoartAufSammelkonto(KONTOART_UST, KONTOART_UST_SAMMEL, buchungsDatum,
				geschaeftsjahr, periode, finanzamtIId, theClientDto);
		if (konto != null)
			sammellist.add(konto);
		// Alle Vst Konten auf VST Sammelkonto
		konto = saldiereKontoartAufSammelkonto(KONTOART_VST, KONTOART_VST_SAMMEL, buchungsDatum, geschaeftsjahr,
				periode, finanzamtIId, theClientDto);
		if (konto != null)
			sammellist.add(konto);
		if (sammellist.size() == 0)
			return; // 18533
		// UST Sammel + VST Sammel auf FA Steuer Verrechnung
		KontoDto kontoZielDto = getSammelkonto(KONTOART_UST_ODER_ERWERBSSTEUERKONTO, finanzamtIId, theClientDto);
		if (kontoZielDto == null)
			return; // rk: SP 2424
		saldoBuchungAufKonto(sammellist, kontoZielDto.getIId(), geschaeftsjahr, periode, buchungsDatum, theClientDto);
		// FA Steuer Verrechnung auf Zahllast Konto
		KontoDto zahllastKontoDto = getSammelkonto(KONTOART_FA_ZAHLLAST, finanzamtIId, theClientDto);
		if (zahllastKontoDto == null)
			return; // rk: SP 2424
		saldoBuchungAufKonto(new KontoDto[] { kontoZielDto }, zahllastKontoDto.getIId(), geschaeftsjahr, periode,
				buchungsDatum, theClientDto);
		// Alle Abgaben auf Zahllastkonto
		saldiereKontoartAufSammelkonto(KONTOART_ABGABEN, KONTOART_FA_ZAHLLAST, buchungsDatum, geschaeftsjahr, periode,
				finanzamtIId, theClientDto);
		if (!hmAZ.isEmpty())
			// SP2015/03280 alle gemerkten AKZ wieder setzen wenn Buchungsdetail
			// wieder vorhanden
			getBuchenFac().setAuszifferungenFinanzamtsbuchungen(geschaeftsjahr, buchungsDatum, finanzamtIId, hmAZ,
					theClientDto);
	}

	public KontoDto getSammelkonto(String kontoartCNr, Integer finanzamtIId, TheClientDto theClientDto) {
		KontoDto[] kontoDtos;
		if (finanzamtIId != null)
			kontoDtos = getFinanzFac().kontoFindAllByKontoartMandantFinanzamt(kontoartCNr, theClientDto.getMandant(),
					finanzamtIId);
		else
			kontoDtos = getFinanzFac().kontoFindAllByKontoartMandant(kontoartCNr, theClientDto.getMandant());

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
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
					"Mehrfache Sammelkonten definiert f\u00FCr Kontoart " + kontoartCNr + ": " + s);
		} else
			sammelkonto = kontoDtos[0];
		return sammelkonto;
	}

	private KontoDto saldiereKontoartAufSammelkonto(String kontoartCNr, String kontoartCNrSammelkonto,
			Date buchungsDatum, int geschaeftsjahr, int periode, int finanzamtIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// es darf nur ein Sammelkonto geben
		KontoDto sammelkonto = getSammelkonto(kontoartCNrSammelkonto, finanzamtIId, theClientDto);
		if (sammelkonto == null) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_BUCHUNG_NICHT_ERLAUBT_BUCHUNGSREGEL,
			// "Kein Sammelkonto f\u00FCr Kontoart " + kontoartCNrSammelkonto);
			// //PJ 18533
			return null;
		}
		KontoDto[] kontoDtos = getFinanzFac().kontoFindAllByKontoartMandantFinanzamt(kontoartCNr,
				theClientDto.getMandant(), finanzamtIId);
		saldoBuchungAufKonto(kontoDtos, sammelkonto.getIId(), geschaeftsjahr, periode, buchungsDatum, theClientDto);
		return sammelkonto;
	}

	private void saldoBuchungAufKonto(ArrayList<KontoDto> sammellist, Integer kontoIIdZiel, int geschaeftsjahr,
			int periode, Date buchungsDatum, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		saldoBuchungAufKonto(sammellist.toArray(new KontoDto[sammellist.size()]), kontoIIdZiel, geschaeftsjahr, periode,
				buchungsDatum, theClientDto);
	}

	private void saldoBuchungAufKonto(KontoDto[] kontoDtos, Integer kontoIIdZiel, int geschaeftsjahr, int periode,
			Date buchungsDatum, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ReversechargeartDto rcartOhneDto = reversechargeartFindOhne(theClientDto);
		BuchungDto buchungDto = new BuchungDto(FinanzFac.BUCHUNGSART_UMBUCHUNG, buchungsDatum, "Sammelbuchung",
				geschaeftsjahr, null, true, false, false);
		ArrayList<BuchungdetailDto> list = getSammelbuchungdetails(kontoDtos, kontoIIdZiel, geschaeftsjahr, periode,
				theClientDto);
		BigDecimal summe = new BigDecimal(0);
		if (list.size() > 0) {
			for (BuchungdetailDto detail : list) {
				summe = summe.add(detail.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung) ? detail.getNBetrag()
						: detail.getNBetrag().negate());
			}

			// Gegenbuchung erzeugen fuer Summe der Buchungen
			BuchungdetailDto detail = new BuchungdetailDto(list.get(0).getBuchungdetailartCNr(), kontoIIdZiel,
					list.size() == 1 ? list.get(0).getKontoIId() : null, summe.abs(), new BigDecimal(0));
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
			buchungDto = getBuchenFac().buchen(buchungDto, list.toArray(new BuchungdetailDto[list.size()]),
					rcartOhneDto.getIId(), true, theClientDto);
		}
	}

	private ArrayList<BuchungdetailDto> getSammelbuchungdetails(KontoDto[] kontoDtos, Integer kontoIIdGegenkonto,
			int geschaeftsjahr, int periode, TheClientDto theClientDto) {
		ArrayList<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		for (int i = 0; i < kontoDtos.length; i++) {
			BigDecimal saldo = getBuchenFac().getSaldoOhneEBVonKonto(kontoDtos[i].getIId(), geschaeftsjahr, periode,
					theClientDto);
			if (saldo.signum() < 0) {
				BuchungdetailDto detailDto = new BuchungdetailDto(BuchenFac.SollBuchung, kontoDtos[i].getIId(),
						kontoIIdGegenkonto, saldo.abs(), new BigDecimal(0));
				list.add(detailDto);
			} else if (saldo.signum() > 0) {
				BuchungdetailDto detailDto = new BuchungdetailDto(BuchenFac.HabenBuchung, kontoDtos[i].getIId(),
						kontoIIdGegenkonto, saldo, new BigDecimal(0));
				list.add(detailDto);
			}
		}
		return list;
	}

	/**
	 * Tagesabschluss fuer das Geschaeftsjahr Periodenweise pr&uuml;fen und updaten
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
	 * RemoteException { int periode = getAktuellePeriode(); // TODO: default wert
	 * aus mandant Integer kostenstelleIId = new Integer(11); Query query =
	 * em.createNamedQuery("SteuerkategorieByMandantCNr"); query.setParameter(1,
	 * theclientDto.getMandant()); List<Steuerkategorie> list =
	 * query.getResultList(); Iterator<Steuerkategorie> iter = list.iterator();
	 * while (iter.hasNext()) { Steuerkategorie stk = iter.next(); for (int i = 1; i
	 * <= periode; i++) { // Forderungen verbuchen verbucheForderungen(stk.getIId(),
	 * stk.getKontoIIdForderungen(), geschaeftsjahr, i, kostenstelleIId,
	 * theclientDto); // Verbindlichkeiten verbuchen } } // Ust Sammelkonto // Vst
	 * Sammelkonto
	 * 
	 * }
	 */

	/*
	 * AD nicht verwendet
	 * 
	 * @SuppressWarnings("unchecked") private void verbucheForderungen(Integer
	 * steuerkategorieIId, Integer sammelKontoIId, int geschaeftsjahr, int periode,
	 * Integer kostenstelleIId, TheClientDto theClientDto) throws EJBExceptionLP,
	 * RemoteException { Query query =
	 * em.createNamedQuery("KontofindByKontotypSteuerkategorie");
	 * query.setParameter(1, FinanzServiceFac.KONTOTYP_DEBITOR);
	 * query.setParameter(2, steuerkategorieIId); List<Konto> list =
	 * query.getResultList(); if (list.size() > 0) { Iterator<Konto> iter =
	 * list.iterator(); BigDecimal saldo = new BigDecimal(0); HashMap hmSaldo = new
	 * HashMap(); int cnt = 0; while (iter.hasNext()) { Konto konto = iter.next();
	 * BigDecimal saldoKonto = getBuchenFac().getSaldoOhneEBVonKonto(
	 * konto.getIId(), geschaeftsjahr, periode, theClientDto); if
	 * (saldoKonto.doubleValue() != 0) { saldo = saldo.add(saldoKonto); KontoSaldo
	 * ks = new KontoSaldo(konto.getIId(), konto, saldoKonto); hmSaldo.put(new
	 * Integer(cnt), ks); cnt++; } } if (hmSaldo.size() > 0) { BuchungDto b = new
	 * BuchungDto(); // b.setBelegartCNr(belegartCNr);
	 * b.setBuchungsartCNr(FinanzFac.BUCHUNGSART_UMBUCHUNG);
	 * b.setCBelegnummer("Abschluss" + periode); b.setCText("Tagesabschluss");
	 * Calendar c = Calendar.getInstance(); c.set(Calendar.YEAR, geschaeftsjahr);
	 * c.set(Calendar.MONTH, periode); c.set(Calendar.DAY_OF_MONTH, 0);
	 * b.setDBuchungsdatum((Date) Helper.cutDate(c.getTime()));
	 * b.setIGeschaeftsjahr(geschaeftsjahr); b.setKostenstelleIId(kostenstelleIId);
	 * BuchungdetailDto[] bd = new BuchungdetailDto[hmSaldo.size() + 1]; bd[0] = new
	 * BuchungdetailDto(); bd[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
	 * bd[0].setKontoIId(sammelKontoIId); bd[0].setNBetrag(saldo); bd[0].setNUst(new
	 * BigDecimal(0)); for (int i = 0; i < hmSaldo.size(); i++) { KontoSaldo ks =
	 * (KontoSaldo) hmSaldo.get(new Integer(i)); bd[i + 1] = new BuchungdetailDto();
	 * bd[i + 1].setBuchungdetailartCNr(BuchenFac.HabenBuchung); bd[i +
	 * 1].setKontoIId(ks.kontoIId); bd[i + 1].setKontoIIdGegenkonto(sammelKontoIId);
	 * bd[i + 1].setNBetrag(ks.saldo); bd[i + 1].setNUst(new BigDecimal(0)); }
	 * getBuchenFac().buchen(b, bd, false, theClientDto); } } }
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

	public UvaartDto uvaartFindByCnrMandant(String cNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		try {
			return getUvaartDto(cNr, theClientDto);
		} catch (NoResultException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"Uvaart nicht gefunden cNr:" + cNr + " Mandant:" + theClientDto.getMandant());
		}
	}

	public UvaartDto uvaartFindByCnrMandantOhneExc(String cNr, TheClientDto theClientDto) throws RemoteException {
		try {
			return getUvaartDto(cNr, theClientDto);
		} catch (NoResultException ex) {
		}
		return null;
	}

	private Query uvaartDtoQuery(String cNr, TheClientDto theClientDto) {
//		Query query = em.createNamedQuery("UvaartfindByCNrMandant");
//		query.setParameter(1, cNr);
//		query.setParameter(2, theClientDto.getMandant());
//		return query;
		return em.createNamedQuery("UvaartfindByCNrMandant").setParameter(1, cNr).setParameter(2,
				theClientDto.getMandant());
	}

	private UvaartDto getUvaartDto(String cNr, TheClientDto theClientDto) {
		Query query = uvaartDtoQuery(cNr, theClientDto);
		Uvaart uvaart = (Uvaart) query.getSingleResult();
		UvaartDto uvaartDto = assembleUvaartDto(uvaart);

		UvaartsprDto sprDto = getUvaartsprDto(uvaart.getIId(), theClientDto.getLocUi(), theClientDto.getLocMandant());

		uvaartDto.setUvaartsprDto(sprDto);

		return uvaartDto;
	}

	@Override
	public Integer getUstKontoFuerSteuerkategorie(Integer steuerkategorieIId, Integer mwstsatzbezId,
			Timestamp gueltigAm) {
		HvOptional<SteuerkategoriekontoDto> stkk = steuerkategoriekontoZuDatum(steuerkategorieIId, mwstsatzbezId,
				gueltigAm);
		return stkk.isPresent() ? stkk.get().getKontoIIdVk() : null;
		/*
		 * Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class, new
		 * SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId)); return stkk ==
		 * null ? null : stkk.getKontoIIdVk();
		 */
	}

	@Override
	public Integer getEUstKontoFuerSteuerkategorie(Integer steuerkategorieIId, Integer mwstsatzbezId,
			Timestamp gueltigAm) {
		HvOptional<SteuerkategoriekontoDto> stkk = steuerkategoriekontoZuDatum(steuerkategorieIId, mwstsatzbezId,
				gueltigAm);
		return stkk.isPresent() ? stkk.get().getKontoIIdEinfuhrUst() : null;
		/*
		 * Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class, new
		 * SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId)); return stkk ==
		 * null ? null : stkk.getKontoIIdEinfuhrUst();
		 */
	}

	@Override
	public Integer getVstKontoFuerSteuerkategorie(Integer steuerkategorieIId, Integer mwstsatzbezId,
			Timestamp gueltigAm) {
		HvOptional<SteuerkategoriekontoDto> stkk = steuerkategoriekontoZuDatum(steuerkategorieIId, mwstsatzbezId,
				gueltigAm);
		return stkk.isPresent() ? stkk.get().getKontoIIdEk() : null;
		/*
		 * Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class, new
		 * SteuerkategoriekontoPK(steuerkategorieIId, mwstsatzbezId)); return stkk ==
		 * null ? null : stkk.getKontoIIdEk();
		 */
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2400)
	public List<BucheBelegPeriodeInfoDto> verbucheBelegePeriode(Integer geschaeftsjahr, int periode, boolean alleNeu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		return getBelegbuchungFac(theClientDto.getMandant())
				.verbucheBelegePeriode(geschaeftsjahr, periode, alleNeu,
				theClientDto);
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(300)
	public ArrayList<FibuFehlerDto> pruefeBelegeKurse(Integer geschaeftsjahr, boolean nurPruefen,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		ArrayList<FibuFehlerDto> fehler = new ArrayList<FibuFehlerDto>();
		fehler = pruefeKurseRechnungen(fehler, geschaeftsjahr, nurPruefen, theClientDto);
		fehler = pruefeKurseRechnungZahlungen(fehler, geschaeftsjahr, nurPruefen, theClientDto);
		fehler = pruefeKurseEingangsrechnungen(fehler, geschaeftsjahr, nurPruefen, theClientDto);
		fehler = pruefeKurseEingangsrechnungZahlungen(fehler, geschaeftsjahr, nurPruefen, theClientDto);
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseRechnungen(ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			boolean nurPruefen, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = null;
		try {
			rechnungen = getRechnungFac().rechnungFindByBelegdatumVonBis(theClientDto.getMandant(), dBeginn, dEnd);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)
						&& !rechnungen[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getNKurs().doubleValue() != 1) {
						BigDecimal alterKurs = rechnungen[i].getNKurs();
						BigDecimal alterWert = rechnungen[i].getNWert();
						WechselkursDto kursDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
								rechnungen[i].getWaehrungCNr(), new Date(rechnungen[i].getTBelegdatum().getTime()),
								theClientDto);
						if (kursDto.getNKurs().compareTo(rechnungen[i].getNKurs()) != 0) {
							if (nurPruefen) {
								fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_FIBU_RECHNUNG, rechnungen[i].getIId(),
										rechnungen[i].getCNr(), FibuFehlerDto.FEHLER_KURS));
								myLogger.error(
										"Kursabweichung: AR" + rechnungen[i].getCNr() + " von " + alterKurs.toString()
												+ " auf " + kursDto.getNKurs().toString() + " Differenz: "
												+ alterWert
														.subtract(rechnungen[i].getNWertfw().divide(kursDto.getNKurs(),
																FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN))
														.toString());
							} else {
								rechnungen[i].setNKurs(kursDto.getNKurs());
								rechnungen[i].setNWert(rechnungen[i].getNWertfw().divide(kursDto.getNKurs(),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
								rechnungen[i].setNWertust(rechnungen[i].getNWertustfw().divide(kursDto.getNKurs(),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
								getRechnungFac().updateRechnungService(rechnungen[i], theClientDto);
								myLogger.error("Kurs korrigiert: AR" + rechnungen[i].getCNr() + " von "
										+ alterKurs.toString() + " auf " + kursDto.getNKurs().toString()
										+ " Differenz: " + alterWert.subtract(rechnungen[i].getNWert()).toString());
							}
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseRechnungZahlungen(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getRechnungFac().rechnungzahlungIdsByMandantZahldatumVonBis(theClientDto.getMandant(),
					dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				RechnungzahlungDto zahlungDto = getRechnungFac().rechnungzahlungFindByPrimaryKey(zahlungenIDs.get(i));
				if (zahlungDto.getNKurs().doubleValue() != 1) {
					Rechnung rechnung = em.find(Rechnung.class, zahlungDto.getRechnungIId());
					BigDecimal alterKurs = zahlungDto.getNKurs();
					BigDecimal alterBetrag = zahlungDto.getNBetrag();
					WechselkursDto kursDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
							rechnung.getWaehrungCNr(), zahlungDto.getDZahldatum(), theClientDto);
					if (kursDto.getNKurs().compareTo(zahlungDto.getNKurs()) != 0) {
						if (nurPruefen) {
							fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_REZAHLUNG, rechnung.getIId(),
									rechnung.getCNr(), FibuFehlerDto.FEHLER_KURS));
							myLogger.error("Kursabweichung: ARZahlung" + rechnung.getCNr() + " von "
									+ alterKurs.toString() + " auf " + kursDto.getNKurs().toString() + " Differenz: "
									+ alterBetrag
											.subtract(zahlungDto.getNBetragfw().divide(kursDto.getNKurs(),
													FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN))
											.toString());
						} else {
							zahlungDto.setNKurs(kursDto.getNKurs());
							zahlungDto.setNBetrag(zahlungDto.getNBetragfw().divide(kursDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
							zahlungDto.setNBetragUst(zahlungDto.getNBetragUstfw().divide(kursDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
							getRechnungFac().updateRechnungzahlung(zahlungDto);
							myLogger.error("Kurs korrigiert: ARZahlung" + rechnung.getCNr() + " von "
									+ alterKurs.toString() + " auf " + kursDto.getNKurs().toString() + " Differenz: "
									+ alterBetrag.subtract(zahlungDto.getNBetrag()).toString());
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseEingangsrechnungen(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = null;
		try {
			rechnungen = getEingangsrechnungFac().eingangsrechnungFindByMandantCNrDatumVonBis(theClientDto.getMandant(),
					dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getNKurs().doubleValue() != 1) {
						BigDecimal alterKurs = rechnungen[i].getNKurs();
						BigDecimal alterWert = rechnungen[i].getNBetrag();
						WechselkursDto kursDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
								rechnungen[i].getWaehrungCNr(), new Date(rechnungen[i].getDBelegdatum().getTime()),
								theClientDto);
						BigDecimal neuerKurs = new BigDecimal(1).divide(kursDto.getNKurs(), 6,
								BigDecimal.ROUND_HALF_EVEN);
						if (neuerKurs.compareTo(rechnungen[i].getNKurs()) != 0) {
							if (nurPruefen) {
								fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_EINGANGSRECHNUNG,
										rechnungen[i].getIId(), rechnungen[i].getCNr(), FibuFehlerDto.FEHLER_KURS));
								myLogger.error("Kursabweichung: ER" + rechnungen[i].getCNr() + " von "
										+ alterKurs.toString() + " auf " + neuerKurs.toString() + " Differenz: "
										+ alterWert
												.subtract(rechnungen[i].getNBetragfw().divide(kursDto.getNKurs(),
														FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN))
												.toString());
							} else {
								rechnungen[i].setNKurs(neuerKurs);
								rechnungen[i].setNBetrag(rechnungen[i].getNBetragfw().divide(kursDto.getNKurs(),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
								rechnungen[i].setNUstBetrag(rechnungen[i].getNUstBetragfw().divide(kursDto.getNKurs(),
										FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
								if (i >= 286)
									System.out.println(i);
								getEingangsrechnungFac().updateEingangsrechnung(rechnungen[i], theClientDto);
								myLogger.error("Kurs korrigiert: ER" + rechnungen[i].getCNr() + " von "
										+ alterKurs.toString() + " auf " + neuerKurs.toString() + " Differenz: "
										+ alterWert.subtract(rechnungen[i].getNBetrag()).toString());
							}
						}
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeKurseEingangsrechnungZahlungen(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, boolean nurPruefen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, -1, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getEingangsrechnungFac()
					.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				EingangsrechnungzahlungDto erzDto = getEingangsrechnungFac()
						.eingangsrechnungzahlungFindByPrimaryKey(zahlungenIDs.get(i));

				if (erzDto.getNKurs().doubleValue() != 1 && !Helper.short2boolean(erzDto.getBKursuebersteuert())) {
					Integer rechnungIId = erzDto.getEingangsrechnungIId();
					Eingangsrechnung er = em.find(Eingangsrechnung.class, rechnungIId);
					BigDecimal alterKurs = erzDto.getNKurs();
					BigDecimal alterBetrag = erzDto.getNBetrag();
					WechselkursDto kursDto = getLocaleFac().getKursZuDatum(theClientDto.getSMandantenwaehrung(),
							er.getWaehrungCNr(), erzDto.getTZahldatum(), theClientDto);
					if (kursDto.getNKurs().compareTo(erzDto.getNKurs()) != 0) {
						if (nurPruefen) {
							fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_ERZAHLUNG, er.getIId(), er.getCNr(),
									FibuFehlerDto.FEHLER_KURS));
							myLogger.error("Kursabweichung: ERZahlung" + er.getCNr() + " von " + alterKurs.toString()
									+ " auf " + kursDto.getNKurs().toString() + " Differenz: "
									+ alterBetrag
											.subtract(erzDto.getNBetragfw().divide(kursDto.getNKurs(),
													FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN))
											.toString());
						} else {
							erzDto.setNKurs(kursDto.getNKurs());
							erzDto.setNBetrag(erzDto.getNBetragfw().divide(kursDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
							erzDto.setNBetragust(erzDto.getNBetragustfw().divide(kursDto.getNKurs(),
									FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN));
							getEingangsrechnungFac().updateEingangsrechnungzahlung(erzDto);
							myLogger.error("Kurs korrigiert: ERZahlung" + er.getCNr() + " von " + alterKurs.toString()
									+ " auf " + kursDto.getNKurs().toString() + " Differenz: "
									+ alterBetrag.subtract(erzDto.getNBetrag()).toString());
						}
					}
				}
			}
		}
		return fehler;
	}

	public ArrayList<FibuFehlerDto> pruefeBelegePeriode(Integer geschaeftsjahr, int periode, boolean pruefeBelegInFibu,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		// SP 2014/02892
		// bei ISTVERSTEURER keine Pruefung von AR und ER
		// bei MISCHVERSTEURER keine Pruefung von AR
		boolean istVersteurer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_ISTVERSTEURER, theClientDto.getMandant());
		boolean mischVersteurer = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
				MandantFac.ZUSATZFUNKTION_MISCHVERSTEURER, theClientDto.getMandant());

		ArrayList<FibuFehlerDto> fehler = new ArrayList<FibuFehlerDto>();
		if (!istVersteurer && !mischVersteurer)
			fehler = pruefeRechnungenPeriode(fehler, geschaeftsjahr, periode, pruefeBelegInFibu, theClientDto);
		fehler = pruefeRechnungZahlungenPeriode(fehler, geschaeftsjahr, periode, pruefeBelegInFibu, theClientDto);
		if (!istVersteurer)
			fehler = pruefeEingangsrechnungenPeriode(fehler, geschaeftsjahr, periode, pruefeBelegInFibu, theClientDto);
		fehler = pruefeEingangsrechnungZahlungenPeriode(fehler, geschaeftsjahr, periode, pruefeBelegInFibu,
				theClientDto);
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeRechnungZahlungenPeriode(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		// hier wird nur geprueft ob in Fibu wenn erforderlich
		if (pruefeBelegInFibu) {
			Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
			Date dBeginn = new Date(tVonBis[0].getTime());
			Date dEnd = new Date(tVonBis[1].getTime());

			List<Integer> zahlungenIDs = null;
			try {
				zahlungenIDs = getRechnungFac().rechnungzahlungIdsByMandantZahldatumVonBis(theClientDto.getMandant(),
						dBeginn, dEnd);
			} catch (EJBExceptionLP e1) {
				e1.printStackTrace();
			}
			if (zahlungenIDs != null) {
				for (int i = 0; i < zahlungenIDs.size(); i++) {
					if (!isBelegInFibu(LocaleFac.BELEGART_REZAHLUNG, zahlungenIDs.get(i), theClientDto)) {
						Rechnungzahlung zahlung = em.find(Rechnungzahlung.class, zahlungenIDs.get(i));
						Integer rechnungIId = zahlung.getRechnungIId();
						Rechnung ar = em.find(Rechnung.class, rechnungIId);
						fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_REZAHLUNG, rechnungIId, ar.getCNr(),
								FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeRechnungenPeriode(ArrayList<FibuFehlerDto> fehler, Integer geschaeftsjahr,
			int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		RechnungDto[] rechnungen = null;
		try {
			rechnungen = getRechnungFac().rechnungFindByBelegdatumVonBis(theClientDto.getMandant(), dBeginn, dEnd);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		}
		if (rechnungen != null) {
			boolean bRechnung0Erlaubt = false;
			try {
				if (getParameterFac().getMandantparameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_FINANZ,
						ParameterFac.PARAMETER_FINANZ_RECHNUNG_WERT0_ERLAUBT).getCWert().equals("1"))
					bRechnung0Erlaubt = true;
			} catch (RemoteException e) {
				//
			}

			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getRechnungartCNr().equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
					if (rechnungen[i].getStatusCNr().equals(RechnungFac.STATUS_ANGELEGT)) {
						fehler.add(new FibuFehlerDto(rechnungen[i].getRechnungartCNr(), rechnungen[i].getIId(),
								rechnungen[i].getCNr(), FibuFehlerDto.FEHLER_STATUS));
					} else if (!rechnungen[i].getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
						if (!bRechnung0Erlaubt || (rechnungen[i].getNWert().signum() != 0)) {
							// SP 2013/01327 nur Rechnungen mit Wert <> 0
							// abhaengig vom Parameter
							if (pruefeBelegInFibu && !isBelegInFibu(LocaleFac.BELEGART_RECHNUNG, rechnungen[i].getIId(),
									theClientDto)) {
								fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_RECHNUNG, rechnungen[i].getIId(),
										rechnungen[i].getCNr(), FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
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
	private boolean isBelegInFibu(String belegart, Integer belegIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		BelegbuchungDto bbDto = null;
		if (belegart.equals(LocaleFac.BELEGART_RECHNUNG)) {
			Rechnung rechnung = em.find(Rechnung.class, belegIId);
			String rechnungtyp = null;
			try {
				RechnungartDto rechnungartDto = getRechnungServiceFac()
						.rechnungartFindByPrimaryKey(rechnung.getRechnungartCNr(), theClientDto);
				rechnungtyp = rechnungartDto.getRechnungtypCNr();
			} catch (Exception ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			}
			if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_RECHNUNG)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_RECHNUNG, rechnung.getIId());
			} else if (rechnungtyp.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_GUTSCHRIFT, rechnung.getIId());
			}

		} else if (belegart.equals(LocaleFac.BELEGART_EINGANGSRECHNUNG)) {
			bbDto = getBelegbuchungFac(theClientDto.getMandant())
					.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_EINGANGSRECHNUNG, belegIId);

		} else if (belegart.equals(LocaleFac.BELEGART_REZAHLUNG)) {
			Rechnungzahlung zahlung = em.find(Rechnungzahlung.class, belegIId);
			if (zahlung.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)
					|| zahlung.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BAR)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_REZAHLUNG, belegIId);
			} else {
				// wird nicht gebucht --> true liefern
				return true;
			}

		} else if (belegart.equals(LocaleFac.BELEGART_ERZAHLUNG)) {
			Eingangsrechnungzahlung zahlung = em.find(Eingangsrechnungzahlung.class, belegIId);
			if (zahlung.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BANK)
					|| zahlung.getZahlungsartCNr().equals(RechnungFac.ZAHLUNGSART_BAR)) {
				bbDto = getBelegbuchungFac(theClientDto.getMandant())
						.belegbuchungFindByBelegartCNrBelegiidOhneExc(LocaleFac.BELEGART_ERZAHLUNG, belegIId);
			} else {
				// wird nicht gebucht --> true liefern
				return true;
			}
		}

		if (bbDto != null)
			return true;
		return false;
	}

	private ArrayList<FibuFehlerDto> pruefeEingangsrechnungenPeriode(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		EingangsrechnungDto[] rechnungen = null;
		try {
			rechnungen = getEingangsrechnungFac().eingangsrechnungFindByMandantCNrDatumVonBis(theClientDto.getMandant(),
					dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		if (rechnungen != null) {
			for (int i = 0; i < rechnungen.length; i++) {
				if (!rechnungen[i].getStatusCNr().equals(EingangsrechnungFac.STATUS_STORNIERT)) {
					if (rechnungen[i].getKostenstelleIId() == null) {
						try {
							BigDecimal bdOffen = getEingangsrechnungFac()
									.getWertNochNichtKontiert(rechnungen[i].getIId());
							if (bdOffen.doubleValue() != 0) {
								fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_EINGANGSRECHNUNG,
										rechnungen[i].getIId(), rechnungen[i].getCNr(),
										FibuFehlerDto.FEHLER_NICHT_VOLLSTAENDIG_KONTIERT));
							}
						} catch (EJBExceptionLP e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
					if (pruefeBelegInFibu && !isBelegInFibu(LocaleFac.BELEGART_EINGANGSRECHNUNG, rechnungen[i].getIId(),
							theClientDto)) {
						fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_EINGANGSRECHNUNG, rechnungen[i].getIId(),
								rechnungen[i].getCNr(), FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
					}
				}
			}
		}
		return fehler;
	}

	private ArrayList<FibuFehlerDto> pruefeEingangsrechnungZahlungenPeriode(ArrayList<FibuFehlerDto> fehler,
			Integer geschaeftsjahr, int periode, boolean pruefeBelegInFibu, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date dBeginn = new Date(tVonBis[0].getTime());
		Date dEnd = new Date(tVonBis[1].getTime());

		List<Integer> zahlungenIDs = null;
		try {
			zahlungenIDs = getEingangsrechnungFac()
					.eingangsrechnungzahlungIdsByMandantZahldatumVonBis(theClientDto.getMandant(), dBeginn, dEnd);
		} catch (EJBExceptionLP e1) {
			e1.printStackTrace();
		}
		if (zahlungenIDs != null) {
			for (int i = 0; i < zahlungenIDs.size(); i++) {
				Eingangsrechnungzahlung erz = em.find(Eingangsrechnungzahlung.class, zahlungenIDs.get(i));
				Integer rechnungIId = erz.getEingangsrechnungIId();
				Eingangsrechnung er = em.find(Eingangsrechnung.class, rechnungIId);
				if (er.getKontoIId() == null) {
					try {
						BigDecimal bdOffen = getEingangsrechnungFac().getWertNochNichtKontiert(rechnungIId);
						if (bdOffen.doubleValue() != 0) {
							fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_ERZAHLUNG, rechnungIId, er.getCNr(),
									FibuFehlerDto.FEHLER_NICHT_VOLLSTAENDIG_KONTIERT));
						}
					} catch (EJBExceptionLP e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				if (pruefeBelegInFibu
						&& !isBelegInFibu(LocaleFac.BELEGART_ERZAHLUNG, zahlungenIDs.get(i), theClientDto)) {
					fehler.add(new FibuFehlerDto(LocaleFac.BELEGART_ERZAHLUNG, rechnungIId, er.getCNr(),
							FibuFehlerDto.FEHLER_NICHT_IN_FIBU));
				}
			}
		}
		return fehler;
	}

	public Integer createUvaverprobung(UvaverprobungDto uvaverprobungDto, TheClientDto theClientDto) {
		if (uvaverprobungDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("uvaverprobungDto == null"));
		}
		if (uvaverprobungDto.getIGeschaeftsjahr() == null || uvaverprobungDto.getIMonat() == null
				|| uvaverprobungDto.getFinanzamtIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception(
					"uvaverprobungDto.getIGeschaeftsjahr() == null || uvaverprobungDto.getIMonat() == null || uvaverprobungDto.getFinanzamtIId() == null"));
		}

		uvaverprobungDto.setMandantCNr(theClientDto.getMandant());

		try {

			Query query = em.createNamedQuery("UvaverprobungfindByGeschaeftsjahrMonatFinanzamtMandant");
			query.setParameter(1, uvaverprobungDto.getIGeschaeftsjahr());
			query.setParameter(2, uvaverprobungDto.getIMonat().intValue() + uvaverprobungDto.getIAbrechnungszeitraum());
			query.setParameter(3, uvaverprobungDto.getFinanzamtIId());
			query.setParameter(4, uvaverprobungDto.getMandantCNr());
			query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_UVAVERPROBUNG.UK"));
		} catch (NoResultException ex) {

		}
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_UVAVERPROBUNG);
			uvaverprobungDto.setIId(pk);

			Uvaverprobung uvap = new Uvaverprobung(uvaverprobungDto.getIId(), uvaverprobungDto.getIGeschaeftsjahr(),
					uvaverprobungDto.getIMonat().intValue() + uvaverprobungDto.getIAbrechnungszeitraum(),
					uvaverprobungDto.getMandantCNr(), uvaverprobungDto.getFinanzamtIId(), theClientDto.getIDPersonal());
			uvap.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			em.persist(uvap);
			em.flush();
			return uvaverprobungDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void removeUvaverprobung(Integer iId) {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL, new Exception("iId == null"));
		}

		Uvaverprobung uvap = em.find(Uvaverprobung.class, iId);
		if (uvap == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeUvaverprobung. Es gibt keine Uvaverprobung mit der iid " + iId);
		}

		// zuerst die zugehoerigen verprobungen zuruecksetzen
		Query query = em.createNamedQuery("BuchungResetUvaverprobung");
		query.setParameter(1, iId);
		query.executeUpdate();

		em.remove(uvap);
		em.flush();
	}

	@SuppressWarnings("unchecked")
	public Integer uvaVerprobung(ReportUvaKriterienDto krit, TheClientDto theClientDto) {
		// Letzte Verprobung fuer das Finanzamt holen
		Query query = em.createNamedQuery("UvaverprobungfindLastByFinanzamtIIdMandant");
		query.setParameter(1, krit.getFinanzamtIId());
		query.setParameter(2, theClientDto.getMandant());
		query.setMaxResults(1);
		List<Uvaverprobung> list = (List<Uvaverprobung>) query.getResultList();
		boolean durchfuehren = true;
		if (list.size() > 0) {
			Uvaverprobung uvap = list.get(0);
			if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
				if (uvap.getGeschaeftsjahr().intValue() == krit.getIGeschaeftsjahr())
					if (uvap.getIMonat() >= krit.getIPeriode())
						durchfuehren = false;
			} else if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				if (uvap.getGeschaeftsjahr().intValue() > krit.getIGeschaeftsjahr())
					durchfuehren = false;
				else if (uvap.getGeschaeftsjahr().intValue() == krit.getIGeschaeftsjahr()) {
					if ((uvap.getIMonat() % UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL) >= krit.getIPeriode())
						durchfuehren = false;
				}
			} else if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {

			}
		}
		if (durchfuehren) {
			Integer[] perioden = null;
			UvaverprobungDto uvaverprobungDto = new UvaverprobungDto();
			if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_MONAT)) {
				perioden = new Integer[1];
				perioden[0] = krit.getIPeriode();
				uvaverprobungDto.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_MONAT);
			} else if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
				perioden = HelperServer.getMonateFuerQuartal(krit.getIPeriode());
				uvaverprobungDto.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_QUARTAL);
			} else if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
				uvaverprobungDto.setIAbrechnungszeitraum(UvaverprobungDto.UVAABRECHNUNGSZEITRAUM_JAHR);
			}

			uvaverprobungDto.setIGeschaeftsjahr(krit.getIGeschaeftsjahr());
			uvaverprobungDto.setIMonat(krit.getIPeriode());
			uvaverprobungDto.setFinanzamtIId(krit.getFinanzamtIId());
			Integer uvaverprobungIId = createUvaverprobung(uvaverprobungDto, theClientDto);

			// alle Belege markieren
			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT DISTINCT o.buchung_i_id FROM FLRFinanzBuchungDetail o"
					+ " WHERE o.konto_i_id IN (SELECT k.i_id FROM FLRFinanzKonto k WHERE k.finanzamt_i_id = :pFinanzamt)"
					+ " AND o.flrbuchung.t_storniert IS null"
					+ " AND o.flrbuchung.d_buchungsdatum >= :pVon AND o.flrbuchung.d_buchungsdatum < :pEnd";
			org.hibernate.Query hquery = session.createQuery(queryString);
			for (int i = 0; i < perioden.length; i++) {
				Timestamp[] tVonBis = getBuchenFac().getDatumbereichPeriodeGJ(krit.getIGeschaeftsjahr(), perioden[i],
						theClientDto);
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
	public UvaverprobungDto getLetzteVerprobung(Integer partnerIIdFinanzamt, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("UvaverprobungfindLastByFinanzamtIIdMandant");
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

	public Map<Integer, SteuerkontoInfo> getAllIIdsUstkontoMitIIdMwstBez(Integer finanzamtIId,
			TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto, STEUERART_UST);
	}

	public Map<Integer, SteuerkontoInfo> getAllIIdsVstkontoMitIIdMwstBez(Integer finanzamtIId,
			TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto, STEUERART_VST);
	}

	public Map<Integer, SteuerkontoInfo> getAllIIdsEUstkontoMitIIdMwstBez(Integer finanzamtIId,
			TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto, STEUERART_EUST);
	}

	public Map<Integer, SteuerkontoInfo> getAllIIdsSteuerkontoMitIIdMwstBez(Integer finanzamtIId,
			TheClientDto theClientDto) {
		return getAllIIdsSteuerkontoMitIIdMwstBez(finanzamtIId, theClientDto, null);
	}

	/**
	 * 
	 * @param finanzamtIId
	 * @param theClientDto
	 * @return eine Map mit den KontoIIds als Key und den MwstsatzbezIId als Value
	 */
	private Map<Integer, SteuerkontoInfo> getAllIIdsSteuerkontoMitIIdMwstBez(Integer finanzamtIId,
			TheClientDto theClientDto, String steuerart) {
		// Map<Integer, Integer> mwstKonten = new HashMap<Integer, Integer>();

		Map<Integer, SteuerkontoInfo> mwstKonten = new HashMap<Integer, SteuerkontoInfo>();

		SteuerkategorieDto[] stkDtos = steuerkategorieFindByFinanzamtIId(finanzamtIId, theClientDto);
		for (SteuerkategorieDto stkat : stkDtos) {
			SteuerkategoriekontoDto[] stkks = steuerkategoriekontoFindAll(stkat.getIId());
			for (SteuerkategoriekontoDto stkk : stkks) {
				if ((steuerart == null || STEUERART_VST.equals(steuerart)) && stkk.getKontoIIdEk() != null) {
					SteuerkontoInfo skInfo = mwstKonten.get(stkk.getKontoIIdEk());
					if (skInfo == null) {
						mwstKonten.put(stkk.getKontoIIdEk(), new SteuerkontoInfo(stkk.getKontoIIdEk(),
								stkat.getReversechargeartId(), stkk.getMwstsatzbezIId()));
					} else {
						skInfo.addDuplicate(stkat.getReversechargeartId(), stkk.getMwstsatzbezIId());
					}
				}
				if ((steuerart == null || STEUERART_UST.equals(steuerart)) && stkk.getKontoIIdVk() != null) {
					SteuerkontoInfo skInfo = mwstKonten.get(stkk.getKontoIIdVk());
					if (skInfo == null) {
						mwstKonten.put(stkk.getKontoIIdVk(), new SteuerkontoInfo(stkk.getKontoIIdVk(),
								stkat.getReversechargeartId(), stkk.getMwstsatzbezIId()));
					} else {
						skInfo.addDuplicate(stkat.getReversechargeartId(), stkk.getMwstsatzbezIId());
					}
				}
				if ((steuerart == null || STEUERART_EUST.equals(steuerart)) && stkk.getKontoIIdEinfuhrUst() != null) {
					SteuerkontoInfo skInfo = mwstKonten.get(stkk.getKontoIIdEinfuhrUst());
					if (skInfo == null) {
						mwstKonten.put(stkk.getKontoIIdEinfuhrUst(), new SteuerkontoInfo(stkk.getKontoIIdEinfuhrUst(),
								stkat.getReversechargeartId(), stkk.getMwstsatzbezIId()));
					} else {
						skInfo.addDuplicate(stkat.getReversechargeartId(), stkk.getMwstsatzbezIId());
					}
				}
			}
		}
		return mwstKonten;
	}

	public Set<Integer> getAllMitlaufendeKonten(Integer finanzamtIId, TheClientDto theClientDto) {
		Set<Integer> mitlaufendeKonten = new HashSet<Integer>();
		SteuerkategorieDto[] stkDtos = steuerkategorieFindByFinanzamtIId(finanzamtIId, theClientDto);
		for (SteuerkategorieDto stkat : stkDtos) {
			if (stkat.getKontoIIdForderungen() != null)
				mitlaufendeKonten.add(stkat.getKontoIIdForderungen());
			if (stkat.getKontoIIdVerbindlichkeiten() != null)
				mitlaufendeKonten.add(stkat.getKontoIIdVerbindlichkeiten());
		}
		return mitlaufendeKonten;

	}

	private UvaverprobungDto assembleUvaverprobungDto(Uvaverprobung uvaverprobung) {
		return UvaverprobungDtoAssembler.createDto(uvaverprobung);
	}

	public void createDefaultSteuerkategoriekonto(Integer steuerkategorieIId, TheClientDto theClientDto) {
		MwstsatzbezDto[] mwstsatzbezDtos = getMandantFac().mwstsatzbezFindAllByMandantAsDto(theClientDto.getMandant(),
				theClientDto);
		if (mwstsatzbezDtos.length > 0) {
			for (int i = 0; i < mwstsatzbezDtos.length; i++) {
				// if (!mwstsatzbezDtos[i].getBHandeingabe()) {
				SteuerkategoriekontoDto steuerkategoriekontoDto = new SteuerkategoriekontoDto();
				steuerkategoriekontoDto.setSteuerkategorieIId(steuerkategorieIId);
				steuerkategoriekontoDto.setMwstsatzbezIId(mwstsatzbezDtos[i].getIId());
				//SP8594 -> Default = 1.1.1970
				steuerkategoriekontoDto.setTGueltigAb(new Timestamp(0));
				createSteuerkategoriekonto(steuerkategoriekontoDto, theClientDto);
				// }
			}
		}
	}

	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(String nr, Integer reversechargeartId,
			Integer finanzamtIId, TheClientDto theClient) {
		Query query = em.createNamedQuery("SteuerkategorieByCNrReversechargeartIdFinanzamtIIDMandant");
		query.setParameter(1, nr);
		query.setParameter(2, reversechargeartId);
		query.setParameter(3, finanzamtIId);
		query.setParameter(4, theClient.getMandant());
		Steuerkategorie steuerkategorie = null;
		try {
			steuerkategorie = (Steuerkategorie) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		return SteuerkategorieDtoAssembler.createDto(steuerkategorie);
	}

	public SteuerkategorieDto steuerkategorieFindByCNrFinanzamtIId(String nr, Integer finanzamtIId,
			TheClientDto theClient) {
		Query query = em.createNamedQuery("SteuerkategorieByCNrFinanzamtIIDMandant");
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

	public SteuerkategorieDto[] steuerkategorieFindByFinanzamtIId(Integer finanzamtIId, TheClientDto theClient) {
		Query query = em.createNamedQuery("SteuerkategorieByFinanzamtIIDMandant");
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

	public SteuerkategorieDto[] steuerkategorieFindByFinanzamtIId(Integer finanzamtIId, Integer reversechargeartId,
			TheClientDto theClient) {
		Query query = em.createNamedQuery("SteuerkategorieByFinanzamtIIDReversechargeartMandant");
		query.setParameter(1, finanzamtIId);
		query.setParameter(2, reversechargeartId);
		query.setParameter(3, theClient.getMandant());

		Collection<?> cl = query.getResultList();
		SteuerkategorieDto[] result = SteuerkategorieDtoAssembler.createDtos(cl);
		return result;
	}

	private SteuerkategorieDto setupDefaultSteuerkategorie(String cnr, String cBez, Integer reversechargeartId) {
		SteuerkategorieDto dto = new SteuerkategorieDto();
		dto.setCNr(cnr);
		dto.setCBez(cBez);
		dto.setReversechargeartId(reversechargeartId);
		return dto;
	}

	public void createIfNeededSteuerkategorieForFinanzamtIId(Integer finanzamtIId, TheClientDto theClientDto) {
		SteuerkategorieDto[] kategorien = steuerkategorieFindByFinanzamtIId(finanzamtIId, theClientDto);
		if (kategorien != null && kategorien.length > 0)
			return;

		// Die Standardwerte werden nur installiert wenn noch gar nichts
		// vorhanden ist
		kategorien = new SteuerkategorieDto[6];
		kategorien[0] = setupDefaultSteuerkategorie("Inland", "Inland", 0);
		kategorien[1] = setupDefaultSteuerkategorie("InlandReverse", "Inland Reverse Charge", 1);
		kategorien[2] = setupDefaultSteuerkategorie("AuslandEUmUID", "Ausland EU mit UID", 0);
		kategorien[3] = setupDefaultSteuerkategorie("AuslandEUoUID", "Ausland EU ohne UID", 0);
		kategorien[4] = setupDefaultSteuerkategorie("Ausland", "Ausland nicht EU", 0);
		kategorien[5] = setupDefaultSteuerkategorie("AuslandReverse", "Ausland Reverse Charge", 1);

		Integer ids[] = new Integer[6];

		for (int i = 0; i < kategorien.length; i++) {
			kategorien[i].setFinanzamtIId(finanzamtIId);
			ids[i] = createSteuerkategorie(kategorien[i], theClientDto);
		}
	}

	public void createIfNeededSteuerkategorieForFinanzamtIId(Integer finanzamtIId, Integer reversechargeartId,
			TheClientDto theClientDto) {
		SteuerkategorieDto[] kategorien = steuerkategorieFindByFinanzamtIId(finanzamtIId, reversechargeartId,
				theClientDto);
		if (kategorien != null && kategorien.length > 0)
			return;

		// Die Standardwerte werden nur installiert wenn noch gar nichts
		// vorhanden ist
		kategorien = new SteuerkategorieDto[] { setupDefaultSteuerkategorie("Inland", "Inland", reversechargeartId),
				setupDefaultSteuerkategorie("AuslandEUmUID", "Ausland EU mit UID", reversechargeartId),
				setupDefaultSteuerkategorie("AuslandEUoUID", "Ausland EU ohne UID", reversechargeartId),
				setupDefaultSteuerkategorie("Ausland", "Ausland nicht EU", reversechargeartId) };

		for (int i = 0; i < kategorien.length; i++) {
			kategorien[i].setFinanzamtIId(finanzamtIId);
			kategorien[i].setIId(createSteuerkategorie(kategorien[i], theClientDto));
		}
	}

	private void createIfNeededSteuerkategorieKontoForMwstsatzbez(SteuerkategorieDto[] kategorien,
			MwstsatzbezDto[] mwstsatzbez, Timestamp gueltigAm, TheClientDto theClientDto) {
		for (SteuerkategorieDto steuerkategorieDto : kategorien) {
			for (MwstsatzbezDto mwstsatzbezDto : mwstsatzbez) {
				HvOptional<SteuerkategoriekontoDto> dto = steuerkategoriekontoZuDatum(steuerkategorieDto.getIId(),
						mwstsatzbezDto.getIId(), gueltigAm);
				if (!dto.isPresent()) {
					SteuerkategoriekontoDto newKonto = new SteuerkategoriekontoDto();
					newKonto.setSteuerkategorieIId(steuerkategorieDto.getIId());
					newKonto.setMwstsatzbezIId(mwstsatzbezDto.getIId());
					newKonto.setTGueltigAb(gueltigAm);
					createSteuerkategoriekonto(newKonto, theClientDto);
				}
			}
		}
	}

	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(Integer finanzamtIId, TheClientDto theClientDto) {
		ReversechargeartDto rcartOhneDto;
		try {
			rcartOhneDto = reversechargeartFindOhne(theClientDto);
			createIfNeededSteuerkategoriekontoForFinanzamtIId(finanzamtIId, rcartOhneDto.getIId(),
					Helper.FIBU_GUELTIG_AB, theClientDto);
		} catch (RemoteException e) {
			throw getThrowEJBExceptionLPRespectOld(e);
		}
	}

	public void createIfNeededSteuerkategoriekontoForFinanzamtIId(Integer finanzamtIId, Integer reversechargeartId,
			Timestamp gueltigAb, TheClientDto theClientDto) {
		SteuerkategorieDto[] kategorien = steuerkategorieFindByFinanzamtIId(finanzamtIId, reversechargeartId,
				theClientDto);
		if (null == kategorien || kategorien.length == 0) {
			createIfNeededSteuerkategorieForFinanzamtIId(finanzamtIId, reversechargeartId, theClientDto);
			kategorien = steuerkategorieFindByFinanzamtIId(finanzamtIId, reversechargeartId, theClientDto);
		}
		if (null == kategorien)
			return;

		MwstsatzbezDto[] mwstsatzbez = getMandantFac().mwstsatzbezFindAllByMandantAsDto(theClientDto.getMandant(),
				theClientDto);

		createIfNeededSteuerkategorieKontoForMwstsatzbez(kategorien, mwstsatzbez, gueltigAb, theClientDto);
	}

	public Integer createAuszifferung(Integer[] buchungdetailIds, TheClientDto theClientDto) {

		if (buchungdetailIds.length == 0)
			return null;

//		pruefeGeschaeftsjahr(theClientDto, buchungdetailIds);

		List<Buchungdetail> buchungdetails = new ArrayList<Buchungdetail>();
		Integer azk = null;

		for (Integer iid : buchungdetailIds) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iid);
			if (buchungdetail == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "Nicht gefunden: Buchungdetail ID " + iid);
			}

			Buchung buchung = em.find(Buchung.class, buchungdetail.getBuchungIId());

			if (buchung.getTStorniert() != null)
				return null;

			if (azk == null) {
				azk = buchungdetail.getIAusziffern();
			} else if (buchungdetail.getIAusziffern() != null && !azk.equals(buchungdetail.getIAusziffern())) {
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

		if (buchungdetails.size() > 0) {
			pruefeGeschaeftsjahr(theClientDto, buchungdetailIds);
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

	public Integer createAuszifferung(Integer geschaeftsjahrVerursacher, Integer[] buchungdetailIds,
			TheClientDto theClientDto) {
		Validator.notNull(geschaeftsjahrVerursacher, "geschaeftsjahrVerursacher");
		Integer[] ids = filterBuchungdetailIdsGJ(geschaeftsjahrVerursacher, buchungdetailIds);
		return createAuszifferung(ids, theClientDto);
	}

	private Integer[] filterBuchungdetailIdsGJ(Integer geschaeftsjahr, Integer[] buchungdetailIds) {
		List<Integer> ids = new ArrayList<Integer>();
		for (Integer iid : buchungdetailIds) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iid);
			if (buchungdetail == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "Nicht gefunden: Buchungdetail ID " + iid);
			}

			Buchung buchung = em.find(Buchung.class, buchungdetail.getBuchungIId());

			if (buchung.getTStorniert() != null) {
				continue;
			}

			if (!buchung.getGeschaeftsjahr().equals(geschaeftsjahr)) {
				continue;
			}

			ids.add(iid);
		}

		return ids.toArray(new Integer[ids.size()]);
	}

	public void removeAuszifferung(Integer[] buchungdetailIds, TheClientDto theClient) {

		pruefeGeschaeftsjahr(theClient, buchungdetailIds);

		for (Integer iid : buchungdetailIds) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iid);
			if (buchungdetail != null) {
				buchungdetail.setIAusziffern(null);
				em.merge(buchungdetail);
			}
		}
		em.flush();
	}

	public Integer updateAuszifferung(Integer geschaeftsjahrVerursacher, Integer auszifferKennzeichen,
			Integer[] buchungdetailIds, TheClientDto theClientDto) {
		Validator.notNull(geschaeftsjahrVerursacher, "geschaeftsjahrVerursacher");
		Integer[] ids = filterBuchungdetailIdsGJ(geschaeftsjahrVerursacher, buchungdetailIds);
		return updateAuszifferung(auszifferKennzeichen, ids, theClientDto);
	}

	public Integer updateAuszifferung(Integer auszifferKennzeichen, Integer[] buchungdetailIds,
			TheClientDto theClientDto) {

		boolean changedEm = false;

		// pruefeGeschaeftsjahr(theClientDto, buchungdetailIds);

		if (auszifferKennzeichen == null) {
			// SP 2013/01093
			// aus erster Buchung mit Kennzeichen holen
			for (int i = 0; i < buchungdetailIds.length; i++) {
				Buchungdetail buchungdetail = em.find(Buchungdetail.class, buchungdetailIds[i]);
				if (buchungdetail == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
							"Nicht gefunden: Buchungdetail ID " + buchungdetailIds[i]);
				}

				auszifferKennzeichen = buchungdetail.getIAusziffern();
				if (auszifferKennzeichen != null)
					break;
			}
		}

		for (int i = 0; i < buchungdetailIds.length; i++) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, buchungdetailIds[i]);
			if (buchungdetail != null) {
				// Pruefung des Geschaeftsjahres nur, wenn auch tatsaechlich ein
				// AZK geaendert wird
				// Fall: Zahlung im GJ2015 machen fuer eine Rechnung die im
				// GJ2014 erstellt wurde und
				// bereits Zahlungen enthaelt -> dann sollen "natuerlich" nur
				// die 2015er Zahlungen
				// sofern notwendig abgeaendert werden koennen
				if (Helper.equalsNull(auszifferKennzeichen, buchungdetail.getIAusziffern()))
					continue;
				pruefeGeschaeftsjahr(theClientDto, new Integer[] { buchungdetail.getIId() });

				buchungdetail.setIAusziffern(auszifferKennzeichen);
				em.merge(buchungdetail);
				changedEm = true;

				// if ((buchungdetail.getIAusziffern() == null)
				// || (buchungdetail.getIAusziffern().compareTo(
				// auszifferKennzeichen) != 0)) {
				//
				// if(!checkedGJ) {
				// pruefeGeschaeftsjahr(theClientDto, buchungdetailIds);
				// checkedGJ = true ;
				// }
				//
				// buchungdetail.setIAusziffern(auszifferKennzeichen);
				// em.merge(buchungdetail);
				// changedEm = true ;
				// }
			}
		}

		if (changedEm) {
			em.flush();
		}

		return auszifferKennzeichen;
	}

	private void pruefeGeschaeftsjahr(TheClientDto theClientDto, Integer[] buchungdetailIds) {

		Validator.notNull(theClientDto, "theClientDto");

		for (Integer iid : buchungdetailIds) {
			Buchungdetail buchungdetail = em.find(Buchungdetail.class, iid);
			if (buchungdetail != null) {
				Buchung buchung = em.find(Buchung.class, buchungdetail.getBuchungIId());
				getSystemFac().pruefeGeschaeftsjahrSperre(buchung.getGeschaeftsjahr(), theClientDto.getMandant());
			}
		}

	}

	public boolean isFibuLandunterschiedlich(Integer partner1IId, Integer partner2IId) {
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
			Integer finanzamtIId, int iGeschaeftsjahr, int iPeriode, int iAbrechnungszeitraum,
			TheClientDto theClientDto) {
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
	public List<SteuerkategoriekontoDto> steuerkategorieFindByKontoIIdMwstSatzBezIId(Integer kontoId,
			Integer mwstsatzBezId, Timestamp gueltigAm) {
		Validator.pkFieldNotNull(kontoId, "kontoId");
		Validator.pkFieldNotNull(mwstsatzBezId, "mwstSatzBezId");
		Validator.notNull(gueltigAm, "gueltigAm");

		SteuerkategoriekontoQuery q = new SteuerkategoriekontoQuery(em);
		List<Steuerkategoriekonto> entries = q.listKontoId(kontoId, mwstsatzBezId, gueltigAm);
		return Arrays.asList(assembleSteuerkategoriekontoDtos(entries));
		/*
		 * HvTypedQuery<Steuerkategoriekonto> query = new
		 * HvTypedQuery<Steuerkategoriekonto>(
		 * em.createNamedQuery("SteuerkategoriekontoByKontoIIdandMwStSatzBeziid"));
		 * query.setParameter(1, kontoIId).setParameter(2, mwstSatzBezIId); return
		 * Arrays.asList(assembleSteuerkategoriekontoDtos(query .getResultList()));
		 */
	}

	@Override
	public BigDecimal getLiquiditaetsKontostand(Integer geschaeftsjahrIId, TheClientDto theClientDto) {

		if (getBenutzerServicesFac().hatRecht(RechteFac.RECHT_FB_FINANZ_R, theClientDto)
				|| getBenutzerServicesFac().hatRecht(RechteFac.RECHT_FB_FINANZ_CUD, theClientDto)) {
			HvTypedQuery<Bankverbindung> query = new HvTypedQuery<Bankverbindung>(
					em.createNamedQuery(Bankverbindung.BankverbindungFindForLiquiditaetsvorschau));
			query.setParameter("mandant", theClientDto.getMandant());
			List<Bankverbindung> bankverbindungen = query.getResultList();
			BigDecimal saldo = BigDecimal.ZERO;
			for (Bankverbindung bv : bankverbindungen) {
				BigDecimal s = getBuchenFac().getAktuellenSaldoVonKontoFuerGeschaeftsjahr(bv.getKontoIId(),
						geschaeftsjahrIId);
				saldo = saldo.add(s);
			}
			return saldo;
		} else {
			// SP18971
			return null;
		}

	}

	class KontoVerifier {
		private HashMap<Integer, SteuerkategorieEntry> kontos;
		private List<KontoVerifierEntry> failedEntries;

		public KontoVerifier() {
			kontos = new HashMap<Integer, SteuerkategorieEntry>();
			failedEntries = new ArrayList<KontoVerifierEntry>();
		}

		public boolean accept(SteuerkategorieEntry entry) {
			SteuerkategorieEntry foundEntry = kontos.get(entry.getKontoId());
			if (foundEntry == null) {
				kontos.put(entry.getKontoId(), entry);
				return true;
			}

			// Doch keine "erlaubten" Doppelverwendungen von Konten
			// boolean accept =
			// entry.getKategorie().getIId().equals(foundEntry.getKategorie().getIId())
			// ;
			// if(accept) {
			// String foundDruckname =
			// foundEntry.getMwstsatzbezDto().getCDruckname() ;
			// String entryDruckname = entry.getMwstsatzbezDto().getCDruckname()
			// ;
			// if(foundDruckname == null && entryDruckname == null) {
			// return true ;
			// }
			//
			// if(foundDruckname == null || entryDruckname == null
			// || foundDruckname.equals(entryDruckname)) {
			// return true ;
			// }
			// }

			KontoVerifierEntry failedEntry = new KontoVerifierEntry(foundEntry, entry);
			failedEntries.add(failedEntry);

			myLogger.warn("Kontoverifikation fehlgeschlagen bei: " + failedEntry.toString());
			return false;
		}

		protected SteuerkategorieEntry createFrom(FLRSteuerkategoriekonto flrSteuerkategoriekonto, Integer kontoId,
				String kontoVerwendung, TheClientDto theClientDto) throws RemoteException {
			SteuerkategoriekontoDto kategorieKontoDto = steuerkategoriekontoFindByPrimaryKey(
					new SteuerkategoriekontoId(flrSteuerkategoriekonto.getI_id()));
//					steuerkategoriekontoFindByPrimaryKey(
//							flrSteuerkategoriekonto.getId_comp()
//									.getSteuerkategorieiid(),
//							flrSteuerkategoriekonto.getId_comp()
//									.getMwstsatzbeziid());
			SteuerkategorieDto kategorieDto = steuerkategorieFindByPrimaryKey(
					flrSteuerkategoriekonto.getFlrsteuerkategorie().getI_id(), theClientDto);
			MwstsatzbezDto mwstsatzBezDto = getMandantFac()
					.mwstsatzbezFindByPrimaryKey(kategorieKontoDto.getMwstsatzbezIId(), theClientDto);

			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(kontoId);
			return new SteuerkategorieEntry(kategorieKontoDto, kategorieDto, kontoId, kontoDto.getCNr(),
					kontoDto.getCBez(), kontoVerwendung, mwstsatzBezDto);
		}

		public boolean accept(FLRSteuerkategoriekonto flrSteuerkategoriekonto, Integer kontoId, String kontoBez,
				TheClientDto theClientDto) throws RemoteException {
			if (kontoId == null)
				return true;
			return accept(createFrom(flrSteuerkategoriekonto, kontoId, kontoBez, theClientDto));
		}

		public boolean accept(FLRSteuerkategoriekonto flrSteuerkategoriekonto, FLRFinanzKonto konto, String kontoBez,
				TheClientDto theClientDto) throws RemoteException {
			if (konto == null)
				return true;
			return accept(createFrom(flrSteuerkategoriekonto, konto.getI_id(), kontoBez, theClientDto));
		}

		public List<KontoVerifierEntry> getFailedEntries() {
			return failedEntries;
		}
	}

	private KontoVerifier pruefeSteuerkontenImpl(Integer finanzamtId, TheClientDto theClientDto)
			throws RemoteException {
		KontoVerifier verifier = new KontoVerifier();

		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT DISTINCT (o) FROM FLRSteuerkategoriekonto o"
				+ " WHERE o.flrsteuerkategorie.finanzamt_i_id = :pFinanzamt";
		org.hibernate.Query hquery = session.createQuery(queryString);
		hquery.setParameter("pFinanzamt", finanzamtId);
		List<FLRSteuerkategoriekonto> results = hquery.list();

		HashSet<Integer> knownSteuerkategorien = new HashSet<Integer>();
		for (FLRSteuerkategoriekonto stkk : results) {
			// if(!knownSteuerkategorien.contains(stkk.getFlrsteuerkategorie().getI_id()))
			// {
			// Steuerkategorie stk = em.find(Steuerkategorie.class, stkk
			// .getFlrsteuerkategorie().getI_id());
			//
			// verifier.accept(stkk, stk.getKontoIIdForderungen(),
			// "Forderungen", theClientDto) ;
			// verifier.accept(stkk, stk.getKontoIIdVerbindlichkeiten(),
			// "Verbindlichkeiten", theClientDto) ;
			// verifier.accept(stkk, stk.getKontoIIdKursgewinn(), "Kursgewinn",
			// theClientDto) ;
			// verifier.accept(stkk, stk.getKontoIIdKursverlust(),
			// "Kursverlust", theClientDto);
			//
			// knownSteuerkategorien.add(stkk.getFlrsteuerkategorie().getI_id());
			// }

			verifier.accept(stkk, stkk.getKontoiidvk(), "VK", theClientDto);
			verifier.accept(stkk, stkk.getKontoiidskontoek(), "Skontoaufwand", theClientDto);
			verifier.accept(stkk, stkk.getKontoiidek(), "EK", theClientDto);
			verifier.accept(stkk, stkk.getKontoiidskontovk(), "Skontoerloes", theClientDto);
			verifier.accept(stkk, stkk.getKontoiideinfuhrust(), "EinfuhrUst", theClientDto);
		}

		session.close();

		return verifier;
	}

	@Override
	public void pruefeSteuerkonten(Integer finanzamtId, TheClientDto theClientDto) throws RemoteException {
		KontoVerifier verifier = pruefeSteuerkontenImpl(finanzamtId, theClientDto);

		if (verifier.getFailedEntries().size() > 0) {
			EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_STEUERKONTEN_MEHRFACH_VERWENDET,
					new Exception(finanzamtId.toString()));
			ArrayList<Object> clientInfos = new ArrayList<Object>();
			clientInfos.addAll(verifier.getFailedEntries());
			ex.setAlInfoForTheClient(clientInfos);
			throw ex;
		}
	}

	public Integer createReversechargeart(ReversechargeartDto reversechargeartDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(reversechargeartDto, "reversechargeartDt");
		Validator.pkFieldNotNull(reversechargeartDto.getCNr(), "cnr");

		if (reversechargeartDto.getMandantCNr() == null) {
			reversechargeartDto.setMandantCNr(theClientDto.getMandant());
		}

		try {
			ReversechargeartQuery.findByCnrMandant(em, reversechargeartDto.getCNr(),
					reversechargeartDto.getMandantCNr());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FB_REVERSECHARGEART.UK"));
		} catch (NoResultException ex) {
		}

		try {
			Integer isort = ReversechargeartQuery.nextISortByMandant(em, reversechargeartDto.getMandantCNr());
			reversechargeartDto.setISort(isort);
			Reversechargeart reversechargeart = new Reversechargeart(reversechargeartDto.getCNr());
			reversechargeart.setMandantCNr(reversechargeartDto.getMandantCNr());
			reversechargeart.setISort(reversechargeartDto.getISort());
			if (reversechargeartDto.getBVersteckt() != null) {
				reversechargeart.setBVersteckt(Helper.boolean2Short(reversechargeartDto.getBVersteckt()));
			}
			em.persist(reversechargeart);
			em.flush();

			if (reversechargeartDto.getSprDto() != null) {
				Reversechargeartspr spr = new Reversechargeartspr(reversechargeart.getIId(),
						theClientDto.getLocUiAsString());
				em.persist(spr);
				em.flush();
				setReversechargeartsprFromReversechargeartsprDto(spr, reversechargeartDto.getSprDto());
			}
			return reversechargeart.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception(e));
		}
	}

	public void updateReversechargeart(ReversechargeartDto reversechargeartDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(reversechargeartDto, "reversechargeartDto");
		Validator.pkFieldNotNull(reversechargeartDto.getIId(), "iId");
		Validator.notEmpty(reversechargeartDto.getMandantCNr(), "mandantCnr");
		Validator.notEmpty(reversechargeartDto.getCNr(), "cnr");

		Integer iId = reversechargeartDto.getIId();
		Reversechargeart reversechargeart = em.find(Reversechargeart.class, iId);
		if (reversechargeart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateReversechargeart. Es gibt keine Reversechargeart mit iid " + iId);
		}

		setReversechargeartFromReversechargeartDto(reversechargeart, reversechargeartDto);

		if (reversechargeartDto.getSprDto() != null) {
			Reversechargeartspr spr = em.find(Reversechargeartspr.class,
					new ReversechargeartsprPK(reversechargeartDto.getIId(), theClientDto.getLocUiAsString()));
			if (spr == null) {
				spr = new Reversechargeartspr(reversechargeartDto.getIId(), theClientDto.getLocUiAsString());
				em.persist(spr);
				em.flush();
			}
			setReversechargeartsprFromReversechargeartsprDto(spr, reversechargeartDto.getSprDto());
		}
	}

	public void removeReversechargeart(Integer iId) {
		Validator.pkFieldNotNull(iId, "iId");

		Reversechargeart reversechargeart = em.find(Reversechargeart.class, iId);
		if (reversechargeart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeReversechargeart. Es gibt keine Reversechargeart mit der iid " + iId);
		}

		Query query = em.createNamedQuery("ReversechargeartsprfindByReversechargeartIId").setParameter(1, iId);
		Collection<Reversechargeartspr> cl = query.getResultList();
		for (Reversechargeartspr reversechargeartspr : cl) {
			em.remove(reversechargeartspr);
		}

		em.remove(reversechargeart);
		em.flush();
	}

	@Override
	public ReversechargeartDto reversechargeartFindByPrimaryKey(Integer reversechargeartId, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(reversechargeartId, "reversechargeartId");
		Reversechargeart entity = em.find(Reversechargeart.class, reversechargeartId);
		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei reversechargeart FindByPrimaryKey. Es gibt keine reversechargeart mit iid "
							+ reversechargeartId);
		}

		ReversechargeartDto dto = assembleReversechargeartDto(entity);

		ReversechargeartsprDto sprDto = getReversechargeartsprDto(reversechargeartId, theClientDto.getLocUi(),
				theClientDto.getLocMandant());
		dto.setSprDto(sprDto);

		return dto;
	}

	public Map<Integer, ReversechargeartDto> reversechargeartFindByPrimaryKeys(TheClientDto theClientDto,
			Integer... keys) throws RemoteException {
		Map<Integer, ReversechargeartDto> dtos = new HashMap<Integer, ReversechargeartDto>();
		for (Integer pk : keys) {
			dtos.put(pk, reversechargeartFindByPrimaryKey(pk, theClientDto));
		}
		return dtos;
	}

	@Override
	public ReversechargeartDto reversechargeartFindByCnrMandant(String reversechargeartCnr, TheClientDto theClientDto)
			throws RemoteException {
		ReversechargeartDto dto = reversechargeartFindByCnrMandant(reversechargeartCnr, theClientDto.getMandant());
		ReversechargeartsprDto sprDto = getReversechargeartsprDto(dto.getIId(), theClientDto.getLocUi(),
				theClientDto.getLocMandant());
		dto.setSprDto(sprDto);

		return dto;
	}

	private ReversechargeartDto reversechargeartFindByCnrMandant(String reversechargeartCnr, String mandantCnr)
			throws RemoteException {
		Reversechargeart entity = ReversechargeartQuery.findByCnrMandant(em, reversechargeartCnr, mandantCnr);
		ReversechargeartDto dto = assembleReversechargeartDto(entity);
		return dto;
	}

	@Override
	public ReversechargeartDto reversechargeartFindOhne(String mandantCNr) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.OHNE, mandantCNr);
	}

	@Override
	public ReversechargeartDto reversechargeartFindLeistung(String mandantCNr) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.LEISTUNG, mandantCNr);
	}

	@Override
	public ReversechargeartDto reversechargeartFindOhne(TheClientDto theClientDto) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.OHNE, theClientDto);
	}

	@Override
	public ReversechargeartDto reversechargeartFindLeistung(TheClientDto theClientDto) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.LEISTUNG, theClientDto);
	}

	private ReversechargeartDto assembleReversechargeartDto(Reversechargeart entity) {
		return ReversechargeartDtoAssembler.createDto(entity);
	}

	private ReversechargeartsprDto getReversechargeartsprDto(Integer reversechargeartId, Locale localeUi,
			Locale localeMandant) {

		Reversechargeartspr rcartspr = getReversechargeartspr(reversechargeartId, localeUi);
		if (rcartspr == null) {
			rcartspr = getReversechargeartspr(reversechargeartId, localeMandant);
		}
		if (rcartspr == null) {
			rcartspr = new Reversechargeartspr(reversechargeartId, Helper.locale2String(localeUi));
			Reversechargeart rcart = em.find(Reversechargeart.class, reversechargeartId);
			rcartspr.setCBez(rcart.getCNr());
		}

		return assembleReversechargeartsprDto(rcartspr);

	}

	private Reversechargeartspr getReversechargeartspr(Integer iId, Locale locale) {
		Reversechargeartspr rcartspr = em.find(Reversechargeartspr.class,
				new ReversechargeartsprPK(iId, Helper.locale2String(locale)));
		return rcartspr;
	}

	private ReversechargeartsprDto assembleReversechargeartsprDto(Reversechargeartspr entity) {
		return ReversechargeartsprDtoAssembler.createDto(entity);
	}

	private void setReversechargeartFromReversechargeartDto(Reversechargeart reversechargeart,
			ReversechargeartDto reversechargeartDto) {
		reversechargeart.setISort(reversechargeartDto.getISort());
		reversechargeart.setCNr(reversechargeartDto.getCNr());
		em.merge(reversechargeart);
		em.flush();
	}

	private void setReversechargeartsprFromReversechargeartsprDto(Reversechargeartspr spr,
			ReversechargeartsprDto sprDto) {
		spr.setCBez(sprDto.getcBez());
		em.merge(spr);
		em.flush();
	}

	public Map<Integer, String> getAllReversechargeArt(TheClientDto theClientDto) {
		return getAllReversechargeArt(Boolean.FALSE, theClientDto);
	}

	public Map<Integer, String> getAllReversechargeArt(Boolean mitVersteckten, TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");

		List<Reversechargeart> rcarts = Boolean.TRUE.equals(mitVersteckten)
				? ReversechargeartQuery.listByMandantMitVersteckten(em, theClientDto.getMandant())
				: ReversechargeartQuery.listByMandant(em, theClientDto.getMandant());
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();

		for (Reversechargeart reversechargeart : rcarts) {
			Integer id = reversechargeart.getIId();
			ReversechargeartsprDto sprDto = getReversechargeartsprDto(reversechargeart.getIId(),
					theClientDto.getLocUi(), theClientDto.getLocMandant());
			map.put(id, sprDto.getcBez());
		}

		return map;
	}

	public Map<Integer, String> getAllReversechargeartAllowed(TheClientDto theClientDto) throws RemoteException {
		return getAllReversechargeartAllowed(Boolean.FALSE, theClientDto);
	}
	@Override
	public Map<Integer, String> getAllReversechargeartAllowed(Boolean mitVersteckten, TheClientDto theClientDto) throws RemoteException {
		Validator.notNull(theClientDto, "theClientDto");
		boolean useRC = getParameterFac().getReversechargeVerwenden(theClientDto.getMandant());
		if (useRC) {
			return getAllReversechargeArt(mitVersteckten, theClientDto);
		} else {
			ReversechargeartDto rcOhneDto = reversechargeartFindOhne(theClientDto);
			Map<Integer, String> map = new LinkedHashMap<Integer, String>();
			map.put(rcOhneDto.getIId(), rcOhneDto.getSprDto().getcBez());
			
			if (Boolean.TRUE.equals(mitVersteckten)) {
				ReversechargeartDto rcIgDto = reversechargeartFindIg(theClientDto);
				map.put(rcIgDto.getIId(), rcIgDto.getSprDto().getcBez());
			}

			return map;
		}
	}

	public boolean isReverseCharge(Integer reversechargeartId, TheClientDto theClientDto) {
		Validator.notNull(reversechargeartId, "reversechargeartId");

		Reversechargeart entity = ReversechargeartQuery.findByCnrMandant(em, ReversechargeArt.OHNE,
				theClientDto.getMandant());
		return !reversechargeartId.equals(entity.getIId());
	}

	public Map<String, String> getAllSteuerkategorieCnr(TheClientDto theClientDto) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(STEUERKATEGORIE_INLAND, "Inland");
		map.put(STEUERKATEGORIE_AUSLANDEU_UID, "Ausland EU mit UID");
		map.put(STEUERKATEGORIE_AUSLANDEU, "Ausland EU ohne UID");
		map.put(STEUERKATEGORIE_AUSLAND, "Ausland");
		return map;
	}

	@Override
	public Integer getUstKontoFuerSteuerkategorie(String steuerkategorieCnr, Integer finanzamtId, Integer mwstsatzbezId,
			Timestamp gueltigAm, TheClientDto theClientDto) throws RemoteException {
		return getUstKontoFuerSteuerkategorie(steuerkategorieCnr,
				reversechargeartFindOhne(theClientDto.getMandant()).getIId(), finanzamtId, mwstsatzbezId, gueltigAm,
				theClientDto);
	}

	@Override
	public Integer getUstKontoFuerSteuerkategorie(String steuerkategorieCnr, Integer reversechargeartId,
			Integer finanzamtId, Integer mwstsatzbezId, Timestamp gueltigAm, TheClientDto theClientDto) {
		SteuerkategorieDto stkDto = steuerkategorieFindByCNrFinanzamtIId(steuerkategorieCnr, reversechargeartId,
				finanzamtId, theClientDto);
		HvOptional<SteuerkategoriekontoDto> stkk = steuerkategoriekontoZuDatum(stkDto.getIId(), mwstsatzbezId,
				gueltigAm);
		if (!stkk.isPresent()) {
			throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(stkDto, mwstsatzbezId);
		}
//		
//		Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class,
//				new SteuerkategoriekontoPK(stkDto.getIId(), mwstsatzbezId));
//        if(stkk == null) {
//        	throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(stkDto, mwstsatzbezId) ;
//        }

		if (stkk.get().getKontoIIdVk() == null) {
			throw EJBExcFactory.steuerkategorieKontoFehlt(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_DEFINIERT,
					reversechargeartId, stkk.get());
		}

		return stkk.get().getKontoIIdVk();
	}

	@Override
	public Integer getVstKontoFuerSteuerkategorie(String steuerkategorieCnr, Integer finanzamtId, Integer mwstsatzbezId,
			Timestamp gueltigAm, TheClientDto theClientDto) throws RemoteException {
		return getVstKontoFuerSteuerkategorie(steuerkategorieCnr,
				reversechargeartFindOhne(theClientDto.getMandant()).getIId(), finanzamtId, mwstsatzbezId, gueltigAm,
				theClientDto);
	}

	@Override
	public Integer getVstKontoFuerSteuerkategorie(String steuerkategorieCnr, Integer reversechargeartId,
			Integer finanzamtId, Integer mwstsatzbezId, Timestamp gueltigAm, TheClientDto theClientDto) {
		SteuerkategorieDto stkDto = steuerkategorieFindByCNrFinanzamtIId(steuerkategorieCnr, reversechargeartId,
				finanzamtId, theClientDto);
		HvOptional<SteuerkategoriekontoDto> stkk = steuerkategoriekontoZuDatum(stkDto.getIId(), mwstsatzbezId,
				gueltigAm);
		if (!stkk.isPresent()) {
			throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(stkDto, mwstsatzbezId);
		}
		if (stkk.get().getKontoIIdEk() == null) {
			throw EJBExcFactory.steuerkategorieKontoFehlt(EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_EK_DEFINIERT,
					reversechargeartId, stkk.get());
		}
		return stkk.get().getKontoIIdEk();
		/*
		 * Steuerkategoriekonto stkk = em.find(Steuerkategoriekonto.class, new
		 * SteuerkategoriekontoPK(stkDto.getIId(), mwstsatzbezId)); if(stkk == null) {
		 * throw EJBExcFactory.steuerkategorieKontoDefinitionFehlt(stkDto,
		 * mwstsatzbezId) ; } if(stkk.getKontoIIdEk() == null) { SteuerkategoriekontoDto
		 * stkkDto = SteuerkategoriekontoDtoAssembler.createDto(stkk) ; throw
		 * EJBExcFactory.steuerkategorieKontoFehlt(
		 * EJBExceptionLP.FEHLER_FINANZ_KEIN_ERLOESKONTO_EK_DEFINIERT,
		 * reversechargeartId, stkkDto) ; } return stkk.getKontoIIdEk();
		 */
	}

	public void vertauscheReversechargeart(Integer id1, Integer id2) {
		ReversechargeartISort isorter = new ReversechargeartISort(em);
		isorter.tausche(id1, id2);
	}

	public boolean hatRechnungIGLieferung(Integer rechnungId, TheClientDto theClientDto) throws RemoteException {
		List<Rechnungposition> lsEntries = RechnungpositionQuery.listByRechnungIdLieferscheinpositionen(em, rechnungId);
		if (lsEntries.isEmpty())
			return false;

		return isIGLieferung(lsEntries.get(0).getLieferscheinIId(), theClientDto);
	}

	public boolean isIGLieferung(Integer lieferscheinId, TheClientDto theClientDto) throws RemoteException {
		Validator.pkFieldNotNull(lieferscheinId, "lieferscheinId");

		Lieferschein ls = em.find(Lieferschein.class, lieferscheinId);
		KundeDto lieferscheinKundeDto = getKundeFac().kundeFindByPrimaryKey(ls.getKundeIIdLieferadresse(),
				theClientDto);

		String laenderartCnr = ls.getLaenderartCnr();
		if (laenderartCnr == null) {
			if (getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
				// In der Fibu bestimmt das Finanzamt das Ausgangsland
				FinanzValidator.debitorkontoDefinition(lieferscheinKundeDto);

				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(lieferscheinKundeDto.getIidDebitorenkonto());
				PartnerDto partnerDtoBasis = getPartnerFac().partnerFindByPrimaryKey(kontoDto.getFinanzamtIId(),
						theClientDto);
				laenderartCnr = getLaenderartZuPartner(
						partnerDtoBasis, lieferscheinKundeDto.getPartnerDto(),
						ls.getTBelegdatum(), theClientDto);
			} else {
				Integer partnerId = lieferscheinKundeDto.getPartnerIId();
				laenderartCnr = getLaenderartZuPartner(partnerId, ls.getTBelegdatum(), theClientDto);
			}
		}

		return FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID.equals(laenderartCnr);
	}

	@Override
	public List<UstUebersetzungDto> ustUebersetzungFindByMandant(String mandant) {
		List<UstUebersetzung> entities = UstUebersetzungQuery.listByMandant(em, mandant);

		return UstUebersetzungDtoAssembler.createDtos(entities);
	}

	/**
	 * Gewinnbuchungen f&uuml;r Periode durchf&uuml;hren
	 * 
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 * 
	 * 
	 */
	public void createGewinnbuchungen(int geschaeftsjahr, int periode, BigDecimal betrag, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		Timestamp tVonBis[] = getBuchenFac().getDatumbereichPeriodeGJ(geschaeftsjahr, periode, theClientDto);
		Date buchungsDatum = Helper.addiereTageZuDatum(new Date(tVonBis[1].getTime()), -1); // letzter Tag der Periode

		FinanzamtDto famt = null;
		MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
		famt = getFinanzFac().finanzamtFindByPrimaryKey(mandantDto.getPartnerIIdFinanzamt(), mandantDto.getCNr(),
				theClientDto);

		// Buchung storno falls existiert
		getBuchenFac().storniereGVbuchungen(geschaeftsjahr, buchungsDatum, famt.getPartnerIId(), theClientDto);

		BuchungDto bDto = new BuchungDto(FinanzFac.BUCHUNGSART_UMBUCHUNG, buchungsDatum, "Erfolgsrechnung " + periode,
				geschaeftsjahr, null, false, false, true);
		bDto.setKostenstelleIId(mandantDto.getKostenstelleIIdFibu());
		bDto.setCBelegnummer(periode + "/" + geschaeftsjahr);

		BuchungdetailDto[] buchungen = new BuchungdetailDto[2];
		buchungen[0] = new BuchungdetailDto();
		buchungen[0].setKontoIId(famt.getKontoIIdGewinnvortrag());
		buchungen[0].setKontoIIdGegenkonto(famt.getKontoIIdJahresgewinn());
		buchungen[0].setBuchungdetailartCNr(betrag.signum() < 0 ? BuchenFac.SollBuchung : BuchenFac.HabenBuchung);
		buchungen[0].setNBetrag(betrag.abs());
		buchungen[0].setNUst(BigDecimal.ZERO);

		buchungen[1] = new BuchungdetailDto();
		buchungen[1].setKontoIId(famt.getKontoIIdJahresgewinn());
		buchungen[1].setKontoIIdGegenkonto(famt.getKontoIIdGewinnvortrag());
		buchungen[1].setBuchungdetailartCNr(betrag.signum() < 0 ? BuchenFac.HabenBuchung : BuchenFac.SollBuchung);
		buchungen[1].setNBetrag(betrag.abs());
		buchungen[1].setNUst(BigDecimal.ZERO);

		ReversechargeartDto rcartOhneDto = getFinanzServiceFac().reversechargeartFindOhne(theClientDto);

		getBuchenFac().buchen(bDto, buchungen, rcartOhneDto.getIId(), true, theClientDto);
	}

	@Override
	public UvaFormularDto uvaFormularFindByPrimaryKey(UvaFormularId formularId) {
		Validator.notNull(formularId, "formularId");
		Validator.notNull(formularId.id(), "formularId.id()");
		
		UvaFormular uvaFormular = em.find(UvaFormular.class, formularId.id());
		Validator.entityFound(uvaFormular, formularId.id());
		return assembleUvaFormular(uvaFormular);
	}
	
	@Override
	public UvaFormularDto[] uvaFormularFindAllByPartnerId(
			Integer finanzamtId, String mandantCnr) {
		Validator.notNull(finanzamtId, "partnerId");
		Validator.notEmpty(mandantCnr, "mandantCnr");
		
		UvaFormularQuery q = new UvaFormularQuery(em);
		List<UvaFormular> entries = q.listFinanzamtId(finanzamtId, mandantCnr);
		return UvaFormularDtoAssembler.createDtos(entries);
	}
	
	@Override
	public HvOptional<UvaFormularDto> uvaFormularFindByFinanzamt(
			Integer finanzamtId, String mandantCnr, UvaartId uvaartId) {
		Validator.notNull(finanzamtId, "partnerId");
		Validator.notEmpty(mandantCnr, "mandantCnr");
		Validator.notNull(uvaartId, "uvaartId");

		UvaFormularQuery q = new UvaFormularQuery(em);
		HvOptional<UvaFormular> entry = q.findPartnerUvaart(finanzamtId, mandantCnr, uvaartId);
		if(!entry.isPresent()) {
			return HvOptional.empty();
		}
		
		return HvOptional.of(assembleUvaFormular(entry.get()));
	}
	
	
	private UvaFormularDto assembleUvaFormular(UvaFormular uvaFormular) {
		return UvaFormularDtoAssembler.createDto(uvaFormular);
	}
	
	private void setUvaFormularFromUvaFormularDto(
			UvaFormular uvaFormular, UvaFormularDto uvaFormularDto) {
		uvaFormular.setFinanzamtIId(uvaFormularDto.getFinanzamtId());
		uvaFormular.setMandantCNr(uvaFormularDto.getMandantCNr());
		uvaFormular.setUvaartIId(uvaFormularDto.getUvaartId());
		uvaFormular.setCKennzeichen(uvaFormularDto.getCKennzeichen());
		uvaFormular.setIGruppe(uvaFormularDto.getIGruppe());
		uvaFormular.setISort(uvaFormularDto.getISort());
		uvaFormular.setPersonalIIdAendern(uvaFormularDto.getPersonalIIdAendern());
		uvaFormular.setPersonalIIdAnlegen(uvaFormularDto.getPersonalIIdAnlegen());
		uvaFormular.setTAnlegen(uvaFormularDto.getTAnlegen());
		uvaFormular.setTAendern(uvaFormularDto.getTAendern());
		em.merge(uvaFormular);
		em.flush();
	}
	
	@Override
	public Integer createUvaFormular(UvaFormularDto uvaformularDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(uvaformularDto, "uvaformularDto");

		try {
			UvaFormular uvaformular = new UvaFormular();
			Timestamp ts = getTimestamp();
			if (uvaformularDto.getIId() == null) {
				Integer iid = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_UVAFORMULAR);
				uvaformularDto.setIId(iid);
				uvaformularDto.setTAnlegen(ts);
				uvaformularDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
				uvaformular.setIId(iid);
			}
			uvaformularDto.setTAendern(ts);
			uvaformularDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			
			setUvaFormularFromUvaFormularDto(uvaformular, uvaformularDto);
			return uvaformular.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@Override
	public void pruefeFinanzaemter(
			TheClientDto theClientDto)  throws RemoteException {
		FinanzamtDto[] faDtos = getFinanzFac()
				.finanzamtFindAllByMandantCNr(theClientDto);
		for (FinanzamtDto finanzamtDto : faDtos) {
			pruefeFinanzamt(finanzamtDto.getPartnerIId(), theClientDto);
		}
	}
	
	@Override
	public void pruefeFinanzamt(Integer finanzamtId,
			TheClientDto theClientDto) throws RemoteException {
		Validator.pkFieldNotNull(finanzamtId, "finanzamtId");
		
		FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
				finanzamtId, theClientDto.getMandant(), theClientDto);
		PartnerDto partnerDto = faDto.getPartnerDto();
		if(partnerDto == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_FINANZAMT_IM_MANDANT_NITCH_DEFINIERT,
					"" + faDto.getPartnerIId());
		} 
		
		// Der Partner muss zumindest ein Land haben
		Validator.notNull(partnerDto.getLandplzortIId(), "LandplzortIId");
		Validator.dtoNotNull(partnerDto.getLandplzortDto(), "LandplzortDto");
		
		// Staerke Pruefungen nur bei integrierter Fibu
		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			return;
		}
		
		if(!isGueltigesFinanzFormular(faDto.getIFormularnummer())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_FORMULARNUMMER,
					"" + faDto.getIFormularnummer());
		}
		
		if(!isGueltigesFinanzLand(faDto.getPartnerDto().getLandplzortDto())) {
			throw new EJBExceptionLP();
		}
		
		// Gueltiges Formular, gueltiges Land. Wenn jemand tatsaechlich
		// "AT" mit Formularnummer 2 angibt, dann weiss er was er tut.
	}
	
	private boolean isGueltigesFinanzFormular(Integer formularnummer) {
		if(formularnummer == null) return true;
		if(formularnummer > 0 && formularnummer < 4) return true;
		return false;
	}
	
	private boolean isGueltigesFinanzLand(LandplzortDto lpoDto) {
		if(lpoDto == null) return false;
		if(lpoDto.getLandDto() == null) return false;
		if(lpoDto.getLandDto().getCLkz() == null) return false;
		
		return Helper.isOneOf(
				lpoDto.getLandDto().getCLkz(), "AT", "DE", "CH", "LI");
	}
	
	private static final String[][] uvaarts = {
			new String[] {"C_NR", "I_SORT", "C_KENNZEICHEN", "B_INVERTIERT", "B_KEINE_AUSWAHL_BEI_ER", "SPR-C_BEZ"},
			new String[] {UVAART_NICHT_ZUTREFFEND,   
					"1", "---", "0", "0", "---"},
			new String[] {UVAART_INLAND_10,           
					"2", "29", "1", "1", "Umsatz Inland red.Steuer"},
			new String[] {UVAART_INLAND_20,           
					"3", "22", "1", "1", "Umsatz Inland Normalsteuer"},
			new String[] {UVAART_EU_AUSLAND_MIT_UID, 
					"4", "17", "1", "1", "EU Ausland mit UiD"},
			new String[] {UVAART_EXPORT_DRITTLAND, 
					"5", "11", "1", "1", "Export Drittland"},
			new String[] {UVAART_FA_VORSTEUERKONTO,
					"6", "60", "0", "0", "Vorsteuerkonto"},
			new String[] {UVAART_IG_ERWERB_10,
					"7", "73", "0", "0", "IG Erwerb red.Steuer"},
			new String[] {UVAART_IG_ERWERB_20,
					"8", "72", "0", "0", "IG Erwerb Normalsteuer"}, 
			new String[] {UVAART_IMPORT_DRITTLAND,
					"9", "61", "0", "0", "Import Drittland"},
			new String[] {UVAART_REVERSE_CHARGE,
					"10", "57->66", "0", "0", "Reverse Charge Leistung"},
			new String[] {UVAART_UMSATZ_REVERSE_CHARGE,
					"11", "021", "1", "1", "Umsatz Reverse Charge"},
			new String[] {UVAART_ZAHLUNG_10, 
					"12", "029a", "1", "1", "Anzahlung red.Steuer"},
			new String[] {UVAART_ZAHLUNG_20,
					"13", "022a", "1", "1", "Anzahlung Normalsteuer"},
			new String[] {UVAART_VERRECHNUNGSKONTO,
					"14", "---", "0", "1", "Verrechnungskonto"},
			new String[] {UVAART_VORSTEUER_KFZ, 
					"15", "027", "0", "0", "Vorsteuer betr KFZ"},
			new String[] {UVAART_VORSTEUER_GEBAEUDE,
					"16", "028", "0", "0", "Vorsteuer betr Geb\u00e4ude"},
			new String[] {UVAART_VORSTEUER_INVESTITIONEN,
					"17", "---", "0", "0", "Vorsteuer Investitionen"},
			new String[] {UVAART_WERBEABGABE, 
					"18", "WA", "1", "1", "Werbeabgabe"},
			new String[] {UVAART_REVERSE_CHARGE_BAULEISTUNG,
					"19", "48->82", "0", "0", "Reverse Charge Bauleistung"},
			new String[] {UVAART_REVERSE_CHARGE_SCHROTT, 
					"20", "32->89", "0", "0", "Reverse Charge Schrott"},
			new String[] {UVAART_ZAHLUNG_EU_AUSLAND_MIT_UID,
					"21", "017a", "1", "1", "Anzahlung EU Ausland mit UiD"},
			new String[] {UVAART_ZAHLUNG_DRITTLAND,
					"22", "011a", "1", "1", "Anzahlung Drittland"},
			new String[] {UVAART_INLAND_STEUERFREI,
					"23", "---", "1", "1", "Umsatz Inland steuerfrei"},
			new String[] {UVAART_IMPORT_DRITTLAND_ZAHLUNG_FA,
					"24", "083", "0", "0", "Import Drittland Zahlung an FA"},
	};
	
	@Override
	public void aktualisiereUvaart(TheClientDto theClientDto) throws RemoteException {
		for(int i = 1; i < uvaarts.length; i++) {
			UvaartDto uvaartDto = uvaartFindByCnrMandantOhneExc(
					uvaarts[i][0], theClientDto);
			if(uvaartDto != null) continue;
			
			uvaartDto = new UvaartDto();
			uvaartDto.setCNr(uvaarts[i][0]);
			uvaartDto.setISort(Integer.valueOf(uvaarts[i][1]));
			uvaartDto.setCKennzeichen(uvaarts[i][2]);
			uvaartDto.setBInvertiert(Short.valueOf(uvaarts[i][3]));
			uvaartDto.setBKeineAuswahlBeiEr(Short.valueOf(uvaarts[i][4]));
			uvaartDto.setMandantCNr(theClientDto.getMandant());
			
			UvaartsprDto sprDto = new UvaartsprDto();
			sprDto.setLocaleCNr(theClientDto.getLocMandantAsString());
			sprDto.setCBez(uvaarts[i][5]);
			uvaartDto.setUvaartsprDto(sprDto);
			
			createUvaart(uvaartDto, theClientDto);
		}
	}
	
	private static final String[][] uvaformularAT = {
			new String[] {"C_NR", "I_SORT", "I_GRUPPE", "C_KENNZEICHEN"},
			new String[] {UVAART_NICHT_ZUTREFFEND,   
					"0", "0", "---"},
			new String[] {UVAART_INLAND_10,           
					"5", "1", "29"},
			new String[] {UVAART_INLAND_20,           
					"4", "1", "22"},
			new String[] {UVAART_EU_AUSLAND_MIT_UID, 
					"3", "1", "17"},
			new String[] {UVAART_EXPORT_DRITTLAND, 
					"2", "1", "11"},
			new String[] {UVAART_FA_VORSTEUERKONTO,
					"1", "3", "60"},
			new String[] {UVAART_IG_ERWERB_10,
					"2", "2", "73"},
			new String[] {UVAART_IG_ERWERB_20,
					"1", "2", "72"}, 
			new String[] {UVAART_IMPORT_DRITTLAND,
					"4", "3", "61"},	
			new String[] {UVAART_REVERSE_CHARGE,
					"1", "4", "57 -> 66"},
			new String[] {UVAART_UMSATZ_REVERSE_CHARGE,
					"1", "1", "21"},
			new String[] {UVAART_ZAHLUNG_10, 
					"11", "1", "029a"},
			new String[] {UVAART_ZAHLUNG_20,
					"10", "1", "022a"},
			new String[] {UVAART_VERRECHNUNGSKONTO,
					"2", "5", "---"},
			new String[] {UVAART_VORSTEUER_KFZ, 
					"2", "3", "27"},
			new String[] {UVAART_VORSTEUER_GEBAEUDE,
					"3", "3", "28"},	
			new String[] {UVAART_WERBEABGABE, 
					"1", "6", "WA"},				
			new String[] {UVAART_REVERSE_CHARGE_BAULEISTUNG,
					"2", "4", "48->82"},
			new String[] {UVAART_REVERSE_CHARGE_SCHROTT, 
					"3", "4", "32->89"},
			new String[] {UVAART_ZAHLUNG_EU_AUSLAND_MIT_UID,
					"12", "1", "017a"},
			new String[] {UVAART_ZAHLUNG_DRITTLAND,
					"13", "1", "011a"},
			new String[] {UVAART_INLAND_STEUERFREI,
					"6", "1", "---"},
			new String[] {UVAART_IMPORT_DRITTLAND_ZAHLUNG_FA,
					"5", "3", "83"}
	};
	
	private static final String[][] uvaformularDE = {
			new String[] {"C_NR", "I_SORT", "I_GRUPPE", "C_KENNZEICHEN"},			
			new String[] {UVAART_NICHT_ZUTREFFEND,   
					"0", "0", "---"},
			new String[] {UVAART_INLAND_10,           
					"1", "4", "86"},
			new String[] {UVAART_INLAND_20,           
					"2", "4", "81"},
			new String[] {UVAART_EU_AUSLAND_MIT_UID, 
					"1", "3", "41"},
			new String[] {UVAART_EXPORT_DRITTLAND, 
					"2", "3", "43"},
			new String[] {UVAART_FA_VORSTEUERKONTO,
					"1", "8", "66"},
			new String[] {UVAART_IG_ERWERB_10,
					"1", "5", "93"},
			new String[] {UVAART_IG_ERWERB_20,
					"2", "5", "89"}, 
			new String[] {UVAART_IMPORT_DRITTLAND,
					"4", "8", "62"},	
			new String[] {UVAART_REVERSE_CHARGE,
					"2", "7", "57->66"},
			new String[] {UVAART_UMSATZ_REVERSE_CHARGE,
					"1", "6", "60"},
			new String[] {UVAART_ZAHLUNG_10, 
					"3", "4", "86a"},
			new String[] {UVAART_ZAHLUNG_20,
					"4", "4", "81a"},
			new String[] {UVAART_REVERSE_CHARGE_BAULEISTUNG,
					"3", "7", "48->82"},
			new String[] {UVAART_REVERSE_CHARGE_SCHROTT, 
					"4", "7", "32->89"},
			new String[] {UVAART_ZAHLUNG_EU_AUSLAND_MIT_UID,
					"3", "3", "41a"},
			new String[] {UVAART_ZAHLUNG_DRITTLAND,
					"4", "3", "43a"},
			new String[] {UVAART_INLAND_STEUERFREI,
					"10", "3", "48"},
			new String[] {UVAART_VORSTEUER_KFZ, 
					"2", "8", "27"},
			new String[] {UVAART_VORSTEUER_GEBAEUDE,
					"3", "8", "28"},
	};

	private static final String[][] uvaformularCH = {
			new String[] {"C_NR", "I_SORT", "I_GRUPPE", "C_KENNZEICHEN"},			
			new String[] {UVAART_VORSTEUER_INVESTITIONEN,
					"2", "3", "405"},
			new String[] {UVAART_EXPORT_DRITTLAND, 
					"2", "1", "221"},
			new String[] {UVAART_FA_VORSTEUERKONTO,
					"1", "3", "400"},
			new String[] {UVAART_IMPORT_DRITTLAND,
					"1", "5", "73"},
			new String[] {UVAART_INLAND_10,           
					"2", "2", "312"},
			new String[] {UVAART_INLAND_20,           
					"1", "2", "302"},
			new String[] {UVAART_UMSATZ_REVERSE_CHARGE,
					"2", "5", "29a"},
			new String[] {UVAART_ZAHLUNG_10, 
					"4", "2", "312a"},
			new String[] {UVAART_ZAHLUNG_20,
					"3", "2", "302a"},
			new String[] {UVAART_INLAND_STEUERFREI,
					"5", "1", "230"},
	};
			
	private String[][] getUvaFormularDefinition(Integer formularnummer) {
		if(formularnummer == null) return uvaformularAT;
		if(formularnummer == 1) return uvaformularDE;
		if(formularnummer == 2 || formularnummer == 3) return uvaformularCH;
		
		throw new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_FORMULARNUMMER,
				"" + formularnummer);		
	}
	
	@Override
	public void aktualisiereUvaformulare(TheClientDto theClientDto) throws RemoteException {
		FinanzamtDto[] faDtos = getFinanzFac()
				.finanzamtFindAllByMandantCNr(theClientDto);
		for (FinanzamtDto finanzamtDto : faDtos) {
			aktualisiereUvaformular(finanzamtDto.getPartnerIId(), theClientDto);
		}	
	}

	@Override
	public void aktualisiereUvaformular(Integer finanzamtId, 
			TheClientDto theClientDto) throws RemoteException {
		Validator.pkFieldNotNull(finanzamtId, "finanzamtId");

		// UVAFormular wird nur bei integrierter Fibu benoetigt
		if (!getMandantFac().hatModulFinanzbuchhaltung(theClientDto)) {
			return;
		}

		FinanzamtDto faDto = getFinanzFac().finanzamtFindByPrimaryKey(
				finanzamtId, theClientDto.getMandant(), theClientDto);
		
		if(!isGueltigesFinanzFormular(faDto.getIFormularnummer())) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FINANZ_UNGUELTIGE_FORMULARNUMMER,
					"" + faDto.getIFormularnummer());
		}
		
		String[][] formular = getUvaFormularDefinition(faDto.getIFormularnummer());
		for(int i = 1; i < formular.length; i++) {
			UvaartDto uvaartDto = uvaartFindByCnrMandantOhneExc(
					formular[i][0], theClientDto);
			if(uvaartDto == null) continue;
			
			HvOptional<UvaFormularDto> fDto = uvaFormularFindByFinanzamt(finanzamtId, 
					theClientDto.getMandant(), new UvaartId(uvaartDto.getIId()));
			if(fDto.isPresent()) continue;
			
			UvaFormularDto formularDto = new UvaFormularDto();
			formularDto.setFinanzamtId(finanzamtId);
			formularDto.setMandantCNr(theClientDto.getMandant());
			formularDto.setUvaartId(uvaartDto.getIId());
			formularDto.setISort(Integer.valueOf(formular[i][1]));
			formularDto.setIGruppe(Integer.valueOf(formular[i][2]));
			formularDto.setCKennzeichen(formular[i][3]);
			createUvaFormular(formularDto, theClientDto);
		}
	}
	
	@Override
	public void pflegeUvaFormulare(TheClientDto theClientDto) throws RemoteException {
		pruefeFinanzaemter(theClientDto);
		aktualisiereUvaart(theClientDto);
		aktualisiereUvaformulare(theClientDto);
	}
	
	@Override
	public ReversechargeartDto reversechargeartFindIg(String mandantCNr) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.IG, mandantCNr);
	}

	@Override
	public ReversechargeartDto reversechargeartFindIg(TheClientDto theClientDto) throws RemoteException {
		return reversechargeartFindByCnrMandant(ReversechargeArt.IG, theClientDto);
	}
}
