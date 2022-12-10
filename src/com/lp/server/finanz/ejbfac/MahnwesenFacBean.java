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

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
import org.hibernate.criterion.Restrictions;
import org.modelmapper.ModelMapper;

import com.lp.server.finanz.assembler.MahnlaufDtoAssembler;
import com.lp.server.finanz.assembler.MahnstufeDtoAssembler;
import com.lp.server.finanz.assembler.MahnungDtoAssembler;
import com.lp.server.finanz.ejb.Mahnlauf;
import com.lp.server.finanz.ejb.Mahnstufe;
import com.lp.server.finanz.ejb.MahnstufePK;
import com.lp.server.finanz.ejb.Mahnung;
import com.lp.server.finanz.fastlanereader.generated.FLRFinanzMahnung;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.finanz.service.MahnstufeDto;
import com.lp.server.finanz.service.MahnungDto;
import com.lp.server.finanz.service.MahnwesenFac;
import com.lp.server.finanz.service.SepaXmlExportResult;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.rechnung.bl.ISepaPain008Exporter;
import com.lp.server.rechnung.bl.SepaPain008Exporter;
import com.lp.server.rechnung.ejb.Lastschriftvorschlag;
import com.lp.server.rechnung.ejb.LastschriftvorschlagQuery;
import com.lp.server.rechnung.ejb.Rechnung;
import com.lp.server.rechnung.fastlanereader.generated.FLRLastschriftvorschlag;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungReport;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.LastschriftvorschlagDtoAssembler;
import com.lp.server.rechnung.service.LastschriftvorschlagKomplettDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.ejb.Zahlungsziel;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeSepaExportLastschriftvorschlag;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class MahnwesenFacBean extends Facade implements MahnwesenFac {
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext context;

	public MahnlaufDto createMahnlauf(TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			MahnlaufDto mahnlaufDto = new MahnlaufDto();
			mahnlaufDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_MAHNLAUF));
			mahnlaufDto.setMandantCNr(theClientDto.getMandant());
			mahnlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mahnlaufDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			Mahnlauf mahnlauf = new Mahnlauf(mahnlaufDto.getIId(), mahnlaufDto.getMandantCNr(),
					mahnlaufDto.getPersonalIIdAnlegen(), mahnlaufDto.getPersonalIIdAendern());
			em.persist(mahnlauf);
			em.flush();
			mahnlaufDto.setTAendern(mahnlauf.getTAendern());
			mahnlaufDto.setTAnlegen(mahnlauf.getTAnlegen());
			setMahnlaufFromMahnlaufDto(mahnlauf, mahnlaufDto);
			return mahnlaufDto;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MahnlaufDto createMahnlaufMitMahnvorschlag(TheClientDto theClientDto) throws EJBExceptionLP {
		Session session = null;
		try {
			MahnlaufDto mahnlaufDto = null;
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRRechnungReport.class);
			// Filter nach Mandant.
			c.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_MANDANT_C_NR, theClientDto.getMandant()));
			// Filter nach Stati
			String[] sStati = new String[] { RechnungFac.STATUS_OFFEN, RechnungFac.STATUS_TEILBEZAHLT,
					RechnungFac.STATUS_VERBUCHT };
			c.add(Restrictions.in(RechnungFac.FLR_RECHNUNG_STATUS_C_NR, sStati));

			// PJ 17236
			ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_MAHNUNGEN_AB_GF_JAHR);
			Integer iGFJahrAB = (Integer) p.getCWertAsObject();
			c.add(Restrictions.ge(RechnungFac.FLR_RECHNUNG_I_GESCHAEFTSJAHR, iGFJahrAB));

			c.createAlias(RechnungFac.FLR_RECHNUNG_FLRRECHNUNGART, "ra").add(
					Restrictions.not(Restrictions.eq("ra.rechnungtyp_c_nr", RechnungFac.RECHNUNGTYP_PROFORMARECHNUNG)));

			// Query ausfuehren.
			List<?> listRechnungen = c.list();
			// nur, wenn Rechnungen eingetragen sind.
			if (listRechnungen.size() == 0)
				return mahnlaufDto;

			mahnlaufDto = context.getBusinessObject(MahnwesenFac.class).createMahnlauf(theClientDto);
			for (Iterator<?> iter = listRechnungen.iterator(); iter.hasNext();) {
				FLRRechnungReport flrRechnung = (FLRRechnungReport) iter.next();
				RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(flrRechnung.getI_id());

				if (getMandantFac().hatZusatzfunktionSepaLastschrift(theClientDto)
						&& rechnungDto.getWaehrungCNr().startsWith("EUR")) {
					Zahlungsziel zahlungsziel = em.find(Zahlungsziel.class, rechnungDto.getZahlungszielIId());
					if (Helper.short2boolean(zahlungsziel.getBLastschrift())) {
						createLastschriftImMahnlaufImpl(mahnlaufDto.getIId(), rechnungDto, theClientDto);
						continue;
					}
				}
				mahneWennMahnbarImpl(theClientDto, mahnlaufDto, rechnungDto);
			}

			return mahnlaufDto;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private void createLastschriftImMahnlaufImpl(Integer mahnlaufIId, RechnungDto rechnungDto,
			TheClientDto theClientDto) throws RemoteException {
		Session sessionV = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT lastschrift FROM FLRLastschriftvorschlag as lastschrift "
				+ " WHERE lastschrift.rechnung_i_id=" + rechnungDto.getIId()
				+ " ORDER BY lastschrift.flrmahnlauf.t_anlegen DESC";

		org.hibernate.Query query = sessionV.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();
		BigDecimal summe = BigDecimal.ZERO;
		while (resultListIterator.hasNext()) {
			FLRLastschriftvorschlag flrlastschrift = (FLRLastschriftvorschlag) resultListIterator.next();
			if (flrlastschrift.getT_gespeichert() == null) {
				sessionV.close();
				return;
			}
			summe = summe.add(flrlastschrift.getN_zahlbetrag());
		}

		if (summe.compareTo(rechnungDto.getNGesamtwertinbelegwaehrung()) < 0) {
			LastschriftvorschlagDto lvDto = setupLastschriftvorschlag(mahnlaufIId, rechnungDto, theClientDto);
			lvDto.setNZahlbetrag(lvDto.getNZahlbetrag().subtract(summe));
			context.getBusinessObject(MahnwesenFac.class).createLastschriftvorschlag(lvDto, theClientDto);
		}

		sessionV.close();
	}

	private LastschriftvorschlagDto setupLastschriftvorschlag(Integer mahnlaufIId, RechnungDto rechnungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LastschriftvorschlagDto dto = new LastschriftvorschlagDto();
		dto.setRechnungIId(rechnungDto.getIId());
		dto.setMahnlaufIId(mahnlaufIId);
		dto.setNRechnungsbetrag(rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw()));
		dto.setNBereitsBezahlt(getRechnungFac().getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(), null));
		dto.setNZahlbetrag(dto.getNRechnungsbetrag().subtract(dto.getNBereitsBezahlt()));
		dto.setTFaellig(getMandantFac().berechneZielDatumFuerBelegdatum(rechnungDto.getTBelegdatum(),
				rechnungDto.getZahlungszielIId(), theClientDto));
		dto.setCVerwendungszweck(getDefaultLastschriftVerwendungszweck(rechnungDto, theClientDto));

		return dto;
	}

	private void mahneWennMahnbarImpl(TheClientDto theClientDto, MahnlaufDto mahnlaufDto, RechnungDto rechnungDto)
			throws RemoteException {
		// kann die Rechnung gemahnt werden?
		if (!istRechnungMahnbar(rechnungDto.getIId(), theClientDto)) {
			myLogger.logData(rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr() + " ist nicht mahnbar");
			return;
		}

		myLogger.logData(rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr() + " ist mahnbar");
		Integer mahnstufe = berechneMahnstufeFuerRechnung(rechnungDto, theClientDto);
		if (mahnstufe == null) {
			myLogger.logData(
					rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr() + " muss nicht gemahnt werden");
			return;
		}

		// pruefen, ob diese Mahnstufe schon gemahnt wurde
		try {
			Query query = em.createNamedQuery("MahnungfindByRechnungMahnstufe");
			query.setParameter(1, rechnungDto.getIId());
			query.setParameter(2, mahnstufe);
			// @todo getSingleResult oder getResultList ?
			Mahnung mahnung = (Mahnung) query.getSingleResult();
			myLogger.logData(rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr() + ", Stufe " + mahnstufe
					+ " ist bereits in einem Mahnvorschlag");
		} catch (NoResultException ex1) {

			Integer letzteMahnstufe = getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);

			if (letzteMahnstufe == null || !letzteMahnstufe.equals(mahnstufe)
					|| (letzteMahnstufe != null && letzteMahnstufe == FinanzServiceFac.MAHNSTUFE_99)) {
				myLogger.logData(rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr() + " wird gemahnt");
				MahnungDto mahnungDto = new MahnungDto();
				mahnungDto.setTMahndatum(super.getDate());
				mahnungDto.setMahnlaufIId(mahnlaufDto.getIId());
				mahnungDto.setMahnstufeIId(mahnstufe);
				mahnungDto.setRechnungIId(rechnungDto.getIId());

				mahnungDto.setTGedruckt(null);
				// Mahnsperre
				if (rechnungDto.getTMahnsperrebis() != null
						&& super.getDate().before(rechnungDto.getTMahnsperrebis())) {
					return;
				}

				Integer mahnstufeIId = getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);

				if (mahnstufeIId != null && mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
					mahnungDto.setTGedruckt(getTimestamp());
					mahnungDto.setPersonalIIdGedruckt(theClientDto.getIDPersonal());
				}

				mahnungDto.setTLetztesmahndatum(getAktuellesMahndatumEinerRechnung(rechnungDto.getIId(), theClientDto));
				mahnungDto.setMahnstufeIIdLetztemahnstufe(mahnstufeIId);

				context.getBusinessObject(MahnwesenFac.class).createMahnung(mahnungDto, theClientDto);
			} else {
				myLogger.logData(rechnungDto.getRechnungartCNr() + ": " + rechnungDto.getCNr()
						+ " hat bereits Mahnstufe " + mahnstufe);
			}
		}
	}

	public MahnlaufDto createMahnlaufAusRechnung(Integer rechnungIId, Integer mahnstufeIId, java.sql.Date tMahndatum,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		MahnlaufDto mahnlauf = null;
		if (rechnungIId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("rechnungIId == null"));
		}
		if (mahnstufeIId != null && tMahndatum != null) {
			// wurde die Rechnung bereits mit dieser Mahnstufe gemahnt?
			MahnungDto mahnungDto = mahnungFindByRechnungMahnstufe(rechnungIId, mahnstufeIId);
			if (mahnungDto != null) {
				if (mahnungDto.getTGedruckt() == null) {
					// wenn noch nicht gedruckt -> dann jetzt
					mahneMahnung(mahnungDto.getIId(), theClientDto);
				}
				mahnlauf = mahnlaufFindByPrimaryKey(mahnungDto.getMahnlaufIId());
			} else {
				// wurde noch nicht gemahnt
				MahnlaufDto mahnlaufDto = createMahnlauf(theClientDto);
				MahnungDto mahnungDtoNew = new MahnungDto();
				mahnungDtoNew.setMahnlaufIId(mahnlaufDto.getIId());
				mahnungDtoNew.setMahnstufeIId(mahnstufeIId);
				mahnungDtoNew.setMahnstufeIIdLetztemahnstufe(null); // spaeter
				mahnungDtoNew.setRechnungIId(rechnungIId);
				mahnungDtoNew.setTMahndatum(tMahndatum);
				// speichern
				mahnungDtoNew = createMahnung(mahnungDtoNew, theClientDto);
				// und mahnen
				mahneMahnlauf(mahnlaufDto.getIId(), false, theClientDto);
				mahnlauf = mahnlaufDto;
			}
		} else if (mahnstufeIId == null && tMahndatum == null) {
			MahnungDto[] mahnungDtos = mahnungFindByRechnungIId(rechnungIId);
			for (int i = mahnungDtos.length - 1; i >= 0; i--) {
				mahneMahnungRueckgaengig(mahnungDtos[i].getIId(), theClientDto);
			}
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("mahnstufeIId==null || tMahndatum==null"));
		}
		return mahnlauf;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removeMahnlauf(MahnlaufDto mahnlaufDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Validator.dtoNotNull(mahnlaufDto, "mahnlaufDto");

		removeMahnlaufImpl(mahnlaufDto, theClientDto, false);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void removeMahnlaufIgnoriereGespeicherteLastschriften(MahnlaufDto mahnlaufDto, TheClientDto theClientDto)
			throws RemoteException {
		Validator.dtoNotNull(mahnlaufDto, "mahnlaufDto");

		removeMahnlaufImpl(mahnlaufDto, theClientDto, true);
	}

	/**
	 * @param mahnlaufDto
	 * @param theClientDto
	 * @throws RemoteException
	 */
	private void removeMahnlaufImpl(MahnlaufDto mahnlaufDto, TheClientDto theClientDto,
			boolean ignoreSavedLastschriften) throws RemoteException {
		Integer iId = mahnlaufDto.getIId();
		// zuerst die angehaengten Mahnungen loeschen
		MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(iId);
		for (int i = 0; i < mahnungen.length; i++) {
			// es darf noch keine der Mahnungen gedruckt sein.
			if (mahnungen[i].getTGedruckt() != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN, "");
			}
		}

		removeLastschriftvorschlagByMahnlaufIIdImpl(iId, theClientDto, ignoreSavedLastschriften);

		// Einzelmahnungen loeschen
		for (int i = 0; i < mahnungen.length; i++) {
			getMahnwesenFac().removeMahnung(mahnungen[i], theClientDto);
		}

		getMahnwesenFac().removeMahnlauf(iId);
	}

	/**
	 * @param mahnlaufIId
	 * @param theClientDto
	 * @param ignoreSavedLastschriften
	 */
	private void removeLastschriftvorschlagByMahnlaufIIdImpl(Integer mahnlaufIId, TheClientDto theClientDto,
			boolean ignoreSavedLastschriften) {
		if (!getMandantFac().hatZusatzfunktionSepaLastschrift(theClientDto))
			return;

		List<Lastschriftvorschlag> lastschriften = LastschriftvorschlagQuery.listByMahnlaufIId(em, mahnlaufIId);
		if (!ignoreSavedLastschriften) {
			for (Lastschriftvorschlag lastschrift : lastschriften) {
				if (lastschrift.gettGespeichert() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_LASTSCHRIFTVORSCHLAEGE_SCHON_GESPEICHERT, "");
				}
			}
		}

		for (Lastschriftvorschlag lastschrift : lastschriften) {
			getMahnwesenFac().removeLastschriftvorschlag(lastschrift.getiId());
		}

	}

	public void removeMahnlauf(Integer iMahnlaufIId) {
		// Mahnlauf loeschen
		// try {
		Mahnlauf toRemove = em.find(Mahnlauf.class, iMahnlaufIId);
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

	public void updateMahnlauf(MahnlaufDto mahnlaufDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		if (mahnlaufDto != null) {
			Integer iId = mahnlaufDto.getIId();
			mahnlaufDto.setTAendern(super.getTimestamp());
			mahnlaufDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			Mahnlauf mahnlauf = em.find(Mahnlauf.class, iId);
			if (mahnlauf == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setMahnlaufFromMahnlaufDto(mahnlauf, mahnlaufDto);
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
		// }
	}

	public MahnlaufDto mahnlaufFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Mahnlauf mahnlauf = em.find(Mahnlauf.class, iId);
		if (mahnlauf == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMahnlaufDto(mahnlauf);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
	}

	public MahnlaufDto[] mahnlaufFindByMandantCNr(String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("MahnlauffindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){ // @ToDo null Pruefung?
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		return assembleMahnlaufDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// return null;
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	private void setMahnlaufFromMahnlaufDto(Mahnlauf mahnlauf, MahnlaufDto mahnlaufDto) {
		mahnlauf.setMandantCNr(mahnlaufDto.getMandantCNr());
		mahnlauf.setPersonalIIdAnlegen(mahnlaufDto.getPersonalIIdAnlegen());
		mahnlauf.setTAnlegen(mahnlaufDto.getTAnlegen());
		mahnlauf.setPersonalIIdAendern(mahnlaufDto.getPersonalIIdAendern());
		mahnlauf.setTAendern(mahnlaufDto.getTAendern());
		em.merge(mahnlauf);
		em.flush();
	}

	private MahnlaufDto assembleMahnlaufDto(Mahnlauf mahnlauf) {
		return MahnlaufDtoAssembler.createDto(mahnlauf);
	}

	private MahnlaufDto[] assembleMahnlaufDtos(Collection<?> mahnlaufs) {
		List<MahnlaufDto> list = new ArrayList<MahnlaufDto>();
		if (mahnlaufs != null) {
			Iterator<?> iterator = mahnlaufs.iterator();
			while (iterator.hasNext()) {
				Mahnlauf mahnlauf = (Mahnlauf) iterator.next();
				list.add(assembleMahnlaufDto(mahnlauf));
			}
		}
		MahnlaufDto[] returnArray = new MahnlaufDto[list.size()];
		return (MahnlaufDto[]) list.toArray(returnArray);
	}

	public Integer createMahnstufe(MahnstufeDto mahnstufeDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnstufeDto);
		mahnstufeDto.setMandantCNr(theClientDto.getMandant());
		try {
			Mahnstufe mahnstufe = new Mahnstufe(mahnstufeDto.getIId(), mahnstufeDto.getMandantCNr(),
					mahnstufeDto.getITage());
			em.persist(mahnstufe);
			em.flush();
			setMahnstufeFromMahnstufeDto(mahnstufe, mahnstufeDto);
			return mahnstufeDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeMahnstufe(MahnstufeDto mahnstufeDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (mahnstufeDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL, new Exception("mahnstufeDto == null"));
		}
		if (mahnstufeDto != null) {
			// try {
			Integer iId = mahnstufeDto.getIId();
			if (iId.intValue() == FinanzServiceFac.MAHNSTUFE_1 || iId.intValue() == FinanzServiceFac.MAHNSTUFE_2
					|| iId.intValue() == FinanzServiceFac.MAHNSTUFE_3
					|| iId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_MAHNSTUFEN_1_2_3_DUERFEN_NICHT_GELOESCHT_WERDEN,
						new Exception("Mahnstufe " + iId.intValue() + " darf nicht geloescht werden"));
			}
			Mahnstufe toRemove = em.find(Mahnstufe.class, new MahnstufePK(iId, mahnstufeDto.getMandantCNr()));
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
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public void updateMahnstufe(MahnstufeDto mahnstufeDto, TheClientDto theClientDto) throws EJBExceptionLP {
		if (mahnstufeDto != null) {
			// try {
			Mahnstufe mahnstufe = em.find(Mahnstufe.class,
					new MahnstufePK(mahnstufeDto.getIId(), mahnstufeDto.getMandantCNr()));
			if (mahnstufe == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
			}
			setMahnstufeFromMahnstufeDto(mahnstufe, mahnstufeDto);
			// }
			// catch (FinderException e) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
			// }
		}
	}

	public MahnstufeDto mahnstufeFindByPrimaryKey(MahnstufePK mahnstufePK) throws EJBExceptionLP {
		// try {
		Mahnstufe mahnstufe = em.find(Mahnstufe.class, mahnstufePK);
		if (mahnstufe == null) {
			return null;
		}
		return assembleMahnstufeDto(mahnstufe);

		// }
		// catch (FinderException e) {
		// return null;
		// }
	}

	public MahnstufeDto[] mahnstufeFindByMandantCNr(String mandantCNr) throws EJBExceptionLP {
		// try {
		Query query = em.createNamedQuery("MahnstufefindByMandantCNr");
		query.setParameter(1, mandantCNr);
		Collection<?> c = query.getResultList();
		// if (c.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, null);
		// }
		Collection<Mahnstufe> c2 = new LinkedList<Mahnstufe>();
		for (Iterator<?> iter = c.iterator(); iter.hasNext();) {
			Mahnstufe item = (Mahnstufe) iter.next();
			// if(item.getIId().intValue()!=FinanzServiceFac.MAHNSTUFE_99) {
			c2.add(item);
			// }
		}
		return assembleMahnstufeDtos(c2);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL, e);
		// }
	}

	public LinkedHashMap<Integer, Integer> getAllMahnstufe(String mandantCNr) throws EJBExceptionLP {
		LinkedHashMap<Integer, Integer> uMap = new LinkedHashMap<Integer, Integer>();
		MahnstufeDto[] mahnstufen = mahnstufeFindByMandantCNr(mandantCNr);
		for (int i = 0; i < mahnstufen.length; i++) {
			uMap.put(mahnstufen[i].getIId(), mahnstufen[i].getIId());
		}
		return uMap;
	}

	private void setMahnstufeFromMahnstufeDto(Mahnstufe mahnstufe, MahnstufeDto mahnstufeDto) {
		mahnstufe.setITage(mahnstufeDto.getITage());
		mahnstufe.setNMahnspesen(mahnstufeDto.getNMahnspesen());
		mahnstufe.setFZinssatz(mahnstufeDto.getFZinssatz());
		em.merge(mahnstufe);
		em.flush();
	}

	private MahnstufeDto assembleMahnstufeDto(Mahnstufe mahnstufe) {
		return MahnstufeDtoAssembler.createDto(mahnstufe);
	}

	private MahnstufeDto[] assembleMahnstufeDtos(Collection<?> mahnstufes) {
		List<MahnstufeDto> list = new ArrayList<MahnstufeDto>();
		if (mahnstufes != null) {
			Iterator<?> iterator = mahnstufes.iterator();
			while (iterator.hasNext()) {
				Mahnstufe mahnstufe = (Mahnstufe) iterator.next();
				list.add(assembleMahnstufeDto(mahnstufe));
			}
		}
		MahnstufeDto[] returnArray = new MahnstufeDto[list.size()];
		return (MahnstufeDto[]) list.toArray(returnArray);
	}

	public MahnungDto createMahnung(MahnungDto mahnungDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		try {
			mahnungDto.setIId(getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_MAHNUNG));
			mahnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mahnungDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			mahnungDto.setMandantCNr(theClientDto.getMandant());
			Mahnung mahnung = new Mahnung(mahnungDto.getIId(), mahnungDto.getMandantCNr(), mahnungDto.getMahnlaufIId(),
					mahnungDto.getMahnstufeIId(), mahnungDto.getRechnungIId(), mahnungDto.getTMahndatum(),
					mahnungDto.getPersonalIIdAnlegen(), mahnungDto.getPersonalIIdAendern());
			em.persist(mahnung);
			em.flush();
			mahnung.setTLetztesmahndatum(mahnungDto.getTLetztesmahndatum());
			mahnungDto.setTAendern(mahnung.getTAendern());
			mahnungDto.setTAnlegen(mahnung.getTAnlegen());
			setMahnungFromMahnungDto(mahnung, mahnungDto);
			return mahnungDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public Integer getAktuelleMahnstufeEinerRechnung(Integer rechnungIId, TheClientDto theClientDto) {
		Integer mahnstufeIId = null;
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT  MAX(m.mahnstufe_i_id) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.i_id=  "
				+ rechnungIId + " AND m.t_gedruckt IS NOT NULL";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {
			mahnstufeIId = (Integer) resultListIterator.next();
		}
		return mahnstufeIId;
	}

	public java.sql.Date getAktuellesMahndatumEinerRechnung(Integer rechnungIId, TheClientDto theClientDto) {
		java.sql.Date date = null;
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT  MAX(m.flrmahnlauf.t_anlegen) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.i_id=  "
				+ rechnungIId + " AND m.t_gedruckt IS NOT NULL";
		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();
		Iterator<?> resultListIterator = resultList.iterator();
		if (resultListIterator.hasNext()) {

			java.util.Date utilDate = (java.util.Date) resultListIterator.next();
			if (utilDate != null) {
				date = new java.sql.Date(utilDate.getTime());
			}
		}
		return date;
	}

	public void removeMahnung(MahnungDto mahnungDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		if (mahnungDto != null) {
			if (mahnungDto.getTGedruckt() != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_MAHNLAUF_WURDE_SCHON_UEBERNOMMEN, "");
			}
			Integer iId = mahnungDto.getIId();
			// try {
			Mahnung toRemove = em.find(Mahnung.class, iId);
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
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
			// ex);
			// }
		}
	}

	public MahnungDto updateMahnung(MahnungDto mahnungDto, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(mahnungDto, theClientDto.getIDUser());
		// try {
		Integer iId = mahnungDto.getIId();
		mahnungDto.setTAendern(super.getTimestamp());
		mahnungDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		Mahnung mahnung = em.find(Mahnung.class, iId);
		if (mahnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setMahnungFromMahnungDto(mahnung, mahnungDto);
		return mahnungDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, e);
		// }
	}

	public MahnungDto mahnungFindByPrimaryKey(Integer iId) throws EJBExceptionLP {
		// try {
		Mahnung mahnung = em.find(Mahnung.class, iId);
		if (mahnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMahnungDto(mahnung);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public MahnungDto[] mahnungFindByMahnlaufIId(Integer mahnlaufIId) throws EJBExceptionLP {
		MahnungDto[] mahnungen = null;
		// try {
		Query query = em.createNamedQuery("MahnungfindByMahnlaufIId");
		query.setParameter(1, mahnlaufIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		mahnungen = assembleMahnungDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// nothing here
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return mahnungen;
	}

	public MahnungDto[] mahnungFindByRechnungIId(Integer rechnungIId) throws EJBExceptionLP {
		MahnungDto[] mahnungen = null;
		// try {
		Query query = em.createNamedQuery("MahnungfindByRechnungIId");
		query.setParameter(1, rechnungIId);
		// @todo getSingleResult oder getResultList ?
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		mahnungen = assembleMahnungDtos(cl);

		// }
		// catch (javax.ejb.ObjectNotFoundException ex) {
		// nothing here
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
		return mahnungen;
	}

	public MahnungDto mahnungFindByRechnungMahnstufe(Integer rechnungIId, Integer mahnstufeIId) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("MahnungfindByRechnungMahnstufe");
			query.setParameter(1, rechnungIId);
			query.setParameter(2, mahnstufeIId);
			Mahnung mahnung = (Mahnung) query.getSingleResult();
			return assembleMahnungDto(mahnung);

			// }
			// catch (javax.ejb.ObjectNotFoundException ex) {
			// return null;
		} catch (NoResultException ex) {
			return null;
		}
	}

	private void setMahnungFromMahnungDto(Mahnung mahnung, MahnungDto mahnungDto) {
		mahnung.setMahnlaufIId(mahnungDto.getMahnlaufIId());
		mahnung.setMahnstufeIId(mahnungDto.getMahnstufeIId());
		mahnung.setRechnungIId(mahnungDto.getRechnungIId());
		mahnung.setTMahndatum(mahnungDto.getTMahndatum());
		mahnung.setTGedruckt(mahnungDto.getTGedruckt());
		mahnung.setPersonalIIdGedruckt(mahnungDto.getPersonalIIdGedruckt());
		mahnung.setTAnlegen(mahnungDto.getTAnlegen());
		mahnung.setPersonalIIdAnlegen(mahnungDto.getPersonalIIdAnlegen());
		mahnung.setTAendern(mahnungDto.getTAendern());
		mahnung.setPersonalIIdAendern(mahnungDto.getPersonalIIdAendern());
		mahnung.setMahnstufeIIdLetztemahnstufe(mahnungDto.getMahnstufeIIdLetztemahnstufe());
		mahnung.setTLetztesmahndatum(mahnungDto.getTLetztesmahndatum());
		mahnung.setMandantCNr(mahnungDto.getMandantCNr());
		em.merge(mahnung);
		em.flush();
	}

	private MahnungDto assembleMahnungDto(Mahnung mahnung) {
		return MahnungDtoAssembler.createDto(mahnung);
	}

	private MahnungDto[] assembleMahnungDtos(Collection<?> mahnungs) {
		List<MahnungDto> list = new ArrayList<MahnungDto>();
		if (mahnungs != null) {
			Iterator<?> iterator = mahnungs.iterator();
			while (iterator.hasNext()) {
				Mahnung mahnung = (Mahnung) iterator.next();
				list.add(assembleMahnungDto(mahnung));
			}
		}
		MahnungDto[] returnArray = new MahnungDto[list.size()];
		return (MahnungDto[]) list.toArray(returnArray);
	}

	public int getMahntageVonMahnstufe(Integer mahnstufeIId, TheClientDto theClientDto) {
		int tage = 0;
		MahnstufeDto[] alle = mahnstufeFindByMandantCNr(theClientDto.getMandant());
		for (int i = 0; i < alle.length; i++) {
			MahnstufeDto mahnstufe = alle[i];
			if (mahnstufe.getIId().intValue() <= mahnstufeIId.intValue()) {
				tage = tage + mahnstufe.getITage().intValue();
			}
		}
		return tage;
	}

	/**
	 * Berechnen, in welche Mahnstufe eine Rechnung faellt.
	 * 
	 * @param rechnungDto  RechnungDto
	 * @param theClientDto String
	 * @return Integer mahnstufe bzw. null, wenn noch keine mahnstufe erreicht
	 */
	public Integer berechneMahnstufeFuerRechnung(RechnungDto rechnungDto, TheClientDto theClientDto) {
		try {
			Integer zahlungszielIId = rechnungDto.getZahlungszielIId();
			if (zahlungszielIId == null) {
				// Rechnungen ohne Zahlungsziel werden nicht gemahnt
				return null;
			}

			Integer mahnstufeIId = getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);
			java.sql.Date mahndatum = getAktuellesMahndatumEinerRechnung(rechnungDto.getIId(), theClientDto);

			if (mahnstufeIId != null) {
				// Rechnungen der letzten Mahnstufe werden nicht mehr weiter
				// gemahnt
				if (mahnstufeIId.intValue() == FinanzServiceFac.MAHNSTUFE_99) {
					return FinanzServiceFac.MAHNSTUFE_99;
				}
			}
			MahnstufeDto[] mahnstufen = mahnstufeFindByMandantCNr(theClientDto.getMandant());
			if (mahnstufen == null || mahnstufen.length < FinanzServiceFac.MAHNSTUFE_3) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN, new Exception(
						"Fuer Mandant " + theClientDto.getMandant() + " sind keine Mahnstufen eingetragen"));
			}
			java.sql.Date dAusgangsdatum;

			// wurde die Rechnung schon gemahnt?
			if (mahnstufeIId != null && mahndatum != null) {
				// vom letzten mahndatum ausgehen
				dAusgangsdatum = mahndatum;
			} else {
				ZahlungszielDto zahlungszielDto = getMandantFac()
						.zahlungszielFindByPrimaryKey(rechnungDto.getZahlungszielIId(), theClientDto);

				// SP18305
				if (Helper.short2boolean(zahlungszielDto.getBStichtag()) == true) {
					dAusgangsdatum = getMandantFac().berechneFaelligkeitAnhandStichtag(
							new java.sql.Date(rechnungDto.getTBelegdatum().getTime()), zahlungszielDto, theClientDto);

				} else {
					int iNettotage;
					if (zahlungszielDto.getAnzahlZieltageFuerNetto() != null) {
						iNettotage = zahlungszielDto.getAnzahlZieltageFuerNetto().intValue();
					} else {
						iNettotage = 0;
					}
					// Belegdatum + Zahlungsziel
					dAusgangsdatum = Helper.addiereTageZuDatum(rechnungDto.getTBelegdatum(), iNettotage);
				}

			}
			int tageSeitAusgangsdatum = Helper.getDifferenzInTagen(dAusgangsdatum, super.getDate());

			if (rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
				tageSeitAusgangsdatum = 9999;
			}

			if (mahnstufeIId == null) {
				// eventuell erste Mahnung
				int iTageMS1 = 0;
				for (int i = 0; i < mahnstufen.length; i++) {
					if (mahnstufen[i].getIId().intValue() == FinanzServiceFac.MAHNSTUFE_1) {
						if (mahnstufen[i].getITage() != null) {
							iTageMS1 = mahnstufen[i].getITage().intValue();
						}
						if (tageSeitAusgangsdatum >= iTageMS1) {
							return mahnstufen[i].getIId();
						}
					}
				}
			} else {
				// n'te Mahnung
				int iTage = 0;
				for (int i = 0; i < mahnstufen.length; i++) {
					// ich suche die naechstgroessere Mahnstufe
					if (mahnstufen[i].getIId().intValue() > mahnstufeIId.intValue()) {
						if (mahnstufen[i].getITage() != null) {
							iTage = mahnstufen[i].getITage().intValue();
						}
						if (tageSeitAusgangsdatum >= iTage) {
							return mahnstufen[i].getIId();
						} else {
							return null;
						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return null;
	}

	/**
	 * Eine Mahnung aus einem Mahnlauf durchfuehren.
	 * 
	 * @param mahnungIId   Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void mahneMahnung(Integer mahnungIId, TheClientDto theClientDto) throws EJBExceptionLP {
		try {
			Mahnung mahnung = em.find(Mahnung.class, mahnungIId);
			if (mahnung == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
			if (mahnung.getTGedruckt() == null) {
				RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(mahnung.getRechnungIId());
				// Stornierte nicht mahnen.
				if (!rechnungDto.getStatusCNr().equals(RechnungFac.STATUS_STORNIERT)) {
					// nur dann mahnen, wenn die Rechnung keine mahnsperre har
					// oder diese schon vorbei ist.
					if (rechnungDto.getTMahnsperrebis() == null || rechnungDto.getTMahnsperrebis().before(getDate())) {
						// die neue Mahnstufe muss groesser sein als die alte
						// gleich kann sie auch sein, wegen dem drucken

						Integer mahnstufeIId = getAktuelleMahnstufeEinerRechnung(rechnungDto.getIId(), theClientDto);
						java.sql.Date mahndatum = getAktuellesMahndatumEinerRechnung(rechnungDto.getIId(),
								theClientDto);
						if (mahnstufeIId == null || mahnstufeIId.intValue() <= mahnung.getMahnstufeIId().intValue()) {
							// Alten Mahnstatus der Rechnung sichern
							mahnung.setMahnstufeIIdLetztemahnstufe(mahnstufeIId);
							mahnung.setTLetztesmahndatum(mahndatum);

							// jetzt kann die Mahnung als gemahnt markiert
							// werden
							mahnung.setTGedruckt(getTimestamp());

							ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_RECHNUNG, ParameterFac.PARAMETER_LIEFERSPERRE_AB_MAHNSTUFE);
							Integer iSperreAbMahnstufe = (Integer) p.getCWertAsObject();

							if (mahnung.getMahnstufeIId() >= iSperreAbMahnstufe
									&& !rechnungDto.getRechnungartCNr().equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
								Kunde k = em.find(Kunde.class, rechnungDto.getKundeIId());
								if (k.getTLiefersperream() == null) {
									MahnlaufDto mlDto = mahnlaufFindByPrimaryKey(mahnung.getMahnlaufIId());
									k.setTLiefersperream(new Date(mlDto.getTAnlegen().getTime()));
									em.merge(k);
									em.flush();
								}
							}

							mahnung.setPersonalIIdGedruckt(theClientDto.getIDPersonal());
						} else {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE,
									new Exception("Die Rechnung " + rechnungDto.getCNr()
											+ " darf nicht gemahnt werden: alte Mahnstufe=" + mahnstufeIId
											+ ", neue Mahnstufe=" + mahnung.getMahnstufeIId()));
						}
					} else {
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_RECHNUNG_MAHNSPERRE,
								new Exception("Die Rechnung " + rechnungDto.getCNr()
										+ " darf nicht gemahnt werden: Mahnsperre bis "
										+ rechnungDto.getTMahnsperrebis()));
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Eine Mahnung aus einem Mahnlauf durchfuehren.
	 * 
	 * @param mahnungIId   Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 */
	public void mahneMahnungRueckgaengig(Integer mahnungIId, TheClientDto theClientDto) throws EJBExceptionLP {

		Mahnung mahnung = em.find(Mahnung.class, mahnungIId);
		if (mahnung == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}

		Rechnung rechnung = em.find(Rechnung.class, mahnung.getRechnungIId());

		// nur wenn ich auch die richtige mahnung dazu hab.

		Integer mahnstufeIId = getAktuelleMahnstufeEinerRechnung(rechnung.getIId(), theClientDto);

		if (mahnstufeIId == null || rechnung.getTMahnsperrebis() != null || // dieser
																			// fall
																			// sollte
		// nicht eintreten.
				(mahnstufeIId != null && mahnung.getMahnstufeIId() != null && mahnung.getTGedruckt() != null
						&& mahnstufeIId.equals(mahnung.getMahnstufeIId()))) {
			// Alten Mahnstatus loeschen
			mahnung.setMahnstufeIIdLetztemahnstufe(null);
			mahnung.setTLetztesmahndatum(null);
			// jetzt kann die Mahnung als gemahnt markiert werden
			mahnung.setTGedruckt(null);
			mahnung.setPersonalIIdGedruckt(null);
		}

		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes mahnen. mahnt schrittweise nur die Mahnungen
	 * eines Kunden und gibt desse Id zurueck gibt null zurueck, wenn alle Mahnungen
	 * des Kunden gemahnt sind
	 * 
	 * @param mahnlaufIId  Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void mahneMahnlauf(Integer mahnlaufIId, boolean ignoriereExceptionMahnstufeGroesserAlsDieAlte,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufIId);
		for (int i = 0; i < mahnungen.length; i++) {
			try {
				getMahnwesenFac().mahneMahnung(mahnungen[i].getIId(), theClientDto);
			} catch (EJBExceptionLP ex1) {
				if (ex1.getCode() == EJBExceptionLP.FEHLER_RECHNUNG_NEUE_MAHNSTUFE_MUSS_GROESSER_SEIN_ALS_DIE_ALTE) {
					if (ignoriereExceptionMahnstufeGroesserAlsDieAlte) {
						// SP7678
					} else {
						throw ex1;
					}
					// nothing here
				} else {
					throw ex1;
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void mahneMahnlauf(Integer mahnlaufIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		mahneMahnlauf(mahnlaufIId, true, theClientDto);
	}

	/**
	 * Alle Mahnungen eines Mahnlaufes mahnen.
	 * 
	 * @param mahnlaufIId  Integer
	 * @param theClientDto String
	 * @throws EJBExceptionLP
	 * @throws RemoteException
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void mahneMahnlaufRueckgaengig(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufIId);
		for (int i = 0; i < mahnungen.length; i++) {
			getMahnwesenFac().mahneMahnungRueckgaengig(mahnungen[i].getIId(), theClientDto);
		}

		List<LastschriftvorschlagDto> lvDtos = lastschriftvorschlagFindByMahnlaufIId(mahnlaufIId);
		for (LastschriftvorschlagDto lastschriftvorschlag : lvDtos) {
			getMahnwesenFac().macheLastschriftvorschlagRueckgaengig(lastschriftvorschlag.getIId());
		}
	}

	public void macheLastschriftvorschlagRueckgaengig(Integer lastschriftvorschlagIId) {
		Lastschriftvorschlag lastschriftvorschlag = em.find(Lastschriftvorschlag.class, lastschriftvorschlagIId);
		if (lastschriftvorschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
					"lastschriftvorschlag, iId=" + String.valueOf(lastschriftvorschlagIId));
		}

		if (lastschriftvorschlag.gettGespeichert() != null) {
			lastschriftvorschlag.settGespeichert(null);
		}
	}

	public java.sql.Date berechneFaelligkeitsdatumFuerMahnstufe(java.util.Date dBelegdatum, Integer zahlungszielIId,
			Integer mahnstufeIId, TheClientDto theClientDto) {
		try {
			ZahlungszielDto zzDto = getMandantFac().zahlungszielFindByPrimaryKey(zahlungszielIId, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return null;
	}

	public int anzahlDerOffenenRechnungenMitMahnstufeGroesser(Integer kundeIId, int iMahnstufe,
			TheClientDto theClientDto) {

		int iAnzahl = 0;
		Session sessionV = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT count(distinct m.flrrechnungreport.i_id) FROM FLRFinanzMahnung as m  WHERE m.flrrechnungreport.kunde_i_id= "
				+ kundeIId + " AND m.mahnstufe_i_id>=" + iMahnstufe
				+ " AND m.t_gedruckt IS NOT NULL AND m.flrrechnungreport.status_c_nr IN ('" + RechnungFac.STATUS_OFFEN
				+ "','" + RechnungFac.STATUS_TEILBEZAHLT + "')";

		org.hibernate.Query query = sessionV.createQuery(queryString);

		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		if (resultListIterator.hasNext()) {
			Long l = (Long) resultListIterator.next();
			if (l != null) {
				iAnzahl = l.intValue();
			}
		}
		return iAnzahl;

	}

	public Boolean bGibtEsEinenOffenenMahnlauf(String mandantCNr, TheClientDto theClientDto) {
		MahnlaufDto[] mahnlaufDtos = mahnlaufFindByMandantCNr(mandantCNr);
		for (int i = 0; i < mahnlaufDtos.length; i++) {
			MahnungDto[] mahnungen = mahnungFindByMahnlaufIId(mahnlaufDtos[i].getIId());
			for (int j = 0; j < mahnungen.length; j++) {
				if (mahnungen[j].getTGedruckt() == null) {
					return Boolean.TRUE;
				}
			}

			if (getMandantFac().hatZusatzfunktionSepaLastschrift(theClientDto)) {
				Boolean isLastschriftOffen = isLastschriftvorschlagOffen(mahnlaufDtos[i].getIId());
				if (Boolean.TRUE.equals(isLastschriftOffen))
					return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public boolean istRechnungMahnbar(Integer rechnungIId, TheClientDto theClientDto) {
		try {
			RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKey(rechnungIId);
			String statusCNr = rechnungDto.getStatusCNr();
			if (statusCNr.equals(RechnungFac.STATUS_TEILBEZAHLT) || statusCNr.equals(RechnungFac.STATUS_OFFEN)
					|| statusCNr.equals(RechnungFac.STATUS_VERBUCHT)) {

				ParametermandantDto p = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_FINANZ, ParameterFac.PARAMETER_FINANZ_MAHNUNG_AB_RECHNUNGSBETRAG);
				BigDecimal bdWert = new BigDecimal(p.getCWert());

				BigDecimal bdBruttoFw = rechnungDto.getNWertfw().add(rechnungDto.getNWertustfw());

				BigDecimal bdBezahltUstFw = getRechnungFac().getBereitsBezahltWertVonRechnungUstFw(rechnungDto.getIId(),
						null);
				BigDecimal bdBezahltNettoFw = getRechnungFac().getBereitsBezahltWertVonRechnungFw(rechnungDto.getIId(),
						null);

				BigDecimal offenFw = bdBruttoFw.subtract(bdBezahltNettoFw.add(bdBezahltUstFw));

				BigDecimal offenMandWhg = getLocaleFac().rechneUmInAndereWaehrungZuDatum(offenFw,
						rechnungDto.getWaehrungCNr(), theClientDto.getSMandantenwaehrung(),
						new Date(System.currentTimeMillis()), theClientDto);

				if (offenMandWhg.abs().doubleValue() <= bdWert.doubleValue()) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return false;
		}
	}

	public BigDecimal getSummeEinesKundenImMahnlauf(Integer mahnlaufIId, Integer kundeIId, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Session session = null;
		try {
			SessionFactory factory = FLRSessionFactory.getFactory();
			session = factory.openSession();
			Criteria c = session.createCriteria(FLRFinanzMahnung.class);
			// Filter nach Mahnlauf
			c.add(Restrictions.eq(FinanzFac.FLR_MAHNUNG_MAHNLAUF_I_ID, mahnlaufIId));
			// Filter nach Lunde
			c.createCriteria(FinanzFac.FLR_MAHNUNG_FLRRECHNUNGREPORT)
					.add(Restrictions.eq(RechnungFac.FLR_RECHNUNG_KUNDE_I_ID, kundeIId));
			// Sortierung aufsteigend nach Kontonummer
			List<?> list = c.list();
			BigDecimal bdSumme = new BigDecimal(0);
			for (Iterator<?> iter = list.iterator(); iter.hasNext();) {
				FLRFinanzMahnung mahnung = (FLRFinanzMahnung) iter.next();
				if (mahnung.getFlrrechnungreport().getN_wert() != null) {
					BigDecimal bdWert = mahnung.getFlrrechnungreport().getN_wert()
							.add(mahnung.getFlrrechnungreport().getN_wertust());
					if (bdWert != null) {
						BigDecimal bdOffen = bdWert
								.subtract(getRechnungFac().getBereitsBezahltWertVonRechnung(
										mahnung.getFlrrechnungreport().getI_id(), null))
								.subtract(getRechnungFac().getBereitsBezahltWertVonRechnungUst(
										mahnung.getFlrrechnungreport().getI_id(), null));

						if (mahnung.getFlrrechnungreport().getFlrrechnungart().getRechnungtyp_c_nr()
								.equals(RechnungFac.RECHNUNGTYP_GUTSCHRIFT)) {
							bdOffen = bdOffen.negate();
						}

						bdSumme = bdSumme.add(bdOffen);

					}
				}
			}
			return bdSumme;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public Integer createLastschriftvorschlag(LastschriftvorschlagDto lastschriftvorschlagDto,
			TheClientDto theClientDto) {
		Validator.dtoNotNull(lastschriftvorschlagDto, "lastschriftvorschlagDto");

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_LASTSCHRIFTVORSCHLAG);
		lastschriftvorschlagDto.setIId(iId);
		lastschriftvorschlagDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		lastschriftvorschlagDto.setTAendern(getTimestamp());
		try {
			Lastschriftvorschlag lastschriftvorschlag = new Lastschriftvorschlag(lastschriftvorschlagDto.getIId(),
					lastschriftvorschlagDto.getMahnlaufIId(), lastschriftvorschlagDto.getRechnungIId(),
					lastschriftvorschlagDto.getTFaellig(), lastschriftvorschlagDto.getNRechnungsbetrag(),
					lastschriftvorschlagDto.getNBereitsBezahlt(), lastschriftvorschlagDto.getNZahlbetrag(),
					lastschriftvorschlagDto.getCAuftraggeberreferenz(),
					lastschriftvorschlagDto.getPersonalIIdAendern());
			lastschriftvorschlag.settAendern(lastschriftvorschlagDto.getTAendern());
			em.persist(lastschriftvorschlag);
			em.flush();
			setLastschriftvorschlagFromLastschriftvorschlagDto(lastschriftvorschlag, lastschriftvorschlagDto);
			return iId;
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setLastschriftvorschlagFromLastschriftvorschlagDto(Lastschriftvorschlag entity,
			LastschriftvorschlagDto dto) {
		entity.setcAuftraggeberreferenz(dto.getCAuftraggeberreferenz());
		entity.setMahnlaufIId(dto.getMahnlaufIId());
		entity.setnBereitsBezahlt(dto.getNBereitsBezahlt());
		entity.setnRechnungsbetrag(dto.getNRechnungsbetrag());
		entity.setnZahlbetrag(dto.getNZahlbetrag());
		entity.setRechnungIId(dto.getRechnungIId());
		entity.settFaellig(dto.getTFaellig());
		entity.setPersonalIIdAendern(dto.getPersonalIIdAendern());
		entity.setPersonalIIdGespeichert(dto.getPersonalIIdGespeichert());
		entity.settAendern(dto.getTAendern());
		entity.settGespeichert(dto.getTGespeichert());
		entity.setCVerwendungszweck(dto.getCVerwendungszweck());
		em.merge(entity);
		em.flush();
	}

	public void removeLastschriftvorschlag(Integer iId) throws EJBExceptionLP {
		Validator.pkFieldNotNull(iId, "iId");

		Lastschriftvorschlag lastschriftvorschlag = em.find(Lastschriftvorschlag.class, iId);
		if (lastschriftvorschlag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei removeLastschriftvorschlag. Es gibt keinen " + "Lastschriftvorschlag mit der iId "
							+ iId);
		}

		em.remove(lastschriftvorschlag);
		em.flush();
	}

	public LastschriftvorschlagDto updateLastschriftvorschlag(LastschriftvorschlagDto dto, TheClientDto theClientDto) {
		Validator.dtoNotNull(dto, "lastschriftvorschlagDto");
		Validator.pkFieldNotNull(dto.getIId(), "iId");

		Lastschriftvorschlag entity = em.find(Lastschriftvorschlag.class, dto.getIId());

		if (entity == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei updateLastschriftvorschlag. Es gibt keinen " + "Lastschriftvorschlag mit der iId "
							+ dto.getIId());
		}
		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAendern(getTimestamp());
		setLastschriftvorschlagFromLastschriftvorschlagDto(entity, dto);
		return dto;
	}

	private LastschriftvorschlagDto assembleLastschriftvorschlagDto(Lastschriftvorschlag entity) {
		return LastschriftvorschlagDtoAssembler.createDto(entity);
	}

	private LastschriftvorschlagDto lastschriftvorschlagFindByAuftraggeberreferenzOhneExc(
			String cAuftraggeberreferenz) {
		List<Lastschriftvorschlag> list = LastschriftvorschlagQuery.listByCAuftraggeberreferenz(em,
				cAuftraggeberreferenz);
		if (list == null || list.size() == 0)
			return null;

		if (list.size() == 1)
			return assembleLastschriftvorschlagDto(list.get(0));

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "Lastschriftvorschlag.cAuftraggeberreferenz");
	}

	public String generateCAuftraggeberreferenzAndUpdateLastschriftvorschlag(LastschriftvorschlagDto dto,
			TheClientDto theClientDto) {

		boolean uniqueIdFound = false;
		String cAuftraggeberreferenz = "";

		while (!uniqueIdFound) {
			cAuftraggeberreferenz = UUID.randomUUID().toString().replace("-", "");
			if (lastschriftvorschlagFindByAuftraggeberreferenzOhneExc(cAuftraggeberreferenz) == null) {
				uniqueIdFound = true;
			}
		}

		dto.setCAuftraggeberreferenz(cAuftraggeberreferenz);
		updateLastschriftvorschlag(dto, theClientDto);

		return cAuftraggeberreferenz;
	}

	@Override
	public LastschriftvorschlagDto lastschriftvorschlagFindByPrimaryKey(Integer lastschriftvorschlagIId)
			throws EJBExceptionLP {
		Validator.pkFieldNotNull(lastschriftvorschlagIId, "lastschriftvorschlagIId");
		LastschriftvorschlagDto dto = lastschriftvorschlagFindByPrimaryKeyOhneExc(lastschriftvorschlagIId);
		if (dto != null)
			return dto;

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
				"forecastpositionIId = " + String.valueOf(lastschriftvorschlagIId));
	}

	public LastschriftvorschlagDto lastschriftvorschlagFindByPrimaryKeyOhneExc(Integer lastschriftvorschlagIId) {
		Lastschriftvorschlag entity = em.find(Lastschriftvorschlag.class, lastschriftvorschlagIId);

		return entity == null ? null : assembleLastschriftvorschlagDto(entity);
	}

	public LastschriftvorschlagKomplettDto lastschriftvorschlagKomplettFindByPrimaryKey(Integer lastschriftvorschlagIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LastschriftvorschlagDto lvBaseDto = lastschriftvorschlagFindByPrimaryKey(lastschriftvorschlagIId);
		LastschriftvorschlagKomplettDto lvDto = new ModelMapper().map(lvBaseDto, LastschriftvorschlagKomplettDto.class);
		lvDto.setRechnungDto(getRechnungFac().rechnungFindByPrimaryKey(lvDto.getRechnungIId()));
		lvDto.setKundeDto(getKundeFac().kundeFindByPrimaryKey(lvDto.getRechnungDto().getKundeIId(), theClientDto));
		PartnerbankDto[] partnerbanken = getBankFac().partnerbankFindByPartnerIId(lvDto.getKundeDto().getPartnerIId(),
				theClientDto);
		if (partnerbanken != null && partnerbanken.length > 0) {
			lvDto.setPartnerbankDto(partnerbanken[0]);
			lvDto.setBankDto(
					getBankFac().bankFindByPrimaryKey(lvDto.getPartnerbankDto().getBankPartnerIId(), theClientDto));
		}

		return lvDto;
	}

	public BigDecimal getGesamtwertEinesLastschriftvorschlaglaufs(Integer mahnlaufIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		BigDecimal summe = BigDecimal.ZERO;
		List<Lastschriftvorschlag> lvList = LastschriftvorschlagQuery.listByMahnlaufIId(em, mahnlaufIId);
		for (Lastschriftvorschlag lv : lvList) {
			Rechnung rechnung = em.find(Rechnung.class, lv.getRechnungIId());
			summe = summe.add(getLocaleFac().rechneUmInAndereWaehrungZuDatum(lv.getnZahlbetrag(),
					rechnung.getWaehrungCNr(), theClientDto.getSMandantenwaehrung(), getDate(), theClientDto));
		}

		return summe;
	}

	public List<LastschriftvorschlagDto> lastschriftvorschlagFindByMahnlaufIId(Integer mahnlaufIId) {
		List<Lastschriftvorschlag> entities = LastschriftvorschlagQuery.listByMahnlaufIId(em, mahnlaufIId);
		return LastschriftvorschlagDtoAssembler.createDtos(entities);
	}

	public SepaXmlExportResult exportiereLastschriftvorschlaege(Integer mahnlaufIId, TheClientDto theClientDto) {
		ISepaPain008Exporter exporter = new SepaPain008Exporter();
		SepaXmlExportResult result = checkExportLastschriftvorschlaegeImpl(exporter, mahnlaufIId, theClientDto);
		if (result.hasErrors())
			return result;

		result = exportiereLastschriftvorschlaegeImpl(exporter, mahnlaufIId, theClientDto);
		result.setExportPath(getLastschriftvorschlagSepaExportFilename(theClientDto));

		return result;
	}

	private SepaXmlExportResult exportiereLastschriftvorschlaegeImpl(ISepaPain008Exporter exporter, Integer mahnlaufIId,
			TheClientDto theClientDto) {
		List<LastschriftvorschlagDto> lvDtos = lastschriftvorschlagFindByMahnlaufIId(mahnlaufIId);
		if (lvDtos.isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_LASTSCHRIFTEN,
					"Keine Lastschriften vorhanden.");
		}

		SepaXmlExportResult result = exporter.doExport(lvDtos, theClientDto);
		if (!result.hasErrors()) {
			for (LastschriftvorschlagDto dto : lvDtos) {
				Lastschriftvorschlag lvEntity = em.find(Lastschriftvorschlag.class, dto.getIId());
				lvEntity.setPersonalIIdGespeichert(theClientDto.getIDPersonal());
				lvEntity.settGespeichert(getTimestamp());
			}
		}
		return result;
	}

	private SepaXmlExportResult checkExportLastschriftvorschlaegeImpl(ISepaPain008Exporter exporter,
			Integer mahnlaufIId, TheClientDto theClientDto) {
		List<LastschriftvorschlagDto> lvDtos = lastschriftvorschlagFindByMahnlaufIId(mahnlaufIId);
		if (lvDtos.isEmpty()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_LASTSCHRIFTEN,
					"Keine Lastschriften vorhanden.");
		}
		SepaXmlExportResult result = exporter.checkExport(lvDtos, theClientDto);

		return result;
	}

	@Override
	public String getLastschriftvorschlagSepaExportFilename(TheClientDto theClientDto) {
		BankverbindungDto bankverbindungDto = getFinanzFac()
				.getBankverbindungFuerSepaLastschriftByMandantOhneExc(theClientDto.getMandant());
		if (bankverbindungDto == null)
			return null; // TODO

		return bankverbindungDto.getCSepaVerzeichnis();
	}

	@Override
	public void archiviereLastschriftvorschlag(Integer mahnlaufIId, String xml, TheClientDto theClientDto) {
		BankverbindungDto bankverbindungDto = getFinanzFac()
				.getBankverbindungFuerSepaLastschriftByMandantOhneExc(theClientDto.getMandant());
		PartnerDto partnerBankDto = getPartnerFac().partnerFindByPrimaryKey(bankverbindungDto.getBankIId(),
				theClientDto);
		MahnlaufDto mahnlaufDto = mahnlaufFindByPrimaryKey(mahnlaufIId);
		Integer geschaeftsjahr = getBuchenFac()
				.findGeschaeftsjahrFuerDatum(new Date(mahnlaufDto.getTAnlegen().getTime()), theClientDto.getMandant());
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		String sDate = dateFormat.format(mahnlaufDto.getTAnlegen());
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
		PartnerDto anlegerDto = personalDto.getPartnerDto();

		JCRDocDto jcrDocDto = new JCRDocDto();
		DocNodeBase docNodeBase = new DocNodeSepaExportLastschriftvorschlag(bankverbindungDto, partnerBankDto,
				geschaeftsjahr, mahnlaufDto);
		DocPath docPath = new DocPath(docNodeBase).add(new DocNodeFile(MahnwesenFac.LASTSCHRIFT_EXPORT_SEPA_FILENAME));

		jcrDocDto.setDocPath(docPath);
		try {
			jcrDocDto.setbData(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new EJBExceptionLP(e);
		}
		jcrDocDto.setbVersteckt(false);
		jcrDocDto.setlAnleger(anlegerDto.getIId());
		jcrDocDto.setlPartner(partnerBankDto.getIId());
		jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
		jcrDocDto.setlZeitpunkt(getTimestamp().getTime());
		jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		jcrDocDto.setsBelegnummer(sDate);
		jcrDocDto.setsFilename(MahnwesenFac.LASTSCHRIFT_EXPORT_SEPA_FILENAME);
		jcrDocDto.setsMIME(".xml");
		jcrDocDto.setsName("Export Sepa " + sDate);
		jcrDocDto.setsRow(mahnlaufDto.getIId().toString());
		jcrDocDto.setsTable("");
		jcrDocDto.setsSchlagworte("Export Sepa Lastschriftvorschlag XML pain.008");

		getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(jcrDocDto, theClientDto);
	}

	@Override
	public Boolean isLastschriftvorschlagOffen(Integer mahnlaufIId) {
		List<Lastschriftvorschlag> entites = LastschriftvorschlagQuery.listByMahnlaufIId(em, mahnlaufIId);

		for (Lastschriftvorschlag entity : entites) {
			if (entity.gettGespeichert() == null)
				return true;
		}
		return false;
	}

	public boolean isLastschriftvorschlagExportierbar(Integer mahnlaufIId) {
		List<Lastschriftvorschlag> entites = LastschriftvorschlagQuery.listByMahnlaufIId(em, mahnlaufIId);
		if (entites.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public String getDefaultLastschriftVerwendungszweck(RechnungDto rechnungDto, TheClientDto theClientDto) {
		String verwendungszweck = getTextRespectUISpr("rech.lastschrift.verwendungszweck.rechvombelegdatum",
				theClientDto.getMandant(), theClientDto.getLocUi(), rechnungDto.getCNr(),
				Helper.formatDatum(rechnungDto.getTBelegdatum(), theClientDto.getLocUi()));
		return verwendungszweck;
	}
}
