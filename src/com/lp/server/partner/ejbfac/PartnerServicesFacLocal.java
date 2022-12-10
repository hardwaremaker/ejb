package com.lp.server.partner.ejbfac;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.LPDatenSubreport;

@Local
public interface PartnerServicesFacLocal {

	public LPDatenSubreport getSubreportAllerMitzudruckendenPartnerkommentare(
			Integer partnerIId, boolean bKunde, String belegartCNr,
			TheClientDto theClientDto);

	/**
	 * Gibt Subreport-Daten zurueck, in denen alle Partnerkommentare des Partners
	 * enthalten sind, die in mindestens einem Belegt mitzudrucken sind.
	 * 
	 * @param partnerIId
	 * @param bKunde
	 * @param theClientDto
	 * @return
	 */
	public LPDatenSubreport getSubreportAllerPartnerkommentare(
			Integer partnerIId, boolean bKunde,	TheClientDto theClientDto);

}
