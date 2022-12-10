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
package com.lp.server.kueche.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.HibernateException;
import org.hibernate.Session;
// import org.jboss.ws.core.CommonMessageContext;
// import org.jboss.ws.core.soap.MessageContextAssociation;
// import org.jboss.wsf.spi.invocation.WebServiceContextEJB;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.ejb.Artikelspr;
import com.lp.server.artikel.ejb.Lager;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.auftrag.ejb.Auftrag;
import com.lp.server.auftrag.ejb.Auftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionDtoAssembler;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.lieferschein.ejb.Lieferschein;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.ejb.Partnerklasse;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeWareneingang;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.ProtokollDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LpListWrapper;

//@WebService(name="Kueche", serviceName="Kueche")
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class KuecheFacBeanWS extends KuecheFacBean {
	@PersistenceContext	private EntityManager em;
	@Resource EJBContext ejbContext;
	@Resource WebServiceContext wsContext;

	private final static int _BESPOS_MENGE = 0;
	private final static int _BESPOS_VERBRAUCH = 1;
	private final static int _BESPOS_ARTIKELNR = 2;
	private final static int _BESPOS_ARTIKELBEZ = 2;
	private final static int _BESPOS_ARTIKELID = 4;
	private final static int _BESPOS_ERLEDIGT = 5;
	private final static int _BESPOS_EINHEIT = 6;
	private final static int _BESPOS_UMRECHNUNG = 7;
	private final static int _BESPOS_ARTIKELNRLIEFERANT = 8;
	private final static int _BESPOS_EINHEITINVERS = 9;
	
	private final static int _WEPOS_MENGE = 0;
	private final static int _WEPOS_OFFEN = 1;
	private final static int _WEPOS_ARTIKELNR = 2;
	private final static int _WEPOS_ARTIKELBEZ = 2;
	private final static int _WEPOS_ARTIKELID = 4;
	private final static int _WEPOS_ERLEDIGT = 5;
	private final static int _WEPOS_EINHEIT = 6;
	private final static int _WEPOS_UMRECHNUNG = 7;
	private final static int _WEPOS_ARTIKELNRLIEFERANT = 8;
	private final static int _WEPOS_EINHEITINVERS = 9;
	
	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param jahr
	 * @param monat
	 * @param tag
	 * @return	CSV Liste der Speiseplanpositionen f&uuml;r das gew&auml;hlte Datum
	 */
	@WebMethod
	@WebResult(name="positionList")
	public String getSpeiseplanListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag) {
		check(idUser);

		Calendar c = Calendar.getInstance();
		c.set(jahr, monat - 1, tag, 0, 0, 0);
		java.sql.Timestamp ts = new java.sql.Timestamp(c.getTimeInMillis());
		ts = Helper.cutTimestamp(ts);

		Session session = FLRSessionFactory.getFactory().openSession();
		// TODO: Filter fuer Fertigungsgruppe Gaestehaus
		String sQuery = "SELECT p.flrkassaartikel.c_bez, "
			+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=p.flrstueckliste.flrartikel.i_id AND spr.Id.locale='deAT'), "
			+ "p.flrstueckliste.flrartikel.i_id "
			+ "FROM FLRSpeiseplan p "  
			+ "JOIN p.flrkassaartikel "
			+ "JOIN p.flrstueckliste.flrartikel "
			+ "WHERE p.mandant_c_nr='" + mandantCNr + "' "
			+ "AND p.t_datum=:date "
			+ "ORDER BY p.flrkassaartikel.c_bez";

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setDate("date", ts);    
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param jahr
	 * @param monat
	 * @param tag
	 * @return	CSV Liste der Speiseplanpositionen f&uuml;r das gew&auml;hlte Datum inkl. Vorbestellung
	 */
	@WebMethod
	@WebResult(name="positionList")
	public String getSpeiseplanListeHvma(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iJahr") int jahr,
			@WebParam(name = "iMonat") int monat,
			@WebParam(name = "iTag") int tag) {
		TheClientDto theClientDto =  check(idUser);
		Kunde kunde = getKunde(theClientDto);
		
		Calendar c = Calendar.getInstance();
		c.set(jahr, monat - 1, tag, 0, 0, 0);
		java.sql.Timestamp ts = new java.sql.Timestamp(c.getTimeInMillis());
		ts = Helper.cutTimestamp(ts);

		AuftragDto auftrag = getAuftrag(kunde.getIId(), ts, theClientDto);

		Session session = FLRSessionFactory.getFactory().openSession();
		// TODO: Filter fuer Fertigungsgruppe Gaestehaus
		String sQuery = "SELECT p.flrkassaartikel.c_bez, "
			+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=p.flrstueckliste.flrartikel.i_id AND spr.Id.locale='deAT'), "
			+ "p.flrstueckliste.flrartikel.i_id, "
			+ "0, 0 "
			+ "FROM FLRSpeiseplan p "  
			+ "JOIN p.flrkassaartikel "
			+ "JOIN p.flrstueckliste.flrartikel "
			+ "WHERE p.mandant_c_nr='" + mandantCNr + "' "
			+ "AND p.t_datum=:date "
			+ "ORDER BY p.flrkassaartikel.c_bez";

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setDate("date", ts);
		List<?> result = query.list();
		if (auftrag != null) {
			// Mengen hinzufügen
			Query query1 = em.createNamedQuery("AuftragpositionfindByAuftrag");
			query1.setParameter(1, auftrag.getIId());
			@SuppressWarnings("unchecked")
			List<Auftragposition> pos = query1.getResultList();
			if (pos.size() > 0 && result.size() > 0) {
				HashMap<Integer, Auftragposition> map = new HashMap<Integer, Auftragposition>();
				for (int i=0; i<pos.size(); i++) {
					map.put(pos.get(i).getArtikelIId(), pos.get(i));
				}
				for (int i=0; i<result.size(); i++) {
					if (map.containsKey((Integer)((Object[])result.get(i))[2])) {
						Auftragposition p = map.get((Integer)((Object[])result.get(i))[2]);
						((Object[])result.get(i))[3] = p.getNMenge();
						((Object[])result.get(i))[4] = p.getIId();
					}
				}
			}
		}
		LpListWrapper resultList = new LpListWrapper(result);
		return resultList.toCSV();
	}

	private AuftragDto getAuftrag(Integer kundeId, Timestamp ts, TheClientDto theClientDto) {
		AuftragDto auftrag = null;
		try {
			AuftragDto[] auftraege = getAuftragFac().auftragFindByKundeIIdAuftragsadresseMandantCNrOhneExc(kundeId, theClientDto.getMandant(), theClientDto);
			for (int i=0; i<auftraege.length; i++) {
				if (!auftraege[i].getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT) && auftraege[i].getDLiefertermin().equals(ts)) {
					auftrag = auftraege[i];
					break;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		}
		return auftrag;
	}

	/**
	 * 
	 * @return	Kundenname
	 * 			"Unbekannte Ausgabestelle (IP: xxx.xxx.xxx.xxx)" wenn kein Kunde zugeordnet (=Arbeitsplatzparameter)
	 */
	@WebMethod
	@WebResult(name="sKunde")
	public String getKundeName() {
		try {
			return getPartner(getKunde(getIP()).getPartnerIId())
					.getCName1nachnamefirmazeile1();
		} catch (Exception e) {
			return "Unbekannte Ausgabestelle (IP:" + getIP() + ")";
		}
	}

	/**
	 * 
	 * @return	Kundenname
	 * 			"Unbekannte Ausgabestelle (IP: xxx.xxx.xxx.xxx)" wenn kein Kunde zugeordnet (=Arbeitsplatzparameter)
	 */
	@WebMethod
	@WebResult(name="sKunde")
	public String getKundeNameHvma(
			@WebParam(name = "idUser") String idUser) {

		TheClientDto theClientDto = check(idUser);
		
		try {
			return getPartner(getKunde(theClientDto).getPartnerIId())
					.getCName1nachnamefirmazeile1();
		} catch (Exception e) {
			return "Unbekannte Ausgabestelle (Benutzer:" + theClientDto.getBenutzername() + ")";
		}
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param csvPos
	 * @return	1 ... erfolgreich angelegt
	 * 			0 ... kein Lieferschein erstellt da alle Mengen 0
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * @throws RemoteException 
	 * @throws EJBExceptionLP 
	 */
	@WebMethod
	@WebResult(name="iResult")
	public int saveSpeiseplanAsLieferschein (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "sCsvPos") String csvPos) throws EJBExceptionLP, RemoteException {
		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		
		LieferscheinDto lsdto = createLieferscheinDto(kundeId, mandantCNr, theClientDto.getIDPersonal());
		Integer lsId = null;
		try {
			lsId = getLieferscheinFac().createLieferschein(lsdto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(1,e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		
		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Integer(pos[0]).intValue() != 0) {
				LieferscheinpositionDto lspos = createLieferscheinPositionDto(lsId, pos, false, null, theClientDto);
				try {
					getLieferscheinpositionFac().createLieferscheinposition(lspos,true, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode()== EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER)
						return -2;
					//throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		try {
			getLieferscheinFac().aktiviereLieferschein(lsId, theClientDto);
		} catch (EJBExceptionLP e) {
			if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
				return 0;
			}
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		return 1;
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param csvPos
	 * @return	1 ... erfolgreich angelegt
	 * 			0 ... kein Lieferschein erstellt da alle Mengen 0
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * @throws RemoteException 
	 * @throws EJBExceptionLP 
	 * 
	 * @implNote Am aktuellen Tag wird der Auftrag, wenn vorhanden, nicht aktualisiert und ein 
	 * 			Lieferschein mit Auftragsbezug erstellt. Ist kein Auftrag vorhanden wird nur
	 * 			ein Lieferschein erzeugt. Bei Buchungen in der Zukunft wird nur der Auftrag
	 * 			aktualisiert bzw. angelegt. Nicht mehr vorhandene Positionen werden geloescht.
	 */
	@WebMethod
	@WebResult(name="iResult")
	public int saveSpeiseplanAsAuftrag (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLiefertermin") String liefertermin ) throws EJBExceptionLP, RemoteException {
		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		Date dLiefertermin = Helper.parseString2Date(liefertermin, new SimpleDateFormat("dd.MM.yyyy"));
		
		Timestamp ts = Helper.cutTimestamp(new Timestamp(dLiefertermin.getTime()));
		AuftragDto aufDto = getAuftrag(kundeId, ts, theClientDto);
		
		LpListWrapper ll = new LpListWrapper(csvPos);

		boolean updateAuftrag = true;
		boolean createLieferschein = false;
		if (Helper.cutCalendar(Calendar.getInstance()).getTime().equals(dLiefertermin)) {
			updateAuftrag = false;
			createLieferschein = true;
		}
		
		Integer auftId = null;
		
		if (updateAuftrag) {
			if (aufDto == null) {
				// neuen Auftrag anlegen
				aufDto = createAuftragDto(kundeId, mandantCNr, theClientDto.getIDPersonal(), dLiefertermin);
			
				try {
					auftId = getAuftragFac().createAuftrag(aufDto, theClientDto);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(1,e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
				// neue Auftragspositionen erzeugen
				Iterator<?> it = ll.getList().iterator();
				while (it.hasNext()) {
					String[] newpos = (String[])it.next();
					if (new Integer(newpos[0]).intValue() != 0) {
						AuftragpositionDto aufpos = createAuftragPositionDto(auftId, newpos, false, null, dLiefertermin, theClientDto);
						try {
							getAuftragpositionFac().createAuftragposition(aufpos, true, theClientDto);
						} catch (EJBExceptionLP e) {
							if (e.getCode()== EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER)
								return -2;
							//throw new EJBExceptionLP(e);
						} catch (RemoteException e) {
							throw new EJBExceptionLP(e);
						}
					}
				}
				updateAuftrag = false;
			} else {
				// vorhanden Auftrag verwenden
				auftId = aufDto.getIId();
			}
		} else {
			if (aufDto != null) {
				auftId = aufDto.getIId();
			}
		}

		if (auftId != null) {
			Query query = em.createNamedQuery("AuftragpositionfindByAuftrag");
			query.setParameter(1, auftId);
			@SuppressWarnings("unchecked")
			List<Auftragposition> pos = query.getResultList();
			
			HashMap<Integer, Auftragposition> posmap = new HashMap<Integer, Auftragposition>();
			for (int i=0; i<pos.size(); i++) {
				posmap.put(pos.get(i).getIId(), pos.get(i));
			}
		
			if (updateAuftrag) {
				//nicht mehr gebrauchte Positionen loeschen und neue erzeugen wenn Auftrag in Zukunft
				ArrayList<Integer> pos2delete = new ArrayList<Integer>();
				for (int i=0; i<pos.size(); i++) {
					pos2delete.add(pos.get(i).getIId());
				}
			
				Iterator<?> it = ll.getList().iterator();
				while (it.hasNext()) {
					String[] newpos = (String[])it.next();
					if (new Integer(newpos[0]).intValue() != 0) {
						if (newpos.length>4 && !(newpos[4].length() == 0)) {
							Integer posId = Integer.parseInt(newpos[4]);
							pos2delete.remove(posId);
							AuftragpositionDto aufposDto = AuftragpositionDtoAssembler.createDto(posmap.get(posId));
							if (aufposDto != null) {
								if (aufposDto.getNMenge().compareTo(new BigDecimal(newpos[0])) != 0) {
									aufposDto.setNMenge(new BigDecimal(newpos[0]));
									getAuftragpositionFac().updateAuftragposition(aufposDto, theClientDto);
								}
								continue;
							}
						}
						AuftragpositionDto aufpos = createAuftragPositionDto(auftId, newpos, false, null, dLiefertermin, theClientDto);
						try {
							getAuftragpositionFac().createAuftragposition(aufpos, true, theClientDto);
						} catch (EJBExceptionLP e) {
							if (e.getCode()== EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER)
								return -2;
							//throw new EJBExceptionLP(e);
						} catch (RemoteException e) {
							throw new EJBExceptionLP(e);
						}
					}
				}
				
				if (pos2delete.size() > 0) {
					for(int i=0; i<pos2delete.size(); i++) {
						getAuftragpositionFac().removeAuftragposition(AuftragpositionDtoAssembler.createDto(posmap.get(pos2delete.get(i))), 
								theClientDto);
					}
				}
				try {
					getAuftragFac().aktiviereAuftrag(auftId, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
						return 0;
					}
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		
		if (createLieferschein) {
			// am aktuellen Tag einen auftragbezogenen Lieferschein erstellen
			// und Auftrag erledigen
			LieferscheinDto lsdto = createLieferscheinDto(kundeId, mandantCNr, theClientDto.getIDPersonal());
			if (auftId != null) {
				lsdto.setAuftragIId(auftId);
				lsdto.setLieferscheinartCNr(LieferscheinFac.LSART_AUFTRAG);
			}
			Integer lsId = null;
			try {
				lsId = getLieferscheinFac().createLieferschein(lsdto, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(1,e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			Iterator<?> it = ll.getList().iterator();
			while (it.hasNext()) {
				String[] newpos = (String[])it.next();
				if (new Integer(newpos[0]).intValue() != 0) {
					LieferscheinpositionDto lspos = createLieferscheinPositionDto(lsId, newpos, false, null, theClientDto);
					Integer posId = null;
					if (newpos.length>4 && newpos[4].length()>0) {
						//auftragsbezogen
						posId = Integer.parseInt(newpos[4]);
						lspos.setAuftragpositionIId(posId);
					}
					try {
						getLieferscheinpositionFac().createLieferscheinposition(lspos, true, theClientDto);
					} catch (EJBExceptionLP e) {
						if (e.getCode()== EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER)
							return -2;
						//throw new EJBExceptionLP(e);
					} catch (RemoteException e) {
						throw new EJBExceptionLP(e);
					}
					if (posId != null) {
						getAuftragpositionFac().updateOffeneMengeAuftragposition(posId, false, theClientDto);
					}
				}
			}
			try {
				getLieferscheinFac().aktiviereLieferschein(lsId, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
					return 0;
				}
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			if (auftId != null) {
				getAuftragFac().setzeAuftragstatusAufgrundAuftragpositionstati(auftId, theClientDto);
			}
		}
		
		return 1;
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @return CSV Liste der Artikellieferanten
	 */
	@WebMethod
	@WebResult(name="lieferantListe")
	public String getLieferantListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr) {
		
		TheClientDto theClientDto = check(idUser);
/*
		boolean isHvma = false;
		try 
		{
			List<HvmarechtDto> rechte = getHvmaFac().getHvmaRechte(theClientDto);
			if (rechte != null && rechte.size() > 0) {
				isHvma = true;
			}
		} catch (Exception ex) {
			//
			System.out.println(ex.getMessage());
		}
*/
		Integer partnerKlasseId = getPartnerKlasseId(getIP());
	
		// Erweiterung: Filter = Partnerklasse "Aktiv"
		boolean alleArtikel = false;
		if (theClientDto.getHvmaLizenzId() != null) { 
			alleArtikel = getHvmaFac().getAlleArtikelKueche(theClientDto);
		}

		if (alleArtikel) {
			Partnerklasse klasse = getPartnerklasse("Aktiv");
			if (klasse != null) {
				partnerKlasseId = klasse.getIId();
			}
		}

		
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = null;
		if (partnerKlasseId == null) {
			// Webshop und Essensausgabe
			sQuery = "SELECT DISTINCT l.flrlieferant.i_id, "
				+ "l.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 "
				+ "FROM FLRArtikellieferant l " 
				+ "JOIN l.flrlieferant "
				+ "WHERE l.flrlieferant.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.b_webshop = 1 "
				+ "ORDER BY l.flrlieferant.flrpartner.c_name1nachnamefirmazeile1";
		} else {
			// Waageterminal
			sQuery = "SELECT DISTINCT l.flrlieferant.i_id, "
				+ "l.flrlieferant.flrpartner.c_name1nachnamefirmazeile1 "
				+ "FROM FLRArtikellieferant l " 
				+ "JOIN l.flrlieferant "
				+ "WHERE l.flrlieferant.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.flrlieferant.flrpartner.partnerklasse_i_id = " + partnerKlasseId + " "
				+ "ORDER BY l.flrlieferant.flrpartner.c_name1nachnamefirmazeile1";
		}
		
		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}
	
	private Partnerklasse getPartnerklasse(String cNr) {
		Query query = em.createNamedQuery("PartnerklassefindByCNr");
		query.setParameter(1, cNr);
		query.setMaxResults(1);
		Partnerklasse klasse = (Partnerklasse) query.getSingleResult();
		return klasse;
	}
	
	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param lieferantId
	 * @param bLieferschein		true ... Lieferantenartikelliste in Artikeleinheit
	 * 							false .. Lieferantenartikelliste in Bestelleinheit und mit Verbrauch Vorwoche
	 * @param bLetzterWareneingang	true ... filtert die Artikel nach dem letzten Wareneingang der letzten Bestellung
	 * 
	 * @return CSV Liste der Positionen
	 */
	@WebMethod
	@WebResult(name="positionList")
	public String getArtikelListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "nLieferantId") Integer lieferantId,
			@WebParam(name = "bLieferschein") Boolean bLieferschein,
			@WebParam(name = "bLetzerWareneingang") Boolean bLetzterWareneingang){

		TheClientDto theClientDto = check(idUser);

		LpListWrapper resultList = getArtikelListeList(idUser, mandantCNr, lieferantId, bLieferschein,
				bLetzterWareneingang, theClientDto);
		resultList = sort(resultList, 2);
		return resultList.toCSV();
	}

	@WebMethod
	@WebResult(name="positionList")
	public String getArtikelListeMitMenge(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "nLieferantId") Integer lieferantId,
			@WebParam(name = "bLieferschein") Boolean bLieferschein,
			@WebParam(name = "bLetzerWareneingang") Boolean bLetzterWareneingang,
			@WebParam(name = "nBestellungId") Integer bestellungIId){

		TheClientDto theClientDto = check(idUser);

		LpListWrapper resultList = getArtikelListeList(idUser, mandantCNr, lieferantId, bLieferschein,
				bLetzterWareneingang, theClientDto);
		
		try {
			BestellpositionDto[] bespos = getBestellpositionFac().bestellpositionFindByBestellung(bestellungIId, theClientDto);
			for (int i = 0; i < bespos.length; i++) {
				if (bespos[i].getNMenge().signum() != 0) {
					SetMenge(resultList, bespos[i].getArtikelIId(), bespos[i].getNMenge());
				}
			}
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		resultList = sort(resultList, 2);
		return resultList.toCSV();
	}
	
	private void SetMenge(LpListWrapper resultList, Integer artikelIId, BigDecimal menge) {
		for (int i = 0; i < resultList.getList().size(); i++) {
			Object[] o = (Object[])resultList.getList().get(i);
			if (o[3].equals(artikelIId)) {
				o[0] = menge;
				break;
			}
		}
	}
	
	private LpListWrapper getArtikelListeList(String idUser, String mandantCNr, Integer lieferantId,
			Boolean bLieferschein, Boolean bLetzterWareneingang, TheClientDto theClientDto) {

		//Kunde kunde = getKunde(getIP());
		boolean alleArtikel = false;
		if (theClientDto.getHvmaLizenzId() != null) { 
			alleArtikel = getHvmaFac().getAlleArtikelKueche(theClientDto);
		}
		
		Calendar cNow = Calendar.getInstance();
		cNow.setTime(Helper.cutDate(new java.util.Date()));
		
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery;
		if (bLieferschein) {
			// Liste in Artikeleinheit
			sQuery = "SELECT 0.0, "
				+ "l.flrartikel.c_nr, "
				+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=l.artikel_i_id AND spr.Id.locale='deAT'), "
				+ "l.artikel_i_id, "
				+ "l.flrartikel.einheit_c_nr, "
				+ "1.0000, "
				+ "l.c_artikelnrlieferant, "
				+ "0.0, "
				+ "0 "
				+ "FROM FLRArtikellieferant l "  
				+ "JOIN l.flrartikel "
				+ "WHERE l.flrartikel.artikelart_c_nr='Artikel        ' "
				+ "AND l.flrartikel.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.lieferant_i_id=" + lieferantId + " "
				+ "AND l.flrartikel.b_versteckt=0 "
				+ (alleArtikel ? "" : "AND l.b_webshop=" + Helper.boolean2Short(true) + " ")
				+ "AND (l.t_preisgueltigbis>='" + Helper.formatDateWithSlashes(new java.sql.Date(cNow.getTimeInMillis())) + "' "
				+ "OR l.t_preisgueltigbis is null) "
				+ "ORDER BY l.flrartikel.c_nr";
		} else {
			// Liste in Bestelleinheit mit Verbrauch Vorwoche
			sQuery = "SELECT 0.0, "
				+ "l.flrartikel.c_nr, "
				+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=l.artikel_i_id AND spr.Id.locale='deAT'), "
				+ "l.artikel_i_id, "
				+ "COALESCE(l.flrartikel.einheit_c_nr_bestellung, l.flrartikel.einheit_c_nr), "
				+ "l.flrartikel.n_umrechnungsfaktor, "
				+ "l.c_artikelnrlieferant, "
				+ "0.0, "
				+ "l.flrartikel.b_bestellmengeneinheitinvers "
				+ "FROM FLRArtikellieferant l "  
				+ "JOIN l.flrartikel "
				+ "WHERE l.flrartikel.artikelart_c_nr='Artikel        ' "
				+ "AND l.flrartikel.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.lieferant_i_id=" + lieferantId + " "
				+ "AND l.flrartikel.b_versteckt=0 "
				+ (alleArtikel ? "" : "AND l.b_webshop=" + Helper.boolean2Short(true) + " ")
				+ "AND (l.t_preisgueltigbis>='" + Helper.formatDateWithSlashes(new java.sql.Date(cNow.getTimeInMillis())) + "' "
				+ "OR l.t_preisgueltigbis is null) "
				+ "ORDER BY l.flrartikel.c_nr";
		}
		
		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		if (bLetzterWareneingang) {
			// Daten filtern nach Wareneingangspositionen und Menge setzen
			Timestamp tsBis = new Timestamp(System.currentTimeMillis());
			tsBis = Helper.cutTimestamp(tsBis);
			//PJ 17878
			//tsBis = Helper.addiereTageZuTimestamp(tsBis, -1);
			Timestamp tsVon = Helper.addiereTageZuTimestamp(tsBis, -90);
			Integer kostenstelleIId = getKostenstelle(theClientDto.getIDPersonal());
			Integer bestellungIId = findBestellungZuDatumVonBis(lieferantId, mandantCNr, kostenstelleIId, tsVon, tsBis);
			if (bestellungIId != null) {
				Integer wareneingangIId = findLetztenWareneingang(bestellungIId);
				if (wareneingangIId != null) {
					sQuery = "SELECT p.n_geliefertemenge, "
						+ "p.flrbestellposition.flrartikel.i_id "
						+ "FROM FLRWareneingangspositionen p "
						+ "WHERE p.wareneingang_i_id=" + wareneingangIId;
					query = session.createQuery(sQuery);
					List<?> weposList = query.list();
					Iterator<?> it = weposList.iterator();
					HashMap<Integer, BigDecimal> hm = new HashMap<Integer, BigDecimal>();
					while (it.hasNext()) {
						Object[] o = (Object[])it.next();
						hm.put((Integer)o[1], (BigDecimal)o[0]);
					}
					ArrayList<Object[]> gefiltert = new ArrayList<Object[]>();
					for (int i=0; i<resultList.getList().size(); i++) {
						Object[] o = (Object[]) resultList.getList().get(i);
						BigDecimal n = (BigDecimal)hm.get(o[3]);
						if (n != null) {
							BigDecimal faktor = (BigDecimal) o[5];
							if (faktor != null)
								if ((Short)o[8] == 0)
									o[0] = Helper.rundeKaufmaennisch(n.multiply(faktor), 3);
								else
									o[0] = Helper.rundeKaufmaennisch(n.multiply(faktor), 3);
							else
								o[0] = n;
							gefiltert.add(o);
						}
					}
					resultList = new LpListWrapper(gefiltert);
				}
			}
		}
		//PJ 18572 Vorwoche entfaellt
		/*
		if (!bLieferschein) {
			// Daten um Verbrauch Vorwoche ergaenzen
			Date[] aDate = getLastWeek();
			Object[][] lstat = getKundeReportFac().getDataLieferstatistik(theClientDto, kunde.getIId(),null,null,  aDate[0], aDate[1], 
					Helper.SORTIERUNG_NACH_IDENT, mandantCNr, false, true,false,KundeReportFac.REPORT_LIEFERSTATISTIK_OPTION_LIEFERADRESSE, false);
		
			HashMap<String, Object> hm = new HashMap<String, Object>();
			for (int i=0; i<lstat.length; i++) {
				hm.put((String)lstat[i][KundeReportFac.REPORT_STATISTIK_IDENT], lstat[i]);
			}
			Iterator<?> it = resultList.getList().iterator();
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();
				Object[] v = (Object[]) hm.get((String)o[1]);
				BigDecimal nVerbrauch = new BigDecimal(0);
				if (v != null) { 
					nVerbrauch = (BigDecimal)(v[KundeReportFac.REPORT_STATISTIK_MENGE]);
				}
				o[7] = nVerbrauch;
			}
		}
		*/
		return resultList;
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param bestellungIId
	 * @return	0 ... erfolgreich storniert
	 * 			-1 .. Status falsch
	 * 			-2 .. fehlgeschlagen
	 * 			>0 .. Fehlercode EJBExceptionLP
	 */
	@WebMethod
	@WebResult(name="result")
	public int storniereBestellung(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "nBestellungId") Integer bestellungIId){
		
		TheClientDto theClientDto = check(idUser);

		Bestellung bestellung = em.find(Bestellung.class, bestellungIId);
		if (bestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || bestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_OFFEN)) {
			try {
				getBestellungFac().stornieren(bestellungIId, theClientDto);
				return 0;
			} catch (EJBExceptionLP e) {
				return e.getCode();
			} catch (RemoteException e) {
				return -2;
			}
		} else {
			return -1;
		}
	}
	
	@WebMethod
	@WebResult(name="result")
	public int saveWareneingangDokument( 
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "nLieferantId") Integer lieferantId,
			@WebParam(name = "nBestellungId") Integer bestellungIId,
			@WebParam(name = "nWareneingangId") Integer wareneingangIId,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "base64Foto") String base64Foto){

		TheClientDto theClientDto = check(idUser);

		String filename = "WaWi-SK_Foto.jpg";
		WareneingangDto wareneingangDto;
		try {
			wareneingangDto = getWareneingangFac().wareneingangFindByPrimaryKey(wareneingangIId);
		} catch (EJBExceptionLP | RemoteException e) {
			e.printStackTrace();
			return -1;
		}

		BestellungDto bestellungDto;
		if (bestellungIId == 0) {
			bestellungIId = wareneingangDto.getBestellungIId(); 
		}
		try {
			bestellungDto = getBestellungFac().bestellungFindByPrimaryKey(bestellungIId);
		} catch (EJBExceptionLP | RemoteException e) {
			e.printStackTrace();
			return -2;
		}
		
		JCRDocDto docDto = new JCRDocDto();
		docDto.setDocPath(getDocPath(wareneingangDto, bestellungDto));
		docDto.getDocPath().add(new DocNodeFile(filename));
		docDto.setsBelegnummer(wareneingangDto.getIId().toString());
		docDto.setsTable("WARENEINGANG");
		docDto.setsRow(wareneingangDto.getIId().toString());
		docDto.setsBelegart(JCRDocFac.DEFAULT_KOPIE_BELEGART);
		docDto.setlSicherheitsstufe(JCRDocFac.SECURITY_LOW);
		docDto.setsSchlagworte(lieferscheinCNr);
		docDto.setsMIME(Helper.getMime(filename));
		docDto.setsFilename(filename);
		docDto.setbData(Base64.decodeBase64(base64Foto));
		docDto.setbVersteckt(false);
		if (lieferantId != 0) {
			docDto.setlPartner(lieferantId); 
		}
		docDto.setlAnleger(theClientDto.getIDPersonal());
		docDto.setlZeitpunkt(System.currentTimeMillis());
		docDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
		docDto.setsName(Helper.getName(filename));
		
		try {
			getJCRDocFac().addNewDocumentOrNewVersionOfDocumentWithinTransaction(docDto, theClientDto);
		} catch (Exception ex) {
			ex.printStackTrace();
			return -3;
		}
		return 0;
	}
	
	private DocPath getDocPath(WareneingangDto wareneingangDto, BestellungDto bestellungDto) {
		return new DocPath(new DocNodeWareneingang(wareneingangDto, bestellungDto));
	}

	@SuppressWarnings("unchecked")
	private LpListWrapper sort(LpListWrapper list, int iCol) {
		final int col = iCol;
		ArrayList al = (ArrayList) list.getList();
		Collections.sort((List<Object[]>) al, new Comparator(){
			public int compare(Object o1, Object o2) {
	           Object[] oa1 = (Object[]) o1;
	           Object[] oa2 = (Object[]) o2;
	           String s1 = (oa1[col] == null ? "" : (String) oa1[col]);
	           String s2 = (oa2[col] == null ? "" : (String) oa2[col]);
	           return s1.compareToIgnoreCase(s2);
	           }
			});
		return new LpListWrapper(al);
	}
	
	protected static Comparator<Object[]> newAscComparator(int iCol) {
	    final int col = iCol;
		return new Comparator<Object[]>() {
	
		@Override
		public int compare(Object[] first, Object[] second) {
			return ((String)first[col]).compareTo((String)second[col]);
			}
		};
	}



	private Date[] getLastWeek() {
		Date[] ats = new Date[2];
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, 1 - c.get(Calendar.DAY_OF_WEEK));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND,999);
		ats[1] = new Date(c.getTimeInMillis());
		c.add(Calendar.DAY_OF_YEAR, -7);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		ats[0] = new Date(c.getTimeInMillis());
		
		return ats;
	}

	@WebMethod
	@WebResult(name="iResult")
	public int saveBestellung (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "bDirekt") Boolean bDirekt) {
	
		int result = saveBestellungEx(idUser, mandantCNr, lieferantId, csvPos, lieferscheinCNr, bDirekt);
		if (result <= 0) {
			return result;
		} else {
			return 1;
		}
	}
	
	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param lieferantId
	 * @param csvPos
	 * @param lieferscheinCNr
	 * @param bDirekt	true ... Bestellung samt Wareneingang wird erstellt
	 * 					false .. Bestellung mit Status Angelegt wird erstellt
	 * @return	>0 ... WareneingangId
	 * 			0 ... keine Bestellung erstellt da alle Mengen 0
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * 			-2 .. keine Lieferscheinnummer bei Direktbestellung angegeben
	 */
	@WebMethod
	@WebResult(name="iResult")
	public int saveBestellungEx (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "bDirekt") Boolean bDirekt) {

		TheClientDto theClientDto = check(idUser);

		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		boolean hatMengen = false;
		
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				hatMengen = true;
				break;
			}
		}
		if (!hatMengen)
			return 0;
		
		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		if (bDirekt && (lieferscheinCNr.length() == 0)) 
			return -2;
		
		Kunde kunde = em.find(Kunde.class, kundeId);
		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer kostenstelleIId = getKostenstelle(theClientDto.getIDPersonal());
		String cBez = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			cBez = getHvmaFac().getProjektWebKueche(theClientDto);
		} else {
			cBez = getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTWEB);
		}
		BestellungDto bsdto = createBestellungDto(kunde, lieferant, mandantCNr, theClientDto.getIDPersonal(), kostenstelleIId, cBez);
		if (!bDirekt) {
			Integer dauer = getIntegerParameter(mandantCNr, ParameterFac.KATEGORIE_BESTELLUNG, 
					ParameterFac.PARAMETER_OFFSET_LIEFERZEIT_IN_TAGEN);
			if ((dauer != null) && (dauer > 0)) {
				bsdto.setDLiefertermin(new Timestamp(
						Helper.addiereTageZuDatum(bsdto.getDLiefertermin(), dauer.intValue()).getTime()));
			}
		}
		
		Integer bsId = null;
		try {
			bsId = getBestellungFac().createBestellung(bsdto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				try {
					BestellpositionDto bspos = createBestellPositionDto(bsId, lieferantId, pos, theClientDto);
					getBestellpositionFac().createBestellposition(bspos, theClientDto, null, null);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		if (!bDirekt) {
			return 1;
		} else {

			try {
				getBestellungFac().aktiviereBestellung(bsId, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
					return 0;
				}
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			WareneingangDto wedto = new WareneingangDto();
			wedto.setBestellungIId(bsId);
			wedto.setCLieferscheinnr(lieferscheinCNr);
			wedto.setTLieferscheindatum(getTimestamp());
			wedto.setTWareneingangsdatum(getTimestamp());
			wedto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
			wedto.setNWechselkurs(new BigDecimal(1));
			Integer weId = null;
			try {
				weId = getWareneingangFac().createWareneingang(wedto, theClientDto);
				getWareneingangFac().uebernimmAlleWepsOhneBenutzerinteraktion(weId, bsId,null, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			return weId;
		}
	}

	@WebMethod
	@WebResult(name="iResult")
	public int saveBestellungTermin (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "bDirekt") Boolean bDirekt,
			@WebParam(name = "sLiefertermin") String liefertermin,
			@WebParam(name = "sInternerKommentar") String internerKommentar) {
		int result = saveBestellungTerminEx(idUser, mandantCNr, lieferantId, csvPos, lieferscheinCNr, bDirekt, liefertermin, internerKommentar);
		if (result <= 0) {
			return result;
		} else {
			return 1;
		}
	}
	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param lieferantId
	 * @param csvPos
	 * @param lieferscheinCNr
	 * @param bDirekt	true ... Bestellung samt Wareneingang wird erstellt
	 * 					false .. Bestellung mit Status Angelegt wird erstellt
	 * @param liefertermin
	 * @param internerKommentar
	 * 
	 * @return	>0 ... WareneingangIId bei Direktbestellung, sonst	1 ... erfolgreich angelegt 
	 * 			0 ... keine Bestellung erstellt da alle Mengen 0
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * 			-2 .. keine Lieferscheinnummer bei Direktbestellung angegeben
	 */
	@WebMethod
	@WebResult(name="nWareneingangId")
	public int saveBestellungTerminEx (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "bDirekt") Boolean bDirekt,
			@WebParam(name = "sLiefertermin") String liefertermin,
			@WebParam(name = "sInternerKommentar") String internerKommentar) {

		TheClientDto theClientDto = check(idUser);

		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		boolean hatMengen = false;
		
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				hatMengen = true;
				break;
			}
		}
		if (!hatMengen)
			return 0;
		
		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		if (bDirekt && (lieferscheinCNr.length() == 0)) 
			return -2;
		
		Kunde kunde = em.find(Kunde.class, kundeId);
		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer kostenstelleIId = getKostenstelle(theClientDto.getIDPersonal());
		String cBez = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			cBez = getHvmaFac().getProjektWebKueche(theClientDto);
		} else {
			cBez = getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTWEB);
		}
		BestellungDto bsdto = createBestellungDto(kunde, lieferant, mandantCNr, theClientDto.getIDPersonal(), kostenstelleIId, cBez);

		if (liefertermin.length() > 0) {
			try {
				java.util.Date dLiefertermin = new SimpleDateFormat("dd.MM.yyyy").parse(liefertermin);
				bsdto.setDLiefertermin(new Timestamp(dLiefertermin.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (internerKommentar.length() > 0) {
			bsdto.setXInternerKommentar(internerKommentar);	
		}
		
		if (!bDirekt) {
			Integer dauer = getIntegerParameter(mandantCNr, ParameterFac.KATEGORIE_BESTELLUNG, 
					ParameterFac.PARAMETER_OFFSET_LIEFERZEIT_IN_TAGEN);
			if ((dauer != null) && (dauer > 0)) {
				bsdto.setDLiefertermin(new Timestamp(
						Helper.addiereTageZuDatum(bsdto.getDLiefertermin(), dauer.intValue()).getTime()));
			}
		}
		
		Integer bsId = null;
		try {
			bsId = getBestellungFac().createBestellung(bsdto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				try {
					BestellpositionDto bspos = createBestellPositionDto(bsId, lieferantId, pos, theClientDto);
					getBestellpositionFac().createBestellposition(bspos, theClientDto, null, null);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		if (!bDirekt) {
			return 1;
		} else {

			try {
				getBestellungFac().aktiviereBestellung(bsId, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
					return 0;
				}
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			WareneingangDto wedto = new WareneingangDto();
			wedto.setBestellungIId(bsId);
			wedto.setCLieferscheinnr(lieferscheinCNr);
			wedto.setTLieferscheindatum(getTimestamp());
			wedto.setTWareneingangsdatum(getTimestamp());
			wedto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
			wedto.setNWechselkurs(new BigDecimal(1));
			Integer weId = null;
			try {
				weId = getWareneingangFac().createWareneingang(wedto, theClientDto);
				getWareneingangFac().uebernimmAlleWepsOhneBenutzerinteraktion(weId, bsId,null, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			return weId;
		}
	}

	private Integer getKostenstelle(Integer personalIId) {
		Personal personal = em.find(Personal.class, personalIId);
		return personal.getKostenstelleIIdStamm();
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param lieferantId
	 * @param csvPos
	 * 
	 * @return	0 ... erfolgreich angelegt
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * 			-2 .. kein Kunde aus Lieferant m&ouml;glich
	 * 			-3 .. kein Lieferschein erstellt da alle Mengen 0
	 *			1 .. n	ArtikelIid mit zu wenig auf Lager
	 */
	@WebMethod
	@WebResult(name="iResult")
	public int saveLieferantLieferschein (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId,
			@WebParam(name = "sCsvPos") String csvPos) {

		TheClientDto theClientDto = check(idUser);

		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		boolean hatMengen = false;
		
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				hatMengen = true;
				break;
			}
		}
		if (!hatMengen)
			return -3;
		
		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		
		Kunde shop = em.find(Kunde.class, kundeId);
//		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer lieferantAlsKundeIId;
		try {
			lieferantAlsKundeIId = getKundeFac().createVerstecktenKundenAusLieferant(lieferantId, theClientDto);
		} catch (EJBExceptionLP e1) {
			return -2;
		} catch (RemoteException e1) {
			return -2;
		}
//		Kunde lieferantAlsKunde = em.find(Kunde.class, lieferantAlsKundeIId);
		
		LieferscheinDto lsdto = createLieferscheinDto(lieferantAlsKundeIId, mandantCNr, theClientDto.getIDPersonal());
		lsdto.setLieferscheinartCNr(LieferscheinFac.LSART_LIEFERANT);
		lsdto.setLagerIId(shop.getLagerIIdAbbuchungslager());
		String sProjekt = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			sProjekt = getHvmaFac().getProjektShopKueche(theClientDto);
		} else {
			sProjekt = getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTSHOP);
		}

		if (sProjekt != null)
			lsdto.setCBezProjektbezeichnung(sProjekt);
		
		Integer lsId = null;
		try {
			lsId = getLieferscheinFac().createLieferschein(lsdto, theClientDto);
		} catch (EJBExceptionLP e) {
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}

		it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_BESPOS_MENGE]).doubleValue() != 0) {
				try {
					LieferscheinpositionDto lspos = createLieferscheinPositionDto(lsId, pos, true, lieferantId, theClientDto);
					getLieferscheinpositionFac().createLieferscheinposition(lspos, false, theClientDto);
				} catch (EJBExceptionLP e) {
					if (e.getCode()==EJBExceptionLP.FEHLER_ZUWENIG_AUF_LAGER)
						return new Integer(pos[_BESPOS_ARTIKELID]);
					else
						throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		try {
			getLieferscheinFac().aktiviereLieferschein(lsId, theClientDto);
		} catch (EJBExceptionLP e) {
			if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
				return -3;
			}
			throw new EJBExceptionLP(e);
		} catch (RemoteException e) {
			throw new EJBExceptionLP(e);
		}
		return 0;
	}

	@WebMethod
	@WebResult(name="artikelgruppenList")
	public String getArtikelgruppenListe(
			@WebParam(name = "idUser") String idUser) {
		check(idUser);

		Integer agruId = getVaterArtikelgruppe(getIP());
		
		if (agruId == null) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL, new Exception("Parameter f\u00FCr Vaterartikelgruppe nicht vorhanden"));
		}
		
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT g.i_id, g.c_nr "
			+ "FROM FLRArtikelgruppe g "  
			+ "WHERE g.flrartikelgruppe.i_id=" + agruId + " "
			+ "ORDER BY g.c_nr";

		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}

	@WebMethod
	@WebResult(name="artikelList")
	public String getGruppenArtikelListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr, 
			@WebParam(name = "iGruppeId") Integer gruppeId) {
		check(idUser);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT a.c_nr, "
			+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=a.i_id AND spr.Id.locale='deAT'), "
			+ "a.i_id "
			+ "FROM FLRArtikelliste a "
			+ "WHERE a.mandant_c_nr='" + mandantCNr + "' "
			+ "AND a.flrartikelgruppe.i_id=" + gruppeId + " "
			+ "ORDER BY a.c_nr";

		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}

	@WebMethod
	@WebResult(name="iBestellungId")
	public int getTagesBestellung (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantId) {
		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		Kunde kunde = em.find(Kunde.class, kundeId);
		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer kostenstelleId = kunde.getKostenstelleIId();
		Integer bsId = findTagesBestellung(lieferantId, mandantCNr, kostenstelleId);
		if (bsId == null) {
			String cBez = null;
			if (theClientDto.getHvmaLizenzId() != null) {
				cBez = getHvmaFac().getProjektWebKueche(theClientDto);
			} else {
				cBez = getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTWEB);
			}
			BestellungDto bsdto = createBestellungDto(kunde, lieferant, mandantCNr, theClientDto.getIDPersonal(),kostenstelleId, cBez);
			try {
				bsId = getBestellungFac().createBestellung(bsdto, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
		}
		return bsId;
	}

	@WebMethod
	@WebResult(name="iResult")
	public int savePositionTagesbestellung (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "bDirekt") Boolean bDirekt,
			@WebParam(name = "bIsEAN") Boolean bIsEAN) {
		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}
		if (kundeId == null)
			return -1;
		Kunde kunde = em.find(Kunde.class, kundeId);
		if (bDirekt && (lieferscheinCNr.length() == 0)) 
			// TODO: Lieferscheinnummer fuer Fleischerei 
			lieferscheinCNr = "Fleischerei";
		Bestellung bestellung = em.find(Bestellung.class, bestellungId);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		// nur auf Tagesbestellung buchen!
		c1.setTimeInMillis(bestellung.getTBelegdatum().getTime());
		c2.setTimeInMillis(System.currentTimeMillis());
		if (c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_ANLEGEN,"Datum der Bestellung ist nicht aktuell. Bitte Lieferant neu ausw\u00E4hlen");
		}
		
		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[0]).doubleValue() != 0) {
				try {
					if (bIsEAN) {
						EinkaufseanDto ekEanDto = getArtikelFac().einkaufseanFindByCEan(pos[2]);
						if (ekEanDto == null) {
							return -3;
						} else {
							pos[4] = ekEanDto.getArtikelIId().toString();
							pos[0] = ekEanDto.getNMenge().toString();
						}
					}
					BestellpositionDto bspos = createBestellPositionDto(bestellungId, bestellung.getLieferantIIdBestelladresse(), pos, theClientDto);
					getBestellpositionFac().createBestellposition(bspos, theClientDto, null,null);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}
		}
		if (!bDirekt) {
			return 1;
		} else {
			try {
				getBestellungFac().aktiviereBestellung(bestellungId, theClientDto);
			} catch (EJBExceptionLP e) {
				if (e.getCode() == EJBExceptionLP.FEHLER_BELEG_HAT_KEINE_POSITIONEN) {
					return 0;
				}
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			
			Integer weId = null;
			WareneingangDto[] wdtos = null;
			try {
				wdtos = getWareneingangFac().wareneingangFindByBestellungIId(bestellungId);
				if (wdtos.length==0) {
					WareneingangDto wedto = new WareneingangDto();
					wedto.setBestellungIId(bestellungId);
					wedto.setCLieferscheinnr(lieferscheinCNr);
					wedto.setTLieferscheindatum(getTimestamp());
					wedto.setTWareneingangsdatum(getTimestamp());
					wedto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
					wedto.setNWechselkurs(new BigDecimal(1));
					try {
						weId = getWareneingangFac().createWareneingang(wedto, theClientDto);
					} catch (EJBExceptionLP e) {
						throw new EJBExceptionLP(e);
					} catch (RemoteException e) {
						throw new EJBExceptionLP(e);
					}
				} else {
					weId = wdtos[0].getIId();
				}
			} catch (EJBExceptionLP e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			try {
				getWareneingangFac().uebernimmAlleWepsOhneBenutzerinteraktion(weId, bestellung.getIId(),null, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			return 1;
		}
	}

	@WebMethod
	@WebResult(name="iResult")
	public int getMenuRechte (
			@WebParam(name = "idUser") String idUser) {
		TheClientDto theClientDto = check(idUser);

		Integer iResult = 0;
		if (theClientDto.getHvmaLizenzId() != null) {
			iResult = getHvmaFac().getMenurechteKueche(theClientDto);
			boolean lieferterminKommentarEingabe = getHvmaFac().getLieferterminKommentarEingabe(theClientDto);
			if (lieferterminKommentarEingabe)
				iResult = iResult + 0x1000;
		} else {
			iResult = getRechte(getIP());
		}
		System.out.println("Menu-Rechte: " + iResult);
		return iResult;
	}
	
	@WebMethod
	@WebResult(name="bestellungListe")
	public String getBestellungOffenListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr) {
		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}
		if (kundeId == null)
			return "KEIN_KUNDE";
		Kunde kunde = em.find(Kunde.class, kundeId);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT b.i_id, "
			+ "b.c_nr, "
			+ "b.t_belegdatum, "
			+ "(SELECT l.flrpartner.c_name1nachnamefirmazeile1 FROM FLRLieferant l WHERE l.i_id = b.lieferant_i_id_bestelladresse), "
			+ "b.bestellungstatus_c_nr, "
			+ "b.t_liefertermin "
			+ "FROM FLRBestellung b "
			+ "JOIN b.flrpartnerlieferadresse "
			+ "WHERE b.mandant_c_nr ='" + mandantCNr + "' "
			+ "AND b.flrpartnerlieferadresse.i_id =" + kunde.getPartnerIId() + " "
			+ "AND (b.bestellungstatus_c_nr ='" + BestellungFac.BESTELLSTATUS_OFFEN + "' OR "
			+ "b.bestellungstatus_c_nr = '" + BestellungFac.BESTELLSTATUS_TEILERLEDIGT + "' OR " 
			+ "b.bestellungstatus_c_nr = '" + BestellungFac.BESTELLSTATUS_ANGELEGT + "') "
			+ "ORDER BY b.t_belegdatum";

		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}

	@WebMethod
	@WebResult(name="bVorhanden")
	public int hasBestellung(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iLieferantId") Integer lieferantIId,
			@WebParam(name = "sLiefertermin") String liefertermin) {

		TheClientDto theClientDto = check(idUser);
		
		Date dt = Helper.parseString2Date(liefertermin, new SimpleDateFormat("dd.MM.yyyy"));
		Timestamp tsVon = new Timestamp(dt.getTime());
		Integer kostenstelleIId = getKostenstelle(theClientDto.getIDPersonal());
		Integer besIId = findBestellungZuDatumVonBis(lieferantIId, mandantCNr, kostenstelleIId, tsVon, tsVon);
		if (besIId == null) {
			return 0;
		} else {
			Bestellung bestellung = em.find(Bestellung.class, besIId);
			if (bestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_ANGELEGT) || 
					bestellung.getBestellungstatusCNr().equals(BestellungFac.BESTELLSTATUS_OFFEN)) {
				return besIId;
			} else {
				return 0;
			}
		}
	}

	@WebMethod
	@WebResult(name="bestellposListe")
	public String getBestellPosOffenListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "iBestellungId") Integer bestellungId) {
		TheClientDto theClientDto = check(idUser);

		Session session = FLRSessionFactory.getFactory().openSession();

		Integer mengenArt = getIntegerParameter(theClientDto.getMandant(), ParameterFac.KATEGORIE_KUECHE,
				ParameterFac.PARAMETER_WARENEINGANG_IN_BESTELLEINHEIT);
		if (mengenArt == null)
			mengenArt = new Integer(0);
		
		String sQuery = null;
		if (mengenArt.intValue() == 1) {
			// hier wird auf bestelleinheit umgerechnet 
			sQuery = "SELECT 0.0, b.flrartikel.c_nr, "
				+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=b.flrartikel.i_id AND spr.Id.locale='deAT'), "
				+ "b.i_id, "
				+ "COALESCE(b.flrartikel.einheit_c_nr_bestellung, b.flrartikel.einheit_c_nr), "
				+ "b.flrartikel.n_umrechnungsfaktor, "
				+ "'', "
				+ "COALESCE(b.n_offenemenge,b.n_menge), "
				+ "b.flrartikel.b_bestellmengeneinheitinvers "
				+ "FROM FLRBestellposition b "  
				+ "JOIN b.flrartikel "
				+ "WHERE b.flrbestellung.i_id =" + bestellungId + " "
				+ "AND (b.n_offenemenge > 0 OR b.n_offenemenge is null) "
				+ "AND (b.position_i_id_artikelset is null) "
				+ "ORDER BY b.i_sort";
		} else {		
			// Wareneingang in Artikeleinheit
			sQuery = "SELECT 0.0, b.flrartikel.c_nr, "
				+ "(SELECT spr.c_bez FROM FLRArtikellistespr spr WHERE spr.Id.artikelliste=b.flrartikel.i_id AND spr.Id.locale='deAT'), "
				+ "b.i_id, "
				+ "b.flrartikel.einheit_c_nr, "
				+ "1.0000 as n_umrechnungsfaktor, "
				+ "'', "
				+ "COALESCE(b.n_offenemenge,b.n_menge) "
				+ "FROM FLRBestellposition b "  
				+ "JOIN b.flrartikel "
				+ "WHERE b.flrbestellung.i_id =" + bestellungId + " "
				+ "AND (b.n_offenemenge > 0 OR b.n_offenemenge is null) "
				+ "AND (b.position_i_id_artikelset is null) "
				+ "ORDER BY b.i_sort";
		}
		org.hibernate.Query query = session.createQuery(sQuery);
		LpListWrapper resultList = new LpListWrapper(query.list());
		return resultList.toCSV();
	}

/*	private String getMandantParameter(String sKategorie, String sParameter, String sMandant, String sDefault) {
		ParametermandantDto pmdto;
		String cWert  = sDefault;
		try {
			pmdto = getParameterFac().parametermandantFindByPrimaryKey (
					sParameter, 
					sKategorie, 
					sMandant);
			cWert = pmdto.getCWert();
		} catch (EJBExceptionLP e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return cWert;
	}
*/
	@WebMethod
	@WebResult(name="csvResult")
	public String getWareneingangInfos (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvArtikel") String csvArtikel) {

		TheClientDto theClientDto = check(idUser);
		String ret = "";
		
		if (csvArtikel != null) {
			String[] artikelNrs = csvArtikel.split("\n");
			if (artikelNrs.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < artikelNrs.length; i++) {
					Artikel artikel = getArtikelByCnr(artikelNrs[i]);
					ArtikelsprDto artikelsprDto = null;
					try {
						artikelsprDto = getArtikelFac().artikelsprFindByArtikelIIdLocaleCNrOhneExc(artikel.getIId(), theClientDto.getLocUiAsString(), theClientDto);
					} catch (EJBExceptionLP | RemoteException e1) {
						e1.printStackTrace();
					}
					if (artikel != null) {
						ArtikelkommentarDto[] ak;
						try {
							ak = getArtikelkommentarFac().artikelkommentardruckFindByArtikelIIdBelegartCNr(artikel.getIId(), LocaleFac.BELEGART_WARENEINGANG, theClientDto.getLocMandantAsString(), theClientDto);
							if (ak != null && ak.length > 0) {
								for (int j = 0; j < ak.length; j++) {
									sb.append(artikel.getIId());
									sb.append(";");
									sb.append(artikel.getCNr());
									sb.append(";");
									sb.append(ak[j].getArtikelkommentarsprDto().getXKommentar().replace("\n", "\\n").replace(";", "||"));
									sb.append(";");
									if (artikelsprDto != null)
										sb.append(artikelsprDto.getCBez());
									sb.append(";");
									if (artikelsprDto != null)
										sb.append(artikelsprDto.getCKbez());
									sb.append("\n");
								}
							}
						} catch (EJBExceptionLP | RemoteException e) {
							e.printStackTrace();
						}
					}
				}
				ret = sb.toString();
			}
		}
		return ret;
	}

	@WebMethod
	@WebResult(name="iResult")
	public int saveWareneingangInfos (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "sBestellinfo") String bestellinfo,
			@WebParam(name = "sCsvInfos") String csvInfos) {

		TheClientDto theClientDto = check(idUser);
		
		if (csvInfos != null && csvInfos.length() > 0) {
			String[] infos = csvInfos.split("\n");
			if (infos.length > 0) {
				for (int i = 0; i < infos.length; i++) {
					ProtokollDto protokollDto = new ProtokollDto();
					protokollDto.setCArt(SystemFac.PROTOKOLL_ART_INFO);
					protokollDto.setCTyp(SystemFac.PROTOKOLL_TYP_BES);
					protokollDto.setCText(bestellinfo);
					protokollDto.setCLangtext(infos[i].replace("\\n", "\n").replace("||", ";"));
					getSystemFac().erstelleProtokolleintrag(protokollDto, theClientDto);
				}
			}
		}
		return 1;
	}
	
	@WebMethod
	@WebResult(name="iResult")
	public int saveWareneingang (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr) {
		return saveWareneingangLager(idUser, mandantCNr, bestellungId,csvPos,lieferscheinCNr,null);
	}

	@WebMethod
	@WebResult(name="nWareneingangId")
	public int saveWareneingangEx (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr) {
		return saveWareneingangLagerEx(idUser, mandantCNr, bestellungId,csvPos,lieferscheinCNr,null);
	}

	@WebMethod
	@WebResult(name="iResult")
	public int saveWareneingangLager (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "iLagerIId") Integer lagerIId) {

		int res = saveWareneingangLagerEx(idUser, mandantCNr, bestellungId, csvPos, lieferscheinCNr, lagerIId);
		if (res > 0) {
			return 1;
		} else {
			return res;
		}
	}
	
	@WebMethod
	@WebResult(name="nWareneingangId")
	public int saveWareneingangLagerEx (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr,
			@WebParam(name = "iLagerIId") Integer lagerIId) {


		TheClientDto theClientDto = check(idUser);

		Integer kundeId = null;
		if (theClientDto.getHvmaLizenzId() != null) {
			kundeId = getKundeId(null, theClientDto);
		} else {
			kundeId = getKundeId(getIP(), null);
		}

		if (kundeId == null)
			return -1;
		if (lieferscheinCNr.length() == 0) 
			return -2;
		Kunde kunde = em.find(Kunde.class, kundeId);

		// zuerst pruefen ob Wareneingang erforderlich
		LpListWrapper ll = new LpListWrapper(csvPos);
		Iterator<?> it = ll.getList().iterator();
		boolean wareneingangErforderlich = false;
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Double(pos[_WEPOS_MENGE]).doubleValue() != 0) {
				wareneingangErforderlich = true;
				break;
			}
		}

		Integer weId = null;
		if (wareneingangErforderlich) {

			// jetzt wareneingang durchfuehren
			WareneingangDto[] wdtos = null;
			try {
				wdtos = getWareneingangFac().wareneingangFindByBestellungIId(bestellungId);
			} catch (EJBExceptionLP e1) {
				//
			} catch (RemoteException e1) {
				//
			}
			if (wdtos != null) {
				for(int i=0; i<wdtos.length; i++) {
					if (wdtos[i].getCLieferscheinnr().equals(lieferscheinCNr)) {
						weId = wdtos[i].getIId();
						break;
					}
				}
			}
			if (weId == null) {
				WareneingangDto wedto = new WareneingangDto();
				wedto.setBestellungIId(bestellungId);
				wedto.setCLieferscheinnr(lieferscheinCNr);
				wedto.setTLieferscheindatum(getTimestamp());
				wedto.setTWareneingangsdatum(getTimestamp());
				
				if (lagerIId == null)
					wedto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
				else
					wedto.setLagerIId(lagerIId);

				wedto.setNWechselkurs(new BigDecimal(1));
				try {
					weId = getWareneingangFac().createWareneingang(wedto, theClientDto);
				} catch (EJBExceptionLP e) {
					throw new EJBExceptionLP(e);
				} catch (RemoteException e) {
					throw new EJBExceptionLP(e);
				}
			}

			Integer kommastellen = getIntegerParameter(mandantCNr, ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN);
			ll = new LpListWrapper(csvPos);
			it = ll.getList().iterator();
			while (it.hasNext()) {
				String[] pos = (String[])it.next();
				if (new Double(pos[_WEPOS_MENGE]).doubleValue() != 0) {
					try {
						WareneingangspositionDto weposDto = new WareneingangspositionDto();
						weposDto.setBestellpositionIId(new Integer(pos[_WEPOS_ARTIKELID]));
						weposDto.setNGeliefertemenge(mengeUmrechnen(pos, _WEPOS_MENGE, _WEPOS_UMRECHNUNG, _WEPOS_EINHEITINVERS, kommastellen));
						weposDto.setPersonalIIdAendern(theClientDto.getIDPersonal());
						weposDto.setPersonalIIdAnlegen(theClientDto.getIDPersonal());
						weposDto.setTAendern(new Timestamp(System.currentTimeMillis()));
						weposDto.setTAnlegen(weposDto.getTAendern());
						weposDto.setWareneingangIId(weId);
						BigDecimal nGeliefertpreis = getPreisBestellPos(weposDto.getBestellpositionIId());
						weposDto.setNGelieferterpreis(nGeliefertpreis);
						getWareneingangFac().createWareneingangsposition(weposDto, theClientDto);
					} catch (EJBExceptionLP e) {
						throw new EJBExceptionLP(e);
					} catch (RemoteException e) {
						throw new EJBExceptionLP(e);
					}
				}
			}
		}

		// anschlieszend Positionen pruefen die erledigt werden sollen
		it = ll.getList().iterator();
		while (it.hasNext()) {
			String[] pos = (String[])it.next();
			if (new Boolean(pos[_WEPOS_ERLEDIGT])) {
				try {
					getBestellpositionFac().manuellAufVollstaendigGeliefertSetzen(new Integer(pos[_WEPOS_ARTIKELID]), theClientDto);
				} catch (NumberFormatException e) {
					return -3;
				} catch (EJBExceptionLP e) {
					return -3;
				} catch (RemoteException e) {
					return -3;
				}
			}
		}

		if (weId == null) {
			return 0;
		}
		return weId;
	}
	
	private BigDecimal getPreisBestellPos(Integer bestellpositionIId) {
		Bestellposition bspos = em.find(Bestellposition.class, bestellpositionIId);
		if (bspos == null) {
			return new BigDecimal(0);
		} else {
			return bspos.getNNettogesamtpreis();
		}
	}

	private LieferscheinpositionDto createLieferscheinPositionDto(Integer lsId, String[] posInfo, boolean bLieferant, 
			Integer lieferantId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		LieferscheinpositionDto lspos = new LieferscheinpositionDto();
		if (bLieferant)
			lspos.setArtikelIId(new Integer(posInfo[_BESPOS_ARTIKELID]));
		else
			lspos.setArtikelIId(new Integer(posInfo[3]));
		Artikel artikel = getArtikel(lspos.getArtikelIId());
		lspos.setLieferscheinIId(lsId);
		lspos.setFRabattsatz(new Double(0));
		lspos.setFZusatzrabattsatz(new Double(0));
		lspos.setLieferscheinpositionartCNr(LieferscheinpositionFac.LIEFERSCHEINPOSITIONSART_IDENT);
		lspos.setNMenge(new BigDecimal(posInfo[0]));
		lspos.setEinheitCNr(artikel.getEinheitCNr());
		lspos.setNEinzelpreis(new BigDecimal(0));
		lspos.setNNettoeinzelpreis(new BigDecimal(0));
		lspos.setNBruttoeinzelpreis(new BigDecimal(0));
/*		
		MwstsatzDto mwstDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(artikel.getMwstsatzIId(), theClientDto);
*/
		Lieferschein ls = em.find(Lieferschein.class, lsId);
		MwstsatzDto mwstDto = getMandantFac().mwstsatzZuDatumValidate(
				artikel.getMwstsatzIId(), ls.getTBelegdatum(), theClientDto);
		lspos.setMwstsatzIId(mwstDto.getIId());

		if (lieferantId != null) {
			//VkPreisfindungFacBean.getArtikeleinzelverkaufspreis
			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikel.getIId(),
					lspos.getNMenge(), theClientDto.getSMandantenwaehrung(), theClientDto);
			if (alDto != null) {
				if (alDto.getNEinzelpreis() != null) {
					lspos.setNEinzelpreis(alDto.getNEinzelpreis());
					lspos.setNNettoeinzelpreis(alDto.getNNettopreis());
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), 
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
					int stellen = FinanzFac.NACHKOMMASTELLEN;
					if (parameter != null)
						stellen = new Integer(parameter.getCWert());
					lspos.setNBruttoeinzelpreis(lspos.getNNettoeinzelpreis()
							.add(Helper.getProzentWert(lspos.getNNettoeinzelpreis(), new BigDecimal(mwstDto.getFMwstsatz()), stellen)));
				}
			}
		}
	
		lspos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		lspos.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		lspos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		lspos.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		return lspos;
	}

	private AuftragpositionDto createAuftragPositionDto(Integer auftId, String[] posInfo, boolean bLieferant, 
			Integer lieferantId, Date liefertermin, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		AuftragpositionDto aufpos = new AuftragpositionDto();
		if (bLieferant)
			aufpos.setArtikelIId(new Integer(posInfo[_BESPOS_ARTIKELID]));
		else
			aufpos.setArtikelIId(new Integer(posInfo[3]));
		Artikel artikel = getArtikel(aufpos.getArtikelIId());
		aufpos.setBelegIId(auftId);
		aufpos.setFRabattsatz(new Double(0));
		aufpos.setFZusatzrabattsatz(new Double(0));
		aufpos.setPositionsartCNr(AuftragServiceFac.AUFTRAGPOSITIONART_IDENT);
		aufpos.setNMenge(new BigDecimal(posInfo[0]));
		aufpos.setEinheitCNr(artikel.getEinheitCNr());
		aufpos.setNEinzelpreis(new BigDecimal(0));
		aufpos.setNNettoeinzelpreis(new BigDecimal(0));
		aufpos.setNBruttoeinzelpreis(new BigDecimal(0));
		aufpos.setNRabattbetrag(new BigDecimal(0));
		aufpos.setNMwstbetrag(new BigDecimal(0));
/*		
		MwstsatzDto mwstDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(artikel.getMwstsatzIId(), theClientDto);
*/
		Auftrag auftrag = em.find(Auftrag.class, auftId);
		MwstsatzDto mwstDto = getMandantFac().mwstsatzZuDatumValidate(
				artikel.getMwstsatzIId(), auftrag.getTBelegdatum(), theClientDto);
		aufpos.setMwstsatzIId(mwstDto.getIId());

		if (lieferantId != null) {
			//VkPreisfindungFacBean.getArtikeleinzelverkaufspreis
			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreisDesBevorzugtenLieferanten(artikel.getIId(),
					aufpos.getNMenge(), theClientDto.getSMandantenwaehrung(), theClientDto);
			if (alDto != null) {
				if (alDto.getNEinzelpreis() != null) {
					aufpos.setNEinzelpreis(alDto.getNEinzelpreis());
					aufpos.setNNettoeinzelpreis(alDto.getNNettopreis());
					ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(), 
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);
					int stellen = FinanzFac.NACHKOMMASTELLEN;
					if (parameter != null)
						stellen = new Integer(parameter.getCWert());
					aufpos.setNBruttoeinzelpreis(aufpos.getNNettoeinzelpreis()
							.add(Helper.getProzentWert(aufpos.getNNettoeinzelpreis(), new BigDecimal(mwstDto.getFMwstsatz()), stellen)));
				}
			}
		}
	
		aufpos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		aufpos.setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		aufpos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		aufpos.setBRabattsatzuebersteuert(Helper.boolean2Short(false));
		aufpos.setTUebersteuerbarerLiefertermin(new Timestamp(liefertermin.getTime()));
		return aufpos;
	}

	private Artikel getArtikel(Integer iId) {
		Artikel artikel = em.find(Artikel.class, iId);
		return artikel;
	}
	
	private Artikel getArtikelByCnr(String cNr) {
		Query query = em.createNamedQuery("ArtikelfindByCNr");
		query.setParameter(1, cNr);
		query.setMaxResults(1);
		Artikel artikel = (Artikel) query.getSingleResult();
		return artikel;
	}
	
	private String getIP() {
//        if (wsContext == null) {
//        	CommonMessageContext msgContext = MessageContextAssociation.peekMessageContext();
//        	//wsContext = new WebServiceContextEJB((SOAPMessageContextJAXWS)msgContext); SP6910
//        	wsContext = new WebServiceContextEJB((SOAPMessageContext)msgContext);
//        }
		
		if(wsContext == null) {
			// TODO: fixme. Wird auf Restapi umgestellt.
			// ghp 10.9.2020: Im Wildfly ist wsContext gesetzt
			return "127.0.0.1";
		}
	
        //System.out.println(wsContext.getMessageContext().toString());
        //   SOAPMessageContext jaxwsContext = (SOAPMessageContext)wsContext.getMessageContext(); SP6910
        javax.xml.ws.handler.MessageContext jaxwsContext = (javax.xml.ws.handler.MessageContext)wsContext.getMessageContext();
        //JBoss 4 Headers
        if (jaxwsContext.containsKey("javax.xml.ws.http.request.headers")) {
        	HashMap h = (HashMap)jaxwsContext.get("javax.xml.ws.http.request.headers");
        	if (h.containsKey("x-forwarded-for")) {
                //System.out.println("Client-IP forwarded: " + ((ArrayList)h.get("x-forwarded-for")).get(0).toString());
        		myLogger.info("Client-IP forwarded: " + ((ArrayList)h.get("x-forwarded-for")).get(0).toString());
        		return ((ArrayList)h.get("x-forwarded-for")).get(0).toString();
        	}
        }
        //Wildfly Headers
        if (jaxwsContext.containsKey("org.apache.cxf.message.Message.PROTOCOL_HEADERS")) {
        	TreeMap h = (TreeMap)jaxwsContext.get("org.apache.cxf.message.Message.PROTOCOL_HEADERS");
        	if (h.containsKey("x-forwarded-for")) {
                //System.out.println("Client-IP forwarded: " + ((ArrayList)h.get("x-forwarded-for")).get(0).toString());
        		myLogger.info("Client-IP forwarded: " + ((ArrayList)h.get("x-forwarded-for")).get(0).toString());
        		return ((ArrayList)h.get("x-forwarded-for")).get(0).toString();
        	}
        }
        HttpServletRequest hRequest = (HttpServletRequest)jaxwsContext.get(MessageContext.SERVLET_REQUEST);
        //System.out.println(hRequest.getRemoteAddr());
        myLogger.info("Client-IP: " + hRequest.getRemoteAddr().toString());
        return hRequest.getRemoteAddr();
	}

	private Integer getKundeId(String ipAddress, TheClientDto theClientDto) {
		if (theClientDto == null)
			return getIntegerApParameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_ESSENSAUSGABE_KUNDEID);
		else
			return getHvmaFac().getEssensausgabeKundeIdKueche(theClientDto);
	}

	private Integer getPartnerKlasseId(String ipAddress) {
		return getIntegerApParameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PARTNERKLASSEID);
	}

	private Integer getVaterArtikelgruppe(String ipAddress) {
		return getIntegerApParameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_VATERARTIKELGRUPPEID);
	}
	
	private String getStringApParameter(String ipAddress, String apParameter) {
		ArbeitsplatzparameterDto app;
		try {
			app = getParameterFac().
				holeArbeitsplatzparameter(ipAddress, apParameter);
		} catch (Exception e) {
			return null;
		}
		if (app == null) return null;
		return app.getCWert();
	}
	
	private Integer getIntegerApParameter(String ipAddress, String apParameter) {
		ArbeitsplatzparameterDto app;
		try {
			app = getParameterFac().
				holeArbeitsplatzparameter(ipAddress, apParameter);
		} catch (Exception e) {
			return null;
		}
		if (app == null) return null;
		return new Integer(app.getCWert());
	}
	
	private Integer getIntegerParameter(String mandantCnr, String kategorie, String parameter) {
		ParametermandantDto p;
		try {
			p = getParameterFac().getMandantparameter(mandantCnr, kategorie, parameter);
		} catch (Exception e) {
			return null;
		}
		return new Integer(p.getCWert());
	}
	
	private Integer getRechte(String ipAddress) {
		ArbeitsplatzparameterDto app;
		try {
			app = getParameterFac().
				holeArbeitsplatzparameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_WEB_MENURECHTE);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		if (app == null) return 0;
		return new Integer(app.getCWert());
	}

	private Kunde getKunde(String ipAddress) {
		ArbeitsplatzparameterDto app;
		try {
			app = getParameterFac().
				holeArbeitsplatzparameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_ESSENSAUSGABE_KUNDEID);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (app == null) return null;
		Kunde kunde = em.find(Kunde.class, new Integer(app.getCWert()));
		return kunde;
	}

	private Kunde getKunde(TheClientDto theClientDto) {
		Integer kundeIId = getHvmaFac().getEssensausgabeKundeIdKueche(theClientDto);
		if (kundeIId == null) return null;
		
		Kunde kunde = em.find(Kunde.class, kundeIId);
		return kunde;
	}

	private Partner getPartner(Integer iId) {
		Partner partner = em.find(Partner.class, iId);
		return partner;
	}
	
	private LieferscheinDto createLieferscheinDto(Integer kundeId, String mandantCNr, Integer personalId) {
		LieferscheinDto lsdto = new LieferscheinDto();
		Kunde kunde = em.find(Kunde.class, kundeId);
		lsdto.setStatusCNr(LieferscheinFac.LSSTATUS_ANGELEGT);
		lsdto.setWaehrungCNr(kunde.getWaehrungCNr());
		lsdto.setTBelegdatum(getTimestamp());
		lsdto.setKostenstelleIId(kunde.getKostenstelleIId());
		lsdto.setKundeIIdLieferadresse(kundeId);
		lsdto.setKundeIIdRechnungsadresse(
				(kunde.getPartnerIIdRechnungsadresse()==null ? kundeId : kunde.getPartnerIIdRechnungsadresse()));
		lsdto.setLieferscheinartCNr(LieferscheinFac.LSART_FREI);
		lsdto.setMandantCNr(mandantCNr);
		lsdto.setPersonalIIdAendern(personalId);
		lsdto.setPersonalIIdAnlegen(personalId);
		lsdto.setSpediteurIId(kunde.getSpediteurIId());
		lsdto.setTAendern(getTimestamp());
		lsdto.setTAnlegen(getTimestamp());
		lsdto.setZahlungszielIId(kunde.getZahlungszielIId());
		lsdto.setLieferartIId(kunde.getLieferartIId());
		lsdto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
		// TODO: Wechselkurs fuer Lieferschein
		lsdto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1));
		lsdto.setWaehrungCNr(kunde.getWaehrungCNr());
		return lsdto;
	}

	private AuftragDto createAuftragDto(Integer kundeId, String mandantCNr, Integer personalId, Date dLiefertermin) {
		AuftragDto auftdto = new AuftragDto();
		Kunde kunde = em.find(Kunde.class, kundeId);
		auftdto.setStatusCNr(AuftragServiceFac.AUFTRAGSTATUS_ANGELEGT);
		auftdto.setWaehrungCNr(kunde.getWaehrungCNr());
		auftdto.setTBelegdatum(getTimestamp());
		auftdto.setKostIId(kunde.getKostenstelleIId());
		auftdto.setKundeIIdAuftragsadresse(kundeId);
		auftdto.setKundeIIdLieferadresse(kundeId);
		auftdto.setKundeIIdRechnungsadresse(
				(kunde.getPartnerIIdRechnungsadresse()==null ? kundeId : kunde.getPartnerIIdRechnungsadresse()));
		auftdto.setAuftragartCNr(AuftragServiceFac.AUFTRAGART_FREI);
		auftdto.setMandantCNr(mandantCNr);
		auftdto.setPersonalIIdAendern(personalId);
		auftdto.setPersonalIIdAnlegen(personalId);
		auftdto.setSpediteurIId(kunde.getSpediteurIId());
		auftdto.setTAendern(getTimestamp());
		auftdto.setTAnlegen(getTimestamp());
		auftdto.setZahlungszielIId(kunde.getZahlungszielIId());
		auftdto.setLieferartIId(kunde.getLieferartIId());
		auftdto.setDLiefertermin(new Timestamp(dLiefertermin.getTime()));
		auftdto.setLagerIIdAbbuchungslager(kunde.getLagerIIdAbbuchungslager());
		// TODO: Wechselkurs fuer Lieferschein
		auftdto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1));
		auftdto.setCAuftragswaehrung(kunde.getWaehrungCNr());
		auftdto.setPersonalIIdVertreter(personalId);
		return auftdto;
	}

	private BestellungDto createBestellungDto(Kunde kunde, Lieferant lieferant, String mandantCNr, Integer personalId, Integer kostenstelleId, String cBez) {
		BestellungDto bsdto = new BestellungDto();
		
		bsdto.setBestellungartCNr(BestellungFac.BESTELLUNGART_FREIE_BESTELLUNG_C_NR);
		bsdto.setStatusCNr(BestellungFac.BESTELLSTATUS_ANGELEGT);
		bsdto.setBTeillieferungMoeglich(Helper.boolean2Short(false));
		bsdto.setDBelegdatum(new Date(System.currentTimeMillis()));
		bsdto.setDLiefertermin(new Timestamp(System.currentTimeMillis()));
		// TODO: Wechselkurs fuer Bestellung
		bsdto.setFWechselkursmandantwaehrungzubelegwaehrung(new Double(1));
		bsdto.setFAllgemeinerRabattsatz(new Double(0));
		bsdto.setLieferantIIdBestelladresse(lieferant.getIId());
		bsdto.setLieferantIIdRechnungsadresse(lieferant.getIId());
		bsdto.setLieferartIId(lieferant.getLieferartIId());
		bsdto.setMandantCNr(mandantCNr);
		bsdto.setPartnerIIdLieferadresse(kunde.getPartnerIId());
		bsdto.setPersonalIIdAendern(personalId);
		bsdto.setPersonalIIdAnlegen(personalId);
		bsdto.setPersonalIIdAnforderer(personalId);
		bsdto.setSpediteurIId(lieferant.getSpediteurIId());
		bsdto.setTAendern(getTimestamp());
		bsdto.setTAnlegen(getTimestamp());
		bsdto.setZahlungszielIId(lieferant.getZahlungszielIId());
		bsdto.setKostenstelleIId(kostenstelleId);
		bsdto.setWaehrungCNr(lieferant.getWaehrungCNr());
		bsdto.setCBez(cBez);
		return bsdto;
	}

	private BestellpositionDto createBestellPositionDto(Integer bsId, Integer lieferantId, String[] posInfo, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
		BestellpositionDto bspos = new BestellpositionDto();
		bspos.setArtikelIId(new Integer(posInfo[_BESPOS_ARTIKELID]));
		Artikel artikel = getArtikel(bspos.getArtikelIId());
		bspos.setBestellungIId(bsId);
		bspos.setPositionsartCNr(BestellpositionFac.BESTELLPOSITIONART_IDENT);
		bspos.setNMenge(mengeUmrechnen(posInfo,_BESPOS_MENGE, _BESPOS_UMRECHNUNG, _BESPOS_EINHEITINVERS, 3));
		bspos.setEinheitCNr(artikel.getEinheitCNr());
		ArtikellieferantDto artikellieferantDto = getArtikelFac().getArtikelEinkaufspreis(
				artikel.getIId(), lieferantId, bspos.getNMenge(), theClientDto.getSMandantenwaehrung(),null, theClientDto);
		if (artikellieferantDto == null) {
			bspos.setNNettoeinzelpreis(new BigDecimal(0));
			bspos.setNNettogesamtpreis(new BigDecimal(0));
			bspos.setDRabattsatz(new Double(0));
		} else {
			bspos.setNNettoeinzelpreis(artikellieferantDto.getNEinzelpreis());
			bspos.setNNettogesamtpreis(artikellieferantDto.getNNettopreis());
			bspos.setDRabattsatz(artikellieferantDto.getFRabatt());
		}
		bspos.setNRabattbetrag(Helper.getProzentWert(bspos.getNNettogesamtpreis(), new BigDecimal(bspos.getDRabattsatz()), 2));
		//bspos.setNNettogesamtPreisminusRabatte(bspos.getNNettogesamtpreis().subtract(bspos.getNRabattbetrag()));
		//bspos.setMwstsatzIId(artikel.getMwstsatzIId());
		bspos.setBArtikelbezeichnunguebersteuert(Helper.boolean2Short(false));
		bspos.setBNettopreisuebersteuert(Helper.boolean2Short(false));
		bspos.setBRabattsatzUebersteuert(Helper.boolean2Short(false));
		bspos.setNMaterialzuschlag(new BigDecimal(0));
		return bspos;
		
	}

	private BigDecimal mengeUmrechnen(String[] posInfo, int posMenge, int posFaktor, int posEinheitInvers, int stellen) {
		BigDecimal nMenge = new BigDecimal(posInfo[posMenge]);
		boolean bInvers = false;
		if (posFaktor < posInfo.length) {
			if (posEinheitInvers < posInfo.length) {
				bInvers = posInfo[posEinheitInvers].equals("true");
			}
			BigDecimal nUmrechnung = new BigDecimal(posInfo[posFaktor]);
			if (bInvers)
				nMenge = Helper.rundeKaufmaennisch(nMenge.multiply(nUmrechnung), stellen);
			else
				nMenge = nMenge.divide(nUmrechnung, 3, BigDecimal.ROUND_HALF_EVEN);
		}
		return nMenge;
	}

	private Integer findTagesBestellung(Integer lieferantIId, String mandantCNr, Integer kostenstelleIId) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts = Helper.cutTimestamp(ts);
		return findBestellungZuDatumVonBis(lieferantIId, mandantCNr, kostenstelleIId, ts, ts);
	}

	private Integer findBestellungZuDatumVonBis(Integer lieferantIId, String mandantCNr, Integer kostenstelleIId, Timestamp tsVon, Timestamp tsBis) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT b.i_id "
			+ "FROM FLRBestellung b "  
			+ "WHERE b.mandant_c_nr='" + mandantCNr + "' "
			+ "AND b.lieferant_i_id_bestelladresse=" + lieferantIId + " "
			+ "AND b.kostenstelle_i_id =" + kostenstelleIId + " "
			+ "AND b.t_belegdatum >= :datevon "
			+ "AND b.t_belegdatum <= :datebis "
			+ "ORDER BY b.t_belegdatum DESC";

		org.hibernate.Query query = session.createQuery(sQuery);
		query.setDate("datevon", tsVon);    
		query.setDate("datebis", tsBis);
		query.setMaxResults(1);
		Integer id = null;
		try {
			id = (Integer)query.uniqueResult();
		} catch (HibernateException e) {
			//
		}
		return id;
	}
	
	private Integer findLetztenWareneingang(Integer bestellungIId) {
		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT w.i_id "
			+ "FROM FLRWareneingang w "
			+ "WHERE w.bestellung_i_id=" + bestellungIId + " "
			+ "ORDER BY w.i_sort DESC";
		org.hibernate.Query query = session.createQuery(sQuery);
		query.setMaxResults(1);
		Integer id = (Integer)query.uniqueResult();
		return id;
	}

	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @return CSV Liste der Artikellieferanten
	 */
	@WebMethod
	@WebResult(name="lagerListe")
	public String getLagerListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr) {
		
		TheClientDto theClientDto = check(idUser);
		
		Integer[] lagerIIds = getBenutzerFac().getBerechtigteLagerIIdsEinerSystemrolle(theClientDto.getSystemrolleIId());
		if (lagerIIds.length > 1)
		{
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<lagerIIds.length; i++) {
				Lager lager = em.find(Lager.class, lagerIIds[i]);
				if (lager != null) {
					sb.append(lager.getIId());
					sb.append(";");
					sb.append(lager.getCNr());
					sb.append("\r\n");
				}
			}
			return sb.toString();
		} else {
			return "";
		}
	}
	
}
