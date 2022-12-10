package com.lp.service.edifact.parser;

import java.math.BigInteger;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseQuantityException;
import com.lp.service.edifact.schema.CntInfo;

public class CntSegment extends EdifactSegment<CntInfo> {
	public CntSegment() {
		this(new CntInfo());
	}
	
	public CntSegment(CntInfo cntInfo) {
		super("CNT");
		set(cntInfo);
		
		/* 010 code type identification */
		addData(3, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setQualifier(value);
			}
		});		

		/* 020 total quantity */
		addComposite(18, false, new IValueSetter() {
			public void set(String value) throws EdifactException {
				try {
					get().setValue(new BigInteger(value));					
				} catch(NumberFormatException e) {
					throw new ParseQuantityException(e, 0, 0);					
				}
			}
		});		

		/* 010 code type identification */
		addComposite(8, true, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setMeasureUnit(value);
			}
		});		
	}
	
	@Override
	protected CntInfo createImpl() {
		return new CntInfo();
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}	
