
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lp.server.artikel.service.LumiQuoteArtikelDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.system.service.JobDetailsAuslieferlisteDto;
import com.lp.server.system.service.JobDetailsLumiquoteDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;
import com.lp.util.csv.LPCSVWriter;

public class AutomatikjobLumiquote extends AutomatikjobPDFDruck {


	public AutomatikjobLumiquote() {
		super();
	}

	@Override
	public void performJobImpl() throws Throwable {

		JobDetailsLumiquoteDto jobAuslieferlisteDto = getAutoLumiquoteFac()
				.findByMandantCNr(getTheClientDto().getMandant());
		if (!hasJobValidProperties(jobAuslieferlisteDto)) {
			setErrorInJob();
			return;
		}

		

		String cPfad=jobAuslieferlisteDto.getCPfad();
		
		ArrayList<LumiQuoteArtikelDto> alDaten =getArtikelServiceFac()
				.getArtikelDatenForLumiquote(jobAuslieferlisteDto.getCArtikelfilter(),getTheClientDto());

		if (alDaten.size() > 0) {

			
	        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF }; 
			
			File file = new File(cPfad);

			OutputStream os = new FileOutputStream(file);
			os.write(bom);
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            LPCSVWriter writerCsv = new LPCSVWriter(osw, ',',
                    LPCSVWriter.DEFAULT_QUOTE_CHARACTER);
            
			writerCsv.writeNext(alDaten.get(0).getColumnNames());

			for (int i = 0; i < alDaten.size(); i++) {

				writerCsv.writeNext(alDaten.get(i).zeileToStringArray());

			}

			writerCsv.close();

			

		}
		

	}

	

	private boolean hasJobValidProperties(JobDetailsLumiquoteDto jobDto) {
		if (jobDto == null) {
			myLogger.error("Autojob Lumiquote not found in DB for Mandant " + getTheClientDto().getMandant());
			return false;
		}

		if (jobDto.getCPfad() == null) {

			myLogger.error(
					"Autojob Lumiquote Property Pfad not set");
			return false;
		}
		return true;
	}

}