package com.lp.server.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.help.UnsupportedOperationException;

import com.lp.server.util.report.MwstsatzReportDto;

public class BelegSteuerDto implements Serializable, Iterable<MwstsatzReportDto> {
	private static final long serialVersionUID = 3240270075910213114L;

	private BigDecimal totalUst;
	private Map<Integer, MwstsatzReportDto> mwstMap;
	
	
	public BigDecimal getTotalUst() {
		return totalUst;
	}
	public void setTotalUst(BigDecimal totalUst) {
		this.totalUst = totalUst;
	}
	
	public Map<Integer, MwstsatzReportDto> getMwstMap() {
		return mwstMap;
	}
	public void setMwstMap(Map<Integer, MwstsatzReportDto> mwstMap) {
		this.mwstMap = mwstMap;
	}
	
	/**
	 * Iterator &uuml;ber alle bekannten Steuers&auml;tze
	 * @return Iterator
	 */
	public Iterator<MwstsatzReportDto> iteratorAll() {
		return mwstMap.values().iterator();
	}

	/**
	 * Iterator &uuml;ber jene Steuers&auml;tze die einen Betrag haben
	 * 
	 * @return Iterator &uuml;ber jene Steuers&auml;tze die auch tats&auml;chlich 
	 * verwendet wurden
	 */
	public Iterator<MwstsatzReportDto> iterator() {
		Iterator<MwstsatzReportDto> it = new Iterator<MwstsatzReportDto>() {
			private int index = -1;
			private MwstsatzReportDto[] values = mwstMap.values()
					.toArray(new MwstsatzReportDto[mwstMap.size()]);
			
			@Override
			public boolean hasNext() {
				int localIndex = index;
				while(++localIndex < values.length - 1) {
					if(values[localIndex].getNSummeMwstbetrag().signum() != 0) {
						return true;
					}
				}
				return false;
			}
			
			@Override
			public MwstsatzReportDto next() {
				int localIndex = index;
				while(++localIndex < values.length - 1) {
					if(values[localIndex].getNSummeMwstbetrag().signum() != 0) {
						index = localIndex;
						return values[index];
					}
				}
				throw new NoSuchElementException();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
		return it;
	}
}
