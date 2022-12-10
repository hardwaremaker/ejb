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
@XmlType(name = "Tour")
public class XMLTour {

	@XmlAttribute(name = "Serialnumber")
	private Integer serialNumber;
	@XmlAttribute(name = "Personalnumber")
	private Integer personalNumber;
	@XmlAttribute(name = "Tourdate")
	@XmlSchemaType(name = "dateTime")
	private XMLGregorianCalendar tourDate;
	@XmlElement(name = "Activity")
	private List<XMLActivity> activities;
	
	public Integer getSerialNumber() {
		return serialNumber;
	}
	
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public Integer getPersonalNumber() {
		return personalNumber;
	}
	
	public void setPersonalNumber(Integer personalNumber) {
		this.personalNumber = personalNumber;
	}
	
	public XMLGregorianCalendar getTourDate() {
		return tourDate;
	}
	
	public void setTourDate(XMLGregorianCalendar tourDate) {
		this.tourDate = tourDate;
	}
	
	public List<XMLActivity> getActivities() {
		if (activities == null) {
			activities = new ArrayList<XMLActivity>();
		}
		return activities;
	}
	
}
