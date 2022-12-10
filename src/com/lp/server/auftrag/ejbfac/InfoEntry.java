package com.lp.server.auftrag.ejbfac;

public class InfoEntry<T> extends ChangeEntry<T> {
	public InfoEntry(T expectedValue, T presentedValue, String msg) {
		super(Level.Info, expectedValue, presentedValue, msg);
	}
}
