package com.lp.server.forecast.ejbfac;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.ejb.Artikelreservierung;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.fertigung.service.LosablieferungResultDto;
import com.lp.server.fertigung.service.LosistmaterialDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.forecast.bl.LinienabrufComparator;
import com.lp.server.forecast.ejb.Fclieferadresse;
import com.lp.server.forecast.ejb.Forecast;
import com.lp.server.forecast.ejb.Forecastart;
import com.lp.server.forecast.ejb.Forecastartspr;
import com.lp.server.forecast.ejb.ForecastartsprPK;
import com.lp.server.forecast.ejb.Forecastauftrag;
import com.lp.server.forecast.ejb.ForecastauftragQuery;
import com.lp.server.forecast.ejb.Forecastposition;
import com.lp.server.forecast.ejb.ForecastpositionQuery;
import com.lp.server.forecast.ejb.Importdef;
import com.lp.server.forecast.ejb.Importdefspr;
import com.lp.server.forecast.ejb.ImportdefsprPk;
import com.lp.server.forecast.ejb.Kommdrucker;
import com.lp.server.forecast.ejb.Linienabruf;
import com.lp.server.forecast.ejb.LinienabrufQuery;
import com.lp.server.forecast.fastlanereader.generated.FLRFclieferadresse;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastauftrag;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastposition;
import com.lp.server.forecast.fastlanereader.generated.FLRForecastpositionProduktion;
import com.lp.server.forecast.fastlanereader.generated.FLRLinienabruf;
import com.lp.server.forecast.service.ArtikelbuchungDto;
import com.lp.server.forecast.service.FclieferadresseDto;
import com.lp.server.forecast.service.FclieferadresseDtoAssembler;
import com.lp.server.forecast.service.FclieferadresseNoka;
import com.lp.server.forecast.service.FclieferadresseNokaDto;
import com.lp.server.forecast.service.ForecastDto;
import com.lp.server.forecast.service.ForecastDtoAssembler;
import com.lp.server.forecast.service.ForecastFac;
import com.lp.server.forecast.service.ForecastartDto;
import com.lp.server.forecast.service.ForecastartDtoAssembler;
import com.lp.server.forecast.service.ForecastartsprDto;
import com.lp.server.forecast.service.ForecastartsprDtoAssembler;
import com.lp.server.forecast.service.ForecastauftragDto;
import com.lp.server.forecast.service.ForecastauftragDtoAssembler;
import com.lp.server.forecast.service.ForecastpositionArtikelbuchungDto;
import com.lp.server.forecast.service.ForecastpositionDto;
import com.lp.server.forecast.service.ForecastpositionDtoAssembler;
import com.lp.server.forecast.service.ForecastpositionProduktionDto;
import com.lp.server.forecast.service.ForecastpositionWare;
import com.lp.server.forecast.service.ImportdefDto;
import com.lp.server.forecast.service.ImportdefsprDto;
import com.lp.server.forecast.service.KommdruckerDto;
import com.lp.server.forecast.service.KommdruckerDtoAssembler;
import com.lp.server.forecast.service.LinienabrufArtikelDto;
import com.lp.server.forecast.service.LinienabrufArtikelbuchungDto;
import com.lp.server.forecast.service.LinienabrufDto;
import com.lp.server.forecast.service.LinienabrufDtoAssembler;
import com.lp.server.forecast.service.LinienabrufProduktionDto;
import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.lieferschein.ejb.LieferscheinpositionQuery;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.partner.fastlanereader.generated.FLRLiefermengen;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.stueckliste.service.FertigungsgruppeDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class ForecastFacBean extends Facade implements ForecastFac {

	@PersistenceContext
	private EntityManager em;

	public Integer createForecast(ForecastDto dto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FORECAST);
			dto.setIId(pk);

			Forecast bean = new Forecast(dto.getIId(), dto.getCNr(), dto.getKundeIId(), dto.getStatusCNr(),
					dto.getITageCod(), dto.getIWochenCow(), dto.getIMonateFca());
			em.persist(bean);
			em.flush();
			setForecastFromForecastDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createKommdrucker(KommdruckerDto dto) {

		try {
			Query query = em.createNamedQuery("KommdruckerfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Kommdrucker doppelt = (Kommdrucker) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FC_KOMMDRUCKER.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_KOMMDRUCKER);
			dto.setIId(pk);

			Kommdrucker bean = new Kommdrucker(dto.getIId(), dto.getCNr(), dto.getMandantCNr(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setKommdruckerFromKommdruckerDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateKommdrucker(KommdruckerDto dto) {
		Kommdrucker bean = em.find(Kommdrucker.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("KommdruckerfindByMandantCNrCNr");
			query.setParameter(1, dto.getMandantCNr());
			query.setParameter(2, dto.getCNr());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Kommdrucker) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FC_KOMMDRUCKER.UK"));
			}
		} catch (NoResultException ex) {

		}

		setKommdruckerFromKommdruckerDto(bean, dto);
	}

	public void removeKommdrucker(KommdruckerDto dto) {
		Kommdrucker toRemove = em.find(Kommdrucker.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public KommdruckerDto kommdruckerFindByPrimaryKey(Integer iId) {
		Kommdrucker bean = em.find(Kommdrucker.class, iId);
		return KommdruckerDtoAssembler.createDto(bean);
	}

	private void setKommdruckerFromKommdruckerDto(Kommdrucker bean, KommdruckerDto dto) {
		bean.setMandantCNr(dto.getMandantCNr());
		bean.setCNr(dto.getCNr());
		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	public String getForecastartEienrForecastposition(Integer forecastpositionIId) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT fcp FROM FLRForecastposition fcp WHERE fcp.i_id=" + forecastpositionIId;

		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		if (it.hasNext()) {
			return HelperServer.getForecastartEienrForecastposition((FLRForecastposition) it.next());
		} else {
			return null;

		}
	}

	public boolean sindBereitsLieferscheinpositionenVorhanden(Integer forecastpositionIId) {
		List<Lieferscheinposition> positions = LieferscheinpositionQuery.listByForecastpositionIId(em,
				forecastpositionIId);
		for (Lieferscheinposition lspos : positions) {
			String status = getLieferscheinFac().lieferscheinFindByPrimaryKey(lspos.getLieferscheinIId())
					.getStatusCNr();

			if (!LocaleFac.STATUS_STORNIERT.equals(status)) {
				return true;
			}
		}
		return false;

		// javax.persistence.Query queryVorhanden = em
		// .createNamedQuery("LieferscheinpositionfindByForecastpositionIId");
		// queryVorhanden.setParameter(1, forecastpositionIId);
		//
		// Collection c = queryVorhanden.getResultList();
		//
		// Iterator it = c.iterator();
		// while (it.hasNext()) {
		// Lieferscheinposition lspos = (Lieferscheinposition) it.next();
		//
		// String status = getLieferscheinFac().lieferscheinFindByPrimaryKey(
		// lspos.getLieferscheinIId()).getStatusCNr();
		//
		// if (!status.equals(LocaleFac.STATUS_STORNIERT)) {
		// return true;
		// }
		// }
		// return false;

	}

	@Override
	public BigDecimal getMengeBereitsGeliefert(Integer forecastpositionId) {
		BigDecimal total = BigDecimal.ZERO;
		List<Lieferscheinposition> positions = LieferscheinpositionQuery.listByForecastpositionIId(em,
				forecastpositionId);
		for (Lieferscheinposition lspos : positions) {
			String status = getLieferscheinFac().lieferscheinFindByPrimaryKey(lspos.getLieferscheinIId())
					.getStatusCNr();

			if (!LocaleFac.STATUS_STORNIERT.equals(status)) {
				total = total.add(lspos.getNMenge());
			}
		}

		return total;
	}

	public boolean sindBereitsPositionenVorhanden(Integer forecastauftragIId) {

		Query query = em.createNamedQuery("ForecastpositionFindByForecastauftragIId");
		query.setParameter(1, forecastauftragIId);

		Collection c = query.getResultList();

		if (c.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public Map getAllForecastart(TheClientDto theClientDto) {
		Map m = new LinkedHashMap();

		Query querynext = em.createNamedQuery("ForecastartfindAll");
		Collection c = querynext.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Forecastart fca = (Forecastart) it.next();

			ForecastartsprPK forecastartsprPK = new ForecastartsprPK();
			forecastartsprPK.setLocaleCNr(theClientDto.getLocUiAsString());
			forecastartsprPK.setForecastartCNr(fca.getCNr());

			Forecastartspr forecastartspr = em.find(Forecastartspr.class, forecastartsprPK);

			if (forecastartspr != null) {
				m.put(fca.getCNr(), forecastartspr.getCBez());
			} else {
				m.put(fca.getCNr(), fca.getCNr());
			}

		}

		return m;
	}

	public Map getAllKommdrucker(TheClientDto theClientDto) {
		Map m = new LinkedHashMap();

		Query querynext = em.createNamedQuery("KommdruckerfindByMandantCNr");
		querynext.setParameter(1, theClientDto.getMandant());
		Collection c = querynext.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Kommdrucker fca = (Kommdrucker) it.next();

			if (fca.getCBez() != null) {
				m.put(fca.getIId(), fca.getCBez());
			} else {
				m.put(fca.getIId(), fca.getCNr());
			}

		}

		return m;
	}

	public Map getAllArtikelEinesForecastAuftrags(Integer forecastauftragIId) {
		Map m = new LinkedHashMap();

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT distinct fcp.flrartikel.i_id, fcp.flrartikel.c_nr FROM FLRForecastposition fcp WHERE fcp.flrforecastauftrag.i_id="
				+ forecastauftragIId + " ORDER BY fcp.flrartikel.c_nr ASC ";

		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		while (it.hasNext()) {
			Object[] zeile = (Object[]) it.next();

			m.put(zeile[0], zeile[1]);

		}
		session.close();
		return m;
	}

	public Map getAllImportdef(TheClientDto theClientDto) {
		Map m = new LinkedHashMap();

		Query querynext = em.createNamedQuery("ImportdefFindAll");
		Collection c = querynext.getResultList();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			Importdef fca = (Importdef) it.next();

			ImportdefsprPk pk = new ImportdefsprPk();
			pk.setLocaleCNr(theClientDto.getLocUiAsString());
			pk.setImportdefCNr(fca.getCNr());

			Importdefspr importdefspr = em.find(Importdefspr.class, pk);

			if (importdefspr != null) {
				m.put(fca.getCNr(), importdefspr.getCBez());
			} else {
				m.put(fca.getCNr(), fca.getCNr());
			}

		}

		return m;
	}

	public void reservierungenMitForecastSynchronisieren(Integer forecastIId, Integer forecastauftragIId,
			Integer forecastpositionIId) {
	
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQueryVorhandeneOffsets = "SELECT fo, (SELECT res.i_id FROM FLRArtikelreservierung res WHERE res.c_belegartnr='"
				+ LocaleFac.BELEGART_FORECAST
				+ "' AND res.i_belegartpositionid=fo.i_id) FROM FLRForecastposition fo WHERE 1=1 ";

		if (forecastIId != null) {
			sQueryVorhandeneOffsets += " AND fo.flrforecastauftrag.flrfclieferadresse.flrforecast.i_id=" + forecastIId;
		}

		if (forecastauftragIId != null) {
			sQueryVorhandeneOffsets += " AND fo.flrforecastauftrag.i_id=" + forecastauftragIId;
		}

		if (forecastpositionIId != null) {
			sQueryVorhandeneOffsets += " AND fo.i_id=" + forecastpositionIId;
		}

		org.hibernate.Query sQuery = session.createQuery(sQueryVorhandeneOffsets);

		List l = sQuery.list();

		Iterator it = l.iterator();

		while (it.hasNext()) {

			Object[] oZeile = (Object[]) it.next();

			FLRForecastposition fp = (FLRForecastposition) oZeile[0];
			Integer artikelreservierungIId = (Integer) oZeile[1];

			BigDecimal bdBereitsGeliefert = getBereitsGelieferteMenge(fp.getI_id());

			if (fp.getFlrforecastauftrag().getStatus_c_nr().equals(LocaleFac.STATUS_FREIGEGEBEN)
					&& fp.getFlrforecastauftrag().getFlrfclieferadresse().getFlrforecast().getStatus_c_nr()
							.equals(LocaleFac.STATUS_ANGELEGT)

					&& fp.getN_menge().doubleValue() > 0
					&& fp.getN_menge().doubleValue() > bdBereitsGeliefert.doubleValue()
					&& fp.getFlrforecastauftrag().getT_freigabe() != null && Helper.isOneOf(fp.getStatus_c_nr(),
							LocaleFac.STATUS_ANGELEGT, LocaleFac.STATUS_IN_PRODUKTION, LocaleFac.STATUS_TEILERLEDIGT)) {
				// && fp.getStatus_c_nr()
				// .equals(LocaleFac.STATUS_ANGELEGT)) {

				// Nur wenn der Forecast und der Forecastauftrag und die
				// Forecastpositon angelegt
				// sind, dann Reservierung erzeugen

				// Wenns schon eine gibt, vorher loeschen
				if (artikelreservierungIId != null) {
					Artikelreservierung artikelreservierung = em.find(Artikelreservierung.class,
							artikelreservierungIId);
					em.remove(artikelreservierung);
					em.flush();
				}

				PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
				Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_ARTIKELRESERVIERUNG);
				Artikelreservierung artikelreservierung = new Artikelreservierung(fp.getI_id(),
						fp.getFlrartikel().getI_id(), new Timestamp(fp.getT_termin().getTime()),
						LocaleFac.BELEGART_FORECAST, pk);
				artikelreservierung.setNMenge(fp.getN_menge().subtract(bdBereitsGeliefert));
				em.persist(artikelreservierung);
				em.flush();

			} else {
				// Reservierung loeschen

				// zuerst Artikelreservierung loeschen

				if (artikelreservierungIId != null) {
					Artikelreservierung artikelreservierung = em.find(Artikelreservierung.class,
							artikelreservierungIId);
					em.remove(artikelreservierung);
					em.flush();
				}

			}

		}

	}

	public BigDecimal getBereitsGelieferteMenge(Integer forecastpositionIId) {
		Session session = FLRSessionFactory.getFactory().openSession();
		try {
			String sQuery = "SELECT sum(lp.n_menge) FROM FLRLieferscheinposition lp WHERE lp.forecastposition_i_id="
					+ forecastpositionIId + " AND lp.flrlieferschein.lieferscheinstatus_status_c_nr<>'"
					+ LocaleFac.STATUS_STORNIERT + "'";

			org.hibernate.Query query = session.createQuery(sQuery);
			List<BigDecimal> l = query.list();
			BigDecimal d = l.size() > 0 ? l.get(0) : BigDecimal.ZERO;
			BigDecimal bereitsGeliefert = d == null ? BigDecimal.ZERO : d;

			return bereitsGeliefert;
		} finally {
			closeSession(session);
		}
	}

	public Integer createForecastposition(ForecastpositionDto dto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FORECASTPOSITION);
			dto.setIId(pk);

			if (dto.getNMengeErfasst() == null) {
				dto.setNMengeErfasst(dto.getNMenge());
			}

			Forecastposition bean = new Forecastposition(dto.getIId(), dto.getForecastauftragIId(), dto.getArtikelIId(),
					dto.getTTermin(), dto.getNMenge(), dto.getStatusCNr(), dto.getNMengeErfasst());
			em.persist(bean);
			em.flush();
			setForecastpositionFromForecastpositionDto(bean, dto);

			reservierungenMitForecastSynchronisieren(null, null, bean.getIId());

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createLinienabruf(LinienabrufDto dto) {

		try {

			// try {
			// Query query = em
			// .createNamedQuery("LinienabrufFindByForecastpositionIIdCLinie");
			// query.setParameter(1, dto.getForecastpositionIId());
			// query.setParameter(2, dto.getCLinie());
			// // @todo getSingleResult oder getResultList ?
			// Linienabruf doppelt = (Linienabruf) query.getSingleResult();
			// throw new EJBExceptionLP(
			// EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
			// "FC_LINIENABRUF.UK"));
			// } catch (NoResultException ex) {
			//
			// }

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_LINIENABRUF);
			dto.setIId(pk);

			Linienabruf bean = new Linienabruf(dto.getIId(), dto.getForecastpositionIId(), dto.getCLinie(),
					dto.getCBereichNr(), dto.getCBereichBez(), dto.getCBestellnummer(), dto.getTProduktionstermin(),
					dto.getNMenge(), dto.getNMenge());
			em.persist(bean);
			em.flush();
			setLinienabrufFromLinienabrufDto(bean, dto);

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createFclieferadresse(FclieferadresseDto dto) {

		try {

			try {
				Query query = em.createNamedQuery("FclieferadresseFindByForecastIIdKundeIIdLieferadresse");
				query.setParameter(1, dto.getForecastIId());
				query.setParameter(2, dto.getKundeIIdLieferadresse());
				// @todo getSingleResult oder getResultList ?
				Fclieferadresse doppelt = (Fclieferadresse) query.getSingleResult();
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("FC_FCLIEFERADRESSE.UK"));
			} catch (NoResultException ex) {

			}

			if (dto.getITypRundungPosition() == null) {
				dto.setITypRundungPosition(ForecastFac.FORECAST_TYP_RUNDUNG_KEINE);
			}
			if (dto.getITypRundungLinienabruf() == null) {
				dto.setITypRundungLinienabruf(ForecastFac.FORECAST_TYP_RUNDUNG_KEINE);
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FCLIEFERADRESSE);
			dto.setIId(pk);

			if (dto.getBZusammenziehen() == null) {
				dto.setBZusammenziehen(Helper.boolean2Short(false));
			}
			if (dto.getBKommissionieren() == null) {
				dto.setBKommissionieren(Helper.boolean2Short(false));
			}
			if (dto.getBLiefermengenberuecksichtigen() == null) {
				dto.setBLiefermengenberuecksichtigen(Helper.boolean2Short(true));
			}

			Fclieferadresse bean = new Fclieferadresse(dto.getiId(), dto.getForecastIId(),
					dto.getKundeIIdLieferadresse(), dto.getBZusammenziehen(), dto.getBKommissionieren(),
					dto.getITypRundungPosition(), dto.getITypRundungLinienabruf(),
					dto.getBLiefermengenberuecksichtigen());
			em.persist(bean);
			em.flush();
			setFclieferadresseFromFclieferadresseDto(bean, dto);

			return dto.getiId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateLinienabruf(LinienabrufDto dto, TheClientDto theClientDto) {
		Linienabruf bean = em.find(Linienabruf.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("LinienabrufFindByForecastpositionIIdCLinie");
			query.setParameter(1, dto.getForecastpositionIId());
			query.setParameter(2, dto.getCLinie());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Linienabruf) query.getSingleResult()).getIId();
			if (dto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("FC_LINIENABRUF.UK"));
			}

		} catch (NoResultException ex) {

		}

		setLinienabrufFromLinienabrufDto(bean, dto);
	}

	public void updateFclieferadresse(FclieferadresseDto dto, TheClientDto theClientDto) {
		Fclieferadresse bean = em.find(Fclieferadresse.class, dto.getiId());

		try {
			Query query = em.createNamedQuery("FclieferadresseFindByForecastIIdKundeIIdLieferadresse");
			query.setParameter(1, dto.getForecastIId());
			query.setParameter(2, dto.getKundeIIdLieferadresse());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Fclieferadresse) query.getSingleResult()).getiId();
			if (dto.getiId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("FC_FCLIEFERADRESSE.UK"));
			}

		} catch (NoResultException ex) {

		}

		setFclieferadresseFromFclieferadresseDto(bean, dto);
	}

	public Integer createForecastauftrag(ForecastauftragDto dto) {

		try {
			// generieren von primary key

			// Alle anderen der gleichen Art auf erledigt setzen

			Query query = em.createNamedQuery(ForecastauftragQuery.ByForecastlieferadresseStatusCNr);
			query.setParameter(1, dto.getFclieferadresseIId());
			query.setParameter(2, LocaleFac.STATUS_ANGELEGT);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			while (it.hasNext()) {
				Forecastauftrag fca = (Forecastauftrag) it.next();
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FORECAST_ES_IST_EIN_ANGELEGTER_NICHT_FREIGEGEBENER_FCAUFTRAG_VORHANDEN,
						new Exception("FEHLER_FORECAST_ES_IST_EIN_ANGELEGTER_NICHT_FREIGEGEBENER_FCAUFTRAG_VORHANDEN"));
			}

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_FORECASTAUFTRAG);
			dto.setIId(pk);
			dto.setTAnlegen(new Timestamp(System.currentTimeMillis()));
			Forecastauftrag bean = new Forecastauftrag(dto.getIId(), dto.getFclieferadresseIId(), dto.getTAnlegen(),
					dto.getStatusCNr());
			em.persist(bean);
			em.flush();
			setForecastauftragFromForecastauftragDto(bean, dto);

			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void updateForecast(ForecastDto dto) {
		Forecast bean = em.find(Forecast.class, dto.getIId());

		// Wenn in den Kopfdaten ein Importpfad hinterlegt ist, dann darf in
		// keiner Lieferadresse in Importpfad vorhanden sein
		if (dto.getCPfadForecastauftrag() != null) {

			Query query = em.createNamedQuery("FclieferadresseFindByForecastIId");
			query.setParameter(1, dto.getIId());
			Collection c = query.getResultList();
			Iterator it = c.iterator();
			while (it.hasNext()) {
				Fclieferadresse fcl = (Fclieferadresse) it.next();

				if (fcl.getCPfadImport() != null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_FORECAST_ES_IST_BEREITS_EIN_EIN_IMPORTPFAD_IN_EINER_LIEFERADRESSE_DEFINIERT,
							new Exception(
									"FEHLER_FORECAST_ES_IST_BEREITS_EIN_EIN_IMPORTPFAD_IN_EINER_LIEFERADRESSE_DEFINIERT"));
				}
			}
		}

		setForecastFromForecastDto(bean, dto);
	}

	public void updateForecastposition(ForecastpositionDto dto) {
		Forecastposition bean = em.find(Forecastposition.class, dto.getIId());
		setForecastpositionFromForecastpositionDto(bean, dto);

		reservierungenMitForecastSynchronisieren(null, null, bean.getIId());

	}

	public void updateForecastauftrag(ForecastauftragDto dto) {
		Forecastauftrag bean = em.find(Forecastauftrag.class, dto.getIId());
		setForecastauftragFromForecastauftragDto(bean, dto);
	}

	public ForecastDto forecastFindByPrimaryKey(Integer iId) {
		Forecast forecast = em.find(Forecast.class, iId);
		return ForecastDtoAssembler.createDto(forecast);
	}

	public LinienabrufDto linienabrufFindByPrimaryKey(Integer iId) {
		Linienabruf linienabruf = em.find(Linienabruf.class, iId);
		return LinienabrufDtoAssembler.createDto(linienabruf);
	}

	public FclieferadresseDto fclieferadresseFindByPrimaryKey(Integer iId) {
		Fclieferadresse fclieferadresse = em.find(Fclieferadresse.class, iId);
		return FclieferadresseDtoAssembler.createDto(fclieferadresse);
	}

	public FclieferadresseDto fclieferadresseFindByForecastIdLieferadresseIdOhneExc(Integer forecastId,
			Integer lieferKundeId) {
		try {
			Query query = em.createNamedQuery("FclieferadresseFindByForecastIIdKundeIIdLieferadresse");
			query.setParameter(1, forecastId);
			query.setParameter(2, lieferKundeId);

			Fclieferadresse adr = (Fclieferadresse) query.getSingleResult();
			return adr != null ? FclieferadresseDtoAssembler.createDto(adr) : null;
		} catch (NoResultException ex) {
		}

		return null;
	}

	public Integer getAktuellFreigegebenenForecastauftragEinerLieferadresse(Integer kundeIIdLieferadresse) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT fa FROM FLRForecastauftrag fa WHERE fa.flrfclieferadresse.kunde_i_id_lieferadresse="
				+ kundeIIdLieferadresse + " AND fa.status_c_nr='" + LocaleFac.STATUS_FREIGEGEBEN + "'";

		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		if (l.size() > 1) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FORECAST_MEHRERE_FREIGEGEBENE_FORECASTAUFTRAEGE_ZU_EINER_LIEFERADRESSE_VORHANDEN,
					new Exception(
							"FEHLER_FORECAST_MEHRERE_FREIGEGEBENE_FORECASTAUFTRAEGE_ZU_EINER_LIEFERADRESSE_VORHANDEN"));
		}

		Iterator it = l.iterator();
		if (it.hasNext()) {

			FLRForecastauftrag fa = (FLRForecastauftrag) it.next();
			return fa.getI_id();

		}
		session.close();
		return null;
	}

	public ForecastDto forecastFindByPrimaryKeyOhneExc(Integer forecastIId) {
		ForecastDto forecastDto = null;

		try {
			forecastDto = forecastFindByPrimaryKey(forecastIId);
		} catch (Throwable t) {
			//
		}

		return forecastDto;
	}

	public BigDecimal getSummeLinienabrufe(Integer forecastpositionIId) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT la.n_menge FROM FLRLinienabruf la WHERE la.forecastposition_i_id="
				+ forecastpositionIId;

		org.hibernate.Query query = session.createQuery(sQuery);

		List l = query.list();

		Iterator it = l.iterator();

		BigDecimal summeAbrufe = BigDecimal.ZERO;

		if (l.size() == 0) {
			return null;
		}

		while (it.hasNext()) {

			BigDecimal bd = (BigDecimal) it.next();

			summeAbrufe = summeAbrufe.add(bd);

		}
		session.close();

		return summeAbrufe;
	}

	public ForecastpositionDto forecastpositonFindByPrimaryKeyOhneExc(Integer forecastpositionIId) {
		Forecastposition forecastposition = em.find(Forecastposition.class, forecastpositionIId);

		return forecastposition != null ? ForecastpositionDtoAssembler.createDto(forecastposition) : null;
	}

	public ForecastartDto forecastartFindByPrimaryKey(String cNrI, TheClientDto theClientDto) {
		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cNrI == null"));
		}
		ForecastartDto forecastartDto = null;
		Forecastart forecastart = em.find(Forecastart.class, cNrI);

		forecastartDto = ForecastartDtoAssembler.createDto(forecastart);
		// jetzt die Spr
		ForecastartsprDto forecastartsprDto = null;

		try {
			ForecastartsprPK forecastartsprPK = new ForecastartsprPK(theClientDto.getLocUiAsString(),
					forecastartDto.getCNr());
			Forecastartspr forecastartspr = em.find(Forecastartspr.class, forecastartsprPK);
			if (forecastartspr != null) {
				forecastartsprDto = ForecastartsprDtoAssembler.createDto(forecastartspr);
			}
		} catch (Throwable t) {
			// ignore
		}
		forecastartDto.setForecastartsprDto(forecastartsprDto);
		return forecastartDto;
	}

	private void setForecastartFromForecastartDto(Forecastart forecastart, ForecastartDto forecastartDto) {
		forecastart.setISort(forecastartDto.getISort());
		em.merge(forecastart);
		em.flush();
	}

	public void updateForecastart(ForecastartDto forecastartDto, TheClientDto theClientDto) {

		String cNr = forecastartDto.getCNr();
		try {
			Forecastart forecastart = em.find(Forecastart.class, cNr);

			setForecastartFromForecastartDto(forecastart, forecastartDto);

			// jetzt die Spr
			ForecastartsprDto forecastartsprDto = forecastartDto.getForecastartsprDto();

			if (forecastartsprDto != null && forecastartsprDto.getForecastartCNr() != null) {
				ForecastartsprPK forecastartsprPK = new ForecastartsprPK();
				forecastartsprPK.setLocaleCNr(forecastartsprDto.getLocaleCNr());
				forecastartsprPK.setForecastartCNr(forecastartsprDto.getForecastartCNr());

				Forecastartspr forecastartspr = em.find(Forecastartspr.class, forecastartsprPK);

				setForecastartsprFromForecastartsprDto(forecastartspr, forecastartsprDto);
			} else {
				Forecastartspr forecastartspr = new Forecastartspr(forecastartDto.getForecastartsprDto().getLocaleCNr(),
						forecastartDto.getCNr(), forecastartDto.getForecastartsprDto().getCBez());
				em.persist(forecastartspr);
				em.flush();

				setForecastartsprFromForecastartsprDto(forecastartspr, forecastartDto.getForecastartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	private void setForecastartsprFromForecastartsprDto(Forecastartspr forecastartspr,
			ForecastartsprDto forecastartsprDto) {
		forecastartspr.setCBez(forecastartsprDto.getCBez());
		em.merge(forecastartspr);
		em.flush();
	}

	public ForecastpositionDto forecastpositionFindByPrimaryKey(Integer iId) {
		ForecastpositionDto fcp = forecastpositonFindByPrimaryKeyOhneExc(iId);
		if (fcp != null)
			return fcp;

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
				"forecastpositionIId = " + (iId == null ? "null" : iId));
	}

	public ForecastauftragDto forecastauftragFindByPrimaryKey(Integer iId) {
		Forecastauftrag forecastauftrag = em.find(Forecastauftrag.class, iId);
		return ForecastauftragDtoAssembler.createDto(forecastauftrag);
	}

	private void aufVerpackungsmengenAufrunden(Integer forecastauftragId, boolean bFreigabe,
			TheClientDto theClientDto) {
		// PJ20199

		Session sessionPos = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT fp FROM FLRForecastposition fp WHERE fp.flrartikelliste.f_verpackungsmenge>0 AND  fp.flrforecastauftrag.i_id="
				+ forecastauftragId;

		org.hibernate.Query queryPos = sessionPos.createQuery(sQuery);
		List<FLRForecastposition> pos = queryPos.list();
		for (FLRForecastposition po : pos) {

//			if(po.getI_id() == 630464 || po.getI_id() == 630477) {
//				myLogger.warn("halt3");
//			}

			// zuerst die Linienabrufe
			if (po.getLinienabrufset().size() > 0) {
				BigDecimal total = BigDecimal.ZERO;
				BigDecimal bdAufMengeAufzurundenLinienabrufe = null;

				if (po.getFlrforecastauftrag().getFlrfclieferadresse().getI_typ_rundung_linienabruf()
						.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE) {
					if (po.getFlrartikelliste().getF_verpackungsmenge() != null) {
						bdAufMengeAufzurundenLinienabrufe = new BigDecimal(
								po.getFlrartikelliste().getF_verpackungsmenge());
					}

				} else if (po.getFlrforecastauftrag().getFlrfclieferadresse().getI_typ_rundung_linienabruf()
						.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE) {

					bdAufMengeAufzurundenLinienabrufe = po.getFlrartikelliste().getN_verpackungsmittelmenge();
				}

				List<Linienabruf> abrufe = LinienabrufQuery.listByForecastpositionIId(em, po.getI_id());

				Iterator itLA = abrufe.iterator();

				while (itLA.hasNext()) {
					Linienabruf la = (Linienabruf) itLA.next();

					if (bFreigabe == true) {
						la.setNMenge(
								aufVielfachesEinerMengeAufrunden(la.getNMenge(), bdAufMengeAufzurundenLinienabrufe));
						total = total.add(la.getNMenge());
					} else {
						// Menge wieder auf MengeErfasst zuruecksetzen
						la.setNMenge(la.getNMengeErfasst());
					}
					em.merge(la);
					em.flush();
				}

				Forecastposition forecastposition = em.find(Forecastposition.class, po.getI_id());
				if (bFreigabe) {
					forecastposition.setNMenge(total);
				} else {
					forecastposition.setNMenge(forecastposition.getNMengeErfasst());
				}
				em.merge(forecastposition);
				em.flush();
			} else {
				Forecastposition forecastposition = em.find(Forecastposition.class, po.getI_id());

				BigDecimal bdAufMengeAufzurundenForecastposition = null;

				if (po.getFlrforecastauftrag().getFlrfclieferadresse().getI_typ_rundung_position()
						.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE) {
					if (po.getFlrartikelliste().getF_verpackungsmenge() != null) {
						bdAufMengeAufzurundenForecastposition = new BigDecimal(
								po.getFlrartikelliste().getF_verpackungsmenge());
					}

				} else if (po.getFlrforecastauftrag().getFlrfclieferadresse().getI_typ_rundung_position()
						.intValue() == ForecastFac.FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE) {

					bdAufMengeAufzurundenForecastposition = po.getFlrartikelliste().getN_verpackungsmittelmenge();
				}
				if (bFreigabe == true) {
					// Menge = Vielfaches der Verpackungsmenge
					forecastposition.setNMenge(aufVielfachesEinerMengeAufrunden(forecastposition.getNMenge(),
							bdAufMengeAufzurundenForecastposition));

				} else {
					// Menge wieder auf MengeErfasst zuruecksetzen
					forecastposition.setNMenge(forecastposition.getNMengeErfasst());
				}

				em.merge(forecastposition);
				em.flush();
			}
		}
	}

	private BigDecimal aufVielfachesEinerMengeAufrunden(BigDecimal bdMenge, BigDecimal bdMengeAufzurunden) {
		if (bdMengeAufzurunden != null && bdMengeAufzurunden.doubleValue() > 0) {
			double dRest = bdMenge.doubleValue() % bdMengeAufzurunden.doubleValue();

			if (dRest > 0) {
				return new BigDecimal(bdMenge.doubleValue() + (bdMengeAufzurunden.doubleValue() - dRest));
			}
		}
		return bdMenge;
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(2400)
	public TreeMap<String, BigDecimal> toggleFreigabe(Integer forecastauftragId, TheClientDto theClientDto) {
		Validator.notNull(forecastauftragId, "forecastauftragId");
		TreeMap<String, BigDecimal> alMengeUnterwegsKleinerNull = null;
		Forecastauftrag forecastauftrag = em.find(Forecastauftrag.class, forecastauftragId);
		if (forecastauftrag == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, forecastauftragId.toString());
		}

		try {
			if (forecastauftrag.getTFreigabe() == null) {
				alMengeUnterwegsKleinerNull = freigeben(forecastauftrag, theClientDto);

				// PJ20199
				aufVerpackungsmengenAufrunden(forecastauftrag.getIId(), true, theClientDto);
			} else {
				freigabeAufheben(forecastauftrag, theClientDto);
				// PJ20199
				aufVerpackungsmengenAufrunden(forecastauftragId, false, theClientDto);
			}

			em.merge(forecastauftrag);
			em.flush();
			reservierungenMitForecastSynchronisieren(null, forecastauftragId, null);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		return alMengeUnterwegsKleinerNull;
	}

	private void freigabeAufheben(Forecastauftrag forecastauftrag, TheClientDto theClientDto) {
		if (forecastauftrag.getStatusCNr().equals(LocaleFac.STATUS_FREIGEGEBEN)) {

			// Ausserdem darf kein nicht freigegebener Call-Off vorhaden //
			// sein
			// Bereits vorhandene angelegte freigegebene Call-Off erledigen
			// // Alle anderen der gleichen Art auf erledigt setzen
			Query query = em.createNamedQuery(ForecastauftragQuery.ByForecastlieferadresseStatusCNr);
			query.setParameter(1, forecastauftrag.getFclieferadresseIId());
			query.setParameter(2, LocaleFac.STATUS_ANGELEGT);

			List<Forecastauftrag> fcas = query.getResultList();
			for (Forecastauftrag fca : fcas) {
				if (fca.getTFreigabe() == null) {
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN,
							new Exception(
									"FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN"));
				}
			}

			forecastauftrag.setPersonalIIdFreigabe(null);
			forecastauftrag.setTFreigabe(null);
			forecastauftrag.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		} else {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH,
					new Exception("FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH"));
		}
	}

	private TreeMap<String, BigDecimal> freigeben(Forecastauftrag forecastauftrag, TheClientDto theClientDto)
			throws RemoteException {
		if (forecastauftrag.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH,
					new Exception("FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH"));
		}

		// Bereits vorhandene angelegte freigegebene Erledigen //
		// Alle anderen der gleichen Art auf erledigt setzen
		List<Forecastauftrag> freigegebeneFcas = ForecastauftragQuery.listByForecastlieferadresseIIdStatusCNr(em,
				forecastauftrag.getFclieferadresseIId(), LocaleFac.STATUS_FREIGEGEBEN);
		for (Forecastauftrag fca : freigegebeneFcas) {
			List<ForecastpositionDto> posDtos = forecastpositionFindByForecastauftragIId(fca.getIId());
			uebernimmLinienabrufe(posDtos, forecastauftrag, theClientDto);
			forecastsZusammenziehen(forecastauftrag, posDtos, theClientDto);

			if (fca.getTFreigabe() != null) {
				fca.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
				em.merge(fca);
				em.flush();
				reservierungenMitForecastSynchronisieren(null, fca.getIId(), null);
			}
		}

		PositionenDurchLinienabrufVerbrauchen pdlv = new PositionenDurchLinienabrufVerbrauchen(this, getArtikelFac(),
				em);
		List<ForecastpositionDto> newPosDtos = forecastpositionFindByForecastauftragIId(forecastauftrag.getIId());
		pdlv.doIt(newPosDtos, theClientDto);

		TreeMap<String, BigDecimal> alMengeUnterwegsKleinerNull = beruecksichtigeLiefermengen(forecastauftrag,
				theClientDto);

		forecastauftrag.setTFreigabe(getTimestamp());
		forecastauftrag.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
		forecastauftrag.setStatusCNr(LocaleFac.STATUS_FREIGEGEBEN);

		return alMengeUnterwegsKleinerNull;
	}

	/**
	 * Jene Forecastpositionen mit Linienabrufen in den neuen FCA uebernehmen, die
	 * noch nicht angearbeitet wurden (ANGELEGT)
	 * 
	 * Ist ein Linienabruf/Forecastposition angearbeitet, nimmt den das
	 * Kommissionierterminal in seine "Nachtragsarbeit"sliste auf.
	 * 
	 * @param posDtos      die Liste der Forecastpositionen aus dem "alten"
	 *                     Forecastauftrag
	 * @param neuerFca     der neue (gerade angelegte) Forecastauftrag, der manche
	 *                     der posDtos uebernehmen soll
	 * @param theClientDto
	 */
	private void uebernimmLinienabrufe(List<ForecastpositionDto> posDtos, Forecastauftrag neuerFca,
			TheClientDto theClientDto) {
		// SP4785 Freigabe ist nur moeglich, wenn beim vorhandenen
		// freigegebenen Forecastauftrag bei allen Positionen mit
		// Linienabrufen auch ein Los hinterlegt ist, das nicht
		// storniert ist.
		for (ForecastpositionDto fpDto : posDtos) {
			Session sessionLA = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT la FROM FLRLinienabruf la WHERE la.forecastposition_i_id=" + fpDto.getIId();

			org.hibernate.Query queryLA = sessionLA.createQuery(sQuery);
			List<FLRLinienabruf> las = queryLA.list();

			if (!las.isEmpty() && fpDto.isAngelegt()) {
				ForecastpositionDto fpDtoKopie = forecastpositionFindByPrimaryKey(fpDto.getIId());
				fpDtoKopie.setIId(null);
				fpDtoKopie.setForecastauftragIId(neuerFca.getIId());
				Integer forecastpositionIIdNeu = createForecastposition(fpDtoKopie);

				for (FLRLinienabruf la : las) {
					LinienabrufDto laDto = linienabrufFindByPrimaryKey(la.getI_id());
					laDto.setForecastpositionIId(forecastpositionIIdNeu);
					laDto.setIId(null);
					createLinienabruf(laDto);
				}
			}

			sessionLA.close();
		}
	}

	/**
	 * Zusammenziehen von alter und neuem Forecastauftrag Forecastpositionen aus dem
	 * alten FCA uebernehmen, deren Artikel nicht im neuen FCA vorhanden sind.
	 * Forecastpositionen die nachgetragen werden muessen, werden im alten FCA auf
	 * erledigt gesetzt.
	 * 
	 * Ist Zusammenziehen nicht erwuenscht, wird eine alte Forecastpostion deren
	 * Menge > 0 ist und die keinen Linienabruf hat ebenfalls auf erledigt gesetzt.
	 * 
	 * @param neuerFca
	 * @param posDtos
	 * @param theClientDto
	 */
	private void forecastsZusammenziehen(Forecastauftrag neuerFca, List<ForecastpositionDto> posDtos,
			TheClientDto theClientDto) throws RemoteException {
		Fclieferadresse fcl = em.find(Fclieferadresse.class, neuerFca.getFclieferadresseIId());
		if (!Helper.short2boolean(fcl.getBZusammenziehen())) {
			return;
		}

		// PJ19852 Wenn 'Zusammenziehen', dann werden aus dem
		// bestehendem freigegebenen Forecastauftrag alle bestehenden
		// Artikel, welche im aktuellen angelegtem Forecastauftrag nicht
		// vorhanden sind und bei denen Menge > 0 ist beim aktuellen
		// angelegtem Forecastauftrag nachgetragen.

		List<ForecastpositionDto> dtosNachzutragen = new ArrayList<ForecastpositionDto>();

		for (ForecastpositionDto fpDto : posDtos) {
			if (fpDto.getNMenge().signum() <= 0)
				continue;

			Session session = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT la.i_id FROM FLRLinienabruf la WHERE la.forecastposition_i_id=" + fpDto.getIId();

			org.hibernate.Query query = session.createQuery(sQuery);
			query.setMaxResults(1);

			if (query.list().size() == 0) {

				Session session2 = FLRSessionFactory.getFactory().openSession();
				String sQuery2 = "SELECT fp.i_id FROM FLRForecastposition fp WHERE fp.flrforecastauftrag.i_id="
						+ neuerFca.getIId() + " AND fp.flrartikel.i_id=" + fpDto.getArtikelIId();
				org.hibernate.Query query2 = session2.createQuery(sQuery2);
				query2.setMaxResults(1);

				if (query2.list().size() == 0) {
					dtosNachzutragen.add(fpDto);
					System.out.println("Nachgetragen" + fpDto.getIId());
				}

				session2.close();

			}

			session.close();

		}

		// Beim Zusammenziehen koennen nur noch fcpos dabei sein,
		// die keine Linienabrufe haben
		for (ForecastpositionDto dtoNachzutragen : dtosNachzutragen) {
//			if (dtoNachzutragen.isAngelegt()) {
			dtoNachzutragen.setIId(null);
			dtoNachzutragen.setForecastauftragIId(neuerFca.getIId());
			dtoNachzutragen.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
			createForecastposition(dtoNachzutragen);
//			}
		}
	}

	private TreeMap<String, BigDecimal> beruecksichtigeLiefermengen(Forecastauftrag forecastauftrag,
			TheClientDto theClientDto) {

		TreeMap<String, BigDecimal> alMengeUnterwegsKleinerNull = new TreeMap<String, BigDecimal>();

		// PJ19913 Liefermengen beruecksichtigen
		Fclieferadresse fcl = em.find(Fclieferadresse.class, forecastauftrag.getFclieferadresseIId());

		//PJ20864
		if (Helper.short2boolean(fcl.getBLiefermengenberuecksichtigen())) {

			Session sessionLA = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT distinct fp.flrartikel.i_id FROM FLRForecastposition fp WHERE fp.flrforecastauftrag.i_id="
					+ forecastauftrag.getIId()
					+ "group by fp.flrartikel.i_id having (SELECT count(*) FROM FLRLiefermengen lm WHERE lm.artikel_i_id=fp.flrartikel.i_id AND lm.kunde_i_id_lieferadresse="
					+ fcl.getKundeIIdLieferadresse() + " )>0 ";

			org.hibernate.Query queryLA = sessionLA.createQuery(sQuery);

			List local = queryLA.list();

			Iterator itLocal = local.iterator();

			while (itLocal.hasNext()) {
				Integer artikelIId = (Integer) itLocal.next();

				// Nun noch den zeitlich letzten Eintrag der Liefermengen holen
				String sQueryLetzterLiefermengenEintrag = "SELECT lm FROM FLRLiefermengen lm WHERE lm.artikel_i_id="
						+ artikelIId + " AND lm.kunde_i_id_lieferadresse=" + fcl.getKundeIIdLieferadresse()
						+ " ORDER BY lm.t_datum DESC";

				Session sessionLetzterLiefermengenEintrag = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Query queryLetzterLiefermengenEintrag = sessionLetzterLiefermengenEintrag
						.createQuery(sQueryLetzterLiefermengenEintrag);
				queryLetzterLiefermengenEintrag.setMaxResults(1);
				List listLetzterLiefermengenEintrag = queryLetzterLiefermengenEintrag.list();
				if (listLetzterLiefermengenEintrag.size() > 0) {
					FLRLiefermengen flrLiefermengen = (FLRLiefermengen) listLetzterLiefermengenEintrag.iterator()
							.next();
					Object[] oZeile = getKundeReportFac().befuelleLiefermengenReportZeile(
							fcl.getKundeIIdLieferadresse(), theClientDto, flrLiefermengen.getI_id());

					if (oZeile[KundeReportFac.REPORT_LIEFERMENGEN_MENGE_UNTERWEGS] != null) {

						BigDecimal bdMengeUnterwegsZuVerbrauchen = (BigDecimal) oZeile[KundeReportFac.REPORT_LIEFERMENGEN_MENGE_UNTERWEGS];

						if (bdMengeUnterwegsZuVerbrauchen.doubleValue() > 0) {
							// Nun alle Forecastpositionen des Artikels holen,
							// die Stati auf angelegt setzen und der Reihe nach
							// verbrauchen

//						Query queryArtikelVorhanden = em
//								.createNamedQuery("ForecastpositionFindByForecastauftragIIdArtikelIId");
//						queryArtikelVorhanden.setParameter(1,
//								forecastauftrag.getIId());
//						queryArtikelVorhanden.setParameter(2, artikelIId);
//
//						Collection cForecastpositionen = queryArtikelVorhanden
//								.getResultList();

							Collection<Forecastposition> cForecastpositionen = ForecastpositionQuery
									.listByForecastauftragIIdArtikelIId(em, forecastauftrag.getIId(), artikelIId);
//						Iterator itForecastpositionen = cForecastpositionen
//								.iterator();
//						while (itForecastpositionen.hasNext()) {
							for (Forecastposition fp : cForecastpositionen) {
//							Forecastposition fp = (Forecastposition) itForecastpositionen
//									.next();
								fp.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

								if (bdMengeUnterwegsZuVerbrauchen.doubleValue() > 0) {

									if (bdMengeUnterwegsZuVerbrauchen.doubleValue() >= fp.getNMenge().doubleValue()) {

										fp.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
										bdMengeUnterwegsZuVerbrauchen = bdMengeUnterwegsZuVerbrauchen
												.subtract(fp.getNMenge());
									} else if (bdMengeUnterwegsZuVerbrauchen.doubleValue() < fp.getNMenge()
											.doubleValue()) {
										// Splitten
										ForecastpositionDto fpDto = getForecastFac()
												.forecastpositionFindByPrimaryKey(fp.getIId());
										fpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
										fpDto.setIId(null);
										fpDto.setNMenge(fp.getNMenge().subtract(bdMengeUnterwegsZuVerbrauchen));
										fpDto.setNMengeErfasst(fp.getNMenge().subtract(bdMengeUnterwegsZuVerbrauchen));
										getForecastFac().createForecastposition(fpDto);

										fp.setNMenge(bdMengeUnterwegsZuVerbrauchen);
										fp.setNMengeErfasst(bdMengeUnterwegsZuVerbrauchen);
										fp.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
										bdMengeUnterwegsZuVerbrauchen = BigDecimal.ZERO;

									}

								}
								em.merge(fp);

							}
						} else {
							if (bdMengeUnterwegsZuVerbrauchen.doubleValue() < 0) {
								alMengeUnterwegsKleinerNull.put(flrLiefermengen.getFlrartikel().getC_nr(),
										bdMengeUnterwegsZuVerbrauchen);
							}

						}
					}
				}

				sessionLetzterLiefermengenEintrag.close();
			}
			sessionLA.close();
		}
		return alMengeUnterwegsKleinerNull;
	}

	public void toggleFreigabe0(Integer forecastauftragIId, TheClientDto theClientDto) {

		Forecastauftrag forecastauftrag = em.find(Forecastauftrag.class, forecastauftragIId);
		if (forecastauftrag.getTFreigabe() == null) {

			if (forecastauftrag.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH,
						new Exception("FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGHLICH"));
			}

			// Bereits vorhandene angelegte freigegebene Erledigen //
			// Alle anderen der gleichen Art auf erledigt setzen
			Query query = em.createNamedQuery(ForecastauftragQuery.ByForecastlieferadresseStatusCNr);
			query.setParameter(1, forecastauftrag.getFclieferadresseIId());
			query.setParameter(2, LocaleFac.STATUS_FREIGEGEBEN);

			Collection c = query.getResultList();

			Iterator it = c.iterator();

			if (it.hasNext()) {
				Forecastauftrag fca = (Forecastauftrag) it.next();

				Fclieferadresse fcl = em.find(Fclieferadresse.class, fca.getFclieferadresseIId());

				List<ForecastpositionDto> dtos = forecastpositionFindByForecastauftragIId(fca.getIId());

				// SP4785 Freigabe ist nur moeglich, wenn beim vorhandenen
				// freigegebenen Forecastauftrag bei allen Positionen mit
				// Linienabrufen auch ein Los hinterlegt ist, das nicht
				// storniert ist.
				for (int i = 0; i < dtos.size(); i++) {
					ForecastpositionDto fpDto = dtos.get(i);

					Session sessionLA = FLRSessionFactory.getFactory().openSession();
					String sQuery = "SELECT la FROM FLRLinienabruf la WHERE la.forecastposition_i_id=" + fpDto.getIId();

					org.hibernate.Query queryLA = sessionLA.createQuery(sQuery);

					List local = queryLA.list();

					Iterator itLocal = local.iterator();

					while (itLocal.hasNext()) {
						FLRLinienabruf la = (FLRLinienabruf) itLocal.next();

						LosDto losDto = getFertigungFac()
								.losFindByForecastpositionIIdOhneExc(la.getForecastposition_i_id());

						// Aufgrund von SP4854 auiskommentiert
						/*
						 * if (la.getN_menge().doubleValue() > 0 && (losDto == null ||
						 * losDto.getStatusCNr() .equals(LocaleFac.STATUS_STORNIERT))) { // Fehler
						 * 
						 * ArtikelDto aDto = getArtikelFac() .artikelFindByPrimaryKey(
						 * fpDto.getArtikelIId(), theClientDto);
						 * 
						 * ArrayList alDaten = new ArrayList(); alDaten.add(aDto.getCNr());
						 * alDaten.add(fpDto.getTTermin()); alDaten.add(la.getT_produktionstermin());
						 * 
						 * throw new EJBExceptionLP( EJBExceptionLP.
						 * FEHLER_FREIGABE_AUFGRUND_FEHLENDER_LOSE_AUF_LINIENABRUFEN_NICHT_MOEGLICH ,
						 * alDaten, new Exception(
						 * "FEHLER_FREIGABE_AUFGRUND_FEHLENDER_LOSE_AUF_LINIENABRUFEN_NICHT_MOEGHLICH"
						 * ));
						 * 
						 * //
						 * 
						 * }
						 */

						// SP4854 Wenn kein Los fuer den Linienabruf vorhanden
						// ist, dann muessen die Forecastpositionen +
						// Linienabrufe in den neuen Auftrag kopiert werden
						// (auch wenn Positionsmenge = 0)
						if (losDto == null || losDto.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {
							ForecastpositionDto fpDtoKopie = forecastpositionFindByPrimaryKey(fpDto.getIId());
							fpDtoKopie.setIId(null);
							fpDtoKopie.setForecastauftragIId(forecastauftrag.getIId());
							Integer forecastpositionIIdNeu = createForecastposition(fpDtoKopie);

							Iterator itKopie = local.iterator();
							while (itKopie.hasNext()) {
								FLRLinienabruf laKopie = (FLRLinienabruf) itKopie.next();
								LinienabrufDto laDto = linienabrufFindByPrimaryKey(laKopie.getI_id());
								laDto.setForecastpositionIId(forecastpositionIIdNeu);
								laDto.setIId(null);
								createLinienabruf(laDto);
							}

							break;
						}

					}
					sessionLA.close();

				}

				// PJ19852 Wenn 'Zusammenziehen', dann werden aus dem
				// bestehendem freigegebenen Forecastauftrag alle bestehenden
				// Artikel, welche im aktuellen angelegtem Forecastauftrag nicht
				// vorhanden sind und bei denen Menge > 0 ist beim aktuellen
				// angelegtem Forecastauftrag nachgetragen.
				if (Helper.short2boolean(fcl.getBZusammenziehen()) == true) {

					List<ForecastpositionDto> dtosNachzutragen = new ArrayList<ForecastpositionDto>();

					for (int i = 0; i < dtos.size(); i++) {

						ForecastpositionDto fpDto = dtos.get(i);

						if (fpDto.getNMenge().doubleValue() > 0) {
							Long l = ForecastpositionQuery.countByForecastauftragIIdArtikelIId(em,
									forecastauftrag.getIId(), fpDto.getArtikelIId());
							if (l == 0l) {
								dtosNachzutragen.add(fpDto);
							}

						}

					}

					for (int i = 0; i < dtosNachzutragen.size(); i++) {
						ForecastpositionDto fpDto = dtosNachzutragen.get(i);
						fpDto.setIId(null);
						fpDto.setForecastauftragIId(forecastauftrag.getIId());
						createForecastposition(fpDto);
					}

				}

				if (fca.getTFreigabe() != null) {
					fca.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
					em.merge(fca);
					em.flush();
					reservierungenMitForecastSynchronisieren(null, fca.getIId(), null);
				}

			}

			// PJ19913 Liefermengen beruecksichtigen
			Fclieferadresse fcl = em.find(Fclieferadresse.class, forecastauftrag.getFclieferadresseIId());
			Session sessionLA = FLRSessionFactory.getFactory().openSession();
			String sQuery = "SELECT distinct fp.flrartikel.i_id FROM FLRForecastposition fp WHERE fp.flrforecastauftrag.i_id="
					+ forecastauftrag.getIId()
					+ "group by fp.flrartikel.i_id having (SELECT count(*) FROM FLRLiefermengen lm WHERE lm.artikel_i_id=fp.flrartikel.i_id AND lm.kunde_i_id_lieferadresse="
					+ fcl.getKundeIIdLieferadresse() + " )>0 ";

			org.hibernate.Query queryLA = sessionLA.createQuery(sQuery);

			List local = queryLA.list();

			Iterator itLocal = local.iterator();

			while (itLocal.hasNext()) {
				Integer artikelIId = (Integer) itLocal.next();

				// Nun noch den zeitlich letzten Eintrag der Liefermengen holen
				String sQueryLetzterLiefermengenEintrag = "SELECT lm FROM FLRLiefermengen lm WHERE lm.artikel_i_id="
						+ artikelIId + " AND lm.kunde_i_id_lieferadresse=" + fcl.getKundeIIdLieferadresse()
						+ " ORDER BY lm.t_datum DESC";

				Session sessionLetzterLiefermengenEintrag = FLRSessionFactory.getFactory().openSession();

				org.hibernate.Query queryLetzterLiefermengenEintrag = sessionLetzterLiefermengenEintrag
						.createQuery(sQueryLetzterLiefermengenEintrag);
				queryLetzterLiefermengenEintrag.setMaxResults(1);
				List listLetzterLiefermengenEintrag = queryLetzterLiefermengenEintrag.list();
				if (listLetzterLiefermengenEintrag.size() > 0) {
					FLRLiefermengen flrLiefermengen = (FLRLiefermengen) listLetzterLiefermengenEintrag.iterator()
							.next();
					Object[] oZeile = getKundeReportFac().befuelleLiefermengenReportZeile(
							fcl.getKundeIIdLieferadresse(), theClientDto, flrLiefermengen.getI_id());

					if (oZeile[KundeReportFac.REPORT_LIEFERMENGEN_MENGE_UNTERWEGS] != null) {

						BigDecimal bdMengeUnterwegsZuVerbrauchen = (BigDecimal) oZeile[KundeReportFac.REPORT_LIEFERMENGEN_MENGE_UNTERWEGS];

						if (bdMengeUnterwegsZuVerbrauchen.doubleValue() > 0) {
							// Nun alle Forecastpositionen des Artikels holen,
							// die Stati auf angelegt setzen und der Reihe nach
							// verbrauchen

//							Query queryArtikelVorhanden = em
//									.createNamedQuery("ForecastpositionFindByForecastauftragIIdArtikelIId");
//							queryArtikelVorhanden.setParameter(1,
//									forecastauftrag.getIId());
//							queryArtikelVorhanden.setParameter(2, artikelIId);
//
//							Collection cForecastpositionen = queryArtikelVorhanden
//									.getResultList();
//							Iterator itForecastpositionen = cForecastpositionen
//									.iterator();

							Collection<Forecastposition> cForecastpositionen = ForecastpositionQuery
									.listByForecastauftragIIdArtikelIId(em, forecastauftrag.getIId(), artikelIId);
//							while (itForecastpositionen.hasNext()) {
							for (Forecastposition fp : cForecastpositionen) {

//								Forecastposition fp = (Forecastposition) itForecastpositionen
//										.next();
								fp.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

								if (bdMengeUnterwegsZuVerbrauchen.doubleValue() > 0) {

									if (bdMengeUnterwegsZuVerbrauchen.doubleValue() >= fp.getNMenge().doubleValue()) {

										fp.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
										bdMengeUnterwegsZuVerbrauchen = bdMengeUnterwegsZuVerbrauchen
												.subtract(fp.getNMenge());
									} else if (bdMengeUnterwegsZuVerbrauchen.doubleValue() < fp.getNMenge()
											.doubleValue()) {
										// Splitten
										ForecastpositionDto fpDto = getForecastFac()
												.forecastpositionFindByPrimaryKey(fp.getIId());
										fpDto.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
										fpDto.setIId(null);
										fpDto.setNMenge(fp.getNMenge().subtract(bdMengeUnterwegsZuVerbrauchen));
										getForecastFac().createForecastposition(fpDto);

										fp.setNMenge(bdMengeUnterwegsZuVerbrauchen);
										fp.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
										bdMengeUnterwegsZuVerbrauchen = BigDecimal.ZERO;

									}

								}
								em.merge(fp);

							}

						}

					}

				}

				sessionLetzterLiefermengenEintrag.close();

			}
			sessionLA.close();
			// Es darf nur einen Freigegebenen Forcastauftrag geben

			forecastauftrag.setTFreigabe(new Timestamp(System.currentTimeMillis()));
			forecastauftrag.setPersonalIIdFreigabe(theClientDto.getIDPersonal());
			forecastauftrag.setStatusCNr(LocaleFac.STATUS_FREIGEGEBEN);

			// Bereits voerhandenen freigegebenen Erledigen

		} else { // Ruecknahme geht nur, wenn Angelegt
			if (forecastauftrag.getStatusCNr().equals(LocaleFac.STATUS_FREIGEGEBEN)) {

				// Ausserdem darf kein nicht freigegebener Call-Off vorhaden //
				// sein
				// Bereits vorhandene angelegte freigegebene Call-Off erledigen
				// // Alle anderen der gleichen Art auf erledigt setzen
				Query query = em.createNamedQuery(ForecastauftragQuery.ByForecastlieferadresseStatusCNr);
				query.setParameter(1, forecastauftrag.getFclieferadresseIId());

				query.setParameter(2, LocaleFac.STATUS_ANGELEGT);

				Collection c = query.getResultList();

				Iterator it = c.iterator();

				while (it.hasNext()) {
					Forecastauftrag fca = (Forecastauftrag) it.next();

					if (fca.getTFreigabe() == null) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN,
								new Exception(
										"FEHLER_RUECKNAHME_DER_FREIGABE_NICHT_MOEGLICH_DA_NICHT_FREIGEGEBENER_VORHANDEN"));
					}
				}

				forecastauftrag.setPersonalIIdFreigabe(null);
				forecastauftrag.setTFreigabe(null);
				forecastauftrag.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

			} else {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGLICH,
						new Exception("FEHLER_FREIGABE_BZW_RUECKNAHME_EINES_ERLEDIGTEN_AUFTRAGES_NICHT_MOEGHLICH"));

			}

		}
		em.merge(forecastauftrag);
		em.flush();
		reservierungenMitForecastSynchronisieren(null, forecastauftragIId, null);

	}

	public void toggleForecastErledigt(Integer forecastIId) {
		Forecast fc = em.find(Forecast.class, forecastIId);

		if (fc.getStatusCNr().equals(LocaleFac.STATUS_ERLEDIGT)) {
			fc.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
		} else {
			fc.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
		}
		em.merge(fc);
		em.flush();

		reservierungenMitForecastSynchronisieren(forecastIId, null, null);

	}

	public void removeForecast(ForecastDto dto) {
		Forecast toRemove = em.find(Forecast.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeLinienabruf(LinienabrufDto dto) {
		Linienabruf toRemove = em.find(Linienabruf.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeFclieferadresse(FclieferadresseDto dto) {
		Fclieferadresse toRemove = em.find(Fclieferadresse.class, dto.getiId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeForecastposition(ForecastpositionDto dto) {
		// Zuerst die Monate loeschen

		Forecastposition toRemove = em.find(Forecastposition.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeForecastauftrag(ForecastauftragDto dto) {
		Forecastauftrag toRemove = em.find(Forecastauftrag.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	private void setForecastFromForecastDto(Forecast bean, ForecastDto dto) {
		bean.setKundeIId(dto.getKundeIId());

		bean.setCNr(dto.getCNr());
		bean.setCProjekt(dto.getCProjekt());
		bean.setStatusCNr(dto.getStatusCNr());

		bean.setCPfadCod(dto.getCPfadCod());
		bean.setCPfadCow(dto.getCPfadCow());
		bean.setCPfadForecastauftrag(dto.getCPfadForecastauftrag());
		bean.setCPfadLinienabruf(dto.getCPfadLinienabruf());

		bean.setImportdefCNrCod(dto.getImportdefCNrCod());
		bean.setImportdefCNrCow(dto.getImportdefCNrCow());
		bean.setImportdefCNrForecastauftrag(dto.getImportdefCNrForecastauftrag());
		bean.setImportdefCNrLinienabruf(dto.getImportdefCNrLinienabruf());

		bean.setITageCod(dto.getITageCod());
		bean.setIWochenCow(dto.getIWochenCow());
		bean.setIMonateFca(dto.getIMonateFca());
		
		bean.setITageGueltig(dto.getITageGueltig());

		em.merge(bean);
		em.flush();
	}

	private void setForecastpositionFromForecastpositionDto(Forecastposition bean, ForecastpositionDto dto) {
		bean.setForecastauftragIId(dto.getForecastauftragIId());
		bean.setNMenge(dto.getNMenge());
		bean.setNMengeErfasst(dto.getNMengeErfasst());
		bean.setArtikelIId(dto.getArtikelIId());
		bean.setCBestellnummer(dto.getCBestellnummer());
		bean.setTTermin(dto.getTTermin());
		bean.setForecastartCNr(dto.getForecastartCNr());
		bean.setStatusCNr(dto.getStatusCNr());
		bean.setXKommentar(dto.getXKommentar());
		bean.setPersonalIIdProduktion(dto.getPersonalIIdProduktion());
		bean.setTProduktion(dto.getTProduktion());

		em.merge(bean);
		em.flush();
	}

	private void setLinienabrufFromLinienabrufDto(Linienabruf bean, LinienabrufDto dto) {
		bean.setForecastpositionIId(dto.getForecastpositionIId());
		bean.setCBereichBez(dto.getCBereichBez());
		bean.setCBereichNr(dto.getCBereichNr());
		bean.setCBestellnummer(dto.getCBestellnummer());

		bean.setTProduktionstermin(dto.getTProduktionstermin());

		bean.setCLinie(dto.getCLinie());
		bean.setNMenge(dto.getNMenge());

		em.merge(bean);
		em.flush();
	}

	private void setFclieferadresseFromFclieferadresseDto(Fclieferadresse bean, FclieferadresseDto dto) {
		bean.setForecastIId(dto.getForecastIId());
		bean.setKundeIIdLieferadresse(dto.getKundeIIdLieferadresse());
		bean.setCPfadImport(dto.getCPfadImport());
		bean.setImportdefCNrPfad(dto.getImportdefCNrPfad());
		bean.setBZusammenziehen(dto.getBZusammenziehen());
		bean.setBKommissionieren(dto.getBKommissionieren());
		bean.setITypRundungLinienabruf(dto.getITypRundungLinienabruf());
		bean.setITypRundungPosition(dto.getITypRundungPosition());
		bean.setKommdruckerIId(dto.getKommdruckerIId());
		bean.setBLiefermengenberuecksichtigen(dto.getBLiefermengenberuecksichtigen());

		em.merge(bean);
		em.flush();
	}

	private void setForecastauftragFromForecastauftragDto(Forecastauftrag bean, ForecastauftragDto dto) {
		bean.setCBemerkung(dto.getCBemerkung());
		bean.setFclieferadresseIId(dto.getFclieferadresseIId());
		bean.setTAnlegen(dto.getTAnlegen());

		bean.setStatusCNr(dto.getStatusCNr());
		bean.setPersonalIIdFreigabe(dto.getPersonalIIdFreigabe());
		bean.setTFreigabe(dto.getTFreigabe());
		bean.setXKommentar(dto.getXKommentar());

		em.merge(bean);
		em.flush();
	}

	@Override
	public List<ForecastpositionDto> forecastpositionFindByForecastauftragIId(Integer forecastauftragIId) {
		List<Forecastposition> forecastpositionen = ForecastpositionQuery.listByForecastauftragIId(em,
				forecastauftragIId);
		List<ForecastpositionDto> fcPositionDtos = new ArrayList<ForecastpositionDto>();

		for (Forecastposition fcposition : forecastpositionen) {
			ForecastpositionDto dto = ForecastpositionDtoAssembler.createDto(fcposition);

			fcPositionDtos.add(dto);
		}
		return fcPositionDtos;
	}

	public boolean gibtEsDenArtikelIneinemOffenenOderFreigegebenenForecastauftrag(Integer artikelIId,
			Integer kundeIIdLieferadresse) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT fp FROM FLRForecastposition fp WHERE fp.flrartikel.i_id=" + artikelIId
				+ " AND fp.flrforecastauftrag.flrfclieferadresse.kunde_i_id_lieferadresse=" + kundeIIdLieferadresse
				+ " AND fp.flrforecastauftrag.status_c_nr IN('" + LocaleFac.STATUS_FREIGEGEBEN + "','"
				+ LocaleFac.STATUS_ANGELEGT + "')";

		org.hibernate.Query query = session.createQuery(sQuery);
		List l = query.list();
		if (l.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(Integer fclieferadresseIId,
			String statusCNr) {
		List<Forecastauftrag> forecastauftraege = ForecastauftragQuery.listByForecastlieferadresseIIdStatusCNr(em,
				fclieferadresseIId, statusCNr);

		List<ForecastauftragDto> fcAuftragDtos = new ArrayList<ForecastauftragDto>();
		for (Forecastauftrag auftrag : forecastauftraege) {
			fcAuftragDtos.add(ForecastauftragDtoAssembler.createDto(auftrag));
		}
		return fcAuftragDtos;
	}

	private void setImportdefFromImportdefDto(Importdef importdef, ImportdefDto importdefDto) {
		em.merge(importdef);
		em.flush();
	}

	public void createImportdef(ImportdefDto importdefDto, TheClientDto theClientDto) {

		try {
			Importdef importdef = new Importdef(importdefDto.getCNr());
			em.persist(importdef);
			em.flush();
			setImportdefFromImportdefDto(importdef, importdefDto);
			if (importdefDto.getImportdefsprDto() != null && importdefDto.getImportdefsprDto().getCBez() != null) {
				Importdefspr artikelartspr = new Importdefspr(theClientDto.getLocUiAsString(), importdefDto.getCNr(),
						importdefDto.getImportdefsprDto().getCBez());
				em.persist(artikelartspr);
				em.flush();
				setImportdefsprFromImportdefsprDto(artikelartspr, importdefDto.getImportdefsprDto());
			}
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setImportdefsprFromImportdefsprDto(Importdefspr importdefspr, ImportdefsprDto importdefsprDto) {
		importdefspr.setCBez(importdefsprDto.getCBez());
		em.merge(importdefspr);
		em.flush();
	}

	public void updateImportdef(ImportdefDto importdefDto) {

		String cNr = importdefDto.getCNr();
		// try {
		Importdef importdef = em.find(Importdef.class, cNr);

		setImportdefFromImportdefDto(importdef, importdefDto);
		// try {
		if (importdefDto.getImportdefsprDto() != null && importdefDto.getImportdefsprDto().getCBez() != null) {

			Importdefspr importdefspr = em.find(Importdefspr.class,
					new ImportdefsprPk(importdefDto.getImportdefsprDto().getLocaleCNr(), cNr));
			if (importdefspr == null) {
				importdefspr = new Importdefspr(importdefDto.getImportdefsprDto().getLocaleCNr(), cNr,
						importdefDto.getImportdefsprDto().getCBez());
				em.persist(importdefspr);
				em.flush();
			}
			setImportdefsprFromImportdefsprDto(importdefspr, importdefDto.getImportdefsprDto());
		}

	}

	public ImportdefDto importdefFindByPrimaryKey(String cNr, TheClientDto theClientDto) {
		// try {
		Importdef importdef = em.find(Importdef.class, cNr);
		if (importdef == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
					"Fehler bei artikelartFindByPrimaryKey. Es gibt keine Cnr " + cNr);
		}

		ImportdefDto importdefDto = new ImportdefDto();
		importdefDto.setCNr(importdef.getCNr());

		Importdefspr importdefspr = em.find(Importdefspr.class,
				new ImportdefsprPk(theClientDto.getLocUiAsString(), importdef.getCNr()));
		if (importdefspr != null) {

			ImportdefsprDto sprDto = new ImportdefsprDto();

			sprDto.setCBez(importdefspr.getCBez());
			sprDto.setLocaleCNr(importdefspr.getPk().getLocaleCNr());
			sprDto.setImportdefCNr(importdefspr.getPk().getImportdefCNr());

			importdefDto.setImportdefsprDto(sprDto);
		}

		return importdefDto;

	}

	public LinienabrufDto linienabrufFindByPrimaryKeyOhneExc(Integer iId) {
		Linienabruf linienabruf = em.find(Linienabruf.class, iId);
		if (linienabruf == null) {
			return null;
		}
		return LinienabrufDtoAssembler.createDto(linienabruf);
	}

	@Override
	public List<LinienabrufDto> linienabrufFindByForecastpositionId(Integer forecastpositionId) {
		Validator.pkFieldNotNull(forecastpositionId, "forecastpositionId");
		List<Linienabruf> abrufe = LinienabrufQuery.listByForecastpositionIId(em, forecastpositionId);
		List<LinienabrufDto> abrufDtos = LinienabrufDtoAssembler.createListDtos(abrufe);
		Collections.sort(abrufDtos, new LinienabrufComparator());
		return abrufDtos;
	}

	public FclieferadresseDto fclieferadresseFindByPrimaryKeyOhneExc(Integer iId) {
		Fclieferadresse entity = em.find(Fclieferadresse.class, iId);
		if (entity == null) {
			return null;
		}
		return FclieferadresseDtoAssembler.createDto(entity);
	}

	public List<ForecastpositionProduktionDto> getLieferbareForecastpositionByFclieferadresseIId(
			Integer fclieferadresseIId, FclieferadresseNoka kommissionierTyp, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		// List<ForecastpositionProduktionDto> forecastpositionen = new
		// ArrayList<ForecastpositionProduktionDto>();
		// ILieferbareForecastposition iterator =
		// getLieferbareForecastpositionIteratorImpl(
		// null, theClientDto, forecastpositionen, false);
		// QueryLieferbareForecastpositionen q = new
		// QueryLieferbareForecastpositionen();
		// q.byFclieferadresseIId(fclieferadresseIId, theClientDto,
		// kommissionierTyp, iterator);
		//
		// return forecastpositionen;
		List<ForecastpositionProduktionDto> forecastpositionen = new ArrayList<ForecastpositionProduktionDto>();
		ILieferbareForecastposition iterator = getLieferbareWarenKopfdatenIteratorImpl(forecastpositionen,
				theClientDto);
		QueryLieferbareWaren q = new QueryLieferbareWaren();
		q.byFclieferadresseIId(fclieferadresseIId, theClientDto, kommissionierTyp, iterator);

		return forecastpositionen;
	}

	public ForecastpositionProduktionDto getLieferbareForecastpositionByIId(Integer forecastpositionIId,
			boolean withLinienabrufArtikel, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		// Integer losIId = null;
		// if (withLinienabrufArtikel) {
		// losIId = findLosZuForecastposition(forecastpositionIId,
		// theClientDto);
		// }
		//
		// List<ForecastpositionProduktionDto> forecastpositionen = new
		// ArrayList<ForecastpositionProduktionDto>();
		// ILieferbareForecastposition iterator =
		// getLieferbareForecastpositionIteratorImpl(
		// losIId, theClientDto, forecastpositionen, true);
		// QueryLieferbareForecastpositionen q = new
		// QueryLieferbareForecastpositionen();
		// q.byIId(forecastpositionIId, theClientDto, iterator);
		//
		// if (forecastpositionen == null || forecastpositionen.isEmpty())
		// return null;
		//
		// if (forecastpositionen.size() > 1) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
		// }
		//
		// return forecastpositionen.get(0);

		List<ForecastpositionProduktionDto> forecastpositionen = new ArrayList<ForecastpositionProduktionDto>();
		QueryLieferbareWaren q = new QueryLieferbareWaren();
		if (withLinienabrufArtikel) {
			q.byIId(forecastpositionIId, theClientDto,
					getLieferbareWarenDetailsIteratorImpl(forecastpositionen, theClientDto));
		} else {
			q.byIId(forecastpositionIId, theClientDto,
					getLieferbareWarenKopfdatenIteratorImpl(forecastpositionen, theClientDto));
		}

		if (forecastpositionen == null || forecastpositionen.isEmpty())
			return null;

		if (forecastpositionen.size() > 1) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, "");
		}

		return forecastpositionen.get(0);
	}

	public List<ForecastpositionProduktionDto> getLieferbareForecastpositionByMandant(TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		// List<ForecastpositionProduktionDto> forecastpositionen = new
		// ArrayList<ForecastpositionProduktionDto>();
		// ILieferbareForecastposition iterator =
		// getLieferbareForecastpositionIteratorImpl(
		// null, theClientDto, forecastpositionen, false);
		// QueryLieferbareForecastpositionen q = new
		// QueryLieferbareForecastpositionen();
		// q.byMandant(theClientDto, iterator);
		//
		// return forecastpositionen;
		List<ForecastpositionProduktionDto> forecastpositionen = new ArrayList<ForecastpositionProduktionDto>();
		ILieferbareForecastposition iterator = getLieferbareWarenKopfdatenIteratorImpl(forecastpositionen,
				theClientDto);
		QueryLieferbareWaren query = new QueryLieferbareWaren();
		query.byMandant(theClientDto, iterator);

		return forecastpositionen;
	}

	private ILieferbareForecastposition getLieferbareWarenKopfdatenIteratorImpl(
			final List<ForecastpositionProduktionDto> forecastpositionen, final TheClientDto theClientDto) {
		ILieferbareForecastposition iterator = new ILieferbareForecastposition() {
			@Override
			public void process(FLRForecastpositionProduktion flrfcposition, FLRFclieferadresse flrfclieferadresse)
					throws RemoteException {
				ForecastpositionProduktionDto fcpositionDto = setupForecastpositionProduktionHeadData(flrfcposition,
						flrfclieferadresse, theClientDto);
				Timestamp terminInklLieferdauer = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
						new Date(fcpositionDto.getTTermin().getTime()),
						flrfclieferadresse.getFlrkunde_lieferadresse().getI_lieferdauer());
				fcpositionDto.setTTermin(terminInklLieferdauer);
				forecastpositionen.add(fcpositionDto);
			}
		};
		return iterator;
	}

	private ILieferbareForecastposition getLieferbareWarenDetailsIteratorImpl(
			final List<ForecastpositionProduktionDto> forecastpositionen, final TheClientDto theClientDto) {
		ILieferbareForecastposition iterator = new ILieferbareForecastposition() {
			@Override
			public void process(FLRForecastpositionProduktion flrfcposition, FLRFclieferadresse flrfclieferadresse)
					throws RemoteException {
				IForecastProduktBuilder builder = producerFactory.getBuilder(flrfcposition.getI_id(), theClientDto);
				ForecastpositionProduktionDto fcpositionDto = builder
						.setupForecastpositionProduktionDetail(flrfcposition, flrfclieferadresse, theClientDto);
				Timestamp terminInklLieferdauer = getAusliefervorschlagFac().umKundenlieferdauerVersetzen(theClientDto,
						new Date(fcpositionDto.getTTermin().getTime()),
						flrfclieferadresse.getFlrkunde_lieferadresse().getI_lieferdauer());
				fcpositionDto.setTTermin(terminInklLieferdauer);
				forecastpositionen.add(fcpositionDto);
			}
		};
		return iterator;
	}

	public List<FclieferadresseNokaDto> getLieferbareFclieferadressenByMandant(TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		final Map<String, Integer> adressenIds = new HashMap<String, Integer>();
		QueryLieferbareWaren q = new QueryLieferbareWaren();
		q.byMandant(theClientDto, new ILieferbareForecastposition() {

			@Override
			public void process(FLRForecastpositionProduktion flrfcposition, FLRFclieferadresse flrfclieferadresse)
					throws RemoteException {
				if (flrfcposition.getLinienabrufset() != null && !flrfcposition.getLinienabrufset().isEmpty()) {
					adressenIds.put(flrfclieferadresse.getI_id().toString() + "|0", flrfclieferadresse.getI_id());
				} else {
					if (Helper.short2boolean(flrfclieferadresse.getB_kommissionieren())
							|| Helper.short2boolean(flrfcposition.getFlrartikel().getB_kommissionieren())) {
						adressenIds.put(flrfclieferadresse.getI_id().toString() + "|1", flrfclieferadresse.getI_id());
					}
				}
			}
		});

		List<FclieferadresseNokaDto> lieferadressen = new ArrayList<FclieferadresseNokaDto>();
		for (Map.Entry<String, Integer> entry : adressenIds.entrySet()) {
			Fclieferadresse fclieferadresse = em.find(Fclieferadresse.class, entry.getValue());
			FclieferadresseNokaDto dto = FclieferadresseDtoAssembler.createNokaDto(fclieferadresse);
			dto.setKommissionieren(
					entry.getKey().endsWith("|1") ? FclieferadresseNoka.ARTIKEL : FclieferadresseNoka.LINIENABRUF);
			lieferadressen.add(dto);
		}

		return lieferadressen;
	}

	private interface ILieferbareForecastposition {
		void process(FLRForecastpositionProduktion flrfcposition, FLRFclieferadresse flrfclieferadresse)
				throws RemoteException;
	}

	// private class QueryLieferbareForecastpositionen {
	// private StringBuilder getBaseQueryBuilder() {
	// StringBuilder qb = new StringBuilder();
	// qb.append(" SELECT DISTINCT fcposition, fclieferadresse FROM
	// FLRForecastpositionProduktion fcposition ");
	// qb.append(" LEFT OUTER JOIN fcposition.linienabrufset AS linienabrufset ");
	// qb.append(" LEFT OUTER JOIN fcposition.flrlos AS los ");
	// qb.append(" LEFT OUTER JOIN fcposition.flrforecastauftrag AS fcauftrag ");
	// qb.append(" LEFT OUTER JOIN fcauftrag.flrfclieferadresse AS fclieferadresse
	// ");
	// qb.append(" LEFT OUTER JOIN fclieferadresse.flrforecast AS forecast ");
	// qb.append(" WHERE forecast.flrkunde.mandant_c_nr=:mandant ");
	// return qb;
	// }
	//
	// private org.hibernate.Query getBaseQuery(StringBuilder qb,
	// TheClientDto theClientDto) {
	// Session session = FLRSessionFactory.getFactory().openSession();
	// org.hibernate.Query query = session.createQuery(qb.toString());
	// query.setParameter("mandant", theClientDto.getMandant());
	// return query;
	// }
	//
	// private void iterateResult(org.hibernate.Query query,
	// ILieferbareForecastposition iterator) throws RemoteException {
	// List<?> resultList = query.list();
	// Iterator<?> resultListIterator = resultList.iterator();
	//
	// while (resultListIterator.hasNext()) {
	// Object[] obj = (Object[]) resultListIterator.next();
	// FLRForecastpositionProduktion flrfcposition =
	// (FLRForecastpositionProduktion) obj[0];
	// FLRFclieferadresse flrfclieferadresse = (FLRFclieferadresse) obj[1];
	//
	// iterator.process(flrfcposition, flrfclieferadresse);
	// }
	// }
	//
	// private void buildKommissionierTyp(StringBuilder qb,
	// FclieferadresseNoka kommissionierTyp) {
	// if (kommissionierTyp == null
	// || FclieferadresseNoka.NICHT_DEFINIERT
	// .equals(kommissionierTyp)) {
	// // Entweder mit Linienabrufen oder es ist ein Noka-Artikel
	// // (=b_kommissionieren)
	// qb.append(" AND (linienabrufset!=null OR fclieferadresse.b_kommissionieren=1
	// OR fcposition.flrartikel.b_kommissionieren=1) ");
	// } else {
	// if (FclieferadresseNoka.LINIENABRUF.equals(kommissionierTyp)) {
	// qb.append(" AND linienabrufset != null");
	// } else {
	// qb.append(" AND (fclieferadresse.b_kommissionieren=1 OR
	// fcposition.flrartikel.b_kommissionieren=1) ");
	// }
	// }
	// }
	//
	// private void buildLos(StringBuilder qb, Integer losIId) {
	// if (losIId != null) {
	// qb.append(" AND los.i_id=:losId ");
	// } else {
	// qb.append(" AND (los=null OR los.status_c_nr!=:losstatus) ");
	// }
	// }
	//
	// private void setParamLos(org.hibernate.Query query, Integer losIId,
	// String losstatus) {
	// if (losIId != null) {
	// query.setParameter("losId", losIId);
	// } else {
	// query.setParameter("losstatus", LocaleFac.STATUS_ERLEDIGT);
	// }
	// }
	//
	// private void buildForecastposition(StringBuilder qb,
	// Integer forecastpositionIId) {
	// qb.append(" AND fcposition.i_id=:id ");
	// }
	//
	// private void setParamForecastposition(org.hibernate.Query query,
	// Integer forecastpositionIId) {
	// query.setParameter("id", forecastpositionIId);
	// }
	//
	// private void buildFclieferadresse(StringBuilder qb,
	// Integer fclieferadresseIId) {
	// if (fclieferadresseIId == null)
	// return;
	// qb.append(" AND fclieferadresse.i_id=:lieferadresseId ");
	// }
	//
	// private void setParamFclieferadresse(org.hibernate.Query query,
	// Integer fclieferadresseIId) {
	// if (fclieferadresseIId == null)
	// return;
	// query.setParameter("lieferadresseId", fclieferadresseIId);
	// }
	//
	// private void buildForecastauftrag(StringBuilder qb) {
	// qb.append(" AND fcauftrag.status_c_nr=:auftragstatus ");
	// }
	//
	// private void setParamForecastauftrag(org.hibernate.Query query,
	// String auftragsstatus) {
	// query.setParameter("auftragstatus", auftragsstatus);
	// }
	//
	// public void byMandant(TheClientDto theClientDto,
	// ILieferbareForecastposition iterator) throws RemoteException {
	// StringBuilder qb = getBaseQueryBuilder();
	//
	// buildForecastauftrag(qb);
	// buildKommissionierTyp(qb, FclieferadresseNoka.NICHT_DEFINIERT);
	// buildLos(qb, null);
	// qb.append(" GROUP BY fcposition.i_id, fclieferadresse.i_id ");
	//
	// org.hibernate.Query query = getBaseQuery(qb, theClientDto);
	// setParamForecastauftrag(query, LocaleFac.STATUS_FREIGEGEBEN);
	// setParamLos(query, null, LocaleFac.STATUS_ERLEDIGT);
	//
	// iterateResult(query, iterator);
	//
	// queryOffeneFCPositionenVonErledigtenFCAuftraegen(
	// FclieferadresseNoka.NICHT_DEFINIERT, theClientDto, iterator);
	// }
	//
	// public void byFclieferadresseIId(Integer fclieferadresseIId,
	// TheClientDto theClientDto,
	// FclieferadresseNoka kommissionierTyp,
	// ILieferbareForecastposition iterator) throws RemoteException {
	// StringBuilder qb = getBaseQueryBuilder();
	//
	// buildFclieferadresse(qb, fclieferadresseIId);
	// buildForecastauftrag(qb);
	// buildKommissionierTyp(qb, kommissionierTyp);
	// buildLos(qb, null);
	// qb.append(" GROUP BY fcposition.i_id, fclieferadresse.i_id ");
	//
	// org.hibernate.Query query = getBaseQuery(qb, theClientDto);
	// setParamFclieferadresse(query, fclieferadresseIId);
	// setParamForecastauftrag(query, LocaleFac.STATUS_FREIGEGEBEN);
	// setParamLos(query, null, LocaleFac.STATUS_ERLEDIGT);
	//
	// iterateResult(query, iterator);
	//
	// queryOffeneFCPositionenVonErledigtenFCAuftraegen(
	// fclieferadresseIId, kommissionierTyp, theClientDto,
	// iterator);
	// }
	//
	// public void byIId(Integer forecastpositionIId,
	// TheClientDto theClientDto, ILieferbareForecastposition iterator)
	// throws RemoteException {
	// StringBuilder qb = getBaseQueryBuilder();
	//
	// buildForecastposition(qb, forecastpositionIId);
	// qb.append(" GROUP BY fcposition.i_id, fclieferadresse.i_id ");
	//
	// org.hibernate.Query query = getBaseQuery(qb, theClientDto);
	// setParamForecastposition(query, forecastpositionIId);
	//
	// iterateResult(query, iterator);
	// }
	//
	// private StringBuilder getBaseQueryBuilderVonErledigtenFCAuftraegen() {
	// StringBuilder qb = new StringBuilder();
	// qb.append(" SELECT DISTINCT fcposition, fclieferadresse FROM FLRLos los ");
	// qb.append(" LEFT OUTER JOIN los.flrforecastposition AS fcposition ");
	// qb.append(" LEFT OUTER JOIN fcposition.linienabrufset AS linienabrufset ");
	// qb.append(" LEFT OUTER JOIN fcposition.flrforecastauftrag AS fcauftrag ");
	// qb.append(" LEFT OUTER JOIN fcauftrag.flrfclieferadresse AS fclieferadresse
	// ");
	// qb.append(" LEFT OUTER JOIN fclieferadresse.flrforecast AS forecast ");
	// qb.append(" WHERE forecast.flrkunde.mandant_c_nr=:mandant ");
	// return qb;
	// }
	//
	// private void queryOffeneFCPositionenVonErledigtenFCAuftraegen(
	// Integer fclieferadresseIId,
	// FclieferadresseNoka kommissionierTyp,
	// TheClientDto theClientDto, ILieferbareForecastposition iterator)
	// throws RemoteException {
	// StringBuilder qb = getBaseQueryBuilderVonErledigtenFCAuftraegen();
	//
	// qb.append(" AND los.status_c_nr IN (:losstati) ");
	// qb.append(" AND los.forecastposition_i_id != null ");
	// buildFclieferadresse(qb, fclieferadresseIId);
	// buildForecastauftrag(qb);
	// buildKommissionierTyp(qb, kommissionierTyp);
	//
	// org.hibernate.Query query = getBaseQuery(qb, theClientDto);
	// List<String> losstati = new ArrayList<String>();
	// losstati.add(FertigungFac.STATUS_IN_PRODUKTION);
	// losstati.add(FertigungFac.STATUS_TEILERLEDIGT);
	// query.setParameterList("losstati", losstati);
	// setParamFclieferadresse(query, fclieferadresseIId);
	// setParamForecastauftrag(query, LocaleFac.STATUS_ERLEDIGT);
	//
	// iterateResult(query, iterator);
	// }
	//
	// private void queryOffeneFCPositionenVonErledigtenFCAuftraegen(
	// FclieferadresseNoka kommissionierTyp,
	// TheClientDto theClientDto, ILieferbareForecastposition iterator)
	// throws RemoteException {
	// queryOffeneFCPositionenVonErledigtenFCAuftraegen(null,
	// kommissionierTyp, theClientDto, iterator);
	// }
	// }

	public class QueryLieferbareWaren {
		private Session session;

		private StringBuilder getBaseQueryBuilder(String mandant) {
			StringBuilder qb = new StringBuilder();
			qb.append(" SELECT DISTINCT fcposition, fclieferadresse FROM FLRForecastpositionProduktion fcposition ");
			qb.append(" LEFT OUTER JOIN fcposition.linienabrufset AS linienabrufset ");
			qb.append(" LEFT OUTER JOIN fcposition.flrlos AS los ");
			qb.append(" LEFT OUTER JOIN fcposition.flrforecastauftrag AS fcauftrag ");
			qb.append(" LEFT OUTER JOIN fcauftrag.flrfclieferadresse AS fclieferadresse ");
			qb.append(" LEFT OUTER JOIN fclieferadresse.flrforecast AS forecast ");
			buildForecast(qb, mandant);
			return qb;
		}

		private void buildGroupBy(StringBuilder qb) {
			qb.append(" GROUP BY fcposition.i_id, fclieferadresse.i_id ");
		}

		private void buildKommissionierTyp(StringBuilder qb, FclieferadresseNoka kommissionierTyp) {
			if (kommissionierTyp == null || FclieferadresseNoka.NICHT_DEFINIERT.equals(kommissionierTyp)) {
				// Entweder mit Linienabrufen oder es ist ein Noka-Artikel
				// (=b_kommissionieren)
				qb.append(" AND (linienabrufset!=null  OR fcposition.flrartikel.b_kommissionieren=1) ");
			} else {
				if (FclieferadresseNoka.LINIENABRUF.equals(kommissionierTyp)) {
					qb.append(" AND linienabrufset != null");
				} else {
					qb.append(" AND fcposition.flrartikel.b_kommissionieren=1 ");
				}
			}
		}

		private void buildForecast(StringBuilder qb, String mandant) {
			qb.append(" WHERE forecast.flrkunde.mandant_c_nr='").append(mandant).append("' ");
			qb.append(" AND forecast.status_c_nr='").append(LocaleFac.STATUS_ANGELEGT).append("' ");
		}

		private void buildForecastauftrag(StringBuilder qb, String... stati) {
			if (stati.length < 1)
				return;
			qb.append(" AND fcauftrag.status_c_nr IN ('");
			for (int count = 0; count < stati.length; count++) {
				qb.append(stati[count]);
				if (count < stati.length - 1)
					qb.append("', '");
			}
			qb.append("') ");
		}

		private void buildForecastpositionStatus(StringBuilder qb, String status) {
			qb.append(" AND fcposition.status_c_nr='").append(status).append("' ");
		}

		private org.hibernate.Query getQuery(StringBuilder qb) {
			session = FLRSessionFactory.getFactory().openSession();
			org.hibernate.Query query = session.createQuery(qb.toString());
			return query;
		}

		private void close() {
			session.close();
		}

		private void buildForecastpositionStati(StringBuilder qb, String... stati) {
			if (stati.length < 1)
				return;
			qb.append(" AND fcposition.status_c_nr IN ('");
			for (int count = 0; count < stati.length; count++) {
				qb.append(stati[count]);
				if (count < stati.length - 1)
					qb.append("', '");
			}
			qb.append("') ");
		}

		private void buildFclieferadresse(StringBuilder qb, Integer fclieferadresseIId) {
			if (fclieferadresseIId == null)
				return;
			qb.append(" AND fclieferadresse.i_id=").append(fclieferadresseIId).append(" ");
		}

		private void buildForecastposition(StringBuilder qb, Integer forecastpositionIId) {
			qb.append(" AND fcposition.i_id=").append(forecastpositionIId).append(" ");
		}

		private void buildLosstatusNichtErledigt(StringBuilder qb) {
			qb.append("  AND (los=null OR los.status_c_nr!='").append(LocaleFac.STATUS_ERLEDIGT).append("') ");
		}

		public void byMandant(TheClientDto theClientDto, ILieferbareForecastposition iterator) throws RemoteException {
			StringBuilder qbNeu = getBaseQueryBuilder(theClientDto.getMandant());
			buildForecastauftrag(qbNeu, LocaleFac.STATUS_FREIGEGEBEN);
			buildKommissionierTyp(qbNeu, FclieferadresseNoka.NICHT_DEFINIERT);
			buildForecastpositionStatus(qbNeu, LocaleFac.STATUS_ANGELEGT);
			buildLosstatusNichtErledigt(qbNeu);
			buildGroupBy(qbNeu);
			org.hibernate.Query query = getQuery(qbNeu);

			iterateResult(query, iterator);

			StringBuilder qpProduktion = getBaseQueryBuilder(theClientDto.getMandant());
			buildForecastauftrag(qpProduktion, LocaleFac.STATUS_ERLEDIGT, LocaleFac.STATUS_FREIGEGEBEN);
			buildForecastpositionStati(qpProduktion, LocaleFac.STATUS_IN_PRODUKTION, LocaleFac.STATUS_TEILERLEDIGT);
			buildLosstatusNichtErledigt(qpProduktion);
			buildGroupBy(qpProduktion);
			query = getQuery(qpProduktion);

			iterateResult(query, iterator);

			close();
		}

		public void byFclieferadresseIId(Integer fclieferadresseIId, TheClientDto theClientDto,
				FclieferadresseNoka kommissionierTyp, ILieferbareForecastposition iterator) throws RemoteException {
			StringBuilder qbNeu = getBaseQueryBuilder(theClientDto.getMandant());
			buildFclieferadresse(qbNeu, fclieferadresseIId);
			buildForecastauftrag(qbNeu, LocaleFac.STATUS_FREIGEGEBEN);
			buildForecastpositionStatus(qbNeu, LocaleFac.STATUS_ANGELEGT);
			buildLosstatusNichtErledigt(qbNeu);
			buildKommissionierTyp(qbNeu, kommissionierTyp);
			buildGroupBy(qbNeu);

			iterateResult(getQuery(qbNeu), iterator);

			StringBuilder qpProduktion = getBaseQueryBuilder(theClientDto.getMandant());
			buildFclieferadresse(qpProduktion, fclieferadresseIId);
			buildForecastauftrag(qpProduktion, LocaleFac.STATUS_ERLEDIGT, LocaleFac.STATUS_FREIGEGEBEN);
			buildForecastpositionStati(qpProduktion, LocaleFac.STATUS_IN_PRODUKTION, LocaleFac.STATUS_TEILERLEDIGT);
			buildLosstatusNichtErledigt(qpProduktion);
			buildKommissionierTyp(qpProduktion, kommissionierTyp);
			buildGroupBy(qpProduktion);

			iterateResult(getQuery(qpProduktion), iterator);

			close();
		}

		public void byIId(Integer forecastpositionIId, TheClientDto theClientDto, ILieferbareForecastposition iterator)
				throws RemoteException {
			StringBuilder qb = getBaseQueryBuilder(theClientDto.getMandant());
			buildForecastposition(qb, forecastpositionIId);
			buildGroupBy(qb);

			iterateResult(getQuery(qb), iterator);
			close();
		}

		private void iterateResult(org.hibernate.Query query, ILieferbareForecastposition iterator)
				throws RemoteException {
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			while (resultListIterator.hasNext()) {
				Object[] obj = (Object[]) resultListIterator.next();
				FLRForecastpositionProduktion flrfcposition = (FLRForecastpositionProduktion) obj[0];
				FLRFclieferadresse flrfclieferadresse = (FLRFclieferadresse) obj[1];

				iterator.process(flrfcposition, flrfclieferadresse);
			}
		}
	}

	// private ILieferbareForecastposition
	// getLieferbareForecastpositionIteratorImpl(
	// final Integer losIId, final TheClientDto theClientDto,
	// final List<ForecastpositionProduktionDto> forecastpositionen,
	// final boolean loadLinienabrufArtikel) {
	// ILieferbareForecastposition iterator = new ILieferbareForecastposition()
	// {
	//
	// @Override
	// public void process(FLRForecastpositionProduktion flrfcposition,
	// FLRFclieferadresse flrfclieferadresse)
	// throws RemoteException {
	// Integer losIIdPosition = losIId == null
	// && flrfcposition.getFlrlos() != null ? flrfcposition
	// .getFlrlos().getI_id() : losIId;
	// ForecastpositionProduktionDto fcposition =
	// setupForecastpositionProduktionImpl(
	// flrfcposition, flrfclieferadresse, losIIdPosition,
	// loadLinienabrufArtikel, theClientDto);
	// fcposition.setFclieferadresseIId(flrfclieferadresse.getI_id());
	// Timestamp terminInklLieferdauer = getAusliefervorschlagFac()
	// .umKundenlieferdauerVersetzen(
	// theClientDto,
	// new Date(fcposition.getTTermin().getTime()),
	// flrfclieferadresse.getFlrkunde_lieferadresse()
	// .getI_lieferdauer());
	// fcposition.setTTermin(terminInklLieferdauer);
	//
	// forecastpositionen.add(fcposition);
	// }
	// };
	// return iterator;
	// }

	// private ForecastpositionProduktionDto
	// setupForecastpositionProduktionImpl(
	// FLRForecastpositionProduktion flrposition,
	// FLRFclieferadresse flrfclieferadresse, Integer losIId,
	// boolean loadLinienabrufArtikel, TheClientDto theClientDto)
	// throws RemoteException, EJBExceptionLP {
	// ForecastpositionProduktionDto fcposition =
	// mapForecastpositionProduktion(flrposition);
	//
	// if (flrposition.getLinienabrufset() != null
	// && !flrposition.getLinienabrufset().isEmpty()) {
	// for (Object flrlinienabruf : flrposition.getLinienabrufset()) {
	// LinienabrufProduktionDto linienabruf =
	// mapLinienabrufProduktion((FLRLinienabruf) flrlinienabruf);
	// fcposition.getLinienabrufe().add(linienabruf);
	// }
	// Collections.sort(fcposition.getLinienabrufe(),
	// new LinienabrufComparator());
	// } else {
	// if (Helper.short2boolean(flrfclieferadresse.getB_kommissionieren())
	// || Helper.short2boolean(flrposition.getFlrartikel()
	// .getB_kommissionieren())) {
	// // Noka-Artikel
	// LinienabrufProduktionDto linienabruf = new LinienabrufProduktionDto();
	// linienabruf.setCBestellnummer(flrposition.getC_bestellnummer());
	// linienabruf.setCLinie("NoKa");
	// ArtikelDto nokaArtikel = getArtikelFac()
	// .artikelFindByPrimaryKeySmall(
	// flrposition.getFlrartikel().getI_id(),
	// theClientDto);
	// linienabruf.setCBereichBez(nokaArtikel.getArtikelsprDto()
	// .getCZbez2());
	// linienabruf.setForecastpositionIId(flrposition.getI_id());
	// linienabruf.setNMenge(flrposition.getN_menge());
	// fcposition.getLinienabrufe().add(linienabruf);
	// }
	// }
	//
	// fcposition.setLagerstand(getLagerFac().getLagerstandOhneExc(
	// fcposition.getArtikelIId(),
	// getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId(),
	// theClientDto));
	//
	// if (losIId == null)
	// return fcposition;
	//
	// LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
	// fcposition.setPersonalIIdKommissionierer(
	// getPersonalIdLetzteZeitbuchungAufLos(losIId));
	// fcposition.setLosCnr(losDto.getCNr());
	//
	// if (!loadLinienabrufArtikel)
	// return fcposition;
	//
	// LossollmaterialDto[] lossollmaterial = getFertigungFac()
	// .lossollmaterialFindByLosIId(losDto.getIId());
	// List<Integer> paramArtikelgruppen =
	// getAusgeschlosseneArtikelgruppenIds(theClientDto);
	// HvCachingProvider<Integer, BigDecimal> lagerstandCache = new
	// HvCachingProvider<Integer, BigDecimal>();
	// for (LossollmaterialDto materialDto : lossollmaterial) {
	// ArtikelDto artikelDto = getArtikelFac()
	// .artikelFindByPrimaryKeySmall(
	// materialDto.getArtikelIId(), theClientDto);
	// if (paramArtikelgruppen.contains(artikelDto.getArtgruIId()))
	// continue;
	//
	// checkMaterialArtikelForLinienabrufProduktion(losDto, materialDto,
	// artikelDto);
	// fcposition.getHmArtikelLinienabrufe().put(artikelDto.getIId(),
	// artikelDto);
	// BigDecimal ausgegebeneMenge = getFertigungFac()
	// .getAusgegebeneMenge(materialDto.getIId(), null,
	// theClientDto);
	//
	// for (LinienabrufProduktionDto linienabruf : fcposition
	// .getLinienabrufe()) {
	// LinienabrufArtikelDto laArtikel = new LinienabrufArtikelDto();
	// laArtikel.setArtikelIId(artikelDto.getIId());
	// laArtikel.setMenge(materialDto.getNMenge()
	// .divide(losDto.getNLosgroesse(),
	// BigDecimal.ROUND_HALF_EVEN)
	// .multiply(linienabruf.getNMenge()));
	// BigDecimal offeneMenge = ausgegebeneMenge
	// .compareTo(laArtikel.getMenge()) >= 0 ? BigDecimal.ZERO
	// : laArtikel.getMenge().subtract(ausgegebeneMenge);
	// ausgegebeneMenge = BigDecimal.ZERO
	// .compareTo(ausgegebeneMenge.subtract(laArtikel
	// .getMenge())) >= 0 ? BigDecimal.ZERO
	// : ausgegebeneMenge.subtract(laArtikel.getMenge());
	// laArtikel.setOffeneMenge(offeneMenge);
	// linienabruf.getLinienabrufArtikel().add(laArtikel);
	// }
	// }
	//
	// return fcposition;
	// }
	//
	// private Integer getPersonalIdLetzteZeitbuchungAufLos(Integer losIId) {
	// List<ZeitdatenDto> zeitdatenDtos = getZeiterfassungFac()
	// .zeitdatenFindByBelegartnrBelegidOrderedByTZeitAsc(
	// LocaleFac.BELEGART_LOS, losIId);
	// if (zeitdatenDtos.isEmpty())
	// return null;
	//
	// return zeitdatenDtos.get(zeitdatenDtos.size() - 1).getPersonalIId();
	// }

	private LinienabrufProduktionDto mapLinienabrufProduktion(FLRLinienabruf flrlinienabruf) {
		LinienabrufProduktionDto linienabruf = new LinienabrufProduktionDto();
		linienabruf.setCBereichBez(flrlinienabruf.getC_bereich_bez());
		linienabruf.setCBereichNr(flrlinienabruf.getC_bereich_nr());
		linienabruf.setCBestellnummer(flrlinienabruf.getC_bestellnummer());
		linienabruf.setCLinie(flrlinienabruf.getC_linie());
		linienabruf.setForecastpositionIId(flrlinienabruf.getForecastposition_i_id());
		linienabruf.setIId(flrlinienabruf.getI_id());
		linienabruf.setNMenge(flrlinienabruf.getN_menge());
		linienabruf.setTProduktionstermin(new Timestamp(flrlinienabruf.getT_produktionstermin().getTime()));
		return linienabruf;
	}

	private ForecastpositionProduktionDto mapForecastpositionProduktion(FLRForecastpositionProduktion flrposition) {
		ForecastpositionProduktionDto fcposition = new ForecastpositionProduktionDto();
		fcposition.setArtikelIId(flrposition.getFlrartikel().getI_id());
		fcposition.setCBestellnummer(flrposition.getC_bestellnummer());
		fcposition.setForecastartCNr(flrposition.getForecastart_c_nr());
		fcposition.setForecastauftragIId(flrposition.getFlrforecastauftrag().getI_id());
		fcposition.setIId(flrposition.getI_id());
		fcposition.setNMenge(flrposition.getN_menge());
		fcposition.setStatusCNr(flrposition.getStatus_c_nr());
		fcposition.setTTermin(new Timestamp(flrposition.getT_termin().getTime()));
		fcposition.setXKommentar(flrposition.getX_kommentar());
		return fcposition;
	}

	private boolean isKommissionierware(Integer forecastpositionId, TheClientDto theClientDto) throws RemoteException {
		return isKommissionierware(forecastpositionFindByPrimaryKey(forecastpositionId), theClientDto);
	}

	private boolean isKommissionierware(ForecastpositionDto posDto, TheClientDto theClientDto) throws RemoteException {
		StuecklisteDto stuecklisteDto = getStuecklisteFac()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(posDto.getArtikelIId(), theClientDto);
		if (stuecklisteDto == null)
			return false;

		LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(posDto.getIId());
		if (losDto != null)
			return true;

		String cnr = getParameterFac().getKommissionierungFertigungsgruppe(theClientDto.getMandant());
		if (Helper.isStringEmpty(cnr))
			return false;

		FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac()
				.fertigungsgruppeFindByMandantCNrCBez(theClientDto.getMandant(), cnr);
		return fertigungsgruppeDto.getIId().equals(stuecklisteDto.getFertigungsgruppeIId());
	}

	public void starteLinienabrufProduktion(Integer forecastpositionIId, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(forecastpositionIId, "forecastpositionIId");
		IForecastProduktProducer producer = producerFactory.getProducer(forecastpositionIId, theClientDto);
		producer.starteProduktion(forecastpositionIId, theClientDto);
		// LosDto losDto =
		// getFertigungFac().losFindByForecastpositionIIdOhneExc(
		// forecastpositionIId);
		// if (losDto != null) {
		// myLogger.info("Forecastposition (iId = " + forecastpositionIId
		// + ") ist bereits mit Los (iId = " + losDto.getIId()
		// + ") verbunden");
		// return;
		// }
		//
		// List<LosDto> lose = findLoseZuForecastposition(forecastpositionIId,
		// theClientDto);
		// if (lose.isEmpty()) {
		// throw createExceptionKeinLosGefunden(forecastpositionIId,
		// theClientDto);
		// }
		//
		// // List<Linienabruf> linienabrufe =
		// // LinienabrufQuery.listByForecastpositionIId(em,
		// forecastpositionIId);
		// ForecastpositionProduktionDto fpProduktionDto =
		// getLieferbareForecastpositionByIId(
		// forecastpositionIId, false, theClientDto);
		//
		// starteLinienabrufProduktionImpl(forecastpositionIId,
		// fpProduktionDto.getLinienabrufe(), lose.get(0),
		// lose.size() > 1 ? lose.subList(1, lose.size())
		// : new ArrayList<LosDto>(), theClientDto);
	}

	public void bucheZeitAufForecastposition(Integer forecastpositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		IForecastProduktProducer producer = producerFactory.getProducer(forecastpositionIId, theClientDto);
		producer.bucheZeitAufWare(forecastpositionIId, theClientDto);
		// LosDto losDto = getLosByForecastpositionIId(forecastpositionIId,
		// theClientDto);
		// ZeitdatenDto zDto = new ZeitdatenDto();
		// zDto.setPersonalIId(theClientDto.getIDPersonal());
		// Timestamp t = getTimestamp();
		// zDto.setTZeit(t);
		// zDto.setTAendern(t);
		// zDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
		// zDto.setIBelegartid(losDto.getIId());
		// LossollarbeitsplanDto[] lossollarbeitsplan = getFertigungFac()
		// .lossollarbeitsplanFindByLosIId(losDto.getIId());
		// if (lossollarbeitsplan.length < 1) {
		// return;
		// }
		// zDto.setArtikelIId(lossollarbeitsplan[0].getArtikelIIdTaetigkeit());
		// getZeiterfassungFac().createZeitdaten(zDto, true, true, true, false,
		// theClientDto);
	}

	// private void starteLinienabrufProduktionImpl(Integer forecastpositionIId,
	// List<LinienabrufProduktionDto> linienabrufe, LosDto hauptlosDto,
	// List<LosDto> weitereLose, TheClientDto theClientDto)
	// throws RemoteException {
	// hauptlosDto.setForecastpositionIId(forecastpositionIId);
	// getFertigungFac().updateLos(hauptlosDto, theClientDto);
	//
	// getFertigungFac().gebeLosAusOhneMaterialbuchung(hauptlosDto.getIId(),
	// false, false, theClientDto, null);
	//
	// BigDecimal linienabrufMenge = BigDecimal.ZERO;
	// for (LinienabrufDto la : linienabrufe) {
	// linienabrufMenge = linienabrufMenge.add(la.getNMenge());
	// }
	// if (linienabrufMenge.compareTo(hauptlosDto.getNLosgroesse()) != 0) {
	// // wenn nicht gleich dann anpassen und differenz auf naechstaeltere
	// // Lose uebertragen
	// BigDecimal differenz = linienabrufMenge.subtract(hauptlosDto
	// .getNLosgroesse());
	// getFertigungFac().aendereLosgroesse(hauptlosDto.getIId(),
	// linienabrufMenge.intValue(), false, theClientDto);
	// BigDecimal losgroesse;
	// for (LosDto losDto : weitereLose) {
	// losgroesse = losDto.getNLosgroesse().add(differenz.negate());
	// if (losgroesse.signum() <= 0) {
	// getFertigungFac().storniereLos(losDto.getIId(), false,
	// theClientDto);
	// differenz = losgroesse.negate();
	// if (losgroesse.signum() == 0)
	// break;
	// continue;
	// }
	// losDto.setNLosgroesse(losgroesse);
	// getFertigungFac().updateLos(losDto, theClientDto);
	// break;
	// // getFertigungFac().aendereLosgroesse(naechstesLosDto.getIId(),
	// // losgroesse.intValue(), false, theClientDto);
	// }
	// }
	// }

	public ForecastpositionProduktionDto produziereMaterial(ForecastpositionArtikelbuchungDto fpaDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (FclieferadresseNoka.NICHT_DEFINIERT.equals(fpaDto.getKommissioniertyp())) {
			// TODO
		}

		IForecastProduktProducer producer = producerFactory.getProducer(fpaDto.getForecastpositionIId(), theClientDto);
		producer.produziere(fpaDto.getForecastpositionIId(), fpaDto, theClientDto);
		// produziereMaterialImpl(fpaDto.getForecastpositionIId(), fpaDto,
		// theClientDto);

		// TODO eventuell Aufruf aendern
		return getLieferbareForecastpositionByIId(fpaDto.getForecastpositionIId(), true, theClientDto);
	}

	public LinienabrufArtikelDto produziereLinienabrufArtikel(LinienabrufArtikelbuchungDto labDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, "not implemented");
		// LinienabrufDto laDto = linienabrufFindByPrimaryKey(labDto
		// .getLinienabrufIId());
		// produziereMaterialImpl(laDto.getForecastpositionIId(), labDto,
		// theClientDto);
		//
		// ForecastpositionProduktionDto fcpositionProduktion =
		// getLieferbareForecastpositionByIId(
		// laDto.getForecastpositionIId(), true, theClientDto);
		// for (LinienabrufProduktionDto laProduktion : fcpositionProduktion
		// .getLinienabrufe()) {
		// if (laProduktion.getIId().equals(labDto.getLinienabrufIId())) {
		// for (LinienabrufArtikelDto laArtikel : laProduktion
		// .getLinienabrufArtikel()) {
		// if (laArtikel.getArtikelIId()
		// .equals(labDto.getArtikelIId())) {
		// return laArtikel;
		// }
		// }
		// }
		// }
		//
		// return null;
	}

	public void beendeLinienabrufProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
		Validator.notNull(forecastpositionIId, "forecastpositionIId");
		IForecastProduktProducer producer = producerFactory.getProducer(forecastpositionIId, theClientDto);
		producer.beendeProduktion(forecastpositionIId, kommissioniertyp, theClientDto);
		// LosDto losDto = getLosByForecastpositionIId(forecastpositionIId,
		// theClientDto);
		// BigDecimal bereitsAbgeliefert = BigDecimal.ZERO;
		// LosablieferungDto[] ablieferungen = getFertigungFac()
		// .losablieferungFindByLosIId(losDto.getIId(), false,
		// theClientDto);
		// for (LosablieferungDto dto : ablieferungen) {
		// bereitsAbgeliefert = bereitsAbgeliefert.add(dto.getNMenge());
		// }
		// BigDecimal abliefermenge = losDto.getNLosgroesse().subtract(
		// bereitsAbgeliefert);
		// createAblieferungLinienabrufProduktionImpl(losDto, abliefermenge,
		// true,
		// kommissioniertyp, theClientDto);
	}

	public void createAblieferungLinienabrufProduktion(Integer forecastpositionIId,
			FclieferadresseNoka kommissioniertyp, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		Validator.notNull(forecastpositionIId, "forecastpositionIId");
		IForecastProduktProducer producer = producerFactory.getProducer(forecastpositionIId, theClientDto);
		producer.createTeillieferung(forecastpositionIId, kommissioniertyp, theClientDto);
		// LosDto losDto = getLosByForecastpositionIId(forecastpositionIId,
		// theClientDto);
		// if (LocaleFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
		// throw new EJBExceptionLP(
		// EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
		// "FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, losCNr = "
		// + losDto.getCNr());
		// }
		// LossollmaterialDto[] lossollmaterial = getFertigungFac()
		// .lossollmaterialFindByLosIId(losDto.getIId());
		// List<Integer> paramArtikelgruppen =
		// getAusgeschlosseneArtikelgruppenIds(theClientDto);
		// BigDecimal minimum = null;
		// for (LossollmaterialDto materialDto : lossollmaterial) {
		// ArtikelDto artikelDto = getArtikelFac()
		// .artikelFindByPrimaryKeySmall(materialDto.getArtikelIId(),
		// theClientDto);
		// if (paramArtikelgruppen.contains(artikelDto.getArtgruIId()))
		// continue;
		// BigDecimal ausgegebeneMenge = getFertigungFac()
		// .getAusgegebeneMenge(materialDto.getIId(), null,
		// theClientDto);
		// BigDecimal sollsatzmenge = materialDto.getNMenge().divide(
		// losDto.getNLosgroesse(), BigDecimal.ROUND_HALF_EVEN);
		// BigDecimal vergleichsmenge = ausgegebeneMenge
		// .compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
		// : ausgegebeneMenge.divide(sollsatzmenge,
		// BigDecimal.ROUND_HALF_EVEN);
		// minimum = minimum == null
		// || minimum.compareTo(vergleichsmenge) == 1 ? vergleichsmenge
		// : minimum;
		// }
		//
		// // StuecklisteDto stklDto =
		// //
		// getStuecklisteFac().stuecklisteFindByPrimaryKey(losDto.getStuecklisteIId(),
		// // theClientDto);
		// // ArtikelDto artikelDto =
		// //
		// getArtikelFac().artikelFindByPrimaryKeySmall(stklDto.getArtikelIId(),
		// // theClientDto);
		// // BigDecimal kistenmenge = artikelDto.getNVerpackungsmittelmenge();
		// // kistenmenge = kistenmenge != null &&
		// // BigDecimal.ZERO.compareTo(kistenmenge) > 0 ? kistenmenge :
		// // BigDecimal.ONE;
		// //
		// // BigDecimal abliefermenge = minimum.divide(kistenmenge,
		// // RoundingMode.DOWN);
		// BigDecimal bereitsAbgeliefert = BigDecimal.ZERO;
		// LosablieferungDto[] ablieferungen = getFertigungFac()
		// .losablieferungFindByLosIId(losDto.getIId(), false,
		// theClientDto);
		// for (LosablieferungDto dto : ablieferungen) {
		// bereitsAbgeliefert = bereitsAbgeliefert.add(dto.getNMenge());
		// }
		// BigDecimal abliefermenge = minimum.subtract(bereitsAbgeliefert);
		// if (BigDecimal.ONE.compareTo(abliefermenge) > 0) {
		// throw
		// EJBExcFactory.linienabrufProduktionBerechneteAbliefermengeKleinerEins(losDto);
		// }
		// createAblieferungLinienabrufProduktionImpl(losDto,
		// abliefermenge.setScale(0, RoundingMode.DOWN), false,
		// kommissioniertyp, theClientDto);
	}

	public boolean isLinienabrufProduktionGestartet(Integer forecastpositionIId) {
		LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(forecastpositionIId);
		if (losDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_LINIENABRUF_PRODUKTION_NICHT_GESTARTET,
					"FEHLER_LINIENABRUF_PRODUKTION_NICHT_GESTARTET, kein Los f\u00FCr forecastpositionIId = "
							+ forecastpositionIId);
			// return false;
		}
		return true;
	}

	private ForecastProduktProducerFactory producerFactory = new ForecastProduktProducerFactory();

	public class ForecastProduktProducerFactory {
		public IForecastProduktBuilder getBuilder(Integer forecastpositionIId, TheClientDto theClientDto)
				throws RemoteException {
			ForecastpositionWare warentyp = getForecastpositionWarentyp(forecastpositionIId, theClientDto);
			if (ForecastpositionWare.KOMMISSIONIERWARE.equals(warentyp)) {
				return new KommissionierwareProducer();
			} else {
				return new LagerwareProducer();
			}
		}

		public IForecastProduktProducer getProducer(Integer forecastpositionIId, TheClientDto theClientDto)
				throws RemoteException {
			ForecastpositionWare warentyp = getForecastpositionWarentyp(forecastpositionIId, theClientDto);
			if (ForecastpositionWare.KOMMISSIONIERWARE.equals(warentyp)) {
				return new KommissionierwareProducer();
			} else {
				return new LagerwareProducer();
			}
		}
	}

	public interface IForecastProduktProducer {
		void starteProduktion(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException;

		void bucheZeitAufWare(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException;

		void produziere(Integer forecastpositionIId, ArtikelbuchungDto abDto, TheClientDto theClientDto)
				throws RemoteException;

		void beendeProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException;

		void createTeillieferung(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException;
	}

	public interface IForecastProduktBuilder {
		ForecastpositionProduktionDto setupForecastpositionProduktionDetail(FLRForecastpositionProduktion flrposition,
				FLRFclieferadresse flrfclieferadresse, TheClientDto theClientDto) throws RemoteException;
	}

	public abstract class WareProducer implements IForecastProduktProducer, IForecastProduktBuilder {

		protected void updateWare(Integer forecastpositionIId, String statusCnr) {
			updateWare(forecastpositionIId, statusCnr, null);
		}

		protected void updateWare(Integer forecastpositionIId, Integer personalIIdProduktion) {
			updateWare(forecastpositionIId, null, personalIIdProduktion);
		}

		protected void updateWare(Integer forecastpositionIId, String statusCnr, Integer personalIIdProduktion) {
			ForecastpositionDto fcpositionDto = forecastpositionFindByPrimaryKey(forecastpositionIId);
			if (statusCnr != null)
				fcpositionDto.setStatusCNr(statusCnr);
			if (personalIIdProduktion != null)
				fcpositionDto.setTProduktion(getTimestamp());
			if (personalIIdProduktion != null)
				fcpositionDto.setPersonalIIdProduktion(personalIIdProduktion);
			updateForecastposition(fcpositionDto);
		}

		protected void bucheMengeInForecastpositionLieferscheinposition(Integer forecastpositionIId, BigDecimal menge,
				FclieferadresseNoka kommissionierTyp, TheClientDto theClientDto) throws RemoteException {
			LieferscheinDto lieferscheinDto = getLieferscheinFuerKommissionierung(forecastpositionIId, kommissionierTyp,
					theClientDto);
			Forecastposition fcposition = em.find(Forecastposition.class, forecastpositionIId);
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(fcposition.getArtikelIId(),
					theClientDto);

			LieferscheinpositionDto[] liefposDtos = getLieferscheinpositionFac()
					.lieferscheinpositionFindByLieferscheinIId(lieferscheinDto.getIId());
			for (LieferscheinpositionDto posDto : liefposDtos) {
				if (forecastpositionIId.equals(posDto.getForecastpositionIId())) {
					posDto.setNMenge(menge.add(posDto.getNMenge()));
					getLieferscheinpositionFac().updateLieferscheinposition(posDto, theClientDto);
					return;
				}
			}

			LieferscheinpositionDto lspositionDto = getLieferscheinpositionFac()
					.setupLieferscheinpositionDto(lieferscheinDto, artikelDto, menge, theClientDto);
			lspositionDto.setForecastpositionIId(forecastpositionIId);
			getLieferscheinpositionFac().createLieferscheinposition(lspositionDto, false, theClientDto);
		}

		private LieferscheinDto getLieferscheinFuerKommissionierung(Integer forecastpositionIId,
				FclieferadresseNoka kommissionierTyp, TheClientDto theClientDto)
				throws RemoteException, EJBExceptionLP {
			ForecastpositionDto fcpositionDto = forecastpositionFindByPrimaryKey(forecastpositionIId);
			ForecastauftragDto fcauftragDto = forecastauftragFindByPrimaryKey(fcpositionDto.getForecastauftragIId());
			FclieferadresseDto fclieferadresseDto = fclieferadresseFindByPrimaryKey(
					fcauftragDto.getFclieferadresseIId());
			List<LieferscheinDto> lieferscheinDtos = getLieferscheinFac()
					.lieferscheinFindByKundeIIdLieferadresseMandantCNrStatusCNrOhneExc(
							fclieferadresseDto.getKundeIIdLieferadresse(), theClientDto.getMandant(),
							LieferscheinFac.LSSTATUS_ANGELEGT, theClientDto);

			if (!lieferscheinDtos.isEmpty()) {
				for (LieferscheinDto lieferscheinDto : lieferscheinDtos) {
					if (kommissionierTyp.equals(lieferscheinDto.getIKommissioniertyp())) {
						return lieferscheinDto;
					}
				}
			}

			return createLieferscheinFuerKommissionierung(kommissionierTyp, fclieferadresseDto, theClientDto);
		}

		private LieferscheinDto createLieferscheinFuerKommissionierung(FclieferadresseNoka kommissionierTyp,
				FclieferadresseDto fclieferadresseDto, TheClientDto theClientDto) throws RemoteException {
			KundeDto lieferkundeDto = getKundeFac()
					.kundeFindByPrimaryKeySmall(fclieferadresseDto.getKundeIIdLieferadresse());
			LieferscheinDto newLieferschein = getLieferscheinFac().setupDefaultLieferschein(lieferkundeDto,
					theClientDto);

			ForecastDto forecastDto = forecastFindByPrimaryKey(fclieferadresseDto.getForecastIId());
			if (!lieferkundeDto.getIId().equals(forecastDto.getKundeIId())) {
				KundeDto rechnungskundeDto = getKundeFac().kundeFindByPrimaryKeySmall(forecastDto.getKundeIId());
				newLieferschein.setKundeIIdRechnungsadresse(rechnungskundeDto.getIId());
				newLieferschein.setZahlungszielIId(rechnungskundeDto.getZahlungszielIId());
			}
			newLieferschein.setIKommissioniertyp(kommissionierTyp);

			Integer lieferscheinIId = getLieferscheinFac().createLieferschein(newLieferschein, theClientDto);
			return getLieferscheinFac().lieferscheinFindByPrimaryKey(lieferscheinIId, theClientDto);
		}
	}

	public class LagerwareProducer extends WareProducer {

		@Override
		public void starteProduktion(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException {
			// TODO FCPos Status aendern
			updateWare(forecastpositionIId, LocaleFac.STATUS_IN_PRODUKTION, theClientDto.getIDPersonal());
		}

		@Override
		public void bucheZeitAufWare(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException {
			updateWare(forecastpositionIId, theClientDto.getIDPersonal());
		}

		@Override
		public void produziere(Integer forecastpositionIId, ArtikelbuchungDto abDto, TheClientDto theClientDto)
				throws RemoteException {
			Forecastposition fcposition = em.find(Forecastposition.class, forecastpositionIId);
			if (!abDto.getArtikelIId().equals(fcposition.getArtikelIId())) {
				// TODO Exception, darf nicht sein
			}

			bucheMengeInForecastpositionLieferscheinposition(forecastpositionIId, abDto.getMenge(),
					abDto.getKommissioniertyp(), theClientDto);

			if (isProduktionErledigt(forecastpositionIId, abDto.getKommissioniertyp(), theClientDto)) {
				beendeProduktion(forecastpositionIId, abDto.getKommissioniertyp(), theClientDto);
				return;
			}

			if (!LocaleFac.STATUS_TEILERLEDIGT.equals(fcposition.getStatusCNr())) {
				updateWare(forecastpositionIId, LocaleFac.STATUS_TEILERLEDIGT);
			}
		}

		private boolean isProduktionErledigt(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) {
			BigDecimal bedarfsmenge = getBedarfsmenge(forecastpositionIId, kommissioniertyp);
			BigDecimal gelieferteMenge = getBereitsGelieferteMenge(forecastpositionIId);
			return bedarfsmenge.compareTo(gelieferteMenge) <= 0;
		}

		private BigDecimal getBedarfsmenge(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp) {
			if (FclieferadresseNoka.ARTIKEL.equals(kommissioniertyp)) {
				Forecastposition fcposition = em.find(Forecastposition.class, forecastpositionIId);
				return fcposition.getNMenge();
			} else if (FclieferadresseNoka.LINIENABRUF.equals(kommissioniertyp)) {
				return getSummeLinienabrufe(forecastpositionIId);
			}
			return BigDecimal.ZERO;
		}

		@Override
		public void beendeProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException {
			ForecastpositionDto fcpositionDto = forecastpositionFindByPrimaryKey(forecastpositionIId);
			fcpositionDto.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
			updateForecastposition(fcpositionDto);
		}

		@Override
		public void createTeillieferung(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException {
			// nicht noetig, da schon Produktion auf Lieferschein
		}

		@Override
		public ForecastpositionProduktionDto setupForecastpositionProduktionDetail(
				FLRForecastpositionProduktion flrposition, FLRFclieferadresse flrfclieferadresse,
				TheClientDto theClientDto) throws RemoteException {
			ForecastpositionProduktionDto dto = setupForecastpositionProduktionHeadData(flrposition, flrfclieferadresse,
					theClientDto);
			dto.setWarentyp(ForecastpositionWare.LAGERWARE);

			BigDecimal gelieferteMenge = getBereitsGelieferteMenge(dto.getIId());
			for (LinienabrufProduktionDto linienabruf : dto.getLinienabrufe()) {
				LinienabrufArtikelDto laArtikel = new LinienabrufArtikelDto();
				laArtikel.setArtikelIId(dto.getArtikelIId());
				laArtikel.setMenge(linienabruf.getNMenge());
				laArtikel.setLagerstand(dto.getLagerstand());
				laArtikel.setOffeneMenge(gelieferteMenge.compareTo(linienabruf.getNMenge()) >= 0 ? BigDecimal.ZERO
						: linienabruf.getNMenge().subtract(gelieferteMenge));
				gelieferteMenge = BigDecimal.ZERO.compareTo(gelieferteMenge.subtract(linienabruf.getNMenge())) >= 0
						? BigDecimal.ZERO
						: gelieferteMenge.subtract(linienabruf.getNMenge());
				// BigDecimal offeneMenge =
				// dto.getNMenge().subtract(gelieferteMenge);
				linienabruf.getLinienabrufArtikel().add(laArtikel);
			}

			return dto;
		}
	}

	public class KommissionierwareProducer extends WareProducer {
		@Override
		public void starteProduktion(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException {
			// TODO FCPos Status auf IN_PRODUKTION
			LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(forecastpositionIId);
			if (losDto != null) {
				bucheZeitAufWare(forecastpositionIId, theClientDto);
				myLogger.info("Forecastposition (iId = " + forecastpositionIId + ") ist bereits mit Los (iId = "
						+ losDto.getIId() + ") verbunden");
				return;
			}

			List<LosDto> lose = findLoseZuForecastposition(forecastpositionIId, theClientDto);
			if (lose.isEmpty()) {
				throw createExceptionKeinLosGefunden(forecastpositionIId, theClientDto);
			}

			ForecastpositionProduktionDto fpProduktionDto = getLieferbareForecastpositionByIId(forecastpositionIId,
					false, theClientDto);

			starteProduktionImpl(forecastpositionIId, fpProduktionDto.getLinienabrufe(), lose.get(0),
					lose.size() > 1 ? lose.subList(1, lose.size()) : new ArrayList<LosDto>(), theClientDto);
			bucheZeitAufWare(forecastpositionIId, theClientDto);
		}

		private void starteProduktionImpl(Integer forecastpositionIId, List<LinienabrufProduktionDto> linienabrufe,
				LosDto hauptlosDto, List<LosDto> weitereLose, TheClientDto theClientDto) throws RemoteException {
			hauptlosDto.setForecastpositionIId(forecastpositionIId);
			hauptlosDto = getFertigungFac().updateLos(hauptlosDto, theClientDto);

			getFertigungFac().gebeLosAusOhneMaterialbuchung(hauptlosDto.getIId(), false, false, theClientDto, null,
					true);

			BigDecimal linienabrufMenge = BigDecimal.ZERO;
			for (LinienabrufDto la : linienabrufe) {
				linienabrufMenge = linienabrufMenge.add(la.getNMenge());
			}
			if (linienabrufMenge.compareTo(hauptlosDto.getNLosgroesse()) != 0) {
				// wenn nicht gleich dann anpassen und differenz auf
				// naechstaeltere
				// Lose uebertragen
				BigDecimal differenz = linienabrufMenge.subtract(hauptlosDto.getNLosgroesse());
				getFertigungFac().aendereLosgroesse(hauptlosDto.getIId(), linienabrufMenge.intValue(), false,
						theClientDto);
				if (weitereLose.isEmpty()) {
					verteileDifferenzAufNeuesLos(differenz, hauptlosDto, theClientDto);
				} else {
					verteileDifferenzAufWeitereLose(differenz, weitereLose, theClientDto);
				}
			}
			updateWare(forecastpositionIId, LocaleFac.STATUS_IN_PRODUKTION);
		}

		private void verteileDifferenzAufNeuesLos(BigDecimal differenz, LosDto losvorlageDto, TheClientDto theClientDto)
				throws RemoteException {
			if (differenz.signum() >= 0)
				return;

			losvorlageDto.setIId(null);
			losvorlageDto.setNLosgroesse(differenz.negate());
			losvorlageDto.setForecastpositionIId(null);
			getFertigungFac().createLos(losvorlageDto, theClientDto);
		}

		private void verteileDifferenzAufWeitereLose(BigDecimal differenz, List<LosDto> weitereLose,
				TheClientDto theClientDto) throws RemoteException {
			BigDecimal losgroesse;
			for (LosDto losDto : weitereLose) {
				losgroesse = losDto.getNLosgroesse().add(differenz.negate());
				if (losgroesse.signum() <= 0) {
					getFertigungFac().storniereLos(losDto.getIId(), false, theClientDto);
					differenz = losgroesse.negate();
					if (losgroesse.signum() == 0)
						break;
					continue;
				}
				losDto.setNLosgroesse(losgroesse);
				getFertigungFac().updateLos(losDto, theClientDto);
				break;
				// getFertigungFac().aendereLosgroesse(naechstesLosDto.getIId(),
				// losgroesse.intValue(), false, theClientDto);
			}
		}

		@Override
		public void bucheZeitAufWare(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException {
			LosDto losDto = getLosByForecastpositionIId(forecastpositionIId, theClientDto);
			ZeitdatenDto zDto = new ZeitdatenDto();
			zDto.setPersonalIId(theClientDto.getIDPersonal());
			Timestamp t = getTimestamp();
			zDto.setTZeit(t);
			zDto.setTAendern(t);
			zDto.setCBelegartnr(LocaleFac.BELEGART_LOS);
			zDto.setIBelegartid(losDto.getIId());
			LossollarbeitsplanDto[] lossollarbeitsplan = getFertigungFac()
					.lossollarbeitsplanFindByLosIId(losDto.getIId());
			if (lossollarbeitsplan.length < 1) {
				return;
			}
			zDto.setArtikelIId(lossollarbeitsplan[0].getArtikelIIdTaetigkeit());
			getZeiterfassungFac().createZeitdaten(zDto, true, true, true, false, theClientDto);

			updateWare(forecastpositionIId, theClientDto.getIDPersonal());
		}

		@Override
		public void produziere(Integer forecastpositionIId, ArtikelbuchungDto abDto, TheClientDto theClientDto)
				throws RemoteException {
			LosDto losDto = getLosByForecastpositionIId(forecastpositionIId, theClientDto);

			LossollmaterialDto[] lossollmaterial = getFertigungFac().lossollmaterialFindByLosIId(losDto.getIId());
			for (LossollmaterialDto materialDto : lossollmaterial) {
				if (materialDto.getArtikelIId().equals(abDto.getArtikelIId())) {
					LosistmaterialDto losistmaterialDto = new LosistmaterialDto();
					losistmaterialDto.setLagerIId(losDto.getLagerIIdZiel());
					losistmaterialDto.setBAbgang(Helper.boolean2Short(true));
					losistmaterialDto.setNMenge(abDto.getMenge());
					getFertigungFac().gebeMaterialNachtraeglichAus(materialDto, losistmaterialDto, null, true, true,
							theClientDto);
				}
			}

			if (isProduktionErledigt(losDto.getIId(), theClientDto)) {
				beendeProduktion(forecastpositionIId, abDto.getKommissioniertyp(), theClientDto);
			}
		}

		private boolean isProduktionErledigt(Integer losIId, TheClientDto theClientDto) throws RemoteException {
			LossollmaterialDto[] lossollmaterial;
			lossollmaterial = getFertigungFac().lossollmaterialFindByLosIId(losIId);
			boolean bErledigt = true;
			List<Integer> artgruIds = getAusgeschlosseneArtikelgruppenIds(theClientDto);
			for (LossollmaterialDto materialDto : lossollmaterial) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(materialDto.getArtikelIId(),
						theClientDto);
				if (artgruIds.contains(artikelDto.getArtgruIId()))
					continue;

				BigDecimal ausgegebeneMenge = getFertigungFac().getAusgegebeneMenge(materialDto.getIId(), null,
						theClientDto);
				if (ausgegebeneMenge.compareTo(materialDto.getNMenge()) < 0) {
					bErledigt = false;
					break;
				}
			}
			return bErledigt;
		}

		@Override
		public void beendeProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException, EJBExceptionLP {
			LosDto losDto = getLosByForecastpositionIId(forecastpositionIId, theClientDto);
			BigDecimal bereitsAbgeliefert = BigDecimal.ZERO;
			LosablieferungDto[] ablieferungen = getFertigungFac().losablieferungFindByLosIId(losDto.getIId(), false,
					theClientDto);
			for (LosablieferungDto dto : ablieferungen) {
				bereitsAbgeliefert = bereitsAbgeliefert.add(dto.getNMenge());
			}
			BigDecimal abliefermenge = losDto.getNLosgroesse().subtract(bereitsAbgeliefert);
			createAblieferungInLieferschein(losDto, abliefermenge, true, kommissioniertyp, theClientDto);
			updateWare(forecastpositionIId, LocaleFac.STATUS_ERLEDIGT);
		}

		@Override
		public void createTeillieferung(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp,
				TheClientDto theClientDto) throws RemoteException {
			LosDto losDto = getLosByForecastpositionIId(forecastpositionIId, theClientDto);
			if (LocaleFac.STATUS_ERLEDIGT.equals(losDto.getStatusCNr())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT,
						"FEHLER_FERTIGUNG_DAS_LOS_IST_BEREITS_ERLEDIGT, losCNr = " + losDto.getCNr());
			}
			LossollmaterialDto[] lossollmaterial = getFertigungFac().lossollmaterialFindByLosIId(losDto.getIId());
			List<Integer> paramArtikelgruppen = getAusgeschlosseneArtikelgruppenIds(theClientDto);
			BigDecimal minimum = null;
			for (LossollmaterialDto materialDto : lossollmaterial) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(materialDto.getArtikelIId(),
						theClientDto);
				if (paramArtikelgruppen.contains(artikelDto.getArtgruIId()))
					continue;
				BigDecimal ausgegebeneMenge = getFertigungFac().getAusgegebeneMenge(materialDto.getIId(), null,
						theClientDto);
				BigDecimal sollsatzmenge = materialDto.getNMenge().divide(losDto.getNLosgroesse(),
						BigDecimal.ROUND_HALF_EVEN);
				BigDecimal vergleichsmenge = ausgegebeneMenge.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
						: ausgegebeneMenge.divide(sollsatzmenge, BigDecimal.ROUND_HALF_EVEN);
				minimum = minimum == null || minimum.compareTo(vergleichsmenge) == 1 ? vergleichsmenge : minimum;
			}

			BigDecimal bereitsAbgeliefert = BigDecimal.ZERO;
			LosablieferungDto[] ablieferungen = getFertigungFac().losablieferungFindByLosIId(losDto.getIId(), false,
					theClientDto);
			for (LosablieferungDto dto : ablieferungen) {
				bereitsAbgeliefert = bereitsAbgeliefert.add(dto.getNMenge());
			}
			BigDecimal abliefermenge = minimum.subtract(bereitsAbgeliefert);
			if (BigDecimal.ONE.compareTo(abliefermenge) > 0) {
				throw EJBExcFactory.linienabrufProduktionBerechneteAbliefermengeKleinerEins(losDto);
			}
			createAblieferungInLieferschein(losDto, abliefermenge.setScale(0, RoundingMode.DOWN), false,
					kommissioniertyp, theClientDto);
			updateWare(forecastpositionIId, LocaleFac.STATUS_TEILERLEDIGT);
		}

		private LosDto getLosByForecastpositionIId(Integer forecastpositionIId, TheClientDto theClientDto) {
			Validator.notNull(forecastpositionIId, "forecastpositionIId");
			LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(forecastpositionIId);
			if (losDto == null) {
				throw createExceptionKeinLosGefunden(forecastpositionIId, theClientDto);
			}
			return losDto;
		}

		private EJBExceptionLP createExceptionKeinLosGefunden(Integer forecastpositionIId, TheClientDto theClientDto) {
			Forecastposition fcp = em.find(Forecastposition.class, forecastpositionIId);
			String artikelCnr = getArtikelFac().artikelFindByPrimaryKeySmall(fcp.getArtikelIId(), theClientDto)
					.getCNr();
			Forecastauftrag fca = em.find(Forecastauftrag.class, fcp.getForecastauftragIId());
			Fclieferadresse fcl = em.find(Fclieferadresse.class, fca.getFclieferadresseIId());
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(fcl.getKundeIIdLieferadresse(), theClientDto);
			return EJBExcFactory.linienabrufProduktionKeinLosGefunden(kundeDto.getPartnerDto().getCKbez(), artikelCnr,
					forecastpositionIId);
		}

		private void createAblieferungInLieferschein(LosDto losDto, BigDecimal menge, boolean erledigeLos,
				FclieferadresseNoka kommissionierTyp, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			createLosablieferung(losDto, menge, erledigeLos, theClientDto);
			bucheMengeInForecastpositionLieferscheinposition(losDto.getForecastpositionIId(), menge, kommissionierTyp,
					theClientDto);
		}

		private void createLosablieferung(LosDto losDto, BigDecimal menge, boolean erledigeLos,
				TheClientDto theClientDto) throws RemoteException {
			LosablieferungDto losablieferungDto = new LosablieferungDto();
			losablieferungDto.setLosIId(losDto.getIId());
			losablieferungDto.setNMenge(menge);
			LosablieferungResultDto laResultDto = getFertigungFac().createLosablieferung(losablieferungDto,
					theClientDto, erledigeLos, true);
			losablieferungDto = laResultDto.getLosablieferungDto();
		}

		@Override
		public ForecastpositionProduktionDto setupForecastpositionProduktionDetail(
				FLRForecastpositionProduktion flrposition, FLRFclieferadresse flrfclieferadresse,
				TheClientDto theClientDto) throws RemoteException {
			ForecastpositionProduktionDto fcposition = setupForecastpositionProduktionHeadData(flrposition,
					flrfclieferadresse, theClientDto);
			fcposition.setWarentyp(ForecastpositionWare.KOMMISSIONIERWARE);

			Integer losIId = null;
			if (flrposition.getFlrlos() != null) {
				losIId = flrposition.getFlrlos().getI_id();
				fcposition.setLosCnr(flrposition.getFlrlos().getC_nr());
			}

			if (losIId == null) {
				losIId = findLosZuForecastposition(flrposition.getI_id(), theClientDto);
				if (losIId == null) {
					throw createExceptionKeinLosGefunden(flrposition.getI_id(), theClientDto);
				}
			}

			LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
			LossollmaterialDto[] lossollmaterial = getFertigungFac().lossollmaterialFindByLosIId(losDto.getIId());
			List<Integer> paramArtikelgruppen = getAusgeschlosseneArtikelgruppenIds(theClientDto);
			Integer hauptlagerIId = getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId();

			for (LossollmaterialDto materialDto : lossollmaterial) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(materialDto.getArtikelIId(),
						theClientDto);
				if (paramArtikelgruppen.contains(artikelDto.getArtgruIId()))
					continue;

				checkMaterialArtikelForLinienabrufProduktion(losDto, materialDto, artikelDto);
				fcposition.getHmArtikelLinienabrufe().put(artikelDto.getIId(), artikelDto);
				BigDecimal lagerstand = getLagerFac().getLagerstandOhneExc(artikelDto.getIId(), hauptlagerIId,
						theClientDto);
				BigDecimal ausgegebeneMenge = getFertigungFac().getAusgegebeneMenge(materialDto.getIId(), null,
						theClientDto);

				for (LinienabrufProduktionDto linienabruf : fcposition.getLinienabrufe()) {
					LinienabrufArtikelDto laArtikel = new LinienabrufArtikelDto();
					laArtikel.setArtikelIId(artikelDto.getIId());
					laArtikel.setMenge(
							materialDto.getNMenge().divide(losDto.getNLosgroesse(), BigDecimal.ROUND_HALF_EVEN)
									.multiply(linienabruf.getNMenge()));
					BigDecimal offeneMenge = ausgegebeneMenge.compareTo(laArtikel.getMenge()) >= 0 ? BigDecimal.ZERO
							: laArtikel.getMenge().subtract(ausgegebeneMenge);
					ausgegebeneMenge = BigDecimal.ZERO.compareTo(ausgegebeneMenge.subtract(laArtikel.getMenge())) >= 0
							? BigDecimal.ZERO
							: ausgegebeneMenge.subtract(laArtikel.getMenge());
					laArtikel.setOffeneMenge(offeneMenge);
					laArtikel.setLagerstand(lagerstand);
					linienabruf.getLinienabrufArtikel().add(laArtikel);
				}
			}

			return fcposition;
		}

		private Integer findLosZuForecastposition(Integer forecastpositionIId, TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			LosDto losDto = getFertigungFac().losFindByForecastpositionIIdOhneExc(forecastpositionIId);
			if (losDto != null)
				return losDto.getIId();

			List<LosDto> lose = findLoseZuForecastposition(forecastpositionIId, theClientDto);

			return lose.isEmpty() ? null : lose.get(0).getIId();
		}

		/**
		 * @param forecastpositionIId
		 * @param theClientDto
		 * @return
		 * @throws RemoteException
		 */
		private List<LosDto> findLoseZuForecastposition(Integer forecastpositionIId, TheClientDto theClientDto)
				throws RemoteException {
			ForecastpositionDto fpDto = forecastpositionFindByPrimaryKey(forecastpositionIId);
			StuecklisteDto stuecklisteDto = getStuecklisteFac()
					.stuecklisteFindByMandantCNrArtikelIIdOhneExc(fpDto.getArtikelIId(), theClientDto);
			if (stuecklisteDto == null) {
				ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKeySmall(fpDto.getArtikelIId(),
						theClientDto);
				throw EJBExcFactory.linienabrufProduktionStklNichtImMandant(artikelDto.getCNr(),
						theClientDto.getMandant());
			}
			List<String> stati = new ArrayList<String>();
			stati.add(LocaleFac.STATUS_ANGELEGT);

			FertigungsgruppeDto fertigungsgruppeDto = getStuecklisteFac().fertigungsgruppeFindByMandantCNrCBez(
					theClientDto.getMandant(),
					getParameterFac().getKommissionierungFertigungsgruppe(theClientDto.getMandant()));
			List<LosDto> lose = getFertigungFac().losFindByStuecklisteIIdFertigungsgruppeIIdStatusCnr(
					stuecklisteDto.getIId(), fertigungsgruppeDto.getIId(), stati);
			if (lose == null || lose.isEmpty())
				return new ArrayList<LosDto>();

			Collections.sort(lose, new Comparator<LosDto>() {
				@Override
				public int compare(LosDto o1, LosDto o2) {
					int compare = o1.getTProduktionsbeginn().compareTo(o2.getTProduktionsbeginn());
					if (compare != 0)
						return compare;

					return o1.getCNr().compareTo(o2.getCNr());
				}
			});
			return lose;
		}

		private List<Integer> getAusgeschlosseneArtikelgruppenIds(TheClientDto theClientDto)
				throws EJBExceptionLP, RemoteException {
			List<String> paramArtikelgruppen = getParameterFac()
					.getKommissionierungAusgeschlosseneArtikelgruppen(theClientDto.getMandant());
			ArtgruDto[] artgruDtos = getArtikelFac().artgruFindAll();
			List<Integer> ids = new ArrayList<Integer>();

			for (String artgruCnr : paramArtikelgruppen) {
				for (ArtgruDto artgruDto : artgruDtos) {
					if (artgruCnr.equals(artgruDto.getCNr())) {
						ids.add(artgruDto.getIId());
					}
				}
			}
			return ids;
		}

		private void checkMaterialArtikelForLinienabrufProduktion(LosDto losDto, LossollmaterialDto sollmaterialDto,
				ArtikelDto artikelDto) {
			BigDecimal sollsatzmenge = sollmaterialDto.getNMenge().divide(losDto.getNLosgroesse(),
					BigDecimal.ROUND_HALF_EVEN);
			try {
				sollsatzmenge.intValueExact();
			} catch (ArithmeticException ex) {
				throw EJBExcFactory.linienabrufProduktionSollsatzgroessenNichtGanzzahlig(losDto, artikelDto.getCNr());
			}

			if (artikelDto.getFVerpackungsmenge() == null) {
				myLogger.warn("Verpackungsmenge von Artikel \"" + artikelDto.getCNr() + "\" ist nicht definiert");
			} else if (artikelDto.getFVerpackungsmenge().compareTo(new Double(0)) <= 0) {
				myLogger.warn("Verpackungsmenge von Artikel \"" + artikelDto.getCNr()
						+ "\"  hat keinen g\u00FCltigen Wert (= " + artikelDto.getFVerpackungsmenge() + ")");
			}
		}
	}

	public ForecastpositionProduktionDto setupForecastpositionProduktionHeadData(
			FLRForecastpositionProduktion flrposition, FLRFclieferadresse flrfclieferadresse, TheClientDto theClientDto)
			throws RemoteException {
		ForecastpositionProduktionDto fcposition = mapForecastpositionProduktion(flrposition);

		if (flrposition.getLinienabrufset() != null && !flrposition.getLinienabrufset().isEmpty()) {
			for (Object flrlinienabruf : flrposition.getLinienabrufset()) {
				LinienabrufProduktionDto linienabruf = mapLinienabrufProduktion((FLRLinienabruf) flrlinienabruf);
				fcposition.getLinienabrufe().add(linienabruf);
			}
			Collections.sort(fcposition.getLinienabrufe(), new LinienabrufComparator());
		} else {
			if (Helper.short2boolean(flrfclieferadresse.getB_kommissionieren())
					|| Helper.short2boolean(flrposition.getFlrartikel().getB_kommissionieren())) {
				// Noka-Artikel
				LinienabrufProduktionDto linienabruf = new LinienabrufProduktionDto();
				linienabruf.setCBestellnummer(flrposition.getC_bestellnummer());
				linienabruf.setCLinie("NoKa");
				ArtikelDto nokaArtikel = getArtikelFac()
						.artikelFindByPrimaryKeySmall(flrposition.getFlrartikel().getI_id(), theClientDto);
				linienabruf.setCBereichBez(nokaArtikel.getArtikelsprDto().getCZbez2());
				linienabruf.setForecastpositionIId(flrposition.getI_id());
				linienabruf.setNMenge(flrposition.getN_menge());
				fcposition.getLinienabrufe().add(linienabruf);
			}
		}

		fcposition.setLagerstand(getLagerFac().getLagerstandOhneExc(fcposition.getArtikelIId(),
				getLagerFac().getHauptlagerDesMandanten(theClientDto).getIId(), theClientDto));
		fcposition.setPersonalIIdKommissionierer(flrposition.getPersonal_i_id_produktion());
		fcposition.setFclieferadresseIId(flrfclieferadresse.getI_id());

		return fcposition;
	}

	public ForecastpositionWare getForecastpositionWarentyp(Integer forecastpositionIId, TheClientDto theClientDto)
			throws RemoteException {
		if (isKommissionierware(forecastpositionIId, theClientDto))
			return ForecastpositionWare.KOMMISSIONIERWARE;

		return ForecastpositionWare.LAGERWARE;
	}

	@Override
	public List<String> getStatiEinerForecastposition() {
		List<String> stati = new ArrayList<String>();
		stati.add(LocaleFac.STATUS_ANGELEGT);
		stati.add(LocaleFac.STATUS_IN_PRODUKTION);
		stati.add(LocaleFac.STATUS_TEILERLEDIGT);
		stati.add(LocaleFac.STATUS_GELIEFERT);
		stati.add(LocaleFac.STATUS_ERLEDIGT);
		return stati;
	}
}
