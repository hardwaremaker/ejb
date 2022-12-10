package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.lp.server.artikel.service.ArtikelDto;

public class LosgutschlechtRueckmeldungDto implements Serializable {
	Integer losgutschlechtIId;
	public Integer getLosgutschlechtIId() {
		return losgutschlechtIId;
	}
	public void setLosgutschlechtIId(Integer losgutschlechtIId) {
		this.losgutschlechtIId = losgutschlechtIId;
	}
	public Map<Integer, BigDecimal> getMaterialbuchungNichtDurchgefuehrt() {
		return materialbuchungNichtDurchgefuehrt;
	}
	public void setMaterialbuchungNichtDurchgefuehrt(
			Map<Integer, BigDecimal> materialbuchungNichtDurchgefuehrt) {
		this.materialbuchungNichtDurchgefuehrt = materialbuchungNichtDurchgefuehrt;
	}
	Map<Integer, BigDecimal> materialbuchungNichtDurchgefuehrt=new LinkedHashMap<Integer, BigDecimal>();
}
