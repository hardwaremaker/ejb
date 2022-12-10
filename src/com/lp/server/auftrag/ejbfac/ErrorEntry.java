package com.lp.server.auftrag.ejbfac;

public class ErrorEntry<T> extends ChangeEntry<T> {
	public ErrorEntry(T expectedValue, T presentedValue, String msg) {
		super(Level.Error, expectedValue, presentedValue, msg);
	}
}
