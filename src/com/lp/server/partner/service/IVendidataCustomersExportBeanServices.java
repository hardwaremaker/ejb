package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;

public interface IVendidataCustomersExportBeanServices {

	public TheClientDto getTheClientDto();

	public List<KundeDto> kundeFindAll();
	
	public List<KostenstelleDto> kostenstelleFindAll() throws RemoteException;

	public Integer generiere4VendingCustomerId(Integer iId) throws RemoteException;
}
