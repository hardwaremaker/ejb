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

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections4.map.HashedMap;

import com.lp.server.system.assembler.EditorContentDtoAssembler;
import com.lp.server.system.ejb.Datenformat;
import com.lp.server.system.ejb.EditorBaseBlock;
import com.lp.server.system.ejb.EditorContent;
import com.lp.server.system.ejb.HvImage;
import com.lp.server.system.ejb.Mediaart;
import com.lp.server.system.ejb.Mediaartspr;
import com.lp.server.system.ejb.MediaartsprPK;
import com.lp.server.system.ejb.Mediastandard;
import com.lp.server.system.ejb.MediastandardQuery;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.service.DatenformatDto;
import com.lp.server.system.service.DatenformatDtoAssembler;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorContentDto;
import com.lp.server.system.service.HvImageDto;
import com.lp.server.system.service.HvImageDtoAssembler;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediaartDto;
import com.lp.server.system.service.MediaartDtoAssembler;
import com.lp.server.system.service.MediaartsprDto;
import com.lp.server.system.service.MediaartsprDtoAssembler;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.MediastandardDtoAssembler;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.EditorBlockIId;
import com.lp.server.util.EditorContentIId;
import com.lp.server.util.Facade;
import com.lp.server.util.HvImageIId;
import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class MediaFacBean extends Facade implements MediaFac {
	@PersistenceContext
	private EntityManager em;

	public void createDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// logg: wenn der PK bereits bekannt ist: xyDto
		myLogger.logData(datenformatDto);
		if (datenformatDto == null) {
			return;
		}
		try {
			Datenformat datenformat = new Datenformat(datenformatDto.getCNr(), datenformatDto.getISort());
			em.persist(datenformat);
			em.flush();
			setDatenformatFromDatenformatDto(datenformat, datenformatDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public void removeDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// logg: removeXY: mit xyDto.toString()
		myLogger.logData(datenformatDto);
		if (datenformatDto != null) {
			String cNr = datenformatDto.getCNr();
			Datenformat toRemove = em.find(Datenformat.class, cNr);
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

	}

	public void updateDatenformat(DatenformatDto datenformatDto, TheClientDto theClientDto) throws EJBExceptionLP {
		// logg: updateXY: mit xyDto.toString()
		myLogger.logData(datenformatDto);

		if (datenformatDto != null) {
			String cNr = datenformatDto.getCNr();
			// try {
			Datenformat datenformat = em.find(Datenformat.class, cNr);
			if (datenformat == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setDatenformatFromDatenformatDto(datenformat, datenformatDto);
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		}
	}

	public DatenformatDto datenformatFindByPrimaryKey(String cNr) throws EJBExceptionLP {
		// logg: xyFindByPrimaryKey loggt nicht
		// try {
		Datenformat datenformat = em.find(Datenformat.class, cNr);
		if (datenformat == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleDatenformatDto(datenformat);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	private void setDatenformatFromDatenformatDto(Datenformat datenformat, DatenformatDto datenformatDto) {
		datenformat.setISort(datenformatDto.getISort());
		datenformat.setCBez(datenformatDto.getCBez());
		em.merge(datenformat);
		em.flush();
	}

	private DatenformatDto assembleDatenformatDto(Datenformat datenformat) {
		return DatenformatDtoAssembler.createDto(datenformat);
	}

	private DatenformatDto[] assembleDatenformatDtos(Collection<?> datenformats) {
		List<DatenformatDto> list = new ArrayList<DatenformatDto>();
		if (datenformats != null) {
			Iterator<?> iterator = datenformats.iterator();
			while (iterator.hasNext()) {
				Datenformat datenformat = (Datenformat) iterator.next();
				list.add(assembleDatenformatDto(datenformat));
			}
		}
		DatenformatDto[] returnArray = new DatenformatDto[list.size()];
		return (DatenformatDto[]) list.toArray(returnArray);
	}

	public MediastandardDto createMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		mediastandardDto.setMandantCNr(theClientDto.getMandant());

		if (mediastandardDto.getLocaleCNr() == null) {
			mediastandardDto.setLocaleCNr(theClientDto.getLocUiAsString());
		}

		if (mediastandardDto.getBVersteckt() == null) {
			mediastandardDto.setBVersteckt(Helper.getShortFalse());
		}

		try {
			Integer iId = getPKGeneratorObj().getNextPrimaryKey(PKConst.PK_MEDIASTANDARD);
			mediastandardDto.setIId(iId);

			Timestamp now = getTimestamp();
			mediastandardDto.setTAendern(now);
			mediastandardDto.setTAnlegen(now);
			mediastandardDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mediastandardDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			Mediastandard mediastandard = new Mediastandard(mediastandardDto.getIId(), mediastandardDto.getCNr(),
					mediastandardDto.getOMediaImage(), mediastandardDto.getDatenformatCNr(),
					mediastandardDto.getCDateiname(), mediastandardDto.getPersonalIIdAnlegen(),
					mediastandardDto.getTAnlegen(), mediastandardDto.getPersonalIIdAendern(),
					mediastandardDto.getTAendern(), mediastandardDto.getMandantCNr(), mediastandardDto.getLocaleCNr(),
					mediastandardDto.getBVersteckt());
			em.persist(mediastandard);
			em.flush();
			setMediastandardFromMediastandardDto(mediastandard, mediastandardDto);
			return mediastandardDto;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}
	}

	public void removeMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		myLogger.logData(mediastandardDto);

		if (mediastandardDto != null) {
			Mediastandard toRemove = em.find(Mediastandard.class, mediastandardDto.getIId());
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

	}

	public MediastandardDto updateMediastandard(MediastandardDto mediastandardDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// logg: updateXY der PK wird geloggt
		myLogger.logData(mediastandardDto);

		if (mediastandardDto != null) {
			// try {
			mediastandardDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			mediastandardDto.setTAendern(super.getTimestamp());
			Mediastandard mediastandard = em.find(Mediastandard.class, mediastandardDto.getIId());
			if (mediastandard == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			setMediastandardFromMediastandardDto(mediastandard, mediastandardDto);
			return mediastandardDto;
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
			// }

		} else {
			return null;
		}
	}

	/**
	 * @deprecated MB: use createDefaultMediastandard(String cNrMediaartI, String
	 *             localeCNr, String cNrUserI)
	 * 
	 *             Einen Default Mediastandard anlegen.
	 * @param cNrMediaartI Mediaart Eigentumsvorbehalt oder Lieferbedingungen
	 * @param theClientDto String der aktuelle Benutzer
	 * @return MediastandardDto Mediastandard
	 * @throws EJBExceptionLP Ausnahme
	 */
	public MediastandardDto createDefaultMediastandard(String cNrMediaartI, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (cNrMediaartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cNrMediaartI == null"));
		}

		myLogger.logData(cNrMediaartI);

		MediastandardDto mediastandardDto = new MediastandardDto();
		mediastandardDto.setCNr(cNrMediaartI);

		String text = null;

		if (cNrMediaartI.equals(MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT)) {
			text = MediaFac.DEFAULT_EIGENTUMSVORBEHALT;
		} else if (cNrMediaartI.equals(MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN)) {
			text = MediaFac.DEFAULT_LIEFERBEDINGUNGEN;
		}

		mediastandardDto.setOMediaText(text);
		mediastandardDto.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);

		mediastandardDto = createMediastandard(mediastandardDto, theClientDto);

		return mediastandardDto;
	}

	/**
	 * @param cNrMediaartI Mediaart Eigentumsvorbehalt oder Lieferbedingungen
	 * @param localeCNr    String
	 * @param theClientDto der aktuelle Benutzer
	 * @return MediastandardDto Mediastandard
	 * @throws EJBExceptionLP Ausnahme
	 */
	public MediastandardDto createDefaultMediastandard(String cNrMediaartI, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (cNrMediaartI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cNrMediaartI == null"));
		}

		myLogger.logData(cNrMediaartI);

		MediastandardDto mediastandardDto = new MediastandardDto();
		mediastandardDto.setCNr(cNrMediaartI);
		mediastandardDto.setLocaleCNr(localeCNr);

		String text = null;

		if (cNrMediaartI.equals(MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT)) {
			text = MediaFac.DEFAULT_EIGENTUMSVORBEHALT;
		} else if (cNrMediaartI.equals(MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN)) {
			text = MediaFac.DEFAULT_LIEFERBEDINGUNGEN;
		}

		mediastandardDto.setOMediaText(text);
		mediastandardDto.setDatenformatCNr(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);

		mediastandardDto = createMediastandard(mediastandardDto, theClientDto);

		return mediastandardDto;
	}

	public MediastandardDto[] mediastandardFindByDatenformatCNrMandantCNr(String datenformatCNrI, String mandantCNrI,
			TheClientDto theClientDto) {

		// try {
		Query query = em.createNamedQuery("MediastandardfindByDatenformatCNrMandantCNr");
		query.setParameter(1, datenformatCNrI);
		query.setParameter(2, mandantCNrI);

		return assembleMediastandardDtos(query.getResultList());

	}

	public String mediastandardTextHtmlFindByCNrMandantCNrLocale(String cNrI, String mandantCNrI, Locale locale) {

		// try {
		Query query = em.createNamedQuery("MediastandardfindByCNrDatenformatCNrMandantCNrLocaleCNr");
		query.setParameter(1, cNrI);
		query.setParameter(2, MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
		query.setParameter(3, mandantCNrI);
		query.setParameter(4, Helper.locale2String(locale));
		Mediastandard mediastandard = null;
		try {
			mediastandard = (Mediastandard) query.getSingleResult();

			return new String(mediastandard.getOMedia(), "UTF8");
		} catch (UnsupportedEncodingException e) {
			// nothing here
			return null;
		} catch (NoResultException e) {
			// nothing here
			return null;
		}

	}

	public MediastandardDto mediastandardFindByCNrDatenformatCNrMandantCNr(String cNrI, String datenformatCNrI,
			String mandantCNrI, String localeCNrI, TheClientDto theClientDto) throws EJBExceptionLP {

		if (cNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("cNrI == null"));
		}

		if (datenformatCNrI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("datenformatCNrI == null"));
		}

		myLogger.info("CNr: " + cNrI + ", datenformatCNr: " + datenformatCNrI);

		MediastandardDto mediastandardDto = null;

		// try {
		Query query = em.createNamedQuery("MediastandardfindByCNrDatenformatCNrMandantCNrLocaleCNr");
		query.setParameter(1, cNrI);
		query.setParameter(2, datenformatCNrI);
		query.setParameter(3, mandantCNrI);
		query.setParameter(4, localeCNrI);
		Mediastandard mediastandard = null;
		try {
			mediastandard = (Mediastandard) query.getSingleResult();
		} catch (NoResultException e) {
			// nothing here
		}
		if (mediastandard == null) {
			if (cNrI.equals(MediaFac.MEDIASTANDARD_EIGENTUMSVORBEHALT)) {
				mediastandardDto = createDefaultMediastandard(MediaFac.MEDIAART_EIGENTUMSVORBEHALT, localeCNrI,
						theClientDto);
			} else if (cNrI.equals(MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN)) {
				mediastandardDto = createDefaultMediastandard(MediaFac.MEDIASTANDARD_LIEFERBEDINGUNGEN, localeCNrI,
						theClientDto);
			} else {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
			}
		} else {
			mediastandardDto = assembleMediastandardDto(mediastandard);
		}
		return mediastandardDto;
	}

	@Override
	public MediastandardDto mediastandardFindByCnrMandantCNr(String cnr, String mandantCnr, HvOptional<String> locale,
			TheClientDto theClientDto) throws EJBExceptionLP {
		HvOptional<MediastandardDto> dto = mediastandardOptFindByCnrMandantCnr(cnr, mandantCnr, locale, theClientDto);
		if (!dto.isPresent()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, cnr);
		}

		return dto.get();
	}

	@Override
	public HvOptional<MediastandardDto> mediastandardOptFindByCnrMandantCnr(String cnr, String mandantCnr,
			HvOptional<String> locale, TheClientDto theClientDto) {
		Validator.notEmpty(cnr, "cnr");
		Validator.notEmpty(mandantCnr, "mandantCnr");

		MediastandardQuery q = new MediastandardQuery(em);
		HvOptional<Mediastandard> media = q.find(mandantCnr, cnr, locale.orElse(theClientDto.getLocUiAsString()));
		if (media.isPresent()) {
			return HvOptional.of(assembleMediastandardDto(media.get()));
		}

		return HvOptional.empty();
	}

	@Override
	public MediastandardDto mediastandardFindByPrimaryKey(Integer mediastandardIId) throws EJBExceptionLP {
		HvOptional<MediastandardDto> dto = mediastandardOptFindByPrimaryKey(mediastandardIId);
		return Validator.entityFound(dto, mediastandardIId);
	}

	@Override
	public HvOptional<MediastandardDto> mediastandardOptFindByPrimaryKey(Integer mediastandardIId) {
		Validator.pkFieldNotNull(mediastandardIId, "mediastandardIId");

		Mediastandard mediastandard = em.find(Mediastandard.class, mediastandardIId);
		if (mediastandard == null)
			return HvOptional.empty();

		return HvOptional.of(assembleMediastandardDto(mediastandard));
	}

	private void setMediastandardFromMediastandardDto(Mediastandard mediastandard, MediastandardDto mediastandardDto) {
		mediastandard.setCNr(mediastandardDto.getCNr());
		mediastandard.setOMedia(mediastandardDto.getOMediaImage());
		mediastandard.setDatenformatCNr(mediastandardDto.getDatenformatCNr());
		mediastandard.setPersonalIIdAnlegen(mediastandardDto.getPersonalIIdAnlegen());
		mediastandard.setTAnlegen(mediastandardDto.getTAnlegen());
		mediastandard.setPersonalIIdAendern(mediastandardDto.getPersonalIIdAendern());
		mediastandard.setTAendern(mediastandardDto.getTAendern());
		mediastandard.setCDateiname(mediastandardDto.getCDateiname());
		mediastandard.setMandantCNr(mediastandardDto.getMandantCNr());
		mediastandard.setBVersteckt(mediastandardDto.getBVersteckt());
		em.merge(mediastandard);
		em.flush();
	}

	private MediastandardDto assembleMediastandardDto(Mediastandard mediastandard) {
		return MediastandardDtoAssembler.createDto(mediastandard);
	}

	private MediastandardDto[] assembleMediastandardDtos(Collection<?> mediastandards) {
		List<MediastandardDto> list = new ArrayList<MediastandardDto>();
		if (mediastandards != null) {
			Iterator<?> iterator = mediastandards.iterator();
			while (iterator.hasNext()) {
				Mediastandard mediastandard = (Mediastandard) iterator.next();
				list.add(assembleMediastandardDto(mediastandard));
			}
		}
		MediastandardDto[] returnArray = new MediastandardDto[list.size()];
		return (MediastandardDto[]) list.toArray(returnArray);
	}

	public DatenformatDto[] eingangsrechnungartFindAll() throws EJBExceptionLP {
		Query query = em.createNamedQuery("EingangsrechnungartfindAll");
		Collection<?> cl = query.getResultList();
		// if(cl.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDALL,"");
		// }
		return assembleDatenformatDtos(cl);
	}

	/**
	 * Hole alle ER-Arten nach Spr.
	 * 
	 * @param cNrSpracheI String
	 * @throws EJBExceptionLP
	 * @return Map
	 */
	public Map getAllDatenformat(String cNrSpracheI) throws EJBExceptionLP {
		LinkedHashMap<Object, Object> tmArten = new LinkedHashMap<Object, Object>();
		// try {
		Query query = em.createNamedQuery("DatenformatfindAll");

		Collection<?> clArten = query.getResultList();
		// if(clArten.isEmpty()){
		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY,"");
		// }

		Iterator<?> itArten = clArten.iterator();
		while (itArten.hasNext()) {
			Datenformat datenformatTemp = (Datenformat) itArten.next();
			Object key = datenformatTemp.getCNr();
			Object value = datenformatTemp.getCBez();
			tmArten.put(key, value);
		}
		// }
		// catch (FinderException ex) {

		// throw new EJBExceptionLP(EJBExceptionLP.
		// FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		// }
		return tmArten;
	}

	// Mediaart
	// ------------------------------------------------------------------

	public String createMediaart(MediaartDto oMediaartDtoI, TheClientDto theClientDto) throws EJBExceptionLP {
		checkMediaartDto(oMediaartDtoI);

		Mediaart mediaart = null;

		try {
			mediaart = new Mediaart(oMediaartDtoI.getCNr());
			em.persist(mediaart);
			em.flush();

			if (oMediaartDtoI.getMediaartsprDto() != null) {
				oMediaartDtoI.getMediaartsprDto().setMediaartCNr(oMediaartDtoI.getCNr());
				createMediaartspr(oMediaartDtoI.getMediaartsprDto(), theClientDto);
			}
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return mediaart.getCNr();
	}

	public void removeMediaart(MediaartDto mediaartDto, TheClientDto theClientDto) throws EJBExceptionLP {
		checkMediaartDto(mediaartDto);

		try {
			Query query = em.createNamedQuery("MediaartsprfindByMediaartCNr");
			query.setParameter(1, mediaartDto.getCNr());
			Collection<?> cl = query.getResultList();
			Mediaart mediaart = em.find(Mediaart.class, mediaartDto.getCNr());
			if (mediaart == null) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
			}
			// Erst alle SPRs dazu loeschen.
			for (Iterator<?> iter = cl.iterator(); iter.hasNext();) {
				Mediaartspr item = (Mediaartspr) iter.next();
				em.remove(item);
			}

			em.remove(mediaart);
			em.flush();
			// }
			// catch (FinderException ex) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		}
	}

	public void updateMediaart(MediaartDto mediaartDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		checkMediaartDto(mediaartDto);

		String cNr = mediaartDto.getCNr();

		// try {
		Mediaart mediaart = em.find(Mediaart.class, cNr);
		if (mediaart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		if (mediaartDto.getMediaartsprDto() != null) {
			mediaartDto.getMediaartsprDto().setLocaleCNr(theClientDto.getLocUiAsString());

			// create
			if (mediaartDto.getMediaartsprDto().getMediaartCNr() == null) {
				// zuerst Key setzen
				mediaartDto.getMediaartsprDto().setMediaartCNr(mediaartDto.getCNr());

				createMediaartspr(mediaartDto.getMediaartsprDto(), theClientDto);
			}

			// update
			else {
				updateMediaartspr(mediaartDto.getMediaartsprDto(), theClientDto);
			}
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public MediaartDto mediaartFindByPrimaryKey(String cNr, TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.logData(cNr);

		if (cNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("cNr == null"));
		}

		MediaartDto mediaartDtoO = null;

		// try {
		Mediaart mediaart = em.find(Mediaart.class, cNr);
		if (mediaart == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		mediaartDtoO = assembleMediaartDto(mediaart);

		try {
			Mediaartspr mediaartspr = em.find(Mediaartspr.class,
					new MediaartsprPK(mediaartDtoO.getCNr(), theClientDto.getLocUiAsString()));
			mediaartDtoO.setMediaartsprDto(assembleMediaartsprDto(mediaartspr));
		} catch (Throwable t) {
			// nothing here.
		}
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return mediaartDtoO;
	}

	private MediaartDto assembleMediaartDto(Mediaart mediaart) {
		return MediaartDtoAssembler.createDto(mediaart);
	}

	private MediaartDto[] assembleMediaartDtos(Collection<?> mediaarts) {
		List<MediaartDto> list = new ArrayList<MediaartDto>();
		if (mediaarts != null) {
			Iterator<?> iterator = mediaarts.iterator();
			while (iterator.hasNext()) {
				Mediaart mediaart = (Mediaart) iterator.next();
				list.add(assembleMediaartDto(mediaart));
			}
		}
		MediaartDto[] returnArray = new MediaartDto[list.size()];
		return (MediaartDto[]) list.toArray(returnArray);
	}

	private void checkMediaartDto(MediaartDto oMediaartDtoI) throws EJBExceptionLP {
		if (oMediaartDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("oMediaartDtoI == null"));
		}
	}

	// Mediaartspr
	// ---------------------------------------------------------------

	public String createMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {

		checkMediaartsprDto(mediaartsprDto);

		Mediaartspr mediaartspr = null;

		try {
			mediaartspr = new Mediaartspr(mediaartsprDto.getMediaartCNr(), mediaartsprDto.getLocaleCNr());
			em.persist(mediaartspr);
			em.flush();
			setMediaartsprFromMediaartsprDto(mediaartspr, mediaartsprDto);
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return mediaartspr.getPk().getMediaartCNr();
	}

	public void removeMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {

		checkMediaartsprDto(mediaartsprDto);

		MediaartsprPK mediaartsprPK = new MediaartsprPK();
		mediaartsprPK.setMediaartCNr(mediaartsprDto.getMediaartCNr());
		mediaartsprPK.setLocaleCNr(mediaartsprDto.getLocaleCNr());

		Mediaartspr toRemove = em.find(Mediaartspr.class, mediaartsprPK);
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

	public void updateMediaartspr(MediaartsprDto mediaartsprDto, TheClientDto theClientDto) throws EJBExceptionLP {
		checkMediaartsprDto(mediaartsprDto);

		MediaartsprPK mediaartsprPK = new MediaartsprPK();
		mediaartsprPK.setMediaartCNr(mediaartsprDto.getMediaartCNr());
		mediaartsprPK.setLocaleCNr(mediaartsprDto.getLocaleCNr());

		// try {
		Mediaartspr mediaartspr = em.find(Mediaartspr.class, mediaartsprPK);
		if (mediaartspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}

		setMediaartsprFromMediaartsprDto(mediaartspr, mediaartsprDto);
		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public MediaartsprDto mediaartsprFindByPrimaryKey(String mediaartCNr, String localeCNr, TheClientDto theClientDto)
			throws EJBExceptionLP {

		if (mediaartCNr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("mediaartCNr == null"));
		}

		// try {
		MediaartsprPK mediaartsprPK = new MediaartsprPK();
		mediaartsprPK.setMediaartCNr(mediaartCNr);
		mediaartsprPK.setLocaleCNr(localeCNr);
		Mediaartspr mediaartspr = em.find(Mediaartspr.class, mediaartsprPK);
		if (mediaartspr == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleMediaartsprDto(mediaartspr);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

	}

	public MediaartsprDto getMediaartspr(String cNrI, String sLocUiI, TheClientDto theClientDto) {

		MediaartsprDto mediaartsprDto = null;

		try {
			mediaartsprDto = mediaartsprFindByPrimaryKey(cNrI, sLocUiI, theClientDto);
		} catch (Exception ex) {
			// nothing here.
		}

		return mediaartsprDto;
	}

	private void setMediaartsprFromMediaartsprDto(Mediaartspr mediaartspr, MediaartsprDto mediaartsprDto) {
		mediaartspr.setCBez(mediaartsprDto.getCBez());
		em.merge(mediaartspr);
		em.flush();
	}

	private MediaartsprDto assembleMediaartsprDto(Mediaartspr mediaartspr) {
		return MediaartsprDtoAssembler.createDto(mediaartspr);
	}

	private MediaartsprDto[] assembleMediaartsprDtos(Collection<?> mediaartsprs) {
		List<MediaartsprDto> list = new ArrayList<MediaartsprDto>();
		if (mediaartsprs != null) {
			Iterator<?> iterator = mediaartsprs.iterator();
			while (iterator.hasNext()) {
				Mediaartspr mediaartspr = (Mediaartspr) iterator.next();
				list.add(assembleMediaartsprDto(mediaartspr));
			}
		}
		MediaartsprDto[] returnArray = new MediaartsprDto[list.size()];
		return (MediaartsprDto[]) list.toArray(returnArray);
	}

	private void checkMediaartsprDto(MediaartsprDto oMediaartsprDtoI) throws EJBExceptionLP {
		if (oMediaartsprDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("oMediaartsprDtoI == null"));
		}
	}

	@Override
	public HvOptional<EditorContentDto> editorContentFindByPrimaryKey(EditorContentIId id, TheClientDto theClientDto) {
		Objects.requireNonNull(id.id());
		Objects.requireNonNull(theClientDto);
		EditorContent content = em.find(EditorContent.class, id.id());
		if(content == null) {
			return HvOptional.empty();
		}
		return HvOptional.of(EditorContentDtoAssembler.assembleEditorContentDto(content));
	}

	@Override
	public EditorContentDto createEditorContent(EditorContentDto dto, TheClientDto theClientDto) {
		Objects.requireNonNull(dto);
		Objects.requireNonNull(theClientDto);
		if (dto.getId() != null && dto.getId().isValid()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception("ID im Dto bei create bereits gesetzt"));
		}
		EditorContent content = EditorContent.fromDto(dto);
		em.persist(content);
		return EditorContentDtoAssembler.assembleEditorContentDto(content);
	}

	@Override
	public EditorContentDto updateEditorContent(EditorContentDto dto, TheClientDto theClientDto) {
		Objects.requireNonNull(theClientDto);
		Validator.dtoNotNull(dto, "EditorContentDto");
		Objects.requireNonNull(dto.getId().id());
		EditorContent oldContent = em.find(EditorContent.class, dto.getId().id());
		//Map fuer Lookup
		Map<EditorBlockIId, EditorBaseBlock> oldBlockMap = new HashMap<>();
		for(EditorBaseBlock block : oldContent.getBlocks()) {
			oldBlockMap.put(block.getIid(), block);
		}
		EditorContent newContent = EditorContent.fromDto(dto);
		for(EditorBaseBlock block : newContent.getBlocks()) {
			if(!block.getIid().isValid()) {
				//Neuer Block
				em.persist(block);
			}
			else {
				block = em.merge(block);
			}
			oldBlockMap.remove(block.getIid());
		}
		em.merge(newContent);
		for(EditorBaseBlock orphan : oldBlockMap.values()) {
			em.remove(orphan);
		}
		em.flush();
		return EditorContentDtoAssembler.assembleEditorContentDto(newContent);
	}
	
	@Override
	public HvOptional<EditorBaseBlockDto> editorBlockFindByPrimaryKey(EditorBlockIId id, TheClientDto theClientDto) {
		EditorBaseBlock block = em.find(EditorBaseBlock.class, id.id());
		if(block == null)
			return HvOptional.empty();
		return HvOptional.of(block.toDto());
	}

	@Override
	public void deleteEditorContent(EditorContentIId id, TheClientDto theClientDto) {
		Objects.requireNonNull(theClientDto);
		EditorContent ref = em.getReference(EditorContent.class, id.id());
		if(ref != null)
			em.remove(ref);
	}

	@Override
	public void deleteEditorBlock(EditorBlockIId id, TheClientDto theClientDto) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(theClientDto);
		EditorBaseBlock blockRef = em.getReference(EditorBaseBlock.class, id.id());
		if(blockRef != null)
			em.remove(blockRef);
	}
	@Override
	public HvImageDto createHvImage(HvImageDto imageDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(imageDto, "image");
		Validator.dtoNotNull(theClientDto, "theClient");
		HvImage image = new HvImage();
		image.setImageData(imageDto.getImageBinaryData());
		image.setImageType(imageDto.getImageFormat());
		em.persist(image);
		em.flush();
		imageDto.setId(image.getId());
		return imageDto;
	}

	@Override
	public void updateHvImage(HvImageDto imageDto, TheClientDto theClientDto) {
		Validator.dtoNotNull(imageDto, "imageDto");
		Validator.pkFieldValid(imageDto.getId(), "imageDto.id");
		Validator.dtoNotNull(theClientDto, "theClientDto");

		HvImage image = em.find(HvImage.class, imageDto.getId().id());
		image.setImageType(imageDto.getImageFormat());
		image.setImageData(imageDto.getImageBinaryData());
		em.flush();
	}

	@Override
	public void deleteHvImage(HvImageIId imageIId, TheClientDto theClientDto) {
		Validator.pkFieldValid(imageIId, "imageDto.id");
		Validator.dtoNotNull(theClientDto, "theClientDto");

		HvImage ref = em.getReference(HvImage.class, imageIId.id());
		if(ref != null)
			em.remove(ref);
	}

	@Override
	public HvOptional<HvImageDto> hvImageFindByPrimaryKey(HvImageIId imageIId) {
		Validator.pkFieldValid(imageIId, "imageIId");
		HvImage img = em.find(HvImage.class, imageIId.id());
		if (img == null)
			return HvOptional.empty();
		else
			return HvOptional.of(HvImageDtoAssembler.assembleHvImageDto(img));
	}
}
