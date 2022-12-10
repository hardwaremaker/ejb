package com.lp.server.schema.easydata.stm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Activity")
public class XMLActivity {

	@XmlAttribute(name = "Name")
	private XMLActivityName name;
	@XmlAttribute(name = "TimestampLogin")
	@XmlSchemaType(name = "dateTime")
	private XMLGregorianCalendar timestampLogin;
	@XmlAttribute(name = "TimestampLogout")
	@XmlSchemaType(name = "dateTime")
	private XMLGregorianCalendar timestampLogout;
	@XmlAttribute(name = "Autoindex")
	private Integer autoindex;
	@XmlAttribute(name = "Reference")
	private String reference;
	@XmlAttribute(name = "Target")
	private String target;
	@XmlElement(name = "Product")
	private List<XMLProduct> products;
	
	public XMLActivityName getName() {
		return name;
	}
	public void setName(XMLActivityName name) {
		this.name = name;
	}
	public XMLGregorianCalendar getTimestampLogin() {
		return timestampLogin;
	}
	public void setTimestampLogin(XMLGregorianCalendar timestampLogin) {
		this.timestampLogin = timestampLogin;
	}
	public XMLGregorianCalendar getTimestampLogout() {
		return timestampLogout;
	}
	public void setTimestampLogout(XMLGregorianCalendar timestampLogout) {
		this.timestampLogout = timestampLogout;
	}
	public Integer getAutoindex() {
		return autoindex;
	}
	public void setAutoindex(Integer autoindex) {
		this.autoindex = autoindex;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<XMLProduct> getProducts() {
		if (products == null) {
			products = new ArrayList<XMLProduct>();
		}
		return products;
	}
	
}
