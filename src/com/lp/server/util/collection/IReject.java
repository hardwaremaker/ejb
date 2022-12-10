package com.lp.server.util.collection;

public interface IReject<T> {
	/**
	 * Soll das Element zurueckgewiesen werden
	 * @param element
	 * @return true wenn das angegebene Element zurueckgewiesen, also nicht akzeptiert, werden soll
	 */
	boolean reject(T element);
}
