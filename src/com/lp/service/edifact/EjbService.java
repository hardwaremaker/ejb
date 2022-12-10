package com.lp.service.edifact;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.partner.service.KundeDto;

public class EjbService implements IEjbService {

	@Override
	public List<ArtikelDto> listArtikelFindByCustomerNr(
			String customerItemNumber) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArtikelDto artikelFindByCustomerNr(String customerItemNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KundeDto kundeFindByIdentification(String identification) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KundeDto kundeFindByAddress(String name1, String strasse1,
			String ortsname, String plz, String laenderCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
