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
package com.lp.server.eingangsrechnung.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface EingangsrechnungReportFac {
	// Konstanten
	public final static String REPORT_MODUL = "eingangsrechnung";

	public final static String REPORT_EINGANGSRECHNUNG_ALLE = "er_eingangsrechnung_alle.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_OFFENE = "er_eingangsrechnung_offene.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_ZAHLUNGSJOURNAL = "er_eingangsrechnung_zahlungsjournal.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_KONTIERUNG = "er_eingangsrechnung_kontierung.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_FEHLENDE_ZOLLPAPIERE = "er_fehlende_zollimportpapiere.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_INLAND = "er_eingangsrechnung_inland.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_AUSLAND = "er_eingangsrechnung_ausland.jasper";
	public final static String REPORT_EINGANGSRECHNUNG_ERFASSTE_ZOLLPAPIERE = "er_erfasste_zollimportpapiere.jasper";
	
	public final static int REPORT_ZAHLUNGEN_SORT_ZAHLUNGSAUSGANG = 0;
	public final static int REPORT_ZAHLUNGEN_SORT_RECHNUNGSNUMMER = 1;
	public final static int REPORT_ZAHLUNGEN_SORT_BANK_AUSZUG = 2;

	public final static int REPORT_KONTIERUNG_FILTER_ER_OFFENE = 0;
	public final static int REPORT_KONTIERUNG_FILTER_ER_BEZAHLT = 1;
	public final static int REPORT_KONTIERUNG_FILTER_ER_ALLE = 2;

	public final static int REPORT_KONTIERUNG_KRIT_DATUM_BELEG = 0;
	public final static int REPORT_KONTIERUNG_KRIT_DATUM_FREIGABE = 1;

	public final static int REPORT_OFFENE_SORT_RECHNUNGSNUMMER = 0;
	public final static int REPORT_OFFENE_SORT_LIEFERANT = 1;
	public final static int REPORT_OFFENE_SORT_FAELLIGKEIT = 2;
	public final static int REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO1 = 3;
	public final static int REPORT_OFFENE_SORT_FAELLIGKEIT_SKONTO2 = 4;

	public JasperPrintLP printZahlungsjournal(TheClientDto theClientDto,
			int iSortierung, Date dVon, Date dBis, boolean bZusatzkosten);

	public JasperPrintLP printFehlendeZollpapiere(TheClientDto theClientDto);

	public JasperPrintLP printKontierungsjournal(TheClientDto theClientDto,
			int iFilterER, Integer kostenstelleIId, int iKritDatum, Date dVon,
			Date dBis, boolean bZusatzkosten);

	public JasperPrintLP printOffene(TheClientDto theClientDto, int iSort,
			Integer lieferantIId, Date dStichtag,
			boolean bStichtagIstFreigabeDatum, boolean bZusatzkosten, boolean mitNichtZugeordnetenBelegen);

	public JasperPrintLP printAlle(ReportJournalKriterienDto krit,
			TheClientDto theClientDto, boolean bZusatzkosten, boolean bDatumIstFreigabedatum);

	public JasperPrintLP[] printEingangsrechnung(Integer iEingangsrechnungIId,
			String sReportName, Integer iKopien, BigDecimal bdBetrag,
			String sZusatztext, Integer schecknummer, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;
	
	public JasperPrintLP printErfassteZollpapiere(Date dVon, Date dBis,
			TheClientDto theClientDto);
	
}
