package com.lp.server.system.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class AutomatikjobSyncResultDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, ResultType> map;

	public AutomatikjobSyncResultDto() {
		map = new TreeMap<>();
	}

	public void addSuccess(String cMandantCNr) {
		map.put(cMandantCNr, new ResultType(true, null));
	}

	public void addException(String cMandantCNr, Exception e) {
		map.put(cMandantCNr, new ResultType(false, e));
	}

	/**
	 * 
	 * @param cMandantCNr
	 * @return true wenn der Mandant erfolgreich synchronisiert wurde, false sonst
	 */
	public boolean mandantErfolgreich(String cMandantCNr) {
		ResultType res = map.get(cMandantCNr);
		return res == null ? false : res.success;
	}

	/**
	 * Wenn die Synchronisation bei diesem Mandanten eine Execption geworfen hat,
	 * wird diese zur&uuml;ck gegeben, ansonten ein leeres HvOptional
	 * 
	 * @param cMandantCNr
	 * @return
	 */
	public Optional<Exception> getExceptionForMandant(String cMandantCNr) {
		return getResultForMandant(cMandantCNr).map(ResultType::getException);
	}

	public Optional<ResultType> getResultForMandant(String cMandantCNr) {
		return Optional.ofNullable(map.get(cMandantCNr));
	}

	public Set<String> getAlleAktualisiertenMandanten() {
		return Collections.unmodifiableSet(map.keySet());
	}

	public static class ResultType implements Serializable {
		private static final long serialVersionUID = 1L;

		public final boolean success;
		private Exception exception;

		public Exception getException() {
			return exception;
		}

		public ResultType(boolean success, Exception exception) {
			super();
			this.success = success;
			this.exception = exception;
		}
	}
}
