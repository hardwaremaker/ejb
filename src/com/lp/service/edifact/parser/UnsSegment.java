package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.UnsInfo;

public class UnsSegment extends EdifactSegment<UnsInfo> {
	public UnsSegment() {
		this(new UnsInfo());
	}
	
	public UnsSegment(UnsInfo unsInfo) {
		super("UNS");
		set(unsInfo);
		
		/* 010 section identification */
		addData(6, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setSectionIdentification(value);
			}
		});		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected UnsInfo createImpl() {
		return new UnsInfo();
	}
}
