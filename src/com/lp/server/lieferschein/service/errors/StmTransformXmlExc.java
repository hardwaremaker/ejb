package com.lp.server.lieferschein.service.errors;

public class StmTransformXmlExc extends StmException {
	private static final long serialVersionUID = 4049270955499997154L;

	public StmTransformXmlExc(Throwable t) {
		super("XML Transformation fehlgeschlagen", t);
		setExcCode(StmExceptionCode.XML_TRANSFORMATION);
	}

}
