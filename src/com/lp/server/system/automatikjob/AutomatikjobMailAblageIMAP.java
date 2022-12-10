package com.lp.server.system.automatikjob;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandFac;
import com.lp.server.system.service.VersandauftragDto;

public class AutomatikjobMailAblageIMAP extends AutomatikjobBasis {

	public AutomatikjobMailAblageIMAP() {
		super();
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("Start Mailablage " + theClientDto.getMandant() + " " + this.hashCode());
		getVersandFac().performJobImap(theClientDto);
/*
		// Anmerkung: der Versandauftrag hat derzeit kein Feld Mandant -> die 10 Auftraege koennen von verschiedenen Mandanten sein
		// und es werden daher eventuell weniger Auftraege pro Timer verarbeitet!
		VersandauftragDto[] auftraege = getVersandFac().versandauftragFindAblage(10);
		myLogger.info("Offene Auftraege fuer IMAPablage: " + auftraege.length );
		for(int i=0; i<auftraege.length; i++) {
			// ablegen
			if (auftraege[i].getStatusCNr().equals(VersandFac.STATUS_TEILERLEDIGT))
				getVersandFac().ablageIMAP(auftraege[i].getIId(), theClientDto.getMandant());
		} */
		
		return false;
	}

}
