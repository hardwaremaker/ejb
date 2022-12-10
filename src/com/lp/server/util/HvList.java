package com.lp.server.util;

import java.util.ArrayList;
import java.util.Collection;

public class HvList<T> extends ArrayList<T> {
	private static final long serialVersionUID = 6981816019077024809L;

	public HvList() { 
		super();
	}
	
	public HvList(Collection<T> collection) {
		super(collection);
	}
	
	public HvList(int capacity) {
		super(capacity);
	}
	
	@Override
	public boolean add(T arg0) {
		if(arg0 == null) return false;
		return super.add(arg0);
	}
	
	@Override
	public void add(int arg0, T arg1) {
		if(arg1 != null) {
			super.add(arg0, arg1);			
		}
	}
}
