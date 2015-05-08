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
package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.TransactionTimeout;

import com.lp.server.artikel.ejb.VkPreisfindungEinzelverkaufspreis;
import com.lp.server.artikel.ejb.VkPreisfindungPreisliste;
import com.lp.server.artikel.ejb.Vkpfartikelpreisliste;
import com.lp.server.artikel.ejb.Vkpfmengenstaffel;
import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.fastlanereader.generated.FLRVkpfStaffelmenge;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.VerkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDto;
import com.lp.server.artikel.service.VkPreisfindungEinzelverkaufspreisDtoAssembler;
import com.lp.server.artikel.service.VkPreisfindungFac;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDto;
import com.lp.server.artikel.service.VkPreisfindungPreislisteDtoAssembler;
import com.lp.server.artikel.service.VkpfMengenstaffelDto;
import com.lp.server.artikel.service.VkpfMengenstaffelDtoAssembler;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.VkpreisfindungDto;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundesokoDto;
import com.lp.server.partner.service.KundesokomengenstaffelDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VkpfartikelpreislisteDtoAssembler;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class VkPreisfindungFacBean extends Facade implements VkPreisfindungFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Neu anlegen einer Preisliste.
	 * 
	 * @param vkPreisfindungPreislisteDto
	 *            VkPreisfindungPreislisteDto
	 * @throws EJBExceptionLP
	 * @return Integer pk der preisliste
	 */
	public Integer createVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {

		checkPreislistePreconditions(vkPreisfindungPreislisteDto);

		// unique key constraint pruefen
		VkPreisfindungPreisliste preis = null;
		try {
			Query query = em
					.createNamedQuery("VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
			query.setParameter(1,
					vkPreisfindungPreislisteDto.getVkpfartikelpreislisteIId());
			query.setParameter(2, vkPreisfindungPreislisteDto.getArtikelIId());
			query.setParameter(3,
					vkPreisfindungPreislisteDto.getTPreisgueltigab());
			preis = (VkPreisfindungPreisliste) query.getSingleResult();
			// }
			// catch (ObjectNotFoundException ex) {
			// // continue
		} catch (NoResultException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		if (preis != null) {
			// constraint verletzt
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception());
		}

		Integer keyPreisliste = new Integer(-1);

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			keyPreisliste = pkGen.getNextPrimaryKey(PKConst.PK_PREISLISTE);

			vkPreisfindungPreislisteDto.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			vkPreisfindungPreislisteDto.setTAendern(new Timestamp(System
					.currentTimeMillis()));

			VkPreisfindungPreisliste vkPreisfindungPreisliste = new VkPreisfindungPreisliste(
					keyPreisliste,
					vkPreisfindungPreislisteDto.getVkpfartikelpreislisteIId(),
					vkPreisfindungPreislisteDto.getArtikelIId(),
					vkPreisfindungPreislisteDto.getTPreisgueltigab(),
					vkPreisfindungPreislisteDto.getNArtikelstandardrabattsatz(),
					vkPreisfindungPreislisteDto.getTAendern(),
					vkPreisfindungPreislisteDto.getPersonalIIdAendern());
			em.persist(vkPreisfindungPreisliste);
			em.flush();
			setVkPreisfindungPreislisteFromVkPreisfindungPreislisteDto(
					vkPreisfindungPreisliste, vkPreisfindungPreislisteDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return keyPreisliste;
	}

	/**
	 * Preconditions pruefen.
	 * 
	 * @param vkPreisfindungPreislisteDto
	 *            VkPreisfindungPreislisteDto
	 */
	private void checkPreislistePreconditions(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto) {
		if (vkPreisfindungPreislisteDto == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception());
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public void pflegeRabattsaetzeNachpflegen(Integer preislisteIId,
			Date tPreisgueltigab, TheClientDto theClientDto) {

		// String query =
		// "SELECT v FROM FLRVkpfartikelpreis v WHERE v.n_artikelfixpreis IS NULL AND v.n_artikelstandardrabattsatz IS NULL AND v.vkpfartikelpreisliste_i_id="
		// +preislisteIId;

		String queryString = "SELECT a FROM FLRArtikel a WHERE a.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND a.artikelart_c_nr not in('"
				+ ArtikelFac.ARTIKELART_HANDARTIKEL + "')";

		Session session = FLRSessionFactory.getFactory().openSession();

		org.hibernate.Query artikelpreis = session.createQuery(queryString);

		List<?> resultList = artikelpreis.list();

		Iterator<?> resultListIterator = resultList.iterator();

		VkpfartikelpreislisteDto preisliste = vkpfartikelpreislisteFindByPrimaryKey(preislisteIId);

		if (preisliste.getNStandardrabattsatz() != null) {

			while (resultListIterator.hasNext()) {

				FLRArtikel a = (FLRArtikel) resultListIterator.next();

				Session session2 = FLRSessionFactory.getFactory().openSession();
				String queryString2 = "SELECT p FROM FLRVkpfartikelpreis p WHERE p.artikel_i_id="
						+ a.getI_id()
						+ " AND p.vkpfartikelpreisliste_i_id="
						+ preislisteIId;
				org.hibernate.Query q2 = session2.createQuery(queryString2);
				List<?> resultList2 = q2.list();

				if (resultList2.size() < 1) {

					VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
					vkPreisfindungPreislisteDto
							.setVkpfartikelpreislisteIId(preislisteIId);
					vkPreisfindungPreislisteDto
							.setNArtikelstandardrabattsatz(preisliste
									.getNStandardrabattsatz());
					vkPreisfindungPreislisteDto
							.setTPreisgueltigab(tPreisgueltigab);

					vkPreisfindungPreislisteDto.setArtikelIId(a.getI_id());
					vkPreisfindungPreislisteDto.setTAendern(new Timestamp(
							System.currentTimeMillis()));
					vkPreisfindungPreislisteDto
							.setTPreisgueltigab(tPreisgueltigab);

					try {
						getVkPreisfindungFac().createVkPreisfindungPreisliste(
								vkPreisfindungPreislisteDto, theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}

				session2.close();
			}
		}
	}

	/**
	 * Entfernen einer Preisliste ueber den technischen Schluessel.
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 */
	public void removeVkPreisfindungPreisliste(Integer iId)
			throws EJBExceptionLP {
		final String METHOD_NAME = "removeVkPreisfindungPreisliste";
		myLogger.entry();

		if (iId == null) {
			throw new NullPointerException("PARAMTER NULL");
		}

		VkPreisfindungPreisliste vkPfPl = em.find(
				VkPreisfindungPreisliste.class, iId);
		em.remove(vkPfPl);
		em.flush();
	}

	/**
	 * Entfernen einer Preiliste.
	 * 
	 * @param vkPreisfindungPreislisteDto
	 *            VkPreisfindungPreislisteDto
	 * @throws EJBExceptionLP
	 */
	public void removeVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto)
			throws EJBExceptionLP {
		final String METHOD_NAME = "removeVkPreisfindungPreisliste";
		myLogger.entry();

		if (vkPreisfindungPreislisteDto == null) {
			throw new NullPointerException("PARAMTER NULL");
		}

		// try {
		VkPreisfindungPreisliste toRemove = em.find(
				VkPreisfindungPreisliste.class,
				vkPreisfindungPreislisteDto.getIId());
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
		// }
		// catch (RemoveException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, ex);
		// }
	}

	/**
	 * Aktualisieren einer Preisliste
	 * 
	 * @param vkPreisfindungPreislisteDto
	 *            VkPreisfindungPreislisteDto
	 * @throws EJBExceptionLP
	 */
	public void updateVkPreisfindungPreisliste(
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		final String METHOD_NAME = "updateVkPreisfindungPreisliste";
		myLogger.entry();

		// this.checkPreislistePreconditions(vkPreisfindungPreislisteDto);

		Integer iId = vkPreisfindungPreislisteDto.getIId();

		// try {
		VkPreisfindungPreisliste vkPreisfindungPreisliste = em.find(
				VkPreisfindungPreisliste.class, iId);

		vkPreisfindungPreislisteDto.setTAendern(new Timestamp(System
				.currentTimeMillis()));
		vkPreisfindungPreislisteDto.setPersonalIIdAendern(theClientDto
				.getIDPersonal());

		if (vkPreisfindungPreisliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		setVkPreisfindungPreislisteFromVkPreisfindungPreislisteDto(
				vkPreisfindungPreisliste, vkPreisfindungPreislisteDto);
		// }
		// catch (FinderException e) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// e);
		// }
	}

	/**
	 * Eine bestimmte Preisliste ueber ihren technischen Schluessel suchen
	 * 
	 * @param iId
	 *            Integer
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto vkPreisfindungPreislisteFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		final String METHOD_NAME = "";
		myLogger.entry();

		if (iId == null) {
			throw new NullPointerException("PARAMETER NULL");
		}

		VkPreisfindungPreislisteDto dto = null;

		// try {
		VkPreisfindungPreisliste vkPreisfindungPreisliste = em.find(
				VkPreisfindungPreisliste.class, iId);
		if (vkPreisfindungPreisliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		dto = assembleVkPreisfindungPreislisteDto(vkPreisfindungPreisliste);

		// }
		// catch (FinderException ex) {
		// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY,
		// ex);
		// }

		return dto;
	}

	public VkPreisfindungPreislisteDto[] vkPreisfindungPreislisteFindByArtikelIId(
			Integer artikelIId) throws EJBExceptionLP {
		Validator.notNull(artikelIId, "artikelIId");

		Query query = em
				.createNamedQuery("VkPreisfindungPreislistefindByArtikelIId");
		query.setParameter(1, artikelIId);
		VkPreisfindungPreislisteDto[] dtos = assembleVkPreisfindungPreislisteDtos(query
				.getResultList());

		return dtos;
	}

	public VkPreisfindungPreislisteDto getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
			Integer artikelIId, Integer preislisteIId,
			Date datGueltikeitsdatumI, TheClientDto theClientDto) {

		VkPreisfindungPreislisteDto dto = null;

		Query ejbquery = em
				.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab");
		ejbquery.setParameter(1, artikelIId);
		ejbquery.setParameter(2, preislisteIId);
		ejbquery.setParameter(3, datGueltikeitsdatumI);

		Collection c = ejbquery.getResultList();

		if (c.size() > 0) {
			//

			VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) c
					.iterator().next();
			dto = VkPreisfindungPreislisteDtoAssembler
					.createDto(vkPreisfindungPreisliste);
		}

		return dto;
	}

	private void setVkPreisfindungPreislisteFromVkPreisfindungPreislisteDto(
			VkPreisfindungPreisliste vkPreisfindungPreisliste,
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto) {
		vkPreisfindungPreisliste
				.setVkpfartikelpreislisteIId(vkPreisfindungPreislisteDto
						.getVkpfartikelpreislisteIId());
		vkPreisfindungPreisliste.setArtikelIId(vkPreisfindungPreislisteDto
				.getArtikelIId());
		vkPreisfindungPreisliste.setTPreisgueltigab(vkPreisfindungPreislisteDto
				.getTPreisgueltigab());
		vkPreisfindungPreisliste
				.setNArtikelstandardrabattsatz(vkPreisfindungPreislisteDto
						.getNArtikelstandardrabattsatz());
		vkPreisfindungPreisliste
				.setNArtikelfixpreis(vkPreisfindungPreislisteDto
						.getNArtikelfixpreis());
		vkPreisfindungPreisliste.setTAendern(vkPreisfindungPreislisteDto
				.getTAendern());
		vkPreisfindungPreisliste
				.setPersonalIIdAendern(vkPreisfindungPreislisteDto
						.getPersonalIIdAendern());
		em.merge(vkPreisfindungPreisliste);
		em.flush();
	}

	private VkPreisfindungPreislisteDto assembleVkPreisfindungPreislisteDto(
			VkPreisfindungPreisliste vkPreisfindungPreisliste) {
		return VkPreisfindungPreislisteDtoAssembler
				.createDto(vkPreisfindungPreisliste);
	}

	private VkPreisfindungPreislisteDto[] assembleVkPreisfindungPreislisteDtos(
			Collection<?> vkPreisfindungPreislistes) {
		List<VkPreisfindungPreislisteDto> list = new ArrayList<VkPreisfindungPreislisteDto>();
		if (vkPreisfindungPreislistes != null) {
			Iterator<?> iterator = vkPreisfindungPreislistes.iterator();
			while (iterator.hasNext()) {
				VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) iterator
						.next();
				list.add(assembleVkPreisfindungPreislisteDto(vkPreisfindungPreisliste));
			}
		}
		VkPreisfindungPreislisteDto[] returnArray = new VkPreisfindungPreislisteDto[list
				.size()];
		return (VkPreisfindungPreislisteDto[]) list.toArray(returnArray);
	}

	/**
	 * Prueft ob ein Datum gleich oder spaeter als eine bestimmte Untergrenze
	 * liegt. Das Datum darf nicht unter der Untergrenze liegen.
	 * 
	 * @param pDatum
	 *            Date
	 * @param pUntergrenze
	 *            Date
	 * @return boolean
	 */
	private boolean datumGueltigInbezugAufUntergrenze(Date pDatum,
			Date pUntergrenze) {
		boolean gueltig = false;

		if (pDatum.equals(pUntergrenze) || (pDatum.after(pUntergrenze))) {
			gueltig = true;
		}

		return gueltig;
	}

	/**
	 * Prueft ob ein Datum gleich oder vor einer bestimmten Obergrenze liegt.
	 * Das Datum darf nicht ueber der Obergrenze liegen.
	 * 
	 * @param pDatum
	 *            Date
	 * @param pObergrenze
	 *            Date
	 * @return boolean
	 */
	private boolean datumGueltigInbezugAufObergrenze(Date pDatum,
			Date pObergrenze) {
		boolean gueltig = false;

		if (pDatum.before(pObergrenze) || pDatum.equals(pObergrenze)) {
			gueltig = true;
		}

		return gueltig;
	}

	/**
	 * Holen der aktuell gueltigen Preisliste zu einem bestimmten Artikel. Gibt
	 * null zurueck, wenn der Artikel in keiner Preisliste enthalten ist.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdVkpfartikelpreislisteI
	 *            PK der Preisliste
	 * @param datGueltigabI
	 *            Preis ist gueltig zu diesem Datum
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto getAktuellePreislisteByArtikelIIdPreislisteIId(
			Integer iIdArtikelI, Integer iIdVkpfartikelpreislisteI,
			Date datGueltigabI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) throws EJBExceptionLP {

		long lUhrQuickDirty2 = System.currentTimeMillis();
		long lUhrQuickDirty = System.currentTimeMillis();
		myLogger.logData("ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIId: 0");
		myLogger.logData("ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIId serverentrytime 0: "
				+ lUhrQuickDirty);

		final String METHOD_NAME = "getAktuellePreislisteByArtikelIIdPreislisteIId";
		myLogger.entry();

		VkPreisfindungPreislisteDto preislisteDto = null;

		// alle Preislisten zu diesem Artikel holen
		VkPreisfindungPreislisteDto[] alleListen = null;

		try {
			Query query = em
					.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIId");
			query.setParameter(1, iIdArtikelI);
			query.setParameter(2, iIdVkpfartikelpreislisteI);
			Collection<?> alleObjekte = query.getResultList();
			alleListen = assembleVkPreisfindungPreislisteDtos(alleObjekte);
		} catch (Exception ex) {
			myLogger.warn(METHOD_NAME
					+ " : Fuer diesen Artikel sind keine Preislisten definiert.");
			return preislisteDto;
		}

		Date aktuellesDatum = getDate(); // obere Grenze

		if (datGueltigabI != null) {
			aktuellesDatum = datGueltigabI;
		}

		Date gueltigesDatum = new Date(
				new GregorianCalendar(0, 0, 0).getTimeInMillis()); // untere
		// Grenze

		for (int i = 0; i < alleListen.length; i++) {
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = alleListen[i];

			if (vkPreisfindungPreislisteDto.getNArtikelfixpreis() != null) {
				VkpfartikelpreislisteDto plDto = vkpfartikelpreislisteFindByPrimaryKey(iIdVkpfartikelpreislisteI);
				try {
					vkPreisfindungPreislisteDto
							.setNArtikelfixpreis(getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											vkPreisfindungPreislisteDto
													.getNArtikelfixpreis(),
											plDto.getWaehrungCNr(),
											waehrungCNrZielwaehrung,
											aktuellesDatum, theClientDto));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}

			Date gueltigAb = new Date(vkPreisfindungPreislisteDto
					.getTPreisgueltigab().getTime());

			if (this.datumGueltigInbezugAufUntergrenze(gueltigAb,
					gueltigesDatum)
					&& this.datumGueltigInbezugAufObergrenze(gueltigAb,
							aktuellesDatum)) {
				gueltigesDatum = new Date(vkPreisfindungPreislisteDto
						.getTPreisgueltigab().getTime());
				preislisteDto = (VkPreisfindungPreislisteDto) alleListen[i];
			}
		}

		myLogger.logData("ART>VK-Preise getAktuellePreislisteByArtikelIIdPreislisteIId leaving 2: "
				+ (System.currentTimeMillis() - lUhrQuickDirty2));
		return preislisteDto;
	}

	/**
	 * Die neuesten Preise zu einer Preisliste fuer einen Artikel finden.
	 * 
	 * @param iiArtikelI
	 *            pk des Artikels
	 * @param iiNameI
	 *            pk der Preisliste
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto getNeuestePreislisteByArtikelPreislistenname(
			Integer iiArtikelI, Integer iiNameI) throws EJBExceptionLP {
		final String METHOD_NAME = "getAktuellePreislisteByArtikelPreislistenname";
		myLogger.entry();

		VkPreisfindungPreislisteDto preislisteDto = null;

		// alle Preislisten zu diesem Artikel holen
		VkPreisfindungPreislisteDto[] alleListen = null;

		try {
			Query query = em
					.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIId");
			query.setParameter(1, iiArtikelI);
			query.setParameter(2, iiNameI);
			Collection<?> alleObjekte = query.getResultList();
			alleListen = this.assembleVkPreisfindungPreislisteDtos(alleObjekte);
		} catch (Exception ex) {
			myLogger.warn(METHOD_NAME
					+ " : Fuer diesen Artikel sind keine Preislisten definiert.");
			return preislisteDto;
		}

		// die preisliste zum gueltigkeitsdatum heraussuchen
		Date gueltigesDatum = new Date(
				new GregorianCalendar(0, 0, 0).getTimeInMillis()); // untere
		// Grenze

		for (int i = 0; i < alleListen.length; i++) {
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = alleListen[i];
			Date gueltigAb = new Date(vkPreisfindungPreislisteDto
					.getTPreisgueltigab().getTime());

			if (this.datumGueltigInbezugAufUntergrenze(gueltigAb,
					gueltigesDatum)) {
				gueltigesDatum = new Date(vkPreisfindungPreislisteDto
						.getTPreisgueltigab().getTime());
				preislisteDto = (VkPreisfindungPreislisteDto) alleListen[i];
			}
		}

		return preislisteDto;
	}

	// Verkaufspreisbasis eines Artikels
	// -----------------------------------------

	/**
	 * Anlegen einer kalenderaehigen Verkaufspreisbasis fuer einen Artikel. <br>
	 * Die Verkaufspreisbasis ist mandantenabhaengig.
	 * 
	 * @param vkPreisfindungEinzelverkaufspreisDto
	 *            VkPreisfindungEinzelverkaufspreisDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Integer PK der neuen Verkaufspreisbasis
	 */
	public Integer createVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkPreisfindungEinzelverkaufspreisDto(vkPreisfindungEinzelverkaufspreisDto);
		// unique key constraint pruefen
		VkPreisfindungEinzelverkaufspreis preis = null;
		try {
			Query query = em
					.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2,
					vkPreisfindungEinzelverkaufspreisDto.getArtikelIId());
			query.setParameter(3, vkPreisfindungEinzelverkaufspreisDto
					.getTVerkaufspreisbasisgueltigab());
			// @todo getSingleResult oder getResultList ?
			preis = (VkPreisfindungEinzelverkaufspreis) query.getSingleResult();

			// }
			// catch (ObjectNotFoundException ex) {
			// continue
		} catch (NoResultException ex) {
			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		if (preis != null) {
			// unique key constraint verletzt
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception());
		}

		Integer keyEinzelverkaufspreis = null;

		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			keyEinzelverkaufspreis = pkGen
					.getNextPrimaryKey(PKConst.PK_EINZELVERKAUFSPREIS);

			vkPreisfindungEinzelverkaufspreisDto
					.setPersonalIIdAendern(theClientDto.getIDPersonal());
			vkPreisfindungEinzelverkaufspreisDto.setTAendern(new Timestamp(
					System.currentTimeMillis()));

			VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = new VkPreisfindungEinzelverkaufspreis(
					keyEinzelverkaufspreis,
					vkPreisfindungEinzelverkaufspreisDto.getMandantCNr(),
					vkPreisfindungEinzelverkaufspreisDto.getArtikelIId(),
					vkPreisfindungEinzelverkaufspreisDto
							.getNVerkaufspreisbasis(),
					vkPreisfindungEinzelverkaufspreisDto
							.getTVerkaufspreisbasisgueltigab(),
					vkPreisfindungEinzelverkaufspreisDto.getTAendern(),
					vkPreisfindungEinzelverkaufspreisDto
							.getPersonalIIdAendern());
			em.persist(vkPreisfindungEinzelverkaufspreis);
			em.flush();
			setVkPreisfindungEinzelverkaufspreisFromVkPreisfindungEinzelverkaufspreisDto(
					vkPreisfindungEinzelverkaufspreis,
					vkPreisfindungEinzelverkaufspreisDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return keyEinzelverkaufspreis;
	}

	private void checkVkPreisfindungEinzelverkaufspreisDto(
			VkPreisfindungEinzelverkaufspreisDto oVkPreisfindungEinzelverkaufspreisDtoI) {
		if (oVkPreisfindungEinzelverkaufspreisDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception(
							"oVkPreisfindungEinzelverkaufspreisDtoI == null"));
		}

		myLogger.info("VkpfartikelverkaufspreisbasisDto: "
				+ oVkPreisfindungEinzelverkaufspreisDtoI.toString());
	}

	/**
	 * Entfernen einer kalenderfaehigen Verkaufspreisbasis fuer einen
	 * bestimmeten Artikel von der DB.
	 * 
	 * @param vkPreisfindungEinzelverkaufspreisDto
	 *            VkPreisfindungEinzelverkaufspreisDto
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkPreisfindungEinzelverkaufspreisDto(vkPreisfindungEinzelverkaufspreisDto);
		VkPreisfindungEinzelverkaufspreis toRemove = em.find(
				VkPreisfindungEinzelverkaufspreis.class,
				vkPreisfindungEinzelverkaufspreisDto.getIId());
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

	/**
	 * Aktualisieren der kalenderfaehigen Verkaufspreisbasis eines Artikels.
	 * 
	 * @param vkpfartikelverkaufspreisbasisDtoI
	 *            die bestehende Verkaufspreisbasis
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateVkPreisfindungEinzelverkaufspreis(
			VkPreisfindungEinzelverkaufspreisDto vkpfartikelverkaufspreisbasisDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkPreisfindungEinzelverkaufspreisDto(vkpfartikelverkaufspreisbasisDtoI);
		VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = em
				.find(VkPreisfindungEinzelverkaufspreis.class,
						vkpfartikelverkaufspreisbasisDtoI.getIId());

		vkpfartikelverkaufspreisbasisDtoI.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		vkpfartikelverkaufspreisbasisDtoI.setTAendern(new Timestamp(System
				.currentTimeMillis()));

		if (vkPreisfindungEinzelverkaufspreis == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setVkPreisfindungEinzelverkaufspreisFromVkPreisfindungEinzelverkaufspreisDto(
				vkPreisfindungEinzelverkaufspreis,
				vkpfartikelverkaufspreisbasisDtoI);
	}

	public VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab(
			Integer artikelIId, Date tGueltigab, TheClientDto theClientDto) {

		Query query = em
				.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, artikelIId);
		query.setParameter(3, tGueltigab);
		// @todo getSingleResult oder getResultList ?
		return assembleVkPreisfindungEinzelverkaufspreisDtos(query
				.getResultList());

	}

	public void pflegeVkpreise(Integer artikelgruppeIId,
			Integer vkpfartikelpreislisteIId, Date tGueltigab,
			BigDecimal nProzent, TheClientDto theClientDto) {
		if (artikelgruppeIId == null || tGueltigab == null || nProzent == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception(
							"artikelgruppeIId == null || tGueltigab == null || nProzent == null"));
		}
		tGueltigab = Helper.cutDate(tGueltigab);
		Session session = FLRSessionFactory.getFactory().openSession();
		int nachkommastellen = 2;
		ArrayList<Integer> listMeldung = new ArrayList<Integer>();

		Criteria artikel = session.createCriteria(FLRArtikel.class);
		artikel.createAlias(ArtikelFac.FLR_ARTIKEL_FLRARTIKELGRUPPE, "ag").add(
				Restrictions.eq("ag.i_id", artikelgruppeIId));
		// Alle Preislisten holen
		VkpfartikelpreislisteDto[] preislistenDtos = null;
		if (vkpfartikelpreislisteIId == null) {
			// try {
			Query query = em
					.createNamedQuery("VkpfartikelpreislistefindByMandantCNrBPreislisteaktiv");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, Helper.boolean2Short(true));
			// @todo getSingleResult oder getResultList ?
			preislistenDtos = assembleVkpfartikelpreislisteDtos(query
					.getResultList());
			// }
			// catch (FinderException ex1) {
			// throw new
			// EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, ex1);
			// }
		} else {
			preislistenDtos = new VkpfartikelpreislisteDto[1];
			preislistenDtos[0] = vkpfartikelpreislisteFindByPrimaryKey(vkpfartikelpreislisteIId);
		}
		List<?> list = artikel.list();

		Iterator<?> listIterator = list.iterator();

		while (listIterator.hasNext()) {
			FLRArtikel flrArtikel = (FLRArtikel) listIterator.next();
			// Preise erhoehen um nFaktor%
			// try {

			// PREISBASIS
			// Suche den letzen vkpreis, wenn vorhanden, von diesen um n-Prozent
			// erhoehen, wenn keine vorhanden, dann neu anlegen
			Query query = em
					.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, flrArtikel.getI_id());
			query.setParameter(3, tGueltigab);
			// @todo getSingleResult oder getResultList ?
			VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisDtosVorher = assembleVkPreisfindungEinzelverkaufspreisDtos(query
					.getResultList());

			if (vkPreisfindungEinzelverkaufspreisDtosVorher.length > 0) {
				VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDtoVorher = vkPreisfindungEinzelverkaufspreisDtosVorher[0];

				BigDecimal preisNeu = Helper.getProzentWert(
						vkPreisfindungEinzelverkaufspreisDtoVorher
								.getNVerkaufspreisbasis(), nProzent,
						nachkommastellen);

				vkPreisfindungEinzelverkaufspreisDtoVorher
						.setNVerkaufspreisbasis(vkPreisfindungEinzelverkaufspreisDtoVorher
								.getNVerkaufspreisbasis().add(preisNeu));

				if (tGueltigab
						.equals(vkPreisfindungEinzelverkaufspreisDtoVorher
								.getTVerkaufspreisbasisgueltigab())) {
					// Wenn gleich, dann update
					updateVkPreisfindungEinzelverkaufspreis(
							vkPreisfindungEinzelverkaufspreisDtoVorher,
							theClientDto);
				} else if (tGueltigab
						.after(vkPreisfindungEinzelverkaufspreisDtoVorher
								.getTVerkaufspreisbasisgueltigab())) {
					// Wenn kleiner, dann Neueintrag
					vkPreisfindungEinzelverkaufspreisDtoVorher.setIId(null);
					vkPreisfindungEinzelverkaufspreisDtoVorher
							.setTVerkaufspreisbasisgueltigab(tGueltigab);
					createVkPreisfindungEinzelverkaufspreis(
							vkPreisfindungEinzelverkaufspreisDtoVorher,
							theClientDto);
				}
			}
			// Wenn nachher noch Preise definiert sind, dann Meldung
			query = em
					.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdAbGueltigab");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, flrArtikel.getI_id());
			query.setParameter(3, tGueltigab);
			// @todo getSingleResult oder getResultList ?
			VkPreisfindungEinzelverkaufspreisDto[] vkPreisfindungEinzelverkaufspreisDtosNachher = assembleVkPreisfindungEinzelverkaufspreisDtos(query
					.getResultList());
			if (vkPreisfindungEinzelverkaufspreisDtosNachher.length > 0) {
				listMeldung.add(flrArtikel.getI_id());
			}

			for (int i = 0; i < preislistenDtos.length; i++) {

				// FIXPREISE IN ARTIKELPREIS
				query = em
						.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab");
				query.setParameter(1, flrArtikel.getI_id());
				query.setParameter(2, preislistenDtos[i].getIId());
				query.setParameter(3, tGueltigab);
				// @todo getSingleResult oder getResultList ?
				VkPreisfindungPreislisteDto[] vkPreisfindungPreislisteDtosVorher = assembleVkPreisfindungPreislisteDtos(query
						.getResultList());
				if (vkPreisfindungPreislisteDtosVorher.length > 0) {
					VkPreisfindungPreislisteDto vkPreisfindungPreislisteDtoVorher = vkPreisfindungPreislisteDtosVorher[0];

					if (vkPreisfindungPreislisteDtoVorher.getNArtikelfixpreis() != null) {

						BigDecimal preisNeu = Helper.getProzentWert(
								vkPreisfindungPreislisteDtoVorher
										.getNArtikelfixpreis(), nProzent,
								nachkommastellen);

						vkPreisfindungPreislisteDtoVorher
								.setNArtikelfixpreis(vkPreisfindungPreislisteDtoVorher
										.getNArtikelfixpreis().add(preisNeu));

						if (tGueltigab.equals(vkPreisfindungPreislisteDtoVorher
								.getTPreisgueltigab())) {
							// Wenn gleich, dann update
							updateVkPreisfindungPreisliste(
									vkPreisfindungPreislisteDtoVorher,
									theClientDto);
						} else if (tGueltigab
								.after(vkPreisfindungPreislisteDtoVorher
										.getTPreisgueltigab())) {
							// Wenn kleiner, dann Neueintrag
							vkPreisfindungPreislisteDtoVorher.setIId(null);
							vkPreisfindungPreislisteDtoVorher
									.setTPreisgueltigab(tGueltigab);
							createVkPreisfindungPreisliste(
									vkPreisfindungPreislisteDtoVorher,
									theClientDto);
						}
					}

				}
			}
			// VKSTAFFELPREISE

			Session sessionStaffel = FLRSessionFactory.getFactory()
					.openSession();

			String sQuery = "select distinct staffelmenge.n_menge from FLRVkpfStaffelmenge staffelmenge WHERE staffelmenge.t_preisgueltigab<='"
					+ Helper.formatDateWithSlashes(new java.sql.Date(tGueltigab
							.getTime()))
					+ "' AND staffelmenge.artikel_i_id="
					+ flrArtikel.getI_id();
			org.hibernate.Query staffeln = sessionStaffel.createQuery(sQuery);

			List<?> resultList = staffeln.list();

			BigDecimal[] mengen = new BigDecimal[resultList.size()];
			mengen = (BigDecimal[]) resultList.toArray(mengen);
			for (int i = 0; i < mengen.length; i++) {

				query = em
						.createNamedQuery("VkpfMengenstaffelfindByArtikelIIdNMenge");
				query.setParameter(1, flrArtikel.getI_id());
				query.setParameter(2, mengen[i]);
				// @todo getSingleResult oder getResultList ?
				VkpfMengenstaffelDto vkpfMengenstaffelDtoVorher = assembleVkpfMengenstaffelDtos(
						query.getResultList(), vkpfartikelpreislisteIId)[0];

				if (vkpfMengenstaffelDtoVorher.getNArtikelfixpreis() != null) {
					BigDecimal preisNeu = Helper.getProzentWert(
							vkpfMengenstaffelDtoVorher.getNArtikelfixpreis(),
							nProzent, nachkommastellen);

					vkpfMengenstaffelDtoVorher
							.setNArtikelfixpreis(vkpfMengenstaffelDtoVorher
									.getNArtikelfixpreis().add(preisNeu));
					if (tGueltigab.equals(vkpfMengenstaffelDtoVorher
							.getTPreisgueltigab())) {
						// Wenn gleich, dann update
						updateVkpfMengenstaffel(vkpfMengenstaffelDtoVorher,
								theClientDto);
					} else if (tGueltigab.after(vkpfMengenstaffelDtoVorher
							.getTPreisgueltigab())) {
						// Wenn kleiner, dann Neueintrag
						vkpfMengenstaffelDtoVorher.setIId(null);
						vkpfMengenstaffelDtoVorher.setTPreisgueltigbis(null);
						vkpfMengenstaffelDtoVorher
								.setTPreisgueltigab(tGueltigab);
						createVkpfMengenstaffel(vkpfMengenstaffelDtoVorher,
								theClientDto);
					}
				}

			}
			sessionStaffel.close();
		}
		session.close();

	}

	/**
	 * Finden einer kalenderfaehigen Verkaufspreisbasis fuer einen Artikel ueber
	 * den PK.
	 * 
	 * @param iIdVkpfartikelverkaufspreisbasisI
	 *            PK der Verkaufspreisbasis
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return VkPreisfindungEinzelverkaufspreisDto die Verkaufspreisbasis
	 */
	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByPrimaryKey(
			Integer iIdVkpfartikelverkaufspreisbasisI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdVkpfartikelverkaufspreisbasisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception());
		}
		VkPreisfindungEinzelverkaufspreisDto dto = null;
		VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = em
				.find(VkPreisfindungEinzelverkaufspreis.class,
						iIdVkpfartikelverkaufspreisbasisI);
		if (vkPreisfindungEinzelverkaufspreis == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		dto = assembleVkPreisfindungEinzelverkaufspreisDto(vkPreisfindungEinzelverkaufspreis);
		return dto;
	}

	/**
	 * Finden der Verkaufspreisbasis eines Artikels ueber den PK des Artikels
	 * bei einem bestimmten Mandanten.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return VkPreisfindungEinzelverkaufspreisDto[] die Verkaufspreisbasiss
	 */
	public VkPreisfindungEinzelverkaufspreisDto[] einzelverkaufspreisFindByMandantCNrArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		VkPreisfindungEinzelverkaufspreisDto[] dtos = null;
		Query query = em
				.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIId");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, iIdArtikelI);
		Collection<?> alleObjekte = query.getResultList();
		dtos = VkPreisfindungEinzelverkaufspreisDtoAssembler
				.createDtos(alleObjekte);
		return dtos;
	}

	/**
	 * Eine Verkaufspreisbasis ueber ihren UK suchen.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param datGueltigabI
	 *            gewuenschtes Gueltigkeitsdatum
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return VkPreisfindungEinzelverkaufspreisDto die Verkaufspreisbasis
	 */
	public VkPreisfindungEinzelverkaufspreisDto einzelverkaufspreisFindByMandantCNrArtikelIIdGueltigab(
			Integer iIdArtikelI, Date datGueltigabI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (datGueltigabI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigabI == null"));
		}
		VkPreisfindungEinzelverkaufspreisDto dto = null;
		try {
			Query query = em
					.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByMandantCNrArtikelIIdGueltigab");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, iIdArtikelI);
			query.setParameter(3, Helper.cutDate(datGueltigabI));
			VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) query
					.getSingleResult();
			dto = assembleVkPreisfindungEinzelverkaufspreisDto(vkPreisfindungEinzelverkaufspreis);
		} catch (NoResultException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, e);
		}
		return dto;
	}

	private void setVkPreisfindungEinzelverkaufspreisFromVkPreisfindungEinzelverkaufspreisDto(
			VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis,
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto) {
		vkPreisfindungEinzelverkaufspreis
				.setMandantCNr(vkPreisfindungEinzelverkaufspreisDto
						.getMandantCNr());
		vkPreisfindungEinzelverkaufspreis
				.setArtikelIId(vkPreisfindungEinzelverkaufspreisDto
						.getArtikelIId());
		vkPreisfindungEinzelverkaufspreis
				.setNVerkaufspreisbasis(vkPreisfindungEinzelverkaufspreisDto
						.getNVerkaufspreisbasis());
		vkPreisfindungEinzelverkaufspreis
				.setTVerkaufspreisbasisgueltigab(vkPreisfindungEinzelverkaufspreisDto
						.getTVerkaufspreisbasisgueltigab());
		vkPreisfindungEinzelverkaufspreis
				.setTAendern(vkPreisfindungEinzelverkaufspreisDto.getTAendern());
		vkPreisfindungEinzelverkaufspreis
				.setPersonalIIdAendern(vkPreisfindungEinzelverkaufspreisDto
						.getPersonalIIdAendern());

		em.merge(vkPreisfindungEinzelverkaufspreis);
		em.flush();

	}

	private VkPreisfindungEinzelverkaufspreisDto assembleVkPreisfindungEinzelverkaufspreisDto(
			VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis) {
		return VkPreisfindungEinzelverkaufspreisDtoAssembler
				.createDto(vkPreisfindungEinzelverkaufspreis);
	}

	private VkPreisfindungEinzelverkaufspreisDto[] assembleVkPreisfindungEinzelverkaufspreisDtos(
			Collection<?> vkPreisfindungEinzelverkaufspreiss) {
		List<VkPreisfindungEinzelverkaufspreisDto> list = new ArrayList<VkPreisfindungEinzelverkaufspreisDto>();
		if (vkPreisfindungEinzelverkaufspreiss != null) {
			Iterator<?> iterator = vkPreisfindungEinzelverkaufspreiss
					.iterator();
			while (iterator.hasNext()) {
				VkPreisfindungEinzelverkaufspreis vkPreisfindungEinzelverkaufspreis = (VkPreisfindungEinzelverkaufspreis) iterator
						.next();
				list.add(assembleVkPreisfindungEinzelverkaufspreisDto(vkPreisfindungEinzelverkaufspreis));
			}
		}
		VkPreisfindungEinzelverkaufspreisDto[] returnArray = new VkPreisfindungEinzelverkaufspreisDto[list
				.size()];
		return (VkPreisfindungEinzelverkaufspreisDto[]) list
				.toArray(returnArray);
	}

	/**
	 * Die neuste Verkaufspreisbasis eines Artikels bei einem bestimmten
	 * Mandanten holen.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VkPreisfindungEinzelverkaufspreisDto die Verkaufspreisbasis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public VkPreisfindungEinzelverkaufspreisDto getNeuestenArtikeleinzelverkaufspreis(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		VkPreisfindungEinzelverkaufspreisDto dto = null;
		// alle Verkaufspreisbasiss zu diesem Artikel holen
		VkPreisfindungEinzelverkaufspreisDto[] allePreise = null;
		try {
			allePreise = this.einzelverkaufspreisFindByMandantCNrArtikelIId(
					iIdArtikelI, theClientDto);
		}
		/**
		 * @todo MB->.. saubere Fehlerbehandlung!
		 */
		catch (Throwable t) {
			myLogger.info("Fuer diesen Artikel sind keine Einzelverkaufspreise definiert.");
			return dto;
		}

		// den Einzelverkaufspreis mit dem neuesten Datum heraussuchen
		Date gueltigesDatum = new Date(
				new GregorianCalendar(0, 0, 0).getTimeInMillis()); // untere
		// Grenze

		for (int i = 0; i < allePreise.length; i++) {
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = allePreise[i];
			Date gueltigAb = new Date(vkPreisfindungEinzelverkaufspreisDto
					.getTVerkaufspreisbasisgueltigab().getTime());

			if (this.datumGueltigInbezugAufUntergrenze(gueltigAb,
					gueltigesDatum)) {
				gueltigesDatum = new Date(vkPreisfindungEinzelverkaufspreisDto
						.getTVerkaufspreisbasisgueltigab().getTime());
				dto = (VkPreisfindungEinzelverkaufspreisDto) allePreise[i];
			}
		}

		return dto;
	}

	/**
	 * Preisliste mit aktuellem Gueltigkeitsdatum suchen.
	 * 
	 * @param pkPreislistenname
	 *            Integer
	 * @return VkPreisfindungPreislisteDto
	 * @throws EJBExceptionLP
	 */
	public VkPreisfindungPreislisteDto getAktuellePreislisteByPreislistenname(
			Integer pkPreislistenname) throws EJBExceptionLP {
		final String METHOD_NAME = "getAktuellePreislisteByPreislistenname";
		myLogger.entry();
		if (pkPreislistenname == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PKFIELD_IS_NULL,
					new Exception());
		}

		VkPreisfindungPreislisteDto dto = null;
		// alle Preislisten zu diesem Preislistennamen holen
		Query query = em
				.createNamedQuery("VkPreisfindungPreislistefindByVkpfartikelpreislisteIId");
		query.setParameter(1, pkPreislistenname);
		VkPreisfindungPreislisteDto[] alleListen = assembleVkPreisfindungPreislisteDtos(query
				.getResultList());
		// aktuelle Preisliste suchen
		Date aktuellesDatum = new Date(
				new GregorianCalendar().getTimeInMillis()); // obere Grenze
		Date gueltigesDatum = new Date(
				new GregorianCalendar(0, 0, 0).getTimeInMillis()); // untere
		// Grenze

		for (int i = 0; i < alleListen.length; i++) {
			VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = alleListen[i];
			Date gueltigAb = new Date(vkPreisfindungPreislisteDto
					.getTPreisgueltigab().getTime());

			if (this.datumGueltigInbezugAufUntergrenze(gueltigAb,
					gueltigesDatum)
					&& this.datumGueltigInbezugAufObergrenze(gueltigAb,
							aktuellesDatum)) {
				gueltigesDatum = new Date(vkPreisfindungPreislisteDto
						.getTPreisgueltigab().getTime());
				dto = (VkPreisfindungPreislisteDto) alleListen[i];
			}
		}
		return dto;
	}

	/**
	 * Ausgehend vom Verkaufspreis in Mandantenwaehrung wird ueber den
	 * Umrechnungsfaktor der Verkaufspreis in Fremdwaehrung bestimmt.
	 * 
	 * @param pPreisInMandantenwhg
	 *            in Mandantenwaehrung VerkaufspreisDto
	 * @param pKurs
	 *            fuer Umrechnung Mandantenwaherung nach Fremdwaehrung
	 *            BigDecimal
	 * @return VerkaufspreisDto in Fremdwaehrung
	 */
	public VerkaufspreisDto getVerkaufspreisInFremdwaehrung(
			VerkaufspreisDto pPreisInMandantenwhg, BigDecimal pKurs) {
		if (pPreisInMandantenwhg == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pPreisInMandantenwhg == null"));
		}

		if (pKurs == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pKurs == null"));
		}

		VerkaufspreisDto fremdwhg = new VerkaufspreisDto();

		fremdwhg.rabattsatz = pPreisInMandantenwhg.rabattsatz;
		fremdwhg.setDdZusatzrabattsatz(pPreisInMandantenwhg
				.getDdZusatzrabattsatz());
		fremdwhg.mwstsatzIId = pPreisInMandantenwhg.mwstsatzIId;

		fremdwhg.einzelpreis = pPreisInMandantenwhg.einzelpreis.multiply(pKurs);
		fremdwhg.rabattsumme = pPreisInMandantenwhg.rabattsumme.multiply(pKurs);
		if (pPreisInMandantenwhg.bdMaterialzuschlag != null) {
			fremdwhg.bdMaterialzuschlag = pPreisInMandantenwhg.bdMaterialzuschlag
					.multiply(pKurs);
		}
		fremdwhg.setNZusatzrabattsumme(pPreisInMandantenwhg
				.getNZusatzrabattsumme().multiply(pKurs));
		fremdwhg.nettopreis = pPreisInMandantenwhg.nettopreis.multiply(pKurs);
		fremdwhg.mwstsumme = pPreisInMandantenwhg.mwstsumme.multiply(pKurs);
		fremdwhg.bruttopreis = pPreisInMandantenwhg.bruttopreis.multiply(pKurs);

		return fremdwhg;
	}

	/**
	 * Ausgehend vom Verkaufspreis in Fremdwaehrung wird ueber den
	 * Umrechnungsfaktor der Verkaufspreis in Mandantenwaehrung bestimmt.
	 * 
	 * @param pPreisInFremdwhg
	 *            in Fremdwaehrung VerkaufspreisDto
	 * @param pKurs
	 *            fuer Umrechnung Mandantenwaherung nach Fremdwaehrung
	 *            BigDecimal
	 * @return VerkaufspreisDto in Mandantenwaherung
	 */
	public VerkaufspreisDto getVerkaufspreisInMandantenwaehrung(
			VerkaufspreisDto pPreisInFremdwhg, BigDecimal pKurs) {
		if (pPreisInFremdwhg == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pPreisInFremdwhg == null"));
		}

		if (pKurs == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("pKurs == null"));
		}

		VerkaufspreisDto mandantenwhg = new VerkaufspreisDto();

		mandantenwhg.rabattsatz = pPreisInFremdwhg.rabattsatz;
		mandantenwhg.setDdZusatzrabattsatz(pPreisInFremdwhg
				.getDdZusatzrabattsatz());
		mandantenwhg.mwstsatzIId = pPreisInFremdwhg.mwstsatzIId;
		if (pPreisInFremdwhg.bdMaterialzuschlag != null) {
			mandantenwhg.bdMaterialzuschlag = pPreisInFremdwhg.bdMaterialzuschlag
					.divide(pKurs, 4, BigDecimal.ROUND_HALF_EVEN);
		}
		mandantenwhg.einzelpreis = pPreisInFremdwhg.einzelpreis.divide(pKurs,
				4, BigDecimal.ROUND_HALF_EVEN);
		mandantenwhg.rabattsumme = pPreisInFremdwhg.rabattsumme.divide(pKurs,
				4, BigDecimal.ROUND_HALF_EVEN);
		mandantenwhg.setNZusatzrabattsumme(pPreisInFremdwhg
				.getNZusatzrabattsumme().divide(pKurs, 4,
						BigDecimal.ROUND_HALF_EVEN));
		mandantenwhg.nettopreis = pPreisInFremdwhg.nettopreis.divide(pKurs, 4,
				BigDecimal.ROUND_HALF_EVEN);
		mandantenwhg.mwstsumme = pPreisInFremdwhg.mwstsumme.divide(pKurs, 4,
				BigDecimal.ROUND_HALF_EVEN);
		mandantenwhg.bruttopreis = pPreisInFremdwhg.bruttopreis.divide(pKurs,
				4, BigDecimal.ROUND_HALF_EVEN);

		return mandantenwhg;
	}

	/**
	 * Eine Preisliste ueber ihren uniquey key finden
	 * 
	 * @param iiPreislisteI
	 *            pk der Preisliste
	 * @param iiArtikelI
	 *            pk des Artikels
	 * @param datGueltigabI
	 *            ab wann gilt der Preis
	 * @return VkPreisfindungPreislisteDto
	 */
	public VkPreisfindungPreislisteDto preislisteFindByUniqueKey(
			Integer iiPreislisteI, Integer iiArtikelI, Date datGueltigabI) {
		final String METHOD_NAME = "einzelverkaufspreisFindByUniqueKey";
		myLogger.entry();
		if (iiPreislisteI == null || iiArtikelI == null
				|| datGueltigabI == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_FELD_DARF_NICHT_NULL_SEIN,
					new Exception());
		}
		VkPreisfindungPreislisteDto dto = null;
		Query query = em
				.createNamedQuery("VkPreisfindungPreislistefindByVkpfartikelpreislisteIIdArtikelIIdPreisgueltigab");
		query.setParameter(1, iiPreislisteI);
		query.setParameter(2, iiArtikelI);
		query.setParameter(3, datGueltigabI);

		VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) query
				.getSingleResult();
		if (vkPreisfindungPreisliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		dto = assembleVkPreisfindungPreislisteDto(vkPreisfindungPreisliste);
		return dto;
	}

	/**
	 * Den Einzelverkaufpreis fuer einen Artikel holen, der zu einem bestimmten
	 * Datum gueltig ist.
	 * 
	 * @param iIdArtikelI
	 *            pk des Artikels
	 * @param datGueltikeitsdatumI
	 *            Gueltigkeitsdatum, wenn null wird das aktuelle Datum verwendet
	 * @param theClientDto
	 *            String
	 * @throws EJBExceptionLP
	 * @return VkPreisfindungEinzelverkaufspreisDto der Einzelverkaufspreis
	 */
	public VkPreisfindungEinzelverkaufspreisDto getArtikeleinzelverkaufspreis(
			Integer iIdArtikelI, Date datGueltikeitsdatumI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws EJBExceptionLP {

		long lUhrQuickDirty2 = System.currentTimeMillis();
		long lUhrQuickDirty = System.currentTimeMillis();
		myLogger.logData("ART>VK-Preise getArtikeleinzelverkaufspreis: 0");
		myLogger.logData("ART>VK-Preise getArtikeleinzelverkaufspreis serverentrytime 0: "
				+ lUhrQuickDirty);

		final String METHOD_NAME = "getArtikeleinzelverkaufspreis";
		myLogger.entry();

		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		// alle Einzelverkaufspreise zu diesem Artikel holen
		VkPreisfindungEinzelverkaufspreisDto[] allePreise = null;

		try {
			allePreise = einzelverkaufspreisFindByMandantCNrArtikelIId(
					iIdArtikelI, theClientDto);
		}
		/**
		 * @todo saubere fehlerbehandlung
		 */
		catch (Throwable t) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FIND,
					new Exception(
							"Fuer diesen Artikel sind keine Einzelverkaufspreise definiert."));
		}

		// den aktuell gueltigen Einzelverkaufspreis heraussuchen
		Date datUntereGrenze = new Date(
				new GregorianCalendar(0, 0, 0).getTimeInMillis()); // untere
		// Grenze

		Date datGueltigkeitsdatum = null;

		if (datGueltikeitsdatumI == null) {
			datGueltigkeitsdatum = getDate(); // obere Grenze ist aktuelles
			// Datum
		} else {
			datGueltigkeitsdatum = datGueltikeitsdatumI; // obere Grenze ist
			// Parameter
		}

		VkPreisfindungEinzelverkaufspreisDto dto = null;

		for (int i = 0; i < allePreise.length; i++) {
			VkPreisfindungEinzelverkaufspreisDto vkPreisfindungEinzelverkaufspreisDto = allePreise[i];
			Date datGueltigAb = new Date(vkPreisfindungEinzelverkaufspreisDto
					.getTVerkaufspreisbasisgueltigab().getTime());

			myLogger.info("Ist Gueltig ab:   "
					+ Helper.formatTimestamp(new java.sql.Timestamp(
							datGueltigAb.getTime()), Locale.GERMAN));
			myLogger.info("Soll Gueltig zum: "
					+ Helper.formatTimestamp(new java.sql.Timestamp(
							datGueltigkeitsdatum.getTime()), Locale.GERMAN));

			if (datumGueltigInbezugAufUntergrenze(datGueltigAb, datUntereGrenze)
					&& datumGueltigInbezugAufObergrenze(datGueltigAb,
							datGueltigkeitsdatum)) {
				datUntereGrenze = new Date(vkPreisfindungEinzelverkaufspreisDto
						.getTVerkaufspreisbasisgueltigab().getTime());

				// Umrechnen von Mandantenwaehrung auf ziewaehrung

				try {
					allePreise[i].setNVerkaufspreisbasis(getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(
									allePreise[i].getNVerkaufspreisbasis(),
									theClientDto.getSMandantenwaehrung(),
									waehrungCNrZielwaehrung,
									datGueltikeitsdatumI, theClientDto));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				dto = (VkPreisfindungEinzelverkaufspreisDto) allePreise[i];
			}
		}
		myLogger.logData("ART>VK-Preise getArtikeleinzelverkaufspreis leaving 2: "
				+ (System.currentTimeMillis() - lUhrQuickDirty2));
		return dto;
	}

	// Methoden zur Bestimmung der Sortierung der Preislistennamen
	// ---------------

	/**
	 * Das maximale iSort bei den Preislistennamen fuer einen bestimmten
	 * Mandanten bestimmen.
	 * 
	 * @param sMandantI
	 *            der aktuelle Mandant
	 * @return Integer das maximale iSort
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer getMaxISort(String sMandantI) throws EJBExceptionLP {
		Integer iiMaxISortO = null;
		try {
			Query query = em
					.createNamedQuery("VkpfartikelpreislisteejbSelectMaxISort");
			query.setParameter(1, sMandantI);
			iiMaxISortO = (Integer) query.getSingleResult();
			if (iiMaxISortO == null) {
				iiMaxISortO = new Integer(0);
			}
		} catch (Throwable e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_EJBSELECT,
					new Exception(e));
		}
		return iiMaxISortO;
	}

	/**
	 * Zwei bestehende Preislisten in Bezug auf ihr iSort umreihen.
	 * 
	 * @param iIdPreisliste1I
	 *            PK der ersten Preisliste
	 * @param iIdPreisliste2I
	 *            PK der zweiten Preisliste
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void vertauscheVkpfartikelpreisliste(Integer iIdPreisliste1I,
			Integer iIdPreisliste2I) throws EJBExceptionLP {
		final String METHOD_NAME = "vertauscheVkpfartikelpreisliste";
		myLogger.entry();
		Vkpfartikelpreisliste oPreisliste1 = em.find(
				Vkpfartikelpreisliste.class, iIdPreisliste1I);
		if (oPreisliste1 == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, "");
		}
		Vkpfartikelpreisliste oPreisliste2 = em.find(
				Vkpfartikelpreisliste.class, iIdPreisliste2I);
		Integer iSort1 = oPreisliste1.getISort();
		Integer iSort2 = oPreisliste2.getISort();
		// iSort der zweiten Preisliste auf ungueltig setzen, damit UK
		// constraint nicht verletzt wird
		oPreisliste2.setISort(new Integer(-1));
		oPreisliste1.setISort(iSort2);
		oPreisliste2.setISort(iSort1);
	}

	/**
	 * Wenn eine neue Preisliste im Hinblick auf iSort vor einer bestehenden
	 * eingefuegt werden soll, dann schafft diese Methode Platz fuer den neuen
	 * Datensatz. <br>
	 * Diese Methode wird am Client aufgerufen, bevor die neue Position
	 * abgespeichert wird.
	 * 
	 * @param sMandantI
	 *            der aktuelle Mandant
	 * @param iSortierungNeuePositionI
	 *            die Stelle, an der eingefuegt werden soll
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
			String sMandantI, int iSortierungNeuePositionI)
			throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("VkpfartikelpreislistefindByMandantCNr");
		query.setParameter(1, sMandantI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Vkpfartikelpreisliste oPreisliste = (Vkpfartikelpreisliste) it
					.next();

			if (oPreisliste.getISort().intValue() >= iSortierungNeuePositionI) {
				iSortierungNeuePositionI++;
				oPreisliste.setISort(new Integer(iSortierungNeuePositionI));
			}
		}
	}

	/**
	 * Wenn eine Preisliste geloescht wurde, dann muss die Sortierung angepasst
	 * werden, damit keine Luecken entstehen. <br>
	 * Diese Methode wird im Zuge des Loeschens der Position am Server
	 * aufgerufen.
	 * 
	 * @param sMandantI
	 *            String
	 * @param iSortierungGeloeschtePreislisteI
	 *            die Position der geloschten Position
	 * @throws EJBExceptionLP
	 */
	private void sortierungAnpassenBeiLoeschenEinerPreisliste(String sMandantI,
			int iSortierungGeloeschtePreislisteI) throws EJBExceptionLP {
		Query query = em
				.createNamedQuery("VkpfartikelpreislistefindByMandantCNr");
		query.setParameter(1, sMandantI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Vkpfartikelpreisliste oPreisliste = (Vkpfartikelpreisliste) it
					.next();

			if (oPreisliste.getISort().intValue() > iSortierungGeloeschtePreislisteI) {
				oPreisliste.setISort(new Integer(
						iSortierungGeloeschtePreislisteI));
				iSortierungGeloeschtePreislisteI++;
			}
		}
	}

	/**
	 * Einen Artikelpreis daraufhin pruefen, ob er unter dem minimalen
	 * Gestehungspreis auf einem bestimmten Lager liegt.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdLagerI
	 *            PK des Lagers
	 * @param bdPreisI
	 *            BigDecimal der vorgeschlagene Verkaufpreis
	 * @param ddWechselkursMandantZuBelegwaehrungI
	 *            Wechselkurs zum Umrechnen auf Mandantenwaehrung
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return boolean true, wenn der Verkaufspreis unter dem minimalen
	 *         Gestehungspreis liegt
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public boolean liegtVerkaufspreisUnterMinVerkaufspreis(Integer iIdArtikelI,
			Integer iIdLagerI, BigDecimal bdPreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMenge,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		if (iIdLagerI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdLagerI == null"));
		}
		if (bdPreisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdPreisI == null"));
		}
		if (ddWechselkursMandantZuBelegwaehrungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"ddWechselkursMandantZuBelegwaehrungI == null"));
		}

		BigDecimal minVK = null;

		try {
			// @todo wenn die minVK Methode fertig ist PJ 4638
			// 1. Gibt es den Artikel auf Lager X? -> return minVK
			// 2. Gibt es den Artikel am Hauptlager? -> return minVK
			// 3. Gibt es den Artikel nicht am Hauptlager, aber es gab ihn schon
			// (sieht man an Buchung) -> return minVK
			// 4. Den Einkaufspreis des bevorzugten Lieferanten (= der Erste)
			// 5. Sonst 0
			minVK = getLagerFac().getMindestverkaufspreis(iIdArtikelI,
					iIdLagerI, nMenge, theClientDto);

			// IMS 818 Verhindern, dass minVK == null
			if (minVK == null) {
				minVK = new BigDecimal(0);
			}
		}
		/**
		 * @todo saubere Fehlerbehandlung
		 */
		catch (Throwable t) {
			// wenn der minVerkaufspreis nicht gefunden wird, z.B. der Artikel
			// liegt nicht am Hauptlager
			minVK = new BigDecimal(0);
		}
		if (ddWechselkursMandantZuBelegwaehrungI != 0) {
			// den vorgeschlagenen Verkaufspreis in Mandantenwaehrung umrechnen
			// und auf zwei Stellen runden
			bdPreisI = bdPreisI.divide(new BigDecimal(
					ddWechselkursMandantZuBelegwaehrungI.doubleValue()),
					BigDecimal.ROUND_HALF_EVEN);
		}
		// beim Preisvergleich muss sichergestellt sein, dass beide Zahlen
		// dieselbe
		// Anzahl von Nachkommastellen besitzen
		minVK = Helper.rundeKaufmaennisch(minVK, 4);
		bdPreisI = Helper.rundeKaufmaennisch(bdPreisI, 4);

		boolean bLiegtVerkaufspreisUnterMinGestehungspreis = false;

		// return -1, 0 or 1 as this nettogesamtpreis is numerically less than,
		// equal to, or greater than minVK
		if (bdPreisI.compareTo(minVK) == -1) {
			bLiegtVerkaufspreisUnterMinGestehungspreis = true;
		}

		return bLiegtVerkaufspreisUnterMinGestehungspreis;
	}

	// Vkpfartikelpreisliste
	// -----------------------------------------------------

	/**
	 * Anlegen einer neuen Artikelpreisliste.
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return Integer
	 */
	public Integer createVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		checkVkpfartikelpreislisteDto(vkpfartikelpreislisteDto);
		// unique key constraint pruefen
		Vkpfartikelpreisliste oPreisliste = null;
		try {
			Query query = em
					.createNamedQuery("VkpfartikelpreislistefindByMandantCNrAndCNr");
			query.setParameter(1, vkpfartikelpreislisteDto.getMandantCNr());
			query.setParameter(2, vkpfartikelpreislisteDto.getCNr());
			oPreisliste = (Vkpfartikelpreisliste) query.getSingleResult();
		} catch (NoResultException ex) {
		}

		if (oPreisliste != null) {
			// unique key constraint verletzt
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception());
		}

		// den PK erzeugen und setzen
		Integer iIdPreisliste = null;

		try {
			iIdPreisliste = getPKGeneratorObj().getNextPrimaryKey(
					PKConst.PK_VKPFARTIKELPREISLISTE);
			vkpfartikelpreislisteDto.setIId(iIdPreisliste);
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR,
					new Exception(t));
		}

		try {
			Vkpfartikelpreisliste oVkpfartikelpreisliste = new Vkpfartikelpreisliste(
					iIdPreisliste, vkpfartikelpreislisteDto.getMandantCNr(),
					vkpfartikelpreislisteDto.getISort(),
					vkpfartikelpreislisteDto.getCNr(),
					vkpfartikelpreislisteDto.getWaehrungCNr());
			em.persist(oVkpfartikelpreisliste);
			em.flush();
			setVkpfartikelpreislisteFromVkpfartikelpreislisteDto(
					oVkpfartikelpreisliste, vkpfartikelpreislisteDto);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return iIdPreisliste;
	}

	/**
	 * Loeschen einer Preisliste.
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		myLogger.entry();
		checkVkpfartikelpreislisteDto(vkpfartikelpreislisteDto);
		Vkpfartikelpreisliste toRemove = em.find(Vkpfartikelpreisliste.class,
				vkpfartikelpreislisteDto.getIId());
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
		// die Sortierung muss angepasst werden
		sortierungAnpassenBeiLoeschenEinerPreisliste(theClientDto.getMandant(),
				vkpfartikelpreislisteDto.getISort().intValue());
	}

	/**
	 * Aktualisieren einer bestehenden Preisliste
	 * 
	 * @param vkpfartikelpreislisteDto
	 *            die Preisliste
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void updateVkpfartikelpreisliste(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto,
			boolean bRabattsatzAendern, java.sql.Date datumGueltigNeu,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkpfartikelpreislisteDto(vkpfartikelpreislisteDto);
		Vkpfartikelpreisliste oVkpfartikelpreisliste = em.find(
				Vkpfartikelpreisliste.class, vkpfartikelpreislisteDto.getIId());

		if (bRabattsatzAendern == true
				&& oVkpfartikelpreisliste.getNStandardrabattsatz() != null
				&& vkpfartikelpreislisteDto.getNStandardrabattsatz() != null) {
			aendereVorhandeneRabatteEinerPreisliste(
					vkpfartikelpreislisteDto.getIId(),
					oVkpfartikelpreisliste.getNStandardrabattsatz(),
					vkpfartikelpreislisteDto.getNStandardrabattsatz(),
					datumGueltigNeu, theClientDto);
		}

		if (oVkpfartikelpreisliste == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_UPDATE, "");
		}
		setVkpfartikelpreislisteFromVkpfartikelpreislisteDto(
				oVkpfartikelpreisliste, vkpfartikelpreislisteDto);
	}

	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}
		VkpfartikelpreislisteDto oDto = null;
		Vkpfartikelpreisliste vkpfartikelpreisliste = em.find(
				Vkpfartikelpreisliste.class, iId);
		if (vkpfartikelpreisliste == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		oDto = assembleVkpfartikelpreislisteDto(vkpfartikelpreisliste);
		return oDto;
	}

	public VkpfartikelpreislisteDto[]

	vkpfartikelpreislisteFindByMandantCNr(String mandantCNrI)
			throws EJBExceptionLP {
		final String METHOD_NAME = "vkpfartikelpreislisteFindByMandantCNr";
		myLogger.entry();
		VkpfartikelpreislisteDto[] dtos = null;
		Query query = em
				.createNamedQuery("VkpfartikelpreislistefindByMandantCNr");
		query.setParameter(1, mandantCNrI);
		dtos = assembleVkpfartikelpreislisteDtos(query.getResultList());
		return dtos;
	}

	public VkpfartikelpreislisteDto vkpfartikelpreislisteFindByMandantCNrAndCNr(
			String mandantCNrI, String cNrI) throws EJBExceptionLP {
		try {
			// @ToDo Ungueltiger Prozeduraufruf oder ungueltiges Argument
			Query query = em
					.createNamedQuery("VkpfartikelpreislistefindByMandantCNrAndCNr");
			query.setParameter(1, mandantCNrI);
			query.setParameter(2, cNrI);
			return this
					.assembleVkpfartikelpreislisteDto((Vkpfartikelpreisliste) query
							.getSingleResult());
		} catch (NoResultException ex) {
			// @ToDo FinderException
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");

			// throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, ex);
		}
	}

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislisten(
			Short bPreislisteaktivI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Validator.notNull(bPreislisteaktivI, "bPreislisteaktivI");

		VkpfartikelpreislisteDto[] aVkpfartikelpreislisteDtoO = null;
		Query query = em
				.createNamedQuery("VkpfartikelpreislistefindByMandantCNrBPreislisteaktiv");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, bPreislisteaktivI);
		Collection<?> cl = query.getResultList();
		aVkpfartikelpreislisteDtoO = assembleVkpfartikelpreislisteDtos(cl);
		return aVkpfartikelpreislisteDtoO;
	}

	public Map getAlleAktivenPreislistenMitVkPreisbasis(
			TheClientDto theClientDto) {

		TreeMap<Integer, String> m = new TreeMap<Integer, String>();

		VkpfartikelpreislisteDto[] pDtos = getAlleAktivenPreislisten(
				Helper.boolean2Short(true), theClientDto);

		for (int i = 0; i < pDtos.length; i++) {
			m.put(pDtos[i].getIId(), pDtos[i].getCNr());
		}

		// Nun noch VKPreisbasis hinzufuegen oder LiefPreis

		try {
			ParametermandantDto p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
			if (p != null && p.getCWert().equals("1")) {
				m.put(new Integer(-1),
						getTextRespectUISpr("artikel.label.lief1Preis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			} else {
				m.put(new Integer(-1),
						getTextRespectUISpr(
								"artikel.label.einzelverkaufspreis",
								theClientDto.getMandant(),
								theClientDto.getLocUi()));
			}
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// PJ 17302 und ALLE PREISLISTEN

		m.put(new Integer(-2),
				getTextRespectUISpr("artikel.allepreislisten",
						theClientDto.getMandant(), theClientDto.getLocUi()));

		return m;
	}

	public VkpfartikelpreislisteDto[] getAlleAktivenPreislistenMitHinterlegtemArtikelpreis(
			Integer iIdArtikelI, Short bPreislisteaktivI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (bPreislisteaktivI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bPreislisteaktivI == null"));
		}

		VkpfartikelpreislisteDto[] aVkpfartikelpreislisteDtoO = null;

		ArrayList<Vkpfartikelpreisliste> alVkpfartikelpreisliste = new ArrayList<Vkpfartikelpreisliste>();
		Query query = em
				.createNamedQuery("VkpfartikelpreislistefindByMandantCNrBPreislisteaktiv");
		query.setParameter(1, theClientDto.getMandant());
		query.setParameter(2, bPreislisteaktivI);
		Collection<?> cl = query.getResultList();
		Iterator<?> it = cl.iterator();
		while (it.hasNext()) {
			Vkpfartikelpreisliste oPreisliste = (Vkpfartikelpreisliste) it
					.next();

			VkPreisfindungPreislisteDto oPreisDto = null;

			try {
				oPreisDto = getAktuellePreislisteByArtikelIIdPreislisteIId(
						iIdArtikelI, oPreisliste.getIId(),
						new Date(System.currentTimeMillis()),
						waehrungCNrZielwaehrung, theClientDto);
			}
			/**
			 * @todo saubere fehlerbehandlung!
			 */
			catch (Throwable t) {
				// wenn kein Preis hinterlegt ist
			}

			if (oPreisDto != null) {
				alVkpfartikelpreisliste.add(oPreisliste);
			}
		}

		aVkpfartikelpreislisteDtoO = new VkpfartikelpreislisteDto[alVkpfartikelpreisliste
				.size()];

		for (int i = 0; i < alVkpfartikelpreisliste.size(); i++) {
			aVkpfartikelpreislisteDtoO[i] = assembleVkpfartikelpreislisteDto((Vkpfartikelpreisliste) alVkpfartikelpreisliste
					.get(i));
		}
		return aVkpfartikelpreislisteDtoO;
	}

	private void setVkpfartikelpreislisteFromVkpfartikelpreislisteDto(
			Vkpfartikelpreisliste vkpfartikelpreisliste,
			VkpfartikelpreislisteDto vkpfartikelpreislisteDto) {
		vkpfartikelpreisliste.setMandantCNr(vkpfartikelpreislisteDto
				.getMandantCNr());
		vkpfartikelpreisliste.setISort(vkpfartikelpreislisteDto.getISort());
		vkpfartikelpreisliste.setCNr(vkpfartikelpreislisteDto.getCNr());
		vkpfartikelpreisliste.setBPreislisteaktiv(vkpfartikelpreislisteDto
				.getBPreislisteaktiv());
		vkpfartikelpreisliste.setWaehrungCNr(vkpfartikelpreislisteDto
				.getWaehrungCNr());
		vkpfartikelpreisliste.setCFremdsystemnr(vkpfartikelpreislisteDto
				.getCFremdsystemnr());
		vkpfartikelpreisliste.setNStandardrabattsatz(vkpfartikelpreislisteDto
				.getNStandardrabattsatz());
		vkpfartikelpreisliste.setWebshopIId(vkpfartikelpreislisteDto
				.getWebshopIId());
		em.merge(vkpfartikelpreisliste);
		em.flush();
	}

	private VkpfartikelpreislisteDto assembleVkpfartikelpreislisteDto(
			Vkpfartikelpreisliste vkpfartikelpreisliste) {
		return VkpfartikelpreislisteDtoAssembler
				.createDto(vkpfartikelpreisliste);
	}

	private VkpfartikelpreislisteDto[] assembleVkpfartikelpreislisteDtos(
			Collection<?> vkpfartikelpreislistes) {
		List<VkpfartikelpreislisteDto> list = new ArrayList<VkpfartikelpreislisteDto>();
		if (vkpfartikelpreislistes != null) {
			Iterator<?> iterator = vkpfartikelpreislistes.iterator();
			while (iterator.hasNext()) {
				Vkpfartikelpreisliste vkpfartikelpreisliste = (Vkpfartikelpreisliste) iterator
						.next();
				list.add(assembleVkpfartikelpreislisteDto(vkpfartikelpreisliste));
			}
		}
		VkpfartikelpreislisteDto[] returnArray = new VkpfartikelpreislisteDto[list
				.size()];
		return (VkpfartikelpreislisteDto[]) list.toArray(returnArray);
	}

	private void checkVkpfartikelpreislisteDto(
			VkpfartikelpreislisteDto vkpfartikelpreislisteDtoI)
			throws EJBExceptionLP {
		if (vkpfartikelpreislisteDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("vkpfartikelpreislisteDtoI == null"));
		}
	}

	/**
	 * Alle Verkaufspreisbasen zu einem bestimmten Artikel holen.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @return VkPreisfindungEinzelverkaufspreisDto[] die Verkaufspreisbasen
	 */
	public VkPreisfindungEinzelverkaufspreisDto[] vkpfartikelverkaufspreisbasisFindByArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		VkPreisfindungEinzelverkaufspreisDto[] aVkpfartikelverkaufspreisbasisDto = null;
		Query query = em
				.createNamedQuery("VkPreisfindungEinzelverkaufspreisfindByArtikelIId");
		query.setParameter(1, iIdArtikelI);
		Collection<?> cl = query.getResultList();
		aVkpfartikelverkaufspreisbasisDto = assembleVkPreisfindungEinzelverkaufspreisDtos(cl);
		return aVkpfartikelverkaufspreisbasisDto;
	}

	/**
	 * Methode zur Berechnung des waehrungsunabhaengigen Verkaufspreises einer
	 * Belegposition.
	 * 
	 * @param bdEinzelpreisI
	 *            Nettoeinzelpreis
	 * @param ddRabattsatzI
	 *            Rabattsatz in Prozent z.B. 20
	 * @param ddZusatzrabattsatzI
	 *            Zusatzrabattsatz in Prozent z.B. 10
	 * @param iIdMwstsatzI
	 *            PK des Mwstsatzes
	 * @param iAnzahlStellenI
	 *            auf wieviele Stellen sollen die Preise kaufmaennisch gerundet
	 *            werden, -1 ohne Rundung
	 * @param theClientDto
	 *            String
	 * @return VerkaufspreisDto die berechneten Preisfolder
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public VerkaufspreisDto berechnePreisfelder(BigDecimal bdEinzelpreisI,
			Double ddRabattsatzI, Double ddZusatzrabattsatzI,
			Integer iIdMwstsatzI, int iAnzahlStellenI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		// TheClientDto theClientDto = check2(cNrUserI);
		if (iAnzahlStellenI != -1 && iAnzahlStellenI <= 0) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(
					"iAnzahlStellenI muss -1 oder positiv sein"));
		}

		if (bdEinzelpreisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdEinzelpreisI == null"));
		}

		if (ddRabattsatzI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("ddRabattsatzI == null"));
		}

		if (ddZusatzrabattsatzI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("ddZusatzrabattsatzI == null"));
		}

		if (iIdMwstsatzI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdMwstsatzI == null"));
		}

		VerkaufspreisDto verkaufspreisDto = new VerkaufspreisDto();

		try {
			verkaufspreisDto.einzelpreis = bdEinzelpreisI;
			verkaufspreisDto.rabattsatz = ddRabattsatzI;

			BigDecimal nRabattsumme = verkaufspreisDto.einzelpreis
					.multiply(new BigDecimal(ddRabattsatzI.doubleValue())
							.movePointLeft(2));

			verkaufspreisDto.rabattsumme = nRabattsumme;

			verkaufspreisDto.setDdZusatzrabattsatz(ddZusatzrabattsatzI);

			BigDecimal nZusatzrabattsumme = verkaufspreisDto.einzelpreis
					.subtract(verkaufspreisDto.rabattsumme)
					.multiply(
							new BigDecimal(verkaufspreisDto
									.getDdZusatzrabattsatz().doubleValue()))
					.movePointLeft(2);

			verkaufspreisDto.setNZusatzrabattsumme(nZusatzrabattsumme);

			verkaufspreisDto.nettopreis = verkaufspreisDto.einzelpreis
					.subtract(verkaufspreisDto.rabattsumme).subtract(
							nZusatzrabattsumme);

			verkaufspreisDto.mwstsatzIId = iIdMwstsatzI;

			MwstsatzDto mwstsatzDto = getMandantFac().mwstsatzFindByPrimaryKey(
					iIdMwstsatzI, theClientDto);

			verkaufspreisDto.mwstsumme = verkaufspreisDto.nettopreis
					.multiply(new BigDecimal(mwstsatzDto.getFMwstsatz()
							.doubleValue()).movePointLeft(2));

			verkaufspreisDto.bruttopreis = verkaufspreisDto.nettopreis
					.add(verkaufspreisDto.mwstsumme);

			if (iAnzahlStellenI != -1) {
				verkaufspreisDto.einzelpreis = Helper.rundeKaufmaennisch(
						verkaufspreisDto.einzelpreis, iAnzahlStellenI);
				verkaufspreisDto.rabattsumme = Helper.rundeKaufmaennisch(
						verkaufspreisDto.rabattsumme, iAnzahlStellenI);
				verkaufspreisDto.setNZusatzrabattsumme(Helper
						.rundeKaufmaennisch(
								verkaufspreisDto.getNZusatzrabattsumme(),
								iAnzahlStellenI));
				verkaufspreisDto.nettopreis = Helper.rundeKaufmaennisch(
						verkaufspreisDto.nettopreis, iAnzahlStellenI);
				verkaufspreisDto.mwstsumme = Helper.rundeKaufmaennisch(
						verkaufspreisDto.mwstsumme, iAnzahlStellenI);
				verkaufspreisDto.bruttopreis = Helper.rundeKaufmaennisch(
						verkaufspreisDto.bruttopreis, iAnzahlStellenI);
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return verkaufspreisDto;
	}

	// VkpfMengenstaffel
	// ---------------------------------------------------------

	/**
	 * Eine VK Mengenstaffel anlegen.
	 * 
	 * @param vkpfMengenstaffelDtoI
	 *            die neue Mengenstaffel
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return Integer PK der neuen Mengenstaffel
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public Integer createVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkpfMengenstaffelDto(vkpfMengenstaffelDtoI);
		Integer iId = new Integer(-1);
		try {
			// Daten normieren
			vkpfMengenstaffelDtoI.setTPreisgueltigab(Helper
					.cutDate(vkpfMengenstaffelDtoI.getTPreisgueltigab()));
			vkpfMengenstaffelDtoI.setTPreisgueltigbis(Helper
					.cutDate(vkpfMengenstaffelDtoI.getTPreisgueltigbis()));

			// UK pruefen
			VkpfMengenstaffelDto vkpfMengenstaffelDto = null;

			try {
				vkpfMengenstaffelDto = vkpfMengenstaffelFindByUniqueKey(
						vkpfMengenstaffelDtoI.getArtikelIId(),
						vkpfMengenstaffelDtoI.getNMenge(),
						vkpfMengenstaffelDtoI.getTPreisgueltigab(),
						vkpfMengenstaffelDtoI.getVkpfartikelpreislisteIId());
			}
			/**
			 * @todo saubere Fehlerbehandlung
			 */
			catch (Throwable t) {
				// continue
			}

			if (vkpfMengenstaffelDto != null) {
				// constraint verletzt
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception());
			}

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj();
			iId = pkGen.getNextPrimaryKey(PKConst.PK_VKPFMENGENSTAFFEL);

			vkpfMengenstaffelDtoI.setIId(iId);
			vkpfMengenstaffelDtoI.setPersonalIIdAendern(theClientDto
					.getIDPersonal());
			vkpfMengenstaffelDtoI.setTAendern(getTimestamp());
			checkVkpfMengenstaffel(vkpfMengenstaffelDtoI, theClientDto);
			Vkpfmengenstaffel vkpfMengenstaffel = new Vkpfmengenstaffel(
					vkpfMengenstaffelDtoI.getIId(),
					vkpfMengenstaffelDtoI.getArtikelIId(),
					vkpfMengenstaffelDtoI.getNMenge(),
					vkpfMengenstaffelDtoI.getTPreisgueltigab(),
					vkpfMengenstaffelDtoI.getPersonalIIdAendern(),
					vkpfMengenstaffelDtoI.getBAllepreislisten());
			em.persist(vkpfMengenstaffel);
			em.flush();
			setVkpfMengenstaffelFromVkpfMengenstaffelDto(vkpfMengenstaffel,
					vkpfMengenstaffelDtoI);
		} catch (EntityExistsException e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, e);
		}

		return iId;
	}

	@TransactionTimeout(1000)
	private void aendereVorhandeneRabatteEinerPreisliste(Integer preislisteIId,
			BigDecimal rabattsatzAlt, BigDecimal rabattsatzNeu,
			java.sql.Date datumNeu, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String queryString = "SELECT a FROM FLRArtikel AS a WHERE a.mandant_c_nr='"
				+ theClientDto.getMandant() + "' AND a.b_versteckt=0";

		datumNeu = Helper.cutDate(datumNeu);

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();
		int row = 0;
		while (resultListIterator.hasNext()) {
			FLRArtikel flrArtikel = (FLRArtikel) resultListIterator.next();

			Query ejbquery = em
					.createNamedQuery("VkPreisfindungPreislistefindByArtikelIIdVkpfartikelpreislisteIIdBisTPreisgueltigab");
			ejbquery.setParameter(1, flrArtikel.getI_id());
			ejbquery.setParameter(2, preislisteIId);
			ejbquery.setParameter(3, datumNeu);

			Collection c = ejbquery.getResultList();

			if (c.size() > 0) {
				//

				VkPreisfindungPreisliste vkPreisfindungPreisliste = (VkPreisfindungPreisliste) c
						.iterator().next();

				if (vkPreisfindungPreisliste.getNArtikelstandardrabattsatz() != null
						&& vkPreisfindungPreisliste
								.getNArtikelstandardrabattsatz().doubleValue() == rabattsatzAlt
								.doubleValue()) {

					if (rabattsatzNeu.doubleValue() != rabattsatzAlt
							.doubleValue()) {

						if (vkPreisfindungPreisliste.getTPreisgueltigab()
								.equals(datumNeu)) {
							vkPreisfindungPreisliste
									.setNArtikelstandardrabattsatz(rabattsatzNeu);
							em.merge(vkPreisfindungPreisliste);
							em.flush();
						} else {
							// Neu anlegen
							VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = new VkPreisfindungPreislisteDto();
							vkPreisfindungPreislisteDto
									.setVkpfartikelpreislisteIId(preislisteIId);
							vkPreisfindungPreislisteDto
									.setArtikelIId(flrArtikel.getI_id());
							vkPreisfindungPreislisteDto
									.setNArtikelstandardrabattsatz(rabattsatzNeu);
							vkPreisfindungPreislisteDto
									.setTPreisgueltigab(new java.sql.Date(
											datumNeu.getTime()));
							vkPreisfindungPreislisteDto
									.setVkpfartikelpreislisteIId(preislisteIId);
							createVkPreisfindungPreisliste(
									vkPreisfindungPreislisteDto, theClientDto);
						}
					}
				}
			}
		}

	}

	/**
	 * Mengenstaffeln duerfen datumsmaessig nicht ueberlappen. Wenn eine Staffel
	 * an eine bestehende Staffel anschliesst, die kein bis-Datum hat, wird
	 * dieses automatisch ergaenzt. Wenn eine Staffel vor eine bestehende
	 * Staffel kommt und kein bis-Datum hat, dann wird dieses automatisch
	 * ergaenzt. Jede Staffel wird in dieser Art eingereiht, sodass lediglich
	 * die letzte erfasste Staffel ein offenes Ende haben kann.
	 * 
	 * @param vkpfMengenstaffelDtoI
	 *            die zu pruefende Mengenstaffel
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void checkVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// Schritt 1: Alle bestehenden Mengenstaffeln zu diesem Artikel holen
		VkpfMengenstaffelDto[] bestehendeDtos = vkpfMengenstaffelFindByArtikelIIdNMenge(
				vkpfMengenstaffelDtoI.getArtikelIId(),
				vkpfMengenstaffelDtoI.getNMenge(),
				vkpfMengenstaffelDtoI.getVkpfartikelpreislisteIId(),
				theClientDto);

		// Schritt 2: durch den vorhergegangen UK ist sichergestellt, dass
		// Artikel, Menge,
		// gueltig ab eindeutig ist. Ausserdem muss sichgestellt sein, dass
		// weder gueltig ab
		// noch gueltig bis durch einen Datumsbereich abgedeckt wurde
		boolean bEnthalten = false;
		int iIndex = 0;

		while (!bEnthalten && iIndex < bestehendeDtos.length) {
			if (vkpfMengenstaffelDtoI.getIId().intValue() != bestehendeDtos[iIndex]
					.getIId().intValue()) {

				if ((vkpfMengenstaffelDtoI.getVkpfartikelpreislisteIId() == null && bestehendeDtos[iIndex]
						.getVkpfartikelpreislisteIId() == null)
						|| (vkpfMengenstaffelDtoI.getVkpfartikelpreislisteIId() != null && vkpfMengenstaffelDtoI
								.getVkpfartikelpreislisteIId().equals(
										bestehendeDtos[iIndex]
												.getVkpfartikelpreislisteIId()))) {

					// PJ 14014
					Date bis = null;
					if (bestehendeDtos[iIndex].getTPreisgueltigbis() != null) {
						bis = new Date(bestehendeDtos[iIndex]
								.getTPreisgueltigbis().getTime());
					}

					bEnthalten = checkDatumInDatumsbereichEnthalten(new Date(
							bestehendeDtos[iIndex].getTPreisgueltigab()
									.getTime()), bis, new Date(
							vkpfMengenstaffelDtoI.getTPreisgueltigab()
									.getTime()), theClientDto);

					if (vkpfMengenstaffelDtoI.getTPreisgueltigbis() != null
							&& !bEnthalten) {
						bEnthalten = checkDatumInDatumsbereichEnthalten(
								new Date(bestehendeDtos[iIndex]
										.getTPreisgueltigab().getTime()),
								new Date(bestehendeDtos[iIndex]
										.getTPreisgueltigbis().getTime()),
								new Date(vkpfMengenstaffelDtoI
										.getTPreisgueltigbis().getTime()),
								theClientDto);
					}
				}
			}

			iIndex++;
		}

		if (bEnthalten) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT,
					new Exception("ArtikelIId="
							+ vkpfMengenstaffelDtoI.getArtikelIId()
							+ ", Menge="
							+ vkpfMengenstaffelDtoI.getNMenge()
							+ ", gueltig bis="
							+ Helper.formatDatum(
									vkpfMengenstaffelDtoI.getTPreisgueltigab(),
									theClientDto.getLocUi())));
		}

		// Schritt 3: Den Vorgaenger und den Nachfolger der neuen Staffel
		// aufgrund ihres Anfangsdatums bestimmen.
		iIndex = 0;
		VkpfMengenstaffelDto vorgaengerDto = null;
		VkpfMengenstaffelDto nachfolgerDto = null;

		if (bestehendeDtos != null && bestehendeDtos.length > 0) {
			while (nachfolgerDto == null && iIndex < bestehendeDtos.length) {
				if (vkpfMengenstaffelDtoI.getIId().intValue() != bestehendeDtos[iIndex]
						.getIId().intValue()) {

					if ((vkpfMengenstaffelDtoI.getBAllepreislisten()
							.equals(bestehendeDtos[iIndex]
									.getBAllepreislisten()))) {

						if ((vkpfMengenstaffelDtoI
								.getVkpfartikelpreislisteIId() == null && bestehendeDtos[iIndex]
								.getVkpfartikelpreislisteIId() == null)
								|| (vkpfMengenstaffelDtoI
										.getVkpfartikelpreislisteIId() != null && vkpfMengenstaffelDtoI
										.getVkpfartikelpreislisteIId()
										.equals(bestehendeDtos[iIndex]
												.getVkpfartikelpreislisteIId()))) {
							if (vkpfMengenstaffelDtoI.getTPreisgueltigab()
									.getTime() > bestehendeDtos[iIndex]
									.getTPreisgueltigab().getTime()) {
								iIndex++;
							} else if (vkpfMengenstaffelDtoI
									.getTPreisgueltigab().getTime() == bestehendeDtos[iIndex]
									.getTPreisgueltigab().getTime()) {
								throw new EJBExceptionLP(
										EJBExceptionLP.FEHLER_VKPF_MENGENSTAFFEL_EXISTIERT,
										new Exception(
												"ArtikelIId="
														+ vkpfMengenstaffelDtoI
																.getArtikelIId()
														+ ", Menge="
														+ vkpfMengenstaffelDtoI
																.getNMenge()
														+ ", gueltig ab="
														+ Helper.formatDatum(
																vkpfMengenstaffelDtoI
																		.getTPreisgueltigab(),
																theClientDto
																		.getLocUi())));
							} else if (vkpfMengenstaffelDtoI
									.getTPreisgueltigab().getTime() < bestehendeDtos[iIndex]
									.getTPreisgueltigab().getTime()) {
								nachfolgerDto = bestehendeDtos[iIndex];

								if (iIndex > 0) {
									vorgaengerDto = bestehendeDtos[iIndex - 1];
								}
							}
						} else {
							iIndex++;
						}
					} else {
						iIndex++;
					}
				} else {
					iIndex++;
				}
			}

			// wenn die neue Mengenstaffel die letzte ist
			if (iIndex > 0
					&& vkpfMengenstaffelDtoI.getIId().intValue() != bestehendeDtos[iIndex - 1]
							.getIId().intValue()
					&& nachfolgerDto == null
					&& vorgaengerDto == null
					&& ((vkpfMengenstaffelDtoI.getVkpfartikelpreislisteIId() + "")
							.equals(bestehendeDtos[iIndex - 1]
									.getVkpfartikelpreislisteIId() + ""))
					&& (vkpfMengenstaffelDtoI.getBAllepreislisten()
							.equals(bestehendeDtos[iIndex - 1]
									.getBAllepreislisten()))) {
				vorgaengerDto = bestehendeDtos[iIndex - 1];
			}
		}

		// Schritt 4: Wenn es einen Nachfolger gibt, muss der neue Bereich ev.
		// ergaenzt werden
		if (nachfolgerDto != null
				&& vkpfMengenstaffelDtoI.getTPreisgueltigbis() == null) {
			// den neuen Zeitraum ergaenzen, wenn es kein bis-Datum, aber einen
			// Nachfolger gibt
			if (vkpfMengenstaffelDtoI.getTPreisgueltigbis() == null) {
				Calendar cal = new GregorianCalendar();
				cal.setTime(nachfolgerDto.getTPreisgueltigab());
				cal.add(Calendar.DATE, -1); // den Tag davor festsetzen

				vkpfMengenstaffelDtoI.setTPreisgueltigbis(new java.sql.Date(cal
						.getTimeInMillis()));
			}
		}

		// Schritt 5: Wenn es einen Vorgaenger gibt, muss dieser ev. ergaenzt
		// werden
		if (vorgaengerDto != null
				&& vorgaengerDto.getTPreisgueltigbis() == null) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(vkpfMengenstaffelDtoI.getTPreisgueltigab());
			cal.add(Calendar.DATE, -1); // den Tag davor festsetzen

			vorgaengerDto.setTPreisgueltigbis(new java.sql.Date(cal
					.getTimeInMillis()));

			updateVkpfMengenstaffel(vorgaengerDto, theClientDto);
		}
	}

	/**
	 * Pruefen, ob ein Datum in einem Datumsbereich enthalten ist.
	 * 
	 * @param tDatumVonI
	 *            Date
	 * @param tDatumBisI
	 *            Date
	 * @param tDatumEnthaltenI
	 *            Date
	 * @param theClientDto
	 *            String
	 * @return boolean true, wenn das Datum enthalten ist
	 * @throws EJBExceptionLP
	 */
	private boolean checkDatumInDatumsbereichEnthalten(Date tDatumVonI,
			Date tDatumBisI, Date tDatumEnthaltenI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		Calendar calIt = new GregorianCalendar();
		calIt.setTime(tDatumVonI);

		boolean bEnthalten = false;

		while (!bEnthalten && tDatumBisI != null
				&& calIt.getTimeInMillis() <= tDatumBisI.getTime()) {
			if (calIt.getTimeInMillis() == tDatumEnthaltenI.getTime()) {
				bEnthalten = true;
			} else {
				calIt.add(Calendar.DATE, 1); // den naechsten Tag pruefen
			}
		}

		return bEnthalten;
	}

	/**
	 * Eine VK Mengenstaffel loeschen.
	 * 
	 * @param vkpfMengenstaffelDtoI
	 *            die zu loeschende VK Mengenstaffel
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public void removeVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkpfMengenstaffelDto(vkpfMengenstaffelDtoI);
		Vkpfmengenstaffel toRemove = em.find(Vkpfmengenstaffel.class,
				vkpfMengenstaffelDtoI.getIId());
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

	/**
	 * Eine bestehende VK Mengenstaffel mit neuen Werten aktualisieren.
	 * 
	 * @param vkpfMengenstaffelDtoI
	 *            die VK Mengenstaffel
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @throws EJBExceptionLP
	 */
	public void updateVkpfMengenstaffel(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		checkVkpfMengenstaffelDto(vkpfMengenstaffelDtoI);
		Vkpfmengenstaffel vkpfMengenstaffel = em.find(Vkpfmengenstaffel.class,
				vkpfMengenstaffelDtoI.getIId());
		if (vkpfMengenstaffel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		if (vkpfMengenstaffelDtoI.getTPreisgueltigbis() == null) {
			checkVkpfMengenstaffel(vkpfMengenstaffelDtoI, theClientDto);
		}
		// Daten normieren
		vkpfMengenstaffelDtoI.setTPreisgueltigab(Helper
				.cutDate(vkpfMengenstaffelDtoI.getTPreisgueltigab()));
		vkpfMengenstaffelDtoI.setTPreisgueltigbis(Helper
				.cutDate(vkpfMengenstaffelDtoI.getTPreisgueltigbis()));

		vkpfMengenstaffelDtoI.setPersonalIIdAendern(theClientDto
				.getIDPersonal());
		vkpfMengenstaffelDtoI.setTAendern(getTimestamp());

		setVkpfMengenstaffelFromVkpfMengenstaffelDto(vkpfMengenstaffel,
				vkpfMengenstaffelDtoI);
	}

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP {
		if (iId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iId == null"));
		}
		Vkpfmengenstaffel vkpfmengenstaffel = em.find(Vkpfmengenstaffel.class,
				iId);
		if (vkpfmengenstaffel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleVkpfMengenstaffelDto(vkpfmengenstaffel);
	}

	private void setVkpfMengenstaffelFromVkpfMengenstaffelDto(
			Vkpfmengenstaffel vkpfMengenstaffel,
			VkpfMengenstaffelDto vkpfMengenstaffelDto) {
		vkpfMengenstaffel.setArtikelIId(vkpfMengenstaffelDto.getArtikelIId());
		vkpfMengenstaffel.setNMenge(vkpfMengenstaffelDto.getNMenge());
		vkpfMengenstaffel.setFArtikelstandardrabattsatz(vkpfMengenstaffelDto
				.getFArtikelstandardrabattsatz());
		vkpfMengenstaffel.setNArtikelfixpreis(vkpfMengenstaffelDto
				.getNArtikelfixpreis());
		vkpfMengenstaffel.setTPreisgueltigab(vkpfMengenstaffelDto
				.getTPreisgueltigab());
		vkpfMengenstaffel.setTPreisgueltigbis(vkpfMengenstaffelDto
				.getTPreisgueltigbis());
		vkpfMengenstaffel.setPersonalIIdAendern(vkpfMengenstaffelDto
				.getPersonalIIdAendern());
		vkpfMengenstaffel.setTAendern(vkpfMengenstaffelDto.getTAendern());
		vkpfMengenstaffel.setVkpfartikelpreislisteIId(vkpfMengenstaffelDto
				.getVkpfartikelpreislisteIId());
		vkpfMengenstaffel.setBAllepreislisten(vkpfMengenstaffelDto
				.getBAllepreislisten());
		em.merge(vkpfMengenstaffel);
		em.flush();
	}

	private VkpfMengenstaffelDto assembleVkpfMengenstaffelDto(
			Vkpfmengenstaffel vkpfMengenstaffel) {
		return VkpfMengenstaffelDtoAssembler.createDto(vkpfMengenstaffel);
	}

	private VkpfMengenstaffelDto[] assembleVkpfMengenstaffelDtos(
			Collection<?> vkpfMengenstaffels, Integer preislisteIId) {

		if (preislisteIId == null) {
			List<VkpfMengenstaffelDto> vkpreisbasisPreisliste = new ArrayList<VkpfMengenstaffelDto>();
			if (vkpfMengenstaffels != null) {
				ArrayList<BigDecimal> aVerwendeteStaffelmengenPreisliste = new ArrayList<BigDecimal>();
				Iterator<?> iterator = vkpfMengenstaffels.iterator();
				while (iterator.hasNext()) {
					Vkpfmengenstaffel vkpfMengenstaffel = (Vkpfmengenstaffel) iterator
							.next();
					// Pro Menge nur letztgueltige Mengenstaffel, Werte sind
					// bereits
					// nach Menge ASC und Gueltig ab DESC sortiert
					if (vkpfMengenstaffel.getNMenge() != null) {
						// nur hinzufuegen, wenn diese Menge noch nicht
						// vorhanden
						// ist
						if (!aVerwendeteStaffelmengenPreisliste
								.contains(vkpfMengenstaffel.getNMenge())) {

							if (vkpfMengenstaffel.getVkpfartikelpreislisteIId() == null) {

								aVerwendeteStaffelmengenPreisliste
										.add(vkpfMengenstaffel.getNMenge());
								vkpreisbasisPreisliste
										.add(assembleVkpfMengenstaffelDto(vkpfMengenstaffel));
							}

						}
					}

				}
			}
			VkpfMengenstaffelDto[] returnArray = new VkpfMengenstaffelDto[vkpreisbasisPreisliste
					.size()];
			return (VkpfMengenstaffelDto[]) vkpreisbasisPreisliste
					.toArray(returnArray);
		} else {
			List<VkpfMengenstaffelDto> listPreisliste = new ArrayList<VkpfMengenstaffelDto>();
			List<VkpfMengenstaffelDto> listAllePreislisten = new ArrayList<VkpfMengenstaffelDto>();
			List<VkpfMengenstaffelDto> listVKpreisbasis = new ArrayList<VkpfMengenstaffelDto>();
			if (vkpfMengenstaffels != null) {
				ArrayList<BigDecimal> aVerwendeteStaffelmengenPreisliste = new ArrayList<BigDecimal>();
				ArrayList<BigDecimal> aVerwendeteStaffelmengenAllePreislisten = new ArrayList<BigDecimal>();
				ArrayList<BigDecimal> aVerwendeteStaffelmengenVKpreisbasis = new ArrayList<BigDecimal>();
				Iterator<?> iterator = vkpfMengenstaffels.iterator();
				while (iterator.hasNext()) {
					Vkpfmengenstaffel vkpfMengenstaffel = (Vkpfmengenstaffel) iterator
							.next();
					// Pro Menge nur letztgueltige Mengenstaffel, Werte sind
					// bereits
					// nach Menge ASC und Gueltig ab DESC sortiert
					if (vkpfMengenstaffel.getNMenge() != null) {
						// nur hinzufuegen, wenn diese Menge noch nicht
						// vorhanden
						// ist
						if (!aVerwendeteStaffelmengenPreisliste
								.contains(vkpfMengenstaffel.getNMenge())) {

							if (preislisteIId.equals(vkpfMengenstaffel
									.getVkpfartikelpreislisteIId())
									&& Helper.short2boolean(vkpfMengenstaffel
											.getBAllepreislisten()) == false) {

								aVerwendeteStaffelmengenPreisliste
										.add(vkpfMengenstaffel.getNMenge());
								listPreisliste
										.add(assembleVkpfMengenstaffelDto(vkpfMengenstaffel));
							}

						}

						if (!aVerwendeteStaffelmengenAllePreislisten
								.contains(vkpfMengenstaffel.getNMenge())) {

							if (Helper.short2boolean(vkpfMengenstaffel
									.getBAllepreislisten()) == true) {

								aVerwendeteStaffelmengenAllePreislisten
										.add(vkpfMengenstaffel.getNMenge());
								listAllePreislisten
										.add(assembleVkpfMengenstaffelDto(vkpfMengenstaffel));
							}

						}
						if (!aVerwendeteStaffelmengenVKpreisbasis
								.contains(vkpfMengenstaffel.getNMenge())) {

							if (vkpfMengenstaffel.getVkpfartikelpreislisteIId() == null
									&& Helper.short2boolean(vkpfMengenstaffel
											.getBAllepreislisten()) == false) {

								aVerwendeteStaffelmengenVKpreisbasis
										.add(vkpfMengenstaffel.getNMenge());
								listVKpreisbasis
										.add(assembleVkpfMengenstaffelDto(vkpfMengenstaffel));
							}

						}

					}

				}
			}

			if (listPreisliste.size() > 0) {
				VkpfMengenstaffelDto[] returnArray = new VkpfMengenstaffelDto[listPreisliste
						.size()];
				return (VkpfMengenstaffelDto[]) listPreisliste
						.toArray(returnArray);
			} else if (listVKpreisbasis.size() > 0) {
				VkpfMengenstaffelDto[] returnArray = new VkpfMengenstaffelDto[listVKpreisbasis
						.size()];
				return (VkpfMengenstaffelDto[]) listVKpreisbasis
						.toArray(returnArray);
			} else {
				VkpfMengenstaffelDto[] returnArray = new VkpfMengenstaffelDto[listAllePreislisten
						.size()];
				return (VkpfMengenstaffelDto[]) listAllePreislisten
						.toArray(returnArray);
			}

		}
	}

	/**
	 * Preconditions pruefen.
	 * 
	 * @param vkpfMengenstaffelDtoI
	 *            VkpfMengenstaffelDto
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private void checkVkpfMengenstaffelDto(
			VkpfMengenstaffelDto vkpfMengenstaffelDtoI) throws EJBExceptionLP {
		if (vkpfMengenstaffelDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception());
		}
	}

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByUniqueKey(
			Integer iIdArtikelI, BigDecimal nMengeI, Date datGueltigAbI,
			Integer vkpfartikelpreislisteIId) throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nMengeI == null"));
		}

		if (datGueltigAbI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigAbI == null"));
		}
		Vkpfmengenstaffel vkpfmengenstaffel = null;
		if (vkpfartikelpreislisteIId != null) {
			Query query = em
					.createNamedQuery("VkpfMengenstaffelfindByUniqueKey");
			query.setParameter(1, iIdArtikelI);
			query.setParameter(2, nMengeI.doubleValue());
			query.setParameter(3, datGueltigAbI);
			vkpfmengenstaffel = (Vkpfmengenstaffel) query.getSingleResult();
		} else {
			Query query = em
					.createNamedQuery("VkpfMengenstaffelfindByUniqueKey2");
			query.setParameter(1, iIdArtikelI);
			query.setParameter(2, nMengeI.doubleValue());
			query.setParameter(3, datGueltigAbI);
			vkpfmengenstaffel = (Vkpfmengenstaffel) query.getSingleResult();
		}
		if (vkpfmengenstaffel == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEI_FIND, "");
		}
		return assembleVkpfMengenstaffelDto(vkpfmengenstaffel);
	}

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdNMenge(
			Integer iIdArtikelI, BigDecimal nMengeI, Integer preislisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		Query query = em
				.createNamedQuery("VkpfMengenstaffelfindByArtikelIIdNMenge");
		query.setParameter(1, iIdArtikelI);
		query.setParameter(2, nMengeI);

		return assembleVkpfMengenstaffelDtos(query.getResultList(), null);
	}

	public VkpfMengenstaffelDto vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
			Integer iIdArtikelI, BigDecimal nMengeI,
			Date datGueltigkeitsdatumI, Integer preislisteIId,
			TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nMengeI == null"));
		}

		if (datGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumI == null"));
		}
		VkpfMengenstaffelDto vkpfMengenstaffel = null;
		Query query = em
				.createNamedQuery("VkpfMengenstaffelfindByArtikelIIdNMengeGueltigkeitsdatum");
		query.setParameter(1, iIdArtikelI);
		query.setParameter(2, nMengeI);
		query.setParameter(3, datGueltigkeitsdatumI);
		VkpfMengenstaffelDto[] dtos = assembleVkpfMengenstaffelDtos(
				query.getResultList(), preislisteIId);
		// ORDER BY nMenge
		if (dtos != null && dtos.length > 0) {
			// Kriterium <= nMenge, d.h. die letzte Staffel im Array ist
			// zutreffend
			vkpfMengenstaffel = dtos[dtos.length - 1];
		}
		return vkpfMengenstaffel;
	}

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdGueltigkeitsdatum(
			Integer iIdArtikelI, Date datGueltigkeitI, Integer preislisteIId,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (datGueltigkeitI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumI == null"));
		}

		myLogger.logData("ArtikelIId, Gueltigkeitsdatum: "
				+ iIdArtikelI
				+ ", "
				+ DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN)
						.format(datGueltigkeitI));
		Query query = em
				.createNamedQuery("VkpfMengenstaffelfindByArtikelIIdGueltigkeitsdatum");
		query.setParameter(1, iIdArtikelI);
		query.setParameter(2, datGueltigkeitI);
		return assembleVkpfMengenstaffelDtos(query.getResultList(),
				preislisteIId);
	}

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIId(
			Integer iIdArtikelI, TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		Query query = em.createNamedQuery("VkpfMengenstaffelfindByArtikelIId");
		query.setParameter(1, iIdArtikelI);
		return assembleVkpfMengenstaffelDtos(query.getResultList(), null);
	}

	public VkpfMengenstaffelDto[] vkpfMengenstaffelFindByArtikelIIdFuerVKPreisentwicklung(
			Integer iIdArtikelI, TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		Query query = em.createNamedQuery("VkpfMengenstaffelfindByArtikelIId");
		query.setParameter(1, iIdArtikelI);
		Collection<?> vkpfMengenstaffels = query.getResultList();

		List<VkpfMengenstaffelDto> vkpreisbasisPreisliste = new ArrayList<VkpfMengenstaffelDto>();
		if (vkpfMengenstaffels != null) {
			ArrayList<BigDecimal> aVerwendeteStaffelmengenPreisliste = new ArrayList<BigDecimal>();
			Iterator<?> iterator = vkpfMengenstaffels.iterator();
			while (iterator.hasNext()) {
				Vkpfmengenstaffel vkpfMengenstaffel = (Vkpfmengenstaffel) iterator
						.next();
				// Pro Menge nur letztgueltige Mengenstaffel, Werte sind
				// bereits
				// nach Menge ASC und Gueltig ab DESC sortiert
				if (vkpfMengenstaffel.getNMenge() != null) {
					// nur hinzufuegen, wenn diese Menge noch nicht
					// vorhanden
					// ist
					if (!aVerwendeteStaffelmengenPreisliste
							.contains(vkpfMengenstaffel.getNMenge())) {

						aVerwendeteStaffelmengenPreisliste
								.add(vkpfMengenstaffel.getNMenge());
						vkpreisbasisPreisliste
								.add(assembleVkpfMengenstaffelDto(vkpfMengenstaffel));

					}
				}

			}
		}
		VkpfMengenstaffelDto[] returnArray = new VkpfMengenstaffelDto[vkpreisbasisPreisliste
				.size()];
		return (VkpfMengenstaffelDto[]) vkpreisbasisPreisliste
				.toArray(returnArray);

	}

	/**
	 * Preisbasis als Bezugsgroesse fuer die Ermittlung von Preisen aufgrund
	 * eines Rabattsatzes. <br>
	 * Es gilt: Aktueller VK-Preis des Artikels > Gestehungspreis am Hauptlager
	 * des Mandanten > 0.0
	 * 
	 * PJ 15000: Ist Mandantenparameter PARAMETER_VKPREISBASIS_IST_LIEF1PREIS==1
	 * kommt die Preisbasis vom 1. Lieferantenpreis
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param datGueltigkeitsdatumVkbasisII
	 *            Gueltigkeitsdatum der VK-Basis
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal die Preisbasis des Artikels
	 */
	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, Integer preislisteIId,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (datGueltigkeitsdatumVkbasisII == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumVkbasisII == null"));
		}

		// Schritt 1: Die Preisbasis mit NULL initialisieren
		BigDecimal nPreisbasis = null;

		try {
			ParametermandantDto p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);

			// Abhaengig vom Parameter
			if (p != null && p.getCWert().equals("1")) {
				// Schritt 2: Preis vom Artikellieferanten
				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(iIdArtikelI, null,
								new BigDecimal(0),
								theClientDto.getSMandantenwaehrung(),
								datGueltigkeitsdatumVkbasisII, theClientDto);

				if (alDto != null && alDto.getLief1Preis() != null) {
					nPreisbasis = alDto.getLief1Preis();
				} else {
					// Schritt 3: Wenn es keine gueltige VK-Basis gibt, dann
					// gilt
					// der Gestehungspreis.
					nPreisbasis = getGemittelterGestehungspreisAmHauptlagerDesMandanten(
							iIdArtikelI, theClientDto);
				}

				try {
					nPreisbasis = getLocaleFac()
							.rechneUmInAndereWaehrungZuDatum(nPreisbasis,
									theClientDto.getSMandantenwaehrung(),
									waehrungCNrZielwaehrung,
									datGueltigkeitsdatumVkbasisII, theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			} else {

				// Schritt 2: Wurde fuer den gewuenschten Zeitpunkt eine
				// VK-Basis
				// hinterlegt?
				VkPreisfindungEinzelverkaufspreisDto vkbasisDto = getArtikeleinzelverkaufspreis(
						iIdArtikelI, datGueltigkeitsdatumVkbasisII,
						waehrungCNrZielwaehrung, theClientDto);

				if (preislisteIId == null) {

					if (vkbasisDto != null) {
						nPreisbasis = vkbasisDto.getNVerkaufspreisbasis();

					} else {

						// PJ 15176 Wenn Kein VK-Preis definiert ist, dann gibts
						// auch keinen, auch nicht den Gestehungspreis

						// Schritt 3: Wenn es keine gueltige VK-Basis gibt, dann
						// gilt
						// der Gestehungspreis.

						// nPreisbasis =
						// getGemittelterGestehungspreisAmHauptlagerDesMandanten(
						// iIdArtikelI, theClientDto);
					}

				} else {
					VkPreisfindungPreislisteDto vkPreisfindungPreislisteDto = getVkPreisfindungPreislisteFindByArtikelIIdPreislisteIIdTPreisgueltigab(
							iIdArtikelI, preislisteIId,
							datGueltigkeitsdatumVkbasisII, theClientDto);

					VkpfartikelpreislisteDto plDto = vkpfartikelpreislisteFindByPrimaryKey(preislisteIId);

					if (vkPreisfindungPreislisteDto != null) {

						if (vkPreisfindungPreislisteDto.getNArtikelfixpreis() != null) {
							nPreisbasis = vkPreisfindungPreislisteDto
									.getNArtikelfixpreis();

							try {
								nPreisbasis = getLocaleFac()
										.rechneUmInAndereWaehrungZuDatum(
												nPreisbasis,
												plDto.getWaehrungCNr(),
												waehrungCNrZielwaehrung,
												datGueltigkeitsdatumVkbasisII,
												theClientDto);
							} catch (RemoteException e) {
								throwEJBExceptionLPRespectOld(e);
							}

						} else {
							if (vkbasisDto != null) {

								BigDecimal rabattsumme = vkbasisDto
										.getNVerkaufspreisbasis()
										.multiply(
												vkPreisfindungPreislisteDto
														.getNArtikelstandardrabattsatz()
														.movePointLeft(2));
								BigDecimal nettopreis = vkbasisDto
										.getNVerkaufspreisbasis().subtract(
												rabattsumme);

								nPreisbasis = nettopreis;

							}
						}
					} else {
						if (vkbasisDto != null) {
							nPreisbasis = vkbasisDto.getNVerkaufspreisbasis();

						}

					}

				}

			}

		}
		/**
		 * @todo saubere Fehlerbehandlung!
		 */
		catch (Throwable t) {
			myLogger.error("Die Preisbasis konnte nicht ermittelt werden.", t);
		}
		return nPreisbasis;
	}

	/**
	 * Preisbasis als Bezugsgroesse fuer die Ermittlung von Preisen aufgrund
	 * eines Rabattsatzes in Abh&auml;ngigkeit von der Menge. <br>
	 * 
	 * PJ 15000: Ist Mandantenparameter PARAMETER_VKPREISBASIS_IST_LIEF1PREIS==1
	 * kommt die Preisbasis vom 1. Lieferantenpreis
	 * 
	 * PJ 15314: auch die Lieferaneten EK Staffel wird ber&uuml;cksichtigt
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param datGueltigkeitsdatumVkbasisII
	 *            Gueltigkeitsdatum der VK-Basis
	 * @param nMengeI
	 *            Menge bei Preisbasis aus Lieferantenpreis
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal die Preisbasis des Artikels
	 */
	public BigDecimal ermittlePreisbasis(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, BigDecimal nMengeI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (datGueltigkeitsdatumVkbasisII == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumVkbasisII == null"));
		}

		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nMengeI == null"));
		}

		ParametermandantDto p = null;
		try {
			p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		} catch (RemoteException e) {
			//
		} catch (EJBExceptionLP e) {
			//
		}
		if ((p == null) || p.getCWert().equals("0")
				|| (nMengeI.doubleValue() == 1)) {

			int preisbasisVerkauf = 0;

			try {
				ParametermandantDto p2 = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

				preisbasisVerkauf = (Integer) p2.getCWertAsObject();

			} catch (RemoteException e1) {
				throwEJBExceptionLPRespectOld(e1);
			}

			if (preisbasisVerkauf == 0 || preisbasisVerkauf == 2) {
				preislisteIId = null;
			}

			return ermittlePreisbasis(iIdArtikelI,
					datGueltigkeitsdatumVkbasisII, preislisteIId,
					waehrungCNrZielwaehrung, theClientDto);
		}

		// Schritt 1: Die Preisbasis mit 0 initialisieren
		BigDecimal nPreisbasis = new BigDecimal(0);

		// Schritt 2: Preis vom Artikellieferanten
		try {
			ArtikellieferantDto alDto = getArtikelFac()
					.getArtikelEinkaufspreis(iIdArtikelI, null, nMengeI,
							theClientDto.getSMandantenwaehrung(),
							datGueltigkeitsdatumVkbasisII, theClientDto);

			if (alDto != null && alDto.getLief1Preis() != null) {
				nPreisbasis = alDto.getLief1Preis();
			} else {
				// Schritt 3: Wenn es keine gueltige VK-Basis gibt, dann
				// gilt
				// der Gestehungspreis.
				nPreisbasis = getGemittelterGestehungspreisAmHauptlagerDesMandanten(
						iIdArtikelI, theClientDto);
			}
		} catch (Throwable t) {
			myLogger.error("Die Preisbasis konnte nicht ermittelt werden.", t);
		}

		// In Zielwaehrung umrechnen
		if (nPreisbasis != null) {
			try {
				nPreisbasis = getLocaleFac().rechneUmInAndereWaehrungZuDatum(
						nPreisbasis, theClientDto.getSMandantenwaehrung(),
						waehrungCNrZielwaehrung, datGueltigkeitsdatumVkbasisII,
						theClientDto);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		return nPreisbasis;
	}

	public BigDecimal ermittlePreisbasisStaffelmenge(Integer iIdArtikelI,
			Date datGueltigkeitsdatumVkbasisII, BigDecimal nMengeI,
			TheClientDto theClientDto) {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		if (datGueltigkeitsdatumVkbasisII == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumVkbasisII == null"));
		}

		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nMengeI == null"));
		}

		ParametermandantDto p = null;
		try {
			p = getParameterFac().getMandantparameter(
					theClientDto.getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_VKPREISBASIS_IST_LIEF1PREIS);
		} catch (RemoteException e) {
			//
		} catch (EJBExceptionLP e) {
			//
		}

		if ((p == null) || p.getCWert().equals("0")) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"Parameter PARAMETER_VKPREISBASIS_IST_LIEF1PREIS nicht gesetzt oder 0"));
		}

		// Schritt 1: Die Menge mit 1 initialisieren
		BigDecimal nMenge = new BigDecimal(1);

		if (nMengeI.doubleValue() > 1) {
			// Schritt 2: Menge vom Artikellieferanten
			try {
				ArtikellieferantDto alDto = getArtikelFac()
						.getArtikelEinkaufspreis(iIdArtikelI, null, nMengeI,
								theClientDto.getSMandantenwaehrung(),
								datGueltigkeitsdatumVkbasisII, theClientDto);

				if (alDto != null && alDto.getNStaffelmenge() != null) {
					nMenge = alDto.getNStaffelmenge();
				}
			} catch (Throwable t) {
				myLogger.error(
						"Die Staffelmenge konnte nicht ermittelt werden.", t);
			}
		}
		return nMenge;
	}

	/**
	 * Den gemittelten Gestehungspreis am Hauptlager des Mandanten fuer einen
	 * bestimmten Artikel bestimmen.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return BigDecimal der ermittelte Gestehungspreis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private BigDecimal getGemittelterGestehungspreisAmHauptlagerDesMandanten(
			Integer iIdArtikelI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		BigDecimal nGestehungspreis = new BigDecimal(0);
		try {
			LagerDto hauptlagerDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);

			if (hauptlagerDto == null) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_LAGER_HAUPTLAGERDESMANDANTEN_NICHT_ANGELEGT,
						new Exception(
								"Das Hauptlager des Mandanten ist nicht definiert."));
			}
			nGestehungspreis = getLagerFac()
					.getGemittelterGestehungspreisEinesLagers(iIdArtikelI,
							hauptlagerDto.getIId(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return nGestehungspreis;
	}

	/**
	 * Aufruf der Helium V Verkaufspreisfindung. Ueber einen Algorithmus wird
	 * der Vorschlagswert fuer den Verkaufspreis eines Artikels bestimmt. Die
	 * VKPF besteht aus drei Stufen, das Ergebnis aller drei Stufen wird
	 * bestimmt, da der Benutzer alternativ einen VK-Preis manuell waehlen kann.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param iIdKundeI
	 *            PK des Kunden, fuer den ein Preis ermittelt wird
	 * @param nMengeI
	 *            welche Menge des Artikls wird verkauft
	 * @param datGueltigkeitsdatumI
	 *            zu welchem Datum soll der VKP ermittelt werden
	 * @param iIdPreislisteI
	 *            PK der Preisliste, die der Benutzer gewaehlt hat
	 * @param iIdMwstsatzI
	 *            PK der Mwst, die der Benutzer gewaehlt hat
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VkpreisfindungDto die ermittelten VK Informationen
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */

	public VkpreisfindungDto verkaufspreisfindung(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto) {
		return verkaufspreisfindung(iIdArtikelI, iIdKundeI, nMengeI,
				datGueltigkeitsdatumI, iIdPreislisteI, iIdMwstsatzI,
				waehrungCNrZielwaehrung, true, theClientDto);
	}

	public VkpreisfindungDto verkaufspreisfindung(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung,
			boolean bMitMaterialzuschlagImNettopreis, TheClientDto theClientDto) {
		Validator.notNull(iIdArtikelI, "iIdArtikelI");
		Validator.notNull(iIdKundeI, "iIdKundeI");
		Validator.notNull(iIdMwstsatzI, "iIdMwstsatzI");

		return verkaufspreisfindungImpl(iIdArtikelI, iIdKundeI, nMengeI,
				datGueltigkeitsdatumI, iIdPreislisteI, iIdMwstsatzI,
				waehrungCNrZielwaehrung, bMitMaterialzuschlagImNettopreis,
				theClientDto);
	}

	public VkpreisfindungDto verkaufspreisfindungWeb(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung, TheClientDto theClientDto)
			throws RemoteException {
		Validator.notNull(iIdArtikelI, "iIdArtikelI");

		if (iIdMwstsatzI == null) {
			if (iIdKundeI != null) {
				Kunde kunde = em.find(Kunde.class, iIdKundeI);
				iIdMwstsatzI = kunde.getMwstsatzIId();
			} else {
				MandantDto mandantDto = getMandantFac()
						.mandantFindByPrimaryKey(theClientDto.getMandant(),
								theClientDto);
				iIdMwstsatzI = mandantDto
						.getMwstsatzbezIIdStandardinlandmwstsatz();
			}
		}

		Validator.notNull(iIdMwstsatzI, "iIdMwstsatzI");
		return verkaufspreisfindungImpl(iIdArtikelI, iIdKundeI, nMengeI,
				datGueltigkeitsdatumI, iIdPreislisteI, iIdMwstsatzI,
				waehrungCNrZielwaehrung, false, theClientDto);
	}

	private VkpreisfindungDto verkaufspreisfindungImpl(Integer iIdArtikelI,
			Integer iIdKundeI, BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			Integer iIdPreislisteI, Integer iIdMwstsatzI,
			String waehrungCNrZielwaehrung,
			boolean bMitMaterialzuschlagImNettopreis, TheClientDto theClientDto) {
		VkpreisfindungDto vkpreisfindungDto = new VkpreisfindungDto(
				theClientDto.getLocUi());

		try {
			// Beim Artikel ist hinterlegt, ob ein eventueller Rabatt fuer den
			// Kunden
			// ausgewiesen werden soll
			ArtikelDto artikelDto = getArtikelFac().artikelFindByPrimaryKey(
					iIdArtikelI, theClientDto);

			BigDecimal bdMaterialzuschlag = new BigDecimal(0);
			if (bMitMaterialzuschlagImNettopreis == true) {
				if (artikelDto.getMaterialIId() != null
						&& artikelDto.getFMaterialgewicht() != null) {
					bdMaterialzuschlag = getMaterialFac()
							.getMaterialzuschlagVKInZielwaehrung(iIdArtikelI,
									datGueltigkeitsdatumI,
									waehrungCNrZielwaehrung, theClientDto);
				}

				vkpreisfindungDto.setNMaterialzuschlag(bdMaterialzuschlag);
			}

			// VKPF 2: Stufe 3 = Ermittlung des VKP ueber die
			// Kundensonderkonditionen
			if (iIdKundeI != null) {
				vkpreisfindungDto = verkaufspreisfindungStufe3(artikelDto,
						iIdKundeI, nMengeI, datGueltigkeitsdatumI,
						vkpreisfindungDto, iIdMwstsatzI, iIdPreislisteI,
						waehrungCNrZielwaehrung, theClientDto);
			}

			// VKPF 3: Stufe 2 = Ermittlung des VKP ueber die VK-Mengenstaffeln
			vkpreisfindungDto = verkaufspreisfindungStufe2(artikelDto, nMengeI,
					datGueltigkeitsdatumI, vkpreisfindungDto, iIdMwstsatzI,
					iIdPreislisteI, waehrungCNrZielwaehrung, theClientDto);

			// VKPF 4: Stufe 1 = Ermittlung des VKP ueber die Kundepreisliste
			if (iIdPreislisteI != null) {
				vkpreisfindungDto = verkaufspreisfindungStufe1(artikelDto,
						datGueltigkeitsdatumI, iIdPreislisteI,
						vkpreisfindungDto, iIdMwstsatzI, nMengeI,
						waehrungCNrZielwaehrung, theClientDto);
			}

			// VKPF: 5 im schlimmsten Fall wird der VKPF mit 0 initialisiert
			VerkaufspreisDto verkaufspreisDto = new VerkaufspreisDto();

			BigDecimal nPreisbasis = ermittlePreisbasis(artikelDto.getIId(),
					datGueltigkeitsdatumI, nMengeI, null,
					waehrungCNrZielwaehrung, theClientDto);

			if (nPreisbasis != null) {
				// VKPF: 6 Wenn kein Verkaufspreis ermittelt werden konnte, gilt
				// die Preisbasis
				// mit Rabattsatz 0
				verkaufspreisDto.einzelpreis = nPreisbasis;
				verkaufspreisDto.rabattsatz = new Double(0);
				verkaufspreisDto.rabattsumme = new BigDecimal(0);
				verkaufspreisDto.nettopreis = verkaufspreisDto.einzelpreis;

				verkaufspreisDto = berechneBruttopreis(verkaufspreisDto,
						iIdMwstsatzI, theClientDto);

				vkpreisfindungDto.setVkpPreisbasis(verkaufspreisDto);

				if (vkpreisfindungDto.getVkpreisberechnetStufe() == null) {
					vkpreisfindungDto
							.setVkpreisberechnetStufe(VkpreisfindungDto.VKPFPREISBASIS);
				}
			}

			// VKPF: 7 auch der niedrigst moegliche VK-Preis wird hinterlegt,
			// wenn er dem Benutzer angezeigt werden soll
			ParametermandantDto pmGeringerenVKPreisVorschlagen = null;

			pmGeringerenVKPreisVorschlagen = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_GERINGEREN_VKPREIS_VORSCHLAGEN);

			if (Helper.short2boolean(Short
					.parseShort(pmGeringerenVKPreisVorschlagen.getCWert()))) {
				// VKPF 8: die hinterlegten VK-Preise vergleichen
				VerkaufspreisDto niedrigsterVKPreis = null;

				if (vkpreisfindungDto.getVkpPreisbasis() != null) {
					vkpreisfindungDto
							.setVkpreisminimalStufe(VkpreisfindungDto.VKPFPREISBASIS);
					niedrigsterVKPreis = vkpreisfindungDto.getVkpPreisbasis();
				}

				if ((niedrigsterVKPreis == null && vkpreisfindungDto
						.getVkpStufe1() != null)
						|| (vkpreisfindungDto.getVkpStufe1() != null && vkpreisfindungDto
								.getVkpStufe1().getNetto2Compare()
								.doubleValue() <= niedrigsterVKPreis
								.getNetto2Compare().doubleValue())) {
					vkpreisfindungDto
							.setVkpreisminimalStufe(VkpreisfindungDto.VKPFSTUFE1);
					niedrigsterVKPreis = vkpreisfindungDto.getVkpStufe1();
				}

				if ((niedrigsterVKPreis == null && vkpreisfindungDto
						.getVkpStufe2() != null)
						|| (vkpreisfindungDto.getVkpStufe2() != null && vkpreisfindungDto
								.getVkpStufe2().getNetto2Compare()
								.doubleValue() <= niedrigsterVKPreis
								.getNetto2Compare().doubleValue())) {
					vkpreisfindungDto
							.setVkpreisminimalStufe(VkpreisfindungDto.VKPFSTUFE2);
					niedrigsterVKPreis = vkpreisfindungDto.getVkpStufe2();
				}

				if ((niedrigsterVKPreis == null && vkpreisfindungDto
						.getVkpStufe3() != null)
						|| (vkpreisfindungDto.getVkpStufe3() != null && vkpreisfindungDto
								.getVkpStufe3().getNetto2Compare()
								.doubleValue() <= niedrigsterVKPreis
								.getNetto2Compare().doubleValue())) {
					vkpreisfindungDto
							.setVkpreisminimalStufe(VkpreisfindungDto.VKPFSTUFE3);
					niedrigsterVKPreis = vkpreisfindungDto.getVkpStufe3();
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return vkpreisfindungDto;
	}

	/**
	 * Mwst beruecksichtigen und Bruttopreis berechnen; die Mwst muss am Server
	 * berechnet werden, weil es moeglich ist, dass der Benutzer am Client keine
	 * Neuberechnung der Preisfelder durch FocusLost ausloest, sondern gleich
	 * zum Speichern geht.
	 * 
	 * @param verkaufspreisDtoI
	 *            VerkaufspreisDto
	 * @param iIdMwstsatzI
	 *            die zu beruecksichtigende Mwst
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VerkaufspreisDto
	 * @throws EJBExceptionLP
	 */
	private VerkaufspreisDto berechneBruttopreis(
			VerkaufspreisDto verkaufspreisDtoI, Integer iIdMwstsatzI,
			TheClientDto theClientDto) throws EJBExceptionLP {
		if (verkaufspreisDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("verkaufspreisDtoI == null"));
		}

		try {
			verkaufspreisDtoI.mwstsatzIId = iIdMwstsatzI;

			verkaufspreisDtoI.mwstsumme = verkaufspreisDtoI.nettopreis
					.multiply(new BigDecimal(getMandantFac()
							.mwstsatzFindByPrimaryKey(iIdMwstsatzI,
									theClientDto).getFMwstsatz().doubleValue())
							.movePointLeft(2));

			verkaufspreisDtoI.bruttopreis = verkaufspreisDtoI.nettopreis
					.add(verkaufspreisDtoI.mwstsumme);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return verkaufspreisDtoI;
	}

	/**
	 * Verkaufspreisdfindung Stufe 1. <br>
	 * Der VKP wird aufgrund einer Kundenpreisliste bestimmt.
	 * 
	 * @param artikelDtoI
	 *            der aktuelle Artikel
	 * @param tGueltigkeitsdatumI
	 *            Gueltigkeitsdatum fuer die VK-Basis
	 * @param iIdPreislisteI
	 *            PK der Preisliste
	 * @param vkpreisfindungDtoI
	 *            die Preisdetails
	 * @param iIdMwstsatzI
	 *            PK des Mwstsatzes
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VkpreisfindungDto die Preisdetails
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public VkpreisfindungDto verkaufspreisfindungStufe1(ArtikelDto artikelDtoI,
			Date tGueltigkeitsdatumI, Integer iIdPreislisteI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			BigDecimal nMengeI, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) {
		if (iIdPreislisteI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdPreislisteI == null"));
		}

		if (tGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tGueltigkeitsdatumI == null"));
		}

		if (vkpreisfindungDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("vkpreisfindungDtoI == null"));
		}

		VerkaufspreisDto verkaufspreisDto = null;

		// Stufe1: 0 Ist fuer den Artikel eine aktuelle Preisliste hinterlegt?
		VkPreisfindungPreislisteDto artikelPreisliste = null;

		try {
			artikelPreisliste = getAktuellePreislisteByArtikelIIdPreislisteIId(
					artikelDtoI.getIId(), iIdPreislisteI,
					new Date(System.currentTimeMillis()),
					waehrungCNrZielwaehrung, theClientDto);
		}
		/**
		 * @todo saubere Fehlerbehandlung
		 */
		catch (Throwable t) {
			// ignore
		}

		if (artikelPreisliste != null) {
			BigDecimal nPreisbasis = ermittlePreisbasis(artikelDtoI.getIId(),
					tGueltigkeitsdatumI, nMengeI, iIdPreislisteI,
					waehrungCNrZielwaehrung, theClientDto);

			// Stufe1: 1 Preisliste Fixpreis > Rabattsatz
			if (artikelPreisliste.getNArtikelfixpreis() != null) {
				verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
						artikelPreisliste.getNArtikelfixpreis(),
						vkpreisfindungDtoI.getNMaterialzuschlag());
			} else {
				// Stufe1: 2 Wenn ein Rabattsatz hinterlegt wurde, wird der
				// Nettoeinzelpreis berechnet
				int preisbasisVerkauf = 0;

				try {
					ParametermandantDto p2 = getParameterFac()
							.getMandantparameter(theClientDto.getMandant(),
									ParameterFac.KATEGORIE_KUNDEN,
									ParameterFac.PARAMETER_PREISBASIS_VERKAUF);

					preisbasisVerkauf = (Integer) p2.getCWertAsObject();

				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				if (preisbasisVerkauf == 0 || preisbasisVerkauf == 2) {
					verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
							artikelPreisliste.getNArtikelstandardrabattsatz()
									.doubleValue(),
							vkpreisfindungDtoI.getNMaterialzuschlag());
				} else {
					verkaufspreisDto = berechneVerkaufspreis(nPreisbasis, 0D,
							vkpreisfindungDtoI.getNMaterialzuschlag());
				}
			}

			// Stufe1: 3 Wenn VKPF Stufe 1 ein Ergebnis liefert, dann wird das
			// entsprechend hinterlegt
			if (verkaufspreisDto != null) {
				Vkpfartikelpreisliste vkpal = em.find(
						Vkpfartikelpreisliste.class, iIdPreislisteI);
				if (vkpal != null)
					if (!(vkpal.getWaehrungCNr().equals(theClientDto
							.getSMandantenwaehrung()))) {
						verkaufspreisDto.waehrungCNr = vkpal.getWaehrungCNr();
						try {
							verkaufspreisDto.tempKurs = getLocaleFac()
									.getWechselkurs2(
											theClientDto
													.getSMandantenwaehrung(),
											vkpal.getWaehrungCNr(),
											theClientDto);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}

				verkaufspreisDto = berechneBruttopreis(verkaufspreisDto,
						iIdMwstsatzI, theClientDto);

				vkpreisfindungDtoI.setVkpStufe1(verkaufspreisDto);

				if (vkpreisfindungDtoI.getVkpreisberechnetStufe() == null) {
					vkpreisfindungDtoI
							.setVkpreisberechnetStufe(VkpreisfindungDto.VKPFSTUFE1);
				}
			}
		}

		return vkpreisfindungDtoI;
	}

	/**
	 * Verkaufspreisfindung Stufe 2. <br>
	 * Fuer einen Artikel koennen Mengenstaffeln mit einer bestimmten
	 * Gueltigkeitsdauer erfasst werden. Es gilt: Fixpreis > rabattbehaftete
	 * Preisbasis.
	 * 
	 * @param artikelDtoI
	 *            fuer welchen Artikel wird der VKPF ermittelt
	 * @param nMengeI
	 *            die gewaehlte Menge des Artikels
	 * @param datGueltigkeitsdatumI
	 *            zu welchem Datum wird der VKPF ermittelt
	 * @param vkpreisfindungDtoI
	 *            der Ergebniscontainer fuer die VKPF
	 * @param iIdMwstsatzI
	 *            der zu beruecksichtigende Mwstsatz
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VerkaufspreisDto der ermittelte VK-Preis
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public VkpreisfindungDto verkaufspreisfindungStufe2(ArtikelDto artikelDtoI,
			BigDecimal nMengeI, Date datGueltigkeitsdatumI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) {
		if (datGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("datGueltigkeitsdatumI == null"));
		}

		if (vkpreisfindungDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("vkpreisfindungDtoI == null"));
		}

		VerkaufspreisDto verkaufspreisDto = null;

		// Stufe2: 0 Ist fuer den Artikel zum gewuenschten Zeitpunkt fuer die
		// gewaehlte Menge eine Mengenstaffel hinterlegt?
		VkpfMengenstaffelDto vkpfmengenstaffelDto = null;

		vkpfmengenstaffelDto = vkpfMengenstaffelFindByArtikelIIdNMengeGueltigkeitsdatum(
				artikelDtoI.getIId(), nMengeI, datGueltigkeitsdatumI,
				preislisteIId, theClientDto);

		if (vkpfmengenstaffelDto != null) {
			Integer preislisteIIdFuerBasis = preislisteIId;

			if (vkpfmengenstaffelDto.getVkpfartikelpreislisteIId() != null) {
				preislisteIIdFuerBasis = vkpfmengenstaffelDto
						.getVkpfartikelpreislisteIId();
			}

			ParametermandantDto p = null;
			try {
				p = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			int iPreisbasis = (Integer) p.getCWertAsObject();

			if (iPreisbasis == 2) {

				BigDecimal nPreisbasisFuerEinStueck = ermittlePreisbasis(
						artikelDtoI.getIId(), datGueltigkeitsdatumI,
						new BigDecimal(1), null, waehrungCNrZielwaehrung,
						theClientDto);

				BigDecimal nPreisbasisFuerGesamtmenge = ermittlePreisbasis(
						artikelDtoI.getIId(), datGueltigkeitsdatumI,
						preislisteIId, waehrungCNrZielwaehrung, theClientDto);

				// Stufe2: 1 Wenn ein Fixpreis hinterlegt wurde, wird der
				// Rabattsatz
				// berechnet
				if (vkpfmengenstaffelDto.getNArtikelfixpreis() != null) {
					verkaufspreisDto = berechneVerkaufspreis(
							nPreisbasisFuerGesamtmenge,
							vkpfmengenstaffelDto.getNArtikelfixpreis(),
							vkpreisfindungDtoI.getNMaterialzuschlag());
				} else {
					// Stufe2: 2 Wenn ein Rabattsatz hinterlegt wurde, wird der
					// Nettoeinzelpreis berechnet
					verkaufspreisDto = berechneVerkaufspreis(
							nPreisbasisFuerEinStueck,
							nPreisbasisFuerGesamtmenge,
							vkpreisfindungDtoI.getNMaterialzuschlag());

					VerkaufspreisDto verkaufspreisDtoFuerZusatzrabatt = berechneVerkaufspreis(
							nPreisbasisFuerGesamtmenge,
							vkpfmengenstaffelDto
									.getFArtikelstandardrabattsatz());

					if (verkaufspreisDtoFuerZusatzrabatt != null
							&& verkaufspreisDto != null) {
						verkaufspreisDto
								.setDdZusatzrabattsatz(verkaufspreisDtoFuerZusatzrabatt.rabattsatz);
						verkaufspreisDto
								.setNZusatzrabattsumme(verkaufspreisDtoFuerZusatzrabatt.rabattsumme);

						verkaufspreisDto.nettopreis = verkaufspreisDto.nettopreis
								.subtract(verkaufspreisDtoFuerZusatzrabatt.rabattsumme);

					}

				}
			} else {
				BigDecimal nPreisbasis = ermittlePreisbasis(
						artikelDtoI.getIId(), datGueltigkeitsdatumI, nMengeI,
						preislisteIIdFuerBasis, waehrungCNrZielwaehrung,
						theClientDto);

				// Stufe2: 1 Wenn ein Fixpreis hinterlegt wurde, wird der
				// Rabattsatz
				// berechnet
				if (vkpfmengenstaffelDto.getNArtikelfixpreis() != null) {
					verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
							vkpfmengenstaffelDto.getNArtikelfixpreis(),
							vkpreisfindungDtoI.getNMaterialzuschlag());
				} else {
					// Stufe2: 2 Wenn ein Rabattsatz hinterlegt wurde, wird der
					// Nettoeinzelpreis berechnet
					verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
							vkpfmengenstaffelDto
									.getFArtikelstandardrabattsatz(),
							vkpreisfindungDtoI.getNMaterialzuschlag());
				}
			}
		}

		// Stufe2: 3 Wenn VKPF Stufe 2 ein Ergebnis liefert, dann wird das
		// entsprechend hinterlegt
		if (verkaufspreisDto != null) {
			verkaufspreisDto = berechneBruttopreis(verkaufspreisDto,
					iIdMwstsatzI, theClientDto);

			vkpreisfindungDtoI.setVkpStufe2(verkaufspreisDto);

			if (vkpreisfindungDtoI.getVkpreisberechnetStufe() == null) {
				vkpreisfindungDtoI
						.setVkpreisberechnetStufe(VkpreisfindungDto.VKPFSTUFE2);
			}
		}
		return vkpreisfindungDtoI;
	}

	/**
	 * Gegeben ist eine Preisbasis (= Einzelpreis) und ein Fixpreis (=
	 * Nettoeinzelpreis). Zu berechnen ist der Rabattsatz, der Zusatzrabattsatz
	 * bleibt auf 0.
	 * 
	 * @param nPreisbasisI
	 *            Einzelpreis
	 * @param nFixpreisI
	 *            Nettoeinzelpreis
	 * @return VerkaufspreisDto VKP
	 * @throws EJBExceptionLP
	 */
	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI) throws EJBExceptionLP {

		return berechneVerkaufspreis(nPreisbasisI, nFixpreisI, null);
	}

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			BigDecimal nFixpreisI, BigDecimal nMaterialzuschlag)
			throws EJBExceptionLP {
		VerkaufspreisDto verkaufspreisDto = new VerkaufspreisDto();

		verkaufspreisDto.einzelpreis = nPreisbasisI;

		verkaufspreisDto.nettopreis = nFixpreisI;
		verkaufspreisDto.bKommtVonFixpreis = true;

		// VF 02.01.08 Wenn keine VK-Preisbasis hinterlegt ist, dann gilt der
		// Nettopreis auch als Einzelpreis.
		if (verkaufspreisDto.einzelpreis != null
				&& verkaufspreisDto.einzelpreis.compareTo(new BigDecimal(0)) == 0) {
			verkaufspreisDto.einzelpreis = verkaufspreisDto.nettopreis;
		}

		if (nFixpreisI != null && nPreisbasisI == null) {
			verkaufspreisDto.einzelpreis = nFixpreisI;
		}

		verkaufspreisDto.rabattsumme = verkaufspreisDto.einzelpreis
				.subtract(verkaufspreisDto.nettopreis);

		if (verkaufspreisDto.einzelpreis.doubleValue() != 0.0) {
			verkaufspreisDto.rabattsatz = new Double(
					verkaufspreisDto.rabattsumme.doubleValue()
							/ verkaufspreisDto.einzelpreis.doubleValue() * 100);
		}

		if (nMaterialzuschlag != null) {
			verkaufspreisDto.nettopreis = nFixpreisI.add(nMaterialzuschlag);
			verkaufspreisDto.bdMaterialzuschlag = nMaterialzuschlag;
		}

		return verkaufspreisDto;
	}

	public BigDecimal berechneEinzelVkpreisEinerMengenstaffel(
			Integer vkpfstaffelmengeIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) {
		Vkpfmengenstaffel vkpfmengenstaffel = null;
		vkpfmengenstaffel = em.find(Vkpfmengenstaffel.class,
				vkpfstaffelmengeIId);
		if (vkpfmengenstaffel == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		// der Preis muss an dieser Stelle berechnet werden
		BigDecimal nBerechneterPreis = new BigDecimal(0);
		if (vkpfmengenstaffel.getNArtikelfixpreis() != null) {
			nBerechneterPreis = vkpfmengenstaffel.getNArtikelfixpreis();
		} else {
			// WH 21.06.06 Es gilt die VK-Basis, die zu Beginn der Mengenstaffel
			// gueltig ist
			BigDecimal nPreisbasis = ermittlePreisbasis(
					vkpfmengenstaffel.getArtikelIId(), new java.sql.Date(
							vkpfmengenstaffel.getTPreisgueltigab().getTime()),
					vkpfmengenstaffel.getVkpfartikelpreislisteIId(),
					waehrungCNrZielwaehrung, theClientDto);

			VerkaufspreisDto vkpfDto = berechneVerkaufspreis(nPreisbasis,
					vkpfmengenstaffel.getFArtikelstandardrabattsatz());

			if (vkpfDto != null) {
				nBerechneterPreis = vkpfDto.nettopreis;
			}
		}

		return nBerechneterPreis;

	}

	/**
	 * Gegeben ist eine Preisbasis (= Einzelpreis) und ein Rabattsatz. Zu
	 * berechnen ist der Nettoeinzelpreis, der Zusatzrabattsatz bleibt auf 0.
	 * 
	 * @param nPreisbasisI
	 *            Einzelpreis
	 * @param dRabattsatzI
	 *            Rabattsatz
	 * @return VerkaufspreisDto VKP
	 * @throws EJBExceptionLP
	 */
	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI) throws EJBExceptionLP {

		return berechneVerkaufspreis(nPreisbasisI, dRabattsatzI, null);

	}

	public VerkaufspreisDto berechneVerkaufspreis(BigDecimal nPreisbasisI,
			Double dRabattsatzI, BigDecimal nMaterialzuschlag)
			throws EJBExceptionLP {

		if (nPreisbasisI != null) {
			VerkaufspreisDto verkaufspreisDto = new VerkaufspreisDto();
			verkaufspreisDto.einzelpreis = nPreisbasisI;
			verkaufspreisDto.rabattsatz = dRabattsatzI;
			verkaufspreisDto.rabattsumme = verkaufspreisDto.einzelpreis
					.multiply(new BigDecimal(verkaufspreisDto.rabattsatz
							.doubleValue()).movePointLeft(2));
			if (nMaterialzuschlag != null) {
				verkaufspreisDto.nettopreis = verkaufspreisDto.einzelpreis
						.subtract(verkaufspreisDto.rabattsumme).add(
								nMaterialzuschlag);
				verkaufspreisDto.bdMaterialzuschlag = nMaterialzuschlag;
			} else {
				verkaufspreisDto.nettopreis = verkaufspreisDto.einzelpreis
						.subtract(verkaufspreisDto.rabattsumme);
			}

			return verkaufspreisDto;
		} else {
			return null;
		}

	}

	/**
	 * v Verkaufspreisdfindung Stufe 3. <br>
	 * Aufgrund des Kunden bzw. seiner Rechnungsadresse wird der VKP ermittelt.
	 * KundeSOKOs koennen fuer einzelne Artikel oder Artikelgruppen erfasst
	 * werden und werden in Form von Mengenstaffeln hinterlegt.
	 * 
	 * @param artikelDtoI
	 *            fuer welchen Artikel wird der VKP ermittelt
	 * @param iIdKundeI
	 *            PK des Kunden, fuer den der VKP ermittelt wird
	 * @param nMengeI
	 *            die Menge des Artikels, die verkauft werden soll
	 * @param tGueltigkeitsdatumI
	 *            zu welchem Zeitpunkt soll der VKP ermittelt werden
	 * @param vkpreisfindungDtoI
	 *            der Ergebniscontainer fuer die VKPF
	 * @param iIdMwstsatzI
	 *            der zu beruecksichtigende Mwstsatz
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VerkaufspreisDto der ermittelte VKP
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public VkpreisfindungDto verkaufspreisfindungStufe3(ArtikelDto artikelDtoI,
			Integer iIdKundeI, BigDecimal nMengeI, Date tGueltigkeitsdatumI,
			VkpreisfindungDto vkpreisfindungDtoI, Integer iIdMwstsatzI,
			Integer preislisteIId, String waehrungCNrZielwaehrung,
			TheClientDto theClientDto) {
		if (tGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tGueltigkeitsdatumI == null"));
		}

		if (vkpreisfindungDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("vkpreisfindungDtoI == null"));
		}

		VerkaufspreisDto verkaufspreisDto = null;

		if (Helper.short2boolean(artikelDtoI.getBRabattierbar())) {
			// Stufe3: 0 die passende Mengenstaffel ermitteln
			KundesokomengenstaffelDto mengenstaffelDto = ermittleKundesokomengenstaffel(
					artikelDtoI, iIdKundeI, nMengeI, tGueltigkeitsdatumI,
					theClientDto);

			if (mengenstaffelDto != null) {

				ParametermandantDto p = null;
				try {
					p = getParameterFac().getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_KUNDEN,
							ParameterFac.PARAMETER_PREISBASIS_VERKAUF);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				int iPreisbasis = (Integer) p.getCWertAsObject();

				BigDecimal nPreisbasis = null;
				if (iPreisbasis == 0) {
					nPreisbasis = ermittlePreisbasis(artikelDtoI.getIId(),
							tGueltigkeitsdatumI, nMengeI, null,
							waehrungCNrZielwaehrung, theClientDto);
				} else {
					nPreisbasis = ermittlePreisbasis(artikelDtoI.getIId(),
							tGueltigkeitsdatumI, nMengeI, preislisteIId,
							waehrungCNrZielwaehrung, theClientDto);
				}

				// Stufe3: 1 Wenn ein Fixpreis hinterlegt wurde, wird der
				// Rabattsatz berechnet
				if (mengenstaffelDto.getNArtikelfixpreis() != null
						&& mengenstaffelDto.getNArtikelfixpreis().doubleValue() != 0) {

					KundeDto kdDto = getKundeFac().kundeFindByPrimaryKeySmall(
							iIdKundeI);

					try {
						if (!kdDto.getCWaehrung().equals(
								waehrungCNrZielwaehrung)) {
							mengenstaffelDto.setNArtikelfixpreis(getLocaleFac()
									.rechneUmInAndereWaehrungZuDatum(
											mengenstaffelDto
													.getNArtikelfixpreis(),
											kdDto.getCWaehrung(),
											waehrungCNrZielwaehrung,
											tGueltigkeitsdatumI, theClientDto));
						}
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
					verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
							mengenstaffelDto.getNArtikelfixpreis(),
							vkpreisfindungDtoI.getNMaterialzuschlag());
				} else {

					// Stufe3: 2 Wenn ein Rabattsatz hinterlegt wurde, wird der
					// Nettoeinzelpreis berechnet
					if (nPreisbasis != null) {
						verkaufspreisDto = berechneVerkaufspreis(nPreisbasis,
								mengenstaffelDto
										.getFArtikelstandardrabattsatz(),
								vkpreisfindungDtoI.getNMaterialzuschlag());
					}
				}
			}
		}

		// Stufe3: 3 Wenn VKPF Stufe 3 ein Ergebnis liefert, dann wird das
		// entsprechend hinterlegt
		if (verkaufspreisDto != null) {
			verkaufspreisDto = berechneBruttopreis(verkaufspreisDto,
					iIdMwstsatzI, theClientDto);

			vkpreisfindungDtoI.setVkpStufe3(verkaufspreisDto);

			if (vkpreisfindungDtoI.getVkpreisberechnetStufe() == null) {
				vkpreisfindungDtoI
						.setVkpreisberechnetStufe(VkpreisfindungDto.VKPFSTUFE3);
			}
		}

		return vkpreisfindungDtoI;
	}

	/**
	 * Kundengruppen werden ueber die Rechnungsadresse des Kunden gebildet. <br>
	 * Diese Methode liefert nur dann ein Ergebnis, wenn der Partner der
	 * Rechnungsadesse des Kunden selbst ein Kunde ist.
	 * 
	 * @param iIdKundeI
	 *            PK des Kunden
	 * @param theClientDtoI
	 *            die aktuellen Benutzerdaten
	 * @return Integer PK des Partners der Rechnungsadresse des Kunden, wenn
	 *         dieser wiederum ein Kunde ist
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private Integer ermittleKundeIIdDerRechnungsadresse(Integer iIdKundeI,
			TheClientDto theClientDtoI) throws EJBExceptionLP {
		Integer kundeIIdDerRechnungsadresse = null;

		try {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(iIdKundeI,
					theClientDtoI);

			PartnerDto partnerRechnungsadresseDto = getPartnerFac()
					.partnerFindByPrimaryKey(kundeDto.getPartnerIId(),
							theClientDtoI);

			// Ist der Partner beim aktuellen Mandanten ein Kunde?
			KundeDto kundeRechnungsadresseDto = getKundeFac()
					.kundeFindByiIdPartnercNrMandantOhneExc(
							partnerRechnungsadresseDto.getIId(),
							theClientDtoI.getMandant(), theClientDtoI);

			if (kundeRechnungsadresseDto != null) {
				kundeIIdDerRechnungsadresse = kundeRechnungsadresseDto.getIId();
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return kundeIIdDerRechnungsadresse;
	}

	/**
	 * Welche Kundesokomengenstaffel wird aufgrund von VKPF 3 verwendet.
	 * 
	 * @param artikelDtoI
	 *            fuer welchen Artikel wird der VKP ermittelt
	 * @param iIdKundeI
	 *            PK des Kunden, fuer den der VKP ermittelt wird
	 * @param nMengeI
	 *            die Menge des Artikels, die verkauft werden soll
	 * @param tGueltigkeitsdatumI
	 *            zu welchem Zeitpunkt soll der VKP ermittelt werden
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return VerkaufspreisDto der ermittelte VKP
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	public KundesokomengenstaffelDto ermittleKundesokomengenstaffel(
			ArtikelDto artikelDtoI, Integer iIdKundeI, BigDecimal nMengeI,
			Date tGueltigkeitsdatumI, TheClientDto theClientDto)
			throws EJBExceptionLP {
		if (artikelDtoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DTO_IS_NULL,
					new Exception("artikelDtoI == null"));
		}
		if (iIdKundeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundeI == null"));
		}
		if (nMengeI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("nMengeI == null"));
		}
		if (tGueltigkeitsdatumI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("tGueltigkeitsdatumI == null"));
		}

		Integer kundeIIdDerRechnungsadresse = ermittleKundeIIdDerRechnungsadresse(
				iIdKundeI, theClientDto);

		// Kundesokomengenstaffel fuer Artikel
		KundesokomengenstaffelDto mengenstaffelArtikelDto = ermittleMengenstaffelFuerArtikel(
				iIdKundeI, kundeIIdDerRechnungsadresse, artikelDtoI.getIId(),
				nMengeI, tGueltigkeitsdatumI);

		KundesokomengenstaffelDto mengenstaffelArtgruDto = null;

		// Kundesokomengenstaffel fuer Artikelgruppe
		if (artikelDtoI.getArtgruIId() != null) {
			mengenstaffelArtgruDto = ermittleMengenstaffelFuerArtikelgruppe(
					iIdKundeI, kundeIIdDerRechnungsadresse,
					artikelDtoI.getArtgruIId(), nMengeI, tGueltigkeitsdatumI);
		}

		KundesokomengenstaffelDto mengenstaffelDto = null;

		// wenn es beides gibt, welche Mengenstaffel ist die passende?
		if (mengenstaffelArtikelDto != null && mengenstaffelArtgruDto != null) {
			mengenstaffelDto = mengenstaffelArtikelDto;
			// PJ 09/0014164
			// if (nMengeI.doubleValue() >=
			// mengenstaffelArtgruDto.getNMenge().doubleValue()) {
			// mengenstaffelDto = mengenstaffelArtgruDto;
			// }
		} else if (mengenstaffelArtikelDto != null) {
			mengenstaffelDto = mengenstaffelArtikelDto;
		} else {
			mengenstaffelDto = mengenstaffelArtgruDto;
		}

		return mengenstaffelDto;
	}

	/**
	 * Liefert die passende Mengenstaffel, wenn fuer den Kunden zum besagten
	 * Artikel zu einem bestimmten Zeitpunkt fuer eine bestimmte Menge eine
	 * Kundesokomengenstaffel erfasst wurde, sonst null.
	 * 
	 * @param iIdKundeI
	 *            PK des Kunden
	 * @param iIdRechnungsadresseKundeI
	 *            PK des Kunden der Kundegruppe von iIdKundeI
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param nMengeI
	 *            BigDecimal die gewuenschte Menge des urspruenglichen Artikels
	 * @param tGueltigkeitsdatumI
	 *            das Datum, zu dem der VKP ermittelt werden soll
	 * @return KundesokomengenstaffelDto null, wenn keine Mengenstaffel
	 *         ermittelt werden konnte
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private KundesokomengenstaffelDto ermittleMengenstaffelFuerArtikel(
			Integer iIdKundeI, Integer iIdRechnungsadresseKundeI,
			Integer iIdArtikelI, BigDecimal nMengeI, Date tGueltigkeitsdatumI)
			throws EJBExceptionLP {
		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}

		KundesokomengenstaffelDto mengenstaffelDto = null;

		try {
			// 0: Wurde fuer diesen Kunden fuer diesen Artikel fuer den
			// fraglichen Zeitpunkt eine SOKO erfasst?
			KundesokoDto kundesokoDto = getKundesokoFac()
					.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
							iIdKundeI, iIdArtikelI, tGueltigkeitsdatumI);

			// PJ18867
			if (kundesokoDto != null
					&& Helper.short2boolean(kundesokoDto
							.getBWirktNichtFuerPreisfindung()) == true) {
				return null;
			}

			if (kundesokoDto != null) {
				mengenstaffelDto = ermittleMengenstaffelAusKundesoko(
						kundesokoDto.getIId(), nMengeI);

				if (mengenstaffelDto == null) {
					// 1: Wurde fuer die Rechnungsadresse des Kunden fuer diesen
					// Artikel
					// fuer den fraglichen Zeitpunkt eine SOKO erfasst?
					if (iIdRechnungsadresseKundeI != null
							&& iIdRechnungsadresseKundeI.intValue() != iIdKundeI
									.intValue()) {
						kundesokoDto = getKundesokoFac()
								.kundesokoFindByKundeIIdArtikelIIdGueltigkeitsdatumOhneExc(
										iIdRechnungsadresseKundeI, iIdArtikelI,
										tGueltigkeitsdatumI);

						if (kundesokoDto != null) {
							mengenstaffelDto = ermittleMengenstaffelAusKundesoko(
									kundesokoDto.getIId(), nMengeI);

						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return mengenstaffelDto;
	}

	/**
	 * Liefert eine Mengenstaffel, wenn fuer den Kunden zur besagten
	 * Artikelgruppe zu einem bestimmten Zeitpunkt fuer eine bestimmte Menge
	 * eine Kundesokomengenstaffel erfasst wurde, sonst null.
	 * 
	 * @param iIdKundeI
	 *            PK des Kunden
	 * @param iIdRechnungsadresseKundeI
	 *            PK des Kunden der Kundegruppe von iIdKundeI
	 * @param iIdArtgruI
	 *            PK der Artikelgruppe
	 * @param nMengeI
	 *            BigDecimal die gewuenschte Menge des urspruenglichen Artikels
	 * @param tGueltigkeitsdatumI
	 *            das Datum, zu dem der VKP ermittelt werden soll
	 * @return KundesokomengenstaffelDto null, wenn keine Mengenstaffel
	 *         ermittelt werden konnte
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private KundesokomengenstaffelDto ermittleMengenstaffelFuerArtikelgruppe(
			Integer iIdKundeI, Integer iIdRechnungsadresseKundeI,
			Integer iIdArtgruI, BigDecimal nMengeI, Date tGueltigkeitsdatumI)
			throws EJBExceptionLP {
		if (iIdArtgruI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtgruI == null"));
		}

		KundesokomengenstaffelDto mengenstaffelDto = null;

		try {
			// 0: Wurde fuer diesen Kunden fuer diese Artikelgruppe fuer den
			// fraglichen Zeitpunkt eine SOKO erfasst?
			KundesokoDto kundesokoDto = getKundesokoFac()
					.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
							iIdKundeI, iIdArtgruI, tGueltigkeitsdatumI);

			if (kundesokoDto != null) {
				mengenstaffelDto = ermittleMengenstaffelAusKundesoko(
						kundesokoDto.getIId(), nMengeI);

				if (mengenstaffelDto == null) {
					// 1: Wurde fuer die Rechnungsadresse des Kunden fuer diese
					// Artikelgruppe
					// fuer den fraglichen Zeitpunkt eine SOKO erfasst?
					if (iIdRechnungsadresseKundeI != null
							&& iIdRechnungsadresseKundeI.intValue() != iIdKundeI
									.intValue()) {
						kundesokoDto = getKundesokoFac()
								.kundesokoFindByKundeIIdArtgruIIdGueltigkeitsdatumOhneExc(
										iIdRechnungsadresseKundeI, iIdArtgruI,
										tGueltigkeitsdatumI);

						if (kundesokoDto != null) {
							mengenstaffelDto = ermittleMengenstaffelAusKundesoko(
									kundesokoDto.getIId(), nMengeI);

						}
					}
				}
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return mengenstaffelDto;
	}

	/**
	 * Zu einer Kundensoko und einer vorgegebenen Menge soll die passende
	 * Mengenstaffel ermittelt werden.
	 * 
	 * @param iIdKundesokoI
	 *            PK der Kundesoko
	 * @param nMengeI
	 *            die vorgegebene Menge eines Artikels
	 * @return KundesokomengenstaffelDto null, wenn keine Mengenstaffel
	 *         ermittelt werden konnte
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 */
	private KundesokomengenstaffelDto ermittleMengenstaffelAusKundesoko(
			Integer iIdKundesokoI, BigDecimal nMengeI) throws EJBExceptionLP {
		if (iIdKundesokoI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdKundesokoI == null"));
		}

		KundesokomengenstaffelDto mengenstaffelDto = null;

		try {
			// 0: Welche Mengenstaffel wurde erfasst?
			KundesokomengenstaffelDto[] aMengenstaffelDtos = getKundesokoFac()
					.kundesokomengenstaffelFindByKundesokoIIdNMenge(
							iIdKundesokoI, nMengeI);

			if (aMengenstaffelDtos != null && aMengenstaffelDtos.length > 0) {
				mengenstaffelDto = aMengenstaffelDtos[aMengenstaffelDtos.length - 1];
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return mengenstaffelDto;
	}

	/**
	 * Pruefen, ob der Verkaufspreis unter dem minimalen Gestehungspreis des
	 * Artikels am Hauptlager des Mandanten liegt.
	 * 
	 * @param iIdArtikelI
	 *            PK des Artikels
	 * @param bdVerkaufspreisI
	 *            der vorgeschlagene Verkaufspreis
	 * @param ddWechselkursMandantZuBelegwaehrungI
	 *            der Wechselkurs des Beleges
	 * @param theClientDto
	 *            der aktuelle Benutzer
	 * @return boolean true, wenn der Verkaufspreis unter dem minimalen
	 *         Gestehungspreis liegt
	 * @throws EJBExceptionLP
	 */
	public boolean liegtVerkaufpreisUnterMinVerkaufpsreis(Integer iIdArtikelI,
			BigDecimal bdVerkaufspreisI,
			Double ddWechselkursMandantZuBelegwaehrungI, BigDecimal nMengeI,
			TheClientDto theClientDto) throws EJBExceptionLP {

		if (iIdArtikelI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("iIdArtikelI == null"));
		}
		if (bdVerkaufspreisI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception("bdVerkaufspreisI == null"));
		}
		if (ddWechselkursMandantZuBelegwaehrungI == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
					new Exception(
							"ddWechselkursMandantZuBelegwaehrungI == null"));
		}
		boolean bLiegtVerkaufspreisUnterMinGestehungspreis = false;
		try {
			LagerDto oHauptlagerDto = getLagerFac().getHauptlagerDesMandanten(
					theClientDto);
			bLiegtVerkaufspreisUnterMinGestehungspreis = liegtVerkaufspreisUnterMinVerkaufspreis(
					iIdArtikelI, oHauptlagerDto.getIId(), bdVerkaufspreisI,
					ddWechselkursMandantZuBelegwaehrungI, nMengeI, theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return bLiegtVerkaufspreisUnterMinGestehungspreis;
	}

	public void pruefeVkpfStaffelmenge(TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();
		ArrayList<Integer> rids = new ArrayList<Integer>();
		try {
			org.hibernate.Criteria crit = session
					.createCriteria(FLRVkpfStaffelmenge.class);
			crit.add(Restrictions.isNull("t_preisgueltigbis"));
			crit.addOrder(Order.asc("artikel_i_id"));
			Integer artikel_i_id = null;
			Integer tmp_i_id = new Integer(0);
			List<?> vkpfsmList = crit.list();
			Iterator<?> it = vkpfsmList.iterator();
			while (it.hasNext()) {
				FLRVkpfStaffelmenge item = (FLRVkpfStaffelmenge) it.next();
				artikel_i_id = item.getArtikel_i_id();
				if (!artikel_i_id.equals(tmp_i_id)) {
					rids.add(artikel_i_id);
					tmp_i_id = artikel_i_id;
				}
			}
			BigDecimal tmpmenge = null;
			for (int i = 0; i < rids.size(); i++) {
				String queryString = "from FLRVkpfStaffelmenge flrvkpfstaffelmenge  WHERE flrvkpfstaffelmenge.artikel_i_id = "
						+ rids.get(i)
						+ " ORDER BY flrvkpfstaffelmenge.n_menge,flrvkpfstaffelmenge.t_preisgueltigab";
				org.hibernate.Query query = session.createQuery(queryString);
				List<?> rList = query.list();
				Iterator<?> rlIterator = rList.iterator();
				ArrayList<FLRVkpfStaffelmenge> items = null;
				tmpmenge = new BigDecimal(0);
				tmpmenge = tmpmenge.setScale(4);
				BigDecimal menge = null;
				while (rlIterator.hasNext()) {
					FLRVkpfStaffelmenge item = (FLRVkpfStaffelmenge) rlIterator
							.next();
					menge = item.getN_menge();
					System.out.println(rids.get(i) + " " + item.getN_menge()
							+ " " + item.getT_preisgueltigab() + " "
							+ item.getT_preisgueltigbis());
					if ((menge.compareTo(tmpmenge) != 0)) {
						if (items != null) {
							int s = items.size();
							if (s >= 2) {
								if (items.get(s - 2).getT_preisgueltigbis() == null) {
									Vkpfmengenstaffel vkpfMengenstaffel = null;
									// try {
									vkpfMengenstaffel = em.find(
											Vkpfmengenstaffel.class,
											items.get(s - 2).getI_id());
									if (vkpfMengenstaffel == null) {
									}
									// }
									// catch (FinderException ex) {
									// }
									java.sql.Date d = new Date(items.get(s - 1)
											.getT_preisgueltigab().getTime());
									vkpfMengenstaffel.setTPreisgueltigbis(d);
								}
							}
						}
						items = new ArrayList<FLRVkpfStaffelmenge>();
						tmpmenge = menge;
					}
					items.add(item);
				}
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

}
