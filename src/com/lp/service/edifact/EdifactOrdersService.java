package com.lp.service.edifact;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.system.service.KennungType;
import com.lp.server.system.service.TheClientDto;

public class EdifactOrdersService implements IOrdersEjbService {
//	private final ArtikelBean artikelBean = new ArtikelBean();
	private final TheClientDto theClientDto;
	private final ArtikelFac artikelFac;
	private final KundeFac kundeFac;
	
	public EdifactOrdersService(ArtikelFac artikelFac, KundeFac kundeFac, TheClientDto theClientDto) {
		this.artikelFac = artikelFac;
		this.kundeFac = kundeFac;
		this.theClientDto = theClientDto;
	}
	
	@Override
	public List<ArtikelDto> listArtikelFindByCustomerNr(String customerItemNumber) {
		List<ArtikelDto> artikels = artikelFac
				.artikelFindByReferenzCNrMandantCNr(
				customerItemNumber, "ZE-", theClientDto);
		return artikels;
	}

	@Override
	public ArtikelDto artikelFindByCustomerNr(String customerItemNumber) {
		List<ArtikelDto> artikels = listArtikelFindByCustomerNr(customerItemNumber);
		if (artikels.size() == 1) {
			return artikels.get(0);
		}
		
		return null;
	}

	@Override
	public List<KundeDto> kundeFindByIdentification(KennungType kennung, String identification) {
		List<KundeDto> dtos = kundeFac.kundeFindByKennungType(
				kennung, identification, theClientDto);
		return dtos;
	}

	@Override
	public KundeDto kundeFindByAddress(String name1, String strasse1, String ortsname, String plz, String laenderCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
