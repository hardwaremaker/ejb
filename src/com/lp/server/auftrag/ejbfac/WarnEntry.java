package com.lp.server.auftrag.ejbfac;

public class WarnEntry<T> extends ChangeEntry<T> {
	public WarnEntry(T expectedValue, T presentedValue, String msg) {
		super(Level.Warn, expectedValue, presentedValue, msg);
	}
}
