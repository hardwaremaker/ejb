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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.lp.server.system.ejb.Belegartdokument;
import com.lp.server.system.ejb.Dokument;
import com.lp.server.system.ejb.Dokumentschlagwort;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.BelegartdokumentDto;
import com.lp.server.system.service.BelegartdokumentDtoAssembler;
import com.lp.server.system.service.DokumentDto;
import com.lp.server.system.service.DokumentDtoAssembler;
import com.lp.server.system.service.DokumenteFac;
import com.lp.server.system.service.DokumentschlagwortDto;
import com.lp.server.system.service.DokumentschlagwortDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.util.EJBExceptionLP;

@Stateless
public class DokumenteFacBean extends LPReport implements DokumenteFac {
	@PersistenceContext
	private EntityManager em;;

	public Object getFieldValue(JRField jRField) throws JRException {
		Object value = null;
		return value;
	}

	/**
	 * Methode fuer JRDataSource
	 * 
	 * @return boolean
	 * @throws JRException
	 */
	public boolean next() throws JRException {
		index++;
		return false ;
	}

	public Integer createDokument(DokumentDto dokumentDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (dokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentDto == null"));
		}
		if (dokumentDto.getDatenformatCNr() == null
				|| dokumentDto.getOInhalt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"dokumentDto.getDatenformatCNr() == null || dokumentDto.getOInhalt() == null"));
		}
		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DOKUMENT);
		dokumentDto.setIId(pk);

		dokumentDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		dokumentDto.setTAnlegen(new Timestamp(System.currentTimeMillis()));

		try {
			Dokument dokument = new Dokument(dokumentDto.getIId(), dokumentDto
					.getOInhalt(), dokumentDto.getDatenformatCNr(), dokumentDto
					.getPersonalIIdAnlegen());
			em.persist(dokument);
			em.flush();
			setDokumentFromDokumentDto(dokument, dokumentDto);

			if (dokumentDto.getDokumentschlagwortDto() != null
					&& dokumentDto.getDokumentschlagwortDto().length > 0) {

				for (int i = 0; i < dokumentDto.getDokumentschlagwortDto().length; i++) {
					DokumentschlagwortDto dto = dokumentDto
							.getDokumentschlagwortDto()[i];
					dto.setDokumentIId(dokumentDto.getIId());
					createDokumentschlagwort(dto);
				}
			}

			return dokumentDto.getIId();
		}

		catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeDokument(DokumentDto dokumentDto) throws EJBExceptionLP {
		if (dokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentDto == null"));
		}
		if (dokumentDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dokumentDto.getIId() == null"));
		}
		Dokument toRemove = em.find(Dokument.class, dokumentDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void updateDokument(DokumentDto dokumentDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (dokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentDto == null"));
		}
		if (dokumentDto.getIId() == null
				|| dokumentDto.getPersonalIIdAnlegen() == null
				|| dokumentDto.getTAnlegen() == null
				|| dokumentDto.getDatenformatCNr() == null
				|| dokumentDto.getOInhalt() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"dokumentDto.getIId() == null || dokumentDto.getPersonalIIdAnlegen() == null || dokumentDto.getTAnlegen() || dokumentDto.getDatenformatCNr() == null || dokumentDto.getOInhalt() == null"));
		}
		Integer iId = dokumentDto.getIId();
		Dokument dokument = null;
		// try {
		dokument = em.find(Dokument.class, iId);
		if (dokument == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		setDokumentFromDokumentDto(dokument, dokumentDto);
	}

	public DokumentDto dokumentFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		DokumentDto dokumentDto = new DokumentDto();
		Dokument dokument = em.find(Dokument.class, iId);
		if (dokument == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}
		dokumentDto = assembleDokumentDto(dokument);

		dokumentDto
				.setDokumentschlagwortDto(dokumentschlagwortFindByDokumentIId(iId));

		return dokumentDto;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public DokumentDto dokumentFindByPrimaryKeyOhneInhaltBeiPdf(Integer iId)
			throws EJBExceptionLP {

		DokumentDto dokumentDto = dokumentFindByPrimaryKey(iId);

		if (dokumentDto
				.getDatenformatCNr()
				.equals(
						com.lp.server.system.service.MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
			// dokumentDto.setOInhalt(null);
		}
		return dokumentDto;
	}

	private void setDokumentFromDokumentDto(Dokument dokument,
			DokumentDto dokumentDto) {
		dokument.setOInhalt(dokumentDto.getOInhalt());
		dokument.setDatenformatCNr(dokumentDto.getDatenformatCNr());
		dokument.setCDateiname(dokumentDto.getCDateiname());
		dokument.setCBez(dokumentDto.getCBez());
		dokument.setPersonalIIdAnlegen(dokumentDto.getPersonalIIdAnlegen());
		dokument.setTAnlegen(dokumentDto.getTAnlegen());
		em.merge(dokument);
		em.flush();
	}

	private DokumentDto assembleDokumentDto(Dokument dokument) {
		return DokumentDtoAssembler.createDto(dokument);
	}

	private DokumentDto[] assembleDokumentDtos(Collection<?> dokuments) {
		List<DokumentDto> list = new ArrayList<DokumentDto>();
		if (dokuments != null) {
			Iterator<?> iterator = dokuments.iterator();
			while (iterator.hasNext()) {
				Dokument dokument = (Dokument) iterator.next();
				list.add(assembleDokumentDto(dokument));
			}
		}
		DokumentDto[] returnArray = new DokumentDto[list.size()];
		return (DokumentDto[]) list.toArray(returnArray);
	}

	public Integer createDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP {
		if (dokumentschlagwortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentschlagwortDto == null"));
		}
		if (dokumentschlagwortDto.getDokumentIId() == null
				|| dokumentschlagwortDto.getCSchlagwort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"dokumentschlagwortDto.getDokumentIId() == null || dokumentschlagwortDto.getCSchlagwort() == null"));
		}

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_DOKUMENTSCHLAGWORT);
		dokumentschlagwortDto.setIId(pk);

		try {
			Dokumentschlagwort dokumentschlagwort = new Dokumentschlagwort(
					dokumentschlagwortDto.getIId(), dokumentschlagwortDto
							.getDokumentIId(), dokumentschlagwortDto
							.getCSchlagwort());
			em.persist(dokumentschlagwort);
			em.flush();
			setDokumentschlagwortFromDokumentschlagwortDto(dokumentschlagwort,
					dokumentschlagwortDto);
			return dokumentschlagwortDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP {
		if (dokumentschlagwortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentschlagwortDto == null"));
		}
		if (dokumentschlagwortDto.getIId() == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("dokumentschlagwortDto.getIId() == null"));
		}
		Dokumentschlagwort toRemove = em.find(Dokumentschlagwort.class,
				dokumentschlagwortDto.getIId());
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void updateDokumentschlagwort(
			DokumentschlagwortDto dokumentschlagwortDto) throws EJBExceptionLP {
		if (dokumentschlagwortDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("dokumentschlagwortDto == null"));
		}
		if (dokumentschlagwortDto.getIId() == null
				|| dokumentschlagwortDto.getDokumentIId() == null
				|| dokumentschlagwortDto.getCSchlagwort() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception(
							"dokumentschlagwortDto.getIId() == null || dokumentschlagwortDto.getDokumentIId() == null || dokumentschlagwortDto.getCSchlagwort() == null"));
		}
		Integer iId = dokumentschlagwortDto.getIId();
		Dokumentschlagwort dokumentschlagwort = null;
		// try {
		dokumentschlagwort = em.find(Dokumentschlagwort.class, iId);
		if (dokumentschlagwort == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");

		}
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }
		setDokumentschlagwortFromDokumentschlagwortDto(dokumentschlagwort,
				dokumentschlagwortDto);
	}

	public void updateDokumentschlagworts(
			DokumentschlagwortDto[] dokumentschlagwortDtos)
			throws EJBExceptionLP {
		if (dokumentschlagwortDtos != null) {
			for (int i = 0; i < dokumentschlagwortDtos.length; i++) {
				updateDokumentschlagwort(dokumentschlagwortDtos[i]);
			}
		}
	}

	public DokumentschlagwortDto dokumentschlagwortFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iId == null"));
		}

		// try {
		Dokumentschlagwort dokumentschlagwort = em.find(
				Dokumentschlagwort.class, iId);
		if (dokumentschlagwort == null) { // @ToDo null Pruefung?
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
		}
		return assembleDokumentschlagwortDto(dokumentschlagwort);

		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	public DokumentschlagwortDto[] dokumentschlagwortFindByDokumentIId(
			Integer dokumentIId) throws EJBExceptionLP {
		if (dokumentIId == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception("dokumentIId == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("DokumentschlagwortfindByDokumentIId");
		query.setParameter(1, dokumentIId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		// }
		return assembleDokumentschlagwortDtos(cl);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	private void setDokumentschlagwortFromDokumentschlagwortDto(
			Dokumentschlagwort dokumentschlagwort,
			DokumentschlagwortDto dokumentschlagwortDto) {
		dokumentschlagwort.setDokumentIId(dokumentschlagwortDto
				.getDokumentIId());
		dokumentschlagwort.setCSchlagwort(dokumentschlagwortDto
				.getCSchlagwort());
		em.merge(dokumentschlagwort);
		em.flush();
	}

	private DokumentschlagwortDto assembleDokumentschlagwortDto(
			Dokumentschlagwort dokumentschlagwort) {
		return DokumentschlagwortDtoAssembler.createDto(dokumentschlagwort);
	}

	private DokumentschlagwortDto[] assembleDokumentschlagwortDtos(
			Collection<?> dokumentschlagworts) {
		List<DokumentschlagwortDto> list = new ArrayList<DokumentschlagwortDto>();
		if (dokumentschlagworts != null) {
			Iterator<?> iterator = dokumentschlagworts.iterator();
			while (iterator.hasNext()) {
				Dokumentschlagwort dokumentschlagwort = (Dokumentschlagwort) iterator
						.next();
				list.add(assembleDokumentschlagwortDto(dokumentschlagwort));
			}
		}
		DokumentschlagwortDto[] returnArray = new DokumentschlagwortDto[list
				.size()];
		return (DokumentschlagwortDto[]) list.toArray(returnArray);
	}

	public Integer createBelegartdokument(
			BelegartdokumentDto belegartdokumentDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (belegartdokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("belegartdokumentDto == null"));
		}

		if (belegartdokumentDto.getIBelegartid() == null
				|| belegartdokumentDto.getBelegartCNr() == null
				|| belegartdokumentDto.getDokumentDto() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"belegartdokumentDto.getIBelegartid() == null  ||  belegartdokumentDto.getBelegartCNr() == null  ||  belegartdokumentDto.getDokumentDto() == null"));
		}

		Integer dokumentIId = createDokument(belegartdokumentDto
				.getDokumentDto(), theClientDto);
		belegartdokumentDto.setDokumentIId(dokumentIId);

		// generieren von primary key
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_BELEGARTDOKUMENT);
		belegartdokumentDto.setIId(pk);
		Query query = em.createNamedQuery("BelegartdokumentejbSelectMaxISort");
		query.setParameter(1, belegartdokumentDto.getBelegartCNr());
		query.setParameter(2, belegartdokumentDto.getIBelegartid());

		Integer maxIsort = (Integer) query.getSingleResult();
		if (maxIsort == null) {
			maxIsort = new Integer(0);
		}
		belegartdokumentDto.setISort(maxIsort + 1);

		belegartdokumentDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
		belegartdokumentDto.setTAnlegen(new Timestamp(System
				.currentTimeMillis()));

		try {

			Belegartdokument belegartdokument = new Belegartdokument(
					belegartdokumentDto.getIId(), belegartdokumentDto
							.getBelegartCNr(), belegartdokumentDto
							.getIBelegartid(), belegartdokumentDto
							.getDokumentIId(), belegartdokumentDto.getISort(),
					belegartdokumentDto.getPersonalIIdAnlegen());
			em.persist(belegartdokument);
			em.flush();
			setBelegartdokumentFromBelegartdokumentDto(belegartdokument,
					belegartdokumentDto);
			return belegartdokumentDto.getIId();
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}
	}

	public void removeBelegartdokument(Integer iId) throws EJBExceptionLP {
		try {
			Belegartdokument toRemove = em.find(Belegartdokument.class, iId);
			if (toRemove == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");
			}
			try {
				em.remove(toRemove);
				em.flush();
			} catch (EntityExistsException er) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN,
						er);
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, e);
		}
	}

	public void removeBelegartdokument(BelegartdokumentDto belegartdokumentDto)
			throws EJBExceptionLP {
		if (belegartdokumentDto != null) {
			Integer iId = belegartdokumentDto.getIId();
			removeBelegartdokument(iId);
		}
	}

	public void updateBelegartdokument(BelegartdokumentDto belegartdokumentDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (belegartdokumentDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("belegartdokumentDto == null"));
		}
		if (belegartdokumentDto.getIId() == null
				|| belegartdokumentDto.getDokumentDto() == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_IN_DTO_IS_NULL,
					new Exception(
							"belegartdokumentDto.getIId() == null || belegartdokumentDto.getDokumentDto() == null"));
		}

		Integer iId = belegartdokumentDto.getIId();
		// try {
		Belegartdokument belegartdokument = em
				.find(Belegartdokument.class, iId);
		if (belegartdokument == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,"");

		}
		setBelegartdokumentFromBelegartdokumentDto(belegartdokument,
				belegartdokumentDto);
		updateDokument(belegartdokumentDto.getDokumentDto(), theClientDto);
		// }
		// catch (FinderException ex1) {
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex1);

		// }

	}

	public BelegartdokumentDto belegartdokumentFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		// try {
		Belegartdokument belegartdokument = em
				.find(Belegartdokument.class, iId);
		if (belegartdokument == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND,"");
		}
		BelegartdokumentDto belegartdokumentDto = assembleBelegartdokumentDto(belegartdokument);
		belegartdokumentDto
				.setDokumentDto(dokumentFindByPrimaryKey(belegartdokumentDto
						.getDokumentIId()));
		return belegartdokumentDto;
		// }
		// catch (FinderException fe) {
		// throw fe;
		// }

	}
	
	public BelegartdokumentDto[] belegartdokumentFindAll(){
		Query query = em.createNamedQuery("BelegartdokumentfindAll");
		Collection<?> cl = query.getResultList();
		BelegartdokumentDto[] belegartdokumentDto = assembleBelegartdokumentDtos(cl);
		return belegartdokumentDto;
	}

	public BelegartdokumentDto[] belegartdokumentFindByBelegartCNrBelegartId(
			String sBelegartCNr, Integer iBelegartId) throws EJBExceptionLP {
		if (iBelegartId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iBelegartId == null"));
		}
		if (sBelegartCNr == null
				|| (sBelegartCNr != null && sBelegartCNr.trim().length() == 0)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("sBelegartCNr == null"));
		}

		// try {
		Query query = em
				.createNamedQuery("BelegartdokumentfindByBelegartCNrIBelegartid");
		query.setParameter(1, sBelegartCNr);
		query.setParameter(2, iBelegartId);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		BelegartdokumentDto[] aBelegartdokumentDtos = assembleBelegartdokumentDtos(cl);

		return aBelegartdokumentDtos;
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public BelegartdokumentDto[] belegartdokumentFindByBelegartCNrBelegartIdOhneExc(
			String sBelegartCNr, Integer iBelegartId) {
		if (iBelegartId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("iBelegartId == null"));
		}
		if (sBelegartCNr == null
				|| (sBelegartCNr != null && sBelegartCNr.trim().length() == 0)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception("sBelegartCNr == null"));
		}
		Query query = em
				.createNamedQuery("BelegartdokumentfindByBelegartCNrIBelegartid");
		query.setParameter(1, sBelegartCNr);
		query.setParameter(2, iBelegartId);
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// return null;
		// }
		BelegartdokumentDto[] aBelegartdokumentDtos = assembleBelegartdokumentDtos(cl);
		return aBelegartdokumentDtos;

	}

	private void setBelegartdokumentFromBelegartdokumentDto(
			Belegartdokument belegartdokument,
			BelegartdokumentDto belegartdokumentDto) {
		belegartdokument.setBelegartCNr(belegartdokumentDto.getBelegartCNr());
		belegartdokument.setIBelegartid(belegartdokumentDto.getIBelegartid());
		belegartdokument.setDokumentIId(belegartdokumentDto.getDokumentIId());
		belegartdokument.setISort(belegartdokumentDto.getISort());
		belegartdokument.setPersonalIIdAnlegen(belegartdokumentDto
				.getPersonalIIdAnlegen());
		belegartdokument.setTAnlegen(belegartdokumentDto.getTAnlegen());
		em.merge(belegartdokument);
		em.flush();
	}

	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			String belegartCNr, Integer iBelegartid,
			int iSortierungNeuePositionI) throws EJBExceptionLP {
		// try {
		Query query = em
				.createNamedQuery("BelegartdokumentfindByBelegartCNrIBelegartid");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegartid);
		Collection<?> cl = query.getResultList();
		// if (cl.isEmpty()) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, null);
		// }
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Belegartdokument oBelegartdokument = (Belegartdokument) it.next();

			if (oBelegartdokument.getISort().intValue() >= iSortierungNeuePositionI) {
				//iSortierungNeuePositionI++;
				oBelegartdokument
						.setISort(new Integer(oBelegartdokument.getISort().intValue()+1));
				em.merge(oBelegartdokument);
				em.flush();
			}
		}
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		// }
	}

	public void vertauscheBelegartDokument(Integer iId1, Integer iId2)
			throws EJBExceptionLP {
		final String METHOD_NAME = "vertauscheVkpfartikelpreisliste";
		myLogger.entry();

		// try {
		Belegartdokument oBelegartDokument1 = em.find(Belegartdokument.class,
				iId1);
		if (oBelegartDokument1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER,"");
		}

		Belegartdokument oBelegartDokument2 = em.find(Belegartdokument.class,
				iId2);

		Integer iSort1 = oBelegartDokument1.getISort();
		Integer iSort2 = oBelegartDokument2.getISort();

		oBelegartDokument2.setISort(new Integer(-1));

		oBelegartDokument1.setISort(iSort2);
		oBelegartDokument2.setISort(iSort1);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
		// }
	}

	/**
	 * Ersetzt bei Belegartdokumenten eine bestimmte BelegartId durch eine
	 * andere
	 * 
	 * @param cNrBelegart
	 *            String
	 * @param iBelegartIdOld
	 *            Integer
	 * @param iBelegartIdNew
	 *            Integer
	 * @param theClientDto
	 *            TheClientDto
	 * @throws EJBExceptionLP
	 */
	public void vertauscheBelegartIdBeiBelegartdokumenten(String cNrBelegart,
			Integer iBelegartIdOld, Integer iBelegartIdNew, TheClientDto theClientDto)
			throws EJBExceptionLP {

		BelegartdokumentDto[] aBelegartdokumentDtos = belegartdokumentFindByBelegartCNrBelegartIdOhneExc(
				cNrBelegart, iBelegartIdOld);
		if (aBelegartdokumentDtos != null) {
			for (int i = 0; i < aBelegartdokumentDtos.length; i++) {
				DokumentDto dokumentDto = this
						.dokumentFindByPrimaryKey(aBelegartdokumentDtos[i]
								.getDokumentIId());
				aBelegartdokumentDtos[i].setDokumentDto(dokumentDto);
				aBelegartdokumentDtos[i].setIBelegartid(iBelegartIdNew);
				updateBelegartdokument(aBelegartdokumentDtos[i], theClientDto);
			}
		}

	}

	/**
	 * Wenn eine Preisliste geloescht wurde, dann muss die Sortierung angepasst
	 * werden, damit keine Luecken entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param belegartCNr
	 *            String
	 * @param iBelegartid
	 *            Integer
	 * @param iSortierungGeloeschtePreislisteI
	 *            die Position der geloschten Position
	 * @throws Throwable
	 *             Ausnahme
	 */
	private void sortierungAnpassenBeiLoeschenEinerPreisliste(
			String belegartCNr, Integer iBelegartid,
			int iSortierungGeloeschtePreislisteI) throws Throwable {
		Query query = em
				.createNamedQuery("BelegartdokumentfindByBelegartCNrIBelegartid");
		query.setParameter(1, belegartCNr);
		query.setParameter(2, iBelegartid);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();

		while (it.hasNext()) {
			Belegartdokument oBelegartdokument = (Belegartdokument) it.next();

			if (oBelegartdokument.getISort().intValue() > iSortierungGeloeschtePreislisteI) {
				oBelegartdokument.setISort(new Integer(
						iSortierungGeloeschtePreislisteI));
				iSortierungGeloeschtePreislisteI++;
			}
		}
	}

	private BelegartdokumentDto assembleBelegartdokumentDto(
			Belegartdokument belegartdokument) {
		return BelegartdokumentDtoAssembler.createDto(belegartdokument);
	}

	private BelegartdokumentDto[] assembleBelegartdokumentDtos(
			Collection<?> belegartdokuments) {
		List<BelegartdokumentDto> list = new ArrayList<BelegartdokumentDto>();
		if (belegartdokuments != null) {
			Iterator<?> iterator = belegartdokuments.iterator();
			while (iterator.hasNext()) {
				Belegartdokument belegartdokument = (Belegartdokument) iterator
						.next();
				list.add(assembleBelegartdokumentDto(belegartdokument));
			}
		}
		BelegartdokumentDto[] returnArray = new BelegartdokumentDto[list.size()];
		return (BelegartdokumentDto[]) list.toArray(returnArray);
	}
}
