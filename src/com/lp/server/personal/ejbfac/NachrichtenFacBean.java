package com.lp.server.personal.ejbfac;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.fertigung.service.LosDto;
import com.lp.server.personal.ejb.Nachrichten;
import com.lp.server.personal.ejb.Nachrichtenabo;
import com.lp.server.personal.ejb.Nachrichtenart;
import com.lp.server.personal.ejb.Nachrichtenartspr;
import com.lp.server.personal.ejb.NachrichtenartsprPK;
import com.lp.server.personal.ejb.Nachrichtenempfaenger;
import com.lp.server.personal.ejb.Nachrichtengruppe;
import com.lp.server.personal.ejb.Nachrichtengruppeteilnehmer;
import com.lp.server.personal.fastlanereader.generated.FLRNachrichtenabo;
import com.lp.server.personal.fastlanereader.generated.FLRNachrichtenempfaenger;
import com.lp.server.personal.service.NachrichtenDto;
import com.lp.server.personal.service.NachrichtenFac;
import com.lp.server.personal.service.NachrichtenaboDto;
import com.lp.server.personal.service.NachrichtenaboDtoAssembler;
import com.lp.server.personal.service.NachrichtenartDto;
import com.lp.server.personal.service.NachrichtenartDtoAssembler;
import com.lp.server.personal.service.NachrichtenartsprDto;
import com.lp.server.personal.service.NachrichtenartsprDtoAssembler;
import com.lp.server.personal.service.NachrichtenempfaengerDto;
import com.lp.server.personal.service.NachrichtenempfaengerDtoAssembler;
import com.lp.server.personal.service.NachrichtengruppeDto;
import com.lp.server.personal.service.NachrichtengruppeDtoAssembler;
import com.lp.server.personal.service.NachrichtengruppeteilnehmerDto;
import com.lp.server.personal.service.NachrichtengruppeteilnehmerDtoAssembler;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.system.jms.service.LPInfoTopicBean;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ClientRemoteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class NachrichtenFacBean extends Facade implements NachrichtenFac {
	@PersistenceContext
	private EntityManager em;

	public NachrichtenartDto nachrichtenartFindByPrimaryKey(Integer iId, TheClientDto theClientDto) {

		NachrichtenartDto nachrichtenartDto = null;
		Nachrichtenart nachrichtenart = em.find(Nachrichtenart.class, iId);

		nachrichtenartDto = NachrichtenartDtoAssembler.createDto(nachrichtenart);
		// jetzt die Spr
		NachrichtenartsprDto nachrichtenartsprDto = null;

		try {
			NachrichtenartsprPK nachrichtenartsprPK = new NachrichtenartsprPK(theClientDto.getLocUiAsString(),
					nachrichtenartDto.getIId());
			Nachrichtenartspr nachrichtenartspr = em.find(Nachrichtenartspr.class, nachrichtenartsprPK);
			if (nachrichtenartspr != null) {
				nachrichtenartsprDto = NachrichtenartsprDtoAssembler.createDto(nachrichtenartspr);
			}
		} catch (Throwable t) {
			// ignore
		}
		nachrichtenartDto.setNachrichtenartsprDto(nachrichtenartsprDto);
		return nachrichtenartDto;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void nachrichtAnEinzelpersonErstellen(String cBetreff, String xText, Integer personalIId,
			TheClientDto theClientDto) {

		getNachrichtenFac().nachrichtErstellen(Art.EINZELNACHRICHT, cBetreff, xText, personalIId, null, null, null,
				theClientDto);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void empfangerBenachrichtigen(TheClientDto theClientDto, Object[] oReturnData) {
		// Empfaenger

		if (oReturnData != null && oReturnData.length > 1) {
			Iterator itEmpfaenger = ((HashSet) oReturnData[0]).iterator();
			while (itEmpfaenger.hasNext()) {
				Integer personalIIdEmpfaenger = (Integer) itEmpfaenger.next();

				getNachrichtenFac().benachrichtigeClientsDasNeueNachrichtenVorhandenSind(personalIIdEmpfaenger,
						theClientDto);
			}
		}

		// Per JMS aktualisieren
		HelperServer.sendJmsInfo(getInitialContext(), LPInfoTopicBean.NACHRICHTEN_AKTUALISIEREN);

	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void nachrichtErstellen(String nachrichtenartCNr, String cBetreff, String xText, String belegartCNr,
			Integer belegartIId, TheClientDto theClientDto) {
		getNachrichtenFac().nachrichtErstellen(nachrichtenartCNr, cBetreff, xText, null, null, belegartCNr, belegartIId,
				theClientDto);

	}

	private boolean hatPersonNachrichtenartAbboniert(String nachrichtenartCNr, Integer personalIId,
			TheClientDto theClientDto) {

		HashSet hsPersonalIIdEmpfaenger = new HashSet();

		Session session = getNewSession();

		String queryString = "SELECT a FROM FLRNachrichtenabo a WHERE a.flrnachrichtenart.c_nr='" + nachrichtenartCNr
				+ "'";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRNachrichtenabo abo = (FLRNachrichtenabo) resultListIterator.next();

			if (abo.getPersonal_i_id() != null && abo.getPersonal_i_id().equals(personalIId)) {
				return true;
			}
			if (abo.getNachrichtengruppe_i_id() != null) {

				Query queryTeilnehmer = em.createNamedQuery("NachrichtengruppeteilnehmerfindByNachrichtengruppeIId");

				queryTeilnehmer.setParameter(1, abo.getNachrichtengruppe_i_id());
				Iterator it = queryTeilnehmer.getResultList().iterator();
				while (it.hasNext()) {
					Nachrichtengruppeteilnehmer ngt = (Nachrichtengruppeteilnehmer) it.next();

					if (ngt.getPersonalIId() != null && ngt.getPersonalIId().equals(personalIId)) {
						return true;
					}
				}

			}

		}

		return false;

	}

	public void nachrichtErstellen(String nachrichtenartCNr, String cBetreff, String xText,
			Integer personalIIdNurEinzelnePerson, Integer personalIIdAusgenommen, String belegartCNr,
			Integer belegartIId, TheClientDto theClientDto) {

		HashSet hsPersonalIIdEmpfaenger = new HashSet();

		if (personalIIdNurEinzelnePerson == null) {

			Session session = getNewSession();

			String queryString = "SELECT a FROM FLRNachrichtenabo a WHERE a.flrnachrichtenart.c_nr='"
					+ nachrichtenartCNr + "'";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();

			while (resultListIterator.hasNext()) {
				FLRNachrichtenabo abo = (FLRNachrichtenabo) resultListIterator.next();

				if (abo.getPersonal_i_id() != null) {
					hsPersonalIIdEmpfaenger.add(abo.getPersonal_i_id());
				}
				if (abo.getNachrichtengruppe_i_id() != null) {

					Query queryTeilnehmer = em
							.createNamedQuery("NachrichtengruppeteilnehmerfindByNachrichtengruppeIId");

					queryTeilnehmer.setParameter(1, abo.getNachrichtengruppe_i_id());
					Iterator it = queryTeilnehmer.getResultList().iterator();
					while (it.hasNext()) {
						Nachrichtengruppeteilnehmer ngt = (Nachrichtengruppeteilnehmer) it.next();
						hsPersonalIIdEmpfaenger.add(ngt.getPersonalIId());
					}

				}

			}
		} else {
			hsPersonalIIdEmpfaenger.add(personalIIdNurEinzelnePerson);
		}

		if (personalIIdAusgenommen != null && hsPersonalIIdEmpfaenger.contains(personalIIdAusgenommen)) {
			hsPersonalIIdEmpfaenger.remove(personalIIdAusgenommen);
		}

		if (hsPersonalIIdEmpfaenger.size() > 0) {
			Query queryART = em.createNamedQuery("NachrichtartenfindByCNr");

			queryART.setParameter(1, nachrichtenartCNr);
			// @todo getSingleResult oder getResultList ?
			Integer nachrichtenartIId = ((Nachrichtenart) queryART.getSingleResult()).getIId();

			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTEN);

			Nachrichten nachrichten = new Nachrichten(pk, nachrichtenartIId, new Timestamp(System.currentTimeMillis()),
					theClientDto.getIDPersonal());
			nachrichten.setCBetreff(cBetreff);
			nachrichten.setXText(xText);
			nachrichten.setCBelegartnr(belegartCNr);
			nachrichten.setBelegIId(belegartIId);
			em.persist(nachrichten);
			em.flush();

			Iterator it = hsPersonalIIdEmpfaenger.iterator();
			while (it.hasNext()) {
				Integer personalIIdEmpfaenger = (Integer) it.next();

				PKGeneratorObj pkGen2 = new PKGeneratorObj(); // PKGEN
				Integer pk2 = pkGen2.getNextPrimaryKey(PKConst.PK_NACHRICHTENEMPFAENGER);

				Nachrichtenempfaenger nachrichtenempfaenger = new Nachrichtenempfaenger(pk2, pk, personalIIdEmpfaenger);
				em.persist(nachrichtenempfaenger);
				em.flush();
			}

			// getNachrichtenFac().empfangerBenachrichtigen(theClientDto, new Object[] {
			// hsPersonalIIdEmpfaenger, pk });

		}

	}

	public void updateNachrichtenart(NachrichtenartDto nachrichtenartDto, TheClientDto theClientDto) {

		try {
			Nachrichtenart nachrichtenart = em.find(Nachrichtenart.class, nachrichtenartDto.getIId());

			setNachrichtenartFromNachrichtenartDto(nachrichtenart, nachrichtenartDto);

			// jetzt die Spr
			NachrichtenartsprDto nachrichtenartsprDto = nachrichtenartDto.getNachrichtenartsprDto();

			if (nachrichtenartsprDto != null && nachrichtenartsprDto.getNachrichtenartIId() != null) {
				NachrichtenartsprPK nachrichtenartsprPK = new NachrichtenartsprPK();
				nachrichtenartsprPK.setLocaleCNr(nachrichtenartsprDto.getLocaleCNr());
				nachrichtenartsprPK.setNachrichtenartIId(nachrichtenartsprDto.getNachrichtenartIId());

				Nachrichtenartspr nachrichtenartspr = em.find(Nachrichtenartspr.class, nachrichtenartsprPK);

				setNachrichtenartsprFromNachrichtenartsprDto(nachrichtenartspr, nachrichtenartsprDto);
			} else {
				Nachrichtenartspr nachrichtenartspr = new Nachrichtenartspr(theClientDto.getLocUiAsString(),
						nachrichtenartDto.getIId(), nachrichtenartDto.getNachrichtenartsprDto().getCBez());
				em.persist(nachrichtenartspr);
				em.flush();

				setNachrichtenartsprFromNachrichtenartsprDto(nachrichtenartspr,
						nachrichtenartDto.getNachrichtenartsprDto());
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	private void setNachrichtenartsprFromNachrichtenartsprDto(Nachrichtenartspr nachrichtenartspr,
			NachrichtenartsprDto nachrichtenartsprDto) {
		nachrichtenartspr.setCBez(nachrichtenartsprDto.getCBez());
		em.merge(nachrichtenartspr);
		em.flush();
	}

	private void setNachrichtenartFromNachrichtenartDto(Nachrichtenart nachrichtenart,
			NachrichtenartDto nachrichtenartDto) {
		nachrichtenart.setBMussErledigtWerden(nachrichtenartDto.getBMussErledigtWerden());
		nachrichtenart.setPersonalIIdEskalation(nachrichtenartDto.getPersonalIIdEskalation());
		nachrichtenart.setPersonalIIdEskalation(nachrichtenartDto.getPersonalIIdEskalation());
		em.merge(nachrichtenart);
		em.flush();
	}

	public NachrichtengruppeDto nachrichtengruppeFindByPrimaryKey(Integer iId) {
		Nachrichtengruppe bean = em.find(Nachrichtengruppe.class, iId);
		return NachrichtengruppeDtoAssembler.createDto(bean);
	}

	public NachrichtenaboDto nachrichtenaboFindByPrimaryKey(Integer iId) {
		Nachrichtenabo bean = em.find(Nachrichtenabo.class, iId);
		return NachrichtenaboDtoAssembler.createDto(bean);
	}

	public NachrichtenDto nachrichtenFindByPrimaryKey(Integer iId) {
		Nachrichten bean = em.find(Nachrichten.class, iId);
		NachrichtenDto nachrichtenDto = new NachrichtenDto();

		nachrichtenDto.setBelegIId(bean.getBelegIId());
		nachrichtenDto.setBelegpositionIId(bean.getBelegpositionIId());
		nachrichtenDto.setCBelegartnr(bean.getCBelegartnr());
		nachrichtenDto.setCBetreff(bean.getCBetreff());
		nachrichtenDto.setIId(bean.getIId());
		nachrichtenDto.setNachrichtenartIId(bean.getNachrichtenartIId());
		nachrichtenDto.setPersonalIIdAbsender(bean.getPersonalIIdAbsender());
		nachrichtenDto.setPersonalIIdErledigt(bean.getPersonalIIdErledigt());
		nachrichtenDto.setTAnlegen(bean.gettAnlegen());
		nachrichtenDto.setTErledigt(bean.getTErledigt());
		nachrichtenDto.setXText(bean.getXText());

		return nachrichtenDto;
	}

	public NachrichtenempfaengerDto nachrichtenempfaengerFindByPrimaryKey(Integer iId) {
		Nachrichtenempfaenger bean = em.find(Nachrichtenempfaenger.class, iId);
		return NachrichtenempfaengerDtoAssembler.createDto(bean);
	}

	public Timestamp getAnlageDatumDerNachricht(Integer nachrichtenempfaengeriId) {
		Nachrichtenempfaenger bean = em.find(Nachrichtenempfaenger.class, nachrichtenempfaengeriId);
		Nachrichten nachrichten = em.find(Nachrichten.class, bean.getNachrichtenIId());

		return nachrichten.gettAnlegen();
	}

	public NachrichtengruppeteilnehmerDto nachrichtengrupperteilnehmerFindByPrimaryKey(Integer iId) {
		Nachrichtengruppeteilnehmer bean = em.find(Nachrichtengruppeteilnehmer.class, iId);
		return NachrichtengruppeteilnehmerDtoAssembler.createDto(bean);
	}

	public Integer createNachrichtengruppe(NachrichtengruppeDto dto) {

		try {
			Query query = em.createNamedQuery("NachrichtengruppefindByCBez");

			query.setParameter(1, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Nachrichtengruppe doppelt = (Nachrichtengruppe) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_NACHRICHTENGRUPPE.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTENGRUPPE);
			dto.setIId(pk);

			Nachrichtengruppe bean = new Nachrichtengruppe(dto.getIId(), dto.getCBez());
			em.persist(bean);
			em.flush();
			setNachrichtengruppeFromNachrichtengruppeDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) {

		try {
			Query query = em.createNamedQuery("NachrichtengruppeteilnehmerfindByNachrichtengruppeIIdPersonalIId");

			query.setParameter(1, dto.getNachrichtengruppeIId());
			query.setParameter(2, dto.getPersonalIId());
			// @todo getSingleResult oder getResultList ?
			Nachrichtengruppeteilnehmer doppelt = (Nachrichtengruppeteilnehmer) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("PERS_NACHRICHTENGRUPPETEILNEHMER.UK"));
		} catch (NoResultException ex1) {
			// nothing here
		}

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTENGRUPPETEILNEHMER);
			dto.setIId(pk);

			Nachrichtengruppeteilnehmer bean = new Nachrichtengruppeteilnehmer(dto.getIId(),
					dto.getNachrichtengruppeIId(), dto.getPersonalIId());
			em.persist(bean);
			em.flush();
			setNachrichtengruppeteilnehmerFromNachrichtengruppeteilnehmerDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public Integer createNachrichtenabo(NachrichtenaboDto dto) {

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_NACHRICHTENABO);
			dto.setIId(pk);

			Nachrichtenabo bean = new Nachrichtenabo(dto.getIId(), dto.getNachrichtenartIId());
			em.persist(bean);
			em.flush();
			setNachrichtenaboFromNachrichtenaboDto(bean, dto);
			return dto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	private void setNachrichtengruppeFromNachrichtengruppeDto(Nachrichtengruppe bean, NachrichtengruppeDto dto) {

		bean.setCBez(dto.getCBez());
		em.merge(bean);
		em.flush();
	}

	private void setNachrichtengruppeteilnehmerFromNachrichtengruppeteilnehmerDto(Nachrichtengruppeteilnehmer bean,
			NachrichtengruppeteilnehmerDto dto) {

		bean.setNachrichtengruppeIId(dto.getNachrichtengruppeIId());
		bean.setPersonalIId(dto.getPersonalIId());
		em.merge(bean);
		em.flush();
	}

	private void setNachrichtenaboFromNachrichtenaboDto(Nachrichtenabo bean, NachrichtenaboDto dto) {

		bean.setNachrichtenartIId(dto.getNachrichtenartIId());
		bean.setNachrichtengruppeIId(dto.getNachrichtengruppeIId());
		bean.setPersonalIId(dto.getPersonalIId());
		em.merge(bean);
		em.flush();
	}

	public void updateNachrichtengruppe(NachrichtengruppeDto dto) {
		Nachrichtengruppe bean = em.find(Nachrichtengruppe.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("NachrichtengruppefindByCBez");

			query.setParameter(1, dto.getCBez());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Nachrichtengruppe) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PERS_NACHRICHTENGRUPPE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setNachrichtengruppeFromNachrichtengruppeDto(bean, dto);
	}

	public void updateNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) {
		Nachrichtengruppeteilnehmer bean = em.find(Nachrichtengruppeteilnehmer.class, dto.getIId());

		try {
			Query query = em.createNamedQuery("NachrichtengruppeteilnehmerfindByNachrichtengruppeIIdPersonalIId");

			query.setParameter(1, dto.getNachrichtengruppeIId());
			query.setParameter(2, dto.getPersonalIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Nachrichtengruppeteilnehmer) query.getSingleResult()).getIId();
			if (bean.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PERS_NACHRICHTENGRUPPE.UK"));
			}
		} catch (NoResultException ex) {

		}

		setNachrichtengruppeteilnehmerFromNachrichtengruppeteilnehmerDto(bean, dto);
	}

	private Context getInitialContextForRMI(String host, Integer port) throws NamingException {
		Hashtable<String, String> environment = new Hashtable<String, String>();

		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		environment.put(Context.PROVIDER_URL, "rmi://" + host + ":" + port);

		return new InitialContext(environment);
	}

	// @Asynchronous
	public void benachrichtigeClientsDasNeueNachrichtenVorhandenSind(Integer personalIId, TheClientDto theClientDto) {

		List<VerfuegbareHostsDto> hosts = getTheClientFac().getVerfuegbareHosts(personalIId, theClientDto);

		Iterator it = hosts.iterator();
		while (it.hasNext()) {
			try {

				VerfuegbareHostsDto host = (VerfuegbareHostsDto) it.next();

				Context context = getInitialContextForRMI(host.getHostname(), host.getPort());
				ClientRemoteFac clientRemoteFac = (ClientRemoteFac) context.lookup(ClientRemoteFac.REMOTE_BIND_NAME);
				clientRemoteFac.neueNachrichtenVerfuegbar();
			} catch (Exception e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
		}

	}

	public void updateNachrichtenabo(NachrichtenaboDto dto) {
		Nachrichtenabo bean = em.find(Nachrichtenabo.class, dto.getIId());

		setNachrichtenaboFromNachrichtenaboDto(bean, dto);

	}

	public void removeNachrichtengruppe(NachrichtengruppeDto dto) {
		Nachrichtengruppe toRemove = em.find(Nachrichtengruppe.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Map getMoeglicheNachrichtenempfaenger(TheClientDto theClientDto) {
		LinkedHashMap lhm = new LinkedHashMap();
		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct bms.flrpersonal_zugeordnet.i_id, bms.flrpersonal_zugeordnet.c_kurzzeichen FROM FLRBenutzermandantsystemrolle bms WHERE bms.flrmandant.c_nr='"
				+ theClientDto.getMandant()
				+ "' AND bms.flrpersonal_zugeordnet.b_versteckt=0 AND bms.flrpersonal_zugeordnet.c_kurzzeichen IS NOT NULL ORDER BY bms.flrpersonal_zugeordnet.c_kurzzeichen ASC";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			Object[] oZeile = (Object[]) resultListIterator.next();

			lhm.put(oZeile[0], oZeile[1]);

		}

		return lhm;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(Integer personalIId,
			Integer personalIIdAbsender, String suche, boolean bNurEmpfangene, TheClientDto theClientDto) {
		return getListeDerNachrichtenFuerEinePerson(personalIId, 100, false, personalIIdAbsender, suche, bNurEmpfangene,
				theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<Map<Integer, String>> getListeDerNachrichtenFuerEinePerson(Integer personalIId, int iAnzahl,
			boolean nurNugelesene, Integer personalIIdAbsender, String suche, boolean bNurEmpfangene,
			TheClientDto theClientDto) {

		ArrayList<Map<Integer, String>> m = new ArrayList<Map<Integer, String>>();

		Session session = getNewSession();

		String queryString = "SELECT empfaenger FROM FLRNachrichtenempfaenger empfaenger WHERE 1=1 ";

		if (bNurEmpfangene) {
			queryString += " AND empfaenger.personal_i_id_empfaenger=" + personalIId;
		} else {
			queryString += " AND (empfaenger.personal_i_id_empfaenger=" + personalIId
					+ " OR flrnachrichten.personal_i_id_absender=" + personalIId + ") ";
		}

		if (personalIIdAbsender != null) {
			queryString += " AND (empfaenger.personal_i_id_empfaenger=" + personalIIdAbsender
					+ " OR flrnachrichten.personal_i_id_absender=" + personalIIdAbsender + ") ";
		}
		if (nurNugelesene == true) {
			queryString += " AND empfaenger.t_gelesen IS NULL";
		}

		if (suche != null && suche.length() > 0) {
			queryString += " AND lower( flrnachrichten.x_text) LIKE '%" + suche.toLowerCase() + "%'";
		}

		queryString += " ORDER BY empfaenger.flrnachrichten.t_anlegen DESC";

		org.hibernate.Query query = session.createQuery(queryString);
		query.setMaxResults(iAnzahl);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		while (resultListIterator.hasNext()) {
			FLRNachrichtenempfaenger empf = (FLRNachrichtenempfaenger) resultListIterator.next();

			String absender = "<span style=\"background-color: #C0C0C0\"><i>"
					+ Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
							empf.getFlrnachrichten().getFlrpersonal_absender().getC_kurzzeichen(), 3)
					+ "</i></span>";

			String uhrzeit = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(
					Helper.formatTimestamp(new Timestamp(empf.getFlrnachrichten().getT_anlegen().getTime()),
							theClientDto.getLocUi()),
					20, false);

			String betreff = Helper.fitString2LengthHTMLBefuelltMitLeerzeichen(empf.getFlrnachrichten().getC_betreff(),
					80, false);

			if (empf.getFlrnachrichten().getPersonal_i_id_absender().equals(personalIId)) {
				betreff = "&nbsp;&nbsp;" + betreff;
			}

			if (empf.getT_gelesen() == null) {
				betreff = "<b>&nbsp;&nbsp;" + betreff + "</b>";
			} else {
				betreff = "&#10003; " + betreff;
			}

			if (theClientDto.getIDPersonal().equals(empf.getFlrnachrichten().getFlrpersonal_absender().getI_id())) {
				betreff = "<font color=\"#808080\">" + betreff + "</font>";
			}

			String empfaenger = "<span style=\"background-color: #C0C0C0\">" + Helper
					.fitString2LengthHTMLBefuelltMitLeerzeichen(empf.getFlrpersonal_empfaenger().getC_kurzzeichen(), 3)
					+ "</span>";

			String sZeile = absender + " " + empfaenger + " " + uhrzeit + betreff;

			String s = "<html><body><font color=\"#000000\">" + sZeile + "</font></body></html>";

			Map<Integer, String> mZeile = new LinkedHashMap<Integer, String>();
			mZeile.put(empf.getI_id(), s);

			m.add(mZeile);

		}

		session.close();

		return m;
	}

	public void nachrichtenempfaengerAlsGelesenMarkieren(Integer nachrichtenempaengerIId, boolean bGelesen,
			TheClientDto theClientDto) {
		Nachrichtenempfaenger nachrichtenempfaenger = em.find(Nachrichtenempfaenger.class, nachrichtenempaengerIId);

		if (bGelesen) {
			if (nachrichtenempfaenger.getTGelesen() == null
					&& theClientDto.getIDPersonal().equals(nachrichtenempfaenger.getPersonalIIdEmpfaenger())) {
				nachrichtenempfaenger.setTGelesen(new Timestamp(System.currentTimeMillis()));
			}
		} else {
			if (nachrichtenempfaenger.getTGelesen() != null
					&& theClientDto.getIDPersonal().equals(nachrichtenempfaenger.getPersonalIIdEmpfaenger())) {
				nachrichtenempfaenger.setTGelesen(null);
			}
		}
		em.persist(nachrichtenempfaenger);
		em.flush();
	}

	public String getBetreff(Integer nachrichtenempaengerIId, TheClientDto theClientDto) {
		Nachrichtenempfaenger nachrichtenempfaenger = em.find(Nachrichtenempfaenger.class, nachrichtenempaengerIId);
		Nachrichten nachrichten = em.find(Nachrichten.class, nachrichtenempfaenger.getNachrichtenIId());
		return nachrichten.getCBetreff();
	}

	public void removeNachrichtengruppeteilnehmer(NachrichtengruppeteilnehmerDto dto) {
		Nachrichtengruppeteilnehmer toRemove = em.find(Nachrichtengruppeteilnehmer.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeNachrichtenabo(NachrichtenaboDto dto) {
		Nachrichtenabo toRemove = em.find(Nachrichtenabo.class, dto.getIId());
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	@Override
	public void nachrichtZeitdatenpruefen(Integer personalId, TheClientDto theClientDto) {
		PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(personalId, theClientDto);
		String betreff = getTextRespectUISpr("nachricht.zeitdatenpruefen.betreff", theClientDto.getMandant(),
				theClientDto.getLocUi(), personalDto.getPartnerDto().formatFixTitelName1Name2(),
				personalDto.getCKurzzeichen());
		String text = getTextRespectUISpr("nachricht.zeitdatenpruefen.text", theClientDto.getMandant(),
				theClientDto.getLocUi());
		nachrichtErstellen(Art.ZEITDATENPRUEFEN, betreff, text, null, null, theClientDto);
	}

	@Override
	public void nachrichtProjektzeitenUeberschritten(ProjektDto projektDto, Double dIstZeiten,
			TheClientDto theClientDto) {
		// PJ22284
		if (projektDto.getDDauer() != null && projektDto.getDDauer() > 0) {

			// Zuerst Nachricht an Erzeuger schicken, wenn abboniert
			boolean bEinzelnachrichthatPersonNachrichtenartAbboniert = hatPersonNachrichtenartAbboniert(
					Art.PROJEKTZEITEN_UEBERSCHRITTEN_ERZEUGER, projektDto.getPersonalIIdErzeuger(), theClientDto);

			String bereich = "";
			if (projektDto.getBereichIId() != null) {
				BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());
				bereich = bereichDto.getCBez();

				if (bereich != null && bereich.length() > 2) {
					bereich = bereich.substring(0, 2);
				}

				bereich = bereich + " ";
			}

			String sSoll = Helper.formatZahl(projektDto.getDDauer(), 1, theClientDto.getLocUi());
			String sIst = Helper.formatZahl(dIstZeiten, 1, theClientDto.getLocUi());

			String betreff = getTextRespectUISpr("nachricht.projektzeitenueberschritten.text",
					theClientDto.getMandant(), theClientDto.getLocUi(), bereich + projektDto.getCNr(), sSoll, sIst);
			String text = getTextRespectUISpr("nachricht.projektzeitenueberschritten.text", theClientDto.getMandant(),
					theClientDto.getLocUi(), projektDto.getCNr(), sSoll, sIst);

			if (bEinzelnachrichthatPersonNachrichtenartAbboniert) {

				nachrichtErstellen(Art.PROJEKTZEITEN_UEBERSCHRITTEN_ERZEUGER, betreff, text,
						projektDto.getPersonalIIdErzeuger(), null, LocaleFac.BELEGART_PROJEKT, projektDto.getIId(),
						theClientDto);
			}

			// Und dann noch an den Rest der Abbonenten schicken

			nachrichtErstellen(Art.PROJEKTZEITEN_UEBERSCHRITTEN_ALLE, betreff, text, null,
					projektDto.getPersonalIIdErzeuger(), LocaleFac.BELEGART_PROJEKT, projektDto.getIId(), theClientDto);

		}

	}

	@Override
	public void nachrichtProjektZugeordnet(Integer projektIId, Integer personalIIdZugewiesener,
			TheClientDto theClientDto) {
		try {
			ProjektDto projektDto = getProjektFac().projektFindByPrimaryKey(projektIId);
			if (hatPersonNachrichtenartAbboniert(Art.PROJEKT_ZUGEORDNET, personalIIdZugewiesener, theClientDto)) {

				String bereich = "";
				if (projektDto.getBereichIId() != null) {
					BereichDto bereichDto = getProjektServiceFac().bereichFindByPrimaryKey(projektDto.getBereichIId());
					bereich = bereichDto.getCBez();

					if (bereich != null && bereich.length() > 2) {
						bereich = bereich.substring(0, 2);
					}

					bereich = bereich + " ";
				}

				String betreff = getTextRespectUISpr("nachricht.projektzugeordnet.text", theClientDto.getMandant(),
						theClientDto.getLocUi(), bereich + projektDto.getCNr());
				String text = getTextRespectUISpr("nachricht.projektzugeordnet.text", theClientDto.getMandant(),
						theClientDto.getLocUi(), projektDto.getCNr());
				nachrichtErstellen(Art.PROJEKT_ZUGEORDNET, betreff, text, personalIIdZugewiesener, null,
						LocaleFac.BELEGART_PROJEKT, projektIId, theClientDto);
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
	}

	@Override
	public void nachrichtAblieferpreisepruefenTerminal(Integer losIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {
		LosDto losDto = getFertigungFac().losFindByPrimaryKey(losIId);
		String betreff = getTextRespectUISpr("nachricht.ablieferpreisepruefen.terminal.betreff",
				theClientDto.getMandant(), theClientDto.getLocUi(), losDto.getCNr());
		String text = getTextRespectUISpr("nachricht.ablieferpreisepruefen.terminal.text", theClientDto.getMandant(),
				theClientDto.getLocUi());
		nachrichtErstellen(Art.ABLIEFERPREISEPRUEFEN, betreff, text, LocaleFac.BELEGART_LOS, losIId, theClientDto);
	}

	@Override
	public void nachrichtMasterDataNotFound(ArtikelId artikelId, String artikelCnr, TheClientDto theClientDto) {
		String betreff = getTextRespectUISpr("nachricht.masterdatanotfound.betreff",
				theClientDto.getMandant(), theClientDto.getLocUi(), artikelId.id(), artikelCnr);
		String text = getTextRespectUISpr("nachricht.masterdatanotfound.text", theClientDto.getMandant(),
				theClientDto.getLocUi(), artikelId.id(), artikelCnr);
		nachrichtErstellen(Art.MASTERDATA_NOTFOUND, betreff, text, null, null, theClientDto);
	}
	
	@Override
	public void nachrichtProdOrderNotFound(String losCnr, String artikelCnr, Integer iSortLossollmaterial, TheClientDto theClientDto) {
		String betreff = getTextRespectUISpr("nachricht.prodordernotfound.betreff",
				theClientDto.getMandant(), theClientDto.getLocUi(), losCnr, artikelCnr, iSortLossollmaterial);
		String text = getTextRespectUISpr("nachricht.prodordernotfound.text", theClientDto.getMandant(),
				theClientDto.getLocUi(), losCnr, artikelCnr, iSortLossollmaterial);
		nachrichtErstellen(Art.PRODORDER_NOTFOUND, betreff, text, null, null, theClientDto);
	}
}
