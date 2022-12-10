package com.lp.server.artikel.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.WebPartSearchResult;
import com.lp.server.artikel.service.WebabfrageArtikellieferantUpdateProperties;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;
import com.lp.util.EJBExceptionLP;

@Local
public interface WebPartSearchServiceFac {

	WebPartSearchResult findNoExc(ArtikelId artikelId, LieferantId lieferantId, TheClientDto theClientDto);
	
	WebPartSearchResult find(ArtikelId artikelId, LieferantId lieferantId, TheClientDto theClientDto);

	ArtikellieferantDto updateOrCreateArtikellieferantByWebPart(WebabfrageArtikellieferantUpdateProperties updateProperties,
			TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	
}
