package com.lp.server.system.automatikjob;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.fertigung.service.VendidataArticleExportResult;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.server.system.service.JobDetails4VendingExportDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.EJBExceptionLP;

public class Automatikjob4VendingExport extends AutomatikjobBasis {
	private final static int ALL = 0;
	private final static int ARTIKEL = 1;
	private final static int KUNDEN = 2;
	private final static int LIEFERANTEN = 3;
	private JobDetails4VendingExportDto jobDetailsDto;
	private TheClientDto theClientDto;
	private final String fileextension = ".xml";
	private Map<Integer, String> exportMessages;
	private boolean bArtikelError;
	private boolean bKundenError;
	private boolean bLieferantenError;

	public Automatikjob4VendingExport() {
		super();
		exportMessages = new HashMap<Integer, String>();
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		if (!initJobDetailsJobDto() || !darfAutomatikjobDurchfuehren()) {
			sendAutojobResultMessage();
			return false;
		}
		
		bArtikelError = perform4VendingArtikelExport();
		bKundenError = perform4VendingKundenExport();
		bLieferantenError = perform4VendingLieferantenExport();
		
		sendAutojobResultMessage();
		return bArtikelError || bKundenError || bLieferantenError;
	}

	private boolean darfAutomatikjobDurchfuehren() {
		if (!getMandantFac().hatZusatzfunktion4Vending(theClientDto)) {
			myLogger.error("Keine Zusatzfunktionsberechtigung f\u00FCr 4Vending Export");
			addErrorMessage(ALL, "Keine Zusatzfunktionsberechtigung f\u00FCr 4Vending Export");
			return false;
		}
		return true;
	}

	private void sendAutojobResultMessage() {
		if (!exportMessages.isEmpty()) {
			if (jobDetailsDto.getCEmailFehler() != null) {
				sendMessage(jobDetailsDto.getCEmailFehler(), 
						"Autojob 4Vending Export fehlgeschlagen", getMessage());
			}
			sendMessage("support@heliumv.com", "Autojob 4Vending Export fehlgeschlagen", getMessage());
		} else {
			if (jobDetailsDto.getCEmailErfolgreich() != null) {
				sendMessage(jobDetailsDto.getCEmailErfolgreich(), 
						"Autojob 4Vending Export erfolgreich durchgef\u00FChrt", "");
			}
		}
	}

	private boolean initJobDetailsJobDto() {
		jobDetailsDto = getJob4VendingExportFac().findByMandantCNr(theClientDto.getMandant());
		if (jobDetailsDto == null) {
			myLogger.error("Autojob 4Vending Export not found in DB");
			addErrorMessage(ALL, "Autojob 4Vending Export not found in DB");
			return false;
		}
		return true;
	}
	
	private boolean perform4VendingArtikelExport() {
		if (!jobDetailsDto.getBArtikel()) return false;
		myLogger.info("Performing 4Vending Artikel Export");
		if (jobDetailsDto.getCPfadPatternArtikel() == null) {
			myLogger.error("Path for 4Vending Artikel Export not set");
			addErrorMessage(ARTIKEL, "Es wurde kein Pfad f\u00FCr den Artikel Export angegeben.");
			return true;
		}
		
		try {
			VendidataArticleExportResult result = getArtikelFac().exportiere4VendingArtikel(true, theClientDto);
			if (hasErrors(result.getExportErrors())) {
				addErrorMessage(ARTIKEL, 
						"Einem oder mehreren Artikeln fehlen für den Export verbindliche Daten. F\u00FCr genauere Informationen "
						+ "f\u00FChren Sie bitte im Artikel-Modul den 4Vending-Export manuell aus.");
				return true;
			}
			result = getArtikelFac().exportiere4VendingArtikel(false, theClientDto);
			exportToFile(result.getXmlContent(), jobDetailsDto.getCPfadPatternArtikel());
		} catch (Throwable t) {
			myLogger.error("Autojob 4Vending Artikel Export failed", t);
			addErrorMessage(ARTIKEL, "Unexpected Exception thrown. See server log for details.");
			return true;
		}
		
		return false;
	}
	
	private boolean perform4VendingKundenExport() {
		if (!jobDetailsDto.getBKunden()) return false;
		myLogger.info("Performing 4Vending Kunden Export");
		if (jobDetailsDto.getCPfadPatternKunden() == null) {
			myLogger.error("Path for 4Vending Kunden Export not set");
			addErrorMessage(KUNDEN, "Es wurde kein Pfad f\u00FCr den Kunden Export angegeben.");
			return true;
		}
		
		try {
			VendidataPartnerExportResult result = getKundeFac().exportiere4VendingKunden(true, theClientDto);
			if (hasErrors(result.getExportErrors())) {
				addErrorMessage(KUNDEN, 
						"Einem oder mehreren Kunden fehlen für den Export verbindliche Daten. F\u00FCr genauere Informationen "
						+ "f\u00FChren Sie bitte im Kunden-Modul den 4Vending-Export manuell aus.");
				return true;
			}
			result = getKundeFac().exportiere4VendingKunden(false, theClientDto);
			exportToFile(result.getXmlContent(), jobDetailsDto.getCPfadPatternKunden());
		} catch (Throwable t) {
			myLogger.error("Autojob 4Vending Kunden Export failed", t);
			addErrorMessage(KUNDEN, "Unexpected Exception thrown. See server log for details.");
			return true;
		}
		return false;
	}

	private boolean perform4VendingLieferantenExport() {
		if (!jobDetailsDto.getBLieferanten()) return false;
		myLogger.info("Performing 4Vending Lieferanten Export");
		if (jobDetailsDto.getCPfadPatternLieferanten() == null) {
			myLogger.error("Path for 4Vending Lieferanten Export not set");
			addErrorMessage(LIEFERANTEN, "Es wurde kein Pfad f\u00FCr den Lieferanten Export angegeben.");
			return true;
		}
		
		try {
			VendidataPartnerExportResult result = getLieferantFac().exportiere4VendingLieferanten(true, theClientDto);
			if (hasErrors(result.getExportErrors())) {
				addErrorMessage(LIEFERANTEN, 
						"Einem oder mehreren Lieferanten fehlen für den Export verbindliche Daten. F\u00FCr genauere Informationen "
						+ "f\u00FChren Sie bitte im Kunden-Modul den 4Vending-Export manuell aus.");
				return true;
			}
			result = getLieferantFac().exportiere4VendingLieferanten(false, theClientDto);
			exportToFile(result.getXmlContent(), jobDetailsDto.getCPfadPatternLieferanten());
		} catch (Throwable t) {
			myLogger.error("Autojob 4Vending Lieferanten Export failed", t);
			addErrorMessage(LIEFERANTEN, "Unexpected Exception thrown. See server log for details.");
			return true;
		}
		return false;
	}

	private void addErrorMessage(int key, String errorText) {
		exportMessages.put(key, errorText);
	}

	private String getMessage() {
		StringBuilder builder = new StringBuilder();
		if (exportMessages.containsKey(ALL)) {
			return "4Vending Export fehlgeschlagen: " + exportMessages.get(ALL);
		}
		if (bArtikelError) {
			builder.append("4Vending Artikel Export fehlgeschlagen: ").append(exportMessages.get(ARTIKEL)).append("\n");
		} else {
			builder.append("4Vending Artikel Export erfolgreich durchgef\u00FChrt").append("\n");
		}
		if (bKundenError) {
			builder.append("4Vending Kunden Export fehlgeschlagen: ").append(exportMessages.get(KUNDEN)).append("\n");
		} else {
			builder.append("4Vending Kunden Export erfolgreich durchgef\u00FChrt").append("\n");
		}
		if (bLieferantenError) {
			builder.append("4Vending Lieferanten Export fehlgeschlagen: ").append(exportMessages.get(LIEFERANTEN)).append("\n");
		} else {
			builder.append("4Vending Lieferanten Export erfolgreich durchgef\u00FChrt").append("\n");
		}

		return builder.toString();
	}

	private void sendMessage(String recipient, String subject, String message) {
		VersandauftragDto dto = new VersandauftragDto();
		dto.setCEmpfaenger(recipient);
		dto.setCBetreff(subject);
		dto.setCText(message);
		
		String absender = getParameterFac().getMailadresseAdmin(theClientDto.getMandant());
		
		dto.setCAbsenderadresse(absender);
		try {
			getVersandFac().createVersandauftrag(dto, false, theClientDto);
		} catch (Throwable t) {
			myLogger.error("Autojob 4Vending Export, sending email to \"" + recipient + "\" failed", t);
		}
	}

	private boolean hasErrors(List<? extends EJBExceptionLP> exportErrors) {
		return !exportErrors.isEmpty();
	}

	private void exportToFile(String xmlContent, String pathPattern) throws IOException {
		FileHandler fileHandler = new FileHandler(fileextension);
		fileHandler.setFilepath(pathPattern, new Object[] {getDate()}, theClientDto.getLocUi());
		if (!fileHandler.writeBytes(xmlContent.getBytes("UTF-8"))) throw new IOException(
				"Fehler beim Schreiben der Datei, eventuell kann auf Pfad \"" + fileHandler.getFilepath()
						+ "nicht zugegriffen oder das Verzeichnis nicht erstellt werden");
		
	}

}
