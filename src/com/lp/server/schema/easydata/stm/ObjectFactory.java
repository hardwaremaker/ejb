package com.lp.server.schema.easydata.stm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _StockMovement_QNAME = new QName("STM");
	
	public ObjectFactory() {
	}
	
	public XMLActivity createXMLActivity() {
		return new XMLActivity();
	}

	public XMLStockMovement createXMLStockMovement() {
		return new XMLStockMovement();
	}
	
	public XMLProduct createXMLProduct() {
		return new XMLProduct();
	}
	
	public XMLTour createXMLTour() {
		return new XMLTour();
	}
	
	@XmlElementDecl(name = "STM")
	public JAXBElement<XMLStockMovement> createSTM(XMLStockMovement value) {
		return new JAXBElement<XMLStockMovement>(_StockMovement_QNAME, XMLStockMovement.class, null, value);
	}
}
