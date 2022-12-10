package com.lp.server.lieferschein.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.lieferschein.service.errors.StmException;

public class EasydataImportResult implements Serializable {
	private static final long serialVersionUID = -4085643380083535404L;

	private List<StmException> errors;
	private EasydataImportStats stats;
	private List<LieferscheinDto> lieferscheine;
	
	public EasydataImportResult() {
	}
	
	public EasydataImportResult(List<StmException> errors) {
		this(errors, new EasydataImportStats());
	}
	
	public EasydataImportResult(List<StmException> errors, EasydataImportStats stats) {
		setErrors(errors);
		setStats(stats);
	}

	public List<StmException> getErrors() {
		if (errors == null) {
			errors = new ArrayList<StmException>();
		}
		return errors;
	}
	
	public void setErrors(List<StmException> errors) {
		this.errors = errors;
	}
	
	public boolean hasErrors() {
		return getErrors().size() > 0;
	}
	
	public EasydataImportStats getStats() {
		return stats;
	}
	
	public void setStats(EasydataImportStats stats) {
		this.stats = stats;
	}

	public void setLieferscheine(List<LieferscheinDto> lieferscheine) {
		this.lieferscheine = lieferscheine;
	}
	
	public List<LieferscheinDto> getLieferscheine() {
		if (lieferscheine == null) {
			lieferscheine = new ArrayList<LieferscheinDto>();
		}
		return lieferscheine;
	}
}
