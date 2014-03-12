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
package com.lp.server.benutzer.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;
import org.jboss.annotation.ejb.Service;

import com.lp.server.benutzer.fastlanereader.generated.FLRRollerecht;
import com.lp.server.benutzer.service.BenutzerServicesFac;
import com.lp.server.system.ejb.Text;
import com.lp.server.system.ejbfac.ParameterFacBean;
import com.lp.server.system.fastlanereader.generated.FLRArbeitsplatzparameter;
import com.lp.server.system.fastlanereader.generated.FLRParametermandant;
import com.lp.server.system.service.AnwenderDto;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Service
@Local(BenutzerServicesFacLocal.class)
public class BenutzerServicesFacBean extends Facade implements
		BenutzerServicesFac, BenutzerServicesFacLocal {

	@PersistenceContext(unitName = "ejb")
	private EntityManager em;

	private HashMap<Integer, HashMap<String, String>> hmRolleRechte = null;
	private HashMap<String, ArrayList<ArbeitsplatzparameterDto>> hmArbeitsplatzparameter = null;

	private HashMap<String, HashMap<String, HashMap<String, String>>> hmLPText = null;
	private HashMap<String, ArrayList<ParametermandantDto>> hmParametermandant = null;

	public void setHashMapUseCaseHandler(HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> hm) {
		useCaseHandlers=hm;
	}
	public HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>> getHashMapUseCaseHandler(){
		
		if (this.useCaseHandlers == null) {
			this.useCaseHandlers = new HashMap<String, HashMap<Integer, HashMap<String, UseCaseHandler>>>();
		}
		iCleanupZaehler++;
		
		// durchsuchen und diese loeschen
		if (iCleanupZaehler > 1000 && useCaseHandlers != null) {

			Iterator<String> it = useCaseHandlers.keySet().iterator();

			while (it.hasNext()) {

				String idUser = it.next();

				HashMap<Integer, HashMap<String, UseCaseHandler>> temp = useCaseHandlers
						.get(it.next());

				Iterator<Integer> itUsecases = temp.keySet().iterator();

				while (itUsecases.hasNext()) {
					Integer useCaseKey = itUsecases.next();
					HashMap<String, UseCaseHandler> usecasesEinesclients = temp
							.get(useCaseKey);

					Iterator<String> itListen = usecasesEinesclients.keySet()
							.iterator();
					while (itListen.hasNext()) {
						String key = itListen.next();
						UseCaseHandler uc = usecasesEinesclients.get(key);

						long lErstellung = uc.getTErstellung().getTime();

						// Nach 2 Tagen wird der eintrag aus der Liste entfernt

						if ((lErstellung + (24 * 3600000 * 2)) < System
								.currentTimeMillis()) {
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
	
	public void reloadRolleRechte() {

		hmRolleRechte = new HashMap<Integer, HashMap<String, String>>();
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRRollerecht.class);

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRRollerecht rollerecht = (FLRRollerecht) resultListIterator
					.next();

			Integer systemrolleIId = rollerecht.getFlrsystemrolle().getI_id();
			String rechtCNr = rollerecht.getFlrrecht().getC_nr().trim();

			HashMap<String, String> hmSystemrolle = null;

			if (hmRolleRechte.containsKey(systemrolleIId)) {
				hmSystemrolle = hmRolleRechte.get(systemrolleIId);

			} else {

				hmSystemrolle = new HashMap<String, String>();

			}
			hmSystemrolle.put(rechtCNr, rechtCNr);
			hmRolleRechte.put(systemrolleIId, hmSystemrolle);

		}

	}

	public void reloadArbeitsplatzparameter() {

		hmArbeitsplatzparameter = new HashMap<String, ArrayList<ArbeitsplatzparameterDto>>();

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRArbeitsplatzparameter.class);

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRArbeitsplatzparameter flrArbeitsplatzparameter = (FLRArbeitsplatzparameter) resultListIterator
					.next();

			String arbeitsplatz = flrArbeitsplatzparameter.getFlrarbeitsplatz()
					.getC_pcname().toLowerCase();

			ArrayList<ArbeitsplatzparameterDto> alParameter = null;

			if (hmArbeitsplatzparameter.containsKey(arbeitsplatz)) {
				alParameter = hmArbeitsplatzparameter.get(arbeitsplatz);

			} else {

				alParameter = new ArrayList<ArbeitsplatzparameterDto>();

			}
			ArbeitsplatzparameterDto apDto = new ArbeitsplatzparameterDto();

			apDto.setArbeitsplatzIId(flrArbeitsplatzparameter.getI_id());
			apDto.setCWert(flrArbeitsplatzparameter.getC_wert());
			apDto.setParameterCNr(flrArbeitsplatzparameter.getFlrparameter()
					.getC_nr());

			alParameter.add(apDto);

			hmArbeitsplatzparameter.put(arbeitsplatz, alParameter);

		}

	}

	public ArbeitsplatzparameterDto holeArbeitsplatzparameter(String cPcname,
			String parameterCNr) {

		if (hmArbeitsplatzparameter == null) {
			reloadArbeitsplatzparameter();
		}

		ArrayList<ArbeitsplatzparameterDto> alParameter = hmArbeitsplatzparameter
				.get(cPcname.toLowerCase());

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

	public void reloadParametermandant() {

		hmParametermandant = new HashMap<String, ArrayList<ParametermandantDto>>();
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRParametermandant.class);

		List<?> results = crit.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRParametermandant parametermandant = (FLRParametermandant) resultListIterator
					.next();

			String mandantCNr = parametermandant.getId_comp().getMandant_c_nr();

			ArrayList<ParametermandantDto> parameter = null;

			if (hmParametermandant.containsKey(mandantCNr)) {
				parameter = hmParametermandant.get(mandantCNr);

			} else {

				parameter = new ArrayList<ParametermandantDto>();

			}

			ParametermandantDto pmDto = new ParametermandantDto();

			pmDto.setCNr(parametermandant.getId_comp().getC_nr());
			pmDto.setCDatentyp(parametermandant.getC_datentyp());
			pmDto.setCKategorie(parametermandant.getId_comp().getC_kategorie());
			pmDto.setCWert(parametermandant.getC_wert());
			pmDto.setMandantCMandant(parametermandant.getId_comp()
					.getMandant_c_nr());
			pmDto.setCBemerkungsmall(parametermandant.getC_bemerkungsmall()) ;
			pmDto.setCBemerkunglarge(parametermandant.getC_bemerkunglarge()) ;
			parameter.add(pmDto);

			hmParametermandant.put(mandantCNr, parameter);

		}

	}

	public void reloadUebersteuertenText() {

		hmLPText = new HashMap<String, HashMap<String, HashMap<String, String>>>();

		Query query = em.createNamedQuery("TextfindAll");
		Collection<Text> c = query.getResultList();

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

	public String getTextRespectUISpr(String sTokenI, String mandantCNr,
			Locale loI) {

		if (hmLPText == null) {
			reloadUebersteuertenText();
		}

		HashMap<String, HashMap<String, String>> hmMandant = hmLPText
				.get(mandantCNr);

		String locale = Helper.locale2String(loI);

		if (hmMandant != null && hmMandant.containsKey(locale)) {

			HashMap<String, String> hmLocale = hmMandant.get(locale);

			if (hmLocale != null && hmLocale.containsKey(sTokenI)) {
				return hmLocale.get(sTokenI);
			}

		}

		return HelperServer.getTextRespectUISpr(sTokenI, loI);

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

	public ParametermandantDto getMandantparameter(String mandant_c_nr,
			String cKategorieI, String mandantparameter_c_nr) {

		if (hmParametermandant == null) {
			reloadParametermandant();
		}

		ArrayList<ParametermandantDto> alParameter = hmParametermandant
				.get(mandant_c_nr);
		ParametermandantDto parametermandantDto = null;
		if (alParameter != null) {

			for (int i = 0; i < alParameter.size(); i++) {

				if (alParameter.get(i).getCNr().equals(mandantparameter_c_nr)
						&& alParameter.get(i).getCKategorie()
								.equals(cKategorieI)) {
					return alParameter.get(i);
				}

			}
		}

		// Wenn nich gefunden, dann beim Hauptmandant suchen
		try {
			AnwenderDto anwenderDto = getSystemFac().anwenderFindByPrimaryKey(
					new Integer(SystemFac.PK_HAUPTMANDANT_IN_LP_ANWENDER));

			alParameter = hmParametermandant.get(anwenderDto
					.getMandantCNrHauptmandant());

			if (alParameter != null) {
				for (int i = 0; i < alParameter.size(); i++) {

					if (alParameter.get(i).getCNr()
							.equals(mandantparameter_c_nr)
							&& alParameter.get(i).getCKategorie()
									.equals(cKategorieI)) {
						return alParameter.get(i);
					}

				}
			}

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		if (parametermandantDto == null) {

			// Parameter auch nicht als Hauptparameter verfuegbar

			String[][] progMandantParameter = ParameterFacBean.progMandantParameter;

			for (int i = 0; i < progMandantParameter.length; i++) {
				if (cKategorieI.equals(progMandantParameter[i][0])
						&& mandantparameter_c_nr
								.equals(progMandantParameter[i][1])) {
					parametermandantDto = new ParametermandantDto();
					parametermandantDto
							.setCDatentyp(progMandantParameter[i][3]);
					parametermandantDto.setCNr(mandantparameter_c_nr);
					parametermandantDto.setCKategorie(cKategorieI);
					parametermandantDto.setCWert(progMandantParameter[i][2]);
					parametermandantDto
							.setCBemerkungsmall(progMandantParameter[i][4]);
					parametermandantDto
							.setCBemerkunglarge(progMandantParameter[i][5]);
					break;
				}
			}
			if (parametermandantDto == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,
						new Exception("Mandantparameter " + cKategorieI + "."
								+ mandantparameter_c_nr
								+ " kann nicht gefunden werden !"));
			}
		}

		return parametermandantDto;

	}
}
