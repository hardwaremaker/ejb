package com.lp.server.system.ejbfac;

import java.util.List;

import javax.ejb.Local;

import com.lp.server.system.service.KundenTelefonSuchergebnisDto;
import com.lp.server.system.service.VerfuegbareHostsDto;

@Local
public interface PhoneNumberFacLocal {
	List<KundenTelefonSuchergebnisDto> search(String phonenumber, boolean isModified);

	List<VerfuegbareHostsDto> getClientList(String hostName);	
}
