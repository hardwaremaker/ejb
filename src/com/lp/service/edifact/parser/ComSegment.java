package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.ComInfo;

public class ComSegment extends EdifactSegment<ComInfo> {
	public ComSegment() {
		this(new ComInfo());
	}
	
	public ComSegment(ComInfo comInfo) {
		super("COM");
		set(comInfo);
		
		/* 010 communication contact */
		addData(512, new IValueSetter() {
			public void set(String value) {
				get().setAddressIdentifier(value);
			}
		});

		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setAddressCode(value);
			}
		});		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected ComInfo createImpl() {
		return new ComInfo();
	}
}
