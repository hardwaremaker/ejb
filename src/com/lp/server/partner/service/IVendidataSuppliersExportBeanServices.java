package com.lp.server.partner.service;

import java.rmi.RemoteException;
import java.util.List;

import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;

public interface IVendidataSuppliersExportBeanServices {
	
	TheClientDto getTheClientDto();

	List<KostenstelleDto> kostenstelleFindAll() throws RemoteException;

	List<LieferantDto> lieferantFindAll();
	
	Integer generiere4VendingSupplierId(Integer lieferantIId) throws RemoteException;

}
