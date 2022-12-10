
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
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Belegartmedia;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.BelegartmediaDto;
import com.lp.server.system.service.BelegartmediaFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

@Stateless
public class BelegartmediaFacBean extends Facade implements BelegartmediaFac {
	@PersistenceContext
	private EntityManager em;

	public ArrayList<Integer> syncBelegartmediaDtos(Integer usecaseId, Integer iKey,
			ArrayList<BelegartmediaDto> belegartmediaDtos, TheClientDto theClientDto) {

		usecaseId = mapUsecaseId(usecaseId);

		Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKey");
		query.setParameter(1, usecaseId);
		query.setParameter(2, iKey);

		Collection c = query.getResultList();

		Iterator it = c.iterator();
		while (it.hasNext()) {

			Belegartmedia bm = (Belegartmedia) it.next();

			em.remove(bm);

		}

		for (int i = 0; i < belegartmediaDtos.size(); i++) {

			BelegartmediaDto bmDto = belegartmediaDtos.get(i);

			if (bmDto.getDatenformatCNr() != null
					&& (bmDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
							|| bmDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
							|| bmDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)
							|| bmDto.getDatenformatCNr().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF))) {

				if (bmDto.getOMedia() != null) {
					try {

						if (bmDto.getIAusrichtung() == null) {
							bmDto.setIAusrichtung(AUSRICHTUNG_LINKSBUENDIG);
						}

						bmDto.setIKey(iKey);
						bmDto.setUsecaseId(usecaseId);

						bmDto.setISort(i + 1);

						createBelegartmedia(bmDto, theClientDto);

					} catch (NoResultException ex) {

					}
				}
			}

		}

		return null;

	}

	public boolean sindMedienVorhanden(Integer usecaseId, Integer iKey, TheClientDto theClientDto) {

		usecaseId = mapUsecaseId(usecaseId);

		ArrayList<BelegartmediaDto> alBm = new ArrayList<BelegartmediaDto>();

		Query query = em.createNamedQuery("BelegartmediaCountByUsecaseIdIKey");
		query.setParameter(1, usecaseId);
		query.setParameter(2, iKey);

		Long l = (Long) query.getSingleResult();
		if (l != null && l > 0) {
			return true;
		} else {
			return false;
		}

	}

	public Integer anzahlDerVorhandenenMedien(Integer usecaseId, Integer iKey, TheClientDto theClientDto) {

		usecaseId = mapUsecaseId(usecaseId);

		ArrayList<BelegartmediaDto> alBm = new ArrayList<BelegartmediaDto>();

		Query query = em.createNamedQuery("BelegartmediaCountByUsecaseIdIKey");
		query.setParameter(1, usecaseId);
		query.setParameter(2, iKey);

		Long l = (Long) query.getSingleResult();
		if (l != null && l > 0) {
			return l.intValue();
		} else {
			return 0;
		}

	}

	public ArrayList<BelegartmediaDto> getBelegartMediaDtos(Integer usecaseId, Integer iKey,
			TheClientDto theClientDto) {

		usecaseId = mapUsecaseId(usecaseId);

		ArrayList<BelegartmediaDto> alBm = new ArrayList<BelegartmediaDto>();

		Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKey");
		query.setParameter(1, usecaseId);
		query.setParameter(2, iKey);

		Collection<Belegartmedia> c = query.getResultList();

		for (Belegartmedia bm : c) {
			BelegartmediaDto bmDto = new BelegartmediaDto();

			bmDto.setIId(bm.getIId());
			bmDto.setCBez(bm.getCBez());
			bmDto.setCDateiname(bm.getCDateiname());
			bmDto.setDatenformatCNr(bm.getDatenformatCNr());
			bmDto.setIAusrichtung(bm.getIAusrichtung());
			bmDto.setIKey(bm.getIKey());

			bmDto.setUsecaseId(bm.getUsecaseId());
			bmDto.setOMedia(bm.getOMedia());
			bmDto.setXText(bm.getXText());
			bmDto.setISort(bm.getISort());

			alBm.add(bmDto);
		}

		return alBm;

	}

	public void removeBelegartmedia(Integer usecaseId, Integer iKey, TheClientDto theClientDto) {

		usecaseId = mapUsecaseId(usecaseId);

		Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKey");
		query.setParameter(1, usecaseId);
		query.setParameter(2, iKey);

		Collection c = query.getResultList();

		Iterator it = c.iterator();
		while (it.hasNext()) {

			Belegartmedia bm = (Belegartmedia) it.next();

			em.remove(bm);

		}
	}

	public void kopiereBelegartmedia(Integer usecaseIdQuelle, Integer iKeyQuelle, Integer usecaseIdZiel,
			Integer iKeyZiel, TheClientDto theClientDto) {

		if (usecaseIdQuelle != null && iKeyQuelle != null && usecaseIdZiel != null && iKeyZiel != null) {

			
			usecaseIdQuelle = mapUsecaseId(usecaseIdQuelle);
			usecaseIdZiel = mapUsecaseId(usecaseIdZiel);
			
			Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKey");
			query.setParameter(1, usecaseIdQuelle);
			query.setParameter(2, iKeyQuelle);

			Collection<Belegartmedia> c = query.getResultList();

			if (c.size() > 0) {

				removeBelegartmedia(usecaseIdZiel, iKeyZiel, theClientDto);

				for (Belegartmedia bm : c) {

					Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_BELEGARTMEDIA);

					Belegartmedia belegartmedia = new Belegartmedia(iId, usecaseIdZiel, iKeyZiel, bm.getISort(),
							bm.getDatenformatCNr(), bm.getIAusrichtung());
					belegartmedia.setOMedia(bm.getOMedia());
					belegartmedia.setXText(bm.getXText());
					belegartmedia.setCBez(bm.getCBez());
					belegartmedia.setCDateiname(bm.getCDateiname());

					em.persist(belegartmedia);
					em.flush();

				}

			}

		}

	}

	private Integer mapUsecaseId(Integer usecaseId) {

		if (usecaseId == QueryParameters.UC_ID_TELEFONZEITENTODO
				|| usecaseId == QueryParameters.UC_ID_COCKPITTELEFONZEITEN) {
			usecaseId = QueryParameters.UC_ID_TELEFONZEITEN;
		}

		return usecaseId;
	}

	public Integer createBelegartmedia(BelegartmediaDto dto, TheClientDto theClientDto) {

		try {
			Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKeyISort");
			query.setParameter(1, dto.getUsecaseId());
			query.setParameter(2, dto.getIKey());
			query.setParameter(3, dto.getISort());

			Belegartmedia belegartmedia = (Belegartmedia) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_BELEGARTMEDIA.UK"));

		} catch (NoResultException ex) {

		}

		Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_BELEGARTMEDIA);
		dto.setIId(iId);

		try {

			// create
			Belegartmedia belegartmedia = new Belegartmedia(dto.getIId(), dto.getUsecaseId(), dto.getIKey(),
					dto.getISort(), dto.getDatenformatCNr(), dto.getIAusrichtung());

			belegartmedia.setOMedia(dto.getOMedia());
			belegartmedia.setXText(dto.getXText());

			belegartmedia.setCBez(dto.getCBez());

			belegartmedia.setCDateiname(dto.getCDateiname());

			em.persist(belegartmedia);
			em.flush();

			return dto.getIId();
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void updateBelegartmedia(BelegartmediaDto dto, TheClientDto theClientDto) {

		Integer iId = dto.getIId();

		try {
			Query query = em.createNamedQuery("BelegartmediaFindByUsecaseIdIKeyISort");
			query.setParameter(1, dto.getUsecaseId());
			query.setParameter(2, dto.getIKey());
			query.setParameter(3, dto.getISort());
			Integer iIdVorhanden = ((Belegartmedia) query.getSingleResult()).getIId();
			if (iId.equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception("LP_BELEGARTMEDIA.UK"));
			}

		} catch (NoResultException ex) {
			//
		}

	}

}
