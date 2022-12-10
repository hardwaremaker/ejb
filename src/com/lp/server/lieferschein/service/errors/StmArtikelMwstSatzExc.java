package com.lp.server.lieferschein.service.errors;

import com.lp.server.artikel.service.ArtikelDto;

public class StmArtikelMwstSatzExc extends StmException {
	private static final long serialVersionUID = 6649676828732313110L;

	private ArtikelDto artikelDto;
	
	public StmArtikelMwstSatzExc(ArtikelDto artikelDto) {
		super("Artikel '" + artikelDto.getCNr() + "' kann keinen Mwst-Satz hinterlegt.");
		this.artikelDto = artikelDto;
		setExcCode(StmExceptionCode.ARTIKEL_OHNE_MWST_SATZ);
	}
	
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}
}
