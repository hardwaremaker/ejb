package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.LinInfo;

public class LinSegment extends EdifactSegment<LinInfo> {

	public LinSegment() {
		this(new LinInfo()); 
	}
	
	public LinSegment(LinInfo linInfo) {
		super("LIN");
		set(linInfo);
		
		/* 010 line item identifier */
		addData(6, new IValueSetter() {
			public void set(String value) {
				get().setLineItemIdentifier(value);
			}
		});

		/* 020 line item identifier */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setActionCode(value);
			}
		});

		/* 030 line item identifier */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setItemTypeIdentificationCode(value);
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

		/* 040 line item identifier */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setSublineIdenticatorCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setSublineItemIdentifier(value);
			}
		});		

		/* 050 line item identifier */
		addData(2, new IValueSetter() {
			public void set(String value) {
				get().setConfigurationLevelNumber(value);
			}
		});

		/* 060 line item identifier */
		addData(2, new IValueSetter() {
			public void set(String value) {
				get().setConfigurationOperationCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected LinInfo createImpl() {
		return new LinInfo();
	}
}
