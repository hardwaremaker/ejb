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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantLock;

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

import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.personal.ejb.Hvmabenutzer;
import com.lp.server.personal.ejb.HvmabenutzerQuery;
import com.lp.server.system.ejb.Installer;
import com.lp.server.system.ejb.LpHvmaUserCount;
import com.lp.server.system.ejb.LpUserCount;
import com.lp.server.system.ejb.Theclient;
import com.lp.server.system.fastlanereader.generated.FLRInstaller;
import com.lp.server.system.fastlanereader.generated.FLRTheClient;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.InstallerDto;
import com.lp.server.system.service.InstallerDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.TheClientDtoAssembler;
import com.lp.server.system.service.TheClientFac;
import com.lp.server.system.service.TheClientLoggedInDto;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class TheClientFacBean extends Facade implements TheClientFac, TheClientFacLocal {

	private static final ReentrantLock clientLock = new ReentrantLock();

	@PersistenceContext
	private EntityManager em;

	public void createTheClient(TheClientDto theClientDto) throws EJBExceptionLP {

		// precondition
		if (theClientDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, new Exception("theClientDto == null"));
		}

		myLogger.info("Client-Lockcount (lock): " + clientLock.getHoldCount() + ".");
		clientLock.lock();
		
		try {
			Theclient theClient = new Theclient(theClientDto.getIDUser(), theClientDto.getBenutzername(),
					theClientDto.getKennwortAsString(), theClientDto.getMandant(), theClientDto.getSMandantenwaehrung(),
					theClientDto.getIDPersonal(), theClientDto.getLocUiAsString(), theClientDto.getLocMandantAsString(),
					theClientDto.getLocKonzernAsString(), theClientDto.getDLoggedin(),
					theClientDto.getSystemrolleIId());
			em.persist(theClient);
//			em.flush(); // -> Aufgrund SP7118 auskommentiert

//			System.out.print(theClientDto);

			setTheClientFromTheClientDto(theClient, theClientDto);
			
			if(theClientDto.getHvmaResource() == null) {
				updateUserCount(theClientDto.getSystemrolleIId());
			} else {
				updateHvmaUserCount(
						theClientDto.getSystemrolleIId(),
						theClientDto.getHvmaLizenzId(),
						theClientDto.getHvmaResource());
			}
		}

		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (PersistenceException ex) {
			myLogger.error("PERSISTENCE",ex);
			ex.printStackTrace();
			throwEJBExceptionLPforPersistence(ex);
		} catch (Throwable t) {
			myLogger.error("ENTITY_EXISTS",t);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, new Exception(t));
		} finally {
			clientLock.unlock();
			myLogger.info("Client-Lockcount (unlock): " + clientLock.getHoldCount() + ".");
		}
	}

	public void removeTheClientTVonTBis0(java.sql.Timestamp tVon, java.sql.Timestamp tBis) throws EJBExceptionLP {
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
				Theclient theClient = em.find(Theclient.class, dtos[i].getIDUser());
				if (theClient == null) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, dtos[i].getIDUser());
				}
				
				if(theClient.getHvmalizenIId() == null) {
					Integer systemrolleIId = theClient.getSystemrolleIId();
					em.remove(theClient);

					updateUserCount(systemrolleIId);
				}
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}
	}

	public void removeTheClientTVonTBis(
			java.sql.Timestamp tVon, java.sql.Timestamp tBis) throws EJBExceptionLP {
		try {
			Query query = em.createNamedQuery("TheClientfindByTLoggedIn");
			query.setParameter(1, tVon);
			query.setParameter(2, tBis);
			Collection<Theclient> cl = query.getResultList();

			for (Theclient entry : cl) {
				// Zwischen query und remove koennte ein anderer Thread schon geloescht haben
				Theclient theClient = em.find(Theclient.class, entry.getCNr());
				if (theClient == null) {
					// TODO: wieso? Wenn der Satz weg ist, dann ist er weg? (ghp, 24.4.2019)
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, entry.getCNr());
				}

				if(theClient.getHvmalizenIId() == null) {
					Integer systemrolleIId = theClient.getSystemrolleIId();
					em.remove(theClient);

					updateUserCount(systemrolleIId);
				}
			}
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}
	}

	public void updateTheClient(TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		if (theClientDto != null) {
			myLogger.info("Client-Update-Lockcount (lock): " + clientLock.getHoldCount() + ".");
			clientLock.lock();

			try {
				String cNr = theClientDto.getIDUser();
				Theclient theClient = em.find(Theclient.class, cNr);
				if (theClient == null) {
					myLogger.warn("Client mit ID " +cNr + " nicht gefunden!" );
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_C_NR_USER_IS_NULL, "");
				}
				setTheClientFromTheClientDto(theClient, theClientDto);
				if(theClientDto.getHvmaLizenzId() == null) {
					updateUserCount(theClientDto.getSystemrolleIId());
				} else {
					updateHvmaUserCount(
							theClientDto.getSystemrolleIId(),
							theClientDto.getHvmaLizenzId(),
							theClientDto.getHvmaResource());
				}				
			} finally {
				clientLock.unlock();
				myLogger.info("Client-Update-Lockcount (unlock): " + clientLock.getHoldCount() + ".");				
			}
		}
	}

	private void updateHvmaUserCount(Integer systemrolleId, 
			Integer hvmaLizenzId, String resource) {
		PKGeneratorObj pkGen = new PKGeneratorObj();
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMAUSERCOUNT);

		LpHvmaUserCount userCount = new LpHvmaUserCount();
		userCount.setIId(pk);
		userCount.setIAnzahl(getUserCountLizenz(hvmaLizenzId));
		userCount.setHvmaLizenzIId(hvmaLizenzId);
		userCount.setSystemrolleIId(systemrolleId);
		userCount.setHvmaResource(resource);
		userCount.setTZeitpunkt(getTimestamp());
		em.persist(userCount);
		em.flush();
		
		myLogger.warn("UserCountHvma: " + userCount.getIAnzahl() + 
				" LizenzId " + userCount.getHvmaLizenzIId() + ".");
	}
	
	private void updateUserCount(Integer systemrolleIId) throws EJBExceptionLP, RemoteException {
		/*
		 * Beim Uebergang vom alten auf das neue Benutzerzaehlersystem kann es
		 * vorkommen, dass keine systemrolle gesetzt ist. Deshalb wird in einem solchen
		 * Fall kein Benutzerzaehler abgeaendert.
		 */
		if (systemrolleIId == null)
			return;

		Integer maxUsers = getBenutzerFac().systemrolleFindByPrimaryKey(systemrolleIId).getIMaxUsers();

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_USERCOUNT);

		LpUserCount userCount = new LpUserCount();
		Integer anzahl = (maxUsers == null ? getUserCountMandant() : getUserCountRolle(systemrolleIId));
		userCount.setIId(pk);
		userCount.setIAnzahl(anzahl);
		userCount.setSystemrolleIId(systemrolleIId);
		userCount.setTZeitpunkt(getTimestamp());
		em.persist(userCount);
		em.flush();
		
		myLogger.info("UserCount: " + anzahl + 
				((maxUsers == null) ? " Mandant." : (" Systemrolle " + systemrolleIId + ".")));
	}

	private Integer getCountResult(Criteria c) {
		Object count = c.uniqueResult();
		return count instanceof Long ? ((Long)count).intValue() : (Integer)count;
	}
	
	/**
	 * Z&auml;hlt die angemeldeten Benutzer in einer Systemrolle am heutigen Tag ab
	 * 00:00 Uhr. Ob diese Rolle auch einen eigenen UserCounter hat, wird hier nicht
	 * gepr&uuml;ft.
	 * 
	 * @param systemrolleIId
	 * @return die Anzahl der angemeldeten Benutzer
	 */
	private Integer getUserCountRolle(Integer systemrolleIId) {
		Query q = em.createNamedQuery("TheClientCountRolleLoggedIn")
				.setParameter("rolle", systemrolleIId)
				.setParameter("today", Helper.cutTimestamp(getTimestamp()));
		Object o = q.getSingleResult();
		return ((Long)o).intValue();
		
//		Session session = getNewSession();
//		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
//		Criteria c = session.createCriteria(FLRTheClient.class);
//		c.createCriteria("flrsystemrolle")
//			.add(Restrictions.eq("i_id", systemrolleIId));
//		c.add(Restrictions.isNull("t_loggedout"))
//			.add(Restrictions.ge("t_loggedin", today))
//			.add(Restrictions.isNull("hvmalizenz_IId"))
//			.add(Restrictions.not(Restrictions.like("c_benutzername", "lpwebappzemecs|%")))
//			.setProjection(Projections.countDistinct("c_benutzername"));
//		return getCountResult(c);
	}

	/**
	 * Z&auml;hlt die angemeldeten Benutzer am heutigen Tag ab 00:00 Uhr, welche
	 * nicht eine Systemrolle mit eigenem UserCounter haben.
	 * 
	 * @return die Anzahl der angemeldeten Benutzer.
	 */
	private Integer getUserCountMandant() {
		Integer emCount = 0;
//		{
//			Query q = em.createNamedQuery("TheClientfindLoggedIn");
//			List<Theclient> clients = q.getResultList();
//			emCount = clients.size();
//		}

		
//		Session session = getNewSession();
//		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
//		Criteria c = session.createCriteria(FLRTheClient.class);
//		c.createCriteria("flrsystemrolle")
//			.add(Restrictions.isNull("i_max_users"));
//		c.add(Restrictions.isNull("t_loggedout"))
//			.add(Restrictions.ge("t_loggedin", today))
//			.add(Restrictions.isNull("hvmalizenz_IId"))
//			.setProjection(Projections.countDistinct("c_benutzername"));
//		Integer count = getCountResult(c);

		{
			Query q = em.createNamedQuery("TheClientCountMandantLoggedIn")
					.setParameter("today", Helper.cutTimestamp(getTimestamp()));
			Object o = q.getSingleResult();
			emCount = ((Long)o).intValue();
		}
		
//		Query q = em.createNamedQuery("TheClientfindLoggedIn");
//		List<Theclient> clients = q.getResultList();
//		if((clients.size() != count) || (emCount != count)) {
//			System.out.println("<>");
//		}
		
		return emCount;
/*
		Session session = getNewSession();
		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
		Criteria c = session.createCriteria(FLRTheClient.class)
				.createCriteria("flrsystemrolle");
		c.add(Restrictions.isNull("i_max_users"))
				.add(Restrictions.ge("t_loggedin", today))
				.add(Restrictions.isNull("t_loggedout"))
				.add(Restrictions.isNull("hvmalizenz_IId"))
				.setProjection(Projections.countDistinct("c_benutzername"));
*/
	}

	@Override
	public List<TheClientLoggedInDto> getLoggedInClients() {
		HvTypedQuery<Theclient> q = new HvTypedQuery<Theclient>(
				em.createNamedQuery("TheClientsMandantLoggedIn")
					.setParameter("today", Helper.cutTimestamp(getTimestamp())));
		List<Theclient> emClients = q.getResultList();
		List<TheClientLoggedInDto> clients = new ArrayList<TheClientLoggedInDto>();
		String lastUser = "";
		int userCount = 0;
		for (Theclient emClient : emClients) {
			TheClientDto dto = theClientFindByPrimaryKey(emClient.getCNr());
			if(!lastUser.equals(dto.getBenutzername())) {
				++userCount;
				lastUser = dto.getBenutzername();
			}
			TheClientLoggedInDto inDto = dto.asLoggedIn();
			inDto.setConcurrentUserCount(userCount);
			clients.add(inDto);
		}
		return clients;
		
//		Session session = getNewSession();
//		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
//		Criteria c = session.createCriteria(FLRTheClient.class);
//		c.createCriteria("flrsystemrolle")
//			.add(Restrictions.isNull("i_max_users"));
//		c.add(Restrictions.isNull("t_loggedout"))
//			.add(Restrictions.ge("t_loggedin", today))
//			.add(Restrictions.isNull("hvmalizenz_IId"));
//		List<FLRTheClient> flrClients = c.list();
//		List<TheClientLoggedInDto> clients = new ArrayList<TheClientLoggedInDto>();
//		for (FLRTheClient flrTheClient : flrClients) {
//			TheClientDto dto = theClientFindByPrimaryKey(flrTheClient.getCnr());
//			clients.add(dto.asLoggedIn());
//		}
//		return clients;
	}
/*	
	public Integer getUserCountMandantSQL() {

		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
		Query q = em.createNativeQuery(
				"select COUNT(DISTINCT L.C_BENUTZERNAME) FROM LP_THECLIENT L inner join PERS_SYSTEMROLLE S on L.SYSTEMROLLE_I_ID=S.I_ID  where S.I_MAX_USERS is null and L.T_LOGGEDOUT is null and L.T_LOGGEDIN>='"
						+ Helper.formatTimestampWithSlashes(today) + "'");

		List c = q.getResultList();

		if (c.get(0) instanceof Long) {
			return ((Long) c.get(0)).intValue();
		} else {
			return (Integer) c.get(0);
		}
	}
*/

	private Integer getUserCountLizenz(Integer hvmaLizenzId) {
		Integer emCount = 0;
		{
			java.sql.Timestamp qToday = Helper.cutTimestamp(getTimestamp());
			Query q = em.createNamedQuery("TheClientCountHvmaLoggedIn")
					.setParameter("today", qToday)
					.setParameter("licenceId", hvmaLizenzId);
			Object o = q.getSingleResult();
			emCount = ((Long)o).intValue();
		}
	
		Session session = getNewSession();
		java.sql.Timestamp today = Helper.cutTimestamp(getTimestamp());
		Criteria c = session.createCriteria(FLRTheClient.class)
				.add(Restrictions.eq("hvmalizenz_IId", hvmaLizenzId))
				.add(Restrictions.ge("t_loggedin", today))
				.add(Restrictions.isNull("t_loggedout"))
				.setProjection(Projections.countDistinct("c_benutzername"));
		Integer count = getCountResult(c);

		return emCount;
	}
	
	public List<VerfuegbareHostsDto> getVerfuegbareHosts(String benutzerCNr, String mandantCNr,
			TheClientDto theClientDto) {

		return getVerfuegbareHosts(benutzerCNr, mandantCNr, false, theClientDto);
	}

	public VerfuegbareHostsDto getVerfuegbarenHost(TheClientDto theClientDto) {
		List<VerfuegbareHostsDto> hosts = getVerfuegbareHosts(theClientDto.getBenutzername(), theClientDto.getMandant(),
				true, theClientDto);

		if (hosts == null || hosts.isEmpty())
			return null;

		if (hosts.size() == 1)
			return hosts.get(0);

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NO_UNIQUE_RESULT, "Fuer Client \""
				+ theClientDto.getBenutzername() + "\" existieren mehrere Eintraege mit gleichem Token");
	}

	public List<VerfuegbareHostsDto> getVerfuegbareHosts(Integer personalIId, TheClientDto theClientDto) {

		ArrayList<VerfuegbareHostsDto> al = new ArrayList<VerfuegbareHostsDto>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT tc FROM FLRTheClient tc WHERE tc.i_personal=" + personalIId
				+ " AND tc.t_loggedout IS NULL AND tc.i_rmiport IS NOT NULL ";
		sQuery += " ORDER BY tc.t_loggedin DESC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		HashSet hsBereitsVorhanden = new HashSet();

		while (resultListIterator.hasNext()) {
			FLRTheClient wr = (FLRTheClient) resultListIterator.next();

			StringTokenizer st = new StringTokenizer(wr.getC_benutzername(), LogonFac.USERNAMEDELIMITER);
			String user = st.nextToken();
			String hostname = st.nextToken();

			VerfuegbareHostsDto host = new VerfuegbareHostsDto();
			host.setHostname(hostname);
			host.setMandantCNr(wr.getC_mandant());
			host.setPort(wr.getI_rmiport());

			String vergl = wr.getC_benutzername() + wr.getC_mandant();

			// Derzeit nur den letzen, wg. Timeout
			if (!hsBereitsVorhanden.contains(vergl)) {
				hsBereitsVorhanden.add(vergl);
				al.add(host);
			}

		}

		return al;
	}

	private List<VerfuegbareHostsDto> getVerfuegbareHosts(String benutzerCNr, String mandantCNr, boolean addToken,
			TheClientDto theClientDto) {

		ArrayList<VerfuegbareHostsDto> al = new ArrayList<VerfuegbareHostsDto>();

		StringTokenizer st = new StringTokenizer(theClientDto.getBenutzername(), LogonFac.USERNAMEDELIMITER);
		String user = st.nextToken();
		String hostname = st.nextToken();

		Session session = FLRSessionFactory.getFactory().openSession();

		String sQuery = "SELECT tc FROM FLRTheClient tc WHERE tc.c_benutzername LIKE '%|" + hostname
				+ "|%' AND tc.c_benutzername LIKE '" + user
				+ "|%' AND tc.t_loggedout IS NULL AND tc.i_rmiport IS NOT NULL AND tc.c_mandant='" + mandantCNr + "'";
		if (addToken) {
			sQuery += " AND tc.cnr = '" + theClientDto.getIDUser() + "'";
		}
		sQuery += " ORDER BY tc.t_loggedin DESC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRTheClient wr = (FLRTheClient) resultListIterator.next();

			VerfuegbareHostsDto host = new VerfuegbareHostsDto();
			host.setHostname(hostname);
			host.setMandantCNr(mandantCNr);
			host.setPort(wr.getI_rmiport());

			al.add(host);

		}

		return al;
	}

	@Override
	public List<VerfuegbareHostsDto> getVerfuegbareBenutzer(String host, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT tc FROM FLRTheClient tc " 
				+ "WHERE tc.c_benutzername LIKE '%|" + host
				+ "|%' AND tc.t_loggedout IS NULL AND tc.i_rmiport IS NOT NULL "
				+ "ORDER BY tc.t_loggedin DESC";

		org.hibernate.Query query = session.createQuery(sQuery);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		List<VerfuegbareHostsDto> entries = new ArrayList<VerfuegbareHostsDto>();
		Set<Integer> knownPorts = new HashSet<Integer>();
		while (resultListIterator.hasNext()) {
			FLRTheClient wr = (FLRTheClient) resultListIterator.next();

			if(knownPorts.contains(wr.getI_rmiport())) {
				// Diese Abfrage erfordert, dass t_loggedin *DESC* verwendet wird
				continue;
			}
			
			StringTokenizer st = new StringTokenizer(wr.getC_benutzername(), LogonFac.USERNAMEDELIMITER);
			String user = st.nextToken();
			String hostname = st.nextToken();

			VerfuegbareHostsDto entry = new VerfuegbareHostsDto();
			entry.setHostname(hostname);
			entry.setMandantCNr(wr.getC_mandant());
			entry.setPort(wr.getI_rmiport());
			entry.setBenutzerCNr(user);

			entries.add(entry);
			knownPorts.add(wr.getI_rmiport());
			
			if(entries.size() > 15) break;
		}

		return entries;
	}

	public void setRmiPort(TheClientDto theClientDto, Integer rmiPort) {
		String cNr = theClientDto.getIDUser();
		Theclient theClient = em.find(Theclient.class, cNr);
		theClient.setIRmiport(rmiPort);
		em.merge(theClient);
		em.flush();
	}

	@Override
	public Integer getRmiPort(TheClientDto theClientDto) {
		String cnr = theClientDto.getIDUser();
		Theclient theClient = em.find(Theclient.class, cnr);
		return theClient.getIRmiport();
	}
	
	public void updateTheClients(TheClientDto[] theClientDtos) throws EJBExceptionLP, RemoteException {
		if (theClientDtos != null) {
			for (int i = 0; i < theClientDtos.length; i++) {
				updateTheClient(theClientDtos[i]);
			}
		}
	}

	public TheClientDto theClientFindByPrimaryKey(String cNrUserI) throws EJBExceptionLP {
		TheClientDto theClient = theClientFindByPrimaryKeyOhneExc(cNrUserI);
		Validator.entityFoundCnr(theClient, cNrUserI);
//		if (theClient == null)
//			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		return theClient;
	}

	public TheClientDto theClientFindByPrimaryKeyOhneExc(String cNrUserI) throws EJBExceptionLP {
		Theclient client = em.find(Theclient.class, cNrUserI);
		return client == null ? null : loadClientDto(client, true);
	}

	private TheClientDto loadClientDto(Theclient theClient, boolean desktopBenutzer) {
		TheClientDto theClientDto = assembleTheClientDto(theClient);
		theClientDto.setDesktopBenutzer(desktopBenutzer);
		theClientDto.setSystemrolleIId(getTheJudgeFac().getSystemrolleIId(theClientDto));
		return theClientDto;
	}

	/**
	 * Pruefe, ob ein User noch eingeloggt ist
	 * 
	 * @param token String 
	 * @throws EJBExceptionLP wenn er nicht mehr eingeloggt ist
	 * @return TheClientDto
	 */
	public TheClientDto theClientFindByUserLoggedIn(String token) throws EJBExceptionLP {
		return theClientFindByUserLoggedInImpl(token, true);
	}

	@Override
	public TheClientDto theClientFindByUserLoggedInMobil(String token) {
		return theClientFindByUserLoggedInImpl(token, false);
	}

	private TheClientDto theClientFindByUserLoggedInImpl(String cnr, boolean desktopBenutzer) {
		Theclient theclient;
		try {
			Query query = em.createNamedQuery("TheClientfindByUserLoggedIn");
			query.setParameter(1, cnr);
			query.setParameter(2, getTimestamp());
			theclient = (Theclient) query.getSingleResult();
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_THECLIENT_WURDE_GELOESCHT, e);
		}

		TheClientDto clientDto = loadClientDto(theclient, desktopBenutzer);
		try {
			validateHvmaLizenz(clientDto);
		} catch(RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// TODO: Wieder reaktivieren, bricht derzeit tests
		// getLogonFac().validateLoggedIn(clientDto);
		return clientDto;		
	}
	
	private void validateHvmaLizenz(TheClientDto theClientDto) throws RemoteException {
		if(theClientDto == null) return;
		if(theClientDto.getHvmaLizenzId() == null) return;
	
		StringTokenizer st = new StringTokenizer(theClientDto.getBenutzername(),
				LogonFac.USERNAMEDELIMITER);
		String user = st.nextToken();
		BenutzerDto benutzerDto = getBenutzerFac()
				.benutzerFindByCBenutzerkennung(user, 
						theClientDto.getKennwortAsString());
		try {
			Hvmabenutzer hvmabenutzer = HvmabenutzerQuery.findBenutzerIdLizenzId(em, 
				benutzerDto.getIId(), theClientDto.getHvmaLizenzId());
		} catch(NoResultException e) {
			myLogger.warn("Token '" + theClientDto.getIDUser() 
				+ "' passt nicht mehr zu Benutzer '" + user + "' und Lizenz Id '" 
				+ theClientDto.getHvmaLizenzId() + "'.");
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_THECLIENT_WURDE_GELOESCHT, e);			
		}
	}

	public TheClientDto theClientFindByCBenutzernameLoggedIn(String cBenutzername) throws EJBExceptionLP {
		Theclient theclient = null;
		try {
			Query query = em.createNamedQuery("TheClientfindByCBenutzernameLoggedIn");
			query.setParameter(1, cBenutzername);
			query.setParameter(2, Helper.cutTimestamp(getTimestamp()));
			query.setMaxResults(1);
			theclient = (Theclient) query.getSingleResult();
		} catch (NoResultException e) {
			//
		} catch (Exception ex) {
			throw new EJBExceptionLP(ex);
		}

		return theclient == null ? null : loadClientDto(theclient, true);
	}

	private void setTheClientFromTheClientDto(Theclient theclient, TheClientDto theClientDto) throws EJBExceptionLP {
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
		theclient.setHvmalizenzIId(theClientDto.getHvmaLizenzId());
		theclient.setHvmaResource(theClientDto.getHvmaResource());
		em.merge(theclient);
//		em.flush(); // -> Aufgrund SP7118 auskommentiert
	}

	private TheClientDto assembleTheClientDto(Theclient theClient) throws EJBExceptionLP {
		return TheClientDtoAssembler.createDto(theClient);
	}

	private TheClientDto[] assembleTheClientDtos(Collection<?> theClients) throws Exception {
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

		Installer installer = em.find(Installer.class, TheClientFac.PK_INSTALLER);
		if (installer == null) {
			try {
				installer = new Installer(installerDto.getIId(), installerDto.getOClientpc(),
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
		Installer toRemove = em.find(Installer.class, TheClientFac.PK_INSTALLER);
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

	public boolean istNeuerClientVerfuegbar(Integer iBuildnummerclient) {
		Session session = getNewSession();

		org.hibernate.Criteria crit = session.createCriteria(FLRInstaller.class);

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
		Installer installer = em.find(Installer.class, TheClientFac.PK_INSTALLER);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
	}

	public InstallerDto getInstallerWithoutClientFile() throws EJBExceptionLP {
		Installer installer = em.find(Installer.class, TheClientFac.PK_INSTALLER);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
	}

	public InstallerDto getInstallerPart(Integer iPart) throws EJBExceptionLP {
		Installer installer = em.find(Installer.class, iPart);
		if (installer == null) {
			return null;
		}
		return assembleInstallerDto(installer);
	}

	private void setInstallerFromInstallerDto(Installer installer, InstallerDto installerDto) {
		installer.setOClientpc(installerDto.getOClientpc());
		installer.setIBuildnummerclientpc(installerDto.getIBuildnummerclientpc());
		em.merge(installer);
		em.flush();
	}

	private InstallerDto assembleInstallerDto(Installer installer) {
		return InstallerDtoAssembler.createDto(installer);
	}

	private InstallerDto assembleInstallerDtoWithoutClientFile(Installer installer) {
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
		int removedSessions = 0;

		myLogger.warn("Beginne alle noch angemeldeten Benutzer abzumelden");
		try {
			HvTypedQuery<Theclient> currentUsers = new HvTypedQuery<Theclient>(
					em.createNamedQuery("TheClientfindLoggedIn"));
			List<Theclient> users = currentUsers.getResultList();

			myLogger.warn("Es gibt " + users.size() + " angemeldete Benutzer");

			Timestamp logoutTimestamp = getTimestamp();
			for (Theclient theclient : users) {
				Integer rolleId = theclient.getSystemrolleIId();
				if (remove) {
					em.remove(theclient);
				} else {
					theclient.setTLoggedout(logoutTimestamp);
					em.merge(theclient);
				}
				updateUserCount(rolleId);
				++removedSessions;
			}

			myLogger.warn("Ende alle noch angemeldeten Benutzer abzumelden");

			return removedSessions;
		} catch (Exception e) {
			throw new EJBExceptionLP(e);
		}
	}
	
	@Override
	public Integer getZaehlerAngemeldeteBenutzer() {
		return getUserCountMandant();
	}
	
	@Override
	public Integer getZaehlerAngemeldeteBenutzerRolle(Integer systemrolleId) {
		return getUserCountRolle(systemrolleId);
	}
	
	@Override
	public Integer getZaehlerAngemeldeteHvmaBenutzer(Integer hvmaLizenzId) {
		Validator.notNull(hvmaLizenzId, "hvmaLizenzId");
		return getUserCountLizenz(hvmaLizenzId);
	}
}
