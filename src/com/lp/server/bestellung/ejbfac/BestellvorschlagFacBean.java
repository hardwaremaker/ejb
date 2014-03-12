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
package com.lp.server.bestellung.ejbfac;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
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
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtklaDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.bestellung.ejb.Bestellvorschlag;
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
import com.lp.server.fertigung.fastlanereader.generated.FLRLos;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.MaterialbedarfDto;
import com.lp.server.partner.fastlanereader.LieferantHandler;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStueckliste;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteMitStrukturDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class BestellvorschlagFacBean extends Facade implements
		BestellvorschlagFac {

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public Integer createBestellvorschlag(
			BestellvorschlagDto bestellvorschlagDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.entry();

		// Preconditions.
		if (bestellvorschlagDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("bestellvorschlagDtoI == null"));
		}

		if (bestellvorschlagDtoI.getCMandantCNr() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"bestellvorschlagDtoI.getCMandantCNr() == null"));
		}

		if (bestellvorschlagDtoI.getIArtikelId() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("bestellvorschlagDtoI.getIArtikelId()== null"));
		}

		if (bestellvorschlagDtoI.getBNettopreisuebersteuert() == null) {
			bestellvorschlagDtoI.setBNettopreisuebersteuert(Helper
					.boolean2Short(false));
		}

		// bestellvorschlagDtoI.setPersonalAendernIID(theClientDto.getIDPersonal(
		// ));
		// bestellvorschlagDtoI.setPersonalAnlegenIID(theClientDto.getIDPersonal(
		// ));

		// PK fuer Partner generieren.
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer iIdBestellvorschlag = pkGen
				.getNextPrimaryKey(PKConst.PK_BESTELLVORSCHLAG);
		bestellvorschlagDtoI.setIId(iIdBestellvorschlag);

		try {
			Bestellvorschlag bestellvorschlag = new Bestellvorschlag(
					bestellvorschlagDtoI.getIId(),
					bestellvorschlagDtoI.getDRabattsatz(),
					bestellvorschlagDtoI.getNNettoGesamtPreisMinusRabatte(),
					bestellvorschlagDtoI.getNNettogesamtpreis(),
					bestellvorschlagDtoI.getNRabattbetrag(),
					bestellvorschlagDtoI.getNNettoeinzelpreis(),
					bestellvorschlagDtoI.getILieferantId(),
					bestellvorschlagDtoI.getIBelegartId(),
					bestellvorschlagDtoI.getCBelegartCNr(),
					bestellvorschlagDtoI.getTLiefertermin(),
					bestellvorschlagDtoI.getNZubestellendeMenge(),
					bestellvorschlagDtoI.getIArtikelId(),
					bestellvorschlagDtoI.getCMandantCNr(),
					bestellvorschlagDtoI.getBNettopreisuebersteuert());
			em.persist(bestellvorschlag);
			em.flush();
			setBestellvorschlagFromBestellvorschlagDto(bestellvorschlag,
					bestellvorschlagDtoI);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
		return bestellvorschlagDtoI.getIId();
	}

	public void removeBestellvorschlag(Integer iId) throws EJBExceptionLP {
		// try {
		Bestellvorschlag toRemove = em.find(Bestellvorschlag.class, iId);
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

	/**
	 * 
	 * @param bestellvorschlagDto
	 *            BestellvorschlagDto
	 * @throws EJBExceptionLP
	 */
	public void removeBestellvorschlag(BestellvorschlagDto bestellvorschlagDto)
			throws EJBExceptionLP {
		if (bestellvorschlagDto != null) {
			Integer iId = bestellvorschlagDto.getIId();
			removeBestellvorschlag(iId);
		}
	}

	public void updateBestellvorschlag(BestellvorschlagDto bestellvorschlagDto)
			throws EJBExceptionLP {
		if (bestellvorschlagDto != null) {
			Integer iId = bestellvorschlagDto.getIId();
			// try {
			Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class,
					iId);
			if (bestellvorschlag == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setBestellvorschlagFromBestellvorschlagDto(bestellvorschlag,
					bestellvorschlagDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }
		}
	}

	public void updateBestellvorschlags(
			BestellvorschlagDto[] bestellvorschlagDtos) throws EJBExceptionLP {
		if (bestellvorschlagDtos != null) {
			for (int i = 0; i < bestellvorschlagDtos.length; i++) {
				updateBestellvorschlag(bestellvorschlagDtos[i]);
			}
		}
	}

	public BestellvorschlagDto bestellvorschlagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Bestellvorschlag bestellvorschlag = em
				.find(Bestellvorschlag.class, iId);
		if (bestellvorschlag == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleBestellvorschlagDto(bestellvorschlag);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public boolean mindestbestellwertErreicht(Integer iLieferantId,
			TheClientDto theClientDto) {

		boolean bMindestbestellwertErreicht = false;
		LieferantDto lieferantDto = getLieferantFac()
				.lieferantFindByPrimaryKey(iLieferantId, theClientDto);

		if (lieferantDto.getNMindestbestellwert() != null
				&& lieferantDto.getNMindestbestellwert().doubleValue() > 0) {

			BestellvorschlagDto[] bestDtos = bestellvorschlagFindByLieferantIIdMandantCNr(
					iLieferantId, theClientDto.getMandant());

			BigDecimal gesamt = new BigDecimal(0);

			for (int i = 0; i < bestDtos.length; i++) {

				if (bestDtos[i].getNNettogesamtpreis() != null) {
					gesamt = gesamt.add(bestDtos[i].getNZubestellendeMenge()
							.multiply(bestDtos[i].getNNettogesamtpreis()));
				}

			}

			if (gesamt.doubleValue() >= lieferantDto.getNMindestbestellwert()
					.doubleValue()) {
				bMindestbestellwertErreicht = true;
			}

		} else {
			bMindestbestellwertErreicht = true;
		}
		return bMindestbestellwertErreicht;
	}

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNr(
			Integer iLieferantId, String cNrMandant) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("BestellvorschlagfindByLieferantIIdMandantCNr");
		query.setParameter(1, iLieferantId);
		query.setParameter(2, cNrMandant);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		return assembleBestellvorschlagDtos(cl);
	}

	public BestellvorschlagDto[] bestellvorschlagFindByLieferantIIdMandantCNrOhneExc(
			Integer iLieferantId, String cNrMandant) {
		try {
			Query query = em
					.createNamedQuery("BestellvorschlagfindByLieferantIIdMandantCNr");
			query.setParameter(1, iLieferantId);
			query.setParameter(2, cNrMandant);
			return assembleBestellvorschlagDtos(query.getResultList());
		} catch (Throwable th) {
			return null;
		}
	}

	private void setBestellvorschlagFromBestellvorschlagDto(
			Bestellvorschlag bestellvorschlag,
			BestellvorschlagDto bestellvorschlagDto) {
		bestellvorschlag.setMandantCNr(bestellvorschlagDto.getCMandantCNr());
		bestellvorschlag.setArtikelIId(bestellvorschlagDto.getIArtikelId());
		bestellvorschlag.setNZubestellendemenge(bestellvorschlagDto
				.getNZubestellendeMenge());
		bestellvorschlag.setTLiefertermin(bestellvorschlagDto
				.getTLiefertermin());
		bestellvorschlag.setBelegartCNr(bestellvorschlagDto.getCBelegartCNr());
		bestellvorschlag.setIBelegartid(bestellvorschlagDto.getIBelegartId());
		bestellvorschlag.setIBelegartpositionid(bestellvorschlagDto
				.getIBelegartpositionid());
		bestellvorschlag.setLieferantIId(bestellvorschlagDto.getILieferantId());
		bestellvorschlag.setNNettoeinzelpreis(bestellvorschlagDto
				.getNNettoeinzelpreis());
		bestellvorschlag.setNRabattbetrag(bestellvorschlagDto
				.getNRabattbetrag());
		bestellvorschlag.setFRabattsatz(bestellvorschlagDto.getDRabattsatz());
		bestellvorschlag.setNNettogesamtpreis(bestellvorschlagDto
				.getNNettogesamtpreis());
		bestellvorschlag.setBNettopreisuebersteuert(bestellvorschlagDto
				.getBNettopreisuebersteuert());
		bestellvorschlag.setProjektIId(bestellvorschlagDto.getProjektIId());
		em.merge(bestellvorschlag);
		em.flush();
	}

	public long getAnzahlBestellvorschlagDesMandanten(TheClientDto theClientDto)
			throws EJBExceptionLP {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(distinct flrbestellvorschlag.i_id) "
					+ " from FLRBestellvorschlag flrbestellvorschlag "
					+ " where flrbestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR
					+ "='" + theClientDto.getMandant() + "'";
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

	private BestellvorschlagDto assembleBestellvorschlagDto(
			Bestellvorschlag bestellvorschlag) {
		return BestellvorschlagDtoAssembler.createDto(bestellvorschlag);
	}

	private BestellvorschlagDto[] assembleBestellvorschlagDtos(
			Collection<?> bestellvorschlaege) {
		List<BestellvorschlagDto> list = new ArrayList<BestellvorschlagDto>();
		if (bestellvorschlaege != null) {
			Iterator<?> iterator = bestellvorschlaege.iterator();
			while (iterator.hasNext()) {
				Bestellvorschlag bestellvorschlag = (Bestellvorschlag) iterator
						.next();
				list.add(assembleBestellvorschlagDto(bestellvorschlag));
			}
		}
		BestellvorschlagDto[] returnArray = new BestellvorschlagDto[list.size()];
		return (BestellvorschlagDto[]) list.toArray(returnArray);
	}

	public void erstelleBestellvorschlagAnhandEinesAngebots(Integer angebotIId,
			java.sql.Date dLiefertermin, TheClientDto theClientDto) {
		loescheBestellvorlaegeEinesMandaten(theClientDto);

		try {
			AngebotpositionDto[] agposDtos = getAngebotpositionFac()
					.angebotpositionFindByAngebotIIdOhneAlternative(angebotIId,
							theClientDto);

			AngebotDto angebotDto = getAngebotFac().angebotFindByPrimaryKey(
					angebotIId, theClientDto);

			for (int i = 0; i < agposDtos.length; i++) {

				if (agposDtos[i].getArtikelIId() != null) {
					ArtikelDto artikelDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									agposDtos[i].getArtikelIId(), theClientDto);

					StuecklisteDto stklDto = getStuecklisteFac()
							.stuecklisteFindByArtikelIIdMandantCNrOhneExc(
									agposDtos[i].getArtikelIId(),
									theClientDto.getMandant());

					if (stklDto != null) {

						List<?> m = null;
						try {
							m = getStuecklisteFac()
									.getStrukturDatenEinerStueckliste(
											stklDto.getIId(),
											theClientDto,
											StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
											0, null, true, true,
											agposDtos[i].getNMenge(), null,
											false);
						} catch (RemoteException ex4) {
							throwEJBExceptionLPRespectOld(ex4);
						}

						Iterator<?> it = m.listIterator();

						while (it.hasNext()) {
							StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
									.next();
							StuecklistepositionDto position = struktur
									.getStuecklistepositionDto();

							bestellvorschlagDtoErzeugen(
									LocaleFac.BELEGART_ANGEBOT,
									theClientDto.getMandant(),
									position.getArtikelIId(),
									angebotDto.getIId(),
									null,
									new java.sql.Timestamp(dLiefertermin
											.getTime()),
									position.getNZielmenge().multiply(
											agposDtos[i].getNMenge()),
									angebotDto.getProjektIId(), theClientDto);

						}

					} else {
						if (Helper.short2boolean(artikelDto
								.getBLagerbewirtschaftet()) == true) {
							bestellvorschlagDtoErzeugen(
									LocaleFac.BELEGART_ANGEBOT,
									theClientDto.getMandant(),
									artikelDto.getIId(),
									angebotDto.getIId(),
									agposDtos[i].getIId(),
									new java.sql.Timestamp(dLiefertermin
											.getTime()),
									agposDtos[i].getNMenge(),
									angebotDto.getProjektIId(), theClientDto);
						}
					}

				}

			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void erstelleBestellvorschlagAnhandStuecklistenmindestlagerstand(
			java.sql.Date dLiefertermin, TheClientDto theClientDto) {
		loescheBestellvorlaegeEinesMandaten(theClientDto);

		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		HashMap<Integer, BigDecimal> hmArtikel = new HashMap<Integer, BigDecimal>();
		session = factory.openSession();
		String queryString = "SELECT stkl,(SELECT count(*) FROM FLRStuecklisteposition AS pos WHERE pos.flrartikel.i_id=stkl.flrartikel.i_id ) as anzstklpos FROM FLRStueckliste AS stkl WHERE stkl.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND stkl.flrartikel.f_lagermindest > 0";
		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale")
				.setParameter("paramLocale", sLocUI);
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

			if (iAzahlinVerwendung == 0) {
				// Dann ists eine Wurzelstueckliste

				// Diese nun aufloesen

				List<?> m = null;
				try {
					m = getStuecklisteFac()
							.getStrukturDatenEinerStueckliste(
									flrStueckliste.getI_id(),
									theClientDto,
									StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE,
									0,
									null,
									true,
									true,
									new BigDecimal(flrStueckliste
											.getFlrartikel()
											.getF_lagermindest()), null, false);
				} catch (RemoteException ex4) {
					throwEJBExceptionLPRespectOld(ex4);
				}

				Iterator<?> it = m.listIterator();

				while (it.hasNext()) {
					StuecklisteMitStrukturDto struktur = (StuecklisteMitStrukturDto) it
							.next();
					StuecklistepositionDto position = struktur
							.getStuecklistepositionDto();

					BigDecimal nBedarf = position.getNZielmenge();
					if (hmArtikel.containsKey(position.getArtikelIId())) {

						nBedarf = nBedarf.add(hmArtikel.get(position
								.getArtikelIId()));
					}

					hmArtikel.put(position.getArtikelIId(), nBedarf);
				}
			}
		}
		closeSession(session);

		// Nun alle Artikel, welche noch nicht in der Liste sind, und einen
		// Lagermindeststand haben, hinzufuegen
		session = factory.openSession();
		queryString = "SELECT a, (SELECT stkl FROM FLRStueckliste AS stkl WHERE stkl.mandant_c_nr=a.mandant_c_nr AND stkl.artikel_i_id=a.i_id) FROM FLRArtikel AS a WHERE a.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND a.f_lagermindest > 0 AND a.b_lagerbewirtschaftet=1";
		query = session.createQuery(queryString);
		resultList = query.list();
		resultListIterator = resultList.iterator();
		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			FLRArtikel a = (FLRArtikel) o[0];
			FLRStueckliste s = (FLRStueckliste) o[1];

			if (!hmArtikel.containsKey(a.getI_id())) {
				if (s == null
						|| Helper.short2boolean(s.getB_fremdfertigung()) == true) {

					hmArtikel.put(a.getI_id(),
							new BigDecimal(a.getF_lagermindest()));
				}
			}

		}
		closeSession(session);

		try {
			LagerDto[] lagerDto = getLagerFac().lagerFindByMandantCNr(
					theClientDto.getMandant());

			Iterator<Integer> it = hmArtikel.keySet().iterator();
			while (it.hasNext()) {
				Integer artikelIId = it.next();

				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						artikelIId, theClientDto);

				if (Helper.short2boolean(aDto.getBLagerbewirtschaftet())) {

					BigDecimal nBedarf = hmArtikel.get(artikelIId);

					// Nun bei allen Artikel die
					// Lagerstaende/In-Fertigung/Bestellt
					// abziehen
					BigDecimal bdBestellt = getArtikelbestelltFac()
							.getAnzahlBestellt(artikelIId);

					BigDecimal bdInFertigung = getFertigungFac()
							.getAnzahlInFertigung(artikelIId, theClientDto);

					BigDecimal bdLagerstand = new BigDecimal(0);

					for (int j = 0; j < lagerDto.length; j++) {

						if (Helper.short2boolean(lagerDto[j]
								.getBBestellvorschlag())) {
							bdLagerstand = bdLagerstand
									.add(getLagerFac().getLagerstand(
											artikelIId, lagerDto[j].getIId(),
											theClientDto));
						}

					}

					// Wenn groesser 0, dann bestellen

					BigDecimal diff = nBedarf.subtract(bdBestellt)
							.subtract(bdInFertigung).subtract(bdLagerstand);

					if (diff.doubleValue() > 0) {

						bestellvorschlagDtoErzeugen(
								null,
								theClientDto.getMandant(),
								artikelIId,
								null,
								null,
								new java.sql.Timestamp(dLiefertermin.getTime()),
								diff, null, theClientDto);
					}
				}

			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	/**
	 * METHODE VON CK
	 * 
	 * @param iVorlaufzeit
	 *            Integer
	 * @param dateFuerEintraegeOhneLiefertermin
	 *            Date
	 * @param theClientDto
	 *            String
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void erstelleBestellvorschlag(Integer iVorlaufzeit,
			Integer iToleranz, java.sql.Date dateFuerEintraegeOhneLiefertermin,
			ArrayList<Integer> arLosIId, ArrayList<Integer> arAuftragIId,
			boolean bMitNichtlagerbewirtschafteten,
			boolean bNurLospositionenBeruecksichtigen, TheClientDto theClientDto) {
		// alten bestellvorschlag loeschen
		loescheBestellvorlaegeEinesMandaten(theClientDto);
		Session session = null;
		try {

			// Wenn auftragsIId angebeben werden, dann deren Los suchen

			if (arAuftragIId != null) {

				for (int i = 0; i < arAuftragIId.size(); i++) {
					LosDto[] losDtos = getFertigungFac().losFindByAuftragIId(
							arAuftragIId.get(i));

					for (int j = 0; j < losDtos.length; j++) {
						if (arLosIId == null) {
							arLosIId = new ArrayList<Integer>();
						}

						arLosIId.add(losDtos[j].getIId());

					}

				}

			}

			// Wenn losIIds vorhanden dann nur die Artikel dieser Lose
			ArrayList<Integer> arArtikelIIds = new ArrayList<Integer>();
			if (arLosIId != null) {
				if (arLosIId.size() > 0) {
					Object[] oLosIId = new Object[arLosIId.size()];
					oLosIId = arLosIId.toArray();
					session = FLRSessionFactory.getFactory().openSession();
					org.hibernate.Criteria critLose = session
							.createCriteria(FLRLos.class);
					critLose.add(Restrictions.in(FertigungFac.FLR_LOS_I_ID,
							oLosIId));
					List<?> resultListLose = critLose.list();
					Iterator<?> resultListLoseIterator = resultListLose
							.iterator();
					while (resultListLoseIterator.hasNext()) {
						FLRLos los = (FLRLos) resultListLoseIterator.next();
						LossollmaterialDto[] material = getFertigungFac()
								.lossollmaterialFindByLosIId(los.getI_id());
						for (int i = 0; i < material.length; i++) {
							if (material[i].getArtikelIId() != null) {
								arArtikelIIds.add(material[i].getArtikelIId());
							}
						}
					}
				}
				closeSession(session);
			}

			// Alle Lagerbewirtschafteten Artikel holen
			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Criteria crit = session
					.createCriteria(FLRArtikel.class);
			// Filter nach Mandant
			crit.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_MANDANT_C_NR,
					theClientDto.getMandant()));
			// keine Hand- und AZ-Artikel
			crit.add(Restrictions.not(Restrictions.in(
					ArtikelFac.FLR_ARTIKELLISTE_ARTIKELART_C_NR, new String[] {
							ArtikelFac.ARTIKELART_HANDARTIKEL,
							ArtikelFac.ARTIKELART_ARBEITSZEIT })));
			if (bMitNichtlagerbewirtschafteten == false) {
				// nur lagerbewirtschaftete
				crit.add(Restrictions.eq(
						ArtikelFac.FLR_ARTIKEL_B_LAGERBEWIRTSCHAFTET,
						Helper.boolean2Short(true)));
			}
			// Zusaetlich nach Losen wenn Materialien vorhanden
			if (arArtikelIIds.size() > 0) {
				Object[] oArtikelIIds = arArtikelIIds.toArray();
				crit.add(Restrictions.in(ArtikelFac.FLR_ARTIKEL_I_ID,
						oArtikelIIds));
			}

			List<?> resultList = crit.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				FLRArtikel artikel = (FLRArtikel) resultListIterator.next();

				if (Helper.short2boolean(artikel.getB_lagerbewirtschaftet())) {

					// Ist dieser Artikel eine Stueckliste?
					StuecklisteDto stuecklisteDto = getStuecklisteFac()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikel.getI_id(), theClientDto);

					if (stuecklisteDto != null
							&& Helper.short2boolean(stuecklisteDto
									.getBFremdfertigung()) == false) {
						// selbstgefertigte Stuecklisten werden im BV nicht
						// beruecksichtigt.
					} else {
						ArtikelDto artikelDto = getArtikelFac()
								.artikelFindByPrimaryKey(artikel.getI_id(),
										theClientDto);
						ArtklaDto artklsDto = null;
						if (artikelDto.getArtklaIId() != null) {
							artklsDto = getArtikelFac().artklaFindByPrimaryKey(
									artikelDto.getArtklaIId(), theClientDto);
						}
						if (artklsDto != null
								&& Helper.short2boolean(artklsDto.getBTops()) == true) {
							// Keine tops artikel
						} else {
							// Wenn keine Stueckliste, bzw. Fremdgefertigte
							// Stueckliste, dann
							ArrayList<?> al = null;

							if (bNurLospositionenBeruecksichtigen == true) {
								al = getInternebestellungFac().berechneBedarfe(
										artikelDto, iVorlaufzeit, iToleranz,
										dateFuerEintraegeOhneLiefertermin,
										false, theClientDto, arLosIId);
							} else {
								al = getInternebestellungFac().berechneBedarfe(
										artikelDto, iVorlaufzeit, iToleranz,
										dateFuerEintraegeOhneLiefertermin,
										false, theClientDto, null);
							}

							MaterialbedarfDto[] materialbedarf = new MaterialbedarfDto[al
									.size()];
							materialbedarf = (MaterialbedarfDto[]) al
									.toArray(materialbedarf);

							for (int i = 0; i < materialbedarf.length; i++) {

								bestellvorschlagDtoErzeugen(
										materialbedarf[i].getSBelegartCNr(),
										artikel.getMandant_c_nr(),
										artikel.getI_id(),
										materialbedarf[i].getIBelegIId(),
										materialbedarf[i]
												.getIBelegpositionIId(),
										new java.sql.Timestamp(
												materialbedarf[i].getTTermin()
														.getTime()),
										materialbedarf[i].getNMenge(),
										materialbedarf[i].getProjektIId(),
										theClientDto);
							}
						}
					}
				} else {
					if (bMitNichtlagerbewirtschafteten == true) {

						BigDecimal stdMenge = new BigDecimal(0);
						ArtikellieferantDto[] dtos = getArtikelFac()
								.artikellieferantFindByArtikelIId(
										artikel.getI_id(), theClientDto);

						if (dtos != null && dtos.length > 0
								&& dtos[0].getFStandardmenge() != null) {
							stdMenge = new BigDecimal(
									dtos[0].getFStandardmenge());
						}

						bestellvorschlagDtoErzeugen(
								null,
								artikel.getMandant_c_nr(),
								artikel.getI_id(),
								null,
								null,
								new java.sql.Timestamp(
										dateFuerEintraegeOhneLiefertermin
												.getTime()), stdMenge, null,
								theClientDto);

					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void bestellvorschlagInLiefergruppenanfragenUmwandeln(
			String projekt, TheClientDto theClientDto) {
		// Liefergruppen
		HashMap<Integer, ArrayList<FLRBestellvorschlag>> liefergruppen = new HashMap<Integer, ArrayList<FLRBestellvorschlag>>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT bv"
				+ " FROM FLRBestellvorschlag AS bv "
				+ " ORDER BY bv.flrartikel.flrliefergruppe.i_id ASC WHERE bv.flrartikel.flrliefergruppe.i_id IS NOT NULL ";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<FLRBestellvorschlag> resultList = hqlquery.list();
		Iterator<FLRBestellvorschlag> resultListIterator = resultList
				.iterator();

		while (resultListIterator.hasNext()) {
			FLRBestellvorschlag bv = resultListIterator.next();

			if (bv.getFlrartikel().getFlrliefergruppe() != null) {
				ArrayList<FLRBestellvorschlag> alArtikel = null;

				if (liefergruppen.containsKey(bv.getFlrartikel()
						.getFlrliefergruppe().getI_id())) {
					alArtikel = liefergruppen.get(bv.getFlrartikel()
							.getFlrliefergruppe().getI_id());
				} else {
					alArtikel = new ArrayList<FLRBestellvorschlag>();

				}

				alArtikel.add(bv);
				liefergruppen.put(bv.getFlrartikel().getFlrliefergruppe()
						.getI_id(), alArtikel);
			}

		}

		KostenstelleDto kostenstelleDto = getPersonalFac()
				.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
						theClientDto).getKostenstelleDto_Stamm();

		Iterator<Integer> liefergrupenIds = liefergruppen.keySet().iterator();

		while (liefergrupenIds.hasNext()) {

			Integer liefergruppeIId = liefergrupenIds.next();
			ArrayList<FLRBestellvorschlag> alArtikel = liefergruppen
					.get(liefergruppeIId);

			AnfrageDto anfrageDto = new AnfrageDto();
			anfrageDto.setMandantCNr(theClientDto.getMandant());
			anfrageDto.setCBez(projekt);
			anfrageDto.setLiefergruppeIId(liefergruppeIId);
			anfrageDto.setFWechselkursmandantwaehrungzubelegwaehrung(1D);
			anfrageDto.setFAllgemeinerRabattsatz(0D);
			anfrageDto.setNTransportkosteninanfragewaehrung(new BigDecimal(0));
			anfrageDto.setWaehrungCNr(theClientDto.getSMandantenwaehrung());
			anfrageDto.setTBelegdatum(Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis())));
			anfrageDto.setStatusCNr(AnfrageServiceFac.ANFRAGESTATUS_ANGELEGT);
			anfrageDto.setArtCNr(AnfrageServiceFac.ANFRAGEART_LIEFERGRUPPE);
			anfrageDto.setBelegartCNr(LocaleFac.BELEGART_ANFRAGE);
			anfrageDto.setKostenstelleIId(kostenstelleDto.getIId());

			Integer anfrageIId = getAnfrageFac().createAnfrage(anfrageDto,
					theClientDto);

			for (int i = 0; i < alArtikel.size(); i++) {
				FLRBestellvorschlag bv = alArtikel.get(i);
				AnfragepositionDto anfragepositionDto = new AnfragepositionDto();
				anfragepositionDto.setBelegIId(anfrageIId);
				anfragepositionDto.setArtikelIId(bv.getArtikel_i_id());
				anfragepositionDto
						.setPositionsartCNr(AnfrageServiceFac.ANFRAGEPOSITIONART_IDENT);
				anfragepositionDto.setNMenge(bv.getN_zubestellendemenge());
				anfragepositionDto.setEinheitCNr(bv.getFlrartikel()
						.getEinheit_c_nr());
				anfragepositionDto.setNRichtpreis(new BigDecimal(0));
				anfragepositionDto.setBArtikelbezeichnunguebersteuert(Helper
						.boolean2Short(false));

				getAnfragepositionFac().createAnfrageposition(
						anfragepositionDto, theClientDto);

				try {
					getBestellvorschlagFac().removeBestellvorschlag(
							bv.getI_id());
				} catch (Exception e) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
							"Bestellvorschlag ID=" + bv.getI_id());
				}
			}

		}

		session.close();

	}

	public void artikellieferantZuruecksetzen(
			ArrayList<Integer> bestellvorschlagIIds, TheClientDto theClientDto) {

		for (int i = 0; i < bestellvorschlagIIds.size(); i++) {
			Bestellvorschlag bestellvorschlag = em.find(Bestellvorschlag.class,
					bestellvorschlagIIds.get(i));

			ArtikellieferantDto[] artLiefDto = getArtikelFac()
					.artikellieferantFindByArtikelIId(
							bestellvorschlag.getArtikelIId(), theClientDto);
			if (artLiefDto != null && artLiefDto.length>0) {
				bestellvorschlag.setLieferantIId(artLiefDto[0].getLieferantIId());
			}
		}
	}

	public void bestellvorschlagDtoErzeugen(String belegartCNr,
			String mandantCNr, Integer artikelIId, Integer belegIId,
			Integer belegpositionIId, java.sql.Timestamp tTermin,
			BigDecimal nMenge, Integer projektIId, TheClientDto theClientDto) {
		BestellvorschlagDto bestellvorschlagDto = new BestellvorschlagDto();
		bestellvorschlagDto.setCBelegartCNr(belegartCNr);
		bestellvorschlagDto.setCMandantCNr(mandantCNr);
		bestellvorschlagDto.setIArtikelId(artikelIId);
		bestellvorschlagDto.setIBelegartId(belegIId);
		bestellvorschlagDto.setIBelegartpositionid(belegpositionIId);
		bestellvorschlagDto.setTLiefertermin(tTermin);
		bestellvorschlagDto.setNZubestellendeMenge(nMenge);
		bestellvorschlagDto.setBNettopreisuebersteuert(Helper
				.boolean2Short(false));
		bestellvorschlagDto.setProjektIId(projektIId);

		// Einkaufspreis des ersten Lieferanten in
		// Mandantenwaehrung
		ArtikellieferantDto[] artLiefDto = getArtikelFac()
				.artikellieferantFindByArtikelIId(artikelIId, theClientDto);
		if (artLiefDto != null) {
			Integer iKleinsterIsort = null;
			for (int y = 0; y < artLiefDto.length; y++) {
				if (iKleinsterIsort == null) {
					iKleinsterIsort = y;
				} else {
					if (artLiefDto[iKleinsterIsort].getISort().intValue() > artLiefDto[y]
							.getISort().intValue()) {
						iKleinsterIsort = y;
					}
				}
			}
			ArtikellieferantDto helper = new ArtikellieferantDto();
			if (iKleinsterIsort != null) {
				helper = artLiefDto[iKleinsterIsort];
				ArtikellieferantDto helper2 = getArtikelFac()
						.getArtikelEinkaufspreis(artikelIId,
								helper.getLieferantIId(), nMenge,
								theClientDto.getSMandantenwaehrung(), null,
								theClientDto);
				bestellvorschlagDto.setILieferantId(helper.getLieferantIId());
				if (helper2 != null) {
					bestellvorschlagDto.setNNettoeinzelpreis(helper2
							.getNEinzelpreis());
					bestellvorschlagDto.setNNettogesamtpreis(helper2
							.getNNettopreis());
					bestellvorschlagDto.setDRabattsatz(helper2.getFRabatt());
					if (helper2.getNEinzelpreis() != null
							&& helper2.getFRabatt() != null) {
						bestellvorschlagDto.setNRabattbetrag(Helper
								.rundeKaufmaennisch(
										helper2.getNEinzelpreis().multiply(
												new BigDecimal(helper2
														.getFRabatt())
														.movePointLeft(2)), 4));
					}
					if (Helper.short2boolean(helper2.getBRabattbehalten())) {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(false));
					} else {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(true));
					}

				} else {
					bestellvorschlagDto.setNNettoeinzelpreis(helper
							.getNEinzelpreis());
					bestellvorschlagDto.setNNettogesamtpreis(helper
							.getNNettopreis());
					bestellvorschlagDto.setDRabattsatz(helper.getFRabatt());
					if (helper.getNEinzelpreis() != null
							&& helper.getFRabatt() != null) {
						bestellvorschlagDto.setNRabattbetrag(Helper
								.rundeKaufmaennisch(
										helper.getNEinzelpreis().multiply(
												new BigDecimal(helper
														.getFRabatt())
														.movePointLeft(2)), 4));
					}
					if (Helper.short2boolean(helper.getBRabattbehalten())) {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(false));
					} else {
						bestellvorschlagDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(true));
					}

				}
			}
		}
		try {
			context.getBusinessObject(BestellvorschlagFac.class)
					.createBestellvorschlag(bestellvorschlagDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	public void loescheBestellvorlaegeEinesMandaten(TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String hqlDelete = "delete FLRBestellvorschlag where mandant_c_nr = :param1";
			session.createQuery(hqlDelete)
					.setString("param1", theClientDto.getMandant())
					.executeUpdate();
		} finally {
			closeSession(session);
		}
	}

	private BestellvorschlagDto[] bereinigeBestellvorschlagVonVeraltenArtikeln(
			BestellvorschlagDto[] bvDtos, TheClientDto theClientDto) {

		boolean bRausfiltern = false;
		try {
			ParametermandantDto param = getParameterFac().getMandantparameter(
					theClientDto.getMandant(),
					ParameterFac.KATEGORIE_STUECKLISTE,
					ParameterFac.PARAMETER_STRUKTURIERTER_STKLIMPORT);

			if (param.getCWertAsObject() != null
					&& ((Integer) param.getCWertAsObject()) == 1) {
				bRausfiltern = true;
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (bRausfiltern == true) {
			ArrayList<BestellvorschlagDto> alDaten = new ArrayList<BestellvorschlagDto>();

			Object[] o = getBestellungReportFac().getGeaenderteArtikelDaten(
					theClientDto);
			HashMap hmVeralteteArtikel = (HashMap) o[0];

			for (int i = 0; i < bvDtos.length; i++) {

				if (!hmVeralteteArtikel.containsKey(bvDtos[i].getIArtikelId())) {

					alDaten.add(bvDtos[i]);
				}

			}

			return (BestellvorschlagDto[]) alDaten
					.toArray(new BestellvorschlagDto[] {});
		} else {
			return bvDtos;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean createBESausBVfuerAlleLieferantenMitGleichenTermin(
			FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen) {
		Boolean bool = new Boolean(true);
		try {
			// alle Lieferanten
			ArrayList<?> lieferantenIds = (ArrayList<?>) LieferantHandler
					.getFLRLieferantenliste(LieferantHandler
							.getQueryLieferantenliste());

			for (int i = 0; i < lieferantenIds.size(); i++) {

				FilterKriterium[] fkneu = new FilterKriterium[2];
				fkneu[0] = new FilterKriterium(
						BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "."
								+ "i_id", true, "'" + lieferantenIds.get(i)
								+ "'", FilterKriterium.OPERATOR_EQUAL, false);

				fkneu[1] = new FilterKriterium(
						BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN,
						true, "", FilterKriterium.OPERATOR_IS + " "
								+ FilterKriterium.OPERATOR_NOT_NULL, false);

				BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler
						.getListeBestellvorschlaege(
								fkneu,
								ski,
								BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF_UND_GLEICHE_TERMIN);

				aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(
						aBestellvorschlagDto, theClientDto);

				if (bProjektklammerberuecksichtigen == true) {
					HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(aBestellvorschlagDto);
					Iterator itProjekte = hmNachProjektenGetrennt.keySet()
							.iterator();
					while (itProjekte.hasNext()) {
						BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt
								.get(itProjekte.next());
						getBestellvorschlagFac()
								.befuellenDerBestellungUndBestellposition(
										bbDtos, kostenstelleIId, theClientDto);
					}
				} else {
					getBestellvorschlagFac()
							.befuellenDerBestellungUndBestellposition(
									aBestellvorschlagDto, kostenstelleIId,
									theClientDto);
				}

			}
		} catch (EJBExceptionLP e) {
			bool = new Boolean(false);
		}
		return bool;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean createBESausBVjeLieferant(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen) {

		// alle Lieferanten
		ArrayList<?> lieferantenIds = (ArrayList<?>) LieferantHandler
				.getFLRLieferantenliste(LieferantHandler
						.getQueryLieferantenliste());

		for (int i = 0; i < lieferantenIds.size(); i++) {

			FilterKriterium[] fkneu = new FilterKriterium[1];
			fkneu[0] = new FilterKriterium(
					BestellungFac.FLR_BESTELLUNG_FLRLIEFERANT + "." + "i_id",
					true, "'" + lieferantenIds.get(i) + "'",
					FilterKriterium.OPERATOR_EQUAL, false);

			BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler
					.getListeBestellvorschlaege(fkneu, ski,
							BestellvorschlagFac.BES_NACH_BV_FUER_JEDEN_LF);
			aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(
					aBestellvorschlagDto, theClientDto);

			if (bProjektklammerberuecksichtigen == true) {
				HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(aBestellvorschlagDto);
				Iterator itProjekte = hmNachProjektenGetrennt.keySet()
						.iterator();
				while (itProjekte.hasNext()) {
					BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt
							.get(itProjekte.next());

					getBestellvorschlagFac()
							.befuellenDerBestellungausBVfuerBestimmtenLieferant(
									bbDtos, kostenstelleIId, theClientDto);

				}
			} else {
				getBestellvorschlagFac()
						.befuellenDerBestellungausBVfuerBestimmtenLieferant(
								aBestellvorschlagDto, kostenstelleIId,
								theClientDto);
			}

		}
		return new Boolean(true);
	}

	public Boolean createBESausBVfuerBestimmtenLieferantUndTermin(
			FilterKriterium[] fk, SortierKriterium[] ski,
			TheClientDto theClientDto, Integer kostenstelleIId,
			boolean bProjektklammerberuecksichtigen) {

		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler
				.getListeBestellvorschlaege(
						fk,
						ski,
						BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF_UND_TERMIN);
		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(
				aBestellvorschlagDto, theClientDto);

		if (bProjektklammerberuecksichtigen == true) {
			HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(aBestellvorschlagDto);
			Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
			while (itProjekte.hasNext()) {
				BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt
						.get(itProjekte.next());
				befuellenDerBestellungUndBestellposition(bbDtos,
						kostenstelleIId, theClientDto);
			}
		} else {
			befuellenDerBestellungUndBestellposition(aBestellvorschlagDto,
					kostenstelleIId, theClientDto);
		}

		return new Boolean(true);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BestellungDto[] createBESausBVzuRahmen(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler
				.getListeBestellvorschlaege(fk, ski,
						BestellvorschlagFac.BES_ABRUFE_ZU_RAHMEN);
		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(
				aBestellvorschlagDto, theClientDto);
		return getBestellvorschlagFac().erstelleAbrufbestellungenAusBV(
				aBestellvorschlagDto, theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean createBESausBVfueBestimmtenLieferant(FilterKriterium[] fk,
			SortierKriterium[] ski, TheClientDto theClientDto,
			Integer kostenstelleIId, boolean bProjektklammerberuecksichtigen) {

		BestellvorschlagDto[] aBestellvorschlagDto = BestellvorschlagHandler
				.getListeBestellvorschlaege(fk, ski,
						BestellvorschlagFac.BES_NACH_BV_FUER_BESTIMMTEN_LF);

		aBestellvorschlagDto = bereinigeBestellvorschlagVonVeraltenArtikeln(
				aBestellvorschlagDto, theClientDto);

		if (bProjektklammerberuecksichtigen == true) {
			HashMap<Integer, BestellvorschlagDto[]> hmNachProjektenGetrennt = trenneNachProjekten(aBestellvorschlagDto);
			Iterator itProjekte = hmNachProjektenGetrennt.keySet().iterator();
			while (itProjekte.hasNext()) {
				BestellvorschlagDto[] bbDtos = hmNachProjektenGetrennt
						.get(itProjekte.next());

				getBestellvorschlagFac()
						.befuellenDerBestellungausBVfuerBestimmtenLieferant(
								bbDtos, kostenstelleIId, theClientDto);
			}
		} else {
			getBestellvorschlagFac()
					.befuellenDerBestellungausBVfuerBestimmtenLieferant(
							aBestellvorschlagDto, kostenstelleIId, theClientDto);
		}

		return new Boolean(true);
	}

	private HashMap<Integer, BestellvorschlagDto[]> trenneNachProjekten(
			BestellvorschlagDto[] aBestellvorschlagDto) {
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
			ArrayList<BestellvorschlagDto> alBV = hmGetrenntNachProjekten
					.get(projektIId);
			hmProjekte.put(projektIId, (BestellvorschlagDto[]) alBV
					.toArray(new BestellvorschlagDto[alBV.size()]));

		}

		return hmProjekte;
	}

	public void befuellenDerBestellungausBVfuerBestimmtenLieferant(
			BestellvorschlagDto[] aBestellvorschlagDto,
			Integer kostenstelleIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// isort in Bestellposition
			int j = 0;

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
			boolean bBestellungAnlegen = true;
			Integer iIdBestellung = null;

			for (int i = 0; i < aBestellvorschlagDto.length; i++) {
				// anlegen der Bestellung
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(
								aBestellvorschlagDto[0].getILieferantId(),
								theClientDto);
				BigDecimal wechselkurs = getLocaleFac().getWechselkurs2(
						mandantDto.getWaehrungCNr(),
						lieferantDto.getWaehrungCNr(), theClientDto);
				if (bBestellungAnlegen == true) {
					BestellungDto bestellungDto = new BestellungDto();
					AnsprechpartnerDto ansprechpartnerDto[] = getAnsprechpartnerFac()
							.ansprechpartnerFindByAnsprechpartnerIId(
									lieferantDto.getPartnerIId(), theClientDto);
					if (ansprechpartnerDto.length > 0) {
						bestellungDto
								.setAnsprechpartnerIId(ansprechpartnerDto[0]
										.getIId());
					}

					PersonalDto anfordererDto = getPersonalFac()
							.personalFindByPrimaryKey(
									theClientDto.getIDPersonal(), theClientDto);
					bestellungDto.setPersonalIIdAnforderer(anfordererDto
							.getIId());

					bestellungDto.setPersonalIIdAendern(theClientDto
							.getIDPersonal());
					bestellungDto.setPersonalIIdAnlegen(theClientDto
							.getIDPersonal());
					bestellungDto.setProjektIId(aBestellvorschlagDto[0]
							.getProjektIId());
					bestellungDto.setZahlungszielIId(lieferantDto
							.getZahlungszielIId());
					bestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
					// bestellungDto.setBestelltextIIdFusstext();
					// bestellungDto.setBestelltextIIdKopftext();
					bestellungDto
							.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
					bestellungDto
							.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
					ParametermandantDto parameterDto = getParameterFac()
							.getMandantparameter(
									theClientDto.getMandant(),
									ParameterFac.KATEGORIE_BESTELLUNG,
									ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG);

					bestellungDto.setBTeillieferungMoeglich(Helper
							.boolean2Short((Boolean) parameterDto
									.getCWertAsObject()));
					bestellungDto.setDBelegdatum(this.getDate());
					bestellungDto.setDLiefertermin(aBestellvorschlagDto[0]
							.getTLiefertermin());
					bestellungDto.setKostenstelleIId(kostenstelleIId);
					bestellungDto.setLieferantIIdBestelladresse(lieferantDto
							.getIId());
					bestellungDto.setLieferantIIdRechnungsadresse(lieferantDto
							.getIId());

					// Default Lieferadresse ist die Adresse des aktuellen
					// Mandanten
					Integer iPartnerIIdLIeferadresse = getMandantFac()
							.mandantFindByPrimaryKey(theClientDto.getMandant(),
									theClientDto).getPartnerIIdLieferadresse();
					bestellungDto
							.setPartnerIIdLieferadresse(iPartnerIIdLIeferadresse);

					bestellungDto
							.setSpediteurIId(lieferantDto.getIdSpediteur());
					bestellungDto.setMandantCNr(mandantDto.getCNr());
					bestellungDto.setLieferartIId(lieferantDto
							.getLieferartIId());
					bestellungDto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
					bestellungDto.setFAllgemeinerRabattsatz(new Double(0));

					bestellungDto
							.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
									wechselkurs.doubleValue()));

					iIdBestellung = getBestellungFac().createBestellung(
							bestellungDto, theClientDto);
					bBestellungAnlegen = false;
				}
				// Positionen anlegen.
				BestellpositionDto bestellpositionDto = new BestellpositionDto();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								aBestellvorschlagDto[i].getIArtikelId(),
								theClientDto);

				bestellpositionDto.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));

				bestellpositionDto.setArtikelIId(aBestellvorschlagDto[i]
						.getIArtikelId());
				bestellpositionDto.setBDrucken(Helper.boolean2Short(false));
				bestellpositionDto
						.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				bestellpositionDto
						.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bestellpositionDto.setBestellungIId(iIdBestellung);
				bestellpositionDto.setCBez(artikelDto.getArtikelsprDto()
						.getCBez());
				bestellpositionDto.setISort(new Integer(j++));
				bestellpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
				bestellpositionDto.setNMenge(aBestellvorschlagDto[i]
						.getNZubestellendeMenge());

				if (aBestellvorschlagDto[i].getCBelegartCNr() != null
						&& aBestellvorschlagDto[i].getCBelegartCNr().equals(
								LocaleFac.BELEGART_LOS)
						&& aBestellvorschlagDto[i].getIBelegartpositionid() != null) {

					LossollmaterialDto lossollmaterialDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKeyOhneExc(
									aBestellvorschlagDto[i]
											.getIBelegartpositionid());
					if (lossollmaterialDto != null) {
						bestellpositionDto
								.setLossollmaterialIId(aBestellvorschlagDto[i]
										.getIBelegartpositionid());
					}
				}

				bestellpositionDto.setNMaterialzuschlag(new BigDecimal(0));

				if (artikelDto.getMaterialIId() != null) {
					BigDecimal zuschlag = getMaterialFac()
							.materialzuschlagFindAktuellenzuschlag(
									artikelDto.getMaterialIId(), theClientDto);
					if (zuschlag != null) {
						bestellpositionDto.setNMaterialzuschlag(zuschlag);

						if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
							BigDecimal nettogesamtpreis = aBestellvorschlagDto[i]
									.getNNettoeinzelpreis();
							if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
								nettogesamtpreis = nettogesamtpreis
										.subtract(aBestellvorschlagDto[i]
												.getNRabattbetrag());
							}

							nettogesamtpreis = nettogesamtpreis.add(zuschlag);
							aBestellvorschlagDto[i]
									.setNNettogesamtpreis(nettogesamtpreis);

						}

					}

				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
					bestellpositionDto.setNNettoeinzelpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i]
											.getNNettoeinzelpreis(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
				}

				if (aBestellvorschlagDto[i].getNNettogesamtpreis() != null) {
					bestellpositionDto.setNNettogesamtpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i]
											.getNNettogesamtpreis(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNNettogesamtpreis(new BigDecimal(0));
				}
				if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
					bestellpositionDto.setNRabattbetrag(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i].getNRabattbetrag(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNRabattbetrag(new BigDecimal(0));
				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null
						&& !(aBestellvorschlagDto[i].getNNettoeinzelpreis()
								.compareTo(new BigDecimal(0)) == 0)) {
					bestellpositionDto.setDRabattsatz(new Double(
							new BigDecimal(100).subtract(
									Helper.calculateRatioInDecimal(
											aBestellvorschlagDto[i]
													.getNRabattbetrag(),
											aBestellvorschlagDto[i]
													.getNNettoeinzelpreis()))
									.doubleValue()));
				} else {
					bestellpositionDto.setDRabattsatz(new Double(0));
				}

				bestellpositionDto
						.setBNettopreisuebersteuert(aBestellvorschlagDto[i]
								.getBNettopreisuebersteuert());
				bestellpositionDto
						.setTUebersteuerterLiefertermin(aBestellvorschlagDto[i]
								.getTLiefertermin());
				getBestellpositionFac()
						.createBestellposition(
								bestellpositionDto,
								theClientDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
								null);
				this.removeBestellvorschlag(aBestellvorschlagDto[i]);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

	}

	public BestellungDto[] erstelleAbrufbestellungenAusBV(
			BestellvorschlagDto[] bestellvorschlagDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			ArrayList<Integer> alUeberlieferteBestellungIId = new ArrayList<Integer>();
			HashMap<Integer, Integer> hmAbrufeZuRahmen = new HashMap<Integer, Integer>();
			for (int i = 0; i < bestellvorschlagDto.length; i++) {
				boolean bUebergeleitet = false;
				if (bestellvorschlagDto[i].getILieferantId() != null) {
					BigDecimal bdZuBestellendeMenge = bestellvorschlagDto[i]
							.getNZubestellendeMenge();
					Criteria besPos = session
							.createCriteria(FLRBestellposition.class);
					Criteria best = besPos
							.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG);
					Criteria artikel = besPos
							.createCriteria(BestellpositionFac.FLR_BESTELLPOSITION_FLRARTIKEL);
					// Filter auf den Mandanten
					best.add(Restrictions.eq(
							BestellungFac.FLR_BESTELLUNG_MANDANT_C_NR,
							theClientDto.getMandant()));
					// nur Rahmenbestellungen.
					best.add(Restrictions.eq(
							BestellungFac.FLR_BESTELLUNG_BESTELLUNGART_C_NR,
							BestellungFac.BESTELLUNGART_RAHMENBESTELLUNG_C_NR));
					// Einschraenkung nach Status Offen, Erledigt
					Collection<String> cStati = new LinkedList<String>();
					cStati.add(BestellungFac.BESTELLSTATUS_TEILERLEDIGT);
					cStati.add(BestellungFac.BESTELLSTATUS_BESTAETIGT);
					cStati.add(BestellungFac.BESTELLSTATUS_OFFEN);
					best.add(Restrictions.in(
							BestellungFac.FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR,
							cStati));
					best.add(Restrictions
							.eq(BestellungFac.FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE,
									bestellvorschlagDto[i].getILieferantId()));
					besPos.add(Restrictions
							.gt(BestellpositionFac.FLR_BESTELLPOSITION_N_OFFENEMENGE,
									new BigDecimal(0)));
					artikel.add(Restrictions.eq(ArtikelFac.FLR_ARTIKEL_I_ID,
							bestellvorschlagDto[i].getIArtikelId()));
					// Sortierung damit die aelteren Rahmen zuerst abgerufen
					// werden
					best.addOrder(Order
							.desc(BestellungFac.FLR_BESTELLUNG_T_LIEFERTERMIN));
					// Query ausfuehren
					List<?> besposList = besPos.list();
					Iterator<?> besposListIterator = besposList.iterator();
					boolean bMengeGenuegt = false;
					ArrayList<Integer> alVerwendeteBesPosIId = new ArrayList<Integer>();
					while (besposListIterator.hasNext() && !bMengeGenuegt) {
						FLRBestellposition bespos = (FLRBestellposition) besposListIterator
								.next();
						if (bdZuBestellendeMenge.compareTo(bespos
								.getN_offenemenge()) < 0) {
							bMengeGenuegt = true;
						} else {
							bdZuBestellendeMenge = bdZuBestellendeMenge
									.subtract(bespos.getN_offenemenge());
						}
						alVerwendeteBesPosIId.add(bespos.getI_id());
					}
					// Jetzt haben wir eine Liste aller Rahmenpositionen die
					// verwendet werden sollen.
					BigDecimal bdBereitsBestellteMenge = new BigDecimal(0);
					for (int y = 0; y < alVerwendeteBesPosIId.size(); y++) {
						BestellpositionDto besPosDto = getBestellpositionFac()
								.bestellpositionFindByPrimaryKey(
										alVerwendeteBesPosIId.get(y));
						BestellungDto abrufBestellungDto = null;
						if (hmAbrufeZuRahmen.containsKey(besPosDto
								.getBelegIId())) {
							abrufBestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											hmAbrufeZuRahmen.get(besPosDto
													.getBelegIId()));
						}
						BestellungDto rahmenBestellungDto = getBestellungFac()
								.bestellungFindByPrimaryKey(
										besPosDto.getBelegIId());
						boolean bAbrufvorhanden = false;
						// wenn wir bereits durch einen anderen Bestellvorschlag
						// einen Abruf fuer diesen Lieferanten mit diesem
						// Liefertermin angelegt haben
						if (abrufBestellungDto != null) {
							if (abrufBestellungDto.getDLiefertermin().equals(
									bestellvorschlagDto[i].getTLiefertermin())) {
								bAbrufvorhanden = true;
							}
						}
						if (!bAbrufvorhanden) {
							// Wir haben fuer diese Bestellung noch keinen Abruf
							// und legen diesen an
							MandantDto mandantDto = getMandantFac()
									.mandantFindByPrimaryKey(
											theClientDto.getMandant(),
											theClientDto);

							// Wechselkurs ist immer der aktuelle
							BigDecimal wechselkurs = getLocaleFac()
									.getWechselkurs2(
											rahmenBestellungDto
													.getWaehrungCNr(),
											mandantDto.getWaehrungCNr(),
											theClientDto);
							abrufBestellungDto = new BestellungDto();
							abrufBestellungDto
									.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
											wechselkurs.doubleValue()));
							abrufBestellungDto
									.setPersonalIIdAnforderer(rahmenBestellungDto
											.getPersonalIIdAnforderer());
							abrufBestellungDto
									.setPersonalIIdAendern(theClientDto
											.getIDPersonal());
							abrufBestellungDto
									.setPersonalIIdAnlegen(theClientDto
											.getIDPersonal());
							abrufBestellungDto
									.setZahlungszielIId(rahmenBestellungDto
											.getZahlungszielIId());
							abrufBestellungDto
									.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
							abrufBestellungDto
									.setBestellungartCNr(BestellungFac.BESTELLUNGART_ABRUFBESTELLUNG_C_NR);
							abrufBestellungDto
									.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
							abrufBestellungDto
									.setIBestellungIIdRahmenbestellung(rahmenBestellungDto
											.getIId());
							abrufBestellungDto
									.setBTeillieferungMoeglich(rahmenBestellungDto
											.getBTeillieferungMoeglich());
							abrufBestellungDto.setDBelegdatum(this.getDate());
							abrufBestellungDto
									.setDLiefertermin(bestellvorschlagDto[i]
											.getTLiefertermin());
							abrufBestellungDto
									.setKostenstelleIId(rahmenBestellungDto
											.getKostenstelleIId());
							abrufBestellungDto
									.setLieferantIIdBestelladresse(rahmenBestellungDto
											.getLieferantIIdBestelladresse());
							abrufBestellungDto
									.setLieferantIIdRechnungsadresse(rahmenBestellungDto
											.getLieferantIIdRechnungsadresse());
							abrufBestellungDto
									.setSpediteurIId(rahmenBestellungDto
											.getSpediteurIId());
							abrufBestellungDto.setMandantCNr(mandantDto
									.getCNr());
							abrufBestellungDto
									.setLieferartIId(rahmenBestellungDto
											.getLieferartIId());
							abrufBestellungDto
									.setWaehrungCNr(rahmenBestellungDto
											.getWaehrungCNr());
							abrufBestellungDto
									.setFAllgemeinerRabattsatz(rahmenBestellungDto
											.getFAllgemeinerRabattsatz());
							Integer iAbrufBEstellungIId = getBestellungFac()
									.createBestellung(abrufBestellungDto,
											theClientDto);
							abrufBestellungDto = getBestellungFac()
									.bestellungFindByPrimaryKey(
											iAbrufBEstellungIId);
							hmAbrufeZuRahmen.put(besPosDto.getBelegIId(),
									abrufBestellungDto.getIId());
						}
						// Bestellung wurde nun angelgt oder war bereits
						// vorhanden
						// Jetzt die Position anlegen oder updaten
						BestellpositionDto[] vorhandeneAbrufe = getBestellpositionFac()
								.bestellpositionFindByBestellung(
										abrufBestellungDto.getIId(),
										theClientDto);
						BestellpositionDto abrufBestellpositionDto = null;
						for (int x = 0; x < vorhandeneAbrufe.length; x++) {
							if (bestellvorschlagDto[i].getIArtikelId().equals(
									vorhandeneAbrufe[x].getArtikelIId())) {
								abrufBestellpositionDto = vorhandeneAbrufe[x];
							}
						}
						BigDecimal bdbenoetigteMenge = bestellvorschlagDto[i]
								.getNZubestellendeMenge().subtract(
										bdBereitsBestellteMenge);
						BigDecimal bdAbrufmenge;
						if (bdbenoetigteMenge.compareTo(besPosDto
								.getNOffeneMenge()) > 0) {
							if (y == alVerwendeteBesPosIId.size() - 1) {
								// Letzte Pos, also wird ueberliefert
								bdAbrufmenge = bdbenoetigteMenge;
								if (!alUeberlieferteBestellungIId
										.contains(besPosDto.getBelegIId())) {
									alUeberlieferteBestellungIId.add(besPosDto
											.getBelegIId());
								}
							} else {
								bdAbrufmenge = besPosDto.getNOffeneMenge();
							}
						} else {
							bdAbrufmenge = bdbenoetigteMenge;
						}

						if (abrufBestellpositionDto == null) {
							abrufBestellpositionDto = new BestellpositionDto();
							abrufBestellpositionDto.setNMenge(bdAbrufmenge);
							bdbenoetigteMenge = bdbenoetigteMenge
									.subtract(bdAbrufmenge);
							ArtikelDto artikelDto = getArtikelFac()
									.artikelFindByPrimaryKey(
											bestellvorschlagDto[i]
													.getIArtikelId(),
											theClientDto);
							abrufBestellpositionDto
									.setIBestellpositionIIdRahmenposition(besPosDto
											.getIId());
							abrufBestellpositionDto
									.setArtikelIId(bestellvorschlagDto[i]
											.getIArtikelId());
							abrufBestellpositionDto.setBDrucken(Helper
									.boolean2Short(false));
							abrufBestellpositionDto
									.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
							abrufBestellpositionDto
									.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
							abrufBestellpositionDto
									.setBestellungIId(abrufBestellungDto
											.getIId());
							abrufBestellpositionDto.setCBez(artikelDto
									.getArtikelsprDto().getCBez());
							// bestellpositionDto.setISort(new Integer(j++));
							abrufBestellpositionDto.setEinheitCNr(artikelDto
									.getEinheitCNr());
							abrufBestellpositionDto
									.setNNettoeinzelpreis(besPosDto
											.getNNettoeinzelpreis());
							abrufBestellpositionDto
									.setNNettogesamtpreis(besPosDto
											.getNNettogesamtpreis());
							abrufBestellpositionDto.setNRabattbetrag(besPosDto
									.getNRabattbetrag());
							abrufBestellpositionDto.setDRabattsatz(besPosDto
									.getDRabattsatz());
							abrufBestellpositionDto
									.setNMaterialzuschlag(besPosDto
											.getNMaterialzuschlag());
							abrufBestellpositionDto
									.setBNettopreisuebersteuert(besPosDto
											.getBNettopreisuebersteuert());
							abrufBestellpositionDto
									.setTUebersteuerterLiefertermin(bestellvorschlagDto[i]
											.getTLiefertermin());
							getBestellpositionFac()
									.createAbrufbestellposition(
											abrufBestellpositionDto,
											BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
											null, theClientDto);
						} else {
							BigDecimal bdMenge = bdAbrufmenge
									.add(abrufBestellpositionDto.getNMenge());
							abrufBestellpositionDto.setNMenge(bdMenge);
							bdbenoetigteMenge = bdbenoetigteMenge
									.subtract(bdMenge);
							getBestellpositionFac()
									.updateAbrufbestellposition(
											abrufBestellpositionDto,
											BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
											null, theClientDto);

						}
						bUebergeleitet = true;
					}
					if (bUebergeleitet) {
						this.removeBestellvorschlag(bestellvorschlagDto[i]);
					}
				}
			}
			BestellungDto[] toReturn = new BestellungDto[alUeberlieferteBestellungIId
					.size()];
			for (int x = 0; x < alUeberlieferteBestellungIId.size(); x++) {
				toReturn[x] = getBestellungFac().bestellungFindByPrimaryKey(
						alUeberlieferteBestellungIId.get(x));
			}
			return toReturn;
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		} finally {
			session.close();
		}
	}

	/**
	 * hier wird Bestellung und Bestellpositionen befuellt fuer Ueberleitung aus
	 * dem Bestellvorschlag
	 * 
	 * @param aBestellvorschlagDto
	 *            BestellvorschlagDto[]
	 * @param kostenstelleIId
	 *            kostenstelleIId
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void befuellenDerBestellungUndBestellposition(
			BestellvorschlagDto[] aBestellvorschlagDto,
			Integer kostenstelleIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			// isort in Bestellposition
			int j = 0;

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			Integer iIdBestellung = null;
			for (int i = 0; i < aBestellvorschlagDto.length; i++) {

				// anlegen der Bestellung
				LieferantDto lieferantDto = getLieferantFac()
						.lieferantFindByPrimaryKey(
								aBestellvorschlagDto[0].getILieferantId(),
								theClientDto);
				BigDecimal wechselkurs = getLocaleFac().getWechselkurs2(
						lieferantDto.getWaehrungCNr(),
						mandantDto.getWaehrungCNr(), theClientDto);

				// Pro Lieferant und Termin eine eigene Bestellung anlegen

				BestellungDto bestellungDto = new BestellungDto();

				AnsprechpartnerDto ansprechpartnerDto[] = getAnsprechpartnerFac()
						.ansprechpartnerFindByAnsprechpartnerIId(
								lieferantDto.getPartnerIId(), theClientDto);
				if (ansprechpartnerDto.length > 0) {
					bestellungDto.setAnsprechpartnerIId(ansprechpartnerDto[0]
							.getIId());
				}

				PersonalDto anfordererDto = getPersonalFac()
						.personalFindByPrimaryKey(theClientDto.getIDPersonal(),
								theClientDto);
				bestellungDto.setPersonalIIdAnforderer(anfordererDto.getIId());

				bestellungDto.setPersonalIIdAendern(theClientDto
						.getIDPersonal());
				bestellungDto.setPersonalIIdAnlegen(theClientDto
						.getIDPersonal());
				bestellungDto.setZahlungszielIId(lieferantDto
						.getZahlungszielIId());
				bestellungDto.setProjektIId(aBestellvorschlagDto[0]
						.getProjektIId());
				bestellungDto.setBelegartCNr(LocaleFac.BELEGART_BESTELLUNG);
				// bestellungDto.setBestelltextIIdFusstext();
				// bestellungDto.setBestelltextIIdKopftext();
				bestellungDto
						.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
				bestellungDto
						.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);

				ParametermandantDto parameterDto = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_BESTELLUNG,
								ParameterFac.PARAMETER_DEFAULT_TEILLIEFERUNG);

				bestellungDto.setBTeillieferungMoeglich(Helper
						.boolean2Short((Boolean) parameterDto
								.getCWertAsObject()));
				bestellungDto.setDBelegdatum(this.getDate());
				bestellungDto.setDLiefertermin(aBestellvorschlagDto[0]
						.getTLiefertermin());
				bestellungDto.setKostenstelleIId(kostenstelleIId);
				bestellungDto.setLieferantIIdBestelladresse(lieferantDto
						.getIId());
				bestellungDto.setLieferantIIdRechnungsadresse(lieferantDto
						.getIId());

				// Default Lieferadresse ist die Adresse des aktuellen
				// Mandanten
				Integer iPartnerIIdLIeferadresse = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto).getPartnerIIdLieferadresse();
				bestellungDto
						.setPartnerIIdLieferadresse(iPartnerIIdLIeferadresse);

				bestellungDto.setSpediteurIId(lieferantDto.getIdSpediteur());
				bestellungDto.setMandantCNr(mandantDto.getCNr());
				bestellungDto.setLieferartIId(lieferantDto.getLieferartIId());
				bestellungDto.setWaehrungCNr(lieferantDto.getWaehrungCNr());
				bestellungDto.setFAllgemeinerRabattsatz(new Double(0));

				bestellungDto
						.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(
								wechselkurs.doubleValue()));

				iIdBestellung = getBestellungFac().createBestellung(
						bestellungDto, theClientDto);

				// Positionen anlegen.
				BestellpositionDto bestellpositionDto = new BestellpositionDto();

				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKey(
								aBestellvorschlagDto[i].getIArtikelId(),
								theClientDto);

				bestellpositionDto.setArtikelIId(aBestellvorschlagDto[i]
						.getIArtikelId());
				bestellpositionDto.setBDrucken(Helper.boolean2Short(false));
				bestellpositionDto
						.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				bestellpositionDto
						.setBestellpositionstatusCNr(BestellpositionFac.BESTELLPOSITIONSTATUS_OFFEN);
				bestellpositionDto.setBestellungIId(iIdBestellung);
				bestellpositionDto.setCBez(artikelDto.getArtikelsprDto()
						.getCBez());
				bestellpositionDto.setISort(new Integer(j++));
				bestellpositionDto.setEinheitCNr(artikelDto.getEinheitCNr());
				bestellpositionDto.setNMenge(aBestellvorschlagDto[i]
						.getNZubestellendeMenge());

				if (aBestellvorschlagDto[i].getCBelegartCNr() != null
						&& aBestellvorschlagDto[i].getCBelegartCNr().equals(
								LocaleFac.BELEGART_LOS)
						&& aBestellvorschlagDto[i].getIBelegartpositionid() != null) {

					LossollmaterialDto lossollmaterialDto = getFertigungFac()
							.lossollmaterialFindByPrimaryKeyOhneExc(
									aBestellvorschlagDto[i]
											.getIBelegartpositionid());
					if (lossollmaterialDto != null) {
						bestellpositionDto
								.setLossollmaterialIId(aBestellvorschlagDto[i]
										.getIBelegartpositionid());
					}
				}

				bestellpositionDto.setNMaterialzuschlag(new BigDecimal(0));

				if (artikelDto.getMaterialIId() != null) {
					BigDecimal zuschlag = getMaterialFac()
							.materialzuschlagFindAktuellenzuschlag(
									artikelDto.getMaterialIId(), theClientDto);
					if (zuschlag != null) {
						bestellpositionDto.setNMaterialzuschlag(zuschlag);

						if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
							BigDecimal nettogesamtpreis = aBestellvorschlagDto[i]
									.getNNettoeinzelpreis();
							if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
								nettogesamtpreis = nettogesamtpreis
										.subtract(aBestellvorschlagDto[i]
												.getNRabattbetrag());
							}

							nettogesamtpreis = nettogesamtpreis.add(zuschlag);
							aBestellvorschlagDto[i]
									.setNNettogesamtpreis(nettogesamtpreis);

						}

					}

				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null) {
					bestellpositionDto.setNNettoeinzelpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i]
											.getNNettoeinzelpreis(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNNettoeinzelpreis(new BigDecimal(0));
				}
				if (aBestellvorschlagDto[i].getNNettogesamtpreis() != null) {
					bestellpositionDto.setNNettogesamtpreis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i]
											.getNNettogesamtpreis(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNNettogesamtpreis(new BigDecimal(0));
				}
				if (aBestellvorschlagDto[i].getNRabattbetrag() != null) {
					bestellpositionDto.setNRabattbetrag(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									aBestellvorschlagDto[i].getNRabattbetrag(),
									mandantDto.getWaehrungCNr(),
									lieferantDto.getWaehrungCNr(),
									new Date(System.currentTimeMillis()),
									theClientDto));
				} else {
					bestellpositionDto.setNRabattbetrag(new BigDecimal(0));
					aBestellvorschlagDto[i].setNRabattbetrag(new BigDecimal(0));
				}

				if (aBestellvorschlagDto[i].getNNettoeinzelpreis() != null
						&& !(aBestellvorschlagDto[i].getNNettoeinzelpreis()
								.compareTo(new BigDecimal(0)) == 0)) {
					bestellpositionDto.setDRabattsatz(new Double(
							new BigDecimal(100).subtract(
									Helper.calculateRatioInDecimal(
											aBestellvorschlagDto[i]
													.getNRabattbetrag(),
											aBestellvorschlagDto[i]
													.getNNettoeinzelpreis()))
									.doubleValue()));
				} else {
					bestellpositionDto.setDRabattsatz(new Double(0));
				}

				bestellpositionDto
						.setBNettopreisuebersteuert(aBestellvorschlagDto[i]
								.getBNettopreisuebersteuert());

				bestellpositionDto
						.setTUebersteuerterLiefertermin(aBestellvorschlagDto[i]
								.getTLiefertermin());
				getBestellpositionFac()
						.createBestellposition(
								bestellpositionDto,
								theClientDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
								null);
				this.removeBestellvorschlag(aBestellvorschlagDto[i]);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
	}

	public void removeLockDesBestellvorschlagesWennIchIhnSperre(
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(
					BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG,
					theClientDto.getMandant());
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(
						theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim()
								.equals(theClientDto.getIDUser().trim())) {
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
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void pruefeBearbeitenDesBestellvorschlagsErlaubt(
			TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			// lock-objekt zusammenstellen
			LockMeDto lockMeDto = new LockMeDto();
			lockMeDto.setCUsernr(theClientDto.getIDUser());
			lockMeDto.setCWas(theClientDto.getMandant());
			lockMeDto.setCWer(BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG);
			LockMeDto[] lockMeDtoLock = getTheJudgeFac().findByWerWasOhneExc(
					BestellvorschlagFac.LOCKME_BESTELLVORSCHLAG,
					theClientDto.getMandant());
			if (lockMeDtoLock != null && lockMeDtoLock.length > 0) {
				if (lockMeDtoLock[0].getPersonalIIdLocker().equals(
						theClientDto.getIDPersonal())
						&& lockMeDtoLock[0].getCUsernr().trim()
								.equals(theClientDto.getIDUser().trim())) {
					// dann ist er eh durch diesen benutzer auf diesem client
					// gelockt
					return;
				} else {
					ArrayList<Object> al = new ArrayList<Object>();
					al.add(lockMeDtoLock[0].getCUsernr());
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_BESTELLVORSCHLAG_IST_GESPERRT,
							al, new Exception("Bestellvorschlag auf Mandant "
									+ theClientDto.getMandant()
									+ " gesperrt durch Personal Id "
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

	private void verdichteBestellvorschlagNachMindesbestellmengen(
			boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto)
			throws EJBExceptionLP {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			// Zuerst die ID's aller vorkommender Artikel holen.
			String queryString = "select distinct bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID
					+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag."
					+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR
					+ "='" + theClientDto.getMandant() + "'";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			// nun die Eintraege fuer jeden Artikel verdichten.
			for (Iterator<?> itArtikel = rowCountResult.iterator(); itArtikel
					.hasNext();) {
				ArtikelDto artikelDto = getArtikelFac()
						.artikelFindByPrimaryKeySmall(
								(Integer) itArtikel.next(), theClientDto);

				ArrayList<Integer> alProjekte = new ArrayList<Integer>();

				if (bBeruecksichtigeProjektklammer == true) {
					String queryStringProjekte = "select distinct bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID
							+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
							+ artikelDto.getIId()
							+ " AND bestellvorschlag."
							+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR
							+ "='" + theClientDto.getMandant() + "'";
					org.hibernate.Query queryProjekte = session
							.createQuery(queryStringProjekte);
					List<?> resultProjekte = queryProjekte.list();
					Iterator itProjekte = resultProjekte.iterator();
					while (itProjekte.hasNext()) {
						Integer projektIId = (Integer) itProjekte.next();
						alProjekte.add(projektIId);
					}
				} else {
					alProjekte.add(null);
				}

				for (int j = 0; j < alProjekte.size(); j++) {
					Integer projektIId = alProjekte.get(j);

					// Alle BV-Eintraege des Mandanten auflisten.
					Criteria crit = session
							.createCriteria(FLRBestellvorschlag.class);
					// Filter nach Mandant
					crit.add(Restrictions
							.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
									theClientDto.getMandant()));
					// Filter nach Artikel
					crit.add(Restrictions
							.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID,
									artikelDto.getIId()));

					if (bBeruecksichtigeProjektklammer == true) {
						if (projektIId == null) {
							crit.add(Restrictions
									.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID));
						} else {
							crit.add(Restrictions
									.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID,
											projektIId));
						}
					}

					// Sortierung nach Liefertermin.
					crit.addOrder(Order
							.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN));
					List<?> resultList = crit.list();
					// Da die Eintraege zwischendurch auf Mindest- bzw.
					// Verpackungsmengen
					// aufgerundet werden muessen, entwickelt sich zwischendurch
					// ein
					// Guthaben
					BigDecimal bdGuthaben = new BigDecimal(0);
					ArtikellieferantDto artLief = null;
					// nun alle Eintraege durchiterieren.
					for (Iterator<?> itBV = resultList.iterator(); itBV
							.hasNext();) {
						FLRBestellvorschlag bv = (FLRBestellvorschlag) itBV
								.next();
						Bestellvorschlag bvEntity = em.find(
								Bestellvorschlag.class, bv.getI_id());
						if (bvEntity == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_BEI_FIND, "");
						}
						if (bv.getArtikel_i_id() != null
								&& bv.getLieferant_i_id() != null) {
							if (artLief == null) { // lazy loading fuer jeden
								// Artikel.
								artLief = getArtikelFac()
										.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
												bv.getArtikel_i_id(),
												bv.getLieferant_i_id(),
												theClientDto);
							}
							// Wenn der Artikellieferant definiert ist (von dort
							// kommen VPE und Mindestbestellmenge)
							if (artLief != null) {
								if (bdGuthaben.compareTo(bv
										.getN_zubestellendemenge()) >= 0) {
									// Wenn durch Mengenerhoehungen vorheriger
									// Eintraege genug Guthaben
									// "erzielt" wurde, um auf diese Position
									// verzichten zu koennen -> loeschen.
									bdGuthaben = bdGuthaben.subtract(bv
											.getN_zubestellendemenge());
									removeBestellvorschlag(bv.getI_id());
								} else {

									BigDecimal bdMindestbestellmenge = new BigDecimal(
											0);

									if (artLief.getFMindestbestelmenge() != null) {
										// Ist keine Mindestbestellmenge
										// definiert,
										// kann ich deren Behandlung als
										// abgeschlossen betrachten.
										bdMindestbestellmenge = new BigDecimal(
												artLief.getFMindestbestelmenge());
									}

									// Wenn die zu bestellende Menge
									// geringer
									// als die Mindestbestellmenge ist,
									// dann muss ich auf die
									// Mindestbestellmenge
									// erhoehen.
									if (bv.getN_zubestellendemenge().compareTo(
											bdMindestbestellmenge) < 0) {
										BigDecimal bdDifferenz = bdMindestbestellmenge
												.subtract(bv
														.getN_zubestellendemenge());
										bv.setN_zubestellendemenge(bdMindestbestellmenge);
										bdGuthaben = bdGuthaben
												.add(bdDifferenz);
										// speichern
										bvEntity.setNZubestellendemenge(bdMindestbestellmenge);
									}

									// Zuletzt muss noch auf die
									// Verpackungseinheit
									// geprueft werden. (sofern diese definiert
									// ist)
									if (artLief.getNVerpackungseinheit() != null) {
										if (artLief.getNVerpackungseinheit()
												.doubleValue() != 0) {
											// Die zu bestellende Menge muss
											// immer
											// ein Vielfaches der VPE sein.
											int iNachkommastellenMandant = getMandantFac()
													.getNachkommastellenMenge(
															theClientDto
																	.getMandant());
											BigDecimal bdVPE = artLief
													.getNVerpackungseinheit();
											bdVPE = bdVPE.setScale(
													iNachkommastellenMandant,
													RoundingMode.HALF_UP);
											BigDecimal bdMenge = bv
													.getN_zubestellendemenge()
													.setScale(
															iNachkommastellenMandant,
															RoundingMode.HALF_UP);
											BigDecimal bdDivRest = bdMenge
													.divideAndRemainder(
															bdVPE,
															new MathContext(
																	4,
																	RoundingMode.HALF_UP))[1]
													.setScale(
															iNachkommastellenMandant,
															RoundingMode.HALF_UP);
											if (bdDivRest
													.compareTo(new BigDecimal(0)
															.setScale(iNachkommastellenMandant)) != 0) {
												BigDecimal bdDifferenz = bdVPE
														.subtract(bdDivRest);
												bdGuthaben = bdGuthaben
														.add(bdDifferenz);
												// speichern
												bvEntity.setNZubestellendemenge(bvEntity
														.getNZubestellendemenge()
														.add(bdDifferenz));
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
	 * BV-Eintraege eines Mandante verdichten. (Einrechnen der
	 * Verpackungseinheiten und Mindestbestellmengen)
	 * 
	 * 
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void verdichteBestellvorschlag(Long lVerdichtungszeitraum,
			boolean bMindestbestellmengenBeruecksichtigt,
			boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto)
			throws EJBExceptionLP {
		verdichteBestellvorschlagNachDatum(lVerdichtungszeitraum,
				bBeruecksichtigeProjektklammer, theClientDto);
		if (bMindestbestellmengenBeruecksichtigt) {
			verdichteBestellvorschlagNachMindesbestellmengen(
					bBeruecksichtigeProjektklammer, theClientDto);
		}
	}

	private void verdichteBestellvorschlagNachDatum(
			Long lVerdichtungszeitraumparam,
			boolean bBeruecksichtigeProjektklammer, TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;

		session = factory.openSession();
		// Zuerst die ID's aller vorkommender Artikel holen.
		String queryString = "select distinct bestellvorschlag."
				+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID
				+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag."
				+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR + "='"
				+ theClientDto.getMandant() + "'";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> rowCountResult = query.list();
		// nun die Eintraege fuer jeden Artikel verdichten.
		for (Iterator<?> itArtikel = rowCountResult.iterator(); itArtikel
				.hasNext();) {
			ArtikelDto artikelDto = getArtikelFac()
					.artikelFindByPrimaryKeySmall((Integer) itArtikel.next(),
							theClientDto);

			ArrayList<Integer> alProjekte = new ArrayList<Integer>();

			if (bBeruecksichtigeProjektklammer == true) {
				String queryStringProjekte = "select distinct bestellvorschlag."
						+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID
						+ " from FLRBestellvorschlag bestellvorschlag where bestellvorschlag.artikel_i_id="
						+ artikelDto.getIId()
						+ " AND bestellvorschlag."
						+ BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR
						+ "='" + theClientDto.getMandant() + "'";
				org.hibernate.Query queryProjekte = session
						.createQuery(queryStringProjekte);
				List<?> resultProjekte = queryProjekte.list();
				Iterator itProjekte = resultProjekte.iterator();
				while (itProjekte.hasNext()) {
					Integer projektIId = (Integer) itProjekte.next();
					alProjekte.add(projektIId);
				}
			} else {
				alProjekte.add(null);
			}

			for (int j = 0; j < alProjekte.size(); j++) {
				Integer projektIId = alProjekte.get(j);
				// Alle BV-Eintraege des Mandanten auflisten.
				Criteria crit = session
						.createCriteria(FLRBestellvorschlag.class);
				// Filter nach Mandant
				crit.add(Restrictions.eq(
						BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
						theClientDto.getMandant()));
				// Filter nach Artikel
				crit.add(Restrictions.eq(
						BestellvorschlagFac.FLR_BESTELLVORSCHLAG_ARTIKEL_I_ID,
						artikelDto.getIId()));

				if (bBeruecksichtigeProjektklammer == true) {
					if (projektIId == null) {
						crit.add(Restrictions
								.isNull(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID));
					} else {
						crit.add(Restrictions
								.eq(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_PROJEKT_I_ID,
										projektIId));
					}
				}

				// Sortierung nach Liefertermin.
				crit.addOrder(Order
						.asc(BestellvorschlagFac.FLR_BESTELLVORSCHLAG_T_LIEFERTERMIN));
				List<?> resultList = crit.list();
				FLRBestellvorschlag[] bestVorschlaege = (FLRBestellvorschlag[]) resultList
						.toArray(new FLRBestellvorschlag[] {});
				// der erste Bestellvorschlag ist der frueheste
				Bestellvorschlag bvFirst = em.find(Bestellvorschlag.class,
						bestVorschlaege[0].getI_id());
				if (bvFirst == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
				}
				Long lVerdichtungszeitraum = 60 * 60 * 1000 * 24
						* lVerdichtungszeitraumparam;
				// Alle Bestellvorschlaege dieses Artikels,
				// die vor dem Datum sind muessen auf diesem gesammelt werden
				Timestamp dateOfVerdichtung = new Timestamp(bvFirst
						.getTLiefertermin().getTime() + lVerdichtungszeitraum);
				for (int i = 1; i < bestVorschlaege.length; i++) {
					if (bestVorschlaege[i].getT_liefertermin().before(
							dateOfVerdichtung)
							|| bestVorschlaege[i].getT_liefertermin()
									.compareTo(dateOfVerdichtung) == 0) {
						// Bestellvorschlag wird auf vorherigen verdichtet
						bvFirst.setNZubestellendemenge(bvFirst
								.getNZubestellendemenge().add(
										bestVorschlaege[i]
												.getN_zubestellendemenge()));

						// Der Beleg wird geloescht, da das nicht mehr stimmen
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
						System.out.println("verdichtet");
					} else {
						// Der Liefertermin wird die neue Referenz
						// fuer die naechsten Vorschlaege
						bvFirst = em.find(Bestellvorschlag.class,
								bestVorschlaege[i].getI_id());
						dateOfVerdichtung = new Timestamp(bvFirst
								.getTLiefertermin().getTime()
								+ lVerdichtungszeitraum);
					}
				}
			}
		}

	}

	public void uebernimmLieferantAusLieferantOptimieren(
			Integer bestellvorschlagIId, Integer lieferantIIdNeu,
			TheClientDto theClientDto) {
		BestellvorschlagDto bestellvorschlagDto = bestellvorschlagFindByPrimaryKey(bestellvorschlagIId);

		bestellvorschlagDto.setILieferantId(lieferantIIdNeu);

		ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(
				bestellvorschlagDto.getIArtikelId(), lieferantIIdNeu,
				bestellvorschlagDto.getNZubestellendeMenge(),
				theClientDto.getSMandantenwaehrung(),
				Helper.cutDate(new java.sql.Date(System.currentTimeMillis())),
				theClientDto);

		if (alDto != null) {

			bestellvorschlagDto.setNNettoeinzelpreis(alDto.getNEinzelpreis());
			bestellvorschlagDto.setNNettogesamtpreis(alDto.getNNettopreis());

			bestellvorschlagDto.setDRabattsatz(alDto.getFRabatt());
			if (alDto.getNEinzelpreis() != null && alDto.getFRabatt() != null) {
				bestellvorschlagDto.setNRabattbetrag(Helper.rundeKaufmaennisch(
						alDto.getNEinzelpreis().multiply(
								new BigDecimal(alDto.getFRabatt())
										.movePointLeft(2)), 4));
			}
			if (Helper.short2boolean(alDto.getBRabattbehalten())) {
				bestellvorschlagDto.setBNettopreisuebersteuert(Helper
						.boolean2Short(false));
			} else {
				bestellvorschlagDto.setBNettopreisuebersteuert(Helper
						.boolean2Short(true));
			}

			bestellvorschlagDto.setNNettoGesamtPreisMinusRabatte(alDto
					.getNNettopreis());
		}

		updateBestellvorschlag(bestellvorschlagDto);
	}

	public Map getAllLieferantenDesBestellvorschlages(TheClientDto theClientDto) {

		LinkedHashMap<Object, Object> tmLieferanten = new LinkedHashMap<Object, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct bv.lieferant_i_id,bv.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 FROM FLRBestellvorschlag bv WHERE bv.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' ORDER BY bv.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 ASC";
		org.hibernate.Query hqlquery = session.createQuery(queryString);
		List<?> resultList = hqlquery.list();
		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();
			Integer lieferantIId = (Integer) o[0];
			LieferantDto lieferantDto = getLieferantFac()
					.lieferantFindByPrimaryKey(lieferantIId, theClientDto);
			tmLieferanten.put(lieferantIId, lieferantDto.getPartnerDto()
					.formatFixName1Name2());

		}

		return tmLieferanten;
	}

	/**
	 * Spaeter wieder beschaffbare Positionen aus dem BV entfernen.
	 * "Spaeter Wiederbeschaffbar" heisst: Das Datum des naechsten geplanten
	 * Bestellvorschlags + Wiederbeschaffungszeit liegt vor dem Datum, an dem
	 * die Artikel benoetigt werden.
	 * 
	 * @param tNaechsterBV
	 *            Date
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void loescheSpaeterWiederbeschaffbarePositionen(
			java.sql.Date tNaechsterBV, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// Precondition
		if (tNaechsterBV == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tWBMoeglichBis == null"));
		}
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			// Alle BV-Eintraege des Mandanten auflisten.
			Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
			// Filter nach Mandant
			crit.add(Restrictions.eq(
					BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
					theClientDto.getMandant()));
			List<?> resultList = crit.list();
			// Wiederbeschaffungszeit kann in Tagen oder Wochen angegeben
			// werden.
			ParametermandantDto parameterDtoWBZ = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT);
			final int iFaktorWBZ;
			if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				iFaktorWBZ = 7;
			} else if (parameterDtoWBZ.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				iFaktorWBZ = 1;
			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_MANDANTPARAMETER_NICHT_ANGELEGT,
						new Exception(
								ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT
										+ " ist nicht richtig definiert"));
			}
			for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
				FLRBestellvorschlag item = (FLRBestellvorschlag) iter.next();
				if (item.getArtikel_i_id() != null
						&& item.getLieferant_i_id() != null) {
					ArtikellieferantDto artLief = getArtikelFac()
							.artikellieferantFindByArtikellIIdLieferantIIdOhneExc(
									item.getArtikel_i_id(),
									item.getLieferant_i_id(), theClientDto);
					// Wenn der Artikellieferant definiert ist (von dort kommt
					// die Wiederbeschaffungszeit)
					// und wenn auch eine WBZ definiert ist.
					if (artLief != null
							&& artLief.getIWiederbeschaffungszeit() != null) {
						// Wiederbeschaffungszeit in Tagen
						int iWBZInTagen = iFaktorWBZ
								* artLief.getIWiederbeschaffungszeit();
						java.sql.Date tMussBestellenBis = Helper
								.addiereTageZuDatum(item.getT_liefertermin(),
										-iWBZInTagen);
						// Wenn ich den also beim naechsten BV auch noch
						// bestellen kann, dann loesch ich ihn raus
						if (tNaechsterBV.before(tMussBestellenBis)) {
							removeBestellvorschlag(item.getI_id());
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		} finally {
			closeSession(session);
		}
	}

	public void loescheBestellvorschlaegeAbTermin(Date dTermin,
			TheClientDto theClientDto) {
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		session = factory.openSession();
		// Alle BV-Eintraege des Mandanten auflisten.
		Criteria crit = session.createCriteria(FLRBestellvorschlag.class);
		// Filter nach Mandant
		crit.add(Restrictions.eq(
				BestellvorschlagFac.FLR_BESTELLVORSCHLAG_MANDANT_C_NR,
				theClientDto.getMandant()));
		List<?> resultList = crit.list();
		for (Iterator<?> iter = resultList.iterator(); iter.hasNext();) {
			FLRBestellvorschlag item = (FLRBestellvorschlag) iter.next();
			if (item.getT_liefertermin().after(dTermin)) {
				removeBestellvorschlag(item.getI_id());
			}
		}
		closeSession(session);
	}
}
