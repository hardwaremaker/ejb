package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.ImdInfo;

public class ImdSegment extends EdifactSegment<ImdInfo> {
	public ImdSegment() {
		this(new ImdInfo());
	}
	
	public ImdSegment(ImdInfo imdInfo) {
		super("IMD");
		set(imdInfo);
		
		/* 010 description format code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFormatCode(value);
			}
		});
		
		/* 020 item characteristic */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setCharacteristicCode(value);
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

		/* 030 item description */
		addData(17, new IValueSetter() {
			public void set(String value) {
				get().setDescriptionCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setDescriptionIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setDescriptionResponsibleAgencyCode(value);
			}
		});
		addComposite(256, true, new IValueSetter() {
			public void set(String value) {
				get().setDescription(0, value);
			}
		});
		addComposite(256, true, new IValueSetter() {
			public void set(String value) {
				get().setDescription(1, value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setLanguageNameCode(value);
			}
		});

		/* 040 item description */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setSurfaceLayerCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected ImdInfo createImpl() {
		return new ImdInfo();
	}
}
