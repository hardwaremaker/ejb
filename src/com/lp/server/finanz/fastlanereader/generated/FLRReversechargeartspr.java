package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;

import com.lp.server.system.fastlanereader.generated.FLRLocale;

public class FLRReversechargeartspr implements Serializable {
	private static final long serialVersionUID = 5161085321840478354L;

	private String c_bez ;
	
	private FLRLocale locale ;
	
	private FLRReversechargeart reversechargeart ;
	
	public FLRReversechargeartspr() {
	}
	
	public FLRReversechargeartspr(String c_bez, FLRReversechargeart reversechargeart, FLRLocale locale) {
		this.setC_bez(c_bez) ;
		this.setReversechargeart(reversechargeart) ;
		this.setLocale(locale) ;
	}

	public String getC_bez() {
		return c_bez;
	}

	public void setC_bez(String c_bez) {
		this.c_bez = c_bez;
	}

	public FLRLocale getLocale() {
		return locale;
	}

	public void setLocale(FLRLocale locale) {
		this.locale = locale;
	}

	public FLRReversechargeart getReversechargeart() {
		return reversechargeart;
	}

	public void setReversechargeart(FLRReversechargeart reversechargeart) {
		this.reversechargeart = reversechargeart;
	}
}
