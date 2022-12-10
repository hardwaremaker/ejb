package com.lp.server.lieferschein.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

@Local
public interface LieferscheinFacLocal {

	public void setzeStatusLieferschein(Integer iIdLieferscheinI,
			String sStatusI, Integer iIdRechnungI, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

}
