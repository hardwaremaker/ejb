package com.lp.server.system.automatikjob;

import com.lp.server.system.service.TheClientDto;

public class AutomatikjobMailversand extends AutomatikjobBasis {

	public AutomatikjobMailversand() {
		super();
	}

	@Override
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("Start Mailversand " + theClientDto.getMandant() + " " + this.hashCode());
		getVersandFac().performJobSmtp(theClientDto);
		return false;
	}

}
