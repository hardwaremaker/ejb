package com.lp.server.schema.easydata.stm;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Product")
public class XMLProduct {

	@XmlAttribute(name = "Barcode")
	private String barcode;
	@XmlAttribute(name = "Quantity")
	private BigDecimal quantity;
	@XmlAttribute(name = "Timestamp")
	@XmlSchemaType(name = "dateTime")
	private XMLGregorianCalendar timestamp;
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public XMLGregorianCalendar getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(XMLGregorianCalendar timestamp) {
		this.timestamp = timestamp;
	}
}
