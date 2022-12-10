package com.lp.server.system.jcr.service.docnode;

import java.util.List;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.MahnlaufDto;
import com.lp.server.partner.service.PartnerDto;

public class DocNodeSepaExportLastschriftvorschlag extends DocNodeSepaExport {
	private static final long serialVersionUID = -3561697004913814850L;

	private MahnlaufDto mahnlaufDto;

	public DocNodeSepaExportLastschriftvorschlag(BankverbindungDto bankverbindungDto, PartnerDto partnerDto,
			Integer gJahr, MahnlaufDto mahnlaufDto) {
		super(bankverbindungDto, partnerDto, gJahr);
		this.mahnlaufDto = mahnlaufDto;
	}
	
	@Override
	public List<DocNodeBase> getHierarchy() {
		List<DocNodeBase> hierarchy = super.getHierarchy();
		hierarchy.add(new DocNodeMahnlauf(mahnlaufDto));
		
		return hierarchy;
	}
}
