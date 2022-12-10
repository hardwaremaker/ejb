package com.lp.util;

public class EJBVendidataArticleExceptionLP extends EJBExceptionLP {
	
	private static final long serialVersionUID = 3925588603050665451L;

	public static final Integer SEVERITY_INFO = 1;
	public static final Integer SEVERITY_WARNING = 2;
	public static final Integer SEVERITY_ERROR = 3;

	private Integer fourVendingId;
	private String fourVendingName;
	private Integer tourNr;
	private Integer severity;

	public EJBVendidataArticleExceptionLP(Integer fourVendingId, String fourVendingName, Integer tourNr, Integer severity, Integer code) {
		super(code, "");
		setFourVendingId(fourVendingId);
		setFourVendingName(fourVendingName);
		setTourNr(tourNr);
		setSeverity(severity);
	}
	
	public EJBVendidataArticleExceptionLP(Integer severity, Integer code, Exception e) {
		super(code, e);
		setSeverity(severity);
	}
	
	public EJBVendidataArticleExceptionLP(Integer severity, Integer code) {
		super(code, "");
		setSeverity(severity);
	}

	public EJBVendidataArticleExceptionLP(Integer severity, Integer code, Object... infoForTheClient) {
		super(code, "", infoForTheClient);
		setSeverity(severity);
	}

	public EJBVendidataArticleExceptionLP(Integer fourVendingIId, String fourVendingName, Integer tourNr, Integer severity,
			int code, Exception ex) {
		super(code, ex);
		setFourVendingId(fourVendingIId);
		setFourVendingName(fourVendingName);
		setTourNr(tourNr);
		setSeverity(severity);
	}

	public Integer getFourVendingId() {
		return fourVendingId;
	}

	public void setFourVendingId(Integer fourVendingId) {
		this.fourVendingId = fourVendingId;
	}

	public String getFourVendingName() {
		return fourVendingName;
	}

	public void setFourVendingName(String fourVendingName) {
		this.fourVendingName = fourVendingName;
	}

	public Integer getTourNr() {
		return tourNr;
	}

	public void setTourNr(Integer tourNr) {
		this.tourNr = tourNr;
	}

	public Integer getSeverity() {
		return severity;
	}

	public void setSeverity(Integer severity) {
		this.severity = severity;
	}

}
