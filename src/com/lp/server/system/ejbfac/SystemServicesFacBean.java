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
package com.lp.server.system.ejbfac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.lp.server.system.ejb.Keyvalue;
import com.lp.server.system.ejb.KeyvaluePK;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.KeyvalueDtoAssembler;
import com.lp.server.system.service.SystemServicesFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.LPReport;
import com.lp.server.util.ServerConfiguration;
import com.lp.util.EJBExceptionLP;

@Stateless
public class SystemServicesFacBean implements SystemServicesFac {
	@PersistenceContext
	private EntityManager em;

	/**
	 * Gib den relativen Pfad von file zu dem Report Root Directory.
	 * 
	 * @param file
	 *            File
	 * @param theClientDto der aktuelle Benutzer
	 * @return String
	 */
	static public String getRelativePathtoLPDir(File file,
			TheClientDto theClientDto) {
		String reportRoot = LPReport.getReportDir();
		File fReportRoot = null;
		String sFileAbsolutePath = null;
		String sRelative = null;
		try {
			fReportRoot = new File(reportRoot).getCanonicalFile();
			String sReportRootAbsolutePath = fReportRoot.getAbsolutePath();

			if (file != null) {
				file = file.getCanonicalFile();
				sFileAbsolutePath = file.getAbsolutePath();
				int index = sFileAbsolutePath
						.lastIndexOf(sReportRootAbsolutePath);
				int length = 0;
				if (index > -1) {
					length = index + sReportRootAbsolutePath.length();
					sRelative = sFileAbsolutePath.substring(length);

					// fuehrende Pfadtrennzeichen loeschen
					while (sRelative != null
							&& sRelative.startsWith(File.separator)) {
						sRelative = sRelative
								.substring(File.separator.length());
					}
				}
			}
		} catch (IOException ex1) {
			// Umwandlung in getCanonicalFile fehlgeschlagen
		}
		return sRelative;
	}
	
	public String getPrinterNameForReport(String modulI, String filenameI,
			Locale spracheI, String cSubdirectory, TheClientDto theClientDto){
		String sReportdir = getPathFromLPDir(modulI, filenameI, theClientDto.getMandant(), spracheI, cSubdirectory, theClientDto);
		if(sReportdir!=null){
			String sPrintfile = sReportdir.substring(0,sReportdir.lastIndexOf(".")) + ".print";
			File file = new File(sPrintfile);
			if(file.exists()){
				try {
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					String sPrinterName = br.readLine();
					
					br.close() ;
					return sPrinterName;
				} catch (FileNotFoundException e) {
					// Can't happen because we check file.exists()
				}	catch (IOException e) {
					return null;
				}
			}
		} 
		return null;
	}

	/**
	 * Pfad
	 * 
	 * @param modulI
	 *            String
	 * @param filenameI
	 *            String
	 * @param mandantCNrI
	 *            String
	 * @param spracheI
	 *            Locale
	 * @param cSubdirectory
	 *            kostenstellenspezifisches Verzeichnis
	 * @param theClientDto der aktuelle Benutzer
	 * @return String
	 */
	static public String getPathFromLPDir(String modulI, String filenameI,
			String mandantCNrI, Locale spracheI, String cSubdirectory,
			TheClientDto theClientDto) {
		String reportRoot = LPReport.getReportDir();
		//----------------------------------------------------------------------
		// --
		// Falls ein kostenstellenspezeifisches Verzeichnis definiert ist, dann
		// zuerst dort suchen
		//----------------------------------------------------------------------
		// --
		if (cSubdirectory != null) {
			// erster Versuch fuer modul/mandant/locale
			String reportDir = reportRoot + modulI + File.separator
					+ cSubdirectory + File.separator + mandantCNrI
					+ File.separator + spracheI.toString();
			File f = new File(reportDir + File.separator + filenameI);
			if (f.exists()) {
				return f.getAbsolutePath();
			}

			// zweiter Versuch fuer modul/mandant
			reportDir = reportRoot + modulI + File.separator + cSubdirectory
					+ File.separator + mandantCNrI;
			f = new File(reportDir + File.separator + filenameI);
			if (f.exists()) {
				return f.getAbsolutePath();
			}

			// dritter Versuch fuer anwender-ebene
			reportDir = reportRoot + modulI + File.separator + cSubdirectory;
			f = new File(reportDir + File.separator + filenameI);
			if (f.exists()) {
				return f.getAbsolutePath();
			}
		}
		//----------------------------------------------------------------------
		// --
		// ansonsten im "normalen" Reportverzeichnis
		//----------------------------------------------------------------------
		// --
		// erster Versuch fuer modul/mandant/locale spracheLAND z.B en_US
		String reportDir = reportRoot + modulI + File.separator + "anwender"
				+ File.separator + mandantCNrI + File.separator
				+ spracheI.toString();
		File f = new File(reportDir + File.separator + filenameI);
		if (f.exists()) {
			return f.getAbsolutePath();
		}

		// zweiter Versuch fuer modul/mandant/locale sprache z.B en
		reportDir = reportRoot + modulI + File.separator + "anwender"
				+ File.separator + mandantCNrI + File.separator
				+ spracheI.getLanguage();
		f = new File(reportDir + File.separator + filenameI);
		if (f.exists()) {
			return f.getAbsolutePath();
		}

		// dritter Versuch fuer modul/mandant
		reportDir = reportRoot + modulI + File.separator + "anwender"
				+ File.separator + mandantCNrI;
		f = new File(reportDir + File.separator + filenameI);
		if (f.exists()) {
			return f.getAbsolutePath();
		}

		// vierter Versuch fuer anwender-ebene
		reportDir = reportRoot + modulI + File.separator + "anwender";
		f = new File(reportDir + File.separator + filenameI);
		if (f.exists()) {
			return f.getAbsolutePath();
		}

		// fuenfter Versuch fuer modul (mitgelieferter Original-Report)
		reportDir = reportRoot + modulI;
		f = new File(reportDir + File.separator + filenameI);
		if (f.exists()) {
			return f.getAbsolutePath();
		}
		// wenn nicht vorhanden
		return null;
	}

	
	public KeyvalueDto[] keyvalueFindyByCGruppe(String cGruppe){
		
		Query query = em.createNamedQuery("KeyvaluefindByCGruppe");
		query.setParameter(1, cGruppe);
		Collection<?> cl = query.getResultList();

		return assembleKeyvalueDtos(cl);
		
	}
	
	public void speichereKeyValueDtos(KeyvalueDto[] keyvalueDto) {
		for (int i = 0; i < keyvalueDto.length; i++) {
			// zuerst suchen
			KeyvaluePK keyvaluePK = new KeyvaluePK(keyvalueDto[i].getCGruppe(),
					keyvalueDto[i].getCKey());
			Keyvalue keyvalue = em.find(Keyvalue.class, keyvaluePK);
			if (keyvalue == null) {
				// wenn nicht vorhanden, dann erstellen
				keyvalue = new Keyvalue(keyvalueDto[i].getCGruppe(),
						keyvalueDto[i].getCKey(), keyvalueDto[i].getCValue(),
						keyvalueDto[i].getCDatentyp());
			} 
			setKeyvalueFromKeyvalueDto(keyvalue, keyvalueDto[i]);
		}

	}

	public void createKeyvalue(KeyvalueDto keyvalueDto) throws EJBExceptionLP {
		if (keyvalueDto == null) {
			return;
		}
		Keyvalue keyvalue = new Keyvalue(keyvalueDto.getCGruppe(), keyvalueDto
				.getCKey(), keyvalueDto.getCValue(), keyvalueDto.getCDatentyp());
		em.persist(keyvalue);
		em.flush();
		setKeyvalueFromKeyvalueDto(keyvalue, keyvalueDto);
	}

	public void removeKeyvalue(String cGruppe, String cKey)
			throws EJBExceptionLP {
		KeyvaluePK keyvaluePK = new KeyvaluePK(cGruppe, cKey);
		Keyvalue toRemove = em.find(Keyvalue.class, keyvaluePK);
		if (toRemove == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		try {
			em.remove(toRemove);
			em.flush();
		} catch (EntityExistsException er) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_LOESCHEN, er);
		}
	}

	public void removeKeyvalue(KeyvalueDto keyvalueDto) throws EJBExceptionLP {
		if (keyvalueDto != null) {
			String cGruppe = keyvalueDto.getCGruppe();
			String cKey = keyvalueDto.getCKey();
			removeKeyvalue(cGruppe, cKey);
		}
	}

	public void updateKeyvalue(KeyvalueDto keyvalueDto) throws EJBExceptionLP {
		if (keyvalueDto != null) {
			KeyvaluePK keyvaluePK = new KeyvaluePK(keyvalueDto.getCGruppe(),
					keyvalueDto.getCKey());
			Keyvalue keyvalue = em.find(Keyvalue.class, keyvaluePK);
			setKeyvalueFromKeyvalueDto(keyvalue, keyvalueDto);
		}
	}

	public KeyvalueDto keyvalueFindByPrimaryKey(String cGruppe, String cKey)
			throws EJBExceptionLP {
		KeyvaluePK keyvaluePK = new KeyvaluePK(cGruppe, cKey);
		Keyvalue keyvalue = em.find(Keyvalue.class, keyvaluePK);
		if (keyvalue == null) {
			throw new EJBExceptionLP(
					EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY, "");
		}
		return assembleKeyvalueDto(keyvalue);
	}

	private void setKeyvalueFromKeyvalueDto(Keyvalue keyvalue,
			KeyvalueDto keyvalueDto) {
		keyvalue.setCValue(keyvalueDto.getCValue());
		keyvalue.setCDatentyp(keyvalueDto.getCDatentyp());
		em.merge(keyvalue);
		em.flush();
	}

	private KeyvalueDto assembleKeyvalueDto(Keyvalue keyvalue) {
		return KeyvalueDtoAssembler.createDto(keyvalue);
	}

	private KeyvalueDto[] assembleKeyvalueDtos(Collection<?> keyvalues) {
		List<KeyvalueDto> list = new ArrayList<KeyvalueDto>();
		if (keyvalues != null) {
			Iterator<?> iterator = keyvalues.iterator();
			while (iterator.hasNext()) {
				Keyvalue keyvalue = (Keyvalue) iterator.next();
				list.add(assembleKeyvalueDto(keyvalue));
			}
		}
		KeyvalueDto[] returnArray = new KeyvalueDto[list.size()];
		return (KeyvalueDto[]) list.toArray(returnArray);
	}
	
	static public String getPathFromSSLDir(String cZertifikatName) {
		if(cZertifikatName.startsWith(File.separator)) {
			return cZertifikatName ;
		}

		String dir = ServerConfiguration.getSSLCertificateDir() ;
		if(!dir.endsWith(File.separator)) {
			dir += File.separator ;
		}

		return dir + cZertifikatName ;
	}
	
	/**
	 * Das Verzeichnis in dem die Scripts liegen
	 * 
	 * @return das Verzeichnis (kompletter Pfad) der Skripts
	 */
	static public String getScriptDir() {
		return ServerConfiguration.getScriptDir() ;
	}
}
