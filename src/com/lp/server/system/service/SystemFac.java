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
package com.lp.server.system.service;

import java.awt.Font;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Remote;

import net.sf.jasperreports.engine.JasperReport;

import com.lp.server.finanz.service.BelegbuchungDto;
import com.lp.service.BelegVerkaufDto;
import com.lp.service.BelegpositionVerkaufDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface SystemFac {

	// ofxml:1 Hier ist das letzt gueltige OpenFactory-OrderPosition-Schema.
	// Schema
	// <xs:element name="Positions">
	// <xs:annotation>
	// <xs:documentation>Die Positionen</xs:documentation>
	// </xs:annotation>
	// <xs:complexType>
	// <xs:sequence maxOccurs="unbounded">
	// <xs:element name="Position">
	// <xs:complexType>
	// <xs:sequence>
	// <xs:element name="PositionIDCustomer" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Die eindeutige ID der Position beim
	// Kunden</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="PositionIDSupplier" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Die eindeutige ID der Position beim
	// Lieferanten</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Item" type="ItemType">
	// <xs:annotation>
	// <xs:documentation>Angaben zum Artikel (ItemType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Quantity" type="QuantityType">
	// <xs:annotation>
	// <xs:documentation>Mengenangaben (QuantityType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="RequestedDelivery" type="RequestedDeliveryType">
	// <xs:annotation>
	// <xs:documentation>Angaben zum vereinbarten Liefertermin
	// (RequestedDeliveryType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Price" type="PriceType">
	// <xs:annotation>
	// <xs:documentation>Preisangaben (PriceType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Features" type="FeaturesType" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Zus&aumltzliche Merkmale: Varianten, Verpackungs- und
	// Transportvorschriften, etc. (FeaturesType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Note" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Anmerkungen</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:sequence>
	// </xs:complexType>
	// </xs:element>
	// </xs:sequence>
	// </xs:complexType>
	// </xs:element>
	
	public static class VersandwegType {
		public final static String CleverCureVerkauf = "CC" ;
	}
	
	public static final String SCHEMA_OF_POSITIONS = "Positions";
	public static final String SCHEMA_OF_POSITION = "Position";
	public static final String SCHEMA_OF_FEATURES = "Features";
	public static final String SCHEMA_OF_ITEM = "Item";
	public static final String SCHEMA_OF_NOTE = "Note";
	public static final String SCHEMA_OF_POSITION_ID_CUSTOMER = "PositionIDCustomer";
	public static final String SCHEMA_OF_POSITION_ID_SUPPLIER = "PositionIDSupplier";
	public static final String SCHEMA_OF_PRICE = "Price";
	public static final String SCHEMA_OF_QUANTITY = "Quantity";
	public static final String SCHEMA_OF_REQUESTED_DELIVERY = "RequestedDelivery";

	//FeatureType***************************************************************
	// **
	// <xs:complexType name="FeatureType">
	// <xs:annotation>
	// <xs:documentation>Merkmal</xs:documentation>
	// </xs:annotation>
	// <xs:all>
	// <xs:element name="Description" type="xs:string">
	// <xs:annotation>
	// <xs:documentation>Beschreibung des Merkmals. Sollte immer gefuellt werden
	// um Bearbeiter das Auslesen zu erleichtern</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="FeatureID" type="xs:string">
	// <xs:annotation>
	// <xs:documentation>Name des Merkmals</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Value" type="xs:string">
	// <xs:annotation>
	// <xs:documentation>Wert des Merkmals</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Unit" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Einheit des Merkmals</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="ClassID" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Klasse des Merkmals </xs:documentation>
	// </xs:annotation>
	// <xs:simpleType>
	// <xs:restriction base="xs:string">
	// <xs:enumeration value="OF_EDI_Ext"/>
	// <xs:enumeration value="eclass"/>
	// </xs:restriction>
	// </xs:simpleType>
	// </xs:element>
	// <xs:element name="Reference" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Dieses Merkmal bezieht sich auf die angegebene
	// Referenz. Hiermit k&ouml;nnen Hierarchien abgebildet
	// werden.</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:all>
	// </xs:complexType>
	final static public String SCHEMA_OF_FEATURE = "Feature";
	final static public String SCHEMA_OF_FEATURE_CLASSID = "ClassID";
	final static public String SCHEMA_OF_FEATURE_DESCRIPTION = "Description";
	final static public String SCHEMA_OF_FEATURE_FEATUREID = "FeatureID";
	final static public String SCHEMA_OF_FEATURE_REFERENCE = "Reference";
	final static public String SCHEMA_OF_FEATURE_UNIT = "Unit";
	final static public String SCHEMA_OF_FEATURE_VALUE = "Value";

	//QuantityType**************************************************************
	// **
	// <xs:complexType name="QuantityType">
	// <xs:annotation>
	// <xs:documentation>Menge</xs:documentation>
	// </xs:annotation>
	// <xs:sequence>
	// <xs:element name="QuantityCustomer" type="xs:double">
	// <xs:annotation>
	// <xs:documentation>Die bestellte Menge in der Mengeneinheit des Kunden
	// (z.B. "4" Stk.). Wenn 0 angegeben wird, wird die Position
	// stoniert.</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="UnitCustomer" type="xs:string">
	// <xs:annotation>
	// <xs:documentation>Mengeneinheit des Kunden f&uuml;r diesen Artikel (z.B.
	// "PCE"=Stk.)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="QuantitySupplier" type="xs:double" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Die bestellte Menge in der Mengeneinheit des
	// Lieferanten, wenn diese von der Mengeneinheit des Kunden abweicht (z.B.
	// "40" m also 4 Stk. zu je 10m)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="UnitSupplier" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Mengeneinheit des Lieferanten f&uuml;r diesen Artikel, wenn
	// diese von der Mengeneinheit des Kunden abweicht (z.B.
	// "MTR"=m)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:sequence>
	// </xs:complexType>
	public static final String SCHEMA_OF_QUANTITY_CUSTOMER = "QuantityCustomer";
	public static final String SCHEMA_OF_QUANTITY_UNIT_CUSTOMER = "UnitCustomer";
	public static final String SCHEMA_OF_QUANTITY_SUPPLIER = "QuantitySupplier";
	public static final String SCHEMA_OF_QUANTITY_UNIT_SUPPLIER = "UnitSupplier";

	//RequestedDeliveryType*****************************************************
	// **
	// <xs:complexType name="RequestedDeliveryType">
	// <xs:annotation>
	// <xs:documentation>Lieferdatum</xs:documentation>
	// </xs:annotation>
	// <xs:all>
	// <xs:element name="FirstDate" type="xs:dateTime" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Erster m&ouml;glicher Liefertermin (wenn nichts angegeben
	// wird, ist eine Anlieferung ab heute m&ouml;glich)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="RequestedDate" type="xs:dateTime" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Wunschtermin f&uuml;r die Lieferung</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="LastDate" type="xs:dateTime">
	// <xs:annotation>
	// <xs:documentation>Letzter m&ouml;glicher Liefertermin (inklusive des Tags)
	// f&uuml;r Kunde und Lieferant verbindlich</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="PartialShipment" type="xs:boolean" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Teilieferungen zwischen dem ersten und dem letzten
	// Liefertermin sind zul&auml;ssig</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="MonitoringDays" type="xs:positiveInteger"
	// minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Statusinformation zu dieser Position im Abstand der
	// angegebenen Tage automatisch vom Lieferanten an den Kunden
	// senden</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:all>
	// </xs:complexType>
	public static final String SCHEMA_OF_REQUESTEDDELIVERY_FIRST_DATE = "FirstDate";
	public static final String SCHEMA_OF_REQUESTEDDELIVERY_REQUESTED_DATE = "RequestedDate";
	public static final String SCHEMA_OF_REQUESTEDDELIVERY_PARTIAL_SHIPMENT = "PartialShipment";
	public static final String SCHEMA_OF_REQUESTEDDELIVERY_MONITORING_DAYS = "MonitoringDays";

	//PriceType*****************************************************************
	// **
	// <xs:complexType name="PriceType">
	// <xs:annotation>
	// <xs:documentation>Preis</xs:documentation>
	// </xs:annotation>
	// <xs:all>
	// <xs:element name="BasePrice" type="xs:double">
	// <xs:annotation>
	// <xs:documentation>Basispreis pro Preiseinheit (z.B. "8"
	// Euro)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="BaseQuantity" type="xs:double" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Die Menge auf die sich der Basispreis bezieht (z.B. pro
	// 100 Stk.). Als Mengeneinheit wird die Einheit des Lieferanten
	// (UnitSupplier) zu Grunde gelegt, wenn sie von der Mengeneinheit des
	// Kunden abweicht, sonst die Mengeneinheit des Kunden (UnitCustomer, z.B.
	// "5" m)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Discounts" type="DiscountsType" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Rabatte und Zuschl&auml;ge
	// (DiscountsType)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Value" type="xs:double">
	// <xs:annotation>
	// <xs:documentation>Der Nettowarenwert (alle Rabatte und Zuschl&auml;ge aber
	// ohne Mwst.) dieser Position, der sich aus Basispreis * Menge und den
	// Discounts berechnet. Dieser Wert ist redundant, dient aber dem Ausschluss
	// von Rundungsfehlern (z.B. "64" Euro = 40m/5m*8Euro)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="VAT" type="xs:double" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Mehrwertsteuer als Prozentsatz</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Contract" type="xs:string" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Bezeichnung des Rahmenvertrags in dessen Rahmen die
	// Position faellt</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:all>
	// </xs:complexType>
	public static final String SCHEMA_OF_PRICE_BASEPRICE = "BasePrice";
	public static final String SCHEMA_OF_PRICE_BASEQUANTITY = "BaseQuantity";
	public static final String SCHEMA_OF_PRICE_DISCOUNTS = "Discounts";
	public static final String SCHEMA_OF_PRICE_VALUE = "Value";
	public static final String SCHEMA_OF_PRICE_VAT = "VAT";
	public static final String SCHEMA_OF_PRICE_CONTRACT = "Contract";

	//DiscountType**************************************************************
	// **
	// <xs:complexType name="DiscountType">
	// <xs:annotation>
	// <xs:documentation>Rabatt oder Zuschlag</xs:documentation>
	// </xs:annotation>
	// <xs:all>
	// <xs:element name="Description" type="xs:string">
	// <xs:annotation>
	// <xs:documentation>Beschreibung des Rabatts bzw.
	// Zuschlags</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Sequence" type="xs:integer" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Nummer zur Bestimmung der Reihenfolge f&uuml;r Discounts,
	// die auf den Restwert bezogen sind</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Percent" type="xs:double" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Prozentwert (ungleich 0 wenn es kein absoluter Wert
	// ist). Dieser Wert ist rein informativ, relevant ist nur der Wert des
	// Zuschlags (Value)</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Rest" type="xs:boolean" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Wenn "true" wird der Prozentsatz auf den Restwert
	// bezogen. Der Restwert entsteht indem alle Discounts mit Rest="false"
	// zun&auml;chst vom Preis abgezogen werden. Bei der Berechnung der Rabatte oder
	// Zuschl&auml;ge muss auf die Sequence geachtet werden</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Daily" type="xs:boolean" minOccurs="0">
	// <xs:annotation>
	// <xs:documentation>Wenn "true" bedeutet dies, dass der Wert tagesabh&auml;ngig
	// berechnet wird z.B. Kupferzuschlag</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// <xs:element name="Value" type="xs:double">
	// <xs:annotation>
	// <xs:documentation>Wert des Rabatts (negativ) bzw. Wert des Zuschlages
	// (positiv). Dieser Wert ist bei Abweichungen bindender als der Wert im
	// Feld Percent</xs:documentation>
	// </xs:annotation>
	// </xs:element>
	// </xs:all>
	// </xs:complexType>
	final static public String SCHEMA_OF_DISCOUNT = "Discount";
	final static public String SCHEMA_OF_DISCOUNTTYPE_DESCRIPTION = "Description";
	final static public String SCHEMA_OF_DISCOUNTTYPE_SEQUENCE = "Sequence";
	final static public String SCHEMA_OF_DISCOUNTTYPE_PERCENT = "Percent";
	final static public String SCHEMA_OF_DISCOUNTTYPE_REST = "Rest";
	final static public String SCHEMA_OF_DISCOUNTTYPE_DAILY = "Daily";
	final static public String SCHEMA_OF_DISCOUNTTYPE_VALUE = "Value";

	// HV-Belegpos.-Felder die alle Bewegungsmodule
	// teilen*************************
	final static public String SCHEMA_HV_FEATURE_ARTIKELBEZEICHNUNGUEBERSTEUERT = "Artikelbezeichnunguebersteuert";
	final static public String SCHEMA_HV_FEATURE_BELEGPOS_I_ID = "iId";
	final static public String SCHEMA_HV_FEATURE_I_ID = "iId";
	final static public String SCHEMA_HV_FEATURE_BELEG_I_ID = "belegIId";
	final static public String SCHEMA_HV_FEATURE_ISORT = "iSort";
	final static public String SCHEMA_HV_FEATURE_POSITIONSART_C_NR = "positionsartCNr";
	final static public String SCHEMA_HV_FEATURE_C_BEZ = "cBez";
	final static public String SCHEMA_HV_FEATURE_C_ZUSATZBEZ = "cZusatzbez";
	final static public String SCHEMA_HV_FEATURE_XTEXTINHALT = "xTextinhalt";
	final static public String SCHEMA_HV_FEATURE_MEDIASTANDARD_I_ID = "mediastandardIId";
	final static public String SCHEMA_HV_FEATURE_ARTIKEL_I_ID = "artikelIId";
	final static public String SCHEMA_HV_FEATURE_EINHEIT_C_NR = "einheitCNr";

	//
	public final static String FILE_NAME_ABBOT_AKTIVIERUNG_AM_CLIENT = "abbot_ist_aktiviert.tmp";

	// expirehv: 0 ab 17 steht das expireddate.
	public final static int MAX_LAENGE_HV_DB_VERSION = 17;

	public final static int MAX_LAENGE_EDITORTEXT = 3000;
	public final static int MAX_LAENGE_EDITORTEXT_WENN_NTEXT = 100000;

	public final static int MAX_LAENGE_EXTRALISTE_BEZEICHNUNG = 30;
	// flrconst2:
	// FLR Spaltennamen aus Hibernate Mapping - allgemein LP-weit.
	// i_id und c_nr sind fix stehende Entitaeten; diese werden niemals
	// geaendert!
	// daher werden sie auch direkt so ("i_id" und "c_nr") verwendet!
	public static final String FLR_LP_VERSTECKT = "b_versteckt";

	// FLR Belegart.
	public static final String FLR_BELEGART_I_STANDARDERLEDIGUNGSZEITINTAGEN = "i_standarderledigungszeitintagen";
	public static final String FLR_BELEGART_BELEGARTSPR_SET = "belegart_belegart_set";
	public static final String FLR_BELEGART_C_KBEZ = "c_kbez";

	// FLR.
	public static final String FLR_LP_ORTID = "i_id";
	public static final String FLR_LP_FLRORT = "flrort";
	public static final String FLR_LP_ORTNAME = "c_name";
	public static final String FLR_LP_LKZ = "land_c_lkz";

	// FLR Zahlungziel
	public static final String FLR_I_ANZAHLZIELTAGEFUERNETTO = "anzahlzieltagefuernetto";
	public static final String FLR_N_SKONTOPROZENTSATZ1 = "skontoprozentsatz1";
	public static final String FLR_I_SKONTOANZAHLTAGE1 = "anzahltage1";
	public static final String FLR_N_SKONTOPROZENTSATZ2 = "skontoprozentsatz2";
	public static final String FLR_I_SKONTOANZAHLTAGE2 = "anzahltage2";
	public static final String FLR_C_BEZ = "c_bez";

	public static final String FLR_KOSTENSTELLE_C_BEZ = "c_bez";
	public static final String FLR_KOSTENSTELLE_B_VERSTECKT = "b_versteckt";

	public static final String FLR_C_NAMEDESSPEDITEURS = "c_namedesspediteurs";
	
	public static final String FLR_DOKUMENTENLINK_C_ORDNER = "c_ordner";
	public static final String FLR_DOKUMENTENLINK_BELEGART_C_NR = "belegart_c_nr";
	public static final String FLR_DOKUMENTENLINK_C_BASISPFAD = "c_basispfad";
	public static final String FLR_DOKUMENTENLINK_C_MENUETEXT = "c_menuetext";
	public static final String FLR_DOKUMENTENLINK_B_PFADABSOLUT = "b_pfadabsolut";
	

	public static final String FLR_SPEDITEUR_B_VERSTECKT = "b_versteckt";
	public static final String FLR_LIEFERART_B_VERSTECKT = "b_versteckt";
	public static final String FLR_ZAHLUNGSZIEL_B_VERSTECKT = "b_versteckt";

	public static final String FLR_LP_LANDID = "i_id";
	public static final String FLR_LP_FLRLAND = "flrland";
	public static final String FLR_LP_LANDNAME = "c_name";
	public static final String FLR_LP_LANDLKZ = "c_lkz";
	public static final String FLR_LP_TELVORWAHL = "c_telvorwahl";
	public static final String FLR_LP_WAEHRUNG = "waehrung_c_nr";

	public static final String FLR_LP_LANDPLZORTID = "i_id";
	public static final String FLR_LP_LANDPLZORTPLZ = "c_plz";

	// Fuer Direktfilter Plz ODER Ort
	public static final String FLR_LP_LANDPLZORT_ORTPLZ = "ORTPLZ";

	public static final String FLR_LP_CNR = "c_nr";
	public static final String FLR_LP_BEZEICHNUNG = "c_bezeichnung";
	public static final String FLR_LP_BASISEINHEIT = "eht_c_basiseht";
	public static final String FLR_LP_FAKTOR = "f_faktor";

	public static final String FLR_LP_EINHEITKONVERTIERUNG_FAKTOR = "n_faktor";
	public static final String FLR_LP_EINHEITKONVERTIERUNG_VON = "einheit_cnr_von";
	public static final String FLR_LP_EINHEITKONVERTIERUNG_ZU = "einheit_cnr_zu";
	public static final String FLT_EINHEIT = "flreinheit";

	// FLR Mwssatz
	public static final String FLR_F_MWSTSATZ = "f_mwstsatz";

	// FLR Waehrungen
	public static final String FLR_WAEHRUNG_C_NR = "c_nr";
	public static final String FLR_WAEHRUNG_C_KOMMENTAR = "c_kommentar";

	// FLR Wechselkurse
	public static final String FLR_WECHSELKURS_ID_COMP = "id_comp";
	public static final String FLR_WECHSELKURS_WAEHRUNG_C_NR_VON = "id_comp.waehrung_c_nr_von";
	public static final String FLR_WECHSELKURS_WAEHRUNG_C_NR_ZU = "id_comp.waehrung_c_nr_zu";
	public static final String FLR_WECHSELKURS_T_DATUM = "id_comp.t_datum";
	public static final String FLR_WECHSELKURS_N_KURS = "n_kurs";

	// KFZ Kennzeichen
	public static final String FLR_LAND_KFZKENNZEICHEN_C_LKZ = "c_lkz";
	public static final String FLR_LAND_KFZKENNZEICHEN_C_KFZKENNZEICHEN = "c_kfzkennzeichen";
	// EXTRALISTE
	public static final String FLR_EXTRALISTE_BELEGART_C_NR = "belegart_c_nr";

	public static final String FLR_ARBEITSPLATZ_C_PCNAME = "c_pcname";
	public static final String FLR_ARBEITSPLATZ_C_STANDORT = "c_standort";

	public static final String FLR_PARAMETER_C_BEMERKUNG = "c_bemerkung";
	public static final String FLR_PARAMETER_C_DATENTYP = "c_datentyp";

	public static final String FLR_ARBEITSPLATZPARAMETER_ARBEITSPLATZ_I_ID = "arbeitsplatz_i_id";
	public static final String FLR_ARBEITSPLATZPARAMETER_PARAMETER_C_NR = "parameter_c_nr";
	public static final String FLR_ARBEITSPLATZPARAMETER_C_WERT = "c_wert";
	public static final String FLR_ARBEITSPLATZPARAMETER_FLRARBEITSPLATZ = "flrarbeitsplatz";
	public static final String FLR_ARBEITSPLATZPARAMETER_FLRPARAMETER = "flrparameter";

	// HauptmandantPK
	public static final int PK_HAUPTMANDANT_IN_LP_ANWENDER = 1;

	// Feldlaengen
	public static final int MAX_LKZ = 3;
	public static final int MAX_PLZ = 10;
	public static final int MAX_LAND = 50;
	public static final int MAX_ORT = 40;
	public static final int MAX_VORWAHL = 15;
	public static final int MAX_WAEHRUNG = 3;
	public static final int MAX_KOSTENSTELLE_CNR = 15;
	public static final int MAX_KOSTENSTELLE_BEZEICHNUNG = 40;
	public static final double MIN_F_PROZENT = -999.99;
	public static final double MAX_F_PROZENT = 999.99;
	public static final double MIN_N_NUMBER = -99999999999.9999;
	public static final double MAX_N_NUMBER = 99999999999.9999;

	// Einheiten
	public static final String EINHEIT_ZENTIMETER = "cm             ";
	public static final String EINHEIT_TAG = "d              ";
	public static final String EINHEIT_DEKAGRAMM = "dag            ";
	public static final String EINHEIT_DEZIMETER = "dm             ";
	public static final String EINHEIT_GRAMM = "g              ";
	public static final String EINHEIT_STUNDE = "h              ";
	public static final String EINHEIT_JAHR = "a              ";
	public static final String EINHEIT_KILOGRAMM = "kg             ";
	public static final String EINHEIT_LITER = "l              ";
	public static final String EINHEIT_METER = "m              ";
	public static final String EINHEIT_QUADRATMETER = "m\u00B2             ";
	public static final String EINHEIT_KUBIKMETER = "m\u00B3             ";
	public static final String EINHEIT_MINUTE = "min            ";
	public static final String EINHEIT_MILLIMETER = "mm             ";
	public static final String EINHEIT_QUADRATMILLIMETER = "mm\u00B2            ";
	public static final String EINHEIT_KILOMETER = "km             ";
	public static final String EINHEIT_MONAT = "mon            ";
	public static final String EINHEIT_SEKUNDE = "s              ";
	public static final String EINHEIT_STUECK = "Stk            ";
	public static final String EINHEIT_WOCHE = "w              ";

	public static final String EINHEIT_MEILEN = "mi             ";
	public static final String EINHEIT_ZOLL = "in             ";
	public static final String EINHEIT_FUSS = "ft             ";
	public static final String EINHEIT_YARD = "yd             ";
	public static final String EINHEIT_PFUND = "lb             ";
	public static final String EINHEIT_UNZE = "oz             ";

	public static final String LKZ_OESTERREICH = "A";

	public final static String REPORT_DREISPALTER = "dreispalter.jasper";
	public final static String REPORTXML_FLRDRUCK = "flrdruck.jrxml";

	public final static String PROTOKOLL_ART_INFO = "INFO";
	public final static String PROTOKOLL_ART_WARNING = "WARNUNG";
	public final static String PROTOKOLL_ART_FEHLER = "FEHLER";

	
	public final static String PROTOKOLL_TYP_KASSA_OSCAR = "KASSA_OSCAR";
	public final static String PROTOKOLL_TYP_KASSA_ADS3000 = "KASSA_ADS3000";
	public final static String PROTOKOLL_TYP_LAGER_ZU = "LAGER_ZU";
	public final static String PROTOKOLL_TYP_LAGER_AB = "LAGER_AB";
	public final static String PROTOKOLL_TYP_UPDATE_GESTPREIS = "UPDATE_GESTPREIS";
	public final static String PROTOKOLL_TYP_PRUEFE_RECHNUNGSWERT = "PRUEFE_RECHNUNGSWERT";
	public final static String PROTOKOLL_TYP_BES = "BES" ;
	
	public final static String HARDWAREART_WIN_TERMINAL = "WIN-Terminal";
	public final static String HARDWAREART_CE_TERMINAL = "CE-Terminal";
	
	/**
	 * @todo uppercase und final
	 */
	public final static int I_ANZAHL_TAGE_PRO_WOCHE = 7;
	public final static int I_ANZAHL_STUNDEN_PRO_TAG = 24;
	public final static int I_ANZAHL_STUNDEN_PRO_WOCHE = I_ANZAHL_TAGE_PRO_WOCHE
			* I_ANZAHL_STUNDEN_PRO_TAG;

	public String getHauptmandant();

	public Integer createLand(LandDto landDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void removeLand(LandDto landDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLand(LandDto landDto) throws EJBExceptionLP,
			RemoteException;

	public LandDto landFindByPrimaryKey(Integer iIdI);

	public LandDto landFindByLkz(String cLkz) throws EJBExceptionLP,
			RemoteException;

	public Integer createLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateLandplzort(LandplzortDto landplzortDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public LandplzortDto landplzortFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP, RemoteException;

	public Integer createOrt(OrtDto ortDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public ExtralisteRueckgabeTabelleDto generiereExtraliste(
			Integer extralisteIId, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;
	public byte[][] konvertierePDFFileInEinzelneBilder(String pdfFile,
			int resolution) throws Throwable;
	public byte[][] konvertierePDFFileInEinzelneBilder(byte[] pdf,
			int resolution);
	public void removeOrt(OrtDto ortDto) throws EJBExceptionLP, RemoteException;

	public void updateOrt(OrtDto ortDtoI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public OrtDto ortFindByPrimaryKey(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public String createEinheit(EinheitDto einheitDtoI,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeEinheit(String cNr) throws EJBExceptionLP,
			RemoteException;

	public void removeEinheit(EinheitDto einheitDto) throws EJBExceptionLP,
			RemoteException;

	public void updateEinheit(EinheitDto einheitDto, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public EinheitDto einheitFindByPrimaryKey(String cNr,
			TheClientDto theClientDtoI) throws EJBExceptionLP, RemoteException;

	public EinheitDto einheitFindByPrimaryKeyOhneExc(String cNr,
			TheClientDto theClientDtoI) throws EJBExceptionLP, RemoteException ;
	
	public EinheitDto[] einheitFindAll() throws EJBExceptionLP, RemoteException;

	public JasperReport getDreispalter(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllEinheit(TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllGeschaeftsjahr(String mandantCnr) throws EJBExceptionLP,
			RemoteException;

	public Integer createKostenstelle(KostenstelleDto kostenstelleDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeKostenstelle(Integer iId) throws EJBExceptionLP,
			RemoteException;

	public void removeKostenstelle(KostenstelleDto kostenstelleDto)
			throws EJBExceptionLP, RemoteException;

	public void updateKostenstelle(KostenstelleDto kostenstelleDto)
			throws EJBExceptionLP, RemoteException;

	public KostenstelleDto kostenstelleFindByPrimaryKey(Integer iId)
			throws EJBExceptionLP;

	public KostenstelleDto[] kostenstelleFindByMandant(String pMandant)
			throws EJBExceptionLP, RemoteException;

	public KostenstelleDto kostenstelleFindByNummerMandant(String cNr,
			String mandantCNr) throws EJBExceptionLP, RemoteException;

	public void createGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP, RemoteException;

	public void removeGeschaeftsjahr(Integer iGeschaeftsjahr, String mandantCnr)
			throws EJBExceptionLP, RemoteException;

	public void removeGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP, RemoteException;

	public void updateGeschaeftsjahr(GeschaeftsjahrMandantDto geschaeftsjahrDto)
			throws EJBExceptionLP, RemoteException;

	public void updateGeschaeftsjahrs(GeschaeftsjahrMandantDto[] geschaeftsjahrDtos)
			throws EJBExceptionLP, RemoteException;

	public GeschaeftsjahrMandantDto geschaeftsjahrFindByPrimaryKey(
			Integer iGeschaeftsjahr, String mandantCnr) throws EJBExceptionLP, RemoteException;

	public GeschaeftsjahrMandantDto geschaeftsjahrFindByPrimaryKeyOhneExc(
			Integer iGeschaeftsjahr, String mandantCnr) throws EJBExceptionLP, RemoteException;

	public Integer createAnwender(AnwenderDto anwenderDto)
			throws EJBExceptionLP, RemoteException;

	public void removeAnwender() throws EJBExceptionLP, RemoteException;

	public void removeAnwender(AnwenderDto anwenderDto) throws EJBExceptionLP,
			RemoteException;

	public void updateAnwender(AnwenderDto anwenderDto) throws EJBExceptionLP,
			RemoteException;

	public void updateAnwenders(AnwenderDto[] anwenderDtos)
			throws EJBExceptionLP, RemoteException;

	public AnwenderDto anwenderFindByPrimaryKey(Integer iIdI)
			throws EJBExceptionLP, RemoteException;

	public OrtDto ortFindByName(String cName) throws EJBExceptionLP,
			RemoteException;

	public Integer createEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void updateEinheitkonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public void removeEinheitKonvertierung(
			EinheitKonvertierungDto einheitKonvertierungDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public EinheitKonvertierungDto einheitKonvertierungFindByPrimaryKey(
			Integer iId, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public BigDecimal pruefeEinheitKonvertierungViceVersa(String cNrVon,
			String cNrZu, TheClientDto theClientDto) throws EJBExceptionLP,
			RemoteException;

	public EinheitKonvertierungDto[] getAllEinheitKonvertierungen()
			throws EJBExceptionLP, RemoteException;

	public Map<?, ?> getAllEinheitKonvertierungMitEinheitVonUndEinheitZu(
			String einheitVon, String einheitZu) throws RemoteException;

	public void createEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void removeEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public void updateEinheitspr(EinheitsprDto einheitsprDto,
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;

	public EinheitsprDto einheitsprFindByPrimaryKey(String einheitCNr,
			String localeCNr, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public EinheitsprDto[] einheitsprFindByEinheitCNr(String einheitCNr)
			throws RemoteException, EJBExceptionLP;

	public Font[] getFontlist(TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP;

	public Timestamp getServerTimestamp() throws RemoteException;

	public void executeUpdateHibernateQuery(String[] sQuery)
			throws RemoteException;

	public BigDecimal rechneUmInAndereEinheit(BigDecimal bdMengeI,
			String cEinheitVonI, String cEinheitNachI,
			Integer stuecklsitepositionIId, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void createLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP,
			RemoteException;

	public void removeLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLandkfzkennzeichen(
			LandkfzkennzeichenDto landkfzkennzeichenDto) throws EJBExceptionLP,
			RemoteException;

	public void updateLandkfzkennzeichens(
			LandkfzkennzeichenDto[] landkfzkennzeichenDtos)
			throws EJBExceptionLP, RemoteException;

	public LandkfzkennzeichenDto landkfzkennzeichenFindByPrimaryKey(String cLkz)
			throws EJBExceptionLP, RemoteException;

	public LandkfzkennzeichenDto landkfzkennzeichenFindByPrimaryKeyOhneExc(
			String cLkz) throws EJBExceptionLP, RemoteException;

	public Integer createExtraliste(ExtralisteDto extralisteDto)
			throws RemoteException, EJBExceptionLP;

	public void removeExtraliste(ExtralisteDto extralisteDto)
			throws RemoteException, EJBExceptionLP;

	public void updateExtraliste(ExtralisteDto extralisteDto)
			throws RemoteException, EJBExceptionLP;

	public ExtralisteDto extralisteFindByPrimaryKey(Integer iId)
			throws RemoteException, EJBExceptionLP;

	public ExtralisteDto[] extralisteFindByBelegartCNr(String belegartCNr)
			throws EJBExceptionLP, RemoteException;

	public KostenstelleDto kostenstelleFindByNummerMandantOhneExc(String cNr,
			String mandantCNr) throws EJBExceptionLP, RemoteException;

	public String formatEinheit(String einheitCNr, Locale loc,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	public String getServerPathSeperator();
	
	public void erstelleProtokolleintrag(ProtokollDto protokollDto,TheClientDto theClientDto);
	
	public byte[] getHintergrundbild();

	public 	Integer getPartnerLandIId(Integer partnerIId);

	public void sperreGeschaeftsjahr(Integer iGeschaeftsjahr, TheClientDto theClientDto);

	public void pruefeGeschaeftsjahrSperre(BelegbuchungDto belegBuchungDto, String mandantCnr);

	public void pruefeGeschaeftsjahrSperre(Integer geschaeftsjahr, String mandantCnr);
	
	
	public PingPacket ping(PingPacket pingPacket) ;

	/**
	 * Ermittelt die Locale & Timezone die am Server verwendet wird.
	 * 
	 * @return die Locale&Timeinfo vom Server
	 */
	ServerLocaleInfo getLocaleInfo() ;
	
	
	ServerJavaAndOSInfo getJavaAndOSInfo() ;

	String getServerVersion() ;
	
	Integer getServerBuildNumber() ;

	
	public LandplzortDto landplzortFindByLandOrtPlzOhneExc(
			String lkz, String ort, String plz)  throws RemoteException ;
	
	public LandplzortDto landplzortFindByLandOrtPlzOhneExc(
			Integer landIId, Integer ortIId, String plz)  throws RemoteException ;
	
	public OrtDto ortFindByNameOhneExc(String cName) throws RemoteException ;
	
	/**
	 * Den Versandweg (mit entsprechender Auspraegung) laden
	 * 
	 * @param versandwegId
	 * @return das VersandwegDto
	 */
	IVersandwegDto versandwegFindByPrimaryKey(Integer versandwegId) ;	
	
	IVersandwegPartnerDto versandwegPartnerFindByPrimaryKey(Integer versandwegId, Integer partnerId) ;
	public boolean enthaeltEinVKBelegUmsatzsteuerObwohlKundeSteuerfrei(
			Integer kundeIId,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto);
	public boolean enthaeltEineVKPositionKeineMwstObwohlKundeSteuerpflichtig(
			Integer kundeIId,
			BelegpositionVerkaufDto[] belegpositionVerkaufDtos,
			BelegVerkaufDto belegVerkaufDto, TheClientDto theClientDto) throws RemoteException;
	
	
	/**
	 * Den Script-Inhalt auslesen
	 * 
	 * @param modulI das betreffende Modul "projekt", "stueckliste", ...  AnfrageReportFac.REPORT_MODUL;
	 * @param filenameI der Dateiname
	 * @param mandantCNrI der Mandant f&uuml;r den das Script geladen werden soll
	 * @param spracheI die Locale
	 * @param cSubdirectory ein optionales (null) Unterverzeichnis
	 * @return liefert null bei Fehlern, ansonsten den Script-Inhalt
	 */
	String getScriptContentFromLPDir(String modulI, String filenameI,
			String mandantCNrI, Locale spracheI, String cSubdirectory) ; 	

	/**
	 * Liefert den http Port auf dem der JBoss seine Web-Dienste anbietet
	 * @return den Port
	 */
	String getServerWebPort();
	
	/**
	 *Liefert die Java Server Info
	 *
	 * @return die Java Server Info
	 */
	JavaInfoDto getServerJavaInfo();

}
