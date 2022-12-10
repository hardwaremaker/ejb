package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lp.server.eingangsrechnung.service.VendidataImportStats;
import com.lp.util.EJBVendidataArticleExceptionLP;

public class VendidataArticleConsumptionImportResult implements Serializable {
	
	private static final long serialVersionUID = 6353566110663459630L;

	private List<EJBVendidataArticleExceptionLP> importErrors;
	private VendidataImportStats stats;
	private Map<Integer, LosDto> usedLosDtos;
	private Set<Integer> includedTours;

	public VendidataArticleConsumptionImportResult(List<EJBVendidataArticleExceptionLP> importErrors, VendidataImportStats stats) {
		setImportErrors(importErrors);
		setStats(stats);
	}

	public VendidataArticleConsumptionImportResult() {
		this(new ArrayList<EJBVendidataArticleExceptionLP>(), new VendidataImportStats());
	}

	public List<EJBVendidataArticleExceptionLP> getImportErrors() {
		return importErrors;
	}

	public void setImportErrors(List<EJBVendidataArticleExceptionLP> importErrors) {
		this.importErrors = importErrors;
	}

	public VendidataImportStats getStats() {
		return stats;
	}

	public void setStats(VendidataImportStats stats) {
		this.stats = stats;
	}
	
	public Map<Integer, LosDto> getUsedLosDtos() {
		if (usedLosDtos == null) {
			usedLosDtos = new HashMap<Integer, LosDto>();
		}
		return usedLosDtos;
	}
	
	public List<LosDto> getUsedLosDtosAsList() {
		return new ArrayList<LosDto>(getUsedLosDtos().values());
	}

	public void addUsedLosDto(LosDto losDto) {
		getUsedLosDtos().put(losDto.getIId(), losDto);
	}

	public Set<Integer> getIncludedTours() {
		if (includedTours == null) {
			includedTours = new HashSet<Integer>();
		}
		return includedTours;
	}

	public void addIncludedTour(Integer tour) {
		getIncludedTours().add(tour);
	}

	public void addData(VendidataArticleConsumptionImportResult result) {
		if (result == null) return;
		
		if (result.getImportErrors() != null) {
			getImportErrors().addAll(result.getImportErrors());
		}
		
		if (result.getStats() != null) {
			getStats().setErrorCounts(getStats().getErrorCounts() + result.getStats().getErrorCounts());
			getStats().setErrorImportsAusgangsgutschriften(getStats().getErrorImportsAusgangsgutschriften() + result.getStats().getErrorImportsAusgangsgutschriften());
			getStats().setGoodImportsAusgangsgutschriften(getStats().getGoodImportsAusgangsgutschriften() + result.getStats().getGoodImportsAusgangsgutschriften());
			getStats().setTotalImportsAusgangsgutschriften(getStats().getGoodImportsAusgangsgutschriften() + result.getStats().getGoodImportsAusgangsgutschriften());
		}
		
		getIncludedTours().addAll(result.getIncludedTours());
		getUsedLosDtos().putAll(result.getUsedLosDtos());
	}

}
