package com.lp.server.shop.service;

import javax.ejb.Remote;

@Remote
public interface ShopTimerFac {
	public static final long DEFAULT_DURATION = 1l * 60l * 1000l;
	
	void setTimer(long millisTillStart);
}
