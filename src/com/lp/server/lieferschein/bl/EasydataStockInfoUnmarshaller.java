package com.lp.server.lieferschein.bl;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.schema.easydata.stm.XMLBaseStockInfo;
import com.lp.server.util.XMLUnmarshaller;

public class EasydataStockInfoUnmarshaller<T extends XMLBaseStockInfo> extends XMLUnmarshaller<T> {

	private Class<T> xmlClass;
	
	public EasydataStockInfoUnmarshaller(Class<T> xmlClass) {
		super();
		this.xmlClass = xmlClass;
	}

	public T unmarshal(String xmlDaten) throws JAXBException, SAXException {
		return unmarshal(xmlDaten, xmlClass);
	}
}
