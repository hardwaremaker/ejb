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
package com.lp.server.bestellung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.system.service.IAktivierbarControlled;
import com.lp.server.system.service.IImportHead;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface BestellungFac extends IAktivierbarControlled {
	// Bestellung
	public static final String FLR_BESTELLUNG_I_ID = "i_id";
	public static final String FLR_BESTELLUNG_FLRKOSTENSTELLE = "flrkostenstelle";
	public static final String FLR_BESTELLUNG_FLRLIEFERANT = "flrlieferant";
	public static final String FLR_BESTELLUNG_C_NR = "c_nr";
	public static final String FLR_BESTELLUNG_MANDANT_C_NR = "mandant_c_nr";
	public static final String FLR_BESTELLUNG_T_BELEGDATUM = "t_belegdatum";
	public static final String FLR_BESTELLUNG_T_VOLLSTAENDIG_GELIEFERT = "t_vollstaendig_geliefert";
	public static final String FLR_BESTELLUNG_T_LIEFERTERMIN = "t_liefertermin";
	public static final String FLR_BESTELLUNG_T_MANUELLGELIEFERT = "t_manuellgeliefert";
	public static final String FLR_BESTELLUNG_N_BESTELLWERT = "n_bestellwert";
	public static final String FLR_BESTELLUNG_BESTELLUNGSTATUS_C_NR = "bestellungstatus_c_nr";
	public static final String FLR_BESTELLUNG_C_BEZPROJEKTBEZEICHNUNG = "c_bezprojektbezeichnung";
	public static final String FLR_BESTELLUNG_BESTELLUNGART_C_NR = "bestellungart_c_nr";
	public static final String FLR_BESTELLUNG_BESTELLUNG_I_ID_RAHMENBESTELLUNG = "bestellung_i_id_rahmenbestellung";
	public static final String FLR_BESTELLUNG_WAEHRUNG_C_NR = "waehrung_c_nr_bestellwaehrung";
	public static final String FLR_BESTELLUNG_KOSTENSTELLE_I_ID = "kostenstelle_i_id";
	public static final String FLR_BESTELLUNG_LIEFERANT_I_ID_BESTELLADRESSE = "lieferant_i_id_bestelladresse";
	public static final String FLR_BESTELLUNG_AUFTRAG_I_ID = "auftrag_i_id";
	public static final String FLR_BESTELLUNG_FLRPERSONAL = "flrpersonal";
	public static final String FLR_BESTELLUNG_MAHNSTUFE_I_ID = "mahnstufe_i_id";
	public static final String FLR_BESTELLUNG_T_MAHNSPERREBIS = "t_mahnsperrebis";
	public static final String FLR_BESTELLUNG_FLRPROJEKT = "flrprojekt";
	public static final String FLR_BESTELLUNG_PROJEKT_I_ID = "projekt_i_id";
	public static final String FLR_BESTELLUNG_FLRPOSITONEN_FLRARTIKEL = "flrartikel";

	// Bestellstatus
	public static final String BESTELLSTATUS_ANGELEGT = LocaleFac.STATUS_ANGELEGT;
	public static final String BESTELLSTATUS_OFFEN = LocaleFac.STATUS_OFFEN;
	public static final String BESTELLSTATUS_BESTAETIGT = LocaleFac.STATUS_BESTAETIGT;
	public static final String BESTELLSTATUS_GELIEFERT = LocaleFac.STATUS_GELIEFERT;
	public static final String BESTELLSTATUS_STORNIERT = LocaleFac.STATUS_STORNIERT;
	public static final String BESTELLSTATUS_ABGERUFEN = LocaleFac.STATUS_ABGERUFEN;
	public static final String BESTELLSTATUS_ERLEDIGT = LocaleFac.STATUS_ERLEDIGT;
	public static final String BESTELLSTATUS_TEILERLEDIGT = LocaleFac.STATUS_TEILERLEDIGT;

	public static final long MAX_I_LEIHTAGE = 9999;
	public static final long MIN_I_LEIHTAGE = 0;
	public static final int FRACTION_LEIHTAGE = 0;

	// Bestellungart
	public static final String BESTELLUNGART_FREIE_BESTELLUNG_C_NR = "Freie Bestellung    ";
	public static final String BESTELLUNGART_ABRUFBESTELLUNG_C_NR = "Abrufbestellung     ";
	public static final String BESTELLUNGART_RAHMENBESTELLUNG_C_NR = "Rahmenbestellung    ";
	public static final String BESTELLUNGART_LEIHBESTELLUNG_C_NR = "Leihbestellung      ";
	public static final String BESTELLUNGART_ABRUFBESTELLUNG_C_NR_KURZ = "A";
	public static final String BESTELLUNGART_RAHMENBESTELLUNG_C_NR_KURZ = "R";
	public static final String BESTELLUNGART_LEIHBESTELLUNG_C_NR_KURZ = "L";

	public BestellungDto createBestellungDto(Integer lieferantIId,
			String mandantCNr, Integer personalId);

	public BestellpositionDto createBestellPositionDto(Integer bsId,
			Integer lieferantId, Integer artikelIId, BigDecimal menge,
			TheClientDto theClientDto);

	public Integer createBestellung(BestellungDto bestellungDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellung(BestellungDto bestellungDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateBestellung(BestellungDto bestellungDtoI,
			TheClientDto theClientDto, boolean pruefeKorrekterStatus)
			throws EJBExceptionLP;

	public void updateBestellungMahnstufe(Integer bestellungIId,
			Integer mahnstufeNeu, TheClientDto theClientDto);

	public BestellungDto bestellungFindByPrimaryKey(Integer iIdBSI)
			throws EJBExceptionLP, RemoteException;

	public BestellungDto bestellungFindByCNrMandantCNr(String cNr,
			String mandantCNr);

	public BestellungDto[] bestellungFindByLieferadressePartnerIIdMandantCNr(
			Integer partnerIId, String cNrMandantI) throws EJBExceptionLP,
			RemoteException;

	public BestellungDto[] bestellungFindByLieferadressePartnerIIdMandantCNrOhneExc(
			Integer partnerIId, String cNrMandantI) throws RemoteException;

	public BestellungDto[] bestellungFindByAnsprechpartnerIIdMandantCNr(
			Integer iAnsprechpartnerIId, String cNrMandantI)
			throws EJBExceptionLP, RemoteException;

	public BestellungDto[] bestellungFindByAnsprechpartnerIIdMandantCNrOhneExc(
			Integer iAnsprechpartnerIId, String cNrMandantI)
			throws RemoteException;

	public void pruefeUndSetzeBestellungstatusBeiAenderung(
			Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void aktiviereBestellung(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneNettowertGesamt(Integer iIdBestellungI,
			TheClientDto theClientDto) throws RemoteException;

	public void updateBestellungKonditionen(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void manuellFreigeben(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void manuellErledigen(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BigDecimal berechneEinkaufswertIst(Integer iIdBestellungI,
			String sArtikelartI, TheClientDto theClientDto);

	public void stornoAufheben(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void stornieren(Integer iIdBestellungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	// public void setzeBestellungstatus(Integer iIdBSI, String sStatusNewI,
	// TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer erzeugeBestellungAusAnfrage(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public BestellungDto[] bestellungFindByAnfrage(Integer iIdAnfrageI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void erledigenAufheben(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;



	public BestellungDto[] abrufBestellungenfindByRahmenbestellung(
			Integer iIdRahmenBestellungI, TheClientDto theClientDto)
			throws RemoteException;

	public BestellungDto bestellungFindByPrimaryKeyOhneExc(Integer iBestellungId)
			throws RemoteException;

	public BestellungDto bestellungFindByPrimaryKeyWithNull(
			Integer iBestellungId);

	public BestellungDto[] bestellungFindByLieferantIIdBestelladresseMandantCNr(
			Integer lieferantIId, String cNrMandantI) throws EJBExceptionLP,
			RemoteException;

	public BestellungDto[] bestellungFindByLieferantIIdBestelladresseMandantCNrOhneExc(
			Integer lieferantIId, String cNrMandantI) throws RemoteException;

	public BestellungDto[] bestellungFindByLieferantIIdRechnungsadresseMandantCNr(
			Integer lieferantIId, String cNrMandantI) throws EJBExceptionLP,
			RemoteException;

	public BestellungDto[] bestellungFindByLieferantIIdRechnungsadresseMandantCNrOhneExc(
			Integer lieferantIId, String cNrMandantI) throws RemoteException;

	public BestellungDto[] bestellungFindByAuftragIId(Integer auftragIId);

	// public void updateRahmenbestellung(BestellungDto bestellungDtoI,
	// TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	// public void updateBestellungOhneWeitereAktionen(
	// BestellungDto bestellungDto, TheClientDto theClientDto)
	// throws EJBExceptionLP, RemoteException;

	public BestellungDto[] findBestellungByMandantCNrAndBestellstatus(
			TheClientDto theClientDto, String bsStatus) throws EJBExceptionLP,
			RemoteException;

	public void setzeBSMahnsperre(Integer bestellungIId, Date tmahnsperre,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Boolean isBSErledigt(Integer iIdBestellungI,
			TheClientDto theClientDto) throws RemoteException;

	public Boolean isBSGeliefert(Integer iIdBestellungI,
			TheClientDto theClientDto) throws RemoteException;

	

	public Integer erzeugeBestellungAusBestellung(Integer iIdBestellungI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void versucheBestellungAufErledigtZuSetzen(Integer bestellungIIdI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer erzeugeEingangsrechnungAusBestellung(Integer iIdBestellungI,
			EingangsrechnungDto eingangsrechnungDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getRichtigenBestellStatus(Integer bestellungIId,
			boolean bRekursiv, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public BestellungDto[] bestellungenFindAll();

	public void importiereMonatsbestellung(
			ArrayList<ImportMonatsbestellungDto> importMonatbestellung,
			TheClientDto theClientDto);

	public String checkBestellStati(TheClientDto theClientDto);

	public void setzeVersandzeitpunktAufJetzt(Integer iBestellungIId,
			String sDruckart);

	public void setAuftragIIdInBestellung(BestellungDto bestellungDto,
			TheClientDto theClientDto);

	public ArrayList<Integer> getAngelegtenBestellungen(Integer lieferantIId,
			TheClientDto theClientDto);

	public void updateBestellungRechnungsadresse(Integer bestellungIId,
			Integer lieferantIIdRechnungsadresseNeu, TheClientDto theClientDto);

	/**
	 * Liefert die Bean als HeadImporter zur&uuml;ck, um auf die Methoden des
	 * Interfaces {@link IImportHead} zuzugreifen.
	 * 
	 * @return this
	 */
	public IImportHead asHeadImporter();

	public Integer getWiederbeschaffungsmoralEinesArtikels(Integer artikelIId,
			TheClientDto theClientDto);

	public Integer getWiederbeschaffungsmoralEinesArtikels(Integer artikelIId,Integer lieferantIId,  TheClientDto theClientDto);
	
	public void bestellungAufPauschalbetragSetzten(Integer bestellungIId,
			BigDecimal bdPauschalbetrag, TheClientDto theClientDto);
	public void bestellungAusAnderemMandantRueckbestaetigen(Integer auftragIId, TheClientDto theClientDto);

	List<BestellungDto> bestellungFindByLieferantIIdBestelladresseMandantCNrFilter(
			Integer lieferantId, String mandantCnr, String[] allowedStati) throws RemoteException;

	BestellungDto erzeugeAenderungsbestellung(Integer bestellungIId, TheClientDto theClientDto);

	void archiviereOpenTransResult(OpenTransXmlReportResult otResult, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public BigDecimal berechneEinstandswertEinerBestellung(Integer iIdBestellungI, boolean bNurHandeingaben,
			TheClientDto theClientDto);
	public BigDecimal berechneOffenenWertEinerBestellung(String bestellungCNr, TheClientDto theClientDto);
	
}
