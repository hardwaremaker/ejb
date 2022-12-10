package com.lp.server.system.jcr.service.docnode;

import java.util.List;

import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.PartnerDto;

public class DocNodeSepaExportZahlungsvorschlaglauf extends DocNodeSepaExport {
	private static final long serialVersionUID = 7342182936540610632L;

	private ZahlungsvorschlaglaufDto zahlungsvorschlaglauf;

	public DocNodeSepaExportZahlungsvorschlaglauf(BankverbindungDto bankverbindungDto, PartnerDto partnerDto, 
			Integer gJahr, ZahlungsvorschlaglaufDto zvDto) {
		super(bankverbindungDto, partnerDto, gJahr);
		zahlungsvorschlaglauf = zvDto;
	}
	
	@Override
	public List<DocNodeBase> getHierarchy() {
		List<DocNodeBase> hierarchy = super.getHierarchy();
		hierarchy.add(new DocNodeZahlungsvorschlaglauf(zahlungsvorschlaglauf));
		
		return hierarchy;
	}
}
