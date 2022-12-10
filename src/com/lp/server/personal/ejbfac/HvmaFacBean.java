package com.lp.server.personal.ejbfac;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.LogonFac;
import com.lp.server.personal.ejb.Hvmabenutzer;
import com.lp.server.personal.ejb.HvmabenutzerParameter;
import com.lp.server.personal.ejb.HvmabenutzerParameterQuery;
import com.lp.server.personal.ejb.HvmabenutzerQuery;
import com.lp.server.personal.ejb.Hvmalizenz;
import com.lp.server.personal.ejb.HvmalizenzQuery;
import com.lp.server.personal.ejb.Hvmaparameter;
import com.lp.server.personal.ejb.HvmaparameterPK;
import com.lp.server.personal.ejb.Hvmarecht;
import com.lp.server.personal.ejb.Hvmarolle;
import com.lp.server.personal.ejb.Hvmasync;
import com.lp.server.personal.fastlanereader.generated.FLRHvmarecht;
import com.lp.server.personal.fastlanereader.generated.FLRHvmarolle;
import com.lp.server.personal.fastlanereader.generated.FLRHvmasync;
import com.lp.server.personal.service.HvmaFac;
import com.lp.server.personal.service.HvmaLizenzEnum;
import com.lp.server.personal.service.HvmabenutzerDto;
import com.lp.server.personal.service.HvmabenutzerParameterDto;
import com.lp.server.personal.service.HvmalizenzDto;
import com.lp.server.personal.service.HvmaparameterDto;
import com.lp.server.personal.service.HvmarechtDto;
import com.lp.server.personal.service.HvmarolleDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.system.ejb.Arbeitsplatzparameter;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvmabenutzerId;
import com.lp.server.util.LPReport;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
public class HvmaFacBean extends LPReport implements HvmaFac {
//	@PersistenceContext
//	private EntityManager em;

	private Object[][] data = null;
	private String cAktuellerReport = null;

	private static int REPORT_HVMASYNC_PERSON = 0;
	private static int REPORT_HVMASYNC_ZEITPUNKT = 1;
	private static int REPORT_HVMASYNC_ROLLE_ZUM_SYNCZEITPUNKT = 2;
	private static int REPORT_HVMASYNC_WOWURDEGEBUCHT = 3;
	private static int REPORT_HVMASYNC_ANZAHL_SPALTEN = 4;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		String fieldName = jRField.getName();

		if (cAktuellerReport.equals(HvmaFac.REPORT_LETZTESYNCHRONISATION)) {

			if ("Person".equals(fieldName)) {
				value = data[index][REPORT_HVMASYNC_PERSON];
			} else if ("Zeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_HVMASYNC_ZEITPUNKT];
			} else if ("RolleZumZeitpunkt".equals(fieldName)) {
				value = data[index][REPORT_HVMASYNC_ROLLE_ZUM_SYNCZEITPUNKT];
			} else if ("WoWurdeGebucht".equals(fieldName)) {
				value = data[index][REPORT_HVMASYNC_WOWURDEGEBUCHT];
			}
		}
		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printLetzteSynchronisation(DatumsfilterVonBis datumsfilter, TheClientDto theClientDto) {

		cAktuellerReport = REPORT_LETZTESYNCHRONISATION;

		HashMap<String, Object> parameter = new HashMap<String, Object>();

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT s FROM FLRHvmasync s WHERE 1=1 ";

		if (datumsfilter.getTimestampVon() != null) {

			queryString += " AND s.t_sync >='" + Helper.formatTimestampWithSlashes(datumsfilter.getTimestampVon())
					+ "'";
			parameter.put("P_VON", datumsfilter.getTimestampVon());
		}

		if (datumsfilter.getTimestampBis() != null) {
			queryString += " AND s.t_sync <'" + Helper.formatTimestampWithSlashes(datumsfilter.getTimestampBis()) + "'";

			parameter.put("P_BIS", datumsfilter.getTimestampBisUnveraendert());
		}

		queryString += " ORDER BY s.t_sync DESC";

		org.hibernate.Query query = session.createQuery(queryString);

		List<?> results = query.list();

		Iterator<?> resultListIterator = results.iterator();

		ArrayList al = new ArrayList();

		while (resultListIterator.hasNext()) {
			FLRHvmasync flr = (FLRHvmasync) resultListIterator.next();
			Object[] oZeile = new Object[REPORT_HVMASYNC_ANZAHL_SPALTEN];

			oZeile[REPORT_HVMASYNC_PERSON] = HelperServer
					.formatPersonAusFLRPartner(flr.getFlrpersonal().getFlrpartner());
			oZeile[REPORT_HVMASYNC_ROLLE_ZUM_SYNCZEITPUNKT] = flr.getFlrsystemrolle().getC_bez();
			oZeile[REPORT_HVMASYNC_ZEITPUNKT] = flr.getT_sync();
			oZeile[REPORT_HVMASYNC_WOWURDEGEBUCHT] = flr.getC_wowurdegebucht();

			al.add(oZeile);

		}

		session.close();

		Object[][] returnArray = new Object[al.size()][REPORT_HVMASYNC_ANZAHL_SPALTEN];
		data = (Object[][]) al.toArray(returnArray);

		initJRDS(parameter, PersonalFac.REPORT_MODUL, REPORT_LETZTESYNCHRONISATION, theClientDto.getMandant(),
				theClientDto.getLocMandant(), theClientDto);
		return getReportPrint();
	}

	public Integer createHvmasync(String tQuelle, TheClientDto theClientDto) {

		if (tQuelle == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN, new Exception("tQuelle == null"));
		}

		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMASYNC);

		Hvmasync hvmasync = new Hvmasync(pk, theClientDto.getIDPersonal(), getTimestamp(),
				getTheJudgeFac().getSystemrolleIId(theClientDto), tQuelle);

		em.persist(hvmasync);
		em.flush();
		return pk;
	}

	public Integer createHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto) {
		try {

			HvmabenutzerParameter bp = HvmabenutzerParameterQuery.findCnrKategorie(em,
					new HvmabenutzerId(dto.getHvmabenutzerId()), dto.getCNr(), dto.getKategorie());
			if (bp != null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
						new Exception("PERS_HVMABENUTZERPARAMETER.UK"));
			}

		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMABENUTZERPARAMETER);
		dto.setIId(pk);

		dto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		dto.setTAendern(getTimestamp());

		try {
			HvmabenutzerParameter bean = new HvmabenutzerParameter(dto.getIId(), dto.getHvmabenutzerId(), dto.getCNr(),
					dto.getKategorie(), dto.getWert(), dto.getPersonalIIdAendern(), dto.getTAendern());
			em.persist(bean);
			em.flush();

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return dto.getIId();

	}

	public Integer createHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto) {
		try {
			Hvmabenutzer temp = HvmabenutzerQuery.findBenutzerIdLizenzId(em, dto.getBenutzerIId(),
					dto.getHvmalizenzIId());
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PERS_HVMABENUTZER.UK"));
		} catch (NoResultException ex1) {
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMABENUTZER);
		dto.setIId(pk);

		try {
			Hvmabenutzer bean = new Hvmabenutzer(dto.getIId(), dto.getBenutzerIId(), dto.getHvmalizenzIId());
			em.persist(bean);
			em.flush();

			setHvmabenutzerFromHvmabenutzerDto(bean, dto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		pruefeObHvmaBenutzerUeberschritten(dto.getHvmalizenzIId());

		return dto.getIId();
	}

	private void pruefeObHvmaBenutzerUeberschritten(Integer hvmalizenzIId) {

		Query query = em.createNamedQuery("HvmabenutzerfindByHvmalizenzIId");
		query.setParameter(1, hvmalizenzIId);
		Long l = (Long) query.getSingleResult();

		Hvmalizenz bean = em.find(Hvmalizenz.class, hvmalizenzIId);

		if (l != null && l > bean.getIMaxUser()) {
			ArrayList al = new ArrayList();
			al.add(bean.getIMaxUser());

			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HVMABENUTZER_ANZAHL_UEBERSCHRITTEN, al,
					new Exception("EJBExceptionLP.FEHLER_HVMABENUTZER_ANZAHL_UEBERSCHRITTEN"));
		}

	}

	public Integer createHvmarolle(HvmarolleDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("HvmarollefindBySystemrolleIIdHvmarechtIId");
			query.setParameter(1, dto.getSystemrolleIId());
			query.setParameter(2, dto.getHvmarechtIId());
			Hvmarolle temp = (Hvmarolle) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PERS_HVMAROLLE.UK"));

		} catch (NoResultException ex1) {
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_HVMAROLLE);
		dto.setIId(pk);

		try {
			Hvmarolle bean = new Hvmarolle(dto.getIId(), dto.getSystemrolleIId(), dto.getHvmarechtIId());
			em.persist(bean);
			em.flush();

			setHvmarolleFromHvmarolleDto(bean, dto);

		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return dto.getIId();
	}

	public ArrayList<HvmarechtDto> getHvmaRechte(String hvmalizenzCNr, TheClientDto theClientDto) {
		ArrayList<HvmarechtDto> rechte = new ArrayList<HvmarechtDto>();

		String benutzername = theClientDto.getBenutzername().trim().substring(0,
				theClientDto.getBenutzername().indexOf("|"));

		BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(benutzername);

		if (benutzerDto != null && benutzerDto.getIId() != null) {
			String queryString = "SELECT r FROM FLRHvmarecht r " + "WHERE r.flrhvmalizenz.c_nr='" + hvmalizenzCNr
					+ "' AND r.b_aktiv=1 ";
//			String queryString = "SELECT r FROM FLRHvmarolle r " +
//					"WHERE r.flrhvmarecht.flrhvmalizenz.c_nr='" + hvmalizenzCNr 
//					+ "' AND r.flrhvmarecht.b_aktiv=1 "
//					+ " AND r.systemrolle_i_id=" + theClientDto.getSystemrolleIId();
			Session session = getNewSession();
			org.hibernate.Query query = session.createQuery(queryString);

			List<FLRHvmarecht> resultList = query.list();
			for (FLRHvmarecht flrHvmarecht : resultList) {
				rechte.add(hvmarechtFindByPrimaryKey(flrHvmarecht.getI_id()));
			}

			closeSession(session);
		}

		return rechte;
	}

	public void removeHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto) {

		Hvmabenutzer bean = em.find(Hvmabenutzer.class, dto.getIId());
		em.remove(bean);
		em.flush();

	}

	public void removeHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto) {

		HvmabenutzerParameter bean = em.find(HvmabenutzerParameter.class, dto.getIId());
		em.remove(bean);
		em.flush();

	}

	public void removeHvmarolle(HvmarolleDto dto, TheClientDto theClientDto) {

		Hvmarolle bean = em.find(Hvmarolle.class, dto.getIId());
		em.remove(bean);
		em.flush();

	}

	public void updateHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();
		// try {
		Hvmabenutzer bean = em.find(Hvmabenutzer.class, iId);
		if (bean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Hvmabenutzer hvmabenutzer = HvmabenutzerQuery.findBenutzerIdLizenzId(em, dto.getBenutzerIId(),
					dto.getHvmalizenzIId());
			if (iId.equals(hvmabenutzer.getIId()) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PERS_HVMABENUTZER.UK"));
			}
		} catch (NoResultException ex) {
			// nix
		}

		setHvmabenutzerFromHvmabenutzerDto(bean, dto);

		pruefeObHvmaBenutzerUeberschritten(dto.getHvmalizenzIId());

	}

	public void updateHvmarolle(HvmarolleDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();
		// try {
		Hvmarolle bean = em.find(Hvmarolle.class, iId);
		if (bean == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");

		}
		try {
			Query query = em.createNamedQuery("HvmarollefindBySystemrolleIIdHvmarechtIId");
			query.setParameter(1, dto.getSystemrolleIId());
			query.setParameter(2, dto.getHvmarechtIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Hvmarolle) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("PERS_HVMABENUTZER.UK"));
			}

		} catch (NoResultException ex) {
			// nix
		}

		setHvmarolleFromHvmarolleDto(bean, dto);

	}

	public void updateHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto) {

		HvmabenutzerParameter bean = em.find(HvmabenutzerParameter.class, dto.getIId());

		try {

			HvmabenutzerParameter bp = HvmabenutzerParameterQuery.findCnrKategorie(em,
					new HvmabenutzerId(dto.getHvmabenutzerId()), dto.getCNr(), dto.getKategorie());
			if (bp != null) {

				if (bp.getIId().equals(dto.getIId()) == false) {

					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
							new Exception("PERS_HVMABENUTZERPARAMETER.UK"));
				}
			}

		} catch (NoResultException ex1) {
		}

		bean.setPersonalIIdAendern(theClientDto.getIDPersonal());
		bean.setTAendern(getTimestamp());

		bean.setCNr(dto.getCNr());
		bean.setKategorie(dto.getKategorie());
		bean.setHvmabenutzerId(dto.getHvmabenutzerId());
		
		bean.setWert(dto.getWert());

		em.merge(bean);
		em.flush();

	}

	@Override
	public HvmalizenzDto hvmalizenzFindByCnr(String licenceCnr) {
		try {
			Hvmalizenz l = HvmalizenzQuery.findCnr(em, licenceCnr);
			return HvmalizenzDtoAssembler.createDto(l);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, licenceCnr);
		}
	}

	@Override
	public HvmalizenzDto hvmalizenzFindByEnum(HvmaLizenzEnum licence) {
		return hvmalizenzFindByCnr(licence.getText());
	}

	public HvmalizenzDto hvmalizenzFindByPrimaryKey(Integer iId) {
		Hvmalizenz bean = em.find(Hvmalizenz.class, iId);
		return HvmalizenzDtoAssembler.createDto(bean);
	}

	public HvmabenutzerParameterDto hvmabenutzerparameterFindByPrimaryKey(Integer iId) {
		HvmabenutzerParameter bean = em.find(HvmabenutzerParameter.class, iId);
		return HvmabenutzerParameterDtoAssembler.createDto(bean);
	}

	public HvmarechtDto hvmarechtFindByPrimaryKey(Integer iId) {
		Hvmarecht bean = em.find(Hvmarecht.class, iId);
		return HvmarechtDtoAssembler.createDto(bean);
	}

	public HvmarolleDto hvmarolleFindByPrimaryKey(Integer iId) {
		Hvmarolle bean = em.find(Hvmarolle.class, iId);
		return HvmarolleDtoAssembler.createDto(bean);
	}

	public HvmabenutzerDto hvmabenutzerFindByPrimaryKey(Integer iId) {
		Hvmabenutzer bean = em.find(Hvmabenutzer.class, iId);
		return HvmabenutzerDtoAssembler.createDto(bean);
	}

	@Override
	public HvmabenutzerDto hvmabenutzerFindByBenutzerIdLizenzId(Integer benutzerId, Integer lizenzId) {
		try {
			Hvmabenutzer bean = HvmabenutzerQuery.findBenutzerIdLizenzId(em, benutzerId, lizenzId);
			return HvmabenutzerDtoAssembler.createDto(bean);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, benutzerId.toString());
		}
	}

	private void setHvmabenutzerFromHvmabenutzerDto(Hvmabenutzer bean, HvmabenutzerDto dto) {
		bean.setBenutzerIId(dto.getBenutzerIId());
		bean.setHvmalizenzIId(dto.getHvmalizenzIId());
		bean.setTAnlegen(dto.getTAnlegen());
		bean.setcToken(dto.getCToken());
		em.merge(bean);
		em.flush();
	}

	private void setHvmarolleFromHvmarolleDto(Hvmarolle bean, HvmarolleDto dto) {
		bean.setHvmarechtIId(dto.getHvmarechtIId());
		bean.setSystemrolleIId(dto.getSystemrolleIId());
		em.merge(bean);
		em.flush();
	}

	@Override
	public ArrayList<HvmarechtDto> getHvmaRechte(TheClientDto theClientDto) throws RemoteException {
		ArrayList<HvmarechtDto> rechte = new ArrayList<HvmarechtDto>();

		TheClientDto dbClientDto = getTheClientFac().theClientFindByUserLoggedInMobil(theClientDto.getIDUser());
		if (!dbClientDto.getIDUser().equals(theClientDto.getIDUser())) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
					theClientDto.getBenutzername());
		}

		StringTokenizer st = new StringTokenizer(theClientDto.getBenutzername().trim(), LogonFac.USERNAMEDELIMITER);
		String benutzername = st.nextToken();

		BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(benutzername);
		if (benutzerDto == null || benutzerDto.getIId() == null) {
			return rechte;
		}

		// Ist dieser User immer noch mit dieser Lizenz erlaubt?
		HvmabenutzerDto hvmaBenutzerDto = hvmabenutzerFindByBenutzerIdLizenzId(benutzerDto.getIId(),
				dbClientDto.getHvmaLizenzId());

		String queryString = "SELECT r FROM FLRHvmarolle r " + "WHERE r.flrhvmarecht.flrhvmalizenz.i_id="
				+ dbClientDto.getHvmaLizenzId().toString() + " AND r.flrhvmarecht.b_aktiv=1"
				+ " AND r.systemrolle_i_id=" + dbClientDto.getSystemrolleIId();
		Session session = getNewSession();
		org.hibernate.Query query = session.createQuery(queryString);

		List<FLRHvmarolle> resultList = query.list();
		for (FLRHvmarolle flrHvmarolle : resultList) {
			rechte.add(hvmarechtFindByPrimaryKey(flrHvmarolle.getHvmarecht_i_id()));
		}

		closeSession(session);
		return rechte;
	}

	@Override
	public void updateLizenzMaxBenutzer(HvmaLizenzEnum lizenz, Integer maxUser) {
		String licenceCnr = lizenz.getText();
		try {
			Hvmalizenz l = HvmalizenzQuery.findCnr(em, licenceCnr);
			l.setIMaxUser(maxUser);
			em.merge(l);
			em.flush();
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, licenceCnr);
		}
	}

	public HvmaparameterDto assembleHvmaparameterDto(Hvmaparameter bean) {
		return HvmaparameterDtoAssembler.createDto(bean);
	}

	public void updateParameter(HvmaparameterDto dto, TheClientDto theClientDto) {

		HvmaparameterPK pk = new HvmaparameterPK(dto.getCNr(), dto.getKategorie());

		Hvmaparameter bean = em.find(Hvmaparameter.class, pk);
		bean.setDefaultWert(dto.getDefaultWert());

		em.merge(bean);
		em.flush();

	}

	public HvmaparameterDto parameterFindByPrimaryKey(HvmaparameterPK pk) {
		Validator.notNull(pk, "pk");

		Hvmaparameter bean = em.find(Hvmaparameter.class, pk);
		Validator.entityFoundCnr(bean, pk.getCNr() + "/" + pk.getKategorie());
		return assembleHvmaparameterDto(bean);
	}

	public HvmabenutzerParameterDto benutzerParameterFindByCnrKategorie(HvmabenutzerId benutzerId, Param param,
			Kat kategorie) {
		Validator.notNull(benutzerId, "benutzerId");
		Validator.notEmpty(param.getText(), "cnr");
		Validator.notEmpty(kategorie.getText(), "kategorie");

		HvmabenutzerParameter bp = HvmabenutzerParameterQuery.findCnrKategorie(em, benutzerId, param.getText(),
				kategorie.getText());
		if (bp == null) {
			HvmaparameterDto paramDto = parameterFindByPrimaryKey(
					new HvmaparameterPK(param.getText(), kategorie.getText()));
			bp = new HvmabenutzerParameter();
			bp.setHvmabenutzerId(benutzerId.id());
			bp.setCNr(paramDto.getCNr());
			bp.setKategorie(paramDto.getKategorie());
			bp.setWert(paramDto.getDefaultWert());
		}

		return HvmabenutzerParameterDtoAssembler.createDto(bp);
	}

	public HvmabenutzerId fromClient(TheClientDto theClientDto) {
		Validator.notNull(theClientDto, "theClientDto");

		StringTokenizer st = new StringTokenizer(theClientDto.getBenutzername().trim(), LogonFac.USERNAMEDELIMITER);
		String benutzername = st.nextToken();

		BenutzerDto benutzerDto = getBenutzerFac().benutzerFindByCBenutzerkennungOhneExc(benutzername);
		if (benutzerDto == null || benutzerDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BENUTZER_IST_NICHT_MEHR_GUELTIG,
					theClientDto.getBenutzername());
		}

		// Ist dieser User immer noch mit dieser Lizenz erlaubt?
		HvmabenutzerDto hvmaBenutzerDto = hvmabenutzerFindByBenutzerIdLizenzId(benutzerDto.getIId(),
				theClientDto.getHvmaLizenzId());
		return new HvmabenutzerId(hvmaBenutzerDto.getIId());
	}

	@Override
	public List<HvmabenutzerParameterDto> parameterMobil(HvmabenutzerId benutzerId) {
		Validator.notNull(benutzerId, "benutzerId");

		List<HvmabenutzerParameter> beans = HvmabenutzerParameterQuery.listBenutzerId(em, benutzerId);
		return HvmabenutzerParameterDtoAssembler.createListDtos(beans);
	}

	@Override
	public List<HvmabenutzerParameterDto> parameterMobil(TheClientDto theClientDto) {
		return parameterMobil(fromClient(theClientDto));
	}

	@Override
	public String getTaetigkeitAuftrag(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.taetigkeitStandard,
				Kat.Auftrag);
		return dto.asString();
	}

	@Override
	public String getTaetigkeitAuftrag(TheClientDto theClientDto) {
		return getTaetigkeitAuftrag(fromClient(theClientDto));
	}

	private Integer verifyArbeitszeitArtikel(String cnr, TheClientDto theClientDto) throws RemoteException {
		ArtikelDto artikelDto = getArtikelFac().artikelFindByCNrOhneExc(cnr, theClientDto);
		if (artikelDto == null || !artikelDto.isArbeitszeit()) {
			myLogger.warn("Taetigkeit '" + cnr + "' ist kein Arbeitszeitartikel!");
			return null;
		}

		return artikelDto.getIId();
	}

	@Override
	public Integer getTaetigkeitIdAuftrag(TheClientDto theClientDto) throws RemoteException {
		HvmabenutzerId benutzerId = fromClient(theClientDto);
		String s = getTaetigkeitAuftrag(benutzerId);
		if (s == null)
			return null;

		return verifyArbeitszeitArtikel(s, theClientDto);
	}

	@Override
	public Integer getTaetigkeitIdProjekt(TheClientDto theClientDto) throws RemoteException {
		HvmabenutzerId benutzerId = fromClient(theClientDto);
		String s = getTaetigkeitProjekt(benutzerId);
		if (s == null)
			return null;

		return verifyArbeitszeitArtikel(s, theClientDto);
	}

	@Override
	public String getTaetigkeitProjekt(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.taetigkeitStandard,
				Kat.Projekt);
		return dto.asString();
	}

	@Override
	public String getTaetigkeitProjekt(TheClientDto theClientDto) {
		return getTaetigkeitProjekt(fromClient(theClientDto));
	}

	@Override
	public Integer getZielterminAuftrag(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.zielterminTage,
				Kat.Auftrag);
		return dto.asInteger();
	}

	@Override
	public Integer getZielterminAuftrag(TheClientDto theClientDto) {
		return getZielterminAuftrag(fromClient(theClientDto));
	}

	@Override
	public Integer getZielterminLos(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.zielterminTage, Kat.Los);
		return dto.asInteger();
	}

	@Override
	public Integer getZielterminLos(TheClientDto theClientDto) {
		return getZielterminLos(fromClient(theClientDto));
	}

	@Override
	public Integer getZielterminProjekt(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.zielterminTage,
				Kat.Projekt);
		return dto.asInteger();
	}

	@Override
	public Integer getZielterminProjekt(TheClientDto theClientDto) {
		return getZielterminProjekt(fromClient(theClientDto));
	}

	@Override
	public List<String> getBelegStatusAuftrag(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.belegstatusListe,
				Kat.Auftrag);
		return dto.asStrings();
	}

	@Override
	public List<String> getBelegStatusAuftrag(TheClientDto theClientDto) {
		return getBelegStatusAuftrag(fromClient(theClientDto));
	}

	@Override
	public List<String> getBelegStatusLos(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.belegstatusListe, Kat.Los);
		return dto.asStrings();
	}

	@Override
	public List<String> getBelegStatusLos(TheClientDto theClientDto) {
		return getBelegStatusLos(fromClient(theClientDto));
	}

	@Override
	public List<String> getBelegStatusProjekt(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.belegstatusListe,
				Kat.Projekt);
		return dto.asStrings();
	}

	@Override
	public List<String> getBelegStatusProjekt(TheClientDto theClientDto) {
		return getBelegStatusProjekt(fromClient(theClientDto));
	}

	@Override
	public Integer getMenurechteKueche(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.menuRechte, Kat.Kueche);
		return dto.asInteger();
	}

	@Override
	public Integer getMenurechteKueche(TheClientDto theClientDto) {
		return getMenurechteKueche(fromClient(theClientDto));
	}

	@Override
	public Integer getEssensausgabeKundeIdKueche(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.essensausgabeKundeId, Kat.Kueche);
		return dto.asInteger();
	}

	@Override
	public Integer getEssensausgabeKundeIdKueche(TheClientDto theClientDto) {
		return getEssensausgabeKundeIdKueche(fromClient(theClientDto));
	}

	@Override
	public String getProjektShopKueche(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.projektShop, Kat.Kueche);
		return dto.asString();
	}

	@Override
	public String getProjektShopKueche(TheClientDto theClientDto) {
		return getProjektShopKueche(fromClient(theClientDto));
	}

	@Override
	public String getProjektWebKueche(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.projektWeb, Kat.Kueche);
		return dto.asString();
	}

	@Override
	public String getProjektWebKueche(TheClientDto theClientDto) {
		return getProjektWebKueche(fromClient(theClientDto));
	}

	@Override
	public boolean getAlleArtikelKueche(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.alleArtikel, Kat.Kueche);
		return dto.asBoolean();
	}

	@Override
	public boolean getAlleArtikelKueche(TheClientDto theClientDto) {
		return getAlleArtikelKueche(fromClient(theClientDto));
	}

	@Override
	public boolean getLieferterminKommentarEingabe(HvmabenutzerId benutzerId) {
		HvmabenutzerParameterDto dto = benutzerParameterFindByCnrKategorie(benutzerId, Param.lieferterminKommentarEingabe, Kat.Kueche);
		return dto.asBoolean();
	}

	@Override
	public boolean getLieferterminKommentarEingabe(TheClientDto theClientDto) {
		return getLieferterminKommentarEingabe(fromClient(theClientDto));
	}
}
