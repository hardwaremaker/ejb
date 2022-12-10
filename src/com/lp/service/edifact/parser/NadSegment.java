package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.NadInfo;

public class NadSegment extends EdifactSegment<NadInfo> {
	
	public NadSegment() {
		this(new NadInfo());
	}
	
	public NadSegment(NadInfo nadInfo) {
		super("NAD");
		set(nadInfo); 
		
		/* 010 party function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setPartyFunctionCode(value);
			}
		});
		/* 020 party identification details */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setPartyIdentifier(value);
			}
		});
		addComposite(17, true, new IValueSetter() {
			public void set(String value) {
				get().setIdentification(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setResponsibleAgency(value);
			}
		});
		/* 030 name and address */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setName(0, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setName(1, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setName(2, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setName(3, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setName(4, value);
			}
		});
		/* 040 party name */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setPartyName(0, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setPartyName(1, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setPartyName(2, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setPartyName(3, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setPartyName(4, value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setPartyNameFormatCode(value);
			}
		});
		/* 050 street */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setStreet(0, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setStreet(1, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setStreet(2, value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setStreet(3, value);
			}
		});
		/* 060 cityname */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setCityName(value);
			}
		});
		/* 070 country sub-entity details */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setCountrySubEntityNameCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setCountryIndentificationCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setCountryResponsibleAgencyCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setCountrySubEntityNameCode(value);
			}
		});
		
		/* 080 postal identification code */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setPostalIdentifcationCode(value);
			}
		});

		/* 090 postal identification code */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setCountryNameCode(value);
			}
		});
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected NadInfo createImpl() {
		return new NadInfo();
	}
}
