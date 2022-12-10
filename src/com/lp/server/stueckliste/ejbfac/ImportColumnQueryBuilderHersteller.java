package com.lp.server.stueckliste.ejbfac;

import java.util.List;

import com.lp.server.artikel.service.ArtikelFac;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.Helper;

public class ImportColumnQueryBuilderHersteller implements IImportColumnQueryBuilder {

	private static final String QUERY = Hersteller + ".c_nr = '{VALUE}'";
	
	@Override
	public boolean isTotalMatch() {
		return false;
	}

	@Override
	public String buildJoinQuery() {
		return JOIN_HERSTELLER;
	}

	@Override
	public String buildWhereQuery(String columnValue, StklImportSpezifikation spez) {
		return QUERY.replaceAll("\\{VALUE\\}", Helper.cutString(columnValue, ArtikelFac.MAX_HERSTELLER_NAME));
	}

	@Override
	public List<String> getDeeperColumnQueryBuilders() {
		return null;
	}

}
