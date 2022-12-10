package com.lp.server.system.jcr.service.docnode;

import java.text.SimpleDateFormat;

import com.lp.server.finanz.service.MahnlaufDto;

public class DocNodeMahnlauf extends DocNodeFolderId {

	private static final long serialVersionUID = 7828204397399179929L;

	public DocNodeMahnlauf(MahnlaufDto mahnlaufDto) {
		super(BELEGART_MAHNLAUF, mahnlaufDto.getIId());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String sDate = dateFormat.format(mahnlaufDto.getTAnlegen());
		setShowingValue(sDate);
	}
}
