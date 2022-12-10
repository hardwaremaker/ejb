package com.lp.server.artikel.ejbfac;

import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.artikel.service.WebPartSearchResult;

public interface IWebPartSearchController {

	WebPartSearchResult findByPartNumber(String partNumber) throws PartSearchUnexpectedResponseExc;
	
	WebPartSearchResult findByManufacturerPartNumber(String manuPartNumber) throws PartSearchUnexpectedResponseExc;
	
	WebPartSearchResult findByKeyword(String keyword) throws PartSearchUnexpectedResponseExc;
}
