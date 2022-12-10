package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.LocInfo;

public class LocSegment extends EdifactSegment<LocInfo> {
	public LocSegment() {
		this(new LocInfo());
	}
	
	public LocSegment(LocInfo locInfo) {
		super("LOC");
		set(locInfo);
		
		/* 010 function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setFunctionCode(value);
			}
		});

		/* 020 identification */
		addData(25, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(0).setNameCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(0).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(0).setResponsibleAgencyCode(value);
			}
		});
		addComposite(256, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(0).setName(value);
			}
		});

		/* 030 identification one */
		addData(25, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(1).setNameCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(1).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(1).setResponsibleAgencyCode(value);
			}
		});
		addComposite(70, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(1).setName(value);
			}
		});

		/* 040 identification one */
		addData(25, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(2).setNameCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(2).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(2).setResponsibleAgencyCode(value);
			}
		});
		addComposite(70, true, new IValueSetter() {
			public void set(String value) {
				get().getIdentification(2).setName(value);
			}
		});

		/* 050 identification one */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setRelationCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected LocInfo createImpl() {
		return new LocInfo();
	}
}
