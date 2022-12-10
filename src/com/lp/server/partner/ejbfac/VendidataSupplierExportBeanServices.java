package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import com.lp.server.partner.service.IVendidataSuppliersExportBeanServices;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;

public class VendidataSupplierExportBeanServices implements IVendidataSuppliersExportBeanServices {
	
	private TheClientDto theClientDto;
	private LieferantFac lieferantFac;
	private SystemFac systemFac;

	public VendidataSupplierExportBeanServices(LieferantFac lieferantFac, SystemFac systemFac, TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		this.lieferantFac = lieferantFac;
		this.systemFac = systemFac;
	}

	@Override
	public List<KostenstelleDto> kostenstelleFindAll() throws RemoteException {
		return Arrays.asList(systemFac.kostenstelleFindByMandant(getTheClientDto().getMandant()));
	}

	@Override
	public List<LieferantDto> lieferantFindAll() {
		return lieferantFac.lieferantFindByMandantCnr(getTheClientDto());
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public Integer generiere4VendingSupplierId(Integer lieferantIId) throws RemoteException {
		return lieferantFac.generiere4VendingSupplierId(lieferantIId, getTheClientDto());
	}
	
}
