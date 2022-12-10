package com.lp.server.lieferschein.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Local;
import javax.xml.datatype.DatatypeConfigurationException;

import com.lp.server.lieferschein.service.PaketVersandAntwortDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LieferscheinId;

@Local
public interface IPaketVersandFacLocal {
	PaketVersandAntwortDto sendeLieferschein(LieferscheinId lieferscheinId, 
			TheClientDto theClientDto) throws RemoteException, DatatypeConfigurationException;

	/**
	 * Kann der jeweilige PaketVersanddienst diesen Lieferschein versenden?
	 * @param lieferscheinId
	 * @param theClientDto
	 * @return true wenn der PaketVersanddienst den angegebenen Lieferschein
	 *   an den Frachtfuehrer uebermitteln kann
	 * @throws RemoteException
	 */
	boolean isLieferscheinVersendbar(
			LieferscheinId lieferscheinId, TheClientDto theClientDto) throws RemoteException;
}
