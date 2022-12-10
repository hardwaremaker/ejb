package com.lp.server.system.service;

import java.io.Serializable;

public class ForecastImportCallOff implements IForecastImportFile<byte[]>, Serializable {
	private static final long serialVersionUID = -5292450799935240391L;
	private byte[] content;
	private String filename;
	
	public ForecastImportCallOff(String filename, byte[] content) {
		this.filename = filename;
		this.content = content;
	}
	
	@Override
	public ForecastImportFileType getFiletype() {
		return ForecastImportFileType.CallOff;
	}

	@Override
	public byte[] getContent() {
		return content;
	}
	
	@Override
	public String getFilename() {
		return filename;
	}
}
