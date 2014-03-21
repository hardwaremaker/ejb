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
package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface KundeFac {

	// Umsatzstatistik
	public static final int IDX_KRIT_KUNDE_I_ID = 0;
	public static final int IDX_KRIT_JAHR = 1;
	public static final int ANZAHL_KRITERIEN = 2;

	public static final String KRIT_JAHR_KALENDERJAHR = "Kalenderjahr";
	public static final String KRIT_JAHR_GESCHAEFTSJAHR = "Geschaeftsjahr";

	// FLR Spaltennamen aus Hibernate Mapping.
	public static final String FLR_PARTNER = "flrpartner";
	public static final String FLR_PERSONAL = "flrpersonal";

	public static final String FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 = FLR_PARTNER
			+ "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1;

	public static final String FLR_PARTNER_LANDPLZORT_ORT_NAME = FLR_PARTNER
			+ "." + PartnerFac.FLR_PARTNER_FLRLANDPLZORT + "."
			+ SystemFac.FLR_LP_FLRORT + "." + SystemFac.FLR_LP_ORTNAME;

	public static final String FLR_KUNDE_I_ID = "i_id";
	public static final String FLR_KUNDE_C_KURZNR = "c_kurznr";
	public static final String FLR_KUNDE_C_ABC = "c_abc";
	public static final String FLR_KUNDE_T_LIEFERSPERREAM = "t_liefersperream";
	public static final String FLR_KUNDE_B_ISTINTERESSENT = "b_istinteressent";
	public static final String FLR_KUNDE_PERSONAL_I_ID_BEKOMMEPROVISION = "personal_i_id_bekommeprovision";
	public static final String FLR_KUNDE_KONTO_I_ID_DEBITORENKONTO = "konto_i_id_debitorenkonto";
	public static final String FLR_KUNDE_I_KUNDENNUMMER = "i_kundennummer";
	public static final String FLR_KUNDE_B_VERSTECKTERLIEFERANT = "b_versteckterlieferant";
	public static final String FLR_KONTO = "flrkonto";
	public static final String FLR_KUNDE_MANDANT_C_NR = "mandant_c_nr";
	
	// fldlae: hier - in jeder *Fac - stehen alle maxfeldlaengen fuer alle
	// kundendbtabellenspalten; diese koennen dann am server und
	// in allen clients verwendet werden

	// Maximale Feldlaengen.
	static public int MAX_KURZNR = 3;
	static public int MAX_TOUR = 10;
	static public int MAX_LIEFERANTENNR = 20;
	static public int MAX_FREMDSYSTEMNR = 40;
	static public int MAX_HINWEIS = 80;

	// Maximale, minimale Zahlenbereiche.
	static public final int MAX_GARANTIE_IN_MONATEN = 999;
	static public final int MIN_GARANTIE_IN_MONATEN = 0;

	static public final int MAX_KOPIEN = 99;
	static public final int MIN_KOPIEN = 0;

	static public final int MAX_RABATTSATZ = 1000;
	static public final int MIN_RABATTSATZ = -1000;
	static public final int FRACTION_RABATTSATZ = 2;

	static public final long MAX_KREDITLIMIT = 99999999;
	static public final int MIN_KREDITLIMIT = 0;
	static public final int FRACTION_KREDITLIMIT = 2;

	static public final long MAX_MITARBEITER_ANZAHL = 999999;
	static public final int MIN_MITARBEITER_ANZAHL = 0;
	static public final int FRACTION_MITARBEITERZAHL = 0;

	static public final int MAX_ZESSIONSFAKTOR = 100;
	static public final int MIN_ZESSIONSFAKTOR = 0;
	static public final int FRACTION_ZESSIONSFAKTOR = 0;

	// Maximale Anzahl der Kurznummer.
	static public int MAX_KURZNR_LP5 = 99;

	public Integer createKunde(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Integer createVerstecktenKundenAusLieferant(Integer iIdLieferant,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	public Integer createKundeAusPartner(Integer partnerIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	public void removeKunde(KundeDto kundeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKunde(KundeDto kundeDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KundeDto kundeFindByPrimaryKey(Integer iId, TheClientDto theClientDto)
			throws EJBExceptionLP;
	public KundeDto kundeFindByPrimaryKeySmall(Integer iId);
	

	public KundeDto kundeFindByPrimaryKeySmallWithNull(Integer iId);
	
	public void updateKundeRechnungsadresse(KundeDto kundeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeKundeRechnungsadresse(KundeDto kundeDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public KundeDto kundeFindByPrimaryKeyOhneExc(Integer iIdKundeI,
			TheClientDto theClientDto);

	public KundeDto[] kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
			Integer iIdVkpfArtikelpreislisteStandardpreislisteI, TheClientDto theClientDto)
			throws RemoteException;

	public KundeDto kundeFindByiIdPartnercNrMandantOhneExc(Integer iIdPartnerI,
			String cNrMandantI, TheClientDto theClientDto) throws RemoteException;

	public KontoDto createDebitorenkontoZuKundenAutomatisch(Integer kundeIId, boolean nichtErstellen,
			String kontonummerVorgabe, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer getNextKundennummer(TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void pruefeKundenUIDNummer(Integer kundeIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getKundeAsXML(String cNrI) throws RemoteException;

	public void zusammenfuehrenKunde(KundeDto kundeZielDto,
			int kundeQuellDtoIId, int kundesokoKundeIId,
			Integer kundePartnerIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void reassignAngebotBeimZusammenfuehren(KundeDto kundeZielDto,
			KundeDto kundeQuellDto, String mandantCNr, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public KundeDto[] kundefindByKontoIIdDebitorenkonto(Integer kontoIId);
	
	public KundeDto kundeFindByLieferantenCnrMandantCnrNull(String lieferantenCnr, String mandantCnr) ;	
	
	/**
	 * Eine Liste aller Kunden fuer den angegebenen Mandanten</br>
	 * Die Kunden werden ueber kundeFindByPrimaryKey(Integer kundeIid) ausgelesen
	 * 
	 * @param theClientDto
	 * @return eine (leere) Liste von Kunden in diesem Mandanten
	 */
	List<KundeDto> kundeFindByMandantCnr(TheClientDto theClientDto) ;
	
	/**
	 * Eine Liste aller Kunden fuer die angegebene Kurzbezeichnung</br>
	 * @param kbez
	 * @param theClientDto
	 * @return eine (leere) Liste von Kunden die im angegebenen Mandanten sind und die kbez haben
	 */
	List<KundeDto> kundeFindByKbezMandantCnr(String kbez, TheClientDto theClientDto) ;
	
	/**
	 * Hat der Kunde einen externen Versandweg - wie beispielsweise Clevercure / EDI - definiert?</br>
	 * <p>Der Versandweg ist letztendlich beim Partner hinterlegt</p>
	 * 
	 * @param kundeIId
	 * @param theClientDto
	 * @return true wenn fuer den Kunden ein Versandweg definiert ist
	 * @throws RemoteException
	 */
	boolean hatKundeVersandweg(Integer kundeIId, TheClientDto theClientDto) throws RemoteException ;	
}
