package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.LieferscheinId;

public class PaketVersandAntwortDto implements Serializable {
	private static final long serialVersionUID = 1318653028529744412L;

	private LieferscheinId lieferscheinId;
	private String errorCode;
	private String errorMessage;
	private List<String> trackingNumbers;
	private boolean ok;
	private String pdfContent;
	private String rawContent;
	private Exception exception;
	
	protected PaketVersandAntwortDto() {
		beOk();
		setTrackingNumbers(new ArrayList<String>());	
	}
	
	public PaketVersandAntwortDto(Exception e) {
		this.exception = e;
		this.errorCode = "HV-0003";
		this.errorMessage = e.getMessage();
	}
	
	public PaketVersandAntwortDto(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;	
	}
	
	public PaketVersandAntwortDto(List<String> trackingNumbers) {
		beOk();
		this.trackingNumbers = trackingNumbers;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public List<String> getTrackingNumbers() {
		return trackingNumbers;
	}

	public PaketVersandAntwortDto setTrackingNumbers(List<String> trackingNumbers) {
		this.trackingNumbers = trackingNumbers;
		return this;
	}

	public boolean isOk() {
		return ok;
	}

	private void beOk() {
		this.ok = true;
	}

	/**
	 * pdfContent ist base64 codiert.
	 * 
	 * @return null oder das base64 codierte PDF 
	 */
	public String getPdfContent() {
		return pdfContent;
	}

	public PaketVersandAntwortDto setPdfContent(String pdfContent) {
		this.pdfContent = pdfContent;
		return this;
	}

	public String getRawContent() {
		return rawContent;
	}

	public PaketVersandAntwortDto setRawContent(String rawContent) {
		this.rawContent = rawContent;
		return this;
	}
	
	public LieferscheinId getLieferscheinId() {
		return lieferscheinId;
	}

	public PaketVersandAntwortDto setLieferscheinId(LieferscheinId lieferscheinId) {
		this.lieferscheinId = lieferscheinId;
		return this;
	}
	
	public PaketVersandAntwortDto setException(Exception e) {
		this.exception = e;
		return this;
	}
	
	public Exception getException() {
		return this.exception;
	}
}
