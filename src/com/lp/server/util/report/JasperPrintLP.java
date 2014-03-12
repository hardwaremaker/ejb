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
package com.lp.server.util.report;

import java.io.Serializable;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrint;

import com.lp.server.system.jcr.service.PrintInfoDto;

public class JasperPrintLP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Schluessel fuer Zusatzinformationen
	public final static String KEY_KUNDE_I_ID = "kunde_i_id";
	public final static String KEY_MAHNSTUFE = "mahnstufe_i_id";
	public final static String KEY_RECHNUNG_C_NR = "rechnung_c_nr";
	public final static String KEY_BELEGART = "key_belegart";
	public final static String KEY_BELEGIID = "key_belegiid";
	
	// Wie wurde gedruckt. Achtung max 15 Zeichen
	public final static String DRUCKTYP_DRUCKER = "DRUCKER";
	public final static String DRUCKTYP_MAIL = "MAIL";
	public final static String DRUCKTYP_FAX = "FAX";
	public final static String DRUCKTYP_PREVIEW = "PREVIEW";
	public final static String DRUCKTYP_SAVE = "SAVE";
	public final static String DRUCKTYP_CSV = "CSV";
	

	private String sReportName = null;
	private JasperPrint print = null;
	private boolean bMitLogo = true;
	private Object[][] datenMitUeberschrift = null;
	public Object[][] getDatenMitUeberschrift() {
		return datenMitUeberschrift;
	}

	public void setDatenMitUeberschrift(Object[][] datenMitUeberschrift) {
		this.datenMitUeberschrift = datenMitUeberschrift;
	}

	private HashMap<String, Object> hmAdditionalInformation = new HashMap<String, Object>();
	private PrintInfoDto oInfoForArchive= null;

	// private LPReportParameter lpReportParameter = null;

	public String getSReportName() {
		return sReportName;
	}

	public JasperPrint getPrint() {
		return print;
	}

	public void setSReportName(String sReportName) {
		this.sReportName = sReportName;
	}

	public void setPrint(JasperPrint print) {
		this.print = print;
	}

	public boolean getBMitLogo() {
		return bMitLogo;
	}

	public void setBMitLogo(boolean bMitLogo) {
		this.bMitLogo = bMitLogo;
	}

	public Object getAdditionalInformation(String key) {
		return hmAdditionalInformation.get(key);
	}

	public void putAdditionalInformation(String key, Object oInformation) {
		hmAdditionalInformation.put(key, oInformation);
	}
	
	public void setOInfoForArchive(PrintInfoDto oInfoForArchive){
		this.oInfoForArchive=oInfoForArchive;
	}
	
	public PrintInfoDto getOInfoForArchive(){
		return this.oInfoForArchive;
	}

	// public LPReportParameter getLpReportParameter() {
	// return lpReportParameter;
	// }
	//
	//
	// public void setLpReportParameter(LPReportParameter lpReportParameter) {
	// this.lpReportParameter = lpReportParameter;
	// }
}
