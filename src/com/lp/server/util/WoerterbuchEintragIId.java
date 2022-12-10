package com.lp.server.util;

public class WoerterbuchEintragIId extends BaseIntegerKey {
	private static final long serialVersionUID = 1L;

	public WoerterbuchEintragIId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WoerterbuchEintragIId(Integer value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	public static WoerterbuchEintragIId emptyKey() {
		return new WoerterbuchEintragIId(null);
	}
	
}
