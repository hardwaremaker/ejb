package com.lp.server.forecast.ejbfac;

import java.util.List;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.service.edifact.IEjbService;

public class EdifactDelforService implements IEjbService {

	private TheClientDto theClientDto;
	private ArtikelFac artikelFac;
	private KundeFac kundeFac;
	
	public EdifactDelforService(TheClientDto theClientDto, ArtikelFac artikelFac, KundeFac kundeFac) {
		this.theClientDto = theClientDto;
		this.artikelFac = artikelFac;
		this.kundeFac = kundeFac;
	}
	
	protected TheClientDto getTheClientDto() {
		return theClientDto;
	}
	
	@Override
	public List<ArtikelDto> listArtikelFindByCustomerNr(
			String customerItemNumber) {
		return artikelFac.artikelFindByCKBezOhneExc(customerItemNumber, getTheClientDto());
	}
	
	@Override
	public ArtikelDto artikelFindByCustomerNr(String customerItemNumber) {
		List<ArtikelDto> artikels = listArtikelFindByCustomerNr(customerItemNumber);
		if(artikels == null || artikels.isEmpty()) return null;
		if(artikels.size() != 1) {
			return null;
		}
		
		return artikels.get(0);
	}

	@Override
	public KundeDto kundeFindByIdentification(String identification) {
		return kundeFac.kundeFindByLieferantenCnrMandantCnrNull(
				identification, getTheClientDto().getMandant());
	}

	@Override
	public KundeDto kundeFindByAddress(String name1, String strasse1,
			String ortsname, String plz, String laenderCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
