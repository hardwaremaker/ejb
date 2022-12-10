package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Transient;

public class RechnungElektronischEdi4All implements IRechnungElektronisch, Serializable {
	private static final long serialVersionUID = 5175020661900805170L;

	private Integer versandwegId;
	private Integer partnerId;
	private String edi4All;
	private RechnungDto rechnungDto;
	
	@Transient
	private transient StringBuffer edi4AllSb;
	@Transient
	private transient StringBuffer eol;
	@Transient
	private transient SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	public RechnungElektronischEdi4All() {
		setEdi4AllSb(new StringBuffer());
		eol = new StringBuffer("\r\n");
	}
	
	@Override
	public Integer getVersandwegId() {
		return versandwegId;
	}

	@Override
	public void setVersandwegId(Integer versandwegId) {
		this.versandwegId = versandwegId;
	}

	@Override
	public Integer getPartnerId() {
		return partnerId;
	}

	@Override
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	@Override
	public RechnungDto getRechnungDto() {
		return rechnungDto;
	}

	@Override
	public void setRechnungDto(RechnungDto rechnungDto) {
		this.rechnungDto = rechnungDto;
	}

	public StringBuffer getEdi4AllSb() {
		return edi4AllSb;
	}

	public void setEdi4AllSb(StringBuffer edi4AllSb) {
		this.edi4AllSb = edi4AllSb;
	}

	public void addSeparator() {
		getEdi4AllSb().append(eol);
	}
	
	public void addField(String kennung, String content) {
		getEdi4AllSb().append(kennung).append(content).append(eol);
	}

	/**
	 * Einen String mit maximaler L&auml;nge ausgeben
	 * @param kennung
	 * @param content
	 * @param maxContentLength
	 */
	public void addField(String kennung, String content, int maxContentLength) {
		String s = content.length() > maxContentLength 
				? content.substring(0, maxContentLength)
				: content;
		addField(kennung, s);
	}
	
	public void addField(String kennung, Date date) {
		getEdi4AllSb().append(kennung).append(sdf.format(date)).append(eol);
	}
	
	public void addField(String kennung, BigDecimal value) {
		getEdi4AllSb().append(kennung).append(value.toPlainString()).append(eol);
	}
	
	/* 0.000 -> 0 */
	public void addFieldNoTrailingZero(String kennung, BigDecimal value) {
		getEdi4AllSb().append(kennung).append(
			value.stripTrailingZeros().toPlainString()).append(eol);
	}
	
	/* 0.0000 -> 0.00 */
	/* 1.0034 -> 100.34/100 */
	private String format2Digits(BigDecimal value) {
		BigDecimal b = value.stripTrailingZeros();
		int scale = b.scale();
		if(scale < 2) {
			b = b.setScale(2);
			scale = b.scale();
		}

		int shiftedDigits = 0;
		if(scale > 2) {
			shiftedDigits = scale - 2;
			b = b.scaleByPowerOfTen(shiftedDigits);
		}
		String s = b.toPlainString();
		if(shiftedDigits > 0) {
			s += "/1";
			while(shiftedDigits > 0) {
				s += "0";
				--shiftedDigits;
			}
		}
		return s;
	}
	
	/* 0.0000 -> 0.00 */
	/* 1.0034 -> 100.34/100 */
	public void addField2Digits(String kennung, BigDecimal value) {
		getEdi4AllSb()
			.append(kennung)
			.append(format2Digits(value)).append(eol);
/*		
		BigDecimal b = value.stripTrailingZeros();
		int scale = b.scale();
		if(scale < 2) {
			b = b.setScale(2);
			scale = b.scale();
		}

		int shiftedDigits = 0;
		if(scale > 2) {
			shiftedDigits = scale - 2;
			b = b.scaleByPowerOfTen(shiftedDigits);
		}
		String s = b.toPlainString();
		if(shiftedDigits > 0) {
			s += "/1";
			while(shiftedDigits > 0) {
				s += "0";
				--shiftedDigits;
			}
		}
		getEdi4AllSb().append(kennung).append(s).append(eol);
*/		
	}
	
	public void addField2Digits(String kennung, BigDecimal value1, BigDecimal value2) {
		getEdi4AllSb()
			.append(kennung)
			.append(format2Digits(value1))
			.append(";")
			.append(format2Digits(value2)).append(eol);		
	}

	public void addField2Digits(String kennung, Double value1, BigDecimal value2) {
		getEdi4AllSb()
			.append(kennung)
			.append(format2Digits(new BigDecimal(value1.doubleValue())))
			.append(";")
			.append(format2Digits(value2)).append(eol);		
	}

	public void addField(String kennung, Double value) {
		if(value != null) {
			addField(kennung, new BigDecimal(value.doubleValue()));
		}
	}
	
	public void addField2Digits(String kennung, Double value) {
		if(value != null) {
			addField2Digits(kennung, new BigDecimal(value.doubleValue()));
		}
	}
	
	public void addFieldNoTrailingZero(String kennung, Double value) {
		if(value != null) {
			addFieldNoTrailingZero(kennung, new BigDecimal(value.doubleValue()));
		}
	}
		
	public String getEdi4All() {
		return edi4All;
	}
	
	public void setEdi4All(String edi4All) {
		this.edi4All = edi4All;
	}
	
	public void persist() {
		setEdi4All(getEdi4AllSb().toString());
	}
	
	public String toString() {
		return getEdi4All();
	}	
	
	private String asPlainString(BigDecimal value) {
		return removeTrailingZero(value.toPlainString());
	}
	
	private String removeTrailingZero(String s) {
		int index = s.indexOf('.');
		if(index > -1) {
			int j = s.length() - 1;
			while(j >= index && s.charAt(j) == '0') {
				--j;
			}
			if(s.charAt(j) != '.') {
				++j;
			}
			s = s.substring(0, j);
		}
		
		return s;
	}
}
