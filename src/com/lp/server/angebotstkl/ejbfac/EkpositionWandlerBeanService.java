package com.lp.server.angebotstkl.ejbfac;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.angebotstkl.service.IEkpositionWandlerBeanServices;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.ArtikellieferantstaffelDto;
import com.lp.server.system.service.TheClientDto;

public class EkpositionWandlerBeanService implements IEkpositionWandlerBeanServices, Serializable {

	private static final long serialVersionUID = -5074107659903711254L;

	private AngebotstklFac angebotstklFac;
	private ArtikelFac artikelFac;
	
	private TheClientDto theClientDto;
	
	public EkpositionWandlerBeanService(TheClientDto theClientDto, AngebotstklFac angebotstklFac, ArtikelFac artikelFac) {
		this.theClientDto = theClientDto;
		this.angebotstklFac = angebotstklFac;
		this.artikelFac = artikelFac;
	}

	@Override
	public TheClientDto gettheClientDto() {
		return theClientDto;
	}

	@Override
	public ArtikelDto artikelFindByPrimaryKey(Integer artikelIId) {
		return artikelFac.artikelFindByPrimaryKey(artikelIId, gettheClientDto());
	}

	@Override
	public EinkaufsangebotpositionDto einkaufsangebotpositionFindByPrimaryKey(Integer positionIId) throws RemoteException {
		return angebotstklFac.einkaufsangebotpositionFindByPrimaryKey(positionIId);
	}

	@Override
	public List<Integer> einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(Integer einkaufsangebotIId) {
		return angebotstklFac.einkaufsangebotpositionenIIdFindByEinkaufsangebotIId(einkaufsangebotIId);
	}

	@Override
	public void updateEinkaufsangebotposition(EinkaufsangebotpositionDto positionDto) throws RemoteException {
		angebotstklFac.updateEinkaufsangebotposition(positionDto);
	}

	@Override
	public void removeEinkaufsangebotposition(EinkaufsangebotpositionDto positionDto) throws RemoteException {
		angebotstklFac.removeEinkaufsangebotposition(positionDto);
	}

	@Override
	public Integer createArtikel(ArtikelDto artikelDto) throws RemoteException {
		return artikelFac.createArtikel(artikelDto, gettheClientDto());
	}

	@Override
	public EinkaufsangebotDto einkaufsangebotFindByPrimaryKey(Integer einkaufsangebotIId) throws RemoteException {
		return angebotstklFac.einkaufsangebotFindByPrimaryKey(einkaufsangebotIId);
	}

	@Override
	public Integer createArtikellieferant(ArtikellieferantDto artikellieferantDto) throws RemoteException {
		return artikelFac.createArtikellieferant(artikellieferantDto, gettheClientDto());
	}

	@Override
	public Integer createArtikellieferantstaffel(ArtikellieferantstaffelDto staffelDto) throws RemoteException {
		return artikelFac.createArtikellieferantstaffel(staffelDto, gettheClientDto());
	}

	@Override
	public List<EinkaufsangebotpositionDto> einkaufsangebotpositionenFindByPrimaryKeys(Integer[] iIds) {
		return angebotstklFac.einkaufsangebotpositionenFindByPrimaryKeys(iIds);
	}

	@Override
	public ArtikelDto artikelFindByCNr(String cNr) throws RemoteException {
		return artikelFac.artikelFindByCNrOhneExc(cNr, gettheClientDto());
	}
}
