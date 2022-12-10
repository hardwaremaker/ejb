package com.lp.server.util.collection;



public interface ISelect<T> {
	/**
	 * Soll das Element aufgenommen werden
	 * @param element
	 * @return true wenn das angegebene Element aufgenommen/akzeptiert werden soll
	 */
	boolean select(T element);
}
