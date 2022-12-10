package com.lp.server.stueckliste.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.ejb.EJBException;

import com.lp.service.plscript.DebuggingScriptRuntime;
import com.lp.service.plscript.ScriptRuntime;

public class StrukturDatenParamDto implements Serializable {
	private static final long serialVersionUID = 292694003347549213L;

	private ArrayList<StuecklisteMitStrukturDto> strukturMap;
	private int ebene;
	private BigDecimal satzgroesse;
	private BigDecimal losgroesse;
	private boolean mitUnterstuecklisten;
	private boolean gleichePositionenZusammenfassen;
	private boolean ueberAlleMandanten;
	private String mandantCnrStueckliste;
	private Sort sortierung;
	private ScriptRuntime runtime;
	
	public enum Sort {
		Artikelnummer,
		Position, 
		Ohne
	}
	
	public StrukturDatenParamDto() {
		setEbene(0);
		setSatzgroesse(BigDecimal.ONE);
		setStrukturMap(new ArrayList<StuecklisteMitStrukturDto>());
		setRuntime(new DebuggingScriptRuntime());
	}


	public ArrayList<StuecklisteMitStrukturDto> getStrukturMap() {
		return strukturMap;
	}

	public StrukturDatenParamDto setStrukturMap(ArrayList<StuecklisteMitStrukturDto> strukturMap) {
		this.strukturMap = strukturMap;
		return this;
	}

	public int getEbene() {
		return ebene;
	}

	public StrukturDatenParamDto setEbene(int ebene) {
		this.ebene = ebene;
		return this;
	}

	public BigDecimal getSatzgroesse() {
		return satzgroesse;
	}

	public StrukturDatenParamDto setSatzgroesse(BigDecimal satzgroesse) {
		this.satzgroesse = satzgroesse; 
		return this;
	}

	public boolean isGleichePositionenZusammenfassen() {
		return gleichePositionenZusammenfassen;
	}

	public StrukturDatenParamDto setGleichePositionenZusammenfassen(boolean gleichePositionenZusammenfassen) {
		this.gleichePositionenZusammenfassen = gleichePositionenZusammenfassen;
		return this;
	}

	public StrukturDatenParamDto beGleichePositionZusammenfassen() {
		setGleichePositionenZusammenfassen(true);
		return this;
	}
	
	public BigDecimal getLosgroesse() {
		return losgroesse;
	}

	public StrukturDatenParamDto setLosgroesse(BigDecimal losgroesse) {
		this.losgroesse = losgroesse;
		return this;
	}

	public boolean isUeberAlleMandanten() {
		return ueberAlleMandanten;
	}

	public StrukturDatenParamDto setUeberAlleMandanten(boolean ueberAlleMandanten) {
		this.ueberAlleMandanten = ueberAlleMandanten;
		return this;
	}

	public String getMandantCnrStueckliste() {
		return mandantCnrStueckliste;
	}

	public StrukturDatenParamDto setMandantCnrStueckliste(String mandantCnrStueckliste) {
		this.mandantCnrStueckliste = mandantCnrStueckliste;
		return this;
	}

	public Sort getSortierung() {
		return sortierung;
	}

	public StrukturDatenParamDto setSortierung(Sort sortierung) {
		this.sortierung = sortierung;
		return this;
	}
	
	public StrukturDatenParamDto setSortierungInt(int intSortierung) {
		if(intSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_ARTIKELNR) {
			sortierung = Sort.Artikelnummer;
			return this;
		} else if(intSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_POSITION){ 
			sortierung = Sort.Position;
			return this;
		} else if(intSortierung == StuecklisteReportFac.REPORT_STUECKLISTE_OPTION_SORTIERUNG_OHNE) {
			sortierung = Sort.Ohne;
			return this;
		}
		
		throw new EJBException("Unknown Sortoption " + intSortierung);
	}

	public boolean isMitUnterstuecklisten() {
		return mitUnterstuecklisten;
	}

	public StrukturDatenParamDto setMitUnterstuecklisten(boolean mitUnterstuecklisten) {
		this.mitUnterstuecklisten = mitUnterstuecklisten;
		return this;
	}
	
	public StrukturDatenParamDto beMitUnterstuecklisten() {
		setMitUnterstuecklisten(true);
		return this;
	}
	
	public StrukturDatenParamDto addStruktur(StuecklisteMitStrukturDto strukturDto) {
		getStrukturMap().add(strukturDto);
		return this;
	}
	
	public StuecklisteMitStrukturDto getLast() {
		return getStrukturMap().get(getStrukturMap().size()-1);
	}
	
	public StrukturDatenParamDto createHierachy() {
		StrukturDatenParamDto paramDto = new StrukturDatenParamDto();
		paramDto.setEbene(getEbene() + 1);
		paramDto.setGleichePositionenZusammenfassen(isGleichePositionenZusammenfassen());
		paramDto.setLosgroesse(getLosgroesse());
		paramDto.setMandantCnrStueckliste(getMandantCnrStueckliste());
		paramDto.setMitUnterstuecklisten(isMitUnterstuecklisten());
		paramDto.setSatzgroesse(getSatzgroesse());
		paramDto.setSortierung(getSortierung());
		paramDto.setUeberAlleMandanten(isUeberAlleMandanten());
		paramDto.setStrukturMap(getStrukturMap());
		paramDto.setRuntime(getRuntime());
		return paramDto;
	}


	public ScriptRuntime getRuntime() {
		return runtime;
	}


	public StrukturDatenParamDto setRuntime(ScriptRuntime runtime) {
		this.runtime = runtime;
		return this;
	}
}
