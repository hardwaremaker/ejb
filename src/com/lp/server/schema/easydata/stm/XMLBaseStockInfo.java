package com.lp.server.schema.easydata.stm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class XMLBaseStockInfo {

	@XmlElement(name = "Tour")
	private List<XMLTour> tours;

	public List<XMLTour> getTours() {
		if (tours == null) {
			tours = new ArrayList<XMLTour>();
		}
		return tours;
	}
	
}
