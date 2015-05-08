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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuExportFormatterSchleupen extends FibuExportFormatter {

	
	private final static String P_BELEGDATUM ="Belegdatum";
	private final static String P_FAELLIG ="Faellig";
	private final static String P_KONTO="Konto";
	private final static String P_GEGENKONTO="Gegenkonto";
	private final static String P_BELEG="Beleg";
	private final static String P_KST="Kst";
	private final static String P_TEXT="Text";
	private final static String P_SOLL = "Soll";
	private final static String P_HABEN = "Haben";

	private Parameterkonten kontenIgErwerb = new Parameterkonten(Parameterkonten.IG_ERWERB);
	private Parameterkonten kontenReverseCharge = new Parameterkonten(Parameterkonten.REVERSE_CHARGE);
	
	protected FibuExportFormatterSchleupen(
			FibuExportKriterienDto exportKriterienDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		super(exportKriterienDto, theClientDto);
	}

	protected String exportiereDaten(FibuexportDto[] fibuExportDtos)
	throws EJBExceptionLP {
		StringBuffer sb = new StringBuffer();
		HashMap<Integer, BigDecimal> hmUstBetraege = new HashMap<Integer, BigDecimal>();
		ArrayList<Integer> alKontoId = new ArrayList<Integer>();
		HashMap<Integer, BigDecimal> hmVstBetraege = new HashMap<Integer, BigDecimal>();
		ArrayList<Integer> alKontoIdVst = new ArrayList<Integer>();
		if(fibuExportDtos.length>1){
			String sTexte = "";
			if(!fibuExportDtos[0].getBelegart().equals("ER")){
				for(int y=1;y<fibuExportDtos.length;y++){
					if(!sTexte.contains(fibuExportDtos[y].getText())){
						if(sTexte.equals("")){
							sTexte = fibuExportDtos[y].getText();
						} else {
							sTexte += ", " + fibuExportDtos[y].getText();
						}
					}
					fibuExportDtos[y].setText(fibuExportDtos[0].getText());
				}
				fibuExportDtos[0].setText(sTexte);
			}
		}
		
		boolean bInnerGemeinschaftlich = false;
		if (fibuExportDtos != null) {
			Integer finanzamtPartnerIId = pruefeFinanzamt(fibuExportDtos);
			// UST-LKZ des Partners bestimmen
			String sPartnerUstLkz = null;
			try {
				sPartnerUstLkz = getPartnerUstLkz(fibuExportDtos);
				// Zuerst: Richtigen UST-Prozentsatz und Finanzamt bestimmen.
				PartnerDto partnerDtoFinanzamt = null;
				// FA holen, wenn eines zugeordnet werden konnte.
				if (finanzamtPartnerIId != null) {
					partnerDtoFinanzamt = super.getFinanzamtPartner(finanzamtPartnerIId);
				}
				boolean bFinanzamtImPartnerLand = isFinanzamtImPartnerland(
						sPartnerUstLkz, partnerDtoFinanzamt);
				// feststellen, ob es sich um IG Erwerb / Erloes handelt
				bInnerGemeinschaftlich = isInnergemeinschaftlich(fibuExportDtos, bFinanzamtImPartnerLand);
			} catch (RemoteException e) {
				//		
			}
		}
		
		Integer iKontoVstIId = null;
		for(int i=0;i<fibuExportDtos.length;i++){
			Integer iKontoUstIId = fibuExportDtos[i].getKontoDto().getKontoIIdWeiterfuehrendUst();
			
			if((fibuExportDtos[i].getHabenbetrag()!="" && !fibuExportDtos[i].getBelegart().equals("ER")) || 
					(fibuExportDtos[i].getBelegart().equals("ER") && fibuExportDtos[i].getSollbetrag()!="")){
				
				boolean hasZusaetzlicheBuchungen = true ;
				
				if (fibuExportDtos[i].isBReverseCharge()) {
					// Reverse Charge Konto suchen
					MwstsatzbezDto mwstbezDto = fibuExportDtos[i].getMwstsatz().getMwstsatzbezDto();
					if (fibuExportDtos[i].getMwstsatz().getMwstsatzbezDto() == null) {
						try {
							mwstbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(fibuExportDtos[i].getMwstsatz().getIIMwstsatzbezId(), theClientDto);
						} catch (RemoteException e) {
							//
						}
					} 
					iKontoUstIId = kontenReverseCharge.getUstKonto(mwstbezDto.getCDruckname());
					iKontoVstIId = kontenReverseCharge.getVstKonto(mwstbezDto.getCDruckname());
					if (iKontoUstIId == null || iKontoVstIId == null)
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_REVERSECHARGE, 
								"Definieren Sie bitte die Steuerkonten f\u00FCr Reverse Charge in den Parametern.");
				} else if (bInnerGemeinschaftlich) {
					// IG Erwerb Konto suchen
					MwstsatzbezDto mwstbezDto = fibuExportDtos[i].getMwstsatz().getMwstsatzbezDto();
					if (fibuExportDtos[i].getMwstsatz().getMwstsatzbezDto() == null) {
						try {
							mwstbezDto = getMandantFac().mwstsatzbezFindByPrimaryKey(fibuExportDtos[i].getMwstsatz().getIIMwstsatzbezId(), theClientDto);
							
							if(!"ER".equals(fibuExportDtos[i].getBelegart()) && 
									FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTOHNE.equals(mwstbezDto.getCDruckname())) {
								hasZusaetzlicheBuchungen = false ;
							}
						} catch (RemoteException e) {
							//
						}
					} 
					iKontoUstIId = kontenIgErwerb.getUstKonto(mwstbezDto.getCDruckname());
					iKontoVstIId = kontenIgErwerb.getVstKonto(mwstbezDto.getCDruckname());
					
					if (hasZusaetzlicheBuchungen && (iKontoUstIId == null || iKontoVstIId == null))
						throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_IGERWERB, 
								"Definieren Sie bitte die Steuerkonten f\u00FCr IG Erwerb in den Parametern.");
				} else {
					if(iKontoUstIId==null){
						EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_UST_KONTO_DEFINIERT,"");
						ArrayList<Object> alInfo = new ArrayList<Object>();
						alInfo.add(fibuExportDtos[i].getKontoDto().getCNr());
						ex.setAlInfoForTheClient(alInfo);
						throw ex;
					}
				}
				if(hasZusaetzlicheBuchungen) {
					addToHashMap(hmUstBetraege,
							alKontoId, iKontoUstIId, fibuExportDtos[i].getSteuerBD());
					if (bInnerGemeinschaftlich) {
						//VST Buchung bei IG Erwerb
						addToHashMap(hmVstBetraege, alKontoIdVst, iKontoVstIId, fibuExportDtos[i].getSteuerBD());
					}
				}
			} else {
				if(fibuExportDtos[i].getBelegart().equals("ER")){
					fibuExportDtos[i].setGkto(fibuExportDtos[i].getGegenkontoDto());
				} else {
					fibuExportDtos[i].setGkto(null);
					//Hier kommen wir nur beim Debitorenkonto rein
					//Also alle anderen Texte hier als Text hinterlegen
				}
			}
			LpMailText mt = new LpMailText();
			mt.addParameter(P_BELEGDATUM ,fibuExportDtos[i].getBelegdatum().toString().replace("-", ""));
			mt.addParameter(P_FAELLIG ,"");
			mt.addParameter(P_KONTO,fibuExportDtos[i].getKontonummer());
			mt.addParameter(P_GEGENKONTO,fibuExportDtos[i].getGegenkonto());
			mt.addParameter(P_BELEG,fibuExportDtos[i].getBelegnummer().replace("/", ""));
			mt.addParameter(P_KST,fibuExportDtos[i].getKostenstelle());
			mt.addParameter(P_TEXT,fibuExportDtos[i].getText());
			mt.addParameter(P_SOLL, fibuExportDtos[i].getSollbetrag());
			mt.addParameter(P_HABEN, fibuExportDtos[i].getHabenbetrag());


			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
					.getLocMandant(), theClientDto);
			sb.append(sZeile).append(Helper.CR_LF);
			
		}
		//Jetzt noch die UST-Zeilen bilden
		for(int y=0;y<alKontoId.size();y++){
			try {
				Integer iBuchung = 1;
				if(fibuExportDtos[0].getBelegart().equals("RE")){
					iBuchung=0;
				}
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(alKontoId.get(y));
				LpMailText mt = new LpMailText();
				mt.addParameter(P_BELEGDATUM ,fibuExportDtos[iBuchung].getBelegdatum().toString().replace("-", ""));
				mt.addParameter(P_FAELLIG ,"");
				mt.addParameter(P_KONTO,kontoDto.getCNr());
				mt.addParameter(P_GEGENKONTO,fibuExportDtos[iBuchung].getGegenkonto());
				mt.addParameter(P_BELEG,fibuExportDtos[iBuchung].getBelegnummer().replace("/", ""));
				mt.addParameter(P_KST,fibuExportDtos[iBuchung].getKostenstelle());
				mt.addParameter(P_TEXT,fibuExportDtos[iBuchung].getText());
				BigDecimal bdBetrag = hmUstBetraege.get(kontoDto.getIId());
				if (fibuExportDtos[iBuchung].getBelegart().equals("ER")) {
					mt.addParameter(P_SOLL, FibuexportDto.formatNumber(bdBetrag));
					mt.addParameter(P_HABEN,"");
				} else {
					mt.addParameter(P_SOLL, "");
					mt.addParameter(P_HABEN,FibuexportDto.formatNumber(bdBetrag));
				}
				String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
						theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
				sb.append(sZeile).append(Helper.CR_LF);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}

		//Jetzt noch die VST-Zeilen bilden
		for(int y=0; y<alKontoIdVst.size(); y++){
			try {
				Integer iBuchung = 1;
				if(fibuExportDtos[0].getBelegart().equals("RE")){
					iBuchung=0;
				}
				KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(alKontoIdVst.get(y));
				LpMailText mt = new LpMailText();
				mt.addParameter(P_BELEGDATUM ,fibuExportDtos[iBuchung].getBelegdatum().toString().replace("-", ""));
				mt.addParameter(P_FAELLIG ,"");
				mt.addParameter(P_KONTO,kontoDto.getCNr());
				mt.addParameter(P_GEGENKONTO,fibuExportDtos[iBuchung].getGegenkonto());
				mt.addParameter(P_BELEG,fibuExportDtos[iBuchung].getBelegnummer().replace("/", ""));
				mt.addParameter(P_KST,fibuExportDtos[iBuchung].getKostenstelle());
				mt.addParameter(P_TEXT,fibuExportDtos[iBuchung].getText());
				BigDecimal bdBetrag = hmVstBetraege.get(kontoDto.getIId());
				if (fibuExportDtos[iBuchung].getBelegart().equals("ER")) {
					mt.addParameter(P_SOLL,"");
					mt.addParameter(P_HABEN, FibuexportDto.formatNumber(bdBetrag));
				} else {
					mt.addParameter(P_SOLL,FibuexportDto.formatNumber(bdBetrag));
					mt.addParameter(P_HABEN, "");
				}
				String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
						theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
				sb.append(sZeile).append(Helper.CR_LF);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
		}
		
		return sb.toString();
	}

	private void addToHashMap(HashMap<Integer, BigDecimal> hmBetrag, ArrayList<Integer> alKonto, Integer kontoIId, BigDecimal betrag) {
		if(hmBetrag.get(kontoIId)==null){
			BigDecimal bdSumme = new BigDecimal(0);
			if(betrag !=null){
				bdSumme = bdSumme.add(betrag);
			}
			hmBetrag.put(kontoIId, bdSumme);
			alKonto.add(kontoIId);
		} else {
			BigDecimal bdSumme = (BigDecimal) hmBetrag.get(kontoIId);
			if(betrag!=null){
				bdSumme = bdSumme.add(betrag);
			}
			hmBetrag.put(kontoIId, bdSumme);
		}
	}

	private Integer getKontoIIdFromCnr(String kontoCNr) {
		KontoDto kontoDto = null;
		try {
			kontoDto = getFinanzFac().kontoFindByCnrKontotypMandantOhneExc(kontoCNr, 
					FinanzServiceFac.KONTOTYP_SACHKONTO, theClientDto.getMandant(), theClientDto);
		} catch (RemoteException e) {
			//
		}
		if (kontoDto == null) {
			EJBExceptionLP ex = new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_KEIN_KONTO_IGERWERB,
					"Bitte korrigieren Sie die Kontodefiniton f\u00FCr IG-Erwerb in den Mandantenparametern.");
			ArrayList<Object> alInfo = new ArrayList<Object>();
			alInfo.add(kontoCNr);
			ex.setAlInfoForTheClient(alInfo);
			throw ex;
		} else {
			return kontoDto.getIId();
		}
	}

	protected String exportiereUeberschrift() throws EJBExceptionLP {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_BELEGDATUM ,P_BELEGDATUM);
		mt.addParameter(P_FAELLIG ,P_FAELLIG);
		mt.addParameter(P_KONTO,P_KONTO);
		mt.addParameter(P_GEGENKONTO,P_GEGENKONTO);
		mt.addParameter(P_BELEG,P_BELEG);
		mt.addParameter(P_KST,P_KST);
		mt.addParameter(P_TEXT,P_TEXT);
		mt.addParameter(P_SOLL,P_SOLL);
		mt.addParameter(P_HABEN, P_HABEN);
		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}

	protected String getXSLFile() {
		return XSL_FILE_SCHLEUPEN;
	}

	protected String uebersetzeUSTLand(String slkz) {
		return "";
	}

	private class Parameterkonten {

		//Parameterart
		public final static int IG_ERWERB = 0;
		public final static int REVERSE_CHARGE = 1;
		
		private final static int NORMALSATZ = 0;
		private final static int REDUZIERTERSATZ = 1;

		private Integer[] ustKontoIId = new Integer[2];
		private Integer[] vstKontoIId = new Integer[2];
		private int parameterart;
		
		public Parameterkonten(int parameterart) {
			this.parameterart = parameterart;
			String kontoUstCNr = null;
			String kontoVstCNr = null;
			try {
				switch (parameterart) {
				case IG_ERWERB:
					kontoUstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_UST_IGERWERB_NORMALSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					kontoVstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_VST_IGERWERB_NORMALSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					break;
				case REVERSE_CHARGE:
					kontoUstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_UST_REVERSECHARGE_NORMALSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					kontoVstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_VST_REVERSECHARGE_NORMALSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					break;
				}
			} catch (RemoteException e) {
				//
			}
			if (!kontoUstCNr.equals("0")) 
				ustKontoIId[NORMALSATZ] = getKontoIIdFromCnr(kontoUstCNr);
			if (!kontoVstCNr.equals("0")) 
				vstKontoIId[NORMALSATZ] = getKontoIIdFromCnr(kontoVstCNr);

			try {
				switch (parameterart) {
				case IG_ERWERB:
					kontoUstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_UST_IGERWERB_REDUZIERTSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					kontoVstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_VST_IGERWERB_REDUZIERTSATZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
				break ;
				case REVERSE_CHARGE:
					kontoUstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_UST_REVERSECHARGE_REDUZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
					kontoVstCNr = getParameterFac().parametermandantFindByPrimaryKey(
							ParameterFac.PARAMETER_FINANZ_EXPORT_VST_REVERSECHARGE_REDUZ_KONTO,
							ParameterFac.KATEGORIE_FINANZ, theClientDto.getMandant()).getCWert();
				break ;
				}	
			} catch (RemoteException e) {
				//
			}

			if (!kontoUstCNr.equals("0")) 
				ustKontoIId[REDUZIERTERSATZ] = getKontoIIdFromCnr(kontoUstCNr);
			if (!kontoVstCNr.equals("0")) 
				vstKontoIId[REDUZIERTERSATZ] = getKontoIIdFromCnr(kontoVstCNr);
		}
		
		public Integer getUstKonto(String mwstsatzbezDruckname) {
			if (mwstsatzbezDruckname.equals(FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTNORMAL))
				return ustKontoIId[NORMALSATZ];
			if (mwstsatzbezDruckname.equals(FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTRED))
				return ustKontoIId[REDUZIERTERSATZ];
			return null;
		}

		public Integer getVstKonto(String mwstsatzbezDruckname) {
			if (mwstsatzbezDruckname.equals(FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTNORMAL))
				return vstKontoIId[NORMALSATZ];
			if (mwstsatzbezDruckname.equals(FibuExportFac.MWSTSATZBEZ_DRUCKNAME_USTRED))
				return vstKontoIId[REDUZIERTERSATZ];
			return null;
		}

		public int getParameterart() {
			return parameterart;
		}

	}
}

