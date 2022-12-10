package com.lp.server.system.service;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.lp.server.util.HvOptional;

public class HvBelegnummernformatHistorisch implements IHvBelegnummernformat, Serializable {
	private static final long serialVersionUID = -7307047274224915850L;

	private String pattern;
	private boolean valid = false;
	private static final Pattern BnPattern = Pattern.compile("J.*#");
	private HvOptional<HvBelegnummernformat> bnFormat = HvOptional.empty();
	
	public HvBelegnummernformatHistorisch(String pattern) {
		this.pattern = pattern;
		validatePattern(pattern);
	}
	
	public boolean validatePattern(String pattern) {
		valid = BnPattern.matcher(pattern).matches();
		if (!valid) return false;
		
		valid = decode(pattern);
		return valid;
	}
	
	private boolean decode(String pattern) {
		Integer lastIdxGj = pattern.lastIndexOf("J");
		if (lastIdxGj < 0) return false;
		
		Integer firstIdxBelegnr = pattern.indexOf("#");
		if (firstIdxBelegnr < 0) return false;
		
		String trennzeichen = pattern.substring(lastIdxGj + 1, firstIdxBelegnr);
		bnFormat = HvOptional.of(new HvBelegnummernformat(lastIdxGj + 1, pattern.length() - firstIdxBelegnr, trennzeichen, null));
		
		return true;
	}
	
	public boolean isValidPattern() {
		return valid;
	}

	@Override
	public Integer getLaengeBelegnr() {
		return bnFormat.isPresent() ? bnFormat.get().getLaengeBelegnr() : 0;
	}

	@Override
	public Integer getLaengeGj() {
		return bnFormat.isPresent() ? bnFormat.get().getLaengeGj() : 0;
	}

	@Override
	public String getTrennzeichen() {
		return bnFormat.isPresent() ? bnFormat.get().getTrennzeichen() : null;
	}

	@Override
	public String getMandantkennung() {
		return bnFormat.isPresent() ? bnFormat.get().getMandantkennung() : null;
	}

	@Override
	public Integer getLaengeGesamt() {
		return bnFormat.isPresent() ? bnFormat.get().getLaengeGesamt() : 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!IHvBelegnummernformat.class.isInstance(obj))
			return false;
			
		IHvBelegnummernformat other = (IHvBelegnummernformat)obj;
		if (getLaengeGesamt() == null) {
			if (other.getLaengeGesamt() != null) return false;
		} else if (!getLaengeGesamt().equals(other.getLaengeGesamt())) {
			return false;
		}
		if (getLaengeBelegnr() == null) {
			if (other.getLaengeBelegnr() != null) return false;
		} else if (!getLaengeBelegnr().equals(other.getLaengeBelegnr())) {
			return false;
		}
		if (getLaengeGj() == null) {
			if (other.getLaengeGj() != null) return false;
		} else if (!getLaengeGj().equals(other.getLaengeGj())) {
			return false;
		}
		if (getTrennzeichen() == null) {
			if (other.getTrennzeichen() != null) return false;
		} else if (!getTrennzeichen().equals(other.getTrennzeichen())) {
			return false;
		}
		if (StringUtils.isEmpty(getMandantkennung())) {
			if (!StringUtils.isEmpty(getMandantkennung())) return false;
		} else if (!getMandantkennung().equals(other.getMandantkennung())) {
			return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLaengeBelegnr() == null) ? 0 : getLaengeBelegnr().hashCode());
		result = prime * result + ((getLaengeGj() == null) ? 0 : getLaengeGj().hashCode());
		result = prime * result + ((getTrennzeichen() == null) ? 0 : getTrennzeichen().hashCode());
		result = prime * result + ((getMandantkennung() == null) ? 0 : getMandantkennung().hashCode());
		return result;
	}
}
