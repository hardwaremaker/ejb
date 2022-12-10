package com.lp.server.system.service;

public interface IForecastImportFile<T> {
	String getFilename();
	ForecastImportFileType getFiletype();
	T getContent();
}
