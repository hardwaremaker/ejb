package com.lp.server.forecast.bl;

import jxl.Cell;

import com.lp.util.EJBExceptionLP;

public interface ICallOffXLSTransformer {

	XlsForecastPosition getPosition(Cell[] cells) throws EJBExceptionLP;
	
	XlsForecastOffset getOffset(Cell[] cells) throws EJBExceptionLP;
	
	String getForecastartCnr();
	
	int getColumnBestellnummer();
	
	int getColumnArtikelnummer();
	
	int getColumnLieferdatum();
	
	int getColumnMenge();
	
	int getDaysOfOffset();
}
