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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den FibuExport
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 09.02.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: Gerold $
 *         </p>
 * 
 * @version not attributable Date $Date: 2013/02/08 13:49:05 $
 */
abstract class FibuExportFormatter extends Facade {
	protected final static String XSL_FILE_CSV = "fb_export_csv";
	protected final static String XSL_FILE_RZL = "fb_export_rzl";
	protected final static String XSL_FILE_BMD = "fb_export_bmd";
	protected final static String XSL_FILE_ABACUS = "fb_export_abacus";
	protected final static String XSL_FILE_ABACUS_TA005 = "_ta005";
	protected final static String XSL_FILE_ABACUS_TA100 = "_ta100";
	protected final static String XSL_FILE_ABACUS_TA101 = "_ta101";
	protected final static String XSL_FILE_ABACUS_TA102 = "_ta102";
	protected final static String XSL_FILE_ABACUS_TA920 = "_ta920";
	protected final static String XSL_FILE_ABACUS_TA922 = "_ta922";
	protected final static String XSL_FILE_ABACUS_TA995 = "_ta995";
	protected final static String XSL_FILE_SCHLEUPEN = "fb_export_schleupen";
	protected final static String XSL_FILE_DATEV = "fb_export_datev-csv";
	protected final static String XSL_FILE_DATEV_EXTF = "fb_export_datev-extf";
		
	protected final TheClientDto theClientDto;
	protected final FibuExportKriterienDto exportKriterienDto;
	protected final Map<Integer, PartnerDto> mFinanzamtPartner;
	protected final MandantDto mandantDto;

	protected FibuExportFormatter(FibuExportKriterienDto exportKriterienDto,
			TheClientDto theClientDto) throws EJBExceptionLP {
		this.theClientDto = theClientDto;
		this.exportKriterienDto = exportKriterienDto;
		this.mFinanzamtPartner = holeFinanzaemter();
		this.mandantDto = holeMandant();
	}

	protected abstract String exportiereDaten(FibuexportDto[] fibuExportDtos)
			throws EJBExceptionLP;

	protected abstract String getXSLFile();

	protected abstract String exportiereUeberschrift() throws EJBExceptionLP;

	protected abstract String uebersetzeUSTLand(String sLKZ);

	private Map<Integer, PartnerDto> holeFinanzaemter() {
		try {
			FinanzamtDto[] finanzaemter = getFinanzFac().finanzamtFindAll(
					theClientDto);
			Map<Integer, PartnerDto> map = new HashMap<Integer, PartnerDto>();
			for (int i = 0; i < finanzaemter.length; i++) {
				FinanzamtDto fa = finanzaemter[i];
				PartnerDto partnerDto = getPartnerFac()
						.partnerFindByPrimaryKey(fa.getPartnerIId(),
								theClientDto);
				// in die Map geben. Key ist die PartnerIId
				map.put(fa.getPartnerIId(), partnerDto);
			}
			return map;
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	private MandantDto holeMandant() throws EJBExceptionLP {
		try {
			return getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}

	protected final PartnerDto getFinanzamtPartner(Integer partnerIIdFinanzamt) {
		return mFinanzamtPartner.get(partnerIIdFinanzamt);
	}

	protected final Integer pruefeFinanzamt(FibuexportDto[] fibuExportDtos)
			throws EJBExceptionLP {
		
		// Finanzamt der Buchungen pruefen
		boolean hatFibu = getMandantFac().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_FINANZBUCHHALTUNG, theClientDto);

		Integer finanzamtPartnerIId = null;
		ArrayList<Integer> al = new ArrayList<Integer>();
		
		for (int i = 0; i < fibuExportDtos.length; i++) {
			// alle beteiligten Finanzaemter sammeln
			// PJ 17079: wenn keine Fibu wird das abweichende Ust Land nicht ueber das Finanzamt gesteuert, 
			// 			 daher Kreditoren und Debitorenkonten nicht beruecksichtigen
			if (fibuExportDtos[i].getKontoDto() != null)
				if (fibuExportDtos[i].getKontoDto().getFinanzamtIId() != null)
					if (hatFibu)
						al.add(fibuExportDtos[i].getKontoDto().getFinanzamtIId());
					else if (fibuExportDtos[i].getKontoDto().getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO))
						al.add(fibuExportDtos[i].getKontoDto().getFinanzamtIId());

			if (fibuExportDtos[i].getGegenkontoDto() != null)
				if (fibuExportDtos[i].getGegenkontoDto().getFinanzamtIId() != null)
					if (hatFibu)
						al.add(fibuExportDtos[i].getGegenkontoDto().getFinanzamtIId());
					else if (fibuExportDtos[i].getGegenkontoDto().getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_SACHKONTO))
						al.add(fibuExportDtos[i].getGegenkontoDto().getFinanzamtIId());
		}
		finanzamtPartnerIId = al.get(0);
		for (int i=1; i < al.size(); i++) {
			// Finanzaemter vergleichen
			if (al.get(i).compareTo(finanzamtPartnerIId) != 0) {
				String msg = "";
				try {
					msg = "Mehrere Finanz\u00E4mter in " + fibuExportDtos[0].getBelegart() + fibuExportDtos[0].getBelegnummer();
				} catch (Exception e) {
					msg = "Mehrere Finanz\u00E4mter in " + fibuExportDtos[0].getBelegnummer();
				}
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER, msg);
			}
		}
/*		
		for (int i = 0; i < fibuExportDtos.length; i++) {
			// wenn ich noch keins hab, nehm ich mal das erstbeste
			if (finanzamtPartnerIId == null) {
				if (fibuExportDtos[i].getKontoDto() != null
						&& fibuExportDtos[i].getKontoDto().getFinanzamtIId() != null) {
					finanzamtPartnerIId = fibuExportDtos[i].getKontoDto()
							.getFinanzamtIId();
				}
				if (finanzamtPartnerIId == null && // noch nicht durch das erste
						// Konto definiert
						fibuExportDtos[i].getGegenkontoDto() != null &&
						// Hier zieht das Land des eventuell bei einem der
						// Konten definierten Finanzamts
						fibuExportDtos[i].getGegenkontoDto().getFinanzamtIId() != null) {
					finanzamtPartnerIId = fibuExportDtos[i].getGegenkontoDto()
							.getFinanzamtIId();
				}
			}
			// wenn schon, dann vergleich noch mit allen weiteren konten
			else {
				if (fibuExportDtos[i].getKontoDto() != null
						&& fibuExportDtos[i].getKontoDto().getFinanzamtIId() != null) {
					if (!finanzamtPartnerIId.equals(fibuExportDtos[i]
							.getKontoDto().getFinanzamtIId())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER,
								new Exception());
					}
				}
				if (finanzamtPartnerIId == null && // noch nicht durch das erste
						// Konto definiert
						fibuExportDtos[i].getGegenkontoDto() != null &&
						// Hier zieht das Land des eventuell bei einem der
						// Konten definierten Finanzamts
						fibuExportDtos[i].getGegenkontoDto().getFinanzamtIId() != null) {
					if (!finanzamtPartnerIId.equals(fibuExportDtos[i]
							.getGegenkontoDto().getFinanzamtIId())) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_FINANZ_EXPORT_MEHRERE_FINANZAEMTER,
								new Exception());
					}
				}
			}
		}
*/
		return finanzamtPartnerIId;
	}

	protected final String getPartnerUstLkz(FibuexportDto[] fibuExportDtos)
			throws RemoteException, EJBExceptionLP {
		final String sPartnerUstLkz;
		// 20080325 MR: Auf Abweichendes UstLand != Land des Mandanten
		// vergleichen
		if (fibuExportDtos[0].getPartnerDto().getLandIIdAbweichendesustland() != null
				&& !(fibuExportDtos[0].getPartnerDto()
						.getLandIIdAbweichendesustland().equals(mandantDto
						.getPartnerDto().getLandplzortDto().getIlandID()))) {
			LandDto landDto = getSystemFac().landFindByPrimaryKey(
					fibuExportDtos[0].getPartnerDto()
							.getLandIIdAbweichendesustland());
			sPartnerUstLkz = landDto.getCLkz();
		} else {
			if(fibuExportDtos[0].getPartnerDto() == null ||
					fibuExportDtos[0].getPartnerDto().getLandplzortDto() == null ||
					fibuExportDtos[0].getPartnerDto().getLandplzortDto().getLandDto() == null) {
				System.out.println("Partner IId " + fibuExportDtos[0].getPartnerDto().getIId() + " hat kein Land") ;
			}
			sPartnerUstLkz = fibuExportDtos[0].getPartnerDto()
					.getLandplzortDto().getLandDto().getCLkz();
		}
		return sPartnerUstLkz;
	}

	protected final boolean isInnergemeinschaftlich(
			FibuexportDto[] fibuExportDtos, boolean bFinanzamtImPartnerLand) {
		boolean bInnerGemeinschaftlich = fibuExportDtos[0].getLaenderartCNr()
				.equals(FinanzFac.LAENDERART_EU_AUSLAND_MIT_UID)
				|| fibuExportDtos[0].getLaenderartCNr().equals(
						FinanzFac.LAENDERART_EU_AUSLAND_OHNE_UID);
		// wenn Partner und FA im gleiche Land sind, dann ist da IMMER wie
		// Inland zu betrachten
		if (bFinanzamtImPartnerLand) {
			bInnerGemeinschaftlich = false;
			for (int i = 0; i < fibuExportDtos.length; i++) {
				fibuExportDtos[i].setLaenderartCNr(FinanzFac.LAENDERART_INLAND);
			}
		}
		return bInnerGemeinschaftlich;
	}

	protected final boolean isFinanzamtImPartnerland(String sPartnerUstLkz,
			PartnerDto partnerDtoFinanzamt) {
		boolean bFinanzamtImPartnerLand = false;
		if (partnerDtoFinanzamt != null
				&& partnerDtoFinanzamt.getLandplzortDto().getLandDto()
						.getCLkz().equals(sPartnerUstLkz)) {
			bFinanzamtImPartnerLand = true;
		}
		return bFinanzamtImPartnerLand;
	}
	
	protected final boolean isER(FibuexportDto fibuexportDto) {
		return FibuExportManager.BELEGART_ER.equals(fibuexportDto.getBelegart());
	}
	
	protected final boolean isAR(FibuexportDto fibuexportDto) {
		return FibuExportManager.BELEGART_AR.equals(fibuexportDto.getBelegart());
	}
	
	protected final boolean isGS(FibuexportDto fibuexportDto) {
		return FibuExportManager.BELEGART_GS.equals(fibuexportDto.getBelegart());
	}
	
	protected final boolean isARorGS(FibuexportDto fibuexportDto) {
		return isAR(fibuexportDto) || isGS(fibuexportDto);
	}
	
}
