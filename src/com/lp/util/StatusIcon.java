package com.lp.util;

import java.io.Serializable;

public class StatusIcon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String icon=null;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTooltip() {
		return tooltip;
	}
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	private String tooltip=null;

}
