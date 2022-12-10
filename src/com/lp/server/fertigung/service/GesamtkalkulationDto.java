package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.artikel.service.WarenzugangsreferenzDto;

public class GesamtkalkulationDto implements Serializable {

	BigDecimal bdGesamtwertAZMann = null;
	BigDecimal bdGesamtwertAZMaschine = null;



	BigDecimal bdLief1WertMaterial = null;

	public BigDecimal getBdLief1WertMaterial() {
		return bdLief1WertMaterial;
	}

	public void setBdLief1WertMaterial(BigDecimal bdLief1WertMaterial) {
		this.bdLief1WertMaterial = bdLief1WertMaterial;
	}

	public BigDecimal getBdGesamtwertAZMann() {
		return bdGesamtwertAZMann;
	}

	public void setBdGesamtwertAZMann(BigDecimal bdGesamtwertAZMann) {
		this.bdGesamtwertAZMann = bdGesamtwertAZMann;
	}

	public BigDecimal getBdGesamtwertAZMaschine() {
		return bdGesamtwertAZMaschine;
	}

	public void setBdGesamtwertAZMaschine(BigDecimal bdGesamtwertAZMaschine) {
		this.bdGesamtwertAZMaschine = bdGesamtwertAZMaschine;
	}

	BigDecimal bdArbeitszeitGesamt = null;

	public BigDecimal getBdArbeitszeitGesamt() {
		return bdArbeitszeitGesamt;
	}

	public void setBdArbeitszeitGesamt(BigDecimal bdArbeitszeitGesamt) {
		this.bdArbeitszeitGesamt = bdArbeitszeitGesamt;
	}

	public BigDecimal getBdArbeitszeitGesamtMann() {
		return bdArbeitszeitGesamtMann;
	}

	public void setBdArbeitszeitGesamtMann(BigDecimal bdArbeitszeitGesamtMann) {
		this.bdArbeitszeitGesamtMann = bdArbeitszeitGesamtMann;
	}

	public BigDecimal getBdArbeitszeitGesamtMaschine() {
		return bdArbeitszeitGesamtMaschine;
	}

	public void setBdArbeitszeitGesamtMaschine(BigDecimal bdArbeitszeitGesamtMaschine) {
		this.bdArbeitszeitGesamtMaschine = bdArbeitszeitGesamtMaschine;
	}

	BigDecimal bdArbeitszeitGesamtMann = null;
	BigDecimal bdArbeitszeitGesamtMaschine = null;

	BigDecimal materialwertGesamt = null;
	BigDecimal aZwertGesamt = null;

	public BigDecimal getMaterialwertGesamt() {
		return materialwertGesamt;
	}

	public void setMaterialwertGesamt(BigDecimal materialwertGesamt) {
		this.materialwertGesamt = materialwertGesamt;
	}

	public BigDecimal getAZwertGesamt() {
		return aZwertGesamt;
	}

	public void setAZwertGesamt(BigDecimal aZwertGesamt) {
		this.aZwertGesamt = aZwertGesamt;
	}

	BigDecimal durchschnittlicherLief1WertProStueck = null;

	public BigDecimal getDurchschnittlicherLief1WertProStueck() {
		return durchschnittlicherLief1WertProStueck;
	}

	public void setDurchschnittlicherLief1WertProStueck(BigDecimal durchschnittlicherLief1WertProStueck) {
		this.durchschnittlicherLief1WertProStueck = durchschnittlicherLief1WertProStueck;
	}

	BigDecimal durchschnittlicherMaterialpreisProStueck = null;

	public BigDecimal getDurchschnittlicherMaterialpreisProStueck() {
		return durchschnittlicherMaterialpreisProStueck;
	}

	public void setDurchschnittlicherMaterialpreisProStueck(BigDecimal durchschnittlicherMaterialpreisProStueck) {
		this.durchschnittlicherMaterialpreisProStueck = durchschnittlicherMaterialpreisProStueck;
	}

	public BigDecimal getDurchschnittlicherAZPreisProStueck() {
		return durchschnittlicherAZPreisProStueck;
	}

	public void setDurchschnittlicherAZPreisProStueck(BigDecimal durchschnittlicherAZPreisProStueck) {
		this.durchschnittlicherAZPreisProStueck = durchschnittlicherAZPreisProStueck;
	}

	BigDecimal durchschnittlicherAZPreisProStueck = null;

	private static final long serialVersionUID = 1L;

	private ArrayList alDaten = new ArrayList();

	public ArrayList getAlDaten() {
		return alDaten;
	}

	public void setAlDaten(ArrayList alDaten) {
		this.alDaten = alDaten;
	}

	public TreeMap<Integer, ArrayList<Integer>> getBetroffeneLose() {
		return betroffeneLose;
	}

	public void setBetroffeneLose(TreeMap<Integer, ArrayList<Integer>> betroffeneLose) {
		this.betroffeneLose = betroffeneLose;
	}

	TreeMap<Integer, ArrayList<Integer>> betroffeneLose = new TreeMap<Integer, ArrayList<Integer>>();

	public void addBetroffenesLos(Integer key, Integer losIId) {
		ArrayList tempList = null;
		if (betroffeneLose.containsKey(key)) {
			tempList = betroffeneLose.get(key);
			if (tempList == null)
				tempList = new ArrayList();
			tempList.add(losIId);
		} else {
			tempList = new ArrayList();
			tempList.add(losIId);
		}
		betroffeneLose.put(key, tempList);
	}

	private HashMap<Integer, ArrayList<WarenzugangsreferenzDto>> hmWareneingangsreferenzLosIstMaterial = new HashMap<Integer, ArrayList<WarenzugangsreferenzDto>>();

	public void add2HmWareneingangsreferenzLosIstMaterial(Integer losistmaterialIId,
			ArrayList<WarenzugangsreferenzDto> temp) {
		hmWareneingangsreferenzLosIstMaterial.put(losistmaterialIId, temp);
	}

	public ArrayList<WarenzugangsreferenzDto> getFromHmWareneingangsreferenzLosIstMaterial(Integer losistmaterialIId) {
		if (hmWareneingangsreferenzLosIstMaterial.containsKey(losistmaterialIId)) {
			return hmWareneingangsreferenzLosIstMaterial.get(losistmaterialIId);
		} else {
			return null;
		}
	}

	private HashMap<Integer, HashMap<BigDecimal, ArtikellieferantDto>> hmLief1Preise = new HashMap<Integer, HashMap<BigDecimal, ArtikellieferantDto>>();

	public void add2HmLief1Preise(Integer artikelIId, BigDecimal bdMenge, ArtikellieferantDto alDto) {
		HashMap<BigDecimal, ArtikellieferantDto> hmMengen = null;
		if (hmLief1Preise.containsKey(artikelIId)) {
			hmMengen = hmLief1Preise.get(artikelIId);
		} else {
			hmMengen = new HashMap<BigDecimal, ArtikellieferantDto>();
		}

		hmMengen.put(bdMenge, alDto);

		hmLief1Preise.put(artikelIId, hmMengen);
	}

	public boolean preisVorhanden(Integer artikelIId, BigDecimal bdMenge) {
		if (hmLief1Preise.containsKey(artikelIId)) {
			HashMap<BigDecimal, ArtikellieferantDto> hmMengen = hmLief1Preise.get(artikelIId);

			if (hmMengen.containsKey(bdMenge)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public ArtikellieferantDto getLief1Preis(Integer artikelIId, BigDecimal bdMenge) {

		if (hmLief1Preise.containsKey(artikelIId)) {
			HashMap<BigDecimal, ArtikellieferantDto> hmMengen = hmLief1Preise.get(artikelIId);

			if (hmMengen.containsKey(bdMenge)) {
				return hmMengen.get(bdMenge);
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	private HashMap<Integer, List<SeriennrChargennrMitMengeDto>> hmSnrChnrLosIstMaterial = new HashMap<Integer, List<SeriennrChargennrMitMengeDto>>();

	public void add2HmSnrChnrLosIstMaterial(Integer losistmaterialIId, List<SeriennrChargennrMitMengeDto> temp) {
		hmSnrChnrLosIstMaterial.put(losistmaterialIId, temp);
	}

	public List<SeriennrChargennrMitMengeDto> getFromHmSnrChnrLosIstMaterial(Integer losistmaterialIId) {
		if (hmSnrChnrLosIstMaterial.containsKey(losistmaterialIId)) {
			return hmSnrChnrLosIstMaterial.get(losistmaterialIId);
		} else {
			return null;
		}
	}

	private HashMap<Integer, BigDecimal> hmEinstandswertLosIstMaterial = new HashMap<Integer, BigDecimal>();

	public void add2HmEinstandswertLosIstMaterial(Integer losistmaterialIId, BigDecimal einstandswert) {
		hmEinstandswertLosIstMaterial.put(losistmaterialIId, einstandswert);
	}

	public BigDecimal getFromHmEinstandswertLosIstMaterial(Integer losistmaterialIId) {
		if (hmEinstandswertLosIstMaterial.containsKey(losistmaterialIId)) {
			return hmEinstandswertLosIstMaterial.get(losistmaterialIId);
		} else {
			return null;
		}
	}

	private HashMap<Integer, BigDecimal> hmPreisLosIstMaterial = new HashMap<Integer, BigDecimal>();

	public void add2HmPreisLosIstMaterial(Integer losistmaterialIId, BigDecimal preis) {
		hmPreisLosIstMaterial.put(losistmaterialIId, preis);
	}

	public BigDecimal getFromHmPreisLosIstMaterial(Integer losistmaterialIId) {
		if (hmPreisLosIstMaterial.containsKey(losistmaterialIId)) {
			return hmPreisLosIstMaterial.get(losistmaterialIId);
		} else {
			return null;
		}
	}
	
	public HashMap<Integer, ArtikelDto> hmArtikelDto = new HashMap<Integer, ArtikelDto>();
	
	public HashMap<Integer,ArrayList<LossollmaterialDto>> hmLossollmaterialDto = new HashMap<Integer,ArrayList<LossollmaterialDto>>();
	
	public HashMap<Integer, LagerDto> hmLagerDto = new HashMap<Integer, LagerDto>();
	
	
	
	
}
