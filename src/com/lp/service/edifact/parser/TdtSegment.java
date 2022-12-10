package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.TdtInfo;

public class TdtSegment extends EdifactSegment<TdtInfo> {
	public TdtSegment() {
		this(new TdtInfo());
	}
	
	public TdtSegment(TdtInfo tdtInfo) {
		super("TDT");
		set(tdtInfo);
		
		/* 010 party function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setTransportStage(value);
			}
		});
		
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	protected TdtInfo createImpl() {
		return new TdtInfo();
	}
}
