package com.lp.server.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public abstract class XMLMarshaller<T> {

	private Marshaller marshaller;
	
	public XMLMarshaller() throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(getClazz().getPackage().getName());
		marshaller = jaxbContext.createMarshaller();
	}

	public abstract String marshal(T jaxbElement) throws JAXBException, SAXException;
	
	protected abstract Class<T> getClazz();
	
	protected Marshaller getMarshaller() {
		return marshaller;
	}
	
//
//	protected String marshal(String xsdSchema, JAXBElement<T> jaxbElement, Class<T> clazz)
//			throws JAXBException, SAXException {
//		
//		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
//		Schema schema = (xsdSchema == null || xsdSchema.trim().length() == 0)
//				? null : schemaFactory.newSchema(new StreamSource(new StringReader(xsdSchema)));
//		JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
//		return marshal(jaxbContext, schema, jaxbElement);
//	}
//
//	
//	protected String marshal(JAXBContext jaxbContext, Schema schema, T jaxbElement)
//			throws JAXBException {
//		
//		StringWriter writer = new StringWriter();
//		Marshaller marshaller = jaxbContext.createMarshaller();
//		marshaller.setSchema(schema);
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		marshaller.marshal(jaxbElement, writer);
//		
//		return writer.toString();
//	}
//	
//	protected String marshal(JAXBContext jaxbContext, Schema schema, JAXBElement<T> jaxbElement)
//			throws JAXBException {
//		
//		StringWriter writer = new StringWriter();
//		Marshaller marshaller = jaxbContext.createMarshaller();
//		marshaller.setSchema(schema);
//		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		marshaller.marshal(jaxbElement, writer);
//		
//		return writer.toString();
//	}

	
	
	protected String marshal(String xsdSchema, JAXBElement<T> jaxbElement)
			throws JAXBException, SAXException {
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		Schema schema = (xsdSchema == null || xsdSchema.trim().length() == 0)
				? null : schemaFactory.newSchema(new StreamSource(new StringReader(xsdSchema)));
		return marshal(schema, jaxbElement);
	}

	protected String marshal(String[] xsdSchemas, JAXBElement<T> jaxbElement)
			throws JAXBException, SAXException {
		if (xsdSchemas == null || xsdSchemas.length == 0) {
			return marshalBySources(null, jaxbElement);
		}
		
		StreamSource[] streamSources = new StreamSource[xsdSchemas.length];
		for (int i=0; i < xsdSchemas.length; i++) {
			streamSources[i] = new StreamSource(new StringReader(xsdSchemas[i]));
		}
		return marshalBySources(streamSources, jaxbElement);
	}

	protected String marshalBySources(StreamSource[] sources, JAXBElement<T> jaxbElement)
			throws JAXBException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		Schema schema = sources == null ? null : schemaFactory.newSchema(sources);
		
		return marshal(schema, jaxbElement);
	}

	protected String marshal(Schema schema, JAXBElement<T> jaxbElement)
			throws JAXBException {
		
		StringWriter writer = new StringWriter();
		marshaller.setSchema(schema);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(jaxbElement, writer);
		
		return writer.toString();
	}
}
