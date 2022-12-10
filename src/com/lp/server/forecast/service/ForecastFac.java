package com.lp.server.forecast.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Remote
public interface ForecastFac {
	
	public static String FORECASTART_FORECASTAUFTRAG = "Forecastauftrag";
	public static String FORECASTART_CALL_OFF_WOCHE = "Call-Off Woche ";
	public static String FORECASTART_CALL_OFF_TAG = "Call-Off Tag   ";
	public static String FORECASTART_NICHT_DEFINIERT = "nicht definiert";
	
	
	public static int FORECAST_TYP_RUNDUNG_KEINE = 0;
	public static int FORECAST_TYP_RUNDUNG_VERPACKUNGSMENGE = 1;
	public static int FORECAST_TYP_RUNDUNG_VERPACKUNGSMITTELMENGE = 2;
	

	public Integer createForecast(ForecastDto dto);

	public Integer createForecastauftrag(ForecastauftragDto dto);

	public void updateForecast(ForecastDto dto);

	public void updateForecastauftrag(ForecastauftragDto dto);

	public ForecastDto forecastFindByPrimaryKey(Integer iId);

	public ForecastauftragDto forecastauftragFindByPrimaryKey(Integer iId);

	public void removeForecast(ForecastDto dto);

	public void removeForecastauftrag(ForecastauftragDto dto);

	public void toggleForecastErledigt(Integer forecastIId);

	public Integer createForecastposition(ForecastpositionDto dto);

	public void removeForecastposition(ForecastpositionDto dto);

	public ForecastpositionDto forecastpositionFindByPrimaryKey(Integer iId);

	public void updateForecastposition(ForecastpositionDto dto);

	public ForecastartDto forecastartFindByPrimaryKey(String cNrI,
			TheClientDto theClientDto);

	public void updateForecastart(ForecastartDto forecastartDto,
			TheClientDto theClientDto);

	public Map getAllForecastart(TheClientDto theClientDto);

	public Map getAllKommdrucker(TheClientDto theClientDto);
	public boolean sindBereitsPositionenVorhanden(Integer forecastauftragIId);

	public void reservierungenMitForecastSynchronisieren(Integer forecastIId,
			Integer forecastauftragIId, Integer forecastpositionIId);

	public TreeMap<String,BigDecimal> toggleFreigabe(Integer forecastauftragIId,
			TheClientDto theClientDto);
	
	public ForecastDto forecastFindByPrimaryKeyOhneExc(Integer forecastIId);

	public boolean sindBereitsLieferscheinpositionenVorhanden(
			Integer forecastpositionIId);

	public ForecastpositionDto forecastpositonFindByPrimaryKeyOhneExc(
			Integer forecastpositionIId);
	
	public BigDecimal getBereitsGelieferteMenge(Integer forecastpositionIId);
	
	public Integer createLinienabruf(LinienabrufDto dto);
	public void updateLinienabruf(LinienabrufDto dto,
			TheClientDto theClientDto);
	public void removeLinienabruf(LinienabrufDto dto);
	public LinienabrufDto linienabrufFindByPrimaryKey(Integer iId);
	
	
	
	public BigDecimal getSummeLinienabrufe(Integer forecastpositionIId);
	
	

	List<ForecastpositionDto> forecastpositionFindByForecastauftragIId(Integer forecastauftragIId);
	
	List<ForecastauftragDto> forecastauftragFindByFclieferadresseIIdStatusCNr(Integer forecastIId, 
			String statusCNr);
	
	public ImportdefDto importdefFindByPrimaryKey(String cNr,
			TheClientDto theClientDto);
	public void updateImportdef(ImportdefDto importdefDto);
	public Map getAllImportdef(TheClientDto theClientDto);
	
	public Integer createFclieferadresse(FclieferadresseDto dto);
	public void removeFclieferadresse(FclieferadresseDto dto);
	public FclieferadresseDto fclieferadresseFindByPrimaryKey(Integer iId);
	public void updateFclieferadresse(FclieferadresseDto dto, TheClientDto theClientDto);
	
	public Map getAllArtikelEinesForecastAuftrags(Integer forecastauftragIId);
	
	public String getForecastartEienrForecastposition(Integer forecastpositionIId);
	
	FclieferadresseDto fclieferadresseFindByForecastIdLieferadresseIdOhneExc(Integer forecastId, Integer lieferKundeId);
	
	LinienabrufDto linienabrufFindByPrimaryKeyOhneExc(Integer iId);
	
	List<ForecastpositionProduktionDto> getLieferbareForecastpositionByMandant(
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
//	List<ForecastpositionProduktionDto> getLieferbareForecastpositionByFclieferadresseIId(
//			Integer fcLieferadresseIId, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	ForecastpositionProduktionDto getLieferbareForecastpositionByIId(
			Integer forecastpositionIId, boolean withLinienabrufArtikel, TheClientDto theClientDto) 
					throws RemoteException, EJBExceptionLP;
	
	void starteLinienabrufProduktion(Integer forecastpositionIId, TheClientDto theClientDto) throws RemoteException;
	
	LinienabrufArtikelDto produziereLinienabrufArtikel(LinienabrufArtikelbuchungDto labDto, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	void beendeLinienabrufProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp, 
			TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	List<FclieferadresseNokaDto> getLieferbareFclieferadressenByMandant(TheClientDto theClientDto) 
			throws RemoteException, EJBExceptionLP;
	
	FclieferadresseDto fclieferadresseFindByPrimaryKeyOhneExc(Integer iId);
	
	boolean isLinienabrufProduktionGestartet(Integer forecastpositionIId);
	
	void bucheZeitAufForecastposition(Integer forecastpositionIId, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;

	List<LinienabrufDto> linienabrufFindByForecastpositionId(
			Integer forecastpositionId);
	
	void createAblieferungLinienabrufProduktion(Integer forecastpositionIId, FclieferadresseNoka kommissioniertyp, 
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	Integer getAktuellFreigegebenenForecastauftragEinerLieferadresse(Integer kundeIIdLieferadresse);
	
	List<ForecastpositionProduktionDto> getLieferbareForecastpositionByFclieferadresseIId(
			Integer fcLieferadresseIId, FclieferadresseNoka kommissionierTyp, TheClientDto theClientDto) throws RemoteException, EJBExceptionLP;
	
	ForecastpositionProduktionDto produziereMaterial(ForecastpositionArtikelbuchungDto fpaDto,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
	public boolean gibtEsDenArtikelIneinemOffenenOderFreigegebenenForecastauftrag(Integer artikelIId, Integer kundeIIdLieferadresse);

	List<String> getStatiEinerForecastposition();
	
	public void removeKommdrucker(KommdruckerDto dto);
	public KommdruckerDto kommdruckerFindByPrimaryKey(Integer iId);
	public void updateKommdrucker(KommdruckerDto dto);
	public Integer createKommdrucker(KommdruckerDto dto);

	BigDecimal getMengeBereitsGeliefert(Integer forecastpositionId);
	
}
