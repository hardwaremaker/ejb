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
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.system.ejb.Installer;
import com.lp.server.system.ejb.LpUserCount;
import com.lp.server.system.ejb.Theclient;
import com.lp.server.system.fastlanereader.generated.FLRInstaller;
import com.lp.server.system.fastlanereader.generated.FLRTheClient;
import com.lp.server.system.service.InstallerDto;
import com.lp.server.system.service.InstallerDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientDtoAssembler;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class TheClientFacBean extends Facade implements TheClientFac {

	@PersistenceContext
	private EntityManager em;

	public void createTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP {

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,
					new Exception("theClientDto == null"));
		}

		try {
			Theclient theClient = new Theclient(theClientDto.getIDUser(),
					theClientDto.getBenutzername(),
					theClientDto.getKennwortAsString(),
					theClientDto.getMandant(),
					theClientDto.getSMandantenwaehrung(),
					theClientDto.getIDPersonal(),
					theClientDto.getLocUiAsString(),
					theClientDto.getLocMandantAsString(),
					theClientDto.getLocKonzernAsString(),
					theClientDto.getDLoggedin(), theClientDto.getSystemrolleIId());
			em.persist(theClient);
			em.flush();
			setTheClientFromTheClientDto(theClient, theClientDto);
			updateUserCount(theClientDto.getSystemrolleIId());
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (PersistenceException ex) {
			throwEJBExceptionLPforPersistence(ex);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE,
					new Exception(t));
		}

	}

	public void removeTheClientTVonTBis(java.sql.Timestamp tVon,
			java.sql.Timestamp tBis) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("TheClientfindByTLoggedIn");
			query.setParameter(1, tVon);
			query.setParameter(2, tBis);
			Collection<?> cl = query.getResultList();
			// if(cl.isEmpty()){
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
			// }
			TheClientDto[] dtos = assembleTheClientDtos(cl);

			for (int i = 0; i < dtos.length; i++) {
				Theclient theClient = em.find(Theclient.class,
						dtos[i].getIDUser());
				if (theClient == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
				}
				Integer systemrolleIId = theClient.getSystemrolleIId();
				em.remove(theClient);
				
				updateUserCount(systemrolleIId);
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}
	}

//	public void removeTheClient(String cNr) throws EJBExceptionLP, RemoteException {
//		Theclient toRemove = em.find(Theclient.class, cNr);
//		if (toRemove == null) {
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
//		}
//		try {
//			Integer systemrolleIId = toRemove.getSystemrolleIId();
//			em.remove(toRemove);
//			em.flush();
//			updateUserCount(systemrolleIId);
//		} catch (EntityExistsException er) {
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
//		}
//	}

//	public void removeTheClient(TheClientDto theClientDto)
//			throws EJBExceptionLP, RemoteException {
//		if (theClientDto != null) {
//			String cNr = theClientDto.getIDUser();
//			removeTheClient(cNr);
//			updateUserCount(theClientDto.getSystemrolleIId());
//		}
//	}

	public void updateTheClient(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		if (theClientDto != null) {
			String cNr = theClientDto.getIDUser();
			Theclient theClient = em.find(Theclient.class, cNr);
			if (theClient == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setTheClientFromTheClientDto(theClient, theClientDto);
			updateUserCount(theClientDto.getSystemrolleIId());
		}
	}
	
	protected void updateUserCount(Integer systemrolleIId) throws EJBExceptionLP, RemoteException {
		/*
		 * Beim Uebergang vom alten auf das neue Benutzerzaehlersystem
		 * kann es vorkommen, dass keine systemrolle gesetzt ist. Deshalb
		 * wird in einem solchen Fall kein Benutzerzaehler abgeaendert.
		 */
		if(systemrolleIId == null) return ;
		
		Integer maxUsers = getBenutzerFac().systemrolleFindByPrimaryKey(systemrolleIId).getIMaxUsers();
		LpUserCount userCount = new LpUserCount();
		Integer anzahl = (maxUsers == null ? getUserCountMandant() : getUserCountRolle(systemrolleIId));
		userCount.setIAnzahl(anzahl);
		userCount.setSystemrolleIId(systemrolleIId);
		userCount.setTZeitpunkt(getTimestamp());
		em.persist(userCount);
		em.flush();
	}
	

	/**
	 * Z&auml;hlt die angemeldeten Benutzer in einer Systemrolle am heutigen Tag ab 00:00 Uhr.
	 * Ob diese Rolle auch einen eigenen UserCounter hat, wird hier nicht gepr&uuml;ft.
	 * @param systemrolleIId
	 * @return die Anzahl der angemeldeten Benutzer
	 */
	protected Integer getUserCountRolle(Integer systemrolleIId) {
		Session session = FLRSessionFactory.getFactory().openSession();
		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
		Criteria c = session.createCriteria(FLRTheClient.class);
		c.createCriteria("flrsystemrolle").add(Restrictions.eq("i_id", systemrolleIId));
		c.add(Restrictions.isNull("t_loggedout"))
			.add(Restrictions.ge("t_loggedin", today))
			.add(Restrictions.not(Restrictions.like("c_benutzername", "lpwebappzemecs|%")))
			.setProjection(Projections.countDistinct("c_benutzername"));;
		return (Integer) c.list().get(0);
	}
	
	/**
	 * Z&auml;hlt die angemeldeten Benutzer am heutigen Tag ab 00:00 Uhr, welche nicht eine Systemrolle mit eigenem UserCounter
	 * haben.
	 * @return die Anzahl der angemeldeten Benutzer.
	 */
	protected Integer getUserCountMandant() {
		
		Session session = FLRSessionFactory.getFactory().openSession();
		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
		Criteria c = session.createCriteria(FLRTheClient.class);
		c.createCriteria("flrsystemrolle").add(Restrictions.isNull("i_max_users"));
		c.add(Restrictions.isNull("t_loggedout"))
			.add(Restrictions.ge("t_loggedin", today))
			.setProjection(Projections.countDistinct("c_benutzername"));
		return (Integer) c.list().get(0);
	}

	public void updateTheClients(TheClientDto[] theClientDtos)
			throws EJBExceptionLP, RemoteException {
		if (theClientDtos != null) {
			for (int i = 0; i < theClientDtos.length; i++) {
				updateTheClient(theClientDtos[i]);
			}
		}
	}

	public TheClientDto theClientFindByPrimaryKey(String cNrUserI)
			throws EJBExceptionLP {
		TheClientDto theClient = theClientFindByPrimaryKeyOhneExc(cNrUserI);
		if(theClient == null)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		return theClient;
	}

	public TheClientDto theClientFindByPrimaryKeyOhneExc(String cNrUserI)
			throws EJBExceptionLP {
		Theclient client = em.find(Theclient.class, cNrUserI);
		return client == null ? null : loadClientDto(client) ;
	}

	private TheClientDto loadClientDto(Theclient theClient) {
		TheClientDto theClientDto = assembleTheClientDto(theClient) ;
		theClientDto.setSystemrolleIId(getTheJudgeFac().getSystemrolleIId(theClientDto));
		return theClientDto ;
	}

	/**
	 * Pruefe, ob ein User noch eingeloggt ist
	 * 
	 * @param cNr
	 *            String
	 * @throws EJBExceptionLP
	 *             wenn er nicht mehr eingeloggt ist
	 * @return TheClientDto
	 */
	public TheClientDto theClientFindByUserLoggedIn(String cNr)
			throws EJBExceptionLP {
		Theclient theclient;
		try {
			Query query = em.createNamedQuery("TheClientfindByUserLoggedIn");
			query.setParameter(1, cNr);
			query.setParameter(2, getTimestamp());
			theclient = (Theclient) query.getSingleResult();
		} catch (NoResultException e) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_THECLIENT_WURDE_GELOESCHT, e);
		}

		TheClientDto clientDto = loadClientDto(theclient) ;
// TODO: Wieder reaktivieren, bricht derzeit tests
//		getLogonFac().validateLoggedIn(clientDto);
		return clientDto ;
	}

	public TheClientDto theClientFindByCBenutzernameLoggedIn(
			String cBenutzername) throws EJBExceptionLP {
		Theclient theclient = null;
		try {
			Query query = em
					.createNamedQuery("TheClientfindByCBenutzernameLoggedIn");
			query.setParameter(1, cBenutzername);
			query.setParameter(2, Helper.cutTimestamp(getTimestamp()));
			query.setMaxResults(1);
			theclient = (Theclient) query.getSingleResult();
		} catch (NoResultException e) {
			//
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}
		
		return theclient == null ? null : loadClientDto(theclient) ;
	}

	private void setTheClientFromTheClientDto(Theclient theclient,
			TheClientDto theClientDto) throws EJBExceptionLP {
		theclient.setCMandant(theClientDto.getMandant());
		theclient.setIPersonal(theClientDto.getIDPersonal());
		theclient.setCUilocale(theClientDto.getLocUiAsString());
		theclient.setCKonzernlocale(theClientDto.getLocKonzernAsString());
		theclient.setCMandantenlocale(theClientDto.getLocMandantAsString());
		theclient.setCBenutzername(theClientDto.getBenutzername());
		theclient.setTLoggedin(theClientDto.getDLoggedin());
		theclient.setTLoggedout(theClientDto.getTsLoggedout());
		theclient.setCMandantwaehrung(theClientDto.getSMandantenwaehrung());
		theclient.setIStatus(theClientDto.getIStatus());
		theclient.setSystemrolleIId(theClientDto.getSystemrolleIId());
		em.merge(theclient);
		em.flush();
	}

	private TheClientDto assembleTheClientDto(Theclient theClient)
			throws EJBExceptionLP {
		return TheClientDtoAssembler.createDto(theClient);
	}

	private TheClientDto[] assembleTheClientDtos(Collection<?> theClients)
			throws Exception {
		List<TheClientDto> list = new ArrayList<TheClientDto>();
		if (theClients != null) {
			Iterator<?> iterator = theClients.iterator();
			while (iterator.hasNext()) {
				Theclient theClient = (Theclient) iterator.next();
				list.add(assembleTheClientDto(theClient));
			}
		}
		TheClientDto[] returnArray = new TheClientDto[list.size()];
		return (TheClientDto[]) list.toArray(returnArray);
	}

	public void putInstaller(InstallerDto installerDto) throws EJBExceptionLP {
		if (installerDto == null) {
			return;
		}
		// try {

		Installer installer = em.find(Installer.class,
				TheClientFac.PK_INSTALLER);
		if (installer == null) {
			try {
				installer = new Installer(installerDto.getIId(),
						installerDto.getOClientpc(),
						installerDto.getIBuildnummerclientpc());
				em.persist(installer);
				em.flush();
			} catch (EntityExistsException ex) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, "");
			}

		}
		setInstallerFromInstallerDto(installer, installerDto);
	}

	public void removeInstaller() throws EJBExceptionLP {
		Installer toRemove = em
				.find(Installer.class, TheClientFac.PK_INSTALLER);
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

	public boolean istNeuerClientVerfuegbar(Integer iBuildnummerclient) {
		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Criteria crit = session
				.createCriteria(FLRInstaller.class);

		List<?> results = crit.list();
		if (results.size() > 0) {
			FLRInstaller installer = (FLRInstaller) results.iterator().next();
			if (iBuildnummerclient < installer.getI_buildnummerclientpc()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public InstallerDto getInstaller() throws EJBExceptionLP {
		// try {
		Installer installer = em.find(Installer.class,
				TheClientFac.PK_INSTALLER);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
		// }
		// catch (FinderException ex) {
		// return null;
		// }
	}

	public InstallerDto getInstallerWithoutClientFile() throws EJBExceptionLP {
		// try {
		Installer installer = em.find(Installer.class,
				TheClientFac.PK_INSTALLER);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
		// { // @ToDo null Pruefung?
		// return null;
		// }
		// }
		// catch (FinderException ex) {
		// return null;
		// }
	}

	public InstallerDto getInstallerPart(Integer iPart) throws EJBExceptionLP {
		Installer installer = em.find(Installer.class, iPart);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
	}

	private void setInstallerFromInstallerDto(Installer installer,
			InstallerDto installerDto) {
		installer.setOClientpc(installerDto.getOClientpc());
		installer.setIBuildnummerclientpc(installerDto
				.getIBuildnummerclientpc());
		em.merge(installer);
		em.flush();
	}

	private InstallerDto assembleInstallerDto(Installer installer) {
		return InstallerDtoAssembler.createDto(installer);
	}

	private InstallerDto assembleInstallerDtoWithoutClientFile(
			Installer installer) {
		return InstallerDtoAssembler.createDtoWithoutClientFile(installer);
	}

	private InstallerDto[] assembleInstallerDtos(Collection<?> installers) {
		List<InstallerDto> list = new ArrayList<InstallerDto>();
		if (installers != null) {
			Iterator<?> iterator = installers.iterator();
			while (iterator.hasNext()) {
				Installer installer = (Installer) iterator.next();
				list.add(assembleInstallerDto(installer));
			}
		}
		InstallerDto[] returnArray = new InstallerDto[list.size()];
		return (InstallerDto[]) list.toArray(returnArray);
	}


	@Override
	public int logoutAllClients(boolean remove) throws EJBExceptionLP {
		int removedSessions = 0 ;
		
		myLogger.warn("Beginne alle noch angemeldeten Benutzer abzumelden") ;
		try {
			HvTypedQuery<Theclient> currentUsers = new HvTypedQuery<Theclient>(
					em.createNamedQuery("TheClientfindLoggedIn")) ;
			List<Theclient> users = currentUsers.getResultList() ;

			myLogger.warn("Es gibt " + users.size() + " angemeldete Benutzer") ;
			
			Timestamp logoutTimestamp = getTimestamp() ;
			for (Theclient theclient : users) {
				Integer rolleId = theclient.getSystemrolleIId();
				if(remove) {
					em.remove(theclient);
				} else {
					theclient.setTLoggedout(logoutTimestamp);
					em.merge(theclient) ;
				}
				updateUserCount(rolleId);
				++removedSessions ;
			}
			
			myLogger.warn("Ende alle noch angemeldeten Benutzer abzumelden") ;
			
			return removedSessions ;
		} catch(Exception e) {
			throw new EJBExceptionLP(e) ;
		}
	}
}
