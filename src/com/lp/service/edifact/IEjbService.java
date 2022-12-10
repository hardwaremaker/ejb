package com.lp.service.edifact;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;

public interface IEjbService {

	List<ArtikelDto> listArtikelFindByCustomerNr(String customerItemNumber);
	ArtikelDto artikelFindByCustomerNr(String customerItemNumber);
	KundeDto kundeFindByIdentification(String identification);
	KundeDto kundeFindByAddress(String name1, String strasse1, String ortsname, String plz, String laenderCode);
}
