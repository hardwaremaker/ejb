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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.anfrage.service.AnfrageServiceFac;
import com.lp.server.anfrage.service.AnfragepositionDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebotstkl.fastlanereader.generated.FLRPositionlieferant;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikellieferant;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.bestellung.ejb.Bestellvorschlag;
import com.lp.server.bestellung.ejb.BestellvorschlagQuery;
import com.lp.server.bestellung.fastlanereader.BestellvorschlagHandler;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellposition;
import com.lp.server.bestellung.fastlanereader.generated.FLRBestellvorschlag;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.BestellvorschlagDto;
import com.lp.server.bestellung.service.BestellvorschlagDtoAssembler;
import com.lp.server.bestellung.service.BestellvorschlagFac;
import com.lp.server.bestellung.service.CreateBestellvorschlagDto;
import com.lp.server.bestellung.service.RueckgabeUeberleitungDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.MaterialbedarfDto;
import com.lp.server.partner.fastlanereader.LieferantHandler;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.ejb.Maschine;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.stueckliste.ejb.Stueckliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.ApkommentarDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.ejb.Mandant;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import jxl.Cell;
import jxl.CellType;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Stateless
public class BestellvorschlagFacBean extends Facade implements BestellvorschlagFac {

	public static String XLS_BESTELLVORSCHLAGIMPORT_SPALTE_ARTIKELNUMMER = "Artikelnummer";
	public static String XLS_BESTELLVORSCHLAGIMPORT_SPALTE_MENGE = "Menge";

	private String fehlerZeileXLSImport = "";

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public Integer createBestellvorschlag(BestellvorschlagDto bestellvorschlagDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		// Preconditions.
		if (bestellvorschlagDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("bestellvorschlagDtoI == null"));
		}

		if (bestellvorschlagDtoI.getCMandantCNr() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("bestellvorschlagDtoI.getCMandantCNr() == null"));
		}

		if (bestellvorschlagDtoI.getIArtikelId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("bestellvorschlagDtoI.getIArtikelId()== null"));
		}

		if (bestellvorschlagDtoI.getBNettopreisuebersteuert() == null) {
			bestellvorschlagDtoI.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		}

		if (bestellvorschlagDtoI.getBVormerkung() == null) {
			bestellvorschlagDtoI.setBVormerkung(Helper.boolean2Short(false));
		}

		// PJ18940
		if (Helper.short2boolean(bestellvorschlagDtoI.getBVormerkung()) == true) {
			bestellvorschlagDtoI.setPersonalIIdVormerkung(theClientDto.getIDPersonal());
			bestellvorschlagDtoI.setTVormerkung(new java.sql.Timestamp(System.currentTimeMillis()));
		}

		// PK fuer Partner generieren.
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdBestellvorschlag = pkGen.getNextPrimaryKey(PKConst.PK_BESTELLVORSCHLAG);
		bestellvorschlagDtoI.setIId(iIdBestellvorschlag);

		try {
			Bestellvorschlag bestellvorschlag = new Bestellvorschlag(bestellvorschlagDtoI.getIId(),
					bestellvorschlagDtoI.getDRabattsatz(), bestellvorschlagDtoI.getNNettoGesamtPreisMinusRabatte(),
					bestellvorschlagDtoI.getNNettogesamtpreis(), bestellvorschlagDtoI.getNRabattbetrag(),
					bestellvorschlagDtoI.getNNettoeinzelpreis(), bestellvorschlagDtoI.getILieferantId(),
					bestellvorschlagDtoI.getIBelegartId(), bestellvorschlagDtoI.getCBelegartCNr(),
					bestellvorschlagDtoI.getTLiefertermin(), bestellvorschlagDtoI.getNZubestellendeMenge(),
					bestellvorschlagDtoI.getIArtikelId(), bestellvorschlagDtoI.getCMandantCNr(),
					bestellvorschlagDtoI.getBNettopreisuebersteuert());
			em.persist(bestellvorschlag);
			em.flush();
			setBestellvorschlagFromBestellvorschlagDto(bestellvorschlag, bestellvorschlagDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return bestellvorschlagDtoI.getIId();
	}

	public void removeBestellvorschlag(Integer iId) throws EJBExceptionLP {
		// try {
		Bestellvorschlag toRemove = em.find(Bestellvorschlag.class, iId);
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

	/**
	 * 
	 * @param bestellvorschlagDto BestellvorschlagDto
	 * @throws EJBExceptionLP
	 */
	public void removeBestellvorschlag(BestellvorschlagDto bestellvorschlagDto) throws EJBExceptionLP {
		if (bestellvorschlagDto != null) {
			Integer iId = bestellvorschlagDto.getIId();
			removeBestellvorschlag(iId);
		}
	}

	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (bestellvorschlagDto != null) {
			Integer iId = bestellvorschlagDto.getIId();
			// try {
			Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class, iId);

			// PJ18940
			if (Helper.short2boolean(bestellvorschlag.getBVormerkung()) == false
					|| Helper.short2boolean(bestellvorschlagDto.getBVormerkung()) == true) {
				bestellvorschlagDto.setPersonalIIdVormerkung(theClientDto.getIDPersonal());
				bestellvorschlagDto.setTVormerkung(new java.sql.Timestamp(System.currentTimeMillis()));
			} else {

				if (Helper.short2boolean(bestellvorschlag.getBVormerkung()) == true
						|| Helper.short2boolean(bestellvorschlagDto.getBVormerkung()) == false) {
					bestellvorschlagDto.setPersonalIIdVormerkung(null);
					bestellvorschlagDto.setTVormerkung(null);
				}

			}

			if (bestellvorschlag == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellvorschlagFromBestellvorschlagDto(bestellvorschlag, bestellvorschlagDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateBestellvorschlags(BestellvorschlagDto[] bestellvorschlagDtos, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (bestellvorschlagDtos != null) {
			for (int i = 0; i < bestellvorschlagDtos.length; i++) {
				updateBestellvorschlag(bestellvorschlagDtos[i], theClientDto);
			}
		}
	}

	public BestellvorschlagDto bestellvorschlagFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class, iId);
		if (bestellvorschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBestellvorschlagDto(bestellvorschlag);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public boolean mindestbestellwertErreicht(Integer iLieferantId, TheClientDto theClientDto) {

		boolean bMindestbestellwertErreicht = false;
		LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(iLieferantId, theClientDto);

		if (lieferantDto.getNMindestbestellwert() != null && lieferantDto.getNMindestbestellwert().doubleValue() > 0) {

			BestellvorschlagDto[] bestDtos = bestellvorschlagFindByLieferantIIdMandantCNr(iLieferantId,
					theClientDto.getMandant());

			BigDecimal gesamt = new BigDecimal(0);

			for (int i = 0; i < bestDtos.length; i++) {

				if (bestDtos[i].getNNettogesamtpreis() != null) {
					gesamt = gesamt
							.add(bestDtos[i].getNZubestellendeMenge().multiply(bestDtos[i].getNNettogesamtpreis()));
				}

			}

			if (gesamt.doubleValue() >= lieferantDto.getNMindestbestellwert().doubleValue()) {
				bMindestbestellwertErreicht = true;
			}

		} else {
			bMindestbestellwertErreicht = true;
		}
		return bMindestbestellwertErreicht;
	}

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNr(Integer iLieferantId, String cNrMandant)
			throws EJBExceptionLP {
		Query query = em.createNamedQuery("BestellvorschlagfindByLieferantIIdMandantCNr");
		query.setParameter(1, iLieferantId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleBestellvorschlagDtos(cl);
	}

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNrOhneExc(Integer iLieferantId,
			String cNrMandant) {
		try {
			Query query = em.createNamedQuery("BestellvorschlagfindByLieferantIIdMandantCNr");
			query.setParameter(1, iLieferantId);
			query.setParameter(2, cNrMandant);
			return assembleBestellvorschlagDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
	}

	private void setBestellvorschlagFromBestellvorschlagDto(Bestellvorschlag bestellvorschlag,
			BestellvorschlagDto bestellvorschlagDto) {
		bestellvorschlag.setMandantCNr(bestellvorschlagDto.getCMandantCNr());
		bestellvorschlag.setArtikelIId(bestellvorschlagDto.getIArtikelId());
		bestellvorschlag.setNZubestellendemenge(bestellvorschlagDto.getNZubestellendeMenge());
		bestellvorschlag.setTLiefertermin(bestellvorschlagDto.getTLiefertermin());
		bestellvorschlag.setBelegartCNr(bestellvorschlagDto.getCBelegartCNr());
		bestellvorschlag.setIBelegartid(bestellvorschlagDto.getIBelegartId());
		bestellvorschlag.setIBelegartpositionid(bestellvorschlagDto.getIBelegartpositionid());
		bestellvorschlag.setLieferantIId(bestellvorschlagDto.getILieferantId());
		bestellvorschlag.setNNettoeinzelpreis(bestellvorschlagDto.getNNettoeinzelpreis());
		bestellvorschlag.setNRabattbetrag(bestellvorschlagDto.getNRabattbetrag());
		bestellvorschlag.setFRabattsatz(bestellvorschlagDto.getDRabattsatz());
		bestellvorschlag.setNNettogesamtpreis(bestellvorschlagDto.getNNettogesamtpreis());
		bestellvorschlag.setBNettopreisuebersteuert(bestellvorschlagDto.getBNettopreisuebersteuert());
		bestellvorschlag.setProjektIId(bestellvorschlagDto.getProjektIId());
		bestellvorschlag.setXTextinhalt(bestellvorschlagDto.getXTextinhalt());
		bestellvorschlag.setBVormerkung(bestellvorschlagDto.getBVormerkung());
		bestellvorschlag.setTVormerkung(bestellvorschlagDto.getTVormerkung());
		bestellvorschlag.setPersonalIIdVormerkung(bestellvorschlagDto.getPersonalIIdVormerkung());
		bestellvorschlag.setPartnerIIdStandort(bestellvorschlagDto.getPartnerIIdStandort());
		bestellvorschlag.setGebindeIId(bestellvorschlagDto.getGebindeIId());
		bestellvorschlag.setNAnzahlgebinde(bestellvorschlagDto.getNAnzahlgebinde());
		bestellvorschlag.setPersonalIId(bestellvorschlagDto.getPersonalIId());
		bestellvorschlag.setIWiederbeschaffungszeit(bestellvorschlagDto.getIWiederbeschaffungszeit());
		bestellvorschlag.setTBearbeitet(bestellvorschlagDto.getTBearbeitet());
		bestellvorschlag.setPersonalIIdBearbeitet(bestellvorschlagDto.getPersonalIIdBearbeitet());
		bestellvorschlag.setFLagermindest(bestellvorschlagDto.getFLagermindest());
		em.merge(bestellvorschlag);
		em.flush();
	}

	public long getAnzahlBestellvorschlagDesMandanten(TheClientDto theClientDto) throws EJBExceptionLP {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(distinct flrbestellvorschlag.i_id) "
					+ " from FLRBestellvorschlag flrbestellvorschlag " + " where flrbestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'";
			org.hibernate.Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} finally {
			closeSession(session);
		}
		return rowCount;
	}

	private BestellvorschlagDto assembleBestellvorschlagDto(Bestellvorschlag bestellvorschlag) {
		return BestellvorschlagDtoAssembler.createDto(bestellvorschlag);
	}

	private BestellvorschlagDto[] assembleBestellvorschlagDtos(Collection<?> bestellvorschlaege) {
		List<BestellvorschlagDto> list = new ArrayList<BestellvorschlagDto>();
		if (bestellvorschlaege != null) {
			Iterator<?> iterator = bestellvorschlaege.iterator();
			while (iterator.hasNext()) {
				Bestellvorschlag bestellvorschlag = (Bestellvorschlag) iterator.next();
				list.add(assembleBestellvorschlagDto(bestellvorschlag));
			}
		}
		BestellvorschlagDto[] returnArray = new BestellvorschlagDto[list.size()];
		return (BestellvorschlagDto[]) list.toArray(returnArray);
	}

	public void erstelleBestellvorschlagAnhandEinesAngebots(Integer angebotIId, java.sql.Date dLiefertermin,
			TheClientDto theClientDto) {

		long lStart = System.currentTimeMillis();
		loescheBestellvorlaegeEinesMandaten(true, theClientDto);

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);
		try {
			AngebotpositionDto[] agposDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(angebotIId, theClientDto);

			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotIId, theClientDto);

			// Einstellungen speichern
			ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();
			alDtos.add(new KeyvalueDto("Typ", "AnfragevorschlagAnhandAngebot"));
			alDtos.add(new KeyvalueDto("Angebot", angebotDto.getCNr()));

			alDtos.add(new KeyvalueDto("Liefertermin", Helper.formatDatum(dLiefertermin, theClientDto.getLocUi())));

			for (int i = 0; i < agposDtos.length; i++) {

				if (agposDtos[i].getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(agposDtos[i].getArtikelIId(),
							theClientDto);

					StuecklisteDto stklDto = getStuecklisteFac().stuecklisteFindByArtikelIIdMandantCNrOhneExc(
							agposDtos[i].getArtikelIId(), theClientDto.getMandant());

					if (stklDto != null) {

						List<?> m = null;
						try {
							m = getStuecklisteFac().getStrukturDatenEinerStueckliste(stklDto.getIId(), theClientDto,
									StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, true, true,
									agposDtos[i].getNMenge(), null, false);
						} catch (RemoteException ex4) {
							throwEJBExceptionLPRespectOld(ex4);
						}

						Iterator<?> it = m.listIterator();

						while (it.hasNext()) {
							StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();
							StuecklistepositionDto position = struktur.getStuecklistepositionDto();

							bestellvorschlagDtoErzeugen(LocaleFac.BELEGART_ANGEBOT, theClientDto.getMandant(),
									position.getArtikelIId(), angebotDto.getIId(), null,
									new java.sql.Timestamp(dLiefertermin.getTime()),
									Helper.rundeKaufmaennisch(position.getNZielmenge(agposDtos[i].getNMenge()), 4),
									angebotDto.getProjektIId(), null, null, null, null, null, bZentralerArtikelstamm,
									theClientDto);

						}

					} else {
						if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet()) == true) {
							bestellvorschlagDtoErzeugen(LocaleFac.BELEGART_ANGEBOT, theClientDto.getMandant(),
									artikelDto.getIId(), angebotDto.getIId(), agposDtos[i].getIId(),
									new java.sql.Timestamp(dLiefertermin.getTime()), agposDtos[i].getNMenge(),
									angebotDto.getProjektIId(), agposDtos[i].getXTextinhalt(),
									agposDtos[i].getLieferantIId(), agposDtos[i].getNEinkaufpreis(), null, null,
									bZentralerArtikelstamm, theClientDto);
						}
					}

				}

			}
			alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(theClientDto), alDtos);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(java.sql.Date dLiefertermin,
			boolean vormerklisteLoeschen, TheClientDto theClientDto, Integer partnerIIdStandort,
			boolean bBestellvorschlagLoechen) {
		long lStart = System.currentTimeMillis();
		if (bBestellvorschlagLoechen) {
			loescheBestellvorlaegeEinesMandaten(vormerklisteLoeschen, theClientDto);
		}

		// Einstellungen speichern
		ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();
		alDtos.add(new KeyvalueDto("Typ", "BestellvorschlagAnhandStuecklistenmindestlagerstand"));
		alDtos.add(new KeyvalueDto("VormerklisteLoeschen", Helper.boolean2Short(vormerklisteLoeschen) + ""));

		alDtos.add(new KeyvalueDto("Liefertermin", Helper.formatDatum(dLiefertermin, theClientDto.getLocUi())));

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		HashMap<Integer, BigDecimal> hmArtikel = new HashMap<Integer, BigDecimal>();
		session = factory.openSession();
		String queryString = null;
		if (lagerminJeLager) {
			queryString = "SELECT stkl,(SELECT count(*) FROM FLRStuecklisteposition AS pos WHERE pos.flrartikel.i_id=stkl.flrartikel.i_id ) as anzstklpos, (SELECT sum(al.f_lagermindest) FROM  FLRArtikellager AS al WHERE al.flrlager.b_bestellvorschlag=1 AND al.flrlager.parnter_i_id_standort="
					+ partnerIIdStandort
					+ ") as lagermindeststand FROM FLRStueckliste AS stkl WHERE stkl.stuecklisteart_c_nr<>'"
					+ StuecklisteFac.STUECKLISTEART_SETARTIKEL + "' AND stkl.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND (SELECT sum(al.f_lagermindest) FROM  FLRArtikellager AS al WHERE al.flrartikel.i_id=stkl.flrartikel.i_id AND al.flrlager.b_bestellvorschlag=1 AND al.flrlager.parnter_i_id_standort="
					+ partnerIIdStandort + ") > 0";
		} else {
			queryString = "SELECT stkl,(SELECT count(*) FROM FLRStuecklisteposition AS pos WHERE pos.flrartikel.i_id=stkl.flrartikel.i_id ) as anzstklpos FROM FLRStueckliste AS stkl WHERE stkl.stuecklisteart_c_nr<>'"
					+ StuecklisteFac.STUECKLISTEART_SETARTIKEL + "' AND stkl.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND stkl.flrartikel.f_lagermindest > 0";
		}

		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRStueckliste flrStueckliste = (FLRStueckliste) o[0];

			long iAzahlinVerwendung = 0;
			if (o[1] != null) {
				iAzahlinVerwendung = (Long) o[1];
			}

			BigDecimal bdLagermindeststand =BigDecimal.ZERO;
			
			if(flrStueckliste.getFlrartikel().getF_lagermindest()!=null) {
				bdLagermindeststand = new BigDecimal(flrStueckliste.getFlrartikel().getF_lagermindest());
			}
			
			if (lagerminJeLager) {
				bdLagermindeststand = new BigDecimal((Double) o[2]);
			}

			if (iAzahlinVerwendung == 0) {
				// Dann ists eine Wurzelstueckliste

				// Diese nun aufloesen

				List<?> m = null;
				try {

					m = getStuecklisteFac().getStrukturDatenEinerStuecklisteMitArbeitsplan(flrStueckliste.getI_id(),
							theClientDto, StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE, 0, null, true,
							true, bdLagermindeststand, null, false, false, false, null);

				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				Iterator<?> it = m.listIterator();

				while (it.hasNext()) {
					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it.next();

					if (struktur.isBArbeitszeit() == false) {
						StuecklistepositionDto position = struktur.getStuecklistepositionDto();

						BigDecimal nBedarf = position.getNZielmenge(bdLagermindeststand);
						if (hmArtikel.containsKey(position.getArtikelIId())) {

							nBedarf = nBedarf.add(hmArtikel.get(position.getArtikelIId()));

						}

						// SP2483 Zusaetzlich noch pro Verwendung des Artikels
						// dessen Lagermindeststand aus der Kopfstueckliste
						// addieren
						if (position.getfLagermindeststandAusKopfartikel() != null) {
							nBedarf = nBedarf.add(new BigDecimal(position.getfLagermindeststandAusKopfartikel()));
						}

						hmArtikel.put(position.getArtikelIId(), nBedarf);
					}
				}
			}
		}
		closeSession(session);

		// Nun alle Artikel, welche noch nicht in der Liste sind, und einen
		// Lagermindeststand haben, hinzufuegen
		session = factory.openSession();

		if (lagerminJeLager) {
			queryString = "SELECT a, (SELECT stkl FROM FLRStueckliste AS stkl WHERE stkl.mandant_c_nr=a.mandant_c_nr AND stkl.artikel_i_id=a.i_id),  (SELECT sum(al.f_lagermindest) FROM  FLRArtikellager AS al WHERE al.flrartikel.i_id=a.i_id AND al.flrlager.b_bestellvorschlag=1 AND al.flrlager.parnter_i_id_standort="
					+ partnerIIdStandort + ") FROM FLRArtikel AS a WHERE a.mandant_c_nr='" + theClientDto.getMandant()
					+ "' AND a.b_lagerbewirtschaftet=1 AND (SELECT sum(al.f_lagermindest) FROM  FLRArtikellager AS al WHERE al.flrartikel.i_id=a.i_id AND al.flrlager.b_bestellvorschlag=1 AND al.flrlager.parnter_i_id_standort="
					+ partnerIIdStandort + ") > 0";
		} else {
			queryString = "SELECT a, (SELECT stkl FROM FLRStueckliste AS stkl WHERE stkl.mandant_c_nr=a.mandant_c_nr AND stkl.artikel_i_id=a.i_id) FROM FLRArtikel AS a WHERE a.mandant_c_nr='"
					+ theClientDto.getMandant() + "' AND a.f_lagermindest > 0 AND a.b_lagerbewirtschaftet=1";
		}

		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRArtikel a = (FLRArtikel) o[0];
			FLRStueckliste s = (FLRStueckliste) o[1];

			BigDecimal bdLagermindeststand =BigDecimal.ZERO;
			
			if(a.getF_lagermindest()!=null) {
				bdLagermindeststand = new BigDecimal(a.getF_lagermindest());
			}
			
			if (lagerminJeLager) {
				bdLagermindeststand = new BigDecimal((Double) o[2]);
			}

			if (!hmArtikel.containsKey(a.getI_id())) {
				if (s == null || Helper.short2boolean(s.getB_fremdfertigung()) == true) {

					hmArtikel.put(a.getI_id(), bdLagermindeststand);
				}
			}

		}
		closeSession(session);

		try {
			LagerDto[] lagerDto = getLagerFac().lagerFindByMandantCNr(theClientDto.getMandant());

			Iterator<Integer> it = hmArtikel.keySet().iterator();
			while (it.hasNext()) {
				Integer artikelIId = it.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(artikelIId, theClientDto);

				if (Helper.short2boolean(aDto.getBLagerbewirtschaftet())) {

					BigDecimal nBedarf = hmArtikel.get(artikelIId);

					// Nun bei allen Artikel die
					// Lagerstaende/In-Fertigung/Bestellt
					// abziehen
					BigDecimal bdBestellt = getArtikelbestelltFac().getAnzahlBestellt(artikelIId);

					BigDecimal bdInFertigung = getFertigungFac().getAnzahlInFertigung(artikelIId, theClientDto);

					BigDecimal bdLagerstand = new BigDecimal(0);

					for (int j = 0; j < lagerDto.length; j++) {

						if (Helper.short2boolean(lagerDto[j].getBBestellvorschlag())) {
							bdLagerstand = bdLagerstand
									.add(getLagerFac().getLagerstand(artikelIId, lagerDto[j].getIId(), theClientDto));
						}

					}

					// Wenn groesser 0, dann bestellen

					BigDecimal diff = nBedarf.subtract(bdBestellt).subtract(bdInFertigung).subtract(bdLagerstand);

					if (diff.doubleValue() > 0) {

						bestellvorschlagDtoErzeugen(null, theClientDto.getMandant(), artikelIId, null, null,
								new java.sql.Timestamp(dLiefertermin.getTime()), diff, null, null, null, null, null, 
								partnerIIdStandort, bZentralerArtikelstamm, theClientDto);
					}
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
		getSystemServicesFac()
				.replaceKeyvaluesEinerGruppe(getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(theClientDto), alDtos);

	}

	public void erstelleBestellvorschlagAnhandEkag(Integer einkaufsangebotIId, int menge,
			Timestamp tGeplanterFertigungstermin, Integer vorlaufzeit, TheClientDto theClientDto) {
		long lStart = System.currentTimeMillis();
		loescheBestellvorlaegeEinesMandaten(true, theClientDto);

		try {
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			java.sql.Date dLiefertermin = new java.sql.Date(getAusliefervorschlagFac().umKundenlieferdauerVersetzen(
					theClientDto, new java.sql.Date(tGeplanterFertigungstermin.getTime()), vorlaufzeit).getTime());

			// Einstellungen speichern
			ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();
			alDtos.add(new KeyvalueDto("Typ", "BestellvorschlagAnhandEkag"));

			alDtos.add(new KeyvalueDto("Vorlaufzeit", vorlaufzeit + ""));

			alDtos.add(new KeyvalueDto("geplanterFertigungstermin", tGeplanterFertigungstermin + ""));

			boolean bZentralerArtikelstamm = getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			session = factory.openSession();
			String queryString = "SELECT pl FROM FLRPositionlieferant AS pl WHERE pl.flreinkaufsangebotposition.flreinkaufsangebot.i_id="
					+ einkaufsangebotIId
					+ " AND (b_menge1_bestellen=1 OR b_menge2_bestellen=1 OR b_menge3_bestellen=1 OR b_menge4_bestellen=1 OR b_menge5_bestellen=1)";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRPositionlieferant flrPositionslieferant = (FLRPositionlieferant) resultListIterator.next();

				if (flrPositionslieferant.getFlreinkaufsangebotposition().getFlrartikel() != null) {

					BigDecimal bdMenge = null;

					Integer lieferantIId = flrPositionslieferant.getFlrekaglieferant().getLieferant_i_id();
					BigDecimal bdPreis = null;

					if (menge == AngebotstklFac.MENGE_1
							&& Helper.short2boolean(flrPositionslieferant.getB_menge1_bestellen())) {
						bdPreis = flrPositionslieferant.getN_preis_menge1();

						bdMenge = flrPositionslieferant.getFlreinkaufsangebotposition().getN_menge()
								.multiply(flrPositionslieferant.getFlreinkaufsangebotposition().getFlreinkaufsangebot()
										.getN_menge1());
					} else if (menge == AngebotstklFac.MENGE_2
							&& Helper.short2boolean(flrPositionslieferant.getB_menge2_bestellen())) {
						bdPreis = flrPositionslieferant.getN_preis_menge2();

						bdMenge = flrPositionslieferant.getFlreinkaufsangebotposition().getN_menge()
								.multiply(flrPositionslieferant.getFlreinkaufsangebotposition().getFlreinkaufsangebot()
										.getN_menge2());
					} else if (menge == AngebotstklFac.MENGE_3
							&& Helper.short2boolean(flrPositionslieferant.getB_menge3_bestellen())) {
						bdPreis = flrPositionslieferant.getN_preis_menge3();

						bdMenge = flrPositionslieferant.getFlreinkaufsangebotposition().getN_menge()
								.multiply(flrPositionslieferant.getFlreinkaufsangebotposition().getFlreinkaufsangebot()
										.getN_menge3());
					} else if (menge == AngebotstklFac.MENGE_4
							&& Helper.short2boolean(flrPositionslieferant.getB_menge4_bestellen())) {
						bdPreis = flrPositionslieferant.getN_preis_menge4();

						bdMenge = flrPositionslieferant.getFlreinkaufsangebotposition().getN_menge()
								.multiply(flrPositionslieferant.getFlreinkaufsangebotposition().getFlreinkaufsangebot()
										.getN_menge4());
					} else if (menge == AngebotstklFac.MENGE_5
							&& Helper.short2boolean(flrPositionslieferant.getB_menge5_bestellen())) {
						bdPreis = flrPositionslieferant.getN_preis_menge5();

						bdMenge = flrPositionslieferant.getFlreinkaufsangebotposition().getN_menge()
								.multiply(flrPositionslieferant.getFlreinkaufsangebotposition().getFlreinkaufsangebot()
										.getN_menge5());
					}

					// Waehrung umrechnen

					if (bdMenge != null) {
						if (bdPreis != null) {
							bdPreis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(bdPreis,
									flrPositionslieferant.getFlrekaglieferant().getWaehrung_c_nr(),
									mandantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto);
						}
						bestellvorschlagDtoErzeugen(LocaleFac.BELEGART_EINKAUFSANGEBOT, theClientDto.getMandant(),
								flrPositionslieferant.getFlreinkaufsangebotposition().getFlrartikel().getI_id(),
								einkaufsangebotIId, null, new java.sql.Timestamp(dLiefertermin.getTime()), bdMenge,
								null, null, lieferantIId, bdPreis, null, null, bZentralerArtikelstamm, theClientDto);
					}
				}
			}
			alDtos.add(new KeyvalueDto("DauerInSekunden", new Long((System.currentTimeMillis() - lStart) / 1000) + ""));
			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(theClientDto), alDtos);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	/**
	 * METHODE VON CK
	 * 
	 * @param iVorlaufzeit                      Integer
	 * @param dateFuerEintraegeOhneLiefertermin Date
	 * @param theClientDto                      String
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@org.jboss.ejb3.annotation.TransactionTimeout(3000)
	public void erstelleBestellvorschlag(Integer iVorlaufzeit, Integer iToleranz,
			java.sql.Date dateFuerEintraegeOhneLiefertermin, ArrayList<Integer> arLosIId,
			ArrayList<Integer> arAuftragIId, boolean bMitNichtlagerbewirtschafteten,
			boolean bNurLospositionenBeruecksichtigen, boolean vormerklisteLoeschen, boolean bBestellvorschlagLoechen,
			boolean bNichtFreigegebeneAuftraegeBeruecksichtigen, TheClientDto theClientDto, Integer partnerIIdStandort,
			boolean bArtikelNurAufAuftraegeIgnorieren, boolean bExakterAuftragsbezug) {

		long lStart = System.currentTimeMillis();

		// alten bestellvorschlag loeschen
		if (bBestellvorschlagLoechen == true) {
			loescheBestellvorlaegeEinesMandaten(vormerklisteLoeschen, theClientDto);
		}

		// Einstellungen speichern
		ArrayList<KeyvalueDto> alDtos = new ArrayList<KeyvalueDto>();

		alDtos.add(new KeyvalueDto("Typ", "Bestellvorschlag"));
		alDtos.add(new KeyvalueDto("ErzeugtAm",
				Helper.formatTimestamp(new java.sql.Timestamp(System.currentTimeMillis()), theClientDto.getLocUi())));
		alDtos.add(new KeyvalueDto("ErzeugtVon", getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).getCKurzzeichen()));

		alDtos.add(new KeyvalueDto("Vorlaufzeit", iVorlaufzeit + ""));
		alDtos.add(new KeyvalueDto("Toleranz", iToleranz + ""));
		alDtos.add(new KeyvalueDto("BestellvorschlagLoeschen", Helper.boolean2Short(bBestellvorschlagLoechen) + ""));

		alDtos.add(new KeyvalueDto("VormerklisteLoeschen", Helper.boolean2Short(vormerklisteLoeschen) + ""));

		alDtos.add(new KeyvalueDto("LieferterminFuerArtikelOhneReservierung",
				Helper.formatDatum(dateFuerEintraegeOhneLiefertermin, theClientDto.getLocUi())));

		alDtos.add(new KeyvalueDto("MitNichlagerbewirtschaftetenArtikeln",
				Helper.boolean2Short(bMitNichtlagerbewirtschafteten) + ""));
		alDtos.add(new KeyvalueDto("NichtFreigegebeneAuftraegeBeruecksichtigen",
				Helper.boolean2Short(bNichtFreigegebeneAuftraegeBeruecksichtigen) + ""));

		alDtos.add(new KeyvalueDto("NurBetroffeneLospositionenBeruecksichtigen",
				Helper.boolean2Short(bNurLospositionenBeruecksichtigen) + ""));
		alDtos.add(new KeyvalueDto("ArtikelNurAufAuftraegeIgnorieren",
				Helper.boolean2Short(bArtikelNurAufAuftraegeIgnorieren) + ""));
		alDtos.add(new KeyvalueDto("exakterAuftragsbezug", Helper.boolean2Short(bExakterAuftragsbezug) + ""));

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		Session session = null;
		try {

			ArrayList<Integer> arArtikelIIds = new ArrayList<Integer>();

			// Wenn auftragsIId angebeben werden, dann deren Los suchen
			String auftraege = "";
			if (arAuftragIId != null) {

				for (int i = 0; i < arAuftragIId.size(); i++) {
					auftraege += getAuftragFac().auftragFindByPrimaryKey(arAuftragIId.get(i)).getCNr() + ",";
					LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(arAuftragIId.get(i));

					for (int j = 0; j < losDtos.length; j++) {
						if (arLosIId == null) {
							arLosIId = new ArrayList<Integer>();
						}

						arLosIId.add(losDtos[j].getIId());

					}

					// und die Auftragspositionen hinzufuegen

					AuftragpositionDto[] aufposDtos = getAuftragpositionFac()
							.auftragpositionFindByAuftrag(arAuftragIId.get(i));
					for (int j = 0; j < aufposDtos.length; j++) {
						AuftragpositionDto apDto = aufposDtos[j];

						if (apDto.isIdent() && apDto.getArtikelIId() != null) {
							arArtikelIIds.add(apDto.getArtikelIId());
						}

					}

				}

			}
			alDtos.add(new KeyvalueDto("Auftraege", auftraege));
			// Wenn losIIds vorhanden dann nur die Artikel dieser Lose
			String lose = "";
			if (arLosIId != null) {
				if (arLosIId.size() > 0) {
					Object[] oLosIId = new Object[arLosIId.size()];
					oLosIId = arLosIId.toArray();
					session = FLRSessionFactory.getFactory().openSession();
					org.hibernate.Criteria critLose = session.createCriteria(FLRLos.class);
					critLose.add(Restrictions.in(FertigungFac.FLR_LOS_I_ID, oLosIId));
					List<?> resultListLose = critLose.list();
					Iterator<?> resultListLoseIterator = resultListLose.iterator();
					while (resultListLoseIterator.hasNext()) {
						FLRLos los = (FLRLos) resultListLoseIterator.next();

						lose += los.getC_nr() + ",";
						LossollmaterialDto[] material = getFertigungFac().lossollmaterialFindByLosIId(los.getI_id());
						for (int i = 0; i < material.length; i++) {
							if (material[i].getArtikelIId() != null) {
								arArtikelIIds.add(material[i].getArtikelIId());
							}
						}
					}
				}
				closeSession(session);
			}

			alDtos.add(new KeyvalueDto("Lose", lose));

			// Alle Lagerbewirtschafteten Artikel holen
			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria crit = session.createCriteria(FLRArtikel.class);
			// Filter nach Mandant

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {

				crit.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_MANDANT_C_NR, theClientDto.getMandant()));
			}
			// keine Hand- und AZ-Artikel
			crit.add(Restrictions.not(Restrictions.in(ArtikelFac.FLR_ARTIKELLISTE_ARTIKELART_C_NR,
					new String[] { ArtikelFac.ARTIKELART_HANDARTIKEL, ArtikelFac.ARTIKELART_ARBEITSZEIT })));
			if (bMitNichtlagerbewirtschafteten == false) {
				// nur lagerbewirtschaftete
				crit.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET, Helper.boolean2Short(true)));
			}

			// crit.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_C_NR, "40260"));

			// Zusaetlich nach Losen wenn Materialien vorhanden
			if (arArtikelIIds.size() > 0) {

				Object[] oArtikelIIds = arArtikelIIds.toArray();
				crit.add(Restrictions.in(ArtikelFac.FLR_ARTIKEL_I_ID, oArtikelIIds));
			} else {
				// Wenn ein Auftrag ausgewaehlt wurde, jedoch keine Artikel
				// vorhanden sind, dann gibts keine Positionen
				if (arAuftragIId != null && arAuftragIId.size() > 0) {
					return;
				}

			}

			
			//crit.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_C_NR, "X000236_00"));
			
			HashSet hmReservierungenVorhanden = getReservierungFac().getSetOfArtikelIdMitReservierungen();

			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();
			
			myLogger.info("Bestellvorschlag mit " + resultList.size() + " Artikeln");

			while (resultListIterator.hasNext()) {
				FLRArtikel artikel = (FLRArtikel) resultListIterator.next();

				if (Helper.short2boolean(artikel.getB_lagerbewirtschaftet())) {

					// Ist dieser Artikel eine Stueckliste?
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikel.getI_id(), theClientDto);

					if (stuecklisteDto != null && Helper.short2boolean(stuecklisteDto.getBFremdfertigung()) == false) {
						// selbstgefertigte Stuecklisten werden im BV nicht
						// beruecksichtigt.
					} else {
						ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikel.getI_id(),
								theClientDto);
						ArtklaDto artklsDto = null;
						if (artikelDto.getArtklaIId() != null) {
							artklsDto = getArtikelFac().artklaFindByPrimaryKey(artikelDto.getArtklaIId(), theClientDto);
						}
						if (artklsDto != null && Helper.short2boolean(artklsDto.getBTops()) == true) {
							// Keine tops artikel
						} else {
							// Wenn keine Stueckliste, bzw. Fremdgefertigte
							// Stueckliste, dann
							ArrayList<?> al = null;

							if (bNurLospositionenBeruecksichtigen == true) {
								al = getInternebestellungFac().berechneBedarfe(artikelDto, iVorlaufzeit, iVorlaufzeit,
										iToleranz, dateFuerEintraegeOhneLiefertermin, false, theClientDto, arLosIId,
										bNichtFreigegebeneAuftraegeBeruecksichtigen, partnerIIdStandort,
										hmReservierungenVorhanden,bExakterAuftragsbezug);
							} else {
								al = getInternebestellungFac().berechneBedarfe(artikelDto, iVorlaufzeit, iVorlaufzeit,
										iToleranz, dateFuerEintraegeOhneLiefertermin, false, theClientDto, null,
										bNichtFreigegebeneAuftraegeBeruecksichtigen, partnerIIdStandort,
										hmReservierungenVorhanden,bExakterAuftragsbezug);
							}

							MaterialbedarfDto[] materialbedarf = new MaterialbedarfDto[al.size()];
							materialbedarf = (MaterialbedarfDto[]) al.toArray(materialbedarf);

							// PJ21665
							if (bArtikelNurAufAuftraegeIgnorieren == true) {

								boolean bEsSindBedarfeVorhanden = false;
								boolean bAlleAbgaengeSindVonAuftraegen = true;

								for (int i = 0; i < materialbedarf.length; i++) {

									bEsSindBedarfeVorhanden = true;
									if (!materialbedarf[i].getSBelegartCNr().equals(LocaleFac.BELEGART_AUFTRAG)) {
										bAlleAbgaengeSindVonAuftraegen = false;
									}

								}

								if (bEsSindBedarfeVorhanden == true && bAlleAbgaengeSindVonAuftraegen == true) {
									continue;
								}
							}

							for (int i = 0; i < materialbedarf.length; i++) {

								bestellvorschlagDtoErzeugen(materialbedarf[i].getSBelegartCNr(),
										artikel.getMandant_c_nr(), artikel.getI_id(), materialbedarf[i].getIBelegIId(),
										materialbedarf[i].getIBelegpositionIId(),
										new java.sql.Timestamp(materialbedarf[i].getTTermin().getTime()),
										materialbedarf[i].getNMenge(), materialbedarf[i].getProjektIId(),
										materialbedarf[i].getXTextinhalt(), materialbedarf[i].getLieferantIId(),
										materialbedarf[i].getNEinkaufpreis(), materialbedarf[i].getFLagermindest(), materialbedarf[i].getPartnerIIdStandort(),
										bZentralerArtikelstamm, theClientDto);
							}
						}
					}
				} else {
					if (bMitNichtlagerbewirtschafteten == true) {

						BigDecimal stdMenge = new BigDecimal(0);
						ArtikellieferantDto[] dtos = getArtikelFac().artikellieferantFindByArtikelIId(artikel.getI_id(),
								theClientDto);

						if (dtos != null && dtos.length > 0 && dtos[0].getFStandardmenge() != null) {
							stdMenge = new BigDecimal(dtos[0].getFStandardmenge());
						}

						bestellvorschlagDtoErzeugen(null, artikel.getMandant_c_nr(), artikel.getI_id(), null, null,
								new java.sql.Timestamp(dateFuerEintraegeOhneLiefertermin.getTime()), stdMenge, null,
								null, null, null, null, partnerIIdStandort, bZentralerArtikelstamm, theClientDto);

					}
				}
			}

			long duration = System.currentTimeMillis() - lStart;
			alDtos.add(new KeyvalueDto("DauerInSekunden", Long.valueOf(duration / 1000) + ""));
			myLogger.info("Dauer Bestellvorschlag: " + duration + "ms");

			getSystemServicesFac().replaceKeyvaluesEinerGruppe(
					getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(theClientDto), alDtos);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	public String getKEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG(TheClientDto theClientDto) {

		String s = SystemServicesFac.KEYVALUE_EINSTELLUNGEN_LETZTER_BESTELLVORSCHLAG + theClientDto.getMandant();

		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant()) == true) {
			s += theClientDto.getIDPersonal();
		}
		return s;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void bestellvorschlagInLiefergruppenanfragenUmwandeln(String projekt, TheClientDto theClientDto) {
		// Liefergruppen
		HashMap<Integer, ArrayList<FLRBestellvorschlag>> liefergruppen = new HashMap<Integer, ArrayList<FLRBestellvorschlag>>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT bv" + " FROM FLRBestellvorschlag AS bv "
				+ " WHERE bv.flrartikel.flrliefergruppe.i_id IS NOT NULL ORDER BY bv.flrartikel.flrliefergruppe.i_id ASC, bv.flrartikel.c_nr ASC ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<FLRBestellvorschlag> resultList = hqlquery.list();
		Iterator<FLRBestellvorschlag> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			FLRBestellvorschlag bv = resultListIterator.next();

			if (bv.getFlrartikel().getFlrliefergruppe() != null) {
				ArrayList<FLRBestellvorschlag> alArtikel = null;

				if (liefergruppen.containsKey(bv.getFlrartikel().getFlrliefergruppe().getI_id())) {
					alArtikel = liefergruppen.get(bv.getFlrartikel().getFlrliefergruppe().getI_id());
				} else {
					alArtikel = new ArrayList<FLRBestellvorschlag>();

				}

				alArtikel.add(bv);
				liefergruppen.put(bv.getFlrartikel().getFlrliefergruppe().getI_id(), alArtikel);
			}

		}

		KostenstelleDto kostenstelleDto = getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto).getKostenstelleDto_Stamm();

		Iterator<Integer> liefergrupenIds = liefergruppen.keySet().iterator();

		while (liefergrupenIds.hasNext()) {

			Integer liefergruppeIId = liefergrupenIds.next();
			ArrayList<FLRBestellvorschlag> alArtikel = liefergruppen.get(liefergruppeIId);

			AnfrageDto anfrageDto = new AnfrageDto();
			anfrageDto.setMandantCNr(theClientDto.getMandant());
			anfrageDto.setCBez(projekt);
			anfrageDto.setLiefergruppeIId(liefergruppeIId);
			anfrageDto.setFWechselkursmandantwaehrungzubelegwaehrung(1D);
			anfrageDto.setFAllgemeinerRabattsatz(0D);
			anfrageDto.setNTransportkosteninanfragewaehrung(new BigDecimal(0));
			anfrageDto.setWaehrungCNr(theClientDto.getSMandantenwaehrung());
			anfrageDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System.currentTimeMillis())));
			anfrageDto.setStatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
			anfrageDto.setArtCNr(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE);
			anfrageDto.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
			anfrageDto.setKostenstelleIId(kostenstelleDto.getIId());

			PersonalDto anfragerDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);
			anfrageDto.setPersonalIIdAnfrager(anfragerDto.getIId());
			
			
			Integer anfrageIId = getAnfrageFac().createAnfrage(anfrageDto, theClientDto);

			for (int i = 0; i < alArtikel.size(); i++) {
				FLRBestellvorschlag bv = alArtikel.get(i);
				AnfragepositionDto anfragepositionDto = new AnfragepositionDto();
				anfragepositionDto.setBelegIId(anfrageIId);
				anfragepositionDto.setArtikelIId(bv.getArtikel_i_id());
				anfragepositionDto.setPositionsartCNr(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT);
				anfragepositionDto.setNMenge(bv.getN_zubestellendemenge());
				anfragepositionDto.setEinheitCNr(bv.getFlrartikel().getEinheit_c_nr());
				anfragepositionDto.setNRichtpreis(new BigDecimal(0));
				anfragepositionDto.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
				anfragepositionDto.setXTextinhalt(bv.getX_textinhalt());

				getAnfragepositionFac().createAnfrageposition(anfragepositionDto, theClientDto);

				try {
					getBestellvorschlagFac().removeBestellvorschlag(bv.getI_id());
				} catch (Exception e) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
							"Bestellvorschlag ID=" + bv.getI_id());
				}
			}

		}

		session.close();

	}

	public void artikellieferantZuruecksetzen(ArrayList<Integer> bestellvorschlagIIds, TheClientDto theClientDto) {

		for (int i = 0; i < bestellvorschlagIIds.size(); i++) {
			Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class, bestellvorschlagIIds.get(i));

			ArtikellieferantDto[] artLiefDto = getArtikelFac()
					.artikellieferantFindByArtikelIId(bestellvorschlag.getArtikelIId(), theClientDto);
			if (artLiefDto != null && artLiefDto.length > 0) {
				bestellvorschlag.setLieferantIId(artLiefDto[0].getLieferantIId());
			}
		}
	}

	private BestellvorschlagDto setupBestellvorschlagDto(String belegartCNr, String mandantCNr, Integer artikelIId,
			Integer belegIId, Integer belegpositionIId, java.sql.Timestamp tTermin, BigDecimal nMenge,
			Integer projektIId, String xTextinhalt, Integer lieferantIId, BigDecimal nEinkaufspreis,Double fLagermindest,
			Integer partnerIIdStandort, boolean bZentralerArtikelstamm, TheClientDto theClientDto) {
		BestellvorschlagDto bestellvorschlagDto = new BestellvorschlagDto();
		bestellvorschlagDto.setCBelegartCNr(belegartCNr);
		bestellvorschlagDto.setCMandantCNr(theClientDto.getMandant());
		bestellvorschlagDto.setIArtikelId(artikelIId);
		bestellvorschlagDto.setIBelegartId(belegIId);
		bestellvorschlagDto.setIBelegartpositionid(belegpositionIId);
		bestellvorschlagDto.setTLiefertermin(tTermin);
		bestellvorschlagDto.setNZubestellendeMenge(nMenge);
		bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		bestellvorschlagDto.setProjektIId(projektIId);
		bestellvorschlagDto.setXTextinhalt(xTextinhalt);
		bestellvorschlagDto.setBVormerkung(Helper.boolean2Short(false));
		bestellvorschlagDto.setPartnerIIdStandort(partnerIIdStandort);
		bestellvorschlagDto.setFLagermindest(fLagermindest);

		// PJ20810
		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
			bestellvorschlagDto.setPersonalIId(theClientDto.getIDPersonal());
		}

		// PJ19435
		boolean bEigengefertigteStuecklisteImAnderenMandanten = false;
		if (bZentralerArtikelstamm == true) {

			bestellvorschlagDto.setNNettoeinzelpreis(BigDecimal.ZERO);
			bestellvorschlagDto.setNNettogesamtpreis(BigDecimal.ZERO);
			bestellvorschlagDto.setDRabattsatz(0D);
			bestellvorschlagDto.setNRabattbetrag(BigDecimal.ZERO);

			Query query = em.createNamedQuery("StuecklistefindByArtikelIId");
			query.setParameter(1, artikelIId);

			Collection c = query.getResultList();
			if (c.size() > 0) {
				Stueckliste stkl = (Stueckliste) c.iterator().next();
				if (!stkl.getMandantCNr().equals(theClientDto.getMandant())
						&& !Helper.short2boolean(stkl.getBFremdfertigung())) {
					bEigengefertigteStuecklisteImAnderenMandanten = true;
					// Lieferant ist der andere Mandant
					Mandant mandant = em.find(Mandant.class, stkl.getMandantCNr());

					try {
						LieferantDto liefDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
								mandant.getPartnerIId(), theClientDto.getMandant(), theClientDto);

						if (liefDto != null) {

							bestellvorschlagDto.setILieferantId(liefDto.getIId());

							// Preis ist VK-Basis

							java.sql.Date dTermin = new java.sql.Date(System.currentTimeMillis());
							if (tTermin != null) {
								dTermin = new java.sql.Date(tTermin.getTime());
							}

							BigDecimal bdPreisbasis = getVkPreisfindungFac().ermittlePreisbasis(artikelIId, dTermin,
									null, theClientDto.getSMandantenwaehrung(), theClientDto);

							if (bdPreisbasis != null) {
								bestellvorschlagDto.setNNettoeinzelpreis(bdPreisbasis);
								bestellvorschlagDto.setNNettogesamtpreis(bdPreisbasis);
							}

						} else {

							PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(mandant.getPartnerIId(),
									theClientDto);

							ArrayList<Object> al = new ArrayList<Object>();
							al.add(pDto.formatAnrede());
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_ANDERER_MANDANT_LIEFERANT_NICHT_ANGELEGT, al,
									new Exception(pDto.formatAnrede() + " nicht als Lieferant angelegt"));

						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			}
		}

		// Einkaufspreis des ersten Lieferanten in
		// Mandantenwaehrung

		if (bEigengefertigteStuecklisteImAnderenMandanten == false) {
			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT al FROM FLRArtikellieferant al WHERE al.b_nicht_lieferbar=0 AND al.artikel_i_id=" + artikelIId;
			sQuery += " AND al.flrlieferant.mandant_c_nr='" + theClientDto.getMandant() + "'";
			sQuery += " ORDER BY al.i_sort ASC";

			ArtikellieferantDto artLiefDto = null;

			org.hibernate.Query query = session.createQuery(sQuery);
			query.setMaxResults(1);
			List<?> result = query.list();

			// PJ19529
			if (result.size() == 0 && bZentralerArtikelstamm == true) {

				sQuery = "SELECT al FROM FLRArtikellieferant al WHERE al.b_nicht_lieferbar=0 AND al.artikel_i_id=" + artikelIId;
				sQuery += " ORDER BY al.i_sort ASC";
				query = session.createQuery(sQuery);
				query.setMaxResults(1);
				result = query.list();

				Iterator it = result.iterator();
				if (it.hasNext()) {
					FLRArtikellieferant flrArtikellieferant = (FLRArtikellieferant) it.next();
					Mandant mandant = em.find(Mandant.class, flrArtikellieferant.getFlrlieferant().getMandant_c_nr());
					try {

						LieferantDto liefDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(
								mandant.getPartnerIId(), theClientDto.getMandant(), theClientDto);

						if (liefDto != null) {

							bestellvorschlagDto.setILieferantId(liefDto.getIId());

							// PJ21369
							Integer iWiederbeschaffungszeit = null;
							ArtikellieferantstaffelDto[] staffeln = getArtikelFac()
									.artikellieferantstaffelFindByArtikellieferantIIdFMenge(
											flrArtikellieferant.getI_id(), nMenge,
											new java.sql.Date(tTermin.getTime()));

							if (staffeln.length > 0) {
								ArtikellieferantstaffelDto staffel = staffeln[0];
								if (staffel.getIWiederbeschaffungszeit() != null) {
									iWiederbeschaffungszeit = staffel.getIWiederbeschaffungszeit();
								}

							}

							if (iWiederbeschaffungszeit == null) {
								iWiederbeschaffungszeit = flrArtikellieferant.getI_wiederbeschaffungszeit();
							}

							if (iWiederbeschaffungszeit == null) {
								iWiederbeschaffungszeit = 0;
							}

							int i = (Integer) getParameterFac().getAnwenderparameter(ParameterFac.KATEGORIE_ALLGEMEIN,
									ParameterFac.ANWENDERPARAMETER_MANDANTEN_TRANSFERDAUER).getCWertAsObject();

							ParametermandantDto parameterDtoWBZ = getParameterFac().getMandantparameter(
									theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
									ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
							int iFaktor = 1;
							if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
								iFaktor = 7;

							} else if (parameterDtoWBZ.getCWert()
									.equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
								iFaktor = 1;
							} else {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
										new Exception(ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT
												+ " ist nicht richtig definiert"));
							}

							bestellvorschlagDto.setIWiederbeschaffungszeit(iWiederbeschaffungszeit + (i / iFaktor));

						} else {

							PartnerDto pDto = getPartnerFac().partnerFindByPrimaryKey(mandant.getPartnerIId(),
									theClientDto);

							ArrayList<Object> al = new ArrayList<Object>();
							al.add(pDto.formatAnrede());
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_ANDERER_MANDANT_LIEFERANT_NICHT_ANGELEGT, al,
									new Exception(pDto.formatAnrede() + " nicht als Lieferant angelegt"));

						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

			} else {
				Iterator it = result.iterator();
				if (it.hasNext()) {
					FLRArtikellieferant flrArtikellieferant = (FLRArtikellieferant) it.next();
					try {
						artLiefDto = getArtikelFac().artikellieferantFindByPrimaryKey(flrArtikellieferant.getI_id(),
								theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}
				session.close();
			}

			if (artLiefDto != null) {

				ArtikellieferantDto helper2 = getArtikelFac().getArtikelEinkaufspreis(artikelIId,
						artLiefDto.getLieferantIId(), nMenge, theClientDto.getSMandantenwaehrung(), null, theClientDto);

				bestellvorschlagDto.setILieferantId(artLiefDto.getLieferantIId());

				if (helper2 != null) {

					// PJ20771
					if (Helper.short2Boolean(helper2.getBNichtLieferbar())) {
						bestellvorschlagDto.setILieferantId(null);
					} else {
						bestellvorschlagDto.setIWiederbeschaffungszeit(helper2.getIWiederbeschaffungszeit());
						bestellvorschlagDto.setNNettoeinzelpreis(helper2.getNEinzelpreis());
						bestellvorschlagDto.setNNettogesamtpreis(helper2.getNNettopreis());
						bestellvorschlagDto.setDRabattsatz(helper2.getFRabatt());
						if (helper2.getNEinzelpreis() != null && helper2.getFRabatt() != null) {
							bestellvorschlagDto.setNRabattbetrag(Helper.rundeKaufmaennisch(helper2.getNEinzelpreis()
									.multiply(new BigDecimal(helper2.getFRabatt()).movePointLeft(2)), 4));
						}
						if (Helper.short2boolean(helper2.getBRabattbehalten())) {
							bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
						} else {
							bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
						}
					}

				} else {
					bestellvorschlagDto.setIWiederbeschaffungszeit(artLiefDto.getIWiederbeschaffungszeit());
					bestellvorschlagDto.setNNettoeinzelpreis(artLiefDto.getNEinzelpreis());
					bestellvorschlagDto.setNNettogesamtpreis(artLiefDto.getNNettopreis());
					bestellvorschlagDto.setDRabattsatz(artLiefDto.getFRabatt());
					if (artLiefDto.getNEinzelpreis() != null && artLiefDto.getFRabatt() != null) {
						bestellvorschlagDto.setNRabattbetrag(Helper.rundeKaufmaennisch(artLiefDto.getNEinzelpreis()
								.multiply(new BigDecimal(artLiefDto.getFRabatt()).movePointLeft(2)), 4));
					}
					if (Helper.short2boolean(artLiefDto.getBRabattbehalten())) {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
					} else {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
					}
				}

			}

			if (nEinkaufspreis != null) {
				bestellvorschlagDto.setNNettoeinzelpreis(nEinkaufspreis);
				bestellvorschlagDto.setNNettogesamtpreis(nEinkaufspreis);
				bestellvorschlagDto.setDRabattsatz(0D);
				bestellvorschlagDto.setNRabattbetrag(BigDecimal.ZERO);
			}
			if (lieferantIId != null) {
				bestellvorschlagDto.setILieferantId(lieferantIId);
			}
		}

		return bestellvorschlagDto;
	}

	public void bestellvorschlagDtoErzeugen(String belegartCNr, String mandantCNr, Integer artikelIId, Integer belegIId,
			Integer belegpositionIId, java.sql.Timestamp tTermin, BigDecimal nMenge, Integer projektIId,
			String xTextinhalt, Integer lieferantIId, BigDecimal nEinkaufspreis, Double fLagermindest,Integer partnerIIdStandort,
			boolean bZentralerArtikelstamm, TheClientDto theClientDto) {
		BestellvorschlagDto bestellvorschlagDto = setupBestellvorschlagDto(belegartCNr, mandantCNr, artikelIId,
				belegIId, belegpositionIId, tTermin, nMenge, projektIId, xTextinhalt, lieferantIId, nEinkaufspreis,fLagermindest,
				partnerIIdStandort, bZentralerArtikelstamm, theClientDto);
		try {
			context.getBusinessObject(BestellvorschlagFac.class).createBestellvorschlag(bestellvorschlagDto,
					theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public void loescheBestellvorlaegeEinesMandaten(boolean vormerklisteLoeschen, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String hqlDelete = "delete FLRBestellvorschlag WHERE 1=1";

			if (!getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
					MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto)) {

				hqlDelete += " and mandant_c_nr ='" + theClientDto.getMandant() + "'";

			} else {
				// SP4441
				if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER,
						theClientDto)) {
					hqlDelete += " and mandant_c_nr ='" + theClientDto.getMandant() + "'";
				}
			}

			if (vormerklisteLoeschen == false) {
				hqlDelete += " and b_vormerkung = 0 ";
			}

			// PJ20810
			if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
				hqlDelete += " and personal_i_id = " + theClientDto.getIDPersonal();
			}

			session.createQuery(hqlDelete).executeUpdate();
		} finally {
			closeSession(session);
		}
	}

	private BestellvorschlagDto[] bereinigeBestellvorschlagVonVeraltenArtikeln(BestellvorschlagDto[] bvDtos,
			TheClientDto theClientDto) {

		boolean bRausfiltern = false;
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE, ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT);

			if (param.getCWertAsObject() != null && ((Integer) param.getCWertAsObject()) == 1) {
				bRausfiltern = true;
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (bRausfiltern == true) {
			ArrayList<BestellvorschlagDto> alDaten = new ArrayList<BestellvorschlagDto>();

			Object[] o = getBestellungReportFac().getGeaenderteArtikelDaten(theClientDto);
			HashMap hmVeralteteArtikel = (HashMap) o[0];

			for (int i = 0; i < bvDtos.length; i++) {

				if (!hmVeralteteArtikel.containsKey(bvDtos[i].getIArtikelId())) {

					alDaten.add(bvDtos[i]);
				}

			}

			return (BestellvorschlagDto[]) alDaten.toArray(new BestellvorschlagDto[] {});
		} else {
			return bvDtos;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RueckgabeUeberleitungDto createBESausBVfuerAlleLieferantenMitGleichenTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen, Integer standortIId,
			boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) {
		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();
		try {
			// alle Lieferanten
			ArrayList<?> lieferantenIds = (ArrayList<?>) LieferantHandler
					.getFLRLieferantenliste(LieferantHandler.getQueryLieferantenliste());

			for (int i = 0; i < lieferantenIds.size(); i++) {

				FilterKriterium[] fkneu = new FilterKriterium[2];
				fkneu[0] = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "." + "i_id", true,
						"'" + lieferantenIds.get(i) + "'", FilterKriterium.OPERATOR_EQUAL, false);

				fkneu[1] = new FilterKriterium(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN, true, "",
						FilterKriterium.OPERATOR_IS + " " + FilterKriterium.OPERATOR_NOT_NULL, false);

				if (fk != null) {
					fkneu = FilterKriterium.concat(fkneu, fk);
				}

				BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler.getListeBestellvorschlaege(fkneu,
						ski, BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF_UND_GLEICHE_TERMIN,
						theClientDto.getMandant(), gemeinsameArtikelBestellen, bInklGesperrteArtikel);

				aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(aBestellvorschlagDto, theClientDto);

				boolean lagerminJeLager = false;
				try {
					ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

					lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				if (lagerminJeLager) {
					HashMap<Integer, BestellvorschlagDto[]> hmNachStandorteGetrennt = trenneNachStandorten(
							aBestellvorschlagDto);
					Iterator itStandorte = hmNachStandorteGetrennt.keySet().iterator();
					while (itStandorte.hasNext()) {
						Integer partnerIIdStandort = (Integer) itStandorte.next();
						BestellvorschlagDto[] bbDtos = hmNachStandorteGetrennt.get(partnerIIdStandort);

						if (standortIId == null || standortIId.equals(partnerIIdStandort)) {

							tmBetroffeneArtikel.putAll(getBestellvorschlagFac()
									.befuellenDerBestellungUndBestellposition(bbDtos, kostenstelleIId,
											partnerIIdStandort, bRahmenbestellungErzeugen, theClientDto));
						}
					}
				} else {

					if (bProjektklammerberuecksichtigen == true) {
						HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(
								aBestellvorschlagDto);
						Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
						while (itProjekte.hasNext()) {
							BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt.get(itProjekte.next());
							tmBetroffeneArtikel
									.putAll(getBestellvorschlagFac().befuellenDerBestellungUndBestellposition(bbDtos,
											kostenstelleIId, null, bRahmenbestellungErzeugen, theClientDto));
						}
					} else {
						tmBetroffeneArtikel = getBestellvorschlagFac().befuellenDerBestellungUndBestellposition(
								aBestellvorschlagDto, kostenstelleIId, null, bRahmenbestellungErzeugen, theClientDto);
					}
				}

			}
		} catch (EJBExceptionLP e) {
			return new RueckgabeUeberleitungDto(false, null);
		}
		return new RueckgabeUeberleitungDto(true, tmBetroffeneArtikel);

	}

	public void gemeinsameArtikelLoeschen(TheClientDto theClientDto) {

		String sQuery = "SELECT flrbestellvorschlag,(SELECT count(*) from FLRBestellvorschlag bv2 WHERE bv2.mandant_c_nr=flrbestellvorschlag.mandant_c_nr AND bv2.artikel_i_id=flrbestellvorschlag.artikel_i_id AND bv2.personal_i_id<>flrbestellvorschlag.personal_i_id) as anzahlbvandererbenutzer from FLRBestellvorschlag flrbestellvorschlag WHERE flrbestellvorschlag.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND flrbestellvorschlag.personal_i_id=" + theClientDto.getIDPersonal();
		Session session = FLRSessionFactory.getFactory().openSession();
		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> list = query.list();

		Iterator<?> it = list.iterator();

		while (it.hasNext()) {

			Object[] o = (Object[]) it.next();
			FLRBestellvorschlag flrbestellvorschlag = (FLRBestellvorschlag) o[0];

			if (o[1] != null && ((Long) o[1]) > 0) {
				removeBestellvorschlag(flrbestellvorschlag.getI_id());
			}

		}

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RueckgabeUeberleitungDto createBESausBVjeLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId, boolean bRahmenbestellungErzeugen,
			boolean bInklGesperrteArtikel) {

		// alle Lieferanten
		ArrayList<?> lieferantenIds = (ArrayList<?>) LieferantHandler
				.getFLRLieferantenliste(LieferantHandler.getQueryLieferantenliste());

		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();

		for (int i = 0; i < lieferantenIds.size(); i++) {

			FilterKriterium[] fkneu = new FilterKriterium[1];
			fkneu[0] = new FilterKriterium(BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "." + "i_id", true,
					"'" + lieferantenIds.get(i) + "'", FilterKriterium.OPERATOR_EQUAL, false);

			if (fk != null) {
				fkneu = FilterKriterium.concat(fkneu, fk);
			}

			BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler.getListeBestellvorschlaege(fkneu, ski,
					BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF, theClientDto.getMandant(),
					gemeinsameArtikelBestellen, bInklGesperrteArtikel);
			aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(aBestellvorschlagDto, theClientDto);

			boolean lagerminJeLager = false;
			try {
				ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

				lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			if (lagerminJeLager) {
				HashMap<Integer, BestellvorschlagDto[]> hmNachStandorteGetrennt = trenneNachStandorten(
						aBestellvorschlagDto);
				Iterator itStandorte = hmNachStandorteGetrennt.keySet().iterator();
				while (itStandorte.hasNext()) {
					Integer partnerIIdStandort = (Integer) itStandorte.next();
					BestellvorschlagDto[] bbDtos = hmNachStandorteGetrennt.get(partnerIIdStandort);

					if (standortIId == null || standortIId.equals(partnerIIdStandort)) {

						tmBetroffeneArtikel.putAll(
								getBestellvorschlagFac().befuellenDerBestellungausBVfuerBestimmtenLieferant(bbDtos,
										kostenstelleIId, partnerIIdStandort, bRahmenbestellungErzeugen, theClientDto));
					}
				}
			} else {

				if (bProjektklammerberuecksichtigen == true) {
					HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(
							aBestellvorschlagDto);
					Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
					while (itProjekte.hasNext()) {
						BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt.get(itProjekte.next());

						tmBetroffeneArtikel.putAll(
								getBestellvorschlagFac().befuellenDerBestellungausBVfuerBestimmtenLieferant(bbDtos,
										kostenstelleIId, null, bRahmenbestellungErzeugen, theClientDto));

					}
				} else {
					tmBetroffeneArtikel.putAll(getBestellvorschlagFac()
							.befuellenDerBestellungausBVfuerBestimmtenLieferant(aBestellvorschlagDto, kostenstelleIId,
									null, bRahmenbestellungErzeugen, theClientDto));
				}
			}

		}
		return new RueckgabeUeberleitungDto(true, tmBetroffeneArtikel);
	}

	public RueckgabeUeberleitungDto createBESausBVfuerBestimmtenLieferantUndTermin(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen, boolean gemeinsameArtikelBestellen, Integer standortIId,
			boolean bRahmenbestellungErzeugen, boolean bInklGesperrteArtikel) {

		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();

		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler.getListeBestellvorschlaege(fk, ski,
				BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF_UND_TERMIN, theClientDto.getMandant(),
				gemeinsameArtikelBestellen, bInklGesperrteArtikel);
		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(aBestellvorschlagDto, theClientDto);

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (lagerminJeLager) {
			HashMap<Integer, BestellvorschlagDto[]> hmNachStandorteGetrennt = trenneNachStandorten(
					aBestellvorschlagDto);
			Iterator itStandorte = hmNachStandorteGetrennt.keySet().iterator();
			while (itStandorte.hasNext()) {
				Integer partnerIIdStandort = (Integer) itStandorte.next();
				BestellvorschlagDto[] bbDtos = hmNachStandorteGetrennt.get(partnerIIdStandort);

				if (standortIId == null || standortIId.equals(partnerIIdStandort)) {

					tmBetroffeneArtikel.putAll(befuellenDerBestellungUndBestellposition(bbDtos, kostenstelleIId,
							partnerIIdStandort, bRahmenbestellungErzeugen, theClientDto));
				}
			}
		} else {

			if (bProjektklammerberuecksichtigen == true) {
				HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(
						aBestellvorschlagDto);
				Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
				while (itProjekte.hasNext()) {
					BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt.get(itProjekte.next());
					tmBetroffeneArtikel.putAll(befuellenDerBestellungUndBestellposition(bbDtos, kostenstelleIId, null,
							bRahmenbestellungErzeugen, theClientDto));
				}
			} else {
				tmBetroffeneArtikel = befuellenDerBestellungUndBestellposition(aBestellvorschlagDto, kostenstelleIId,
						null, bRahmenbestellungErzeugen, theClientDto);
			}
		}

		return new RueckgabeUeberleitungDto(true, tmBetroffeneArtikel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RueckgabeUeberleitungDto createBESausBVzuRahmen(FilterKriterium[] fk, SortierKriterium[] ski,
			Integer standortIId, boolean gemeinsameArtikelBestellen, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {

		RueckgabeUeberleitungDto rueckgabeUeberleitungDto = new RueckgabeUeberleitungDto();
		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler.getListeBestellvorschlaege(fk, ski,
				BestellvorschlagFac.BES_ABRUFE_ZU_RAHMEN, theClientDto.getMandant(), gemeinsameArtikelBestellen, true);
		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(aBestellvorschlagDto, theClientDto);

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (lagerminJeLager) {
			HashMap<Integer, BestellvorschlagDto[]> hmNachStandorteGetrennt = trenneNachStandorten(
					aBestellvorschlagDto);
			Iterator itStandorte = hmNachStandorteGetrennt.keySet().iterator();
			while (itStandorte.hasNext()) {
				Integer partnerIIdStandort = (Integer) itStandorte.next();
				BestellvorschlagDto[] bbDtos = hmNachStandorteGetrennt.get(partnerIIdStandort);

				if (standortIId == null || standortIId.equals(partnerIIdStandort)) {

					rueckgabeUeberleitungDto.add(getBestellvorschlagFac().erstelleAbrufbestellungenAusBV(bbDtos,
							partnerIIdStandort, theClientDto));
				}
			}
		} else {
			rueckgabeUeberleitungDto.add(
					getBestellvorschlagFac().erstelleAbrufbestellungenAusBV(aBestellvorschlagDto, null, theClientDto));
		}

		rueckgabeUeberleitungDto.setbErfolgreich(true);

		return rueckgabeUeberleitungDto;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RueckgabeUeberleitungDto createBESausBVfueBestimmtenLieferant(FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen,
			boolean gemeinsameArtikelBestellen, Integer standortIId, boolean bRahmenbestellungErzeugen,
			boolean bInklGesperrteArtikel) {

		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();

		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler.getListeBestellvorschlaege(fk, ski,
				BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF, theClientDto.getMandant(),

				gemeinsameArtikelBestellen, bInklGesperrteArtikel);

		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(aBestellvorschlagDto, theClientDto);

		boolean lagerminJeLager = false;
		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (lagerminJeLager) {
			HashMap<Integer, BestellvorschlagDto[]> hmNachStandorteGetrennt = trenneNachStandorten(
					aBestellvorschlagDto);
			Iterator itStandorte = hmNachStandorteGetrennt.keySet().iterator();
			while (itStandorte.hasNext()) {
				Integer partnerIIdStandort = (Integer) itStandorte.next();
				BestellvorschlagDto[] bbDtos = hmNachStandorteGetrennt.get(partnerIIdStandort);

				if (standortIId == null || standortIId.equals(partnerIIdStandort)) {

					tmBetroffeneArtikel
							.putAll(getBestellvorschlagFac().befuellenDerBestellungausBVfuerBestimmtenLieferant(bbDtos,
									kostenstelleIId, partnerIIdStandort, bRahmenbestellungErzeugen, theClientDto));
				}
			}
		} else {

			if (bProjektklammerberuecksichtigen == true) {
				HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(
						aBestellvorschlagDto);
				Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
				while (itProjekte.hasNext()) {
					BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt.get(itProjekte.next());

					tmBetroffeneArtikel
							.putAll(getBestellvorschlagFac().befuellenDerBestellungausBVfuerBestimmtenLieferant(bbDtos,
									kostenstelleIId, null, bRahmenbestellungErzeugen, theClientDto));
				}
			} else {
				tmBetroffeneArtikel = getBestellvorschlagFac().befuellenDerBestellungausBVfuerBestimmtenLieferant(
						aBestellvorschlagDto, kostenstelleIId, null, bRahmenbestellungErzeugen, theClientDto);
			}
		}
		return new RueckgabeUeberleitungDto(true, tmBetroffeneArtikel);

	}

	private HashMap<Integer, BestellvorschlagDto[]> trenneNachProjekten(BestellvorschlagDto[] aBestellvorschlagDto) {
		HashMap<Integer, ArrayList<BestellvorschlagDto>> hmGetrenntNachProjekten = new HashMap<Integer, ArrayList<BestellvorschlagDto>>();

		for (int i = 0; i < aBestellvorschlagDto.length; i++) {

			Integer projektIId = aBestellvorschlagDto[i].getProjektIId();

			ArrayList<BestellvorschlagDto> alBV = new ArrayList<BestellvorschlagDto>();

			if (hmGetrenntNachProjekten.containsKey(projektIId)) {
				alBV = hmGetrenntNachProjekten.get(projektIId);
			} else {
				alBV = new ArrayList<BestellvorschlagDto>();
			}

			alBV.add(aBestellvorschlagDto[i]);

			hmGetrenntNachProjekten.put(projektIId, alBV);
		}

		HashMap<Integer, BestellvorschlagDto[]> hmProjekte = new HashMap<Integer, BestellvorschlagDto[]>();

		Iterator it = hmGetrenntNachProjekten.keySet().iterator();
		while (it.hasNext()) {
			Integer projektIId = (Integer) it.next();
			ArrayList<BestellvorschlagDto> alBV = hmGetrenntNachProjekten.get(projektIId);
			hmProjekte.put(projektIId, (BestellvorschlagDto[]) alBV.toArray(new BestellvorschlagDto[alBV.size()]));

		}

		return hmProjekte;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public java.util.TreeMap befuellenDerBestellungausBVfuerBestimmtenLieferant(
			BestellvorschlagDto[] aBestellvorschlagDto, Integer kostenstelleIId, Integer partnerIIdStandort,
			boolean bRahmenbestellungErzeugen, TheClientDto theClientDto) throws EJBExceptionLP {

		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();
		try {
			// isort in Bestellposition
			int j = 0;

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			boolean bBestellungAnlegen = true;
			Integer iIdBestellung = null;

			boolean bBestellvorschlagErzeugtLoszuordnung = true;

			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BESTELLUNG_AUS_BESTVOR_POSITIONEN_MIT_LOSZUORDNUNG);
			bBestellvorschlagErzeugtLoszuordnung = (Boolean) parameterDto.getCWertAsObject();

			boolean bAnsprechpartnerVorbesetzen = false;
			ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BESTELLUNG_ANSP_VORBESETZEN);
			bAnsprechpartnerVorbesetzen = (Boolean) param.getCWertAsObject();

			int iMaximaleAnzahlBestellpositionen = 250;
			ParametermandantDto paramPos = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_BESTELLUNG,
					ParameterFac.PARAMETER_BV_UEBERLEITUNG_MAXIMALE_ANZAHL_POSITIONEN);
			iMaximaleAnzahlBestellpositionen = (Integer) paramPos.getCWertAsObject();

			int iZahlerBestPos = 0;
			for (int i = 0; i < aBestellvorschlagDto.length; i++) {

				iZahlerBestPos++;

				// anlegen der Bestellung
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(aBestellvorschlagDto[0].getILieferantId(), theClientDto);
				BigDecimal wechselkurs = getLocaleFac().getWechselkurs2(mandantDto.getWaehrungCNr(),
						lieferantDto.getWaehrungCNr(), theClientDto);

				if (iZahlerBestPos > iMaximaleAnzahlBestellpositionen) {
					bBestellungAnlegen = true;
					j = 0;
					iZahlerBestPos = 1;
				}

				if (bBestellungAnlegen == true) {
					BestellungDto bestellungDto = new BestellungDto();

					// SP3131
					if (bAnsprechpartnerVorbesetzen == true) {
						AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
								.ansprechpartnerFindErstenEinesPartnersOhneExc(lieferantDto.getPartnerIId(),
										theClientDto);
						if (anspDto != null) {
							bestellungDto.setAnsprechpartnerIId(anspDto.getIId());
						}
					}

					PersonalDto anfordererDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
							theClientDto);
					bestellungDto.setPersonalIIdAnforderer(anfordererDto.getIId());

					bestellungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
					bestellungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
					bestellungDto.setProjektIId(aBestellvorschlagDto[0].getProjektIId());
					bestellungDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
					bestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
					// bestellungDto.setBestelltextIIdFusstext();
					// bestellungDto.setBestelltextIIdKopftext();

					if (bRahmenbestellungErzeugen) {
						bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR);
					} else {
						bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
					}

					bestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
					parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG);

					bestellungDto
							.setBTeillieferungMoeglich(Helper.boolean2Short((Boolean) parameterDto.getCWertAsObject()));
					bestellungDto.setDBelegdatum(this.getDate());
					bestellungDto.setDLiefertermin(aBestellvorschlagDto[0].getTLiefertermin());
					bestellungDto.setKostenstelleIId(kostenstelleIId);
					bestellungDto.setLieferantIIdBestelladresse(lieferantDto.getIId());
					bestellungDto.setLieferantIIdRechnungsadresse(lieferantDto.getIId());

					// Default Lieferadresse ist die Adresse des aktuellen
					// Mandanten
					Integer iPartnerIIdLIeferadresse = getMandantFac()
							.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
							.getPartnerIIdLieferadresse();

					if (partnerIIdStandort != null) {
						iPartnerIIdLIeferadresse = partnerIIdStandort;
					}

					// PJ21786 hat hoechste Prio
					if (lieferantDto.getPartnerIIdLieferadresse() != null) {
						iPartnerIIdLIeferadresse = lieferantDto.getPartnerIIdLieferadresse();
					}

					bestellungDto.setPartnerIIdLieferadresse(iPartnerIIdLIeferadresse);

					bestellungDto.setSpediteurIId(lieferantDto.getIdSpediteur());
					bestellungDto.setMandantCNr(mandantDto.getCNr());
					bestellungDto.setLieferartIId(lieferantDto.getLieferartIId());
					bestellungDto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
					bestellungDto.setFAllgemeinerRabattsatz(new Double(0));

					bestellungDto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(wechselkurs.doubleValue()));

					// PJ21315 Punkt i
					if (aBestellvorschlagDto[i].getCBelegartCNr() != null
							&& aBestellvorschlagDto[i].getCBelegartCNr().equals(LocaleFac.BELEGART_EINKAUFSANGEBOT)
							&& aBestellvorschlagDto[i].getIBelegartId() != null) {
						EinkaufsangebotDto ekagDto = getAngebotstklFac()
								.einkaufsangebotFindByPrimaryKey(aBestellvorschlagDto[i].getIBelegartId());

						bestellungDto.setCBez(ekagDto.getCProjekt());

					}

					iIdBestellung = getBestellungFac().createBestellung(bestellungDto, theClientDto);
					bBestellungAnlegen = false;
				}
				// Positionen anlegen.
				BestellpositionDto bestellpositionDto = new BestellpositionDto();

				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(aBestellvorschlagDto[i].getIArtikelId(),
						theClientDto);

				bestellpositionDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));

				bestellpositionDto.setArtikelIId(aBestellvorschlagDto[i].getIArtikelId());
				bestellpositionDto.setBDrucken(Helper.boolean2Short(false));
				bestellpositionDto.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				bestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bestellpositionDto.setBestellungIId(iIdBestellung);
				bestellpositionDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
				bestellpositionDto.setISort(new Integer(j++));
				bestellpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
				bestellpositionDto.setNMenge(aBestellvorschlagDto[i].getNZubestellendeMenge());
				bestellpositionDto.setXTextinhalt(aBestellvorschlagDto[i].getXTextinhalt());

				if (bBestellvorschlagErzeugtLoszuordnung == true) {
					if (aBestellvorschlagDto[i].getCBelegartCNr() != null
							&& aBestellvorschlagDto[i].getCBelegartCNr().equals(LocaleFac.BELEGART_LOS)
							&& aBestellvorschlagDto[i].getIBelegartpositionid() != null) {

						LossollmaterialDto lossollmaterialDto = getFertigungFac()
								.lossollmaterialFindByPrimaryKeyOhneExc(
										aBestellvorschlagDto[i].getIBelegartpositionid());
						if (lossollmaterialDto != null) {
							bestellpositionDto.setLossollmaterialIId(aBestellvorschlagDto[i].getIBelegartpositionid());
						}
					}
				}

				bestellpositionDto.setNMaterialzuschlag(new BigDecimal(0));

				if (artikelDto.getMaterialIId() != null) {
					BigDecimal zuschlag = getMaterialFac().getMaterialzuschlagEKInZielwaehrung(artikelDto.getIId(),
							lieferantDto.getIId(), this.getDate(), lieferantDto.getWaehrungCNr(), theClientDto);
					if (zuschlag != null) {
						bestellpositionDto.setNMaterialzuschlag(zuschlag);

						if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
							BigDecimal nettogesamtpreis = aBestellvorschlagDto[i].getNNettoeinzelpreis();
							if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
								nettogesamtpreis = nettogesamtpreis
										.subtract(aBestellvorschlagDto[i].getNRabattbetrag());
							}

							nettogesamtpreis = nettogesamtpreis.add(zuschlag);
							aBestellvorschlagDto[i].setNNettogesamtpreis(nettogesamtpreis);

						}

					}

				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
					bestellpositionDto.setNNettoeinzelpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							aBestellvorschlagDto[i].getNNettoeinzelpreis(), mandantDto.getWaehrungCNr(),
							lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
				} else {
					bestellpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
				}

				if (aBestellvorschlagDto[i].getNNettogesamtpreis() != null) {
					bestellpositionDto.setNNettogesamtpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							aBestellvorschlagDto[i].getNNettogesamtpreis(), mandantDto.getWaehrungCNr(),
							lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
				} else {
					bestellpositionDto.setNNettogesamtpreis(new BigDecimal(0));
				}
				if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
					bestellpositionDto.setNRabattbetrag(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
							aBestellvorschlagDto[i].getNRabattbetrag(), mandantDto.getWaehrungCNr(),
							lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
				} else {
					bestellpositionDto.setNRabattbetrag(new BigDecimal(0));
				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null
						&& !(aBestellvorschlagDto[i].getNNettoeinzelpreis().compareTo(new BigDecimal(0)) == 0)) {
					bestellpositionDto.setDRabattsatz(new Double(new BigDecimal(100)
							.subtract(Helper.calculateRatioInDecimal(aBestellvorschlagDto[i].getNRabattbetrag(),
									aBestellvorschlagDto[i].getNNettoeinzelpreis()))
							.doubleValue()));
				} else {
					bestellpositionDto.setDRabattsatz(new Double(0));
				}

				// SP2241
				ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
						aBestellvorschlagDto[i].getIArtikelId(), aBestellvorschlagDto[i].getILieferantId(),
						aBestellvorschlagDto[i].getNZubestellendeMenge(), theClientDto.getSMandantenwaehrung(),
						this.getDate(), theClientDto);

				if (alDto != null) {
					bestellpositionDto.setCAngebotnummer(alDto.getCAngebotnummer());
				}

				bestellpositionDto.setBNettopreisuebersteuert(aBestellvorschlagDto[i].getBNettopreisuebersteuert());
				bestellpositionDto.setTUebersteuerterLiefertermin(aBestellvorschlagDto[i].getTLiefertermin());
				bestellpositionDto.setGebindeIId(aBestellvorschlagDto[i].getGebindeIId());
				bestellpositionDto.setNAnzahlgebinde(aBestellvorschlagDto[i].getNAnzahlgebinde());

				getBestellpositionFac().createBestellposition(bestellpositionDto, theClientDto,
						BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);

				tmBetroffeneArtikel.put(artikelDto.getCNr(), artikelDto);

				this.removeBestellvorschlag(aBestellvorschlagDto[i]);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return tmBetroffeneArtikel;
	}

	public RueckgabeUeberleitungDto erstelleAbrufbestellungenAusBV(BestellvorschlagDto[] bestellvorschlagDto,
			Integer partnerIIdStandort, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Session session = FLRSessionFactory.getFactory().openSession();
		try {

			java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();

			ArrayList<Integer> alUeberlieferteBestellungIId = new ArrayList<Integer>();
			HashMap<Integer, Integer> hmAbrufeZuRahmen = new HashMap<Integer, Integer>();
			for (int i = 0; i < bestellvorschlagDto.length; i++) {
				boolean bUebergeleitet = false;
				if (bestellvorschlagDto[i].getILieferantId() != null) {
					BigDecimal bdZuBestellendeMenge = bestellvorschlagDto[i].getNZubestellendeMenge();
					Criteria besPos = session.createCriteria(FLRBestellposition.class);
					Criteria best = besPos.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
					Criteria artikel = besPos.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
					// Filter auf den Mandanten
					best.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR, theClientDto.getMandant()));
					// nur Rahmenbestellungen.
					best.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
							BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
					// Einschraenkung nach Status Offen, Erledigt
					Collection<String> cStati = new LinkedList<String>();
					cStati.add(BestellungFac.BESTELLSTATUS_TEILERLEDIGT);
					cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
					cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
					best.add(Restrictions.in(BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR, cStati));
					best.add(Restrictions.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
							bestellvorschlagDto[i].getILieferantId()));
					besPos.add(
							Restrictions.gt(BestellpositionFac.FLR_BESTELLPOSITION_N_OFFENEMENGE, new BigDecimal(0)));
					artikel.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_I_ID, bestellvorschlagDto[i].getIArtikelId()));
					// Sortierung damit die aelteren Rahmen zuerst abgerufen
					// werden
					best.addOrder(Order.desc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
					// Query ausfuehren
					List<?> besposList = besPos.list();
					Iterator<?> besposListIterator = besposList.iterator();
					boolean bMengeGenuegt = false;
					ArrayList<Integer> alVerwendeteBesPosIId = new ArrayList<Integer>();
					while (besposListIterator.hasNext() && !bMengeGenuegt) {
						FLRBestellposition bespos = (FLRBestellposition) besposListIterator.next();
						if (bdZuBestellendeMenge.compareTo(bespos.getN_offenemenge()) < 0) {
							bMengeGenuegt = true;
						} else {
							bdZuBestellendeMenge = bdZuBestellendeMenge.subtract(bespos.getN_offenemenge());
						}
						alVerwendeteBesPosIId.add(bespos.getI_id());
					}
					// Jetzt haben wir eine Liste aller Rahmenpositionen die
					// verwendet werden sollen.
					BigDecimal bdBereitsBestellteMenge = new BigDecimal(0);
					for (int y = 0; y < alVerwendeteBesPosIId.size(); y++) {
						BestellpositionDto besPosDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(alVerwendeteBesPosIId.get(y));
						BestellungDto abrufBestellungDto = null;
						if (hmAbrufeZuRahmen.containsKey(besPosDto.getBelegIId())) {
							abrufBestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(hmAbrufeZuRahmen.get(besPosDto.getBelegIId()));
						}
						BestellungDto rahmenBestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(besPosDto.getBelegIId());
						boolean bAbrufvorhanden = false;
						// wenn wir bereits durch einen anderen Bestellvorschlag
						// einen Abruf fuer diesen Lieferanten mit diesem
						// Liefertermin angelegt haben
						if (abrufBestellungDto != null) {
							if (abrufBestellungDto.getDLiefertermin()
									.equals(bestellvorschlagDto[i].getTLiefertermin())) {
								bAbrufvorhanden = true;
							}
						}
						if (!bAbrufvorhanden) {
							// Wir haben fuer diese Bestellung noch keinen Abruf
							// und legen diesen an
							MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
									theClientDto);

							// Wechselkurs ist immer der aktuelle
							BigDecimal wechselkurs = getLocaleFac().getWechselkurs2(
									rahmenBestellungDto.getWaehrungCNr(), mandantDto.getWaehrungCNr(), theClientDto);
							abrufBestellungDto = new BestellungDto();
							abrufBestellungDto.setFWechselkursmandantwaehrungzubelegwaehrung(
									new Double(wechselkurs.doubleValue()));
							abrufBestellungDto.setPersonalIIdAnforderer(rahmenBestellungDto.getPersonalIIdAnforderer());
							abrufBestellungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
							abrufBestellungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
							abrufBestellungDto.setZahlungszielIId(rahmenBestellungDto.getZahlungszielIId());
							abrufBestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
							abrufBestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR);
							abrufBestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
							abrufBestellungDto.setIBestellungIIdRahmenbestellung(rahmenBestellungDto.getIId());
							abrufBestellungDto
									.setBTeillieferungMoeglich(rahmenBestellungDto.getBTeillieferungMoeglich());
							abrufBestellungDto.setDBelegdatum(this.getDate());
							abrufBestellungDto.setDLiefertermin(bestellvorschlagDto[i].getTLiefertermin());
							abrufBestellungDto.setKostenstelleIId(rahmenBestellungDto.getKostenstelleIId());
							abrufBestellungDto
									.setLieferantIIdBestelladresse(rahmenBestellungDto.getLieferantIIdBestelladresse());

							abrufBestellungDto.setAnsprechpartnerIId(rahmenBestellungDto.getAnsprechpartnerIId());

							abrufBestellungDto.setLieferantIIdRechnungsadresse(
									rahmenBestellungDto.getLieferantIIdRechnungsadresse());

							// Default Lieferadresse ist die Adresse des
							// aktuellen
							// Mandanten
							Integer iPartnerIIdLIeferadresse = getMandantFac()
									.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
									.getPartnerIIdLieferadresse();

							if (partnerIIdStandort != null) {
								iPartnerIIdLIeferadresse = partnerIIdStandort;
							}

							// SP9034 Lieferadresse kommt aus Rahmen, wenn im Rahmen vorhanden
							if (rahmenBestellungDto.getPartnerIIdLieferadresse() != null) {
								iPartnerIIdLIeferadresse = rahmenBestellungDto.getPartnerIIdLieferadresse();
							}

							abrufBestellungDto.setPartnerIIdLieferadresse(iPartnerIIdLIeferadresse);

							abrufBestellungDto.setSpediteurIId(rahmenBestellungDto.getSpediteurIId());
							abrufBestellungDto.setMandantCNr(mandantDto.getCNr());
							abrufBestellungDto.setLieferartIId(rahmenBestellungDto.getLieferartIId());
							abrufBestellungDto.setWaehrungCNr(rahmenBestellungDto.getWaehrungCNr());
							abrufBestellungDto
									.setFAllgemeinerRabattsatz(rahmenBestellungDto.getFAllgemeinerRabattsatz());
							Integer iAbrufBEstellungIId = getBestellungFac().createBestellung(abrufBestellungDto,
									theClientDto);
							abrufBestellungDto = getBestellungFac().bestellungFindByPrimaryKey(iAbrufBEstellungIId);
							hmAbrufeZuRahmen.put(besPosDto.getBelegIId(), abrufBestellungDto.getIId());
						}
						// Bestellung wurde nun angelgt oder war bereits
						// vorhanden
						// Jetzt die Position anlegen oder updaten
						BestellpositionDto[] vorhandeneAbrufe = getBestellpositionFac()
								.bestellpositionFindByBestellung(abrufBestellungDto.getIId(), theClientDto);
						BestellpositionDto abrufBestellpositionDto = null;
						for (int x = 0; x < vorhandeneAbrufe.length; x++) {
							if (bestellvorschlagDto[i].getIArtikelId().equals(vorhandeneAbrufe[x].getArtikelIId())) {
								abrufBestellpositionDto = vorhandeneAbrufe[x];
							}
						}
						BigDecimal bdbenoetigteMenge = bestellvorschlagDto[i].getNZubestellendeMenge()
								.subtract(bdBereitsBestellteMenge);
						BigDecimal bdAbrufmenge;
						if (bdbenoetigteMenge.compareTo(besPosDto.getNOffeneMenge()) > 0) {
							if (y == alVerwendeteBesPosIId.size() - 1) {
								// Letzte Pos, also wird ueberliefert
								bdAbrufmenge = bdbenoetigteMenge;
								if (!alUeberlieferteBestellungIId.contains(besPosDto.getBelegIId())) {
									alUeberlieferteBestellungIId.add(besPosDto.getBelegIId());
								}
							} else {
								bdAbrufmenge = besPosDto.getNOffeneMenge();
							}
						} else {
							bdAbrufmenge = bdbenoetigteMenge;
						}
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(bestellvorschlagDto[i].getIArtikelId(), theClientDto);
						if (abrufBestellpositionDto == null) {
							abrufBestellpositionDto = new BestellpositionDto();
							abrufBestellpositionDto.setNMenge(bdAbrufmenge);
							bdbenoetigteMenge = bdbenoetigteMenge.subtract(bdAbrufmenge);

							abrufBestellpositionDto.setIBestellpositionIIdRahmenposition(besPosDto.getIId());
							abrufBestellpositionDto.setArtikelIId(bestellvorschlagDto[i].getIArtikelId());
							abrufBestellpositionDto.setBDrucken(Helper.boolean2Short(false));
							abrufBestellpositionDto.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
							abrufBestellpositionDto
									.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
							abrufBestellpositionDto.setBestellungIId(abrufBestellungDto.getIId());
							abrufBestellpositionDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
							// bestellpositionDto.setISort(new Integer(j++));
							abrufBestellpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
							abrufBestellpositionDto.setNNettoeinzelpreis(besPosDto.getNNettoeinzelpreis());
							abrufBestellpositionDto.setNNettogesamtpreis(besPosDto.getNNettogesamtpreis());
							abrufBestellpositionDto.setNRabattbetrag(besPosDto.getNRabattbetrag());
							abrufBestellpositionDto.setDRabattsatz(besPosDto.getDRabattsatz());
							abrufBestellpositionDto.setNMaterialzuschlag(besPosDto.getNMaterialzuschlag());
							abrufBestellpositionDto.setBNettopreisuebersteuert(besPosDto.getBNettopreisuebersteuert());
							abrufBestellpositionDto
									.setTUebersteuerterLiefertermin(bestellvorschlagDto[i].getTLiefertermin());
							getBestellpositionFac().createAbrufbestellposition(abrufBestellpositionDto,
									BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null,
									theClientDto);
						} else {
							BigDecimal bdMenge = bdAbrufmenge.add(abrufBestellpositionDto.getNMenge());
							abrufBestellpositionDto.setNMenge(bdMenge);
							bdbenoetigteMenge = bdbenoetigteMenge.subtract(bdMenge);
							getBestellpositionFac().updateAbrufbestellposition(abrufBestellpositionDto,
									BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null,
									theClientDto);

						}

						tmBetroffeneArtikel.put(artikelDto.getCNr(), artikelDto);

						bUebergeleitet = true;
					}
					if (bUebergeleitet) {
						this.removeBestellvorschlag(bestellvorschlagDto[i]);
					}
				}
			}
			ArrayList<BestellungDto> toReturn = new ArrayList<BestellungDto>();
			for (int x = 0; x < alUeberlieferteBestellungIId.size(); x++) {
				toReturn.add(getBestellungFac().bestellungFindByPrimaryKey(alUeberlieferteBestellungIId.get(x)));
			}
			return new RueckgabeUeberleitungDto(true, tmBetroffeneArtikel, toReturn);
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		} finally {
			session.close();
		}
	}

	/**
	 * hier wird Bestellung und Bestellpositionen befuellt fuer Ueberleitung aus dem
	 * Bestellvorschlag
	 * 
	 * @param aBestellvorschlagDto BestellvorschlagDto[]
	 * @param kostenstelleIId      kostenstelleIId
	 * @param theClientDto         String
	 * @throws EJBExceptionLP
	 */
	@org.jboss.ejb3.annotation.TransactionTimeout(1000)
	public java.util.TreeMap befuellenDerBestellungUndBestellposition(BestellvorschlagDto[] aBestellvorschlagDtoI,
			Integer kostenstelleIId, Integer partnerIIdStandort, boolean bRahmenbestellungErzeugen,
			TheClientDto theClientDto) throws EJBExceptionLP {

		java.util.TreeMap tmBetroffeneArtikel = new java.util.TreeMap();

		// Nach Terminen trennen

		TreeMap<Timestamp, ArrayList<BestellvorschlagDto>> hmGetrenntNachLieferterminen = new TreeMap<Timestamp, ArrayList<BestellvorschlagDto>>();

		for (int i = 0; i < aBestellvorschlagDtoI.length; i++) {

			Timestamp tLiefertermin = aBestellvorschlagDtoI[i].getTLiefertermin();

			ArrayList<BestellvorschlagDto> alBV = new ArrayList<BestellvorschlagDto>();

			if (hmGetrenntNachLieferterminen.containsKey(tLiefertermin)) {
				alBV = hmGetrenntNachLieferterminen.get(tLiefertermin);
			} else {
				alBV = new ArrayList<BestellvorschlagDto>();
			}

			alBV.add(aBestellvorschlagDtoI[i]);
			hmGetrenntNachLieferterminen.put(tLiefertermin, alBV);
		}

		Iterator itProjekte = hmGetrenntNachLieferterminen.keySet().iterator();
		while (itProjekte.hasNext()) {

			ArrayList al = hmGetrenntNachLieferterminen.get(itProjekte.next());
			BestellvorschlagDto[] aBestellvorschlagDto = (BestellvorschlagDto[]) al
					.toArray(new BestellvorschlagDto[al.size()]);

			try {
				// isort in Bestellposition
				int j = 0;
				boolean bBestellvorschlagErzeugtLoszuordnung = true;

				ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_BESTELLUNG_AUS_BESTVOR_POSITIONEN_MIT_LOSZUORDNUNG);
				bBestellvorschlagErzeugtLoszuordnung = (Boolean) parameterDto.getCWertAsObject();
				MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(),
						theClientDto);

				boolean bAnsprechpartnerVorbesetzen = false;
				ParametermandantDto param = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_BESTELLUNG_ANSP_VORBESETZEN);
				bAnsprechpartnerVorbesetzen = (Boolean) param.getCWertAsObject();

				int iMaximaleAnzahlBestellpositionen = 250;
				ParametermandantDto paramPos = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_BESTELLUNG,
						ParameterFac.PARAMETER_BV_UEBERLEITUNG_MAXIMALE_ANZAHL_POSITIONEN);
				iMaximaleAnzahlBestellpositionen = (Integer) paramPos.getCWertAsObject();

				int iZahlerBestPos = 0;
				BestellungDto bestellungDto = null;
				for (int i = 0; i < aBestellvorschlagDto.length; i++) {
					iZahlerBestPos++;
					// anlegen der Bestellung
					LieferantDto lieferantDto = getLieferantFac()
							.lieferantFindByPrimaryKey(aBestellvorschlagDto[0].getILieferantId(), theClientDto);
					BigDecimal wechselkurs = getLocaleFac().getWechselkurs2(lieferantDto.getWaehrungCNr(),
							mandantDto.getWaehrungCNr(), theClientDto);

					// Pro Lieferant und Termin eine eigene Bestellung anlegen

					if (iZahlerBestPos > iMaximaleAnzahlBestellpositionen) {
						bestellungDto = null;
						j = 0;
						iZahlerBestPos = 1;
					}

					if (bestellungDto == null) {
						bestellungDto = new BestellungDto();

						// SP3131
						if (bAnsprechpartnerVorbesetzen == true) {
							AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
									.ansprechpartnerFindErstenEinesPartnersOhneExc(lieferantDto.getPartnerIId(),
											theClientDto);
							if (anspDto != null) {
								bestellungDto.setAnsprechpartnerIId(anspDto.getIId());
							}
						}

						PersonalDto anfordererDto = getPersonalFac()
								.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
						bestellungDto.setPersonalIIdAnforderer(anfordererDto.getIId());

						bestellungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
						bestellungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
						bestellungDto.setZahlungszielIId(lieferantDto.getZahlungszielIId());
						bestellungDto.setProjektIId(aBestellvorschlagDto[0].getProjektIId());
						bestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
						// bestellungDto.setBestelltextIIdFusstext();
						// bestellungDto.setBestelltextIIdKopftext();
						if (bRahmenbestellungErzeugen) {
							bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR);
						} else {
							bestellungDto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
						}

						bestellungDto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);

						parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_BESTELLUNG, ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG);

						bestellungDto.setBTeillieferungMoeglich(
								Helper.boolean2Short((Boolean) parameterDto.getCWertAsObject()));
						bestellungDto.setDBelegdatum(this.getDate());
						bestellungDto.setDLiefertermin(aBestellvorschlagDto[0].getTLiefertermin());
						bestellungDto.setKostenstelleIId(kostenstelleIId);
						bestellungDto.setLieferantIIdBestelladresse(lieferantDto.getIId());
						bestellungDto.setLieferantIIdRechnungsadresse(lieferantDto.getIId());

						// Default Lieferadresse ist die Adresse des aktuellen
						// Mandanten
						Integer iPartnerIIdLIeferadresse = getMandantFac()
								.mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto)
								.getPartnerIIdLieferadresse();

						if (partnerIIdStandort != null) {
							iPartnerIIdLIeferadresse = partnerIIdStandort;
						}

						// PJ21786 hat hoechste Prio
						if (lieferantDto.getPartnerIIdLieferadresse() != null) {
							iPartnerIIdLIeferadresse = lieferantDto.getPartnerIIdLieferadresse();
						}

						bestellungDto.setPartnerIIdLieferadresse(iPartnerIIdLIeferadresse);

						bestellungDto.setSpediteurIId(lieferantDto.getIdSpediteur());
						bestellungDto.setMandantCNr(mandantDto.getCNr());
						bestellungDto.setLieferartIId(lieferantDto.getLieferartIId());
						bestellungDto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
						bestellungDto.setFAllgemeinerRabattsatz(new Double(0));

						bestellungDto
								.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(wechselkurs.doubleValue()));

						// PJ21315 Punkt i
						if (aBestellvorschlagDto[i].getCBelegartCNr() != null
								&& aBestellvorschlagDto[i].getCBelegartCNr().equals(LocaleFac.BELEGART_EINKAUFSANGEBOT)
								&& aBestellvorschlagDto[i].getIBelegartId() != null) {
							EinkaufsangebotDto ekagDto = getAngebotstklFac()
									.einkaufsangebotFindByPrimaryKey(aBestellvorschlagDto[i].getIBelegartId());

							bestellungDto.setCBez(ekagDto.getCProjekt());

						}

						bestellungDto.setIId(getBestellungFac().createBestellung(bestellungDto, theClientDto));
					}

					// Positionen anlegen.
					BestellpositionDto bestellpositionDto = new BestellpositionDto();

					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKey(aBestellvorschlagDto[i].getIArtikelId(), theClientDto);

					bestellpositionDto.setArtikelIId(aBestellvorschlagDto[i].getIArtikelId());
					bestellpositionDto.setBDrucken(Helper.boolean2Short(false));
					bestellpositionDto.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
					bestellpositionDto.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
					bestellpositionDto.setBestellungIId(bestellungDto.getIId());
					bestellpositionDto.setCBez(artikelDto.getArtikelsprDto().getCBez());
					bestellpositionDto.setISort(new Integer(j++));
					bestellpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
					bestellpositionDto.setNMenge(aBestellvorschlagDto[i].getNZubestellendeMenge());
					bestellpositionDto.setXTextinhalt(aBestellvorschlagDto[i].getXTextinhalt());

					if (bBestellvorschlagErzeugtLoszuordnung == true) {

						if (aBestellvorschlagDto[i].getCBelegartCNr() != null
								&& aBestellvorschlagDto[i].getCBelegartCNr().equals(LocaleFac.BELEGART_LOS)
								&& aBestellvorschlagDto[i].getIBelegartpositionid() != null) {

							LossollmaterialDto lossollmaterialDto = getFertigungFac()
									.lossollmaterialFindByPrimaryKeyOhneExc(
											aBestellvorschlagDto[i].getIBelegartpositionid());
							if (lossollmaterialDto != null) {
								bestellpositionDto
										.setLossollmaterialIId(aBestellvorschlagDto[i].getIBelegartpositionid());
							}
						}
					}

					bestellpositionDto.setNMaterialzuschlag(new BigDecimal(0));

					if (artikelDto.getMaterialIId() != null) {
						BigDecimal zuschlag = getMaterialFac().getMaterialzuschlagEKInZielwaehrung(artikelDto.getIId(),
								lieferantDto.getIId(), this.getDate(), lieferantDto.getWaehrungCNr(), theClientDto);
						if (zuschlag != null) {
							bestellpositionDto.setNMaterialzuschlag(zuschlag);

							if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
								BigDecimal nettogesamtpreis = aBestellvorschlagDto[i].getNNettoeinzelpreis();
								if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
									nettogesamtpreis = nettogesamtpreis
											.subtract(aBestellvorschlagDto[i].getNRabattbetrag());
								}

								nettogesamtpreis = nettogesamtpreis.add(zuschlag);
								aBestellvorschlagDto[i].setNNettogesamtpreis(nettogesamtpreis);

							}

						}

					}

					if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
						bestellpositionDto.setNNettoeinzelpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								aBestellvorschlagDto[i].getNNettoeinzelpreis(), mandantDto.getWaehrungCNr(),
								lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
					} else {
						bestellpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
					}
					if (aBestellvorschlagDto[i].getNNettogesamtpreis() != null) {
						bestellpositionDto.setNNettogesamtpreis(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								aBestellvorschlagDto[i].getNNettogesamtpreis(), mandantDto.getWaehrungCNr(),
								lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
					} else {
						bestellpositionDto.setNNettogesamtpreis(new BigDecimal(0));
					}
					if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
						bestellpositionDto.setNRabattbetrag(getLocaleFac().rechneUmInAndereWaehrungZuDatum(
								aBestellvorschlagDto[i].getNRabattbetrag(), mandantDto.getWaehrungCNr(),
								lieferantDto.getWaehrungCNr(), new Date(System.currentTimeMillis()), theClientDto));
					} else {
						bestellpositionDto.setNRabattbetrag(new BigDecimal(0));
						aBestellvorschlagDto[i].setNRabattbetrag(new BigDecimal(0));
					}

					if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null
							&& !(aBestellvorschlagDto[i].getNNettoeinzelpreis().compareTo(new BigDecimal(0)) == 0)) {
						bestellpositionDto.setDRabattsatz(new Double(new BigDecimal(100)
								.subtract(Helper.calculateRatioInDecimal(aBestellvorschlagDto[i].getNRabattbetrag(),
										aBestellvorschlagDto[i].getNNettoeinzelpreis()))
								.doubleValue()));
					} else {
						bestellpositionDto.setDRabattsatz(new Double(0));
					}

					// SP2241
					ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
							aBestellvorschlagDto[i].getIArtikelId(), aBestellvorschlagDto[i].getILieferantId(),
							aBestellvorschlagDto[i].getNZubestellendeMenge(), theClientDto.getSMandantenwaehrung(),
							this.getDate(), theClientDto);

					if (alDto != null) {
						bestellpositionDto.setCAngebotnummer(alDto.getCAngebotnummer());
					}

					bestellpositionDto.setBNettopreisuebersteuert(aBestellvorschlagDto[i].getBNettopreisuebersteuert());

					bestellpositionDto.setTUebersteuerterLiefertermin(aBestellvorschlagDto[i].getTLiefertermin());

					bestellpositionDto.setGebindeIId(aBestellvorschlagDto[i].getGebindeIId());
					bestellpositionDto.setNAnzahlgebinde(aBestellvorschlagDto[i].getNAnzahlgebinde());

					getBestellpositionFac().createBestellposition(bestellpositionDto, theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT, null);
					tmBetroffeneArtikel.put(artikelDto.getCNr(), artikelDto);
					this.removeBestellvorschlag(aBestellvorschlagDto[i]);
				}
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
		}

		return tmBetroffeneArtikel;
	}

	public void removeLockDesBestellvorschlagesWennIchIhnSperre(TheClientDto theClientDto) throws EJBExceptionLP {
		try {

			String mandant = theClientDto.getMandant();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)
					&& !getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)) {
				mandant = getSystemFac().getHauptmandant();
			}

			LockMeDto[] lockMeDtoLock = getTheJudgeFac()
					.findByWerWasOhneExc(BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG, mandant);
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

	/**
	 * Pruefen, ob der Bestellvorschlag des Mandanten, auf dem der Benutzer
	 * arbeitet, freigegeben ist. falls nicht, wird eine Exception geworfen.
	 * 
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void pruefeBearbeitenDesBestellvorschlagsErlaubt(TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());

			String mandant = theClientDto.getMandant();

			if (getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM,
					theClientDto)
					&& !getMandantFac().darfAnwenderAufZusatzfunktionZugreifen(
							MandantFac.ZUSATZFUNKTION_GETRENNTE_LAGER, theClientDto)) {
				mandant = getSystemFac().getHauptmandant();
			}

			// PJ20810
			if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
				return;
			}

			lockMeDto.setCWas(mandant);
			lockMeDto.setCWer(BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac()
					.findByWerWasOhneExc(BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG, mandant);
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim().equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT, al,
							new Exception("Bestellvorschlag auf Mandant " + mandant + " gesperrt durch Personal Id "
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

	public void toggleBearbeitet(Integer bestellvorschlagIId, TheClientDto theClientDto) {

		Bestellvorschlag bv = em.find(Bestellvorschlag.class, bestellvorschlagIId);
		if (bv.getTBearbeitet() == null) {
			bv.setTBearbeitet(new Timestamp(System.currentTimeMillis()));
			bv.setPersonalIIdBearbeitet(theClientDto.getIDPersonal());

		} else {
			bv.setTBearbeitet(null);
			bv.setPersonalIIdBearbeitet(null);

		}

	}

	public void termineAnhandLiefertagVerschieben(TheClientDto theClientDto) {

		String sQuery = "select bestellvorschlag from FLRBestellvorschlag bestellvorschlag where bestellvorschlag. "
				+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'";
		Session session = FLRSessionFactory.getFactory().openSession();

		Integer tagesartIId_Feiertag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_FEIERTAG, theClientDto).getIId();
		Integer tagesartIId_Halbtag = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_HALBTAG, theClientDto).getIId();
		Integer tagesartIId_Betriebsurlaub = getZeiterfassungFac()
				.tagesartFindByCNr(ZeiterfassungFac.TAGESART_BETRIEBSURLAUB, theClientDto).getIId();

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> result = query.list();
		Iterator itBV = result.iterator();
		while (itBV.hasNext()) {
			FLRBestellvorschlag flrBestellvorschlag = (FLRBestellvorschlag) itBV.next();

			if (flrBestellvorschlag.getFlrartikel().getC_nr().equals("40432")) {
				int i = 0;
			}

			Timestamp tLiefertermin = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
					tagesartIId_Feiertag, tagesartIId_Halbtag, tagesartIId_Betriebsurlaub,
					new java.sql.Date(flrBestellvorschlag.getT_liefertermin().getTime()), 0);

			if (flrBestellvorschlag.getFlrlieferant() != null
					&& flrBestellvorschlag.getFlrlieferant().getI_liefertag() != null) {

				Calendar cAusliefertermin = Calendar.getInstance();
				cAusliefertermin.setTimeInMillis(tLiefertermin.getTime());
				boolean bVerschoben = false;
				while (cAusliefertermin.get(Calendar.DAY_OF_WEEK) != flrBestellvorschlag.getFlrlieferant()
						.getI_liefertag()) {
					cAusliefertermin.add(Calendar.DAY_OF_MONTH, -1);
					bVerschoben = true;
				}
				if (bVerschoben) {
					// Wenn dieser Tag wieder auf einen Feiertag faellt, dann noch einmal
					// vorverlegen

					tLiefertermin = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
							tagesartIId_Feiertag, tagesartIId_Halbtag, tagesartIId_Betriebsurlaub,
							new java.sql.Date(cAusliefertermin.getTimeInMillis()), 0);
				}
			}

			if (flrBestellvorschlag.getFlrlieferant() != null
					&& flrBestellvorschlag.getFlrlieferant().getI_liefertag() != null
					&& flrBestellvorschlag.getT_liefertermin().getTime() != tLiefertermin.getTime()) {

				if (tLiefertermin.before(getDate())) {

					// Wenn Zieldatum vor Heute, dann einmal noch den Liefertermin auf den Liefertag
					// in die Zukunft rechnen

					Calendar cAusliefertermin = Calendar.getInstance();
					cAusliefertermin.setTimeInMillis(getDate().getTime());

					while (cAusliefertermin.get(Calendar.DAY_OF_WEEK) != flrBestellvorschlag.getFlrlieferant()
							.getI_liefertag()) {
						cAusliefertermin.add(Calendar.DAY_OF_MONTH, +1);
					}
					tLiefertermin = Helper.cutTimestamp(new Timestamp(cAusliefertermin.getTimeInMillis()));

				}

			}

			Bestellvorschlag bv = em.find(Bestellvorschlag.class, flrBestellvorschlag.getI_id());
			bv.setTLiefertermin(tLiefertermin);
			em.merge(bv);
			em.flush();

		}

	}

	public void verdichteBestellvorschlagNachMindesbestellmengen(boolean bBeruecksichtigeProjektklammer,
			TheClientDto theClientDto) throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		try {

			String filterPersoenlicherBestellvorschlag = "";

			if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
				filterPersoenlicherBestellvorschlag = " AND bestellvorschlag.personal_i_id="
						+ theClientDto.getIDPersonal();
			}
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);

			boolean lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();

			session = factory.openSession();
			// Zuerst die ID's aller vorkommender Artikel holen.
			String queryString = "select distinct bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID
					+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'"
					+ filterPersoenlicherBestellvorschlag;

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			// nun die Eintraege fuer jeden Artikel verdichten.
			for (Iterator<?> itArtikel = rowCountResult.iterator(); itArtikel.hasNext();) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall((Integer) itArtikel.next(),
						theClientDto);

				ArrayList<Integer> alProjekte = new ArrayList<Integer>();

				if (bBeruecksichtigeProjektklammer == true) {
					String queryStringProjekte = "select distinct bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID
							+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
							+ artikelDto.getIId() + " AND bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant()
							+ "'" + filterPersoenlicherBestellvorschlag;
					org.hibernate.Query queryProjekte = session.createQuery(queryStringProjekte);
					List<?> resultProjekte = queryProjekte.list();
					Iterator itProjekte = resultProjekte.iterator();
					while (itProjekte.hasNext()) {
						Integer projektIId = (Integer) itProjekte.next();
						alProjekte.add(projektIId);
					}
				} else {
					alProjekte.add(null);
				}

				ArrayList<Integer> alStandorte = new ArrayList<Integer>();
				if (lagerminJeLager == true) {
					String queryStringProjekte = "select distinct flrpartner_standort.i_id from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
							+ artikelDto.getIId() + " AND bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant()
							+ "'" + filterPersoenlicherBestellvorschlag;
					org.hibernate.Query queryProjekte = session.createQuery(queryStringProjekte);
					List<?> resultStandorte = queryProjekte.list();
					Iterator itStandorte = resultStandorte.iterator();
					while (itStandorte.hasNext()) {
						Integer partnerIIdStandort = (Integer) itStandorte.next();
						alStandorte.add(partnerIIdStandort);
					}
				} else {
					alStandorte.add(null);
				}

				for (int j = 0; j < alProjekte.size(); j++) {
					Integer projektIId = alProjekte.get(j);

					for (int k = 0; k < alStandorte.size(); k++) {
						Integer partnerIIdStandort = alStandorte.get(k);
						// Alle BV-Eintraege des Mandanten auflisten.
						Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
						// Filter nach Mandant
						crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
								theClientDto.getMandant()));
						// Filter nach Artikel
						crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID,
								artikelDto.getIId()));

						if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
							crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
									theClientDto.getIDPersonal()));
						}

						if (bBeruecksichtigeProjektklammer == true) {
							if (projektIId == null) {
								crit.add(Restrictions.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID));
							} else {
								crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID,
										projektIId));
							}
						}

						if (lagerminJeLager == true) {
							if (partnerIIdStandort == null) {
								crit.add(Restrictions
										.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PARTNER_I_ID_STANDORT));
							} else {
								crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PARTNER_I_ID_STANDORT,
										partnerIIdStandort));
							}
						}

						// Sortierung nach Liefertermin.
						crit.addOrder(Order.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN));
						List<?> resultList = crit.list();
						// Da die Eintraege zwischendurch auf Mindest- bzw.
						// Verpackungsmengen
						// aufgerundet werden muessen, entwickelt sich
						// zwischendurch
						// ein
						// Guthaben
						BigDecimal bdGuthaben = new BigDecimal(0);
						ArtikellieferantDto artLief = null;
						// nun alle Eintraege durchiterieren.
						for (Iterator<?> itBV = resultList.iterator(); itBV.hasNext();) {
							FLRBestellvorschlag bv = (FLRBestellvorschlag) itBV.next();
							Bestellvorschlag bvEntity = em.find(Bestellvorschlag.class, bv.getI_id());
							if (bvEntity == null) {
								throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
							}
							if (bv.getArtikel_i_id() != null && bv.getLieferant_i_id() != null) {
								if (artLief == null) { // lazy loading fuer
														// jeden
									// Artikel.
									artLief = getArtikelFac().getArtikelEinkaufspreis(bv.getArtikel_i_id(),
											bv.getLieferant_i_id(), BigDecimal.ONE,
											theClientDto.getSMandantenwaehrung(),
											new java.sql.Date(System.currentTimeMillis()), theClientDto);
								}
								// SP3573
								if (artLief == null) {
									artLief = getArtikelFac()
											.artikellieferantFindByArtikellIIdLieferantIIdTPreisgueltigabKleiner(
													bv.getArtikel_i_id(), bv.getLieferant_i_id(),
													new java.sql.Date(System.currentTimeMillis()), null, theClientDto);
								}

								// Wenn der Artikellieferant definiert ist (von
								// dort
								// kommen VPE und Mindestbestellmenge)
								if (artLief != null) {
									if (bdGuthaben.compareTo(bv.getN_zubestellendemenge()) >= 0) {
										// Wenn durch Mengenerhoehungen
										// vorheriger
										// Eintraege genug Guthaben
										// "erzielt" wurde, um auf diese
										// Position
										// verzichten zu koennen -> loeschen.
										bdGuthaben = bdGuthaben.subtract(bv.getN_zubestellendemenge());
										removeBestellvorschlag(bv.getI_id());
									} else {

										BigDecimal bdMindestbestellmenge = new BigDecimal(0);

										if (artLief.getFMindestbestelmenge() != null) {
											// Ist keine Mindestbestellmenge
											// definiert,
											// kann ich deren Behandlung als
											// abgeschlossen betrachten.
											bdMindestbestellmenge = new BigDecimal(artLief.getFMindestbestelmenge());
										}

										// Wenn die zu bestellende Menge
										// geringer
										// als die Mindestbestellmenge ist,
										// dann muss ich auf die
										// Mindestbestellmenge
										// erhoehen.
										if (bv.getN_zubestellendemenge().compareTo(bdMindestbestellmenge) < 0) {
											BigDecimal bdDifferenz = bdMindestbestellmenge
													.subtract(bv.getN_zubestellendemenge());
											bv.setN_zubestellendemenge(bdMindestbestellmenge);
											bdGuthaben = bdGuthaben.add(bdDifferenz);
											// speichern
											bvEntity.setNZubestellendemenge(bdMindestbestellmenge);
										}

										// Zuletzt muss noch auf die
										// Verpackungseinheit
										// geprueft werden. (sofern diese
										// definiert
										// ist)
										if (artLief.getNVerpackungseinheit() != null) {
											if (artLief.getNVerpackungseinheit().doubleValue() != 0) {
												// Die zu bestellende Menge muss
												// immer
												// ein Vielfaches der VPE sein.
												int iNachkommastellenMandant = getMandantFac()
														.getNachkommastellenMenge(theClientDto.getMandant());
												BigDecimal bdVPE = artLief.getNVerpackungseinheit();
												bdVPE = bdVPE.setScale(iNachkommastellenMandant, RoundingMode.HALF_UP);
												BigDecimal bdMenge = bv.getN_zubestellendemenge()
														.setScale(iNachkommastellenMandant, RoundingMode.HALF_UP);
												BigDecimal bdDivRest = bdMenge.divideAndRemainder(bdVPE)[1]
														.setScale(iNachkommastellenMandant, RoundingMode.HALF_UP);
												if (bdDivRest.compareTo(
														new BigDecimal(0).setScale(iNachkommastellenMandant)) != 0) {
													BigDecimal bdDifferenz = bdVPE.subtract(bdDivRest);
													bdGuthaben = bdGuthaben.add(bdDifferenz);
													// speichern
													bvEntity.setNZubestellendemenge(
															bvEntity.getNZubestellendemenge().add(bdDifferenz));
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	/**
	 * BV-Eintraege eines Mandante verdichten. (Einrechnen der Verpackungseinheiten
	 * und Mindestbestellmengen)
	 * 
	 * 
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void verdichteBestellvorschlag(Long lVerdichtungszeitraum, boolean bMindestbestellmengenBeruecksichtigt,
			boolean bBeruecksichtigeProjektklammer, boolean bPreiseaktualisieren, TheClientDto theClientDto)
			throws EJBExceptionLP {
		getBestellvorschlagFac().verdichteBestellvorschlagNachDatum(lVerdichtungszeitraum,
				bBeruecksichtigeProjektklammer, theClientDto);
		if (bMindestbestellmengenBeruecksichtigt) {
			getBestellvorschlagFac().verdichteBestellvorschlagNachMindesbestellmengen(bBeruecksichtigeProjektklammer,
					theClientDto);
		}
		// PJ21304
		getBestellvorschlagFac().termineAnhandLiefertagVerschieben(theClientDto);

		// SP8469
		if (bPreiseaktualisieren) {
			getBestellvorschlagFac().aktualisierePreiseWbz(theClientDto);
		}

	}

	public void aktualisierePreiseWbz(TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		session = factory.openSession();
		// Zuerst die ID's aller vorkommender Artikel holen.
		String queryString = "select bestellvorschlag from FLRBestellvorschlag bestellvorschlag where bestellvorschlag."
				+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'";
		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
			queryString += " AND bestellvorschlag.personal_i_id=" + theClientDto.getIDPersonal();
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> rowCountResult = query.list();
		// nun die Eintraege fuer jeden Artikel verdichten.
		for (Iterator<?> itBestellvorschlag = rowCountResult.iterator(); itBestellvorschlag.hasNext();) {

			FLRBestellvorschlag flrBestellvorschlag = (FLRBestellvorschlag) itBestellvorschlag.next();

			BestellvorschlagDto bvDto = bestellvorschlagFindByPrimaryKey(flrBestellvorschlag.getI_id());

			BestellvorschlagDto bvDtoModified = setupBestellvorschlagDto(bvDto.getCBelegartCNr(),
					bvDto.getCMandantCNr(), bvDto.getIArtikelId(), bvDto.getIBelegartId(),
					bvDto.getIBelegartpositionid(), bvDto.getTLiefertermin(), bvDto.getNZubestellendeMenge(),
					bvDto.getProjektIId(), bvDto.getXTextinhalt(), bvDto.getILieferantId(), null, null,
					bvDto.getPartnerIIdStandort(), bZentralerArtikelstamm, theClientDto);

			bvDtoModified.setIId(bvDto.getIId());

			updateBestellvorschlag(bvDtoModified, theClientDto);

		}

	}

	public void verdichteBestellvorschlagNachDatum(Long lVerdichtungszeitraumparam,
			boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		boolean lagerminJeLager = false;

		try {
			ParametermandantDto parameterDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_LAGERMIN_JE_LAGER);
			lagerminJeLager = (Boolean) parameterDto.getCWertAsObject();
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		session = factory.openSession();
		// Zuerst die ID's aller vorkommender Artikel holen.
		String queryString = "select distinct bestellvorschlag." + BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID
				+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag."
				+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'";
		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
			queryString += " AND bestellvorschlag.personal_i_id=" + theClientDto.getIDPersonal();
		}

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> rowCountResult = query.list();
		// nun die Eintraege fuer jeden Artikel verdichten.
		for (Iterator<?> itArtikel = rowCountResult.iterator(); itArtikel.hasNext();) {
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall((Integer) itArtikel.next(),
					theClientDto);

			getBestellvorschlagFac().verdichteEinenArtikel(lVerdichtungszeitraumparam, bBeruecksichtigeProjektklammer,
					theClientDto, lagerminJeLager, artikelDto);
		}

	}

	public void verdichteEinenArtikel(Long lVerdichtungszeitraumparam, boolean bBeruecksichtigeProjektklammer,
			TheClientDto theClientDto, boolean lagerminJeLager, ArtikelDto artikelDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		ArrayList<Integer> alProjekte = new ArrayList<Integer>();

		String filterPersoenlicherBestellvorschlag = "";

		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
			filterPersoenlicherBestellvorschlag = " AND bestellvorschlag.personal_i_id=" + theClientDto.getIDPersonal();
		}

		if (bBeruecksichtigeProjektklammer == true) {
			String queryStringProjekte = "select distinct bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID
					+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
					+ artikelDto.getIId() + " AND bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'"
					+ filterPersoenlicherBestellvorschlag;
			org.hibernate.Query queryProjekte = session.createQuery(queryStringProjekte);
			List<?> resultProjekte = queryProjekte.list();
			Iterator itProjekte = resultProjekte.iterator();
			while (itProjekte.hasNext()) {
				Integer projektIId = (Integer) itProjekte.next();
				alProjekte.add(projektIId);
			}
		} else {
			alProjekte.add(null);
		}

		ArrayList<Integer> alStandorte = new ArrayList<Integer>();
		if (lagerminJeLager == true) {
			String queryStringProjekte = "select distinct flrpartner_standort.i_id from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
					+ artikelDto.getIId() + " AND bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='" + theClientDto.getMandant() + "'"
					+ filterPersoenlicherBestellvorschlag;
			org.hibernate.Query queryProjekte = session.createQuery(queryStringProjekte);
			List<?> resultStandorte = queryProjekte.list();
			Iterator itStandorte = resultStandorte.iterator();
			while (itStandorte.hasNext()) {
				Integer partnerIIdStandort = (Integer) itStandorte.next();
				alStandorte.add(partnerIIdStandort);
			}
		} else {
			alStandorte.add(null);
		}

		for (int j = 0; j < alProjekte.size(); j++) {
			Integer projektIId = alProjekte.get(j);

			for (int k = 0; k < alStandorte.size(); k++) {
				Integer partnerIIdStandort = alStandorte.get(k);
				// Alle BV-Eintraege des Mandanten auflisten.
				Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
				// Filter nach Mandant
				crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
						theClientDto.getMandant()));
				// Filter nach Artikel
				crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID, artikelDto.getIId()));

				if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
					crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
							theClientDto.getIDPersonal()));
				}

				if (bBeruecksichtigeProjektklammer == true) {
					if (projektIId == null) {
						crit.add(Restrictions.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID));
					} else {
						crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID, projektIId));
					}
				}

				if (lagerminJeLager == true) {
					if (partnerIIdStandort == null) {
						crit.add(Restrictions.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PARTNER_I_ID_STANDORT));
					} else {
						crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PARTNER_I_ID_STANDORT,
								partnerIIdStandort));
					}
				}

				// Sortierung nach Liefertermin.
				crit.addOrder(Order.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN));
				List<?> resultList = crit.list();
				FLRBestellvorschlag[] bestVorschlaege = (FLRBestellvorschlag[]) resultList
						.toArray(new FLRBestellvorschlag[] {});
				// der erste Bestellvorschlag ist der frueheste
				Bestellvorschlag bvFirst = em.find(Bestellvorschlag.class, bestVorschlaege[0].getI_id());
				if (bvFirst == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
				}

				// Alle Bestellvorschlaege dieses Artikels,
				// die vor dem Datum sind muessen auf diesem gesammelt
				// werden
				Timestamp dateOfVerdichtung = Helper.addiereTageZuTimestamp(bvFirst.getTLiefertermin(),
						lVerdichtungszeitraumparam.intValue());
				for (int i = 1; i < bestVorschlaege.length; i++) {
					if (bestVorschlaege[i].getT_liefertermin().before(dateOfVerdichtung)
							|| bestVorschlaege[i].getT_liefertermin().compareTo(dateOfVerdichtung) == 0) {
						// Bestellvorschlag wird auf vorherigen verdichtet
						bvFirst.setNZubestellendemenge(
								bvFirst.getNZubestellendemenge().add(bestVorschlaege[i].getN_zubestellendemenge()));

						// Der Beleg wird geloescht, da das nicht mehr
						// stimmen
						// kann
						bvFirst.setBelegartCNr(null);
						bvFirst.setIBelegartid(null);
						bvFirst.setIBelegartpositionid(null);

						if (bBeruecksichtigeProjektklammer == false) {
							bvFirst.setProjektIId(null);
						}

						// Der Bestellvorschlag der verdichtet wurde kann
						// geloescht werden
						removeBestellvorschlag(bestVorschlaege[i].getI_id());
					} else {
						// Der Liefertermin wird die neue Referenz
						// fuer die naechsten Vorschlaege
						bvFirst = em.find(Bestellvorschlag.class, bestVorschlaege[i].getI_id());
						dateOfVerdichtung = Helper.addiereTageZuTimestamp(bvFirst.getTLiefertermin(),
								lVerdichtungszeitraumparam.intValue());
					}
				}
			}
		}
	}

	public void uebernimmLieferantAusLieferantOptimieren(Integer bestellvorschlagIId, Integer lieferantIIdNeu,
			TheClientDto theClientDto) {
		BestellvorschlagDto bestellvorschlagDto = bestellvorschlagFindByPrimaryKey(bestellvorschlagIId);

		bestellvorschlagDto.setILieferantId(lieferantIIdNeu);

		ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(bestellvorschlagDto.getIArtikelId(),
				lieferantIIdNeu, bestellvorschlagDto.getNZubestellendeMenge(), theClientDto.getSMandantenwaehrung(),
				Helper.cutDate(new java.sql.Date(System.currentTimeMillis())), theClientDto);

		if (alDto != null) {

			bestellvorschlagDto.setNNettoeinzelpreis(alDto.getNEinzelpreis());
			bestellvorschlagDto.setNNettogesamtpreis(alDto.getNNettopreis());
			bestellvorschlagDto.setIWiederbeschaffungszeit(alDto.getIWiederbeschaffungszeit());

			bestellvorschlagDto.setDRabattsatz(alDto.getFRabatt());
			if (alDto.getNEinzelpreis() != null && alDto.getFRabatt() != null) {
				bestellvorschlagDto.setNRabattbetrag(Helper.rundeKaufmaennisch(
						alDto.getNEinzelpreis().multiply(new BigDecimal(alDto.getFRabatt()).movePointLeft(2)), 4));
			}
			if (Helper.short2boolean(alDto.getBRabattbehalten())) {
				bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(false));
			} else {
				bestellvorschlagDto.setBNettopreisuebersteuert(Helper.boolean2Short(true));
			}

			bestellvorschlagDto.setNNettoGesamtPreisMinusRabatte(alDto.getNNettopreis());
		}

		updateBestellvorschlag(bestellvorschlagDto, theClientDto);
	}

	public Map getAllLieferantenDesBestellvorschlages(TheClientDto theClientDto) {

		LinkedHashMap<Object, Object> tmLieferanten = new LinkedHashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct bv.lieferant_i_id,bv.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 FROM FLRBestellvorschlag bv WHERE bv.mandant_c_nr='"
				+ theClientDto.getMandant() + "' ORDER BY bv.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer lieferantIId = (Integer) o[0];
			LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(lieferantIId, theClientDto);
			tmLieferanten.put(lieferantIId, lieferantDto.getPartnerDto().formatFixName1Name2());

		}

		return tmLieferanten;
	}

	/**
	 * Spaeter wieder beschaffbare Positionen aus dem BV entfernen. "Spaeter
	 * Wiederbeschaffbar" heisst: Das Datum des naechsten geplanten
	 * Bestellvorschlags + Wiederbeschaffungszeit liegt vor dem Datum, an dem die
	 * Artikel benoetigt werden.
	 * 
	 * @param tNaechsterBV Date
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void loescheSpaeterWiederbeschaffbarePositionen(java.sql.Date tNaechsterBV, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// Precondition
		if (tNaechsterBV == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("tWBMoeglichBis == null"));
		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			// Alle BV-Eintraege des Mandanten auflisten.
			Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
			// Filter nach Mandant
			crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR, theClientDto.getMandant()));

			// PJ20810
			if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
				crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
						theClientDto.getIDPersonal()));
			}

			List<?> resultList = crit.list();
			// Wiederbeschaffungszeit kann in Tagen oder Wochen angegeben
			// werden.
			ParametermandantDto parameterDtoWBZ = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
			final int iFaktorWBZ;
			if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				iFaktorWBZ = 7;
			} else if (parameterDtoWBZ.getCWert().equals(ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				iFaktorWBZ = 1;
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT, new Exception(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT + " ist nicht richtig definiert"));
			}
			for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
				FLRBestellvorschlag item = (FLRBestellvorschlag) iter.next();
				if (item.getI_wiederbeschaffungszeit() != null) {

					// Wenn der Artikellieferant definiert ist (von dort kommt
					// die Wiederbeschaffungszeit)
					// und wenn auch eine WBZ definiert ist.

					// Wiederbeschaffungszeit in Tagen
					int iWBZInTagen = iFaktorWBZ * item.getI_wiederbeschaffungszeit();
					java.sql.Date tMussBestellenBis = Helper.addiereTageZuDatum(item.getT_liefertermin(), -iWBZInTagen);
					// Wenn ich den also beim naechsten BV auch noch
					// bestellen kann, dann loesch ich ihn raus
					if (tNaechsterBV.before(tMussBestellenBis)) {
						removeBestellvorschlag(item.getI_id());
					}

				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	public void loescheBestellvorschlaegeAbTermin(Date dTermin, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		session = factory.openSession();
		// Alle BV-Eintraege des Mandanten auflisten.
		Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
		// Filter nach Mandant
		crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR, theClientDto.getMandant()));

		// PJ20810
		if (getParameterFac().getPersoenlicherBestellvorschlag(theClientDto.getMandant())) {
			crit.add(Restrictions.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PERSONAL_I_ID,
					theClientDto.getIDPersonal()));
		}

		List<?> resultList = crit.list();
		for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
			FLRBestellvorschlag item = (FLRBestellvorschlag) iter.next();
			if (item.getT_liefertermin().after(dTermin)) {
				removeBestellvorschlag(item.getI_id());
			}
		}
		closeSession(session);
	}

	@Override
	public Integer setupCreateBestellvorschlag(CreateBestellvorschlagDto createDto, TheClientDto theClientDto) {
		Validator.notNull(createDto, "createBestellvorschlagDto");
		Validator.notNull(createDto.getArtikelId(), "artikelId");
		Validator.notNull(createDto.getMenge(), "menge");

		Timestamp liefertermin = createDto.getLiefertermin() != null ? createDto.getLiefertermin() : getTimestamp();
		BestellvorschlagDto bestellvorschlagDto = setupBestellvorschlagDto(null, theClientDto.getMandant(),
				createDto.getArtikelId().id(), null, null, liefertermin, createDto.getMenge(), null, null, null, null, null,
				null, getMandantFac().hatZusatzfunktionZentralerArtikelstamm(theClientDto), theClientDto);
		bestellvorschlagDto.setBVormerkung(Helper.boolean2Short(createDto.isVormerkung()));

		return createBestellvorschlag(bestellvorschlagDto, theClientDto);
	}

	@Override
	public List<BestellvorschlagDto> bestellvorschlagFindByArtikelIdVormerkungMandantCNr(ArtikelId artikelId,
			String mandantCNr) {
		Validator.notNull(artikelId, "artikelId");
		Validator.notEmpty(mandantCNr, "mandantCNr");

		List<Bestellvorschlag> entities = BestellvorschlagQuery.listByArtikelIIdVormerkungMandantCnr(em, artikelId,
				mandantCNr);
		return Arrays.asList(assembleBestellvorschlagDtos(entities));
	}

	@Override
	public BestellvorschlagDto bestellvorschlagFindByPrimaryKeyOhneExc(Integer iId) {
		Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class, iId);
		return bestellvorschlag != null ? assembleBestellvorschlagDto(bestellvorschlag) : null;
	}

	private String getStringAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten, String feldname,
			int iLaenge, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getContents().length() > iLaenge) {

						fehlerZeileXLSImport += feldname + " ist zu lang (>" + iLaenge + ") Zeile " + iZeile
								+ new String(CRLFAscii);

					}

					return c.getContents();
				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	private BigDecimal getBigDecimalAusXLS(jxl.Cell[] zeilen, HashMap<String, Integer> hmVorhandeneSpalten,
			String feldname, int iZeile) {
		byte[] CRLFAscii = { 13, 10 };

		if (hmVorhandeneSpalten.containsKey(feldname)) {

			Integer iSpalte = hmVorhandeneSpalten.get(feldname);

			if (iSpalte != null && zeilen.length > iSpalte) {

				Cell c = zeilen[iSpalte];

				if (c != null && c.getContents() != null && c.getContents().length() > 0) {

					if (c.getType() == CellType.NUMBER || c.getType() == CellType.NUMBER_FORMULA) {

						double d = ((NumberCell) c).getValue();
						return new BigDecimal(d);

					} else {

						fehlerZeileXLSImport += feldname + " muss vom Typ 'Zahl' sein. Zeile " + iZeile
								+ new String(CRLFAscii);

						return null;
					}

				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			return null;
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public String pruefeUndImportiereBestellvorschlagXLS(byte[] xlsDatei, java.sql.Timestamp tLiefertermin,
			boolean bVorhandenenBestellvorschlagLoeschen, boolean bImportierenWennKeinFehler,
			TheClientDto theClientDto) {

		boolean bZentralerArtikelstamm = getMandantFac()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM, theClientDto);

		byte[] CRLFAscii = { 13, 10 };
		String rueckgabe = "";
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(xlsDatei);

			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");

			Workbook workbook = Workbook.getWorkbook(is, ws);

			Sheet sheet = workbook.getSheet(0);
			HashMap<String, Integer> hmVorhandeneSpalten = new HashMap<String, Integer>();

			if (bImportierenWennKeinFehler == true && bVorhandenenBestellvorschlagLoeschen == true) {
				loescheBestellvorlaegeEinesMandaten(false, theClientDto);
			}

			if (sheet.getRows() > 1) {
				Cell[] sZeile = sheet.getRow(0);

				for (int i = 0; i < sZeile.length; i++) {

					if (sZeile[i].getContents() != null && sZeile[i].getContents().length() > 0) {
						hmVorhandeneSpalten.put(sZeile[i].getContents().trim(), new Integer(i));
					}

				}

			}

			// Artikelnummer muss immer vorhanden sein
			if (hmVorhandeneSpalten.containsKey(XLS_BESTELLVORSCHLAGIMPORT_SPALTE_ARTIKELNUMMER) == false
					|| hmVorhandeneSpalten.containsKey(XLS_BESTELLVORSCHLAGIMPORT_SPALTE_MENGE) == false) {
				rueckgabe += "Es muessen zumindest die Spalten 'Artikelnummer/Menge' vorhanden sein"
						+ new String(CRLFAscii);
				return rueckgabe;
			}

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] sZeile = sheet.getRow(i);
				fehlerZeileXLSImport = "";

				Integer iSpalteArtikelnummer = hmVorhandeneSpalten.get(XLS_BESTELLVORSCHLAGIMPORT_SPALTE_ARTIKELNUMMER);

				if (sZeile != null && sZeile.length > 0) {

					String artikelnummer = sZeile[iSpalteArtikelnummer].getContents();

					if (artikelnummer == null || artikelnummer.length() == 0) {
						rueckgabe += "Die Artikelnummer darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
						continue;
					}

					BigDecimal menge = getBigDecimalAusXLS(sZeile, hmVorhandeneSpalten,
							XLS_BESTELLVORSCHLAGIMPORT_SPALTE_MENGE, i);

					if (menge == null) {
						rueckgabe += "Die Menge darf nicht leer sein. Zeile " + i + new String(CRLFAscii);
						continue;
					}

					ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(artikelnummer, theClientDto);

					if (artikelDto != null) {

						if (bImportierenWennKeinFehler) {
							artikelDto = getArtikelFac().artikelFindByPrimaryKey(artikelDto.getIId(), theClientDto);

							/*
							 * BestellvorschlagDto bvDto = new BestellvorschlagDto();
							 * bvDto.setCMandantCNr(theClientDto.getMandant());
							 * bvDto.setIArtikelId(artikelDto.getIId());
							 * bvDto.setNZubestellendeMenge(menge); bvDto.setTLiefertermin(tLiefertermin);
							 * getBestellvorschlagFac().createBestellvorschlag(bvDto, theClientDto);
							 */

							getBestellvorschlagFac().bestellvorschlagDtoErzeugen(null, theClientDto.getMandant(),
									artikelDto.getIId(), null, null, tLiefertermin, menge, null, null, null, null, null, null,
									bZentralerArtikelstamm, theClientDto);

						}

					} else {
						rueckgabe += "Es konnte kein Artikel mit der Nummer '" + artikelnummer
								+ "' gefunden werden. Zeile " + i + new String(CRLFAscii);
						continue;

					}

				}
			}

		} catch (BiffException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		} catch (IOException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		}

		return rueckgabe;
	}

}
