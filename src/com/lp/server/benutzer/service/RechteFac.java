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
package com.lp.server.benutzer.service;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface RechteFac {
	public Integer createRollerecht(RollerechtDto rollerechtDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void removeRollerecht(RollerechtDto rollerechtDto)
			throws RemoteException, EJBExceptionLP;

	public void updateRollerecht(RollerechtDto rollerechtDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public RollerechtDto rollerechtFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public RollerechtDto rollerechtFindBySystemrolleIIdRechtCNr(
			Integer systemrolleIId, String rechtCNr) throws RemoteException,
			EJBExceptionLP;

	public RollerechtDto rollerechtFindBySystemrolleIIdRechtCNrOhneExc(
			Integer systemrolleIId, String rechtCNr) throws RemoteException,
			EJBExceptionLP;

	public void createRecht(RechtDto rechtDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public void kopiereRechteEinerRolle(Integer systemrolleIIdQuelle,
			Integer systemrolleIIdZiel, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public void removeRecht(RechtDto rechtDto) throws RemoteException,
			EJBExceptionLP;

	public void updateRecht(RechtDto rechtDto, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public RechtDto rechtFindByPrimaryKey(String cNr) throws RemoteException,
			EJBExceptionLP;

	public static final String FLR_RECHT_X_KOMMENTAR = "x_kommentar";
	public static final String FLR_ROLLERECHT_FLRRECHT = "flrrecht";
	public static final String FLR_ROLLERECHT_FLRSYSTEMROLLE = "flrsystemrolle";

	// hatrecht: 1 hier zusaetzliches recht anfuegen
	public static final String RECHT_PERS_ANWESENHEITSLISTE_R = "PERS_ANWESENHEITSLISTE_R";
	public static final String RECHT_PERS_BENUTZER_R = "PERS_BENUTZER_R";
	public static final String RECHT_PERS_BENUTZER_CUD = "PERS_BENUTZER_CUD";
	public static final String RECHT_PERS_PERSONAL_R = "PERS_PERSONAL_R";
	public static final String RECHT_PERS_PERSONAL_CUD = "PERS_PERSONAL_CUD";
	public static final String RECHT_PERS_ZEITERFASSUNG_R = "PERS_ZEITERFASSUNG_R";
	public static final String RECHT_PERS_ZEITEREFASSUNG_CUD = "PERS_ZEITEREFASSUNG_CUD";
	public static final String RECHT_LP_HAUPTMANDANT_U = "LP_HAUPTMANDANT_U";
	public static final String RECHT_LP_SYSTEM_R = "LP_SYSTEM_R";
	public static final String RECHT_LP_SYSTEM_CUD = "LP_SYSTEM_CUD";
	public static final String RECHT_PART_PARTNER_R = "PART_PARTNER_R";
	public static final String RECHT_PART_PARTNER_CUD = "PART_PARTNER_CUD";
	public static final String RECHT_PART_KUNDE_R = "PART_KUNDE_R";
	public static final String RECHT_PART_KUNDE_CUD = "PART_KUNDE_CUD";
	public static final String RECHT_PART_LIEFERANT_R = "PART_LIEFERANT_R";
	public static final String RECHT_PART_LIEFERANT_CUD = "PART_LIEFERANT_CUD";
	public static final String RECHT_WW_ARTIKEL_R = "WW_ARTIKEL_R";
	public static final String RECHT_WW_ARTIKEL_CUD = "WW_ARTIKEL_CUD";
	public static final String RECHT_AUFT_AUFTRAG_R = "AUFT_AUFTRAG_R";
	public static final String RECHT_AUFT_AUFTRAG_CUD = "AUFT_AUFTRAG_CUD";
	public static final String RECHT_LS_LIEFERSCHEIN_R = "LS_LIEFERSCHEIN_R";
	public static final String RECHT_LS_LIEFERSCHEIN_CUD = "LS_LIEFERSCHEIN_CUD";
	public static final String RECHT_LS_LIEFERSCHEIN_VERSAND = "LS_LIEFERSCHEIN_VERSAND";
	public static final String RECHT_RECH_RECHNUNG_R = "RECH_RECHNUNG_R";
	public static final String RECHT_RECH_RECHNUNG_CUD = "RECH_RECHNUNG_CUD";
	public static final String RECHT_FB_FINANZ_R = "FB_FINANZ_R";
	public static final String RECHT_FB_FINANZ_CUD = "FB_FINANZ_CUD";
	public static final String RECHT_ANF_ANFRAGE_R = "ANF_ANFRAGE_R";
	public static final String RECHT_ANF_ANFRAGE_CUD = "ANF_ANFRAGE_CUD";
	public static final String RECHT_BES_BESTELLUNG_R = "BES_BESTELLUNG_R";
	public static final String RECHT_BES_BESTELLUNG_CUD = "BES_BESTELLUNG_CUD";
	public static final String RECHT_BES_WARENEINGANG_CUD = "BES_WARENEINGANG_CUD";
	public static final String RECHT_ER_EINGANGSRECHNUNG_R = "ER_EINGANGSRECHNUNG_R";
	public static final String RECHT_ER_EINGANGSRECHNUNG_CUD = "ER_EINGANGSRECHNUNG_CUD";
	public static final String RECHT_ANGB_ANGEBOT_R = "ANGB_ANGEBOT_R";
	public static final String RECHT_ANGB_ANGEBOT_CUD = "ANGB_ANGEBOT_CUD";
	public static final String RECHT_FERT_LOS_R = "FERT_LOS_R";
	public static final String RECHT_FERT_LOS_CUD = "FERT_LOS_CUD";
	public static final String RECHT_STK_STUECKLISTE_R = "STK_STUECKLISTE_R";
	public static final String RECHT_STK_STUECKLISTE_CUD = "STK_STUECKLISTE_CUD";
	public static final String RECHT_LP_ALLE_VERSANDAUFTRAEGE_R = "LP_ALLE_VERSANDAUFTRAEGE_R";
	public static final String RECHT_LP_ALLE_VERSANDAUFTRAEGE_UD = "LP_ALLE_VERSANDAUFTRAEGE_UD";
	public static final String RECHT_LP_MANDANT_ANLEGEN = "LP_MANDANT_ANLEGEN";
	public static final String RECHT_LP_DARF_VERSTECKTE_SEHEN = "LP_DARF_VERSTECKTE_SEHEN";
	public static final String RECHT_AS_ANGEBOTSTKL_R = "AS_ANGEBOTSTKL_R";
	public static final String RECHT_AS_ANGEBOTSTKL_CUD = "AS_ANGEBOTSTKL_CUD";
	public static final String RECHT_FB_DARF_EXPORT_ZURUECKNEHMEN = "FB_DARF_EXPORT_ZURUECKNEHMEN";
	public static final String RECHT_WW_DARF_LAGERPRUEFFUNKTIONEN_SEHEN = "WW_DARF_LAGERPRUEFFUNKTIONEN_SEHEN";
	public static final String RECHT_PERS_SICHTBARKEIT_ABTEILUNG = "PERS_SICHTBARKEIT_ABTEILUNG";
	public static final String RECHT_PERS_SICHTBARKEIT_ALLE = "PERS_SICHTBARKEIT_ALLE";
	public static final String RECHT_LP_EIGENSCHAFTSPANELS_BEARBEITEN = "LP_EIGENSCHAFTSPANELS_BEARBEITEN";
	public static final String RECHT_PERS_ZEITEINGABE_NUR_BUCHEN = "PERS_ZEITEINGABE_NUR_BUCHEN";
	public static final String RECHT_PROJ_PROJEKT_R = "PROJ_PROJEKT_R";
	public static final String RECHT_PROJ_PROJEKT_CUD = "PROJ_PROJEKT_CUD";
	public static final String RECHT_PERS_ZUTRITT_R = "PERS_ZUTRITT_R";
	public static final String RECHT_PERS_ZUTRITT_CUD = "PERS_ZUTRITT_CUD";
	public static final String RECHT_PERS_ZUTRITT_ONLINECHECK_CUD = "PERS_ZUTRITT_ONLINECHECK_CUD";
	public static final String RECHT_WW_ARTIKEL_GESTPREISE_CU = "WW_ARTIKEL_GESTPREISE_CU";
	public static final String RECHT_PERS_ZUTRITT_DAUEROFFEN_CRUD = "PERS_ZUTRITT_DAUEROFFEN_CRUD";
	public static final String RECHT_FB_CHEFBUCHHALTER = "FB_CHEFBUCHHALTER";
	public static final String RECHT_PERS_ZEITERFASSUNG_ZEITMODELL_TAGEWEISE_AENDERN = "PERS_ZEITERFASSUNG_ZEITMODELL_TAGEWEISE_AENDERN";
	public static final String RECHT_PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN = "PERS_ZEITERFASSUNG_MONATSABRECHNUNG_DRUCKEN";
	public static final String RECHT_FERT_ABLIEFERUNG_UNTER_SOLLSATZGROESSE_ERLAUBT = "FERT_ABLIEFERUNG_UNTER_SOLLSATZGROESSE_ERLAUBT";
	public static final String RECHT_FERT_TECHNIKER_BEARBEITEN = "FERT_TECHNIKER_BEARBEITEN_ERLAUBT";
	
	public static final String RECHT_PART_PARTNER_ZUSAMMENFUEHREN_ERLAUBT = "PART_PARTNER_ZUSAMMENFUEHREN_ERLAUBT";
	public static final String RECHT_PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT = "PART_KUNDE_ZUSAMMENFUEHREN_ERLAUBT";
	public static final String RECHT_PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT = "PART_LIEFERANT_ZUSAMMENFUEHREN_ERLAUBT";
	public static final String RECHT_PERS_PERSONAL_ZUSAMMENFUEHREN_ERLAUBT = "PERS_PERSONAL_ZUSAMMENFUEHREN_ERLAUBT";
	public static final String RECHT_PART_BANK_ZUSAMMENFUEHREN_ERLAUBT = "PART_BANK_ZUSAMMENFUEHREN_ERLAUBT";

	public static final String RECHT_LP_DARF_RECHTE_SEHEN = "LP_DARF_RECHTE_SEHEN";
	public static final String RECHT_LP_DARF_PREISE_SEHEN_EINKAUF = "LP_DARF_PREISE_SEHEN_EINKAUF";
	public static final String RECHT_LP_DARF_PREISE_SEHEN_VERKAUF = "LP_DARF_PREISE_SEHEN_VERKAUF";

	public static final String RECHT_REKLA_REKLAMATION_R = "REKLA_REKLAMATION_R";
	public static final String RECHT_REKLA_REKLAMATION_CUD = "REKLA_REKLAMATION_CUD";
	public static final String RECHT_FERT_DARF_LOS_ERLEDIGEN = "FERT_DARF_LOS_ERLEDIGEN";
	public static final String RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN = "AUFT_DARF_AUFTRAG_ERLEDIGEN";
	public static final String RECHT_MODULWEIT_READ = "MODULWEIT_READ";
	public static final String RECHT_MODULWEIT_UPDATE = "MODULWEIT_UPDATE";
	public static final String RECHT_PERS_ZEITERFASSUNG_JOURNALE_DRUCKEN = "PERS_ZEITERFASSUNG_REPORTS_DRUCKEN";

	public static final String RECHT_LP_DARF_GRUNDDATEN_SEHEN = "LP_DARF_GRUNDDATEN_SEHEN";

	public static final String RECHT_LP_DARF_PREISE_AENDERN_EINKAUF = "LP_DARF_PREISE_AENDERN_EINKAUF";
	public static final String RECHT_LP_DARF_PREISE_AENDERN_VERKAUF = "LP_DARF_PREISE_AENDERN_VERKAUF";

	public static final String RECHT_WW_HANDLAGERBEWEGUNG_CUD = "WW_HANDLAGERBEWEGUNG_CUD";
	public static final String RECHT_FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN = "FERT_DARF_FEHLMENGEN_PER_DIALOG_AUFLOESEN";
	
	public static final String RECHT_FERT_DARF_SOLLMATERIAL_CUD = "FERT_DARF_SOLLMATERIAL_CUD";
	
	public static final String RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU = "DOKUMENTE_SICHERHEITSSTUFE_0_CU";
	public static final String RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU = "DOKUMENTE_SICHERHEITSSTUFE_1_CU";
	public static final String RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU = "DOKUMENTE_SICHERHEITSSTUFE_2_CU";
	public static final String RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU = "DOKUMENTE_SICHERHEITSSTUFE_3_CU";
	public static final String RECHT_DOKUMENTE_SICHERHEITSSTUFE_99_CU = "DOKUMENTE_SICHERHEITSSTUFE_99_CU";
	
	public static final String RECHT_REKLA_QUALITAETSSICHERUNG_CUD = "REKLA_QUALITAETSSICHERUNG_CUD";
	
	public static final String RECHT_DOKUMENTE_DARF_DOKUMENTE_GROESSER_ALS_MAX_ZULAESSIG_SPEICHERN = "DOKUMENTE_DARF_DOKUMENTE_GROESSER_ALS_MAX_ZULAESSIG_SPEICHERN";
	
	
	public static final String RECHT_KUE_KUECHE_R = "KUE_KUECHE_R";
	public static final String RECHT_KUE_KUECHE_CUD = "KUE_KUECHE_CUD";
	public static final String RECHT_IS_INSTANDHALTUNG_R = "IS_INSTANDHALTUNG_R";
	public static final String RECHT_IS_INSTANDHALTUNG_CUD = "IS_INSTANDHALTUNG_CUD";
	
	public static final String RECHT_FERT_TERMINE_VERSCHIEBEN = "FERT_TERMINE_VERSCHIEBEN";
	
	public static final String RECHT_AUFT_AKTIVIEREN = "AUFT_AKTIVIEREN";
	public static final String RECHT_ANGB_AKTIVIEREN = "ANGB_AKTIVIEREN";
	public static final String RECHT_PERS_DARF_KOMMT_GEHT_AENDERN = "PERS_DARF_KOMMT_GEHT_AENDERN";
	public static final String RECHT_PERS_DARF_PERSONALDETAILDATEN_SEHEN = "PERS_DARF_PERSONALDETAILDATEN_SEHEN";
	public static final String RECHT_PERS_ZEITERFASSUNG_TECHNIKERAUSWAHL = "PERS_ZEITERFASSUNG_TECHNIKERAUSWAHL";
	public static final String RECHT_PART_KURZBRIEF_CUD = "PART_KURZBRIEF_CUD";
	
	public static final String RECHT_FERT_LOS_DARF_ABLIEFERN = "FERT_LOS_DARF_ABLIEFERN";
	public static final String RECHT_FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN = "FERT_LOS_DARF_ISTMATERIAL_MANUELL_NACHBUCHEN";
	public static final String RECHT_LP_FINANCIAL_INFO_TYP_1 = "LP_FINANCIAL_INFO_TYP_1";
	public static final String RECHT_FERT_LOS_SCHNELLANLAGE = "FERT_LOS_SCHNELLANLAGE";
	
	public static final String RECHT_IV_INSERAT_R = "IV_INSERAT_R";
	public static final String RECHT_IV_INSERAT_CUD = "IV_INSERAT_CUD";

	public static final String RECHT_WW_LAGERCOCKPIT = "WW_LAGERCOCKPIT";	
}
