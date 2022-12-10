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
package com.lp.server.benutzer.ejbfac;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.lp.server.benutzer.fastlanereader.generated.FLRRollerecht;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerServicesFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.system.ejb.ParametermandantPK;
import com.lp.server.system.ejb.Text;
import com.lp.server.system.ejbfac.ParameterFacBean;
import com.lp.server.system.fastlanereader.generated.FLRArbeitsplatzparameter;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Singleton
@Local(BenutzerServicesFacLocal.class)
public class BenutzerServicesFacBean extends Facade implements BenutzerServicesFac, BenutzerServicesFacLocal {
	public SessionFactory getSessionFactory() {

		return factory;
	}

	@PersistenceContext(unitName = "ejb")
	private EntityManager em;

	@PersistenceUnit
	private SessionFactory factory;
	@PersistenceContext
	private Session session1;

	private HashMap<Integer, HashMap<String, String>> hmRolleRechte = null;
	private HashMap<String, ArrayList<ArbeitsplatzparameterDto>> hmArbeitsplatzparameter = null;

	private HashMap<String, HashMap<String, HashMap<String, String>>> hmLPText = null;
	private ParameterMandantCachingProvider cacheParametermandant = new ParameterMandantCachingProvider();

	private final Set<String> hsParameterMandantZeitabhaenigCNr = createParameterMandantZeitabhaenigCNr();

	public void setHashMapUseCaseHandler(HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> hm) {
		useCaseHandlers = hm;
	}

	public HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> getHashMapUseCaseHandler() {

		if (this.useCaseHandlers == null) {
			this.useCaseHandlers = new HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>>();
		}
		iCleanupZaehler++;

		// durchsuchen und diese loeschen
		if (iCleanupZaehler > 1000 && useCaseHandlers != null) {

			Iterator<String> it = useCaseHandlers.keySet().iterator();

			while (it.hasNext()) {

				String idUser = it.next();

				HashMap<Integer, HashMap<String, UseCaseHandler>> temp = useCaseHandlers.get(it.next());

				Iterator<Integer> itUsecases = temp.keySet().iterator();

				while (itUsecases.hasNext()) {
					Integer useCaseKey = itUsecases.next();
					HashMap<String, UseCaseHandler> usecasesEinesclients = temp.get(useCaseKey);

					Iterator<String> itListen = usecasesEinesclients.keySet().iterator();
					while (itListen.hasNext()) {
						String key = itListen.next();
						UseCaseHandler uc = usecasesEinesclients.get(key);

						long lErstellung = uc.getTErstellung().getTime();

						// Nach 2 Tagen wird der eintrag aus der Liste entfernt

						if ((lErstellung + (24 * 3600000 * 2)) < System.currentTimeMillis()) {
							usecasesEinesclients.remove(key);
						}

					}

					temp.put(useCaseKey, usecasesEinesclients);
				}

				useCaseHandlers.put(idUser, temp);

			}

			iCleanupZaehler = 0;
		}

		return useCaseHandlers;
	}

	private int iCleanupZaehler = 0;

	private HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> useCaseHandlers;

	public synchronized void reloadRolleRechte() {
		boolean subscriptionAbgelaufen = false;
		try {
			AnwenderDto anwenderDto = getSystemFac().anwenderFindByPrimaryKey(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER);
			subscriptionAbgelaufen = anwenderDto.getTSubscription() == null ? false
					: anwenderDto.getTSubscription().before(getTimestamp());
		} catch (RemoteException e) {
			myLogger.error("", e);
		} catch (EJBExceptionLP e) {
			myLogger.error("", e);
		}

		hmRolleRechte = new HashMap<Integer, HashMap<String, String>>();
		Session session = getNewSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRRollerecht.class);

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRRollerecht rollerecht = (FLRRollerecht) resultListIterator.next();

			Integer systemrolleIId = rollerecht.getFlrsystemrolle().getI_id();
			String rechtCNr = rollerecht.getFlrrecht().getC_nr().trim();

			if (subscriptionAbgelaufen && rechtCNr.endsWith("CUD"))
				rechtCNr = rechtCNr.substring(0, rechtCNr.lastIndexOf("CUD")) + "R";

			HashMap<String, String> hmSystemrolle = null;

			if (hmRolleRechte.containsKey(systemrolleIId)) {
				hmSystemrolle = hmRolleRechte.get(systemrolleIId);

			} else {

				hmSystemrolle = new HashMap<String, String>();

			}

			if (hmSystemrolle == null) {
				int z = 0;
			}

			hmSystemrolle.put(rechtCNr, rechtCNr);
			hmRolleRechte.put(systemrolleIId, hmSystemrolle);

		}

	}

	public void reloadArbeitsplatzparameter() {

		hmArbeitsplatzparameter = new HashMap<String, ArrayList<ArbeitsplatzparameterDto>>();

		Session session = getNewSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRArbeitsplatzparameter.class);

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRArbeitsplatzparameter flrArbeitsplatzparameter = (FLRArbeitsplatzparameter) resultListIterator.next();

			String arbeitsplatz = flrArbeitsplatzparameter.getFlrarbeitsplatz().getC_pcname().toLowerCase();

			ArrayList<ArbeitsplatzparameterDto> alParameter = null;

			if (hmArbeitsplatzparameter.containsKey(arbeitsplatz)) {
				alParameter = hmArbeitsplatzparameter.get(arbeitsplatz);

			} else {

				alParameter = new ArrayList<ArbeitsplatzparameterDto>();

			}
			ArbeitsplatzparameterDto apDto = new ArbeitsplatzparameterDto();

			apDto.setArbeitsplatzIId(flrArbeitsplatzparameter.getI_id());
			apDto.setCWert(flrArbeitsplatzparameter.getC_wert());
			apDto.setParameterCNr(flrArbeitsplatzparameter.getFlrparameter().getC_nr());

			alParameter.add(apDto);

			hmArbeitsplatzparameter.put(arbeitsplatz, alParameter);

		}

	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(String cPcname, String parameterCNr) {

		if (hmArbeitsplatzparameter == null) {
			reloadArbeitsplatzparameter();
		}

		ArrayList<ArbeitsplatzparameterDto> alParameter = hmArbeitsplatzparameter.get(cPcname.toLowerCase());

		ArbeitsplatzparameterDto apDto = null;
		if (alParameter != null) {
			for (int i = 0; i < alParameter.size(); i++) {

				if (alParameter.get(i).getParameterCNr().equals(parameterCNr)) {
					apDto = alParameter.get(i);
					break;
				}
			}
		}
		return apDto;
	}

	public boolean isMSSQL() {
		return FLRSessionFactory.isMSSQL();
	}

	// TODO loeschen wenn nicht mehr benoetigt
//	private synchronized void reloadParametermandant() {
//		int changeCount = countParametermandantChanges.get();
//		if (changeCount == 0)
//			return;
//
//		Session session = getNewSession();
//
//		org.hibernate.Criteria crit = session.createCriteria(FLRParametermandant.class);
//
//		List<?> results = crit.list();
//		Iterator<?> resultListIterator = results.iterator();
//
//		hmParametermandant.clear();
//
//		while (resultListIterator.hasNext()) {
//			FLRParametermandant parametermandant = (FLRParametermandant) resultListIterator.next();
//
//			ParametermandantDto pmDto = new ParametermandantDto();
//
//			pmDto.setCNr(parametermandant.getId_comp().getC_nr());
//			pmDto.setCDatentyp(parametermandant.getC_datentyp());
//			pmDto.setCKategorie(parametermandant.getId_comp().getC_kategorie());
//			// pmDto.setCWert(parametermandant.getC_wert());
//			String decodedValue = Helper.istKennwortParameter(parametermandant.getId_comp().getC_nr())
//					? Helper.decode(parametermandant.getC_wert())
//					: parametermandant.getC_wert();
//			pmDto.setCWert(decodedValue);
//
//			pmDto.setMandantCMandant(parametermandant.getId_comp().getMandant_c_nr());
//			pmDto.setCBemerkungsmall(parametermandant.getC_bemerkungsmall());
//			pmDto.setCBemerkunglarge(parametermandant.getC_bemerkunglarge());
//
//			if (getMandantparameterZeitabhaenigCNr().contains(pmDto.getCNr())) {
//
//				pmDto.setTmWerteGueltigab(getParameterFac().parametermandantgueltigabGetWerteZumZeitpunkt(
//						parametermandant.getId_comp().getMandant_c_nr(), parametermandant.getId_comp().getC_nr(),
//						parametermandant.getId_comp().getC_kategorie()));
//			}
//
//			hmParametermandant.put(
//					new ParametermandantPK(pmDto.getCNr(), pmDto.getMandantCMandant(), pmDto.getCKategorie()), pmDto);
//
//		}
//
//		countParametermandantChanges.addAndGet(-changeCount);
//
//	}

	public void reloadUebersteuertenText() {

		hmLPText = new HashMap<String, HashMap<String, HashMap<String, String>>>();

		Query query = em.createNamedQuery("TextfindAll");
		Collection<?> c = query.getResultList();

		Iterator<?> resultListIterator = c.iterator();

		while (resultListIterator.hasNext()) {
			Text text = (Text) resultListIterator.next();

			String mandantCNr = text.getPk().getMandantCNr();

			HashMap<String, HashMap<String, String>> hmMandant = null;

			if (hmLPText.containsKey(mandantCNr)) {
				hmMandant = hmLPText.get(mandantCNr);

			} else {

				hmMandant = new HashMap<String, HashMap<String, String>>();

			}

			HashMap<String, String> hmLocale = null;

			String localeCNr = text.getPk().getLocaleCNr();
			if (hmMandant.containsKey(localeCNr)) {
				hmLocale = hmMandant.get(localeCNr);
			} else {

				hmLocale = new HashMap<String, String>();
			}

			hmLocale.put(text.getPk().getCToken(), text.getCText());

			hmMandant.put(localeCNr, hmLocale);
			hmLPText.put(mandantCNr, hmMandant);

		}

	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI) {

		if (hmLPText == null) {
			reloadUebersteuertenText();
		}

		HashMap<String, HashMap<String, String>> hmMandant = hmLPText.get(mandantCNr);

		String locale = Helper.locale2String(loI);

		if (hmMandant != null && hmMandant.containsKey(locale)) {

			HashMap<String, String> hmLocale = hmMandant.get(locale);

			if (hmLocale != null && hmLocale.containsKey(sTokenI)) {
				return hmLocale.get(sTokenI);
			}

		}

		return HelperServer.getTextRespectUISpr(sTokenI, loI);

	}

	public String getTextRespectUISpr(String sTokenI, String mandantCNr, Locale loI, Object... replacements) {
		String msg = getTextRespectUISpr(sTokenI, mandantCNr, loI);
		return MessageFormat.format(msg, replacements);
	}

	public boolean hatRecht(String rechtCNr, TheClientDto theClientDto) {

		if (hmRolleRechte == null) {
			reloadRolleRechte();
		}
		Integer systemrolleIId = theClientDto.getSystemrolleIId();
		HashMap<String, String> hmRechte = hmRolleRechte.get(systemrolleIId);

		if (hmRechte == null) {
			return false;
		}

		if (hmRechte.containsKey(rechtCNr.trim())) {

			return true;
		} else {
			return false;
		}

	}

	public boolean hatRechtInZielmandant(String rechtCNr, String mandantCNrZiel, TheClientDto theClientDto) {

		String benutzername = theClientDto.getBenutzername().trim().substring(0,
				theClientDto.getBenutzername().indexOf("|"));

		BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(benutzername);

		if (benutzerDto != null && benutzerDto.getIId() != null) {
			BenutzermandantsystemrolleDto benutzermandantsystemrolleDto = getBenutzerFac()
					.benutzermandantsystemrolleFindByBenutzerIIdMandantCNrOhneExc(benutzerDto.getIId(), mandantCNrZiel);

			if (benutzermandantsystemrolleDto != null) {

				Session session = FLRSessionFactory.getFactory().openSession();
				org.hibernate.Criteria crit = session.createCriteria(FLRRollerecht.class);

				crit.createAlias("flrsystemrolle", "s")
						.add(Restrictions.eq("s.i_id", benutzermandantsystemrolleDto.getSystemrolleIId()));

				crit.createAlias("flrrecht", "r").add(Restrictions.eq("r.c_nr", rechtCNr));

				List<?> results = crit.list();
				Iterator<?> resultListIterator = results.iterator();

				if (resultListIterator.hasNext()) {
					return true;
				} else {
					return false;
				}

			}

		}

		return false;

	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr, String cKategorieI,
			String mandantparameter_c_nr) {
		return getMandantparameter(mandant_c_nr, cKategorieI, mandantparameter_c_nr, null);
	}

	private ParametermandantDto setzeWertZumZeitpunkt(ParametermandantDto parametermandantDto,
			java.sql.Timestamp tZeitpunkt) {

		if (tZeitpunkt != null) {

			ParametermandantDto pmDtoClone = ParametermandantDto.clone(parametermandantDto);

			pmDtoClone.setCWert(pmDtoClone.getCWertZumZeitpunkt(tZeitpunkt));
			return pmDtoClone;

		} else {
			return parametermandantDto;
		}

	}

	private HvOptional<ParametermandantDto> getParametermandantFromMap(String mandant_c_nr,
			String mandantparameter_c_nr, String cKategorieI) {
		ParametermandantPK pk = new ParametermandantPK(mandantparameter_c_nr, mandant_c_nr, cKategorieI);
		return cacheParametermandant.get(pk);

	}

	public ParametermandantDto getMandantparameter(String mandant_c_nr, String cKategorieI,
			String mandantparameter_c_nr, java.sql.Timestamp tZeitpunkt) {

		HvOptional<ParametermandantDto> parametermandantDto = getParametermandantFromMap(mandant_c_nr,
				mandantparameter_c_nr, cKategorieI);
		if (parametermandantDto.isPresent()) {
			return setzeWertZumZeitpunkt(parametermandantDto.get(), tZeitpunkt);
		}

		// Wenn nich gefunden, dann beim Hauptmandant suchen
		try {
			AnwenderDto anwenderDto = getSystemFac()
					.anwenderFindByPrimaryKey(new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

			parametermandantDto = getParametermandantFromMap(anwenderDto.getMandantCNrHauptmandant(),
					mandantparameter_c_nr, cKategorieI);

			if (parametermandantDto.isPresent()) {
				return setzeWertZumZeitpunkt(parametermandantDto.get(), tZeitpunkt);
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Parameter auch nicht als Hauptparameter verfuegbar

		String[][] progMandantParameter = ParameterFacBean.progMandantParameter;
		ParametermandantDto parametermandant = null;
		for (int i = 0; i < progMandantParameter.length; i++) {
			if (cKategorieI.equals(progMandantParameter[i][0])
					&& mandantparameter_c_nr.equals(progMandantParameter[i][1])) {
				parametermandant = new ParametermandantDto();
				parametermandant.setCDatentyp(progMandantParameter[i][3]);
				parametermandant.setCNr(mandantparameter_c_nr);
				parametermandant.setCKategorie(cKategorieI);
				parametermandant.setCWert(progMandantParameter[i][2]);
				parametermandant.setCBemerkungsmall(progMandantParameter[i][4]);
				parametermandant.setCBemerkunglarge(progMandantParameter[i][5]);
				break;
			}
		}
		if (parametermandant == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, new Exception(
					"Mandantparameter " + cKategorieI + "." + mandantparameter_c_nr + " kann nicht gefunden werden !"));
		}

		return parametermandant;

	}

	public Set<String> getMandantparameterZeitabhaenigCNr() {
		return hsParameterMandantZeitabhaenigCNr;
	}

	@Override
	public boolean hatRechtOder(TheClientDto theClientDto, String[] rechtCnrs) {
		for (String rechtCnr : rechtCnrs) {
			if (hatRecht(rechtCnr, theClientDto)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hatRechtUnd(TheClientDto theClientDto, String[] rechtCnrs) {
		for (String rechtCnr : rechtCnrs) {
			if (!hatRecht(rechtCnr, theClientDto)) {
				return false;
			}
		}
		return true;
	}

	public void markMandantparameterModified(ParametermandantPK pk) {
		cacheParametermandant.invalidate(pk);
	}

	@Override
	public void markAllMandantenparameterModified() {
		cacheParametermandant.invalidateAll();
	}

	private Set<String> createParameterMandantZeitabhaenigCNr() {
		Set<String> parametermandantZeitabhaenigCNr = new HashSet<String>();
		parametermandantZeitabhaenigCNr.add(ParameterFac.MATERIALGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.PARAMETER_GEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.PARAMETER_VORSTEUER_BEI_WE_EINSTANDSPREIS);
		parametermandantZeitabhaenigCNr.add(ParameterFac.ARBEITSZEITGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.FERTIGUNGSGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.ENTWICKLUNGSGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.VERWALTUNGSGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.VERTRIEBSGEMEINKOSTENFAKTOR);
		parametermandantZeitabhaenigCNr.add(ParameterFac.PARAMETER_AUFSCHLAG1);
		parametermandantZeitabhaenigCNr.add(ParameterFac.PARAMETER_AUFSCHLAG2);
		parametermandantZeitabhaenigCNr.add(ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_HISTORISCH);
		return Collections.unmodifiableSet(parametermandantZeitabhaenigCNr);
	}

	private class ParameterMandantCachingProvider {

		private Map<ParametermandantPK, ParametermandantDto> cache = new ConcurrentHashMap<ParametermandantPK, ParametermandantDto>();

		public HvOptional<ParametermandantDto> get(ParametermandantPK key) {
			if (cache.containsKey(key))
				return HvOptional.of(cache.get(key));
			HvOptional<ParametermandantDto> newVal = create(key);
			if (newVal.isPresent()) {
				cache.put(key, newVal.get());
			}
			return newVal;
		}

		public void invalidate(ParametermandantPK key) {
			cache.remove(key);
		}

		public void invalidateAll() {
			cache.clear();
		}

		private HvOptional<ParametermandantDto> create(ParametermandantPK key) {
			try {
				HvOptional<ParametermandantDto> optional = getParameterFac()
						.parametermandantFindByPrimaryKeyOptional(key);
				if (optional.isPresent()) {
					ParametermandantDto dto = optional.get();
					if (BenutzerServicesFacBean.this.getMandantparameterZeitabhaenigCNr().contains(dto.getCNr())) {
						dto.setTmWerteGueltigab(getParameterFac().parametermandantgueltigabGetWerteZumZeitpunkt(
								dto.getMandantCMandant(), dto.getCNr(), dto.getCKategorie()));
					}
					return HvOptional.of(dto);
				}
				return optional;
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}

		}

	}

}