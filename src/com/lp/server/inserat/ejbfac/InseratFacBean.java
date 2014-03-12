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
package com.lp.server.inserat.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Session;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.inserat.ejb.Inserat;
import com.lp.server.inserat.ejb.Inseratartikel;
import com.lp.server.inserat.ejb.Inserater;
import com.lp.server.inserat.ejb.Inseratrechnung;
import com.lp.server.inserat.ejb.Inseratrechnungartikel;
import com.lp.server.inserat.fastlanereader.generated.FLRInserat;
import com.lp.server.inserat.fastlanereader.generated.FLRInseratartikel;
import com.lp.server.inserat.fastlanereader.generated.FLRInseratrechnung;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratDtoAssembler;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.inserat.service.InseratartikelDto;
import com.lp.server.inserat.service.InseratartikelDtoAssembler;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.inserat.service.InseraterDtoAssembler;
import com.lp.server.inserat.service.InseratrechnungDto;
import com.lp.server.inserat.service.InseratrechnungDtoAssembler;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnung;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.pkgenerator.PKConst;
import com.lp.server.system.pkgenerator.bl.PKGeneratorObj;
import com.lp.server.system.pkgenerator.format.LpBelegnummer;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Stateless
public class InseratFacBean extends Facade implements InseratFac {
	@PersistenceContext
	private EntityManager em;

	public Integer createInserat(InseratDto inseratDto,
			TheClientDto theClientDto) {
		Integer inseratIId = null;
		try {
			// Generieren von PK + Belegnummer
			LpBelegnummerFormat f = getBelegnummerGeneratorObj()
					.getBelegnummernFormat(inseratDto.getMandantCNr());

			Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(
					inseratDto.getMandantCNr(), inseratDto.getTBelegdatum());

			LpBelegnummer bnr = getBelegnummerGeneratorObj().getNextBelegNr(
					iGeschaeftsjahr, PKConst.PK_INSERAT,
					inseratDto.getMandantCNr(), theClientDto);

			inseratIId = bnr.getPrimaryKey();
			String inseratCNr = f.format(bnr);

			inseratDto.setIId(inseratIId);
			inseratDto.setCNr(inseratCNr);
			inseratDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			inseratDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

			Timestamp t = getTimestamp();
			inseratDto.setTAnlegen(t);
			inseratDto.setTAendern(t);

			Inserat inserat = new Inserat(inseratDto.getIId(),
					inseratDto.getCNr(), inseratDto.getMandantCNr(),
					inseratDto.getFKdRabatt(), inseratDto.getFKdZusatzrabatt(),
					inseratDto.getFKdNachlass(), inseratDto.getLieferantIId(),
					inseratDto.getTBelegdatum(), inseratDto.getFLFRabatt(),
					inseratDto.getFLfZusatzrabatt(),
					inseratDto.getFLfNachlass(), inseratDto.getTTermin(),
					inseratDto.getStatusCNr(),
					inseratDto.getArtikelIIdInseratart(),
					inseratDto.getPersonalIIdAnlegen(),
					inseratDto.getPersonalIIdAendern(),
					inseratDto.getPersonalIIdVertreter(),
					inseratDto.getNMenge(),
					inseratDto.getNNettoeinzelpreisEk(),
					inseratDto.getNNettoeinzelpreisVk(),
					inseratDto.getTAnlegen(), inseratDto.getTAendern(),
					inseratDto.getBDruckBestellungLf(),
					inseratDto.getBDruckBestellungKd(),
					inseratDto.getBDruckRechnungKd(),
					inseratDto.getBWertaufteilen());
			em.persist(inserat);
			em.flush();

			setInseratFromInseratDto(inserat, inseratDto);

		

			if (inseratDto.getInseratrechnungDto() != null) {
				inseratDto.getInseratrechnungDto().setInseratIId(inseratIId);
				inseratDto.getInseratrechnungDto().setRechnungpositionIId(null);
				createInseratrechnung(inseratDto.getInseratrechnungDto(),
						theClientDto);
			}
			
			if (inseratDto.inseratIId_Kopiertvon != null) {
				inseratArtikelRechnungKopieren(inseratDto.inseratIId_Kopiertvon,
						inseratIId, theClientDto);
			}
			

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return inseratIId;
	}

	public Integer createInseratrechnung(InseratrechnungDto inseratrechnungDto,
			TheClientDto theClientDto) {
		Integer inseratIId = null;

		try {
			Query query = em
					.createNamedQuery("InseratrechnungfindInseratIIdKundeIId");
			query.setParameter(1, inseratrechnungDto.getInseratIId());
			query.setParameter(2, inseratrechnungDto.getKundeIId());
			// @todo getSingleResult oder getResultList ?
			Inseratrechnung doppelt = (Inseratrechnung) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IV_INSERATRECHNUNG.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// Generieren von PK

			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSERATRECHNUNG);
			inseratrechnungDto.setIId(pk);

			Query queryNext = em
					.createNamedQuery("InseratrechnungejbSelectNextReihung");
			queryNext.setParameter(1, inseratrechnungDto.getInseratIId());

			Integer i = (Integer) queryNext.getSingleResult();

			if (i == null) {
				i = new Integer(0);
			}
			i = new Integer(i.intValue() + 1);
			inseratrechnungDto.setiSort(i);

			Inseratrechnung inseratrechnung = new Inseratrechnung(
					inseratrechnungDto.getIId(),
					inseratrechnungDto.getInseratIId(),
					inseratrechnungDto.getKundeIId(),
					inseratrechnungDto.getiSort());
			em.persist(inseratrechnung);
			em.flush();

			setInseratrechnungFromInseratrechnungDto(inseratrechnung,
					inseratrechnungDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return inseratIId;
	}

	public Integer createInserater(InseraterDto inseraterDto,
			TheClientDto theClientDto) {

		try {
			Query query = em
					.createNamedQuery("InseraterfindByInseratIIdEingansrechnungIId");
			query.setParameter(1, inseraterDto.getInseratIId());
			query.setParameter(2, inseraterDto.getEingangsrechnungIId());
			// @todo getSingleResult oder getResultList ?
			Inserater doppelt = (Inserater) query.getSingleResult();
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE,
					new Exception("IV_INSERATER.UK"));
		} catch (NoResultException ex) {

		}

		try {
			// Generieren von PK
			inseraterDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
			inseraterDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSERATER);
			inseraterDto.setIId(pk);
			InseratDto inseratDto = inseratFindByPrimaryKey(inseraterDto
					.getInseratIId());
			if (Helper.short2boolean(inseratDto.getBWertaufteilen()) == false) {
				// Wenn Inserat.b_wertaufteilen = false, wird der Betrag
				// berechnet
				// Preise berechnen

				InseratartikelDto[] inseratartikelDtos = inseratartikelFindByInseratIId(inseraterDto
						.getInseratIId());

				BigDecimal preisZusatzEK = new BigDecimal(0);

				for (int i = 0; i < inseratartikelDtos.length; i++) {
					preisZusatzEK = preisZusatzEK.add(inseratartikelDtos[i]
							.getNMenge().multiply(
									inseratartikelDtos[i]
											.getNNettoeinzelpreisEk()));

				}

				int iNachkommastellenPreis = 2;
				try {
					iNachkommastellenPreis = getMandantFac()
							.getNachkommastellenPreisEK(
									theClientDto.getMandant());
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

				inseraterDto.setNBetrag(inseratDto.getErrechneterWertEK(
						iNachkommastellenPreis).add(preisZusatzEK));
			}

			Inserater inserater = new Inserater(inseraterDto.getIId(),
					inseraterDto.getInseratIId(),
					inseraterDto.getEingangsrechnungIId(),
					inseraterDto.getNBetrag(),
					inseraterDto.getPersonalIIdAnlegen(),
					inseraterDto.getPersonalIIdAendern());

			inseraterDto.setTAendern(inserater.getTAendern());
			inseraterDto.setTAnlegen(inserater.getTAnlegen());
			em.persist(inserater);
			em.flush();

			setInseraterFromInseraterDto(inserater, inseraterDto);

			// Inserat auf Verrechenbar setzen

			Inserat inserat = em.find(Inserat.class,
					inseraterDto.getInseratIId());

			// SP1416, ausser es ist bereits verrechnet
			if (!inserat.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)) {

				inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
				inserat.setPersonalIIdVerrechnen(theClientDto.getIDPersonal());
				inserat.setTVerrechnen(new Timestamp(System.currentTimeMillis()));
				em.flush();
			}
			// Bestellposition erledigen
			if (inserat.getBestellpositionIId() != null) {
				try {
					getBestellpositionFac()
							.manuellAufVollstaendigGeliefertSetzen(
									inserat.getBestellpositionIId(),
									theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			return pk;
		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

	}

	public Integer createInseratartikel(InseratartikelDto inseratartikelDto,
			TheClientDto theClientDto) {
		Integer inseratIId = null;
		try {
			// generieren von primary key
			PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
			Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSERATARTIKEL);
			inseratartikelDto.setIId(pk);

			Inseratartikel inseratrechnung = new Inseratartikel(
					inseratartikelDto.getIId(),
					inseratartikelDto.getInseratIId(),
					inseratartikelDto.getArtikelIId(),
					inseratartikelDto.getNMenge(),
					inseratartikelDto.getNNettoeinzelpreisEk(),
					inseratartikelDto.getNNettoeinzelpreisVk());
			em.persist(inseratrechnung);
			em.flush();

			setInseratartikelFromInseratartikelDto(inseratrechnung,
					inseratartikelDto);

		} catch (EntityExistsException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN, ex);
		}

		return inseratIId;
	}

	public void vertauscheInseratrechnung(Integer inseratrechnungIId1,
			Integer inseratrechnungIId2) {

		Inseratrechnung oLieferant1 = em.find(Inseratrechnung.class,
				inseratrechnungIId1);
		Inseratrechnung oLieferant2 = em.find(Inseratrechnung.class,
				inseratrechnungIId2);
		Integer iSort1 = oLieferant1.getiSort();
		Integer iSort2 = oLieferant2.getiSort();

		oLieferant2.setiSort(new Integer(-1));
		oLieferant1.setiSort(iSort2);
		oLieferant2.setiSort(iSort1);

	}

	public void inseratVertreterAendern(Integer inseratIId,
			Integer personalId_Vertreter, TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);
		inserat.setPersonalIIdVertreter(personalId_Vertreter);
		inserat.setTAendern(getTimestamp());
		inserat.setPersonalIIdAendern(theClientDto.getIDPersonal());

	}

	public InseratDto inseratFindByPrimaryKey(Integer iId) {
		Inserat inserat = em.find(Inserat.class, iId);
		InseratDto inseratDto = assembleInseratDto(inserat);
		inseratDto
				.setInseratrechnungDto(inseratrechnungFindErstenEintragByInseratIId(inseratDto
						.getIId()));
		return inseratDto;
	}

	public ArrayList eingangsrechnungsIIdsEinesInserates(Integer inseratIId) {
		ArrayList al = new ArrayList();
		Query query = em
				.createNamedQuery("InseraterfindInseratIIdEingansrechnungIId");
		query.setParameter(1, inseratIId);
		query.setMaxResults(1);

		List l = query.getResultList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Inserater inserater = (Inserater) it.next();
			al.add(inserater.getEingangsrechnungIId());
		}
		return al;
	}

	public InseraterDto[] inseraterFindByEingangsrechnungIId(
			Integer eingangsrechnungIId) {

		Query query = em.createNamedQuery("InseraterfindByEingangsrechnungIId");
		query.setParameter(1, eingangsrechnungIId);
		return InseraterDtoAssembler.createDtos(query.getResultList());
	}

	public InseraterDto[] inseraterFindByInseratIId(Integer inseratIId) {

		Query query = em.createNamedQuery("InseraterfindByInseratIId");
		query.setParameter(1, inseratIId);
		return InseraterDtoAssembler.createDtos(query.getResultList());
	}

	public InseratrechnungDto inseratrechnungFindByPrimaryKey(Integer iId) {
		Inseratrechnung inseratrechnung = em.find(Inseratrechnung.class, iId);
		return assembleInseratrechnungDto(inseratrechnung);
	}

	public InseraterDto inseraterFindByPrimaryKey(Integer iId) {
		Inserater inserater = em.find(Inserater.class, iId);
		return InseraterDtoAssembler.createDto(inserater);
	}

	public InseratartikelDto inseratartikelFindByPrimaryKey(Integer iId) {
		Inseratartikel inseratartikel = em.find(Inseratartikel.class, iId);
		return InseratartikelDtoAssembler.createDto(inseratartikel);
	}

	public boolean istInseratInEinerRechnungEnthalten(Integer rechnungIId) {
		RechnungPositionDto[] rechposDtos = null;
		try {
			rechposDtos = getRechnungFac().rechnungPositionFindByRechnungIId(
					rechnungIId);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		for (int i = 0; i < rechposDtos.length; i++) {
			InseratDto dto = istIseratAufRechnungspositionVorhanden(rechposDtos[i]
					.getIId());

			if (dto != null) {
				return true;
			}
		}
		return false;
	}

	public InseratartikelDto[] inseratartikelFindByInseratIId(Integer inseratIId) {
		Query query = em.createNamedQuery("InseratartikelfindInseratIId");
		query.setParameter(1, inseratIId);
		List l = query.getResultList();
		return InseratartikelDtoAssembler.createDtos(l);

	}

	public InseratrechnungDto inseratrechnungFindErstenEintragByInseratIId(
			Integer inseratIId) {
		Query query = em
				.createNamedQuery("InseratrechnungfindDenErstenEintragByInseratIId");
		query.setParameter(1, inseratIId);
		query.setMaxResults(1);

		List l = query.getResultList();
		if (l.size() == 1) {
			return assembleInseratrechnungDto((Inseratrechnung) l.get(0));
		} else {
			return null;
		}
	}

	public void toggleManuellerledigt(Integer inseratIId,
			TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);
		if (inserat.getTManuellerledigt() == null) {
			inserat.setTManuellerledigt(new Timestamp(System
					.currentTimeMillis()));
			inserat.setPersonalIIdManuellerledigt(theClientDto.getIDPersonal());
			inserat.setStatusCNr(LocaleFac.STATUS_ERLEDIGT);
		} else {
			inserat.setPersonalIIdManuellerledigt(null);
			inserat.setTManuellerledigt(null);

			if (inserat.getPersonalIIdGestoppt() != null) {
				inserat.setStatusCNr(LocaleFac.STATUS_GESTOPPT);
				return;
			}

			if (inserat.getPersonalIIdErschienen() != null) {
				inserat.setStatusCNr(LocaleFac.STATUS_ERSCHIENEN);
			}
			if (gibtEsNochWeitereRechnungenFuerdiesesInserat(inseratIId,
					theClientDto).size() > 0) {
				inserat.setStatusCNr(LocaleFac.STATUS_VERRECHNET);
			} else if (inserat.getPersonalIIdVerrechnen() != null
					|| inserat.getPersonalIIdManuellverrechnen() != null) {
				inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
			}

		}
	}

	public void toggleErschienen(Integer inseratIId, TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);
		if (inserat.getTErschienen() == null) {
			inserat.setTErschienen(new Timestamp(System.currentTimeMillis()));
			inserat.setPersonalIIdErschienen(theClientDto.getIDPersonal());
			inserat.setStatusCNr(LocaleFac.STATUS_ERSCHIENEN);
		} else {
			inserat.setPersonalIIdErschienen(null);
			inserat.setTErschienen(null);
			inserat.setStatusCNr(LocaleFac.STATUS_BESTELLT);
		}
		em.merge(inserat);
		em.flush();
	}

	public void toggleGestoppt(Integer inseratIId, String cBegruendung,
			TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);
		if (inserat.getStatusCNr().equals(LocaleFac.STATUS_VERRECHENBAR)) {
			inserat.setStatusCNr(LocaleFac.STATUS_GESTOPPT);
			inserat.setTGestoppt(new Timestamp(System.currentTimeMillis()));
			inserat.setCGestoppt(cBegruendung);
			inserat.setPersonalIIdGestoppt(theClientDto.getIDPersonal());
		} else if (inserat.getStatusCNr().equals(LocaleFac.STATUS_GESTOPPT)) {
			inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
			inserat.setTGestoppt(null);
			inserat.setPersonalIIdGestoppt(null);
		}
		em.merge(inserat);
		em.flush();
	}

	public void toggleVerrechenbar(Integer inseratIId, TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);
		if (inserat.getStatusCNr().equals(LocaleFac.STATUS_ERSCHIENEN)) {
			inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
			inserat.setTManuellverrechnen(new Timestamp(System
					.currentTimeMillis()));
			inserat.setPersonalIIdManuellverrechnen(theClientDto
					.getIDPersonal());
		} else if (inserat.getStatusCNr().equals(LocaleFac.STATUS_VERRECHENBAR)) {
			inserat.setStatusCNr(LocaleFac.STATUS_ERSCHIENEN);
			inserat.setTManuellverrechnen(null);
			inserat.setPersonalIIdManuellverrechnen(null);
		}
		em.merge(inserat);
		em.flush();
	}

	private InseratDto assembleInseratDto(Inserat inserat) {
		return InseratDtoAssembler.createDto(inserat);
	}

	private List<InseratDto> assembleInseratDtos(List<Inserat> inserate) {
		List<InseratDto> list = new ArrayList<InseratDto>();
		for (Inserat inserat : inserate) {
			list.add(assembleInseratDto(inserat));
		}
		return list;
	}

	private InseratrechnungDto assembleInseratrechnungDto(
			Inseratrechnung inseratrechnung) {
		return InseratrechnungDtoAssembler.createDto(inseratrechnung);
	}

	public void eingangsrechnungZuordnen(Integer inseratIId,
			Integer eingangsrechnungIId, BigDecimal nBetrag,
			TheClientDto theClientDto) {
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSERATER);
		Inserater inserater = new Inserater(pk, inseratIId,
				eingangsrechnungIId, nBetrag, theClientDto.getIDPersonal(),
				theClientDto.getIDPersonal());
		em.merge(inserater);
		em.flush();

		Inserat inserat = em.find(Inserat.class, inseratIId);
		inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
		inserat.setPersonalIIdVerrechnen(theClientDto.getIDPersonal());
		inserat.setTVerrechnen(new Timestamp(System.currentTimeMillis()));
		em.merge(inserat);
		em.flush();

	}

	public void updateInseratOhneWeiterAktion(InseratDto inseratDto,
			TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratDto.getIId());
		setInseratFromInseratDto(inserat, inseratDto);
	}

	public void updateInserat(InseratDto inseratDto, TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratDto.getIId());

		inseratDto.setTAendern(getTimestamp());
		inseratDto.setPersonalIIdAendern(theClientDto.getIDPersonal());

		try {
			Query query = em.createNamedQuery("InseratfindByMandantCNrCnr");
			query.setParameter(1, theClientDto.getMandant());
			query.setParameter(2, inseratDto.getCNr());

			Integer iIdVorhanden = ((Inserat) query.getSingleResult()).getIId();
			if (inserat.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IV_INSERAT.UK"));
			}
			setInseratFromInseratDto(inserat, inseratDto);
		} catch (NoResultException ex) {
			setInseratFromInseratDto(inserat, inseratDto);
		}

		if (inseratDto.getInseratrechnungDto() != null) {
			if (inseratDto.getInseratrechnungDto().getIId() == null) {
				createInseratrechnung(inseratDto.getInseratrechnungDto(),
						theClientDto);
			} else {
				updateInseratrechnung(inseratDto.getInseratrechnungDto(),
						theClientDto);
			}

		}
		if (inseratDto.getBestellpositionIId() != null) {
			preisInBestellpositionRueckpflegenAusKopfdaten(inseratDto.getIId(),
					theClientDto);
		}
	}

	public void updateInseratrechnung(InseratrechnungDto inseratrechnungDto,
			TheClientDto theClientDto) {
		Inseratrechnung inseratrechnung = em.find(Inseratrechnung.class,
				inseratrechnungDto.getIId());

		try {
			Query query = em
					.createNamedQuery("InseratrechnungfindInseratIIdKundeIId");
			query.setParameter(1, inseratrechnungDto.getInseratIId());
			query.setParameter(2, inseratrechnungDto.getKundeIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Inseratrechnung) query.getSingleResult())
					.getIId();
			if (inseratrechnungDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IV_INSERATRECHNUNG.UK"));
			}

		} catch (NoResultException ex) {

		}

		setInseratrechnungFromInseratrechnungDto(inseratrechnung,
				inseratrechnungDto);
	}

	public void updateInserater(InseraterDto inseraterDto,
			TheClientDto theClientDto) {
		inseraterDto.setTAendern(new Timestamp(System.currentTimeMillis()));
		inseraterDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
		Inserater inserater = em.find(Inserater.class, inseraterDto.getIId());

		try {
			Query query = em
					.createNamedQuery("InseraterfindByInseratIIdEingansrechnungIId");
			query.setParameter(1, inseraterDto.getInseratIId());
			query.setParameter(2, inseraterDto.getEingangsrechnungIId());
			// @todo getSingleResult oder getResultList ?
			Integer iIdVorhanden = ((Inserater) query.getSingleResult())
					.getIId();
			if (inseraterDto.getIId().equals(iIdVorhanden) == false) {
				throw new EJBExceptionLP(
						EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE, new Exception(
								"IV_INSERATER.UK"));
			}

		} catch (NoResultException ex) {

		}

		InseratDto inseratDto = inseratFindByPrimaryKey(inseraterDto
				.getInseratIId());
		if (Helper.short2boolean(inseratDto.getBWertaufteilen()) == false) {
			// Wenn Inserat.b_wertaufteilen = false, wird der Betrag
			// berechnet
			// Preise berechnen

			InseratartikelDto[] inseratartikelDtos = inseratartikelFindByInseratIId(inseraterDto
					.getInseratIId());

			BigDecimal preisZusatzEK = new BigDecimal(0);

			for (int i = 0; i < inseratartikelDtos.length; i++) {
				preisZusatzEK = preisZusatzEK
						.add(inseratartikelDtos[i].getNMenge().multiply(
								inseratartikelDtos[i].getNNettoeinzelpreisEk()));

			}

			int iNachkommastellenPreis = 2;
			try {
				iNachkommastellenPreis = getMandantFac()
						.getNachkommastellenPreisVK(theClientDto.getMandant());
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			inseraterDto.setNBetrag(inseratDto.getErrechneterWertEK(
					iNachkommastellenPreis).add(preisZusatzEK));
		}

		setInseraterFromInseraterDto(inserater, inseraterDto);
	}

	public void updateInseratartikel(InseratartikelDto inseratartikelDto,
			TheClientDto theClientDto) {
		Inseratartikel inseratartikel = em.find(Inseratartikel.class,
				inseratartikelDto.getIId());

		setInseratartikelFromInseratartikelDto(inseratartikel,
				inseratartikelDto);

		preisInBestellpositionRueckpflegenAusInseratArtikel(
				inseratartikel.getIId(), theClientDto);
	}

	public void storniereInserat(Integer inseratIId, TheClientDto theClientDto) {

		Inserat inserat = em.find(Inserat.class, inseratIId);

		if (inserat.getStatusCNr().equals(LocaleFac.STATUS_ANGELEGT)
				|| inserat.getStatusCNr().equals(LocaleFac.STATUS_BESTELLT)) {
			inserat.setStatusCNr(LocaleFac.STATUS_STORNIERT);
			em.merge(inserat);
			em.flush();
		}

	}

	public void removeInseratrechnung(Integer inseratrechnungIId) {
		Inseratrechnung toRemove = em.find(Inseratrechnung.class,
				inseratrechnungIId);

		Integer inseratIId = toRemove.getInseratIId();
		Query query = em
				.createNamedQuery("InseratrechnungfindDenErstenEintragByInseratIId");
		query.setParameter(1, toRemove.getInseratIId());
		List l = query.getResultList();
		if (l.size() == 1) {
			// Es muss mindestens 1 Kunde verbleiben
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN,
					new Exception(
							"FEHLER_INSERAT_EIN_KUNDE_MUSS_VORHANDEN_SEIN"));

		}

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

		// Es muss immer einen Kunden mit der I_SORT=1 geben, daher nach dem
		// loeschen unbedingt neu re.indizieren

		query = em
				.createNamedQuery("InseratrechnungfindDenErstenEintragByInseratIId");
		query.setParameter(1, inseratIId);

		l = query.getResultList();
		Iterator it = l.iterator();
		int iSort = 1;
		while (it.hasNext()) {
			Inseratrechnung ir = (Inseratrechnung) it.next();
			ir.setiSort(iSort);
			iSort++;

		}
	}

	public BigDecimal getZuEingangsrechnungenZugeordnetenWert(
			Integer inseratIId, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("InseraterfindByInseratIId");
		query.setParameter(1, inseratIId);

		BigDecimal bdWert = new BigDecimal(0);

		List l = query.getResultList();

		Iterator it = l.iterator();

		while (it.hasNext()) {
			Inserater inserater = (Inserater) it.next();
			bdWert = bdWert.add(inserater.getNBetrag());
		}
		return bdWert;
	}

	public BigDecimal berechneWerbeabgabeLFEinesInserates(Integer inseratIId,
			TheClientDto theClientDto) {

		InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
				inseratIId);

		BigDecimal werbeabgabe = new BigDecimal(0);

		try {
			ParametermandantDto parameterWerbeabgabe = getParameterFac()
					.getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ARTIKEL,
							ParameterFac.PARAMETER_WERBEABGABE_ARTIKEL);

			String werbeabgabeArtikel = parameterWerbeabgabe.getCWert();
			if (werbeabgabeArtikel != null
					&& werbeabgabeArtikel.trim().length() > 0) {
				parameterWerbeabgabe = getParameterFac().getMandantparameter(
						theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ARTIKEL,
						ParameterFac.PARAMETER_WERBEABGABE_PROZENT);

				double dProzent = (Integer) parameterWerbeabgabe
						.getCWertAsObject();

				ArtikelDto aDtoWerbeabgabe = getArtikelFac()
						.artikelFindByCNrMandantCNrOhneExc(werbeabgabeArtikel,
								theClientDto.getMandant());
				if (aDtoWerbeabgabe != null) {

					InseratartikelDto[] iaDtos = getInseratFac()
							.inseratartikelFindByInseratIId(inseratIId);

					// Wert der Werbeabgabepflichtigen Artikelberechnen
					BigDecimal bdWertDerAbgabepflichtigenArtikel = new BigDecimal(
							0.0000);

					int iNachkommastellenPreis = 2;
					try {
						iNachkommastellenPreis = getMandantFac()
								.getNachkommastellenPreisVK(
										theClientDto.getMandant());
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									inseratDto.getArtikelIIdInseratart(),
									theClientDto);
					if (Helper.short2boolean(aDto.getBWerbeabgabepflichtig())) {
						bdWertDerAbgabepflichtigenArtikel = bdWertDerAbgabepflichtigenArtikel
								.add(inseratDto
										.getErrechneterWertEK(iNachkommastellenPreis));
					}

					for (int i = 0; i < iaDtos.length; i++) {

						aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
								iaDtos[i].getArtikelIId(), theClientDto);
						if (Helper.short2boolean(aDto
								.getBWerbeabgabepflichtig())) {
							bdWertDerAbgabepflichtigenArtikel = bdWertDerAbgabepflichtigenArtikel
									.add(iaDtos[i].getNNettoeinzelpreisEk()
											.multiply(iaDtos[i].getNMenge()));
						}

					}

					werbeabgabe = bdWertDerAbgabepflichtigenArtikel
							.multiply(new BigDecimal(dProzent / 100));

				} else {
					// Werbeabgabeartikel nicht vorhanen
					ArrayList al = new ArrayList();
					throw new EJBExceptionLP(
							EJBExceptionLP.FEHLER_WERBEABGABEARTIKEL_NICHT_VORHANDEN,
							al, new Exception(
									"WERBEABGABEARTIKEL_NICHT_VORHANDEN: "
											+ werbeabgabeArtikel));

				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return werbeabgabe;
	}

	public void removeInserater(Integer inseraterIId, TheClientDto theClientDto) {
		Inserater toRemove = em.find(Inserater.class, inseraterIId);

		Integer inseratIId = toRemove.getInseratIId();
		try {
			em.remove(toRemove);
			em.flush();

			// Status des Inserats auf Erschienen zuruecksetzen, wenns keine
			// zuordnungen mehr gibt
			Query query = em.createNamedQuery("InseraterfindByInseratIId");
			query.setParameter(1, inseratIId);

			List l = query.getResultList();

			if (l.size() == 0) {
				Inserat inserat = em.find(Inserat.class, inseratIId);

				// SP1416, ausser es ist bereits verrechnet
				if (!inserat.getStatusCNr().equals(LocaleFac.STATUS_VERRECHNET)) {

					if (inserat.getPersonalIIdManuellverrechnen() != null) {
						inserat.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
					} else {
						inserat.setStatusCNr(LocaleFac.STATUS_ERSCHIENEN);
					}

					em.flush();
				}
				if (inserat.getBestellpositionIId() != null) {
					try {
						getBestellpositionFac().manuellErledigungAufheben(
								inserat.getBestellpositionIId(), theClientDto);
					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}
				}
			}

		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public void removeInseratartikel(Integer inseratartikelIId) {
		Inseratartikel toRemove = em.find(Inseratartikel.class,
				inseratartikelIId);

		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}

	}

	public Integer inseratArtikelRechnungKopieren(Integer inseratIIdAlt,
			Integer inseratIIdNeu, TheClientDto theClientDto) {

		// Kunden + Artikel kopieren

		InseratartikelDto[] iaDtos = inseratartikelFindByInseratIId(inseratIIdAlt);
		for (int i = 0; i < iaDtos.length; i++) {
			iaDtos[i].setIId(null);
			iaDtos[i].setInseratIId(inseratIIdNeu);
			createInseratartikel(iaDtos[i], theClientDto);
		}

	//	Query query = em
	//			.createNamedQuery("InseratrechnungfindDenErstenEintragByInseratIId");
	//	query.setParameter(1, inseratIIdAlt);

	//	List l = query.getResultList();
	//	Iterator it = l.iterator();
	//	while (it.hasNext()) {
	//		Inseratrechnung ir = (Inseratrechnung) it.next();
	//		InseratrechnungDto irDto = assembleInseratrechnungDto(ir);
			
	//		try {
	//			Query queryUK = em
	//					.createNamedQuery("InseratrechnungfindInseratIIdKundeIId");
	//			queryUK.setParameter(1, inseratIIdNeu);
	//			queryUK.setParameter(2, irDto.getKundeIId());
	//			Inseratrechnung doppelt = (Inseratrechnung) query.getSingleResult();
	//			//Auslassen
	//		} catch (NoResultException ex) {
	//			irDto.setInseratIId(inseratIIdNeu);
	//			irDto.setIId(null);
	//			irDto.setRechnungpositionIId(null);
	//			createInseratrechnung(irDto, theClientDto);
	//		}
	//		
	//		
	//	}

		return inseratIIdNeu;
	}

	public ArrayList<RechnungDto> gibtEsNochWeitereRechnungenFuerdiesesInserat(
			Integer inseratIId, TheClientDto theClientDto) {
		ArrayList<RechnungDto> al = new ArrayList<RechnungDto>();

		Query query = em
				.createNamedQuery("InseratrechnungfindDenErstenEintragByInseratIId");
		query.setParameter(1, inseratIId);

		List l = query.getResultList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Inseratrechnung ir = (Inseratrechnung) it.next();
			if (ir.getRechnungpositionIId() != null) {

				try {
					Integer rechnungIId = getRechnungFac()
							.rechnungPositionFindByPrimaryKey(
									ir.getRechnungpositionIId())
							.getRechnungIId();
					al.add(getRechnungFac().rechnungFindByPrimaryKey(
							rechnungIId));
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}

			}
		}

		return al;
	}

	public InseratDto istIseratAufRechnungspositionVorhanden(
			Integer rechnungspositionIId) {

		Query query = em
				.createNamedQuery("InseratrechnungfindByRechnungpositionIId");
		query.setParameter(1, rechnungspositionIId);
		query.setMaxResults(1);
		List l = query.getResultList();
		if (l.size() >= 1) {
			return inseratFindByPrimaryKey(((Inseratrechnung) l.get(0))
					.getInseratIId());
		} else {
			return null;
		}
	}

	public InseratDto istInseratAufBestellpositionVorhanden(
			Integer bestellpositionIId) {

		Query query = em.createNamedQuery("InseratfindByBestellpositionIId");
		query.setParameter(1, bestellpositionIId);
		query.setMaxResults(1);
		List l = query.getResultList();
		if (l.size() >= 1) {
			return inseratFindByPrimaryKey(((Inserat) l.get(0)).getIId());
		} else {
			return null;
		}
	}

	public void beziehungZuRechnungspositionAufloesenUndRechnungspositionenLoeschen(
			Integer rechnungspositionIId, TheClientDto theClientDto) {
		Query query = em
				.createNamedQuery("InseratrechnungfindByRechnungpositionIId");
		query.setParameter(1, rechnungspositionIId);
		List l = query.getResultList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Inseratrechnung ir = (Inseratrechnung) it.next();
			ir.setRechnungpositionIId(null);

			ArrayList<RechnungDto> alRechnungen = gibtEsNochWeitereRechnungenFuerdiesesInserat(
					ir.getInseratIId(), theClientDto);

			if (alRechnungen.size() == 0) {
				Inserat i = em.find(Inserat.class, ir.getInseratIId());
				i.setStatusCNr(LocaleFac.STATUS_VERRECHENBAR);
			}

			Query query2 = em
					.createNamedQuery("InseratrechnungartikelfindInseratrechnungIId");
			query2.setParameter(1, ir.getIId());
			List l2 = query2.getResultList();
			Iterator it2 = l2.iterator();
			while (it2.hasNext()) {
				Inseratrechnungartikel ira = (Inseratrechnungartikel) it2
						.next();
				try {
					RechnungPositionDto reposDto = getRechnungFac()
							.rechnungPositionFindByPrimaryKey(
									ira.getRechnungpositionIId());
					em.remove(ira);
					em.flush();
					getRechnungFac().removeRechnungPosition(reposDto,
							theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
		try {
			RechnungPositionDto reposDto = getRechnungFac()
					.rechnungPositionFindByPrimaryKey(rechnungspositionIId);
			getRechnungFac().removeRechnungPosition(reposDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Zugehoerige Positionen aus Inseratrechnungartikel loeschen

	}

	public void beziehungZuBestellpositionAufloesenUndBestellpositionenLoeschen(
			Integer bestellpositionIId, TheClientDto theClientDto) {
		Query query = em.createNamedQuery("InseratfindByBestellpositionIId");
		query.setParameter(1, bestellpositionIId);
		List l = query.getResultList();
		Iterator it = l.iterator();
		while (it.hasNext()) {
			Inserat i = (Inserat) it.next();
			i.setBestellpositionIId(null);
			i.setStatusCNr(LocaleFac.STATUS_ANGELEGT);

			Query query2 = em.createNamedQuery("InseratartikelfindInseratIId");
			query2.setParameter(1, i.getIId());
			List l2 = query2.getResultList();
			Iterator it2 = l2.iterator();
			while (it2.hasNext()) {
				Inseratartikel ira = (Inseratartikel) it2.next();
				try {
					BestellpositionDto besposDto = getBestellpositionFac()
							.bestellpositionFindByPrimaryKey(
									ira.getBestellpositionIId());

					ira.setBestellpositionIId(null);

					em.flush();
					getBestellpositionFac().removeBestellposition(besposDto,
							theClientDto);
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
		try {
			BestellpositionDto besposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(bestellpositionIId);
			getBestellpositionFac().removeBestellposition(besposDto,
					theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

		// Zugehoerige Positionen aus Inseratrechnungartikel loeschen

	}

	public void storniertAufheben(Integer inseratIId, TheClientDto theClientDto) {
		Inserat inserat = em.find(Inserat.class, inseratIId);

		if (inserat.getStatusCNr().equals(LocaleFac.STATUS_STORNIERT)) {

			if (inserat.getBestellpositionIId() != null) {
				inserat.setStatusCNr(LocaleFac.STATUS_BESTELLT);
			} else {
				inserat.setStatusCNr(LocaleFac.STATUS_ANGELEGT);
			}

			em.merge(inserat);
			em.flush();
		}
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int rechnungenAusloesen(Integer kundeIId, Integer inseratIId,
			java.sql.Date neuDatum, TheClientDto theClientDto) {
		int anz = 0;
		Session sessionKunden = FLRSessionFactory.getFactory().openSession();
		String queryStringKunden = "SELECT inserat FROM FLRInserat inserat LEFT OUTER JOIN inserat.rechnungset as rechnungset WHERE rechnungset.rechnungposition_i_id IS NULL  AND inserat.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND inserat.status_c_nr='"
				+ LocaleFac.STATUS_VERRECHENBAR + "' ";

		// PJ18049

		if (kundeIId != null) {
			queryStringKunden += " AND rechnungset.kunde_i_id=" + kundeIId;
		}
		if (inseratIId != null) {
			queryStringKunden += " AND inserat.i_id=" + inseratIId;
		}

		queryStringKunden += " ORDER BY inserat.c_nr ASC";

		org.hibernate.Query queryKunden = sessionKunden
				.createQuery(queryStringKunden);
		List<?> resultsKunden = queryKunden.list();
		Iterator<?> resultListIteratorInserate = resultsKunden.iterator();

		LinkedHashMap<Integer, LinkedHashMap<Integer, FLRInserat>> hmInserateMitEinemKundenVerdichtet = new LinkedHashMap<Integer, LinkedHashMap<Integer, FLRInserat>>();
		LinkedHashMap<Integer, FLRInserat> hmInserateMitMehrerenKunden = new LinkedHashMap<Integer, FLRInserat>();

		while (resultListIteratorInserate.hasNext()) {

			FLRInserat flrInserat = (FLRInserat) resultListIteratorInserate
					.next();

			Set s = flrInserat.getRechnungset();
			Iterator itKunden = s.iterator();

			if (s.size() == 1) {

				FLRInseratrechnung ir = (FLRInseratrechnung) itKunden.next();

				LinkedHashMap<Integer, FLRInserat> hmInserate = null;

				// Wenn Kunde Monatsrechnung angehakt hat und im Vormonat noch
				// keine Rechnung erhalten hat, wird alles bis jetzt verrechnet

				if (Helper
						.short2boolean(ir.getFlrkunde().getB_monatsrechnung())) {

					Calendar c = Calendar.getInstance();

					c.add(Calendar.MONTH, -1);
					c.set(Calendar.DAY_OF_MONTH, 1);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);

					java.sql.Date dVon = new java.sql.Date(c.getTime()
							.getTime());

					c.set(Calendar.DAY_OF_MONTH,
							c.getActualMaximum(Calendar.DAY_OF_MONTH));
					c.set(Calendar.HOUR_OF_DAY, 23);
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
					c.set(Calendar.MILLISECOND, 990);

					java.sql.Date dBis = new java.sql.Date(c.getTime()
							.getTime());
					try {
						Integer iAnzahl = getRechnungFac()
								.getAnzahlDerRechnungenVomKundenImZeitraum(
										theClientDto, ir.getKunde_i_id(), dVon,
										dBis, false);

						if (iAnzahl > 0) {

							continue;
						}

					} catch (RemoteException e) {
						throwEJBExceptionLPRespectOld(e);
					}

				}

				if (hmInserateMitEinemKundenVerdichtet.containsKey(ir
						.getKunde_i_id())) {
					hmInserate = hmInserateMitEinemKundenVerdichtet.get(ir
							.getKunde_i_id());
				} else {
					hmInserate = new LinkedHashMap<Integer, FLRInserat>();
				}

				hmInserate.put(flrInserat.getI_id(), flrInserat);

				hmInserateMitEinemKundenVerdichtet.put(ir.getKunde_i_id(),
						hmInserate);

			} else {
				hmInserateMitMehrerenKunden.put(flrInserat.getI_id(),
						flrInserat);
			}

		}

		anz += getInseratFac().rechnungenAusloesenInserateMitEinemKunden(
				hmInserateMitEinemKundenVerdichtet, neuDatum, theClientDto);
		anz += getInseratFac().rechnungenAusloesenInserateMitMehrerenKunden(
				hmInserateMitMehrerenKunden, neuDatum, theClientDto);

		sessionKunden.close();
		return anz;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int rechnungenAusloesenInserateMitEinemKunden(
			LinkedHashMap<Integer, LinkedHashMap<Integer, FLRInserat>> hmInserateMitEinemKundenVerdichtet,
			java.sql.Date neuDatum, TheClientDto theClientDto) {

		int anz = 0;

		Iterator<Integer> itKunden = hmInserateMitEinemKundenVerdichtet
				.keySet().iterator();

		while (itKunden.hasNext()) {
			Integer kundeIId = itKunden.next();
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(kundeIId,
					theClientDto);
			HashMap<Integer, FLRInserat> hmInserate = hmInserateMitEinemKundenVerdichtet
					.get(kundeIId);

			Timestamp tBelegdatum = Helper.cutTimestamp(new Timestamp(System
					.currentTimeMillis()));

			if (neuDatum != null) {
				tBelegdatum = Helper.cutTimestamp(new Timestamp(neuDatum
						.getTime()));
			}

			Iterator itInserate = hmInserate.keySet().iterator();
			while (itInserate.hasNext()) {
				Integer inseratIId = (Integer) itInserate.next();
				FLRInserat flrInserat = hmInserate.get(inseratIId);
				try {
					InseratDto inseratDto = inseratFindByPrimaryKey(flrInserat
							.getI_id());
					Integer iGeschaeftsjahr = getParameterFac()
							.getGeschaeftsjahr(theClientDto.getMandant(),
									tBelegdatum);
					// Wenn zu dem Kunden schon einen angelegte Rechnung gibt,
					// dann
					// die verwenden
					String sQuery2 = "SELECT r"
							+ " FROM FLRRechnung AS r WHERE r.flrkunde.i_id="
							+ kundeIId + " AND r.status_c_nr='"
							+ LocaleFac.STATUS_ANGELEGT
							+ "' AND r.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "' AND r.flrrechnungart.c_nr='"
							+ RechnungFac.RECHNUNGART_RECHNUNG
							+ "' AND r.i_geschaeftsjahr=" + iGeschaeftsjahr
							+ "";

					if (inseratDto.getInseratrechnungDto()
							.getAnsprechpartnerIId() != null) {
						sQuery2 += " AND r.ansprechpartner_i_id="
								+ inseratDto.getInseratrechnungDto()
										.getAnsprechpartnerIId();
					} else {
						sQuery2 += " AND r.ansprechpartner_i_id IS NULL ";
					}

					sQuery2 += " ORDER BY r.c_nr DESC";
					Session sessionRech = FLRSessionFactory.getFactory()
							.openSession();
					org.hibernate.Query rechnungen = sessionRech
							.createQuery(sQuery2);
					rechnungen.setMaxResults(1);
					List<?> resultListR = rechnungen.list();

					Integer rechnungIId = null;

					if (resultListR.iterator().hasNext()) {
						// Bestehende verwenden
						FLRRechnung r = (FLRRechnung) resultListR.iterator()
								.next();
						rechnungIId = r.getI_id();
					} else {

						// Zuerst Rechnung anlegen

						RechnungDto rechnungDto = new RechnungDto();
						rechnungDto.setMandantCNr(theClientDto.getMandant());
						rechnungDto.setKundeIId(kundeIId);
						rechnungDto.setAnsprechpartnerIId(inseratDto
								.getInseratrechnungDto()
								.getAnsprechpartnerIId());
						rechnungDto.setKundeIIdStatistikadresse(kundeIId);
						rechnungDto
								.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);

						rechnungDto.setLieferartIId(kundeDto.getLieferartIId());
						rechnungDto.setZahlungszielIId(kundeDto
								.getZahlungszielIId());
						rechnungDto.setSpediteurIId(kundeDto.getSpediteurIId());
						rechnungDto.setWaehrungCNr(kundeDto.getCWaehrung());
						rechnungDto.setLagerIId(kundeDto
								.getLagerIIdAbbuchungslager());

						rechnungDto.setTBelegdatum(tBelegdatum);

						rechnungDto.setPersonalIIdVertreter(kundeDto
								.getPersonaliIdProvisionsempfaenger());
						BigDecimal bdKurs = getLocaleFac().getWechselkurs2(
								theClientDto.getSMandantenwaehrung(),
								kundeDto.getCWaehrung(), theClientDto);
						rechnungDto.setNKurs(bdKurs);

						rechnungDto.setKostenstelleIId(kundeDto
								.getKostenstelleIId());

						rechnungIId = getRechnungFac().createRechnung(
								rechnungDto, theClientDto).getIId();
						anz++;
					}

					sessionRech.close();

					RechnungPositionDto reposDto = new RechnungPositionDto();
					reposDto.setArtikelIId(inseratDto.getArtikelIIdInseratart());
					reposDto.setBelegIId(rechnungIId);
					reposDto.setBDrucken(Helper.boolean2Short(true));

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									inseratDto.getArtikelIIdInseratart(),
									theClientDto);

					reposDto.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
					reposDto.setEinheitCNr(aDto.getEinheitCNr());

					reposDto.setBNettopreisuebersteuert(Helper
							.boolean2Short(true));
					MwstsatzDto mwst = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									kundeDto.getMwstsatzbezIId(), theClientDto);

					reposDto.setMwstsatzIId(mwst.getIId());

					double dRabattsatz = Helper
							.berechneRabattsatzMehrererRabatte(
									inseratDto.getFKdRabatt(),
									inseratDto.getFKdZusatzrabatt(),
									inseratDto.getFKdNachlass());
					reposDto.setFRabattsatz(inseratDto.getFKdRabatt());

					reposDto.setFRabattsatz(dRabattsatz);
					reposDto.setFZusatzrabattsatz(0D);
					reposDto.setNMaterialzuschlag(BigDecimal.ZERO);
					reposDto.setBDrucken(Helper.boolean2Short(true));
					reposDto.setNMenge(inseratDto.getNMenge());
					reposDto.setNEinzelpreis(inseratDto
							.getNNettoeinzelpreisVk());
					int iNachkommastellenPreis = getMandantFac()
							.getNachkommastellenPreisVK(
									theClientDto.getMandant());

					BigDecimal bdRabattbetrag = Helper
							.getProzentWert(
									inseratDto.getNNettoeinzelpreisVk(),
									new BigDecimal(dRabattsatz),
									iNachkommastellenPreis);
					BigDecimal nettoeinzeilpreis = inseratDto
							.getNNettoeinzelpreisVk().subtract(bdRabattbetrag);

					reposDto.setNNettoeinzelpreis(nettoeinzeilpreis);

					BigDecimal bdMwstbetrag = Helper.getProzentWert(
							nettoeinzeilpreis,
							new BigDecimal(mwst.getFMwstsatz()),
							iNachkommastellenPreis);

					reposDto.setNBruttoeinzelpreis(nettoeinzeilpreis
							.add(bdMwstbetrag));

					reposDto = getRechnungFac().createRechnungPosition(
							reposDto, kundeDto.getLagerIIdAbbuchungslager(),
							theClientDto);
					// Dann Im Inserat hinterlegen und ev. Status aendern

					InseratrechnungDto inseratrechnungDto = getInseratFac()
							.inseratrechnungFindByPrimaryKey(
									inseratDto.getInseratrechnungDto().getIId());

					inseratrechnungDto
							.setRechnungpositionIId(reposDto.getIId());

					getInseratFac().updateInseratrechnung(inseratrechnungDto,
							theClientDto);

					inseratDto.setStatusCNr(LocaleFac.STATUS_VERRECHNET);
					getInseratFac().updateInseratOhneWeiterAktion(inseratDto,
							theClientDto);

					// Zusatzartikel anlegen
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();
					String queryString2 = "SELECT artikel FROM FLRInseratartikel artikel WHERE artikel.inserat_i_id="
							+ inseratDto.getIId()
							+ " ORDER BY artikel.flrartikel.c_nr ASC";

					org.hibernate.Query query2 = session2
							.createQuery(queryString2);
					List<?> results2 = query2.list();
					Iterator<?> resultListIterator2 = results2.iterator();
					while (resultListIterator2.hasNext()) {
						FLRInseratartikel flrinseratartikel = (FLRInseratartikel) resultListIterator2
								.next();

						RechnungPositionDto reposDtoZusatzArtikel = new RechnungPositionDto();

						reposDtoZusatzArtikel.setArtikelIId(flrinseratartikel
								.getFlrartikel().getI_id());
						reposDtoZusatzArtikel.setBelegIId(rechnungIId);
						reposDtoZusatzArtikel.setBDrucken(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setFRabattsatz(0D);
						reposDtoZusatzArtikel
								.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
						reposDtoZusatzArtikel.setEinheitCNr(flrinseratartikel
								.getFlrartikel().getEinheit_c_nr());

						reposDtoZusatzArtikel.setBNettopreisuebersteuert(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setMwstsatzIId(mwst.getIId());

						reposDtoZusatzArtikel.setFZusatzrabattsatz(0D);
						reposDtoZusatzArtikel.setBDrucken(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setNMenge(flrinseratartikel
								.getN_menge());
						reposDtoZusatzArtikel.setNEinzelpreis(flrinseratartikel
								.getN_nettoeinzelpreis_vk());
						reposDtoZusatzArtikel
								.setNNettoeinzelpreis(flrinseratartikel
										.getN_nettoeinzelpreis_vk());

						BigDecimal bdMwstbetragZusatz = Helper.getProzentWert(
								flrinseratartikel.getN_nettoeinzelpreis_vk(),
								new BigDecimal(mwst.getFMwstsatz()), 2);

						reposDtoZusatzArtikel
								.setNBruttoeinzelpreis(flrinseratartikel
										.getN_nettoeinzelpreis_vk().add(
												bdMwstbetragZusatz));

						reposDtoZusatzArtikel = getRechnungFac()
								.createRechnungPosition(reposDtoZusatzArtikel,
										kundeDto.getLagerIIdAbbuchungslager(),
										theClientDto);

						getInseratFac().createInseratrechnungartikel(
								inseratrechnungDto.getIId(),
								reposDtoZusatzArtikel);

					}
					session2.close();
				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

		}
		return anz;
	}

	public void createInseratrechnungartikel(Integer inseratrechnungIId,
			RechnungPositionDto reposDtoZusatzArtikel) {
		// Auch in Inseratrechnungartikel hinterlegen
		PKGeneratorObj pkGen = new PKGeneratorObj(); // PKGEN
		Integer pk = pkGen.getNextPrimaryKey(PKConst.PK_INSERATRECHNUNGARTIKEL);

		Inseratrechnungartikel inseratrechnungartikel = new Inseratrechnungartikel(
				pk, inseratrechnungIId, reposDtoZusatzArtikel.getIId());
		em.persist(inseratrechnungartikel);
		em.flush();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public int rechnungenAusloesenInserateMitMehrerenKunden(
			LinkedHashMap<Integer, FLRInserat> hmInserateMitMehrerenKunden,
			java.sql.Date neuDatum, TheClientDto theClientDto) {

		int anz = 0;

		Iterator<Integer> itInserate = hmInserateMitMehrerenKunden.keySet()
				.iterator();

		while (itInserate.hasNext()) {
			Integer inseratIId = itInserate.next();

			FLRInserat flrinserat = hmInserateMitMehrerenKunden.get(inseratIId);

			Set<FLRInseratrechnung> s = flrinserat.getRechnungset();
			Iterator itKunden = flrinserat.getRechnungset().iterator();

			int iAnzahlKunden = s.size();

			if (iAnzahlKunden == 0) {
				iAnzahlKunden = 1;
			}

			BigDecimal bdRabattGesamt = new BigDecimal(100.00);

			BigDecimal bdSchritt = bdRabattGesamt.divide(new BigDecimal(
					iAnzahlKunden), 2, BigDecimal.ROUND_HALF_UP);

			BigDecimal bdKumuliert = new BigDecimal(0.00);

			// Zuerst Rechnung anlegen

			while (itKunden.hasNext()) {

				FLRInseratrechnung ir = (FLRInseratrechnung) itKunden.next();

				InseratrechnungDto inseratrechnungDto = getInseratFac()
						.inseratrechnungFindByPrimaryKey(ir.getI_id());

				RechnungDto rechnungDto = new RechnungDto();
				rechnungDto.setMandantCNr(theClientDto.getMandant());
				rechnungDto.setKundeIId(ir.getKunde_i_id());
				rechnungDto.setAnsprechpartnerIId(inseratrechnungDto
						.getAnsprechpartnerIId());
				rechnungDto.setKundeIIdStatistikadresse(ir.getKunde_i_id());
				rechnungDto.setRechnungartCNr(RechnungFac.RECHNUNGART_RECHNUNG);

				if (itKunden.hasNext() == false) {
					// Der letzte bekommt die Rundung
					rechnungDto.setFAllgemeinerRabattsatz(bdRabattGesamt
							.subtract(bdKumuliert).doubleValue());
				} else {
					rechnungDto.setFAllgemeinerRabattsatz(bdSchritt
							.doubleValue());
				}

				bdKumuliert = bdKumuliert.add(bdSchritt);

				KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
						ir.getKunde_i_id(), theClientDto);

				rechnungDto.setLieferartIId(kundeDto.getLieferartIId());
				rechnungDto.setZahlungszielIId(kundeDto.getZahlungszielIId());
				rechnungDto.setSpediteurIId(kundeDto.getSpediteurIId());
				rechnungDto.setWaehrungCNr(kundeDto.getCWaehrung());
				rechnungDto.setLagerIId(kundeDto.getLagerIIdAbbuchungslager());

				if (neuDatum != null) {
					rechnungDto.setTBelegdatum(Helper
							.cutTimestamp(new Timestamp(neuDatum.getTime())));
				} else {
					rechnungDto.setTBelegdatum(Helper
							.cutTimestamp(new Timestamp(System
									.currentTimeMillis())));
				}

				rechnungDto.setPersonalIIdVertreter(kundeDto
						.getPersonaliIdProvisionsempfaenger());

				try {

					BigDecimal bdKurs = getLocaleFac().getWechselkurs2(
							theClientDto.getSMandantenwaehrung(),
							kundeDto.getCWaehrung(), theClientDto);
					rechnungDto.setNKurs(bdKurs);

					rechnungDto.setKostenstelleIId(kundeDto
							.getKostenstelleIId());
					anz++;

					Integer iGeschaeftsjahr = getParameterFac()
							.getGeschaeftsjahr(rechnungDto.getMandantCNr(),
									rechnungDto.getTBelegdatum());

					// Wenn zu dem Kunden schon einen angelegte Rechnung gibt,
					// dann
					// die verwenden
					String sQuery2 = "SELECT r"
							+ " FROM FLRRechnung AS r WHERE r.flrkunde.i_id="
							+ ir.getKunde_i_id() + " AND r.status_c_nr='"
							+ LocaleFac.STATUS_ANGELEGT
							+ "' AND r.mandant_c_nr='"
							+ theClientDto.getMandant()
							+ "' AND r.flrrechnungart.c_nr='"
							+ RechnungFac.RECHNUNGART_RECHNUNG
							+ "' AND r.i_geschaeftsjahr=" + iGeschaeftsjahr;

					if (inseratrechnungDto.getAnsprechpartnerIId() != null) {
						sQuery2 += " AND r.ansprechpartner_i_id="
								+ inseratrechnungDto.getAnsprechpartnerIId();
					} else {
						sQuery2 += " AND r.ansprechpartner_i_id IS NULL ";
					}

					sQuery2 += " ORDER BY r.c_nr DESC";

					Session sessionRech = FLRSessionFactory.getFactory()
							.openSession();
					org.hibernate.Query rechnungen = sessionRech
							.createQuery(sQuery2);
					rechnungen.setMaxResults(1);
					List<?> resultListR = rechnungen.list();

					Integer rechnungIId = null;

					if (resultListR.iterator().hasNext()) {
						// Bestehende verwenden
						FLRRechnung r = (FLRRechnung) resultListR.iterator()
								.next();
						rechnungIId = r.getI_id();
					} else {
						rechnungIId = getRechnungFac().createRechnung(
								rechnungDto, theClientDto).getIId();
					}

					// Dann Rechnungsposition anlegen

					InseratDto inseratDto = inseratFindByPrimaryKey(flrinserat
							.getI_id());

					RechnungPositionDto reposDto = new RechnungPositionDto();
					reposDto.setArtikelIId(inseratDto.getArtikelIIdInseratart());
					reposDto.setBelegIId(rechnungIId);
					reposDto.setBDrucken(Helper.boolean2Short(true));

					reposDto.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);

					ArtikelDto aDto = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									inseratDto.getArtikelIIdInseratart(),
									theClientDto);

					reposDto.setEinheitCNr(aDto.getEinheitCNr());

					reposDto.setBNettopreisuebersteuert(Helper
							.boolean2Short(true));
					MwstsatzDto mwst = getMandantFac()
							.mwstsatzFindByMwstsatzbezIIdAktuellster(
									kundeDto.getMwstsatzbezIId(), theClientDto);

					reposDto.setMwstsatzIId(mwst.getIId());

					double dRabattsatz = Helper
							.berechneRabattsatzMehrererRabatte(
									inseratDto.getFKdRabatt(),
									inseratDto.getFKdZusatzrabatt(),
									inseratDto.getFKdNachlass());
					reposDto.setFRabattsatz(inseratDto.getFKdRabatt());

					int iNachkommastellenPreis = getMandantFac()
							.getNachkommastellenPreisVK(
									theClientDto.getMandant());

					reposDto.setFRabattsatz(dRabattsatz);
					reposDto.setFZusatzrabattsatz(0D);
					reposDto.setNMaterialzuschlag(BigDecimal.ZERO);
					reposDto.setBDrucken(Helper.boolean2Short(true));
					reposDto.setNMenge(inseratDto.getNMenge());
					reposDto.setNEinzelpreis(inseratDto
							.getNNettoeinzelpreisVk());
					BigDecimal bdRabattbetrag = Helper
							.getProzentWert(
									inseratDto.getNNettoeinzelpreisVk(),
									new BigDecimal(dRabattsatz),
									iNachkommastellenPreis);
					BigDecimal nettoeinzeilpreis = inseratDto
							.getNNettoeinzelpreisVk().subtract(bdRabattbetrag);

					reposDto.setNNettoeinzelpreis(nettoeinzeilpreis);

					BigDecimal bdMwstbetrag = Helper.getProzentWert(
							nettoeinzeilpreis,
							new BigDecimal(mwst.getFMwstsatz()),
							iNachkommastellenPreis);

					reposDto.setNBruttoeinzelpreis(nettoeinzeilpreis
							.add(bdMwstbetrag));

					reposDto = getRechnungFac().createRechnungPosition(
							reposDto, rechnungDto.getLagerIId(), theClientDto);
					// Dann Im Inserat hinterlegen und ev. Status aendern

					inseratrechnungDto
							.setRechnungpositionIId(reposDto.getIId());

					getInseratFac().updateInseratrechnung(inseratrechnungDto,
							theClientDto);

					// Zusatzartikel anlegen
					Session session2 = FLRSessionFactory.getFactory()
							.openSession();
					String queryString2 = "SELECT artikel FROM FLRInseratartikel artikel WHERE artikel.inserat_i_id="
							+ inseratDto.getIId()
							+ " ORDER BY artikel.flrartikel.c_nr ASC";

					org.hibernate.Query query2 = session2
							.createQuery(queryString2);
					List<?> results2 = query2.list();
					Iterator<?> resultListIterator2 = results2.iterator();
					while (resultListIterator2.hasNext()) {
						FLRInseratartikel flrinseratartikel = (FLRInseratartikel) resultListIterator2
								.next();

						RechnungPositionDto reposDtoZusatzArtikel = new RechnungPositionDto();

						reposDtoZusatzArtikel.setArtikelIId(flrinseratartikel
								.getFlrartikel().getI_id());
						reposDtoZusatzArtikel.setBelegIId(rechnungIId);
						reposDtoZusatzArtikel.setBDrucken(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setFRabattsatz(0D);
						reposDtoZusatzArtikel
								.setPositionsartCNr(RechnungFac.POSITIONSART_RECHNUNG_IDENT);
						reposDtoZusatzArtikel.setEinheitCNr(flrinseratartikel
								.getFlrartikel().getEinheit_c_nr());

						reposDtoZusatzArtikel.setBNettopreisuebersteuert(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setMwstsatzIId(mwst.getIId());

						reposDtoZusatzArtikel.setFZusatzrabattsatz(0D);
						reposDtoZusatzArtikel.setBDrucken(Helper
								.boolean2Short(true));
						reposDtoZusatzArtikel.setNMenge(flrinseratartikel
								.getN_menge());
						reposDtoZusatzArtikel.setNEinzelpreis(flrinseratartikel
								.getN_nettoeinzelpreis_vk());
						reposDtoZusatzArtikel
								.setNNettoeinzelpreis(flrinseratartikel
										.getN_nettoeinzelpreis_vk());

						BigDecimal bdMwstbetragZusatz = Helper.getProzentWert(
								flrinseratartikel.getN_nettoeinzelpreis_vk(),
								new BigDecimal(mwst.getFMwstsatz()), 2);

						reposDtoZusatzArtikel
								.setNBruttoeinzelpreis(flrinseratartikel
										.getN_nettoeinzelpreis_vk().add(
												bdMwstbetragZusatz));

						reposDtoZusatzArtikel = getRechnungFac()
								.createRechnungPosition(reposDtoZusatzArtikel,
										rechnungDto.getLagerIId(), theClientDto);

						getInseratFac().createInseratrechnungartikel(
								inseratrechnungDto.getIId(),
								reposDtoZusatzArtikel);

					}
					session2.close();

				} catch (RemoteException e) {
					throwEJBExceptionLPRespectOld(e);
				}
			}

			InseratDto inseratDto = getInseratFac().inseratFindByPrimaryKey(
					flrinserat.getI_id());

			inseratDto.setStatusCNr(LocaleFac.STATUS_VERRECHNET);
			getInseratFac().updateInseratOhneWeiterAktion(inseratDto,
					theClientDto);

		}
		return anz;
	}

	public void preisInBestellpositionRueckpflegenAusKopfdaten(
			Integer inseratIId, TheClientDto theClientDto) {

		// 1.) Position aus Inserat-Kopfdaten
		InseratDto inseratDto = inseratFindByPrimaryKey(inseratIId);

		try {
			BestellpositionDto bestposDto = getBestellpositionFac()
					.bestellpositionFindByPrimaryKey(
							inseratDto.getBestellpositionIId());
			// Preise und Artikel aendern
			bestposDto.setArtikelIId(inseratDto.getArtikelIIdInseratart());
			bestposDto
					.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
			ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
					inseratDto.getArtikelIIdInseratart(), theClientDto);
			bestposDto.setEinheitCNr(aDto.getEinheitCNr());
			Double fRabattsatz = Helper.berechneRabattsatzMehrererRabatte(
					inseratDto.getFLFRabatt(), inseratDto.getFLfZusatzrabatt(),
					inseratDto.getFLfNachlass());

			if (inseratDto.getInseratrechnungDto() != null) {
				bestposDto.setKundeIId(inseratDto.getInseratrechnungDto()
						.getKundeIId());
			}

			bestposDto.setNMenge(inseratDto.getNMenge());

			bestposDto.setDRabattsatz(fRabattsatz);

			bestposDto
					.setNNettoeinzelpreis(inseratDto.getNNettoeinzelpreisEk());

			BigDecimal bdRabattbetrag = Helper.getProzentWert(bestposDto
					.getNNettoeinzelpreis(),
					new BigDecimal(bestposDto.getDRabattsatz()), 2);

			bestposDto.setNRabattbetrag(bdRabattbetrag);

			bestposDto.setNNettogesamtpreis(bestposDto.getNNettoeinzelpreis()
					.subtract(bdRabattbetrag));
			getBestellpositionFac()
					.updateBestellposition(
							bestposDto,
							theClientDto,
							BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
							null);

		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}

	}

	public void preisInBestellpositionRueckpflegenAusInseratArtikel(
			Integer inseratartikelIId, TheClientDto theClientDto) {

		// Positionen aus Inserat-Artikel
		InseratartikelDto iaDto = inseratartikelFindByPrimaryKey(inseratartikelIId);
		InseratDto inseratDto = inseratFindByPrimaryKey(iaDto.getInseratIId());
		if (iaDto.getBestellpositionIId() != null) {

			try {
				BestellpositionDto bestposDto = getBestellpositionFac()
						.bestellpositionFindByPrimaryKey(
								iaDto.getBestellpositionIId());
				// Preise und Artikel aendern
				bestposDto.setArtikelIId(iaDto.getArtikelIId());

				bestposDto
						.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
				ArtikelDto aDto = getArtikelFac().artikelFindByPrimaryKeySmall(
						iaDto.getArtikelIId(), theClientDto);
				bestposDto.setEinheitCNr(aDto.getEinheitCNr());

				if (inseratDto.getInseratrechnungDto() != null) {
					bestposDto.setKundeIId(inseratDto.getInseratrechnungDto()
							.getKundeIId());
				}

				bestposDto.setNNettoeinzelpreis(iaDto.getNNettoeinzelpreisEk());

				bestposDto.setDRabattsatz(0D);

				bestposDto.setNRabattbetrag(new BigDecimal(0));

				bestposDto.setNNettogesamtpreis(iaDto.getNNettoeinzelpreisEk());
				getBestellpositionFac()
						.updateBestellposition(
								bestposDto,
								theClientDto,
								BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
								null);

			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public ArrayList<Integer> bestellungenAusloesen(Integer lieferantIId,
			Integer kundeIId, TheClientDto theClientDto) {
		// Eine Bestellung erzeugen

		try {

			LinkedHashMap<Integer, LinkedHashMap<java.util.Date, ArrayList<FLRInserat>>> hmAnsprechpartner = new LinkedHashMap<Integer, LinkedHashMap<java.util.Date, ArrayList<FLRInserat>>>();
			// Nun fuer jeden Erscheinungstermin eine Bestellung erzeugen
			Session session = FLRSessionFactory.getFactory().openSession();
			String queryString = "SELECT ins FROM FLRInserat ins LEFT OUTER JOIN ins.rechnungset as rechnungset WHERE rechnungset.kunde_i_id="
					+ kundeIId
					+ " AND ins.bestellposition_i_id IS NULL  AND ins.mandant_c_nr='"
					+ theClientDto.getMandant()
					+ "' AND ins.lieferant_i_id="
					+ lieferantIId
					+ " AND ins.status_c_nr <>'"
					+ LocaleFac.STATUS_STORNIERT
					+ "' ORDER BY ins.t_termin ASC, ins.c_nr ASC";

			org.hibernate.Query query = session.createQuery(queryString);
			List<?> results = query.list();
			Iterator<?> resultListIterator = results.iterator();
			while (resultListIterator.hasNext()) {
				FLRInserat flrInserat = (FLRInserat) resultListIterator.next();

				InseratDto inseratDto = inseratFindByPrimaryKey(flrInserat
						.getI_id());

				Integer ansprechpartnerIId = inseratDto
						.getAnsprechpartnerIIdLieferant();

				LinkedHashMap<java.util.Date, ArrayList<FLRInserat>> hmTermine = null;
				if (hmAnsprechpartner.containsKey(ansprechpartnerIId)) {
					hmTermine = hmAnsprechpartner.get(ansprechpartnerIId);
				} else {
					hmTermine = new LinkedHashMap<java.util.Date, ArrayList<FLRInserat>>();
				}

				ArrayList<FLRInserat> alInserate = null;
				if (hmTermine.containsKey(flrInserat.getT_termin())) {
					alInserate = hmTermine.get(flrInserat.getT_termin());
				} else {
					alInserate = new ArrayList<FLRInserat>();
				}
				alInserate.add(flrInserat);

				hmTermine.put(flrInserat.getT_termin(), alInserate);

				hmAnsprechpartner.put(ansprechpartnerIId, hmTermine);

			}

			ArrayList<Integer> alBestellungIId = new ArrayList<Integer>();

			Iterator<Integer> itansprechpartner = hmAnsprechpartner.keySet()
					.iterator();

			while (itansprechpartner.hasNext()) {

				Integer ansprechpartnerIId = itansprechpartner.next();
				HashMap<java.util.Date, ArrayList<FLRInserat>> hmTermine = hmAnsprechpartner
						.get(ansprechpartnerIId);

				Iterator<java.util.Date> itTermine = hmTermine.keySet()
						.iterator();

				while (itTermine.hasNext()) {
					java.util.Date dTermin = itTermine.next();
					BestellungDto bestellungDto = getBestellungFac()
							.createBestellungDto(lieferantIId,
									theClientDto.getMandant(),
									theClientDto.getIDPersonal());
					bestellungDto.setDBelegdatum(Helper
							.cutDate(new java.sql.Date(System
									.currentTimeMillis())));
					bestellungDto.setAnsprechpartnerIId(ansprechpartnerIId);
					bestellungDto.setDLiefertermin(Helper
							.cutTimestamp(new java.sql.Timestamp(dTermin
									.getTime())));
					Integer bestellungIId = getBestellungFac()
							.createBestellung(bestellungDto, theClientDto);
					bestellungDto = getBestellungFac()
							.bestellungFindByPrimaryKey(bestellungIId);

					// Allg. Rabatt auf 0 setzen, da der Rabatt in den
					// Positionen enthalten ist
					bestellungDto.setFAllgemeinerRabattsatz(0D);
					getBestellungFac().updateBestellung(bestellungDto,
							theClientDto);

					alBestellungIId.add(bestellungIId);

					ArrayList<FLRInserat> inserate = hmTermine.get(dTermin);
					Iterator<FLRInserat> itInserate = inserate.iterator();

					while (itInserate.hasNext()) {
						FLRInserat flrInserat = itInserate.next();

						InseratDto inseratDto = inseratFindByPrimaryKey(flrInserat
								.getI_id());

						BestellpositionDto bestposDto = getBestellungFac()
								.createBestellPositionDto(
										bestellungIId,
										lieferantIId,
										flrInserat.getFlrartikel_inseratart()
												.getI_id(),
										flrInserat.getN_menge(), theClientDto);

						bestposDto.setNNettoeinzelpreis(flrInserat
								.getN_nettoeinzelpreis_ek());
						bestposDto
								.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
						bestposDto.setEinheitCNr(flrInserat
								.getFlrartikel_inseratart().getEinheit_c_nr());

						Double fRabattsatz = Helper
								.berechneRabattsatzMehrererRabatte(
										inseratDto.getFLFRabatt(),
										inseratDto.getFLfZusatzrabatt(),
										inseratDto.getFLfNachlass());

						bestposDto.setDRabattsatz(fRabattsatz);

						bestposDto.setNNettoeinzelpreis(flrInserat
								.getN_nettoeinzelpreis_ek());

						BigDecimal bdRabattbetrag = Helper.getProzentWert(
								bestposDto.getNNettoeinzelpreis(),
								new BigDecimal(bestposDto.getDRabattsatz()), 2);

						bestposDto.setNRabattbetrag(bdRabattbetrag);

						bestposDto.setNNettogesamtpreis(bestposDto
								.getNNettoeinzelpreis()
								.subtract(bdRabattbetrag));

						bestposDto.setBNettopreisuebersteuert(Helper
								.boolean2Short(true));

						bestposDto.setTUebersteuerterLiefertermin(inseratDto
								.getTTermin());
						if (inseratDto.getInseratrechnungDto() != null) {
							bestposDto.setKundeIId(inseratDto
									.getInseratrechnungDto().getKundeIId());
						}
						bestposDto.setArtikelIId(flrInserat
								.getFlrartikel_inseratart().getI_id());
						Integer bestellpositionIId = getBestellpositionFac()
								.createBestellposition(
										bestposDto,
										theClientDto,
										BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
										null);
						inseratDto.setBestellpositionIId(bestellpositionIId);
						inseratDto.setStatusCNr(LocaleFac.STATUS_BESTELLT);
						getInseratFac().updateInserat(inseratDto, theClientDto);

						// Zusatzartikel anlegen
						Session session2 = FLRSessionFactory.getFactory()
								.openSession();
						String queryString2 = "SELECT artikel FROM FLRInseratartikel artikel WHERE artikel.inserat_i_id="
								+ inseratDto.getIId()
								+ " ORDER BY artikel.flrartikel.c_nr ASC";

						org.hibernate.Query query2 = session2
								.createQuery(queryString2);
						List<?> results2 = query2.list();
						Iterator<?> resultListIterator2 = results2.iterator();
						while (resultListIterator2.hasNext()) {
							FLRInseratartikel flrinseratartikel = (FLRInseratartikel) resultListIterator2
									.next();

							BestellpositionDto bestposDtoZusatzartikel = getBestellungFac()
									.createBestellPositionDto(
											bestellungIId,
											lieferantIId,
											flrinseratartikel.getFlrartikel()
													.getI_id(),
											flrinseratartikel.getN_menge(),
											theClientDto);

							bestposDtoZusatzartikel
									.setNNettoeinzelpreis(flrinseratartikel
											.getN_nettoeinzelpreis_ek());
							bestposDtoZusatzartikel
									.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);

							bestposDtoZusatzartikel
									.setEinheitCNr(flrinseratartikel
											.getFlrartikel().getEinheit_c_nr());
							bestposDtoZusatzartikel.setDRabattsatz(0D);

							bestposDtoZusatzartikel
									.setNRabattbetrag(new BigDecimal(0));

							bestposDtoZusatzartikel
									.setNNettogesamtpreis(flrinseratartikel
											.getN_nettoeinzelpreis_ek());

							bestposDtoZusatzartikel
									.setBNettopreisuebersteuert(Helper
											.boolean2Short(true));

							bestposDtoZusatzartikel
									.setTUebersteuerterLiefertermin(inseratDto
											.getTTermin());
							if (inseratDto.getInseratrechnungDto() != null) {
								bestposDtoZusatzartikel.setKundeIId(inseratDto
										.getInseratrechnungDto().getKundeIId());
							}

							Integer bestposIId = getBestellpositionFac()
									.createBestellposition(
											bestposDtoZusatzartikel,
											theClientDto,
											BestellpositionFac.PREISPFLEGEARTIKELLIEFERANT_PREIS_UNVERAENDERT,
											null);

							InseratartikelDto iaDto = inseratartikelFindByPrimaryKey(flrinseratartikel
									.getI_id());

							iaDto.setBestellpositionIId(bestposIId);

							getInseratFac().updateInseratartikel(iaDto,
									theClientDto);

						}
						session2.close();
					}
				}
			}
			session.close();
			return alBestellungIId;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
			return null;
		}
	}

	public ArrayList<Integer> getAllLieferantIIdsAusInseratenOhneBestellung(
			Integer kundeIId, TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(ins.lieferant_i_id) FROM FLRInserat ins LEFT OUTER JOIN ins.rechnungset as rechnungset WHERE rechnungset.kunde_i_id="
				+ kundeIId
				+ " AND  ins.bestellposition_i_id IS NULL  AND ins.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND ins.status_c_nr <>'"
				+ LocaleFac.STATUS_STORNIERT + "'";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Integer> alLieferantIIds = new ArrayList<Integer>();
		while (resultListIterator.hasNext()) {
			Integer i = (Integer) resultListIterator.next();
			alLieferantIIds.add(i);
		}
		session.close();

		return alLieferantIIds;
	}

	public ArrayList<Integer> getAllKundeIIdsAusInseratenOhneBestellung(
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(re.kunde_i_id) FROM FLRInseratrechnung re WHERE re.flrinserat.bestellposition_i_id IS NULL  AND re.flrinserat.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND re.flrinserat.status_c_nr <>'"
				+ LocaleFac.STATUS_STORNIERT + "'";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Integer> alLieferantIIds = new ArrayList<Integer>();
		while (resultListIterator.hasNext()) {
			Integer i = (Integer) resultListIterator.next();
			alLieferantIIds.add(i);
		}
		session.close();

		return alLieferantIIds;
	}

	public ArrayList<Integer> getAllLieferantIIdsAusOffenenInseraten(
			TheClientDto theClientDto) {

		Session session = FLRSessionFactory.getFactory().openSession();

		String queryString = "SELECT distinct(ins.lieferant_i_id) FROM FLRInserat ins LEFT OUTER JOIN ins.erset as erset WHERE erset.i_id IS NULL  AND ins.mandant_c_nr='"
				+ theClientDto.getMandant()
				+ "' AND ins.status_c_nr<>'"
				+ LocaleFac.STATUS_STORNIERT + "')";

		org.hibernate.Query query = session.createQuery(queryString);
		List<?> results = query.list();
		Iterator<?> resultListIterator = results.iterator();

		ArrayList<Integer> alLieferantIIds = new ArrayList<Integer>();
		while (resultListIterator.hasNext()) {
			Integer i = (Integer) resultListIterator.next();
			alLieferantIIds.add(i);
		}
		session.close();

		return alLieferantIIds;
	}

	private void setInseratFromInseratDto(Inserat inserat, InseratDto inseratDto)
			throws EJBExceptionLP {
		inserat.setCNr(inseratDto.getCNr());
		inserat.setMandantCNr(inseratDto.getMandantCNr());
		inserat.setTBelegdatum(inseratDto.getTBelegdatum());

		inserat.setFKdRabatt(inseratDto.getFKdRabatt());
		inserat.setFKdZusatzrabatt(inseratDto.getFKdZusatzrabatt());
		inserat.setFKdNachlass(inseratDto.getFKdNachlass());
		inserat.setCBez(inseratDto.getCBez());
		inserat.setCRubrik(inseratDto.getCRubrik());
		inserat.setCRubrik2(inseratDto.getCRubrik2());
		inserat.setCStichwort(inseratDto.getCStichwort());
		inserat.setCStichwort2(inseratDto.getCStichwort2());
		inserat.setCMedium(inseratDto.getCMedium());
		inserat.setXAnhang(inseratDto.getXAnhang());
		inserat.setLieferantIId(inseratDto.getLieferantIId());
		inserat.setAnsprechpartnerIIdLieferant(inseratDto
				.getAnsprechpartnerIIdLieferant());
		inserat.setFLFRabatt(inseratDto.getFLFRabatt());
		inserat.setFLfZusatzrabatt(inseratDto.getFLfZusatzrabatt());
		inserat.setFLfNachlass(inseratDto.getFLfNachlass());
		inserat.setXAnhangLf(inseratDto.getXAnhangLf());
		inserat.setTTermin(inseratDto.getTTermin());
		inserat.setArtikelIIdInseratart(inseratDto.getArtikelIIdInseratart());
		inserat.setStatusCNr(inseratDto.getStatusCNr());
		inserat.setPersonalIIdVertreter(inseratDto.getPersonalIIdVertreter());
		inserat.setNMenge(inseratDto.getNMenge());
		inserat.setNNettoeinzelpreisEk(inseratDto.getNNettoeinzelpreisEk());
		inserat.setNNettoeinzelpreisVk(inseratDto.getNNettoeinzelpreisVk());
		inserat.setTErschienen(inseratDto.getTErschienen());
		inserat.setPersonalIIdErschienen(inseratDto.getPersonalIIdErschienen());
		inserat.setTVerrechnen(inseratDto.getTVerrechnen());
		inserat.setPersonalIIdVerrechnen(inseratDto.getPersonalIIdVerrechnen());
		inserat.setCGestoppt(inseratDto.getCGestoppt());
		inserat.setPersonalIIdAnlegen(inseratDto.getPersonalIIdAnlegen());
		inserat.setTAnlegen(inseratDto.getTAnlegen());
		inserat.setPersonalIIdAendern(inseratDto.getPersonalIIdAendern());
		inserat.setTAendern(inseratDto.getTAendern());
		inserat.setBestellpositionIId(inseratDto.getBestellpositionIId());
		inserat.setTTerminBis(inseratDto.getTTerminBis());
		inserat.setBDruckBestellungKd(inseratDto.getBDruckBestellungKd());
		inserat.setBDruckBestellungLf(inseratDto.getBDruckBestellungLf());
		inserat.setBDruckRechnungKd(inseratDto.getBDruckRechnungKd());
		inserat.setPersonalIIdManuellerledigt(inseratDto
				.getPersonalIIdManuellerledigt());
		inserat.setTManuellerledigt(inseratDto.getTManuellerledigt());
		inserat.setBWertaufteilen(inseratDto.getBWertaufteilen());
		inserat.setTManuellverrechnen(inseratDto.getTManuellverrechnen());
		inserat.setPersonalIIdManuellverrechnen(inseratDto
				.getPersonalIIdManuellverrechnen());

		em.merge(inserat);
		em.flush();
	}

	private void setInseraterFromInseraterDto(Inserater inserater,
			InseraterDto inseraterDto) throws EJBExceptionLP {
		inserater.setEingangsrechnungIId(inseraterDto.getEingangsrechnungIId());
		inserater.setInseratIId(inseraterDto.getInseratIId());
		inserater.setCText(inseraterDto.getCText());
		inserater.setNBetrag(inseraterDto.getNBetrag());

		inserater.setPersonalIIdAnlegen(inseraterDto.getPersonalIIdAnlegen());
		inserater.setTAnlegen(inseraterDto.getTAnlegen());
		inserater.setPersonalIIdAendern(inseraterDto.getPersonalIIdAendern());
		inserater.setTAendern(inseraterDto.getTAendern());
		em.merge(inserater);
		em.flush();
	}

	private void setInseratrechnungFromInseratrechnungDto(
			Inseratrechnung inseratrechnung,
			InseratrechnungDto inseratrechnungDto) throws EJBExceptionLP {
		inseratrechnung.setKundeIId(inseratrechnungDto.getKundeIId());
		inseratrechnung.setAnsprechpartnerIId(inseratrechnungDto
				.getAnsprechpartnerIId());
		inseratrechnung.setRechnungpositionIId(inseratrechnungDto
				.getRechnungpositionIId());
		inseratrechnung.setInseratIId(inseratrechnungDto.getInseratIId());
		inseratrechnung.setiSort(inseratrechnungDto.getiSort());

		em.merge(inseratrechnung);
		em.flush();
	}

	private void setInseratartikelFromInseratartikelDto(
			Inseratartikel inseratrechnung, InseratartikelDto inseratrechnungDto)
			throws EJBExceptionLP {
		inseratrechnung.setArtikelIId(inseratrechnungDto.getArtikelIId());
		inseratrechnung.setInseratIId(inseratrechnungDto.getInseratIId());
		inseratrechnung.setNMenge(inseratrechnungDto.getNMenge());
		inseratrechnung.setNNettoeinzelpreisEk(inseratrechnungDto
				.getNNettoeinzelpreisEk());
		inseratrechnung.setNNettoeinzelpreisVk(inseratrechnungDto
				.getNNettoeinzelpreisVk());

		inseratrechnung.setBestellpositionIId(inseratrechnungDto
				.getBestellpositionIId());

		em.merge(inseratrechnung);
		em.flush();
	}

	@Override
	public List<InseratDto> inseratFindByLieferantIId(Integer lieferantIId) {
		HvTypedQuery<Inserat> query = new HvTypedQuery<Inserat>(
				em.createNamedQuery("InseratfindByLieferantIId"));
		query.setParameter(1, lieferantIId);
		return assembleInseratDtos(query.getResultList());
	}

	@Override
	public List<InseratDto> inseratFindByKundeIId(Integer kundeIId) {
		HvTypedQuery<Inseratrechnung> query = new HvTypedQuery<Inseratrechnung>(
				em.createNamedQuery("InseratrechnungfindByKundeIId"));
		query.setParameter(1, kundeIId);
		List<InseratDto> inserate = new ArrayList<InseratDto>();
		for (Inseratrechnung ire : query.getResultList()) {
			inserate.add(inseratFindByPrimaryKey(ire.getInseratIId()));
		}
		return inserate;
	}

}
