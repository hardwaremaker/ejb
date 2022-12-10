package com.lp.service.edifact.parser;

import java.math.BigDecimal;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.CuxInfo;

public class CuxSegment extends EdifactSegment<CuxInfo> {
	public CuxSegment() throws EdifactException {
		this(new CuxInfo());
	}
	
	public CuxSegment(CuxInfo cuxInfo) throws EdifactException {
		super("CUX");
		set(cuxInfo);
		
		addData(3, new IValueSetter() {		
			public void set(String value) throws EdifactException {
				get().setDetailsQualifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setCodedCurrency(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setQualifier(value);
			}
		});
		addComposite(4, true, new IValueSetter() {
			public void set(String value) {
				get().setBaseRate(new BigDecimal(value));
			}
		});
		
		addData(12, new IValueSetter() {
			public void set(String value) {
				get().setExchangeRate(new BigDecimal(value));
			}
		});
		
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setExchangeCurrency(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected CuxInfo createImpl() {
		return new CuxInfo();
	}
}
