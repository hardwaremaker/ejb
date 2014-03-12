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
package com.lp.server.kueche.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jboss.ws.core.CommonMessageContext;
import org.jboss.ws.core.jaxws.handler.SOAPMessageContextJAXWS;
import org.jboss.ws.core.soap.MessageContextAssociation;
import org.jboss.wsf.spi.invocation.WebServiceContextEJB;

import com.lp.server.artikel.ejb.Artikel;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.EinkaufseanDto;
import com.lp.server.bestellung.ejb.Bestellposition;
import com.lp.server.bestellung.ejb.Bestellung;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.ejb.Kunde;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.partner.ejb.Partner;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.personal.ejb.Personal;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LpListWrapper;

@WebService(name="Kueche", serviceName="Kueche")
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

		Integer kundeId = getKundeId(getIP());
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
	 * @return CSV Liste der Artikellieferanten
	 */
	@WebMethod
	@WebResult(name="lieferantListe")
	public String getLieferantListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr) {
		check(idUser);

		Integer partnerKlasseId = getPartnerKlasseId(getIP());
		
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

		Kunde kunde = getKunde(getIP());
		
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
				+ "WHERE l.flrartikel.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.lieferant_i_id=" + lieferantId + " "
				+ "AND l.b_webshop=" + Helper.boolean2Short(true) + " "
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
				+ "WHERE l.flrartikel.mandant_c_nr='" + mandantCNr + "' "
				+ "AND l.lieferant_i_id=" + lieferantId + " "
				+ "AND l.b_webshop=" + Helper.boolean2Short(true) + " "
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
		resultList = sort(resultList, 2);
		return resultList.toCSV();
	}

	@SuppressWarnings("unchecked")
	private LpListWrapper sort(LpListWrapper list, int iCol) {
		final int col = iCol;
		ArrayList al = (ArrayList) list.getList();
		Collections.sort((List<Object[]>) al, new Comparator(){
			public int compare(Object o1, Object o2) {
	           Object[] oa1 = (Object[]) o1;
	           Object[] oa2 = (Object[]) o2;
	           String s1 = (String) oa1[col];
	           String s2 = (String) oa2[col];
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
	
	/**
	 * 
	 * @param idUser
	 * @param mandantCNr
	 * @param lieferantId
	 * @param csvPos
	 * @param lieferscheinCNr
	 * @param bDirekt	true ... Bestellung samt Wareneingang wird erstellt
	 * 					false .. Bestellung mit Status Angelegt wird erstellt
	 * @return	1 ... erfolgreich angelegt
	 * 			0 ... keine Bestellung erstellt da alle Mengen 0
	 * 			-1 .. keine Kundenzuordnung zu IP
	 * 			-2 .. keine Lieferscheinnummer bei Direktbestellung angegeben
	 */
	@WebMethod
	@WebResult(name="iResult")
	public int saveBestellung (
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
		
		Integer kundeId = getKundeId(getIP());
		if (kundeId == null)
			return -1;
		if (bDirekt && (lieferscheinCNr.length() == 0)) 
			return -2;
		
		Kunde kunde = em.find(Kunde.class, kundeId);
		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer kostenstelleIId = getKostenstelle(theClientDto.getIDPersonal());
		BestellungDto bsdto = createBestellungDto(kunde, lieferant, mandantCNr, theClientDto.getIDPersonal(), kostenstelleIId);
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
				getWareneingangFac().uebernimmAlleWepsOhneBenutzerinteraktion(weId, bsId, theClientDto);
			} catch (EJBExceptionLP e) {
				throw new EJBExceptionLP(e);
			} catch (RemoteException e) {
				throw new EJBExceptionLP(e);
			}
			return 1;
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
		
		Integer kundeId = getKundeId(getIP());
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
		String sProjekt = getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTSHOP);
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

		Integer kundeId = getKundeId(getIP());
		if (kundeId == null)
			return -1;
		Kunde kunde = em.find(Kunde.class, kundeId);
		Lieferant lieferant = em.find(Lieferant.class, lieferantId);
		Integer kostenstelleId = kunde.getKostenstelleIId();
		Integer bsId = findTagesBestellung(lieferantId, mandantCNr, kostenstelleId);
		if (bsId == null) {
			BestellungDto bsdto = createBestellungDto(kunde, lieferant, mandantCNr, theClientDto.getIDPersonal(),kostenstelleId);
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

		Integer kundeId = getKundeId(getIP());
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
				getWareneingangFac().uebernimmAlleWepsOhneBenutzerinteraktion(weId, bestellung.getIId(), theClientDto);
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

		Integer iResult = getRechte(getIP());
		return iResult;
	}
	
	@WebMethod
	@WebResult(name="bestellungListe")
	public String getBestellungOffenListe(
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr) {
		check(idUser);

		Integer kundeId = getKundeId(getIP());
		if (kundeId == null)
			return "KEIN_KUNDE";
		Kunde kunde = em.find(Kunde.class, kundeId);

		Session session = FLRSessionFactory.getFactory().openSession();
		String sQuery = "SELECT b.i_id, "
			+ "b.c_nr, "
			+ "b.t_belegdatum, "
			+ "(SELECT l.flrpartner.c_name1nachnamefirmazeile1 FROM FLRLieferant l WHERE l.i_id = b.lieferant_i_id_bestelladresse), "
			+ "b.bestellungstatus_c_nr "
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
	@WebResult(name="iResult")
	public int saveWareneingang (
			@WebParam(name = "idUser") String idUser,
			@WebParam(name = "sMandant") String mandantCNr,
			@WebParam(name = "iBestellungId") Integer bestellungId,
			@WebParam(name = "sCsvPos") String csvPos,
			@WebParam(name = "sLieferscheinNr") String lieferscheinCNr) {


		TheClientDto theClientDto = check(idUser);

		Integer kundeId = getKundeId(getIP());
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
			Integer weId = null;
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
				wedto.setLagerIId(kunde.getLagerIIdAbbuchungslager());
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

		return 1;
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
		MwstsatzDto mwstDto = getMandantFac().mwstsatzFindByMwstsatzbezIIdAktuellster(artikel.getMwstsatzIId(), theClientDto);
		lspos.setMwstsatzIId(mwstDto.getIId());

		if (lieferantId != null) {
			//VkPreisfindungFacBean.getArtikeleinzelverkaufspreis
			ArtikellieferantDto alDto = getArtikelFac().getArtikelEinkaufspreis(artikel.getIId(),
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
	
	private Artikel getArtikel(Integer iId) {
		Artikel artikel = em.find(Artikel.class, iId);
		return artikel;
	}
	
	private String getIP() {
        if (wsContext == null) {
        	CommonMessageContext msgContext = MessageContextAssociation.peekMessageContext();
        	wsContext = new WebServiceContextEJB((SOAPMessageContextJAXWS)msgContext);
        }
	 SOAPMessageContextJAXWS jaxwsContext = (SOAPMessageContextJAXWS)wsContext.getMessageContext();
	 HttpServletRequest hRequest = (HttpServletRequest)jaxwsContext.get(MessageContext.SERVLET_REQUEST);
	 return hRequest.getRemoteAddr();
	}

	private Integer getKundeId(String ipAddress) {
		return getIntegerApParameter(ipAddress, ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_ESSENSAUSGABE_KUNDEID);
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

	private BestellungDto createBestellungDto(Kunde kunde, Lieferant lieferant, String mandantCNr, Integer personalId, Integer kostenstelleId) {
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
		bsdto.setCBez(getStringApParameter(getIP(), ParameterFac.ARBEITSPLATZPARAMETER_KUECHE_PROJEKTWEB));
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
}
