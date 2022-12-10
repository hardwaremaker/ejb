package com.lp.service.edifact;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.system.service.KennungType;

public interface IOrdersEjbService {
	List<ArtikelDto> listArtikelFindByCustomerNr(String customerItemNumber);
	ArtikelDto artikelFindByCustomerNr(String customerItemNumber);

	List<KundeDto> kundeFindByIdentification(KennungType kennung, String identification);
	KundeDto kundeFindByAddress(String name1, String strasse1, String ortsname, String plz, String laenderCode);
}
