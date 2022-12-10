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
package com.lp.server.partner.ejbfac;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lp.layer.hibernate.HvTypedCriteria;
import com.lp.server.partner.fastlanereader.DsgvotextHandler;
import com.lp.server.partner.fastlanereader.generated.FLRAnsprechpartner;
import com.lp.server.partner.fastlanereader.generated.FLRDsgvotext;
import com.lp.server.partner.fastlanereader.generated.FLRKontakt;
import com.lp.server.partner.fastlanereader.generated.FLRKurzbrief;
import com.lp.server.partner.fastlanereader.generated.FLRPASelektion;
import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.AnsprechpartnerfunktionDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.BeauskunftungDto;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.IdentifikationDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeReportFac;
import com.lp.server.partner.service.KurzbriefDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.MaileSerienbriefResponse;
import com.lp.server.partner.service.PASelektionDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.partner.service.PartnerReportFac;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.partner.service.PartnerklasseDto;
import com.lp.server.partner.service.SelektionDto;
import com.lp.server.partner.service.SerienbriefDto;
import com.lp.server.partner.service.SerienbriefEmpfaengerDto;
import com.lp.server.partner.service.SerienbriefselektionDto;
import com.lp.server.partner.service.SerienbriefselektionnegativDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.fastlanereader.generated.FLREntitylog;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.service.HvDtoLogClass;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LPReport;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.LPDatenSubreport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:31 $ >>>>>>> 1.3
 */
@Stateless
@Interceptors(TimingInterceptor.class)
public class PartnerReportFacBean extends LPReport implements PartnerReportFac {
	@PersistenceContext
	private EntityManager em;

	private String sAktuellerReport = null;
	private Object[][] data = null;

	private static int REPORT_KURZBRIEF_TEXT = 0;
	private static int REPORT_KURZBRIEF_KURZZEICHEN = 1;
	private static int REPORT_KURZBRIEF_DATUM = 2;
	private static int REPORT_KURZBRIEF_ERSTELLUNGSDATUM = 3;
	private static int REPORT_KURZBRIEF_BRIEFANREDE = 4;
	private static int REPORT_KURZBRIEF_ANZAHL_SPALTEN = 5;

	private static int REPORT_SERIENBRIEF_TEXT = 0;
	private static int REPORT_SERIENBRIEF_KURZZEICHEN = 1;
	private static int REPORT_SERIENBRIEF_DATUM = 2;
	private static int REPORT_SERIENBRIEF_ANZAHL_SPALTEN = 3;

	private static int REPORT_BEAUSKUNFTUNG_TABELLE = 0;
	private static int REPORT_BEAUSKUNFTUNG_SCHLUESSELSPALTE = 1;
	private static int REPORT_BEAUSKUNFTUNG_I_ID = 2;
	private static int REPORT_BEAUSKUNFTUNG_C_NR = 3;
	private static int REPORT_BEAUSKUNFTUNG_T_ANLEGEN = 4;
	private static int REPORT_BEAUSKUNFTUNG_T_AENDERN = 5;
	private static int REPORT_BEAUSKUNFTUNG_ANZAHL_SPALTEN = 6;

	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_VORNAME = 0;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_NACHNAME = 1;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FUNKTION = 2;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TITEL = 3;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TELDW = 4;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FAXDW = 5;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_MOBIL = 6;
	private static int REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_EMAIL = 7;

	private static int REPORT_ADRESSETIKETT_EMPFAENGER = 0;
	private static int REPORT_ADRESSETIKETT_ABSENDER = 1;
	private static int REPORT_ADRESSETIKETT_KONTONUMMER = 2;
	private static int REPORT_ADRESSETIKETT_KUNDENNUMMER = 3;
	private static int REPORT_ADRESSETIKETT_UID = 4;

	private static int REPORT_EMPFAENGERLISTE_FIRMA1 = 0;
	private static int REPORT_EMPFAENGERLISTE_FIRMA2 = 1;
	private static int REPORT_EMPFAENGERLISTE_STRASSE = 2;
	private static int REPORT_EMPFAENGERLISTE_TELEFON = 3;
	private static int REPORT_EMPFAENGERLISTE_FAX = 4;
	private static int REPORT_EMPFAENGERLISTE_EMAIL = 5;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_TITEL = 6;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_VORNAME = 7;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_NACHNAME = 8;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_HANDY = 9;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_EMAIL = 10;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_TELDW = 11;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_FAXDW = 12;
	private static int REPORT_EMPFAENGERLISTE_BEKOMMT_BRIEF = 13;
	private static int REPORT_EMPFAENGERLISTE_BEKOMMT_FAX = 14;
	private static int REPORT_EMPFAENGERLISTE_BEKOMMT_EMAIL = 15;
	private static int REPORT_EMPFAENGERLISTE_LAND = 16;
	private static int REPORT_EMPFAENGERLISTE_PLZ = 17;
	private static int REPORT_EMPFAENGERLISTE_ORT = 18;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_ANREDE = 19;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_NTITEL = 20;
	private static int REPORT_EMPFAENGERLISTE_ANSPR_ABTEILUNG = 21;
	private static int REPORT_EMPFAENGERLISTE_PARTNER_I_ID = 22;
	private static int REPORT_EMPFAENGERLISTE_BRANCHE = 23;
	private static int REPORT_EMPFAENGERLISTE_PARTNERKLASSE = 24;
	private static int REPORT_EMPFAENGERLISTE_ANZAHL_SPALTEN = 25;

	private static int REPORT_GEBURTSTAGSLISTE_FIRMA1 = 0;
	private static int REPORT_GEBURTSTAGSLISTE_FIRMA2 = 1;
	private static int REPORT_GEBURTSTAGSLISTE_TELEFON = 2;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_TITEL = 3;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_VORNAME = 4;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_NACHNAME = 5;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_TELDW = 6;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_HANDY = 7;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_GEBURTSADTUM = 8;
	private static int REPORT_GEBURTSTAGSLISTE_ANSPR_ALTER = 9;
	private static int REPORT_GEBURTSTAGSLISTE_ANZAHL_SPALTEN = 10;

	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_NAME = 0;
	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNGSZEITPUNKT = 1;
	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_PERSON_AENDERN = 2;
	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_VON = 3;
	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_NACH = 4;
	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_OPERATION = 5;

	private static int REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_ANZAHL_SPALTEN = 6;

	public boolean next() throws JRException {
		index++;
		return (index < data.length);
	}

	public Object getFieldValue(JRField jRField) throws JRException {

		Object value = null;
		if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_KURZBRIEF)) {
			if ("F_TEXT".equals(jRField.getName())) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_KURZBRIEF_TEXT]);
			} else if ("F_UNSER_ZEICHEN".equals(jRField.getName())) {
				value = data[index][REPORT_KURZBRIEF_KURZZEICHEN];
			} else if ("F_DATUM".equals(jRField.getName())) {
				value = data[index][REPORT_KURZBRIEF_DATUM];
			} else if ("F_ERSTELLUNGSDATUM".equals(jRField.getName())) {
				value = data[index][REPORT_KURZBRIEF_ERSTELLUNGSDATUM];
			} else if ("F_BRIEFANREDE".equals(jRField.getName())) {
				value = data[index][REPORT_KURZBRIEF_BRIEFANREDE];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_SERIENBRIEF)) {
			if ("F_TEXT".equals(jRField.getName())) {
				value = Helper.formatStyledTextForJasper(data[index][REPORT_SERIENBRIEF_TEXT]);
			} else if ("F_UNSER_ZEICHEN".equals(jRField.getName())) {
				value = data[index][REPORT_SERIENBRIEF_KURZZEICHEN];
			} else if ("F_DATUM".equals(jRField.getName())) {
				value = data[index][REPORT_SERIENBRIEF_DATUM];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_BEAUSKUNFTUNG)) {
			if ("F_TABELLE".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_TABELLE];
			} else if ("F_SCHLUESSELSPALTE".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_SCHLUESSELSPALTE];
			} else if ("F_I_ID".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_I_ID];
			} else if ("F_C_NR".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_C_NR];
			} else if ("F_T_ANLEGEN".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_T_ANLEGEN];
			} else if ("F_T_AENDERN".equals(jRField.getName())) {
				value = data[index][REPORT_BEAUSKUNFTUNG_T_AENDERN];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_NEWSLETTER_AENDERUNG)) {
			if ("F_ANSPRECHPARTNER_NAME".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_NAME];
			} else if ("F_AENDERUNGSZEITPUNKT".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNGSZEITPUNKT];
			} else if ("F_AENDERUNG_VON".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_VON];
			} else if ("F_AENDERUNG_NACH".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_NACH];
			} else if ("F_PERSON_AENDERN".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_PERSON_AENDERN];
			} else if ("F_OPERATION".equals(jRField.getName())) {
				value = data[index][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_OPERATION];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_GEBURTSTAGSLISTE)) {
			if ("F_ANSPR_ALTER".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_ALTER];
			} else if ("F_ANSPR_GEBURTSDATUM".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_GEBURTSADTUM];
			} else if ("F_ANSPR_HANDY".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_HANDY];
			} else if ("F_ANSPR_NACHNAME".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_NACHNAME];
			} else if ("F_ANSPR_TELDW".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_TELDW];
			} else if ("F_ANSPR_TITEL".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_TITEL];
			} else if ("F_ANSPR_VORNAME".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_ANSPR_VORNAME];
			} else if ("F_FIRMA1".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_FIRMA1];
			} else if ("F_FIRMA2".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_FIRMA2];
			} else if ("F_TELEFON".equals(jRField.getName())) {
				value = data[index][REPORT_GEBURTSTAGSLISTE_TELEFON];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_ADRESSETIKETT)) {
			if ("F_EMPFAENGER".equals(jRField.getName())) {
				value = data[index][REPORT_ADRESSETIKETT_EMPFAENGER];
			} else if ("F_ABSENDER".equals(jRField.getName())) {
				value = data[index][REPORT_ADRESSETIKETT_ABSENDER];
			} else if ("F_KONTONUMMER".equals(jRField.getName())) {
				value = data[index][REPORT_ADRESSETIKETT_KONTONUMMER];
			} else if ("F_KUNDENNUMMER".equals(jRField.getName())) {
				value = data[index][REPORT_ADRESSETIKETT_KUNDENNUMMER];
			} else if ("F_UID".equals(jRField.getName())) {
				value = data[index][REPORT_ADRESSETIKETT_UID];
			}
		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_EMPFAENGERLISTE)) {
			if ("F_PARTNER_NAME1".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_FIRMA1];
			} else if ("F_PARTNER_NAME2".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_FIRMA2];
			} else if ("F_PARTNER_LAND".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_LAND];
			} else if ("F_PARTNER_PLZ".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_PLZ];
			} else if ("F_PARTNER_ORT".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ORT];
			} else if ("F_PARTNER_STRASSE".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_STRASSE];
			} else if ("F_ANSPRECHPARTNER_VORNAME".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_VORNAME];
			} else if ("F_ANSPRECHPARTNER_NACHNAME".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_NACHNAME];
			} else if ("F_ANSPRECHPARTNER_EMAIL".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_EMAIL];
			} else if ("F_ANSPRECHPARTNER_TELEFONDW".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_TELDW];
			} else if ("F_ANSPRECHPARTNER_FAXDW".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_FAXDW];
			} else if ("F_ANSPRECHPARTNER_HANDY".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_HANDY];
			} else if ("F_ANSPRECHPARTNER_TITEL".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_TITEL];
			} else if ("F_ANSPRECHPARTNER_NTITEL".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_NTITEL];
			} else if ("F_ANSPRECHPARTNER_ABTEILUNG".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_ABTEILUNG];
			} else if ("F_ANSPRECHPARTNER_ANREDE".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_ANSPR_ANREDE];
			} else if ("F_PARTNER_EMAIL".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_EMAIL];
			} else if ("F_PARTNER_TELEFON".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_TELEFON];
			} else if ("F_PARTNER_FAX".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_FAX];
			} else if ("F_BEKOMMTFAX".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_BEKOMMT_FAX];
			} else if ("F_BEKOMMTBRIEF".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_BEKOMMT_BRIEF];
			} else if ("F_BEKOMMTEMAIL".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_BEKOMMT_EMAIL];
			} else if ("F_PARTNER_I_ID".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_PARTNER_I_ID];
			} else if ("F_PARTNER_BRANCHE".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_BRANCHE];
			} else if ("F_PARTNER_PARTNERKLASSE".equals(jRField.getName())) {
				value = data[index][REPORT_EMPFAENGERLISTE_PARTNERKLASSE];
			}

		} else if (sAktuellerReport.equals(PartnerReportFac.REPORT_PART_PARTNERSTAMMBLATT)) {
			if ("F_FAXDW".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FAXDW];
			} else if ("F_FUNKTION".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FUNKTION];
			} else if ("F_MOBIL".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_MOBIL];
			} else if ("F_EMAIL".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_EMAIL];
			} else if ("F_NACHNAME".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_NACHNAME];
			} else if ("F_TELDW".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TELDW];
			} else if ("F_TITEL".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TITEL];
			} else if ("F_VORNAME".equals(jRField.getName())) {
				value = data[index][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_VORNAME];
			}
		}
		return value;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printPartnerstammblatt(Integer partnerIId, TheClientDto theClientDto) {
		sAktuellerReport = PartnerReportFac.REPORT_PART_PARTNERSTAMMBLATT;
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

			parameter.put("P_NAME1", partnerDto.getCName1nachnamefirmazeile1());
			parameter.put("P_NAME2", partnerDto.getCName2vornamefirmazeile2());
			parameter.put("P_NAME3", partnerDto.getCName3vorname2abteilung());
			parameter.put("P_STRASSE", partnerDto.getCStrasse());

			if (partnerDto.getLandplzortDto() != null) {
				parameter.put("P_LANDPLZORT", partnerDto.getLandplzortDto().formatLandPlzOrt());
			}

			parameter.put("P_TELEFON", partnerDto.getCTelefon());

			parameter.put("P_HOMEPAGE", partnerDto.getCHomepage());

			parameter.put("P_FAX", partnerDto.getCFax());

			parameter.put("P_EMAIL", partnerDto.getCEmail());

			String sKommentar = null;

			if (partnerDto.getXBemerkung() != null && partnerDto.getXBemerkung().length() > 0) {
				sKommentar = getTextRespectUISpr("lp.partner", theClientDto.getMandant(), theClientDto.getLocUi());
				sKommentar += ":\n" + partnerDto.getXBemerkung() + "\n";
			}

			parameter.put("P_KOMMENTAR", sKommentar);

			// Selektionen
			PASelektionDto[] paselDtos = getPartnerFac().pASelektionFindByPartnerIId(partnerDto.getIId());
			if (paselDtos != null && paselDtos.length > 0) {
				String sPasel = "";
				for (int i = 0; i < paselDtos.length; i++) {
					PASelektionDto paselDto = paselDtos[i];
					SelektionDto selektionDto = getPartnerServicesFac()
							.selektionFindByPrimaryKey(paselDto.getSelektionIId(), theClientDto);
					sPasel += " " + (i + 1) + ": " + selektionDto.getBezeichnung();
				}
				parameter.put("P_SELEKTIONEN", sPasel);
			}

			ArrayList<?> alAnsprechpartner = getAnsprechpartnerFac().getAllAnsprechpartner(partnerDto.getIId(),
					theClientDto);

			data = new Object[alAnsprechpartner.size()][8];
			if (alAnsprechpartner.size() == 0) {
				data = new Object[0][8];
			}

			for (int i = 0; i < alAnsprechpartner.size(); i++) {
				AnsprechpartnerDto dtoTemp = (AnsprechpartnerDto) alAnsprechpartner.get(i);
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_NACHNAME] = dtoTemp.getPartnerDto()
						.getCName1nachnamefirmazeile1();
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_VORNAME] = dtoTemp.getPartnerDto()
						.getCName2vornamefirmazeile2();
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TITEL] = dtoTemp.getPartnerDto().getCTitel();

				if (dtoTemp.getAnsprechpartnerfunktionIId() != null) {
					AnsprechpartnerfunktionDto dto = getAnsprechpartnerFac().ansprechpartnerfunktionFindByPrimaryKey(
							dtoTemp.getAnsprechpartnerfunktionIId(), theClientDto);
					data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FUNKTION] = dto.getBezeichnung();
				}

				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_FAXDW] = dtoTemp.getCFax();
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_TELDW] = dtoTemp.getCTelefon();
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_MOBIL] = dtoTemp.getCHandy();
				data[i][REPORT_PARTNERSTAMMBLATT_ANSPRECHPARTNER_EMAIL] = dtoTemp.getCEmail();

			}

			// Kurzbriefe
			Session session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			Criteria crit = session.createCriteria(FLRKurzbrief.class);
			crit.add(Restrictions.eq("partner_i_id", partnerDto.getIId()));
			crit.add(Restrictions.eq("mandant_c_nr", theClientDto.getMandant()));
			crit.add(Restrictions.eq("belegart_c_nr", LocaleFac.BELEGART_PARTNER));

			crit.addOrder(Order.desc("t_aendern"));

			List<?> list = crit.list();
			Iterator<?> it = list.iterator();
			ArrayList<Object[]> al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRKurzbrief kurzbrief = (FLRKurzbrief) it.next();

				Object[] o = new Object[7];
				o[0] = kurzbrief.getC_betreff();

				if (kurzbrief.getFlransprechpartner() != null) {
					AnsprechpartnerDto oAnsprechpartner = getAnsprechpartnerFac()
							.ansprechpartnerFindByPrimaryKey(kurzbrief.getFlransprechpartner().getI_id(), theClientDto);
					o[1] = oAnsprechpartner.getPartnerDto().formatAnrede();
				}
				PersonalDto personalDto = getPersonalFac()
						.personalFindByPrimaryKey(kurzbrief.getPersonal_i_id_aendern(), theClientDto);

				o[2] = personalDto.getPartnerDto().formatAnrede();
				o[3] = kurzbrief.getT_aendern();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Betreff", "Ansprechpartner", "Person", "Zeitpunkt" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("P_SUBREPORT_KURZBRIEFE",
						((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(dataSub, fieldnames)));
			}

			// Umsaetze der vergangenen Jahre

			// Kurzbriefe
			session = FLRSessionFactory.getFactory().openSession();
			// Filter und Sortierung
			crit = session.createCriteria(FLRKontakt.class);
			// Filter nach Kunde
			crit.add(Restrictions.eq("partner_i_id", partnerDto.getIId()));
			crit.addOrder(Order.desc("t_kontakt"));

			list = crit.list();
			it = list.iterator();
			al = new ArrayList<Object[]>();
			while (it.hasNext()) {
				FLRKontakt kontakt = (FLRKontakt) it.next();

				Object[] o = new Object[7];
				o[0] = kontakt.getC_titel();
				o[1] = kontakt.getFlrkontaktart().getC_bez();
				o[2] = kontakt.getT_kontakt();
				o[3] = kontakt.getT_kontaktbis();

				PersonalDto personalDto = getPersonalFac().personalFindByPrimaryKey(kontakt.getFlrpersonal().getI_id(),
						theClientDto);

				o[4] = personalDto.getPartnerDto().formatAnrede();
				al.add(o);
			}

			if (al.size() > 0) {
				String[] fieldnames = new String[] { "Titel", "Kontaktart", "Von", "Bis", "Zugewiesener" };
				Object[][] dataSub = new Object[al.size()][fieldnames.length];
				dataSub = (Object[][]) al.toArray(dataSub);

				parameter.put("P_SUBREPORT_KONTAKTE",
						((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(dataSub, fieldnames)));
			}

			session.close();

			parameter.put("P_SUBREPORT_PROJEKTE",
					getKundeReportFac().getSubreportProjekte(partnerDto.getIId(), false, theClientDto));

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		parameter.put("P_MANDANTENWAEHRUNG", theClientDto.getSMandantenwaehrung());

		initJRDS(parameter, KundeReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_PARTNERSTAMMBLATT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}

	private ArrayList getAbhaengige(String tabelle, String id, int bisEbene, TheClientDto theClientDto) {

		Session sessiontest = (Session) em.getDelegate();

		SessionFactory sessionFactory = sessiontest.getSessionFactory();

		boolean bPostgres = true;

		if (FLRSessionFactory.isMSSQL()) {
			bPostgres = false;
		}

		return getAbhaengige(tabelle, id, null, new ArrayList(), bisEbene, 0, bPostgres, theClientDto);
	}

	private ArrayList getAbhaengige(String tabelle, String id, String letzteCnrDerVorgaenger, ArrayList alDaten,
			int bisEbene, int iEbene, boolean bPostgres, TheClientDto theClientDto) {

		iEbene++;
		if (iEbene >= bisEbene) {
			return alDaten;
		}
		Session session = FLRSessionFactory.getFactory().openSession();
		String sqlQuery = null;
		if (bPostgres) {
			sqlQuery = "SELECT RELNAME, (SELECT attname FROM PG_ATTRIBUTE WHERE attnum = CONKEY[1] AND attrelid=conrelid) FROM PG_CONSTRAINT LEFT OUTER JOIN  PG_CLASS ON PG_CONSTRAINT.CONRELID = PG_CLASS.OID WHERE CONFRELID = (SELECT oid FROM PG_CLASS WHERE RELNAME = '"
					+ tabelle + "')";
		} else {
			sqlQuery = "SELECT name,(SELECT name FROM sys.columns WHERE object_id=TB.object_id AND column_id=parent_column_id)  FROM sys.foreign_key_columns FK LEFT OUTER JOIN  sys.tables as TB ON FK.parent_object_id=TB.object_id WHERE FK.referenced_object_id=(SELECT object_id FROM sys.tables WHERE NAME = '"
					+ tabelle.toUpperCase() + "')";
		}

		Query query = session.createSQLQuery(sqlQuery);

		List<?> resultList = query.list();

		Iterator<?> resultListIterator = resultList.iterator();

		while (resultListIterator.hasNext()) {
			Object[] o = (Object[]) resultListIterator.next();

			try {
				Session session2 = FLRSessionFactory.getFactory().openSession();
				String sqlQueryInhalt = "SELECT I_ID FROM " + o[0] + " WHERE " + o[1] + "=" + id;

				Query queryInhalt = session2.createSQLQuery(sqlQueryInhalt);
				List<?> resultListInhalt = queryInhalt.list();
				Iterator<?> resultListIteratorInhallt = resultListInhalt.iterator();
				while (resultListIteratorInhallt.hasNext()) {
					Integer i_id = (Integer) resultListIteratorInhallt.next();

					Object[] zeile = new Object[REPORT_BEAUSKUNFTUNG_ANZAHL_SPALTEN];

					zeile[REPORT_BEAUSKUNFTUNG_TABELLE] = o[0];
					zeile[REPORT_BEAUSKUNFTUNG_SCHLUESSELSPALTE] = o[1];
					zeile[REPORT_BEAUSKUNFTUNG_I_ID] = i_id + "";

					try {
						String sqlQuery3 = "SELECT C_NR FROM " + o[0] + " WHERE I_ID =" + i_id;

						Session session3 = FLRSessionFactory.getFactory().openSession();
						Query query3 = session3.createSQLQuery(sqlQuery3);
						List<?> resultList3 = query3.list();
						Iterator<?> resultListIterator3 = resultList3.iterator();
						if (resultListIterator3.hasNext()) {
							String c_nr = (String) resultListIterator3.next();
							zeile[REPORT_BEAUSKUNFTUNG_C_NR] = c_nr;

						}
						session3.close();
					} catch (HibernateException ex) {
						zeile[REPORT_BEAUSKUNFTUNG_C_NR] = letzteCnrDerVorgaenger;
					}

					try {
						String sqlQuery3 = "SELECT T_ANLEGEN FROM " + o[0] + " WHERE I_ID =" + i_id;

						Session session3 = FLRSessionFactory.getFactory().openSession();
						Query query3 = session3.createSQLQuery(sqlQuery3);
						List<?> resultList3 = query3.list();
						Iterator<?> resultListIterator3 = resultList3.iterator();
						if (resultListIterator3.hasNext()) {
							zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN] = resultListIterator3.next();
						}
						session3.close();
					} catch (HibernateException ex) {
					}
					try {
						String sqlQuery3 = "SELECT T_AENDERN FROM " + o[0] + " WHERE I_ID =" + i_id;

						Session session3 = FLRSessionFactory.getFactory().openSession();
						Query query3 = session3.createSQLQuery(sqlQuery3);
						List<?> resultList3 = query3.list();
						Iterator<?> resultListIterator3 = resultList3.iterator();
						if (resultListIterator3.hasNext()) {
							zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN] = resultListIterator3.next();
						}
						session3.close();
					} catch (HibernateException ex) {
					}

					try {
						String sqlQuery3 = "SELECT MANDANT_C_NR FROM " + o[0] + " WHERE I_ID =" + i_id;

						Session session3 = FLRSessionFactory.getFactory().openSession();
						Query query3 = session3.createSQLQuery(sqlQuery3);
						List<?> resultList3 = query3.list();
						Iterator<?> resultListIterator3 = resultList3.iterator();
						if (resultListIterator3.hasNext()) {
							String mandant = (String) resultListIterator3.next();
							if (mandant != null && !mandant.equals(theClientDto.getMandant())) {
								continue;
							}
						}
						session3.close();
					} catch (HibernateException ex) {
					}

					if (zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN] == null
							&& zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN] != null) {
						zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN] = zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN];
					}
					if (zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN] == null
							&& zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN] != null) {
						zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN] = zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN];
					}

					if (((String) o[0]).toUpperCase().trim().startsWith("PERS_TELEFON")) {
						try {
							String sqlQuery3 = "SELECT T_VON,T_BIS FROM " + o[0] + " WHERE I_ID =" + i_id;

							Session session3 = FLRSessionFactory.getFactory().openSession();
							Query query3 = session3.createSQLQuery(sqlQuery3);
							List<?> resultList3 = query3.list();
							Iterator<?> resultListIterator3 = resultList3.iterator();
							if (resultListIterator3.hasNext()) {

								Object[] oTelefonzeiten = (Object[]) resultListIterator3.next();
								zeile[REPORT_BEAUSKUNFTUNG_T_ANLEGEN] = oTelefonzeiten[0];
								zeile[REPORT_BEAUSKUNFTUNG_T_AENDERN] = oTelefonzeiten[1];
							}
							session3.close();
						} catch (HibernateException ex) {
						}
					}

					alDaten.add(zeile);

					alDaten = getAbhaengige(o[0] + "", i_id + "", (String) zeile[REPORT_BEAUSKUNFTUNG_C_NR], alDaten,
							bisEbene, iEbene, bPostgres, theClientDto);

				}

			} catch (HibernateException ex) {

				continue;
			}

		}

		session.close();

		return alDaten;
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printBeauskunftung(Integer partnerIId, Integer beauskunftungIId, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_BEAUSKUNFTUNG;

		// PJ20631
		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		mapParameter.put("P_SUBREPORT_KOPFTEXTE", getSubreportDSGVOText(true, theClientDto));
		mapParameter.put("P_SUBREPORT_FUSSTEXTE", getSubreportDSGVOText(false, theClientDto));

		ArrayList alDaten = getAbhaengige("part_partner", partnerIId + "", 3, theClientDto);

		// Sortieren
		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				Object[] o = (Object[]) alDaten.get(j);
				Object[] o1 = (Object[]) alDaten.get(j + 1);

				String s1 = Helper.fitString2Length((String) o[REPORT_BEAUSKUNFTUNG_TABELLE], 100, ' ')
						+ o[REPORT_BEAUSKUNFTUNG_SCHLUESSELSPALTE];
				String s2 = Helper.fitString2Length((String) o1[REPORT_BEAUSKUNFTUNG_TABELLE], 100, ' ')
						+ o1[REPORT_BEAUSKUNFTUNG_SCHLUESSELSPALTE];

				String kennung1 = "";
				if (o[REPORT_BEAUSKUNFTUNG_C_NR] != null) {
					kennung1 += o[REPORT_BEAUSKUNFTUNG_C_NR];
				} else if (o[REPORT_BEAUSKUNFTUNG_I_ID] != null) {
					kennung1 += o[REPORT_BEAUSKUNFTUNG_I_ID];
				}

				s1 += kennung1;

				String kennung2 = "";
				if (o1[REPORT_BEAUSKUNFTUNG_C_NR] != null) {
					kennung2 += o1[REPORT_BEAUSKUNFTUNG_C_NR];
				} else if (o1[REPORT_BEAUSKUNFTUNG_I_ID] != null) {
					kennung2 += o1[REPORT_BEAUSKUNFTUNG_I_ID];
				}

				s2 += kennung2;

				if (s1.compareTo(s2) > 0) {

					alDaten.set(j, o1);
					alDaten.set(j + 1, o);
				}
			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_BEAUSKUNFTUNG_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);

		PersonalDto personalDto = getPersonalFac().personalFindByPartnerIIdMandantCNrOhneExc(partnerIId,
				theClientDto.getMandant());

		mapParameter.put("F_PARTNER_I_ID", partnerIId);
		mapParameter.put("F_C_NAME1", partnerDto.getCName1nachnamefirmazeile1());
		mapParameter.put("F_C_NAME2", partnerDto.getCName2vornamefirmazeile2());

		mapParameter.put("F_ANREDE", partnerDto.getAnredeCNr());
		if (personalDto != null) {
			mapParameter.put("F_GEBURTSDATUM", personalDto.getTGeburtsdatum());
		}
		mapParameter.put("F_STRASSE", partnerDto.getCStrasse());

		if (partnerDto.getLandplzortDto() != null) {
			mapParameter.put("F_LKZ", partnerDto.getLandplzortDto().getLandDto().getCLkz());
			mapParameter.put("F_PLZ", partnerDto.getLandplzortDto().getCPlz());
			mapParameter.put("F_ORT", partnerDto.getLandplzortDto().getOrtDto().getCName());
		}

		if (beauskunftungIId != null) {

			BeauskunftungDto beauskunftungDto = getPartnerServicesFac().beauskunftungFindByPrimaryKey(beauskunftungIId);

			mapParameter.put("F_BEAUSKUNFTUNG_ZEITPUNKT", beauskunftungDto.getTAnlegen());
			mapParameter.put("F_BEAUSKUNFTUNG_KOSTENPFLICHTIG",
					Helper.short2Boolean(beauskunftungDto.getBKostenpflichtig()));

			PersonalDto personalDtoBeauskunftung = getPersonalFac()
					.personalFindByPrimaryKey(beauskunftungDto.getPersonalIIdAnlegen(), theClientDto);

			mapParameter.put("F_BEAUSKUNFTUNG_PERSON", personalDtoBeauskunftung.getCKurzzeichen());

			if (beauskunftungDto.getIdentifikationIId() != null) {
				IdentifikationDto idDto = getPartnerServicesFac()
						.identifikationFindByPrimaryKey(beauskunftungDto.getIdentifikationIId(), theClientDto);

				mapParameter.put("F_BEAUSKUNFTUNG_IDENTIFIKATION", idDto.formatBezeichnung());

			}

		}

		initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_BEAUSKUNFTUNG,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		JasperPrintLP aJasperPrint = getReportPrint();
		if (beauskunftungIId != null) {
			PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(beauskunftungIId,
					QueryParameters.UC_ID_BEAUSKUNFTUNG, theClientDto);
			aJasperPrint.setOInfoForArchive(values);
		}

		return aJasperPrint;
	}

	private LPDatenSubreport getSubreportDSGVOText(boolean bKopftext, TheClientDto theClientDto) {
		Session session = FLRSessionFactory.getFactory().openSession();

		String sqlQuery = "SELECT ds FROM FLRDsgvotext ds WHERE ds.mandant_c_nr='" + theClientDto.getMandant()
				+ "' AND ds.b_kopftext=" + Helper.boolean2Short(bKopftext) + " ORDER BY ds.i_sort";

		Query query = session.createQuery(sqlQuery);

		List list = query.list();
		Iterator it = list.iterator();
		ArrayList al = new ArrayList();

		String[] fieldnames = new String[] { "Kategorie", "Inhalt" };

		while (it.hasNext()) {
			FLRDsgvotext log = (FLRDsgvotext) it.next();

			Object[] o = new Object[2];

			Iterator<?> sprsetIterator = log.getFlrdsgvokategorie().getSprset().iterator();
			String spr = DsgvotextHandler.findSpr(theClientDto.getLocUiAsString(), sprsetIterator);
			if (spr != null) {
				o[0] = spr;
			} else {
				o[0] = log.getFlrdsgvokategorie().getC_nr();
			}

			o[1] = log.getX_inhalt();
			al.add(o);
		}

		session.close();

		if (al.size() > 0) {

			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			return new LPDatenSubreport(dataSub, fieldnames);
		} else {
			return null;
		}
	}

	public JasperPrintLP printGeburtstagsliste(java.sql.Timestamp tVon, java.sql.Timestamp tBis,
			TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_GEBURTSTAGSLISTE;

		Calendar c = Calendar.getInstance();

		int tagVon = 1;
		int monatVon = 1;
		if (tVon != null) {
			c.setTime(tVon);
			tagVon = c.get(Calendar.DAY_OF_MONTH);
			monatVon = c.get(Calendar.MONTH) + 1;
		}

		int tagBis = 31;
		int monatBis = 12;
		if (tBis != null) {
			c.setTime(tBis);
			tagBis = c.get(Calendar.DAY_OF_MONTH);
			monatBis = c.get(Calendar.MONTH) + 1;
		}
		String sQuery = "SELECT ansp "
				+ " FROM FLRAnsprechpartner AS ansp WHERE ansp.flrpartneransprechpartner.t_geburtsdatumansprechpartner IS NOT NULL AND month(ansp.flrpartneransprechpartner.t_geburtsdatumansprechpartner)>="
				+ monatVon + " AND month(ansp.flrpartneransprechpartner.t_geburtsdatumansprechpartner)<=" + monatBis
				+ " ORDER BY ansp.flrpartner.c_name1nachnamefirmazeile1 ASC";
		Session session = FLRSessionFactory.getFactory().openSession();
		Query inventurliste = session.createQuery(sQuery);

		List<?> resultList = inventurliste.list();

		Iterator<?> resultListIterator = resultList.iterator();

		ArrayList<Object[]> alDaten = new ArrayList<Object[]>();

		while (resultListIterator.hasNext()) {
			FLRAnsprechpartner ansp = (FLRAnsprechpartner) resultListIterator.next();

			c.setTime(ansp.getFlrpartneransprechpartner().getT_geburtsdatumansprechpartner());

			if (c.get(Calendar.MONTH) == monatVon - 1) {
				if (c.get(Calendar.DAY_OF_MONTH) < tagVon) {
					continue;
				}
			}

			if (c.get(Calendar.MONTH) == monatBis - 1) {
				if (c.get(Calendar.DAY_OF_MONTH) > tagBis) {
					continue;
				}
			}

			Object[] zeile = new Object[REPORT_GEBURTSTAGSLISTE_ANZAHL_SPALTEN];

			zeile[REPORT_GEBURTSTAGSLISTE_FIRMA1] = ansp.getFlrpartner().getC_name1nachnamefirmazeile1();
			zeile[REPORT_GEBURTSTAGSLISTE_FIRMA2] = ansp.getFlrpartner().getC_name2vornamefirmazeile2();
			zeile[REPORT_GEBURTSTAGSLISTE_TELEFON] = ansp.getFlrpartner().getC_telefon();

			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_HANDY] = ansp.getC_handy();
			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_NACHNAME] = ansp.getFlrpartneransprechpartner()
					.getC_name1nachnamefirmazeile1();
			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_TELDW] = ansp.getC_telefon();
			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_TITEL] = ansp.getFlrpartneransprechpartner().getC_titel();
			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_VORNAME] = ansp.getFlrpartneransprechpartner()
					.getC_name2vornamefirmazeile2();

			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_GEBURTSADTUM] = ansp.getFlrpartneransprechpartner()
					.getT_geburtsdatumansprechpartner();
			Calendar cGeb = Calendar.getInstance();
			cGeb.setTimeInMillis(ansp.getFlrpartneransprechpartner().getT_geburtsdatumansprechpartner().getTime());
			zeile[REPORT_GEBURTSTAGSLISTE_ANSPR_ALTER] = Calendar.getInstance().get(Calendar.YEAR)
					- cGeb.get(Calendar.YEAR);

			alDaten.add(zeile);

		}

		for (int i = alDaten.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				java.util.Date d = (java.util.Date) alDaten.get(j)[REPORT_GEBURTSTAGSLISTE_ANSPR_GEBURTSADTUM];
				java.util.Date d1 = (java.util.Date) alDaten.get(j + 1)[REPORT_GEBURTSTAGSLISTE_ANSPR_GEBURTSADTUM];

				int dayOfYear = 0;
				if (d != null) {
					Calendar c1 = Calendar.getInstance();
					c1.setTimeInMillis(d.getTime());
					c1.set(Calendar.YEAR, 2000);
					dayOfYear = c1.get(Calendar.DAY_OF_YEAR);
				}

				int dayOfYear1 = 0;

				if (d1 != null) {
					Calendar c2 = Calendar.getInstance();
					c2.setTimeInMillis(d1.getTime());
					c2.set(Calendar.YEAR, 2000);
					dayOfYear1 = c2.get(Calendar.DAY_OF_YEAR);
				}

				if (dayOfYear > dayOfYear1) {
					Object[] h = alDaten.get(j);
					alDaten.set(j, alDaten.get(j + 1));
					alDaten.set(j + 1, h);
				}

			}
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_GEBURTSTAGSLISTE_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		mapParameter.put("P_VON", tVon);
		mapParameter.put("P_BIS", tBis);
		initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_GEBURTSTAGSLISTE,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printNewsletterAenderung(Integer partnerIId, TheClientDto theClientDto) {

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();
		sAktuellerReport = REPORT_PART_NEWSLETTER_AENDERUNG;

		PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
		mapParameter.put("P_PARTNER", partnerDto.formatFixName1Name2());

		Session session_newslettergrund = FLRSessionFactory.getFactory().openSession();
		// Filter und Sortierung
		HvTypedCriteria<FLREntitylog> crit_newslettergund = new HvTypedCriteria<FLREntitylog>(
				session_newslettergrund.createCriteria(FLREntitylog.class, "elog"));
		// Filter nach Kunde
		crit_newslettergund.add(Restrictions.eq("c_entity_key", "Newslettergrund"));
		crit_newslettergund.addOrder(Order.desc("i_id"));

		List list_newslettergund = crit_newslettergund.list();
		Iterator it_newslettergund = list_newslettergund.iterator();
		ArrayList al = new ArrayList();

		String[] fieldnames = new String[] { "F_AENDERUNGSZEITPUNKT", "F_PERSON_AENDERN", "F_OPERATION", "F_VON",
				"F_NACH", "F_FELD" };

		while (it_newslettergund.hasNext()) {
			FLREntitylog log = (FLREntitylog) it_newslettergund.next();

			Object[] o = new Object[6];

			o[0] = log.getT_aendern();

			o[1] = HelperServer.formatPersonAusFLRPartner(log.getFlrpersonal().getFlrpartner());

			o[2] = log.getC_operation();
			o[3] = log.getC_von();
			o[4] = log.getC_nach();
			o[5] = log.getC_key();
			al.add(o);
		}

		session_newslettergrund.close();

		if (al.size() > 0) {

			Object[][] dataSub = new Object[al.size()][fieldnames.length];
			dataSub = (Object[][]) al.toArray(dataSub);

			mapParameter.put("P_SUBREPORT_AENDERUNGEN_NEWSLETTERGRUND",
					((net.sf.jasperreports.engine.JRDataSource) new LPDatenSubreport(dataSub, fieldnames)));
		}

		Session session = FLRSessionFactory.getFactory().openSession();
		HvTypedCriteria<FLREntitylog> crit = new HvTypedCriteria<FLREntitylog>(
				session.createCriteria(FLREntitylog.class, "elog"));

		crit.createAlias("elog.flrpersonal", "personal");
		crit.createAlias("personal.flrpartner", "partner");

		/*
		 * crit.add(Restrictions.eq("c_filter_key", HvDtoLogClass.ANSPRECHPARTNER));
		 * crit.add(Restrictions.eq("filter_i_id", partnerIId + ""));
		 */

		crit.add(Restrictions.or(Restrictions.eq("c_filter_key", HvDtoLogClass.ANSPRECHPARTNER),
				Restrictions.eq("c_filter_key", HvDtoLogClass.PARTNER)));
		crit.add(Restrictions.eq("filter_i_id", partnerIId + ""));

		crit.add(Restrictions.eq("c_key", "NewslettergrundIId"));

		crit.addOrder(Order.desc("i_id"));

		ArrayList alDaten = new ArrayList();

		List<FLREntitylog> results = crit.list();
		Iterator it = results.iterator();
		while (it.hasNext()) {

			FLREntitylog log = (FLREntitylog) it.next();
			Object[] oZeile = new Object[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_ANZAHL_SPALTEN];
			try {
				AnsprechpartnerDto anspDto = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKeyOhneExc(new Integer(log.getEntity_i_id()), theClientDto);

				if (anspDto != null) {
					oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_NAME] = anspDto.getPartnerDto()
							.formatFixName1Name2();

				} else {

					if (log.getC_entity_key().equals(HvDtoLogClass.PARTNER)) {
						oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_NAME] = "-Partner-Kopfdaten-";
					} else {
						oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_NAME] = "-Nicht mehr vorhanden-";
					}

				}

				oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNGSZEITPUNKT] = log.getT_aendern();
				oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_OPERATION] = log.getC_operation();
				oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_PERSON_AENDERN] = HelperServer
						.formatPersonAusFLRPartner(log.getFlrpersonal().getFlrpartner());
				oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_VON] = log.getC_von();
				oZeile[REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_AENDERUNG_NACH] = log.getC_nach();

			} catch (EJBExceptionLP e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}

			alDaten.add(oZeile);
		}

		Object[][] returnArray = new Object[alDaten.size()][REPORT_NEWSLETTER_AENDERUNG_ANSPRECHPARTNER_ANZAHL_SPALTEN];
		data = (Object[][]) alDaten.toArray(returnArray);

		initJRDS(mapParameter, REPORT_MODUL, REPORT_PART_NEWSLETTER_AENDERUNG, theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	public JasperPrintLP printAdressetikett(Integer partnerIId, Integer ansprechpartnerIId, int iOption,
			TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_ADRESSETIKETT;
		data = new Object[1][9];

		try {
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partnerIId, theClientDto);
			AnsprechpartnerDto ansprechpartnerDto = null;
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			if (ansprechpartnerIId != null) {
				ansprechpartnerDto = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(ansprechpartnerIId,
						theClientDto);
			}

			data[0][REPORT_ADRESSETIKETT_EMPFAENGER] = formatAdresseFuerAusdruck(partnerDto, ansprechpartnerDto,
					mandantDto, theClientDto.getLocUi());
			data[0][REPORT_ADRESSETIKETT_ABSENDER] = Helper.formatMandantAdresse(mandantDto);

			data[0][REPORT_ADRESSETIKETT_UID] = partnerDto.getCUid();

			if (iOption == REPORT_ADRESSETIKETT_OPTION_KUNDE) {
				KundeDto kdto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(partnerIId,
						theClientDto.getMandant(), theClientDto);
				if (kdto != null) {

					if (kdto.getIidDebitorenkonto() != null) {
						data[0][REPORT_ADRESSETIKETT_KONTONUMMER] = getFinanzFac()
								.kontoFindByPrimaryKey(kdto.getIidDebitorenkonto()).getCNr();
					}

					data[0][REPORT_ADRESSETIKETT_KUNDENNUMMER] = kdto.getIKundennummer();

				}
			} else if (iOption == REPORT_ADRESSETIKETT_OPTION_LIEFERANT) {
				LieferantDto lfDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(partnerIId,
						theClientDto.getMandant(), theClientDto);
				if (lfDto != null && lfDto.getKontoIIdKreditorenkonto() != null) {

					data[0][REPORT_ADRESSETIKETT_KONTONUMMER] = getFinanzFac()
							.kontoFindByPrimaryKey(lfDto.getKontoIIdKreditorenkonto()).getCNr();
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		HashMap<String, Object> mapParameter = new HashMap<String, Object>();

		initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_ADRESSETIKETT,
				theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		return getReportPrint();
	}

	@org.jboss.ejb3.annotation.TransactionTimeout(20000)
	public int faxeSerienbrief(Integer serienbriefIId, SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos,
			String sAbsender, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {

		SerienbriefDto serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId,
				theClientDto);
		VersandauftragDto versandAuftragDto = null;
		int iSent = 0;

		for (int i = 0; i < serienbriefEmpfaengerDtos.length; i++) {

			SerienbriefEmpfaengerDto dto = serienbriefEmpfaengerDtos[i];
			if (dto.isBBekommtFax()) {
				versandAuftragDto = null;

				// Faxe immer mit Logo drucken
				JasperPrintLP jasperPrint = getPartnerReportFac().printSerienbrief(serienbriefDto, null, theClientDto,
						dto.getPartnerDto().getIId(), true);

				versandAuftragDto = new VersandauftragDto();

				versandAuftragDto.setCAbsenderadresse(sAbsender);

				versandAuftragDto.setCBetreff(serienbriefDto.getSBetreff());

				versandAuftragDto.setOInhalt(exportToPDF(jasperPrint.getPrint()));

				versandAuftragDto.setCText(null);

				versandAuftragDto.setCEmpfaenger(dto.getCVersandFaxnummer());

				// wir haben was zu faxen
				getVersandFac().createVersandauftrag(versandAuftragDto, false, theClientDto);
				iSent++;
			}
		}
		return iSent;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public MaileSerienbriefResponse maileSerienbrief(Integer serienbriefIId,
			SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos, String sAbsenderEmail, TheClientDto theClientDto)
			throws RemoteException, EJBExceptionLP {

		SerienbriefDto serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId,
				theClientDto);

		VersandauftragDto versandAuftragDto = null;

		int emailCount = 0;
		for (SerienbriefEmpfaengerDto dto : serienbriefEmpfaengerDtos) {
			if (!dto.isBBekommtEmail()) {
				continue;
			}

			KurzbriefDto kurzbriefDto = new KurzbriefDto();
			kurzbriefDto.setCBetreff(serienbriefDto.getSBetreff());
			kurzbriefDto.setXText(serienbriefDto.getSXText());
			kurzbriefDto.setBHtml(serienbriefDto.getBHtml());

			Integer ansprechpartnerIId = null;
			if (dto.getAnsprechpartnerDto() != null) {
				ansprechpartnerIId = dto.getAnsprechpartnerDto().getIId();
			}
			// emails immer mit Logo drucken
			JasperPrintLP jPrint = getPartnerReportFac().printSerienbrief(serienbriefDto, ansprechpartnerIId,
					theClientDto, dto.getPartnerDto().getIId(), true);

			versandAuftragDto = new VersandauftragDto();
			versandAuftragDto.setCAbsenderadresse(sAbsenderEmail);
			versandAuftragDto.setCBetreff(serienbriefDto.getSBetreff());

			com.lp.server.system.service.MailtextDto m = new com.lp.server.system.service.MailtextDto();
			m.setParamXslFile(PartnerReportFac.REPORT_PART_SERIENBRIEF);
			m.setMailAnprechpartnerIId(kurzbriefDto.getAnsprechpartnerIId());
			m.setMailPartnerIId(dto.getPartnerDto().getIId());
			m.setParamLocale(theClientDto.getLocUi());
			m.setParamMandantCNr(theClientDto.getMandant());
			m.setParamModul(PartnerReportFac.REPORT_MODUL);
			m.setParamLocale(theClientDto.getLocUi());

			if (serienbriefDto.isPlain()) {
				versandAuftragDto.setOInhalt(exportToPDF(jPrint.getPrint()));
				versandAuftragDto.setCDateiname(getVersandFac().getDefaultAnhangForBelegEmail(m, null, null,
						theClientDto.getLocUi(), theClientDto));
			}

			ResourceBundle.getBundle(HelperServer.RESOURCE_BUNDEL_ALLG,
					Helper.string2Locale(dto.getPartnerDto().getLocaleCNrKommunikation()));

			if (serienbriefDto.getXMailtext() == null) {

				if (serienbriefDto.isPlain()) {
					versandAuftragDto.setCText(getVersandFac().getDefaultTextForBelegEmail(m, theClientDto));
				} else {
					String s = serienbriefDto.getSXText();
					for (Entry<String, Object> entry : jPrint.getMapParameters().entrySet()) {
						Object v = entry.getValue();
						if (v == null) {
							v = "";
						}
						s = s.replaceAll("@@" + entry.getKey() + "@@", v.toString());
					}

					if (!(s.startsWith("<html") || s.startsWith("<!DOCTYPE html"))) {
						m.setMailText(s);
						s = getVersandFac().getDefaultTextForBelegHtmlEmail(m, theClientDto);
						s = "<html lang=\"de\">" + "<head>" + "</head>" + "<body>" + s + "</body>" + "</html>";
					}
					versandAuftragDto.setCText(s);
					// versandAuftragDto.setOInhalt(s.getBytes());
				}
			} else {
				String preparedText = serienbriefDto.getXMailtext();

				String cBriefanrede = "";

				if (ansprechpartnerIId != null) {
					cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(ansprechpartnerIId,
							dto.getPartnerDto().getIId(),
							Helper.string2Locale(dto.getPartnerDto().getLocaleCNrKommunikation()), theClientDto);
				} else {
					// neutrale Anrede
					cBriefanrede = getBriefanredeNeutralOderPrivatperson(dto.getPartnerDto().getIId(),
							Helper.string2Locale(dto.getPartnerDto().getLocaleCNrKommunikation()), theClientDto);
				}

				preparedText = preparedText.replaceAll("###BRIEFANREDE###", cBriefanrede);

				versandAuftragDto.setCText(preparedText);
			}

			versandAuftragDto.setCEmpfaenger(dto.getCVersandEmailadresse());

			getVersandFac().createVersandauftrag(versandAuftragDto, false, theClientDto);

			emailCount++;
		}

		MaileSerienbriefResponse response = new MaileSerienbriefResponse(serienbriefEmpfaengerDtos.length, emailCount);
		return response;
	}

	public JasperPrintLP druckeSerienbrief(Integer serienbriefIId, SerienbriefEmpfaengerDto[] serienbriefEmpfaengerDtos,
			boolean bMitLogo, TheClientDto theClientDto) throws EJBExceptionLP {
		JasperPrintLP jasperPrint = null;
		try {

			SerienbriefDto serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId,
					theClientDto);

			for (int i = 0; i < serienbriefEmpfaengerDtos.length; i++) {

				SerienbriefEmpfaengerDto dto = serienbriefEmpfaengerDtos[i];
				if (dto.isBBekommtBrief()) {

					Integer ansprechpartnerIId = null;
					if (dto.getAnsprechpartnerDto() != null) {
						ansprechpartnerIId = dto.getAnsprechpartnerDto().getIId();
					}
					JasperPrintLP jPrint = getPartnerReportFac().printSerienbrief(serienbriefDto, ansprechpartnerIId,
							theClientDto, dto.getPartnerDto().getIId(), bMitLogo);
					jasperPrint = (jasperPrint == null) ? jPrint
							: Helper.addReport2Report(jasperPrint, jPrint.getPrint());
				}
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		return jasperPrint;
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SerienbriefEmpfaengerDto[] getSerienbriefEmpfaenger(Integer serienbriefIId, TheClientDto theClientDto) {

		SerienbriefDto serienbriefDto = null;

		try {
			serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId, theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return getSerienbriefEmpfaenger(serienbriefIId, serienbriefDto.getLocaleCNr(), theClientDto);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public SerienbriefEmpfaengerDto[] getSerienbriefEmpfaenger(Integer serienbriefIId, String localeCNr,
			TheClientDto theClientDto) {
		ArrayList<SerienbriefEmpfaengerDto> al = new ArrayList<SerienbriefEmpfaengerDto>();
		try {
			SerienbriefDto serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId,
					theClientDto);

			Session session = FLRSessionFactory.getFactory().openSession();

			String queryString = "SELECT p.i_id,(SELECT k.i_id FROM FLRKunde as k WHERE k.flrpartner.i_id=p.i_id AND k.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";
			queryString += ") ,"
					+ "(SELECT k.b_istinteressent FROM FLRKunde as k WHERE k.flrpartner.i_id=p.i_id AND k.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			queryString += ") ,"
					+ " (SELECT l.i_id FROM FLRLieferant as l WHERE l.flrpartner.i_id=p.i_id AND l.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";
			queryString += ") ,"
					+ "(SELECT l.b_moeglicherlieferant FROM FLRLieferant as l WHERE l.flrpartner.i_id=p.i_id AND l.mandant_c_nr='"
					+ theClientDto.getMandant() + "'";

			queryString += "),p.c_name1nachnamefirmazeile1, p  FROM FLRPartner as p  WHERE 1=1 ";

			if (Helper.short2boolean(serienbriefDto.getBVersteckteDabei()) == false) {
				queryString += " AND p.b_versteckt=0";
			}

			if (serienbriefDto.getCPlz() != null) {
				queryString += " AND p.flrlandplzort.c_plz LIKE '" + serienbriefDto.getCPlz() + "%'";
			}

			if (serienbriefDto.getLandIId() != null) {
				queryString += " AND p.flrlandplzort.flrland.i_id = " + serienbriefDto.getLandIId();
			}

			if (serienbriefDto.getPartnerklasseIId() != null) {
				queryString += " AND p.flrpartnerklasse.i_id = " + serienbriefDto.getPartnerklasseIId();
			}
			if (serienbriefDto.getBrancheIId() != null) {
				queryString += " AND p.branche_i_id = " + serienbriefDto.getBrancheIId();
			}

			if (localeCNr != null) {
				queryString += " AND p.locale_c_nr_kommunikation = '" + localeCNr + "'";
			}

			// Selektionen
			SerienbriefselektionDto[] serienbriefselektionDtos = getPartnerServicesFac()
					.serienbriefselektionFindBySerienbriefIId(serienbriefIId);

			// PJ18074 negative Selektionen
			SerienbriefselektionnegativDto[] serienbriefselektionnegattivDtos = getPartnerServicesFac()
					.serienbriefselektionnegativFindBySerienbriefIId(serienbriefIId);

			queryString += " ORDER BY p.c_name1nachnamefirmazeile1";

			Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				Integer partner_i_id = (Integer) o[0];

				Integer kunde_i_id = (Integer) o[1];
				Boolean bInteresent = false;
				if (o[2] != null) {
					bInteresent = Helper.short2Boolean((Short) o[2]);
				}
				Integer lieferant_i_id = (Integer) o[3];
				Boolean bMoegliche = false;
				if (o[4] != null) {
					bMoegliche = Helper.short2Boolean((Short) o[4]);
				}

				FLRPartner flrPartner = (FLRPartner) o[6];

				Set<FLRPASelektion> sSelektionen = flrPartner.getPartner_paselektion_set();

				// Selektionen

				if (serienbriefselektionDtos != null && serienbriefselektionDtos.length > 0) {

					if (sSelektionen.size() == 0) {
						continue;
					}

					if (Helper.short2boolean(serienbriefDto.getBSelektionenLogischesOder()) == true) {

						// LOGISCHES ODER -> Wenn einer passt, dann ist er dabei
						boolean bEinerPasst = false;

						Iterator<FLRPASelektion> itSelektionen = sSelektionen.iterator();

						while (itSelektionen.hasNext()) {
							FLRPASelektion pasel = itSelektionen.next();

							for (int i = 0; i < serienbriefselektionDtos.length; i++) {
								if (serienbriefselektionDtos[i].getSelektionIId().intValue() == pasel
										.getSelektion_i_id().intValue()) {
									bEinerPasst = true;
									break;
								}
							}

						}

						if (bEinerPasst == false) {
							continue;
						}

					} else {

						// LOGISCHES UND
						// Es muessen alle passen
						// LOGISCHES ODER -> Wenn einer passt, dann ist er dabei
						boolean bEinerFehlt = false;

						for (int i = 0; i < serienbriefselektionDtos.length; i++) {

							boolean bGefunden = false;
							Iterator<FLRPASelektion> itSelektionen = sSelektionen.iterator();
							while (itSelektionen.hasNext()) {
								FLRPASelektion pasel = itSelektionen.next();

								if (serienbriefselektionDtos[i].getSelektionIId().intValue() == pasel
										.getSelektion_i_id().intValue()) {
									bGefunden = true;
								}

							}

							if (bGefunden == false) {
								bEinerFehlt = true;
								break;
							}

						}

						if (bEinerFehlt == true) {
							continue;
						}
					}

				}

				// Negative Selektionen
				if (serienbriefselektionnegattivDtos != null && serienbriefselektionnegattivDtos.length > 0) {

					Iterator<FLRPASelektion> itSelektionen = sSelektionen.iterator();

					boolean bEinerPasst = false;
					while (itSelektionen.hasNext()) {
						FLRPASelektion pasel = itSelektionen.next();

						for (int i = 0; i < serienbriefselektionnegattivDtos.length; i++) {
							if (serienbriefselektionnegattivDtos[i].getSelektionIId().intValue() == pasel
									.getSelektion_i_id().intValue()) {
								bEinerPasst = true;
								break;
							}
						}

					}

					if (bEinerPasst) {
						continue;
					}

				}

				boolean bBekommtSerienbrief = false;
				SerienbriefEmpfaengerDto dto = new SerienbriefEmpfaengerDto();
				dto.setIstInteressent(bInteresent);
				dto.setKundeIId(kunde_i_id);
				dto.setLieferantIId(lieferant_i_id);
				dto.setPartnerIId(partner_i_id);

				if (Helper.short2boolean(serienbriefDto.getBGehtanpartner()) == true) {
					bBekommtSerienbrief = true;
				} else {
					if (kunde_i_id != null) {
						if (Helper.short2boolean(serienbriefDto.getBGehtAnKunden()) == true && bInteresent == false) {

							if (serienbriefDto.getNAbumsatz() == null && serienbriefDto.getNBisumsatz() == null) {
								bBekommtSerienbrief = true;
							} else {

								java.sql.Date dVon = null;
								if (serienbriefDto.getTUmsatzab() != null) {
									dVon = new java.sql.Date(serienbriefDto.getTUmsatzab().getTime());
								}
								java.sql.Date dBis = null;
								if (serienbriefDto.getTUmsatzbis() != null) {
									dBis = new java.sql.Date(serienbriefDto.getTUmsatzbis().getTime());
								}
								BigDecimal umsatz = getRechnungFac().getUmsatzVomKundenImZeitraum(theClientDto,
										kunde_i_id, dVon, dBis, false);
								dto.setUmsatz(umsatz);

								double umsatzVon = -999999999;
								double umsatzBis = 999999999;

								if (serienbriefDto.getNAbumsatz() != null) {
									umsatzVon = serienbriefDto.getNAbumsatz().doubleValue();
								}
								if (serienbriefDto.getNBisumsatz() != null) {
									umsatzBis = serienbriefDto.getNBisumsatz().doubleValue();
								}

								if (umsatz.doubleValue() >= umsatzVon && umsatz.doubleValue() <= umsatzBis) {
									bBekommtSerienbrief = true;
								}
							}

						}
						if (Helper.short2boolean(serienbriefDto.getBGehtAnInteressenten()) == true && bInteresent) {
							bBekommtSerienbrief = true;
						}
					} else if (lieferant_i_id != null) {
						if (Helper.short2boolean(serienbriefDto.getBGehtanlieferanten()) == true) {
							bBekommtSerienbrief = true;
						}
						if (Helper.short2boolean(serienbriefDto.getBGehtanmoeglichelieferanten()) == true
								&& bMoegliche) {
							bBekommtSerienbrief = true;
						}
					}
				}

				if (bBekommtSerienbrief) {

					if (Helper.short2boolean(serienbriefDto.getBMitzugeordnetenfirmen()) == true) {

						AnsprechpartnerDto[] zugeordneteanspDtos = getAnsprechpartnerFac()
								.ansprechpartnerFindByPartnerIIdAnsprechpartner(partner_i_id, theClientDto);

						if (zugeordneteanspDtos != null && zugeordneteanspDtos.length > 0) {
							for (int i = 0; i < zugeordneteanspDtos.length; i++) {

								// SP4355
								if (Helper.short2boolean(serienbriefDto.getBVersteckteDabei()) == false
										&& Helper.short2boolean(zugeordneteanspDtos[i].getBVersteckt()) == true) {

								} else {

									PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(
											zugeordneteanspDtos[i].getPartnerIId(), theClientDto);
									dto.setPartnerDto(partnerDto);

									PartnerDto partnerDtoAnsp = getPartnerFac().partnerFindByPrimaryKey(
											zugeordneteanspDtos[i].getPartnerIIdAnsprechpartner(), theClientDto);
									zugeordneteanspDtos[i].setPartnerDto(partnerDtoAnsp);

									// Damit die Kommunikations-Dtos befuellt
									// werden
									AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
											.ansprechpartnerFindByPrimaryKey(zugeordneteanspDtos[i].getIId(),
													theClientDto);

									dto.setAnsprechpartnerDto(ansprechpartnerDto);
									al.add(dto.clone());
								}
							}
						} else {
							PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partner_i_id, theClientDto);

							dto.setPartnerDto(partnerDto);

							al.add(dto);
						}

					} else {
						PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(partner_i_id, theClientDto);

						dto.setPartnerDto(partnerDto);

						// PJ19475
						if (serienbriefDto.isNewsletter() && partnerDto.getNewslettergrundIId() != null) {
							boolean bNewsletterAngemeldet = Helper.short2boolean(getPartnerServicesFac()
									.newslettergrundFindByPrimaryKey(partnerDto.getNewslettergrundIId(), theClientDto)
									.getBAngemeldet());
							if (bNewsletterAngemeldet) {
								al.add(dto.clone());
							}
						}

						if (serienbriefDto.getAnsprechpartnerfunktionIId() != null || serienbriefDto.isNewsletter()) {

							Session sessionAnsp = FLRSessionFactory.getFactory().openSession();
							org.hibernate.Criteria critAnsp = session.createCriteria(FLRAnsprechpartner.class);
							critAnsp.add(
									Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNER_I_ID, partner_i_id));

							if (Helper.short2boolean(serienbriefDto.getBVersteckteDabei()) == false) {
								critAnsp.add(Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_VERSTECKT,
										Helper.boolean2Short(false)));
							}

							if (serienbriefDto.isNewsletter()) {
								critAnsp.createAlias(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_FLRNEWSLETTERGRUND, "a");
								critAnsp.add(Restrictions.eq("a." + AnsprechpartnerFac.FLR_NEWSLETTERGRUND_B_ANGEMELDET,
										new Short((short) 1)));
							}
							if (serienbriefDto.getAnsprechpartnerfunktionIId() != null) {
								critAnsp.add(Restrictions.eq(
										AnsprechpartnerFac.FLR_ANSPRECHPARTNER_ANSPRECHPARTNERFUNKTION_I_ID,
										serienbriefDto.getAnsprechpartnerfunktionIId()));
							}
							critAnsp.addOrder(Order.asc("i_sort"));
							List<?> resultListansp = critAnsp.list();
							Iterator<?> resultListIteratorAnsp = resultListansp.iterator();
							if (resultListansp.size() == 0
									&& Helper.short2Boolean(serienbriefDto.getBAnsprechpartnerfunktionAuchOhne())) {
								al.add(dto);
							} else if (resultListansp.size() == 0
									&& Helper.short2Boolean(serienbriefDto.getBWennkeinanspmitfktDannersteransp())) {
								// PJ18074

								Session sessionAnspErster = FLRSessionFactory.getFactory().openSession();
								org.hibernate.Criteria critAnspErster = sessionAnspErster
										.createCriteria(FLRAnsprechpartner.class);
								critAnspErster.add(Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNER_I_ID,
										partner_i_id));

								if (Helper.short2boolean(serienbriefDto.getBVersteckteDabei()) == false) {
									critAnspErster.add(Restrictions.eq(AnsprechpartnerFac.FLR_ANSPRECHPARTNER_VERSTECKT,
											Helper.boolean2Short(false)));
								}

								critAnspErster.addOrder(Order.asc("i_sort"));
								critAnspErster.setMaxResults(1);
								List<?> resultListanspErster = critAnspErster.list();
								Iterator<?> resultListIteratorAnspErster = resultListanspErster.iterator();
								if (resultListIteratorAnspErster.hasNext()) {
									FLRAnsprechpartner flrAnsprechpartnerErster = (FLRAnsprechpartner) resultListIteratorAnspErster
											.next();
									AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
											.ansprechpartnerFindByPrimaryKey(flrAnsprechpartnerErster.getI_id(),
													theClientDto);
									SerienbriefEmpfaengerDto dtoTemp = dto.clone();
									dtoTemp.setAnsprechpartnerDto(ansprechpartnerDto);

									al.add(dtoTemp);

								} else {
									// PJ20557
									SerienbriefEmpfaengerDto dtoTemp = dto.clone();
									dtoTemp.setAnsprechpartnerDto(null);
									al.add(dtoTemp);
								}
								sessionAnspErster.close();

							} else {
								while (resultListIteratorAnsp.hasNext()) {
									FLRAnsprechpartner flrAnsprechpartner = (FLRAnsprechpartner) resultListIteratorAnsp
											.next();
									AnsprechpartnerDto ansprechpartnerDto = getAnsprechpartnerFac()
											.ansprechpartnerFindByPrimaryKey(flrAnsprechpartner.getI_id(),
													theClientDto);
									// dto.setAnsprechpartnerDto(ansprechpartnerDto);

									SerienbriefEmpfaengerDto dtoTemp = dto.clone();
									dtoTemp.setAnsprechpartnerDto(ansprechpartnerDto);

									al.add(dtoTemp);
								}
							}
							sessionAnsp.close();
						} else {
							al.add(dto);
						}
					}
				}
			}

			// TODO ghp: 11.12.2015 Was sollen die naechsten 4 auskommentierten
			// Zeilen erreichen?
			// while (resultListIterator.hasNext()) {
			// FLRKunde flrKunde = (FLRKunde) resultListIterator.next();
			// SerienbriefEmpfaengerDto dto = new SerienbriefEmpfaengerDto();
			// }
			session.close();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			for (int i = 0; i < al.size(); i++) {
				SerienbriefEmpfaengerDto dto = (SerienbriefEmpfaengerDto) al.get(i);

				dto.setCFirma1_Partner(dto.getPartnerDto().getCName1nachnamefirmazeile1());

				dto.setCFirma2_Partner(dto.getPartnerDto().getCName2vornamefirmazeile2());

				if (dto.getPartnerDto().getLandplzortDto_Postfach() != null) {

					dto.setCLand_Partner(dto.getPartnerDto().getLandplzortDto_Postfach().getLandDto().getCLkz());
					dto.setCPlz_Partner(dto.getPartnerDto().getLandplzortDto_Postfach().getCPlz());
					dto.setCOrt_Partner(dto.getPartnerDto().getLandplzortDto_Postfach().getOrtDto().getCName());

					String postfach = getTextRespectUISpr("lp.postfach", mandantDto.getCNr(), theClientDto.getLocUi());

					dto.setCStrasse_Partner(postfach + " " + dto.getPartnerDto().getCPostfach());

				} else {
					if (dto.getPartnerDto().getLandplzortDto() != null) {

						dto.setCLand_Partner(dto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz());
						dto.setCPlz_Partner(dto.getPartnerDto().getLandplzortDto().getCPlz());
						dto.setCOrt_Partner(dto.getPartnerDto().getLandplzortDto().getOrtDto().getCName());

						dto.setCStrasse_Partner(dto.getPartnerDto().getCStrasse());

					}
				}
				PartnerDto partnerDto = dto.getPartnerDto();
				HvOptional<String> branche = findBranche(partnerDto, theClientDto);
				if (branche.isPresent()) {
					dto.setCBranche(branche.get());
				}

				HvOptional<String> partnerklasse = findPartnerklasse(partnerDto, theClientDto);
				if (partnerklasse.isPresent()) {
					dto.setCPartnerklasse(partnerklasse.get());
				}

				dto.setCTelefon_Partner(partnerDto.getCTelefon());

				dto.setCFax_Partner(partnerDto.getCFax());

				dto.setCEmail_Partner(partnerDto.getCEmail());

				if (dto.getAnsprechpartnerDto() != null) {

					AnsprechpartnerDto ansprechpartnerDto = dto.getAnsprechpartnerDto();
					dto.setAnsprechpartnerDto(ansprechpartnerDto);
					dto.setCVorname_Ansprechpartner(ansprechpartnerDto.getPartnerDto().getCName2vornamefirmazeile2());
					dto.setCNachname_Ansprechpartner(ansprechpartnerDto.getPartnerDto().getCName1nachnamefirmazeile1());
					dto.setCTitel_Ansprechpartner(ansprechpartnerDto.getPartnerDto().getCTitel());
					dto.setCNTitel_Ansprechpartner(ansprechpartnerDto.getPartnerDto().getCNtitel());
					dto.setCAnrede_Ansprechpartner(ansprechpartnerDto.getPartnerDto().getAnredeCNr());

					dto.setCAbteilung_Ansprechpartner(ansprechpartnerDto.getCAbteilung());

					dto.setCEmail_Ansprechpartner(ansprechpartnerDto.getCEmail());

					dto.setCTelefonDW_Ansprechpartner(ansprechpartnerDto.getCTelefon());

					dto.setCHandy_Ansprechpartner(ansprechpartnerDto.getCHandy());

					dto.setCFaxDW_Ansprechpartner(ansprechpartnerDto.getCDirektfax());

				}

				// Wer bekommt was?
				// Brief
				if ((partnerDto.getLandplzortDto() != null && partnerDto.getCStrasse() != null)
						|| partnerDto.getLandplzortDto_Postfach() != null) {
					dto.setBBekommtBrief(true);
					dto.setCVersandBriefanrede(formatAdresseFuerAusdruck(partnerDto, dto.getAnsprechpartnerDto(),
							mandantDto, Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));
				}

				// Email
				if (dto.getAnsprechpartnerDto() != null && dto.getAnsprechpartnerDto().getCEmail() != null
						&& dto.getAnsprechpartnerDto().getCEmail().length() > 0) {

					dto.setBBekommtEmail(true);
					dto.setCVersandEmailadresse(dto.getAnsprechpartnerDto().getCEmail());

				} else {
					if (dto.getPartnerDto().getCEmail() != null) {
						if (dto.getPartnerDto().getCEmail().length() > 0) {
							dto.setBBekommtEmail(true);
							dto.setCVersandEmailadresse(dto.getPartnerDto().getCEmail());
						}
					}
				}
				// Fax
				if (dto.getAnsprechpartnerDto() != null && dto.getAnsprechpartnerDto().getCFax() != null) {

					if (dto.getAnsprechpartnerDto().getCFax() != null
							&& dto.getAnsprechpartnerDto().getCFax().length() > 0) {

						dto.setCVersandFaxnummer(dto.getAnsprechpartnerDto().getCFax());
					}
				} else {
					if (dto.getPartnerDto().getCFax() != null && dto.getPartnerDto().getCFax() != null) {
						if (dto.getPartnerDto().getCFax().length() > 0) {
							dto.setBBekommtFax(true);
							dto.setCVersandFaxnummer(dto.getPartnerDto().getCFax());
						}
					}
				}
				al.set(i, dto);
			}

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		SerienbriefEmpfaengerDto[] returnArray = new SerienbriefEmpfaengerDto[al.size()];
		return (SerienbriefEmpfaengerDto[]) al.toArray(returnArray);
	}

	@TransactionAttribute(TransactionAttributeType.NEVER)
	public JasperPrintLP printEmpfaengerliste(Integer serienbriefIId, TheClientDto theClientDto) {

		try {
			SerienbriefDto serienbriefDto = getPartnerServicesFac().serienbriefFindByPrimaryKey(serienbriefIId,
					theClientDto);

			SerienbriefEmpfaengerDto[] dtos = getSerienbriefEmpfaenger(serienbriefIId, theClientDto);
			index = -1;
			sAktuellerReport = PartnerReportFac.REPORT_PART_EMPFAENGERLISTE;
			data = new Object[dtos.length][REPORT_EMPFAENGERLISTE_ANZAHL_SPALTEN];

			for (int i = 0; i < dtos.length; i++) {
				SerienbriefEmpfaengerDto serienbriefEmpfaengerDto = dtos[i];

				data[i][REPORT_EMPFAENGERLISTE_FIRMA1] = serienbriefEmpfaengerDto.getCFirma1_Partner();
				data[i][REPORT_EMPFAENGERLISTE_FIRMA2] = serienbriefEmpfaengerDto.getCFirma2_Partner();

				data[i][REPORT_EMPFAENGERLISTE_STRASSE] = serienbriefEmpfaengerDto.getCStrasse_Partner();

				data[i][REPORT_EMPFAENGERLISTE_LAND] = serienbriefEmpfaengerDto.getCLand_Partner();

				data[i][REPORT_EMPFAENGERLISTE_PLZ] = serienbriefEmpfaengerDto.getCPlz_Partner();

				data[i][REPORT_EMPFAENGERLISTE_ORT] = serienbriefEmpfaengerDto.getCOrt_Partner();

				data[i][REPORT_EMPFAENGERLISTE_EMAIL] = serienbriefEmpfaengerDto.getCEmail_Partner();
				data[i][REPORT_EMPFAENGERLISTE_FAX] = serienbriefEmpfaengerDto.getCFax_Partner();
				data[i][REPORT_EMPFAENGERLISTE_TELEFON] = serienbriefEmpfaengerDto.getCTelefon_Partner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_ANREDE] = serienbriefEmpfaengerDto.getCAnrede_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_TITEL] = serienbriefEmpfaengerDto.getCTitel_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_NTITEL] = serienbriefEmpfaengerDto.getCNTitel_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_ABTEILUNG] = serienbriefEmpfaengerDto
						.getCAbteilung_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_VORNAME] = serienbriefEmpfaengerDto.getCVorname_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_NACHNAME] = serienbriefEmpfaengerDto
						.getCNachname_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_TELDW] = serienbriefEmpfaengerDto.getCTelefonDW_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_FAXDW] = serienbriefEmpfaengerDto.getCFaxDW_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_HANDY] = serienbriefEmpfaengerDto.getCHandy_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_ANSPR_EMAIL] = serienbriefEmpfaengerDto.getCEmail_Ansprechpartner();
				data[i][REPORT_EMPFAENGERLISTE_BEKOMMT_BRIEF] = new Boolean(serienbriefEmpfaengerDto.isBBekommtBrief());
				data[i][REPORT_EMPFAENGERLISTE_BEKOMMT_FAX] = new Boolean(serienbriefEmpfaengerDto.isBBekommtFax());
				data[i][REPORT_EMPFAENGERLISTE_BEKOMMT_EMAIL] = new Boolean(serienbriefEmpfaengerDto.isBBekommtEmail());
				data[i][REPORT_EMPFAENGERLISTE_PARTNER_I_ID] = serienbriefEmpfaengerDto.getPartnerIId();
				data[i][REPORT_EMPFAENGERLISTE_BRANCHE] = serienbriefEmpfaengerDto.getCBranche();
				data[i][REPORT_EMPFAENGERLISTE_PARTNERKLASSE] = serienbriefEmpfaengerDto.getCPartnerklasse();
			}

			HashMap<String, Object> mapParameter = new HashMap<String, Object>();
			mapParameter.put("P_BEZEICHNUNG", serienbriefDto.getCBez());

			if (serienbriefDto.getBrancheIId() != null) {

				mapParameter.put("P_BRANCHE", getPartnerServicesFac()
						.brancheFindByPrimaryKey(serienbriefDto.getBrancheIId(), theClientDto).getBezeichnung());

			}
			if (serienbriefDto.getPartnerklasseIId() != null) {

				mapParameter.put("P_PARTNERKLASSE",
						getPartnerFac()
								.partnerklasseFindByPrimaryKey(serienbriefDto.getPartnerklasseIId(), theClientDto)
								.getBezeichnung());

			}

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);

			mapParameter.put("P_ABSENDER", Helper.formatMandantAdresse(mandantDto));

			initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_EMPFAENGERLISTE,
					theClientDto.getMandant(), theClientDto.getLocUi(), theClientDto);

		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return getReportPrint();
	}

	public JasperPrintLP printSerienbrief(SerienbriefDto serienbriefDto, Integer ansprechpartnerIId,
			TheClientDto theClientDto, Integer iIdPartnerI, boolean bMitLogo) throws EJBExceptionLP {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_SERIENBRIEF;
		data = new Object[1][REPORT_KURZBRIEF_ANZAHL_SPALTEN];

		try {

			data[0][REPORT_SERIENBRIEF_TEXT] = serienbriefDto.getSXText();
			data[0][REPORT_SERIENBRIEF_DATUM] = new Date();

			HashMap<String, Object> mapParameter = new HashMap<String, Object>();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_BETREFF", serienbriefDto.getSBetreff());

			mapParameter.put("P_LOCALE", serienbriefDto.getLocaleCNr());

			mapParameter.put("P_GEHTANINTERESSENTEN", Helper.short2Boolean(serienbriefDto.getBGehtAnInteressenten()));
			mapParameter.put("P_GEHTANKUNDEN", Helper.short2Boolean(serienbriefDto.getBGehtAnKunden()));
			mapParameter.put("P_GEHTANLIEFERANTEN", Helper.short2Boolean(serienbriefDto.getBGehtanlieferanten()));
			mapParameter.put("P_GEHTANMEOGLICHELIEFERANTEN",
					Helper.short2Boolean(serienbriefDto.getBGehtanmoeglichelieferanten()));
			mapParameter.put("P_GEHTANPARTNER", Helper.short2Boolean(serienbriefDto.getBGehtanpartner()));
			mapParameter.put("P_MITVERSTECKTEN", Helper.short2Boolean(serienbriefDto.getBVersteckteDabei()));

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);

			// PJ20426
			mapParameter.put("P_UIDNUMMER", partnerDto.getCUid());

			KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(iIdPartnerI,
					theClientDto.getMandant(), theClientDto);
			if (kundeDto != null) {
				if (kundeDto.getIidDebitorenkonto() != null) {
					mapParameter.put("P_DEBITORENKONTO",
							getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto()).getCNr());
				}

				mapParameter.put("P_KUNDE_I_ID", kundeDto.getIId());

			}

			LieferantDto lieferantDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(iIdPartnerI,
					theClientDto.getMandant(), theClientDto);
			if (lieferantDto != null) {
				if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
					mapParameter.put("P_KREDITORENKONTO",
							getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
				}
				mapParameter.put("P_LIEFERANT_I_ID", lieferantDto.getIId());
			}

			AnsprechpartnerDto oAnsprechpartner = null;
			if (ansprechpartnerIId != null) {
				oAnsprechpartner = getAnsprechpartnerFac().ansprechpartnerFindByPrimaryKey(ansprechpartnerIId,
						theClientDto);
				// belegkommunikation:
				String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
						partnerDto, PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);
				String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
						partnerDto, PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);
				String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
						partnerDto, PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
				String sHandy = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
						partnerDto, PartnerFac.KOMMUNIKATIONSART_HANDY, theClientDto.getMandant(), theClientDto);
				// belegkommunikation: 3 daten als parameter an den Report
				// weitergeben
				mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
				mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
				mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");
			}

			String cBriefanrede = "";

			if (ansprechpartnerIId != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(ansprechpartnerIId, partnerDto.getIId(),
						Helper.string2Locale(serienbriefDto.getLocaleCNr()), theClientDto);
			} else {
				// neutrale Anrede
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(partnerDto.getIId(),
						Helper.string2Locale(serienbriefDto.getLocaleCNr()), theClientDto);
			}

			mapParameter.put("P_BRIEFANREDE", cBriefanrede);

			// getAnsprechpartnerFac().ansprechpartnerFindByAnsprechpartnerIId(
			// kundeDto.getPartnerIId(), cNrUserI);

			mapParameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, oAnsprechpartner, mandantDto,
					Helper.string2Locale(serienbriefDto.getLocaleCNr())));
			mapParameter.put("P_MITLOGO", new Boolean(true));

			if (oAnsprechpartner != null) {
				PartnerDto Ansprechpartner = getPartnerFac()
						.partnerFindByPrimaryKey(oAnsprechpartner.getPartnerIIdAnsprechpartner(), theClientDto);
				mapParameter.put("P_NAME1", Ansprechpartner.getCName1nachnamefirmazeile1());
				mapParameter.put("P_NAME2", Ansprechpartner.getCName2vornamefirmazeile2());
				mapParameter.put("P_NAME3", Ansprechpartner.getCName3vorname2abteilung());
				mapParameter.put("P_ANREDE", Ansprechpartner.getAnredeCNr());
				mapParameter.put("P_ANREDE_FIX", Ansprechpartner.formatFixTitelName1Name2());
			} else {
				mapParameter.put("P_NAME1", partnerDto.getCName1nachnamefirmazeile1());
				mapParameter.put("P_NAME2", partnerDto.getCName2vornamefirmazeile2());
				mapParameter.put("P_NAME3", partnerDto.getCName3vorname2abteilung());
				mapParameter.put("P_ANREDE", partnerDto.getAnredeCNr());
				mapParameter.put("P_ANREDE_FIX", partnerDto.formatFixTitelName1Name2());
			}

			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);

			data[0][REPORT_SERIENBRIEF_KURZZEICHEN] = Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalBenutzer.getCKurzzeichen());

			initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_SERIENBRIEF,
					theClientDto.getMandant(), Helper.string2Locale(serienbriefDto.getLocaleCNr()), theClientDto,
					bMitLogo, null);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		JasperPrintLP print = getReportPrint();
		PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(iIdPartnerI, QueryParameters.UC_ID_PARTNER,
				theClientDto);
		print.setOInfoForArchive(values);
		return print;
	}

	public JasperPrintLP printLastschrift(Integer partnerbankIId, boolean bMitLogo, TheClientDto theClientDto) {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_LASTSCHRIFT;
		data = new Object[0][0];
		try {
			PartnerbankDto pbDto = getBankFac().partnerbankFindByPrimaryKey(partnerbankIId, theClientDto);

			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(pbDto.getPartnerIId(), theClientDto);

			HashMap<String, Object> mapParameter = new HashMap<String, Object>();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(null, partnerDto,
					PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(null, partnerDto,
					PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(null, partnerDto,
					PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);

			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			mapParameter.put("P_EMAIL", sEmail);
			mapParameter.put("P_FAX", sFax);
			mapParameter.put("P_TELEFON", sTelefon);

			mapParameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, null, mandantDto,
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));

			mapParameter.put("P_NAME1", partnerDto.getCName1nachnamefirmazeile1());
			mapParameter.put("P_NAME2", partnerDto.getCName2vornamefirmazeile2());
			mapParameter.put("P_NAME3", partnerDto.getCName3vorname2abteilung());
			mapParameter.put("P_ANREDE", partnerDto.getAnredeCNr());
			mapParameter.put("P_ANREDE_FIX", partnerDto.formatFixTitelName1Name2());

			PartnerDto bankDto = getPartnerFac().partnerFindByPrimaryKey(pbDto.getBankPartnerIId(), theClientDto);
			BankDto bDto = getBankFac().bankFindByPrimaryKey(pbDto.getBankPartnerIId(), theClientDto);

			mapParameter.put("P_BIC", bDto.getCBic());

			mapParameter.put("P_BANK_NAME", bankDto.formatFixName1Name2());

			mapParameter.put("P_IBAN", pbDto.getCIban());
			mapParameter.put("P_KONTONUMMER", pbDto.getCKtonr());
			mapParameter.put("P_SEPA_MANDANTSNUMMER", pbDto.getCSepamandatsnummer());
			mapParameter.put("P_SEPA_ERTEILT", pbDto.getTSepaerteilt());

			mapParameter.put("P_MITLOGO", new Boolean(bMitLogo));

			initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_LASTSCHRIFT,
					theClientDto.getMandant(), Helper.string2Locale(partnerDto.getLocaleCNrKommunikation()),
					theClientDto, bMitLogo, null);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}

		return getReportPrint();

	}

	public JasperPrintLP printKurzbrief(KurzbriefDto kurzbriefDtoI, TheClientDto theClientDto, Integer iIdPartnerI,
			boolean bMitLogo) throws EJBExceptionLP {
		index = -1;
		sAktuellerReport = PartnerReportFac.REPORT_PART_KURZBRIEF;
		data = new Object[1][REPORT_KURZBRIEF_ANZAHL_SPALTEN];

		try {

			data[0][REPORT_KURZBRIEF_TEXT] = kurzbriefDtoI.getXText();
			data[0][REPORT_KURZBRIEF_DATUM] = kurzbriefDtoI.getTAendern();

			data[0][REPORT_KURZBRIEF_ERSTELLUNGSDATUM] = kurzbriefDtoI.getTAnlegen();

			PartnerDto partner = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);
			Locale loc = Helper.string2Locale(partner.getLocaleCNrKommunikation());
			String cBriefanrede;
			if (kurzbriefDtoI.getAnsprechpartnerIId() != null) {
				cBriefanrede = getPartnerServicesFac().getBriefanredeFuerBeleg(kurzbriefDtoI.getAnsprechpartnerIId(),
						iIdPartnerI, loc, theClientDto);
			} else {
				cBriefanrede = getBriefanredeNeutralOderPrivatperson(iIdPartnerI, loc, theClientDto);
			}

			data[0][REPORT_KURZBRIEF_BRIEFANREDE] = cBriefanrede;

			HashMap<String, Object> mapParameter = new HashMap<String, Object>();

			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mapParameter.put("P_MANDANTADRESSE", Helper.formatMandantAdresse(mandantDto));

			mapParameter.put("P_BETREFF", kurzbriefDtoI.getCBetreff());
			mapParameter.put("P_BELEGART", kurzbriefDtoI.getBelegartCNr());

			mapParameter.put("P_BEARBEITER",
					getPersonalFac().getPersonRpt(kurzbriefDtoI.getPersonalIIdAnlegen(), theClientDto));

			// KundeDto kundeDto =
			// getKundeFac().kundeFindByPrimaryKey(iIdKundeI, cNrUserI);
			PartnerDto partnerDto = getPartnerFac().partnerFindByPrimaryKey(iIdPartnerI, theClientDto);

			AnsprechpartnerDto oAnsprechpartner = null;
			Integer ansprechpartnerIId = null;
			if (kurzbriefDtoI.getAnsprechpartnerIId() != null) {
				oAnsprechpartner = getAnsprechpartnerFac()
						.ansprechpartnerFindByPrimaryKey(kurzbriefDtoI.getAnsprechpartnerIId(), theClientDto);
				ansprechpartnerIId = oAnsprechpartner.getIId();

			}
			// belegkommunikation:
			String sEmail = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					partnerDto, PartnerFac.KOMMUNIKATIONSART_EMAIL, theClientDto.getMandant(), theClientDto);
			String sFax = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId, partnerDto,
					PartnerFac.KOMMUNIKATIONSART_FAX, theClientDto.getMandant(), theClientDto);
			String sTelefon = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					partnerDto, PartnerFac.KOMMUNIKATIONSART_TELEFON, theClientDto.getMandant(), theClientDto);
			String sHandy = getPartnerFac().partnerkommFindRespectPartnerAsStringOhneExec(ansprechpartnerIId,
					partnerDto, PartnerFac.KOMMUNIKATIONSART_HANDY, theClientDto.getMandant(), theClientDto);
			// belegkommunikation: 3 daten als parameter an den Report
			// weitergeben
			mapParameter.put(LPReport.P_ANSPRECHPARTNEREMAIL, sEmail != null ? sEmail : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERFAX, sFax != null ? sFax : "");
			mapParameter.put(LPReport.P_ANSPRECHPARTNERTELEFON, sTelefon != null ? sTelefon : "");

			// getAnsprechpartnerFac().ansprechpartnerFindByAnsprechpartnerIId(
			// kundeDto.getPartnerIId(), cNrUserI);

			mapParameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, oAnsprechpartner, mandantDto,
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));
			mapParameter.put("P_MITLOGO", new Boolean(true));

			if (oAnsprechpartner != null) {
				PartnerDto Ansprechpartner = getPartnerFac()
						.partnerFindByPrimaryKey(oAnsprechpartner.getPartnerIIdAnsprechpartner(), theClientDto);
				mapParameter.put("P_NAME1", Ansprechpartner.getCName1nachnamefirmazeile1());
				mapParameter.put("P_NAME2", Ansprechpartner.getCName2vornamefirmazeile2());
				mapParameter.put("P_NAME3", Ansprechpartner.getCName3vorname2abteilung());
				mapParameter.put("P_ANREDE", Ansprechpartner.getAnredeCNr());
				mapParameter.put("P_ANREDE_FIX", getPartnerFac().formatFixAnredeTitelName2Name1(Ansprechpartner,
						Helper.string2Locale(partnerDto.getLocaleCNrKommunikation()), theClientDto));
			} else {
				mapParameter.put("P_NAME1", partnerDto.getCName1nachnamefirmazeile1());
				mapParameter.put("P_NAME2", partnerDto.getCName2vornamefirmazeile2());
				mapParameter.put("P_NAME3", partnerDto.getCName3vorname2abteilung());
				mapParameter.put("P_ANREDE", partnerDto.getAnredeCNr());
				mapParameter.put("P_ANREDE_FIX", partnerDto.formatFixTitelName1Name2());
			}

			// PJ20426
			mapParameter.put("P_UIDNUMMER", partnerDto.getCUid());

			KundeDto kundeDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(iIdPartnerI,
					theClientDto.getMandant(), theClientDto);
			if (kundeDto != null) {
				if (kundeDto.getIidDebitorenkonto() != null) {
					mapParameter.put("P_DEBITORENKONTO",
							getFinanzFac().kontoFindByPrimaryKey(kundeDto.getIidDebitorenkonto()).getCNr());
				}

				mapParameter.put("P_KUNDE_I_ID", kundeDto.getIId());

			}

			LieferantDto lieferantDto = getLieferantFac().lieferantFindByiIdPartnercNrMandantOhneExc(iIdPartnerI,
					theClientDto.getMandant(), theClientDto);
			if (lieferantDto != null) {
				if (lieferantDto.getKontoIIdKreditorenkonto() != null) {
					mapParameter.put("P_KREDITORENKONTO",
							getFinanzFac().kontoFindByPrimaryKey(lieferantDto.getKontoIIdKreditorenkonto()).getCNr());
				}
				mapParameter.put("P_LIEFERANT_I_ID", lieferantDto.getIId());
			}

			mapParameter.put("P_KUNDE_ADRESSBLOCK", formatAdresseFuerAusdruck(partnerDto, oAnsprechpartner, mandantDto,
					Helper.string2Locale(partnerDto.getLocaleCNrKommunikation())));
			mapParameter.put("P_MITLOGO", new Boolean(true));

			PersonalDto oPersonalBenutzer = getPersonalFac().personalFindByPrimaryKey(theClientDto.getIDPersonal(),
					theClientDto);

			data[0][REPORT_KURZBRIEF_KURZZEICHEN] = Helper.getKurzzeichenkombi(oPersonalBenutzer.getCKurzzeichen(),
					oPersonalBenutzer.getCKurzzeichen());

			initJRDS(mapParameter, PartnerReportFac.REPORT_MODUL, PartnerReportFac.REPORT_PART_KURZBRIEF,
					theClientDto.getMandant(), Helper.string2Locale(partnerDto.getLocaleCNrKommunikation()),
					theClientDto, bMitLogo, null);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		JasperPrintLP print = getReportPrint();
		PrintInfoDto values = getJCRDocFac().getPathAndPartnerAndTable(iIdPartnerI, QueryParameters.UC_ID_PARTNER,
				theClientDto);
		print.setOInfoForArchive(values);
		return print;
	}

	private HvOptional<String> findBranche(PartnerDto partnerDto, TheClientDto theClientDto) {
		if (partnerDto.getBrancheIId() == null)
			return HvOptional.empty();

		BrancheDto brancheDto = getPartnerServicesFac().brancheFindByPrimaryKey(partnerDto.getBrancheIId(),
				theClientDto);
		return HvOptional.ofNullable(brancheDto.getBezeichnung());
	}

	private HvOptional<String> findPartnerklasse(PartnerDto partnerDto, TheClientDto theClientDto) {
		if (partnerDto.getPartnerklasseIId() == null)
			return HvOptional.empty();

		PartnerklasseDto partnerklasseDto = getPartnerFac()
				.partnerklasseFindByPrimaryKey(partnerDto.getPartnerklasseIId(), theClientDto);
		return HvOptional.ofNullable(partnerklasseDto.getBezeichnung());
	}
}
