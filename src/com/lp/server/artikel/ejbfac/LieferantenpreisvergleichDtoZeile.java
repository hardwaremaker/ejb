package com.lp.server.artikel.ejbfac;

import java.math.BigDecimal;

public class LieferantenpreisvergleichDtoZeile {


	public BigDecimal getPreis() {
		return preis;
	}
	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}
	public Integer getWbz() {
		return wbz;
	}
	public void setWbz(Integer wbz) {
		this.wbz = wbz;
	}
	public boolean isbGuenstigster() {
		return bGuenstigster;
	}
	public void setbGuenstigster(boolean bGuenstigster) {
		this.bGuenstigster = bGuenstigster;
	}
	public boolean isbSchnellster() {
		return bSchnellster;
	}
	public void setbSchnellster(boolean bSchnellster) {
		this.bSchnellster = bSchnellster;
	}
	private BigDecimal preis;
	private Integer wbz;
	private boolean bGuenstigster;
	private boolean bSchnellster;
	
	private boolean bNichtLieferbar=false;

	public boolean isbNichtLieferbar() {
		return bNichtLieferbar;
	}
	public void setbNichtLieferbar(boolean bNichtLieferbar) {
		this.bNichtLieferbar = bNichtLieferbar;
	}
	
	
	
}
