package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.PiaInfo;

public class PiaSegment extends EdifactSegment<PiaInfo> {
	public PiaSegment() {
		this(new PiaInfo());
	}

	public PiaSegment(PiaInfo piaInfo) {
		super("PIA");
		set(piaInfo);
	
		/* 010 product identifier code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setIdentifierCode(value);
			}
		});

		/* 020 item info 1 */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(0).setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(0).setTypeIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(0).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(0).setResponsibleAgencyCode(value);
			}
		});

		/* 030 item info 2 */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(1).setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(1).setTypeIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(1).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(1).setResponsibleAgencyCode(value);
			}
		});

		/* 040 item info 3 */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(2).setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(2).setTypeIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(2).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(2).setResponsibleAgencyCode(value);
			}
		});
		
		/* 050 item info 4 */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(3).setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(3).setTypeIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(3).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(3).setResponsibleAgencyCode(value);
			}
		});
		
		/* 060 item info 5 */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(4).setItemIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(4).setTypeIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(4).setIdentificationCode(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().getItemInfo(4).setResponsibleAgencyCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected PiaInfo createImpl() {
		return new PiaInfo();
	}
}
