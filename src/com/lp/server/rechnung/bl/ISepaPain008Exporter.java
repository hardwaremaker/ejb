package com.lp.server.rechnung.bl;

import java.util.List;

import com.lp.server.finanz.service.SepaXmlExportResult;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.system.service.TheClientDto;

public interface ISepaPain008Exporter {

	SepaXmlExportResult doExport(
			List<LastschriftvorschlagDto> lastschriftvorschlaege, TheClientDto theClientDto);
	
	SepaXmlExportResult checkExport(
			List<LastschriftvorschlagDto> lastschriftvorschlaege, TheClientDto theClientDto);
}
