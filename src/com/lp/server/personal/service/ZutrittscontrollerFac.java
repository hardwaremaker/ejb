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
package com.lp.server.personal.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ZutrittscontrollerFac {

	public static final String FLR_PERSONALZUTRITTSKLASSE_T_GUELTIGAB = "t_gueltigab";
	public static final String FLR_PERSONALZUTRITTSKLASSE_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALZUTRITTSKLASSE_FLRZUTRITTSKLASSE = "flrzutrittsklasse";
	public static final String FLR_PERSONALZUTRITTSKLASSE_FLRPERSONAL = "flrpersonal";

	public static final String FLR_ZUTRITTSCONTROLLER_C_ADRESSE = "c_adresse";
	public static final String FLR_ZUTRITTSCONTROLLER_FLRMANDANT = "flrmandant";
	public static final int MAX_ZUTRITTSCONTROLLER_C_NR = 15;
	public static final int MAX_ZUTRITTSCONTROLLER_C_BEZ = 80;
	public static final int MAX_ZUTRITTSCONTROLLER_C_ADRESSE = 40;

	public static final String FLR_ZUTRITTSOBJEKT_C_RELAIS = "c_relais";
	public static final String FLR_ZUTRITTSOBJEKT_C_ADRESSE = "c_adresse";
	public static final String FLR_ZUTRITTSOBJEKT_ZUTRITTSLESER_C_NR = "zutrittsleser_c_nr";
	public static final String FLR_ZUTRITTSOBJEKT_F_OEFFNUNGSZEIT = "f_oeffnungszeit";
	public static final String FLR_ZUTRITTSOBJEKT_ZUTRITTSCONTROLLER_I_ID = "zutrittscontroller_i_id";
	public static final String FLR_ZUTRITTSOBJEKT_FLRZUTRITTSCONTROLLER = "flrzutrittscontroller";

	public static final String FLR_ZUTRITTSMODELL_X_BEM = "x_bem";

	public static final int MAX_ZUTRITTSOBJEKT_C_RELAIS = 10;
	public static final int MAX_ZUTRITTSOBJEKT_C_BEZ = 80;
	public static final int MAX_ZUTRITTSOBJEKT_C_NR = 15;
	public static final int MAX_ZUTRITTSOBJEKT_C_ADRESSE = 40;
	public static final int MAX_ZUTRITTSOBJEKT_ZUTRITTSLESER_C_NR = 15;

	public static final int MAX_ZUTRITTSKLASSE_C_BEZ = 80;
	public static final int MAX_ZUTRITTSKLASSE_C_NR = 15;

	public static final int MAX_ZUTRITTSMODELL_C_NR = 40;

	public static final String FLR_ZUTRITTSMODELLTAG_ZUTRITTSMODELL_I_ID = "zutrittsmodell_i_id";
	public static final String FLR_ZUTRITTSMODELLTAG_TAGESART_I_ID = "tagesart_i_id";
	public static final String FLR_ZUTRITTSMODELLTAG_FLRZUTRITTSMODELL = "flrzutrittsmodell";
	public static final String FLR_ZUTRITTSMODELLTAG_FLRTAGESART = "flrtagesart";

	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_ZUTRITTSMODELLTAG_I_ID = "zutrittsmodelltag_i_id";
	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_U_OFFENVON = "u_offenvon";
	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_U_OFFENBIS = "u_offenbis";
	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_ZUTRITTOEFFNUNGSART_C_NR = "zutrittsoeffnungsart_c_nr";
	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_FLRZUTRITTSMODELLTAG = "flrzutrittsmodelltag";
	public static final String FLR_ZUTRITTSMODELLTAGDETAIL_B_RESTDESTAGES = "b_restdestages";

	public static final String FLR_ZUTRITTSKLASSEOBJEKT_T_GUELTIGAB = "t_gueltigab";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSKLASSE_I_ID = "zutrittsklasse_i_id";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSOBJEKT_I_ID = "zutrittsobjekt_i_id";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_ZUTRITTSMODELL_I_ID = "zutrittsmodell_i_id";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSKLASSE = "flrzutrittsklasse";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSOBJEKT = "flrzutrittsobjekt";
	public static final String FLR_ZUTRITTSKLASSEOBJEKT_FLRZUTRITTSMODELL = "flrzutrittsmodell";

	public static final String FLR_ZUTRITTONLINECHECK_T_GUELITGAB = "t_gueltigab";
	public static final String FLR_ZUTRITTONLINECHECK_T_GUELITGBIS = "t_gueltigbis";
	public static final String FLR_ZUTRITTONLINECHECK_C_PINCODE = "c_pincode";
	public static final String FLR_ZUTRITTONLINECHECK_C_AUSWEIS = "c_ausweis";
	public static final String FLR_ZUTRITTONLINECHECK_FLRZUTRITTSKLASSE = "flrzutrittsklasse";

	public static final int MAX_ZUTRITTONLINECHECK_C_AUSWEIS = 80;
	public static final int MAX_ZUTRITTONLINECHECK_C_PINCODE = 40;

	public static final String FLR_ZUTRITTSOBJEKTVERWENDUNG_I_ANZAHLVERWENDUNG = "i_anzahlverwendung";
	public static final String FLR_ZUTRITTSOBJEKTVERWENDUNG_FLRZUTRITTSOBJEKT = "flrzutrittsobjekt";

	public static final String FLR_ZUTRITTSLOG_FLRMANDANT = "flrmandant";
	public static final String FLR_ZUTRITTSLOG_C_AUSWEIS = "c_ausweis";
	public static final String FLR_ZUTRITTSLOG_C_PERSON = "c_person";
	public static final String FLR_ZUTRITTSLOG_C_ZUTRITTSOBJEKT = "c_zutrittsobjekt";
	public static final String FLR_ZUTRITTSLOG_C_ZUTRITTSCONTROLLER = "c_zutrittscontroller";
	public static final String FLR_ZUTRITTSLOG_T_ZEITPUNKT = "t_zeitpunkt";
	public static final String FLR_ZUTRITTSLOG_B_ERLAUBT = "b_erlaubt";
	public static final String FLR_ZUTRITTSLOG_MANDANT_C_NR_OBJEKT = "mandant_c_nr_objekt";

	public static final String FLR_ZUTRITTDAUEROFFEN_U_OFFENVON = "u_offenvon";
	public static final String FLR_ZUTRITTDAUEROFFEN_U_OFFENBIS = "u_offenbis";
	public static final String FLR_ZUTRITTDAUEROFFEN_TAGESART_I_ID = "tagesart_i_id";
	public static final String FLR_ZUTRITTDAUEROFFEN_ZUTRITTSOBJEKT_I_ID = "zutrittsobjekt_i_id";
	public static final String FLR_ZUTRITTDAUEROFFEN_FLRZUTRITTSOBJEKT = "flrzutrittsobjekt";
	public static final String FLR_ZUTRITTDAUEROFFEN_FLRTAGESART = "flrtagesart";

	public static final String ZUTRITTSLESER_ARYGONMIFARE = "A              ";
	public static final String ZUTRITTSLESER_PROXLINEMIFARE = "N              ";
	public static final String ZUTRITTSLESER_LEGIC = "L              ";

	public static final String ZUTRITTSOEFFNUNGSART_KEY = "A              ";
	public static final String ZUTRITTSOEFFNUNGSART_KEYMITPINCODE = "P              ";
	public static final String ZUTRITTSOEFFNUNGSART_ONLINECHECK = "O              ";

	public static final String FLR_PERSONALFINGER_C_FINGERART = "c_fingerart";
	public static final String FLR_PERSONALFINGER_FLRPERSONAL = "flrpersonal";
	public static final String FLR_PERSONALFINGER_I_FINGERID = "i_fingerid";
	public static final String FLR_PERSONALFINGER_PERSONAL_I_ID = "personal_i_id";
	public static final String FLR_PERSONALFINGER_I_FINGERSUBID = "i_fingersubid";

	public static final String ZUTRITTSKLASSE_ONLINECHECK = "ONL";

	public Integer createZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP,
			RemoteException;

	public void removeZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP,
			RemoteException;

	public void updateZutrittscontroller(
			ZutrittscontrollerDto zutrittscontrollerDto) throws EJBExceptionLP,
			RemoteException;

	public ZutrittscontrollerDto zutrittscontrollerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ZutrittscontrollerDto zutrittscontrollerFindByCNr(String cBez)
			throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZutrittsklasse(ZutrittsklasseDto zutrittsklasseDto)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsklasseDto zutrittsklasseFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsklasseDto zutrittsklasseFindByCNr(String cNr)
			throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZutrittsmodell(ZutrittsmodellDto zutrittsmodellDto)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsmodellDto zutrittsmodellFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZutrittsobjekt(ZutrittsobjektDto zutrittsobjektDto)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsobjektDto zutrittsobjektFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsobjektDto[] zutrittsobjektFindByZutrittscontrollerIId(
			Integer zutrittscontrollerIId) throws EJBExceptionLP,
			RemoteException;

	public ZutrittsobjektDto zutrittsobjektFindByCNr(String cNr)
			throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllZutrittsoeffnungsarten() throws EJBExceptionLP,
			RemoteException;

	public Map<?, ?> getAllZutrittsleser() throws EJBExceptionLP, RemoteException;

	public void removeZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZutrittsmodelltagdetail(
			ZutrittsmodelltagdetailDto zutrittsmodelltagdetailDto)
			throws EJBExceptionLP, RemoteException;

	public ZutrittsmodelltagdetailDto zutrittsmodelltagdetailFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto)
			throws EJBExceptionLP, RemoteException;

	public void updateZutrittsklasseobjekt(
			ZutrittsklasseobjektDto zutrittsklasseobjektDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public String getZutrittsdatenFuerEinObjektFuerMecs(
			Integer zutrittsobjektIId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public ZutrittsklasseobjektDto zutrittsklasseobjektFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public ZutrittsklasseobjektDto[] zutrittsklasseobjektFindByZutrittsklasseIId(
			Integer zutrittsklasseIId) throws EJBExceptionLP, RemoteException;

	public Integer createZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP,
			RemoteException;

	public void removeZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP,
			RemoteException;

	public void updateZutrittsmodelltag(
			ZutrittsmodelltagDto zutrittsmodelltagDto) throws EJBExceptionLP,
			RemoteException;

	public ZutrittsmodelltagDto zutrittsmodelltagFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createPersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto)
			throws EJBExceptionLP, RemoteException;

	public void updatePersonalzutrittsklasse(
			PersonalzutrittsklasseDto personalzutrittsklasseDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PersonalzutrittsklasseDto personalzutrittsklasseFindByPrimaryKey(
			Integer iId) throws EJBExceptionLP, RemoteException;

	public PersonalzutrittsklasseDto personalzutrittsklasseFindZutrittsklasseZuDatum(
			Integer personalIId, Timestamp dDatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public PersonalzutrittsklasseDto[] personalzutrittsklassenFindByTGueltigab(
			Timestamp tDatum, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public Integer createZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto)
			throws RemoteException, EJBExceptionLP;

	public void updateZutrittonlinecheck(
			ZutrittonlinecheckDto zutrittonlinecheckDto)
			throws RemoteException, EJBExceptionLP;

	public ZutrittonlinecheckDto zutrittonlinecheckFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public Integer createZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws RemoteException;

	public void pruefeZutrittsobjektverwendung(Integer zutrittsklasseIId,
			TheClientDto theClientDto) throws RemoteException;

	public void removeZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws RemoteException, EJBExceptionLP;

	public void updateZutrittsobjektverwendung(
			ZutrittsobjektverwendungDto zutrittsobjektverwendungDto)
			throws RemoteException, EJBExceptionLP;

	public ZutrittsobjektverwendungDto zutrittsobjektverwendungFindByPrimaryKey(
			Integer iId) throws RemoteException, EJBExceptionLP;

	public ZutrittsobjektverwendungDto[] zutrittsobjektverwendungFindByZutrittsobjektIId(
			Integer zutrittsobjektIId) throws EJBExceptionLP, RemoteException;

	public void createZutrittsleser(ZutrittsleserDto zutrittsleserDto)
			throws RemoteException, EJBExceptionLP;

	public void updateZutrittsleser(ZutrittsleserDto zutrittsleserDto)
			throws RemoteException, EJBExceptionLP;

	public ZutrittsleserDto zutrittsleserFindByPrimaryKey(String cNr)
			throws RemoteException, EJBExceptionLP;

	public ZutrittsleserDto[] zutrittsleserFindAll() throws RemoteException,
			EJBExceptionLP;

	public void createZutrittsoeffnungsart(
			ZutrittsoeffnungsartDto zutrittsoeffnungsartDto)
			throws RemoteException, EJBExceptionLP;

	public void updateZutrittsoeffnungsart(
			ZutrittsoeffnungsartDto zutrittsoeffnungsartDto)
			throws RemoteException, EJBExceptionLP;

	public ZutrittsoeffnungsartDto zutrittsoeffnungsartFindByPrimaryKey(
			String cNr) throws RemoteException, EJBExceptionLP;

	public ZutrittsoeffnungsartDto[] zutrittsoeffnungsartFindAll()
			throws RemoteException, EJBExceptionLP;

	public Integer createZutrittslog(ZutrittslogDto zutrittslogDto)
			throws RemoteException, EJBExceptionLP;

	public ZutrittslogDto zutrittslogFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public boolean onlineCheck(String cAusweis, String pinMd5,
			Timestamp tZeitpunkt, Integer zutrittsobjektIId)
			throws EJBExceptionLP, RemoteException;

	public void kopiereRestlicheZutrittsmodelltage(Integer zutrittsmodelIId)
			throws EJBExceptionLP, RemoteException;

	public String getZutrittsEventsFuerMecs(Integer zutrittsobjektIId,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public Integer createZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws RemoteException,
			EJBExceptionLP;

	public void removeZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws RemoteException,
			EJBExceptionLP;

	public void updateZutrittdaueroffen(
			ZutrittdaueroffenDto zutrittdaueroffenDto) throws RemoteException,
			EJBExceptionLP;

	public String[] zutrittonlinecheckAusweiseFindByTGueltigab(Timestamp tDatum)
			throws EJBExceptionLP, RemoteException;

	public ZutrittdaueroffenDto zutrittdaueroffenFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public Integer createPersonalfinger(PersonalfingerDto personalfingerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removePersonalfinger(PersonalfingerDto personalfingerDto)
			throws EJBExceptionLP, RemoteException;

	public void updatePersonalfinger(PersonalfingerDto personalfingerDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PersonalfingerDto personalfingerFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public PersonalfingerDto[] personalfingerFindByTAendern(Timestamp tAendern,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public PersonalfingerDto[] personalfingerFindAll() throws EJBExceptionLP,
			RemoteException;

	public Integer createFingerart(FingerartDto fingerartDto)
			throws RemoteException, EJBExceptionLP;

	public void removeFingerart(Integer iId) throws RemoteException,
			EJBExceptionLP;

	public void removeFingerart(FingerartDto fingerartDto)
			throws RemoteException, EJBExceptionLP;

	public void updateFingerart(FingerartDto fingerartDto)
			throws RemoteException, EJBExceptionLP;

	public FingerartDto fingerartFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

}
