package com.lp.server.system.service;

import java.util.ArrayList;

import javax.ejb.Remote;

@Remote
public interface BelegartmediaFac {

	public static int AUSRICHTUNG_LINKSBUENDIG = 1;
	public static int AUSRICHTUNG_RECHTSBUENDIG = 2;
	public static int AUSRICHTUNG_ZENTRIERT = 3;

	public ArrayList<BelegartmediaDto> getBelegartMediaDtos(Integer usecaseId, Integer iKey, TheClientDto theClientDto);

	public ArrayList<Integer> syncBelegartmediaDtos(Integer usecaseId, Integer iKey,
			ArrayList<BelegartmediaDto> belegartmediaDtos, TheClientDto theClientDto);
	
	public void removeBelegartmedia(Integer usecaseId, Integer iKey, TheClientDto theClientDto);
	
	public boolean sindMedienVorhanden (Integer usecaseId, Integer iKey,
			TheClientDto theClientDto);
	
	public Integer anzahlDerVorhandenenMedien(Integer usecaseId, Integer iKey, TheClientDto theClientDto);

	public void kopiereBelegartmedia(Integer usecaseIdQuelle, Integer iKeyQuelle, Integer usecaseIdZiel,
			Integer iKeyZiel, TheClientDto theClientDto);
}
