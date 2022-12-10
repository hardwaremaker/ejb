package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.schema.GisInfo;

public class GisSegment extends EdifactSegment<GisInfo> {
	public GisSegment() throws EdifactException {
		this(new GisInfo());
	}
	
	public GisSegment(GisInfo gisInfo) throws EdifactException {
		super("GIS");
		set(gisInfo);
		
		/* 010 transcmit a processing indicator */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setIndicatorDescriptionCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setResponsibleAgencyCode(value);
			}
		});
		addComposite(17, true, new IValueSetter() {
			public void set(String value) {
				get().setTypeDescriptionCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected GisInfo createImpl() {
		return new GisInfo();
	}
}
