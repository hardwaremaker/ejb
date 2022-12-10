package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseDateFormatException;
import com.lp.service.edifact.schema.UntInfo;

public class UntSegment extends EdifactSegment<UntInfo> {
	public UntSegment() {
		this(new UntInfo());
	}
	
	public UntSegment(UntInfo untInfo) {
		super("UNT");
		set(untInfo);
		
		/* 010 number of segments in message */
		addData(6, new IValueSetter() {
			public void set(String value) throws EdifactException {
				try {
					Integer count = Integer.parseInt(value);
					get().setSegmentCount(count);
				} catch(NumberFormatException e) {
					throw new ParseDateFormatException(e, 0, 0);
				}
			}
		});
		
		/* 010 number of segments in message */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setReferenceNumber(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected UntInfo createImpl() {
		return new UntInfo();
	}
}
