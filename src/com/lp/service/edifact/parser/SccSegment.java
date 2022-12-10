package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.SccInfo;

public class SccSegment extends EdifactSegment<SccInfo> {
	public SccSegment() {
		this(new SccInfo());
	}
	
	public SccSegment(SccInfo sccInfo) {
		super("SCC");
		set(sccInfo);
		
		/* 010 delivery plan commitment code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setPlanCommitmentCode(value);
			}
		});

		/* 020 delivery plan commitment code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setDeliveryInstructionCode(value);
			}
		});

		/* 030 pattern description */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFrequencyCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setDespatchCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setDespatchTimingCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected SccInfo createImpl() {
		return new SccInfo();
	}
}
