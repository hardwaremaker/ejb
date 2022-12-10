package com.lp.server.schema.opentrans_1_0;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.schema.opentrans_1_0.order.ObjectFactory;
import com.lp.server.schema.opentrans_1_0.order.XmlOtORDER;
import com.lp.server.util.XMLMarshaller;

public class OpenTransOrderMarshaller extends XMLMarshaller<XmlOtORDER> {

	public OpenTransOrderMarshaller() throws JAXBException {
		super();
	}

	@Override
	public String marshal(XmlOtORDER jaxbElement) throws JAXBException, SAXException {
		return marshal(new String[] {}, new ObjectFactory().createOrder(jaxbElement));
	}

	@Override
	protected Class<XmlOtORDER> getClazz() {
		return XmlOtORDER.class;
	}

}
