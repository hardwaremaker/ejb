package com.lp.server.personal.service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.personal.ejb.HvmaparameterPK;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.HvmabenutzerId;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface HvmaFac {
	enum Param {
		zielterminTage("ZIELTERMIN_TAGE"),
		belegstatusListe("BELEGSTATUS_LISTE"),
		taetigkeitStandard("TAETIGKEIT_STANDARD"),
		taetigkeitswechselDokumentieren("TAETIGKEITSWECHSEL_DOKUMENTIEREN"),
		
		//Kueche
		menuRechte("MENU_RECHTE"),
		essensausgabeKundeId("ESSENSAUSGABE_KUNDEID"),
		projektShop("PROJEKTSHOP"),
		projektWeb("PROJEKTWEB"),
		alleArtikel("ALLE_ARTIKEL"),
		lieferterminKommentarEingabe("LIEFERTERMIN_KOMMENTAR");
		
		Param(String value) {
			this.value = value;		
		}
		
		public String getText() {
			return value;
		}	
		
		private String value;			
	};
	
	enum Kat {
		Allgemein("ALLGEMEIN"),
		Auftrag("AUFTRAG"),
		Los("LOS"),
		Projekt("PROJEKT"),
		Kueche("KUECHE");
		
		Kat(String value) {
			this.value = value;
		}
		
		public String getText() {
			return value;
		}	
		
		private String value;		
	}
	
	HvmalizenzDto hvmalizenzFindByPrimaryKey(Integer iId);

	HvmarechtDto hvmarechtFindByPrimaryKey(Integer iId);

	HvmabenutzerDto hvmabenutzerFindByPrimaryKey(Integer iId);

	Integer createHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto);

	void updateHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto);

	void removeHvmabenutzer(HvmabenutzerDto dto, TheClientDto theClientDto);

	void updateHvmarolle(HvmarolleDto dto, TheClientDto theClientDto);

	Integer createHvmarolle(HvmarolleDto dto, TheClientDto theClientDto);

	HvmarolleDto hvmarolleFindByPrimaryKey(Integer iId);

	void removeHvmarolle(HvmarolleDto dto, TheClientDto theClientDto);

	ArrayList<HvmarechtDto> getHvmaRechte(String hvmalizenzCNr, TheClientDto theClientDto);

	public Integer createHvmasync(String tQuelle, TheClientDto theClientDto);
	
	public JasperPrintLP printLetzteSynchronisation(DatumsfilterVonBis datumsfilter, TheClientDto theClientDto);
	
	public final static String REPORT_LETZTESYNCHRONISATION = "pers_letztesynchronisation.jasper";

	HvmalizenzDto hvmalizenzFindByCnr(String licenceCnr);
	HvmalizenzDto hvmalizenzFindByEnum(HvmaLizenzEnum licence);
	HvmabenutzerDto hvmabenutzerFindByBenutzerIdLizenzId(Integer benutzerId, Integer lizenzId);

	ArrayList<HvmarechtDto> getHvmaRechte(TheClientDto theClientDto) throws RemoteException;
	void updateLizenzMaxBenutzer(HvmaLizenzEnum lizenz, Integer maxUser);
	
	List<HvmabenutzerParameterDto> parameterMobil(HvmabenutzerId benutzerId);
	List<HvmabenutzerParameterDto> parameterMobil(TheClientDto theClientDto);

	Integer getZielterminAuftrag(HvmabenutzerId benutzerId);
	Integer getZielterminAuftrag(TheClientDto theClientDto);
	Integer getZielterminLos(HvmabenutzerId benutzerId);
	Integer getZielterminLos(TheClientDto theClientDto);
	Integer getZielterminProjekt(HvmabenutzerId benutzerId);
	Integer getZielterminProjekt(TheClientDto theClientDto);

	List<String> getBelegStatusAuftrag(HvmabenutzerId benutzerId);
	List<String> getBelegStatusLos(HvmabenutzerId benutzerId);
	List<String> getBelegStatusProjekt(HvmabenutzerId benutzerId);
	List<String> getBelegStatusAuftrag(TheClientDto theClientDto);
	List<String> getBelegStatusLos(TheClientDto theClientDto);
	List<String> getBelegStatusProjekt(TheClientDto theClientDto);

	String getTaetigkeitAuftrag(HvmabenutzerId benutzerId);
	String getTaetigkeitAuftrag(TheClientDto theClientDto);
	Integer getTaetigkeitIdAuftrag(TheClientDto theClientDto) throws RemoteException;	

	String getTaetigkeitProjekt(HvmabenutzerId benutzerId);
	String getTaetigkeitProjekt(TheClientDto theClientDto);
	Integer getTaetigkeitIdProjekt(TheClientDto theClientDto) throws RemoteException;
	
	public HvmaparameterDto parameterFindByPrimaryKey(HvmaparameterPK pk);
	
	public void updateParameter(HvmaparameterDto dto, TheClientDto theClientDto);
	
	public Integer createHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto);
	
	public void updateHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto);
	
	public HvmabenutzerParameterDto hvmabenutzerparameterFindByPrimaryKey(Integer iId);
	public void removeHvmabenutzerparameter(HvmabenutzerParameterDto dto, TheClientDto theClientDto);
	
	Integer getMenurechteKueche(HvmabenutzerId benutzerId);
	Integer getMenurechteKueche(TheClientDto theClientDto);
	
	Integer getEssensausgabeKundeIdKueche(HvmabenutzerId benutzerId);
	Integer getEssensausgabeKundeIdKueche(TheClientDto theClientDto);
			
	String getProjektShopKueche(HvmabenutzerId benutzerId);
	String getProjektShopKueche(TheClientDto theClientDto);
			
	String getProjektWebKueche(HvmabenutzerId benutzerId);
	String getProjektWebKueche(TheClientDto theClientDto);
	
	boolean getAlleArtikelKueche(HvmabenutzerId benutzerId);
	boolean getAlleArtikelKueche(TheClientDto theClientDto);
	
	boolean getLieferterminKommentarEingabe(HvmabenutzerId benutzerId);
	boolean getLieferterminKommentarEingabe(TheClientDto theClientDto);
}