package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseDateFormatException;
import com.lp.service.edifact.schema.UnzInfo;

public class UnzSegment extends EdifactSegment<UnzInfo> {
	public UnzSegment() {
		this(new UnzInfo());
	}
	
	public UnzSegment(UnzInfo unzInfo) {
		super("UNZ");
		set(unzInfo);
		
		/* 010 interchange control count */
		addData(6, new IValueSetter() {
			public void set(String value) throws EdifactException {
				try {
					Integer count = Integer.parseInt(value);
					get().setControlCount(count);
				} catch(NumberFormatException e) {
					throw new ParseDateFormatException(e, 0, 0);
				}
			}
		});
		
		/* 010 number of segments in message */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setControlReference(value);
			}
		});	
	}
	
	@Override
	protected UnzInfo createImpl() {
		return new UnzInfo();
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}
