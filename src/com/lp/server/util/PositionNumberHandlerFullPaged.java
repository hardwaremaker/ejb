package com.lp.server.util;

import java.rmi.RemoteException;

import com.lp.server.auftrag.service.AuftragpositionFac;

public class PositionNumberHandlerFullPaged extends PositionNumberHandlerPaged {
	private AuftragpositionFac positionFac ;
	
	public PositionNumberHandlerFullPaged() {
	}
	
	public PositionNumberHandlerFullPaged(PositionNumberAdapter adapter, AuftragpositionFac fac) {
		this.positionFac = fac ;
		initializePositionNummer(adapter); 
	}
	
	@Override
	protected Integer initializeFirstPositionNummer(
			PositionNumberAdapter adapter) {
		try {
			return positionFac.getPositionNummer(adapter.getIId()) ;
		} catch (RemoteException e) {
			return null ;
		}
	}
}
