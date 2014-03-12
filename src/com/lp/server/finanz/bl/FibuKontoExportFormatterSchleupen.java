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
package com.lp.server.finanz.bl;

import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuKontoExportFormatterSchleupen extends FibuKontoExportFormatter {
	
	
	private final static String P_KONTO ="Konto";
	private final static String P_ANREDE ="Anrede";
	private final static String P_NAME ="Name";
	private final static String P_BEZEICHNUNG ="Bezeichnung";
	private final static String P_STRASSE ="Strasse";
	private final static String P_PLZ ="Plz";
	private final static String P_ORT ="Ort";
	private final static String P_BANKKONTO ="Bankkonto";
	private final static String P_BLZ ="Blz";
	private final static String P_BANK ="Bank";
	private final static String P_UST_ID_NUMMER ="USt-Id-Nummer";

	public FibuKontoExportFormatterSchleupen() {
		super();
	}

	protected String exportiereDaten(FibuKontoExportDto[] fibuExportDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<fibuExportDtos.length;i++){
			LpMailText mt = new LpMailText();
			
			mt.addParameter(P_KONTO,fibuExportDtos[i].getKontoDto().getCNr());
			if(fibuExportDtos[i].getPartnerDto()!=null){
				mt.addParameter(P_ANREDE,fibuExportDtos[i].getPartnerDto().getAnredeCNr());
				mt.addParameter(P_NAME,fibuExportDtos[i].getPartnerDto().getCName1nachnamefirmazeile1());
				mt.addParameter(P_BEZEICHNUNG,fibuExportDtos[i].getPartnerDto().getCName2vornamefirmazeile2());
				mt.addParameter(P_STRASSE,fibuExportDtos[i].getPartnerDto().getCStrasse());
				
				if(fibuExportDtos[i].getPartnerDto().getLandplzortDto()!=null){
					mt.addParameter(P_PLZ,fibuExportDtos[i].getPartnerDto().getLandplzortDto().getCPlz());
					if(fibuExportDtos[i].getPartnerDto().getLandplzortDto().getOrtDto()!=null){
						mt.addParameter(P_ORT,fibuExportDtos[i].getPartnerDto().getLandplzortDto().getOrtDto().getCName());
					} else {
						mt.addParameter(P_ORT,"");
					}
				} else {
					mt.addParameter(P_PLZ,"");
					mt.addParameter(P_ORT,"");
				}
				if(fibuExportDtos[i].getPartnerDto().getBankDto()!=null){
					mt.addParameter(P_BANKKONTO,"");
					mt.addParameter(P_BLZ,fibuExportDtos[i].getPartnerDto().getBankDto().getCBlz());
					mt.addParameter(P_BANK,fibuExportDtos[i].getPartnerDto().getBankDto().getCBic());
				} else {
					mt.addParameter(P_BANKKONTO,"");
					mt.addParameter(P_BLZ,"");
					mt.addParameter(P_BANK,"");
				}
				mt.addParameter(P_UST_ID_NUMMER,fibuExportDtos[i].getPartnerDto().getCUid()==null?"":
					fibuExportDtos[i].getPartnerDto().getCUid().replace(" ", ""));
			} else {
				mt.addParameter(P_ANREDE,"");
				mt.addParameter(P_NAME,"");
				mt.addParameter(P_BEZEICHNUNG,"");
				mt.addParameter(P_STRASSE,"");
				mt.addParameter(P_PLZ,"");
				mt.addParameter(P_ORT,"");
				mt.addParameter(P_BANKKONTO,"");
				mt.addParameter(P_BLZ,"");
				mt.addParameter(P_BANK,"");
				mt.addParameter(P_UST_ID_NUMMER,"");
			}
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
							.getLocMandant(), theClientDto);
			sb.append(sZeile).append(Helper.CR_LF);
		}
		return sb.toString();
	}

	protected String exportiereUebberschrift(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		LpMailText mt = new LpMailText();
		mt.addParameter(P_KONTO,"Konto");
		mt.addParameter(P_ANREDE,"Anrede");
		mt.addParameter(P_NAME,"Name");
		mt.addParameter(P_BEZEICHNUNG,"Bezeichnung");
		mt.addParameter(P_STRASSE,"Strasse");
		mt.addParameter(P_PLZ,"Plz");
		mt.addParameter(P_ORT,"Ort");
		mt.addParameter(P_BANKKONTO,"Bankkonto");
		mt.addParameter(P_BLZ,"Blz");
		mt.addParameter(P_BANK,"Bank");
		mt.addParameter(P_UST_ID_NUMMER,"USt-Id-Nummer");
		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}

	protected String getXSLFile() {
		return XSL_FILE_SCHLEUPEN;
	}

}
