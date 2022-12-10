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

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Reportkonf;
import com.lp.server.system.ejb.Reportvariante;
import com.lp.server.system.ejb.ReportvarianteQuery;
import com.lp.server.system.ejb.Standarddrucker;
import com.lp.server.system.ejb.Text;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.DruckerFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.ReportkonfDto;
import com.lp.server.system.service.ReportkonfDtoAssembler;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.ReportvarianteDtoAssembler;
import com.lp.server.system.service.StandarddruckerDto;
import com.lp.server.system.service.StandarddruckerDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

@Stateless
public class DruckerFacBean extends Facade implements DruckerFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createStandarddrucker(StandarddruckerDto standarddruckerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		// log
		myLogger.logData(standarddruckerDto);
		// begin
		// primary key
		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_STANDARDDRUCKER);
		standarddruckerDto.setIId(iId);
		try {
			// pruefen, ob es schon einen eintrag gibt
			StandarddruckerDto s = standarddruckerFindByPcReportnameOhneExc(
					standarddruckerDto, theClientDto);
			// create wenns noch keinen gibt - sonst update
			if (s == null) {
				// den Pfad auf den naheliegendsten Report setzen
				standarddruckerDto.setCReportname(getNaheliegendstenReportPfad(
						standarddruckerDto, theClientDto));
				// create
				Standarddrucker standarddrucker = new Standarddrucker(
						standarddruckerDto.getIId(),
						standarddruckerDto.getCPc(),
						standarddruckerDto.getCReportname(),
						standarddruckerDto.getCDrucker(),
						theClientDto.getMandant(),
						standarddruckerDto.getBStandard(),
						standarddruckerDto.getReportvarianteIId());
				em.persist(standarddrucker);
				em.flush();
				setStandarddruckerFromStandarddruckerDto(standarddrucker,
						standarddruckerDto);
				aufStandardSetzten(standarddruckerDto);
			} else {
				standarddruckerDto.setIId(s.getIId());
				updateStandarddrucker(standarddruckerDto, theClientDto);
			}
			return standarddruckerDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	private String ressourceFuerReportvarianteAnlegen(
			Integer reportvarianteIId, String bezeichnung,
			TheClientDto theClientDto) {

		String token = "hv.reportvariante" + reportvarianteIId;

		Query query = em
				.createNamedQuery("TextfindByMandantCNrLocaleCNrCToken");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, theClientDto.getLocUiAsString());
		query.setParameter(3, token);

		Text text;
		try {
			text = (Text) query.getSingleResult();
			text.setCText(bezeichnung);
		} catch (NoResultException e) {
			text = new Text(token, theClientDto.getMandant(),
					theClientDto.getLocUiAsString(), bezeichnung);
		}

		em.merge(text);
		em.flush();

		getBenutzerServicesFac().reloadUebersteuertenText();

		return token;

	}

	public Integer createReportvariante(ReportvarianteDto reportvarianteDto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery(ReportvarianteQuery.ByCReportnameCReportnamevariante);
			query.setParameter(1, reportvarianteDto.getCReportname());
			query.setParameter(2, reportvarianteDto.getCReportnamevariante());

			Reportvariante reportvariante = (Reportvariante) query
					.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("LP_REPORTVARIANTE.UK"));

		} catch (NoResultException ex) {

		}

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(
				PKConst.PK_REPORTVARIANTE);
		reportvarianteDto.setIId(iId);

		reportvarianteDto.setCRessource(ressourceFuerReportvarianteAnlegen(
				reportvarianteDto.getIId(), reportvarianteDto.getCRessource(),
				theClientDto));

		try {

			// create
			Reportvariante reportvariante = new Reportvariante(
					reportvarianteDto.getIId(),
					reportvarianteDto.getCRessource(),
					reportvarianteDto.getCReportname(),
					reportvarianteDto.getCReportnamevariante());
			em.persist(reportvariante);
			em.flush();
			setReportvarianteFromReportvarianteDto(reportvariante,
					reportvarianteDto);

			return reportvarianteDto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void updateReportvariante(ReportvarianteDto reportvarianteDto,
			TheClientDto theClientDto) {

		Integer iId = reportvarianteDto.getIId();

		Reportvariante zeitabschluss = em.find(Reportvariante.class, iId);

		try {
			Query query = em
					.createNamedQuery(ReportvarianteQuery.ByCReportnameCReportnamevariante);
			query.setParameter(1, reportvarianteDto.getCReportname());
			query.setParameter(2, reportvarianteDto.getCReportnamevariante());
			Integer iIdVorhanden = ((Reportvariante) query.getSingleResult())
					.getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"LP_REPORTVARIANTE.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

		reportvarianteDto.setCRessource(ressourceFuerReportvarianteAnlegen(
				reportvarianteDto.getIId(), reportvarianteDto.getCRessource(),
				theClientDto));

		setReportvarianteFromReportvarianteDto(zeitabschluss, reportvarianteDto);

	}

	private void aufStandardSetzten(StandarddruckerDto standarddruckerDto) {

		Query query = em
				.createNamedQuery("StandarddruckerfindByPcReportnameMandantCNr");
		query.setParameter(1, standarddruckerDto.getCPc());
		query.setParameter(2, standarddruckerDto.getCReportname());
		query.setParameter(3, standarddruckerDto.getMandantCNr());

		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Standarddrucker std = (Standarddrucker) iterator.next();

			if (std.getIId().equals(standarddruckerDto.getIId())) {
				std.setBStandard((short) 1);
			} else {
				std.setBStandard((short) 0);
			}

			em.merge(std);
			em.flush();
		}

	}

	public void saveReportKonf(Integer standarddruckerIId,
			ReportkonfDto[] dtos, TheClientDto theClientDto) {

		deleteReportKonf(standarddruckerIId, theClientDto);

		for (int i = 0; i < dtos.length; i++) {
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_REPORTKONF);
			dtos[i].setIId(iId);

			Reportkonf reportkonf = new Reportkonf(dtos[i].getIId(),
					dtos[i].getCKomponentenname(),
					dtos[i].getCKomponententyp(), dtos[i].getCKey(),
					dtos[i].getCTyp(), standarddruckerIId);
			em.persist(reportkonf);
			em.flush();
		}

	}

	public void deleteReportKonf(Integer standarddruckerIId,
			TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("StandarddruckerfindByStandarddruckerIId");
		query.setParameter(1, standarddruckerIId);

		Collection<?> cl = query.getResultList();

		Iterator<?> iterator = cl.iterator();
		while (iterator.hasNext()) {
			Reportkonf reportkonf = (Reportkonf) iterator.next();
			em.remove(reportkonf);
			em.flush();
		}

	}

	public void removeReportkonf(Integer reportkonfIId) {
		Reportkonf reportkonf = em.find(Reportkonf.class, reportkonfIId);
		em.remove(reportkonf);
		em.flush();
	}

	/**
	 * @param standarddruckerDto
	 *            StandarddruckerDto
	 * @param idUser
	 *            String
	 * @throws EJBExceptionLP
	 */
	public void removeStandarddrucker(StandarddruckerDto standarddruckerDto,
			String idUser) throws EJBExceptionLP {
		try {
			Integer iId = standarddruckerDto.getIId();
			// schaun, ob der ueberhaupt noch da ist
			Standarddrucker standarddrucker = em.find(Standarddrucker.class,
					iId);
			if (standarddrucker == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// wenn ja, dann loeschen
			em.remove(standarddrucker);
			em.flush();
		}
		// catch (ObjectNotFoundException ex) {
		// nothing here
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void removeReportvariante(ReportvarianteDto dto,
			TheClientDto theClientDto) {
		try {
			Integer iId = dto.getIId();

			// PJ19416 Vorhwer noch ReportKonf und Standarddrucker loeschen
			Query query = em
					.createNamedQuery("StandarddruckerfindByReportvarianteIId");
			query.setParameter(1, iId);

			Collection c = query.getResultList();

			Iterator<?> iterator = c.iterator();
			while (iterator.hasNext()) {

				Standarddrucker std = (Standarddrucker) iterator.next();

				deleteReportKonf(std.getIId(), theClientDto);
				em.remove(std);
				em.flush();

			}

			Reportvariante reportvariante = em.find(Reportvariante.class, iId);
			em.remove(reportvariante);
			em.flush();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public Integer updateStandarddrucker(
			StandarddruckerDto standarddruckerDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		Integer iId = null;
		if (standarddruckerDtoI != null) {
			// try {
			// gibt es fuer diesen PC und Drucker schon einen eintrag?
			StandarddruckerDto standarddrucker = standarddruckerFindByPcReportnameOhneExc(
					standarddruckerDtoI, theClientDto);
			// wenn nicht, dann anlegen
			if (standarddrucker == null) {
				iId = createStandarddrucker(standarddruckerDtoI, theClientDto);
			}
			// wenn schon, dann update
			else {
				// den Pfad auf den naheliegendsten Report setzen
				standarddruckerDtoI
						.setCReportname(getNaheliegendstenReportPfad(
								standarddruckerDtoI, theClientDto));
				// update
				Query query = em
						.createNamedQuery("StandarddruckerfindByPcReportnameMandantCNr");
				query.setParameter(1, standarddruckerDtoI.getCPc());
				query.setParameter(2, standarddruckerDtoI.getCReportname());
				query.setParameter(3, standarddruckerDtoI.getMandantCNr());

				Collection c = query.getResultList();

				Iterator<?> iterator = c.iterator();
				while (iterator.hasNext()) {

					Standarddrucker std = (Standarddrucker) iterator.next();
					if (std.getReportvarianteIId() != null
							&& std.getReportvarianteIId().equals(
									standarddruckerDtoI.getReportvarianteIId())) {
						setStandarddruckerFromStandarddruckerDto(std,
								standarddruckerDtoI);

						aufStandardSetzten(standarddruckerDtoI);
						iId = std.getIId();
						break;
					} else if (std.getReportvarianteIId() == null
							&& standarddruckerDtoI.getReportvarianteIId() == null) {
						setStandarddruckerFromStandarddruckerDto(std,
								standarddruckerDtoI);

						aufStandardSetzten(standarddruckerDtoI);
						iId = std.getIId();
						break;
					} else if (std.getReportvarianteIId() != null
							&& standarddruckerDtoI.getReportvarianteIId() == null) {
						setStandarddruckerFromStandarddruckerDto(std,
								standarddruckerDtoI);

						aufStandardSetzten(standarddruckerDtoI);
						iId = std.getIId();
						break;
					}

				}

			}
			// }
			// catch (ObjectNotFoundException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, ex);
			// }
			// catch (FinderException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
			// }
		}
		return iId;
	}

	public StandarddruckerDto standarddruckerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Standarddrucker standarddrucker = em.find(Standarddrucker.class, iId);
		if (standarddrucker == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleStandarddruckerDto(standarddrucker);

		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	public ReportvarianteDto reportvarianteFindByPrimaryKey(Integer iId) {
		// try {
		Reportvariante standarddrucker = em.find(Reportvariante.class, iId);
		if (standarddrucker == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		return assembleReportvarianteDto(standarddrucker);

	}

	public StandarddruckerDto standarddruckerFindByPcReportname(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// try {
		return findBestmoeglichenStandarddrucker(standarddruckerDto,
				theClientDto);
		// }
		// catch (ObjectNotFoundException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		// }
		// catch (FinderException ex) {
		// { // @ToDo FinderException
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, null);
		// }
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		// }
	}

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneExc(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		try {
			return findBestmoeglichenStandarddrucker(standarddruckerDto,
					theClientDto);
		} catch (Exception e) {
			return null;
		}

	}

	public StandarddruckerDto standarddruckerFindByPcReportnameOhneVariante(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto) {
		String cReportname = getNaheliegendstenReportPfad(standarddruckerDto,
				theClientDto);
		Query query = em
				.createNamedQuery("StandarddruckerfindByPcReportnameMandantCNrOhneVariante");

		query.setParameter(1, standarddruckerDto.getCPc());
		query.setParameter(2, cReportname);
		query.setParameter(3, theClientDto.getMandant());
		try {
			Standarddrucker standarddrucker = (Standarddrucker) query
					.getSingleResult();
			standarddruckerDto = assembleStandarddruckerDto(standarddrucker);
			standarddruckerDto.setCReportname(cReportname);
			return standarddruckerDto;
		} catch (Exception e) {
			return null;
		}

	}

	private StandarddruckerDto findBestmoeglichenStandarddrucker(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP, NoResultException {
		String cReportname = getNaheliegendstenReportPfad(standarddruckerDto,
				theClientDto);

		if (standarddruckerDto.getReportvarianteIId() != null) {
			Query query = em
					.createNamedQuery("StandarddruckerfindByPcReportnameMandantCNrReportvarianteIId");

			query.setParameter(1, standarddruckerDto.getCPc());
			query.setParameter(2, cReportname);
			query.setParameter(3, theClientDto.getMandant());
			query.setParameter(4, standarddruckerDto.getReportvarianteIId());
			Standarddrucker standarddrucker = (Standarddrucker) query
					.getSingleResult();
			standarddruckerDto = assembleStandarddruckerDto(standarddrucker);
			standarddruckerDto.setCReportname(cReportname);
			return standarddruckerDto;

		} else {
			Query query = em
					.createNamedQuery("StandarddruckerfindByPcReportnameMandantCNrBStandard");

			query.setParameter(1, standarddruckerDto.getCPc());
			query.setParameter(2, cReportname);
			query.setParameter(3, theClientDto.getMandant());
			Collection c = query.getResultList();
			StandarddruckerDto[] dtos = assembleStandarddruckerDtos(c);

			if (dtos.length > 0) {
				standarddruckerDto = dtos[0];
				standarddruckerDto.setCReportname(cReportname);
				return standarddruckerDto;
			} else {
				return null;
			}
		}

	}

	public Map holeAlleVarianten(String reportname, TheClientDto theClientDto) {
		TreeMap<Object, Object> tmArten = new TreeMap<Object, Object>();
		Query query = em.createNamedQuery("ReportvarianteFindByCReportname");
		query.setParameter(1, reportname);

		Collection c = query.getResultList();
		ReportvarianteDto[] dtos = assembleReportvarianteDtos(c);

		tmArten.put(
				new Integer(-999),
				getTextRespectUISpr("lp.standard", theClientDto.getMandant(),
						theClientDto.getLocUi()));

		for (int i = 0; i < dtos.length; i++) {
			tmArten.put(
					dtos[i].getIId(),
					getTextRespectUISpr(dtos[i].getCRessource(),
							theClientDto.getMandant(), theClientDto.getLocUi()));
		}
		return tmArten;

	}

	public ReportkonfDto[] reportkonfFindByStandarddruckerIId(
			Integer standarddruckerIId) {
		// try {
		Query query = em
				.createNamedQuery("StandarddruckerfindByStandarddruckerIId");
		query.setParameter(1, standarddruckerIId);

		Collection<?> cl = query.getResultList();

		return assembleReportkonfDtos(cl);

	}

	private String getNaheliegendstenReportPfad(
			StandarddruckerDto standarddruckerDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		String cReportname = null;
		String cReportnameRelative = null;

		String cSubDirectory = null;
		if (standarddruckerDto.getKostenstelleIId_notInDB() != null) {
			KostenstelleDto kstDto = getSystemFac()
					.kostenstelleFindByPrimaryKey(
							standarddruckerDto.getKostenstelleIId_notInDB());
			cSubDirectory = kstDto.getCSubdirectory();
		}
		cReportname = SystemServicesFacBean.getPathFromLPDir(
				standarddruckerDto.getSModul_notInDB(),
				standarddruckerDto.getSFilename_notInDB(),
				standarddruckerDto.getMandantCNr(),
				standarddruckerDto.getLocale_notInDB(), cSubDirectory,
				theClientDto);
		// in relativen Reportpfad zu Report Root Verzeichnis umwandeln
		if (cReportname != null) {
			cReportnameRelative = SystemServicesFacBean.getRelativePathtoLPDir(
					new File(cReportname), theClientDto);
		}

		return cReportnameRelative;
	}

	private void setStandarddruckerFromStandarddruckerDto(
			Standarddrucker standarddrucker,
			StandarddruckerDto standarddruckerDto) {
		standarddrucker.setCPc(standarddruckerDto.getCPc());
		standarddrucker.setCReportname(standarddruckerDto.getCReportname());
		standarddrucker.setCDrucker(standarddruckerDto.getCDrucker());
		standarddrucker.setCDruckerKopien(standarddruckerDto
				.getCDruckerKopien());
		standarddrucker.setMandantCNr(standarddrucker.getMandantCNr());
		standarddrucker.setReportvarianteIId(standarddruckerDto
				.getReportvarianteIId());
		standarddrucker.setBStandard(standarddruckerDto.getBStandard());
		em.merge(standarddrucker);
		em.flush();
	}

	private void setReportvarianteFromReportvarianteDto(
			Reportvariante reportvariante, ReportvarianteDto reportvarianteDto) {
		reportvariante.setCReportname(reportvarianteDto.getCReportname());
		reportvariante.setCReportnamevariante(reportvarianteDto
				.getCReportnamevariante());
		reportvariante.setCRessource(reportvarianteDto.getCRessource());

		em.merge(reportvariante);
		em.flush();
	}

	private ReportkonfDto assembleReportkonfDto(Reportkonf reportkonf) {
		return ReportkonfDtoAssembler.createDto(reportkonf);
	}

	private StandarddruckerDto assembleStandarddruckerDto(
			Standarddrucker standarddrucker) {
		return StandarddruckerDtoAssembler.createDto(standarddrucker);
	}

	private ReportvarianteDto assembleReportvarianteDto(
			Reportvariante reportvariante) {
		return ReportvarianteDtoAssembler.createDto(reportvariante);
	}

	private StandarddruckerDto[] assembleStandarddruckerDtos(
			Collection<?> standarddruckers) {
		List<StandarddruckerDto> list = new ArrayList<StandarddruckerDto>();
		if (standarddruckers != null) {
			Iterator<?> iterator = standarddruckers.iterator();
			while (iterator.hasNext()) {
				Standarddrucker standarddrucker = (Standarddrucker) iterator
						.next();
				list.add(assembleStandarddruckerDto(standarddrucker));
			}
		}
		StandarddruckerDto[] returnArray = new StandarddruckerDto[list.size()];
		return (StandarddruckerDto[]) list.toArray(returnArray);
	}

	private ReportkonfDto[] assembleReportkonfDtos(Collection<?> reportkonfs) {
		List<ReportkonfDto> list = new ArrayList<ReportkonfDto>();
		if (reportkonfs != null) {
			Iterator<?> iterator = reportkonfs.iterator();
			while (iterator.hasNext()) {
				Reportkonf reportkonf = (Reportkonf) iterator.next();
				list.add(assembleReportkonfDto(reportkonf));
			}
		}
		ReportkonfDto[] returnArray = new ReportkonfDto[list.size()];
		return (ReportkonfDto[]) list.toArray(returnArray);
	}

	private ReportvarianteDto[] assembleReportvarianteDtos(
			Collection<?> reportkonfs) {
		List<ReportvarianteDto> list = new ArrayList<ReportvarianteDto>();
		if (reportkonfs != null) {
			Iterator<?> iterator = reportkonfs.iterator();
			while (iterator.hasNext()) {
				Reportvariante reportkonf = (Reportvariante) iterator.next();
				list.add(assembleReportvarianteDto(reportkonf));
			}
		}
		ReportvarianteDto[] returnArray = new ReportvarianteDto[list.size()];
		return (ReportvarianteDto[]) list.toArray(returnArray);
	}

	@Override
	public ReportvarianteDto reportvarianteFindByCReportnameCReportnameVariante(
			String cReportname, String cReportnameVariante) {
		Reportvariante entity = ReportvarianteQuery
				.resultByCReportnameCReportnamevariante(em, cReportname,
						cReportnameVariante);
		if (entity == null) {
			throw EJBExcFactory.reportvarianteZuReportNichtGefunden(cReportname, cReportnameVariante);
		}

		return assembleReportvarianteDto(entity);
	}
}
