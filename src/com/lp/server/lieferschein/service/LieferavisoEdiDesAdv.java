package com.lp.server.lieferschein.service;

import com.lp.server.auftrag.ejbfac.EdiSegmentBuilder;

public class LieferavisoEdiDesAdv implements ILieferscheinAviso {
	private Integer versandwegId;
	private Integer partnerId;
	private LieferscheinDto lieferscheinDto;
	private EdiSegmentBuilder segmentBuilder;

	private int postStatusCode;
	private String postResponseContent;
	private String postResponseType;

	public LieferavisoEdiDesAdv(LieferscheinDto lieferscheinDto) {
		this.lieferscheinDto = lieferscheinDto;
		segmentBuilder = new EdiSegmentBuilder();
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
	
	public void addSegment(String segment, String content) {
		segmentBuilder.addSegment(segment, content);
	}
	
	public void addSegment(String segment, StringBuffer content) {
		 segmentBuilder.addSegment(segment, content);
	}
	
	public String asEdiContent() {
		return segmentBuilder.asEdiContent();
	}
	
	public int getPostStatusCode() {
		return postStatusCode;
	}

	public void setPostStatusCode(int postStatusCode) {
		this.postStatusCode = postStatusCode;
	}

	public String getPostResponseContent() {
		return postResponseContent;
	}

	public void setPostResponseContent(String postResponseContent) {
		this.postResponseContent = postResponseContent;
	}

	public String getPostResponseType() {
		return postResponseType;
	}

	public void setPostResponseType(String postResponseType) {
		this.postResponseType = postResponseType;
	}
}
