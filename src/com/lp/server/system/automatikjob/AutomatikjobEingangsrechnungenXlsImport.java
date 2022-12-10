package com.lp.server.system.automatikjob;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lp.server.eingangsrechnung.service.ErImportError20475.Severity;
import com.lp.server.eingangsrechnung.service.ErImportItem20475;
import com.lp.server.eingangsrechnung.service.ErImportItemList20475;
import com.lp.server.system.service.JobDetailsErImportDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.util.Helper;

public class AutomatikjobEingangsrechnungenXlsImport extends AutomatikjobBasis {

	private static final String ARCHIVE_DIRECTORY = "old";
	private JobDetailsErImportDto detailsDto;
	private TheClientDto theClientDto;

	public AutomatikjobEingangsrechnungenXlsImport() {
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		
		if (!initJobDetailsJobDto(theClientDto)) {
			return false;
		}
		
		File file = new File(detailsDto.getCImportPfad());
		
		if (!isValidDirectory(file)) {
			return false;
		}
	
		
		return importFiles(file);
	}

	private boolean importFiles(File directory) {
		List<File> importFiles = getSortedFileList(directory);
		
		for (File file : importFiles) {
			try {
				byte[] content = readBinaryFile(file);
				ErImportItemList20475 erItems = getEingangsrechnungFac().importXls20475(content, false, theClientDto);
				if (erItems.hasErrors(Severity.ERROR)) {
					if (erItems.hasImportedItems()) {
						sendFehlerTeilimport(file.getAbsolutePath(), asMailtext(erItems.getItems()));
					} else {
						sendFehler(file.getAbsolutePath(), asMailtext(erItems.getItems()));
					}
					return false;
				}
				
				sendMessage(file.getAbsolutePath(), asMailtext(erItems.getItems()));
				moveImportFileToArchive(file);
			} catch (IOException ex) {
				myLogger.error("IOException", ex);
				sendFehler(file.getAbsolutePath(), "Waehrend des Lesens von Datei '" + file.getAbsolutePath() 
					+ "' trat ein unerwarteter Fehler auf (" + ex.getMessage() + ")");
				return true;
			} catch (Throwable ex) {
				myLogger.error("Exception", ex);
				sendFehler(file.getAbsolutePath(), "Es trat ein unerwarteter Fehler auf (" + ex.getMessage() + ")");
				return true;
			}
			
			
		}
		return false;
	}

	private String asMailtext(List<ErImportItem20475> erItems) {
		StringBuilder builder = new StringBuilder();
		for (ErImportItem20475 item : erItems) {
			builder.append("\n").append(item.asWholeInfo());
		}
		return builder.toString();
	}

	private List<File> getSortedFileList(File directory) {
		List<File> importFiles = Arrays.asList(directory.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".xls");
			}
		}));
		
		Collections.sort(importFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return new Long(f1.lastModified()).compareTo(new Long(f2.lastModified()));
			}
		});
		
		return importFiles;
	}
	
	private boolean isValidDirectory(File file) {
		if (!file.exists()) {
			sendFehler(null, "Pfad des Import-Verzeichnisses '" + file.getAbsolutePath() + "' existiert nicht");
			return false;
		}
		
		if (!file.isDirectory()) {
			sendFehler(null, "Pfad des Import-Verzeichnisses '" + file.getAbsolutePath() + "' ist kein Verzeichnis");
			return false;
		}
		
		return true;
	}

	private void sendFehlerTeilimport(String filepath, String message) {
		message = "ACHTUNG: Es konnten nicht alle Eingangsrechnungen aus Datei '" + filepath + "' importiert werden. \n" + message;
		myLogger.error("Automatikjob Eingangsrechnungen importieren ist fehlgeschlagen: \n" + message);
		createVersandauftrag(detailsDto.getCEmailFehler(), 
				"Automatikjob Eingangsrechnungen importieren fehlgeschlagen", message);
	}

	private void sendFehler(String filepath, String message) {
		if (filepath != null) {
			message = "Eingangsrechnungen aus Datei '" + filepath + "' konnten nicht importiert werden. \n" + message;
		} else {
			message = "Eingangsrechnungen konnten nicht importiert werden. \n" + message;
		}
		myLogger.error("Automatikjob Eingangsrechnungen importieren ist fehlgeschlagen: \n" + message);
		createVersandauftrag(detailsDto.getCEmailFehler(), 
				"Automatikjob Eingangsrechnungen importieren fehlgeschlagen", message);
	}
	
	private void sendMessage(String filepath, String message) {
		message = "Eingangsrechnungen aus Datei '" + filepath + "' erfolgreich importiert. \n" + message;
		myLogger.info(message);
		createVersandauftrag(detailsDto.getCEmailErfolgreich(), 
				"Automatikjob Eingangsrechnungen importieren erfolgreich durchgef\u00fchrt", message);
	}

	private void createVersandauftrag(String recipient, String subject, String message) {
		if (Helper.isStringEmpty(recipient)) {
			return;
		}
		if (!Helper.validateEmailadresse(recipient)) {
			myLogger.error("Nachricht kann nicht gesendet werden. Email-Adresse '" 
					+ recipient + "' ist nicht gueltig.");
			return;
		}
		
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

	private boolean initJobDetailsJobDto(TheClientDto theClientDto) {
		detailsDto = getJobDetailsErImportFac().findByMandantCNr(theClientDto.getMandant());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob Eingangsrechnungen Import not found in DB");
			return false;
		}
		
		if (Helper.isStringEmpty(detailsDto.getCImportPfad())) {
			myLogger.error("Import Pfad ist nicht definiert");
			return false;
		}
		
		return true;
	}
	
	private byte[] readBinaryFile(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException ex) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException ex) {
			}
		}

		return ous.toByteArray();
	}

	protected void moveImportFileToArchive(File file) throws IOException {
		File parent = new File(file.getParent());
		if (!parent.isDirectory()) return;
		
		File archiveFolder = new File(parent, ARCHIVE_DIRECTORY);
		if (!archiveFolder.exists()) {
			archiveFolder.mkdir();
		}
		
		File archive = new File(archiveFolder, file.getName());
		
		FileInputStream inputstream = null;
		FileChannel inChannel = null;
		FileOutputStream outputstream = null;
		FileChannel outChannel = null;
		
		try {
			inputstream = new FileInputStream(file);
			inChannel = inputstream.getChannel();
			if (!archiveFolder.canWrite()) return;
			outputstream = new FileOutputStream(archive);
			outChannel = outputstream.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			myLogger.error("Kopieren von '" + file.getName() + "' ist fehlgeschlagen");
			throw e;
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
				if (inputstream != null) {
					inputstream.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
				if (outputstream != null) {
					outputstream.close();
				}
			} catch (IOException e) {
			}
		}
		
		if (archive != null && archive.exists()) {
			archive.setLastModified(file.lastModified());
			boolean deletionSucceeded = file.delete();
			if (!deletionSucceeded) {
				myLogger.error("Konnte Datei '" + file.getAbsolutePath() + "' nicht loeschen");
			}
		}
	}
}
