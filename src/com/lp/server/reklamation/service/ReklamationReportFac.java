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
package com.lp.server.reklamation.service;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface ReklamationReportFac {

	public final static String REPORT_MODUL = "reklamation";

	public final static String REPORT_REKLAMATIONSJOURNAL = "rekla_reklamationsjournal.jasper";
	public final static String REPORT_FEHLERART = "rekla_fehlerart.jasper";
	public final static String REPORT_MITARBEITERREKLAMATION = "rekla_mitarbeiterreklamation.jasper";
	public final static String REPORT_MASCHINENREKLAMATION = "rekla_maschinenreklamation.jasper";
	public final static String REPORT_LIEFERANTENBEURTEILUNG = "rekla_lieferantenbeurteilung.jasper";
	public final static String REPORT_OFFENEREKLAMATIONENEINESARTIKEL = "rekla_offenereklamationeneinesartikels.jasper";

	public final static String REPORT_REKLAMATION = "rekla_reklamation.jasper";
	public final static String REPORT_REKLAMATION_LIEFERANT = "rekla_reklamation_lieferant.jasper";

	public final static String REPORT_LEFERANTENTERMINTREUE = "rekla_lieferantentermintreue.jasper";

	public static int SORTIERGUN_REKLAMATIONSJOURNAL_BELEGNR = 1;
	public static int SORTIERGUN_REKLAMATIONSJOURNAL_FEHLER = 2;
	public static int SORTIERGUN_REKLAMATIONSJOURNAL_KUNDELIEFERANT = 3;

	public static int SORTIERUNG_FEHLERART_FEHLERART = 1;
	public static int SORTIERUNG_FEHLERART_MASCHINENGRUPPE = 2;
	public static int SORTIERUNG_FEHLERART_MASCHINENGRUPPE_MITARBEITER = 3;
	public static int SORTIERUNG_FEHLERART_MITARBEITER = 4;

	public JasperPrintLP printReklamationsjournal(Integer kostenstelleIId, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant, boolean bFertigung, boolean bNurOffene,
			int iSortierung, TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printOffeneReklamationenEinesArtikels(Integer artikelIId, boolean bNurOffene,
			DatumsfilterVonBis vonbis, TheClientDto theClientDto);

	public JasperPrintLP printReklamation(Integer reklamationIId, boolean druckeUnterartLieferant,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printLieferantentermintreue(Timestamp tVon, Timestamp tBis, Integer lieferantIId,
			TheClientDto theClientDto) throws RemoteException;

	public JasperPrintLP printLieferantenbeurteilung(Timestamp tVon, Timestamp tBis, Integer LieferantIId,
			Integer brancheIId, Integer liefergruppeIId, Integer partnerklasseIId, boolean bVerdichtet,
			boolean bDokumentenablage, TheClientDto theClientDto) throws RemoteException;

//	public JasperPrintLP printFehlerarten(java.sql.Timestamp tVon,
//			java.sql.Timestamp tBis, boolean bKunde, boolean bLieferant,
//			boolean bFertigung, Integer kundeIId, int iGruppierung,
//			boolean bNurBerechtigte, TheClientDto theClientDto);

	JasperPrintLP printFehlerarten(ReklamationFehlerartenJournalKriterienDto kritDto, TheClientDto theClientDto);

	public JasperPrintLP printMitarbeiterreklamation(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bKunde,
			boolean bLieferant, boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte,
			TheClientDto theClientDto);

	public JasperPrintLP printMaschinenreklamation(java.sql.Timestamp tVon, java.sql.Timestamp tBis, boolean bKunde,
			boolean bLieferant, boolean bFertigung, Integer kundeIId, boolean bNurBerechtigte,
			TheClientDto theClientDto);
}
