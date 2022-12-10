package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

public class LieferscheinAvisoEdi4All implements ILieferscheinAviso,
		Serializable {
	private static final long serialVersionUID = 731566765709954837L;
	private Integer versandwegId ;
	private Integer partnerId ;	
	private String edi4All;
	
	@Transient
	private transient StringBuffer edi4AllSb;
	@Transient
	private transient StringBuffer eol;
	@Transient
	private transient SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private LieferscheinDto lieferscheinDto;
	
	public LieferscheinAvisoEdi4All() {
		setEdi4AllSb(new StringBuffer());
		eol = new StringBuffer("\r\n");
	}
	
	@Override
	public Integer getVersandwegId() {
		return versandwegId;
	}

	@Override
	public void setVersandwegId(Integer versandwegId) {
		this.versandwegId = versandwegId;
	}

	@Override
	public Integer getPartnerId() {
		return partnerId;
	}

	@Override
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	@Override
	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}
	
	@Override
	public void setLieferscheinDto(LieferscheinDto lieferscheinDto) {
		this.lieferscheinDto = lieferscheinDto;
	}
	
	public String getEdi4All() {
		return edi4All;
	}
	
	public void setEdi4All(String edi4All) {
		this.edi4All = edi4All;
	}

	public StringBuffer getEdi4AllSb() {
		return edi4AllSb;
	}

	public void setEdi4AllSb(StringBuffer edi4AllSb) {
		this.edi4AllSb = edi4AllSb;
	}
	
	public void addSeparator() {
		getEdi4AllSb().append(eol);
	}
	
	public void addField(String kennung, String content) {
		getEdi4AllSb().append(kennung).append(content).append(eol);
	}
	
	/**
	 * Einen String mit maximaler L&auml;nge ausgeben
	 * @param kennung
	 * @param content
	 * @param maxContentLength
	 */
	public void addField(String kennung, String content, int maxContentLength) {
		String s = content.length() > maxContentLength 
				? content.substring(0, maxContentLength)
				: content;
		addField(kennung, s);
	}
	
	public void addField(String kennung, Date date) {
		getEdi4AllSb().append(kennung).append(sdf.format(date)).append(eol);
	}
	
	public void addField(String kennung, BigDecimal value) {
		getEdi4AllSb()
			.append(kennung)
			.append(asPlainString(value)).append(eol);
	}
	
	public void persist() {
		setEdi4All(getEdi4AllSb().toString());
	}
	
	public String toString() {
		return getEdi4All();
	}
	
	private String asPlainString(BigDecimal value) {
		return removeTrailingZero(value.toPlainString());
	}
	
	private String removeTrailingZero(String s) {
		int index = s.indexOf('.');
		if(index > -1) {
			int j = s.length() - 1;
			while(j >= index && s.charAt(j) == '0') {
				--j;
			}
			if(s.charAt(j) != '.') {
				++j;
			}
			s = s.substring(0, j);
		}
		
		return s;
	}
}
