package com.lp.service.edifact.parser;

import java.math.BigDecimal;

import com.lp.service.edifact.EdifactSegment;
import com.lp.service.edifact.IValueSetter;
import com.lp.service.edifact.IVisitor;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.ParseQuantityException;
import com.lp.service.edifact.schema.QtyInfo;

public class QtySegment extends EdifactSegment<QtyInfo> {
	public QtySegment() throws EdifactException {
		this(new QtyInfo());
	}
	
	public QtySegment(QtyInfo qtyInfo) throws EdifactException {
		super("QTY");
		set(qtyInfo);

		/* 010 party function code */
		addData(3, new IValueSetter() {
			public void set(String value) {
				get().setTypeCode(value);
			}
		});
		addComposite(35, true, new IValueSetter() {
			public void set(String value) throws ParseQuantityException {
				get().setQuantity(value);
				convertQuantity();
			}
		});
		addComposite(3, true, new IValueSetter() {
			public void set(String value) throws EdifactException {
				get().setUnitCode(value);
				convertQuantity();
			}
		});
	}

	protected void convertQuantity() throws ParseQuantityException {
		try {
			get().setAmount(new BigDecimal(get().getQuantity()));
		} catch(NumberFormatException e) {
			throw new ParseQuantityException(e, 0, 0);
		}
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	protected QtyInfo createImpl() {
		return new QtyInfo();
	}
}
