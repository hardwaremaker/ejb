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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;

import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;

public abstract class AutomatikjobPDFDruck extends AutomatikjobBasis {
	protected static final Integer EARLIEST_DAYS_BEFORE_ARCHIVE = 14;
	protected static final String PDF_EXTENSION = ".pdf";
	protected static final String JASPER_EXTENSION = ".jasper";
	
	private TheClientDto theClientDto;
	private boolean errorInJob;
	private String filepath;
	
	public AutomatikjobPDFDruck() {
		super();
		errorInJob = false;
	}

	protected boolean isErrorInJob() {
		return errorInJob;
	}

	protected void setErrorInJob() {
		errorInJob = true;
	}
	
	protected void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}
	
	protected TheClientDto getTheClientDto() {
		return theClientDto;
	}
	
	protected abstract void performJobImpl() throws Throwable;
	
	@Override
	public boolean performJob(TheClientDto theClientDto) {
		setTheClientDto(theClientDto);
		
		try {
			myLogger.info("Start of '" + this.getClass().getCanonicalName() + "'");
			performJobImpl();
		} catch (Throwable e) {
			myLogger.error("Error performing Automatikjob", e);
			setErrorInJob();
		} finally {
			myLogger.info("Stop of '" + this.getClass().getCanonicalName() + "' (Error: " + isErrorInJob() + ").");
		}
		
		getTheClientDto().setReportvarianteIId(null);
		return isErrorInJob();
	}

	protected boolean hasAccessToFilepath() {
		File file = new File(getFilepath());
		
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdir()) {
				myLogger.error("Directory \"" + file.getParent() + "\" can not be made!");
				setErrorInJob();
				return false;
			}
		}
		
		if (!file.getParentFile().canWrite()) {
			myLogger.error("Directory \"" + file.getParent() + "\" is not writable!");
			setErrorInJob();
			return false;
		}

		return true;
	}
	
	protected void exportPrint(JasperPrintLP jasperPrint) throws JRException {
		HVPDFExporter exporter = new HVPDFExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint.getPrint());
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(getFilepathNoDuplicate()));
		
		exporter.exportReport();
	}
	
	protected String getFilepath() {
		return filepath;
	}

	protected void setFilepath(String filepath) {
		if (filepath != null && !filepath.endsWith(PDF_EXTENSION)) {
			filepath = filepath + PDF_EXTENSION;
		}
		this.filepath = filepath;
	}
	
	protected void setFilepath(String pattern, Object[] values) {
		MessageFormat msgFormat = new MessageFormat(pattern, getTheClientDto().getLocUi()); 
		setFilepath(msgFormat.format(values));
	}
	
	private String getFilepathNoDuplicate() {
		String path = getFilepath();
		File file = new File(path);
		Integer count = 1;
		while (file.exists()) {
			StringBuilder builder = new StringBuilder(getFilepath());
			builder = builder.insert(getFilepath().indexOf(PDF_EXTENSION), "_" + count);
			file = new File(builder.toString());
			count++;
		}
		
		return file.getAbsolutePath();
	}
	
	/**
	 * Holt alle Reportvarianten und die zugehoerigen Dtos dazu
	 * Fuer den Standard-Druck wird eines erzeugt. Die Reportvariantennamen
	 * werden in die Ressource-Property des Dtos geschrieben.
	 * 
	 * @param reportname Name des Reports
	 * @return Liste aller Reportvarianten inklusive des Standarddrucks
	 */
	protected List<ReportvarianteDto> getReportvarianten(String reportname) {
		List<ReportvarianteDto> varianten = new ArrayList<ReportvarianteDto>();
		
		Map<Integer,String> variantenMap = getDruckerFac().holeAlleVarianten(reportname, getTheClientDto());
		for(Entry<Integer, String> variante : variantenMap.entrySet()) {
			if (variante != null) {
				ReportvarianteDto rvpDto;
				if (variante.getKey() < 0) { 
					//Standarddrucker
					rvpDto = new ReportvarianteDto();
					rvpDto.setCReportname(reportname);
					rvpDto.setCReportnamevariante(reportname);
					rvpDto.setCRessource(getTextRespectUISpr("lp.standard", getTheClientDto().getMandant(),
							getTheClientDto().getLocUi()));
					rvpDto.setIId(null);
				} else {
					rvpDto = getDruckerFac().reportvarianteFindByPrimaryKey(variante.getKey());
					rvpDto.setCRessource(variante.getValue());
				}
				if (rvpDto != null && rvpDto.getCReportname().equals(reportname)) {
					varianten.add(rvpDto);
				}
			}
		}
		
		return varianten;
	}
	
	protected boolean deleteFile(String pfadpattern, Object[] values) {
		setFilepath(pfadpattern, values);
		File file = new File(getFilepath());
		
		return file.delete();
	}

	protected Object[] buildPatternValues(java.sql.Date cuttedNow, ReportvarianteDto variante) {
		String reportname = variante.getCReportnamevariante();
		if (reportname != null && reportname.endsWith(JASPER_EXTENSION)) {
			reportname = reportname.substring(0, reportname.length() - JASPER_EXTENSION.length());
		}
		return new Object[]{cuttedNow, variante.getCRessource(), reportname};
	}
	
	protected void deleteOldPdfs(java.sql.Date cuttedNow, 
			List<ReportvarianteDto> reportVarianten, String pfadPattern, Integer archivierungsTage) {
		if(isErrorInJob()) return;
		
		for (ReportvarianteDto variante : reportVarianten) {
			deleteOldVersionsOfReport(pfadPattern, 
					buildPatternValues(cuttedNow, variante), archivierungsTage);
		}
	}
	
	private void deleteOldVersionsOfReport(String pfadpattern,
			Object[] values, Integer archivierungstage) {
		Date baseDate = (Date) values[0];
		for (int day = 0; day < EARLIEST_DAYS_BEFORE_ARCHIVE; day++) {
			Date date = Helper.addiereTageZuDatum(baseDate, (archivierungstage*-1) - day);
			values[0] = date;
			deleteFile(pfadpattern, values);
		}
	}
}
