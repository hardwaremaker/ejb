package com.lp.server.rechnung.ejbfac;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.system.service.LandDto;

public class CoinRoundingInfo implements Serializable {
	private static final long serialVersionUID = 5143218716716005164L;

	private BigDecimal value;
	private boolean roundTax;
	private boolean roundDown;
	private String countryCurrency;
	private String slipCurrency;
	
	public CoinRoundingInfo() {
		this(null, false, "", "");
	}
	
	public CoinRoundingInfo(LandDto landDto, String slipCurrency) {
		this(landDto.getNMuenzRundung(), 
				landDto.getBMwstMuenzRundung() > 0, landDto.getWaehrungCNr(), slipCurrency);
	}
	
	public CoinRoundingInfo(BigDecimal roundingValue, boolean roundTax, String landesWaehrungCnr, String belegWaehrungCnr) {
		if(roundingValue != null) {
			this.value = roundingValue.abs();
			this.roundDown = roundingValue.signum() < 0;			
		} else {
			this.value = null;
			this.roundDown = false;
		}
		this.roundTax = roundTax;
		this.countryCurrency = landesWaehrungCnr;
		this.slipCurrency = belegWaehrungCnr;
	}

	public BigDecimal getValue() {
		return value;
	}

	public boolean isRoundTax() {
		return roundTax;
	}
	
	public boolean isRoundDown() {
		return roundDown;
	}
	
	public boolean isRounding() {
		return value != null && value.signum() != 0;
	}
	
	public String getCurrency() {
		return countryCurrency;
	}
	
	public String getSlipCurrency() {
		return slipCurrency;
	}
	
	/**
	 * Soll gerundet werden?
	 * <p>Dies bedingt, dass prinzipiell f&uuml;r das Land (dessen W&auml;hrung verwendet wird)
	 * &uuml;berhaupt die M&uuml;nzrundung aktiv ist und der Beleg auch in dieser W&auml;hrung
	 * ausgestellt ist</p>
	 * @return true wenn im Land M&uuml;nzrundung aktiv und diese W&auml;hrung der Belegw&auml;hrung entspricht
	 */
	public boolean needsRounding() {
		return isRounding() && countryCurrency.equals(slipCurrency);
	}
}
