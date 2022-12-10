package com.lp.server.auftrag.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderResponseEdiOrdRsp implements IOrderResponse {
	private DateFormat controlFormat = new SimpleDateFormat("yyMMddHHmmss") ;
	private static final String eol = "'\n";

	private Integer versandwegId;
	private Integer partnerId;
	private StringBuffer ediContent = 
			new StringBuffer("UNA:+.? ").append(eol);
	private int segmentCount = 0;
	private String controlReference = "";
	
	private int postStatusCode;
	private String postResponseContent;
	private String postResponseType;

	public OrderResponseEdiOrdRsp() {
		controlReference = controlFormat.format(new Date());
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
	
	public void setControlReference(String reference) {
		this.controlReference = reference;
	}
	
	public String getControlReference() {
		return this.controlReference;
	}
	
	public void addSegment(String segment, String content) {
		ediContent.append(segment).append("+").append(content).append(eol);
		segmentCount++;
	}
	
	public void addSegment(String segment, StringBuffer content) {
		addSegment(segment, content.toString());
	}
	
	public String asEdiContent() {
		appendFooter();
		return ediContent.toString();
	}
		
	private void appendFooter() {
		addSegment("UNS", "S");
		ediContent.append("UNT+").append(segmentCount)
			.append("+").append(controlReference).append(eol);
		ediContent.append("UNZ+1+").append(controlReference).append(eol);		
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
