package com.lp.server.finanz.bl;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class FibuSachkontoExportFormatterRZL extends FibuKontoExportFormatterRZL {
	private final static String P_KONTONUMMER = "Kontonummer";
	private final static String P_KONTENART = "Kontenart";
	private final static String P_WAEHRUNG = "Waehrung";
	private final static String P_TEXTZEILE1 = "Textzeile1";
	
	private final static int KONTONUMMER_MAX_STELLEN = 9;

	public FibuSachkontoExportFormatterRZL() {
	}

	@Override
	protected String exportiereDaten(FibuKontoExportDto[] fibuExportDtos, TheClientDto theClientDto)
			throws EJBExceptionLP {
		StringBuilder linesBuilder = new StringBuilder();
		for (FibuKontoExportDto exportDto : fibuExportDtos) {
			KontoDto kontoDto = exportDto.getKontoDto();
			
			LpMailText mt = new LpMailText();
			if (kontoDto.getCNr().length() > KONTONUMMER_MAX_STELLEN) {
				throw createKontonummerZuLangExc(kontoDto);
			}
			mt.addParameter(P_KONTONUMMER, kontoDto.getCNr());
			mt.addParameter(P_KONTENART, getKontenartOfKonto(kontoDto));
			mt.addParameter(P_WAEHRUNG, getWaehrungOfKonto(kontoDto, theClientDto));
			mt.addParameter(P_TEXTZEILE1, kontoDto.getCBez());

			linesBuilder
				.append(transformMailtext(mt, theClientDto))
				.append(Helper.CR_LF);
		}
		
		return linesBuilder.toString();
	}

	private String getWaehrungOfKonto(KontoDto kontoDto, TheClientDto theClientDto) {
		return kontoDto.getWaehrungCNrDruck() != null 
				? kontoDto.getWaehrungCNrDruck() 
						: theClientDto.getSMandantenwaehrung();
	}

	private EJBExceptionLP createKontonummerZuLangExc(KontoDto kontoDto) {
		EJBExceptionLP exc = new EJBExceptionLP(
				EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KONTONUMMER_MAXIMAL_9_STELLEN,
				"Konto '" + kontoDto.getCNr() + "' hat mehr als " + KONTONUMMER_MAX_STELLEN + " Stellen");
		List<Object> infoList = new ArrayList<Object>();
		infoList.add(kontoDto.getCNr());
		infoList.add(kontoDto.getCBez());
		exc.setAlInfoForTheClient(infoList);
		
		return exc;
	}
	
	private String transformMailtext(LpMailText mt, TheClientDto theClientDto) {
		return mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
				.getLocMandant(), theClientDto);
	}

	@Override
	protected String getXSLFile() {
		return XSL_FILE_RZL_SACHKONTO;
	}

	@Override
	protected String exportiereUebberschrift(TheClientDto theClientDto) throws EJBExceptionLP {
		LpMailText mt = new LpMailText();
		mt.addParameter(P_KONTONUMMER, P_KONTONUMMER);
		mt.addParameter(P_KONTENART, P_KONTENART);
		mt.addParameter(P_WAEHRUNG, P_WAEHRUNG);
		mt.addParameter(P_TEXTZEILE1, P_TEXTZEILE1);
		
		return transformMailtext(mt, theClientDto);
	}

}
