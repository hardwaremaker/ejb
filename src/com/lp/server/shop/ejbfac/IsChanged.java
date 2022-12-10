package com.lp.server.shop.ejbfac;

public interface IsChanged<T> {
	/**
	 * Hat sich das Datenobjekt ge&auml;ndert und sollte daher
	 * weiter verarbeitet werden?
	 * 
	 * @param dto
	 * @return true wenn das Datenobjekt weiterverarbeitet werden soll
	 */
	boolean isChanged(T dto);
}
