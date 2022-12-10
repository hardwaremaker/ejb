package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.persistence.Transient;

public class LieferscheinAvisoLinienabruf implements ILieferscheinAviso,
		Serializable {
	private static final long serialVersionUID = 731566765709954837L;
	private Integer versandwegId ;
	private Integer partnerId ;	
	private String content;
	
	@Transient
	private transient StringBuffer contentSb;
	@Transient
	private transient StringBuffer eol;
	@Transient
	private transient SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
	private LieferscheinDto lieferscheinDto;
	
	public LieferscheinAvisoLinienabruf() {
		setContentSb(new StringBuffer());
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
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public StringBuffer getContentSb() {
		return contentSb;
	}

	public void setContentSb(StringBuffer edi4AllSb) {
		this.contentSb = edi4AllSb;
	}
	
	public void addSeparator() {
		getContentSb().append(eol);
	}
	
	public void addHeader(String headerline) {
		getContentSb().append(headerline);
	}
	
	public void addLine(String line) {
		getContentSb().append(line);
	}
	
	public void persist() {
		setContent(getContentSb().toString());
	}
	
	public String toString() {
		return getContent();
	}
}
