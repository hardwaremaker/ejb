package com.lp.service.edifact.parser;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.ParseQuantityException;
import com.lp.service.edifact.schema.DocInfo;

public class DocSegment extends EdifactSegment<DocInfo> {
	public DocSegment() {
		this(new DocInfo());
	}
	
	public DocSegment(DocInfo docInfo) {
		super("DOC");
		set(docInfo);
		
		/* 010 document/message name */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setNameCode(value);
			}
		});
		addComposite(17, true, new IValueSetter() {
			public void set(String value) {
				get().setIdentificationCode(value);
			}
		});

		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setResponsibleAgencyCode(value);
			}
		});

		addComposite(35, true, new IValueSetter() {
			public void set(String value) {
				get().setName(value);
			}
		});

		/* 020 document/message details */
		addData(35, new IValueSetter() {
			public void set(String value) {
				get().setIdentifier(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setStatusCode(value);
			}
		});
		addComposite(70, true, new IValueSetter() {
			public void set(String value) {
				get().setSourceDescription(value);
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) {
				get().setLanguageName(value);
			}
		});
		addComposite(9, true, new IValueSetter() {
			public void set(String value) {
				get().setVersion(value);
			}
		});
		addComposite(6, true, new IValueSetter() {
			public void set(String value) {
				get().setRevision(value);
			}
		});

		/* 020 document/message details */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setMediumType(value);
			}
		});

		/* 020 document/message details */
		addData(2, new IValueSetter() {
			public void set(String value) throws ParseQuantityException {
				try  {
					Integer i = Integer.parseInt(value);
					get().setCopiesQuantity(i);				
				} catch(NumberFormatException e) {
					throw new ParseQuantityException(e, 0, 0);									
				}
			}
		});

		/* 020 document/message details */
		addData(2, new IValueSetter() {
			public void set(String value) throws ParseQuantityException {
				try  {
					Integer i = Integer.parseInt(value);
					get().setOriginalQuantity(i);				
				} catch(NumberFormatException e) {
					throw new ParseQuantityException(e, 0, 0);									
				}
			}
		});
	}
	
	@Override
	protected DocInfo createImpl() {
		return new DocInfo();
	}
	
	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
}
