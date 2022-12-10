package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.schema.UnhInfo;

public class UnhSegment extends EdifactSegment<UnhInfo> {
	public UnhSegment() {
		this(new UnhInfo());
	}
	
	public UnhSegment(UnhInfo unhInfo) {
		super("UNH");
		set(unhInfo);

		/* 010 document/message name */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setReferenceNumber(value);
			}
		});

		/* 020 document/message name */
		addData(6, new IValueSetter() {
			public void set(String value) {
				get().setType(value);
			}
		});
		addComposite(3, false, new IValueSetter() {
			public void set(String value) {
				get().setVersion(value);
			}
		});
		addComposite(3, false, new IValueSetter() {
			public void set(String value) {
				get().setRelease(value);
			}
		});
		addComposite(3, false, new IValueSetter() {
			public void set(String value) {
				get().setControllingAgency(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setAssociationCode(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setCodeListDirectoryVersion(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setSubfunction(value);
			}
		});

		/* 030 document/message name */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setAccessReference(value);
			}
		});
		
		/* 040 document/message name */
		addData(2, new IValueSetter() {
			public void set(String value) {
				get().setSequenceTransfer(value);
			}
		});
		addComposite(1, true, new IValueSetter() {
			public void set(String value) {
				get().setFirstLastTransfer(value);
			}
		});
		
		/* 050 subset identification */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setSubsetIdentification(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setSubsetVersion(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setSubsetRelease(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setSubsetControllingAgency(value);
			}
		});

		/* 060 subset identification */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setGuidelineIdentification(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setGuidelineVersion(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setGuidelineRelease(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setGuidelineControllingAgency(value);
			}
		});		

		/* 070 scenario identification */
		addData(14, new IValueSetter() {
			public void set(String value) {
				get().setScenarioIdentification(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setScenarioVersion(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setScenarioRelease(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setScenarioControllingAgency(value);
			}
		});		
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected UnhInfo createImpl() {
		return new UnhInfo();
	}
}
