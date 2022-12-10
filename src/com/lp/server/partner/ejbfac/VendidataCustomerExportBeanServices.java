package com.lp.server.partner.ejbfac;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import com.lp.server.partner.service.IVendidataCustomersExportBeanServices;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.system.service.TheClientDto;

public class VendidataCustomerExportBeanServices implements	IVendidataCustomersExportBeanServices {

	private TheClientDto theClientDto;
	private KundeFac kundeFac;
	private SystemFac systemFac;
	
	public VendidataCustomerExportBeanServices(KundeFac kundeFac, SystemFac systemFac,
			TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
		this.kundeFac = kundeFac;
		this.systemFac = systemFac;
	}

	@Override
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}

	@Override
	public List<KundeDto> kundeFindAll() {
		return kundeFac.kundeFindByMandantCnr(getTheClientDto());
	}

	@Override
	public List<KostenstelleDto> kostenstelleFindAll() throws RemoteException {
		return Arrays.asList(systemFac.kostenstelleFindByMandant(getTheClientDto().getMandant()));
	}
	
	@Override
	public Integer generiere4VendingCustomerId(Integer kundeIId) throws RemoteException {
		return kundeFac.generiere4VendingCustomerId(kundeIId, getTheClientDto());
	}
}
