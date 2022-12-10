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
package com.lp.server.finanz.bl;

import java.util.HashMap;
import java.util.Map;

import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LandplzortDto;
import com.lp.server.system.service.OrtDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004, 2005, 2006
 * </p>
 * 
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version unbekannt Date $Date: 2009/11/12 16:58:33 $
 */
public class FibuKontoExportFormatterDatev extends FibuKontoExportFormatter {
	
	private final static String P_KONTONUMMER = "kontonummer";
	private final static String P_NAME = "name";
	private final static String P_EU_LAND = "eu-land";
	private final static String P_EU_UID = "eu-uid";
	private final static String P_ADRESSART = "adressart";
	private final static String P_STRASSE = "strasse";
	private final static String P_PLZ = "plz";
	private final static String P_ORT = "ort";
	private final static String P_LAND = "land";
	
	private class Adressart {
		public final static String STRASSE = "STR";
	}
	
	private class EuMitgliedCache extends HvCreatingCachingProvider<Integer, Boolean> {
		private Map<Integer, LandDto> hmLand = new HashMap<Integer, LandDto>();
		protected Boolean provideValue(Integer key, Integer transformedKey) {
			return getSystemFac().isEUMitglied(hmLand.get(key), getDate());
		}
		
		public boolean isEuMitglied(LandDto landDto) {
			hmLand.put(landDto.getIID(), landDto);
			return getValueOfKey(landDto.getIID());
		}
	}
	
	private EuMitgliedCache euMitgliedCache;

	FibuKontoExportFormatterDatev() {
		super();
	}

	/**
	 * exportiereDaten
	 * 
	 * @param fibuExportDtos
	 *            FibuKontoExportDto[]
	 * @param theClientDto der aktuelle Benutzer
	 * @return String
	 * @throws EJBExceptionLP
	 * TODO: Diese com.lp.server.finanz.bl.FibuKontoExportFormatter-Methode
	 *       implementieren
	 */
	protected String exportiereDaten(FibuKontoExportDto[] fibuExportDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		euMitgliedCache = new EuMitgliedCache();
		StringBuilder builder = new StringBuilder();
		for (FibuKontoExportDto exportDto : fibuExportDtos) {
			KontoExportdatenAdapter adapter = new KontoExportdatenAdapter(exportDto);
			builder
				.append(exportiereDatenImpl(adapter, theClientDto))
				.append(Helper.CR_LF);
		}
		return builder.toString();
	}
	
	private String exportiereDatenImpl(KontoExportdatenAdapter exportdaten, TheClientDto theClientDto) {
		LpMailText mt = new LpMailText();

		mt.addParameter(P_KONTONUMMER, exportdaten.getKontonummer());
		mt.addParameter(P_NAME, exportdaten.getKontoname());
		mt.addParameter(P_EU_LAND, exportdaten.getEuLand());
		mt.addParameter(P_EU_UID, exportdaten.getEuUid());
		mt.addParameter(P_ADRESSART, exportdaten.getAdressart());
		mt.addParameter(P_STRASSE, exportdaten.getStrasse());
		mt.addParameter(P_PLZ, exportdaten.getPlz());
		mt.addParameter(P_ORT, exportdaten.getOrt());
		mt.addParameter(P_LAND, exportdaten.getLkz());
		
		return transform(mt, theClientDto);
	}
	
	class KontoExportdatenAdapter {
		private FibuKontoExportDto exportDto;
		
		public KontoExportdatenAdapter(FibuKontoExportDto exportDto) {
			this.exportDto = exportDto;
		}
		
		public String getLkz() {
			return hasLand() ? land().getCLkz() : "";
		}

		public String getOrt() {
			return hasOrt() ? ort().getCName() : "";
		}

		public String getPlz() {
			return hasLandPlzort() ? landplzort().getCPlz() : "";
		}

		public String getStrasse() {
			return hasPartner() ? partner().getCStrasse() : "";
		}

		public String getAdressart() {
			return hasPartner() ? Adressart.STRASSE : "";
		}

		public String getEuUid() {
			if (hasLand()
					&& euMitgliedCache.isEuMitglied(land())
					&& partner().getCUid() != null)
				return partner().getCUid().toUpperCase().replaceAll(" ", "");
			return null;
		}

		public String getEuLand() {
			if (hasLand()
					&& euMitgliedCache.isEuMitglied(land()))
				return land().getCLkz();
			
			return "";
		}

		private KontoDto konto() {
			return exportDto.getKontoDto();
		}
		
		private PartnerDto partner() {
			return exportDto.getPartnerDto();
		}
		
		private boolean hasPartner() {
			return partner() != null;
		}
		
		private boolean hasLandPlzort() {
			return hasPartner()
					&& partner().getLandplzortDto() != null;
		}
		
		private boolean hasLand() {
			return hasLandPlzort() 
					&& partner().getLandplzortDto().getLandDto() != null;
		}
		
		private boolean hasOrt() {
			return hasLandPlzort()
					&& partner().getLandplzortDto().getOrtDto() != null;
		}
		
		private LandplzortDto landplzort() {
			return partner().getLandplzortDto();
		}
		
		private OrtDto ort() {
			return landplzort().getOrtDto();
		}
		
		private LandDto land() {
			return landplzort().getLandDto();
		}
		
		public String getKontonummer() {
			return konto().getCNr();
		}
		
		public String getKontoname() {
			return hasPartner() ? partner().getCName1nachnamefirmazeile1() : konto().getCBez();
		}
		
	}

	/**
	 * exportiereUebberschrift
	 * 
	 * @param theClientDto der aktuelle Benutzer
	 * @return String
	 * @throws EJBExceptionLP
	 * TODO: Diese com.lp.server.finanz.bl.FibuKontoExportFormatter-Methode
	 *       implementieren
	 */
	protected String exportiereUebberschrift(TheClientDto theClientDto)
			throws EJBExceptionLP {
		LpMailText mt = new LpMailText();

		mt.addParameter(P_KONTONUMMER, P_KONTONUMMER);
		mt.addParameter(P_NAME, P_NAME);
		mt.addParameter(P_EU_LAND, P_EU_LAND);
		mt.addParameter(P_EU_UID, P_EU_UID);
		mt.addParameter(P_ADRESSART, P_ADRESSART);
		mt.addParameter(P_STRASSE, P_STRASSE);
		mt.addParameter(P_PLZ, P_PLZ);
		mt.addParameter(P_ORT, P_ORT);
		mt.addParameter(P_LAND, P_LAND);

		return transform(mt, theClientDto);
	}

	private String transform(LpMailText mt, TheClientDto theClientDto) {
		return mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), 
				getXSLFile(), 
				theClientDto.getLocMandant(), 
				theClientDto);
	}
	/**
	 * getXSLFile
	 * 
	 * @return String
	 * @todo Diese com.lp.server.finanz.bl.FibuKontoExportFormatter-Methode
	 *       implementieren
	 */
	protected String getXSLFile() {
		return XSL_FILE_DATEV;
	}
}
