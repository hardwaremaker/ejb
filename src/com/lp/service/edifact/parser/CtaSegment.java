package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.CtaInfo;

public class CtaSegment extends EdifactSegment<CtaInfo> {
	public CtaSegment() {
		this(new CtaInfo());
	}
	
	public CtaSegment(CtaInfo ctaInfo) {
		super("CTA");
		set(ctaInfo);
		
		/* 010 contact function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});

		/* 020 contact function code */
		addData(17, new IValueSetter() {
			public void set(String value) {
				get().setDepartmentCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setDepartmentName(value);
			}
		});		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected CtaInfo createImpl() {
		return new CtaInfo();
	}
}
