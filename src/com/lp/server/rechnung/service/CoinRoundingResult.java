package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.server.system.service.MwstsatzDto;

public class CoinRoundingResult implements Serializable {
	private static final long serialVersionUID = 7871251356307400099L;
	
	private BigDecimal bruttoAmount;
	private BigDecimal taxAmount;
	private BigDecimal taxAmountFW;
	public BigDecimal getTaxAmountFW() {
		return taxAmountFW;
	}

	public void setTaxAmountFW(BigDecimal taxAmountFW) {
		this.taxAmountFW = taxAmountFW;
	}

	private boolean validBruttoAmount;
	private boolean needsRounding;
	private boolean needsTaxRounding;
	private MwstsatzDto mwstsatzDto;
	
	/**
	 * Handelt es sich um einen g&uuml;ltigen Bruttobetrag</br>
	 * <p>Ein g&uuml;ltiger Bruttobetrag ist gegeben wenn, entweder im
	 * Land keine M&uuml;nzrundung gew&uuml;nscht ist, oder der vom
	 * Anwender erfasste Betrag rund ist, also dem gerundeten Betrag
	 * entspricht</p>
	 * <p>Hintergrund: Wir f&uuml;hren keine Rundung eines abh&auml;ngigen
	 * Betrags (wie zum Beispiel der Steuer, sofern gew&uuml;nscht) durch, 
	 * wenn der Bruttobetrag nicht rund ist.</p>
	 * 
	 * @return true wenn entweder nicht gerundet werden muss, oder der
	 * Betrag dem gerundeten Betrag entspricht
	 */
	public boolean isValidBruttoAmount() {
		return validBruttoAmount;
	}
	
	public void setValidBruttoAmount(boolean validBruttoAmount) {
		this.validBruttoAmount = validBruttoAmount;
	}

	/**
	 * Ist eine Rundung prinzpiell gew&uuml;nscht?
	 * 
	 * @return true, wenn im Land die M&uuml;nzrundung aktiviert ist
	 */
	public boolean isNeedsRounding() {
		return needsRounding;
	}

	public void setNeedsRounding(boolean needsRounding) {
		this.needsRounding = needsRounding;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getBruttoAmount() {
		return bruttoAmount;
	}

	public void setBruttoAmount(BigDecimal bruttoAmount) {
		this.bruttoAmount = bruttoAmount;
	}

	/**
	 * Soll der Steuerbetrag gerundet werden?
	 * 
	 * @return true wenn im Land eingestellt ist, dass der Steuerbetrag gerundet werden soll
	 */
	public boolean isNeedsTaxRounding() {
		return needsTaxRounding;
	}

	public void setNeedsTaxRounding(boolean needsTaxRounding) {
		this.needsTaxRounding = needsTaxRounding;
	}

	public MwstsatzDto getMwstsatzDto() {
		return mwstsatzDto;
	}

	public void setMwstsatzDto(MwstsatzDto mwstsatzDto) {
		this.mwstsatzDto = mwstsatzDto;
	}
}
