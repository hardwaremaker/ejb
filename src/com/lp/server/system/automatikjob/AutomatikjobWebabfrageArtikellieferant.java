package com.lp.server.system.automatikjob;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lp.server.angebotstkl.service.WebpartnerDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantDto;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.artikel.service.WebabfrageArtikellieferantProperties;
import com.lp.server.artikel.service.WebabfrageArtikellieferantResult;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.WebabfrageArtikellieferantException;

public class AutomatikjobWebabfrageArtikellieferant extends AutomatikjobBasis {

	private JobDetailsWebabfrageArtikellieferantDto detailsDto;
	private TheClientDto theClientDto;
	
	public AutomatikjobWebabfrageArtikellieferant() {
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		
		if (!initValidateJobDetails()) {
			return true;
		}
		
		try {
			performJobImpl();
		} catch (Throwable e) {
			myLogger.error("Error performing Automatikjob Artikellieferant Webabfrage", e);
			return true;
		}
		
		return false;
	}

	private HvCreatingCachingProvider<Integer, ArtikelDto> artikelCache = new HvCreatingCachingProvider<Integer, ArtikelDto>() {
		protected ArtikelDto provideValue(Integer key, Integer transformedKey) {
			return getArtikelFac().artikelFindByPrimaryKeySmall(key, theClientDto);
		}
	};
	
	private HvCreatingCachingProvider<Integer, LieferantDto> lieferantCache = new HvCreatingCachingProvider<Integer, LieferantDto>() {
		protected LieferantDto provideValue(Integer key, Integer transformedKey) {
			return getLieferantFac().lieferantFindByPrimaryKey(key, theClientDto);
		}
	};
	
	private void performJobImpl() throws EJBExceptionLP, RemoteException {
		Map<Integer,List<Integer>> hmLieferantArtikeln = findUniqueArtikellieferanten();
		StringBuilder msgBuilder = new StringBuilder();
		
		for (Entry<Integer, List<Integer>> entry : hmLieferantArtikeln.entrySet()) {
			LieferantId lieferantId = new LieferantId(entry.getKey());
			msgBuilder.append("F\u00fcr Lieferant '" + lieferantCache.getValueOfKey(lieferantId.id()).getPartnerDto().formatName() + "' :\n");
			for (Integer artikelIId : entry.getValue()) {
				if (validArtikel(artikelIId)) {
					searchAndUpdate(new ArtikelId(artikelIId), lieferantId, msgBuilder);
				}
			}
			msgBuilder.append("\n\n");
		}
		
		sendMessage(msgBuilder.toString());
	}

	private void searchAndUpdate(ArtikelId artikelId, LieferantId lieferantId, StringBuilder msgBuilder) {
		WebabfrageArtikellieferantProperties properties = new WebabfrageArtikellieferantProperties(artikelId, lieferantId);
		properties.setUpdate(true);
		try {
			WebabfrageArtikellieferantResult result = getArtikelFac().aktualisiereArtikellieferantByWebabfrage(properties, theClientDto);
			appendResultMsg(result, msgBuilder);
		} catch (EJBExceptionLP e) {
			appendExcMsg(properties, e, msgBuilder);
		} catch (Exception e) {
			myLogger.error("Unexpected Exception", e);
			appendUnexpectedExcMsg(properties,  e, msgBuilder);
		}
	}

	private void appendResultMsg(WebabfrageArtikellieferantResult result, StringBuilder msgBuilder) {
		ArtikelDto artikelDto = artikelCache.getValueOfKey(result.getArtikellieferantDto().getArtikelIId());
		msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': Aktualisiert\n");
	}

	private void appendSearchParams(WebabfrageArtikellieferantException excData, StringBuilder msgBuilder) {
		msgBuilder.append(" Suchparameter: ");
		if (excData.getResultLiefNr().wasExecuted()) {
			msgBuilder.append("Lieferantenartikelnummer '" + excData.getResultLiefNr().getSearchValue() + "' ");
		}
		if (excData.getResultHstNr().wasExecuted()) {
			msgBuilder.append("Herstellernummer '" + excData.getResultHstNr().getSearchValue() + "'");
		}
		msgBuilder.append("\n");
	}
	
	private void appendExcMsg(WebabfrageArtikellieferantProperties properties, EJBExceptionLP ex, StringBuilder msgBuilder) {
		ArtikelDto artikelDto = artikelCache.getValueOfKey(properties.getArtikelId().id());
		WebabfrageArtikellieferantException excData = null;
		if (ex.getExceptionData() instanceof WebabfrageArtikellieferantException) {
			excData = (WebabfrageArtikellieferantException)ex.getExceptionData();
		}
		if (EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_KEIN_ERGEBNIS == ex.getCode()) {
			if (excData != null) {
				msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': KEIN ERGEBNIS\n");
				appendSearchParams(excData, msgBuilder);
			} else {
				msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': KEIN ERGEBNIS\n");
			}
			return;
			
		} else if (EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_MEHRFACHE_ERGEBNISSE == ex.getCode()) {
			if (excData != null) {
				msgBuilder.append(
					"Artikel '" + artikelDto.getCNr() + "': MEHRFACHE ERGEBNISSE\n");
				appendSearchParams(excData, msgBuilder);
			} else {
				msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': MEHRFACHE ERGEBNISSE\n");
			}
			return;
			
		}
		
		PartSearchUnexpectedResponseExc partSearchExc = null;
		if (ex.getAlInfoForTheClient().size() >= 3
				&& ex.getAlInfoForTheClient().get(2) instanceof PartSearchUnexpectedResponseExc) {
			partSearchExc = (PartSearchUnexpectedResponseExc)ex.getAlInfoForTheClient().get(2);
		}
		
		if (EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_UNERWARTETE_RESPONSE == ex.getCode()
				|| EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_RESPONSE_FORBIDDEN == ex.getCode()) {
			if (partSearchExc != null) {
				msgBuilder.append(
						"Artikel '" + artikelDto.getCNr() + "': UNERWARTETE RESPONSE " + partSearchExc.getStatusLine() 
						+ (partSearchExc.getApiError() != null ? " [" + partSearchExc.getApiError() + "]" : "") + "\n");
			} else {
				msgBuilder.append(
						"Artikel '" + artikelDto.getCNr() + "': UNERWARTETE RESPONSE\n");
			}
		} else {
			msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': UNBEKANNTER FEHLER " + ex.getMessage() + "\n");
		}
	}

	private void appendUnexpectedExcMsg(WebabfrageArtikellieferantProperties properties, Exception ex,  StringBuilder msgBuilder) {
		ArtikelDto artikelDto = artikelCache.getValueOfKey(properties.getArtikelId().id());
		msgBuilder.append("Artikel '" + artikelDto.getCNr() + "': UNBEKANNTER FEHLER " + ex.getMessage() + "\n");
	}

	private void sendMessage(String message) {
		message = "Protokoll der Durchf\u00fchrung des Automatikjobs Webabfrage Artikellieferant. \n\n" + message;
		myLogger.info(message);
		createVersandauftrag(detailsDto.getCEmailErfolgreich(), 
				"Automatikjob Webabfrage Artikellieferanten - Protokoll", message);
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
			myLogger.error("Autojob Webabfrage Artikellieferant, sending email to \"" + recipient + "\" failed", t);
		}
	}

	private boolean validArtikel(Integer artikelIId) {
		ArtikelDto artikelDto = artikelCache.getValueOfKey(artikelIId);
		return !Helper.isTrue(artikelDto.getBVersteckt());
	}

	private Map<Integer,List<Integer>> findUniqueArtikellieferanten() throws EJBExceptionLP, RemoteException {
		List<WebpartnerDto> weblieferanten = getLieferantFac().weblieferantFindByMandant(theClientDto.getMandant());
		Map<Integer,List<Integer>> hmLieferantArtikeln = new HashMap<Integer,List<Integer>>();
		for (WebpartnerDto weblieferantDto : weblieferanten) {
			List<Integer> artikelIIds = getArtikelFac().getArtikelIdsArtikellieferantByLieferantIId(weblieferantDto.getLieferantIId());
			hmLieferantArtikeln.put(weblieferantDto.getLieferantIId(), artikelIIds);
		}
		return hmLieferantArtikeln;
	}

	private boolean initValidateJobDetails() {
		detailsDto = getJobWebabfrageArtikellieferant().findByMandantCNr(theClientDto.getMandant());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob Artikellieferant Webabfrage not found in DB");
			return false;
		}
		return true;
	}
}
