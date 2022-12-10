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
package com.lp.server.system.automatikjob;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.lp.server.artikel.ejb.Paternoster;
import com.lp.server.system.service.AutoPaternosterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.util.csv.LPCSVReader;

public class AutomatikjobPaternoster extends AutomatikjobBasis {


	private boolean errorInJob;

	public AutomatikjobPaternoster() {
		super();
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Paternosterabfrage");
		try {
			// get all paternoster

			Collection<Paternoster> allPaternoster;
//			allPaternoster = getAutoPaternosterFac().getAllPaternoster();
			allPaternoster = getAutoPaternosterFac().paternosterFindByMandant(theClientDto.getMandant());
			Iterator<Paternoster> iter = allPaternoster.iterator();
			
			while (iter.hasNext()) {			// for each
				errorInJob = false;
				int waitResponse = 5;
				String dirCmd = "";
				String dirResponse = "";
				Paternoster pn = iter.next();
				// get parameter
				HashMap<String,String> pnAttribMap = getAutoPaternosterFac().getPaternosterParameter(pn.getIId());
				if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_CMD))
					dirCmd = pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_CMD);
				if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_RESPONSE))
					dirResponse = pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_DIR_RESPONSE);
				if (pnAttribMap.containsKey(AutoPaternosterFac.PT_EIGENSCHAFT_WAITRESPONSE))
					waitResponse = new Integer(pnAttribMap.get(AutoPaternosterFac.PT_EIGENSCHAFT_WAITRESPONSE));
				
				// parameter pruefen
				if (dirCmd.length() == 0) {
					myLogger.error("Fehler Parameter dirCmd");
					errorInJob = true;
				}
				if (dirResponse.length() == 0) {
					myLogger.error("Fehler Parameter dirResponse");
					errorInJob = true;
				}

				// verarbeiten
				if (!errorInJob) {
					if (pn.getCPaternostertyp().trim().equals(AutoPaternosterFac.PT_TYP_LEANLIFT))
						getLagerstandLeanlift(pn.getIId(), dirCmd, dirResponse, waitResponse);
				}
				// hier weitere Paternostertypen einbauen
			}
		} catch (Exception ex) {
			myLogger.error("Fehler bei Paternosterabfrage", ex);
		}
		myLogger.info("ende Paternosterabfrage");
		return errorInJob;
	}


	private void getLagerstandLeanlift(Integer paternosterIId, String dirCmd, String dirResponse, int waitResponse) {
		String filename = "Lagstand";
		String extRequest = ".req";
		String extrResponse = ".amd";
		String data = "READ AMDBACKUP " + filename + "\r\n";
		try {
			FileWriter fstream = new FileWriter(dirCmd + filename + extRequest);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(data);
			out.flush();
			out.close();
		} catch (IOException ex) {
			myLogger.error("Fehler beim Erstellen Commandfile", ex);
		}
		try {
			Thread.sleep(waitResponse * 1000);
		} catch (InterruptedException ex) {
			myLogger.error("Fehler beim Warten auf Ergebnis", ex);
		}

		ArrayList<String[]> al = null;
		try {
			LPCSVReader reader = new LPCSVReader(
					new FileReader(dirResponse + filename + extrResponse),	'$');
			al = (ArrayList<String[]>) reader.readAll();
			reader.close();
		} catch (FileNotFoundException ex) {
			myLogger.error("Response File fehlt", ex);
		} catch (IOException ex) {
			myLogger.error("Zugriffsfehler Responsefile", ex);
		}
		myLogger.info("Anzahl Artikel im Paternoster: " + al.size());
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		org.hibernate.Session session = FLRSessionFactory.getFactory().openSession();
		int i=0;
		for (i=0; i<al.size(); i++) {
			String[] a = al.get(i);
			if (a[0].equals("*")) {
				String cNr = a[1].substring(1).trim();
				String cBestand = a[5].substring(1).trim();
				BigDecimal menge = new BigDecimal(cBestand);
				speicherePaternostermenge(session, paternosterIId, cNr, menge, ts);
			} else {
				myLogger.error("Ung\u00FCltige Zeile " + i + " ignoriert");
			}
		}
		myLogger.info("Artikel verarbeitet: " + i);
		session.close();
	}

	private void speicherePaternostermenge(org.hibernate.Session session, Integer paternosterIId, String cNr, BigDecimal menge, Timestamp ts) {
		String sql = "UPDATE WW_ARTIKELLAGERPLAETZE SET N_LAGERSTANDPATERNOSTER = ?, T_AENDERN = ? "
			+ "WHERE I_ID = (SELECT I_ID FROM WW_ARTIKELLAGERPLAETZE "
			+ "WHERE LAGERPLATZ_I_ID = (SELECT I_ID FROM WW_LAGERPLATZ "
			+ "WHERE PATERNOSTER_I_ID = ? AND ARTIKEL_I_ID = "
			+ "(SELECT I_ID FROM WW_ARTIKEL WHERE C_NR = ?)))";
		
		org.hibernate.SQLQuery sq = session.createSQLQuery(sql);
		sq.setBigDecimal(0, menge);
		sq.setTimestamp(1, ts);
		sq.setInteger(2, paternosterIId);
		sq.setString(3, cNr);
		int anzahl = sq.executeUpdate();
		if (anzahl == 0) {
			if (menge.doubleValue() > 0) {
				myLogger.info("Artikel " + cNr + " ist im Paternoster ohne Paternosterlagerplatz mit Menge " + menge.doubleValue());
			}
		}
	}



}
